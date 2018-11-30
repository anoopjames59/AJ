/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import filenet.vw.api.VWSession;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PEUtil
/*    */ {
/*    */   public static VWSession getPESession(String connectionPointName, String connectionUri, HttpServletRequest request)
/*    */   {
/* 18 */     VWSession peSession = (VWSession)SessionUtil.getCacheProperty(connectionPointName + connectionUri, request);
/* 19 */     if (peSession == null) {
/* 20 */       if (connectionPointName == null) {
/* 21 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_MISSING_CONN_POINT_NAME, new Object[0]);
/*    */       }
/*    */       try
/*    */       {
/* 25 */         peSession = new VWSession();
/* 26 */         peSession.setBootstrapCEURI(connectionUri);
/* 27 */         peSession.logon(connectionPointName);
/* 28 */         SessionUtil.setCacheProperty(connectionPointName + connectionUri, peSession, request);
/*    */       } catch (Exception ex) {
/* 30 */         throw IERUIRuntimeException.createUIRuntimeException(ex, MessageCode.E_FAILED_PE_SESSION, new Object[] { ex.getLocalizedMessage() });
/*    */       }
/*    */     }
/* 33 */     return peSession;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\PEUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */