/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.CBRForwardPager;
/*     */ import com.ibm.ier.plugin.util.ForwardPager;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.jarm.api.collection.CBRPageIterator;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.query.CBRResult;
/*     */ import com.ibm.jarm.api.query.RMSearch;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ public class RetrieveNextPageService
/*     */   extends OpenContainerService
/*     */ {
/*  50 */   private P8QueryContinuationData queryData = null;
/*     */   
/*     */   public void setP8QueryContinuationData(P8QueryContinuationData queryData) {
/*  53 */     this.queryData = queryData;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  57 */     return "ierRetrieveNextPageService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  61 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*     */   {
/*  66 */     String methodName = "executeAction";
/*  67 */     Logger.logEntry(this, methodName, request);
/*  68 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*     */     try
/*     */     {
/*  71 */       HttpSession session = request.getSession();
/*     */       
/*  73 */       String itemsNeededParameter = request.getParameter("itemsNeeded");
/*  74 */       String serverName = request.getParameter("repositoryId");
/*     */       
/*  76 */       int pageSize = IERUtil.getSearchPagingSize(request, this.queryData.rmCBRDef);
/*  77 */       mediator.setPageSize(pageSize);
/*  78 */       mediator.setServerName(serverName);
/*  79 */       mediator.setContinuation(true);
/*     */       
/*  81 */       FilePlanRepository repository = this.baseService.getFilePlanRepository();
/*  82 */       if ((this.queryData.objectStoreId != null) && (!this.queryData.objectStoreId.isEmpty())) {
/*  83 */         repository = this.baseService.getFilePlanRepository(this.queryData.objectStoreId);
/*  84 */         if (repository == null) {
/*  85 */           repository = this.baseService.getFilePlanRepository();
/*     */         }
/*     */       }
/*  88 */       boolean isPageEnd = false;
/*  89 */       int hitSize = 0;
/*  90 */       if (this.queryData.rmCBRDef == null) {
/*  91 */         ForwardPager<BaseEntity> pager = (ForwardPager)session.getAttribute(this.queryData.sessionKey);
/*     */         
/*  93 */         if (pager == null) {
/*  94 */           pager = executeQuery(this.queryData, null, repository);
/*  95 */           pager.loadItems(this.queryData.itemsToSkip);
/*     */         }
/*  97 */         List<BaseEntity> hits = pager.loadNextPage();
/*     */         
/*  99 */         if (StringUtils.isNotEmpty(itemsNeededParameter)) {
/* 100 */           int itemsNeeded = Integer.parseInt(itemsNeededParameter);
/* 101 */           while ((itemsNeeded > pager.getNumberOfItemsRetrieved()) && (!pager.isEndReached())) {
/* 102 */             List<BaseEntity> nextPage = pager.loadNextPage();
/* 103 */             hits.addAll(nextPage);
/*     */           }
/*     */         }
/*     */         
/* 107 */         isPageEnd = (pager == null) || (pager.isEndReached());
/* 108 */         this.queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/* 109 */         mediator.setMaxResultsReached(pager.isMaxResultsReached());
/*     */         
/* 111 */         Collection<RMClassDescription> classDescriptions = findUniqueClassDescriptions(hits, repository, request);
/* 112 */         mediator.setClassDescriptions(new ArrayList(classDescriptions));
/* 113 */         IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(hits, true);
/* 114 */         mediator.setSearchResultsBean(searchResultsBean);
/* 115 */         hitSize = hits != null ? hits.size() : 0;
/*     */       }
/*     */       else {
/* 118 */         CBRForwardPager<CBRResult> pager = (CBRForwardPager)session.getAttribute(this.queryData.sessionKey);
/*     */         
/* 120 */         if (pager == null) {
/* 121 */           RMSearch uut = new RMSearch(repository);
/* 122 */           PageableSet<CBRResult> ps = uut.contentBasedRetrieval(this.queryData.rmCBRDef, Integer.valueOf(pageSize), Boolean.valueOf(true));
/*     */           
/*     */ 
/* 125 */           CBRPageIterator it = (CBRPageIterator)ps.pageIterator();
/* 126 */           pager = new CBRForwardPager(it, pageSize);
/*     */           
/*     */ 
/* 129 */           pager.loadItems(this.queryData.itemsToSkip);
/*     */         }
/*     */         
/* 132 */         List<CBRResult> hits = pager.loadNextPage();
/*     */         
/* 134 */         if (StringUtils.isNotEmpty(itemsNeededParameter)) {
/* 135 */           int itemsNeeded = Integer.parseInt(itemsNeededParameter);
/* 136 */           while ((itemsNeeded > pager.getNumberOfItemsRetrieved()) && (!pager.isEndReached())) {
/* 137 */             List<CBRResult> nextPage = pager.loadNextPage();
/* 138 */             hits.addAll(nextPage);
/*     */           }
/*     */         }
/*     */         
/* 142 */         isPageEnd = (pager == null) || (pager.isEndReached());
/* 143 */         this.queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/* 144 */         mediator.setMaxResultsReached(pager.isMaxResultsReached());
/*     */         
/* 146 */         Collection<RMClassDescription> classDescriptions = getUniqueClassDescriptions(this.queryData.searchClassNames, repository, request);
/* 147 */         mediator.setClassDescriptions(new ArrayList(classDescriptions));
/* 148 */         mediator.setCBRResultRows(hits);
/* 149 */         hitSize = hits != null ? hits.size() : 0;
/*     */       }
/*     */       
/* 152 */       if (isPageEnd) {
/* 153 */         session.removeAttribute(this.queryData.sessionKey);
/*     */       } else {
/* 155 */         String continuationData = this.queryData.saveToString();
/* 156 */         mediator.setContinuationData(continuationData);
/*     */       }
/*     */       
/* 159 */       mediator.setFilePlanRepository(repository);
/* 160 */       mediator.setParentDocID(this.queryData.folderId);
/*     */       
/* 162 */       List<String> requestedProperties = IERUtil.convertStringsToListString(this.queryData.properties);
/* 163 */       if (requestedProperties != null)
/* 164 */         requestedProperties.add("OnHold");
/* 165 */       mediator.setRequestedProperties(requestedProperties);
/*     */       
/* 167 */       mediator.addMessage(hitSize == 1 ? MessageCode.SEARCH_RESULTS_RETURNEDONE : MessageCode.SEARCH_RESULTS_RETURNED, new Object[] { Integer.valueOf(hitSize) });
/*     */       
/*     */ 
/* 170 */       Logger.logDebug(this, methodName, request, " after adding search results to session.");
/*     */     } catch (Exception e) {
/* 172 */       Logger.logError(this, methodName, request, e);
/*     */     }
/*     */     
/* 175 */     Logger.logExit(this, methodName, request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean validateRequest(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 182 */     String contData = request.getParameter("continuationData");
/* 183 */     String repository = request.getParameter("repositoryId");
/* 184 */     return (contData != null) && (repository != null);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\RetrieveNextPageService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */