/*    */ package com.ibm.ier.report.util;
/*    */ 
/*    */ import com.ibm.ier.logtrace.BaseTracer;
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
/*    */ public class RptTracer
/*    */   extends BaseTracer
/*    */ {
/*    */   private static final String RPT_TRACE_PREFIX = "rptTrace";
/*    */   
/*    */   private RptTracer(String traceNamePrefix)
/*    */   {
/* 23 */     super(traceNamePrefix);
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
/*    */   public static RptTracer getRptTracer(SubSystem subSystem)
/*    */   {
/* 37 */     String traceNamePrefix = "rptTrace." + subSystem.getLabel();
/* 38 */     return new RptTracer(traceNamePrefix);
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
/*    */   public static enum SubSystem
/*    */   {
/* 51 */     RptEng("rptEng");
/*    */     
/*    */ 
/*    */     private String label;
/*    */     
/*    */     private SubSystem(String label)
/*    */     {
/* 58 */       this.label = label;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     public String getLabel()
/*    */     {
/* 69 */       return this.label;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\util\RptTracer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */