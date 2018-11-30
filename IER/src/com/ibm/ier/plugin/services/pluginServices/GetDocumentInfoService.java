/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.collection.PageableSet;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.ContentItem;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.BaseEntity;
/*    */ import com.ibm.jarm.api.core.Record;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.Iterator;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetDocumentInfoService extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 27 */     return "ierGetDocumentInfoService";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator() {
/* 31 */     return new IERSearchResultsMediator();
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 35 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 37 */     FilePlanRepository fp_repo = this.baseService.getFilePlanRepository();
/* 38 */     String recordId = IERUtil.getIdFromDocIdString(request.getParameter("ier_recordId"));
/* 39 */     Record rd = (Record)RMFactory.BaseEntity.fetchInstance(fp_repo, EntityType.Record, recordId, RMPropertyFilter.MinimumPropertySet);
/* 40 */     PageableSet<ContentItem> docItems = rd.getAssociatedContentItems();
/* 41 */     JSONObject responseObject = new JSONObject();
/* 42 */     JSONArray responseArray = new JSONArray();
/* 43 */     if (!docItems.isEmpty()) {
/* 44 */       Iterator<ContentItem> docIt = docItems.iterator();
/* 45 */       while (docIt.hasNext()) {
/* 46 */         ContentItem docItem = (ContentItem)docIt.next();
/* 47 */         String documentObjectStoreId = null;
/* 48 */         String repoConnUrl = null;
/* 49 */         if (docItem.getRepository() != null) {
/* 50 */           documentObjectStoreId = docItem.getRepository().getSymbolicName();
/* 51 */           repoConnUrl = docItem.getRepository().getDomain().getDomainConnection().getUrl();
/*    */         }
/*    */         
/* 54 */         JSONObject docObject = new JSONObject();
/* 55 */         docObject.put("docId", IERUtil.getDocId(docItem));
/* 56 */         docObject.put("docName", docItem.getName());
/* 57 */         docObject.put("documentObjectStoreId", documentObjectStoreId);
/* 58 */         docObject.put("ceEJBURL", repoConnUrl);
/* 59 */         responseArray.add(docObject);
/*    */       }
/* 61 */       responseObject.put("documents", responseArray);
/*    */     } else {
/* 63 */       responseObject.put("error", "noDocument");
/*    */     }
/* 65 */     setCompletedJSONResponseObject(responseObject);
/* 66 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetDocumentInfoService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */