/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.PrivilegesUtil;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.query.CBRResult;
/*     */ import com.ibm.jarm.api.query.ResultRow;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ public class CBRRowResultMediator extends ResultRowResultMediator
/*     */ {
/*     */   private static final long serialVersionUID = 1005L;
/*     */   public static final String CBRRANK_PSUDO_SYMNAME = "rank";
/*     */   private CBRResult cbrresult;
/*     */   private BaseEntity entity;
/*     */   
/*     */   public CBRRowResultMediator(IERBaseService service, HttpServletRequest request, List<String> requestedProperties, String className)
/*     */   {
/*  27 */     super(service, request, requestedProperties, className);
/*     */   }
/*     */   
/*     */   public CBRRowResultMediator(FilePlanRepository repository, HttpServletRequest request, List<String> requestedProperties, String className) {
/*  31 */     super(repository, request, requestedProperties, className);
/*     */   }
/*     */   
/*     */   public void setCBRResultRow(CBRResult cbrresult) {
/*  35 */     this.cbrresult = cbrresult;
/*  36 */     if (cbrresult != null) {
/*  37 */       setResultRow(cbrresult.getResultRow());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBaseEntity(BaseEntity be) {
/*  42 */     this.entity = be;
/*     */   }
/*     */   
/*     */   public String getItemId() throws Exception {
/*  46 */     if ((this.cbrresult == null) || (this.cbrresult.getResultRow() == null)) { return super.getItemId();
/*     */     }
/*  48 */     RMProperties properties = this.cbrresult.getResultRow().getProperties();
/*  49 */     RMClassDescription cd = getClassDescription(properties);
/*  50 */     if (cd == null) { return super.getItemId();
/*     */     }
/*  52 */     return cd.getSymbolicName() + "," + this.fp_repository.getClientIdentifier() + "," + properties.getStringValue("Id");
/*     */   }
/*     */   
/*     */   public JSONObject buildAttributes(RMProperties properties)
/*     */   {
/*  57 */     JSONObject attriJSON = super.buildAttributes(properties);
/*     */     
/*     */ 
/*  60 */     JSONArray propertyValues = new JSONArray();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */     Object value = new Double(this.cbrresult.getScore());
/*     */     
/*  71 */     propertyValues.add(MediatorUtil.objectValueToJSONObject(value));
/*     */     
/*  73 */     propertyValues.add(MediatorUtil.getJSONDataType(DataType.String));
/*     */     
/*  75 */     propertyValues.add(null);
/*     */     
/*  77 */     propertyValues.add(null);
/*     */     
/*  79 */     propertyValues.add(Boolean.valueOf(true));
/*     */     
/*  81 */     attriJSON.put("rank", propertyValues);
/*  82 */     return attriJSON;
/*     */   }
/*     */   
/*     */   public JSONObject toJSONObject() throws Exception {
/*  86 */     String methodName = "toJSONObject";
/*  87 */     HttpServletRequest request = getHttpServletRequest();
/*  88 */     Logger.logEntry(this, methodName, request);
/*     */     
/*  90 */     JSONObject jsonObject = super.toJSONObject();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     if (this.entity != null) {
/*     */       try {
/*  98 */         RMProperties properties = this.cbrresult.getResultRow().getProperties();
/*  99 */         PrivilegesUtil.setPrivilegesJSON(this.entity, jsonObject, request);
/* 100 */         setEntityJSON(this.entity, jsonObject, properties);
/*     */       }
/*     */       catch (Exception ignored) {}
/*     */     }
/*     */     
/* 105 */     Logger.logExit(this, methodName, request);
/* 106 */     return jsonObject;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\CBRRowResultMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */