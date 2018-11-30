/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.filenet.api.constants.AccessRight;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.DefensiblyDisposable;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Holdable;
/*     */ import com.ibm.jarm.api.core.RMFactory.Container;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordCategory;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.core.RecordFolder;
/*     */ import com.ibm.jarm.api.core.RecordVolume;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrivilegesUtil
/*     */ {
/*  52 */   public static final int ACCESSMASK_FULL_CONTROL_OBJECT_STORE = AccessRight.WRITE_ANY_OWNER.getValue() + AccessRight.REMOVE_OBJECTS.getValue() + AccessRight.MODIFY_OBJECTS.getValue() + AccessRight.STORE_OBJECTS.getValue() + AccessRight.CONNECT.getValue() + AccessRight.WRITE_ACL.getValue() + AccessRight.READ_ACL.getValue();
/*     */   
/*     */ 
/*  55 */   public static final int ACCESSMASK_FULL_CONTROL_FOLDER = AccessRight.VIEW_CONTENT.getValue() + AccessRight.WRITE_ACL.getValue() + AccessRight.CREATE_CHILD.getValue() + AccessRight.LINK.getValue() + AccessRight.UNLINK.getValue() + AccessRight.READ_ACL.getValue() + AccessRight.CREATE_INSTANCE.getValue() + AccessRight.DELETE.getValue() + AccessRight.WRITE_OWNER.getValue() + AccessRight.MAJOR_VERSION.getValue() + AccessRight.MINOR_VERSION.getValue() + AccessRight.PUBLISH.getValue();
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean hasAccessRights(AccessRight accessRight, int accessAllowed)
/*     */   {
/*  61 */     return (accessAllowed & accessRight.getValue()) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canDeclareRecordToContainer(RecordContainer recordContainer)
/*     */   {
/*  70 */     boolean privFileInFolder = hasAccessRights(AccessRight.LINK, recordContainer.getAccessAllowed().intValue());
/*     */     
/*  72 */     boolean isActive = true;
/*  73 */     boolean isClosed = false;
/*  74 */     boolean isRecordContainer = false;
/*     */     
/*  76 */     isRecordContainer = true;
/*     */     
/*  78 */     isActive = !recordContainer.isInactive();
/*     */     
/*  80 */     isClosed = recordContainer.isClosed();
/*  81 */     return (privFileInFolder) && (isRecordContainer) && (isActive) && (!isClosed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canMoveRecordToContainer(RecordContainer recordContainer, HttpServletRequest request)
/*     */   {
/*  92 */     boolean privFileInFolder = hasAccessRights(AccessRight.LINK, recordContainer.getAccessAllowed().intValue());
/*     */     
/*  94 */     boolean isRecordContainer = true;
/*     */     
/*  96 */     boolean isActive = !recordContainer.isInactive();
/*     */     
/*  98 */     boolean isClosed = recordContainer.isClosed();
/*     */     
/* 100 */     boolean isAdminForRecordVolume = false;
/*     */     
/*     */ 
/*     */ 
/* 104 */     if ((recordContainer instanceof RecordCategory)) {
/* 105 */       return (privFileInFolder) && (isRecordContainer) && (isActive);
/*     */     }
/*     */     
/* 108 */     if ((recordContainer instanceof RecordFolder)) {
/* 109 */       return (privFileInFolder) && (isRecordContainer) && (isActive) && (!isClosed);
/*     */     }
/*     */     
/*     */ 
/* 113 */     if ((recordContainer instanceof RecordVolume)) {
/* 114 */       isAdminForRecordVolume = isRecordsAdministratorAndManager(recordContainer.getRepository(), request);
/*     */       
/* 116 */       return (privFileInFolder) && (isRecordContainer) && (isActive) && ((!isClosed) || (isAdminForRecordVolume));
/*     */     }
/*     */     
/* 119 */     return false;
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
/*     */   public static boolean canFileRecordToContainer(RecordContainer recordContainer, HttpServletRequest request)
/*     */   {
/* 132 */     boolean canFile = canMoveRecordToContainer(recordContainer, request);
/* 133 */     if (recordContainer.isADefensiblyDisposableContainer()) {
/* 134 */       canFile = false;
/*     */     }
/* 136 */     return canFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canIntiateDisposition(BaseEntity entity, HttpServletRequest request)
/*     */   {
/* 147 */     boolean canInitiateDisposition = isRecordsAdministratorAndManager(entity.getRepository(), request);
/*     */     
/*     */ 
/* 150 */     if ((entity instanceof RecordContainer)) {
/* 151 */       RecordContainer container = (RecordContainer)entity;
/* 152 */       if (container.isADefensiblyDisposableContainer()) {
/* 153 */         canInitiateDisposition = false;
/*     */       }
/*     */     }
/*     */     
/* 157 */     if (((entity instanceof Record)) && 
/* 158 */       (isParentDefensibleDisposalContainer(entity, request))) {
/* 159 */       canInitiateDisposition = false;
/*     */     }
/*     */     
/* 162 */     return canInitiateDisposition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canFileRecord(BaseEntity entity, HttpServletRequest request)
/*     */   {
/* 172 */     boolean privCanFileRecord = hasAccessRights(AccessRight.READ, entity.getAccessAllowed().intValue());
/*     */     
/* 174 */     Repository repository = entity.getRepository();
/* 175 */     Object multiFilingSupportedObj = SessionUtil.getCacheProperty(repository.getObjectIdentity() + "_isMultiFilingSupported", request);
/* 176 */     boolean multiFilingSupported = false;
/* 177 */     if ((multiFilingSupportedObj == null) && 
/* 178 */       ((repository instanceof FilePlanRepository))) {
/* 179 */       FilePlanRepository fp_repo = (FilePlanRepository)repository;
/* 180 */       multiFilingSupportedObj = new Boolean(fp_repo.isRecordMultiFilingEnabled());
/* 181 */       SessionUtil.setCacheProperty(repository.getObjectIdentity() + "_isMultiFilingSupported", multiFilingSupportedObj, request);
/*     */     }
/*     */     
/* 184 */     multiFilingSupported = ((Boolean)multiFilingSupportedObj).booleanValue();
/*     */     
/* 186 */     if ((!multiFilingSupported) || (isParentDefensibleDisposalContainer(entity, request))) {
/* 187 */       privCanFileRecord = false;
/*     */     }
/* 189 */     return privCanFileRecord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canMoveRecord(BaseEntity entity, HttpServletRequest request)
/*     */   {
/* 199 */     boolean privCanMoveRecord = hasAccessRights(AccessRight.WRITE, entity.getAccessAllowed().intValue());
/*     */     
/* 201 */     RMProperties properties = entity.getProperties();
/* 202 */     if (properties.isPropertyPresent("CurrentPhaseExecutionStatus")) {
/* 203 */       Integer executionStatus = properties.getIntegerValue("CurrentPhaseExecutionStatus");
/* 204 */       boolean isBasicScheduleDispositionInProgress = (executionStatus != null) && (executionStatus.intValue() >= 1000);
/* 205 */       if (isBasicScheduleDispositionInProgress) {
/* 206 */         privCanMoveRecord = false;
/*     */       }
/*     */     }
/* 209 */     return privCanMoveRecord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isParentDefensibleDisposalContainer(BaseEntity entity, HttpServletRequest request)
/*     */   {
/* 218 */     Repository repository = entity.getRepository();
/* 219 */     Object parentIsDDContainerObj = SessionUtil.getCacheProperty(entity.getObjectIdentity() + "_isParentDefensibleDisposalContainer", request);
/* 220 */     boolean parentIsDDContainer = false;
/* 221 */     if ((parentIsDDContainerObj == null) && 
/* 222 */       ((repository instanceof FilePlanRepository))) {
/* 223 */       List<Container> containedBy = entity.getContainedBy();
/* 224 */       if (containedBy.size() != 0) {
/* 225 */         BaseEntity parentEntity = (BaseEntity)entity.getContainedBy().get(0);
/* 226 */         if ((parentEntity instanceof DefensiblyDisposable)) {
/* 227 */           DefensiblyDisposable container = (DefensiblyDisposable)parentEntity;
/* 228 */           if (container.isADefensiblyDisposableContainer()) {
/* 229 */             parentIsDDContainer = true;
/*     */           }
/*     */         }
/*     */       }
/* 233 */       parentIsDDContainerObj = new Boolean(parentIsDDContainer);
/* 234 */       SessionUtil.setCacheProperty(entity.getObjectIdentity() + "_isParentDefensibleDisposalContainer", parentIsDDContainerObj, request);
/*     */     }
/*     */     
/* 237 */     return ((Boolean)parentIsDDContainerObj).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canCloseRecordContainer(RecordContainer recordContainer)
/*     */   {
/* 246 */     boolean privModifyProperties = hasAccessRights(AccessRight.WRITE, recordContainer.getAccessAllowed().intValue());
/* 247 */     String externallyManaged = null;
/*     */     
/* 249 */     if (recordContainer.getProperties().isPropertyPresent("RMExternallyManagedBy")) {
/* 250 */       externallyManaged = recordContainer.getProperties().getStringValue("RMExternallyManagedBy");
/*     */     }
/* 252 */     return (recordContainer.isOpen()) && (privModifyProperties) && (externallyManaged == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canReopenContainer(RecordContainer recordContainer)
/*     */   {
/* 261 */     boolean privModifyProperties = hasAccessRights(AccessRight.WRITE, recordContainer.getAccessAllowed().intValue());
/* 262 */     String externallyManaged = null;
/*     */     
/* 264 */     if (recordContainer.getProperties().isPropertyPresent("RMExternallyManagedBy")) {
/* 265 */       externallyManaged = recordContainer.getProperties().getStringValue("RMExternallyManagedBy");
/*     */     }
/* 267 */     return (!recordContainer.isOpen()) && (privModifyProperties) && (externallyManaged == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canAddRecordCategory(Container container)
/*     */   {
/* 277 */     int accessAllowed = container.getAccessAllowed().intValue();
/* 278 */     boolean privCreateInstance = hasAccessRights(AccessRight.CREATE_INSTANCE, accessAllowed);
/* 279 */     boolean privFileInFolder = hasAccessRights(AccessRight.LINK, accessAllowed);
/* 280 */     boolean privCreateSubFolder = hasAccessRights(AccessRight.CREATE_CHILD, accessAllowed);
/*     */     
/* 282 */     boolean isActive = true;
/* 283 */     boolean isClosed = false;
/* 284 */     boolean allowRecordCategory = false;
/*     */     
/* 286 */     if ((container instanceof RecordContainer)) {
/* 287 */       RecordContainer recordContainer = (RecordContainer)container;
/*     */       
/* 289 */       isActive = !recordContainer.isInactive();
/*     */       
/* 291 */       isClosed = recordContainer.isClosed();
/*     */     }
/*     */     
/*     */ 
/* 295 */     EntityType[] allowedEntityTypes = container.getAllowedContaineeTypes();
/* 296 */     for (EntityType entityType : allowedEntityTypes) {
/* 297 */       if (entityType == EntityType.RecordCategory) {
/* 298 */         allowRecordCategory = true;
/*     */       }
/*     */     }
/*     */     
/* 302 */     return (privCreateInstance) && (privFileInFolder) && (privCreateSubFolder) && (allowRecordCategory) && (isActive) && (!isClosed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canAddRecordVolume(Container container)
/*     */   {
/* 312 */     int accessAllowed = container.getAccessAllowed().intValue();
/* 313 */     boolean privCreateInstance = hasAccessRights(AccessRight.CREATE_INSTANCE, accessAllowed);
/* 314 */     boolean privFileInFolder = hasAccessRights(AccessRight.LINK, accessAllowed);
/* 315 */     boolean privCreateSubFolder = hasAccessRights(AccessRight.CREATE_CHILD, accessAllowed);
/*     */     
/* 317 */     boolean isActive = true;
/* 318 */     boolean isClosed = false;
/* 319 */     boolean allowRecordVolume = false;
/*     */     
/* 321 */     if ((container instanceof RecordContainer)) {
/* 322 */       RecordContainer recordContainer = (RecordContainer)container;
/*     */       
/* 324 */       isActive = !recordContainer.isInactive();
/*     */       
/* 326 */       isClosed = recordContainer.isClosed();
/*     */       
/*     */ 
/* 329 */       EntityType[] allowedEntityTypes = container.getAllowedContaineeTypes();
/* 330 */       for (EntityType entityType : allowedEntityTypes) {
/* 331 */         if (entityType == EntityType.RecordVolume) {
/* 332 */           allowRecordVolume = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 337 */     return (privCreateInstance) && (privFileInFolder) && (privCreateSubFolder) && (allowRecordVolume) && (isActive) && (!isClosed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canAddRecordFolder(Container container)
/*     */   {
/* 347 */     int accessAllowed = container.getAccessAllowed().intValue();
/* 348 */     boolean privCreateInstance = hasAccessRights(AccessRight.CREATE_INSTANCE, accessAllowed);
/* 349 */     boolean privFileInFolder = hasAccessRights(AccessRight.LINK, accessAllowed);
/* 350 */     boolean privCreateSubFolder = hasAccessRights(AccessRight.CREATE_CHILD, accessAllowed);
/*     */     
/* 352 */     boolean isActive = true;
/* 353 */     boolean isClosed = false;
/* 354 */     boolean allowRecordFolder = false;
/*     */     
/* 356 */     if ((container instanceof RecordContainer)) {
/* 357 */       RecordContainer recordContainer = (RecordContainer)container;
/*     */       
/* 359 */       isActive = !recordContainer.isInactive();
/*     */       
/* 361 */       isClosed = recordContainer.isClosed();
/*     */       
/*     */ 
/* 364 */       EntityType[] allowedEntityTypes = container.getAllowedContaineeTypes();
/* 365 */       for (EntityType entityType : allowedEntityTypes) {
/* 366 */         if ((entityType == EntityType.RecordFolder) || (entityType == EntityType.ElectronicRecordFolder) || (entityType == EntityType.PhysicalContainer) || (entityType == EntityType.HybridRecordFolder) || (entityType == EntityType.PhysicalRecordFolder))
/*     */         {
/* 368 */           allowRecordFolder = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 373 */     return (privCreateInstance) && (privFileInFolder) && (privCreateSubFolder) && (allowRecordFolder) && (isActive) && (!isClosed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canPlaceHolds(BaseEntity entity)
/*     */   {
/* 383 */     int accessAllowed = entity.getAccessAllowed().intValue();
/* 384 */     boolean privModifyProperties = hasAccessRights(AccessRight.WRITE, accessAllowed);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 389 */     return privModifyProperties;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean canRemoveHolds(BaseEntity entity)
/*     */   {
/* 395 */     int accessAllowed = entity.getAccessAllowed().intValue();
/* 396 */     boolean privModifyProperties = hasAccessRights(AccessRight.WRITE, accessAllowed);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 401 */     return privModifyProperties;
/*     */   }
/*     */   
/*     */   public static boolean canDelete(BaseEntity entity, HttpServletRequest request)
/*     */   {
/* 406 */     int accessAllowed = entity.getAccessAllowed().intValue();
/* 407 */     boolean privDelete = hasAccessRights(AccessRight.DELETE, accessAllowed);
/*     */     
/* 409 */     if ((entity instanceof RecordContainer))
/*     */     {
/* 411 */       String externallyManaged = null;
/* 412 */       RecordContainer recordContainer = (RecordContainer)entity;
/*     */       
/* 414 */       if (recordContainer.getProperties().isPropertyPresent("RMExternallyManagedBy")) {
/* 415 */         externallyManaged = recordContainer.getProperties().getStringValue("RMExternallyManagedBy");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 425 */       boolean isLocalSchedule = false;
/* 426 */       List<Container> containedByList = recordContainer.getContainedBy();
/* 427 */       if ((containedByList != null) && (!containedByList.isEmpty()))
/*     */       {
/* 429 */         String containedById = ((Container)containedByList.get(0)).getObjectIdentity();
/* 430 */         Object isLocalScheduleObj = SessionUtil.getCacheProperty("isLocalSchedule" + containedById, request);
/* 431 */         if (isLocalScheduleObj == null) {
/* 432 */           isLocalSchedule = ((Container)recordContainer.getContainedBy().get(0)).getPathName().indexOf("LocalSchedules") >= 0;
/*     */           
/* 434 */           SessionUtil.setCacheProperty("isLocalSchedule" + containedById, Boolean.valueOf(isLocalSchedule), request);
/*     */         }
/*     */         else {
/* 437 */           isLocalSchedule = ((Boolean)isLocalScheduleObj).booleanValue();
/*     */         }
/*     */       }
/* 440 */       return (privDelete) && ((externallyManaged == null) || ((externallyManaged != null) && ((((RecordContainer)entity).isClosed()) || (!isLocalSchedule))));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 447 */     return privDelete;
/*     */   }
/*     */   
/*     */   public static boolean canRelocateContainer(RecordContainer recordContainer)
/*     */   {
/* 452 */     int accessAllowed = recordContainer.getAccessAllowed().intValue();
/* 453 */     boolean privRelocate = hasAccessRights(AccessRight.WRITE, accessAllowed);
/*     */     
/* 455 */     String externallyManaged = null;
/*     */     
/* 457 */     if (recordContainer.getProperties().isPropertyPresent("RMExternallyManagedBy")) {
/* 458 */       externallyManaged = recordContainer.getProperties().getStringValue("RMExternallyManagedBy");
/*     */     }
/* 460 */     return (privRelocate) && (externallyManaged == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isRecordsAdministratorAndManager(Repository repository, HttpServletRequest request)
/*     */   {
/* 470 */     if ((repository instanceof FilePlanRepository)) {
/* 471 */       FilePlanRepository fp_repository = (FilePlanRepository)repository;
/* 472 */       Object isAdministrator = null;
/* 473 */       if (request != null) {
/* 474 */         isAdministrator = SessionUtil.getCacheProperty("isRecordsAdministratorAndManager" + fp_repository.getClientIdentifier(), request);
/*     */       }
/* 476 */       if (isAdministrator == null) {
/* 477 */         int rights = fp_repository.getAccessAllowed().intValue();
/* 478 */         boolean isObjectStoreAdministrator = false;
/* 479 */         if (rights != 0) {
/* 480 */           isObjectStoreAdministrator = (ACCESSMASK_FULL_CONTROL_OBJECT_STORE & rights) == ACCESSMASK_FULL_CONTROL_OBJECT_STORE;
/*     */         }
/* 482 */         Container recordsManagementFolder = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, "//Records Management", RMPropertyFilter.MinimumPropertySet);
/* 483 */         boolean isRecordsManagementFolderFullControl = false;
/* 484 */         if (recordsManagementFolder != null) {
/* 485 */           int rmFolderRights = recordsManagementFolder.getAccessAllowed().intValue();
/* 486 */           if (rmFolderRights != 0) {
/* 487 */             isRecordsManagementFolderFullControl = (ACCESSMASK_FULL_CONTROL_FOLDER & rmFolderRights) == ACCESSMASK_FULL_CONTROL_FOLDER;
/*     */           }
/*     */         }
/*     */         
/* 491 */         isAdministrator = new Boolean((isObjectStoreAdministrator) && (isRecordsManagementFolderFullControl));
/* 492 */         if (request != null) {
/* 493 */           SessionUtil.setCacheProperty("isRecordsAdministratorAndManager" + fp_repository.getClientIdentifier(), isAdministrator, request);
/*     */         }
/*     */       }
/* 496 */       return ((Boolean)isAdministrator).booleanValue();
/*     */     }
/* 498 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canConvertToDDContainer(RecordContainer entity, HttpServletRequest request) {
/* 502 */     if ((!entity.isADefensiblyDisposableContainer()) && (isRecordsAdministratorAndManager(entity.getRepository(), request)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 512 */       if (entity.getEntityType() == EntityType.RecordCategory)
/* 513 */         return true;
/*     */     }
/* 515 */     return false;
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
/*     */   public static void setPrivilegesJSON(BaseEntity entity, JSONObject jsonPrivileges, HttpServletRequest request)
/*     */   {
/* 528 */     int accessAllowed = entity.getAccessAllowed().intValue();
/* 529 */     boolean privViewProperties = hasAccessRights(AccessRight.READ, accessAllowed);
/* 530 */     boolean privUseMarking = hasAccessRights(AccessRight.USE_MARKING, accessAllowed);
/* 531 */     boolean privAddMarking = hasAccessRights(AccessRight.ADD_MARKING, accessAllowed);
/* 532 */     boolean privModifyProperties = hasAccessRights(AccessRight.WRITE, accessAllowed);
/* 533 */     boolean privCreateInstance = hasAccessRights(AccessRight.CREATE_INSTANCE, accessAllowed);
/* 534 */     boolean privFileInFolder = hasAccessRights(AccessRight.LINK, accessAllowed);
/* 535 */     boolean privUnfileInFolder = hasAccessRights(AccessRight.UNLINK, accessAllowed);
/* 536 */     boolean privCreateSubFolder = hasAccessRights(AccessRight.CREATE_CHILD, accessAllowed);
/* 537 */     boolean privModifyPermissions = hasAccessRights(AccessRight.WRITE_ACL, accessAllowed);
/* 538 */     boolean privReadPermissions = hasAccessRights(AccessRight.READ_ACL, accessAllowed);
/* 539 */     boolean privViewEntities = hasAccessRights(AccessRight.READ, accessAllowed);
/*     */     
/* 541 */     if ((entity instanceof Container)) {
/* 542 */       Container container = (Container)entity;
/*     */       
/*     */ 
/* 545 */       jsonPrivileges.put("privIERAddRecordCategory", Boolean.valueOf(canAddRecordCategory(container)));
/* 546 */       jsonPrivileges.put("privIERAddRecordFolder", Boolean.valueOf(canAddRecordFolder(container)));
/* 547 */       jsonPrivileges.put("privIERAddRecordVolume", Boolean.valueOf(canAddRecordVolume(container)));
/*     */       
/* 549 */       if ((container instanceof RecordContainer)) {
/* 550 */         RecordContainer recordContainer = (RecordContainer)container;
/*     */         
/*     */ 
/* 553 */         jsonPrivileges.put("privCanDeclareRecordToContainer", Boolean.valueOf(canDeclareRecordToContainer(recordContainer)));
/* 554 */         jsonPrivileges.put("privCanMoveRecordToContainer", Boolean.valueOf(canMoveRecordToContainer(recordContainer, request)));
/* 555 */         jsonPrivileges.put("privCanFileRecordToContainer", Boolean.valueOf(canFileRecordToContainer(recordContainer, request)));
/* 556 */         jsonPrivileges.put("privIERCloseContainer", Boolean.valueOf(canCloseRecordContainer(recordContainer)));
/* 557 */         jsonPrivileges.put("privIERReopenContainer", Boolean.valueOf(canReopenContainer(recordContainer)));
/* 558 */         jsonPrivileges.put("privIERRelocateContainer", Boolean.valueOf(canRelocateContainer(recordContainer)));
/* 559 */         jsonPrivileges.put("privCanConvertToDDContainer", Boolean.valueOf(canConvertToDDContainer(recordContainer, request)));
/*     */       }
/*     */     }
/*     */     else {
/* 563 */       jsonPrivileges.put("privCanFileRecord", Boolean.valueOf(canFileRecord(entity, request)));
/* 564 */       jsonPrivileges.put("privCanMoveRecord", Boolean.valueOf(canMoveRecord(entity, request)));
/*     */     }
/*     */     
/* 567 */     if ((entity instanceof Holdable)) {
/* 568 */       jsonPrivileges.put("privIERPlaceOnHold", Boolean.valueOf(canPlaceHolds(entity)));
/*     */       
/* 570 */       jsonPrivileges.put("privIERRemoveHold", Boolean.valueOf(canRemoveHolds(entity)));
/*     */     }
/*     */     
/*     */ 
/* 574 */     jsonPrivileges.put("privIERDelete", Boolean.valueOf(canDelete(entity, request)));
/* 575 */     jsonPrivileges.put("privIERRecordsAdminAndManager", Boolean.valueOf(isRecordsAdministratorAndManager(entity.getRepository(), request)));
/*     */     
/*     */ 
/* 578 */     jsonPrivileges.put("privViewProperties", Boolean.valueOf(privViewProperties));
/* 579 */     jsonPrivileges.put("privModifyProperties", Boolean.valueOf(privModifyProperties));
/* 580 */     jsonPrivileges.put("privEditProperties", Boolean.valueOf(privModifyProperties));
/* 581 */     jsonPrivileges.put("privUseMarkings", Boolean.valueOf(privUseMarking));
/* 582 */     jsonPrivileges.put("privAddMarkings", Boolean.valueOf(privAddMarking));
/* 583 */     jsonPrivileges.put("privFileInFolder", Boolean.valueOf(privFileInFolder));
/* 584 */     jsonPrivileges.put("privCreateInstance", Boolean.valueOf(privCreateInstance));
/* 585 */     jsonPrivileges.put("privUnfileInFolder", Boolean.valueOf(privUnfileInFolder));
/* 586 */     jsonPrivileges.put("privModifyPermissions", Boolean.valueOf(privModifyPermissions));
/* 587 */     jsonPrivileges.put("privCreateSubFolder", Boolean.valueOf(privCreateSubFolder));
/* 588 */     jsonPrivileges.put("privReadPermissions", Boolean.valueOf(privReadPermissions));
/* 589 */     jsonPrivileges.put("privCanInitiateDisposition", Boolean.valueOf(canIntiateDisposition(entity, request)));
/* 590 */     jsonPrivileges.put("privIERViewEntitiesOnHold", Boolean.valueOf(privViewEntities));
/* 591 */     jsonPrivileges.put("privIERRemoveHold", Boolean.valueOf(privModifyPermissions));
/* 592 */     jsonPrivileges.put("privIERInitiateRemoveHoldRequest", Boolean.valueOf(privModifyPermissions));
/* 593 */     jsonPrivileges.put("privIERCancelRemoveHoldRequest", Boolean.valueOf(privModifyPermissions));
/* 594 */     jsonPrivileges.put("privIERActivateHoldSweepProcessing", Boolean.valueOf(privModifyPermissions));
/* 595 */     jsonPrivileges.put("privIERRunReport", Boolean.valueOf(privViewEntities));
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\PrivilegesUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */