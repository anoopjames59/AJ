/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.IERPermission;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.ier.plugin.util.ValidationUtil;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.FilePlan;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.FilePlan;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.jarm.api.security.RMPermission;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EditFilePlanService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 33 */     return "ierEditFilePlanService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 38 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 41 */       String docId = request.getParameter("ier_entityId");
/* 42 */       String className = request.getParameter("ier_className");
/* 43 */       String repositoryId = request.getParameter("ier_fileplanRepositoryId");
/*    */       
/* 45 */       JSONObject requestContent = getRequestContent();
/* 46 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/* 47 */       JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/*    */       
/* 49 */       IERBaseService service = getBaseService();
/* 50 */       Repository repository = service.getRepository(service.getP8RepositoryId(repositoryId));
/* 51 */       if ((repository instanceof FilePlanRepository)) {
/* 52 */         FilePlanRepository fp_repository = (FilePlanRepository)repository;
/*    */         
/* 54 */         String id = IERUtil.getIdFromDocIdString(docId);
/* 55 */         FilePlan filePlan = RMFactory.FilePlan.fetchInstance(fp_repository, id, null);
/*    */         
/*    */ 
/* 58 */         RMProperties props = MediatorUtil.setProperties(SessionUtil.getClassDescription(fp_repository, className, request), criterias, fp_repository, filePlan.getProperties());
/*    */         
/*    */ 
/* 61 */         String filePlanName = props.getStringValue("ClassificationSchemeName");
/* 62 */         boolean valid = ValidationUtil.validateRecordContainerNames(filePlanName);
/* 63 */         if (!valid) {
/* 64 */           throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_CONTAINER_INVALID_NAME, new Object[0]);
/*    */         }
/*    */         
/* 67 */         List<RMPermission> permissionList = permissionsJSON != null ? IERPermission.getPermissionsFromJSON(permissionsJSON) : null;
/* 68 */         if (permissionList != null) {
/* 69 */           filePlan.setPermissions(permissionList);
/*    */         }
/*    */         
/* 72 */         filePlan.save(RMRefreshMode.Refresh);
/*    */       }
/*    */       else {
/* 75 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_FILEPLAN_INVALID_LOC, new Object[] { repository.getName() });
/*    */       }
/*    */     }
/*    */     catch (Exception exp) {
/* 79 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_FILEPLAN_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\EditFilePlanService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */