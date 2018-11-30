/*     */ package com.ibm.ier.plugin.services.requestFilters;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.ContentItem;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONArtifact;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.collections.list.SetUniqueList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8RetrieveItemsActionPreOverride
/*     */   extends IERBaseRequestFilterService
/*     */ {
/*     */   private static final String RECORDS_MANAGEMENT = "Records Management";
/*     */   
/*     */   public String[] getFilteredServices()
/*     */   {
/*  39 */     return new String[] { "/p8/getContentItems" };
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  43 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*     */     throws Exception
/*     */   {
/*  55 */     Repository repository = this.baseService.getRepositoryFromNexusRepositoryId(this.baseService.getNexusRepositoryId());
/*  56 */     String className = request.getParameter("template_name");
/*     */     
/*     */ 
/*  59 */     if ((repository != null) && ((repository instanceof FilePlanRepository)))
/*     */     {
/*  61 */       ArrayList<BaseEntity> results = new ArrayList();
/*  62 */       SetUniqueList requestedProperties = SetUniqueList.decorate(new ArrayList());
/*  63 */       String[] docIds = request.getParameterValues("docid");
/*     */       
/*  65 */       for (int i = 0; i < docIds.length; i++) {
/*  66 */         String id = IERUtil.getIdFromDocIdString(docIds[i]);
/*  67 */         if ((className == null) || (className.length() == 0))
/*  68 */           className = IERUtil.getClassFromDocIdString(docIds[i]);
/*  69 */         if (id == null) {
/*  70 */           id = docIds[i];
/*     */         }
/*  72 */         BaseEntity entity = null;
/*     */         try {
/*  74 */           if (className == null) {
/*  75 */             entity = RMFactory.BaseEntity.fetchInstance(repository, EntityType.Container, id, RMPropertyFilter.MinimumPropertySet);
/*  76 */             className = entity.getClassName();
/*     */           }
/*     */           else {
/*  79 */             entity = RMFactory.BaseEntity.fetchInstance(repository, className, id, RMPropertyFilter.MinimumPropertySet);
/*     */           }
/*     */         } catch (Exception exp) {
/*  82 */           return null;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*  87 */         if ((entity instanceof Container)) {
/*  88 */           Container container = (Container)entity;
/*  89 */           if (!container.getPathName().contains("Records Management")) {
/*  90 */             return null;
/*     */           }
/*     */         }
/*  93 */         else if ((entity instanceof ContentItem)) {
/*  94 */           return null;
/*     */         }
/*     */         
/*  97 */         requestedProperties.addAll(MinimumPropertiesUtil.getPropertySetList(entity.getEntityType()));
/*  98 */         results.add(entity);
/*     */       }
/*     */       
/* 101 */       IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/* 102 */       mediator.setPageSize(200);
/*     */       
/* 104 */       RMClassDescription cd = null;
/* 105 */       cd = SessionUtil.getClassDescription(repository, className, request);
/* 106 */       mediator.addClassDescription(cd);
/*     */       
/* 108 */       IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(request);
/* 109 */       searchResultsBean.setResults(results);
/*     */       
/* 111 */       mediator.setSearchResultsBean(searchResultsBean);
/* 112 */       mediator.setRequestedProperties(requestedProperties);
/* 113 */       if (cd != null) {
/* 114 */         mediator.setFolderDescription(cd.getDisplayName());
/*     */       }
/*     */       
/* 117 */       JSONObject jsonResponse = mediator.toJSONObject();
/* 118 */       return jsonResponse;
/*     */     }
/*     */     
/* 121 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8RetrieveItemsActionPreOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */