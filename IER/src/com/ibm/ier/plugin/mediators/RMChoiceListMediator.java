/*    */ package com.ibm.ier.plugin.mediators;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.jarm.api.constants.ChoiceItemType;
/*    */ import com.ibm.jarm.api.meta.RMChoiceItem;
/*    */ import com.ibm.jarm.api.meta.RMChoiceList;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ 
/*    */ public class RMChoiceListMediator
/*    */   extends BaseMediator
/*    */ {
/*    */   private static final long serialVersionUID = 134L;
/* 16 */   private RMChoiceList choiceList = null;
/*    */   
/*    */   public void setChoiceList(RMChoiceList choiceList) {
/* 19 */     this.choiceList = choiceList;
/*    */   }
/*    */   
/*    */   public JSONObject toJSONObject() throws Exception
/*    */   {
/* 24 */     Logger.logEntry(this, "toJSONObject", this.servletRequest);
/* 25 */     JSONObject jsonObj = super.toJSONObject();
/*    */     
/* 27 */     jsonObj.put("repositoryId", this.baseService.getP8RepositoryId());
/* 28 */     jsonObj.put("num_results", Integer.valueOf(this.choiceList == null ? 0 : this.choiceList.getChoiceListValues().size()));
/* 29 */     JSONObject items = new JSONObject();
/* 30 */     JSONArray choiceListJSON = new JSONArray();
/*    */     
/* 32 */     if (this.choiceList != null) {
/* 33 */       List<RMChoiceItem> choiceItems = this.choiceList.getChoiceListValues();
/* 34 */       for (RMChoiceItem item : choiceItems) {
/* 35 */         JSONObject itemJSON = new JSONObject();
/* 36 */         itemJSON.put("name", item.getDisplayName());
/*    */         
/* 38 */         ChoiceItemType type = item.getChoiceItemType();
/* 39 */         if (type == ChoiceItemType.Integer) {
/* 40 */           itemJSON.put("fixedValue", item.getChoiceIntegerValue());
/* 41 */         } else if (type == ChoiceItemType.String) {
/* 42 */           itemJSON.put("fixedValue", item.getChoiceStringValue());
/*    */         }
/* 44 */         choiceListJSON.add(itemJSON);
/*    */       }
/*    */     }
/*    */     
/* 48 */     items.put("items", choiceListJSON);
/* 49 */     jsonObj.put("datastore", items);
/* 50 */     Logger.logExit(this, "toJSONObject", this.servletRequest);
/*    */     
/* 52 */     return jsonObj;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\RMChoiceListMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */