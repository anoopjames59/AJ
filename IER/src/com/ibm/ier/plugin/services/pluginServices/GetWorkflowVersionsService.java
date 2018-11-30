/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.filenet.api.collection.VersionableSet;
/*    */ import com.filenet.api.core.Factory.WorkflowDefinition;
/*    */ import com.filenet.api.core.WorkflowDefinition;
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.jarm.api.core.BaseEntity;
/*    */ import com.ibm.jarm.api.core.ContentItem;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.meta.RMClassDescription;
/*    */ import com.ibm.jarm.api.util.P8CE_Convert;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetWorkflowVersionsService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 33 */     return "ierGetWorkflowVersions";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator() {
/* 37 */     return new IERSearchResultsMediator();
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 42 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */ 
/* 45 */     FilePlanRepository fp_repository = this.baseService.getFilePlanRepository();
/* 46 */     String itemId = IERUtil.getIdFromDocIdString(request.getParameter("ier_entityId"));
/* 47 */     WorkflowDefinition workflow = Factory.WorkflowDefinition.fetchInstance(P8CE_Convert.fromJARM(fp_repository), itemId, null);
/* 48 */     Iterator workflowVersions_it = workflow.get_Versions().iterator();
/* 49 */     List<BaseEntity> hits = new ArrayList();
/* 50 */     while (workflowVersions_it.hasNext()) {
/* 51 */       WorkflowDefinition workflowVersion = (WorkflowDefinition)workflowVersions_it.next();
/* 52 */       if ((workflowVersion != null) && (workflowVersion.get_VWVersion() != null)) {
/* 53 */         ContentItem item = P8CE_Convert.fromP8CE(workflowVersion);
/* 54 */         hits.add(item);
/*    */       }
/*    */     }
/*    */     
/* 58 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*    */     
/* 60 */     RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, "WorkflowDefinition", request);
/* 61 */     mediator.addClassDescription(cd);
/* 62 */     mediator.setServerName(this.baseService.getNexusRepositoryId());
/* 63 */     mediator.setClassName("WorkflowDefinition");
/* 64 */     mediator.setNameProperty("DocumentTitle");
/* 65 */     mediator.setIsObject(true);
/* 66 */     List<String> list = Arrays.asList(new String[] { "DocumentTitle", "MajorVersionNumber", "DateLastModified" });
/*    */     
/* 68 */     mediator.setRequestedProperties(list);
/* 69 */     mediator.setDisplayColumns(list);
/* 70 */     IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(hits, true);
/* 71 */     mediator.setSearchResultsBean(searchResultsBean);
/*    */     
/* 73 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetWorkflowVersionsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */