/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.ContentRepositoriesListMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.core.ContentRepository;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetAssociatedContentRepositoriesService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 18 */     return "ierGetAssociatedContentRepositories";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator()
/*    */   {
/* 23 */     return new ContentRepositoriesListMediator();
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 28 */     Logger.logEntry(this, "serviceExecute", request);
/* 29 */     FilePlanRepository fpRepo = getFilePlanRepository();
/*    */     
/* 31 */     List<ContentRepository> contentRepoList = fpRepo.getAssociatedContentRepositories();
/* 32 */     ContentRepositoriesListMediator mediator = (ContentRepositoriesListMediator)getMediator();
/* 33 */     mediator.setContentRepositoryList(contentRepoList);
/*    */     
/* 35 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetAssociatedContentRepositoriesService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */