/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.Container;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.RecordContainer;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReopenRecordContainerService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 28 */     return "ierReopenRecordContainerService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 32 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 35 */       String containerId = request.getParameter("ier_containerid");
/*    */       
/*    */ 
/* 38 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*    */       
/*    */ 
/* 41 */       containerId = containerId.split(",")[2];
/*    */       
/*    */ 
/* 44 */       Container container = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, containerId, null);
/* 45 */       if ((container instanceof RecordContainer))
/*    */       {
/* 47 */         RecordContainer rc = (RecordContainer)container;
/* 48 */         rc.reopen();
/*    */         
/*    */ 
/* 51 */         setCompletedJSONResponseObject(MediatorUtil.createEntityItemJSONObject(rc, MinimumPropertiesUtil.getPropertySetList(rc.getEntityType()), this.servletRequest));
/*    */       }
/*    */       else
/*    */       {
/* 55 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_CATOPEN_INVALID_CONTAINER, new Object[] { container.getName() });
/*    */       }
/*    */     } catch (Exception exp) {
/* 58 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_CATOPEN_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */     
/* 61 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\ReopenRecordContainerService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */