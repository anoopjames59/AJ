/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.collection.ContentElementList;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.ContentElement;
/*     */ import com.filenet.api.core.ContentTransfer;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplate.IERMacros;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplateDocument;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.json.java.JSON;
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
/*     */ public class OpenSearchTemplateService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   private final JSONObject jsonResponse;
/*     */   
/*     */   public void setP8QueryContinuationData(P8QueryContinuationData queryData) {}
/*     */   
/*     */   public String getId()
/*     */   {
/*  49 */     return "ierOpenSearchTemplateService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  53 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public OpenSearchTemplateService(JSONObject jsonResponse) {
/*  57 */     this.jsonResponse = jsonResponse;
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  61 */     String methodName = "executeAction";
/*  62 */     Logger.logEntry(this, methodName, request);
/*     */     
/*  64 */     P8Connection connection = createP8Connection();
/*  65 */     Connection ceConnection = connection.getCEConnection();
/*     */     
/*  67 */     String templateId = request.getParameter("template_name");
/*  68 */     Logger.logDebug(this, methodName, request, "className = " + templateId);
/*  69 */     String vsId = request.getParameter("vsId");
/*  70 */     Logger.logDebug(this, methodName, request, "vsId = " + vsId);
/*     */     
/*  72 */     Object synchObject = ceConnection == null ? new Object() : ceConnection;
/*  73 */     synchronized (synchObject) {
/*  74 */       P8SearchTemplateDocument searchDocument = new P8SearchTemplateDocument(request, connection, templateId, vsId, null);
/*  75 */       searchDocument.loadSearchDefinitionDocument(true, false);
/*     */       
/*  77 */       String textSearchOption = "content";
/*  78 */       Document document = searchDocument.getSourceDocument();
/*  79 */       if (document != null) {
/*  80 */         ContentElement contentElement = null;
/*  81 */         ContentElementList contentElements = document.get_ContentElements();
/*  82 */         if (contentElements.size() > 1) {
/*  83 */           for (int i = 1; i < contentElements.size(); i++) {
/*  84 */             contentElement = (ContentElement)contentElements.get(i);
/*  85 */             if ("application/json".equals(contentElement.get_ContentType())) {
/*  86 */               JSONObject json = (JSONObject)JSON.parse(((ContentTransfer)contentElement).accessContentStream());
/*  87 */               if (json == null) break;
/*  88 */               JSONObject macrosJson = (JSONObject)json.get("macros");
/*  89 */               if (macrosJson != null) {
/*  90 */                 P8SearchTemplate.IERMacros macros = new P8SearchTemplate.IERMacros();
/*  91 */                 macros.fromJSON(macrosJson);
/*  92 */                 textSearchOption = macros.getTextSearchOption();
/*     */               }
/*  94 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 102 */       this.jsonResponse.put("textSearchOption", textSearchOption);
/*     */     }
/*     */     
/* 105 */     Logger.logExit(this, methodName, request);
/*     */   }
/*     */   
/*     */   protected void logFDC(HttpServletRequest request, HttpServletResponse response, Object param, String methodName) {
/* 109 */     String serverName = request.getParameter("repositoryId");
/* 110 */     Logger.logInfo(this, methodName, request, "serverName = " + serverName);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\OpenSearchTemplateService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */