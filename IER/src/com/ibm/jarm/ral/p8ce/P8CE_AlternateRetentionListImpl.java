/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.core.AlternateRetention;
/*      */ import com.ibm.jarm.api.core.AlternateRetentionList;
/*      */ import com.ibm.jarm.api.core.DispositionPhase;
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
/*      */ class P8CE_AlternateRetentionListImpl
/*      */   implements AlternateRetentionList
/*      */ {
/* 1231 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*      */   
/*      */   private DispositionPhase owningPhase;
/*      */   private List<AlternateRetention> altRetentList;
/*      */   private List<AlternateRetention> altRetentsToDelete;
/*      */   
/*      */   P8CE_AlternateRetentionListImpl(DispositionPhase owningPhase, List<AlternateRetention> alternateRetentions)
/*      */   {
/* 1239 */     Tracer.traceMethodEntry(new Object[] { owningPhase, alternateRetentions });
/* 1240 */     this.owningPhase = owningPhase;
/* 1241 */     this.altRetentList = alternateRetentions;
/* 1242 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int add(AlternateRetention altRetent)
/*      */   {
/* 1250 */     Tracer.traceMethodEntry(new Object[] { altRetent });
/* 1251 */     Util.ckNullObjParam("altRetent", altRetent);
/* 1252 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1255 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1257 */       String altRetentIdent = altRetent.getObjectIdentity();
/*      */       
/* 1259 */       if (contains(altRetent))
/*      */       {
/* 1261 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_ALTRETENT_ALREADY_EXISTS, new Object[] { altRetentIdent });
/*      */       }
/*      */       
/*      */ 
/* 1265 */       DispositionPhase newAltRetentPhase = altRetent.getDispositionPhase();
/* 1266 */       if (newAltRetentPhase != null)
/*      */       {
/* 1268 */         if (!this.owningPhase.getObjectIdentity().equalsIgnoreCase(newAltRetentPhase.getObjectIdentity()))
/*      */         {
/* 1270 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_ALTRETENT_ALREADY_IN_USE, new Object[] { altRetentIdent });
/*      */         }
/*      */       }
/*      */       
/* 1274 */       this.altRetentList.add(altRetent);
/* 1275 */       renumberAltRetentList();
/* 1276 */       int result = this.altRetentList.size();
/* 1277 */       markPhaseAsChanged();
/* 1278 */       Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/* 1279 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException rre)
/*      */     {
/* 1283 */       throw rre;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1287 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/* 1291 */       if (establishedSubject) {
/* 1292 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AlternateRetention get(int position)
/*      */   {
/* 1301 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(position) });
/* 1302 */     AlternateRetention result = (AlternateRetention)this.altRetentList.get(position - 1);
/* 1303 */     Tracer.traceMethodExit(new Object[] { result });
/* 1304 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(String altRetentIdent)
/*      */   {
/* 1312 */     Tracer.traceMethodEntry(new Object[] { altRetentIdent });
/* 1313 */     Util.ckInvalidStrParam("altRetentIdent", altRetentIdent);
/* 1314 */     int position = findAltRetentPosition(altRetentIdent);
/* 1315 */     if (position == -1)
/*      */     {
/* 1317 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ALTRETENT_NOT_IN_COLLECTION, new Object[] { altRetentIdent });
/*      */     }
/*      */     
/* 1320 */     remove(position);
/* 1321 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(int position)
/*      */   {
/* 1329 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(position) });
/* 1330 */     AlternateRetention removedAltRetent = (AlternateRetention)this.altRetentList.remove(position - 1);
/* 1331 */     renumberAltRetentList();
/* 1332 */     addToDeleteList(removedAltRetent);
/* 1333 */     markPhaseAsChanged();
/* 1334 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(AlternateRetention altRetent)
/*      */   {
/* 1342 */     Tracer.traceMethodEntry(new Object[] { altRetent });
/* 1343 */     Util.ckNullObjParam("altRetent", altRetent);
/* 1344 */     int altRetentPosition = findAltRetentPosition(altRetent.getObjectIdentity());
/* 1345 */     if (altRetentPosition < 1)
/*      */     {
/* 1347 */       String altRetentIdent = altRetent.getObjectIdentity();
/* 1348 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ALTRETENT_NOT_IN_COLLECTION, new Object[] { altRetentIdent });
/*      */     }
/*      */     
/* 1351 */     AlternateRetention removedAltRetent = (AlternateRetention)this.altRetentList.remove(altRetentPosition - 1);
/* 1352 */     renumberAltRetentList();
/* 1353 */     addToDeleteList(removedAltRetent);
/* 1354 */     markPhaseAsChanged();
/* 1355 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/* 1363 */     Tracer.traceMethodEntry(new Object[0]);
/* 1364 */     int result = this.altRetentList.size();
/* 1365 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/* 1366 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1374 */     Tracer.traceMethodEntry(new Object[0]);
/* 1375 */     boolean result = this.altRetentList.isEmpty();
/* 1376 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1377 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean contains(AlternateRetention altRetent)
/*      */   {
/* 1385 */     Tracer.traceMethodEntry(new Object[] { altRetent });
/* 1386 */     Util.ckNullObjParam("altRetent", altRetent);
/* 1387 */     String altRetentIdent = altRetent.getObjectIdentity();
/* 1388 */     int position = findAltRetentPosition(altRetentIdent);
/* 1389 */     boolean result = position >= 1;
/* 1390 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1391 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<AlternateRetention> iterator()
/*      */   {
/* 1399 */     Tracer.traceMethodEntry(new Object[0]);
/* 1400 */     Iterator<AlternateRetention> iter = this.altRetentList.iterator();
/* 1401 */     Tracer.traceMethodExit(new Object[] { iter });
/* 1402 */     return iter;
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
/* 1414 */     Tracer.traceMethodEntry(new Object[0]);
/* 1415 */     P8CE_AlternateRetentionImpl altRetentImpl = null;
/*      */     
/* 1417 */     for (int i = 0; i < this.altRetentList.size(); i++)
/*      */     {
/* 1419 */       altRetentImpl = (P8CE_AlternateRetentionImpl)this.altRetentList.get(i);
/* 1420 */       altRetentImpl.contributeToSaveBatch(jarmRefreshMode, jaceUB);
/*      */     }
/*      */     
/*      */ 
/* 1424 */     CustomObject jaceAltRetentBase = null;
/* 1425 */     if (this.altRetentsToDelete != null)
/*      */     {
/* 1427 */       for (int i = this.altRetentsToDelete.size() - 1; i >= 0; i--)
/*      */       {
/* 1429 */         altRetentImpl = (P8CE_AlternateRetentionImpl)this.altRetentsToDelete.remove(i);
/* 1430 */         jaceAltRetentBase = altRetentImpl.jaceCustomObject;
/* 1431 */         jaceAltRetentBase.delete();
/* 1432 */         jaceUB.add(jaceAltRetentBase, P8CE_Util.CEPF_Empty);
/*      */       }
/*      */     }
/* 1435 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void renumberAltRetentList()
/*      */   {
/* 1444 */     AlternateRetention altRetent = null;
/* 1445 */     for (int i = 0; i < this.altRetentList.size(); i++)
/*      */     {
/* 1447 */       altRetent = (AlternateRetention)this.altRetentList.get(i);
/*      */       
/*      */ 
/*      */ 
/* 1451 */       Integer expectedAltRetentNumber = Integer.valueOf(i + 1);
/* 1452 */       Integer altRetentNumber = null;
/* 1453 */       if (altRetent.getProperties().isPropertyPresent("IntelligentRetentionNumber"))
/*      */       {
/* 1455 */         altRetentNumber = altRetent.getRetentionNumber();
/*      */       }
/* 1457 */       if (!expectedAltRetentNumber.equals(altRetentNumber))
/*      */       {
/* 1459 */         altRetent.getProperties().putIntegerValue("IntelligentRetentionNumber", expectedAltRetentNumber);
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
/*      */   private int findAltRetentPosition(String identToFind)
/*      */   {
/* 1475 */     Tracer.traceMethodEntry(new Object[] { identToFind });
/* 1476 */     int foundPosition = -1;
/* 1477 */     AlternateRetention altRetentItem = null;
/* 1478 */     for (int i = 0; i < this.altRetentList.size(); i++)
/*      */     {
/* 1480 */       altRetentItem = (AlternateRetention)this.altRetentList.get(i);
/* 1481 */       if (altRetentItem.getObjectIdentity().equalsIgnoreCase(identToFind))
/*      */       {
/* 1483 */         foundPosition = i + 1;
/* 1484 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1488 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(foundPosition) });
/* 1489 */     return foundPosition;
/*      */   }
/*      */   
/*      */   private void addToDeleteList(AlternateRetention altRetentToDelete)
/*      */   {
/* 1494 */     Tracer.traceMethodEntry(new Object[] { altRetentToDelete });
/* 1495 */     if (this.altRetentsToDelete == null)
/*      */     {
/* 1497 */       this.altRetentsToDelete = new ArrayList(1);
/*      */     }
/*      */     
/* 1500 */     this.altRetentsToDelete.add(altRetentToDelete);
/* 1501 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   private void markPhaseAsChanged()
/*      */   {
/* 1506 */     Tracer.traceMethodEntry(new Object[0]);
/* 1507 */     if (!this.owningPhase.isCreationPending())
/*      */     {
/* 1509 */       String phaseReason = this.owningPhase.getReasonForChange();
/* 1510 */       this.owningPhase.setReasonForChange(phaseReason);
/*      */     }
/* 1512 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_AlternateRetentionListImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */