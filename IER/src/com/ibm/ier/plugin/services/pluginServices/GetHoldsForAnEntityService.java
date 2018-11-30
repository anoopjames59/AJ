/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.ForwardPager;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.IteratorWrapper;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.query.ResultRow;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.collections.list.SetUniqueList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GetHoldsForAnEntityService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  37 */     return "ierGetHoldsForEntityService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  41 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*     */   {
/*  46 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  48 */     P8QueryContinuationData queryData = new P8QueryContinuationData();
/*  49 */     FilePlanRepository fp_repository = this.baseService.getFilePlanRepository();
/*     */     
/*  51 */     String itemId = IERUtil.getIdFromDocIdString(request.getParameter("ier_entityId"));
/*  52 */     String filterString = request.getParameter("ier_filterString");
/*  53 */     String serverName = request.getParameter("repositoryId");
/*  54 */     String includeActiveRestrictionString = request.getParameter("ier_excludeActive");
/*  55 */     String type = request.getParameter("type");
/*     */     
/*  57 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*  58 */     mediator.setServerName(serverName);
/*  59 */     mediator.setPageSize(200);
/*  60 */     SetUniqueList requestedProperties = null;
/*  61 */     Iterator<ResultRow> rowIterator = null;
/*     */     
/*  63 */     String sql = null;
/*  64 */     List<String> documentProps = Arrays.asList(new String[] { "HoldName", "HoldReason", "Id", "HoldType", "Active", "ConditionXML", "IsDynamicHold", "DateCreated" });
/*     */     
/*     */ 
/*  67 */     requestedProperties = SetUniqueList.decorate(new ArrayList(documentProps));
/*     */     
/*  69 */     String holdLinkClass = (type != null) && (type.equals("document")) ? "RecordHoldLink" : "RMFolderHoldLink";
/*  70 */     sql = "SELECT h.[HoldName], h.[ClassDescription], h.[Name], h.[RMEntityType], h.[HoldReason], h.[Id], h.[HoldType], h.[Active], h.[ConditionXML], rhl.[IsDynamicHold], rhl.[DateCreated] FROM [RecordHold] h INNER JOIN " + holdLinkClass + " rhl ON h.This = rhl.Tail " + "WHERE rhl.Head = OBJECT('" + itemId + "') ";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  75 */     if (includeActiveRestrictionString != null) {
/*  76 */       boolean includeActive = Boolean.valueOf(includeActiveRestrictionString).booleanValue();
/*  77 */       if (includeActive)
/*  78 */         sql = sql + "AND h.Active = true ";
/*     */     }
/*  80 */     if (filterString != null) {
/*  81 */       sql = sql + "AND h.HoldName like '%" + filterString + "%' ";
/*     */     }
/*  83 */     sql = sql + "ORDER BY h.HoldName";
/*  84 */     queryData.documentSQL = sql;
/*     */     
/*  86 */     rowIterator = IERQuery.executeQueryAsRowsIterator(fp_repository, sql, null, 200);
/*  87 */     List<Iterator<ResultRow>> allIterators = new ArrayList();
/*  88 */     if (rowIterator != null) {
/*  89 */       allIterators.add(rowIterator);
/*     */     }
/*  91 */     IteratorWrapper<ResultRow> wrapperIterator = new IteratorWrapper(allIterators);
/*  92 */     ForwardPager<ResultRow> pager = new ForwardPager(wrapperIterator, 200);
/*  93 */     List<ResultRow> rows = pager.loadNextPage();
/*     */     
/*  95 */     if (!pager.isEndReached()) {
/*  96 */       queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/*  97 */       queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/*  98 */       request.getSession().setAttribute(queryData.sessionKey, pager);
/*  99 */       queryData.properties = IERUtil.StringListToString(requestedProperties);
/*     */       
/* 101 */       String data = queryData.saveToString();
/* 102 */       mediator.setContinuationData(data);
/*     */     }
/*     */     
/* 105 */     mediator.setResultRows(rows);
/* 106 */     mediator.setServerName(serverName);
/* 107 */     mediator.setIsObject(true);
/* 108 */     mediator.setClassName("RecordHold");
/* 109 */     mediator.setNameProperty("HoldName");
/* 110 */     mediator.setMaxResultsReached(pager.isMaxResultsReached());
/* 111 */     mediator.setRequestedProperties(requestedProperties);
/* 112 */     mediator.setDisplayColumns(Arrays.asList(new String[] { "HoldName", "HoldReason", "HoldType", "IsDynamicHold", "DateCreated" }));
/*     */     
/* 114 */     Collection<RMClassDescription> classDescriptions = findUniqueClassDescriptions(fp_repository, request);
/* 115 */     mediator.setClassDescriptions(new ArrayList(classDescriptions));
/*     */     
/* 117 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   protected static Collection<RMClassDescription> findUniqueClassDescriptions(FilePlanRepository fp_repository, HttpServletRequest request)
/*     */   {
/* 122 */     Map<String, RMClassDescription> mapOfCDs = new HashMap();
/* 123 */     RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, "RecordHold", request);
/* 124 */     String id = fp_repository.getObjectIdentity() + "/" + "RecordHold";
/* 125 */     mapOfCDs.put(id, cd);
/*     */     
/* 127 */     cd = SessionUtil.getClassDescription(fp_repository, "RecordHoldLink", request);
/* 128 */     id = fp_repository.getObjectIdentity() + "/" + "RecordHoldLink";
/* 129 */     mapOfCDs.put(id, cd);
/* 130 */     cd = SessionUtil.getClassDescription(fp_repository, "RMFolderHoldLink", request);
/* 131 */     id = fp_repository.getObjectIdentity() + "/" + "RMFolderHoldLink";
/* 132 */     mapOfCDs.put(id, cd);
/* 133 */     return mapOfCDs.values();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetHoldsForAnEntityService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */