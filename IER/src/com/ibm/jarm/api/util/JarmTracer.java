/*    */ package com.ibm.jarm.api.util;
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
/*    */ public class JarmTracer
/*    */   extends BaseTracer
/*    */ {
/*    */   private static final String JARM_TRACE_PREFIX = "jarmTrace";
/*    */   
/*    */   private JarmTracer(String traceNamePrefix)
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
/*    */   public static JarmTracer getJarmTracer(SubSystem subSystem)
/*    */   {
/* 37 */     String traceNamePrefix = "jarmTrace." + subSystem.getLabel();
/* 38 */     return new JarmTracer(traceNamePrefix);
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
/* 51 */     Api("api"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 56 */     RalCommon("ralCommon"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 61 */     RalP8CE("ralP8CE"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 66 */     RalP8CE_CBR("ralP8CE_CBR"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 71 */     RalCM8("ralCM8");
/*    */     
/*    */ 
/*    */     private String label;
/*    */     
/*    */     private SubSystem(String label)
/*    */     {
/* 78 */       this.label = label;
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
/* 89 */       return this.label;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\JarmTracer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */