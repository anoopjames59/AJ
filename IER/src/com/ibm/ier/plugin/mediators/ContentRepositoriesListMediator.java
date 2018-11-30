/*    */ package com.ibm.ier.plugin.mediators;
/*    */ 
/*    */ import com.filenet.api.core.ObjectStore;
/*    */ import com.filenet.api.property.Properties;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.jarm.api.core.ContentRepository;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.jarm.api.util.P8CE_Convert;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ContentRepositoriesListMediator extends BaseMediator
/*    */ {
/*    */   private static final long serialVersionUID = 150L;
/* 17 */   private List<ContentRepository> contentRepoList = null;
/*    */   
/*    */   public void setContentRepositoryList(List<ContentRepository> rosList) {
/* 20 */     this.contentRepoList = rosList;
/*    */   }
/*    */   
/*    */   public JSONObject toJSONObject() throws Exception
/*    */   {
/* 25 */     Logger.logEntry(this, "toJSONObject", this.servletRequest);
/* 26 */     JSONObject jsonObj = super.toJSONObject();
/*    */     
/* 28 */     jsonObj.put("repositoryId", this.baseService.getP8RepositoryId());
/* 29 */     jsonObj.put("num_results", Integer.valueOf(this.contentRepoList == null ? 0 : this.contentRepoList.size()));
/* 30 */     JSONObject items = new JSONObject();
/* 31 */     JSONArray contentReposJSONArray = new JSONArray();
/*    */     
/* 33 */     if (this.contentRepoList != null) {
/* 34 */       for (ContentRepository contentRepo : this.contentRepoList) {
/* 35 */         JSONObject itemJSON = new JSONObject();
/* 36 */         itemJSON.put("name", contentRepo.getSymbolicName());
/* 37 */         itemJSON.put("displayName", contentRepo.getDisplayName());
/*    */         
/* 39 */         Integer cbrSearchType = Integer.valueOf(0);
/*    */         try {
/* 41 */           cbrSearchType = contentRepo.getProperties().getIntegerValue("CBRSearchType");
/*    */         }
/*    */         catch (Exception ignored) {
/* 44 */           cbrSearchType = Integer.valueOf(0);
/*    */         }
/* 46 */         itemJSON.put("CBRSearchType", cbrSearchType);
/*    */         try
/*    */         {
/* 49 */           ObjectStore objStore = P8CE_Convert.fromJARM(contentRepo);
/* 50 */           if (!objStore.getProperties().isPropertyPresent("SchemaVersion"))
/* 51 */             objStore.fetchProperties(new String[] { "SchemaVersion" });
/* 52 */           boolean ifCE52OrAbove = com.ibm.ier.plugin.search.p8.P8Util.ifCE52OrAbove(objStore);
/* 53 */           if (ifCE52OrAbove) {
/* 54 */             Integer cbrQueryOptimization = Integer.valueOf(0);
/*    */             try {
/* 56 */               cbrQueryOptimization = contentRepo.getProperties().getIntegerValue("CBRQueryOptimization");
/*    */             }
/*    */             catch (Exception ignored) {
/* 59 */               cbrQueryOptimization = Integer.valueOf(0);
/*    */             }
/* 61 */             itemJSON.put("CBRQueryOptimization", cbrQueryOptimization);
/*    */             
/* 63 */             Integer cbrQueryRankOverride = Integer.valueOf(0);
/*    */             try {
/* 65 */               cbrQueryRankOverride = contentRepo.getProperties().getIntegerValue("CBRQueryRankOverride");
/*    */             }
/*    */             catch (Exception ignored) {
/* 68 */               cbrQueryRankOverride = Integer.valueOf(0);
/*    */             }
/* 70 */             itemJSON.put("CBRQueryRankOverride", cbrQueryRankOverride);
/*    */           }
/*    */         }
/*    */         catch (Exception ignored)
/*    */         {
/* 75 */           Logger.logError(this, "toJSONObject", ignored);
/*    */         }
/*    */         
/* 78 */         contentReposJSONArray.add(itemJSON);
/*    */       }
/*    */     }
/* 81 */     items.put("items", contentReposJSONArray);
/* 82 */     jsonObj.put("datastore", items);
/* 83 */     Logger.logExit(this, "toJSONObject", this.servletRequest);
/*    */     
/* 85 */     return jsonObj;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\ContentRepositoriesListMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */