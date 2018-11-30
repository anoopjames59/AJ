/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERConditionsUtil;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.ier.plugin.util.ValidationUtil;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Hold;
/*    */ import com.ibm.jarm.api.core.RMFactory.Hold;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SaveHoldService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 30 */     return "ierSaveHoldService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 34 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 37 */       JSONObject requestContent = getRequestContent();
/* 38 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/* 39 */       FilePlanRepository repository = getFilePlanRepository();
/*    */       
/* 41 */       RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(repository, "RecordHold", request), criterias, repository);
/*    */       
/* 43 */       String holdName = props.getStringValue("HoldName");
/* 44 */       if (!ValidationUtil.validateHoldName(holdName)) {
/* 45 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_HOLD_INVALID_NAME, new Object[0]);
/*    */       }
/* 47 */       P8Connection conn = createP8Connection();
/* 48 */       String conditionXml = IERConditionsUtil.serializeConditionsFromJSON((JSONArray)requestContent.get("ier_conditions"), request, repository, conn);
/*    */       
/* 50 */       Hold hold = null;
/* 51 */       String holdId = (String)requestContent.get("ier_holdId");
/* 52 */       if ((holdId != null) && (holdId.length() > 0)) {
/* 53 */         holdId = IERUtil.getIdFromDocIdString(holdId);
/* 54 */         hold = RMFactory.Hold.fetchInstance(repository, holdId, null);
/*    */       } else {
/* 56 */         hold = RMFactory.Hold.createInstance(repository);
/*    */       }
/* 58 */       hold.setHoldName(holdName);
/* 59 */       if (props.isPropertyPresent("HoldReason")) {
/* 60 */         hold.setHoldReason(props.getStringValue("HoldReason"));
/*    */       }
/* 62 */       if (props.isPropertyPresent("HoldType")) {
/* 63 */         hold.setHoldType(props.getStringValue("HoldType"));
/*    */       }
/* 65 */       if (props.isPropertyPresent("Active")) {
/* 66 */         Boolean active = props.getBooleanValue("Active");
/* 67 */         if (active != null) {
/* 68 */           hold.setActiveState(active.booleanValue());
/*    */         }
/*    */       }
/* 71 */       if (conditionXml != null) {
/* 72 */         hold.setSweepState(Integer.valueOf(1));
/* 73 */         hold.setConditionXML(conditionXml);
/*    */       }
/* 75 */       hold.save(RMRefreshMode.NoRefresh);
/*    */     } catch (Exception e) {
/* 77 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_HOLD_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 80 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\SaveHoldService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */