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
/*    */ import com.ibm.jarm.api.core.RMFactory.Record;
/*    */ import com.ibm.jarm.api.core.Record;
/*    */ import com.ibm.jarm.api.core.RecordContainer;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class MoveRecordService extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 21 */     return "ierMoveRecordService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 25 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */     try
/*    */     {
/* 29 */       String recordId = request.getParameter("ier_recordId");
/* 30 */       String srcContainerId = request.getParameter("ier_sourceContainer");
/* 31 */       String destContainerId = request.getParameter("ier_destinationContainer");
/* 32 */       String reason = request.getParameter("ier_reasonformove");
/*    */       
/*    */ 
/* 35 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*    */       
/*    */ 
/* 38 */       recordId = recordId.split(",")[2];
/* 39 */       srcContainerId = srcContainerId.split(",")[2];
/* 40 */       destContainerId = destContainerId.split(",")[2];
/*    */       
/* 42 */       Record record = RMFactory.Record.fetchInstance(fp_repository, recordId, null);
/*    */       
/*    */ 
/* 45 */       Container srcContainer = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, srcContainerId, null);
/* 46 */       Container destContainer = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, destContainerId, null);
/* 47 */       if ((destContainer instanceof RecordContainer))
/*    */       {
/* 49 */         record.move((RecordContainer)srcContainer, (RecordContainer)destContainer, reason);
/*    */       }
/*    */       else
/*    */       {
/* 53 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_MOVERECORD_UNEXPECTED, new Object[] { destContainer });
/*    */       }
/*    */     }
/*    */     catch (Exception exp)
/*    */     {
/* 58 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_MOVERECORD_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 61 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\MoveRecordService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */