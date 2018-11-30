/*    */ package com.ibm.ier.plugin.mediators;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecordEntryTemplateListMediator
/*    */   extends BaseMediator
/*    */ {
/*    */   private static final long serialVersionUID = 133L;
/* 30 */   private List<BaseEntity> recordEntryTemplates = null;
/*    */   
/*    */   public void setRecordEntryTemplates(List<BaseEntity> recordEntryTemplates) {
/* 33 */     this.recordEntryTemplates = recordEntryTemplates;
/*    */   }
/*    */   
/*    */   public JSONObject toJSONObject() throws Exception {
/* 37 */     Logger.logEntry(this, "toJSONObject", this.servletRequest);
/*    */     
/* 39 */     JSONObject jsonObject = super.toJSONObject();
/*    */     
/* 41 */     jsonObject.put("repositoryId", this.baseService.getP8RepositoryId());
/* 42 */     jsonObject.put("num_templates", new Integer(this.recordEntryTemplates.size()));
/* 43 */     JSONObject items = new JSONObject();
/* 44 */     JSONArray templates = new JSONArray();
/*    */     
/* 46 */     for (BaseEntity recordEntryTemplate : this.recordEntryTemplates) {
/* 47 */       JSONObject templateJSON = new JSONObject();
/* 48 */       RMProperties props = recordEntryTemplate.getProperties();
/* 49 */       templateJSON.put("template_id", IERUtil.getDocId(recordEntryTemplate));
/* 50 */       templateJSON.put("template_name", recordEntryTemplate.getName());
/* 51 */       templateJSON.put("template_desc", props.getStringValue("EntryTemplateDescription"));
/* 52 */       templateJSON.put("objectStore", props.getStringValue("TargetObjectStoreName"));
/* 53 */       templates.add(templateJSON);
/*    */     }
/* 55 */     items.put("items", templates);
/* 56 */     jsonObject.put("datastore", items);
/*    */     
/* 58 */     Logger.logExit(this, "toJSONObject", this.servletRequest);
/* 59 */     return jsonObject;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\RecordEntryTemplateListMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */