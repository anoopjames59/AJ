/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.DispositionSchedule;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetDispositionSchedulesService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 53 */     return "ierGetDispositionSchedules";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 57 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 59 */     JSONObject jsonResponse = new JSONObject();
/* 60 */     FilePlanRepository fp_repo = getFilePlanRepository();
/*    */     
/* 62 */     JSONArray dispSchedulesJSON = new JSONArray();
/* 63 */     List<DispositionSchedule> dispSchedules = fp_repo.getDispositionSchedules(RMPropertyFilter.MinimumPropertySet);
/* 64 */     for (DispositionSchedule dispSchedule : dispSchedules) {
/* 65 */       dispSchedulesJSON.add(MediatorUtil.createEntityItemJSONObject(dispSchedule, MinimumPropertiesUtil.getPropertySetList(EntityType.DispositionSchedule), this.servletRequest));
/*    */     }
/*    */     
/*    */ 
/* 69 */     jsonResponse.put("dispositionSchedules", dispSchedulesJSON);
/*    */     
/* 71 */     setCompletedJSONResponseObject(jsonResponse);
/*    */     
/* 73 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetDispositionSchedulesService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */