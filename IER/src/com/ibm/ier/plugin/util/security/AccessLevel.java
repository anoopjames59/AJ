/*    */ package com.ibm.ier.plugin.util.security;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccessLevel
/*    */ {
/*    */   public static final int READ = 131073;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int VIEW = 131201;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int WRITE_FOLDER = 266227;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int WRITE_CUSTOM = 131347;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int FULL_CONTROL_CLASS = 983827;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int FULL_CONTROL_CUSTOM = 983315;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int LINK_FOLDER = 131121;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int FULL_CONTROL_DOMAIN = 459267;
/*    */   
/*    */ 
/*    */   public static final int FULL_CONTROL_OBJECT_STORE = 586547200;
/*    */   
/*    */ 
/*    */   public static final int FULL_CONTROL_DOCUMENT = 986583;
/*    */   
/*    */ 
/*    */   public static final int FULL_CONTROL_FOLDER = 987127;
/*    */   
/*    */ 
/*    */   public static final int CUSTOM_FULL_CONTROL_FOLDER = 986100;
/*    */   
/*    */ 
/*    */ 
/*    */   public static boolean isObjectStoreAdministrator(int rights)
/*    */   {
/* 54 */     if (rights != 0) {
/* 55 */       return (0x22F60000 & rights) == 586547200;
/*    */     }
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isFolderAdministrator(int rights)
/*    */   {
/* 62 */     if (rights != 0) {
/* 63 */       return (0xF0BF4 & rights) == 986100;
/*    */     }
/* 65 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isGCDAdministrator(int rights)
/*    */   {
/* 77 */     if (rights != 0) {
/* 78 */       return (0x70203 & rights) == 459267;
/*    */     }
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\security\AccessLevel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */