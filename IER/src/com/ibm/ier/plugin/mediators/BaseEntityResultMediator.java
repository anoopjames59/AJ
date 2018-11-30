/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyEngineObject;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.PrivilegesUtil;
/*     */ import com.ibm.ier.plugin.util.PropertyUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.ContentItem;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_BaseEntityImpl;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.HashMap;
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
/*     */ public class BaseEntityResultMediator
/*     */   extends BaseResultMediator
/*     */ {
/*     */   private static final long serialVersionUID = 4L;
/*     */   private BaseEntity item;
/*  41 */   private final Map<String, String> namePropertyMap = new HashMap();
/*     */   
/*     */   public BaseEntityResultMediator(IERBaseService service, HttpServletRequest request, List<String> requestedProperties) {
/*  44 */     super(service, request);
/*     */     
/*  46 */     this.fp_repository = service.getFilePlanRepository();
/*  47 */     this.requestedProperties = requestedProperties;
/*     */   }
/*     */   
/*     */   public BaseEntityResultMediator(FilePlanRepository repository, HttpServletRequest request, List<String> requestedProperties) {
/*  51 */     super(repository, request, requestedProperties);
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(List<String> requestedProperties) {
/*  55 */     this.requestedProperties = requestedProperties;
/*     */   }
/*     */   
/*     */   public void setEntityItem(BaseEntity item) {
/*  59 */     this.item = item;
/*  60 */     this.nameProperty = null;
/*     */   }
/*     */   
/*     */   public void setNameProperty(String nameProperty) {
/*  64 */     this.nameProperty = nameProperty;
/*     */   }
/*     */   
/*     */   public String getContentType() {
/*  68 */     if ((this.item instanceof Container))
/*  69 */       return "folder";
/*  70 */     if (((this.item instanceof ContentItem)) || ((this.item instanceof Record))) {
/*  71 */       return "document";
/*     */     }
/*  73 */     return "customObject";
/*     */   }
/*     */   
/*     */   public String getEntityType() {
/*  77 */     return String.valueOf(this.item.getEntityType().getIntValue());
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  81 */     return this.item.getClassName();
/*     */   }
/*     */   
/*     */   public String getItemId() throws Exception {
/*  85 */     String docId = null;
/*  86 */     if (this.item != null) {
/*  87 */       docId = IERUtil.getDocId(this.item);
/*  88 */       Logger.logDebug(this, "getItemId", getHttpServletRequest(), "docId: " + docId);
/*     */     }
/*     */     
/*  91 */     return docId;
/*     */   }
/*     */   
/*     */   public String getItemName() {
/*  95 */     String methodName = "toJSONObject";
/*  96 */     HttpServletRequest request = getHttpServletRequest();
/*  97 */     Logger.logEntry(this, methodName, getHttpServletRequest());
/*     */     
/*  99 */     if (this.nameProperty == null) {
/* 100 */       RMPropertyDescription namePropDesc = null;
/* 101 */       String className = this.item.getClassName();
/* 102 */       this.nameProperty = ((String)this.namePropertyMap.get(className));
/* 103 */       if (this.nameProperty == null) {
/* 104 */         RMClassDescription cd = this.item.getClassDescription();
/* 105 */         List<RMPropertyDescription> pds = SessionUtil.getPropertyDescriptionList(this.fp_repository, className, request);
/* 106 */         if (cd.getNamePropertyIndex() != null) {
/* 107 */           int namePropertyIndex = cd.getNamePropertyIndex().intValue();
/* 108 */           namePropDesc = (RMPropertyDescription)pds.get(namePropertyIndex);
/* 109 */           this.namePropertyMap.put(className, namePropDesc.getSymbolicName());
/* 110 */           this.nameProperty = namePropDesc.getSymbolicName();
/*     */         }
/*     */       }
/*     */     }
/* 114 */     Logger.logExit(this, methodName, getHttpServletRequest());
/*     */     
/* 116 */     RMProperties properties = this.item.getProperties();
/* 117 */     if (this.nameProperty != null) {
/* 118 */       return properties.isPropertyPresent(this.nameProperty) ? properties.getStringValue(this.nameProperty) : "";
/*     */     }
/* 120 */     return "";
/*     */   }
/*     */   
/*     */   public JSONObject toJSONObject() throws Exception {
/* 124 */     String methodName = "toJSONObject";
/* 125 */     HttpServletRequest request = getHttpServletRequest();
/* 126 */     Logger.logEntry(this, methodName, request);
/* 127 */     JSONObject jsonObject = null;
/*     */     
/* 129 */     if (this.item != null) {
/* 130 */       RMProperties properties = this.item.getProperties();
/* 131 */       jsonObject = super.toJSONObject(properties);
/* 132 */       PrivilegesUtil.setPrivilegesJSON(this.item, jsonObject, request);
/*     */       
/* 134 */       setEntityJSON(this.item, jsonObject, properties);
/*     */       
/*     */ 
/* 137 */       if (((this.item instanceof P8CE_BaseEntityImpl)) && (this.item.getClassName() == "StoredSearch")) {
/* 138 */         P8CE_BaseEntityImpl p8StoredSearch = (P8CE_BaseEntityImpl)this.item;
/* 139 */         Boolean isCurrentVersion = (Boolean)PropertyUtil.getPropertyValue(p8StoredSearch.getJaceBaseObject(), "IsCurrentVersion");
/*     */         
/*     */ 
/* 142 */         Boolean reserved = (Boolean)PropertyUtil.getPropertyValue(p8StoredSearch.getJaceBaseObject(), "IsReserved");
/* 143 */         Integer versionStatus = (Integer)PropertyUtil.getPropertyValue(p8StoredSearch.getJaceBaseObject(), "VersionStatus");
/* 144 */         boolean isCheckedOut = false;
/* 145 */         if ((reserved.booleanValue()) || (versionStatus.intValue() == 3)) {
/* 146 */           isCheckedOut = true;
/*     */         }
/*     */         
/* 149 */         if (isCheckedOut) {
/* 150 */           jsonObject.put("locked", Boolean.valueOf(isCheckedOut));
/*     */         }
/* 152 */         if (isCurrentVersion.booleanValue()) {
/* 153 */           jsonObject.put("currentVersion", isCurrentVersion);
/*     */         }
/*     */         
/* 156 */         String vsId = getVersionSeriesId(p8StoredSearch.getJaceBaseObject());
/* 157 */         if (vsId != null) {
/* 158 */           jsonObject.put("vsId", vsId);
/*     */         }
/* 160 */         Boolean autoRun = (Boolean)PropertyUtil.getPropertyValue(p8StoredSearch.getJaceBaseObject(), "IcnAutoRun");
/* 161 */         if ((autoRun != null) && (autoRun.booleanValue() == true)) {
/* 162 */           jsonObject.put("mimetype", "application/x-searchtemplate.automatic");
/*     */         } else {
/* 164 */           String mimetype = (String)PropertyUtil.getPropertyValue(p8StoredSearch.getJaceBaseObject(), "MimeType");
/* 165 */           if ((mimetype != null) && (!mimetype.isEmpty())) {
/* 166 */             jsonObject.put("mimetype", mimetype);
/*     */           } else {
/* 168 */             jsonObject.put("mimetype", "application/x-filenet-searchtemplate");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 174 */     Logger.logExit(this, methodName, request);
/* 175 */     return jsonObject;
/*     */   }
/*     */   
/*     */   private String getVersionSeriesId(Object hit) {
/* 179 */     String id = null;
/* 180 */     Properties properties = null;
/* 181 */     if ((hit instanceof IndependentObject)) {
/* 182 */       properties = ((IndependentObject)hit).getProperties();
/* 183 */     } else if ((hit instanceof RepositoryRow)) {
/* 184 */       properties = ((RepositoryRow)hit).getProperties();
/*     */     }
/*     */     
/* 187 */     if ((properties != null) && (properties.isPropertyPresent("VersionSeries"))) {
/* 188 */       ObjectReference ref = ((PropertyEngineObject)properties.get("VersionSeries")).getObjectReference();
/* 189 */       if (ref != null) {
/* 190 */         id = ref.getObjectIdentity();
/*     */       }
/* 192 */     } else if ((hit instanceof IndependentObject)) {
/* 193 */       IndependentObject indObj = (IndependentObject)hit;
/* 194 */       String[] propNames = { "VersionSeries" };
/* 195 */       indObj.fetchProperties(propNames);
/* 196 */       properties = indObj.getProperties();
/* 197 */       ObjectReference ref = ((PropertyEngineObject)properties.get("VersionSeries")).getObjectReference();
/* 198 */       if (ref != null) {
/* 199 */         id = ref.getObjectIdentity();
/*     */       }
/*     */     }
/* 202 */     return id;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\BaseEntityResultMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */