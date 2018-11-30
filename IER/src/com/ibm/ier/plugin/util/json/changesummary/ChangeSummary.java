/*     */ package com.ibm.ier.plugin.util.json.changesummary;
/*     */ 
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChangeSummary
/*     */ {
/*     */   private ChangedItem[] changedItems;
/*     */   
/*     */   public ChangeSummary() {}
/*     */   
/*     */   public ChangeSummary(String jsonString)
/*     */     throws IOException
/*     */   {
/*  30 */     fromJSONString(jsonString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fromJSONString(String jsonString)
/*     */     throws IOException
/*     */   {
/*  41 */     JSONObject jsonObject = JSONObject.parse(jsonString);
/*     */     
/*     */ 
/*  44 */     JSONArray array = (JSONArray)jsonObject.get("changedItems");
/*     */     
/*  46 */     this.changedItems = new ChangedItem[array.size()];
/*     */     
/*  48 */     for (int i = 0; i < array.size(); i++) {
/*  49 */       JSONObject object = (JSONObject)array.get(i);
/*     */       
/*     */ 
/*  52 */       ChangedItem item = new ChangedItem();
/*  53 */       item.fromJSONString(object.toString());
/*     */       
/*     */ 
/*  56 */       this.changedItems[i] = item;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChangedItem[] getAddedItems()
/*     */   {
/*  66 */     return getItemsByOperation("ADD");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChangedItem[] getChangedItems()
/*     */   {
/*  77 */     return this.changedItems;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChangedItem[] getItemsByOperation(String operation)
/*     */   {
/*  88 */     List itemList = new ArrayList();
/*     */     
/*  90 */     for (int i = 0; i < this.changedItems.length; i++) {
/*  91 */       ChangedItem item = this.changedItems[i];
/*     */       
/*  93 */       if (item.getOperation().equals(operation)) {
/*  94 */         itemList.add(item);
/*     */       }
/*     */     }
/*     */     
/*  98 */     return (ChangedItem[])itemList.toArray(new ChangedItem[itemList.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChangedItem[] getRemovedItems()
/*     */   {
/* 107 */     return getItemsByOperation("REMOVE");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChangedItem[] getUpdatedItems()
/*     */   {
/* 116 */     return getItemsByOperation("UPDATE");
/*     */   }
/*     */   
/*     */   public String toString() {
/* 120 */     StringBuffer buffer = new StringBuffer("");
/*     */     
/*     */ 
/* 123 */     for (int i = 0; i < this.changedItems.length; i++) {
/* 124 */       buffer.append(this.changedItems[i].toString());
/*     */     }
/*     */     
/* 127 */     return buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 138 */     String jsonString = "{\"className\":\"ChangeHistory\",\"changedItems\":[{\"className\":\"ChangedItem\",\"name\":\"\",\"id\":\"\",\"server\":\"ICMNLSDB\",\"type\":\"\",\"parentId\":\"jwt_base_model;HasChildComp;92 3 ICM8 ICMNLSDB12 HasChildComp59 26 A1001001A06E24B40036H2300418 A06E24B40036H230041 14 1013;Books\",\"operation\":\"ADD\",\"subItems\":[],\"values\":[{\"className\":\"Value\",\"id\":\"editForm_Author_1150923352464\",\"name\":\"Author\",\"value\":\"Author\"},{\"className\":\"Value\",\"id\":\"editForm_TItle_1150923352464\",\"name\":\"TItle\",\"value\":\"TItle\"},{\"className\":\"Value\",\"id\":\"editForm_Subject_1150923352464\",\"name\":\"Subject\",\"value\":\"Subject\"}]}]}";
/*     */     
/* 140 */     ChangeSummary summary = new ChangeSummary();
/*     */     try
/*     */     {
/* 143 */       summary.fromJSONString(jsonString);
/*     */       
/* 145 */       System.out.println(summary.toString());
/*     */     }
/*     */     catch (IOException e) {}
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\json\changesummary\ChangeSummary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */