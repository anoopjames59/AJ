/*      */ package com.ibm.jarm.ral.common;
/*      */ 
/*      */ import com.ibm.jarm.api.constants.DataModelType;
/*      */ import com.ibm.jarm.api.constants.DataType;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*      */ import com.ibm.jarm.api.core.AlternateRetention;
/*      */ import com.ibm.jarm.api.core.AlternateRetentionList;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.DispositionAction;
/*      */ import com.ibm.jarm.api.core.DispositionAllocatable;
/*      */ import com.ibm.jarm.api.core.DispositionPhase;
/*      */ import com.ibm.jarm.api.core.DispositionPhaseList;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.DispositionTrigger;
/*      */ import com.ibm.jarm.api.core.Dispositionable;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.Holdable;
/*      */ import com.ibm.jarm.api.core.Persistable;
/*      */ import com.ibm.jarm.api.core.RMCustomObject;
/*      */ import com.ibm.jarm.api.core.RMFactory.Container;
/*      */ import com.ibm.jarm.api.core.RMFactory.RMCustomObject;
/*      */ import com.ibm.jarm.api.core.RMWorkflowDefinition;
/*      */ import com.ibm.jarm.api.core.RecordContainer;
/*      */ import com.ibm.jarm.api.core.RecordType;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMProperty;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.RMLString;
/*      */ import com.ibm.jarm.ral.p8ce.P8CE_FilePlanRepositoryImpl;
/*      */ import java.io.StringReader;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.InputSource;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RALDispositionLogic
/*      */ {
/*   64 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String AUTODESTROY_WFL_NAME = "__InternalWFLDefUsedByAutoDelAction__";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String AUTODESTROY_WFL_ID_STR = "{52CB24BA-B6DA-4D90-9E38-2BA9FB8D13E7}";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String DefaultCutoffBaseValue = "EventDate";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int PhaseRecalculationRequired = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int PhaseRecalculationNotRequired = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int CutoffRecalculationRequired = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int CutoffRecalculationRequiredForInternalEvent = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int CutoffRecalculationNotRequiredForInternalEvent = 4;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int ScheduleChanged = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void validateDispositionTrigger(FilePlanRepository repository, DispositionTrigger dispTrigger)
/*      */   {
/*  115 */     Tracer.traceMethodEntry(new Object[] { repository, dispTrigger });
/*  116 */     RMProperties props = dispTrigger.getProperties();
/*  117 */     if (dispTrigger.isCreationPending())
/*      */     {
/*      */ 
/*      */ 
/*  121 */       String proposedName = null;
/*  122 */       if (props.isPropertyPresent("DisposalTriggerName"))
/*      */       {
/*  124 */         proposedName = props.getStringValue("DisposalTriggerName");
/*  125 */         if (proposedName != null)
/*      */         {
/*  127 */           proposedName = proposedName.trim();
/*  128 */           if (proposedName.length() == 0)
/*  129 */             proposedName = null;
/*      */         }
/*      */       }
/*  132 */       if (proposedName == null)
/*      */       {
/*  134 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "DisposalTriggerName" });
/*      */       }
/*  136 */       if (!((RALBaseRepository)repository).customObjNameIsUnique("DisposalTrigger", "DisposalTriggerName", proposedName, null))
/*      */       {
/*  138 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_TRIGGER_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  143 */       RMProperty prop = null;
/*      */       
/*  145 */       if ((prop = RALBaseEntity.getPropIfModified(props, "DisposalTriggerName")) != null)
/*      */       {
/*  147 */         String proposedName = prop.getStringValue();
/*  148 */         if (proposedName != null)
/*      */         {
/*  150 */           proposedName = proposedName.trim();
/*  151 */           if (proposedName.length() == 0)
/*  152 */             proposedName = null;
/*      */         }
/*  154 */         if (proposedName == null)
/*      */         {
/*  156 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "DisposalTriggerName" });
/*      */         }
/*      */         
/*  159 */         String ident = dispTrigger.getObjectIdentity();
/*  160 */         if (!((RALBaseRepository)repository).customObjNameIsUnique("DisposalTrigger", "DisposalTriggerName", proposedName, ident))
/*      */         {
/*  162 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_TRIGGER_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  168 */     DispositionTriggerType triggerType = null;
/*  169 */     if (props.isPropertyPresent("EventType"))
/*      */     {
/*  171 */       Integer rawTypeVal = props.getIntegerValue("EventType");
/*  172 */       if (rawTypeVal != null)
/*      */       {
/*  174 */         triggerType = DispositionTriggerType.getInstanceFromInt(rawTypeVal.intValue());
/*      */       }
/*      */     }
/*  177 */     if (triggerType == null)
/*      */     {
/*  179 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "EventType" });
/*      */     }
/*      */     
/*      */ 
/*  183 */     switch (triggerType)
/*      */     {
/*      */     case PredefinedDateTrigger: 
/*  186 */       validateNonEmptyProp(props, "DateTime");
/*  187 */       break;
/*      */     
/*      */     case InternalEventTrigger: 
/*  190 */       validateNonEmptyProp(props, "AGGREGATION");
/*  191 */       validateNonEmptyProp(props, "ConditionXML");
/*  192 */       break;
/*      */     
/*      */     case RecurringEventTigger: 
/*  195 */       validateNonEmptyProp(props, "DateTime");
/*  196 */       Integer[] cycleValues = extractTimePeriod(props, new String[] { "CycleYears", "CycleMonths", "CycleDays" });
/*      */       
/*      */ 
/*      */ 
/*  200 */       int sum = 0;
/*  201 */       for (Integer cycleVal : cycleValues)
/*      */       {
/*  203 */         if ((cycleVal != null) && (cycleVal.intValue() > 0))
/*  204 */           sum += cycleVal.intValue();
/*      */       }
/*  206 */       if (sum == 0)
/*      */       {
/*  208 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_CYCLIC_PERIOD_VALUES, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*  217 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void validateDispositionAction(FilePlanRepository repository, DispositionAction dispAction)
/*      */   {
/*  228 */     Tracer.traceMethodEntry(new Object[] { repository, dispAction });
/*  229 */     RMProperties props = dispAction.getProperties();
/*  230 */     if (dispAction.isCreationPending())
/*      */     {
/*      */ 
/*      */ 
/*  234 */       String proposedName = null;
/*  235 */       if (props.isPropertyPresent("ActionName"))
/*      */       {
/*  237 */         proposedName = props.getStringValue("ActionName");
/*  238 */         if (proposedName != null)
/*      */         {
/*  240 */           proposedName = proposedName.trim();
/*  241 */           if (proposedName.length() == 0)
/*  242 */             proposedName = null;
/*      */         }
/*      */       }
/*  245 */       if (proposedName == null)
/*      */       {
/*  247 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "ActionName" });
/*      */       }
/*  249 */       if (!((RALBaseRepository)repository).customObjNameIsUnique("Action1", "ActionName", proposedName, null))
/*      */       {
/*  251 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_ACTION_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  256 */       RMProperty prop = null;
/*      */       
/*  258 */       if ((prop = RALBaseEntity.getPropIfModified(props, "ActionName")) != null)
/*      */       {
/*  260 */         String proposedName = prop.getStringValue();
/*  261 */         if (proposedName != null)
/*      */         {
/*  263 */           proposedName = proposedName.trim();
/*  264 */           if (proposedName.length() == 0)
/*  265 */             proposedName = null;
/*      */         }
/*  267 */         if (proposedName == null)
/*      */         {
/*  269 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "ActionName" });
/*      */         }
/*      */         
/*  272 */         String ident = dispAction.getObjectIdentity();
/*  273 */         if (!((RALBaseRepository)repository).customObjNameIsUnique("Action1", "ActionName", proposedName, ident))
/*      */         {
/*  275 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_ACTION_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  280 */     if (dispAction.getActionType() == DispositionActionType.AutoDestroy)
/*      */     {
/*      */ 
/*  283 */       RMWorkflowDefinition autoDestroyWFL = ((P8CE_FilePlanRepositoryImpl)repository).getAutoDestroyWorkflow("{52CB24BA-B6DA-4D90-9E38-2BA9FB8D13E7}", "__InternalWFLDefUsedByAutoDelAction__");
/*      */       
/*  285 */       dispAction.setAssociatedWorkflow(autoDestroyWFL);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  290 */       Object rawWflValue = validateNonEmptyProp(props, "DefaultWorkflow");
/*      */       
/*  292 */       if ((rawWflValue instanceof RALBaseEntity))
/*      */       {
/*  294 */         String className = ((RALBaseEntity)rawWflValue).getClassName();
/*  295 */         if ((className != null) && (!"WorkflowDefinition".equalsIgnoreCase(className)))
/*      */         {
/*  297 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_ACTION_WRONG_WFL_CLASS, new Object[] { className });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  302 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void validateDispositionSchedule(RALBaseRepository repository, DispositionSchedule dispSchedule)
/*      */   {
/*  313 */     Tracer.traceMethodEntry(new Object[] { repository, dispSchedule });
/*  314 */     RMProperties props = dispSchedule.getProperties();
/*      */     
/*      */ 
/*  317 */     if (!dispSchedule.isCreationPending())
/*      */     {
/*  319 */       String reasonForChangeValue = null;
/*  320 */       if (props.isPropertyPresent("ReasonForChange"))
/*      */       {
/*  322 */         reasonForChangeValue = props.getStringValue("ReasonForChange");
/*  323 */         if (reasonForChangeValue != null)
/*      */         {
/*  325 */           if (reasonForChangeValue.trim().length() == 0) {
/*  326 */             reasonForChangeValue = null;
/*      */           }
/*      */         }
/*      */       }
/*  330 */       if (reasonForChangeValue == null)
/*      */       {
/*  332 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "ReasonForChange" });
/*      */       }
/*      */     }
/*      */     
/*  336 */     String proposedName = null;
/*  337 */     if (props.isPropertyPresent("DisposalScheduleName"))
/*      */     {
/*  339 */       proposedName = props.getStringValue("DisposalScheduleName");
/*  340 */       if (proposedName != null)
/*      */       {
/*  342 */         proposedName = proposedName.trim();
/*  343 */         if (proposedName.length() == 0)
/*  344 */           proposedName = null;
/*      */       }
/*  346 */       if (proposedName == null)
/*      */       {
/*  348 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "DisposalScheduleName" });
/*      */       }
/*      */     }
/*      */     
/*  352 */     if ((dispSchedule.isCreationPending()) && (proposedName == null))
/*      */     {
/*  354 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "DisposalScheduleName" });
/*      */     }
/*      */     
/*      */ 
/*  358 */     String scheduleIdent = dispSchedule.isCreationPending() ? null : dispSchedule.getObjectIdentity();
/*  359 */     if (!repository.customObjNameIsUnique("DisposalSchedule", "DisposalScheduleName", proposedName, scheduleIdent))
/*      */     {
/*  361 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_SCHEDULE_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*      */     }
/*      */     
/*  364 */     if (dispSchedule.isCreationPending())
/*      */     {
/*      */ 
/*  367 */       if ((!props.isPropertyPresent("CutOffBase")) || (props.getObjectValue("CutOffBase") == null))
/*      */       {
/*      */ 
/*  370 */         props.putStringValue("CutOffBase", "EventDate");
/*      */       }
/*  372 */       if ((!props.isPropertyPresent("EffectiveDateModified")) || (props.getObjectValue("EffectiveDateModified") == null))
/*      */       {
/*      */ 
/*  375 */         props.putDateTimeValue("EffectiveDateModified", new Date());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  380 */       if ((props.isPropertyPresent("CutOffBase")) && (props.getObjectValue("CutOffBase") == null))
/*      */       {
/*      */ 
/*  383 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "CutOffBase" });
/*      */       }
/*  385 */       if ((props.isPropertyPresent("EffectiveDateModified")) && (props.getObjectValue("EffectiveDateModified") == null))
/*      */       {
/*      */ 
/*  388 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "EffectiveDateModified" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  393 */     if (dispSchedule.isCreationPending())
/*      */     {
/*  395 */       boolean sawTrigger = false;
/*  396 */       if (props.isPropertyPresent("CutoffDisposalTrigger"))
/*      */       {
/*  398 */         Object dispTrigger = props.getObjectValue("CutoffDisposalTrigger");
/*  399 */         if ((dispTrigger == null) || (!(dispTrigger instanceof BaseEntity)) || (EntityType.DispositionTrigger != ((BaseEntity)dispTrigger).getEntityType()))
/*      */         {
/*      */ 
/*  402 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_SCHEDULE_BAD_DISP_TRIGGER, new Object[0]);
/*      */         }
/*  404 */         sawTrigger = true;
/*      */       }
/*  406 */       if ((!sawTrigger) && (props.isPropertyPresent("CalendarDate")))
/*      */       {
/*  408 */         Object calDateValue = props.getObjectValue("CalendarDate");
/*  409 */         if ((calDateValue == null) || (!(calDateValue instanceof Date)))
/*      */         {
/*  411 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_SCHEDULE_BAD_DISP_TRIGGER, new Object[0]);
/*      */         }
/*  413 */         sawTrigger = true;
/*      */       }
/*  415 */       if (!sawTrigger)
/*      */       {
/*  417 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_SCHEDULE_BAD_DISP_TRIGGER, new Object[0]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  422 */     DispositionPhaseList phases = dispSchedule.getDispositionPhases();
/*  423 */     if (phases != null)
/*      */     {
/*  425 */       for (DispositionPhase phase : phases)
/*      */       {
/*  427 */         validateDispositionPhase(repository, phase);
/*      */       }
/*      */     }
/*      */     
/*  431 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void validateDispositionPhase(RALBaseRepository repository, DispositionPhase phase)
/*      */   {
/*  442 */     Tracer.traceMethodEntry(new Object[] { repository, phase });
/*  443 */     RMProperties props = phase.getProperties();
/*      */     
/*      */ 
/*  446 */     String proposedName = null;
/*  447 */     if (props.isPropertyPresent("PhaseName"))
/*      */     {
/*  449 */       proposedName = props.getStringValue("PhaseName");
/*  450 */       if (proposedName != null)
/*      */       {
/*  452 */         proposedName = proposedName.trim();
/*  453 */         if (proposedName.length() == 0)
/*  454 */           proposedName = null;
/*      */       }
/*      */     }
/*  457 */     if (proposedName == null)
/*      */     {
/*  459 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "PhaseName" });
/*      */     }
/*      */     
/*      */ 
/*  463 */     validateNonEmptyProp(props, "IsScreeningRequired");
/*      */     
/*      */ 
/*  466 */     AlternateRetentionList altRetentions = phase.getAlternateRetentions();
/*  467 */     if (altRetentions != null)
/*      */     {
/*  469 */       for (AlternateRetention altRetention : altRetentions)
/*      */       {
/*  471 */         validateAlternateRetention(repository, altRetention);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  476 */     String[] retentionPropNames = { "RetentionPeriodYears", "RetentionPeriodMonths", "RetentionPeriodDays" };
/*      */     
/*      */ 
/*  479 */     Integer[] retentionValues = extractTimePeriod(props, retentionPropNames);
/*  480 */     boolean sawAtLeastOneValue = false;
/*  481 */     for (int i = 0; i < retentionPropNames.length; i++)
/*      */     {
/*  483 */       Integer retentionValue = retentionValues[i];
/*  484 */       if (retentionValue != null)
/*      */       {
/*  486 */         if (retentionValue.intValue() >= 0) {
/*  487 */           sawAtLeastOneValue = true;
/*      */         } else {
/*  489 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INVALID_PROPERTY_VALUE, new Object[] { retentionPropNames[i], Integer.valueOf(retentionValue.intValue()) });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  494 */     if ((!sawAtLeastOneValue) && ((altRetentions == null) || (altRetentions.size() == 0)))
/*      */     {
/*  496 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_DISP_PHASE_BAD_RETENTION_PERIOD, new Object[0]);
/*      */     }
/*      */     
/*  499 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void validateAlternateRetention(RALBaseRepository repository, AlternateRetention altRetention)
/*      */   {
/*  510 */     Tracer.traceMethodEntry(new Object[] { repository, altRetention });
/*  511 */     RMProperties props = altRetention.getProperties();
/*      */     
/*      */ 
/*  514 */     validateNonEmptyProp(props, "ConditionXML");
/*      */     
/*      */ 
/*  517 */     validateNonEmptyProp(props, "RetentionBase");
/*      */     
/*      */ 
/*  520 */     String[] retentionPropNames = { "RetentionPeriodYears", "RetentionPeriodMonths", "RetentionPeriodDays" };
/*      */     
/*      */ 
/*  523 */     Integer[] retentionValues = extractTimePeriod(props, retentionPropNames);
/*  524 */     boolean sawAtLeastOneValue = false;
/*  525 */     for (int i = 0; i < retentionPropNames.length; i++)
/*      */     {
/*  527 */       Integer retentionValue = retentionValues[i];
/*  528 */       if (retentionValue != null)
/*      */       {
/*  530 */         if (retentionValue.intValue() >= 0) {
/*  531 */           sawAtLeastOneValue = true;
/*      */         } else
/*  533 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INVALID_PROPERTY_VALUE, new Object[] { retentionPropNames[i], Integer.valueOf(retentionValue.intValue()) });
/*      */       }
/*      */     }
/*  536 */     if (!sawAtLeastOneValue)
/*      */     {
/*  538 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_ALT_RETENTION_BAD_RETENTION_PERIOD, new Object[0]);
/*      */     }
/*  540 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   public static void validateRecordType(RALBaseRepository repository, RecordType recordType)
/*      */   {
/*  545 */     Tracer.traceMethodEntry(new Object[] { repository, recordType });
/*  546 */     RMProperties props = recordType.getProperties();
/*  547 */     if (recordType.isCreationPending())
/*      */     {
/*      */ 
/*      */ 
/*  551 */       String proposedName = null;
/*  552 */       if (props.isPropertyPresent("RecordTypeName"))
/*      */       {
/*  554 */         proposedName = props.getStringValue("RecordTypeName");
/*  555 */         if (proposedName != null)
/*      */         {
/*  557 */           proposedName = proposedName.trim();
/*  558 */           if (proposedName.length() == 0)
/*  559 */             proposedName = null;
/*      */         }
/*      */       }
/*  562 */       if (proposedName == null)
/*      */       {
/*  564 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "RecordTypeName" });
/*      */       }
/*  566 */       if (!repository.customObjNameIsUnique("RecordType", "RecordTypeName", proposedName, null))
/*      */       {
/*  568 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_RECORD_TYPE_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*      */       }
/*      */       
/*      */ 
/*  572 */       Object dispSchedPropValue = null;
/*  573 */       if (props.isPropertyPresent("DisposalSchedule"))
/*      */       {
/*  575 */         dispSchedPropValue = props.getObjectValue("DisposalSchedule");
/*      */       }
/*  577 */       if (dispSchedPropValue == null)
/*      */       {
/*  579 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "DisposalSchedule" });
/*      */       }
/*      */       
/*      */ 
/*  583 */       Object dsAllocValue = null;
/*  584 */       if (props.isPropertyPresent("DisposalScheduleAllocationDate"))
/*      */       {
/*  586 */         dsAllocValue = props.getObjectValue("DisposalScheduleAllocationDate");
/*      */       }
/*  588 */       if (dsAllocValue == null)
/*      */       {
/*  590 */         props.putDateTimeValue("DisposalScheduleAllocationDate", new Date());
/*      */       }
/*      */       
/*      */ 
/*  594 */       Object recalcValue = null;
/*  595 */       if (props.isPropertyPresent("RecalculatePhaseRetention"))
/*      */       {
/*  597 */         recalcValue = props.getObjectValue("RecalculatePhaseRetention");
/*      */       }
/*  599 */       if (recalcValue == null)
/*      */       {
/*  601 */         props.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(5));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  606 */       RMProperty prop = null;
/*      */       
/*  608 */       if ((prop = RALBaseEntity.getPropIfModified(props, "RecordTypeName")) != null)
/*      */       {
/*  610 */         String proposedName = prop.getStringValue();
/*  611 */         if (proposedName != null)
/*      */         {
/*  613 */           proposedName = proposedName.trim();
/*  614 */           if (proposedName.length() == 0)
/*  615 */             proposedName = null;
/*      */         }
/*  617 */         if (proposedName == null)
/*      */         {
/*  619 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "RecordTypeName" });
/*      */         }
/*      */         
/*  622 */         String ident = recordType.getObjectIdentity();
/*  623 */         if (!repository.customObjNameIsUnique("RecordType", "RecordTypeName", proposedName, ident))
/*      */         {
/*  625 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_RECORD_TYPE_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*      */         }
/*      */       }
/*      */       
/*  629 */       if ((prop = RALBaseEntity.getPropIfModified(props, "DisposalSchedule")) != null)
/*      */       {
/*  631 */         if (prop.getObjectValue() == null)
/*      */         {
/*  633 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "DisposalSchedule" });
/*      */         }
/*      */         
/*  636 */         props.putDateTimeValue("DisposalScheduleAllocationDate", null);
/*  637 */         props.putDateTimeValue("LastSweepDate", null);
/*  638 */         props.putDateTimeValue("ExternalEventOccurrenceDate", null);
/*      */       }
/*      */     }
/*      */     
/*  642 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public static boolean hasCurrentPhaseChanged(BaseEntity entity, DispositionPhase phase)
/*      */   {
/*  658 */     Tracer.traceMethodEntry(new Object[] { entity });
/*  659 */     boolean result = true;
/*      */     
/*  661 */     DispositionPhase currentPhase = phase;
/*  662 */     if (currentPhase == null)
/*  663 */       currentPhase = getCurrentPhaseForEntity(entity);
/*  664 */     if (currentPhase != null)
/*      */     {
/*      */ 
/*      */ 
/*  668 */       Date curPhaseDLM = currentPhase.getProperties().getDateTimeValue("DateLastModified");
/*  669 */       Date entityLSD = entity.getProperties().getDateTimeValue("LastSweepDate");
/*  670 */       if ((curPhaseDLM != null) && (entityLSD != null))
/*      */       {
/*  672 */         long diff = curPhaseDLM.getTime() - entityLSD.getTime();
/*      */         
/*  674 */         if (diff <= 1000L) {
/*  675 */           result = false;
/*      */         }
/*      */       }
/*      */     }
/*  679 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  680 */     return result;
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
/*      */   public static DispositionPhase getCurrentPhaseForEntity(BaseEntity entity)
/*      */   {
/*  694 */     Tracer.traceMethodEntry(new Object[] { entity });
/*  695 */     RMCustomObject currentPhase = null;
/*  696 */     RMProperties entityProps = entity.getProperties();
/*  697 */     String currentPhaseId = entityProps.getGuidValue("CurrentPhaseID");
/*  698 */     if ((currentPhaseId != null) && (currentPhaseId.trim().length() > 0))
/*      */     {
/*  700 */       Repository repository = entity.getRepository();
/*      */       try
/*      */       {
/*  703 */         currentPhase = RMFactory.RMCustomObject.fetchInstance(repository, EntityType.Phase, currentPhaseId, RMPropertyFilter.MinimumPropertySet);
/*      */       }
/*      */       catch (Exception ignored) {}
/*      */     }
/*      */     
/*  708 */     Tracer.traceMethodExit(new Object[] { currentPhase });
/*  709 */     return (DispositionPhase)currentPhase;
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
/*      */   public static DispositionSchedule getInheritedSchedule(BaseEntity entity)
/*      */   {
/*  723 */     Tracer.traceMethodEntry(new Object[] { entity });
/*  724 */     DispositionSchedule schedule = null;
/*      */     
/*  726 */     String cutoffInheritedFrom = entity.getProperties().getGuidValue("CutoffInheritedFrom");
/*  727 */     Repository repository = entity.getRepository();
/*      */     
/*  729 */     BaseEntity scheduleHolder = getAssociatedScheduleHolder(repository, cutoffInheritedFrom);
/*  730 */     if (scheduleHolder != null)
/*      */     {
/*  732 */       schedule = ((DispositionAllocatable)scheduleHolder).getAssignedSchedule();
/*      */     }
/*      */     
/*  735 */     Tracer.traceMethodExit(new Object[] { schedule });
/*  736 */     return schedule;
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
/*      */   public static boolean isRecalculatePhaseIndicated(Repository repository, BaseEntity entity, String cutoffInheritedFrom)
/*      */   {
/*  752 */     Tracer.traceMethodEntry(new Object[] { repository, cutoffInheritedFrom });
/*      */     
/*  754 */     boolean result = false;
/*      */     
/*  756 */     if (entity != null)
/*      */     {
/*  758 */       Integer recalcState = entity.getProperties().getIntegerValue("RecalculatePhaseRetention");
/*  759 */       if ((recalcState != null) && (recalcState.intValue() == 5)) {
/*  760 */         result = true;
/*      */       }
/*      */     }
/*  763 */     if ((!result) && (repository != null) && (cutoffInheritedFrom != null))
/*      */     {
/*  765 */       BaseEntity scheduleHolder = getAssociatedScheduleHolder(repository, cutoffInheritedFrom);
/*  766 */       if (scheduleHolder != null)
/*      */       {
/*  768 */         Integer recalcState = scheduleHolder.getProperties().getIntegerValue("RecalculatePhaseRetention");
/*  769 */         if ((recalcState != null) && (recalcState.intValue() == 5)) {
/*  770 */           result = true;
/*      */         }
/*      */       }
/*      */     }
/*  774 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  775 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void resetEntityDispositionState(RMProperties props)
/*      */   {
/*  787 */     Tracer.traceMethodEntry(new Object[] { props });
/*  788 */     props.putDateTimeValue("CutoffDate", null);
/*  789 */     props.putDateTimeValue("CurrentPhaseExecutionDate", null);
/*  790 */     props.putIntegerValue("CurrentPhaseExecutionStatus", null);
/*  791 */     props.putDateTimeValue("CurrentPhaseDecisionDate", null);
/*  792 */     props.putObjectValue("CurrentPhaseAction", null);
/*  793 */     props.putStringValue("CurrentPhaseReviewDecision", null);
/*  794 */     props.putDateTimeValue("LastSweepDate", null);
/*  795 */     props.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(0));
/*  796 */     props.putGuidValue("CutoffInheritedFrom", null);
/*  797 */     props.putIntegerValue("CurrentActionType", null);
/*  798 */     props.putGuidValue("CurrentPhaseID", null);
/*  799 */     props.putBooleanValue("IsScreeningRequired", Boolean.valueOf(false));
/*  800 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public static void setFinalPhaseDataOnEntity(BaseEntity entity)
/*      */   {
/*  813 */     Tracer.traceMethodEntry(new Object[] { entity });
/*  814 */     RMProperties jarmProps = entity.getProperties();
/*      */     
/*  816 */     jarmProps.putIntegerValue("CurrentPhaseExecutionStatus", null);
/*  817 */     jarmProps.putDateTimeValue("CurrentPhaseExecutionDate", null);
/*  818 */     jarmProps.putDateTimeValue("CurrentPhaseDecisionDate", null);
/*  819 */     jarmProps.putBooleanValue("IsScreeningRequired", Boolean.FALSE);
/*  820 */     jarmProps.putGuidValue("CurrentPhaseID", null);
/*  821 */     jarmProps.putObjectValue("CurrentPhaseAction", null);
/*      */     
/*  823 */     if ((entity instanceof RecordContainer))
/*      */     {
/*  825 */       Integer rawCurrentActionType = jarmProps.getIntegerValue("CurrentActionType");
/*  826 */       if ((rawCurrentActionType != null) && (rawCurrentActionType.intValue() == DispositionActionType.Cutoff.getIntValue()))
/*      */       {
/*      */ 
/*  829 */         FilePlanRepository fpRepos = (FilePlanRepository)entity.getRepository();
/*  830 */         if (DataModelType.PRO2002 != fpRepos.getDataModelType())
/*      */         {
/*  832 */           if (((RecordContainer)entity).isOpen())
/*      */           {
/*  834 */             String closeReason = RMLString.get("disposition.closedByDisposition", "Closed due to disposition processing.");
/*  835 */             ((RecordContainer)entity).close(closeReason);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  841 */     jarmProps.putIntegerValue("CurrentActionType", null);
/*  842 */     jarmProps.putStringValue("CurrentPhaseExportDestination", null);
/*  843 */     jarmProps.putObjectValue("CurrentPhaseExportFormat", null);
/*  844 */     jarmProps.putBooleanValue("IsFinalPhaseCompleted", Boolean.TRUE);
/*      */     
/*  846 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public static Date addRetentionPeriod(Date baseDate, Integer[] retentionPeriod)
/*      */   {
/*  860 */     Tracer.traceMethodEntry(new Object[] { baseDate, retentionPeriod });
/*  861 */     Date result = baseDate;
/*  862 */     if ((baseDate != null) && (retentionPeriod != null) && (retentionPeriod.length >= 3))
/*      */     {
/*  864 */       Calendar cal = Calendar.getInstance();
/*  865 */       cal.setTime(baseDate);
/*  866 */       int years = retentionPeriod[0] == null ? 0 : retentionPeriod[0].intValue();
/*  867 */       int months = retentionPeriod[1] == null ? 0 : retentionPeriod[1].intValue();
/*  868 */       int days = retentionPeriod[2] == null ? 0 : retentionPeriod[2].intValue();
/*      */       
/*  870 */       cal.add(1, years);
/*  871 */       cal.add(2, months);
/*  872 */       cal.add(5, days);
/*  873 */       result = cal.getTime();
/*      */     }
/*      */     
/*  876 */     Tracer.traceMethodExit(new Object[] { result });
/*  877 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void validateAttemptedAction(BaseEntity entity, DispositionActionType attemptedActionType)
/*      */   {
/*  889 */     RMProperties entityProps = entity.getProperties();
/*      */     
/*  891 */     Integer rawCurrentPhaseExecStatus = entityProps.getIntegerValue("CurrentPhaseExecutionStatus");
/*  892 */     if ((rawCurrentPhaseExecStatus == null) || (rawCurrentPhaseExecStatus.intValue() != RMWorkflowStatus.Started.getIntValue()))
/*      */     {
/*      */ 
/*  895 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_ERROR_WORKFLOW_NOT_STARTED, new Object[] { entity.getObjectIdentity() });
/*      */     }
/*      */     
/*  898 */     FilePlanRepository fpRepos = (FilePlanRepository)entity.getRepository();
/*  899 */     String currentPhaseId = entityProps.getGuidValue("CurrentPhaseID");
/*  900 */     Integer rawCurrentActionType = entityProps.getIntegerValue("CurrentActionType");
/*  901 */     DispositionActionType entityCurrentActionType = rawCurrentActionType != null ? DispositionActionType.getInstanceFromInt(rawCurrentActionType.intValue()) : null;
/*      */     
/*      */ 
/*  904 */     if (currentPhaseId != null)
/*      */     {
/*      */ 
/*  907 */       DispositionPhase currentPhase = (DispositionPhase)RMFactory.RMCustomObject.fetchInstance(fpRepos, EntityType.Phase, currentPhaseId, RMPropertyFilter.MinimumPropertySet);
/*  908 */       DispositionAction currentPhaseAction = currentPhase.getPhaseAction();
/*  909 */       if ((currentPhaseAction != null) && (currentPhaseAction.getActionType() != entityCurrentActionType)) {
/*  910 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_ERROR_PHASE_HAS_CHANGED, new Object[] { entity.getObjectIdentity(), currentPhase.getPhaseNumber() });
/*      */       }
/*      */     }
/*      */     
/*  914 */     Date now = new Date();
/*  915 */     Date currentPhaseExecDate = entityProps.getDateTimeValue("CurrentPhaseExecutionDate");
/*  916 */     if ((currentPhaseExecDate == null) || (currentPhaseExecDate.after(now)))
/*      */     {
/*  918 */       String formattedDate = RMRuntimeException.formatDate(currentPhaseExecDate);
/*  919 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_ERROR_BAD_CURRENT_PHASE_EXEC_DATE, new Object[] { entity.getObjectIdentity(), formattedDate });
/*      */     }
/*      */     
/*      */ 
/*  923 */     if (entityCurrentActionType != attemptedActionType)
/*      */     {
/*      */ 
/*  926 */       if ((entityCurrentActionType != DispositionActionType.Transfer) || ((attemptedActionType != DispositionActionType.Export) && (attemptedActionType != DispositionActionType.Destroy)))
/*      */       {
/*      */ 
/*      */ 
/*  930 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_ERROR_INVALID_ACTION_TYPE, new Object[] { entity.getObjectIdentity(), entityCurrentActionType, attemptedActionType });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  935 */     boolean checkParentContainerHierarchy = true;
/*  936 */     if ((((Holdable)entity).isOnHold(checkParentContainerHierarchy)) || (((entity instanceof RecordContainer)) && (((RecordContainer)entity).isAnyChildOnHold())))
/*      */     {
/*      */ 
/*  939 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_ERROR_DUE_TO_HOLD, new Object[] { entity.getObjectIdentity() });
/*      */     }
/*      */     
/*      */ 
/*  943 */     if (((attemptedActionType == DispositionActionType.InterimTransfer) || (attemptedActionType == DispositionActionType.Export)) && ((entity instanceof RecordContainer)) && (((RecordContainer)entity).isReopened()))
/*      */     {
/*      */ 
/*      */ 
/*  947 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_ERROR_DUE_TO_REOPENED_CONTAINER, new Object[] { entity.getObjectIdentity(), attemptedActionType });
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
/*      */ 
/*      */ 
/*      */   public static BaseEntity getAssociatedScheduleHolder(Repository repository, String cutoffInheritedFrom)
/*      */   {
/*  965 */     Tracer.traceMethodEntry(new Object[] { repository, cutoffInheritedFrom });
/*  966 */     BaseEntity scheduleHolder = null;
/*  967 */     if ((cutoffInheritedFrom != null) && (cutoffInheritedFrom.trim().length() > 0))
/*      */     {
/*  969 */       FilePlanRepository fpRepos = (FilePlanRepository)repository;
/*  970 */       RMPropertyFilter pf = new RMPropertyFilter();
/*  971 */       pf.addIncludeProperty(Integer.valueOf(0), null, null, "RecalculatePhaseRetention", null);
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  976 */         scheduleHolder = RMFactory.Container.fetchInstance(fpRepos, EntityType.Container, cutoffInheritedFrom, pf);
/*      */ 
/*      */       }
/*      */       catch (Exception ignored)
/*      */       {
/*  981 */         scheduleHolder = RMFactory.RMCustomObject.fetchInstance(fpRepos, cutoffInheritedFrom, pf);
/*      */       }
/*      */     }
/*      */     
/*  985 */     Tracer.traceMethodExit(new Object[] { scheduleHolder });
/*  986 */     return scheduleHolder;
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
/*      */   public static void completePhaseExecution(BaseEntity entity, Date proposedPhaseExecutionDate, boolean transitionToNextPhase)
/*      */   {
/* 1000 */     Tracer.traceMethodEntry(new Object[] { entity, proposedPhaseExecutionDate, Boolean.valueOf(transitionToNextPhase) });
/*      */     
/* 1002 */     String entityIdent = entity.getObjectIdentity();
/* 1003 */     DispositionSchedule dispSched = getInheritedSchedule(entity);
/* 1004 */     RMProperties entityProps = entity.getProperties();
/*      */     
/* 1006 */     Date cutoffDate = entityProps.getDateTimeValue("CutoffDate");
/* 1007 */     if (cutoffDate == null)
/*      */     {
/* 1009 */       if (!transitionToNextPhase)
/*      */       {
/* 1011 */         entityProps.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(3));
/* 1012 */         ((Persistable)entity).save(RMRefreshMode.Refresh);
/* 1013 */         recalculateCutoffDate(dispSched, entity);
/* 1014 */         entityProps = entity.getProperties();
/*      */       }
/*      */     }
/*      */     
/* 1018 */     Date now = new Date();
/* 1019 */     Date curPhExeDate = entityProps.getDateTimeValue("CurrentPhaseExecutionDate");
/* 1020 */     Integer curPhExeStatus = entityProps.getIntegerValue("CurrentPhaseExecutionStatus");
/*      */     
/* 1022 */     if ((curPhExeDate == null) || (curPhExeDate.after(now)))
/* 1023 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_COMPLETE_DISPPHASE_CPED_NOT_READY, new Object[] { entityIdent });
/* 1024 */     if ((curPhExeStatus == null) || (curPhExeStatus.intValue() != RMWorkflowStatus.Started.getIntValue())) {
/* 1025 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_COMPLETE_DISPPHASE_CPES_NOT_READY, new Object[] { entityIdent });
/*      */     }
/*      */     
/* 1028 */     if (transitionToNextPhase)
/*      */     {
/*      */ 
/* 1031 */       if ((proposedPhaseExecutionDate == null) || (proposedPhaseExecutionDate.after(now)))
/*      */       {
/* 1033 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_COMPLETE_DISPPHASE_INVALID_PROPOSED_CPED, new Object[] { entityIdent, proposedPhaseExecutionDate });
/*      */       }
/*      */       
/*      */ 
/* 1037 */       if ((proposedPhaseExecutionDate.before(curPhExeDate)) || (proposedPhaseExecutionDate.equals(curPhExeDate)) || (proposedPhaseExecutionDate.before(now)) || (proposedPhaseExecutionDate.equals(now)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1042 */         setCurrentPhaseDate(entity, proposedPhaseExecutionDate, RMWorkflowStatus.Completed);
/* 1043 */         if (dispSched != null)
/*      */         {
/* 1045 */           ((RALBaseEntity)entity).triggerNextPhase(dispSched);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1051 */       setCurrentPhaseDate(entity, proposedPhaseExecutionDate, RMWorkflowStatus.NotStarted);
/*      */     }
/*      */     
/*      */ 
/* 1055 */     ((Persistable)entity).save(RMRefreshMode.Refresh);
/* 1056 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String extractWhereClauseFromConditionXML(String conditionXML)
/*      */     throws Exception
/*      */   {
/* 1080 */     Tracer.traceMethodEntry(new Object[] { conditionXML });
/* 1081 */     String whereClause = "";
/* 1082 */     if (conditionXML != null)
/*      */     {
/* 1084 */       InputSource is = new InputSource(new StringReader(conditionXML));
/* 1085 */       DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 1086 */       Document domDoc = docBuilder.parse(is);
/* 1087 */       NodeList nodes = domDoc.getElementsByTagName("sql");
/* 1088 */       Node sqlElementNode = nodes.item(0);
/* 1089 */       String sqlStr = sqlElementNode.getFirstChild().getNodeValue();
/*      */       
/*      */ 
/* 1092 */       int posWHERE = sqlStr.toUpperCase().indexOf("WHERE");
/* 1093 */       if (posWHERE != -1) {
/* 1094 */         whereClause = sqlStr.substring(posWHERE + 5);
/*      */       } else {
/* 1096 */         whereClause = sqlStr;
/*      */       }
/*      */     }
/* 1099 */     Tracer.traceMethodExit(new Object[] { whereClause });
/* 1100 */     return whereClause;
/*      */   }
/*      */   
/*      */   private static void setCurrentPhaseDate(BaseEntity entity, Date proposedPhaseExecutionDate, RMWorkflowStatus proposedWorflowStatus)
/*      */   {
/* 1105 */     Tracer.traceMethodEntry(new Object[] { entity, proposedPhaseExecutionDate, proposedWorflowStatus });
/*      */     
/* 1107 */     RMProperties entityProps = entity.getProperties();
/* 1108 */     entityProps.putIntegerValue("CurrentPhaseExecutionStatus", Integer.valueOf(proposedWorflowStatus.getIntValue()));
/* 1109 */     if ((proposedWorflowStatus == RMWorkflowStatus.NotStarted) && (proposedPhaseExecutionDate != null))
/*      */     {
/* 1111 */       entityProps.putDateTimeValue("CurrentPhaseExecutionDate", proposedPhaseExecutionDate);
/*      */     }
/* 1113 */     else if (proposedWorflowStatus == RMWorkflowStatus.Completed)
/*      */     {
/* 1115 */       entityProps.putDateTimeValue("CurrentPhaseExecutionDate", (Date)null);
/*      */       
/* 1117 */       Integer curActionType = entityProps.getIntegerValue("CurrentActionType");
/* 1118 */       if ((curActionType != null) && (curActionType.intValue() == DispositionActionType.Cutoff.getIntValue()))
/*      */       {
/* 1120 */         if ((entity instanceof RecordContainer))
/*      */         {
/* 1122 */           FilePlanRepository fpRepos = (FilePlanRepository)entity.getRepository();
/* 1123 */           if (DataModelType.PRO2002 != fpRepos.getDataModelType())
/*      */           {
/* 1125 */             if (((RecordContainer)entity).isOpen())
/*      */             {
/* 1127 */               String closeReason = RMLString.get("disposition.closedByDisposition", "Closed due to disposition processing.");
/* 1128 */               ((RecordContainer)entity).close(closeReason);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1133 */         entityProps.putDateTimeValue("CutoffDate", proposedPhaseExecutionDate);
/*      */       }
/*      */       
/* 1136 */       entityProps.putDateTimeValue("CurrentPhaseDecisionDate", new Date());
/*      */     }
/*      */     
/* 1139 */     ((Persistable)entity).save(RMRefreshMode.Refresh);
/* 1140 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   private static void recalculateCutoffDate(DispositionSchedule dispSched, BaseEntity entity)
/*      */   {
/* 1145 */     Tracer.traceMethodEntry(new Object[] { entity });
/* 1146 */     if (dispSched != null)
/*      */     {
/* 1148 */       Date dispSchedDLM = dispSched.getProperties().getDateTimeValue("DateLastModified");
/* 1149 */       RMProperties entityProps = entity.getProperties();
/* 1150 */       Date entityLSD = entityProps.getDateTimeValue("LastSweepDate");
/* 1151 */       Integer recalcPhRetent = entityProps.getIntegerValue("RecalculatePhaseRetention");
/*      */       
/* 1153 */       if ((recalcPhRetent == null) || (3 != recalcPhRetent.intValue()) || (!dispSchedDLM.equals(entityLSD)))
/*      */       {
/*      */ 
/* 1156 */         dispSched.recalculateCutoffDate((Dispositionable)entity);
/*      */       }
/*      */     }
/* 1159 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   private static Object validateNonEmptyProp(RMProperties props, String propName)
/*      */   {
/* 1173 */     Tracer.traceMethodEntry(new Object[] { props, propName });
/* 1174 */     Object value = null;
/* 1175 */     if (props.isPropertyPresent(propName))
/*      */     {
/* 1177 */       value = props.getObjectValue(propName);
/* 1178 */       if ((value != null) && ((value instanceof String)))
/*      */       {
/* 1180 */         if (((String)value).trim().length() == 0) {
/* 1181 */           value = null;
/*      */         }
/*      */       }
/*      */     }
/* 1185 */     if (value == null)
/*      */     {
/* 1187 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { propName });
/*      */     }
/*      */     
/* 1190 */     Tracer.traceMethodExit(new Object[] { value });
/* 1191 */     return value;
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
/*      */   private static Integer[] extractTimePeriod(RMProperties props, String[] propNames)
/*      */   {
/* 1207 */     Tracer.traceMethodEntry(new Object[] { props, propNames });
/* 1208 */     Integer[] results = new Integer[propNames.length];
/* 1209 */     RMProperty prop = null;
/* 1210 */     for (int i = 0; i < propNames.length; i++)
/*      */     {
/* 1212 */       if (props.isPropertyPresent(propNames[i]))
/*      */       {
/* 1214 */         prop = props.get(propNames[i]);
/* 1215 */         if (prop.getDataType() == DataType.Integer) {
/* 1216 */           results[i] = prop.getIntegerValue();
/*      */         }
/*      */       }
/*      */     }
/* 1220 */     Tracer.traceMethodExit(new Object[] { results });
/* 1221 */     return results;
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALDispositionLogic.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */