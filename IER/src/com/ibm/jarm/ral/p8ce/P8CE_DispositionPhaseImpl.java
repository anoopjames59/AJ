/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.CustomObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.UpdatingBatch;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.Property;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.AlternateRetention;
/*     */ import com.ibm.jarm.api.core.AlternateRetentionList;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.ContentItem;
/*     */ import com.ibm.jarm.api.core.DispositionAction;
/*     */ import com.ibm.jarm.api.core.DispositionPhase;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.common.RALBaseRepository;
/*     */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
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
/*     */ public class P8CE_DispositionPhaseImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements DispositionPhase
/*     */ {
/*  57 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  58 */   private static final IGenerator<DispositionPhase> DispPhaseGenerator = new Generator();
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
/*  69 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "PhaseName", "IsScreeningRequired", "RetentionPeriodYears", "RetentionPeriodMonths", "RetentionPeriodDays", "ExportDestination", "PhaseNumber", "PhaseEffectiveDateModified", "DateLastModified" };
/*     */   
/*     */ 
/*     */ 
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */ 
/*     */   private AlternateRetentionList alternateRetentions;
/*     */   
/*     */ 
/*     */   private P8CE_DispositionScheduleImpl owningSchedule;
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  85 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  87 */     List<FilterElement> tempList = new ArrayList(1);
/*  88 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  89 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  94 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  99 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<DispositionPhase> getGenerator()
/*     */   {
/* 109 */     return DispPhaseGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_DispositionScheduleImpl getOwningSchedule()
/*     */   {
/* 120 */     return this.owningSchedule;
/*     */   }
/*     */   
/*     */   void setOwningSchedule(P8CE_DispositionScheduleImpl owningSchedule)
/*     */   {
/* 125 */     this.owningSchedule = owningSchedule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 133 */     return getMandatoryJaceFEs();
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
/*     */   static DispositionPhase createNew(Repository repository, String phaseName, P8CE_DispositionScheduleImpl owningSchedule, String idStr)
/*     */   {
/* 153 */     Tracer.traceMethodEntry(new Object[] { repository, phaseName, idStr });
/* 154 */     ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 155 */     Id newId = P8CE_Util.processIdStr(idStr);
/* 156 */     CustomObject jaceCustObj = Factory.CustomObject.createInstance(jaceObjStore, "Phase", newId);
/* 157 */     jaceCustObj.getProperties().putValue("PhaseName", phaseName);
/* 158 */     jaceCustObj.getProperties().putValue("DisposalSchedulePtr", owningSchedule.jaceCustomObject);
/*     */     
/* 160 */     P8CE_DispositionPhaseImpl newPhase = new P8CE_DispositionPhaseImpl(repository, phaseName, jaceCustObj, true);
/* 161 */     newPhase.owningSchedule = owningSchedule;
/*     */     
/* 163 */     Tracer.traceMethodExit(new Object[] { newPhase });
/* 164 */     return newPhase;
/*     */   }
/*     */   
/*     */   public P8CE_DispositionPhaseImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/* 169 */     super(EntityType.Phase, repository, identity, jaceCustomObject, isPlaceholder);
/* 170 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/* 171 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPhaseName()
/*     */   {
/* 180 */     Tracer.traceMethodEntry(new Object[0]);
/* 181 */     String result = P8CE_Util.getJacePropertyAsString(this, "PhaseName");
/* 182 */     Tracer.traceMethodExit(new Object[] { result });
/* 183 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 192 */     Tracer.traceMethodEntry(new Object[0]);
/* 193 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 196 */       establishedSubject = P8CE_Util.associateSubject();
/* 197 */       String result = null;
/* 198 */       PropertyFilter jacePF = new PropertyFilter();
/* 199 */       jacePF.addIncludeProperty(0, null, null, "RMEntityDescription", null);
/* 200 */       Property jaceProp = P8CE_Util.getOrFetchJaceProperty(this.jaceCustomObject, "RMEntityDescription", jacePF);
/* 201 */       if (jaceProp != null)
/* 202 */         result = jaceProp.getStringValue();
/* 203 */       Tracer.traceMethodExit(new Object[] { result });
/* 204 */       return result;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 208 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 212 */       if (establishedSubject) {
/* 213 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDescription(String description)
/*     */   {
/* 222 */     Tracer.traceMethodEntry(new Object[] { description });
/* 223 */     getProperties().putStringValue("RMEntityDescription", description);
/* 224 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DispositionAction getPhaseAction()
/*     */   {
/* 232 */     Tracer.traceMethodEntry(new Object[0]);
/* 233 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 236 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 238 */       List<FilterElement> jaceDispTrigFEs = P8CE_DispositionActionImpl.getMandatoryJaceFEs();
/* 239 */       IGenerator<DispositionAction> generator = P8CE_DispositionActionImpl.getGenerator();
/* 240 */       DispositionAction result = (DispositionAction)fetchSVObjPropValue(jaceDispTrigFEs, "PhaseAction", generator);
/*     */       
/* 242 */       Tracer.traceMethodExit(new Object[] { result });
/* 243 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 247 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 251 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 255 */       if (establishedSubject) {
/* 256 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPhaseAction(DispositionAction action)
/*     */   {
/* 265 */     Tracer.traceMethodEntry(new Object[] { action });
/* 266 */     getProperties().putObjectValue("PhaseAction", action);
/* 267 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isScreeningRequired()
/*     */   {
/* 275 */     Tracer.traceMethodEntry(new Object[0]);
/* 276 */     Boolean rawValue = P8CE_Util.getJacePropertyAsBoolean(this, "IsScreeningRequired");
/* 277 */     boolean result = rawValue != null ? rawValue.booleanValue() : false;
/* 278 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 279 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScreeningRequired(boolean isRequired)
/*     */   {
/* 287 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(isRequired) });
/* 288 */     getProperties().putBooleanValue("IsScreeningRequired", Boolean.valueOf(isRequired));
/* 289 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer[] getRetentionPeriod()
/*     */   {
/* 297 */     Tracer.traceMethodEntry(new Object[0]);
/* 298 */     Integer[] results = new Integer[3];
/* 299 */     results[0] = P8CE_Util.getJacePropertyAsInteger(this, "RetentionPeriodYears");
/* 300 */     results[1] = P8CE_Util.getJacePropertyAsInteger(this, "RetentionPeriodMonths");
/* 301 */     results[2] = P8CE_Util.getJacePropertyAsInteger(this, "RetentionPeriodDays");
/*     */     
/* 303 */     Tracer.traceMethodExit((Object[])results);
/* 304 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRetentionPeriod(Integer years, Integer months, Integer days)
/*     */   {
/* 312 */     Tracer.traceMethodEntry(new Object[] { years, months, days });
/* 313 */     Properties jaceProps = this.jaceCustomObject.getProperties();
/* 314 */     jaceProps.putValue("RetentionPeriodYears", years);
/* 315 */     jaceProps.putValue("RetentionPeriodMonths", months);
/* 316 */     jaceProps.putValue("RetentionPeriodDays", days);
/* 317 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReasonForChange()
/*     */   {
/* 326 */     Tracer.traceMethodEntry(new Object[0]);
/* 327 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 330 */       establishedSubject = P8CE_Util.associateSubject();
/* 331 */       String result = null;
/* 332 */       PropertyFilter jacePF = new PropertyFilter();
/* 333 */       jacePF.addIncludeProperty(0, null, null, "ReasonForChange", null);
/* 334 */       Property jaceProp = P8CE_Util.getOrFetchJaceProperty(this.jaceCustomObject, "ReasonForChange", jacePF);
/* 335 */       if (jaceProp != null)
/* 336 */         result = jaceProp.getStringValue();
/* 337 */       Tracer.traceMethodExit(new Object[] { result });
/* 338 */       return result;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 342 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 346 */       if (establishedSubject) {
/* 347 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setReasonForChange(String value)
/*     */   {
/* 356 */     Tracer.traceMethodEntry(new Object[] { value });
/* 357 */     getProperties().putStringValue("ReasonForChange", value);
/* 358 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExportDestination()
/*     */   {
/* 366 */     Tracer.traceMethodEntry(new Object[0]);
/* 367 */     String result = P8CE_Util.getJacePropertyAsString(this, "ExportDestination");
/* 368 */     Tracer.traceMethodExit(new Object[] { result });
/* 369 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExportDestination(String fileSystemDirPath)
/*     */   {
/* 377 */     Tracer.traceMethodEntry(new Object[] { fileSystemDirPath });
/* 378 */     getProperties().putStringValue("ExportDestination", fileSystemDirPath);
/* 379 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentItem getExportFormat()
/*     */   {
/* 387 */     Tracer.traceMethodEntry(new Object[0]);
/* 388 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 391 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 393 */       List<FilterElement> jaceDispScheduleFEs = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 394 */       IGenerator<ContentItem> generator = P8CE_ContentItemImpl.getContentItemGenerator();
/* 395 */       ContentItem result = (ContentItem)fetchSVObjPropValue(jaceDispScheduleFEs, "ExportFormat", generator);
/*     */       
/* 397 */       Tracer.traceMethodExit(new Object[] { result });
/* 398 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 402 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 406 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 410 */       if (establishedSubject) {
/* 411 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setExportFormat(ContentItem contentItem)
/*     */   {
/* 420 */     Tracer.traceMethodEntry(new Object[] { contentItem });
/* 421 */     getProperties().putObjectValue("ExportFormat", contentItem);
/* 422 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getPhaseNumber()
/*     */   {
/* 430 */     Tracer.traceMethodEntry(new Object[0]);
/* 431 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "PhaseNumber");
/* 432 */     Tracer.traceMethodExit(new Object[] { result });
/* 433 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DispositionSchedule getAssociatedSchedule()
/*     */   {
/* 441 */     Tracer.traceMethodEntry(new Object[0]);
/* 442 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 445 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 447 */       List<FilterElement> jaceDispScheduleFEs = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 448 */       IGenerator<DispositionSchedule> generator = P8CE_DispositionScheduleImpl.getGenerator();
/* 449 */       DispositionSchedule result = (DispositionSchedule)fetchSVObjPropValue(jaceDispScheduleFEs, "DisposalSchedulePtr", generator);
/*     */       
/* 451 */       Tracer.traceMethodExit(new Object[] { result });
/* 452 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 456 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 460 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 464 */       if (establishedSubject) {
/* 465 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AlternateRetentionList getAlternateRetentions()
/*     */   {
/* 474 */     Tracer.traceMethodEntry(new Object[0]);
/* 475 */     boolean establishedSubject = false;
/*     */     try {
/*     */       List<AlternateRetention> rawList;
/* 478 */       if (this.alternateRetentions == null)
/*     */       {
/* 480 */         establishedSubject = P8CE_Util.associateSubject();
/* 481 */         if (this.isCreationPending)
/*     */         {
/*     */ 
/* 484 */           this.alternateRetentions = new P8CE_AlternateRetentionListImpl(this, new ArrayList(0));
/*     */         }
/*     */         else
/*     */         {
/* 488 */           rawList = fetchRawAltRetentionList();
/* 489 */           List<AlternateRetention> orderedList = orderAlternateRetentions(rawList);
/* 490 */           this.alternateRetentions = new P8CE_AlternateRetentionListImpl(this, orderedList);
/*     */         }
/*     */       }
/*     */       
/* 494 */       Tracer.traceMethodExit(new Object[] { this.alternateRetentions });
/* 495 */       return this.alternateRetentions;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 499 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 503 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 507 */       if (establishedSubject) {
/* 508 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<AlternateRetention> fetchRawAltRetentionList()
/*     */   {
/* 520 */     Tracer.traceMethodEntry(new Object[0]);
/* 521 */     List<FilterElement> jaceAltRetentionFEs = P8CE_AlternateRetentionImpl.getMandatoryJaceFEs();
/* 522 */     IGenerator<AlternateRetention> generator = P8CE_AlternateRetentionImpl.getGenerator();
/* 523 */     List<AlternateRetention> results = fetchMVObjPropValueAsList(jaceAltRetentionFEs, "AlternateRetentions", generator);
/* 524 */     for (AlternateRetention ar : results)
/*     */     {
/* 526 */       ((P8CE_AlternateRetentionImpl)ar).setDispositionPhase(this);
/*     */     }
/*     */     
/* 529 */     Tracer.traceMethodExit(new Object[] { results });
/* 530 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AlternateRetention createAlternateRetention()
/*     */   {
/* 538 */     Tracer.traceMethodEntry(new Object[0]);
/* 539 */     AlternateRetention result = createAlternateRetention(null);
/* 540 */     Tracer.traceMethodExit(new Object[] { result });
/* 541 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AlternateRetention createAlternateRetention(String idStr)
/*     */   {
/* 549 */     Tracer.traceMethodEntry(new Object[0]);
/* 550 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 553 */       establishedSubject = P8CE_Util.associateSubject();
/* 554 */       AlternateRetention result = P8CE_AlternateRetentionImpl.createNew(getRepository(), this, idStr);
/* 555 */       Tracer.traceMethodExit(new Object[] { result });
/* 556 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 560 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 564 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 568 */       if (establishedSubject) {
/* 569 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 579 */     Tracer.traceMethodEntry(new Object[0]);
/* 580 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANT_DIRECT_DELETE_PHASE, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 589 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 590 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 593 */       establishedSubject = P8CE_Util.associateSubject();
/* 594 */       RALDispositionLogic.validateDispositionPhase((RALBaseRepository)getRepository(), this);
/*     */       
/* 596 */       Domain jaceDomain = P8CE_Util.getJaceDomain(this.jaceCustomObject);
/* 597 */       RefreshMode jaceRefreshMode = jarmRefreshMode == RMRefreshMode.Refresh ? RefreshMode.REFRESH : RefreshMode.NO_REFRESH;
/*     */       
/* 599 */       UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, jaceRefreshMode);
/* 600 */       boolean forcePhaseObjSave = true;
/* 601 */       contributeToSaveBatch(jarmRefreshMode, jaceUB, forcePhaseObjSave);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 606 */       CustomObject jaceDispSchedBase = markScheduleAsChanged();
/* 607 */       jaceUB.add(jaceDispSchedBase, null);
/*     */       
/* 609 */       long startTime = System.currentTimeMillis();
/* 610 */       jaceUB.updateBatch();
/* 611 */       long stopTime = System.currentTimeMillis();
/* 612 */       Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, stopTime, Integer.valueOf(1), null, new Object[] { jaceRefreshMode });
/*     */       
/* 614 */       this.isCreationPending = false;
/*     */       
/* 616 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 620 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 624 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.DisposalPhase });
/*     */     }
/*     */     finally
/*     */     {
/* 628 */       if (establishedSubject) {
/* 629 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   CustomObject markScheduleAsChanged()
/*     */   {
/* 639 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 641 */     CustomObject jaceSchedBase = null;
/* 642 */     boolean scheduleCreationIsPending = false;
/* 643 */     if (this.owningSchedule != null)
/*     */     {
/* 645 */       scheduleCreationIsPending = this.owningSchedule.isCreationPending();
/* 646 */       jaceSchedBase = this.owningSchedule.jaceCustomObject;
/*     */     }
/*     */     else
/*     */     {
/* 650 */       if (!this.jaceCustomObject.getProperties().isPropertyPresent("DisposalSchedulePtr"))
/*     */       {
/* 652 */         this.jaceCustomObject.fetchProperty("DisposalSchedulePtr", null);
/*     */       }
/* 654 */       jaceSchedBase = (CustomObject)this.jaceCustomObject.getProperties().getEngineObjectValue("DisposalSchedulePtr");
/*     */     }
/*     */     
/* 657 */     if ((jaceSchedBase != null) && (!scheduleCreationIsPending))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 662 */       if (!jaceSchedBase.getProperties().isPropertyPresent("ReasonForChange"))
/*     */       {
/* 664 */         jaceSchedBase.fetchProperties(new String[] { "ReasonForChange" });
/*     */       }
/* 666 */       String schedReasonForChg = jaceSchedBase.getProperties().getStringValue("ReasonForChange");
/* 667 */       if ((schedReasonForChg == null) || (schedReasonForChg.trim().length() == 0))
/*     */       {
/* 669 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_SCHED_MUST_HAVE_REASON_FOR_CHANGE, new Object[0]);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 674 */       jaceSchedBase.getProperties().putValue("ReasonForChange", schedReasonForChg);
/*     */     }
/*     */     
/* 677 */     Tracer.traceMethodExit(new Object[] { jaceSchedBase });
/* 678 */     return jaceSchedBase;
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
/*     */   void contributeToSaveBatch(RMRefreshMode jarmRefreshMode, UpdatingBatch jaceUB, boolean forceUpdate)
/*     */   {
/* 697 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode, jaceUB });
/* 698 */     if (forceUpdate)
/*     */     {
/* 700 */       String phaseReason = getReasonForChange();
/* 701 */       setReasonForChange(phaseReason);
/*     */     }
/*     */     
/* 704 */     if (this.jaceCustomObject.getProperties().isDirty())
/*     */     {
/* 706 */       jaceUB.add(this.jaceCustomObject, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 711 */     if (this.alternateRetentions != null)
/*     */     {
/* 713 */       ((P8CE_AlternateRetentionListImpl)this.alternateRetentions).contributeToSaveBatch(jarmRefreshMode, jaceUB);
/*     */     }
/* 715 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 724 */     Tracer.traceMethodEntry(new Object[0]);
/* 725 */     this.alternateRetentions = null;
/* 726 */     super.refresh();
/* 727 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 736 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 737 */     this.alternateRetentions = null;
/* 738 */     super.refresh(jarmFilter);
/* 739 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 748 */     Tracer.traceMethodEntry(new Object[] { symbolicPropertyNames });
/* 749 */     this.alternateRetentions = null;
/* 750 */     super.refresh(symbolicPropertyNames);
/* 751 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public Date calcAltRetentionEffectiveDate(BaseEntity entity)
/*     */   {
/* 768 */     Tracer.traceMethodEntry(new Object[] { entity });
/* 769 */     Date effectiveDate = null;
/*     */     
/* 771 */     AlternateRetentionList altRetents = getAlternateRetentions();
/* 772 */     TreeSet<Date> altRetentsEffectiveDates = new TreeSet();
/* 773 */     if ((altRetents != null) && (altRetents.size() > 0))
/*     */     {
/* 775 */       for (AlternateRetention altRetent : altRetents)
/*     */       {
/*     */         try
/*     */         {
/* 779 */           Date altRetentEffDate = ((P8CE_AlternateRetentionImpl)altRetent).calcEffectiveRetentionDate(entity);
/* 780 */           if (altRetentEffDate != null) {
/* 781 */             altRetentsEffectiveDates.add(altRetentEffDate);
/*     */           }
/*     */         }
/*     */         catch (Exception ignored) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 789 */       if (altRetentsEffectiveDates.size() > 0)
/*     */       {
/* 791 */         effectiveDate = (Date)altRetentsEffectiveDates.last();
/*     */       }
/*     */     }
/*     */     
/* 795 */     Tracer.traceMethodExit(new Object[] { effectiveDate });
/* 796 */     return effectiveDate;
/*     */   }
/*     */   
/*     */   public Date calcDefaultRetentionEffectiveDate(BaseEntity entity)
/*     */   {
/* 801 */     Tracer.traceMethodEntry(new Object[] { entity });
/* 802 */     Date effectiveDate = null;
/*     */     
/* 804 */     Date entityCutoffDate = entity.getProperties().getDateTimeValue("CutoffDate");
/* 805 */     if (entityCutoffDate != null)
/*     */     {
/* 807 */       Integer[] defaultRetentionPeriod = getRetentionPeriod();
/* 808 */       effectiveDate = RALDispositionLogic.addRetentionPeriod(entityCutoffDate, defaultRetentionPeriod);
/*     */     }
/*     */     
/* 811 */     Tracer.traceMethodExit(new Object[] { effectiveDate });
/* 812 */     return effectiveDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 821 */     return super.toString("P8CE_DispositionPhaseImpl", "PhaseName");
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
/*     */   private List<AlternateRetention> orderAlternateRetentions(List<AlternateRetention> origList)
/*     */   {
/* 838 */     Tracer.traceMethodEntry(new Object[] { origList });
/* 839 */     Collections.sort(origList, new P8CE_AlternateRetentionImpl.AlternateRetentionComparator());
/* 840 */     Tracer.traceMethodExit(new Object[] { origList });
/* 841 */     return origList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<DispositionPhase>
/*     */   {
/*     */     public DispositionPhase create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 854 */       P8CE_DispositionPhaseImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 855 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 857 */       String identity = jaceCustomObj.getProperties().getStringValue("PhaseName");
/* 858 */       DispositionPhase result = new P8CE_DispositionPhaseImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 860 */       P8CE_DispositionPhaseImpl.Tracer.traceMethodExit(new Object[] { result });
/* 861 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class DispositionPhaseComparator
/*     */     implements Comparator<DispositionPhase>
/*     */   {
/*     */     public int compare(DispositionPhase o1, DispositionPhase o2)
/*     */     {
/* 876 */       P8CE_DispositionPhaseImpl.Tracer.traceMethodEntry(new Object[] { o1, o2 });
/* 877 */       Integer o1PhaseNum = o1.getPhaseNumber();
/* 878 */       Integer o2PhaseNum = o2.getPhaseNumber();
/*     */       
/* 880 */       int result = o1PhaseNum.compareTo(o2PhaseNum);
/* 881 */       P8CE_DispositionPhaseImpl.Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/* 882 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_DispositionPhaseImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */