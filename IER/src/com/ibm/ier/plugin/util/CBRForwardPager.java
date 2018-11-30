/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import com.ibm.jarm.api.collection.CBRPageIterator;
/*    */ import com.ibm.jarm.api.query.CBRResult;
/*    */ 
/*    */ public class CBRForwardPager<T> extends ForwardPager<T>
/*    */ {
/*    */   private CBRPageIterator<CBRResult> cbrIterator;
/*    */   
/*    */   public CBRForwardPager(CBRPageIterator<CBRResult> iterator, int pageSize)
/*    */   {
/* 12 */     super(null, pageSize);
/* 13 */     if ((iterator != null) && (iterator.hasNextPage())) {
/* 14 */       this.cbrIterator = iterator;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isEndReached() {
/* 19 */     return (this.cbrIterator == null) || (!this.cbrIterator.hasNextPage());
/*    */   }
/*    */   
/*    */   public java.util.List<CBRResult> loadItems()
/*    */   {
/* 24 */     java.util.List<CBRResult> results = null;
/* 25 */     if ((this.cbrIterator != null) && (this.cbrIterator.hasNextPage())) {
/* 26 */       results = this.cbrIterator.getNextPage();
/*    */       
/* 28 */       if (results != null) {
/* 29 */         this.itemsRetrieved += results.size();
/*    */       }
/*    */     }
/* 32 */     return results;
/*    */   }
/*    */   
/*    */   public java.util.List<T> loadNextPage()
/*    */   {
/* 37 */     return loadItems();
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\CBRForwardPager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */