/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.PropertyUtil;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.BaseEntity;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMProperties;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescriptionObject;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.collections.Transformer;
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
/*     */ public class MediatorUtil
/*     */ {
/*     */   public static final String NAME = "name";
/*     */   public static final String ITEMS = "items";
/*     */   public static final String ROWS = "rows";
/*     */   public static final String DOCID = "docid";
/*     */   public static final String TEMPLATE = "template";
/*     */   public static final String DESCRIPTION = "description";
/*     */   public static final String CRITERIAS = "criterias";
/*     */   public static final String CRITERIA_VALUE = "value";
/*     */   public static final String DATATYPE_GROUP = "xs:group";
/*     */   public static final String DATATYPE_USER = "xs:user";
/*     */   public static final String DATATYPE_STRING = "xs:string";
/*     */   public static final String DATATYPE_LONG = "xs:long";
/*     */   public static final String DATATYPE_INTEGER = "xs:integer";
/*     */   public static final String DATATYPE_BOOLEAN = "xs:boolean";
/*     */   public static final String DATATYPE_DOUBLE = "xs:double";
/*     */   public static final String DATATYPE_TIMESTAMP = "xs:timestamp";
/*     */   public static final String DATATYPE_TIME = "xs:time";
/*     */   private static final String SHORT_NAME = "shortName";
/*  64 */   private static Map<String, String> DataTypeToJSON_Conversion_Map = Collections.unmodifiableMap(new HashMap() {});
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
/*     */   public static JSONArray createEntityItemJSONItemArrayData(BaseEntity entity, List<String> requestedProperties, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/*  81 */     JSONArray items = new JSONArray();
/*  82 */     items.add(convertPropertiesToJSON(entity, requestedProperties, request));
/*  83 */     return items;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONObject createEntityItemJSONObject(BaseEntity entity, List<String> requestedProperties, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/*  92 */     JSONObject entityJSON = new JSONObject();
/*     */     
/*  94 */     entityJSON.put("id", entity.getObjectIdentity());
/*  95 */     entityJSON.put("docid", IERUtil.getDocId(entity));
/*  96 */     entityJSON.put("template", entity.getClassName());
/*  97 */     entityJSON.put("RMEntityType", entity.getEntityType().toString());
/*  98 */     entityJSON.put("rows", createEntityItemJSONItemArrayData(entity, requestedProperties, request));
/*  99 */     return entityJSON;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONObject createFakeEntityItemJSONObject(String id, String name, String className, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 108 */     JSONObject entityJSON = new JSONObject();
/*     */     
/* 110 */     entityJSON.put("id", id);
/* 111 */     entityJSON.put("docid", id);
/* 112 */     entityJSON.put("template", className);
/* 113 */     entityJSON.put("rows", null);
/* 114 */     return entityJSON;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getJSONDataType(RMPropertyDescription propertyDescription)
/*     */   {
/* 123 */     return getJSONDataType(propertyDescription.getDataType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getJSONDataType(DataType typeId)
/*     */   {
/* 132 */     return (String)DataTypeToJSON_Conversion_Map.get(typeId.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONObject convertPropertiesToJSON(BaseEntity item, List<String> requestedProperties, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 142 */     BaseEntityResultMediator resultsMediator = new BaseEntityResultMediator((FilePlanRepository)item.getRepository(), request, requestedProperties);
/* 143 */     resultsMediator.setEntityItem(item);
/* 144 */     return resultsMediator.toJSONObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONObject convertPropertiesToJSON(BaseEntity item, HttpServletRequest request, List<String> requestedProperties)
/*     */     throws Exception
/*     */   {
/* 153 */     return convertPropertiesToJSON(item, requestedProperties, request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RMProperties getProperties(RMClassDescription cd, JSONArray criteriasJsonArray, Repository repo)
/*     */     throws Exception
/*     */   {
/* 163 */     RMProperties props = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/* 164 */     return setProperties(cd, criteriasJsonArray, repo, props);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RMProperties setProperties(RMClassDescription cd, JSONArray criteriasJsonArray, Repository repo, RMProperties props)
/*     */     throws Exception
/*     */   {
/* 175 */     JSONObject criteria = null;
/*     */     
/* 177 */     Map<String, RMPropertyDescription> propDescsMap = new HashMap();
/* 178 */     for (RMPropertyDescription pd : cd.getPropertyDescriptions()) {
/* 179 */       propDescsMap.put(pd.getSymbolicName(), pd);
/*     */     }
/*     */     
/* 182 */     for (int iCriteriaCounter = 0; iCriteriaCounter < criteriasJsonArray.size(); iCriteriaCounter++)
/*     */     {
/* 184 */       criteria = (JSONObject)criteriasJsonArray.get(iCriteriaCounter);
/* 185 */       Object criteriaNameObj = criteria.get("name");
/* 186 */       if (criteriaNameObj != null)
/*     */       {
/*     */ 
/* 189 */         String criteriaName = criteriaNameObj.toString();
/* 190 */         String criteriaValue = null;
/* 191 */         JSONArray valuesJsonArray = new JSONArray();
/* 192 */         Object crit_value = criteria.get("value");
/* 193 */         if ((crit_value instanceof JSONObject)) {
/* 194 */           criteriaValue = ((JSONObject)crit_value).toString();
/* 195 */         } else if ((crit_value instanceof JSONArray)) {
/* 196 */           valuesJsonArray = (JSONArray)crit_value;
/* 197 */           criteriaValue = valuesJsonArray.size() == 0 ? "" : String.valueOf(valuesJsonArray.get(0));
/*     */         } else {
/* 199 */           criteriaValue = crit_value != null ? crit_value.toString() : null;
/*     */         }
/* 201 */         RMPropertyDescription pd = (RMPropertyDescription)propDescsMap.get(criteriaName);
/* 202 */         RMProperty property = null;
/* 203 */         Boolean settable = Boolean.valueOf(true);
/*     */         
/* 205 */         if (props.isPropertyPresent(criteriaName)) {
/* 206 */           property = props.get(criteriaName);
/* 207 */           settable = Boolean.valueOf(property.isSettable());
/*     */         }
/*     */         
/* 210 */         if ((criteriaValue != null) && (!pd.isReadOnly()) && (settable.booleanValue()))
/*     */         {
/* 212 */           RMCardinality cardinality = pd.getCardinality();
/* 213 */           DataType dataType = pd.getDataType();
/* 214 */           boolean isList = (cardinality == RMCardinality.List) || (cardinality == RMCardinality.Enumeration);
/*     */           
/* 216 */           Object dataTypeOutput = PropertyUtil.getDataTypeOutputFromString(criteriaValue, dataType);
/*     */           
/* 218 */           if (dataType == DataType.Binary) {
/* 219 */             if (isList) {
/* 220 */               List<byte[]> list = new ArrayList();
/* 221 */               Collection<Object> collection = valuesJsonArray.subList(0, valuesJsonArray.size());
/* 222 */               if (!collection.isEmpty()) {
/* 223 */                 CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/*     */               } else {
/* 225 */                 props.putBinaryListValue(criteriaName, list);
/*     */               }
/*     */             } else {
/* 228 */               props.putBinaryValue(criteriaName, (byte[])dataTypeOutput);
/*     */             }
/* 230 */           } else if (dataType == DataType.Object) {
/* 231 */             if (isList) {
/* 232 */               List<Object> list = new ArrayList();
/* 233 */               Collection<Object> collection = valuesJsonArray.subList(0, valuesJsonArray.size());
/* 234 */               if (!collection.isEmpty()) {
/* 235 */                 CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/*     */               }
/* 237 */               props.putObjectListValue(criteriaName, list);
/*     */             }
/*     */             else {
/* 240 */               String id = dataTypeOutput != null ? IERUtil.getIdFromDocIdString(dataTypeOutput.toString()) : null;
/* 241 */               FilePlanRepository fp_repo = (FilePlanRepository)repo;
/* 242 */               RMPropertyDescriptionObject objPd = (RMPropertyDescriptionObject)pd;
/* 243 */               BaseEntity obj = null;
/* 244 */               if ((id != null) && (id.length() > 0)) {
/* 245 */                 obj = RMFactory.BaseEntity.getInstance(fp_repo, objPd.getRequiredClass().getSymbolicName(), id);
/*     */               }
/* 247 */               props.putObjectValue(criteriaName, obj);
/*     */             }
/* 249 */           } else if (dataType == DataType.DateTime) {
/* 250 */             if (isList) {
/* 251 */               List<Date> list = new ArrayList();
/* 252 */               Collection<Object> collection = valuesJsonArray.subList(0, valuesJsonArray.size());
/* 253 */               if (!collection.isEmpty()) {
/* 254 */                 CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/*     */               }
/* 256 */               props.putDateTimeListValue(criteriaName, list);
/*     */             }
/*     */             else {
/* 259 */               props.putDateTimeValue(criteriaName, (Date)dataTypeOutput);
/*     */             }
/* 261 */           } else if (dataType == DataType.Double) {
/* 262 */             if (isList) {
/* 263 */               List<Double> list = new ArrayList();
/* 264 */               Collection<Object> collection = valuesJsonArray.subList(0, valuesJsonArray.size());
/* 265 */               if (!collection.isEmpty()) {
/* 266 */                 CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/*     */               }
/* 268 */               props.putDoubleListValue(criteriaName, list);
/*     */             }
/*     */             else {
/* 271 */               props.putDoubleValue(criteriaName, (Double)dataTypeOutput);
/*     */             }
/* 273 */           } else if (dataType == DataType.Integer) {
/* 274 */             if (isList) {
/* 275 */               List<Integer> list = new ArrayList();
/* 276 */               Collection<Object> collection = valuesJsonArray.subList(0, valuesJsonArray.size());
/*     */               
/* 278 */               if (!collection.isEmpty())
/*     */               {
/*     */ 
/*     */ 
/* 282 */                 if (pd.getChoiceList() != null) {
/* 283 */                   if ((collection.size() == 1) && (collection.contains(""))) {
/* 284 */                     list = new ArrayList();
/*     */                   }
/*     */                   else {
/* 287 */                     CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/*     */                   }
/*     */                 } else {
/* 290 */                   CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/*     */                 }
/*     */               }
/*     */               
/* 294 */               props.putIntegerListValue(criteriaName, list);
/*     */             }
/*     */             else {
/* 297 */               props.putIntegerValue(criteriaName, (Integer)dataTypeOutput);
/*     */             }
/* 299 */           } else if (dataType == DataType.Guid) {
/* 300 */             if (isList) {
/* 301 */               List<String> list = new ArrayList();
/* 302 */               Collection<Object> collection = valuesJsonArray.subList(0, valuesJsonArray.size());
/* 303 */               if (!collection.isEmpty())
/* 304 */                 CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/* 305 */               props.putGuidListValue(criteriaName, list);
/*     */             }
/*     */             else {
/* 308 */               props.putGuidValue(criteriaName, (String)dataTypeOutput);
/*     */             }
/* 310 */           } else if (dataType == DataType.Boolean) {
/* 311 */             if (isList) {
/* 312 */               List<Boolean> list = new ArrayList();
/* 313 */               Collection<Object> collection = valuesJsonArray.subList(0, valuesJsonArray.size());
/* 314 */               if (collection.size() != 0) {
/* 315 */                 CollectionUtils.collect(collection, new StringToTypeTransformer(dataType), list);
/*     */               }
/* 317 */               props.putBooleanListValue(criteriaName, list);
/*     */             }
/*     */             else {
/* 320 */               props.putBooleanValue(criteriaName, (Boolean)dataTypeOutput);
/*     */             }
/*     */           }
/* 323 */           else if (dataType == DataType.String) {
/* 324 */             if (isList) {
/* 325 */               if (!valuesJsonArray.isEmpty()) {
/* 326 */                 List<String> list = valuesJsonArray.subList(0, valuesJsonArray.size());
/*     */                 
/*     */ 
/*     */ 
/* 330 */                 if (pd.getChoiceList() != null) {
/* 331 */                   if ((list.size() == 1) && (((String)list.get(0)).isEmpty())) {
/* 332 */                     props.putStringListValue(criteriaName, null);
/*     */                   }
/*     */                   else {
/* 335 */                     props.putStringListValue(criteriaName, list);
/*     */                   }
/*     */                 } else {
/* 338 */                   props.putStringListValue(criteriaName, list);
/*     */                 }
/*     */               }
/*     */               else {
/* 342 */                 props.putStringListValue(criteriaName, null);
/*     */               }
/*     */             }
/*     */             else {
/* 346 */               String value = (String)dataTypeOutput;
/*     */               
/* 348 */               if ((criteriaName.equals("Reviewer")) && 
/* 349 */                 (value != null)) {
/*     */                 try {
/* 351 */                   JSONObject groupObject = JSONObject.parse(value);
/* 352 */                   value = (String)groupObject.get("shortName");
/*     */                 }
/*     */                 catch (IOException e) {}
/*     */               }
/*     */               
/* 357 */               props.putStringValue(criteriaName, value);
/*     */             }
/*     */             
/*     */           }
/*     */           else
/* 362 */             props.putStringValue(criteriaName, (String)dataTypeOutput);
/*     */         }
/*     */       }
/*     */     }
/* 366 */     return props;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONArray convertPropertiesAndPropertyDescriptionToJSON(BaseEntity item, RMClassDescription cd, HttpServletRequest request)
/*     */   {
/* 376 */     JSONArray jsonArray = new JSONArray();
/* 377 */     RMProperties properties = item.getProperties();
/*     */     
/* 379 */     Iterator<RMProperty> iterator = properties.iterator();
/* 380 */     while (iterator.hasNext())
/*     */     {
/* 382 */       RMProperty property = (RMProperty)iterator.next();
/* 383 */       RMPropertyDescription propertyDescription = PropertyUtil.getPropertyDescription(cd, property);
/* 384 */       if (propertyDescription != null)
/*     */       {
/* 386 */         JSONObject jsonCriteria = new JSONObject();
/*     */         
/*     */ 
/* 389 */         if ((!propertyDescription.isHidden()) && 
/*     */         
/*     */ 
/*     */ 
/* 393 */           (propertyDescription.getDataType() != DataType.Binary))
/*     */         {
/*     */ 
/* 396 */           Object value = PropertyUtil.getDisplayableValue(property, false);
/* 397 */           String maxEntry = String.valueOf(PropertyUtil.getMaxEntryForProperty(propertyDescription));
/* 398 */           String[] minMaxValues = PropertyUtil.getMinAndMaxValuesForProperty(propertyDescription, request);
/*     */           
/* 400 */           jsonCriteria.put("cardinality", propertyDescription.getCardinality().toString());
/* 401 */           jsonCriteria.put("name", propertyDescription.getSymbolicName());
/* 402 */           jsonCriteria.put("dataType", getJSONDataType(propertyDescription));
/* 403 */           jsonCriteria.put("readOnly", Boolean.valueOf(propertyDescription.isReadOnly()));
/* 404 */           jsonCriteria.put("required", Boolean.valueOf(propertyDescription.isValueRequired()));
/* 405 */           jsonCriteria.put("label", propertyDescription.getDisplayName());
/* 406 */           jsonCriteria.put("minValue", minMaxValues[0] != null ? minMaxValues[0] : "");
/* 407 */           jsonCriteria.put("maxValue", minMaxValues[1] != null ? minMaxValues[1] : "");
/* 408 */           jsonCriteria.put("maxEntry", maxEntry);
/* 409 */           jsonCriteria.put("values", objectValueToJSONObject(value));
/*     */           
/* 411 */           jsonArray.add(jsonCriteria);
/*     */         }
/*     */       } }
/* 414 */     return jsonArray;
/*     */   }
/*     */   
/*     */   public static Object objectValueToJSONObject(Object value) {
/* 418 */     if ((value instanceof String[])) {
/* 419 */       JSONArray jsonArray = new JSONArray();
/* 420 */       String[] array = (String[])value;
/* 421 */       for (int i = 0; i < array.length; i++) {
/* 422 */         String v = array[i];
/* 423 */         jsonArray.add(v);
/*     */       }
/* 425 */       return jsonArray;
/*     */     }
/* 427 */     return value;
/*     */   }
/*     */   
/*     */   public static class StringToTypeTransformer
/*     */     implements Transformer {
/*     */     private final DataType dataType;
/*     */     
/* 434 */     public StringToTypeTransformer(DataType type) { this.dataType = type; }
/*     */     
/*     */     public Object transform(Object arg0) {
/* 437 */       String input = null;
/* 438 */       if ((arg0 instanceof String)) {
/* 439 */         input = (String)arg0;
/*     */       } else {
/* 441 */         input = String.valueOf(arg0);
/*     */       }
/* 443 */       return PropertyUtil.getDataTypeOutputFromString(input, this.dataType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\MediatorUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */