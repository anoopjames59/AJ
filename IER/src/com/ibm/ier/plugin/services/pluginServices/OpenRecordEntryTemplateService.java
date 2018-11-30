/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.RecordEntryTemplateMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.core.ContentItem;
/*    */ import com.ibm.jarm.api.core.RMFactory.ContentItem;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
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
/*    */ public class OpenRecordEntryTemplateService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 31 */     return "ierOpenRecordEntryTemplate";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator() {
/* 35 */     return new RecordEntryTemplateMediator();
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 40 */     String methodName = "executeAction";
/* 41 */     Logger.logEntry(this, methodName, request);
/*    */     
/* 43 */     RecordEntryTemplateMediator mediator = (RecordEntryTemplateMediator)getMediator();
/* 44 */     String templateId = request.getParameter("template_name");
/*    */     
/* 46 */     if (templateId != null) {
/* 47 */       ContentItem entryTemplate = RMFactory.ContentItem.fetchInstance(getRepository(), IERUtil.getIdFromDocIdString(templateId), RMPropertyFilter.MinimumPropertySet);
/* 48 */       mediator.setRecordEntryTemplateBaseEntity(entryTemplate);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\OpenRecordEntryTemplateService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */