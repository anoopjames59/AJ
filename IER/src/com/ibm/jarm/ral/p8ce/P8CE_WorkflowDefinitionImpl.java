/*    */ package com.ibm.jarm.ral.p8ce;
/*    */ 
/*    */ import com.filenet.api.core.Document;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.RMWorkflowDefinition;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.exception.RMErrorCode;
/*    */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8CE_WorkflowDefinitionImpl
/*    */   extends P8CE_ContentItemImpl
/*    */   implements RMWorkflowDefinition
/*    */ {
/* 23 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String WORKFLOW_DEFS_CONTAINER_NAME = "Workflows";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected P8CE_WorkflowDefinitionImpl(Repository repository, String identity, Document jaceDocument, boolean isPlaceholder)
/*    */   {
/* 40 */     super(EntityType.WorkflowDefinition, repository, identity, jaceDocument, isPlaceholder);
/* 41 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceDocument, Boolean.valueOf(isPlaceholder) });
/* 42 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void delete()
/*    */   {
/* 51 */     Tracer.traceMethodEntry(new Object[0]);
/* 52 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "delete", getEntityType(), getClientIdentifier() });
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_WorkflowDefinitionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */