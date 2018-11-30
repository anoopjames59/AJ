/*     */ package com.ibm.ier.ddcp.entity;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class RecordEntity
/*     */   implements Serializable
/*     */ {
/*  17 */   private String recordID = null;
/*  18 */   private String recordName = null;
/*  19 */   private String description = null;
/*  20 */   private String reviewer = null;
/*  21 */   private String parentPath = null;
/*  22 */   private String parentContainerID = null;
/*  23 */   private String filePlan = null;
/*  24 */   private String retentionTriggerName = null;
/*  25 */   private String retentionTriggerValue = null;
/*  26 */   private String retentionPeriod = null;
/*  27 */   private String fiscalYearEndDate = null;
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
/*     */   public String getRecordID()
/*     */   {
/*  41 */     return this.recordID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRecordID(String recordID)
/*     */   {
/*  49 */     this.recordID = recordID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRecordName()
/*     */   {
/*  57 */     return this.recordName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRecordName(String recordName)
/*     */   {
/*  65 */     this.recordName = recordName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/*  73 */     return this.description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String description)
/*     */   {
/*  81 */     this.description = description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReviewer()
/*     */   {
/*  89 */     return this.reviewer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReviewer(String reviewer)
/*     */   {
/*  97 */     this.reviewer = reviewer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParentPath()
/*     */   {
/* 105 */     return this.parentPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParentPath(String parentPath)
/*     */   {
/* 113 */     this.parentPath = parentPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParentContainerID()
/*     */   {
/* 121 */     return this.parentContainerID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParentContainerID(String parentContainerID)
/*     */   {
/* 129 */     this.parentContainerID = parentContainerID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFilePlan()
/*     */   {
/* 137 */     return this.filePlan;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFilePlan(String filePlan)
/*     */   {
/* 145 */     this.filePlan = filePlan;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRetentionTriggerName()
/*     */   {
/* 153 */     return this.retentionTriggerName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionTriggerName(String retentionTriggerName)
/*     */   {
/* 161 */     this.retentionTriggerName = retentionTriggerName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRetentionTriggerValue()
/*     */   {
/* 169 */     return this.retentionTriggerValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionTriggerValue(String retentionTriggerValue)
/*     */   {
/* 177 */     this.retentionTriggerValue = retentionTriggerValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRetentionPeriod()
/*     */   {
/* 185 */     return this.retentionPeriod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionPeriod(String retentionPeriod)
/*     */   {
/* 193 */     this.retentionPeriod = retentionPeriod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFiscalYearEndDate()
/*     */   {
/* 201 */     return this.fiscalYearEndDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFiscalYearEndDate(String fiscalYearEndDate)
/*     */   {
/* 209 */     this.fiscalYearEndDate = fiscalYearEndDate;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\entity\RecordEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */