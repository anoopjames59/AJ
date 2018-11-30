/*    */ package com.ibm.ier.plugin.search.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ChildSearchCriteria
/*    */   extends SearchCriteria
/*    */ {
/*    */   private static final long serialVersionUID = -645085567677354106L;
/* 10 */   private List<SearchCriterion> criteria = new ArrayList();
/*    */   
/*    */   public List<SearchCriterion> getSearchCriteria() {
/* 13 */     return this.criteria;
/*    */   }
/*    */   
/*    */   public void setSearchCriteria(List<SearchCriterion> criteria) {
/* 17 */     this.criteria = criteria;
/*    */   }
/*    */   
/*    */   public void addCriterion(SearchCriterion criterion) {
/* 21 */     if (this.criteria == null) {
/* 22 */       this.criteria = new ArrayList();
/*    */     }
/* 24 */     this.criteria.add(criterion);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\ChildSearchCriteria.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */