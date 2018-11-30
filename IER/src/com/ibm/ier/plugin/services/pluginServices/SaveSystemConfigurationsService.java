/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.SystemConfiguration;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ public class SaveSystemConfigurationsService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 21 */     return "ierSaveSystemConfigurationsService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 25 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 28 */       JSONObject requestContent = getRequestContent();
/* 29 */       JSONArray configArray = (JSONArray)requestContent.get("ier_systemConfigurations");
/* 30 */       repository = getFilePlanRepository();
/* 31 */       systemConfigurations = repository.getSystemConfigurations();
/* 32 */       for (Object o : configArray) {
/* 33 */         JSONObject configObject = (JSONObject)o;
/* 34 */         String name = (String)configObject.get("name");
/* 35 */         String value = (String)configObject.get("value");
/* 36 */         if ((name != null) && (value != null)) {
/* 37 */           SystemConfiguration config = (SystemConfiguration)systemConfigurations.get(name);
/* 38 */           if ((config != null) && (config.canBeUpdated()) && (!value.equals(config.getPropertyValue())))
/* 39 */             repository.putSystemConfiguration(name, value);
/*    */         }
/*    */       }
/*    */     } catch (Exception e) { FilePlanRepository repository;
/*    */       Map<String, SystemConfiguration> systemConfigurations;
/* 44 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_SYSTEM_CONFIGURATION_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 47 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\SaveSystemConfigurationsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */