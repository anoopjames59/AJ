/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERPermission;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.constants.SchedulePropagation;
/*     */ import com.ibm.jarm.api.core.DispositionAllocatable;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.Container;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.RMFactory.RecordCategory;
/*     */ import com.ibm.jarm.api.core.RMFactory.RecordFolder;
/*     */ import com.ibm.jarm.api.core.RMFactory.RecordVolume;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ public class EditRecordContainerService extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  34 */     return "ierEditRecordContainerService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  40 */     String docId = request.getParameter("ier_entityId");
/*  41 */     String className = request.getParameter("ier_className");
/*     */     
/*  43 */     FilePlanRepository repository = getFilePlanRepository();
/*     */     
/*  45 */     JSONObject requestContent = getRequestContent();
/*  46 */     JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*  47 */     JSONArray permissionsJson = (JSONArray)requestContent.get("ier_permissions");
/*  48 */     List<RMPermission> permissionList = null;
/*  49 */     if (permissionsJson != null) {
/*  50 */       permissionList = IERPermission.getPermissionsFromJSON(permissionsJson);
/*     */     }
/*     */     
/*  53 */     RecordContainer recordContainer = null;
/*  54 */     String containerId = IERUtil.getIdFromDocIdString(docId);
/*  55 */     if (className.equals("RecordCategory")) {
/*  56 */       recordContainer = RMFactory.RecordCategory.fetchInstance(repository, containerId, null);
/*  57 */     } else if (className.equals("RecordFolder")) {
/*  58 */       recordContainer = RMFactory.RecordFolder.fetchInstance(repository, containerId, null);
/*  59 */     } else if (className.equals("Volume")) {
/*  60 */       recordContainer = RMFactory.RecordVolume.fetchInstance(repository, containerId, null);
/*     */     } else {
/*  62 */       recordContainer = (RecordContainer)RMFactory.Container.fetchInstance(repository, EntityType.Container, containerId, null);
/*     */     }
/*     */     
/*  65 */     RMProperties properties = MediatorUtil.setProperties(SessionUtil.getClassDescription(repository, className, request), criterias, repository, recordContainer.getProperties());
/*  66 */     if (permissionList != null) {
/*  67 */       recordContainer.setPermissions(permissionList);
/*     */     }
/*     */     
/*     */ 
/*  71 */     if (recordContainer.isADefensiblyDisposableContainer()) {
/*  72 */       String ddRetentionPeriodYears = request.getParameter("retentionPeriodYears");
/*  73 */       String ddRetentionPeriodMonths = request.getParameter("retentionPeriodMonths");
/*  74 */       String ddRetentionPeriodDays = request.getParameter("retentionPeriodDays");
/*  75 */       String ddRetentionTriggerPropertyName = request.getParameter("retentionTriggerPropertyName");
/*     */       
/*  77 */       if ((ddRetentionPeriodYears != null) && (ddRetentionPeriodMonths != null) && (ddRetentionPeriodDays != null) && (ddRetentionTriggerPropertyName != null)) {
/*  78 */         recordContainer.setTriggerPropertyName(ddRetentionTriggerPropertyName);
/*  79 */         recordContainer.setRetentionPeriod(Integer.parseInt(ddRetentionPeriodYears), Integer.parseInt(ddRetentionPeriodMonths), Integer.parseInt(ddRetentionPeriodDays));
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */     }
/*  86 */     else if ((recordContainer instanceof DispositionAllocatable)) {
/*  87 */       DispositionAllocatable dispositionContainer = (DispositionAllocatable)recordContainer;
/*  88 */       String schedulePropagationLevel = request.getParameter("schedulePropagationLevel");
/*  89 */       String dispScheduleId = request.getParameter("ier_dispositionScheduleId");
/*  90 */       String dispositionAuthority = request.getParameter("AuthorisingStatute");
/*     */       
/*  92 */       Logger.logDebug(this, "serviceExecute", "Legacy disposition schedule..schedulePropagationLevel: " + schedulePropagationLevel + " dispScheduleId: " + dispScheduleId + " dispositionAuthority: " + dispositionAuthority);
/*     */       
/*     */ 
/*  95 */       SchedulePropagation level = SchedulePropagation.NoPropagation;
/*  96 */       if (schedulePropagationLevel != null) {
/*  97 */         level = SchedulePropagation.getInstanceFromInt(Integer.parseInt(schedulePropagationLevel));
/*     */       }
/*     */       
/* 100 */       if (dispScheduleId != null) {
/* 101 */         DispositionSchedule dispSchedule = RMFactory.DispositionSchedule.fetchInstance(repository, IERUtil.getIdFromDocIdString(dispScheduleId), RMPropertyFilter.MinimumPropertySet);
/*     */         
/*     */ 
/* 104 */         Logger.logDebug(this, "serviceExecute", "Legacy disposition schedule..schedulePropagationName: " + dispSchedule.getName());
/*     */         
/* 106 */         if (dispSchedule != null) {
/* 107 */           dispositionContainer.assignDispositionSchedule(dispSchedule, level);
/*     */         }
/*     */       } else {
/* 110 */         DispositionSchedule dispSchedule = dispositionContainer.getAssignedSchedule();
/*     */         
/* 112 */         if ((dispSchedule != null) && (dispScheduleId == null)) {
/* 113 */           dispositionContainer.clearDispositionAssignment(level);
/*     */         }
/*     */       }
/*     */       
/* 117 */       properties.putStringValue("AuthorisingStatute", dispositionAuthority);
/*     */       
/*     */ 
/* 120 */       String scheduleInheritedFrom = request.getParameter("DisposalScheduleInheritedFrom");
/* 121 */       properties.putGuidValue("DisposalScheduleInheritedFrom", scheduleInheritedFrom);
/*     */     }
/*     */     
/*     */ 
/* 125 */     recordContainer.save(RMRefreshMode.Refresh);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\EditRecordContainerService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */