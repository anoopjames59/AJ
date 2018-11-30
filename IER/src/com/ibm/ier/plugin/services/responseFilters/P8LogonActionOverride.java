/*    */ package com.ibm.ier.plugin.services.responseFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*    */ import com.ibm.ier.plugin.mediators.RepositoryAttributesMediator;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseResponseFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.PrivilegesUtil;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
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
/*    */ public class P8LogonActionOverride
/*    */   extends IERBaseResponseFilterService
/*    */ {
/*    */   public String[] getFilteredServices()
/*    */   {
/* 32 */     return new String[] { "/logon" };
/*    */   }
/*    */   
/*    */   public void filterServiceExecute(HttpServletRequest request, JSONObject jsonResponse) throws Exception
/*    */   {
/* 37 */     Logger.logEntry(this, "filterServiceExecute", request);
/*    */     
/* 39 */     Logger.logDebug(this, "filterServiceExecute", request, "Attempting to do p8 login action overriding");
/* 40 */     if (getBaseService().isIERDesktop()) {
/*    */       try {
/* 42 */         String repositoryId = this.baseService.getNexusRepositoryId();
/* 43 */         if (repositoryId == null) {
/* 44 */           repositoryId = this.baseService.getAuthenticatingRepository();
/*    */         }
/* 46 */         JSONArray servers = (JSONArray)jsonResponse.get("servers");
/* 47 */         for (int i = 0; i < servers.size(); i++) {
/* 48 */           JSONObject repository = (JSONObject)servers.get(i);
/* 49 */           String repoId = (String)repository.get("repositoryId");
/* 50 */           if (repoId.equals(repositoryId)) {
/* 51 */             Object obj = repository.get("attributes");
/* 52 */             JSONObject attributes = null;
/* 53 */             if (obj != null) {
/* 54 */               attributes = (JSONObject)obj;
/*    */             }
/*    */             else {
/* 57 */               attributes = new JSONObject();
/* 58 */               repository.put("attributes", attributes);
/*    */             }
/*    */             
/* 61 */             Repository repo = this.baseService.getRepositoryFromNexusRepositoryId(repositoryId);
/* 62 */             if ((repo instanceof FilePlanRepository)) {
/* 63 */               FilePlanRepository fp_repo = (FilePlanRepository)repo;
/* 64 */               if (PrivilegesUtil.isRecordsAdministratorAndManager(fp_repo, this.servletRequest)) {
/* 65 */                 attributes.put("isRecordsAdministratorAndManager", Boolean.valueOf(true));
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/*    */         
/* 71 */         RepositoryAttributesMediator mediator = (RepositoryAttributesMediator)getMediator();
/* 72 */         mediator.setCompletedJSONResponseObject(jsonResponse);
/* 73 */         mediator.setIsLogin(true);
/* 74 */         jsonResponse = mediator.toJSONObject();
/*    */       }
/*    */       catch (Exception exp) {
/* 77 */         Logger.logError(this, "filterServiceExecute", request, exp.getMessage());
/*    */       }
/*    */     }
/*    */     
/* 81 */     Logger.logExit(this, "filterServiceExecute", request);
/*    */   }
/*    */   
/*    */   protected BaseMediator createMediator()
/*    */   {
/* 86 */     return new RepositoryAttributesMediator();
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\responseFilters\P8LogonActionOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */