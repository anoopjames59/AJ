/*     */ package com.ibm.ier.plugin.tasks;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.ContentElementList;
/*     */ import com.filenet.api.constants.AutoClassify;
/*     */ import com.filenet.api.constants.AutoUniqueName;
/*     */ import com.filenet.api.constants.CheckinType;
/*     */ import com.filenet.api.constants.DefineSecurityParentage;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.ContentTransfer;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.ContentElement;
/*     */ import com.filenet.api.core.Factory.ContentTransfer;
/*     */ import com.filenet.api.core.Factory.Document;
/*     */ import com.filenet.api.core.Factory.Folder;
/*     */ import com.filenet.api.core.Factory.ObjectStore;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*     */ import com.filenet.api.core.UpdatingBatch;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.filenet.api.meta.ClassDescription;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ecm.task.TaskLogger;
/*     */ import com.ibm.ecm.task.entities.Task;
/*     */ import com.ibm.ecm.task.entities.TaskExecutionRecord;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.tasks.util.CognosReportRunner;
/*     */ import com.ibm.ier.plugin.tasks.util.TaskUtil;
/*     */ import com.ibm.ier.plugin.util.IERPermission;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.JDBCUtil;
/*     */ import com.ibm.ier.plugin.util.P8Util;
/*     */ import com.ibm.ier.plugin.util.P8Util.ModifyType;
/*     */ import com.ibm.ier.plugin.util.ReportUtil;
/*     */ import com.ibm.ier.report.service.ReportDataResult;
/*     */ import com.ibm.ier.report.service.Report_Services;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMFactory.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMDomain;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class RunReportTask extends BaseIERTask
/*     */ {
/*     */   public RunReportTask(Task task, File file) throws IOException
/*     */   {
/*  62 */     super(task, file);
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
/*     */   public String getEmailNotificationContent(long status)
/*     */   {
/*  80 */     if (this.task.getNotifyInfo() != null) {
/*  81 */       if (status == 16L)
/*  82 */         return MessageResources.getLocalizedMessage("task.started", new Object[] { this.task.getName() });
/*  83 */       if (status == 128L)
/*  84 */         return MessageResources.getLocalizedMessage("task.failed", new Object[] { this.task.getName() });
/*  85 */       if (status == 32L) {
/*  86 */         JSONObject taskInfo = null;
/*     */         try {
/*  88 */           taskInfo = JSONObject.parse(this.task.getTaskInfo());
/*  89 */           JSONObject reportTaskRequest = (JSONObject)taskInfo.get("specificTaskRequest");
/*  90 */           JSONObject results = this.taskExecutionRecord != null ? JSONObject.parse(this.taskExecutionRecord.getTaskExecutionInfo()) : (JSONObject)taskInfo.get("results");
/*     */           
/*  92 */           String reportName = (String)reportTaskRequest.get("ier_reportName");
/*  93 */           String link = (String)reportTaskRequest.get("rootDownloadLinkURL");
/*  94 */           StringBuilder downloadLink = new StringBuilder(link);
/*  95 */           downloadLink.append("&docid=");
/*  96 */           downloadLink.append(results.get("reportResultDocumentId"));
/*  97 */           downloadLink.append("&template_name=Document&version=released");
/*     */           
/*  99 */           StringBuilder url = new StringBuilder("<a href=\"");
/* 100 */           url.append(downloadLink.toString());
/* 101 */           url.append("\">");
/* 102 */           url.append(reportName);
/* 103 */           url.append("</a>");
/*     */           
/* 105 */           return MessageResources.getLocalizedMessage("reportTask.completed", new Object[] { url.toString() });
/*     */         }
/*     */         catch (Exception exp) {
/* 108 */           return MessageResources.getLocalizedMessage("task.completed", new Object[] { this.task.getName() });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public void performBaseTask() throws Exception
/*     */   {
/* 118 */     String methodName = "performTask";
/* 119 */     String className = RunReportTask.class.getName();
/* 120 */     Logger logger = getLogger();
/*     */     
/*     */ 
/* 123 */     JSONObject taskInfo = JSONObject.parse(this.task.getTaskInfo());
/* 124 */     JSONObject reportTaskRequest = (JSONObject)taskInfo.get("specificTaskRequest");
/* 125 */     String reportId = (String)reportTaskRequest.get("ier_reportid");
/* 126 */     String reportName = (String)reportTaskRequest.get("ier_reportName");
/* 127 */     String p8RepositoryId = (String)reportTaskRequest.get("ier_p8RepositoryId");
/* 128 */     String reportTitle = (String)reportTaskRequest.get("ier_reportTitle");
/* 129 */     String cognosServletDispathURL = (String)reportTaskRequest.get("CognosReportServletDispatchURL");
/* 130 */     String username = (String)taskInfo.get("userId");
/* 131 */     String password = TaskUtil.getDecryptedPassword(taskInfo);
/*     */     
/*     */ 
/* 134 */     String connectionUrl = (String)taskInfo.get("ceEJBURL");
/* 135 */     DomainConnection connection = RMFactory.DomainConnection.createInstance(DomainType.P8_CE, connectionUrl, null);
/* 136 */     RMDomain domain = RMFactory.RMDomain.fetchInstance(connection, null, null);
/* 137 */     FilePlanRepository fp_repository = RMFactory.FilePlanRepository.fetchInstance(domain, p8RepositoryId, RMPropertyFilter.MinimumPropertySet);
/*     */     
/*     */ 
/* 140 */     String jdbcReportSource = (String)reportTaskRequest.get("JDBCReportDBDataSource");
/* 141 */     String initialReportPath = (String)reportTaskRequest.get("CognosReportPath");
/* 142 */     String namespace = (String)reportTaskRequest.get("CognosNamespace");
/* 143 */     com.ibm.ier.report.db.DBDescriptor dbDescriptor = JDBCUtil.connectReportDataSource(jdbcReportSource, null);
/* 144 */     Report_Services rptServices = new Report_Services(dbDescriptor);
/* 145 */     ReportDataResult result = generateReportData(rptServices, fp_repository, taskInfo);
/*     */     
/*     */ 
/* 148 */     if ((initialReportPath == null) || (initialReportPath.isEmpty())) {
/* 149 */       initialReportPath = "/content/folder[@name='IERReport']";
/*     */     }
/* 151 */     String reportPath = initialReportPath + "/package[@name='" + reportName + " package']/report[@name='" + reportName + "']";
/* 152 */     logger.fine("Report path to run: " + reportPath);
/*     */     
/* 154 */     TaskLogger.fine(className, methodName, "Cognos Servlet Dispatch URL: " + cognosServletDispathURL);
/* 155 */     TaskLogger.fine(className, methodName, "Starting the cognos report runner");
/* 156 */     CognosReportRunner runner = new CognosReportRunner(logger, cognosServletDispathURL);
/*     */     
/* 158 */     if (!IERUtil.isNullOrEmpty(namespace)) {
/* 159 */       runner.logon(namespace, username, password);
/*     */     }
/* 161 */     TaskLogger.fine(className, methodName, "Starting to execute report for: " + result.getJobID());
/* 162 */     byte[] pdfReport = runner.executeReport(reportPath, result.getJobID());
/*     */     
/*     */ 
/* 165 */     String saveInFolderDocId = (String)reportTaskRequest.get("ier_saveInFolderLocation");
/* 166 */     String objectStoreId = IERUtil.getObjectStoreFromDocIdString(saveInFolderDocId);
/*     */     
/* 168 */     Domain p8domain = P8CE_Convert.fromJARM(domain);
/* 169 */     ObjectStore os = null;
/* 170 */     if (objectStoreId.equals(fp_repository.getObjectIdentity())) {
/* 171 */       os = P8CE_Convert.fromJARM(fp_repository);
/*     */     }
/*     */     else {
/* 174 */       os = Factory.ObjectStore.fetchInstance(p8domain, objectStoreId, null);
/*     */     }
/*     */     
/* 177 */     UpdatingBatch ub = UpdatingBatch.createUpdatingBatchInstance(p8domain, RefreshMode.REFRESH);
/*     */     
/* 179 */     Id id = Id.createId();
/* 180 */     Document document = createReportOutput(os, ub, reportName, pdfReport, reportTitle, taskInfo, id);
/* 181 */     TaskLogger.fine(className, methodName, "Finished saving the report: " + reportName + " " + id);
/*     */     
/*     */ 
/* 184 */     String folderDocId = IERUtil.getIdFromDocIdString((String)reportTaskRequest.get("ier_saveInFolderLocation"));
/* 185 */     TaskLogger.fine(className, methodName, "Report output filed into " + folderDocId);
/* 186 */     fileReportDocument(folderDocId, os, ub, document, reportTitle, taskInfo);
/* 187 */     ub.updateBatch();
/*     */     
/*     */ 
/* 190 */     rptServices.deleteReportData(result.getJobID(), (String)reportTaskRequest.get("ier_tableName"));
/* 191 */     TaskLogger.fine(className, methodName, "Deleting report data: " + result.getJobID());
/*     */     
/*     */ 
/* 194 */     this.results.put("ier_reportResultsId", result.getJobID());
/* 195 */     this.results.put("ier_reportid", reportId);
/* 196 */     this.results.put("ier_reportName", reportName);
/* 197 */     this.results.put("reportResultDocumentId", IERUtil.getDocId(document.getClassName(), os.get_Id().toString(), id.toString()));
/* 198 */     this.results.put("reportResultRepositorySymbolicName", os.get_SymbolicName());
/* 199 */     this.results.put("reportResultRepositoryGUID", os.get_Id().toString());
/* 200 */     this.results.put("reportResultRepositoryServer", connectionUrl);
/*     */     
/* 202 */     TaskLogger.fine(className, methodName, "Report task completed!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReportDataResult generateReportData(Report_Services rptServices, FilePlanRepository fp_repository, JSONObject taskInfo)
/*     */     throws Exception
/*     */   {
/* 212 */     JSONObject reportTaskRequest = (JSONObject)taskInfo.get("specificTaskRequest");
/* 213 */     JSONArray criterias = (JSONArray)reportTaskRequest.get("criterias");
/* 214 */     String reportId = (String)reportTaskRequest.get("ier_reportid");
/*     */     
/* 216 */     Map<String, Object> rptParams = ReportUtil.getReportParameters(fp_repository, criterias);
/*     */     
/*     */ 
/* 219 */     ReportDataResult result = rptServices.generateReportData(reportId, fp_repository, rptParams, Id.createId().toString());
/* 220 */     this.task.setSuccessCount(Long.valueOf(result.getTotalRowsProcessed()));
/* 221 */     return result;
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
/*     */   public Document createReportOutput(ObjectStore os, UpdatingBatch ub, String reportName, byte[] pdfReport, String reportTitle, JSONObject taskInfo, Id id)
/*     */     throws Exception
/*     */   {
/* 235 */     TaskLogger.fine("RunReportTask", "createReportOutput", "Creating report document in CE:" + id);
/* 236 */     JSONObject reportTaskRequest = (JSONObject)taskInfo.get("specificTaskRequest");
/* 237 */     JSONArray permissionsJSON = (JSONArray)reportTaskRequest.get("ier_permissions");
/* 238 */     AccessPermissionList permissionList = IERPermission.getCEPermissionsFromJSON(permissionsJSON);
/* 239 */     JSONArray criteriasJSONArray = (JSONArray)reportTaskRequest.get("archive_criterias");
/* 240 */     String className = (String)reportTaskRequest.get("ier_className");
/* 241 */     if (className == null) {
/* 242 */       className = "Document";
/*     */     }
/* 244 */     Document document = Factory.Document.createInstance(os, className, id);
/*     */     
/* 246 */     ClassDescription cd = com.filenet.api.core.Factory.ClassDescription.fetchInstance(os, className, null);
/* 247 */     P8Util.setProperties(null, document, criteriasJSONArray, cd, P8Util.ModifyType.ADD);
/* 248 */     document.set_Permissions(permissionList);
/*     */     
/* 250 */     ContentElementList contentList = Factory.ContentElement.createList();
/* 251 */     ContentTransfer tempContent = Factory.ContentTransfer.createInstance();
/* 252 */     tempContent.set_ContentType("application/pdf");
/* 253 */     tempContent.set_RetrievalName(reportName);
/* 254 */     tempContent.setCaptureSource(new java.io.ByteArrayInputStream(pdfReport));
/* 255 */     contentList.add(tempContent);
/* 256 */     document.set_ContentElements(contentList);
/*     */     
/* 258 */     ub.add(document, null);
/*     */     
/*     */ 
/* 261 */     Document updateDoc = Factory.Document.getInstance(os, className, new Id(document.getObjectReference().getObjectIdentity()));
/* 262 */     updateDoc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/* 263 */     Properties properties = updateDoc.getProperties();
/* 264 */     properties.putValue("MimeType", "application/pdf");
/* 265 */     properties.putValue("DocumentTitle", reportTitle);
/* 266 */     ub.add(updateDoc, null);
/* 267 */     return document;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fileReportDocument(String folderDocId, ObjectStore os, UpdatingBatch ub, Document document, String reportTitle, JSONObject taskInfo)
/*     */   {
/* 278 */     Folder folder = Factory.Folder.fetchInstance(os, folderDocId, null);
/*     */     
/*     */ 
/* 281 */     if ((this.taskExecutionRecord != null) && (this.task.getTaskMode().longValue() == 1L)) {
/* 282 */       String folderName = TaskUtil.stripInvalidCharacters(this.task.getName());
/*     */       try
/*     */       {
/* 285 */         Folder taskFolder = folder.createSubFolder(folderName);
/* 286 */         taskFolder.save(RefreshMode.REFRESH);
/*     */         
/* 288 */         folder = taskFolder;
/*     */       }
/*     */       catch (EngineRuntimeException exp)
/*     */       {
/* 292 */         if (exp.getExceptionCode() == ExceptionCode.E_NOT_UNIQUE) {
/* 293 */           folder = Factory.Folder.fetchInstance(os, folder.get_PathName() + "/" + folderName, null);
/*     */         }
/*     */         else {
/* 296 */           throw exp;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 301 */     ReferentialContainmentRelationship rcr = folder.file(document, AutoUniqueName.AUTO_UNIQUE, TaskUtil.stripInvalidCharacters(reportTitle), DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
/*     */     
/*     */ 
/* 304 */     ub.add(rcr, null);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\RunReportTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */