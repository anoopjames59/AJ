/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.ibm.ecm.configuration.SettingsConfig;
/*     */ import com.ibm.icm.edc.api.ServiceManager;
/*     */ import com.ibm.icm.edc.api.TaskService;
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.configuration.DesktopConfig;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.TaskManagerUtil;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Enumeration;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScheduleReportService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  62 */     return "ierScheduleReportService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  66 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  69 */       SettingsConfig settings = Config.getSettingsConfig("navigator");
/*  70 */       String taskManagerURL = settings.getTaskManagerServiceURL();
/*     */       
/*  72 */       DesktopConfig config = Config.getDesktopConfig(request.getParameter("desktop"));
/*  73 */       if ((config == null) || (IERUtil.isNullOrEmpty(config.getCognosReportGatewayServer())) || (IERUtil.isNullOrEmpty(config.getReportEngineDataSource())) || (IERUtil.isNullOrEmpty(config.getCognosReportDispatchServletServer())) || (IERUtil.isNullOrEmpty(taskManagerURL)))
/*     */       {
/*  75 */         Logger.logDebug(this, "serviceExecute", config.getCognosReportGatewayServer() + ":" + config.getReportEngineDataSource() + ":" + config.getCognosReportDispatchServletServer() + ":" + taskManagerURL + ":" + config.getCognosNamespace());
/*     */         
/*     */ 
/*  78 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_REPORTS_CONFIG_MISSING, new Object[0]);
/*     */       }
/*     */       
/*  81 */       JSONObject taskRequest = new JSONObject();
/*     */       
/*     */ 
/*  84 */       JSONObject requestContent = getRequestContent();
/*  85 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*  86 */       JSONArray archiveCriterias = (JSONArray)requestContent.get("archive_criterias");
/*  87 */       JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/*     */       
/*  89 */       JSONObject reportTaskRequest = new JSONObject();
/*  90 */       reportTaskRequest.put("JDBCReportDBDataSource", config.getReportEngineDataSource());
/*  91 */       reportTaskRequest.put("CognosReportServletDispatchURL", config.getCognosReportDispatchServletServer());
/*  92 */       reportTaskRequest.put("CognosReportPath", config.getCognosReportPath());
/*  93 */       reportTaskRequest.put("CognosNamespace", config.getCognosNamespace());
/*     */       
/*     */ 
/*  96 */       Enumeration parameterNames = request.getParameterNames();
/*  97 */       while (parameterNames.hasMoreElements()) {
/*  98 */         String parameterName = (String)parameterNames.nextElement();
/*  99 */         reportTaskRequest.put(parameterName, request.getParameter(parameterName));
/*     */       }
/* 101 */       reportTaskRequest.put("criterias", criterias);
/* 102 */       reportTaskRequest.put("archive_criterias", archiveCriterias);
/* 103 */       reportTaskRequest.put("ier_permissions", permissionsJSON);
/* 104 */       reportTaskRequest.put("ier_className", request.getParameter("ier_className"));
/*     */       
/* 106 */       reportTaskRequest.put("saveInFolderConnectionInfo", this.baseService.getP8Connection(request.getParameter("ier_saveInRepository")).getURI());
/*     */       
/* 108 */       ServiceManager taskManager = (ServiceManager)request.getSession(false).getAttribute("taskManagerServiceManager");
/*     */       
/* 110 */       String description = request.getParameter("description");
/* 111 */       String name = request.getParameter("name");
/* 112 */       taskRequest.put("name", name);
/* 113 */       taskRequest.put("description", description);
/* 114 */       taskRequest.put("handlerClassName", "com.ibm.ier.plugin.tasks.RunReportTask");
/* 115 */       taskRequest.put("specificTaskRequest", reportTaskRequest);
/* 116 */       taskRequest.put("parent", "IER");
/*     */       
/* 118 */       TaskManagerUtil.setSchedulingInformation(taskRequest, request, getRepository(), requestContent);
/*     */       
/*     */ 
/* 121 */       JSONObject jsonResponse = taskManager.getTaskService().scheduleTask(taskRequest);
/* 122 */       if (Logger.isDebugLogged()) {
/* 123 */         Logger.logDebug(this, "serviceExecute", request, "ScheduleReportService response: " + jsonResponse.serialize());
/*     */       }
/* 125 */       String isRecurring = request.getParameter("isRecurring");
/* 126 */       JSONObject attributes = new JSONObject();
/* 127 */       attributes.put("isRecurring", isRecurring);
/* 128 */       jsonResponse.put("attributes", attributes);
/* 129 */       setCompletedJSONResponseObject(jsonResponse);
/*     */     }
/*     */     catch (Exception exp) {
/* 132 */       if ((exp instanceof IERUIRuntimeException)) {
/* 133 */         throw exp;
/*     */       }
/* 135 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_REPORTS_FAILD_TO_RUN, new Object[] { exp.getLocalizedMessage() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\ScheduleReportService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */