/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchCriterion;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchType;
/*     */ import com.ibm.ier.plugin.search.util.WCDateFormat;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import org.apache.commons.digester.Digester;
/*     */ import org.apache.commons.digester.RegexMatcher;
/*     */ import org.apache.commons.digester.RegexRules;
/*     */ import org.apache.commons.digester.Rule;
/*     */ import org.apache.commons.digester.SimpleRegexMatcher;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class P8SearchDefinitionParser
/*     */ {
/*     */   private static class SearchDefinitionRule
/*     */     extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes)
/*     */     {
/*  50 */       Digester digester = getDigester();
/*  51 */       P8SearchDefinition searchDefinition = new P8SearchDefinition();
/*  52 */       digester.push(searchDefinition);
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/*  57 */       getDigester().pop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InfoAndOptionsRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/*  64 */       P8SearchDefinition searchDefinition = (P8SearchDefinition)getDigester().peek();
/*  65 */       if (name.equals("version")) {
/*  66 */         searchDefinition.setDtdVersion(attributes.getValue("dtd"));
/*  67 */         searchDefinition.setSearchVersion(attributes.getValue("searchobject"));
/*  68 */       } else if (name.equals("product")) {
/*  69 */         searchDefinition.setProductName(attributes.getValue("name"));
/*  70 */         searchDefinition.setProductVersion(attributes.getValue("version"));
/*  71 */       } else if (name.equals("searchspec")) {
/*  72 */         searchDefinition.setVersionSelection(attributes.getValue("versionselection"));
/*  73 */       } else if (name.equals("template")) {
/*  74 */         searchDefinition.setTemplate(true);
/*  75 */         searchDefinition.setShowAndOrConditions(attributes.getValue("showandorconditions"));
/*  76 */         searchDefinition.setShowMaxRecords(attributes.getValue("showmaxrecords"));
/*  77 */         searchDefinition.setShowOperators(attributes.getValue("showoperators"));
/*  78 */       } else if (name.equals("objectstores")) {
/*  79 */         searchDefinition.setMergeOption(attributes.getValue("mergeoption"));
/*  80 */       } else if (name.equals("options")) {
/*  81 */         searchDefinition.setMaxRecords(attributes.getValue("maxrecords"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ObjectStoreRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/*  89 */       P8SearchDefinition searchDefinition = (P8SearchDefinition)getDigester().peek();
/*  90 */       P8SearchDefinition.SearchInObjectStore objectStore = new P8SearchDefinition.SearchInObjectStore(attributes.getValue("id"), attributes.getValue("name"));
/*  91 */       searchDefinition.addObjectStore(objectStore);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FolderRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/*  98 */       Digester digester = getDigester();
/*  99 */       P8SearchDefinition searchDefinition = (P8SearchDefinition)digester.peek();
/* 100 */       String id = attributes.getValue("id");
/* 101 */       String pathname = attributes.getValue("pathname");
/* 102 */       boolean searchSubfolders = Boolean.valueOf(attributes.getValue("searchsubfolders")).booleanValue();
/* 103 */       String view = attributes.getValue("view");
/* 104 */       P8SearchDefinition.SearchInFolder folder = new P8SearchDefinition.SearchInFolder(id, pathname, searchSubfolders, view, null);
/* 105 */       String itemId = attributes.getValue("itemid");
/* 106 */       folder.setItemId(itemId);
/* 107 */       searchDefinition.addFolder(folder);
/* 108 */       digester.push(folder);
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 113 */       getDigester().pop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FolderObjectStoreRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 120 */       P8SearchDefinition.SearchInFolder folder = (P8SearchDefinition.SearchInFolder)getDigester().peek();
/* 121 */       folder.setObjectStoreId(attributes.getValue("id"));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SearchClauseRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 128 */       Digester digester = getDigester();
/* 129 */       P8SearchDefinition searchDefinition = (P8SearchDefinition)digester.peek();
/* 130 */       P8SearchDefinition.SearchClause searchClause = new P8SearchDefinition.SearchClause();
/* 131 */       String join = attributes.getValue("join");
/* 132 */       searchClause.setJoin(join != null ? P8SearchDefinition.Operator.valueOf(join) : null);
/* 133 */       searchDefinition.addSearchClause(searchClause);
/* 134 */       digester.push(searchClause);
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 139 */       getDigester().pop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SearchClauseObjectTypeRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 146 */       P8SearchDefinition.SearchClause searchClause = (P8SearchDefinition.SearchClause)getDigester().peek();
/* 147 */       searchClause.setFrom(attributes.getValue("symname"));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SelectPropertyRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 154 */       P8SearchDefinition.SearchClause searchClause = (P8SearchDefinition.SearchClause)getDigester().peek();
/* 155 */       String sortLevelAttr = attributes.getValue("sortlevel");
/* 156 */       int sortLevel = sortLevelAttr == null ? 0 : Integer.valueOf(sortLevelAttr).intValue();
/* 157 */       P8SearchDefinition.SelectProperty selectProp = new P8SearchDefinition.SelectProperty(attributes.getValue("symname"), attributes.getValue("name"), attributes.getValue("objecttype"), sortLevel, attributes.getValue("sortorder"), attributes.getValue("alignment"));
/* 158 */       selectProp.setItemId(attributes.getValue("itemid"));
/* 159 */       searchClause.addSelectProperty(selectProp);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SelectAllPropertiesRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 166 */       P8SearchDefinition.SearchClause searchClause = (P8SearchDefinition.SearchClause)getDigester().peek();
/* 167 */       searchClause.addSelectProperty(P8SearchDefinition.SELECT_PROPERTY_ALL);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ContainerRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 174 */       Digester digester = getDigester();
/* 175 */       Object parent = digester.peek();
/* 176 */       P8SearchDefinition.Container container = new P8SearchDefinition.Container();
/* 177 */       container.setJoin(name.startsWith("verity") ? P8SearchDefinition.Operator.valueOf(name.substring(6)) : P8SearchDefinition.Operator.valueOf(name));
/* 178 */       if ((parent instanceof P8SearchDefinition.SearchClause)) {
/* 179 */         container.setSearchClause((P8SearchDefinition.SearchClause)parent);
/* 180 */         ((P8SearchDefinition.SearchClause)parent).setWhereClause(container);
/* 181 */       } else if ((parent instanceof P8SearchDefinition.ContentCriteria)) {
/* 182 */         container.setSearchClause(((P8SearchDefinition.ContentCriteria)parent).getSearchClause());
/* 183 */         ((P8SearchDefinition.ContentCriteria)parent).setContentClause(container);
/*     */       } else {
/* 185 */         container.setSearchClause(((P8SearchDefinition.Container)parent).getSearchClause());
/* 186 */         ((P8SearchDefinition.Container)parent).addClause(container);
/*     */       }
/* 188 */       digester.push(container);
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 193 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)getDigester().pop();
/* 194 */       container.setInclude(P8SearchUtil.isWhereClauseContainerIncludeCondition(container));
/* 195 */       container.setRange(P8SearchUtil.isWhereClauseContainerRangeCondition(container));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class WhereClauseConditionRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String operator, Attributes attributes) {
/* 202 */       Digester digester = getDigester();
/* 203 */       Object parent = digester.peek();
/* 204 */       P8SearchDefinition.WhereClauseCondition condition = new P8SearchDefinition.WhereClauseCondition();
/* 205 */       condition.setOperator(operator);
/* 206 */       digester.push(condition);
/* 207 */       if ((parent instanceof P8SearchDefinition.SearchClause))
/*     */       {
/* 209 */         condition.setSearchClause((P8SearchDefinition.SearchClause)parent);
/* 210 */         ((P8SearchDefinition.SearchClause)parent).setWhereClause(condition);
/*     */       }
/*     */       else {
/* 213 */         condition.setSearchClause(((P8SearchDefinition.Container)parent).getSearchClause());
/* 214 */         ((P8SearchDefinition.Container)parent).addClause(condition);
/*     */       }
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 220 */       P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)getDigester().pop();
/* 221 */       P8SearchDefinition.Operator operator = condition.getOperator();
/* 222 */       String literal = condition.getLiteral();
/* 223 */       if ((literal != null) && (literal.length() > 0)) {
/* 224 */         if ((operator == P8SearchDefinition.Operator.like) || (operator == P8SearchDefinition.Operator.notlike))
/*     */         {
/* 226 */           if (operator == P8SearchDefinition.Operator.like) {
/* 227 */             if ((literal.charAt(0) != '%') && (literal.charAt(literal.length() - 1) == '%')) {
/* 228 */               operator = P8SearchDefinition.Operator.startswith;
/* 229 */             } else if ((literal.charAt(0) == '%') && (literal.charAt(literal.length() - 1) != '%')) {
/* 230 */               operator = P8SearchDefinition.Operator.endswith;
/*     */             }
/*     */           }
/*     */           
/* 234 */           if (literal.charAt(0) == '%')
/* 235 */             literal = literal.substring(1);
/* 236 */           if ((literal.length() > 0) && (literal.charAt(literal.length() - 1) == '%')) {
/* 237 */             literal = literal.substring(0, literal.length() - 1);
/*     */           }
/* 239 */           if (literal.equals("{}")) {
/* 240 */             literal = "";
/*     */           }
/* 242 */           condition.setOperator(operator.toString());
/* 243 */           condition.setLiteral(literal);
/* 244 */         } else if (condition.getPropDataType().equals(P8SearchDefinition.DataType.typedate)) {
/* 245 */           Calendar cal = WCDateFormat.parseW3CDate(literal, null);
/* 246 */           literal = WCDateFormat.getDateString(null, cal.getTimeInMillis(), false);
/* 247 */           condition.setLiteral(literal);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class WhereCommonPlaceholderRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 256 */       Digester digester = getDigester();
/* 257 */       Object parent = digester.peek();
/* 258 */       digester.push(P8SearchDefinition.COMMON_CLAUSE);
/* 259 */       if ((parent instanceof P8SearchDefinition.SearchClause))
/*     */       {
/* 261 */         ((P8SearchDefinition.SearchClause)parent).setWhereClause(P8SearchDefinition.COMMON_CLAUSE);
/*     */       }
/*     */       else {
/* 264 */         ((P8SearchDefinition.Container)parent).addClause(P8SearchDefinition.COMMON_CLAUSE);
/*     */       }
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 270 */       getDigester().pop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class WherePropertyRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 277 */       P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)getDigester().peek();
/* 278 */       if (name.equals("whereprop")) {
/* 279 */         condition.setPropName(attributes.getValue("name"));
/* 280 */         condition.setPropSymbolicName(attributes.getValue("symname"));
/* 281 */         condition.setPropObjectType(attributes.getValue("objecttype"));
/* 282 */         condition.setItemId(attributes.getValue("itemid"));
/* 283 */         condition.setEditProperty(attributes.getValue("editproperty"));
/* 284 */         condition.setSmartOperator(attributes.getValue("smartoperator"));
/* 285 */       } else if (name.equals("propdesc")) {
/* 286 */         condition.setPropDataType(attributes.getValue("datatype"));
/* 287 */         condition.setPropHasChoices(Boolean.valueOf(attributes.getValue("haschoices")).booleanValue());
/* 288 */         condition.setPropChoiceId(attributes.getValue("choiceId"));
/* 289 */         condition.setPropHasMarkings(Boolean.valueOf(attributes.getValue("hasmarkings")).booleanValue());
/*     */       }
/*     */     }
/*     */     
/*     */     public void body(String namespace, String name, String text)
/*     */     {
/* 295 */       if (name.equals("literal")) {
/* 296 */         text = text.replace("\n", "");
/* 297 */         P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)getDigester().peek();
/* 298 */         condition.setLiteral(text);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ContentClauseRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 306 */       P8SearchDefinition.ContentCriteria contentClause = new P8SearchDefinition.ContentCriteria();
/* 307 */       Digester digester = getDigester();
/* 308 */       P8SearchDefinition.SearchClause searchClause = (P8SearchDefinition.SearchClause)digester.peek();
/* 309 */       contentClause.setSearchClause(searchClause);
/* 310 */       searchClause.setContentCriteria(contentClause);
/* 311 */       contentClause.setDialect(attributes.getValue("dialect"));
/* 312 */       contentClause.setShowContentSummary(attributes.getValue("contentsummary"));
/* 313 */       contentClause.setShowRank(attributes.getValue("rank"));
/* 314 */       contentClause.setJoin(attributes.getValue("jointype"));
/* 315 */       contentClause.setTextSearchType(name == "content" ? SearchTemplate.TextSearchType.cascade : SearchTemplate.TextSearchType.verity);
/* 316 */       digester.push(contentClause);
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 321 */       getDigester().pop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FieldRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 328 */       Digester digester = getDigester();
/* 329 */       P8SearchDefinition.ContentClauseItem item = new P8SearchDefinition.ContentClauseItem();
/* 330 */       digester.push(item);
/* 331 */       item.setPropertySymbolicName(attributes.getValue("name"));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ContentClauseItemRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 338 */       Digester digester = getDigester();
/* 339 */       Object parent = digester.peek();
/* 340 */       P8SearchDefinition.ContentClauseItem item = P8TextSearchUtil.newContentClauseItem(attributes.getValue("groupaction"));
/* 341 */       if ((parent instanceof P8SearchDefinition.ContentClauseItem)) {
/* 342 */         item.setPropertySymbolicName(((P8SearchDefinition.ContentClauseItem)parent).getPropertySymbolicName());
/* 343 */         digester.pop();
/* 344 */         parent = digester.peek();
/*     */       }
/* 346 */       digester.push(item);
/* 347 */       if ((parent instanceof P8SearchDefinition.ContentCriteria))
/*     */       {
/* 349 */         item.setSearchClause(((P8SearchDefinition.ContentCriteria)parent).getSearchClause());
/* 350 */         ((P8SearchDefinition.ContentCriteria)parent).setContentClause(item);
/*     */       }
/*     */       else {
/* 353 */         item.setSearchClause(((P8SearchDefinition.Container)parent).getSearchClause());
/* 354 */         ((P8SearchDefinition.Container)parent).addClause(item);
/*     */       }
/* 356 */       item.setGroupAction(attributes.getValue("groupaction"));
/* 357 */       item.setSearchModifier(attributes.getValue("searchmodifier"));
/* 358 */       String modifierRange = attributes.getValue("searchmodifierrange");
/* 359 */       item.setSearchModifierRange(modifierRange == null ? 0 : Integer.valueOf(modifierRange).intValue());
/* 360 */       item.setRequiredState(attributes.getValue("requiredstate"));
/* 361 */       item.setItemId(attributes.getValue("itemid"));
/* 362 */       item.setEditProperty(attributes.getValue("editproperty"));
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 367 */       P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)getDigester().pop();
/* 368 */       P8SearchDefinition.SearchClause searchClause = item.getSearchClause();
/* 369 */       if (searchClause.getContentCriteria().isCascade()) {
/* 370 */         StringBuilder text = new StringBuilder();
/* 371 */         List<P8SearchDefinition.ContentTerm> contentTerms = item.getContentTerms();
/* 372 */         for (P8SearchDefinition.ContentTerm term : contentTerms) {
/* 373 */           String value = term.getValue();
/* 374 */           if ((value != null) && (value.length() >= 1))
/*     */           {
/*     */ 
/* 377 */             if (text.length() > 0)
/* 378 */               text.append(" ");
/* 379 */             if (item.isRequiredStateProhibited()) {
/* 380 */               text.append("-");
/*     */             }
/* 382 */             if (term.isPhrase()) {
/* 383 */               text.append("\"").append(value).append("\"");
/*     */             } else
/* 385 */               text.append(value);
/*     */           } }
/* 387 */         item.setUserText(text.toString());
/*     */       } else {
/*     */         try {
/* 390 */           String userText = item.getUserText();
/* 391 */           P8SearchDefinition.GroupAction groupAction = item.getGroupAction();
/* 392 */           if ((groupAction == P8SearchDefinition.GroupAction.none) && (userText != null) && (userText.length() > 1) && (userText.startsWith("!")))
/* 393 */             userText = "-" + userText.substring(1);
/* 394 */           SearchTemplate.TextSearchCriterion criterion = new SearchTemplate.TextSearchCriterion(userText, groupAction == P8SearchDefinition.GroupAction.none ? "" : groupAction.toString(), item.getSearchModifierRange());
/* 395 */           P8TextSearchUtil.convertTextSearchCriterion(criterion, item, false, false);
/*     */         } catch (Exception e) {
/* 397 */           throw new RuntimeException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ContentItemTermRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes)
/*     */     {
/* 407 */       Digester digester = getDigester();
/* 408 */       P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)digester.peek();
/* 409 */       P8SearchDefinition.ContentTerm term = new P8SearchDefinition.ContentTerm();
/* 410 */       term.setCaseSensitive(Boolean.valueOf(attributes.getValue("case")).booleanValue());
/* 411 */       term.setPhrase(Boolean.valueOf(attributes.getValue("phrase")).booleanValue());
/* 412 */       digester.addBeanPropertySetter("*/term", "value");
/* 413 */       item.addContentTerm(term);
/* 414 */       digester.push(term);
/*     */     }
/*     */     
/*     */     public void body(String namespace, String name, String text)
/*     */     {
/* 419 */       P8SearchDefinition.ContentTerm term = (P8SearchDefinition.ContentTerm)getDigester().peek();
/* 420 */       term.setValue(text);
/*     */     }
/*     */     
/*     */     public void end(String namespace, String name)
/*     */     {
/* 425 */       getDigester().pop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ContentClauseItemTextRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 432 */       P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)getDigester().peek();
/* 433 */       if (name.equals("veritynear")) {
/* 434 */         String distance = attributes.getValue("distance");
/* 435 */         item.setSearchModifierRange(distance == null ? 0 : Integer.valueOf(distance).intValue());
/*     */       }
/*     */     }
/*     */     
/*     */     public void body(String namespace, String name, String text)
/*     */     {
/* 441 */       if (name.equals("usertext")) {
/* 442 */         P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)getDigester().peek();
/* 443 */         item.setUserText(text);
/* 444 */       } else if (name.equals("verityzonename")) {
/* 445 */         P8SearchDefinition.VerityClauseInItem item = (P8SearchDefinition.VerityClauseInItem)getDigester().peek();
/* 446 */         item.setZone(text);
/* 447 */       } else if (name.equals("verityvql")) {
/* 448 */         P8SearchDefinition.VerityClauseVQLItem item = (P8SearchDefinition.VerityClauseVQLItem)getDigester().peek();
/* 449 */         item.setVql(text);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SubclassRule extends Rule
/*     */   {
/*     */     public void begin(String namespace, String name, Attributes attributes) {
/* 457 */       P8SearchDefinition.SearchClause searchClause = (P8SearchDefinition.SearchClause)getDigester().peek();
/* 458 */       P8SearchDefinition.Subclass subclass = new P8SearchDefinition.Subclass(attributes.getValue("symname"), attributes.getValue("name"), attributes.getValue("objecttype"), attributes.getValue("editproperty"), attributes.getValue("itemid"), attributes.getValue("includesubclasses"));
/* 459 */       searchClause.addSubclass(subclass);
/*     */     }
/*     */   }
/*     */   
/*     */   public static P8SearchDefinition parse(InputStream definition) throws IOException, SAXException {
/* 464 */     Digester digester = new Digester();
/* 465 */     RegexMatcher m = new SimpleRegexMatcher();
/* 466 */     digester.setRules(new RegexRules(m));
/* 467 */     digester.setValidating(false);
/* 468 */     addSearchDefinitionParsingRules(digester);
/*     */     
/* 470 */     return (P8SearchDefinition)digester.parse(definition);
/*     */   }
/*     */   
/*     */   private static void addSearchDefinitionParsingRules(Digester digester) {
/* 474 */     digester.addRule("storedsearch", new SearchDefinitionRule(null));
/* 475 */     digester.addRule("storedsearch/version", new InfoAndOptionsRule(null));
/* 476 */     digester.addRule("storedsearch/product", new InfoAndOptionsRule(null));
/* 477 */     digester.addRule("storedsearch/searchspec", new InfoAndOptionsRule(null));
/* 478 */     digester.addRule("storedsearch/searchspec/searchtype/template", new InfoAndOptionsRule(null));
/* 479 */     digester.addRule("storedsearch/searchspec/objectstores", new InfoAndOptionsRule(null));
/* 480 */     digester.addRule("storedsearch/searchspec/options", new InfoAndOptionsRule(null));
/* 481 */     digester.addRule("storedsearch/searchspec/objectstores/objectstore", new ObjectStoreRule(null));
/* 482 */     digester.addRule("storedsearch/searchspec/searchcriteria/folders/folder", new FolderRule(null));
/* 483 */     digester.addRule("storedsearch/searchspec/searchcriteria/folders/folder/objectstore", new FolderObjectStoreRule(null));
/* 484 */     digester.addRule("storedsearch/searchspec/searchcriteria/searchclauses/searchclause", new SearchClauseRule(null));
/* 485 */     digester.addRule("storedsearch/searchspec/searchcriteria/searchclauses/searchclause/from/class", new SearchClauseObjectTypeRule(null));
/* 486 */     digester.addRule("storedsearch/searchspec/searchcriteria/searchclauses/searchclause/select/selectpropall", new SelectAllPropertiesRule(null));
/* 487 */     digester.addRule("storedsearch/searchspec/searchcriteria/searchclauses/searchclause/select/selectprops/selectprop", new SelectPropertyRule(null));
/* 488 */     digester.addRule("*/" + P8SearchDefinition.Operator.commonplaceholder, new WhereCommonPlaceholderRule(null));
/* 489 */     digester.addRule("*/" + P8SearchDefinition.Operator.and, new ContainerRule(null));
/* 490 */     digester.addRule("*/" + P8SearchDefinition.Operator.or, new ContainerRule(null));
/* 491 */     digester.addRule("*/" + P8SearchDefinition.Operator.eq, new WhereClauseConditionRule(null));
/* 492 */     digester.addRule("*/" + P8SearchDefinition.Operator.neq, new WhereClauseConditionRule(null));
/* 493 */     digester.addRule("*/" + P8SearchDefinition.Operator.gt, new WhereClauseConditionRule(null));
/* 494 */     digester.addRule("*/" + P8SearchDefinition.Operator.gte, new WhereClauseConditionRule(null));
/* 495 */     digester.addRule("*/" + P8SearchDefinition.Operator.lt, new WhereClauseConditionRule(null));
/* 496 */     digester.addRule("*/" + P8SearchDefinition.Operator.lte, new WhereClauseConditionRule(null));
/* 497 */     digester.addRule("*/" + P8SearchDefinition.Operator.in, new WhereClauseConditionRule(null));
/* 498 */     digester.addRule("*/" + P8SearchDefinition.Operator.like, new WhereClauseConditionRule(null));
/* 499 */     digester.addRule("*/" + P8SearchDefinition.Operator.notlike, new WhereClauseConditionRule(null));
/* 500 */     digester.addRule("*/" + P8SearchDefinition.Operator.isnull, new WhereClauseConditionRule(null));
/* 501 */     digester.addRule("*/" + P8SearchDefinition.Operator.isnotnull, new WhereClauseConditionRule(null));
/* 502 */     digester.addRule("*/whereprop", new WherePropertyRule(null));
/* 503 */     digester.addRule("*/whereprop/propdesc", new WherePropertyRule(null));
/* 504 */     digester.addRule("*/literal", new WherePropertyRule(null));
/* 505 */     digester.addRule("storedsearch/searchspec/searchcriteria/searchclauses/searchclause/content", new ContentClauseRule(null));
/* 506 */     digester.addRule("*/item", new ContentClauseItemRule(null));
/* 507 */     digester.addRule("*/field", new FieldRule(null));
/* 508 */     digester.addRule("*/term", new ContentItemTermRule(null));
/* 509 */     digester.addRule("storedsearch/searchspec/searchcriteria/searchclauses/searchclause/veritycontent", new ContentClauseRule(null));
/* 510 */     digester.addRule("*/verityand", new ContainerRule(null));
/* 511 */     digester.addRule("*/verityor", new ContainerRule(null));
/* 512 */     digester.addRule("*/verityitem", new ContentClauseItemRule(null));
/* 513 */     digester.addRule("*/verityitem/usertext", new ContentClauseItemTextRule(null));
/* 514 */     digester.addRule("*/verityitem/verityitemdata/veritynear", new ContentClauseItemTextRule(null));
/* 515 */     digester.addRule("*/verityitem/verityitemdata/verityin/verityzones/verityzone/verityzonename", new ContentClauseItemTextRule(null));
/* 516 */     digester.addRule("*/verityitem/verityitemdata/verityvql", new ContentClauseItemTextRule(null));
/* 517 */     digester.addRule("storedsearch/searchspec/searchcriteria/searchclauses/searchclause/subclasses/subclass", new SubclassRule(null));
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8SearchDefinitionParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */