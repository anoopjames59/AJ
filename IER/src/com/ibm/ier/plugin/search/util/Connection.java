/*    */ package com.ibm.ier.plugin.search.util;
/*    */ 
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ public abstract class Connection
/*    */ {
/*  9 */   protected String userId = null;
/*    */   
/* 11 */   protected String userDisplayName = null;
/*    */   
/* 13 */   protected String type = null;
/*    */   
/*    */   public String getUserId() {
/* 16 */     return this.userId;
/*    */   }
/*    */   
/*    */   public void setUserId(String userId) {
/* 20 */     this.userId = userId;
/*    */   }
/*    */   
/*    */   public String getUserDisplayName() {
/* 24 */     return this.userDisplayName;
/*    */   }
/*    */   
/*    */   public void setUserDisplayName(String userDisplayName) {
/* 28 */     this.userDisplayName = userDisplayName;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 32 */     return this.type;
/*    */   }
/*    */   
/*    */   public abstract boolean isConnected();
/*    */   
/*    */   public abstract void addPermissionsToJSON(JSONObject paramJSONObject, HttpServletRequest paramHttpServletRequest, String paramString);
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\Connection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */