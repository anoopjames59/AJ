/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.EncoderUtil;
/*     */ import com.ibm.ier.plugin.util.ForwardPager;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.IteratorWrapper;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescriptionObject;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GetObjectsService
/*     */   extends IERBasePluginService
/*     */ {
/*  45 */   private List<String> objectProps = null;
/*  46 */   private List<String> displayProperties = null;
/*  47 */   private StringBuilder whereClause = new StringBuilder();
/*  48 */   private boolean showAdditionalColumns = false;
/*  49 */   private EntityType entityType = EntityType.CustomObject;
/*  50 */   private String nameProperty = null;
/*     */   
/*     */   private String orderByField;
/*  53 */   private final int pageSize = 200;
/*  54 */   private IERQuery query = null;
/*     */   
/*     */   public String getId()
/*     */   {
/*  58 */     return "ierGetObjectsService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator()
/*     */   {
/*  63 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  69 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  71 */     FilePlanRepository fp_repository = this.baseService.getFilePlanRepository();
/*  72 */     P8QueryContinuationData queryData = new P8QueryContinuationData();
/*     */     
/*  74 */     String className = request.getParameter("ier_className");
/*  75 */     String serverName = request.getParameter("repositoryId");
/*  76 */     String entityTypeParam = request.getParameter("ier_entityType");
/*  77 */     String showAdditionalColumnsParam = request.getParameter("ier_showAdditionalColumns");
/*  78 */     String propertyName = request.getParameter("ier_propertyName");
/*  79 */     String descendingOrderParam = request.getParameter("order_descending");
/*  80 */     String type = request.getParameter("type");
/*     */     
/*  82 */     boolean descending = StringUtils.equals(descendingOrderParam, "true");
/*  83 */     this.orderByField = request.getParameter("ier_orderBy");
/*     */     
/*  85 */     if ((showAdditionalColumnsParam != null) && (showAdditionalColumnsParam.equals("true"))) {
/*  86 */       this.showAdditionalColumns = true;
/*     */     }
/*  88 */     JSONObject requestContent = getRequestContent();
/*  89 */     String filterString = requestContent.get("ier_filterString") != null ? requestContent.get("ier_filterString").toString() : null;
/*     */     
/*  91 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*  92 */     mediator.setServerName(serverName);
/*  93 */     mediator.setPageSize(200);
/*     */     
/*     */ 
/*  96 */     this.query = new IERQuery();
/*     */     
/*     */ 
/*     */ 
/* 100 */     if ((className != null) && (propertyName != null)) {
/* 101 */       RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, className, this.servletRequest);
/* 102 */       RMPropertyDescription pd = cd.getPropertyDescription(propertyName);
/* 103 */       if ((pd instanceof RMPropertyDescriptionObject)) {
/* 104 */         RMPropertyDescriptionObject pdObject = (RMPropertyDescriptionObject)pd;
/* 105 */         RMClassDescription requiredClassCD = pdObject.getRequiredClass();
/* 106 */         className = requiredClassCD.getSymbolicName();
/*     */       }
/*     */     }
/*     */     
/* 110 */     setClassData(className, fp_repository, type);
/* 111 */     this.query.setFromClause(className);
/*     */     
/*     */ 
/* 114 */     if ((filterString != null) && (filterString.length() > 0) && (this.nameProperty != null)) {
/* 115 */       String encodedFilterString = "%" + EncoderUtil.escapeSQLString(filterString) + "%";
/* 116 */       StringBuilder sb = new StringBuilder();
/* 117 */       sb.append(this.nameProperty + " like '");
/* 118 */       sb.append(encodedFilterString);
/* 119 */       sb.append("'");
/*     */       
/* 121 */       if (this.whereClause.length() != 0) {
/* 122 */         this.whereClause.append(" AND ( " + sb.toString() + ")");
/*     */       } else {
/* 124 */         this.whereClause = sb;
/*     */       }
/*     */     }
/* 127 */     if (this.showAdditionalColumns) {
/* 128 */       RepositoryConfig config = Config.getRepositoryConfig(serverName);
/* 129 */       if (config != null) {
/* 130 */         String[] columns = config.getDisplayColumns(className);
/* 131 */         if (columns != null) {
/* 132 */           this.displayProperties = new ArrayList(Arrays.asList(columns));
/* 133 */           for (String p : this.displayProperties) {
/* 134 */             if (this.objectProps.indexOf(p) < 0) {
/* 135 */               this.objectProps.add(p);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 142 */     if ((className.equalsIgnoreCase("Action1")) || (className.equalsIgnoreCase("DisposalSchedule")) || (className.equalsIgnoreCase("DisposalTrigger")))
/*     */     {
/*     */ 
/*     */ 
/* 146 */       if (this.whereClause.length() != 0)
/*     */       {
/* 148 */         this.whereClause.append(" AND RMExternallyManagedBy IS NULL");
/*     */       }
/*     */       else
/*     */       {
/* 152 */         this.whereClause.append("RMExternallyManagedBy IS NULL");
/*     */       }
/* 154 */       this.query.setWhereClause(this.whereClause.toString());
/*     */     }
/* 156 */     else if (this.whereClause.length() != 0) {
/* 157 */       this.query.setWhereClause(this.whereClause.toString());
/*     */     }
/* 159 */     String sortProperty = getOrderByField();
/* 160 */     if (!IERUtil.isNullOrEmpty(sortProperty)) {
/* 161 */       this.query.setOrderByClause(sortProperty + (descending ? " DESC" : ""));
/*     */     }
/* 163 */     this.query.setRequestedProperties(this.objectProps);
/* 164 */     this.query.setRepository(fp_repository);
/* 165 */     this.query.setPageSize(200);
/*     */     
/* 167 */     Iterator<BaseEntity> objectIterator = null;
/*     */     
/* 169 */     if (entityTypeParam != null) {
/* 170 */       this.entityType = EntityType.getInstanceFromInt(Integer.parseInt(entityTypeParam));
/*     */     }
/* 172 */     objectIterator = this.query.executeQueryAsObjectsIterator(this.entityType);
/* 173 */     List<Iterator<BaseEntity>> allIterators = new ArrayList();
/* 174 */     allIterators.add(objectIterator);
/* 175 */     IteratorWrapper<BaseEntity> wrapperIterator = new IteratorWrapper(allIterators);
/* 176 */     ForwardPager<BaseEntity> pager = new ForwardPager(wrapperIterator, 200);
/* 177 */     List<BaseEntity> hits = pager.loadNextPage();
/*     */     
/* 179 */     if (!pager.isEndReached()) {
/* 180 */       queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/* 181 */       queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/* 182 */       request.getSession().setAttribute(queryData.sessionKey, pager);
/* 183 */       queryData.properties = IERUtil.StringListToString(this.objectProps);
/*     */       
/* 185 */       String data = queryData.saveToString();
/* 186 */       mediator.setContinuationData(data);
/*     */     }
/*     */     
/* 189 */     Collection<RMClassDescription> classDescriptions = findUniqueClassDescriptions(hits, fp_repository, request, true);
/* 190 */     mediator.setClassDescriptions(new ArrayList(classDescriptions));
/*     */     
/* 192 */     if (this.displayProperties == null) {
/* 193 */       this.displayProperties = this.objectProps;
/*     */     }
/* 195 */     this.displayProperties.remove("RMExternallyManagedBy");
/* 196 */     mediator.setDisplayColumns(this.displayProperties);
/*     */     
/* 198 */     IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(hits);
/* 199 */     mediator.setSearchResultsBean(searchResultsBean);
/* 200 */     mediator.setIsObject(true);
/* 201 */     mediator.setMaxResultsReached(pager.isMaxResultsReached());
/* 202 */     mediator.setRequestedProperties(this.objectProps);
/* 203 */     mediator.setSortByProperty(sortProperty);
/* 204 */     mediator.setSortDirection(descending ? -1 : 1);
/*     */     
/* 206 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setClassData(String className, Repository repository, String type)
/*     */   {
/* 217 */     if (className.equals("ClassificationScheme"))
/*     */     {
/* 219 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "ClassificationSchemeName", "RMEntityDescription" }));
/*     */       
/* 221 */       if (this.showAdditionalColumns) {
/* 222 */         this.objectProps.add("Pattern");
/* 223 */         this.objectProps.add("RetainMetadata");
/*     */       }
/*     */       
/* 226 */       setOrderByField("ClassificationSchemeName");
/*     */       
/* 228 */       this.entityType = EntityType.FilePlan;
/*     */     }
/* 230 */     else if (className.equals("DisposalSchedule"))
/*     */     {
/* 232 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "DisposalScheduleName", "RMEntityDescription", "AuthorisingStatute" }));
/*     */       
/* 234 */       this.displayProperties = new ArrayList(Arrays.asList(new String[] { "DisposalScheduleName", "RMEntityDescription" }));
/*     */       
/* 236 */       if (this.showAdditionalColumns) {
/* 237 */         this.objectProps.add("CutOffBase");
/* 238 */         this.objectProps.add("Creator");
/* 239 */         this.objectProps.add("DateCreated");
/*     */       }
/*     */       
/* 242 */       setOrderByField("DisposalScheduleName");
/*     */     }
/* 244 */     else if (className.equals("RMReportDefinition"))
/*     */     {
/* 246 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "RMReportTitle", "Description" }));
/*     */       
/* 248 */       if (this.showAdditionalColumns) {
/* 249 */         this.objectProps.add("Creator");
/* 250 */         this.objectProps.add("DateCreated");
/*     */       }
/*     */       
/* 253 */       setOrderByField("RMReportTitle");
/* 254 */       this.whereClause.append("IsCurrentVersion = true");
/*     */ 
/*     */     }
/* 257 */     else if (className.equals("Location"))
/*     */     {
/* 259 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "LocationName", "BarcodeID", "RMEntityDescription" }));
/*     */       
/* 261 */       if (this.showAdditionalColumns) {
/* 262 */         this.objectProps.add("Reviewer");
/* 263 */         this.objectProps.add("Creator");
/* 264 */         this.objectProps.add("DateCreated");
/*     */       }
/*     */       
/* 267 */       setOrderByField("LocationName");
/*     */     }
/* 269 */     else if (className.equals("Action1"))
/*     */     {
/* 271 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "ActionName", "RMEntityDescription" }));
/* 272 */       this.displayProperties = new ArrayList(this.objectProps);
/*     */       
/* 274 */       if (this.showAdditionalColumns) {
/* 275 */         this.objectProps.add("ActionType");
/* 276 */         this.objectProps.add("Creator");
/* 277 */         this.objectProps.add("DateCreated");
/*     */         
/*     */ 
/* 280 */         this.displayProperties.add("Creator");
/* 281 */         this.displayProperties.add("DateCreated");
/*     */       }
/*     */       
/* 284 */       setOrderByField("ActionName");
/*     */     }
/* 286 */     else if (className.equals("DisposalTrigger"))
/*     */     {
/* 288 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "DisposalTriggerName", "RMEntityDescription" }));
/* 289 */       this.displayProperties = new ArrayList(this.objectProps);
/*     */       
/* 291 */       if (this.showAdditionalColumns) {
/* 292 */         this.objectProps.add("EventType");
/* 293 */         this.objectProps.add("Creator");
/* 294 */         this.objectProps.add("DateCreated");
/*     */         
/*     */ 
/* 297 */         this.displayProperties.add("Creator");
/* 298 */         this.displayProperties.add("DateCreated");
/*     */       }
/*     */       
/* 301 */       setOrderByField("DisposalTriggerName");
/*     */     }
/* 303 */     else if (className.equals("RecordType"))
/*     */     {
/* 305 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "RecordTypeName", "RMEntityDescription" }));
/*     */       
/* 307 */       if (this.showAdditionalColumns) {
/* 308 */         this.objectProps.add("Creator");
/* 309 */         this.objectProps.add("DateCreated");
/*     */       }
/*     */       
/* 312 */       setOrderByField("RecordTypeName");
/*     */     }
/* 314 */     else if (className.equals("RecordInfo"))
/*     */     {
/* 316 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "DocumentTitle", "RMEntityDescription", "MimeType" }));
/* 317 */       this.displayProperties = new ArrayList(Arrays.asList(new String[] { "DocumentTitle", "RMEntityDescription" }));
/*     */       
/* 319 */       this.orderByField = null;
/*     */       
/* 321 */       this.nameProperty = "DocumentTitle";
/*     */       
/* 323 */       this.entityType = EntityType.Record;
/*     */       
/* 325 */       this.query.setTopLimitSize(1000);
/*     */     }
/* 327 */     else if (className.equals("RecordHold"))
/*     */     {
/* 329 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "HoldName", "HoldReason", "HoldType", "Active" }));
/*     */       
/* 331 */       this.displayProperties = new ArrayList(Arrays.asList(new String[] { "HoldName", "HoldReason", "HoldType", "Active" }));
/*     */       
/* 333 */       if (this.showAdditionalColumns) {
/* 334 */         this.objectProps.add("SweepState");
/* 335 */         this.objectProps.add("LastHoldSweepDate");
/* 336 */         this.objectProps.add("ConditionXML");
/* 337 */         this.objectProps.add("Creator");
/* 338 */         this.objectProps.add("DateCreated");
/*     */         
/*     */ 
/* 341 */         this.displayProperties.add("SweepState");
/* 342 */         this.displayProperties.add("LastHoldSweepDate");
/* 343 */         this.displayProperties.add("Creator");
/* 344 */         this.displayProperties.add("DateCreated");
/*     */       }
/*     */       
/* 347 */       setOrderByField("HoldName");
/*     */     }
/* 349 */     else if (className.equals("Pattern"))
/*     */     {
/* 351 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "PatternName", "RMEntityDescription", "ApplyToNameOrId" }));
/*     */       
/* 353 */       if (this.showAdditionalColumns) {
/* 354 */         this.objectProps.add("Creator");
/* 355 */         this.objectProps.add("DateCreated");
/*     */       }
/*     */       
/* 358 */       setOrderByField("PatternName");
/*     */     }
/* 360 */     else if (className.equals("RMTransferMapping"))
/*     */     {
/* 362 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "DocumentTitle", "Description", "Creator", "DateCreated" }));
/*     */       
/* 364 */       setOrderByField("DocumentTitle");
/*     */       
/* 366 */       this.entityType = EntityType.ContentItem;
/*     */       
/* 368 */       this.whereClause.append("IsCurrentVersion = true AND this insubfolder '/Records Management/RMTransferMappings'");
/*     */ 
/*     */     }
/* 371 */     else if (className.equals("WorkflowDefinition"))
/*     */     {
/* 373 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "DocumentTitle", "Creator", "DateCreated" }));
/*     */       
/* 375 */       setOrderByField("DocumentTitle");
/*     */       
/* 377 */       this.entityType = EntityType.ContentItem;
/*     */       
/* 379 */       this.whereClause.append("IsCurrentVersion = true");
/*     */       
/* 381 */       if (type != null) {
/* 382 */         this.whereClause.append(" AND THIS INFOLDER '/Records Management/Workflows/" + type + "'");
/*     */       }
/*     */     }
/* 385 */     else if (className.equals("SystemConfiguration"))
/*     */     {
/* 387 */       this.objectProps = new ArrayList(Arrays.asList(new String[] { "PropertyName", "PropertyDataType", "PropertyValue" }));
/*     */       
/* 389 */       setOrderByField("PropertyName");
/*     */       
/* 391 */       this.entityType = EntityType.SystemConfiguration;
/*     */     }
/*     */     else
/*     */     {
/* 395 */       RMClassDescription cd = SessionUtil.getClassDescription(repository, className, this.servletRequest);
/* 396 */       this.entityType = EntityType.Unknown;
/*     */       
/* 398 */       Integer index = cd.getNamePropertyIndex();
/* 399 */       if (index != null) {
/* 400 */         RMPropertyDescription namePD = (RMPropertyDescription)cd.getPropertyDescriptions().get(index.intValue());
/* 401 */         this.objectProps = Arrays.asList(new String[] { namePD.getSymbolicName(), "Id" });
/*     */         
/* 403 */         setOrderByField(namePD.getSymbolicName());
/*     */       }
/*     */       else {
/* 406 */         this.objectProps = Arrays.asList(new String[] { "Id" });
/* 407 */         setOrderByField("Id");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 412 */     if ((this.orderByField != null) && (this.nameProperty == null)) {
/* 413 */       this.nameProperty = this.orderByField;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getOrderByField() {
/* 418 */     return this.orderByField;
/*     */   }
/*     */   
/*     */   private void setOrderByField(String orderBy)
/*     */   {
/* 423 */     if ((this.orderByField == null) || (this.orderByField.length() == 0)) {
/* 424 */       this.orderByField = orderBy;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetObjectsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */