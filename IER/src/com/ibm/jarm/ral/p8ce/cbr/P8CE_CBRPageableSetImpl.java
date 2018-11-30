/*    */ package com.ibm.jarm.ral.p8ce.cbr;
/*    */ 
/*    */ import com.ibm.jarm.api.collection.CBRPageIterator;
/*    */ import com.ibm.jarm.api.collection.PageableSet;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.exception.RMErrorCode;
/*    */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*    */ import com.ibm.jarm.api.query.CBRResult;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ class P8CE_CBRPageableSetImpl
/*    */   implements PageableSet<CBRResult>
/*    */ {
/* 17 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE_CBR);
/*    */   
/*    */   private Repository repository;
/*    */   private P8CE_CBRSearch rmCBRSearch;
/* 21 */   private boolean supportsPaging = false;
/*    */   
/*    */   P8CE_CBRPageableSetImpl(Repository repository, P8CE_CBRSearch rmCBRSearch, boolean supportsPaging)
/*    */   {
/* 25 */     Tracer.traceMethodEntry(new Object[] { repository, rmCBRSearch, Boolean.valueOf(supportsPaging) });
/* 26 */     this.repository = repository;
/* 27 */     this.rmCBRSearch = rmCBRSearch;
/* 28 */     this.supportsPaging = supportsPaging;
/* 29 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean supportsPaging()
/*    */   {
/* 37 */     return this.supportsPaging;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isEmpty()
/*    */   {
/* 45 */     Tracer.traceMethodEntry(new Object[0]);
/* 46 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_OPERATION, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Iterator<CBRResult> iterator()
/*    */   {
/* 54 */     Tracer.traceMethodEntry(new Object[0]);
/* 55 */     List<CBRResult> list = this.rmCBRSearch.nextPage();
/* 56 */     Iterator<CBRResult> it = list != null ? list.iterator() : null;
/* 57 */     Tracer.traceMethodExit(new Object[] { it });
/* 58 */     return it;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public CBRPageIterator<CBRResult> pageIterator()
/*    */   {
/* 66 */     Tracer.traceMethodEntry(new Object[0]);
/* 67 */     if (!this.supportsPaging) {
/* 68 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PAGING_NOT_SUPPORTED, new Object[0]);
/*    */     }
/* 70 */     CBRPageIterator<CBRResult> result = new P8CE_CBRPageIteratorImpl(this.repository, this.rmCBRSearch);
/* 71 */     Tracer.traceMethodExit(new Object[] { result });
/* 72 */     return result;
/*    */   }
/*    */   
/*    */   public Integer totalCount()
/*    */     throws NoSuchMethodError
/*    */   {
/* 78 */     Tracer.traceMethodEntry(new Object[0]);
/* 79 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_OPERATION, new Object[0]);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBRPageableSetImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */