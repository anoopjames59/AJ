/*    */ package com.ibm.jarm.ral.common;
/*    */ 
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.RMLink;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RALBaseRMLink
/*    */   extends RALBaseEntity
/*    */   implements RMLink
/*    */ {
/* 19 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
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
/*    */   protected RALBaseRMLink(EntityType entityType, Repository repository, String identity, boolean isPlaceholder)
/*    */   {
/* 36 */     super(entityType, repository, identity, isPlaceholder);
/* 37 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, Boolean.valueOf(isPlaceholder) });
/* 38 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseRMLink.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */