/*      */ package com.ibm.ier.ddcp;
/*      */ 
/*      */ import com.filenet.api.collection.ContentElementList;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.constants.AuditLevel;
/*      */ import com.filenet.api.core.ContentTransfer;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.Factory.ObjectStore;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.RepositoryRow;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.filenet.api.util.UserContext;
/*      */ import com.ibm.ier.ddcp.constants.DDCPConstants;
/*      */ import com.ibm.ier.ddcp.constants.ReportStatus;
/*      */ import com.ibm.ier.ddcp.entity.RecordEntity;
/*      */ import com.ibm.ier.ddcp.exception.DDCPErrorCode;
/*      */ import com.ibm.ier.ddcp.exception.DDCPRuntimeException;
/*      */ import com.ibm.ier.ddcp.util.DDCPLString;
/*      */ import com.ibm.ier.ddcp.util.DDCPLogCode;
/*      */ import com.ibm.ier.ddcp.util.DDCPLogger;
/*      */ import com.ibm.ier.ddcp.util.DDCPTracer;
/*      */ import com.ibm.ier.ddcp.util.DDCPUtil;
/*      */ import com.ibm.ier.logtrace.BaseTracer.TraceLevel;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import org.apache.log4j.Level;
/*      */ import org.supercsv.cellprocessor.constraint.NotNull;
/*      */ import org.supercsv.cellprocessor.ift.CellProcessor;
/*      */ import org.supercsv.io.ICsvBeanReader;
/*      */ import org.supercsv.prefs.CsvPreference;
/*      */ 
/*      */ public class DefensibleDestroy
/*      */ {
/*      */   private static final PropertyFilter filePlanPropertyFilter;
/*      */   private static final PropertyFilter recordReceiptsPropertyFilter;
/*      */   private static final PropertyFilter recordedDocumentPropertyFilter;
/*      */   private static final PropertyFilter objectStorePropertyFilter;
/*      */   private static final PropertyFilter reportPropertyFilter;
/*      */   private static final PropertyFilter ddContainerPropertyFilter;
/*      */   private static final String QUERY_TEMPLATE_CHECK_DDCONTAINER_SCHEDULE;
/*      */   private static final String QUERY_CHECK_FPOS_DOD_MODEL;
/*      */   private static final String QUERY_FILEPLAN_RETAINMETADATA;
/*      */   private static final String QUERY_TEMPLATE_SPECIFIED_RECORD_RECEIPTS;
/*      */   private static final String QUERY_TEMPLATE_RECORDED_DOCUMENTS;
/*      */   private static final String[] REPORT_CSV_HEADER;
/*      */   private static final CellProcessor[] CSV_CEELLPROCESSORS;
/*      */   private static final Map<String, Object> UPDATE_PROPS_RESET_REPORT_STATUS;
/*      */   private static final Map<String, Object> UPDATE_PROPS_RECINFO_NULL;
/*      */   private static final Map<String, Object> UPDATE_PROPS_SOFT_DEL_REC;
/*      */   private static final int RECEIPT_STATUS_PARENT_DESTROYED = 3;
/*      */   
/*      */   static
/*      */   {
/*   76 */     StringBuilder sb = new StringBuilder();
/*   77 */     sb.append("ClassificationSchemeName").append(' ');
/*   78 */     sb.append("RetainMetadata").append(' ');
/*   79 */     filePlanPropertyFilter = new PropertyFilter();
/*   80 */     filePlanPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   86 */     recordReceiptsPropertyFilter = new PropertyFilter();
/*   87 */     recordReceiptsPropertyFilter.addIncludeProperty(0, null, null, "Receipts", null);
/*      */     
/*   89 */     recordReceiptsPropertyFilter.addIncludeProperty(1, null, null, "Id", null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   96 */     StringBuilder sb = new StringBuilder();
/*   97 */     sb.append("Id").append(' ');
/*   98 */     sb.append("Name").append(' ');
/*   99 */     recordedDocumentPropertyFilter = new PropertyFilter();
/*  100 */     recordedDocumentPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */     StringBuilder sb = new StringBuilder();
/*  107 */     sb.append("AuditLevel").append(' ');
/*  108 */     sb.append("Domain").append(' ');
/*  109 */     objectStorePropertyFilter = new PropertyFilter();
/*  110 */     objectStorePropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  116 */     StringBuilder sb = new StringBuilder();
/*  117 */     sb.append("ContentSize").append(' ');
/*  118 */     sb.append("ContentElements").append(' ');
/*  119 */     reportPropertyFilter = new PropertyFilter();
/*  120 */     reportPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */     StringBuilder sb = new StringBuilder();
/*  128 */     sb.append("RMRetentionTriggerPropertyName").append(' ');
/*  129 */     sb.append("RMRetentionPeriod").append(' ');
/*      */     
/*      */ 
/*      */ 
/*  133 */     ddContainerPropertyFilter = new PropertyFilter();
/*  134 */     ddContainerPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  142 */     StringBuilder sb = new StringBuilder();
/*  143 */     sb.append("SELECT ");
/*  144 */     sb.append("RMRetentionTriggerPropertyName");
/*  145 */     sb.append(", ").append("RMRetentionPeriod");
/*  146 */     sb.append(" FROM RecordCategory WHERE ");
/*  147 */     sb.append("Id");
/*  148 */     sb.append(" = %s");
/*      */     
/*  150 */     QUERY_TEMPLATE_CHECK_DDCONTAINER_SCHEDULE = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  157 */     StringBuilder sb = new StringBuilder();
/*  158 */     sb.append("SELECT ");
/*  159 */     sb.append("PropertyValue");
/*  160 */     sb.append(" FROM SYSTEMCONFIGURATION WHERE ");
/*  161 */     sb.append("PropertyName");
/*  162 */     sb.append(" = 'FPOS Setup'");
/*      */     
/*  164 */     QUERY_CHECK_FPOS_DOD_MODEL = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  170 */     StringBuilder sb = new StringBuilder();
/*  171 */     sb.append("SELECT ");
/*  172 */     sb.append("ClassificationSchemeName");
/*  173 */     sb.append(", ").append("RetainMetadata");
/*  174 */     sb.append(" FROM ");
/*  175 */     sb.append("ClassificationScheme");
/*      */     
/*  177 */     QUERY_FILEPLAN_RETAINMETADATA = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  183 */     StringBuilder sb = new StringBuilder();
/*  184 */     sb.append("SELECT ");
/*  185 */     sb.append("r.").append("Id");
/*  186 */     sb.append(", r.").append("Receipts");
/*  187 */     sb.append(" FROM ElectronicRecordInfo r WHERE r.Id IN (%s) AND r.OnHold = FALSE  AND r.IsDeleted = FALSE");
/*      */     
/*  189 */     QUERY_TEMPLATE_SPECIFIED_RECORD_RECEIPTS = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  196 */     StringBuilder sb = new StringBuilder();
/*  197 */     sb.append("SELECT ");
/*  198 */     sb.append("r.").append("Id");
/*  199 */     sb.append(",r.").append("OnHold");
/*  200 */     sb.append(",r.").append("IsDeleted");
/*  201 */     sb.append(", r.").append("RecordedDocuments");
/*      */     
/*  203 */     sb.append(" FROM ElectronicRecordInfo r WHERE r.Id IN (%s)");
/*      */     
/*  205 */     QUERY_TEMPLATE_RECORDED_DOCUMENTS = sb.toString();
/*      */     
/*      */ 
/*      */ 
/*  209 */     REPORT_CSV_HEADER = new String[] { "RecordName", null, null, "ParentPath", "FilePlan", "RetentionTriggerName", null, "RetentionPeriod", "RecordID", "ParentContainerID" };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  214 */     CSV_CEELLPROCESSORS = new CellProcessor[] { new NotNull(), null, null, new NotNull(), new NotNull(), new NotNull(), null, new NotNull(), new NotNull(), new NotNull() };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  221 */     UPDATE_PROPS_RESET_REPORT_STATUS = new HashMap(1);
/*  222 */     UPDATE_PROPS_RESET_REPORT_STATUS.put("CurrentPhaseExecutionStatus", Integer.valueOf(ReportStatus.NotInReport.getIntValue()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  230 */     UPDATE_PROPS_RECINFO_NULL = new HashMap(1);
/*  231 */     UPDATE_PROPS_RECINFO_NULL.put("RecordInformation", (com.filenet.api.core.EngineObject)null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  237 */     UPDATE_PROPS_SOFT_DEL_REC = new HashMap(3);
/*  238 */     UPDATE_PROPS_SOFT_DEL_REC.put("PreventRMEntityDeletion", (String)null);
/*  239 */     UPDATE_PROPS_SOFT_DEL_REC.put("IsDeleted", Boolean.valueOf(true));
/*  240 */     UPDATE_PROPS_SOFT_DEL_REC.put("DocURI", "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  245 */   private DDCPLogger mLogger = null;
/*  246 */   private DDCPTracer mTracer = null;
/*      */   
/*  248 */   private Domain mJaceDomain = null;
/*      */   
/*  250 */   private ObjectStore mJaceFPOS = null;
/*      */   
/*  252 */   private boolean mIsAuditOn = false;
/*      */   
/*  254 */   private boolean mIsDODModel = false;
/*      */   
/*      */ 
/*  257 */   private Map<String, Boolean> mFilePlanRetainMetaDataCacheMap = new HashMap();
/*      */   
/*      */ 
/*  260 */   private Map<String, Map<ObjectStore, Boolean>> mROSCache = new HashMap();
/*      */   
/*      */ 
/*  263 */   private Map<String, BatchProcessingManager> mROSBatchProcessingMgrCache = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  268 */   private Map<String, Map<String, List<String>>> mDDContainerScheduleChangeCache = new HashMap();
/*      */   
/*  270 */   private OnHoldValidator mHoldValidator = null;
/*  271 */   private LinkDetector mLinkDetector = null;
/*      */   
/*  273 */   private BatchProcessingManager mBatchProcessingMgrFPOS = null;
/*      */   
/*      */ 
/*  276 */   private int mThreadCount = 1;
/*  277 */   private int mReadBatchSize = 10000;
/*  278 */   private int mUpdateBatchSize = 1000;
/*  279 */   private int mContentSizeLimit = 200000;
/*  280 */   private int mOnHoldContainerCacheSizeLimit = 200000;
/*  281 */   private int mLinkCacheSizeLimit = 100000;
/*      */   
/*  283 */   private int mToBeDeletedRMLinkSizeLimit = 100000;
/*  284 */   private int mToBeUpdatedReceiptsSizeLimit = 100000;
/*      */   
/*  286 */   private String mReviewer = null;
/*      */   
/*      */ 
/*  289 */   private Document mSuccessReport = null;
/*  290 */   private StringBuilder mSuccessReportBuilder = new StringBuilder();
/*  291 */   private boolean mHasHeaderIndicatorForSucRep = false;
/*  292 */   private int mCurrentContentSizeCountForSucRep = 0;
/*      */   
/*      */ 
/*  295 */   private Document mFailureReport = null;
/*  296 */   private StringBuilder mFailureReportBuilder = new StringBuilder();
/*  297 */   private boolean mHasHeaderIndicatorForFailRep = false;
/*  298 */   private int mCurrentContentSizeCountForFailRep = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DefensibleDestroy(Domain jaceDomain, String objectStoreName, String reviewer, Map<String, String> configContextMap, String logLocation, String taskName, Level logLevel, BaseTracer.TraceLevel traceLevel)
/*      */   {
/*  315 */     this.mLogger = DDCPLogger.getDDCPLogger(DefensibleDestroy.class.getName(), logLocation, taskName, logLevel);
/*  316 */     this.mTracer = DDCPTracer.getDDCPTracer(com.ibm.ier.ddcp.util.DDCPTracer.SubSystem.Delete, logLocation, taskName, traceLevel);
/*      */     
/*  318 */     this.mTracer.traceMinimumMsg("---- Start initialization for destroy ----", new Object[0]);
/*  319 */     this.mLogger.info(DDCPLogCode.START_INIT_DESTROY, new Object[] { "----", "----" });
/*      */     
/*  321 */     this.mReviewer = reviewer;
/*  322 */     this.mJaceDomain = jaceDomain;
/*      */     try
/*      */     {
/*  325 */       this.mJaceFPOS = Factory.ObjectStore.fetchInstance(this.mJaceDomain, objectStoreName, objectStorePropertyFilter);
/*      */     } catch (Exception ex) {
/*  327 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_FETCH_OBJECTSTORE, new Object[] { objectStoreName });
/*      */     }
/*      */     
/*  330 */     if (this.mJaceFPOS == null) {
/*  331 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.FAILED_TO_FETCH_OBJECTSTORE, new Object[] { objectStoreName });
/*      */     }
/*      */     
/*  334 */     if (this.mJaceFPOS.get_AuditLevel() == AuditLevel.ENABLED) {
/*  335 */       this.mIsAuditOn = true;
/*      */     }
/*      */     
/*  338 */     verifyFPOSGetDataModel(this.mJaceFPOS);
/*  339 */     cacheFilePlanRetainMetaData(this.mJaceFPOS);
/*      */     
/*  341 */     this.mTracer.traceMinimumMsg("Object store name: {0}, reviewer: {1}, audit: {2}, DOD model: {3}", new Object[] { objectStoreName, this.mReviewer, Boolean.valueOf(this.mIsAuditOn), Boolean.valueOf(this.mIsDODModel) });
/*      */     
/*  343 */     this.mLogger.info(DDCPLogCode.DELETE_BASIC_INFO, new Object[] { objectStoreName, this.mReviewer, Boolean.valueOf(this.mIsAuditOn), Boolean.valueOf(this.mIsDODModel) });
/*      */     
/*      */ 
/*  346 */     validatePerformanceTuningParams(configContextMap);
/*      */     
/*  348 */     this.mTracer.traceMinimumMsg("---- End initialization for destroy ----\n", new Object[0]);
/*  349 */     this.mLogger.info(DDCPLogCode.END_INIT_DESTROY, new Object[] { "----", "----\n" });
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
/*      */   public String[] destroyRecordInReport(String retentionDueReportId)
/*      */     throws Exception
/*      */   {
/*  368 */     this.mTracer.traceMethodEntry(new Object[] { retentionDueReportId });
/*      */     
/*  370 */     this.mTracer.traceMinimumMsg("---- Start destroy records from report {0} for reviewer {1} ----", new Object[] { retentionDueReportId, this.mReviewer });
/*  371 */     this.mLogger.info(DDCPLogCode.START_DESTROY, new Object[] { "----", retentionDueReportId, this.mReviewer, "----" });
/*      */     
/*  373 */     long startTimeSpot = System.currentTimeMillis();
/*      */     
/*      */ 
/*  376 */     ContentElementList reportContentList = readDeletionReport(retentionDueReportId);
/*      */     
/*  378 */     if (reportContentList.size() <= 0) {
/*  379 */       this.mTracer.traceMinimumMsg("Report content is empty, no destroy is needed.", new Object[0]);
/*  380 */       this.mLogger.info(DDCPLogCode.REPORT_CONTENT_EMPTY, new Object[0]);
/*      */       
/*  382 */       this.mTracer.traceMinimumMsg("---- End destroy records from report {0} for reviewer {1} ----\n\n", new Object[] { retentionDueReportId, this.mReviewer });
/*  383 */       this.mLogger.info(DDCPLogCode.END_DESTROY, new Object[] { "----", retentionDueReportId, this.mReviewer, "----\n\n" });
/*      */       
/*  385 */       this.mTracer.traceMethodExit(new Object[0]);
/*  386 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  390 */     this.mHoldValidator = new OnHoldValidator(this.mJaceFPOS, this.mReadBatchSize, this.mOnHoldContainerCacheSizeLimit);
/*      */     
/*      */ 
/*  393 */     this.mBatchProcessingMgrFPOS = new BatchProcessingManager(this.mJaceFPOS, this.mThreadCount, this.mIsAuditOn, this.mReviewer);
/*      */     
/*      */ 
/*      */ 
/*  397 */     this.mLinkDetector = new LinkDetector(this.mJaceFPOS, this.mReadBatchSize, this.mLinkCacheSizeLimit);
/*      */     
/*      */ 
/*  400 */     resetVarsForReports();
/*      */     
/*  402 */     long initTimeSpot = System.currentTimeMillis();
/*  403 */     this.mTracer.traceMinimumMsg("Time for initiation: {0} ms.", new Object[] { Long.valueOf(initTimeSpot - startTimeSpot) });
/*      */     
/*  405 */     String theFailureReportId = null;
/*      */     
/*  407 */     String theSuccessReportId = null;
/*      */     
/*  409 */     List<File> tempFileList = new ArrayList();
/*      */     try
/*      */     {
/*  412 */       int totalRecCount = 0;
/*  413 */       int totalFailedRecCount = 0;
/*  414 */       int totalDeletedRecCount = 0;
/*      */       
/*      */ 
/*  417 */       Map<RecordEntity, List<String>> failedRecordSeverityReasonMap = new HashMap();
/*      */       
/*      */ 
/*      */ 
/*  421 */       Map<RecordEntity, List<String>> notFoundRecordSeverityReasonMap = new HashMap();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  426 */       Map<String, RecordEntity> toBeHardDeletedRecIdEntityMap = new HashMap();
/*      */       
/*      */ 
/*      */ 
/*  430 */       Map<String, RecordEntity> toBeSoftDeletedRecIdEntityMap = new HashMap();
/*      */       
/*      */ 
/*      */ 
/*  434 */       Set<String> allRelatedRMLinkIdSet = new HashSet();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  439 */       Map<String, Set<String>> allRecIdReceiptIdSetMapNoRetainMetaData = new HashMap();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  444 */       Map<String, Set<String>> allRecIdReceiptIdSetMapWithRetainMetaData = new HashMap();
/*      */       
/*  446 */       Iterator reportContentListIter = reportContentList.iterator();
/*  447 */       while (reportContentListIter.hasNext()) {
/*  448 */         ContentTransfer ct = (ContentTransfer)reportContentListIter.next();
/*      */         
/*  450 */         this.mTracer.traceMinimumMsg("Element Sequence number: {0}, content type: {1}, content size: {2}", new Object[] { Integer.valueOf(ct.get_ElementSequenceNumber().intValue()), ct.get_ContentType(), ct.get_ContentSize() });
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  455 */         File tempFile = null;
/*      */         
/*  457 */         ReadableByteChannel rbc = null;
/*  458 */         FileOutputStream fos = null;
/*      */         try
/*      */         {
/*  461 */           java.io.InputStream contentStream = ct.accessContentStream();
/*  462 */           rbc = java.nio.channels.Channels.newChannel(contentStream);
/*      */           
/*  464 */           tempFile = File.createTempFile("DDTemp", Long.toString(System.nanoTime()));
/*  465 */           tempFileList.add(tempFile);
/*      */           
/*  467 */           fos = new FileOutputStream(tempFile);
/*  468 */           this.mTracer.traceMinimumMsg("Start downloading the content from CE ...", new Object[0]);
/*  469 */           long startTime = System.currentTimeMillis();
/*  470 */           fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
/*  471 */           long endTime = System.currentTimeMillis();
/*  472 */           this.mTracer.traceMinimumMsg("Total time of download: {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*  473 */           this.mTracer.traceMinimumMsg("Temp file path: {0}", new Object[] { tempFile.getAbsolutePath() });
/*      */         } finally {
/*  475 */           if (rbc != null) {
/*  476 */             rbc.close();
/*      */           }
/*  478 */           if (fos != null) {
/*  479 */             fos.close();
/*      */           }
/*      */         }
/*      */         
/*  483 */         ICsvBeanReader csvBeanReader = null;
/*      */         try {
/*  485 */           InputStreamReader rd = new InputStreamReader(new FileInputStream(tempFile), "UTF-8");
/*      */           
/*  487 */           csvBeanReader = new org.supercsv.io.CsvBeanReader(rd, CsvPreference.STANDARD_PREFERENCE);
/*      */           
/*  489 */           csvBeanReader.getHeader(true);
/*      */           
/*  491 */           RecordEntity aRecEntity = null;
/*      */           
/*  493 */           while ((aRecEntity = (RecordEntity)csvBeanReader.read(RecordEntity.class, REPORT_CSV_HEADER, CSV_CEELLPROCESSORS)) != null) {
/*  494 */             totalRecCount++;
/*      */             
/*      */ 
/*  497 */             if (isScheduleChangedInDDContainer(this.mJaceFPOS, aRecEntity.getParentContainerID(), aRecEntity.getRetentionTriggerName(), aRecEntity.getRetentionPeriod()))
/*      */             {
/*      */ 
/*      */ 
/*  501 */               totalFailedRecCount++;
/*      */               
/*  503 */               Map<String, List<String>> failureMap = (Map)this.mDDContainerScheduleChangeCache.get(aRecEntity.getParentContainerID());
/*      */               
/*      */ 
/*  506 */               if ((failureMap != null) && (failureMap.containsKey("DDContainerScheduleChanged"))) {
/*  507 */                 failedRecordSeverityReasonMap.put(aRecEntity, failureMap.get("DDContainerScheduleChanged"));
/*      */               }
/*  509 */               else if ((failureMap != null) && (failureMap.containsKey("DDContainerNotExit")))
/*      */               {
/*  511 */                 notFoundRecordSeverityReasonMap.put(aRecEntity, failureMap.get("DDContainerNotExit"));
/*      */               }
/*      */             }
/*  514 */             else if (this.mHoldValidator.isOnHold(aRecEntity.getParentContainerID(), aRecEntity.getParentPath(), aRecEntity.getFilePlan()))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  520 */               totalFailedRecCount++;
/*      */               
/*  522 */               failedRecordSeverityReasonMap.put(aRecEntity, DDCPConstants.SEVERITY_REASON_LIST_PARENT_ON_HOLD);
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  527 */               Boolean isRetainMetaDataEnabled = (Boolean)this.mFilePlanRetainMetaDataCacheMap.get(aRecEntity.getFilePlan());
/*      */               
/*      */ 
/*      */ 
/*  531 */               if ((isRetainMetaDataEnabled != null) && (!isRetainMetaDataEnabled.booleanValue())) {
/*  532 */                 toBeHardDeletedRecIdEntityMap.put(aRecEntity.getRecordID(), aRecEntity);
/*  533 */                 int toBeHardDeletedRecCount = toBeHardDeletedRecIdEntityMap.size();
/*  534 */                 if (toBeHardDeletedRecCount >= this.mUpdateBatchSize) {
/*  535 */                   int deletedRecCount = startHardDeleteRecords(toBeHardDeletedRecIdEntityMap, failedRecordSeverityReasonMap, notFoundRecordSeverityReasonMap, allRecIdReceiptIdSetMapNoRetainMetaData, allRelatedRMLinkIdSet);
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  543 */                   toBeHardDeletedRecIdEntityMap = new HashMap();
/*      */                   
/*  545 */                   totalDeletedRecCount += deletedRecCount;
/*  546 */                   totalFailedRecCount += toBeHardDeletedRecCount - deletedRecCount;
/*      */                   
/*  548 */                   this.mTracer.traceMinimumMsg("\nAlready have deleted {0} records and cannot deleted {1} records.\n", new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */                   
/*  550 */                   this.mLogger.info(DDCPLogCode.DELETE_PROGRSESS, new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */                 }
/*      */               }
/*      */               else {
/*  554 */                 toBeSoftDeletedRecIdEntityMap.put(aRecEntity.getRecordID(), aRecEntity);
/*  555 */                 int toBeSoftDeletedRecCount = toBeSoftDeletedRecIdEntityMap.size();
/*  556 */                 if (toBeSoftDeletedRecCount >= this.mUpdateBatchSize) {
/*  557 */                   int deletedRecCount = startSoftDeleteRecords(toBeSoftDeletedRecIdEntityMap, failedRecordSeverityReasonMap, notFoundRecordSeverityReasonMap, allRecIdReceiptIdSetMapWithRetainMetaData, allRelatedRMLinkIdSet);
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  565 */                   toBeSoftDeletedRecIdEntityMap = new HashMap();
/*      */                   
/*  567 */                   totalDeletedRecCount += deletedRecCount;
/*  568 */                   totalFailedRecCount += toBeSoftDeletedRecCount - deletedRecCount;
/*      */                   
/*  570 */                   this.mTracer.traceMinimumMsg("\nAlready have deleted {0} records and cannot deleted {1} records.\n", new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */                   
/*  572 */                   this.mLogger.info(DDCPLogCode.DELETE_PROGRSESS, new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  579 */             if (failedRecordSeverityReasonMap.size() >= this.mUpdateBatchSize)
/*      */             {
/*  581 */               writeFailedRecsInfo(this.mJaceFPOS, failedRecordSeverityReasonMap);
/*      */               
/*      */ 
/*  584 */               resetFailedRecsInReportStatus(failedRecordSeverityReasonMap);
/*      */               
/*      */ 
/*      */ 
/*  588 */               failedRecordSeverityReasonMap = new HashMap();
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  593 */             if (notFoundRecordSeverityReasonMap.size() >= this.mUpdateBatchSize)
/*      */             {
/*  595 */               writeFailedRecsInfo(this.mJaceFPOS, notFoundRecordSeverityReasonMap);
/*      */               
/*      */ 
/*  598 */               notFoundRecordSeverityReasonMap = new HashMap();
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  604 */             if (allRecIdReceiptIdSetMapNoRetainMetaData.size() >= this.mToBeUpdatedReceiptsSizeLimit) {
/*  605 */               updateReceipts(allRecIdReceiptIdSetMapNoRetainMetaData, false);
/*      */               
/*      */ 
/*  608 */               allRecIdReceiptIdSetMapNoRetainMetaData = new HashMap();
/*      */             }
/*      */             
/*      */ 
/*  612 */             if (allRecIdReceiptIdSetMapWithRetainMetaData.size() > 0) {
/*  613 */               updateReceipts(allRecIdReceiptIdSetMapWithRetainMetaData, true);
/*      */               
/*      */ 
/*  616 */               allRecIdReceiptIdSetMapWithRetainMetaData = new HashMap();
/*      */             }
/*      */             
/*      */ 
/*  620 */             if (allRelatedRMLinkIdSet.size() >= this.mToBeDeletedRMLinkSizeLimit) {
/*  621 */               deleteRelatedRMLinks(allRelatedRMLinkIdSet);
/*      */               
/*      */ 
/*  624 */               allRelatedRMLinkIdSet = new HashSet();
/*      */             }
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/*  630 */           if (csvBeanReader != null) {
/*  631 */             csvBeanReader.close();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  637 */       int toBeHardDeletedRecCount = toBeHardDeletedRecIdEntityMap.size();
/*  638 */       if (toBeHardDeletedRecCount > 0) {
/*  639 */         int deletedRecCount = startHardDeleteRecords(toBeHardDeletedRecIdEntityMap, failedRecordSeverityReasonMap, notFoundRecordSeverityReasonMap, allRecIdReceiptIdSetMapNoRetainMetaData, allRelatedRMLinkIdSet);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  646 */         totalDeletedRecCount += deletedRecCount;
/*  647 */         totalFailedRecCount += toBeHardDeletedRecCount - deletedRecCount;
/*      */         
/*  649 */         this.mTracer.traceMinimumMsg("\nAlready have deleted {0} records and cannot deleted {1} records.\n", new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */         
/*  651 */         this.mLogger.info(DDCPLogCode.DELETE_PROGRSESS, new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  656 */       int toBeSoftDeletedRecCount = toBeSoftDeletedRecIdEntityMap.size();
/*  657 */       if (toBeSoftDeletedRecCount > 0) {
/*  658 */         int deletedRecCount = startSoftDeleteRecords(toBeSoftDeletedRecIdEntityMap, failedRecordSeverityReasonMap, notFoundRecordSeverityReasonMap, allRecIdReceiptIdSetMapWithRetainMetaData, allRelatedRMLinkIdSet);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  666 */         totalDeletedRecCount += deletedRecCount;
/*  667 */         totalFailedRecCount += toBeSoftDeletedRecCount - deletedRecCount;
/*      */         
/*  669 */         this.mTracer.traceMinimumMsg("\nAlready have deleted {0} records and cannot deleted {1} records.\n", new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */         
/*  671 */         this.mLogger.info(DDCPLogCode.DELETE_PROGRSESS, new Object[] { Integer.valueOf(totalDeletedRecCount), Integer.valueOf(totalFailedRecCount) });
/*      */       }
/*      */       
/*      */ 
/*  675 */       if (failedRecordSeverityReasonMap.size() > 0)
/*      */       {
/*  677 */         writeFailedRecsInfo(this.mJaceFPOS, failedRecordSeverityReasonMap);
/*      */         
/*      */ 
/*  680 */         resetFailedRecsInReportStatus(failedRecordSeverityReasonMap);
/*      */       }
/*      */       
/*      */ 
/*  684 */       if (notFoundRecordSeverityReasonMap.size() > 0)
/*      */       {
/*  686 */         writeFailedRecsInfo(this.mJaceFPOS, notFoundRecordSeverityReasonMap);
/*      */       }
/*      */       
/*      */ 
/*  690 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/*      */ 
/*  693 */       if ((this.mFailureReport == null) && (this.mFailureReportBuilder.length() == 0)) {
/*  694 */         this.mFailureReportBuilder.append(DDCPConstants.HEADLINE_FAILURE_REPORT);
/*      */       }
/*      */       
/*      */ 
/*  698 */       if ((this.mSuccessReport == null) && (this.mSuccessReportBuilder.length() == 0)) {
/*  699 */         this.mSuccessReportBuilder.append(DDCPConstants.HEADLINE_SUCCESS_REPORT);
/*      */       }
/*      */       
/*      */ 
/*  703 */       if (this.mFailureReportBuilder.length() > 0) {
/*  704 */         String docName = DDCPUtil.generateReportName("NotDeletedRecReport_", 0);
/*      */         
/*  706 */         this.mFailureReport = DDCPUtil.saveReport(this.mJaceFPOS, this.mFailureReport, this.mFailureReportBuilder, docName);
/*      */       }
/*      */       
/*      */ 
/*  710 */       if (this.mSuccessReportBuilder.length() > 0) {
/*  711 */         String docName = DDCPUtil.generateReportName("DeletedRecReport_", 0);
/*      */         
/*  713 */         this.mSuccessReport = DDCPUtil.saveReport(this.mJaceFPOS, this.mSuccessReport, this.mSuccessReportBuilder, docName);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  718 */       if (this.mFailureReport != null) {
/*  719 */         theFailureReportId = DDCPUtil.checkinReport(this.mFailureReport);
/*      */       }
/*      */       
/*      */ 
/*  723 */       if (this.mSuccessReport != null) {
/*  724 */         theSuccessReportId = DDCPUtil.checkinReport(this.mSuccessReport);
/*      */       }
/*      */       
/*  727 */       long timeSpot1 = System.currentTimeMillis();
/*      */       
/*  729 */       this.mTracer.traceMinimumMsg("Time to save the rest contents and check in both reports: {0} ms.", new Object[] { Long.valueOf(timeSpot1 - timeSpot0) });
/*      */       
/*      */ 
/*  732 */       if (theFailureReportId != null) {
/*  733 */         this.mTracer.traceMinimumMsg("In failure report, number of document content elements: " + this.mFailureReport.get_ContentElements().size() + "\ttotal size of content: " + this.mFailureReport.get_ContentSize(), new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  738 */       if (theSuccessReportId != null) {
/*  739 */         this.mTracer.traceMinimumMsg("In success report, number of document content elements: " + this.mSuccessReport.get_ContentElements().size() + "\ttotal size of content: " + this.mSuccessReport.get_ContentSize(), new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  745 */       if (allRecIdReceiptIdSetMapNoRetainMetaData.size() > 0) {
/*  746 */         updateReceipts(allRecIdReceiptIdSetMapNoRetainMetaData, false);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  751 */       if (allRecIdReceiptIdSetMapWithRetainMetaData.size() > 0) {
/*  752 */         updateReceipts(allRecIdReceiptIdSetMapWithRetainMetaData, true);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  757 */       if (allRelatedRMLinkIdSet.size() > 0) {
/*  758 */         deleteRelatedRMLinks(allRelatedRMLinkIdSet);
/*      */       }
/*      */       
/*      */ 
/*  762 */       long endTimeSpot = System.currentTimeMillis();
/*  763 */       float durationForDeletion = (float)(endTimeSpot - startTimeSpot) / 1000.0F;
/*  764 */       float throughput = totalRecCount / durationForDeletion;
/*  765 */       this.mTracer.traceMinimumMsg("Total records being successfully deleted: {0}.", new Object[] { Integer.valueOf(totalDeletedRecCount) });
/*  766 */       this.mLogger.info(DDCPLogCode.TOTAL_RECORDS_DELETED, new Object[] { Integer.valueOf(totalDeletedRecCount) });
/*  767 */       this.mTracer.traceMinimumMsg("Total records cannot be deleted: {0}.", new Object[] { Integer.valueOf(totalFailedRecCount) });
/*  768 */       this.mLogger.info(DDCPLogCode.TOTAL_RECORDS_NOT_DELETED, new Object[] { Integer.valueOf(totalFailedRecCount) });
/*  769 */       this.mTracer.traceMinimumMsg("Total records being processed: {0}, total time: {1} sec, throughput: {2} rec/sec.\n", new Object[] { Integer.valueOf(totalRecCount), Float.valueOf(durationForDeletion), Float.valueOf(throughput) });
/*      */       
/*      */ 
/*  772 */       this.mLogger.info(DDCPLogCode.TOTAL_DESTROY_PERFORMANCE, new Object[] { Integer.valueOf(totalRecCount), Float.valueOf(durationForDeletion), Float.valueOf(throughput) });
/*      */       
/*  774 */       this.mTracer.traceMinimumMsg("Deleted record report ID : {0}", new Object[] { theSuccessReportId });
/*  775 */       this.mLogger.info(DDCPLogCode.SUCCESS_REPORT_ID, new Object[] { theSuccessReportId });
/*  776 */       this.mTracer.traceMinimumMsg("Not deleted record report ID : {0}", new Object[] { theFailureReportId });
/*  777 */       this.mLogger.info(DDCPLogCode.FAILURE_REPORT_ID, new Object[] { theFailureReportId });
/*      */     }
/*      */     catch (DDCPRuntimeException ddcpEx) {
/*  780 */       throw ddcpEx;
/*      */     } catch (Exception generalEx) {
/*  782 */       this.mTracer.traceMinimumMsg("Caught unprocessed exception: {0}", new Object[] { generalEx.getMessage() });
/*  783 */       this.mLogger.error(DDCPLogCode.UNPROCESSED_EXCEPTION, generalEx, new Object[0]);
/*  784 */       throw DDCPRuntimeException.createDDCPRuntimeException(generalEx, DDCPErrorCode.UNEXPECTED_ERROR, new Object[0]);
/*      */     } finally {
/*  786 */       this.mTracer.traceMinimumMsg("---- End destroy records from report {0} for reviewer {1} ----\n\n", new Object[] { retentionDueReportId, this.mReviewer });
/*  787 */       this.mLogger.info(DDCPLogCode.END_DESTROY, new Object[] { "----", retentionDueReportId, this.mReviewer, "----\n\n" });
/*      */       
/*      */ 
/*  790 */       if (this.mBatchProcessingMgrFPOS != null) {
/*  791 */         this.mBatchProcessingMgrFPOS.stop();
/*      */       }
/*      */       
/*  794 */       if (!this.mROSBatchProcessingMgrCache.isEmpty()) {
/*  795 */         Collection<BatchProcessingManager> batchProcessingMgrsForRos = this.mROSBatchProcessingMgrCache.values();
/*  796 */         for (BatchProcessingManager batchProcessingMgr : batchProcessingMgrsForRos) {
/*  797 */           batchProcessingMgr.stop();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  802 */       if ((tempFileList != null) && (tempFileList.size() > 0)) {
/*  803 */         this.mTracer.traceMinimumMsg("Deleting {0} temp files...", new Object[] { Integer.valueOf(tempFileList.size()) });
/*  804 */         for (File aTempFile : tempFileList) {
/*  805 */           aTempFile.delete();
/*      */         }
/*  807 */         this.mTracer.traceMinimumMsg("Done.", new Object[0]);
/*      */       }
/*      */     }
/*      */     
/*  811 */     this.mTracer.traceMethodExit(new Object[] { theSuccessReportId, theFailureReportId });
/*      */     
/*  813 */     String[] result = { theSuccessReportId, theFailureReportId };
/*  814 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resetVarsForReports()
/*      */   {
/*  822 */     this.mSuccessReport = null;
/*  823 */     this.mSuccessReportBuilder = new StringBuilder();
/*  824 */     this.mHasHeaderIndicatorForSucRep = false;
/*  825 */     this.mCurrentContentSizeCountForSucRep = 0;
/*      */     
/*      */ 
/*  828 */     this.mFailureReport = null;
/*  829 */     this.mFailureReportBuilder = new StringBuilder();
/*  830 */     this.mHasHeaderIndicatorForFailRep = false;
/*  831 */     this.mCurrentContentSizeCountForFailRep = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ContentElementList readDeletionReport(String retentionDueReportId)
/*      */   {
/*  841 */     this.mTracer.traceMethodEntry(new Object[] { retentionDueReportId });
/*  842 */     Document report = null;
/*  843 */     long timeSpot1 = System.currentTimeMillis();
/*      */     try {
/*  845 */       report = com.filenet.api.core.Factory.Document.fetchInstance(this.mJaceFPOS, retentionDueReportId, reportPropertyFilter);
/*      */     } catch (Exception ex) {
/*  847 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_GET_REPORT, new Object[] { retentionDueReportId });
/*      */     }
/*      */     
/*  850 */     long timeSpot2 = System.currentTimeMillis();
/*  851 */     this.mTracer.traceMinimumMsg("Time to fetch the report : {0} ms.", new Object[] { Long.valueOf(timeSpot2 - timeSpot1) });
/*      */     
/*      */ 
/*  854 */     this.mTracer.traceMinimumMsg("Number of report content elements: {0}, total size of content: {1}.", new Object[] { Integer.valueOf(report.get_ContentElements().size()), report.get_ContentSize() });
/*      */     
/*  856 */     this.mLogger.info(DDCPLogCode.REPORT_CONTENT_INFO, new Object[] { Integer.valueOf(report.get_ContentElements().size()), report.get_ContentSize() });
/*      */     
/*      */ 
/*      */ 
/*  860 */     ContentElementList reportContentList = report.get_ContentElements();
/*  861 */     this.mTracer.traceMethodExit(new Object[] { reportContentList });
/*  862 */     return reportContentList;
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
/*      */   private Document writeDeletedRecsInfo(ObjectStore jaceOS, Map<String, RecordEntity> recIdEntityMap, Set<String> deletedRecIdSet)
/*      */   {
/*  877 */     this.mTracer.traceMethodEntry(new Object[] { recIdEntityMap, deletedRecIdSet });
/*      */     
/*      */ 
/*  880 */     if ((deletedRecIdSet == null) || (deletedRecIdSet.isEmpty())) {
/*  881 */       return this.mSuccessReport;
/*      */     }
/*      */     
/*  884 */     for (String deletedRecId : deletedRecIdSet) {
/*  885 */       RecordEntity deletedRecEntity = (RecordEntity)recIdEntityMap.get(deletedRecId);
/*      */       
/*      */ 
/*  888 */       if (!this.mHasHeaderIndicatorForSucRep) {
/*  889 */         this.mSuccessReportBuilder.append(DDCPConstants.HEADLINE_SUCCESS_REPORT);
/*      */         
/*  891 */         this.mHasHeaderIndicatorForSucRep = true;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  900 */       String recName = deletedRecEntity.getRecordName();
/*  901 */       recName = DDCPUtil.sterilizeStringForCSV(recName);
/*  902 */       this.mSuccessReportBuilder.append("\"").append(recName).append("\"");
/*      */       
/*  904 */       String parentPath = deletedRecEntity.getParentPath();
/*  905 */       parentPath = DDCPUtil.sterilizeStringForCSV(parentPath);
/*  906 */       this.mSuccessReportBuilder.append(",\"").append(parentPath).append("\"");
/*      */       
/*  908 */       String filePlan = deletedRecEntity.getFilePlan();
/*  909 */       filePlan = DDCPUtil.sterilizeStringForCSV(filePlan);
/*  910 */       this.mSuccessReportBuilder.append(",\"").append(filePlan).append("\"");
/*      */       
/*  912 */       String recId = deletedRecEntity.getRecordID();
/*  913 */       this.mSuccessReportBuilder.append(",").append(recId);
/*      */       
/*  915 */       this.mSuccessReportBuilder.append("\n");
/*      */       
/*  917 */       this.mCurrentContentSizeCountForSucRep += 1;
/*  918 */       if (this.mCurrentContentSizeCountForSucRep == this.mContentSizeLimit) {
/*  919 */         long timeSpot0 = System.currentTimeMillis();
/*  920 */         String successReportName = DDCPUtil.generateReportName("DeletedRecReport_", 0);
/*      */         
/*  922 */         this.mSuccessReport = DDCPUtil.saveReport(jaceOS, this.mSuccessReport, this.mSuccessReportBuilder, successReportName);
/*      */         
/*      */ 
/*  925 */         this.mSuccessReportBuilder.setLength(0);
/*      */         
/*      */ 
/*  928 */         this.mHasHeaderIndicatorForSucRep = false;
/*      */         
/*  930 */         this.mCurrentContentSizeCountForSucRep = 0;
/*      */         
/*  932 */         long timeSpot1 = System.currentTimeMillis();
/*  933 */         this.mTracer.traceMinimumMsg("Time to save this success report content element: {0} ms.", new Object[] { Long.valueOf(timeSpot1 - timeSpot0) });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  938 */     this.mTracer.traceMethodExit(new Object[] { this.mSuccessReport });
/*      */     
/*  940 */     return this.mSuccessReport;
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
/*      */   private Document writeFailedRecsInfo(ObjectStore jaceOS, Map<RecordEntity, List<String>> failureRecSeverityReasonMap)
/*      */   {
/*  954 */     this.mTracer.traceMethodEntry(new Object[] { Boolean.valueOf(this.mHasHeaderIndicatorForFailRep), failureRecSeverityReasonMap, Integer.valueOf(this.mCurrentContentSizeCountForFailRep) });
/*      */     
/*      */ 
/*      */ 
/*  958 */     if (failureRecSeverityReasonMap.isEmpty()) {
/*  959 */       return this.mFailureReport;
/*      */     }
/*      */     
/*  962 */     Set<Map.Entry<RecordEntity, List<String>>> failureMapEntrySet = failureRecSeverityReasonMap.entrySet();
/*      */     
/*  964 */     for (Map.Entry<RecordEntity, List<String>> failureMapEntry : failureMapEntrySet) {
/*  965 */       RecordEntity failedRecEntity = (RecordEntity)failureMapEntry.getKey();
/*  966 */       List<String> severityReasonList = (List)failureMapEntry.getValue();
/*      */       
/*      */ 
/*  969 */       if (!this.mHasHeaderIndicatorForFailRep) {
/*  970 */         this.mFailureReportBuilder.append(DDCPConstants.HEADLINE_FAILURE_REPORT);
/*      */         
/*  972 */         this.mHasHeaderIndicatorForFailRep = true;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  981 */       String recName = failedRecEntity.getRecordName();
/*  982 */       recName = DDCPUtil.sterilizeStringForCSV(recName);
/*  983 */       this.mFailureReportBuilder.append("\"").append(recName).append("\"");
/*      */       
/*  985 */       String parentPath = failedRecEntity.getParentPath();
/*  986 */       parentPath = DDCPUtil.sterilizeStringForCSV(parentPath);
/*  987 */       this.mFailureReportBuilder.append(",\"").append(parentPath).append("\"");
/*      */       
/*  989 */       String filePlan = failedRecEntity.getFilePlan();
/*  990 */       filePlan = DDCPUtil.sterilizeStringForCSV(filePlan);
/*  991 */       this.mFailureReportBuilder.append(",\"").append(filePlan).append("\"");
/*      */       
/*  993 */       String severity = (String)severityReasonList.get(0);
/*  994 */       severity = DDCPUtil.sterilizeStringForCSV(severity);
/*  995 */       this.mFailureReportBuilder.append(",\"").append(severity).append("\"");
/*      */       
/*  997 */       String failureReason = (String)severityReasonList.get(1);
/*  998 */       failureReason = DDCPUtil.sterilizeStringForCSV(failureReason);
/*  999 */       this.mFailureReportBuilder.append(",\"").append(failureReason).append("\"");
/*      */       
/* 1001 */       String recId = failedRecEntity.getRecordID();
/* 1002 */       this.mFailureReportBuilder.append(",").append(recId);
/*      */       
/* 1004 */       this.mFailureReportBuilder.append("\n");
/*      */       
/* 1006 */       this.mCurrentContentSizeCountForFailRep += 1;
/* 1007 */       if (this.mCurrentContentSizeCountForFailRep == this.mContentSizeLimit) {
/* 1008 */         long timeSpot0 = System.currentTimeMillis();
/* 1009 */         String failureReportName = DDCPUtil.generateReportName("NotDeletedRecReport_", 0);
/*      */         
/* 1011 */         this.mFailureReport = DDCPUtil.saveReport(jaceOS, this.mFailureReport, this.mFailureReportBuilder, failureReportName);
/*      */         
/*      */ 
/* 1014 */         this.mFailureReportBuilder.setLength(0);
/*      */         
/*      */ 
/* 1017 */         this.mHasHeaderIndicatorForFailRep = false;
/*      */         
/* 1019 */         this.mCurrentContentSizeCountForFailRep = 0;
/*      */         
/* 1021 */         long timeSpot1 = System.currentTimeMillis();
/* 1022 */         this.mTracer.traceMinimumMsg("Time to save this failure report content element: {0} ms.", new Object[] { Long.valueOf(timeSpot1 - timeSpot0) });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1027 */     this.mTracer.traceMethodExit(new Object[] { this.mFailureReport });
/*      */     
/* 1029 */     return this.mFailureReport;
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
/*      */   private int startSoftDeleteRecords(Map<String, RecordEntity> toBeDeletedRecIdEntityMap, Map<RecordEntity, List<String>> failedRecordSeverityReasonMap, Map<RecordEntity, List<String>> notFoundRecordSeverityReasonMap, Map<String, Set<String>> recIdReceiptIdSetMap, Set<String> relatedLinkIdSet)
/*      */   {
/* 1056 */     this.mTracer.traceMethodEntry(new Object[] { toBeDeletedRecIdEntityMap });
/*      */     
/*      */ 
/*      */ 
/* 1060 */     Map<String, Set<Map<String, String>>> rosIdDocumentIdNameMapSetMap = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1065 */     Map<String, String> docIdRecIdMap = new HashMap();
/*      */     
/*      */ 
/* 1068 */     Set<String> toBeDeletedRecIdSet = getRecordedDocuments(this.mJaceFPOS, this.mReadBatchSize, toBeDeletedRecIdEntityMap, failedRecordSeverityReasonMap, notFoundRecordSeverityReasonMap, rosIdDocumentIdNameMapSetMap, docIdRecIdMap);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1073 */     if (toBeDeletedRecIdSet.isEmpty()) {
/* 1074 */       this.mTracer.traceMinimumMsg("Nothing to delete.", new Object[0]);
/* 1075 */       this.mTracer.traceMethodExit(new Object[] { Integer.valueOf(0) });
/* 1076 */       return 0;
/*      */     }
/*      */     
/* 1079 */     this.mTracer.traceMinimumMsg("There are total {0} Record Object Store.", new Object[] { Integer.valueOf(rosIdDocumentIdNameMapSetMap.size()) });
/* 1080 */     this.mTracer.traceMinimumMsg("There are total {0} recorded documents.", new Object[] { Integer.valueOf(docIdRecIdMap.size()) });
/*      */     
/*      */ 
/* 1083 */     Map<String, Set<String>> receiptsMap = null;
/* 1084 */     if ((this.mIsDODModel) && 
/* 1085 */       (toBeDeletedRecIdSet != null) && (!toBeDeletedRecIdSet.isEmpty())) {
/* 1086 */       receiptsMap = getReceiptsForRecords(this.mJaceFPOS, this.mReadBatchSize, toBeDeletedRecIdSet);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1092 */     Set<String> failedRecIdSet = new HashSet();
/*      */     
/* 1094 */     Set<Map.Entry<String, Set<Map<String, String>>>> toBeDeletedRosIdDocIdNameMapSet = rosIdDocumentIdNameMapSetMap.entrySet();
/*      */     
/* 1096 */     for (Map.Entry<String, Set<Map<String, String>>> toBeDeletedRosDocMapEntry : toBeDeletedRosIdDocIdNameMapSet) {
/* 1097 */       String rosID = (String)toBeDeletedRosDocMapEntry.getKey();
/* 1098 */       BatchProcessingManager batchProcessingMgrROS = getBatchProcessingMgrForRos(rosID);
/*      */       
/* 1100 */       this.mTracer.traceMinimumMsg("Start deleting recorded documents from ROS: {0}.", new Object[] { rosID });
/*      */       
/*      */ 
/*      */ 
/* 1104 */       Set<Map<String, String>> docIDNameMapSet = (Set)toBeDeletedRosDocMapEntry.getValue();
/* 1105 */       for (Map<String, String> docIDNameMap : docIDNameMapSet) {
/* 1106 */         Set<Map.Entry<String, String>> docIDNameMapEntrySet = docIDNameMap.entrySet();
/* 1107 */         for (Map.Entry<String, String> aDocIDNameMapEntry : docIDNameMapEntrySet) {
/* 1108 */           batchProcessingMgrROS.addToBatch((String)aDocIDNameMapEntry.getKey(), (String)aDocIDNameMapEntry.getValue());
/*      */         }
/*      */       }
/*      */       
/* 1112 */       Map<String, Map<String, List<String>>> failedMap = batchProcessingMgrROS.batchDelete();
/*      */       
/*      */ 
/* 1115 */       if ((failedMap != null) && (failedMap.size() > 0)) {
/* 1116 */         Set<Map.Entry<String, Map<String, List<String>>>> failedMapEntrySet = failedMap.entrySet();
/*      */         
/* 1118 */         for (Map.Entry<String, Map<String, List<String>>> afailedMapEntry : failedMapEntrySet) {
/* 1119 */           Map<String, List<String>> failedDocIdSeverityReasonMap = (Map)afailedMapEntry.getValue();
/* 1120 */           Set<String> failedDocIdSet = failedDocIdSeverityReasonMap.keySet();
/*      */           
/* 1122 */           for (String failedDocId : failedDocIdSet) {
/* 1123 */             failedRecIdSet.add(docIdRecIdMap.get(failedDocId));
/*      */           }
/*      */         }
/* 1126 */         this.mTracer.traceMinimumMsg("There are total {0} records failed to delete their recorded documents.", new Object[] { Integer.valueOf(failedRecIdSet.size()) });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1131 */     if (!failedRecIdSet.isEmpty())
/*      */     {
/* 1133 */       if ((receiptsMap != null) && (!receiptsMap.isEmpty())) {
/* 1134 */         receiptsMap.entrySet().removeAll(failedRecIdSet);
/*      */       }
/*      */       
/*      */ 
/* 1138 */       for (String failedRecID : failedRecIdSet) {
/* 1139 */         failedRecordSeverityReasonMap.put(toBeDeletedRecIdEntityMap.get(failedRecID), DDCPConstants.SEVERITY_REASON_LIST_RECORD_RECORDEDDOCUMENT_DELETION_FAILED);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1145 */       toBeDeletedRecIdSet.removeAll(failedRecIdSet);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1151 */     if (!toBeDeletedRecIdSet.isEmpty()) {
/* 1152 */       this.mBatchProcessingMgrFPOS.setProcessingObjectClassName("Document");
/* 1153 */       this.mBatchProcessingMgrFPOS.setUpdatingProperties(UPDATE_PROPS_SOFT_DEL_REC);
/* 1154 */       for (String recID : toBeDeletedRecIdSet) {
/* 1155 */         this.mBatchProcessingMgrFPOS.addToBatch(recID, null);
/*      */       }
/*      */       
/* 1158 */       Set<String> failedUpdatingRecIdSet = this.mBatchProcessingMgrFPOS.batchUpdate();
/*      */       
/*      */ 
/* 1161 */       for (String failedRecId : failedUpdatingRecIdSet)
/*      */       {
/* 1163 */         failedRecordSeverityReasonMap.put(toBeDeletedRecIdEntityMap.get(failedRecId), DDCPConstants.SEVERITY_REASON_LIST_RECORD_SOFT_DELETION_PROPS_UPDATE_FAILED);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1168 */         if ((receiptsMap != null) && (!receiptsMap.isEmpty())) {
/* 1169 */           receiptsMap.entrySet().remove(failedRecId);
/*      */         }
/*      */         
/*      */ 
/* 1173 */         toBeDeletedRecIdSet.remove(failedRecId);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1178 */     if ((receiptsMap != null) && (!receiptsMap.isEmpty())) {
/* 1179 */       recIdReceiptIdSetMap.putAll(receiptsMap);
/*      */     }
/*      */     
/*      */ 
/* 1183 */     if ((toBeDeletedRecIdSet != null) && (!toBeDeletedRecIdSet.isEmpty())) {
/* 1184 */       relatedLinkIdSet.addAll(this.mLinkDetector.getRelatedRMLinkIds(toBeDeletedRecIdSet));
/*      */     }
/*      */     
/*      */ 
/* 1188 */     writeDeletedRecsInfo(this.mJaceFPOS, toBeDeletedRecIdEntityMap, toBeDeletedRecIdSet);
/*      */     
/* 1190 */     int deletedRecCount = toBeDeletedRecIdSet.size();
/*      */     
/* 1192 */     this.mTracer.traceMinimumMsg("There are {0} records being soft deleted in this deleting batch.", new Object[] { Integer.valueOf(deletedRecCount) });
/*      */     
/*      */ 
/* 1195 */     this.mTracer.traceMethodExit(new Object[] { Integer.valueOf(deletedRecCount) });
/* 1196 */     return deletedRecCount;
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
/*      */   private Set<String> getRecordedDocuments(ObjectStore jaceFPOS, int readBatchSize, Map<String, RecordEntity> toBeDeletedRecIdEntityMap, Map<RecordEntity, List<String>> failedRecordSeverityReasonMap, Map<RecordEntity, List<String>> notFoundRecordSeverityReasonMap, Map<String, Set<Map<String, String>>> rosIdDocumentIdNameMapSetMap, Map<String, String> docIdRecIdMap)
/*      */   {
/* 1225 */     this.mTracer.traceMethodEntry(new Object[] { Integer.valueOf(readBatchSize), toBeDeletedRecIdEntityMap });
/*      */     
/* 1227 */     long timeSpot1 = System.currentTimeMillis();
/*      */     
/* 1229 */     List<String> toBeDeletedRecIdList = new ArrayList();
/* 1230 */     toBeDeletedRecIdList.addAll(toBeDeletedRecIdEntityMap.keySet());
/* 1231 */     this.mTracer.traceMinimumMsg("Need retrieve RecordedDocuments for RetainMetaData for {0} records.", new Object[] { Integer.valueOf(toBeDeletedRecIdList.size()) });
/*      */     
/*      */ 
/* 1234 */     Set<String> onHoldRecIdSet = new HashSet();
/* 1235 */     Set<String> markedDeletedRecIdSet = new HashSet();
/* 1236 */     Set<String> allFoundDeletedRecIdSet = new HashSet();
/*      */     
/* 1238 */     SearchScope ss = new SearchScope(jaceFPOS);
/* 1239 */     String recDocQuery = null;
/*      */     try {
/* 1241 */       int startIndex = 0;
/* 1242 */       int endIndex = toBeDeletedRecIdList.size();
/* 1243 */       if (toBeDeletedRecIdList.size() > 100) {
/* 1244 */         endIndex = 100;
/*      */       }
/* 1246 */       while (startIndex < toBeDeletedRecIdList.size())
/*      */       {
/* 1248 */         List<String> subIdList = toBeDeletedRecIdList.subList(startIndex, endIndex);
/*      */         
/* 1250 */         this.mTracer.traceMinimumMsg("Query to retrieve RecordedDocuments for RetainMetaData: {0} with {1} items.", new Object[] { QUERY_TEMPLATE_RECORDED_DOCUMENTS, Integer.valueOf(subIdList.size()) });
/*      */         
/*      */ 
/* 1253 */         String recIdsInStr = DDCPUtil.getEntityIdListInString(subIdList);
/* 1254 */         recDocQuery = String.format(QUERY_TEMPLATE_RECORDED_DOCUMENTS, new Object[] { recIdsInStr });
/* 1255 */         SearchSQL rSql = new SearchSQL(recDocQuery);
/*      */         
/* 1257 */         long beforeFetch = System.currentTimeMillis();
/* 1258 */         RepositoryRowSet recDocRowSetFromQuery = ss.fetchRows(rSql, Integer.valueOf(readBatchSize), recordedDocumentPropertyFilter, Boolean.TRUE);
/*      */         
/* 1260 */         long afterFetch = System.currentTimeMillis();
/*      */         
/* 1262 */         this.mTracer.traceMinimumMsg("Time to get the first result page for retrieving RecordedDocuments: {0} ms.", new Object[] { Long.valueOf(afterFetch - beforeFetch) });
/*      */         
/*      */ 
/* 1265 */         if ((recDocRowSetFromQuery != null) && (!recDocRowSetFromQuery.isEmpty())) {
/* 1266 */           Iterator recRowIt = recDocRowSetFromQuery.iterator();
/* 1267 */           while (recRowIt.hasNext()) {
/* 1268 */             RepositoryRow recordRow = (RepositoryRow)recRowIt.next();
/* 1269 */             String recordId = recordRow.getProperties().getIdValue("Id").toString();
/*      */             
/*      */ 
/*      */ 
/* 1273 */             allFoundDeletedRecIdSet.add(recordId);
/*      */             
/* 1275 */             boolean isRecOnHold = recordRow.getProperties().getBooleanValue("OnHold").booleanValue();
/* 1276 */             boolean isRecDeleted = recordRow.getProperties().getBooleanValue("IsDeleted").booleanValue();
/*      */             
/* 1278 */             if (isRecOnHold)
/*      */             {
/* 1280 */               onHoldRecIdSet.add(recordId);
/* 1281 */               failedRecordSeverityReasonMap.put(toBeDeletedRecIdEntityMap.get(recordId), DDCPConstants.SEVERITY_REASON_LIST_RECORD_ON_HOLD);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/* 1287 */             else if (isRecDeleted)
/*      */             {
/*      */ 
/* 1290 */               markedDeletedRecIdSet.add(recordId);
/* 1291 */               notFoundRecordSeverityReasonMap.put(toBeDeletedRecIdEntityMap.get(recordId), DDCPConstants.SEVERITY_REASON_LIST_RECORD_MARKED_DELETED);
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/* 1297 */               IndependentObjectSet recordedDocSet = recordRow.getProperties().getIndependentObjectSetValue("RecordedDocuments");
/*      */               
/* 1299 */               Iterator recordedDocSetIter = recordedDocSet.iterator();
/* 1300 */               if (recordedDocSetIter.hasNext()) {
/* 1301 */                 IndependentObject doc = (IndependentObject)recordedDocSetIter.next();
/* 1302 */                 String docID = doc.getProperties().getIdValue("Id").toString();
/* 1303 */                 String docName = doc.getProperties().getStringValue("Name");
/* 1304 */                 String rosID = doc.getObjectReference().getObjectStoreIdentity();
/*      */                 
/* 1306 */                 docIdRecIdMap.put(docID, recordId);
/*      */                 
/* 1308 */                 Map<String, String> docIdNameMap = new HashMap();
/* 1309 */                 docIdNameMap.put(docID, docName);
/*      */                 
/* 1311 */                 if (rosIdDocumentIdNameMapSetMap.containsKey(rosID)) {
/* 1312 */                   Set<Map<String, String>> documentIdNameMapSet = (Set)rosIdDocumentIdNameMapSetMap.get(rosID);
/* 1313 */                   documentIdNameMapSet.add(docIdNameMap);
/*      */                 } else {
/* 1315 */                   Set<Map<String, String>> documentIdNameMapSet = new HashSet();
/* 1316 */                   documentIdNameMapSet.add(docIdNameMap);
/* 1317 */                   rosIdDocumentIdNameMapSetMap.put(rosID, documentIdNameMapSet);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1324 */         startIndex = endIndex;
/* 1325 */         endIndex += 100;
/* 1326 */         if (endIndex > toBeDeletedRecIdList.size()) {
/* 1327 */           endIndex = toBeDeletedRecIdList.size();
/*      */         }
/*      */       }
/*      */     } catch (Exception ex) {
/* 1331 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { recDocQuery });
/*      */     }
/*      */     
/*      */ 
/* 1335 */     if (!onHoldRecIdSet.isEmpty()) {
/* 1336 */       this.mTracer.traceMinimumMsg("There are {0} records on hold in this records deletion batch.", new Object[] { Integer.valueOf(onHoldRecIdSet.size()) });
/*      */     }
/*      */     
/* 1339 */     if (!markedDeletedRecIdSet.isEmpty()) {
/* 1340 */       this.mTracer.traceMinimumMsg("There are {0} records marked as deleted in this records deletion batch.", new Object[] { Integer.valueOf(markedDeletedRecIdSet.size()) });
/*      */     }
/*      */     
/* 1343 */     toBeDeletedRecIdList.removeAll(allFoundDeletedRecIdSet);
/*      */     
/* 1345 */     if (!toBeDeletedRecIdList.isEmpty()) {
/* 1346 */       for (String notFoundRecId : toBeDeletedRecIdList) {
/* 1347 */         notFoundRecordSeverityReasonMap.put(toBeDeletedRecIdEntityMap.get(notFoundRecId), DDCPConstants.SEVERITY_REASON_LIST_RECORD_MARKED_DELETED);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1352 */       this.mTracer.traceMinimumMsg("There are {0} records not found in this records deletion batch.", new Object[] { Integer.valueOf(toBeDeletedRecIdList.size()) });
/*      */     }
/*      */     
/* 1355 */     long timeSpot2 = System.currentTimeMillis();
/* 1356 */     this.mTracer.traceMinimumMsg("Time to get RecordedDocuments for this records deletion batch : {0} ms.", new Object[] { Long.valueOf(timeSpot2 - timeSpot1) });
/*      */     
/* 1358 */     allFoundDeletedRecIdSet.removeAll(onHoldRecIdSet);
/* 1359 */     allFoundDeletedRecIdSet.removeAll(markedDeletedRecIdSet);
/*      */     
/*      */ 
/* 1362 */     this.mTracer.traceMethodExit(new Object[] { allFoundDeletedRecIdSet });
/*      */     
/* 1364 */     return allFoundDeletedRecIdSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private BatchProcessingManager getBatchProcessingMgrForRos(String rosID)
/*      */   {
/* 1375 */     this.mTracer.traceMethodEntry(new Object[] { rosID });
/* 1376 */     if (this.mROSBatchProcessingMgrCache.containsKey(rosID)) {
/* 1377 */       this.mTracer.traceMethodExit(new Object[0]);
/* 1378 */       return (BatchProcessingManager)this.mROSBatchProcessingMgrCache.get(rosID);
/*      */     }
/* 1380 */     ObjectStore ros = null;
/* 1381 */     boolean isAuditOn = false;
/* 1382 */     if (this.mROSCache.containsKey(rosID)) {
/* 1383 */       Map<ObjectStore, Boolean> theROSAuditMap = (Map)this.mROSCache.get(rosID);
/* 1384 */       Set<Map.Entry<ObjectStore, Boolean>> entrySet = theROSAuditMap.entrySet();
/* 1385 */       Iterator i$ = entrySet.iterator(); if (i$.hasNext()) { Map.Entry<ObjectStore, Boolean> anEntry = (Map.Entry)i$.next();
/* 1386 */         ros = (ObjectStore)anEntry.getKey();
/* 1387 */         isAuditOn = ((Boolean)anEntry.getValue()).booleanValue();
/*      */       }
/*      */     }
/*      */     else {
/*      */       try {
/* 1392 */         ros = Factory.ObjectStore.fetchInstance(this.mJaceDomain, rosID, objectStorePropertyFilter);
/*      */       } catch (Exception ex) {
/* 1394 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_FETCH_OBJECTSTORE, new Object[] { rosID });
/*      */       }
/* 1396 */       if (ros == null) {
/* 1397 */         throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.FAILED_TO_FETCH_OBJECTSTORE, new Object[] { rosID });
/*      */       }
/*      */       
/* 1400 */       if (ros.get_AuditLevel() == AuditLevel.ENABLED) {
/* 1401 */         isAuditOn = true;
/*      */       }
/* 1403 */       Map<ObjectStore, Boolean> theROSAuditMap = new HashMap(1);
/* 1404 */       theROSAuditMap.put(ros, Boolean.valueOf(isAuditOn));
/*      */       
/* 1406 */       this.mROSCache.put(rosID, theROSAuditMap);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1411 */     BatchProcessingManager batchProcessingMgrROS = new BatchProcessingManager(ros, this.mThreadCount, isAuditOn, this.mReviewer);
/*      */     
/* 1413 */     batchProcessingMgrROS.setUpdatingProperties(UPDATE_PROPS_RECINFO_NULL);
/* 1414 */     batchProcessingMgrROS.setProcessingObjectClassName("Document");
/*      */     
/*      */ 
/* 1417 */     this.mROSBatchProcessingMgrCache.put(rosID, batchProcessingMgrROS);
/*      */     
/* 1419 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */     
/* 1421 */     return batchProcessingMgrROS;
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
/*      */   private int startHardDeleteRecords(Map<String, RecordEntity> toBeDeletedRecIdEntityMap, Map<RecordEntity, List<String>> failedRecordSeverityReasonMap, Map<RecordEntity, List<String>> notFoundRecordSeverityReasonMap, Map<String, Set<String>> recIdReceiptIdSetMap, Set<String> relatedLinkIdSet)
/*      */   {
/* 1447 */     this.mTracer.traceMethodEntry(new Object[] { toBeDeletedRecIdEntityMap });
/*      */     
/* 1449 */     Set<String> deletedRecIdSet = new HashSet();
/* 1450 */     deletedRecIdSet.addAll(toBeDeletedRecIdEntityMap.keySet());
/*      */     
/*      */ 
/* 1453 */     Map<String, Set<String>> receiptsMap = null;
/* 1454 */     if ((this.mIsDODModel) && 
/* 1455 */       (deletedRecIdSet != null) && (!deletedRecIdSet.isEmpty())) {
/* 1456 */       receiptsMap = getReceiptsForRecords(this.mJaceFPOS, this.mReadBatchSize, deletedRecIdSet);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1463 */     this.mBatchProcessingMgrFPOS.setUpdatingProperties(null);
/* 1464 */     this.mBatchProcessingMgrFPOS.setProcessingObjectClassName("Document");
/*      */     
/* 1466 */     Set<Map.Entry<String, RecordEntity>> toBeDeletedRecMapEntrySet = toBeDeletedRecIdEntityMap.entrySet();
/*      */     
/* 1468 */     for (Map.Entry<String, RecordEntity> toBeDeletedRecMapEntry : toBeDeletedRecMapEntrySet) {
/* 1469 */       String recId = (String)toBeDeletedRecMapEntry.getKey();
/* 1470 */       String recName = ((RecordEntity)toBeDeletedRecMapEntry.getValue()).getRecordName();
/*      */       
/* 1472 */       this.mBatchProcessingMgrFPOS.addToBatch(recId, recName);
/*      */     }
/*      */     
/*      */ 
/* 1476 */     Map<String, Map<String, List<String>>> failedMap = this.mBatchProcessingMgrFPOS.batchDelete();
/*      */     
/* 1478 */     Set<String> allFailedRecIdSet = new HashSet();
/*      */     
/*      */ 
/* 1481 */     if (failedMap.containsKey("NotDeletedRecMap")) {
/* 1482 */       Map<String, List<String>> failedRecIdSeverityReasonMap = (Map)failedMap.get("NotDeletedRecMap");
/*      */       
/* 1484 */       Set<String> failedRecIdSet = processFailedRecordsMap(toBeDeletedRecIdEntityMap, failedRecIdSeverityReasonMap, failedRecordSeverityReasonMap);
/*      */       
/*      */ 
/*      */ 
/* 1488 */       if ((failedRecIdSet != null) && (!failedRecIdSet.isEmpty())) {
/* 1489 */         this.mTracer.traceMinimumMsg("There are {0} not deleted records (excluding not found records) in this deleting batch.", new Object[] { Integer.valueOf(failedRecIdSet.size()) });
/*      */         
/* 1491 */         allFailedRecIdSet.addAll(failedRecIdSet);
/* 1492 */         deletedRecIdSet.removeAll(failedRecIdSet);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1497 */     if (failedMap.containsKey("NotFoundRecMap")) {
/* 1498 */       Map<String, List<String>> notFoundRecIdSeverityReasonMap = (Map)failedMap.get("NotFoundRecMap");
/*      */       
/* 1500 */       Set<String> notFoundRecIdSet = processFailedRecordsMap(toBeDeletedRecIdEntityMap, notFoundRecIdSeverityReasonMap, notFoundRecordSeverityReasonMap);
/*      */       
/*      */ 
/*      */ 
/* 1504 */       if ((notFoundRecIdSet != null) && (!notFoundRecIdSet.isEmpty())) {
/* 1505 */         this.mTracer.traceMinimumMsg("There are {0} not found records in this deleting batch.", new Object[] { Integer.valueOf(notFoundRecIdSet.size()) });
/*      */         
/* 1507 */         allFailedRecIdSet.addAll(notFoundRecIdSet);
/* 1508 */         deletedRecIdSet.removeAll(notFoundRecIdSet);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1513 */     if ((receiptsMap != null) && (!receiptsMap.isEmpty())) {
/* 1514 */       if (!allFailedRecIdSet.isEmpty()) {
/* 1515 */         receiptsMap.entrySet().removeAll(allFailedRecIdSet);
/*      */       }
/*      */       
/* 1518 */       recIdReceiptIdSetMap.putAll(receiptsMap);
/*      */     }
/*      */     
/*      */ 
/* 1522 */     if ((deletedRecIdSet != null) && (!deletedRecIdSet.isEmpty())) {
/* 1523 */       relatedLinkIdSet.addAll(this.mLinkDetector.getRelatedRMLinkIds(deletedRecIdSet));
/*      */     }
/*      */     
/*      */ 
/* 1527 */     writeDeletedRecsInfo(this.mJaceFPOS, toBeDeletedRecIdEntityMap, deletedRecIdSet);
/*      */     
/* 1529 */     int deletedRecCount = deletedRecIdSet.size();
/*      */     
/* 1531 */     this.mTracer.traceMinimumMsg("There are {0} records being deleted in this deleting batch.", new Object[] { Integer.valueOf(deletedRecCount) });
/*      */     
/*      */ 
/* 1534 */     this.mTracer.traceMethodExit(new Object[] { Integer.valueOf(deletedRecCount) });
/* 1535 */     return deletedRecCount;
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
/*      */   private Set<String> processFailedRecordsMap(Map<String, RecordEntity> toBeDeletedRecIdEntityMap, Map<String, List<String>> failedRecIdSeverityReasonMap, Map<RecordEntity, List<String>> failedRecordSeverityReasonMap)
/*      */   {
/* 1553 */     this.mTracer.traceMethodEntry(new Object[] { failedRecIdSeverityReasonMap });
/*      */     
/* 1555 */     Set<String> failedRecIdSet = new HashSet();
/*      */     
/* 1557 */     if ((failedRecIdSeverityReasonMap != null) && (!failedRecIdSeverityReasonMap.isEmpty())) {
/* 1558 */       failedRecIdSet.addAll(failedRecIdSeverityReasonMap.keySet());
/*      */       
/* 1560 */       Set<Map.Entry<String, List<String>>> failedRecIdSeverityReasonMapEntrySet = failedRecIdSeverityReasonMap.entrySet();
/*      */       
/* 1562 */       for (Map.Entry<String, List<String>> failedRecIdSeverityReasonMapEntry : failedRecIdSeverityReasonMapEntrySet) {
/* 1563 */         String recId = (String)failedRecIdSeverityReasonMapEntry.getKey();
/* 1564 */         RecordEntity recEntity = (RecordEntity)toBeDeletedRecIdEntityMap.get(recId);
/*      */         
/* 1566 */         failedRecordSeverityReasonMap.put(recEntity, failedRecIdSeverityReasonMapEntry.getValue());
/*      */       }
/*      */     }
/*      */     
/* 1570 */     this.mTracer.traceMethodExit(new Object[] { failedRecIdSet });
/* 1571 */     return failedRecIdSet;
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
/*      */   private Map<String, Set<String>> getReceiptsForRecords(ObjectStore jaceOS, int readBatchSize, Set<String> recIdSet)
/*      */   {
/* 1585 */     this.mTracer.traceMethodEntry(new Object[] { recIdSet });
/*      */     
/* 1587 */     Map<String, Set<String>> recIDReceitSetMap = new HashMap();
/*      */     
/* 1589 */     if (this.mIsDODModel) {
/* 1590 */       long timeSpot1 = System.currentTimeMillis();
/*      */       
/* 1592 */       List<String> recIdList = new ArrayList();
/* 1593 */       recIdList.addAll(recIdSet);
/* 1594 */       this.mTracer.traceMinimumMsg("Need retrieve receipt for {0} records.", new Object[] { Integer.valueOf(recIdList.size()) });
/*      */       
/*      */ 
/* 1597 */       SearchScope ss = new SearchScope(jaceOS);
/*      */       try
/*      */       {
/* 1600 */         int startIndex = 0;
/* 1601 */         int endIndex = recIdList.size();
/* 1602 */         if (recIdList.size() > 100) {
/* 1603 */           endIndex = 100;
/*      */         }
/* 1605 */         while (startIndex < recIdList.size()) {
/* 1606 */           List<String> subIdList = recIdList.subList(startIndex, endIndex);
/*      */           
/* 1608 */           String recIdsInStr = DDCPUtil.getEntityIdListInString(subIdList);
/* 1609 */           String receiptQuery = String.format(QUERY_TEMPLATE_SPECIFIED_RECORD_RECEIPTS, new Object[] { recIdsInStr });
/* 1610 */           SearchSQL rSql = new SearchSQL(receiptQuery);
/* 1611 */           this.mTracer.traceMinimumMsg("Query to retrieve record''s receipt: {0} with {1} items.", new Object[] { QUERY_TEMPLATE_SPECIFIED_RECORD_RECEIPTS, Integer.valueOf(subIdList.size()) });
/*      */           
/*      */ 
/* 1614 */           long beforeFetch = System.currentTimeMillis();
/* 1615 */           RepositoryRowSet recordRowSetFromQuery = ss.fetchRows(rSql, Integer.valueOf(readBatchSize), recordReceiptsPropertyFilter, Boolean.TRUE);
/*      */           
/* 1617 */           long afterFetch = System.currentTimeMillis();
/*      */           
/* 1619 */           this.mTracer.traceMinimumMsg("Time to get the first result page for retrieving Receipts property: {0} ms.", new Object[] { Long.valueOf(afterFetch - beforeFetch) });
/*      */           
/*      */ 
/* 1622 */           if ((recordRowSetFromQuery != null) && (!recordRowSetFromQuery.isEmpty())) {
/* 1623 */             Iterator recRowIt = recordRowSetFromQuery.iterator();
/* 1624 */             while (recRowIt.hasNext()) {
/* 1625 */               RepositoryRow recordRow = (RepositoryRow)recRowIt.next();
/* 1626 */               String recordId = recordRow.getProperties().getIdValue("Id").toString();
/* 1627 */               IndependentObjectSet receiptSetFromQuery = recordRow.getProperties().getIndependentObjectSetValue("Receipts");
/*      */               
/*      */ 
/* 1630 */               Set<String> receiptIdSet = new HashSet();
/*      */               
/* 1632 */               if ((receiptSetFromQuery != null) && (!receiptSetFromQuery.isEmpty())) {
/* 1633 */                 Iterator receiptIt = receiptSetFromQuery.iterator();
/* 1634 */                 while (receiptIt.hasNext()) {
/* 1635 */                   IndependentObject receiptObj = (IndependentObject)receiptIt.next();
/* 1636 */                   String receiptId = receiptObj.getProperties().getIdValue("Id").toString();
/* 1637 */                   receiptIdSet.add(receiptId);
/*      */                 }
/*      */               }
/*      */               
/* 1641 */               if (!receiptIdSet.isEmpty()) {
/* 1642 */                 recIDReceitSetMap.put(recordId, receiptIdSet);
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 1647 */           startIndex = endIndex;
/* 1648 */           endIndex += 100;
/* 1649 */           if (endIndex > recIdList.size()) {
/* 1650 */             endIndex = recIdList.size();
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception ex) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1661 */       long timeSpot2 = System.currentTimeMillis();
/* 1662 */       this.mTracer.traceMinimumMsg("Time to get possible receipts for this records deletion batch : {0} ms.", new Object[] { Long.valueOf(timeSpot2 - timeSpot1) });
/*      */     }
/*      */     
/* 1665 */     this.mTracer.traceMethodExit(new Object[] { recIDReceitSetMap });
/* 1666 */     return recIDReceitSetMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validatePerformanceTuningParams(Map<String, String> configContextMap)
/*      */   {
/* 1678 */     this.mTracer.traceMethodEntry(new Object[] { configContextMap });
/*      */     
/* 1680 */     if (configContextMap.containsKey("ThreadCount")) {
/* 1681 */       int threadCount = Integer.valueOf((String)configContextMap.get("ThreadCount")).intValue();
/*      */       
/*      */ 
/* 1684 */       if (threadCount > 0) {
/* 1685 */         this.mThreadCount = threadCount;
/*      */       }
/*      */     }
/* 1688 */     this.mTracer.traceMinimumMsg("Thread count : {0}", new Object[] { Integer.valueOf(this.mThreadCount) });
/* 1689 */     this.mLogger.info(DDCPLogCode.THREAD_COUNT, new Object[] { Integer.valueOf(this.mThreadCount) });
/*      */     
/* 1691 */     if (configContextMap.containsKey("ReadBatchSize")) {
/* 1692 */       int readBatchSize = Integer.valueOf((String)configContextMap.get("ReadBatchSize")).intValue();
/*      */       
/*      */ 
/* 1695 */       if (readBatchSize > 0) {
/* 1696 */         this.mReadBatchSize = readBatchSize;
/*      */       }
/*      */     }
/* 1699 */     this.mTracer.traceMinimumMsg("Read batch size : {0}", new Object[] { Integer.valueOf(this.mReadBatchSize) });
/* 1700 */     this.mLogger.info(DDCPLogCode.READ_BATCH_SIZE, new Object[] { Integer.valueOf(this.mReadBatchSize) });
/*      */     
/* 1702 */     if (configContextMap.containsKey("UpdateBatchSize")) {
/* 1703 */       int updateBatchSize = Integer.valueOf((String)configContextMap.get("UpdateBatchSize")).intValue();
/*      */       
/*      */ 
/* 1706 */       if (updateBatchSize > 0) {
/* 1707 */         this.mUpdateBatchSize = updateBatchSize;
/*      */       }
/*      */     }
/* 1710 */     this.mTracer.traceMinimumMsg("Update batch size : {0}", new Object[] { Integer.valueOf(this.mUpdateBatchSize) });
/* 1711 */     this.mLogger.info(DDCPLogCode.UPDATE_BATCH_SIZE, new Object[] { Integer.valueOf(this.mUpdateBatchSize) });
/*      */     
/* 1713 */     if (configContextMap.containsKey("ContentSizeLimit")) {
/* 1714 */       int contentSizeLimit = Integer.valueOf((String)configContextMap.get("ContentSizeLimit")).intValue();
/*      */       
/*      */ 
/* 1717 */       if (contentSizeLimit > 0) {
/* 1718 */         this.mContentSizeLimit = contentSizeLimit;
/*      */       }
/*      */     }
/* 1721 */     this.mTracer.traceMinimumMsg("Content size limit : {0}", new Object[] { Integer.valueOf(this.mContentSizeLimit) });
/* 1722 */     this.mLogger.info(DDCPLogCode.CONTENT_SIZE_LIMIT, new Object[] { Integer.valueOf(this.mContentSizeLimit) });
/*      */     
/* 1724 */     if (configContextMap.containsKey("LinkCacheSizeLimit")) {
/* 1725 */       int linkCacheSizeLimit = Integer.valueOf((String)configContextMap.get("LinkCacheSizeLimit")).intValue();
/*      */       
/*      */ 
/* 1728 */       if (linkCacheSizeLimit > 0) {
/* 1729 */         this.mLinkCacheSizeLimit = linkCacheSizeLimit;
/*      */       }
/*      */     }
/* 1732 */     this.mTracer.traceMinimumMsg("Link cache size limit : {0}", new Object[] { Integer.valueOf(this.mLinkCacheSizeLimit) });
/* 1733 */     this.mLogger.info(DDCPLogCode.LINK_CACHE_SIZE_LIMIT, new Object[] { Integer.valueOf(this.mLinkCacheSizeLimit) });
/*      */     
/* 1735 */     if (configContextMap.containsKey("OnHoldContainerCacheSizeLimit")) {
/* 1736 */       int onHoldContainerCacheSizeLimit = Integer.valueOf((String)configContextMap.get("OnHoldContainerCacheSizeLimit")).intValue();
/*      */       
/*      */ 
/* 1739 */       if (onHoldContainerCacheSizeLimit > 0) {
/* 1740 */         this.mOnHoldContainerCacheSizeLimit = onHoldContainerCacheSizeLimit;
/*      */       }
/*      */     }
/* 1743 */     this.mTracer.traceMinimumMsg("On hold Container cache size limit : {0}", new Object[] { Integer.valueOf(this.mOnHoldContainerCacheSizeLimit) });
/* 1744 */     this.mLogger.info(DDCPLogCode.ONHOLD_CONTAINER_CACHE_SIZE_LIMIT, new Object[] { Integer.valueOf(this.mOnHoldContainerCacheSizeLimit) });
/*      */     
/* 1746 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void verifyFPOSGetDataModel(ObjectStore jaceOS)
/*      */   {
/* 1758 */     this.mTracer.traceMethodEntry(new Object[0]);
/*      */     
/* 1760 */     this.mTracer.traceMinimumMsg("Query to verify FPOS and get data model: {0}", new Object[] { QUERY_CHECK_FPOS_DOD_MODEL });
/*      */     
/* 1762 */     PropertyFilter filter = new PropertyFilter();
/* 1763 */     FilterElement fe = new FilterElement(null, null, null, "Id", null);
/* 1764 */     filter.addIncludeProperty(fe);
/*      */     
/* 1766 */     SearchSQL qSql = new SearchSQL(QUERY_CHECK_FPOS_DOD_MODEL);
/*      */     
/* 1768 */     SearchScope ss = new SearchScope(jaceOS);
/*      */     
/* 1770 */     long startTime = System.currentTimeMillis();
/* 1771 */     RepositoryRowSet resultSet = null;
/*      */     try {
/* 1773 */       resultSet = ss.fetchRows(qSql, Integer.valueOf(1), filter, Boolean.FALSE);
/*      */     } catch (Exception ex) {
/* 1775 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.OBJECT_STORE_IS_NOT_FPOS, new Object[0]);
/*      */     }
/* 1777 */     long endTime = System.currentTimeMillis();
/* 1778 */     this.mTracer.traceMinimumMsg("Time to retrieve FPOS Setup from SystemConfiguration : {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*      */     
/* 1780 */     if ((resultSet == null) || (resultSet.isEmpty())) {
/* 1781 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.OBJECT_STORE_IS_NOT_FPOS, new Object[0]);
/*      */     }
/*      */     
/* 1784 */     boolean isDODModel = false;
/* 1785 */     Iterator rowIter = resultSet.iterator();
/* 1786 */     if (rowIter.hasNext())
/*      */     {
/* 1788 */       RepositoryRow row = (RepositoryRow)rowIter.next();
/* 1789 */       String dataModel = row.getProperties().getStringValue("PropertyValue");
/* 1790 */       if ("DOD-5015.2 Classified".equalsIgnoreCase(dataModel)) {
/* 1791 */         isDODModel = true;
/*      */       }
/*      */     }
/*      */     
/* 1795 */     this.mIsDODModel = isDODModel;
/*      */     
/* 1797 */     this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(this.mIsDODModel) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void cacheFilePlanRetainMetaData(ObjectStore jaceOS)
/*      */   {
/* 1808 */     this.mTracer.traceMethodEntry(new Object[0]);
/*      */     
/* 1810 */     this.mTracer.traceMinimumMsg("Query for file plan''s RetainMetaData: {0}", new Object[] { QUERY_FILEPLAN_RETAINMETADATA });
/*      */     
/* 1812 */     SearchSQL qSql = new SearchSQL(QUERY_FILEPLAN_RETAINMETADATA);
/*      */     
/* 1814 */     SearchScope ss = new SearchScope(jaceOS);
/*      */     
/* 1816 */     long startTime = System.currentTimeMillis();
/* 1817 */     RepositoryRowSet resultSet = null;
/*      */     try {
/* 1819 */       resultSet = ss.fetchRows(qSql, Integer.valueOf(this.mReadBatchSize), filePlanPropertyFilter, Boolean.FALSE);
/*      */     } catch (Exception ex) {
/* 1821 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { QUERY_FILEPLAN_RETAINMETADATA });
/*      */     }
/*      */     
/* 1824 */     long endTime = System.currentTimeMillis();
/* 1825 */     this.mTracer.traceMinimumMsg("Time to retrieve file plans : {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*      */     
/* 1827 */     if ((resultSet == null) || (resultSet.isEmpty())) {
/* 1828 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.NO_FILEPLAN, new Object[0]);
/*      */     }
/*      */     
/* 1831 */     Iterator rowIter = resultSet.iterator();
/* 1832 */     while (rowIter.hasNext())
/*      */     {
/* 1834 */       RepositoryRow row = (RepositoryRow)rowIter.next();
/* 1835 */       String filePlanName = row.getProperties().getStringValue("ClassificationSchemeName");
/* 1836 */       this.mTracer.traceMinimumMsg("Checking File plan [{0}] RetainMetaData value", new Object[] { filePlanName });
/* 1837 */       int retainMetaData = row.getProperties().getInteger32Value("RetainMetadata").intValue();
/* 1838 */       if (retainMetaData == 1) {
/* 1839 */         this.mFilePlanRetainMetaDataCacheMap.put(filePlanName, Boolean.valueOf(false));
/* 1840 */         this.mTracer.traceMinimumMsg("File plan [{0}] RetainMetaData is disabled.", new Object[] { filePlanName });
/* 1841 */         this.mLogger.info(DDCPLogCode.RETAINMETADATA_OFF, new Object[] { filePlanName });
/*      */       } else {
/* 1843 */         this.mFilePlanRetainMetaDataCacheMap.put(filePlanName, Boolean.valueOf(true));
/* 1844 */         this.mTracer.traceMinimumMsg("File plan [{0}] RetainMetaData is enabled.", new Object[] { filePlanName });
/* 1845 */         this.mLogger.info(DDCPLogCode.RETAINMETADATA_ON, new Object[] { filePlanName });
/*      */       }
/*      */     }
/*      */     
/* 1849 */     this.mTracer.traceMethodExit(new Object[0]);
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
/*      */   private boolean isScheduleChangedInDDContainer(ObjectStore jaceFPOS, String ddContainerID, String triggerNameInReport, String retentionPeriodInReport)
/*      */   {
/* 1866 */     this.mTracer.traceMethodEntry(new Object[] { ddContainerID });
/*      */     
/*      */ 
/* 1869 */     if (this.mDDContainerScheduleChangeCache.containsKey(ddContainerID)) {
/* 1870 */       Map<String, List<String>> severityReasonListMap = (Map)this.mDDContainerScheduleChangeCache.get(ddContainerID);
/* 1871 */       if (severityReasonListMap == null) {
/* 1872 */         this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 1873 */         return false;
/*      */       }
/* 1875 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 1876 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1880 */     String ddContainerQuery = String.format(QUERY_TEMPLATE_CHECK_DDCONTAINER_SCHEDULE, new Object[] { ddContainerID });
/* 1881 */     SearchSQL rSql = new SearchSQL(ddContainerQuery);
/* 1882 */     this.mTracer.traceMinimumMsg("Query to retrieve the basic schedule for this container: {0}", new Object[] { ddContainerQuery });
/*      */     
/* 1884 */     SearchScope ss = new SearchScope(jaceFPOS);
/*      */     
/* 1886 */     long beforeFetch = System.currentTimeMillis();
/* 1887 */     RepositoryRowSet ddContainerRowSet = null;
/*      */     try {
/* 1889 */       ddContainerRowSet = ss.fetchRows(rSql, Integer.valueOf(1), ddContainerPropertyFilter, Boolean.FALSE);
/*      */     }
/*      */     catch (Exception ex) {
/* 1892 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { ddContainerQuery });
/*      */     }
/*      */     
/* 1895 */     long afterFetch = System.currentTimeMillis();
/*      */     
/* 1897 */     this.mTracer.traceMinimumMsg("Time to get the result for retrieving the basic schedule for this container: {0} ms.", new Object[] { Long.valueOf(afterFetch - beforeFetch) });
/*      */     
/*      */ 
/* 1900 */     if ((ddContainerRowSet != null) && (!ddContainerRowSet.isEmpty())) {
/* 1901 */       Iterator ddContainerRowIt = ddContainerRowSet.iterator();
/* 1902 */       if (ddContainerRowIt.hasNext()) {
/* 1903 */         RepositoryRow ddContainerRow = (RepositoryRow)ddContainerRowIt.next();
/* 1904 */         String triggerName = ddContainerRow.getProperties().getStringValue("RMRetentionTriggerPropertyName").toString();
/*      */         
/*      */ 
/* 1907 */         String retentionPeriod = ddContainerRow.getProperties().getStringValue("RMRetentionPeriod").toString();
/*      */         
/*      */ 
/*      */ 
/* 1911 */         Map<String, List<String>> failSeverityReasonListMap = null;
/* 1912 */         boolean isScheduleChanged = false;
/* 1913 */         if ((triggerName == null) || (!triggerName.trim().equals(triggerNameInReport.trim()))) {
/* 1914 */           failSeverityReasonListMap = new HashMap(1);
/* 1915 */           List<String> failSeverityReasonList = new ArrayList(2);
/* 1916 */           failSeverityReasonList.add(DDCPLString.get("severity.scheduleChanged", "Schedule Changed"));
/* 1917 */           String failureReason = DDCPLString.get("failure.retentionTriggerChanged", "RMRetentionTriggerPropertyName in its container with basic schedule is changed to {0}");
/*      */           
/* 1919 */           String formattedFailureReason = MessageFormat.format(failureReason, new Object[] { triggerName });
/* 1920 */           failSeverityReasonList.add(formattedFailureReason);
/* 1921 */           failSeverityReasonListMap.put("DDContainerScheduleChanged", failSeverityReasonList);
/*      */           
/* 1923 */           this.mTracer.traceMinimumMsg("RMRetentionTriggerPropertyName in its container with basic schedule {0} is changed to {1}", new Object[] { ddContainerID, triggerName });
/*      */           
/* 1925 */           this.mLogger.warn(DDCPLogCode.TRIGGER_NAME_CHANGED, new Object[] { ddContainerID, triggerName });
/*      */           
/* 1927 */           isScheduleChanged = true;
/* 1928 */         } else if ((retentionPeriod == null) || (!retentionPeriod.trim().equals(retentionPeriodInReport.trim()))) {
/* 1929 */           failSeverityReasonListMap = new HashMap(1);
/* 1930 */           List<String> failSeverityReasonList = new ArrayList(2);
/* 1931 */           failSeverityReasonList.add(DDCPLString.get("severity.scheduleChanged", "Schedule Changed"));
/* 1932 */           String failureReason = DDCPLString.get("failure.retentionPeriodChanged", "RMRetentionPeriod in its container with basic schedule is changed to {0}");
/*      */           
/* 1934 */           String formattedFailureReason = MessageFormat.format(failureReason, new Object[] { retentionPeriod });
/* 1935 */           failSeverityReasonList.add(formattedFailureReason);
/* 1936 */           failSeverityReasonListMap.put("DDContainerScheduleChanged", failSeverityReasonList);
/*      */           
/* 1938 */           this.mTracer.traceMinimumMsg("RMRetentionPeriod in its container with basic schedule {0} is changed to {1}", new Object[] { ddContainerID, retentionPeriod });
/*      */           
/* 1940 */           this.mLogger.warn(DDCPLogCode.RETENTION_PERIOD_CHANGED, new Object[] { ddContainerID, retentionPeriod });
/*      */           
/* 1942 */           isScheduleChanged = true;
/*      */         }
/*      */         
/*      */ 
/* 1946 */         this.mDDContainerScheduleChangeCache.put(ddContainerID, failSeverityReasonListMap);
/*      */         
/* 1948 */         this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(isScheduleChanged) });
/* 1949 */         return isScheduleChanged;
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1955 */       Map<String, List<String>> failSeverityReasonListMap = new HashMap(1);
/* 1956 */       List<String> failSeverityReasonList = new ArrayList(2);
/* 1957 */       failSeverityReasonList.add(DDCPLString.get("severity.notFound", "Not Found"));
/* 1958 */       String failureReason = DDCPLString.get("failure.notFound", "This record does not exist");
/*      */       
/* 1960 */       String formattedFailureReason = MessageFormat.format(failureReason, new Object[] { ddContainerID });
/* 1961 */       failSeverityReasonList.add(formattedFailureReason);
/* 1962 */       failSeverityReasonListMap.put("DDContainerNotExit", failSeverityReasonList);
/*      */       
/* 1964 */       this.mTracer.traceMinimumMsg("The container with basic schedule {0} cannot be found.", new Object[] { ddContainerID });
/*      */       
/* 1966 */       this.mLogger.warn(DDCPLogCode.DDCONTAINER_NOT_FOUND, new Object[] { ddContainerID });
/*      */       
/*      */ 
/* 1969 */       this.mDDContainerScheduleChangeCache.put(ddContainerID, failSeverityReasonListMap);
/*      */       
/* 1971 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 1972 */       return true;
/*      */     }
/* 1974 */     this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 1975 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resetFailedRecsInReportStatus(Map<RecordEntity, List<String>> failedRecMap)
/*      */   {
/* 1987 */     this.mTracer.traceMethodEntry(new Object[] { Integer.valueOf(failedRecMap.size()) });
/*      */     
/* 1989 */     if (!failedRecMap.isEmpty()) {
/* 1990 */       this.mTracer.traceMinimumMsg("Start updating {0} not deleted records in-report status to NotInReport...", new Object[] { Integer.valueOf(failedRecMap.size()) });
/*      */       
/*      */ 
/* 1993 */       Set<RecordEntity> failedRecSet = failedRecMap.keySet();
/*      */       
/* 1995 */       this.mBatchProcessingMgrFPOS.setProcessingObjectClassName("Document");
/* 1996 */       this.mBatchProcessingMgrFPOS.setUpdatingProperties(UPDATE_PROPS_RESET_REPORT_STATUS);
/*      */       
/* 1998 */       int updateBatchCount = 0;
/* 1999 */       for (RecordEntity failedRec : failedRecSet) {
/* 2000 */         this.mBatchProcessingMgrFPOS.addToBatch(failedRec.getRecordID(), null);
/* 2001 */         updateBatchCount++;
/* 2002 */         if (updateBatchCount == this.mUpdateBatchSize) {
/* 2003 */           this.mBatchProcessingMgrFPOS.batchUpdate();
/*      */           
/*      */ 
/* 2006 */           updateBatchCount = 0;
/*      */         }
/*      */       }
/*      */       
/* 2010 */       if (this.mBatchProcessingMgrFPOS.hasPendingExecute()) {
/* 2011 */         this.mBatchProcessingMgrFPOS.batchUpdate();
/*      */       }
/*      */       
/* 2014 */       this.mTracer.traceMinimumMsg("Updating not deleted records in-report status to NotInReport is done.", new Object[0]);
/*      */     }
/*      */     
/* 2017 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateReceipts(Map<String, Set<String>> recIdReceiptIdSetMap, boolean isMRetainMetaDataEnabled)
/*      */   {
/* 2028 */     this.mTracer.traceMethodEntry(new Object[] { recIdReceiptIdSetMap, Boolean.valueOf(isMRetainMetaDataEnabled) });
/*      */     
/* 2030 */     if (recIdReceiptIdSetMap.size() > 0) {
/* 2031 */       this.mTracer.traceMinimumMsg("Start updating related receipts status...", new Object[0]);
/*      */       
/* 2033 */       Map<String, Object> receiptPropertyNameValueMap = new HashMap();
/* 2034 */       receiptPropertyNameValueMap.put("ReceiptStatus", Integer.valueOf(3));
/*      */       
/* 2036 */       if (!isMRetainMetaDataEnabled)
/*      */       {
/* 2038 */         receiptPropertyNameValueMap.put("ReceiptOf", null);
/*      */       }
/*      */       
/* 2041 */       this.mBatchProcessingMgrFPOS.setProcessingObjectClassName("Document");
/* 2042 */       this.mBatchProcessingMgrFPOS.setUpdatingProperties(receiptPropertyNameValueMap);
/*      */       
/*      */ 
/* 2045 */       Set<String> allReceiptIdSet = new HashSet();
/* 2046 */       Set<Map.Entry<String, Set<String>>> mapEntrySet = recIdReceiptIdSetMap.entrySet();
/* 2047 */       for (Map.Entry<String, Set<String>> anEntry : mapEntrySet) {
/* 2048 */         allReceiptIdSet.addAll((Collection)anEntry.getValue());
/*      */       }
/*      */       
/* 2051 */       this.mTracer.traceMinimumMsg("There are total {0} receipts to be updated.", new Object[] { Integer.valueOf(allReceiptIdSet.size()) });
/*      */       
/* 2053 */       int updateBatchCount = 0;
/* 2054 */       for (String aReceiptId : allReceiptIdSet) {
/* 2055 */         this.mBatchProcessingMgrFPOS.addToBatch(aReceiptId, null);
/* 2056 */         updateBatchCount++;
/* 2057 */         if (updateBatchCount == this.mUpdateBatchSize) {
/* 2058 */           this.mBatchProcessingMgrFPOS.batchUpdate();
/*      */           
/*      */ 
/* 2061 */           updateBatchCount = 0;
/*      */         }
/*      */       }
/*      */       
/* 2065 */       if (this.mBatchProcessingMgrFPOS.hasPendingExecute()) {
/* 2066 */         this.mBatchProcessingMgrFPOS.batchUpdate();
/*      */       }
/*      */       
/* 2069 */       this.mTracer.traceMinimumMsg("Updating related receipts status is done.", new Object[0]);
/*      */     }
/*      */     
/* 2072 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void deleteRelatedRMLinks(Set<String> rmLinkIdSet)
/*      */   {
/* 2082 */     this.mTracer.traceMethodEntry(new Object[] { rmLinkIdSet });
/*      */     
/* 2084 */     if (rmLinkIdSet.size() > 0) {
/* 2085 */       this.mTracer.traceMinimumMsg("Start deleting total {0} related RMLink objects...", new Object[] { Integer.valueOf(rmLinkIdSet.size()) });
/*      */       
/*      */ 
/* 2088 */       this.mBatchProcessingMgrFPOS.setUpdatingProperties(null);
/*      */       
/* 2090 */       this.mBatchProcessingMgrFPOS.setProcessingObjectClassName("Link");
/*      */       
/* 2092 */       int deleteBatchCount = 0;
/* 2093 */       for (String aLinkObjId : rmLinkIdSet) {
/* 2094 */         this.mBatchProcessingMgrFPOS.addToBatch(aLinkObjId, null);
/* 2095 */         deleteBatchCount++;
/* 2096 */         if (deleteBatchCount == this.mUpdateBatchSize) {
/* 2097 */           this.mBatchProcessingMgrFPOS.batchDelete();
/*      */           
/*      */ 
/* 2100 */           deleteBatchCount = 0;
/*      */         }
/*      */       }
/*      */       
/* 2104 */       if (this.mBatchProcessingMgrFPOS.hasPendingExecute()) {
/* 2105 */         this.mBatchProcessingMgrFPOS.batchDelete();
/*      */       }
/*      */       
/* 2108 */       this.mTracer.traceMinimumMsg("Deleting related RMLink objects is done.", new Object[0]);
/*      */     }
/*      */     
/* 2111 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 2118 */     java.util.Properties prop = new java.util.Properties();
/*      */     
/*      */     try
/*      */     {
/* 2122 */       prop.load(new FileInputStream("./config.properties"));
/*      */       
/* 2124 */       String serverUrl = prop.getProperty("CEServerURI");
/* 2125 */       String username = prop.getProperty("UserName");
/* 2126 */       String password = prop.getProperty("Password");
/* 2127 */       String fposName = prop.getProperty("FilePlanObjectStore");
/*      */       
/* 2129 */       Map<String, String> configContextMap = new HashMap();
/*      */       
/* 2131 */       configContextMap.put("Uri", serverUrl);
/* 2132 */       configContextMap.put("UserName", username);
/* 2133 */       configContextMap.put("Password", password);
/*      */       
/* 2135 */       configContextMap.put("ThreadCount", prop.getProperty("ThreadCount"));
/* 2136 */       configContextMap.put("ReadBatchSize", prop.getProperty("QueryPageSize"));
/* 2137 */       configContextMap.put("UpdateBatchSize", prop.getProperty("UpdateBatchSize"));
/* 2138 */       configContextMap.put("ContentSizeLimit", prop.getProperty("ContentSizeLimit"));
/* 2139 */       configContextMap.put("LinkCacheSizeLimit", prop.getProperty("LinkCacheSizeLimit"));
/* 2140 */       configContextMap.put("OnHoldContainerCacheSizeLimit", prop.getProperty("OnHoldContainerCacheSizeLimit"));
/*      */       
/* 2142 */       UserContext userCtx = UserContext.get();
/* 2143 */       com.filenet.api.core.Connection jaceConn = com.filenet.api.core.Factory.Connection.getConnection(serverUrl);
/* 2144 */       String stanza = "FileNetP8";
/* 2145 */       javax.security.auth.Subject jaceSubject = UserContext.createSubject(jaceConn, username, password, stanza);
/*      */       
/* 2147 */       userCtx.pushSubject(jaceSubject);
/*      */       
/* 2149 */       Domain jaceDomain = com.filenet.api.core.Factory.Domain.fetchInstance(jaceConn, null, null);
/*      */       
/*      */ 
/* 2152 */       String logLocation = prop.getProperty("LogLocation");
/*      */       
/* 2154 */       DefensibleDestroy destroyService = new DefensibleDestroy(jaceDomain, fposName, "p8admin", configContextMap, logLocation, null, Level.INFO, BaseTracer.TraceLevel.Minimum);
/*      */       
/*      */ 
/* 2157 */       String reportIDs = prop.getProperty("ReportIDs");
/* 2158 */       String[] reportIDArray = null;
/* 2159 */       if ((reportIDs != null) && (!"".equals(reportIDs.trim()))) {
/* 2160 */         reportIDArray = reportIDs.split(";", -1);
/*      */       }
/*      */       
/* 2163 */       if ((reportIDArray == null) || (reportIDArray.length == 0) || ("".equals(reportIDArray[0]))) {
/* 2164 */         System.out.println("No report ID is provided, no destroy is needed, done!");
/*      */       } else {
/* 2166 */         for (String reportID : reportIDArray) {
/* 2167 */           System.out.println("Main: start destroying report: " + reportID);
/* 2168 */           destroyService.destroyRecordInReport(reportID);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception ex) {
/* 2173 */       ex.printStackTrace();
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\DefensibleDestroy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */