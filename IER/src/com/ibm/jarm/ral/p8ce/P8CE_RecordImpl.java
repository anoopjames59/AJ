/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.admin.CMODFixedContentDevice;
/*      */ import com.filenet.api.admin.FixedContentDevice;
/*      */ import com.filenet.api.admin.FixedStorageArea;
/*      */ import com.filenet.api.admin.IICEFixedContentDevice;
/*      */ import com.filenet.api.admin.IMFixedContentDevice;
/*      */ import com.filenet.api.admin.StorageArea;
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.ContentElementList;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.PageIterator;
/*      */ import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
/*      */ import com.filenet.api.constants.AutoClassify;
/*      */ import com.filenet.api.constants.AutoUniqueName;
/*      */ import com.filenet.api.constants.CheckinType;
/*      */ import com.filenet.api.constants.DefineSecurityParentage;
/*      */ import com.filenet.api.constants.PermissionSource;
/*      */ import com.filenet.api.constants.RefreshMode;
/*      */ import com.filenet.api.core.ContentElement;
/*      */ import com.filenet.api.core.ContentReference;
/*      */ import com.filenet.api.core.ContentTransfer;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.EngineObject;
/*      */ import com.filenet.api.core.Factory.AccessPermission;
/*      */ import com.filenet.api.core.Factory.ContentElement;
/*      */ import com.filenet.api.core.Factory.ContentReference;
/*      */ import com.filenet.api.core.Factory.ContentTransfer;
/*      */ import com.filenet.api.core.Factory.Document;
/*      */ import com.filenet.api.core.Factory.DynamicReferentialContainmentRelationship;
/*      */ import com.filenet.api.core.Factory.Link;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.Link;
/*      */ import com.filenet.api.core.ObjectReference;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*      */ import com.filenet.api.core.RetrievingBatch;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.Property;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.security.AccessPermission;
/*      */ import com.filenet.api.security.User;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.collection.RMPageIterator;
/*      */ import com.ibm.jarm.api.constants.AuditStatus;
/*      */ import com.ibm.jarm.api.constants.ContentXMLExport;
/*      */ import com.ibm.jarm.api.constants.DeleteMode;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.DomainType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMAccessRight;
/*      */ import com.ibm.jarm.api.constants.RMReceiptStatus;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*      */ import com.ibm.jarm.api.core.AuditEvent;
/*      */ import com.ibm.jarm.api.core.AuditableEntity;
/*      */ import com.ibm.jarm.api.core.BulkItemResult;
/*      */ import com.ibm.jarm.api.core.BulkOperation;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.ContentItem;
/*      */ import com.ibm.jarm.api.core.ContentRepository;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.ExternalExport;
/*      */ import com.ibm.jarm.api.core.FilePlan;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.Hold;
/*      */ import com.ibm.jarm.api.core.Location;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.RMFactory.ContentRepository;
/*      */ import com.ibm.jarm.api.core.RMFactory.RMProperties;
/*      */ import com.ibm.jarm.api.core.Record;
/*      */ import com.ibm.jarm.api.core.RecordContainer;
/*      */ import com.ibm.jarm.api.core.RecordType;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMProperty;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.util.JarmLogger;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.P8CE_Convert;
/*      */ import com.ibm.jarm.api.util.RMLString;
/*      */ import com.ibm.jarm.api.util.RMLogCode;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.common.RALBaseContainer;
/*      */ import com.ibm.jarm.ral.common.RALBaseRecord;
/*      */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*      */ import com.ibm.jarm.ral.common.RAL_XMLSupport;
/*      */ import java.io.File;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
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
/*      */ class P8CE_RecordImpl
/*      */   extends RALBaseRecord
/*      */   implements Record, JaceBasable, AuditableEntity
/*      */ {
/*      */   private static final int RECORDED_DOCUMENTS_PAGE_SIZE = 25;
/*  119 */   private static final JarmLogger Logger = JarmLogger.getJarmLogger(P8CE_RecordImpl.class.getName());
/*  120 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  121 */   private static final IGenerator<Record> RecordGenerator = new Generator();
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
/*  132 */   private static final String[] MandatoryPropertyNames = { "AGGREGATION", "Alias", "AssociatedRecordType", "BarcodeID", "CarbonCopy", "CurrentPhaseExecutionDate", "DocumentTitle", "EmailSubject", "Format", "From", "HomeLocation", "Id", "IsDeleted", "IsPermanentRecord", "IsVitalRecord", "Location", "MediaType", "MethodofDestruction", "Name", "UniqueRecordIdentifier", "OnHold", "OriginatingOrganization", "ReceivedOn", "Reviewer", "RMEntityDescription", "RMEntityType", "SentOn", "SupercedingRecords", "SupercededBy", "To", "CurrentPhaseID", "CurrentPhaseReviewComments", "CurrentActionType", "CurrentPhaseExecutionStatus", "CutoffDate", "CurrentPhaseAction", "CutoffInheritedFrom", "RecalculatePhaseRetention", "CurrentPhaseExportFormat", "CurrentPhaseExportDestination", "LastSweepDate", "CurrentPhaseDecisionDate" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final List<FilterElement> MandatoryJaceFEs;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private com.filenet.api.core.Document jaceDocument;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private FilePlan myFilePlan;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*  156 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*      */     
/*  158 */     List<FilterElement> tempList = new ArrayList(1);
/*  159 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  160 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*      */     
/*  162 */     String[] subArray = P8CE_BaseContainerImpl.getMandatoryPropertyNames();
/*  163 */     String[] fullArray = new String[subArray.length + 1];
/*  164 */     fullArray[0] = "SecurityFolder";
/*  165 */     System.arraycopy(subArray, 0, fullArray, 1, subArray.length);
/*      */   }
/*      */   
/*      */   static String[] getMandatoryPropertyNames()
/*      */   {
/*  170 */     return MandatoryPropertyNames;
/*      */   }
/*      */   
/*      */   static List<FilterElement> getMandatoryJaceFEs()
/*      */   {
/*  175 */     return MandatoryJaceFEs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static IGenerator<Record> getGenerator()
/*      */   {
/*  185 */     return RecordGenerator;
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
/*      */   public List<FilterElement> getMandatoryFEs()
/*      */   {
/*  198 */     return getMandatoryJaceFEs();
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
/*      */   P8CE_RecordImpl(EntityType entityType, Repository repository, String identity, com.filenet.api.core.Document jaceDocument, boolean isPlaceholder)
/*      */   {
/*  214 */     super(entityType, repository, identity, isPlaceholder);
/*  215 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, jaceDocument, Boolean.valueOf(isPlaceholder) });
/*  216 */     this.jaceDocument = jaceDocument;
/*  217 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EngineObject getJaceBaseObject()
/*      */   {
/*  225 */     return this.jaceDocument;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getObjectIdentity()
/*      */   {
/*  234 */     Tracer.traceMethodEntry(new Object[0]);
/*  235 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceDocument);
/*  236 */     Tracer.traceMethodExit(new Object[] { result });
/*  237 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Container> getContainedBy()
/*      */   {
/*  246 */     Tracer.traceMethodEntry(new Object[0]);
/*  247 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  250 */       establishedSubject = P8CE_Util.associateSubject();
/*  251 */       boolean excludeDeleted = true;
/*  252 */       List<Container> result = P8CE_Util.getContainedBy(getRepository(), this.jaceDocument, excludeDeleted);
/*      */       
/*  254 */       Tracer.traceMethodExit(new Object[] { result });
/*  255 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  259 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  263 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_PARENT_CONTAINER_FAILED, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  267 */       if (establishedSubject) {
/*  268 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  277 */     return this.jaceDocument.get_Name();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  285 */     Tracer.traceMethodEntry(new Object[0]);
/*  286 */     String result = P8CE_Util.getJaceObjectClassName(this.jaceDocument);
/*  287 */     Tracer.traceMethodExit(new Object[] { result });
/*  288 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<RMPermission> getPermissions()
/*      */   {
/*  296 */     Tracer.traceMethodEntry(new Object[0]);
/*  297 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceDocument);
/*  298 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*      */     
/*  300 */     Tracer.traceMethodExit(new Object[] { result });
/*  301 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPermissions(List<RMPermission> jarmPerms)
/*      */   {
/*  309 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/*      */     
/*  311 */     AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(jarmPerms);
/*  312 */     this.jaceDocument.set_Permissions(jacePerms);
/*      */     
/*  314 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMProperties getProperties()
/*      */   {
/*  322 */     Tracer.traceMethodEntry(new Object[0]);
/*  323 */     RMProperties result = null;
/*  324 */     if (this.jaceDocument != null)
/*      */     {
/*  326 */       result = new P8CE_RMPropertiesImpl(this.jaceDocument, this);
/*      */     }
/*      */     
/*  329 */     Tracer.traceMethodExit(new Object[] { result });
/*  330 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PageableSet<AuditEvent> getAuditedEvents(RMPropertyFilter filter)
/*      */   {
/*  338 */     Tracer.traceMethodEntry(new Object[] { filter });
/*  339 */     PageableSet<AuditEvent> resultSet = P8CE_Util.getAuditedEvents(this, filter);
/*  340 */     Tracer.traceMethodExit(new Object[] { resultSet });
/*  341 */     return resultSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void save(RMRefreshMode jarmRefreshMode)
/*      */   {
/*  349 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/*  350 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  353 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  355 */       if (!this.isCreationPending)
/*      */       {
/*  357 */         Properties jaceRecProps = this.jaceDocument.getProperties();
/*      */         
/*  359 */         if (((FilePlanRepository)getRepository()).supportsDefensibleDisposal())
/*      */         {
/*      */ 
/*  362 */           if (!this.jaceDocument.getProperties().isPropertyPresent("SecurityFolder"))
/*      */           {
/*  364 */             PropertyFilter pf = new PropertyFilter();
/*  365 */             pf.addIncludeProperty(1, null, null, "CurrentPhaseExecutionStatus SecurityFolder RMRetentionTriggerPropertyName RMRetentionPeriod", null);
/*  366 */             this.jaceDocument.fetchProperties(pf);
/*  367 */             jaceRecProps = this.jaceDocument.getProperties();
/*      */           }
/*  369 */           Folder jaceSecFldr = (Folder)this.jaceDocument.getProperties().getEngineObjectValue("SecurityFolder");
/*  370 */           if (jaceSecFldr != null)
/*      */           {
/*  372 */             String trigPropName = jaceSecFldr.getProperties().getStringValue("RMRetentionTriggerPropertyName");
/*  373 */             String retentionVal = jaceSecFldr.getProperties().getStringValue("RMRetentionPeriod");
/*  374 */             if ((trigPropName != null) && (trigPropName.trim().length() > 0) && (retentionVal != null) && (retentionVal.trim().length() > 0))
/*      */             {
/*      */ 
/*  377 */               if (P8CE_Util.isEntityInDDReport(this.jaceDocument))
/*      */               {
/*      */ 
/*      */ 
/*  381 */                 if ((jaceRecProps.isPropertyPresent(trigPropName)) && (jaceRecProps.get(trigPropName).isDirty()))
/*      */                 {
/*      */ 
/*  384 */                   String recordIdent = this.jaceDocument.getObjectReference().getObjectIdentity();
/*  385 */                   throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CHANGE_DDTRIGPROP_REC_IN_DDREPORT, new Object[] { recordIdent });
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  395 */         boolean recalcPropIsDirty = false;
/*  396 */         boolean recalcPropIsPresent = jaceRecProps.isPropertyPresent("RecalculatePhaseRetention");
/*  397 */         if (recalcPropIsPresent)
/*      */         {
/*  399 */           recalcPropIsDirty = jaceRecProps.get("RecalculatePhaseRetention").isDirty();
/*      */         }
/*  401 */         if (!recalcPropIsDirty)
/*      */         {
/*  403 */           if (!recalcPropIsPresent)
/*      */           {
/*  405 */             this.jaceDocument.fetchProperties(new String[] { "RecalculatePhaseRetention" });
/*  406 */             jaceRecProps = this.jaceDocument.getProperties();
/*      */           }
/*  408 */           Integer propValue = jaceRecProps.getInteger32Value("RecalculatePhaseRetention");
/*  409 */           int currentRecalc = propValue != null ? propValue.intValue() : 0;
/*      */           
/*  411 */           if (currentRecalc == 4) {
/*  412 */             jaceRecProps.putValue("RecalculatePhaseRetention", 0);
/*  413 */           } else if (currentRecalc == 2) {
/*  414 */             jaceRecProps.putValue("RecalculatePhaseRetention", 1);
/*      */           }
/*      */         }
/*      */       }
/*  418 */       internalSave(jarmRefreshMode);
/*  419 */       this.isCreationPending = false;
/*      */       
/*  421 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  425 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  429 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.Record });
/*      */     }
/*      */     finally
/*      */     {
/*  433 */       if (establishedSubject) {
/*  434 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*      */   {
/*  443 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/*  444 */     RefreshMode jaceRefreshMode = P8CE_Util.convertToJaceRefreshMode(jarmRefreshMode);
/*      */     
/*  446 */     long startTime = System.currentTimeMillis();
/*  447 */     this.jaceDocument.save(jaceRefreshMode);
/*  448 */     Tracer.traceExtCall("Document.save()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceRefreshMode, new Object[0]);
/*      */     
/*  450 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PageableSet<ContentItem> getAssociatedContentItems()
/*      */   {
/*  458 */     Tracer.traceMethodEntry(new Object[0]);
/*  459 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  462 */       establishedSubject = P8CE_Util.associateSubject();
/*  463 */       PageableSet<ContentItem> result = null;
/*      */       
/*  465 */       PropertyFilter jacePF = new PropertyFilter();
/*  466 */       jacePF.addIncludeProperty(1, null, Boolean.TRUE, "RecordedDocuments", Integer.valueOf(25));
/*  467 */       List<FilterElement> contentItemFEs = P8CE_ContentItemImpl.getMandatoryJaceFEs();
/*  468 */       for (FilterElement fe : contentItemFEs)
/*      */       {
/*  470 */         FilterElement adjustedFE = new FilterElement(Integer.valueOf(1), null, null, fe.getValue(), null);
/*  471 */         jacePF.addIncludeProperty(adjustedFE);
/*      */       }
/*      */       
/*  474 */       Property recordDocsProp = P8CE_Util.getOrFetchJaceProperty(this.jaceDocument, "RecordedDocuments", jacePF);
/*  475 */       IndependentObjectSet jaceDocumentSet; if (recordDocsProp != null)
/*      */       {
/*  477 */         jaceDocumentSet = recordDocsProp.getIndependentObjectSetValue();
/*  478 */         if (jaceDocumentSet != null)
/*      */         {
/*  480 */           boolean supportsPaging = true;
/*  481 */           IGenerator<ContentItem> generator = P8CE_ContentItemImpl.getContentItemGenerator();
/*      */           
/*      */ 
/*      */ 
/*  485 */           Repository contentRepos = null;
/*  486 */           result = new P8CE_PageableSetImpl(contentRepos, jaceDocumentSet, supportsPaging, generator);
/*      */         }
/*      */       }
/*      */       
/*  490 */       Tracer.traceMethodExit(new Object[] { result });
/*  491 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  495 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  499 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_ASSOC_CONTENT_FAILED, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  503 */       if (establishedSubject) {
/*  504 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Record> getChildReceipts()
/*      */   {
/*  514 */     Tracer.traceMethodEntry(new Object[0]);
/*  515 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  518 */       establishedSubject = P8CE_Util.associateSubject();
/*  519 */       List<Record> result = Collections.emptyList();
/*      */       IndependentObjectSet receiptsSet;
/*  521 */       IGenerator<Record> generator; com.filenet.api.core.Document jaceChildReceiptBase; Iterator it; if (supportsReceipts())
/*      */       {
/*  523 */         if (!this.jaceDocument.getProperties().isPropertyPresent("Receipts"))
/*      */         {
/*      */ 
/*      */ 
/*  527 */           List<FilterElement> recordFEs = getMandatoryJaceFEs();
/*  528 */           FilterElement firstFE = new FilterElement(Integer.valueOf(2), null, null, "Receipts", null);
/*  529 */           recordFEs.add(0, firstFE);
/*  530 */           PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, recordFEs);
/*      */           
/*  532 */           long startTime = System.currentTimeMillis();
/*  533 */           this.jaceDocument.refresh(jacePF);
/*  534 */           long endTime = System.currentTimeMillis();
/*  535 */           Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */         }
/*      */         
/*  538 */         if (this.jaceDocument.getProperties().isPropertyPresent("Receipts"))
/*      */         {
/*  540 */           receiptsSet = this.jaceDocument.getProperties().getIndependentObjectSetValue("Receipts");
/*  541 */           generator = getGenerator();
/*  542 */           if ((receiptsSet != null) && (!receiptsSet.isEmpty()))
/*      */           {
/*  544 */             result = new ArrayList();
/*  545 */             jaceChildReceiptBase = null;
/*  546 */             for (it = receiptsSet.iterator(); it.hasNext();)
/*      */             {
/*  548 */               jaceChildReceiptBase = (com.filenet.api.core.Document)it.next();
/*  549 */               if (jaceChildReceiptBase != null) {
/*  550 */                 result.add(generator.create(getRepository(), jaceChildReceiptBase));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  556 */       Tracer.traceMethodExit(new Object[] { result });
/*  557 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  561 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  565 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  569 */       if (establishedSubject) {
/*  570 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Record getReceiptParent()
/*      */   {
/*  579 */     Tracer.traceMethodEntry(new Object[0]);
/*  580 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  583 */       establishedSubject = P8CE_Util.associateSubject();
/*  584 */       Record result = null;
/*  585 */       EngineObject jaceReceiptOfBase; if (supportsReceipts())
/*      */       {
/*  587 */         if (!this.jaceDocument.getProperties().isPropertyPresent("ReceiptOf"))
/*      */         {
/*      */ 
/*      */ 
/*  591 */           List<FilterElement> recordFEs = new ArrayList(getMandatoryJaceFEs());
/*  592 */           FilterElement firstFE = new FilterElement(Integer.valueOf(2), null, null, "ReceiptOf", null);
/*  593 */           recordFEs.add(0, firstFE);
/*  594 */           PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, recordFEs);
/*  595 */           long startTime = System.currentTimeMillis();
/*  596 */           this.jaceDocument.refresh(jacePF);
/*  597 */           long endTime = System.currentTimeMillis();
/*  598 */           Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */         }
/*      */         
/*  601 */         if (this.jaceDocument.getProperties().isPropertyPresent("ReceiptOf"))
/*      */         {
/*  603 */           jaceReceiptOfBase = this.jaceDocument.getProperties().getEngineObjectValue("ReceiptOf");
/*  604 */           if (jaceReceiptOfBase != null)
/*      */           {
/*  606 */             IGenerator<Record> generator = getGenerator();
/*  607 */             result = (Record)generator.create(getRepository(), jaceReceiptOfBase);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  612 */       Tracer.traceMethodExit(new Object[] { result });
/*  613 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  617 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  621 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  625 */       if (establishedSubject) {
/*  626 */         P8CE_Util.disassociateSubject();
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
/*      */   public void assignRecordAsReceiptOfParent(Record receiptParent)
/*      */   {
/*  644 */     Tracer.traceMethodEntry(new Object[] { receiptParent });
/*  645 */     Util.ckNullObjParam("receiptParent", receiptParent);
/*  646 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  649 */       establishedSubject = P8CE_Util.associateSubject();
/*  650 */       if (!supportsReceipts())
/*      */       {
/*  652 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RECORD_PARENT_ALREADY_A_RECEIPT, new Object[0]);
/*      */       }
/*      */       
/*  655 */       String[] additionalPropsNeeded = { "Receipts", "ReceiptOf" };
/*  656 */       refresh(additionalPropsNeeded);
/*      */       
/*  658 */       List<Object> propList = getProperties().getObjectListValue("Receipts");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  664 */       if (propList.size() > 0)
/*      */       {
/*  666 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RECORD_ALREADY_A_RECEIPT_PARENT, new Object[0]);
/*      */       }
/*      */       
/*  669 */       if (getProperties().getObjectValue("ReceiptOf") != null)
/*      */       {
/*  671 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RECORD_ALREADY_A_RECEIPT, new Object[0]);
/*      */       }
/*      */       
/*  674 */       receiptParent.refresh(additionalPropsNeeded);
/*  675 */       if (receiptParent.getProperties().getObjectValue("ReceiptOf") != null)
/*      */       {
/*  677 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RECORD_PARENT_ALREADY_A_RECEIPT, new Object[0]);
/*      */       }
/*      */       
/*  680 */       getProperties().putObjectValue("ReceiptOf", receiptParent);
/*  681 */       getProperties().putIntegerValue("ReceiptStatus", Integer.valueOf(RMReceiptStatus.Attached.getIntValue()));
/*  682 */       save(RMRefreshMode.Refresh);
/*      */       
/*  684 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  688 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  692 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.CANNOT_ATTACH_RECEIPT, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  696 */       if (establishedSubject) {
/*  697 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Record unassignRecordAsReceiptOfParent()
/*      */   {
/*  709 */     Tracer.traceMethodEntry(new Object[0]);
/*  710 */     boolean establishedSubject = false;
/*      */     
/*      */     try
/*      */     {
/*  714 */       establishedSubject = P8CE_Util.associateSubject();
/*  715 */       Record receiptParent = null;
/*      */       
/*  717 */       if (supportsReceipts())
/*      */       {
/*  719 */         refresh(new String[] { "ReceiptOf" });
/*      */         
/*  721 */         receiptParent = (Record)getProperties().getObjectValue("ReceiptOf");
/*  722 */         if (receiptParent == null)
/*      */         {
/*  724 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RECORD_NOT_A_RECEIPT, new Object[] { getObjectIdentity() });
/*      */         }
/*      */         
/*      */ 
/*  728 */         getProperties().putObjectValue("ReceiptOf", null);
/*  729 */         getProperties().putIntegerValue("ReceiptStatus", Integer.valueOf(RMReceiptStatus.None.getIntValue()));
/*  730 */         save(RMRefreshMode.Refresh);
/*      */       }
/*      */       
/*  733 */       Tracer.traceMethodExit(new Object[] { receiptParent });
/*  734 */       return receiptParent;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  738 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  742 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.CANNOT_DETACH_RECEIPT, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  746 */       if (establishedSubject) {
/*  747 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void move(RecordContainer srcContainer, RecordContainer destContainer, String reason)
/*      */   {
/*  756 */     Tracer.traceMethodEntry(new Object[] { srcContainer, destContainer, reason });
/*  757 */     Util.ckNullObjParam("srcContainer", srcContainer);
/*  758 */     Util.ckNullObjParam("destContainer", destContainer);
/*  759 */     Util.ckInvalidStrParam("reason", reason);
/*  760 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  763 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*  766 */       FilePlanRepository repository = (FilePlanRepository)getRepository();
/*  767 */       List<String> entityIdents = new ArrayList(1);
/*  768 */       entityIdents.add(getObjectIdentity());
/*  769 */       String sourceContainerIdent = srcContainer.getObjectIdentity();
/*  770 */       String destinationContainerIdent = destContainer.getObjectIdentity();
/*      */       
/*  772 */       List<BulkItemResult> results = BulkOperation.moveRecords(repository, entityIdents, sourceContainerIdent, destinationContainerIdent, reason);
/*      */       
/*      */ 
/*      */ 
/*  776 */       if ((results != null) && (results.size() >= 1))
/*      */       {
/*  778 */         BulkItemResult bir = (BulkItemResult)results.get(0);
/*  779 */         if ((!bir.wasSuccessful()) && (bir.getException() != null)) {
/*  780 */           throw bir.getException();
/*      */         }
/*      */       }
/*  783 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  787 */       if (establishedSubject) {
/*  788 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void delete(boolean skipValidation, DeleteMode deleteMode, String reasonForDelete)
/*      */   {
/*  797 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(skipValidation), deleteMode, reasonForDelete });
/*  798 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  801 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  803 */       RMDomain jarmDomain = getRepository().getDomain();
/*  804 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/*      */       
/*  806 */       UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */       
/*  808 */       contributeToDeleteBatch(skipValidation, deleteMode, reasonForDelete, jaceUpdateBatch, null);
/*  809 */       if (jaceUpdateBatch.hasPendingExecute())
/*      */       {
/*  811 */         long startTime = System.currentTimeMillis();
/*  812 */         jaceUpdateBatch.updateBatch();
/*  813 */         long endTime = System.currentTimeMillis();
/*  814 */         Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       }
/*      */       
/*  817 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  821 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  825 */       P8CE_AuditServices.attachDeleteAuditEvent(this, cause.getLocalizedMessage(), AuditStatus.Failure, true);
/*  826 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.Record });
/*      */     }
/*      */     finally
/*      */     {
/*  830 */       if (establishedSubject) {
/*  831 */         P8CE_Util.disassociateSubject();
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
/*      */   void contributeToDeleteBatch(boolean skipValidation, DeleteMode deleteMode, String reasonForDelete, UpdatingBatch jaceUpdateBatch, RecordContainer fromContainer)
/*      */   {
/*  853 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(skipValidation), deleteMode, reasonForDelete, jaceUpdateBatch });
/*      */     
/*      */     try
/*      */     {
/*  857 */       if (!skipValidation)
/*  858 */         super.validateRecordDeletion();
/*      */       Iterator it;
/*  860 */       if ((fromContainer != null) && (getContainedBy().size() > 1))
/*      */       {
/*      */ 
/*  863 */         ((P8CE_BaseContainerImpl)fromContainer).unfileRecord(this, true, jaceUpdateBatch);
/*      */       }
/*      */       else
/*      */       {
/*  867 */         IndependentObjectSet jaceReceipts = null;
/*  868 */         if (supportsReceipts())
/*      */         {
/*  870 */           PropertyFilter pf = new PropertyFilter();
/*  871 */           String filterPropNames = "Id Receipts";
/*  872 */           pf.addIncludeProperty(1, null, null, filterPropNames, null);
/*  873 */           Property jaceReceiptsProp = P8CE_Util.getOrFetchJaceProperty(this.jaceDocument, "Receipts", pf);
/*  874 */           if (jaceReceiptsProp != null)
/*  875 */             jaceReceipts = jaceReceiptsProp.getIndependentObjectSetValue();
/*      */         }
/*      */         Iterator it;
/*  878 */         if ((deleteMode == DeleteMode.ForceHardDelete) || ((deleteMode == DeleteMode.CheckRetainMetadata) && (!isRetainMetadataEnabled())))
/*      */         {
/*      */ 
/*      */ 
/*  882 */           this.jaceDocument.getProperties().putValue("PreventRMEntityDeletion", (String)null);
/*      */           
/*      */ 
/*      */ 
/*  886 */           if (!skipValidation) {
/*  887 */             P8CE_AuditServices.attachDeleteAuditEvent(this, reasonForDelete, AuditStatus.Success, false);
/*      */           }
/*      */           
/*  890 */           this.jaceDocument.delete();
/*  891 */           jaceUpdateBatch.add(this.jaceDocument, P8CE_Util.CEPF_Empty);
/*      */           
/*      */ 
/*  894 */           List<Link> associatedJaceLinks = getAssociatedJaceLinks();
/*  895 */           for (Link jaceLink : associatedJaceLinks)
/*      */           {
/*  897 */             jaceLink.delete();
/*  898 */             jaceUpdateBatch.add(jaceLink, P8CE_Util.CEPF_Empty);
/*      */           }
/*      */           
/*      */ 
/*  902 */           if (jaceReceipts != null)
/*      */           {
/*  904 */             for (it = jaceReceipts.iterator(); it.hasNext();)
/*      */             {
/*  906 */               com.filenet.api.core.Document jaceReceiptBase = (com.filenet.api.core.Document)it.next();
/*  907 */               jaceReceiptBase.getProperties().putValue("ReceiptOf", (EngineObject)null);
/*  908 */               jaceReceiptBase.getProperties().putValue("ReceiptStatus", RMReceiptStatus.ParentDeleted.getIntValue());
/*  909 */               jaceUpdateBatch.add(jaceReceiptBase, P8CE_Util.CEPF_Empty);
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  915 */           PropertyFilter jacePF4Rec = new PropertyFilter();
/*  916 */           for (FilterElement fe : getMandatoryJaceFEs())
/*      */           {
/*  918 */             jacePF4Rec.addIncludeProperty(fe);
/*      */           }
/*      */           
/*  921 */           if (!this.jaceDocument.getProperties().isPropertyPresent("RecordedDocuments"))
/*      */           {
/*  923 */             jacePF4Rec.addIncludeProperty(1, null, null, "RecordedDocuments", null);
/*  924 */             this.jaceDocument.refresh(jacePF4Rec);
/*      */           }
/*      */           
/*  927 */           Properties jaceRecProps = this.jaceDocument.getProperties();
/*      */           
/*  929 */           jaceRecProps.putValue("PreventRMEntityDeletion", (String)null);
/*      */           
/*  931 */           jaceRecProps.putValue("DocURI", "");
/*      */           
/*  933 */           jaceRecProps.putValue("IsDeleted", Boolean.TRUE);
/*      */           
/*  935 */           if (!skipValidation) {
/*  936 */             P8CE_AuditServices.attachDeleteAuditEvent(this, reasonForDelete, AuditStatus.Success, false);
/*      */           }
/*  938 */           jaceUpdateBatch.add(this.jaceDocument, jacePF4Rec);
/*      */           
/*      */ 
/*  941 */           IndependentObjectSet jaceAssocDocs = jaceRecProps.getIndependentObjectSetValue("RecordedDocuments");
/*  942 */           com.filenet.api.core.Document jaceAssocDoc; Iterator it; if ((jaceAssocDocs != null) && (!jaceAssocDocs.isEmpty()))
/*      */           {
/*  944 */             jaceAssocDoc = null;
/*  945 */             for (it = jaceAssocDocs.iterator(); it.hasNext();)
/*      */             {
/*  947 */               jaceAssocDoc = (com.filenet.api.core.Document)it.next();
/*  948 */               jaceAssocDoc.getProperties().putValue("RecordInformation", (EngineObject)null);
/*  949 */               jaceAssocDoc.delete();
/*  950 */               jaceUpdateBatch.add(jaceAssocDoc, P8CE_Util.CEPF_Empty);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  955 */           if (jaceReceipts != null)
/*      */           {
/*  957 */             for (it = jaceReceipts.iterator(); it.hasNext();)
/*      */             {
/*  959 */               com.filenet.api.core.Document jaceReceiptBase = (com.filenet.api.core.Document)it.next();
/*  960 */               jaceReceiptBase.getProperties().putValue("ReceiptOf", (EngineObject)null);
/*  961 */               jaceReceiptBase.getProperties().putValue("ReceiptStatus", RMReceiptStatus.ParentDeleted.getIntValue());
/*  962 */               jaceUpdateBatch.add(jaceReceiptBase, P8CE_Util.CEPF_Empty);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  968 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  972 */       P8CE_AuditServices.attachDeleteAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/*  973 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  977 */       P8CE_AuditServices.attachDeleteAuditEvent(this, cause.getLocalizedMessage(), AuditStatus.Failure, true);
/*  978 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordCategory });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void refresh()
/*      */   {
/*  987 */     Tracer.traceMethodEntry(new Object[0]);
/*  988 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  991 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  993 */       long startTime = System.currentTimeMillis();
/*  994 */       this.jaceDocument.refresh();
/*  995 */       long endTime = System.currentTimeMillis();
/*  996 */       Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[0]);
/*      */       
/*  998 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1002 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1006 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1010 */       if (establishedSubject) {
/* 1011 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refresh(RMPropertyFilter jarmFilter)
/*      */   {
/* 1020 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1021 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1024 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 1028 */       List<FilterElement> mandatoryRecordFEs = getMandatoryJaceFEs();
/* 1029 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*      */       
/* 1031 */       long startTime = System.currentTimeMillis();
/* 1032 */       this.jaceDocument.refresh(jacePF);
/* 1033 */       long endTime = System.currentTimeMillis();
/* 1034 */       Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       
/* 1036 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1040 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1044 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1048 */       if (establishedSubject) {
/* 1049 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refresh(String[] symbolicPropertyNames)
/*      */   {
/* 1058 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 1059 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1062 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 1065 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 1066 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 1067 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 1068 */       List<FilterElement> mandatoryRecordFEs = getMandatoryJaceFEs();
/* 1069 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*      */       
/* 1071 */       long startTime = System.currentTimeMillis();
/* 1072 */       this.jaceDocument.refresh(jacePF);
/* 1073 */       long endTime = System.currentTimeMillis();
/* 1074 */       Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       
/* 1076 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1080 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1084 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1088 */       if (establishedSubject) {
/* 1089 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Integer getAccessAllowed()
/*      */   {
/* 1098 */     return this.jaceDocument.getAccessAllowed();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilePlan getFilePlan()
/*      */   {
/* 1106 */     Tracer.traceMethodEntry(new Object[0]);
/* 1107 */     if (this.myFilePlan == null)
/*      */     {
/* 1109 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/* 1112 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/* 1114 */         RALBaseContainer secFolder = (RALBaseContainer)getSecurityFolder();
/* 1115 */         if (secFolder != null) {
/* 1116 */           this.myFilePlan = secFolder.getFilePlan();
/*      */         }
/*      */       }
/*      */       finally {
/* 1120 */         if (establishedSubject) {
/* 1121 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/* 1125 */     Tracer.traceMethodExit(new Object[] { this.myFilePlan });
/* 1126 */     return this.myFilePlan;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Container getSecurityFolder()
/*      */   {
/* 1134 */     Tracer.traceMethodEntry(new Object[0]);
/* 1135 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1138 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1140 */       List<FilterElement> jaceContainerFEs = P8CE_BaseContainerImpl.getMandatoryJaceFEs();
/* 1141 */       IGenerator<Container> generator = P8CE_BaseContainerImpl.getBaseContainerGenerator();
/* 1142 */       Container securityFolder = (Container)P8CE_Util.fetchSVObjPropValue(this.repository, this.jaceDocument, jaceContainerFEs, "SecurityFolder", generator);
/*      */       
/* 1144 */       Tracer.traceMethodExit(new Object[] { securityFolder });
/* 1145 */       return securityFolder;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1149 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1153 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1157 */       if (establishedSubject) {
/* 1158 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void undeclare()
/*      */   {
/* 1168 */     Tracer.traceMethodEntry(new Object[0]);
/* 1169 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1172 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 1175 */       boolean checkForReceipts = supportsReceipts();
/* 1176 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(getMandatoryJaceFEs());
/* 1177 */       String additionalProps = "CurrentPhaseExecutionStatus";
/* 1178 */       if (checkForReceipts)
/* 1179 */         additionalProps = additionalProps + ' ' + "Receipts";
/* 1180 */       jacePF.addIncludeProperty(1, null, null, additionalProps, null);
/* 1181 */       this.jaceDocument.refresh(jacePF);
/* 1182 */       this.isPlaceholder = false;
/*      */       
/*      */ 
/* 1185 */       super.validateRecordUndeclare();
/*      */       
/*      */ 
/* 1188 */       RMDomain jarmDomain = getRepository().getDomain();
/* 1189 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 1190 */       UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.NO_REFRESH);
/*      */       
/*      */ 
/* 1193 */       IndependentObjectSet assocROSDocSet = this.jaceDocument.getProperties().getIndependentObjectSetValue("RecordedDocuments");
/* 1194 */       com.filenet.api.core.Document assocDoc; Iterator it; if (assocROSDocSet != null)
/*      */       {
/* 1196 */         assocDoc = null;
/* 1197 */         for (it = assocROSDocSet.iterator(); it.hasNext();)
/*      */         {
/* 1199 */           assocDoc = (com.filenet.api.core.Document)it.next();
/* 1200 */           assocDoc.getProperties().putValue("RecordInformation", (EngineObject)null);
/* 1201 */           jaceUB.add(assocDoc, P8CE_Util.CEPF_Empty);
/*      */         }
/*      */       }
/*      */       com.filenet.api.core.Document jaceReceiptBase;
/*      */       Iterator it;
/* 1206 */       if (checkForReceipts)
/*      */       {
/* 1208 */         IndependentObjectSet receiptRecords = this.jaceDocument.getProperties().getIndependentObjectSetValue("Receipts");
/* 1209 */         if (receiptRecords != null)
/*      */         {
/* 1211 */           jaceReceiptBase = null;
/* 1212 */           for (it = receiptRecords.iterator(); it.hasNext();)
/*      */           {
/* 1214 */             jaceReceiptBase = (com.filenet.api.core.Document)it.next();
/* 1215 */             jaceReceiptBase.getProperties().putValue("ReceiptStatus", RMReceiptStatus.None.getIntValue());
/* 1216 */             jaceUB.add(jaceReceiptBase, P8CE_Util.CEPF_Empty);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1222 */       List<Link> assocLinks = getAssociatedJaceLinks();
/* 1223 */       for (Link assocLink : assocLinks)
/*      */       {
/* 1225 */         assocLink.delete();
/* 1226 */         jaceUB.add(assocLink, P8CE_Util.CEPF_Empty);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1231 */       P8CE_AuditServices.attachDeleteAuditEvent(this, "", AuditStatus.Success, false);
/*      */       
/*      */ 
/* 1234 */       this.jaceDocument.delete();
/* 1235 */       jaceUB.add(this.jaceDocument, P8CE_Util.CEPF_Empty);
/*      */       
/* 1237 */       long startTime = System.currentTimeMillis();
/* 1238 */       jaceUB.updateBatch();
/* 1239 */       long endTime = System.currentTimeMillis();
/* 1240 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/* 1242 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1246 */       P8CE_AuditServices.attachDeleteAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/* 1247 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1251 */       P8CE_AuditServices.attachDeleteAuditEvent(this, cause.getLocalizedMessage(), AuditStatus.Failure, true);
/* 1252 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1256 */       if (establishedSubject) {
/* 1257 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updatePhaseDataOnEntity()
/*      */   {
/* 1268 */     Tracer.traceMethodEntry(new Object[0]);
/* 1269 */     P8CE_Util.updatePhaseDataOnEntity(this);
/* 1270 */     Tracer.traceMethodExit(new Object[0]);
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
/* 1282 */     Tracer.traceMethodEntry(new Object[0]);
/* 1283 */     P8CE_DispositionScheduleImpl dispSchedImpl = (P8CE_DispositionScheduleImpl)dispSchedule;
/* 1284 */     dispSchedImpl.triggerNextPhase(this);
/* 1285 */     Tracer.traceMethodExit(new Object[0]);
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
/* 1299 */     Tracer.traceMethodEntry(new Object[] { proposedPhaseExecutionDate, Boolean.valueOf(transitionToNextPhase) });
/* 1300 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1303 */       establishedSubject = P8CE_Util.associateSubject();
/* 1304 */       this.jaceDocument.refresh((PropertyFilter)null);
/* 1305 */       RALDispositionLogic.completePhaseExecution(this, proposedPhaseExecutionDate, transitionToNextPhase);
/* 1306 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1310 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1314 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1318 */       if (establishedSubject) {
/* 1319 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetDispositionData()
/*      */   {
/* 1329 */     Tracer.traceMethodEntry(new Object[0]);
/* 1330 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1333 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1335 */       super.resetDispositionData();
/*      */       
/* 1337 */       long startTime = System.currentTimeMillis();
/* 1338 */       this.jaceDocument.save(RefreshMode.REFRESH);
/* 1339 */       long endTime = System.currentTimeMillis();
/* 1340 */       Tracer.traceExtCall("Document.save", startTime, endTime, null, null, new Object[0]);
/*      */       
/* 1342 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1346 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1350 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1354 */       if (establishedSubject) {
/* 1355 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetVitalData()
/*      */   {
/* 1365 */     Tracer.traceMethodEntry(new Object[0]);
/* 1366 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1369 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1371 */       super.resetVitalData();
/*      */       
/* 1373 */       long startTime = System.currentTimeMillis();
/* 1374 */       this.jaceDocument.save(RefreshMode.REFRESH);
/* 1375 */       long endTime = System.currentTimeMillis();
/* 1376 */       Tracer.traceExtCall("Document.save", startTime, endTime, null, null, new Object[0]);
/*      */       
/* 1378 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1382 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1386 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1390 */       if (establishedSubject) {
/* 1391 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setVital(RMProperties vitalProperties)
/*      */   {
/* 1401 */     Tracer.traceMethodEntry(new Object[] { vitalProperties });
/* 1402 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1405 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1407 */       super.setVital(vitalProperties);
/* 1408 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1412 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1416 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1420 */       if (establishedSubject) {
/* 1421 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isVital()
/*      */   {
/* 1431 */     Tracer.traceMethodEntry(new Object[0]);
/* 1432 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1435 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1437 */       boolean result = super.isVital();
/*      */       
/* 1439 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1440 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1444 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1448 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1452 */       if (establishedSubject) {
/* 1453 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void executeVital()
/*      */   {
/* 1463 */     Tracer.traceMethodEntry(new Object[0]);
/* 1464 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1467 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1469 */       super.executeVital();
/* 1470 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1474 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1478 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1482 */       if (establishedSubject) {
/* 1483 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateVitalStatus(Date vitalReviewDate)
/*      */   {
/* 1493 */     Tracer.traceMethodEntry(new Object[] { vitalReviewDate });
/* 1494 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1497 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1499 */       super.updateVitalStatus(vitalReviewDate);
/* 1500 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1504 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1508 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1512 */       if (establishedSubject) {
/* 1513 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void associateRecordType(RecordType newRecordType)
/*      */   {
/* 1523 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1526 */       establishedSubject = P8CE_Util.associateSubject();
/* 1527 */       super.associateRecordType(newRecordType);
/*      */     }
/*      */     finally
/*      */     {
/* 1531 */       if (establishedSubject) {
/* 1532 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordType getAssociatedRecordType()
/*      */   {
/* 1542 */     Tracer.traceMethodEntry(new Object[0]);
/* 1543 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1546 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1548 */       List<FilterElement> jaceDispTrigFEs = P8CE_RecordTypeImpl.getMandatoryJaceFEs();
/* 1549 */       IGenerator<RecordType> generator = P8CE_RecordTypeImpl.getGenerator();
/* 1550 */       RecordType result = (RecordType)P8CE_Util.fetchSVObjPropValue(this.repository, this.jaceDocument, jaceDispTrigFEs, "AssociatedRecordType", generator);
/*      */       
/* 1552 */       Tracer.traceMethodExit(new Object[] { result });
/* 1553 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1557 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1561 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1565 */       if (establishedSubject) {
/* 1566 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resolve()
/*      */   {
/* 1577 */     Tracer.traceMethodEntry(new Object[0]);
/* 1578 */     if (this.isPlaceholder)
/*      */     {
/* 1580 */       P8CE_Util.refreshWithMandatoryProperties(this);
/* 1581 */       this.isPlaceholder = false;
/*      */       
/* 1583 */       if (this.jaceDocument.getProperties().isPropertyPresent("RMEntityType"))
/*      */       {
/* 1585 */         Integer rawEntityType = this.jaceDocument.getProperties().getInteger32Value("RMEntityType");
/* 1586 */         if (rawEntityType != null)
/* 1587 */           this.entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*      */       }
/*      */     }
/* 1590 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Record copy(RecordContainer destContainer, RMProperties overrideProps)
/*      */   {
/* 1598 */     Tracer.traceMethodEntry(new Object[] { destContainer, overrideProps });
/* 1599 */     Util.ckNullObjParam("destContainer", destContainer);
/*      */     
/* 1601 */     List<InputStream> closableStreams = new ArrayList(1);
/* 1602 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1605 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 1608 */       List<String> propNamesToCopy = getEligibleRecordCopyPropertyNames();
/*      */       
/*      */ 
/*      */ 
/* 1612 */       List<String> propNamesToFetch = new ArrayList(propNamesToCopy);
/* 1613 */       propNamesToFetch.add("RecordedDocuments");
/* 1614 */       propNamesToFetch.add("Permissions");
/* 1615 */       String feString = P8CE_Util.createSpaceSeparatedString(propNamesToFetch);
/* 1616 */       PropertyFilter jacePF = P8CE_ContentItemImpl.JaceContentElementPF;
/* 1617 */       jacePF.addIncludeProperty(1, null, null, feString, null);
/* 1618 */       jacePF.addIncludeProperty((FilterElement)P8CE_RMPermissionImpl.getMandatoryJaceFEs().get(0));
/*      */       
/* 1620 */       long startTime = System.currentTimeMillis();
/* 1621 */       this.jaceDocument.refresh(jacePF);
/* 1622 */       long endTime = System.currentTimeMillis();
/* 1623 */       Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       
/* 1625 */       this.isPlaceholder = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1630 */       Integer recordAccess = getAccessAllowed();
/* 1631 */       if (recordAccess != null)
/*      */       {
/* 1633 */         if ((RMAccessRight.Write.getIntValue() & recordAccess.intValue()) == 0)
/*      */         {
/* 1635 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INSUFFICIENT_RIGHTS_TO_COPY_RECORD, new Object[] { getObjectIdentity() });
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1640 */       RMProperties newRecProps = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/* 1641 */       RMProperties currentRecProps = getProperties();
/* 1642 */       RMProperty jarmProp = null;
/* 1643 */       for (String propSymName : propNamesToCopy)
/*      */       {
/* 1645 */         if (!"DocURI".equalsIgnoreCase(propSymName))
/*      */         {
/* 1647 */           jarmProp = currentRecProps.get(propSymName);
/* 1648 */           newRecProps.add(jarmProp);
/*      */         }
/*      */       }
/*      */       
/* 1652 */       if (overrideProps != null) {
/* 1653 */         newRecProps.add(overrideProps);
/*      */       }
/*      */       
/*      */ 
/* 1657 */       RMDomain jarmDomain = getRepository().getDomain();
/* 1658 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 1659 */       UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1664 */       String classIdent = getClassName();
/* 1665 */       List<RMPermission> newRecPerms = getPermissions();
/* 1666 */       List<RecordContainer> additionalContainers = null;
/* 1667 */       List<ContentItem> newContentItems = null;
/* 1668 */       boolean forRecordCopy = true;
/* 1669 */       Record copyOfRecord = ((P8CE_BaseContainerImpl)destContainer).internalDeclare(classIdent, newRecProps, newRecPerms, additionalContainers, newContentItems, forRecordCopy, jaceUB);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1675 */       com.filenet.api.core.Document jaceCopyOfRecordBase = ((P8CE_RecordImpl)copyOfRecord).jaceDocument;
/* 1676 */       ObjectStore jaceFPOSBase = this.jaceDocument.getObjectStore();
/* 1677 */       Link recCopyLink = Factory.Link.createInstance(jaceFPOSBase, "RecordCopyLink");
/* 1678 */       recCopyLink.getProperties().putValue("Head", this.jaceDocument);
/* 1679 */       recCopyLink.getProperties().putValue("Tail", jaceCopyOfRecordBase);
/* 1680 */       jaceUB.add(recCopyLink, P8CE_Util.CEPF_Empty);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1688 */       newContentItems = makeCopyOfAssocContent(jaceUB, closableStreams);
/* 1689 */       for (ContentItem newContentItem : newContentItems)
/*      */       {
/* 1691 */         newContentItem.getProperties().putObjectValue("RecordInformation", copyOfRecord);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1696 */       startTime = System.currentTimeMillis();
/* 1697 */       jaceUB.updateBatch();
/*      */       
/* 1699 */       jaceCopyOfRecordBase.refresh((PropertyFilter)null);
/* 1700 */       endTime = System.currentTimeMillis();
/* 1701 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       
/* 1703 */       Tracer.traceMethodExit(new Object[] { copyOfRecord });
/* 1704 */       Iterator i$; InputStream is; return copyOfRecord;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1708 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1712 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1716 */       if (closableStreams != null)
/*      */       {
/*      */ 
/* 1719 */         for (InputStream is : closableStreams) {
/*      */           try {
/* 1721 */             is.close();
/*      */           } catch (Exception ignored) {}
/*      */         }
/*      */       }
/* 1725 */       if (establishedSubject) {
/* 1726 */         P8CE_Util.disassociateSubject();
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
/*      */   private List<ContentItem> makeCopyOfAssocContent(UpdatingBatch jaceUB, List<InputStream> closableStreams)
/*      */   {
/* 1746 */     Tracer.traceMethodEntry(new Object[] { jaceUB, closableStreams });
/* 1747 */     List<ContentItem> newContentItems = new ArrayList(1);
/* 1748 */     IndependentObjectSet assocDocSet = this.jaceDocument.getProperties().getIndependentObjectSetValue("RecordedDocuments");
/*      */     
/*      */ 
/* 1751 */     RMDomain jarmDomain = getRepository().getDomain();
/* 1752 */     Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 1753 */     RetrievingBatch jaceRB = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/* 1754 */     List<com.filenet.api.core.Document> assocDocs = new ArrayList(1);
/* 1755 */     for (Iterator it = assocDocSet.iterator(); it.hasNext();)
/*      */     {
/* 1757 */       com.filenet.api.core.Document existingAssocDoc = (com.filenet.api.core.Document)it.next();
/* 1758 */       assocDocs.add(existingAssocDoc);
/* 1759 */       jaceRB.add(existingAssocDoc, null); }
/*      */     ContentRepository jarmDocRepository;
/*      */     com.filenet.api.core.Document newDoc;
/* 1762 */     ContentItem newContentItem; if (assocDocs.size() > 0)
/*      */     {
/* 1764 */       long startTime = System.currentTimeMillis();
/* 1765 */       jaceRB.retrieveBatch();
/* 1766 */       long endTime = System.currentTimeMillis();
/* 1767 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, endTime, Integer.valueOf(assocDocs.size()), null, new Object[0]);
/*      */       
/*      */ 
/* 1770 */       validateContentForRecordCopy(assocDocs);
/*      */       
/*      */ 
/* 1773 */       String rosIdent = ((com.filenet.api.core.Document)assocDocs.get(0)).getObjectStore().getObjectReference().getObjectIdentity();
/* 1774 */       jarmDocRepository = RMFactory.ContentRepository.getInstance(jarmDomain, rosIdent);
/* 1775 */       newDoc = null;
/* 1776 */       newContentItem = null;
/* 1777 */       for (com.filenet.api.core.Document assocDoc : assocDocs)
/*      */       {
/*      */ 
/*      */ 
/* 1781 */         newDoc = duplicateJaceDoc(assocDoc, jaceUB, closableStreams);
/* 1782 */         duplicateJaceDocFilings(assocDoc, newDoc, jaceUB);
/*      */         
/* 1784 */         String newDocIdent = newDoc.getObjectReference().getObjectIdentity();
/* 1785 */         newContentItem = new P8CE_ContentItemImpl(EntityType.ContentItem, jarmDocRepository, newDocIdent, newDoc, true);
/* 1786 */         newContentItems.add(newContentItem);
/*      */       }
/*      */     }
/*      */     
/* 1790 */     Tracer.traceMethodExit(new Object[] { newContentItems });
/* 1791 */     return newContentItems;
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
/*      */   private void duplicateJaceDocFilings(com.filenet.api.core.Document origDoc, com.filenet.api.core.Document newDoc, UpdatingBatch jaceUB)
/*      */   {
/* 1808 */     Tracer.traceMethodEntry(new Object[] { origDoc, newDoc, jaceUB });
/* 1809 */     ReferentialContainmentRelationshipSet rcrSet = origDoc.get_Containers();
/* 1810 */     ReferentialContainmentRelationship origRCR = null;
/* 1811 */     ReferentialContainmentRelationship newRCR = null;
/* 1812 */     Folder docFolder = null;
/* 1813 */     for (Iterator it = rcrSet.iterator(); it.hasNext();)
/*      */     {
/* 1815 */       origRCR = (ReferentialContainmentRelationship)it.next();
/* 1816 */       docFolder = (Folder)origRCR.get_Tail();
/* 1817 */       Integer accessAllowed = docFolder.getAccessAllowed();
/* 1818 */       if ((accessAllowed != null) && ((0x10 & accessAllowed.intValue()) == 0))
/*      */       {
/* 1820 */         String docIdent = newDoc.getObjectReference().getObjectIdentity();
/* 1821 */         String folderIdent = docFolder.getObjectReference().getObjectIdentity();
/* 1822 */         Logger.warn(RMLogCode.RECORDCOPY_DOCUMENT_FILING_FAILURE, new Object[] { docIdent, folderIdent });
/*      */       }
/*      */       else
/*      */       {
/* 1826 */         newRCR = Factory.DynamicReferentialContainmentRelationship.createInstance(origDoc.getObjectStore(), "DynamicReferentialContainmentRelationship", AutoUniqueName.AUTO_UNIQUE, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1831 */         newRCR.set_Head(newDoc);
/* 1832 */         newRCR.set_Tail(origRCR.get_Tail());
/* 1833 */         jaceUB.add(newRCR, P8CE_Util.CEPF_Empty);
/*      */       }
/*      */     }
/* 1836 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   private com.filenet.api.core.Document duplicateJaceDoc(com.filenet.api.core.Document origDoc, UpdatingBatch jaceUB, List<InputStream> closableStreams)
/*      */   {
/* 1854 */     Tracer.traceMethodEntry(new Object[] { origDoc, jaceUB, closableStreams });
/* 1855 */     com.filenet.api.core.Document newDoc = Factory.Document.createInstance(origDoc.getObjectStore(), origDoc.getClassName());
/*      */     
/*      */ 
/* 1858 */     Properties origDocProps = origDoc.getProperties();
/* 1859 */     Properties newDocProps = newDoc.getProperties();
/* 1860 */     for (Iterator it = origDocProps.iterator(); it.hasNext();)
/*      */     {
/* 1862 */       Property jaceProp = (Property)it.next();
/* 1863 */       String propSymName = jaceProp.getPropertyName();
/* 1864 */       if ((jaceProp.isSettable()) && (!"Id".equalsIgnoreCase(propSymName)) && (!"Permissions".equalsIgnoreCase(propSymName)))
/*      */       {
/*      */ 
/* 1867 */         P8CE_Util.copyJacePropIntoCollection(jaceProp, newDocProps);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1872 */     if (origDocProps.isPropertyPresent("Permissions"))
/*      */     {
/* 1874 */       AccessPermissionList origDocPerms = origDoc.get_Permissions();
/* 1875 */       AccessPermissionList newDocPerms = Factory.AccessPermission.createList();
/* 1876 */       AccessPermission perm = null;
/* 1877 */       PermissionSource permSrc = null;
/* 1878 */       for (int i = 0; i < origDocPerms.size(); i++)
/*      */       {
/* 1880 */         perm = (AccessPermission)origDocPerms.get(i);
/* 1881 */         permSrc = perm.get_PermissionSource();
/* 1882 */         if ((PermissionSource.SOURCE_DIRECT.equals(permSrc)) || (PermissionSource.SOURCE_DEFAULT.equals(permSrc)) || (PermissionSource.SOURCE_TEMPLATE.equals(permSrc)))
/*      */         {
/*      */ 
/*      */ 
/* 1886 */           newDocPerms.add(perm);
/*      */         }
/*      */       }
/* 1889 */       if (newDocPerms.size() > 0)
/*      */       {
/* 1891 */         newDoc.set_Permissions(newDocPerms);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1896 */     ContentElementList origElements = origDoc.get_ContentElements();
/* 1897 */     if (!origElements.isEmpty())
/*      */     {
/* 1899 */       ContentElementList newElements = Factory.ContentElement.createList();
/* 1900 */       for (Iterator it = origElements.iterator(); it.hasNext();)
/*      */       {
/* 1902 */         ContentElement origCE = (ContentElement)it.next();
/* 1903 */         if ((origCE instanceof ContentTransfer))
/*      */         {
/* 1905 */           ContentTransfer origCT = (ContentTransfer)origCE;
/* 1906 */           ContentTransfer newCT = Factory.ContentTransfer.createInstance();
/* 1907 */           newCT.set_RetrievalName(origCT.get_RetrievalName());
/* 1908 */           newCT.set_ContentType(origCT.get_ContentType());
/* 1909 */           InputStream contentIS = origCT.accessContentStream();
/* 1910 */           newCT.setCaptureSource(contentIS);
/* 1911 */           closableStreams.add(contentIS);
/* 1912 */           newElements.add(newCT);
/*      */         }
/*      */         else
/*      */         {
/* 1916 */           ContentReference origCR = (ContentReference)origCE;
/* 1917 */           ContentReference newCR = Factory.ContentReference.createInstance();
/* 1918 */           newCR.set_ContentType(origCR.get_ContentType());
/* 1919 */           newCR.set_ContentLocation(origCR.get_ContentLocation());
/* 1920 */           newElements.add(newCR);
/*      */         }
/*      */       }
/*      */       
/* 1924 */       newDoc.set_ContentElements(newElements);
/*      */     }
/* 1926 */     newDoc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/* 1927 */     jaceUB.add(newDoc, P8CE_Util.CEPF_Empty);
/*      */     
/* 1929 */     Tracer.traceMethodExit(new Object[] { newDoc });
/* 1930 */     return newDoc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validateContentForRecordCopy(List<com.filenet.api.core.Document> jaceAssocDocs)
/*      */   {
/* 1940 */     Tracer.traceMethodEntry(new Object[] { jaceAssocDocs });
/* 1941 */     for (com.filenet.api.core.Document jaceAssocDoc : jaceAssocDocs)
/*      */     {
/* 1943 */       if (isJaceDocumentFederated(jaceAssocDoc))
/*      */       {
/* 1945 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_COPY_FEDERATED_DOCUMENT, new Object[0]);
/*      */       }
/*      */     }
/* 1948 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hasFederatedContent()
/*      */   {
/* 1960 */     Tracer.traceMethodEntry(new Object[0]);
/* 1961 */     boolean result = false;
/*      */     
/* 1963 */     if (!this.jaceDocument.getProperties().isPropertyPresent("RecordedDocuments"))
/*      */     {
/*      */ 
/* 1966 */       PropertyFilter jacePF = new PropertyFilter();
/* 1967 */       jacePF.addIncludeProperty(0, null, null, "RecordedDocuments", null);
/* 1968 */       String level1PropNames = "StorageArea ContentElements";
/* 1969 */       jacePF.addIncludeProperty(1, null, null, level1PropNames, null);
/* 1970 */       String level2PropNames = level1PropNames + ' ' + "FixedContentDevice" + ' ' + "ContentSize";
/* 1971 */       jacePF.addIncludeProperty(2, null, null, level2PropNames, null);
/*      */       
/* 1973 */       long startTime = System.currentTimeMillis();
/* 1974 */       this.jaceDocument.fetchProperties(jacePF);
/* 1975 */       long endTime = System.currentTimeMillis();
/* 1976 */       Tracer.traceExtCall("Document.fetchProperties()", startTime, endTime, null, null, new Object[] { jacePF });
/*      */     }
/*      */     
/* 1979 */     IndependentObjectSet assocDocSet = this.jaceDocument.getProperties().getIndependentObjectSetValue("RecordedDocuments");
/* 1980 */     com.filenet.api.core.Document assocJaceDoc = null;
/* 1981 */     for (Iterator it = assocDocSet.iterator(); it.hasNext();)
/*      */     {
/* 1983 */       assocJaceDoc = (com.filenet.api.core.Document)it.next();
/* 1984 */       if (isJaceDocumentFederated(assocJaceDoc))
/*      */       {
/* 1986 */         result = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1991 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1992 */     return result;
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
/*      */   private boolean isJaceDocumentFederated(com.filenet.api.core.Document jaceDoc)
/*      */   {
/* 2006 */     Tracer.traceMethodEntry(new Object[] { jaceDoc });
/* 2007 */     boolean result = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2013 */     ContentElementList jaceContentElements = jaceDoc.get_ContentElements();
/* 2014 */     ContentElement jaceContentElement = null;
/* 2015 */     for (Iterator it = jaceContentElements.iterator(); it.hasNext();)
/*      */     {
/* 2017 */       jaceContentElement = (ContentElement)it.next();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2022 */       if ((jaceContentElement instanceof ContentTransfer))
/*      */       {
/* 2024 */         Double contentSize = ((ContentTransfer)jaceContentElement).get_ContentSize();
/* 2025 */         if (contentSize == null)
/*      */         {
/* 2027 */           result = true;
/* 2028 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2033 */     if (!result)
/*      */     {
/* 2035 */       StorageArea jaceStorageArea = jaceDoc.get_StorageArea();
/* 2036 */       if ((jaceStorageArea != null) && ((jaceStorageArea instanceof FixedStorageArea)))
/*      */       {
/* 2038 */         FixedContentDevice jaceFCD = ((FixedStorageArea)jaceStorageArea).get_FixedContentDevice();
/* 2039 */         if ((jaceFCD != null) && (((jaceFCD instanceof IICEFixedContentDevice)) || ((jaceFCD instanceof IMFixedContentDevice)) || ((jaceFCD instanceof CMODFixedContentDevice))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2044 */           result = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2049 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2050 */     return result;
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
/* 2061 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/* 2063 */     List<Link> associatedLinks = new ArrayList(0);
/* 2064 */     String idStr = getObjectIdentity();
/*      */     
/* 2066 */     StringBuilder sb = new StringBuilder();
/* 2067 */     sb.append("SELECT l.").append("Id");
/* 2068 */     sb.append(" FROM ").append("Link").append(" l");
/* 2069 */     sb.append(" WHERE (").append("l.").append("Head").append(" = OBJECT('").append(idStr).append("')");
/* 2070 */     sb.append(" OR ").append("l.").append("Tail").append(" = OBJECT('").append(idStr).append("'))");
/*      */     
/* 2072 */     String sqlStmt = sb.toString();
/* 2073 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 2074 */     SearchScope jaceSearchScope = new SearchScope(this.jaceDocument.getObjectStore());
/* 2075 */     Integer pageSize = null;
/* 2076 */     Boolean continuable = Boolean.TRUE;
/*      */     
/* 2078 */     long startTime = System.currentTimeMillis();
/* 2079 */     IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, continuable);
/* 2080 */     long endTime = System.currentTimeMillis();
/* 2081 */     Boolean elementCountIndicator = null;
/* 2082 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 2084 */       elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */     }
/* 2086 */     Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStmt, null, P8CE_Util.CEPF_IdOnly, Boolean.FALSE });
/*      */     
/* 2088 */     Link jaceLink = null;
/* 2089 */     PageIterator jacePI = jaceObjSet.pageIterator();
/* 2090 */     while (jacePI.nextPage())
/*      */     {
/* 2092 */       Object[] currentPage = jacePI.getCurrentPage();
/* 2093 */       for (Object obj : currentPage)
/*      */       {
/* 2095 */         jaceLink = (Link)obj;
/* 2096 */         associatedLinks.add(jaceLink);
/*      */       }
/*      */     }
/*      */     
/* 2100 */     Tracer.traceMethodExit(new Object[] { associatedLinks });
/* 2101 */     return associatedLinks;
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
/* 2113 */     Tracer.traceMethodEntry(new Object[0]);
/* 2114 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2117 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 2119 */       boolean doValidation = true;
/* 2120 */       doDestroy(doValidation);
/* 2121 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2125 */       if (establishedSubject) {
/* 2126 */         P8CE_Util.disassociateSubject();
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
/* 2145 */     Tracer.traceMethodEntry(new Object[0]);
/* 2146 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2149 */       establishedSubject = P8CE_Util.associateSubject();
/* 2150 */       doDestroy(doValidation);
/* 2151 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2155 */       if (establishedSubject) {
/* 2156 */         P8CE_Util.disassociateSubject();
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
/*      */   void doDestroy(boolean doValidation)
/*      */   {
/* 2171 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(doValidation) });
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2176 */       refresh(P8CE_BaseContainerImpl.DispositionRelatedPropNames);
/*      */       
/*      */ 
/* 2179 */       boolean isPartOfTransferProcess = false;
/* 2180 */       Integer rawCurrentActionType = P8CE_Util.getJacePropertyAsInteger(this, "CurrentActionType");
/* 2181 */       if ((rawCurrentActionType != null) && (rawCurrentActionType.intValue() == DispositionActionType.Transfer.getIntValue()))
/*      */       {
/*      */ 
/* 2184 */         isPartOfTransferProcess = true;
/*      */       }
/*      */       
/* 2187 */       String cutoffInheritedFrom = P8CE_Util.getJacePropertyAsString(this, "CutoffInheritedFrom");
/* 2188 */       Date cutoffDate = P8CE_Util.getJacePropertyAsDate(this, "CutoffDate");
/* 2189 */       if ((cutoffInheritedFrom != null) && (cutoffDate != null) && (Id.isId(cutoffInheritedFrom)))
/*      */       {
/* 2191 */         if (RALDispositionLogic.isRecalculatePhaseIndicated(getRepository(), this, cutoffInheritedFrom))
/*      */         {
/* 2193 */           RALDispositionLogic.resetEntityDispositionState(getProperties());
/* 2194 */           save(RMRefreshMode.Refresh);
/*      */           
/* 2196 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_SCHEDULE_HAS_CHANGED, new Object[0]);
/*      */         }
/*      */         
/* 2199 */         if (RALDispositionLogic.hasCurrentPhaseChanged(this, null))
/*      */         {
/* 2201 */           DispositionSchedule schedule = RALDispositionLogic.getInheritedSchedule(this);
/* 2202 */           if (schedule != null) {
/* 2203 */             ((P8CE_DispositionScheduleImpl)schedule).updateCurrentPhaseDataOnEntity(this);
/*      */           }
/* 2205 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_SCHEDULE_HAS_CHANGED, new Object[0]);
/*      */         }
/*      */       }
/*      */       
/* 2209 */       boolean recordWasUnfiled = false;
/* 2210 */       if ((doValidation) || (isPartOfTransferProcess))
/*      */       {
/* 2212 */         if (doValidation) {
/* 2213 */           RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.Destroy);
/*      */         }
/* 2215 */         boolean cutoffInheritedFromRecType = false;
/* 2216 */         RecordType recType = getAssociatedRecordType();
/* 2217 */         if ((recType != null) && (cutoffInheritedFrom.equalsIgnoreCase(recType.getObjectIdentity())))
/*      */         {
/* 2219 */           cutoffInheritedFromRecType = true;
/*      */         }
/*      */         
/* 2222 */         if (!cutoffInheritedFromRecType)
/*      */         {
/*      */ 
/* 2225 */           List<Container> currentRecContainers = getContainedBy();
/* 2226 */           if ((currentRecContainers != null) && (currentRecContainers.size() > 1))
/*      */           {
/* 2228 */             for (Container container : currentRecContainers)
/*      */             {
/* 2230 */               Container comparisonCandidate = container;
/* 2231 */               if (container.getEntityType() == EntityType.RecordVolume) {
/* 2232 */                 comparisonCandidate = container.getParent();
/*      */               }
/* 2234 */               if (cutoffInheritedFrom.equalsIgnoreCase(comparisonCandidate.getObjectIdentity()))
/*      */               {
/*      */ 
/*      */ 
/* 2238 */                 boolean resetDispositionState = true;
/* 2239 */                 ((P8CE_BaseContainerImpl)container).unfileRecord(this, resetDispositionState, null);
/* 2240 */                 recordWasUnfiled = true;
/* 2241 */                 break;
/*      */               }
/*      */             }
/*      */             
/* 2245 */             if (recordWasUnfiled)
/*      */             {
/* 2247 */               P8CE_AuditServices.attachDestroyAuditEvent(this, "", AuditStatus.Success, true);
/*      */             }
/*      */             else
/*      */             {
/* 2251 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_ERROR_IMPROPER_CUTOFFINHERIT_VALUE, new Object[] { getObjectIdentity() });
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2257 */       if (!recordWasUnfiled)
/*      */       {
/*      */ 
/* 2260 */         RMDomain jarmDomain = getRepository().getDomain();
/* 2261 */         Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 2262 */         User jaceUser = P8CE_Util.fetchCurrentJaceUser(jaceDomain);
/* 2263 */         String userShortName = jaceUser.get_ShortName();
/*      */         
/* 2265 */         P8CE_AuditServices.attachDestroyAuditEvent(this, "", AuditStatus.Success, false);
/* 2266 */         getProperties().putStringValue("DisposalAuthorizedBy", userShortName);
/* 2267 */         save(RMRefreshMode.Refresh);
/*      */         
/* 2269 */         boolean skipValidation = true;
/* 2270 */         DeleteMode deleteMode = DeleteMode.CheckRetainMetadata;
/* 2271 */         String reasonForDelete = RMLString.get("disposition.deleteFromDestroy", "Deletion performed as a result of destroy action.");
/* 2272 */         delete(skipValidation, deleteMode, reasonForDelete);
/*      */       }
/*      */       
/* 2275 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2279 */       P8CE_AuditServices.attachDestroyAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/* 2280 */       rollbackCurrentPhaseState();
/*      */       
/* 2282 */       if ((ex instanceof RMRuntimeException)) {
/* 2283 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 2285 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_DESTROY_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */   }
/*      */   
/*      */   private void rollbackCurrentPhaseState()
/*      */   {
/* 2291 */     Tracer.traceMethodEntry(new Object[0]);
/* 2292 */     getProperties().putIntegerValue("CurrentPhaseExecutionStatus", Integer.valueOf(RMWorkflowStatus.NotStarted.getIntValue()));
/* 2293 */     save(RMRefreshMode.Refresh);
/*      */     
/* 2295 */     Id currentPhaseId = P8CE_Util.getJacePropertyAsId(this, "CurrentPhaseID");
/* 2296 */     if (currentPhaseId != null)
/*      */     {
/* 2298 */       DispositionSchedule schedule = RALDispositionLogic.getInheritedSchedule(this);
/* 2299 */       if (schedule != null)
/* 2300 */         ((P8CE_DispositionScheduleImpl)schedule).updateCurrentPhaseDataOnEntity(this);
/*      */     }
/* 2302 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   void destroyFromContainer(RecordContainer recContainer, boolean doValidation, boolean exportCalledBeforeDestroy)
/*      */   {
/* 2307 */     Tracer.traceMethodEntry(new Object[] { recContainer, Boolean.valueOf(doValidation), Boolean.valueOf(exportCalledBeforeDestroy) });
/* 2308 */     List<Container> currentContainers = getContainedBy();
/* 2309 */     if (currentContainers.size() > 1)
/*      */     {
/* 2311 */       recContainer.unfileRecord(this);
/*      */     }
/*      */     else
/*      */     {
/* 2315 */       doDestroy(doValidation);
/*      */     }
/* 2317 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void executeInterimTransferUsingExternalExport(Location newLocation, ExternalExport externalExport, boolean doValidation)
/*      */   {
/* 2325 */     Tracer.traceMethodEntry(new Object[] { newLocation });
/* 2326 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2329 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 2331 */       boolean doExport = true;
/* 2332 */       internalExecuteInterimTransfer(doValidation, doExport, newLocation, externalExport);
/* 2333 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2337 */       if (establishedSubject) {
/* 2338 */         P8CE_Util.disassociateSubject();
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
/*      */   public void executeInterimTransferUsingP8_XML(Location newLocation)
/*      */   {
/* 2356 */     Tracer.traceMethodEntry(new Object[] { newLocation });
/* 2357 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2360 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 2362 */       boolean doValidation = true;
/* 2363 */       boolean doExport = true;
/* 2364 */       internalExecuteInterimTransfer(doValidation, doExport, newLocation, null);
/* 2365 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2369 */       if (establishedSubject) {
/* 2370 */         P8CE_Util.disassociateSubject();
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
/*      */   void internalExecuteInterimTransfer(boolean doValidation, boolean doExport, Location newLocation, ExternalExport externalExport)
/*      */   {
/* 2393 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(doValidation), Boolean.valueOf(doExport), newLocation });
/* 2394 */     Util.ckNullObjParam("newLocation", newLocation);
/*      */     
/* 2396 */     String exportDirPath = null;
/*      */     try
/*      */     {
/* 2399 */       refresh((RMPropertyFilter)null);
/*      */       
/*      */ 
/* 2402 */       if (doValidation)
/*      */       {
/* 2404 */         P8CE_Util.validateCurrentPhaseAndSchedule(this);
/* 2405 */         RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.InterimTransfer);
/*      */       }
/*      */       
/*      */ 
/* 2409 */       exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/* 2410 */       if (doExport)
/*      */       {
/* 2412 */         if (externalExport == null)
/*      */         {
/* 2414 */           boolean doExportValidation = false;
/* 2415 */           exportUsingP8_XML(doExportValidation);
/*      */         }
/*      */         else
/*      */         {
/* 2419 */           String reposIdent = getRepository().getSymbolicName();
/* 2420 */           String entityIdent = getObjectIdentity();
/* 2421 */           externalExport.export(reposIdent, entityIdent);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2426 */       RMDomain jarmDomain = getRepository().getDomain();
/* 2427 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 2428 */       UpdatingBatch jaceUpdateBatch = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/* 2429 */       Properties jaceRecProps = this.jaceDocument.getProperties();
/*      */       
/* 2431 */       jaceRecProps.putValue("PreventRMEntityDeletion", (String)null);
/*      */       
/* 2433 */       jaceRecProps.putValue("DocURI", "");
/*      */       
/* 2435 */       EngineObject jaceLocationBase = ((JaceBasable)newLocation).getJaceBaseObject();
/* 2436 */       jaceRecProps.putValue("Location", jaceLocationBase);
/* 2437 */       if (jaceRecProps.isPropertyPresent("HomeLocation"))
/*      */       {
/* 2439 */         jaceRecProps.putValue("HomeLocation", jaceLocationBase);
/*      */       }
/* 2441 */       jaceUpdateBatch.add(this.jaceDocument, P8CE_Util.CEPF_Empty);
/*      */       
/*      */ 
/* 2444 */       IndependentObjectSet jaceAssocDocs = jaceRecProps.getIndependentObjectSetValue("RecordedDocuments");
/* 2445 */       com.filenet.api.core.Document jaceAssocDoc; Iterator it; if ((jaceAssocDocs != null) && (!jaceAssocDocs.isEmpty()))
/*      */       {
/* 2447 */         jaceAssocDoc = null;
/* 2448 */         for (it = jaceAssocDocs.iterator(); it.hasNext();)
/*      */         {
/* 2450 */           jaceAssocDoc = (com.filenet.api.core.Document)it.next();
/* 2451 */           jaceAssocDoc.getProperties().putValue("RecordInformation", (EngineObject)null);
/* 2452 */           jaceAssocDoc.delete();
/* 2453 */           jaceUpdateBatch.add(jaceAssocDoc, P8CE_Util.CEPF_Empty);
/*      */         }
/*      */       }
/* 2456 */       if (jaceUpdateBatch.hasPendingExecute())
/*      */       {
/* 2458 */         long startTime = System.currentTimeMillis();
/* 2459 */         jaceUpdateBatch.updateBatch();
/* 2460 */         long endTime = System.currentTimeMillis();
/* 2461 */         Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*      */       }
/*      */       
/* 2464 */       if (doValidation) {
/* 2465 */         P8CE_AuditServices.attachInterimTransferAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/* 2467 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2471 */       P8CE_AuditServices.attachInterimTransferAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, exportDirPath, true);
/* 2472 */       rollbackCurrentPhaseState();
/*      */       
/* 2474 */       if ((ex instanceof RMRuntimeException)) {
/* 2475 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 2477 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INTERIMTRANSFER_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
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
/* 2495 */     Tracer.traceMethodEntry(new Object[] { externalExport, Boolean.valueOf(doValidation) });
/* 2496 */     Util.ckNullObjParam("externalExport", externalExport);
/*      */     
/* 2498 */     internalTransfer(externalExport, doValidation);
/*      */     
/* 2500 */     Tracer.traceMethodExit(new Object[0]);
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
/* 2514 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/* 2516 */     ExternalExport externalExport = null;
/* 2517 */     boolean doValidation = true;
/* 2518 */     internalTransfer(externalExport, doValidation);
/*      */     
/* 2520 */     Tracer.traceMethodExit(new Object[0]);
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
/* 2533 */     Tracer.traceMethodEntry(new Object[0]);
/* 2534 */     boolean establishedSubject = false;
/* 2535 */     String exportDirPath = null;
/*      */     try
/*      */     {
/* 2538 */       establishedSubject = P8CE_Util.associateSubject();
/* 2539 */       refresh((RMPropertyFilter)null);
/*      */       
/* 2541 */       if (doValidation)
/*      */       {
/*      */ 
/* 2544 */         P8CE_Util.validateCurrentPhaseAndSchedule(this);
/* 2545 */         RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.Transfer);
/*      */       }
/*      */       
/*      */ 
/* 2549 */       exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/*      */       
/* 2551 */       if (externalExport == null)
/*      */       {
/* 2553 */         boolean doExportValidation = false;
/* 2554 */         exportUsingP8_XML(doExportValidation);
/*      */       }
/*      */       else
/*      */       {
/* 2558 */         String objStoreIdent = getRepository().getObjectIdentity();
/* 2559 */         String entityIdStr = getObjectIdentity();
/* 2560 */         externalExport.export(objStoreIdent, entityIdStr);
/*      */       }
/*      */       
/* 2563 */       if (doValidation) {
/* 2564 */         P8CE_AuditServices.attachTransferAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/*      */       
/* 2567 */       boolean doDestroyValidation = false;
/* 2568 */       doDestroy(doDestroyValidation);
/*      */       
/* 2570 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2574 */       if (doValidation) {
/* 2575 */         P8CE_AuditServices.attachTransferAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, exportDirPath, true);
/*      */       }
/* 2577 */       rollbackCurrentPhaseState();
/*      */       
/* 2579 */       if ((ex instanceof RMRuntimeException)) {
/* 2580 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 2582 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_TRANSFER_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */     finally
/*      */     {
/* 2586 */       if (establishedSubject) {
/* 2587 */         P8CE_Util.disassociateSubject();
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
/* 2604 */     Tracer.traceMethodEntry(new Object[] { externalExport, Boolean.valueOf(doValidation) });
/* 2605 */     Util.ckNullObjParam("externalExport", externalExport);
/* 2606 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2609 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 2613 */       refresh(P8CE_BaseContainerImpl.DispositionRelatedPropNames);
/*      */       
/* 2615 */       if (doValidation)
/*      */       {
/* 2617 */         validateForDispositionExport();
/*      */       }
/*      */       
/*      */ 
/* 2621 */       String objStoreIdent = getRepository().getObjectIdentity();
/* 2622 */       String entityIdStr = getObjectIdentity();
/* 2623 */       externalExport.export(objStoreIdent, entityIdStr);
/*      */       
/* 2625 */       if (doValidation)
/*      */       {
/*      */ 
/* 2628 */         String exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/* 2629 */         P8CE_AuditServices.attachExportAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/* 2631 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2635 */       if (establishedSubject) {
/* 2636 */         P8CE_Util.disassociateSubject();
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
/* 2652 */     Tracer.traceMethodEntry(new Object[0]);
/* 2653 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2656 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 2658 */       boolean doValidation = true;
/* 2659 */       exportUsingP8_XML(doValidation);
/* 2660 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 2664 */       if (establishedSubject) {
/* 2665 */         P8CE_Util.disassociateSubject();
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
/* 2678 */     Tracer.traceMethodEntry(new Object[0]);
/* 2679 */     String exportDirPath = null;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2684 */       refresh(P8CE_BaseContainerImpl.DispositionRelatedPropNames);
/*      */       
/* 2686 */       if (doValidation)
/*      */       {
/* 2688 */         validateForDispositionExport();
/*      */       }
/*      */       
/*      */ 
/* 2692 */       exportDirPath = P8CE_Util.getJacePropertyAsString(this, "CurrentPhaseExportDestination");
/* 2693 */       if ((exportDirPath == null) || (exportDirPath.trim().length() == 0))
/*      */       {
/* 2695 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_FILESYSTEM_DESTINATION_PATH_INVALID, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/*      */ 
/* 2699 */       String xsltStr = P8CE_Util.fetchExportFormatXSLT(this);
/*      */       
/*      */ 
/* 2702 */       ContentXMLExport contentExportMode = P8CE_Util.getSystemConfiguration_ExportConfigSetting((FilePlanRepository)getRepository());
/*      */       
/*      */ 
/* 2705 */       exportUsingP8_XML(exportDirPath, xsltStr, contentExportMode);
/*      */       
/* 2707 */       if (doValidation) {
/* 2708 */         P8CE_AuditServices.attachExportAuditEvent(this, "", AuditStatus.Success, exportDirPath, true);
/*      */       }
/* 2710 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2714 */       P8CE_AuditServices.attachExportAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, exportDirPath, true);
/* 2715 */       rollbackCurrentPhaseState();
/*      */       
/* 2717 */       if ((ex instanceof RMRuntimeException)) {
/* 2718 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 2720 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_EXPORT_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
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
/* 2739 */     Tracer.traceMethodEntry(new Object[] { exportFileSystemPath, xsltDefStr, contentExportOption });
/* 2740 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2743 */       establishedSubject = P8CE_Util.associateSubject();
/* 2744 */       resolve();
/*      */       
/*      */ 
/* 2747 */       File exportFileSystemDir = null;
/* 2748 */       if ((exportFileSystemPath == null) || (exportFileSystemPath.trim().length() == 0))
/*      */       {
/* 2750 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_FILESYSTEM_DESTINATION_PATH_INVALID, new Object[] { getObjectIdentity() });
/*      */       }
/* 2752 */       boolean dirExists = false;
/*      */       try
/*      */       {
/* 2755 */         exportFileSystemDir = new File(exportFileSystemPath);
/* 2756 */         dirExists = exportFileSystemDir.exists();
/* 2757 */         if (!dirExists) {
/* 2758 */           dirExists = exportFileSystemDir.mkdirs();
/*      */         }
/*      */       }
/*      */       catch (Exception ex) {
/* 2762 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.API_EXPORT_CANT_CREATE_DESTINATION_DIR, new Object[] { exportFileSystemPath });
/*      */       }
/* 2764 */       if (!dirExists) {
/* 2765 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_CANT_CREATE_DESTINATION_DIR, new Object[] { exportFileSystemPath });
/*      */       }
/*      */       
/* 2768 */       String exportFilterStr = P8CE_XMLExportSupport.getRecordExportClassFilter(this);
/*      */       
/*      */ 
/* 2771 */       Repository jarmRepository = getRepository();
/* 2772 */       int exportFlags = 2;
/* 2773 */       org.w3c.dom.Document exportedEntityDOMDoc = P8CE_XMLExportSupport.doJaceXMLExport(jarmRepository, this.jaceDocument, exportFilterStr, exportFlags);
/*      */       
/*      */ 
/* 2776 */       List<Link> memberLinks = P8CE_Util.findAssociatedLinks(this.jaceDocument);
/* 2777 */       Node mainXMLOthersNode; org.w3c.dom.Document exportedLinkDOMDoc; if (memberLinks.size() > 0)
/*      */       {
/*      */ 
/* 2780 */         mainXMLOthersNode = RAL_XMLSupport.findXMLNode(exportedEntityDOMDoc, "/ObjectManifest/Others", true);
/* 2781 */         if (mainXMLOthersNode == null) {
/* 2782 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_EXPORT_P8CE_XMLGENERATION_FAILURE, new Object[] { getObjectIdentity() });
/*      */         }
/*      */         
/* 2785 */         exportedLinkDOMDoc = null;
/* 2786 */         for (Link link : memberLinks)
/*      */         {
/* 2788 */           exportedLinkDOMDoc = P8CE_XMLExportSupport.doJaceXMLExport(jarmRepository, link, "", 0);
/* 2789 */           NodeList linkNodes = RAL_XMLSupport.findXMLNodes(exportedLinkDOMDoc, "/ObjectManifest/*", true);
/* 2790 */           if (linkNodes != null)
/*      */           {
/* 2792 */             for (int i = 0; i < linkNodes.getLength(); i++)
/*      */             {
/* 2794 */               Node linkNode = linkNodes.item(i);
/* 2795 */               boolean deepImport = true;
/* 2796 */               Node importedLinkNode = exportedEntityDOMDoc.importNode(linkNode, deepImport);
/* 2797 */               mainXMLOthersNode.appendChild(importedLinkNode);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2804 */       if ((xsltDefStr != null) && (xsltDefStr.trim().length() > 0))
/*      */       {
/* 2806 */         exportedEntityDOMDoc = RAL_XMLSupport.transformUsingXSLT(exportedEntityDOMDoc, xsltDefStr);
/*      */       }
/*      */       
/*      */ 
/* 2810 */       String mainXMLFileName = P8CE_XMLExportSupport.generateExportFileName("RM_", getName(), "_Hierarchy_");
/* 2811 */       File mainXMLFile = new File(exportFileSystemDir, mainXMLFileName);
/* 2812 */       RAL_XMLSupport.writeDomDocToFile(exportedEntityDOMDoc, mainXMLFile);
/*      */       
/*      */ 
/* 2815 */       exportAssocContentUsingP8_XML(exportFileSystemDir, contentExportOption);
/*      */       
/* 2817 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException rre)
/*      */     {
/* 2821 */       throw rre;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2825 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_EXPORT_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */     finally
/*      */     {
/* 2829 */       if (establishedSubject) {
/* 2830 */         P8CE_Util.disassociateSubject();
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
/*      */   void exportAssocContentUsingP8_XML(File exportFileSystemDir, ContentXMLExport contentExportOption)
/*      */   {
/* 2852 */     Tracer.traceMethodEntry(new Object[] { exportFileSystemDir, contentExportOption });
/* 2853 */     PageableSet<ContentItem> assocContentItemSet = getAssociatedContentItems();
/* 2854 */     if (assocContentItemSet != null)
/*      */     {
/* 2856 */       RMPageIterator<ContentItem> jarmPI = assocContentItemSet.pageIterator();
/* 2857 */       while (jarmPI.nextPage())
/*      */       {
/* 2859 */         List<ContentItem> currentPage = jarmPI.getCurrentPage();
/* 2860 */         for (ContentItem assocContentItem : currentPage)
/*      */         {
/* 2862 */           ((P8CE_ContentItemImpl)assocContentItem).exportAsXML(exportFileSystemDir, contentExportOption);
/*      */         }
/*      */       }
/*      */     }
/* 2866 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public void review(RMProperties reviewProps)
/*      */   {
/* 2889 */     Tracer.traceMethodEntry(new Object[0]);
/* 2890 */     Util.ckNullObjParam("reviewProps", reviewProps);
/* 2891 */     if (reviewProps.size() == 0)
/*      */     {
/* 2893 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_REVIEW_EMPTY_PROPS_COLLECTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/* 2896 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2899 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 2901 */       Properties jaceProps = this.jaceDocument.getProperties();
/* 2902 */       P8CE_Util.convertToJaceProperties(reviewProps, jaceProps);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2908 */       PropertyFilter jacePF = new PropertyFilter();
/* 2909 */       for (FilterElement jaceFE : P8CE_BaseContainerImpl.getMandatoryJaceFEs())
/*      */       {
/* 2911 */         jacePF.addIncludeProperty(jaceFE);
/*      */       }
/* 2913 */       this.jaceDocument.save(RefreshMode.REFRESH, jacePF);
/*      */       
/*      */ 
/* 2916 */       P8CE_Util.validateCurrentPhaseAndSchedule(this);
/* 2917 */       RALDispositionLogic.validateAttemptedAction(this, DispositionActionType.Review);
/*      */       
/* 2919 */       P8CE_AuditServices.attachReviewAuditEvent(this, "", AuditStatus.Success, true);
/* 2920 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 2924 */       P8CE_AuditServices.attachReviewAuditEvent(this, ex.getLocalizedMessage(), AuditStatus.Failure, true);
/* 2925 */       rollbackCurrentPhaseState();
/*      */       
/* 2927 */       if ((ex instanceof RMRuntimeException)) {
/* 2928 */         throw ((RMRuntimeException)ex);
/*      */       }
/* 2930 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_TRANSFER_FAILURE, new Object[] { getObjectIdentity(), ex.getLocalizedMessage() });
/*      */     }
/*      */     finally
/*      */     {
/* 2934 */       if (establishedSubject) {
/* 2935 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOnHold(boolean checkContainerHierarchy)
/*      */   {
/* 2945 */     Tracer.traceMethodEntry(new Object[0]);
/* 2946 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2949 */       establishedSubject = P8CE_Util.associateSubject();
/* 2950 */       boolean result = super.isOnHold(checkContainerHierarchy);
/* 2951 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2952 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 2956 */       if (establishedSubject) {
/* 2957 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyParentOnHold()
/*      */   {
/* 2967 */     Tracer.traceMethodEntry(new Object[0]);
/* 2968 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2971 */       establishedSubject = P8CE_Util.associateSubject();
/* 2972 */       boolean result = super.isAnyParentOnHold();
/* 2973 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2974 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 2978 */       if (establishedSubject) {
/* 2979 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyChildOnHold()
/*      */   {
/* 2989 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void placeHold(Hold hold)
/*      */   {
/* 2998 */     Tracer.traceMethodEntry(new Object[0]);
/* 2999 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3002 */       establishedSubject = P8CE_Util.associateSubject();
/* 3003 */       super.placeHold(hold);
/* 3004 */       Tracer.traceMethodExit(new Object[0]);
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
/*      */ 
/*      */   public void removeHold(Hold hold)
/*      */   {
/* 3019 */     Tracer.traceMethodEntry(new Object[0]);
/* 3020 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3023 */       establishedSubject = P8CE_Util.associateSubject();
/* 3024 */       super.removeHold(hold);
/* 3025 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 3029 */       if (establishedSubject) {
/* 3030 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Hold> getAssociatedHolds()
/*      */   {
/* 3040 */     Tracer.traceMethodEntry(new Object[0]);
/* 3041 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3044 */       establishedSubject = P8CE_Util.associateSubject();
/* 3045 */       List<Hold> result = super.getAssociatedHolds();
/* 3046 */       Tracer.traceMethodExit(new Object[] { result });
/* 3047 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 3051 */       if (establishedSubject) {
/* 3052 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<RecordContainer> getParentsOnHold()
/*      */   {
/* 3062 */     Tracer.traceMethodEntry(new Object[0]);
/* 3063 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3066 */       establishedSubject = P8CE_Util.associateSubject();
/* 3067 */       List<RecordContainer> result = super.getParentsOnHold();
/* 3068 */       Tracer.traceMethodExit(new Object[] { result });
/* 3069 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 3073 */       if (establishedSubject) {
/* 3074 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isSuperseded()
/*      */   {
/* 3083 */     Tracer.traceMethodEntry(new Object[0]);
/* 3084 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3087 */       establishedSubject = P8CE_Util.associateSubject();
/* 3088 */       boolean result = super.isSuperseded();
/* 3089 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 3090 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 3094 */       if (establishedSubject) {
/* 3095 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void supersede(Record supersedeTargetRecord)
/*      */   {
/* 3105 */     Tracer.traceMethodEntry(new Object[] { supersedeTargetRecord });
/* 3106 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3109 */       establishedSubject = P8CE_Util.associateSubject();
/* 3110 */       super.supersede(supersedeTargetRecord);
/* 3111 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 3115 */       if (establishedSubject) {
/* 3116 */         P8CE_Util.disassociateSubject();
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
/* 3132 */     Tracer.traceMethodEntry(new Object[] { vwSession });
/* 3133 */     Util.ckNullObjParam("vwSession", vwSession);
/* 3134 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3137 */       establishedSubject = P8CE_Util.associateSubject();
/* 3138 */       List<BulkItemResult> results = super.initiateDisposition(vwSession);
/* 3139 */       Tracer.traceMethodExit(new Object[] { results });
/* 3140 */       return results;
/*      */     }
/*      */     finally
/*      */     {
/* 3144 */       if (establishedSubject) {
/* 3145 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DomainType getDomainType()
/*      */   {
/* 3154 */     return DomainType.P8_CE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 3163 */     String ident = "<unknown>";
/* 3164 */     if (this.jaceDocument.getProperties().isPropertyPresent("DocumentTitle")) {
/* 3165 */       ident = this.jaceDocument.getProperties().getStringValue("DocumentTitle");
/*      */     } else {
/* 3167 */       ident = getObjectIdentity();
/*      */     }
/* 3169 */     return "P8CE_RecordImpl: " + ident;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void validateForDispositionExport()
/*      */   {
/* 3177 */     Tracer.traceMethodEntry(new Object[0]);
/* 3178 */     P8CE_Util.validateDispositionExport(this);
/* 3179 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class Generator
/*      */     implements IGenerator<Record>
/*      */   {
/*      */     public Record create(Repository repository, Object jaceBaseObject)
/*      */     {
/* 3193 */       P8CE_RecordImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 3194 */       com.filenet.api.core.Document jaceDocument = (com.filenet.api.core.Document)jaceBaseObject;
/*      */       
/* 3196 */       EntityType entityType = EntityType.Record;
/* 3197 */       if ((jaceBaseObject != null) && ((jaceBaseObject instanceof EngineObject)))
/*      */       {
/* 3199 */         Properties jaceProps = ((EngineObject)jaceBaseObject).getProperties();
/* 3200 */         if (jaceProps.isPropertyPresent("RMEntityType"))
/*      */         {
/* 3202 */           Integer rawEntityType = jaceProps.getInteger32Value("RMEntityType");
/* 3203 */           entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*      */         }
/*      */       }
/*      */       
/* 3207 */       Repository actualRepository = repository;
/* 3208 */       if (actualRepository == null)
/*      */       {
/* 3210 */         ObjectStore jaceObjStore = jaceDocument.getObjectStore();
/* 3211 */         if (jaceObjStore != null)
/*      */         {
/* 3213 */           actualRepository = P8CE_Convert.fromP8CE(jaceObjStore);
/*      */         }
/*      */       }
/*      */       
/* 3217 */       String identity = P8CE_Util.getJaceObjectIdentity(jaceDocument);
/* 3218 */       Record result = new P8CE_RecordImpl(entityType, actualRepository, identity, jaceDocument, false);
/*      */       
/* 3220 */       P8CE_RecordImpl.Tracer.traceMethodExit(new Object[] { result });
/* 3221 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RecordImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */