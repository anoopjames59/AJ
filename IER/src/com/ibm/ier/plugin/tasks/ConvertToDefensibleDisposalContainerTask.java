/*    */ package com.ibm.ier.plugin.tasks;
/*    */ 
/*    */ import com.ibm.ecm.task.TaskLogger;
/*    */ import com.ibm.ecm.task.entities.Task;
/*    */ import com.ibm.ier.plugin.nls.MessageResources;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.DomainType;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMDomain;
/*    */ import com.ibm.jarm.api.core.RMFactory.DomainConnection;
/*    */ import com.ibm.jarm.api.core.RMFactory.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMDomain;
/*    */ import com.ibm.jarm.api.core.RMFactory.RecordCategory;
/*    */ import com.ibm.jarm.api.core.RecordCategory;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ConvertToDefensibleDisposalContainerTask extends BaseIERTask
/*    */ {
/*    */   public ConvertToDefensibleDisposalContainerTask(Task task, File logDirectory) throws IOException
/*    */   {
/* 24 */     super(task, logDirectory);
/*    */   }
/*    */   
/*    */   public void performBaseTask() throws Exception
/*    */   {
/* 29 */     String methodName = "performTask";
/* 30 */     String className = ConvertToDefensibleDisposalContainerTask.class.getName();
/*    */     
/* 32 */     TaskLogger.fine(className, methodName, "Starting conversion to a defensible disposal container");
/*    */     
/*    */ 
/* 35 */     JSONObject taskInfo = JSONObject.parse(this.task.getTaskInfo());
/* 36 */     JSONObject specificTaskRequest = (JSONObject)taskInfo.get("specificTaskRequest");
/* 37 */     String p8RepositoryId = (String)specificTaskRequest.get("ier_p8RepositoryId");
/*    */     
/*    */ 
/* 40 */     String connectionUrl = (String)specificTaskRequest.get("ceEJBURL");
/* 41 */     com.ibm.jarm.api.core.DomainConnection connection = RMFactory.DomainConnection.createInstance(DomainType.P8_CE, connectionUrl, null);
/* 42 */     RMDomain domain = RMFactory.RMDomain.fetchInstance(connection, null, null);
/* 43 */     FilePlanRepository fp_repository = RMFactory.FilePlanRepository.fetchInstance(domain, p8RepositoryId, RMPropertyFilter.MinimumPropertySet);
/*    */     
/* 45 */     String containerDocId = (String)specificTaskRequest.get("ier_containerid");
/* 46 */     String containerId = IERUtil.getIdFromDocIdString(containerDocId);
/* 47 */     String containerName = (String)specificTaskRequest.get("containerName");
/* 48 */     int ddRetentionPeriodYears = Integer.parseInt(specificTaskRequest.get("retentionPeriodYears").toString());
/* 49 */     int ddRetentionPeriodMonths = Integer.parseInt(specificTaskRequest.get("retentionPeriodMonths").toString());
/* 50 */     int ddRetentionPeriodDays = Integer.parseInt(specificTaskRequest.get("retentionPeriodDays").toString());
/* 51 */     String ddRetentionTriggerPropertyName = (String)specificTaskRequest.get("retentionTriggerPropertyName");
/*    */     
/* 53 */     TaskLogger.fine(className, methodName, "ContainerId: " + containerId + " RetentionPeriod: " + ddRetentionPeriodYears + "-" + ddRetentionPeriodMonths + "-" + ddRetentionPeriodDays);
/*    */     
/* 55 */     RecordCategory container = RMFactory.RecordCategory.fetchInstance(fp_repository, containerId, RMPropertyFilter.MinimumPropertySet);
/* 56 */     container.convertToDefensiblyDisposable(ddRetentionTriggerPropertyName, ddRetentionPeriodYears, ddRetentionPeriodMonths, ddRetentionPeriodDays, false);
/*    */     
/*    */ 
/* 59 */     this.results.put("containerName", containerName);
/* 60 */     this.results.put("ier_containerid", containerDocId);
/* 61 */     this.results.put("ier_p8RepositoryId", p8RepositoryId);
/* 62 */     this.results.put("p8RepositoryGuid", fp_repository.getObjectIdentity());
/* 63 */     this.results.put("ceEJBURL", connectionUrl);
/* 64 */     this.results.put("retentionPeriodYears", Integer.valueOf(ddRetentionPeriodYears));
/* 65 */     this.results.put("retentionPeriodMonths", Integer.valueOf(ddRetentionPeriodMonths));
/* 66 */     this.results.put("retentionPeriodDays", Integer.valueOf(ddRetentionPeriodDays));
/* 67 */     this.results.put("retentionTriggerPropertyName", ddRetentionTriggerPropertyName);
/* 68 */     this.results.put("taskResultMessage", MessageResources.getLocalizedMessage("conversionDDTask.completed", new Object[0]));
/*    */     
/* 70 */     TaskLogger.fine(className, methodName, "Conversion to defensible disposal task completed!");
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\ConvertToDefensibleDisposalContainerTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */