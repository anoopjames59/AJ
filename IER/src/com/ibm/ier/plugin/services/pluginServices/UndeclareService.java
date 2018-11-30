/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.core.BulkItemResult;
/*    */ import com.ibm.jarm.api.core.BulkOperation;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UndeclareService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 28 */     return "ierUndeclareService";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 40 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */     List<BulkItemResult> bulkResults;
/*    */     try
/*    */     {
/* 45 */       String numberOfObjects = request.getParameter("ier_ndocs");
/* 46 */       int ndocs = Integer.parseInt(numberOfObjects);
/*    */       
/* 48 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*    */       
/*    */ 
/* 51 */       List<String> recordIds = new ArrayList();
/* 52 */       for (int i = 0; i < ndocs; i++)
/*    */       {
/* 54 */         recordIds.add(IERUtil.getIdFromDocIdString(request.getParameter("ier_docId" + i)));
/*    */       }
/*    */       
/* 57 */       bulkResults = BulkOperation.undeclareRecords(fp_repository, recordIds);
/*    */     }
/*    */     catch (Exception exp) {
/* 60 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_UNDECLARE_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/*    */ 
/* 64 */     BulkItemResult itemResult = (BulkItemResult)bulkResults.get(0);
/* 65 */     if (!itemResult.wasSuccessful())
/*    */     {
/* 67 */       RMRuntimeException exp = itemResult.getException();
/* 68 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_UNDECLARE_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 71 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\UndeclareService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */