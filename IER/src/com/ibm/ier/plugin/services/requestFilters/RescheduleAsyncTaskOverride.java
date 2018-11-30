/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.configuration.Config;
/*    */ import com.ibm.ier.plugin.configuration.SettingsConfig;
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.tasks.util.AESEncryptionUtil;
/*    */ import com.ibm.ier.plugin.util.J2EEServerUtil;
/*    */ import com.ibm.ier.plugin.util.TaskManagerUtil;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ public class RescheduleAsyncTaskOverride extends IERBaseRequestFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 18 */     return new String[] { "/rescheduleAsyncTask" };
/*    */   }
/*    */   
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest) throws Exception
/*    */   {
/* 23 */     if ((this.baseService.isIERDesktop()) && 
/* 24 */       (jsonRequest != null) && ((jsonRequest instanceof JSONObject))) {
/* 25 */       JSONObject taskRequest = (JSONObject)jsonRequest;
/* 26 */       String password = (String)taskRequest.get("password");
/* 27 */       if (taskRequest != null) {
/* 28 */         if (password != null) {
/* 29 */           String encryptionKey = Config.getSettingsConfig().getAESEncryptionKey();
/* 30 */           taskRequest.put("password", AESEncryptionUtil.encrypt(password, encryptionKey));
/* 31 */           taskRequest.put("AESEncryptionKey", encryptionKey);
/*    */         }
/*    */         
/* 34 */         String ltpaToken = (String)taskRequest.get("ltpaToken");
/* 35 */         boolean isWebsphere = J2EEServerUtil.getJ2EEAppServerType(request).equals("websphere");
/* 36 */         if ((ltpaToken != null) || (isWebsphere)) {
/* 37 */           if (isWebsphere) {
/* 38 */             taskRequest.put("ltpaToken", TaskManagerUtil.getLtpaToken(request));
/* 39 */           } else if (ltpaToken != null) {
/* 40 */             taskRequest.put("ltpaToken", ltpaToken);
/*    */           }
/*    */         }
/* 43 */         taskRequest.put("serverType", J2EEServerUtil.getJ2EEAppServerType(request));
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 48 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\RescheduleAsyncTaskOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */