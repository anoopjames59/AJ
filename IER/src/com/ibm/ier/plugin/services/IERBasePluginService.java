/*     */ package com.ibm.ier.plugin.services;
/*     */ 
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.ibm.ecm.extension.PluginService;
/*     */ import com.ibm.ecm.extension.PluginServiceCallbacks;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.Subject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IERBasePluginService
/*     */   extends PluginService
/*     */ {
/*     */   protected IERBaseService baseService;
/*     */   protected HttpServletRequest servletRequest;
/*  73 */   protected boolean skipResponseWriting = false;
/*     */   
/*     */   public static final String METHOD_EXECUTE = "execute";
/*     */   
/*     */   public static final String METHOD_SERVICE_EXECUTE = "serviceExecute";
/*     */   
/*     */   public static final String METHOD_WRITE_JSON_MEDIATOR = "writeJSONMediator";
/*     */   
/*     */   public static final String ATTRIB_BASE_SERVICE = "ierBaseService";
/*     */   
/*     */ 
/*     */   public abstract void serviceExecute(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*     */     throws Exception;
/*     */   
/*     */   public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  90 */     Logger.logEntry(this, "execute", request);
/*     */     
/*     */     try
/*     */     {
/*  94 */       this.servletRequest = request;
/*     */       
/*     */ 
/*  97 */       createBaseService(createMediator(), callbacks, request, response);
/*     */       
/*     */ 
/* 100 */       getBaseService().preServiceExecute();
/*     */       
/*     */ 
/* 103 */       serviceExecute(request, response);
/*     */       
/*     */ 
/* 106 */       getBaseService().postServiceExecute();
/*     */     }
/*     */     catch (IERUIRuntimeException exp)
/*     */     {
/* 110 */       getBaseService().handleIERUIRuntimeExceptions(exp);
/*     */     }
/*     */     catch (RMRuntimeException exp) {
/* 113 */       getBaseService().handleJarmRuntimeExceptions(exp);
/*     */     }
/*     */     catch (Exception exp)
/*     */     {
/* 117 */       getBaseService().handleGeneralExceptions(exp);
/*     */     }
/*     */     finally {
/* 120 */       if (!this.skipResponseWriting)
/*     */       {
/* 122 */         getBaseService().writeJSONMediator(request, response);
/*     */       }
/*     */       
/*     */ 
/* 126 */       getBaseService().finalServiceExecute();
/*     */     }
/*     */     
/* 129 */     Logger.logExit(this, "execute", request);
/*     */   }
/*     */   
/*     */   private void createBaseService(BaseMediator mediator, PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response) {
/* 133 */     if (this.baseService == null) {
/* 134 */       this.baseService = new IERBaseService(mediator, callbacks, getId(), request, response);
/* 135 */       request.setAttribute("ierBaseService", this.baseService);
/*     */     }
/*     */   }
/*     */   
/*     */   protected IERBaseService getBaseService() {
/* 140 */     if (this.baseService != null)
/* 141 */       return this.baseService;
/* 142 */     if ((this.servletRequest != null) && (this.servletRequest.getAttribute("ierBaseService") != null)) {
/* 143 */       return (IERBaseService)this.servletRequest.getAttribute("ierBaseService");
/*     */     }
/* 145 */     return null;
/*     */   }
/*     */   
/*     */   public void setSkipResponseWriting(boolean skip) {
/* 149 */     this.skipResponseWriting = skip;
/*     */   }
/*     */   
/*     */ 
/*     */   public JSONObject getRequestContent()
/*     */     throws Exception
/*     */   {
/* 156 */     return getBaseService().getRequestContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JSONObject getJSONPluginConfiguration()
/*     */     throws Exception
/*     */   {
/* 164 */     return getBaseService().getJSONPluginConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FilePlanRepository getFilePlanRepository()
/*     */   {
/* 173 */     return getBaseService().getFilePlanRepository();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository getRepository()
/*     */   {
/* 182 */     return getRepository(getP8RepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Repository getRepository(String p8RepositoryId)
/*     */   {
/* 192 */     return getBaseService().getRepository(p8RepositoryId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getP8RepositoryId()
/*     */   {
/* 202 */     return getBaseService().getP8RepositoryId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getNexusRepositoryId()
/*     */   {
/* 212 */     return getBaseService().getNexusRepositoryId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setCompletedJSONResponseObject(JSONObject jsonResponse)
/*     */   {
/* 220 */     getMediator().setCompletedJSONResponseObject(jsonResponse);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMDomain getRMDomain()
/*     */   {
/* 228 */     return getBaseService().getRMDomain();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BaseMediator createMediator()
/*     */   {
/* 237 */     return new BaseMediator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setMediator(BaseMediator mediator)
/*     */   {
/* 245 */     getBaseService().setMediator(mediator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseMediator getMediator()
/*     */   {
/* 253 */     return getBaseService().getMediator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Collection<RMClassDescription> findUniqueClassDescriptions(List<BaseEntity> objects, FilePlanRepository fp_repository, HttpServletRequest request, boolean sameClass)
/*     */   {
/* 265 */     Map<String, RMClassDescription> mapOfCDs = new HashMap();
/* 266 */     String repositoryId = fp_repository.getObjectIdentity();
/* 267 */     for (BaseEntity obj : objects)
/*     */     {
/* 269 */       String id = repositoryId + "/" + obj.getClassName();
/* 270 */       if (!mapOfCDs.containsKey(id)) {
/* 271 */         RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, obj.getClassName(), request);
/* 272 */         mapOfCDs.put(id, cd);
/*     */         
/* 274 */         if (sameClass)
/* 275 */           return mapOfCDs.values();
/*     */       }
/*     */     }
/* 278 */     return mapOfCDs.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Collection<RMClassDescription> findUniqueClassDescriptions(List<BaseEntity> objects, FilePlanRepository fp_repository, HttpServletRequest request)
/*     */   {
/* 289 */     return findUniqueClassDescriptions(objects, fp_repository, request, false);
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
/*     */   public boolean getBooleanRequestParameter(HttpServletRequest request, String name, boolean defaultValue)
/*     */   {
/* 306 */     boolean value = defaultValue;
/*     */     
/* 308 */     String valueString = request.getParameter(name);
/*     */     
/* 310 */     if ((valueString != null) && (valueString.length() > 0))
/*     */     {
/* 312 */       value = valueString.equals("1") ? true : Boolean.valueOf(valueString).booleanValue();
/*     */     }
/*     */     
/* 315 */     return value;
/*     */   }
/*     */   
/*     */   public P8Connection createP8Connection()
/*     */   {
/* 320 */     Connection conn = getBaseService().getP8Connection();
/* 321 */     Subject sub = getBaseService().getP8Subject();
/* 322 */     Domain domain = P8CE_Convert.fromJARM(getBaseService().getRMDomain());
/* 323 */     String objectStoreName = this.baseService.getFilePlanRepository() != null ? this.baseService.getFilePlanRepository().getDisplayName() : null;
/* 324 */     return new P8Connection(null, null, conn, sub, domain, objectStoreName, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String readJSON(HttpServletRequest request)
/*     */     throws IOException
/*     */   {
/* 334 */     String json = request.getParameter("json_post");
/* 335 */     if (json == null) {
/* 336 */       json = request.getReader().readLine();
/*     */     }
/* 338 */     return json;
/*     */   }
/*     */   
/*     */   public Collection<RMClassDescription> getUniqueClassDescriptions(String className, FilePlanRepository fp_repository, HttpServletRequest request) {
/* 342 */     RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, className, request);
/* 343 */     Map<String, RMClassDescription> mapOfCDs = new HashMap();
/* 344 */     String repositoryId = fp_repository.getObjectIdentity();
/* 345 */     String id = repositoryId + "/" + className;
/* 346 */     mapOfCDs.put(id, cd);
/* 347 */     return mapOfCDs.values();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\IERBasePluginService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */