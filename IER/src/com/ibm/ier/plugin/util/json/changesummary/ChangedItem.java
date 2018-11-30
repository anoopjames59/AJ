/*     */ package com.ibm.ier.plugin.util.json.changesummary;
/*     */ 
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChangedItem
/*     */ {
/*     */   private String id;
/*     */   private String name;
/*     */   private String operation;
/*     */   private String parentId;
/*     */   private String server;
/*     */   private ChangedItem[] subItems;
/*     */   private String type;
/*     */   private Value[] values;
/*     */   
/*     */   public void fromJSONString(String jsonString)
/*     */     throws IOException
/*     */   {
/*  37 */     JSONObject jsonObject = JSONObject.parse(jsonString);
/*     */     
/*     */ 
/*  40 */     this.id = jsonObject.get("id").toString();
/*  41 */     this.name = jsonObject.get("name").toString();
/*  42 */     this.operation = jsonObject.get("operation").toString();
/*  43 */     this.parentId = jsonObject.get("parentId").toString();
/*  44 */     this.server = jsonObject.get("server").toString();
/*  45 */     this.type = jsonObject.get("type").toString();
/*     */     
/*     */ 
/*  48 */     JSONArray subItemArray = (JSONArray)jsonObject.get("subItems");
/*     */     
/*  50 */     this.subItems = new ChangedItem[subItemArray.size()];
/*     */     
/*  52 */     for (int i = 0; i < subItemArray.size(); i++) {
/*  53 */       JSONObject object = (JSONObject)subItemArray.get(i);
/*     */       
/*     */ 
/*  56 */       ChangedItem item = new ChangedItem();
/*  57 */       item.fromJSONString(object.toString());
/*     */       
/*     */ 
/*  60 */       this.subItems[i] = item;
/*     */     }
/*     */     
/*     */ 
/*  64 */     JSONArray valuesArray = (JSONArray)jsonObject.get("values");
/*     */     
/*  66 */     this.values = new Value[valuesArray.size()];
/*     */     
/*  68 */     for (int i = 0; i < valuesArray.size(); i++) {
/*  69 */       JSONObject object = (JSONObject)valuesArray.get(i);
/*     */       
/*     */ 
/*  72 */       Value value = new Value();
/*  73 */       value.fromJSONString(object.toString());
/*     */       
/*     */ 
/*  76 */       this.values[i] = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getId() {
/*  81 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getItemType()
/*     */   {
/*  90 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  94 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isAdded() {
/*  98 */     return getOperation().equals("ADD");
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 102 */     return getOperation().equals("REMOVE");
/*     */   }
/*     */   
/*     */   public boolean isUpdated() {
/* 106 */     return getOperation().equals("UPDATE");
/*     */   }
/*     */   
/*     */   public String getOperation() {
/* 110 */     return this.operation;
/*     */   }
/*     */   
/*     */   public String getParentId() {
/* 114 */     return this.parentId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServer()
/*     */   {
/* 123 */     return this.server;
/*     */   }
/*     */   
/*     */   public ChangedItem[] getSubItems() {
/* 127 */     return this.subItems;
/*     */   }
/*     */   
/*     */   public String getType() {
/* 131 */     return this.type;
/*     */   }
/*     */   
/*     */   public Value[] getValues() {
/* 135 */     return this.values;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 139 */     StringBuffer buffer = new StringBuffer("ChangedItem: {");
/*     */     
/* 141 */     buffer.append("id=\"");
/* 142 */     buffer.append(getId());
/* 143 */     buffer.append("\", ");
/*     */     
/* 145 */     buffer.append("name=\"");
/* 146 */     buffer.append(getName());
/* 147 */     buffer.append("\", ");
/*     */     
/* 149 */     buffer.append("operation=\"");
/* 150 */     buffer.append(getOperation());
/* 151 */     buffer.append("\", ");
/*     */     
/* 153 */     buffer.append("parentId=\"");
/* 154 */     buffer.append(getParentId());
/* 155 */     buffer.append("\", ");
/*     */     
/* 157 */     buffer.append("server=\"");
/* 158 */     buffer.append(getServer());
/* 159 */     buffer.append("\", ");
/*     */     
/* 161 */     buffer.append("type=\"");
/* 162 */     buffer.append(getType());
/* 163 */     buffer.append("\", ");
/*     */     
/*     */ 
/* 166 */     for (int i = 0; i < this.subItems.length; i++) {
/* 167 */       buffer.append(this.subItems[i].toString());
/*     */     }
/*     */     
/*     */ 
/* 171 */     for (int i = 0; i < this.values.length; i++) {
/* 172 */       buffer.append("\n  ");
/* 173 */       buffer.append(this.values[i].toString());
/*     */     }
/*     */     
/* 176 */     buffer.append("\n}\n");
/*     */     
/* 178 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\json\changesummary\ChangedItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */