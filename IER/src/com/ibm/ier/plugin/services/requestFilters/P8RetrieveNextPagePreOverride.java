/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.services.pluginServices.RetrieveNextPageService;
/*    */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8RetrieveNextPagePreOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 22 */     return new String[] { "/p8/continueQuery" };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*    */     throws Exception
/*    */   {
/* 31 */     Repository repo = getRepository(getP8RepositoryId());
/* 32 */     if ((repo != null) && ((repo instanceof FilePlanRepository))) {
/* 33 */       String continuationData = request.getParameter("continuationData");
/*    */       
/* 35 */       if (continuationData != null)
/*    */       {
/* 37 */         if (P8QueryContinuationData.isIERQuery(continuationData)) {
/* 38 */           P8QueryContinuationData queryData = new P8QueryContinuationData(continuationData, false);
/*    */           
/* 40 */           if ((queryData.folderId != null) && (!queryData.folderId.equals("{0F1E2D3C-4B5A-6978-8796-A5B4C3D2E1F0}")))
/*    */           {
/* 42 */             RetrieveNextPageService action = new RetrieveNextPageService();
/* 43 */             action.setP8QueryContinuationData(queryData);
/*    */             
/* 45 */             action.setPageSize(200);
/* 46 */             action.setSkipResponseWriting(true);
/* 47 */             action.execute(this.baseService.getPluginServiceCallbacks(), request, null);
/*    */             
/* 49 */             return action.getMediator().toFinalJSONObject();
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8RetrieveNextPagePreOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */