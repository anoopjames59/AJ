/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.constants.DataType;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMClassDescription;
/*    */ import com.ibm.jarm.api.meta.RMClassDescription;
/*    */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetClassPropertyDescriptionsService extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 22 */     return "ierGetClassPropertyDescriptions";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 26 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 29 */       JSONArray propsArray = new JSONArray();
/* 30 */       FilePlanRepository repository = getFilePlanRepository();
/* 31 */       String className = request.getParameter("ier_className");
/*    */       
/* 33 */       if (className != null) {
/* 34 */         RMClassDescription classDesc = RMFactory.RMClassDescription.fetchInstance(repository, className, null);
/* 35 */         List<RMPropertyDescription> propDescList = classDesc.getPropertyDescriptions();
/* 36 */         for (RMPropertyDescription propDesc : propDescList) {
/* 37 */           if ((!propDesc.isSystemGenerated()) && (!propDesc.isSystemOwned())) {
/* 38 */             JSONObject prop = new JSONObject();
/* 39 */             prop.put("displayName", propDesc.getDisplayName());
/* 40 */             prop.put("symbolicName", propDesc.getSymbolicName());
/* 41 */             prop.put("dataType", propDesc.getDataType().name());
/* 42 */             prop.put("template_desc", propDesc.getDescriptiveText());
/*    */             
/* 44 */             propsArray.add(prop);
/*    */           }
/*    */         }
/*    */       }
/* 48 */       JSONObject items = new JSONObject();
/* 49 */       items.put("items", propsArray);
/*    */       
/* 51 */       JSONObject responseObject = new JSONObject();
/* 52 */       responseObject.put("datastore", items);
/* 53 */       setCompletedJSONResponseObject(responseObject);
/*    */     } catch (Exception e) {
/* 55 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_APP_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 58 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetClassPropertyDescriptionsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */