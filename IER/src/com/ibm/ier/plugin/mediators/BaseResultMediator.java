/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*     */ import com.ibm.ier.plugin.util.PropertyUtil;
/*     */ import com.ibm.ier.plugin.util.UserUtil;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
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
/*     */ public abstract class BaseResultMediator
/*     */   extends BaseMediator
/*     */ {
/*     */   protected static final long serialVersionUID = 3L;
/*     */   protected static final String NAME = "{NAME}";
/*     */   protected static final String CONTAINER_ID = "ContainerId";
/*  41 */   protected String nameProperty = null;
/*  42 */   protected FilePlanRepository fp_repository = null;
/*  43 */   protected List<String> requestedProperties = null;
/*  44 */   protected boolean isDocId = false;
/*     */   
/*     */   public BaseResultMediator(IERBaseService service, HttpServletRequest request) {
/*  47 */     super(service, request);
/*     */   }
/*     */   
/*     */   public BaseResultMediator(IERBaseService service, HttpServletRequest request, List<String> requestedProperties) {
/*  51 */     super(service, request);
/*     */     
/*  53 */     this.fp_repository = service.getFilePlanRepository();
/*  54 */     this.requestedProperties = requestedProperties;
/*     */   }
/*     */   
/*     */   public BaseResultMediator(FilePlanRepository repository, HttpServletRequest request, List<String> requestedProperties)
/*     */   {
/*  59 */     this.fp_repository = repository;
/*  60 */     setHttpServletRequest(request);
/*  61 */     this.requestedProperties = requestedProperties;
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(List<String> requestedProperties) {
/*  65 */     this.requestedProperties = requestedProperties;
/*     */   }
/*     */   
/*     */   public void setNameProperty(String nameProperty) {
/*  69 */     this.nameProperty = nameProperty;
/*     */   }
/*     */   
/*     */   public void setIsDocId(boolean isDocId) {
/*  73 */     this.isDocId = isDocId;
/*     */   }
/*     */   
/*     */   public abstract String getItemId() throws Exception;
/*     */   
/*     */   public abstract String getItemName();
/*     */   
/*     */   public abstract String getContentType();
/*     */   
/*     */   public abstract String getEntityType();
/*     */   
/*     */   public abstract String getClassName();
/*     */   
/*     */   public JSONObject toJSONObject(RMProperties properties) throws Exception
/*     */   {
/*  88 */     String methodName = "toJSONObject";
/*  89 */     HttpServletRequest request = getHttpServletRequest();
/*  90 */     Logger.logEntry(this, methodName, request);
/*  91 */     JSONObject jsonObject = super.toJSONObject();
/*     */     
/*  93 */     if (properties != null) {
/*  94 */       jsonObject.put("id", getItemId());
/*  95 */       jsonObject.put("name", getItemName());
/*  96 */       jsonObject.put("objectStoreDisplayName", this.fp_repository.getDisplayName());
/*  97 */       jsonObject.put("objectStoreName", this.fp_repository.getName());
/*  98 */       jsonObject.put("objectStoreId", this.fp_repository.getClientIdentifier());
/*  99 */       jsonObject.put("ierContentType", getContentType());
/* 100 */       jsonObject.put("mimetype", getContentType());
/* 101 */       jsonObject.put("nameProperty", this.nameProperty);
/* 102 */       jsonObject.put("attributes", buildAttributes(properties));
/* 103 */       jsonObject.put("entityType", getEntityType());
/* 104 */       jsonObject.put("template", getClassName());
/*     */     }
/*     */     
/* 107 */     Logger.logExit(this, methodName, request);
/* 108 */     return jsonObject;
/*     */   }
/*     */   
/*     */   public void setEntityJSON(BaseEntity item, JSONObject jsonObject, RMProperties properties) {
/* 112 */     if (item == null) { return;
/*     */     }
/* 114 */     if ((item instanceof Container)) {
/* 115 */       Container container = (Container)item;
/*     */       
/* 117 */       if ((container instanceof RecordContainer)) {
/* 118 */         RecordContainer recordContainer = (RecordContainer)container;
/*     */         
/* 120 */         jsonObject.put("isDefensibleDiposalContainer", Boolean.valueOf(recordContainer.isADefensiblyDisposableContainer()));
/*     */         
/*     */ 
/* 123 */         jsonObject.put("isClosed", Boolean.valueOf(!recordContainer.isOpen()));
/* 124 */         jsonObject.put("isReopen", Boolean.valueOf(recordContainer.isReopened()));
/*     */         
/*     */ 
/* 127 */         if (properties.isPropertyPresent("CurrentPhaseExecutionStatus")) {
/* 128 */           boolean isReadyForDisposition = recordContainer.isReadyForInitiateDisposition();
/* 129 */           jsonObject.put("isReadyForDisposition", Boolean.valueOf(isReadyForDisposition));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 134 */     if ((item instanceof Record)) {
/* 135 */       Record recordItem = (Record)item;
/* 136 */       if (properties.isPropertyPresent("MimeType")) {
/* 137 */         jsonObject.put("CEMimeType", properties.getStringValue("MimeType"));
/*     */       }
/*     */       
/* 140 */       jsonObject.put("isReadyForDisposition", Boolean.valueOf(recordItem.isReadyForInitiateDisposition()));
/*     */     }
/*     */     
/*     */ 
/* 144 */     if (properties.isPropertyPresent("CurrentPhaseExecutionStatus")) {
/* 145 */       Integer executionStatus = properties.getIntegerValue("CurrentPhaseExecutionStatus");
/* 146 */       boolean isDispositionInProgress = (executionStatus != null) && (executionStatus.intValue() == RMWorkflowStatus.Started.getIntValue());
/* 147 */       jsonObject.put("isDispositionInProgress", Boolean.valueOf(isDispositionInProgress));
/*     */       
/* 149 */       boolean isBasicScheduleDispositionInProgress = (executionStatus != null) && (executionStatus.intValue() >= 1000);
/* 150 */       jsonObject.put("isBasicScheduleInProgress", Boolean.valueOf(isBasicScheduleDispositionInProgress));
/*     */     }
/*     */   }
/*     */   
/*     */   public JSONObject buildAttributes(RMProperties properties)
/*     */   {
/* 156 */     String methodName = "buildAttributes";
/* 157 */     HttpServletRequest request = getHttpServletRequest();
/*     */     
/* 159 */     JSONObject attributesJSON = new JSONObject();
/* 160 */     if (properties != null) {
/* 161 */       String nameDisplayName = MessageResources.getLocalizedMessage(MessageCode.NAME, new Object[0]);
/* 162 */       Logger.logDebug(this, methodName, request, "properties count: " + properties.size());
/*     */       try
/*     */       {
/* 165 */         for (String requestedProperty : this.requestedProperties) {
/* 166 */           if (properties.isPropertyPresent(requestedProperty)) {
/* 167 */             RMProperty property = properties.get(requestedProperty);
/*     */             
/* 169 */             if (property != null) {
/* 170 */               String symbolicName = property.getSymbolicName();
/* 171 */               JSONArray propertyValues = new JSONArray();
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */               Object value = PropertyUtil.getDisplayableValue(property, this.isDocId);
/* 181 */               if ((symbolicName != null) && ((symbolicName.equals("Creator")) || (symbolicName.equals("LastModifier")) || (symbolicName.equals("Reviewer"))) && 
/* 182 */                 (value != null) && (!value.toString().isEmpty()) && (this.baseService != null)) {
/* 183 */                 String user = UserUtil.retrieveUserDisplayName(value.toString(), this.baseService, this.baseService.getNexusRepositoryId());
/* 184 */                 if (user != null) {
/* 185 */                   value = user;
/*     */                 }
/*     */               }
/*     */               
/* 189 */               propertyValues.add(MediatorUtil.objectValueToJSONObject(value));
/*     */               
/* 191 */               propertyValues.add(MediatorUtil.getJSONDataType(property.getDataType()));
/*     */               
/*     */ 
/* 194 */               if (symbolicName.equals("PathName")) {
/* 195 */                 propertyValues.add("path");
/*     */               }
/*     */               else {
/* 198 */                 propertyValues.add(null);
/*     */               }
/*     */               
/* 201 */               if (property.getDataType() == DataType.Object) {
/* 202 */                 Object baseObject = property.getObjectValue();
/* 203 */                 if ((baseObject != null) && ((baseObject instanceof BaseEntity))) {
/* 204 */                   BaseEntity baseEntityObject = (BaseEntity)property.getObjectValue();
/* 205 */                   propertyValues.add(baseEntityObject.getName());
/* 206 */                 } else if ((baseObject != null) && ((baseObject instanceof RMClassDescription))) {
/* 207 */                   RMClassDescription cd = (RMClassDescription)property.getObjectValue();
/* 208 */                   propertyValues.add(cd.getName());
/*     */                 } else {
/* 210 */                   propertyValues.add(null);
/*     */                 }
/*     */               }
/*     */               else {
/* 214 */                 propertyValues.add(null);
/*     */               }
/*     */               
/* 217 */               if (!property.isSettable()) {
/* 218 */                 propertyValues.add(Boolean.valueOf(true));
/*     */               } else {
/* 220 */                 propertyValues.add(null);
/*     */               }
/*     */               
/* 223 */               if (property.getDataType() == DataType.Object) {
/* 224 */                 Object baseObject = property.getObjectValue();
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
/* 235 */                 if ((baseObject != null) && ((baseObject instanceof BaseEntity))) {
/* 236 */                   BaseEntity baseEntityObject = (BaseEntity)baseObject;
/* 237 */                   if (baseEntityObject != null) {
/* 238 */                     propertyValues.add(MediatorUtil.createEntityItemJSONObject(baseEntityObject, MinimumPropertiesUtil.getPropertySetList(baseEntityObject), this.servletRequest));
/*     */                   }
/*     */                 }
/*     */               }
/*     */               
/* 243 */               if ((this.nameProperty != null) && (symbolicName.equals(this.nameProperty))) {
/* 244 */                 attributesJSON.put(nameDisplayName, propertyValues);
/* 245 */                 Logger.logDebug(this, methodName, request, "found name prop, row.put of " + nameDisplayName + ',' + value.toString());
/*     */                 
/*     */ 
/* 248 */                 attributesJSON.put("{NAME}", propertyValues);
/*     */               }
/*     */               
/* 251 */               if ((property.getSymbolicName().equals("RecordCategoryIdentifier")) || (property.getSymbolicName().equals("RecordFolderIdentifier"))) {
/* 252 */                 attributesJSON.put("ContainerId", propertyValues);
/*     */               } else {
/* 254 */                 attributesJSON.put(symbolicName, propertyValues);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/* 260 */         Logger.logError(this, methodName, request, e);
/*     */       }
/*     */     }
/* 263 */     return attributesJSON;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\BaseResultMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */