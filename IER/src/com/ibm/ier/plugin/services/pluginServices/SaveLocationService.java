/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.ier.plugin.util.ValidationUtil;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Location;
/*    */ import com.ibm.jarm.api.core.RMFactory.Location;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SaveLocationService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 28 */     return "ierSaveLocationService";
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
/* 39 */       RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(repository, "Location", request), criterias, repository);
/*    */       
/* 41 */       String locationName = props.getStringValue("LocationName");
/* 42 */       if (!ValidationUtil.validateLocationName(locationName)) {
/* 43 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_LOCATION_INVALID_NAME, new Object[0]);
/*    */       }
/*    */       
/* 46 */       Location location = null;
/* 47 */       String locationId = (String)requestContent.get("ier_locationId");
/* 48 */       if ((locationId != null) && (locationId.length() > 0)) {
/* 49 */         locationId = IERUtil.getIdFromDocIdString(locationId);
/* 50 */         location = RMFactory.Location.fetchInstance(repository, locationId, null);
/*    */       } else {
/* 52 */         location = RMFactory.Location.createInstance(repository);
/*    */       }
/* 54 */       location.setLocationName(locationName);
/* 55 */       if (props.isPropertyPresent("BarcodeID")) {
/* 56 */         location.setBarcode(props.getStringValue("BarcodeID"));
/*    */       }
/* 58 */       if (props.isPropertyPresent("RMEntityDescription")) {
/* 59 */         location.setDescription(props.getStringValue("RMEntityDescription"));
/*    */       }
/* 61 */       if (props.isPropertyPresent("Reviewer")) {
/* 62 */         location.setReviewer(props.getStringValue("Reviewer"));
/*    */       }
/* 64 */       location.save(RMRefreshMode.NoRefresh);
/*    */     } catch (Exception e) {
/* 66 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_LOCATION_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 69 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\SaveLocationService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */