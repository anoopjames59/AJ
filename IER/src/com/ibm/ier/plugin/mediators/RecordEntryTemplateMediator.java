/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.PropertyUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.ier.plugin.util.model.RecordEntryTemplate;
/*     */ import com.ibm.ier.plugin.util.model.RecordEntryTemplate.FolderLocation;
/*     */ import com.ibm.ier.plugin.util.model.RecordEntryTemplate.Option;
/*     */ import com.ibm.ier.plugin.util.model.RecordEntryTemplate.PropertyOptions;
/*     */ import com.ibm.ier.plugin.util.model.RecordEntryTemplate.Reference;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import com.ibm.json.java.OrderedJSONObject;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public class RecordEntryTemplateMediator
/*     */   extends BaseMediator
/*     */ {
/*     */   private static final long serialVersionUID = 324234L;
/*  40 */   private BaseEntity recordEntryTemplateBaseEntity = null;
/*     */   
/*     */   public void setRecordEntryTemplateBaseEntity(BaseEntity baseObj) {
/*  43 */     this.recordEntryTemplateBaseEntity = baseObj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONObject toJSONObject()
/*     */     throws Exception
/*     */   {
/*  53 */     RMProperties properties = this.recordEntryTemplateBaseEntity.getProperties();
/*  54 */     RecordEntryTemplate recordEntryTemplate = RecordEntryTemplate.loadEntryTemplate(this.servletRequest, this.baseService.getRepository(), this.recordEntryTemplateBaseEntity.getClientIdentifier());
/*     */     
/*  56 */     JSONObject jsonObject = super.toJSONObject();
/*     */     
/*     */ 
/*  59 */     jsonObject.put("id", IERUtil.getDocId(this.recordEntryTemplateBaseEntity));
/*  60 */     jsonObject.put("displayName", this.recordEntryTemplateBaseEntity.getName());
/*  61 */     jsonObject.put("description", properties.getStringValue("EntryTemplateDescription"));
/*     */     
/*  63 */     jsonObject.put("entrytemplate_type", properties.getStringValue("TargetObjectType"));
/*     */     
/*  65 */     Repository targetRepo = this.baseService.getRepository(properties.getStringValue("TargetObjectStoreName"));
/*  66 */     RMClassDescription cd = SessionUtil.getClassDescription(targetRepo, recordEntryTemplate.getRecordClassId(), this.servletRequest);
/*     */     
/*  68 */     jsonObject.put("targetRecordObjectStoreId", properties.getStringValue("TargetObjectStoreName"));
/*  69 */     jsonObject.put("targetRecordObjectStoreDisplayName", targetRepo.getName());
/*  70 */     jsonObject.put("selectedRecordClassId", cd.getSymbolicName());
/*  71 */     jsonObject.put("selectedRecordClassLabel", cd.getName());
/*     */     
/*     */ 
/*     */ 
/*  75 */     jsonObject.put("repositoryId", properties.getStringValue("TargetObjectStoreName"));
/*  76 */     jsonObject.put("showPropertiesStep", Boolean.valueOf(recordEntryTemplate.getShowPropertiesStep()));
/*  77 */     jsonObject.put("showSelectFilePlanLocationStep", Boolean.valueOf(recordEntryTemplate.getShowSelectFilePlanLocationStep()));
/*     */     
/*     */ 
/*     */ 
/*  81 */     JSONArray jsonProperties = new JSONArray();
/*  82 */     Collection<RecordEntryTemplate.PropertyOptions> propsOptions = recordEntryTemplate.getPropertiesOptions().values();
/*  83 */     Iterator<RecordEntryTemplate.PropertyOptions> iter = propsOptions.iterator();
/*     */     
/*  85 */     while (iter.hasNext()) {
/*  86 */       RecordEntryTemplate.PropertyOptions propOptions = (RecordEntryTemplate.PropertyOptions)iter.next();
/*     */       
/*  88 */       JSONObject jsonPropertyOpts = new OrderedJSONObject();
/*     */       
/*  90 */       jsonPropertyOpts.put("id", propOptions.getId());
/*  91 */       jsonPropertyOpts.put("name", propOptions.getName());
/*  92 */       jsonPropertyOpts.put("required", Boolean.valueOf(propOptions.isRequired()));
/*  93 */       jsonPropertyOpts.put("readOnly", Boolean.valueOf(propOptions.isReadOnly()));
/*  94 */       jsonPropertyOpts.put("hidden", Boolean.valueOf(propOptions.isHidden()));
/*  95 */       jsonPropertyOpts.put("cardinality", propOptions.getCardinalityName());
/*  96 */       jsonPropertyOpts.put("dataType", MediatorUtil.getJSONDataType(propOptions.getDataType()));
/*     */       
/*  98 */       DataType dataType = propOptions.getDataType();
/*     */       
/* 100 */       if (dataType == DataType.Object)
/*     */       {
/*     */ 
/* 103 */         OrderedJSONObject jsonRequiredClass = new OrderedJSONObject();
/* 104 */         String requiredClassName = propOptions.getRequiredClassName();
/* 105 */         jsonRequiredClass.put("template_id", requiredClassName);
/* 106 */         jsonPropertyOpts.put("required_template", jsonRequiredClass);
/*     */         
/*     */ 
/* 109 */         RecordEntryTemplate.Reference defObjValue = propOptions.getDefaultObjectValue();
/*     */         
/* 111 */         if (defObjValue != null) {
/* 112 */           String defObjId = defObjValue.getId();
/*     */           
/* 114 */           if (!defObjId.isEmpty()) {
/* 115 */             RecordEntryTemplate.Reference defObjObjectStore = propOptions.getDefaultObjectValueObjectStore();
/*     */             
/* 117 */             JSONArray jsonValues = new JSONArray();
/* 118 */             jsonValues.add(IERUtil.getDocId(propOptions.getRequiredClassName(), defObjObjectStore.getId(), defObjId));
/* 119 */             jsonPropertyOpts.put("defaultValue", jsonValues);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 124 */         Object defValue = propOptions.getDefaultValue();
/* 125 */         if (defValue != null) {
/* 126 */           JSONArray jsonValues = new JSONArray();
/* 127 */           if ((propOptions.getCardinality() == RMCardinality.List) || (propOptions.getCardinality() == RMCardinality.Enumeration))
/*     */           {
/* 129 */             List<String> values = (List)defValue;
/* 130 */             for (String value : values) {
/* 131 */               parseDefaultValue(value, dataType, jsonValues);
/*     */             }
/*     */           } else {
/* 134 */             parseDefaultValue(defValue.toString(), dataType, jsonValues);
/*     */           }
/* 136 */           jsonPropertyOpts.put("defaultValue", jsonValues);
/*     */         }
/*     */       }
/*     */       
/* 140 */       jsonProperties.add(jsonPropertyOpts);
/*     */     }
/* 142 */     jsonObject.put("propertiesOptions", jsonProperties);
/*     */     
/*     */ 
/* 145 */     RecordEntryTemplate.Option showRecordClass = recordEntryTemplate.getShowRecordClassSelection();
/* 146 */     JSONObject jsonRecordClass = new JSONObject();
/* 147 */     jsonRecordClass.put("readOnly", Boolean.valueOf(showRecordClass.isReadOnly()));
/* 148 */     jsonRecordClass.put("hidden", Boolean.valueOf(showRecordClass.isHidden()));
/* 149 */     jsonObject.put("showRecordClassSelection", jsonRecordClass);
/*     */     
/*     */ 
/* 152 */     RecordEntryTemplate.Option showFilePlanLocation = recordEntryTemplate.getShowSelectFilePlanLocation();
/* 153 */     JSONObject jsonShowFilePlanLocation = new JSONObject();
/* 154 */     jsonShowFilePlanLocation.put("readOnly", Boolean.valueOf(showFilePlanLocation.isReadOnly()));
/* 155 */     jsonShowFilePlanLocation.put("hidden", Boolean.valueOf(showFilePlanLocation.isHidden()));
/* 156 */     jsonObject.put("showFilePlanLocationSelection", jsonShowFilePlanLocation);
/*     */     
/*     */ 
/* 159 */     List<RecordEntryTemplate.FolderLocation> filePlanLocations = recordEntryTemplate.getFilePlanLocations();
/* 160 */     if (filePlanLocations.size() > 0) {
/* 161 */       JSONArray jsonfilePlanLocations = new JSONArray();
/* 162 */       for (RecordEntryTemplate.FolderLocation filePlanLocation : filePlanLocations) {
/* 163 */         JSONObject jsonFilePlanLocation = new JSONObject();
/* 164 */         jsonFilePlanLocation.put("folderId", filePlanLocation.getId());
/* 165 */         jsonFilePlanLocation.put("objectStore", filePlanLocation.getObjectStoreId());
/* 166 */         jsonfilePlanLocations.add(jsonFilePlanLocation);
/*     */       }
/* 168 */       jsonObject.put("filePlanFolderLocations", jsonfilePlanLocations);
/*     */     }
/*     */     
/* 171 */     jsonObject.put("primaryFilePlanLocation", recordEntryTemplate.getPrimaryFilePlanLocationId());
/*     */     
/*     */ 
/* 174 */     jsonObject.put("constrainStartingFilePlanLocation", Boolean.valueOf(recordEntryTemplate.isConstrainFolder()));
/*     */     
/*     */ 
/* 177 */     jsonObject.put("showClassAndLocationSelectorsStep", Boolean.valueOf(recordEntryTemplate.getShowSelectFilePlanLocationStep()));
/*     */     
/*     */ 
/* 180 */     jsonObject.put("showPropertiesStep", Boolean.valueOf(recordEntryTemplate.getShowPropertiesStep()));
/*     */     
/*     */ 
/* 183 */     jsonObject.put("startingFilePlanLocationId", recordEntryTemplate.getStartingFilePlanLocationId());
/*     */     
/* 185 */     return jsonObject;
/*     */   }
/*     */   
/*     */   public void parseDefaultValue(String defValue, DataType dataType, JSONArray jsonValues) {
/* 189 */     if ((dataType == DataType.DateTime) || (dataType == DataType.Boolean) || (dataType == DataType.Double)) {
/* 190 */       jsonValues.add(defValue.toString());
/*     */     } else {
/* 192 */       jsonValues.add(PropertyUtil.getDataTypeOutputFromString(defValue.toString(), dataType));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\RecordEntryTemplateMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */