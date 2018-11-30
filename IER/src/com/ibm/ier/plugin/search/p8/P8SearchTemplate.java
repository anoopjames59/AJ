/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.ibm.ier.plugin.search.util.JSONUtils;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.Macros;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchType;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8SearchTemplate
/*     */   extends SearchTemplate
/*     */ {
/*     */   private String productName;
/*     */   private List<P8SearchDefinition.SearchInObjectStore> objectStores;
/*     */   private CriteriaLayout textSearchCriteriaLayout;
/*     */   private boolean operatorHidden;
/*     */   private boolean criteriaRelationshipHidden;
/*     */   public P8SearchTemplate() {}
/*     */   
/*     */   public static enum EnumTextSearchOption
/*     */   {
/*  28 */     content,  property;
/*     */     
/*     */ 
/*     */ 
/*     */     private EnumTextSearchOption() {}
/*     */   }
/*     */   
/*     */ 
/*  36 */   private P8SearchDefinition searchDefinition = null;
/*     */   
/*     */   private EnumTextSearchOption textSearchOption;
/*     */   
/*     */ 
/*     */   public P8SearchTemplate(P8SearchDefinition searchDefinition)
/*     */   {
/*  43 */     this.searchDefinition = searchDefinition;
/*     */   }
/*     */   
/*     */   public String getProductName() {
/*  47 */     return this.productName;
/*     */   }
/*     */   
/*     */   public void setProductName(String productName) {
/*  51 */     this.productName = productName;
/*     */   }
/*     */   
/*     */   public void addObjectStore(P8SearchDefinition.SearchInObjectStore objectStore) {
/*  55 */     if (this.objectStores == null) {
/*  56 */       this.objectStores = new ArrayList();
/*     */     }
/*  58 */     this.objectStores.add(objectStore);
/*     */   }
/*     */   
/*     */   public List<P8SearchDefinition.SearchInObjectStore> getObjectStores() {
/*  62 */     return this.objectStores;
/*     */   }
/*     */   
/*     */   public CriteriaLayout getTextSearchCriteriaLayout() {
/*  66 */     return this.textSearchCriteriaLayout;
/*     */   }
/*     */   
/*     */   public void setTextSearchCriteriaLayout(CriteriaLayout textSearchCriteriaLayout) {
/*  70 */     this.textSearchCriteriaLayout = textSearchCriteriaLayout;
/*     */   }
/*     */   
/*     */   public boolean isOperatorHidden() {
/*  74 */     return this.operatorHidden;
/*     */   }
/*     */   
/*     */   public void setOperatorHidden(boolean operatorHidden) {
/*  78 */     this.operatorHidden = operatorHidden;
/*     */   }
/*     */   
/*     */   public boolean isCriteriaRelationshipHidden() {
/*  82 */     return this.criteriaRelationshipHidden;
/*     */   }
/*     */   
/*     */   public void setCriteriaRelationshipHidden(boolean criteriaRelationshipHidden) {
/*  86 */     this.criteriaRelationshipHidden = criteriaRelationshipHidden;
/*     */   }
/*     */   
/*     */   public boolean isCascade() {
/*  90 */     SearchTemplate.TextSearchType type = getTextSearchType();
/*     */     
/*  92 */     return (type == null) || (type == SearchTemplate.TextSearchType.cascade);
/*     */   }
/*     */   
/*     */   public P8SearchDefinition getSearchDefinition() {
/*  96 */     return this.searchDefinition;
/*     */   }
/*     */   
/*     */   void setSearchDefinition(P8SearchDefinition searchDefinition) {
/* 100 */     this.searchDefinition = searchDefinition;
/*     */   }
/*     */   
/*     */   public EnumTextSearchOption getTextSearchOption()
/*     */   {
/* 105 */     return this.textSearchOption;
/*     */   }
/*     */   
/*     */   public void setTextSearchOption(EnumTextSearchOption op)
/*     */   {
/* 110 */     this.textSearchOption = op;
/*     */   }
/*     */   
/*     */   public void fromJSON(JSONObject searchTemplateJson) {
/* 114 */     super.fromJSON(searchTemplateJson);
/*     */     
/* 116 */     JSONArray objectStoresJson = (JSONArray)searchTemplateJson.get("search_objectstores");
/* 117 */     if ((objectStoresJson != null) && (objectStoresJson.size() > 0)) {
/* 118 */       Iterator<JSONObject> i = objectStoresJson.iterator();
/* 119 */       while (i.hasNext()) {
/* 120 */         JSONObject osJson = (JSONObject)i.next();
/* 121 */         String id = (String)osJson.get("id");
/* 122 */         String symbolicName = (String)osJson.get("name");
/* 123 */         String displayName = (String)osJson.get("displayName");
/* 124 */         P8SearchDefinition.SearchInObjectStore os = new P8SearchDefinition.SearchInObjectStore(id, displayName);
/* 125 */         os.setSymbolicName(symbolicName);
/* 126 */         addObjectStore(os);
/*     */       }
/*     */     }
/*     */     
/* 130 */     String textSearchOption = (String)searchTemplateJson.get("textSearchOption");
/* 131 */     if ((textSearchOption != null) && (textSearchOption.length() > 0))
/*     */     {
/* 133 */       setTextSearchOption(EnumTextSearchOption.valueOf(textSearchOption));
/* 134 */       IERMacros macros = new IERMacros();
/* 135 */       macros.setTextSearchOption(textSearchOption);
/* 136 */       setMacros(macros);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IERMacros extends SearchTemplate.Macros
/*     */   {
/*     */     private String textSearchOption;
/*     */     
/*     */     public String getTextSearchOption()
/*     */     {
/* 146 */       return this.textSearchOption;
/*     */     }
/*     */     
/*     */     public void setTextSearchOption(String op) {
/* 150 */       this.textSearchOption = op;
/*     */     }
/*     */     
/*     */     public void fromJSON(JSONObject json) {
/* 154 */       super.fromJSON(json);
/* 155 */       String searchOp = (String)json.get("textSearchOption");
/* 156 */       setTextSearchOption(searchOp);
/*     */     }
/*     */     
/*     */     public JSONObject toJSON() {
/* 160 */       JSONObject obj = super.toJSON();
/* 161 */       if ((obj != null) && (this.textSearchOption != null) && (this.textSearchOption.length() > 0)) {
/* 162 */         obj.put("textSearchOption", this.textSearchOption);
/*     */       }
/* 164 */       return obj;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CriteriaLayout
/*     */   {
/*     */     private String join;
/*     */     private List<String> itemIds;
/*     */     private List<CriteriaLayout> childLayouts;
/*     */     
/*     */     CriteriaLayout(String join) {
/* 175 */       this.join = join;
/*     */     }
/*     */     
/*     */     public String getJoin() {
/* 179 */       return this.join;
/*     */     }
/*     */     
/*     */     public void setJoin(String join) {
/* 183 */       this.join = join;
/*     */     }
/*     */     
/*     */     public List<String> getItemIds() {
/* 187 */       return this.itemIds;
/*     */     }
/*     */     
/*     */     public void setItemIds(List<String> itemIds) {
/* 191 */       this.itemIds = itemIds;
/*     */     }
/*     */     
/*     */     public void addItemId(String itemId) {
/* 195 */       if (this.itemIds == null)
/* 196 */         this.itemIds = new ArrayList();
/* 197 */       this.itemIds.add(itemId);
/*     */     }
/*     */     
/*     */     public void removeItemId(String itemId) {
/* 201 */       if (this.itemIds != null)
/* 202 */         this.itemIds.remove(itemId);
/*     */     }
/*     */     
/*     */     public int getItemIdsSize() {
/* 206 */       if (this.itemIds == null)
/* 207 */         return 0;
/* 208 */       return this.itemIds.size();
/*     */     }
/*     */     
/*     */     public List<CriteriaLayout> getChildLayouts() {
/* 212 */       return this.childLayouts;
/*     */     }
/*     */     
/*     */     public void setChildLayouts(List<CriteriaLayout> childLayouts) {
/* 216 */       this.childLayouts = childLayouts;
/*     */     }
/*     */     
/*     */     public void addChildLayout(CriteriaLayout childLayout) {
/* 220 */       if (this.childLayouts == null)
/* 221 */         this.childLayouts = new ArrayList();
/* 222 */       this.childLayouts.add(childLayout);
/*     */     }
/*     */     
/*     */     public void removeChildLayout(CriteriaLayout childLayout) {
/* 226 */       if (this.childLayouts != null)
/* 227 */         this.childLayouts.remove(childLayout);
/*     */     }
/*     */     
/*     */     public int getChildLayoutsSize() {
/* 231 */       if (this.childLayouts == null)
/* 232 */         return 0;
/* 233 */       return this.childLayouts.size();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 237 */       boolean empty = true;
/* 238 */       if ((this.itemIds != null) && (this.itemIds.size() > 0))
/* 239 */         empty = false;
/* 240 */       if ((this.childLayouts != null) && (this.childLayouts.size() > 0)) {
/* 241 */         empty = false;
/*     */       }
/* 243 */       return empty;
/*     */     }
/*     */     
/*     */     public JSONObject toJSON() {
/* 247 */       JSONObject json = new JSONObject();
/* 248 */       json.put("criteriaLayoutJoin", (this.join == null) || (this.join.length() < 1) ? P8SearchDefinition.Operator.and.toString() : this.join);
/* 249 */       if ((this.itemIds != null) && (this.itemIds.size() > 0)) {
/* 250 */         for (String itemId : this.itemIds)
/* 251 */           JSONUtils.accumulate(json, "criteriaLayoutItemIds", itemId);
/*     */       }
/* 253 */       if ((this.childLayouts != null) && (this.childLayouts.size() > 0)) {
/* 254 */         for (CriteriaLayout layout : this.childLayouts) {
/* 255 */           JSONUtils.accumulate(json, "criteriaLayoutChildLayouts", layout.toJSON());
/*     */         }
/*     */       }
/* 258 */       return json;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8SearchTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */