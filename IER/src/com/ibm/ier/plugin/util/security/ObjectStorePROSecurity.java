/*     */ package com.ibm.ier.plugin.util.security;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.admin.PropertyDefinition;
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.ClassDefinitionSet;
/*     */ import com.filenet.api.collection.MarkingList;
/*     */ import com.filenet.api.collection.MarkingSetSet;
/*     */ import com.filenet.api.collection.PropertyDefinitionList;
/*     */ import com.filenet.api.constants.AccessRight;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.core.Factory.ClassDefinition;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.UpdatingBatch;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.filenet.api.security.Marking;
/*     */ import com.filenet.api.security.MarkingSet;
/*     */ import com.filenet.api.security.SecurityPrincipal;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
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
/*     */ public class ObjectStorePROSecurity
/*     */   extends ObjectStoreBaseSecurity
/*     */ {
/*     */   public ObjectStorePROSecurity(String osname, List<SecurityPrincipal>[] rmGroups, Connection con)
/*     */     throws Exception
/*     */   {
/*  39 */     super(osname, rmGroups, con);
/*     */   }
/*     */   
/*     */   protected void setMarkingSetRelatedSecurity() throws Exception {
/*  43 */     updateDeletePreventMarkingSecurityPRO();
/*  44 */     updateSecurityCategoryMarkingSecurity();
/*     */   }
/*     */   
/*     */   protected void setObjectStoreRelatedSecurity() throws Exception {
/*  48 */     applyPRODefaultInstanceSecurity();
/*     */   }
/*     */   
/*     */   private void updateSecurityCategoryMarkingSecurity() throws Exception
/*     */   {
/*  53 */     Iterator markingSets_it = this.objStore.get_Domain().get_MarkingSets().iterator();
/*  54 */     MarkingSet securityCategoriesMS = null;
/*  55 */     while (markingSets_it.hasNext()) {
/*  56 */       MarkingSet ms = (MarkingSet)markingSets_it.next();
/*  57 */       if (ms.get_DisplayName().equals("Security Categories")) {
/*  58 */         securityCategoriesMS = ms;
/*  59 */         break;
/*     */       }
/*     */     }
/*     */     
/*  63 */     if (securityCategoriesMS == null) {
/*  64 */       throw IERUIRuntimeException.createUIRuntimeException("error.missingMarkingSet", new Object[] { "Security Categories" });
/*     */     }
/*  66 */     Marking unclassifiedM = null;
/*  67 */     Marking restrictedM = null;
/*  68 */     Marking confidentialM = null;
/*  69 */     Marking topSecretM = null;
/*  70 */     Marking secretM = null;
/*  71 */     Iterator markings_it = securityCategoriesMS.get_Markings().iterator();
/*  72 */     while (markings_it.hasNext()) {
/*  73 */       Marking ms = (Marking)markings_it.next();
/*  74 */       if (ms.get_MarkingValue().equals("Unclassified")) {
/*  75 */         unclassifiedM = ms;
/*     */       }
/*  77 */       if (ms.get_MarkingValue().equals("Top Secret")) {
/*  78 */         topSecretM = ms;
/*     */       }
/*  80 */       if (ms.get_MarkingValue().equals("Secret")) {
/*  81 */         secretM = ms;
/*     */       }
/*  83 */       if (ms.get_MarkingValue().equals("Restricted")) {
/*  84 */         restrictedM = ms;
/*     */       }
/*  86 */       if (ms.get_MarkingValue().equals("Confidential")) {
/*  87 */         confidentialM = ms;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  92 */     if ((unclassifiedM != null) && (restrictedM != null) && (confidentialM != null) && (topSecretM != null) && (secretM != null)) {
/*  93 */       int allMarkings = AccessRight.ADD_MARKING.getValue() + AccessRight.REMOVE_MARKING.getValue() + AccessRight.USE_MARKING.getValue();
/*  94 */       addAccessPermissionsMain(unclassifiedM.get_Permissions(), 0, allMarkings);
/*  95 */       addAccessPermission(this.adminGroup, allMarkings, AccessType.ALLOW, 0, restrictedM.get_Permissions());
/*  96 */       addAccessPermission(this.adminGroup, allMarkings, AccessType.ALLOW, 0, confidentialM.get_Permissions());
/*  97 */       addAccessPermission(this.adminGroup, allMarkings, AccessType.ALLOW, 0, topSecretM.get_Permissions());
/*  98 */       addAccessPermission(this.adminGroup, allMarkings, AccessType.ALLOW, 0, secretM.get_Permissions());
/*     */     }
/* 100 */     this.updatingBatch.add(securityCategoriesMS, null);
/*     */   }
/*     */   
/*     */   private void updateDeletePreventMarkingSecurityPRO() throws Exception
/*     */   {
/* 105 */     Iterator markingSets_it = this.objStore.get_Domain().get_MarkingSets().iterator();
/* 106 */     MarkingSet deleteMS = null;
/* 107 */     while (markingSets_it.hasNext()) {
/* 108 */       MarkingSet ms = (MarkingSet)markingSets_it.next();
/* 109 */       if (ms.get_DisplayName().equals("Prevent RM Entity Deletion PRO")) {
/* 110 */         deleteMS = ms;
/* 111 */         break;
/*     */       }
/*     */     }
/*     */     
/* 115 */     if (deleteMS == null) {
/* 116 */       throw IERUIRuntimeException.createUIRuntimeException("error.missingMarkingSet", new Object[] { "Prevent RM Entity Deletion" });
/*     */     }
/* 118 */     Marking preventDelete = null;
/* 119 */     Marking defaultM = null;
/* 120 */     Iterator markings_it = deleteMS.get_Markings().iterator();
/* 121 */     while (markings_it.hasNext()) {
/* 122 */       Marking ms = (Marking)markings_it.next();
/* 123 */       if (ms.get_MarkingValue().equals("Prevent Delete")) {
/* 124 */         preventDelete = ms;
/*     */       }
/* 126 */       if (ms.get_MarkingValue().equals("Default")) {
/* 127 */         defaultM = ms;
/*     */       }
/*     */     }
/*     */     
/* 131 */     if ((defaultM != null) && (preventDelete != null)) {
/* 132 */       int addRemoveRights = AccessRight.ADD_MARKING.getValue() + AccessRight.REMOVE_MARKING.getValue();
/* 133 */       int addUseRights = AccessRight.ADD_MARKING.getValue() + AccessRight.USE_MARKING.getValue();
/*     */       
/*     */ 
/* 136 */       addAccessPermission(this.adminGroup, addRemoveRights, AccessType.ALLOW, 0, preventDelete.get_Permissions());
/* 137 */       addAccessPermission(this.managerGroup, addRemoveRights, AccessType.ALLOW, 0, preventDelete.get_Permissions());
/*     */       
/*     */ 
/* 140 */       addAccessPermissionsMainPROPreventDelete(defaultM.get_Permissions(), 0, addUseRights, AccessRight.ADD_MARKING.getValue());
/*     */       
/* 142 */       this.updatingBatch.add(deleteMS, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void applyPRODefaultInstanceSecurity() throws Exception
/*     */   {
/* 148 */     updateSecurityForRootClassDefinitions();
/*     */     
/*     */ 
/* 151 */     updateRecManagementFolderSecurity();
/*     */     
/*     */ 
/* 154 */     addAccessPermissionsAdminsAndManagers(this.objStore.get_Permissions(), 0, 586547200);
/* 155 */     this.updatingBatch.add(this.objStore, null);
/*     */     
/*     */ 
/* 158 */     ClassDefinition cSsCD = Factory.ClassDefinition.fetchInstance(this.objStore, "ClassificationScheme", null);
/* 159 */     AccessPermissionList cs_dapl = Factory.AccessPermission.createList();
/* 160 */     addAccessPermissionsMain(cs_dapl, 0, 987127, 131073);
/* 161 */     cSsCD.set_DefaultInstancePermissions(cs_dapl);
/*     */     
/* 163 */     removeAuthUserPermission(cSsCD, false);
/* 164 */     addAccessPermissionsMain(cSsCD.get_Permissions(), 0, 983827, 1);
/* 165 */     this.updatingBatch.add(cSsCD, null);
/*     */     
/*     */ 
/* 168 */     ClassDefinition cSCD = Factory.ClassDefinition.fetchInstance(this.objStore, "ClassificationSchemes", null);
/* 169 */     AccessPermissionList class_schemes_dapl = Factory.AccessPermission.createList();
/* 170 */     addAccessPermissionsMain(class_schemes_dapl, 0, 987127, 131073);
/* 171 */     cSCD.set_DefaultInstancePermissions(class_schemes_dapl);
/* 172 */     this.updatingBatch.add(cSCD, null);
/*     */     
/*     */ 
/* 175 */     ClassDefinition recordCategory = Factory.ClassDefinition.fetchInstance(this.objStore, "RecordCategory", null);
/* 176 */     Iterator allowedRMContainees_it = recordCategory.get_PropertyDefinitions().iterator();
/* 177 */     while (allowedRMContainees_it.hasNext()) {
/* 178 */       PropertyDefinition pd = (PropertyDefinition)allowedRMContainees_it.next();
/* 179 */       if (pd.get_SymbolicName().equals("AllowedRMContainees")) {
/* 180 */         pd.set_ModificationAccessRequired(Integer.valueOf(AccessRight.CREATE_CHILD.getValue()));
/*     */       }
/*     */     }
/*     */     
/* 184 */     AccessPermissionList recordCat_dapl = Factory.AccessPermission.createList();
/* 185 */     addAccessPermissionsAdminsAndManagers(recordCat_dapl, -1, 987127);
/* 186 */     addAccessPermission(this.privUserGroup, 135159, AccessType.ALLOW, -1, recordCat_dapl);
/* 187 */     addAccessPermission(this.privUserGroup, AccessRight.CREATE_CHILD.getValue(), AccessType.DENY, -1, recordCat_dapl);
/* 188 */     addAccessPermission(this.userGroup, AccessRight.VIEW_CONTENT.getValue() + 131121 + AccessRight.CREATE_CHILD.getValue(), AccessType.ALLOW, -1, recordCat_dapl);
/*     */     
/* 190 */     recordCategory.set_DefaultInstancePermissions(recordCat_dapl);
/*     */     
/* 192 */     AccessPermissionList recordCat_apl = recordCategory.get_Permissions();
/* 193 */     addAccessPermissionsAdminsAndManagers(recordCat_apl, -1, 983827);
/* 194 */     Iterator record_apl_it = recordCat_apl.iterator();
/* 195 */     while (record_apl_it.hasNext()) {
/* 196 */       AccessPermission recordCat_ap = (AccessPermission)record_apl_it.next();
/* 197 */       if (recordCat_ap.get_GranteeName().equals("#AUTHENTICATED-USERS"))
/* 198 */         recordCat_ap.set_AccessMask(Integer.valueOf(recordCat_ap.get_AccessMask().intValue() - AccessRight.CREATE_INSTANCE.getValue()));
/*     */     }
/* 200 */     addAccessPermission(this.userGroup, AccessRight.READ.getValue(), AccessType.ALLOW, -1, recordCat_apl);
/* 201 */     this.updatingBatch.add(recordCategory, null);
/*     */     
/*     */ 
/* 204 */     setClassDefSecurityFolder1("RecordFolder", 0, false);
/*     */     
/*     */ 
/* 207 */     setClassDefSecurityFolder2("ElectronicRecordFolder", -1);
/*     */     
/*     */ 
/* 210 */     setClassDefSecurityFolder2("HybridRecordFolder", -1);
/*     */     
/*     */ 
/* 213 */     setClassDefSecurityFolder2("Box", -1);
/*     */     
/*     */ 
/* 216 */     setClassDefSecurityFolder1("PhysicalBox", -1, true);
/*     */     
/*     */ 
/* 219 */     setClassDefSecurityFolder2("PhysicalRecordFolder", -1);
/*     */     
/*     */ 
/* 222 */     setClassDefSecurityFolder1("Volume", -1, true);
/*     */     
/* 224 */     updateBaseRecordSecurities();
/* 225 */     updatePROCustomObjectSecurities();
/*     */     
/*     */ 
/* 228 */     setClassDefSecurityLink2("ExtractLink", -1);
/*     */     
/*     */ 
/* 231 */     setClassDefSecurityLink2("HybridFolderLink", -1);
/*     */     
/*     */ 
/* 234 */     ClassDefinition recordCopyLink_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "RecordCopyLink", null);
/* 235 */     AccessPermissionList recordCopyLink_cd_dapl = Factory.AccessPermission.createList();
/* 236 */     addAccessPermissionsAdminsAndManagers(recordCopyLink_cd_dapl, -1, 983315);
/* 237 */     addAccessPermission(this.privUserGroup, 983315, AccessType.ALLOW, -1, recordCopyLink_cd_dapl);
/* 238 */     addAccessPermission(this.userGroup, 983315, AccessType.ALLOW, -1, recordCopyLink_cd_dapl);
/* 239 */     recordCopyLink_cd.set_DefaultInstancePermissions(recordCopyLink_cd_dapl);
/*     */     
/* 241 */     AccessPermissionList recordCopyLink_cd_apl = recordCopyLink_cd.get_Permissions();
/* 242 */     addAccessPermission(this.managerGroup, 983827, AccessType.ALLOW, -1, recordCopyLink_cd_apl);
/* 243 */     addAccessPermission(this.privUserGroup, 983827, AccessType.ALLOW, -1, recordCopyLink_cd_apl);
/* 244 */     addAccessPermission(this.userGroup, 983827, AccessType.ALLOW, -1, recordCopyLink_cd_apl);
/* 245 */     removeAuthUserPermission(recordCopyLink_cd, true);
/* 246 */     this.updatingBatch.add(recordCopyLink_cd, null);
/*     */     
/*     */ 
/* 249 */     setClassDefSecurityLink2("RecordFolderSeeAlsoLink", -1);
/*     */     
/*     */ 
/* 252 */     setClassDefSecurityLink2("RecordSeeAlsoLink", -1);
/*     */     
/*     */ 
/* 255 */     setClassDefSecurityLink2("ReferenceLink", -1);
/*     */     
/*     */ 
/* 258 */     setClassDefSecurityLink2("RenditionLink", -1);
/*     */     
/*     */ 
/* 261 */     setClassDefSecurityLink4("RecordHoldLink", -1);
/*     */     
/*     */ 
/* 264 */     setClassDefSecurityLink4("RMFolderHoldLink", -1);
/*     */   }
/*     */   
/*     */   protected void updateSecurityForRootClassDefinitionsPRO()
/*     */   {
/* 269 */     Iterator cds_it = this.objStore.get_RootClassDefinitions().iterator();
/* 270 */     while (cds_it.hasNext()) {
/* 271 */       ClassDefinition root_cd = (ClassDefinition)cds_it.next();
/* 272 */       if ((!root_cd.get_Name().equals("Document")) && (!root_cd.get_Name().equals("Folder")) && (!root_cd.get_Name().equals("Custom Object"))) {
/* 273 */         addAccessPermission(this.adminGroup, 983827, AccessType.ALLOW, -1, root_cd.get_Permissions());
/*     */         
/*     */ 
/* 276 */         addAccessPermission(this.managerGroup, 983827, AccessType.ALLOW, -1, root_cd.get_Permissions());
/*     */       }
/*     */       
/* 279 */       this.updatingBatch.add(root_cd, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updatePROCustomObjectSecurities()
/*     */   {
/* 285 */     setClassDefSecurityCustom1("RecordType", 0);
/*     */     
/*     */ 
/* 288 */     ClassDefinition disposalSchedule_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "DisposalSchedule", null);
/* 289 */     AccessPermissionList disposalSchedule_cd_dapl = Factory.AccessPermission.createList();
/* 290 */     addAccessPermissionsMain(disposalSchedule_cd_dapl, 0, 983315, 131073);
/* 291 */     disposalSchedule_cd.set_DefaultInstancePermissions(disposalSchedule_cd_dapl);
/*     */     
/* 293 */     AccessPermissionList disposalSchedule_cd_apl = disposalSchedule_cd.get_Permissions();
/* 294 */     addAccessPermissionsAdminsAndManagers(disposalSchedule_cd_apl, 0, 983827);
/* 295 */     addAccessPermission(this.privUserGroup, 131073, AccessType.ALLOW, 0, disposalSchedule_cd_apl);
/* 296 */     addAccessPermission(this.userGroup, 131073, AccessType.DENY, 0, disposalSchedule_cd_apl);
/* 297 */     removeAuthUserPermission(disposalSchedule_cd, true);
/* 298 */     this.updatingBatch.add(disposalSchedule_cd, null);
/*     */     
/*     */ 
/* 301 */     ClassDefinition auditMeta_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "AuditMetadataConfiguration", null);
/* 302 */     AccessPermissionList auditMeta_cd_dapl = Factory.AccessPermission.createList();
/* 303 */     addAccessPermissionsMain(auditMeta_cd_dapl, 0, 983315, 131073, 131073, 131073);
/*     */     
/* 305 */     auditMeta_cd.set_DefaultInstancePermissions(auditMeta_cd_dapl);
/*     */     
/* 307 */     addAccessPermissionsMain(auditMeta_cd.get_Permissions(), 0, 983827, 131073, 131073, 131073);
/*     */     
/* 309 */     removeAuthUserPermission(auditMeta_cd, true);
/* 310 */     this.updatingBatch.add(auditMeta_cd, null);
/*     */     
/*     */ 
/* 313 */     setClassDefSecurityCustom1("Action1", 0);
/*     */     
/*     */ 
/* 316 */     setClassDefSecurityCustom1("Phase", 0);
/*     */     
/*     */ 
/* 319 */     setClassDefSecurityCustom1("AlternateRetention", 0);
/*     */     
/*     */ 
/* 322 */     setClassDefSecurityCustom1("Exception", 0);
/*     */     
/*     */ 
/* 325 */     setClassDefSecurityCustom1("DisposalTrigger", 0);
/*     */     
/*     */ 
/* 328 */     setClassDefSecurityCustom1("DisposalPhase", 0);
/*     */     
/*     */ 
/* 331 */     setClassDefSecurityCustom1("RecordHold", 0);
/*     */     
/*     */ 
/* 334 */     setClassDefSecurityCustom1("Report", 0);
/*     */     
/*     */ 
/* 337 */     setClassDefSecurityCustom1("Pattern", 0);
/*     */     
/*     */ 
/* 340 */     setClassDefSecurityCustom1("PatternLevel", 0);
/*     */     
/*     */ 
/* 343 */     ClassDefinition patternSequences_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "PatternSequences", null);
/* 344 */     AccessPermissionList patternSequencescd_dapl = Factory.AccessPermission.createList();
/* 345 */     addAccessPermissionsMain(patternSequencescd_dapl, 0, 983315, 983315);
/* 346 */     patternSequences_cd.set_DefaultInstancePermissions(patternSequencescd_dapl);
/*     */     
/* 348 */     addAccessPermissionsMain(patternSequences_cd.get_Permissions(), 0, 983827, 983827);
/*     */     
/* 350 */     removeAuthUserPermission(patternSequences_cd, true);
/* 351 */     this.updatingBatch.add(patternSequences_cd, null);
/*     */     
/*     */ 
/* 354 */     setClassDefSecurityCustom1("Location", 0);
/*     */     
/*     */ 
/* 357 */     setClassDefSecurityCustom1("SystemConfiguration", 0);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\security\ObjectStorePROSecurity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */