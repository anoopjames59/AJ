/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.BulkItemResult;
/*    */ import com.ibm.jarm.api.core.BulkOperation;
/*    */ import com.ibm.jarm.api.core.BulkOperation.EntityDescription;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ public class PlaceOnHoldService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 27 */     return "ierPlaceOnHoldService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 32 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 34 */     List<BulkItemResult> results = null;
/*    */     try {
/* 36 */       JSONArray holdableArray = JSONArray.parse(request.getParameter("ier_holdables"));
/* 37 */       JSONArray holdArray = JSONArray.parse(request.getParameter("ier_holds"));
/*    */       
/* 39 */       FilePlanRepository repository = getFilePlanRepository();
/*    */       
/* 41 */       List<String> holdList = new ArrayList();
/* 42 */       for (Object o : holdArray) {
/* 43 */         JSONObject holdObject = (JSONObject)o;
/* 44 */         String id = IERUtil.getIdFromDocIdString((String)holdObject.get("id"));
/* 45 */         holdList.add(id);
/*    */       }
/*    */       
/* 48 */       List<BulkOperation.EntityDescription> entityDescriptions = new ArrayList();
/* 49 */       for (Object o : holdableArray) {
/* 50 */         JSONObject holdableObject = (JSONObject)o;
/* 51 */         String id = IERUtil.getIdFromDocIdString((String)holdableObject.get("id"));
/* 52 */         EntityType entityType = EntityType.getInstanceFromInt(Integer.parseInt((String)holdableObject.get("entityType")));
/* 53 */         entityDescriptions.add(new BulkOperation.EntityDescription(entityType, id));
/*    */       }
/*    */       
/* 56 */       if (!entityDescriptions.isEmpty()) {
/* 57 */         results = BulkOperation.placeHolds(repository, entityDescriptions, holdList);
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 61 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_PLACEONHOLD_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 66 */     if ((results != null) && (!results.isEmpty())) {
/* 67 */       if (results.size() == 1) {
/* 68 */         BulkItemResult result = (BulkItemResult)results.get(0);
/* 69 */         if (!result.wasSuccessful()) {
/* 70 */           throw IERUIRuntimeException.createUIRuntimeException(result.getException(), MessageCode.E_PLACEONHOLD_UNEXPECTED, new Object[] { result.getException().getLocalizedMessage() });
/*    */         }
/*    */       }
/*    */       else {
/* 74 */         Exception firstException = IERUtil.getFirstException(results);
/* 75 */         if (firstException != null) {
/* 76 */           throw IERUIRuntimeException.createUIRuntimeException(firstException, MessageCode.E_PLACE_ON_HOLD_SOME_FAILURES, new Object[] { firstException.getLocalizedMessage() });
/*    */         }
/*    */       }
/*    */     }
/* 80 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\PlaceOnHoldService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */