/*    */ package com.ibm.ier.plugin.services.responseFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.configuration.Config;
/*    */ import com.ibm.ier.plugin.configuration.DesktopConfig;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseResponseFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.PrivilegesUtil;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
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
/*    */ public class GetDesktopActionOverride
/*    */   extends IERBaseResponseFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 32 */     return new String[] { "/getDesktop" };
/*    */   }
/*    */   
/*    */   public void filterServiceExecute(HttpServletRequest request, JSONObject jsonResponse) throws Exception
/*    */   {
/* 37 */     Logger.logEntry(this, "filterServiceExecute", request);
/*    */     
/*    */ 
/* 40 */     if (getBaseService().isIERDesktop()) {
/* 41 */       String repositoryId = this.baseService.getNexusRepositoryId();
/* 42 */       if (repositoryId == null) {
/* 43 */         repositoryId = this.baseService.getAuthenticatingRepository();
/*    */       }
/*    */       
/*    */ 
/* 47 */       DesktopConfig config = Config.getDesktopConfig(request.getParameter("desktop"));
/*    */       
/*    */ 
/* 50 */       jsonResponse.put("cognosDispatchServletServerName", config.getCognosReportDispatchServletServer());
/* 51 */       jsonResponse.put("cognosGatewayServerName", config.getCognosReportGatewayServer());
/* 52 */       jsonResponse.put("cognosReportPath", config.getCognosReportPath());
/* 53 */       jsonResponse.put("cognosReportNamespace", config.getCognosNamespace());
/*    */       
/*    */ 
/* 56 */       jsonResponse.put("defensibleSweepContentSizeLimit", config.getDDSweepContentSizeLimit());
/* 57 */       jsonResponse.put("defensibleSweepLinkCacheSizeLimit", config.getDDSweepLinkCacheSizeLimit());
/* 58 */       jsonResponse.put("defensibleSweepOnHoldContainerCacheSize", config.getDDSweepOnHoldCOntainerCacheSize());
/* 59 */       jsonResponse.put("defensibleSweepQueryPageSize", config.getDDSweepQueryPageSize());
/* 60 */       jsonResponse.put("defensibleSweepThreadCount", config.getDDSweepThreadCount());
/* 61 */       jsonResponse.put("defensibleSweepUpdateBatchSize", config.getDDSweepUpdateBatchSize());
/*    */       
/* 63 */       if (this.baseService.getP8Connection(repositoryId) != null) {
/*    */         try {
/* 65 */           Object desktopAttributes = jsonResponse.get("authenticatingRepositoryAttributes");
/* 66 */           JSONObject attributes = null;
/* 67 */           if (desktopAttributes != null) {
/* 68 */             attributes = (JSONObject)desktopAttributes;
/*    */           }
/*    */           else {
/* 71 */             attributes = new JSONObject();
/* 72 */             jsonResponse.put("authenticatingRepositoryAttributes", attributes);
/*    */           }
/*    */           
/* 75 */           Repository repo = this.baseService.getRepositoryFromNexusRepositoryId(repositoryId);
/* 76 */           if ((repo instanceof FilePlanRepository)) {
/* 77 */             FilePlanRepository fp_repo = (FilePlanRepository)repo;
/* 78 */             if (PrivilegesUtil.isRecordsAdministratorAndManager(fp_repo, this.servletRequest)) {
/* 79 */               attributes.put("isRecordsAdministratorAndManager", Boolean.valueOf(true));
/*    */             }
/*    */           }
/*    */         }
/*    */         catch (Exception exp) {
/* 84 */           Logger.logError(this, "serviceExecute", request, exp.getMessage());
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 89 */     Logger.logExit(this, "filterServiceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\responseFilters\GetDesktopActionOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */