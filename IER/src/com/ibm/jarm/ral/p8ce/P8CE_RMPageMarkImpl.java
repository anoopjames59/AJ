/*    */ package com.ibm.jarm.ral.p8ce;
/*    */ 
/*    */ import com.filenet.api.collection.PageMark;
/*    */ import com.ibm.jarm.api.collection.RMPageMark;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class P8CE_RMPageMarkImpl
/*    */   implements RMPageMark
/*    */ {
/* 16 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*    */   
/*    */   private PageMark jacePageMark;
/*    */   
/*    */   P8CE_RMPageMarkImpl(PageMark jacePageMark)
/*    */   {
/* 22 */     Tracer.traceMethodEntry(new Object[] { jacePageMark });
/* 23 */     this.jacePageMark = jacePageMark;
/* 24 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */   PageMark getJacePageMark()
/*    */   {
/* 29 */     return this.jacePageMark;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMPageMarkImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */