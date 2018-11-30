/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.configuration.DesktopConfig;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.JDBCUtil;
/*     */ import com.ibm.ier.plugin.util.ReportUtil;
/*     */ import com.ibm.ier.report.db.DBDescriptor;
/*     */ import com.ibm.ier.report.service.ReportDataResult;
/*     */ import com.ibm.ier.report.service.Report_Services;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ 
/*     */ public class RunReportService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  64 */     return "ierRunReport";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  68 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  71 */       String reportId = request.getParameter("ier_reportid");
/*  72 */       DesktopConfig config = Config.getDesktopConfig(request.getParameter("desktop"));
/*  73 */       if ((config == null) || (IERUtil.isNullOrEmpty(config.getCognosReportGatewayServer())) || (IERUtil.isNullOrEmpty(config.getReportEngineDataSource()))) {
/*  74 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_REPORTS_CONFIG_MISSING, new Object[0]);
/*     */       }
/*     */       
/*  77 */       DBDescriptor dbDescriptor = JDBCUtil.connectReportDataSource(config.getReportEngineDataSource(), request);
/*     */       
/*  79 */       ObjectStore jaceObjectStore = P8CE_Convert.fromJARM(getRepository());
/*     */       
/*     */ 
/*  82 */       JSONObject requestContent = getRequestContent();
/*  83 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*  84 */       Map<String, Object> reportParameters = ReportUtil.getReportParameters(getFilePlanRepository(), criterias);
/*     */       
/*  86 */       Report_Services rptServices = new Report_Services(dbDescriptor);
/*     */       
/*     */ 
/*  89 */       ReportDataResult result = rptServices.generateReportData(reportId, jaceObjectStore, reportParameters, Id.createId().toString());
/*     */       
/*  91 */       Logger.logDebug(this, "serviceExecute", request, "Report job id: " + result.getJobID());
/*  92 */       Logger.logDebug(this, "serviceExecute", request, "Total Database Rows Inserted: " + result.getTotalRowsProcessed());
/*     */       
/*  94 */       JSONObject jsonResults = new JSONObject();
/*  95 */       jsonResults.put("reportResultId", result.getJobID());
/*  96 */       setCompletedJSONResponseObject(jsonResults);
/*     */       
/*  98 */       Logger.logExit(this, "serviceExecute", request);
/*     */     }
/*     */     catch (Exception exp) {
/* 101 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_REPORTS_FAILD_TO_RUN, new Object[] { exp.getLocalizedMessage() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\RunReportService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */