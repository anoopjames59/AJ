/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class ForwardPager<T>
/*    */ {
/*    */   private Iterator<T> iterator;
/*    */   private final int pageSize;
/*    */   protected int itemsRetrieved;
/*    */   
/*    */   public ForwardPager(Iterator<T> iterator, int pageSize)
/*    */   {
/* 13 */     this.pageSize = pageSize;
/* 14 */     if ((iterator != null) && (iterator.hasNext())) {
/* 15 */       this.iterator = iterator;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isEndReached() {
/* 20 */     return this.iterator == null;
/*    */   }
/*    */   
/*    */   public boolean isMaxResultsReached() {
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   public java.util.List<T> loadItems(int count) {
/* 28 */     java.util.List<T> results = new java.util.ArrayList(count);
/*    */     
/* 30 */     if (this.iterator != null) {
/* 31 */       for (int i = 0; i < count; i++) {
/* 32 */         if (this.iterator.hasNext()) {
/* 33 */           T nextElement = this.iterator.next();
/* 34 */           results.add(nextElement);
/* 35 */           this.itemsRetrieved += 1;
/*    */         } else {
/* 37 */           this.iterator = null;
/* 38 */           break;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 43 */     return results;
/*    */   }
/*    */   
/*    */   public java.util.List<T> loadNextPage() {
/* 47 */     return loadItems(this.pageSize);
/*    */   }
/*    */   
/*    */   public int getNumberOfItemsRetrieved() {
/* 51 */     return this.itemsRetrieved;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\ForwardPager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */