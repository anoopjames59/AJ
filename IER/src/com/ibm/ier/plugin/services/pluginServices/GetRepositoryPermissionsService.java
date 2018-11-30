/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.filenet.api.collection.AccessPermissionList;
/*    */ import com.filenet.api.core.Connection;
/*    */ import com.filenet.api.core.Domain;
/*    */ import com.filenet.api.core.Factory.Domain;
/*    */ import com.filenet.api.core.Factory.ObjectStore;
/*    */ import com.filenet.api.core.ObjectStore;
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.GetObjectStoreSecurityMediator;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetRepositoryPermissionsService extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 24 */     return "ierGetRepositoryPermissions";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator() {
/* 28 */     return new GetObjectStoreSecurityMediator(this.baseService, this.servletRequest);
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 33 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 36 */       String repositoryId = request.getParameter("ier_fileplanRepositoryId");
/*    */       
/* 38 */       IERBaseService service = getBaseService();
/* 39 */       Repository repository = service.getRepository(service.getP8RepositoryId(repositoryId));
/* 40 */       Connection conn = service.getP8Connection();
/*    */       
/* 42 */       GetObjectStoreSecurityMediator mediator = (GetObjectStoreSecurityMediator)getMediator();
/* 43 */       Domain domain = Factory.Domain.fetchInstance(conn, null, null);
/* 44 */       String reposIdent = repository.getSymbolicName();
/* 45 */       ObjectStore os = Factory.ObjectStore.fetchInstance(domain, reposIdent, null);
/* 46 */       AccessPermissionList apl = os.get_Permissions();
/* 47 */       mediator.setAccessPermissionList(conn, apl);
/*    */     }
/*    */     catch (Exception exp) {
/* 50 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_FILEPLAN_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetRepositoryPermissionsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */