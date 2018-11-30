/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.filenet.api.collection.ContentElementList;
/*    */ import com.filenet.api.core.ContentTransfer;
/*    */ import com.filenet.api.core.Document;
/*    */ import com.filenet.api.core.Factory.Document;
/*    */ import com.filenet.api.core.ObjectStore;
/*    */ import com.filenet.api.exception.EngineRuntimeException;
/*    */ import com.filenet.api.property.PropertyFilter;
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.GetObjectStoreSecurityMediator;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.util.P8CE_Convert;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.Iterator;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetObjectStoreSecurityService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 34 */     return "ierGetObjectStoreSecurity";
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator() {
/* 38 */     return new GetObjectStoreSecurityMediator(this.baseService, this.servletRequest);
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 44 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 47 */       String repositoryId = request.getParameter("ier_fileplanRepositoryId");
/* 48 */       IERBaseService service = getBaseService();
/* 49 */       Repository repository = service.getRepository(service.getP8RepositoryId(repositoryId));
/* 50 */       if ((repository instanceof FilePlanRepository)) {
/* 51 */         FilePlanRepository fp_repository = (FilePlanRepository)repository;
/*    */         
/* 53 */         GetObjectStoreSecurityMediator mediator = (GetObjectStoreSecurityMediator)getMediator();
/* 54 */         ObjectStore objStore = P8CE_Convert.fromJARM(fp_repository);
/*    */         try {
/* 56 */           Document doc = Factory.Document.fetchInstance(objStore, "{CA81CDCF-9BBF-40C2-AC21-4C65ACCB3D45}", new PropertyFilter());
/* 57 */           doc = doc != null ? (Document)doc.get_CurrentVersion() : null;
/* 58 */           ContentElementList list = doc.get_ContentElements();
/* 59 */           Iterator iter = list.iterator();
/* 60 */           while (iter.hasNext()) {
/* 61 */             Object o = iter.next();
/* 62 */             if ((o instanceof ContentTransfer)) {
/* 63 */               InputStream is = ((ContentTransfer)o).accessContentStream();
/* 64 */               BufferedReader reader = new BufferedReader(new InputStreamReader(is));
/* 65 */               String line = null;
/* 66 */               while ((line = reader.readLine()) != null) {
/* 67 */                 mediator.addPermission(getBaseService().getP8Connection(), line);
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/*    */         catch (EngineRuntimeException ignored) {}
/*    */       }
/*    */       else {
/* 75 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_REPOSITORY_INVALID_LOC, new Object[] { repository.getName() });
/*    */       }
/*    */     }
/*    */     catch (Exception exp) {
/* 79 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_REPOSITORY_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetObjectStoreSecurityService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */