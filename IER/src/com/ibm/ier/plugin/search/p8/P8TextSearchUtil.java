/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.ibm.ecm.serviceability.Logger;
/*     */ import com.ibm.ier.plugin.search.util.SearchCriterion;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchCriterion;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchType;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class P8TextSearchUtil
/*     */ {
/*  46 */   private static final char[] VERITY_SPECIAL_CHARS = { ',', '(', ')', '[', '{', '=', '>', '<', '!', '`', '\'', '@', '/', '\\', '&', '|', '-' };
/*  47 */   private static final char[] TEXT_SPECIAL_CHARS = { '^', ':', '_', '(', ')', '~', '#', '$', '%', '&', '@', '[', ']', '{', '}', '!', '\\', '=', '/', '<', '>', ';', '\'', '|', '.', ',', '+', '-' };
/*  48 */   private static final String[] CONTENT_RESERVED_WORDS = { "AND", "OR", "NOT", "&&", "||" };
/*     */   
/*     */   private static final char QUESTION_MARK = '?';
/*     */   
/*     */   public static final char QUOTE_CHAR = '"';
/*     */   private static final char WILDCARD_CHAR = '*';
/*     */   private static final char MINUS_CHAR = '-';
/*     */   public static final String MINUS = "-";
/*     */   
/*     */   public static P8SearchDefinition.Clause createContentClause(SearchTemplate.TextSearchCriterion criterion, boolean cascade, P8SearchDefinition.SearchClause searchClause, boolean processToken)
/*     */     throws IOException
/*     */   {
/*  60 */     P8SearchDefinition.Clause contentClause = createEmptyContentClauseItem(criterion, searchClause);
/*  61 */     String text = criterion.getText();
/*  62 */     if ((text == null) || (text.length() < 1)) {
/*  63 */       return contentClause;
/*     */     }
/*  65 */     P8SearchDefinition.GroupAction groupAction = getGroupAction(criterion.getOperator());
/*  66 */     if (groupAction == P8SearchDefinition.GroupAction.none) {
/*  67 */       List<String> tokens = getTokens(criterion, cascade, processToken);
/*  68 */       int size = tokens.size();
/*  69 */       if (size < 1) {
/*  70 */         return contentClause;
/*     */       }
/*  72 */       P8SearchDefinition.Container container = new P8SearchDefinition.Container(P8SearchDefinition.Operator.and);
/*  73 */       container.setSearchClause(searchClause);
/*  74 */       for (int i = 0; i < size; i++) {
/*  75 */         String token = (String)tokens.get(i);
/*  76 */         P8SearchDefinition.ContentTerm term = createContentTerm(token, true, cascade);
/*  77 */         P8SearchDefinition.RequiredState requiredState = term.getRequiredState();
/*  78 */         P8SearchDefinition.ContentClauseItem item = new P8SearchDefinition.ContentClauseItem();
/*  79 */         item.addContentTerm(term);
/*  80 */         item.setGroupAction(groupAction);
/*  81 */         item.setRequiredState(requiredState);
/*  82 */         item.setUserText(token);
/*  83 */         item.setSearchClause(searchClause);
/*  84 */         if (size == 1) {
/*  85 */           item.setItemId(criterion.getItemId());
/*  86 */           contentClause = item;
/*  87 */           break;
/*     */         }
/*  89 */         if (i == 0)
/*  90 */           contentClause = container;
/*  91 */         if ((i % 2 != 0) && (i + 1 < size)) {
/*  92 */           P8SearchDefinition.Container subContainer = new P8SearchDefinition.Container(P8SearchDefinition.Operator.and);
/*  93 */           subContainer.setSearchClause(searchClause);
/*  94 */           subContainer.addClause(item);
/*  95 */           container.addClause(subContainer);
/*  96 */           container = subContainer;
/*     */         } else {
/*  98 */           container.addClause(item);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 103 */       P8SearchDefinition.ContentClauseItem item = newContentClauseItem(criterion.getOperator());
/* 104 */       item.setSearchClause(searchClause);
/* 105 */       item.setItemId(criterion.getItemId());
/* 106 */       convertTextSearchCriterion(criterion, item, cascade, processToken);
/* 107 */       contentClause = item;
/*     */     }
/*     */     
/* 110 */     return contentClause;
/*     */   }
/*     */   
/*     */   public static P8SearchDefinition.Clause createContentClause(List<SearchCriterion> propTextCriteria, boolean cascade, P8SearchDefinition.SearchClause searchClause, boolean processToken)
/*     */     throws IOException
/*     */   {
/* 116 */     return createContentClause(propTextCriteria, cascade, searchClause, processToken, P8SearchDefinition.Operator.and);
/*     */   }
/*     */   
/*     */   public static P8SearchDefinition.Clause createContentClause(List<SearchCriterion> propTextCriteria, boolean cascade, P8SearchDefinition.SearchClause searchClause, boolean processToken, P8SearchDefinition.Operator op) throws IOException
/*     */   {
/* 121 */     if ((propTextCriteria == null) || (propTextCriteria.size() < 1)) {
/* 122 */       return null;
/*     */     }
/* 124 */     P8SearchDefinition.Clause contentClause = null;
/* 125 */     P8SearchDefinition.Container container = new P8SearchDefinition.Container(op);
/* 126 */     container.setSearchClause(searchClause);
/* 127 */     int size = propTextCriteria.size();
/* 128 */     for (int i = 0; i < size; i++) {
/* 129 */       SearchCriterion criterion = (SearchCriterion)propTextCriteria.get(i);
/* 130 */       String[] values = criterion.getValues();
/* 131 */       SearchTemplate.TextSearchCriterion tempCriterion = new SearchTemplate.TextSearchCriterion((values == null) || (values.length < 1) ? "" : criterion.getValues()[0], P8SearchDefinition.GroupAction.none.toString(), 0, null, criterion.getItemId());
/* 132 */       P8SearchDefinition.Clause clause = createContentClause(tempCriterion, cascade, searchClause, processToken);
/* 133 */       populatePropertySymbolicName(clause, criterion.getName());
/* 134 */       if (size == 1) {
/* 135 */         contentClause = clause;
/* 136 */         break;
/*     */       }
/* 138 */       if (i == 0)
/* 139 */         contentClause = container;
/* 140 */       if ((i % 2 != 0) && (i + 1 < size)) {
/* 141 */         P8SearchDefinition.Container subContainer = new P8SearchDefinition.Container(op);
/* 142 */         subContainer.setSearchClause(searchClause);
/* 143 */         subContainer.addClause(clause);
/* 144 */         container.addClause(subContainer);
/* 145 */         container = subContainer;
/*     */       } else {
/* 147 */         container.addClause(clause);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 152 */     return contentClause;
/*     */   }
/*     */   
/*     */   private static void populatePropertySymbolicName(P8SearchDefinition.Clause contentClause, String propertySymbolicName) {
/* 156 */     if (contentClause == null) {
/* 157 */       return;
/*     */     }
/* 159 */     if (contentClause.isContainer()) {
/* 160 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)contentClause;
/* 161 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/* 162 */       for (P8SearchDefinition.Clause clause : clauses)
/* 163 */         populatePropertySymbolicName(clause, propertySymbolicName);
/*     */     } else {
/* 165 */       ((P8SearchDefinition.ContentClauseItem)contentClause).setPropertySymbolicName(propertySymbolicName);
/*     */     }
/*     */   }
/*     */   
/*     */   public static P8SearchDefinition.ContentClauseItem newContentClauseItem(String groupAction) {
/* 170 */     P8SearchDefinition.ContentClauseItem item = null;
/* 171 */     P8SearchDefinition.GroupAction operator = getGroupAction(groupAction);
/* 172 */     if (operator == P8SearchDefinition.GroupAction.in) {
/* 173 */       item = new P8SearchDefinition.VerityClauseInItem();
/* 174 */       if (groupAction.indexOf(":") > -1)
/* 175 */         ((P8SearchDefinition.VerityClauseInItem)item).setZone(groupAction.substring(groupAction.indexOf(":") + 1));
/* 176 */     } else if (operator == P8SearchDefinition.GroupAction.vql) {
/* 177 */       item = new P8SearchDefinition.VerityClauseVQLItem();
/*     */     } else {
/* 179 */       item = new P8SearchDefinition.ContentClauseItem();
/*     */     }
/* 181 */     item.setGroupAction(operator);
/*     */     
/* 183 */     return item;
/*     */   }
/*     */   
/*     */   private static P8SearchDefinition.GroupAction getGroupAction(String operator) {
/* 187 */     boolean advanced = (operator == null) || (operator.length() < 1) || (operator.equals("none"));
/*     */     
/* 189 */     return operator.startsWith("in:") ? P8SearchDefinition.GroupAction.in : advanced ? P8SearchDefinition.GroupAction.none : P8SearchDefinition.GroupAction.valueOf(operator);
/*     */   }
/*     */   
/*     */   public static void convertTextSearchCriterion(SearchTemplate.TextSearchCriterion criterion, P8SearchDefinition.ContentClauseItem item, boolean cascade, boolean processToken) throws IOException {
/* 193 */     P8SearchDefinition.GroupAction groupAction = getGroupAction(criterion.getOperator());
/* 194 */     item.setGroupAction(groupAction);
/* 195 */     item.setUserText(criterion.getText());
/* 196 */     if (groupAction == P8SearchDefinition.GroupAction.vql) {
/* 197 */       ((P8SearchDefinition.VerityClauseVQLItem)item).setVql(criterion.getText());
/*     */     } else {
/* 199 */       if (groupAction == P8SearchDefinition.GroupAction.near) {
/* 200 */         item.setSearchModifier(P8SearchDefinition.SearchModifier.proximity);
/* 201 */         item.setSearchModifierRange(criterion.getDistance());
/*     */       }
/* 203 */       List<P8SearchDefinition.ContentTerm> newTerms = new ArrayList();
/* 204 */       List<String> tokens = getTokens(criterion, cascade, processToken);
/* 205 */       for (int i = 0; i < tokens.size(); i++) {
/* 206 */         String token = (String)tokens.get(i);
/* 207 */         P8SearchDefinition.ContentTerm term = createContentTerm(token, groupAction == P8SearchDefinition.GroupAction.none, cascade);
/* 208 */         newTerms.add(term);
/*     */       }
/* 210 */       if (groupAction == P8SearchDefinition.GroupAction.none)
/* 211 */         item.setRequiredState((newTerms.size() == 1) && (((P8SearchDefinition.ContentTerm)newTerms.get(0)).isProhibited()) ? P8SearchDefinition.RequiredState.prohibited : null);
/* 212 */       item.setContentTerms(newTerms);
/*     */     }
/*     */   }
/*     */   
/*     */   private static List<String> getTokens(SearchTemplate.TextSearchCriterion criterion, boolean cascade, boolean processToken) throws IOException {
/* 217 */     List<String> tokens = new ArrayList();
/* 218 */     String text = criterion.getText();
/* 219 */     if ((text != null) && (text.length() > 0)) {
/* 220 */       P8SearchDefinition.GroupAction groupAction = getGroupAction(criterion.getOperator());
/*     */       
/* 222 */       boolean escapeSpecial = true;
/* 223 */       if ((groupAction == P8SearchDefinition.GroupAction.near) && (cascade)) {
/* 224 */         tokens = new ArrayList();
/* 225 */         tokens.add(processToken ? processToken(text, cascade, escapeSpecial, true) : text);
/*     */       } else {
/* 227 */         tokens = tokenizeContentSearchText(text, groupAction == P8SearchDefinition.GroupAction.none, cascade, processToken, escapeSpecial);
/*     */       }
/*     */     }
/* 230 */     return tokens;
/*     */   }
/*     */   
/*     */   private static P8SearchDefinition.ContentTerm createContentTerm(String term, boolean advanced, boolean cascade) {
/* 234 */     String wordVariation = null;
/* 235 */     boolean caseSensitive = false;
/* 236 */     P8SearchDefinition.RequiredState requiredState = P8SearchDefinition.RequiredState.none;
/* 237 */     boolean phrase = false;
/* 238 */     if ((advanced) && (term.length() > 1) && (term.startsWith("-"))) {
/* 239 */       requiredState = P8SearchDefinition.RequiredState.prohibited;
/* 240 */       term = term.substring(1);
/*     */     }
/* 242 */     if ((term.startsWith("\"")) && (term.endsWith("\""))) {
/* 243 */       wordVariation = P8SearchDefinition.VeritySearchOperator.none.toString();
/* 244 */       caseSensitive = true;
/* 245 */       phrase = true;
/* 246 */       term = term.substring(1, term.length() - 1);
/*     */     }
/* 248 */     else if ((term.indexOf('*') >= 0) || (term.indexOf('?') >= 0)) {
/* 249 */       wordVariation = P8SearchDefinition.VeritySearchOperator.wildcard.toString();
/*     */     } else {
/* 251 */       wordVariation = P8SearchDefinition.VeritySearchOperator.stem.toString();
/*     */     }
/* 253 */     return new P8SearchDefinition.ContentTerm(term, cascade ? null : wordVariation, cascade ? false : caseSensitive, requiredState, phrase);
/*     */   }
/*     */   
/*     */   private static List<String> tokenizeContentSearchText(String text, boolean advanced, boolean cascade, boolean processToken, boolean escapeSpecial) throws IOException {
/* 257 */     Reader strReader = new StringReader(text);
/* 258 */     Reader bufferedReader = new BufferedReader(strReader);
/* 259 */     StreamTokenizer parser = new StreamTokenizer(bufferedReader);
/* 260 */     parser.resetSyntax();
/*     */     
/* 262 */     parser.quoteChar(34);
/* 263 */     parser.wordChars(97, 122);
/* 264 */     parser.wordChars(65, 90);
/* 265 */     parser.wordChars(48, 57);
/* 266 */     parser.wordChars(39, 39);
/* 267 */     parser.wordChars(42, 42);
/* 268 */     parser.wordChars(63, 63);
/* 269 */     parser.wordChars(160, 255);
/* 270 */     char[] specialChars = getContentSpecialChars(cascade);
/* 271 */     for (int i = 0; i < specialChars.length; i++)
/* 272 */       parser.wordChars(specialChars[i], specialChars[i]);
/* 273 */     parser.whitespaceChars(0, 32);
/*     */     
/* 275 */     List<String> tokens = new ArrayList();
/* 276 */     boolean prohibitNextPhrase = false;
/* 277 */     while (parser.nextToken() != -1)
/* 278 */       if (parser.sval != null)
/*     */       {
/* 280 */         String value = parser.sval.trim();
/* 281 */         if (value.length() != 0)
/*     */         {
/* 283 */           char firstChar = value.charAt(0);
/* 284 */           boolean prohibit = (advanced) && (firstChar == '-');
/* 285 */           if ((prohibit) && (value.length() == 1)) {
/* 286 */             prohibitNextPhrase = true;
/*     */           }
/*     */           else
/*     */           {
/* 290 */             if (parser.ttype == 34) {
/* 291 */               if (processToken)
/* 292 */                 value = processToken(value, cascade, escapeSpecial, true);
/* 293 */               value = '"' + value + '"';
/* 294 */               if (prohibitNextPhrase)
/* 295 */                 value = "-" + value;
/* 296 */               tokens.add(value);
/* 297 */             } else if (parser.ttype == -3) {
/* 298 */               if (prohibit)
/* 299 */                 value = value.substring(1);
/* 300 */               if (processToken)
/* 301 */                 value = processToken(value, cascade, escapeSpecial, false);
/* 302 */               if (prohibit)
/* 303 */                 value = "-" + value;
/* 304 */               tokens.add(value);
/*     */             }
/* 306 */             prohibitNextPhrase = false;
/*     */           }
/*     */         } }
/* 309 */     if (prohibitNextPhrase) {
/* 310 */       tokens.add("\\-");
/*     */     }
/* 312 */     return tokens;
/*     */   }
/*     */   
/*     */   private static String processToken(String value, boolean cascade, boolean escapeSpecial, boolean escapeReserved)
/*     */   {
/* 317 */     if ((!cascade) && ((value.indexOf('*') >= 0) || (value.indexOf('?') >= 0)) && (value.indexOf('}') >= 0)) {
/* 318 */       value = value.replaceAll("}", "");
/*     */     }
/* 320 */     if ((cascade) && (value.length() == 1) && ((value.equals(String.valueOf('*'))) || (value.equals(String.valueOf('?'))))) {
/* 321 */       value = "\\" + value;
/* 322 */     } else if (escapeSpecial)
/* 323 */       value = escapeSpecialChars(value, cascade);
/* 324 */     value = quoteContentReservedWords(value, escapeReserved);
/* 325 */     return value;
/*     */   }
/*     */   
/*     */   private static char[] getContentSpecialChars(boolean cascade) {
/* 329 */     if (cascade) {
/* 330 */       return TEXT_SPECIAL_CHARS;
/*     */     }
/* 332 */     return VERITY_SPECIAL_CHARS;
/*     */   }
/*     */   
/*     */   private static String escapeSpecialChars(String text, boolean cascade) {
/* 336 */     StringBuilder buffer = new StringBuilder();
/* 337 */     char[] charVals = text.toCharArray();
/* 338 */     boolean questionsOnly = true;
/* 339 */     for (int i = 0; i < charVals.length; i++) {
/* 340 */       if (charVals[i] != '?') {
/* 341 */         questionsOnly = false;
/* 342 */         break;
/*     */       }
/*     */     }
/* 345 */     if (questionsOnly) {
/* 346 */       for (int i = 0; i < charVals.length; i++) {
/* 347 */         buffer.append('\\');
/* 348 */         buffer.append(charVals[i]);
/*     */       }
/*     */     } else {
/* 351 */       for (int i = 0; i < charVals.length; i++) {
/* 352 */         boolean specialChar = isContentSpecialChar(charVals[i], cascade);
/* 353 */         if (specialChar) {
/* 354 */           buffer.append('\\');
/* 355 */         } else if ((charVals[i] == '*') || (charVals[i] == '?')) {
/* 356 */           boolean afterSpecial = (i - 1 >= 0) && ((isContentSpecialChar(charVals[(i - 1)], cascade)) || (charVals[(i - 1)] == '*'));
/* 357 */           boolean beforeSpecial = (i + 1 < charVals.length) && ((isContentSpecialChar(charVals[(i + 1)], cascade)) || (charVals[(i + 1)] == '*'));
/* 358 */           if ((afterSpecial) || (beforeSpecial))
/* 359 */             buffer.append('\\');
/*     */         }
/* 361 */         buffer.append(charVals[i]);
/* 362 */         if (charVals[i] == '\'') {
/* 363 */           buffer.append(charVals[i]);
/*     */         }
/*     */       }
/*     */     }
/* 367 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private static boolean isContentSpecialChar(char c, boolean cascade) {
/* 371 */     boolean specialChar = false;
/* 372 */     char[] specialChars = getContentSpecialChars(cascade);
/* 373 */     for (int i = 0; i < specialChars.length; i++) {
/* 374 */       if (c == specialChars[i]) {
/* 375 */         specialChar = true;
/* 376 */         break;
/*     */       }
/*     */     }
/*     */     
/* 380 */     return specialChar;
/*     */   }
/*     */   
/*     */   private static String quoteContentReservedWords(String text, boolean escape) {
/* 384 */     StringTokenizer t = new StringTokenizer(text);
/* 385 */     String quotedText = "";
/* 386 */     while (t.hasMoreTokens()) {
/* 387 */       String token = t.nextToken();
/* 388 */       if (isContentReservedWord(token)) {
/* 389 */         if (escape) {
/* 390 */           token = "\\\"" + token + "\\\"";
/*     */         } else {
/* 392 */           token = "\"" + token + "\"";
/*     */         }
/*     */       }
/* 395 */       if (quotedText.length() > 0) {
/* 396 */         quotedText = quotedText + " ";
/*     */       }
/* 398 */       quotedText = quotedText + token;
/*     */     }
/*     */     
/* 401 */     return quotedText;
/*     */   }
/*     */   
/*     */   private static boolean isContentReservedWord(String text) {
/* 405 */     boolean reserved = false;
/* 406 */     for (int i = 0; i < CONTENT_RESERVED_WORDS.length; i++) {
/* 407 */       reserved = text.equalsIgnoreCase(CONTENT_RESERVED_WORDS[i]);
/* 408 */       if (reserved) {
/*     */         break;
/*     */       }
/*     */     }
/* 412 */     return reserved;
/*     */   }
/*     */   
/*     */   private static P8SearchDefinition.ContentClauseItem createEmptyContentClauseItem(SearchTemplate.TextSearchCriterion textCriterion, P8SearchDefinition.SearchClause searchClause) {
/* 416 */     String itemId = textCriterion.getItemId();
/* 417 */     P8SearchDefinition.ContentClauseItem item = (itemId != null) && (!itemId.isEmpty()) && (searchClause != null) ? searchClause.getContentClauseItem(itemId) : null;
/* 418 */     if (item == null) {
/* 419 */       item = new P8SearchDefinition.ContentClauseItem();
/* 420 */       item.setSearchClause(searchClause);
/*     */     }
/* 422 */     String op = textCriterion.getOperator();
/* 423 */     P8SearchDefinition.GroupAction groupAction = (op != null) && (op.length() > 0) ? P8SearchDefinition.GroupAction.valueOf(op) : P8SearchDefinition.GroupAction.none;
/* 424 */     item.setGroupAction(groupAction);
/* 425 */     if (groupAction == P8SearchDefinition.GroupAction.near) {
/* 426 */       item.setSearchModifier(P8SearchDefinition.SearchModifier.proximity);
/* 427 */       item.setSearchModifierRange(textCriterion.getDistance());
/*     */     }
/* 429 */     item.setRequiredState(P8SearchDefinition.RequiredState.none);
/* 430 */     List<P8SearchDefinition.ContentTerm> contentTerms = new ArrayList();
/* 431 */     contentTerms.add(new P8SearchDefinition.ContentTerm("", null, false, null, false));
/* 432 */     item.setContentTerms(contentTerms);
/* 433 */     item.setUserText(null);
/*     */     
/* 435 */     return item;
/*     */   }
/*     */   
/*     */   public static String getTextSearchType(HttpServletRequest request) {
/* 439 */     SearchTemplate.TextSearchType type = getTextSearchType(P8Util.getConnection(request), null, request);
/* 440 */     return type == null ? "" : type.toString();
/*     */   }
/*     */   
/*     */   public static String getTextSearchType(P8Connection connection, HttpServletRequest request) {
/* 444 */     SearchTemplate.TextSearchType type = getTextSearchType(connection, null, request);
/* 445 */     return type == null ? "" : type.toString();
/*     */   }
/*     */   
/*     */   public static SearchTemplate.TextSearchType getTextSearchType(P8Connection connection, String objectStoreId, HttpServletRequest request) {
/* 449 */     if (connection == null) {
/* 450 */       return null;
/*     */     }
/* 452 */     int searchType = 0;
/*     */     try {
/* 454 */       ObjectStore os = (objectStoreId == null) || (objectStoreId.length() < 1) ? connection.getObjectStore() : P8Util.getObjectStore(connection, objectStoreId);
/* 455 */       searchType = P8Util.getPropertyIntegerValue(os, "CBRSearchType");
/*     */     } catch (Exception e) {
/* 457 */       Logger.logDebug(P8TextSearchUtil.class, "getTextSearchType", request, "Failed to get the value of CBRSearchType");
/* 458 */       return null;
/*     */     }
/*     */     
/* 461 */     SearchTemplate.TextSearchType type = null;
/* 462 */     if (searchType == 1) {
/* 463 */       type = SearchTemplate.TextSearchType.verity;
/* 464 */     } else if (searchType == 2) {
/* 465 */       type = SearchTemplate.TextSearchType.cascade;
/*     */     }
/* 467 */     return type;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8TextSearchUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */