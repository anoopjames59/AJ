/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.RecordVolumeContainer;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.jarm.api.util.NamingUtils;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetNextVolumeNameService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 20 */     return "ierGetNextVolumeNameService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 24 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 26 */     JSONObject results = new JSONObject();
/* 27 */     String parentContainerId = request.getParameter("ier_id");
/* 28 */     if (parentContainerId != null)
/*    */     {
/* 30 */       RecordVolumeContainer container = (RecordVolumeContainer)RMFactory.Container.fetchInstance(getFilePlanRepository(), EntityType.RecordFolder, IERUtil.getIdFromDocIdString(parentContainerId), RMPropertyFilter.MinimumPropertySet);
/* 31 */       String name = NamingUtils.generateNextVolumeName(container);
/* 32 */       results.put("name", name);
/*    */     }
/*    */     
/* 35 */     setCompletedJSONResponseObject(results);
/*    */     
/* 37 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetNextVolumeNameService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */