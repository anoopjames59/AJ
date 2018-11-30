/*     */ package com.ibm.jarm.api.query;
/*     */ 
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMMergeMode;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALBaseDomain;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RMSearch
/*     */ {
/*  26 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*     */   
/*     */ 
/*     */ 
/*     */   private Repository[] repositories;
/*     */   
/*     */ 
/*     */ 
/*     */   private RMMergeMode jarmMergeMode;
/*     */   
/*     */ 
/*     */ 
/*     */   public RMSearch(Repository repository)
/*     */   {
/*  40 */     Tracer.traceMethodEntry(new Object[] { repository });
/*  41 */     Util.ckNullObjParam("repository", repository);
/*     */     
/*  43 */     this.repositories = new Repository[] { repository };
/*  44 */     this.jarmMergeMode = RMMergeMode.Intersection;
/*  45 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public RMSearch(Repository[] repositories, RMMergeMode mergeMode)
/*     */   {
/*  61 */     Tracer.traceMethodEntry(new Object[] { repositories, mergeMode });
/*  62 */     Util.ckNullOrInvalidArrayParam("repositories", repositories);
/*  63 */     Util.ckNullObjParam("mergeMode", mergeMode);
/*     */     
/*     */ 
/*  66 */     boolean homogeneousRepositories = true;
/*  67 */     RMDomain commonDomain = repositories[0].getDomain();
/*  68 */     RMDomain currentReposDomain = null;
/*  69 */     for (int i = 1; i < repositories.length; i++)
/*     */     {
/*  71 */       currentReposDomain = repositories[i].getDomain();
/*  72 */       if ((commonDomain == null) || (currentReposDomain == null) || (commonDomain.getDomainType() != currentReposDomain.getDomainType()) || (!commonDomain.getObjectIdentity().equals(currentReposDomain.getObjectIdentity())))
/*     */       {
/*     */ 
/*     */ 
/*  76 */         homogeneousRepositories = false;
/*  77 */         break;
/*     */       }
/*     */     }
/*  80 */     if (!homogeneousRepositories)
/*     */     {
/*  82 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_SEARCH_REPOSITORIES, new Object[0]);
/*     */     }
/*     */     
/*  85 */     this.repositories = repositories;
/*  86 */     this.jarmMergeMode = mergeMode;
/*  87 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository[] getRepositories()
/*     */   {
/*  98 */     return this.repositories;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMMergeMode getMergeMode()
/*     */   {
/* 109 */     return this.jarmMergeMode;
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
/*     */   public PageableSet<? extends BaseEntity> fetchObjects(String sqlStatement, EntityType entityType, Integer pageSize, RMPropertyFilter filter, Boolean continuable)
/*     */   {
/* 137 */     Tracer.traceMethodEntry(new Object[] { sqlStatement, entityType, pageSize, filter, continuable });
/* 138 */     Util.ckInvalidStrParam("sqlStatement", sqlStatement);
/* 139 */     Util.ckNullObjParam("entityType", entityType);
/* 140 */     PageableSet<? extends BaseEntity> result = null;
/*     */     
/* 142 */     RMDomain domain = getApplicableDomain();
/* 143 */     result = ((RALBaseDomain)domain).fetchObjects(this, sqlStatement, entityType, pageSize, filter, continuable);
/*     */     
/* 145 */     Tracer.traceMethodExit(new Object[] { result });
/* 146 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<ResultRow> fetchRows(String sqlStatement, Integer pageSize, RMPropertyFilter filter, Boolean continuable)
/*     */   {
/* 171 */     Tracer.traceMethodEntry(new Object[] { sqlStatement, pageSize, filter, continuable });
/* 172 */     Util.ckInvalidStrParam("sqlStatement", sqlStatement);
/* 173 */     PageableSet<ResultRow> result = null;
/*     */     
/* 175 */     RMDomain domain = getApplicableDomain();
/* 176 */     result = ((RALBaseDomain)domain).fetchRows(this, sqlStatement, pageSize, filter, continuable);
/*     */     
/* 178 */     Tracer.traceMethodExit(new Object[] { result });
/* 179 */     return result;
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
/*     */ 
/*     */   public PageableSet<CBRResult> contentBasedRetrieval(RMContentSearchDefinition srchDef, Integer pageSize, Boolean continuable)
/*     */   {
/* 197 */     Tracer.traceMethodEntry(new Object[] { srchDef, pageSize, continuable });
/*     */     
/* 199 */     RMDomain domain = getApplicableDomain();
/* 200 */     PageableSet<CBRResult> result = ((RALBaseDomain)domain).contentBasedRetrieval(this, srchDef, pageSize, continuable);
/*     */     
/* 202 */     Tracer.traceMethodExit(new Object[] { result });
/* 203 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RMDomain getApplicableDomain()
/*     */   {
/* 214 */     Tracer.traceMethodEntry(new Object[0]);
/* 215 */     RMDomain result = null;
/* 216 */     if ((this.repositories != null) && (this.repositories.length >= 1))
/*     */     {
/* 218 */       if (this.repositories[0] != null) {
/* 219 */         result = this.repositories[0].getDomain();
/*     */       }
/*     */     }
/* 222 */     Tracer.traceMethodExit(new Object[] { result });
/* 223 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\query\RMSearch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */