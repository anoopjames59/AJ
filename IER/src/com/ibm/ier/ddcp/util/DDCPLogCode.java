/*    */ package com.ibm.ier.ddcp.util;
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
/*    */ public enum DDCPLogCode
/*    */ {
/* 19 */   CE_QUERY_PAGE_MAX_SIZE("1100"), 
/* 20 */   CE_NON_PAGED_QUERY_MAX_SIZE("1101"), 
/* 21 */   THREAD_COUNT("1102"), 
/* 22 */   READ_BATCH_SIZE("1103"), 
/* 23 */   UPDATE_BATCH_SIZE("1104"), 
/* 24 */   CONTENT_SIZE_LIMIT("1105"), 
/* 25 */   TOTAL_ONHOLD_CONTAINERS("1106"), 
/* 26 */   NO_VALID_DDCONTAINER("1107"), 
/* 27 */   FAILED_UPDATE_REC_PROPERTY("1108"), 
/* 28 */   FAILED_DELETE_REC("1109"), 
/* 29 */   FAILED_AUDIT_REC("1110"), 
/* 30 */   TOTAL_RECS_IN_CONTAINER("1111"), 
/* 31 */   TOTAL_DDCONTAINERS_FOR_THE_REVIEWER("1112"), 
/* 32 */   TOTAL_COUNT_FOR_REVIEWER("1113"), 
/* 33 */   TOTAL_RECORDS_IN_ALL_REPORTS("1114"), 
/* 34 */   DDCONTAINER_ON_INDIRECT_IER_HOLD("1115"), 
/* 35 */   DELETE_BATCH_SUMMARY("1116"), 
/* 36 */   UPDATE_BATCH_SUMMARY("1117"), 
/* 37 */   WORFLOW_LAUNCHED("1118"), 
/* 38 */   REPORT_CONTENT_INFO("1119"), 
/* 39 */   FOUND_LINKS("1120"), 
/* 40 */   RECEIPTS_FOR_RECORDS("1121"), 
/* 41 */   NUMBER_OF_LINKS_TO_DELETED_RECORDS("1122"), 
/* 42 */   RETAINMETADATA_ON("1123"), 
/* 43 */   RETAINMETADATA_OFF("1124"), 
/* 44 */   DELETE_BASIC_INFO("1125"), 
/* 45 */   DELETE_PROGRSESS("1126"), 
/* 46 */   FAILED_TO_LAUNCH_WORKFLOW("1127"), 
/* 47 */   FAILED_TO_RETRIEVE_FROM_DDCONTAINER("1128"), 
/* 48 */   REPORT_CONTENT_EMPTY("1129"), 
/* 49 */   REPORT_RECOVERY_ID("1130"), 
/* 50 */   TRIGGER_NAME_CHANGED("1131"), 
/* 51 */   RETENTION_PERIOD_CHANGED("1132"), 
/* 52 */   DDCONTAINER_NOT_FOUND("1133"), 
/* 53 */   LINK_CACHE_SIZE_LIMIT("1134"), 
/* 54 */   LINK_CACHE_IS_FULL("1135"), 
/* 55 */   ONHOLD_CONTAINER_CACHE_SIZE_LIMIT("1136"), 
/* 56 */   TOTAL_RECORDS_DELETED("1137"), 
/* 57 */   TOTAL_RECORDS_NOT_DELETED("1138"), 
/* 58 */   TOTAL_DESTROY_PERFORMANCE("1139"), 
/* 59 */   CACHE_FOR_CONTAINERS_ONDIRECTHOLD_FULL("1140"), 
/* 60 */   FAILURE_REPORT_ID("1141"), 
/* 61 */   UNPROCESSED_EXCEPTION("1142"), 
/* 62 */   EXCEPTION_IN_AUDIT("1143"), 
/* 63 */   SUCCESS_REPORT_ID("1144"), 
/* 64 */   NOT_FOUND_SPECIFIED_CONTAINER_IDS("1145"), 
/* 65 */   NO_WORKFLOW_NEEDS_LAUNCH("1146"), 
/* 66 */   START_REPORT("1147"), 
/* 67 */   END_REPORT("1148"), 
/* 68 */   START_WF_LAUNCH("1149"), 
/* 69 */   END_WF_LAUNCH("1150"), 
/* 70 */   START_DESTROY("1151"), 
/* 71 */   END_DESTROY("1152"), 
/* 72 */   START_INIT_DESTROY("1153"), 
/* 73 */   END_INIT_DESTROY("1154"), 
/* 74 */   ADVANCED_DAYS("1155"), 
/* 75 */   REPORT_ONLY("1156");
/*    */   
/*    */   private String messageKey;
/*    */   
/*    */   private DDCPLogCode(String code)
/*    */   {
/* 81 */     this.messageKey = ("FNRRD" + code);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMessageKey()
/*    */   {
/* 92 */     return this.messageKey;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\util\DDCPLogCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */