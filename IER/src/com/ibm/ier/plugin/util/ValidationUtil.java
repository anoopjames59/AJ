/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationUtil
/*    */ {
/*  9 */   private static char[] InvalidFolderChars = { '\\', '/', ':', '*', '?', '"', '<', '>', '|' };
/*    */   
/*    */   public static boolean validateRecordContainerNames(String containerName) {
/* 12 */     for (int i = 0; i < containerName.length(); i++) {
/* 13 */       char c = containerName.charAt(i);
/* 14 */       for (int j = 0; j < InvalidFolderChars.length; j++) {
/* 15 */         if (c == InvalidFolderChars[j])
/* 16 */           return false;
/*    */       }
/*    */     }
/* 19 */     return true;
/*    */   }
/*    */   
/*    */   public static boolean validateHoldName(String holdName) {
/* 23 */     return validateRecordContainerNames(holdName);
/*    */   }
/*    */   
/*    */   public static boolean validateLocationName(String locationName) {
/* 27 */     return validateRecordContainerNames(locationName);
/*    */   }
/*    */   
/*    */   public static boolean validatePatternName(String patternName) {
/* 31 */     return validateRecordContainerNames(patternName);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\ValidationUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */