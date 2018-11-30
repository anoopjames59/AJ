/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.jarm.api.constants.DomainType;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMLink;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMProperties;
/*    */ import com.ibm.jarm.api.core.RMFactory.Record;
/*    */ import com.ibm.jarm.api.core.RMLink;
/*    */ import com.ibm.jarm.api.core.Record;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LinkRecordService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 29 */     return "ierLinkRecordService";
/*    */   }
/*    */   
/*    */ 
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 36 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     try
/*    */     {
/* 43 */       String recordId = request.getParameter("ier_recordId");
/* 44 */       String linkedRecordId = request.getParameter("ier_linkedRecordId");
/* 45 */       String linkName = request.getParameter("template_name");
/* 46 */       String description = request.getParameter("template_desc");
/* 47 */       String linkClass = request.getParameter("ier_linkClass");
/* 48 */       String linkReasonForExtract = request.getParameter("ier_linkReasonForExtract");
/*    */       
/* 50 */       recordId = recordId.substring(recordId.lastIndexOf("{"), recordId.lastIndexOf("}") + 1);
/* 51 */       linkedRecordId = linkedRecordId.substring(linkedRecordId.lastIndexOf("{"), linkedRecordId.lastIndexOf("}") + 1);
/*    */       
/* 53 */       RMProperties recProps = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/*    */       
/*    */ 
/* 56 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*    */       
/*    */ 
/* 59 */       RMLink newRMLink = RMFactory.RMLink.createInstance(fp_repository, linkClass);
/* 60 */       RMProperties rmLinkProps = newRMLink.getProperties();
/*    */       
/*    */ 
/* 63 */       Record jarmHeadTargetObj = RMFactory.Record.getInstance(fp_repository, "{36C8FC8A-D984-41E8-AF92-7D77EF2CBC2F}", EntityType.Record);
/* 64 */       Record jarmTailTargetObj = RMFactory.Record.getInstance(fp_repository, recordId, EntityType.Record);
/*    */       
/*    */ 
/* 67 */       rmLinkProps.putObjectValue("Head", jarmHeadTargetObj);
/* 68 */       rmLinkProps.putObjectValue("Tail", jarmTailTargetObj);
/* 69 */       rmLinkProps.putStringValue("LinkName", linkName);
/* 70 */       rmLinkProps.putStringValue("RMEntityDescription", description);
/* 71 */       if (null != linkReasonForExtract)
/* 72 */         rmLinkProps.putStringValue("Reason", linkReasonForExtract);
/* 73 */       newRMLink.save(RMRefreshMode.Refresh);
/*    */     }
/*    */     catch (Exception exp)
/*    */     {
/* 77 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_LINKRECORD_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 80 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\LinkRecordService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */