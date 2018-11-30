/*    */ package com.ibm.ier.report.util;
/*    */ 
/*    */ import com.ibm.ier.logtrace.BaseLogger;
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
/*    */ public class RptLogger
/*    */   extends BaseLogger
/*    */ {
/*    */   private static final String DEFAULT_BUNDLE_BASENAME = "resources/RptResources";
/*    */   
/*    */   private RptLogger(String loggerName)
/*    */   {
/* 21 */     super(loggerName, "resources/RptResources");
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
/*    */   public static RptLogger getRptLogger(String loggerName)
/*    */   {
/* 34 */     return new RptLogger(loggerName);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\util\RptLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */