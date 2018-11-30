/*     */ package com.ibm.ier.plugin.services.responseFilters;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBaseResponseFilterService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.jarm.api.constants.RepositoryType;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Iterator;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8OpenFolderOverride
/*     */   extends IERBaseResponseFilterService
/*     */ {
/*  21 */   private HttpServletRequest request = null;
/*  22 */   private JSONObject jsonResponse = null;
/*     */   
/*     */   public String[] getFilteredServices()
/*     */   {
/*  26 */     return new String[] { "/p8/openFolder" };
/*     */   }
/*     */   
/*     */   public void filterServiceExecute(HttpServletRequest request, JSONObject jsonResponse) throws Exception
/*     */   {
/*     */     try {
/*  32 */       this.request = request;
/*  33 */       this.jsonResponse = jsonResponse;
/*     */       
/*     */ 
/*  36 */       removeNonFilePlanFolders();
/*     */       
/*  38 */       if (!getBaseService().isIERDesktop())
/*     */       {
/*  40 */         removeRecordsManagementFolder();
/*     */       }
/*     */     }
/*     */     catch (Exception exp) {
/*  44 */       Logger.logError(this, "filterserviceExecute", request, exp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeRecordsManagementFolder()
/*     */     throws Exception
/*     */   {
/*  52 */     String folderDocId = this.request.getParameter("docid");
/*     */     
/*  54 */     if (folderDocId != null) {
/*  55 */       String folderId = IERUtil.getIdFromDocIdString(folderDocId);
/*  56 */       if ((folderId != null) && (folderId.equals("{0F1E2D3C-4B5A-6978-8796-A5B4C3D2E1F0}")))
/*     */       {
/*  58 */         Repository repository = getRepository();
/*  59 */         if ((repository != null) && ((repository.getRepositoryType() == RepositoryType.FilePlan) || (repository.getRepositoryType() == RepositoryType.Combined))) {
/*  60 */           JSONArray rows = (JSONArray)this.jsonResponse.get("rows");
/*     */           
/*  62 */           Iterator items_it = rows.iterator();
/*  63 */           while (items_it.hasNext())
/*     */           {
/*  65 */             JSONObject item = (JSONObject)items_it.next();
/*  66 */             String itemClass = item.get("template").toString();
/*     */             
/*  68 */             if (itemClass.equals("ClassificationSchemes")) {
/*  69 */               items_it.remove();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeNonFilePlanFolders()
/*     */     throws Exception
/*     */   {
/*  83 */     String folderDocId = this.request.getParameter("docid");
/*     */     
/*     */ 
/*  86 */     if (folderDocId != null) {
/*  87 */       String folderId = IERUtil.getIdFromDocIdString(folderDocId);
/*  88 */       if ((folderId != null) && (folderId.equals("{56EA3C34-4084-4709-BBE6-A1B9E35C3E7B}")))
/*     */       {
/*  90 */         Repository repository = getRepository();
/*  91 */         if ((repository != null) && ((repository.getRepositoryType() == RepositoryType.FilePlan) || (repository.getRepositoryType() == RepositoryType.Combined))) {
/*  92 */           JSONArray rows = (JSONArray)this.jsonResponse.get("rows");
/*     */           
/*     */ 
/*  95 */           Iterator items_it = rows.iterator();
/*  96 */           while (items_it.hasNext())
/*     */           {
/*  98 */             JSONObject item = (JSONObject)items_it.next();
/*  99 */             String itemClass = item.get("template").toString();
/*     */             
/* 101 */             if (!itemClass.equals("ClassificationScheme")) {
/* 102 */               items_it.remove();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\responseFilters\P8OpenFolderOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */