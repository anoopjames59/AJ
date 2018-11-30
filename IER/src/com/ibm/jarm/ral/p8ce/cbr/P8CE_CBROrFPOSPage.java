/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_CBROrFPOSPage
/*     */   extends P8CE_CBRPage
/*     */ {
/*     */   private P8CE_CBRPage.XTransPageStatus pageStatus;
/* 124 */   private P8CE_CBRPage.ItemSources pageMarkSource = null;
/* 125 */   private boolean isAllLeftOver = false;
/* 126 */   private P8CE_CBRPage.ItemSources itemSrc = P8CE_CBRPage.ItemSources.Mixed;
/*     */   
/*     */   P8CE_CBROrFPOSPage(int fullPageSize)
/*     */   {
/* 130 */     super(fullPageSize);
/* 131 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(fullPageSize) });
/* 132 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   P8CE_CBRPage.XTransPageStatus getPageStatus()
/*     */   {
/* 137 */     return this.pageStatus;
/*     */   }
/*     */   
/*     */   void setPageStatus(P8CE_CBRPage.XTransPageStatus pageStatus)
/*     */   {
/* 142 */     this.pageStatus = pageStatus;
/*     */   }
/*     */   
/*     */   P8CE_CBRPage.ItemSources getPageMarkSource()
/*     */   {
/* 147 */     return this.pageMarkSource;
/*     */   }
/*     */   
/*     */   void setPageMarkSource(P8CE_CBRPage.ItemSources pageMarkSource)
/*     */   {
/* 152 */     this.pageMarkSource = pageMarkSource;
/*     */   }
/*     */   
/*     */   boolean isAllLeftOverItems()
/*     */   {
/* 157 */     Tracer.traceMethodEntry(new Object[0]);
/* 158 */     if (this.items.isEmpty())
/*     */     {
/* 160 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(this.isAllLeftOver) });
/* 161 */       return this.isAllLeftOver;
/*     */     }
/*     */     
/* 164 */     for (P8CE_CBRResultItem item : this.items)
/*     */     {
/* 166 */       P8CE_CBROrFPOSResultItem cbrOrFposItem = (P8CE_CBROrFPOSResultItem)item;
/* 167 */       if (!cbrOrFposItem.isFromLeftoverItem())
/*     */       {
/* 169 */         Tracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 170 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 174 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 175 */     return true;
/*     */   }
/*     */   
/*     */   P8CE_CBRPage.ItemSources getItemsSources()
/*     */   {
/* 180 */     Tracer.traceMethodEntry(new Object[0]);
/* 181 */     if (this.items.isEmpty())
/*     */     {
/* 183 */       Tracer.traceMethodExit(new Object[] { this.itemSrc });
/* 184 */       return this.itemSrc;
/*     */     }
/*     */     
/* 187 */     boolean hasCBR = false;
/* 188 */     boolean hasFPOS = false;
/* 189 */     for (P8CE_CBRResultItem item : this.items)
/*     */     {
/* 191 */       P8CE_CBROrFPOSResultItem cbrOrFposItem = (P8CE_CBROrFPOSResultItem)item;
/* 192 */       if (cbrOrFposItem.getItemSource() == P8CE_CBRPage.ItemSources.CBR) {
/* 193 */         hasCBR = true;
/* 194 */       } else if (cbrOrFposItem.getItemSource() == P8CE_CBRPage.ItemSources.FPOS)
/* 195 */         hasFPOS = true;
/*     */     }
/*     */     P8CE_CBRPage.ItemSources result;
/*     */     P8CE_CBRPage.ItemSources result;
/* 199 */     if ((!hasCBR) && (hasFPOS)) {
/* 200 */       result = P8CE_CBRPage.ItemSources.FPOS; } else { P8CE_CBRPage.ItemSources result;
/* 201 */       if ((hasCBR) && (!hasFPOS)) {
/* 202 */         result = P8CE_CBRPage.ItemSources.CBR;
/*     */       } else
/* 204 */         result = P8CE_CBRPage.ItemSources.Mixed;
/*     */     }
/* 206 */     Tracer.traceMethodExit(new Object[] { result });
/* 207 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   void removeItems()
/*     */   {
/* 213 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 215 */     this.isAllLeftOver = isAllLeftOverItems();
/* 216 */     this.itemSrc = getItemsSources();
/* 217 */     this.items.clear();
/* 218 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   void resetPageMark()
/*     */   {
/* 223 */     Tracer.traceMethodEntry(new Object[0]);
/* 224 */     this.begPageMark = null;
/* 225 */     this.pageMarkSource = null;
/* 226 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBROrFPOSPage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */