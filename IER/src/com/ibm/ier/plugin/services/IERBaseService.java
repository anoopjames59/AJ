/*     */ package com.ibm.ier.plugin.services;
/*     */ 
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.filenet.api.util.UserContext;
/*     */ import com.ibm.ecm.configuration.ApplicationConfig;
/*     */ import com.ibm.ecm.configuration.Config;
/*     */ import com.ibm.ecm.configuration.DesktopConfig;
/*     */ import com.ibm.ecm.configuration.PluginConfig;
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ecm.extension.PluginServiceCallbacks;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.MessageInfo;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import com.ibm.jarm.api.util.RMUserContext;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.net.URLDecoder;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.zip.GZIPOutputStream;
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
/*     */ public class IERBaseService
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6364869716546394968L;
/*     */   protected BaseMediator mediator;
/*  86 */   private PluginServiceCallbacks pluginServiceCallback = null;
/*  87 */   private HttpServletRequest servletRequest = null;
/*  88 */   private String serviceId = null;
/*     */   public static final String METHOD_EXECUTE = "execute";
/*     */   public static final String METHOD_SERVICE_EXECUTE = "serviceExecute";
/*     */   public static final String METHOD_WRITE_JSON_MEDIATOR = "writeJSONMediator";
/*     */   public static final String DESKTOP = "desktop";
/*     */   
/*     */   public IERBaseService(BaseMediator mediator, PluginServiceCallbacks callback, String serviceId, HttpServletRequest request, HttpServletResponse response)
/*     */   {
/*  96 */     this.mediator = mediator;
/*  97 */     this.pluginServiceCallback = callback;
/*  98 */     this.servletRequest = request;
/*  99 */     setServiceId(serviceId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void preServiceExecute()
/*     */   {
/*     */     try
/*     */     {
/* 109 */       String repositoryId = getNexusRepositoryId();
/* 110 */       if (repositoryId == null) {
/* 111 */         DesktopConfig desktopConfig = Config.getDesktopConfig(this.servletRequest);
/* 112 */         repositoryId = desktopConfig.getDefaultRepositoryId();
/*     */       }
/* 114 */       Subject subject = this.pluginServiceCallback.getP8Subject(repositoryId);
/* 115 */       if (subject != null) {
/* 116 */         UserContext.get().pushSubject(subject);
/* 117 */         RMUserContext.get().setSubject(subject);
/*     */       } else {
/* 119 */         Subject activeSubject = Subject.getSubject(AccessController.getContext());
/* 120 */         if (activeSubject != null) {
/* 121 */           UserContext.get().pushSubject(activeSubject);
/* 122 */           RMUserContext.get().setSubject(activeSubject);
/*     */         } else {
/* 124 */           Logger.logDebug(this, "preServiceExecute", this.servletRequest, "Failed to obtain subject and set to user context: " + repositoryId);
/*     */         }
/*     */       }
/*     */     } catch (Exception exp) {
/* 128 */       Subject activeSubject = Subject.getSubject(AccessController.getContext());
/* 129 */       if (activeSubject != null) {
/* 130 */         UserContext.get().pushSubject(activeSubject);
/* 131 */         RMUserContext.get().setSubject(activeSubject);
/*     */       } else {
/* 133 */         Logger.logDebug(this, "preServiceExecute", this.servletRequest, "Failed to obtain subject and set to user context");
/*     */       }
/*     */     }
/*     */     
/* 137 */     if (this.servletRequest != null) {
/* 138 */       UserContext.get().setLocale(this.servletRequest.getLocale());
/* 139 */       RMUserContext.get().setLocale(this.servletRequest.getLocale());
/*     */     }
/*     */     
/*     */ 
/* 143 */     this.mediator.setBaseService(this);
/* 144 */     this.mediator.setHttpServletRequest(this.servletRequest);
/*     */     
/*     */ 
/* 147 */     if (Logger.isDebugLogged())
/*     */     {
/* 149 */       Enumeration parameters = this.servletRequest.getParameterNames();
/* 150 */       StringBuilder allparams = new StringBuilder();
/* 151 */       while (parameters.hasMoreElements()) {
/* 152 */         String parameterName = parameters.nextElement().toString();
/* 153 */         StringBuilder parameterString = new StringBuilder("Parameter Name: ");
/* 154 */         parameterString.append(parameterName);
/* 155 */         parameterString.append(" Parameter Value: ");
/* 156 */         parameterString.append(this.servletRequest.getParameter(parameterName));
/* 157 */         parameterString.append(",");
/* 158 */         allparams.append(parameterString.toString());
/*     */       }
/*     */       
/* 161 */       Logger.logDebug(this, "serviceExecute", this.servletRequest, allparams.toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void postServiceExecute() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finalServiceExecute()
/*     */   {
/* 178 */     UserContext uc = UserContext.get();
/* 179 */     if (uc != null) {
/* 180 */       Subject subject = uc.getSubject();
/* 181 */       if (subject != null) {
/* 182 */         uc.popSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public JSONObject getRequestContent()
/*     */     throws Exception
/*     */   {
/* 191 */     String strJsonFormData = this.servletRequest.getReader().readLine();
/*     */     
/* 193 */     JSONObject content = null;
/* 194 */     if (strJsonFormData != null) {
/* 195 */       content = JSONObject.parse(strJsonFormData);
/* 196 */       if (content != null) {
/* 197 */         return content;
/*     */       }
/*     */     }
/* 200 */     return null;
/*     */   }
/*     */   
/*     */   public void setServiceId(String serviceId) {
/* 204 */     this.serviceId = serviceId;
/*     */   }
/*     */   
/*     */   public String getServiceId() {
/* 208 */     return this.serviceId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginServiceCallbacks getPluginServiceCallbacks()
/*     */   {
/* 216 */     return this.pluginServiceCallback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpServletRequest getServletRequest()
/*     */   {
/* 224 */     return this.servletRequest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilePlanRepository getFilePlanRepository()
/*     */   {
/* 233 */     return (FilePlanRepository)getRepository(getP8RepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository getRepository()
/*     */   {
/* 241 */     String p8RepositoryId = getP8RepositoryId();
/*     */     
/* 243 */     if (Logger.isDebugLogged()) {
/* 244 */       Logger.logDebug(this, "getRepository", this.servletRequest, p8RepositoryId);
/*     */     }
/* 246 */     return getRepository(p8RepositoryId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilePlanRepository getFilePlanRepository(String p8RepositoryId)
/*     */   {
/* 256 */     if (Logger.isDebugLogged()) {
/* 257 */       Logger.logDebug(this, "getFilePlanRepository", this.servletRequest, p8RepositoryId);
/*     */     }
/* 259 */     return (FilePlanRepository)SessionUtil.getRepository(p8RepositoryId, getRMDomain(), this.servletRequest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository getRepository(String p8RepositoryId)
/*     */   {
/* 269 */     if (Logger.isDebugLogged()) {
/* 270 */       Logger.logDebug(this, "getRepository", this.servletRequest, p8RepositoryId);
/*     */     }
/* 272 */     RMDomain domain = getRMDomain();
/* 273 */     if (domain == null) {
/* 274 */       return null;
/*     */     }
/* 276 */     Repository repo = SessionUtil.getRepository(p8RepositoryId, domain, this.servletRequest);
/* 277 */     if (repo == null) {
/* 278 */       if (Logger.isDebugLogged()) {
/* 279 */         Logger.logDebug(this, "getRepository", this.servletRequest, "Repository not found: " + p8RepositoryId);
/*     */       }
/* 281 */       return null;
/*     */     }
/*     */     
/* 284 */     return repo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository getRepositoryFromNexusRepositoryId(String nexusRepositoryId)
/*     */   {
/* 294 */     String p8RepositoryId = getP8RepositoryId(nexusRepositoryId);
/*     */     
/* 296 */     if (Logger.isDebugLogged()) {
/* 297 */       Logger.logDebug(this, "getRepositoryFromNexusRepositoryId", this.servletRequest, "Nexus repository Id: " + nexusRepositoryId + " p8repositoryId" + p8RepositoryId);
/*     */     }
/*     */     
/* 300 */     if (p8RepositoryId != null) {
/* 301 */       return SessionUtil.getRepository(p8RepositoryId, getRMDomain(nexusRepositoryId), this.servletRequest);
/*     */     }
/* 303 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getP8RepositoryId()
/*     */   {
/* 313 */     return getP8RepositoryId(getNexusRepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getP8RepositoryId(String nexusRepositoryId)
/*     */   {
/* 320 */     if (Logger.isDebugLogged()) {
/* 321 */       Logger.logDebug(this, "getP8RepositoryId", this.servletRequest, "Nexus repository Id: " + nexusRepositoryId);
/*     */     }
/*     */     
/* 324 */     ObjectStore os = this.pluginServiceCallback.getP8ObjectStore(nexusRepositoryId);
/*     */     
/* 326 */     if (Logger.isDebugLogged()) {
/* 327 */       Logger.logDebug(this, "getP8RepositoryId", this.servletRequest, "OS: " + os);
/*     */     }
/*     */     
/* 330 */     if (os != null) {
/* 331 */       return os.get_Id().toString();
/*     */     }
/* 333 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getNexusRepositoryId()
/*     */   {
/* 343 */     return this.servletRequest.getParameter("repositoryId");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Connection getP8Connection()
/*     */   {
/* 351 */     return getP8Connection(getNexusRepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Connection getP8Connection(String nexusRepositoryId)
/*     */   {
/* 359 */     return this.pluginServiceCallback.getP8Connection(nexusRepositoryId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Subject getP8Subject()
/*     */   {
/* 367 */     return this.pluginServiceCallback.getP8Subject(getNexusRepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RMDomain getRMDomain()
/*     */   {
/* 374 */     return getRMDomain(getNexusRepositoryId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMDomain getRMDomain(String nexusRepositoryId)
/*     */   {
/* 382 */     RMDomain domain = (RMDomain)SessionUtil.getCacheProperty("rmDomain" + nexusRepositoryId, this.servletRequest);
/* 383 */     if (domain == null) {
/* 384 */       PropertyFilter pf = new PropertyFilter();
/* 385 */       pf.addIncludeProperty(new FilterElement(null, null, null, "Name", null));
/* 386 */       pf.addIncludeProperty(new FilterElement(null, null, null, "Id", null));
/* 387 */       pf.setMaxRecursion(1);
/* 388 */       domain = P8CE_Convert.fromP8CE(this.pluginServiceCallback.getP8Domain(nexusRepositoryId, pf));
/* 389 */       SessionUtil.setCacheProperty("rmDomain" + nexusRepositoryId, domain, this.servletRequest);
/*     */     }
/* 391 */     return domain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMediator(BaseMediator mediator)
/*     */   {
/* 399 */     this.mediator = mediator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseMediator getMediator()
/*     */   {
/* 407 */     return this.mediator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isIERDesktop()
/*     */   {
/* 415 */     DesktopConfig desktop = getDesktopConfig();
/* 416 */     if (desktop != null) {
/* 417 */       String layout = desktop.getLayout();
/* 418 */       String ierDesktopTag = desktop.getProperty("IERDesktop");
/* 419 */       if ((layout != null) && (layout.equals("ier.widget.layout.IERMainLayout"))) {
/* 420 */         return true;
/*     */       }
/* 422 */       if ((ierDesktopTag != null) && (ierDesktopTag.equals(Boolean.TRUE.toString()))) {
/* 423 */         return true;
/*     */       }
/*     */     }
/* 426 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DesktopConfig getDesktopConfig()
/*     */   {
/*     */     try
/*     */     {
/* 435 */       String appId = this.servletRequest.getParameter("application");
/* 436 */       String desktopId = this.servletRequest.getParameter("desktop");
/* 437 */       if (desktopId == null) {
/* 438 */         desktopId = this.servletRequest.getParameter("desktop");
/*     */       }
/* 440 */       ApplicationConfig appConfig = Config.getApplicationConfig(appId);
/* 441 */       return Config.getDesktopConfig(appConfig.getObjectId(), desktopId);
/*     */     }
/*     */     catch (Exception exp) {}
/*     */     
/* 445 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONObject getJSONPluginConfiguration()
/*     */     throws Exception
/*     */   {
/* 454 */     PluginConfig config = Config.getPluginConfig("navigator", "IERApplicationPlugin");
/* 455 */     String configuration = config.getConfiguration();
/* 456 */     if (configuration != null) {
/* 457 */       configuration = URLDecoder.decode(configuration, "UTF-8");
/*     */       
/* 459 */       JSONObject jsonConfiguration = JSONObject.parse(configuration);
/* 460 */       return jsonConfiguration;
/*     */     }
/*     */     
/* 463 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isP8ServiceRequest()
/*     */   {
/* 470 */     RepositoryConfig config = Config.getRepositoryConfig(this.servletRequest);
/* 471 */     if (config != null) {
/* 472 */       String type = config.getType();
/* 473 */       if (type != null) {
/* 474 */         return type.equals("p8");
/*     */       }
/*     */     }
/* 477 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAuthenticatingRepository()
/*     */   {
/* 485 */     DesktopConfig desktopConfig = Config.getDesktopConfig(this.servletRequest);
/* 486 */     return desktopConfig.getDefaultRepositoryId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleGeneralExceptions(Exception exp)
/*     */   {
/* 494 */     IERUIRuntimeException newExp = IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_APP_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/* 495 */     getMediator().addError(newExp.getMessageCode(), new Object[] { exp.getLocalizedMessage() });
/* 496 */     getMediator().setThrowable(newExp);
/* 497 */     Logger.logError(this, "execute", this.servletRequest, newExp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleJarmRuntimeExceptions(RMRuntimeException exp)
/*     */   {
/* 505 */     MessageInfo msgInfo = exp.getMessageInfo();
/* 506 */     getMediator().addError(exp.getErrorCode().getMessageId(), msgInfo.getFormattedMessage(), msgInfo.getExplanation(), msgInfo.getAction(), null);
/*     */     
/* 508 */     getMediator().setThrowable(exp);
/* 509 */     Logger.logError(this, "execute", this.servletRequest, exp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleIERUIRuntimeExceptions(IERUIRuntimeException exp)
/*     */   {
/* 518 */     Object cause = null;
/* 519 */     if ((exp != null) && (exp.getCause() != null)) {
/* 520 */       cause = exp.getCause();
/*     */     }
/* 522 */     if ((cause instanceof RMRuntimeException)) {
/* 523 */       getMediator().addError(exp.getMessageCode(), (RMRuntimeException)cause);
/*     */ 
/*     */     }
/* 526 */     else if (cause != null) {
/* 527 */       getMediator().addError(exp.getMessageCode(), new Object[] { cause });
/*     */     } else {
/* 529 */       getMediator().addError(exp.getMessageCode(), new Object[0]);
/*     */     }
/* 531 */     getMediator().setThrowable(exp);
/* 532 */     Logger.logError(this, "execute", this.servletRequest, exp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeJSONMediator(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 541 */     Logger.logEntry(this, "writeJSONMediator", request);
/*     */     
/*     */     try
/*     */     {
/* 545 */       response.addHeader("Cache-Control", "no-cache");
/*     */       
/* 547 */       if (this.mediator.getErrors().size() == 0) {
/* 548 */         JSONObject jsonResponse = this.mediator.toFinalJSONObject();
/* 549 */         response.setContentType("text/plain");
/* 550 */         response.setCharacterEncoding("UTF-8");
/* 551 */         Writer writer = response.getWriter();
/* 552 */         jsonResponse.serialize(writer);
/*     */       }
/*     */       else {
/* 555 */         String acceptedEncodings = request.getHeader("Accept-Encoding");
/* 556 */         if ((acceptedEncodings != null) && (acceptedEncodings.indexOf("gzip") >= 0)) {
/* 557 */           response.setBufferSize(65536);
/* 558 */           response.setHeader("Content-Encoding", "gzip");
/* 559 */           response.setContentType("text/plain");
/* 560 */           GZIPOutputStream gzos = new GZIPOutputStream(response.getOutputStream());
/* 561 */           Writer writer = new OutputStreamWriter(gzos, "UTF-8");
/* 562 */           this.mediator.writeJSON(writer);
/* 563 */           writer.close();
/*     */         } else {
/* 565 */           response.setContentType("text/plain");
/* 566 */           response.setCharacterEncoding("UTF-8");
/* 567 */           Writer writer = response.getWriter();
/* 568 */           this.mediator.writeJSON(writer);
/*     */         }
/*     */       }
/*     */     } catch (UnsupportedEncodingException e) {
/* 572 */       e.printStackTrace();
/*     */     } catch (Throwable e) {
/* 574 */       e.printStackTrace();
/* 575 */       this.mediator.addError(MessageCode.E_APP_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/* 576 */       Logger.logError(this, "writeJSONMediator", request, e);
/* 577 */       response.setContentType("text/plain");
/* 578 */       response.setCharacterEncoding("UTF-8");
/*     */       try {
/* 580 */         Writer writer = response.getWriter();
/* 581 */         this.mediator.writeJSON(writer);
/*     */       } catch (Exception e2) {
/* 583 */         Logger.logError(this, "writeJSONMediator", request, e2);
/*     */       }
/*     */     }
/*     */     
/* 587 */     Logger.logExit(this, "writeJSONMediator", request);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\IERBaseService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */