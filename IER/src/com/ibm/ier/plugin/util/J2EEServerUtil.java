/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ public class J2EEServerUtil
/*    */ {
/*    */   public static final String TOMCAT = "apache tomcat";
/*    */   public static final String WEBSPHERE = "websphere";
/*    */   public static final String WEBLOGIC = "weblogic";
/*    */   public static final String UNKNOWN = "unknown";
/*    */   
/*    */   public static String getJ2EEAppServerType(HttpServletRequest request)
/*    */   {
/* 15 */     ServletContext context = request.getSession().getServletContext();
/*    */     
/* 17 */     String serverInfo = context == null ? "unknown" : context.getServerInfo().toLowerCase();
/*    */     
/* 19 */     if (serverInfo.indexOf("apache tomcat") >= 0)
/* 20 */       return "apache tomcat";
/* 21 */     if (serverInfo.indexOf("websphere") >= 0)
/* 22 */       return "websphere";
/* 23 */     if (serverInfo.indexOf("weblogic") >= 0) {
/* 24 */       return "weblogic";
/*    */     }
/* 26 */     return "unknown";
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\J2EEServerUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */