/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class Comment
/*     */ {
/*     */   private String id;
/*     */   private String commentText;
/*     */   private Date dateAdded;
/*     */   private String originator;
/*     */   private String originatorDisplayName;
/*     */   private String itemVersion;
/*     */   private int itemVersionStatus;
/*     */   private String itemVersionStatusLabel;
/*     */   private boolean itemCurrentVersion;
/*     */   private boolean editable;
/*     */   private boolean deletable;
/*     */   
/*     */   public String getId()
/*     */   {
/*  23 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  27 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getCommentText() {
/*  31 */     return this.commentText;
/*     */   }
/*     */   
/*     */   public void setCommentText(String commentText) {
/*  35 */     this.commentText = commentText;
/*     */   }
/*     */   
/*     */   public Date getDateAdded() {
/*  39 */     return this.dateAdded;
/*     */   }
/*     */   
/*     */   public void setDateAdded(Date dateAdded) {
/*  43 */     this.dateAdded = dateAdded;
/*     */   }
/*     */   
/*     */   public String getOriginator() {
/*  47 */     return this.originator;
/*     */   }
/*     */   
/*     */   public void setOriginator(String originator) {
/*  51 */     this.originator = originator;
/*     */   }
/*     */   
/*     */   public String getOriginatorDisplayName() {
/*  55 */     return this.originatorDisplayName;
/*     */   }
/*     */   
/*     */   public void setOriginatorDisplayName(String originatorDisplayName) {
/*  59 */     this.originatorDisplayName = originatorDisplayName;
/*     */   }
/*     */   
/*     */   public String getItemVersion() {
/*  63 */     return this.itemVersion;
/*     */   }
/*     */   
/*     */   public void setItemVersion(String itemVersion) {
/*  67 */     this.itemVersion = itemVersion;
/*     */   }
/*     */   
/*     */   public int getItemVersionStatus() {
/*  71 */     return this.itemVersionStatus;
/*     */   }
/*     */   
/*     */   public void setItemVersionStatus(int versionStatus) {
/*  75 */     this.itemVersionStatus = versionStatus;
/*     */   }
/*     */   
/*     */   public String getItemVersionStatusLabel() {
/*  79 */     return this.itemVersionStatusLabel;
/*     */   }
/*     */   
/*     */   public void setItemVersionStatusLabel(String itemVersionStatusLabel) {
/*  83 */     this.itemVersionStatusLabel = itemVersionStatusLabel;
/*     */   }
/*     */   
/*     */   public boolean isItemCurrentVersion() {
/*  87 */     return this.itemCurrentVersion;
/*     */   }
/*     */   
/*     */   public void setItemCurrentVersion(boolean currentVersion) {
/*  91 */     this.itemCurrentVersion = currentVersion;
/*     */   }
/*     */   
/*     */   public boolean isEditable() {
/*  95 */     return this.editable;
/*     */   }
/*     */   
/*     */   public void setEditable(boolean editable) {
/*  99 */     this.editable = editable;
/*     */   }
/*     */   
/*     */   public boolean isDeletable() {
/* 103 */     return this.deletable;
/*     */   }
/*     */   
/*     */   public void setDeletable(boolean deletable) {
/* 107 */     this.deletable = deletable;
/*     */   }
/*     */   
/*     */   public JSONObject toJSONObject() {
/* 111 */     JSONObject jsonAttributes = new JSONObject();
/* 112 */     JSONArray jsonAttributeInfo = new JSONArray();
/* 113 */     jsonAttributeInfo.add(getCommentText());
/* 114 */     jsonAttributeInfo.add("xs:string");
/* 115 */     jsonAttributes.put("commentText", jsonAttributeInfo);
/* 116 */     jsonAttributeInfo = new JSONArray();
/* 117 */     jsonAttributeInfo.add(getItemVersion());
/* 118 */     jsonAttributeInfo.add("xs:string");
/* 119 */     jsonAttributes.put("itemVersion", jsonAttributeInfo);
/* 120 */     jsonAttributeInfo = new JSONArray();
/* 121 */     jsonAttributeInfo.add(Integer.valueOf(getItemVersionStatus()));
/* 122 */     jsonAttributeInfo.add("xs:integer");
/* 123 */     jsonAttributeInfo.add(null);
/* 124 */     jsonAttributeInfo.add(getItemVersionStatusLabel());
/* 125 */     jsonAttributes.put("itemVersionStatus", jsonAttributeInfo);
/* 126 */     jsonAttributeInfo = new JSONArray();
/* 127 */     jsonAttributeInfo.add(Boolean.valueOf(isItemCurrentVersion()));
/* 128 */     jsonAttributeInfo.add("xs:boolean");
/* 129 */     jsonAttributes.put("itemCurrentVersion", jsonAttributeInfo);
/* 130 */     jsonAttributeInfo = new JSONArray();
/* 131 */     jsonAttributeInfo.add(getOriginator());
/* 132 */     jsonAttributeInfo.add("xs:string");
/* 133 */     jsonAttributeInfo.add(null);
/* 134 */     jsonAttributeInfo.add(getOriginatorDisplayName());
/* 135 */     jsonAttributes.put("originator", jsonAttributeInfo);
/* 136 */     jsonAttributeInfo = new JSONArray();
/* 137 */     jsonAttributeInfo.add(DateUtil.getISODateString(getDateAdded(), true));
/* 138 */     jsonAttributeInfo.add("xs:timestamp");
/* 139 */     jsonAttributes.put("dateAdded", jsonAttributeInfo);
/*     */     
/* 141 */     JSONObject jsonObject = new JSONObject();
/* 142 */     jsonObject.put("id", getId());
/* 143 */     jsonObject.put("attributes", jsonAttributes);
/* 144 */     jsonObject.put("commentEditable", Boolean.valueOf(isEditable()));
/* 145 */     jsonObject.put("commentDeletable", Boolean.valueOf(isDeletable()));
/*     */     
/* 147 */     return jsonObject;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\Comment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */