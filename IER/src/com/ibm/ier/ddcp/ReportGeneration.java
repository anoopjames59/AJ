/*      */ package com.ibm.ier.ddcp;
/*      */ 
/*      */ import com.filenet.api.admin.ClassDefinition;
/*      */ import com.filenet.api.admin.PropertyDefinition;
/*      */ import com.filenet.api.admin.PropertyDefinitionBoolean;
/*      */ import com.filenet.api.admin.ServerCacheConfiguration;
/*      */ import com.filenet.api.collection.Integer32List;
/*      */ import com.filenet.api.collection.PropertyDefinitionList;
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.collection.SubsystemConfigurationList;
/*      */ import com.filenet.api.constants.FilteredPropertyType;
/*      */ import com.filenet.api.core.Connection;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.Factory.ClassDefinition;
/*      */ import com.filenet.api.core.Factory.Connection;
/*      */ import com.filenet.api.core.Factory.Domain;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.RepositoryRow;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.filenet.api.util.UserContext;
/*      */ import com.ibm.ier.ddcp.constants.DDCPConstants;
/*      */ import com.ibm.ier.ddcp.constants.ReportStatus;
/*      */ import com.ibm.ier.ddcp.entity.DDContainerEntity;
/*      */ import com.ibm.ier.ddcp.exception.DDCPErrorCode;
/*      */ import com.ibm.ier.ddcp.exception.DDCPRuntimeException;
/*      */ import com.ibm.ier.ddcp.util.DDCPLogCode;
/*      */ import com.ibm.ier.ddcp.util.DDCPLogger;
/*      */ import com.ibm.ier.ddcp.util.DDCPTracer;
/*      */ import com.ibm.ier.ddcp.util.DDCPUtil;
/*      */ import com.ibm.ier.logtrace.BaseTracer.TraceLevel;
/*      */ import filenet.vw.api.VWSession;
/*      */ import filenet.vw.api.VWStepElement;
/*      */ import java.io.PrintStream;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import javax.security.auth.Subject;
/*      */ import org.apache.log4j.Level;
/*      */ 
/*      */ public class ReportGeneration
/*      */ {
/*      */   private static final PropertyFilter ddContainerPropertyFilter;
/*      */   private static final String recordPropertyFilterTemplate;
/*      */   private static final PropertyFilter workflowPropertyFilter;
/*      */   private static final PropertyFilter recContainerPropertyFilter;
/*      */   private static final String QUERY_RECORD_IN_DD_CONTAINER;
/*      */   private static final String QUERY_ALL_DD_CONTAINERS;
/*      */   private static final String QUERY_TEMPLATE_DD_CONTAINERS_UNDER_SPECIFIED_CONTAINER;
/*      */   private static final String QUERY_TEMPLATE_VERIFY_SPECIFIED_CONTAINER_IDS;
/*      */   private static final String QUERY_CHECK_FPOS_DOD_MODEL;
/*      */   private static final String QUERY_WORKFLOW_VWVERSION;
/*      */   private static final String QUERY_RECORD_CONTAINER;
/*      */   private static final String KEY_DDCONTAINERS = "DDContainers";
/*      */   private static final String KEY_NON_DDCONTAINERS = "NonDDContainers";
/*      */   
/*      */   static
/*      */   {
/*   69 */     StringBuilder sb = new StringBuilder();
/*   70 */     sb.append("Id").append(' ');
/*   71 */     sb.append("Name").append(' ');
/*   72 */     sb.append("RMEntityType").append(' ');
/*   73 */     sb.append("PathName").append(' ');
/*   74 */     sb.append("Reviewer").append(' ');
/*   75 */     sb.append("RMRetentionTriggerPropertyName").append(' ');
/*   76 */     sb.append("RMRetentionPeriod").append(' ');
/*      */     
/*   78 */     ddContainerPropertyFilter = new PropertyFilter();
/*   79 */     ddContainerPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   86 */     StringBuilder sb = new StringBuilder();
/*   87 */     sb.append("Id").append(' ');
/*   88 */     sb.append("RMEntityType").append(' ');
/*   89 */     sb.append("RMEntityDescription").append(' ');
/*   90 */     sb.append("Name").append(' ');
/*   91 */     sb.append("Reviewer").append(' ');
/*   92 */     sb.append("CurrentPhaseExecutionStatus").append(' ');
/*   93 */     sb.append("%s").append(' ');
/*      */     
/*   95 */     recordPropertyFilterTemplate = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */     StringBuilder sb = new StringBuilder();
/*  102 */     sb.append("DocumentTitle").append(' ');
/*  103 */     sb.append("VWVersion").append(' ');
/*      */     
/*  105 */     workflowPropertyFilter = new PropertyFilter();
/*  106 */     workflowPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  113 */     StringBuilder sb = new StringBuilder();
/*  114 */     sb.append("AllowedRMTypes").append(' ');
/*      */     
/*  116 */     recContainerPropertyFilter = new PropertyFilter();
/*  117 */     recContainerPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */     StringBuilder sb = new StringBuilder();
/*  127 */     sb.append("SELECT ");
/*  128 */     sb.append("d.").append("Id");
/*  129 */     sb.append(", d.").append("Name");
/*  130 */     sb.append(", d.").append("RMEntityDescription");
/*  131 */     sb.append(", d.").append("RMEntityType");
/*  132 */     sb.append(", d.").append("Reviewer");
/*  133 */     sb.append(", d.%s");
/*  134 */     sb.append(" FROM ElectronicRecordInfo d INNER JOIN ReferentialContainmentRelationship r ON d.This = r.Head");
/*  135 */     sb.append(" WHERE r.Tail = OBJECT('%s')");
/*      */     
/*  137 */     sb.append(" AND (d.");
/*  138 */     sb.append("CurrentPhaseExecutionStatus");
/*  139 */     sb.append(" IS NULL OR d.");
/*  140 */     sb.append("CurrentPhaseExecutionStatus");
/*  141 */     sb.append(" = ");
/*  142 */     sb.append(ReportStatus.NotInReport.getIntValue());
/*  143 */     sb.append(") AND d.%s < %s ");
/*  144 */     sb.append(" AND d.OnHold = FALSE  AND d.IsDeleted = FALSE ORDER BY d.Id");
/*      */     
/*  146 */     QUERY_RECORD_IN_DD_CONTAINER = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  152 */     StringBuilder sb = new StringBuilder();
/*  153 */     sb.append("SELECT ");
/*  154 */     sb.append("f.").append("Id");
/*  155 */     sb.append(", f.").append("Name");
/*  156 */     sb.append(", f.").append("RMEntityType");
/*  157 */     sb.append(", f.").append("PathName");
/*  158 */     sb.append(", f.").append("Reviewer");
/*  159 */     sb.append(", f.").append("RMRetentionTriggerPropertyName");
/*      */     
/*  161 */     sb.append(", f.").append("RMRetentionPeriod");
/*      */     
/*  163 */     sb.append(" FROM RecordCategory f WHERE f.");
/*  164 */     sb.append("RMRetentionTriggerPropertyName");
/*  165 */     sb.append(" IS NOT NULL");
/*  166 */     sb.append(" AND f.OnHold = FALSE AND f.IsDeleted = FALSE ORDER BY f.Id");
/*      */     
/*  168 */     QUERY_ALL_DD_CONTAINERS = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  175 */     StringBuilder sb = new StringBuilder();
/*  176 */     sb.append("SELECT ");
/*  177 */     sb.append("f.").append("Id");
/*  178 */     sb.append(", f.").append("Name");
/*  179 */     sb.append(", f.").append("RMEntityType");
/*  180 */     sb.append(", f.").append("PathName");
/*  181 */     sb.append(", f.").append("Reviewer");
/*  182 */     sb.append(", f.").append("RMRetentionTriggerPropertyName");
/*      */     
/*  184 */     sb.append(", f.").append("RMRetentionPeriod");
/*      */     
/*  186 */     sb.append(" FROM RecordCategory f WHERE f.");
/*  187 */     sb.append("RMRetentionTriggerPropertyName");
/*  188 */     sb.append(" IS NOT NULL");
/*  189 */     sb.append(" AND f.This INSUBFOLDER '%s' AND f.OnHold = FALSE  AND f.IsDeleted = FALSE");
/*      */     
/*  191 */     QUERY_TEMPLATE_DD_CONTAINERS_UNDER_SPECIFIED_CONTAINER = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  198 */     StringBuilder sb = new StringBuilder();
/*  199 */     sb.append("SELECT ");
/*  200 */     sb.append("f.").append("Id");
/*  201 */     sb.append(", f.").append("Name");
/*  202 */     sb.append(", f.").append("RMEntityType");
/*  203 */     sb.append(", f.").append("PathName");
/*  204 */     sb.append(", f.").append("Reviewer");
/*  205 */     sb.append(", f.").append("RMRetentionTriggerPropertyName");
/*  206 */     sb.append(", f.").append("RMRetentionPeriod");
/*  207 */     sb.append(", f.").append("OnHold");
/*  208 */     sb.append(", f.").append("IsDeleted");
/*      */     
/*  210 */     sb.append(" FROM RMFolder f WHERE");
/*  211 */     sb.append(" f.Id IN (%s)");
/*      */     
/*  213 */     QUERY_TEMPLATE_VERIFY_SPECIFIED_CONTAINER_IDS = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  220 */     StringBuilder sb = new StringBuilder();
/*  221 */     sb.append("SELECT ");
/*  222 */     sb.append("PropertyValue");
/*  223 */     sb.append(" FROM SYSTEMCONFIGURATION WHERE ");
/*  224 */     sb.append("PropertyName");
/*  225 */     sb.append(" = 'FPOS Setup'");
/*      */     
/*  227 */     QUERY_CHECK_FPOS_DOD_MODEL = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */     StringBuilder sb = new StringBuilder();
/*  234 */     sb.append("SELECT ");
/*  235 */     sb.append("DocumentTitle");
/*  236 */     sb.append(", ").append("VWVersion");
/*  237 */     sb.append(" FROM WorkflowDefinition WHERE ");
/*  238 */     sb.append("Id");
/*  239 */     sb.append(" = %s");
/*      */     
/*  241 */     QUERY_WORKFLOW_VWVERSION = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  247 */     StringBuilder sb = new StringBuilder();
/*  248 */     sb.append("SELECT ");
/*  249 */     sb.append("AllowedRMTypes");
/*  250 */     sb.append(" FROM RMFolder WHERE ");
/*  251 */     sb.append("Id");
/*  252 */     sb.append(" = %s");
/*      */     
/*  254 */     QUERY_RECORD_CONTAINER = sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  260 */   private DDCPLogger mLogger = null;
/*  261 */   private DDCPTracer mTracer = null;
/*      */   
/*      */ 
/*  264 */   private Map<String, ObjectStore> mJaceObjectStoreCache = new HashMap();
/*      */   
/*      */ 
/*  267 */   private Map<String, Boolean> mDODModelCache = new HashMap();
/*      */   
/*  269 */   private OnHoldValidator mHoldValidator = null;
/*      */   
/*  271 */   private int mThreadCount = 1;
/*  272 */   private int mReadBatchSize = 10000;
/*  273 */   private int mUpdateBatchSize = 1000;
/*  274 */   private int mContentSizeLimit = 200000;
/*  275 */   private int mLinkCacheSizeLimit = 100000;
/*  276 */   private int mOnHoldContainerCacheSizeLimit = 200000;
/*      */   
/*  278 */   private Map<String, String> mWorkflowConfigMap = new HashMap();
/*      */   
/*  280 */   private VWSession mPESession = null;
/*  281 */   private String mVWVersion = null;
/*      */   
/*      */ 
/*  284 */   private String mWorkflowLogLevel = "";
/*  285 */   private String mWorkflowTraceLevel = "";
/*      */   
/*      */ 
/*  288 */   private boolean mHaveValidatedReportGenParams = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReportGeneration(String logLocation, String taskName, Level logLevel, BaseTracer.TraceLevel traceLevel)
/*      */   {
/*  298 */     if (logLevel != null) {
/*  299 */       this.mWorkflowLogLevel = logLevel.toString();
/*      */     }
/*  301 */     if (traceLevel != null) {
/*  302 */       this.mWorkflowTraceLevel = traceLevel.getLabel();
/*      */     }
/*      */     
/*      */ 
/*  306 */     this.mLogger = DDCPLogger.getDDCPLogger(ReportGeneration.class.getName(), logLocation, taskName, logLevel);
/*  307 */     this.mTracer = DDCPTracer.getDDCPTracer(com.ibm.ier.ddcp.util.DDCPTracer.SubSystem.Report, logLocation, taskName, traceLevel);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, String> generateRetentionDueReport(String objectStoreName, Set<String> containerIdSet, int advanceDays, boolean isReportOnly, Map<String, String> configContextMap)
/*      */     throws Exception
/*      */   {
/*  347 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, containerIdSet, Integer.valueOf(advanceDays), Boolean.valueOf(isReportOnly), configContextMap });
/*      */     
/*      */ 
/*  350 */     this.mTracer.traceMinimumMsg("---- Start disposition report generation ----", new Object[0]);
/*  351 */     this.mLogger.info(DDCPLogCode.START_REPORT, new Object[] { "----", "----" });
/*      */     
/*  353 */     this.mTracer.traceMinimumMsg("Advanced days: {0}", new Object[] { Integer.valueOf(advanceDays) });
/*  354 */     this.mLogger.info(DDCPLogCode.ADVANCED_DAYS, new Object[] { Integer.valueOf(advanceDays) });
/*      */     
/*  356 */     this.mTracer.traceMinimumMsg("Report only: {0}", new Object[] { Boolean.valueOf(isReportOnly) });
/*  357 */     this.mLogger.info(DDCPLogCode.REPORT_ONLY, new Object[] { Boolean.valueOf(isReportOnly) });
/*      */     
/*  359 */     BatchProcessingManager batchProcessingMgr = null;
/*  360 */     Map<String, String> reviewerReportIDMap = new HashMap();
/*      */     try {
/*  362 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/*  364 */       Map<String, Set<DDContainerEntity>> containerMap = validateReportGenParams(objectStoreName, containerIdSet, advanceDays, configContextMap);
/*      */       
/*      */ 
/*      */ 
/*  368 */       this.mHoldValidator = new OnHoldValidator((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), this.mReadBatchSize, this.mOnHoldContainerCacheSizeLimit);
/*      */       
/*      */ 
/*      */ 
/*  372 */       Map<String, Set<DDContainerEntity>> reviewerBasedContainerMap = organizeDDContainerBasedOnReviewer(objectStoreName, containerMap);
/*      */       
/*      */ 
/*  375 */       long timeSpot1 = System.currentTimeMillis();
/*  376 */       this.mTracer.traceMinimumMsg("Time to validate and get all qualified containers with basic schedule in the range: {0}", new Object[] { Long.valueOf(timeSpot1 - timeSpot0) });
/*      */       
/*      */ 
/*  379 */       if (reviewerBasedContainerMap.size() == 0) {
/*  380 */         this.mTracer.traceMinimumMsg("There is no valid container with basic schedule, done!", new Object[0]);
/*  381 */         this.mLogger.info(DDCPLogCode.NO_VALID_DDCONTAINER, new Object[0]);
/*  382 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  386 */       if (!isReportOnly) {
/*  387 */         batchProcessingMgr = new BatchProcessingManager((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), this.mThreadCount, false, null);
/*      */         
/*      */ 
/*      */ 
/*  391 */         batchProcessingMgr.setProcessingObjectClassName("Document");
/*      */       }
/*      */       
/*  394 */       int totalRecCountInReports = 0;
/*  395 */       int totalReadRecords = 0;
/*  396 */       int recProgressCount = 0;
/*  397 */       Set<Map.Entry<String, Set<DDContainerEntity>>> reviewerContainerEntrySet = reviewerBasedContainerMap.entrySet();
/*      */       
/*      */ 
/*      */ 
/*  401 */       for (Map.Entry<String, Set<DDContainerEntity>> reviewerContainerEntry : reviewerContainerEntrySet) {
/*  402 */         String reviewer = (String)reviewerContainerEntry.getKey();
/*  403 */         this.mTracer.traceMinimumMsg("Start working for reviewer: {0}", new Object[] { reviewer });
/*      */         
/*      */ 
/*  406 */         int reportRecoveryID = getReportRecoveryID();
/*  407 */         this.mTracer.traceMinimumMsg("The recovery ID for this report is {0}.", new Object[] { Integer.valueOf(reportRecoveryID) });
/*  408 */         this.mLogger.info(DDCPLogCode.REPORT_RECOVERY_ID, new Object[] { Integer.valueOf(reportRecoveryID) });
/*  409 */         Map<String, Object> updateProperties = new HashMap(1);
/*  410 */         updateProperties.put("CurrentPhaseExecutionStatus", Integer.valueOf(reportRecoveryID));
/*      */         
/*  412 */         if (batchProcessingMgr != null) {
/*  413 */           batchProcessingMgr.setUpdatingProperties(updateProperties);
/*      */         }
/*      */         
/*  416 */         long startTimeForReviewer = System.currentTimeMillis();
/*      */         
/*  418 */         Document theReport = null;
/*      */         
/*      */ 
/*  421 */         StringBuilder reportBuilder = new StringBuilder();
/*      */         
/*  423 */         Set<Boolean> hasHeaderIndicator = new HashSet(1);
/*      */         
/*      */ 
/*      */ 
/*  427 */         List<Integer> contentSizeCountList = new ArrayList(1);
/*  428 */         contentSizeCountList.add(Integer.valueOf(0));
/*      */         
/*  430 */         int updateBatchCount = 0;
/*  431 */         int totalRecCountForReviewer = 0;
/*      */         
/*      */ 
/*  434 */         Map<String, RepositoryRow> recordForUpdatingBatchMap = new HashMap();
/*      */         
/*      */ 
/*  437 */         Set<DDContainerEntity> ddContainerSet = (Set)reviewerContainerEntry.getValue();
/*  438 */         this.mTracer.traceMinimumMsg("There are {0} containers with basic schedule for the reviewer {1}.\n", new Object[] { Integer.valueOf(ddContainerSet.size()), reviewer });
/*      */         
/*  440 */         this.mLogger.info(DDCPLogCode.TOTAL_DDCONTAINERS_FOR_THE_REVIEWER, new Object[] { Integer.valueOf(ddContainerSet.size()), reviewer });
/*      */         
/*      */ 
/*  443 */         for (DDContainerEntity ddContainer : ddContainerSet) {
/*  444 */           this.mTracer.traceMinimumMsg("Start working on container with basic schedule : {0}", new Object[] { ddContainer.getContainerName() });
/*      */           
/*      */ 
/*  447 */           long timeSpot2 = System.currentTimeMillis();
/*      */           
/*  449 */           String comparisonTimeInUTC = getComparisonTimeinUTCString(ddContainer, advanceDays);
/*      */           
/*  451 */           this.mTracer.traceMinimumMsg("The time string in UTC for compare: {0} (advance days is: {1} and retention period is: {2}).", new Object[] { comparisonTimeInUTC, Integer.valueOf(advanceDays), ddContainer.getRetentionPeriod() });
/*      */           
/*      */ 
/*      */ 
/*  455 */           String recQuery = String.format(QUERY_RECORD_IN_DD_CONTAINER, new Object[] { ddContainer.getRetentionTriggerName(), ddContainer.getContainerId(), ddContainer.getRetentionTriggerName(), comparisonTimeInUTC });
/*      */           
/*      */ 
/*      */ 
/*  459 */           this.mTracer.traceMinimumMsg("Query for retention due records : {0}", new Object[] { recQuery });
/*      */           
/*  461 */           String recordPropertyFilterStr = String.format(recordPropertyFilterTemplate, new Object[] { ddContainer.getRetentionTriggerName() });
/*      */           
/*  463 */           PropertyFilter recPropertyFilter = new PropertyFilter();
/*  464 */           recPropertyFilter.addIncludeProperty(0, null, null, recordPropertyFilterStr, null);
/*      */           
/*      */ 
/*  467 */           long timeSpot3 = System.currentTimeMillis();
/*  468 */           RepositoryRowSet recSet = null;
/*      */           try {
/*  470 */             recSet = DDCPUtil.fetchDBRows((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), recQuery, recPropertyFilter, this.mReadBatchSize, Boolean.TRUE);
/*      */           }
/*      */           catch (Exception ex)
/*      */           {
/*  474 */             this.mLogger.error(DDCPLogCode.FAILED_TO_RETRIEVE_FROM_DDCONTAINER, ex, new Object[] { recQuery });
/*  475 */             this.mTracer.traceMinimumMsg("Failed to retrieve records from container with basic schedule : {0} because of exception: {1}", new Object[] { recQuery, ex.getMessage() });
/*      */           }
/*      */           
/*  478 */           long timeSpot4 = System.currentTimeMillis();
/*  479 */           this.mTracer.traceMinimumMsg("Time for first page query to retrieve records: {0} ms.", new Object[] { Long.valueOf(timeSpot4 - timeSpot3) });
/*      */           
/*      */ 
/*  482 */           int totalRecCountInContainer = 0;
/*  483 */           if ((recSet != null) && (!recSet.isEmpty()))
/*      */           {
/*      */ 
/*  486 */             Iterator recIt = recSet.iterator();
/*  487 */             while (recIt.hasNext()) {
/*  488 */               RepositoryRow rec = (RepositoryRow)recIt.next();
/*  489 */               String recID = rec.getProperties().getIdValue("Id").toString();
/*      */               
/*  491 */               recProgressCount++;
/*  492 */               totalReadRecords++;
/*  493 */               if (recProgressCount == 1000) {
/*  494 */                 recProgressCount = 0;
/*  495 */                 this.mTracer.traceMinimumMsg("Just read 1000 records. Already read {0} records in total.", new Object[] { Integer.valueOf(totalReadRecords) });
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*  500 */               if (batchProcessingMgr != null) {
/*  501 */                 batchProcessingMgr.addToBatch(recID, null);
/*      */               }
/*      */               
/*  504 */               recordForUpdatingBatchMap.put(recID, rec);
/*  505 */               updateBatchCount++;
/*      */               
/*  507 */               if (updateBatchCount == this.mUpdateBatchSize)
/*      */               {
/*  509 */                 Set<String> failedUpdatingRecIdSet = null;
/*      */                 
/*  511 */                 if (batchProcessingMgr != null) {
/*  512 */                   failedUpdatingRecIdSet = batchProcessingMgr.batchUpdate();
/*      */                 }
/*      */                 
/*      */ 
/*  516 */                 if ((failedUpdatingRecIdSet != null) && (failedUpdatingRecIdSet.size() > 0)) {
/*  517 */                   recordForUpdatingBatchMap.keySet().removeAll(failedUpdatingRecIdSet);
/*      */                 }
/*      */                 
/*      */ 
/*  521 */                 theReport = writeRecsInfo(hasHeaderIndicator, objectStoreName, advanceDays, theReport, reportBuilder, recordForUpdatingBatchMap, ddContainer, contentSizeCountList);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  528 */                 totalRecCountInContainer += recordForUpdatingBatchMap.size();
/*  529 */                 totalRecCountForReviewer += recordForUpdatingBatchMap.size();
/*  530 */                 totalRecCountInReports += recordForUpdatingBatchMap.size();
/*      */                 
/*      */ 
/*  533 */                 updateBatchCount = 0;
/*      */                 
/*  535 */                 recordForUpdatingBatchMap = new HashMap();
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  543 */           if (!recordForUpdatingBatchMap.isEmpty())
/*      */           {
/*      */ 
/*      */ 
/*  547 */             Set<String> failedUpdatingRecIdSet = null;
/*      */             
/*  549 */             if (batchProcessingMgr != null) {
/*  550 */               failedUpdatingRecIdSet = batchProcessingMgr.batchUpdate();
/*      */             }
/*      */             
/*      */ 
/*  554 */             if ((failedUpdatingRecIdSet != null) && (failedUpdatingRecIdSet.size() > 0)) {
/*  555 */               recordForUpdatingBatchMap.keySet().removeAll(failedUpdatingRecIdSet);
/*      */             }
/*      */             
/*      */ 
/*  559 */             theReport = writeRecsInfo(hasHeaderIndicator, objectStoreName, advanceDays, theReport, reportBuilder, recordForUpdatingBatchMap, ddContainer, contentSizeCountList);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  566 */             totalRecCountInContainer += recordForUpdatingBatchMap.size();
/*  567 */             totalRecCountForReviewer += recordForUpdatingBatchMap.size();
/*  568 */             totalRecCountInReports += recordForUpdatingBatchMap.size();
/*      */             
/*      */ 
/*  571 */             updateBatchCount = 0;
/*      */             
/*  573 */             recordForUpdatingBatchMap = new HashMap();
/*      */           }
/*      */           
/*      */ 
/*  577 */           this.mTracer.traceMinimumMsg("There are {0} records will expire in {1} days in the container with basic schedule {2}.", new Object[] { Integer.valueOf(totalRecCountInContainer), Integer.valueOf(advanceDays), ddContainer.getContainerName() });
/*      */           
/*      */ 
/*  580 */           this.mLogger.info(DDCPLogCode.TOTAL_RECS_IN_CONTAINER, new Object[] { Integer.valueOf(totalRecCountInContainer), Integer.valueOf(advanceDays), ddContainer.getContainerName() });
/*      */           
/*      */ 
/*      */ 
/*  584 */           long timeSpot5 = System.currentTimeMillis();
/*  585 */           float duration1 = (float)(timeSpot5 - timeSpot2) / 1000.0F;
/*  586 */           float throughput1 = totalRecCountInContainer / duration1;
/*  587 */           this.mTracer.traceMinimumMsg("Time to process records in this container {0} : {1} sec.", new Object[] { ddContainer.getContainerName(), Float.valueOf(duration1) });
/*      */           
/*  589 */           this.mTracer.traceMinimumMsg("Throughput: {0} rec/sec.", new Object[] { Float.valueOf(throughput1) });
/*      */           
/*      */ 
/*  592 */           totalRecCountInContainer = 0;
/*      */         }
/*      */         
/*      */ 
/*  596 */         String reportId = null;
/*  597 */         if (totalRecCountForReviewer > 0)
/*      */         {
/*      */ 
/*  600 */           long timeSpot6 = System.currentTimeMillis();
/*      */           
/*  602 */           if (reportBuilder.length() > 0) {
/*  603 */             String docName = DDCPUtil.generateReportName("RetentionDueRecReport_", advanceDays);
/*      */             
/*  605 */             theReport = DDCPUtil.saveReport((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), theReport, reportBuilder, docName);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  610 */           if (theReport != null) {
/*  611 */             reportId = DDCPUtil.checkinReport(theReport);
/*      */           }
/*  613 */           long timeSpot7 = System.currentTimeMillis();
/*      */           
/*  615 */           this.mTracer.traceMinimumMsg("Time to save the rest content and check in the report: {0} ms.", new Object[] { Long.valueOf(timeSpot7 - timeSpot6) });
/*      */           
/*  617 */           this.mTracer.traceMinimumMsg("No. of document content elements: " + theReport.get_ContentElements().size() + "\tTotal size of content: " + theReport.get_ContentSize(), new Object[0]);
/*      */           
/*      */ 
/*  620 */           this.mLogger.info(DDCPLogCode.TOTAL_COUNT_FOR_REVIEWER, new Object[] { reviewer, reportId, Integer.valueOf(advanceDays), Integer.valueOf(ddContainerSet.size()), Integer.valueOf(totalRecCountForReviewer) });
/*      */           
/*  622 */           if (reportId != null) {
/*  623 */             reviewerReportIDMap.put(reviewer, reportId);
/*      */           }
/*      */         }
/*      */         
/*  627 */         this.mTracer.traceMinimumMsg("Reviewer: {0}, report GUID: {1}, advance days: {2}, total containers with basic schedule: {3}, total records: {4}.", new Object[] { reviewer, reportId, Integer.valueOf(advanceDays), Integer.valueOf(ddContainerSet.size()), Integer.valueOf(totalRecCountForReviewer) });
/*      */         
/*      */ 
/*      */ 
/*  631 */         long timeSpot8 = System.currentTimeMillis();
/*  632 */         float durationForReviewer = (float)(timeSpot8 - startTimeForReviewer) / 1000.0F;
/*  633 */         float throughputForReviewer = totalRecCountForReviewer / durationForReviewer;
/*  634 */         this.mTracer.traceMinimumMsg("Time to process records for reviewer {0} : {1} sec, throughput: {2} rec/sec.\n", new Object[] { reviewer, Float.valueOf(durationForReviewer), Float.valueOf(throughputForReviewer) });
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  639 */         totalRecCountForReviewer = 0;
/*      */       }
/*      */       
/*  642 */       long timeSpot9 = System.currentTimeMillis();
/*  643 */       float durationForAllReports = (float)(timeSpot9 - timeSpot0) / 1000.0F;
/*  644 */       float throughputForAllReports = totalReadRecords / durationForAllReports;
/*  645 */       this.mTracer.traceMinimumMsg("Total records in all reports: {0}.", new Object[] { Integer.valueOf(totalRecCountInReports) });
/*  646 */       this.mLogger.info(DDCPLogCode.TOTAL_RECORDS_IN_ALL_REPORTS, new Object[] { Integer.valueOf(totalRecCountInReports) });
/*  647 */       this.mTracer.traceMinimumMsg("Time to process all reports : {0} sec, throughput: {1} rec/sec.\n", new Object[] { Float.valueOf(durationForAllReports), Float.valueOf(throughputForAllReports) });
/*      */ 
/*      */     }
/*      */     catch (DDCPRuntimeException ddcpEx)
/*      */     {
/*  652 */       throw ddcpEx;
/*      */     } catch (Exception generalEx) {
/*  654 */       this.mTracer.traceMinimumMsg("Caught unprocessed exception: {0}", new Object[] { generalEx.getMessage() });
/*  655 */       this.mLogger.error(DDCPLogCode.UNPROCESSED_EXCEPTION, generalEx, new Object[0]);
/*  656 */       throw DDCPRuntimeException.createDDCPRuntimeException(generalEx, DDCPErrorCode.UNEXPECTED_ERROR, new Object[0]);
/*      */     } finally {
/*  658 */       this.mTracer.traceMinimumMsg("---- End disposition report generation ----\n\n", new Object[0]);
/*  659 */       this.mLogger.info(DDCPLogCode.END_REPORT, new Object[] { "----", "----\n\n" });
/*      */       
/*  661 */       if (batchProcessingMgr != null) {
/*  662 */         batchProcessingMgr.stop();
/*      */       }
/*      */     }
/*      */     
/*  666 */     this.mTracer.traceMethodExit(new Object[] { reviewerReportIDMap });
/*      */     
/*  668 */     return reviewerReportIDMap;
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
/*      */   public void launchDDWorkflows(String objectStoreName, String connectionPointName, String deletionWFId, int advanceDays, Map<String, String> reviewerReportIDMap, Map<String, String> configContextMap)
/*      */   {
/*  684 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, connectionPointName, deletionWFId, Integer.valueOf(advanceDays), reviewerReportIDMap, configContextMap });
/*      */     
/*      */ 
/*  687 */     this.mTracer.traceMinimumMsg("---- Start workflow launch ----", new Object[0]);
/*  688 */     this.mLogger.info(DDCPLogCode.START_WF_LAUNCH, new Object[] { "----", "----" });
/*      */     
/*  690 */     if ((reviewerReportIDMap == null) || (reviewerReportIDMap.isEmpty())) {
/*  691 */       this.mTracer.traceMinimumMsg("There is no report, don't need launch workflow.", new Object[0]);
/*  692 */       this.mLogger.info(DDCPLogCode.NO_WORKFLOW_NEEDS_LAUNCH, new Object[0]);
/*  693 */       return;
/*      */     }
/*      */     
/*      */ 
/*  697 */     if (!this.mHaveValidatedReportGenParams) {
/*  698 */       validateReportGenParams(objectStoreName, null, advanceDays, configContextMap);
/*      */     }
/*      */     
/*      */ 
/*  702 */     validatePEParams(objectStoreName, deletionWFId, connectionPointName, configContextMap);
/*      */     
/*      */ 
/*  705 */     Set<Map.Entry<String, String>> reviewerReportMapEntrySet = reviewerReportIDMap.entrySet();
/*  706 */     for (Map.Entry<String, String> reviewerReportMapEntry : reviewerReportMapEntrySet) {
/*  707 */       String reviewer = (String)reviewerReportMapEntry.getKey();
/*  708 */       String reportId = (String)reviewerReportMapEntry.getValue();
/*  709 */       this.mTracer.traceMinimumMsg("Start launching basic schedule workflow for reviewer {0}...", new Object[] { reviewer });
/*  710 */       launchWorkflow(reviewer, reportId, objectStoreName, advanceDays);
/*  711 */       this.mTracer.traceMinimumMsg("Basic schedule workflow is successfully launched for reviewer {0} with report {1}.", new Object[] { reviewer, reportId });
/*      */       
/*      */ 
/*  714 */       this.mLogger.info(DDCPLogCode.WORFLOW_LAUNCHED, new Object[] { reviewer, reportId });
/*      */     }
/*      */     
/*  717 */     this.mTracer.traceMinimumMsg("---- End workflow launch ----\n\n", new Object[0]);
/*  718 */     this.mLogger.info(DDCPLogCode.END_WF_LAUNCH, new Object[] { "----", "----\n\n" });
/*  719 */     this.mTracer.traceMethodExit(new Object[0]);
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
/*      */   private Document writeRecsInfo(Set<Boolean> hasHeaderIndicator, String objectStoreName, int advanceDays, Document theReport, StringBuilder reportBuilder, Map<String, RepositoryRow> recordForUpdatingBatchMap, DDContainerEntity ddContainer, List<Integer> currentContentSizeCountList)
/*      */   {
/*  743 */     this.mTracer.traceMethodEntry(new Object[] { hasHeaderIndicator, objectStoreName, Integer.valueOf(advanceDays), theReport, reportBuilder, recordForUpdatingBatchMap, currentContentSizeCountList });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  748 */     if (recordForUpdatingBatchMap.isEmpty()) {
/*  749 */       return theReport;
/*      */     }
/*      */     
/*  752 */     int contentSizeCount = ((Integer)currentContentSizeCountList.get(0)).intValue();
/*      */     
/*  754 */     Set<Map.Entry<String, RepositoryRow>> updateMapEntrySet = recordForUpdatingBatchMap.entrySet();
/*      */     
/*  756 */     for (Map.Entry<String, RepositoryRow> updateMapEntry : updateMapEntrySet) {
/*  757 */       RepositoryRow rec = (RepositoryRow)updateMapEntry.getValue();
/*      */       
/*  759 */       if (hasHeaderIndicator.isEmpty()) {
/*  760 */         reportBuilder.append(DDCPConstants.HEADLINE_RETENTION_DUE_REPORT);
/*      */         
/*  762 */         hasHeaderIndicator.add(Boolean.valueOf(true));
/*      */       }
/*      */       
/*  765 */       java.util.Date retentionTriggerValue = rec.getProperties().getDateTimeValue(ddContainer.getRetentionTriggerName());
/*      */       
/*  767 */       String retentionTriggerValueStr = DDCPRuntimeException.formatDate(retentionTriggerValue);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */       String recName = rec.getProperties().getStringValue("Name");
/*  776 */       recName = DDCPUtil.sterilizeStringForCSV(recName);
/*  777 */       reportBuilder.append("\"").append(recName).append("\"");
/*      */       
/*  779 */       String recDesc = rec.getProperties().getStringValue("RMEntityDescription");
/*  780 */       if (recDesc == null) {
/*  781 */         recDesc = "";
/*      */       }
/*  783 */       recDesc = DDCPUtil.sterilizeStringForCSV(recDesc);
/*  784 */       reportBuilder.append(",\"").append(recDesc).append("\"");
/*      */       
/*  786 */       String recReviewer = rec.getProperties().getStringValue("Reviewer");
/*  787 */       if (recReviewer == null) {
/*  788 */         recReviewer = "";
/*      */       }
/*  790 */       recReviewer = DDCPUtil.sterilizeStringForCSV(recReviewer);
/*  791 */       reportBuilder.append(",\"").append(recReviewer).append("\"");
/*      */       
/*  793 */       String parentPath = ddContainer.getPathName();
/*  794 */       parentPath = DDCPUtil.sterilizeStringForCSV(parentPath);
/*  795 */       reportBuilder.append(",\"").append(parentPath).append("\"");
/*      */       
/*  797 */       String filePlanName = ddContainer.getFilePlanName();
/*  798 */       filePlanName = DDCPUtil.sterilizeStringForCSV(filePlanName);
/*  799 */       reportBuilder.append(",\"").append(filePlanName).append("\"");
/*      */       
/*  801 */       String triggerName = ddContainer.getRetentionTriggerName();
/*  802 */       triggerName = DDCPUtil.sterilizeStringForCSV(triggerName);
/*  803 */       reportBuilder.append(",\"").append(triggerName).append("\"");
/*      */       
/*  805 */       reportBuilder.append(",").append(retentionTriggerValueStr);
/*      */       
/*  807 */       reportBuilder.append(",").append(ddContainer.getRetentionPeriod());
/*      */       
/*  809 */       reportBuilder.append(",").append(rec.getProperties().getIdValue("Id").toString());
/*      */       
/*      */ 
/*  812 */       reportBuilder.append(",").append(ddContainer.getContainerId());
/*      */       
/*  814 */       reportBuilder.append("\n");
/*      */       
/*  816 */       contentSizeCount++;
/*  817 */       if (contentSizeCount == this.mContentSizeLimit) {
/*  818 */         long timeSpot0 = System.currentTimeMillis();
/*  819 */         String docName = DDCPUtil.generateReportName("RetentionDueRecReport_", advanceDays);
/*      */         
/*  821 */         theReport = DDCPUtil.saveReport((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), theReport, reportBuilder, docName);
/*      */         
/*      */ 
/*      */ 
/*  825 */         reportBuilder.setLength(0);
/*      */         
/*      */ 
/*  828 */         hasHeaderIndicator.remove(Boolean.valueOf(true));
/*      */         
/*  830 */         contentSizeCount = 0;
/*      */         
/*  832 */         long timeSpot1 = System.currentTimeMillis();
/*  833 */         this.mTracer.traceMinimumMsg("Time to save this content element: {0} ms.", new Object[] { Long.valueOf(timeSpot1 - timeSpot0) });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  839 */     currentContentSizeCountList.set(0, Integer.valueOf(contentSizeCount));
/*      */     
/*  841 */     this.mTracer.traceMethodExit(new Object[] { theReport });
/*      */     
/*  843 */     return theReport;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getReportRecoveryID()
/*      */   {
/*  855 */     this.mTracer.traceMethodEntry(new Object[0]);
/*  856 */     Double result = Double.valueOf(Math.random() * 3000.0D + 1000.0D);
/*  857 */     this.mTracer.traceMethodExit(new Object[] { Integer.valueOf(result.intValue()) });
/*  858 */     return result.intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getComparisonTimeinUTCString(DDContainerEntity ddContainerEntity, int advanceDays)
/*      */   {
/*  870 */     this.mTracer.traceMethodEntry(new Object[] { ddContainerEntity, Integer.valueOf(advanceDays) });
/*  871 */     SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
/*  872 */     f.setTimeZone(TimeZone.getTimeZone("UTC"));
/*      */     
/*  874 */     Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/*      */     
/*  876 */     currentTime.add(5, advanceDays);
/*      */     
/*  878 */     String currentTimeInUTC = f.format(currentTime.getTime());
/*  879 */     this.mTracer.traceMinimumMsg("Current time + advance days {0} in UTC yyyyMMdd''T''HHmmss''Z'' : {1}", new Object[] { Integer.valueOf(advanceDays), currentTimeInUTC });
/*      */     
/*  881 */     currentTime.add(1, ddContainerEntity.getRetentionYears() * -1);
/*  882 */     currentTime.add(2, ddContainerEntity.getRetentionMonths() * -1);
/*  883 */     currentTime.add(5, ddContainerEntity.getRetentionDays() * -1);
/*      */     
/*      */ 
/*      */ 
/*  887 */     currentTime.set(11, 0);
/*  888 */     currentTime.clear(12);
/*  889 */     currentTime.clear(13);
/*  890 */     currentTime.add(5, 1);
/*      */     
/*  892 */     String result = f.format(currentTime.getTime());
/*  893 */     this.mTracer.traceMethodExit(new Object[] { result });
/*  894 */     return result;
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
/*      */   private Map<String, Set<DDContainerEntity>> validateReportGenParams(String objectStoreName, Set<String> containerIdSet, int advanceDays, Map<String, String> configContextMap)
/*      */   {
/*  913 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, containerIdSet, Integer.valueOf(advanceDays), configContextMap });
/*      */     
/*      */ 
/*  916 */     if ((configContextMap == null) || (configContextMap.isEmpty())) {
/*  917 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.EMPTY_CONFIG_CONTEXT_MAP, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  923 */     Map<String, Set<DDContainerEntity>> organizedContainerEntityMap = null;
/*      */     
/*      */ 
/*  926 */     if ((objectStoreName == null) || ("".equals(objectStoreName.trim()))) {
/*  927 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_OBJECT_STORE_NAME, new Object[] { objectStoreName });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  932 */     if (advanceDays < 0) {
/*  933 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_ADVANCE_DAYS, new Object[] { Integer.valueOf(advanceDays) });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  938 */     validatePerformanceTuningParams(configContextMap);
/*      */     
/*      */ 
/*  941 */     getJaceObjectStore(objectStoreName, configContextMap);
/*      */     
/*      */ 
/*  944 */     verifyFPOSGetDataModel(objectStoreName);
/*      */     
/*  946 */     if ((containerIdSet != null) && (containerIdSet.size() > 0))
/*      */     {
/*  948 */       organizedContainerEntityMap = validateContainerIds(objectStoreName, containerIdSet);
/*      */     }
/*      */     
/*      */ 
/*  952 */     this.mHaveValidatedReportGenParams = true;
/*      */     
/*  954 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */     
/*  956 */     return organizedContainerEntityMap;
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
/*      */   private void validatePEParams(String objectStoreName, String deletionWFId, String connectionPointName, Map<String, String> configContextMap)
/*      */   {
/*  970 */     if ((connectionPointName == null) || ("".equals(connectionPointName.trim()))) {
/*  971 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.EMPTY_CONNECTION_POINT_NAME, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  976 */     getPESession(connectionPointName, configContextMap);
/*      */     
/*      */ 
/*  979 */     if ((deletionWFId == null) || ("".equals(deletionWFId.trim()))) {
/*  980 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.EMPTY_WORKFLOW_ID, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  985 */     getWFDefVWVersion(objectStoreName, deletionWFId);
/*      */     
/*      */ 
/*  988 */     validateInitWorkflowConfigMap(objectStoreName, configContextMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validateInitWorkflowConfigMap(String objectStoreName, Map<String, String> configContextMap)
/*      */   {
/* 1000 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, configContextMap });
/*      */     
/* 1002 */     this.mWorkflowConfigMap.put("NeedApproval", "false");
/* 1003 */     this.mWorkflowConfigMap.put("AlwaysDeclareRecord", "true");
/* 1004 */     this.mWorkflowConfigMap.put("AlwaysShowDeclareResult", "false");
/* 1005 */     this.mWorkflowConfigMap.put("RecordContainerID", "");
/*      */     
/* 1007 */     if (configContextMap.containsKey("NeedApproval")) {
/* 1008 */       String needApproval = (String)configContextMap.get("NeedApproval");
/* 1009 */       if ((needApproval != null) && ("true".equalsIgnoreCase(needApproval.trim()))) {
/* 1010 */         this.mWorkflowConfigMap.put("NeedApproval", "true");
/*      */       }
/*      */     }
/*      */     
/* 1014 */     if (configContextMap.containsKey("AlwaysDeclareRecord")) {
/* 1015 */       String declareRec = (String)configContextMap.get("AlwaysDeclareRecord");
/* 1016 */       if ((declareRec != null) && ("false".equalsIgnoreCase(declareRec.trim()))) {
/* 1017 */         this.mWorkflowConfigMap.put("AlwaysDeclareRecord", "false");
/*      */       }
/*      */     }
/*      */     
/* 1021 */     if (configContextMap.containsKey("AlwaysShowDeclareResult")) {
/* 1022 */       String showDeclareResult = (String)configContextMap.get("AlwaysShowDeclareResult");
/* 1023 */       if ((showDeclareResult != null) && ("true".equalsIgnoreCase(showDeclareResult.trim()))) {
/* 1024 */         this.mWorkflowConfigMap.put("AlwaysShowDeclareResult", "true");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1029 */     if (Boolean.valueOf((String)this.mWorkflowConfigMap.get("AlwaysDeclareRecord")).booleanValue())
/*      */     {
/*      */ 
/* 1032 */       ObjectStore fpos = (ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName);
/* 1033 */       boolean canDeclare = getCanDeclare(fpos, "Transcript");
/* 1034 */       if (!canDeclare) {
/* 1035 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.NOT_SUPPORT_DECLARE_REC, new Object[] { objectStoreName });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1040 */       if (!configContextMap.containsKey("RecordContainerID")) {
/* 1041 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.NO_RECORD_CONTAINER_ID, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/* 1045 */       String recContainerID = (String)configContextMap.get("RecordContainerID");
/*      */       
/* 1047 */       if ((recContainerID == null) || ("".equals(recContainerID.trim())) || (!Id.isId(recContainerID))) {
/* 1048 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_RECORD_CONTAINER_ID, new Object[] { recContainerID });
/*      */       }
/*      */       
/*      */ 
/* 1052 */       String queryForRecContainer = String.format(QUERY_RECORD_CONTAINER, new Object[] { recContainerID });
/* 1053 */       this.mTracer.traceMinimumMsg("Query for validating record container: {0}", new Object[] { queryForRecContainer });
/*      */       
/* 1055 */       long startTime = System.currentTimeMillis();
/* 1056 */       RepositoryRowSet resultSet = null;
/*      */       try {
/* 1058 */         resultSet = DDCPUtil.fetchDBRows((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), queryForRecContainer, recContainerPropertyFilter, 1, Boolean.FALSE);
/*      */ 
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 1063 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.INVALID_RECORD_CONTAINER_ID, new Object[] { recContainerID });
/*      */       }
/*      */       
/* 1066 */       long endTime = System.currentTimeMillis();
/* 1067 */       this.mTracer.traceMinimumMsg("Time to retrieve record container : {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*      */       
/*      */ 
/* 1070 */       if ((resultSet == null) || (resultSet.isEmpty())) {
/* 1071 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_RECORD_CONTAINER_ID, new Object[] { recContainerID });
/*      */       }
/*      */       
/*      */ 
/* 1075 */       Iterator rowIter = resultSet.iterator();
/* 1076 */       if (rowIter.hasNext()) {
/* 1077 */         RepositoryRow row = (RepositoryRow)rowIter.next();
/* 1078 */         Integer32List allowedRMTypes = row.getProperties().getInteger32ListValue("AllowedRMTypes");
/*      */         
/* 1080 */         if ((allowedRMTypes == null) || (allowedRMTypes.isEmpty()) || (!allowedRMTypes.contains(Integer.valueOf(301))))
/*      */         {
/*      */ 
/* 1083 */           throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_RECORD_CONTAINER_ID, new Object[] { recContainerID });
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1089 */       this.mWorkflowConfigMap.put("RecordContainerID", recContainerID);
/*      */     }
/*      */     
/* 1092 */     this.mTracer.traceMethodExit(new Object[] { this.mWorkflowConfigMap });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean getCanDeclare(ObjectStore jaceOS, String className)
/*      */   {
/* 1104 */     this.mTracer.traceMethodEntry(new Object[] { className });
/* 1105 */     boolean canDeclare = false;
/*      */     
/*      */ 
/* 1108 */     PropertyFilter pf = new PropertyFilter();
/* 1109 */     pf.addIncludeType(0, null, Boolean.TRUE, FilteredPropertyType.ANY, null);
/*      */     
/*      */ 
/* 1112 */     ClassDefinition objClassDef = Factory.ClassDefinition.fetchInstance(jaceOS, className, pf);
/*      */     
/*      */ 
/* 1115 */     PropertyDefinitionList objPropDefs = objClassDef.get_PropertyDefinitions();
/*      */     
/* 1117 */     Iterator iter = objPropDefs.iterator();
/* 1118 */     PropertyDefinition objPropDef = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1123 */     while (iter.hasNext())
/*      */     {
/* 1125 */       objPropDef = (PropertyDefinition)iter.next();
/*      */       
/*      */ 
/* 1128 */       String objPropDefSymbolicName = objPropDef.get_SymbolicName();
/*      */       
/* 1130 */       if (objPropDefSymbolicName.equalsIgnoreCase("CanDeclare"))
/*      */       {
/*      */ 
/* 1133 */         PropertyDefinitionBoolean canDeclareProp = (PropertyDefinitionBoolean)objPropDef;
/* 1134 */         canDeclare = canDeclareProp.get_PropertyDefaultBoolean().booleanValue();
/*      */       }
/*      */     }
/*      */     
/* 1138 */     this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(canDeclare) });
/* 1139 */     return canDeclare;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private VWSession getPESession(String connectionPointName, Map<String, String> configContextMap)
/*      */   {
/* 1150 */     this.mTracer.traceMethodEntry(new Object[] { connectionPointName });
/*      */     try {
/* 1152 */       String ceUri = (String)configContextMap.get("Uri");
/*      */       
/* 1154 */       establishJaceSecuritySubject(ceUri, configContextMap);
/*      */       
/* 1156 */       this.mPESession = new VWSession();
/* 1157 */       this.mPESession.setBootstrapCEURI(ceUri);
/* 1158 */       this.mPESession.logon(connectionPointName);
/*      */     } catch (Exception ex) {
/* 1160 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_GET_PESESSION, new Object[0]);
/*      */     }
/*      */     
/* 1163 */     this.mTracer.traceMethodExit(new Object[0]);
/* 1164 */     return this.mPESession;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getWFDefVWVersion(String objectStoreName, String workflowId)
/*      */   {
/* 1176 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, workflowId });
/*      */     
/* 1178 */     String queryForVWVersion = String.format(QUERY_WORKFLOW_VWVERSION, new Object[] { workflowId });
/* 1179 */     this.mTracer.traceMinimumMsg("Query for VWVersion: {0}", new Object[] { queryForVWVersion });
/*      */     
/* 1181 */     long startTime = System.currentTimeMillis();
/* 1182 */     RepositoryRowSet resultSet = null;
/*      */     try {
/* 1184 */       resultSet = DDCPUtil.fetchDBRows((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), queryForVWVersion, workflowPropertyFilter, 1, Boolean.FALSE);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1188 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_GET_VWVERSION, new Object[] { workflowId });
/*      */     }
/* 1190 */     long endTime = System.currentTimeMillis();
/* 1191 */     this.mTracer.traceMinimumMsg("Time to retrieve VWVersion : {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*      */     
/* 1193 */     if ((resultSet == null) || (resultSet.isEmpty())) {
/* 1194 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.FAILED_TO_GET_VWVERSION, new Object[] { workflowId });
/*      */     }
/*      */     
/* 1197 */     Iterator rowIter = resultSet.iterator();
/* 1198 */     if (rowIter.hasNext())
/*      */     {
/* 1200 */       RepositoryRow row = (RepositoryRow)rowIter.next();
/* 1201 */       this.mVWVersion = row.getProperties().getStringValue("VWVersion");
/* 1202 */       if ((this.mVWVersion == null) || ("".equals(this.mVWVersion))) {
/* 1203 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.FAILED_TO_GET_VWVERSION, new Object[] { workflowId });
/*      */       }
/*      */     }
/*      */     else {
/* 1207 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.FAILED_TO_GET_VWVERSION, new Object[] { workflowId });
/*      */     }
/*      */     
/* 1210 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */     
/* 1212 */     return this.mVWVersion;
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
/*      */   private void launchWorkflow(String reviewer, String reportID, String objectStoreName, int advanceDays)
/*      */   {
/* 1226 */     this.mTracer.traceMethodEntry(new Object[] { reviewer, reportID, objectStoreName, Integer.valueOf(advanceDays) });
/*      */     try {
/* 1228 */       VWStepElement launchStep = this.mPESession.createWorkflow(this.mVWVersion);
/*      */       
/* 1230 */       launchStep.setParameterValue("LogLevel", this.mWorkflowLogLevel, false);
/* 1231 */       launchStep.setParameterValue("TraceLevel", this.mWorkflowTraceLevel, false);
/*      */       
/* 1233 */       launchStep.setParameterValue("Reviewer", reviewer, false);
/* 1234 */       launchStep.setParameterValue("RetentionDueReportID", reportID, false);
/* 1235 */       launchStep.setParameterValue("ObjectStoreName", objectStoreName, false);
/* 1236 */       launchStep.setParameterValue("ReportAdvanceDays", Integer.valueOf(advanceDays), false);
/*      */       
/* 1238 */       launchStep.setParameterValue("NeedApproval", Boolean.valueOf((String)this.mWorkflowConfigMap.get("NeedApproval")), false);
/*      */       
/* 1240 */       launchStep.setParameterValue("AlwaysDeclareRecord", Boolean.valueOf((String)this.mWorkflowConfigMap.get("AlwaysDeclareRecord")), false);
/*      */       
/*      */ 
/*      */ 
/* 1244 */       if (Boolean.valueOf((String)this.mWorkflowConfigMap.get("AlwaysDeclareRecord")).booleanValue()) {
/* 1245 */         launchStep.setParameterValue("RecordContainerID", this.mWorkflowConfigMap.get("RecordContainerID"), false);
/*      */       }
/*      */       
/*      */ 
/* 1249 */       launchStep.setParameterValue("AlwaysShowDeclareResult", Boolean.valueOf((String)this.mWorkflowConfigMap.get("AlwaysShowDeclareResult")), false);
/*      */       
/*      */ 
/* 1252 */       launchStep.setParameterValue("RetrievalBatchSize", Integer.valueOf(this.mReadBatchSize), false);
/* 1253 */       launchStep.setParameterValue("UpdateBatchSize", Integer.valueOf(this.mUpdateBatchSize), false);
/* 1254 */       launchStep.setParameterValue("ThreadCount", Integer.valueOf(this.mThreadCount), false);
/* 1255 */       launchStep.setParameterValue("ReportContentSizeLimit", Integer.valueOf(this.mContentSizeLimit), false);
/* 1256 */       launchStep.setParameterValue("LinkCacheSize", Integer.valueOf(this.mLinkCacheSizeLimit), false);
/* 1257 */       launchStep.setParameterValue("OnHoldCacheSize", Integer.valueOf(this.mOnHoldContainerCacheSizeLimit), false);
/*      */       
/*      */ 
/* 1260 */       launchStep.doDispatch();
/*      */     } catch (Exception ex) {
/* 1262 */       this.mLogger.error(DDCPLogCode.FAILED_TO_LAUNCH_WORKFLOW, ex, new Object[] { reviewer });
/* 1263 */       this.mTracer.traceMinimumMsg("Failed to launch workflow for reviewer {0} because of {1}.", new Object[] { reviewer, ex.getMessage() });
/*      */       
/* 1265 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_LAUNCH_WORKFLOW, new Object[0]);
/*      */     }
/* 1267 */     this.mTracer.traceMethodExit(new Object[0]);
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
/*      */   private Map<String, Set<DDContainerEntity>> validateContainerIds(String objectStoreName, Set<String> containerIdSet)
/*      */   {
/* 1283 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, containerIdSet });
/*      */     
/*      */ 
/*      */ 
/* 1287 */     Map<String, Set<DDContainerEntity>> organizedContainerEntityMap = new HashMap();
/*      */     
/* 1289 */     Set<DDContainerEntity> ddContainerSet = new HashSet();
/* 1290 */     Set<DDContainerEntity> nonDDContainerSet = new HashSet();
/* 1291 */     organizedContainerEntityMap.put("DDContainers", ddContainerSet);
/* 1292 */     organizedContainerEntityMap.put("NonDDContainers", nonDDContainerSet);
/*      */     
/*      */ 
/* 1295 */     for (String containerId : containerIdSet) {
/* 1296 */       if (!Id.isId(containerId.trim())) {
/* 1297 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_CONTAINER_IDS, new Object[] { containerId });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1302 */     Set<String> originalContainerIdSet = new HashSet();
/* 1303 */     for (String containerID : containerIdSet) {
/* 1304 */       originalContainerIdSet.add(containerID.trim());
/*      */     }
/*      */     
/* 1307 */     Set<String> foundContainerIdSet = new HashSet();
/*      */     
/* 1309 */     List<String> oriContainerIdList = new ArrayList();
/* 1310 */     oriContainerIdList.addAll(originalContainerIdSet);
/* 1311 */     this.mTracer.traceMinimumMsg("Need validate {0} containers.", new Object[] { Integer.valueOf(oriContainerIdList.size()) });
/*      */     
/*      */ 
/* 1314 */     int startIndex = 0;
/* 1315 */     int endIndex = oriContainerIdList.size();
/* 1316 */     if (oriContainerIdList.size() > 100) {
/* 1317 */       endIndex = 100;
/*      */     }
/* 1319 */     while (startIndex < oriContainerIdList.size()) {
/* 1320 */       List<String> subIdList = oriContainerIdList.subList(startIndex, endIndex);
/*      */       
/* 1322 */       String containerIdsInStr = DDCPUtil.getEntityIdListInString(subIdList);
/* 1323 */       String queryStr = String.format(QUERY_TEMPLATE_VERIFY_SPECIFIED_CONTAINER_IDS, new Object[] { containerIdsInStr });
/*      */       
/*      */ 
/* 1326 */       this.mTracer.traceMinimumMsg("Query to verify container IDs: {0} with {1} items.", new Object[] { QUERY_TEMPLATE_VERIFY_SPECIFIED_CONTAINER_IDS, Integer.valueOf(subIdList.size()) });
/*      */       
/*      */ 
/* 1329 */       long startTime = System.currentTimeMillis();
/* 1330 */       RepositoryRowSet resultSet = null;
/*      */       try {
/* 1332 */         resultSet = DDCPUtil.fetchDBRows((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), queryStr, ddContainerPropertyFilter, this.mReadBatchSize, Boolean.TRUE);
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 1336 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { queryStr });
/*      */       }
/*      */       
/* 1339 */       long endTime = System.currentTimeMillis();
/* 1340 */       this.mTracer.traceMinimumMsg("Time for first page query of verifying container IDs: {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*      */       
/*      */ 
/* 1343 */       if ((resultSet != null) && (!resultSet.isEmpty())) {
/* 1344 */         Iterator it = resultSet.iterator();
/* 1345 */         while (it.hasNext()) {
/* 1346 */           RepositoryRow containerRow = (RepositoryRow)it.next();
/* 1347 */           if (isValidContainer(containerRow)) {
/* 1348 */             DDContainerEntity containerEntity = getDDContainerEntity(containerRow);
/*      */             
/* 1350 */             foundContainerIdSet.add(containerEntity.getContainerId());
/*      */             
/* 1352 */             if (containerEntity.getRetentionTriggerName() != null)
/*      */             {
/* 1354 */               ddContainerSet.add(containerEntity);
/*      */             }
/*      */             else {
/* 1357 */               nonDDContainerSet.add(containerEntity);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1363 */       startIndex = endIndex;
/* 1364 */       endIndex += 100;
/* 1365 */       if (endIndex > oriContainerIdList.size()) {
/* 1366 */         endIndex = oriContainerIdList.size();
/*      */       }
/*      */     }
/*      */     
/* 1370 */     originalContainerIdSet.removeAll(foundContainerIdSet);
/* 1371 */     if (originalContainerIdSet.size() > 0)
/*      */     {
/* 1373 */       String notFoundContainerIds = DDCPUtil.getEntityIdSetInString(originalContainerIdSet);
/* 1374 */       this.mLogger.warn(DDCPLogCode.NOT_FOUND_SPECIFIED_CONTAINER_IDS, new Object[] { notFoundContainerIds });
/* 1375 */       this.mTracer.traceMinimumMsg("The containers with following specified container IDs cannot be found or are on hold or are marked as deleted: {0}", new Object[] { notFoundContainerIds });
/*      */     }
/*      */     
/* 1378 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */     
/* 1380 */     return organizedContainerEntityMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isValidContainer(RepositoryRow containerRow)
/*      */   {
/* 1389 */     int rmEntityType = containerRow.getProperties().getInteger32Value("RMEntityType").intValue();
/* 1390 */     if (rmEntityType == 100)
/*      */     {
/* 1392 */       return true;
/*      */     }
/* 1394 */     Boolean onHold = containerRow.getProperties().getBooleanValue("OnHold");
/* 1395 */     Boolean isDeleted = containerRow.getProperties().getBooleanValue("IsDeleted");
/* 1396 */     if ((onHold != null) && (isDeleted != null) && (!onHold.booleanValue()) && (!isDeleted.booleanValue())) {
/* 1397 */       return true;
/*      */     }
/*      */     
/* 1400 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private DDContainerEntity getDDContainerEntity(RepositoryRow containerRow)
/*      */   {
/* 1409 */     this.mTracer.traceMethodEntry(new Object[0]);
/* 1410 */     DDContainerEntity containerEntity = new DDContainerEntity();
/* 1411 */     containerEntity.setContainerId(containerRow.getProperties().getIdValue("Id").toString());
/*      */     
/* 1413 */     containerEntity.setContainerName(containerRow.getProperties().getStringValue("Name"));
/*      */     
/*      */ 
/* 1416 */     Integer rmEntityType = containerRow.getProperties().getInteger32Value("RMEntityType");
/* 1417 */     containerEntity.setContainerType(rmEntityType.toString());
/*      */     
/*      */ 
/* 1420 */     if (rmEntityType.intValue() == 100) {
/* 1421 */       return containerEntity;
/*      */     }
/*      */     
/* 1424 */     String pathName = containerRow.getProperties().getStringValue("PathName");
/* 1425 */     if ((pathName != null) && (pathName.startsWith("/Records Management/"))) {
/* 1426 */       String[] nodes = pathName.split("/");
/* 1427 */       String filePlanName = nodes[2];
/* 1428 */       containerEntity.setFilePlanName(filePlanName);
/*      */       
/* 1430 */       StringBuilder parentPathBuilder = new StringBuilder();
/* 1431 */       for (int i = 3; i < nodes.length; i++) {
/* 1432 */         parentPathBuilder.append(nodes[i]);
/* 1433 */         parentPathBuilder.append("/");
/*      */       }
/* 1435 */       String parentPath = parentPathBuilder.substring(0, parentPathBuilder.length() - 1);
/* 1436 */       containerEntity.setPathName(parentPath);
/*      */     }
/*      */     
/* 1439 */     containerEntity.setReviewer(containerRow.getProperties().getStringValue("Reviewer"));
/*      */     
/*      */ 
/* 1442 */     String retentionTriggerName = containerRow.getProperties().getStringValue("RMRetentionTriggerPropertyName");
/*      */     
/* 1444 */     containerEntity.setRetentionTriggerName(retentionTriggerName);
/*      */     
/* 1446 */     if ((retentionTriggerName != null) && (!"".equals(retentionTriggerName)))
/*      */     {
/* 1448 */       String retentionPeriod = containerRow.getProperties().getStringValue("RMRetentionPeriod");
/*      */       
/*      */ 
/*      */ 
/* 1452 */       if ((retentionPeriod == null) || ("".equals(retentionPeriod.trim()))) {
/* 1453 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.NO_RETENTION_PERIOD_DDCONTAINER, new Object[] { containerEntity.getContainerName() });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1458 */       if (!retentionPeriod.matches("[0-9]+-[0-9]+-[0-9]+")) {
/* 1459 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_RETENTION_PERIOD_FORMAT, new Object[] { retentionPeriod });
/*      */       }
/*      */       
/*      */ 
/* 1463 */       String[] retentionPeriodsArray = retentionPeriod.split("-", -1);
/* 1464 */       containerEntity.setRetentionYears(Integer.valueOf(retentionPeriodsArray[0]).intValue());
/* 1465 */       containerEntity.setRetentionMonths(Integer.valueOf(retentionPeriodsArray[1]).intValue());
/* 1466 */       containerEntity.setRetentionDays(Integer.valueOf(retentionPeriodsArray[2]).intValue());
/*      */       
/*      */ 
/* 1469 */       containerEntity.setRetentionPeriod(retentionPeriod);
/*      */     }
/* 1471 */     this.mTracer.traceMethodExit(new Object[0]);
/* 1472 */     return containerEntity;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validatePerformanceTuningParams(Map<String, String> configContextMap)
/*      */   {
/* 1483 */     this.mTracer.traceMethodEntry(new Object[] { configContextMap });
/*      */     
/* 1485 */     if (configContextMap.containsKey("ThreadCount")) {
/* 1486 */       int threadCount = Integer.valueOf((String)configContextMap.get("ThreadCount")).intValue();
/*      */       
/*      */ 
/* 1489 */       if (threadCount > 0) {
/* 1490 */         this.mThreadCount = threadCount;
/*      */       }
/*      */     }
/* 1493 */     this.mTracer.traceMinimumMsg("Thread count : {0}", new Object[] { Integer.valueOf(this.mThreadCount) });
/* 1494 */     this.mLogger.info(DDCPLogCode.THREAD_COUNT, new Object[] { Integer.valueOf(this.mThreadCount) });
/*      */     
/* 1496 */     if (configContextMap.containsKey("ReadBatchSize")) {
/* 1497 */       int readBatchSize = Integer.valueOf((String)configContextMap.get("ReadBatchSize")).intValue();
/*      */       
/*      */ 
/* 1500 */       if (readBatchSize > 0) {
/* 1501 */         this.mReadBatchSize = readBatchSize;
/*      */       }
/*      */     }
/* 1504 */     this.mTracer.traceMinimumMsg("Read batch size : {0}", new Object[] { Integer.valueOf(this.mReadBatchSize) });
/* 1505 */     this.mLogger.info(DDCPLogCode.READ_BATCH_SIZE, new Object[] { Integer.valueOf(this.mReadBatchSize) });
/*      */     
/* 1507 */     if (configContextMap.containsKey("UpdateBatchSize")) {
/* 1508 */       int updateBatchSize = Integer.valueOf((String)configContextMap.get("UpdateBatchSize")).intValue();
/*      */       
/*      */ 
/* 1511 */       if (updateBatchSize > 0) {
/* 1512 */         this.mUpdateBatchSize = updateBatchSize;
/*      */       }
/*      */     }
/* 1515 */     this.mTracer.traceMinimumMsg("Update batch size : {0}", new Object[] { Integer.valueOf(this.mUpdateBatchSize) });
/* 1516 */     this.mLogger.info(DDCPLogCode.UPDATE_BATCH_SIZE, new Object[] { Integer.valueOf(this.mUpdateBatchSize) });
/*      */     
/* 1518 */     if (configContextMap.containsKey("ContentSizeLimit")) {
/* 1519 */       int contentSizeLimit = Integer.valueOf((String)configContextMap.get("ContentSizeLimit")).intValue();
/*      */       
/*      */ 
/* 1522 */       if (contentSizeLimit > 0) {
/* 1523 */         this.mContentSizeLimit = contentSizeLimit;
/*      */       }
/*      */     }
/* 1526 */     this.mTracer.traceMinimumMsg("Content size limit : {0}", new Object[] { Integer.valueOf(this.mContentSizeLimit) });
/* 1527 */     this.mLogger.info(DDCPLogCode.CONTENT_SIZE_LIMIT, new Object[] { Integer.valueOf(this.mContentSizeLimit) });
/*      */     
/* 1529 */     if (configContextMap.containsKey("OnHoldContainerCacheSizeLimit")) {
/* 1530 */       int onHoldContainerCacheSizeLimit = Integer.valueOf((String)configContextMap.get("OnHoldContainerCacheSizeLimit")).intValue();
/*      */       
/*      */ 
/* 1533 */       if (onHoldContainerCacheSizeLimit > 0) {
/* 1534 */         this.mOnHoldContainerCacheSizeLimit = onHoldContainerCacheSizeLimit;
/*      */       }
/*      */     }
/* 1537 */     this.mTracer.traceMinimumMsg("On hold Container cache size limit : {0}", new Object[] { Integer.valueOf(this.mOnHoldContainerCacheSizeLimit) });
/* 1538 */     this.mLogger.info(DDCPLogCode.ONHOLD_CONTAINER_CACHE_SIZE_LIMIT, new Object[] { Integer.valueOf(this.mOnHoldContainerCacheSizeLimit) });
/*      */     
/* 1540 */     if (configContextMap.containsKey("LinkCacheSizeLimit")) {
/* 1541 */       int linkCacheSizeLimit = Integer.valueOf((String)configContextMap.get("LinkCacheSizeLimit")).intValue();
/*      */       
/*      */ 
/* 1544 */       if (linkCacheSizeLimit > 0) {
/* 1545 */         this.mLinkCacheSizeLimit = linkCacheSizeLimit;
/*      */       }
/*      */     }
/* 1548 */     this.mTracer.traceMinimumMsg("Link cache size limit : {0}", new Object[] { Integer.valueOf(this.mLinkCacheSizeLimit) });
/* 1549 */     this.mLogger.info(DDCPLogCode.LINK_CACHE_SIZE_LIMIT, new Object[] { Integer.valueOf(this.mLinkCacheSizeLimit) });
/*      */     
/* 1551 */     this.mTracer.traceMethodExit(new Object[0]);
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
/*      */   private ObjectStore getJaceObjectStore(String objectStoreName, Map<String, String> contextMap)
/*      */   {
/* 1564 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, contextMap });
/*      */     
/* 1566 */     if (!this.mJaceObjectStoreCache.containsKey(objectStoreName)) {
/* 1567 */       String serverUrl = null;
/* 1568 */       if (!contextMap.containsKey("Uri")) {
/* 1569 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_CEURL, new Object[0]);
/*      */       }
/*      */       
/* 1572 */       serverUrl = (String)contextMap.get("Uri");
/*      */       
/*      */ 
/* 1575 */       establishJaceSecuritySubject(serverUrl, contextMap);
/*      */       
/* 1577 */       ObjectStore jaceOS = null;
/*      */       try
/*      */       {
/* 1580 */         Connection connection = Factory.Connection.getConnection(serverUrl);
/*      */         
/*      */ 
/*      */ 
/* 1584 */         PropertyFilter pf = new PropertyFilter();
/* 1585 */         pf.setMaxRecursion(1);
/* 1586 */         pf.addIncludeProperty(new FilterElement(null, null, null, "SubsystemConfigurations ServerCacheConfiguration QueryPageMaxSize NonPagedQueryMaxSize", null));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1592 */         Domain jaceDomain = Factory.Domain.fetchInstance(connection, null, pf);
/*      */         
/*      */ 
/* 1595 */         SubsystemConfigurationList subSysConfigList = jaceDomain.get_SubsystemConfigurations();
/*      */         
/* 1597 */         Iterator subSysConfigIte = subSysConfigList.iterator();
/* 1598 */         ServerCacheConfiguration serverCachConfig = null;
/* 1599 */         while (subSysConfigIte.hasNext()) {
/* 1600 */           Object config = subSysConfigIte.next();
/* 1601 */           if ((config instanceof ServerCacheConfiguration)) {
/* 1602 */             serverCachConfig = (ServerCacheConfiguration)config;
/* 1603 */             break;
/*      */           }
/*      */         }
/* 1606 */         if (serverCachConfig != null) {
/* 1607 */           Integer queryPageMaxSize = serverCachConfig.get_QueryPageMaxSize();
/*      */           
/* 1609 */           Integer nonPagedQueryMaxSize = serverCachConfig.get_NonPagedQueryMaxSize();
/*      */           
/* 1611 */           this.mTracer.traceMinimumMsg("CE QueryPageMaxSize : {0}", new Object[] { queryPageMaxSize });
/* 1612 */           this.mLogger.info(DDCPLogCode.CE_QUERY_PAGE_MAX_SIZE, new Object[] { queryPageMaxSize });
/*      */           
/* 1614 */           this.mTracer.traceMinimumMsg("CE NoPagedQueryMaxSize : {0}", new Object[] { nonPagedQueryMaxSize });
/* 1615 */           this.mLogger.info(DDCPLogCode.CE_NON_PAGED_QUERY_MAX_SIZE, new Object[] { nonPagedQueryMaxSize });
/*      */         }
/*      */         
/*      */ 
/* 1619 */         jaceOS = com.filenet.api.core.Factory.ObjectStore.fetchInstance(jaceDomain, objectStoreName.trim(), null);
/*      */       }
/*      */       catch (Exception ex) {
/* 1622 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.FAIL_TO_FETCH_CEOBJECTSTORE, new Object[] { objectStoreName });
/*      */       }
/*      */       
/*      */ 
/* 1626 */       this.mJaceObjectStoreCache.put(objectStoreName, jaceOS);
/*      */       
/* 1628 */       this.mTracer.traceMethodExit(new Object[0]);
/*      */       
/* 1630 */       return jaceOS;
/*      */     }
/* 1632 */     this.mTracer.traceMethodExit(new Object[0]);
/* 1633 */     return (ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void verifyFPOSGetDataModel(String objectStoreName)
/*      */   {
/* 1644 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName });
/*      */     
/* 1646 */     this.mTracer.traceMinimumMsg("Query to verify FPOS and get data model: {0}", new Object[] { QUERY_CHECK_FPOS_DOD_MODEL });
/*      */     
/* 1648 */     PropertyFilter filter = new PropertyFilter();
/* 1649 */     FilterElement fe = new FilterElement(null, null, null, "Id", null);
/* 1650 */     filter.addIncludeProperty(fe);
/*      */     
/* 1652 */     long startTime = System.currentTimeMillis();
/* 1653 */     RepositoryRowSet resultSet = null;
/*      */     try {
/* 1655 */       resultSet = DDCPUtil.fetchDBRows((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), QUERY_CHECK_FPOS_DOD_MODEL, filter, 1, Boolean.FALSE);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1659 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.OBJECT_STORE_IS_NOT_FPOS, new Object[0]);
/*      */     }
/* 1661 */     long endTime = System.currentTimeMillis();
/* 1662 */     this.mTracer.traceMinimumMsg("Time to retrieve FPOS Setup from SystemConfiguration : {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*      */     
/* 1664 */     if ((resultSet == null) || (resultSet.isEmpty())) {
/* 1665 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.OBJECT_STORE_IS_NOT_FPOS, new Object[0]);
/*      */     }
/*      */     
/* 1668 */     boolean isDODModel = false;
/* 1669 */     Iterator rowIter = resultSet.iterator();
/* 1670 */     if (rowIter.hasNext())
/*      */     {
/* 1672 */       RepositoryRow row = (RepositoryRow)rowIter.next();
/* 1673 */       String dataModel = row.getProperties().getStringValue("PropertyValue");
/* 1674 */       if ("DOD-5015.2 Classified".equalsIgnoreCase(dataModel)) {
/* 1675 */         isDODModel = true;
/*      */       }
/* 1677 */       this.mDODModelCache.put(objectStoreName, Boolean.valueOf(isDODModel));
/*      */     }
/*      */     
/* 1680 */     this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(isDODModel) });
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
/*      */   private void establishJaceSecuritySubject(String serverUrl, Map<String, String> contextMap)
/*      */   {
/* 1693 */     this.mTracer.traceMethodEntry(new Object[] { serverUrl });
/*      */     
/* 1695 */     UserContext userCtx = UserContext.get();
/* 1696 */     if (userCtx.getSubject() == null) {
/* 1697 */       String userName = null;
/* 1698 */       if (!contextMap.containsKey("UserName")) {
/* 1699 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_USERNAME, new Object[0]);
/*      */       }
/*      */       
/* 1702 */       userName = (String)contextMap.get("UserName");
/*      */       
/*      */ 
/* 1705 */       String password = null;
/* 1706 */       if (!contextMap.containsKey("Password")) {
/* 1707 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_PASSWORD, new Object[0]);
/*      */       }
/*      */       
/* 1710 */       password = (String)contextMap.get("Password");
/*      */       
/*      */       try
/*      */       {
/* 1714 */         Connection jaceConn = Factory.Connection.getConnection(serverUrl);
/*      */         
/* 1716 */         String stanza = "FileNetP8";
/* 1717 */         Subject jaceSubject = UserContext.createSubject(jaceConn, userName, password, stanza);
/*      */         
/* 1719 */         userCtx.pushSubject(jaceSubject);
/*      */       } catch (Exception ex) {
/* 1721 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAIL_GENERATE_JACE_SUBJECT, new Object[] { serverUrl, userName, password });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1727 */     this.mTracer.traceMethodExit(new Object[0]);
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
/*      */   private Map<String, Set<DDContainerEntity>> organizeDDContainerBasedOnReviewer(String objectStoreName, Map<String, Set<DDContainerEntity>> containerMap)
/*      */   {
/* 1741 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName });
/*      */     
/* 1743 */     Map<String, Set<DDContainerEntity>> reviewerBasedDDContainerMap = new HashMap();
/*      */     
/*      */ 
/*      */ 
/* 1747 */     if ((containerMap == null) || (containerMap.size() == 0)) {
/* 1748 */       reviewerBasedDDContainerMap = retrieveDDContainersInWholeOS(objectStoreName);
/*      */       
/* 1750 */       this.mTracer.traceMethodExit(new Object[0]);
/* 1751 */       return reviewerBasedDDContainerMap;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1756 */     if (containerMap.containsKey("DDContainers")) {
/* 1757 */       Set<DDContainerEntity> ddContainers = (Set)containerMap.get("DDContainers");
/* 1758 */       insertDDContainerBasedOnReviewer(reviewerBasedDDContainerMap, ddContainers);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1764 */     if (containerMap.containsKey("NonDDContainers")) {
/* 1765 */       Set<DDContainerEntity> nonDDContainers = (Set)containerMap.get("NonDDContainers");
/* 1766 */       Map<String, Set<DDContainerEntity>> ddContainersFromParentMap = retrieveDDCFromParentContainers(objectStoreName, nonDDContainers);
/*      */       
/*      */ 
/* 1769 */       mergeDDContainerReviewerMaps(reviewerBasedDDContainerMap, ddContainersFromParentMap);
/*      */     }
/*      */     
/* 1772 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */     
/* 1774 */     return reviewerBasedDDContainerMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Map<String, Set<DDContainerEntity>> retrieveDDContainersInWholeOS(String objectStoreName)
/*      */   {
/* 1785 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName });
/*      */     
/* 1787 */     String queryStr = QUERY_ALL_DD_CONTAINERS;
/* 1788 */     this.mTracer.traceMinimumMsg("Query to get containers with basic schedule in this FPOS: {0}", new Object[] { queryStr });
/*      */     
/* 1790 */     long startTime = System.currentTimeMillis();
/* 1791 */     RepositoryRowSet resultSet = null;
/*      */     try {
/* 1793 */       resultSet = DDCPUtil.fetchDBRows((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), queryStr, ddContainerPropertyFilter, this.mReadBatchSize, Boolean.TRUE);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1797 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { queryStr });
/*      */     }
/*      */     
/* 1800 */     long endTime = System.currentTimeMillis();
/* 1801 */     this.mTracer.traceMinimumMsg("Time for first page query for containers with basic schedule in this FPOS: {0}", new Object[] { Long.valueOf(endTime - startTime) });
/*      */     
/* 1803 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */     
/* 1805 */     return breakupDDCResultSetOnReviewer(resultSet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Map<String, Set<DDContainerEntity>> retrieveDDCFromParentContainers(String objectStoreName, Set<DDContainerEntity> nonDDContainers)
/*      */   {
/* 1817 */     this.mTracer.traceMethodEntry(new Object[] { objectStoreName, nonDDContainers });
/*      */     
/* 1819 */     Map<String, Set<DDContainerEntity>> reviewerBasedDDContainerMap = new HashMap();
/*      */     
/*      */ 
/* 1822 */     if ((nonDDContainers == null) || (nonDDContainers.size() == 0)) {
/* 1823 */       this.mTracer.traceMethodExit(new Object[0]);
/* 1824 */       return reviewerBasedDDContainerMap;
/*      */     }
/*      */     
/* 1827 */     Map<String, Set<DDContainerEntity>> revieveResultMap = null;
/* 1828 */     for (DDContainerEntity parentContainer : nonDDContainers) {
/* 1829 */       String queryStr = String.format(QUERY_TEMPLATE_DD_CONTAINERS_UNDER_SPECIFIED_CONTAINER, new Object[] { parentContainer.getContainerId() });
/*      */       
/*      */ 
/* 1832 */       this.mTracer.traceMinimumMsg("Query to get containers with basic schedule in specified container: {0}", new Object[] { queryStr });
/*      */       
/* 1834 */       long startTime = System.currentTimeMillis();
/* 1835 */       RepositoryRowSet resultSet = null;
/*      */       try {
/* 1837 */         resultSet = DDCPUtil.fetchDBRows((ObjectStore)this.mJaceObjectStoreCache.get(objectStoreName), queryStr, ddContainerPropertyFilter, this.mReadBatchSize, Boolean.TRUE);
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 1841 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { queryStr });
/*      */       }
/*      */       
/* 1844 */       long endTime = System.currentTimeMillis();
/* 1845 */       this.mTracer.traceMinimumMsg("Time for first page query for containers with basic schedule in specified container: {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*      */       
/* 1847 */       revieveResultMap = breakupDDCResultSetOnReviewer(resultSet);
/* 1848 */       mergeDDContainerReviewerMaps(reviewerBasedDDContainerMap, revieveResultMap);
/*      */     }
/*      */     
/* 1851 */     this.mTracer.traceMethodExit(new Object[] { reviewerBasedDDContainerMap });
/* 1852 */     return reviewerBasedDDContainerMap;
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
/*      */   private Map<String, Set<DDContainerEntity>> breakupDDCResultSetOnReviewer(RepositoryRowSet ddcQueryResultSet)
/*      */   {
/* 1865 */     this.mTracer.traceMethodEntry(new Object[] { ddcQueryResultSet });
/*      */     
/* 1867 */     Map<String, Set<DDContainerEntity>> reveiwerBasedDDCMap = new HashMap();
/*      */     
/*      */ 
/* 1870 */     if ((ddcQueryResultSet != null) && (!ddcQueryResultSet.isEmpty())) {
/* 1871 */       Iterator it = ddcQueryResultSet.iterator();
/* 1872 */       while (it.hasNext()) {
/* 1873 */         RepositoryRow ddContainerRow = (RepositoryRow)it.next();
/*      */         
/* 1875 */         DDContainerEntity ddContainerEntity = getDDContainerEntity(ddContainerRow);
/*      */         
/*      */ 
/* 1878 */         if (this.mHoldValidator.isOnHold(ddContainerEntity.getContainerId(), ddContainerEntity.getPathName(), ddContainerEntity.getFilePlanName()))
/*      */         {
/*      */ 
/* 1881 */           this.mTracer.traceMinimumMsg("The container with basic schedule {0} is on indirect IER hold.", new Object[] { ddContainerEntity.getContainerName() });
/* 1882 */           this.mLogger.info(DDCPLogCode.DDCONTAINER_ON_INDIRECT_IER_HOLD, new Object[] { ddContainerEntity.getContainerName() });
/*      */         } else {
/* 1884 */           String containerReviewer = ddContainerEntity.getReviewer();
/* 1885 */           Set<DDContainerEntity> ddConainerSetForReviewer = (Set)reveiwerBasedDDCMap.get(containerReviewer);
/*      */           
/*      */ 
/* 1888 */           if (ddConainerSetForReviewer == null) {
/* 1889 */             ddConainerSetForReviewer = new HashSet();
/* 1890 */             ddConainerSetForReviewer.add(ddContainerEntity);
/* 1891 */             reveiwerBasedDDCMap.put(containerReviewer, ddConainerSetForReviewer);
/*      */           } else {
/* 1893 */             ddConainerSetForReviewer.add(ddContainerEntity);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1899 */     this.mTracer.traceMethodExit(new Object[] { reveiwerBasedDDCMap });
/*      */     
/* 1901 */     return reveiwerBasedDDCMap;
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
/*      */   private void insertDDContainerBasedOnReviewer(Map<String, Set<DDContainerEntity>> reviewerBasedDDContainerMap, Set<DDContainerEntity> ddContainers)
/*      */   {
/* 1914 */     this.mTracer.traceMethodEntry(new Object[] { ddContainers });
/*      */     
/* 1916 */     if ((ddContainers == null) || (ddContainers.size() == 0)) {
/* 1917 */       return;
/*      */     }
/*      */     
/* 1920 */     for (DDContainerEntity aDDContainer : ddContainers)
/*      */     {
/* 1922 */       if (this.mHoldValidator.isOnHold(aDDContainer.getContainerId(), aDDContainer.getPathName(), aDDContainer.getFilePlanName()))
/*      */       {
/* 1924 */         this.mTracer.traceMinimumMsg("The container with basic schedule {0} is on indirect IER hold", new Object[] { aDDContainer.getContainerName() });
/* 1925 */         this.mLogger.info(DDCPLogCode.DDCONTAINER_ON_INDIRECT_IER_HOLD, new Object[] { aDDContainer.getContainerName() });
/*      */       } else {
/* 1927 */         String reviewerOfDDContainer = aDDContainer.getReviewer();
/* 1928 */         Set<DDContainerEntity> ddContainersForReviewer = (Set)reviewerBasedDDContainerMap.get(reviewerOfDDContainer);
/* 1929 */         if (ddContainersForReviewer == null) {
/* 1930 */           ddContainersForReviewer = new HashSet();
/* 1931 */           ddContainersForReviewer.add(aDDContainer);
/* 1932 */           reviewerBasedDDContainerMap.put(reviewerOfDDContainer, ddContainersForReviewer);
/*      */         } else {
/* 1934 */           ddContainersForReviewer.add(aDDContainer);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1939 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void mergeDDContainerReviewerMaps(Map<String, Set<DDContainerEntity>> targetReviewerBasedDDContainerMap, Map<String, Set<DDContainerEntity>> sourceReviewerBasedDDContainerMap)
/*      */   {
/* 1950 */     this.mTracer.traceMethodEntry(new Object[0]);
/*      */     
/* 1952 */     if ((sourceReviewerBasedDDContainerMap == null) || (sourceReviewerBasedDDContainerMap.size() == 0)) {
/* 1953 */       return;
/*      */     }
/*      */     
/* 1956 */     Set<Map.Entry<String, Set<DDContainerEntity>>> theEntrySet = sourceReviewerBasedDDContainerMap.entrySet();
/*      */     
/* 1958 */     for (Map.Entry<String, Set<DDContainerEntity>> anEntry : theEntrySet) {
/* 1959 */       String aReviewerFromSource = (String)anEntry.getKey();
/* 1960 */       Set<DDContainerEntity> ddContainerSetFromSource = (Set)anEntry.getValue();
/*      */       
/* 1962 */       Set<DDContainerEntity> ddContainersFromTarget = (Set)targetReviewerBasedDDContainerMap.get(aReviewerFromSource);
/* 1963 */       if (ddContainersFromTarget == null) {
/* 1964 */         targetReviewerBasedDDContainerMap.put(aReviewerFromSource, ddContainerSetFromSource);
/*      */       } else {
/* 1966 */         ddContainersFromTarget.addAll(ddContainerSetFromSource);
/*      */       }
/*      */     }
/*      */     
/* 1970 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1977 */     boolean isReportRequired = true;
/* 1978 */     boolean isWorkflowLaunchRequired = false;
/*      */     
/* 1980 */     if (args.length > 0) {
/* 1981 */       if ("w".equalsIgnoreCase(args[0])) {
/* 1982 */         isWorkflowLaunchRequired = true;
/* 1983 */         isReportRequired = false;
/* 1984 */       } else if ("rw".equalsIgnoreCase(args[0])) {
/* 1985 */         isWorkflowLaunchRequired = true;
/* 1986 */         isReportRequired = true;
/*      */       }
/*      */     }
/*      */     
/* 1990 */     java.util.Properties prop = new java.util.Properties();
/*      */     
/*      */     try
/*      */     {
/* 1994 */       prop.load(new java.io.FileInputStream("./config.properties"));
/*      */       
/* 1996 */       String serverUrl = prop.getProperty("CEServerURI");
/* 1997 */       String username = prop.getProperty("UserName");
/* 1998 */       String password = prop.getProperty("Password");
/* 1999 */       String fposName = prop.getProperty("FilePlanObjectStore");
/*      */       
/* 2001 */       Set<String> containerIDSet = new HashSet();
/* 2002 */       String containerIds = prop.getProperty("ContainerIDs");
/* 2003 */       if ((containerIds != null) && (!"".equals(containerIds.trim()))) {
/* 2004 */         String[] containerIdArray = containerIds.split(";", -1);
/* 2005 */         for (String containerId : containerIdArray) {
/* 2006 */           containerIDSet.add(containerId);
/*      */         }
/*      */       }
/*      */       
/* 2010 */       boolean isReportOnly = Boolean.valueOf(prop.getProperty("ReportOnly")).booleanValue();
/* 2011 */       int advanceDays = Integer.valueOf(prop.getProperty("AdvanceDays")).intValue();
/*      */       
/* 2013 */       Map<String, String> configContextMap = new HashMap();
/*      */       
/* 2015 */       configContextMap.put("Uri", serverUrl);
/* 2016 */       configContextMap.put("UserName", username);
/* 2017 */       configContextMap.put("Password", password);
/*      */       
/* 2019 */       configContextMap.put("ThreadCount", prop.getProperty("ThreadCount"));
/* 2020 */       configContextMap.put("ReadBatchSize", prop.getProperty("QueryPageSize"));
/* 2021 */       configContextMap.put("UpdateBatchSize", prop.getProperty("UpdateBatchSize"));
/* 2022 */       configContextMap.put("ContentSizeLimit", prop.getProperty("ContentSizeLimit"));
/* 2023 */       configContextMap.put("LinkCacheSizeLimit", prop.getProperty("LinkCacheSizeLimit"));
/* 2024 */       configContextMap.put("OnHoldContainerCacheSizeLimit", prop.getProperty("OnHoldContainerCacheSizeLimit"));
/*      */       
/* 2026 */       UserContext userCtx = UserContext.get();
/* 2027 */       Connection jaceConn = Factory.Connection.getConnection(serverUrl);
/* 2028 */       String stanza = "FileNetP8";
/* 2029 */       Subject jaceSubject = UserContext.createSubject(jaceConn, username, password, stanza);
/*      */       
/* 2031 */       userCtx.pushSubject(jaceSubject);
/*      */       
/* 2033 */       String logLocation = prop.getProperty("LogLocation");
/*      */       
/* 2035 */       ReportGeneration reportGen = new ReportGeneration(logLocation, null, Level.INFO, BaseTracer.TraceLevel.Minimum);
/*      */       
/* 2037 */       Map<String, String> reveiwerReportIdMap = new HashMap();
/*      */       
/* 2039 */       if (isReportRequired) {
/* 2040 */         reveiwerReportIdMap = reportGen.generateRetentionDueReport(fposName, containerIDSet, advanceDays, isReportOnly, configContextMap);
/*      */         
/* 2042 */         Set<Map.Entry<String, String>> entrySet = reveiwerReportIdMap.entrySet();
/* 2043 */         for (Map.Entry<String, String> entry : entrySet) {
/* 2044 */           String reviewer = (String)entry.getKey();
/* 2045 */           String reportID = (String)entry.getValue();
/* 2046 */           System.out.println("Main: Reviewer : " + reviewer + "\tReport ID : " + reportID);
/*      */         }
/*      */       }
/*      */       
/* 2050 */       if (isWorkflowLaunchRequired) {
/* 2051 */         if (!isReportRequired)
/*      */         {
/* 2053 */           String reportIDs = prop.getProperty("ReportIDs");
/* 2054 */           String[] reportIDArray = null;
/* 2055 */           if ((reportIDs != null) && (!"".equals(reportIDs.trim()))) {
/* 2056 */             reportIDArray = reportIDs.split(";", -1);
/*      */           }
/*      */           
/* 2059 */           String reviewers = prop.getProperty("Reviewers");
/* 2060 */           String[] reviewerArray = null;
/* 2061 */           if ((reviewers != null) && (!"".equals(reviewers.trim()))) {
/* 2062 */             reviewerArray = reviewers.split(";", -1);
/*      */           }
/*      */           
/* 2065 */           if ((reviewerArray == null) || (reportIDArray == null) || (reviewerArray.length != reportIDArray.length)) {
/* 2066 */             System.out.println("Main: no reviewers or no report IDs or reviewers not match report IDs in the config file!");
/* 2067 */             return;
/*      */           }
/*      */           
/* 2070 */           for (int i = 0; i < reviewerArray.length; i++) {
/* 2071 */             reveiwerReportIdMap.put(reviewerArray[i], reportIDArray[i]);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2076 */         if ((reveiwerReportIdMap == null) || (reveiwerReportIdMap.size() == 0)) {
/* 2077 */           System.out.println("Main: there is no report, no workflow will be launched!");
/* 2078 */           return;
/*      */         }
/*      */         
/* 2081 */         String connPointName = prop.getProperty("ConnectionPointName");
/* 2082 */         String wfID = prop.getProperty("DefensibleDisposalWorkflowId");
/*      */         
/* 2084 */         configContextMap.put("AlwaysDeclareRecord", prop.getProperty("AlwaysDeclareRecord"));
/* 2085 */         configContextMap.put("AlwaysShowDeclareResult", prop.getProperty("AlwaysShowDeclareResult"));
/* 2086 */         configContextMap.put("NeedApproval", prop.getProperty("NeedApproval"));
/* 2087 */         configContextMap.put("RecordContainerID", prop.getProperty("RecordContainerID"));
/*      */         
/* 2089 */         reportGen.launchDDWorkflows(fposName, connPointName, wfID, advanceDays, reveiwerReportIdMap, configContextMap);
/*      */       }
/*      */     }
/*      */     catch (Exception ex) {
/* 2093 */       ex.printStackTrace();
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\ReportGeneration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */