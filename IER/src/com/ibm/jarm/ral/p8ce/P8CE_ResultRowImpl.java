/*    */ package com.ibm.jarm.ral.p8ce;
/*    */ 
/*    */ import com.filenet.api.query.RepositoryRow;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.jarm.api.query.ResultRow;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ import com.ibm.jarm.ral.common.RALBaseRepository;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8CE_ResultRowImpl
/*    */   implements ResultRow
/*    */ {
/* 18 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/* 19 */   private static final IGenerator<ResultRow> ResultRowGenerator = new Generator();
/*    */   
/*    */   private RALBaseRepository ralRepository;
/*    */   
/*    */   private RepositoryRow jaceRow;
/*    */   
/*    */ 
/*    */   static IGenerator<ResultRow> getGenerator()
/*    */   {
/* 28 */     return ResultRowGenerator;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public P8CE_ResultRowImpl(Repository rmRepository, RepositoryRow jaceRow)
/*    */   {
/* 36 */     Tracer.traceMethodEntry(new Object[] { rmRepository, jaceRow });
/* 37 */     this.ralRepository = ((RALBaseRepository)rmRepository);
/* 38 */     this.jaceRow = jaceRow;
/* 39 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RMProperties getProperties()
/*    */   {
/* 48 */     Tracer.traceMethodEntry(new Object[0]);
/* 49 */     RMProperties result = new P8CE_RMPropertiesImpl(this.jaceRow.getProperties(), this.ralRepository);
/* 50 */     Tracer.traceMethodExit(new Object[] { result });
/* 51 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static class Generator
/*    */     implements IGenerator<ResultRow>
/*    */   {
/*    */     public ResultRow create(Repository repository, Object jaceRow)
/*    */     {
/* 65 */       P8CE_ResultRowImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceRow });
/*    */       
/* 67 */       ResultRow result = new P8CE_ResultRowImpl(repository, (RepositoryRow)jaceRow);
/*    */       
/* 69 */       P8CE_ResultRowImpl.Tracer.traceMethodExit(new Object[] { result });
/* 70 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_ResultRowImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */