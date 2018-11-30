/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.core.ContentItem;
/*    */ import com.ibm.jarm.api.core.ContentRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.ContentItem;
/*    */ import com.ibm.jarm.api.core.Record;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.json.java.JSONObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetRecordFromDeclaredDocumentService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 57 */     return "ierGetRecordFromDeclaredDocumentService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 61 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 63 */     String docId = request.getParameter("ier_docId");
/* 64 */     ContentRepository content_repository = (ContentRepository)getRepository();
/*    */     
/* 66 */     ContentItem contentItem = RMFactory.ContentItem.fetchInstance(content_repository, IERUtil.getIdFromDocIdString(docId), RMPropertyFilter.MinimumPropertySet);
/* 67 */     Record record = contentItem.getAssociatedRecord();
/*    */     
/* 69 */     JSONObject responseObject = new JSONObject();
/* 70 */     if (record != null) {
/* 71 */       responseObject.put("recordId", IERUtil.getDocId(record));
/* 72 */       Repository fp_repository = record.getRepository();
/* 73 */       responseObject.put("fileplanObjectStore", fp_repository.getSymbolicName());
/*    */     }
/* 75 */     setCompletedJSONResponseObject(responseObject);
/*    */     
/* 77 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetRecordFromDeclaredDocumentService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */