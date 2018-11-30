/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.services.pluginServices.OpenContainerService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.Container;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8OpenFolderPreOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   private static final String RECORDS_MANAGEMENT = "Records Management";
/*    */   
/*    */   public String[] getFilteredServices()
/*    */   {
/* 27 */     return new String[] { "/p8/openFolder" };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*    */     throws Exception
/*    */   {
/* 37 */     String folderDocId = request.getParameter("docid");
/* 38 */     String folderId = IERUtil.getIdFromDocIdString(folderDocId);
/* 39 */     if ((folderId != null) && (!folderId.equals("{0F1E2D3C-4B5A-6978-8796-A5B4C3D2E1F0}")))
/*    */     {
/* 41 */       Repository repo = getRepository(getP8RepositoryId());
/* 42 */       if (((repo instanceof FilePlanRepository)) && 
/* 43 */         (folderId != null)) {
/* 44 */         Container container = RMFactory.Container.fetchInstance((FilePlanRepository)repo, EntityType.Container, folderId, RMPropertyFilter.MinimumPropertySet);
/* 45 */         if (container.getPathName().contains("Records Management")) {
/* 46 */           OpenContainerService openContainerService = new OpenContainerService();
/*    */           
/* 48 */           openContainerService.setPageSize(200);
/* 49 */           openContainerService.setSkipResponseWriting(true);
/* 50 */           openContainerService.execute(this.baseService.getPluginServiceCallbacks(), request, null);
/* 51 */           return openContainerService.getMediator().toJSONObject();
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 57 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8OpenFolderPreOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */