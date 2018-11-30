/*    */ package com.ibm.ier.plugin.services.responseFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseResponseFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.services.pluginServices.OpenSearchTemplateService;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8OpenSearchTemplateOverride
/*    */   extends IERBaseResponseFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 20 */     return new String[] { "/p8/openSearchTemplate" };
/*    */   }
/*    */   
/*    */   public void filterServiceExecute(HttpServletRequest request, JSONObject jsonResponse) throws Exception
/*    */   {
/*    */     try {
/* 26 */       if (this.baseService.isIERDesktop()) {
/* 27 */         Repository repo = getRepository(getP8RepositoryId());
/* 28 */         if ((repo != null) && ((repo instanceof FilePlanRepository))) {
/* 29 */           OpenSearchTemplateService action = new OpenSearchTemplateService(jsonResponse);
/* 30 */           action.setSkipResponseWriting(true);
/* 31 */           action.execute(this.baseService.getPluginServiceCallbacks(), request, null);
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Exception exp) {
/* 36 */       Logger.logError(this, "filterserviceExecute", request, exp);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\responseFilters\P8OpenSearchTemplateOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */