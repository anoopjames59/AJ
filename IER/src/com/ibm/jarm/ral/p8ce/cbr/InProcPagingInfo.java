/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.collection.PageMark;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ class InProcPagingInfo
/*     */ {
/*  14 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*  16 */   List<PageMark> pageMarkList = new ArrayList();
/*  17 */   ListIterator<PageMark> curPageMarkIt = null;
/*  18 */   PageIterator pageIterator = null;
/*  19 */   private PageMark curPageMark = null;
/*     */   
/*     */   InProcPagingInfo(PageIterator pi)
/*     */   {
/*  23 */     this.pageIterator = pi;
/*     */   }
/*     */   
/*     */   PageMark getCurPageMark()
/*     */   {
/*  28 */     return this.curPageMark;
/*     */   }
/*     */   
/*     */   void setCurPageMark(PageMark pm)
/*     */   {
/*  33 */     this.curPageMark = pm;
/*     */   }
/*     */   
/*     */   void reset()
/*     */   {
/*  38 */     Tracer.traceMethodEntry(new Object[0]);
/*  39 */     if (this.pageIterator != null) {
/*  40 */       this.pageIterator.reset();
/*     */     }
/*  42 */     this.curPageMarkIt = null;
/*  43 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   boolean hasNextPage()
/*     */   {
/*  48 */     Tracer.traceMethodEntry(new Object[0]);
/*  49 */     boolean bHasNext = false;
/*  50 */     if (this.pageIterator != null)
/*     */     {
/*     */ 
/*  53 */       PageMark curMark = this.pageIterator.getPageMark();
/*     */       
/*  55 */       bHasNext = this.pageIterator.nextPage();
/*     */       
/*  57 */       if (this.curPageMarkIt == null)
/*     */       {
/*  59 */         this.pageIterator.reset();
/*     */       }
/*     */       else
/*     */       {
/*  63 */         this.pageIterator.reset(curMark);
/*  64 */         this.pageIterator.nextPage();
/*     */       }
/*     */     }
/*     */     
/*  68 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(bHasNext) });
/*  69 */     return bHasNext;
/*     */   }
/*     */   
/*     */   boolean hasPreviousPage()
/*     */   {
/*  74 */     Tracer.traceMethodEntry(new Object[0]);
/*  75 */     boolean result = hasPreviousPage(false);
/*  76 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  77 */     return result;
/*     */   }
/*     */   
/*     */   boolean hasPreviousPage(boolean onPageBackward)
/*     */   {
/*  82 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(onPageBackward) });
/*  83 */     boolean bRet = false;
/*  84 */     if (this.curPageMarkIt != null)
/*     */     {
/*  86 */       if (onPageBackward)
/*     */       {
/*  88 */         bRet = this.curPageMarkIt.hasPrevious();
/*     */ 
/*     */ 
/*     */       }
/*  92 */       else if (this.curPageMarkIt.hasPrevious())
/*     */       {
/*     */ 
/*  95 */         this.curPageMarkIt.previous();
/*  96 */         bRet = this.curPageMarkIt.hasPrevious();
/*     */         
/*     */ 
/*  99 */         this.curPageMarkIt.next();
/*     */       }
/*     */       else
/*     */       {
/* 103 */         this.pageIterator.reset();
/* 104 */         bRet = false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 109 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(bRet) });
/* 110 */     return bRet;
/*     */   }
/*     */   
/*     */   Object[] nextPage() throws Exception
/*     */   {
/* 115 */     Tracer.traceMethodEntry(new Object[0]);
/* 116 */     if (this.curPageMarkIt == null) {
/* 117 */       this.curPageMarkIt = this.pageMarkList.listIterator();
/*     */     }
/* 119 */     if (this.curPageMarkIt.hasNext())
/*     */     {
/*     */ 
/* 122 */       PageMark pMark = (PageMark)this.curPageMarkIt.next();
/* 123 */       setCurPageMark(pMark);
/*     */       
/* 125 */       this.pageIterator.reset(pMark);
/* 126 */       this.pageIterator.nextPage();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 132 */     else if (this.pageIterator.nextPage())
/*     */     {
/*     */ 
/* 135 */       PageMark nextMark = this.pageIterator.getPageMark();
/* 136 */       this.pageMarkList.add(nextMark);
/* 137 */       setCurPageMark(nextMark);
/*     */       
/*     */ 
/* 140 */       this.curPageMarkIt = this.pageMarkList.listIterator();
/* 141 */       while (this.curPageMarkIt.hasNext()) {
/* 142 */         this.curPageMarkIt.next();
/*     */       }
/*     */     }
/*     */     
/* 146 */     Object[] rowArray = this.pageIterator.getCurrentPage();
/*     */     
/* 148 */     Tracer.traceMethodExit(new Object[] { rowArray });
/* 149 */     return rowArray;
/*     */   }
/*     */   
/*     */   Object[] previousPage()
/*     */   {
/* 154 */     Tracer.traceMethodEntry(new Object[0]);
/* 155 */     Object[] result = previousPage(false);
/* 156 */     Tracer.traceMethodExit(new Object[] { result });
/* 157 */     return result;
/*     */   }
/*     */   
/*     */   Object[] previousPage(boolean onPageBackward)
/*     */   {
/* 162 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(onPageBackward) });
/* 163 */     Object[] rowArray = null;
/* 164 */     if (hasPreviousPage(onPageBackward))
/*     */     {
/* 166 */       if (onPageBackward)
/*     */       {
/* 168 */         PageMark pMark = (PageMark)this.curPageMarkIt.previous();
/*     */         
/*     */ 
/* 171 */         this.pageIterator.reset(pMark);
/* 172 */         this.pageIterator.nextPage();
/*     */         
/*     */ 
/* 175 */         rowArray = this.pageIterator.getCurrentPage();
/*     */         
/* 177 */         this.curPageMarkIt.next();
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 182 */         this.curPageMarkIt.previous();
/* 183 */         if (this.curPageMarkIt.hasPrevious())
/*     */         {
/* 185 */           PageMark pMark = (PageMark)this.curPageMarkIt.previous();
/*     */           
/*     */ 
/* 188 */           this.curPageMarkIt.next();
/*     */           
/*     */ 
/* 191 */           this.pageIterator.reset(pMark);
/* 192 */           this.pageIterator.nextPage();
/*     */           
/*     */ 
/* 195 */           rowArray = this.pageIterator.getCurrentPage();
/*     */         }
/*     */         else
/*     */         {
/* 199 */           this.curPageMarkIt.next();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 204 */     Tracer.traceMethodExit(new Object[] { rowArray });
/* 205 */     return rowArray;
/*     */   }
/*     */   
/*     */   void setPageStart(PageMark pMarkStart, boolean onePageBackward) throws Exception
/*     */   {
/* 210 */     Tracer.traceMethodEntry(new Object[] { pMarkStart, Boolean.valueOf(onePageBackward) });
/* 211 */     if (hasPreviousPage(onePageBackward))
/*     */     {
/*     */ 
/*     */ 
/* 215 */       PageMark pMark = (PageMark)this.curPageMarkIt.previous();
/* 216 */       if ((onePageBackward) || (this.curPageMarkIt.hasPrevious()))
/*     */       {
/* 218 */         if (!onePageBackward) {
/* 219 */           pMark = (PageMark)this.curPageMarkIt.previous();
/*     */         }
/*     */         
/* 222 */         while ((this.curPageMarkIt.hasPrevious()) && ((pMarkStart == null) || (!pMarkStart.equals(pMark))))
/*     */         {
/* 224 */           pMark = (PageMark)this.curPageMarkIt.previous();
/*     */         }
/*     */         
/*     */ 
/* 228 */         if (pMarkStart != null)
/*     */         {
/* 230 */           this.curPageMarkIt.next();
/* 231 */           this.pageIterator.reset(pMarkStart);
/* 232 */           this.pageIterator.nextPage();
/*     */         }
/*     */         else
/*     */         {
/* 236 */           this.pageIterator.reset();
/*     */         }
/*     */       }
/*     */     }
/* 240 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\InProcPagingInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */