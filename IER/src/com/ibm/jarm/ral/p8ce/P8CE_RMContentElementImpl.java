/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.ContentElement;
/*     */ import com.filenet.api.core.ContentReference;
/*     */ import com.filenet.api.core.ContentTransfer;
/*     */ import com.ibm.jarm.api.core.RMContentElement;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMContentElementImpl
/*     */   implements RMContentElement
/*     */ {
/*  20 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */   private ContentElement jaceContentElement;
/*     */   private boolean isAContentTransfer;
/*     */   
/*     */   P8CE_RMContentElementImpl(ContentElement jaceContentElement)
/*     */   {
/*  27 */     Tracer.traceMethodEntry(new Object[] { jaceContentElement });
/*  28 */     this.jaceContentElement = jaceContentElement;
/*  29 */     this.isAContentTransfer = (jaceContentElement instanceof ContentTransfer);
/*  30 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBinaryContentRetrievalName()
/*     */   {
/*  38 */     Tracer.traceMethodEntry(new Object[0]);
/*  39 */     String result = null;
/*  40 */     if (this.isAContentTransfer) {
/*  41 */       result = ((ContentTransfer)this.jaceContentElement).get_RetrievalName();
/*     */     }
/*  43 */     Tracer.traceMethodExit(new Object[] { result });
/*  44 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Double getBinaryContentSize()
/*     */   {
/*  52 */     Tracer.traceMethodEntry(new Object[0]);
/*  53 */     Double result = null;
/*  54 */     if (this.isAContentTransfer) {
/*  55 */       result = ((ContentTransfer)this.jaceContentElement).get_ContentSize();
/*     */     }
/*  57 */     Tracer.traceMethodExit(new Object[] { result });
/*  58 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getBinaryContentStream()
/*     */   {
/*  66 */     Tracer.traceMethodEntry(new Object[0]);
/*  67 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/*  70 */       establishedSubject = P8CE_Util.associateSubject();
/*  71 */       InputStream result = null;
/*  72 */       long startTime; if (this.isAContentTransfer)
/*     */       {
/*  74 */         startTime = System.currentTimeMillis();
/*  75 */         result = ((ContentTransfer)this.jaceContentElement).accessContentStream();
/*  76 */         long endTime = System.currentTimeMillis();
/*  77 */         Tracer.traceExtCall("ContentTransfer.accessContentStream", startTime, endTime, null, result, new Object[0]);
/*     */       }
/*     */       
/*  80 */       Tracer.traceMethodExit(new Object[] { result });
/*  81 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/*  85 */       if (establishedSubject) {
/*  86 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/*  95 */     return this.jaceContentElement.get_ContentType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReferenceContentLocation()
/*     */   {
/* 103 */     Tracer.traceMethodEntry(new Object[0]);
/* 104 */     String result = null;
/* 105 */     if (!this.isAContentTransfer) {
/* 106 */       result = ((ContentReference)this.jaceContentElement).get_ContentLocation();
/*     */     }
/* 108 */     Tracer.traceMethodExit(new Object[] { result });
/* 109 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAContentReference()
/*     */   {
/* 117 */     return !this.isAContentTransfer;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMContentElementImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */