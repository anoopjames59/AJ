/*     */ package com.ibm.ier.plugin.services;
/*     */ 
/*     */ import com.ibm.ecm.extension.PluginResponseFilter;
/*     */ import com.ibm.ecm.extension.PluginServiceCallbacks;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
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
/*     */ public abstract class IERBaseResponseFilterService
/*     */   extends PluginResponseFilter
/*     */ {
/*     */   protected IERBaseService baseService;
/*     */   protected HttpServletRequest servletRequest;
/*     */   public static final String METHOD_EXECUTE = "execute";
/*     */   public static final String METHOD_SERVICE_EXECUTE = "serviceExecute";
/*     */   public static final String METHOD_WRITE_JSON_MEDIATOR = "writeJSONMediator";
/*     */   public static final String ATTRIB_BASE_SERVICE = "ierBaseService";
/*     */   
/*     */   public abstract void filterServiceExecute(HttpServletRequest paramHttpServletRequest, JSONObject paramJSONObject)
/*     */     throws Exception;
/*     */   
/*     */   public void filter(String serverType, PluginServiceCallbacks callbacks, HttpServletRequest request, JSONObject jsonResponse)
/*     */     throws Exception
/*     */   {
/*  75 */     Logger.logEntry(this, "execute", request);
/*     */     
/*     */     try
/*     */     {
/*  79 */       this.servletRequest = request;
/*     */       
/*     */ 
/*  82 */       createBaseService(createMediator(), callbacks, request);
/*     */       
/*     */ 
/*  85 */       getBaseService().preServiceExecute();
/*     */       
/*     */ 
/*  88 */       filterServiceExecute(request, jsonResponse);
/*     */       
/*     */ 
/*  91 */       getMediator().setCompletedJSONResponseObject(jsonResponse);
/*     */       
/*     */ 
/*  94 */       getBaseService().postServiceExecute();
/*     */     }
/*     */     catch (IERUIRuntimeException exp)
/*     */     {
/*  98 */       getBaseService().handleIERUIRuntimeExceptions(exp);
/*     */     }
/*     */     catch (RMRuntimeException exp) {
/* 101 */       getBaseService().handleJarmRuntimeExceptions(exp);
/*     */     }
/*     */     catch (Exception exp)
/*     */     {
/* 105 */       getBaseService().handleGeneralExceptions(exp);
/*     */     }
/*     */     finally
/*     */     {
/* 109 */       jsonResponse = getMediator().toJSONObject();
/*     */       
/*     */ 
/* 112 */       getBaseService().finalServiceExecute();
/*     */     }
/*     */     
/* 115 */     Logger.logExit(this, "execute", request);
/*     */   }
/*     */   
/*     */   private void createBaseService(BaseMediator mediator, PluginServiceCallbacks callbacks, HttpServletRequest request) {
/* 119 */     if (this.baseService == null) {
/* 120 */       this.baseService = new IERBaseService(mediator, callbacks, IERUtil.arrayToString(getFilteredServices()), request, null);
/* 121 */       request.setAttribute("ierBaseService", this.baseService);
/*     */     }
/*     */   }
/*     */   
/*     */   protected IERBaseService getBaseService() {
/* 126 */     if (this.baseService != null)
/* 127 */       return this.baseService;
/* 128 */     if ((this.servletRequest != null) && (this.servletRequest.getAttribute("ierBaseService") != null)) {
/* 129 */       return (IERBaseService)this.servletRequest.getAttribute("ierBaseService");
/*     */     }
/* 131 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public JSONObject getRequestContent()
/*     */     throws Exception
/*     */   {
/* 138 */     return getBaseService().getRequestContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JSONObject getJSONPluginConfiguration()
/*     */     throws Exception
/*     */   {
/* 146 */     return getBaseService().getJSONPluginConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FilePlanRepository getFilePlanRepository()
/*     */   {
/* 154 */     return getBaseService().getFilePlanRepository();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository getRepository()
/*     */   {
/* 162 */     return getRepository(getP8RepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Repository getRepository(String p8RepositoryId)
/*     */   {
/* 172 */     return getBaseService().getRepository(p8RepositoryId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getP8RepositoryId()
/*     */   {
/* 182 */     return getBaseService().getP8RepositoryId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RMDomain getRMDomain()
/*     */   {
/* 190 */     return getBaseService().getRMDomain();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BaseMediator createMediator()
/*     */   {
/* 199 */     return new BaseMediator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setMediator(BaseMediator mediator)
/*     */   {
/* 207 */     getBaseService().setMediator(mediator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BaseMediator getMediator()
/*     */   {
/* 215 */     return getBaseService().getMediator();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\IERBaseResponseFilterService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */