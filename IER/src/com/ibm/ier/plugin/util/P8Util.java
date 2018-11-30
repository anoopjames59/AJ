/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.filenet.api.admin.ChoiceList;
/*     */ import com.filenet.api.collection.BinaryList;
/*     */ import com.filenet.api.collection.BooleanList;
/*     */ import com.filenet.api.collection.DateTimeList;
/*     */ import com.filenet.api.collection.DependentObjectList;
/*     */ import com.filenet.api.collection.Float64List;
/*     */ import com.filenet.api.collection.IdList;
/*     */ import com.filenet.api.collection.Integer32List;
/*     */ import com.filenet.api.collection.PropertyDescriptionList;
/*     */ import com.filenet.api.collection.StringList;
/*     */ import com.filenet.api.constants.Cardinality;
/*     */ import com.filenet.api.constants.PropertySettability;
/*     */ import com.filenet.api.constants.TypeID;
/*     */ import com.filenet.api.core.Containable;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.Factory.BooleanList;
/*     */ import com.filenet.api.core.Factory.DateTimeList;
/*     */ import com.filenet.api.core.Factory.Float64List;
/*     */ import com.filenet.api.core.Factory.IdList;
/*     */ import com.filenet.api.core.Factory.Integer32List;
/*     */ import com.filenet.api.core.Factory.StringList;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.meta.ClassDescription;
/*     */ import com.filenet.api.meta.PropertyDescription;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.Property;
/*     */ import com.filenet.api.property.PropertyBinary;
/*     */ import com.filenet.api.property.PropertyBinaryList;
/*     */ import com.filenet.api.property.PropertyBoolean;
/*     */ import com.filenet.api.property.PropertyBooleanList;
/*     */ import com.filenet.api.property.PropertyDateTime;
/*     */ import com.filenet.api.property.PropertyDateTimeList;
/*     */ import com.filenet.api.property.PropertyEngineObject;
/*     */ import com.filenet.api.property.PropertyFloat64;
/*     */ import com.filenet.api.property.PropertyFloat64List;
/*     */ import com.filenet.api.property.PropertyId;
/*     */ import com.filenet.api.property.PropertyIdList;
/*     */ import com.filenet.api.property.PropertyInteger32;
/*     */ import com.filenet.api.property.PropertyInteger32List;
/*     */ import com.filenet.api.property.PropertyString;
/*     */ import com.filenet.api.property.PropertyStringList;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.filenet.apiimpl.core.DocumentImpl;
/*     */ import com.ibm.ecm.configuration.Config;
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.search.util.DateUtil;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8Util
/*     */ {
/*     */   public static final String DOCID_SEPARATOR = ",";
/*     */   
/*     */   public static enum ModifyType
/*     */   {
/*  74 */     ADD, 
/*  75 */     CHECKIN, 
/*  76 */     EDIT;
/*     */     
/*     */     private ModifyType() {}
/*     */   }
/*     */   
/*  81 */   public static void setProperties(HttpServletRequest request, IndependentObject item, JSONArray criterias, ClassDescription classDescription, ModifyType modifyType) throws Exception { String methodName = "setProperties";
/*  82 */     Logger.logEntry(P8Util.class, methodName, request);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  87 */     JSONObject criteria = null;
/*     */     
/*     */ 
/*  90 */     PropertyDescriptionList propertyDescriptionList = classDescription.get_PropertyDescriptions();
/*     */     
/*     */ 
/*  93 */     Map<String, PropertyDescription> propDescsMap = new HashMap();
/*  94 */     PropertyDescription propertyDescription = null;
/*  95 */     for (int i = 0; i < propertyDescriptionList.size(); i++) {
/*  96 */       propertyDescription = (PropertyDescription)propertyDescriptionList.get(i);
/*  97 */       propDescsMap.put(propertyDescription.get_SymbolicName(), propertyDescription);
/*     */     }
/*     */     
/* 100 */     if (request != null) {
/* 101 */       Logger.logDebug(P8Util.class, methodName, request, "criterias.size()" + criterias.size());
/* 102 */       Logger.logDebug(P8Util.class, methodName, request, "adding the attribute values for the newly created item");
/*     */     }
/*     */     
/* 105 */     for (int iCriteriaCounter = 0; iCriteriaCounter < criterias.size(); iCriteriaCounter++) {
/* 106 */       criteria = (JSONObject)criterias.get(iCriteriaCounter);
/* 107 */       String criteriaName = criteria.get("name").toString();
/*     */       
/* 109 */       JSONArray valuesJsonArray = null;
/*     */       
/*     */ 
/* 112 */       Object valuesJson = criteria.get("value");
/*     */       
/*     */ 
/*     */ 
/* 116 */       if (valuesJson == null) {
/* 117 */         valuesJson = criteria.get("values");
/*     */       }
/*     */       
/* 120 */       if (valuesJson != null) {
/* 121 */         if ((valuesJson instanceof JSONArray)) {
/* 122 */           valuesJsonArray = (JSONArray)valuesJson;
/*     */         } else {
/* 124 */           valuesJsonArray = new JSONArray();
/* 125 */           valuesJsonArray.add(valuesJson.toString());
/*     */         }
/*     */       } else {
/* 128 */         valuesJsonArray = new JSONArray();
/*     */       }
/*     */       
/*     */ 
/* 132 */       String criteriaValue = (valuesJsonArray.isEmpty()) || (valuesJsonArray.get(0) == null) ? "" : valuesJsonArray.get(0).toString();
/*     */       
/* 134 */       if (request != null)
/* 135 */         Logger.logDebug(P8Util.class, methodName, request, "update Item: attrName=" + criteriaName + " attrValue=" + criteriaValue);
/* 136 */       propertyDescription = (PropertyDescription)propDescsMap.get(criteriaName);
/*     */       
/* 138 */       if (request != null) {
/* 139 */         Logger.logDebug(P8Util.class, methodName, request, "criteriaName=" + criteriaName + "  criteriaValue=" + criteriaValue);
/*     */       }
/* 141 */       Properties itemProperties = item.getProperties();
/* 142 */       if ((criteriaName.equalsIgnoreCase("FolderName")) && ((item instanceof Folder)))
/*     */       {
/*     */ 
/* 145 */         if (modifyType == ModifyType.EDIT) {
/* 146 */           Property folderNameProperty = itemProperties.get("FolderName");
/* 147 */           String folderNameValue = folderNameProperty.getStringValue();
/* 148 */           if (!folderNameValue.equals(criteriaValue)) {
/* 149 */             ((Folder)item).set_FolderName(criteriaValue);
/*     */           }
/*     */         } else {
/* 152 */           ((Folder)item).set_FolderName(criteriaValue);
/*     */         }
/* 154 */       } else if ((propertyDescription != null) && (!propertyDescription.get_IsReadOnly().booleanValue()) && (criteriaValue != null)) {
/* 155 */         boolean modifiable = true;
/* 156 */         if (modifyType == ModifyType.CHECKIN) {
/* 157 */           PropertySettability propertySettability = propertyDescription.get_Settability();
/* 158 */           if (propertySettability == PropertySettability.SETTABLE_ONLY_ON_CREATE) {
/* 159 */             Logger.logDebug(P8Util.class, methodName, request, "Property settable only on create.");
/* 160 */             modifiable = false;
/*     */           }
/*     */         }
/* 163 */         String propertyName = propertyDescription.get_SymbolicName();
/*     */         
/*     */ 
/* 166 */         if ((modifiable) && (itemProperties.isPropertyPresent(propertyName))) {
/* 167 */           modifiable = itemProperties.get(propertyName).isSettable();
/*     */         }
/* 169 */         if (request != null) {
/* 170 */           Logger.logDebug(P8Util.class, methodName, request, "Property modifiable = " + modifiable);
/*     */         }
/* 172 */         if (modifiable) {
/* 173 */           TypeID type = propertyDescription.get_DataType();
/* 174 */           Cardinality cardinality = propertyDescription.get_Cardinality();
/*     */           
/* 176 */           if (cardinality.getValue() == 2) {
/* 177 */             ChoiceList choiceList = propertyDescription.get_ChoiceList();
/* 178 */             if (choiceList != null) {
/* 179 */               if (choiceList.get_DataType().equals(TypeID.STRING)) {
/* 180 */                 StringList list = Factory.StringList.createList();
/* 181 */                 if (criteriaValue.length() > 0) {
/* 182 */                   for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 183 */                     String value = (String)valuesJsonArray.get(j);
/* 184 */                     list.add(value);
/*     */                   }
/*     */                 }
/*     */                 
/* 188 */                 if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */                 {
/* 190 */                   StringList currentValues = itemProperties.getStringListValue(propertyName);
/* 191 */                   if (!compareListProperties(list, currentValues)) {
/* 192 */                     if (criteriaValue.length() < 1) {
/* 193 */                       putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                     } else {
/* 195 */                       itemProperties.putObjectValue(propertyName, list);
/*     */                     }
/*     */                   }
/*     */                 }
/* 199 */                 else if (criteriaValue.length() < 1) {
/* 200 */                   putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                 } else {
/* 202 */                   itemProperties.putObjectValue(propertyName, list);
/*     */                 }
/*     */               }
/* 205 */               else if (choiceList.get_DataType().equals(TypeID.LONG)) {
/* 206 */                 Integer32List list = Factory.Integer32List.createList();
/* 207 */                 if (criteriaValue.length() > 0) {
/* 208 */                   for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 209 */                     if (valuesJsonArray.get(j) != null) {
/* 210 */                       String value = valuesJsonArray.get(j).toString();
/* 211 */                       list.add(new Integer(value));
/*     */                     }
/*     */                   }
/*     */                 }
/*     */                 
/* 216 */                 if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/* 217 */                   Integer32List currentValues = itemProperties.getInteger32ListValue(propertyName);
/* 218 */                   if (!compareListProperties(list, currentValues)) {
/* 219 */                     if (criteriaValue.length() < 1) {
/* 220 */                       putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                     } else {
/* 222 */                       itemProperties.putObjectValue(propertyName, list);
/*     */                     }
/*     */                   }
/*     */                 }
/* 226 */                 else if (criteriaValue.length() < 1) {
/* 227 */                   putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                 } else {
/* 229 */                   itemProperties.putObjectValue(propertyName, list);
/*     */                 }
/*     */                 
/*     */               }
/*     */             }
/* 234 */             else if (type == TypeID.BOOLEAN) {
/* 235 */               BooleanList list = Factory.BooleanList.createList();
/* 236 */               if (criteriaValue.length() > 0) {
/* 237 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 238 */                   if (valuesJsonArray.get(j) != null) {
/* 239 */                     String value = valuesJsonArray.get(j).toString();
/* 240 */                     list.add(new Boolean(value));
/*     */                   }
/*     */                 }
/*     */               }
/* 244 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */               {
/* 246 */                 BooleanList currentValues = itemProperties.getBooleanListValue(propertyName);
/* 247 */                 if (!compareListProperties(list, currentValues)) {
/* 248 */                   if (criteriaValue.length() < 1) {
/* 249 */                     putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                   } else {
/* 251 */                     itemProperties.putObjectValue(propertyName, list);
/*     */                   }
/*     */                 }
/*     */               }
/* 255 */               else if (criteriaValue.length() < 1) {
/* 256 */                 putNullValues(type, cardinality, itemProperties, propertyName);
/*     */               } else {
/* 258 */                 itemProperties.putObjectValue(propertyName, list);
/*     */               }
/*     */             }
/* 261 */             else if (type == TypeID.DATE) {
/* 262 */               DateTimeList list = Factory.DateTimeList.createList();
/* 263 */               if (criteriaValue.length() > 0) {
/* 264 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 265 */                   if (valuesJsonArray.get(j) != null) {
/* 266 */                     String value = valuesJsonArray.get(j).toString();
/* 267 */                     list.add(DateUtil.parseISODate(value, true));
/*     */                   }
/*     */                 }
/*     */               }
/* 271 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */               {
/* 273 */                 DateTimeList currentValues = itemProperties.getDateTimeListValue(propertyName);
/* 274 */                 if (!compareListProperties(list, currentValues)) {
/* 275 */                   if (criteriaValue.length() < 1) {
/* 276 */                     putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                   } else {
/* 278 */                     itemProperties.putObjectValue(propertyName, list);
/*     */                   }
/*     */                 }
/*     */               }
/* 282 */               else if (criteriaValue.length() < 1) {
/* 283 */                 putNullValues(type, cardinality, itemProperties, propertyName);
/*     */               } else {
/* 285 */                 itemProperties.putObjectValue(propertyName, list);
/*     */               }
/*     */             }
/* 288 */             else if (type == TypeID.LONG) {
/* 289 */               Integer32List list = Factory.Integer32List.createList();
/* 290 */               if (criteriaValue.length() > 0) {
/* 291 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 292 */                   if (valuesJsonArray.get(j) != null) {
/* 293 */                     String value = valuesJsonArray.get(j).toString();
/* 294 */                     list.add(new Integer(value));
/*     */                   }
/*     */                 }
/*     */               }
/* 298 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */               {
/* 300 */                 Integer32List currentValues = itemProperties.getInteger32ListValue(propertyName);
/* 301 */                 if (!compareListProperties(list, currentValues)) {
/* 302 */                   if (criteriaValue.length() < 1) {
/* 303 */                     putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                   } else {
/* 305 */                     itemProperties.putObjectValue(propertyName, list);
/*     */                   }
/*     */                 }
/*     */               }
/* 309 */               else if (criteriaValue.length() < 1) {
/* 310 */                 putNullValues(type, cardinality, itemProperties, propertyName);
/*     */               } else {
/* 312 */                 itemProperties.putObjectValue(propertyName, list);
/*     */               }
/*     */             }
/* 315 */             else if (type == TypeID.DOUBLE) {
/* 316 */               Float64List list = Factory.Float64List.createList();
/* 317 */               if (criteriaValue.length() > 0) {
/* 318 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 319 */                   if (valuesJsonArray.get(j) != null) {
/* 320 */                     String value = valuesJsonArray.get(j).toString();
/* 321 */                     list.add(new Double(value));
/*     */                   }
/*     */                 }
/*     */               }
/* 325 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */               {
/* 327 */                 Float64List currentValues = itemProperties.getFloat64ListValue(propertyName);
/* 328 */                 if (!compareListProperties(list, currentValues)) {
/* 329 */                   if (criteriaValue.length() < 1) {
/* 330 */                     putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                   } else {
/* 332 */                     itemProperties.putObjectValue(propertyName, list);
/*     */                   }
/*     */                 }
/*     */               }
/* 336 */               else if (criteriaValue.length() < 1) {
/* 337 */                 putNullValues(type, cardinality, itemProperties, propertyName);
/*     */               } else {
/* 339 */                 itemProperties.putObjectValue(propertyName, list);
/*     */               }
/*     */             }
/* 342 */             else if (type == TypeID.GUID) {
/* 343 */               IdList list = Factory.IdList.createList();
/* 344 */               if (criteriaValue.length() > 0) {
/* 345 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 346 */                   if (valuesJsonArray.get(j) != null) {
/* 347 */                     String value = valuesJsonArray.get(j).toString();
/* 348 */                     list.add(new Id(value));
/*     */                   }
/*     */                 }
/*     */               }
/* 352 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */               {
/* 354 */                 IdList currentValues = itemProperties.getIdListValue(propertyName);
/* 355 */                 if (!compareListProperties(list, currentValues)) {
/* 356 */                   if (criteriaValue.length() < 1) {
/* 357 */                     putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                   } else {
/* 359 */                     itemProperties.putObjectValue(propertyName, list);
/*     */                   }
/*     */                 }
/*     */               }
/* 363 */               else if (criteriaValue.length() < 1) {
/* 364 */                 putNullValues(type, cardinality, itemProperties, propertyName);
/*     */               } else {
/* 366 */                 itemProperties.putObjectValue(propertyName, list);
/*     */               }
/*     */             }
/* 369 */             else if (type != TypeID.OBJECT)
/*     */             {
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
/* 382 */               StringList list = Factory.StringList.createList();
/* 383 */               if (criteriaValue.length() > 0) {
/* 384 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/* 385 */                   if (valuesJsonArray.get(j) != null) {
/* 386 */                     String value = valuesJsonArray.get(j).toString();
/* 387 */                     list.add(value);
/*     */                   }
/*     */                 }
/*     */               }
/* 391 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */               {
/* 393 */                 StringList currentValues = itemProperties.getStringListValue(propertyName);
/* 394 */                 if (!compareListProperties(list, currentValues)) {
/* 395 */                   if (criteriaValue.length() < 1) {
/* 396 */                     putNullValues(type, cardinality, itemProperties, propertyName);
/*     */                   } else {
/* 398 */                     itemProperties.putObjectValue(propertyName, list);
/*     */                   }
/*     */                 }
/*     */               }
/* 402 */               else if (criteriaValue.length() < 1) {
/* 403 */                 putNullValues(type, cardinality, itemProperties, propertyName);
/*     */               } else {
/* 405 */                 itemProperties.putObjectValue(propertyName, list);
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/* 411 */           else if ((type != TypeID.OBJECT) && (criteriaValue.length() < 1)) {
/* 412 */             if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */             {
/* 414 */               Property property = itemProperties.get(propertyName);
/* 415 */               Object value = property.getObjectValue();
/* 416 */               if (value != null) {
/* 417 */                 itemProperties.putObjectValue(propertyName, value);
/*     */               }
/*     */             } else {
/* 420 */               putNullValues(type, cardinality, itemProperties, propertyName);
/*     */             }
/* 422 */           } else if (type == TypeID.DATE) {
/* 423 */             Date value = DateUtil.parseISODate(criteriaValue, true);
/* 424 */             if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */             {
/* 426 */               Property property = itemProperties.get(propertyName);
/* 427 */               Date propertyValue = property.getDateTimeValue();
/* 428 */               if (!value.equals(propertyValue)) {
/* 429 */                 itemProperties.putObjectValue(propertyName, value);
/*     */               }
/*     */             } else {
/* 432 */               itemProperties.putObjectValue(propertyName, value);
/*     */             }
/* 434 */           } else if (type == TypeID.DOUBLE) {
/* 435 */             Double value = new Double(criteriaValue);
/* 436 */             if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */             {
/* 438 */               Property property = itemProperties.get(propertyName);
/* 439 */               Double propertyValue = property.getFloat64Value();
/* 440 */               if (!value.equals(propertyValue)) {
/* 441 */                 itemProperties.putObjectValue(propertyName, value);
/*     */               }
/*     */             } else {
/* 444 */               itemProperties.putObjectValue(propertyName, value);
/*     */             }
/* 446 */           } else if (type == TypeID.LONG) {
/* 447 */             Integer value = new Integer(criteriaValue);
/* 448 */             if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */             {
/* 450 */               Property property = itemProperties.get(propertyName);
/* 451 */               Integer propertyValue = property.getInteger32Value();
/* 452 */               if (!value.equals(propertyValue)) {
/* 453 */                 itemProperties.putObjectValue(propertyName, value);
/*     */               }
/*     */             } else {
/* 456 */               itemProperties.putObjectValue(propertyName, value);
/*     */             }
/* 458 */           } else if (type == TypeID.GUID) {
/* 459 */             Id value = new Id(criteriaValue);
/* 460 */             if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */             {
/* 462 */               Property property = itemProperties.get(propertyName);
/* 463 */               Id propertyValue = property.getIdValue();
/* 464 */               if (!value.equals(propertyValue)) {
/* 465 */                 itemProperties.putObjectValue(propertyName, value);
/*     */               }
/*     */             } else {
/* 468 */               itemProperties.putObjectValue(propertyName, value);
/*     */             }
/* 470 */           } else if (type != TypeID.OBJECT)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 479 */             if (type == TypeID.BOOLEAN) {
/* 480 */               Boolean value = new Boolean(criteriaValue);
/* 481 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */               {
/* 483 */                 Property property = itemProperties.get(propertyName);
/* 484 */                 Boolean propertyValue = property.getBooleanValue();
/* 485 */                 if (!value.equals(propertyValue)) {
/* 486 */                   itemProperties.putObjectValue(propertyName, value);
/*     */                 }
/*     */               } else {
/* 489 */                 itemProperties.putObjectValue(propertyName, value);
/*     */               }
/*     */             }
/* 492 */             else if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))
/*     */             {
/* 494 */               Property property = itemProperties.get(propertyName);
/* 495 */               String propertyValue = property.getStringValue();
/* 496 */               if (!criteriaValue.equals(propertyValue)) {
/* 497 */                 itemProperties.putObjectValue(propertyName, criteriaValue);
/*     */               }
/*     */             } else {
/* 500 */               itemProperties.putValue(propertyName, criteriaValue);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void putNullValues(TypeID type, Cardinality cardinality, Properties itemProperties, String propertyName)
/*     */   {
/* 511 */     if (type == TypeID.DATE) {
/* 512 */       if (cardinality.getValue() == 2) {
/* 513 */         itemProperties.putValue(propertyName, (DateTimeList)null);
/*     */       }
/*     */       else {
/* 516 */         itemProperties.putValue(propertyName, (Date)null);
/*     */       }
/*     */     }
/* 519 */     else if (type == TypeID.DOUBLE) {
/* 520 */       if (cardinality.getValue() == 2) {
/* 521 */         itemProperties.putValue(propertyName, (Float64List)null);
/*     */       }
/*     */       else {
/* 524 */         itemProperties.putValue(propertyName, (Double)null);
/*     */       }
/* 526 */     } else if (type == TypeID.LONG) {
/* 527 */       if (cardinality.getValue() == 2) {
/* 528 */         itemProperties.putValue(propertyName, (Integer32List)null);
/*     */       }
/*     */       else {
/* 531 */         itemProperties.putValue(propertyName, ((Long)null).longValue());
/*     */       }
/* 533 */     } else if (type == TypeID.OBJECT) {
/* 534 */       if (cardinality.getValue() == 2) {
/* 535 */         itemProperties.putValue(propertyName, (DependentObjectList)null);
/*     */       }
/*     */       else {
/* 538 */         itemProperties.putValue(propertyName, (EngineObject)null);
/*     */       }
/* 540 */     } else if (type == TypeID.BOOLEAN) {
/* 541 */       if (cardinality.getValue() == 2) {
/* 542 */         itemProperties.putValue(propertyName, (BooleanList)null);
/*     */       }
/*     */       else {
/* 545 */         itemProperties.putValue(propertyName, (Boolean)null);
/*     */       }
/* 547 */     } else if (type == TypeID.STRING) {
/* 548 */       if (cardinality.getValue() == 2) {
/* 549 */         itemProperties.putValue(propertyName, (StringList)null);
/*     */       }
/*     */       else {
/* 552 */         itemProperties.putValue(propertyName, (String)null);
/*     */       }
/* 554 */     } else if (type == TypeID.BINARY) {
/* 555 */       if (cardinality.getValue() == 2) {
/* 556 */         itemProperties.putValue(propertyName, (BinaryList)null);
/*     */       }
/*     */       else {
/* 559 */         itemProperties.putValue(propertyName, (byte[])null);
/*     */       }
/* 561 */     } else if (type == TypeID.GUID) {
/* 562 */       if (cardinality.getValue() == 2) {
/* 563 */         itemProperties.putValue(propertyName, (IdList)null);
/*     */       }
/*     */       else {
/* 566 */         itemProperties.putValue(propertyName, (Id)null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean compareListProperties(List newValues, List currentValues)
/*     */   {
/* 576 */     if (newValues.size() != currentValues.size()) {
/* 577 */       return false;
/*     */     }
/* 579 */     for (int index = 0; index < newValues.size(); index++) {
/* 580 */       if (!newValues.get(index).equals(currentValues.get(index))) {
/* 581 */         return false;
/*     */       }
/*     */     }
/* 584 */     return true;
/*     */   }
/*     */   
/*     */   public static PropertyDescription getPropertyDescription(ClassDescription classDescription, String symbolicName)
/*     */   {
/* 589 */     return getPropertyDescription(classDescription.get_PropertyDescriptions(), symbolicName);
/*     */   }
/*     */   
/*     */ 
/*     */   private static PropertyDescription getPropertyDescription(PropertyDescriptionList propertyDescriptionList, String symbolicName)
/*     */   {
/* 595 */     Iterator propertyDescriptionIterator = propertyDescriptionList.iterator();
/* 596 */     while (propertyDescriptionIterator.hasNext()) {
/* 597 */       PropertyDescription propertyDescription = (PropertyDescription)propertyDescriptionIterator.next();
/* 598 */       String descriptionName = propertyDescription.get_SymbolicName();
/* 599 */       if (descriptionName.equals(symbolicName)) {
/* 600 */         return propertyDescription;
/*     */       }
/*     */     }
/* 603 */     return null;
/*     */   }
/*     */   
/*     */   public static PropertyDescription getPropertyDescription(ClassDescription classDescription, Property property) {
/* 607 */     String symbolicName = property.getPropertyName();
/* 608 */     return getPropertyDescription(classDescription, symbolicName);
/*     */   }
/*     */   
/*     */   public static String getNameProperty(HttpServletRequest request, boolean forFolder) {
/* 612 */     RepositoryConfig repositoryConfig = Config.getRepositoryConfig(request);
/* 613 */     String nameProperty = null;
/* 614 */     if (repositoryConfig != null) {
/* 615 */       if (forFolder) {
/* 616 */         nameProperty = repositoryConfig.getFolderNameProperty();
/*     */       } else {
/* 618 */         nameProperty = repositoryConfig.getDocNameProperty();
/*     */       }
/*     */     }
/*     */     
/* 622 */     if (nameProperty == null) {
/* 623 */       nameProperty = forFolder ? "FolderName" : "DocumentTitle";
/*     */     }
/*     */     
/* 626 */     return nameProperty;
/*     */   }
/*     */   
/*     */   public static String getNameProperty(HttpServletRequest request, ClassDescription cd, PropertyDescriptionList pdl) {
/* 630 */     if (cd.get_NamePropertyIndex() != null) {
/* 631 */       int namePropertyIndex = cd.get_NamePropertyIndex().intValue();
/* 632 */       PropertyDescription pd = (PropertyDescription)pdl.get(namePropertyIndex);
/* 633 */       return pd.get_SymbolicName();
/*     */     }
/* 635 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Object getValue(Property property)
/*     */   {
/* 642 */     return getValue(property, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object getValue(Property property, boolean ovpSupport)
/*     */   {
/* 650 */     Object returnVal = null;
/* 651 */     if ((property instanceof PropertyIdList)) {
/* 652 */       IdList idList = property.getIdListValue();
/* 653 */       if (idList != null) {
/* 654 */         Iterator iterator = idList.iterator();
/* 655 */         String[] array = new String[idList.size()];
/* 656 */         int i = 0;
/* 657 */         while (iterator.hasNext()) {
/* 658 */           Id id = (Id)iterator.next();
/* 659 */           array[i] = id.toString();
/* 660 */           i++;
/*     */         }
/* 662 */         returnVal = array;
/*     */       }
/* 664 */     } else if ((property instanceof PropertyBooleanList)) {
/* 665 */       BooleanList booleanList = property.getBooleanListValue();
/* 666 */       if (booleanList != null) {
/* 667 */         Iterator iterator = booleanList.iterator();
/* 668 */         Object[] array = new Boolean[booleanList.size()];
/* 669 */         int i = 0;
/* 670 */         while (iterator.hasNext()) {
/* 671 */           Boolean b = (Boolean)iterator.next();
/* 672 */           array[i] = b;
/* 673 */           i++;
/*     */         }
/* 675 */         returnVal = array;
/*     */       }
/* 677 */     } else if ((property instanceof PropertyDateTimeList)) {
/* 678 */       DateTimeList dateTimeList = property.getDateTimeListValue();
/* 679 */       if (dateTimeList != null) {
/* 680 */         Iterator iterator = dateTimeList.iterator();
/* 681 */         String[] array = new String[dateTimeList.size()];
/* 682 */         int i = 0;
/* 683 */         while (iterator.hasNext()) {
/* 684 */           Date date = (Date)iterator.next();
/* 685 */           array[i] = ("" + DateUtil.getISODateString(date, true));
/* 686 */           i++;
/*     */         }
/* 688 */         returnVal = array;
/*     */       }
/* 690 */     } else if ((property instanceof PropertyFloat64List)) {
/* 691 */       Float64List floatList = property.getFloat64ListValue();
/* 692 */       if (floatList != null) {
/* 693 */         Iterator iterator = floatList.iterator();
/* 694 */         Double[] array = new Double[floatList.size()];
/* 695 */         int i = 0;
/* 696 */         while (iterator.hasNext()) {
/* 697 */           Double d = (Double)iterator.next();
/* 698 */           array[i] = d;
/* 699 */           i++;
/*     */         }
/* 701 */         returnVal = array;
/*     */       }
/* 703 */     } else if ((property instanceof PropertyInteger32List)) {
/* 704 */       List list = property.getInteger32ListValue();
/* 705 */       if (list != null) {
/* 706 */         Iterator iterator = list.iterator();
/* 707 */         Integer[] array = new Integer[list.size()];
/* 708 */         int i = 0;
/* 709 */         while (iterator.hasNext()) {
/* 710 */           Integer value = (Integer)iterator.next();
/* 711 */           array[i] = value;
/* 712 */           i++;
/*     */         }
/* 714 */         returnVal = array;
/*     */       }
/* 716 */     } else if ((property instanceof PropertyStringList)) {
/* 717 */       List list = property.getStringListValue();
/* 718 */       if (list != null) {
/* 719 */         Iterator iterator = list.iterator();
/* 720 */         String[] array = new String[list.size()];
/* 721 */         int i = 0;
/* 722 */         while (iterator.hasNext()) {
/* 723 */           String value = (String)iterator.next();
/* 724 */           array[i] = value;
/* 725 */           i++;
/*     */         }
/* 727 */         returnVal = array;
/*     */       }
/* 729 */     } else if ((property instanceof PropertyEngineObject)) {
/* 730 */       EngineObject engObj = property.getEngineObjectValue();
/* 731 */       if (engObj != null) {
/* 732 */         if (ovpSupport) {
/* 733 */           Object objectValue = property.getObjectValue();
/* 734 */           if ((objectValue != null) && ((objectValue instanceof Containable))) {
/* 735 */             returnVal = objectValue;
/*     */           }
/*     */         }
/* 738 */         else if ((property.getObjectValue() instanceof DocumentImpl)) {
/* 739 */           returnVal = ((DocumentImpl)property.getObjectValue()).get_Id().toString();
/* 740 */         } else if ((property.getObjectValue() instanceof Containable)) {
/* 741 */           returnVal = ((Containable)property.getObjectValue()).get_Id().toString();
/*     */         }
/*     */       }
/*     */     }
/* 745 */     else if (!(property instanceof PropertyBinary))
/*     */     {
/* 747 */       if (!(property instanceof PropertyBinaryList))
/*     */       {
/* 749 */         if ((property instanceof PropertyId)) {
/* 750 */           if (property.getIdValue() != null) {
/* 751 */             returnVal = property.getIdValue().toString();
/*     */           }
/* 753 */         } else if ((property instanceof PropertyBoolean)) {
/* 754 */           if (property.getBooleanValue() != null) {
/* 755 */             returnVal = property.getBooleanValue();
/*     */           }
/* 757 */         } else if ((property instanceof PropertyDateTime)) {
/* 758 */           if (property.getDateTimeValue() != null) {
/* 759 */             returnVal = DateUtil.getISODateString(property.getDateTimeValue(), true);
/*     */           }
/* 761 */         } else if ((property instanceof PropertyFloat64)) {
/* 762 */           if (property.getFloat64Value() != null) {
/* 763 */             returnVal = property.getFloat64Value();
/*     */           }
/* 765 */         } else if (((property instanceof PropertyInteger32)) || ((property instanceof PropertyString))) {
/* 766 */           Object value = null;
/* 767 */           if ((property instanceof PropertyInteger32)) {
/* 768 */             value = property.getInteger32Value();
/* 769 */           } else if ((property instanceof PropertyString))
/* 770 */             value = property.getStringValue();
/* 771 */           if (value != null)
/* 772 */             returnVal = value;
/*     */         } }
/*     */     }
/* 775 */     return returnVal;
/*     */   }
/*     */   
/*     */   public static TypeID getTypeId(String dataType) {
/* 779 */     TypeID typeId = TypeID.STRING;
/*     */     
/* 781 */     if (dataType.equalsIgnoreCase("xs:binary")) {
/* 782 */       typeId = TypeID.BINARY;
/* 783 */     } else if (dataType.equalsIgnoreCase("xs:boolean")) {
/* 784 */       typeId = TypeID.BOOLEAN;
/* 785 */     } else if (dataType.equalsIgnoreCase("xs:timestamp")) {
/* 786 */       typeId = TypeID.DATE;
/* 787 */     } else if (dataType.equalsIgnoreCase("xs:double")) {
/* 788 */       typeId = TypeID.DOUBLE;
/* 789 */     } else if (dataType.equalsIgnoreCase("xs:guid")) {
/* 790 */       typeId = TypeID.GUID;
/* 791 */     } else if (dataType.equalsIgnoreCase("xs:integer")) {
/* 792 */       typeId = TypeID.LONG;
/* 793 */     } else if (dataType.equalsIgnoreCase("xs:object")) {
/* 794 */       typeId = TypeID.OBJECT;
/* 795 */     } else if (dataType.equalsIgnoreCase("xs:string")) {
/* 796 */       typeId = TypeID.STRING;
/*     */     }
/* 798 */     return typeId;
/*     */   }
/*     */   
/*     */   public static String getDataType(TypeID typeId) {
/* 802 */     String dataType = "xs:string";
/* 803 */     if (typeId == TypeID.BINARY) {
/* 804 */       dataType = "xs:binary";
/* 805 */     } else if (typeId == TypeID.BOOLEAN) {
/* 806 */       dataType = "xs:boolean";
/* 807 */     } else if (typeId == TypeID.DATE) {
/* 808 */       dataType = "xs:timestamp";
/* 809 */     } else if (typeId == TypeID.DOUBLE) {
/* 810 */       dataType = "xs:double";
/* 811 */     } else if (typeId == TypeID.GUID) {
/* 812 */       dataType = "xs:guid";
/* 813 */     } else if (typeId == TypeID.LONG) {
/* 814 */       dataType = "xs:integer";
/* 815 */     } else if (typeId == TypeID.OBJECT) {
/* 816 */       dataType = "xs:object";
/* 817 */     } else if (typeId == TypeID.STRING) {
/* 818 */       dataType = "xs:string";
/*     */     }
/* 820 */     return dataType;
/*     */   }
/*     */   
/*     */   public static String getDataType(PropertyDescription propertyDescription) {
/* 824 */     return getDataType(propertyDescription.get_DataType());
/*     */   }
/*     */   
/*     */   public static boolean isNumericDataType(PropertyDescription propertyDescription) {
/* 828 */     String dataType = getDataType(propertyDescription);
/* 829 */     if ((dataType.equals("xs:double")) || (dataType.equals("xs:integer"))) {
/* 830 */       return true;
/*     */     }
/* 832 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getDocId(ObjectReference ref)
/*     */   {
/* 838 */     String objectReferenceId = ref.getClassIdentity();
/*     */     
/* 840 */     if (objectReferenceId.equals("QuickrLibrary")) {
/* 841 */       objectReferenceId = "Folder";
/*     */     }
/* 843 */     return objectReferenceId + "," + ref.getObjectStoreIdentity() + "," + ref.getObjectIdentity();
/*     */   }
/*     */   
/*     */   public static String getDocId(String className, String objectStoreId, String id) {
/* 847 */     return className + "," + objectStoreId + "," + id;
/*     */   }
/*     */   
/*     */   public static String getObjectIdentity(String id) {
/* 851 */     int firstSeparator = id.indexOf(",");
/* 852 */     if (firstSeparator == -1)
/* 853 */       return id;
/* 854 */     int secondSeparator = id.indexOf(",", firstSeparator + 1);
/* 855 */     if (secondSeparator == -1) {
/* 856 */       return id;
/*     */     }
/* 858 */     String objectIdentity = id.substring(secondSeparator + 1);
/* 859 */     return objectIdentity;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\P8Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */