/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ecm.configuration.Config;
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.PEUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.BulkItemResult;
/*     */ import com.ibm.jarm.api.core.Dispositionable;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.BaseEntity;
/*     */ import com.ibm.jarm.api.exception.MessageInfo;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import filenet.vw.api.VWSession;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InitiateDispositionService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  39 */     return "ierInitiateDispositionService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  43 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  45 */     String itemId = request.getParameter("ier_id");
/*  46 */     String entityType = request.getParameter("ier_entityType");
/*  47 */     String numberOfDocuments = request.getParameter("ier_ndocs");
/*     */     
/*  49 */     List<BulkItemResult> results = new ArrayList();
/*  50 */     if ((numberOfDocuments != null) && (Integer.parseInt(numberOfDocuments) > 0)) {
/*  51 */       String[] ids = itemId.split(",");
/*  52 */       String[] entityTypes = entityType.split(",");
/*  53 */       for (int i = 0; i < ids.length; i++) {
/*  54 */         List<BulkItemResult> oneResult = initiateDisposition(ids[i], entityTypes[i], request);
/*  55 */         if (oneResult != null) {
/*  56 */           results.addAll(oneResult);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/*  61 */       List<BulkItemResult> oneResult = initiateDisposition(itemId, entityType, request);
/*  62 */       if (oneResult != null) {
/*  63 */         results.addAll(oneResult);
/*     */       }
/*     */     }
/*     */     
/*  67 */     JSONObject completedJSON = new JSONObject();
/*  68 */     if (results.size() == 0) {
/*  69 */       completedJSON.put("message", MessageResources.getLocalizedMessage("initiateDispositionNoResults", new Object[0]));
/*     */     }
/*  71 */     else if (results.size() == 1) {
/*  72 */       BulkItemResult result = (BulkItemResult)results.get(0);
/*  73 */       if (!result.wasSuccessful()) {
/*  74 */         RMRuntimeException exp = result.getException();
/*  75 */         throw exp;
/*     */       }
/*     */       
/*  78 */       completedJSON.put("message", MessageResources.getLocalizedMessage("initiateDispositionAllSuccessful", new Object[0]));
/*     */     }
/*     */     else {
/*  81 */       boolean errorFound = false;
/*  82 */       RMRuntimeException firstException = null;
/*  83 */       for (BulkItemResult result : results) {
/*  84 */         if (!result.wasSuccessful()) {
/*  85 */           errorFound = true;
/*  86 */           firstException = result.getException();
/*  87 */           break;
/*     */         }
/*     */       }
/*     */       
/*  91 */       if (errorFound) {
/*  92 */         throw IERUIRuntimeException.createUIRuntimeException(firstException, MessageCode.E_INIT_DISP_SOME_FAILURES, new Object[] { firstException.getMessageInfo().getFormattedMessage() });
/*     */       }
/*  94 */       completedJSON.put("message", MessageResources.getLocalizedMessage("initiateDispositionAllSuccessful", new Object[0]));
/*     */     }
/*     */     
/*     */ 
/*  98 */     setCompletedJSONResponseObject(completedJSON);
/*     */     
/* 100 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   public List<BulkItemResult> initiateDisposition(String itemId, String entityType, HttpServletRequest request) {
/* 104 */     FilePlanRepository repository = getFilePlanRepository();
/*     */     
/* 106 */     BaseEntity entity = RMFactory.BaseEntity.fetchInstance(repository, EntityType.getInstanceFromInt(Integer.parseInt(entityType)), itemId, RMPropertyFilter.MinimumPropertySet);
/* 107 */     if ((entity instanceof Dispositionable)) {
/* 108 */       Dispositionable dispositionEntity = (Dispositionable)entity;
/*     */       
/* 110 */       String connectionPointName = Config.getRepositoryConfig(this.baseService.getServletRequest()).getConnectionPoint();
/* 111 */       if (IERUtil.isNullOrEmpty(connectionPointName)) {
/* 112 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_MISSING_CONN_POINT_NAME, new Object[0]);
/*     */       }
/*     */       
/* 115 */       connectionPointName = connectionPointName.split(":")[0];
/*     */       
/*     */ 
/* 118 */       VWSession session = PEUtil.getPESession(connectionPointName, repository.getDomain().getDomainConnection().getUrl(), request);
/* 119 */       List<BulkItemResult> results = dispositionEntity.initiateDisposition(session);
/* 120 */       return results;
/*     */     }
/* 122 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\InitiateDispositionService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */