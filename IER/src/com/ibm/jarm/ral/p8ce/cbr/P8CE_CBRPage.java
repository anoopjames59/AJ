/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.collection.PageMark;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_CBRPage
/*     */ {
/*  15 */   static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE_CBR);
/*     */   private int fullPageSize;
/*     */   
/*     */   static enum ItemSources {
/*  19 */     FPOS,  CBR,  Mixed;
/*     */     
/*     */     private ItemSources() {}
/*     */   }
/*     */   
/*  24 */   static enum XTransPageStatus { CBR,  FPOS,  Crossing;
/*     */     
/*     */     private XTransPageStatus() {} }
/*     */   
/*  28 */   protected List<P8CE_CBRResultItem> items = new ArrayList();
/*  29 */   protected List<P8CE_CBRResultItem> itemsForNextPage = new ArrayList();
/*  30 */   protected PageMark begPageMark = null;
/*     */   
/*     */   P8CE_CBRPage(int fullPageSize)
/*     */   {
/*  34 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(fullPageSize) });
/*  35 */     this.fullPageSize = fullPageSize;
/*  36 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   boolean isPageFull()
/*     */   {
/*  41 */     Tracer.traceMethodEntry(new Object[0]);
/*  42 */     boolean result = this.items.size() >= this.fullPageSize;
/*  43 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  44 */     return result;
/*     */   }
/*     */   
/*     */   List<P8CE_CBRResultItem> getItems()
/*     */   {
/*  49 */     return this.items;
/*     */   }
/*     */   
/*     */   void setItems(List<P8CE_CBRResultItem> items)
/*     */   {
/*  54 */     this.items = items;
/*     */   }
/*     */   
/*     */   void addItems(List<P8CE_CBRResultItem> items)
/*     */   {
/*  59 */     Tracer.traceMethodEntry(new Object[] { items });
/*  60 */     this.items.addAll(items);
/*  61 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   void addItem(P8CE_CBRResultItem item)
/*     */   {
/*  66 */     Tracer.traceMethodEntry(new Object[] { item });
/*  67 */     this.items.add(item);
/*  68 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   void removeItems()
/*     */   {
/*  73 */     Tracer.traceMethodEntry(new Object[0]);
/*  74 */     this.items.clear();
/*  75 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   boolean hasItemsForNextPage()
/*     */   {
/*  80 */     Tracer.traceMethodEntry(new Object[0]);
/*  81 */     boolean result = !this.itemsForNextPage.isEmpty();
/*  82 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  83 */     return result;
/*     */   }
/*     */   
/*     */   List<P8CE_CBRResultItem> getItemsForNextPage()
/*     */   {
/*  88 */     return this.itemsForNextPage;
/*     */   }
/*     */   
/*     */   void setItemsForNextPage(List<P8CE_CBRResultItem> itemsForNextPage) {
/*  92 */     this.itemsForNextPage = itemsForNextPage;
/*     */   }
/*     */   
/*     */   void addItemsForNextPage(List<P8CE_CBRResultItem> itemsForNextPage)
/*     */   {
/*  97 */     Tracer.traceMethodEntry(new Object[] { itemsForNextPage });
/*  98 */     this.itemsForNextPage.addAll(itemsForNextPage);
/*  99 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   void addItemForNextPage(P8CE_CBRResultItem itemForNextPage)
/*     */   {
/* 104 */     Tracer.traceMethodEntry(new Object[] { itemForNextPage });
/* 105 */     this.itemsForNextPage.add(itemForNextPage);
/* 106 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   PageMark getBegPageMark()
/*     */   {
/* 111 */     return this.begPageMark;
/*     */   }
/*     */   
/*     */   void setBegPageMark(PageMark begPageMark)
/*     */   {
/* 116 */     this.begPageMark = begPageMark;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBRPage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */