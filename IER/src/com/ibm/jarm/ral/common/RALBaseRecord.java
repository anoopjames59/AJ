/*     */ package com.ibm.jarm.ral.common;
/*     */ 
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*     */ import com.ibm.jarm.api.constants.RetainMetadata;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.FilePlan;
/*     */ import com.ibm.jarm.api.core.Persistable;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.core.RecordType;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
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
/*     */ public abstract class RALBaseRecord
/*     */   extends RALBaseEntity
/*     */   implements Persistable
/*     */ {
/*  39 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int DDReportStatusRange_Start = 1000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int DDReportStatusRange_End = 4000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RALBaseRecord(EntityType entityType, Repository repository, String identity, boolean isPlaceholder)
/*     */   {
/*  62 */     super(entityType, repository, identity, isPlaceholder);
/*  63 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, Boolean.valueOf(isPlaceholder) });
/*  64 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPhysicalRecord()
/*     */   {
/*  72 */     Tracer.traceMethodEntry(new Object[0]);
/*  73 */     boolean result = EntityType.PhysicalRecord.equals(getEntityType());
/*  74 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  75 */     return result;
/*     */   }
/*     */   
/*     */   protected void associateRecordType(RecordType newRecordType)
/*     */   {
/*  80 */     Tracer.traceMethodEntry(new Object[] { newRecordType });
/*  81 */     boolean changeDetected = true;
/*  82 */     RecordType previousRT = getAssociatedRecordType();
/*     */     
/*  84 */     if (newRecordType != null)
/*     */     {
/*  86 */       if ((previousRT != null) && (newRecordType.getObjectIdentity().equalsIgnoreCase(previousRT.getObjectIdentity())))
/*     */       {
/*     */ 
/*     */ 
/*  90 */         changeDetected = false;
/*     */       }
/*     */     }
/*  93 */     else if (previousRT == null)
/*     */     {
/*     */ 
/*  96 */       changeDetected = false;
/*     */     }
/*     */     
/*  99 */     if (changeDetected)
/*     */     {
/* 101 */       RMProperties props = getProperties();
/* 102 */       props.putObjectValue("AssociatedRecordType", newRecordType);
/* 103 */       super.resetDispositionData(props);
/* 104 */       save(RMRefreshMode.Refresh);
/*     */     }
/*     */     
/* 107 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract RecordType getAssociatedRecordType();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isOnHold(boolean checkContainerHierarchy)
/*     */   {
/* 182 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(checkContainerHierarchy) });
/* 183 */     boolean result = false;
/*     */     
/* 185 */     RMProperties jarmProps = getProperties();
/* 186 */     if (jarmProps != null)
/*     */     {
/* 188 */       Boolean boolObj = jarmProps.getBooleanValue("OnHold");
/* 189 */       if (boolObj != null) {
/* 190 */         result = boolObj.booleanValue();
/*     */       }
/*     */     }
/* 193 */     if ((!result) && (checkContainerHierarchy))
/*     */     {
/* 195 */       result = isAnyParentOnHold();
/*     */     }
/*     */     
/* 198 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 199 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isAnyParentOnHold()
/*     */   {
/* 210 */     Tracer.traceMethodEntry(new Object[0]);
/* 211 */     boolean result = false;
/*     */     
/* 213 */     boolean ckContainerHier = true;
/* 214 */     List<Container> immediateParents = getContainedBy();
/* 215 */     for (Container container : immediateParents)
/*     */     {
/* 217 */       if (((RecordContainer)container).isOnHold(ckContainerHier))
/*     */       {
/* 219 */         result = true;
/* 220 */         break;
/*     */       }
/*     */     }
/*     */     
/* 224 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 225 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean isSuperseded()
/*     */   {
/* 230 */     Tracer.traceMethodEntry(new Object[0]);
/* 231 */     boolean result = false;
/*     */     
/* 233 */     if (!getProperties().isPropertyPresent("SupercedingRecords"))
/*     */     {
/* 235 */       RMPropertyFilter jarmPF = new RMPropertyFilter();
/* 236 */       jarmPF.addIncludeProperty(Integer.valueOf(1), null, null, "SupercedingRecords", null);
/* 237 */       refresh(jarmPF);
/*     */     }
/*     */     
/* 240 */     if (getProperties().isPropertyPresent("SupercedingRecords"))
/*     */     {
/* 242 */       List<Object> supersedingRecords = getProperties().getObjectListValue("SupercedingRecords");
/* 243 */       if (supersedingRecords != null) {
/* 244 */         result = !supersedingRecords.isEmpty();
/*     */       }
/*     */     }
/* 247 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 248 */     return result;
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
/*     */   protected void supersede(Record supersedeTargetRecord)
/*     */   {
/* 262 */     Tracer.traceMethodEntry(new Object[] { supersedeTargetRecord });
/* 263 */     Util.ckNullObjParam("supersedeTargetRecord", supersedeTargetRecord);
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
/*     */ 
/* 279 */     if (!supersedeTargetRecord.isSuperseded())
/*     */     {
/* 281 */       getProperties().putObjectValue("SupercededBy", supersedeTargetRecord);
/* 282 */       save(RMRefreshMode.Refresh);
/* 283 */       supersedeTargetRecord.getProperties().putDateTimeValue("SupercededDate", new Date());
/* 284 */       supersedeTargetRecord.save(RMRefreshMode.Refresh);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 289 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ERROR_SUPERSEDE_TARGET_ALREADY_SUPERSEDED, new Object[0]);
/*     */     }
/* 291 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isRetainMetadataEnabled()
/*     */   {
/* 302 */     Tracer.traceMethodEntry(new Object[0]);
/* 303 */     RetainMetadata setting = getFilePlan().getRetainMetadata();
/* 304 */     boolean result = setting != RetainMetadata.NeverRetain;
/*     */     
/* 306 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 307 */     return result;
/*     */   }
/*     */   
/*     */   protected void validateRecordDeletion()
/*     */   {
/* 312 */     Tracer.traceMethodEntry(new Object[0]);
/* 313 */     boolean ckContainerHier = true;
/* 314 */     if (isOnHold(ckContainerHier)) {
/* 315 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_DELETE_RECORD_DUE_TO_HOLD_CONDITION, new Object[] { getObjectIdentity() });
/*     */     }
/* 317 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   protected void validateRecordUndeclare()
/*     */   {
/* 322 */     Tracer.traceMethodEntry(new Object[0]);
/* 323 */     String recordIdent = getObjectIdentity();
/* 324 */     RMProperties jarmProps = getProperties();
/*     */     
/*     */ 
/* 327 */     if (Boolean.TRUE.equals(jarmProps.getBooleanValue("IsDeleted")))
/*     */     {
/* 329 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_UNDECLARE_LOGICALLY_DELETED_RECORD, new Object[] { recordIdent });
/*     */     }
/*     */     
/*     */ 
/* 333 */     Integer currentPhaseExeStatus = jarmProps.getIntegerValue("CurrentPhaseExecutionStatus");
/* 334 */     if ((currentPhaseExeStatus != null) && (currentPhaseExeStatus.intValue() > RMWorkflowStatus.NotStarted.getIntValue()))
/*     */     {
/*     */ 
/* 337 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_UNDECLARE_RECORD_IN_ACTIVE_WORKFLOW, new Object[] { recordIdent });
/*     */     }
/*     */     
/*     */ 
/* 341 */     boolean ckContainerHier = true;
/* 342 */     if (isOnHold(ckContainerHier))
/*     */     {
/* 344 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_UNDECLARE_RECORD_WITH_HOLD, new Object[] { recordIdent });
/*     */     }
/*     */     
/*     */ 
/* 348 */     if (hasFederatedContent())
/*     */     {
/* 350 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_UNDECLARE_RECORD_WITH_FEDERATED_CONTENT, new Object[] { recordIdent });
/*     */     }
/* 352 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean hasFederatedContent();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract FilePlan getFilePlan();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<String> getEligibleRecordCopyPropertyNames()
/*     */   {
/* 380 */     Tracer.traceMethodEntry(new Object[0]);
/* 381 */     List<String> results = new ArrayList();
/*     */     
/* 383 */     RMClassDescription classDesc = getClassDescription();
/* 384 */     List<RMPropertyDescription> propDescs = classDesc.getPropertyDescriptions();
/* 385 */     for (RMPropertyDescription propDesc : propDescs)
/*     */     {
/* 387 */       String propSymName = propDesc.getSymbolicName();
/*     */       
/* 389 */       if ((!propDesc.isReadOnly()) && (!propDesc.isSystemGenerated()) && (!propDesc.isSystemOwned()) && (!propDesc.isRMSystemProperty()) && (!RALBaseEntity.isDispositionRelatedProperty(propSymName)) && (!RALBaseEntity.isVitalRelatedProperty(propSymName)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 396 */         results.add(propSymName);
/*     */       }
/*     */     }
/*     */     
/* 400 */     Tracer.traceMethodExit(new Object[] { results });
/* 401 */     return results;
/*     */   }
/*     */   
/*     */   protected boolean supportsReceipts()
/*     */   {
/* 406 */     Tracer.traceMethodEntry(new Object[0]);
/* 407 */     boolean result = ((RALBaseRepository)getRepository()).supportsRecordReceipts();
/* 408 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 409 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseRecord.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */