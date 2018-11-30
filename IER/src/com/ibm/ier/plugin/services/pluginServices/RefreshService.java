/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*    */ import com.ibm.jarm.api.core.BaseEntity;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.BaseEntity;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
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
/*    */ public class RefreshService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 55 */     return "ierRefreshService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 59 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 61 */     String className = request.getParameter("ier_className");
/* 62 */     String parentClass = request.getParameter("parentClassName");
/* 63 */     String parentFolderId = request.getParameter("ier_parentFolderId");
/* 64 */     String id = request.getParameter("ier_id");
/*    */     
/* 66 */     FilePlanRepository fp_repository = getFilePlanRepository();
/*    */     
/* 68 */     BaseEntity parent = null;
/* 69 */     if (parentFolderId != null) {
/* 70 */       parentFolderId = IERUtil.getIdFromDocIdString(parentFolderId);
/*    */       
/*    */ 
/* 73 */       if ((parentClass != null) && (parentFolderId != null)) {
/* 74 */         parent = RMFactory.BaseEntity.fetchInstance(fp_repository, parentClass, parentFolderId, RMPropertyFilter.MinimumPropertySet);
/*    */       }
/*    */     }
/* 77 */     BaseEntity item = null;
/* 78 */     if (id != null) {
/* 79 */       id = IERUtil.getIdFromDocIdString(id);
/* 80 */       item = RMFactory.BaseEntity.fetchInstance(fp_repository, className, id, RMPropertyFilter.MinimumPropertySet);
/*    */     }
/*    */     
/* 83 */     JSONObject resultJSON = new JSONObject();
/* 84 */     if (item != null) {
/* 85 */       resultJSON.put("item", MediatorUtil.createEntityItemJSONObject(item, MinimumPropertiesUtil.getPropertySetList(item.getEntityType()), this.servletRequest));
/*    */     }
/* 87 */     if (parent != null) {
/* 88 */       resultJSON.put("parent", MediatorUtil.createEntityItemJSONObject(parent, MinimumPropertiesUtil.getPropertySetList(parent.getEntityType()), this.servletRequest));
/*    */     }
/* 90 */     setCompletedJSONResponseObject(resultJSON);
/*    */     
/* 92 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\RefreshService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */