/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.constants.FilteredPropertyType;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8Query
/*     */ {
/*     */   private static final int STRING_BUFFER_SIZE = 400;
/*     */   private static final int DEFAULT_PAGE_SIZE = 200;
/*     */   private Collection<String> subProperties;
/*     */   private Collection<String> requestedProperties;
/*     */   private String fromClause;
/*     */   private String whereClause;
/*     */   private String orderByClause;
/*     */   private ObjectStore objectStore;
/*  42 */   private int pageSize = 200;
/*  43 */   private boolean addBracketsAroundSelectProperties = true;
/*     */   
/*     */   public void setAddBracketsAroundSelectProperties(boolean value) {
/*  46 */     this.addBracketsAroundSelectProperties = value;
/*     */   }
/*     */   
/*     */   public ObjectStore getObjectStore() {
/*  50 */     return this.objectStore;
/*     */   }
/*     */   
/*     */   public void setObjectStore(ObjectStore objectStore) {
/*  54 */     this.objectStore = objectStore;
/*     */   }
/*     */   
/*     */   public int getPageSize() {
/*  58 */     return this.pageSize;
/*     */   }
/*     */   
/*     */   public void setPageSize(int pageSize) {
/*  62 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */   public String getFromClause() {
/*  66 */     return this.fromClause;
/*     */   }
/*     */   
/*     */   public void setFromClause(String fromClause) {
/*  70 */     this.fromClause = fromClause;
/*     */   }
/*     */   
/*     */   public void setOrderByClause(String orderByClause) {
/*  74 */     this.orderByClause = orderByClause;
/*     */   }
/*     */   
/*     */   public String getOrderByClause() {
/*  78 */     return this.orderByClause;
/*     */   }
/*     */   
/*     */   public Collection<String> getRequestedProperties() {
/*  82 */     return this.requestedProperties;
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(String... props) {
/*  86 */     this.requestedProperties = new HashSet();
/*  87 */     this.requestedProperties.addAll(new HashSet(Arrays.asList(props)));
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(Collection<String> props) {
/*  91 */     this.requestedProperties = props;
/*     */   }
/*     */   
/*     */   public Collection<String> getSubProperties() {
/*  95 */     return this.subProperties;
/*     */   }
/*     */   
/*     */   public void setSubProperties(Collection<String> subProperties) {
/*  99 */     this.subProperties = subProperties;
/*     */   }
/*     */   
/*     */   public String getWhereClause() {
/* 103 */     return this.whereClause;
/*     */   }
/*     */   
/*     */   public void setWhereClause(String whereClause) {
/* 107 */     this.whereClause = whereClause;
/*     */   }
/*     */   
/*     */   public String generateSQL() {
/* 111 */     StringBuilder sql = new StringBuilder(400);
/* 112 */     sql.append("SELECT ");
/*     */     Iterator<String> iterator;
/* 114 */     if ((this.requestedProperties != null) && (this.requestedProperties.size() != 0)) {
/* 115 */       for (iterator = this.requestedProperties.iterator(); iterator.hasNext();) {
/* 116 */         String prop = (String)iterator.next();
/* 117 */         if (this.addBracketsAroundSelectProperties)
/* 118 */           sql.append('[');
/* 119 */         sql.append(prop);
/* 120 */         if (this.addBracketsAroundSelectProperties) {
/* 121 */           sql.append(']');
/*     */         }
/* 123 */         if (iterator.hasNext())
/* 124 */           sql.append(", ");
/*     */       }
/*     */     } else {
/* 127 */       sql.append("*");
/*     */     }
/*     */     
/* 130 */     sql.append(" FROM ");
/* 131 */     sql.append(this.fromClause);
/*     */     
/* 133 */     if ((this.whereClause != null) && (this.whereClause.length() != 0)) {
/* 134 */       sql.append(" WHERE ");
/* 135 */       sql.append(this.whereClause);
/*     */     }
/*     */     
/* 138 */     if ((this.orderByClause != null) && (this.orderByClause.length() != 0)) {
/* 139 */       sql.append(" ORDER BY ");
/* 140 */       sql.append(this.orderByClause);
/*     */     }
/*     */     
/* 143 */     String sqlString = sql.toString();
/*     */     
/* 145 */     return sqlString;
/*     */   }
/*     */   
/*     */   public static Iterator<IndependentObject> executeQueryAsObjectsIterator(ObjectStore objectStore, String sql, Collection<String> subProperties, int pageSize, PropertyFilter filter) {
/* 149 */     SearchScope scope = new SearchScope(objectStore);
/* 150 */     SearchSQL search = new SearchSQL(sql);
/*     */     
/* 152 */     if (filter == null) {
/* 153 */       filter = configurePropertyFilter(subProperties);
/*     */     }
/* 155 */     IndependentObjectSet rows = scope.fetchObjects(search, Integer.valueOf(pageSize), filter, Boolean.TRUE);
/* 156 */     return rows.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<IndependentObject> executeQueryAsObjectsIterator() {
/* 160 */     String sql = generateSQL();
/* 161 */     return executeQueryAsObjectsIterator(this.objectStore, sql, this.subProperties, this.pageSize, null);
/*     */   }
/*     */   
/*     */   private static PropertyFilter configurePropertyFilter(Collection<String> properties) {
/* 165 */     PropertyFilter filter = new PropertyFilter();
/* 166 */     if (properties != null) {
/* 167 */       for (String propertyName : properties) {
/* 168 */         filter.addIncludeProperty(1, null, null, propertyName, null);
/*     */       }
/* 170 */       filter.setMaxRecursion(1);
/*     */     }
/* 172 */     return filter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyFilter getDocumentPropertyFilter()
/*     */   {
/* 181 */     PropertyFilter propertyFilter = new PropertyFilter();
/*     */     
/* 183 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_BOOLEAN, null);
/* 184 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_DATE, null);
/* 185 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_DOUBLE, null);
/* 186 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_GUID, null);
/* 187 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_LONG, null);
/* 188 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_STRING, null);
/* 189 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_BOOLEAN, null);
/* 190 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_DATE, null);
/* 191 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_DOUBLE, null);
/* 192 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_GUID, null);
/* 193 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_LONG, null);
/* 194 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_STRING, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 199 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "ContentSize", null));
/* 200 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "MimeType", null));
/* 201 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IsReserved", null));
/* 202 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IsCurrentVersion", null));
/* 203 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IsVersioningEnabled", null));
/* 204 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Reservation", null));
/* 205 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "ReservationType", null));
/* 206 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "Creator", null));
/* 207 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "Owner", null));
/* 208 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "VersionStatus", null));
/* 209 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "MajorVersionNumber", null));
/* 210 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "MinorVersionNumber", null));
/*     */     
/* 212 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "ClassDescription", null));
/* 213 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "PropertyDescriptions", null));
/* 214 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(4), null, null, "SymbolicName", null));
/* 215 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(4), null, null, "DataType", null));
/* 216 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(4), null, null, "IsHidden", null));
/* 217 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(4), null, null, "ChoiceList", null));
/*     */     
/* 219 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "ReplicationGroup", null));
/* 220 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "DisplayName", null));
/*     */     
/* 222 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "EntryTemplateObjectStoreName", null));
/* 223 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "EntryTemplateId", null));
/* 224 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "RecordInformation", null));
/* 225 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Parent", null));
/*     */     
/* 227 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IcnAutoRun", null));
/* 228 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IcnShowInTree", null));
/*     */     
/* 230 */     return propertyFilter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyFilter getFolderPropertyFilter()
/*     */   {
/* 239 */     PropertyFilter propertyFilter = new PropertyFilter();
/*     */     
/* 241 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_BOOLEAN, null);
/* 242 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_DATE, null);
/* 243 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_DOUBLE, null);
/* 244 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_GUID, null);
/* 245 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_LONG, null);
/* 246 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.LIST_OF_STRING, null);
/* 247 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_BOOLEAN, null);
/* 248 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_DATE, null);
/* 249 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_DOUBLE, null);
/* 250 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_GUID, null);
/* 251 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_LONG, null);
/* 252 */     propertyFilter.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_STRING, null);
/*     */     
/* 254 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Parent", null));
/* 255 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "Id", null));
/* 256 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "WorkspaceName", null));
/*     */     
/* 258 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "ClassDescription", null));
/* 259 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "PropertyDescriptions", null));
/* 260 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(4), null, null, "SymbolicName", null));
/* 261 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(4), null, null, "DataType", null));
/* 262 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(4), null, null, "IsHidden", null));
/*     */     
/* 264 */     return propertyFilter;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 268 */     return "(" + generateSQL() + ")";
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8Query.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */