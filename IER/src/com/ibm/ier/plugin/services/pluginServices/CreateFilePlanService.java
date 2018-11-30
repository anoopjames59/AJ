/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.IERPermission;
/*    */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.ier.plugin.util.ValidationUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.FilePlan;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
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
/*    */ public class CreateFilePlanService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 32 */     return "ierCreateFilePlanService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 37 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 40 */       String className = request.getParameter("ier_className");
/* 41 */       String repositoryId = request.getParameter("ier_fileplanRepositoryId");
/*    */       
/* 43 */       JSONObject requestContent = getRequestContent();
/* 44 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/* 45 */       JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/*    */       
/* 47 */       IERBaseService service = getBaseService();
/* 48 */       Repository repository = service.getRepository(service.getP8RepositoryId(repositoryId));
/* 49 */       if ((repository instanceof FilePlanRepository)) {
/* 50 */         FilePlanRepository fp_repository = (FilePlanRepository)repository;
/*    */         
/*    */ 
/* 53 */         RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(fp_repository, className, request), criterias, fp_repository);
/*    */         
/* 55 */         String filePlanName = props.getStringValue("ClassificationSchemeName");
/* 56 */         boolean valid = ValidationUtil.validateRecordContainerNames(filePlanName);
/* 57 */         if (!valid) {
/* 58 */           throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_CONTAINER_INVALID_NAME, new Object[0]);
/*    */         }
/*    */         
/* 61 */         List<RMPermission> permissionList = IERPermission.getPermissionsFromJSON(permissionsJSON);
/*    */         
/* 63 */         FilePlan fp = fp_repository.addFilePlan(className, props, permissionList);
/*    */         
/* 65 */         setCompletedJSONResponseObject(MediatorUtil.createEntityItemJSONObject(fp, MinimumPropertiesUtil.getPropertySetList(EntityType.FilePlan), this.servletRequest));
/*    */       }
/*    */       else
/*    */       {
/* 69 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_FILEPLAN_INVALID_LOC, new Object[] { repository.getName() });
/*    */       }
/*    */     }
/*    */     catch (Exception exp) {
/* 73 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_FILEPLAN_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 76 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateFilePlanService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */