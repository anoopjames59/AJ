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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileRecordService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 25 */     return "ierFileRecordService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 29 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */     try
/*    */     {
/* 33 */       String recordId = request.getParameter("ier_recordId");
/* 34 */       String destContainerId = request.getParameter("ier_destinationContainer");
/*    */       
/*    */ 
/* 37 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*    */       
/*    */ 
/* 40 */       recordId = recordId.split(",")[2];
/* 41 */       destContainerId = destContainerId.split(",")[2];
/*    */       
/* 43 */       Record record = RMFactory.Record.fetchInstance(fp_repository, recordId, null);
/*    */       
/*    */ 
/* 46 */       Container destContainer = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, destContainerId, null);
/* 47 */       if ((destContainer instanceof RecordContainer))
/*    */       {
/* 49 */         RecordContainer recContainer = (RecordContainer)destContainer;
/* 50 */         recContainer.fileRecord(record);
/*    */       }
/*    */       else
/*    */       {
/* 54 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_FILERECORD_INVALID_DEST, new Object[] { destContainer });
/*    */       }
/*    */     }
/*    */     catch (Exception exp) {
/* 58 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_FILERECORD_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 61 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\FileRecordService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */