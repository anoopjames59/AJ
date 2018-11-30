/*      */ package com.ibm.ier.ddcp;
/*      */ 
/*      */ import com.filenet.api.constants.RefreshMode;
/*      */ import com.filenet.api.core.BatchItemHandle;
/*      */ import com.filenet.api.core.Connection;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.Factory.Connection;
/*      */ import com.filenet.api.core.Factory.CustomEvent;
/*      */ import com.filenet.api.core.Factory.Domain;
/*      */ import com.filenet.api.core.Factory.ObjectStore;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.core.IndependentlyPersistableObject;
/*      */ import com.filenet.api.core.ObjectReference;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.Subscribable;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.events.CustomEvent;
/*      */ import com.filenet.api.exception.EngineRuntimeException;
/*      */ import com.filenet.api.exception.ExceptionCode;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.filenet.api.util.UserContext;
/*      */ import com.ibm.ier.ddcp.constants.AuditStatus;
/*      */ import com.ibm.ier.ddcp.constants.DDCPConstants;
/*      */ import com.ibm.ier.ddcp.entity.RecordEntity;
/*      */ import com.ibm.ier.ddcp.exception.DDCPErrorCode;
/*      */ import com.ibm.ier.ddcp.exception.DDCPRuntimeException;
/*      */ import com.ibm.ier.ddcp.util.DDCPLString;
/*      */ import com.ibm.ier.ddcp.util.DDCPLogCode;
/*      */ import com.ibm.ier.ddcp.util.DDCPLogger;
/*      */ import com.ibm.ier.ddcp.util.DDCPTracer;
/*      */ import com.ibm.ier.ddcp.util.DDCPTracer.SubSystem;
/*      */ import com.ibm.ier.ddcp.util.DDCPUtil;
/*      */ import java.io.PrintStream;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.CompletionService;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.ExecutorCompletionService;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.security.auth.Subject;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BatchProcessingManager
/*      */ {
/*   60 */   private DDCPLogger mLogger = DDCPLogger.getDDCPLogger(ReportGeneration.class.getName());
/*   61 */   private DDCPTracer mTracer = DDCPTracer.getDDCPTracer(DDCPTracer.SubSystem.Delete);
/*      */   
/*   63 */   private ObjectStore mJaceObjectStore = null;
/*      */   
/*   65 */   private ExecutorService mThreadPool = null;
/*      */   
/*   67 */   private CompletionService<UpdatingBatch> mCompletionService = null;
/*      */   
/*   69 */   private int mThreadCount = 0;
/*      */   
/*   71 */   private List<List<String>> mProcessRecIdListPerThreadList = null;
/*      */   
/*   73 */   private Map<String, Object> mUpdateProperties = null;
/*      */   
/*   75 */   private boolean mIsAuditOn = false;
/*      */   
/*   77 */   private String mReviewer = null;
/*      */   
/*   79 */   private int mThreadIndex = 0;
/*      */   
/*   81 */   private int mTotalEntitiesInBatch = 0;
/*      */   
/*      */ 
/*      */ 
/*   85 */   private Map<String, List<String>> mNotDeletedRecMap = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*   89 */   private Map<String, String> mNotDeletedRecIdReasonMap = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*   93 */   private Map<String, List<String>> mNotFoundRecMap = new HashMap();
/*      */   
/*      */ 
/*   96 */   private Map<String, String> mRecordIdNameMap = new HashMap();
/*      */   
/*   98 */   private Set<String> mAllFailedUpdatingObjIdSet = new HashSet();
/*      */   
/*  100 */   private Subject mSubject = null;
/*      */   
/*      */ 
/*  103 */   private String mProcessingObjectClassName = "Document";
/*      */   
/*  105 */   private int mTotalRetryTimes = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BatchProcessingManager(ObjectStore jaceOS, int threadCount, boolean isAuditOn, String reviewer)
/*      */   {
/*  116 */     this.mIsAuditOn = isAuditOn;
/*      */     
/*  118 */     this.mTracer.traceMinimumMsg("Audit : {0}", new Object[] { Boolean.valueOf(this.mIsAuditOn) });
/*      */     
/*      */ 
/*  121 */     if ((this.mIsAuditOn) && ((reviewer == null) || ("".equals(reviewer.trim())))) {
/*  122 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_REVIEWER_FOR_DELETE_AUDIT, new Object[] { reviewer });
/*      */     }
/*      */     
/*  125 */     this.mReviewer = reviewer;
/*      */     
/*  127 */     this.mJaceObjectStore = jaceOS;
/*      */     
/*  129 */     this.mThreadPool = Executors.newFixedThreadPool(threadCount);
/*  130 */     this.mCompletionService = new ExecutorCompletionService(this.mThreadPool);
/*      */     
/*  132 */     this.mThreadCount = threadCount;
/*  133 */     this.mTracer.traceMinimumMsg("Thread pool size for BatchProcessingManager: {0}", new Object[] { Integer.valueOf(threadCount) });
/*      */     
/*  135 */     this.mProcessRecIdListPerThreadList = new ArrayList(this.mThreadCount);
/*      */     
/*  137 */     UserContext userCtx = UserContext.get();
/*  138 */     this.mSubject = userCtx.getSubject();
/*      */     
/*  140 */     Runtime.getRuntime().addShutdownHook(new Thread() {
/*      */       public void run() {
/*  142 */         BatchProcessingManager.this.mThreadPool.shutdown();
/*      */         try {
/*  144 */           if (!BatchProcessingManager.this.mThreadPool.awaitTermination(600L, TimeUnit.SECONDS)) {
/*  145 */             BatchProcessingManager.this.mTracer.traceMinimumMsg("ThreadPool for BatchProcessingManager did not terminate in the 5 minutes.", new Object[0]);
/*  146 */             List<Runnable> droppedTasks = BatchProcessingManager.this.mThreadPool.shutdownNow();
/*  147 */             BatchProcessingManager.this.mTracer.traceMinimumMsg("ThreadPool for BatchProcessingManager was abruptly shut down. {0} tasks will not be executed.", new Object[] { Integer.valueOf(droppedTasks.size()) });
/*      */           }
/*      */         }
/*      */         catch (InterruptedException ie)
/*      */         {
/*  152 */           BatchProcessingManager.this.mThreadPool.shutdownNow();
/*      */           
/*  154 */           Thread.currentThread().interrupt();
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addToBatch(String entityId, String entityName)
/*      */   {
/*  167 */     this.mTracer.traceMethodEntry(new Object[] { entityId, entityName });
/*  168 */     this.mThreadIndex %= this.mThreadCount;
/*      */     
/*  170 */     List<String> processRecIdListInThisThread = null;
/*  171 */     if ((this.mProcessRecIdListPerThreadList.isEmpty()) || (this.mProcessRecIdListPerThreadList.size() < this.mThreadIndex + 1)) {
/*  172 */       processRecIdListInThisThread = new ArrayList();
/*  173 */       this.mProcessRecIdListPerThreadList.add(this.mThreadIndex, processRecIdListInThisThread);
/*      */     } else {
/*  175 */       processRecIdListInThisThread = (List)this.mProcessRecIdListPerThreadList.get(this.mThreadIndex);
/*      */     }
/*      */     
/*  178 */     processRecIdListInThisThread.add(entityId);
/*      */     
/*  180 */     if ((entityName == null) || ("".equals(entityName.trim()))) {
/*  181 */       entityName = entityId;
/*      */     }
/*  183 */     this.mRecordIdNameMap.put(entityId, entityName);
/*      */     
/*  185 */     this.mThreadIndex += 1;
/*      */     
/*  187 */     this.mTotalEntitiesInBatch += 1;
/*  188 */     this.mTracer.traceMethodExit(new Object[] { Integer.valueOf(this.mTotalEntitiesInBatch) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUpdatingProperties(Map<String, Object> updateProps)
/*      */   {
/*  196 */     this.mUpdateProperties = updateProps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProcessingObjectClassName(String className)
/*      */   {
/*  204 */     this.mProcessingObjectClassName = className;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasPendingExecute()
/*      */   {
/*  212 */     return !this.mRecordIdNameMap.isEmpty();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, Map<String, List<String>>> batchDelete()
/*      */   {
/*  221 */     this.mTracer.traceMethodEntry(new Object[0]);
/*  222 */     if (!hasPendingExecute()) {
/*  223 */       this.mTracer.traceMinimumMsg("The deleting batch list is empty, done!", new Object[0]);
/*      */       
/*  225 */       resetForBatchDeletion();
/*      */       
/*  227 */       return new HashMap(0);
/*      */     }
/*      */     
/*  230 */     if (this.mProcessingObjectClassName == null) {
/*  231 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.NO_PROCESS_CLASSNAME, new Object[0]);
/*      */     }
/*      */     
/*  234 */     String updatingPropertyNames = "";
/*  235 */     if ((this.mUpdateProperties != null) && (!this.mUpdateProperties.isEmpty())) {
/*  236 */       Set<String> updatingPropertyNameSet = this.mUpdateProperties.keySet();
/*  237 */       updatingPropertyNames = DDCPUtil.getEntityIdSetInString(updatingPropertyNameSet);
/*      */     }
/*      */     
/*  240 */     this.mTracer.traceMinimumMsg("Start deleting the batch, thread count : {0}, audit : {1}, class name: {2}, total objects : {3}, updating properties: {4}", new Object[] { Integer.valueOf(this.mThreadCount), Boolean.valueOf(this.mIsAuditOn), this.mProcessingObjectClassName, Integer.valueOf(this.mTotalEntitiesInBatch), updatingPropertyNames });
/*      */     
/*      */ 
/*  243 */     long startBatchTime = System.currentTimeMillis();
/*      */     
/*  245 */     batchDelete(this.mProcessRecIdListPerThreadList);
/*      */     
/*  247 */     if ((this.mIsAuditOn) && (!this.mNotDeletedRecMap.isEmpty())) {
/*  248 */       auditDeletionFailure(this.mNotDeletedRecMap);
/*      */     }
/*      */     
/*  251 */     Map<String, Map<String, List<String>>> allFailedRecIdSeverityReasonMap = new HashMap();
/*  252 */     allFailedRecIdSeverityReasonMap.put("NotDeletedRecMap", this.mNotDeletedRecMap);
/*  253 */     allFailedRecIdSeverityReasonMap.put("NotFoundRecMap", this.mNotFoundRecMap);
/*      */     
/*  255 */     int totalFailedEntities = this.mNotDeletedRecMap.size() + this.mNotFoundRecMap.size();
/*      */     
/*  257 */     this.mTracer.traceMinimumMsg("Successfully deleted {0} objects and failed to delete {1} objects ({2} objects cannot be found) in this batch.", new Object[] { Integer.valueOf(this.mTotalEntitiesInBatch - totalFailedEntities), Integer.valueOf(totalFailedEntities), Integer.valueOf(this.mNotFoundRecMap.size()) });
/*      */     
/*  259 */     this.mLogger.info(DDCPLogCode.DELETE_BATCH_SUMMARY, new Object[] { Integer.valueOf(this.mTotalEntitiesInBatch - totalFailedEntities), Integer.valueOf(totalFailedEntities), Integer.valueOf(this.mNotFoundRecMap.size()) });
/*      */     
/*      */ 
/*  262 */     long endBatchTime = System.currentTimeMillis();
/*  263 */     float durationForBatch = (float)(endBatchTime - startBatchTime) / 1000.0F;
/*  264 */     float throughputForBatch = this.mTotalEntitiesInBatch / durationForBatch;
/*  265 */     this.mTracer.traceMinimumMsg("Time to delete {0} objects in this deleting batch : {1} sec, throughput: {2} obj/sec.", new Object[] { Integer.valueOf(this.mTotalEntitiesInBatch), Float.valueOf(durationForBatch), Float.valueOf(throughputForBatch) });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  270 */     resetForBatchDeletion();
/*  271 */     this.mTracer.traceMethodExit(new Object[] { allFailedRecIdSeverityReasonMap });
/*  272 */     return allFailedRecIdSeverityReasonMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void resetForBatchDeletion()
/*      */   {
/*  279 */     this.mTracer.traceMethodEntry(new Object[0]);
/*      */     
/*  281 */     this.mThreadIndex = 0;
/*  282 */     this.mTotalEntitiesInBatch = 0;
/*  283 */     this.mProcessRecIdListPerThreadList = new ArrayList(this.mThreadCount);
/*  284 */     this.mRecordIdNameMap = new HashMap();
/*  285 */     this.mNotDeletedRecMap = new HashMap();
/*  286 */     this.mNotFoundRecMap = new HashMap();
/*  287 */     this.mNotDeletedRecIdReasonMap = new HashMap();
/*      */     
/*  289 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void stop()
/*      */   {
/*  296 */     this.mTracer.traceMethodEntry(new Object[0]);
/*  297 */     this.mTracer.traceMinimumMsg("calling ThreadPool in BatchProcessingManager to shut down.", new Object[0]);
/*  298 */     this.mThreadPool.shutdown();
/*  299 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> batchUpdate()
/*      */   {
/*  307 */     this.mTracer.traceMethodEntry(new Object[0]);
/*  308 */     if (!hasPendingExecute()) {
/*  309 */       this.mTracer.traceMinimumMsg("The updating batch list is empty, done!", new Object[0]);
/*      */       
/*  311 */       resetForBatchUpdate();
/*      */       
/*  313 */       return new HashSet(0);
/*      */     }
/*      */     
/*  316 */     if (this.mUpdateProperties.isEmpty()) {
/*  317 */       this.mTracer.traceMinimumMsg("There is no updating properties, done!", new Object[0]);
/*      */       
/*  319 */       resetForBatchUpdate();
/*      */       
/*  321 */       return new HashSet(0);
/*      */     }
/*      */     
/*  324 */     Set<String> updatingPropertyNameSet = this.mUpdateProperties.keySet();
/*  325 */     String updatingPropertyNames = DDCPUtil.getEntityIdSetInString(updatingPropertyNameSet);
/*      */     
/*  327 */     this.mTracer.traceMinimumMsg("Start updating the batch, thread count : {0}, class name: {1}, total objects : {2}, updating properties: {3}", new Object[] { Integer.valueOf(this.mThreadCount), this.mProcessingObjectClassName, Integer.valueOf(this.mTotalEntitiesInBatch), updatingPropertyNames });
/*      */     
/*      */ 
/*  330 */     long startBatchTime = System.currentTimeMillis();
/*      */     
/*  332 */     batchUpdate(this.mProcessRecIdListPerThreadList);
/*      */     
/*      */ 
/*  335 */     this.mTracer.traceMinimumMsg("Successfully update {0} records and failed to update {1} records in this batch.", new Object[] { Integer.valueOf(this.mTotalEntitiesInBatch - this.mAllFailedUpdatingObjIdSet.size()), Integer.valueOf(this.mAllFailedUpdatingObjIdSet.size()) });
/*      */     
/*      */ 
/*  338 */     this.mLogger.info(DDCPLogCode.UPDATE_BATCH_SUMMARY, new Object[] { Integer.valueOf(this.mTotalEntitiesInBatch - this.mAllFailedUpdatingObjIdSet.size()), Integer.valueOf(this.mAllFailedUpdatingObjIdSet.size()) });
/*      */     
/*      */ 
/*      */ 
/*  342 */     long endBatchTime = System.currentTimeMillis();
/*  343 */     float durationForBatch = (float)(endBatchTime - startBatchTime) / 1000.0F;
/*  344 */     float throughputForBatch = this.mTotalEntitiesInBatch / durationForBatch;
/*  345 */     this.mTracer.traceMinimumMsg("Time to update {0} records in this batch : {1} sec, throughput: {2} rec/sec.", new Object[] { Integer.valueOf(this.mTotalEntitiesInBatch), Float.valueOf(durationForBatch), Float.valueOf(throughputForBatch) });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  350 */     resetForBatchUpdate();
/*      */     
/*  352 */     this.mTracer.traceMethodExit(new Object[] { this.mAllFailedUpdatingObjIdSet });
/*      */     
/*  354 */     return this.mAllFailedUpdatingObjIdSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void resetForBatchUpdate()
/*      */   {
/*  361 */     this.mTracer.traceMethodEntry(new Object[0]);
/*      */     
/*  363 */     this.mThreadIndex = 0;
/*  364 */     this.mTotalEntitiesInBatch = 0;
/*  365 */     this.mProcessRecIdListPerThreadList = new ArrayList(this.mThreadCount);
/*  366 */     this.mRecordIdNameMap = new HashMap();
/*  367 */     this.mAllFailedUpdatingObjIdSet = new HashSet();
/*      */     
/*  369 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void batchUpdate(List<List<String>> updateObjectIdListPerThreadList)
/*      */   {
/*  377 */     this.mTracer.traceMethodEntry(new Object[] { updateObjectIdListPerThreadList });
/*      */     
/*  379 */     int tasksCount = updateObjectIdListPerThreadList.size();
/*      */     
/*  381 */     for (List<String> updateObjIdListInThisThread : updateObjectIdListPerThreadList) {
/*  382 */       if ((updateObjIdListInThisThread != null) && (!updateObjIdListInThisThread.isEmpty())) {
/*  383 */         BatchUpdater updater = new BatchUpdater(updateObjIdListInThisThread, null);
/*  384 */         this.mCompletionService.submit(updater);
/*      */       }
/*      */     }
/*      */     
/*  388 */     processBatchUpdateResults(tasksCount);
/*      */     
/*  390 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void processBatchUpdateResults(int pendingTasksCount)
/*      */   {
/*  400 */     this.mTracer.traceMethodEntry(new Object[] { Integer.valueOf(pendingTasksCount) });
/*      */     
/*      */ 
/*  403 */     List<List<String>> updateObjectIdListForThreadList = new ArrayList();
/*      */     
/*      */ 
/*  406 */     List<String> sureUpdateObjectIdListForThreadList = new ArrayList();
/*      */     
/*      */ 
/*  409 */     List<String> notSureUpdateObjectIdListForThreadList = new ArrayList();
/*      */     
/*      */     boolean haveMetTheException;
/*  412 */     for (int i = 0; i < pendingTasksCount; i++) {
/*  413 */       UpdatingBatch ceUpdateBatch = null;
/*      */       try {
/*  415 */         ceUpdateBatch = (UpdatingBatch)this.mCompletionService.take().get();
/*      */       } catch (ExecutionException ex) {
/*  417 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.BATCH_EXEC_EXCEPTION_UPDATE, new Object[0]);
/*      */       } catch (InterruptedException iex) {
/*  419 */         throw DDCPRuntimeException.createDDCPRuntimeException(iex, DDCPErrorCode.INTERRUPTED_EXCEPTION_WHILE_WAITING_UPDATE, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*  423 */       if (ceUpdateBatch != null) {
/*  424 */         List<BatchItemHandle> batchItemHandleList = ceUpdateBatch.getBatchItemHandles(null);
/*      */         
/*  426 */         haveMetTheException = false;
/*      */         
/*  428 */         for (BatchItemHandle aHandle : batchItemHandleList) {
/*  429 */           IndependentObject theObj = aHandle.getObject();
/*  430 */           String recID = theObj.getObjectReference().getObjectIdentity();
/*      */           
/*      */ 
/*  433 */           if ((!haveMetTheException) && (aHandle.hasException())) {
/*  434 */             EngineRuntimeException ceEx = aHandle.getException();
/*  435 */             haveMetTheException = true;
/*      */             
/*      */ 
/*  438 */             this.mAllFailedUpdatingObjIdSet.add(recID);
/*      */             
/*      */ 
/*  441 */             this.mLogger.error(DDCPLogCode.FAILED_UPDATE_REC_PROPERTY, ceEx, new Object[] { recID });
/*      */             
/*  443 */             this.mTracer.traceMinimumMsg("Failed to update the record {0} property because of {1}.", new Object[] { recID, ceEx.getMessage() });
/*      */ 
/*      */           }
/*  446 */           else if (!haveMetTheException)
/*      */           {
/*  448 */             sureUpdateObjectIdListForThreadList.add(recID);
/*      */           }
/*      */           else {
/*  451 */             notSureUpdateObjectIdListForThreadList.add(recID);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  459 */     if (!sureUpdateObjectIdListForThreadList.isEmpty()) {
/*  460 */       updateObjectIdListForThreadList.add(sureUpdateObjectIdListForThreadList);
/*      */     }
/*      */     
/*  463 */     if (!notSureUpdateObjectIdListForThreadList.isEmpty()) {
/*  464 */       updateObjectIdListForThreadList.add(notSureUpdateObjectIdListForThreadList);
/*      */     }
/*      */     
/*  467 */     if (!updateObjectIdListForThreadList.isEmpty()) {
/*  468 */       batchUpdate(updateObjectIdListForThreadList);
/*      */     }
/*      */     
/*  471 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private class BatchUpdater
/*      */     implements Callable<UpdatingBatch>
/*      */   {
/*  480 */     private List<String> mUpdateObjIdListInThisThread = null;
/*      */     
/*      */ 
/*      */ 
/*      */     private BatchUpdater()
/*      */     {
/*  486 */       this.mUpdateObjIdListInThisThread = updateObjIdListInThisThread;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public UpdatingBatch call()
/*      */     {
/*  493 */       BatchProcessingManager.this.mTracer.traceMethodEntry(new Object[] { this.mUpdateObjIdListInThisThread });
/*      */       
/*  495 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} starts updaing with {1} items...", new Object[] { Thread.currentThread().getName(), Integer.valueOf(this.mUpdateObjIdListInThisThread.size()) });
/*      */       
/*      */ 
/*  498 */       if (this.mUpdateObjIdListInThisThread.isEmpty()) {
/*  499 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("nothing in the batch, done!", new Object[0]);
/*  500 */         return null;
/*      */       }
/*      */       
/*  503 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/*      */ 
/*  506 */       UserContext userCtx = UserContext.get();
/*  507 */       Subject subject = userCtx.getSubject();
/*  508 */       if (subject == null) {
/*  509 */         userCtx.pushSubject(BatchProcessingManager.this.mSubject);
/*      */       }
/*      */       
/*  512 */       Domain myDomain = BatchProcessingManager.this.mJaceObjectStore.get_Domain();
/*  513 */       UpdatingBatch ceUB = UpdatingBatch.createUpdatingBatchInstance(myDomain, RefreshMode.NO_REFRESH);
/*      */       
/*  515 */       for (String updateObjectID : this.mUpdateObjIdListInThisThread) {
/*  516 */         IndependentlyPersistableObject updateObject = (IndependentlyPersistableObject)BatchProcessingManager.this.mJaceObjectStore.getObject(BatchProcessingManager.this.mProcessingObjectClassName, new Id(updateObjectID));
/*      */         
/*      */ 
/*      */ 
/*  520 */         Set<Map.Entry<String, Object>> updatePropsEntrySet = BatchProcessingManager.this.mUpdateProperties.entrySet();
/*      */         
/*  522 */         for (Map.Entry<String, Object> updatePropsEntry : updatePropsEntrySet) {
/*  523 */           String propertyName = (String)updatePropsEntry.getKey();
/*  524 */           Object propertyValue = updatePropsEntry.getValue();
/*  525 */           updateObject.getProperties().putObjectValue(propertyName, propertyValue);
/*      */         }
/*      */         
/*  528 */         ceUB.add(updateObject, null);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  533 */         ceUB.updateBatch();
/*      */       } catch (EngineRuntimeException ceException) {
/*  535 */         long timeSpot2 = System.currentTimeMillis();
/*  536 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} updating caught CE exception: {1} in {2} ms.", new Object[] { Thread.currentThread().getName(), ceException.getExceptionCode().getErrorId(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*  538 */         BatchProcessingManager.this.mTracer.traceMaximumMsg("Thread {0} updating caught CE exception: {1} in {2} ms.", new Object[] { Thread.currentThread().getName(), ceException.toString(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*      */ 
/*      */ 
/*  542 */         ExceptionCode ceErrorCode = ceException.getExceptionCode();
/*  543 */         if ((ceErrorCode == ExceptionCode.DB_ERROR) || (ceErrorCode == ExceptionCode.E_TRANSACTION_FAILURE))
/*      */         {
/*  545 */           retryUpdating(myDomain, this.mUpdateObjIdListInThisThread, BatchProcessingManager.this.mUpdateProperties, 1);
/*      */         }
/*      */         else {
/*  548 */           BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/*  549 */           return ceUB;
/*      */         }
/*      */       } catch (Exception ex) {
/*  552 */         long timeSpot2 = System.currentTimeMillis();
/*  553 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} updating caught exception: {1} in {2} ms.", new Object[] { Thread.currentThread().getName(), ex.getMessage(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*  555 */         BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/*  556 */         return ceUB;
/*      */       }
/*      */       
/*  559 */       long timeSpot1 = System.currentTimeMillis();
/*  560 */       float totalTime = (float)(timeSpot1 - timeSpot0) / 1000.0F;
/*  561 */       float throughput = this.mUpdateObjIdListInThisThread.size() / totalTime;
/*  562 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} updated {1} entities in {2} sec., throughput: {3} rec/sec.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(this.mUpdateObjIdListInThisThread.size()), Float.valueOf(totalTime), Float.valueOf(throughput) });
/*      */       
/*      */ 
/*      */ 
/*  566 */       BatchProcessingManager.this.mTracer.traceMethodExit(new Object[0]);
/*      */       
/*  568 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private UpdatingBatch retryUpdating(Domain myDomain, List<String> updateObjIdList, Map<String, Object> updatePropertiesNameValueMap, int retryTimes)
/*      */     {
/*  583 */       BatchProcessingManager.this.mTracer.traceMethodEntry(new Object[] { updateObjIdList, updatePropertiesNameValueMap, Integer.valueOf(retryTimes) });
/*      */       
/*  585 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry updaing starts with {2} items...", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), Integer.valueOf(updateObjIdList.size()) });
/*      */       
/*      */ 
/*  588 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/*  590 */       UpdatingBatch ceUB = UpdatingBatch.createUpdatingBatchInstance(myDomain, RefreshMode.NO_REFRESH);
/*      */       
/*  592 */       for (String updateObjectID : updateObjIdList) {
/*  593 */         IndependentlyPersistableObject updateObject = (IndependentlyPersistableObject)BatchProcessingManager.this.mJaceObjectStore.getObject(BatchProcessingManager.this.mProcessingObjectClassName, new Id(updateObjectID));
/*      */         
/*      */ 
/*      */ 
/*  597 */         Set<Map.Entry<String, Object>> updatePropsEntrySet = updatePropertiesNameValueMap.entrySet();
/*      */         
/*  599 */         for (Map.Entry<String, Object> updatePropsEntry : updatePropsEntrySet) {
/*  600 */           String propertyName = (String)updatePropsEntry.getKey();
/*  601 */           Object propertyValue = updatePropsEntry.getValue();
/*  602 */           updateObject.getProperties().putObjectValue(propertyName, propertyValue);
/*      */         }
/*      */         
/*  605 */         ceUB.add(updateObject, null);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  610 */         ceUB.updateBatch();
/*      */       } catch (EngineRuntimeException ceException) {
/*  612 */         long timeSpot2 = System.currentTimeMillis();
/*  613 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry updating still caught CE exception: {2} in {3} ms.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), ceException.getExceptionCode().getErrorId(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*      */ 
/*  616 */         retryTimes++;
/*      */         
/*  618 */         if (retryTimes <= BatchProcessingManager.this.mTotalRetryTimes)
/*      */         {
/*  620 */           ExceptionCode ceErrorCode = ceException.getExceptionCode();
/*  621 */           if ((ceErrorCode == ExceptionCode.DB_ERROR) || (ceErrorCode == ExceptionCode.E_TRANSACTION_FAILURE))
/*      */           {
/*  623 */             retryUpdating(myDomain, updateObjIdList, updatePropertiesNameValueMap, retryTimes);
/*      */           }
/*      */           else {
/*  626 */             BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/*  627 */             return ceUB;
/*      */           }
/*      */         }
/*      */         else {
/*  631 */           BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/*  632 */           return ceUB;
/*      */         }
/*      */       } catch (Exception ex) {
/*  635 */         long timeSpot2 = System.currentTimeMillis();
/*  636 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry updating caught exception: {2} in {3} ms.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), ex.getMessage(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*  638 */         BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/*  639 */         return ceUB;
/*      */       }
/*      */       
/*  642 */       long timeSpot1 = System.currentTimeMillis();
/*  643 */       float totalTime = (float)(timeSpot1 - timeSpot0) / 1000.0F;
/*  644 */       float throughput = updateObjIdList.size() / totalTime;
/*  645 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry successfully updated {2} entities in {3} sec., throughput: {4} rec/sec.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), Integer.valueOf(updateObjIdList.size()), Float.valueOf(totalTime), Float.valueOf(throughput) });
/*      */       
/*      */ 
/*      */ 
/*  649 */       BatchProcessingManager.this.mTracer.traceMethodExit(new Object[0]);
/*      */       
/*  651 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void batchDelete(List<List<String>> deleteRecIdListPerThreadList)
/*      */   {
/*  661 */     this.mTracer.traceMethodEntry(new Object[] { deleteRecIdListPerThreadList });
/*      */     
/*  663 */     int tasksCount = deleteRecIdListPerThreadList.size();
/*      */     
/*  665 */     for (List<String> deleteRecIdListInThisThread : deleteRecIdListPerThreadList) {
/*  666 */       if ((deleteRecIdListInThisThread != null) && (!deleteRecIdListInThisThread.isEmpty())) {
/*  667 */         BatchDeleter deleter = new BatchDeleter(deleteRecIdListInThisThread, null);
/*  668 */         this.mCompletionService.submit(deleter);
/*      */       }
/*      */     }
/*      */     
/*  672 */     processBatchDeleteResults(tasksCount);
/*  673 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void processBatchDeleteResults(int pendingTasksCount)
/*      */   {
/*  683 */     this.mTracer.traceMethodEntry(new Object[] { Integer.valueOf(pendingTasksCount) });
/*      */     
/*  685 */     List<List<String>> deleteRecIdListForThreadList = new ArrayList();
/*      */     
/*      */ 
/*  688 */     List<String> sureDeleteRecIdListForThreadList = new ArrayList();
/*      */     
/*      */ 
/*  691 */     List<String> notSureDeleteRecIdListForThreadList = new ArrayList();
/*      */     
/*      */ 
/*  694 */     for (int i = 0; i < pendingTasksCount; i++) {
/*  695 */       UpdatingBatch ceUpdateBatch = null;
/*      */       try {
/*  697 */         ceUpdateBatch = (UpdatingBatch)this.mCompletionService.take().get();
/*      */       } catch (ExecutionException ex) {
/*  699 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.BATCH_EXEC_EXCEPTION_DELETE, new Object[0]);
/*      */       } catch (InterruptedException iex) {
/*  701 */         throw DDCPRuntimeException.createDDCPRuntimeException(iex, DDCPErrorCode.INTERRUPTED_EXCEPTION_WHILE_WAITING_DELETE, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*  705 */       if (ceUpdateBatch != null) {
/*  706 */         List<BatchItemHandle> batchItemHandleList = ceUpdateBatch.getBatchItemHandles(null);
/*      */         
/*  708 */         boolean haveMetTheException = false;
/*      */         
/*  710 */         for (int j = 0; j < batchItemHandleList.size(); j++) {
/*  711 */           BatchItemHandle aHandle = (BatchItemHandle)batchItemHandleList.get(j);
/*      */           
/*  713 */           IndependentObject theObj = aHandle.getObject();
/*  714 */           String objId = theObj.getObjectReference().getObjectIdentity();
/*      */           
/*      */ 
/*  717 */           if ((!haveMetTheException) && (aHandle.hasException())) {
/*  718 */             EngineRuntimeException ceEx = aHandle.getException();
/*      */             
/*      */ 
/*  721 */             this.mLogger.error(DDCPLogCode.FAILED_DELETE_REC, new Object[] { objId, ceEx.getMessage() });
/*  722 */             this.mTracer.traceMinimumMsg("Failed to delete the object {0} because of {1}.", new Object[] { objId, ceEx.getMessage() });
/*      */             
/*      */ 
/*  725 */             haveMetTheException = true;
/*      */             
/*  727 */             ExceptionCode exceptionCode = ceEx.getExceptionCode();
/*  728 */             String failureReason = ceEx.getLocalizedMessage();
/*      */             
/*  730 */             if ((exceptionCode == ExceptionCode.E_OBJECT_REFERENCED) || (exceptionCode == ExceptionCode.E_ACCESS_DENIED))
/*      */             {
/*      */ 
/*  733 */               this.mNotDeletedRecMap.put(objId, DDCPConstants.SEVERITY_REASON_LIST_RECORD_ON_HOLD);
/*      */             }
/*  735 */             else if (exceptionCode == ExceptionCode.E_OBJECT_NOT_FOUND) {
/*  736 */               this.mNotFoundRecMap.put(objId, DDCPConstants.SEVERITY_REASON_LIST_RECORD_NOT_FOUND);
/*      */             }
/*      */             else {
/*  739 */               List<String> severityReasonList = new ArrayList(2);
/*  740 */               severityReasonList.add(DDCPLString.get("severity.error", "Error"));
/*  741 */               severityReasonList.add(failureReason);
/*      */               
/*  743 */               this.mNotDeletedRecMap.put(objId, severityReasonList);
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*  748 */           else if (!haveMetTheException)
/*      */           {
/*  750 */             sureDeleteRecIdListForThreadList.add(objId);
/*      */           }
/*      */           else {
/*  753 */             notSureDeleteRecIdListForThreadList.add(objId);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  761 */     if (!sureDeleteRecIdListForThreadList.isEmpty()) {
/*  762 */       deleteRecIdListForThreadList.add(sureDeleteRecIdListForThreadList);
/*      */     }
/*      */     
/*  765 */     if (!notSureDeleteRecIdListForThreadList.isEmpty()) {
/*  766 */       deleteRecIdListForThreadList.add(notSureDeleteRecIdListForThreadList);
/*      */     }
/*      */     
/*  769 */     if (!deleteRecIdListForThreadList.isEmpty()) {
/*  770 */       batchDelete(deleteRecIdListForThreadList);
/*      */     }
/*  772 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void raiseAuditEvent(IndependentlyPersistableObject aRec, String recId, boolean isAuditSuccess, String failureReason)
/*      */   {
/*  784 */     this.mTracer.traceMethodEntry(new Object[] { recId, Boolean.valueOf(isAuditSuccess), failureReason });
/*  785 */     String recName = (String)this.mRecordIdNameMap.get(recId);
/*      */     
/*  787 */     CustomEvent jaceEvent = Factory.CustomEvent.createInstance(this.mJaceObjectStore, "RMAudit");
/*  788 */     Properties eventProps = jaceEvent.getProperties();
/*  789 */     eventProps.putValue("AuditActionType", "Delete");
/*      */     
/*  791 */     eventProps.putValue("Reviewer", this.mReviewer);
/*      */     
/*  793 */     if (isAuditSuccess) {
/*  794 */       eventProps.putValue("EventStatus", AuditStatus.Success.getIntValue());
/*      */       
/*  796 */       String delReason = DDCPLString.get("audit.deleteReason", "Deletion performed as a defensible disposal");
/*  797 */       eventProps.putValue("ReasonForAction", delReason);
/*      */       
/*  799 */       String delDescPattern = DDCPLString.get("audit.successfulDelete", "RM Entity {0}, was successfully deleted");
/*  800 */       String description = MessageFormat.format(delDescPattern, new Object[] { recName });
/*  801 */       eventProps.putValue("RMEntityDescription", description);
/*      */     } else {
/*  803 */       eventProps.putValue("EventStatus", AuditStatus.Failure.getIntValue());
/*      */       
/*  805 */       String delReasonPattern = DDCPLString.get("audit.reasonForFailure", "Reason For Failure: {0}");
/*  806 */       String delReason = MessageFormat.format(delReasonPattern, new Object[] { failureReason });
/*      */       
/*      */ 
/*  809 */       if ((delReason != null) && (delReason.length() > 300)) {
/*  810 */         eventProps.putValue("ReasonForAction", delReason.substring(0, 300));
/*      */       } else {
/*  812 */         eventProps.putValue("ReasonForAction", delReason);
/*      */       }
/*      */       
/*  815 */       String delDescPattern = DDCPLString.get("audit.failedDelete", "RM Entity {0}, was not deleted");
/*  816 */       String description = MessageFormat.format(delDescPattern, new Object[] { recName });
/*  817 */       eventProps.putValue("RMEntityDescription", description);
/*      */       
/*  819 */       aRec.clearPendingActions();
/*      */     }
/*      */     
/*  822 */     ((Subscribable)aRec).raiseEvent(jaceEvent);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  827 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void auditDeletionFailure(Map<String, List<String>> notDeletedRecMap)
/*      */   {
/*  835 */     this.mTracer.traceMethodEntry(new Object[] { notDeletedRecMap });
/*  836 */     if (notDeletedRecMap.isEmpty()) {
/*  837 */       this.mTracer.traceMethodExit(new Object[0]);
/*  838 */       return;
/*      */     }
/*      */     
/*      */ 
/*  842 */     List<List<String>> failureRecIdListforEachThreadList = new ArrayList(1);
/*  843 */     List<String> failureRecIdList = new ArrayList();
/*  844 */     failureRecIdListforEachThreadList.add(failureRecIdList);
/*      */     
/*  846 */     Set<Map.Entry<String, List<String>>> failureRecMapEntrySet = notDeletedRecMap.entrySet();
/*  847 */     for (Map.Entry<String, List<String>> failureRecMapEntry : failureRecMapEntrySet) {
/*  848 */       String recId = (String)failureRecMapEntry.getKey();
/*  849 */       failureRecIdList.add(recId);
/*      */       
/*  851 */       String failureReason = (String)((List)failureRecMapEntry.getValue()).get(1);
/*      */       
/*      */ 
/*  854 */       this.mNotDeletedRecIdReasonMap.put(recId, failureReason);
/*      */     }
/*      */     
/*  857 */     batchAudit(failureRecIdListforEachThreadList);
/*      */     
/*  859 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void batchAudit(List<List<String>> auditRecIdListPerThreadList)
/*      */   {
/*  867 */     this.mTracer.traceMethodEntry(new Object[] { auditRecIdListPerThreadList });
/*      */     
/*  869 */     int tasksCount = auditRecIdListPerThreadList.size();
/*      */     
/*  871 */     for (List<String> auditRecIdListInThisThread : auditRecIdListPerThreadList) {
/*  872 */       if ((auditRecIdListInThisThread != null) && (!auditRecIdListInThisThread.isEmpty())) {
/*  873 */         BatchFailureAuditer auditer = new BatchFailureAuditer(auditRecIdListInThisThread, null);
/*  874 */         this.mCompletionService.submit(auditer);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  879 */     processAuditBatchResults(tasksCount);
/*  880 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void processAuditBatchResults(int pendingTasksCount)
/*      */   {
/*  890 */     this.mTracer.traceMethodEntry(new Object[] { Integer.valueOf(pendingTasksCount) });
/*      */     
/*  892 */     List<List<String>> auditRecIdListForThreadList = new ArrayList();
/*      */     
/*  894 */     List<String> sureAuditRecIdListForThreadList = new ArrayList();
/*      */     
/*  896 */     List<String> notSureAuditRecIdListForThreadList = new ArrayList();
/*      */     
/*  898 */     for (int i = 0; i < pendingTasksCount; i++) {
/*  899 */       UpdatingBatch ceUpdateBatch = null;
/*      */       try {
/*  901 */         ceUpdateBatch = (UpdatingBatch)this.mCompletionService.take().get();
/*      */       } catch (ExecutionException ex) {
/*  903 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.BATCH_EXEC_EXCEPTION_AUDIT, new Object[0]);
/*      */       } catch (InterruptedException iex) {
/*  905 */         throw DDCPRuntimeException.createDDCPRuntimeException(iex, DDCPErrorCode.INTERRUPTED_EXCEPTION_WHILE_WAITING_AUDIT, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*  909 */       if (ceUpdateBatch != null) {
/*  910 */         List<BatchItemHandle> batchItemHandleList = ceUpdateBatch.getBatchItemHandles(null);
/*      */         
/*  912 */         boolean haveMetTheException = false;
/*      */         
/*  914 */         for (int j = 0; j < batchItemHandleList.size(); j++) {
/*  915 */           BatchItemHandle aHandle = (BatchItemHandle)batchItemHandleList.get(j);
/*      */           
/*  917 */           IndependentObject theObj = aHandle.getObject();
/*  918 */           String objId = theObj.getObjectReference().getObjectIdentity();
/*      */           
/*      */ 
/*  921 */           if ((!haveMetTheException) && (aHandle.hasException())) {
/*  922 */             EngineRuntimeException ceEx = aHandle.getException();
/*      */             
/*      */ 
/*  925 */             this.mLogger.error(DDCPLogCode.FAILED_AUDIT_REC, ceEx, new Object[] { objId });
/*  926 */             this.mTracer.traceMinimumMsg("Failed to audit the deletion failed object {0} because of {1}.", new Object[] { objId, ceEx.getMessage() });
/*      */             
/*      */ 
/*  929 */             haveMetTheException = true;
/*      */ 
/*      */           }
/*  932 */           else if (!haveMetTheException)
/*      */           {
/*  934 */             sureAuditRecIdListForThreadList.add(objId);
/*      */           }
/*      */           else {
/*  937 */             notSureAuditRecIdListForThreadList.add(objId);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  945 */     if (!sureAuditRecIdListForThreadList.isEmpty()) {
/*  946 */       auditRecIdListForThreadList.add(sureAuditRecIdListForThreadList);
/*      */     }
/*      */     
/*  949 */     if (!notSureAuditRecIdListForThreadList.isEmpty()) {
/*  950 */       auditRecIdListForThreadList.add(notSureAuditRecIdListForThreadList);
/*      */     }
/*      */     
/*  953 */     if (!auditRecIdListForThreadList.isEmpty()) {
/*  954 */       batchAudit(auditRecIdListForThreadList);
/*      */     }
/*      */     
/*  957 */     this.mTracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private class BatchFailureAuditer
/*      */     implements Callable<UpdatingBatch>
/*      */   {
/*  966 */     private List<String> mfailureRecIdListInThisThread = null;
/*      */     
/*      */ 
/*      */ 
/*      */     private BatchFailureAuditer()
/*      */     {
/*  972 */       this.mfailureRecIdListInThisThread = failureRecIdListInThisThread;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public UpdatingBatch call()
/*      */     {
/*  979 */       BatchProcessingManager.this.mTracer.traceMethodEntry(new Object[] { this.mfailureRecIdListInThisThread });
/*  980 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} starts auditing failures with {1} items...", new Object[] { Thread.currentThread().getName(), Integer.valueOf(this.mfailureRecIdListInThisThread.size()) });
/*      */       
/*      */ 
/*  983 */       if (this.mfailureRecIdListInThisThread.isEmpty()) {
/*  984 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("nothing in the batch, done!", new Object[0]);
/*  985 */         return null;
/*      */       }
/*      */       
/*  988 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/*      */ 
/*  991 */       UserContext userCtx = UserContext.get();
/*  992 */       Subject subject = userCtx.getSubject();
/*  993 */       if (subject == null) {
/*  994 */         userCtx.pushSubject(BatchProcessingManager.this.mSubject);
/*      */       }
/*      */       
/*  997 */       Domain myDomain = BatchProcessingManager.this.mJaceObjectStore.get_Domain();
/*  998 */       UpdatingBatch ceUB = UpdatingBatch.createUpdatingBatchInstance(myDomain, RefreshMode.NO_REFRESH);
/*      */       
/* 1000 */       for (String recId : this.mfailureRecIdListInThisThread) {
/* 1001 */         String failureReason = (String)BatchProcessingManager.this.mNotDeletedRecIdReasonMap.get(recId);
/*      */         
/* 1003 */         IndependentlyPersistableObject failedRec = (IndependentlyPersistableObject)BatchProcessingManager.this.mJaceObjectStore.getObject(BatchProcessingManager.this.mProcessingObjectClassName, new Id(recId));
/*      */         
/*      */ 
/*      */ 
/* 1007 */         BatchProcessingManager.this.raiseAuditEvent(failedRec, recId, false, failureReason);
/*      */         
/*      */ 
/* 1010 */         ceUB.add(failedRec, null);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 1015 */         ceUB.updateBatch();
/*      */       } catch (EngineRuntimeException ceException) {
/* 1017 */         long timeSpot2 = System.currentTimeMillis();
/* 1018 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} auditing caught CE exception: {1} in {2} ms.", new Object[] { Thread.currentThread().getName(), ceException.getExceptionCode().getErrorId(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/* 1020 */         BatchProcessingManager.this.mTracer.traceMaximumMsg("Thread {0} auditing caught CE exception: {1} in {2} ms.", new Object[] { Thread.currentThread().getName(), ceException.toString(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*      */ 
/*      */ 
/* 1024 */         ExceptionCode ceErrorCode = ceException.getExceptionCode();
/* 1025 */         if ((ceErrorCode == ExceptionCode.DB_ERROR) || (ceErrorCode == ExceptionCode.E_TRANSACTION_FAILURE))
/*      */         {
/* 1027 */           retryAuditing(myDomain, this.mfailureRecIdListInThisThread, BatchProcessingManager.this.mNotDeletedRecIdReasonMap, 1);
/*      */         }
/*      */         else {
/* 1030 */           BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1031 */           return ceUB;
/*      */         }
/*      */       } catch (Exception ex) {
/* 1034 */         long timeSpot2 = System.currentTimeMillis();
/* 1035 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} caught exception: {1} in {2} ms when auditing failed documents.", new Object[] { Thread.currentThread().getName(), ex.getMessage(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/* 1037 */         BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1038 */         return ceUB;
/*      */       }
/*      */       
/* 1041 */       long timeSpot1 = System.currentTimeMillis();
/* 1042 */       float totalTime = (float)(timeSpot1 - timeSpot0) / 1000.0F;
/* 1043 */       float throughput = this.mfailureRecIdListInThisThread.size() / totalTime;
/* 1044 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} successfully audit failed {1} documents in {2} sec., throughput: {3} rec/sec.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(this.mfailureRecIdListInThisThread.size()), Float.valueOf(totalTime), Float.valueOf(throughput) });
/*      */       
/*      */ 
/* 1047 */       BatchProcessingManager.this.mTracer.traceMethodExit(new Object[0]);
/* 1048 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private UpdatingBatch retryAuditing(Domain myDomain, List<String> auditObjIdList, Map<String, String> notDeletedRecIdReasonMap, int retryTimes)
/*      */     {
/* 1063 */       BatchProcessingManager.this.mTracer.traceMethodEntry(new Object[] { auditObjIdList, notDeletedRecIdReasonMap, Integer.valueOf(retryTimes) });
/*      */       
/* 1065 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry auditing starts with {2} items...", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), Integer.valueOf(auditObjIdList.size()) });
/*      */       
/*      */ 
/* 1068 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/* 1070 */       UpdatingBatch ceUB = UpdatingBatch.createUpdatingBatchInstance(myDomain, RefreshMode.NO_REFRESH);
/*      */       
/* 1072 */       for (String recId : auditObjIdList) {
/* 1073 */         String failureReason = (String)notDeletedRecIdReasonMap.get(recId);
/*      */         
/* 1075 */         IndependentlyPersistableObject failedRec = (IndependentlyPersistableObject)BatchProcessingManager.this.mJaceObjectStore.getObject(BatchProcessingManager.this.mProcessingObjectClassName, new Id(recId));
/*      */         
/*      */ 
/*      */ 
/* 1079 */         BatchProcessingManager.this.raiseAuditEvent(failedRec, recId, false, failureReason);
/*      */         
/*      */ 
/* 1082 */         ceUB.add(failedRec, null);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 1087 */         ceUB.updateBatch();
/*      */       } catch (EngineRuntimeException ceException) {
/* 1089 */         long timeSpot2 = System.currentTimeMillis();
/* 1090 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry auditing still caught CE exception: {2} in {3} ms.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), ceException.getExceptionCode().getErrorId(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*      */ 
/* 1093 */         retryTimes++;
/*      */         
/* 1095 */         if (retryTimes <= BatchProcessingManager.this.mTotalRetryTimes)
/*      */         {
/* 1097 */           ExceptionCode ceErrorCode = ceException.getExceptionCode();
/* 1098 */           if ((ceErrorCode == ExceptionCode.DB_ERROR) || (ceErrorCode == ExceptionCode.E_TRANSACTION_FAILURE))
/*      */           {
/* 1100 */             retryAuditing(myDomain, auditObjIdList, notDeletedRecIdReasonMap, retryTimes);
/*      */           }
/*      */           else {
/* 1103 */             BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1104 */             return ceUB;
/*      */           }
/*      */         }
/*      */         else {
/* 1108 */           BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1109 */           return ceUB;
/*      */         }
/*      */       } catch (Exception ex) {
/* 1112 */         long timeSpot2 = System.currentTimeMillis();
/* 1113 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry updating caught exception: {2} in {3} ms.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), ex.getMessage(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/* 1115 */         BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1116 */         return ceUB;
/*      */       }
/*      */       
/* 1119 */       long timeSpot1 = System.currentTimeMillis();
/* 1120 */       float totalTime = (float)(timeSpot1 - timeSpot0) / 1000.0F;
/* 1121 */       float throughput = auditObjIdList.size() / totalTime;
/* 1122 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry successfully audit {2} entities in {3} sec., throughput: {4} rec/sec.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), Integer.valueOf(auditObjIdList.size()), Float.valueOf(totalTime), Float.valueOf(throughput) });
/*      */       
/*      */ 
/*      */ 
/* 1126 */       BatchProcessingManager.this.mTracer.traceMethodExit(new Object[0]);
/*      */       
/* 1128 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class BatchDeleter
/*      */     implements Callable<UpdatingBatch>
/*      */   {
/* 1139 */     private List<String> mDeleteRecIdListInThisThread = null;
/*      */     
/*      */ 
/*      */ 
/*      */     private BatchDeleter()
/*      */     {
/* 1145 */       this.mDeleteRecIdListInThisThread = deleteRecIdListInThisThread;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public UpdatingBatch call()
/*      */     {
/* 1152 */       BatchProcessingManager.this.mTracer.traceMethodEntry(new Object[] { this.mDeleteRecIdListInThisThread });
/* 1153 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} starts deleting with {1} items...", new Object[] { Thread.currentThread().getName(), Integer.valueOf(this.mDeleteRecIdListInThisThread.size()) });
/*      */       
/*      */ 
/* 1156 */       if (this.mDeleteRecIdListInThisThread.isEmpty()) {
/* 1157 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("nothing in the batch, done!", new Object[0]);
/* 1158 */         return null;
/*      */       }
/*      */       
/* 1161 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/*      */ 
/* 1164 */       UserContext userCtx = UserContext.get();
/* 1165 */       Subject subject = userCtx.getSubject();
/* 1166 */       if (subject == null) {
/* 1167 */         userCtx.pushSubject(BatchProcessingManager.this.mSubject);
/*      */       }
/*      */       
/* 1170 */       Domain myDomain = BatchProcessingManager.this.mJaceObjectStore.get_Domain();
/* 1171 */       UpdatingBatch ceUB = UpdatingBatch.createUpdatingBatchInstance(myDomain, RefreshMode.NO_REFRESH);
/*      */       
/* 1173 */       for (String delRecId : this.mDeleteRecIdListInThisThread) {
/* 1174 */         IndependentlyPersistableObject delRec = (IndependentlyPersistableObject)BatchProcessingManager.this.mJaceObjectStore.getObject(BatchProcessingManager.this.mProcessingObjectClassName, new Id(delRecId));
/*      */         
/*      */ 
/* 1177 */         if ((BatchProcessingManager.this.mUpdateProperties != null) && (!BatchProcessingManager.this.mUpdateProperties.isEmpty()))
/*      */         {
/* 1179 */           Set<Map.Entry<String, Object>> updatePropsEntrySet = BatchProcessingManager.this.mUpdateProperties.entrySet();
/* 1180 */           for (Map.Entry<String, Object> updatePropsEntry : updatePropsEntrySet) {
/* 1181 */             String propertyName = (String)updatePropsEntry.getKey();
/* 1182 */             Object propertyValue = updatePropsEntry.getValue();
/* 1183 */             delRec.getProperties().putObjectValue(propertyName, propertyValue);
/*      */           }
/*      */         }
/*      */         
/* 1187 */         if (BatchProcessingManager.this.mIsAuditOn) {
/*      */           try {
/* 1189 */             BatchProcessingManager.this.raiseAuditEvent(delRec, delRecId, true, null);
/*      */           } catch (EngineRuntimeException ceEx) {
/* 1191 */             ExceptionCode exceptionCode = ceEx.getExceptionCode();
/* 1192 */             if (exceptionCode == ExceptionCode.E_OBJECT_NOT_FOUND)
/*      */             {
/* 1194 */               BatchProcessingManager.this.mLogger.error(DDCPLogCode.FAILED_DELETE_REC, new Object[] { delRecId, ceEx.getMessage() });
/* 1195 */               BatchProcessingManager.this.mTracer.traceMinimumMsg("Failed to delete the object {0} because of {1}.", new Object[] { delRecId, ceEx.getMessage() });
/*      */               
/*      */ 
/* 1198 */               BatchProcessingManager.this.mNotFoundRecMap.put(delRecId, DDCPConstants.SEVERITY_REASON_LIST_RECORD_NOT_FOUND);
/* 1199 */               continue;
/*      */             }
/* 1201 */             BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} caught unexpected exception: {1} when raising DeletionSuccess RM Audit event.", new Object[] { ceEx.getMessage() });
/*      */             
/* 1203 */             BatchProcessingManager.this.mLogger.error(DDCPLogCode.EXCEPTION_IN_AUDIT, ceEx, new Object[] { Thread.currentThread().getName() });
/*      */           }
/*      */           
/*      */         }
/*      */         else
/*      */         {
/* 1209 */           delRec.delete();
/*      */           
/*      */ 
/* 1212 */           ceUB.add(delRec, null);
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1217 */         ceUB.updateBatch();
/*      */       } catch (EngineRuntimeException ceException) {
/* 1219 */         long timeSpot2 = System.currentTimeMillis();
/* 1220 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} deleting caught CE exception: {1} in {2} ms.", new Object[] { Thread.currentThread().getName(), ceException.getExceptionCode().getErrorId(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/* 1222 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} deleting caught CE exception: {1} in {2} ms.", new Object[] { Thread.currentThread().getName(), ceException.toString(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*      */ 
/*      */ 
/* 1226 */         ExceptionCode ceErrorCode = ceException.getExceptionCode();
/* 1227 */         if ((ceErrorCode == ExceptionCode.DB_ERROR) || (ceErrorCode == ExceptionCode.E_TRANSACTION_FAILURE))
/*      */         {
/* 1229 */           retryDeleting(myDomain, this.mDeleteRecIdListInThisThread, BatchProcessingManager.this.mUpdateProperties, BatchProcessingManager.this.mIsAuditOn, 1);
/*      */         }
/*      */         else {
/* 1232 */           BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1233 */           return ceUB;
/*      */         }
/*      */       } catch (Exception ex) {
/* 1236 */         long timeSpot2 = System.currentTimeMillis();
/* 1237 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} caught exception: {1} in {2} ms when deleting.", new Object[] { Thread.currentThread().getName(), ex.getMessage(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/* 1239 */         BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1240 */         return ceUB;
/*      */       }
/*      */       
/* 1243 */       long timeSpot1 = System.currentTimeMillis();
/* 1244 */       float totalTime = (float)(timeSpot1 - timeSpot0) / 1000.0F;
/* 1245 */       float throughput = this.mDeleteRecIdListInThisThread.size() / totalTime;
/* 1246 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} deleted {1} entities in {2} sec., throughput: {3} rec/sec with audit: {4}.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(this.mDeleteRecIdListInThisThread.size()), Float.valueOf(totalTime), Float.valueOf(throughput), Boolean.valueOf(BatchProcessingManager.this.mIsAuditOn) });
/*      */       
/*      */ 
/* 1249 */       BatchProcessingManager.this.mTracer.traceMethodExit(new Object[0]);
/* 1250 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private UpdatingBatch retryDeleting(Domain myDomain, List<String> deleteObjIdList, Map<String, Object> updatePropertiesNameValueMap, boolean isAuditOn, int retryTimes)
/*      */     {
/* 1267 */       BatchProcessingManager.this.mTracer.traceMethodEntry(new Object[] { deleteObjIdList, updatePropertiesNameValueMap, Boolean.valueOf(isAuditOn), Integer.valueOf(retryTimes) });
/*      */       
/* 1269 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry deleting starts with {2} items...", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), Integer.valueOf(deleteObjIdList.size()) });
/*      */       
/*      */ 
/* 1272 */       long timeSpot0 = System.currentTimeMillis();
/*      */       
/* 1274 */       UpdatingBatch ceUB = UpdatingBatch.createUpdatingBatchInstance(myDomain, RefreshMode.NO_REFRESH);
/*      */       
/* 1276 */       for (String delRecId : deleteObjIdList) {
/* 1277 */         IndependentlyPersistableObject delRec = (IndependentlyPersistableObject)BatchProcessingManager.this.mJaceObjectStore.getObject(BatchProcessingManager.this.mProcessingObjectClassName, new Id(delRecId));
/*      */         
/*      */ 
/* 1280 */         if ((updatePropertiesNameValueMap != null) && (!updatePropertiesNameValueMap.isEmpty()))
/*      */         {
/* 1282 */           Set<Map.Entry<String, Object>> updatePropsEntrySet = updatePropertiesNameValueMap.entrySet();
/* 1283 */           for (Map.Entry<String, Object> updatePropsEntry : updatePropsEntrySet) {
/* 1284 */             String propertyName = (String)updatePropsEntry.getKey();
/* 1285 */             Object propertyValue = updatePropsEntry.getValue();
/* 1286 */             delRec.getProperties().putObjectValue(propertyName, propertyValue);
/*      */           }
/*      */         }
/*      */         
/* 1290 */         if (isAuditOn) {
/*      */           try {
/* 1292 */             BatchProcessingManager.this.raiseAuditEvent(delRec, delRecId, true, null);
/*      */           } catch (EngineRuntimeException ceEx) {
/* 1294 */             ExceptionCode exceptionCode = ceEx.getExceptionCode();
/* 1295 */             if (exceptionCode == ExceptionCode.E_OBJECT_NOT_FOUND)
/*      */             {
/* 1297 */               BatchProcessingManager.this.mLogger.error(DDCPLogCode.FAILED_DELETE_REC, new Object[] { delRecId, ceEx.getMessage() });
/* 1298 */               BatchProcessingManager.this.mTracer.traceMinimumMsg("Failed to delete the object {0} because of {1}.", new Object[] { delRecId, ceEx.getMessage() });
/*      */               
/*      */ 
/* 1301 */               BatchProcessingManager.this.mNotFoundRecMap.put(delRecId, DDCPConstants.SEVERITY_REASON_LIST_RECORD_NOT_FOUND);
/* 1302 */               continue;
/*      */             }
/* 1304 */             BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} caught unexpected exception: {1} when raising DeletionSuccess RM Audit event.", new Object[] { ceEx.getMessage() });
/*      */             
/* 1306 */             BatchProcessingManager.this.mLogger.error(DDCPLogCode.EXCEPTION_IN_AUDIT, ceEx, new Object[] { Thread.currentThread().getName() });
/*      */           }
/*      */           
/*      */         }
/*      */         else
/*      */         {
/* 1312 */           delRec.delete();
/*      */           
/*      */ 
/* 1315 */           ceUB.add(delRec, null);
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1320 */         ceUB.updateBatch();
/*      */       } catch (EngineRuntimeException ceException) {
/* 1322 */         long timeSpot2 = System.currentTimeMillis();
/* 1323 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry deleting still caught CE exception: {2} in {3} ms.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), ceException.getExceptionCode().getErrorId(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/*      */ 
/* 1326 */         retryTimes++;
/*      */         
/* 1328 */         if (retryTimes <= BatchProcessingManager.this.mTotalRetryTimes)
/*      */         {
/* 1330 */           ExceptionCode ceErrorCode = ceException.getExceptionCode();
/* 1331 */           if ((ceErrorCode == ExceptionCode.DB_ERROR) || (ceErrorCode == ExceptionCode.E_TRANSACTION_FAILURE))
/*      */           {
/* 1333 */             retryDeleting(myDomain, deleteObjIdList, updatePropertiesNameValueMap, isAuditOn, retryTimes);
/*      */           }
/*      */           else {
/* 1336 */             BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1337 */             return ceUB;
/*      */           }
/*      */         }
/*      */         else {
/* 1341 */           BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1342 */           return ceUB;
/*      */         }
/*      */       } catch (Exception ex) {
/* 1345 */         long timeSpot2 = System.currentTimeMillis();
/* 1346 */         BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry deleting caught exception: {2} in {3} ms.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), ex.getMessage(), Long.valueOf(timeSpot2 - timeSpot0) });
/*      */         
/* 1348 */         BatchProcessingManager.this.mTracer.traceMethodExit(new Object[] { ceUB });
/* 1349 */         return ceUB;
/*      */       }
/*      */       
/* 1352 */       long timeSpot1 = System.currentTimeMillis();
/* 1353 */       float totalTime = (float)(timeSpot1 - timeSpot0) / 1000.0F;
/* 1354 */       float throughput = deleteObjIdList.size() / totalTime;
/* 1355 */       BatchProcessingManager.this.mTracer.traceMinimumMsg("Thread {0} for {1} time retry successfully deleted {2} entities in {3} sec., throughput: {4} rec/sec.", new Object[] { Thread.currentThread().getName(), Integer.valueOf(retryTimes), Integer.valueOf(deleteObjIdList.size()), Float.valueOf(totalTime), Float.valueOf(throughput) });
/*      */       
/*      */ 
/*      */ 
/* 1359 */       BatchProcessingManager.this.mTracer.traceMethodExit(new Object[0]);
/*      */       
/* 1361 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1370 */     String serverUrl = "http://cm-rm-win07:9080/wsi/FNCEWS40MTOM";
/*      */     try {
/* 1372 */       UserContext userCtx = UserContext.get();
/* 1373 */       Connection jaceConn = Factory.Connection.getConnection(serverUrl);
/* 1374 */       String stanza = "FileNetP8";
/* 1375 */       Subject jaceSubject = UserContext.createSubject(jaceConn, "p8admin", "p8admin", stanza);
/*      */       
/* 1377 */       userCtx.pushSubject(jaceSubject);
/*      */       
/* 1379 */       Domain jaceDomain = Factory.Domain.fetchInstance(jaceConn, null, null);
/*      */       
/* 1381 */       ObjectStore jaceOS = Factory.ObjectStore.fetchInstance(jaceDomain, "LZHOU_FPOS_Base", null);
/*      */       
/*      */ 
/* 1384 */       BatchProcessingManager batchMgr = new BatchProcessingManager(jaceOS, 2, true, "p8admin");
/*      */       
/* 1386 */       RecordEntity rec = new RecordEntity();
/* 1387 */       rec.setRecordID("{46129A14-2B4F-4570-9F4A-4101C756D61A}");
/* 1388 */       rec.setRecordName("SenOn,02/15/12");
/* 1389 */       batchMgr.addToBatch(rec.getRecordID(), rec.getRecordName());
/*      */       
/* 1391 */       rec = new RecordEntity();
/* 1392 */       rec.setRecordID("{D45532E4-79B4-4CAF-BAB6-691566CB6586}");
/* 1393 */       rec.setRecordName("SenOn,03/12/12");
/* 1394 */       batchMgr.addToBatch(rec.getRecordID(), rec.getRecordName());
/*      */       
/* 1396 */       rec = new RecordEntity();
/* 1397 */       rec.setRecordID("{5BABE629-6456-497E-9A5A-86387F98BD98}");
/* 1398 */       rec.setRecordName("SentOn 021012");
/* 1399 */       batchMgr.addToBatch(rec.getRecordID(), rec.getRecordName());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1416 */       Map<String, Map<String, List<String>>> deleteFailureMap = batchMgr.batchDelete();
/* 1417 */       Map<String, List<String>> notDeletedFailureMap = (Map)deleteFailureMap.get("NotDeletedRecMap");
/* 1418 */       System.out.println("Total not deleted recs : " + notDeletedFailureMap.size());
/* 1419 */       Set<Map.Entry<String, List<String>>> entrySet = notDeletedFailureMap.entrySet();
/* 1420 */       for (Map.Entry<String, List<String>> entry : entrySet) {
/* 1421 */         String failedRecId = (String)entry.getKey();
/* 1422 */         List<String> severityReasonList = (List)entry.getValue();
/* 1423 */         System.out.println("Rec ID : " + failedRecId + "\tSeverity : " + (String)severityReasonList.get(0) + "\tReason : " + (String)severityReasonList.get(1));
/*      */       }
/*      */       
/*      */ 
/* 1427 */       Map<String, List<String>> notFoundFailureMap = (Map)deleteFailureMap.get("NotFoundRecMap");
/* 1428 */       System.out.println("Total not found recs : " + notFoundFailureMap.size());
/* 1429 */       entrySet = notFoundFailureMap.entrySet();
/* 1430 */       for (Map.Entry<String, List<String>> entry : entrySet) {
/* 1431 */         String failedRecId = (String)entry.getKey();
/* 1432 */         List<String> severityReasonList = (List)entry.getValue();
/* 1433 */         System.out.println("Rec ID : " + failedRecId + "\tSeverity : " + (String)severityReasonList.get(0) + "\tReason : " + (String)severityReasonList.get(1));
/*      */       }
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1438 */       ex.printStackTrace();
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\BatchProcessingManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */