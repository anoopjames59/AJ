/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.core.DispositionAction;
/*      */ import com.ibm.jarm.api.core.DispositionPhase;
/*      */ import com.ibm.jarm.api.core.DispositionPhaseList;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class P8CE_DispositionPhaseListImpl
/*      */   implements DispositionPhaseList
/*      */ {
/* 1446 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*      */   
/*      */   private static final String SQLForCurrentPhaseID = "SELECT TOP 1 Id FROM %s WHERE CurrentPhaseID = '%s' ";
/*      */   
/*      */   private DispositionSchedule owningSchedule;
/*      */   
/*      */   private List<DispositionPhase> phaseList;
/*      */   private List<DispositionPhase> phasesToDelete;
/*      */   
/*      */   P8CE_DispositionPhaseListImpl(DispositionSchedule owningSchedule, List<DispositionPhase> phaseList)
/*      */   {
/* 1457 */     Tracer.traceMethodEntry(new Object[] { owningSchedule, phaseList });
/* 1458 */     this.owningSchedule = owningSchedule;
/* 1459 */     this.phaseList = phaseList;
/* 1460 */     if (phaseList != null)
/*      */     {
/* 1462 */       for (int i = 0; i < phaseList.size(); i++)
/*      */       {
/* 1464 */         P8CE_DispositionPhaseImpl phase = (P8CE_DispositionPhaseImpl)phaseList.get(i);
/* 1465 */         phase.setOwningSchedule((P8CE_DispositionScheduleImpl)owningSchedule);
/*      */       }
/*      */     }
/* 1468 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int add(DispositionPhase phase)
/*      */   {
/* 1476 */     Tracer.traceMethodEntry(new Object[] { phase });
/* 1477 */     Util.ckNullObjParam("phase", phase);
/* 1478 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1481 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1483 */       String phaseIdent = phase.getObjectIdentity();
/*      */       
/* 1485 */       if (contains(phase))
/*      */       {
/* 1487 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_PHASE_ALREADY_EXISTS, new Object[] { phaseIdent });
/*      */       }
/*      */       
/*      */ 
/* 1491 */       DispositionSchedule newPhasesSchedule = phase.getAssociatedSchedule();
/* 1492 */       if (newPhasesSchedule != null)
/*      */       {
/* 1494 */         if (!this.owningSchedule.getObjectIdentity().equalsIgnoreCase(newPhasesSchedule.getObjectIdentity()))
/*      */         {
/* 1496 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_PHASE_ALREADY_IN_USE, new Object[] { phaseIdent });
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1502 */       DispositionAction action = null;
/* 1503 */       DispositionActionType actionType = null;
/* 1504 */       for (Iterator i$ = this.phaseList.iterator(); i$.hasNext();) { existingPhase = (DispositionPhase)i$.next();
/*      */         
/* 1506 */         action = existingPhase.getPhaseAction();
/* 1507 */         if (action != null)
/*      */         {
/* 1509 */           actionType = action.getActionType();
/* 1510 */           if ((actionType != null) && ((actionType == DispositionActionType.Destroy) || (actionType == DispositionActionType.Transfer) || (actionType == DispositionActionType.AutoDestroy)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1515 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_PHASE_AFTER_TERMINATING_PHASE, new Object[0]);
/*      */           }
/*      */         }
/*      */       }
/*      */       DispositionPhase existingPhase;
/* 1520 */       this.phaseList.add(phase);
/* 1521 */       renumberPhaseList();
/* 1522 */       int result = this.phaseList.size();
/* 1523 */       markScheduleAsChanged();
/* 1524 */       Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/* 1525 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException rre)
/*      */     {
/* 1529 */       throw rre;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1533 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1537 */       if (establishedSubject) {
/* 1538 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DispositionPhase get(int position)
/*      */   {
/* 1547 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(position) });
/* 1548 */     DispositionPhase result = (DispositionPhase)this.phaseList.get(position - 1);
/* 1549 */     Tracer.traceMethodExit(new Object[] { result });
/* 1550 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(String phaseIdent)
/*      */   {
/* 1558 */     Tracer.traceMethodEntry(new Object[] { phaseIdent });
/* 1559 */     Util.ckInvalidStrParam("phaseIdent", phaseIdent);
/* 1560 */     int position = findPhasePosition(phaseIdent);
/* 1561 */     if (position == -1)
/*      */     {
/* 1563 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PHASE_NOT_IN_COLLECTION, new Object[] { phaseIdent });
/*      */     }
/*      */     
/* 1566 */     remove(position);
/* 1567 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(int position)
/*      */   {
/* 1575 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(position) });
/* 1576 */     DispositionPhase phaseToRemove = (DispositionPhase)this.phaseList.get(position - 1);
/* 1577 */     validatePhaseRemoval(phaseToRemove);
/*      */     
/* 1579 */     DispositionPhase removedPhase = (DispositionPhase)this.phaseList.remove(position - 1);
/* 1580 */     renumberPhaseList();
/* 1581 */     addToDeleteList(removedPhase);
/* 1582 */     markScheduleAsChanged();
/* 1583 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(DispositionPhase phase)
/*      */   {
/* 1591 */     Tracer.traceMethodEntry(new Object[] { phase });
/* 1592 */     Util.ckNullObjParam("phase", phase);
/* 1593 */     int phasePosition = findPhasePosition(phase.getObjectIdentity());
/* 1594 */     if (phasePosition == -1)
/*      */     {
/* 1596 */       String phaseIdent = phase.getObjectIdentity();
/* 1597 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PHASE_NOT_IN_COLLECTION, new Object[] { phaseIdent });
/*      */     }
/* 1599 */     validatePhaseRemoval(phase);
/*      */     
/* 1601 */     DispositionPhase removedPhase = (DispositionPhase)this.phaseList.remove(phasePosition - 1);
/* 1602 */     renumberPhaseList();
/* 1603 */     addToDeleteList(removedPhase);
/* 1604 */     markScheduleAsChanged();
/* 1605 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/* 1613 */     Tracer.traceMethodEntry(new Object[0]);
/* 1614 */     int result = this.phaseList.size();
/* 1615 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/* 1616 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1624 */     Tracer.traceMethodEntry(new Object[0]);
/* 1625 */     boolean result = this.phaseList.isEmpty();
/* 1626 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1627 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean contains(DispositionPhase phase)
/*      */   {
/* 1635 */     Tracer.traceMethodEntry(new Object[] { phase });
/* 1636 */     Util.ckNullObjParam("phase", phase);
/* 1637 */     String phaseIdent = phase.getObjectIdentity();
/* 1638 */     int position = findPhasePosition(phaseIdent);
/* 1639 */     boolean result = position >= 1;
/* 1640 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1641 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<DispositionPhase> iterator()
/*      */   {
/* 1649 */     Tracer.traceMethodEntry(new Object[0]);
/* 1650 */     Iterator<DispositionPhase> iter = this.phaseList.iterator();
/* 1651 */     Tracer.traceMethodExit(new Object[] { iter });
/* 1652 */     return iter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void contributeToSaveBatch(RMRefreshMode jarmRefreshMode, UpdatingBatch jaceUB)
/*      */   {
/* 1664 */     Tracer.traceMethodEntry(new Object[0]);
/* 1665 */     P8CE_DispositionPhaseImpl phaseImpl = null;
/* 1666 */     boolean forcePhaseObjSave = false;
/*      */     
/* 1668 */     for (int i = 0; i < this.phaseList.size(); i++)
/*      */     {
/* 1670 */       phaseImpl = (P8CE_DispositionPhaseImpl)this.phaseList.get(i);
/* 1671 */       phaseImpl.contributeToSaveBatch(jarmRefreshMode, jaceUB, forcePhaseObjSave);
/*      */     }
/*      */     
/*      */ 
/* 1675 */     CustomObject jacePhaseBase = null;
/* 1676 */     if (this.phasesToDelete != null)
/*      */     {
/* 1678 */       for (int i = this.phasesToDelete.size() - 1; i >= 0; i--)
/*      */       {
/* 1680 */         phaseImpl = (P8CE_DispositionPhaseImpl)this.phasesToDelete.remove(i);
/* 1681 */         jacePhaseBase = phaseImpl.jaceCustomObject;
/* 1682 */         jacePhaseBase.delete();
/* 1683 */         jaceUB.add(jacePhaseBase, P8CE_Util.CEPF_Empty);
/*      */       }
/*      */     }
/* 1686 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void renumberPhaseList()
/*      */   {
/* 1695 */     DispositionPhase phase = null;
/* 1696 */     for (int i = 0; i < this.phaseList.size(); i++)
/*      */     {
/* 1698 */       phase = (DispositionPhase)this.phaseList.get(i);
/*      */       
/*      */ 
/*      */ 
/* 1702 */       Integer expectedPhaseNumber = Integer.valueOf(i + 1);
/* 1703 */       Integer phaseNumber = null;
/* 1704 */       if (phase.getProperties().isPropertyPresent("PhaseNumber"))
/*      */       {
/* 1706 */         phaseNumber = phase.getPhaseNumber();
/*      */       }
/* 1708 */       if (!expectedPhaseNumber.equals(phaseNumber))
/*      */       {
/* 1710 */         phase.getProperties().putIntegerValue("PhaseNumber", expectedPhaseNumber);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validatePhaseRemoval(DispositionPhase phaseToRemove)
/*      */   {
/* 1727 */     Tracer.traceMethodEntry(new Object[] { phaseToRemove });
/* 1728 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1731 */       establishedSubject = P8CE_Util.associateSubject();
/* 1732 */       String phaseIdent = phaseToRemove.getObjectIdentity();
/* 1733 */       Repository repository = phaseToRemove.getRepository();
/* 1734 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*      */       
/*      */ 
/* 1737 */       String sqlStmt = String.format("SELECT TOP 1 Id FROM %s WHERE CurrentPhaseID = '%s' ", new Object[] { "RMFolder", phaseIdent });
/* 1738 */       SearchSQL searchSQL = new SearchSQL(sqlStmt);
/* 1739 */       SearchScope scope = new SearchScope(jaceObjStore);
/* 1740 */       RepositoryRowSet rowSet = scope.fetchRows(searchSQL, null, null, Boolean.FALSE);
/* 1741 */       if (!rowSet.isEmpty())
/*      */       {
/* 1743 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_REMOVE_PHASE_IT_IS_IN_USE, new Object[] { phaseIdent });
/*      */       }
/*      */       
/*      */ 
/* 1747 */       sqlStmt = String.format("SELECT TOP 1 Id FROM %s WHERE CurrentPhaseID = '%s' ", new Object[] { "RecordInfo", phaseIdent });
/* 1748 */       searchSQL = new SearchSQL(sqlStmt);
/* 1749 */       scope = new SearchScope(jaceObjStore);
/* 1750 */       rowSet = scope.fetchRows(searchSQL, null, null, Boolean.FALSE);
/* 1751 */       if (!rowSet.isEmpty())
/*      */       {
/* 1753 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_REMOVE_PHASE_IT_IS_IN_USE, new Object[] { phaseIdent });
/*      */       }
/*      */       
/* 1756 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException rre)
/*      */     {
/* 1760 */       throw rre;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1764 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1768 */       if (establishedSubject) {
/* 1769 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int findPhasePosition(String identToFind)
/*      */   {
/* 1784 */     Tracer.traceMethodEntry(new Object[] { identToFind });
/* 1785 */     int foundPosition = -1;
/* 1786 */     DispositionPhase phaseItem = null;
/* 1787 */     for (int i = 0; i < this.phaseList.size(); i++)
/*      */     {
/* 1789 */       phaseItem = (DispositionPhase)this.phaseList.get(i);
/* 1790 */       if (phaseItem.getObjectIdentity().equalsIgnoreCase(identToFind))
/*      */       {
/* 1792 */         foundPosition = i + 1;
/* 1793 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1797 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(foundPosition) });
/* 1798 */     return foundPosition;
/*      */   }
/*      */   
/*      */   private void addToDeleteList(DispositionPhase phaseToDelete)
/*      */   {
/* 1803 */     Tracer.traceMethodEntry(new Object[] { phaseToDelete });
/* 1804 */     if (this.phasesToDelete == null)
/*      */     {
/* 1806 */       this.phasesToDelete = new ArrayList(1);
/*      */     }
/*      */     
/* 1809 */     this.phasesToDelete.add(phaseToDelete);
/* 1810 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   private void markScheduleAsChanged()
/*      */   {
/* 1815 */     Tracer.traceMethodEntry(new Object[0]);
/* 1816 */     if (!this.owningSchedule.isCreationPending())
/*      */     {
/* 1818 */       String schedReason = this.owningSchedule.getReasonForChange();
/* 1819 */       this.owningSchedule.setReasonForChange(schedReason);
/*      */     }
/* 1821 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_DispositionPhaseListImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */