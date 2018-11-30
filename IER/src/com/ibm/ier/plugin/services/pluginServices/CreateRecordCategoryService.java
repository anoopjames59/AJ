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
/*     */ import com.ibm.jarm.api.core.DefensiblyDisposableContainerParent;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.Container;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.RecordCategory;
/*     */ import com.ibm.jarm.api.core.RecordCategoryContainer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreateRecordCategoryService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  72 */     return "ierCreateRecordCategoryService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  76 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  79 */       String className = request.getParameter("ier_className");
/*  80 */       String parentFolderId = request.getParameter("ier_parentFolderId");
/*     */       
/*  82 */       JSONObject requestContent = getRequestContent();
/*  83 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*  84 */       JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/*     */       
/*  86 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*     */       
/*     */ 
/*  89 */       RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(fp_repository, className, request), criterias, fp_repository);
/*     */       
/*     */ 
/*  92 */       String dispositionAuthority = request.getParameter("AuthorisingStatute");
/*  93 */       if (dispositionAuthority != null) {
/*  94 */         props.putStringValue("AuthorisingStatute", dispositionAuthority);
/*     */       }
/*     */       
/*  97 */       String scheduleInheritedFrom = request.getParameter("DisposalScheduleInheritedFrom");
/*  98 */       if (scheduleInheritedFrom != null) {
/*  99 */         props.putGuidValue("DisposalScheduleInheritedFrom", scheduleInheritedFrom);
/*     */       }
/* 101 */       String categoryName = props.getStringValue("RecordCategoryName");
/* 102 */       boolean valid = ValidationUtil.validateRecordContainerNames(categoryName);
/* 103 */       if (!valid) {
/* 104 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_CONTAINER_INVALID_NAME, new Object[0]);
/*     */       }
/*     */       
/* 107 */       List<RMPermission> permissionList = IERPermission.getPermissionsFromJSON(permissionsJSON);
/*     */       
/*     */ 
/* 110 */       parentFolderId = IERUtil.getIdFromDocIdString(parentFolderId);
/*     */       
/*     */ 
/* 113 */       Container parentContainer = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, parentFolderId, RMPropertyFilter.MinimumPropertySet);
/* 114 */       if ((parentContainer instanceof RecordCategoryContainer))
/*     */       {
/* 116 */         RecordCategoryContainer rc = (RecordCategoryContainer)parentContainer;
/* 117 */         RecordCategory newRc = null;
/*     */         
/* 119 */         String ddRetentionPeriodYears = request.getParameter("retentionPeriodYears");
/* 120 */         String ddRetentionPeriodMonths = request.getParameter("retentionPeriodMonths");
/* 121 */         String ddRetentionPeriodDays = request.getParameter("retentionPeriodDays");
/* 122 */         String ddRetentionTriggerPropertyName = request.getParameter("retentionTriggerPropertyName");
/*     */         
/* 124 */         Logger.logDebug(this, "serviceExecute", "Defensible Disposal Settings..triggerPropertyName: " + ddRetentionTriggerPropertyName + " years: " + ddRetentionPeriodYears + " months: " + ddRetentionPeriodMonths + " days: " + ddRetentionPeriodDays);
/*     */         
/*     */ 
/*     */ 
/* 128 */         if ((ddRetentionPeriodYears != null) && (ddRetentionPeriodMonths != null) && (ddRetentionPeriodDays != null) && (ddRetentionTriggerPropertyName != null))
/*     */         {
/* 130 */           if ((rc instanceof DefensiblyDisposableContainerParent)) {
/* 131 */             DefensiblyDisposableContainerParent defensiblyDisposableContainer = (DefensiblyDisposableContainerParent)rc;
/* 132 */             newRc = (RecordCategory)defensiblyDisposableContainer.addDefensiblyDisposableContainer(ddRetentionTriggerPropertyName, Integer.parseInt(ddRetentionPeriodYears), Integer.parseInt(ddRetentionPeriodMonths), Integer.parseInt(ddRetentionPeriodDays), props, permissionList, null);
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 137 */           newRc = rc.addRecordCategory(className, props, permissionList);
/*     */         }
/*     */         
/* 140 */         String dispScheduleId = request.getParameter("ier_dispositionScheduleId");
/*     */         
/* 142 */         Logger.logDebug(this, "serviceExecute", "Legacy disposition schedule..dispScheduleId: " + dispScheduleId);
/*     */         
/* 144 */         if (dispScheduleId != null) {
/* 145 */           String schedulePropagationLevel = request.getParameter("schedulePropagationLevel");
/*     */           
/* 147 */           Logger.logDebug(this, "serviceExecute", "Legacy disposition schedule..schedulePropagationLevel: " + schedulePropagationLevel + " dispScheduleId: " + dispScheduleId + " dispositionAuthority: " + dispositionAuthority);
/*     */           
/*     */ 
/* 150 */           SchedulePropagation level = SchedulePropagation.NoPropagation;
/* 151 */           if (schedulePropagationLevel != null) {
/* 152 */             level = SchedulePropagation.getInstanceFromInt(Integer.parseInt(schedulePropagationLevel));
/*     */           }
/*     */           
/* 155 */           DispositionSchedule dispSchedule = RMFactory.DispositionSchedule.fetchInstance(fp_repository, IERUtil.getIdFromDocIdString(dispScheduleId), RMPropertyFilter.MinimumPropertySet);
/*     */           
/* 157 */           newRc.assignDispositionSchedule(dispSchedule, level);
/*     */         }
/*     */         
/* 160 */         JSONObject resultJSON = new JSONObject();
/* 161 */         resultJSON.put("item", MediatorUtil.createEntityItemJSONObject(newRc, MinimumPropertiesUtil.getPropertySetList(EntityType.RecordCategory), this.servletRequest));
/* 162 */         resultJSON.put("parent", MediatorUtil.createEntityItemJSONObject(parentContainer, MinimumPropertiesUtil.getPropertySetList(EntityType.RecordCategory), this.servletRequest));
/*     */         
/* 164 */         setCompletedJSONResponseObject(resultJSON);
/*     */       }
/*     */       else {
/* 167 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_RECCAT_INVALID_LOC, new Object[] { parentContainer.getName() });
/*     */       }
/*     */     } catch (Exception exp) {
/* 170 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_RECCAT_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*     */     }
/*     */     
/* 173 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateRecordCategoryService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */