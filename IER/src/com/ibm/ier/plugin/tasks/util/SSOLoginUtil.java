/*    */ package com.ibm.ier.plugin.tasks.util;
/*    */ 
/*    */ import com.ibm.wsspi.security.auth.callback.WSCallbackHandlerFactory;
/*    */ import javax.security.auth.Subject;
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import javax.security.auth.login.LoginContext;
/*    */ import javax.security.auth.login.LoginException;
/*    */ import weblogic.security.Security;
/*    */ 
/*    */ public class SSOLoginUtil
/*    */ {
/*    */   public static Subject getSubjectForWAS(String ltpaTokenStr) throws LoginException
/*    */   {
/* 14 */     if (ltpaTokenStr != null) {
/* 15 */       byte[] ltpaToken = com.ibm.ws.webservices.engine.encoding.Base64.decode(ltpaTokenStr);
/* 16 */       CallbackHandler h = WSCallbackHandlerFactory.getInstance().getCallbackHandler(ltpaToken);
/* 17 */       LoginContext lc = new LoginContext("WSLogin", h);
/* 18 */       lc.login();
/* 19 */       Subject subject = lc.getSubject();
/* 20 */       return subject;
/*    */     }
/* 22 */     return null;
/*    */   }
/*    */   
/*    */   public static Subject getSubjectForWL() throws LoginException {
/* 26 */     return Security.getCurrentSubject();
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\util\SSOLoginUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */