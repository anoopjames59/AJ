/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.ibm.jarm.api.collection.CBRPageIterator;
/*     */ import com.ibm.jarm.api.collection.RMPageMark;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.query.CBRResult;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_Util;
/*     */ import java.util.List;
/*     */ 
/*     */ class P8CE_CBRPageIteratorImpl
/*     */   implements CBRPageIterator<CBRResult>
/*     */ {
/*  18 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE_CBR);
/*     */   
/*     */   P8CE_CBRSearch cbrSearchImpl;
/*     */   
/*     */   P8CE_CBRPageIteratorImpl(Repository repository, P8CE_CBRSearch cbrSearchImpl)
/*     */   {
/*  24 */     Tracer.traceMethodEntry(new Object[] { repository, cbrSearchImpl });
/*  25 */     this.cbrSearchImpl = cbrSearchImpl;
/*  26 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean nextPage()
/*     */   {
/*  34 */     Tracer.traceMethodEntry(new Object[0]);
/*  35 */     boolean ret = hasNextPage();
/*  36 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/*  37 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<CBRResult> getCurrentPage()
/*     */   {
/*  45 */     Tracer.traceMethodEntry(new Object[0]);
/*  46 */     List<CBRResult> ret = getNextPage();
/*  47 */     Tracer.traceMethodExit(new Object[] { ret });
/*  48 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<CBRResult> getNextPage()
/*     */   {
/*  56 */     Tracer.traceMethodEntry(new Object[0]);
/*  57 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/*  60 */       establishedSubject = P8CE_Util.associateSubject();
/*  61 */       List<CBRResult> ret = this.cbrSearchImpl.nextPage();
/*  62 */       Tracer.traceMethodExit(new Object[] { ret });
/*  63 */       return ret;
/*     */     }
/*     */     finally
/*     */     {
/*  67 */       if (establishedSubject) {
/*  68 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<CBRResult> getPrevPage()
/*     */   {
/*  77 */     Tracer.traceMethodEntry(new Object[0]);
/*  78 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/*  81 */       establishedSubject = P8CE_Util.associateSubject();
/*  82 */       List<CBRResult> ret = this.cbrSearchImpl.previousPage();
/*  83 */       Tracer.traceMethodExit(new Object[] { ret });
/*  84 */       return ret;
/*     */     }
/*     */     finally
/*     */     {
/*  88 */       if (establishedSubject) {
/*  89 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasNextPage()
/*     */   {
/*  98 */     Tracer.traceMethodEntry(new Object[0]);
/*  99 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 102 */       establishedSubject = P8CE_Util.associateSubject();
/* 103 */       boolean ret = this.cbrSearchImpl.hasNext();
/* 104 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/* 105 */       return ret;
/*     */     }
/*     */     catch (EngineRuntimeException ere) {
/* 108 */       Tracer.traceMethodExit(new Object[] { ere.getExceptionCode() + ere.getMessage() });
/* 109 */       throw ere;
/*     */     }
/*     */     finally
/*     */     {
/* 113 */       if (establishedSubject) {
/* 114 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasPrevPage()
/*     */   {
/* 123 */     Tracer.traceMethodEntry(new Object[0]);
/* 124 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 127 */       establishedSubject = P8CE_Util.associateSubject();
/* 128 */       boolean ret = this.cbrSearchImpl.hasPrevious();
/* 129 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/* 130 */       return ret;
/*     */     }
/*     */     catch (EngineRuntimeException ere) {
/* 133 */       Tracer.traceMethodExit(new Object[] { ere.getExceptionCode() + ere.getMessage() });
/* 134 */       throw ere;
/*     */     }
/*     */     finally
/*     */     {
/* 138 */       if (establishedSubject) {
/* 139 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/* 148 */     Tracer.traceMethodEntry(new Object[0]);
/* 149 */     int sz = this.cbrSearchImpl.getPageSize();
/* 150 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(sz) });
/* 151 */     return sz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getElementCount()
/*     */   {
/* 159 */     Tracer.traceMethodEntry(new Object[0]);
/* 160 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_OPERATION, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMPageMark getPageMark()
/*     */   {
/* 168 */     Tracer.traceMethodEntry(new Object[0]);
/* 169 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_OPERATION, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset(RMPageMark rmPageMark)
/*     */   {
/* 177 */     Tracer.traceMethodEntry(new Object[0]);
/* 178 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_OPERATION, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 186 */     Tracer.traceMethodEntry(new Object[0]);
/* 187 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_OPERATION, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPageSize(int pageSize)
/*     */   {
/* 195 */     Tracer.traceMethodEntry(new Object[0]);
/* 196 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_OPERATION, new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBRPageIteratorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */