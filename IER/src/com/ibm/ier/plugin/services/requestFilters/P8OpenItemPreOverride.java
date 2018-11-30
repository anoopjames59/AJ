/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.json.java.JSONArtifact;
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
/*    */ public class P8OpenItemPreOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 21 */     return new String[] { "/p8/openItem" };
/*    */   }
/*    */   
/*    */ 
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*    */     throws Exception
/*    */   {
/* 28 */     request.setAttribute("loadAttributeItems", Boolean.valueOf(true));
/*    */     
/*    */ 
/* 31 */     com.ibm.ecm.configuration.RepositoryConfig repositoryConfig = com.ibm.ecm.configuration.Config.getRepositoryConfig(request);
/* 32 */     com.ibm.ier.plugin.configuration.RepositoryConfig ierRepositoryConfig = com.ibm.ier.plugin.configuration.Config.getRepositoryConfig(request.getParameter("repositoryId"));
/*    */     
/* 34 */     if ((repositoryConfig != null) && (ierRepositoryConfig != null)) {
/* 35 */       String[] documentSystemProperties = ierRepositoryConfig.getSystemProperties("RecordInfo");
/* 36 */       if (documentSystemProperties != null) {
/* 37 */         repositoryConfig.setDocumentSystemProperties(documentSystemProperties);
/*    */       }
/* 39 */       String[] folderSystemProperties = ierRepositoryConfig.getSystemProperties("RMFolder");
/* 40 */       if (folderSystemProperties != null) {
/* 41 */         repositoryConfig.setFolderSystemProperties(folderSystemProperties);
/*    */       }
/*    */     }
/* 44 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8OpenItemPreOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */