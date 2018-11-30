/*     */ package com.ibm.ier.plugin.util.security;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.FolderSet;
/*     */ import com.filenet.api.constants.AccessRight;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.core.Factory.ClassDefinition;
/*     */ import com.filenet.api.core.Factory.User;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*     */ import com.filenet.api.core.UpdatingBatch;
/*     */ import com.filenet.api.security.Marking;
/*     */ import com.filenet.api.security.MarkingSet;
/*     */ import com.filenet.api.security.SecurityPrincipal;
/*     */ import com.filenet.api.util.UserContext;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ObjectStoreBaseSecurity extends ObjectStoreSecurityUpdater
/*     */ {
/*     */   protected static final String MS_PREVENT_DELETE = "Prevent RM Entity Deletion";
/*     */   protected static final String PREVENT_DELETE = "Prevent Delete";
/*     */   protected static final String DEFAULT = "Default";
/*     */   protected static final String RECORDS_MANAG_FOLDER_PATH = "/Records Management";
/*     */   protected static final String TRANFER_MAPPING_FOLDER_PATH = "/Records Management/RMTransferMappings";
/*     */   public static final String CustomObject_SecurityRunDataSystemConfiguration = "{C83A89E1-95EE-4E62-8B54-FEE57DBDBD3E}";
/*     */   public static final String ResetDeclassificationDateID = "{C6FDF762-52BC-4668-AA84-6A14D7A45B1D}";
/*     */   public static final String DeclassificationTimeFrameID = "{77C87E45-7BF0-40C0-BEC2-ECE139FB3459}";
/*     */   
/*     */   public ObjectStoreBaseSecurity(String osname, List<SecurityPrincipal>[] rmGroups, Connection conn) throws Exception
/*     */   {
/*  39 */     super(osname, rmGroups, conn);
/*     */   }
/*     */   
/*     */   protected void setMarkingSetRelatedSecurity() throws Exception {
/*  43 */     updateDeletePreventMarkingSecurity();
/*     */   }
/*     */   
/*     */   protected void setObjectStoreRelatedSecurity() throws Exception
/*     */   {
/*  48 */     applyBaseDefaultInstanceSecurity();
/*     */   }
/*     */   
/*     */   protected void updateDeletePreventMarkingSecurity() throws Exception
/*     */   {
/*  53 */     Iterator markingSets_it = this.objStore.get_Domain().get_MarkingSets().iterator();
/*  54 */     MarkingSet deleteMS = null;
/*  55 */     while (markingSets_it.hasNext()) {
/*  56 */       MarkingSet ms = (MarkingSet)markingSets_it.next();
/*  57 */       if (ms.get_DisplayName().equals("Prevent RM Entity Deletion")) {
/*  58 */         deleteMS = ms;
/*  59 */         break;
/*     */       }
/*     */     }
/*     */     
/*  63 */     if (deleteMS == null) {
/*  64 */       throw com.ibm.ier.plugin.nls.IERUIRuntimeException.createUIRuntimeException(com.ibm.ier.plugin.nls.MessageCode.E_MARKINGSET_MISSING, new Object[] { "Prevent RM Entity Deletion" });
/*     */     }
/*     */     
/*  67 */     Marking preventDelete = null;
/*  68 */     Marking defaultM = null;
/*     */     
/*  70 */     Iterator markings_it = deleteMS.get_Markings().iterator();
/*  71 */     while (markings_it.hasNext()) {
/*  72 */       Marking ms = (Marking)markings_it.next();
/*  73 */       if (ms.get_MarkingValue().equals("Prevent Delete")) {
/*  74 */         preventDelete = ms;
/*     */       }
/*  76 */       if (ms.get_MarkingValue().equals("Default")) {
/*  77 */         defaultM = ms;
/*     */       }
/*     */     }
/*     */     
/*  81 */     if ((defaultM != null) && (preventDelete != null)) {
/*  82 */       int addRemoveRights = AccessRight.ADD_MARKING.getValue() + AccessRight.REMOVE_MARKING.getValue();
/*  83 */       int addUseRights = AccessRight.ADD_MARKING.getValue() + AccessRight.USE_MARKING.getValue();
/*     */       
/*  85 */       addAccessPermissionsAdminsAndManagers(preventDelete.get_Permissions(), 0, addRemoveRights);
/*  86 */       addAccessPermissionsMain(defaultM.get_Permissions(), 0, addUseRights, AccessRight.ADD_MARKING.getValue());
/*     */     }
/*  88 */     this.updatingBatch.add(deleteMS, null);
/*     */   }
/*     */   
/*     */   protected void applyBaseDefaultInstanceSecurity()
/*     */     throws Exception
/*     */   {
/*  94 */     updateSecurityForRootClassDefinitions();
/*     */     
/*     */ 
/*  97 */     updateRecManagementFolderSecurity();
/*     */     
/*     */ 
/* 100 */     addAccessPermissionsAdminsAndManagers(this.objStore.get_Permissions(), 0, 586547200);
/* 101 */     this.updatingBatch.add(this.objStore, null);
/*     */     
/* 103 */     updateBaseRecordSecurities();
/*     */     
/* 105 */     updateBaseRecordFolderSecurities();
/*     */     
/* 107 */     updateBaseCustomObjectSecurities();
/*     */     
/* 109 */     updateBaseLinkSecurities(this.updatingBatch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void updateRecManagementFolderSecurity()
/*     */   {
/* 116 */     Folder recManagementFolder = com.filenet.api.core.Factory.Folder.fetchInstance(this.objStore, "/Records Management", null);
/*     */     
/* 118 */     Iterator folderSet_it = recManagementFolder.get_SubFolders().iterator();
/*     */     
/*     */ 
/* 121 */     while (folderSet_it.hasNext())
/*     */     {
/* 123 */       Folder folder = (Folder)folderSet_it.next();
/*     */       
/*     */ 
/* 126 */       if ((folder.get_Name().equals("Locations")) || (folder.get_Name().equals("Record Types")) || (folder.get_Name().equals("File Plan")) || (folder.get_Name().equals("Templates")) || (folder.get_Name().equals("Workflows")) || (folder.get_Name().equals("Report Definitions")) || (folder.get_Name().equals("RMMaster")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */         if ((folder.get_Name().equals("Workflows")) || (folder.get_Name().equals("Report Definitions")))
/*     */         {
/*     */ 
/* 136 */           Iterator docSet_it = folder.get_ContainedDocuments().iterator();
/* 137 */           while (docSet_it.hasNext())
/*     */           {
/* 139 */             Document wf_doc = (Document)docSet_it.next();
/* 140 */             AccessPermissionList wfdoc_apl = wf_doc.get_Permissions();
/* 141 */             addAccessPermissionsAdminsAndManagers(wfdoc_apl, 0, 986583);
/* 142 */             this.updatingBatch.add(wf_doc, null);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 147 */         if (folder.get_Name().equals("RMMaster"))
/*     */         {
/*     */ 
/* 150 */           Iterator master_subFolders_it = folder.get_SubFolders().iterator();
/* 151 */           while (master_subFolders_it.hasNext()) {
/* 152 */             Folder master_subFolder = (Folder)master_subFolders_it.next();
/*     */             
/* 154 */             if (master_subFolder.get_Name().equals("SystemConfiguration"))
/*     */             {
/*     */ 
/* 157 */               updateSysConfigFolderContainees(master_subFolder);
/*     */             }
/*     */             
/*     */ 
/* 161 */             AccessPermissionList master_apl = master_subFolder.get_Permissions();
/* 162 */             addAccessPermissionsAdminsAndManagers(master_apl, 0, 987127);
/* 163 */             this.updatingBatch.add(master_subFolder, null);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */         AccessPermissionList folder_apl = Factory.AccessPermission.createList();
/* 173 */         addAccessPermissionsMain(folder_apl, 0, 987127, 131073);
/* 174 */         folder.set_Permissions(folder_apl);
/* 175 */         this.updatingBatch.add(folder, null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 180 */     AccessPermissionList recManagementFolder_apl = Factory.AccessPermission.createList();
/* 181 */     addAccessPermissionsMain(recManagementFolder_apl, 0, 987127, 131073);
/* 182 */     recManagementFolder.set_Permissions(recManagementFolder_apl);
/* 183 */     this.updatingBatch.add(recManagementFolder, null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void updateSysConfigFolderContainees(Folder folder)
/*     */   {
/* 189 */     Iterator containee_it = folder.get_Containees().iterator();
/* 190 */     while (containee_it.hasNext()) {
/* 191 */       CustomObject customObject = (CustomObject)((ReferentialContainmentRelationship)containee_it.next()).get_Head();
/*     */       
/*     */ 
/* 194 */       if (!customObject.get_Name().equalsIgnoreCase("{C83A89E1-95EE-4E62-8B54-FEE57DBDBD3E}"))
/*     */       {
/* 196 */         AccessPermissionList apl = customObject.get_Permissions();
/* 197 */         addAccessPermissionsAdminsAndManagers(apl, 0, 983315);
/* 198 */         this.updatingBatch.add(customObject, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateBaseRecordSecurities() throws Exception
/*     */   {
/* 205 */     setClassDefSecurityRecord1("RecordInfo", 0, false);
/*     */     
/*     */ 
/* 208 */     setClassDefSecurityRecord1("EmailRecordInfo", -1, true);
/*     */     
/*     */ 
/* 211 */     setClassDefSecurityRecord1("ElectronicRecordInfo", -1, true);
/*     */     
/*     */ 
/* 214 */     setClassDefSecurityRecord1("Markers", -1, true);
/*     */     
/*     */ 
/* 217 */     setClassDefSecurityRecord1(getClassDefinition("PDFRecord"), -1, true);
/*     */     
/*     */ 
/* 220 */     setClassDefSecurityRecord1(getClassDefinition("WebRecord"), -1, true);
/*     */     
/*     */ 
/* 223 */     setClassDefSecurityRecord1(getClassDefinition("ScannedRecord"), -1, true);
/*     */     
/*     */ 
/* 226 */     setClassDefSecurityRecord1(getClassDefinition("DigitalPhotographRecord"), -1, true);
/*     */   }
/*     */   
/*     */   protected void updateBaseRecordFolderSecurities()
/*     */   {
/* 231 */     ClassDefinition cSsCD = Factory.ClassDefinition.fetchInstance(this.objStore, "ClassificationScheme", null);
/* 232 */     AccessPermissionList cs_dapl = Factory.AccessPermission.createList();
/* 233 */     addAccessPermissionsMain(cs_dapl, 0, 987127, 131073);
/* 234 */     cSsCD.set_DefaultInstancePermissions(cs_dapl);
/*     */     
/* 236 */     removeAuthUserPermission(cSsCD, false);
/* 237 */     addAccessPermissionsMain(cSsCD.get_Permissions(), 0, 987127, 1);
/* 238 */     this.updatingBatch.add(cSsCD, null);
/*     */     
/*     */ 
/* 241 */     ClassDefinition cSCD = Factory.ClassDefinition.fetchInstance(this.objStore, "ClassificationSchemes", null);
/* 242 */     AccessPermissionList class_schemes_dapl = Factory.AccessPermission.createList();
/* 243 */     addAccessPermissionsMain(class_schemes_dapl, 0, 987127, 131073);
/* 244 */     cSCD.set_DefaultInstancePermissions(class_schemes_dapl);
/* 245 */     this.updatingBatch.add(cSCD, null);
/*     */     
/*     */ 
/* 248 */     ClassDefinition recordCategory = Factory.ClassDefinition.fetchInstance(this.objStore, "RecordCategory", null);
/* 249 */     AccessPermissionList recordCat_apl = Factory.AccessPermission.createList();
/* 250 */     addAccessPermissionsMain(recordCat_apl, -1, 987127, 987127, 135159, AccessRight.VIEW_CONTENT.getValue() + 131121);
/*     */     
/* 252 */     recordCategory.set_DefaultInstancePermissions(recordCat_apl);
/*     */     
/* 254 */     removeAuthUserPermission(recordCategory, false);
/* 255 */     addAccessPermissionsMain(recordCategory.get_Permissions(), -1, 983827, 131073);
/* 256 */     this.updatingBatch.add(recordCategory, null);
/*     */     
/*     */ 
/* 259 */     setClassDefSecurityFolder1("RecordFolder", 0, false);
/*     */     
/*     */ 
/* 262 */     setClassDefSecurityFolder1("ElectronicRecordFolder", -1, true);
/*     */     
/*     */ 
/* 265 */     setClassDefSecurityFolder1("HybridRecordFolder", -1, true);
/*     */     
/*     */ 
/* 268 */     setClassDefSecurityFolder1("Box", -1, true);
/*     */     
/*     */ 
/* 271 */     setClassDefSecurityFolder1("PhysicalBox", -1, true);
/*     */     
/*     */ 
/* 274 */     setClassDefSecurityFolder1("PhysicalRecordFolder", -1, true);
/*     */     
/*     */ 
/* 277 */     ClassDefinition volume = Factory.ClassDefinition.fetchInstance(this.objStore, "Volume", null);
/* 278 */     volume.set_DefaultInstancePermissions(Factory.AccessPermission.createList());
/* 279 */     addAccessPermissionsAdminsAndManagers(volume.get_Permissions(), -1, 135159);
/* 280 */     this.updatingBatch.add(volume, null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void updateBaseCustomObjectSecurities()
/*     */   {
/* 286 */     setClassDefSecurityCustom1("RecordType", 0);
/*     */     
/*     */ 
/* 289 */     setClassDefSecurityCustom1("DisposalSchedule", 0);
/*     */     
/*     */ 
/* 292 */     ClassDefinition auditMeta_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "AuditMetadataConfiguration", null);
/* 293 */     AccessPermissionList auditMeta_cd_dapl = Factory.AccessPermission.createList();
/* 294 */     addAccessPermissionsMain(auditMeta_cd_dapl, 0, 983315, 131073, 131073, 131073);
/*     */     
/* 296 */     auditMeta_cd.set_DefaultInstancePermissions(auditMeta_cd_dapl);
/*     */     
/* 298 */     addAccessPermissionsMain(auditMeta_cd.get_Permissions(), 0, 983827, 131073, 131073, 131073);
/*     */     
/* 300 */     removeAuthUserPermission(auditMeta_cd, true);
/* 301 */     this.updatingBatch.add(auditMeta_cd, null);
/*     */     
/*     */ 
/* 304 */     setClassDefSecurityCustom1("Action1", 0);
/*     */     
/*     */ 
/* 307 */     setClassDefSecurityCustom1("Phase", 0);
/*     */     
/*     */ 
/* 310 */     setClassDefSecurityCustom1("AlternateRetention", 0);
/*     */     
/*     */ 
/* 313 */     setClassDefSecurityCustom1("Exception", 0);
/*     */     
/*     */ 
/* 316 */     setClassDefSecurityCustom1("DisposalTrigger", 0);
/*     */     
/*     */ 
/* 319 */     setClassDefSecurityCustom1("DisposalPhase", 0);
/*     */     
/*     */ 
/* 322 */     setClassDefSecurityCustom1("RecordHold", 0);
/*     */     
/*     */ 
/* 325 */     setClassDefSecurityCustom1("Report", 0);
/*     */     
/*     */ 
/* 328 */     setClassDefSecurityCustom1("Pattern", 0);
/*     */     
/*     */ 
/* 331 */     setClassDefSecurityCustom1("PatternLevel", 0);
/*     */     
/*     */ 
/* 334 */     ClassDefinition patternSequences_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "PatternSequences", null);
/* 335 */     AccessPermissionList patternSequencescd_dapl = Factory.AccessPermission.createList();
/* 336 */     addAccessPermissionsMain(patternSequencescd_dapl, 0, 983315, 983315);
/* 337 */     patternSequences_cd.set_DefaultInstancePermissions(patternSequencescd_dapl);
/*     */     
/* 339 */     addAccessPermissionsMain(patternSequences_cd.get_Permissions(), 0, 983827, 983827);
/*     */     
/* 341 */     removeAuthUserPermission(patternSequences_cd, true);
/* 342 */     this.updatingBatch.add(patternSequences_cd, null);
/*     */     
/*     */ 
/* 345 */     setClassDefSecurityCustom1("Location", 0);
/*     */     
/*     */ 
/* 348 */     setClassDefSecurityCustom1("SystemConfiguration", 0);
/*     */   }
/*     */   
/*     */   protected void updateBaseLinkSecurities(UpdatingBatch ub)
/*     */   {
/* 353 */     setClassDefSecurityLink1("ExtractLink", -1);
/*     */     
/*     */ 
/* 356 */     setClassDefSecurityLink1("HybridFolderLink", -1);
/*     */     
/*     */ 
/* 359 */     setClassDefSecurityLink1("RecordCopyLink", -1);
/*     */     
/*     */ 
/* 362 */     setClassDefSecurityLink1("RecordFolderSeeAlsoLink", -1);
/*     */     
/*     */ 
/* 365 */     setClassDefSecurityLink1("RecordSeeAlsoLink", -1);
/*     */     
/*     */ 
/* 368 */     setClassDefSecurityLink1("ReferenceLink", -1);
/*     */     
/*     */ 
/* 371 */     setClassDefSecurityLink1("RenditionLink", -1);
/*     */     
/*     */ 
/* 374 */     setClassDefSecurityLink3("RecordHoldLink", -1);
/*     */     
/*     */ 
/* 377 */     setClassDefSecurityLink3("RMFolderHoldLink", -1);
/*     */   }
/*     */   
/*     */   protected void updateSecurityForRootClassDefinitions()
/*     */   {
/* 382 */     Iterator cds_it = this.objStore.get_RootClassDefinitions().iterator();
/* 383 */     while (cds_it.hasNext()) {
/* 384 */       ClassDefinition root_cd = (ClassDefinition)cds_it.next();
/* 385 */       if ((!root_cd.get_Name().equals("Document")) && (!root_cd.get_Name().equals("Folder")) && (!root_cd.get_Name().equals("Custom Object"))) {
/* 386 */         addAccessPermission(this.adminGroup, 983827, AccessType.ALLOW, -1, root_cd.get_Permissions());
/*     */         
/* 388 */         if (root_cd.get_Name().equals("Link"))
/*     */         {
/* 390 */           addAccessPermission(this.managerGroup, 983827, AccessType.ALLOW, -1, root_cd.get_Permissions());
/*     */         }
/*     */       }
/*     */       
/* 394 */       this.updatingBatch.add(root_cd, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception
/*     */   {
/* 400 */     Connection conn = com.filenet.api.core.Factory.Connection.getConnection("http://9.68.68.113:9080/wsi/FNCEWS40MTOM");
/* 401 */     UserContext.get().pushSubject(UserContext.createSubject(conn, "administrator", "Passw0rd1", null));
/*     */     
/*     */ 
/* 404 */     List<SecurityPrincipal>[] rmGroups = new List[5];
/* 405 */     rmGroups[0] = null;
/*     */     
/* 407 */     List<SecurityPrincipal> group = new ArrayList();
/* 408 */     com.filenet.api.security.User user = Factory.User.fetchInstance(conn, "Auditor", null);
/* 409 */     group.add(user);
/* 410 */     rmGroups[1] = group;
/*     */     
/* 412 */     group = new ArrayList();
/* 413 */     user = Factory.User.fetchInstance(conn, "EDA_USER", null);
/* 414 */     group.add(user);
/* 415 */     rmGroups[2] = group;
/*     */     
/* 417 */     group = new ArrayList();
/* 418 */     user = Factory.User.fetchInstance(conn, "CaseReviewer", null);
/* 419 */     group.add(user);
/* 420 */     rmGroups[3] = group;
/*     */     
/* 422 */     group = new ArrayList();
/* 423 */     user = Factory.User.fetchInstance(conn, "Guest", null);
/* 424 */     group.add(user);
/* 425 */     rmGroups[4] = group;
/*     */     
/*     */ 
/*     */ 
/* 429 */     ObjectStoreSecurityUpdater securityUpdater = new ObjectStoreBaseSecurity("BaseFPOS", rmGroups, conn);
/* 430 */     securityUpdater.setEntireSecurity();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\security\ObjectStoreBaseSecurity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */