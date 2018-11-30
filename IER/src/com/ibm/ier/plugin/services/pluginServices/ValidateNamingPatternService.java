/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.exception.MessageInfo;
/*    */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*    */ import com.ibm.jarm.api.util.NamingUtils;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class ValidateNamingPatternService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 17 */     return "ierValidateNamingPattern";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 21 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 23 */     JSONObject messageObject = null;
/* 24 */     String pattern = request.getParameter("ier_patternString");
/* 25 */     if ((pattern != null) && (pattern.length() > 0)) {
/*    */       try {
/* 27 */         NamingUtils.isNamingPatternValid(pattern);
/*    */       } catch (RMRuntimeException e) {
/* 29 */         MessageInfo info = e.getMessageInfo();
/* 30 */         messageObject = new JSONObject();
/* 31 */         messageObject.put("text", info.getFormattedMessage());
/* 32 */         messageObject.put("explanation", info.getExplanation());
/* 33 */         messageObject.put("userResponse", info.getAction());
/*    */       } catch (Exception e) {
/* 35 */         messageObject = new JSONObject();
/* 36 */         messageObject.put("text", e.getLocalizedMessage());
/*    */       }
/*    */     }
/* 39 */     JSONObject responseObject = new JSONObject();
/* 40 */     responseObject.put("valid", Boolean.valueOf(messageObject == null));
/* 41 */     if (messageObject != null) {
/* 42 */       responseObject.put("message", messageObject);
/*    */     }
/* 44 */     setCompletedJSONResponseObject(responseObject);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\ValidateNamingPatternService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */