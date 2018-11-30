/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.ForwardPager;
/*    */ import com.ibm.ier.plugin.util.IERQuery;
/*    */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.BaseEntity;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.meta.RMClassDescription;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ public class ConditionSearchService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 29 */     return "ierGetConditionResults";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 33 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 35 */     String repositoryId = request.getParameter("repositoryId");
/* 36 */     String queryString = request.getParameter("ier_sql");
/*    */     
/* 38 */     int pageSize = 200;
/*    */     
/* 40 */     IERSearchResultsMediator mediator = new IERSearchResultsMediator();
/* 41 */     mediator.setServerName(repositoryId);
/* 42 */     mediator.setPageSize(pageSize);
/*    */     try
/*    */     {
/* 45 */       FilePlanRepository repository = this.baseService.getFilePlanRepository();
/*    */       
/* 47 */       Iterator<BaseEntity> iterator = IERQuery.executeQueryAsObjectsIterator(repository, queryString, null, pageSize, EntityType.Unknown);
/* 48 */       ForwardPager<BaseEntity> pager = new ForwardPager(iterator, pageSize);
/* 49 */       List<BaseEntity> entities = pager.loadNextPage();
/* 50 */       mediator.setSearchResultsBean(new IERSearchResultsBean(entities));
/* 51 */       mediator.setClassDescriptions(getClassDescriptions(request, repository, entities));
/*    */     } catch (Exception e) {
/* 53 */       e.printStackTrace();
/*    */     }
/*    */     
/*    */ 
/* 57 */     setCompletedJSONResponseObject(mediator.toJSONObject());
/*    */     
/* 59 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */   
/*    */   private List<RMClassDescription> getClassDescriptions(HttpServletRequest request, FilePlanRepository repository, List<BaseEntity> entities) {
/* 63 */     Map<String, RMClassDescription> map = new HashMap();
/* 64 */     for (BaseEntity entity : entities) {
/* 65 */       String className = entity.getClassName();
/* 66 */       if (map.get(className) == null) {
/* 67 */         RMClassDescription desc = SessionUtil.getClassDescription(repository, className, request);
/* 68 */         map.put(className, desc);
/*    */       }
/*    */     }
/* 71 */     return new ArrayList(map.values());
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\ConditionSearchService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */