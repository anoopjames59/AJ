/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchClass;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public final class P8SearchUtil
/*     */ {
/*     */   public static final String RANK = "Rank";
/*     */   
/*     */   public static boolean isSearchingAllClasses(List<? extends SearchTemplate.SearchClass> classes)
/*     */   {
/*  34 */     if ((classes == null) || (classes.size() < 1)) {
/*  35 */       return true;
/*     */     }
/*  37 */     boolean searchingAll = false;
/*  38 */     if (classes.size() == 1) {
/*  39 */       SearchTemplate.SearchClass subclass = (SearchTemplate.SearchClass)classes.get(0);
/*  40 */       String classSymName = subclass.getName();
/*  41 */       SearchTemplateBase.ObjectType objectType = subclass.getObjectType();
/*  42 */       boolean rootClass = ((objectType == SearchTemplateBase.ObjectType.document) && (classSymName.equals("Document"))) || ((objectType == SearchTemplateBase.ObjectType.folder) && (classSymName.equals("Folder")));
/*  43 */       if ((rootClass) && (subclass.isSearchSubclasses())) {
/*  44 */         searchingAll = true;
/*     */       }
/*     */     }
/*  47 */     return searchingAll;
/*     */   }
/*     */   
/*     */   public static boolean isSearchingInOneSubclassTree(List<? extends SearchTemplate.SearchClass> classes) {
/*  51 */     if ((classes == null) || (classes.size() < 1)) {
/*  52 */       return false;
/*     */     }
/*  54 */     boolean subclassTree = false;
/*  55 */     if (classes.size() == 1) {
/*  56 */       SearchTemplate.SearchClass subclass = (SearchTemplate.SearchClass)classes.get(0);
/*  57 */       String classSymName = subclass.getName();
/*  58 */       SearchTemplateBase.ObjectType objectType = subclass.getObjectType();
/*  59 */       boolean rootClass = ((objectType == SearchTemplateBase.ObjectType.document) && (classSymName.equals("Document"))) || ((objectType == SearchTemplateBase.ObjectType.folder) && (classSymName.equals("Folder")));
/*  60 */       if ((!rootClass) && (subclass.isSearchSubclasses())) {
/*  61 */         subclassTree = true;
/*     */       }
/*     */     }
/*  64 */     return subclassTree;
/*     */   }
/*     */   
/*     */   public static boolean isIcnMatchAllSearchClause(P8SearchDefinition.SearchClause searchClause) {
/*  68 */     P8SearchDefinition.Operator join = searchClause.getJoin();
/*  69 */     if (join != null) {
/*  70 */       return join == P8SearchDefinition.Operator.and;
/*     */     }
/*  72 */     P8SearchDefinition.Clause whereClause = searchClause.getWhereClause();
/*  73 */     if (whereClause == null) {
/*  74 */       return true;
/*     */     }
/*  76 */     return ((P8SearchDefinition.Container)whereClause).getJoin() == P8SearchDefinition.Operator.and;
/*     */   }
/*     */   
/*     */   public static boolean isNewGroup(P8SearchDefinition.Container container) {
/*  80 */     P8SearchDefinition.Container parent = container.getContainer();
/*  81 */     boolean newGroup = (parent == null) || (container.getJoin() != parent.getJoin());
/*  82 */     if ((!newGroup) && (parent != null))
/*     */     {
/*  84 */       Iterator<P8SearchDefinition.Clause> i = parent.getClauses().iterator();
/*  85 */       newGroup = true;
/*  86 */       while (i.hasNext()) {
/*  87 */         if (!((P8SearchDefinition.Clause)i.next()).isContainer()) {
/*  88 */           newGroup = false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  93 */     return newGroup;
/*     */   }
/*     */   
/*     */   static boolean isWhereClauseContainerIncludeCondition(P8SearchDefinition.Container container) {
/*  97 */     boolean newGroup = isNewGroup(container);
/*  98 */     return ((newGroup) && (isWhereClauseContainerIncludeCondition(container, null, null, null, false))) || ((!newGroup) && (isWhereClauseContainerIncludeCondition(container, null, null, null, true)));
/*     */   }
/*     */   
/*     */   static boolean isWhereClauseContainerIncludeCondition(P8SearchDefinition.Container container, P8SearchDefinition.Operator joinLookup, String nameLookup, P8SearchDefinition.Operator operatorLookup, boolean strict)
/*     */   {
/* 103 */     if (joinLookup == null) {
/* 104 */       joinLookup = container.getJoin();
/* 105 */     } else if (container.getJoin() != joinLookup) {
/* 106 */       return false;
/*     */     }
/* 108 */     List<P8SearchDefinition.Clause> clauses = container.getClauses();
/* 109 */     Iterator<P8SearchDefinition.Clause> i = clauses.iterator();
/* 110 */     boolean first = true;
/* 111 */     while (i.hasNext()) {
/* 112 */       P8SearchDefinition.Clause clause = (P8SearchDefinition.Clause)i.next();
/* 113 */       if (clause.isContainer()) {
/* 114 */         if (first)
/* 115 */           return false;
/* 116 */         if (!isWhereClauseContainerIncludeCondition((P8SearchDefinition.Container)clause, joinLookup, nameLookup, operatorLookup, strict))
/* 117 */           return false;
/*     */       } else {
/* 119 */         if (!(clause instanceof P8SearchDefinition.WhereClauseCondition)) {
/* 120 */           return false;
/*     */         }
/* 122 */         P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)clause;
/* 123 */         if ((condition.getSmartOperator() == P8SearchDefinition.Operator.in) || (condition.getSmartOperator() == P8SearchDefinition.Operator.inany) || (condition.getSmartOperator() == P8SearchDefinition.Operator.notin))
/*     */         {
/*     */ 
/* 126 */           return true; }
/* 127 */         if (strict) {
/* 128 */           return false;
/*     */         }
/* 130 */         if ((condition.isHidden()) || (condition.isReadOnly()) || (condition.isRequired())) {
/* 131 */           return false;
/*     */         }
/* 133 */         P8SearchDefinition.Operator operator = condition.getOperator();
/* 134 */         if ((condition.getPropDataType() == P8SearchDefinition.DataType.typeobject) || ((operator != P8SearchDefinition.Operator.eq) && (operator != P8SearchDefinition.Operator.neq) && (operator != P8SearchDefinition.Operator.in))) {
/* 135 */           return false;
/*     */         }
/* 137 */         String name = condition.getPropName();
/* 138 */         if ((nameLookup != null) && ((!name.equals(nameLookup)) || (operator != operatorLookup))) {
/* 139 */           return false;
/*     */         }
/* 141 */         if (nameLookup == null)
/* 142 */           nameLookup = name;
/* 143 */         if (operatorLookup == null)
/* 144 */           operatorLookup = operator;
/*     */       }
/* 146 */       first = false;
/*     */     }
/*     */     
/* 149 */     if ((container.getJoin() == P8SearchDefinition.Operator.or) && (operatorLookup != P8SearchDefinition.Operator.in) && (operatorLookup != P8SearchDefinition.Operator.eq))
/* 150 */       return false;
/* 151 */     if ((container.getJoin() == P8SearchDefinition.Operator.and) && (operatorLookup != P8SearchDefinition.Operator.in) && (operatorLookup != P8SearchDefinition.Operator.neq)) {
/* 152 */       return false;
/*     */     }
/* 154 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isWhereClauseContainerRangeCondition(P8SearchDefinition.Container container)
/*     */   {
/* 159 */     List<P8SearchDefinition.Clause> clauses = container.getClauses();
/* 160 */     if (clauses.size() != 2) {
/* 161 */       return false;
/*     */     }
/* 163 */     P8SearchDefinition.Clause from = (P8SearchDefinition.Clause)clauses.get(0);
/* 164 */     P8SearchDefinition.Clause to = (P8SearchDefinition.Clause)clauses.get(1);
/* 165 */     if ((!(from instanceof P8SearchDefinition.WhereClauseCondition)) || (from.isContainer()) || (!(to instanceof P8SearchDefinition.WhereClauseCondition)) || (to.isContainer())) {
/* 166 */       return false;
/*     */     }
/* 168 */     P8SearchDefinition.WhereClauseCondition fromCondition = (P8SearchDefinition.WhereClauseCondition)from;
/* 169 */     P8SearchDefinition.WhereClauseCondition toCondition = (P8SearchDefinition.WhereClauseCondition)to;
/* 170 */     if (!fromCondition.getPropSymbolicName().equals(toCondition.getPropSymbolicName())) {
/* 171 */       return false;
/*     */     }
/* 173 */     if ((fromCondition.isHidden()) || (fromCondition.isReadOnly()) || (fromCondition.isRequired())) {
/* 174 */       return false;
/*     */     }
/* 176 */     if ((toCondition.isHidden()) || (toCondition.isReadOnly()) || (toCondition.isRequired())) {
/* 177 */       return false;
/*     */     }
/* 179 */     P8SearchDefinition.DataType dataType = fromCondition.getPropDataType();
/* 180 */     if ((dataType != P8SearchDefinition.DataType.typedate) && (dataType != P8SearchDefinition.DataType.typedouble) && (dataType != P8SearchDefinition.DataType.typelong)) {
/* 181 */       return false;
/*     */     }
/* 183 */     boolean between = container.getJoin() == P8SearchDefinition.Operator.and;
/* 184 */     P8SearchDefinition.Operator fromOperator = fromCondition.getOperator();
/* 185 */     P8SearchDefinition.Operator toOperator = toCondition.getOperator();
/*     */     
/* 187 */     return (fromOperator == P8SearchDefinition.Operator.gte) && (toOperator == P8SearchDefinition.Operator.lte);
/*     */   }
/*     */   
/*     */   public static String convertP8SearchDefinitionOperator(P8SearchDefinition.Operator operator) {
/*     */     String op;
/* 192 */     switch (operator) {
/*     */     case eq: 
/* 194 */       op = "EQUAL";
/* 195 */       break;
/*     */     case neq: 
/* 197 */       op = "NOTEQUAL";
/* 198 */       break;
/*     */     case like: 
/* 200 */       op = "LIKE";
/* 201 */       break;
/*     */     case notlike: 
/* 203 */       op = "NOTLIKE";
/* 204 */       break;
/*     */     case lt: 
/* 206 */       op = "LESS";
/* 207 */       break;
/*     */     case lte: 
/* 209 */       op = "LESSOREQUAL";
/* 210 */       break;
/*     */     case gt: 
/* 212 */       op = "GREATER";
/* 213 */       break;
/*     */     case gte: 
/* 215 */       op = "GREATEROREQUAL";
/* 216 */       break;
/*     */     case in: 
/* 218 */       op = "IN";
/* 219 */       break;
/*     */     case notin: 
/* 221 */       op = "NOTIN";
/* 222 */       break;
/*     */     case inany: 
/* 224 */       op = "INANY";
/* 225 */       break;
/*     */     case isnull: 
/* 227 */       op = "NULL";
/* 228 */       break;
/*     */     case isnotnull: 
/* 230 */       op = "NOTNULL";
/* 231 */       break;
/*     */     case between: 
/* 233 */       op = "BETWEEN";
/* 234 */       break;
/*     */     case notbetween: 
/* 236 */       op = "NOTBETWEEN";
/* 237 */       break;
/*     */     case startswith: 
/* 239 */       op = "STARTSWITH";
/* 240 */       break;
/*     */     case endswith: 
/* 242 */       op = "ENDSWITH";
/* 243 */       break;
/*     */     case contains: 
/* 245 */       op = "CONTAINS";
/* 246 */       break;
/*     */     default: 
/* 248 */       op = "EQUAL";
/*     */     }
/*     */     
/* 251 */     return op;
/*     */   }
/*     */   
/*     */   public static P8SearchDefinition.Operator convertP8SearchTemplateOperator(String operator) { P8SearchDefinition.Operator op;
/*     */     P8SearchDefinition.Operator op;
/* 256 */     if (operator.equals("EQUAL")) {
/* 257 */       op = P8SearchDefinition.Operator.eq; } else { P8SearchDefinition.Operator op;
/* 258 */       if (operator.equals("NOTEQUAL")) {
/* 259 */         op = P8SearchDefinition.Operator.neq; } else { P8SearchDefinition.Operator op;
/* 260 */         if (operator.equals("LIKE")) {
/* 261 */           op = P8SearchDefinition.Operator.like; } else { P8SearchDefinition.Operator op;
/* 262 */           if (operator.equals("NOTLIKE")) {
/* 263 */             op = P8SearchDefinition.Operator.notlike; } else { P8SearchDefinition.Operator op;
/* 264 */             if (operator.equals("LESS")) {
/* 265 */               op = P8SearchDefinition.Operator.lt; } else { P8SearchDefinition.Operator op;
/* 266 */               if (operator.equals("LESSOREQUAL")) {
/* 267 */                 op = P8SearchDefinition.Operator.lte; } else { P8SearchDefinition.Operator op;
/* 268 */                 if (operator.equals("GREATER")) {
/* 269 */                   op = P8SearchDefinition.Operator.gt; } else { P8SearchDefinition.Operator op;
/* 270 */                   if (operator.equals("GREATEROREQUAL")) {
/* 271 */                     op = P8SearchDefinition.Operator.gte; } else { P8SearchDefinition.Operator op;
/* 272 */                     if (operator.equals("IN")) {
/* 273 */                       op = P8SearchDefinition.Operator.in; } else { P8SearchDefinition.Operator op;
/* 274 */                       if (operator.equals("NOTIN")) {
/* 275 */                         op = P8SearchDefinition.Operator.notin; } else { P8SearchDefinition.Operator op;
/* 276 */                         if (operator.equals("INANY")) {
/* 277 */                           op = P8SearchDefinition.Operator.inany; } else { P8SearchDefinition.Operator op;
/* 278 */                           if (operator.equals("NULL")) {
/* 279 */                             op = P8SearchDefinition.Operator.isnull; } else { P8SearchDefinition.Operator op;
/* 280 */                             if (operator.equals("NOTNULL")) {
/* 281 */                               op = P8SearchDefinition.Operator.isnotnull; } else { P8SearchDefinition.Operator op;
/* 282 */                               if (operator.equals("BETWEEN")) {
/* 283 */                                 op = P8SearchDefinition.Operator.between; } else { P8SearchDefinition.Operator op;
/* 284 */                                 if (operator.equals("NOTBETWEEN")) {
/* 285 */                                   op = P8SearchDefinition.Operator.notbetween; } else { P8SearchDefinition.Operator op;
/* 286 */                                   if (operator.equals("STARTSWITH")) {
/* 287 */                                     op = P8SearchDefinition.Operator.startswith; } else { P8SearchDefinition.Operator op;
/* 288 */                                     if (operator.equals("ENDSWITH")) {
/* 289 */                                       op = P8SearchDefinition.Operator.endswith; } else { P8SearchDefinition.Operator op;
/* 290 */                                       if (operator.equals("CONTAINS")) {
/* 291 */                                         op = P8SearchDefinition.Operator.contains;
/*     */                                       } else
/* 293 */                                         op = P8SearchDefinition.Operator.eq;
/*     */                                     } } } } } } } } } } } } } } } } }
/* 295 */     return op;
/*     */   }
/*     */   
/*     */   public static String convertP8SearchDefinitionDataType(P8SearchDefinition.DataType dataType) {
/*     */     String xsType;
/* 300 */     switch (dataType) {
/*     */     case typestring: 
/* 302 */       xsType = "xs:string";
/* 303 */       break;
/*     */     case typedate: 
/* 305 */       xsType = "xs:date";
/* 306 */       break;
/*     */     case typeboolean: 
/* 308 */       xsType = "xs:boolean";
/* 309 */       break;
/*     */     case typedouble: 
/* 311 */       xsType = "xs:double";
/* 312 */       break;
/*     */     case typelong: 
/* 314 */       xsType = "xs:integer";
/* 315 */       break;
/*     */     case typeobject: 
/* 317 */       xsType = "xs:object";
/* 318 */       break;
/*     */     default: 
/* 320 */       xsType = "xs:string";
/*     */     }
/*     */     
/* 323 */     return xsType;
/*     */   }
/*     */   
/*     */   public static P8SearchDefinition.DataType convertP8SearchTemplateDataType(String dataType) { P8SearchDefinition.DataType dt;
/*     */     P8SearchDefinition.DataType dt;
/* 328 */     if (dataType.equals("xs:string")) {
/* 329 */       dt = P8SearchDefinition.DataType.typestring; } else { P8SearchDefinition.DataType dt;
/* 330 */       if ((dataType.equals("xs:date")) || (dataType.equals("xs:timestamp"))) {
/* 331 */         dt = P8SearchDefinition.DataType.typedate; } else { P8SearchDefinition.DataType dt;
/* 332 */         if (dataType.equals("xs:boolean")) {
/* 333 */           dt = P8SearchDefinition.DataType.typeboolean; } else { P8SearchDefinition.DataType dt;
/* 334 */           if (dataType.equals("xs:double")) {
/* 335 */             dt = P8SearchDefinition.DataType.typedouble; } else { P8SearchDefinition.DataType dt;
/* 336 */             if (dataType.equals("xs:integer")) {
/* 337 */               dt = P8SearchDefinition.DataType.typelong; } else { P8SearchDefinition.DataType dt;
/* 338 */               if (dataType.equals("xs:guid")) {
/* 339 */                 dt = P8SearchDefinition.DataType.typeguid; } else { P8SearchDefinition.DataType dt;
/* 340 */                 if (dataType.equals("xs:object")) {
/* 341 */                   dt = P8SearchDefinition.DataType.typeobject;
/*     */                 } else
/* 343 */                   dt = P8SearchDefinition.DataType.typestring;
/*     */               } } } } } }
/* 345 */     return dt;
/*     */   }
/*     */   
/*     */   public static String convertLikeValue(String value, P8SearchDefinition.Operator operator) {
/* 349 */     if ((value != null) && (value.length() > 0)) {
/* 350 */       if (operator == P8SearchDefinition.Operator.startswith) {
/* 351 */         value = value + "%";
/* 352 */       } else if (operator == P8SearchDefinition.Operator.endswith) {
/* 353 */         value = "%" + value;
/*     */       } else {
/* 355 */         value = "%" + value + "%";
/*     */       }
/*     */     }
/* 358 */     return value;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8SearchUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */