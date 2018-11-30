/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.SearchClause;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.SearchInFolder;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.Subclass;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplate;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplateDocument;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.VersionOption;
/*     */ import com.ibm.ier.plugin.util.model.IERConditionValues;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.collections.BidiMap;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
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
/*     */ public class IERConditionsUtil
/*     */ {
/*     */   public static IERConditionValues[] readConditionXML(String xml)
/*     */     throws Exception
/*     */   {
/*  40 */     Document doc = XPathUtil.loadDocument(new ByteArrayInputStream(xml.getBytes("UTF-8")));
/*     */     
/*  42 */     List<Node> conditions = XPathUtil.selectNodes(doc, "/wcm:response/wcm:objectset/wcm:customobject");
/*  43 */     IERConditionValues[] conditionsArray = new IERConditionValues[conditions.size()];
/*  44 */     int conditionCounter = 0;
/*  45 */     for (Node condition : conditions) {
/*  46 */       IERConditionValues conditionValues = new IERConditionValues();
/*     */       
/*  48 */       List<Node> properties = XPathUtil.selectNodes(condition, "wcm:properties/wcm:property");
/*  49 */       String[] propertyNames = new String[properties.size()];
/*  50 */       String[] labelNames = new String[properties.size()];
/*  51 */       String[] selectables = new String[properties.size()];
/*  52 */       String[] operators = new String[properties.size()];
/*  53 */       String[] types = new String[properties.size()];
/*  54 */       String[][] values = new String[properties.size()][3];
/*     */       
/*  56 */       int counter = 0;
/*  57 */       for (Node property : properties) {
/*  58 */         propertyNames[counter] = XPathUtil.getXPathResult(property, "wcm:symname");
/*  59 */         labelNames[counter] = XPathUtil.getXPathResult(property, "wcm:displayname");
/*  60 */         selectables[counter] = XPathUtil.getXPathResult(property, "wcm:selectable");
/*  61 */         String type = XPathUtil.getXPathResult(property, "wcm:propertytype");
/*  62 */         types[counter] = (type != null ? (String)IERConditionValues.reverseTypeMap.get(type) : null);
/*  63 */         String operator = XPathUtil.getXPathResult(property, "wcm:operator");
/*  64 */         if ((operator != null) && (operator.length() > 0)) {
/*  65 */           operator = (String)IERConditionValues.operatorMap.get(EncoderUtil.decodeFromXML(operator));
/*     */         }
/*  67 */         operators[counter] = operator;
/*     */         
/*  69 */         String id = XPathUtil.getXPathResult(property, "wcm:id");
/*  70 */         String value = XPathUtil.getXPathResult(property, "wcm:value");
/*  71 */         String jsonValue = XPathUtil.getXPathResult(property, "wcm:jsonValue");
/*  72 */         values[counter][0] = value;
/*  73 */         values[counter][1] = id;
/*  74 */         if (jsonValue != null) {
/*  75 */           values[counter][2] = jsonValue;
/*     */         }
/*  77 */         counter++;
/*     */         
/*  79 */         if (conditionValues.getPropertiesJoinType() == null)
/*  80 */           conditionValues.setPropertiesJoinType(XPathUtil.getXPathResult(property, "wcm:jointype"));
/*     */       }
/*  82 */       conditionValues.setPropertiesNames(propertyNames);
/*  83 */       conditionValues.setPropertiesLabels(labelNames);
/*  84 */       conditionValues.setPropertiesOpers(operators);
/*  85 */       conditionValues.setPropertiesSelectable(selectables);
/*  86 */       conditionValues.setPropertiesValues(values);
/*  87 */       conditionValues.setPropertiesTypes(types);
/*  88 */       conditionValues.setSQL(XPathUtil.getXPathResult(condition, "wcm:sql"));
/*     */       
/*  90 */       String aggregation = XPathUtil.getXPathResult(condition, "wcm:aggregation");
/*  91 */       conditionValues.setAggregationType(aggregation);
/*  92 */       if (aggregation.equalsIgnoreCase("RecordInfo"))
/*     */       {
/*  94 */         conditionValues.setRecordContentContainsValues(XPathUtil.getXPathResult(condition, "wcm:content/wcm:contains"));
/*  95 */         conditionValues.setRecordContentJoinType(XPathUtil.getXPathResult(condition, "wcm:content/wcm:jointype"));
/*  96 */         conditionValues.setRecordContentSearchInType(XPathUtil.getXPathResult(condition, "wcm:content/wcm:searchin"));
/*     */       }
/*     */       
/*  99 */       conditionsArray[conditionCounter] = conditionValues;
/* 100 */       conditionCounter++;
/*     */     }
/*     */     
/*     */ 
/* 104 */     return conditionsArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String saveConditionsXML(IERConditionValues[] conditions, HttpServletRequest request, Repository repository, P8Connection conn)
/*     */     throws Exception
/*     */   {
/* 115 */     StringBuffer strBuf = new StringBuffer();
/* 116 */     strBuf.append("<response xmlns=\"http://filenet.com/namespaces/wcm/apps/1.0\">");
/* 117 */     strBuf.append("<newUserInterface>true</newUserInterface>");
/* 118 */     strBuf.append("<version>5.1.2</version>");
/* 119 */     strBuf.append("<versionNumber>1</versionNumber>");
/* 120 */     strBuf.append("<objectset><count>" + conditions.length + "</count>");
/*     */     
/* 122 */     for (IERConditionValues condition : conditions) {
/* 123 */       strBuf.append("<customobject>");
/*     */       
/* 125 */       String[] propertiesNames = condition.getPropertiesNames();
/* 126 */       String[] types = condition.getPropertiesTypes();
/* 127 */       String[] labels = condition.getPropertiesLabels();
/* 128 */       String[][] values = condition.getPropertiesValues();
/*     */       
/* 130 */       if (propertiesNames != null) {
/* 131 */         strBuf.append("<properties>");
/*     */         
/* 133 */         for (int i = 0; i < propertiesNames.length; i++)
/*     */         {
/*     */ 
/*     */ 
/* 137 */           boolean ignoreValue = (condition.getOperator(i).equalsIgnoreCase("NULL")) || (condition.getOperator(i).equalsIgnoreCase("NOTNULL"));
/* 138 */           if (propertiesNames[i] != null)
/*     */           {
/* 140 */             strBuf.append("<property>");
/* 141 */             strBuf.append("<jointype>" + condition.getPropertiesJoinType() + "</jointype>");
/* 142 */             strBuf.append("<symname>" + propertiesNames[i] + "</symname>");
/* 143 */             strBuf.append("<displayname>" + labels[i] + "</displayname>");
/* 144 */             String type = types[i];
/* 145 */             if ((type != null) && (type.length() > 0)) {
/* 146 */               type = (String)IERConditionValues.typeMap.get(type);
/*     */             }
/* 148 */             strBuf.append("<propertytype>" + type + "</propertytype>");
/* 149 */             strBuf.append("<selectable>" + condition.getPropertiesSelectable()[i] + "</selectable>");
/* 150 */             if ((types[i] != null) && (types[i].equals("7")))
/*     */             {
/* 152 */               if (ignoreValue) {
/* 153 */                 strBuf.append("<id></id>");
/*     */               } else {
/* 155 */                 strBuf.append("<id>" + values[i][1] + "</id>");
/*     */               }
/*     */             }
/* 158 */             if (ignoreValue) {
/* 159 */               strBuf.append("<value></value>");
/*     */             } else {
/* 161 */               strBuf.append("<value>" + EncoderUtil.encodeForXML(values[i][0]) + "</value>");
/*     */             }
/*     */             
/* 164 */             if ((values[i].length == 3) && (values[i][2] != null)) {
/* 165 */               strBuf.append("<jsonValue>" + EncoderUtil.encodeForXML(values[i][2]) + "</jsonValue>");
/*     */             }
/*     */             
/* 168 */             String operator = condition.getOperator(i);
/* 169 */             if ((operator != null) && (operator.length() > 0)) {
/* 170 */               operator = EncoderUtil.encodeForXML((String)IERConditionValues.operatorMap.getKey(operator));
/*     */             }
/* 172 */             strBuf.append("<operator>" + operator + "</operator>");
/* 173 */             strBuf.append("</property>");
/*     */           }
/*     */         }
/*     */         
/* 177 */         strBuf.append("</properties>");
/*     */       }
/*     */       
/* 180 */       strBuf.append("<aggregation>" + condition.getAggegationType());
/* 181 */       strBuf.append("</aggregation>");
/* 182 */       if (condition.getAggegationType().equalsIgnoreCase("RecordInfo"))
/*     */       {
/* 184 */         strBuf.append("<content>");
/* 185 */         strBuf.append("<contains>" + EncoderUtil.encodeForXML(condition.getRecordContentContainsValues()));
/* 186 */         strBuf.append("</contains>");
/* 187 */         strBuf.append("<jointype>" + condition.getRecordContentJoinType());
/* 188 */         strBuf.append("</jointype>");
/* 189 */         strBuf.append("<searchin>" + condition.getRecordContentSearchInType());
/* 190 */         strBuf.append("</searchin>");
/* 191 */         strBuf.append("</content>");
/*     */       }
/* 193 */       strBuf.append("<sql>" + generateSQL(condition, request, repository, conn));
/* 194 */       strBuf.append("</sql>");
/* 195 */       strBuf.append("</customobject>");
/*     */     }
/*     */     
/* 198 */     strBuf.append("</objectset></response>");
/*     */     
/* 200 */     return strBuf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String serializeConditionsFromJSON(JSONArray conditionArray, HttpServletRequest request, Repository repository, P8Connection conn)
/*     */     throws Exception
/*     */   {
/* 209 */     String conditionXml = null;
/* 210 */     if ((conditionArray != null) && (conditionArray.size() > 0)) {
/* 211 */       int size = conditionArray.size();
/* 212 */       IERConditionValues[] conditions = new IERConditionValues[size];
/* 213 */       for (int index = 0; index < size; index++) {
/* 214 */         JSONObject conditionObject = (JSONObject)conditionArray.get(index);
/* 215 */         IERConditionValues condition = new IERConditionValues();
/* 216 */         String aggregationType = (String)conditionObject.get("className");
/* 217 */         if (aggregationType == null) {
/* 218 */           aggregationType = "";
/*     */         }
/* 220 */         condition.setAggregationType(aggregationType);
/*     */         
/* 222 */         String criteriaJson = (String)conditionObject.get("criteriaJson");
/* 223 */         if (criteriaJson != null) {
/* 224 */           condition.setCriteriaJson(criteriaJson);
/*     */         }
/*     */         
/* 227 */         JSONArray criteriaArray = (JSONArray)conditionObject.get("criteria");
/* 228 */         if ((criteriaArray != null) && (criteriaArray.size() > 0)) {
/* 229 */           int s = criteriaArray.size();
/* 230 */           String[] names = new String[s];
/* 231 */           String[] operators = new String[s];
/* 232 */           String[][] values = new String[s][3];
/* 233 */           String[] labels = new String[s];
/* 234 */           String[] types = new String[s];
/* 235 */           String[] selectable = new String[s];
/* 236 */           for (int i = 0; i < s; i++) {
/* 237 */             JSONObject criteriaObject = (JSONObject)criteriaArray.get(i);
/* 238 */             names[i] = ((String)criteriaObject.get("name"));
/* 239 */             operators[i] = ((String)criteriaObject.get("operator"));
/* 240 */             JSONArray valueArray = (JSONArray)criteriaObject.get("values");
/* 241 */             if (valueArray != null) {
/* 242 */               int vs = valueArray.size();
/* 243 */               if (vs > 0) {
/* 244 */                 if ((valueArray.get(0) instanceof JSONObject)) {
/* 245 */                   JSONObject json = (JSONObject)valueArray.get(0);
/* 246 */                   values[i][0] = ((String)json.get("name"));
/*     */                   
/*     */ 
/* 249 */                   values[i][2] = json.serialize();
/*     */                 }
/*     */                 else {
/* 252 */                   values[i][0] = ((String)valueArray.get(0)); }
/* 253 */                 if (vs > 1) {
/* 254 */                   if ((valueArray.get(1) instanceof JSONObject)) {
/* 255 */                     values[i][1] = ((JSONObject)valueArray.get(1)).serialize();
/*     */                   } else {
/* 257 */                     values[i][1] = ((String)valueArray.get(1));
/*     */                   }
/*     */                 }
/*     */                 
/* 261 */                 values[i][2] = valueArray.serialize();
/*     */               }
/*     */             }
/* 264 */             String label = (String)criteriaObject.get("label");
/* 265 */             if (label == null) {
/* 266 */               label = (String)criteriaObject.get("displayName");
/* 267 */               if (label == null)
/* 268 */                 label = names[i];
/*     */             }
/* 270 */             labels[i] = label;
/* 271 */             String type = (String)criteriaObject.get("type");
/* 272 */             if (type == null) {
/* 273 */               type = "xs:string";
/*     */             }
/* 275 */             types[i] = type;
/* 276 */             selectable[i] = "true";
/*     */           }
/* 278 */           condition.setPropertiesNames(names);
/* 279 */           condition.setPropertiesOpers(operators);
/* 280 */           condition.setPropertiesValues(values);
/* 281 */           condition.setPropertiesLabels(labels);
/* 282 */           condition.setPropertiesTypes(types);
/* 283 */           condition.setPropertiesSelectable(selectable);
/*     */         }
/* 285 */         Boolean matchAll = (Boolean)conditionObject.get("matchAll");
/* 286 */         if (matchAll != null) {
/* 287 */           condition.setPropertiesJoinType(matchAll.booleanValue() ? "AND" : "OR");
/*     */         } else {
/* 289 */           condition.setPropertiesJoinType("");
/*     */         }
/* 291 */         JSONObject contentObject = (JSONObject)conditionObject.get("content");
/* 292 */         if (contentObject != null) {
/* 293 */           String contentValue = (String)contentObject.get("value");
/* 294 */           String contentType = (String)contentObject.get("type");
/* 295 */           condition.setRecordContentContainsValues(contentValue);
/* 296 */           condition.setRecordContentSearchInType(contentType);
/* 297 */           condition.setRecordContentJoinType(matchAll.booleanValue() ? "AND" : "OR");
/*     */         } else {
/* 299 */           condition.setRecordContentContainsValues("");
/* 300 */           condition.setRecordContentSearchInType("AND");
/* 301 */           condition.setRecordContentJoinType("METADATA");
/*     */         }
/* 303 */         if ((condition.recordContentContainsValues.length() > 0) || (condition.propertiesJoinType.length() > 0) || (condition.propertiesNames != null)) {
/* 304 */           conditions[index] = condition;
/*     */         }
/* 306 */         String sql = generateSQL(condition, request, repository, conn);
/* 307 */         condition.setSQL(sql);
/*     */       }
/* 309 */       conditionXml = saveConditionsXML(conditions, request, repository, conn);
/*     */     }
/* 311 */     return conditionXml;
/*     */   }
/*     */   
/*     */   public static JSONArray convertConditionsFromXML(String conditionXml) throws Exception {
/* 315 */     JSONArray conditionArray = new JSONArray();
/* 316 */     if (conditionXml != null) {
/* 317 */       IERConditionValues[] conditions = readConditionXML(conditionXml);
/* 318 */       if (conditions != null) {
/* 319 */         int length = conditions.length;
/* 320 */         for (int index = 0; index < length; index++) {
/* 321 */           IERConditionValues condition = conditions[index];
/* 322 */           String[] names = condition.getPropertiesNames();
/* 323 */           JSONObject conditionObject = new JSONObject();
/* 324 */           conditionObject.put("className", condition.getAggegationType());
/* 325 */           if ((names != null) && (names.length > 0)) {
/* 326 */             String[] operators = condition.getPropertiesOpers();
/* 327 */             String[][] values = condition.getPropertiesValues();
/* 328 */             String[] labels = condition.getPropertiesLabels();
/* 329 */             JSONArray criteriaArray = new JSONArray();
/* 330 */             int l = names.length;
/* 331 */             for (int i = 0; i < l; i++) {
/* 332 */               JSONObject criteriaObject = new JSONObject();
/* 333 */               criteriaObject.put("name", names[i]);
/* 334 */               criteriaObject.put("operator", operators[i]);
/* 335 */               JSONArray valueArray = new JSONArray();
/* 336 */               Object value = values[i][0];
/* 337 */               String jsonValue = values[i].length == 3 ? values[i][2] : null;
/*     */               
/* 339 */               if (jsonValue != null) {
/* 340 */                 criteriaObject.put("jsonValues", jsonValue);
/*     */               }
/*     */               
/* 343 */               if (value != null) {
/* 344 */                 valueArray.add(value);
/* 345 */                 value = values[i][1];
/* 346 */                 if (value != null) {
/* 347 */                   valueArray.add(value);
/*     */                 }
/*     */               }
/* 350 */               criteriaObject.put("values", valueArray);
/* 351 */               criteriaObject.put("label", labels[i]);
/* 352 */               criteriaObject.put("type", condition.getPropertiesType(i));
/* 353 */               criteriaArray.add(criteriaObject);
/*     */             }
/* 355 */             conditionObject.put("criteria", criteriaArray);
/*     */           }
/* 357 */           conditionObject.put("matchAll", Boolean.valueOf("AND".equals(condition.getPropertiesJoinType())));
/* 358 */           String contentValue = condition.getRecordContentContainsValues();
/* 359 */           if ((contentValue != null) && (contentValue.length() > 0)) {
/* 360 */             String contentType = condition.getRecordContentSearchInType();
/* 361 */             JSONObject contentObject = new JSONObject();
/* 362 */             contentObject.put("value", contentValue);
/* 363 */             contentObject.put("type", contentType);
/* 364 */             contentObject.put("matchAll", Boolean.valueOf("AND".equals(condition.getRecordContentJoinType())));
/* 365 */             conditionObject.put("content", contentObject);
/*     */           }
/* 367 */           conditionObject.put("sql", condition.getSQL());
/* 368 */           conditionObject.put("criteriaJson", condition.getCriteriaJson());
/* 369 */           conditionArray.add(conditionObject);
/*     */         }
/*     */       }
/*     */     }
/* 373 */     return conditionArray;
/*     */   }
/*     */   
/*     */   private static String generateSQL(IERConditionValues condition, HttpServletRequest request, Repository repository, P8Connection conn) throws Exception
/*     */   {
/* 378 */     JSONObject searchTemplateJson = JSONObject.parse(condition.getCriteriaJson());
/* 379 */     if (searchTemplateJson == null) {
/* 380 */       return null;
/*     */     }
/* 382 */     P8SearchTemplate searchTemplate = new P8SearchTemplate();
/* 383 */     P8SearchTemplateDocument searchDocument = new P8SearchTemplateDocument(request, conn, "", "", null);
/* 384 */     searchTemplate = (P8SearchTemplate)searchDocument.getSearchTemplate(searchTemplateJson, false);
/*     */     
/* 386 */     IERSearchRunner searchRunner = new IERSearchRunner(request, repository, searchTemplate, "", conn);
/* 387 */     searchRunner.setPropertyTableAlias(condition.getAggegationType());
/* 388 */     searchRunner.convertToP8SearchDefinition();
/* 389 */     P8SearchDefinition.SearchClause searchClause = searchRunner.getSearchDefinition().getSearchClause(SearchTemplateBase.ObjectType.document);
/* 390 */     P8SearchDefinition.SearchClause commonClause = searchRunner.getSearchDefinition().getSearchClause(SearchTemplateBase.ObjectType.common);
/* 391 */     SearchTemplateBase.VersionOption versionSelection = searchRunner.getSearchDefinition().getVersionSelection();
/* 392 */     List<P8SearchDefinition.SearchInFolder> folders = searchRunner.getSearchDefinition().getFolders();
/* 393 */     String query = null;
/* 394 */     if (searchClause != null) {
/* 395 */       List<String> additionalPropertiesList = new ArrayList(Arrays.asList(IERSearchRunner.getRecordSelectProps()));
/*     */       
/* 397 */       List<P8SearchDefinition.Subclass> subclasses = searchClause.getSubclasses();
/* 398 */       if (subclasses != null)
/*     */       {
/* 400 */         for (P8SearchDefinition.Subclass subclass : subclasses) {
/* 401 */           if ((subclass.getName().equals("StoredSearch")) || ((subclass.getName().equals("Document")) && (subclass.isSearchSubclasses()))) {
/* 402 */             additionalPropertiesList.add("IcnAutoRun");
/* 403 */             additionalPropertiesList.add("IcnShowInTree");
/* 404 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 409 */       StringBuilder nonPropSQLStatementBuffer = new StringBuilder();
/*     */       
/*     */ 
/* 412 */       query = searchRunner.getQuery(searchClause, commonClause, additionalPropertiesList, folders, versionSelection, null, true, -1, -1, -1, nonPropSQLStatementBuffer, true, true);
/*     */     }
/*     */     
/* 415 */     searchClause = searchRunner.getSearchDefinition().getSearchClause(SearchTemplateBase.ObjectType.folder);
/* 416 */     if (searchClause != null) {
/* 417 */       List<String> additionalProperties = new ArrayList(Arrays.asList(IERSearchRunner.getRmfolderSelectProps()));
/* 418 */       additionalProperties.addAll(searchRunner.getRMFolderIdentifier(searchClause));
/* 419 */       query = searchRunner.getQuery(searchClause, commonClause, additionalProperties, folders, null, null, true, -1, -1, -1, null, true, true);
/*     */     }
/* 421 */     return EncoderUtil.encodeForXML(query);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\IERConditionsUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */