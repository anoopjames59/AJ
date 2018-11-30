/*     */ package com.ibm.jarm.ral.common;
/*     */ 
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RepositoryType;
/*     */ import com.ibm.jarm.api.constants.RetainMetadata;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RALBaseRepository
/*     */   extends RALBaseEntity
/*     */   implements Repository
/*     */ {
/*  28 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RMDomain domain;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RALBaseRepository(RMDomain domain, String identity, boolean isPlaceholder)
/*     */   {
/*  43 */     super(EntityType.Repository, (Repository)null, identity, isPlaceholder);
/*  44 */     Tracer.traceMethodEntry(new Object[] { domain, identity, Boolean.valueOf(isPlaceholder) });
/*  45 */     this.repository = this;
/*  46 */     this.domain = domain;
/*  47 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
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
/*     */   protected void validateAndPrepareFilePlan(String classIdent, RMProperties props, List<RMPermission> perms)
/*     */   {
/*  65 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms });
/*     */     
/*     */ 
/*  68 */     if ((props == null) || (!props.isPropertyPresent("ClassificationSchemeName")))
/*  69 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "ClassificationSchemeName" });
/*  70 */     String filePlanName = props.getStringValue("ClassificationSchemeName");
/*  71 */     if ((filePlanName == null) || (filePlanName.trim().length() == 0)) {
/*  72 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "ClassificationSchemeName" });
/*     */     }
/*     */     
/*  75 */     if (!filePlanNameIsUnique(filePlanName)) {
/*  76 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_FILEPLAN_NAME_NOT_UNIQUE, new Object[] { filePlanName });
/*     */     }
/*     */     
/*     */ 
/*  80 */     List<Integer> initAllowedRMTypes = getFilePlanInitialAllowedRMTypes(classIdent);
/*  81 */     props.putIntegerListValue("AllowedRMTypes", initAllowedRMTypes);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  86 */     if (!props.isPropertyPresent("RetainMetadata")) {
/*  87 */       props.putIntegerValue("RetainMetadata", Integer.valueOf(RetainMetadata.NeverRetain.getIntValue()));
/*     */     }
/*  89 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getDisplayName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getSymbolicName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMDomain getDomain()
/*     */   {
/* 107 */     return this.domain;
/*     */   }
/*     */   
/*     */   public abstract RepositoryType getRepositoryType();
/*     */   
/*     */   protected abstract boolean filePlanNameIsUnique(String paramString);
/*     */   
/*     */   protected abstract boolean customObjNameIsUnique(String paramString1, String paramString2, String paramString3, String paramString4);
/*     */   
/*     */   protected abstract List<Integer> getFilePlanInitialAllowedRMTypes(String paramString);
/*     */   
/*     */   public abstract boolean supportsRecordReceipts();
/*     */   
/*     */   protected abstract RMClassDescription getClassDescription(String paramString, boolean paramBoolean);
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseRepository.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */