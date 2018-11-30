/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.constants.DomainType;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.Container;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMProperties;
/*    */ import com.ibm.jarm.api.core.RMFactory.Record;
/*    */ import com.ibm.jarm.api.core.Record;
/*    */ import com.ibm.jarm.api.core.RecordContainer;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CopyRecordService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 29 */     return "ierCopyRecordService";
/*    */   }
/*    */   
/*    */ 
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 36 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */     try
/*    */     {
/* 40 */       String recordId = request.getParameter("ier_recordId");
/* 41 */       String destContainerId = request.getParameter("ier_destinationContainer");
/* 42 */       String docTitle = request.getParameter("template_name");
/* 43 */       String description = request.getParameter("template_desc");
/*    */       
/* 45 */       RMProperties recProps = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/* 46 */       recProps.putStringValue("DocumentTitle", docTitle);
/* 47 */       recProps.putStringValue("RMEntityDescription", description);
/*    */       
/*    */ 
/* 50 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*    */       
/*    */ 
/* 53 */       recordId = recordId.split(",")[2];
/* 54 */       destContainerId = destContainerId.split(",")[2];
/*    */       
/* 56 */       Record record = RMFactory.Record.fetchInstance(fp_repository, recordId, null);
/*    */       
/*    */ 
/* 59 */       Container destContainer = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, destContainerId, null);
/* 60 */       if ((destContainer instanceof RecordContainer))
/*    */       {
/* 62 */         RecordContainer recContainer = (RecordContainer)destContainer;
/* 63 */         record.copy(recContainer, recProps);
/*    */       }
/*    */       else
/*    */       {
/* 67 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_COPYRECORD_INVALID_DEST, new Object[] { destContainer });
/*    */       }
/*    */     }
/*    */     catch (Exception exp) {
/* 71 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_COPYRECORD_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 74 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CopyRecordService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */