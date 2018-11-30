/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import com.filenet.api.core.IndependentObject;
/*    */ import com.filenet.api.property.Properties;
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8HitSorter
/*    */   implements Comparator
/*    */ {
/*    */   protected boolean ascending;
/*    */   protected String columnName;
/*    */   
/*    */   public P8HitSorter(boolean ascending, String columnName)
/*    */   {
/* 37 */     this.ascending = ascending;
/* 38 */     this.columnName = columnName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int compare(Object argument, Object anotherArgument)
/*    */   {
/* 47 */     int returnCode = 0;
/* 48 */     IndependentObject hit = null;
/* 49 */     IndependentObject anotherHit = null;
/* 50 */     if (((argument instanceof IndependentObject)) && ((anotherArgument instanceof IndependentObject))) {
/* 51 */       hit = (IndependentObject)argument;
/* 52 */       anotherHit = (IndependentObject)anotherArgument;
/*    */       
/* 54 */       String strTemp1 = hit.getProperties().getStringValue(this.columnName);
/* 55 */       String strTemp2 = anotherHit.getProperties().getStringValue(this.columnName);
/* 56 */       returnCode = strTemp1.compareToIgnoreCase(strTemp2);
/*    */     }
/* 58 */     if (!this.ascending) {
/* 59 */       returnCode *= -1;
/*    */     }
/* 61 */     return returnCode;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\P8HitSorter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */