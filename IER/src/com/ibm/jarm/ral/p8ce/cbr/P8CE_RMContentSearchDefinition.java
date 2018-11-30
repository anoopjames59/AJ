/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.OrderBy;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.SortOrder;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_RMContentSearchDefinition
/*     */   extends RMContentSearchDefinition
/*     */ {
/*     */   private static final String msContentQuerySelectClause = "SELECT  d.id, d.RecordInformation, c.Rank FROM [Document] d inner join contentsearch c on d.this = c.queriedobject ";
/*     */   private static final String msContentQueryWHERECriteria = " d.RecordInformation IS not null ";
/*     */   private static final String msContentQueryContainsClause = " (contains(content, '";
/*     */   private static final String msContentQueryOrderBy = " ORDER BY c.Rank ";
/*  19 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public P8CE_RMContentSearchDefinition() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public P8CE_RMContentSearchDefinition(String selectClause, String fromClause, String whereClause, String orderBy, String sqlAlias, String content, RMContentSearchDefinition.SortOrder so, RMContentSearchDefinition.OrderBy ob, boolean cbrConditionOnly)
/*     */   {
/*  36 */     super(selectClause, fromClause, whereClause, orderBy, sqlAlias, content, so, ob, cbrConditionOnly);
/*  37 */     Tracer.traceMethodEntry(new Object[] { selectClause, fromClause, whereClause, orderBy, sqlAlias, content, so, ob, Boolean.valueOf(cbrConditionOnly) });
/*  38 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String buildContentQueryStmt()
/*     */   {
/*  46 */     Tracer.traceMethodEntry(new Object[0]);
/*  47 */     String contentSearch = getContentSearch();
/*     */     
/*     */ 
/*  50 */     String queryStmtPart2 = " (contains(content, '" + contentSearch + "'))";
/*     */     
/*  52 */     StringBuffer sb = new StringBuffer("SELECT  d.id, d.RecordInformation, c.Rank FROM [Document] d inner join contentsearch c on d.this = c.queriedobject ");
/*  53 */     sb.append(" WHERE ").append(" ( ").append(" d.RecordInformation IS not null ").append(" AND ").append(queryStmtPart2).append(" ) ");
/*     */     
/*     */ 
/*  56 */     sb.append(" ORDER BY c.Rank ");
/*  57 */     if (getSortOrder() != RMContentSearchDefinition.SortOrder.none) {
/*  58 */       sb.append(getSortOrder());
/*     */     }
/*  60 */     String result = sb.toString();
/*  61 */     Tracer.traceMethodExit(new Object[] { result });
/*  62 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String buildContentQueryStmt(List<String> idList, boolean bRankSort)
/*     */   {
/*  70 */     Tracer.traceMethodEntry(new Object[] { idList });
/*  71 */     String contentSearch = getContentSearch();
/*     */     
/*     */ 
/*  74 */     String queryContainsClause = " (contains(content, '" + contentSearch + "'))";
/*     */     
/*  76 */     StringBuffer sb = new StringBuffer("SELECT  d.id, d.RecordInformation, c.Rank FROM [Document] d inner join contentsearch c on d.this = c.queriedobject ");
/*  77 */     sb.append(" WHERE ").append(queryContainsClause);
/*     */     
/*  79 */     int rowIdListSize = idList.size();
/*  80 */     StringBuffer idBuf = new StringBuffer(" ( ");
/*  81 */     for (int i = 0; i < rowIdListSize; i++)
/*     */     {
/*  83 */       idBuf.append("(d.").append("RecordInformation").append("=Object('").append((String)idList.get(i)).append("')) ");
/*  84 */       if (i < rowIdListSize - 1)
/*  85 */         idBuf.append(" or ");
/*     */     }
/*  87 */     idBuf.append(" ) ");
/*     */     
/*  89 */     if (rowIdListSize > 0) {
/*  90 */       sb.append(" AND ").append(idBuf);
/*     */     }
/*  92 */     if (bRankSort) {
/*  93 */       sb.append(" ORDER BY c.Rank ");
/*  94 */       if (getSortOrder() != RMContentSearchDefinition.SortOrder.none) {
/*  95 */         sb.append(getSortOrder());
/*     */       }
/*     */     }
/*  98 */     String result = sb.toString();
/*  99 */     Tracer.traceMethodExit(new Object[] { result });
/* 100 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasContentQueryDefined()
/*     */   {
/* 108 */     Tracer.traceMethodEntry(new Object[0]);
/* 109 */     boolean result = (getContentSearch() != null) && (getContentSearch().length() != 0);
/* 110 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 111 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_RMContentSearchDefinition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */