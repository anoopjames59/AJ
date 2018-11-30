/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.ier.plugin.search.p8.P8Util;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class SearchTemplate
/*     */   extends SearchTemplateBase
/*     */ {
/*  27 */   public static String THIS_TEAMSPACE_ID = "{this_teamspace_id}";
/*     */   private List<SearchFolder> folders;
/*     */   
/*  30 */   public static enum TextSearchType { cascade,  verity,  basic;
/*     */     
/*     */     private TextSearchType() {} }
/*     */   
/*  34 */   public static enum SearchEditProperty { editable,  hidden,  required,  readonly;
/*     */     
/*     */     private SearchEditProperty() {} }
/*     */   
/*  38 */   public static enum SearchFolderView { editable,  hidden;
/*     */     
/*     */     private SearchFolderView() {} }
/*     */   
/*     */   private List<SearchClass> classes;
/*  43 */   public SearchTemplate() { this.textSearchCriteria = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*  47 */     this.propertyTextAnded = true; }
/*     */   
/*     */   private List<TextSearchCriterion> textSearchCriteria;
/*     */   private TextSearchType textSearchType;
/*     */   private Macros macros;
/*     */   private SearchTemplateBase.VersionOption versionOption;
/*     */   private boolean propertyTextAnded;
/*     */   private boolean autoResolved;
/*     */   private boolean invalidRepository;
/*     */   
/*  57 */   public boolean isInvalidRepository() { return this.invalidRepository; }
/*     */   
/*     */   private boolean invalidFolder;
/*     */   
/*  61 */   public void setInvalidRepository(boolean invalidRepository) { this.invalidRepository = invalidRepository; }
/*     */   
/*     */   private boolean invalidClass;
/*     */   
/*  65 */   public boolean isInvalidFolder() { return this.invalidFolder; }
/*     */   
/*     */   private boolean invalidFileType;
/*     */   
/*  69 */   public void setInvalidFolder(boolean invalidFolder) { this.invalidFolder = invalidFolder; }
/*     */   
/*     */   private boolean invalidProperty;
/*     */   private boolean invalidTextSearch;
/*  73 */   public boolean isInvalidClass() { return this.invalidClass; }
/*     */   
/*     */ 
/*     */   public void setInvalidClass(boolean invalidClass) {
/*  77 */     this.invalidClass = invalidClass;
/*     */   }
/*     */   
/*     */   public boolean isInvalidFileType() {
/*  81 */     return this.invalidFileType;
/*     */   }
/*     */   
/*     */   public void setInvalidFileType(boolean invalidFileType) {
/*  85 */     this.invalidFileType = invalidFileType;
/*     */   }
/*     */   
/*     */   public boolean isInvalidProperty() {
/*  89 */     return this.invalidProperty;
/*     */   }
/*     */   
/*     */   public void setInvalidProperty(boolean invalidProperty) {
/*  93 */     this.invalidProperty = invalidProperty;
/*     */   }
/*     */   
/*     */   public boolean isInvalidTextSearch() {
/*  97 */     return this.invalidTextSearch;
/*     */   }
/*     */   
/*     */   public void setInvalidTextSearch(boolean invalidTextSearch) {
/* 101 */     this.invalidTextSearch = invalidTextSearch;
/*     */   }
/*     */   
/*     */   public void addFolder(SearchFolder folder) {
/* 105 */     if (this.folders == null) {
/* 106 */       this.folders = new ArrayList();
/*     */     }
/* 108 */     this.folders.add(folder);
/*     */   }
/*     */   
/*     */   public List<SearchFolder> getFolders() {
/* 112 */     return this.folders;
/*     */   }
/*     */   
/*     */   public void addClass(SearchClass searchClass) {
/* 116 */     if (this.classes == null) {
/* 117 */       this.classes = new ArrayList();
/*     */     }
/* 119 */     this.classes.add(searchClass);
/*     */   }
/*     */   
/*     */   public List<SearchClass> getClasses() {
/* 123 */     return this.classes;
/*     */   }
/*     */   
/*     */   public String getFirstClassName() {
/* 127 */     return (this.classes != null) && (this.classes.size() > 0) ? ((SearchClass)this.classes.get(0)).getName() : null;
/*     */   }
/*     */   
/*     */   public String getFirstClassDisplayName() {
/* 131 */     return (this.classes != null) && (this.classes.size() > 0) ? ((SearchClass)this.classes.get(0)).getDisplayName() : null;
/*     */   }
/*     */   
/*     */   public boolean isFirstClassSearchSubclasses() {
/* 135 */     return (this.classes != null) && (this.classes.size() > 0) ? ((SearchClass)this.classes.get(0)).isSearchSubclasses() : false;
/*     */   }
/*     */   
/*     */   public void addChild(ChildSearchCriteria childCriterion) {
/* 139 */     getSearchCriteria().add(childCriterion);
/*     */   }
/*     */   
/*     */   public List<SearchCriterion> getPropertySearchCriteria() {
/* 143 */     List<SearchCriteria> criteria = getSearchCriteria();
/* 144 */     List<SearchCriterion> textCriteria = new ArrayList();
/* 145 */     if ((criteria != null) && (criteria.size() > 0)) {
/* 146 */       for (SearchCriteria criterion : criteria) {
/* 147 */         if ((criterion instanceof SearchCriterion))
/*     */         {
/* 149 */           SearchCriterion sc = (SearchCriterion)criterion;
/* 150 */           if (!sc.getOperator().equals("CONTAINS"))
/* 151 */             textCriteria.add(sc);
/*     */         }
/*     */       }
/*     */     }
/* 155 */     return textCriteria;
/*     */   }
/*     */   
/*     */   public List<SearchCriterion> getPropertyTextSearchCriteria() {
/* 159 */     List<SearchCriteria> criteria = getSearchCriteria();
/* 160 */     List<SearchCriterion> textCriteria = new ArrayList();
/* 161 */     if ((criteria != null) && (criteria.size() > 0)) {
/* 162 */       for (SearchCriteria criterion : criteria) {
/* 163 */         if ((criterion instanceof SearchCriterion))
/*     */         {
/* 165 */           SearchCriterion sc = (SearchCriterion)criterion;
/* 166 */           if (sc.getOperator().equals("CONTAINS"))
/* 167 */             textCriteria.add(sc);
/*     */         }
/*     */       }
/*     */     }
/* 171 */     return textCriteria;
/*     */   }
/*     */   
/*     */   public void addTextSearchCriterion(TextSearchCriterion textSearchCriteria) {
/* 175 */     this.textSearchCriteria.add(textSearchCriteria);
/*     */   }
/*     */   
/*     */   public void removeTextSearchCriterion(TextSearchCriterion textSearchCriteria) {
/* 179 */     this.textSearchCriteria.remove(textSearchCriteria);
/*     */   }
/*     */   
/*     */   public List<TextSearchCriterion> getTextSearchCriteria() {
/* 183 */     return this.textSearchCriteria;
/*     */   }
/*     */   
/*     */   public TextSearchCriterion getFirstTextSearchCriterion() {
/* 187 */     return this.textSearchCriteria.size() > 0 ? (TextSearchCriterion)this.textSearchCriteria.get(0) : null;
/*     */   }
/*     */   
/*     */   public TextSearchType getTextSearchType() {
/* 191 */     return this.textSearchType;
/*     */   }
/*     */   
/*     */   public void setTextSearchType(TextSearchType textSearchType) {
/* 195 */     this.textSearchType = textSearchType;
/*     */   }
/*     */   
/*     */   public Macros getMacros() {
/* 199 */     return this.macros;
/*     */   }
/*     */   
/*     */   public void setMacros(Macros macros) {
/* 203 */     this.macros = macros;
/*     */   }
/*     */   
/*     */   public SearchTemplateBase.VersionOption getVersionOption() {
/* 207 */     return this.versionOption;
/*     */   }
/*     */   
/*     */   public void setVersionOption(SearchTemplateBase.VersionOption versionOption) {
/* 211 */     this.versionOption = versionOption;
/*     */   }
/*     */   
/*     */   public boolean isPropertyTextAnded() {
/* 215 */     return this.propertyTextAnded;
/*     */   }
/*     */   
/*     */   public void setPropertyTextAnded(boolean propertyTextAnded) {
/* 219 */     this.propertyTextAnded = propertyTextAnded;
/*     */   }
/*     */   
/*     */   public boolean isAutoResolved() {
/* 223 */     return this.autoResolved;
/*     */   }
/*     */   
/*     */   public void setAutoResolved(boolean autoResolved) {
/* 227 */     this.autoResolved = autoResolved;
/*     */   }
/*     */   
/*     */   public void fromJSON(JSONObject searchTemplateJson)
/*     */   {
/* 232 */     super.fromJSON(searchTemplateJson);
/*     */     
/* 234 */     JSONArray foldersJSON = (JSONArray)searchTemplateJson.get("search_folders");
/* 235 */     if ((foldersJSON != null) && (foldersJSON.size() > 0)) {
/* 236 */       Iterator<JSONObject> i = foldersJSON.iterator();
/* 237 */       while (i.hasNext()) {
/* 238 */         JSONObject folderJSON = (JSONObject)i.next();
/* 239 */         String id = P8Util.getObjectIdentity((String)folderJSON.get("id"));
/* 240 */         String folderView = (String)folderJSON.get("view");
/* 241 */         SearchFolder folder = new SearchFolder(id, (String)folderJSON.get("pathName"), ((Boolean)folderJSON.get("searchSubfolders")).booleanValue(), folderView == null ? null : SearchFolderView.valueOf(folderView), (String)folderJSON.get("objectStoreId"), null, (String)folderJSON.get("itemId"));
/* 242 */         addFolder(folder);
/*     */       }
/*     */     }
/*     */     
/* 246 */     JSONArray classesJSON = (JSONArray)searchTemplateJson.get("search_classes");
/* 247 */     if ((classesJSON != null) && (classesJSON.size() > 0)) {
/* 248 */       Iterator<JSONObject> i = classesJSON.iterator();
/* 249 */       while (i.hasNext()) {
/* 250 */         JSONObject classJSON = (JSONObject)i.next();
/* 251 */         String className = (String)classJSON.get("name");
/* 252 */         String displayName = (String)classJSON.get("displayName");
/* 253 */         Object classTypeParam = classJSON.get("objectType");
/* 254 */         SearchTemplateBase.ObjectType classType = (classTypeParam != null) && (((String)classTypeParam).length() > 0) ? SearchTemplateBase.ObjectType.valueOf((String)classTypeParam) : null;
/* 255 */         Object subClassesParam = classJSON.get("searchSubclasses");
/* 256 */         boolean subClasses = subClassesParam == null ? false : ((Boolean)subClassesParam).booleanValue();
/* 257 */         Object editPropertyParam = classJSON.get("editProperty");
/* 258 */         SearchEditProperty editProperty = (editPropertyParam != null) && (((String)editPropertyParam).length() > 0) ? SearchEditProperty.valueOf((String)editPropertyParam) : SearchEditProperty.editable;
/* 259 */         String itemId = (String)classJSON.get("itemId");
/* 260 */         SearchClass searchClass = new SearchClass(className, displayName, classType, subClasses, editProperty, itemId);
/* 261 */         addClass(searchClass);
/*     */       }
/*     */     } else {
/* 264 */       String className = (String)searchTemplateJson.get("template_name");
/* 265 */       if ((className != null) && (!className.isEmpty())) {
/* 266 */         String displayName = (String)searchTemplateJson.get("template_label");
/* 267 */         Object subClassesParam = searchTemplateJson.get("searchSubclasses");
/* 268 */         boolean subClasses = subClassesParam == null ? false : ((Boolean)subClassesParam).booleanValue();
/* 269 */         SearchClass searchClass = new SearchClass(className, displayName, getObjectType(), subClasses, SearchEditProperty.editable, null);
/* 270 */         addClass(searchClass);
/*     */       }
/*     */     }
/*     */     
/* 274 */     JSONObject moreOptionsJson = (JSONObject)searchTemplateJson.get("moreOptions");
/* 275 */     if (moreOptionsJson != null) {
/* 276 */       String versionOption = (String)moreOptionsJson.get("versionOption");
/* 277 */       if ((versionOption != null) && (versionOption.length() > 0)) {
/* 278 */         setVersionOption(SearchTemplateBase.VersionOption.valueOf(versionOption));
/*     */       }
/* 280 */       Boolean propertyTextAnded = (Boolean)moreOptionsJson.get("propertyTextAnded");
/* 281 */       if (propertyTextAnded != null) {
/* 282 */         setPropertyTextAnded(propertyTextAnded.booleanValue());
/*     */       }
/* 284 */       JSONObject macrosJson = (JSONObject)moreOptionsJson.get("macros");
/* 285 */       if (macrosJson != null) {
/* 286 */         Macros macros = new Macros();
/* 287 */         macros.fromJSON(macrosJson);
/* 288 */         setMacros(macros);
/*     */       }
/*     */     }
/*     */     
/* 292 */     JSONArray searchTextCriteriaJSON = (JSONArray)searchTemplateJson.get("search_text_criteria");
/* 293 */     JSONObject textSearchCriteriaJSON = (JSONObject)searchTemplateJson.get("textSearchCriteria");
/* 294 */     if (searchTextCriteriaJSON != null) {
/* 295 */       for (int i = 0; i < searchTextCriteriaJSON.size(); i++) {
/* 296 */         JSONObject searchCriterionJson = (JSONObject)searchTextCriteriaJSON.get(i);
/* 297 */         TextSearchCriterion textSearchCriteria = new TextSearchCriterion();
/* 298 */         textSearchCriteria.fromJSON(searchCriterionJson);
/* 299 */         addTextSearchCriterion(textSearchCriteria);
/*     */       }
/* 301 */     } else if (textSearchCriteriaJSON != null) {
/* 302 */       TextSearchCriterion textSearchCriteria = new TextSearchCriterion();
/* 303 */       textSearchCriteria.fromJSON(textSearchCriteriaJSON);
/* 304 */       addTextSearchCriterion(textSearchCriteria);
/*     */     }
/* 306 */     String textSearchType = (String)searchTemplateJson.get("textSearchType");
/* 307 */     if ((textSearchType != null) && (textSearchType.length() > 0)) {
/* 308 */       setTextSearchType(TextSearchType.valueOf(textSearchType));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SearchFolder {
/*     */     private String id;
/*     */     private String pathName;
/*     */     private boolean searchSubfolders;
/*     */     private SearchTemplate.SearchFolderView view;
/*     */     private String objectStoreId;
/*     */     private String objectStoreName;
/*     */     private String itemId;
/*     */     
/*     */     public SearchFolder() {}
/*     */     
/*     */     public SearchFolder(String id, String pathName, boolean searchSubfolders, SearchTemplate.SearchFolderView view, String objectStoreId, String objectStoreName, String itemId) {
/* 324 */       this.id = id;
/* 325 */       this.pathName = pathName;
/* 326 */       this.searchSubfolders = searchSubfolders;
/* 327 */       this.view = view;
/* 328 */       this.objectStoreId = objectStoreId;
/* 329 */       this.objectStoreName = objectStoreName;
/* 330 */       this.itemId = itemId;
/*     */     }
/*     */     
/*     */     public boolean isHidden() {
/* 334 */       return (this.view != null) && (this.view == SearchTemplate.SearchFolderView.hidden);
/*     */     }
/*     */     
/*     */     public String getId() {
/* 338 */       return this.id;
/*     */     }
/*     */     
/*     */     public void setId(String id) {
/* 342 */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getPathName() {
/* 346 */       return this.pathName;
/*     */     }
/*     */     
/*     */     public void setPathName(String pathName) {
/* 350 */       this.pathName = pathName;
/*     */     }
/*     */     
/*     */     public boolean isSearchSubfolders() {
/* 354 */       return this.searchSubfolders;
/*     */     }
/*     */     
/*     */     public void setSearchSubfolders(boolean searchSubfolders) {
/* 358 */       this.searchSubfolders = searchSubfolders;
/*     */     }
/*     */     
/*     */     public SearchTemplate.SearchFolderView getView() {
/* 362 */       return this.view;
/*     */     }
/*     */     
/*     */     public void setView(SearchTemplate.SearchFolderView view) {
/* 366 */       this.view = view;
/*     */     }
/*     */     
/*     */     public String getObjectStoreId() {
/* 370 */       return this.objectStoreId;
/*     */     }
/*     */     
/*     */     public void setObjectStoreId(String objectStoreId) {
/* 374 */       this.objectStoreId = objectStoreId;
/*     */     }
/*     */     
/*     */     public String getObjectStoreName() {
/* 378 */       return this.objectStoreName;
/*     */     }
/*     */     
/*     */     public void setObjectStoreName(String objectStoreName) {
/* 382 */       this.objectStoreName = objectStoreName;
/*     */     }
/*     */     
/*     */     public void setItemId(String itemId) {
/* 386 */       this.itemId = itemId;
/*     */     }
/*     */     
/*     */     public String getItemId() {
/* 390 */       return this.itemId;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SearchClass
/*     */   {
/*     */     private String name;
/*     */     private String displayName;
/*     */     private SearchTemplateBase.ObjectType objectType;
/*     */     private boolean searchSubclasses;
/*     */     private SearchTemplate.SearchEditProperty editProperty;
/*     */     private String itemId;
/*     */     
/*     */     public SearchClass() {}
/*     */     
/*     */     public SearchClass(String name, String displayName, SearchTemplateBase.ObjectType objectType, boolean searchSubclasses, SearchTemplate.SearchEditProperty editProperty, String itemId) {
/* 406 */       this.name = name;
/* 407 */       this.displayName = displayName;
/* 408 */       this.objectType = objectType;
/* 409 */       this.searchSubclasses = searchSubclasses;
/* 410 */       this.editProperty = editProperty;
/* 411 */       this.itemId = itemId;
/*     */     }
/*     */     
/*     */     public String getItemId() {
/* 415 */       return this.itemId;
/*     */     }
/*     */     
/*     */     public void setItemId(String itemId) {
/* 419 */       this.itemId = itemId;
/*     */     }
/*     */     
/*     */     public String getDisplayName() {
/* 423 */       return this.displayName;
/*     */     }
/*     */     
/*     */     public void setDisplayName(String displayName) {
/* 427 */       this.displayName = displayName;
/*     */     }
/*     */     
/*     */     public SearchTemplateBase.ObjectType getObjectType() {
/* 431 */       return this.objectType == null ? SearchTemplateBase.ObjectType.document : this.objectType;
/*     */     }
/*     */     
/*     */     public void setObjectType(String objectType) {
/* 435 */       this.objectType = ((objectType == null) || (objectType.length() < 1) ? SearchTemplateBase.ObjectType.document : SearchTemplateBase.ObjectType.valueOf(objectType));
/*     */     }
/*     */     
/*     */     public String getName() {
/* 439 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/* 443 */       this.name = name;
/*     */     }
/*     */     
/*     */     public SearchTemplate.SearchEditProperty getEditProperty() {
/* 447 */       return this.editProperty == null ? SearchTemplate.SearchEditProperty.editable : this.editProperty;
/*     */     }
/*     */     
/*     */     public void setEditProperty(String editProperty) {
/* 451 */       this.editProperty = ((editProperty == null) || (editProperty.length() < 1) ? SearchTemplate.SearchEditProperty.editable : SearchTemplate.SearchEditProperty.valueOf(editProperty));
/*     */     }
/*     */     
/*     */     public boolean isHidden() {
/* 455 */       return (this.editProperty != null) && (this.editProperty.equals(SearchTemplate.SearchEditProperty.hidden));
/*     */     }
/*     */     
/*     */     public boolean isReadOnly() {
/* 459 */       return (this.editProperty != null) && (this.editProperty.equals(SearchTemplate.SearchEditProperty.readonly));
/*     */     }
/*     */     
/*     */     public boolean isSearchSubclasses() {
/* 463 */       return this.searchSubclasses;
/*     */     }
/*     */     
/*     */     public void setSearchSubclasses(boolean searchSubclasses) {
/* 467 */       this.searchSubclasses = searchSubclasses;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TextSearchCriterion {
/*     */     private String text;
/*     */     private String operator;
/*     */     private int distance;
/*     */     SearchTemplate.SearchEditProperty editProperty;
/*     */     String itemId;
/*     */     
/*     */     public TextSearchCriterion() {
/* 479 */       this.text = "";
/*     */     }
/*     */     
/*     */     public TextSearchCriterion(String text, String operator, int distance) {
/* 483 */       this.text = (text == null ? "" : text);
/* 484 */       this.operator = operator;
/* 485 */       this.distance = distance;
/*     */     }
/*     */     
/*     */     public TextSearchCriterion(String text, String operator, int distance, SearchTemplate.SearchEditProperty editProperty, String itemId) {
/* 489 */       this.text = (text == null ? "" : text);
/* 490 */       this.operator = operator;
/* 491 */       this.distance = distance;
/* 492 */       this.editProperty = editProperty;
/* 493 */       this.itemId = itemId;
/*     */     }
/*     */     
/*     */     public String getText() {
/* 497 */       return this.text == null ? "" : this.text;
/*     */     }
/*     */     
/*     */     public void setText(String text) {
/* 501 */       this.text = (text == null ? "" : text);
/*     */     }
/*     */     
/*     */     public String getOperator() {
/* 505 */       return this.operator;
/*     */     }
/*     */     
/*     */     public void setOperator(String operator) {
/* 509 */       this.operator = operator;
/*     */     }
/*     */     
/*     */     public int getDistance() {
/* 513 */       return this.distance;
/*     */     }
/*     */     
/*     */     public void setDistance(int distance) {
/* 517 */       this.distance = distance;
/*     */     }
/*     */     
/*     */     public SearchTemplate.SearchEditProperty getEditProperty() {
/* 521 */       return this.editProperty == null ? SearchTemplate.SearchEditProperty.editable : this.editProperty;
/*     */     }
/*     */     
/*     */     public void setEditProperty(SearchTemplate.SearchEditProperty editProperty) {
/* 525 */       this.editProperty = editProperty;
/*     */     }
/*     */     
/*     */     public String getItemId() {
/* 529 */       return this.itemId;
/*     */     }
/*     */     
/*     */     public void setItemId(String itemId) {
/* 533 */       this.itemId = itemId;
/*     */     }
/*     */     
/*     */     public JSONObject toJSON() {
/* 537 */       JSONObject json = new JSONObject();
/* 538 */       if (this.text != null)
/* 539 */         json.put("text", this.text);
/* 540 */       if (this.operator != null)
/* 541 */         json.put("operator", this.operator);
/* 542 */       json.put("proximityDistance", Integer.valueOf(this.distance));
/* 543 */       json.put("editProperty", getEditProperty().toString());
/* 544 */       json.put("itemId", this.itemId);
/* 545 */       return json;
/*     */     }
/*     */     
/*     */     public void fromJSON(JSONObject json) {
/* 549 */       Object text = json.get("text");
/* 550 */       if (text != null)
/* 551 */         setText((String)text);
/* 552 */       setOperator((String)json.get("operator"));
/* 553 */       Long distance = (Long)json.get("proximityDistance");
/* 554 */       setDistance(distance == null ? 0 : distance.intValue());
/* 555 */       Object editProp = json.get("editProperty");
/* 556 */       if ((editProp != null) && (((String)editProp).length() > 0))
/* 557 */         setEditProperty(SearchTemplate.SearchEditProperty.valueOf((String)editProp));
/* 558 */       Object itemId = json.get("itemId");
/* 559 */       if ((itemId != null) && (((String)itemId).length() > 0))
/* 560 */         setItemId((String)itemId);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Macros {
/*     */     private String[] fileTypes;
/*     */     private SearchTemplate.UserAction[] userActions;
/*     */     
/*     */     public String[] getFileTypes() {
/* 569 */       return this.fileTypes;
/*     */     }
/*     */     
/*     */     public void setFileTypes(String[] fileTypes) {
/* 573 */       this.fileTypes = fileTypes;
/*     */     }
/*     */     
/*     */     public SearchTemplate.UserAction[] getUserActions() {
/* 577 */       return this.userActions;
/*     */     }
/*     */     
/*     */     public void setUserActions(SearchTemplate.UserAction[] userActions) {
/* 581 */       this.userActions = userActions;
/*     */     }
/*     */     
/*     */     public void fromJSON(JSONObject json) {
/* 585 */       setFileTypes(SearchTemplate.getJsonArrayValues(json, "fileTypes"));
/* 586 */       JSONArray actions = (JSONArray)json.get("userActions");
/* 587 */       if (actions != null) {
/* 588 */         SearchTemplate.UserAction[] userActions = new SearchTemplate.UserAction[actions.size()];
/* 589 */         for (int i = 0; i < actions.size(); i++) {
/* 590 */           userActions[i] = new SearchTemplate.UserAction();
/* 591 */           userActions[i].fromJSON((JSONObject)actions.get(i));
/*     */         }
/* 593 */         setUserActions(userActions);
/*     */       }
/*     */     }
/*     */     
/*     */     public JSONObject toJSON() {
/* 598 */       JSONObject json = new JSONObject();
/* 599 */       if ((this.fileTypes != null) && (this.fileTypes.length > 0)) {
/* 600 */         for (String type : this.fileTypes)
/* 601 */           JSONUtils.accumulate(json, "fileTypes", type);
/*     */       }
/* 603 */       if ((this.userActions != null) && (this.userActions.length > 0)) {
/* 604 */         for (SearchTemplate.UserAction action : this.userActions) {
/* 605 */           JSONUtils.accumulate(json, "userActions", action.toJSON());
/*     */         }
/*     */       }
/* 608 */       return json;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UserAction {
/*     */     private String action;
/*     */     private String[] users;
/*     */     private String[] dates;
/*     */     private String dateOperator;
/*     */     private String[] userDisplayNames;
/*     */     
/*     */     public String getAction() {
/* 620 */       return this.action;
/*     */     }
/*     */     
/*     */     public void setAction(String action) {
/* 624 */       this.action = action;
/*     */     }
/*     */     
/*     */     public String[] getUsers() {
/* 628 */       return this.users;
/*     */     }
/*     */     
/*     */     public void setUsers(String[] users) {
/* 632 */       this.users = users;
/*     */     }
/*     */     
/*     */     public String[] getDates() {
/* 636 */       return this.dates;
/*     */     }
/*     */     
/*     */     public void setDates(String[] dates) {
/* 640 */       this.dates = dates;
/*     */     }
/*     */     
/*     */     public String getDateOperator() {
/* 644 */       return this.dateOperator;
/*     */     }
/*     */     
/*     */     public void setDateOperator(String dateOperator) {
/* 648 */       this.dateOperator = dateOperator;
/*     */     }
/*     */     
/*     */     public String[] getUserDisplayNames() {
/* 652 */       return this.userDisplayNames;
/*     */     }
/*     */     
/*     */     public void setUserDisplayNames(String[] userDisplayNames) {
/* 656 */       this.userDisplayNames = userDisplayNames;
/*     */     }
/*     */     
/*     */     public void fromJSON(JSONObject json) {
/* 660 */       setAction((String)json.get("action"));
/* 661 */       setUsers(SearchTemplate.getJsonArrayValues(json, "users"));
/* 662 */       setDates(SearchTemplate.getJsonArrayValues(json, "dates"));
/* 663 */       setDateOperator((String)json.get("dateOperator"));
/*     */     }
/*     */     
/*     */     public JSONObject toJSON() {
/* 667 */       JSONObject json = new JSONObject();
/* 668 */       json.put("action", this.action);
/* 669 */       json.put("dateOperator", this.dateOperator);
/* 670 */       if ((this.users != null) && (this.users.length > 0)) {
/* 671 */         for (String user : this.users)
/* 672 */           JSONUtils.accumulate(json, "users", user);
/*     */       }
/* 674 */       if ((this.dates != null) && (this.dates.length > 0)) {
/* 675 */         for (String date : this.dates)
/* 676 */           JSONUtils.accumulate(json, "dates", date);
/*     */       }
/* 678 */       if ((this.userDisplayNames != null) && (this.userDisplayNames.length > 0)) {
/* 679 */         for (String userDisplayName : this.userDisplayNames) {
/* 680 */           JSONUtils.accumulate(json, "userDisplayNames", userDisplayName);
/*     */         }
/*     */       }
/* 683 */       return json;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String[] getJsonArrayValues(JSONObject json, String key)
/*     */   {
/* 689 */     if (json == null) {
/* 690 */       return null;
/*     */     }
/* 692 */     Object jsonValue = json.get(key);
/* 693 */     int size = (jsonValue instanceof JSONArray) ? ((JSONArray)jsonValue).size() : 1;
/* 694 */     String[] values = new String[size];
/* 695 */     if ((jsonValue instanceof JSONArray)) {
/* 696 */       ((JSONArray)jsonValue).toArray(values);
/* 697 */     } else if ((jsonValue instanceof String)) {
/* 698 */       values[0] = ((String)jsonValue);
/*     */     } else {
/* 700 */       values = null;
/*     */     }
/* 702 */     return values;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\SearchTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */