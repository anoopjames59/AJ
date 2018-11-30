/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.Hold;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ class P8CE_HoldImpl
/*     */   extends P8CE_RMCustomObjectImpl implements Hold
/*     */ {
/*  32 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  33 */   private static final IGenerator<Hold> RMHoldGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "HoldName", "SweepState", "HoldReason", "HoldType", "Active", "ConditionXML" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   static
/*     */   {
/*  46 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  48 */     List<FilterElement> tempList = new ArrayList(1);
/*  49 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  50 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  55 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  60 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<Hold> getGenerator()
/*     */   {
/*  70 */     return RMHoldGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  78 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */   P8CE_HoldImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/*  84 */     super(EntityType.Hold, repository, identity, jaceCustomObject, isPlaceholder);
/*  85 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  86 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/*  95 */     Tracer.traceMethodEntry(new Object[0]);
/*  96 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/*  99 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 101 */       PageableSet<Record> recOnHold = getAssociatedRecords(Integer.valueOf(1));
/* 102 */       PageableSet<Container> containerOnHold = getAssociatedContainers(Integer.valueOf(1));
/*     */       
/* 104 */       if ((!recOnHold.isEmpty()) || (!containerOnHold.isEmpty()))
/*     */       {
/* 106 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_DELETE_HOLD_ENTITIES_ARE_ON_HOLD, new Object[0]);
/*     */       }
/* 108 */       super.delete();
/* 109 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 113 */       if (establishedSubject) {
/* 114 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getHoldName()
/*     */   {
/* 123 */     Tracer.traceMethodEntry(new Object[0]);
/* 124 */     String result = P8CE_Util.getJacePropertyAsString(this, "HoldName");
/* 125 */     Tracer.traceMethodExit(new Object[] { result });
/* 126 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHoldName(String holdName)
/*     */   {
/* 134 */     Tracer.traceMethodEntry(new Object[] { holdName });
/* 135 */     Util.ckInvalidStrParam("holdName", holdName);
/* 136 */     getProperties().putStringValue("HoldName", holdName);
/* 137 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConditionXML(String conditionXML)
/*     */   {
/* 145 */     Tracer.traceMethodEntry(new Object[] { conditionXML });
/* 146 */     getProperties().putStringValue("ConditionXML", conditionXML);
/* 147 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getConditionXML()
/*     */   {
/* 155 */     Tracer.traceMethodEntry(new Object[0]);
/* 156 */     String result = P8CE_Util.getJacePropertyAsString(this, "ConditionXML");
/* 157 */     Tracer.traceMethodExit(new Object[] { result });
/* 158 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSweepState(Integer sweepState)
/*     */   {
/* 166 */     Tracer.traceMethodEntry(new Object[] { sweepState });
/* 167 */     getProperties().putIntegerValue("SweepState", sweepState);
/* 168 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getSweepState()
/*     */   {
/* 176 */     Tracer.traceMethodEntry(new Object[0]);
/* 177 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "SweepState");
/* 178 */     Tracer.traceMethodExit(new Object[] { result });
/* 179 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean isActive()
/*     */   {
/* 187 */     Tracer.traceMethodEntry(new Object[0]);
/* 188 */     Boolean result = P8CE_Util.getJacePropertyAsBoolean(this, "Active");
/* 189 */     Tracer.traceMethodExit(new Object[] { result });
/* 190 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setActiveState(boolean isActive)
/*     */   {
/* 198 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(isActive) });
/* 199 */     getProperties().putBooleanValue("Active", Boolean.valueOf(isActive));
/* 200 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHoldType()
/*     */   {
/* 208 */     Tracer.traceMethodEntry(new Object[0]);
/* 209 */     String result = P8CE_Util.getJacePropertyAsString(this, "HoldType");
/* 210 */     Tracer.traceMethodExit(new Object[] { result });
/* 211 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHoldType(String holdType)
/*     */   {
/* 219 */     Tracer.traceMethodEntry(new Object[] { holdType });
/* 220 */     getProperties().putStringValue("HoldType", holdType);
/* 221 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 230 */     Tracer.traceMethodEntry(new Object[0]);
/* 231 */     String result = getHoldName();
/* 232 */     Tracer.traceMethodExit(new Object[] { result });
/* 233 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHoldReason()
/*     */   {
/* 241 */     Tracer.traceMethodEntry(new Object[0]);
/* 242 */     String result = P8CE_Util.getJacePropertyAsString(this, "HoldReason");
/* 243 */     Tracer.traceMethodExit(new Object[] { result });
/* 244 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHoldReason(String reason)
/*     */   {
/* 252 */     Tracer.traceMethodEntry(new Object[] { reason });
/* 253 */     getProperties().putStringValue("HoldReason", reason);
/* 254 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 263 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 264 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 267 */       establishedSubject = P8CE_Util.associateSubject();
/* 268 */       validateHold();
/*     */       
/* 270 */       super.save(jarmRefreshMode);
/* 271 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 275 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 279 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.Hold });
/*     */     }
/*     */     finally
/*     */     {
/* 283 */       if (establishedSubject) {
/* 284 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<Record> getAssociatedRecords(Integer pageSize)
/*     */   {
/* 294 */     Tracer.traceMethodEntry(new Object[] { pageSize });
/* 295 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 298 */       PageableSet<Record> associatedRecords = null;
/* 299 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 302 */       List<FilterElement> mandatoryRecordFEs = P8CE_RecordImpl.getMandatoryJaceFEs();
/* 303 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryRecordFEs);
/*     */       
/*     */ 
/* 306 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordInfo", "r");
/* 307 */       sb.append(" INNER JOIN ").append("RecordHoldLink").append(" rhl ON r.This = rhl.").append("Head");
/* 308 */       sb.append(" WHERE rhl.").append("Tail").append(" = OBJECT('").append(getObjectIdentity()).append("')");
/* 309 */       sb.append(" ORDER BY r.").append("DocumentTitle");
/* 310 */       String sqlStatement = sb.toString();
/*     */       
/* 312 */       P8CE_RepositoryImpl repository = (P8CE_RepositoryImpl)this.repository.getRepository();
/* 313 */       ObjectStore jaceObjStore = repository.getJaceObjectStore();
/* 314 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 315 */       SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/*     */       
/*     */ 
/* 318 */       long startTime = System.currentTimeMillis();
/* 319 */       IndependentObjectSet jaceDocumentSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, Boolean.TRUE);
/* 320 */       long stopTime = System.currentTimeMillis();
/*     */       
/* 322 */       Boolean elementCountIndicator = null;
/* 323 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 325 */         elementCountIndicator = jaceDocumentSet != null ? Boolean.valueOf(jaceDocumentSet.isEmpty()) : null;
/*     */       }
/* 327 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceDocumentSet, new Object[] { sqlStatement });
/*     */       
/*     */ 
/*     */ 
/* 331 */       boolean supportsPaging = true;
/* 332 */       IGenerator<Record> generator = P8CE_RecordImpl.getGenerator();
/* 333 */       associatedRecords = new P8CE_PageableSetImpl(repository, jaceDocumentSet, supportsPaging, generator);
/*     */       
/* 335 */       Tracer.traceMethodExit(new Object[] { associatedRecords });
/* 336 */       return associatedRecords;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 340 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 344 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 348 */       if (establishedSubject) {
/* 349 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PageableSet<Container> getAssociatedContainers(Integer pageSize)
/*     */   {
/* 358 */     Tracer.traceMethodEntry(new Object[] { pageSize });
/* 359 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 362 */       PageableSet<Container> associatedContainers = null;
/* 363 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 366 */       List<FilterElement> mandatoryContainerFEs = P8CE_BaseContainerImpl.getMandatoryJaceFEs();
/* 367 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryContainerFEs);
/*     */       
/* 369 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RMFolder", "rf");
/* 370 */       sb.append(" INNER JOIN ").append("RMFolderHoldLink").append(" rhl ON rf.This = rhl.").append("Head");
/* 371 */       sb.append(" WHERE rhl.").append("Tail").append(" = OBJECT('").append(getObjectIdentity()).append("')");
/* 372 */       sb.append(" ORDER BY rf.").append("FolderName");
/* 373 */       String sqlStatement = sb.toString();
/*     */       
/* 375 */       P8CE_RepositoryImpl repository = (P8CE_RepositoryImpl)this.repository.getRepository();
/* 376 */       ObjectStore jaceObjStore = repository.getJaceObjectStore();
/* 377 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 378 */       SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/*     */       
/*     */ 
/* 381 */       long startTime = System.currentTimeMillis();
/* 382 */       IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, Boolean.TRUE);
/* 383 */       long stopTime = System.currentTimeMillis();
/* 384 */       Boolean elementCountIndicator = null;
/* 385 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 387 */         elementCountIndicator = jaceFolderSet != null ? Boolean.valueOf(jaceFolderSet.isEmpty()) : null;
/*     */       }
/* 389 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement, pageSize, jacePF, Boolean.TRUE });
/*     */       
/*     */ 
/*     */ 
/* 393 */       boolean supportsPaging = true;
/* 394 */       IGenerator<Container> generator = P8CE_BaseContainerImpl.getBaseContainerGenerator();
/* 395 */       associatedContainers = new P8CE_PageableSetImpl(repository, jaceFolderSet, supportsPaging, generator);
/*     */       
/* 397 */       Tracer.traceMethodExit(new Object[] { associatedContainers });
/* 398 */       return associatedContainers;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 402 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 406 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 410 */       if (establishedSubject) {
/* 411 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 423 */     if (this.isPlaceholder) {
/* 424 */       return "P8CE_HoldImpl ObjectIdent: " + getObjectIdentity();
/*     */     }
/* 426 */     return "P8CE_HoldImpl HoldName: " + getHoldName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<Hold>
/*     */   {
/*     */     public Hold create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 439 */       P8CE_HoldImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 440 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 442 */       String identity = "<undefined>";
/* 443 */       if (jaceCustomObj.getProperties().isPropertyPresent("HoldName")) {
/* 444 */         identity = jaceCustomObj.getProperties().getStringValue("HoldName");
/*     */       }
/* 446 */       Hold result = new P8CE_HoldImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 448 */       P8CE_HoldImpl.Tracer.traceMethodExit(new Object[] { result });
/* 449 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_HoldImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */