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
/*     */ 
/*     */ public class DDContainerEntity
/*     */   implements Serializable
/*     */ {
/*  18 */   private String containerId = null;
/*  19 */   private String containerName = null;
/*  20 */   private String containerType = null;
/*  21 */   private String pathName = null;
/*  22 */   private String filePlanName = null;
/*  23 */   private String reviewer = null;
/*  24 */   private String retentionTriggerName = null;
/*  25 */   private String retentionPeriod = null;
/*  26 */   private int retentionYears = 0;
/*  27 */   private int retentionMonths = 0;
/*  28 */   private int retentionDays = 0;
/*  29 */   private String fiscalYearEndDate = null;
/*  30 */   private int fiscalYearEndMonth = 0;
/*  31 */   private int fiscalYearEndDay = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFilePlanName()
/*     */   {
/*  38 */     return this.filePlanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFilePlanName(String filePlanName)
/*     */   {
/*  46 */     this.filePlanName = filePlanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRetentionYears()
/*     */   {
/*  54 */     return this.retentionYears;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionYears(int retentionYears)
/*     */   {
/*  62 */     this.retentionYears = retentionYears;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRetentionMonths()
/*     */   {
/*  70 */     return this.retentionMonths;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionMonths(int retentionMonths)
/*     */   {
/*  78 */     this.retentionMonths = retentionMonths;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRetentionDays()
/*     */   {
/*  86 */     return this.retentionDays;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionDays(int retentionDays)
/*     */   {
/*  94 */     this.retentionDays = retentionDays;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFiscalYearEndMonth()
/*     */   {
/* 102 */     return this.fiscalYearEndMonth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFiscalYearEndMonth(int fiscalYearEndMonth)
/*     */   {
/* 110 */     this.fiscalYearEndMonth = fiscalYearEndMonth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFiscalYearEndDay()
/*     */   {
/* 118 */     return this.fiscalYearEndDay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFiscalYearEndDay(int fiscalYearEndDay)
/*     */   {
/* 126 */     this.fiscalYearEndDay = fiscalYearEndDay;
/*     */   }
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
/*     */   public String getContainerId()
/*     */   {
/* 141 */     return this.containerId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainerId(String containerId)
/*     */   {
/* 149 */     this.containerId = containerId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContainerName()
/*     */   {
/* 157 */     return this.containerName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainerName(String containerName)
/*     */   {
/* 165 */     this.containerName = containerName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContainerType()
/*     */   {
/* 173 */     return this.containerType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainerType(String containerType)
/*     */   {
/* 181 */     this.containerType = containerType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathName()
/*     */   {
/* 189 */     return this.pathName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPathName(String pathName)
/*     */   {
/* 197 */     this.pathName = pathName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReviewer()
/*     */   {
/* 205 */     return this.reviewer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReviewer(String reviewer)
/*     */   {
/* 213 */     this.reviewer = reviewer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRetentionTriggerName()
/*     */   {
/* 221 */     return this.retentionTriggerName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionTriggerName(String retentionTriggerName)
/*     */   {
/* 229 */     this.retentionTriggerName = retentionTriggerName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRetentionPeriod()
/*     */   {
/* 237 */     return this.retentionPeriod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionPeriod(String retentionPeriod)
/*     */   {
/* 245 */     this.retentionPeriod = retentionPeriod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFiscalYearEndDate()
/*     */   {
/* 253 */     return this.fiscalYearEndDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFiscalYearEndDate(String fiscalYearEndDate)
/*     */   {
/* 261 */     this.fiscalYearEndDate = fiscalYearEndDate;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\entity\DDContainerEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */