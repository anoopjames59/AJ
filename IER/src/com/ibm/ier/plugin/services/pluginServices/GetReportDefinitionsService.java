/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.ReportDefinitionListMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.ReportDefinition;
/*    */ import com.ibm.jarm.api.core.ReportDefinition;
/*    */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetReportDefinitionsService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 29 */     return "ierGetReportDefinitions";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator()
/*    */   {
/* 34 */     return new ReportDefinitionListMediator();
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 39 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 41 */     FilePlanRepository fpRepo = getFilePlanRepository();
/* 42 */     String reportId = request.getParameter("ier_reportid");
/*    */     
/* 44 */     if (reportId == null)
/*    */     {
/* 46 */       List<ReportDefinition> rdList = fpRepo.getReportDefinitions(RMPropertyFilter.MinimumPropertySet);
/* 47 */       ReportDefinitionListMediator mediator = (ReportDefinitionListMediator)getMediator();
/* 48 */       mediator.setReportDefinitions(rdList);
/* 49 */       mediator.setGenerateDetailedInfo(false);
/*    */     }
/*    */     else
/*    */     {
/* 53 */       ReportDefinition rd = RMFactory.ReportDefinition.fetchInstance(fpRepo, reportId, RMPropertyFilter.MinimumPropertySet);
/* 54 */       List<ReportDefinition> rdList = new ArrayList();
/* 55 */       rdList.add(rd);
/*    */       
/* 57 */       List<RMPropertyDescription> propDespList = SessionUtil.getPropertyDescriptionList(fpRepo, "ReportHoldings", request);
/* 58 */       ReportDefinitionListMediator mediator = (ReportDefinitionListMediator)getMediator();
/* 59 */       mediator.setReportDefinitions(rdList);
/* 60 */       mediator.setReportPropertyDescriptions(propDespList);
/* 61 */       mediator.setGenerateDetailedInfo(true);
/*    */     }
/*    */     
/* 64 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetReportDefinitionsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */