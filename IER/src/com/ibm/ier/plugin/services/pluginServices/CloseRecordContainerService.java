/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.Container;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.RecordContainer;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CloseRecordContainerService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 26 */     return "ierCloseRecordContainerService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 30 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 33 */       String containerId = request.getParameter("ier_containerid");
/* 34 */       String reasonForClose = request.getParameter("ier_reasonforclose");
/*    */       
/* 36 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*    */       
/* 38 */       containerId = containerId.split(",")[2];
/*    */       
/*    */ 
/* 41 */       Container container = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, containerId, null);
/* 42 */       if ((container instanceof RecordContainer))
/*    */       {
/* 44 */         RecordContainer rc = (RecordContainer)container;
/* 45 */         rc.close(reasonForClose);
/*    */       }
/*    */       else {
/* 48 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_CATCLOSE_INVALID_CONTAINER, new Object[] { container.getName() });
/*    */       }
/*    */     } catch (Exception exp) {
/* 51 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_CATCLOSE_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 54 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CloseRecordContainerService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */