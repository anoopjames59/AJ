/*    */ package com.ibm.jarm.ral.common;
/*    */ 
/*    */ import com.ibm.jarm.api.collection.PageableSet;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.BaseEntity;
/*    */ import com.ibm.jarm.api.core.DomainConnection;
/*    */ import com.ibm.jarm.api.core.RMDomain;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.jarm.api.query.CBRResult;
/*    */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*    */ import com.ibm.jarm.api.query.RMSearch;
/*    */ import com.ibm.jarm.api.query.ResultRow;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RALBaseDomain
/*    */   extends RALBaseEntity
/*    */   implements RMDomain
/*    */ {
/* 25 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected DomainConnection domainConn;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected RALBaseDomain(DomainConnection domainConn, String identity, boolean isPlaceholder)
/*    */   {
/* 40 */     super(EntityType.Domain, null, identity, isPlaceholder);
/* 41 */     Tracer.traceMethodEntry(new Object[] { domainConn, identity, Boolean.valueOf(isPlaceholder) });
/*    */     
/* 43 */     this.domainConn = domainConn;
/* 44 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public DomainConnection getDomainConnection()
/*    */   {
/* 52 */     return this.domainConn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EntityType getEntityType()
/*    */   {
/* 62 */     return EntityType.Domain;
/*    */   }
/*    */   
/*    */   public abstract PageableSet<? extends BaseEntity> fetchObjects(RMSearch paramRMSearch, String paramString, EntityType paramEntityType, Integer paramInteger, RMPropertyFilter paramRMPropertyFilter, Boolean paramBoolean);
/*    */   
/*    */   public abstract PageableSet<ResultRow> fetchRows(RMSearch paramRMSearch, String paramString, Integer paramInteger, RMPropertyFilter paramRMPropertyFilter, Boolean paramBoolean);
/*    */   
/*    */   public abstract PageableSet<CBRResult> contentBasedRetrieval(RMSearch paramRMSearch, RMContentSearchDefinition paramRMContentSearchDefinition, Integer paramInteger, Boolean paramBoolean);
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseDomain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */