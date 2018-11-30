/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.ClassDescriptionSet;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.Integer32List;
/*      */ import com.filenet.api.collection.PageIterator;
/*      */ import com.filenet.api.collection.PropertyDescriptionList;
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.constants.AutoClassify;
/*      */ import com.filenet.api.constants.AutoUniqueName;
/*      */ import com.filenet.api.constants.Cardinality;
/*      */ import com.filenet.api.constants.CheckinType;
/*      */ import com.filenet.api.constants.DefineSecurityParentage;
/*      */ import com.filenet.api.constants.RefreshMode;
/*      */ import com.filenet.api.constants.SpecialPrincipal;
/*      */ import com.filenet.api.constants.TypeID;
/*      */ import com.filenet.api.core.BatchItemHandle;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.EngineObject;
/*      */ import com.filenet.api.core.Factory.Document;
/*      */ import com.filenet.api.core.Factory.Folder;
/*      */ import com.filenet.api.core.Factory.Integer32List;
/*      */ import com.filenet.api.core.Factory.MetadataCache;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.core.IndependentlyPersistableObject;
/*      */ import com.filenet.api.core.Link;
/*      */ import com.filenet.api.core.ObjectReference;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*      */ import com.filenet.api.core.RetrievingBatch;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.exception.EngineRuntimeException;
/*      */ import com.filenet.api.meta.ClassDescription;
/*      */ import com.filenet.api.meta.MetadataCache;
/*      */ import com.filenet.api.meta.PropertyDescription;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.Property;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.RepositoryRow;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.security.User;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.filenet.apiimpl.property.PropertiesImpl;
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.collection.RMPageIterator;
/*      */ import com.ibm.jarm.api.constants.AuditStatus;
/*      */ import com.ibm.jarm.api.constants.ContentXMLExport;
/*      */ import com.ibm.jarm.api.constants.DeleteMode;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.DomainType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMGranteeType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*      */ import com.ibm.jarm.api.constants.SchedulePropagation;
/*      */ import com.ibm.jarm.api.core.AuditEvent;
/*      */ import com.ibm.jarm.api.core.AuditableEntity;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.BulkItemResult;
/*      */ import com.ibm.jarm.api.core.BulkOperation;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.ContentItem;
/*      */ import com.ibm.jarm.api.core.ContentRepository;
/*      */ import com.ibm.jarm.api.core.DefensiblyDisposable;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.ExternalExport;
/*      */ import com.ibm.jarm.api.core.FilePlan;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.Hold;
/*      */ import com.ibm.jarm.api.core.Location;
/*      */ import com.ibm.jarm.api.core.NamingPatternSequence;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.RMFactory.RMProperties;
/*      */ import com.ibm.jarm.api.core.RMFactory.Record;
/*      */ import com.ibm.jarm.api.core.Record;
/*      */ import com.ibm.jarm.api.core.RecordCategory;
/*      */ import com.ibm.jarm.api.core.RecordContainer;
/*      */ import com.ibm.jarm.api.core.RecordFolder;
/*      */ import com.ibm.jarm.api.core.RecordVolume;
/*      */ import com.ibm.jarm.api.core.RecordVolumeContainer;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.core.SystemConfiguration;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*      */ import com.ibm.jarm.api.meta.RMPropertyDescriptionInteger;
/*      */ import com.ibm.jarm.api.meta.RMPropertyDescriptionString;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMProperty;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.util.JarmLogger;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.NamingUtils;
/*      */ import com.ibm.jarm.api.util.RMLString;
/*      */ import com.ibm.jarm.api.util.RMLogCode;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.common.RALBaseContainer;
/*      */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*      */ import com.ibm.jarm.ral.common.RALBaseRecord;
/*      */ import com.ibm.jarm.ral.common.RALBaseRepository;
/*      */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*      */ import com.ibm.jarm.ral.common.RAL_XMLSupport;
/*      */ import java.io.File;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class P8CE_BaseContainerImpl
/*      */   extends RALBaseContainer
/*      */   implements Container, JaceBasable, AuditableEntity
/*      */ {
/*  136 */   private static final JarmLogger Logger = JarmLogger.getJarmLogger(P8CE_BaseContainerImpl.class.getName());
/*  137 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*      */   
/*      */   private static final int RecordDeletionPageSize = 25;
/*      */   
/*  141 */   private static final IGenerator<Container> BaseContainerGenerator = new Generator();
/*      */   
/*      */ 
/*      */   private static String[] MandatoryPropertyNames;
/*      */   
/*      */ 
/*      */   protected static final RMPropertyFilter DispositionJarmPF;
/*      */   
/*  149 */   static final String[] DispositionRelatedPropNames = { "CurrentPhaseID", "CurrentPhaseExecutionDate", "CurrentActionType", "CurrentPhaseExecutionStatus", "CutoffDate", "CurrentPhaseAction", "CutoffInheritedFrom", "RecalculatePhaseRetention", "CurrentPhaseExportFormat", "CurrentPhaseExportDestination", "LastSweepDate", "CurrentPhaseDecisionDate", "CurrentPhaseReviewComments" };
/*      */   private static List<FilterElement> MandatoryJaceFEs;
/*      */   private static final Set<String> Special1stVolumePropNames;
/*      */   private static final String SqlForVerifySubContainerExistence;
/*      */   private static final String SqlToCheckForMultiFiledChildRecords = "SELECT TOP 1 rcr1.Id, rcr1.Head FROM DynamicReferentialContainmentRelationship rcr1 WITH EXCLUDESUBCLASSES WHERE EXISTS(SELECT * FROM DynamicReferentialContainmentRelationship rcr2 WITH EXCLUDESUBCLASSES WHERE rcr2.Head = rcr1.Head AND rcr2.Id <> rcr1.Id)   AND rcr1.Tail = OBJECT('%s') ";
/*      */   private static final String SqlForDDVerificationOfChildRecords = "SELECT r.Id, r.RMEntityType, r.AssociatedRecordType, r.ClassDescription, r.CurrentPhaseExecutionStatus FROM RecordInfo r INNER JOIN ReferentialContainmentRelationship rcr ON r.This = rcr.Head WHERE (rcr.Tail = OBJECT('%s'))   AND (r.IsDeleted = FALSE) ";
/*      */   private static PropertyFilter JacePF_ForPSSAtlasInfo;
/*      */   private P8CE_FilePlanImpl myFilePlan;
/*      */   protected Folder jaceFolder;
/*      */   protected boolean isAddDefensDisposalContainerInProgress;
/*      */   
/*      */   static {
/*  161 */     DispositionJarmPF = new RMPropertyFilter();
/*  162 */     String dispositionRelatedNames = P8CE_Util.createSpaceSeparatedString(DispositionRelatedPropNames);
/*  163 */     DispositionJarmPF.addIncludeProperty(Integer.valueOf(0), null, Boolean.FALSE, dispositionRelatedNames, null);
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
/*  177 */     Special1stVolumePropNames = new HashSet();
/*  178 */     Special1stVolumePropNames.add("IsVitalRecord");
/*  179 */     Special1stVolumePropNames.add("VitalRecordDeclareDate");
/*  180 */     Special1stVolumePropNames.add("VitalRecordDisposalTrigger");
/*  181 */     Special1stVolumePropNames.add("VitalRecordReviewAction");
/*  182 */     Special1stVolumePropNames.add("VitalWorkflowStatus");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  188 */     StringBuilder sb = new StringBuilder();
/*  189 */     sb.append("SELECT rmf.").append("Id");
/*  190 */     sb.append(" FROM ").append("RMFolder").append(" rmf");
/*  191 */     sb.append(" WHERE rmf.This INSUBFOLDER '%s'");
/*  192 */     sb.append("  AND rmf.").append("Id").append(" = '%s' ");
/*      */     
/*  194 */     SqlForVerifySubContainerExistence = sb.toString();
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
/*      */   static synchronized String[] getMandatoryPropertyNames()
/*      */   {
/*  214 */     if (MandatoryPropertyNames == null)
/*      */     {
/*  216 */       Set<String> propNameSet = new TreeSet();
/*  217 */       String[][] allContainerTypesMandatoryNames = { P8CE_FilePlanImpl.getMandatoryPropertyNames(), P8CE_RecordCategoryImpl.getMandatoryPropertyNames(), P8CE_RecordFolderImpl.getMandatoryPropertyNames(), P8CE_RecordVolumeImpl.getMandatoryPropertyNames(), DispositionRelatedPropNames };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  227 */       for (int i = 0; i < allContainerTypesMandatoryNames.length; i++)
/*      */       {
/*  229 */         for (String propName : allContainerTypesMandatoryNames[i])
/*      */         {
/*  231 */           if (!propNameSet.contains(propName)) {
/*  232 */             propNameSet.add(propName);
/*      */           }
/*      */         }
/*      */       }
/*  236 */       MandatoryPropertyNames = (String[])propNameSet.toArray(new String[propNameSet.size()]);
/*      */     }
/*      */     
/*  239 */     return MandatoryPropertyNames;
/*      */   }
/*      */   
/*      */   static synchronized List<FilterElement> getMandatoryJaceFEs()
/*      */   {
/*  244 */     if (MandatoryJaceFEs == null)
/*      */     {
/*  246 */       String mandatoryNames = P8CE_Util.createSpaceSeparatedString(getMandatoryPropertyNames());
/*  247 */       List<FilterElement> tempList = new ArrayList(1);
/*  248 */       tempList.add(new FilterElement(Integer.valueOf(0), null, Boolean.FALSE, mandatoryNames, null));
/*  249 */       MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*      */     }
/*      */     
/*  252 */     return MandatoryJaceFEs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static IGenerator<Container> getBaseContainerGenerator()
/*      */   {
/*  262 */     return BaseContainerGenerator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<FilterElement> getMandatoryFEs()
/*      */   {
/*  270 */     return getMandatoryJaceFEs();
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
/*      */   protected P8CE_BaseContainerImpl(EntityType entityType, Repository repository, String identity, Folder jaceFolder, boolean isPlaceholder)
/*      */   {
/*  296 */     super(entityType, repository, identity, isPlaceholder);
/*  297 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, jaceFolder, Boolean.valueOf(isPlaceholder) });
/*  298 */     this.jaceFolder = jaceFolder;
/*      */     
/*  300 */     if (!isPlaceholder)
/*      */     {
/*  302 */       updateAllowedEntityTypes();
/*      */     }
/*      */     
/*  305 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EngineObject getJaceBaseObject()
/*      */   {
/*  313 */     return this.jaceFolder;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getObjectIdentity()
/*      */   {
/*  322 */     Tracer.traceMethodEntry(new Object[0]);
/*  323 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceFolder);
/*  324 */     Tracer.traceMethodExit(new Object[] { result });
/*  325 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  333 */     Tracer.traceMethodEntry(new Object[0]);
/*  334 */     String className = P8CE_Util.getJaceObjectClassName(this.jaceFolder);
/*  335 */     if (className == null) {
/*  336 */       className = P8CE_Util.getEntityClassName(this.entityType);
/*      */     }
/*  338 */     Tracer.traceMethodExit(new Object[] { className });
/*  339 */     return className;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getFolderName()
/*      */   {
/*  348 */     Tracer.traceMethodEntry(new Object[0]);
/*  349 */     String result = P8CE_Util.getJacePropertyAsString(this, "FolderName");
/*  350 */     Tracer.traceMethodExit(new Object[] { result });
/*  351 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPathName()
/*      */   {
/*  360 */     Tracer.traceMethodEntry(new Object[0]);
/*  361 */     String result = P8CE_Util.getJacePropertyAsString(this, "PathName");
/*  362 */     Tracer.traceMethodExit(new Object[] { result });
/*  363 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void delete(boolean skipValidation, DeleteMode deleteMode, String reasonForDelete)
/*      */   {
/*  371 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(skipValidation), deleteMode, reasonForDelete });
/*  372 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  375 */       establishedSubject = P8CE_Util.associateSubject();
/*  376 */       resolve();
/*      */       
/*      */ 
/*  379 */       if (!skipValidation) {
/*  380 */         super.validateContainerDeletion();
/*      */       }
/*  382 */       RMDomain jarmDomain = getRepository().getDomain();
/*  383 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/*  384 */       Folder jaceBaseFolder = (Folder)getJaceBaseObject();
/*      */       
/*      */ 
/*  387 */       NamingPatternSequence assocNamingSequence = null;
/*  388 */       if (getEntityType() != EntityType.FilePlan)
/*      */       {
/*  390 */         assocNamingSequence = NamingUtils.getNamingPatternSequence((RecordContainer)this);
/*      */       }
/*      */       
/*  393 */       if ((deleteMode == DeleteMode.ForceHardDelete) || ((deleteMode == DeleteMode.CheckRetainMetadata) && (!isRetainMetadataEnabled())))
/*      */       {
/*      */ 
/*      */ 
/*  397 */         deleteContainees(skipValidation, DeleteMode.ForceHardDelete, reasonForDelete);
/*      */         
/*      */ 
/*  400 */         if (hasLogicallyDeletedContainees()) {
/*  401 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_HAS_LOGICALLY_DELETED_CONTAINEES, new Object[] { getObjectIdentity() });
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  406 */         jaceBaseFolder.refresh();
/*      */         
/*      */ 
/*  409 */         List<CustomObject> deletablePSSAtlasArtifacts = findAutoDeletable_PSSAtlas_artifacts();
/*      */         
/*      */ 
/*  412 */         UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */         
/*      */ 
/*  415 */         jaceBaseFolder.getProperties().putValue("PreventRMEntityDeletion", (String)null);
/*      */         
/*      */ 
/*      */ 
/*  419 */         if (!skipValidation) {
/*  420 */           P8CE_AuditServices.attachDeleteAuditEvent(this, reasonForDelete, AuditStatus.Success, false);
/*      */         }
/*      */         
/*  423 */         jaceBaseFolder.delete();
/*  424 */         jaceUpdateBatch.add(jaceBaseFolder, P8CE_Util.CEPF_Empty);
/*      */         
/*      */ 
/*  427 */         List<Link> associatedJaceLinks = getAssociatedJaceLinks();
/*  428 */         for (Link jaceLink : associatedJaceLinks)
/*      */         {
/*  430 */           jaceLink.delete();
/*  431 */           jaceUpdateBatch.add(jaceLink, P8CE_Util.CEPF_Empty);
/*      */         }
/*      */         
/*      */ 
/*  435 */         P8CE_BaseContainerImpl parentContainer = (P8CE_BaseContainerImpl)getParent();
/*  436 */         if (parentContainer.adjustForChildRemoval(this))
/*      */         {
/*  438 */           jaceUpdateBatch.add(parentContainer.jaceFolder, P8CE_Util.CEPF_Empty);
/*      */         }
/*      */         
/*      */ 
/*  442 */         long startTime = System.currentTimeMillis();
/*  443 */         jaceUpdateBatch.updateBatch();
/*  444 */         long endTime = System.currentTimeMillis();
/*  445 */         Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */         
/*      */ 
/*  448 */         deletePSSAtlasDispositionArtifacts(deletablePSSAtlasArtifacts, jaceDomain);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  453 */         deleteContainees(skipValidation, DeleteMode.ForceLogicalDelete, reasonForDelete);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  458 */         jaceBaseFolder.refresh();
/*      */         
/*      */ 
/*  461 */         UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */         
/*  463 */         jaceBaseFolder.getProperties().putValue("IsDeleted", true);
/*      */         
/*  465 */         jaceBaseFolder.getProperties().putValue("PreventRMEntityDeletion", (String)null);
/*      */         
/*  467 */         if (!skipValidation) {
/*  468 */           P8CE_AuditServices.attachDeleteAuditEvent(this, reasonForDelete, AuditStatus.Success, false);
/*      */         }
/*      */         
/*  471 */         jaceUpdateBatch.add(jaceBaseFolder, P8CE_Util.CEPF_Empty);
/*      */         
/*      */ 
/*  474 */         P8CE_BaseContainerImpl parentContainer = (P8CE_BaseContainerImpl)getParent();
/*  475 */         if (parentContainer.adjustForChildRemoval(this))
/*      */         {
/*  477 */           jaceUpdateBatch.add(parentContainer.jaceFolder, P8CE_Util.CEPF_Empty);
/*      */         }
/*      */         
/*      */ 
/*  481 */         long startTime = System.currentTimeMillis();
/*  482 */         jaceUpdateBatch.updateBatch();
/*  483 */         long endTime = System.currentTimeMillis();
/*  484 */         Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       }
/*      */       
/*  487 */       if (assocNamingSequence != null)
/*      */       {
/*  489 */         assocNamingSequence.delete();
/*      */       }
/*  491 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  495 */       P8CE_AuditServices.attachDeleteAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/*  496 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  500 */       P8CE_AuditServices.attachDeleteAuditEvent(this, cause.getLocalizedMessage(), AuditStatus.Failure, true);
/*  501 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordCategory });
/*      */     }
/*      */     finally
/*      */     {
/*  505 */       if (establishedSubject) {
/*  506 */         P8CE_Util.disassociateSubject();
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
/*      */   protected void deleteContainees(boolean skipValidation, DeleteMode deleteMode, String reasonForDelete)
/*      */   {
/*  519 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(skipValidation), deleteMode, reasonForDelete });
/*      */     
/*  521 */     PageableSet<Container> subContainerSet = getImmediateSubContainers(null);
/*  522 */     if (subContainerSet != null)
/*      */     {
/*  524 */       RMPageIterator<Container> pi = subContainerSet.pageIterator();
/*  525 */       while ((pi != null) && (pi.nextPage()))
/*      */       {
/*  527 */         List<Container> currentSubContainers = pi.getCurrentPage();
/*  528 */         for (Container subContainer : currentSubContainers)
/*      */         {
/*  530 */           subContainer.delete(skipValidation, deleteMode, reasonForDelete);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  537 */     PageableSet<Record> recordSet = getRecords(Integer.valueOf(25));
/*  538 */     if (recordSet != null)
/*      */     {
/*  540 */       RMDomain jarmDomain = getRepository().getDomain();
/*  541 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/*  542 */       UpdatingBatch jaceUpdateBatch = null;
/*  543 */       RefreshMode jaceRefreshMode = deleteMode == DeleteMode.ForceLogicalDelete ? RefreshMode.REFRESH : RefreshMode.NO_REFRESH;
/*  544 */       RMPageIterator<Record> pi = recordSet.pageIterator();
/*  545 */       while ((pi != null) && (pi.nextPage()))
/*      */       {
/*  547 */         jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, jaceRefreshMode);
/*  548 */         List<Record> records = pi.getCurrentPage();
/*  549 */         for (Record record : records)
/*      */         {
/*      */ 
/*  552 */           ((P8CE_RecordImpl)record).contributeToDeleteBatch(skipValidation, deleteMode, reasonForDelete, jaceUpdateBatch, (RecordContainer)this);
/*      */         }
/*      */         
/*  555 */         if (jaceUpdateBatch.hasPendingExecute())
/*      */         {
/*  557 */           long startTime = System.currentTimeMillis();
/*  558 */           jaceUpdateBatch.updateBatch();
/*  559 */           long endTime = System.currentTimeMillis();
/*  560 */           Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  565 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Container> getContainedBy()
/*      */   {
/*  574 */     Tracer.traceMethodEntry(new Object[0]);
/*  575 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  578 */       establishedSubject = P8CE_Util.associateSubject();
/*  579 */       List<Container> result = new ArrayList(1);
/*      */       
/*  581 */       String thisId = this.jaceFolder.getObjectReference().getObjectIdentity();
/*  582 */       StringBuilder sb = new StringBuilder();
/*      */       
/*  584 */       sb.append("SELECT f.[").append("Parent").append("] ");
/*  585 */       sb.append("FROM [").append("Folder").append("] f ");
/*  586 */       sb.append("WHERE f.[").append("Id").append("] = '").append(thisId).append("' ");
/*  587 */       String sqlStatement = sb.toString();
/*      */       
/*  589 */       PropertyFilter jacePF = new PropertyFilter();
/*  590 */       jacePF.addIncludeProperty(1, null, Boolean.FALSE, "Parent", null);
/*  591 */       jacePF.addIncludeProperty(1, null, Boolean.FALSE, P8CE_Util.getContainerTypesPropNames(), null);
/*      */       
/*  593 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/*  594 */       P8CE_RepositoryImpl repository = (P8CE_RepositoryImpl)getRepository();
/*  595 */       ObjectStore jaceObjStore = repository.getJaceObjectStore();
/*  596 */       SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/*      */       
/*  598 */       long startTime = System.currentTimeMillis();
/*  599 */       RepositoryRowSet jaceRowSet = jaceSearchScope.fetchRows(jaceSearchSQL, Integer.valueOf(100), jacePF, Boolean.FALSE);
/*  600 */       long endTime = System.currentTimeMillis();
/*      */       
/*  602 */       Boolean elementCountIndicator = null;
/*  603 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  605 */         elementCountIndicator = jaceRowSet != null ? Boolean.valueOf(jaceRowSet.isEmpty()) : null;
/*      */       }
/*  607 */       Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, jaceRowSet, new Object[] { sqlStatement, Integer.valueOf(100), jacePF, Boolean.FALSE });
/*      */       RepositoryRow jaceRow;
/*  609 */       if (jaceRowSet != null)
/*      */       {
/*  611 */         jaceRow = null;
/*  612 */         Folder jaceBaseFolder = null;
/*  613 */         Container jarmContainer = null;
/*  614 */         IGenerator<? extends Container> generator = null;
/*  615 */         Iterator<RepositoryRow> it = jaceRowSet.iterator();
/*  616 */         while ((it != null) && (it.hasNext()))
/*      */         {
/*  618 */           jaceRow = (RepositoryRow)it.next();
/*  619 */           if ((jaceRow != null) && (jaceRow.getProperties().isPropertyPresent("Parent")))
/*      */           {
/*  621 */             jaceBaseFolder = (Folder)jaceRow.getProperties().getEngineObjectValue("Parent");
/*  622 */             if (jaceBaseFolder != null)
/*      */             {
/*  624 */               generator = P8CE_Util.getEntityGenerator(jaceBaseFolder);
/*  625 */               if (generator != null)
/*      */               {
/*  627 */                 jarmContainer = (Container)generator.create(repository, jaceBaseFolder);
/*  628 */                 if (jarmContainer != null) {
/*  629 */                   result.add(jarmContainer);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  636 */       Tracer.traceMethodExit(new Object[] { result });
/*  637 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  641 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  645 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_PARENT_CONTAINER_FAILED, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  649 */       if (establishedSubject) {
/*  650 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Container getParent()
/*      */   {
/*  659 */     Tracer.traceMethodEntry(new Object[0]);
/*  660 */     P8CE_BaseContainerImpl myParent = null;
/*      */     
/*  662 */     List<Container> containers = getContainedBy();
/*  663 */     if ((containers != null) && (!containers.isEmpty()))
/*      */     {
/*  665 */       myParent = (P8CE_BaseContainerImpl)containers.get(0);
/*      */     }
/*      */     
/*  668 */     Tracer.traceMethodExit(new Object[] { myParent });
/*  669 */     return myParent;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  677 */     Tracer.traceMethodEntry(new Object[0]);
/*  678 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/*  679 */     Tracer.traceMethodExit(new Object[] { result });
/*  680 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<RMPermission> getPermissions()
/*      */   {
/*  688 */     Tracer.traceMethodEntry(new Object[0]);
/*  689 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceFolder);
/*  690 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*      */     
/*  692 */     Tracer.traceMethodExit(new Object[] { result });
/*  693 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMProperties getProperties()
/*      */   {
/*  701 */     Tracer.traceMethodEntry(new Object[0]);
/*  702 */     RMProperties result = null;
/*  703 */     if (this.jaceFolder != null)
/*      */     {
/*  705 */       result = new P8CE_RMPropertiesImpl(this.jaceFolder, this);
/*      */     }
/*      */     
/*  708 */     Tracer.traceMethodExit(new Object[] { result });
/*  709 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PageableSet<AuditEvent> getAuditedEvents(RMPropertyFilter filter)
/*      */   {
/*  717 */     Tracer.traceMethodEntry(new Object[] { filter });
/*  718 */     PageableSet<AuditEvent> resultSet = P8CE_Util.getAuditedEvents(this, filter);
/*  719 */     Tracer.traceMethodExit(new Object[] { resultSet });
/*  720 */     return resultSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void save(RMRefreshMode jarmRefreshMode)
/*      */   {
/*  728 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/*  729 */     Properties jaceProps = this.jaceFolder.getProperties();
/*      */     
/*      */ 
/*      */ 
/*  733 */     preventDirectSettingOfDefensDisposalProps(jaceProps);
/*      */     
/*  735 */     if (!this.isCreationPending)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  740 */       boolean recalcPropIsDirty = false;
/*  741 */       boolean recalcPropIsPresent = jaceProps.isPropertyPresent("RecalculatePhaseRetention");
/*  742 */       if (recalcPropIsPresent)
/*      */       {
/*  744 */         recalcPropIsDirty = jaceProps.get("RecalculatePhaseRetention").isDirty();
/*      */       }
/*  746 */       if (!recalcPropIsDirty)
/*      */       {
/*  748 */         if (!recalcPropIsPresent)
/*      */         {
/*  750 */           this.jaceFolder.fetchProperties(new String[] { "RecalculatePhaseRetention" });
/*  751 */           jaceProps = this.jaceFolder.getProperties();
/*      */         }
/*  753 */         Integer propValue = jaceProps.getInteger32Value("RecalculatePhaseRetention");
/*  754 */         int currentRecalc = propValue != null ? propValue.intValue() : 0;
/*      */         
/*  756 */         if (currentRecalc == 4) {
/*  757 */           jaceProps.putValue("RecalculatePhaseRetention", 0);
/*  758 */         } else if (currentRecalc == 2) {
/*  759 */           jaceProps.putValue("RecalculatePhaseRetention", 1);
/*      */         }
/*      */       }
/*  762 */       if (jaceProps.isPropertyPresent("DisposalSchedule"))
/*      */       {
/*  764 */         Property dispSchedProp = jaceProps.get("DisposalSchedule");
/*  765 */         if ((dispSchedProp.isDirty()) && (dispSchedProp.getObjectValue() != null) && (isADefensiblyDisposableContainer()))
/*      */         {
/*      */ 
/*  768 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ASSIGN_DISPSCHEDULE_TO_DD_CONTAINER, new Object[0]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  773 */     internalSave(jarmRefreshMode);
/*  774 */     this.isCreationPending = false;
/*      */     
/*  776 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*      */   {
/*  784 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/*  785 */     RefreshMode jaceRefreshMode = P8CE_Util.convertToJaceRefreshMode(jarmRefreshMode);
/*      */     
/*  787 */     long startTime = System.currentTimeMillis();
/*  788 */     this.jaceFolder.save(jaceRefreshMode);
/*  789 */     Tracer.traceExtCall("Folder.save()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceRefreshMode, new Object[0]);
/*      */     
/*  791 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPermissions(List<RMPermission> jarmPerms)
/*      */   {
/*  799 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/*  800 */     Util.ckNullObjParam("jarmPerms", jarmPerms);
/*      */     
/*  802 */     AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(jarmPerms);
/*  803 */     this.jaceFolder.set_Permissions(jacePerms);
/*  804 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  816 */     Tracer.traceMethodEntry(new Object[0]);
/*  817 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  820 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  822 */       boolean doValidation = true;
/*  823 */       doDestroy(doValidation);
/*  824 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  828 */       if (establishedSubject) {
/*  829 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */   public void destroy(boolean doValidation)
/*      */   {
/*  848 */     Tracer.traceMethodEntry(new Object[0]);
/*  849 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  852 */       establishedSubject = P8CE_Util.associateSubject();
/*  853 */       doDestroy(doValidation);
/*  854 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  858 */       if (establishedSubject) {
/*  859 */         P8CE_Util.disassociateSubject();
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
/*      */   void doDestroy(boolean doValidation)
/*      */   {
/*      */     try
/*      */     {
/*  878 */       refresh(DispositionRelatedPropNames);
/*      */       
/*      */ 
/*  881 */       if ((isAnyChildContainerInActivePhase()) || (isAnyChildRecordInActivePhase()))
/*      */       {
/*      */ 
/*  884 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DESTROY_FAILURE_CHILD_WITH_DIFF_RETENTION, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/*      */ 
/*  888 */       boolean isPartOfTransferProcess = false;
/*  889 */       Integer rawCurrentActionType = P8CE_Util.getJacePropertyAsInteger(this, "CurrentActionType");
/*  890 */       if ((rawCurrentActionType != null) && (rawCurrentActionType.intValue() == DispositionActionType.Transfer.getIntValue()))
/*      */       {
/*      */ 
/*  893 */         isPartOfTransferProcess = true;
/*      */       }
/*      */       
/*      */ 
/*  897 */       P8CE_Util.validateCurrentPhaseAndSchedule(this);
/*      */       
/*      */ 
/*  900 */       if (doValidation)
/*      */       {
/*  902 */         RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.Destroy);
/*      */       }
/*      */       
/*      */ 
/*  906 */       RMDomain jarmDomain = getRepository().getDomain();
/*  907 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/*  908 */       User jaceUser = P8CE_Util.fetchCurrentJaceUser(jaceDomain);
/*  909 */       String userShortName = jaceUser.get_ShortName();
/*      */       
/*      */ 
/*  912 */       internalDestroy(doValidation, isPartOfTransferProcess, userShortName);
/*      */       
/*  914 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  918 */       P8CE_AuditServices.attachDestroyAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/*  919 */       rollbackCurrentPhaseState();
/*      */       
/*  921 */       if ((ex instanceof RMRuntimeException)) {
/*  922 */         throw ((RMRuntimeException)ex);
/*      */       }
/*  924 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_DESTROY_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
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
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void internalDestroy(boolean doValidation, boolean isPartOfTransferProcess, String userName)
/*      */   {
/*  943 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(doValidation), Boolean.valueOf(isPartOfTransferProcess), userName });
/*  944 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[0]);
/*      */   }
/*      */   
/*      */   protected void destroyEmptyContainer(boolean doValidation, boolean isPartOfTransferProcess, String userName)
/*      */   {
/*  949 */     if ((!hasChildContainers()) && (!hasChildRecords()))
/*      */     {
/*  951 */       boolean isLogicalDelete = isRetainMetadataEnabled();
/*  952 */       if (isLogicalDelete)
/*      */       {
/*  954 */         refresh();
/*      */         
/*  956 */         if ((doValidation) || (isPartOfTransferProcess)) {
/*  957 */           RALDispositionLogic.setFinalPhaseDataOnEntity(this);
/*      */         }
/*  959 */         getProperties().putStringValue("DisposalAuthorizedBy", userName);
/*  960 */         save(RMRefreshMode.Refresh);
/*      */       }
/*      */       
/*  963 */       if (doValidation) {
/*  964 */         P8CE_AuditServices.attachDestroyAuditEvent(this, "", AuditStatus.Success, true);
/*      */       }
/*      */       
/*  967 */       boolean skipValidation = true;
/*  968 */       DeleteMode deleteMode = isLogicalDelete ? DeleteMode.ForceLogicalDelete : DeleteMode.ForceHardDelete;
/*  969 */       String reasonForDelete = RMLString.get("disposition.deleteFromDestroy", "Deletion performed as a result of destroy action.");
/*  970 */       delete(skipValidation, deleteMode, reasonForDelete);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void executeInterimTransferUsingExternalExport(Location newLocation, ExternalExport externalExport, boolean doValidation)
/*      */   {
/*  979 */     Tracer.traceMethodEntry(new Object[] { newLocation });
/*  980 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  983 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  985 */       boolean doExport = true;
/*  986 */       internalExecuteInterimTransfer(doValidation, doExport, newLocation, externalExport);
/*  987 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  991 */       if (establishedSubject) {
/*  992 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */   public void executeInterimTransferUsingP8_XML(Location newLocation)
/*      */   {
/* 1011 */     Tracer.traceMethodEntry(new Object[] { newLocation });
/* 1012 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1015 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1017 */       boolean doValidation = true;
/* 1018 */       boolean doExport = true;
/* 1019 */       internalExecuteInterimTransfer(doValidation, doExport, newLocation, null);
/* 1020 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1024 */       if (establishedSubject) {
/* 1025 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void internalExecuteInterimTransfer(boolean doValidation, boolean doExport, Location newLocation, ExternalExport externalExport)
/*      */   {
/* 1047 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(doValidation), Boolean.valueOf(doExport), newLocation });
/* 1048 */     Util.ckNullObjParam("newLocation", newLocation);
/*      */     
/* 1050 */     String exportDirPath = null;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1055 */       refresh(DispositionRelatedPropNames);
/*      */       
/*      */ 
/* 1058 */       if (doValidation)
/*      */       {
/* 1060 */         P8CE_Util.validateCurrentPhaseAndSchedule(this);
/* 1061 */         RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.InterimTransfer);
/*      */       }
/*      */       
/*      */ 
/* 1065 */       exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/*      */       
/* 1067 */       if (doExport)
/*      */       {
/* 1069 */         if (externalExport == null)
/*      */         {
/* 1071 */           boolean doExportValidation = false;
/* 1072 */           exportUsingP8_XML(doExportValidation);
/*      */         }
/*      */         else
/*      */         {
/* 1076 */           String reposIdent = getRepository().getSymbolicName();
/* 1077 */           String entityIdent = getObjectIdentity();
/* 1078 */           externalExport.export(reposIdent, entityIdent);
/*      */         } }
/*      */       boolean doValidationOnChildRecord;
/*      */       boolean doExportOnChildRecord;
/* 1082 */       if ((doValidation) && (doExport))
/*      */       {
/*      */ 
/* 1085 */         boolean doValidationOnChildContainer = false;
/* 1086 */         boolean doExportOnChildContainer = false;
/* 1087 */         List<Container> jarmHierChildContainers = retrieveAllHierarchicalChildContainers();
/* 1088 */         for (Container jarmChildContainer : jarmHierChildContainers)
/*      */         {
/* 1090 */           ((P8CE_BaseContainerImpl)jarmChildContainer).internalExecuteInterimTransfer(doValidationOnChildContainer, doExportOnChildContainer, newLocation, externalExport);
/*      */         }
/*      */         
/*      */ 
/* 1094 */         doValidationOnChildRecord = false;
/* 1095 */         doExportOnChildRecord = false;
/* 1096 */         List<Record> jarmHierChildRecords = retrieveAllHierarchicalChildRecords();
/* 1097 */         for (Record jarmChildRecord : jarmHierChildRecords)
/*      */         {
/* 1099 */           ((P8CE_RecordImpl)jarmChildRecord).internalExecuteInterimTransfer(doValidationOnChildRecord, doExportOnChildRecord, newLocation, externalExport);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1104 */       RMProperties jarmProps = getProperties();
/* 1105 */       jarmProps.putObjectValue("Location", newLocation);
/* 1106 */       if (EntityType.ElectronicRecordFolder != getEntityType())
/*      */       {
/* 1108 */         jarmProps.putObjectValue("HomeLocation", newLocation);
/*      */       }
/* 1110 */       save(RMRefreshMode.Refresh);
/*      */       
/* 1112 */       if (doValidation) {
/* 1113 */         P8CE_AuditServices.attachInterimTransferAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/* 1115 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1119 */       P8CE_AuditServices.attachInterimTransferAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, exportDirPath, true);
/* 1120 */       rollbackCurrentPhaseState();
/*      */       
/* 1122 */       if ((ex instanceof RMRuntimeException)) {
/* 1123 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 1125 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INTERIMTRANSFER_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
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
/*      */ 
/*      */ 
/*      */   public void transferUsingExternalExport(ExternalExport externalExport, boolean doValidation)
/*      */   {
/* 1143 */     Tracer.traceMethodEntry(new Object[] { externalExport, Boolean.valueOf(doValidation) });
/* 1144 */     Util.ckNullObjParam("externalExport", externalExport);
/*      */     
/* 1146 */     internalTransfer(externalExport, doValidation);
/*      */     
/* 1148 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public void transferUsingP8_XML()
/*      */   {
/* 1162 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/* 1164 */     ExternalExport externalExport = null;
/* 1165 */     boolean doValidation = true;
/* 1166 */     internalTransfer(externalExport, doValidation);
/*      */     
/* 1168 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public void internalTransfer(ExternalExport externalExport, boolean doValidation)
/*      */   {
/* 1181 */     Tracer.traceMethodEntry(new Object[0]);
/* 1182 */     boolean establishedSubject = false;
/* 1183 */     String exportDirPath = null;
/*      */     try
/*      */     {
/* 1186 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 1190 */       refresh(DispositionRelatedPropNames);
/*      */       
/* 1192 */       if (doValidation)
/*      */       {
/*      */ 
/* 1195 */         P8CE_Util.validateCurrentPhaseAndSchedule(this);
/* 1196 */         RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.Transfer);
/*      */       }
/*      */       
/*      */ 
/* 1200 */       exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/*      */       
/* 1202 */       if (externalExport == null)
/*      */       {
/* 1204 */         boolean doExportValidation = false;
/* 1205 */         exportUsingP8_XML(doExportValidation);
/*      */       }
/*      */       else
/*      */       {
/* 1209 */         String objStoreIdent = getRepository().getObjectIdentity();
/* 1210 */         String entityIdStr = getObjectIdentity();
/* 1211 */         externalExport.export(objStoreIdent, entityIdStr);
/*      */       }
/*      */       
/* 1214 */       if (doValidation) {
/* 1215 */         P8CE_AuditServices.attachTransferAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/*      */       
/* 1218 */       boolean doDestroyValidation = false;
/* 1219 */       doDestroy(doDestroyValidation);
/*      */       
/* 1221 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1225 */       if (doValidation) {
/* 1226 */         P8CE_AuditServices.attachTransferAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, exportDirPath, true);
/*      */       }
/* 1228 */       rollbackCurrentPhaseState();
/*      */       
/* 1230 */       if ((ex instanceof RMRuntimeException)) {
/* 1231 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 1233 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_TRANSFER_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */     finally
/*      */     {
/* 1237 */       if (establishedSubject) {
/* 1238 */         P8CE_Util.disassociateSubject();
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
/*      */   public void exportUsingExternalExport(ExternalExport externalExport, boolean doValidation)
/*      */   {
/* 1255 */     Tracer.traceMethodEntry(new Object[] { externalExport, Boolean.valueOf(doValidation) });
/* 1256 */     Util.ckNullObjParam("externalExport", externalExport);
/* 1257 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1260 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 1264 */       refresh(DispositionRelatedPropNames);
/*      */       
/* 1266 */       if (doValidation)
/*      */       {
/* 1268 */         validateForDispositionExport();
/*      */       }
/*      */       
/*      */ 
/* 1272 */       String objStoreIdent = getRepository().getObjectIdentity();
/* 1273 */       String entityIdStr = getObjectIdentity();
/* 1274 */       externalExport.export(objStoreIdent, entityIdStr);
/*      */       
/* 1276 */       if (doValidation)
/*      */       {
/*      */ 
/* 1279 */         String exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/* 1280 */         P8CE_AuditServices.attachExportAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/* 1282 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1286 */       if (establishedSubject) {
/* 1287 */         P8CE_Util.disassociateSubject();
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
/*      */   public void exportUsingP8_XML()
/*      */   {
/* 1303 */     Tracer.traceMethodEntry(new Object[0]);
/* 1304 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1307 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1309 */       boolean doValidation = true;
/* 1310 */       exportUsingP8_XML(doValidation);
/* 1311 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1315 */       if (establishedSubject) {
/* 1316 */         P8CE_Util.disassociateSubject();
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
/*      */   void exportUsingP8_XML(boolean doValidation)
/*      */   {
/* 1329 */     Tracer.traceMethodEntry(new Object[0]);
/* 1330 */     String exportDirPath = null;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1335 */       refresh(DispositionRelatedPropNames);
/*      */       
/* 1337 */       if (doValidation)
/*      */       {
/* 1339 */         validateForDispositionExport();
/*      */       }
/*      */       
/*      */ 
/* 1343 */       exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/* 1344 */       if ((exportDirPath == null) || (exportDirPath.trim().length() == 0))
/*      */       {
/* 1346 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_FILESYSTEM_DESTINATION_PATH_INVALID, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/*      */ 
/* 1350 */       String xsltStr = P8CE_Util.fetchExportFormatXSLT(this);
/*      */       
/*      */ 
/* 1353 */       ContentXMLExport contentExportMode = P8CE_Util.getSystemConfiguration_ExportConfigSetting((FilePlanRepository)getRepository());
/*      */       
/*      */ 
/* 1356 */       exportUsingP8_XML(exportDirPath, xsltStr, contentExportMode);
/*      */       
/* 1358 */       if (doValidation) {
/* 1359 */         P8CE_AuditServices.attachExportAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/* 1361 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1365 */       P8CE_AuditServices.attachExportAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, exportDirPath, true);
/* 1366 */       rollbackCurrentPhaseState();
/*      */       
/* 1368 */       if ((ex instanceof RMRuntimeException)) {
/* 1369 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 1371 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_EXPORT_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public void exportUsingP8_XML(String exportFileSystemPath, String xsltDefStr, ContentXMLExport contentExportOption)
/*      */   {
/* 1390 */     Tracer.traceMethodEntry(new Object[] { exportFileSystemPath, xsltDefStr, contentExportOption });
/* 1391 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1394 */       establishedSubject = P8CE_Util.associateSubject();
/* 1395 */       resolve();
/*      */       
/*      */ 
/* 1398 */       File exportFileSystemDir = null;
/* 1399 */       if ((exportFileSystemPath == null) || (exportFileSystemPath.trim().length() == 0))
/*      */       {
/* 1401 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_FILESYSTEM_DESTINATION_PATH_INVALID, new Object[] { getObjectIdentity() });
/*      */       }
/* 1403 */       boolean dirExists = false;
/*      */       try
/*      */       {
/* 1406 */         exportFileSystemDir = new File(exportFileSystemPath);
/* 1407 */         dirExists = exportFileSystemDir.exists();
/* 1408 */         if (!dirExists) {
/* 1409 */           dirExists = exportFileSystemDir.mkdirs();
/*      */         }
/*      */       }
/*      */       catch (Exception ex) {
/* 1413 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.API_EXPORT_CANT_CREATE_DESTINATION_DIR, new Object[] { exportFileSystemPath });
/*      */       }
/* 1415 */       if (!dirExists) {
/* 1416 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_CANT_CREATE_DESTINATION_DIR, new Object[] { exportFileSystemPath });
/*      */       }
/*      */       
/* 1419 */       String exportFilterStr = P8CE_XMLExportSupport.getContainerExportClassFilter((RecordContainer)this);
/*      */       
/*      */ 
/*      */ 
/* 1423 */       Repository jarmRepository = getRepository();
/* 1424 */       int exportFlags = 2;
/* 1425 */       org.w3c.dom.Document exportedEntityDOMDoc = P8CE_XMLExportSupport.doJaceXMLExport(jarmRepository, this.jaceFolder, exportFilterStr, exportFlags);
/*      */       
/*      */ 
/*      */ 
/* 1429 */       List<Container> jarmHierChildContainers = retrieveAllHierarchicalChildContainers();
/*      */       
/* 1431 */       List<Record> jarmHierChildRecords = retrieveAllHierarchicalChildRecords();
/* 1432 */       List<BaseEntity> jaceLinkMemberCandidates = new ArrayList();
/* 1433 */       jaceLinkMemberCandidates.addAll(jarmHierChildContainers);
/* 1434 */       jaceLinkMemberCandidates.addAll(jarmHierChildRecords);
/* 1435 */       jaceLinkMemberCandidates.add(0, this);
/* 1436 */       org.w3c.dom.Document exportedLinkDOMDoc = null;
/* 1437 */       Node mainXMLOthersNode = null;
/* 1438 */       IndependentObject jaceLinkMemberCandidate = null;
/* 1439 */       for (BaseEntity baseEntity : jaceLinkMemberCandidates)
/*      */       {
/* 1441 */         jaceLinkMemberCandidate = (IndependentObject)((JaceBasable)baseEntity).getJaceBaseObject();
/* 1442 */         List<Link> memberLinks = P8CE_Util.findAssociatedLinks(jaceLinkMemberCandidate);
/* 1443 */         if (memberLinks.size() > 0)
/*      */         {
/* 1445 */           if (mainXMLOthersNode == null)
/*      */           {
/* 1447 */             mainXMLOthersNode = RAL_XMLSupport.findXMLNode(exportedEntityDOMDoc, "/ObjectManifest/Others", true);
/* 1448 */             if (mainXMLOthersNode == null) {
/* 1449 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_EXPORT_P8CE_XMLGENERATION_FAILURE, new Object[] { getObjectIdentity() });
/*      */             }
/*      */           }
/* 1452 */           for (Link link : memberLinks)
/*      */           {
/* 1454 */             exportedLinkDOMDoc = P8CE_XMLExportSupport.doJaceXMLExport(jarmRepository, link, "", 0);
/* 1455 */             NodeList linkNodes = RAL_XMLSupport.findXMLNodes(exportedLinkDOMDoc, "/ObjectManifest/*", true);
/* 1456 */             if (linkNodes != null)
/*      */             {
/* 1458 */               for (int i = 0; i < linkNodes.getLength(); i++)
/*      */               {
/* 1460 */                 Node linkNode = linkNodes.item(i);
/* 1461 */                 boolean deepImport = true;
/* 1462 */                 Node importedLinkNode = exportedEntityDOMDoc.importNode(linkNode, deepImport);
/* 1463 */                 mainXMLOthersNode.appendChild(importedLinkNode);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1471 */       if ((xsltDefStr != null) && (xsltDefStr.trim().length() > 0))
/*      */       {
/* 1473 */         exportedEntityDOMDoc = RAL_XMLSupport.transformUsingXSLT(exportedEntityDOMDoc, xsltDefStr);
/*      */       }
/*      */       
/*      */ 
/* 1477 */       String mainXMLFileName = P8CE_XMLExportSupport.generateExportFileName("RM_", getFolderName(), "_Hierarchy_");
/* 1478 */       File mainXMLFile = new File(exportFileSystemDir, mainXMLFileName);
/* 1479 */       RAL_XMLSupport.writeDomDocToFile(exportedEntityDOMDoc, mainXMLFile);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1486 */       if ((jarmHierChildRecords != null) && (jarmHierChildRecords.size() > 0))
/*      */       {
/* 1488 */         for (Record childRecord : jarmHierChildRecords)
/*      */         {
/* 1490 */           ((P8CE_RecordImpl)childRecord).exportAssocContentUsingP8_XML(exportFileSystemDir, contentExportOption);
/*      */         }
/*      */       }
/*      */       
/* 1494 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException rre)
/*      */     {
/* 1498 */       throw rre;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1502 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_EXPORT_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */     finally
/*      */     {
/* 1506 */       if (establishedSubject) {
/* 1507 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void review(RMProperties reviewProps)
/*      */   {
/* 1531 */     Tracer.traceMethodEntry(new Object[0]);
/* 1532 */     Util.ckNullObjParam("reviewProps", reviewProps);
/* 1533 */     if (reviewProps.size() == 0)
/*      */     {
/* 1535 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_REVIEW_EMPTY_PROPS_COLLECTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/* 1538 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1541 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1543 */       Properties jaceProps = this.jaceFolder.getProperties();
/* 1544 */       P8CE_Util.convertToJaceProperties(reviewProps, jaceProps);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1550 */       PropertyFilter jacePF = new PropertyFilter();
/* 1551 */       for (FilterElement jaceFE : getMandatoryJaceFEs())
/*      */       {
/* 1553 */         jacePF.addIncludeProperty(jaceFE);
/*      */       }
/* 1555 */       this.jaceFolder.save(RefreshMode.REFRESH, jacePF);
/*      */       
/*      */ 
/* 1558 */       P8CE_Util.validateCurrentPhaseAndSchedule(this);
/* 1559 */       RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.Review);
/*      */       
/* 1561 */       P8CE_AuditServices.attachReviewAuditEvent(this, "", AuditStatus.Success, true);
/* 1562 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1566 */       P8CE_AuditServices.attachReviewAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/* 1567 */       rollbackCurrentPhaseState();
/*      */       
/* 1569 */       if ((ex instanceof RMRuntimeException)) {
/* 1570 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 1572 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_TRANSFER_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */     finally
/*      */     {
/* 1576 */       if (establishedSubject) {
/* 1577 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<Container> retrieveAllHierarchicalChildContainers()
/*      */   {
/* 1588 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*      */ 
/*      */ 
/* 1592 */     List<FilterElement> mandatoryContainerFEs = getMandatoryJaceFEs();
/* 1593 */     PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryContainerFEs);
/*      */     
/*      */ 
/* 1596 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RMFolder", "rf");
/*      */     
/* 1598 */     sb.append("WHERE rf.This INSUBFOLDER '").append(getObjectIdentity()).append("' ");
/* 1599 */     sb.append("AND rf.").append("IsDeleted").append(" = FALSE ");
/* 1600 */     String sqlStatement = sb.toString();
/*      */     
/* 1602 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1603 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */     
/*      */ 
/* 1606 */     Integer pageSize = null;
/* 1607 */     Boolean continuable = Boolean.TRUE;
/* 1608 */     long startTime = System.currentTimeMillis();
/* 1609 */     IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1610 */     long stopTime = System.currentTimeMillis();
/*      */     
/* 1612 */     Boolean elementCountIndicator = null;
/* 1613 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 1615 */       elementCountIndicator = Boolean.valueOf(jaceFolderSet.isEmpty());
/*      */     }
/* 1617 */     Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement });
/*      */     
/*      */ 
/* 1620 */     IGenerator<Container> generator = getBaseContainerGenerator();
/* 1621 */     List<Container> resultList = new ArrayList();
/* 1622 */     PageIterator jacePI = jaceFolderSet.pageIterator();
/* 1623 */     while (jacePI.nextPage())
/*      */     {
/* 1625 */       Object[] currentPage = jacePI.getCurrentPage();
/* 1626 */       for (Object obj : currentPage)
/*      */       {
/* 1628 */         resultList.add(generator.create(this.repository, (Folder)obj));
/*      */       }
/*      */     }
/*      */     
/* 1632 */     Tracer.traceMethodExit(new Object[] { resultList });
/* 1633 */     return resultList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<Record> retrieveAllHierarchicalChildRecords()
/*      */   {
/* 1643 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*      */ 
/*      */ 
/* 1647 */     List<FilterElement> mandatoryRecordFEs = P8CE_RecordImpl.getMandatoryJaceFEs();
/* 1648 */     PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryRecordFEs);
/*      */     
/*      */ 
/* 1651 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordInfo", "rec");
/*      */     
/* 1653 */     sb.append("WHERE rec.This INSUBFOLDER '").append(getObjectIdentity()).append("' ");
/* 1654 */     sb.append("AND rec.").append("IsDeleted").append(" = FALSE ");
/* 1655 */     String sqlStatement = sb.toString();
/*      */     
/* 1657 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1658 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */     
/* 1660 */     Integer pageSize = null;
/* 1661 */     Boolean continuable = Boolean.TRUE;
/* 1662 */     long startTime = System.currentTimeMillis();
/* 1663 */     IndependentObjectSet jaceDocSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1664 */     long stopTime = System.currentTimeMillis();
/*      */     
/* 1666 */     Boolean elementCountIndicator = null;
/* 1667 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 1669 */       elementCountIndicator = Boolean.valueOf(jaceDocSet.isEmpty());
/*      */     }
/* 1671 */     Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceDocSet, new Object[] { sqlStatement });
/*      */     
/*      */ 
/* 1674 */     IGenerator<Record> generator = P8CE_RecordImpl.getGenerator();
/* 1675 */     List<Record> resultList = new ArrayList();
/* 1676 */     PageIterator jacePI = jaceDocSet.pageIterator();
/* 1677 */     while (jacePI.nextPage())
/*      */     {
/* 1679 */       Object[] currentPage = jacePI.getCurrentPage();
/* 1680 */       for (Object obj : currentPage)
/*      */       {
/* 1682 */         resultList.add(generator.create(this.repository, (com.filenet.api.core.Document)obj));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1687 */     Tracer.traceMethodExit(new Object[] { resultList });
/* 1688 */     return resultList;
/*      */   }
/*      */   
/*      */   protected void rollbackCurrentPhaseState()
/*      */   {
/* 1693 */     getProperties().putIntegerValue("CurrentPhaseExecutionStatus", Integer.valueOf(RMWorkflowStatus.NotStarted.getIntValue()));
/* 1694 */     save(RMRefreshMode.Refresh);
/*      */     
/* 1696 */     Id currentPhaseId = P8CE_Util.getJacePropertyAsId(this, "CurrentPhaseID");
/* 1697 */     if (currentPhaseId != null)
/*      */     {
/* 1699 */       DispositionSchedule schedule = getAssignedSchedule();
/* 1700 */       if (schedule != null) {
/* 1701 */         ((P8CE_DispositionScheduleImpl)schedule).updateCurrentPhaseDataOnEntity(this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1711 */     return toString("P8CE_BaseContainerImpl");
/*      */   }
/*      */   
/*      */   protected String toString(String prefix)
/*      */   {
/* 1716 */     String ident = "<unknown>";
/* 1717 */     if (P8CE_Util.isJacePropertyPresent(this, "PathName")) {
/* 1718 */       ident = getPathName();
/* 1719 */     } else if (P8CE_Util.isJacePropertyPresent(this, "Id")) {
/* 1720 */       ident = getObjectIdentity();
/* 1721 */     } else if (P8CE_Util.isJacePropertyPresent(this, "FolderName")) {
/* 1722 */       ident = getFolderName();
/* 1723 */     } else if (getClientIdentifier() != null) {
/* 1724 */       ident = getClientIdentifier();
/*      */     }
/* 1726 */     return prefix + ": '" + ident + "'";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordCategory addRecordCategory(String classIdent, RMProperties props, List<RMPermission> perms)
/*      */   {
/* 1734 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms });
/* 1735 */     RecordCategory result = addRecordCategory(classIdent, props, perms, null);
/* 1736 */     Tracer.traceMethodExit(new Object[] { result });
/* 1737 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordCategory addRecordCategory(String classIdent, RMProperties props, List<RMPermission> perms, String idStr)
/*      */   {
/* 1745 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms, idStr });
/* 1746 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1749 */       establishedSubject = P8CE_Util.associateSubject();
/* 1750 */       resolve();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1755 */       super.validateAndPrepareNewRecordCategory(classIdent, props, perms);
/*      */       
/*      */ 
/* 1758 */       RMDomain jarmDomain = getRepository().getDomain();
/* 1759 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 1760 */       UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */       
/* 1762 */       Folder jaceParentBaseFolder = (Folder)getJaceBaseObject();
/* 1763 */       ObjectStore jaceObjStore = jaceParentBaseFolder.getObjectStore();
/*      */       
/*      */ 
/* 1766 */       Id newId = P8CE_Util.processIdStr(idStr);
/* 1767 */       Folder newJaceBaseFolder = Factory.Folder.createInstance(jaceObjStore, classIdent, newId);
/*      */       
/*      */ 
/* 1770 */       Properties jaceProps = newJaceBaseFolder.getProperties();
/* 1771 */       P8CE_Util.convertToJaceProperties(props, jaceProps);
/*      */       
/* 1773 */       if (!this.isAddDefensDisposalContainerInProgress)
/*      */       {
/*      */ 
/* 1776 */         preventDirectSettingOfDefensDisposalProps(jaceProps);
/*      */       }
/*      */       
/*      */ 
/* 1780 */       jaceProps.putValue("Parent", jaceParentBaseFolder);
/*      */       
/*      */ 
/* 1783 */       if (perms != null)
/*      */       {
/* 1785 */         AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(perms);
/* 1786 */         newJaceBaseFolder.set_Permissions(jacePerms);
/*      */       }
/*      */       
/*      */ 
/* 1790 */       PropertyFilter jacePF = new PropertyFilter();
/* 1791 */       for (FilterElement fe : P8CE_RecordCategoryImpl.getMandatoryJaceFEs()) {
/* 1792 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*      */       
/* 1795 */       jaceUpdateBatch.add(newJaceBaseFolder, jacePF);
/*      */       
/*      */ 
/* 1798 */       jaceUpdateBatch.add(jaceParentBaseFolder, jacePF);
/*      */       
/*      */ 
/* 1801 */       long startTime = System.currentTimeMillis();
/* 1802 */       jaceUpdateBatch.updateBatch();
/* 1803 */       long endTime = System.currentTimeMillis();
/* 1804 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/*      */ 
/* 1807 */       RecordCategory result = (RecordCategory)P8CE_RecordCategoryImpl.getGenerator().create(getRepository(), newJaceBaseFolder);
/*      */       
/* 1809 */       Tracer.traceMethodExit(new Object[] { result });
/* 1810 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1814 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1818 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_ADD_RECORDCATEGORY_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordCategory });
/*      */     }
/*      */     finally
/*      */     {
/* 1822 */       if (establishedSubject) {
/* 1823 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PageableSet<RecordCategory> fetchRecordCategories(RMPropertyFilter jarmFilter, Integer pageSize)
/*      */   {
/* 1832 */     Tracer.traceMethodEntry(new Object[] { jarmFilter, pageSize });
/* 1833 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1836 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1838 */       PageableSet<RecordCategory> result = null;
/*      */       
/*      */ 
/*      */ 
/* 1842 */       List<FilterElement> mandatoryRecCatFEs = P8CE_RecordCategoryImpl.getMandatoryJaceFEs();
/* 1843 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecCatFEs);
/*      */       
/* 1845 */       String parentId = getObjectIdentity();
/*      */       
/* 1847 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordCategory", "rc");
/*      */       
/* 1849 */       sb.append(" WHERE rc.[").append("Parent").append("] = OBJECT('").append(parentId).append("') ");
/* 1850 */       sb.append(" AND rc.[").append("IsDeleted").append("] = FALSE ");
/* 1851 */       sb.append(" ORDER BY rc.[").append("RecordCategoryName").append("] ");
/* 1852 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/* 1855 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1856 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */       
/*      */ 
/* 1859 */       Boolean continuable = Boolean.TRUE;
/* 1860 */       long startTime = System.currentTimeMillis();
/* 1861 */       IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1862 */       long stopTime = System.currentTimeMillis();
/* 1863 */       Boolean elementCountIndicator = null;
/* 1864 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 1866 */         elementCountIndicator = jaceFolderSet != null ? Boolean.valueOf(jaceFolderSet.isEmpty()) : null;
/*      */       }
/* 1868 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/* 1871 */       boolean supportsPaging = true;
/* 1872 */       IGenerator<RecordCategory> generator = P8CE_RecordCategoryImpl.getGenerator();
/* 1873 */       result = new P8CE_PageableSetImpl(this.repository, jaceFolderSet, supportsPaging, generator);
/*      */       
/* 1875 */       Tracer.traceMethodExit(new Object[] { result });
/* 1876 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1880 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1884 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_RECORD_CATEGORIES_FAILED, new Object[] { getPathName() });
/*      */     }
/*      */     finally
/*      */     {
/* 1888 */       if (establishedSubject) {
/* 1889 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PageableSet<RecordFolder> fetchRecordFolders(RMPropertyFilter jarmFilter, Integer pageSize)
/*      */   {
/* 1898 */     Tracer.traceMethodEntry(new Object[] { jarmFilter, pageSize });
/* 1899 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1902 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1904 */       PageableSet<RecordFolder> result = null;
/*      */       
/*      */ 
/*      */ 
/* 1908 */       List<FilterElement> mandatoryRecFolderFEs = P8CE_RecordFolderImpl.getMandatoryJaceFEs();
/* 1909 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecFolderFEs);
/*      */       
/* 1911 */       String parentId = getObjectIdentity();
/*      */       
/* 1913 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordFolder", "rf");
/*      */       
/* 1915 */       sb.append(" WHERE rf.[").append("Parent").append("] = OBJECT('").append(parentId).append("') ");
/* 1916 */       sb.append(" AND rf.[").append("IsDeleted").append("] = FALSE ");
/* 1917 */       sb.append(" ORDER BY rf.[").append("RecordFolderName").append("] ");
/* 1918 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/* 1921 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1922 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */       
/*      */ 
/* 1925 */       Boolean continuable = Boolean.TRUE;
/* 1926 */       long startTime = System.currentTimeMillis();
/* 1927 */       IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1928 */       long stopTime = System.currentTimeMillis();
/* 1929 */       Boolean elementCountIndicator = null;
/* 1930 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 1932 */         elementCountIndicator = jaceFolderSet != null ? Boolean.valueOf(jaceFolderSet.isEmpty()) : null;
/*      */       }
/* 1934 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement });
/*      */       
/*      */ 
/*      */ 
/* 1938 */       boolean supportsPaging = true;
/* 1939 */       IGenerator<RecordFolder> generator = P8CE_RecordFolderImpl.getGenerator();
/* 1940 */       result = new P8CE_PageableSetImpl(this.repository, jaceFolderSet, supportsPaging, generator);
/*      */       
/* 1942 */       Tracer.traceMethodExit(new Object[] { result });
/* 1943 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1947 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1951 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_RECORD_FOLDERS_FAILED, new Object[] { getPathName() });
/*      */     }
/*      */     finally
/*      */     {
/* 1955 */       if (establishedSubject) {
/* 1956 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void move(Container destContainer, String reasonForMove)
/*      */   {
/* 1966 */     Tracer.traceMethodEntry(new Object[] { destContainer, reasonForMove });
/* 1967 */     Util.ckNullObjParam("destContainer", destContainer);
/* 1968 */     Util.ckInvalidStrParam("reasonForMove", reasonForMove);
/*      */     
/* 1970 */     if (getObjectIdentity().equalsIgnoreCase(destContainer.getObjectIdentity()))
/*      */     {
/* 1972 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_SOURCE_DESTINATION_SAME, new Object[] { destContainer });
/*      */     }
/*      */     
/* 1975 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1978 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1980 */       String destIdent = destContainer.getObjectIdentity();
/* 1981 */       if (isContainerAHierarchyChild(destIdent))
/*      */       {
/* 1983 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_CONTAINER_MOVE_TO_SUBCONTAINER, new Object[] { getObjectIdentity(), destIdent });
/*      */       }
/*      */       
/* 1986 */       resolve();
/* 1987 */       ((P8CE_BaseContainerImpl)destContainer).resolve();
/*      */       
/* 1989 */       Container[] containersToValidate = new Container[2];
/* 1990 */       containersToValidate[0] = this;
/* 1991 */       containersToValidate[1] = destContainer;
/* 1992 */       validateFilePlanScope(containersToValidate);
/*      */       
/* 1994 */       if (!destContainer.canContain(getEntityType()))
/*      */       {
/* 1996 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CONTAIN_CHILDTYPE, new Object[] { destContainer.getObjectIdentity(), getEntityType() });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2003 */       RMDomain jarmDomain = getRepository().getDomain();
/* 2004 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 2005 */       UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */       
/* 2007 */       RMProperties props = getProperties();
/* 2008 */       props.putStringValue("ReasonForReclassification", reasonForMove);
/*      */       
/*      */ 
/* 2011 */       props.putGuidValue("DisposalScheduleInheritedFrom", null);
/*      */       
/*      */ 
/* 2014 */       P8CE_BaseContainerImpl originalParentContainer = (P8CE_BaseContainerImpl)getParent();
/* 2015 */       if (originalParentContainer.adjustForChildRemoval(this))
/*      */       {
/* 2017 */         jaceUpdateBatch.add(originalParentContainer.jaceFolder, P8CE_Util.CEPF_Empty);
/*      */       }
/*      */       
/* 2020 */       List<Integer> newDestParentAllowedTypes = new ArrayList();
/* 2021 */       List<Integer> currentDestParentAllowedTypes = destContainer.getProperties().getIntegerListValue("AllowedRMTypes");
/*      */       
/*      */ 
/*      */ 
/* 2025 */       if ((this instanceof RecordCategory))
/*      */       {
/*      */ 
/*      */ 
/* 2029 */         for (Iterator i$ = currentDestParentAllowedTypes.iterator(); i$.hasNext();) { int allowedType = ((Integer)i$.next()).intValue();
/*      */           
/* 2031 */           if ((allowedType != EntityType.ElectronicRecordFolder.getIntValue()) && (allowedType != EntityType.HybridRecordFolder.getIntValue()) && (allowedType != EntityType.PhysicalContainer.getIntValue()) && (allowedType != EntityType.PhysicalRecordFolder.getIntValue()) && (allowedType != EntityType.RecordFolder.getIntValue()))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2037 */             newDestParentAllowedTypes.add(Integer.valueOf(allowedType));
/*      */           }
/*      */         }
/* 2040 */         destContainer.getProperties().putIntegerListValue("AllowedRMTypes", newDestParentAllowedTypes);
/*      */       }
/* 2042 */       else if ((this instanceof RecordFolder))
/*      */       {
/*      */ 
/*      */ 
/* 2046 */         for (Iterator i$ = currentDestParentAllowedTypes.iterator(); i$.hasNext();) { int allowedType = ((Integer)i$.next()).intValue();
/*      */           
/* 2048 */           if (allowedType != EntityType.RecordCategory.getIntValue())
/* 2049 */             newDestParentAllowedTypes.add(Integer.valueOf(allowedType));
/*      */         }
/* 2051 */         destContainer.getProperties().putIntegerListValue("AllowedRMTypes", newDestParentAllowedTypes);
/*      */       }
/*      */       
/* 2054 */       this.jaceFolder.move(((P8CE_BaseContainerImpl)destContainer).jaceFolder);
/*      */       
/* 2056 */       jaceUpdateBatch.add(this.jaceFolder, P8CE_Util.CEPF_Empty);
/*      */       
/* 2058 */       Folder jaceSrcFolder = originalParentContainer.jaceFolder;
/* 2059 */       Folder jaceDestFolder = ((P8CE_BaseContainerImpl)destContainer).jaceFolder;
/* 2060 */       P8CE_AuditServices.attachRelocateAuditEvent(this, jaceSrcFolder, jaceDestFolder, reasonForMove, AuditStatus.Success, false);
/*      */       
/* 2062 */       jaceUpdateBatch.add(jaceDestFolder, P8CE_Util.CEPF_Empty);
/*      */       
/*      */ 
/* 2065 */       long startTime = System.currentTimeMillis();
/* 2066 */       jaceUpdateBatch.updateBatch();
/* 2067 */       long endTime = System.currentTimeMillis();
/* 2068 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/* 2070 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2074 */       P8CE_AuditServices.attachRelocateAuditEvent(this, null, null, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/* 2075 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2079 */       P8CE_AuditServices.attachRelocateAuditEvent(this, null, null, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/* 2080 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_CANNOT_MOVE_OBJECT, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */     finally
/*      */     {
/* 2084 */       if (establishedSubject) {
/* 2085 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RecordFolder addRecordFolder(String classIdent, RMProperties props, List<RMPermission> perms)
/*      */   {
/* 2094 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms });
/* 2095 */     RecordFolder result = addRecordFolder(classIdent, props, perms, null);
/* 2096 */     Tracer.traceMethodExit(new Object[] { result });
/* 2097 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordFolder addRecordFolder(String classIdent, RMProperties props, List<RMPermission> perms, String idStr)
/*      */   {
/* 2105 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms, idStr });
/* 2106 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2109 */       establishedSubject = P8CE_Util.associateSubject();
/* 2110 */       resolve();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2115 */       boolean createChildVolume = super.validateAndPrepareNewRecordFolder(classIdent, props, perms);
/*      */       
/*      */ 
/* 2118 */       RMDomain jarmDomain = getRepository().getDomain();
/* 2119 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 2120 */       UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */       
/* 2122 */       Folder jaceParentBaseFolder = (Folder)getJaceBaseObject();
/* 2123 */       ObjectStore jaceObjStore = jaceParentBaseFolder.getObjectStore();
/*      */       
/* 2125 */       Id newId = P8CE_Util.processIdStr(idStr);
/* 2126 */       Folder newJaceBaseFolder = Factory.Folder.createInstance(jaceObjStore, classIdent, newId);
/*      */       
/* 2128 */       Properties jaceProps = newJaceBaseFolder.getProperties();
/* 2129 */       P8CE_Util.convertToJaceProperties(props, jaceProps);
/*      */       
/* 2131 */       jaceProps.putValue("Parent", jaceParentBaseFolder);
/*      */       
/* 2133 */       if (perms != null)
/*      */       {
/* 2135 */         AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(perms);
/* 2136 */         newJaceBaseFolder.set_Permissions(jacePerms);
/*      */       }
/*      */       
/* 2139 */       PropertyFilter jacePF = new PropertyFilter();
/* 2140 */       for (FilterElement fe : P8CE_RecordFolderImpl.getMandatoryJaceFEs()) {
/* 2141 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*      */       
/* 2144 */       jaceUpdateBatch.add(newJaceBaseFolder, jacePF);
/*      */       
/*      */ 
/* 2147 */       jaceUpdateBatch.add(jaceParentBaseFolder, jacePF);
/*      */       
/* 2149 */       if (createChildVolume)
/*      */       {
/*      */ 
/* 2152 */         EntityType recFolderType = getEntityTypeForClass(getRepository(), classIdent);
/* 2153 */         Folder jaceVolumeBaseFolder = defineFirstJaceVolume("Volume", recFolderType, newJaceBaseFolder, 1);
/* 2154 */         if (jaceVolumeBaseFolder != null) {
/* 2155 */           jaceUpdateBatch.add(jaceVolumeBaseFolder, P8CE_Util.CEPF_Empty);
/*      */         }
/*      */       }
/*      */       
/* 2159 */       long startTime = System.currentTimeMillis();
/* 2160 */       jaceUpdateBatch.updateBatch();
/* 2161 */       long endTime = System.currentTimeMillis();
/* 2162 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/*      */ 
/* 2165 */       RecordFolder result = (RecordFolder)P8CE_RecordFolderImpl.getGenerator().create(getRepository(), newJaceBaseFolder);
/*      */       
/* 2167 */       Tracer.traceMethodExit(new Object[] { result });
/* 2168 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2172 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2176 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_ADD_RECORDFOLDER_FAILED, new Object[] { getObjectIdentity(), getEntityType() });
/*      */     }
/*      */     finally
/*      */     {
/* 2180 */       if (establishedSubject) {
/* 2181 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordVolume addRecordVolume(String classIdent, String volumeName, RMProperties props, List<RMPermission> perms)
/*      */   {
/* 2191 */     Tracer.traceMethodEntry(new Object[] { classIdent, volumeName, props, perms });
/* 2192 */     RecordVolume result = addRecordVolume(classIdent, volumeName, props, perms, null);
/* 2193 */     Tracer.traceMethodExit(new Object[] { result });
/* 2194 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordVolume addRecordVolume(String classIdent, String volumeName, RMProperties props, List<RMPermission> perms, String idStr)
/*      */   {
/* 2203 */     Tracer.traceMethodEntry(new Object[] { classIdent, volumeName, props, perms, idStr });
/* 2204 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2207 */       establishedSubject = P8CE_Util.associateSubject();
/* 2208 */       resolve();
/*      */       
/* 2210 */       String volClassIdent = classIdent;
/* 2211 */       if ((volClassIdent == null) || (volClassIdent.trim().length() == 0)) {
/* 2212 */         volClassIdent = "Volume";
/*      */       }
/* 2214 */       if (props == null)
/*      */       {
/* 2216 */         props = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2221 */       super.validateAndPrepareNewRecordVolume(volClassIdent, volumeName, props, perms);
/*      */       
/*      */ 
/*      */ 
/* 2225 */       boolean updateCurrentActiveVolume = false;
/* 2226 */       RecordVolume currentActiveVolume = ((RecordVolumeContainer)this).getActiveRecordVolume();
/* 2227 */       if ((currentActiveVolume != null) && (currentActiveVolume.isOpen()))
/*      */       {
/* 2229 */         updateCurrentActiveVolume = true;
/* 2230 */         RMProperties currentProps = currentActiveVolume.getProperties();
/* 2231 */         currentProps.putDateTimeValue("DateClosed", new Date());
/* 2232 */         String closedByValue = props.getStringValue("Reviewer");
/* 2233 */         currentProps.putStringValue("ClosedBy", closedByValue);
/* 2234 */         currentProps.putStringValue("ReasonForClose", RALBaseContainer.AutomaticVolumeCloseReason);
/* 2235 */         currentProps.putDateTimeValue("ReOpenedDate", null);
/*      */       }
/*      */       
/*      */ 
/* 2239 */       RMDomain jarmDomain = getRepository().getDomain();
/* 2240 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 2241 */       UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */       
/* 2243 */       Folder jaceParentBaseFolder = (Folder)getJaceBaseObject();
/* 2244 */       ObjectStore jaceObjStore = jaceParentBaseFolder.getObjectStore();
/*      */       
/* 2246 */       Id newId = P8CE_Util.processIdStr(idStr);
/* 2247 */       Folder newJaceBaseFolder = Factory.Folder.createInstance(jaceObjStore, volClassIdent, newId);
/*      */       
/* 2249 */       Properties jaceProps = newJaceBaseFolder.getProperties();
/* 2250 */       P8CE_Util.convertToJaceProperties(props, jaceProps);
/*      */       
/* 2252 */       jaceProps.putValue("Parent", jaceParentBaseFolder);
/*      */       
/* 2254 */       if (perms != null)
/*      */       {
/* 2256 */         AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(perms);
/* 2257 */         newJaceBaseFolder.set_Permissions(jacePerms);
/*      */       }
/*      */       
/* 2260 */       PropertyFilter jacePF = new PropertyFilter();
/* 2261 */       for (FilterElement fe : P8CE_RecordVolumeImpl.getMandatoryJaceFEs()) {
/* 2262 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*      */       
/* 2265 */       jaceUpdateBatch.add(newJaceBaseFolder, jacePF);
/*      */       
/* 2267 */       if (updateCurrentActiveVolume)
/*      */       {
/* 2269 */         Folder currentActiveVolBase = ((P8CE_RecordVolumeImpl)currentActiveVolume).jaceFolder;
/* 2270 */         jaceUpdateBatch.add(currentActiveVolBase, jacePF);
/*      */       }
/*      */       
/*      */ 
/* 2274 */       long startTime = System.currentTimeMillis();
/* 2275 */       jaceUpdateBatch.updateBatch();
/* 2276 */       long endTime = System.currentTimeMillis();
/* 2277 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/*      */ 
/* 2280 */       RecordVolume result = (RecordVolume)P8CE_RecordVolumeImpl.getGenerator().create(getRepository(), newJaceBaseFolder);
/*      */       
/* 2282 */       Tracer.traceMethodExit(new Object[] { result });
/* 2283 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2287 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2291 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_ADD_RECORDVOLUME_FAILED, new Object[] { getObjectIdentity(), getEntityType() });
/*      */     }
/*      */     finally
/*      */     {
/* 2295 */       if (establishedSubject) {
/* 2296 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Folder defineFirstJaceVolume(String classIdent, EntityType parentEntityType, Folder parentJaceFolder, int nameSuffixCount)
/*      */   {
/* 2316 */     Tracer.traceMethodEntry(new Object[] { classIdent, parentEntityType, parentJaceFolder, Integer.valueOf(nameSuffixCount) });
/*      */     
/* 2318 */     FilePlanRepository fpRepository = (FilePlanRepository)getRepository();
/* 2319 */     RMClassDescription volumeClassDesc = getClassDescription(fpRepository, classIdent);
/* 2320 */     if (volumeClassDesc == null) {
/* 2321 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTION_FAILED, new Object[] { fpRepository.getName(), classIdent });
/*      */     }
/* 2323 */     List<RMPropertyDescription> propDescs = volumeClassDesc.getPropertyDescriptions();
/* 2324 */     List<String> applicablePropNames = new ArrayList();
/* 2325 */     for (RMPropertyDescription propDesc : propDescs)
/*      */     {
/* 2327 */       String propSymName = propDesc.getSymbolicName();
/* 2328 */       if (!Special1stVolumePropNames.contains(propSymName))
/*      */       {
/* 2330 */         if ((!"AGGREGATION".equalsIgnoreCase(propSymName)) && 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2335 */           (!propDesc.isHidden()) && (!propDesc.isReadOnly()) && (!propDesc.isSystemGenerated()) && (!propDesc.isSystemOwned()) && 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2340 */           ((!propSymName.equalsIgnoreCase("MethodofDestruction")) || (parentEntityType != EntityType.ElectronicRecordFolder)) && (
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2346 */           (!propSymName.equalsIgnoreCase("HomeLocation")) || ((parentEntityType == EntityType.PhysicalRecordFolder) && (parentEntityType == EntityType.HybridRecordFolder))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2352 */           String descText = propDesc.getDescriptiveText();
/* 2353 */           if ((descText != null) && (descText.toUpperCase().indexOf("RMSYSTEM") != -1)) {}
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else {
/* 2359 */         applicablePropNames.add(propSymName);
/*      */       }
/*      */     }
/* 2362 */     Folder jaceVolumeBase = Factory.Folder.createInstance(parentJaceFolder.getObjectStore(), classIdent);
/* 2363 */     PropertiesImpl jaceVolumeProps = (PropertiesImpl)jaceVolumeBase.getProperties();
/*      */     
/*      */ 
/* 2366 */     Properties parentFolderJaceProps = parentJaceFolder.getProperties();
/* 2367 */     for (String propSymName : applicablePropNames)
/*      */     {
/* 2369 */       if (parentFolderJaceProps.isPropertyPresent(propSymName))
/*      */       {
/* 2371 */         jaceVolumeProps.put(parentFolderJaceProps.get(propSymName));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2378 */     String volSuffixPattern = "00000";
/* 2379 */     Map<String, SystemConfiguration> systemConfigs = fpRepository.getSystemConfigurations();
/* 2380 */     if (systemConfigs != null)
/*      */     {
/* 2382 */       SystemConfiguration namingPatternConfig = (SystemConfiguration)systemConfigs.get("Volume Pattern Suffix");
/* 2383 */       if (namingPatternConfig != null)
/*      */       {
/* 2385 */         String value = namingPatternConfig.getPropertyValue();
/* 2386 */         if ((value != null) && (value.trim().length() > 0))
/* 2387 */           volSuffixPattern = value.trim();
/*      */       }
/*      */     }
/* 2390 */     DecimalFormat df = new DecimalFormat(volSuffixPattern);
/* 2391 */     String parentName = parentJaceFolder.getProperties().getStringValue("FolderName");
/* 2392 */     String volName = parentName + "-" + df.format(nameSuffixCount);
/* 2393 */     jaceVolumeProps.putValue("FolderName", volName);
/* 2394 */     jaceVolumeProps.putValue("RecordFolderIdentifier", volName);
/* 2395 */     jaceVolumeProps.putValue("VolumeName", volName);
/*      */     
/*      */ 
/* 2398 */     String volContainerType = super.determineVolumeContainerType(parentEntityType);
/* 2399 */     jaceVolumeProps.putValue("ContainerType", volContainerType);
/*      */     
/*      */ 
/* 2402 */     Integer32List allowedRMTypesValue = Factory.Integer32List.createList();
/* 2403 */     if (parentEntityType == EntityType.PhysicalRecordFolder)
/*      */     {
/* 2405 */       allowedRMTypesValue.add(Integer.valueOf(EntityType.PhysicalRecord.getIntValue()));
/*      */     }
/*      */     else
/*      */     {
/* 2409 */       allowedRMTypesValue.add(Integer.valueOf(EntityType.ElectronicRecord.getIntValue()));
/* 2410 */       allowedRMTypesValue.add(Integer.valueOf(EntityType.EmailRecord.getIntValue()));
/* 2411 */       allowedRMTypesValue.add(Integer.valueOf(EntityType.PhysicalRecord.getIntValue()));
/*      */     }
/* 2413 */     jaceVolumeProps.putValue("AllowedRMTypes", allowedRMTypesValue);
/*      */     
/*      */ 
/* 2416 */     jaceVolumeProps.putValue("Parent", parentJaceFolder);
/*      */     
/* 2418 */     Tracer.traceMethodExit(new Object[] { jaceVolumeBase });
/* 2419 */     return jaceVolumeBase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int findLastVolumeSuffix()
/*      */   {
/* 2430 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/* 2432 */     String idStr = getObjectIdentity();
/* 2433 */     StringBuilder sb = new StringBuilder();
/* 2434 */     sb.append("SELECT v.").append("RecordFolderIdentifier").append(", v.").append("VolumeName");
/* 2435 */     sb.append(" FROM ").append("Volume").append(" v");
/* 2436 */     sb.append(" WHERE v.").append("Parent").append(" = OBJECT('").append(idStr).append("')");
/*      */     
/* 2438 */     String sqlStmt = sb.toString();
/* 2439 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 2440 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/* 2441 */     PropertyFilter pf = new PropertyFilter();
/* 2442 */     String desiredProps = "RecordFolderIdentifier VolumeName";
/* 2443 */     pf.addIncludeProperty(0, null, null, desiredProps, null);
/* 2444 */     Integer pageSize = null;
/* 2445 */     Boolean continuable = Boolean.TRUE;
/* 2446 */     RepositoryRowSet resultRows = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, pf, continuable);
/*      */     
/* 2448 */     RepositoryRow row = null;
/* 2449 */     Properties jaceProps = null;
/* 2450 */     String namePropValue = null;
/* 2451 */     int lastDashPos = -1;
/* 2452 */     String suffixStr = null;
/* 2453 */     int suffixInt = 0;
/* 2454 */     int currentMax = 0;
/* 2455 */     PageIterator jacePI = resultRows.pageIterator();
/* 2456 */     while (jacePI.nextPage())
/*      */     {
/* 2458 */       Object[] currentPage = jacePI.getCurrentPage();
/* 2459 */       for (Object obj : currentPage)
/*      */       {
/* 2461 */         row = (RepositoryRow)obj;
/* 2462 */         if (row != null)
/*      */         {
/* 2464 */           jaceProps = row.getProperties();
/*      */           
/* 2466 */           if (jaceProps.isPropertyPresent("RecordFolderIdentifier"))
/*      */           {
/* 2468 */             namePropValue = jaceProps.getStringValue("RecordFolderIdentifier");
/* 2469 */             if ((namePropValue != null) && ((lastDashPos = namePropValue.lastIndexOf('-')) != -1) && (lastDashPos < namePropValue.length() + 1))
/*      */             {
/*      */ 
/*      */ 
/* 2473 */               suffixStr = namePropValue.substring(lastDashPos + 1);
/*      */             }
/*      */           }
/*      */           
/* 2477 */           if ((suffixStr == null) || (suffixStr.trim().length() == 0))
/*      */           {
/* 2479 */             if (jaceProps.isPropertyPresent("VolumeName"))
/*      */             {
/* 2481 */               namePropValue = jaceProps.getStringValue("VolumeName");
/* 2482 */               if ((namePropValue != null) && ((lastDashPos = namePropValue.lastIndexOf('-')) != -1) && (lastDashPos < namePropValue.length() + 1))
/*      */               {
/*      */ 
/*      */ 
/* 2486 */                 suffixStr = namePropValue.substring(lastDashPos + 1);
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 2491 */           if ((suffixStr != null) && (suffixStr.trim().length() > 0))
/*      */           {
/* 2493 */             suffixInt = Integer.parseInt(suffixStr, 10);
/* 2494 */             if (suffixInt > currentMax) {
/* 2495 */               currentMax = suffixInt;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2501 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(currentMax) });
/* 2502 */     return currentMax;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyChildOnHold()
/*      */   {
/* 2510 */     Tracer.traceMethodEntry(new Object[0]);
/* 2511 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2514 */       establishedSubject = P8CE_Util.associateSubject();
/* 2515 */       boolean result = false;
/*      */       
/*      */ 
/* 2518 */       String idStr = getObjectIdentity();
/* 2519 */       StringBuilder sb = new StringBuilder();
/* 2520 */       sb.append("SELECT TOP 1 f.").append("Id");
/* 2521 */       sb.append(" FROM ").append("RMFolder").append(" f");
/* 2522 */       sb.append(" WHERE f.This INSUBFOLDER '").append(idStr).append("'");
/* 2523 */       sb.append("   AND f.").append("OnHold").append(" = TRUE");
/* 2524 */       sb.append("   AND f.").append("IsDeleted").append(" = FALSE ");
/*      */       
/* 2526 */       String sqlStmt = sb.toString();
/* 2527 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 2528 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/* 2529 */       RepositoryRowSet resultRows = jaceSearchScope.fetchRows(jaceSearchSQL, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 2530 */       long startTime; if ((resultRows != null) && (!resultRows.isEmpty()))
/*      */       {
/* 2532 */         result = true;
/*      */       }
/*      */       else
/*      */       {
/* 2536 */         sb = new StringBuilder();
/* 2537 */         sb.append("SELECT TOP 1 r.").append("Id");
/* 2538 */         sb.append(" FROM ").append("RecordInfo").append(" r");
/* 2539 */         sb.append(" WHERE r.This INSUBFOLDER '").append(idStr).append("'");
/* 2540 */         sb.append("   AND r.").append("OnHold").append(" = TRUE");
/* 2541 */         sb.append("   AND r.").append("IsDeleted").append(" = FALSE ");
/*      */         
/* 2543 */         sqlStmt = sb.toString();
/* 2544 */         jaceSearchSQL = new SearchSQL(sqlStmt);
/* 2545 */         jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */         
/* 2547 */         startTime = System.currentTimeMillis();
/* 2548 */         resultRows = jaceSearchScope.fetchRows(jaceSearchSQL, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 2549 */         long endTime = System.currentTimeMillis();
/*      */         
/* 2551 */         Boolean elementCountIndicator = null;
/* 2552 */         if (Tracer.isMediumTraceEnabled())
/*      */         {
/* 2554 */           elementCountIndicator = resultRows != null ? Boolean.valueOf(resultRows.isEmpty()) : null;
/*      */         }
/* 2556 */         Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, resultRows, new Object[] { sqlStmt, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */         
/* 2558 */         if ((resultRows != null) && (!resultRows.isEmpty()))
/*      */         {
/* 2560 */           result = true;
/*      */         }
/*      */       }
/*      */       
/* 2564 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2565 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2569 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2573 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2577 */       if (establishedSubject) {
/* 2578 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public PageableSet<Record> getRecords(Integer pageSize) {
/* 2584 */     Tracer.traceMethodEntry(new Object[] { pageSize });
/* 2585 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2588 */       establishedSubject = P8CE_Util.associateSubject();
/* 2589 */       PageableSet<Record> result = null;
/*      */       
/*      */ 
/*      */ 
/* 2593 */       List<FilterElement> mandatoryRecordFEs = P8CE_RecordImpl.getMandatoryJaceFEs();
/* 2594 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryRecordFEs);
/*      */       
/* 2596 */       String containerId = getObjectIdentity();
/*      */       
/* 2598 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordInfo", "r");
/*      */       
/* 2600 */       sb.append(" INNER JOIN ").append("ReferentialContainmentRelationship").append(" rcr ON r.This = rcr.").append("Head");
/* 2601 */       sb.append(" WHERE rcr.").append("Tail").append(" = OBJECT('").append(containerId).append("')");
/* 2602 */       sb.append(" AND r.").append("IsDeleted").append(" = FALSE ");
/* 2603 */       sb.append(" ORDER BY r.").append("DocumentTitle");
/* 2604 */       String sqlStatement = sb.toString();
/*      */       
/* 2606 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 2607 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */       
/*      */ 
/* 2610 */       Boolean continuable = Boolean.TRUE;
/* 2611 */       long startTime = System.currentTimeMillis();
/* 2612 */       IndependentObjectSet jaceDocumentSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 2613 */       long stopTime = System.currentTimeMillis();
/*      */       
/* 2615 */       Boolean elementCountIndicator = null;
/* 2616 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 2618 */         elementCountIndicator = jaceDocumentSet != null ? Boolean.valueOf(jaceDocumentSet.isEmpty()) : null;
/*      */       }
/* 2620 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceDocumentSet, new Object[] { sqlStatement });
/*      */       
/*      */ 
/* 2623 */       boolean supportsPaging = true;
/* 2624 */       IGenerator<Record> generator = P8CE_RecordImpl.getGenerator();
/* 2625 */       result = new P8CE_PageableSetImpl(this.repository, jaceDocumentSet, supportsPaging, generator);
/*      */       
/* 2627 */       Tracer.traceMethodExit(new Object[] { result });
/* 2628 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2632 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2636 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2640 */       if (establishedSubject) {
/* 2641 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PageableSet<Container> getImmediateSubContainers(Integer pageSize)
/*      */   {
/* 2650 */     Tracer.traceMethodEntry(new Object[0]);
/* 2651 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2654 */       establishedSubject = P8CE_Util.associateSubject();
/* 2655 */       PageableSet<Container> result = null;
/*      */       
/*      */ 
/*      */ 
/* 2659 */       List<FilterElement> mandatoryRecordFEs = getMandatoryJaceFEs();
/* 2660 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryRecordFEs);
/*      */       
/* 2662 */       String containerId = getObjectIdentity();
/*      */       
/* 2664 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RMFolder", "rf");
/*      */       
/* 2666 */       sb.append(" WHERE (rf.").append("Parent").append(" = OBJECT('").append(containerId).append("'))");
/* 2667 */       sb.append(" AND ((rf.").append("IsDeleted").append(" IS NULL) OR (rf.").append("IsDeleted").append(" = FALSE)) ");
/* 2668 */       sb.append(" ORDER BY rf.").append("FolderName");
/* 2669 */       String sqlStatement = sb.toString();
/*      */       
/* 2671 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 2672 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */       
/*      */ 
/* 2675 */       Boolean continuable = Boolean.TRUE;
/* 2676 */       long startTime = System.currentTimeMillis();
/* 2677 */       IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 2678 */       long stopTime = System.currentTimeMillis();
/*      */       
/* 2680 */       Boolean elementCountIndicator = null;
/* 2681 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 2683 */         elementCountIndicator = jaceFolderSet != null ? Boolean.valueOf(jaceFolderSet.isEmpty()) : null;
/*      */       }
/* 2685 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement });
/*      */       
/*      */ 
/* 2688 */       boolean supportsPaging = true;
/* 2689 */       IGenerator<Container> generator = getBaseContainerGenerator();
/* 2690 */       result = new P8CE_PageableSetImpl(this.repository, jaceFolderSet, supportsPaging, generator);
/*      */       
/* 2692 */       Tracer.traceMethodExit(new Object[] { result });
/* 2693 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2697 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 2701 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 2705 */       if (establishedSubject) {
/* 2706 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void close(String reasonForClose)
/*      */   {
/* 2715 */     Tracer.traceMethodEntry(new Object[] { reasonForClose });
/*      */     
/* 2717 */     FilePlanRepository repository = (FilePlanRepository)getRepository();
/* 2718 */     List<String> entityIdents = new ArrayList(1);
/* 2719 */     entityIdents.add(getObjectIdentity());
/*      */     
/* 2721 */     List<BulkItemResult> results = BulkOperation.closeContainers(repository, entityIdents, reasonForClose);
/* 2722 */     if ((results != null) && (results.size() >= 1))
/*      */     {
/* 2724 */       BulkItemResult bir = (BulkItemResult)results.get(0);
/* 2725 */       if (bir.wasSuccessful())
/*      */       {
/* 2727 */         refresh();
/*      */ 
/*      */ 
/*      */       }
/* 2731 */       else if (bir.getException() != null) {
/* 2732 */         throw bir.getException();
/*      */       }
/*      */     }
/* 2735 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reopen()
/*      */   {
/* 2747 */     Tracer.traceMethodEntry(new Object[0]);
/* 2748 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2751 */       establishedSubject = P8CE_Util.associateSubject();
/* 2752 */       super.reopen();
/* 2753 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2757 */       if (establishedSubject) {
/* 2758 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void fileRecord(String recordIdent)
/*      */   {
/* 2768 */     Tracer.traceMethodEntry(new Object[] { recordIdent });
/* 2769 */     Util.ckInvalidStrParam("recordIdent", recordIdent);
/*      */     
/*      */ 
/* 2772 */     FilePlanRepository repository = (FilePlanRepository)getRepository();
/* 2773 */     List<String> entityIdents = new ArrayList(1);
/* 2774 */     entityIdents.add(recordIdent);
/* 2775 */     String destinationContainerIdent = getObjectIdentity();
/*      */     
/* 2777 */     List<BulkItemResult> results = BulkOperation.fileRecords(repository, entityIdents, destinationContainerIdent);
/* 2778 */     if ((results != null) && (results.size() >= 1))
/*      */     {
/* 2780 */       BulkItemResult bir = (BulkItemResult)results.get(0);
/* 2781 */       if ((!bir.wasSuccessful()) && (bir.getException() != null))
/* 2782 */         throw bir.getException();
/*      */     }
/* 2784 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void fileRecord(Record record)
/*      */   {
/* 2792 */     Tracer.traceMethodEntry(new Object[] { record });
/* 2793 */     Util.ckNullObjParam("record", record);
/* 2794 */     fileRecord(record.getObjectIdentity());
/* 2795 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unfileRecord(String recordIdent)
/*      */   {
/* 2803 */     Tracer.traceMethodEntry(new Object[] { recordIdent });
/* 2804 */     Util.ckInvalidStrParam("recordIdent", recordIdent);
/*      */     
/* 2806 */     FilePlanRepository fpRepos = (FilePlanRepository)getRepository();
/* 2807 */     Record record = RMFactory.Record.getInstance(fpRepos, recordIdent, EntityType.Record);
/* 2808 */     boolean forceDispositionStateReset = false;
/* 2809 */     unfileRecord(record, forceDispositionStateReset, null);
/*      */     
/* 2811 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unfileRecord(Record record)
/*      */   {
/* 2819 */     boolean forceDispositionStateReset = false;
/* 2820 */     unfileRecord(record, forceDispositionStateReset, null);
/*      */   }
/*      */   
/*      */   void unfileRecord(Record record, boolean forceDispositionStateReset, UpdatingBatch existingJaceUB)
/*      */   {
/* 2825 */     Tracer.traceMethodEntry(new Object[] { record, Boolean.valueOf(forceDispositionStateReset) });
/* 2826 */     Util.ckNullObjParam("record", record);
/* 2827 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2830 */       establishedSubject = P8CE_Util.associateSubject();
/* 2831 */       ((RALBaseRecord)record).refresh(new String[] { "AssociatedRecordType", "CutoffInheritedFrom" });
/* 2832 */       resolve();
/*      */       
/*      */ 
/*      */ 
/* 2836 */       Container newSecurityFolder = validateRecordUnfileability(record);
/*      */       
/*      */ 
/* 2839 */       UpdatingBatch jaceUB = existingJaceUB;
/* 2840 */       if (jaceUB == null)
/*      */       {
/* 2842 */         RMDomain jarmDomain = getRepository().getDomain();
/* 2843 */         Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 2844 */         jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */       }
/*      */       
/* 2847 */       com.filenet.api.core.Document jaceRecordBase = (com.filenet.api.core.Document)((P8CE_RecordImpl)record).getJaceBaseObject();
/* 2848 */       ReferentialContainmentRelationship rcr = this.jaceFolder.unfile(jaceRecordBase);
/* 2849 */       jaceUB.add(rcr, P8CE_Util.CEPF_Empty);
/*      */       
/*      */ 
/* 2852 */       if (newSecurityFolder != null)
/*      */       {
/* 2854 */         Folder jaceSecFolderBase = (Folder)((JaceBasable)newSecurityFolder).getJaceBaseObject();
/* 2855 */         jaceRecordBase.getProperties().putValue("SecurityFolder", jaceSecFolderBase);
/* 2856 */         PropertyFilter recordPF = P8CE_Util.buildMandatoryJacePF((JaceBasable)record);
/* 2857 */         jaceUB.add(jaceRecordBase, recordPF);
/*      */       }
/*      */       
/* 2860 */       if (forceDispositionStateReset)
/*      */       {
/* 2862 */         ((RALBaseRecord)record).resetDispositionData(record.getProperties());
/*      */       }
/* 2864 */       else if (jaceRecordBase.getProperties().getEngineObjectValue("AssociatedRecordType") == null)
/*      */       {
/* 2866 */         Id cutoffInheritedFromId = jaceRecordBase.getProperties().getIdValue("CutoffInheritedFrom");
/* 2867 */         if ((cutoffInheritedFromId != null) && (cutoffInheritedFromId.toString().equalsIgnoreCase(getObjectIdentity())))
/*      */         {
/*      */ 
/* 2870 */           ((RALBaseRecord)record).resetDispositionData(record.getProperties());
/*      */         }
/*      */       }
/*      */       
/* 2874 */       if (existingJaceUB == null)
/*      */       {
/* 2876 */         long startTime = System.currentTimeMillis();
/* 2877 */         jaceUB.updateBatch();
/* 2878 */         long endTime = System.currentTimeMillis();
/* 2879 */         Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       }
/*      */       
/* 2882 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2886 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 2890 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 2894 */       if (establishedSubject) {
/* 2895 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refresh()
/*      */   {
/* 2904 */     Tracer.traceMethodEntry(new Object[0]);
/* 2905 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2908 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 2910 */       long startTime = System.currentTimeMillis();
/* 2911 */       this.jaceFolder.refresh();
/* 2912 */       long endTime = System.currentTimeMillis();
/* 2913 */       Tracer.traceExtCall("Folder.refresh", startTime, endTime, null, null, new Object[0]);
/*      */       
/* 2915 */       updateAllowedEntityTypes();
/* 2916 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2920 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 2924 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 2928 */       if (establishedSubject) {
/* 2929 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refresh(RMPropertyFilter jarmFilter)
/*      */   {
/* 2938 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 2939 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2942 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 2946 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 2947 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*      */       
/* 2949 */       long startTime = System.currentTimeMillis();
/* 2950 */       this.jaceFolder.refresh(jacePF);
/* 2951 */       long endTime = System.currentTimeMillis();
/* 2952 */       Tracer.traceExtCall("Folder.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       
/* 2954 */       updateAllowedEntityTypes();
/* 2955 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2959 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 2963 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 2967 */       if (establishedSubject) {
/* 2968 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refresh(String[] symbolicPropertyNames)
/*      */   {
/* 2977 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 2978 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2981 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 2984 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 2985 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 2986 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 2987 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 2988 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*      */       
/* 2990 */       long startTime = System.currentTimeMillis();
/* 2991 */       this.jaceFolder.refresh(jacePF);
/* 2992 */       long endTime = System.currentTimeMillis();
/* 2993 */       Tracer.traceExtCall("Folder.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       
/* 2995 */       updateAllowedEntityTypes();
/* 2996 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 3000 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 3004 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 3008 */       if (establishedSubject) {
/* 3009 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Integer getAccessAllowed()
/*      */   {
/* 3018 */     return this.jaceFolder.getAccessAllowed();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Record declare(String classIdent, RMProperties props, List<RMPermission> perms, List<RecordContainer> additionalContainers)
/*      */   {
/* 3027 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms, additionalContainers });
/* 3028 */     Record result = declare(classIdent, props, perms, additionalContainers, (List)null);
/*      */     
/* 3030 */     Tracer.traceMethodExit(new Object[] { result });
/* 3031 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Record declare(String classIdent, RMProperties props, List<RMPermission> perms, List<RecordContainer> additionalContainers, ContentRepository contentRepository, List<String> contentIdents)
/*      */   {
/* 3041 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms, additionalContainers, contentRepository, contentIdents });
/* 3042 */     Util.ckInvalidStrParam("classIdent", classIdent);
/*      */     
/* 3044 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3047 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 3050 */       if ((contentIdents != null) && (contentIdents.size() > 0) && (contentRepository == null)) {
/* 3051 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_NO_CONTENT_REPOSITORY, new Object[0]);
/*      */       }
/* 3053 */       List<ContentItem> associatedContent = null;
/* 3054 */       RetrievingBatch jaceBatch; IGenerator<ContentItem> generator; com.filenet.api.core.Document jaceDoc; if ((contentRepository != null) && (contentIdents.size() > 0))
/*      */       {
/*      */ 
/*      */ 
/* 3058 */         RMDomain jarmDomain = getRepository().getDomain();
/* 3059 */         Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 3060 */         ObjectStore jaceObjStore = ((P8CE_ContentRepositoryImpl)contentRepository).getJaceObjectStore();
/* 3061 */         jaceBatch = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/* 3062 */         List<BatchItemHandle> jaceBIHs = new ArrayList(contentIdents.size());
/* 3063 */         PropertyFilter jacePF = new PropertyFilter();
/* 3064 */         for (FilterElement fe : P8CE_ContentItemImpl.getMandatoryJaceFEs()) {
/* 3065 */           jacePF.addIncludeProperty(fe);
/*      */         }
/* 3067 */         com.filenet.api.core.Document jaceDocument = null;
/* 3068 */         for (String contentIdent : contentIdents)
/*      */         {
/* 3070 */           if (Id.isId(contentIdent))
/*      */           {
/* 3072 */             Id jaceDocId = new Id(contentIdent);
/* 3073 */             jaceDocument = Factory.Document.getInstance(jaceObjStore, "Document", jaceDocId);
/*      */           }
/*      */           else
/*      */           {
/* 3077 */             jaceDocument = Factory.Document.getInstance(jaceObjStore, "Document", contentIdent);
/*      */           }
/*      */           
/* 3080 */           jaceBIHs.add(jaceBatch.add(jaceDocument, jacePF));
/*      */         }
/*      */         
/*      */ 
/* 3084 */         jaceBatch.retrieveBatch();
/*      */         
/*      */ 
/*      */ 
/* 3088 */         generator = P8CE_ContentItemImpl.getContentItemGenerator();
/* 3089 */         associatedContent = new ArrayList(contentIdents.size());
/* 3090 */         jaceDoc = null;
/* 3091 */         for (BatchItemHandle jaceBIH : jaceBIHs)
/*      */         {
/* 3093 */           jaceDoc = (com.filenet.api.core.Document)jaceBIH.getObject();
/* 3094 */           if (jaceBIH.hasException())
/*      */           {
/* 3096 */             Exception ex = jaceBIH.getException();
/* 3097 */             throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_BATCH_CONTENT_ITEM_RETRIEVAL_ERROR, new Object[] { jaceDoc.getObjectReference() });
/*      */           }
/*      */           
/*      */ 
/* 3101 */           associatedContent.add(generator.create(contentRepository, jaceDoc));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3106 */       boolean forRecordCopy = false;
/* 3107 */       UpdatingBatch existingUB = null;
/* 3108 */       Record result = internalDeclare(classIdent, props, perms, additionalContainers, associatedContent, forRecordCopy, existingUB);
/* 3109 */       Tracer.traceMethodExit(new Object[] { result });
/* 3110 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 3114 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 3118 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 3122 */       if (establishedSubject) {
/* 3123 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Record declare(String classIdent, RMProperties props, List<RMPermission> perms, List<RecordContainer> additionalContainers, List<ContentItem> associatedContent)
/*      */   {
/* 3133 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms, additionalContainers, associatedContent });
/* 3134 */     Util.ckInvalidStrParam("classIdent", classIdent);
/*      */     
/* 3136 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3139 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 3143 */       List<ContentItem> refreshedContentItems = refreshContentItems(associatedContent);
/*      */       
/* 3145 */       boolean forRecordCopy = false;
/* 3146 */       UpdatingBatch existingUB = null;
/* 3147 */       Record result = internalDeclare(classIdent, props, perms, additionalContainers, refreshedContentItems, forRecordCopy, existingUB);
/* 3148 */       Tracer.traceMethodExit(new Object[] { result });
/* 3149 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 3153 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 3157 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 3161 */       if (establishedSubject) {
/* 3162 */         P8CE_Util.disassociateSubject();
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
/*      */   Record internalDeclare(String classIdent, RMProperties jarmProps, List<RMPermission> jarmPerms, List<RecordContainer> additionalContainers, List<ContentItem> associatedContent, boolean forRecordCopy, UpdatingBatch existingUB)
/*      */   {
/* 3195 */     Tracer.traceMethodEntry(new Object[] { classIdent, jarmProps, jarmPerms, additionalContainers, associatedContent, Boolean.valueOf(forRecordCopy), existingUB });
/* 3196 */     FilePlanRepository fpRepos = (FilePlanRepository)getRepository();
/* 3197 */     RMDomain jarmDomain = fpRepos.getDomain();
/* 3198 */     Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3205 */     List<RecordContainer> targetContainers = refreshContainers(additionalContainers);
/*      */     
/*      */ 
/* 3208 */     if (fpRepos.supportsDefensibleDisposal())
/*      */     {
/* 3210 */       for (RecordContainer targetContainer : targetContainers)
/*      */       {
/* 3212 */         if (((targetContainer instanceof DefensiblyDisposable)) && (targetContainer.isADefensiblyDisposableContainer()))
/*      */         {
/*      */ 
/* 3215 */           if (targetContainers.size() > 1)
/*      */           {
/* 3217 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MULTIFILE_INTO_DD_CONTAINER, new Object[0]);
/*      */           }
/*      */           
/* 3220 */           EntityType recordEntityType = getEntityTypeForClass(classIdent);
/* 3221 */           if (EntityType.PhysicalRecord == recordEntityType)
/*      */           {
/* 3223 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_PHYSREC_INTO_DD_CONTAINER, new Object[0]);
/*      */           }
/*      */           
/* 3226 */           if ((jarmProps.isPropertyPresent("AssociatedRecordType")) && (jarmProps.getObjectValue("AssociatedRecordType") != null))
/*      */           {
/*      */ 
/* 3229 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_REC_WITH_RECTYPE_INTO_DD_CONTAINER, new Object[0]);
/*      */           }
/*      */           
/*      */ 
/* 3233 */           String recClassName = classIdent.trim();
/* 3234 */           if (Id.isId(recClassName))
/*      */           {
/* 3236 */             ObjectStore jaceFPOSBase = ((P8CE_RepositoryImpl)fpRepos).getJaceObjectStore();
/* 3237 */             MetadataCache jaceMetaCache = Factory.MetadataCache.getDefaultInstance();
/* 3238 */             ClassDescription jaceCD = jaceMetaCache.getClassDescription(jaceFPOSBase, recClassName);
/* 3239 */             recClassName = jaceCD.get_SymbolicName();
/*      */           }
/* 3241 */           String ddTriggerPropName = targetContainer.getTriggerPropertyName();
/* 3242 */           Set<String> applicableDDRecordClassNames = ((P8CE_BaseContainerImpl)targetContainer).validateDDTriggerProperty(ddTriggerPropName);
/* 3243 */           if ((applicableDDRecordClassNames == null) || (!applicableDDRecordClassNames.contains(recClassName)))
/*      */           {
/*      */ 
/*      */ 
/* 3247 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_REC_WITHOUT_TRIGPROP_INTO_DD_CONTAINER, new Object[0]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3258 */     targetContainers = super.validateAndPrepareRecordDeclaration(classIdent, jarmProps, jarmPerms, targetContainers, associatedContent, forRecordCopy);
/*      */     
/*      */ 
/*      */ 
/* 3262 */     IndependentlyPersistableObject jaceSupersedingTargetBase = null;
/* 3263 */     if (jarmProps.isPropertyPresent("SupercededBy"))
/*      */     {
/* 3265 */       P8CE_RMPropertyImpl jarmSupersedesProp = (P8CE_RMPropertyImpl)jarmProps.get("SupercededBy");
/* 3266 */       Property jaceSupersedesProp = jarmSupersedesProp.getJaceProperty();
/* 3267 */       jaceSupersedingTargetBase = (IndependentlyPersistableObject)jaceSupersedesProp.getEngineObjectValue();
/*      */     }
/*      */     
/*      */ 
/* 3271 */     doDoDClassifiedRecordVersioning(jarmProps);
/*      */     
/*      */ 
/*      */ 
/* 3275 */     List<RMPermission> actualJarmPerms = jarmPerms;
/* 3276 */     if (actualJarmPerms == null)
/*      */     {
/* 3278 */       actualJarmPerms = getDefaultRecordJarmPermissions(classIdent);
/*      */     }
/*      */     
/*      */ 
/* 3282 */     AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(actualJarmPerms);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3287 */     UpdatingBatch jaceUpdateBatch = existingUB;
/* 3288 */     if (jaceUpdateBatch == null) {
/* 3289 */       jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */     }
/* 3291 */     Folder jaceParentBaseFolder = (Folder)getJaceBaseObject();
/* 3292 */     ObjectStore jaceObjStore = jaceParentBaseFolder.getObjectStore();
/*      */     
/* 3294 */     Id proposedRecordId = null;
/* 3295 */     if (jarmProps.isPropertyPresent("Id"))
/*      */     {
/* 3297 */       proposedRecordId = new Id(jarmProps.getGuidValue("Id"));
/*      */     }
/*      */     else
/*      */     {
/* 3301 */       proposedRecordId = Id.createId();
/*      */     }
/* 3303 */     com.filenet.api.core.Document jaceRecordBase = Factory.Document.createInstance(jaceObjStore, classIdent, proposedRecordId);
/*      */     
/* 3305 */     Properties jaceProps = jaceRecordBase.getProperties();
/* 3306 */     P8CE_Util.convertToJaceProperties(jarmProps, jaceProps);
/*      */     
/* 3308 */     jaceRecordBase.set_Permissions(jacePerms);
/*      */     
/* 3310 */     jaceRecordBase.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/* 3311 */     PropertyFilter recPF = new PropertyFilter();
/* 3312 */     for (FilterElement fe : P8CE_RecordImpl.getMandatoryJaceFEs())
/*      */     {
/* 3314 */       recPF.addIncludeProperty(fe);
/*      */     }
/* 3316 */     jaceUpdateBatch.add(jaceRecordBase, recPF);
/*      */     
/*      */ 
/* 3319 */     Folder jaceTargetFolderBase = null;
/* 3320 */     ReferentialContainmentRelationship rcr = null;
/* 3321 */     for (Container targetContainer : targetContainers)
/*      */     {
/* 3323 */       jaceTargetFolderBase = ((P8CE_BaseContainerImpl)targetContainer).jaceFolder;
/* 3324 */       rcr = jaceTargetFolderBase.file(jaceRecordBase, AutoUniqueName.AUTO_UNIQUE, null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
/* 3325 */       jaceUpdateBatch.add(rcr, P8CE_Util.CEPF_Empty);
/*      */     }
/*      */     
/*      */ 
/* 3329 */     if ((associatedContent != null) && (associatedContent.size() > 0))
/*      */     {
/* 3331 */       PropertyFilter contentPF = new PropertyFilter();
/* 3332 */       for (FilterElement fe : P8CE_ContentItemImpl.getMandatoryJaceFEs())
/* 3333 */         contentPF.addIncludeProperty(fe);
/* 3334 */       com.filenet.api.core.Document jaceContentItemBase = null;
/* 3335 */       for (ContentItem contentItem : associatedContent)
/*      */       {
/* 3337 */         jaceContentItemBase = (com.filenet.api.core.Document)((P8CE_ContentItemImpl)contentItem).getJaceBaseObject();
/* 3338 */         jaceContentItemBase.getProperties().putValue("RecordInformation", jaceRecordBase);
/* 3339 */         if (!forRecordCopy)
/*      */         {
/* 3341 */           jaceContentItemBase.takeFederatedOwnership();
/* 3342 */           jaceUpdateBatch.add(jaceContentItemBase, contentPF);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3347 */       P8CE_FilePlanRepositoryImpl p8ceFPRepos = (P8CE_FilePlanRepositoryImpl)getRepository();
/* 3348 */       Repository contentRepos = ((ContentItem)associatedContent.get(0)).getRepository();
/* 3349 */       ((RALBaseRepository)contentRepos).resolve();
/* 3350 */       Id contentReposId = ((P8CE_RepositoryImpl)contentRepos).getJaceObjectStore().get_Id();
/* 3351 */       p8ceFPRepos.updateConnectorRegistrations(contentReposId);
/*      */     }
/*      */     
/*      */ 
/* 3355 */     if (jaceSupersedingTargetBase != null)
/*      */     {
/* 3357 */       jaceSupersedingTargetBase.getProperties().putValue("SupercededDate", new Date());
/* 3358 */       jaceUpdateBatch.add(jaceSupersedingTargetBase, P8CE_Util.CEPF_Empty);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3363 */     if (existingUB == null)
/*      */     {
/*      */ 
/* 3366 */       long startTime = System.currentTimeMillis();
/* 3367 */       jaceUpdateBatch.updateBatch();
/* 3368 */       long endTime = System.currentTimeMillis();
/* 3369 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/* 3373 */     Record result = (Record)P8CE_RecordImpl.getGenerator().create(getRepository(), jaceRecordBase);
/*      */     
/* 3375 */     Tracer.traceMethodExit(new Object[] { result });
/* 3376 */     return result;
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
/*      */   private void doDoDClassifiedRecordVersioning(RMProperties jarmProps)
/*      */   {
/* 3394 */     Tracer.traceMethodEntry(new Object[] { jarmProps });
/*      */     try
/*      */     {
/* 3397 */       IndependentlyPersistableObject jacePrevVersionRecBase = null;
/* 3398 */       if ((jarmProps != null) && (jarmProps.isPropertyPresent("PreviousVersionRecord")))
/*      */       {
/* 3400 */         Object objRawValue = jarmProps.getObjectValue("PreviousVersionRecord");
/* 3401 */         if ((objRawValue != null) && ((objRawValue instanceof JaceBasable)))
/*      */         {
/* 3403 */           jacePrevVersionRecBase = (IndependentlyPersistableObject)((JaceBasable)objRawValue).getJaceBaseObject();
/*      */         }
/*      */         
/* 3406 */         IndependentlyPersistableObject jaceCurrentTipCandidate = jacePrevVersionRecBase;
/* 3407 */         if (jaceCurrentTipCandidate != null)
/*      */         {
/* 3409 */           boolean haveFoundTip = false;
/* 3410 */           String[] refreshPropNames = { "Id", "NextVersionRecord", "RecordVersion" };
/* 3411 */           while (!haveFoundTip)
/*      */           {
/* 3413 */             haveFoundTip = true;
/* 3414 */             jaceCurrentTipCandidate.refresh(refreshPropNames);
/* 3415 */             if (jaceCurrentTipCandidate.getProperties().isPropertyPresent("NextVersionRecord"))
/*      */             {
/* 3417 */               IndependentObjectSet jaceNextVersionRecValue = jaceCurrentTipCandidate.getProperties().getIndependentObjectSetValue("NextVersionRecord");
/*      */               
/* 3419 */               if ((jaceNextVersionRecValue != null) && (!jaceNextVersionRecValue.isEmpty()))
/*      */               {
/*      */ 
/* 3422 */                 Iterator it = jaceNextVersionRecValue.iterator();
/* 3423 */                 if (it.hasNext())
/*      */                 {
/* 3425 */                   IndependentlyPersistableObject jaceNextVersionRecord = (IndependentlyPersistableObject)it.next();
/* 3426 */                   if (jaceNextVersionRecord != null)
/*      */                   {
/*      */ 
/* 3429 */                     jaceCurrentTipCandidate = jaceNextVersionRecord;
/* 3430 */                     haveFoundTip = false;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 3438 */         int currentTipVersionNumber = 1;
/* 3439 */         if (jaceCurrentTipCandidate != null)
/*      */         {
/*      */ 
/* 3442 */           if (jaceCurrentTipCandidate.getProperties().isPropertyPresent("RecordVersion"))
/*      */           {
/* 3444 */             Integer rawVersionNumber = jaceCurrentTipCandidate.getProperties().getInteger32Value("RecordVersion");
/* 3445 */             if (rawVersionNumber != null) {
/* 3446 */               currentTipVersionNumber = rawVersionNumber.intValue();
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 3452 */         if (jaceCurrentTipCandidate != null)
/*      */         {
/* 3454 */           P8CE_RMPropertyImpl p8ceProp = (P8CE_RMPropertyImpl)jarmProps.get("PreviousVersionRecord");
/* 3455 */           Property jaceProp = p8ceProp.getJaceProperty();
/* 3456 */           jaceProp.setObjectValue(jaceCurrentTipCandidate);
/* 3457 */           if (currentTipVersionNumber < 1)
/* 3458 */             currentTipVersionNumber = 1;
/* 3459 */           Integer newRecVersionNumber = Integer.valueOf(currentTipVersionNumber + 1);
/* 3460 */           jarmProps.putIntegerValue("RecordVersion", newRecVersionNumber);
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 3467 */       Logger.warn(RMLogCode.DOD_CLASSIFIED_DECL_VERSIONING_FAILURE, new Object[] { ex.getLocalizedMessage() });
/*      */     }
/*      */     
/* 3470 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   private List<RMPermission> getDefaultRecordJarmPermissions(String classIdent)
/*      */   {
/* 3484 */     Tracer.traceMethodEntry(new Object[] { classIdent });
/* 3485 */     List<RMPermission> defaultJarmPerms = null;
/* 3486 */     RMClassDescription recordClassDesc = getClassDescription(getRepository(), classIdent);
/* 3487 */     String creatorOwnerName; if (recordClassDesc != null)
/*      */     {
/* 3489 */       List<RMPermission> defaultInstancePerms = recordClassDesc.getDefaultInstancePermissions();
/* 3490 */       if (defaultInstancePerms != null)
/*      */       {
/*      */ 
/* 3493 */         defaultJarmPerms = new ArrayList(defaultInstancePerms.size());
/* 3494 */         creatorOwnerName = SpecialPrincipal.CREATOR_OWNER.toString();
/* 3495 */         for (RMPermission defaultPerm : defaultInstancePerms)
/*      */         {
/* 3497 */           if ((defaultPerm.getGranteeType() != RMGranteeType.Unknown) && (!creatorOwnerName.equals(defaultPerm.getGranteeName())))
/*      */           {
/*      */ 
/* 3500 */             defaultJarmPerms.add(defaultPerm);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3506 */     Tracer.traceMethodExit(new Object[] { defaultJarmPerms });
/* 3507 */     return defaultJarmPerms;
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
/*      */   private List<ContentItem> refreshContentItems(List<ContentItem> originalContentItemList)
/*      */   {
/* 3522 */     Tracer.traceMethodEntry(new Object[] { originalContentItemList });
/* 3523 */     List<ContentItem> refreshedContentItemList = null;
/* 3524 */     if (originalContentItemList != null)
/*      */     {
/* 3526 */       refreshedContentItemList = new ArrayList(originalContentItemList.size());
/*      */       
/* 3528 */       RMDomain jarmDomain = getRepository().getDomain();
/* 3529 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 3530 */       PropertyFilter jacePF = new PropertyFilter();
/* 3531 */       for (FilterElement fe : P8CE_ContentItemImpl.getMandatoryJaceFEs()) {
/* 3532 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*      */       
/* 3535 */       RetrievingBatch jaceBatch = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/* 3536 */       List<BatchItemHandle> BIHs = new ArrayList(originalContentItemList.size());
/* 3537 */       com.filenet.api.core.Document jaceContentItemBase = null;
/* 3538 */       List<com.filenet.api.core.Document> targetJaceDocuments = new ArrayList(originalContentItemList.size());
/* 3539 */       for (ContentItem targetContentItem : originalContentItemList)
/*      */       {
/* 3541 */         jaceContentItemBase = (com.filenet.api.core.Document)((P8CE_ContentItemImpl)targetContentItem).getJaceBaseObject();
/* 3542 */         targetJaceDocuments.add(jaceContentItemBase);
/* 3543 */         BIHs.add(jaceBatch.add(jaceContentItemBase, jacePF));
/*      */       }
/*      */       
/* 3546 */       long startTime = System.currentTimeMillis();
/* 3547 */       jaceBatch.retrieveBatch();
/* 3548 */       long endTime = System.currentTimeMillis();
/* 3549 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(originalContentItemList.size()), null, new Object[0]);
/*      */       
/*      */ 
/* 3552 */       IGenerator<ContentItem> contentItemGenerator = P8CE_ContentItemImpl.getContentItemGenerator();
/* 3553 */       Repository contentRepos = null;
/* 3554 */       for (int i = 0; i < BIHs.size(); i++)
/*      */       {
/* 3556 */         BatchItemHandle bih = (BatchItemHandle)BIHs.get(i);
/* 3557 */         if (bih.hasException()) {
/* 3558 */           throw P8CE_Util.processJaceException(bih.getException(), RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*      */         }
/* 3560 */         com.filenet.api.core.Document updatedTargetJaceDocument = (com.filenet.api.core.Document)targetJaceDocuments.get(i);
/* 3561 */         contentRepos = ((ContentItem)originalContentItemList.get(i)).getRepository();
/* 3562 */         refreshedContentItemList.add(contentItemGenerator.create(contentRepos, updatedTargetJaceDocument));
/*      */       }
/*      */     }
/*      */     
/* 3566 */     Tracer.traceMethodExit(new Object[] { refreshedContentItemList });
/* 3567 */     return refreshedContentItemList;
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
/*      */   private List<RecordContainer> refreshContainers(List<RecordContainer> originalContainerList)
/*      */   {
/* 3582 */     Tracer.traceMethodEntry(new Object[] { originalContainerList });
/* 3583 */     List<RecordContainer> refreshedContainers = null;
/*      */     
/*      */ 
/* 3586 */     List<Folder> targetJaceFolders = new ArrayList(1);
/*      */     
/* 3588 */     targetJaceFolders.add(this.jaceFolder);
/* 3589 */     if (originalContainerList != null)
/*      */     {
/* 3591 */       for (Container additionalContainer : originalContainerList) {
/* 3592 */         targetJaceFolders.add(((P8CE_BaseContainerImpl)additionalContainer).jaceFolder);
/*      */       }
/*      */     }
/* 3595 */     RMDomain jarmDomain = getRepository().getDomain();
/* 3596 */     Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 3597 */     PropertyFilter jacePF = new PropertyFilter();
/* 3598 */     for (FilterElement fe : getMandatoryJaceFEs()) {
/* 3599 */       jacePF.addIncludeProperty(fe);
/*      */     }
/*      */     
/* 3602 */     RetrievingBatch jaceBatch = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/* 3603 */     List<BatchItemHandle> BIHs = new ArrayList(targetJaceFolders.size());
/* 3604 */     for (Folder targetJaceFolder : targetJaceFolders)
/*      */     {
/* 3606 */       BIHs.add(jaceBatch.add(targetJaceFolder, jacePF));
/*      */     }
/* 3608 */     long startTime = System.currentTimeMillis();
/* 3609 */     jaceBatch.retrieveBatch();
/* 3610 */     long endTime = System.currentTimeMillis();
/* 3611 */     Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(targetJaceFolders.size()), null, new Object[0]);
/*      */     
/*      */ 
/* 3614 */     refreshedContainers = new ArrayList(targetJaceFolders.size());
/* 3615 */     for (int i = 0; i < BIHs.size(); i++)
/*      */     {
/* 3617 */       BatchItemHandle bih = (BatchItemHandle)BIHs.get(i);
/* 3618 */       if (bih.hasException()) {
/* 3619 */         throw P8CE_Util.processJaceException(bih.getException(), RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*      */       }
/* 3621 */       Folder updatedTargetJaceFolder = (Folder)targetJaceFolders.get(i);
/*      */       
/*      */ 
/* 3624 */       IGenerator<?> containerGenerator = P8CE_Util.getEntityGenerator(updatedTargetJaceFolder);
/* 3625 */       refreshedContainers.add((RecordContainer)containerGenerator.create(getRepository(), updatedTargetJaceFolder));
/*      */     }
/*      */     
/* 3628 */     Tracer.traceMethodExit(new Object[] { refreshedContainers });
/* 3629 */     return refreshedContainers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<Integer> getInitialAllowedRMTypes(String classIdent)
/*      */   {
/* 3638 */     Tracer.traceMethodEntry(new Object[] { classIdent });
/* 3639 */     String repositoryIdent = this.repository.getObjectIdentity();
/*      */     
/*      */ 
/* 3642 */     RMClassDescription classDesc = getClassDescription(this.repository, classIdent);
/* 3643 */     if (classDesc == null) {
/* 3644 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTION_FAILED, new Object[] { repositoryIdent, classIdent });
/*      */     }
/* 3646 */     RMPropertyDescriptionString propDesc = (RMPropertyDescriptionString)classDesc.getPropertyDescription("AllowedRMContainees");
/*      */     
/* 3648 */     if (propDesc == null)
/* 3649 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_RETRIEVING_PROPERTYDESCRIPTION_FAILED, new Object[] { "AllowedRMContainees", classIdent, repositoryIdent });
/* 3650 */     String commaSepStrList = propDesc.getStringDefaultValue();
/* 3651 */     if ((commaSepStrList == null) || (commaSepStrList.trim().length() == 0)) {
/* 3652 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_RETRIEVING_PROPERTYDESCRIPTION_FAILED, new Object[] { "AllowedRMContainees", classIdent, repositoryIdent });
/*      */     }
/* 3654 */     String[] defaultAllowedTypeStrs = commaSepStrList.split(",");
/* 3655 */     List<Integer> result = new ArrayList(defaultAllowedTypeStrs.length);
/* 3656 */     for (int i = 0; i < defaultAllowedTypeStrs.length; i++)
/*      */     {
/* 3658 */       result.add(Integer.valueOf(defaultAllowedTypeStrs[i], 10));
/*      */     }
/*      */     
/* 3661 */     Tracer.traceMethodExit(new Object[] { result });
/* 3662 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected EntityType getEntityTypeForClass(Repository repository, String classIdent)
/*      */   {
/* 3670 */     Tracer.traceMethodEntry(new Object[] { repository, classIdent });
/* 3671 */     String repositoryIdent = repository.getObjectIdentity();
/*      */     
/*      */ 
/* 3674 */     RMClassDescription classDesc = getClassDescription(repository, classIdent);
/* 3675 */     if (classDesc == null) {
/* 3676 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTION_FAILED, new Object[] { repositoryIdent, classIdent });
/*      */     }
/* 3678 */     RMPropertyDescriptionInteger propDesc = (RMPropertyDescriptionInteger)classDesc.getPropertyDescription("RMEntityType");
/*      */     
/* 3680 */     if (propDesc == null) {
/* 3681 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_RETRIEVING_PROPERTYDESCRIPTION_FAILED, new Object[] { "RMEntityType", classIdent, repositoryIdent });
/*      */     }
/* 3683 */     Integer propValue = propDesc.getIntegerDefaultValue();
/* 3684 */     if (propValue == null) {
/* 3685 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_RETRIEVING_PROPERTYDESCRIPTION_FAILED, new Object[] { "RMEntityType", classIdent, repositoryIdent });
/*      */     }
/* 3687 */     EntityType result = EntityType.getInstanceFromInt(propValue.intValue());
/* 3688 */     Tracer.traceMethodExit(new Object[] { result });
/* 3689 */     return result;
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
/*      */   protected RMClassDescription getClassDescription(Repository repository, String classIdent)
/*      */   {
/* 3703 */     Tracer.traceMethodEntry(new Object[] { repository, classIdent });
/* 3704 */     boolean allowFromCache = true;
/* 3705 */     RMClassDescription result = P8CE_Util.getClassDescription(repository, classIdent, allowFromCache);
/* 3706 */     Tracer.traceMethodExit(new Object[] { result });
/* 3707 */     return result;
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
/*      */   protected boolean verifyContainerNamesAreUnique(Container parent, EntityType entityType, RMProperties newProps, Container entityToIgnore)
/*      */   {
/* 3724 */     Tracer.traceMethodEntry(new Object[] { parent, entityType, newProps, entityToIgnore });
/* 3725 */     boolean namesAreUnique = true;
/* 3726 */     String[] propNamesToCheck = null;
/* 3727 */     String tableName = null;
/* 3728 */     if (entityType == EntityType.RecordCategory)
/*      */     {
/* 3730 */       tableName = "RecordCategory";
/* 3731 */       propNamesToCheck = new String[] { "RecordCategoryName", "RecordCategoryIdentifier" };
/*      */     }
/* 3733 */     else if (entityType == EntityType.RecordFolder)
/*      */     {
/* 3735 */       tableName = "RecordFolder";
/* 3736 */       propNamesToCheck = new String[] { "RecordFolderName", "RecordFolderIdentifier" };
/*      */     }
/* 3738 */     else if (entityType == EntityType.RecordVolume)
/*      */     {
/* 3740 */       tableName = "Volume";
/* 3741 */       propNamesToCheck = new String[] { "VolumeName" };
/*      */     }
/*      */     else
/*      */     {
/* 3745 */       propNamesToCheck = new String[0];
/*      */     }
/*      */     
/* 3748 */     String[] propValuesToCheck = new String[propNamesToCheck.length];
/* 3749 */     for (int i = 0; i < propNamesToCheck.length; i++)
/*      */     {
/* 3751 */       propValuesToCheck[i] = RALBaseEntity.escapeSQLStringValue(newProps.getStringValue(propNamesToCheck[i]));
/*      */     }
/*      */     
/* 3754 */     if ((propNamesToCheck != null) && (propNamesToCheck.length > 0))
/*      */     {
/* 3756 */       StringBuilder sb = new StringBuilder();
/* 3757 */       sb.append("SELECT TOP 1 ").append("Id");
/* 3758 */       sb.append(" FROM ").append(tableName);
/* 3759 */       sb.append(" WHERE ").append("Parent").append(" = OBJECT('").append(parent.getObjectIdentity()).append("')");
/* 3760 */       sb.append(" AND (");
/* 3761 */       for (int i = 0; i < propNamesToCheck.length; i++)
/*      */       {
/* 3763 */         if (i > 0)
/* 3764 */           sb.append(" OR ");
/* 3765 */         sb.append('(').append(propNamesToCheck[i]).append(" = '").append(propValuesToCheck[i]).append("')");
/*      */       }
/* 3767 */       sb.append(')');
/* 3768 */       if (entityToIgnore != null)
/*      */       {
/* 3770 */         String ignoreIdent = entityToIgnore.getObjectIdentity();
/* 3771 */         sb.append(" AND (").append("Id").append(" <> '").append(ignoreIdent).append("') ");
/*      */       }
/*      */       
/* 3774 */       String sqlStmt = sb.toString();
/* 3775 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 3776 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */       
/* 3778 */       long startTime = System.currentTimeMillis();
/* 3779 */       RepositoryRowSet resultRows = jaceSearchScope.fetchRows(jaceSearchSQL, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 3780 */       long endTime = System.currentTimeMillis();
/* 3781 */       Boolean elementCountIndicator = null;
/* 3782 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 3784 */         elementCountIndicator = resultRows != null ? Boolean.valueOf(resultRows.isEmpty()) : null;
/*      */       }
/* 3786 */       Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, resultRows, new Object[] { sqlStmt, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */       
/* 3788 */       if ((resultRows != null) && (!resultRows.isEmpty()))
/*      */       {
/* 3790 */         namesAreUnique = false;
/*      */       }
/*      */     }
/*      */     
/* 3794 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(namesAreUnique) });
/* 3795 */     return namesAreUnique;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hasChildContainers()
/*      */   {
/* 3804 */     Tracer.traceMethodEntry(new Object[0]);
/* 3805 */     boolean result = false;
/*      */     
/* 3807 */     String idStr = getObjectIdentity();
/* 3808 */     StringBuilder sb = new StringBuilder();
/* 3809 */     sb.append("SELECT TOP 1 f.").append("Id");
/* 3810 */     sb.append(" FROM ").append("RMFolder").append(" f");
/* 3811 */     sb.append(" WHERE f.").append("Parent").append(" = OBJECT('").append(idStr).append("')");
/* 3812 */     sb.append("   AND f.").append("IsDeleted").append(" = FALSE");
/*      */     
/* 3814 */     String sqlStmt = sb.toString();
/* 3815 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 3816 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */     
/* 3818 */     long startTime = System.currentTimeMillis();
/* 3819 */     RepositoryRowSet resultRows = jaceSearchScope.fetchRows(jaceSearchSQL, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 3820 */     long endTime = System.currentTimeMillis();
/* 3821 */     Boolean elementCountIndicator = null;
/* 3822 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 3824 */       elementCountIndicator = resultRows != null ? Boolean.valueOf(resultRows.isEmpty()) : null;
/*      */     }
/* 3826 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, resultRows, new Object[] { sqlStmt, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */     
/* 3828 */     if ((resultRows != null) && (!resultRows.isEmpty())) {
/* 3829 */       result = true;
/*      */     }
/* 3831 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 3832 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hasChildRecords()
/*      */   {
/* 3841 */     Tracer.traceMethodEntry(new Object[0]);
/* 3842 */     boolean result = false;
/*      */     
/* 3844 */     String idStr = getObjectIdentity();
/* 3845 */     StringBuilder sb = new StringBuilder();
/* 3846 */     sb.append("SELECT TOP 1 r.").append("Id");
/* 3847 */     sb.append(" FROM ").append("RecordInfo").append(" r");
/* 3848 */     sb.append(" WHERE r.This INSUBFOLDER '").append(idStr).append("'");
/* 3849 */     sb.append("   AND r.").append("IsDeleted").append(" = FALSE");
/*      */     
/* 3851 */     String sqlStmt = sb.toString();
/* 3852 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 3853 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */     
/* 3855 */     long startTime = System.currentTimeMillis();
/* 3856 */     RepositoryRowSet resultRows = jaceSearchScope.fetchRows(jaceSearchSQL, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 3857 */     long endTime = System.currentTimeMillis();
/* 3858 */     Boolean elementCountIndicator = null;
/* 3859 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 3861 */       elementCountIndicator = resultRows != null ? Boolean.valueOf(resultRows.isEmpty()) : null;
/*      */     }
/* 3863 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, resultRows, new Object[] { sqlStmt, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */     
/* 3865 */     if ((resultRows != null) && (!resultRows.isEmpty())) {
/* 3866 */       result = true;
/*      */     }
/* 3868 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 3869 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected EntityType[] determineAllowedContaineeTypes()
/*      */   {
/* 3880 */     Tracer.traceMethodEntry(new Object[0]);
/* 3881 */     EntityType[] entityTypes = null;
/*      */     
/* 3883 */     Properties jaceProps = this.jaceFolder.getProperties();
/*      */     
/*      */ 
/* 3886 */     if (jaceProps.isPropertyPresent("AllowedRMTypes"))
/*      */     {
/* 3888 */       Integer32List allowedRMTypeList = jaceProps.getInteger32ListValue("AllowedRMTypes");
/* 3889 */       if ((allowedRMTypeList != null) && (!allowedRMTypeList.isEmpty())) {
/* 3890 */         entityTypes = P8CE_Util.convertFromInteger32List(allowedRMTypeList);
/*      */       }
/*      */     }
/*      */     
/* 3894 */     if ((entityTypes == null) && (jaceProps.isPropertyPresent("AllowedRMContainees")))
/*      */     {
/* 3896 */       String commaSepStringList = jaceProps.getStringValue("AllowedRMContainees");
/* 3897 */       entityTypes = P8CE_Util.convertFromCommaSepStringList(commaSepStringList);
/*      */     }
/*      */     
/* 3900 */     if (entityTypes == null)
/*      */     {
/* 3902 */       entityTypes = new EntityType[0];
/* 3903 */       if (P8CE_Util.isAnIERRootContainer(this.jaceFolder))
/*      */       {
/* 3905 */         entityTypes = new EntityType[] { EntityType.FilePlan };
/*      */       }
/*      */     }
/*      */     
/* 3909 */     Tracer.traceMethodExit(new Object[] { entityTypes });
/* 3910 */     return entityTypes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<Link> getAssociatedJaceLinks()
/*      */   {
/* 3921 */     Tracer.traceMethodEntry(new Object[0]);
/* 3922 */     List<Link> associatedLinks = new ArrayList(0);
/*      */     
/* 3924 */     String idStr = getObjectIdentity();
/*      */     
/* 3926 */     StringBuilder sb = new StringBuilder();
/* 3927 */     sb.append("SELECT l.").append("Id");
/* 3928 */     sb.append(" FROM ").append("Link").append(" l");
/* 3929 */     sb.append(" WHERE (").append("l.").append("Head").append(" = OBJECT('").append(idStr).append("')");
/* 3930 */     sb.append(" OR ").append("l.").append("Tail").append(" = OBJECT('").append(idStr).append("'))");
/*      */     
/* 3932 */     String sqlStmt = sb.toString();
/* 3933 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 3934 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/* 3935 */     Integer pageSize = null;
/* 3936 */     Boolean continuable = Boolean.TRUE;
/*      */     
/* 3938 */     long startTime = System.currentTimeMillis();
/* 3939 */     IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, continuable);
/* 3940 */     long endTime = System.currentTimeMillis();
/* 3941 */     Boolean elementCountIndicator = null;
/* 3942 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 3944 */       elementCountIndicator = jaceObjSet != null ? Boolean.valueOf(jaceObjSet.isEmpty()) : null;
/*      */     }
/* 3946 */     Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStmt, pageSize, P8CE_Util.CEPF_IdOnly, continuable });
/*      */     
/* 3948 */     if (jaceObjSet != null)
/*      */     {
/* 3950 */       Link jaceLink = null;
/* 3951 */       PageIterator jacePI = jaceObjSet.pageIterator();
/* 3952 */       while (jacePI.nextPage())
/*      */       {
/* 3954 */         Object[] currentPage = jacePI.getCurrentPage();
/* 3955 */         for (Object obj : currentPage)
/*      */         {
/* 3957 */           jaceLink = (Link)obj;
/* 3958 */           associatedLinks.add(jaceLink);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3963 */     Tracer.traceMethodExit(new Object[] { associatedLinks });
/* 3964 */     return associatedLinks;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilePlan getFilePlan()
/*      */   {
/* 3973 */     Tracer.traceMethodEntry(new Object[0]);
/* 3974 */     if (this.myFilePlan == null)
/*      */     {
/* 3976 */       if (getEntityType() == EntityType.FilePlan)
/*      */       {
/* 3978 */         this.myFilePlan = ((P8CE_FilePlanImpl)this);
/*      */       }
/*      */       else
/*      */       {
/* 3982 */         boolean establishedSubject = false;
/*      */         try
/*      */         {
/* 3985 */           establishedSubject = P8CE_Util.associateSubject();
/* 3986 */           Container currentContainer = this;
/* 3987 */           Container currentParent = null;
/*      */           
/* 3989 */           while ((currentParent = currentContainer.getParent()) != null)
/*      */           {
/* 3991 */             if (currentParent.getEntityType() == EntityType.FilePlan)
/*      */             {
/* 3993 */               this.myFilePlan = ((P8CE_FilePlanImpl)currentParent);
/* 3994 */               break;
/*      */             }
/*      */             
/* 3997 */             currentContainer = currentParent;
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/* 4002 */           if (establishedSubject) {
/* 4003 */             P8CE_Util.disassociateSubject();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 4008 */     Tracer.traceMethodExit(new Object[] { this.myFilePlan });
/* 4009 */     return this.myFilePlan;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void validateFilePlanScope(Container[] containers)
/*      */   {
/* 4020 */     Tracer.traceMethodEntry((Object[])containers);
/*      */     
/* 4022 */     if (containers.length > 1)
/*      */     {
/* 4024 */       FilePlan firstFilePlan = ((P8CE_BaseContainerImpl)containers[0]).getFilePlan();
/* 4025 */       String firstFilePlanId = firstFilePlan.getObjectIdentity();
/*      */       
/* 4027 */       FilePlan subsequentFilePlan = null;
/* 4028 */       for (int i = 1; i < containers.length; i++)
/*      */       {
/* 4030 */         subsequentFilePlan = ((P8CE_BaseContainerImpl)containers[i]).getFilePlan();
/* 4031 */         if (!firstFilePlanId.equalsIgnoreCase(subsequentFilePlan.getObjectIdentity()))
/*      */         {
/* 4033 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_CONTAINERS_IN_DIFFERENT_FILEPLAN, new Object[] { containers[0], containers[i] });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4038 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   protected boolean adjustForChildRemoval(Container childToRemove)
/*      */   {
/* 4057 */     Tracer.traceMethodEntry(new Object[] { childToRemove });
/* 4058 */     boolean result = false;
/*      */     
/* 4060 */     if (this.entityType == EntityType.RecordCategory)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 4065 */       String idStr = getObjectIdentity();
/* 4066 */       StringBuilder sb = new StringBuilder();
/* 4067 */       sb.append("SELECT TOP 2 rf.").append("Id");
/* 4068 */       sb.append(" FROM ").append("RMFolder").append(" rf");
/* 4069 */       sb.append(" WHERE rf.").append("Parent").append(" = OBJECT('").append(idStr).append("')");
/* 4070 */       sb.append(" AND rf.").append("IsDeleted").append(" = FALSE");
/*      */       
/* 4072 */       String sqlStmt = sb.toString();
/* 4073 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 4074 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/* 4075 */       Integer pageSize = Integer.valueOf(2);
/*      */       
/* 4077 */       long startTime = System.currentTimeMillis();
/* 4078 */       RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 4079 */       long endTime = System.currentTimeMillis();
/* 4080 */       Boolean elementCountIndicator = null;
/* 4081 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 4083 */         elementCountIndicator = Boolean.valueOf(rowSet.isEmpty());
/*      */       }
/* 4085 */       Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, rowSet, new Object[] { sqlStmt, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */       
/* 4087 */       String childIdStr = childToRemove.getObjectIdentity();
/* 4088 */       int responseCount = 0;
/* 4089 */       boolean sawRemovalTarget = false;
/* 4090 */       for (Iterator it = rowSet.iterator(); it.hasNext();)
/*      */       {
/* 4092 */         RepositoryRow row = (RepositoryRow)it.next();
/* 4093 */         responseCount++;
/* 4094 */         if (childIdStr.equalsIgnoreCase(row.getProperties().getIdValue("Id").toString())) {
/* 4095 */           sawRemovalTarget = true;
/*      */         }
/*      */       }
/* 4098 */       if ((responseCount == 1) && (sawRemovalTarget))
/*      */       {
/*      */ 
/* 4101 */         List<Integer> initalRMTypes = getInitialAllowedRMTypes(getClassName());
/* 4102 */         Integer32List jaceValue = P8CE_Util.convertToInteger32List(initalRMTypes);
/* 4103 */         this.jaceFolder.getProperties().putValue("AllowedRMTypes", jaceValue);
/* 4104 */         result = true;
/*      */       }
/*      */     }
/*      */     
/* 4108 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4109 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hasLogicallyDeletedContainees()
/*      */   {
/* 4120 */     Tracer.traceMethodEntry(new Object[0]);
/* 4121 */     boolean result = false;
/* 4122 */     String idStr = getObjectIdentity();
/*      */     
/*      */ 
/* 4125 */     StringBuilder sb = new StringBuilder();
/* 4126 */     sb.append("SELECT TOP 1 rf.").append("Id");
/* 4127 */     sb.append(" FROM ").append("RMFolder").append(" rf");
/* 4128 */     sb.append(" WHERE rf.").append("Parent").append(" = OBJECT('").append(idStr).append("')");
/* 4129 */     sb.append(" AND rf.").append("IsDeleted").append(" = TRUE");
/* 4130 */     String sqlStmt = sb.toString();
/* 4131 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 4132 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/* 4133 */     Integer pageSize = Integer.valueOf(1);
/*      */     
/* 4135 */     long startTime = System.currentTimeMillis();
/* 4136 */     RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 4137 */     long endTime = System.currentTimeMillis();
/* 4138 */     Boolean elementCountIndicator = null;
/* 4139 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 4141 */       elementCountIndicator = Boolean.valueOf(rowSet.isEmpty());
/*      */     }
/* 4143 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, rowSet, new Object[] { sqlStmt, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */     
/* 4145 */     result = !rowSet.isEmpty();
/* 4146 */     if (!result)
/*      */     {
/* 4148 */       sb = new StringBuilder();
/* 4149 */       sb.append("SELECT TOP 1 r.").append("Id");
/* 4150 */       sb.append(" FROM ").append("RecordInfo").append(" r");
/* 4151 */       sb.append(" INNER JOIN ").append("ReferentialContainmentRelationship").append(" rcr ");
/* 4152 */       sb.append(" ON r.This = rcr.").append("Head");
/* 4153 */       sb.append(" WHERE rcr.").append("Tail").append(" = OBJECT('").append(idStr).append("')");
/* 4154 */       sb.append(" AND r.").append("IsDeleted").append(" = TRUE");
/* 4155 */       sqlStmt = sb.toString();
/* 4156 */       jaceSearchSQL = new SearchSQL(sqlStmt);
/* 4157 */       jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */       
/* 4159 */       startTime = System.currentTimeMillis();
/* 4160 */       rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 4161 */       endTime = System.currentTimeMillis();
/* 4162 */       elementCountIndicator = null;
/* 4163 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 4165 */         elementCountIndicator = Boolean.valueOf(rowSet.isEmpty());
/*      */       }
/* 4167 */       Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, rowSet, new Object[] { sqlStmt, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */       
/* 4169 */       result = !rowSet.isEmpty();
/*      */     }
/*      */     
/* 4172 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4173 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionSchedule getAssignedSchedule()
/*      */   {
/* 4182 */     Tracer.traceMethodEntry(new Object[0]);
/* 4183 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4186 */       establishedSubject = P8CE_Util.associateSubject();
/* 4187 */       List<FilterElement> jaceFEs = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 4188 */       IGenerator<DispositionSchedule> generator = P8CE_DispositionScheduleImpl.getGenerator();
/*      */       
/* 4190 */       DispositionSchedule result = (DispositionSchedule)P8CE_Util.fetchSVObjPropValue(this.repository, this.jaceFolder, jaceFEs, "DisposalSchedule", generator);
/*      */       
/*      */ 
/* 4193 */       Tracer.traceMethodExit(new Object[] { result });
/* 4194 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4198 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4202 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordCategory });
/*      */     }
/*      */     finally
/*      */     {
/* 4206 */       if (establishedSubject) {
/* 4207 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void assignDispositionSchedule(DispositionSchedule newSchedule, SchedulePropagation propagationMode)
/*      */   {
/* 4216 */     Tracer.traceMethodEntry(new Object[] { newSchedule, propagationMode });
/* 4217 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4220 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 4224 */       if (isADefensiblyDisposableContainer())
/*      */       {
/* 4226 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ASSIGN_DISPSCHEDULE_TO_DD_CONTAINER, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4231 */       List<CustomObject> deletablePSSAtlasArtifacts = null;
/* 4232 */       DispositionSchedule existingSchedule = getAssignedSchedule();
/* 4233 */       if ((existingSchedule != null) && (newSchedule != null) && (!existingSchedule.getObjectIdentity().equalsIgnoreCase(newSchedule.getObjectIdentity())))
/*      */       {
/*      */ 
/*      */ 
/* 4237 */         deletablePSSAtlasArtifacts = findAutoDeletable_PSSAtlas_artifacts();
/*      */       }
/*      */       
/* 4240 */       super.assignDispositionSchedule(newSchedule, propagationMode);
/*      */       
/* 4242 */       if (deletablePSSAtlasArtifacts != null)
/*      */       {
/* 4244 */         RMDomain jarmDomain = getRepository().getDomain();
/* 4245 */         Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 4246 */         deletePSSAtlasDispositionArtifacts(deletablePSSAtlasArtifacts, jaceDomain);
/*      */       }
/* 4248 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4252 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4256 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordCategory });
/*      */     }
/*      */     finally
/*      */     {
/* 4260 */       if (establishedSubject) {
/* 4261 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void clearDispositionAssignment(SchedulePropagation propagationMode)
/*      */   {
/* 4270 */     Tracer.traceMethodEntry(new Object[] { propagationMode });
/* 4271 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4274 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4276 */       List<CustomObject> deletablePSSAtlasArtifacts = findAutoDeletable_PSSAtlas_artifacts();
/*      */       
/* 4278 */       super.clearDispositionAssignment(propagationMode);
/*      */       
/* 4280 */       if (deletablePSSAtlasArtifacts != null)
/*      */       {
/* 4282 */         RMDomain jarmDomain = getRepository().getDomain();
/* 4283 */         Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 4284 */         deletePSSAtlasDispositionArtifacts(deletablePSSAtlasArtifacts, jaceDomain);
/*      */       }
/* 4286 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4290 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4294 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordCategory });
/*      */     }
/*      */     finally
/*      */     {
/* 4298 */       if (establishedSubject) {
/* 4299 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updatePhaseDataOnEntity()
/*      */   {
/* 4309 */     Tracer.traceMethodEntry(new Object[0]);
/* 4310 */     P8CE_Util.updatePhaseDataOnEntity(this);
/* 4311 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void triggerNextPhase(DispositionSchedule dispSchedule)
/*      */   {
/* 4323 */     Tracer.traceMethodEntry(new Object[0]);
/* 4324 */     P8CE_DispositionScheduleImpl dispSchedImpl = (P8CE_DispositionScheduleImpl)dispSchedule;
/* 4325 */     dispSchedImpl.triggerNextPhase(this);
/* 4326 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public void completePhaseExecution(Date proposedPhaseExecutionDate, boolean transitionToNextPhase)
/*      */   {
/* 4340 */     Tracer.traceMethodEntry(new Object[] { proposedPhaseExecutionDate, Boolean.valueOf(transitionToNextPhase) });
/* 4341 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4344 */       establishedSubject = P8CE_Util.associateSubject();
/* 4345 */       this.jaceFolder.refresh((PropertyFilter)null);
/* 4346 */       if (isReopened())
/*      */       {
/* 4348 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_COMPLETE_DISPPHASE_ON_REOPENED_CONTAINER, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/* 4351 */       RALDispositionLogic.completePhaseExecution(this, proposedPhaseExecutionDate, transitionToNextPhase);
/* 4352 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4356 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4360 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4364 */       if (establishedSubject) {
/* 4365 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetDispositionData()
/*      */   {
/* 4375 */     Tracer.traceMethodEntry(new Object[0]);
/* 4376 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4379 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4381 */       super.resetDispositionData();
/* 4382 */       this.jaceFolder.save(RefreshMode.REFRESH);
/*      */       
/* 4384 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4388 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4392 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4396 */       if (establishedSubject) {
/* 4397 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetVitalData()
/*      */   {
/* 4407 */     Tracer.traceMethodEntry(new Object[0]);
/* 4408 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4411 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4413 */       super.resetVitalData();
/* 4414 */       this.jaceFolder.save(RefreshMode.REFRESH);
/*      */       
/* 4416 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4420 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4424 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4428 */       if (establishedSubject) {
/* 4429 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setVital(RMProperties vitalProperties)
/*      */   {
/* 4439 */     Tracer.traceMethodEntry(new Object[] { vitalProperties });
/* 4440 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4443 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4445 */       super.setVital(vitalProperties);
/* 4446 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4450 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4454 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4458 */       if (establishedSubject) {
/* 4459 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isVital()
/*      */   {
/* 4469 */     Tracer.traceMethodEntry(new Object[0]);
/* 4470 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4473 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4475 */       boolean result = super.isVital();
/*      */       
/* 4477 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4478 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4482 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4486 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4490 */       if (establishedSubject) {
/* 4491 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void executeVital()
/*      */   {
/* 4501 */     Tracer.traceMethodEntry(new Object[0]);
/* 4502 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4505 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4507 */       super.executeVital();
/* 4508 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4512 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4516 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4520 */       if (establishedSubject) {
/* 4521 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateVitalStatus(Date vitalReviewDate)
/*      */   {
/* 4531 */     Tracer.traceMethodEntry(new Object[] { vitalReviewDate });
/* 4532 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4535 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4537 */       super.updateVitalStatus(vitalReviewDate);
/* 4538 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4542 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4546 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4550 */       if (establishedSubject) {
/* 4551 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resolve()
/*      */   {
/* 4561 */     Tracer.traceMethodEntry(new Object[0]);
/* 4562 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4565 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4567 */       if (this.isPlaceholder)
/*      */       {
/* 4569 */         P8CE_Util.refreshWithMandatoryProperties(this);
/* 4570 */         this.isPlaceholder = false;
/*      */         
/* 4572 */         if (this.jaceFolder.getProperties().isPropertyPresent("RMEntityType"))
/*      */         {
/* 4574 */           Integer rawEntityType = this.jaceFolder.getProperties().getInteger32Value("RMEntityType");
/* 4575 */           if (rawEntityType != null) {
/* 4576 */             this.entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*      */           }
/*      */         }
/* 4579 */         updateAllowedEntityTypes();
/*      */       }
/*      */       
/* 4582 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4586 */       if (establishedSubject) {
/* 4587 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOnHold(boolean checkContainerHierarchy)
/*      */   {
/* 4597 */     Tracer.traceMethodEntry(new Object[0]);
/* 4598 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4601 */       establishedSubject = P8CE_Util.associateSubject();
/* 4602 */       boolean result = super.isOnHold(checkContainerHierarchy);
/* 4603 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4604 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 4608 */       if (establishedSubject) {
/* 4609 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyParentOnHold()
/*      */   {
/* 4619 */     Tracer.traceMethodEntry(new Object[0]);
/* 4620 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4623 */       establishedSubject = P8CE_Util.associateSubject();
/* 4624 */       boolean result = super.isAnyParentOnHold();
/* 4625 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4626 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 4630 */       if (establishedSubject) {
/* 4631 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void placeHold(Hold hold)
/*      */   {
/* 4641 */     Tracer.traceMethodEntry(new Object[0]);
/* 4642 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4645 */       establishedSubject = P8CE_Util.associateSubject();
/* 4646 */       super.placeHold(hold);
/* 4647 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4651 */       if (establishedSubject) {
/* 4652 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeHold(Hold hold)
/*      */   {
/* 4662 */     Tracer.traceMethodEntry(new Object[0]);
/* 4663 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4666 */       establishedSubject = P8CE_Util.associateSubject();
/* 4667 */       super.removeHold(hold);
/* 4668 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4672 */       if (establishedSubject) {
/* 4673 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Hold> getAssociatedHolds()
/*      */   {
/* 4683 */     Tracer.traceMethodEntry(new Object[0]);
/* 4684 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4687 */       establishedSubject = P8CE_Util.associateSubject();
/* 4688 */       List<Hold> result = super.getAssociatedHolds();
/* 4689 */       Tracer.traceMethodExit(new Object[] { result });
/* 4690 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 4694 */       if (establishedSubject) {
/* 4695 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<RecordContainer> getParentsOnHold()
/*      */   {
/* 4705 */     Tracer.traceMethodEntry(new Object[0]);
/* 4706 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4709 */       establishedSubject = P8CE_Util.associateSubject();
/* 4710 */       List<RecordContainer> result = super.getParentsOnHold();
/* 4711 */       Tracer.traceMethodExit(new Object[] { result });
/* 4712 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 4716 */       if (establishedSubject) {
/* 4717 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyParentClosed()
/*      */   {
/* 4727 */     Tracer.traceMethodEntry(new Object[0]);
/* 4728 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4731 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 4733 */       boolean result = super.isAnyParentClosed();
/* 4734 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4735 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 4739 */       if (establishedSubject) {
/* 4740 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isInactive()
/*      */   {
/* 4749 */     Tracer.traceMethodEntry(new Object[0]);
/* 4750 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4753 */       establishedSubject = P8CE_Util.associateSubject();
/* 4754 */       boolean result = super.isInactive();
/* 4755 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4756 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 4760 */       if (establishedSubject) {
/* 4761 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPhysicalRecordContainer()
/*      */   {
/* 4771 */     Tracer.traceMethodEntry(new Object[0]);
/* 4772 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4775 */       establishedSubject = P8CE_Util.associateSubject();
/* 4776 */       boolean result = super.isPhysicalRecordContainer();
/* 4777 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4778 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 4782 */       if (establishedSubject) {
/* 4783 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setActive()
/*      */   {
/* 4792 */     Tracer.traceMethodEntry(new Object[0]);
/* 4793 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4796 */       establishedSubject = P8CE_Util.associateSubject();
/* 4797 */       super.setActive();
/* 4798 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4802 */       if (establishedSubject) {
/* 4803 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setInactive(String reasonForInactive)
/*      */   {
/* 4812 */     Tracer.traceMethodEntry(new Object[0]);
/* 4813 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4816 */       establishedSubject = P8CE_Util.associateSubject();
/* 4817 */       super.setInactive(reasonForInactive);
/* 4818 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4822 */       if (establishedSubject) {
/* 4823 */         P8CE_Util.disassociateSubject();
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
/*      */   public List<BulkItemResult> initiateDisposition(Object vwSession)
/*      */   {
/* 4839 */     Tracer.traceMethodEntry(new Object[] { vwSession });
/* 4840 */     Util.ckNullObjParam("vwSession", vwSession);
/* 4841 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4844 */       establishedSubject = P8CE_Util.associateSubject();
/* 4845 */       List<BulkItemResult> results = super.initiateDisposition(vwSession);
/* 4846 */       Tracer.traceMethodExit(new Object[] { results });
/* 4847 */       return results;
/*      */     }
/*      */     finally
/*      */     {
/* 4851 */       if (establishedSubject) {
/* 4852 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DomainType getDomainType()
/*      */   {
/* 4861 */     return DomainType.P8_CE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canContainDefensibleDisposalContainer()
/*      */   {
/* 4870 */     Tracer.traceMethodEntry(new Object[0]);
/* 4871 */     boolean result = false;
/* 4872 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 4873 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordContainer addDefensiblyDisposableContainer(String triggerPropertyName, int retentionInYears, int retentionInMonths, int retentionInDays, RMProperties props, List<RMPermission> perms, String idStr)
/*      */   {
/* 4883 */     Tracer.traceMethodEntry(new Object[] { triggerPropertyName, Integer.valueOf(retentionInYears), Integer.valueOf(retentionInMonths), Integer.valueOf(retentionInDays), props, perms, idStr });
/* 4884 */     Util.ckInvalidStrParam("triggerPropertyName", triggerPropertyName);
/* 4885 */     validateDDRetentionNumbers(retentionInYears, retentionInMonths, retentionInDays);
/*      */     
/* 4887 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4890 */       establishedSubject = P8CE_Util.associateSubject();
/* 4891 */       RMRuntimeException noDDEx = verifyDDMetadataIsInstalled();
/* 4892 */       if (noDDEx != null)
/*      */       {
/* 4894 */         throw noDDEx;
/*      */       }
/*      */       
/* 4897 */       if (!canContainDefensibleDisposalContainer())
/*      */       {
/* 4899 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DEFENDISPOS_CONTAINER_INVALID_PARENT_TYPE, new Object[0]);
/*      */       }
/*      */       
/* 4902 */       this.isAddDefensDisposalContainerInProgress = true;
/*      */       
/*      */ 
/* 4905 */       Set<String> applicableClassNames = validateDDTriggerProperty(triggerPropertyName);
/* 4906 */       if (applicableClassNames.size() == 0)
/*      */       {
/* 4908 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_SPECIFIED_TRIGGER_PROPERTY_NOT_RECORD_PROPERTY, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/* 4912 */       if (props.isPropertyPresent("DisposalSchedule"))
/*      */       {
/* 4914 */         if (props.get("DisposalSchedule").getObjectValue() != null)
/*      */         {
/* 4916 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ASSIGN_DISPSCHEDULE_TO_DD_CONTAINER, new Object[0]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 4921 */       props.putStringValue("RMRetentionTriggerPropertyName", triggerPropertyName);
/* 4922 */       String ddRetentionValue = String.format("%04d-%04d-%04d", new Object[] { Integer.valueOf(retentionInYears), Integer.valueOf(retentionInMonths), Integer.valueOf(retentionInDays) });
/* 4923 */       props.putStringValue("RMRetentionPeriod", ddRetentionValue);
/*      */       
/*      */ 
/* 4926 */       RecordContainer result = addRecordCategory("RecordCategory", props, perms, idStr);
/* 4927 */       Tracer.traceMethodExit(new Object[] { result });
/* 4928 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4932 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 4936 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 4940 */       if (establishedSubject) {
/* 4941 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMRuntimeException isEligibleForConversion(String triggerPropertyName)
/*      */   {
/* 4950 */     Tracer.traceMethodEntry(new Object[] { triggerPropertyName });
/* 4951 */     Util.ckInvalidStrParam("triggerPropertyName", triggerPropertyName);
/* 4952 */     RMRuntimeException failureException = null;
/* 4953 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 4956 */       establishedSubject = P8CE_Util.associateSubject();
/* 4957 */       resolve();
/*      */       
/* 4959 */       RMRuntimeException noDDEx = verifyDDMetadataIsInstalled();
/* 4960 */       if (noDDEx != null)
/*      */       {
/* 4962 */         failureException = noDDEx;
/*      */       }
/* 4964 */       else if (getEntityType() != EntityType.RecordCategory)
/*      */       {
/* 4966 */         failureException = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_WRONG_CONTAINERTYPE, new Object[0]);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 4971 */         refresh(new String[] { "CurrentPhaseExecutionStatus" });
/* 4972 */         Integer curPhExeStatus = this.jaceFolder.getProperties().getInteger32Value("CurrentPhaseExecutionStatus");
/* 4973 */         if ((curPhExeStatus != null) && (curPhExeStatus.intValue() == RMWorkflowStatus.Started.getIntValue()))
/*      */         {
/* 4975 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_DISPOSITION_IN_PROGRESS, new Object[] { getObjectIdentity() });
/*      */         }
/*      */         
/*      */ 
/* 4979 */         PageableSet<Container> subContainerSet = getImmediateSubContainers(Integer.valueOf(1));
/* 4980 */         if (!subContainerSet.isEmpty())
/*      */         {
/* 4982 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_LEAF_NODE, new Object[] { getObjectIdentity() });
/*      */         }
/*      */         
/*      */ 
/* 4986 */         Set<String> applicableRecordClassNames = validateDDTriggerProperty(triggerPropertyName);
/* 4987 */         if (applicableRecordClassNames.size() == 0)
/*      */         {
/* 4989 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_SPECIFIED_TRIGGER_PROPERTY_NOT_RECORD_PROPERTY, new Object[0]);
/*      */         }
/*      */         
/*      */ 
/* 4993 */         validateContainedRecordsForDDeligibility(applicableRecordClassNames);
/*      */       }
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 4998 */       failureException = ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 5002 */       failureException = P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 5006 */       if (establishedSubject) {
/* 5007 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/* 5010 */     Tracer.traceMethodExit(new Object[] { failureException });
/* 5011 */     return failureException;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void convertToDefensiblyDisposable(String triggerPropertyName, int retentionInYears, int retentionInMonths, int retentionInDays, boolean skipValidation)
/*      */   {
/* 5020 */     Tracer.traceMethodEntry(new Object[] { triggerPropertyName, Integer.valueOf(retentionInYears), Integer.valueOf(retentionInMonths), Integer.valueOf(retentionInDays), Boolean.valueOf(skipValidation) });
/* 5021 */     Util.ckInvalidStrParam("triggerPropertyName", triggerPropertyName);
/* 5022 */     validateDDRetentionNumbers(retentionInYears, retentionInMonths, retentionInDays);
/*      */     
/* 5024 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 5027 */       establishedSubject = P8CE_Util.associateSubject();
/* 5028 */       resolve();
/*      */       
/* 5030 */       RMRuntimeException noDDEx = verifyDDMetadataIsInstalled();
/* 5031 */       if (noDDEx != null)
/*      */       {
/* 5033 */         throw noDDEx;
/*      */       }
/*      */       
/* 5036 */       if (getEntityType() != EntityType.RecordCategory)
/*      */       {
/* 5038 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_WRONG_CONTAINERTYPE, new Object[0]);
/*      */       }
/*      */       
/* 5041 */       if (!skipValidation)
/*      */       {
/* 5043 */         RMRuntimeException invalidEx = isEligibleForConversion(triggerPropertyName);
/* 5044 */         if (invalidEx != null) {
/* 5045 */           throw invalidEx;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5050 */       clearDispositionAssignment(SchedulePropagation.ToAllInheritors);
/* 5051 */       RMProperties props = getProperties();
/* 5052 */       super.resetDispositionData(props);
/* 5053 */       props.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(5));
/* 5054 */       props.putGuidValue("DisposalScheduleInheritedFrom", null);
/*      */       
/*      */ 
/* 5057 */       props.putStringValue("RMRetentionTriggerPropertyName", triggerPropertyName);
/* 5058 */       String ddRetentionValue = String.format("%04d-%04d-%04d", new Object[] { Integer.valueOf(retentionInYears), Integer.valueOf(retentionInMonths), Integer.valueOf(retentionInDays) });
/* 5059 */       props.putStringValue("RMRetentionPeriod", ddRetentionValue);
/*      */       
/*      */ 
/* 5062 */       props.putIntegerListValue("AllowedRMTypes", RALBaseContainer.DDAllowedEntityTypes);
/*      */       
/* 5064 */       internalSave(RMRefreshMode.Refresh);
/* 5065 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 5069 */       if (establishedSubject) {
/* 5070 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isADefensiblyDisposableContainer()
/*      */   {
/* 5079 */     Tracer.traceMethodEntry(new Object[0]);
/* 5080 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 5083 */       establishedSubject = P8CE_Util.associateSubject();
/* 5084 */       resolve();
/*      */       
/* 5086 */       boolean result = false;
/* 5087 */       String[] desiredProps; if ((this.entityType == EntityType.RecordCategory) && (verifyDDMetadataIsInstalled() == null))
/*      */       {
/*      */ 
/*      */ 
/* 5091 */         desiredProps = new String[] { "RMRetentionTriggerPropertyName", "RMRetentionPeriod" };
/* 5092 */         this.jaceFolder.fetchProperties(desiredProps);
/* 5093 */         String triggerPropName = this.jaceFolder.getProperties().getStringValue("RMRetentionTriggerPropertyName");
/* 5094 */         String retentionPeriod = this.jaceFolder.getProperties().getStringValue("RMRetentionPeriod");
/* 5095 */         if ((triggerPropName != null) && (triggerPropName.trim().length() > 0) && (retentionPeriod != null) && (retentionPeriod.trim().length() > 0))
/*      */         {
/*      */ 
/* 5098 */           result = true;
/*      */         }
/*      */       }
/*      */       
/* 5102 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 5103 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 5107 */       if (establishedSubject) {
/* 5108 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getTriggerPropertyName()
/*      */   {
/* 5117 */     Tracer.traceMethodEntry(new Object[0]);
/* 5118 */     String result = null;
/* 5119 */     if (isADefensiblyDisposableContainer())
/*      */     {
/* 5121 */       result = this.jaceFolder.getProperties().getStringValue("RMRetentionTriggerPropertyName");
/*      */     }
/* 5123 */     Tracer.traceMethodExit(new Object[] { result });
/* 5124 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTriggerPropertyName(String triggerPropertyName)
/*      */   {
/* 5132 */     Tracer.traceMethodEntry(new Object[] { triggerPropertyName });
/* 5133 */     Util.ckInvalidStrParam("triggerPropertyName", triggerPropertyName);
/*      */     
/*      */ 
/*      */ 
/* 5137 */     Properties jaceProps = this.jaceFolder.getProperties();
/* 5138 */     preventDirectSettingOfDefensDisposalProps(jaceProps);
/*      */     
/* 5140 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 5143 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 5148 */       List<Property> dirtyProps = P8CE_Util.findDirtyProperties(jaceProps);
/* 5149 */       resolve();
/*      */       
/* 5151 */       if (!isADefensiblyDisposableContainer())
/*      */       {
/* 5153 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_ALREADY_A_DD_CONTAINER, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 5159 */       RMRuntimeException invalidEx = isEligibleForConversion(triggerPropertyName);
/* 5160 */       if (invalidEx != null) {
/* 5161 */         throw invalidEx;
/*      */       }
/*      */       
/* 5164 */       jaceProps = this.jaceFolder.getProperties();
/* 5165 */       for (Property dirtyProp : dirtyProps)
/*      */       {
/* 5167 */         jaceProps.putObjectValue(dirtyProp.getPropertyName(), dirtyProp.getObjectValue());
/*      */       }
/* 5169 */       getProperties().putStringValue("RMRetentionTriggerPropertyName", triggerPropertyName);
/* 5170 */       internalSave(RMRefreshMode.Refresh);
/* 5171 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 5175 */       if (establishedSubject) {
/* 5176 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int[] getRetentionPeriod()
/*      */   {
/* 5185 */     Tracer.traceMethodEntry(new Object[0]);
/* 5186 */     int[] result = null;
/* 5187 */     if (isADefensiblyDisposableContainer())
/*      */     {
/*      */       try
/*      */       {
/* 5191 */         String rawRetentionValue = this.jaceFolder.getProperties().getStringValue("RMRetentionPeriod");
/* 5192 */         String[] rawStrs = rawRetentionValue.trim().split("-");
/* 5193 */         int years = Integer.parseInt(rawStrs[0], 10);
/* 5194 */         int months = Integer.parseInt(rawStrs[1], 10);
/* 5195 */         int days = Integer.parseInt(rawStrs[2], 10);
/*      */         
/* 5197 */         result = new int[] { years, months, days };
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 5201 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DD_TRIGPROPNAME_BAD_VALUE, new Object[0]);
/*      */       }
/*      */     }
/*      */     
/* 5205 */     Tracer.traceMethodExit(new Object[] { result });
/* 5206 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRetentionPeriod(int retentionInYears, int retentionInMonths, int retentionInDays)
/*      */   {
/* 5214 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(retentionInYears), Integer.valueOf(retentionInMonths), Integer.valueOf(retentionInDays) });
/* 5215 */     validateDDRetentionNumbers(retentionInYears, retentionInMonths, retentionInDays);
/*      */     
/*      */ 
/*      */ 
/* 5219 */     Properties jaceProps = this.jaceFolder.getProperties();
/* 5220 */     preventDirectSettingOfDefensDisposalProps(jaceProps);
/*      */     
/* 5222 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 5225 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 5230 */       List<Property> dirtyProps = P8CE_Util.findDirtyProperties(jaceProps);
/* 5231 */       resolve();
/*      */       
/* 5233 */       if (!isADefensiblyDisposableContainer())
/*      */       {
/* 5235 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_ALREADY_A_DD_CONTAINER, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/*      */ 
/* 5239 */       jaceProps = this.jaceFolder.getProperties();
/* 5240 */       for (Property dirtyProp : dirtyProps)
/*      */       {
/* 5242 */         jaceProps.putObjectValue(dirtyProp.getPropertyName(), dirtyProp.getObjectValue());
/*      */       }
/* 5244 */       String ddRetentionValue = String.format("%04d-%04d-%04d", new Object[] { Integer.valueOf(retentionInYears), Integer.valueOf(retentionInMonths), Integer.valueOf(retentionInDays) });
/* 5245 */       getProperties().putStringValue("RMRetentionPeriod", ddRetentionValue);
/* 5246 */       internalSave(RMRefreshMode.Refresh);
/* 5247 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 5251 */       if (establishedSubject) {
/* 5252 */         P8CE_Util.disassociateSubject();
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
/*      */   protected RMRuntimeException verifyDDMetadataIsInstalled()
/*      */   {
/* 5265 */     Tracer.traceMethodEntry(new Object[0]);
/* 5266 */     RMRuntimeException result = null;
/* 5267 */     FilePlanRepository fpRepos = (FilePlanRepository)getRepository();
/* 5268 */     if (!fpRepos.supportsDefensibleDisposal())
/*      */     {
/* 5270 */       String fpReposName = fpRepos.getDisplayName();
/* 5271 */       result = RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DD_METADATA_NOT_INSTALLED, new Object[] { fpReposName });
/*      */     }
/* 5273 */     Tracer.traceMethodExit(new Object[] { result });
/* 5274 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void validateDDRetentionNumbers(int retentionInYears, int retentionInMonths, int retentionInDays)
/*      */   {
/* 5286 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(retentionInYears), Integer.valueOf(retentionInMonths), Integer.valueOf(retentionInDays) });
/* 5287 */     if ((retentionInYears < 0) || (retentionInYears > 9999) || (retentionInMonths < 0) || (retentionInMonths > 9999) || (retentionInDays < 0) || (retentionInDays > 9999))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5294 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_RETENTION_NUMBER_OUT_OF_RANGE, new Object[0]);
/*      */     }
/* 5296 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   Set<String> validateDDTriggerProperty(String triggerPropertyName)
/*      */   {
/* 5314 */     Tracer.traceMethodEntry(new Object[] { triggerPropertyName });
/*      */     
/* 5316 */     MetadataCache jaceMetaCache = Factory.MetadataCache.getDefaultInstance();
/* 5317 */     ObjectStore jaceFPOSBase = ((P8CE_RepositoryImpl)getRepository()).getJaceObjectStore();
/* 5318 */     ClassDescription jaceRecInfoCD = jaceMetaCache.getClassDescription(jaceFPOSBase, "RecordInfo");
/*      */     
/*      */ 
/*      */ 
/* 5322 */     Set<String> applicableClassNames = new HashSet();
/* 5323 */     findTriggerPropertyClassUsages(applicableClassNames, jaceRecInfoCD, triggerPropertyName, false);
/* 5324 */     if (applicableClassNames.size() == 0)
/*      */     {
/* 5326 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_SPECIFIED_TRIGGER_PROPERTY_NOT_RECORD_PROPERTY, new Object[] { triggerPropertyName });
/*      */     }
/*      */     
/* 5329 */     Tracer.traceMethodExit(new Object[] { applicableClassNames });
/* 5330 */     return applicableClassNames;
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
/*      */   private void findTriggerPropertyClassUsages(Set<String> applicableClassNameSet, ClassDescription currentJaceCD, String triggerPropSymName, boolean skipCheck)
/*      */   {
/* 5351 */     Tracer.traceMethodEntry(new Object[] { applicableClassNameSet, currentJaceCD, triggerPropSymName, Boolean.valueOf(skipCheck) });
/* 5352 */     boolean foundPropertyInCurrentClass = false;
/* 5353 */     if (!skipCheck)
/*      */     {
/*      */ 
/* 5356 */       PropertyDescriptionList jacePDList = currentJaceCD.get_PropertyDescriptions();
/* 5357 */       PropertyDescription jacePD = null;
/* 5358 */       for (Iterator it = jacePDList.iterator(); it.hasNext();)
/*      */       {
/* 5360 */         jacePD = (PropertyDescription)it.next();
/* 5361 */         if (triggerPropSymName.equalsIgnoreCase(jacePD.get_SymbolicName()))
/*      */         {
/* 5363 */           TypeID propTypeID = jacePD.get_DataType();
/* 5364 */           Cardinality propCardinality = jacePD.get_Cardinality();
/* 5365 */           if ((TypeID.DATE.equals(propTypeID)) && (Cardinality.SINGLE.equals(propCardinality)))
/*      */           {
/* 5367 */             foundPropertyInCurrentClass = true;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 5372 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_SPECIFIED_TRIGGER_PROPERTY_NOT_SV_DATETIME, new Object[] { triggerPropSymName });
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 5377 */       if (foundPropertyInCurrentClass)
/*      */       {
/*      */ 
/* 5380 */         applicableClassNameSet.add(currentJaceCD.get_SymbolicName());
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 5387 */       applicableClassNameSet.add(currentJaceCD.get_SymbolicName());
/*      */     }
/*      */     
/*      */ 
/* 5391 */     boolean outgoingSkipCheck = skipCheck | foundPropertyInCurrentClass;
/* 5392 */     ClassDescriptionSet jaceSubCDSet = currentJaceCD.get_ImmediateSubclassDescriptions();
/* 5393 */     ClassDescription jaceSubCD = null;
/* 5394 */     for (Iterator it = jaceSubCDSet.iterator(); it.hasNext();)
/*      */     {
/* 5396 */       jaceSubCD = (ClassDescription)it.next();
/* 5397 */       findTriggerPropertyClassUsages(applicableClassNameSet, jaceSubCD, triggerPropSymName, outgoingSkipCheck);
/*      */     }
/*      */     
/* 5400 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   private void validateContainedRecordsForDDeligibility(Set<String> acceptableRecordClassSet)
/*      */   {
/* 5420 */     Tracer.traceMethodEntry(new Object[] { acceptableRecordClassSet });
/* 5421 */     P8CE_FilePlanRepositoryImpl fpRepository = (P8CE_FilePlanRepositoryImpl)getRepository();
/* 5422 */     ObjectStore jaceObjStore = fpRepository.getJaceObjectStore();
/*      */     
/* 5424 */     if (fpRepository.isRecordMultiFilingEnabled())
/*      */     {
/*      */ 
/*      */ 
/* 5428 */       String sqlStr = String.format("SELECT TOP 1 rcr1.Id, rcr1.Head FROM DynamicReferentialContainmentRelationship rcr1 WITH EXCLUDESUBCLASSES WHERE EXISTS(SELECT * FROM DynamicReferentialContainmentRelationship rcr2 WITH EXCLUDESUBCLASSES WHERE rcr2.Head = rcr1.Head AND rcr2.Id <> rcr1.Id)   AND rcr1.Tail = OBJECT('%s') ", new Object[] { getObjectIdentity() });
/* 5429 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStr);
/* 5430 */       SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/* 5431 */       RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, null, null, Boolean.FALSE);
/* 5432 */       if (!rowSet.isEmpty())
/*      */       {
/* 5434 */         RepositoryRow row = (RepositoryRow)rowSet.iterator().next();
/* 5435 */         IndependentObject jaceRecBase = (IndependentObject)row.getProperties().getEngineObjectValue("Head");
/* 5436 */         String recIdent = jaceRecBase.getObjectReference().getObjectIdentity();
/* 5437 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_MULTIFILED_RECORDS, new Object[] { recIdent });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5444 */     String sqlStr = String.format("SELECT r.Id, r.RMEntityType, r.AssociatedRecordType, r.ClassDescription, r.CurrentPhaseExecutionStatus FROM RecordInfo r INNER JOIN ReferentialContainmentRelationship rcr ON r.This = rcr.Head WHERE (rcr.Tail = OBJECT('%s'))   AND (r.IsDeleted = FALSE) ", new Object[] { getObjectIdentity() });
/* 5445 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStr);
/* 5446 */     SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/* 5447 */     Integer pageSize = null;
/* 5448 */     PropertyFilter jacePF = new PropertyFilter();
/* 5449 */     jacePF.addIncludeProperty(1, null, null, "Id, RMEntityType, AssociatedRecordType, SymbolicName", pageSize);
/* 5450 */     RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, null, null, Boolean.FALSE);
/* 5451 */     PageIterator pi = rowSet.pageIterator();
/* 5452 */     Object[] rawObjs = null;
/* 5453 */     Properties rowProps = null;
/* 5454 */     Integer inDispProcessing = Integer.valueOf(RMWorkflowStatus.Started.getIntValue());
/* 5455 */     Integer physRecType = Integer.valueOf(EntityType.PhysicalRecord.getIntValue());
/* 5456 */     while (pi.nextPage())
/*      */     {
/* 5458 */       rawObjs = pi.getCurrentPage();
/* 5459 */       for (int i = 0; i < rawObjs.length; i++)
/*      */       {
/* 5461 */         rowProps = ((RepositoryRow)rawObjs[i]).getProperties();
/* 5462 */         String recIdent = rowProps.getIdValue("Id").toString();
/*      */         
/* 5464 */         if (inDispProcessing.equals(rowProps.getInteger32Value("CurrentPhaseExecutionStatus")))
/*      */         {
/* 5466 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_DISPOSITION_IN_PROGRESS, new Object[] { recIdent });
/*      */         }
/*      */         
/* 5469 */         if (physRecType.equals(rowProps.getInteger32Value("RMEntityType")))
/*      */         {
/* 5471 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_PHYSREC, new Object[] { recIdent });
/*      */         }
/*      */         
/* 5474 */         if (rowProps.getEngineObjectValue("AssociatedRecordType") != null)
/*      */         {
/* 5476 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_RECORDTYPE, new Object[] { recIdent });
/*      */         }
/*      */         
/* 5479 */         String recClassName = ((ClassDescription)rowProps.getEngineObjectValue("ClassDescription")).get_SymbolicName();
/* 5480 */         if (!acceptableRecordClassSet.contains(recClassName))
/*      */         {
/* 5482 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_NOT_DD_ELIGIBLE_RECORDCLASS, new Object[] { recIdent });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5487 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */   private void updateAllowedEntityTypes()
/*      */   {
/* 5493 */     Tracer.traceMethodEntry(new Object[0]);
/* 5494 */     EntityType[] allowedEntityTypes = determineAllowedContaineeTypes();
/* 5495 */     setAllowedContaineeTypes(allowedEntityTypes);
/* 5496 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean userCanFileIntoClosedContainer(FilePlanRepository fpRepos)
/*      */   {
/* 5508 */     Tracer.traceMethodEntry(new Object[0]);
/* 5509 */     boolean result = false;
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 5515 */       P8CE_FilePlanRepositoryImpl p8ce_fpRepos = (P8CE_FilePlanRepositoryImpl)fpRepos;
/* 5516 */       ObjectStore jaceObjStore = p8ce_fpRepos.getJaceObjectStore();
/* 5517 */       String fpRootPath = p8ce_fpRepos.getFilePlanRootPath();
/* 5518 */       PropertyFilter jacePF = new PropertyFilter();
/* 5519 */       jacePF.addIncludeProperty(0, null, null, "Id", null);
/* 5520 */       Folder jaceRootBase = Factory.Folder.fetchInstance(jaceObjStore, fpRootPath, jacePF);
/* 5521 */       int accessMask = jaceRootBase.getAccessAllowed().intValue();
/*      */       
/* 5523 */       result = accessMask >= 987127;
/*      */ 
/*      */     }
/*      */     catch (EngineRuntimeException ere)
/*      */     {
/*      */ 
/* 5529 */       result = false;
/*      */     }
/*      */     
/* 5532 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 5533 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isAnyChildContainerInActivePhase()
/*      */   {
/* 5544 */     Tracer.traceMethodEntry(new Object[0]);
/* 5545 */     boolean result = false;
/* 5546 */     String idStr = getObjectIdentity();
/*      */     
/* 5548 */     StringBuilder sb = new StringBuilder();
/* 5549 */     sb.append("SELECT TOP 1 rf.").append("Id");
/* 5550 */     sb.append(" FROM ").append("RMFolder").append(" rf");
/* 5551 */     sb.append(" WHERE rf.This INSUBFOLDER '").append(idStr).append("' ");
/* 5552 */     sb.append(" AND rf.").append("CurrentPhaseExecutionDate").append(" IS NOT NULL");
/* 5553 */     sb.append(" AND rf.").append("IsDeleted").append(" = FALSE");
/* 5554 */     sb.append(" AND rf.").append("IsFinalPhaseCompleted").append(" = FALSE");
/* 5555 */     sb.append(" AND rf.").append("LastSweepDate").append(" IS NOT NULL");
/* 5556 */     String sqlStmt = sb.toString();
/* 5557 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 5558 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/* 5559 */     Integer pageSize = Integer.valueOf(1);
/*      */     
/* 5561 */     long startTime = System.currentTimeMillis();
/* 5562 */     RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 5563 */     long endTime = System.currentTimeMillis();
/* 5564 */     Boolean elementCountIndicator = null;
/* 5565 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 5567 */       elementCountIndicator = Boolean.valueOf(rowSet.isEmpty());
/*      */     }
/* 5569 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, rowSet, new Object[] { sqlStmt, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */     
/* 5571 */     result = !rowSet.isEmpty();
/* 5572 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 5573 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isAnyChildRecordInActivePhase()
/*      */   {
/* 5584 */     Tracer.traceMethodEntry(new Object[0]);
/* 5585 */     boolean result = false;
/* 5586 */     String idStr = getObjectIdentity();
/*      */     
/* 5588 */     StringBuilder sb = new StringBuilder();
/* 5589 */     sb.append("SELECT TOP 1 rec.").append("Id");
/* 5590 */     sb.append(" FROM ").append("RecordInfo").append(" rec");
/* 5591 */     sb.append(" WHERE rec.This INSUBFOLDER '").append(idStr).append("' ");
/* 5592 */     sb.append(" AND rec.").append("CurrentPhaseExecutionDate").append(" IS NOT NULL");
/* 5593 */     sb.append(" AND rec.").append("IsDeleted").append(" = FALSE");
/* 5594 */     sb.append(" AND rec.").append("IsFinalPhaseCompleted").append(" = FALSE");
/* 5595 */     sb.append(" AND rec.").append("LastSweepDate").append(" IS NOT NULL");
/* 5596 */     String sqlStmt = sb.toString();
/* 5597 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 5598 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/* 5599 */     Integer pageSize = Integer.valueOf(1);
/*      */     
/* 5601 */     long startTime = System.currentTimeMillis();
/* 5602 */     RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 5603 */     long endTime = System.currentTimeMillis();
/* 5604 */     Boolean elementCountIndicator = null;
/* 5605 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 5607 */       elementCountIndicator = Boolean.valueOf(rowSet.isEmpty());
/*      */     }
/* 5609 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, rowSet, new Object[] { sqlStmt, pageSize, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */     
/* 5611 */     result = !rowSet.isEmpty();
/* 5612 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 5613 */     return result;
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
/*      */   protected boolean isContainerAHierarchyChild(String containerIdent)
/*      */   {
/* 5627 */     Tracer.traceMethodEntry(new Object[] { containerIdent });
/*      */     
/* 5629 */     String thisIdent = getObjectIdentity();
/* 5630 */     String sqlStmt = String.format(SqlForVerifySubContainerExistence, new Object[] { thisIdent, containerIdent });
/* 5631 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 5632 */     SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*      */     
/* 5634 */     long startTime = System.currentTimeMillis();
/* 5635 */     RepositoryRowSet resultRows = jaceSearchScope.fetchRows(jaceSearchSQL, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE);
/* 5636 */     long endTime = System.currentTimeMillis();
/* 5637 */     Boolean elementCountIndicator = null;
/* 5638 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 5640 */       elementCountIndicator = resultRows != null ? Boolean.valueOf(resultRows.isEmpty()) : null;
/*      */     }
/* 5642 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, resultRows, new Object[] { sqlStmt, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */     
/* 5644 */     boolean result = (resultRows != null) && (!resultRows.isEmpty());
/* 5645 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 5646 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<CustomObject> findAutoDeletable_PSSAtlas_artifacts()
/*      */   {
/* 5658 */     Tracer.traceMethodEntry(new Object[0]);
/* 5659 */     List<CustomObject> results = new ArrayList(0);
/*      */     
/*      */ 
/* 5662 */     if ((this.entityType == EntityType.RecordCategory) || (isARecordFolderType(this.entityType)))
/*      */     {
/*      */ 
/* 5665 */       FilePlanRepository fpRepos = (FilePlanRepository)getRepository();
/*      */       try
/*      */       {
/* 5668 */         if (fpRepos.supportsExternalManagement())
/*      */         {
/*      */ 
/*      */ 
/* 5672 */           PropertyFilter jacePF = getJacePFforPSSAtlasInfo();
/* 5673 */           this.jaceFolder.fetchProperties(jacePF);
/* 5674 */           Properties jaceFolderProps = this.jaceFolder.getProperties();
/*      */           
/*      */ 
/* 5677 */           String extManagedByValue = jaceFolderProps.getStringValue("RMExternallyManagedBy");
/*      */           
/* 5679 */           CustomObject jaceScheduleBase = (CustomObject)jaceFolderProps.getEngineObjectValue("DisposalSchedule");
/* 5680 */           Id scheduleInheritedFrom = jaceFolderProps.getIdValue("DisposalScheduleInheritedFrom");
/* 5681 */           if ((jaceScheduleBase != null) && ((scheduleInheritedFrom == null) || (scheduleInheritedFrom.equals(this.jaceFolder.get_Id()))))
/*      */           {
/*      */ 
/*      */ 
/* 5685 */             Properties jaceSchedProps = jaceScheduleBase.getProperties();
/* 5686 */             extManagedByValue = jaceSchedProps.getStringValue("RMExternallyManagedBy");
/* 5687 */             if ("Atlas".equalsIgnoreCase(extManagedByValue))
/*      */             {
/* 5689 */               CustomObject jaceTriggerBase = (CustomObject)jaceSchedProps.getEngineObjectValue("CutoffDisposalTrigger");
/* 5690 */               if (jaceTriggerBase != null)
/*      */               {
/*      */ 
/* 5693 */                 extManagedByValue = jaceTriggerBase.getProperties().getStringValue("RMExternallyManagedBy");
/* 5694 */                 if ("Atlas".equalsIgnoreCase(extManagedByValue))
/*      */                 {
/*      */ 
/* 5697 */                   results.add(jaceScheduleBase);
/* 5698 */                   results.add(jaceTriggerBase);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 5707 */         Logger.warn(RMLogCode.PSS_Atlas_ARTIFACT_DELETE_FAILURE, ex, new Object[] { ex.getLocalizedMessage() });
/*      */       }
/*      */     }
/*      */     
/* 5711 */     Tracer.traceMethodExit(new Object[] { results });
/* 5712 */     return results;
/*      */   }
/*      */   
/*      */   private synchronized PropertyFilter getJacePFforPSSAtlasInfo()
/*      */   {
/* 5717 */     Tracer.traceMethodEntry(new Object[0]);
/* 5718 */     if (JacePF_ForPSSAtlasInfo == null)
/*      */     {
/* 5720 */       PropertyFilter pf = new PropertyFilter();
/* 5721 */       String level1PropNames = "DisposalSchedule DisposalScheduleInheritedFrom";
/*      */       
/* 5723 */       pf.addIncludeProperty(1, null, null, level1PropNames, null);
/*      */       
/* 5725 */       String level2PropNames = "CutoffDisposalTrigger";
/* 5726 */       pf.addIncludeProperty(2, null, null, level2PropNames, null);
/*      */       
/* 5728 */       String level3PropNames = "RMExternallyManagedBy";
/* 5729 */       pf.addIncludeProperty(3, null, null, level3PropNames, null);
/*      */       
/* 5731 */       JacePF_ForPSSAtlasInfo = pf;
/*      */     }
/*      */     
/* 5734 */     Tracer.traceMethodExit(new Object[] { JacePF_ForPSSAtlasInfo });
/* 5735 */     return JacePF_ForPSSAtlasInfo;
/*      */   }
/*      */   
/*      */   private void deletePSSAtlasDispositionArtifacts(List<CustomObject> deletablePSSAtlasArtifacts, Domain jaceDomain)
/*      */   {
/* 5740 */     Tracer.traceMethodEntry(new Object[] { deletablePSSAtlasArtifacts });
/* 5741 */     if ((deletablePSSAtlasArtifacts != null) && (deletablePSSAtlasArtifacts.size() == 2))
/*      */     {
/*      */       try
/*      */       {
/* 5745 */         UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.NO_REFRESH);
/*      */         
/* 5747 */         CustomObject jaceSchedBase = (CustomObject)deletablePSSAtlasArtifacts.get(0);
/* 5748 */         jaceSchedBase.getProperties().putValue("CutoffDisposalTrigger", (EngineObject)null);
/* 5749 */         jaceSchedBase.delete();
/* 5750 */         jaceUB.add(jaceSchedBase, P8CE_Util.CEPF_Empty);
/* 5751 */         CustomObject jaceTriggerBase = (CustomObject)deletablePSSAtlasArtifacts.get(1);
/* 5752 */         jaceTriggerBase.delete();
/* 5753 */         jaceUB.add(jaceTriggerBase, P8CE_Util.CEPF_Empty);
/*      */         
/* 5755 */         jaceUB.updateBatch();
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 5759 */         Logger.warn(RMLogCode.PSS_Atlas_ARTIFACT_DELETE_FAILURE, ex, new Object[] { ex.getLocalizedMessage() });
/*      */       }
/*      */     }
/* 5762 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void preventDirectSettingOfDefensDisposalProps(Properties jaceProps)
/*      */   {
/* 5769 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*      */ 
/* 5772 */     String[] ddPropNames = { "RMRetentionTriggerPropertyName", "RMRetentionPeriod" };
/* 5773 */     for (String ddPropName : ddPropNames)
/*      */     {
/* 5775 */       if (jaceProps.isPropertyPresent(ddPropName))
/*      */       {
/* 5777 */         if (jaceProps.get(ddPropName).isDirty())
/*      */         {
/* 5779 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DD_PROPS_NOT_DIRECTLY_SETTABLE, new Object[0]);
/*      */         }
/*      */       }
/*      */     }
/* 5783 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void validateForDispositionExport()
/*      */   {
/* 5792 */     Tracer.traceMethodEntry(new Object[0]);
/* 5793 */     P8CE_Util.validateDispositionExport(this);
/* 5794 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   static class Generator
/*      */     implements IGenerator<Container>
/*      */   {
/*      */     public Container create(Repository repository, Object jaceBaseObject)
/*      */     {
/* 5811 */       P8CE_BaseContainerImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 5812 */       Folder jaceFolder = (Folder)jaceBaseObject;
/*      */       
/* 5814 */       EntityType entityType = EntityType.Unknown;
/* 5815 */       Properties jaceProps = ((EngineObject)jaceBaseObject).getProperties();
/* 5816 */       if (jaceProps.isPropertyPresent("RMEntityType"))
/*      */       {
/* 5818 */         Integer rawEntityType = jaceProps.getInteger32Value("RMEntityType");
/* 5819 */         entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*      */       }
/*      */       
/* 5822 */       if (entityType == EntityType.FilePlan)
/*      */       {
/*      */ 
/* 5825 */         String containerType = jaceFolder.getProperties().getStringValue("ContainerType");
/* 5826 */         if ("application/x-filenet-rm-classificationschemeroot".equalsIgnoreCase(containerType))
/*      */         {
/* 5828 */           entityType = EntityType.Unknown;
/*      */         }
/*      */       }
/*      */       
/* 5832 */       String identity = jaceFolder.get_Id().toString();
/* 5833 */       Container result = null;
/*      */       
/* 5835 */       if ((entityType == EntityType.Container) || (entityType == EntityType.Unknown))
/*      */       {
/* 5837 */         result = new P8CE_BaseContainerImpl(entityType, repository, identity, jaceFolder, false);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 5842 */         IGenerator<BaseEntity> generator = P8CE_Util.getEntityGenerator(entityType);
/*      */         
/*      */ 
/* 5845 */         result = (Container)generator.create(repository, jaceFolder);
/*      */       }
/*      */       
/* 5848 */       P8CE_BaseContainerImpl.Tracer.traceMethodExit(new Object[] { result });
/* 5849 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_BaseContainerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */