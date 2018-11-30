/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.RMChoiceListMediator;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMChoiceList;
/*    */ import com.ibm.jarm.api.meta.RMChoiceList;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetChoiceListService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 17 */     return "ierGetChoiceListService";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator()
/*    */   {
/* 22 */     return new RMChoiceListMediator();
/*    */   }
/*    */   
/*    */ 
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 29 */     FilePlanRepository fpRepo = getFilePlanRepository();
/* 30 */     String choiceListId = request.getParameter("ier_id");
/*    */     
/* 32 */     RMChoiceList choiceList = RMFactory.RMChoiceList.fetchInstance(fpRepo, choiceListId);
/* 33 */     RMChoiceListMediator mediator = (RMChoiceListMediator)getMediator();
/* 34 */     mediator.setChoiceList(choiceList);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetChoiceListService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */