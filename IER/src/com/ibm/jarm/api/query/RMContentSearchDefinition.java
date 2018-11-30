/*     */ package com.ibm.jarm.api.query;
/*     */ 
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.RALService;
/*     */ import java.util.List;
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
/*     */ public abstract class RMContentSearchDefinition
/*     */   implements Cloneable
/*     */ {
/*  45 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*     */   private String selectClause;
/*     */   private String fromClause;
/*     */   private String whereClause;
/*     */   private String orderClause;
/*     */   private String sqlFromClassAlias;
/*     */   private AndOrOper operBtwContentAndMetadataSearch;
/*     */   
/*     */   public static enum SortOrder
/*     */   {
/*  55 */     asc, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  60 */     desc, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  65 */     none;
/*     */     
/*     */ 
/*     */ 
/*     */     private SortOrder() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum AndOrOper
/*     */   {
/*  76 */     and, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  81 */     or;
/*     */     
/*     */ 
/*     */ 
/*     */     private AndOrOper() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum OrderBy
/*     */   {
/*  92 */     metadata, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  97 */     cbrscores, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 102 */     none;
/*     */     
/*     */ 
/*     */ 
/*     */     private OrderBy() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum ContentSearchOption
/*     */   {
/* 113 */     content, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 118 */     property;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private ContentSearchOption() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMContentSearchDefinition()
/*     */   {
/* 139 */     Tracer.traceMethodEntry(new Object[0]);
/* 140 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String contentSearch;
/*     */   
/*     */ 
/*     */   private SortOrder sortOrder;
/*     */   
/*     */   private boolean isCBRConditionOnly;
/*     */   
/*     */   private String commonWhereClause;
/*     */   
/*     */   private OrderBy orderBy;
/*     */   
/*     */   private ContentSearchOption searchOption;
/*     */   
/*     */   public RMContentSearchDefinition(String selectClause, String fromClause, String whereClause, String orderClause, String sqlAlias, String content, SortOrder sortOrder, OrderBy orderBy, boolean cbrConditionOnly)
/*     */   {
/* 160 */     Tracer.traceMethodEntry(new Object[] { selectClause, fromClause, whereClause, orderClause, sqlAlias, content, sortOrder, orderBy, Boolean.valueOf(cbrConditionOnly) });
/*     */     
/* 162 */     this.selectClause = selectClause;
/* 163 */     this.fromClause = fromClause;
/* 164 */     this.whereClause = whereClause;
/* 165 */     this.orderClause = orderClause;
/* 166 */     this.sqlFromClassAlias = sqlAlias;
/* 167 */     this.contentSearch = content;
/* 168 */     this.sortOrder = sortOrder;
/* 169 */     this.orderBy = orderBy;
/* 170 */     this.isCBRConditionOnly = cbrConditionOnly;
/*     */     
/* 172 */     this.searchOption = ContentSearchOption.content;
/* 173 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommonWhereClause()
/*     */   {
/* 183 */     return this.commonWhereClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCommonWhereClause(String commonWhereClause)
/*     */   {
/* 193 */     this.commonWhereClause = commonWhereClause;
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
/*     */   public static RMContentSearchDefinition createInstance(RMDomain rmDomain)
/*     */   {
/* 206 */     Tracer.traceMethodEntry(new Object[] { rmDomain });
/* 207 */     RALService ralService = RMFactory.getRALService(rmDomain);
/* 208 */     RMContentSearchDefinition result = ralService.createRMSearchDefinition();
/* 209 */     Tracer.traceMethodExit(new Object[] { result });
/* 210 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCBRConditionOnly()
/*     */   {
/* 220 */     return this.isCBRConditionOnly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCBRConditionOnly(boolean isCBRConditionOnly)
/*     */   {
/* 230 */     this.isCBRConditionOnly = isCBRConditionOnly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSelectClause()
/*     */   {
/* 240 */     return this.selectClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectClause(String selectClause)
/*     */   {
/* 250 */     this.selectClause = selectClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFromClause()
/*     */   {
/* 260 */     return this.fromClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFromClause(String fromClause)
/*     */   {
/* 270 */     this.fromClause = fromClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getWhereClause()
/*     */   {
/* 280 */     return this.whereClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWhereClause(String whereClause)
/*     */   {
/* 290 */     this.whereClause = whereClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOrderClause()
/*     */   {
/* 300 */     return this.orderClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrderClause(String orderClause)
/*     */   {
/* 311 */     this.orderClause = orderClause;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSqlAlias()
/*     */   {
/* 321 */     return this.sqlFromClassAlias;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSqlAlias(String sqlAlias)
/*     */   {
/* 331 */     this.sqlFromClassAlias = sqlAlias;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AndOrOper getOperBtwContentAndMetadataSearch()
/*     */   {
/* 341 */     return this.operBtwContentAndMetadataSearch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOperBtwContentAndMetadataSearch(AndOrOper operBtwContentAndMetadataSearch)
/*     */   {
/* 352 */     this.operBtwContentAndMetadataSearch = operBtwContentAndMetadataSearch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentSearchOption getContentSearchOption()
/*     */   {
/* 362 */     return this.searchOption;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentSearchOption(ContentSearchOption option)
/*     */   {
/* 372 */     this.searchOption = option;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentSearch()
/*     */   {
/* 382 */     return this.contentSearch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentSearch(String contentSearch)
/*     */   {
/* 392 */     this.contentSearch = contentSearch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortOrder getSortOrder()
/*     */   {
/* 402 */     return this.sortOrder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortOrder(SortOrder sortOrder)
/*     */   {
/* 412 */     this.sortOrder = sortOrder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OrderBy getOrderBy()
/*     */   {
/* 422 */     return this.orderBy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrderBy(OrderBy orderBy)
/*     */   {
/* 432 */     this.orderBy = orderBy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String buildSQLStmt()
/*     */   {
/* 442 */     Tracer.traceMethodEntry(new Object[0]);
/* 443 */     String ret = buildSQLStmt(this.whereClause);
/* 444 */     Tracer.traceMethodExit(new Object[] { ret });
/* 445 */     return ret;
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
/*     */   public String buildSQLStmtFromCommonCriteria()
/*     */   {
/* 458 */     Tracer.traceMethodEntry(new Object[0]);
/* 459 */     String ret = buildSQLStmt(getCommonWhereClause());
/* 460 */     Tracer.traceMethodExit(new Object[] { ret });
/* 461 */     return ret;
/*     */   }
/*     */   
/*     */   private String buildSQLStmt(String whereClause)
/*     */   {
/* 466 */     Tracer.traceMethodEntry(new Object[0]);
/* 467 */     StringBuffer sb = new StringBuffer(getSelectClause());
/* 468 */     sb.append(" ").append(getFromClause()).append(" ").append(whereClause);
/* 469 */     if (getOrderBy() == OrderBy.metadata)
/*     */     {
/* 471 */       sb.append(" ").append(getOrderClause());
/* 472 */       if (getSortOrder() != SortOrder.none) {
/* 473 */         sb.append(" ").append(getSortOrder());
/*     */       }
/*     */     }
/* 476 */     String result = sb.toString();
/* 477 */     Tracer.traceMethodExit(new Object[] { result });
/* 478 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 489 */     Tracer.traceMethodEntry(new Object[0]);
/* 490 */     RMContentSearchDefinition srcDef = null;
/*     */     try
/*     */     {
/* 493 */       srcDef = (RMContentSearchDefinition)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException ex)
/*     */     {
/* 497 */       srcDef = null;
/*     */     }
/* 499 */     Tracer.traceMethodExit(new Object[] { srcDef });
/* 500 */     return srcDef;
/*     */   }
/*     */   
/*     */   public abstract String buildContentQueryStmt();
/*     */   
/*     */   public abstract String buildContentQueryStmt(List<String> paramList, boolean paramBoolean);
/*     */   
/*     */   public abstract boolean hasContentQueryDefined();
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\query\RMContentSearchDefinition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */