/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.RepositoryAttributesMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
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
/*    */ public class GetRepositoryAttributesService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 26 */     return "ierGetRepositoryAttributes";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator()
/*    */   {
/* 31 */     return new RepositoryAttributesMediator();
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 35 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 37 */     String repositoryIds = request.getParameter("ier_repositoryIds");
/* 38 */     String idType = request.getParameter("ier_repositoryIdType");
/* 39 */     RepositoryAttributesMediator mediator = (RepositoryAttributesMediator)getMediator();
/*    */     
/* 41 */     if (Logger.isDebugLogged()) {
/* 42 */       Logger.logDebug(this, "serviceExecute", request, "RepositoryIds: " + repositoryIds + " " + "IdType:" + idType + " ");
/*    */     }
/*    */     
/* 45 */     String[] repositoryIdArray = null;
/* 46 */     if (repositoryIds == null) {
/* 47 */       repositoryIdArray = new String[1];
/* 48 */       if ((idType != null) && (idType.equals("p8"))) {
/* 49 */         repositoryIdArray[0] = getP8RepositoryId();
/*    */       } else {
/* 51 */         repositoryIdArray[0] = getNexusRepositoryId();
/*    */       }
/* 53 */       if (Logger.isDebugLogged()) {
/* 54 */         Logger.logDebug(this, "serviceExecute", request, "Single RepositoryId used: " + repositoryIdArray[0]);
/*    */       }
/*    */     }
/*    */     else {
/* 58 */       repositoryIdArray = repositoryIds.split(",");
/* 59 */       mediator.setRetrieveFileplans(false);
/*    */     }
/*    */     
/*    */ 
/* 63 */     mediator.setRepositoryIds(repositoryIdArray);
/*    */     
/* 65 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetRepositoryAttributesService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */