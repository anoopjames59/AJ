/*    */ package com.ibm.ier.plugin.mediators;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.jarm.api.core.BaseEntity;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SearchTemplateListMediator
/*    */   extends BaseMediator
/*    */ {
/*    */   private static final long serialVersionUID = 134L;
/* 22 */   private List<BaseEntity> searchTemplates = null;
/*    */   
/*    */   public void setSearchTemplates(List<BaseEntity> searchTemplates)
/*    */   {
/* 26 */     this.searchTemplates = searchTemplates;
/*    */   }
/*    */   
/*    */   public JSONObject toJSONObject()
/*    */     throws Exception
/*    */   {
/* 32 */     Logger.logEntry(this, "toJSONObject", this.servletRequest);
/* 33 */     JSONObject jsonObject = super.toJSONObject();
/* 34 */     jsonObject.put("repositoryId", this.baseService.getP8RepositoryId());
/* 35 */     jsonObject.put("num_templates", Integer.valueOf(this.searchTemplates.size()));
/*    */     
/* 37 */     JSONObject datastore = new JSONObject();
/* 38 */     JSONArray items = new JSONArray();
/*    */     
/* 40 */     for (BaseEntity searchTemplate : this.searchTemplates)
/*    */     {
/* 42 */       JSONObject template = new JSONObject();
/* 43 */       RMProperties prop = searchTemplate.getProperties();
/* 44 */       template.put("template_id", prop.getStringValue("Id"));
/* 45 */       template.put("template_name", prop.getStringValue("Id"));
/* 46 */       template.put("template_label", prop.getStringValue("DocumentTitle"));
/* 47 */       template.put("template_desc", prop.getStringValue("Description"));
/*    */       
/* 49 */       boolean mimeTypeSet = false;
/* 50 */       if (prop.isPropertyPresent("IcnAutoRun")) {
/* 51 */         Boolean autoRun = prop.getBooleanValue("IcnAutoRun");
/* 52 */         if ((autoRun != null) && (autoRun.booleanValue() == true)) {
/* 53 */           template.put("mimetype", "application/x-searchtemplate.automatic");
/* 54 */           mimeTypeSet = true;
/*    */         }
/*    */       }
/* 57 */       if (!mimeTypeSet) {
/* 58 */         template.put("mimetype", prop.getStringValue("MimeType"));
/*    */       }
/* 60 */       items.add(template);
/*    */     }
/*    */     
/* 63 */     datastore.put("items", items);
/* 64 */     jsonObject.put("datastore", datastore);
/*    */     
/* 66 */     Logger.logExit(this, "toJSONObject", this.servletRequest);
/*    */     
/* 68 */     return jsonObject;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\SearchTemplateListMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */