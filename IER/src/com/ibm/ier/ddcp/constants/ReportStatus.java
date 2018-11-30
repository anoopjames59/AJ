/*    */ package com.ibm.ier.ddcp.constants;
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
/*    */ public enum ReportStatus
/*    */ {
/* 18 */   NotInReport(0);
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */   private ReportStatus(int intValue)
/*    */   {
/* 25 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 35 */     return this.intValue;
/*    */   }
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
/*    */   public static ReportStatus getInstanceFromInt(int intValue)
/*    */   {
/* 49 */     ReportStatus result = null;
/* 50 */     if (intValue == NotInReport.intValue) {
/* 51 */       result = NotInReport;
/*    */     }
/* 53 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\constants\ReportStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */