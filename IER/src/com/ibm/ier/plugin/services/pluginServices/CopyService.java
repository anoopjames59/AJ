/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CopyService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 23 */     return "ierCopyService";
/*    */   }
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
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 40 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */     try
/*    */     {
/* 44 */       String numberOfObjects = request.getParameter("ier_ndocs");
/* 45 */       int ndocs = Integer.parseInt(numberOfObjects);
/*    */       
/* 47 */       getFilePlanRepository();
/* 48 */       for (int i = 0; i < ndocs; i++)
/*    */       {
/* 50 */         IERUtil.getIdFromDocIdString(request.getParameter("ier_docId" + i));
/* 51 */         String objectEntityTypeStr = request.getParameter("ier_entityType" + i);
/* 52 */         EntityType objectEntityType = EntityType.getInstanceFromInt(Integer.parseInt(objectEntityTypeStr));
/* 53 */         switch (objectEntityType)
/*    */         {
/*    */ 
/*    */         }
/*    */         
/*    */       }
/*    */       
/*    */ 
/*    */     }
/*    */     catch (Exception exp)
/*    */     {
/* 64 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_COPY_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 67 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CopyService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */