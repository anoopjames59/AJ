/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERConditionsUtil;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.DispositionTrigger;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.DispositionTrigger;
/*    */ import com.ibm.jarm.api.meta.RMClassDescription;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreateTriggerService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 30 */     return "ierCreateTriggerService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 34 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 37 */       String triggerType = request.getParameter("ier_eventType");
/*    */       
/*    */ 
/* 40 */       JSONObject requestContent = getRequestContent();
/* 41 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/* 42 */       JSONArray conditions = (JSONArray)requestContent.get("ier_conditions");
/*    */       
/* 44 */       if (Logger.isDebugLogged()) {
/* 45 */         Logger.logDebug(this, "serviceExecute", request, "triggerType=" + triggerType + " critieries: " + criterias.toString() + " conditions: " + conditions.toString());
/*    */       }
/*    */       
/*    */ 
/*    */ 
/* 50 */       FilePlanRepository repository = getFilePlanRepository();
/*    */       
/*    */ 
/* 53 */       DispositionTrigger trigger = RMFactory.DispositionTrigger.createInstance(repository, DispositionTriggerType.getInstanceFromInt(Integer.parseInt(triggerType)));
/*    */       
/* 55 */       RMClassDescription cd = SessionUtil.getClassDescription(repository, "DisposalTrigger", request);
/* 56 */       RMProperties props = MediatorUtil.setProperties(cd, criterias, repository, trigger.getProperties());
/*    */       
/*    */ 
/* 59 */       String triggerName = props.getStringValue("DisposalTriggerName");
/* 60 */       trigger.setTriggerName(triggerName);
/* 61 */       trigger.setAggregation(props.getStringValue("AGGREGATION"));
/*    */       
/* 63 */       if (conditions != null) {
/* 64 */         P8Connection conn = createP8Connection();
/* 65 */         trigger.setConditionXML(IERConditionsUtil.serializeConditionsFromJSON(conditions, request, repository, conn));
/*    */       }
/*    */       
/* 68 */       trigger.save(RMRefreshMode.NoRefresh);
/*    */     } catch (Exception e) {
/* 70 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_TRIGGER_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 73 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateTriggerService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */