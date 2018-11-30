/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.collection.EngineSet;
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.constants.GroupAction;
/*     */ import com.filenet.api.constants.RequiredState;
/*     */ import com.filenet.api.constants.SearchModifier;
/*     */ import com.filenet.api.constants.SortOrder;
/*     */ import com.filenet.api.constants.VersionSelection;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Factory.PageIterator;
/*     */ import com.filenet.api.core.Factory.StoredSearch;
/*     */ import com.filenet.api.core.IndependentlyPersistableObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.query.SearchTemplateContent;
/*     */ import com.filenet.api.query.SearchTemplateContentItem;
/*     */ import com.filenet.api.query.SearchTemplateParameters;
/*     */ import com.filenet.api.query.SearchTemplateSelectProperty;
/*     */ import com.filenet.api.query.SearchTemplateSubclass;
/*     */ import com.filenet.api.query.SearchTemplateWhereProperty;
/*     */ import com.filenet.api.query.StoredSearch;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
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
/*     */ final class P8CeSearchUtil
/*     */ {
/*     */   public static final String SELECT_RANK_COLUMN = "CS.Rank";
/*  62 */   private static final String[] AUGMENT_DOCUMENT_SELECT_PROPERTIES = { "ClassDescription", "IsCurrentVersion", "IsReserved", "MimeType", "VersionStatus", "MajorVersionNumber", "MinorVersionNumber", "VersionSeries", "Id", "CompoundDocumentState", "Reservation", "ReservationType", "IsVersioningEnabled", "ReplicationGroup" };
/*  63 */   private static final String[] AUGMENT_FOLDER_SELECT_PROPERTIES = { "ClassDescription", "Id", "ContainerType" };
/*     */   
/*     */ 
/*     */ 
/*     */   static PageIterator resumePageIterator(P8Connection connection, byte[] checkpoint)
/*     */   {
/*  69 */     return Factory.PageIterator.resumeInstance(connection.getCEConnection(), checkpoint);
/*     */   }
/*     */   
/*     */   static byte[] getNextPageCheckpoint(PageIterator pageIterator) {
/*  73 */     return pageIterator == null ? null : pageIterator.getNextPageCheckpoint();
/*     */   }
/*     */   
/*     */   static IndependentlyPersistableObject fetchStoredSearch(ObjectStore objectStore, Id searchId) {
/*  77 */     return Factory.StoredSearch.fetchInstance(objectStore, searchId, null);
/*     */   }
/*     */   
/*     */   static Document getStoredSearch(ObjectStore objectStore, String storedSearchId) {
/*  81 */     P8DocID docId = new P8DocID(storedSearchId);
/*     */     
/*  83 */     return (Document)objectStore.getObject("StoredSearch", new Id(docId.getObjectID()));
/*     */   }
/*     */   
/*     */   static EngineSet executeStoredSearch(SearchScope searchScope, Document storedSearch, String fromClass, Object searchParameters, int pageSize, PropertyFilter propertyFilter, boolean orderByRank) {
/*     */     EngineSet results;
/*     */     try { EngineSet results;
/*  89 */       if (orderByRank) {
/*  90 */         results = searchScope.fetchRows((StoredSearch)storedSearch, fromClass, (SearchTemplateParameters)searchParameters, Integer.valueOf(pageSize), propertyFilter, Boolean.TRUE);
/*     */       } else
/*  92 */         results = searchScope.fetchObjects((StoredSearch)storedSearch, fromClass, (SearchTemplateParameters)searchParameters, Integer.valueOf(pageSize), propertyFilter, Boolean.TRUE);
/*     */     } catch (Exception e) {
/*  94 */       if ((e instanceof InvocationTargetException)) {
/*  95 */         Throwable cause = e.getCause();
/*  96 */         if ((cause instanceof EngineRuntimeException))
/*  97 */           throw ((EngineRuntimeException)cause);
/*     */       }
/*  99 */       throw new RuntimeException("Failed to invoke fetchRows method", e);
/*     */     }
/*     */     
/* 102 */     return results;
/*     */   }
/*     */   
/*     */   static boolean isContentSearchInvalidException(EngineRuntimeException exception) {
/* 106 */     return exception.getExceptionCode() == ExceptionCode.CBR_INVALID_SEARCH;
/*     */   }
/*     */   
/*     */   static Object convertToCeSearchParameters(P8Connection connection, P8SearchDefinition.SearchClause searchClause, String sortProp, boolean sortDescending, boolean selectOnly) throws Exception {
/* 110 */     SearchTemplateParameters searchParameters = new SearchTemplateParameters();
/* 111 */     P8SearchDefinition definition = searchClause.getSearchDefinition();
/* 112 */     List<P8SearchDefinition.SelectProperty> searchSelectProps = searchClause.getSelectProperties();
/* 113 */     boolean showRank = (searchClause.isContentCriteriaDefined()) && (searchClause.getContentCriteria().isShowRank());
/* 114 */     List<SearchTemplateSelectProperty> ceSelectProps = convertSelectClause(searchSelectProps, sortProp, sortDescending, showRank);
/* 115 */     if (ceSelectProps != null)
/* 116 */       searchParameters.setSelectProperties(ceSelectProps);
/* 117 */     SearchTemplateBase.ObjectType objectType = searchClause.getFrom();
/* 118 */     if (objectType == SearchTemplateBase.ObjectType.document) {
/* 119 */       searchParameters.setAugmentedSelectList(Arrays.asList(AUGMENT_DOCUMENT_SELECT_PROPERTIES));
/* 120 */     } else if (objectType == SearchTemplateBase.ObjectType.folder) {
/* 121 */       searchParameters.setAugmentedSelectList(Arrays.asList(AUGMENT_FOLDER_SELECT_PROPERTIES));
/*     */     }
/* 123 */     if (selectOnly) {
/* 124 */       return searchParameters;
/*     */     }
/* 126 */     List<SearchTemplateWhereProperty> whereProps = convertWhereClause(searchClause.retrieveWhereClauseConditions());
/* 127 */     if (whereProps != null)
/* 128 */       searchParameters.setWhereProperties(whereProps);
/* 129 */     P8SearchDefinition.ContentCriteria contentCriteria = searchClause.getContentCriteria();
/* 130 */     if (contentCriteria != null) {
/* 131 */       SearchTemplateContent content = convertContentClause(searchClause.retrieveContentClauseItems(), contentCriteria.isCascade());
/* 132 */       if (content != null) {
/* 133 */         content.setRank(Boolean.valueOf(searchClause.getContentCriteria().isShowRank()));
/* 134 */         content.setSummary(Boolean.valueOf(searchClause.getContentCriteria().isShowContentSummary()));
/* 135 */         searchParameters.setContent(content);
/*     */       }
/*     */     }
/* 138 */     List<SearchTemplateSubclass> subclasses = convertSubclasses(searchClause.getSubclasses());
/* 139 */     if (subclasses != null)
/* 140 */       searchParameters.setSubclasses(subclasses);
/* 141 */     searchParameters.setVersionSelection(definition.isVersionSelectionCurrent() ? VersionSelection.CURRENT_VERSION : definition.isVersionSelectionReleased() ? VersionSelection.RELEASED_VERSION : definition.isVersionSelectionAll() ? VersionSelection.ALL_VERSIONS : null);
/*     */     
/* 143 */     return searchParameters;
/*     */   }
/*     */   
/*     */   static List<SearchTemplateSelectProperty> convertSelectClause(List<P8SearchDefinition.SelectProperty> searchSelectProps, String sortProp, boolean sortDescending, boolean showRank) throws Exception {
/* 147 */     boolean selectAll = (searchSelectProps.size() == 1) && (searchSelectProps.get(0) == P8SearchDefinition.SELECT_PROPERTY_ALL);
/* 148 */     if (((searchSelectProps.size() < 1) || (selectAll)) && ((sortProp == null) || (sortProp.length() < 1))) {
/* 149 */       return null;
/*     */     }
/* 151 */     List<SearchTemplateSelectProperty> ceSelectProps = new ArrayList();
/* 152 */     boolean sorted = false;
/* 153 */     for (P8SearchDefinition.SelectProperty selectProp : searchSelectProps) {
/* 154 */       if (selectProp != P8SearchDefinition.SELECT_PROPERTY_ALL)
/*     */       {
/*     */ 
/* 157 */         String selectPropName = selectProp.getSymbolicName();
/* 158 */         SearchTemplateSelectProperty ceSelectProp = new SearchTemplateSelectProperty();
/* 159 */         ceSelectProp.setItemId(selectProp.getItemId());
/* 160 */         ceSelectProp.setSymbolicName(selectPropName);
/* 161 */         if (sortProp == null) {
/* 162 */           ceSelectProp.setSortLevel(Integer.valueOf(selectProp.getSortLevel()));
/* 163 */           ceSelectProp.setSortOrder(selectProp.isSortOrderDescending() ? SortOrder.DESCENDING : SortOrder.ASCENDING);
/* 164 */           sorted = (sorted) || (selectProp.getSortLevel() > 0);
/* 165 */         } else if (selectPropName.equalsIgnoreCase(sortProp)) {
/* 166 */           ceSelectProp.setSortLevel(Integer.valueOf(1));
/* 167 */           ceSelectProp.setSortOrder(sortDescending ? SortOrder.DESCENDING : SortOrder.ASCENDING);
/* 168 */           sorted = true;
/*     */         } else {
/* 170 */           ceSelectProp.setSortLevel(Integer.valueOf(0));
/* 171 */           ceSelectProp.setSortOrder(sortDescending ? SortOrder.DESCENDING : SortOrder.ASCENDING);
/*     */         }
/* 173 */         ceSelectProps.add(ceSelectProp);
/*     */       }
/*     */     }
/* 176 */     if ((!sorted) && (sortProp != null) && (sortProp.length() > 0) && (!sortProp.equals("Rank"))) {
/* 177 */       SearchTemplateSelectProperty newSortProp = new SearchTemplateSelectProperty();
/* 178 */       newSortProp.setItemId(UUID.randomUUID().toString());
/* 179 */       newSortProp.setSymbolicName(sortProp);
/* 180 */       newSortProp.setSortLevel(Integer.valueOf(1));
/* 181 */       newSortProp.setSortOrder(sortDescending ? SortOrder.DESCENDING : SortOrder.ASCENDING);
/* 182 */       ceSelectProps.add(newSortProp);
/* 183 */       sorted = true;
/*     */     }
/*     */     
/* 186 */     if (showRank) {
/* 187 */       SearchTemplateSelectProperty rankProp = new SearchTemplateSelectProperty();
/* 188 */       rankProp.setItemId(UUID.randomUUID().toString());
/* 189 */       rankProp.setSymbolicName("CS.Rank");
/* 190 */       rankProp.setSortLevel(Integer.valueOf(sorted ? 0 : 1));
/* 191 */       rankProp.setSortOrder(sortDescending ? SortOrder.DESCENDING : SortOrder.ASCENDING);
/* 192 */       ceSelectProps.add(rankProp);
/*     */     }
/*     */     
/* 195 */     return ceSelectProps;
/*     */   }
/*     */   
/*     */   static List<SearchTemplateWhereProperty> convertWhereClause(Map<String, P8SearchDefinition.ClauseItem> whereClause) throws Exception {
/* 199 */     if ((whereClause == null) || (whereClause.size() < 1)) {
/* 200 */       return null;
/*     */     }
/* 202 */     List<SearchTemplateWhereProperty> whereProps = new ArrayList();
/* 203 */     Set<String> itemIds = whereClause.keySet();
/* 204 */     for (String itemId : itemIds) {
/* 205 */       P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)whereClause.get(itemId);
/* 206 */       SearchTemplateWhereProperty whereProp = new SearchTemplateWhereProperty();
/* 207 */       whereProp.setItemId(itemId);
/* 208 */       String value = condition.getLiteral();
/* 209 */       boolean exclude = ((value == null) || (value.length() < 1)) && (!condition.isOperatorNullOrNotNull());
/* 210 */       if (exclude) {
/* 211 */         whereProp.setExcludeFromQuery(Boolean.valueOf(exclude));
/*     */       } else {
/* 213 */         if ((condition.getPropDataType() == P8SearchDefinition.DataType.typedate) && (value.length() > 19)) {
/* 214 */           value = value.substring(0, 10) + value.substring(19);
/*     */         }
/* 216 */         whereProp.setLiteral(value);
/*     */       }
/* 218 */       whereProps.add(whereProp);
/*     */     }
/*     */     
/* 221 */     return whereProps;
/*     */   }
/*     */   
/*     */   static SearchTemplateContent convertContentClause(Map<String, P8SearchDefinition.ClauseItem> contentClause, boolean cascade) throws Exception {
/* 225 */     if ((contentClause == null) || (contentClause.size() < 1)) {
/* 226 */       return null;
/*     */     }
/* 228 */     SearchTemplateContent content = new SearchTemplateContent();
/* 229 */     List<SearchTemplateContentItem> contentItems = new ArrayList();
/* 230 */     Set<String> itemIds = contentClause.keySet();
/* 231 */     for (String itemId : itemIds) {
/* 232 */       P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)contentClause.get(itemId);
/* 233 */       SearchTemplateContentItem contentItem = new SearchTemplateContentItem();
/* 234 */       contentItem.setItemId(itemId);
/* 235 */       String value = item.getUserText();
/* 236 */       boolean exclude = (value == null) || (value.length() < 1);
/* 237 */       if (exclude) {
/* 238 */         contentItem.setExcludeFromQuery(Boolean.valueOf(exclude));
/*     */       } else {
/* 240 */         List<String> itemData = new ArrayList();
/* 241 */         List<P8SearchDefinition.ContentTerm> terms = item.getContentTerms();
/* 242 */         StringBuilder cascadeProximity = new StringBuilder();
/* 243 */         for (P8SearchDefinition.ContentTerm term : terms) {
/* 244 */           String v = term.getValue();
/* 245 */           if (((term.isPhrase()) && ((!cascade) || (!item.isSearchModifierProximity()))) || ((cascade) && (term.isProhibited())))
/* 246 */             v = "\"" + v + "\"";
/* 247 */           if ((cascade) && (item.isSearchModifierProximity())) {
/* 248 */             if (cascadeProximity.length() > 0)
/* 249 */               cascadeProximity.append(" ");
/* 250 */             cascadeProximity.append(v);
/*     */           } else {
/* 252 */             itemData.add(v);
/*     */           }
/*     */         }
/* 255 */         if ((cascade) && (item.isSearchModifierProximity()))
/* 256 */           itemData.add(cascadeProximity.toString());
/* 257 */         if (item.isGroupActionVql()) {
/* 258 */           itemData.add(item.getUserText());
/*     */         }
/* 260 */         contentItem.setGroupAction(item.isGroupActionAny() ? GroupAction.ANY : item.isGroupActionAll() ? GroupAction.ALL : null);
/* 261 */         contentItem.setItemData(itemData);
/* 262 */         contentItem.setItemId(itemId);
/* 263 */         contentItem.setRequiredState(item.isRequiredStateProhibited() ? RequiredState.PROHIBITED : null);
/* 264 */         if ((cascade) && (item.isSearchModifierProximity())) {
/* 265 */           contentItem.setSearchModifier(SearchModifier.PROXIMITY);
/* 266 */           contentItem.setSearchModifierRange(Double.valueOf(item.getSearchModifierRange()));
/*     */         }
/*     */       }
/* 269 */       contentItems.add(contentItem);
/*     */     }
/* 271 */     content.setContentItems(contentItems);
/*     */     
/* 273 */     return content;
/*     */   }
/*     */   
/*     */   static List<SearchTemplateSubclass> convertSubclasses(List<P8SearchDefinition.Subclass> subclasses) throws Exception {
/* 277 */     if (P8SearchUtil.isSearchingAllClasses(subclasses)) {
/* 278 */       return null;
/*     */     }
/* 280 */     List<SearchTemplateSubclass> ceSubclasses = new ArrayList();
/* 281 */     for (P8SearchDefinition.Subclass subclass : subclasses) {
/* 282 */       SearchTemplateSubclass ceSubclass = new SearchTemplateSubclass();
/* 283 */       ceSubclass.setItemId(subclass.getItemId());
/* 284 */       ceSubclass.setExcludeFromQuery(Boolean.valueOf(subclass.isUnselected()));
/* 285 */       if (!subclass.isUnselected()) {
/* 286 */         ceSubclass.setIncludeSubclasses(Boolean.valueOf(false));
/* 287 */         ceSubclass.setSymbolicName(subclass.getName());
/*     */       }
/* 289 */       ceSubclasses.add(ceSubclass);
/*     */     }
/*     */     
/* 292 */     return ceSubclasses;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8CeSearchUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */