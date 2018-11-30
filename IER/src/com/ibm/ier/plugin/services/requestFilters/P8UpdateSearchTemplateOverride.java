/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.services.pluginServices.UpdateSearchTemplateService;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ public class P8UpdateSearchTemplateOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 19 */     return new String[] { "/p8/updateSearchTemplate" };
/*    */   }
/*    */   
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*    */     throws Exception
/*    */   {
/* 25 */     if (this.baseService.isIERDesktop()) {
/* 26 */       Repository repo = getRepository(getP8RepositoryId());
/* 27 */       if ((repo != null) && ((repo instanceof FilePlanRepository))) {
/* 28 */         UpdateSearchTemplateService action = new UpdateSearchTemplateService();
/* 29 */         action.setSkipResponseWriting(true);
/* 30 */         action.execute(this.baseService.getPluginServiceCallbacks(), request, null);
/*    */         
/* 32 */         return action.getMediator().toJSONObject();
/*    */       }
/*    */     }
/*    */     
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8UpdateSearchTemplateOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */