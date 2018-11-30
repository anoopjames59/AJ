/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.configuration.Config;
/*    */ import com.ibm.ier.plugin.configuration.DesktopConfig;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.ier.plugin.util.JDBCUtil;
/*    */ import com.ibm.ier.report.db.DBDescriptor;
/*    */ import com.ibm.ier.report.service.Report_Services;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeleteReportResultsService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 55 */     return "ierDeleteReportResults";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 59 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 61 */     String tableNameArray = request.getParameter("ier_tableNames");
/* 62 */     String reportResultIds = request.getParameter("ier_reportResultsIds");
/*    */     
/* 64 */     Logger.logInfo(this, "serviceExecute", request, "tableName: " + tableNameArray + " reportResultIds " + reportResultIds);
/*    */     
/* 66 */     if ((tableNameArray != null) && (reportResultIds != null)) {
/* 67 */       DesktopConfig config = Config.getDesktopConfig(request.getParameter("desktop"));
/* 68 */       if ((config == null) || (IERUtil.isNullOrEmpty(config.getCognosReportGatewayServer())) || (IERUtil.isNullOrEmpty(config.getReportEngineDataSource()))) {
/* 69 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_REPORTS_CONFIG_MISSING, new Object[0]);
/*    */       }
/*    */       
/* 72 */       DBDescriptor dbDescriptor = JDBCUtil.connectReportDataSource(config.getReportEngineDataSource(), request);
/*    */       
/* 74 */       Report_Services rptServices = new Report_Services(dbDescriptor);
/* 75 */       String[] reportResults = reportResultIds.split(",");
/* 76 */       String[] tableNames = tableNameArray.split(",");
/* 77 */       for (int i = 0; i < reportResults.length; i++) {
/* 78 */         String reportResult = reportResults[i];
/* 79 */         String tableName = tableNames[i];
/* 80 */         if ((!reportResult.isEmpty()) && (!tableName.isEmpty())) {
/* 81 */           rptServices.deleteReportData(reportResult, tableName);
/* 82 */           Logger.logInfo(this, "serviceExecute", request, "Delete successful for: " + tableName + " and " + reportResultIds);
/*    */         }
/*    */         else {
/* 85 */           Logger.logInfo(this, "serviceExecute", request, "Empty parameters: " + tableName + " and " + reportResultIds);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 90 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\DeleteReportResultsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */