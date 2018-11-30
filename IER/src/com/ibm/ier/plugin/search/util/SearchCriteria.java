/*    */ package com.ibm.ier.plugin.search.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SearchCriteria
/*    */   implements Serializable
/*    */ {
/*    */   protected String name;
/*    */   protected String displayName;
/*    */   
/*    */   public void setName(String name)
/*    */   {
/* 26 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 33 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getDisplayName() {
/* 37 */     if (this.displayName == null) {
/* 38 */       return this.name;
/*    */     }
/* 40 */     return this.displayName;
/*    */   }
/*    */   
/*    */   public void setDisplayName(String displayName) {
/* 44 */     this.displayName = displayName;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\SearchCriteria.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */