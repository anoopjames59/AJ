/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.Container;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.RecordContainer;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.jarm.api.util.NamingUtils;
/*    */ import com.ibm.json.java.JSONObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetEntityNameFromNamingPatternService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public static final String NAMING_PATTERN_NAME = "nameFromNamingPattern";
/*    */   
/*    */   public String getId()
/*    */   {
/* 59 */     return "ierGetEntityNameFromNamingPattern";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 63 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 65 */     String namingPatternName = null;
/*    */     
/* 67 */     String containerId = request.getParameter("ier_entityId");
/*    */     
/*    */ 
/* 70 */     String entityTypeOfNameToRetrieve = request.getParameter("ier_entityType");
/*    */     
/* 72 */     FilePlanRepository repository = getFilePlanRepository();
/* 73 */     Container container = RMFactory.Container.fetchInstance(repository, EntityType.Container, IERUtil.getIdFromDocIdString(containerId), RMPropertyFilter.MinimumPropertySet);
/*    */     
/* 75 */     EntityType entityType = EntityType.getInstanceFromInt(Integer.parseInt(entityTypeOfNameToRetrieve));
/*    */     
/*    */ 
/* 78 */     if (entityType == EntityType.Record) {
/* 79 */       namingPatternName = NamingUtils.generateRecordNameFromPattern(container);
/* 80 */     } else if (entityType == EntityType.RecordFolder) {
/* 81 */       namingPatternName = NamingUtils.generateFolderNameFromPattern((RecordContainer)container);
/* 82 */     } else if (entityType == EntityType.RecordCategory) {
/* 83 */       namingPatternName = NamingUtils.generateCategoryNameFromPattern(container);
/*    */     } else {
/* 85 */       namingPatternName = null;
/*    */     }
/* 87 */     if (Logger.isDebugLogged()) {
/* 88 */       Logger.logDebug(this, "GetEntityNameFromNamingPattern", request, "Naming pattern generated: " + namingPatternName + " from container entity: " + containerId + " with this entity type: " + entityTypeOfNameToRetrieve);
/*    */     }
/*    */     
/*    */ 
/* 92 */     JSONObject jsonResponse = new JSONObject();
/* 93 */     jsonResponse.put("nameFromNamingPattern", namingPatternName);
/* 94 */     setCompletedJSONResponseObject(jsonResponse);
/*    */     
/* 96 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetEntityNameFromNamingPatternService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */