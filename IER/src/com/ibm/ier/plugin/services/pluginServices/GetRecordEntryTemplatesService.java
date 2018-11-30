/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.RecordEntryTemplateListMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERQuery;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.BaseEntity;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetRecordEntryTemplatesService
/*    */   extends IERBasePluginService
/*    */ {
/* 34 */   private static final List<String> TEMPLATE_PROPERTIES = Arrays.asList(new String[] { "Id", "Name", "EntryTemplateDescription", "VersionSeries", "DocumentTitle", "EntryTemplateDescription", "TargetObjectStoreName", "TargetObjectClassId" });
/*    */   
/*    */ 
/*    */   public String getId()
/*    */   {
/* 39 */     return "ierGetRecordEntryTemplates";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator() {
/* 43 */     return new RecordEntryTemplateListMediator();
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 48 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 50 */     String mimeType = "application/x-filenet-declarerecordentrytemplate";
/* 51 */     List<BaseEntity> recordEntryTemplates = getAllEntryTemplates(mimeType);
/* 52 */     RecordEntryTemplateListMediator mediator = (RecordEntryTemplateListMediator)getMediator();
/* 53 */     mediator.setRecordEntryTemplates(recordEntryTemplates);
/*    */     
/* 55 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */   
/*    */   public List<BaseEntity> getAllEntryTemplates(String mimeType) {
/* 59 */     String methodName = "getAllEntryTemplates";
/* 60 */     Logger.logEntry(this, methodName, this.servletRequest);
/*    */     
/* 62 */     List<BaseEntity> recordEntryTemplates = new ArrayList();
/*    */     
/* 64 */     IERQuery query = new IERQuery();
/* 65 */     query.setRepository(getRepository());
/* 66 */     query.setFromClause("RecordsTemplate");
/* 67 */     query.setRequestedProperties(TEMPLATE_PROPERTIES);
/* 68 */     StringBuilder sqlWhere = new StringBuilder();
/*    */     
/* 70 */     if ((mimeType != null) && (mimeType.length() > 0)) {
/* 71 */       sqlWhere.append("MimeType = '").append(mimeType).append("' AND ");
/*    */     }
/* 73 */     sqlWhere.append("VersionStatus").append(" = ").append(1);
/* 74 */     query.setWhereClause(sqlWhere.toString());
/* 75 */     query.setOrderByClause("DocumentTitle");
/*    */     
/* 77 */     if (Logger.isDebugLogged()) {
/* 78 */       Logger.logDebug(this, methodName, this.servletRequest, "Retrieve entry templates sql query: " + query.toString());
/*    */     }
/* 80 */     Iterator<BaseEntity> recordEntryTemplate_it = query.executeQueryAsObjectsIterator(EntityType.ContentItem);
/* 81 */     while (recordEntryTemplate_it.hasNext()) {
/* 82 */       recordEntryTemplates.add(recordEntryTemplate_it.next());
/*    */     }
/*    */     
/* 85 */     Logger.logExit(this, methodName, this.servletRequest);
/* 86 */     return recordEntryTemplates;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetRecordEntryTemplatesService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */