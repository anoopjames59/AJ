/*    */ package com.ibm.jarm.ral.p8ce;
/*    */ 
/*    */ import com.filenet.api.security.Marking;
/*    */ import com.filenet.api.util.Id;
/*    */ import com.ibm.jarm.api.meta.RMMarkingItem;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class P8CE_RMMarkingItemImpl
/*    */   implements RMMarkingItem
/*    */ {
/* 19 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 26 */   static final String[] MandatoryPropertyNames = { "Id", "ConstraintMask", "MarkingUseGranted", "MarkingValue" };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private Marking jaceMarking;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   P8CE_RMMarkingItemImpl(Marking jaceMarking)
/*    */   {
/* 38 */     Tracer.traceMethodEntry(new Object[] { jaceMarking });
/* 39 */     this.jaceMarking = jaceMarking;
/* 40 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Integer getConstraintMask()
/*    */   {
/* 48 */     return this.jaceMarking.get_ConstraintMask();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getId()
/*    */   {
/* 56 */     Tracer.traceMethodEntry(new Object[0]);
/* 57 */     String result = null;
/* 58 */     Id markingId = this.jaceMarking.get_Id();
/* 59 */     if (markingId != null) {
/* 60 */       result = markingId.toString();
/*    */     }
/* 62 */     Tracer.traceMethodExit(new Object[] { result });
/* 63 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Integer getMarkingUseGranted()
/*    */   {
/* 71 */     return this.jaceMarking.get_MarkingUseGranted();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMarkingValue()
/*    */   {
/* 79 */     return this.jaceMarking.get_MarkingValue();
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMMarkingItemImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */