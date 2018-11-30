/*    */ package com.ibm.ier.ddcp.exception;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DDCPErrorCode
/*    */ {
/* 32 */   INVALID_CEURL("1000"), 
/* 33 */   INVALID_USERNAME("1001"), 
/* 34 */   INVALID_PASSWORD("1002"), 
/* 35 */   FAIL_GENERATE_JACE_SUBJECT("1003"), 
/* 36 */   FAIL_TO_FETCH_CEOBJECTSTORE("1004"), 
/* 37 */   INVALID_OBJECT_STORE_NAME("1005"), 
/* 38 */   INVALID_ADVANCE_DAYS("1006"), 
/* 39 */   INVALID_CONTAINER_IDS("1007"), 
/* 40 */   FAILED_TO_RETRIEVE("1008"), 
/* 41 */   OBJECT_STORE_IS_NOT_FPOS("1009"), 
/* 42 */   NOT_FOUND_SPECIFIED_CONTAINER_IDS("1010"), 
/* 43 */   EMPTY_CONNECTION_POINT_NAME("1011"), 
/* 44 */   FAILED_TO_GET_PESESSION("1012"), 
/* 45 */   EMPTY_WORKFLOW_ID("1013"), 
/* 46 */   FAILED_TO_GET_VWVERSION("1014"), 
/* 47 */   FAILED_TO_LAUNCH_WORKFLOW("1015"), 
/* 48 */   NO_RETENTION_PERIOD_DDCONTAINER("1016"), 
/* 49 */   INVALID_RETENTION_PERIOD_FORMAT("1017"), 
/* 50 */   INVALID_FISCAL_YEAR_END_DATE_FORMAT("1018"), 
/* 51 */   BATCH_EXEC_EXCEPTION_UPDATE("1019"), 
/* 52 */   INTERRUPTED_EXCEPTION_WHILE_WAITING_UPDATE("1020"), 
/* 53 */   INVALID_REVIEWER_FOR_DELETE_AUDIT("1021"), 
/* 54 */   BATCH_EXEC_EXCEPTION_DELETE("1022"), 
/* 55 */   INTERRUPTED_EXCEPTION_WHILE_WAITING_DELETE("1023"), 
/* 56 */   BATCH_EXEC_EXCEPTION_AUDIT("1024"), 
/* 57 */   INTERRUPTED_EXCEPTION_WHILE_WAITING_AUDIT("1025"), 
/* 58 */   FAILED_TO_SAVE_CONTENT_IN_DOC("1026"), 
/* 59 */   NO_FILEPLAN("1027"), 
/* 60 */   FAILED_TO_GET_REPORT("1028"), 
/* 61 */   INVALID_PARENTPATH_FILEPLANNAME("1029"), 
/* 62 */   UNEXPECTED_ERROR("1030"), 
/* 63 */   NO_PROCESS_CLASSNAME("1031"), 
/* 64 */   EMPTY_CONFIG_CONTEXT_MAP("1032"), 
/* 65 */   NO_RECORD_CONTAINER_ID("1033"), 
/* 66 */   INVALID_RECORD_CONTAINER_ID("1034"), 
/* 67 */   NOT_SUPPORT_DECLARE_REC("1035"), 
/* 68 */   FAILED_TO_FETCH_OBJECTSTORE("1036");
/*    */   
/*    */ 
/*    */ 
/*    */   private String messageId;
/*    */   
/*    */ 
/*    */ 
/*    */   private DDCPErrorCode(String code)
/*    */   {
/* 78 */     this.messageId = ("FNRRD" + code);
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
/* 89 */     return this.messageId;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\exception\DDCPErrorCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */