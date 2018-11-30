/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ecm.extension.PluginServiceCallbacks;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetAndSavePluginConfiguration extends com.ibm.ier.plugin.services.IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 14 */     return "ierGetAndSavePluginConfiguration";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 18 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 20 */     JSONObject requestContent = getRequestContent();
/* 21 */     String pluginConfiguration = null;
/* 22 */     if (requestContent != null) {
/* 23 */       pluginConfiguration = (String)requestContent.get("ierPluginConfiguration");
/*    */     }
/* 25 */     if (pluginConfiguration == null) {
/* 26 */       JSONObject jsonResponse = new JSONObject();
/* 27 */       jsonResponse.put("configuration", getBaseService().getPluginServiceCallbacks().loadConfiguration());
/*    */       
/* 29 */       setCompletedJSONResponseObject(jsonResponse);
/*    */     }
/*    */     else {
/* 32 */       getBaseService().getPluginServiceCallbacks().saveConfiguration(pluginConfiguration);
/*    */     }
/* 34 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetAndSavePluginConfiguration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */