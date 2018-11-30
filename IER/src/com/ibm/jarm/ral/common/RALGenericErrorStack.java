/*    */ package com.ibm.jarm.ral.common;
/*    */ 
/*    */ import com.ibm.jarm.api.exception.RMErrorRecord;
/*    */ import com.ibm.jarm.api.exception.RMErrorStack;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ 
/*    */ public class RALGenericErrorStack implements RMErrorStack
/*    */ {
/*  9 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(com.ibm.jarm.api.util.JarmTracer.SubSystem.RalCommon);
/*    */   
/*    */   private RMErrorRecord[] errorRecords;
/*    */   
/*    */   public RALGenericErrorStack(RMErrorRecord[] errorRecords)
/*    */   {
/* 15 */     Tracer.traceMethodEntry(new Object[] { errorRecords });
/* 16 */     this.errorRecords = errorRecords;
/* 17 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */   public RMErrorRecord[] getErrorRecords()
/*    */   {
/* 22 */     return this.errorRecords;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALGenericErrorStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */