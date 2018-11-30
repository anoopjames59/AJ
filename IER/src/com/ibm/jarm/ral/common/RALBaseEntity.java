/*     */ package com.ibm.jarm.ral.common;
/*     */ 
/*     */ import com.ibm.jarm.api.constants.DispositionActionType;
/*     */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.BulkItemResult;
/*     */ import com.ibm.jarm.api.core.BulkOperation;
/*     */ import com.ibm.jarm.api.core.BulkOperation.EntityDescription;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.DispositionTrigger;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Hold;
/*     */ import com.ibm.jarm.api.core.Persistable;
/*     */ import com.ibm.jarm.api.core.RMCustomObject;
/*     */ import com.ibm.jarm.api.core.RMLink;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public abstract class RALBaseEntity
/*     */   implements BaseEntity
/*     */ {
/*  49 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*     */   
/*     */   public static final String MimeTypeRecordsManagerRoot = "application/x-filenet-rm-classificationschemeroot";
/*     */   
/*     */   public static final String MimeTypeFilePlan = "application/x-filenet-rm-classificationscheme";
/*     */   
/*     */   public static final String MimeTypeElectronicVolume = "application/x-filenet-rm-volumeelectronic";
/*     */   
/*     */   public static final String MimeTypeHybridVolume = "application/x-filenet-rm-volumehybrid";
/*     */   
/*     */   public static final String MimeTypePhysicalVolume = "application/x-filenet-rm-volumephysical";
/*     */   
/*     */   public static final String MimeTypeElectronicRecord = "application/x-filenet-rm-electronicrecord";
/*     */   
/*     */   public static final String MimeTypeEmailRecord = "application/x-filenet-rm-emailrecord";
/*     */   
/*     */   public static final String MimeTypePhysicalRecord = "application/x-filenet-rm-physicalrecord";
/*     */   public static final String MimeTypeReportDefinition = "application/x-filenet-reportdefinition";
/*  67 */   protected static final Set<String> VitalPropertySymNames = new HashSet();
/*  68 */   static { VitalPropertySymNames.add("VitalRecordNextReviewDate");
/*  69 */     VitalPropertySymNames.add("VitalRecordReviewDate");
/*  70 */     VitalPropertySymNames.add("VitalRecordDeclareDate");
/*  71 */     VitalPropertySymNames.add("VitalRecordReviewComments");
/*  72 */     VitalPropertySymNames.add("CurrentPhaseAction");
/*  73 */     VitalPropertySymNames.add("VitalWorkflowStatus");
/*  74 */     VitalPropertySymNames.add("VitalSweepDate");
/*     */     
/*  76 */     DispositionPropertySymNames = new HashSet();
/*  77 */     DispositionPropertySymNames.add("CutoffDate");
/*  78 */     DispositionPropertySymNames.add("CurrentPhaseExecutionDate");
/*  79 */     DispositionPropertySymNames.add("CurrentPhaseExecutionStatus");
/*  80 */     DispositionPropertySymNames.add("CurrentPhaseDecisionDate");
/*  81 */     DispositionPropertySymNames.add("CurrentPhaseAction");
/*  82 */     DispositionPropertySymNames.add("CurrentPhaseReviewDecision");
/*  83 */     DispositionPropertySymNames.add("LastSweepDate");
/*  84 */     DispositionPropertySymNames.add("CutoffInheritedFrom");
/*  85 */     DispositionPropertySymNames.add("CurrentActionType");
/*  86 */     DispositionPropertySymNames.add("CurrentPhaseID");
/*  87 */     DispositionPropertySymNames.add("IsScreeningRequired");
/*  88 */     DispositionPropertySymNames.add("CurrentPhaseReviewComments");
/*  89 */     DispositionPropertySymNames.add("IsFinalPhaseCompleted");
/*     */     
/*  91 */     StringBuilder sb = new StringBuilder();
/*  92 */     sb.append("IsVitalRecord");
/*  93 */     sb.append(' ').append("VitalRecordDeclareDate");
/*  94 */     sb.append(' ').append("VitalRecordDisposalTrigger");
/*  95 */     sb.append(' ').append("VitalRecordNextReviewDate");
/*  96 */     sb.append(' ').append("VitalRecordReviewAction");
/*  97 */     sb.append(' ').append("VitalRecordReviewDate");
/*  98 */     sb.append(' ').append("VitalSweepDate");
/*  99 */     sb.append(' ').append("VitalWorkflowStatus");
/* 100 */     VitalPropsPF = new RMPropertyFilter();
/* 101 */     VitalPropsPF.addIncludeProperty(Integer.valueOf(0), null, null, sb.toString(), null);
/* 102 */     String level2Props = "EventType ActionType";
/* 103 */     VitalPropsPF.addIncludeProperty(Integer.valueOf(1), null, null, level2Props, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final Set<String> DispositionPropertySymNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final RMPropertyFilter VitalPropsPF;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EntityType entityType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Repository repository;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String identity;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isPlaceholder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isCreationPending;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RALBaseEntity(EntityType entityType, Repository repository, String identity, boolean isPlaceholder)
/*     */   {
/* 149 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, Boolean.valueOf(isPlaceholder) });
/* 150 */     this.entityType = entityType;
/* 151 */     this.repository = repository;
/* 152 */     this.identity = identity;
/* 153 */     this.isPlaceholder = isPlaceholder;
/* 154 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityType getEntityType()
/*     */   {
/* 162 */     return this.entityType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Repository getRepository()
/*     */   {
/* 170 */     return this.repository;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClientIdentifier()
/*     */   {
/* 178 */     return this.identity;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMClassDescription getClassDescription()
/*     */   {
/* 196 */     Tracer.traceMethodEntry(new Object[0]);
/* 197 */     String className = getClassName();
/* 198 */     boolean allowFromCache = true;
/* 199 */     RMClassDescription result = ((RALBaseRepository)this.repository).getClassDescription(className, allowFromCache);
/*     */     
/* 201 */     Tracer.traceMethodExit(new Object[] { result });
/* 202 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isPlaceholder()
/*     */   {
/* 207 */     return this.isPlaceholder;
/*     */   }
/*     */   
/*     */   public boolean isCreationPending()
/*     */   {
/* 212 */     return this.isCreationPending;
/*     */   }
/*     */   
/*     */   public void setCreationPending(boolean value)
/*     */   {
/* 217 */     this.isCreationPending = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetDispositionData()
/*     */   {
/* 225 */     Tracer.traceMethodEntry(new Object[0]);
/* 226 */     resetDispositionData(getProperties());
/* 227 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   public void resetDispositionData(RMProperties props)
/*     */   {
/* 232 */     Tracer.traceMethodEntry(new Object[] { props });
/* 233 */     props.putDateTimeValue("ExternalEventOccurrenceDate", null);
/* 234 */     props.putDateTimeValue("CutoffDate", null);
/* 235 */     props.putDateTimeValue("CurrentPhaseExecutionDate", null);
/* 236 */     props.putIntegerValue("CurrentPhaseExecutionStatus", null);
/* 237 */     props.putDateTimeValue("CurrentPhaseDecisionDate", null);
/* 238 */     props.putObjectValue("CurrentPhaseAction", null);
/* 239 */     props.putStringValue("CurrentPhaseReviewDecision", null);
/* 240 */     props.putStringValue("CurrentPhaseReviewComments", null);
/* 241 */     props.putDateTimeValue("LastSweepDate", null);
/* 242 */     props.putGuidValue("CutoffInheritedFrom", null);
/* 243 */     props.putIntegerValue("CurrentActionType", null);
/* 244 */     props.putGuidValue("CurrentPhaseID", null);
/* 245 */     props.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(0));
/* 246 */     props.putBooleanValue("IsScreeningRequired", Boolean.valueOf(false));
/* 247 */     props.putBooleanValue("IsFinalPhaseCompleted", Boolean.valueOf(false));
/*     */     
/* 249 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetVitalData()
/*     */   {
/* 257 */     Tracer.traceMethodEntry(new Object[0]);
/* 258 */     RMProperties props = getProperties();
/* 259 */     props.putDateTimeValue("VitalRecordNextReviewDate", null);
/* 260 */     props.putDateTimeValue("VitalRecordReviewDate", null);
/* 261 */     props.putDateTimeValue("VitalRecordDeclareDate", null);
/* 262 */     props.putStringValue("VitalRecordReviewComments", null);
/* 263 */     props.putIntegerValue("VitalWorkflowStatus", null);
/* 264 */     props.putDateTimeValue("VitalSweepDate", null);
/*     */     
/* 266 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setVital(RMProperties vitalProperties)
/*     */   {
/* 277 */     Tracer.traceMethodEntry(new Object[] { vitalProperties });
/* 278 */     if ((vitalProperties == null) || (vitalProperties.size() == 0))
/*     */     {
/* 280 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.INVALID_VITAL_RECORD_PROPERTIES, new Object[0]);
/*     */     }
/*     */     
/* 283 */     validateVitalProperties(vitalProperties, true);
/* 284 */     getProperties().add(vitalProperties);
/* 285 */     ((Persistable)this).save(RMRefreshMode.Refresh);
/* 286 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadyForInitiateDisposition()
/*     */   {
/* 294 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 296 */     boolean result = false;
/* 297 */     Integer curPhExeStatus = getProperties().getIntegerValue("CurrentPhaseExecutionStatus");
/* 298 */     if ((curPhExeStatus != null) && (curPhExeStatus.intValue() == RMWorkflowStatus.NotStarted.getIntValue()))
/*     */     {
/* 300 */       Date curPhExeDate = getProperties().getDateTimeValue("CurrentPhaseExecutionDate");
/* 301 */       if (curPhExeDate != null)
/*     */       {
/* 303 */         Date currentDate = new Date();
/* 304 */         result = currentDate.after(curPhExeDate);
/*     */       }
/*     */     }
/*     */     
/* 308 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 309 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isVital()
/*     */   {
/* 319 */     Tracer.traceMethodEntry(new Object[0]);
/* 320 */     boolean isVital = false;
/* 321 */     RMProperties props = getProperties();
/* 322 */     if (!props.isPropertyPresent("IsVitalRecord")) {
/* 323 */       refresh(new String[] { "IsVitalRecord" });
/*     */     }
/* 325 */     Boolean rawBool = props.getBooleanValue("IsVitalRecord");
/* 326 */     if (rawBool != null) {
/* 327 */       isVital = rawBool.booleanValue();
/*     */     }
/* 329 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(isVital) });
/* 330 */     return isVital;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void executeVital()
/*     */   {
/* 339 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/*     */ 
/* 342 */     List<String> vitalListRMProperties = getVitalPropertyNames();
/* 343 */     refresh((String[])vitalListRMProperties.toArray(new String[vitalListRMProperties.size()]));
/* 344 */     RMProperties props = getProperties();
/*     */     
/* 346 */     if (isVital())
/*     */     {
/* 348 */       DispositionTrigger vitalTrigger = (DispositionTrigger)props.getObjectValue("VitalRecordDisposalTrigger");
/* 349 */       if (vitalTrigger != null)
/*     */       {
/*     */ 
/* 352 */         if (vitalTrigger.getDateTime() == null)
/*     */         {
/* 354 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.INVALID_NULL_TRIGGER_PROPERTY, new Object[] { "DateTime" });
/*     */         }
/*     */         
/*     */ 
/* 358 */         Integer[] rawCyclePeriod = vitalTrigger.getRecurringCyclePeriod();
/* 359 */         int cycleYears = (rawCyclePeriod[0] != null) && (rawCyclePeriod[0].intValue() >= 0) ? rawCyclePeriod[0].intValue() : 0;
/* 360 */         int cycleMonths = (rawCyclePeriod[1] != null) && (rawCyclePeriod[1].intValue() >= 0) ? rawCyclePeriod[1].intValue() : 0;
/* 361 */         int cycleDays = (rawCyclePeriod[2] != null) && (rawCyclePeriod[2].intValue() >= 0) ? rawCyclePeriod[2].intValue() : 0;
/* 362 */         if ((cycleYears == 0) && (cycleMonths == 0) && (cycleDays == 0))
/*     */         {
/* 364 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.INVALID_CYCLIC_VALUES, new Object[] { vitalTrigger.getName(), Integer.valueOf(cycleYears), Integer.valueOf(cycleMonths), Integer.valueOf(cycleDays) });
/*     */         }
/*     */         
/*     */ 
/* 368 */         Date triggerDateTime = vitalTrigger.getDateTime();
/* 369 */         Date triggerDLM = vitalTrigger.getProperties().getDateTimeValue("DateLastModified");
/* 370 */         Date vitalSweepDate = props.getDateTimeValue("VitalSweepDate");
/* 371 */         RMWorkflowStatus vitalWflStatus = RMWorkflowStatus.getInstanceFromInt(props.getIntegerValue("VitalWorkflowStatus").intValue());
/* 372 */         if ((!triggerDLM.equals(vitalSweepDate)) || (vitalWflStatus == RMWorkflowStatus.Completed))
/*     */         {
/*     */ 
/* 375 */           Date newVitalReviewDate = props.getDateTimeValue("VitalRecordReviewDate");
/* 376 */           Date vitalNextReviewDate = props.getDateTimeValue("VitalRecordNextReviewDate");
/* 377 */           if (vitalNextReviewDate == null)
/*     */           {
/* 379 */             Date vitalDeclareDateTime = props.getDateTimeValue("VitalRecordDeclareDate");
/* 380 */             Calendar vitalDeclareDate_ZT = setTimeToZero(vitalDeclareDateTime);
/*     */             
/* 382 */             Calendar triggerDate_ZT = setTimeToZero(vitalTrigger.getDateTime());
/*     */             
/* 384 */             if ((vitalDeclareDate_ZT != null) && (vitalDeclareDate_ZT.after(triggerDate_ZT)))
/*     */             {
/* 386 */               newVitalReviewDate = calculateLaunchDate(vitalDeclareDate_ZT.getTime(), triggerDateTime, cycleDays, cycleMonths, cycleYears, 1);
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/* 392 */               newVitalReviewDate = triggerDateTime;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 397 */             newVitalReviewDate = vitalNextReviewDate;
/*     */           }
/*     */           
/*     */ 
/* 401 */           Date newNextReviewDate = calculateNextLaunchDate(newVitalReviewDate, triggerDateTime, cycleDays, cycleMonths, cycleYears, 1);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 406 */           props.putDateTimeValue("VitalSweepDate", triggerDLM);
/* 407 */           props.putDateTimeValue("VitalRecordReviewDate", newVitalReviewDate);
/* 408 */           props.putDateTimeValue("VitalRecordNextReviewDate", newNextReviewDate);
/* 409 */           props.putIntegerValue("VitalWorkflowStatus", Integer.valueOf(RMWorkflowStatus.NotStarted.getIntValue()));
/* 410 */           ((Persistable)this).save(RMRefreshMode.Refresh);
/*     */         }
/*     */       }
/*     */     }
/* 414 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   public static Calendar setTimeToZero(Date date)
/*     */   {
/* 420 */     Tracer.traceMethodEntry(new Object[] { date });
/* 421 */     Calendar calendarDate = null;
/* 422 */     if (date != null)
/*     */     {
/*     */ 
/* 425 */       calendarDate = Calendar.getInstance();
/* 426 */       calendarDate.setTime(date);
/*     */       
/*     */ 
/* 429 */       calendarDate.set(11, 0);
/* 430 */       calendarDate.set(12, 0);
/* 431 */       calendarDate.set(13, 0);
/* 432 */       calendarDate.set(14, 0);
/*     */     }
/*     */     
/* 435 */     Tracer.traceMethodExit(new Object[] { calendarDate });
/* 436 */     return calendarDate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Date calculateLaunchDate(Date aoBaseDate, Date aoFrequencyDate, int aiCycleDays, int aiCycleMonths, int aiCycleYears, int aiIncrementBy)
/*     */   {
/* 455 */     Tracer.traceMethodEntry(new Object[] { aoBaseDate, aoFrequencyDate, Integer.valueOf(aiCycleDays), Integer.valueOf(aiCycleMonths), Integer.valueOf(aiCycleYears), Integer.valueOf(aiIncrementBy) });
/* 456 */     Date loNextReviewDate = null;
/* 457 */     Date loBaseCompareDate = aoBaseDate;
/*     */     
/* 459 */     Date loFrequecyDate = new Date(aoFrequencyDate.getTime());
/* 460 */     if (aiIncrementBy < 1)
/*     */     {
/* 462 */       aiIncrementBy = 1;
/*     */     }
/* 464 */     while (!loFrequecyDate.after(loBaseCompareDate))
/*     */     {
/* 466 */       loFrequecyDate = addToDate(aoFrequencyDate, aiIncrementBy * aiCycleYears, aiIncrementBy * aiCycleMonths, aiIncrementBy * aiCycleDays);
/*     */       
/*     */ 
/* 469 */       aiIncrementBy++;
/*     */     }
/*     */     
/* 472 */     loNextReviewDate = loFrequecyDate;
/*     */     
/* 474 */     Tracer.traceMethodExit(new Object[] { loNextReviewDate });
/* 475 */     return loNextReviewDate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private Date calculateNextLaunchDate(Date baseDate, Date frequencyDate, int triggerCycleDays, int triggerCycleMonths, int triggerCycleYears, int incrementBy)
/*     */   {
/* 493 */     Tracer.traceMethodEntry(new Object[] { baseDate, frequencyDate, Integer.valueOf(triggerCycleDays), Integer.valueOf(triggerCycleMonths), Integer.valueOf(triggerCycleYears), Integer.valueOf(incrementBy) });
/* 494 */     Date nextReviewDate = null;
/* 495 */     Date baseCompareDate = baseDate;
/* 496 */     Date newFrequencyDate = null;
/*     */     
/* 498 */     if (incrementBy < 1)
/*     */     {
/* 500 */       incrementBy = 1;
/*     */     }
/*     */     
/*     */     do
/*     */     {
/* 505 */       newFrequencyDate = addToDate(frequencyDate, incrementBy * triggerCycleYears, incrementBy * triggerCycleMonths, incrementBy * triggerCycleDays);
/*     */       
/*     */ 
/* 508 */       incrementBy++;
/*     */     }
/* 510 */     while (!newFrequencyDate.after(baseCompareDate));
/*     */     
/* 512 */     nextReviewDate = newFrequencyDate;
/*     */     
/* 514 */     Tracer.traceMethodExit(new Object[] { nextReviewDate });
/* 515 */     return nextReviewDate;
/*     */   }
/*     */   
/*     */   protected List<String> getVitalPropertyNames()
/*     */   {
/* 520 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 522 */     List<String> vitalProperties = new ArrayList();
/*     */     
/*     */ 
/* 525 */     vitalProperties.add("IsVitalRecord");
/* 526 */     vitalProperties.add("VitalRecordDeclareDate");
/* 527 */     vitalProperties.add("VitalRecordDisposalTrigger");
/* 528 */     vitalProperties.add("VitalRecordNextReviewDate");
/* 529 */     vitalProperties.add("VitalRecordReviewAction");
/* 530 */     vitalProperties.add("VitalRecordReviewDate");
/* 531 */     vitalProperties.add("VitalSweepDate");
/* 532 */     vitalProperties.add("VitalWorkflowStatus");
/*     */     
/* 534 */     Tracer.traceMethodExit(new Object[] { vitalProperties });
/* 535 */     return vitalProperties;
/*     */   }
/*     */   
/*     */   public static Date addToDate(Date aoBaseDate, int aiYears, int aiMonths, int aiDays)
/*     */   {
/* 540 */     Tracer.traceMethodEntry(new Object[] { aoBaseDate, Integer.valueOf(aiYears), Integer.valueOf(aiMonths), Integer.valueOf(aiDays) });
/* 541 */     Calendar loCalender = Calendar.getInstance();
/* 542 */     loCalender.setTime(aoBaseDate);
/* 543 */     if (aiYears < 0)
/*     */     {
/* 545 */       aiYears = 0;
/*     */     }
/* 547 */     if (aiMonths < 0)
/*     */     {
/* 549 */       aiMonths = 0;
/*     */     }
/* 551 */     if (aiDays < 0)
/*     */     {
/* 553 */       aiDays = 0;
/*     */     }
/* 555 */     loCalender.add(1, aiYears);
/* 556 */     loCalender.add(2, aiMonths);
/* 557 */     loCalender.add(5, aiDays);
/* 558 */     Date loNewDate = loCalender.getTime();
/*     */     
/* 560 */     Tracer.traceMethodExit(new Object[] { loNewDate });
/* 561 */     return loNewDate;
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
/*     */   protected void validateVitalProperties(RMProperties newProps, boolean forExistingEntity)
/*     */   {
/* 574 */     Tracer.traceMethodEntry(new Object[] { newProps, Boolean.valueOf(forExistingEntity) });
/* 575 */     if (EntityType.FilePlan != getEntityType())
/*     */     {
/* 577 */       boolean hasProposedIsVital = newProps.isPropertyPresent("IsVitalRecord");
/* 578 */       Boolean proposedIsVital = null;
/* 579 */       if (hasProposedIsVital) {
/* 580 */         proposedIsVital = newProps.getBooleanValue("IsVitalRecord");
/*     */       }
/* 582 */       RMProperties existingProps = null;
/* 583 */       Boolean existingIsVital = null;
/* 584 */       if (forExistingEntity)
/*     */       {
/*     */ 
/* 587 */         refresh(VitalPropsPF);
/* 588 */         existingProps = getProperties();
/* 589 */         existingIsVital = existingProps.getBooleanValue("IsVitalRecord");
/*     */       }
/*     */       
/* 592 */       if (((hasProposedIsVital) && (Boolean.TRUE.equals(proposedIsVital))) || ((!hasProposedIsVital) && (Boolean.TRUE.equals(existingIsVital))))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 600 */         RMCustomObject vitalDispTrig = null;
/* 601 */         if (newProps.isPropertyPresent("VitalRecordDisposalTrigger"))
/* 602 */           vitalDispTrig = (RMCustomObject)newProps.getObjectValue("VitalRecordDisposalTrigger");
/* 603 */         if ((vitalDispTrig == null) && (forExistingEntity))
/* 604 */           vitalDispTrig = (RMCustomObject)existingProps.getObjectValue("VitalRecordDisposalTrigger");
/* 605 */         if (vitalDispTrig == null)
/*     */         {
/* 607 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.INVALID_VITAL_RECORD_PROPERTY, new Object[] { "VitalRecordDisposalTrigger", null });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 613 */         Integer eventType = vitalDispTrig.getProperties().getIntegerValue("EventType");
/* 614 */         if ((eventType == null) || ((DispositionTriggerType.PredefinedDateTrigger.getIntValue() != eventType.intValue()) && (DispositionTriggerType.RecurringEventTigger.getIntValue() != eventType.intValue())))
/*     */         {
/*     */ 
/*     */ 
/* 618 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.INVALID_VITAL_RECORD_PROPERTY, new Object[] { "EventType", eventType });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 623 */         RMCustomObject vitalReviewAction = null;
/* 624 */         if (newProps.isPropertyPresent("VitalRecordReviewAction"))
/* 625 */           vitalReviewAction = (RMCustomObject)newProps.getObjectValue("VitalRecordReviewAction");
/* 626 */         if ((vitalReviewAction == null) && (forExistingEntity))
/* 627 */           vitalReviewAction = (RMCustomObject)existingProps.getObjectValue("VitalRecordReviewAction");
/* 628 */         if (vitalReviewAction == null)
/*     */         {
/* 630 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.INVALID_VITAL_RECORD_PROPERTY, new Object[] { "VitalRecordReviewAction", null });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 635 */         Integer actionType = vitalReviewAction.getProperties().getIntegerValue("ActionType");
/* 636 */         if ((actionType == null) || (DispositionActionType.VitalReview.getIntValue() != actionType.intValue()))
/*     */         {
/* 638 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.INVALID_VITAL_RECORD_PROPERTY, new Object[] { "ActionType", actionType });
/*     */         }
/*     */         
/*     */ 
/* 642 */         if ((hasProposedIsVital) && (Boolean.TRUE.equals(proposedIsVital)))
/*     */         {
/* 644 */           newProps.putDateTimeValue("VitalRecordDeclareDate", new Date());
/* 645 */           newProps.putIntegerValue("VitalWorkflowStatus", Integer.valueOf(RMWorkflowStatus.NotStarted.getIntValue()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateVitalStatus(Date vitalReviewDate)
/*     */   {
/* 659 */     Tracer.traceMethodEntry(new Object[] { vitalReviewDate });
/*     */     
/* 661 */     List<String> vitalListRMProperties = getVitalPropertyNames();
/* 662 */     refresh((String[])vitalListRMProperties.toArray(new String[vitalListRMProperties.size()]));
/*     */     
/* 664 */     RMProperties props = getProperties();
/* 665 */     Integer rawStatus = props.getIntegerValue("VitalWorkflowStatus");
/* 666 */     int vitalWorkflowStatus = rawStatus != null ? rawStatus.intValue() : 0;
/* 667 */     if ((isVital()) || (vitalWorkflowStatus == RMWorkflowStatus.Started.getIntValue()))
/*     */     {
/* 669 */       if (vitalReviewDate != null)
/*     */       {
/*     */ 
/* 672 */         props.putDateTimeValue("VitalRecordReviewDate", vitalReviewDate);
/* 673 */         Date currNextReviewDate = props.getDateTimeValue("VitalRecordNextReviewDate");
/* 674 */         if (vitalReviewDate.after(currNextReviewDate))
/*     */         {
/* 676 */           props.putDateTimeValue("VitalRecordNextReviewDate", vitalReviewDate);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 681 */       props.putIntegerValue("VitalWorkflowStatus", Integer.valueOf(RMWorkflowStatus.Completed.getIntValue()));
/*     */       
/* 683 */       ((Persistable)this).save(RMRefreshMode.Refresh);
/*     */     }
/* 685 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve()
/*     */   {
/* 696 */     Tracer.traceMethodEntry(new Object[0]);
/* 697 */     if (this.isPlaceholder)
/*     */     {
/* 699 */       refresh((RMPropertyFilter)null);
/* 700 */       this.isPlaceholder = false;
/*     */     }
/* 702 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   public void placeHold(Hold hold)
/*     */   {
/* 707 */     Tracer.traceMethodEntry(new Object[] { hold });
/* 708 */     if (hold == null)
/*     */     {
/* 710 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_EMPTY_INPUT_PARAM, new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 714 */     FilePlanRepository repository = (FilePlanRepository)getRepository();
/* 715 */     List<BulkOperation.EntityDescription> entityDescs = new ArrayList(1);
/* 716 */     entityDescs.add(new BulkOperation.EntityDescription(getEntityType(), getObjectIdentity()));
/* 717 */     List<String> holdIdents = new ArrayList(1);
/* 718 */     holdIdents.add(hold.getObjectIdentity());
/* 719 */     List<BulkItemResult> results = BulkOperation.placeHolds(repository, entityDescs, holdIdents);
/* 720 */     if ((results != null) && (results.size() >= 1))
/*     */     {
/* 722 */       BulkItemResult bir = (BulkItemResult)results.get(0);
/* 723 */       if ((!bir.wasSuccessful()) && (bir.getException() != null))
/* 724 */         throw bir.getException();
/*     */     }
/* 726 */     refresh();
/*     */     
/* 728 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   public void removeHold(Hold hold)
/*     */   {
/* 733 */     Tracer.traceMethodEntry(new Object[] { hold });
/* 734 */     if (hold == null)
/*     */     {
/* 736 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_EMPTY_INPUT_PARAM, new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 740 */     FilePlanRepository repository = (FilePlanRepository)getRepository();
/* 741 */     BulkOperation.EntityDescription entityDesc = new BulkOperation.EntityDescription(getEntityType(), getObjectIdentity());
/* 742 */     List<String> holdIdents = new ArrayList(1);
/* 743 */     holdIdents.add(hold.getObjectIdentity());
/* 744 */     List<BulkItemResult> results = BulkOperation.removeHolds(repository, entityDesc, holdIdents);
/* 745 */     if ((results != null) && (results.size() >= 1))
/*     */     {
/* 747 */       BulkItemResult bir = (BulkItemResult)results.get(0);
/* 748 */       if ((!bir.wasSuccessful()) && (bir.getException() != null))
/* 749 */         throw bir.getException();
/*     */     }
/* 751 */     refresh();
/*     */     
/* 753 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   public List<Hold> getAssociatedHolds()
/*     */   {
/* 758 */     Tracer.traceMethodEntry(new Object[0]);
/* 759 */     List<Hold> result = new ArrayList();
/* 760 */     if (!getProperties().isPropertyPresent("Holds"))
/*     */     {
/* 762 */       refresh(new String[] { "Holds" });
/*     */     }
/* 764 */     if (getProperties().isPropertyPresent("Holds"))
/*     */     {
/* 766 */       RMProperties props = getProperties();
/* 767 */       List<Object> objList = props.getObjectListValue("Holds");
/* 768 */       for (Object curObj : objList)
/*     */       {
/* 770 */         if ((curObj instanceof RMLink))
/*     */         {
/* 772 */           RMLink linkObj = (RMLink)curObj;
/* 773 */           Hold theHold = (Hold)linkObj.getTail();
/* 774 */           result.add(theHold);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 779 */     Tracer.traceMethodExit(new Object[] { result });
/* 780 */     return result;
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
/*     */ 
/*     */ 
/*     */   public static RMProperty getPropIfModified(RMProperties props, String symbolicName)
/*     */   {
/* 797 */     Tracer.traceMethodEntry(new Object[] { props, symbolicName });
/* 798 */     RMProperty result = null;
/*     */     
/* 800 */     if (props.isPropertyPresent(symbolicName))
/*     */     {
/* 802 */       RMProperty prop = props.get(symbolicName);
/* 803 */       if ((prop != null) && (prop.isDirty())) {
/* 804 */         result = prop;
/*     */       }
/*     */     }
/* 807 */     Tracer.traceMethodExit(new Object[] { result });
/* 808 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<RecordContainer> getParentsOnHold()
/*     */   {
/* 814 */     Tracer.traceMethodEntry(new Object[0]);
/* 815 */     List<RecordContainer> result = new ArrayList();
/*     */     
/* 817 */     List<Container> containedBy = getContainedBy();
/* 818 */     for (Container curParent : containedBy)
/*     */     {
/* 820 */       if (curParent.getEntityType() == EntityType.FilePlan) {
/*     */         break;
/*     */       }
/*     */       
/* 824 */       if (((RecordContainer)curParent).isOnHold(false))
/*     */       {
/* 826 */         result.add((RecordContainer)curParent);
/*     */       }
/* 828 */       List<RecordContainer> parentsOnHold = ((RecordContainer)curParent).getParentsOnHold();
/* 829 */       result.addAll(parentsOnHold);
/*     */     }
/*     */     
/* 832 */     Tracer.traceMethodExit(new Object[] { result });
/* 833 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean isDispositionRelatedProperty(String symbolicName)
/*     */   {
/* 838 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 839 */     boolean result = DispositionPropertySymNames.contains(symbolicName);
/* 840 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 841 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean isVitalRelatedProperty(String symbolicName)
/*     */   {
/* 846 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 847 */     boolean result = VitalPropertySymNames.contains(symbolicName);
/* 848 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 849 */     return result;
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
/*     */ 
/*     */   public static String escapeSQLStringValue(String orig)
/*     */   {
/* 865 */     Tracer.traceMethodEntry(new Object[] { orig });
/* 866 */     String result = orig;
/* 867 */     if ((result != null) && (result.length() > 0))
/*     */     {
/* 869 */       result = orig.replace("'", "''");
/*     */     }
/*     */     
/* 872 */     Tracer.traceMethodExit(new Object[] { result });
/* 873 */     return result;
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
/*     */   protected List<BulkItemResult> initiateDisposition(Object vwSession)
/*     */   {
/* 900 */     Tracer.traceMethodEntry(new Object[] { vwSession });
/*     */     
/*     */ 
/* 903 */     FilePlanRepository repository = (FilePlanRepository)getRepository();
/* 904 */     List<BulkOperation.EntityDescription> entityDescs = new ArrayList(1);
/* 905 */     entityDescs.add(new BulkOperation.EntityDescription(getEntityType(), getObjectIdentity()));
/*     */     
/* 907 */     List<BulkItemResult> results = BulkOperation.initiateDisposition(repository, entityDescs, vwSession);
/* 908 */     Tracer.traceMethodExit(new Object[] { results });
/* 909 */     return results;
/*     */   }
/*     */   
/*     */   public abstract String getObjectIdentity();
/*     */   
/*     */   public abstract String getClassName();
/*     */   
/*     */   protected void triggerNextPhase(DispositionSchedule dispSchedule) {}
/*     */   
/*     */   public abstract void internalSave(RMRefreshMode paramRMRefreshMode);
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */