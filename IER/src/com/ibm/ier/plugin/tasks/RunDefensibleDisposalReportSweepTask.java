/*     */ package com.ibm.ier.plugin.tasks;
/*     */ 
/*     */ import com.ibm.ecm.task.TaskLogger;
/*     */ import com.ibm.ecm.task.entities.Task;
/*     */ import com.ibm.ier.ddcp.ReportGeneration;
/*     */ import com.ibm.ier.logtrace.BaseTracer.TraceLevel;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.tasks.util.TaskUtil;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMFactory.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMDomain;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class RunDefensibleDisposalReportSweepTask
/*     */   extends BaseIERTask
/*     */ {
/*     */   public RunDefensibleDisposalReportSweepTask(Task task, File logDirectory)
/*     */     throws IOException
/*     */   {
/*  35 */     super(task, logDirectory);
/*     */   }
/*     */   
/*     */   public void performBaseTask() throws Throwable
/*     */   {
/*  40 */     String methodName = "performTask";
/*  41 */     String className = RunDefensibleDisposalReportSweepTask.class.getName();
/*     */     
/*  43 */     TaskLogger.fine(className, methodName, "Running basic schedule sweep task");
/*     */     
/*     */ 
/*  46 */     JSONObject taskInfo = JSONObject.parse(this.task.getTaskInfo());
/*  47 */     JSONObject specificTaskRequest = (JSONObject)taskInfo.get("specificTaskRequest");
/*  48 */     String p8RepositoryId = (String)specificTaskRequest.get("ier_p8RepositoryId");
/*     */     
/*     */ 
/*  51 */     String connectionUrl = (String)specificTaskRequest.get("ceEJBURL");
/*  52 */     String username = (String)taskInfo.get("userId");
/*  53 */     DomainConnection connection = RMFactory.DomainConnection.createInstance(DomainType.P8_CE, connectionUrl, null);
/*  54 */     RMDomain domain = RMFactory.RMDomain.fetchInstance(connection, null, null);
/*  55 */     FilePlanRepository fp_repository = RMFactory.FilePlanRepository.fetchInstance(domain, p8RepositoryId, RMPropertyFilter.MinimumPropertySet);
/*     */     
/*  57 */     String containerId = (String)specificTaskRequest.get("ier_containerid");
/*  58 */     String containerName = (String)specificTaskRequest.get("containerName");
/*  59 */     String advancedDays = specificTaskRequest.get("advancedDays").toString();
/*  60 */     String reportOnly = (String)specificTaskRequest.get("reportOnly");
/*  61 */     String connectionPointName = (String)specificTaskRequest.get("connectionPointName");
/*  62 */     String defensibleDisposalWorkflowId = IERUtil.getIdFromDocIdString((String)specificTaskRequest.get("defensibleDisposalWorkflowId"));
/*     */     
/*     */ 
/*  65 */     Map<String, String> configs = new HashMap();
/*  66 */     configs.put("ThreadCount", (String)specificTaskRequest.get("defensibleSweepThreadCount"));
/*  67 */     configs.put("ReadBatchSize", (String)specificTaskRequest.get("defensibleSweepQueryPageSize"));
/*  68 */     configs.put("ContentSizeLimit", (String)specificTaskRequest.get("defensibleSweepContentSizeLimit"));
/*  69 */     configs.put("LinkCacheSizeLimit", (String)specificTaskRequest.get("defensibleSweepLinkCacheSizeLimit"));
/*  70 */     configs.put("OnHoldContainerCacheSizeLimit", (String)specificTaskRequest.get("defensibleSweepOnHoldContainerCacheSize"));
/*  71 */     configs.put("Uri", connectionUrl);
/*  72 */     configs.put("UserName", username);
/*  73 */     configs.put("Password", TaskUtil.getDecryptedPassword(taskInfo));
/*  74 */     configs.put("NeedApproval", (String)specificTaskRequest.get("needApproval"));
/*  75 */     configs.put("RecordContainerID", (String)specificTaskRequest.get("containerToDeclareRecordTo"));
/*  76 */     configs.put("AlwaysDeclareRecord", (String)specificTaskRequest.get("defensibleSweepAlwaysDeclareRecord"));
/*  77 */     configs.put("AlwaysShowDeclareResult", (String)specificTaskRequest.get("defensibleSweepAlwaysShowDeclareResult"));
/*     */     
/*  79 */     java.util.logging.Level level = this.logger.getLevel();
/*  80 */     BaseTracer.TraceLevel traceLevel = null;
/*  81 */     if ((level == java.util.logging.Level.FINE) || (level == java.util.logging.Level.FINER) || (level == java.util.logging.Level.FINEST)) {
/*  82 */       traceLevel = BaseTracer.TraceLevel.Minimum;
/*     */     }
/*  84 */     ReportGeneration reportGeneration = new ReportGeneration(this.logDirectory.getCanonicalPath(), "task_" + this.task.getId(), getLog4JLevel(), traceLevel);
/*  85 */     Set<String> containerIds = null;
/*  86 */     if ((containerId != null) && (!containerId.isEmpty())) {
/*  87 */       String[] ids = containerId.split(",");
/*  88 */       containerIds = new HashSet();
/*  89 */       for (String id : ids) {
/*  90 */         containerIds.add(id);
/*     */       }
/*     */     }
/*     */     
/*  94 */     Map<String, String> reportResults = null;
/*  95 */     if ((this.results.get("taskResultMessage") == null) && (this.results.get("reports") == null)) {
/*  96 */       TaskLogger.fine(className, methodName, "Report results are null");
/*     */       
/*     */ 
/*  99 */       reportResults = reportGeneration.generateRetentionDueReport(p8RepositoryId, containerIds, Integer.parseInt(advancedDays), Boolean.parseBoolean(reportOnly), configs);
/*     */       
/* 101 */       TaskLogger.fine(className, methodName, "Finished generating report results");
/*     */       
/*     */ 
/* 104 */       this.results.put("containerName", containerName);
/* 105 */       this.results.put("ier_containerid", containerId);
/* 106 */       this.results.put("ier_p8RepositoryId", p8RepositoryId);
/* 107 */       this.results.put("p8RepositoryGuid", fp_repository.getObjectIdentity());
/* 108 */       this.results.put("ceEJBURL", connectionUrl);
/*     */       
/*     */ 
/* 111 */       if ((reportResults != null) && (reportResults.size() != 0)) {
/* 112 */         JSONObject documentResults = new JSONObject();
/*     */         
/* 114 */         Iterator<Map.Entry<String, String>> it = reportResults.entrySet().iterator();
/* 115 */         while (it.hasNext()) {
/* 116 */           Map.Entry<String, String> pairs = (Map.Entry)it.next();
/* 117 */           documentResults.put(pairs.getKey(), pairs.getValue());
/*     */         }
/* 119 */         this.results.put("reports", documentResults.serialize());
/* 120 */         if (this.logger.isLoggable(java.util.logging.Level.FINE)) {
/* 121 */           TaskLogger.fine(className, methodName, "Report results: " + this.results.get("reports"));
/*     */         }
/*     */       } else {
/* 124 */         this.results.put("taskResultMessage", MessageResources.getLocalizedMessage("runDefensibleDisposalDDTask.completedNoResults", new Object[0]));
/* 125 */         this.results.put("taskResultMessageKey", "basicScheduleNoRecordsToProcess");
/*     */       }
/*     */       
/*     */ 
/* 129 */       saveTaskInfo();
/*     */ 
/*     */     }
/* 132 */     else if (this.results.get("reports") != null) {
/* 133 */       TaskLogger.fine(className, methodName, "Report results are present.  Obtain the results for launching the workflow.");
/*     */       
/* 135 */       JSONObject documentResults = (JSONObject)this.results.get("reports");
/* 136 */       if (documentResults.size() != 0) {
/* 137 */         reportResults = new HashMap();
/*     */         
/* 139 */         Iterator keys_it = documentResults.keySet().iterator();
/* 140 */         while (keys_it.hasNext()) {
/* 141 */           String key = (String)keys_it.next();
/* 142 */           reportResults.put(key, (String)documentResults.get(key));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 148 */     boolean reportOnlyBoolean = Boolean.parseBoolean(reportOnly);
/* 149 */     TaskLogger.fine(className, methodName, "Launching the workflow if the report only boolean is false:" + reportOnlyBoolean);
/*     */     
/* 151 */     if ((!reportOnlyBoolean) && (reportResults != null) && (reportResults.size() != 0)) {
/* 152 */       reportGeneration.launchDDWorkflows(p8RepositoryId, connectionPointName, defensibleDisposalWorkflowId, Integer.parseInt(advancedDays), reportResults, configs);
/* 153 */       this.results.put("taskResultMessage", MessageResources.getLocalizedMessage("basicScheduleWorkflowSuccessful", new Object[0]));
/* 154 */       this.results.put("taskResultMessageKey", "basicScheduleWorkflowLaunchedSuccess");
/*     */     }
/*     */     
/* 157 */     TaskLogger.fine(className, methodName, "Finished running basic schedule sweep task!");
/*     */   }
/*     */   
/*     */   private org.apache.log4j.Level getLog4JLevel() {
/* 161 */     java.util.logging.Level level = this.logger.getLevel();
/* 162 */     if (level == java.util.logging.Level.SEVERE)
/* 163 */       return org.apache.log4j.Level.ERROR;
/* 164 */     if (level == java.util.logging.Level.WARNING)
/* 165 */       return org.apache.log4j.Level.WARN;
/* 166 */     if (level == java.util.logging.Level.INFO)
/* 167 */       return org.apache.log4j.Level.INFO;
/* 168 */     if ((level == java.util.logging.Level.FINE) || (level == java.util.logging.Level.CONFIG))
/* 169 */       return org.apache.log4j.Level.DEBUG;
/* 170 */     if ((level == java.util.logging.Level.FINER) || (level == java.util.logging.Level.FINEST))
/* 171 */       return org.apache.log4j.Level.TRACE;
/* 172 */     if (level == java.util.logging.Level.OFF) {
/* 173 */       return org.apache.log4j.Level.OFF;
/*     */     }
/* 175 */     return org.apache.log4j.Level.ALL;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\RunDefensibleDisposalReportSweepTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */