/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.constants.RefreshMode;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.EngineObject;
/*      */ import com.filenet.api.core.Factory.CustomObject;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*      */ import com.ibm.jarm.api.core.AlternateRetentionList;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.DispositionAction;
/*      */ import com.ibm.jarm.api.core.DispositionPhase;
/*      */ import com.ibm.jarm.api.core.DispositionPhaseList;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.DispositionTrigger;
/*      */ import com.ibm.jarm.api.core.Dispositionable;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.Record;
/*      */ import com.ibm.jarm.api.core.RecordCategory;
/*      */ import com.ibm.jarm.api.core.RecordFolder;
/*      */ import com.ibm.jarm.api.core.RecordType;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.util.JarmLogger;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.RMLogCode;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*      */ import com.ibm.jarm.ral.common.RALBaseRepository;
/*      */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
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
/*      */ public class P8CE_DispositionScheduleImpl
/*      */   extends P8CE_RMCustomObjectImpl
/*      */   implements DispositionSchedule
/*      */ {
/*   66 */   private static final JarmLogger Logger = JarmLogger.getJarmLogger(P8CE_DispositionScheduleImpl.class.getName());
/*   67 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*   68 */   private static final IGenerator<DispositionSchedule> DispScheduleGenerator = new Generator();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   79 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityDescription", "RMEntityType", "DisposalScheduleName", "CalendarDate", "DateLastModified", "CutoffOffSetYears", "CutoffOffSetMonths", "CutoffOffSetDays", "IsScreeningRequired", "ReasonForChange", "EffectiveDateModified", "AuthorisingStatute", "IsTriggerChanged", "CutOffBase", "SweepState", "SweepAuditXML", "Name" };
/*      */   
/*      */ 
/*      */   private static final List<FilterElement> MandatoryJaceFEs;
/*      */   
/*      */ 
/*      */   private DispositionPhaseList phaseList;
/*      */   
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*   91 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*      */     
/*   93 */     List<FilterElement> tempList = new ArrayList(1);
/*   94 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*   95 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*      */   }
/*      */   
/*      */   static String[] getMandatoryPropertyNames()
/*      */   {
/*  100 */     return MandatoryPropertyNames;
/*      */   }
/*      */   
/*      */   static List<FilterElement> getMandatoryJaceFEs()
/*      */   {
/*  105 */     return MandatoryJaceFEs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static IGenerator<DispositionSchedule> getGenerator()
/*      */   {
/*  115 */     return DispScheduleGenerator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<FilterElement> getMandatoryFEs()
/*      */   {
/*  126 */     return getMandatoryJaceFEs();
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
/*      */   static DispositionSchedule createNew(Repository repository, String scheduleName)
/*      */   {
/*  141 */     Tracer.traceMethodEntry(new Object[] { repository, scheduleName });
/*  142 */     ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  143 */     CustomObject jaceCustObj = Factory.CustomObject.createInstance(jaceObjStore, "DisposalSchedule");
/*  144 */     jaceCustObj.getProperties().putValue("DisposalScheduleName", scheduleName);
/*      */     
/*  146 */     DispositionSchedule result = new P8CE_DispositionScheduleImpl(repository, scheduleName, jaceCustObj, true);
/*  147 */     Tracer.traceMethodExit(new Object[] { result });
/*  148 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   P8CE_DispositionScheduleImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*      */   {
/*  154 */     super(EntityType.DispositionSchedule, repository, identity, jaceCustomObject, isPlaceholder);
/*  155 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  156 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getScheduleName()
/*      */   {
/*  164 */     Tracer.traceMethodEntry(new Object[0]);
/*  165 */     String result = P8CE_Util.getJacePropertyAsString(this, "DisposalScheduleName");
/*  166 */     Tracer.traceMethodExit(new Object[] { result });
/*  167 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScheduleName(String name)
/*      */   {
/*  175 */     Tracer.traceMethodEntry(new Object[] { name });
/*  176 */     Util.ckInvalidStrParam("name", name);
/*  177 */     getProperties().putStringValue("DisposalScheduleName", name);
/*  178 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDescription()
/*      */   {
/*  186 */     Tracer.traceMethodEntry(new Object[0]);
/*  187 */     String result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/*  188 */     Tracer.traceMethodExit(new Object[] { result });
/*  189 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDescription(String value)
/*      */   {
/*  197 */     Tracer.traceMethodEntry(new Object[] { value });
/*  198 */     getProperties().putStringValue("RMEntityDescription", value);
/*  199 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getCalendarDate()
/*      */   {
/*  207 */     Tracer.traceMethodEntry(new Object[0]);
/*  208 */     Date result = P8CE_Util.getJacePropertyAsDate(this, "CalendarDate");
/*  209 */     Tracer.traceMethodExit(new Object[] { result });
/*  210 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCalendarDate(Date dateValue)
/*      */   {
/*  218 */     Tracer.traceMethodEntry(new Object[] { dateValue });
/*  219 */     getProperties().putDateTimeValue("CalendarDate", dateValue);
/*  220 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCutoffBase()
/*      */   {
/*  228 */     Tracer.traceMethodEntry(new Object[0]);
/*  229 */     String result = P8CE_Util.getJacePropertyAsString(this, "CutOffBase");
/*  230 */     Tracer.traceMethodExit(new Object[] { result });
/*  231 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCutoffBase(String propertySymbolicName)
/*      */   {
/*  239 */     Tracer.traceMethodEntry(new Object[] { propertySymbolicName });
/*  240 */     getProperties().putStringValue("CutOffBase", propertySymbolicName);
/*  241 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionAction getCutoffAction()
/*      */   {
/*  250 */     Tracer.traceMethodEntry(new Object[0]);
/*  251 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  254 */       establishedSubject = P8CE_Util.associateSubject();
/*  255 */       List<FilterElement> jaceDispTrigFEs = P8CE_DispositionActionImpl.getMandatoryJaceFEs();
/*  256 */       IGenerator<DispositionAction> generator = P8CE_DispositionActionImpl.getGenerator();
/*  257 */       DispositionAction result = (DispositionAction)fetchSVObjPropValue(jaceDispTrigFEs, "CutoffAction", generator);
/*      */       
/*  259 */       Tracer.traceMethodExit(new Object[] { result });
/*  260 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  264 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  268 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  272 */       if (establishedSubject) {
/*  273 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCutoffAction(DispositionAction action)
/*      */   {
/*  282 */     Tracer.traceMethodEntry(new Object[] { action });
/*  283 */     EngineObject jaceActionBase = null;
/*  284 */     if (action != null)
/*      */     {
/*  286 */       jaceActionBase = ((JaceBasable)action).getJaceBaseObject();
/*      */     }
/*      */     
/*  289 */     Properties jaceProps = this.jaceCustomObject.getProperties();
/*  290 */     jaceProps.putValue("CutoffAction", jaceActionBase);
/*  291 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDispositionAuthority()
/*      */   {
/*  299 */     Tracer.traceMethodEntry(new Object[0]);
/*  300 */     String result = P8CE_Util.getJacePropertyAsString(this, "AuthorisingStatute");
/*  301 */     Tracer.traceMethodExit(new Object[] { result });
/*  302 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDispositionAuthority(String value)
/*      */   {
/*  310 */     Tracer.traceMethodEntry(new Object[] { value });
/*  311 */     getProperties().putStringValue("AuthorisingStatute", value);
/*  312 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer[] getDispositionEventOffset()
/*      */   {
/*  320 */     Tracer.traceMethodEntry(new Object[0]);
/*  321 */     Integer[] results = new Integer[3];
/*  322 */     results[0] = P8CE_Util.getJacePropertyAsInteger(this, "CutoffOffSetYears");
/*  323 */     results[1] = P8CE_Util.getJacePropertyAsInteger(this, "CutoffOffSetMonths");
/*  324 */     results[2] = P8CE_Util.getJacePropertyAsInteger(this, "CutoffOffSetDays");
/*      */     
/*  326 */     Tracer.traceMethodExit((Object[])results);
/*  327 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDispositionEventOffset(Integer years, Integer months, Integer days)
/*      */   {
/*  335 */     Tracer.traceMethodEntry(new Object[] { years, months, days });
/*  336 */     Properties jaceProps = this.jaceCustomObject.getProperties();
/*  337 */     jaceProps.putValue("CutoffOffSetYears", years);
/*  338 */     jaceProps.putValue("CutoffOffSetMonths", months);
/*  339 */     jaceProps.putValue("CutoffOffSetDays", days);
/*  340 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionTrigger getDispositionTrigger()
/*      */   {
/*  348 */     Tracer.traceMethodEntry(new Object[0]);
/*  349 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  352 */       establishedSubject = P8CE_Util.associateSubject();
/*  353 */       List<FilterElement> jaceDispTrigFEs = P8CE_DispositionTriggerImpl.getMandatoryJaceFEs();
/*  354 */       IGenerator<DispositionTrigger> generator = P8CE_DispositionTriggerImpl.getGenerator();
/*  355 */       DispositionTrigger result = (DispositionTrigger)fetchSVObjPropValue(jaceDispTrigFEs, "CutoffDisposalTrigger", generator);
/*      */       
/*  357 */       Tracer.traceMethodExit(new Object[] { result });
/*  358 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  362 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  366 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  370 */       if (establishedSubject) {
/*  371 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDispositionTigger(DispositionTrigger trigger)
/*      */   {
/*  380 */     Tracer.traceMethodEntry(new Object[] { trigger });
/*  381 */     EngineObject jaceTriggerBase = null;
/*  382 */     if (trigger != null)
/*      */     {
/*  384 */       jaceTriggerBase = ((JaceBasable)trigger).getJaceBaseObject();
/*      */     }
/*      */     
/*  387 */     Properties jaceProps = this.jaceCustomObject.getProperties();
/*  388 */     jaceProps.putValue("CutoffDisposalTrigger", jaceTriggerBase);
/*  389 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionPhaseList getDispositionPhases()
/*      */   {
/*  397 */     Tracer.traceMethodEntry(new Object[0]);
/*  398 */     boolean establishedSubject = false;
/*      */     try {
/*      */       List<DispositionPhase> rawList;
/*  401 */       if (this.phaseList == null)
/*      */       {
/*  403 */         establishedSubject = P8CE_Util.associateSubject();
/*  404 */         if (this.isCreationPending)
/*      */         {
/*      */ 
/*  407 */           this.phaseList = new P8CE_DispositionPhaseListImpl(this, new ArrayList(0));
/*      */         }
/*      */         else
/*      */         {
/*  411 */           rawList = fetchRawPhaseList();
/*  412 */           List<DispositionPhase> orderedList = orderPhases(rawList);
/*  413 */           this.phaseList = new P8CE_DispositionPhaseListImpl(this, orderedList);
/*      */         }
/*      */       }
/*      */       
/*  417 */       Tracer.traceMethodExit(new Object[] { this.phaseList });
/*  418 */       return this.phaseList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  422 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  426 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  430 */       if (establishedSubject) {
/*  431 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<DispositionPhase> fetchRawPhaseList()
/*      */   {
/*  442 */     Tracer.traceMethodEntry(new Object[0]);
/*  443 */     List<FilterElement> jaceDispPhasesFEs = P8CE_DispositionPhaseImpl.getMandatoryJaceFEs();
/*  444 */     IGenerator<DispositionPhase> generator = P8CE_DispositionPhaseImpl.getGenerator();
/*  445 */     List<DispositionPhase> result = fetchMVObjPropValueAsList(jaceDispPhasesFEs, "Phases", generator);
/*      */     
/*  447 */     Tracer.traceMethodExit(new Object[] { result });
/*  448 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void updateCurrentPhaseDataOnEntity(BaseEntity entity)
/*      */   {
/*  460 */     Tracer.traceMethodEntry(new Object[] { entity });
/*  461 */     String currentPhaseId = entity.getProperties().getGuidValue("CurrentPhaseID");
/*  462 */     if ((currentPhaseId != null) && (currentPhaseId.trim().length() > 0))
/*      */     {
/*  464 */       DispositionPhaseList schedPhases = getDispositionPhases();
/*  465 */       DispositionPhase currentPhase = null;
/*  466 */       if (schedPhases.size() > 0)
/*      */       {
/*      */ 
/*  469 */         for (DispositionPhase schedPhase : schedPhases)
/*      */         {
/*  471 */           if (schedPhase.getObjectIdentity().equalsIgnoreCase(currentPhaseId))
/*      */           {
/*  473 */             currentPhase = schedPhase;
/*  474 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  479 */       if (currentPhase == null)
/*      */       {
/*      */ 
/*  482 */         if (schedPhases.size() > 0) {
/*  483 */           currentPhase = schedPhases.get(1);
/*      */         }
/*      */       }
/*  486 */       if (currentPhase != null)
/*      */       {
/*  488 */         setPhaseDataOnEntity(entity, currentPhase);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  493 */         RALDispositionLogic.setFinalPhaseDataOnEntity(entity);
/*  494 */         ((RALBaseEntity)entity).internalSave(RMRefreshMode.Refresh);
/*      */       }
/*      */     }
/*      */     
/*  498 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void recalculateCutoffDate(Dispositionable disposableEntity)
/*      */   {
/*  506 */     Tracer.traceMethodEntry(new Object[] { disposableEntity });
/*  507 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  510 */       establishedSubject = P8CE_Util.associateSubject();
/*  511 */       BaseEntity entity = (BaseEntity)disposableEntity;
/*  512 */       RMProperties entityProps = entity.getProperties();
/*  513 */       String entityCutoffInhFromId = entityProps.getGuidValue("CutoffInheritedFrom");
/*  514 */       String entityIdent = entity.getObjectIdentity();
/*  515 */       Integer[] cutoffOffset = getDispositionEventOffset();
/*      */       
/*  517 */       DispositionTrigger cutoffTrigger = getDispositionTrigger();
/*  518 */       DispositionTriggerType cutoffTriggerType = null;
/*  519 */       if (cutoffTrigger != null)
/*      */       {
/*  521 */         cutoffTriggerType = cutoffTrigger.getTriggerType();
/*      */       }
/*      */       
/*  524 */       if ((cutoffTriggerType != DispositionTriggerType.InternalEventTrigger) && (entityIdent.equalsIgnoreCase(entityCutoffInhFromId)))
/*      */       {
/*      */ 
/*  527 */         Date eventDate = getEventFiredDate(cutoffTrigger, entity);
/*  528 */         if (eventDate != null)
/*      */         {
/*  530 */           Date cutoffDate = applyDateOffset(eventDate, cutoffOffset);
/*  531 */           setCutoffDataOnEntity(cutoffDate, entity);
/*  532 */           Logger.info(RMLogCode.RECALCULATE_CUTOFF_PERFORMED, new Object[] { entityIdent });
/*      */         }
/*      */       }
/*  535 */       else if (cutoffTriggerType == DispositionTriggerType.InternalEventTrigger)
/*      */       {
/*  537 */         String conditionXML = cutoffTrigger.getConditionXML();
/*  538 */         String whereClause = RALDispositionLogic.extractWhereClauseFromConditionXML(conditionXML);
/*  539 */         boolean startsWithAND = (whereClause != null) && (whereClause.trim().toUpperCase().startsWith("AND "));
/*      */         
/*  541 */         String tableName = cutoffTrigger.getAggregation();
/*      */         
/*  543 */         StringBuilder sb = new StringBuilder();
/*  544 */         sb.append("SELECT Id ");
/*  545 */         sb.append(" FROM ").append(tableName);
/*  546 */         sb.append(" WHERE (Id = '").append(entity.getObjectIdentity()).append("') ");
/*  547 */         if (!startsWithAND)
/*  548 */           sb.append("AND ");
/*  549 */         sb.append(whereClause).append(" ");
/*  550 */         String sql = sb.toString();
/*      */         
/*  552 */         Repository repository = entity.getRepository();
/*  553 */         ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  554 */         SearchSQL searchSQL = new SearchSQL(sql);
/*  555 */         SearchScope scope = new SearchScope(jaceObjStore);
/*  556 */         RepositoryRowSet rowSet = scope.fetchRows(searchSQL, null, null, Boolean.FALSE);
/*  557 */         if (rowSet.isEmpty())
/*      */         {
/*  559 */           setDefaultDispositionProperties(entity);
/*      */         }
/*      */         else
/*      */         {
/*  563 */           String schedCutoffBasePropName = getCutoffBase();
/*  564 */           if ("EventDate".equalsIgnoreCase(schedCutoffBasePropName))
/*      */           {
/*  566 */             setCutoffDataOnEntity(new Date(), entity);
/*  567 */             Logger.info(RMLogCode.RECALCULATE_CUTOFF_PERFORMED, new Object[] { entityIdent });
/*      */           }
/*      */           else
/*      */           {
/*  571 */             Date entityCutoffBaseValue = entityProps.getDateTimeValue(schedCutoffBasePropName);
/*  572 */             if (entityCutoffBaseValue == null)
/*      */             {
/*  574 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ENTITY_MISSING_CUTOFF_BASE_DATE, new Object[] { entityIdent, schedCutoffBasePropName });
/*      */             }
/*      */             
/*  577 */             Date cutoffDate = applyDateOffset(entityCutoffBaseValue, cutoffOffset);
/*  578 */             setCutoffDataOnEntity(cutoffDate, entity);
/*  579 */             Logger.info(RMLogCode.RECALCULATE_CUTOFF_PERFORMED, new Object[] { entityIdent });
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  584 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  588 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  592 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  596 */       if (establishedSubject) {
/*  597 */         P8CE_Util.disassociateSubject();
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
/*      */   private void setDefaultDispositionProperties(BaseEntity entity)
/*      */   {
/*  610 */     Tracer.traceMethodEntry(new Object[] { entity });
/*  611 */     RMProperties props = entity.getProperties();
/*      */     
/*  613 */     props.putDateTimeValue("CutoffDate", (Date)null);
/*  614 */     props.putDateTimeValue("CurrentPhaseExecutionDate", (Date)null);
/*  615 */     props.putIntegerValue("CurrentPhaseExecutionStatus", (Integer)null);
/*  616 */     props.putGuidValue("CurrentPhaseID", (String)null);
/*  617 */     props.putIntegerValue("CurrentActionType", (Integer)null);
/*  618 */     props.putGuidValue("CutoffInheritedFrom", (String)null);
/*  619 */     props.putBooleanValue("IsScreeningRequired", Boolean.FALSE);
/*  620 */     props.putObjectValue("CurrentPhaseAction", (Object)null);
/*  621 */     props.putStringValue("CurrentPhaseReviewDecision", (String)null);
/*  622 */     props.putDateTimeValue("CurrentPhaseDecisionDate", (Date)null);
/*  623 */     props.putBooleanValue("IsFinalPhaseCompleted", Boolean.FALSE);
/*  624 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setCutoffDataOnEntity(Date cutoffDate, BaseEntity entity)
/*      */   {
/*  635 */     Tracer.traceMethodEntry(new Object[] { cutoffDate, entity });
/*  636 */     RMProperties props = entity.getProperties();
/*      */     
/*  638 */     Date entityCOD = props.getDateTimeValue("CutoffDate");
/*  639 */     Date entityCPED = props.getDateTimeValue("CurrentPhaseExecutionDate");
/*  640 */     Integer entityCPES = props.getIntegerValue("CurrentPhaseExecutionStatus");
/*  641 */     Integer entityRCPR = props.getIntegerValue("RecalculatePhaseRetention");
/*      */     
/*  643 */     if ((entity instanceof Record))
/*      */     {
/*  645 */       List<Container> recordContainers = entity.getContainedBy();
/*  646 */       if ((recordContainers.size() > 1) && (((entityCPED != null) && (entityCPED.before(cutoffDate))) || (entityCOD != null)))
/*      */       {
/*      */ 
/*      */ 
/*  650 */         Tracer.traceMethodExit(new Object[0]);
/*  651 */         return;
/*      */       }
/*      */     }
/*      */     
/*  655 */     if ((entityCPES == null) || (entityCPES.intValue() < 0))
/*      */     {
/*  657 */       props.putIntegerValue("CurrentPhaseExecutionStatus", Integer.valueOf(RMWorkflowStatus.NotStarted.getIntValue()));
/*      */     }
/*      */     
/*  660 */     Date trimmedCutoffDate = RALBaseEntity.setTimeToZero(cutoffDate).getTime();
/*  661 */     props.putDateTimeValue("CurrentPhaseExecutionDate", trimmedCutoffDate);
/*      */     
/*  663 */     if ((entityRCPR != null) && (5 == entityRCPR.intValue()))
/*      */     {
/*  665 */       props.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(0));
/*      */     }
/*      */     
/*  668 */     Date schedDLM = getProperties().getDateTimeValue("DateLastModified");
/*  669 */     props.putDateTimeValue("LastSweepDate", schedDLM);
/*      */     
/*  671 */     DispositionAction schedCutoffAction = getCutoffAction();
/*  672 */     props.putObjectValue("CurrentPhaseAction", schedCutoffAction);
/*      */     
/*  674 */     ((RALBaseEntity)entity).internalSave(RMRefreshMode.Refresh);
/*  675 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   private Date applyDateOffset(Date baseDate, Integer[] offset)
/*      */   {
/*  688 */     int years = offset[0] != null ? offset[0].intValue() : 0;
/*  689 */     int months = offset[1] != null ? offset[1].intValue() : 0;
/*  690 */     int days = offset[2] != null ? offset[2].intValue() : 0;
/*      */     
/*  692 */     return RALBaseEntity.addToDate(baseDate, years, months, days);
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
/*      */   private Date getEventFiredDate(DispositionTrigger dispTrigger, BaseEntity entity)
/*      */   {
/*  706 */     Tracer.traceMethodEntry(new Object[] { dispTrigger, entity });
/*  707 */     String cutoffBasePropName = getCutoffBase();
/*  708 */     if ((cutoffBasePropName == null) || (cutoffBasePropName.trim().length() == 0))
/*      */     {
/*  710 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSTION_SCHEDULE_MISSING_CUTOFF_BASE, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/*  713 */     Date eventFiredDate = null;
/*  714 */     Date schedCalendarDate = getCalendarDate();
/*  715 */     if (schedCalendarDate != null)
/*      */     {
/*  717 */       eventFiredDate = getCalendarEventDate(entity, cutoffBasePropName);
/*      */     }
/*      */     else
/*      */     {
/*  721 */       DispositionTriggerType triggerType = dispTrigger.getTriggerType();
/*  722 */       if (DispositionTriggerType.PredefinedDateTrigger == triggerType)
/*      */       {
/*  724 */         eventFiredDate = getPredefinedEventDate(entity, cutoffBasePropName, dispTrigger);
/*      */       }
/*  726 */       else if (DispositionTriggerType.ExternalEventTrigger == triggerType)
/*      */       {
/*  728 */         eventFiredDate = getExternalEventDate(entity, cutoffBasePropName, dispTrigger);
/*      */       }
/*  730 */       else if (DispositionTriggerType.RecurringEventTigger == triggerType)
/*      */       {
/*  732 */         eventFiredDate = getCyclicEventDate(entity, cutoffBasePropName, dispTrigger);
/*      */       }
/*      */       else
/*      */       {
/*  736 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_TRIGGER_INVALID_TRIGGER_TYPE, new Object[] { dispTrigger.getObjectIdentity(), triggerType });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  741 */     Tracer.traceMethodExit(new Object[] { eventFiredDate });
/*  742 */     return eventFiredDate;
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
/*      */   private Date getCyclicEventDate(BaseEntity entity, String cutoffBasePropName, DispositionTrigger dispTrigger)
/*      */   {
/*  757 */     Tracer.traceMethodEntry(new Object[] { entity, cutoffBasePropName, dispTrigger });
/*  758 */     Date cyclicEventDate = null;
/*  759 */     if ("EventDate".equalsIgnoreCase(cutoffBasePropName))
/*      */     {
/*  761 */       Date triggerDateTime = dispTrigger.getDateTime();
/*  762 */       Date dispSchedAllocDate = entity.getProperties().getDateTimeValue("DisposalScheduleAllocationDate");
/*  763 */       dispSchedAllocDate = dispSchedAllocDate != null ? RALBaseEntity.setTimeToZero(dispSchedAllocDate).getTime() : null;
/*      */       
/*  765 */       if (dispSchedAllocDate != null)
/*      */       {
/*  767 */         if ((triggerDateTime.after(dispSchedAllocDate)) || (triggerDateTime.equals(dispSchedAllocDate)))
/*      */         {
/*  769 */           cyclicEventDate = triggerDateTime;
/*      */         }
/*      */         else
/*      */         {
/*  773 */           Integer[] cyclicPeriod = dispTrigger.getRecurringCyclePeriod();
/*  774 */           int years = cyclicPeriod[0] != null ? cyclicPeriod[0].intValue() : 0;
/*  775 */           int months = cyclicPeriod[1] != null ? cyclicPeriod[1].intValue() : 0;
/*  776 */           int days = cyclicPeriod[2] != null ? cyclicPeriod[2].intValue() : 0;
/*  777 */           int cycleNumber = 0;
/*  778 */           Date frequencyDate = new Date(triggerDateTime.getTime());
/*  779 */           while (frequencyDate.before(dispSchedAllocDate))
/*      */           {
/*  781 */             cycleNumber++;
/*  782 */             frequencyDate = RALBaseEntity.addToDate(frequencyDate, cycleNumber * years, cycleNumber * months, cycleNumber * days);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  787 */           cyclicEventDate = frequencyDate;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  793 */       cyclicEventDate = entity.getProperties().getDateTimeValue(cutoffBasePropName);
/*      */     }
/*      */     
/*  796 */     if (cyclicEventDate == null)
/*      */     {
/*  798 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ENTITY_MISSING_CUTOFF_BASE_DATE, new Object[] { entity.getObjectIdentity(), cutoffBasePropName });
/*      */     }
/*      */     
/*  801 */     Tracer.traceMethodExit(new Object[] { cyclicEventDate });
/*  802 */     return cyclicEventDate;
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
/*      */   private Date getExternalEventDate(BaseEntity entity, String cutoffBasePropName, DispositionTrigger dispTrigger)
/*      */   {
/*  817 */     Tracer.traceMethodEntry(new Object[] { entity, cutoffBasePropName, dispTrigger });
/*  818 */     Date externalEventDate = null;
/*  819 */     if ("EventDate".equalsIgnoreCase(cutoffBasePropName))
/*      */     {
/*  821 */       externalEventDate = dispTrigger.getExternalEventOccurrenceDate();
/*      */     }
/*      */     else
/*      */     {
/*  825 */       externalEventDate = entity.getProperties().getDateTimeValue(cutoffBasePropName);
/*      */     }
/*      */     
/*  828 */     if (externalEventDate == null)
/*      */     {
/*  830 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ENTITY_MISSING_CUTOFF_BASE_DATE, new Object[] { entity.getObjectIdentity(), cutoffBasePropName });
/*      */     }
/*      */     
/*  833 */     Tracer.traceMethodExit(new Object[] { externalEventDate });
/*  834 */     return externalEventDate;
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
/*      */   private Date getPredefinedEventDate(BaseEntity entity, String cutoffBasePropName, DispositionTrigger dispTrigger)
/*      */   {
/*  849 */     Tracer.traceMethodEntry(new Object[] { entity, cutoffBasePropName, dispTrigger });
/*  850 */     Date predefinedEventDate = null;
/*  851 */     if ("EventDate".equalsIgnoreCase(cutoffBasePropName))
/*      */     {
/*  853 */       predefinedEventDate = dispTrigger.getDateTime();
/*      */     }
/*      */     else
/*      */     {
/*  857 */       predefinedEventDate = entity.getProperties().getDateTimeValue(cutoffBasePropName);
/*      */     }
/*      */     
/*  860 */     if (predefinedEventDate == null)
/*      */     {
/*  862 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ENTITY_MISSING_CUTOFF_BASE_DATE, new Object[] { entity.getObjectIdentity(), cutoffBasePropName });
/*      */     }
/*      */     
/*  865 */     Tracer.traceMethodExit(new Object[] { predefinedEventDate });
/*  866 */     return predefinedEventDate;
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
/*      */   private Date getCalendarEventDate(BaseEntity entity, String cutoffBasePropName)
/*      */   {
/*  880 */     Tracer.traceMethodEntry(new Object[] { entity, cutoffBasePropName });
/*  881 */     Date calendarEventDate = null;
/*  882 */     if ("EventDate".equalsIgnoreCase(cutoffBasePropName))
/*      */     {
/*  884 */       calendarEventDate = getCalendarDate();
/*      */     }
/*      */     else
/*      */     {
/*  888 */       calendarEventDate = entity.getProperties().getDateTimeValue(cutoffBasePropName);
/*      */     }
/*      */     
/*  891 */     if (calendarEventDate == null)
/*      */     {
/*  893 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ENTITY_MISSING_CUTOFF_BASE_DATE, new Object[] { entity.getObjectIdentity(), cutoffBasePropName });
/*      */     }
/*      */     
/*  896 */     Tracer.traceMethodExit(new Object[] { calendarEventDate });
/*  897 */     return calendarEventDate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void triggerNextPhase(BaseEntity entity)
/*      */   {
/*  908 */     Tracer.traceMethodEntry(new Object[] { entity });
/*      */     
/*  910 */     DispositionPhaseList schedPhases = getDispositionPhases();
/*  911 */     if (schedPhases.size() > 0)
/*      */     {
/*  913 */       DispositionPhase currentPhase = RALDispositionLogic.getCurrentPhaseForEntity(entity);
/*  914 */       if (currentPhase == null)
/*      */       {
/*  916 */         setPhaseDataOnEntity(entity, schedPhases.get(1));
/*      */       }
/*      */       else
/*      */       {
/*  920 */         int currentPhaseNumber = currentPhase.getPhaseNumber().intValue();
/*  921 */         if (currentPhaseNumber < schedPhases.size())
/*      */         {
/*  923 */           DispositionPhase nextPhase = schedPhases.get(currentPhaseNumber);
/*  924 */           setPhaseDataOnEntity(entity, nextPhase);
/*      */         }
/*      */         else
/*      */         {
/*  928 */           RALDispositionLogic.setFinalPhaseDataOnEntity(entity);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  934 */       RALDispositionLogic.setFinalPhaseDataOnEntity(entity);
/*      */     }
/*  936 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   void setPhaseDataOnEntity(BaseEntity entity, DispositionPhase phase)
/*      */   {
/*  950 */     Tracer.traceMethodEntry(new Object[] { entity, phase });
/*      */     
/*  952 */     P8CE_DispositionPhaseImpl phaseImpl = (P8CE_DispositionPhaseImpl)phase;
/*  953 */     Date curPhaseExecDate = null;
/*  954 */     int recalculatePhaseRetentionValue = -1;
/*  955 */     AlternateRetentionList phaseAltRetents = phase.getAlternateRetentions();
/*  956 */     if ((phaseAltRetents != null) && (phaseAltRetents.size() > 0))
/*      */     {
/*  958 */       curPhaseExecDate = phaseImpl.calcAltRetentionEffectiveDate(entity);
/*  959 */       if (curPhaseExecDate != null)
/*      */       {
/*  961 */         recalculatePhaseRetentionValue = 2;
/*      */       }
/*      */       else
/*      */       {
/*  965 */         curPhaseExecDate = phaseImpl.calcDefaultRetentionEffectiveDate(entity);
/*  966 */         recalculatePhaseRetentionValue = 2;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  971 */       curPhaseExecDate = phaseImpl.calcDefaultRetentionEffectiveDate(entity);
/*  972 */       recalculatePhaseRetentionValue = 0;
/*      */     }
/*      */     
/*  975 */     RMProperties entityProps = entity.getProperties();
/*  976 */     entityProps.putGuidValue("CurrentPhaseID", phase.getObjectIdentity());
/*  977 */     entityProps.putIntegerValue("CurrentPhaseExecutionStatus", Integer.valueOf(RMWorkflowStatus.NotStarted.getIntValue()));
/*      */     
/*  979 */     DispositionAction phaseAction = phase.getPhaseAction();
/*  980 */     if (phaseAction != null)
/*      */     {
/*  982 */       entityProps.putObjectValue("CurrentPhaseAction", phaseAction);
/*      */       
/*  984 */       DispositionActionType phaseActionType = phaseAction.getActionType();
/*  985 */       entityProps.putIntegerValue("CurrentActionType", Integer.valueOf(phaseActionType.getIntValue()));
/*      */       
/*  987 */       if ((phaseActionType == DispositionActionType.Export) || (phaseActionType == DispositionActionType.InterimTransfer) || (phaseActionType == DispositionActionType.Transfer))
/*      */       {
/*      */ 
/*      */ 
/*  991 */         String exportDestination = phase.getExportDestination();
/*  992 */         entityProps.putStringValue("CurrentPhaseExportDestination", exportDestination);
/*  993 */         Object exportFormat = phase.getExportFormat();
/*  994 */         entityProps.putObjectValue("CurrentPhaseExportFormat", exportFormat);
/*      */       }
/*  996 */       else if (phaseActionType == DispositionActionType.Review)
/*      */       {
/*  998 */         Date currentPhaseDecisionDate = entityProps.getDateTimeValue("CurrentPhaseDecisionDate");
/*  999 */         entityProps.putDateTimeValue("DateOfLastReview", currentPhaseDecisionDate);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1004 */       entityProps.putObjectValue("CurrentPhaseAction", null);
/*      */     }
/*      */     
/* 1007 */     if (curPhaseExecDate != null)
/*      */     {
/* 1009 */       entityProps.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(recalculatePhaseRetentionValue));
/* 1010 */       entityProps.putDateTimeValue("CurrentPhaseExecutionDate", curPhaseExecDate);
/* 1011 */       boolean isScreeningReq = phase.isScreeningRequired();
/* 1012 */       entityProps.putBooleanValue("IsScreeningRequired", Boolean.valueOf(isScreeningReq));
/* 1013 */       Date phaseDLM = phase.getProperties().getDateTimeValue("DateLastModified");
/* 1014 */       entityProps.putDateTimeValue("LastSweepDate", phaseDLM);
/*      */     }
/*      */     else
/*      */     {
/* 1018 */       entityProps.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(recalculatePhaseRetentionValue));
/*      */       
/* 1020 */       entityProps.putObjectValue("CurrentPhaseAction", null);
/* 1021 */       entityProps.putDateTimeValue("CurrentPhaseExecutionDate", null);
/* 1022 */       entityProps.putDateTimeValue("CurrentPhaseDecisionDate", null);
/* 1023 */       entityProps.putStringValue("CurrentPhaseReviewDecision", null);
/*      */     }
/*      */     
/* 1026 */     ((RALBaseEntity)entity).internalSave(RMRefreshMode.Refresh);
/* 1027 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionPhase createNewPhase(String phaseName)
/*      */   {
/* 1035 */     Tracer.traceMethodEntry(new Object[] { phaseName });
/* 1036 */     DispositionPhase result = createNewPhase(phaseName, null);
/* 1037 */     Tracer.traceMethodExit(new Object[] { result });
/* 1038 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionPhase createNewPhase(String phaseName, String idStr)
/*      */   {
/* 1046 */     Tracer.traceMethodEntry(new Object[] { phaseName, idStr });
/* 1047 */     Util.ckInvalidStrParam("phaseName", phaseName);
/*      */     
/* 1049 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1052 */       establishedSubject = P8CE_Util.associateSubject();
/* 1053 */       DispositionPhase result = P8CE_DispositionPhaseImpl.createNew(getRepository(), phaseName, this, idStr);
/*      */       
/* 1055 */       Tracer.traceMethodExit(new Object[] { result });
/* 1056 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1060 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1064 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1068 */       if (establishedSubject) {
/* 1069 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<RecordCategory> getAssociatedRecordCategories()
/*      */   {
/* 1078 */     Tracer.traceMethodEntry(new Object[0]);
/* 1079 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1082 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1084 */       List<FilterElement> jaceRecCatFEs = P8CE_RecordCategoryImpl.getMandatoryJaceFEs();
/* 1085 */       IGenerator<RecordCategory> generator = P8CE_RecordCategoryImpl.getGenerator();
/* 1086 */       List<RecordCategory> results = fetchMVObjPropValueAsList(jaceRecCatFEs, "AssociatedRecordCategories", generator);
/*      */       
/* 1088 */       Tracer.traceMethodExit(new Object[] { results });
/* 1089 */       return results;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1093 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1097 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "AssociatedRecordCategories" });
/*      */     }
/*      */     finally
/*      */     {
/* 1101 */       if (establishedSubject) {
/* 1102 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<RecordFolder> getAssociatedRecordFolders()
/*      */   {
/* 1111 */     Tracer.traceMethodEntry(new Object[0]);
/* 1112 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1115 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1117 */       List<FilterElement> jaceRecFolderFEs = P8CE_RecordFolderImpl.getMandatoryJaceFEs();
/* 1118 */       IGenerator<RecordFolder> generator = P8CE_RecordFolderImpl.getGenerator();
/* 1119 */       List<RecordFolder> results = fetchMVObjPropValueAsList(jaceRecFolderFEs, "AssociatedRecordFolders", generator);
/*      */       
/* 1121 */       Tracer.traceMethodExit(new Object[] { results });
/* 1122 */       return results;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1126 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1130 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "AssociatedRecordFolders" });
/*      */     }
/*      */     finally
/*      */     {
/* 1134 */       if (establishedSubject) {
/* 1135 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<RecordType> getAssociatedRecordTypes()
/*      */   {
/* 1144 */     Tracer.traceMethodEntry(new Object[0]);
/* 1145 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1148 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1150 */       List<FilterElement> jaceRecTypeFEs = P8CE_RecordTypeImpl.getMandatoryJaceFEs();
/* 1151 */       IGenerator<RecordType> generator = P8CE_RecordTypeImpl.getGenerator();
/* 1152 */       List<RecordType> results = fetchMVObjPropValueAsList(jaceRecTypeFEs, "AssociatedRecordTypes", generator);
/*      */       
/* 1154 */       Tracer.traceMethodExit(new Object[] { results });
/* 1155 */       return results;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1159 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1163 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "AssociatedRecordTypes" });
/*      */     }
/*      */     finally
/*      */     {
/* 1167 */       if (establishedSubject) {
/* 1168 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Date getEffectiveDateModified()
/*      */   {
/* 1177 */     Tracer.traceMethodEntry(new Object[0]);
/* 1178 */     Date result = P8CE_Util.getJacePropertyAsDate(this, "EffectiveDateModified");
/* 1179 */     Tracer.traceMethodExit(new Object[] { result });
/* 1180 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Boolean isScreeningRequired()
/*      */   {
/* 1188 */     Tracer.traceMethodEntry(new Object[0]);
/* 1189 */     Boolean result = P8CE_Util.getJacePropertyAsBoolean(this, "IsScreeningRequired");
/* 1190 */     Tracer.traceMethodExit(new Object[] { result });
/* 1191 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScreeningRequired(Boolean value)
/*      */   {
/* 1199 */     Tracer.traceMethodEntry(new Object[] { value });
/* 1200 */     getProperties().putBooleanValue("IsScreeningRequired", value);
/* 1201 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Boolean isTriggerChanged()
/*      */   {
/* 1209 */     Tracer.traceMethodEntry(new Object[0]);
/* 1210 */     Boolean result = P8CE_Util.getJacePropertyAsBoolean(this, "IsTriggerChanged");
/* 1211 */     Tracer.traceMethodExit(new Object[] { result });
/* 1212 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getReasonForChange()
/*      */   {
/* 1220 */     Tracer.traceMethodEntry(new Object[0]);
/* 1221 */     String result = P8CE_Util.getJacePropertyAsString(this, "ReasonForChange");
/* 1222 */     Tracer.traceMethodExit(new Object[] { result });
/* 1223 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReasonForChange(String reason)
/*      */   {
/* 1231 */     this.jaceCustomObject.getProperties().putValue("ReasonForChange", reason);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAuditXML()
/*      */   {
/* 1239 */     return P8CE_Util.getJacePropertyAsString(this, "SweepAuditXML");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getSweepState()
/*      */   {
/* 1247 */     Tracer.traceMethodEntry(new Object[0]);
/* 1248 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "SweepState");
/* 1249 */     Tracer.traceMethodExit(new Object[] { result });
/* 1250 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void delete()
/*      */   {
/* 1259 */     Tracer.traceMethodEntry(new Object[0]);
/* 1260 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1263 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1265 */       long startTime = System.currentTimeMillis();
/*      */       
/*      */ 
/* 1268 */       this.jaceCustomObject.delete();
/* 1269 */       this.jaceCustomObject.save(RefreshMode.NO_REFRESH);
/* 1270 */       long stopTime = System.currentTimeMillis();
/* 1271 */       Tracer.traceExtCall("CustomObject.save", startTime, stopTime, Integer.valueOf(1), null, new Object[0]);
/*      */       
/* 1273 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1277 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1281 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.DispositionSchedule });
/*      */     }
/*      */     finally
/*      */     {
/* 1285 */       if (establishedSubject) {
/* 1286 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void save(RMRefreshMode jarmRefreshMode)
/*      */   {
/* 1296 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 1297 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1300 */       establishedSubject = P8CE_Util.associateSubject();
/* 1301 */       RALDispositionLogic.validateDispositionSchedule((RALBaseRepository)getRepository(), this);
/*      */       
/* 1303 */       RMDomain jarmDomain = this.repository.getDomain();
/* 1304 */       Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 1305 */       RefreshMode jaceRefreshMode = jarmRefreshMode == RMRefreshMode.Refresh ? RefreshMode.REFRESH : RefreshMode.NO_REFRESH;
/*      */       
/* 1307 */       UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, jaceRefreshMode);
/* 1308 */       contributeToSaveBatch(jarmRefreshMode, jaceUB);
/*      */       
/* 1310 */       long startTime = System.currentTimeMillis();
/* 1311 */       jaceUB.updateBatch();
/* 1312 */       long stopTime = System.currentTimeMillis();
/* 1313 */       Tracer.traceExtCall("CustomObject.save", startTime, stopTime, Integer.valueOf(1), null, new Object[] { jaceRefreshMode });
/* 1314 */       this.isCreationPending = false;
/*      */       
/* 1316 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1320 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1324 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.DispositionSchedule });
/*      */     }
/*      */     finally
/*      */     {
/* 1328 */       if (establishedSubject) {
/* 1329 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void contributeToSaveBatch(RMRefreshMode jarmRefreshMode, UpdatingBatch jaceUB) {
/* 1335 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode, jaceUB });
/* 1336 */     jaceUB.add(this.jaceCustomObject, null);
/*      */     
/*      */ 
/*      */ 
/* 1340 */     if (this.phaseList != null)
/*      */     {
/* 1342 */       ((P8CE_DispositionPhaseListImpl)this.phaseList).contributeToSaveBatch(jarmRefreshMode, jaceUB);
/*      */     }
/* 1344 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void refresh()
/*      */   {
/* 1353 */     Tracer.traceMethodEntry(new Object[0]);
/* 1354 */     this.phaseList = null;
/* 1355 */     super.refresh();
/* 1356 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void refresh(RMPropertyFilter jarmFilter)
/*      */   {
/* 1365 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1366 */     this.phaseList = null;
/* 1367 */     super.refresh(jarmFilter);
/* 1368 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void refresh(String[] symbolicPropertyNames)
/*      */   {
/* 1377 */     Tracer.traceMethodEntry(new Object[] { symbolicPropertyNames });
/* 1378 */     this.phaseList = null;
/* 1379 */     super.refresh(symbolicPropertyNames);
/* 1380 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */ 
/*      */ 
/*      */   private List<DispositionPhase> orderPhases(List<DispositionPhase> origList)
/*      */   {
/* 1397 */     Tracer.traceMethodEntry(new Object[] { origList });
/* 1398 */     Collections.sort(origList, new P8CE_DispositionPhaseImpl.DispositionPhaseComparator());
/* 1399 */     Tracer.traceMethodExit(new Object[] { origList });
/* 1400 */     return origList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1409 */     return super.toString("P8CE_DispositionScheduleImpl", "DisposalScheduleName");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class Generator
/*      */     implements IGenerator<DispositionSchedule>
/*      */   {
/*      */     public DispositionSchedule create(Repository repository, Object jaceBaseObject)
/*      */     {
/* 1423 */       P8CE_DispositionScheduleImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 1424 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*      */       
/* 1426 */       String identity = "<undefined>";
/* 1427 */       if (jaceCustomObj.getProperties().isPropertyPresent("DisposalScheduleName")) {
/* 1428 */         identity = jaceCustomObj.getProperties().getStringValue("DisposalScheduleName");
/*      */       }
/* 1430 */       DispositionSchedule result = new P8CE_DispositionScheduleImpl(repository, identity, jaceCustomObj, false);
/*      */       
/* 1432 */       P8CE_DispositionScheduleImpl.Tracer.traceMethodExit(new Object[] { result });
/* 1433 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_DispositionScheduleImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */