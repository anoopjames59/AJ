/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import com.ibm.jarm.api.query.ResultRow;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResultRowResultMediator
/*     */   extends BaseResultMediator
/*     */ {
/*     */   private static final long serialVersionUID = 5L;
/*     */   private ResultRow resultRow;
/*  24 */   private String className = null;
/*     */   protected static final String NOPERMISSIONLOADED = "noPermissionsLoaded";
/*     */   
/*     */   public ResultRowResultMediator(IERBaseService service, HttpServletRequest request, List<String> requestedProperties, String className) {
/*  28 */     super(service, request);
/*     */     
/*  30 */     this.fp_repository = service.getFilePlanRepository();
/*  31 */     this.requestedProperties = requestedProperties;
/*  32 */     this.className = className;
/*     */   }
/*     */   
/*     */   public ResultRowResultMediator(FilePlanRepository repository, HttpServletRequest request, List<String> requestedProperties, String className) {
/*  36 */     super(repository, request, requestedProperties);
/*  37 */     this.className = className;
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(List<String> requestedProperties) {
/*  41 */     this.requestedProperties = requestedProperties;
/*     */   }
/*     */   
/*     */   public void setResultRow(ResultRow row) {
/*  45 */     this.resultRow = row;
/*  46 */     this.nameProperty = null;
/*     */   }
/*     */   
/*     */   public void setNameProperty(String nameProperty) {
/*  50 */     this.nameProperty = nameProperty;
/*     */   }
/*     */   
/*     */   public String getItemId() throws Exception {
/*  54 */     RMProperties properties = this.resultRow.getProperties();
/*  55 */     String className = getClassName();
/*  56 */     if ((className == null) || (className.length() == 0)) {
/*  57 */       className = getClassNameFromProperty(properties);
/*     */     }
/*  59 */     return className + "," + this.fp_repository.getClientIdentifier() + "," + properties.getStringValue("Id");
/*     */   }
/*     */   
/*     */   public String getItemName() {
/*  63 */     RMProperties properties = this.resultRow.getProperties();
/*  64 */     return properties.getStringValue(this.nameProperty);
/*     */   }
/*     */   
/*     */   public String getContentType() {
/*  68 */     return null;
/*     */   }
/*     */   
/*     */   public String getEntityType()
/*     */   {
/*  73 */     RMProperties properties = this.resultRow.getProperties();
/*  74 */     return String.valueOf(properties.getStringValue("RMEntityType"));
/*     */   }
/*     */   
/*     */   public String getClassName()
/*     */   {
/*  79 */     return this.className;
/*     */   }
/*     */   
/*     */   protected RMClassDescription getClassDescription(RMProperties properties)
/*     */   {
/*  84 */     RMClassDescription cd = null;
/*  85 */     if ((properties != null) && (properties.isPropertyPresent("ClassDescription"))) {
/*  86 */       RMProperty property = properties.get("ClassDescription");
/*     */       
/*  88 */       if ((property != null) && (property.getDataType() == DataType.Object)) {
/*  89 */         Object baseObject = property.getObjectValue();
/*  90 */         if ((baseObject != null) && ((baseObject instanceof RMClassDescription))) {
/*  91 */           cd = (RMClassDescription)property.getObjectValue();
/*     */         }
/*     */       }
/*     */     }
/*  95 */     return cd;
/*     */   }
/*     */   
/*     */   public JSONObject toJSONObject() throws Exception {
/*  99 */     String methodName = "toJSONObject";
/* 100 */     HttpServletRequest request = getHttpServletRequest();
/* 101 */     Logger.logEntry(this, methodName, request);
/* 102 */     JSONObject jsonObject = null;
/*     */     
/* 104 */     if (this.resultRow != null) {
/* 105 */       RMProperties properties = this.resultRow.getProperties();
/* 106 */       if ((this.requestedProperties != null) && (this.requestedProperties.contains("{CLASS}"))) {
/* 107 */         RMClassDescription cd = getClassDescription(properties);
/* 108 */         if (cd != null)
/* 109 */           this.className = cd.getDisplayName();
/*     */       }
/* 111 */       jsonObject = super.toJSONObject(properties);
/* 112 */       jsonObject.put("noPermissionsLoaded", Boolean.valueOf(true));
/*     */     }
/*     */     
/* 115 */     Logger.logExit(this, methodName, request);
/* 116 */     return jsonObject;
/*     */   }
/*     */   
/*     */   private String getClassNameFromProperty(RMProperties properties)
/*     */   {
/* 121 */     int classNameInt = 0;
/* 122 */     for (RMProperty prop : properties.toArray()) {
/* 123 */       if ("RMEntityType".equalsIgnoreCase(prop.getSymbolicName())) {
/* 124 */         classNameInt = prop.getIntegerValue().intValue();
/* 125 */         break;
/*     */       }
/*     */     }
/* 128 */     if (classNameInt != 0) {
/* 129 */       for (EntityType e : EntityType.values()) {
/* 130 */         if (classNameInt == e.getIntValue()) {
/* 131 */           return e.name();
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\ResultRowResultMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */