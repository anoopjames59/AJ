/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.DispositionActionType;
/*     */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.DispositionAction;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.DispositionTrigger;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionAction;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionTrigger;
/*     */ import com.ibm.jarm.api.core.RMFactory.WorkflowDefinition;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
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
/*     */ public class CreateSimpleDispositionScheduleService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public static final String ID_TRIGGER_RC = "{6DDC1BF0-47D8-411B-87CD-E28AD34E677E}";
/*     */   public static final String ID_TRIGGER_RF = "{95846F55-C1F9-41F8-9303-4453A242D149}";
/*     */   public static final String ID_TRIGGER_RV = "{5A6C45F9-E645-43F8-8438-1FD161D123D1}";
/*     */   public static final String ID_TRIGGER_RD = "{E8717DED-E591-4CC0-8AD7-CDA9FE2BB345}";
/*     */   public static final String ID_ACTION_AUTODESTROY = "{C1A83456-8940-4920-9F66-B737D3388107}";
/*     */   public static final String ID_ACTION_DESTROY = "{782C9F51-0FBF-4CB5-95E2-13DF238D0297}";
/*     */   public static final String RM_INTERNAL_SYSTEM = "RMInternalSystem";
/*     */   public static final String ID_DESTORY_WF = "{8E8558FA-7630-4338-AA71-59903700006C}";
/*     */   
/*     */   public String getId()
/*     */   {
/*  78 */     return "ierCreateSimpleDispositionScheduleService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  82 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  85 */       String aggregationEntityType = request.getParameter("ier_aggregationType");
/*  86 */       String dispositionCutoffAction = request.getParameter("ier_dispositionCutoffAction");
/*  87 */       String dispositionEventOffSet = request.getParameter("ier_dispositionEventOffset");
/*  88 */       String dispositionCutoffBase = request.getParameter("ier_dispositionEventCutoffBase");
/*     */       
/*  90 */       JSONObject requestContent = getRequestContent();
/*  91 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*     */       
/*  93 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*     */       
/*     */ 
/*  96 */       DispositionSchedule ds = RMFactory.DispositionSchedule.createInstance(fp_repository);
/*     */       
/*     */ 
/*  99 */       RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, "DisposalSchedule", request);
/* 100 */       MediatorUtil.setProperties(cd, criterias, fp_repository, ds.getProperties());
/*     */       
/*     */ 
/* 103 */       ds.setDispositionTigger(getDispositionTrigger(fp_repository, EntityType.getInstanceFromInt(Integer.valueOf(aggregationEntityType).intValue())));
/* 104 */       String[] offset = dispositionEventOffSet.split(";");
/* 105 */       ds.setDispositionEventOffset(Integer.valueOf(offset[0]), Integer.valueOf(offset[1]), Integer.valueOf(offset[2]));
/*     */       
/*     */ 
/* 108 */       ds.setCutoffAction(getDispositionAction(fp_repository, DispositionActionType.getInstanceFromInt(Integer.valueOf(dispositionCutoffAction).intValue())));
/* 109 */       ds.setCutoffBase(dispositionCutoffBase);
/* 110 */       ds.save(RMRefreshMode.NoRefresh);
/*     */     }
/*     */     catch (Exception exp) {
/* 113 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_APP_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*     */     }
/*     */     
/* 116 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   private DispositionTrigger getDispositionTrigger(FilePlanRepository fp, EntityType type) {
/* 120 */     String id = null;
/* 121 */     if (type == EntityType.RecordCategory) {
/* 122 */       id = "{6DDC1BF0-47D8-411B-87CD-E28AD34E677E}";
/* 123 */     } else if ((type == EntityType.RecordFolder) || (type == EntityType.PhysicalContainer) || (type == EntityType.PhysicalRecordFolder) || (type == EntityType.ElectronicRecordFolder) || (type == EntityType.HybridRecordFolder))
/*     */     {
/* 125 */       id = "{95846F55-C1F9-41F8-9303-4453A242D149}";
/* 126 */     } else if (type == EntityType.RecordVolume) {
/* 127 */       id = "{5A6C45F9-E645-43F8-8438-1FD161D123D1}";
/* 128 */     } else if ((type == EntityType.Record) || (type == EntityType.ElectronicRecord) || (type == EntityType.PhysicalRecord)) {
/* 129 */       id = "{E8717DED-E591-4CC0-8AD7-CDA9FE2BB345}";
/*     */     }
/*     */     try
/*     */     {
/* 133 */       return RMFactory.DispositionTrigger.fetchInstance(fp, id, RMPropertyFilter.MinimumPropertySet);
/*     */     }
/*     */     catch (RMRuntimeException exp)
/*     */     {
/* 137 */       if (exp.getErrorCode() == RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE) {
/* 138 */         String name = null;
/* 139 */         String aggregation = null;
/* 140 */         String conditionXML = null;
/* 141 */         if (type == EntityType.RecordCategory) {
/* 142 */           name = "RMCatInternalTrig";
/* 143 */           aggregation = "RecordCategory";
/* 144 */           conditionXML = "<response xmlns=\"http://filenet.com/namespaces/wcm/apps/1.0\"><objectset><count>1</count><customobject><properties><property><symname>RecordCategoryName</symname><displayname>Record Category Name</displayname><propertytype>8</propertytype><value/><operator>IS NOT NULL</operator><jointype>OR</jointype></property></properties><aggregation>RecordCategory</aggregation><sql> SELECT OIID , ID , RMENTITYTYPE FROM RecordCategory WHERE ( ([RecordCategoryName] IS NOT NULL) )</sql></customobject></objectset></response>";
/*     */         }
/* 146 */         else if ((type == EntityType.RecordFolder) || (type == EntityType.PhysicalContainer) || (type == EntityType.PhysicalRecordFolder) || (type == EntityType.ElectronicRecordFolder) || (type == EntityType.HybridRecordFolder))
/*     */         {
/* 148 */           name = "RMFolderInternalTrig";
/* 149 */           aggregation = "RecordFolder";
/* 150 */           conditionXML = "<response xmlns=\"http://filenet.com/namespaces/wcm/apps/1.0\"><objectset><count>1</count><customobject><properties><property><symname>RecordFolderName</symname><displayname>Record Folder Name</displayname><propertytype>8</propertytype><value/><operator>IS NOT NULL</operator><jointype>OR</jointype></property></properties><aggregation>RecordCategory</aggregation><sql> SELECT OIID , ID , RMENTITYTYPE FROM RecordFolder WHERE ( ([RecordFolderName] IS NOT NULL) )</sql></customobject></objectset></response>";
/*     */         }
/* 152 */         else if (type == EntityType.RecordVolume) {
/* 153 */           name = "RMVolInternalTrig";
/* 154 */           aggregation = "Volume";
/* 155 */           conditionXML = "<response xmlns=\"http://filenet.com/namespaces/wcm/apps/1.0\"><objectset><count>1</count><customobject><properties><property><symname>VolumeName</symname><displayname>Volume Name</displayname><propertytype>8</propertytype><value/><operator>IS NOT NULL</operator><jointype>OR</jointype></property></properties><aggregation>Volume</aggregation><sql> SELECT OIID , ID , RMENTITYTYPE FROM Volume WHERE ( ([VolumeName] IS NOT NULL) )</sql></customobject></objectset></response>";
/*     */         }
/* 157 */         else if ((type == EntityType.Record) || (type == EntityType.ElectronicRecord) || (type == EntityType.PhysicalRecord)) {
/* 158 */           name = "RMRecInternalTrig";
/* 159 */           aggregation = "RecordInfo";
/* 160 */           conditionXML = "<response xmlns=\"http://filenet.com/namespaces/wcm/apps/1.0\"><objectset><count>1</count><customobject><properties><property><symname>DocumentTitle</symname><displayname>Document Title</displayname><propertytype>8</propertytype><value/><operator>IS NOT NULL</operator><jointype>OR</jointype></property></properties><aggregation>RecordInfo</aggregation><sql> SELECT OIID , ID , RMENTITYTYPE FROM RecordInfo WHERE ( ([DocumentTitle] IS NOT NULL) )</sql></customobject></objectset></response>";
/*     */         }
/* 162 */         DispositionTrigger dt = RMFactory.DispositionTrigger.createInstance(fp, DispositionTriggerType.InternalEventTrigger, id);
/* 163 */         dt.setAggregation(aggregation);
/* 164 */         dt.setTriggerName(name);
/* 165 */         dt.setConditionXML(conditionXML);
/* 166 */         RMProperties props = dt.getProperties();
/* 167 */         props.putStringValue("RMExternallyManagedBy", "RMInternalSystem");
/* 168 */         dt.save(RMRefreshMode.NoRefresh);
/* 169 */         return dt;
/*     */       }
/*     */     }
/* 172 */     return null;
/*     */   }
/*     */   
/*     */   private DispositionAction getDispositionAction(FilePlanRepository fp, DispositionActionType actionType)
/*     */   {
/* 177 */     String id = null;
/* 178 */     if (actionType == DispositionActionType.AutoDestroy) {
/* 179 */       id = "{C1A83456-8940-4920-9F66-B737D3388107}";
/* 180 */     } else if (actionType == DispositionActionType.Destroy) {
/* 181 */       id = "{782C9F51-0FBF-4CB5-95E2-13DF238D0297}";
/*     */     }
/*     */     try {
/* 184 */       return RMFactory.DispositionAction.fetchInstance(fp, id, RMPropertyFilter.MinimumPropertySet);
/*     */     }
/*     */     catch (RMRuntimeException exp) {
/* 187 */       if (exp.getErrorCode() == RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE) {
/* 188 */         DispositionAction da = RMFactory.DispositionAction.createInstance(fp, actionType, id);
/* 189 */         if (actionType == DispositionActionType.Destroy) {
/* 190 */           da.setActionName("RMIntDestAction");
/* 191 */           da.setAssociatedWorkflow(RMFactory.WorkflowDefinition.fetchInstance(fp, "{8E8558FA-7630-4338-AA71-59903700006C}", null));
/*     */         }
/*     */         else {
/* 194 */           da.setActionName("RMIntAutoDestAction");
/*     */         }
/* 196 */         RMProperties props = da.getProperties();
/* 197 */         props.putStringValue("RMExternallyManagedBy", "RMInternalSystem");
/* 198 */         da.save(RMRefreshMode.NoRefresh);
/* 199 */         return da;
/*     */       }
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateSimpleDispositionScheduleService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */