/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERConditionsUtil;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.DispositionTrigger;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Hold;
/*    */ import com.ibm.jarm.api.core.RMFactory.Hold;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.jarm.api.property.RMProperty;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetObjectConditionsService extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 25 */     return "ierGetObjectConditions";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 29 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 32 */       JSONArray conditionArray = null;
/* 33 */       FilePlanRepository repository = getFilePlanRepository();
/*    */       
/* 35 */       String objectId = request.getParameter("ier_id");
/* 36 */       String entityType = request.getParameter("ier_entityType");
/*    */       
/* 38 */       if (objectId != null) {
/* 39 */         objectId = IERUtil.getIdFromDocIdString(objectId);
/*    */         
/* 41 */         if (entityType != null) {
/* 42 */           EntityType type = EntityType.getInstanceFromInt(Integer.parseInt(entityType));
/* 43 */           if (type == EntityType.Hold) {
/* 44 */             Hold hold = RMFactory.Hold.fetchInstance(repository, objectId, null);
/* 45 */             if (hold != null) {
/* 46 */               conditionArray = IERConditionsUtil.convertConditionsFromXML(hold.getProperties().get("ConditionXML").getStringValue());
/*    */             }
/*    */           }
/* 49 */           if (type == EntityType.DispositionTrigger) {
/* 50 */             DispositionTrigger trigger = com.ibm.jarm.api.core.RMFactory.DispositionTrigger.fetchInstance(repository, objectId, null);
/* 51 */             if (trigger != null) {
/* 52 */               conditionArray = IERConditionsUtil.convertConditionsFromXML(trigger.getProperties().get("ConditionXML").getStringValue());
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/* 57 */       JSONObject responseObject = new JSONObject();
/* 58 */       responseObject.put("conditions", conditionArray);
/* 59 */       setCompletedJSONResponseObject(responseObject);
/*    */     } catch (Exception e) {
/* 61 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_APP_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 64 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetObjectConditionsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */