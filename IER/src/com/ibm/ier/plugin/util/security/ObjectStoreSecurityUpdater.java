/*     */ package com.ibm.ier.plugin.util.security;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.ContentElementList;
/*     */ import com.filenet.api.constants.AccessRight;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.constants.AutoClassify;
/*     */ import com.filenet.api.constants.AutoUniqueName;
/*     */ import com.filenet.api.constants.CheckinType;
/*     */ import com.filenet.api.constants.DefineSecurityParentage;
/*     */ import com.filenet.api.constants.GuidConstants;
/*     */ import com.filenet.api.constants.PermissionSource;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.constants.ReservationType;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.ContentTransfer;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.core.Factory.ClassDefinition;
/*     */ import com.filenet.api.core.Factory.ContentElement;
/*     */ import com.filenet.api.core.Factory.ContentTransfer;
/*     */ import com.filenet.api.core.Factory.CustomObject;
/*     */ import com.filenet.api.core.Factory.Document;
/*     */ import com.filenet.api.core.Factory.Domain;
/*     */ import com.filenet.api.core.Factory.Folder;
/*     */ import com.filenet.api.core.Factory.ObjectStore;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*     */ import com.filenet.api.core.UpdatingBatch;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.filenet.api.security.Group;
/*     */ import com.filenet.api.security.SecurityPrincipal;
/*     */ import com.filenet.api.security.User;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.util.DateUtil;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public abstract class ObjectStoreSecurityUpdater
/*     */ {
/*     */   protected List<SecurityPrincipal> guideAdminsGroup;
/*     */   protected List<SecurityPrincipal> adminGroup;
/*     */   protected List<SecurityPrincipal> managerGroup;
/*     */   protected List<SecurityPrincipal> privUserGroup;
/*     */   protected List<SecurityPrincipal> userGroup;
/*     */   protected ObjectStore objStore;
/*     */   protected UpdatingBatch updatingBatch;
/*     */   private final Domain domain;
/*     */   protected static final int DEPTH_NO_INHERITANCE = 0;
/*     */   protected static final int DEPTH_IMMEDIATE_CHILDREN_ONLY = 1;
/*     */   protected static final int DEPTH_ALL_CHILDREN = -1;
/*     */   protected static final String CREATOR_OWNER = "#CREATOR-OWNER";
/*     */   protected static final String MS_DODCLASSIFIED_SECURITY_CAT = "Security Categories DoD Classified";
/*     */   protected static final String MS_SECURITY_CAT = "Security Categories";
/*     */   protected static final String PROPERTY_NAME = "PropertyName";
/*     */   protected static final String PROPERTY_VALUE = "PropertyValue";
/*     */   protected static final String DEFAULT_VALUE = "DefaultValue";
/*     */   protected static final String SYSTEM_CONFIGURATION = "SystemConfiguration";
/*     */   protected static final String PROPERTY_DATA_TYPE = "PropertyDataType";
/*     */   protected static final String AUTH_USER = "#AUTHENTICATED-USERS";
/*     */   protected static final int ACCESSLEVEL_CUSTOM_WRITE_FOLDER = 135159;
/*     */   public static final String DOCUMENT_SECURITY_GROUP_STORAGE = "{CA81CDCF-9BBF-40C2-AC21-4C65ACCB3D45}";
/*     */   public static final String CUSTOM_OBJECT_SECURITY_RUN_DATA_SYSTEM_CONFIGURATION = "{C83A89E1-95EE-4E62-8B54-FEE57DBDBD3E}";
/*     */   public static final String FOLDER_SYSTEMCONFIGURATION = "{56CFE062-DADF-4E5E-A9EB-E83A1352AC8F}";
/*     */   public static final int RMGROUP_RMCLASS_GUIDE_ADMINS = 0;
/*     */   public static final int RMGROUP_RMADMINS = 1;
/*     */   public static final int RMGROUP_RMMANAGERS = 2;
/*     */   public static final int RMGROUP_RMPRIVUSERS = 3;
/*     */   public static final int RMGROUP_RMUSERS = 4;
/*     */   
/*     */   public ObjectStoreSecurityUpdater(String osname, List<SecurityPrincipal>[] rmGroups, Connection conn)
/*     */     throws Exception
/*     */   {
/*  97 */     if ((osname == null) || (rmGroups == null) || (conn == null)) {
/*  98 */       throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_REPOSITORY_UNEXPECTED, new Object[0]);
/*     */     }
/* 100 */     this.guideAdminsGroup = rmGroups[0];
/* 101 */     this.adminGroup = rmGroups[1];
/* 102 */     this.managerGroup = rmGroups[2];
/* 103 */     this.privUserGroup = rmGroups[3];
/* 104 */     this.userGroup = rmGroups[4];
/*     */     
/*     */ 
/* 107 */     if (this.guideAdminsGroup != null) {
/* 108 */       this.userGroup.addAll(this.guideAdminsGroup);
/*     */     }
/* 110 */     this.domain = Factory.Domain.fetchInstance(conn, null, null);
/* 111 */     this.objStore = Factory.ObjectStore.fetchInstance(this.domain, osname, null);
/* 112 */     this.updatingBatch = UpdatingBatch.createUpdatingBatchInstance(this.domain, RefreshMode.REFRESH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String setEntireSecurity()
/*     */     throws Exception
/*     */   {
/* 122 */     String methodName = "setEntireSecurity";
/* 123 */     String isoDate = null;
/*     */     try
/*     */     {
/* 126 */       Logger.logEntry(this, methodName, "Starting to set Security Script for " + this.objStore.get_DisplayName());
/* 127 */       if (this.guideAdminsGroup != null)
/* 128 */         Logger.logInfo(this, methodName, getGroupInfo("Classification Guide Administrator", this.guideAdminsGroup));
/* 129 */       if (this.adminGroup != null)
/* 130 */         Logger.logInfo(this, methodName, getGroupInfo("Records Administrator", this.adminGroup));
/* 131 */       if (this.managerGroup != null)
/* 132 */         Logger.logInfo(this, methodName, getGroupInfo("Records Manager", this.managerGroup));
/* 133 */       if (this.privUserGroup != null)
/* 134 */         Logger.logInfo(this, methodName, getGroupInfo("Records Privileged User", this.privUserGroup));
/* 135 */       if (this.userGroup != null) {
/* 136 */         Logger.logInfo(this, methodName, getGroupInfo("Records User", this.userGroup));
/*     */       }
/* 138 */       int rights = this.domain.getAccessAllowed().intValue();
/* 139 */       if (!AccessLevel.isGCDAdministrator(rights))
/*     */       {
/* 141 */         throw IERUIRuntimeException.createUIRuntimeException("error.invalidGCDAccess", new Object[0]);
/*     */       }
/*     */       
/* 144 */       Logger.logDebug(this, methodName, "Beginning to set marking set related security");
/* 145 */       setMarkingSetRelatedSecurity();
/* 146 */       Logger.logDebug(this, methodName, "Finished settomg marking set related security");
/*     */       
/* 148 */       Logger.logDebug(this, methodName, "Beginning to set object store related security");
/* 149 */       setObjectStoreRelatedSecurity();
/* 150 */       Logger.logDebug(this, methodName, "Finish setting object store related security");
/*     */       
/*     */ 
/* 153 */       Logger.logDebug(this, methodName, "Beginning to create the security run date object");
/* 154 */       isoDate = setOrCreateSecurityRunDate();
/*     */       
/* 156 */       Logger.logDebug(this, methodName, "Beginning to save user security groups");
/* 157 */       saveSecurityGroups();
/*     */       
/* 159 */       if (this.updatingBatch.hasPendingExecute()) {
/* 160 */         this.updatingBatch.updateBatch();
/*     */       }
/* 162 */       Logger.logExit(this, methodName, "Setting security for " + this.objStore.get_DisplayName() + " completed successfully");
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 166 */       Logger.logError(this, methodName, ex);
/* 167 */       if ((ex instanceof EngineRuntimeException))
/*     */       {
/* 169 */         EngineRuntimeException ce = (EngineRuntimeException)ex;
/* 170 */         if ((ce.getExceptionCode() != null) && (ce.getExceptionCode() == ExceptionCode.E_READ_ONLY))
/*     */         {
/* 172 */           ex = IERUIRuntimeException.createUIRuntimeException(ce, MessageCode.E_PRIVILEGE_INSUFFICIENT, new Object[] { ce.getLocalizedMessage() });
/*     */         }
/*     */       }
/* 175 */       throw ex;
/*     */     }
/*     */     
/* 178 */     return isoDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void saveSecurityGroups()
/*     */     throws Exception
/*     */   {
/* 188 */     Document document = null;
/* 189 */     StringBuffer groups = new StringBuffer();
/* 190 */     if (this.guideAdminsGroup != null)
/* 191 */       groups.append(getMembersInfo(0, this.guideAdminsGroup, "<-RM_GROUP_SEPARATOR->") + "\n");
/* 192 */     if (this.adminGroup != null)
/* 193 */       groups.append(getMembersInfo(1, this.adminGroup, "<-RM_GROUP_SEPARATOR->") + "\n");
/* 194 */     if (this.managerGroup != null)
/* 195 */       groups.append(getMembersInfo(2, this.managerGroup, "<-RM_GROUP_SEPARATOR->") + "\n");
/* 196 */     if (this.privUserGroup != null)
/* 197 */       groups.append(getMembersInfo(3, this.privUserGroup, "<-RM_GROUP_SEPARATOR->") + "\n");
/* 198 */     if (this.userGroup != null) {
/* 199 */       groups.append(getMembersInfo(4, this.userGroup, "<-RM_GROUP_SEPARATOR->") + "\n");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 204 */       document = Factory.Document.fetchInstance(this.objStore, new Id("{CA81CDCF-9BBF-40C2-AC21-4C65ACCB3D45}"), null);
/* 205 */       if (!document.get_IsReserved().booleanValue())
/*     */       {
/* 207 */         document = (Document)document.get_CurrentVersion();
/* 208 */         document.checkout(ReservationType.COLLABORATIVE, null, null, document.getProperties());
/* 209 */         document.save(RefreshMode.NO_REFRESH);
/*     */       }
/* 211 */       document = (Document)document.get_Reservation();
/*     */ 
/*     */     }
/*     */     catch (EngineRuntimeException e)
/*     */     {
/*     */ 
/* 217 */       if ((e.getExceptionCode() != null) && (e.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND))
/*     */       {
/* 219 */         document = Factory.Document.createInstance(this.objStore, GuidConstants.Class_Document.toString(), new Id("{CA81CDCF-9BBF-40C2-AC21-4C65ACCB3D45}"), Id.createId(), ReservationType.OBJECT_STORE_DEFAULT);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 224 */         throw e;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 229 */     if (groups.length() > 0)
/*     */     {
/* 231 */       ContentElementList contentList = Factory.ContentElement.createList();
/* 232 */       ContentTransfer tempContent = Factory.ContentTransfer.createInstance();
/* 233 */       tempContent.set_ContentType("application/text");
/* 234 */       tempContent.setCaptureSource(new ByteArrayInputStream(groups.toString().getBytes("utf-8")));
/* 235 */       tempContent.set_RetrievalName("{CA81CDCF-9BBF-40C2-AC21-4C65ACCB3D45}");
/* 236 */       contentList.add(tempContent);
/*     */       
/* 238 */       document.set_ContentElements(contentList);
/*     */       
/*     */ 
/* 241 */       AccessPermissionList apl = Factory.AccessPermission.createList();
/* 242 */       addAccessPermissionsMain(apl, 0, 986583);
/* 243 */       document.set_Permissions(apl);
/*     */       
/* 245 */       document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/* 246 */       this.updatingBatch.add(document, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private String setOrCreateSecurityRunDate()
/*     */   {
/* 253 */     String isoDate = DateUtil.getISODateString(new Date(), true);
/*     */     try
/*     */     {
/* 256 */       CustomObject runDate = Factory.CustomObject.fetchInstance(this.objStore, new Id("{C83A89E1-95EE-4E62-8B54-FEE57DBDBD3E}"), null);
/* 257 */       Properties props = runDate.getProperties();
/* 258 */       props.putValue("PropertyValue", isoDate);
/* 259 */       this.updatingBatch.add(runDate, null);
/*     */     }
/*     */     catch (EngineRuntimeException e) {
/* 262 */       if ((e.getExceptionCode() != null) && (e.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND))
/*     */       {
/* 264 */         Folder systemConfigurationFolder = Factory.Folder.fetchInstance(this.objStore, new Id("{56CFE062-DADF-4E5E-A9EB-E83A1352AC8F}"), null);
/* 265 */         CustomObject runDate = Factory.CustomObject.createInstance(this.objStore, "SystemConfiguration", new Id("{C83A89E1-95EE-4E62-8B54-FEE57DBDBD3E}"));
/* 266 */         Properties props = runDate.getProperties();
/* 267 */         props.putValue("PropertyName", "Security Script Run Date");
/* 268 */         props.putValue("DefaultValue", "Security Script Run Date");
/* 269 */         props.putValue("PropertyValue", isoDate);
/* 270 */         this.updatingBatch.add(runDate, null);
/*     */         
/* 272 */         ReferentialContainmentRelationship rcr = systemConfigurationFolder.file(runDate, AutoUniqueName.AUTO_UNIQUE, "Security Script Run Date", DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
/*     */         
/* 274 */         this.updatingBatch.add(rcr, null);
/*     */       }
/*     */       else
/*     */       {
/* 278 */         throw e;
/*     */       }
/*     */     }
/* 281 */     return isoDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void setObjectStoreRelatedSecurity()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void setMarkingSetRelatedSecurity()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   private String getGroupInfo(String groupName, List<SecurityPrincipal> group)
/*     */   {
/* 298 */     return getGroupInfo(groupName, group, ", ");
/*     */   }
/*     */   
/*     */   private String getGroupInfo(String groupName, List<SecurityPrincipal> group, String separator)
/*     */   {
/* 303 */     StringBuffer caGroup = new StringBuffer(groupName);
/* 304 */     caGroup.append(" : ");
/* 305 */     caGroup.append(group.size());
/* 306 */     caGroup.append(" ");
/* 307 */     caGroup.append("[");
/* 308 */     caGroup.append(getMembersInfo(group, separator));
/* 309 */     caGroup.append("]");
/* 310 */     return caGroup.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private String getMembersInfo(List<SecurityPrincipal> group, String separator)
/*     */   {
/* 316 */     StringBuffer members = new StringBuffer();
/* 317 */     for (int i = 0; i < group.size(); i++)
/*     */     {
/* 319 */       SecurityPrincipal principal = (SecurityPrincipal)group.get(i);
/* 320 */       if ((principal instanceof User))
/*     */       {
/* 322 */         members.append(((User)principal).get_DisplayName());
/*     */       }
/* 324 */       else if ((principal instanceof Group))
/*     */       {
/* 326 */         members.append(((Group)principal).get_DisplayName());
/*     */       }
/* 328 */       if (i < group.size() - 1)
/*     */       {
/* 330 */         members.append(separator);
/*     */       }
/*     */     }
/* 333 */     return members.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private String getMembersInfo(int groupSlot, List<SecurityPrincipal> group, String separator)
/*     */   {
/* 339 */     StringBuffer members = new StringBuffer(String.valueOf(groupSlot));
/* 340 */     members.append(separator);
/* 341 */     for (int i = 0; i < group.size(); i++)
/*     */     {
/* 343 */       Object ref = group.get(i);
/* 344 */       boolean notAppend = false;
/*     */       
/*     */ 
/* 347 */       if (group == this.userGroup)
/*     */       {
/* 349 */         if (this.guideAdminsGroup != null)
/*     */         {
/* 351 */           for (int j = 0; j < this.guideAdminsGroup.size(); j++)
/*     */           {
/* 353 */             if (ref == this.guideAdminsGroup.get(j))
/*     */             {
/* 355 */               notAppend = true;
/* 356 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 362 */       if (!notAppend)
/*     */       {
/* 364 */         if ((ref instanceof User)) {
/* 365 */           members.append(((User)ref).get_ShortName());
/* 366 */           members.append("<-RM_MEMBER_SEPARATOR->2000");
/* 367 */         } else if ((ref instanceof Group)) {
/* 368 */           members.append(((Group)ref).get_ShortName());
/* 369 */           members.append("<-RM_MEMBER_SEPARATOR->2001");
/*     */         }
/* 371 */         if (i < group.size() - 1)
/*     */         {
/* 373 */           members.append(separator);
/*     */         }
/*     */       }
/*     */     }
/* 377 */     return members.toString();
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityFolder1(String name, int depth, boolean removeDefaultInstance) {
/* 381 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 382 */     if (removeDefaultInstance)
/* 383 */       cd.set_DefaultInstancePermissions(Factory.AccessPermission.createList());
/* 384 */     addAccessPermissionsAdminsAndManagers(cd.get_Permissions(), depth, 983827);
/* 385 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityFolder2(String name, int depth) {
/* 389 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 390 */     AccessPermissionList cd_dapl = Factory.AccessPermission.createList();
/* 391 */     addAccessPermission(this.userGroup, 135159, AccessType.ALLOW, depth, cd_dapl);
/* 392 */     addAccessPermission(this.userGroup, AccessRight.CREATE_INSTANCE.getValue(), AccessType.DENY, depth, cd_dapl);
/* 393 */     cd.set_DefaultInstancePermissions(cd_dapl);
/*     */     
/* 395 */     addAccessPermissionsAdminsAndManagers(cd.get_Permissions(), -1, 983827);
/* 396 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityRecord1(String name, int depth, boolean removeDefaultInstance) {
/* 400 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 401 */     if (removeDefaultInstance)
/*     */     {
/* 403 */       setCreatorOwnerAsOnlyDefaultInstance(cd);
/*     */     }
/* 405 */     addAccessPermissionsAdminsAndManagers(cd.get_Permissions(), depth, 983827);
/* 406 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityRecord1(ClassDefinition cd, int depth, boolean removeDefaultInstance) {
/* 410 */     if (cd != null) {
/* 411 */       if (removeDefaultInstance)
/*     */       {
/* 413 */         setCreatorOwnerAsOnlyDefaultInstance(cd);
/*     */       }
/* 415 */       addAccessPermissionsAdminsAndManagers(cd.get_Permissions(), depth, 983827);
/* 416 */       this.updatingBatch.add(cd, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ClassDefinition getClassDefinition(String name) throws Exception {
/*     */     try {
/* 422 */       return Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/*     */     }
/*     */     catch (EngineRuntimeException ex) {
/* 425 */       if (ex.getExceptionCode() == ExceptionCode.E_BAD_CLASSID) {
/* 426 */         return null;
/*     */       }
/*     */       
/* 429 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityCustom1(String name, int depth)
/*     */   {
/* 435 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 436 */     AccessPermissionList cd_dapl = Factory.AccessPermission.createList();
/* 437 */     addAccessPermissionsMain(cd_dapl, depth, 983315, 131073);
/* 438 */     cd.set_DefaultInstancePermissions(cd_dapl);
/*     */     
/* 440 */     removeAuthUserPermission(cd, true);
/* 441 */     addAccessPermissionsMain(cd.get_Permissions(), depth, 983827, 131073);
/* 442 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityLink1(String name, int depth)
/*     */   {
/* 447 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 448 */     AccessPermissionList cd_dapl = Factory.AccessPermission.createList();
/* 449 */     addAccessPermissionsAdminsAndManagers(cd_dapl, depth, 983315);
/* 450 */     addAccessPermission(this.privUserGroup, 983315, AccessType.ALLOW, depth, cd_dapl);
/* 451 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_dapl);
/* 452 */     cd.set_DefaultInstancePermissions(cd_dapl);
/*     */     
/* 454 */     AccessPermissionList cd_apl = cd.get_Permissions();
/* 455 */     addAccessPermission(this.privUserGroup, 983827, AccessType.ALLOW, depth, cd_apl);
/* 456 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_apl);
/* 457 */     removeAuthUserPermission(cd, true);
/* 458 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityLink2(String name, int depth) {
/* 462 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 463 */     AccessPermissionList cd_dapl = Factory.AccessPermission.createList();
/* 464 */     addAccessPermissionsAdminsAndManagers(cd_dapl, depth, 983315);
/* 465 */     addAccessPermission(this.privUserGroup, 983315, AccessType.ALLOW, depth, cd_dapl);
/* 466 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_dapl);
/* 467 */     cd.set_DefaultInstancePermissions(cd_dapl);
/*     */     
/* 469 */     AccessPermissionList cd_apl = cd.get_Permissions();
/* 470 */     addAccessPermission(this.managerGroup, 983827, AccessType.ALLOW, depth, cd_apl);
/* 471 */     addAccessPermission(this.privUserGroup, 983827, AccessType.ALLOW, depth, cd_apl);
/* 472 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_apl);
/* 473 */     removeAuthUserPermission(cd, true);
/* 474 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityLink3(String name, int depth) {
/* 478 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 479 */     AccessPermissionList cd_dapl = Factory.AccessPermission.createList();
/* 480 */     addAccessPermissionsAdminsAndManagers(cd_dapl, depth, 983315);
/* 481 */     addAccessPermission(this.privUserGroup, 131073, AccessType.ALLOW, depth, cd_dapl);
/* 482 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_dapl);
/* 483 */     cd.set_DefaultInstancePermissions(cd_dapl);
/*     */     
/* 485 */     AccessPermissionList cd_apl = cd.get_Permissions();
/* 486 */     addAccessPermission(this.privUserGroup, 131073, AccessType.ALLOW, depth, cd_apl);
/* 487 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_apl);
/* 488 */     removeAuthUserPermission(cd, true);
/* 489 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setClassDefSecurityLink4(String name, int depth) {
/* 493 */     ClassDefinition cd = Factory.ClassDefinition.fetchInstance(this.objStore, name, null);
/* 494 */     AccessPermissionList cd_dapl = Factory.AccessPermission.createList();
/* 495 */     addAccessPermissionsAdminsAndManagers(cd_dapl, depth, 983315);
/* 496 */     addAccessPermission(this.privUserGroup, 131073, AccessType.ALLOW, depth, cd_dapl);
/* 497 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_dapl);
/* 498 */     cd.set_DefaultInstancePermissions(cd_dapl);
/*     */     
/* 500 */     AccessPermissionList cd_apl = cd.get_Permissions();
/* 501 */     addAccessPermission(this.managerGroup, 983827, AccessType.ALLOW, depth, cd_apl);
/* 502 */     addAccessPermission(this.privUserGroup, 131073, AccessType.ALLOW, depth, cd_apl);
/* 503 */     addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, depth, cd_apl);
/* 504 */     removeAuthUserPermission(cd, true);
/* 505 */     this.updatingBatch.add(cd, null);
/*     */   }
/*     */   
/*     */   protected void setCreatorOwnerAsOnlyDefaultInstance(ClassDefinition cd)
/*     */   {
/* 510 */     AccessPermissionList new_apl = Factory.AccessPermission.createList();
/* 511 */     addSingleAccessPermission("#CREATOR-OWNER", 983827, AccessType.ALLOW, 0, new_apl);
/*     */     
/* 513 */     cd.set_DefaultInstancePermissions(new_apl);
/*     */   }
/*     */   
/*     */   protected void removeAuthUserPermission(ClassDefinition cd, boolean removeCreatorOwner) {
/* 517 */     PropertyFilter propertyFilter = new PropertyFilter();
/* 518 */     propertyFilter.addIncludeProperty(1, null, Boolean.FALSE, "Permissions", null);
/* 519 */     propertyFilter.addIncludeProperty(1, null, Boolean.FALSE, "PermissionSource GranteeName", null);
/* 520 */     cd.fetchProperties(propertyFilter);
/*     */     
/* 522 */     Iterator apl_it = cd.get_Permissions().iterator();
/* 523 */     while (apl_it.hasNext()) {
/* 524 */       AccessPermission ap = (AccessPermission)apl_it.next();
/* 525 */       PermissionSource ps = ap.get_PermissionSource();
/* 526 */       if ((ap.get_GranteeName().equals("#AUTHENTICATED-USERS")) || ((removeCreatorOwner) && (ap.get_GranteeName().equals("#CREATOR-OWNER"))))
/*     */       {
/* 528 */         if ((ps == PermissionSource.SOURCE_DIRECT) || (ps == PermissionSource.SOURCE_DEFAULT)) {
/* 529 */           apl_it.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addAccessPermission(List<SecurityPrincipal> group, int accessmask, AccessType type, int inheritableDepth, AccessPermissionList apl) {
/* 536 */     Iterator<SecurityPrincipal> i = group.iterator();
/* 537 */     while (i.hasNext()) {
/* 538 */       AccessPermission ap = Factory.AccessPermission.createInstance();
/* 539 */       ap.set_AccessMask(Integer.valueOf(accessmask));
/* 540 */       ap.set_AccessType(type);
/* 541 */       ap.set_InheritableDepth(Integer.valueOf(inheritableDepth));
/* 542 */       SecurityPrincipal principal = (SecurityPrincipal)i.next();
/* 543 */       if ((principal instanceof User)) {
/* 544 */         ap.set_GranteeName(((User)principal).get_ShortName());
/* 545 */       } else if ((principal instanceof Group)) {
/* 546 */         ap.set_GranteeName(((Group)principal).get_ShortName());
/*     */       }
/* 548 */       apl.add(ap);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addSingleAccessPermission(String user, int accessmask, AccessType type, int inheritableDepth, AccessPermissionList apl)
/*     */   {
/* 554 */     AccessPermission ap = Factory.AccessPermission.createInstance();
/* 555 */     ap.set_AccessMask(Integer.valueOf(accessmask));
/* 556 */     ap.set_AccessType(type);
/* 557 */     ap.set_InheritableDepth(Integer.valueOf(inheritableDepth));
/* 558 */     ap.set_GranteeName(user);
/* 559 */     apl.add(ap);
/*     */   }
/*     */   
/*     */   protected void addAccessPermissionsMain(AccessPermissionList apl, int inheritableDepth, int al, int al1) {
/* 563 */     addAccessPermission(this.adminGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 564 */     addAccessPermission(this.managerGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 565 */     addAccessPermission(this.privUserGroup, al1, AccessType.ALLOW, inheritableDepth, apl);
/* 566 */     addAccessPermission(this.userGroup, al1, AccessType.ALLOW, inheritableDepth, apl);
/*     */   }
/*     */   
/*     */   protected void addAccessPermissionsMainPROPreventDelete(AccessPermissionList apl, int inheritableDepth, int al, int al1) {
/* 570 */     addAccessPermission(this.adminGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 571 */     addAccessPermission(this.privUserGroup, al1, AccessType.ALLOW, inheritableDepth, apl);
/* 572 */     addAccessPermission(this.userGroup, al1, AccessType.ALLOW, inheritableDepth, apl);
/*     */   }
/*     */   
/*     */   protected void addAccessPermissionsCGA(AccessPermissionList apl, int inheritableDepth, int al) {
/* 576 */     addAccessPermission(this.guideAdminsGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/*     */   }
/*     */   
/*     */   protected void addAccessPermissionsMain(AccessPermissionList apl, int inheritableDepth, int al, int al1, int al2, int al3) {
/* 580 */     addAccessPermission(this.adminGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 581 */     addAccessPermission(this.managerGroup, al1, AccessType.ALLOW, inheritableDepth, apl);
/* 582 */     addAccessPermission(this.privUserGroup, al2, AccessType.ALLOW, inheritableDepth, apl);
/* 583 */     addAccessPermission(this.userGroup, al3, AccessType.ALLOW, inheritableDepth, apl);
/*     */   }
/*     */   
/*     */   protected void addAccessPermissionsMain(AccessPermissionList apl, int inheritableDepth, int al) {
/* 587 */     addAccessPermission(this.adminGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 588 */     addAccessPermission(this.managerGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 589 */     addAccessPermission(this.privUserGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 590 */     addAccessPermission(this.userGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/*     */   }
/*     */   
/*     */   protected void addAccessPermissionsAdminsAndManagers(AccessPermissionList apl, int inheritableDepth, int al) {
/* 594 */     addAccessPermission(this.adminGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/* 595 */     addAccessPermission(this.managerGroup, al, AccessType.ALLOW, inheritableDepth, apl);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\security\ObjectStoreSecurityUpdater.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */