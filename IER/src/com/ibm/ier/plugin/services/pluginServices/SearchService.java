/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.ibm.ecm.configuration.Config;
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplate;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplateDocument;
/*     */ import com.ibm.ier.plugin.search.p8.P8Util;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchClass;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ResultsDisplay;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERSearchRunner;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.query.CBRResult;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ public class SearchService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  42 */     return "ierSearch";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator()
/*     */   {
/*  47 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  53 */     Logger.logEntry(this, "serviceExecute", request);
/*  54 */     String methodName = "serviceExecute";
/*  55 */     boolean ifCE52OrAbove = false;
/*  56 */     int countLimit = -1;
/*     */     
/*  58 */     FilePlanRepository repository = getFilePlanRepository();
/*  59 */     String templateID = request.getParameter("templateID");
/*     */     
/*  61 */     String vsId = request.getParameter("vsId");
/*     */     
/*     */ 
/*  64 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*     */     
/*     */     try
/*     */     {
/*  68 */       String orderBy = request.getParameter("order_by");
/*  69 */       boolean orderDescending = getBooleanRequestParameter(request, "order_descending", false);
/*     */       
/*  71 */       RepositoryConfig repositoryConfig = Config.getRepositoryConfig(request);
/*  72 */       if (repositoryConfig != null) {
/*  73 */         Logger.logDebug(this, "serviceExecute", request, "showDocLocation = " + repositoryConfig.isShowDocLocation());
/*     */       }
/*     */       
/*     */ 
/*  77 */       P8Connection conn = createP8Connection();
/*     */       
/*  79 */       JSONObject searchTemplateJson = getRequestContent();
/*  80 */       P8SearchTemplate searchTemplate = new P8SearchTemplate();
/*     */       
/*     */ 
/*  83 */       P8SearchTemplateDocument searchDocument = new P8SearchTemplateDocument(request, conn, templateID, vsId, null);
/*  84 */       searchTemplate = (P8SearchTemplate)searchDocument.getSearchTemplate(searchTemplateJson, (templateID != null) && (!templateID.isEmpty()));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  89 */       if ((searchTemplate.getResultsDisplay() != null) && ((orderBy == null) || (orderBy.length() == 0))) {
/*  90 */         orderBy = searchTemplate.getResultsDisplay().getSortByProperty();
/*  91 */         orderDescending = !searchTemplate.getResultsDisplay().getSortAscending();
/*     */       }
/*     */       
/*  94 */       ObjectStore os = conn.getObjectStore();
/*     */       
/*  96 */       ifCE52OrAbove = P8Util.ifCE52OrAbove(os);
/*  97 */       if (ifCE52OrAbove) {
/*  98 */         countLimit = Integer.MAX_VALUE;
/*     */       }
/*     */       
/* 101 */       IERSearchRunner searchRunner = new IERSearchRunner(request, repository, searchTemplate, templateID, conn);
/* 102 */       List<?> hits = searchRunner.runSearch(orderBy, orderDescending, repositoryConfig.getMaxResults(), repositoryConfig.getTimeoutInSeconds(), countLimit);
/*     */       
/*     */ 
/* 105 */       repository = (FilePlanRepository)searchRunner.getSearchScopeRepository();
/*     */       
/* 107 */       mediator.setFilePlanRepository(repository);
/*     */       
/*     */ 
/* 110 */       int pageSize = searchRunner.getPageSize();
/* 111 */       mediator.setPageSize(pageSize);
/*     */       
/*     */ 
/* 114 */       Integer totalCount = searchRunner.getTotalCount();
/* 115 */       int totalCountInt = hits != null ? hits.size() : 0;
/* 116 */       String totalCountType = "none";
/*     */       
/* 118 */       if (ifCE52OrAbove)
/*     */       {
/* 120 */         if (totalCountInt == pageSize)
/*     */         {
/* 122 */           if (totalCount != null)
/*     */           {
/* 124 */             if (totalCount.intValue() > pageSize) {
/* 125 */               totalCountInt = totalCount.intValue();
/* 126 */               totalCountType = "total";
/* 127 */             } else if (totalCount.intValue() < 0)
/*     */             {
/*     */ 
/* 130 */               int maxReusltsinAdmin = repositoryConfig.getMaxResults();
/* 131 */               int serverMaxSettings = -totalCount.intValue();
/* 132 */               totalCountInt = maxReusltsinAdmin < serverMaxSettings ? serverMaxSettings : maxReusltsinAdmin;
/* 133 */               totalCountType = "threshold";
/*     */             }
/*     */           }
/*     */           else {
/* 137 */             totalCountType = "threshold";
/*     */           }
/* 139 */         } else if (totalCountInt < pageSize) {
/* 140 */           totalCountType = "total";
/*     */         }
/*     */       }
/*     */       
/* 144 */       Logger.logDebug(this, "serviceExecute", request, "Total Count Type = " + totalCountType);
/* 145 */       Logger.logDebug(this, "serviceExecute", request, "Total Count = " + totalCountInt);
/* 146 */       mediator.setTotalCount(totalCountInt);
/* 147 */       mediator.setTotalCountType(totalCountType);
/*     */       
/* 149 */       List<String> requestedProperties = new ArrayList();
/* 150 */       if (searchTemplate.getResultsDisplay() != null) {
/* 151 */         String[] cols = searchTemplate.getResultsDisplay().getColumns();
/* 152 */         List<String> colList = Arrays.asList(cols);
/* 153 */         mediator.setDisplayColumns(colList);
/* 154 */         mediator.setIncludeSubClasses(searchTemplate.isFirstClassSearchSubclasses());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 159 */         if (orderBy != null) {
/* 160 */           mediator.setSortByProperty(orderBy);
/* 161 */           mediator.setSortDirection(orderDescending ? -1 : 1);
/*     */         }
/*     */         
/* 164 */         searchRunner.setDisplayCols(cols);
/* 165 */         requestedProperties.addAll(colList);
/*     */       }
/*     */       
/* 168 */       requestedProperties.add("OnHold");
/* 169 */       mediator.setRequestedProperties(requestedProperties);
/* 170 */       mediator.setContinuationData(searchRunner.getQueryContinuationData());
/*     */       
/* 172 */       if ((hits != null) && (!hits.isEmpty())) {
/* 173 */         Object row = hits.get(0);
/* 174 */         if ((row instanceof BaseEntity)) {
/* 175 */           mediator.setSearchResultsBean(new IERSearchResultsBean(hits));
/* 176 */           mediator.setClassDescriptions(getClassDescriptions(request, repository, hits));
/*     */         }
/* 178 */         else if ((row instanceof CBRResult))
/*     */         {
/* 180 */           mediator.setCBRResultRows(hits);
/* 181 */           mediator.setClassDescriptions(getClassDescriptions(request, repository, searchTemplate));
/*     */         }
/*     */       }
/*     */       
/* 185 */       int hitSize = hits != null ? hits.size() : 0;
/* 186 */       mediator.addMessage(hitSize == 1 ? MessageCode.SEARCH_RESULTS_RETURNEDONE : MessageCode.SEARCH_RESULTS_RETURNED, new Object[] { Integer.valueOf(hitSize) });
/*     */       
/*     */ 
/* 189 */       Logger.logDebug(this, "serviceExecute", request, " after adding search results.");
/*     */     } catch (EngineRuntimeException ere) {
/* 191 */       logFDC(request, response, null, "serviceExecute");
/* 192 */       Logger.logError(this, "serviceExecute", request, ere);
/* 193 */       ExceptionCode exceptionCode = ere.getExceptionCode();
/* 194 */       if (exceptionCode == ExceptionCode.RETRIEVE_PROPERTY_NOT_DEFINED) {
/* 195 */         mediator.addError("search.exception.propertyNotFound", ere.getLocalizedMessage(), null, null, null);
/* 196 */       } else if (exceptionCode == ExceptionCode.E_OBJECT_NOT_FOUND) {
/* 197 */         mediator.addError("error.exception.opensearchnotfound", templateID, null, null, null);
/*     */       } else {
/* 199 */         mediator.addError("error.exception.general", ere.getLocalizedMessage(), null, null, null);
/*     */       }
/*     */     } catch (RuntimeException uoe) {
/* 202 */       logFDC(request, response, null, "serviceExecute");
/* 203 */       String errorMsg = uoe.getMessage();
/* 204 */       mediator.addError("error.exception.general", errorMsg, null, null, null);
/* 205 */       Logger.logError(this, "serviceExecute", request, uoe);
/*     */     } catch (Exception e) {
/* 207 */       logFDC(request, response, null, "serviceExecute");
/* 208 */       Logger.logError(this, "serviceExecute", request, e);
/* 209 */       mediator.addError("error.exception.general", e.toString(), null, null, null);
/*     */     }
/*     */     finally {
/* 212 */       setCompletedJSONResponseObject(mediator.toJSONObject());
/*     */     }
/*     */     
/* 215 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void logFDC(HttpServletRequest request, HttpServletResponse response, Object param, String methodName)
/*     */   {
/* 221 */     String templateID = request.getParameter("templateID");
/* 222 */     Logger.logInfo(this, methodName, request, "templateID = " + templateID);
/* 223 */     String vsid = request.getParameter("vsId");
/* 224 */     Logger.logInfo(this, methodName, request, "vsid = " + vsid);
/* 225 */     String criteria = request.getParameter("criterias");
/* 226 */     Logger.logInfo(this, methodName, request, "criteria = " + criteria);
/* 227 */     String repositoryId = request.getParameter("repositoryId");
/* 228 */     Logger.logInfo(this, methodName, request, "repositoryId = " + repositoryId);
/*     */   }
/*     */   
/*     */   private List<RMClassDescription> getClassDescriptions(HttpServletRequest request, FilePlanRepository repository, List<BaseEntity> entities) {
/* 232 */     Map<String, RMClassDescription> map = new HashMap();
/* 233 */     for (BaseEntity entity : entities) {
/* 234 */       String className = entity.getClassName();
/* 235 */       if (map.get(className) == null) {
/* 236 */         RMClassDescription desc = SessionUtil.getClassDescription(repository, className, request);
/* 237 */         map.put(className, desc);
/*     */       }
/*     */     }
/* 240 */     return new ArrayList(map.values());
/*     */   }
/*     */   
/*     */   private List<RMClassDescription> getClassDescriptions(HttpServletRequest request, FilePlanRepository repository, P8SearchTemplate template)
/*     */   {
/* 245 */     String className = template.getObjectType().name();
/* 246 */     List<SearchTemplate.SearchClass> classes = template.getClasses();
/* 247 */     if ((classes != null) && (!classes.isEmpty())) {
/* 248 */       className = ((SearchTemplate.SearchClass)classes.get(0)).getName();
/*     */     }
/* 250 */     Map<String, RMClassDescription> map = new HashMap();
/* 251 */     RMClassDescription desc = SessionUtil.getClassDescription(repository, className, request);
/* 252 */     map.put(className, desc);
/* 253 */     return new ArrayList(map.values());
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\SearchService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */