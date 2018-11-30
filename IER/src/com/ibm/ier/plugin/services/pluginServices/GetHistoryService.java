/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.DateUtil;
/*     */ import com.ibm.ier.plugin.util.ForwardPager;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.IteratorWrapper;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import org.apache.commons.collections.list.SetUniqueList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GetHistoryService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String ACTION_TYPE = "ActionType";
/*     */   private static final String EVENT_STATUS = "EventStatus";
/*     */   private static final String ALL = "All";
/*     */   private static final String STATUS_SUCCESS = "Success";
/*     */   private static final String INITIATOR = "InitiatingUser";
/*     */   
/*     */   public String getId()
/*     */   {
/*  48 */     return "ierGetHistoryService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  52 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*     */   {
/*  57 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  59 */     List<String> eventProps = Arrays.asList(new String[] { "ClassDescription", "DateCreated", "InitiatingUser", "EventStatus" });
/*  60 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*  61 */     mediator.setDisplayColumns(eventProps);
/*  62 */     mediator.setIsObject(true);
/*  63 */     SetUniqueList requestedProperties = SetUniqueList.decorate(new ArrayList(eventProps));
/*  64 */     mediator.setRequestedProperties(requestedProperties);
/*     */     
/*  66 */     String orderBy = request.getParameter("ier_orderBy");
/*  67 */     if ((orderBy == null) || (orderBy.isEmpty())) {
/*  68 */       orderBy = "DateCreated";
/*     */     }
/*     */     
/*  71 */     String sql = generateSql(request, orderBy);
/*  72 */     FilePlanRepository fp_repository = this.baseService.getFilePlanRepository();
/*  73 */     Iterator<BaseEntity> entityIterator = IERQuery.executeQueryAsObjectsIterator(fp_repository, sql, null, 200, EntityType.AuditEvent);
/*     */     
/*  75 */     List<Iterator<BaseEntity>> allIterators = new ArrayList();
/*  76 */     if (entityIterator != null) {
/*  77 */       allIterators.add(entityIterator);
/*     */     }
/*     */     
/*  80 */     IteratorWrapper<BaseEntity> wrapperIterator = new IteratorWrapper(allIterators);
/*  81 */     ForwardPager<BaseEntity> pager = new ForwardPager(wrapperIterator, 200);
/*  82 */     List<BaseEntity> hits = pager.loadNextPage();
/*     */     
/*  84 */     Collection<RMClassDescription> classDescriptions = findUniqueClassDescriptions(hits, fp_repository, request);
/*  85 */     mediator.setClassDescriptions(new ArrayList(classDescriptions));
/*  86 */     P8QueryContinuationData queryData = new P8QueryContinuationData();
/*  87 */     if (!pager.isEndReached()) {
/*  88 */       queryData.documentSQL = sql;
/*  89 */       queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/*  90 */       queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/*  91 */       request.getSession().setAttribute(queryData.sessionKey, pager);
/*  92 */       queryData.properties = IERUtil.StringListToString(requestedProperties);
/*  93 */       String data = queryData.saveToString();
/*  94 */       mediator.setContinuationData(data);
/*     */     }
/*     */     
/*  97 */     IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(hits, false);
/*  98 */     mediator.setSearchResultsBean(searchResultsBean);
/*  99 */     mediator.setMaxResultsReached(pager.isMaxResultsReached());
/* 100 */     mediator.setSortByProperty(orderBy);
/* 101 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   private String generateSql(HttpServletRequest request, String orderBy) {
/* 105 */     String entityId = IERUtil.getIdFromDocIdString(request.getParameter("ier_entityId"));
/* 106 */     String sqlStr = "";
/* 107 */     String filterField = request.getParameter("filterField");
/* 108 */     String filterActionType = request.getParameter("filterActionType");
/* 109 */     String filterStatus = request.getParameter("filterStatus");
/* 110 */     String filterStr = request.getParameter("ier_filterString");
/* 111 */     String itemType = "ObjectChangeEvent";
/* 112 */     if ((!filterActionType.equalsIgnoreCase("All")) && (filterField.equalsIgnoreCase("ActionType")) && (filterActionType != null) && (filterActionType.length() != 0)) {
/* 113 */       itemType = filterActionType;
/*     */     }
/*     */     
/* 116 */     sqlStr = "SELECT ae.[Id], ae.[ClassDescription], ae.[DateCreated], ae.[InitiatingUser], ae.[EventStatus] FROM [" + itemType + "] ae" + " WHERE ae.[SourceObjectId] = '" + entityId + "'";
/*     */     
/*     */ 
/*     */ 
/* 120 */     if ((!filterStatus.equalsIgnoreCase("All")) && (filterField.equalsIgnoreCase("EventStatus")) && (filterStatus != null) && (filterStatus.length() != 0)) {
/* 121 */       if (filterStatus.equalsIgnoreCase("Success")) {
/* 122 */         sqlStr = sqlStr + " AND ae.[EventStatus] = 0";
/*     */       } else {
/* 124 */         sqlStr = sqlStr + " AND ae.[EventStatus] <> 0";
/*     */       }
/* 126 */     } else if ((filterField.equalsIgnoreCase("InitiatingUser")) && (filterStr != null) && (filterStr.length() != 0)) {
/* 127 */       sqlStr = sqlStr + " AND ae.[" + filterField + "] like '%" + filterStr + "%'";
/*     */     }
/*     */     
/* 130 */     String dateSQL = generateDateSQL(request);
/* 131 */     if ((dateSQL != null) && (dateSQL.length() != 0)) {
/* 132 */       sqlStr = sqlStr + dateSQL;
/*     */     }
/* 134 */     sqlStr = sqlStr + " ORDER BY ae.[" + orderBy + "]";
/* 135 */     return sqlStr;
/*     */   }
/*     */   
/*     */   private java.sql.Date getSQLdate(String dateStr) {
/* 139 */     java.sql.Date sqlDate = null;
/* 140 */     java.util.Date date = null;
/*     */     try {
/* 142 */       date = DateUtil.parseISODate(dateStr, false);
/*     */     } catch (ParseException e) {
/* 144 */       e.printStackTrace();
/*     */     } catch (DatatypeConfigurationException e) {
/* 146 */       e.printStackTrace();
/*     */     }
/* 148 */     if (date != null) {
/* 149 */       sqlDate = new java.sql.Date(date.getTime());
/*     */     }
/* 151 */     return sqlDate;
/*     */   }
/*     */   
/*     */   private String generateDateSQL(HttpServletRequest request) {
/* 155 */     String dateSQL = "";
/*     */     
/* 157 */     String startDateStr = request.getParameter("startDate");
/* 158 */     String endDateStr = request.getParameter("endDate");
/* 159 */     if ((startDateStr != null) && (startDateStr.length() != 0)) {
/* 160 */       java.sql.Date sqlStartDate = getSQLdate(startDateStr);
/* 161 */       dateSQL = dateSQL + " AND ae.[DateCreated] >= " + sqlStartDate;
/*     */     }
/* 163 */     if ((endDateStr != null) && (endDateStr.length() != 0)) {
/* 164 */       java.sql.Date sqlEndDate = getSQLdate(endDateStr);
/* 165 */       dateSQL = dateSQL + " AND ae.[DateCreated] <= " + sqlEndDate;
/*     */     }
/* 167 */     return dateSQL;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetHistoryService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */