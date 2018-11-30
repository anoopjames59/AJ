/*    */ package com.ibm.ier.report.exception;
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
/*    */ public enum RptErrorCode
/*    */ {
/* 25 */   RPTENG_REPORT_IDENTIFIER_NOT_UNIQUE("0101"), 
/* 26 */   RPTENG_INVALID_VALUE_FOR_REQUIRED_PARAMETER("0102"), 
/* 27 */   RPTENG_NO_AVAILABLE_REQUIRED_PARAMETER("0103"), 
/* 28 */   RPTENG_INPUT_PARAMETER_NOT_DEFINED("0104"), 
/* 29 */   RPTENG_ERROR_DB_SERVICE_CLOSE("0105"), 
/* 30 */   RPTENG_DB_TABLE_NAME_NOT_DEFINED("0106");
/*    */   
/*    */   private String messageId;
/*    */   
/*    */   private RptErrorCode(String code)
/*    */   {
/* 36 */     this.messageId = ("FNRRE" + code);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMessageId()
/*    */   {
/* 47 */     return this.messageId;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\exception\RptErrorCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */