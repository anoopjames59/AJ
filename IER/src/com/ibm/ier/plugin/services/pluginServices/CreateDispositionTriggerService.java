/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.jarm.api.constants.DispositionActionType;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.DispositionAction;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.DispositionAction;
/*    */ import com.ibm.jarm.api.core.RMWorkflowDefinition;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreateDispositionTriggerService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 28 */     return "ierCreateDispositionTriggerService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 32 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 35 */       JSONObject requestContent = getRequestContent();
/* 36 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/* 37 */       FilePlanRepository repository = getFilePlanRepository();
/*    */       
/* 39 */       RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(repository, "DisposalTrigger", request), criterias, repository);
/*    */       
/* 41 */       String actionType = props.getStringValue("ActionType");
/* 42 */       DispositionAction action = RMFactory.DispositionAction.createInstance(repository, DispositionActionType.getInstanceFromInt(Integer.parseInt(actionType)));
/*    */       
/* 44 */       String actionName = props.getStringValue("ActionName");
/* 45 */       action.setActionName(actionName);
/*    */       
/* 47 */       if (props.isPropertyPresent("DefaultWorkflow")) {
/* 48 */         Object obj = props.getObjectValue("DefaultWorkflow");
/* 49 */         if ((obj != null) && ((obj instanceof RMWorkflowDefinition))) {
/* 50 */           RMWorkflowDefinition def = (RMWorkflowDefinition)obj;
/* 51 */           action.setAssociatedWorkflow(def);
/*    */         }
/*    */       }
/*    */       
/* 55 */       if (props.isPropertyPresent("RMEntityDescription")) {
/* 56 */         action.setDescription(props.getStringValue("RMEntityDescription"));
/*    */       }
/*    */       
/* 59 */       action.save(RMRefreshMode.NoRefresh);
/*    */     } catch (Exception e) {
/* 61 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_ACTION_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 64 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateDispositionTriggerService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */