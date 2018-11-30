/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
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
/*     */ class P8CE_CBROrFPOSResultItem
/*     */   extends P8CE_CBRResultItem
/*     */ {
/*     */   private boolean isFromLeftoverItem;
/*     */   private P8CE_CBRPage.ItemSources itemSource;
/*     */   
/*     */   public P8CE_CBROrFPOSResultItem(Repository jarmRepository, RepositoryRow rr)
/*     */   {
/* 260 */     super(jarmRepository, rr);
/* 261 */     Tracer.traceMethodEntry(new Object[] { jarmRepository, rr });
/* 262 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   public boolean isFromLeftoverItem()
/*     */   {
/* 267 */     return this.isFromLeftoverItem;
/*     */   }
/*     */   
/*     */   public void setFromLeftoverItem(boolean isFromLeftoverItem)
/*     */   {
/* 272 */     this.isFromLeftoverItem = isFromLeftoverItem;
/*     */   }
/*     */   
/*     */   public P8CE_CBRPage.ItemSources getItemSource()
/*     */   {
/* 277 */     return this.itemSource;
/*     */   }
/*     */   
/*     */   public void setItemSource(P8CE_CBRPage.ItemSources itemSource)
/*     */   {
/* 282 */     this.itemSource = itemSource;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBROrFPOSResultItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */