/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.icm.edc.api.ServiceManager;
/*    */ import com.ibm.icm.edc.api.TaskService;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.TaskManagerUtil;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpSession;
/*    */ 
/*    */ 
/*    */ public class ScheduleAsyncTaskOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 22 */     return new String[] { "/scheduleAsyncTask" };
/*    */   }
/*    */   
/*    */ 
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*    */     throws Exception
/*    */   {
/* 29 */     if (this.baseService.isIERDesktop()) {
/* 30 */       String methodName = "executeAction";
/* 31 */       Logger.logEntry(this, methodName, request);
/*    */       
/* 33 */       JSONObject taskRequest = new JSONObject();
/*    */       
/*    */ 
/* 36 */       JSONObject specificTaskRequest = new JSONObject();
/*    */       
/* 38 */       JSONObject requestContent = getRequestContent();
/*    */       
/*    */ 
/*    */ 
/* 42 */       Enumeration parameterNames = request.getParameterNames();
/* 43 */       while (parameterNames.hasMoreElements()) {
/* 44 */         String parameterName = (String)parameterNames.nextElement();
/* 45 */         specificTaskRequest.put(parameterName, request.getParameter(parameterName));
/*    */       }
/*    */       
/*    */ 
/* 49 */       Map<String, Object> parameters = (Map)request.getAttribute("parameters");
/* 50 */       if (parameters != null) {
/* 51 */         taskRequest.putAll(parameters);
/*    */       }
/*    */       
/* 54 */       Map<String, Object> specificTaskParameters = (Map)request.getAttribute("specificTaskRequest");
/* 55 */       if (specificTaskParameters != null) {
/* 56 */         specificTaskRequest.putAll(specificTaskParameters);
/*    */       }
/* 58 */       ServiceManager taskManager = (ServiceManager)request.getSession(false).getAttribute("taskManagerServiceManager");
/*    */       
/* 60 */       String description = request.getParameter("description");
/* 61 */       String name = request.getParameter("name");
/* 62 */       String handlerClassName = request.getParameter("handlerClassName");
/* 63 */       String parentType = request.getParameter("parent");
/*    */       
/* 65 */       taskRequest.put("name", name);
/* 66 */       taskRequest.put("description", description);
/* 67 */       taskRequest.put("handlerClassName", handlerClassName);
/* 68 */       taskRequest.put("specificTaskRequest", specificTaskRequest);
/* 69 */       taskRequest.put("parent", parentType);
/*    */       
/* 71 */       TaskManagerUtil.setSchedulingInformation(taskRequest, request, getRepository(), requestContent);
/*    */       
/*    */ 
/* 74 */       JSONObject jsonResponse = taskManager.getTaskService().scheduleTask(taskRequest);
/* 75 */       if (Logger.isDebugLogged()) {
/* 76 */         Logger.logDebug(this, "serviceExecute", request, "ScheduleReportService response: " + jsonResponse.serialize());
/*    */       }
/* 78 */       String isRecurring = request.getParameter("isRecurring");
/* 79 */       JSONObject attributes = new JSONObject();
/* 80 */       attributes.put("isRecurring", isRecurring);
/* 81 */       jsonResponse.put("attributes", attributes);
/* 82 */       return jsonResponse;
/*    */     }
/*    */     
/* 85 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\ScheduleAsyncTaskOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */