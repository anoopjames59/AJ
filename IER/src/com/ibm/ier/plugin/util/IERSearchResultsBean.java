/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ public class IERSearchResultsBean
/*     */   implements Serializable, Enumeration<BaseEntity>
/*     */ {
/*     */   private static final long serialVersionUID = 8731157793062169124L;
/*     */   private List<BaseEntity> results;
/*     */   private Iterator<BaseEntity> hitsIterator;
/*     */   protected HttpServletRequest request;
/*  33 */   private boolean hasFolderContents = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public IERSearchResultsBean(HttpServletRequest request)
/*     */   {
/*  39 */     this.results = new ArrayList(0);
/*  40 */     this.request = request;
/*  41 */     this.hitsIterator = this.results.iterator();
/*  42 */     Logger.logDebug(this, "IERSearchResultsBean()", request, "In constructor()");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IERSearchResultsBean(List<BaseEntity> newHits, boolean hasFolderContents)
/*     */   {
/*  51 */     this.hasFolderContents = hasFolderContents;
/*  52 */     initialize(newHits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IERSearchResultsBean(List<BaseEntity> newHits)
/*     */   {
/*  60 */     initialize(newHits);
/*     */   }
/*     */   
/*     */   private void initialize(List<BaseEntity> newHits)
/*     */   {
/*  65 */     if (newHits != null)
/*     */     {
/*  67 */       this.results = newHits;
/*  68 */       this.hitsIterator = newHits.iterator();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/*  76 */     this.hitsIterator = this.results.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*  83 */     return this.hitsIterator.hasNext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BaseEntity nextElement()
/*     */   {
/*  90 */     return (BaseEntity)this.hitsIterator.next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/*  97 */     return this.results.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sort(boolean ascending, String columnName)
/*     */   {
/* 105 */     BaseEntity[] theHits = (BaseEntity[])this.results.toArray(new BaseEntity[0]);
/* 106 */     Arrays.sort(theHits, new P8HitSorter(ascending, columnName));
/* 107 */     this.results = new ArrayList(Arrays.asList(theHits));
/* 108 */     reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<BaseEntity> getResults()
/*     */   {
/* 117 */     return this.results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResults(List<BaseEntity> results)
/*     */   {
/* 128 */     this.results = results;
/*     */   }
/*     */   
/*     */   public void setHasFolderContents(boolean value)
/*     */   {
/* 133 */     this.hasFolderContents = value;
/*     */   }
/*     */   
/*     */   public boolean hasFolderContents()
/*     */   {
/* 138 */     return this.hasFolderContents;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\IERSearchResultsBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */