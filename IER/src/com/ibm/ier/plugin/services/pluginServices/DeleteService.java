/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BulkItemResult;
/*     */ import com.ibm.jarm.api.core.BulkOperation;
/*     */ import com.ibm.jarm.api.core.BulkOperation.EntityDescription;
/*     */ import com.ibm.jarm.api.core.DispositionAction;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.DispositionTrigger;
/*     */ import com.ibm.jarm.api.core.FilePlan;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Hold;
/*     */ import com.ibm.jarm.api.core.Location;
/*     */ import com.ibm.jarm.api.core.NamingPattern;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionAction;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.RMFactory.Hold;
/*     */ import com.ibm.jarm.api.core.RMFactory.Location;
/*     */ import com.ibm.jarm.api.core.RMFactory.NamingPattern;
/*     */ import com.ibm.jarm.api.core.RMFactory.RecordType;
/*     */ import com.ibm.jarm.api.core.RMFactory.ReportDefinition;
/*     */ import com.ibm.jarm.api.core.RecordType;
/*     */ import com.ibm.jarm.api.core.ReportDefinition;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ public class DeleteService extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  41 */     return "ierDeleteService";
/*     */   }
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
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  60 */     Logger.logEntry(this, "serviceExecute", request);
/*  61 */     List<BulkItemResult> bulkResults = null;
/*  62 */     Exception firstException = null;
/*     */     
/*  64 */     String numberOfObjects = request.getParameter("ier_ndocs");
/*  65 */     int ndocs = Integer.parseInt(numberOfObjects);
/*  66 */     JSONObject deleteSuccessObjectResponse = new JSONObject();
/*     */     
/*     */     try
/*     */     {
/*  70 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */       List<BulkOperation.EntityDescription> entityDescriptions = new ArrayList();
/*  77 */       for (int i = 0; i < ndocs; i++)
/*     */       {
/*  79 */         String docId = request.getParameter("ier_docId" + i);
/*     */         try {
/*  81 */           String objectID = IERUtil.getIdFromDocIdString(docId);
/*  82 */           String objectEntityTypeStr = request.getParameter("ier_entityType" + i);
/*  83 */           EntityType objectEntityType = EntityType.getInstanceFromInt(Integer.parseInt(objectEntityTypeStr));
/*  84 */           switch (objectEntityType) {
/*     */           case Hold: 
/*  86 */             deleteHold(fp_repository, objectID);
/*  87 */             break;
/*     */           case Location: 
/*  89 */             deleteLocation(fp_repository, objectID);
/*  90 */             break;
/*     */           case Pattern: 
/*  92 */             deleteNamingPattern(fp_repository, objectID);
/*  93 */             break;
/*     */           case DispositionSchedule: 
/*  95 */             deleteDispositionSchedule(fp_repository, objectID);
/*  96 */             break;
/*     */           case DispositionAction: 
/*  98 */             deleteAction(fp_repository, objectID);
/*  99 */             break;
/*     */           case DispositionTrigger: 
/* 101 */             DispositionTrigger trigger = com.ibm.jarm.api.core.RMFactory.DispositionTrigger.getInstance(fp_repository, objectID);
/* 102 */             trigger.delete();
/* 103 */             break;
/*     */           case RecordType: 
/* 105 */             RecordType type = RMFactory.RecordType.getInstance(fp_repository, objectID);
/* 106 */             type.delete();
/* 107 */             break;
/*     */           case ReportDefinition: 
/* 109 */             ReportDefinition repDef = RMFactory.ReportDefinition.fetchInstance(fp_repository, objectID, null);
/* 110 */             repDef.deleteAllVersions();
/* 111 */             break;
/*     */           case FilePlan: 
/* 113 */             FilePlan filePlan = com.ibm.jarm.api.core.RMFactory.FilePlan.getInstance(fp_repository, objectID);
/* 114 */             filePlan.delete();
/* 115 */             break;
/*     */           
/*     */           case TransferMapping: 
/*     */           default: 
/* 119 */             entityDescriptions.add(new BulkOperation.EntityDescription(objectEntityType, objectID));
/*     */           }
/*     */           
/* 122 */           deleteSuccessObjectResponse.put(objectID, Boolean.valueOf(true));
/*     */         }
/*     */         catch (Exception exp) {
/* 125 */           firstException = exp;
/*     */         }
/*     */       }
/* 128 */       if (!entityDescriptions.isEmpty())
/*     */       {
/* 130 */         bulkResults = BulkOperation.delete(fp_repository, entityDescriptions);
/*     */       }
/*     */     }
/*     */     catch (Exception exp) {
/* 134 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_DELETE_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 140 */     BaseMediator mediator = getMediator();
/* 141 */     if ((bulkResults != null) && (!bulkResults.isEmpty())) {
/* 142 */       if (bulkResults.size() == 1) {
/* 143 */         BulkItemResult itemResult = (BulkItemResult)bulkResults.get(0);
/*     */         
/* 145 */         if (!itemResult.wasSuccessful()) {
/* 146 */           throw IERUIRuntimeException.createUIRuntimeException(itemResult.getException(), MessageCode.E_DELETE_UNEXPECTED, new Object[] { itemResult.getException().getLocalizedMessage() });
/*     */         }
/*     */       }
/*     */       else {
/* 150 */         for (BulkItemResult result : bulkResults) {
/* 151 */           if (!result.wasSuccessful()) {
/* 152 */             if (firstException == null) {
/* 153 */               firstException = result.getException();
/*     */             }
/* 155 */             deleteSuccessObjectResponse.remove(result.getEntityIdent());
/*     */           }
/*     */         }
/*     */         
/* 159 */         if (firstException != null) {
/* 160 */           mediator.addWarning(MessageCode.E_DELETE_SOME_FAILURES, new Object[] { firstException.getLocalizedMessage() });
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/* 166 */     else if (firstException != null) {
/* 167 */       if (ndocs == 1) {
/* 168 */         throw IERUIRuntimeException.createUIRuntimeException(firstException, MessageCode.E_DELETE_UNEXPECTED, new Object[] { firstException.getLocalizedMessage() });
/*     */       }
/* 170 */       mediator.addWarning(MessageCode.E_DELETE_SOME_FAILURES, new Object[] { firstException.getLocalizedMessage() });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 175 */     JSONObject completedResponse = new JSONObject();
/* 176 */     completedResponse.put("success", deleteSuccessObjectResponse);
/* 177 */     setCompletedJSONResponseObject(completedResponse);
/*     */     
/* 179 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   private void deleteHold(FilePlanRepository repository, String id) {
/* 183 */     Hold hold = RMFactory.Hold.getInstance(repository, id);
/* 184 */     if (hold != null) {
/* 185 */       hold.delete();
/*     */     }
/*     */   }
/*     */   
/*     */   private void deleteLocation(FilePlanRepository repository, String id) {
/* 190 */     Location location = RMFactory.Location.getInstance(repository, id);
/* 191 */     if (location != null) {
/* 192 */       location.delete();
/*     */     }
/*     */   }
/*     */   
/*     */   private void deleteNamingPattern(FilePlanRepository repository, String id) {
/* 197 */     NamingPattern pattern = RMFactory.NamingPattern.getInstance(repository, id);
/* 198 */     if (pattern != null) {
/* 199 */       pattern.delete();
/*     */     }
/*     */   }
/*     */   
/*     */   private void deleteAction(FilePlanRepository repository, String id) {
/* 204 */     DispositionAction action = RMFactory.DispositionAction.getInstance(repository, id);
/* 205 */     if (action != null) {
/* 206 */       action.delete();
/*     */     }
/*     */   }
/*     */   
/*     */   private void deleteDispositionSchedule(FilePlanRepository repository, String id) {
/* 211 */     DispositionSchedule schedule = RMFactory.DispositionSchedule.getInstance(repository, id);
/* 212 */     if (schedule != null) {
/* 213 */       schedule.delete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\DeleteService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */