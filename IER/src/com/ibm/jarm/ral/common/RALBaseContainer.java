/*      */ package com.ibm.jarm.ral.common;
/*      */ 
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.collection.RMPageIterator;
/*      */ import com.ibm.jarm.api.constants.DataModelType;
/*      */ import com.ibm.jarm.api.constants.DataType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMAccessRight;
/*      */ import com.ibm.jarm.api.constants.RMCardinality;
/*      */ import com.ibm.jarm.api.constants.RMReceiptStatus;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.constants.RetainMetadata;
/*      */ import com.ibm.jarm.api.constants.SchedulePropagation;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.ContentItem;
/*      */ import com.ibm.jarm.api.core.ContentRepository;
/*      */ import com.ibm.jarm.api.core.DefensiblyDisposable;
/*      */ import com.ibm.jarm.api.core.DispositionAllocatable;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.FilePlan;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.Record;
/*      */ import com.ibm.jarm.api.core.RecordContainer;
/*      */ import com.ibm.jarm.api.core.RecordFolder;
/*      */ import com.ibm.jarm.api.core.RecordVolume;
/*      */ import com.ibm.jarm.api.core.RecordVolumeContainer;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMErrorRecord;
/*      */ import com.ibm.jarm.api.exception.RMErrorStack;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.meta.RMPropertyDescriptionInteger;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMProperty;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.query.RMSearch;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.security.RMUser;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.NamingUtils;
/*      */ import com.ibm.jarm.api.util.RMLString;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class RALBaseContainer
/*      */   extends RALBaseEntity
/*      */   implements Container, DispositionAllocatable
/*      */ {
/*   63 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*      */   
/*      */   public static final String DefaultVolumeSuffixPattern = "00000";
/*      */   
/*      */   public static final String DoDUnclassifiedValue = "Unclassified";
/*      */   
/*      */   public static final String FormerlyRestrictedData = "Formerly Restricted Data";
/*      */   public static final String RestrictedData = "Restricted Data";
/*      */   public static final String DDRetentionPeriodFormat = "%04d-%04d-%04d";
/*      */   public static final int DDRetentionYearsMin = 0;
/*      */   public static final int DDRetentionYearsMax = 9999;
/*      */   public static final int DDRetentionMonthsMin = 0;
/*      */   public static final int DDRetentionMonthsMax = 9999;
/*      */   public static final int DDRetentionDaysMin = 0;
/*      */   public static final int DDRetentionDaysMax = 9999;
/*      */   public static final List<Integer> DDAllowedEntityTypes;
/*      */   
/*      */   static
/*      */   {
/*   82 */     List<Integer> tmpList = new ArrayList(2);
/*   83 */     tmpList.add(Integer.valueOf(EntityType.ElectronicRecord.getIntValue()));
/*   84 */     tmpList.add(Integer.valueOf(EntityType.EmailRecord.getIntValue()));
/*   85 */     DDAllowedEntityTypes = Collections.unmodifiableList(tmpList);
/*      */   }
/*      */   
/*      */ 
/*   89 */   protected static final String AutomaticVolumeCloseReason = RMLString.get("volume.autoCloseReason", "Volume closed due to addition of new active sibling volume.");
/*      */   
/*   91 */   private static final EntityType[] EMPTY_ENTITY_TYPES = new EntityType[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private EntityType[] currentAllowedEntityTypes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected RALBaseContainer(EntityType entityType, Repository repository, String identity, boolean isPlaceholder)
/*      */   {
/*  111 */     super(entityType, repository, identity, isPlaceholder);
/*  112 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, Boolean.valueOf(isPlaceholder) });
/*      */     
/*  114 */     this.currentAllowedEntityTypes = EMPTY_ENTITY_TYPES;
/*  115 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityType[] getAllowedContaineeTypes()
/*      */   {
/*  123 */     Tracer.traceMethodEntry(new Object[0]);
/*  124 */     EntityType[] clonedTypes = new EntityType[this.currentAllowedEntityTypes.length];
/*  125 */     System.arraycopy(this.currentAllowedEntityTypes, 0, clonedTypes, 0, this.currentAllowedEntityTypes.length);
/*      */     
/*  127 */     Tracer.traceMethodExit(new Object[] { clonedTypes });
/*  128 */     return clonedTypes;
/*      */   }
/*      */   
/*      */   protected void setAllowedContaineeTypes(EntityType[] allowedEntityTypes)
/*      */   {
/*  133 */     this.currentAllowedEntityTypes = allowedEntityTypes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canContain(EntityType type)
/*      */   {
/*  141 */     Tracer.traceMethodEntry(new Object[] { type });
/*  142 */     boolean result = false;
/*  143 */     for (int i = 0; i < this.currentAllowedEntityTypes.length; i++)
/*      */     {
/*  145 */       if (this.currentAllowedEntityTypes[i].equals(type))
/*      */       {
/*  147 */         result = true;
/*  148 */         break;
/*      */       }
/*      */     }
/*      */     
/*  152 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  153 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isPhysicalRecordContainer()
/*      */   {
/*  161 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*  163 */     EntityType containerType = getEntityType();
/*  164 */     if (containerType == EntityType.RecordVolume)
/*      */     {
/*  166 */       Container volumeParent = getParent();
/*  167 */       containerType = volumeParent.getEntityType();
/*      */     }
/*      */     
/*  170 */     boolean result = (containerType == EntityType.PhysicalRecordFolder) || (containerType == EntityType.PhysicalContainer) || (containerType == EntityType.HybridRecordFolder);
/*      */     
/*      */ 
/*      */ 
/*  174 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  175 */     return result;
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
/*      */   public static boolean isARecordFolderType(EntityType entityType)
/*      */   {
/*  189 */     Tracer.traceMethodEntry(new Object[] { entityType });
/*      */     
/*  191 */     boolean result = (entityType == EntityType.RecordFolder) || (entityType == EntityType.ElectronicRecordFolder) || (entityType == EntityType.PhysicalContainer) || (entityType == EntityType.HybridRecordFolder) || (entityType == EntityType.PhysicalRecordFolder);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  197 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  198 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void validateRecordCategoryUpdate(RMProperties props)
/*      */   {
/*  209 */     Tracer.traceMethodEntry(new Object[] { props });
/*      */     
/*      */ 
/*  212 */     RMProperty inactiveProp = RALBaseEntity.getPropIfModified(props, "Inactive");
/*  213 */     if (inactiveProp != null)
/*      */     {
/*  215 */       if (Boolean.TRUE.equals(inactiveProp.getBooleanValue()))
/*      */       {
/*  217 */         boolean hasValidReason = false;
/*  218 */         if ((hasValidReason = props.isPropertyPresent("ReasonForInactivate")))
/*      */         {
/*  220 */           String reasonForInactive = props.getStringValue("ReasonForInactivate");
/*  221 */           if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0)) {
/*  222 */             hasValidReason = false;
/*      */           }
/*      */         }
/*  225 */         if (!hasValidReason) {
/*  226 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_DEPENDENT_REQUIRED_PROP_VALUE, new Object[] { "ReasonForInactivate", "Inactive" });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  232 */     RMProperty rcNameProp = RALBaseEntity.getPropIfModified(props, "RecordCategoryName");
/*  233 */     if (rcNameProp != null)
/*      */     {
/*  235 */       String rcName = rcNameProp.getStringValue();
/*  236 */       if ((rcName == null) || (rcName.trim().length() == 0))
/*  237 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordCategoryName" });
/*      */     }
/*  239 */     RMProperty rcIdentProp = RALBaseEntity.getPropIfModified(props, "RecordCategoryIdentifier");
/*  240 */     if (rcIdentProp != null)
/*      */     {
/*  242 */       String rcIdent = rcIdentProp.getStringValue();
/*  243 */       if ((rcIdent == null) || (rcIdent.trim().length() == 0)) {
/*  244 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordCategoryIdentifier" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  249 */     if ((rcNameProp != null) || (rcIdentProp != null))
/*      */     {
/*  251 */       Container parent = getParent();
/*  252 */       if (!verifyContainerNamesAreUnique(parent, EntityType.RecordCategory, props, this))
/*      */       {
/*  254 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_NON_UNIQUE_CONTAINER_PROP_VALUE, new Object[] { "RecordCategory", getObjectIdentity(), "RecordCategoryName", "RecordCategoryIdentifier" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  260 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   protected void validateAndPrepareNewRecordCategory(String classIdent, RMProperties props, List<RMPermission> perms)
/*      */   {
/*  275 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms });
/*      */     
/*  277 */     EntityType parentContainerEntityType = getEntityType();
/*  278 */     if ((parentContainerEntityType != EntityType.FilePlan) && (!isOpen()))
/*      */     {
/*  280 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PARENT_CONTAINER_ISCLOSED, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  285 */     EntityType newRecCategoryType = getEntityTypeForClass(getRepository(), classIdent);
/*  286 */     if (!canContain(newRecCategoryType))
/*      */     {
/*  288 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CONTAIN_CHILDTYPE, new Object[] { getObjectIdentity(), "RecordCategory" });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  293 */     if (isInactive())
/*      */     {
/*  295 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_TO_INACTIVE_PARENT_CONTAINER, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/*      */ 
/*  299 */     if ((props.isPropertyPresent("Inactive")) && (Boolean.TRUE == props.getBooleanValue("Inactive")))
/*      */     {
/*      */ 
/*  302 */       boolean hasValidReason = false;
/*  303 */       if ((hasValidReason = props.isPropertyPresent("ReasonForInactivate")))
/*      */       {
/*  305 */         String reasonForInactive = props.getStringValue("ReasonForInactivate");
/*  306 */         if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0)) {
/*  307 */           hasValidReason = false;
/*      */         }
/*      */       }
/*  310 */       if (!hasValidReason) {
/*  311 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_DEPENDENT_REQUIRED_PROP_VALUE, new Object[] { "ReasonForInactivate", "Inactive" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  317 */     String rcName = null;
/*  318 */     if (props.isPropertyPresent("RecordCategoryName"))
/*  319 */       rcName = props.getStringValue("RecordCategoryName");
/*  320 */     if ((rcName == null) || (rcName.trim().length() == 0))
/*  321 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordCategoryName" });
/*  322 */     String rcIdent = null;
/*  323 */     if (props.isPropertyPresent("RecordCategoryIdentifier"))
/*  324 */       rcIdent = props.getStringValue("RecordCategoryIdentifier");
/*  325 */     if ((rcIdent == null) || (rcIdent.trim().length() == 0)) {
/*  326 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordCategoryIdentifier" });
/*      */     }
/*      */     
/*      */ 
/*  330 */     if (!verifyContainerNamesAreUnique(this, EntityType.RecordCategory, props, null))
/*      */     {
/*  332 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_NON_UNIQUE_CONTAINER_PROP_VALUE, new Object[] { "RecordCategory", getObjectIdentity(), "RecordCategoryName", "RecordCategoryIdentifier" });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  339 */     if ((props.isPropertyPresent("IsVitalRecord")) && (Boolean.TRUE.equals(props.getBooleanValue("IsVitalRecord"))))
/*      */     {
/*      */ 
/*  342 */       validateVitalProperties(props, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  349 */     Date dateNow = new Date();
/*      */     
/*  351 */     String recCatName = props.getStringValue("RecordCategoryName");
/*  352 */     props.putStringValue("FolderName", recCatName);
/*      */     
/*  354 */     if ((!props.isPropertyPresent("DateOpened")) || (props.getDateTimeValue("DateOpened") == null))
/*      */     {
/*      */ 
/*  357 */       props.putDateTimeValue("DateOpened", dateNow);
/*      */     }
/*      */     
/*      */ 
/*  361 */     initializeDispositionProperties(props, dateNow);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  366 */     List<Integer> initAllowedRMTypes = null;
/*  367 */     if ((props.isPropertyPresent("RMRetentionTriggerPropertyName")) && (props.getStringValue("RMRetentionTriggerPropertyName") != null))
/*      */     {
/*      */ 
/*  370 */       initAllowedRMTypes = DDAllowedEntityTypes;
/*      */     }
/*      */     else
/*      */     {
/*  374 */       initAllowedRMTypes = getInitialAllowedRMTypes(classIdent);
/*      */     }
/*  376 */     props.putIntegerListValue("AllowedRMTypes", initAllowedRMTypes);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  381 */     List<Integer> newParentAllowedTypes = new ArrayList();
/*  382 */     List<Integer> currentParentAllowedTypes = getProperties().getIntegerListValue("AllowedRMTypes");
/*  383 */     for (Iterator i$ = currentParentAllowedTypes.iterator(); i$.hasNext();) { int allowedType = ((Integer)i$.next()).intValue();
/*      */       
/*  385 */       if ((allowedType != EntityType.ElectronicRecordFolder.getIntValue()) && (allowedType != EntityType.HybridRecordFolder.getIntValue()) && (allowedType != EntityType.PhysicalContainer.getIntValue()) && (allowedType != EntityType.PhysicalRecordFolder.getIntValue()) && (allowedType != EntityType.RecordFolder.getIntValue()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  391 */         newParentAllowedTypes.add(Integer.valueOf(allowedType));
/*      */       }
/*      */     }
/*  394 */     getProperties().putIntegerListValue("AllowedRMTypes", newParentAllowedTypes);
/*      */     
/*  396 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void validateRecordFolderUpdate(RMProperties props)
/*      */   {
/*  407 */     Tracer.traceMethodEntry(new Object[] { props });
/*      */     
/*      */ 
/*  410 */     RMProperty inactiveProp = RALBaseEntity.getPropIfModified(props, "Inactive");
/*  411 */     if (inactiveProp != null)
/*      */     {
/*  413 */       if (Boolean.TRUE.equals(inactiveProp.getBooleanValue()))
/*      */       {
/*  415 */         boolean hasValidReason = false;
/*  416 */         if ((hasValidReason = props.isPropertyPresent("ReasonForInactivate")))
/*      */         {
/*  418 */           String reasonForInactive = props.getStringValue("ReasonForInactivate");
/*  419 */           if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0)) {
/*  420 */             hasValidReason = false;
/*      */           }
/*      */         }
/*  423 */         if (!hasValidReason) {
/*  424 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_DEPENDENT_REQUIRED_PROP_VALUE, new Object[] { "ReasonForInactivate", "Inactive" });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  430 */     RMProperty reviewerProp = RALBaseEntity.getPropIfModified(props, "Reviewer");
/*  431 */     if (reviewerProp != null)
/*      */     {
/*  433 */       String reviewerValue = reviewerProp.getStringValue();
/*  434 */       if ((reviewerValue == null) || (reviewerValue.trim().length() == 0)) {
/*  435 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "Reviewer" });
/*      */       }
/*      */     }
/*      */     
/*  439 */     RMProperty rfNameProp = RALBaseEntity.getPropIfModified(props, "RecordFolderName");
/*  440 */     if (rfNameProp != null)
/*      */     {
/*  442 */       String rfName = rfNameProp.getStringValue();
/*  443 */       if ((rfName == null) || (rfName.trim().length() == 0))
/*  444 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordFolderName" });
/*      */     }
/*  446 */     RMProperty rfIdentProp = RALBaseEntity.getPropIfModified(props, "RecordFolderIdentifier");
/*  447 */     if (rfIdentProp != null)
/*      */     {
/*  449 */       String rfIdent = rfIdentProp.getStringValue();
/*  450 */       if ((rfIdent == null) || (rfIdent.trim().length() == 0)) {
/*  451 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordFolderIdentifier" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  456 */     if ((rfNameProp != null) || (rfIdentProp != null))
/*      */     {
/*  458 */       Container parent = getParent();
/*  459 */       if (!verifyContainerNamesAreUnique(parent, EntityType.RecordFolder, props, this))
/*      */       {
/*  461 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_NON_UNIQUE_CONTAINER_PROP_VALUE, new Object[] { "RecordFolder", getObjectIdentity(), "RecordFolderName", "RecordFolderIdentifier" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  467 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   protected boolean validateAndPrepareNewRecordFolder(String classIdent, RMProperties props, List<RMPermission> perms)
/*      */   {
/*  486 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms });
/*      */     
/*      */ 
/*  489 */     if (isClosed())
/*      */     {
/*  491 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PARENT_CONTAINER_ISCLOSED, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  496 */     EntityType newRecFolderType = getEntityTypeForClass(getRepository(), classIdent);
/*  497 */     if (!canContain(newRecFolderType))
/*      */     {
/*  499 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CONTAIN_CHILDTYPE, new Object[] { getObjectIdentity(), "RecordFolder" });
/*      */     }
/*      */     
/*      */ 
/*  503 */     refresh(new String[] { "Inactive", "Reviewer" });
/*  504 */     RMProperties parentProps = getProperties();
/*      */     
/*      */ 
/*  507 */     if (Boolean.TRUE == parentProps.getBooleanValue("Inactive"))
/*      */     {
/*  509 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_TO_INACTIVE_PARENT_CONTAINER, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  514 */     if ((props.isPropertyPresent("Inactive")) && (Boolean.TRUE == props.getBooleanValue("Inactive")))
/*      */     {
/*      */ 
/*  517 */       boolean hasValidReason = false;
/*  518 */       if ((hasValidReason = props.isPropertyPresent("ReasonForInactivate")))
/*      */       {
/*  520 */         String reasonForInactive = props.getStringValue("ReasonForInactivate");
/*  521 */         if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0)) {
/*  522 */           hasValidReason = false;
/*      */         }
/*      */       }
/*  525 */       if (!hasValidReason) {
/*  526 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_DEPENDENT_REQUIRED_PROP_VALUE, new Object[] { "ReasonForInactivate", "Inactive" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  532 */     String reviewerValue = null;
/*  533 */     if (props.isPropertyPresent("Reviewer"))
/*  534 */       reviewerValue = props.getStringValue("Reviewer");
/*  535 */     if ((reviewerValue == null) || (reviewerValue.trim().length() == 0))
/*      */     {
/*  537 */       if (parentProps.isPropertyPresent("Reviewer")) {
/*  538 */         props.putStringValue("Reviewer", parentProps.getStringValue("Reviewer"));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  543 */     String rfName = null;
/*  544 */     if (props.isPropertyPresent("RecordFolderName"))
/*  545 */       rfName = props.getStringValue("RecordFolderName");
/*  546 */     if ((rfName == null) || (rfName.trim().length() == 0))
/*  547 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordFolderName" });
/*  548 */     String rfIdent = null;
/*  549 */     if (props.isPropertyPresent("RecordFolderIdentifier"))
/*  550 */       rfIdent = props.getStringValue("RecordFolderIdentifier");
/*  551 */     if ((rfIdent == null) || (rfIdent.trim().length() == 0)) {
/*  552 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "RecordFolderIdentifier" });
/*      */     }
/*      */     
/*      */ 
/*  556 */     if (!verifyContainerNamesAreUnique(this, EntityType.RecordFolder, props, null))
/*      */     {
/*  558 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_NON_UNIQUE_CONTAINER_PROP_VALUE, new Object[] { "RecordFolder", getObjectIdentity(), "RecordFolderName", "RecordFolderIdentifier" });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  565 */     if ((props.isPropertyPresent("DateClosed")) && (props.getDateTimeValue("DateClosed") != null))
/*      */     {
/*      */ 
/*  568 */       boolean hasValidReason = false;
/*  569 */       if ((hasValidReason = props.isPropertyPresent("ReasonForClose")))
/*      */       {
/*  571 */         String reasonForInactive = props.getStringValue("ReasonForClose");
/*  572 */         if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0)) {
/*  573 */           hasValidReason = false;
/*      */         }
/*      */       }
/*  576 */       if (!hasValidReason) {
/*  577 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_DEPENDENT_REQUIRED_PROP_VALUE, new Object[] { "ReasonForClose", "DateClosed" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  583 */     if ((props.isPropertyPresent("IsVitalRecord")) && (Boolean.TRUE.equals(props.getBooleanValue("IsVitalRecord"))))
/*      */     {
/*      */ 
/*  586 */       validateVitalProperties(props, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  593 */     Date dateNow = new Date();
/*      */     
/*  595 */     String recFolderName = props.getStringValue("RecordFolderName");
/*  596 */     props.putStringValue("FolderName", recFolderName);
/*      */     
/*  598 */     if ((!props.isPropertyPresent("DateOpened")) || (props.getDateTimeValue("DateOpened") == null))
/*      */     {
/*      */ 
/*  601 */       props.putDateTimeValue("DateOpened", dateNow);
/*      */     }
/*      */     
/*      */ 
/*  605 */     initializeDispositionProperties(props, dateNow);
/*      */     
/*      */ 
/*      */ 
/*  609 */     if (newRecFolderType != EntityType.ElectronicRecordFolder)
/*      */     {
/*  611 */       if ((!props.isPropertyPresent("HomeLocation")) || (props.getObjectValue("HomeLocation") == null))
/*      */       {
/*      */ 
/*  614 */         if (props.isPropertyPresent("Location"))
/*      */         {
/*  616 */           Object locationValue = props.getObjectValue("Location");
/*  617 */           if (locationValue != null) {
/*  618 */             props.putObjectValue("HomeLocation", locationValue);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  625 */     List<Integer> initAllowedRMTypes = getInitialAllowedRMTypes(classIdent);
/*  626 */     props.putIntegerListValue("AllowedRMTypes", initAllowedRMTypes);
/*      */     
/*      */ 
/*      */ 
/*  630 */     List<Integer> newParentAllowedTypes = new ArrayList();
/*  631 */     List<Integer> currentParentAllowedTypes = getProperties().getIntegerListValue("AllowedRMTypes");
/*  632 */     EntityType thisParentType = getEntityType();
/*  633 */     if (thisParentType == EntityType.RecordCategory)
/*      */     {
/*      */ 
/*  636 */       for (Iterator i$ = currentParentAllowedTypes.iterator(); i$.hasNext();) { int allowedType = ((Integer)i$.next()).intValue();
/*      */         
/*  638 */         if (allowedType != EntityType.RecordCategory.getIntValue()) {
/*  639 */           newParentAllowedTypes.add(Integer.valueOf(allowedType));
/*      */         }
/*      */       }
/*  642 */       getProperties().putIntegerListValue("AllowedRMTypes", newParentAllowedTypes);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  653 */     boolean createChildRecVolume = newRecFolderType != EntityType.PhysicalContainer;
/*      */     
/*  655 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(createChildRecVolume) });
/*  656 */     return createChildRecVolume;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void validateRecordVolumeUpdate(RMProperties props)
/*      */   {
/*  667 */     Tracer.traceMethodEntry(new Object[] { props });
/*      */     
/*      */ 
/*  670 */     RMProperty reviewerProp = RALBaseEntity.getPropIfModified(props, "Reviewer");
/*  671 */     if (reviewerProp != null)
/*      */     {
/*  673 */       String reviewerValue = reviewerProp.getStringValue();
/*  674 */       if ((reviewerValue == null) || (reviewerValue.trim().length() == 0)) {
/*  675 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "Reviewer" });
/*      */       }
/*      */     }
/*      */     
/*  679 */     RMProperty dateClosedProp = RALBaseEntity.getPropIfModified(props, "DateClosed");
/*  680 */     if ((dateClosedProp != null) && (dateClosedProp.getDateTimeValue() != null))
/*      */     {
/*  682 */       boolean hasValidReason = false;
/*  683 */       if ((hasValidReason = props.isPropertyPresent("ReasonForClose")))
/*      */       {
/*  685 */         String reasonForInactive = props.getStringValue("ReasonForClose");
/*  686 */         if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0)) {
/*  687 */           hasValidReason = false;
/*      */         }
/*      */       }
/*  690 */       if (!hasValidReason) {
/*  691 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_DEPENDENT_REQUIRED_PROP_VALUE, new Object[] { "ReasonForClose", "DateClosed" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  696 */     RMProperty volumeNameProp = RALBaseEntity.getPropIfModified(props, "VolumeName");
/*  697 */     if (volumeNameProp != null)
/*      */     {
/*  699 */       String volumeNameValue = volumeNameProp.getStringValue();
/*  700 */       if ((volumeNameValue == null) || (volumeNameValue.trim().length() == 0)) {
/*  701 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_REQUIRED_PROP_VALUE, new Object[] { "VolumeName" });
/*      */       }
/*  703 */       Container parent = getParent();
/*  704 */       if (!verifyContainerNamesAreUnique(parent, EntityType.RecordVolume, props, this))
/*      */       {
/*  706 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_NON_UNIQUE_CONTAINER_PROP_VALUE, new Object[] { "RecordVolume", getObjectIdentity(), "VolumeName" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  712 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   protected void validateAndPrepareNewRecordVolume(String classIdent, String volumeName, RMProperties props, List<RMPermission> perms)
/*      */   {
/*  730 */     Tracer.traceMethodEntry(new Object[] { classIdent, volumeName, props, perms });
/*      */     
/*  732 */     if (!isOpen())
/*      */     {
/*  734 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PARENT_CONTAINER_ISCLOSED, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/*      */ 
/*  738 */     EntityType thisEntityType = getEntityType();
/*      */     
/*  740 */     if ((!canContain(EntityType.RecordVolume)) || (EntityType.PhysicalContainer == thisEntityType))
/*      */     {
/*  742 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CONTAIN_CHILDTYPE, new Object[] { getObjectIdentity(), "RecordVolume" });
/*      */     }
/*      */     
/*      */ 
/*  746 */     List<String> refreshProperties = getVitalPropertyNames();
/*  747 */     refreshProperties.add("Inactive");
/*  748 */     refreshProperties.add("Reviewer");
/*  749 */     refresh((String[])refreshProperties.toArray(new String[0]));
/*  750 */     RMProperties parentProps = getProperties();
/*      */     
/*      */ 
/*  753 */     if (Boolean.TRUE == parentProps.getBooleanValue("Inactive"))
/*      */     {
/*  755 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_ADD_TO_INACTIVE_PARENT_CONTAINER, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  761 */     String reviewerValue = null;
/*  762 */     if (props.isPropertyPresent("Reviewer"))
/*  763 */       reviewerValue = props.getStringValue("Reviewer");
/*  764 */     if ((reviewerValue == null) || (reviewerValue.trim().length() == 0))
/*      */     {
/*  766 */       if (parentProps.isPropertyPresent("Reviewer")) {
/*  767 */         props.putStringValue("Reviewer", parentProps.getStringValue("Reviewer"));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  772 */     if ((props.isPropertyPresent("DateClosed")) && (props.getDateTimeValue("DateClosed") != null))
/*      */     {
/*      */ 
/*  775 */       boolean hasValidReason = false;
/*  776 */       if ((hasValidReason = props.isPropertyPresent("ReasonForClose")))
/*      */       {
/*  778 */         String reasonForInactive = props.getStringValue("ReasonForClose");
/*  779 */         if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0)) {
/*  780 */           hasValidReason = false;
/*      */         }
/*      */       }
/*  783 */       if (!hasValidReason) {
/*  784 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_MISSING_DEPENDENT_REQUIRED_PROP_VALUE, new Object[] { "ReasonForClose", "DateClosed" });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  790 */     if ((props.isPropertyPresent("IsVitalRecord")) && (Boolean.TRUE.equals(props.getBooleanValue("IsVitalRecord"))))
/*      */     {
/*      */ 
/*  793 */       validateVitalProperties(props, false);
/*      */     }
/*      */     
/*      */ 
/*  797 */     String folderUniqueIdentValue = NamingUtils.generateNextVolumeName((RecordVolumeContainer)this);
/*  798 */     props.putStringValue("RecordFolderIdentifier", folderUniqueIdentValue);
/*      */     
/*      */ 
/*  801 */     String volumeNameValue = volumeName;
/*  802 */     if ((volumeNameValue == null) || (volumeNameValue.trim().length() == 0))
/*  803 */       volumeNameValue = folderUniqueIdentValue;
/*  804 */     props.putStringValue("VolumeName", volumeNameValue);
/*  805 */     props.putStringValue("FolderName", volumeNameValue);
/*      */     
/*      */ 
/*      */ 
/*  809 */     Object locationValue = null;
/*  810 */     if (!props.isPropertyPresent("Location"))
/*      */     {
/*  812 */       refresh(new String[] { "Location" });
/*  813 */       if (getProperties().isPropertyPresent("Location"))
/*      */       {
/*  815 */         locationValue = getProperties().getObjectValue("Location");
/*  816 */         props.putObjectValue("Location", locationValue);
/*      */       }
/*      */     }
/*      */     
/*  820 */     if ((locationValue != null) && (!props.isPropertyPresent("HomeLocation"))) {
/*  821 */       props.putObjectValue("HomeLocation", locationValue);
/*      */     }
/*      */     
/*  824 */     String containerTypeValue = determineVolumeContainerType(thisEntityType);
/*  825 */     props.putStringValue("ContainerType", containerTypeValue);
/*      */     
/*      */ 
/*  828 */     if ((!props.isPropertyPresent("DateOpened")) || (props.getDateTimeValue("DateOpened") == null))
/*      */     {
/*      */ 
/*  831 */       props.putDateTimeValue("DateOpened", new Date());
/*      */     }
/*      */     
/*      */ 
/*  835 */     List<Integer> volumeAllowedTypes = determineVolumeAllowedTypes(thisEntityType);
/*  836 */     props.putIntegerListValue("AllowedRMTypes", volumeAllowedTypes);
/*      */     
/*  838 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void validateContainerDeletion()
/*      */   {
/*  848 */     Tracer.traceMethodEntry(new Object[0]);
/*  849 */     boolean ckContainerHier = true;
/*  850 */     if ((isOnHold(ckContainerHier)) || (((RecordContainer)this).isAnyChildOnHold())) {
/*  851 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_DELETE_CONTAINER_DUE_TO_HOLD_CONDITION, new Object[] { getObjectIdentity() });
/*      */     }
/*  853 */     if (getEntityType() == EntityType.RecordCategory)
/*      */     {
/*  855 */       if ((hasChildContainers()) || (hasChildRecords()))
/*  856 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_DELETE_NON_EMPTY_CONTAINER, new Object[] { getObjectIdentity() });
/*      */     }
/*  858 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */ 
/*      */   public List<RecordContainer> validateAndPrepareRecordDeclaration(String classIdent, RMProperties props, List<RMPermission> perms, List<RecordContainer> candiateContainers, List<ContentItem> associatedContent, boolean forRecordCopy)
/*      */   {
/*  882 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms, candiateContainers, associatedContent, Boolean.valueOf(forRecordCopy) });
/*  883 */     EntityType recordEntityType = getEntityTypeForClass(classIdent);
/*  884 */     Date dateNow = new Date();
/*      */     
/*      */ 
/*  887 */     List<RecordContainer> targetContainers = performDeclarationContainerValidation(recordEntityType, candiateContainers);
/*      */     
/*  889 */     if (!forRecordCopy)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  897 */       performDeclarationContentValidation(associatedContent);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  904 */     FilePlanRepository fpRepos = (FilePlanRepository)getRepository();
/*  905 */     if (fpRepos.getDataModelType() == DataModelType.DoDClassified)
/*      */     {
/*  907 */       performDeclarationDoDClassifiedValidation(recordEntityType, props);
/*  908 */       performDeclarationReceiptOfValidation(recordEntityType, props);
/*      */     }
/*      */     
/*      */ 
/*  912 */     performSupersedeValidation(props);
/*      */     
/*      */ 
/*  915 */     if (recordEntityType == EntityType.PhysicalRecord)
/*      */     {
/*      */ 
/*      */ 
/*  919 */       if ((!props.isPropertyPresent("HomeLocation")) && (props.isPropertyPresent("Location")))
/*      */       {
/*      */ 
/*  922 */         Object locationValue = props.getObjectValue("Location");
/*  923 */         props.putObjectValue("HomeLocation", locationValue);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  928 */     if (!props.isPropertyPresent("DateDeclared")) {
/*  929 */       props.putDateTimeValue("DateDeclared", dateNow);
/*      */     }
/*      */     
/*  932 */     String reviewerValue = null;
/*  933 */     if (props.isPropertyPresent("Reviewer"))
/*      */     {
/*  935 */       reviewerValue = props.getStringValue("Reviewer");
/*  936 */       if (reviewerValue != null)
/*  937 */         reviewerValue = reviewerValue.trim();
/*      */     }
/*  939 */     if ((reviewerValue == null) || (reviewerValue.length() == 0))
/*      */     {
/*  941 */       RMUser currentUser = getRepository().getDomain().fetchCurrentUser();
/*  942 */       if (currentUser != null) {
/*  943 */         props.putStringValue("Reviewer", currentUser.getShortName());
/*      */       }
/*      */     }
/*      */     
/*  947 */     String mimeTypeValue = null;
/*  948 */     if (recordEntityType == EntityType.ElectronicRecord) {
/*  949 */       mimeTypeValue = "application/x-filenet-rm-electronicrecord";
/*  950 */     } else if (recordEntityType == EntityType.EmailRecord) {
/*  951 */       mimeTypeValue = "application/x-filenet-rm-emailrecord";
/*  952 */     } else if (recordEntityType == EntityType.PhysicalRecord)
/*  953 */       mimeTypeValue = "application/x-filenet-rm-physicalrecord";
/*  954 */     props.putStringValue("MimeType", mimeTypeValue);
/*      */     
/*      */ 
/*  957 */     super.validateVitalProperties(props, false);
/*      */     
/*      */ 
/*  960 */     props.putStringValue("DocURI", "");
/*      */     
/*      */ 
/*  963 */     props.putObjectValue("SecurityFolder", targetContainers.get(0));
/*      */     
/*  965 */     Tracer.traceMethodExit(new Object[] { targetContainers });
/*  966 */     return targetContainers;
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
/*      */   private List<RecordContainer> performDeclarationContainerValidation(EntityType recordEntityType, List<RecordContainer> candidateContainers)
/*      */   {
/*  982 */     Tracer.traceMethodEntry(new Object[] { recordEntityType, candidateContainers });
/*      */     
/*  984 */     FilePlanRepository fpRepos = (FilePlanRepository)getRepository();
/*  985 */     if (candidateContainers.size() > 1)
/*      */     {
/*  987 */       if (!fpRepos.isRecordMultiFilingEnabled())
/*      */       {
/*  989 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MULTIFILE_RECORDS, new Object[] { fpRepos.getName() });
/*      */       }
/*      */     }
/*      */     
/*  993 */     List<RecordContainer> updatedTargetContainers = new ArrayList(candidateContainers.size());
/*  994 */     String commonReposIdent = fpRepos.getObjectIdentity();
/*  995 */     for (RecordContainer candidateContainer : candidateContainers)
/*      */     {
/*      */ 
/*      */ 
/*  999 */       if (!commonReposIdent.equalsIgnoreCase(candidateContainer.getRepository().getObjectIdentity()))
/*      */       {
/* 1001 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_CONTAINERS_DIFF_REPOSITORIES, new Object[0]);
/*      */       }
/*      */       
/* 1004 */       updatedTargetContainers.add(candidateContainer);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1010 */     Container container = null;
/* 1011 */     RecordVolume activeVolume = null;
/* 1012 */     for (int i = 0; i < updatedTargetContainers.size(); i++)
/*      */     {
/* 1014 */       container = (Container)updatedTargetContainers.get(i);
/* 1015 */       if (!container.canContain(recordEntityType)) {
/* 1016 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CONTAIN_CHILDTYPE, new Object[] { container.getObjectIdentity(), "Record" });
/*      */       }
/* 1018 */       if (((container instanceof RecordFolder)) && (container.canContain(EntityType.RecordVolume)))
/*      */       {
/* 1020 */         activeVolume = ((RecordFolder)container).getActiveRecordVolume();
/* 1021 */         if (activeVolume != null)
/*      */         {
/* 1023 */           if (!activeVolume.canContain(recordEntityType)) {
/* 1024 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_CONTAIN_CHILDTYPE, new Object[] { activeVolume.getObjectIdentity(), "Record" });
/*      */           }
/*      */           
/* 1027 */           updatedTargetContainers.set(i, activeVolume);
/*      */         }
/*      */         else
/*      */         {
/* 1031 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NO_ACTIVE_VOLUME_AVAILABLE, new Object[] { container.getObjectIdentity() });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1037 */     int countOfPhysicalContainers = 0;
/* 1038 */     for (RecordContainer targetContainer : updatedTargetContainers)
/*      */     {
/* 1040 */       if (((RALBaseContainer)targetContainer).isLogicallyDeleted())
/*      */       {
/* 1042 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_CONTAINER_IS_DELETED, new Object[] { targetContainer.getObjectIdentity() });
/*      */       }
/* 1044 */       if (!targetContainer.isOpen())
/*      */       {
/* 1046 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_CONTAINER_IS_CLOSED, new Object[] { targetContainer.getObjectIdentity() });
/*      */       }
/* 1048 */       if (((RALBaseContainer)targetContainer).isInactive())
/*      */       {
/* 1050 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_CONTAINER_IS_INACTIVE, new Object[] { targetContainer.getObjectIdentity() });
/*      */       }
/* 1052 */       EntityType containerEntityType = targetContainer.getEntityType();
/* 1053 */       if ((containerEntityType == EntityType.PhysicalContainer) || (containerEntityType == EntityType.HybridRecordFolder) || (containerEntityType == EntityType.PhysicalRecordFolder))
/*      */       {
/*      */ 
/*      */ 
/* 1057 */         countOfPhysicalContainers++;
/*      */       }
/*      */     }
/* 1060 */     if (countOfPhysicalContainers > 1)
/*      */     {
/* 1062 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_MULITPLE_PHYS_CONTAINERS, new Object[0]);
/*      */     }
/*      */     
/* 1065 */     Tracer.traceMethodExit(new Object[] { updatedTargetContainers });
/* 1066 */     return updatedTargetContainers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void performSupersedeValidation(RMProperties newRecordProps)
/*      */   {
/* 1076 */     Tracer.traceMethodEntry(new Object[] { newRecordProps });
/* 1077 */     if (newRecordProps.isPropertyPresent("SupercededBy"))
/*      */     {
/* 1079 */       Object supersedePropVal = null;
/*      */       try
/*      */       {
/* 1082 */         supersedePropVal = newRecordProps.getObjectValue("SupercededBy");
/*      */       }
/*      */       catch (RMRuntimeException ex)
/*      */       {
/* 1086 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.API_DECL_ERROR_SUPERSEDE_TARGET_UNAVAILABLE, new Object[0]);
/*      */       }
/*      */       
/* 1089 */       if (supersedePropVal != null)
/*      */       {
/* 1091 */         if ((supersedePropVal instanceof Record))
/*      */         {
/* 1093 */           Record supersedeTarget = (Record)supersedePropVal;
/*      */           
/*      */ 
/*      */           try
/*      */           {
/* 1098 */             supersedeTarget.refresh(new String[] { "SupercedingRecords" });
/*      */           }
/*      */           catch (RMRuntimeException ex)
/*      */           {
/* 1102 */             throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.API_DECL_ERROR_SUPERSEDE_TARGET_UNAVAILABLE, new Object[0]);
/*      */           }
/*      */           
/* 1105 */           if (supersedeTarget.isSuperseded())
/*      */           {
/*      */ 
/* 1108 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_ERROR_SUPERSEDE_TARGET_ALREADY_SUPERSEDED, new Object[0]);
/*      */           }
/*      */           
/* 1111 */           Integer accessAllowed = supersedeTarget.getAccessAllowed();
/* 1112 */           if ((accessAllowed == null) || ((accessAllowed.intValue() & RMAccessRight.Write.getIntValue()) == 0))
/*      */           {
/* 1114 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_SUPERSEDE_TARGET_CANNOT_WRITE, new Object[0]);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1119 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_SUPERSEDE_TARGET_NOT_A_RECORD, new Object[0]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1124 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   private void performDeclarationContentValidation(List<ContentItem> associatedContent)
/*      */   {
/* 1129 */     Tracer.traceMethodEntry(new Object[] { associatedContent });
/* 1130 */     ContentRepository commonContentRepos; String commonContentReposIdent; String contentItemReposIdent; if ((associatedContent != null) && (associatedContent.size() > 0))
/*      */     {
/* 1132 */       commonContentRepos = null;
/* 1133 */       commonContentReposIdent = null;
/* 1134 */       contentItemReposIdent = null;
/* 1135 */       for (ContentItem contentItem : associatedContent)
/*      */       {
/*      */ 
/* 1138 */         if (commonContentRepos != null)
/*      */         {
/* 1140 */           contentItemReposIdent = contentItem.getRepository().getObjectIdentity();
/* 1141 */           if (!commonContentReposIdent.equalsIgnoreCase(contentItemReposIdent))
/*      */           {
/* 1143 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_MULTIPLE_CONTENT_REPOSITORIES, new Object[0]);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1148 */           commonContentRepos = (ContentRepository)contentItem.getRepository();
/* 1149 */           commonContentReposIdent = commonContentRepos != null ? commonContentRepos.getObjectIdentity() : null;
/*      */         }
/*      */         
/*      */ 
/* 1153 */         if (!contentItem.isEligibleForDeclaration())
/*      */         {
/* 1155 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_INELIGIBLE_CONTENT_ITEM, new Object[] { contentItem.getObjectIdentity() });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1160 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void performDeclarationDoDClassifiedValidation(EntityType recordEntityType, RMProperties props)
/*      */   {
/* 1171 */     Tracer.traceMethodEntry(new Object[] { recordEntityType, props });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1176 */     List<RMErrorRecord> jarmErrorRecs = new ArrayList(0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1182 */     if (!isPropertyValuePresent(props, "CurrentClassification"))
/*      */     {
/* 1184 */       jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_CURRENT_CLASSIFICATION, new Object[0]));
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1189 */       String strVal = props.getStringValue("CurrentClassification");
/* 1190 */       if ("Unclassified".equalsIgnoreCase(strVal))
/*      */       {
/* 1192 */         Tracer.traceMethodExit(new Object[0]);
/* 1193 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1201 */     if (!isPropertyValuePresent(props, "InitialClassification"))
/*      */     {
/* 1203 */       jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_INITIAL_CLASSIFICATION, new Object[0]));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1210 */     if (!isPropertyValuePresent(props, "DerivedFrom"))
/*      */     {
/* 1212 */       if ((!isPropertyValuePresent(props, "ClassifiedBy")) || (!isPropertyValuePresent(props, "ReasonsForClassification")))
/*      */       {
/*      */ 
/* 1215 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_CLASSIFICATION_AUTHORITY, new Object[0]));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1223 */     boolean hasRestrictiveMarking = false;
/* 1224 */     if (isPropertyValuePresent(props, "SupplementalMarkings"))
/*      */     {
/* 1226 */       List<String> supplMarkValues = props.getStringListValue("SupplementalMarkings");
/* 1227 */       if (supplMarkValues != null)
/*      */       {
/* 1229 */         for (String supplMarkValue : supplMarkValues)
/*      */         {
/* 1231 */           if (("Formerly Restricted Data".equalsIgnoreCase(supplMarkValue)) || ("Restricted Data".equalsIgnoreCase(supplMarkValue)))
/*      */           {
/*      */ 
/* 1234 */             hasRestrictiveMarking = true;
/* 1235 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1244 */     boolean hasExemptions = isPropertyValuePresent(props, "Exemptions");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1249 */     boolean haveDeclassifyOnDate = isPropertyValuePresent(props, "DeclassifyOnDate");
/* 1250 */     boolean haveDeclassifyOnEvent = isPropertyValuePresent(props, "DeclassifyOnEvents");
/*      */     
/*      */ 
/* 1253 */     if (!hasRestrictiveMarking)
/*      */     {
/* 1255 */       if ((!hasExemptions) && (!haveDeclassifyOnDate) && (!haveDeclassifyOnEvent))
/*      */       {
/* 1257 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_DECLASSIFY_DATA, new Object[0]));
/*      */       }
/* 1259 */       else if ((!hasExemptions) && (haveDeclassifyOnDate))
/*      */       {
/*      */ 
/* 1262 */         if (isPropertyValuePresent(props, "SentOn"))
/*      */         {
/* 1264 */           Integer maxDeclassifyLimitObj = ((FilePlanRepository)getRepository()).getDoDMaxDeclassifyTimeLimit();
/* 1265 */           if (maxDeclassifyLimitObj != null)
/*      */           {
/* 1267 */             int maxDeclassifyLimit = maxDeclassifyLimitObj.intValue();
/* 1268 */             Date pubDate = props.getDateTimeValue("SentOn");
/* 1269 */             Calendar pubCalendar = Calendar.getInstance();
/* 1270 */             pubCalendar.setTime(pubDate);
/*      */             
/* 1272 */             int maxYear = pubCalendar.get(1) + maxDeclassifyLimit;
/* 1273 */             Calendar maxCalendar = (Calendar)pubCalendar.clone();
/* 1274 */             maxCalendar.set(1, maxYear);
/*      */             
/* 1276 */             Date declassifyOnDate = props.getDateTimeValue("DeclassifyOnDate");
/* 1277 */             Calendar declassifyOnCalendar = Calendar.getInstance();
/* 1278 */             declassifyOnCalendar.setTime(declassifyOnDate);
/*      */             
/* 1280 */             if (declassifyOnCalendar.after(maxCalendar))
/*      */             {
/* 1282 */               jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_DECLASS_DATE_OUT_OF_RANGE, new Object[0]));
/*      */             }
/*      */             
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1291 */       if ((haveDeclassifyOnDate) || (haveDeclassifyOnEvent))
/*      */       {
/* 1293 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_DECLASSIFY_DATA_RESTRICTED, new Object[0]));
/*      */       }
/*      */       
/* 1296 */       if (hasExemptions)
/*      */       {
/* 1298 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_EXEMPTION_DATA_RESTRICTED, new Object[0]));
/*      */       }
/*      */       
/* 1301 */       boolean haveDowngradeOnInfo = (isPropertyValuePresent(props, "DowngradeOnDate")) || (isPropertyValuePresent(props, "DowngradeOnEvents"));
/*      */       
/* 1303 */       if (haveDowngradeOnInfo)
/*      */       {
/* 1305 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_DOWNGRADE_DATA_RESTRICTED, new Object[0]));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1313 */     if (!isPropertyValuePresent(props, "ClassifyingAgency"))
/*      */     {
/* 1315 */       jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_CLASSIFYING_AGENCY, new Object[0]));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1323 */     if ((isPropertyValuePresent(props, "DowngradeOnDate")) || (isPropertyValuePresent(props, "DowngradeOnEvents")))
/*      */     {
/*      */ 
/* 1326 */       if (!isPropertyValuePresent(props, "DowngradeInstructions"))
/*      */       {
/* 1328 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_DOWNGRADE_INSTRUCTIONS, new Object[0]));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1336 */     if (isPropertyValuePresent(props, "UpgradedOn"))
/*      */     {
/* 1338 */       if (!isPropertyValuePresent(props, "ReasonsForUpgrade"))
/*      */       {
/* 1340 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_REASONS_FOR_UPGRADE, new Object[0]));
/*      */       }
/*      */       
/* 1343 */       if (!isPropertyValuePresent(props, "UpgradedBy"))
/*      */       {
/* 1345 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_UPGRADED_BY, new Object[0]));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1353 */     if (isPropertyValuePresent(props, "SecurityClassificationReviewedOn"))
/*      */     {
/* 1355 */       if (!isPropertyValuePresent(props, "SecurityClassificationReviewedBy"))
/*      */       {
/* 1357 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_REVIEWED_BY, new Object[0]));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1365 */     if (isPropertyValuePresent(props, "DowngradedOn"))
/*      */     {
/* 1367 */       if (!isPropertyValuePresent(props, "DowngradedBy"))
/*      */       {
/* 1369 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_DOWNGRADED_BY, new Object[0]));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1377 */     if (isPropertyValuePresent(props, "DeclassifiedOn"))
/*      */     {
/* 1379 */       if (!isPropertyValuePresent(props, "DeclassifiedBy"))
/*      */       {
/* 1381 */         jarmErrorRecs.add(new RALGenericErrorRecord(RMErrorCode.API_DOD_CLASS_DECL_ERROR_INVALID_DECLASSIFIED_BY, new Object[0]));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1388 */     if (jarmErrorRecs.size() > 0)
/*      */     {
/* 1390 */       RMErrorRecord[] jarmErrorRecsArray = (RMErrorRecord[])jarmErrorRecs.toArray(new RMErrorRecord[jarmErrorRecs.size()]);
/* 1391 */       RMErrorStack jarmErrorStack = new RALGenericErrorStack(jarmErrorRecsArray);
/*      */       
/* 1393 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DOD_CLASS_DECL_ONE_OR_MORE_VALIDATION_ERRORS, jarmErrorStack, new Object[0]);
/*      */     }
/*      */     
/* 1396 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   private boolean isPropertyValuePresent(RMProperties props, String symbolicName)
/*      */   {
/* 1413 */     Tracer.traceMethodEntry(new Object[] { props, symbolicName });
/* 1414 */     boolean result = false;
/* 1415 */     if (props.isPropertyPresent(symbolicName))
/*      */     {
/* 1417 */       RMProperty prop = props.get(symbolicName);
/* 1418 */       Object rawValue = prop.getObjectValue();
/* 1419 */       if (rawValue != null)
/*      */       {
/* 1421 */         result = true;
/* 1422 */         if ((rawValue instanceof String))
/*      */         {
/* 1424 */           if (((String)rawValue).trim().length() == 0) {
/* 1425 */             result = false;
/*      */           }
/* 1427 */         } else if ((rawValue instanceof List))
/*      */         {
/* 1429 */           if (((List)rawValue).size() == 0) {
/* 1430 */             result = false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1435 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1436 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void performDeclarationReceiptOfValidation(EntityType recordEntityType, RMProperties props)
/*      */   {
/* 1448 */     Tracer.traceMethodEntry(new Object[] { recordEntityType, props });
/* 1449 */     if (props.isPropertyPresent("ReceiptOf"))
/*      */     {
/* 1451 */       int newRecReceiptStatus = RMReceiptStatus.None.getIntValue();
/* 1452 */       Object rawValue = props.getObjectValue("ReceiptOf");
/* 1453 */       if ((rawValue != null) && ((rawValue instanceof Record)))
/*      */       {
/* 1455 */         newRecReceiptStatus = RMReceiptStatus.Attached.getIntValue();
/* 1456 */         Record receiptParent = (Record)rawValue;
/*      */         
/* 1458 */         if (receiptParent.getReceiptParent() != null)
/*      */         {
/* 1460 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DECL_ERROR_RECEIPTOF_TARGET_INELIGIBLE, new Object[0]);
/*      */         }
/*      */         
/*      */ 
/* 1464 */         props.add("ReceiptStatus", DataType.Integer, RMCardinality.Single, Integer.valueOf(newRecReceiptStatus));
/*      */       }
/*      */     }
/* 1467 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOpen()
/*      */   {
/* 1508 */     Tracer.traceMethodEntry(new Object[0]);
/* 1509 */     boolean result = true;
/*      */     
/*      */ 
/* 1512 */     Date dateClosed = getProperties().getDateTimeValue("DateClosed");
/* 1513 */     if (dateClosed != null)
/*      */     {
/* 1515 */       Date dateReopened = getProperties().getDateTimeValue("ReOpenedDate");
/* 1516 */       result = dateReopened != null;
/*      */     }
/*      */     
/* 1519 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1520 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isClosed()
/*      */   {
/* 1528 */     Tracer.traceMethodEntry(new Object[0]);
/* 1529 */     boolean result = false;
/* 1530 */     if (getProperties().isPropertyPresent("DateClosed"))
/*      */     {
/* 1532 */       Date dateClosed = getProperties().getDateTimeValue("DateClosed");
/* 1533 */       if (dateClosed != null)
/*      */       {
/* 1535 */         if (getProperties().isPropertyPresent("ReOpenedDate"))
/*      */         {
/* 1537 */           Date dateReopened = getProperties().getDateTimeValue("ReOpenedDate");
/* 1538 */           result = dateReopened == null;
/*      */         }
/*      */         else {
/* 1541 */           result = true;
/*      */         } }
/*      */     }
/* 1544 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1545 */     return result;
/*      */   }
/*      */   
/*      */   protected boolean isAnyParentClosed()
/*      */   {
/* 1550 */     Tracer.traceMethodEntry(new Object[0]);
/* 1551 */     boolean result = false;
/*      */     
/* 1553 */     Container parent = getParent();
/*      */     
/* 1555 */     while ((parent != null) && (parent.getEntityType() != EntityType.FilePlan))
/*      */     {
/* 1557 */       RecordContainer parentRecordContainer = (RecordContainer)parent;
/* 1558 */       if (!parentRecordContainer.isOpen())
/*      */       {
/* 1560 */         result = true;
/* 1561 */         break;
/*      */       }
/* 1563 */       parent = parentRecordContainer.getParent();
/*      */     }
/*      */     
/* 1566 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1567 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isReopened()
/*      */   {
/* 1576 */     Tracer.traceMethodEntry(new Object[0]);
/* 1577 */     Date dateReopened = getProperties().getDateTimeValue("ReOpenedDate");
/* 1578 */     boolean result = dateReopened != null;
/*      */     
/* 1580 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1581 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void reopen()
/*      */   {
/* 1589 */     Tracer.traceMethodEntry(new Object[0]);
/* 1590 */     RMProperties jarmProps = getProperties();
/* 1591 */     if (jarmProps != null)
/*      */     {
/*      */ 
/* 1594 */       if ((!jarmProps.isPropertyPresent("DateClosed")) || (jarmProps.getDateTimeValue("DateClosed") == null))
/*      */       {
/* 1596 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_REOPEN_CONTAINER_NOT_CLOSED, new Object[0]);
/*      */       }
/*      */       
/* 1599 */       if ((jarmProps.isPropertyPresent("ReOpenedDate")) && (jarmProps.getDateTimeValue("ReOpenedDate") != null))
/*      */       {
/* 1601 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_REOPEN_CONTAINER_ALREADY_REOPENED, new Object[0]);
/*      */       }
/*      */       
/* 1604 */       if (isAnyParentClosed())
/*      */       {
/* 1606 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_REOPEN_CONTAINER_PARENT_CONTANER_IS_CLOSED, new Object[0]);
/*      */       }
/* 1608 */       jarmProps.putDateTimeValue("ReOpenedDate", new Date());
/* 1609 */       RMUser currentUser = getRepository().getDomain().fetchCurrentUser();
/* 1610 */       if (currentUser != null)
/* 1611 */         jarmProps.putStringValue("ReOpenedBy", currentUser.getShortName());
/* 1612 */       save(RMRefreshMode.Refresh);
/*      */     }
/* 1614 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public boolean isOnHold(boolean checkContainerHierarchy)
/*      */   {
/* 1634 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(checkContainerHierarchy) });
/* 1635 */     boolean result = false;
/*      */     
/* 1637 */     RMProperties jarmProps = getProperties();
/* 1638 */     if (jarmProps != null)
/*      */     {
/* 1640 */       Boolean boolObj = jarmProps.getBooleanValue("OnHold");
/* 1641 */       if (boolObj != null) {
/* 1642 */         result = boolObj.booleanValue();
/*      */       }
/*      */     }
/* 1645 */     if ((!result) && (checkContainerHierarchy))
/*      */     {
/* 1647 */       result = isAnyParentOnHold();
/*      */     }
/*      */     
/* 1650 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1651 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyParentOnHold()
/*      */   {
/* 1662 */     Tracer.traceMethodEntry(new Object[0]);
/* 1663 */     boolean result = false;
/*      */     
/* 1665 */     Container parent = getParent();
/* 1666 */     if ((parent != null) && (parent.getEntityType() != EntityType.FilePlan))
/*      */     {
/* 1668 */       result = (((RecordContainer)parent).isOnHold(false)) || (((RecordContainer)parent).isAnyParentOnHold());
/*      */     }
/*      */     
/*      */ 
/* 1672 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1673 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isInactive()
/*      */   {
/* 1681 */     Tracer.traceMethodEntry(new Object[0]);
/* 1682 */     boolean result = false;
/* 1683 */     Container containerOfInterest = this;
/* 1684 */     if (getEntityType() == EntityType.RecordVolume)
/*      */     {
/* 1686 */       containerOfInterest = getParent();
/*      */     }
/*      */     
/* 1689 */     if (containerOfInterest != null)
/*      */     {
/* 1691 */       if (containerOfInterest.getProperties().isPropertyPresent("Inactive"))
/*      */       {
/* 1693 */         Boolean propValue = containerOfInterest.getProperties().getBooleanValue("Inactive");
/* 1694 */         if (propValue != null) {
/* 1695 */           result = propValue.booleanValue();
/*      */         }
/*      */       }
/*      */     }
/* 1699 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1700 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setActive()
/*      */   {
/* 1708 */     Tracer.traceMethodEntry(new Object[0]);
/* 1709 */     Boolean propValue = getProperties().getBooleanValue("Inactive");
/*      */     
/* 1711 */     if ((propValue == null) || (!propValue.booleanValue()))
/*      */     {
/* 1713 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_ALREADY_ACTIVE, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/* 1717 */     if (isAnyParentInactive())
/*      */     {
/* 1719 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PARENT_CONTAINER_IS_INACTIVE, new Object[0]);
/*      */     }
/*      */     
/* 1722 */     getProperties().putBooleanValue("Inactive", Boolean.valueOf(false));
/* 1723 */     save(RMRefreshMode.Refresh);
/*      */     
/* 1725 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyParentInactive()
/*      */   {
/* 1736 */     Tracer.traceMethodEntry(new Object[0]);
/* 1737 */     boolean result = false;
/* 1738 */     Container curParent = getParent();
/* 1739 */     while ((curParent != null) && (curParent.getEntityType() != EntityType.FilePlan))
/*      */     {
/* 1741 */       if (curParent.getProperties().isPropertyPresent("Inactive"))
/*      */       {
/* 1743 */         Boolean propValue = curParent.getProperties().getBooleanValue("Inactive");
/* 1744 */         if ((propValue != null) && (propValue.booleanValue() == true))
/*      */         {
/* 1746 */           result = true;
/* 1747 */           break;
/*      */         }
/*      */       }
/* 1750 */       curParent = curParent.getParent();
/*      */     }
/* 1752 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1753 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setInactive(String reasonForInactive)
/*      */   {
/* 1761 */     Tracer.traceMethodEntry(new Object[] { reasonForInactive });
/* 1762 */     if ((reasonForInactive == null) || (reasonForInactive.trim().length() == 0))
/*      */     {
/* 1764 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_REASON_FOR_INACTIVE_IS_REQUIRED, new Object[0]);
/*      */     }
/*      */     
/* 1767 */     RMProperties props = getProperties();
/* 1768 */     if (props.isPropertyPresent("Inactive"))
/*      */     {
/* 1770 */       Boolean propValue = props.getBooleanValue("Inactive");
/*      */       
/* 1772 */       if ((propValue != null) && (propValue.booleanValue() == true))
/*      */       {
/* 1774 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CONTAINER_ALREADY_INACTIVE, new Object[0]);
/*      */       }
/*      */     }
/*      */     
/* 1778 */     props.putBooleanValue("Inactive", Boolean.valueOf(true));
/* 1779 */     props.putStringValue("ReasonForInactivate", reasonForInactive);
/* 1780 */     save(RMRefreshMode.Refresh);
/*      */     
/*      */ 
/* 1783 */     setSubContainersInactive(this, reasonForInactive);
/*      */     
/* 1785 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSubContainersInactive(Container theContainer, String reasonForInactive)
/*      */   {
/* 1797 */     Tracer.traceMethodEntry(new Object[] { theContainer, reasonForInactive });
/* 1798 */     StringBuilder sb = new StringBuilder();
/* 1799 */     sb.append("Select [");
/* 1800 */     sb.append("Id");
/* 1801 */     sb.append("] from [RMFolder] WHERE ((this insubfolder '");
/* 1802 */     sb.append(theContainer.getObjectIdentity());
/* 1803 */     sb.append("') AND [RMEntityType] <> ");
/* 1804 */     sb.append(EntityType.RecordVolume.getIntValue());
/* 1805 */     sb.append(" AND [IsDeleted] = FALSE AND [Inactive] <> TRUE)");
/*      */     
/* 1807 */     RMSearch objSearch = new RMSearch(getRepository());
/* 1808 */     PageableSet<Container> searchResults = objSearch.fetchObjects(sb.toString(), EntityType.Container, null, null, Boolean.valueOf(true));
/* 1809 */     if (searchResults != null)
/*      */     {
/* 1811 */       RMPageIterator<Container> pi = searchResults.pageIterator();
/* 1812 */       if (pi != null)
/*      */       {
/* 1814 */         while (pi.nextPage())
/*      */         {
/* 1816 */           List<Container> containerList = pi.getCurrentPage();
/* 1817 */           for (Container container : containerList)
/*      */           {
/* 1819 */             container.getProperties().putBooleanValue("Inactive", Boolean.valueOf(true));
/* 1820 */             container.getProperties().putStringValue("ReasonForInactivate", reasonForInactive);
/* 1821 */             container.save(RMRefreshMode.Refresh);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1826 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public void synchronizeBaseFolderName(String containerNameProperty, RMProperties baseProps)
/*      */   {
/* 1846 */     Tracer.traceMethodEntry(new Object[] { containerNameProperty, baseProps });
/* 1847 */     if ((baseProps != null) && (baseProps.isPropertyPresent(containerNameProperty)))
/*      */     {
/* 1849 */       String filePlanName = baseProps.getStringValue(containerNameProperty);
/* 1850 */       baseProps.putStringValue("FolderName", filePlanName);
/*      */     }
/* 1852 */     else if ((baseProps != null) && (baseProps.isPropertyPresent("FolderName")))
/*      */     {
/* 1854 */       String folderName = baseProps.getStringValue("FolderName");
/* 1855 */       baseProps.putStringValue(containerNameProperty, folderName);
/*      */     }
/* 1857 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   protected void initializeDispositionProperties(RMProperties props, Date allocationDate)
/*      */   {
/* 1875 */     Tracer.traceMethodEntry(new Object[] { props, allocationDate });
/* 1876 */     if ((props.isPropertyPresent("DisposalSchedule")) && (props.getObjectValue("DisposalSchedule") != null))
/*      */     {
/*      */ 
/* 1879 */       props.putDateTimeValue("DisposalScheduleAllocationDate", allocationDate);
/*      */     }
/* 1881 */     super.resetDispositionData(props);
/* 1882 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   public boolean isLogicallyDeleted()
/*      */   {
/* 1887 */     Tracer.traceMethodEntry(new Object[0]);
/* 1888 */     boolean result = false;
/* 1889 */     if (getProperties().isPropertyPresent("IsDeleted"))
/*      */     {
/* 1891 */       Boolean propValue = getProperties().getBooleanValue("IsDeleted");
/* 1892 */       if (propValue != null) {
/* 1893 */         result = propValue.booleanValue();
/*      */       }
/*      */     }
/* 1896 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1897 */     return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RecordContainer validateRecordFiling_part1()
/*      */   {
/* 1932 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*      */ 
/* 1935 */     if (isInactive())
/*      */     {
/* 1937 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_INTO_INACTIVE_CONTAINER, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/* 1941 */     if ((isClosed()) && (!userCanFileIntoClosedContainer((FilePlanRepository)getRepository())))
/*      */     {
/* 1943 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_INTO_CLOSED_CONTAINER, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1948 */     RecordContainer actualTargetContainer = (RecordContainer)this;
/* 1949 */     if (requiresChildVolume())
/*      */     {
/* 1951 */       actualTargetContainer = ((RecordVolumeContainer)this).getActiveRecordVolume();
/* 1952 */       if (actualTargetContainer == null)
/*      */       {
/* 1954 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NO_ACTIVE_VOLUME_AVAILABLE, new Object[] { getObjectIdentity() });
/*      */       }
/*      */     }
/*      */     
/* 1958 */     Tracer.traceMethodExit(new Object[] { actualTargetContainer });
/* 1959 */     return actualTargetContainer;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Container> validateRecordFiling_part2(RecordContainer targetContainer, Record record, String containerIdentToIgnore, boolean forFileOperation)
/*      */   {
/* 1993 */     Tracer.traceMethodEntry(new Object[] { targetContainer, record, containerIdentToIgnore, Boolean.valueOf(forFileOperation) });
/*      */     
/* 1995 */     RecordContainer actualTargetContainer = targetContainer;
/* 1996 */     if (actualTargetContainer == null) {
/* 1997 */       actualTargetContainer = (RecordContainer)this;
/*      */     }
/*      */     
/* 2000 */     EntityType recordType = record.getEntityType();
/* 2001 */     if (!actualTargetContainer.canContain(recordType))
/*      */     {
/* 2003 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_SPECIFIC_RECORD_TYPE, new Object[] { recordType });
/*      */     }
/*      */     
/* 2006 */     List<Container> existingRecordContainers = record.getContainedBy();
/* 2007 */     if (existingRecordContainers != null)
/*      */     {
/*      */ 
/* 2010 */       for (Container existingContainer : existingRecordContainers)
/*      */       {
/* 2012 */         if (actualTargetContainer.getObjectIdentity().equalsIgnoreCase(existingContainer.getObjectIdentity()))
/*      */         {
/* 2014 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_RECORD_IT_ALREADY_EXISTS_IN_CONTAINER, new Object[0]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2020 */       RecordContainer existingContainter = (RecordContainer)existingRecordContainers.get(0);
/* 2021 */       FilePlan existingRecordFilePlan = existingContainter.getFilePlan();
/* 2022 */       if (!getFilePlan().getObjectIdentity().equalsIgnoreCase(existingRecordFilePlan.getObjectIdentity()))
/*      */       {
/* 2024 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_RECORD_INTO_EXTERNAL_FILEPLAN, new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2029 */       if ((forFileOperation) && ((existingContainter instanceof DefensiblyDisposable)) && (existingContainter.isADefensiblyDisposableContainer()))
/*      */       {
/*      */ 
/* 2032 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_MULTIFILE_DD_RECORD, new Object[] { record.getObjectIdentity() });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2038 */       if ((recordType == EntityType.PhysicalRecord) && (isPhysicalRecordContainer()))
/*      */       {
/* 2040 */         for (Container existingContainer : existingRecordContainers)
/*      */         {
/* 2042 */           if ((containerIdentToIgnore == null) || (!containerIdentToIgnore.equalsIgnoreCase(existingContainer.getObjectIdentity())))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2048 */             if (((RecordContainer)existingContainer).isPhysicalRecordContainer())
/*      */             {
/* 2050 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_FILE_PHYSRECORD_INTO_MULTIPLE_PHYS_CONTAINERS, new Object[0]);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2056 */     Tracer.traceMethodExit(new Object[] { existingRecordContainers });
/* 2057 */     return existingRecordContainers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isRetainMetadataEnabled()
/*      */   {
/* 2068 */     Tracer.traceMethodEntry(new Object[0]);
/* 2069 */     RetainMetadata setting = getFilePlan().getRetainMetadata();
/* 2070 */     boolean result = setting != RetainMetadata.NeverRetain;
/*      */     
/* 2072 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2073 */     return result;
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
/*      */   protected String determineVolumeContainerType(EntityType parentEntityType)
/*      */   {
/* 2086 */     Tracer.traceMethodEntry(new Object[] { parentEntityType });
/* 2087 */     String volContainerType = "";
/* 2088 */     if (parentEntityType == EntityType.ElectronicRecordFolder) {
/* 2089 */       volContainerType = "application/x-filenet-rm-volumeelectronic";
/* 2090 */     } else if (parentEntityType == EntityType.HybridRecordFolder) {
/* 2091 */       volContainerType = "application/x-filenet-rm-volumehybrid";
/* 2092 */     } else if (parentEntityType == EntityType.PhysicalRecordFolder) {
/* 2093 */       volContainerType = "application/x-filenet-rm-volumephysical";
/*      */     }
/* 2095 */     Tracer.traceMethodExit(new Object[] { volContainerType });
/* 2096 */     return volContainerType;
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
/*      */   protected List<Integer> determineVolumeAllowedTypes(EntityType parentEntityType)
/*      */   {
/* 2109 */     Tracer.traceMethodEntry(new Object[] { parentEntityType });
/* 2110 */     List<Integer> allowedTypes = new ArrayList();
/* 2111 */     if (EntityType.PhysicalRecordFolder == parentEntityType)
/*      */     {
/* 2113 */       allowedTypes.add(Integer.valueOf(EntityType.PhysicalRecord.getIntValue()));
/*      */     }
/* 2115 */     else if ((EntityType.HybridRecordFolder == parentEntityType) || (EntityType.ElectronicRecordFolder == parentEntityType))
/*      */     {
/*      */ 
/* 2118 */       allowedTypes.add(Integer.valueOf(EntityType.ElectronicRecord.getIntValue()));
/* 2119 */       allowedTypes.add(Integer.valueOf(EntityType.EmailRecord.getIntValue()));
/* 2120 */       allowedTypes.add(Integer.valueOf(EntityType.PhysicalRecord.getIntValue()));
/*      */     }
/*      */     
/* 2123 */     Tracer.traceMethodEntry(new Object[] { allowedTypes });
/* 2124 */     return allowedTypes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected EntityType getEntityTypeForClass(String classIdent)
/*      */   {
/* 2136 */     Tracer.traceMethodEntry(new Object[] { classIdent });
/* 2137 */     EntityType result = null;
/*      */     
/* 2139 */     RMClassDescription classDesc = getClassDescription(getRepository(), classIdent);
/* 2140 */     if (classDesc != null)
/*      */     {
/* 2142 */       RMPropertyDescriptionInteger entityTypePD = (RMPropertyDescriptionInteger)classDesc.getPropertyDescription("RMEntityType");
/*      */       
/* 2144 */       if (entityTypePD != null)
/*      */       {
/* 2146 */         Integer defaultValue = entityTypePD.getIntegerDefaultValue();
/* 2147 */         if (defaultValue != null) {
/* 2148 */           result = EntityType.getInstanceFromInt(defaultValue.intValue());
/*      */         }
/*      */       }
/*      */     }
/* 2152 */     Tracer.traceMethodExit(new Object[] { result });
/* 2153 */     return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void assignDispositionSchedule(DispositionSchedule newSchedule, SchedulePropagation propagationMode)
/*      */   {
/* 2198 */     Tracer.traceMethodEntry(new Object[] { newSchedule, propagationMode });
/* 2199 */     Util.ckNullObjParam("newSchedule", newSchedule);
/* 2200 */     Util.ckNullObjParam("propagationMode", propagationMode);
/*      */     
/* 2202 */     DispositionSchedule existingSchedule = getAssignedSchedule();
/* 2203 */     if ((existingSchedule != null) && (existingSchedule.getObjectIdentity().equalsIgnoreCase(newSchedule.getObjectIdentity())))
/*      */     {
/*      */ 
/* 2206 */       return;
/*      */     }
/*      */     
/* 2209 */     RMProperties props = getProperties();
/* 2210 */     props.putObjectValue("DisposalSchedule", newSchedule);
/* 2211 */     Date newAllocationDate = new Date();
/* 2212 */     initializeDispositionProperties(props, newAllocationDate);
/* 2213 */     props.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(5));
/* 2214 */     internalSave(RMRefreshMode.Refresh);
/*      */     
/*      */ 
/* 2217 */     PageableSet<Container> inheritingSubcontainers = null;
/* 2218 */     PageableSet<Container> immediateSubcontainers = null;
/* 2219 */     RMPageIterator<Container> pi = null;
/* 2220 */     Integer pageSize = null;
/*      */     
/* 2222 */     switch (propagationMode)
/*      */     {
/*      */ 
/*      */     case ToAllInheritors: 
/* 2226 */       inheritingSubcontainers = getScheduleInheritingSubcontainers();
/* 2227 */       pi = inheritingSubcontainers.pageIterator();
/* 2228 */     case NoPropagation: case ToImmediateSubContainersAndAllInheritors: case ForceInheritanceUponNonAssignedSubContainers:  while (pi.nextPage())
/*      */       {
/* 2230 */         List<Container> currPage = pi.getCurrentPage();
/* 2231 */         for (Container subContainer : currPage)
/*      */         {
/* 2233 */           if (!((DefensiblyDisposable)subContainer).isADefensiblyDisposableContainer())
/*      */           {
/* 2235 */             ((DispositionAllocatable)subContainer).assignDispositionSchedule(newSchedule, propagationMode);
/*      */           }
/*      */         }
/* 2238 */         continue;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2244 */         inheritingSubcontainers = getScheduleInheritingSubcontainers();
/* 2245 */         pi = inheritingSubcontainers.pageIterator();
/* 2246 */         while (pi.nextPage())
/*      */         {
/* 2248 */           List<Container> currPage = pi.getCurrentPage();
/* 2249 */           for (Container subContainer : currPage)
/*      */           {
/* 2251 */             subContainer.getProperties().putGuidValue("DisposalScheduleInheritedFrom", null);
/* 2252 */             subContainer.save(RMRefreshMode.NoRefresh);
/*      */           }
/* 2254 */           continue;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2259 */           inheritingSubcontainers = getScheduleInheritingSubcontainers();
/* 2260 */           pi = inheritingSubcontainers.pageIterator();
/* 2261 */           while (pi.nextPage())
/*      */           {
/* 2263 */             List<Container> currPage = pi.getCurrentPage();
/* 2264 */             for (Container subContainer : currPage)
/*      */             {
/* 2266 */               if (!((DefensiblyDisposable)subContainer).isADefensiblyDisposableContainer())
/*      */               {
/* 2268 */                 ((DispositionAllocatable)subContainer).assignDispositionSchedule(newSchedule, SchedulePropagation.ToAllInheritors);
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2275 */           immediateSubcontainers = getImmediateSubContainers(pageSize);
/* 2276 */           pi = immediateSubcontainers.pageIterator();
/* 2277 */           while (pi.nextPage())
/*      */           {
/* 2279 */             List<Container> currPage = pi.getCurrentPage();
/* 2280 */             for (Container subContainer : currPage)
/*      */             {
/* 2282 */               if (!((DefensiblyDisposable)subContainer).isADefensiblyDisposableContainer())
/*      */               {
/* 2284 */                 ((DispositionAllocatable)subContainer).assignDispositionSchedule(newSchedule, SchedulePropagation.ToAllInheritors);
/*      */               }
/*      */             }
/* 2287 */             continue;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2294 */             String inheritorIdent = getObjectIdentity();
/* 2295 */             Boolean ckIfExistingScheduleIsNull = Boolean.TRUE;
/* 2296 */             PageableSet<Container> nonInheritingSubcontainers = getNonScheduleInheritingSubcontainers(ckIfExistingScheduleIsNull);
/*      */             
/* 2298 */             pi = nonInheritingSubcontainers.pageIterator();
/* 2299 */             while (pi.nextPage())
/*      */             {
/* 2301 */               List<Container> currPage = pi.getCurrentPage();
/* 2302 */               for (Container subContainer : currPage)
/*      */               {
/* 2304 */                 if (!((DefensiblyDisposable)subContainer).isADefensiblyDisposableContainer())
/*      */                 {
/* 2306 */                   subContainer.getProperties().putGuidValue("DisposalScheduleInheritedFrom", inheritorIdent);
/* 2307 */                   subContainer.save(RMRefreshMode.Refresh);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2314 */             inheritingSubcontainers = getScheduleInheritingSubcontainers();
/* 2315 */             pi = inheritingSubcontainers.pageIterator();
/* 2316 */             while (pi.nextPage())
/*      */             {
/* 2318 */               List<Container> currPage = pi.getCurrentPage();
/* 2319 */               for (Container subContainer : currPage)
/*      */               {
/* 2321 */                 if (!((DefensiblyDisposable)subContainer).isADefensiblyDisposableContainer())
/*      */                 {
/* 2323 */                   ((DispositionAllocatable)subContainer).assignDispositionSchedule(newSchedule, SchedulePropagation.ToAllInheritors); } }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2330 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearDispositionAssignment(SchedulePropagation propagationMode)
/*      */   {
/* 2338 */     Tracer.traceMethodEntry(new Object[] { propagationMode });
/* 2339 */     Util.ckNullObjParam("propagationMode", propagationMode);
/*      */     
/* 2341 */     DispositionSchedule existingSchedule = getAssignedSchedule();
/* 2342 */     if (existingSchedule != null)
/*      */     {
/* 2344 */       RMProperties props = getProperties();
/* 2345 */       props.putObjectValue("DisposalSchedule", null);
/* 2346 */       Date newAllocationDate = null;
/* 2347 */       initializeDispositionProperties(props, newAllocationDate);
/* 2348 */       props.putIntegerValue("RecalculatePhaseRetention", Integer.valueOf(5));
/* 2349 */       save(RMRefreshMode.Refresh);
/*      */       
/*      */ 
/* 2352 */       PageableSet<Container> inheritingSubcontainers = null;
/* 2353 */       PageableSet<Container> immediateSubcontainers = null;
/* 2354 */       RMPageIterator<Container> pi = null;
/* 2355 */       switch (propagationMode)
/*      */       {
/*      */ 
/*      */       case ToAllInheritors: 
/* 2359 */         inheritingSubcontainers = getScheduleInheritingSubcontainers();
/* 2360 */         pi = inheritingSubcontainers.pageIterator();
/* 2361 */       case NoPropagation: case ToImmediateSubContainersAndAllInheritors: case ForceInheritanceUponNonAssignedSubContainers:  while (pi.nextPage())
/*      */         {
/* 2363 */           List<Container> currPage = pi.getCurrentPage();
/* 2364 */           for (Container subContainer : currPage)
/*      */           {
/* 2366 */             ((DispositionAllocatable)subContainer).clearDispositionAssignment(propagationMode);
/*      */           }
/* 2368 */           continue;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2374 */           inheritingSubcontainers = getScheduleInheritingSubcontainers();
/* 2375 */           pi = inheritingSubcontainers.pageIterator();
/* 2376 */           while (pi.nextPage())
/*      */           {
/* 2378 */             List<Container> currPage = pi.getCurrentPage();
/* 2379 */             for (Container subContainer : currPage)
/*      */             {
/* 2381 */               subContainer.getProperties().putGuidValue("DisposalScheduleInheritedFrom", null);
/* 2382 */               subContainer.save(RMRefreshMode.NoRefresh);
/*      */             }
/* 2384 */             continue;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2389 */             inheritingSubcontainers = getScheduleInheritingSubcontainers();
/* 2390 */             pi = inheritingSubcontainers.pageIterator();
/* 2391 */             while (pi.nextPage())
/*      */             {
/* 2393 */               List<Container> currPage = pi.getCurrentPage();
/* 2394 */               for (Container subContainer : currPage)
/*      */               {
/* 2396 */                 ((DispositionAllocatable)subContainer).clearDispositionAssignment(SchedulePropagation.ToAllInheritors);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2402 */             Integer pageSize = null;
/* 2403 */             immediateSubcontainers = getImmediateSubContainers(pageSize);
/* 2404 */             pi = immediateSubcontainers.pageIterator();
/* 2405 */             while (pi.nextPage())
/*      */             {
/* 2407 */               List<Container> currPage = pi.getCurrentPage();
/* 2408 */               for (Container subContainer : currPage)
/*      */               {
/* 2410 */                 ((DispositionAllocatable)subContainer).clearDispositionAssignment(SchedulePropagation.ToAllInheritors);
/*      */               }
/* 2412 */               continue;
/*      */               
/*      */ 
/*      */ 
/* 2416 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_UNSUPPORTED_DISP_SCHEDULE_CLEAR_MODE, new Object[] { SchedulePropagation.ForceInheritanceUponNonAssignedSubContainers });
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 2424 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public boolean requiresChildVolume()
/*      */   {
/* 2437 */     Tracer.traceMethodEntry(new Object[0]);
/* 2438 */     EntityType containerType = getEntityType();
/* 2439 */     boolean result = (containerType == EntityType.ElectronicRecordFolder) || (containerType == EntityType.PhysicalRecordFolder) || (containerType == EntityType.HybridRecordFolder);
/*      */     
/*      */ 
/*      */ 
/* 2443 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 2444 */     return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Container validateRecordUnfileability(Record record)
/*      */   {
/* 2471 */     Tracer.traceMethodEntry(new Object[] { record });
/* 2472 */     Container newSecurityFolder = null;
/* 2473 */     String thisContainerIdent = getObjectIdentity();
/* 2474 */     String recordIdent = record.getObjectIdentity();
/*      */     
/*      */ 
/* 2477 */     List<Container> currentRecContainers = record.getContainedBy();
/* 2478 */     boolean sawThisContainer = false;
/* 2479 */     for (Container currentRecContainer : currentRecContainers)
/*      */     {
/* 2481 */       if (thisContainerIdent.equalsIgnoreCase(currentRecContainer.getObjectIdentity()))
/*      */       {
/* 2483 */         sawThisContainer = true;
/* 2484 */         break;
/*      */       }
/*      */     }
/* 2487 */     if (!sawThisContainer)
/*      */     {
/* 2489 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_UNFILE_RECORD_FROM_NONCONTAINING_CONTAINER, new Object[] { recordIdent, thisContainerIdent });
/*      */     }
/*      */     
/*      */ 
/* 2493 */     if (currentRecContainers.size() <= 1)
/*      */     {
/* 2495 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_UNFILE_SINGLE_FILED_RECORD, new Object[] { recordIdent });
/*      */     }
/*      */     
/* 2498 */     Container currentSecFolder = record.getSecurityFolder();
/* 2499 */     if (thisContainerIdent.equalsIgnoreCase(currentSecFolder.getObjectIdentity()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2504 */       for (Container existingRecContainer : currentRecContainers)
/*      */       {
/* 2506 */         if (!thisContainerIdent.equalsIgnoreCase(existingRecContainer.getObjectIdentity()))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2512 */           newSecurityFolder = existingRecContainer;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2518 */     Tracer.traceMethodExit(new Object[] { newSecurityFolder });
/* 2519 */     return newSecurityFolder;
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
/*      */   private PageableSet<Container> getScheduleInheritingSubcontainers()
/*      */   {
/* 2532 */     Tracer.traceMethodEntry(new Object[0]);
/* 2533 */     String containerId = getObjectIdentity();
/*      */     
/* 2535 */     StringBuilder sb = new StringBuilder();
/* 2536 */     sb.append("SELECT rf.[").append("Id").append("], ");
/* 2537 */     sb.append("rf.[").append("RMEntityType").append("], ");
/* 2538 */     sb.append("rf.[").append("FolderName").append("] ");
/* 2539 */     sb.append("FROM [").append("RMFolder").append("] rf ");
/* 2540 */     sb.append("WHERE rf.[").append("Parent").append("] = OBJECT('").append(containerId).append("') ");
/* 2541 */     sb.append("AND rf.[").append("DisposalScheduleInheritedFrom").append("] = ").append(containerId).append(' ');
/* 2542 */     sb.append("AND rf.[").append("IsDeleted").append("] = FALSE ");
/* 2543 */     String sql = sb.toString();
/*      */     
/* 2545 */     Repository repository = getRepository();
/* 2546 */     RMSearch search = new RMSearch(repository);
/*      */     
/* 2548 */     Integer pageSize = null;
/* 2549 */     RMPropertyFilter pf = RMPropertyFilter.MinimumPropertySet;
/* 2550 */     Boolean continuable = Boolean.TRUE;
/* 2551 */     PageableSet<Container> results = search.fetchObjects(sql, EntityType.Container, pageSize, pf, continuable);
/*      */     
/* 2553 */     Tracer.traceMethodExit(new Object[] { results });
/* 2554 */     return results;
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
/*      */   private PageableSet<Container> getNonScheduleInheritingSubcontainers(Boolean ckExistingScheduleIsNull)
/*      */   {
/* 2576 */     Tracer.traceMethodEntry(new Object[] { ckExistingScheduleIsNull });
/* 2577 */     String containerId = getObjectIdentity();
/*      */     
/* 2579 */     StringBuilder sb = new StringBuilder();
/* 2580 */     sb.append("SELECT rf.[").append("Id").append("], ");
/* 2581 */     sb.append("rf.[").append("RMEntityType").append("], ");
/* 2582 */     sb.append("rf.[").append("FolderName").append("] ");
/* 2583 */     sb.append("FROM [").append("RMFolder").append("] rf ");
/* 2584 */     sb.append("WHERE rf.[").append("Parent").append("] = OBJECT('").append(containerId).append("') ");
/* 2585 */     sb.append("AND rf.[").append("DisposalScheduleInheritedFrom").append("] IS NULL ");
/* 2586 */     sb.append("AND rf.[").append("IsDeleted").append("] = FALSE ");
/* 2587 */     if (ckExistingScheduleIsNull != null)
/*      */     {
/* 2589 */       sb.append("AND rf.[").append("DisposalSchedule").append("] ");
/* 2590 */       sb.append(ckExistingScheduleIsNull.booleanValue() ? "IS NULL " : "IS NOT NULL ");
/*      */     }
/* 2592 */     String sql = sb.toString();
/*      */     
/* 2594 */     Repository repository = getRepository();
/* 2595 */     RMSearch search = new RMSearch(repository);
/*      */     
/* 2597 */     Integer pageSize = null;
/* 2598 */     RMPropertyFilter pf = RMPropertyFilter.MinimumPropertySet;
/* 2599 */     Boolean continuable = Boolean.TRUE;
/* 2600 */     PageableSet<Container> results = search.fetchObjects(sql, EntityType.Container, pageSize, pf, continuable);
/*      */     
/* 2602 */     Tracer.traceMethodExit(new Object[] { results });
/* 2603 */     return results;
/*      */   }
/*      */   
/*      */   public abstract String getFolderName();
/*      */   
/*      */   public abstract String getPathName();
/*      */   
/*      */   protected abstract boolean verifyContainerNamesAreUnique(Container paramContainer1, EntityType paramEntityType, RMProperties paramRMProperties, Container paramContainer2);
/*      */   
/*      */   protected abstract List<Integer> getInitialAllowedRMTypes(String paramString);
/*      */   
/*      */   public abstract Container getParent();
/*      */   
/*      */   protected abstract boolean hasChildContainers();
/*      */   
/*      */   protected abstract boolean hasChildRecords();
/*      */   
/*      */   public abstract FilePlan getFilePlan();
/*      */   
/*      */   protected abstract EntityType getEntityTypeForClass(Repository paramRepository, String paramString);
/*      */   
/*      */   protected abstract RMClassDescription getClassDescription(Repository paramRepository, String paramString);
/*      */   
/*      */   protected abstract int findLastVolumeSuffix();
/*      */   
/*      */   public abstract DispositionSchedule getAssignedSchedule();
/*      */   
/*      */   protected abstract boolean userCanFileIntoClosedContainer(FilePlanRepository paramFilePlanRepository);
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */