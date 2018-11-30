/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class IteratorWrapper<T> implements Iterator<T>
/*    */ {
/*    */   private Iterator<T> current;
/*    */   private java.util.LinkedList<Iterator<T>> iterators;
/*    */   
/*    */   public IteratorWrapper(java.util.Collection<Iterator<T>> iters)
/*    */   {
/* 12 */     this.iterators = new java.util.LinkedList();
/* 13 */     for (Iterator<T> it : iters) {
/* 14 */       if (it.hasNext()) {
/* 15 */         if (this.current == null) {
/* 16 */           this.current = it;
/*    */         } else
/* 18 */           this.iterators.addLast(it);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean hasNext() {
/* 24 */     if ((this.current != null) && (this.current.hasNext())) {
/* 25 */       return true;
/*    */     }
/* 27 */     return !this.iterators.isEmpty();
/*    */   }
/*    */   
/*    */   public T next() {
/* 31 */     if ((this.current != null) && (this.current.hasNext())) {
/* 32 */       return (T)this.current.next();
/*    */     }
/* 34 */     if (this.iterators.isEmpty()) {
/* 35 */       return null;
/*    */     }
/* 37 */     this.current = ((Iterator)this.iterators.removeFirst());
/* 38 */     return (T)this.current.next();
/*    */   }
/*    */   
/*    */   public void remove() {
/* 42 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\IteratorWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */