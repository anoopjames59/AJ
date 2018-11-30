/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.query.RMSearch;
/*     */ import com.ibm.jarm.api.query.ResultRow;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ public class IERQuery
/*     */ {
/*     */   private static final int STRING_BUFFER_SIZE = 400;
/*     */   private Collection<String> subProperties;
/*     */   private Collection<String> requestedProperties;
/*     */   private String fromClause;
/*     */   private String whereClause;
/*     */   private String orderByClause;
/*     */   private Repository repository;
/*  37 */   private int topLimit = 0;
/*  38 */   private int pageSize = 200;
/*  39 */   private boolean addBracketsAroundSelectProperties = true;
/*     */   
/*     */   public void setAddBracketsAroundSelectProperties(boolean value)
/*     */   {
/*  43 */     this.addBracketsAroundSelectProperties = value;
/*     */   }
/*     */   
/*     */   public Repository getRepository()
/*     */   {
/*  48 */     return this.repository;
/*     */   }
/*     */   
/*     */   public void setRepository(Repository repository)
/*     */   {
/*  53 */     this.repository = repository;
/*     */   }
/*     */   
/*     */   public int getPageSize()
/*     */   {
/*  58 */     return this.pageSize;
/*     */   }
/*     */   
/*     */   public void setPageSize(int pageSize)
/*     */   {
/*  63 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */   public String getFromClause()
/*     */   {
/*  68 */     return this.fromClause;
/*     */   }
/*     */   
/*     */   public void setFromClause(String fromClause)
/*     */   {
/*  73 */     this.fromClause = fromClause;
/*     */   }
/*     */   
/*     */   public void setOrderByClause(String orderByClause)
/*     */   {
/*  78 */     this.orderByClause = orderByClause;
/*     */   }
/*     */   
/*     */   public String getOrderByClause()
/*     */   {
/*  83 */     return this.orderByClause;
/*     */   }
/*     */   
/*     */   public Collection<String> getRequestedProperties()
/*     */   {
/*  88 */     return this.requestedProperties;
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(String... props)
/*     */   {
/*  93 */     this.requestedProperties = new HashSet();
/*  94 */     this.requestedProperties.addAll(new HashSet(Arrays.asList(props)));
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(Collection<String> props)
/*     */   {
/*  99 */     this.requestedProperties = props;
/*     */   }
/*     */   
/*     */   public Collection<String> getSubProperties()
/*     */   {
/* 104 */     return this.subProperties;
/*     */   }
/*     */   
/*     */   public void setSubProperties(Collection<String> subProperties)
/*     */   {
/* 109 */     this.subProperties = subProperties;
/*     */   }
/*     */   
/*     */   public String getWhereClause()
/*     */   {
/* 114 */     return this.whereClause;
/*     */   }
/*     */   
/*     */   public void setWhereClause(String whereClause)
/*     */   {
/* 119 */     this.whereClause = whereClause;
/*     */   }
/*     */   
/*     */   public void setTopLimitSize(int topLimit)
/*     */   {
/* 124 */     this.topLimit = topLimit;
/*     */   }
/*     */   
/*     */   public String generateSQL()
/*     */   {
/* 129 */     StringBuilder sql = new StringBuilder(400);
/* 130 */     sql.append("SELECT ");
/*     */     
/* 132 */     if (this.topLimit != 0) {
/* 133 */       sql.append("TOP ");
/* 134 */       sql.append(this.topLimit);
/* 135 */       sql.append(" ");
/*     */     }
/*     */     Iterator<String> iterator;
/* 138 */     if ((this.requestedProperties != null) && (this.requestedProperties.size() != 0))
/*     */     {
/* 140 */       for (iterator = this.requestedProperties.iterator(); iterator.hasNext();)
/*     */       {
/* 142 */         String prop = (String)iterator.next();
/* 143 */         if (this.addBracketsAroundSelectProperties)
/* 144 */           sql.append('[');
/* 145 */         sql.append(prop);
/* 146 */         if (this.addBracketsAroundSelectProperties) {
/* 147 */           sql.append(']');
/*     */         }
/* 149 */         if (iterator.hasNext()) {
/* 150 */           sql.append(", ");
/*     */         }
/*     */       }
/*     */     }
/* 154 */     sql.append(" FROM ");
/* 155 */     sql.append(this.fromClause);
/*     */     
/* 157 */     if ((this.whereClause != null) && (this.whereClause.length() != 0))
/*     */     {
/* 159 */       sql.append(" WHERE ");
/* 160 */       sql.append(this.whereClause);
/*     */     }
/*     */     
/* 163 */     if ((this.orderByClause != null) && (this.orderByClause.length() != 0))
/*     */     {
/* 165 */       sql.append(" ORDER BY ");
/* 166 */       sql.append(this.orderByClause);
/*     */     }
/*     */     
/* 169 */     String sqlString = sql.toString();
/*     */     
/* 171 */     return sqlString;
/*     */   }
/*     */   
/*     */   public Iterator<ResultRow> executeQueryAsRowsIterator()
/*     */   {
/* 176 */     RMSearch scope = new RMSearch(this.repository);
/* 177 */     String sql = generateSQL();
/* 178 */     RMPropertyFilter filter = configurePropertyFilter(this.subProperties);
/* 179 */     PageableSet<ResultRow> rows = scope.fetchRows(sql, Integer.valueOf(this.pageSize), filter, Boolean.TRUE);
/* 180 */     return rows.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<BaseEntity> executeQueryAsObjectsIterator(EntityType entityType)
/*     */   {
/* 185 */     String sql = generateSQL();
/* 186 */     return executeQueryAsObjectsIterator(this.repository, sql, this.subProperties, this.pageSize, entityType);
/*     */   }
/*     */   
/*     */   public static Iterator<BaseEntity> executeQueryAsObjectsIterator(Repository repository, String sql, Collection<String> subProperties, int pageSize, EntityType entityType) {
/* 190 */     PageableSet<BaseEntity> row = executeQueryAsObjectsPageableSet(repository, sql, subProperties, pageSize, entityType);
/* 191 */     return row.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public static PageableSet<BaseEntity> executeQueryAsObjectsPageableSet(Repository repository, String sql, Collection<String> subProperties, int pageSize, EntityType entityType)
/*     */   {
/* 197 */     RMSearch scope = new RMSearch(repository);
/*     */     
/* 199 */     RMPropertyFilter filter = configurePropertyFilter(subProperties);
/* 200 */     PageableSet<BaseEntity> rows = scope.fetchObjects(sql, entityType, Integer.valueOf(pageSize), filter, Boolean.TRUE);
/*     */     
/* 202 */     return rows;
/*     */   }
/*     */   
/*     */   public static Iterator<ResultRow> executeQueryAsRowsIterator(Repository repository, String sql, Collection<String> subProperties, int pageSize)
/*     */   {
/* 207 */     RMSearch scope = new RMSearch(repository);
/* 208 */     RMPropertyFilter filter = configurePropertyFilter(subProperties);
/* 209 */     PageableSet<ResultRow> rows = scope.fetchRows(sql, Integer.valueOf(pageSize), filter, Boolean.TRUE);
/* 210 */     return rows.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static RMPropertyFilter configurePropertyFilter(Collection<String> properties)
/*     */   {
/* 217 */     RMPropertyFilter filter = new RMPropertyFilter();
/* 218 */     if (properties != null)
/*     */     {
/*     */ 
/* 221 */       for (String propertyName : properties) {
/* 222 */         filter.addIncludeProperty(Integer.valueOf(1), null, new Boolean(false), propertyName, null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 231 */       filter.addIncludeProperty(Integer.valueOf(1), null, new Boolean(false), "Name", null);
/* 232 */       filter.addIncludeProperty(Integer.valueOf(1), null, new Boolean(false), "ClassName", null);
/* 233 */       filter.addIncludeProperty(Integer.valueOf(1), null, new Boolean(false), "Tail", null);
/* 234 */       filter.addIncludeProperty(Integer.valueOf(1), null, new Boolean(false), "HoldName", null);
/*     */       
/*     */ 
/* 237 */       filter.setMaxRecursion(0);
/*     */     }
/* 239 */     return filter;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 244 */     return "(" + generateSQL() + ")";
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\IERQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */