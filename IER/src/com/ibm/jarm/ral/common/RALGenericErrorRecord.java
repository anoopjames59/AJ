/*    */ package com.ibm.jarm.ral.common;
/*    */ 
/*    */ import com.ibm.jarm.api.exception.Diagnostic;
/*    */ import com.ibm.jarm.api.exception.RMErrorCode;
/*    */ import com.ibm.jarm.api.exception.RMErrorRecord;
/*    */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ 
/*    */ public class RALGenericErrorRecord implements RMErrorRecord
/*    */ {
/* 11 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(com.ibm.jarm.api.util.JarmTracer.SubSystem.RalCommon);
/*    */   
/*    */   private String description;
/*    */   private String source;
/*    */   private Diagnostic[] diagnostics;
/*    */   
/*    */   public RALGenericErrorRecord(String description, String source, Diagnostic[] diagnostics)
/*    */   {
/* 19 */     Tracer.traceMethodEntry(new Object[] { description, source, diagnostics });
/* 20 */     this.description = description;
/* 21 */     this.source = source;
/* 22 */     this.diagnostics = diagnostics;
/* 23 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */   public RALGenericErrorRecord(RMErrorCode errorCode, Object... params)
/*    */   {
/* 28 */     Tracer.traceMethodEntry(new Object[] { errorCode, params });
/* 29 */     Exception ex = RMRuntimeException.createRMRuntimeException(errorCode, params);
/* 30 */     this.description = ex.getLocalizedMessage();
/* 31 */     this.source = errorCode.name();
/* 32 */     this.diagnostics = new Diagnostic[0];
/* 33 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */   public String getDescription()
/*    */   {
/* 38 */     return this.description;
/*    */   }
/*    */   
/*    */   public Diagnostic[] getDiagnosticTypes()
/*    */   {
/* 43 */     return this.diagnostics;
/*    */   }
/*    */   
/*    */   public String getSource()
/*    */   {
/* 48 */     return this.source;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALGenericErrorRecord.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */