/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SearchTemplateBase
/*     */ {
/*     */   public static final String SEARCH_MIMETYPE = "application/x-searchtemplate";
/*     */   public static final String AUTO_SEARCH_MIMETYPE = "application/x-searchtemplate.automatic";
/*     */   public static final String UNIFIED_SEARCH_MIMETYPE = "application/x-unifiedsearchtemplate";
/*     */   private String id;
/*     */   private String description;
/*     */   private String displayName;
/*     */   private boolean autoRun;
/*     */   private boolean showInTree;
/*     */   private boolean andSearch;
/*     */   private ObjectType objectType;
/*     */   private List<SearchCriteria> criteria;
/*     */   private ResultsDisplay resultDisplay;
/*     */   
/*     */   public static enum VersionOption
/*     */   {
/*  28 */     currentversion,  releasedversion,  allversions,  none;
/*     */     
/*     */     private VersionOption() {} }
/*     */   
/*  32 */   public static enum ObjectType { document,  folder,  all,  common,  customobject,  summaryTable;
/*     */     
/*     */ 
/*     */     private ObjectType() {}
/*     */   }
/*     */   
/*     */   public SearchTemplateBase()
/*     */   {
/*  40 */     this.andSearch = true;
/*     */     
/*  42 */     this.criteria = new ArrayList();
/*     */   }
/*     */   
/*     */   public String getId() {
/*  46 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  50 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  54 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/*  58 */     this.description = description;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/*  62 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/*  66 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void setAutoRun(boolean autoRun) {
/*  70 */     this.autoRun = autoRun;
/*     */   }
/*     */   
/*     */   public boolean isAutoRun() {
/*  74 */     return this.autoRun;
/*     */   }
/*     */   
/*     */   public void setShowInTree(boolean showInTree) {
/*  78 */     this.showInTree = showInTree;
/*     */   }
/*     */   
/*     */   public boolean isShowInTree() {
/*  82 */     return this.showInTree;
/*     */   }
/*     */   
/*     */   public void setAndSearch(boolean andSearch) {
/*  86 */     this.andSearch = andSearch;
/*     */   }
/*     */   
/*     */   public boolean isAndSearch() {
/*  90 */     return this.andSearch;
/*     */   }
/*     */   
/*     */   public ObjectType getObjectType() {
/*  94 */     return this.objectType;
/*     */   }
/*     */   
/*     */   public void setObjectType(ObjectType objectType) {
/*  98 */     this.objectType = objectType;
/*     */   }
/*     */   
/*     */   public void addCriterion(SearchCriterion criterion) {
/* 102 */     if (this.criteria == null) {
/* 103 */       this.criteria = new ArrayList();
/*     */     }
/* 105 */     this.criteria.add(criterion);
/*     */   }
/*     */   
/*     */   public void addChild(ChildSearchCriteria childCriterion) {
/* 109 */     if (this.criteria == null) {
/* 110 */       this.criteria = new ArrayList();
/*     */     }
/* 112 */     this.criteria.add(childCriterion);
/*     */   }
/*     */   
/*     */   public List<SearchCriteria> getSearchCriteria() {
/* 116 */     return this.criteria;
/*     */   }
/*     */   
/*     */   public ResultsDisplay getResultsDisplay() {
/* 120 */     return this.resultDisplay;
/*     */   }
/*     */   
/*     */   public void setResultsDisplay(ResultsDisplay resultDisplay) {
/* 124 */     this.resultDisplay = resultDisplay;
/*     */   }
/*     */   
/*     */   public void fromJSON(JSONObject searchTemplateJson) {
/* 128 */     Object objectTypeParam = searchTemplateJson.get("objectType");
/* 129 */     ObjectType objectType = (objectTypeParam != null) && (((String)objectTypeParam).length() > 0) ? ObjectType.valueOf((String)objectTypeParam) : null;
/* 130 */     setObjectType(objectType);
/* 131 */     Object andSearch = searchTemplateJson.get("andSearch");
/* 132 */     if (andSearch != null) {
/* 133 */       setAndSearch(Boolean.parseBoolean(andSearch.toString()));
/*     */     }
/* 135 */     Object autoRun = searchTemplateJson.get("autoRun");
/* 136 */     if (autoRun != null) {
/* 137 */       setAutoRun(Boolean.parseBoolean(autoRun.toString()));
/*     */     }
/*     */     
/* 140 */     Object showInTree = searchTemplateJson.get("showInTree");
/* 141 */     if (showInTree != null) {
/* 142 */       setShowInTree(Boolean.parseBoolean(showInTree.toString()));
/*     */     }
/*     */     
/* 145 */     JSONArray searchCriteriaJson = (JSONArray)searchTemplateJson.get("searchCriteria");
/* 146 */     if (searchCriteriaJson != null) {
/* 147 */       for (int i = 0; i < searchCriteriaJson.size(); i++) {
/* 148 */         fromSearchCriterionJson((JSONObject)searchCriteriaJson.get(i));
/*     */       }
/*     */     }
/*     */     
/* 152 */     JSONObject resultsDisplayJson = (JSONObject)searchTemplateJson.get("resultsDisplay");
/* 153 */     if (resultsDisplayJson != null) {
/* 154 */       ResultsDisplay resultsDisplay = new ResultsDisplay();
/* 155 */       resultsDisplay.fromJSON(resultsDisplayJson);
/* 156 */       setResultsDisplay(resultsDisplay);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fromSearchCriterionJson(JSONObject searchCriterionJson) {
/* 161 */     if (searchCriterionJson.get("id") != null) {
/* 162 */       SearchCriterion criterion = new SearchCriterion();
/* 163 */       criterion.fromJSON(searchCriterionJson);
/* 164 */       addCriterion(criterion);
/* 165 */     } else if (searchCriterionJson.get("template_name") != null) {
/* 166 */       ChildSearchCriteria childCriteria = new ChildSearchCriteria();
/* 167 */       childCriteria.setName((String)searchCriterionJson.get("template_name"));
/* 168 */       ArrayList<SearchCriterion> criterionArray = new ArrayList();
/* 169 */       JSONArray childCriteriaJson = (JSONArray)searchCriterionJson.get("searchCriteria");
/*     */       
/* 171 */       if (childCriteriaJson != null) {
/* 172 */         for (int j = 0; j < childCriteriaJson.size(); j++) {
/* 173 */           SearchCriterion criterion = new SearchCriterion();
/* 174 */           criterion.fromJSON((JSONObject)childCriteriaJson.get(j));
/* 175 */           criterionArray.add(criterion);
/*     */         }
/* 177 */         childCriteria.setSearchCriteria(criterionArray);
/*     */       }
/* 179 */       addChild(childCriteria);
/*     */     } }
/*     */   
/*     */   public static class ResultsDisplay { private String sortBy;
/*     */     private boolean sortAscending;
/*     */     
/* 185 */     private static enum SORT_ORDER { ascending,  descending;
/*     */       
/*     */ 
/*     */       private SORT_ORDER() {}
/*     */     }
/*     */     
/*     */ 
/*     */     public ResultsDisplay() {}
/*     */     
/*     */ 
/*     */     public ResultsDisplay(String sortByProperty, boolean ascending, String[] columns)
/*     */     {
/* 197 */       this.sortBy = sortByProperty;
/* 198 */       this.sortAscending = ascending;
/* 199 */       this.columns = columns;
/*     */     }
/*     */     
/*     */     public String getSortByProperty() {
/* 203 */       return this.sortBy;
/*     */     }
/*     */     
/*     */     public void setSortByProperty(String sortBy) {
/* 207 */       this.sortBy = sortBy;
/*     */     }
/*     */     
/*     */     public boolean getSortAscending() {
/* 211 */       return this.sortAscending;
/*     */     }
/*     */     
/*     */     public void setSortAscending(boolean ascending) {
/* 215 */       this.sortAscending = ascending;
/*     */     }
/*     */     
/*     */     public String[] getColumns() {
/* 219 */       return this.columns;
/*     */     }
/*     */     
/*     */     public void setColumns(String[] columns) {
/* 223 */       this.columns = columns;
/*     */     }
/*     */     
/*     */     private String[] columns;
/*     */     private String[] magazineColumns;
/*     */     public String[] getMagazineColumns()
/*     */     {
/* 230 */       return this.magazineColumns;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void setMagazineColumns(String[] columns)
/*     */     {
/* 237 */       this.magazineColumns = columns;
/*     */     }
/*     */     
/*     */     public String getSortOrder() {
/* 241 */       return this.sortAscending ? SORT_ORDER.ascending.name() : SORT_ORDER.descending.name();
/*     */     }
/*     */     
/*     */     public int getSortByIndex() {
/* 245 */       if (this.columns == null) {
/* 246 */         return -1;
/*     */       }
/* 248 */       int index = -1;
/* 249 */       for (int i = 0; i < this.columns.length; i++) {
/* 250 */         if ((this.sortBy != null) && (this.sortBy.equals(this.columns[i]))) {
/* 251 */           index = i;
/* 252 */           break;
/*     */         }
/*     */       }
/*     */       
/* 256 */       return index;
/*     */     }
/*     */     
/*     */     public JSONObject toJSON() {
/* 260 */       JSONObject json = new JSONObject();
/* 261 */       if (this.sortBy != null)
/* 262 */         json.put("sortBy", this.sortBy);
/* 263 */       json.put("sortAsc", Boolean.valueOf(this.sortAscending));
/* 264 */       if ((this.columns != null) && (this.columns.length > 0)) {
/* 265 */         for (String col : this.columns)
/* 266 */           JSONUtils.accumulate(json, "columns", col);
/*     */       }
/* 268 */       if ((this.magazineColumns != null) && (this.magazineColumns.length > 0)) {
/* 269 */         for (String col : this.magazineColumns)
/* 270 */           JSONUtils.accumulate(json, "magazineColumns", col);
/*     */       }
/* 272 */       return json;
/*     */     }
/*     */     
/*     */     public void fromJSON(JSONObject json)
/*     */     {
/* 277 */       setSortByProperty((String)json.get("sortBy"));
/* 278 */       Boolean sortAsc = (Boolean)json.get("sortAsc");
/* 279 */       setSortAscending(sortAsc == null ? true : sortAsc.booleanValue());
/* 280 */       JSONArray columnsJSON = (JSONArray)json.get("columns");
/* 281 */       if (columnsJSON != null) {
/* 282 */         this.columns = new String[columnsJSON.size()];
/* 283 */         columnsJSON.toArray(this.columns);
/*     */       }
/* 285 */       JSONArray magazineColumnsJSON = (JSONArray)json.get("magazineColumns");
/* 286 */       if (magazineColumnsJSON != null) {
/* 287 */         this.magazineColumns = new String[magazineColumnsJSON.size()];
/* 288 */         magazineColumnsJSON.toArray(this.magazineColumns);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\SearchTemplateBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */