/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.FilePlan;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.NamingPattern;
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
/*    */ public class GetFilePlansService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 54 */     return "ierGetFilePlans";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 58 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 60 */     JSONObject jsonResponse = new JSONObject();
/* 61 */     FilePlanRepository fp_repo = getFilePlanRepository();
/*    */     
/* 63 */     JSONArray fileplansJSON = new JSONArray();
/* 64 */     List<FilePlan> fileplans = fp_repo.getFilePlans(RMPropertyFilter.MinimumPropertySet);
/*    */     
/*    */ 
/* 67 */     for (FilePlan fp : fileplans) {
/* 68 */       JSONObject fpProperties = MediatorUtil.createEntityItemJSONObject(fp, MinimumPropertiesUtil.getPropertySetList(EntityType.FilePlan), this.servletRequest);
/*    */       
/* 70 */       NamingPattern namingPattern = fp.getNamingPattern();
/* 71 */       if (namingPattern != null) {
/* 72 */         JSONObject namingPatternJSON = MediatorUtil.createEntityItemJSONObject(fp.getNamingPattern(), MinimumPropertiesUtil.getPropertySetList(EntityType.Pattern), this.servletRequest);
/*    */         
/* 74 */         fpProperties.put("namingPattern", namingPatternJSON);
/*    */       }
/*    */       
/* 77 */       fileplansJSON.add(fpProperties);
/*    */     }
/*    */     
/* 80 */     jsonResponse.put("fileplans", fileplansJSON);
/*    */     
/* 82 */     setCompletedJSONResponseObject(jsonResponse);
/*    */     
/* 84 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetFilePlansService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */