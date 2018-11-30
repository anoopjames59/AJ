/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.PageIterator;
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.constants.AutoUniqueName;
/*      */ import com.filenet.api.constants.DefineSecurityParentage;
/*      */ import com.filenet.api.constants.RefreshMode;
/*      */ import com.filenet.api.core.BatchItemHandle;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.Factory.CustomObject;
/*      */ import com.filenet.api.core.Factory.Document;
/*      */ import com.filenet.api.core.Factory.Folder;
/*      */ import com.filenet.api.core.Factory.Link;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.core.IndependentlyPersistableObject;
/*      */ import com.filenet.api.core.Link;
/*      */ import com.filenet.api.core.ObjectReference;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*      */ import com.filenet.api.core.RetrievingBatch;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.RepositoryRow;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.collection.RMPageIterator;
/*      */ import com.ibm.jarm.api.constants.AuditStatus;
/*      */ import com.ibm.jarm.api.constants.DeleteMode;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.DomainType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.BulkItemResult;
/*      */ import com.ibm.jarm.api.core.BulkOperation.EntityDescription;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.DefensiblyDisposable;
/*      */ import com.ibm.jarm.api.core.DispositionAction;
/*      */ import com.ibm.jarm.api.core.Dispositionable;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.Holdable;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.RMFactory.Container;
/*      */ import com.ibm.jarm.api.core.RMFactory.RMProperties;
/*      */ import com.ibm.jarm.api.core.RMFactory.Record;
/*      */ import com.ibm.jarm.api.core.Record;
/*      */ import com.ibm.jarm.api.core.RecordCategory;
/*      */ import com.ibm.jarm.api.core.RecordContainer;
/*      */ import com.ibm.jarm.api.core.RecordFolder;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.property.RMFilteredPropertyType;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.query.RMSearch;
/*      */ import com.ibm.jarm.api.security.RMUser;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.P8CE_Convert;
/*      */ import com.ibm.jarm.api.util.RMLString;
/*      */ import com.ibm.jarm.ral.common.RALBaseContainer;
/*      */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*      */ import com.ibm.jarm.ral.common.RALBaseRecord;
/*      */ import com.ibm.jarm.ral.common.RALBulkOperation;
/*      */ import com.ibm.jarm.ral.common.RALPESupport;
/*      */ import com.ibm.jarm.ral.common.RALWorkflowSupport;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class P8CE_RALBulkOperationImpl
/*      */   implements RALBulkOperation
/*      */ {
/*  100 */   private static final String BulkDefaultReasonForDelete = RMLString.get("bulk.deleteReason", "Part of a bulk deletion operation");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */   public static final Integer SkipIfAnyChildOnHold = Integer.valueOf(0);
/*      */   
/*      */ 
/*  109 */   public static final Integer ProcessNonHeldChildren = Integer.valueOf(1);
/*      */   
/*  111 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*      */   
/*  113 */   private static final P8CE_RALBulkOperationImpl Singleton = new P8CE_RALBulkOperationImpl();
/*      */   private static final String SqlForImmediateRecsReadyForInitDisp;
/*      */   private static final String SqlForSubContainersReadyForInitDisp;
/*      */   
/*      */   static {
/*  118 */     List<FilterElement> mandatoryRecordFEs = P8CE_RecordImpl.getMandatoryJaceFEs();
/*  119 */     PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryRecordFEs);
/*      */     
/*  121 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordInfo", "r");
/*  122 */     sb.append(" INNER JOIN ").append("ReferentialContainmentRelationship").append(" rcr ON r.This = rcr.").append("Head");
/*  123 */     sb.append(" WHERE rcr.").append("Tail").append(" = OBJECT('%s')");
/*  124 */     sb.append(" AND r.").append("CutoffDate").append(" IS NOT NULL");
/*  125 */     sb.append(" AND r.").append("IsDeleted").append(" = FALSE");
/*  126 */     sb.append(" AND r.").append("IsFinalPhaseCompleted").append(" = FALSE");
/*  127 */     sb.append(" AND r.").append("CurrentPhaseExecutionDate").append(" <= %s");
/*  128 */     sb.append(" AND r.").append("CurrentPhaseExecutionStatus").append(" = ").append(RMWorkflowStatus.NotStarted.getIntValue());
/*  129 */     sb.append(" AND r.").append("OnHold").append(" = FALSE ");
/*  130 */     SqlForImmediateRecsReadyForInitDisp = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  136 */     List<FilterElement> mandatoryRecordFEs = P8CE_BaseContainerImpl.getMandatoryJaceFEs();
/*  137 */     PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryRecordFEs);
/*      */     
/*  139 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RMFolder", "rmf");
/*  140 */     sb.append(" WHERE rmf.This INSUBFOLDER '%s'");
/*  141 */     sb.append(" AND rmf.").append("CutoffDate").append(" IS NOT NULL");
/*  142 */     sb.append(" AND rmf.").append("IsDeleted").append(" = FALSE");
/*  143 */     sb.append(" AND rmf.").append("IsFinalPhaseCompleted").append(" = FALSE");
/*  144 */     sb.append(" AND rmf.").append("CurrentPhaseExecutionDate").append(" <= %s");
/*  145 */     sb.append(" AND rmf.").append("CurrentPhaseExecutionStatus").append(" = ").append(RMWorkflowStatus.NotStarted.getIntValue());
/*  146 */     sb.append(" AND rmf.").append("OnHold").append(" = FALSE ");
/*  147 */     SqlForSubContainersReadyForInitDisp = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  153 */     List<FilterElement> mandatoryRecordFEs = P8CE_BaseContainerImpl.getMandatoryJaceFEs();
/*  154 */     PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryRecordFEs);
/*      */     
/*  156 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RMFolder", "rmf");
/*  157 */     sb.append(" WHERE rmf.This INSUBFOLDER '%s'");
/*  158 */     sb.append(" AND rmf.").append("IsDeleted").append(" = FALSE");
/*  159 */     sb.append(" AND rmf.").append("OnHold").append(" = FALSE ");
/*  160 */     SqlForSubContainersNotOnHold = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  166 */     StringBuilder sb = new StringBuilder();
/*  167 */     sb.append("SELECT rmf.").append("Id").append(" FROM ").append("RMFolder").append(" rmf");
/*  168 */     sb.append(" WHERE rmf.This INSUBFOLDER '%s'");
/*  169 */     sb.append(" AND rmf.").append("IsDeleted").append(" = FALSE");
/*  170 */     sb.append(" AND rmf.").append("OnHold").append(" = TRUE ");
/*  171 */     SqlForOnHoldSubContainerIDs = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  177 */     StringBuilder sb = new StringBuilder();
/*  178 */     sb.append("SELECT TOP 1 ").append("Id").append(" FROM ").append("RMFolder");
/*  179 */     sb.append(" WHERE (").append("RMEntityType").append(" = ").append(EntityType.PhysicalContainer.getIntValue());
/*  180 */     sb.append(" OR ").append("RMEntityType").append(" = ").append(EntityType.HybridRecordFolder.getIntValue());
/*  181 */     sb.append(" OR ").append("RMEntityType").append(" = ").append(EntityType.PhysicalRecordFolder.getIntValue()).append(")");
/*  182 */     sb.append(" AND (").append("OnHold").append(" = TRUE)");
/*  183 */     sb.append(" AND (").append("IsDeleted").append(" = FALSE)");
/*  184 */     sb.append(" AND (This INSUBFOLDER '%s') ");
/*  185 */     SqlForAnyPhysicalSubContainersOnHold = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  191 */     StringBuilder sb = new StringBuilder();
/*  192 */     sb.append("SELECT TOP 1 rmf.").append("Id").append(" FROM ").append("RMFolder").append(" rmf ");
/*  193 */     sb.append(" LEFT JOIN ").append("Volume").append(" v ON rmf.This = v.").append("Parent");
/*  194 */     sb.append(" WHERE (v.This IS NOT NULL)");
/*  195 */     sb.append("   AND (v.").append("OnHold").append(" = TRUE)");
/*  196 */     sb.append("   AND (rmf.").append("RMEntityType").append(" = ").append(EntityType.HybridRecordFolder.getIntValue()).append(" OR ");
/*  197 */     sb.append(" rmf.").append("RMEntityType").append(" = ").append(EntityType.PhysicalRecordFolder.getIntValue()).append(") ");
/*  198 */     sb.append("   AND (v.This INSUBFOLDER '%s') ");
/*  199 */     SqlForPhysicalVolumeOnHold = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  205 */     StringBuilder sb = new StringBuilder();
/*  206 */     sb.append("SELECT TOP 1 ").append("Id").append(" FROM ").append("Markers");
/*  207 */     sb.append(" WHERE (").append("OnHold").append(" = TRUE)");
/*  208 */     sb.append(" AND (").append("IsDeleted").append(" = FALSE)");
/*  209 */     sb.append(" AND (This INSUBFOLDER '%s') ");
/*  210 */     SqlForAnyPhysicalRecordsOnHold = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  216 */     StringBuilder sb = new StringBuilder();
/*  217 */     sb.append("SELECT TOP 1 ").append("Id").append(" FROM ").append("Markers");
/*  218 */     sb.append(" WHERE (").append("IsDeleted").append(" = FALSE)");
/*  219 */     sb.append(" AND (This INSUBFOLDER '%s') ");
/*  220 */     SqlForAnyPhysicalRecords = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  226 */     StringBuilder sb = new StringBuilder();
/*  227 */     sb.append("SELECT TOP 1 ").append("Id").append(" FROM ").append("RMFolder");
/*  228 */     sb.append(" WHERE (").append("IsDeleted").append(" = FALSE)");
/*  229 */     sb.append(" AND RMEntityType IN (106, 108, 110) ");
/*  230 */     sb.append(" AND (This INSUBFOLDER '%s') ");
/*  231 */     SqlForAnyPhysicalSubContainers = sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static P8CE_RALBulkOperationImpl getSingleton()
/*      */   {
/*  242 */     return Singleton;
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
/*      */   public List<BulkItemResult> activateContainers(FilePlanRepository repository, List<String> entityIdents)
/*      */   {
/*  256 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents });
/*  257 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  260 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  265 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  266 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/*  267 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/*  268 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/*  269 */       Map<String, BatchItemHandle> jaceRBItemMap = new HashMap();
/*  270 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(P8CE_BaseContainerImpl.getMandatoryJaceFEs());
/*  271 */       Folder jaceEntityBase = null;
/*  272 */       for (String entityIdent : entityIdents)
/*      */       {
/*  274 */         if (Id.isId(entityIdent))
/*      */         {
/*  276 */           Id jaceEntityId = new Id(entityIdent);
/*  277 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", jaceEntityId);
/*      */         }
/*      */         else
/*      */         {
/*  281 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", entityIdent);
/*      */         }
/*      */         
/*  284 */         jaceRBItemMap.put(entityIdent, jaceRB.add(jaceEntityBase, jacePF));
/*      */       }
/*  286 */       long startTime = System.currentTimeMillis();
/*  287 */       jaceRB.retrieveBatch();
/*  288 */       long endTime = System.currentTimeMillis();
/*  289 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(entityIdents.size()), null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/*  293 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/*  294 */       IGenerator<Container> generator = P8CE_BaseContainerImpl.getBaseContainerGenerator();
/*  295 */       List<BulkItemResult> results = new ArrayList(entityIdents.size());
/*  296 */       for (String entityIdent : entityIdents)
/*      */       {
/*  298 */         currentBulkResult = new P8CE_BulkItemResultImpl(entityIdent);
/*  299 */         results.add(currentBulkResult);
/*  300 */         BatchItemHandle currentBIH = (BatchItemHandle)jaceRBItemMap.get(entityIdent);
/*  301 */         if (currentBIH.hasException())
/*      */         {
/*      */ 
/*  304 */           currentBulkResult.setException(currentBIH.getException());
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           try
/*      */           {
/*      */ 
/*      */ 
/*  314 */             IndependentObject currentJaceContainerBase = currentBIH.getObject();
/*  315 */             RecordContainer jarmContainer = (RecordContainer)generator.create(repository, currentJaceContainerBase);
/*  316 */             EntityType containerEntityType = jarmContainer.getEntityType();
/*  317 */             if ((!(jarmContainer instanceof RecordCategory)) && (!(jarmContainer instanceof RecordFolder)))
/*      */             {
/*  319 */               Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_BULK_ACTIVATE_ENTITY_TYPE, new Object[] { containerEntityType, entityIdent });
/*  320 */               currentBulkResult.setException(ex);
/*      */             }
/*  322 */             else if (!jarmContainer.isInactive())
/*      */             {
/*  324 */               Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_ALREADY_ACTIVE, new Object[0]);
/*  325 */               currentBulkResult.setException(ex);
/*      */             }
/*  327 */             else if (((RALBaseContainer)jarmContainer).isAnyParentInactive())
/*      */             {
/*  329 */               Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PARENT_CONTAINER_IS_INACTIVE, new Object[0]);
/*  330 */               currentBulkResult.setException(ex);
/*      */             }
/*      */             else
/*      */             {
/*  334 */               jarmContainer.getProperties().putBooleanValue("Inactive", Boolean.FALSE);
/*  335 */               jarmContainer.save(RMRefreshMode.Refresh);
/*      */             }
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/*  340 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  345 */       Tracer.traceMethodExit(new Object[] { results });
/*  346 */       return results;
/*      */     }
/*      */     finally
/*      */     {
/*  350 */       if (establishedSubject) {
/*  351 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   public List<BulkItemResult> inactivateContainers(FilePlanRepository repository, List<String> entityIdents, String commonReasonForInactivate)
/*      */   {
/*  367 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, commonReasonForInactivate });
/*  368 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  371 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  373 */       if ((commonReasonForInactivate == null) || (commonReasonForInactivate.trim().length() == 0))
/*      */       {
/*  375 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_REASON_FOR_INACTIVE_IS_REQUIRED, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  381 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  382 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/*  383 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/*  384 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/*  385 */       Map<String, BatchItemHandle> jaceRBItemMap = new HashMap();
/*  386 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(P8CE_BaseContainerImpl.getMandatoryJaceFEs());
/*  387 */       Folder jaceEntityBase = null;
/*  388 */       for (String entityIdent : entityIdents)
/*      */       {
/*  390 */         if (Id.isId(entityIdent))
/*      */         {
/*  392 */           Id jaceEntityId = new Id(entityIdent);
/*  393 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", jaceEntityId);
/*      */         }
/*      */         else
/*      */         {
/*  397 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", entityIdent);
/*      */         }
/*      */         
/*  400 */         jaceRBItemMap.put(entityIdent, jaceRB.add(jaceEntityBase, jacePF));
/*      */       }
/*  402 */       long startTime = System.currentTimeMillis();
/*  403 */       jaceRB.retrieveBatch();
/*  404 */       long endTime = System.currentTimeMillis();
/*  405 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(entityIdents.size()), null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/*  409 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/*  410 */       IGenerator<Container> generator = P8CE_BaseContainerImpl.getBaseContainerGenerator();
/*  411 */       List<BulkItemResult> results = new ArrayList(entityIdents.size());
/*  412 */       for (String entityIdent : entityIdents)
/*      */       {
/*  414 */         currentBulkResult = new P8CE_BulkItemResultImpl(entityIdent);
/*  415 */         results.add(currentBulkResult);
/*  416 */         BatchItemHandle currentBIH = (BatchItemHandle)jaceRBItemMap.get(entityIdent);
/*  417 */         if (currentBIH.hasException())
/*      */         {
/*      */ 
/*  420 */           currentBulkResult.setException(currentBIH.getException());
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           try
/*      */           {
/*      */ 
/*      */ 
/*  430 */             IndependentObject currentJaceContainerBase = currentBIH.getObject();
/*  431 */             RecordContainer jarmContainer = (RecordContainer)generator.create(repository, currentJaceContainerBase);
/*  432 */             EntityType containerEntityType = jarmContainer.getEntityType();
/*  433 */             if ((!(jarmContainer instanceof RecordCategory)) && (!(jarmContainer instanceof RecordFolder)))
/*      */             {
/*  435 */               Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_BULK_INACTIVATE_ENTITY_TYPE, new Object[] { containerEntityType, entityIdent });
/*  436 */               currentBulkResult.setException(ex);
/*      */             }
/*  438 */             else if (jarmContainer.isInactive())
/*      */             {
/*  440 */               Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_ALREADY_INACTIVE, new Object[0]);
/*  441 */               currentBulkResult.setException(ex);
/*      */             }
/*      */             else
/*      */             {
/*  445 */               RMProperties props = jarmContainer.getProperties();
/*  446 */               props.putBooleanValue("Inactive", Boolean.TRUE);
/*  447 */               props.putStringValue("ReasonForInactivate", commonReasonForInactivate);
/*  448 */               jarmContainer.save(RMRefreshMode.Refresh);
/*      */               
/*  450 */               ((P8CE_BaseContainerImpl)jarmContainer).setSubContainersInactive(jarmContainer, commonReasonForInactivate);
/*      */             }
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/*  455 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  460 */       Tracer.traceMethodExit(new Object[] { results });
/*  461 */       return results;
/*      */     }
/*      */     finally
/*      */     {
/*  465 */       if (establishedSubject) {
/*  466 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   public List<BulkItemResult> closeContainers(FilePlanRepository repository, List<String> entityIdents, String commonReasonForClose)
/*      */   {
/*  482 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, commonReasonForClose });
/*  483 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  486 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  488 */       if ((commonReasonForClose == null) || (commonReasonForClose.trim().length() == 0))
/*      */       {
/*  490 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_REASON_FOR_CLOSE_IS_REQUIRED, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  496 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  497 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/*  498 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/*  499 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/*  500 */       Map<String, BatchItemHandle> jaceRBItemMap = new HashMap();
/*  501 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(P8CE_BaseContainerImpl.getMandatoryJaceFEs());
/*  502 */       Folder jaceEntityBase = null;
/*  503 */       for (String entityIdent : entityIdents)
/*      */       {
/*  505 */         if (Id.isId(entityIdent))
/*      */         {
/*  507 */           Id jaceEntityId = new Id(entityIdent);
/*  508 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", jaceEntityId);
/*      */         }
/*      */         else
/*      */         {
/*  512 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", entityIdent);
/*      */         }
/*      */         
/*  515 */         jaceRBItemMap.put(entityIdent, jaceRB.add(jaceEntityBase, jacePF));
/*      */       }
/*  517 */       long startTime = System.currentTimeMillis();
/*  518 */       jaceRB.retrieveBatch();
/*  519 */       long endTime = System.currentTimeMillis();
/*  520 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(entityIdents.size()), null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/*  524 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/*  525 */       IGenerator<Container> generator = P8CE_BaseContainerImpl.getBaseContainerGenerator();
/*  526 */       List<BulkItemResult> bulkResultsList = new ArrayList(entityIdents.size());
/*  527 */       Map<String, RecordContainer> recContainersToClose = new HashMap();
/*  528 */       for (String entityIdent : entityIdents)
/*      */       {
/*  530 */         currentBulkResult = new P8CE_BulkItemResultImpl(entityIdent);
/*  531 */         bulkResultsList.add(currentBulkResult);
/*  532 */         BatchItemHandle currentBIH = (BatchItemHandle)jaceRBItemMap.get(entityIdent);
/*  533 */         if (currentBIH.hasException())
/*      */         {
/*      */ 
/*  536 */           currentBulkResult.setException(currentBIH.getException());
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           try
/*      */           {
/*      */ 
/*  545 */             IndependentObject currentJaceContainerBase = currentBIH.getObject();
/*  546 */             RecordContainer jarmRecContainer = (RecordContainer)generator.create(repository, currentJaceContainerBase);
/*  547 */             if (jarmRecContainer.isOpen())
/*      */             {
/*  549 */               recContainersToClose.put(entityIdent, jarmRecContainer);
/*      */             }
/*      */             else
/*      */             {
/*  553 */               Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CLOSE_CONTAINER_ALREADY_CLOSED, new Object[0]);
/*  554 */               currentBulkResult.setException(ex);
/*      */             }
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/*  559 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  564 */       Date now = new Date();
/*  565 */       RMUser currentUser = repository.getDomain().fetchCurrentUser();
/*  566 */       String userName = currentUser.getShortName();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  572 */       for (BulkItemResult eligibleBulkResult : bulkResultsList)
/*      */       {
/*  574 */         if (eligibleBulkResult.wasSuccessful())
/*      */         {
/*      */           try
/*      */           {
/*      */ 
/*  579 */             UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.NO_REFRESH);
/*  580 */             RecordContainer currentRecContainer = (RecordContainer)recContainersToClose.get(eligibleBulkResult.getEntityIdent());
/*      */             
/*  582 */             Folder jaceRecContainerBase = (Folder)((JaceBasable)currentRecContainer).getJaceBaseObject();
/*  583 */             Properties jaceContainerProps = jaceRecContainerBase.getProperties();
/*  584 */             jaceContainerProps.putValue("DateClosed", now);
/*  585 */             jaceContainerProps.putValue("ReasonForClose", commonReasonForClose);
/*  586 */             jaceContainerProps.putValue("ClosedBy", userName);
/*  587 */             jaceContainerProps.putValue("ReOpenedDate", (Date)null);
/*  588 */             jaceUB.add(jaceRecContainerBase, P8CE_Util.CEPF_Empty);
/*      */             
/*  590 */             List<Folder> descendantContainers = fetchNonClosedSubContainers(currentRecContainer);
/*  591 */             for (Folder jaceDescContainerBase : descendantContainers)
/*      */             {
/*  593 */               jaceContainerProps = jaceDescContainerBase.getProperties();
/*  594 */               jaceContainerProps.putValue("ReasonForClose", commonReasonForClose);
/*  595 */               jaceContainerProps.putValue("ReOpenedDate", (Date)null);
/*  596 */               jaceContainerProps.putValue("ReOpenedBy", (String)null);
/*  597 */               if ((jaceContainerProps.isPropertyPresent("DateClosed")) && (jaceContainerProps.getDateTimeValue("DateClosed") == null))
/*      */               {
/*      */ 
/*  600 */                 jaceContainerProps.putValue("DateClosed", now);
/*  601 */                 jaceContainerProps.putValue("ClosedBy", userName);
/*  602 */                 jaceContainerProps.putValue("ReOpenedDate", (Date)null);
/*      */               }
/*      */               
/*  605 */               jaceUB.add(jaceDescContainerBase, P8CE_Util.CEPF_Empty);
/*      */             }
/*      */             
/*  608 */             startTime = System.currentTimeMillis();
/*  609 */             jaceUB.updateBatch();
/*  610 */             endTime = System.currentTimeMillis();
/*  611 */             Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, Integer.valueOf(descendantContainers.size()), null, new Object[0]);
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/*  615 */             ((P8CE_BulkItemResultImpl)eligibleBulkResult).setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*  619 */       Tracer.traceMethodExit(new Object[] { bulkResultsList });
/*  620 */       return bulkResultsList;
/*      */     }
/*      */     finally
/*      */     {
/*  624 */       if (establishedSubject) {
/*  625 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private List<Folder> fetchNonClosedSubContainers(RecordContainer parentContainer) {
/*  631 */     Tracer.traceMethodEntry(new Object[] { parentContainer });
/*  632 */     String parentIdent = parentContainer.getObjectIdentity();
/*  633 */     StringBuilder sb = new StringBuilder();
/*  634 */     sb.append("SELECT rf.[").append("Id").append("], rf.[").append("DateClosed").append("] ");
/*  635 */     sb.append("FROM [").append("RMFolder").append("] rf ");
/*  636 */     sb.append("WHERE rf.[This] INSUBFOLDER '").append(parentIdent).append("' ");
/*  637 */     sb.append(" AND rf.[").append("IsDeleted").append("] = FALSE");
/*  638 */     sb.append(" AND (rf.[").append("DateClosed").append("] IS NULL OR ");
/*  639 */     sb.append("rf.[").append("ReOpenedDate").append("] IS NOT NULL) ");
/*  640 */     String sqlStatement = sb.toString();
/*      */     
/*      */ 
/*  643 */     List<Folder> jaceSubContainers = new ArrayList();
/*  644 */     Folder jaceParentFolder = ((P8CE_BaseContainerImpl)parentContainer).jaceFolder;
/*  645 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/*  646 */     SearchScope jaceSearchScope = new SearchScope(jaceParentFolder.getObjectStore());
/*  647 */     Integer pageSize = null;
/*  648 */     Boolean continuable = Boolean.TRUE;
/*      */     
/*  650 */     long startTime = System.currentTimeMillis();
/*  651 */     IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, continuable);
/*  652 */     long endTime = System.currentTimeMillis();
/*  653 */     Boolean elementCountIndicator = null;
/*  654 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/*  656 */       elementCountIndicator = Boolean.valueOf(jaceFolderSet.isEmpty());
/*      */     }
/*  658 */     Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement, null, P8CE_Util.CEPF_IdOnly, continuable });
/*      */     
/*  660 */     Folder jaceFolder = null;
/*  661 */     PageIterator jacePI = jaceFolderSet.pageIterator();
/*  662 */     while (jacePI.nextPage())
/*      */     {
/*  664 */       Object[] currentPage = jacePI.getCurrentPage();
/*  665 */       for (Object obj : currentPage)
/*      */       {
/*  667 */         jaceFolder = (Folder)obj;
/*  668 */         jaceSubContainers.add(jaceFolder);
/*      */       }
/*      */     }
/*      */     
/*  672 */     Tracer.traceMethodExit(new Object[] { jaceSubContainers });
/*  673 */     return jaceSubContainers;
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
/*      */   public List<BulkItemResult> reopenContainers(FilePlanRepository repository, List<String> entityIdents)
/*      */   {
/*  687 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents });
/*  688 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  691 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*  694 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  695 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/*  696 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/*  697 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/*  698 */       Map<String, BatchItemHandle> jaceRBItemMap = new HashMap();
/*  699 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(P8CE_BaseContainerImpl.getMandatoryJaceFEs());
/*  700 */       Folder jaceEntityBase = null;
/*  701 */       for (String entityIdent : entityIdents)
/*      */       {
/*  703 */         if (Id.isId(entityIdent))
/*      */         {
/*  705 */           Id jaceEntityId = new Id(entityIdent);
/*  706 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", jaceEntityId);
/*      */         }
/*      */         else
/*      */         {
/*  710 */           jaceEntityBase = Factory.Folder.getInstance(jaceRepositoryBase, "Folder", entityIdent);
/*      */         }
/*      */         
/*  713 */         jaceRBItemMap.put(entityIdent, jaceRB.add(jaceEntityBase, jacePF));
/*      */       }
/*  715 */       long startTime = System.currentTimeMillis();
/*  716 */       jaceRB.retrieveBatch();
/*  717 */       long endTime = System.currentTimeMillis();
/*  718 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(entityIdents.size()), null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/*  722 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/*  723 */       IGenerator<Container> generator = P8CE_BaseContainerImpl.getBaseContainerGenerator();
/*  724 */       List<BulkItemResult> bulkResultsList = new ArrayList(entityIdents.size());
/*  725 */       for (String entityIdent : entityIdents)
/*      */       {
/*  727 */         currentBulkResult = new P8CE_BulkItemResultImpl(entityIdent);
/*  728 */         bulkResultsList.add(currentBulkResult);
/*  729 */         BatchItemHandle currentBIH = (BatchItemHandle)jaceRBItemMap.get(entityIdent);
/*  730 */         if (currentBIH.hasException())
/*      */         {
/*      */ 
/*  733 */           currentBulkResult.setException(currentBIH.getException());
/*      */         }
/*      */         else
/*      */         {
/*      */           try
/*      */           {
/*  739 */             IndependentObject currentJaceContainerBase = currentBIH.getObject();
/*  740 */             RecordContainer jarmRecContainer = (RecordContainer)generator.create(repository, currentJaceContainerBase);
/*  741 */             jarmRecContainer.reopen();
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/*  745 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  750 */       Tracer.traceMethodExit(new Object[] { bulkResultsList });
/*  751 */       return bulkResultsList;
/*      */     }
/*      */     finally
/*      */     {
/*  755 */       if (establishedSubject) {
/*  756 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String SqlForSubContainersNotOnHold;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String SqlForOnHoldSubContainerIDs;
/*      */   
/*      */ 
/*      */   public List<BulkItemResult> placeHolds(FilePlanRepository repository, List<BulkOperation.EntityDescription> entityDescriptions, List<String> holdIdents)
/*      */   {
/*  773 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescriptions, holdIdents });
/*  774 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  777 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*  780 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  781 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/*  782 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/*  783 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/*      */       
/*  785 */       Map<String, BatchItemHandle> jaceEntityRBItemMap = new HashMap();
/*      */       
/*  787 */       Map<String, BatchItemHandle> jaceHoldRBItemMap = new HashMap();
/*      */       
/*  789 */       PropertyFilter jaceEntityPF = P8CE_Util.buildMandatoryJacePF(P8CE_BaseEntityImpl.getMandatoryJaceFEs());
/*  790 */       String propNames = "Holds Tail";
/*  791 */       jaceEntityPF.addIncludeProperty(1, null, null, propNames, null);
/*  792 */       PropertyFilter jaceHoldPF = P8CE_Util.buildMandatoryJacePF(P8CE_HoldImpl.getMandatoryJaceFEs());
/*      */       
/*  794 */       IndependentObject jaceBaseEntity = null;
/*  795 */       EntityType entityType = null;
/*  796 */       String entityIdent = null;
/*  797 */       Id jaceBaseEntityId = null;
/*  798 */       for (BulkOperation.EntityDescription entityDesc : entityDescriptions)
/*      */       {
/*  800 */         entityIdent = entityDesc.getEntityIdent();
/*  801 */         jaceBaseEntityId = Id.isId(entityIdent) ? new Id(entityIdent) : null;
/*  802 */         entityType = entityDesc.getEntityType();
/*  803 */         if (isRecordContainerEntityType(entityType))
/*      */         {
/*  805 */           if (jaceBaseEntityId != null) {
/*  806 */             jaceBaseEntity = Factory.Folder.getInstance(jaceRepositoryBase, "RMFolder", jaceBaseEntityId);
/*      */           } else {
/*  808 */             jaceBaseEntity = Factory.Folder.getInstance(jaceRepositoryBase, "RMFolder", entityIdent);
/*      */           }
/*  810 */         } else if (isRecordEntityType(entityType))
/*      */         {
/*  812 */           if (jaceBaseEntityId != null) {
/*  813 */             jaceBaseEntity = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", jaceBaseEntityId);
/*      */           } else {
/*  815 */             jaceBaseEntity = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", entityIdent);
/*      */           }
/*      */         }
/*  818 */         jaceEntityRBItemMap.put(entityIdent, jaceRB.add(jaceBaseEntity, jaceEntityPF));
/*      */       }
/*      */       
/*  821 */       Id jaceHoldId = null;
/*  822 */       IndependentObject jaceHoldBase = null;
/*  823 */       for (String holdIdent : holdIdents)
/*      */       {
/*  825 */         jaceHoldId = Id.isId(holdIdent) ? new Id(holdIdent) : null;
/*  826 */         if (jaceHoldId != null) {
/*  827 */           jaceHoldBase = Factory.CustomObject.getInstance(jaceRepositoryBase, "RecordHold", jaceHoldId);
/*      */         } else {
/*  829 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_HOLD_IDENT, new Object[] { holdIdent });
/*      */         }
/*  831 */         jaceHoldRBItemMap.put(holdIdent, jaceRB.add(jaceHoldBase, jaceHoldPF));
/*      */       }
/*      */       
/*  834 */       long startTime = System.currentTimeMillis();
/*  835 */       jaceRB.retrieveBatch();
/*  836 */       long endTime = System.currentTimeMillis();
/*  837 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  842 */       Map<String, IndependentObject> jaceHoldBaseMap = new LinkedHashMap();
/*  843 */       BatchItemHandle jaceHoldBIH = null;
/*  844 */       for (String holdIdent : holdIdents)
/*      */       {
/*  846 */         jaceHoldBIH = (BatchItemHandle)jaceHoldRBItemMap.get(holdIdent);
/*  847 */         if (jaceHoldBIH.hasException())
/*      */         {
/*  849 */           throw P8CE_Util.processJaceException(jaceHoldBIH.getException(), RMErrorCode.RAL_UNABLE_TO_RETRIEVE_HOLD, new Object[] { holdIdent });
/*      */         }
/*  851 */         if (Boolean.FALSE.equals(jaceHoldBIH.getObject().getProperties().getBooleanValue("Active")))
/*      */         {
/*  853 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_APPLY_HOLD_THAT_IS_NOT_ACTIVE, new Object[] { holdIdent });
/*      */         }
/*  855 */         if (!jaceHoldBaseMap.containsKey(holdIdent))
/*      */         {
/*  857 */           jaceHoldBase = jaceHoldBIH.getObject();
/*  858 */           jaceHoldBaseMap.put(holdIdent, jaceHoldBase);
/*      */         }
/*      */       }
/*      */       
/*  862 */       String holdAuditReason = RMLString.get("audit.holdReason", "Manual hold");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  867 */       List<BulkItemResult> bulkResultsList = new ArrayList(entityDescriptions.size());
/*  868 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/*  869 */       BatchItemHandle jaceEntityBIH = null;
/*  870 */       BaseEntity jarmCurrentEntity = null;
/*  871 */       for (BulkOperation.EntityDescription entityDesc : entityDescriptions)
/*      */       {
/*  873 */         entityIdent = entityDesc.getEntityIdent();
/*  874 */         entityType = entityDesc.getEntityType();
/*  875 */         currentBulkResult = new P8CE_BulkItemResultImpl(entityIdent);
/*  876 */         bulkResultsList.add(currentBulkResult);
/*  877 */         jaceEntityBIH = (BatchItemHandle)jaceEntityRBItemMap.get(entityIdent);
/*  878 */         if (jaceEntityBIH.hasException())
/*      */         {
/*      */ 
/*  881 */           currentBulkResult.setException(jaceEntityBIH.getException());
/*      */         }
/*      */         else
/*      */         {
/*  885 */           IndependentlyPersistableObject jaceCurrentEntityBase = (IndependentlyPersistableObject)jaceEntityBIH.getObject();
/*  886 */           jarmCurrentEntity = new P8CE_BaseEntityImpl(repository, entityIdent, null, entityType, jaceCurrentEntityBase, false);
/*      */           
/*      */ 
/*  889 */           IndependentObjectSet entityExistingHoldLinks = jaceCurrentEntityBase.getProperties().getIndependentObjectSetValue("Holds");
/*  890 */           Set<String> existingEntityHoldIdentSet = new HashSet();
/*  891 */           for (Iterator it = entityExistingHoldLinks.iterator(); it.hasNext();)
/*      */           {
/*  893 */             IndependentObject jaceHoldLinkBase = (IndependentObject)it.next();
/*  894 */             IndependentObject jaceTailBase = (IndependentObject)jaceHoldLinkBase.getProperties().getEngineObjectValue("Tail");
/*  895 */             existingEntityHoldIdentSet.add(jaceTailBase.getObjectReference().getObjectIdentity());
/*      */           }
/*      */           
/*      */ 
/*  899 */           List<String> dupHoldNames = new ArrayList(0);
/*  900 */           List<BatchItemHandle> jaceHoldLinkBIHs = new ArrayList(jaceHoldBaseMap.size());
/*  901 */           for (IndependentObject jaceHoldBase2 : jaceHoldBaseMap.values())
/*      */           {
/*  903 */             String holdName = jaceHoldBase2.getProperties().getStringValue("HoldName");
/*  904 */             if (existingEntityHoldIdentSet.contains(jaceHoldBase2.getObjectReference().getObjectIdentity()))
/*      */             {
/*      */ 
/*  907 */               dupHoldNames.add(holdName);
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  912 */               UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */               
/*      */ 
/*  915 */               String holdLinkClassIdent = (jaceCurrentEntityBase instanceof Folder) ? "RMFolderHoldLink" : "RecordHoldLink";
/*      */               
/*  917 */               Link jaceNewHoldLinkBase = Factory.Link.createInstance(jaceRepositoryBase, holdLinkClassIdent);
/*  918 */               Properties jaceLinkProps = jaceNewHoldLinkBase.getProperties();
/*  919 */               jaceLinkProps.putValue("Head", jaceCurrentEntityBase);
/*  920 */               jaceLinkProps.putValue("Tail", jaceHoldBase2);
/*  921 */               jaceLinkProps.putValue("IsDynamicHold", Boolean.FALSE);
/*  922 */               jaceHoldLinkBIHs.add(jaceUB.add(jaceNewHoldLinkBase, P8CE_Util.CEPF_Empty));
/*      */               
/*      */ 
/*  925 */               Properties jaceCurrentEntityProps = jaceCurrentEntityBase.getProperties();
/*  926 */               jaceCurrentEntityProps.putValue("OnHold", Boolean.TRUE);
/*  927 */               jaceCurrentEntityProps.putValue("PreventRMEntityDeletion", "Prevent Delete");
/*  928 */               jaceUB.add(jaceCurrentEntityBase, P8CE_Util.CEPF_Empty);
/*      */               
/*  930 */               P8CE_AuditServices.attachHoldAuditEvent(jarmCurrentEntity, holdName, holdAuditReason, AuditStatus.Success, false);
/*      */               
/*      */               try
/*      */               {
/*  934 */                 startTime = System.currentTimeMillis();
/*  935 */                 jaceUB.updateBatch();
/*  936 */                 endTime = System.currentTimeMillis();
/*  937 */                 Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */               }
/*      */               catch (Exception ex)
/*      */               {
/*  941 */                 currentBulkResult.setException(ex);
/*  942 */                 P8CE_AuditServices.attachHoldAuditEvent(jarmCurrentEntity, holdName, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/*  943 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  950 */           if ((currentBulkResult.getException() == null) && (dupHoldNames.size() > 0))
/*      */           {
/*      */ 
/*  953 */             StringBuilder sb = new StringBuilder();
/*  954 */             for (int i = 0; i < dupHoldNames.size(); i++)
/*      */             {
/*  956 */               if (i > 0) {
/*  957 */                 sb.append(", ");
/*      */               }
/*  959 */               sb.append((String)dupHoldNames.get(i));
/*      */             }
/*  961 */             Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_HOLD_ALREADY_ASSOC_WITH_ENTITY, new Object[] { sb.toString() });
/*  962 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*  966 */       Tracer.traceMethodExit(new Object[] { bulkResultsList });
/*  967 */       return bulkResultsList;
/*      */     }
/*      */     finally
/*      */     {
/*  971 */       if (establishedSubject) {
/*  972 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   public List<BulkItemResult> removeHolds(FilePlanRepository repository, BulkOperation.EntityDescription entityDescription, List<String> holdIdents)
/*      */   {
/*  989 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescription, holdIdents });
/*  990 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  993 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*  996 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  997 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/*  998 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/*  999 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/*      */       
/* 1001 */       BatchItemHandle jaceEntityRBItem = null;
/*      */       
/* 1003 */       Map<String, BatchItemHandle> jaceHoldRBItemMap = new HashMap();
/*      */       
/* 1005 */       PropertyFilter jaceEntityPF = P8CE_Util.buildMandatoryJacePF(P8CE_BaseEntityImpl.getMandatoryJaceFEs());
/* 1006 */       String propNames = "Holds Tail IsDynamicHold HoldName";
/* 1007 */       jaceEntityPF.addIncludeProperty(1, null, null, propNames, null);
/* 1008 */       PropertyFilter jaceHoldPF = P8CE_Util.buildMandatoryJacePF(P8CE_HoldImpl.getMandatoryJaceFEs());
/*      */       
/* 1010 */       IndependentlyPersistableObject jaceBaseEntity = null;
/* 1011 */       String entityIdent = entityDescription.getEntityIdent();
/* 1012 */       Id jaceBaseEntityId = Id.isId(entityIdent) ? new Id(entityIdent) : null;
/* 1013 */       EntityType entityType = entityDescription.getEntityType();
/* 1014 */       if (isRecordContainerEntityType(entityType))
/*      */       {
/* 1016 */         if (jaceBaseEntityId != null) {
/* 1017 */           jaceBaseEntity = Factory.Folder.getInstance(jaceRepositoryBase, "RMFolder", jaceBaseEntityId);
/*      */         } else {
/* 1019 */           jaceBaseEntity = Factory.Folder.getInstance(jaceRepositoryBase, "RMFolder", entityIdent);
/*      */         }
/* 1021 */       } else if (isRecordEntityType(entityType))
/*      */       {
/* 1023 */         if (jaceBaseEntityId != null) {
/* 1024 */           jaceBaseEntity = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", jaceBaseEntityId);
/*      */         } else
/* 1026 */           jaceBaseEntity = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", entityIdent);
/*      */       }
/* 1028 */       jaceEntityRBItem = jaceRB.add(jaceBaseEntity, jaceEntityPF);
/*      */       
/*      */ 
/* 1031 */       CustomObject jaceBaseHold = null;
/* 1032 */       for (String holdIdent : holdIdents)
/*      */       {
/* 1034 */         Id jaceBaseHoldId = Id.isId(holdIdent) ? new Id(holdIdent) : null;
/* 1035 */         if (jaceBaseHoldId != null) {
/* 1036 */           jaceBaseHold = Factory.CustomObject.getInstance(jaceRepositoryBase, "RecordHold", jaceBaseHoldId);
/*      */         } else {
/* 1038 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_HOLD_IDENT, new Object[] { holdIdent });
/*      */         }
/* 1040 */         jaceHoldRBItemMap.put(holdIdent, jaceRB.add(jaceBaseHold, jaceHoldPF));
/*      */       }
/*      */       
/* 1043 */       long startTime = System.currentTimeMillis();
/* 1044 */       jaceRB.retrieveBatch();
/* 1045 */       long endTime = System.currentTimeMillis();
/* 1046 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/* 1050 */       if (jaceEntityRBItem.hasException())
/*      */       {
/* 1052 */         Exception ex = jaceEntityRBItem.getException();
/* 1053 */         throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { entityIdent, entityType });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1058 */       Map<String, IndependentObject> jaceHoldBaseMap = new LinkedHashMap();
/* 1059 */       BatchItemHandle jaceHoldBIH = null;
/* 1060 */       for (String holdIdent : holdIdents)
/*      */       {
/* 1062 */         jaceHoldBIH = (BatchItemHandle)jaceHoldRBItemMap.get(holdIdent);
/* 1063 */         if (jaceHoldBIH.hasException())
/*      */         {
/* 1065 */           throw P8CE_Util.processJaceException(jaceHoldBIH.getException(), RMErrorCode.RAL_UNABLE_TO_RETRIEVE_HOLD, new Object[] { holdIdent });
/*      */         }
/* 1067 */         if (!jaceHoldBaseMap.containsKey(holdIdent))
/*      */         {
/* 1069 */           jaceHoldBaseMap.put(holdIdent, jaceHoldBIH.getObject());
/*      */         }
/*      */       }
/*      */       
/* 1073 */       boolean auditEnabled = ((P8CE_RepositoryImpl)repository).isAuditEnabled();
/* 1074 */       String holdAuditReason = RMLString.get("audit.removeHoldReason", "Manual remove hold");
/*      */       
/* 1076 */       List<BulkItemResult> bulkResultsList = new ArrayList(holdIdents.size());
/* 1077 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/* 1078 */       IndependentObjectSet jaceHoldLinkSet = jaceBaseEntity.getProperties().getIndependentObjectSetValue("Holds");
/* 1079 */       BaseEntity jarmCurrentEntity = new P8CE_BaseEntityImpl(repository, entityIdent, null, entityType, jaceBaseEntity, false);
/*      */       
/* 1081 */       IndependentlyPersistableObject jaceHoldLink = null;
/* 1082 */       IndependentObject jaceHoldLinkTail = null;
/* 1083 */       String holdLinkTailIdent = null;
/*      */       
/*      */ 
/* 1086 */       int countOfHoldsRemoved = 0;
/* 1087 */       for (Map.Entry<String, IndependentObject> holdBaseMapEntry : jaceHoldBaseMap.entrySet())
/*      */       {
/* 1089 */         String holdIdent = (String)holdBaseMapEntry.getKey();
/* 1090 */         IndependentObject jaceHoldBase = (IndependentObject)holdBaseMapEntry.getValue();
/* 1091 */         String holdName = jaceHoldBase.getProperties().getStringValue("HoldName");
/*      */         try
/*      */         {
/* 1094 */           currentBulkResult = new P8CE_BulkItemResultImpl(holdIdent);
/* 1095 */           bulkResultsList.add(currentBulkResult);
/*      */           
/*      */ 
/*      */ 
/* 1099 */           boolean holdIsCurrentlyApplied = false;
/* 1100 */           Boolean holdLinkIsDynamic = null;
/* 1101 */           for (Iterator it = jaceHoldLinkSet.iterator(); it.hasNext();)
/*      */           {
/* 1103 */             jaceHoldLink = (IndependentlyPersistableObject)it.next();
/* 1104 */             jaceHoldLinkTail = (IndependentObject)jaceHoldLink.getProperties().getEngineObjectValue("Tail");
/* 1105 */             holdLinkTailIdent = jaceHoldLinkTail.getObjectReference().getObjectIdentity();
/* 1106 */             if (holdIdent.equalsIgnoreCase(holdLinkTailIdent))
/*      */             {
/* 1108 */               holdIsCurrentlyApplied = true;
/* 1109 */               holdLinkIsDynamic = jaceHoldLink.getProperties().getBooleanValue("IsDynamicHold");
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1114 */           if (!holdIsCurrentlyApplied)
/*      */           {
/* 1116 */             Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_REMOVE_HOLD_NOT_ASSOC_WITH_OBJECT, new Object[] { holdIdent });
/* 1117 */             currentBulkResult.setException(ex);
/* 1118 */             P8CE_AuditServices.attachRemoveHoldAuditEvent(jarmCurrentEntity, holdName, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/*      */ 
/*      */ 
/*      */           }
/* 1122 */           else if (Boolean.TRUE.equals(holdLinkIsDynamic))
/*      */           {
/*      */ 
/*      */ 
/* 1126 */             Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MANUALLY_REMOVE_DYNAMIC_HOLD, new Object[] { holdIdent });
/* 1127 */             currentBulkResult.setException(ex);
/* 1128 */             P8CE_AuditServices.attachRemoveHoldAuditEvent(jarmCurrentEntity, holdName, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1134 */             UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */             
/*      */ 
/* 1137 */             jaceHoldLink.delete();
/* 1138 */             jaceUB.add(jaceHoldLink, P8CE_Util.CEPF_Empty);
/*      */             
/* 1140 */             if (auditEnabled)
/*      */             {
/* 1142 */               P8CE_AuditServices.attachRemoveHoldAuditEvent(jarmCurrentEntity, holdName, holdAuditReason, AuditStatus.Success, false);
/* 1143 */               jaceUB.add(jaceBaseEntity, jaceEntityPF);
/*      */             }
/*      */             
/* 1146 */             startTime = System.currentTimeMillis();
/* 1147 */             jaceUB.updateBatch();
/* 1148 */             endTime = System.currentTimeMillis();
/* 1149 */             Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */             
/* 1151 */             countOfHoldsRemoved++;
/*      */           }
/*      */         }
/*      */         catch (Exception ex) {
/* 1155 */           currentBulkResult.setException(ex);
/* 1156 */           P8CE_AuditServices.attachRemoveHoldAuditEvent(jarmCurrentEntity, holdName, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/*      */         }
/*      */       }
/*      */       
/* 1160 */       if (countOfHoldsRemoved > 0)
/*      */       {
/*      */ 
/*      */ 
/* 1164 */         jaceBaseEntity.refresh();
/* 1165 */         jaceHoldLinkSet = jaceBaseEntity.getProperties().getIndependentObjectSetValue("Holds");
/* 1166 */         if (jaceHoldLinkSet.isEmpty())
/*      */         {
/* 1168 */           jaceBaseEntity.getProperties().putValue("OnHold", Boolean.FALSE);
/* 1169 */           jaceBaseEntity.getProperties().putValue("PreventRMEntityDeletion", "Default");
/* 1170 */           jaceBaseEntity.save(RefreshMode.NO_REFRESH);
/*      */         }
/*      */       }
/*      */       
/* 1174 */       Tracer.traceMethodExit(new Object[] { bulkResultsList });
/* 1175 */       return bulkResultsList;
/*      */     }
/*      */     finally
/*      */     {
/* 1179 */       if (establishedSubject) {
/* 1180 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   public List<BulkItemResult> delete(FilePlanRepository repository, List<BulkOperation.EntityDescription> entityDescriptions)
/*      */   {
/* 1195 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescriptions });
/* 1196 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1199 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 1203 */       List<BulkItemResult> bulkResultsList = new ArrayList(entityDescriptions.size());
/*      */       
/*      */ 
/* 1206 */       EntityType entityType = null;
/* 1207 */       String entityIdent = null;
/* 1208 */       P8CE_BulkItemResultImpl currentItemResult = null;
/* 1209 */       Map<String, BaseEntity> jarmEntitiesToDelete = new HashMap();
/* 1210 */       for (BulkOperation.EntityDescription entityDesc : entityDescriptions)
/*      */       {
/* 1212 */         entityType = entityDesc.getEntityType();
/* 1213 */         entityIdent = entityDesc.getEntityIdent();
/* 1214 */         currentItemResult = new P8CE_BulkItemResultImpl(entityIdent);
/* 1215 */         bulkResultsList.add(currentItemResult);
/*      */         try
/*      */         {
/* 1218 */           if (isRecordContainerEntityType(entityType))
/*      */           {
/* 1220 */             P8CE_BaseContainerImpl jarmContainer = (P8CE_BaseContainerImpl)RMFactory.Container.fetchInstance(repository, entityType, entityIdent, RMPropertyFilter.MinimumPropertySet);
/* 1221 */             jarmEntitiesToDelete.put(entityIdent, jarmContainer);
/*      */           }
/* 1223 */           else if (isRecordEntityType(entityType))
/*      */           {
/* 1225 */             P8CE_RecordImpl jarmRecord = (P8CE_RecordImpl)RMFactory.Record.fetchInstance(repository, entityIdent, RMPropertyFilter.MinimumPropertySet);
/* 1226 */             jarmEntitiesToDelete.put(entityIdent, jarmRecord);
/*      */           }
/*      */           else
/*      */           {
/* 1230 */             Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_BULK_DELETE_ENTITY_TYPE, new Object[] { entityType, entityIdent });
/* 1231 */             currentItemResult.setException(ex);
/*      */           }
/*      */         }
/*      */         catch (Exception ex)
/*      */         {
/* 1236 */           currentItemResult.setException(ex);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1241 */       boolean skipDeleteValidation = false;
/* 1242 */       for (BulkItemResult itemResult : bulkResultsList)
/*      */       {
/* 1244 */         if (itemResult.wasSuccessful())
/*      */         {
/* 1246 */           BaseEntity entityToDelete = (BaseEntity)jarmEntitiesToDelete.get(itemResult.getEntityIdent());
/*      */           try
/*      */           {
/* 1249 */             if (isRecordContainerEntityType(entityToDelete.getEntityType()))
/*      */             {
/* 1251 */               ((Container)entityToDelete).delete(skipDeleteValidation, DeleteMode.CheckRetainMetadata, BulkDefaultReasonForDelete);
/*      */             }
/*      */             else
/*      */             {
/* 1255 */               ((Record)entityToDelete).delete(skipDeleteValidation, DeleteMode.CheckRetainMetadata, BulkDefaultReasonForDelete);
/*      */             }
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/* 1260 */             ((P8CE_BulkItemResultImpl)itemResult).setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1265 */       Tracer.traceMethodExit(new Object[] { bulkResultsList });
/* 1266 */       return bulkResultsList;
/*      */     }
/*      */     finally
/*      */     {
/* 1270 */       if (establishedSubject) {
/* 1271 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   public List<BulkItemResult> fileRecords(FilePlanRepository repository, List<String> entityIdents, String destinationContainerIdent)
/*      */   {
/* 1286 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, destinationContainerIdent });
/* 1287 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1290 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 1293 */       if (!repository.isRecordMultiFilingEnabled())
/*      */       {
/* 1295 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MULTIFILE_RECORDS, new Object[] { repository.getName() });
/*      */       }
/*      */       
/* 1298 */       RALBaseContainer targetContainer = (RALBaseContainer)RMFactory.Container.fetchInstance(repository, EntityType.Container, destinationContainerIdent, RMPropertyFilter.MinimumPropertySet);
/*      */       
/*      */ 
/*      */ 
/* 1302 */       if (((targetContainer instanceof DefensiblyDisposable)) && (((DefensiblyDisposable)targetContainer).isADefensiblyDisposableContainer()))
/*      */       {
/*      */ 
/* 1305 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MULTIFILE_INTO_DD_CONTAINER, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1314 */       RecordContainer actualTargetContainer = targetContainer.validateRecordFiling_part1();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1319 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 1320 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/* 1321 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/* 1322 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/* 1323 */       Map<String, BatchItemHandle> jaceRBItemMap = new HashMap();
/* 1324 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(P8CE_RecordImpl.getMandatoryJaceFEs());
/* 1325 */       Document jaceRecordBaseDoc = null;
/* 1326 */       for (String entityIdent : entityIdents)
/*      */       {
/* 1328 */         if (Id.isId(entityIdent))
/*      */         {
/* 1330 */           Id jaceRecordId = new Id(entityIdent);
/* 1331 */           jaceRecordBaseDoc = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", jaceRecordId);
/*      */         }
/*      */         else
/*      */         {
/* 1335 */           jaceRecordBaseDoc = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", entityIdent);
/*      */         }
/*      */         
/* 1338 */         jaceRBItemMap.put(entityIdent, jaceRB.add(jaceRecordBaseDoc, jacePF));
/*      */       }
/* 1340 */       long startTime = System.currentTimeMillis();
/* 1341 */       jaceRB.retrieveBatch();
/* 1342 */       long endTime = System.currentTimeMillis();
/* 1343 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/* 1347 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/* 1348 */       IGenerator<Record> generator = P8CE_RecordImpl.getGenerator();
/* 1349 */       List<BulkItemResult> results = new ArrayList(entityIdents.size());
/* 1350 */       Map<String, Record> recordsMap = new HashMap();
/* 1351 */       for (String entityIdent : entityIdents)
/*      */       {
/* 1353 */         currentBulkResult = new P8CE_BulkItemResultImpl(entityIdent);
/* 1354 */         results.add(currentBulkResult);
/* 1355 */         BatchItemHandle currentBIH = (BatchItemHandle)jaceRBItemMap.get(entityIdent);
/* 1356 */         if (currentBIH.hasException())
/*      */         {
/*      */ 
/* 1359 */           currentBulkResult.setException(currentBIH.getException());
/*      */         }
/*      */         else
/*      */         {
/*      */           try
/*      */           {
/* 1365 */             IndependentObject currentJaceRecordBase = currentBIH.getObject();
/* 1366 */             Record jarmRecord = (Record)generator.create(repository, currentJaceRecordBase);
/* 1367 */             recordsMap.put(entityIdent, jarmRecord);
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/* 1371 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1378 */       boolean forFileOperation = true;
/* 1379 */       for (int i = 0; i < results.size(); i++)
/*      */       {
/* 1381 */         currentBulkResult = (P8CE_BulkItemResultImpl)results.get(i);
/* 1382 */         if (currentBulkResult.wasSuccessful())
/*      */         {
/*      */           try
/*      */           {
/* 1386 */             Record record = (Record)recordsMap.get(currentBulkResult.getEntityIdent());
/*      */             
/*      */ 
/* 1389 */             ((RALBaseContainer)actualTargetContainer).validateRecordFiling_part2(actualTargetContainer, record, null, forFileOperation);
/*      */             
/*      */ 
/* 1392 */             Folder actualTargetJaceFolderBase = ((P8CE_BaseContainerImpl)actualTargetContainer).jaceFolder;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1397 */             DefineSecurityParentage dsp = DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE;
/* 1398 */             Container securityFolder = ((P8CE_RecordImpl)record).getSecurityFolder();
/* 1399 */             if (securityFolder == null) {
/* 1400 */               dsp = DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE;
/*      */             }
/*      */             
/* 1403 */             IndependentlyPersistableObject jaceRecBase = (IndependentlyPersistableObject)((JaceBasable)record).getJaceBaseObject();
/*      */             
/* 1405 */             AutoUniqueName aun = AutoUniqueName.AUTO_UNIQUE;
/* 1406 */             String containmentName = null;
/* 1407 */             ReferentialContainmentRelationship rcr = actualTargetJaceFolderBase.file(jaceRecBase, aun, containmentName, dsp);
/*      */             
/*      */ 
/* 1410 */             startTime = System.currentTimeMillis();
/* 1411 */             rcr.save(RefreshMode.NO_REFRESH);
/* 1412 */             endTime = System.currentTimeMillis();
/* 1413 */             Tracer.traceExtCall("RCR.save", startTime, endTime, null, null, new Object[0]);
/*      */             
/* 1415 */             currentBulkResult.setSuccess(true);
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/* 1419 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1425 */       Tracer.traceMethodExit(new Object[] { results });
/* 1426 */       return results;
/*      */     }
/*      */     finally
/*      */     {
/* 1430 */       if (establishedSubject) {
/* 1431 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String SqlForAnyPhysicalSubContainersOnHold;
/*      */   
/*      */ 
/*      */   private static final String SqlForPhysicalVolumeOnHold;
/*      */   
/*      */   private static final String SqlForAnyPhysicalRecordsOnHold;
/*      */   
/*      */   private static final String SqlForAnyPhysicalRecords;
/*      */   
/*      */   private static final String SqlForAnyPhysicalSubContainers;
/*      */   
/*      */   public List<BulkItemResult> moveRecords(FilePlanRepository repository, List<String> entityIdents, String sourceContainerIdent, String destinationContainerIdent, String reasonForMove)
/*      */   {
/* 1451 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, sourceContainerIdent, destinationContainerIdent, reasonForMove });
/* 1452 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1455 */       establishedSubject = P8CE_Util.associateSubject();
/* 1456 */       RALBaseContainer targetContainer = (RALBaseContainer)RMFactory.Container.fetchInstance(repository, EntityType.Container, destinationContainerIdent, RMPropertyFilter.MinimumPropertySet);
/* 1457 */       RALBaseContainer sourceContainer = (RALBaseContainer)RMFactory.Container.fetchInstance(repository, EntityType.Container, sourceContainerIdent, RMPropertyFilter.MinimumPropertySet);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1465 */       RecordContainer actualTargetContainer = targetContainer.validateRecordFiling_part1();
/*      */       
/*      */ 
/* 1468 */       String actualTargetContainerIdent = actualTargetContainer.getObjectIdentity();
/* 1469 */       String actualSrcContainerIdent = sourceContainer.getObjectIdentity();
/* 1470 */       if (actualTargetContainerIdent.equalsIgnoreCase(actualSrcContainerIdent))
/*      */       {
/* 1472 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_RECORD_IT_ALREADY_EXISTS_IN_CONTAINER, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1478 */       boolean targetIsDDContainer = false;
/* 1479 */       Set<String> applicableDDRecordClassNames = null;
/* 1480 */       if (((targetContainer instanceof DefensiblyDisposable)) && (((DefensiblyDisposable)targetContainer).isADefensiblyDisposableContainer()))
/*      */       {
/*      */ 
/* 1483 */         targetIsDDContainer = true;
/* 1484 */         String ddTriggerPropName = ((DefensiblyDisposable)targetContainer).getTriggerPropertyName();
/* 1485 */         applicableDDRecordClassNames = ((P8CE_BaseContainerImpl)targetContainer).validateDDTriggerProperty(ddTriggerPropName);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1492 */       ObjectStore jaceRepositoryBase = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 1493 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)repository.getDomain();
/* 1494 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/* 1495 */       RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/* 1496 */       Map<String, BatchItemHandle> jaceRBItemMap = new HashMap();
/* 1497 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(P8CE_RecordImpl.getMandatoryJaceFEs());
/* 1498 */       String additionalRecPropsNeeded = "CutoffInheritedFrom";
/* 1499 */       jacePF.addIncludeProperty(1, null, null, additionalRecPropsNeeded, null);
/* 1500 */       Document jaceRecordBaseDoc = null;
/* 1501 */       for (String entityIdent : entityIdents)
/*      */       {
/* 1503 */         if (Id.isId(entityIdent))
/*      */         {
/* 1505 */           Id jaceRecordId = new Id(entityIdent);
/* 1506 */           jaceRecordBaseDoc = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", jaceRecordId);
/*      */         }
/*      */         else
/*      */         {
/* 1510 */           jaceRecordBaseDoc = Factory.Document.getInstance(jaceRepositoryBase, "RecordInfo", entityIdent);
/*      */         }
/*      */         
/* 1513 */         jaceRBItemMap.put(entityIdent, jaceRB.add(jaceRecordBaseDoc, jacePF));
/*      */       }
/* 1515 */       long startTime = System.currentTimeMillis();
/* 1516 */       jaceRB.retrieveBatch();
/* 1517 */       long endTime = System.currentTimeMillis();
/* 1518 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(entityIdents.size()), null, new Object[0]);
/*      */       
/*      */ 
/*      */ 
/* 1522 */       P8CE_BulkItemResultImpl currentBulkResult = null;
/* 1523 */       IGenerator<Record> generator = P8CE_RecordImpl.getGenerator();
/* 1524 */       List<BulkItemResult> results = new ArrayList(entityIdents.size());
/* 1525 */       Map<String, Record> recordsMap = new HashMap();
/* 1526 */       for (String entityIdent : entityIdents)
/*      */       {
/* 1528 */         currentBulkResult = new P8CE_BulkItemResultImpl(entityIdent);
/* 1529 */         results.add(currentBulkResult);
/* 1530 */         BatchItemHandle currentBIH = (BatchItemHandle)jaceRBItemMap.get(entityIdent);
/* 1531 */         if (currentBIH.hasException())
/*      */         {
/*      */ 
/* 1534 */           currentBulkResult.setException(currentBIH.getException());
/*      */         }
/*      */         else
/*      */         {
/*      */           try
/*      */           {
/* 1540 */             IndependentObject currentJaceRecordBase = currentBIH.getObject();
/* 1541 */             if (P8CE_Util.isEntityInDDReport(currentJaceRecordBase))
/*      */             {
/* 1543 */               String recordIdent = currentJaceRecordBase.getObjectReference().getObjectIdentity();
/* 1544 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MOVE_RECORD_THAT_IS_IN_DD_REPORT, new Object[] { recordIdent });
/*      */             }
/*      */             
/* 1547 */             Record jarmRecord = (Record)generator.create(repository, currentJaceRecordBase);
/* 1548 */             recordsMap.put(entityIdent, jarmRecord);
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/* 1552 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1559 */       boolean forFileOperation = false;
/* 1560 */       Record record = null;
/* 1561 */       for (int i = 0; i < results.size(); i++)
/*      */       {
/* 1563 */         currentBulkResult = (P8CE_BulkItemResultImpl)results.get(i);
/* 1564 */         if (currentBulkResult.wasSuccessful())
/*      */         {
/*      */           try
/*      */           {
/* 1568 */             record = (Record)recordsMap.get(currentBulkResult.getEntityIdent());
/*      */             
/* 1570 */             if (targetIsDDContainer)
/*      */             {
/* 1572 */               if (record.getEntityType() == EntityType.PhysicalRecord)
/*      */               {
/* 1574 */                 throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_PHYSREC_INTO_DD_CONTAINER, new Object[0]);
/*      */               }
/*      */               
/* 1577 */               String recordClassName = record.getClassName();
/* 1578 */               if ((applicableDDRecordClassNames == null) || (!applicableDDRecordClassNames.contains(recordClassName)))
/*      */               {
/*      */ 
/*      */ 
/* 1582 */                 throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_REC_WITHOUT_TRIGPROP_INTO_DD_CONTAINER, new Object[0]);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1589 */             List<Container> existingRecordContainers = ((RALBaseContainer)actualTargetContainer).validateRecordFiling_part2(actualTargetContainer, record, actualSrcContainerIdent, forFileOperation);
/*      */             
/*      */ 
/* 1592 */             boolean recordIsInSourceContainer = false;
/* 1593 */             for (Container existingRecContainer : existingRecordContainers)
/*      */             {
/* 1595 */               if (actualSrcContainerIdent.equalsIgnoreCase(existingRecContainer.getObjectIdentity()))
/*      */               {
/* 1597 */                 recordIsInSourceContainer = true;
/* 1598 */                 break;
/*      */               }
/*      */             }
/* 1601 */             if (!recordIsInSourceContainer)
/*      */             {
/* 1603 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MOVE_RECORD_NOT_IN_SOURCE_CONTAINER, new Object[0]);
/*      */             }
/*      */             
/*      */ 
/* 1607 */             boolean recordIsMultiFiled = existingRecordContainers.size() > 1;
/* 1608 */             Container newSecurityFolder = null;
/* 1609 */             if (!recordIsMultiFiled)
/*      */             {
/*      */ 
/*      */ 
/* 1613 */               newSecurityFolder = actualTargetContainer;
/*      */             }
/*      */             else
/*      */             {
/* 1617 */               if (targetIsDDContainer)
/*      */               {
/* 1619 */                 throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MULTIFILE_INTO_DD_CONTAINER, new Object[0]);
/*      */               }
/*      */               
/* 1622 */               Container existingSecFolder = record.getSecurityFolder();
/* 1623 */               if (existingSecFolder.getObjectIdentity().equalsIgnoreCase(actualSrcContainerIdent))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/* 1628 */                 for (Container existingRecContainer : existingRecordContainers)
/*      */                 {
/* 1630 */                   if (!actualSrcContainerIdent.equalsIgnoreCase(existingRecContainer.getObjectIdentity()))
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1636 */                     newSecurityFolder = existingRecContainer;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 1642 */             if (newSecurityFolder != null) {
/* 1643 */               record.getProperties().putObjectValue("SecurityFolder", newSecurityFolder);
/*      */             }
/*      */             
/* 1646 */             if (record.getAssociatedRecordType() == null)
/*      */             {
/* 1648 */               String cutoffInheritedFrom = record.getProperties().getGuidValue("CutoffInheritedFrom");
/* 1649 */               if ((!recordIsMultiFiled) || (actualSrcContainerIdent.equalsIgnoreCase(cutoffInheritedFrom)))
/*      */               {
/*      */ 
/*      */ 
/* 1653 */                 ((RALBaseRecord)record).resetDispositionData(record.getProperties());
/*      */               }
/*      */             }
/* 1656 */             else if (targetIsDDContainer)
/*      */             {
/* 1658 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_REC_WITH_RECTYPE_INTO_DD_CONTAINER, new Object[0]);
/*      */             }
/*      */             
/* 1661 */             record.getProperties().putStringValue("ReasonForReclassification", reasonForMove);
/*      */             
/*      */ 
/*      */ 
/* 1665 */             Folder sourceJaceFolderBase = (Folder)((P8CE_BaseContainerImpl)sourceContainer).getJaceBaseObject();
/* 1666 */             Folder actualTargetJaceFolderBase = (Folder)((P8CE_BaseContainerImpl)actualTargetContainer).getJaceBaseObject();
/* 1667 */             IndependentlyPersistableObject jaceRecBase = (IndependentlyPersistableObject)((JaceBasable)record).getJaceBaseObject();
/*      */             
/*      */ 
/* 1670 */             UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.NO_REFRESH);
/* 1671 */             PropertyFilter emptyPF = new PropertyFilter();
/*      */             
/*      */ 
/* 1674 */             DefineSecurityParentage dsp = DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE;
/*      */             
/* 1676 */             AutoUniqueName aun = AutoUniqueName.AUTO_UNIQUE;
/* 1677 */             String containmentName = null;
/* 1678 */             ReferentialContainmentRelationship fileRCR = actualTargetJaceFolderBase.file(jaceRecBase, aun, containmentName, dsp);
/*      */             
/* 1680 */             jaceUB.add(fileRCR, emptyPF);
/*      */             
/* 1682 */             ReferentialContainmentRelationship unfileRCR = sourceJaceFolderBase.unfile(jaceRecBase);
/*      */             
/* 1684 */             jaceUB.add(unfileRCR, emptyPF);
/*      */             
/* 1686 */             P8CE_AuditServices.attachRelocateAuditEvent(record, sourceJaceFolderBase, actualTargetJaceFolderBase, reasonForMove, AuditStatus.Success, false);
/*      */             
/*      */ 
/* 1689 */             jaceUB.add(jaceRecBase, emptyPF);
/*      */             
/*      */ 
/* 1692 */             startTime = System.currentTimeMillis();
/* 1693 */             jaceUB.updateBatch();
/* 1694 */             endTime = System.currentTimeMillis();
/* 1695 */             Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */             
/* 1697 */             currentBulkResult.setSuccess(true);
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/* 1701 */             currentBulkResult.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1707 */       if (results != null)
/*      */       {
/* 1709 */         for (BulkItemResult bri : results)
/*      */         {
/* 1711 */           if (!bri.wasSuccessful())
/*      */           {
/* 1713 */             record = (Record)recordsMap.get(bri.getEntityIdent());
/* 1714 */             if (record != null)
/*      */             {
/* 1716 */               String failMsg = bri.getException().getLocalizedMessage();
/* 1717 */               P8CE_AuditServices.attachRelocateAuditEvent(record, null, null, failMsg, AuditStatus.Failure, true);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1723 */       Tracer.traceMethodExit(new Object[] { results });
/* 1724 */       return results;
/*      */     }
/*      */     finally
/*      */     {
/* 1728 */       if (establishedSubject) {
/* 1729 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   public List<BulkItemResult> copyRecords(FilePlanRepository repository, List<String> entityIdents, String destinationContainerIdent, String namePrefix)
/*      */   {
/* 1745 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, destinationContainerIdent, namePrefix });
/* 1746 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1749 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 1752 */       List<BulkItemResult> bulkResultsList = new ArrayList(entityIdents.size());
/*      */       
/* 1754 */       RecordContainer destContainer = (RecordContainer)RMFactory.Container.fetchInstance(repository, EntityType.Container, destinationContainerIdent, RMPropertyFilter.MinimumPropertySet);
/*      */       
/*      */ 
/* 1757 */       P8CE_BulkItemResultImpl currentItemResult = null;
/* 1758 */       Map<String, Record> recordsToCopy = new HashMap();
/* 1759 */       Record record = null;
/* 1760 */       for (String entityIdent : entityIdents)
/*      */       {
/* 1762 */         currentItemResult = new P8CE_BulkItemResultImpl(entityIdent);
/* 1763 */         bulkResultsList.add(currentItemResult);
/*      */         try
/*      */         {
/* 1766 */           record = RMFactory.Record.fetchInstance(repository, entityIdent, RMPropertyFilter.MinimumPropertySet);
/* 1767 */           recordsToCopy.put(entityIdent, record);
/*      */         }
/*      */         catch (Exception ex)
/*      */         {
/* 1771 */           currentItemResult.setException(ex);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1776 */       RMProperties overrideProps = null;
/* 1777 */       String existingDocTitle = null;
/* 1778 */       String copyDocTitle = null;
/* 1779 */       for (BulkItemResult itemResult : bulkResultsList)
/*      */       {
/* 1781 */         if (itemResult.wasSuccessful())
/*      */         {
/* 1783 */           Record recordToCopy = (Record)recordsToCopy.get(itemResult.getEntityIdent());
/* 1784 */           Record newlyCopiedRecord = null;
/*      */           try
/*      */           {
/* 1787 */             if (namePrefix != null)
/*      */             {
/* 1789 */               overrideProps = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/* 1790 */               existingDocTitle = recordToCopy.getProperties().getStringValue("DocumentTitle");
/* 1791 */               copyDocTitle = namePrefix + existingDocTitle;
/* 1792 */               overrideProps.putStringValue("DocumentTitle", copyDocTitle);
/*      */             }
/*      */             else
/*      */             {
/* 1796 */               overrideProps = null;
/*      */             }
/*      */             
/* 1799 */             newlyCopiedRecord = recordToCopy.copy(destContainer, overrideProps);
/* 1800 */             ((P8CE_BulkItemResultImpl)itemResult).setResultData(newlyCopiedRecord);
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/* 1804 */             ((P8CE_BulkItemResultImpl)itemResult).setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1809 */       Tracer.traceMethodExit(new Object[] { bulkResultsList });
/* 1810 */       return bulkResultsList;
/*      */     }
/*      */     finally
/*      */     {
/* 1814 */       if (establishedSubject) {
/* 1815 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<BulkItemResult> undeclareRecords(FilePlanRepository repository, List<String> entityIdents)
/*      */   {
/* 1829 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents });
/* 1830 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1833 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 1836 */       List<BulkItemResult> bulkResultsList = new ArrayList(entityIdents.size());
/*      */       
/*      */ 
/* 1839 */       P8CE_BulkItemResultImpl currentItemResult = null;
/* 1840 */       Map<String, Record> recordsToUndeclare = new HashMap();
/* 1841 */       Record record = null;
/* 1842 */       for (String entityIdent : entityIdents)
/*      */       {
/* 1844 */         currentItemResult = new P8CE_BulkItemResultImpl(entityIdent);
/* 1845 */         bulkResultsList.add(currentItemResult);
/*      */         try
/*      */         {
/* 1848 */           record = RMFactory.Record.getInstance(repository, entityIdent, EntityType.Record);
/* 1849 */           recordsToUndeclare.put(entityIdent, record);
/*      */         }
/*      */         catch (Exception ex)
/*      */         {
/* 1853 */           currentItemResult.setException(ex);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1858 */       for (BulkItemResult itemResult : bulkResultsList)
/*      */       {
/* 1860 */         if (itemResult.wasSuccessful())
/*      */         {
/* 1862 */           Record recordToUndeclare = (Record)recordsToUndeclare.get(itemResult.getEntityIdent());
/*      */           try
/*      */           {
/* 1865 */             recordToUndeclare.undeclare();
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/* 1869 */             ((P8CE_BulkItemResultImpl)itemResult).setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1874 */       Tracer.traceMethodExit(new Object[] { bulkResultsList });
/* 1875 */       return bulkResultsList;
/*      */     }
/*      */     finally
/*      */     {
/* 1879 */       if (establishedSubject) {
/* 1880 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   public List<BulkItemResult> initiateDisposition(FilePlanRepository repository, List<BulkOperation.EntityDescription> entityDescriptions, Object vwSession)
/*      */   {
/* 1897 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescriptions, vwSession });
/*      */     
/* 1899 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1902 */       establishedSubject = P8CE_Util.associateSubject();
/* 1903 */       Map<String, BulkItemResult> bulkItemResultsMap = new LinkedHashMap();
/* 1904 */       Date now = new Date();
/* 1905 */       SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/* 1906 */       dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 1907 */       String nowStr = dateFormatter.format(now);
/* 1908 */       RMPropertyFilter jarmPFForSearches = new RMPropertyFilter();
/* 1909 */       jarmPFForSearches.addIncludeType(null, null, null, RMFilteredPropertyType.AnyNonObject, null);
/*      */       
/*      */ 
/* 1912 */       EntityType entityType = null;
/* 1913 */       String entityIdent = null;
/* 1914 */       BaseEntity entity = null;
/* 1915 */       RMProperties entityProps = null;
/* 1916 */       P8CE_BulkItemResultImpl currentItemResult = null;
/* 1917 */       List<BaseEntity> jarmEntitiesToProcess = new ArrayList(entityDescriptions.size());
/* 1918 */       for (BulkOperation.EntityDescription entityDesc : entityDescriptions)
/*      */       {
/* 1920 */         entityType = entityDesc.getEntityType();
/* 1921 */         entityIdent = entityDesc.getEntityIdent();
/* 1922 */         currentItemResult = new P8CE_BulkItemResultImpl(entityIdent);
/*      */         try
/*      */         {
/* 1925 */           if (isRecordContainerEntityType(entityType))
/*      */           {
/* 1927 */             entity = RMFactory.Container.fetchInstance(repository, entityType, entityIdent, null);
/* 1928 */             jarmEntitiesToProcess.add(entity);
/*      */           }
/* 1930 */           else if (isRecordEntityType(entityType))
/*      */           {
/* 1932 */             entity = RMFactory.Record.fetchInstance(repository, entityIdent, null);
/* 1933 */             jarmEntitiesToProcess.add(entity);
/*      */           }
/*      */           else
/*      */           {
/* 1937 */             Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATE_DISP_BAD_ENTITY_TYPE, new Object[] { entityType, entityIdent });
/* 1938 */             currentItemResult.setException(ex);
/*      */           }
/*      */         }
/*      */         catch (Exception ex)
/*      */         {
/* 1943 */           currentItemResult.setException(ex);
/*      */         }
/*      */         
/* 1946 */         bulkItemResultsMap.put(entityIdent, currentItemResult);
/*      */       }
/*      */       
/*      */ 
/* 1950 */       List<BaseEntity> globalEntitiesForDisposition = new ArrayList();
/*      */       
/*      */ 
/* 1953 */       for (BaseEntity currentEntity : jarmEntitiesToProcess)
/*      */       {
/* 1955 */         entityIdent = currentEntity.getObjectIdentity();
/*      */         
/*      */ 
/*      */         try
/*      */         {
/* 1960 */           List<BaseEntity> localEntitiesForDisposition = new ArrayList();
/*      */           
/* 1962 */           entityType = currentEntity.getEntityType();
/* 1963 */           currentItemResult = (P8CE_BulkItemResultImpl)bulkItemResultsMap.get(entityIdent);
/* 1964 */           entityProps = currentEntity.getProperties();
/*      */           
/*      */ 
/* 1967 */           boolean ckParentHier = true;
/* 1968 */           if (((Holdable)currentEntity).isOnHold(ckParentHier))
/*      */           {
/*      */ 
/* 1971 */             Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_DUE_TO_HOLD, new Object[] { entityIdent });
/* 1972 */             currentItemResult.setException(ex);
/*      */           }
/*      */           else
/*      */           {
/* 1976 */             Date cutoffDate = entityProps.getDateTimeValue("CutoffDate");
/* 1977 */             Date curPhExeDate = entityProps.getDateTimeValue("CurrentPhaseExecutionDate");
/* 1978 */             Integer curPhExeStatus = entityProps.getIntegerValue("CurrentPhaseExecutionStatus");
/* 1979 */             Integer curItemActionValidOption = getPhaseActionValidationOption(entityProps);
/*      */             
/*      */ 
/*      */ 
/* 1983 */             ObjectStore jaceFPOS = P8CE_Convert.fromJARM(entity.getRepository());
/* 1984 */             if ((cutoffDate != null) && (curPhExeDate != null) && (curPhExeDate.before(now)) && (curPhExeStatus != null) && (curPhExeStatus.intValue() == RMWorkflowStatus.NotStarted.getIntValue()))
/*      */             {
/*      */ 
/*      */ 
/* 1988 */               if (isRecordContainerEntityType(entityType))
/*      */               {
/*      */ 
/* 1991 */                 RMRuntimeException rrex = ckInitDispHoldEligibility(jaceFPOS, (RecordContainer)currentEntity, curItemActionValidOption);
/* 1992 */                 if (rrex == null)
/*      */                 {
/* 1994 */                   localEntitiesForDisposition.add(currentEntity);
/*      */                 }
/*      */                 else
/*      */                 {
/* 1998 */                   currentItemResult.setException(rrex);
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 2003 */                 localEntitiesForDisposition.add(currentEntity);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2009 */             if (isRecordContainerEntityType(entityType))
/*      */             {
/*      */ 
/*      */ 
/* 2013 */               String sqlStmt = String.format(SqlForImmediateRecsReadyForInitDisp, new Object[] { entityIdent, nowStr });
/* 2014 */               RMSearch jarmSearch = new RMSearch(repository);
/* 2015 */               PageableSet<Record> recResultSet = jarmSearch.fetchObjects(sqlStmt, EntityType.Record, null, jarmPFForSearches, Boolean.TRUE);
/* 2016 */               RMPageIterator<Record> piRecords = recResultSet.pageIterator();
/* 2017 */               while (piRecords.nextPage())
/*      */               {
/* 2019 */                 List<Record> currentPage = piRecords.getCurrentPage();
/* 2020 */                 localEntitiesForDisposition.addAll(currentPage);
/*      */               }
/*      */               
/*      */ 
/* 2024 */               Integer actionValidation = null;
/* 2025 */               sqlStmt = String.format(SqlForSubContainersReadyForInitDisp, new Object[] { entityIdent, nowStr });
/* 2026 */               jarmSearch = new RMSearch(repository);
/* 2027 */               PageableSet<Container> containerResultSet = jarmSearch.fetchObjects(sqlStmt, EntityType.Container, null, jarmPFForSearches, Boolean.TRUE);
/* 2028 */               RMPageIterator<Container> piContainers = containerResultSet.pageIterator();
/* 2029 */               while (piContainers.nextPage())
/*      */               {
/* 2031 */                 List<Container> currentPage = piContainers.getCurrentPage();
/* 2032 */                 for (Container container : currentPage)
/*      */                 {
/* 2034 */                   Integer localActionOption = getPhaseActionValidationOption(container.getProperties());
/* 2035 */                   actionValidation = localActionOption != null ? localActionOption : curItemActionValidOption;
/* 2036 */                   RMRuntimeException rrex = ckInitDispHoldEligibility(jaceFPOS, (RecordContainer)container, actionValidation);
/* 2037 */                   if (rrex == null)
/*      */                   {
/* 2039 */                     localEntitiesForDisposition.add(container);
/*      */                   }
/*      */                   else
/*      */                   {
/* 2043 */                     String subContainerIdent = container.getObjectIdentity();
/*      */                     
/* 2045 */                     P8CE_BulkItemResultImpl subContainerResult = (P8CE_BulkItemResultImpl)bulkItemResultsMap.get(subContainerIdent);
/*      */                     
/* 2047 */                     if (subContainerResult == null)
/*      */                     {
/* 2049 */                       subContainerResult = new P8CE_BulkItemResultImpl(subContainerIdent);
/* 2050 */                       bulkItemResultsMap.put(subContainerIdent, subContainerResult);
/*      */                     }
/* 2052 */                     subContainerResult.setException(rrex);
/*      */                   }
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 2059 */               sqlStmt = String.format(SqlForSubContainersNotOnHold, new Object[] { entityIdent });
/* 2060 */               jarmSearch = new RMSearch(repository);
/* 2061 */               containerResultSet = jarmSearch.fetchObjects(sqlStmt, EntityType.Container, null, jarmPFForSearches, Boolean.TRUE);
/* 2062 */               piContainers = containerResultSet.pageIterator();
/* 2063 */               while (piContainers.nextPage())
/*      */               {
/* 2065 */                 List<Container> currentPage = piContainers.getCurrentPage();
/* 2066 */                 for (Container container : currentPage)
/*      */                 {
/* 2068 */                   RecordContainer recContainer = (RecordContainer)container;
/* 2069 */                   if ((!recContainer.requiresChildVolume()) && (
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/* 2074 */                     (EntityType.RecordVolume != recContainer.getEntityType()) || (!recContainer.isAnyParentOnHold())))
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2081 */                     sqlStmt = String.format(SqlForImmediateRecsReadyForInitDisp, new Object[] { recContainer.getObjectIdentity(), nowStr });
/* 2082 */                     jarmSearch = new RMSearch(repository);
/* 2083 */                     recResultSet = jarmSearch.fetchObjects(sqlStmt, EntityType.Record, null, jarmPFForSearches, Boolean.TRUE);
/* 2084 */                     piRecords = recResultSet.pageIterator();
/* 2085 */                     while (piRecords.nextPage())
/*      */                     {
/* 2087 */                       List<Record> currentRecsPage = piRecords.getCurrentPage();
/* 2088 */                       localEntitiesForDisposition.addAll(currentRecsPage);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2097 */             for (int i = localEntitiesForDisposition.size() - 1; i >= 0; i--)
/*      */             {
/* 2099 */               BaseEntity candidate = (BaseEntity)localEntitiesForDisposition.get(i);
/* 2100 */               ((Dispositionable)candidate).updatePhaseDataOnEntity();
/* 2101 */               candidate.refresh((RMPropertyFilter)null);
/*      */               
/* 2103 */               Date newCurPhaseExeDate = candidate.getProperties().getDateTimeValue("CurrentPhaseExecutionDate");
/* 2104 */               Integer newCurActionType = candidate.getProperties().getIntegerValue("CurrentActionType");
/* 2105 */               String candidateIdent = candidate.getObjectIdentity();
/* 2106 */               RMRuntimeException ineligibleReason = null;
/* 2107 */               if ((newCurPhaseExeDate == null) || (newCurPhaseExeDate.after(now)))
/*      */               {
/* 2109 */                 ineligibleReason = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_FOR_ENTITY_DUE_TO_CPED, new Object[] { candidateIdent, newCurPhaseExeDate });
/*      */               }
/* 2111 */               else if ((newCurActionType != null) && (newCurActionType.intValue() == DispositionActionType.AutoDestroy.getIntValue()))
/*      */               {
/* 2113 */                 ineligibleReason = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_FOR_ENTITY_AUTODESTROY_PHASE, new Object[] { candidateIdent });
/*      */               }
/*      */               
/* 2116 */               if (ineligibleReason != null)
/*      */               {
/* 2118 */                 P8CE_BulkItemResultImpl itemResult = (P8CE_BulkItemResultImpl)bulkItemResultsMap.get(candidateIdent);
/*      */                 
/* 2120 */                 if (itemResult == null)
/*      */                 {
/* 2122 */                   itemResult = new P8CE_BulkItemResultImpl(candidateIdent);
/* 2123 */                   bulkItemResultsMap.put(candidateIdent, itemResult);
/*      */                 }
/* 2125 */                 itemResult.setException(ineligibleReason);
/*      */                 
/* 2127 */                 localEntitiesForDisposition.remove(i);
/*      */               }
/*      */             }
/*      */             
/* 2131 */             if (localEntitiesForDisposition.size() > 0)
/*      */             {
/*      */ 
/* 2134 */               for (BaseEntity localEntity : localEntitiesForDisposition)
/*      */               {
/* 2136 */                 String localIdent = localEntity.getObjectIdentity();
/* 2137 */                 if (!bulkItemResultsMap.containsKey(localIdent))
/*      */                 {
/* 2139 */                   bulkItemResultsMap.put(localIdent, new P8CE_BulkItemResultImpl(localIdent));
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 2144 */               globalEntitiesForDisposition.addAll(localEntitiesForDisposition);
/*      */ 
/*      */ 
/*      */             }
/* 2148 */             else if (currentItemResult.getException() == null)
/*      */             {
/* 2150 */               Exception ex = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_FOR_ENTITY, new Object[] { entityIdent });
/* 2151 */               currentItemResult.setException(ex);
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/*      */         catch (Exception ex)
/*      */         {
/* 2159 */           P8CE_BulkItemResultImpl bir = (P8CE_BulkItemResultImpl)bulkItemResultsMap.get(entityIdent);
/* 2160 */           if ((bir != null) && (bir.wasSuccessful())) {
/* 2161 */             bir.setException(ex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2169 */       int i = globalEntitiesForDisposition.size(); for (;;) { i--; if (i < 0)
/*      */           break;
/* 2171 */         String ident1 = ((BaseEntity)globalEntitiesForDisposition.get(i)).getObjectIdentity();
/*      */         
/* 2173 */         int j = i; for (;;) { j--; if (j < 0)
/*      */             break;
/* 2175 */           String ident2 = ((BaseEntity)globalEntitiesForDisposition.get(j)).getObjectIdentity();
/* 2176 */           if (ident1.equalsIgnoreCase(ident2))
/*      */           {
/* 2178 */             globalEntitiesForDisposition.remove(j);
/* 2179 */             i--;
/*      */           }
/*      */         }
/*      */       }
/*      */       Exception cause;
/* 2184 */       if (globalEntitiesForDisposition.size() > 0)
/*      */       {
/* 2186 */         List<BaseEntity> entitiesForWorkflow = new ArrayList();
/* 2187 */         for (BaseEntity initDispCandidate : globalEntitiesForDisposition)
/*      */         {
/* 2189 */           RMProperties candidateProps = initDispCandidate.getProperties();
/* 2190 */           Object rawCurPhAction = candidateProps.getObjectValue("CurrentPhaseAction");
/* 2191 */           if (rawCurPhAction != null)
/*      */           {
/* 2193 */             entitiesForWorkflow.add(initDispCandidate);
/*      */           }
/*      */           else
/*      */           {
/* 2197 */             Date curPhExeDate = candidateProps.getDateTimeValue("CurrentPhaseExecutionDate");
/* 2198 */             candidateProps.putIntegerValue("CurrentPhaseExecutionStatus", Integer.valueOf(RMWorkflowStatus.Started.getIntValue()));
/* 2199 */             ((RALBaseEntity)initDispCandidate).internalSave(RMRefreshMode.Refresh);
/* 2200 */             ((Dispositionable)initDispCandidate).completePhaseExecution(curPhExeDate, true);
/*      */           }
/*      */         }
/*      */         
/* 2204 */         if (entitiesForWorkflow.size() > 0)
/*      */         {
/* 2206 */           if (vwSession != null)
/*      */           {
/* 2208 */             if (RALPESupport.isPESupportAvailable())
/*      */             {
/* 2210 */               RALWorkflowSupport.launchWorkflows(repository, entitiesForWorkflow, "Reviewer", 1003, vwSession);
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/* 2217 */               cause = RALPESupport.getSetupException();
/* 2218 */               throw RMRuntimeException.createRMRuntimeException(cause, RMErrorCode.RAL_PE_API_NOT_AVAILABLE, new Object[0]);
/*      */             }
/*      */             
/*      */           }
/*      */           else {
/* 2223 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_INVALID_VWSESSION, new Object[] { "<null>" });
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2228 */       List<BulkItemResult> bulkItemResultList = new ArrayList(bulkItemResultsMap.values());
/* 2229 */       Tracer.traceMethodExit(new Object[] { bulkItemResultList });
/* 2230 */       return bulkItemResultList;
/*      */     }
/*      */     catch (RMRuntimeException rre)
/*      */     {
/* 2234 */       throw rre;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2238 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_CANNOT_INITIATEDISP_UNEXPECTED_ERROR, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2242 */       if (establishedSubject) {
/* 2243 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private RMRuntimeException ckInitDispHoldEligibility(ObjectStore jaceFPOS, RecordContainer candidateContainer, Integer actionValidationOption)
/*      */   {
/* 2253 */     Tracer.traceMethodEntry(new Object[] { jaceFPOS, candidateContainer });
/* 2254 */     RMRuntimeException result = null;
/* 2255 */     EntityType entityType = candidateContainer.getEntityType();
/* 2256 */     String entityIdent = candidateContainer.getObjectIdentity();
/*      */     
/*      */ 
/* 2259 */     boolean ckParentHier = true;
/* 2260 */     if (candidateContainer.isOnHold(ckParentHier))
/*      */     {
/* 2262 */       result = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_DUE_TO_HOLD, new Object[] { entityIdent });
/*      */ 
/*      */     }
/* 2265 */     else if (((entityType == EntityType.RecordCategory) || (entityType == EntityType.PhysicalRecordFolder) || (entityType == EntityType.HybridRecordFolder)) && (hasAnyPhysicalSubContainerOnHold(entityIdent, jaceFPOS)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2270 */       result = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_DUE_TO_SUBPHYSCONTAINER_ONHOLD, new Object[] { entityIdent });
/*      */ 
/*      */     }
/* 2273 */     else if (queryForAny(SqlForAnyPhysicalRecordsOnHold, entityIdent, jaceFPOS))
/*      */     {
/* 2275 */       result = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_DUE_TO_PHYSREC_ONHOLD, new Object[] { entityIdent });
/*      */ 
/*      */     }
/* 2278 */     else if (physRecExistsInAnyOnHoldSubContainer(entityIdent, jaceFPOS))
/*      */     {
/* 2280 */       result = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_DUE_TO_PHYSREC_IN_ONHOLD_CONTAINER, new Object[] { entityIdent });
/*      */ 
/*      */ 
/*      */     }
/* 2284 */     else if (candidateContainer.isAnyChildOnHold())
/*      */     {
/*      */ 
/*      */ 
/* 2288 */       if ((!ProcessNonHeldChildren.equals(actionValidationOption)) || (hasAnyChildPhysContainer(entityIdent, jaceFPOS)) || (hasAnyChildPhysRecord(entityIdent, jaceFPOS)))
/*      */       {
/*      */ 
/*      */ 
/* 2292 */         result = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_INITIATEDISP_DUE_TO_CHILD_HOLD, new Object[] { entityIdent });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2297 */     Tracer.traceMethodExit(new Object[] { result });
/* 2298 */     return result;
/*      */   }
/*      */   
/*      */   private Integer getPhaseActionValidationOption(RMProperties entityProps)
/*      */   {
/* 2303 */     Tracer.traceMethodEntry(new Object[] { entityProps });
/* 2304 */     Integer actionValidationOption = null;
/* 2305 */     if (entityProps.isPropertyPresent("CurrentPhaseAction"))
/*      */     {
/* 2307 */       DispositionAction curPhAction = (DispositionAction)entityProps.getObjectValue("CurrentPhaseAction");
/* 2308 */       if (curPhAction != null) {
/* 2309 */         actionValidationOption = curPhAction.getProperties().getIntegerValue("RMValidationOptions");
/*      */       }
/*      */     }
/* 2312 */     Tracer.traceMethodExit(new Object[] { actionValidationOption });
/* 2313 */     return actionValidationOption;
/*      */   }
/*      */   
/*      */   private boolean isRecordContainerEntityType(EntityType entityType)
/*      */   {
/* 2318 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 2319 */     boolean result = (entityType == EntityType.RecordCategory) || (entityType == EntityType.RecordFolder) || (entityType == EntityType.ElectronicRecordFolder) || (entityType == EntityType.PhysicalContainer) || (entityType == EntityType.HybridRecordFolder) || (entityType == EntityType.PhysicalRecordFolder) || (entityType == EntityType.RecordVolume);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2327 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2328 */     return result;
/*      */   }
/*      */   
/*      */   private boolean isRecordEntityType(EntityType entityType)
/*      */   {
/* 2333 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 2334 */     boolean result = (entityType == EntityType.Record) || (entityType == EntityType.ElectronicRecord) || (entityType == EntityType.EmailRecord) || (entityType == EntityType.PhysicalRecord) || (entityType == EntityType.PDFRecord);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2340 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2341 */     return result;
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
/*      */   private boolean queryForAny(String sqlStr, String entityIdent, ObjectStore jaceObjStore)
/*      */   {
/* 2355 */     Tracer.traceMethodEntry(new Object[] { sqlStr, entityIdent, jaceObjStore });
/* 2356 */     String sqlStatement = String.format(sqlStr, new Object[] { entityIdent });
/* 2357 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 2358 */     SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/* 2359 */     Integer pageSize = null;
/* 2360 */     Boolean continuable = Boolean.FALSE;
/*      */     
/* 2362 */     RepositoryRowSet resultSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, null, continuable);
/* 2363 */     boolean result = !resultSet.isEmpty();
/* 2364 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2365 */     return result;
/*      */   }
/*      */   
/*      */   private boolean hasAnyPhysicalSubContainerOnHold(String entityIdent, ObjectStore jaceFPOS)
/*      */   {
/* 2370 */     Tracer.traceMethodEntry(new Object[] { entityIdent, jaceFPOS });
/*      */     
/* 2372 */     boolean foundOne = queryForAny(SqlForAnyPhysicalSubContainersOnHold, entityIdent, jaceFPOS);
/* 2373 */     if (!foundOne)
/*      */     {
/* 2375 */       foundOne = queryForAny(SqlForPhysicalVolumeOnHold, entityIdent, jaceFPOS);
/*      */     }
/*      */     
/* 2378 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(foundOne) });
/* 2379 */     return foundOne;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean physRecExistsInAnyOnHoldSubContainer(String entityIdent, ObjectStore jaceFPOS)
/*      */   {
/* 2385 */     Tracer.traceMethodEntry(new Object[] { entityIdent, jaceFPOS });
/* 2386 */     boolean foundOne = false;
/* 2387 */     String sqlStatement = String.format(SqlForOnHoldSubContainerIDs, new Object[] { entityIdent });
/* 2388 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 2389 */     SearchScope jaceSearchScope = new SearchScope(jaceFPOS);
/* 2390 */     Integer pageSize = null;
/* 2391 */     Boolean continuable = Boolean.FALSE;
/* 2392 */     RepositoryRowSet resultSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, null, continuable);
/* 2393 */     for (Iterator it = resultSet.iterator(); it.hasNext();)
/*      */     {
/*      */ 
/* 2396 */       RepositoryRow row = (RepositoryRow)it.next();
/* 2397 */       String subcontainerIdent = row.getProperties().getIdValue("Id").toString();
/* 2398 */       if (queryForAny(SqlForAnyPhysicalRecords, subcontainerIdent, jaceFPOS))
/*      */       {
/* 2400 */         foundOne = true;
/* 2401 */         break;
/*      */       }
/*      */     }
/*      */     
/* 2405 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(foundOne) });
/* 2406 */     return foundOne;
/*      */   }
/*      */   
/*      */   private boolean hasAnyChildPhysContainer(String entityIdent, ObjectStore jaceFPOS)
/*      */   {
/* 2411 */     Tracer.traceMethodEntry(new Object[] { entityIdent, jaceFPOS });
/*      */     
/* 2413 */     boolean foundOne = queryForAny(SqlForAnyPhysicalSubContainers, entityIdent, jaceFPOS);
/*      */     
/* 2415 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(foundOne) });
/* 2416 */     return foundOne;
/*      */   }
/*      */   
/*      */   private boolean hasAnyChildPhysRecord(String entityIdent, ObjectStore jaceFPOS)
/*      */   {
/* 2421 */     Tracer.traceMethodEntry(new Object[] { entityIdent, jaceFPOS });
/*      */     
/* 2423 */     boolean foundOne = queryForAny(SqlForAnyPhysicalRecords, entityIdent, jaceFPOS);
/*      */     
/* 2425 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(foundOne) });
/* 2426 */     return foundOne;
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RALBulkOperationImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */