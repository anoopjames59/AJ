/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.ier.plugin.util.json.JSONUtils;
/*     */ import com.ibm.ier.plugin.util.json.TableView;
/*     */ import com.ibm.ier.plugin.util.json.TableView.ColumnSet;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescriptionBoolean;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.query.CBRResult;
/*     */ import com.ibm.jarm.api.query.ResultRow;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
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
/*     */ public class IERSearchResultsMediator
/*     */   extends BaseMediator
/*     */ {
/*     */   private static final long serialVersionUID = -2904822157840003273L;
/*  57 */   private static List<String> DEFAULT_DISPLAY_COLUMNS = Arrays.asList(new String[] { "Reviewer", "DateLastModified", "CurrentPhaseExecutionDate" });
/*     */   
/*     */   private static final String NAME = "{NAME}";
/*     */   
/*     */   private static final String CONTAINER_ID = "ContainerId";
/*     */   public static final String PSEUDO_CLASS = "{CLASS}";
/*  63 */   protected String[] displayColumns = null;
/*     */   
/*  65 */   protected List<String> requestedProperties = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IERSearchResultsBean searchResultsBean;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   public List<ResultRow> resultRows = null;
/*     */   
/*  78 */   public List<CBRResult> cbrResultRows = null;
/*     */   
/*  80 */   public String className = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  85 */   public String nameProperty = null;
/*     */   
/*     */   public List<RMClassDescription> classDescriptions;
/*     */   
/*     */   public String folderDescription;
/*     */   
/*  91 */   protected JSONObject sortJsonObject = new JSONObject();
/*     */   
/*     */   public String serverName;
/*     */   
/*  95 */   public int pageSize = 200;
/*     */   
/*  97 */   public int sortIndex = -1;
/*     */   
/*  99 */   private final Hashtable<String, Integer> columnSizes = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 104 */   public int sortDirection = 0;
/*     */   
/*     */ 
/*     */   public String parentDocID;
/*     */   
/*     */ 
/*     */   protected String continuationData;
/*     */   
/*     */ 
/*     */   protected boolean isContinuation;
/*     */   
/*     */   protected boolean maxResultsReached;
/*     */   
/* 117 */   private Map<String, RMPropertyDescription> propertyDescriptionMap = null;
/*     */   
/* 119 */   private boolean includeSubclasses = false;
/* 120 */   private boolean isObject = false;
/*     */   protected String sortByProperty;
/*     */   private FilePlanRepository repository;
/* 123 */   private boolean isDocId = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 128 */   public int totalCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */   public String totalCountType = "none";
/*     */   
/*     */   public IERSearchResultsBean getSearchResultsBean()
/*     */   {
/* 138 */     return this.searchResultsBean;
/*     */   }
/*     */   
/*     */   public void setSearchResultsBean(IERSearchResultsBean searchResultsBean) {
/* 142 */     this.searchResultsBean = searchResultsBean;
/*     */   }
/*     */   
/*     */   public void setResultRows(List<ResultRow> rows) {
/* 146 */     this.resultRows = rows;
/*     */   }
/*     */   
/*     */   public void setCBRResultRows(List<CBRResult> rows) {
/* 150 */     this.cbrResultRows = rows;
/*     */   }
/*     */   
/*     */   public String getContinuationData() {
/* 154 */     return this.continuationData;
/*     */   }
/*     */   
/*     */   public void setContinuationData(String continuationData) {
/* 158 */     this.continuationData = continuationData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerName()
/*     */   {
/* 166 */     return this.serverName;
/*     */   }
/*     */   
/*     */   public void setNameProperty(String nameProperty) {
/* 170 */     this.nameProperty = nameProperty;
/*     */   }
/*     */   
/*     */   public void setClassName(String className) {
/* 174 */     this.className = className;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServerName(String serverName)
/*     */   {
/* 183 */     this.serverName = serverName;
/*     */   }
/*     */   
/*     */   public List<RMClassDescription> getClassDescriptions()
/*     */   {
/* 188 */     return this.classDescriptions;
/*     */   }
/*     */   
/*     */   public void setClassDescriptions(List<RMClassDescription> classDescriptions) {
/* 192 */     this.classDescriptions = classDescriptions;
/*     */   }
/*     */   
/*     */   public RMClassDescription getClassDescription() {
/* 196 */     if ((this.classDescriptions == null) || (this.classDescriptions.size() == 0)) {
/* 197 */       return null;
/*     */     }
/* 199 */     return (RMClassDescription)this.classDescriptions.iterator().next();
/*     */   }
/*     */   
/*     */   public void addClassDescription(RMClassDescription classDescription) {
/* 203 */     if (this.classDescriptions == null) {
/* 204 */       this.classDescriptions = new Vector();
/*     */     }
/* 206 */     this.classDescriptions.add(classDescription);
/*     */   }
/*     */   
/*     */   public void setIncludeSubClasses(boolean includeSubclasses) {
/* 210 */     this.includeSubclasses = includeSubclasses;
/*     */   }
/*     */   
/*     */   public void setDisplayColumns(List<String> columns) {
/* 214 */     this.displayColumns = ((String[])columns.toArray(new String[columns.size()]));
/*     */   }
/*     */   
/*     */   public void setRequestedProperties(List<String> requestedProperties) {
/* 218 */     this.requestedProperties = requestedProperties;
/*     */   }
/*     */   
/*     */   public void setIsObject(boolean isObject)
/*     */   {
/* 223 */     this.isObject = isObject;
/*     */   }
/*     */   
/*     */   public void setIsDocId(boolean isDocId) {
/* 227 */     this.isDocId = isDocId;
/*     */   }
/*     */   
/*     */   public boolean getIncludeSubClasses() {
/* 231 */     return this.includeSubclasses;
/*     */   }
/*     */   
/*     */   public void setParentDocID(String parentId) {
/* 235 */     this.parentDocID = parentId;
/*     */   }
/*     */   
/*     */   public void setSortByProperty(String sortByProperty) {
/* 239 */     this.sortByProperty = sortByProperty;
/*     */   }
/*     */   
/*     */   public String getSortByProperty() {
/* 243 */     return this.sortByProperty;
/*     */   }
/*     */   
/*     */   public void setFilePlanRepository(FilePlanRepository rep) {
/* 247 */     this.repository = rep;
/* 248 */     if (rep != null) {
/* 249 */       setServerName(rep.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONObject toJSONObject()
/*     */     throws Exception
/*     */   {
/* 260 */     JSONObject jsonObject = super.toJSONObject();
/*     */     try
/*     */     {
/* 263 */       popuplatePropertyDescriptionMap();
/*     */       
/* 265 */       if (getClassDescription() != null) {
/* 266 */         jsonObject.put("template_name", getClassDescription().getName());
/*     */       }
/* 268 */       jsonObject.put("repositoryId", getServerName());
/* 269 */       jsonObject.put("sortformats", this.sortJsonObject);
/*     */       
/*     */ 
/* 272 */       getClassDescriptionJSON(jsonObject, "templates");
/* 273 */       jsonObject.put("docid", this.parentDocID);
/*     */       
/* 275 */       int numberOfResults = 0;
/*     */       
/*     */ 
/* 278 */       if (this.searchResultsBean != null) {
/* 279 */         numberOfResults = getSearchResultsBean().size();
/*     */ 
/*     */       }
/* 282 */       else if (this.resultRows != null) {
/* 283 */         numberOfResults = this.resultRows.size();
/*     */       }
/* 285 */       else if (this.cbrResultRows != null) {
/* 286 */         numberOfResults = this.cbrResultRows.size();
/*     */       }
/*     */       
/* 289 */       jsonObject.put("num_results", Integer.valueOf(numberOfResults));
/* 290 */       if (!this.isContinuation) {
/* 291 */         jsonObject.put("columns", buildTableColumns());
/*     */       }
/* 293 */       jsonObject.put("rows", buildTableRows());
/* 294 */       jsonObject.put("pageSize", Integer.valueOf(this.pageSize));
/* 295 */       jsonObject.put("totalCount", Integer.valueOf(this.totalCount));
/* 296 */       jsonObject.put("totalCountType", this.totalCountType);
/*     */       
/*     */ 
/* 299 */       jsonObject.put("sortIndex", Integer.valueOf(getSortIndex()));
/* 300 */       jsonObject.put("sortDirection", Integer.valueOf(getSortDirection()));
/*     */       
/* 302 */       if (this.continuationData != null) {
/* 303 */         jsonObject.put("continuationData", this.continuationData);
/*     */       }
/*     */       
/* 306 */       if (this.maxResultsReached) {
/* 307 */         jsonObject.put("maxResultsReached", "true");
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 312 */       Logger.logError(this, "toJSONObject", this.servletRequest, e.getLocalizedMessage());
/* 313 */       throw e;
/*     */     }
/*     */     
/* 316 */     return jsonObject;
/*     */   }
/*     */   
/*     */   protected void getClassDescriptionJSON(JSONObject jsonObject, String category) throws IOException {
/* 320 */     String methodName = "toJSONFromVectorForClassDescriptions";
/*     */     
/* 322 */     Logger.logEntry(this, methodName, this.servletRequest);
/*     */     
/* 324 */     if ((this.classDescriptions != null) && (jsonObject != null)) {
/* 325 */       Logger.logDebug(this, methodName, this.servletRequest, "classDescriptions().size(): " + this.classDescriptions.size());
/*     */       
/* 327 */       for (RMClassDescription cd : this.classDescriptions) {
/* 328 */         String cdDisplayName = cd.getDisplayName();
/* 329 */         JSONObject jsonCD = new JSONObject();
/* 330 */         jsonCD.put("template_name", cd.getSymbolicName());
/* 331 */         jsonCD.put("template_label", cdDisplayName);
/* 332 */         jsonCD.put("template_desc", cdDisplayName);
/* 333 */         JSONUtils.accumulate(jsonObject, category, jsonCD);
/*     */       }
/*     */     }
/* 336 */     Logger.logExit(this, methodName, this.servletRequest);
/*     */   }
/*     */   
/*     */   public void fromJSONObject(JSONObject jsonObject) {
/* 340 */     throw new RuntimeException("This method is not implemented.");
/*     */   }
/*     */   
/*     */   private JSONArray buildTableRows() throws Exception {
/* 344 */     String methodName = "buildTableRows";
/* 345 */     Logger.logEntry(this, methodName, this.servletRequest);
/* 346 */     JSONArray rowsArray = new JSONArray();
/* 347 */     Logger.logDebug(this, methodName, this.servletRequest, "Starting to build json rows from hits");
/*     */     
/* 349 */     if ((this.requestedProperties == null) && (this.displayColumns != null)) {
/* 350 */       this.requestedProperties = Arrays.asList(this.displayColumns);
/*     */     }
/* 352 */     FilePlanRepository fp_Repository = this.repository == null ? this.baseService.getFilePlanRepository() : this.repository;
/*     */     Map<String, BaseEntity> entityMap;
/* 354 */     CBRRowResultMediator resultsMediator; if (this.searchResultsBean != null) {
/* 355 */       this.searchResultsBean.reset();
/*     */       
/* 357 */       IERSearchResultsBean searchResults = getSearchResultsBean();
/* 358 */       BaseEntityResultMediator resultsMediator = new BaseEntityResultMediator(fp_Repository, this.servletRequest, this.requestedProperties);
/* 359 */       if (this.baseService != null)
/* 360 */         resultsMediator.setBaseService(this.baseService);
/* 361 */       resultsMediator.setIsDocId(this.isDocId);
/*     */       
/* 363 */       List<BaseEntity> results = searchResults.getResults();
/* 364 */       for (BaseEntity item : results) {
/* 365 */         resultsMediator.setEntityItem(item);
/* 366 */         resultsMediator.setNameProperty(this.nameProperty);
/*     */         
/* 368 */         JSONObject json = resultsMediator.toJSONObject();
/* 369 */         if (this.requestedProperties.contains("ClassName")) {
/* 370 */           JSONObject attrs = (JSONObject)json.get("attributes");
/* 371 */           if (attrs != null) {
/* 372 */             JSONArray attrVal = new JSONArray();
/*     */             
/* 374 */             attrVal.add(item.getClassDescription().getDisplayName());
/* 375 */             attrVal.add(MediatorUtil.getJSONDataType(DataType.String));
/* 376 */             attrs.put("ClassName", attrVal);
/*     */           }
/*     */         }
/* 379 */         rowsArray.add(json);
/*     */       }
/*     */       
/* 382 */       Logger.logDebug(this, methodName, this.servletRequest, "results size: " + results.size());
/*     */     } else {
/*     */       ResultRowResultMediator resultsMediator;
/* 385 */       if (this.resultRows != null) {
/* 386 */         resultsMediator = new ResultRowResultMediator(fp_Repository, this.servletRequest, this.requestedProperties, this.className);
/* 387 */         for (ResultRow row : this.resultRows) {
/* 388 */           resultsMediator.setResultRow(row);
/* 389 */           resultsMediator.setNameProperty(this.nameProperty);
/* 390 */           rowsArray.add(resultsMediator.toJSONObject());
/*     */         }
/*     */       }
/* 393 */       else if (this.cbrResultRows != null)
/*     */       {
/* 395 */         RMClassDescription rmCD = getClassDescription();
/* 396 */         if ((this.className == null) && (rmCD != null)) {
/* 397 */           this.className = rmCD.getSymbolicName();
/*     */         }
/*     */         
/* 400 */         if (this.nameProperty == null) {
/* 401 */           List<RMPropertyDescription> pds = SessionUtil.getPropertyDescriptionList(fp_Repository, this.className, this.servletRequest);
/* 402 */           if ((rmCD != null) && (rmCD.getNamePropertyIndex() != null)) {
/* 403 */             int namePropertyIndex = rmCD.getNamePropertyIndex().intValue();
/* 404 */             RMPropertyDescription namePropDesc = (RMPropertyDescription)pds.get(namePropertyIndex);
/* 405 */             this.nameProperty = namePropDesc.getSymbolicName();
/*     */           }
/*     */           
/* 408 */           if (this.nameProperty == null) { this.nameProperty = "DocumentTitle";
/*     */           }
/*     */         }
/* 411 */         entityMap = getCBRResultsBaseEntity(this.cbrResultRows, rmCD);
/* 412 */         resultsMediator = new CBRRowResultMediator(fp_Repository, this.servletRequest, this.requestedProperties, this.className);
/* 413 */         for (CBRResult row : this.cbrResultRows) {
/* 414 */           resultsMediator.setCBRResultRow(row);
/* 415 */           resultsMediator.setNameProperty(this.nameProperty);
/*     */           
/* 417 */           String id = getId(row);
/* 418 */           if ((id != null) && (entityMap != null))
/* 419 */             resultsMediator.setBaseEntity((BaseEntity)entityMap.get(id));
/* 420 */           rowsArray.add(resultsMediator.toJSONObject());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 425 */     Logger.logExit(this, methodName, this.servletRequest);
/* 426 */     return rowsArray;
/*     */   }
/*     */   
/*     */   private String getId(CBRResult row) {
/* 430 */     if (row != null) {
/* 431 */       RMProperties props = row.getResultRow().getProperties();
/* 432 */       if (props.isPropertyPresent("Id"))
/* 433 */         return props.getGuidValue("Id").toString();
/*     */     }
/* 435 */     return null;
/*     */   }
/*     */   
/*     */   private Map<String, BaseEntity> getCBRResultsBaseEntity(List<CBRResult> cbrResultRows, RMClassDescription rmCD)
/*     */   {
/* 440 */     Map<String, BaseEntity> objMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*     */       EntityType et;
/*     */       
/*     */ 
/* 449 */       if (rmCD.describedIsOfClass("RecordInfo")) {
/* 450 */         String className = "RecordInfo";
/* 451 */         et = EntityType.Record; } else { EntityType et;
/* 452 */         if (rmCD.describedIsOfClass("RecordCategory")) {
/* 453 */           String className = "RecordCategory";
/* 454 */           et = EntityType.RecordCategory; } else { EntityType et;
/* 455 */           if (rmCD.describedIsOfClass("RecordFolder")) {
/* 456 */             String className = "RecordFolder";
/* 457 */             et = EntityType.RecordFolder; } else { EntityType et;
/* 458 */             if (rmCD.describedIsOfClass("Volume")) {
/* 459 */               String className = "Volume";
/* 460 */               et = EntityType.RecordVolume;
/*     */             } else {
/* 462 */               return null; } } } }
/*     */       EntityType et;
/*     */       String className;
/* 465 */       List<String> idList = new ArrayList();
/* 466 */       for (CBRResult row : cbrResultRows) {
/* 467 */         idList.add(getId(row));
/*     */       }
/* 469 */       if (idList.isEmpty()) { return null;
/*     */       }
/* 471 */       IERQuery dq = new IERQuery();
/* 472 */       dq.setFromClause(className);
/*     */       
/* 474 */       List<String> objectProps = new ArrayList(Arrays.asList(new String[] { "Id" }));
/*     */       
/* 476 */       if (et == EntityType.Record) {
/* 477 */         objectProps.add("DocumentTitle");
/* 478 */         objectProps.add("MimeType");
/*     */       } else {
/* 480 */         objectProps.add("FolderName");
/* 481 */         objectProps.add("CurrentPhaseExecutionStatus");
/*     */       }
/*     */       
/* 484 */       dq.setRequestedProperties(objectProps);
/* 485 */       dq.setRepository(this.repository);
/* 486 */       StringBuffer sb = new StringBuffer("Id IN (");
/* 487 */       for (int i = 0; i < idList.size(); i++) {
/* 488 */         if (i > 0) sb.append(",");
/* 489 */         sb.append("'").append((String)idList.get(i)).append("'");
/*     */       }
/* 491 */       sb.append(")");
/*     */       
/* 493 */       dq.setWhereClause(sb.toString());
/*     */       
/* 495 */       Iterator<BaseEntity> objectIterator = dq.executeQueryAsObjectsIterator(et);
/* 496 */       if (objectIterator != null) {
/* 497 */         while (objectIterator.hasNext()) {
/* 498 */           BaseEntity be = (BaseEntity)objectIterator.next();
/* 499 */           objMap.put(be.getObjectIdentity(), be);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 504 */       Logger.logError(this, "getCBRResultsBaseEntity", this.servletRequest, ex.getLocalizedMessage());
/*     */     }
/*     */     
/*     */ 
/* 508 */     return objMap;
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
/*     */   private JSONObject buildTableColumns()
/*     */     throws Exception
/*     */   {
/* 558 */     String methodName = "buildTableColums";
/* 559 */     Logger.logEntry(this, methodName, this.servletRequest);
/*     */     
/* 561 */     String dateFormat = "%m/%d/%Y";
/* 562 */     String timeFormat = "hh:mm:ss";
/* 563 */     TableView tableView = new TableView();
/* 564 */     TableView.ColumnSet columnSet = tableView.createColumnSet();
/*     */     
/* 566 */     if (!this.isObject)
/*     */     {
/*     */ 
/* 569 */       columnSet.addColumnProperty(buildStateColumnProperty("", "xs:string", "61px", "76px", "multiStateIcon", "ierMultiStateIconsDecorator", false), true);
/*     */       
/* 571 */       columnSet.addColumnProperty(buildStateColumnProperty("", "xs:string", "17px", "32px", "mimeTypeIcon", "ierMimeTypeDecorator", false), true);
/*     */     }
/*     */     else {
/* 574 */       columnSet.addColumnProperty(buildStateColumnProperty("", "xs:string", "17px", "32px", "mimeTypeIcon", "ierMimeTypeDecorator", false));
/*     */     }
/*     */     
/*     */ 
/* 578 */     if ((this.displayColumns != null) && (this.displayColumns.length > 0)) { String name;
/* 579 */       for (name : this.displayColumns) {
/* 580 */         if (name.equals("{NAME}"))
/*     */         {
/* 582 */           columnSet.addColumnProperty(buildColumnProperty(MessageResources.getLocalizedMessage(MessageCode.NAME, new Object[0]), "xs:string", "20em", "20em", "{NAME}", true));
/* 583 */           if (this.sortByProperty == null) {
/* 584 */             setSortIndex(columnSet.size());
/*     */           }
/* 586 */         } else if (name.equals("ContainerId"))
/*     */         {
/* 588 */           columnSet.addColumnProperty(buildColumnProperty(MessageResources.getLocalizedMessage(MessageCode.CONTAINER_ID, new Object[0]), "xs:string", "15em", "15em", "ContainerId", true));
/* 589 */         } else if (name.equals("{CLASS}")) {
/* 590 */           columnSet.addColumnProperty(buildColumnProperty(MessageResources.getLocalizedMessage(MessageCode.CLASS, new Object[0]), "xs:string", "20em", "20em", name, false));
/*     */         }
/* 592 */         else if ((this.classDescriptions != null) && (this.classDescriptions.size() != 0)) {
/* 593 */           for (RMClassDescription cd : this.classDescriptions) {
/* 594 */             RMPropertyDescription pd = (RMPropertyDescription)this.propertyDescriptionMap.get(cd.getSymbolicName() + name);
/* 595 */             if (pd != null) {
/* 596 */               addToColumnSet(columnSet, name, pd);
/* 597 */               if ((this.sortByProperty == null) || (!name.equals(this.sortByProperty))) break;
/* 598 */               setSortIndex(columnSet.size()); break;
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 607 */       if (!this.isObject)
/*     */       {
/* 609 */         columnSet.addColumnProperty(buildColumnProperty(MessageResources.getLocalizedMessage(MessageCode.NAME, new Object[0]), "xs:string", "20em", "20em", "{NAME}", true));
/* 610 */         if (this.sortByProperty == null) {
/* 611 */           setSortIndex(columnSet.size());
/*     */         }
/*     */         
/* 614 */         columnSet.addColumnProperty(buildColumnProperty(MessageResources.getLocalizedMessage(MessageCode.CONTAINER_ID, new Object[0]), "xs:string", "15em", "15em", "ContainerId", true));
/*     */       }
/* 616 */       if ((this.classDescriptions != null) && (this.classDescriptions.size() != 0)) {
/* 617 */         for (String defaultColumn : DEFAULT_DISPLAY_COLUMNS) {
/* 618 */           addToColumnSet(columnSet, defaultColumn, null);
/* 619 */           if ((this.sortByProperty != null) && (defaultColumn.equals(this.sortByProperty))) {
/* 620 */             setSortIndex(columnSet.size());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 626 */     this.sortJsonObject.put("dataFormat", dateFormat);
/* 627 */     this.sortJsonObject.put("timeFormat", timeFormat);
/*     */     
/* 629 */     Logger.logExit(this, methodName, this.servletRequest);
/* 630 */     return tableView.toJson();
/*     */   }
/*     */   
/*     */   private void addToColumnSet(TableView.ColumnSet set, String columnName, RMPropertyDescription pd) {
/* 634 */     if ((pd == null) && 
/* 635 */       (this.classDescriptions != null) && (this.classDescriptions.size() > 0)) {
/* 636 */       pd = (RMPropertyDescription)this.propertyDescriptionMap.get(((RMClassDescription)this.classDescriptions.get(0)).getSymbolicName() + columnName);
/*     */     }
/*     */     
/*     */ 
/* 640 */     if (pd != null) {
/* 641 */       String criteriaName = pd.getDisplayName();
/* 642 */       String symbolicName = pd.getSymbolicName();
/*     */       
/* 644 */       double size = 15.0D;
/*     */       
/* 646 */       if ((pd instanceof RMPropertyDescriptionBoolean)) {
/* 647 */         size = 8.0D;
/*     */       }
/*     */       
/* 650 */       String nameFixed = criteriaName.replace('.', '_');
/* 651 */       if (this.columnSizes.get(nameFixed) != null) {
/* 652 */         int tempsize = ((Integer)this.columnSizes.get(nameFixed)).intValue();
/* 653 */         if (tempsize > size)
/* 654 */           size = tempsize;
/*     */       }
/* 656 */       size = Math.max(criteriaName.length(), size) * 0.7D;
/*     */       
/* 658 */       if ((this.isObject) && ((symbolicName.equals("Description")) || (symbolicName.equals("HoldReason")) || (symbolicName.equals("RMEntityDescription")))) {
/* 659 */         size *= 2.0D;
/*     */       }
/* 661 */       String width = formatSize(size) + "em";
/*     */       
/* 663 */       boolean orderable = false;
/*     */       try {
/* 665 */         orderable = pd.isOrderable();
/*     */       }
/*     */       catch (EngineRuntimeException e) {}
/*     */       
/* 669 */       set.addColumnProperty(buildColumnProperty(criteriaName, MediatorUtil.getJSONDataType(pd.getDataType()), width, width, symbolicName, orderable));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void popuplatePropertyDescriptionMap() {
/* 674 */     if (this.propertyDescriptionMap == null)
/* 675 */       this.propertyDescriptionMap = new HashMap();
/*     */     FilePlanRepository fp_Repository;
/* 677 */     Iterator i$; if (this.classDescriptions != null) {
/* 678 */       fp_Repository = this.repository == null ? this.baseService.getFilePlanRepository() : this.repository;
/* 679 */       for (i$ = this.classDescriptions.iterator(); i$.hasNext();) { classDescription = (RMClassDescription)i$.next();
/* 680 */         List<RMPropertyDescription> pds = SessionUtil.getPropertyDescriptionList(fp_Repository, classDescription.getSymbolicName(), this.servletRequest, true);
/* 681 */         for (RMPropertyDescription pd : pds)
/* 682 */           this.propertyDescriptionMap.put(classDescription.getSymbolicName() + pd.getSymbolicName(), pd);
/*     */       }
/*     */     }
/*     */     RMClassDescription classDescription;
/*     */   }
/*     */   
/*     */   protected Map<String, Object> buildColumnProperty(String displayName, String dataType, String width, String webkitWidth, String symbolicName, boolean isSortable) {
/* 689 */     Map<String, Object> propertyMap = new HashMap();
/* 690 */     propertyMap.put("name", displayName);
/* 691 */     propertyMap.put("dataType", dataType);
/* 692 */     propertyMap.put("width", width);
/* 693 */     propertyMap.put("widthWebKit", webkitWidth);
/* 694 */     propertyMap.put("field", symbolicName);
/* 695 */     propertyMap.put("columnName", symbolicName);
/*     */     
/* 697 */     if (dataType.equals("xs:object")) {
/* 698 */       propertyMap.put("widgetsInCell", Boolean.valueOf(true));
/* 699 */       propertyMap.put("setCellValue", "ierObjectTypeCellValueFormatter");
/* 700 */       propertyMap.put("decorator", "ierObjectTypeDecorator");
/*     */       
/* 702 */       propertyMap.put("sortable", Boolean.valueOf(false));
/*     */     }
/*     */     else {
/* 705 */       propertyMap.put("sortable", Boolean.valueOf(isSortable));
/*     */     }
/* 707 */     return propertyMap;
/*     */   }
/*     */   
/*     */   private Map<String, Object> buildStateColumnProperty(String displayName, String dataType, String width, String webkitWidth, String symbolicName, String decorator, boolean isSortable) {
/* 711 */     Map<String, Object> propertyMap = new HashMap();
/* 712 */     propertyMap.put("name", displayName);
/* 713 */     propertyMap.put("dataType", dataType);
/* 714 */     propertyMap.put("width", width);
/* 715 */     propertyMap.put("widthWebKit", webkitWidth);
/* 716 */     propertyMap.put("field", symbolicName);
/* 717 */     propertyMap.put("cellClasses", "ecmIconCell");
/* 718 */     propertyMap.put("headerClasses", "ecmIconCell");
/* 719 */     propertyMap.put("sortable", Boolean.valueOf(isSortable));
/* 720 */     propertyMap.put("decorator", decorator);
/* 721 */     propertyMap.put("columnName", symbolicName);
/*     */     
/* 723 */     return propertyMap;
/*     */   }
/*     */   
/*     */   private static String formatSize(double val) {
/* 727 */     int ix = (int)(val * 100.0D);
/* 728 */     double rounded = ix / 100.0D;
/* 729 */     return Double.toString(rounded);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFolderDescription()
/*     */   {
/* 736 */     return this.folderDescription;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFolderDescription(String folderDescription)
/*     */   {
/* 744 */     this.folderDescription = folderDescription;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/* 751 */     return this.pageSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPageSize(int pageSize)
/*     */   {
/* 759 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getSortDirection()
/*     */   {
/* 766 */     return this.sortDirection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortDirection(int sortDirection)
/*     */   {
/* 774 */     this.sortDirection = sortDirection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getSortIndex()
/*     */   {
/* 781 */     return this.sortIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortIndex(int sortIndex)
/*     */   {
/* 789 */     this.sortIndex = sortIndex;
/*     */   }
/*     */   
/*     */   public void setContinuation(boolean value) {
/* 793 */     this.isContinuation = value;
/*     */   }
/*     */   
/*     */   public boolean isContinuation() {
/* 797 */     return this.isContinuation;
/*     */   }
/*     */   
/*     */   public boolean isMaxResultsReached() {
/* 801 */     return this.maxResultsReached;
/*     */   }
/*     */   
/*     */   public void setMaxResultsReached(boolean maxResultsReached) {
/* 805 */     this.maxResultsReached = maxResultsReached;
/*     */   }
/*     */   
/*     */   public int getTotalCount() {
/* 809 */     return this.totalCount;
/*     */   }
/*     */   
/*     */   public void setTotalCount(int totalCount) {
/* 813 */     this.totalCount = totalCount;
/*     */   }
/*     */   
/*     */   public String getTotalCountType() {
/* 817 */     return this.totalCountType;
/*     */   }
/*     */   
/*     */   public void setTotalCountType(String totalCountType) {
/* 821 */     this.totalCountType = totalCountType;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\IERSearchResultsMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */