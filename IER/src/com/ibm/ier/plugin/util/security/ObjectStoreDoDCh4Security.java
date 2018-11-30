/*     */ package com.ibm.ier.plugin.util.security;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.MarkingList;
/*     */ import com.filenet.api.collection.MarkingSetSet;
/*     */ import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
/*     */ import com.filenet.api.constants.AccessRight;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.core.Factory.ClassDefinition;
/*     */ import com.filenet.api.core.Factory.Folder;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.ReferentialContainmentRelationship;
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
/*     */ public class ObjectStoreDoDCh4Security
/*     */   extends ObjectStoreBaseSecurity
/*     */ {
/*     */   protected static final String CLASS_GUID_FOLDER_PATH = "/Records Management/Classification Guides";
/*     */   
/*     */   public ObjectStoreDoDCh4Security(String osname, List<SecurityPrincipal>[] rmGroups, Connection conn)
/*     */     throws Exception
/*     */   {
/*  42 */     super(osname, rmGroups, conn);
/*     */   }
/*     */   
/*     */   protected void setMarkingSetRelatedSecurity() throws Exception {
/*  46 */     updateDeletePreventMarkingSecurity();
/*  47 */     updateSecurityCategoryMarkingSecurityDODCH4();
/*     */   }
/*     */   
/*     */   protected void setObjectStoreRelatedSecurity() throws Exception {
/*  51 */     applyDoDCH4DefaultInstanceSecurity();
/*     */   }
/*     */   
/*     */   private void updateSecurityCategoryMarkingSecurityDODCH4() throws Exception
/*     */   {
/*  56 */     Iterator markingSets_it = this.objStore.get_Domain().get_MarkingSets().iterator();
/*  57 */     MarkingSet securityCategoriesMS = null;
/*  58 */     while (markingSets_it.hasNext()) {
/*  59 */       MarkingSet ms = (MarkingSet)markingSets_it.next();
/*  60 */       if (ms.get_DisplayName().equals("Security Categories DoD Classified")) {
/*  61 */         securityCategoriesMS = ms;
/*  62 */         break;
/*     */       }
/*     */     }
/*     */     
/*  66 */     if (securityCategoriesMS == null) {
/*  67 */       throw IERUIRuntimeException.createUIRuntimeException("error.missingMarkingSet", new Object[] { "Security Categories DoD Classified" });
/*     */     }
/*  69 */     Marking unclassifiedM = null;
/*  70 */     Iterator markings_it = securityCategoriesMS.get_Markings().iterator();
/*  71 */     while (markings_it.hasNext()) {
/*  72 */       Marking ms = (Marking)markings_it.next();
/*  73 */       if (ms.get_MarkingValue().equals("Unclassified")) {
/*  74 */         unclassifiedM = ms;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  79 */     if (unclassifiedM != null) {
/*  80 */       int allMarkings = AccessRight.ADD_MARKING.getValue() + AccessRight.REMOVE_MARKING.getValue() + AccessRight.USE_MARKING.getValue();
/*  81 */       addSingleAccessPermission("#AUTHENTICATED-USERS", allMarkings, AccessType.ALLOW, 0, unclassifiedM.get_Permissions());
/*     */     }
/*  83 */     this.updatingBatch.add(securityCategoriesMS, null);
/*     */   }
/*     */   
/*     */   private void applyDoDCH4DefaultInstanceSecurity()
/*     */     throws Exception
/*     */   {
/*  89 */     applyBaseDefaultInstanceSecurity();
/*     */     
/*     */ 
/*  92 */     ClassDefinition classGuide_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "ClassificationGuide", null);
/*  93 */     AccessPermissionList classGuide_dapl = Factory.AccessPermission.createList();
/*  94 */     addAccessPermissionsCGA(classGuide_dapl, -1, 987127);
/*  95 */     addAccessPermissionsMain(classGuide_dapl, -1, 131201);
/*  96 */     classGuide_cd.set_DefaultInstancePermissions(classGuide_dapl);
/*  97 */     removeAuthUserPermission(classGuide_cd, false);
/*     */     
/*  99 */     AccessPermissionList classGuide_apl = classGuide_cd.get_Permissions();
/* 100 */     addAccessPermissionsCGA(classGuide_apl, -1, 983827);
/* 101 */     addAccessPermissionsMain(classGuide_apl, -1, 983827, 131201, 131201, 131201);
/*     */     
/* 103 */     this.updatingBatch.add(classGuide_cd, null);
/*     */     
/*     */ 
/* 106 */     ClassDefinition guideSection_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "GuideSection", null);
/* 107 */     guideSection_cd.set_DefaultInstancePermissions(Factory.AccessPermission.createList());
/* 108 */     removeAuthUserPermission(guideSection_cd, false);
/*     */     
/* 110 */     AccessPermissionList guideSection_apl = guideSection_cd.get_Permissions();
/* 111 */     addAccessPermissionsCGA(guideSection_apl, -1, 983827);
/* 112 */     addAccessPermissionsMain(guideSection_apl, -1, 983827, 131201, 131201, 131201);
/*     */     
/* 114 */     this.updatingBatch.add(guideSection_cd, null);
/*     */     
/*     */ 
/* 117 */     ClassDefinition guideTopic_cd = Factory.ClassDefinition.fetchInstance(this.objStore, "GuideTopic", null);
/*     */     
/* 119 */     AccessPermissionList guideTopic_dapl = Factory.AccessPermission.createList();
/* 120 */     AccessPermission creatorOwner_ap = Factory.AccessPermission.createInstance();
/* 121 */     int documentFullControlNoVersions_accessMask = 986583 - AccessRight.MAJOR_VERSION.getValue() - AccessRight.MINOR_VERSION.getValue();
/* 122 */     creatorOwner_ap.set_AccessMask(Integer.valueOf(documentFullControlNoVersions_accessMask));
/* 123 */     creatorOwner_ap.set_AccessType(AccessType.ALLOW);
/* 124 */     creatorOwner_ap.set_GranteeName("#CREATOR-OWNER");
/* 125 */     creatorOwner_ap.set_InheritableDepth(Integer.valueOf(0));
/* 126 */     guideTopic_dapl.add(creatorOwner_ap);
/* 127 */     guideTopic_cd.set_DefaultInstancePermissions(guideTopic_dapl);
/*     */     
/* 129 */     removeAuthUserPermission(guideTopic_cd, false);
/*     */     
/* 131 */     AccessPermissionList guideTopic_apl = guideTopic_cd.get_Permissions();
/* 132 */     addAccessPermissionsCGA(guideTopic_apl, 0, 983827);
/* 133 */     addAccessPermissionsMain(guideTopic_apl, 0, 983827, 131201, 131201, 131201);
/*     */     
/* 135 */     this.updatingBatch.add(guideTopic_cd, null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void updateRecManagementFolderSecurity()
/*     */   {
/* 141 */     super.updateRecManagementFolderSecurity();
/* 142 */     updateClassificationGuidesFolder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void updateClassificationGuidesFolder()
/*     */   {
/* 149 */     Folder classificationFolder = Factory.Folder.fetchInstance(this.objStore, "/Records Management/Classification Guides", null);
/* 150 */     AccessPermissionList apl = Factory.AccessPermission.createList();
/* 151 */     addAccessPermissionsMain(apl, 0, 131073, 131073);
/* 152 */     addAccessPermissionsCGA(apl, 0, 987127);
/* 153 */     classificationFolder.set_Permissions(apl);
/* 154 */     this.updatingBatch.add(classificationFolder, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void updateSysConfigFolderContainees(Folder folder)
/*     */   {
/* 161 */     Iterator containee_it = folder.get_Containees().iterator();
/* 162 */     while (containee_it.hasNext()) {
/* 163 */       CustomObject customObject = (CustomObject)((ReferentialContainmentRelationship)containee_it.next()).get_Head();
/*     */       
/*     */ 
/* 166 */       if (!customObject.get_Name().equalsIgnoreCase("{C83A89E1-95EE-4E62-8B54-FEE57DBDBD3E}"))
/*     */       {
/*     */ 
/* 169 */         if (customObject.get_Name().equals("{C6FDF762-52BC-4668-AA84-6A14D7A45B1D}"))
/*     */         {
/*     */ 
/* 172 */           AccessPermissionList apl = customObject.get_Permissions();
/* 173 */           addSingleAccessPermission("#AUTHENTICATED-USERS", 983315, AccessType.ALLOW, 0, apl);
/*     */ 
/*     */         }
/* 176 */         else if (customObject.get_Name().equals("{77C87E45-7BF0-40C0-BEC2-ECE139FB3459}"))
/*     */         {
/*     */ 
/* 179 */           AccessPermissionList apl = Factory.AccessPermission.createList();
/*     */           
/* 181 */           addAccessPermission(this.adminGroup, 983315, AccessType.ALLOW, 0, apl);
/* 182 */           addAccessPermission(this.managerGroup, 131073, AccessType.ALLOW, 0, apl);
/* 183 */           addAccessPermission(this.privUserGroup, 131073, AccessType.ALLOW, 0, apl);
/* 184 */           addAccessPermission(this.userGroup, 131073, AccessType.ALLOW, 0, apl);
/* 185 */           customObject.set_Permissions(apl);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 190 */           AccessPermissionList apl = customObject.get_Permissions();
/* 191 */           addAccessPermissionsAdminsAndManagers(apl, 0, 983315);
/*     */         }
/*     */         
/* 194 */         this.updatingBatch.add(customObject, null);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\security\ObjectStoreDoDCh4Security.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */