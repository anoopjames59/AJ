/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.services.pluginServices.EditAttributesService;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ public class P8EditAttributesOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 17 */     return new String[] { "/p8/editAttributes" };
/*    */   }
/*    */   
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest) throws Exception
/*    */   {
/* 22 */     if (getBaseService().isIERDesktop()) {
/* 23 */       EditAttributesService editAttributesService = new EditAttributesService();
/* 24 */       editAttributesService.setSkipResponseWriting(true);
/* 25 */       editAttributesService.execute(getBaseService().getPluginServiceCallbacks(), request, null);
/* 26 */       return editAttributesService.getMediator().toFinalJSONObject();
/*    */     }
/* 28 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8EditAttributesOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */