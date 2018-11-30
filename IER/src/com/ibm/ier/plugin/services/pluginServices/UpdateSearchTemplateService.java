/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.constants.ReservationType;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplateDocument;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplateDocumentBase;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_BaseEntityImpl;
/*     */ import com.ibm.json.java.JSON;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class UpdateSearchTemplateService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public void setP8QueryContinuationData(P8QueryContinuationData queryData) {}
/*     */   
/*     */   public String getId()
/*     */   {
/*  55 */     return "ierUpdateSearchTemplateService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  59 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  63 */     String methodName = "executeAction";
/*  64 */     Logger.logEntry(this, methodName, request);
/*  65 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*     */     
/*  67 */     P8Connection connection = createP8Connection();
/*  68 */     Connection ceConnection = connection.getCEConnection();
/*  69 */     synchronized (ceConnection)
/*     */     {
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/*  76 */         String serverName = request.getParameter("repositoryId");
/*  77 */         Logger.logDebug(this, methodName, request, "serverName = " + serverName);
/*     */         
/*  79 */         String docId = request.getParameter("docid");
/*  80 */         Logger.logDebug(this, methodName, request, "docId = " + docId);
/*     */         
/*     */ 
/*  83 */         String t = readJSON(request);
/*  84 */         JSONObject searchTemplateJson = (JSONObject)JSON.parse(t);
/*     */         
/*     */ 
/*  87 */         String name = (String)searchTemplateJson.get("name");
/*  88 */         Logger.logDebug(this, methodName, request, "name = " + name);
/*  89 */         String description = (String)searchTemplateJson.get("description");
/*  90 */         Logger.logDebug(this, methodName, request, "description = " + description);
/*  91 */         String folderPID = (String)searchTemplateJson.get("parentFolderId");
/*  92 */         Logger.logDebug(this, methodName, request, "parentFolderId = " + folderPID);
/*     */         
/*  94 */         P8SearchTemplateDocumentBase searchDocument = new P8SearchTemplateDocument(request, connection, docId, null, null);
/*     */         Document checkedOutDoc;
/*     */         try
/*     */         {
/*  98 */           checkedOutDoc = searchDocument.save(name, description, searchTemplateJson);
/*     */         } catch (IllegalAccessException e) {
/* 100 */           mediator.addError("checkin.error.noMajorVersionPermission", e.getLocalizedMessage(), null, null, null);
/*     */           
/*     */ 
/* 103 */           Logger.logExit(this, methodName, request);
/* 104 */           return;
/*     */         }
/* 106 */         if (checkedOutDoc == null) {
/* 107 */           mediator.addError("checkin.error.notCheckedOut", null, null, null, null);
/*     */           
/*     */ 
/* 110 */           Logger.logExit(this, methodName, request);
/* 111 */           return;
/*     */         }
/*     */         
/*     */ 
/* 115 */         checkedOutDoc.checkout(ReservationType.OBJECT_STORE_DEFAULT, null, null, null);
/* 116 */         checkedOutDoc.save(RefreshMode.REFRESH);
/*     */         
/*     */ 
/* 119 */         Collection<RMClassDescription> classDescriptions = getUniqueClassDescriptions("StoredSearch", this.baseService.getFilePlanRepository(), request);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 124 */         mediator.setServerName(serverName);
/* 125 */         mediator.setClassDescriptions(new ArrayList(classDescriptions));
/*     */         
/*     */ 
/* 128 */         IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(request);
/* 129 */         ArrayList<BaseEntity> hits = new ArrayList(1);
/*     */         
/* 131 */         BaseEntity entity = new P8CE_BaseEntityImpl(this.baseService.getRepository(), checkedOutDoc.get_Id().toString(), "StoredSearch", EntityType.Unknown, checkedOutDoc, false);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 136 */         hits.add(entity);
/* 137 */         searchResultsBean.setResults(hits);
/* 138 */         mediator.setSearchResultsBean(searchResultsBean);
/*     */         
/* 140 */         mediator.setRequestedProperties(IERUtil.convertStringsToListString("id"));
/*     */       } catch (Exception e) {
/* 142 */         logFDC(request, response, null, methodName);
/* 143 */         Logger.logError(this, methodName, request, e);
/* 144 */         mediator.addError("error.exception.general", e.getLocalizedMessage(), null, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */     Logger.logExit(this, methodName, request);
/*     */   }
/*     */   
/*     */   protected void logFDC(HttpServletRequest request, HttpServletResponse response, Object param, String methodName) {
/* 157 */     String serverName = request.getParameter("repositoryId");
/* 158 */     Logger.logInfo(this, methodName, request, "serverName = " + serverName);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\UpdateSearchTemplateService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */