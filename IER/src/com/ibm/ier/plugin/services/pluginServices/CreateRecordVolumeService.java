/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERPermission;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.ier.plugin.util.ValidationUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.Container;
/*     */ import com.ibm.jarm.api.core.RecordVolume;
/*     */ import com.ibm.jarm.api.core.RecordVolumeContainer;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreateRecordVolumeService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  39 */     return "ierCreateRecordVolumeService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  43 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  46 */       String className = request.getParameter("ier_className");
/*  47 */       String parentFolderId = request.getParameter("ier_parentFolderId");
/*     */       
/*  49 */       JSONObject requestContent = getRequestContent();
/*  50 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*  51 */       JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/*     */       
/*  53 */       FilePlanRepository repository = getFilePlanRepository();
/*     */       
/*     */ 
/*  56 */       RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(repository, className, request), criterias, repository);
/*     */       
/*  58 */       String volumeName = props.getStringValue("VolumeName");
/*  59 */       boolean valid = ValidationUtil.validateRecordContainerNames(volumeName);
/*  60 */       if (!valid) {
/*  61 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_CONTAINER_INVALID_NAME, new Object[0]);
/*     */       }
/*     */       
/*     */ 
/*  65 */       List<RMPermission> permissionList = IERPermission.getPermissionsFromJSON(permissionsJSON);
/*     */       
/*     */ 
/*  68 */       parentFolderId = IERUtil.getIdFromDocIdString(parentFolderId);
/*  69 */       Container parentContainer = RMFactory.Container.fetchInstance(repository, EntityType.Container, parentFolderId, RMPropertyFilter.MinimumPropertySet);
/*  70 */       if ((parentContainer instanceof RecordVolumeContainer)) {
/*  71 */         RecordVolumeContainer container = (RecordVolumeContainer)parentContainer;
/*  72 */         RecordVolume volume = container.addRecordVolume(className, volumeName, props, permissionList);
/*     */         
/*  74 */         JSONObject resultJSON = new JSONObject();
/*  75 */         resultJSON.put("item", MediatorUtil.createEntityItemJSONObject(volume, MinimumPropertiesUtil.getPropertySetList(EntityType.RecordCategory), this.servletRequest));
/*  76 */         resultJSON.put("parent", MediatorUtil.createEntityItemJSONObject(parentContainer, MinimumPropertiesUtil.getPropertySetList(EntityType.RecordCategory), this.servletRequest));
/*     */         
/*  78 */         setCompletedJSONResponseObject(resultJSON);
/*     */       }
/*     */       else {
/*  81 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_RECVOLUME_INVALID_LOC, new Object[] { parentContainer.getName() });
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  85 */       if ((e instanceof RMRuntimeException)) {
/*  86 */         RMRuntimeException exp = (RMRuntimeException)e;
/*  87 */         if (exp.getErrorCode() == RMErrorCode.RAL_ADD_RECORDVOLUME_FAILED) {
/*  88 */           Throwable expCause = exp.getCause();
/*  89 */           if ((expCause instanceof EngineRuntimeException)) {
/*  90 */             EngineRuntimeException ceExp = (EngineRuntimeException)expCause;
/*  91 */             if (ceExp.getExceptionCode() == ExceptionCode.E_NOT_UNIQUE) {
/*  92 */               throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_CONTAINER_DUPLICATE_NAME, new Object[] { e.getLocalizedMessage() });
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  98 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_RECVOLUME_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*     */     }
/*     */     
/* 101 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateRecordVolumeService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */