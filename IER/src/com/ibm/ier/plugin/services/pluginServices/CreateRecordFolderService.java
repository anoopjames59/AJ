/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
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
/*     */ import com.ibm.jarm.api.constants.SchedulePropagation;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.Container;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.RecordFolder;
/*     */ import com.ibm.jarm.api.core.RecordFolderContainer;
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
/*     */ public class CreateRecordFolderService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  37 */     return "ierCreateRecordFolderService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  41 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  44 */       String className = request.getParameter("ier_className");
/*  45 */       String dispScheduleId = request.getParameter("ier_dispositionScheduleId");
/*  46 */       String parentFolderId = request.getParameter("ier_parentFolderId");
/*     */       
/*  48 */       JSONObject requestContent = getRequestContent();
/*  49 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*  50 */       JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/*     */       
/*  52 */       FilePlanRepository repository = getFilePlanRepository();
/*     */       
/*     */ 
/*  55 */       RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(repository, className, request), criterias, repository);
/*     */       
/*     */ 
/*  58 */       String dispositionAuthority = request.getParameter("AuthorisingStatute");
/*  59 */       if (dispositionAuthority != null) {
/*  60 */         props.putStringValue("AuthorisingStatute", dispositionAuthority);
/*     */       }
/*     */       
/*  63 */       String scheduleInheritedFrom = request.getParameter("DisposalScheduleInheritedFrom");
/*  64 */       props.putGuidValue("DisposalScheduleInheritedFrom", scheduleInheritedFrom);
/*     */       
/*  66 */       String folderName = props.getStringValue("RecordFolderName");
/*  67 */       boolean valid = ValidationUtil.validateRecordContainerNames(folderName);
/*  68 */       if (!valid) {
/*  69 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_CONTAINER_INVALID_NAME, new Object[0]);
/*     */       }
/*     */       
/*     */ 
/*  73 */       List<RMPermission> permissionList = IERPermission.getPermissionsFromJSON(permissionsJSON);
/*     */       
/*     */ 
/*  76 */       parentFolderId = IERUtil.getIdFromDocIdString(parentFolderId);
/*  77 */       Container parentContainer = RMFactory.Container.fetchInstance(repository, EntityType.Container, parentFolderId, RMPropertyFilter.MinimumPropertySet);
/*  78 */       if ((parentContainer instanceof RecordFolderContainer)) {
/*  79 */         RecordFolderContainer container = (RecordFolderContainer)parentContainer;
/*  80 */         RecordFolder folder = container.addRecordFolder(className, props, permissionList);
/*     */         
/*  82 */         if (dispScheduleId != null) {
/*  83 */           DispositionSchedule dispSchedule = RMFactory.DispositionSchedule.fetchInstance(repository, IERUtil.getIdFromDocIdString(dispScheduleId), RMPropertyFilter.MinimumPropertySet);
/*     */           
/*  85 */           String schedulePropagationLevel = request.getParameter("schedulePropagationLevel");
/*     */           
/*  87 */           SchedulePropagation level = SchedulePropagation.NoPropagation;
/*  88 */           if (schedulePropagationLevel != null) {
/*  89 */             level = SchedulePropagation.getInstanceFromInt(Integer.parseInt(schedulePropagationLevel));
/*     */           }
/*  91 */           folder.assignDispositionSchedule(dispSchedule, level);
/*     */         }
/*     */         
/*  94 */         JSONObject resultJSON = new JSONObject();
/*  95 */         resultJSON.put("item", MediatorUtil.createEntityItemJSONObject(folder, MinimumPropertiesUtil.getPropertySetList(EntityType.RecordCategory), this.servletRequest));
/*  96 */         resultJSON.put("parent", MediatorUtil.createEntityItemJSONObject(parentContainer, MinimumPropertiesUtil.getPropertySetList(EntityType.RecordCategory), this.servletRequest));
/*     */         
/*  98 */         setCompletedJSONResponseObject(resultJSON);
/*     */       }
/*     */       else {
/* 101 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_RECFOLDER_INVALID_LOC, new Object[] { parentContainer.getName() });
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 105 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_RECFOLDER_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*     */     }
/*     */     
/* 108 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateRecordFolderService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */