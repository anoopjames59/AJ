/*    */ package com.ibm.jarm.api.util;
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
/*    */ public enum RMLogCode
/*    */ {
/* 21 */   TEST_CODE("9999"), 
/* 22 */   RMAUDIT_CREATION_FAILURE("0700"), 
/* 23 */   RECORDCOPY_DOCUMENT_FILING_FAILURE("0701"), 
/* 24 */   MULTIPLE_ACTIVE_VOLUMES("0702"), 
/* 25 */   DOD_CLASSIFIED_DECL_VERSIONING_FAILURE("0703"), 
/* 26 */   PSS_Atlas_ARTIFACT_DELETE_FAILURE("0704"), 
/* 27 */   RECALCULATE_CUTOFF_PERFORMED("0705");
/*    */   
/*    */   private String messageKey;
/*    */   
/*    */   private RMLogCode(String code)
/*    */   {
/* 33 */     this.messageKey = ("FNRRS" + code);
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
/* 44 */     return this.messageKey;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\RMLogCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */