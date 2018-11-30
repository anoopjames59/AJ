/*     */ package com.ibm.ier.plugin.services;
/*     */ 
/*     */ import com.ibm.ecm.extension.PluginRequestFilter;
/*     */ import com.ibm.ecm.extension.PluginServiceCallbacks;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.json.java.JSONArtifact;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ public abstract class IERBaseRequestFilterService
/*     */   extends PluginRequestFilter
/*     */ {
/*     */   protected IERBaseService baseService;
/*     */   protected HttpServletRequest servletRequest;
/*     */   public static final String METHOD_EXECUTE = "execute";
/*     */   public static final String METHOD_SERVICE_EXECUTE = "serviceExecute";
/*     */   public static final String METHOD_WRITE_JSON_MEDIATOR = "writeJSONMediator";
/*     */   public static final String ATTRIB_BASE_SERVICE = "ierBaseService";
/*     */   
/*     */   public abstract JSONObject filterServiceExecute(HttpServletRequest paramHttpServletRequest, JSONArtifact paramJSONArtifact)
/*     */     throws Exception;
/*     */   
/*     */   public JSONObject filter(PluginServiceCallbacks callbacks, HttpServletRequest request, JSONArtifact jsonRequest)
/*     */     throws Exception
/*     */   {
/*  80 */     Logger.logEntry(this, "execute", request);
/*     */     
/*     */     try
/*     */     {
/*  84 */       this.servletRequest = request;
/*     */       
/*     */ 
/*  87 */       createBaseService(createMediator(), callbacks, request);
/*     */       
/*     */ 
/*  90 */       getBaseService().preServiceExecute();
/*     */       
/*     */ 
/*  93 */       JSONObject jsonResponse = filterServiceExecute(request, jsonRequest);
/*     */       
/*     */ 
/*  96 */       if (jsonResponse == null) {
/*  97 */         return null;
/*     */       }
/*     */       
/* 100 */       getMediator().setCompletedJSONResponseObject(jsonResponse);
/*     */       
/*     */ 
/* 103 */       getBaseService().postServiceExecute();
/*     */ 
/*     */     }
/*     */     catch (IERUIRuntimeException exp)
/*     */     {
/* 108 */       getBaseService().handleIERUIRuntimeExceptions(exp);
/*     */     }
/*     */     catch (RMRuntimeException exp) {
/* 111 */       getBaseService().handleJarmRuntimeExceptions(exp);
/*     */     }
/*     */     catch (Exception exp)
/*     */     {
/* 115 */       getBaseService().handleGeneralExceptions(exp);
/*     */     }
/*     */     finally
/*     */     {
/* 119 */       getBaseService().finalServiceExecute();
/*     */     }
/*     */     
/* 122 */     Logger.logExit(this, "execute", request);
/* 123 */     return getMediator().toFinalJSONObject();
/*     */   }
/*     */   
/*     */   private void createBaseService(BaseMediator mediator, PluginServiceCallbacks callbacks, HttpServletRequest request) {
/* 127 */     if (this.baseService == null) {
/* 128 */       this.baseService = new IERBaseService(mediator, callbacks, IERUtil.arrayToString(getFilteredServices()), request, null);
/* 129 */       request.setAttribute("ierBaseService", this.baseService);
/*     */     }
/*     */   }
/*     */   
/*     */   protected IERBaseService getBaseService() {
/* 134 */     if (this.baseService != null)
/* 135 */       return this.baseService;
/* 136 */     if ((this.servletRequest != null) && (this.servletRequest.getAttribute("ierBaseService") != null)) {
/* 137 */       return (IERBaseService)this.servletRequest.getAttribute("ierBaseService");
/*     */     }
/* 139 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public JSONObject getRequestContent()
/*     */     throws Exception
/*     */   {
/* 146 */     return getBaseService().getRequestContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JSONObject getJSONPluginConfiguration()
/*     */     throws Exception
/*     */   {
/* 154 */     return getBaseService().getJSONPluginConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FilePlanRepository getFilePlanRepository()
/*     */   {
/* 162 */     return getBaseService().getFilePlanRepository();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository getRepository()
/*     */   {
/* 170 */     return getRepository(getP8RepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Repository getRepository(String p8RepositoryId)
/*     */   {
/* 180 */     return getBaseService().getRepository(p8RepositoryId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getP8RepositoryId()
/*     */   {
/* 190 */     return getBaseService().getP8RepositoryId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RMDomain getRMDomain()
/*     */   {
/* 198 */     return getBaseService().getRMDomain();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BaseMediator createMediator()
/*     */   {
/* 207 */     return new BaseMediator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setMediator(BaseMediator mediator)
/*     */   {
/* 215 */     getBaseService().setMediator(mediator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BaseMediator getMediator()
/*     */   {
/* 223 */     return getBaseService().getMediator();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\IERBaseRequestFilterService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */