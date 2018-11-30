/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.services.pluginServices.SaveSearchTemplateService;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ public class P8AddSearchTemplateOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 19 */     return new String[] { "/p8/addSearchTemplate" };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*    */     throws Exception
/*    */   {
/* 28 */     if (this.baseService.isIERDesktop()) {
/* 29 */       Repository repo = getRepository(getP8RepositoryId());
/* 30 */       if ((repo != null) && ((repo instanceof FilePlanRepository)))
/*    */       {
/*    */ 
/* 33 */         SaveSearchTemplateService action = new SaveSearchTemplateService();
/* 34 */         action.setSkipResponseWriting(true);
/* 35 */         action.execute(this.baseService.getPluginServiceCallbacks(), request, null);
/*    */         
/* 37 */         return action.getMediator().toJSONObject();
/*    */       }
/*    */     }
/*    */     
/* 41 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8AddSearchTemplateOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */