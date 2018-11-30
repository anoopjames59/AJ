/*    */ package com.ibm.jarm.ral.p8ce.cbr;
/*    */ 
/*    */ import com.filenet.api.query.RepositoryRow;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.query.CBRResult;
/*    */ import com.ibm.jarm.api.query.ResultRow;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ import com.ibm.jarm.ral.p8ce.P8CE_ResultRowImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8CE_CBRResultImpl
/*    */   implements CBRResult
/*    */ {
/* 16 */   static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE_CBR);
/*    */   
/* 18 */   private RepositoryRow rr = null;
/* 19 */   private ResultRow result = null;
/* 20 */   private double score = 0.0D;
/*    */   
/*    */   public P8CE_CBRResultImpl(Repository jarmRepository, RepositoryRow rr)
/*    */   {
/* 24 */     Tracer.traceMethodEntry(new Object[] { jarmRepository, rr });
/* 25 */     this.rr = rr;
/* 26 */     this.result = new P8CE_ResultRowImpl(jarmRepository, rr);
/* 27 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ResultRow getResultRow()
/*    */   {
/* 35 */     return this.result;
/*    */   }
/*    */   
/*    */   protected void setResultRow(ResultRow result)
/*    */   {
/* 40 */     this.result = result;
/*    */   }
/*    */   
/*    */   protected void setScore(double score)
/*    */   {
/* 45 */     this.score = score;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getScore()
/*    */   {
/* 53 */     return this.score;
/*    */   }
/*    */   
/*    */   public RepositoryRow getP8CEResultRow()
/*    */   {
/* 58 */     return this.rr;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBRResultImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */