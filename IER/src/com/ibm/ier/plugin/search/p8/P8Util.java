/*      */ package com.ibm.ier.plugin.search.p8;
/*      */ 
/*      */ import com.filenet.api.admin.ChoiceList;
/*      */ import com.filenet.api.admin.ClassDefinition;
/*      */ import com.filenet.api.admin.PropertyTemplate;
/*      */ import com.filenet.api.collection.AccessPermissionDescriptionList;
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.BooleanList;
/*      */ import com.filenet.api.collection.ContentElementList;
/*      */ import com.filenet.api.collection.DateTimeList;
/*      */ import com.filenet.api.collection.Float64List;
/*      */ import com.filenet.api.collection.IdList;
/*      */ import com.filenet.api.collection.Integer32List;
/*      */ import com.filenet.api.collection.PropertyDescriptionList;
/*      */ import com.filenet.api.collection.StringList;
/*      */ import com.filenet.api.constants.AccessType;
/*      */ import com.filenet.api.constants.Cardinality;
/*      */ import com.filenet.api.constants.ConfigurationParameter;
/*      */ import com.filenet.api.constants.PropertySettability;
/*      */ import com.filenet.api.constants.TypeID;
/*      */ import com.filenet.api.core.Containable;
/*      */ import com.filenet.api.core.ContentTransfer;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.Factory.AccessPermission;
/*      */ import com.filenet.api.core.Factory.BooleanList;
/*      */ import com.filenet.api.core.Factory.ClassDefinition;
/*      */ import com.filenet.api.core.Factory.ClassDescription;
/*      */ import com.filenet.api.core.Factory.CustomObject;
/*      */ import com.filenet.api.core.Factory.DateTimeList;
/*      */ import com.filenet.api.core.Factory.Document;
/*      */ import com.filenet.api.core.Factory.Float64List;
/*      */ import com.filenet.api.core.Factory.Folder;
/*      */ import com.filenet.api.core.Factory.IdList;
/*      */ import com.filenet.api.core.Factory.Integer32List;
/*      */ import com.filenet.api.core.Factory.MetadataCache;
/*      */ import com.filenet.api.core.Factory.ObjectStore;
/*      */ import com.filenet.api.core.Factory.PropertyTemplate;
/*      */ import com.filenet.api.core.Factory.StringList;
/*      */ import com.filenet.api.core.Factory.VersionSeries;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.core.IndependentlyPersistableObject;
/*      */ import com.filenet.api.core.ObjectReference;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.VersionSeries;
/*      */ import com.filenet.api.exception.EngineRuntimeException;
/*      */ import com.filenet.api.exception.ExceptionCode;
/*      */ import com.filenet.api.meta.ClassDescription;
/*      */ import com.filenet.api.meta.MetadataCache;
/*      */ import com.filenet.api.meta.PropertyDescription;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.Property;
/*      */ import com.filenet.api.property.PropertyBinary;
/*      */ import com.filenet.api.property.PropertyBinaryList;
/*      */ import com.filenet.api.property.PropertyBoolean;
/*      */ import com.filenet.api.property.PropertyBooleanList;
/*      */ import com.filenet.api.property.PropertyDateTime;
/*      */ import com.filenet.api.property.PropertyDateTimeList;
/*      */ import com.filenet.api.property.PropertyEngineObject;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.property.PropertyFloat64;
/*      */ import com.filenet.api.property.PropertyFloat64List;
/*      */ import com.filenet.api.property.PropertyId;
/*      */ import com.filenet.api.property.PropertyIdList;
/*      */ import com.filenet.api.property.PropertyInteger32;
/*      */ import com.filenet.api.property.PropertyInteger32List;
/*      */ import com.filenet.api.property.PropertyString;
/*      */ import com.filenet.api.property.PropertyStringList;
/*      */ import com.filenet.api.security.AccessPermission;
/*      */ import com.filenet.api.security.AccessPermissionDescription;
/*      */ import com.filenet.api.util.Configuration;
/*      */ import com.filenet.api.util.ConfigurationParameters;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.filenet.api.util.UserContext;
/*      */ import com.ibm.ecm.configuration.Config;
/*      */ import com.ibm.ecm.configuration.RepositoryConfig;
/*      */ import com.ibm.ecm.serviceability.Logger;
/*      */ import com.ibm.ier.plugin.search.util.BaseUtil;
/*      */ import com.ibm.ier.plugin.search.util.DateUtil;
/*      */ import com.ibm.ier.plugin.search.util.MessageUtil;
/*      */ import com.ibm.json.java.JSONArray;
/*      */ import com.ibm.json.java.JSONObject;
/*      */ import java.io.IOException;
/*      */ import java.text.Collator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
/*      */ import java.util.Vector;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpSession;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class P8Util
/*      */ {
/*  107 */   private static final Collator COLLATOR = ;
/*      */   private static final String VERSION_STATUS_LOOKUP_ID = "{BFF36187-91DF-4F90-BAC5-CD5B3F3C6D8A}";
/*      */   private static final String LOOKUP_LIST_PROPERTY = "LookupList";
/*      */   public static final String DOCID_SEPARATOR = ",";
/*      */   
/*  112 */   public static enum ModifyType { ADD, 
/*  113 */     CHECKIN, 
/*  114 */     EDIT;
/*      */     
/*      */     private ModifyType() {} }
/*      */   
/*  118 */   public static void setProperties(HttpServletRequest request, IndependentObject item, JSONArray criterias, ClassDescription classDescription, ModifyType modifyType) throws Exception { String methodName = "setProperties";
/*  119 */     Logger.logEntry(P8Util.class, methodName, request);
/*      */     
/*      */ 
/*  122 */     P8Connection connection = getConnection(request);
/*      */     
/*  124 */     JSONObject criteria = null;
/*      */     
/*      */ 
/*  127 */     PropertyDescriptionList propertyDescriptionList = classDescription.get_PropertyDescriptions();
/*      */     
/*      */ 
/*  130 */     Map<String, PropertyDescription> propDescsMap = new HashMap();
/*  131 */     PropertyDescription propertyDescription = null;
/*  132 */     for (int i = 0; i < propertyDescriptionList.size(); i++) {
/*  133 */       propertyDescription = (PropertyDescription)propertyDescriptionList.get(i);
/*  134 */       propDescsMap.put(propertyDescription.get_SymbolicName(), propertyDescription);
/*      */     }
/*      */     
/*  137 */     Logger.logDebug(P8Util.class, methodName, request, "criterias.size()" + criterias.size());
/*  138 */     Logger.logDebug(P8Util.class, methodName, request, "adding the attribute values for the newly created item");
/*      */     
/*  140 */     for (int iCriteriaCounter = 0; iCriteriaCounter < criterias.size(); iCriteriaCounter++) {
/*  141 */       criteria = (JSONObject)criterias.get(iCriteriaCounter);
/*  142 */       String criteriaName = criteria.get("name").toString();
/*      */       
/*  144 */       JSONArray valuesJsonArray = null;
/*      */       
/*      */ 
/*  147 */       Object valuesJson = criteria.get("value");
/*      */       
/*      */ 
/*      */ 
/*  151 */       if (valuesJson == null) {
/*  152 */         valuesJson = criteria.get("values");
/*      */       }
/*      */       
/*  155 */       if (valuesJson != null) {
/*  156 */         if ((valuesJson instanceof JSONArray)) {
/*  157 */           valuesJsonArray = (JSONArray)valuesJson;
/*      */         } else {
/*  159 */           valuesJsonArray = new JSONArray();
/*  160 */           valuesJsonArray.add(valuesJson.toString());
/*      */         }
/*      */       } else {
/*  163 */         valuesJsonArray = new JSONArray();
/*      */       }
/*      */       
/*      */ 
/*  167 */       String criteriaValue = (valuesJsonArray.isEmpty()) || (valuesJsonArray.get(0) == null) ? "" : valuesJsonArray.get(0).toString();
/*      */       
/*  169 */       Logger.logDebug(P8Util.class, methodName, request, "update Item: attrName=" + criteriaName + " attrValue=" + criteriaValue);
/*  170 */       propertyDescription = (PropertyDescription)propDescsMap.get(criteriaName);
/*      */       
/*  172 */       Logger.logDebug(P8Util.class, methodName, request, "criteriaName=" + criteriaName + "  criteriaValue=" + criteriaValue);
/*  173 */       Properties itemProperties = item.getProperties();
/*  174 */       if ((criteriaName.equalsIgnoreCase("FolderName")) && ((item instanceof Folder)))
/*      */       {
/*      */ 
/*  177 */         if (modifyType == ModifyType.EDIT) {
/*  178 */           Property folderNameProperty = itemProperties.get("FolderName");
/*  179 */           String folderNameValue = folderNameProperty.getStringValue();
/*  180 */           if (!folderNameValue.equals(criteriaValue)) {
/*  181 */             ((Folder)item).set_FolderName(criteriaValue);
/*      */           }
/*      */         } else {
/*  184 */           ((Folder)item).set_FolderName(criteriaValue);
/*      */         }
/*  186 */       } else if ((propertyDescription != null) && (!propertyDescription.get_IsReadOnly().booleanValue()) && (criteriaValue != null)) {
/*  187 */         boolean modifiable = true;
/*  188 */         if (modifyType == ModifyType.CHECKIN) {
/*  189 */           PropertySettability propertySettability = propertyDescription.get_Settability();
/*  190 */           if (propertySettability == PropertySettability.SETTABLE_ONLY_ON_CREATE) {
/*  191 */             Logger.logDebug(P8Util.class, methodName, request, "Property settable only on create.");
/*  192 */             modifiable = false;
/*      */           }
/*      */         }
/*  195 */         String propertyName = propertyDescription.get_SymbolicName();
/*  196 */         boolean isPresent = itemProperties.isPropertyPresent(propertyName);
/*  197 */         boolean isModified = !isPresent;
/*      */         
/*      */ 
/*  200 */         if ((modifiable) && (isPresent)) {
/*  201 */           modifiable = itemProperties.get(propertyName).isSettable();
/*      */         }
/*  203 */         Logger.logDebug(P8Util.class, methodName, request, "Property modifiable = " + modifiable);
/*      */         
/*  205 */         if (modifiable) {
/*  206 */           TypeID type = propertyDescription.get_DataType();
/*  207 */           Cardinality cardinality = propertyDescription.get_Cardinality();
/*      */           
/*  209 */           if (cardinality.getValue() == 2) {
/*  210 */             ChoiceList choiceList = propertyDescription.get_ChoiceList();
/*  211 */             if (choiceList != null) {
/*  212 */               if (choiceList.get_DataType().equals(TypeID.STRING)) {
/*  213 */                 StringList list = Factory.StringList.createList();
/*  214 */                 if (criteriaValue.length() > 0) {
/*  215 */                   for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  216 */                     String value = (String)valuesJsonArray.get(j);
/*  217 */                     list.add(value);
/*      */                   }
/*      */                 }
/*      */                 
/*  221 */                 if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  222 */                   if (isPresent) {
/*  223 */                     StringList currentValues = itemProperties.getStringListValue(propertyName);
/*  224 */                     isModified = !compareListProperties(list, currentValues);
/*      */                   }
/*      */                   
/*  227 */                   if (isModified) {
/*  228 */                     if (criteriaValue.length() < 1) {
/*  229 */                       itemProperties.putObjectValue(propertyName, null);
/*      */                     } else {
/*  231 */                       itemProperties.putObjectValue(propertyName, list);
/*      */                     }
/*      */                   }
/*      */                 }
/*  235 */                 else if (criteriaValue.length() < 1) {
/*  236 */                   itemProperties.putObjectValue(propertyName, null);
/*      */                 } else {
/*  238 */                   itemProperties.putObjectValue(propertyName, list);
/*      */                 }
/*      */               }
/*  241 */               else if (choiceList.get_DataType().equals(TypeID.LONG)) {
/*  242 */                 Integer32List list = Factory.Integer32List.createList();
/*  243 */                 if (criteriaValue.length() > 0) {
/*  244 */                   for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  245 */                     if (valuesJsonArray.get(j) != null) {
/*  246 */                       String value = valuesJsonArray.get(j).toString();
/*  247 */                       list.add(new Integer(value));
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 
/*  252 */                 if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  253 */                   if (isPresent) {
/*  254 */                     Integer32List currentValues = itemProperties.getInteger32ListValue(propertyName);
/*  255 */                     isModified = !compareListProperties(list, currentValues);
/*      */                   }
/*      */                   
/*  258 */                   if (isModified) {
/*  259 */                     if (criteriaValue.length() < 1) {
/*  260 */                       itemProperties.putObjectValue(propertyName, null);
/*      */                     } else {
/*  262 */                       itemProperties.putObjectValue(propertyName, list);
/*      */                     }
/*      */                   }
/*      */                 }
/*  266 */                 else if (criteriaValue.length() < 1) {
/*  267 */                   itemProperties.putObjectValue(propertyName, null);
/*      */                 } else {
/*  269 */                   itemProperties.putObjectValue(propertyName, list);
/*      */                 }
/*      */                 
/*      */               }
/*      */             }
/*  274 */             else if (type == TypeID.BOOLEAN) {
/*  275 */               BooleanList list = Factory.BooleanList.createList();
/*  276 */               if (criteriaValue.length() > 0) {
/*  277 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  278 */                   if (valuesJsonArray.get(j) != null) {
/*  279 */                     String value = valuesJsonArray.get(j).toString();
/*  280 */                     list.add(new Boolean(value));
/*      */                   }
/*      */                 }
/*      */               }
/*  284 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  285 */                 if (isPresent) {
/*  286 */                   BooleanList currentValues = itemProperties.getBooleanListValue(propertyName);
/*  287 */                   isModified = !compareListProperties(list, currentValues);
/*      */                 }
/*      */                 
/*  290 */                 if (isModified) {
/*  291 */                   if (criteriaValue.length() < 1) {
/*  292 */                     itemProperties.putObjectValue(propertyName, null);
/*      */                   } else {
/*  294 */                     itemProperties.putObjectValue(propertyName, list);
/*      */                   }
/*      */                 }
/*      */               }
/*  298 */               else if (criteriaValue.length() < 1) {
/*  299 */                 itemProperties.putObjectValue(propertyName, null);
/*      */               } else {
/*  301 */                 itemProperties.putObjectValue(propertyName, list);
/*      */               }
/*      */             }
/*  304 */             else if (type == TypeID.DATE) {
/*  305 */               DateTimeList list = Factory.DateTimeList.createList();
/*  306 */               if (criteriaValue.length() > 0) {
/*  307 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  308 */                   if (valuesJsonArray.get(j) != null) {
/*  309 */                     String value = valuesJsonArray.get(j).toString();
/*  310 */                     list.add(DateUtil.parseISODate(value, true));
/*      */                   }
/*      */                 }
/*      */               }
/*  314 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  315 */                 if (isPresent) {
/*  316 */                   DateTimeList currentValues = itemProperties.getDateTimeListValue(propertyName);
/*  317 */                   isModified = !compareListProperties(list, currentValues);
/*      */                 }
/*      */                 
/*  320 */                 if (isModified) {
/*  321 */                   if (criteriaValue.length() < 1) {
/*  322 */                     itemProperties.putObjectValue(propertyName, null);
/*      */                   } else {
/*  324 */                     itemProperties.putObjectValue(propertyName, list);
/*      */                   }
/*      */                 }
/*      */               }
/*  328 */               else if (criteriaValue.length() < 1) {
/*  329 */                 itemProperties.putObjectValue(propertyName, null);
/*      */               } else {
/*  331 */                 itemProperties.putObjectValue(propertyName, list);
/*      */               }
/*      */             }
/*  334 */             else if (type == TypeID.LONG) {
/*  335 */               Integer32List list = Factory.Integer32List.createList();
/*  336 */               if (criteriaValue.length() > 0) {
/*  337 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  338 */                   if (valuesJsonArray.get(j) != null) {
/*  339 */                     String value = valuesJsonArray.get(j).toString();
/*  340 */                     list.add(new Integer(value));
/*      */                   }
/*      */                 }
/*      */               }
/*  344 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  345 */                 if (isPresent) {
/*  346 */                   Integer32List currentValues = itemProperties.getInteger32ListValue(propertyName);
/*  347 */                   isModified = !compareListProperties(list, currentValues);
/*      */                 }
/*      */                 
/*  350 */                 if (isModified) {
/*  351 */                   if (criteriaValue.length() < 1) {
/*  352 */                     itemProperties.putObjectValue(propertyName, null);
/*      */                   } else {
/*  354 */                     itemProperties.putObjectValue(propertyName, list);
/*      */                   }
/*      */                 }
/*      */               }
/*  358 */               else if (criteriaValue.length() < 1) {
/*  359 */                 itemProperties.putObjectValue(propertyName, null);
/*      */               } else {
/*  361 */                 itemProperties.putObjectValue(propertyName, list);
/*      */               }
/*      */             }
/*  364 */             else if (type == TypeID.DOUBLE) {
/*  365 */               Float64List list = Factory.Float64List.createList();
/*  366 */               if (criteriaValue.length() > 0) {
/*  367 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  368 */                   if (valuesJsonArray.get(j) != null) {
/*  369 */                     String value = valuesJsonArray.get(j).toString();
/*  370 */                     list.add(new Double(value));
/*      */                   }
/*      */                 }
/*      */               }
/*  374 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  375 */                 if (isPresent) {
/*  376 */                   Float64List currentValues = itemProperties.getFloat64ListValue(propertyName);
/*  377 */                   isModified = !compareListProperties(list, currentValues);
/*      */                 }
/*      */                 
/*  380 */                 if (isModified) {
/*  381 */                   if (criteriaValue.length() < 1) {
/*  382 */                     itemProperties.putObjectValue(propertyName, null);
/*      */                   } else {
/*  384 */                     itemProperties.putObjectValue(propertyName, list);
/*      */                   }
/*      */                 }
/*      */               }
/*  388 */               else if (criteriaValue.length() < 1) {
/*  389 */                 itemProperties.putObjectValue(propertyName, null);
/*      */               } else {
/*  391 */                 itemProperties.putObjectValue(propertyName, list);
/*      */               }
/*      */             }
/*  394 */             else if (type == TypeID.GUID) {
/*  395 */               IdList list = Factory.IdList.createList();
/*  396 */               if (criteriaValue.length() > 0) {
/*  397 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  398 */                   if (valuesJsonArray.get(j) != null) {
/*  399 */                     String value = valuesJsonArray.get(j).toString();
/*  400 */                     list.add(new Id(value));
/*      */                   }
/*      */                 }
/*      */               }
/*  404 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  405 */                 if (isPresent) {
/*  406 */                   IdList currentValues = itemProperties.getIdListValue(propertyName);
/*  407 */                   isModified = !compareListProperties(list, currentValues);
/*      */                 }
/*      */                 
/*  410 */                 if (isModified) {
/*  411 */                   if (criteriaValue.length() < 1) {
/*  412 */                     itemProperties.putObjectValue(propertyName, null);
/*      */                   } else {
/*  414 */                     itemProperties.putObjectValue(propertyName, list);
/*      */                   }
/*      */                 }
/*      */               }
/*  418 */               else if (criteriaValue.length() < 1) {
/*  419 */                 itemProperties.putObjectValue(propertyName, null);
/*      */               } else {
/*  421 */                 itemProperties.putObjectValue(propertyName, list);
/*      */               }
/*      */             }
/*  424 */             else if (type != TypeID.OBJECT)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  437 */               StringList list = Factory.StringList.createList();
/*  438 */               if (criteriaValue.length() > 0) {
/*  439 */                 for (int j = 0; j < valuesJsonArray.size(); j++) {
/*  440 */                   if (valuesJsonArray.get(j) != null) {
/*  441 */                     String value = valuesJsonArray.get(j).toString();
/*  442 */                     list.add(value);
/*      */                   }
/*      */                 }
/*      */               }
/*  446 */               if ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)) {
/*  447 */                 if (isPresent) {
/*  448 */                   StringList currentValues = itemProperties.getStringListValue(propertyName);
/*  449 */                   isModified = !compareListProperties(list, currentValues);
/*      */                 }
/*      */                 
/*  452 */                 if (isModified) {
/*  453 */                   if (criteriaValue.length() < 1) {
/*  454 */                     itemProperties.putObjectValue(propertyName, null);
/*      */                   } else {
/*  456 */                     itemProperties.putObjectValue(propertyName, list);
/*      */                   }
/*      */                 }
/*      */               }
/*  460 */               else if (criteriaValue.length() < 1) {
/*  461 */                 itemProperties.putObjectValue(propertyName, null);
/*      */               } else {
/*  463 */                 itemProperties.putObjectValue(propertyName, list);
/*      */               }
/*      */               
/*      */             }
/*      */             
/*      */           }
/*  469 */           else if ((type != TypeID.OBJECT) && (criteriaValue.length() < 1)) {
/*  470 */             if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)))
/*      */             {
/*  472 */               Property property = itemProperties.get(propertyName);
/*  473 */               Object value = property.getObjectValue();
/*  474 */               if (value != null) {
/*  475 */                 itemProperties.putObjectValue(propertyName, null);
/*      */               }
/*      */             } else {
/*  478 */               itemProperties.putObjectValue(propertyName, null);
/*      */             }
/*  480 */           } else if (type == TypeID.DATE) {
/*  481 */             Date value = DateUtil.parseISODate(criteriaValue, true);
/*  482 */             if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)))
/*      */             {
/*  484 */               Property property = itemProperties.get(propertyName);
/*  485 */               Date propertyValue = property.getDateTimeValue();
/*  486 */               if (!value.equals(propertyValue)) {
/*  487 */                 itemProperties.putObjectValue(propertyName, value);
/*      */               }
/*      */             } else {
/*  490 */               itemProperties.putObjectValue(propertyName, value);
/*      */             }
/*  492 */           } else if (type == TypeID.DOUBLE) {
/*  493 */             Double value = new Double(criteriaValue);
/*  494 */             if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)))
/*      */             {
/*  496 */               Property property = itemProperties.get(propertyName);
/*  497 */               Double propertyValue = property.getFloat64Value();
/*  498 */               if (!value.equals(propertyValue)) {
/*  499 */                 itemProperties.putObjectValue(propertyName, value);
/*      */               }
/*      */             } else {
/*  502 */               itemProperties.putObjectValue(propertyName, value);
/*      */             }
/*  504 */           } else if (type == TypeID.LONG) {
/*  505 */             Integer value = new Integer(criteriaValue);
/*  506 */             if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)))
/*      */             {
/*  508 */               Property property = itemProperties.get(propertyName);
/*  509 */               Integer propertyValue = property.getInteger32Value();
/*  510 */               if (!value.equals(propertyValue)) {
/*  511 */                 itemProperties.putObjectValue(propertyName, value);
/*      */               }
/*      */             } else {
/*  514 */               itemProperties.putObjectValue(propertyName, value);
/*      */             }
/*  516 */           } else if (type == TypeID.GUID) {
/*  517 */             Id value = new Id(criteriaValue);
/*  518 */             if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)))
/*      */             {
/*  520 */               Property property = itemProperties.get(propertyName);
/*  521 */               Id propertyValue = property.getIdValue();
/*  522 */               if (!value.equals(propertyValue)) {
/*  523 */                 itemProperties.putObjectValue(propertyName, value);
/*      */               }
/*      */             } else {
/*  526 */               itemProperties.putObjectValue(propertyName, value);
/*      */             }
/*  528 */           } else if (type == TypeID.OBJECT)
/*      */           {
/*      */ 
/*  531 */             if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT))) {
/*  532 */               Property property = itemProperties.get(propertyName);
/*  533 */               Object propertyValue = property.getObjectValue();
/*  534 */               String id = "";
/*  535 */               if (propertyValue != null) {
/*  536 */                 Containable containableObject = (Containable)propertyValue;
/*  537 */                 id = containableObject.get_Id().toString();
/*      */               }
/*  539 */               if (!id.equals(getObjectIdentity(criteriaValue))) {
/*  540 */                 IndependentlyPersistableObject value = null;
/*  541 */                 if ((criteriaValue != null) && (!criteriaValue.isEmpty())) {
/*  542 */                   value = getPersistableObject(request, connection, criteriaValue);
/*      */                 }
/*  544 */                 itemProperties.putObjectValue(propertyName, value);
/*      */               }
/*      */             } else {
/*  547 */               IndependentlyPersistableObject value = null;
/*  548 */               if ((criteriaValue != null) && (!criteriaValue.isEmpty())) {
/*  549 */                 value = getPersistableObject(request, connection, criteriaValue);
/*      */               }
/*  551 */               itemProperties.putObjectValue(propertyName, value);
/*      */             }
/*  553 */           } else if (type == TypeID.BOOLEAN) {
/*  554 */             Boolean value = new Boolean(criteriaValue);
/*  555 */             if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)))
/*      */             {
/*  557 */               Property property = itemProperties.get(propertyName);
/*  558 */               Boolean propertyValue = property.getBooleanValue();
/*  559 */               if (!value.equals(propertyValue)) {
/*  560 */                 itemProperties.putObjectValue(propertyName, value);
/*      */               }
/*      */             } else {
/*  563 */               itemProperties.putObjectValue(propertyName, value);
/*      */             }
/*      */           }
/*  566 */           else if ((isPresent) && ((modifyType == ModifyType.CHECKIN) || (modifyType == ModifyType.EDIT)))
/*      */           {
/*  568 */             Property property = itemProperties.get(propertyName);
/*  569 */             String propertyValue = property.getStringValue();
/*  570 */             if (!criteriaValue.equals(propertyValue)) {
/*  571 */               itemProperties.putObjectValue(propertyName, criteriaValue);
/*      */             }
/*      */           } else {
/*  574 */             itemProperties.putObjectValue(propertyName, criteriaValue);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  582 */     Logger.logExit(P8Util.class, methodName, request);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static boolean compareListProperties(List newValues, List currentValues)
/*      */   {
/*  589 */     if (newValues.size() != currentValues.size()) {
/*  590 */       return false;
/*      */     }
/*  592 */     for (int index = 0; index < newValues.size(); index++) {
/*  593 */       if (!newValues.get(index).equals(currentValues.get(index))) {
/*  594 */         return false;
/*      */       }
/*      */     }
/*  597 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static P8Connection getConnection(HttpServletRequest request, String serverName)
/*      */   {
/*  609 */     String methodName = "getConnection";
/*  610 */     Logger.logEntry(P8Util.class, methodName, request);
/*      */     
/*  612 */     HttpSession session = request.getSession(false);
/*  613 */     P8Connection connection = null;
/*      */     
/*  615 */     if (session != null) {
/*  616 */       Logger.logDebug(P8Util.class, methodName, request, "HttpSession ID is: " + session.getId());
/*  617 */       Logger.logDebug(P8Util.class, methodName, request, "serverName: " + serverName);
/*  618 */       Hashtable connectionList = (Hashtable)session.getAttribute("p8_servers");
/*      */       
/*  620 */       if ((connectionList != null) && (serverName != null)) {
/*  621 */         connection = (P8Connection)connectionList.get(serverName);
/*      */       }
/*      */     }
/*      */     
/*  625 */     Logger.logDebug(P8Util.class, methodName, request, "Returning connection: " + connection);
/*  626 */     Logger.logExit(P8Util.class, methodName, request);
/*  627 */     return connection;
/*      */   }
/*      */   
/*      */   public static P8Connection getConnection(HttpServletRequest request) {
/*  631 */     return getConnection(request, request.getParameter("repositoryId"));
/*      */   }
/*      */   
/*      */   public static boolean isSummaryTableProperty(String symbolicName) {
/*  635 */     return (symbolicName.equals("ClbCommentCount")) || (symbolicName.equals("ClbDownloadCount"));
/*      */   }
/*      */   
/*      */   public static PropertyDescription getPropertyDescription(ClassDescription classDescription, String symbolicName) {
/*  639 */     return getPropertyDescription(classDescription.get_PropertyDescriptions(), symbolicName);
/*      */   }
/*      */   
/*      */   private static PropertyDescription getPropertyDescription(PropertyDescriptionList propertyDescriptionList, String symbolicName)
/*      */   {
/*  644 */     Iterator propertyDescriptionIterator = propertyDescriptionList.iterator();
/*  645 */     while (propertyDescriptionIterator.hasNext()) {
/*  646 */       PropertyDescription propertyDescription = (PropertyDescription)propertyDescriptionIterator.next();
/*  647 */       String descriptionName = propertyDescription.get_SymbolicName();
/*  648 */       if (descriptionName.equals(symbolicName)) {
/*  649 */         return propertyDescription;
/*      */       }
/*      */     }
/*  652 */     return null;
/*      */   }
/*      */   
/*      */   public static PropertyDescription getPropertyDescription(ClassDescription classDescription, Property property) {
/*  656 */     String symbolicName = property.getPropertyName();
/*  657 */     return getPropertyDescription(classDescription, symbolicName);
/*      */   }
/*      */   
/*      */   public static String getNameProperty(HttpServletRequest request, boolean forFolder) {
/*  661 */     RepositoryConfig repositoryConfig = Config.getRepositoryConfig(request);
/*  662 */     String nameProperty = null;
/*  663 */     if (repositoryConfig != null) {
/*  664 */       if (forFolder) {
/*  665 */         nameProperty = repositoryConfig.getFolderNameProperty();
/*      */       } else {
/*  667 */         nameProperty = repositoryConfig.getDocNameProperty();
/*      */       }
/*      */     }
/*      */     
/*  671 */     if (nameProperty == null) {
/*  672 */       nameProperty = forFolder ? "FolderName" : "DocumentTitle";
/*      */     }
/*      */     
/*  675 */     return nameProperty;
/*      */   }
/*      */   
/*      */   public static String getNameProperty(HttpServletRequest request, ClassDescription cd, PropertyDescriptionList pdl) {
/*  679 */     if (cd.get_NamePropertyIndex() != null) {
/*  680 */       int namePropertyIndex = cd.get_NamePropertyIndex().intValue();
/*  681 */       PropertyDescription pd = (PropertyDescription)pdl.get(namePropertyIndex);
/*  682 */       return pd.get_SymbolicName();
/*      */     }
/*  684 */     return null;
/*      */   }
/*      */   
/*      */   public static Vector getItemsFromJson(String jsonString) {
/*  688 */     String methodName = "getItemsFromJson";
/*  689 */     Vector items = null;
/*      */     try {
/*  691 */       JSONArray jsonArray = JSONArray.parse(jsonString);
/*      */       
/*  693 */       items = new Vector(jsonArray.size());
/*      */       
/*  695 */       for (int i = 0; i < jsonArray.size(); i++) {
/*  696 */         String value = jsonArray.get(i).toString();
/*      */         
/*  698 */         items.add(value);
/*      */       }
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  703 */       Logger.logError(P8Util.class, methodName, (HttpServletRequest)null, e);
/*      */     }
/*      */     
/*      */ 
/*  707 */     return items;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object getValue(Property property)
/*      */   {
/*  715 */     return getValue(property, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static Object getValue(Property property, boolean ovpSupport)
/*      */   {
/*  722 */     Object returnVal = null;
/*  723 */     if ((property instanceof PropertyIdList)) {
/*  724 */       IdList idList = property.getIdListValue();
/*  725 */       if (idList != null) {
/*  726 */         Iterator iterator = idList.iterator();
/*  727 */         String[] array = new String[idList.size()];
/*  728 */         int i = 0;
/*  729 */         while (iterator.hasNext()) {
/*  730 */           Id id = (Id)iterator.next();
/*  731 */           array[i] = id.toString();
/*  732 */           i++;
/*      */         }
/*  734 */         returnVal = array;
/*      */       }
/*  736 */     } else if ((property instanceof PropertyBooleanList)) {
/*  737 */       BooleanList booleanList = property.getBooleanListValue();
/*  738 */       if (booleanList != null) {
/*  739 */         Iterator iterator = booleanList.iterator();
/*  740 */         Object[] array = new Boolean[booleanList.size()];
/*  741 */         int i = 0;
/*  742 */         while (iterator.hasNext()) {
/*  743 */           Boolean b = (Boolean)iterator.next();
/*  744 */           array[i] = b;
/*  745 */           i++;
/*      */         }
/*  747 */         returnVal = array;
/*      */       }
/*  749 */     } else if ((property instanceof PropertyDateTimeList)) {
/*  750 */       DateTimeList dateTimeList = property.getDateTimeListValue();
/*  751 */       if (dateTimeList != null) {
/*  752 */         Iterator iterator = dateTimeList.iterator();
/*  753 */         String[] array = new String[dateTimeList.size()];
/*  754 */         int i = 0;
/*  755 */         while (iterator.hasNext()) {
/*  756 */           Date date = (Date)iterator.next();
/*  757 */           array[i] = ("" + DateUtil.getISODateString(date, true));
/*  758 */           i++;
/*      */         }
/*  760 */         returnVal = array;
/*      */       }
/*  762 */     } else if ((property instanceof PropertyFloat64List)) {
/*  763 */       Float64List floatList = property.getFloat64ListValue();
/*  764 */       if (floatList != null) {
/*  765 */         Iterator iterator = floatList.iterator();
/*  766 */         Double[] array = new Double[floatList.size()];
/*  767 */         int i = 0;
/*  768 */         while (iterator.hasNext()) {
/*  769 */           Double d = (Double)iterator.next();
/*  770 */           array[i] = d;
/*  771 */           i++;
/*      */         }
/*  773 */         returnVal = array;
/*      */       }
/*  775 */     } else if ((property instanceof PropertyInteger32List)) {
/*  776 */       List list = property.getInteger32ListValue();
/*  777 */       if (list != null) {
/*  778 */         Iterator iterator = list.iterator();
/*  779 */         Integer[] array = new Integer[list.size()];
/*  780 */         int i = 0;
/*  781 */         while (iterator.hasNext()) {
/*  782 */           Integer value = (Integer)iterator.next();
/*  783 */           array[i] = value;
/*  784 */           i++;
/*      */         }
/*  786 */         returnVal = array;
/*      */       }
/*  788 */     } else if ((property instanceof PropertyStringList)) {
/*  789 */       List list = property.getStringListValue();
/*  790 */       if (list != null) {
/*  791 */         Iterator iterator = list.iterator();
/*  792 */         String[] array = new String[list.size()];
/*  793 */         int i = 0;
/*  794 */         while (iterator.hasNext()) {
/*  795 */           String value = (String)iterator.next();
/*  796 */           array[i] = value;
/*  797 */           i++;
/*      */         }
/*  799 */         returnVal = array;
/*      */       }
/*  801 */     } else if ((property instanceof PropertyEngineObject)) {
/*  802 */       ObjectReference ref = ((PropertyEngineObject)property).getObjectReference();
/*  803 */       if (ref != null) {
/*  804 */         if (ovpSupport) {
/*  805 */           Object objectValue = property.getObjectValue();
/*  806 */           if ((objectValue != null) && ((objectValue instanceof Containable))) {
/*  807 */             returnVal = objectValue;
/*      */           }
/*      */         } else {
/*  810 */           returnVal = getDocId(ref);
/*      */         }
/*      */       }
/*  813 */     } else if (!(property instanceof PropertyBinary))
/*      */     {
/*  815 */       if (!(property instanceof PropertyBinaryList))
/*      */       {
/*  817 */         if ((property instanceof PropertyId)) {
/*  818 */           if (property.getIdValue() != null) {
/*  819 */             returnVal = property.getIdValue().toString();
/*      */           }
/*  821 */         } else if ((property instanceof PropertyBoolean)) {
/*  822 */           if (property.getBooleanValue() != null) {
/*  823 */             returnVal = property.getBooleanValue();
/*      */           }
/*  825 */         } else if ((property instanceof PropertyDateTime)) {
/*  826 */           if (property.getDateTimeValue() != null) {
/*  827 */             returnVal = DateUtil.getISODateString(property.getDateTimeValue(), true);
/*      */           }
/*  829 */         } else if ((property instanceof PropertyFloat64)) {
/*  830 */           if (property.getFloat64Value() != null) {
/*  831 */             returnVal = property.getFloat64Value();
/*      */           }
/*  833 */         } else if (((property instanceof PropertyInteger32)) || ((property instanceof PropertyString))) {
/*  834 */           Object value = null;
/*  835 */           if ((property instanceof PropertyInteger32)) {
/*  836 */             value = property.getInteger32Value();
/*  837 */           } else if ((property instanceof PropertyString))
/*  838 */             value = property.getStringValue();
/*  839 */           if (value != null)
/*  840 */             returnVal = value;
/*      */         } }
/*      */     }
/*  843 */     return returnVal;
/*      */   }
/*      */   
/*      */   public static List<String> getVersionStatusLookups(HttpServletRequest request, ObjectStore objectStore) {
/*  847 */     String key = "_versionStatusLookup" + objectStore.get_Id();
/*  848 */     List<String> vs = (List)request.getAttribute(key);
/*  849 */     if (vs == null) {
/*  850 */       PropertyFilter propertyFilter = new PropertyFilter();
/*  851 */       propertyFilter.addIncludeProperty(1, null, null, "LookupList", null);
/*  852 */       CustomObject customObj = Factory.CustomObject.fetchInstance(objectStore, new Id("{BFF36187-91DF-4F90-BAC5-CD5B3F3C6D8A}"), propertyFilter);
/*  853 */       Properties properties = customObj.getProperties();
/*      */       
/*  855 */       vs = properties.getStringListValue("LookupList");
/*  856 */       request.setAttribute(key, vs);
/*      */     }
/*      */     
/*  859 */     return vs;
/*      */   }
/*      */   
/*      */   public static TypeID getTypeId(String dataType) {
/*  863 */     TypeID typeId = TypeID.STRING;
/*      */     
/*  865 */     if (dataType.equalsIgnoreCase("xs:binary")) {
/*  866 */       typeId = TypeID.BINARY;
/*  867 */     } else if (dataType.equalsIgnoreCase("xs:boolean")) {
/*  868 */       typeId = TypeID.BOOLEAN;
/*  869 */     } else if (dataType.equalsIgnoreCase("xs:timestamp")) {
/*  870 */       typeId = TypeID.DATE;
/*  871 */     } else if (dataType.equalsIgnoreCase("xs:double")) {
/*  872 */       typeId = TypeID.DOUBLE;
/*  873 */     } else if (dataType.equalsIgnoreCase("xs:guid")) {
/*  874 */       typeId = TypeID.GUID;
/*  875 */     } else if (dataType.equalsIgnoreCase("xs:integer")) {
/*  876 */       typeId = TypeID.LONG;
/*  877 */     } else if (dataType.equalsIgnoreCase("xs:object")) {
/*  878 */       typeId = TypeID.OBJECT;
/*  879 */     } else if (dataType.equalsIgnoreCase("xs:string")) {
/*  880 */       typeId = TypeID.STRING;
/*      */     }
/*  882 */     return typeId;
/*      */   }
/*      */   
/*      */   public static String getDataType(TypeID typeId) {
/*  886 */     String dataType = "xs:string";
/*  887 */     if (typeId == TypeID.BINARY) {
/*  888 */       dataType = "xs:binary";
/*  889 */     } else if (typeId == TypeID.BOOLEAN) {
/*  890 */       dataType = "xs:boolean";
/*  891 */     } else if (typeId == TypeID.DATE) {
/*  892 */       dataType = "xs:timestamp";
/*  893 */     } else if (typeId == TypeID.DOUBLE) {
/*  894 */       dataType = "xs:double";
/*  895 */     } else if (typeId == TypeID.GUID) {
/*  896 */       dataType = "xs:guid";
/*  897 */     } else if (typeId == TypeID.LONG) {
/*  898 */       dataType = "xs:integer";
/*  899 */     } else if (typeId == TypeID.OBJECT) {
/*  900 */       dataType = "xs:object";
/*  901 */     } else if (typeId == TypeID.STRING) {
/*  902 */       dataType = "xs:string";
/*      */     }
/*  904 */     return dataType;
/*      */   }
/*      */   
/*      */   public static String getDataType(PropertyDescription propertyDescription) {
/*  908 */     return getDataType(propertyDescription.get_DataType());
/*      */   }
/*      */   
/*      */   public static boolean isNumericDataType(PropertyDescription propertyDescription) {
/*  912 */     String dataType = getDataType(propertyDescription);
/*  913 */     if ((dataType.equals("xs:double")) || (dataType.equals("xs:integer"))) {
/*  914 */       return true;
/*      */     }
/*  916 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String getDocId(ObjectReference ref)
/*      */   {
/*  922 */     String objectReferenceId = ref.getClassIdentity();
/*      */     
/*  924 */     if (objectReferenceId.equals("QuickrLibrary")) {
/*  925 */       objectReferenceId = "Folder";
/*      */     }
/*  927 */     return objectReferenceId + "," + ref.getObjectStoreIdentity() + "," + ref.getObjectIdentity();
/*      */   }
/*      */   
/*      */   public static String getDocId(String className, String objectStoreId, String id) {
/*  931 */     return className + "," + objectStoreId + "," + id;
/*      */   }
/*      */   
/*      */   public static String getObjectIdentity(String id) {
/*  935 */     int firstSeparator = id.indexOf(",");
/*  936 */     if (firstSeparator == -1)
/*  937 */       return id;
/*  938 */     int secondSeparator = id.indexOf(",", firstSeparator + 1);
/*  939 */     if (secondSeparator == -1) {
/*  940 */       return id;
/*      */     }
/*  942 */     String objectIdentity = id.substring(secondSeparator + 1);
/*  943 */     return objectIdentity;
/*      */   }
/*      */   
/*      */   public static String getObjectStoreIdentity(String id) {
/*  947 */     int firstSeparator = id.indexOf(",");
/*  948 */     if (firstSeparator == -1)
/*  949 */       return null;
/*  950 */     int secondSeparator = id.indexOf(",", firstSeparator + 1);
/*  951 */     if (secondSeparator == -1)
/*  952 */       return null;
/*  953 */     String objectStoreIdentity = id.substring(firstSeparator + 1, secondSeparator);
/*  954 */     return objectStoreIdentity;
/*      */   }
/*      */   
/*      */   public static ObjectStore getObjectStore(P8Connection connection, String objectStoreIdOrName) {
/*  958 */     if (objectStoreIdOrName == null) {
/*  959 */       objectStoreIdOrName = connection.getObjectStoreName();
/*      */     }
/*  961 */     ObjectStore os = connection.getObjectStore(objectStoreIdOrName);
/*  962 */     if (os != null) {
/*  963 */       return os;
/*      */     }
/*  965 */     Domain defaultDomain = connection.getDomain();
/*  966 */     if (Id.isId(objectStoreIdOrName)) {
/*  967 */       os = Factory.ObjectStore.fetchInstance(defaultDomain, new Id(objectStoreIdOrName), null);
/*      */     } else {
/*  969 */       os = Factory.ObjectStore.fetchInstance(defaultDomain, objectStoreIdOrName, null);
/*      */       
/*  971 */       ObjectStore storedOS = connection.getObjectStore(os.get_Id().toString());
/*  972 */       if (storedOS != null)
/*      */       {
/*      */ 
/*  975 */         os = storedOS;
/*      */       }
/*      */     }
/*  978 */     connection.addObjectStore(objectStoreIdOrName, os);
/*      */     
/*  980 */     return os;
/*      */   }
/*      */   
/*      */   public static ObjectStore getObjectStore(HttpServletRequest request, P8Connection connection, String itemId) {
/*  984 */     String osId = (itemId != null) && (itemId.length() > 0) ? getObjectStoreIdentity(itemId) : null;
/*  985 */     if (osId == null)
/*  986 */       osId = request.getParameter("objectStoreId");
/*  987 */     ObjectStore os = (osId != null) && (osId.length() > 0) ? getObjectStore(connection, osId) : connection.getObjectStore();
/*      */     
/*  989 */     return os;
/*      */   }
/*      */   
/*      */   public static String getObjectStoreIdentityFromName(P8Connection connection, String objectStoreName) {
/*  993 */     ObjectStore os = getObjectStore(connection, objectStoreName);
/*  994 */     return os != null ? os.get_Id().toString() : null;
/*      */   }
/*      */   
/*      */   public static IndependentlyPersistableObject getPersistableObject(HttpServletRequest request, P8Connection connection, String docId, String objectStoreId, String vsId, String version) {
/*  998 */     IndependentlyPersistableObject independentlyPersistableObject = null;
/*  999 */     if (((vsId == null) || (vsId.isEmpty())) && ((version == null) || (version.isEmpty()))) {
/* 1000 */       independentlyPersistableObject = getPersistableObject(request, connection, docId);
/*      */     } else {
/* 1002 */       Object vsObject = getFromVersionSeries(request, connection, docId != null ? new P8DocID(docId) : null, objectStoreId, vsId, version, null);
/* 1003 */       if ((vsObject instanceof IndependentlyPersistableObject)) {
/* 1004 */         independentlyPersistableObject = (IndependentlyPersistableObject)vsObject;
/*      */       }
/*      */     }
/* 1007 */     return independentlyPersistableObject;
/*      */   }
/*      */   
/*      */   public static IndependentlyPersistableObject getPersistableObject(HttpServletRequest request, P8Connection connection, String docId) {
/* 1011 */     ObjectStore os = getObjectStore(request, connection, docId);
/* 1012 */     return getPersistableObject(os, connection, docId);
/*      */   }
/*      */   
/*      */   public static IndependentlyPersistableObject getPersistableObject(ObjectStore objectStore, P8Connection connection, String docId) {
/* 1016 */     String methodName = "IndependentlyPersistableObject(objectStore,connection,docId)";
/* 1017 */     if (docId.startsWith("/")) {
/* 1018 */       return Factory.Folder.fetchInstance(objectStore, docId, null);
/*      */     }
/*      */     
/* 1021 */     String classIdentity = null;
/* 1022 */     String objectIdentity = null;
/*      */     
/* 1024 */     int idSeparator = docId.indexOf(",");
/*      */     
/* 1026 */     if (idSeparator == -1) {
/* 1027 */       objectIdentity = docId;
/*      */     } else {
/* 1029 */       P8DocID p8DocID = new P8DocID(docId);
/*      */       
/* 1031 */       classIdentity = p8DocID.getClassID();
/* 1032 */       objectIdentity = p8DocID.getObjectID();
/*      */       
/*      */ 
/* 1035 */       String objectStoreId = p8DocID.getObjectStoreID();
/* 1036 */       if (!objectStoreId.equals(objectStore.get_Id().toString())) {
/* 1037 */         objectStore = getObjectStore(connection, objectStoreId);
/*      */       }
/*      */     }
/*      */     
/* 1041 */     Id id = new Id(objectIdentity);
/*      */     IndependentlyPersistableObject independentlyPersistableObject;
/* 1043 */     IndependentlyPersistableObject independentlyPersistableObject; if (classIdentity != null) { IndependentlyPersistableObject independentlyPersistableObject;
/* 1044 */       if (classIdentity.equalsIgnoreCase("Document")) {
/* 1045 */         independentlyPersistableObject = Factory.Document.fetchInstance(objectStore, id, null); } else { IndependentlyPersistableObject independentlyPersistableObject;
/* 1046 */         if (classIdentity.equalsIgnoreCase("Folder")) {
/* 1047 */           independentlyPersistableObject = Factory.Folder.fetchInstance(objectStore, id, null);
/*      */         } else {
/* 1049 */           ClassDescription classDescription = Factory.ClassDescription.fetchInstance(objectStore, classIdentity, null);
/* 1050 */           IndependentlyPersistableObject independentlyPersistableObject; if (classDescription.describedIsOfClass("Document").booleanValue()) {
/* 1051 */             independentlyPersistableObject = Factory.Document.fetchInstance(objectStore, id, null); } else { IndependentlyPersistableObject independentlyPersistableObject;
/* 1052 */             if (classDescription.describedIsOfClass("Folder").booleanValue()) {
/* 1053 */               independentlyPersistableObject = Factory.Folder.fetchInstance(objectStore, id, null);
/*      */             } else
/* 1055 */               independentlyPersistableObject = Factory.CustomObject.fetchInstance(objectStore, id, null);
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/* 1060 */       try { independentlyPersistableObject = Factory.Document.fetchInstance(objectStore, id, null);
/*      */       } catch (Exception e) {
/*      */         try {
/* 1063 */           independentlyPersistableObject = Factory.Folder.fetchInstance(objectStore, id, null);
/*      */         } catch (Exception e2) {
/* 1065 */           independentlyPersistableObject = Factory.CustomObject.fetchInstance(objectStore, id, null);
/*      */         }
/*      */       }
/*      */     }
/* 1069 */     return independentlyPersistableObject;
/*      */   }
/*      */   
/*      */   public static Document getDocument(HttpServletRequest request, P8Connection connection, String docID) {
/* 1073 */     P8DocID p8DocID = new P8DocID(docID);
/* 1074 */     return getDocument(request, connection, p8DocID, null, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Document getDocument(HttpServletRequest request, P8Connection connection, P8DocID p8DocID, String vsId, String version, PropertyFilter filter)
/*      */   {
/* 1103 */     String methodName = "getDocument";
/*      */     String objectStoreIdentity;
/* 1105 */     String objectStoreIdentity; if (p8DocID != null) {
/* 1106 */       objectStoreIdentity = p8DocID.getObjectStoreID();
/*      */     } else {
/* 1108 */       objectStoreIdentity = request.getParameter("objectStoreId");
/*      */       
/* 1110 */       if ((objectStoreIdentity == null) || (objectStoreIdentity.isEmpty())) {
/* 1111 */         objectStoreIdentity = connection.getObjectStore().get_Id().toString();
/*      */       }
/*      */     }
/* 1114 */     Document document = null;
/*      */     
/* 1116 */     if ((vsId == null) || (vsId.isEmpty()))
/*      */     {
/* 1118 */       String objectIdentity = p8DocID.getObjectID();
/* 1119 */       Id id = new Id(objectIdentity);
/* 1120 */       ObjectStore os = getObjectStore(request, connection, p8DocID.toString());
/* 1121 */       Logger.logDebug(P8Util.class, methodName, request, "Retrieving the document using docver id " + objectIdentity + " in objectstore " + objectStoreIdentity + " (vsId was not specified)");
/* 1122 */       document = Factory.Document.fetchInstance(os, id, filter);
/*      */       
/* 1124 */       if ((version != null) && (!version.isEmpty())) {
/* 1125 */         vsId = document.get_VersionSeries().get_Id().toString();
/* 1126 */         document = getDocument(request, connection, p8DocID, vsId, version, filter);
/*      */       }
/*      */     } else {
/* 1129 */       Object vsObject = getFromVersionSeries(request, connection, p8DocID, objectStoreIdentity, vsId, version, filter);
/* 1130 */       if ((!(vsObject instanceof Document)) && (vsObject != null)) {
/* 1131 */         String className = vsObject.getClass().getName();
/* 1132 */         Logger.logError(vsObject, methodName, request, "Expected a Document, but returned object is: " + className);
/*      */ 
/*      */       }
/* 1135 */       else if (vsObject != null) {
/* 1136 */         document = (Document)vsObject;
/*      */       }
/*      */     }
/*      */     
/* 1140 */     return document;
/*      */   }
/*      */   
/*      */   private static Object getFromVersionSeries(HttpServletRequest request, P8Connection connection, P8DocID p8DocID, String objectStoreId, String vsId, String version, PropertyFilter propertyFilter) {
/* 1144 */     String methodName = "getFromVersionSeries";
/* 1145 */     String objectStoreIdentity = null;
/* 1146 */     ObjectStore os = null;
/*      */     
/* 1148 */     if (p8DocID != null) {
/* 1149 */       String osId = p8DocID.getObjectStoreID();
/* 1150 */       if (osId != null) {
/* 1151 */         os = getObjectStore(connection, osId);
/* 1152 */         objectStoreIdentity = osId;
/*      */       }
/*      */     }
/*      */     
/* 1156 */     if (objectStoreIdentity == null) {
/* 1157 */       if ((objectStoreId != null) && (!objectStoreId.isEmpty())) {
/* 1158 */         objectStoreIdentity = objectStoreId;
/* 1159 */         os = getObjectStore(connection, objectStoreId);
/*      */       } else {
/* 1161 */         objectStoreIdentity = request.getParameter("objectStoreId");
/*      */         
/* 1163 */         if ((objectStoreIdentity == null) || (objectStoreIdentity.isEmpty())) {
/* 1164 */           objectStoreIdentity = connection.getObjectStore().get_Id().toString();
/*      */         }
/* 1166 */         os = getObjectStore(connection, objectStoreId);
/*      */       }
/*      */     }
/*      */     
/* 1170 */     Object vsObject = null;
/* 1171 */     if ((os != null) && (objectStoreIdentity != null))
/*      */     {
/*      */ 
/* 1174 */       Id id = new Id(vsId);
/* 1175 */       VersionSeries versionSeries = Factory.VersionSeries.fetchInstance(os, id, propertyFilter);
/*      */       
/* 1177 */       Logger.logDebug(P8Util.class, methodName, request, "Retrieving the document using version series id " + vsId + " in objectstore " + objectStoreIdentity);
/*      */       
/* 1179 */       if ((version == null) || (version.equalsIgnoreCase("released"))) {
/* 1180 */         Logger.logDebug(P8Util.class, methodName, request, "Checking for releasedVersion of versionSeries " + vsId);
/* 1181 */         vsObject = versionSeries.get_ReleasedVersion();
/*      */       }
/*      */       
/* 1184 */       if (vsObject == null) {
/* 1185 */         Logger.logDebug(P8Util.class, methodName, request, "Getting the currentVersion of versionSeries " + vsId);
/* 1186 */         vsObject = versionSeries.get_CurrentVersion();
/*      */       }
/* 1188 */       if (vsObject == null) {
/* 1189 */         Logger.logDebug(P8Util.class, methodName, request, "Getting the reservation of versionSeries " + vsId);
/* 1190 */         vsObject = versionSeries.get_Reservation();
/*      */       }
/*      */     }
/*      */     else {
/* 1194 */       Logger.logDebug(P8Util.class, methodName, request, "Object store was not specified, or could not be found.");
/*      */     }
/*      */     
/* 1197 */     return vsObject;
/*      */   }
/*      */   
/*      */   public static List<Integer> getContentElementList(HttpServletRequest request, P8Connection p8Connection, P8DocID p8DocID, String vsId, String version) {
/* 1201 */     List<Integer> contentElementList = new ArrayList();
/*      */     try
/*      */     {
/* 1204 */       UserContext.get().pushSubject(p8Connection.getSubject());
/*      */       
/* 1206 */       Document document = getDocument(request, p8Connection, p8DocID, vsId, version, null);
/* 1207 */       if (document != null) {
/* 1208 */         StringList list = document.get_ContentElementsPresent();
/* 1209 */         if (list.size() > 1) {
/* 1210 */           ContentElementList p8ContentElementList = document.get_ContentElements();
/* 1211 */           for (int idx = 0; idx < p8ContentElementList.size(); idx++) {
/* 1212 */             if ((p8ContentElementList.get(idx) instanceof ContentTransfer)) {
/* 1213 */               contentElementList.add(Integer.valueOf(idx));
/*      */             }
/*      */           }
/*      */         }
/* 1217 */         saveAsLastDocument(request.getSession(), document, p8DocID.getObjectID(), vsId, version);
/*      */       }
/*      */     } finally {
/* 1220 */       UserContext.get().popSubject();
/*      */     }
/*      */     
/* 1223 */     return contentElementList;
/*      */   }
/*      */   
/*      */   static void saveAsLastDocument(HttpSession session, Document document, String id, String vsId, String version) {
/* 1227 */     synchronized (session) {
/* 1228 */       LastDocument lastDocument = new LastDocument(document, id, vsId, version, null);
/* 1229 */       session.setAttribute("P8RetrieveDocument.LastDocument", lastDocument);
/*      */     }
/*      */   }
/*      */   
/*      */   public static Folder getFolder(HttpServletRequest request, P8Connection connection, String docId) {
/* 1234 */     int firstSeparator = docId.indexOf(",");
/* 1235 */     int secondSeparator = docId.indexOf(",", firstSeparator + 1);
/* 1236 */     String objectIdentity = docId.substring(secondSeparator + 1);
/* 1237 */     Id id = new Id(objectIdentity);
/* 1238 */     return Factory.Folder.fetchInstance(getObjectStore(request, connection, docId), id, null);
/*      */   }
/*      */   
/*      */   public static String getFolderPathName(ObjectStore objectStore, String folderId) {
/* 1242 */     PropertyFilter pf = new PropertyFilter();
/* 1243 */     pf.addIncludeProperty(0, null, null, "PathName", null);
/* 1244 */     Folder ceFolder = (Folder)objectStore.fetchObject("Folder", folderId, pf);
/* 1245 */     return ceFolder.get_PathName();
/*      */   }
/*      */   
/*      */   public static ClassDescription getCachedCD(ObjectStore objectStore, String className) {
/* 1249 */     MetadataCache cmc = Factory.MetadataCache.getDefaultInstance();
/* 1250 */     return cmc.getClassDescription(objectStore, className);
/*      */   }
/*      */   
/*      */   public static boolean isDocumentClass(ClassDescription cd) {
/* 1254 */     MetadataCache cmc = Factory.MetadataCache.getDefaultInstance();
/* 1255 */     return cmc.describedIsOfClass(cd, "Document");
/*      */   }
/*      */   
/*      */   public static boolean isFolderClass(ClassDescription cd) {
/* 1259 */     MetadataCache cmc = Factory.MetadataCache.getDefaultInstance();
/* 1260 */     return cmc.describedIsOfClass(cd, "Folder");
/*      */   }
/*      */   
/*      */   public static String getClassDisplayName(ObjectStore objectStore, String className) {
/* 1264 */     ClassDescription cd = getCachedCD(objectStore, className);
/* 1265 */     return cd.get_DisplayName();
/*      */   }
/*      */   
/*      */   public static String getPropertyDisplayName(ObjectStore objectStore, String className, String propertyName) {
/* 1269 */     String displayName = null;
/* 1270 */     PropertyDescription pd = getPropertyDescription(objectStore, className, propertyName);
/* 1271 */     if (pd != null)
/* 1272 */       displayName = pd.get_DisplayName();
/* 1273 */     return displayName;
/*      */   }
/*      */   
/*      */   public static PropertyDescription getPropertyDescription(ObjectStore objectStore, String className, String propertyName) {
/* 1277 */     ClassDescription cd = getCachedCD(objectStore, className);
/* 1278 */     PropertyDescription pd = getPropertyDescription(cd, propertyName);
/* 1279 */     if (pd == null) {
/* 1280 */       PropertyDescriptionList pds = getPropertyDescriptions(cd, "ProperSubclassPropertyDescriptions");
/* 1281 */       pd = getPropertyDescription(pds, propertyName);
/*      */     }
/* 1283 */     return pd;
/*      */   }
/*      */   
/*      */   public static String getPropertyDisplayName(HttpServletRequest request, PropertyDescription propertyDescription) {
/* 1287 */     String displayName = propertyDescription.get_DisplayName();
/* 1288 */     if (propertyDescription.get_IsSystemOwned().booleanValue()) {
/* 1289 */       if (isDefaultLabel(propertyDescription.get_SymbolicName())) {
/* 1290 */         String messageKey = "systemp8." + propertyDescription.get_SymbolicName() + ".defaultLabel";
/* 1291 */         displayName = MessageUtil.getMessage(request.getLocale(), messageKey);
/*      */       }
/*      */       
/* 1294 */       String customLabel = BaseUtil.getCustomLabelValue(request, "sys_" + propertyDescription.get_SymbolicName());
/* 1295 */       if (customLabel != null) {
/* 1296 */         displayName = customLabel;
/*      */       }
/*      */     }
/* 1299 */     return displayName;
/*      */   }
/*      */   
/*      */   public static ArrayList<PropertyDescription> getPropertyDescriptions(ClassDescription classDesc, boolean includeSubclasses, boolean sort, boolean searchableOnly, boolean systemOnly, boolean includeOVPBinary) {
/* 1303 */     List<PropertyDescription> propertyDescriptionList = new ArrayList();
/* 1304 */     PropertyDescriptionList pdl = getPropertyDescriptions(classDesc, "PropertyDescriptions");
/* 1305 */     propertyDescriptionList.addAll(pdl);
/* 1306 */     if (includeSubclasses) {
/* 1307 */       PropertyDescriptionList pdlSubclasses = getPropertyDescriptions(classDesc, "ProperSubclassPropertyDescriptions");
/* 1308 */       propertyDescriptionList.addAll(pdlSubclasses);
/*      */     }
/*      */     
/* 1311 */     int capacity = propertyDescriptionList.size();
/* 1312 */     Map<String, PropertyDescription> propertyDescriptionMap = sort ? new LinkedHashMap(capacity) : new HashMap(capacity);
/* 1313 */     Map<String, Object> sortedMap = sort ? new TreeMap(COLLATOR) : null;
/* 1314 */     ArrayList<PropertyDescription> pdl1 = new ArrayList();
/*      */     
/*      */ 
/* 1317 */     for (PropertyDescription propertyDesc : propertyDescriptionList) {
/* 1318 */       if ((propertyDesc.get_SymbolicName().equals("CmThumbnails")) || (
/* 1319 */         ((propertyDesc.get_IsSearchable().booleanValue()) || (!searchableOnly)) && 
/*      */         
/*      */ 
/* 1322 */         ((propertyDesc.get_IsSystemOwned().booleanValue()) || (!systemOnly)) && (
/*      */         
/*      */ 
/* 1325 */         (includeOVPBinary) || ((propertyDesc.get_DataType() != TypeID.OBJECT) && (propertyDesc.get_DataType() != TypeID.BINARY)))))
/*      */       {
/*      */ 
/*      */ 
/* 1329 */         if (sort)
/*      */         {
/* 1331 */           String displayName = null;
/* 1332 */           if (displayName == null) {
/* 1333 */             displayName = propertyDesc.get_DisplayName();
/*      */           }
/*      */           
/*      */ 
/* 1337 */           if (sortedMap.containsKey(displayName)) {
/* 1338 */             Object value = sortedMap.get(displayName);
/*      */             List list;
/* 1340 */             List list; if ((value instanceof List)) {
/* 1341 */               list = (List)value;
/*      */             } else {
/* 1343 */               list = new ArrayList();
/* 1344 */               list.add(value);
/* 1345 */               sortedMap.put(displayName, list);
/*      */             }
/* 1347 */             list.add(propertyDesc);
/*      */           } else {
/* 1349 */             sortedMap.put(displayName, propertyDesc);
/*      */           }
/*      */         }
/*      */         else {
/* 1353 */           pdl1.add(propertyDesc);
/*      */         }
/*      */       }
/*      */     }
/* 1357 */     if (sort)
/*      */     {
/* 1359 */       for (String key : sortedMap.keySet()) {
/* 1360 */         Object value = sortedMap.get(key);
/* 1361 */         if ((value instanceof List)) {
/* 1362 */           List<PropertyDescription> pds = (List)value;
/* 1363 */           for (PropertyDescription propertyDesc : pds) {
/* 1364 */             propertyDescriptionMap.put(propertyDesc.get_SymbolicName(), propertyDesc);
/*      */           }
/*      */         } else {
/* 1367 */           PropertyDescription propertyDesc = (PropertyDescription)value;
/* 1368 */           propertyDescriptionMap.put(propertyDesc.get_SymbolicName(), propertyDesc);
/*      */         }
/*      */       }
/*      */       
/* 1372 */       for (PropertyDescription p : propertyDescriptionMap.values()) {
/* 1373 */         pdl1.add(p);
/*      */       }
/*      */     }
/*      */     
/* 1377 */     return pdl1;
/*      */   }
/*      */   
/*      */   private static PropertyDescriptionList getPropertyDescriptions(ClassDescription classDesc, String propName)
/*      */   {
/* 1382 */     Properties classDescProperties = classDesc.getProperties();
/* 1383 */     PropertyDescriptionList pdl; PropertyDescriptionList pdl; if (classDescProperties.isPropertyPresent(propName)) {
/* 1384 */       Property prop = classDescProperties.get(propName);
/* 1385 */       pdl = (PropertyDescriptionList)prop.getObjectValue();
/*      */     }
/*      */     else {
/* 1388 */       PropertyFilter pf = new PropertyFilter();
/* 1389 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "SymbolicName", null));
/* 1390 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "DisplayName", null));
/* 1391 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "DataType", null));
/* 1392 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Cardinality", null));
/* 1393 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IsOrderable", null));
/* 1394 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IsSystemOwned", null));
/* 1395 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "UsesLongColumn", null));
/* 1396 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IsSearchable", null));
/* 1397 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "MarkingSet", null));
/* 1398 */       Property prop = classDesc.fetchProperty(propName, pf);
/* 1399 */       pdl = (PropertyDescriptionList)prop.getObjectValue();
/*      */     }
/*      */     
/* 1402 */     return pdl;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<AccessPermissionDescription> getPermissionsDescriptions(ClassDescription classDesc)
/*      */   {
/* 1413 */     List<AccessPermissionDescription> permissionDescriptions = new ArrayList();
/*      */     
/* 1415 */     AccessPermissionDescriptionList apdl = classDesc.get_PermissionDescriptions();
/* 1416 */     for (Iterator it = apdl.iterator(); it.hasNext();) {
/* 1417 */       AccessPermissionDescription apd = (AccessPermissionDescription)it.next();
/* 1418 */       permissionDescriptions.add(apd);
/*      */     }
/*      */     
/* 1421 */     return permissionDescriptions;
/*      */   }
/*      */   
/*      */   public static int getPropertyIntegerValue(IndependentObject object, String propertyName) {
/* 1425 */     Properties props = object.getProperties();
/* 1426 */     if (!props.isPropertyPresent(propertyName)) {
/* 1427 */       object.fetchProperties(new String[] { propertyName });
/* 1428 */       props = object.getProperties();
/*      */     }
/*      */     
/* 1431 */     return (props.isPropertyPresent(propertyName) ? props.getInteger32Value(propertyName) : null).intValue();
/*      */   }
/*      */   
/*      */   public static boolean isDefaultLabel(String propertyName) {
/* 1435 */     String[] defaultLabels = { "Creator", "DateCreated", "LastModifier", "DateLastModified", "IsReserved", "MajorVersionNumber", "MinorVersionNumber", "ContentSize", "MimeType" };
/* 1436 */     for (int i = 0; i < defaultLabels.length; i++) {
/* 1437 */       if (defaultLabels[i].equalsIgnoreCase(propertyName)) {
/* 1438 */         return true;
/*      */       }
/*      */     }
/* 1441 */     return false;
/*      */   }
/*      */   
/*      */   public static void appendAccessPermission(AccessPermissionList apl, String user, int accessmask, AccessType type, int inheritableDepth)
/*      */   {
/* 1446 */     AccessPermission ap = Factory.AccessPermission.createInstance();
/* 1447 */     ap.set_AccessMask(Integer.valueOf(accessmask));
/* 1448 */     ap.set_AccessType(type);
/* 1449 */     ap.set_InheritableDepth(Integer.valueOf(inheritableDepth));
/* 1450 */     ap.set_GranteeName(user);
/* 1451 */     apl.add(ap);
/*      */   }
/*      */   
/*      */   public static boolean classAllowsInstances(ObjectStore objectStore, ClassDescription classDescription) {
/* 1455 */     boolean allowsInstances = classDescription.getProperties().getBooleanValue("AllowsInstances").booleanValue();
/* 1456 */     if (allowsInstances) {
/* 1457 */       PropertyFilter pf = new PropertyFilter();
/* 1458 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(0), null, null, "Id", null));
/* 1459 */       ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(objectStore, classDescription.get_SymbolicName(), pf);
/* 1460 */       allowsInstances = (classDefinition.getAccessAllowed().intValue() & 0x100) == 256;
/*      */     }
/* 1462 */     return allowsInstances;
/*      */   }
/*      */   
/*      */   public static PropertyTemplate getPropertyTemplate(ObjectStore objectStore, Id propertyId) {
/* 1466 */     PropertyFilter pf = new PropertyFilter();
/* 1467 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Id", null));
/* 1468 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(0), null, null, "Name", null));
/* 1469 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(0), null, null, "ChoiceList", null));
/* 1470 */     PropertyTemplate propTemplate = Factory.PropertyTemplate.fetchInstance(objectStore, propertyId, pf);
/* 1471 */     return propTemplate;
/*      */   }
/*      */   
/*      */   public static boolean isCommonProperty(HttpServletRequest request, P8Connection connection, ObjectStore objectStore, String propertySymbolicName, String[] classSymbolicNames) {
/* 1475 */     if (propertySymbolicName == null) {
/* 1476 */       return false;
/*      */     }
/* 1478 */     if (classSymbolicNames == null)
/* 1479 */       classSymbolicNames = new String[] { "Document", "Folder" };
/* 1480 */     boolean common = true;
/* 1481 */     for (int i = 0; i < classSymbolicNames.length; i++) {
/* 1482 */       PropertyDescription pd = locatePDInThisCD(request, getCachedCD(objectStore, classSymbolicNames[i]), propertySymbolicName, true);
/* 1483 */       common = pd != null;
/* 1484 */       if (!common) {
/*      */         break;
/*      */       }
/*      */     }
/* 1488 */     return common;
/*      */   }
/*      */   
/*      */   public static PropertyDescription locatePDInThisCD(HttpServletRequest request, ClassDescription classDescription, String propertySymbolicName, boolean lookInSubclasses) {
/* 1492 */     String methodName = "locatePDInThisCD";
/* 1493 */     Logger.logEntry(P8Util.class, methodName, request);
/* 1494 */     String classSymbolicName = classDescription.get_SymbolicName();
/* 1495 */     String debugMsg = "propertySymbolicName = " + propertySymbolicName + ", classSymbolicName = " + classSymbolicName;
/* 1496 */     Logger.logDebug(P8Util.class, methodName, request, "Locate a property in class: " + debugMsg);
/* 1497 */     List<PropertyDescription> pdl = classDescription.get_PropertyDescriptions();
/* 1498 */     PropertyDescription p = null;
/* 1499 */     for (PropertyDescription pd : pdl) {
/* 1500 */       if (propertySymbolicName.equals(pd.get_SymbolicName())) {
/* 1501 */         p = pd;
/* 1502 */         Logger.logDebug(P8Util.class, methodName, request, "Property found: " + debugMsg);
/* 1503 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1507 */     if (lookInSubclasses) {
/* 1508 */       pdl = classDescription.get_ProperSubclassPropertyDescriptions();
/* 1509 */       for (PropertyDescription pd : pdl) {
/* 1510 */         if (propertySymbolicName.equals(pd.get_SymbolicName())) {
/* 1511 */           p = pd;
/* 1512 */           Logger.logDebug(P8Util.class, methodName, request, "Property found in subclass: " + debugMsg);
/* 1513 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1518 */     Logger.logExit(P8Util.class, methodName, request);
/* 1519 */     return p;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean ifCE52OrAbove(ObjectStore os)
/*      */   {
/* 1529 */     boolean result = false;
/* 1530 */     if (os != null) {
/* 1531 */       String schema = os.getProperties().getStringValue("SchemaVersion");
/* 1532 */       int index = schema.indexOf(".");
/* 1533 */       if (index < 0) {
/* 1534 */         index = schema.length();
/*      */       }
/* 1536 */       String mainNumber = schema.substring(0, index);
/* 1537 */       if (Integer.parseInt(mainNumber) >= 18) {
/* 1538 */         result = true;
/*      */       }
/*      */     }
/* 1541 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void initCMCconfig(HttpServletRequest request, int cmcTTL)
/*      */     throws Exception
/*      */   {
/* 1555 */     String methodName = "initCMCconfig";
/* 1556 */     Logger.logEntry(P8Util.class, methodName, request);
/*      */     
/*      */     try
/*      */     {
/* 1560 */       long ttl = cmcTTL * 1000L;
/* 1561 */       ConfigurationParameters parameters = new ConfigurationParameters();
/* 1562 */       parameters.setParameter(ConfigurationParameter.CMC_TIME_TO_LIVE, Long.valueOf(ttl));
/* 1563 */       Configuration.init(parameters);
/*      */     } catch (EngineRuntimeException ere) {
/* 1565 */       if (!ere.getExceptionCode().equals(ExceptionCode.API_INITIALIZED)) {
/* 1566 */         Logger.logError(P8Util.class, methodName, (HttpServletRequest)null, ere);
/* 1567 */         throw ere;
/*      */       }
/*      */     } catch (Exception e) {
/* 1570 */       Logger.logError(P8Util.class, methodName, (HttpServletRequest)null, e);
/* 1571 */       throw e;
/*      */     }
/* 1573 */     Logger.logExit(P8Util.class, methodName, request);
/*      */   }
/*      */   
/*      */   private static class LastDocument
/*      */   {
/*      */     private static final String LAST_DOCUMENT_KEY = "P8RetrieveDocument.LastDocument";
/*      */     long timestamp;
/*      */     transient Document document;
/*      */     String id;
/*      */     String vsId;
/*      */     String key;
/*      */     String version;
/*      */     
/*      */     private LastDocument(Document document, String id, String vsId, String version) {
/* 1587 */       this.timestamp = System.currentTimeMillis();
/* 1588 */       this.document = document;
/* 1589 */       this.id = id;
/* 1590 */       this.vsId = vsId;
/* 1591 */       this.version = version;
/* 1592 */       this.key = getDocumentKey(id, vsId, version);
/*      */     }
/*      */     
/*      */     private static String getDocumentKey(String id, String vsId, String version) {
/* 1596 */       return String.valueOf(id) + String.valueOf(vsId) + String.valueOf(version);
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */