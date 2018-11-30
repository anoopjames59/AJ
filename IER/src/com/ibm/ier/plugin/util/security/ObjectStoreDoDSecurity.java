/*    */ package com.ibm.ier.plugin.util.security;
/*    */ 
/*    */ import com.filenet.api.core.Connection;
/*    */ import com.filenet.api.security.SecurityPrincipal;
/*    */ import java.util.List;
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
/*    */ public class ObjectStoreDoDSecurity
/*    */   extends ObjectStoreBaseSecurity
/*    */ {
/*    */   public ObjectStoreDoDSecurity(String osname, List<SecurityPrincipal>[] rmGroups, Connection conn)
/*    */     throws Exception
/*    */   {
/* 29 */     super(osname, rmGroups, conn);
/*    */   }
/*    */   
/*    */   protected void setMarkingSetRelatedSecurity() throws Exception {
/* 33 */     updateDeletePreventMarkingSecurity();
/*    */   }
/*    */   
/*    */   protected void setObjectStoreRelatedSecurity() throws Exception {
/* 37 */     applyBaseDefaultInstanceSecurity();
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\security\ObjectStoreDoDSecurity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */