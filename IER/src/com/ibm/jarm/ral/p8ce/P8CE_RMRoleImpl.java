/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.constants.PermissionSource;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.constants.SecurityPrincipalType;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.EntireNetwork;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.core.Factory.CustomObject;
/*     */ import com.filenet.api.core.Factory.EntireNetwork;
/*     */ import com.filenet.api.core.Factory.Group;
/*     */ import com.filenet.api.core.Factory.User;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.UpdatingBatch;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.filenet.api.security.Group;
/*     */ import com.filenet.api.security.User;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMPrincipalSearchAttribute;
/*     */ import com.ibm.jarm.api.constants.RMPrincipalSearchSortType;
/*     */ import com.ibm.jarm.api.constants.RMPrincipalSearchType;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMUser;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMGroup;
/*     */ import com.ibm.jarm.api.security.RMPrincipal;
/*     */ import com.ibm.jarm.api.security.RMRole;
/*     */ import com.ibm.jarm.api.security.RMUser;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_RMRoleImpl
/*     */   implements RMRole, JaceBasable
/*     */ {
/*  62 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
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
/*  73 */   private static final String[] MandatoryPropertyNames = { "Id", "ApplicationName", "Description", "RoleName", "RoleType" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   private Repository jarmRepository;
/*     */   private ObjectStore jaceObjStore;
/*     */   private CustomObject jaceSecuredAccessRoleObj;
/*     */   
/*     */   static
/*     */   {
/*  81 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  82 */     List<FilterElement> tempList = new ArrayList(1);
/*  83 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.FALSE, mandatoryNames, null));
/*  84 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  89 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  94 */     return MandatoryJaceFEs;
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
/*     */   P8CE_RMRoleImpl(Repository repository, CustomObject jaceSecuredObj)
/*     */   {
/* 109 */     Tracer.traceMethodEntry(new Object[] { repository, jaceSecuredObj });
/* 110 */     this.jarmRepository = repository;
/* 111 */     this.jaceSecuredAccessRoleObj = jaceSecuredObj;
/* 112 */     this.jaceObjStore = ((P8CE_RepositoryImpl)this.jarmRepository).getJaceObjectStore();
/* 113 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/* 121 */     return this.jaceSecuredAccessRoleObj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 129 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMember(RMPrincipal principal)
/*     */   {
/* 139 */     Tracer.traceMethodEntry(new Object[] { principal });
/* 140 */     Util.ckNullObjParam("principal", principal);
/*     */     
/* 142 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 145 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 147 */       CustomObject securedAccessRoleObj = getSecuredAccessRoleObj();
/* 148 */       if (securedAccessRoleObj != null)
/*     */       {
/* 150 */         String principalName = principal.getName();
/* 151 */         AccessPermissionList jacePermList = P8CE_Util.getJacePermissions(securedAccessRoleObj);
/*     */         
/*     */ 
/* 154 */         AccessPermission jacePerm = null;
/* 155 */         boolean principalAlreadyAMember = false;
/* 156 */         Iterator<AccessPermission> it = jacePermList.iterator();
/* 157 */         while ((it != null) && (it.hasNext()))
/*     */         {
/* 159 */           jacePerm = (AccessPermission)it.next();
/* 160 */           if ((jacePerm != null) && 
/*     */           
/* 162 */             (principalName.equalsIgnoreCase(jacePerm.get_GranteeName())))
/*     */           {
/* 164 */             principalAlreadyAMember = true;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 170 */         if (!principalAlreadyAMember)
/*     */         {
/* 172 */           jacePerm = Factory.AccessPermission.createInstance();
/* 173 */           jacePerm.set_AccessType(AccessType.ALLOW);
/* 174 */           jacePerm.set_AccessMask(Integer.valueOf(995603));
/* 175 */           jacePerm.set_GranteeName(principalName);
/* 176 */           jacePerm.set_InheritableDepth(Integer.valueOf(0));
/*     */           
/* 178 */           jacePermList.add(jacePerm);
/* 179 */           securedAccessRoleObj.set_Permissions(jacePermList);
/* 180 */           securedAccessRoleObj.save(RefreshMode.REFRESH);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 185 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INSUFFICIENT_RIGHTS_TO_ACCESS_ROLE, new Object[] { getRoleName(), getRoleType(), getApplicationName() });
/*     */       }
/*     */       
/* 188 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 192 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 196 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 200 */       if (establishedSubject) {
/* 201 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getApplicationName()
/*     */   {
/* 211 */     Tracer.traceMethodEntry(new Object[0]);
/* 212 */     String result = P8CE_Util.getJacePropertyAsString(this, "ApplicationName");
/* 213 */     Tracer.traceMethodExit(new Object[] { result });
/* 214 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 222 */     Tracer.traceMethodEntry(new Object[0]);
/* 223 */     String result = P8CE_Util.getJacePropertyAsString(this, "Description");
/* 224 */     Tracer.traceMethodExit(new Object[] { result });
/* 225 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPrincipal> getMembers()
/*     */   {
/* 234 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 236 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 239 */       establishedSubject = P8CE_Util.associateSubject();
/* 240 */       List<RMPrincipal> result = null;
/*     */       
/* 242 */       CustomObject securedAccessRoleObj = getSecuredAccessRoleObj();
/* 243 */       AccessPermissionList jacePermList; if (securedAccessRoleObj != null)
/*     */       {
/* 245 */         jacePermList = securedAccessRoleObj.get_Permissions();
/* 246 */         if (jacePermList != null)
/*     */         {
/* 248 */           result = new ArrayList();
/* 249 */           AccessPermission jacePerm = null;
/* 250 */           RMPrincipal jarmPrincipal = null;
/* 251 */           Iterator<AccessPermission> it = jacePermList.iterator();
/* 252 */           while ((it != null) && (it.hasNext()))
/*     */           {
/* 254 */             jacePerm = (AccessPermission)it.next();
/*     */             
/* 256 */             if ((jacePerm != null) && (AccessType.ALLOW.equals(jacePerm.get_AccessType())))
/*     */             {
/* 258 */               jarmPrincipal = lookupPrincipal(jacePerm);
/* 259 */               if (jarmPrincipal != null) {
/* 260 */                 result.add(jarmPrincipal);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 267 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INSUFFICIENT_RIGHTS_TO_ACCESS_ROLE, new Object[] { getRoleName(), getRoleType(), getApplicationName() });
/*     */       }
/*     */       
/* 270 */       Tracer.traceMethodExit(new Object[] { result });
/* 271 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 275 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 279 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 283 */       if (establishedSubject) {
/* 284 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRoleName()
/*     */   {
/* 293 */     Tracer.traceMethodEntry(new Object[0]);
/* 294 */     String result = P8CE_Util.getJacePropertyAsString(this, "RoleName");
/* 295 */     Tracer.traceMethodExit(new Object[] { result });
/* 296 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRoleType()
/*     */   {
/* 304 */     Tracer.traceMethodEntry(new Object[0]);
/* 305 */     String result = P8CE_Util.getJacePropertyAsString(this, "RoleType");
/* 306 */     Tracer.traceMethodExit(new Object[] { result });
/* 307 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isPrincipalInRole(RMPrincipal principal)
/*     */   {
/* 314 */     Tracer.traceMethodEntry(new Object[0]);
/* 315 */     boolean found = false;
/*     */     
/* 317 */     if (principal != null)
/*     */     {
/* 319 */       AccessPermissionList jacePermList = P8CE_Util.getJacePermissions(getSecuredAccessRoleObj());
/* 320 */       Iterator<AccessPermission> permIter = jacePermList.iterator();
/* 321 */       while (permIter.hasNext())
/*     */       {
/* 323 */         AccessPermission jacePerm = (AccessPermission)permIter.next();
/* 324 */         if ((jacePerm != null) && (jacePerm.get_GranteeName().equalsIgnoreCase(principal.getName())) && ((jacePerm.get_AccessMask().intValue() & 0xF3113) == 995603))
/*     */         {
/*     */ 
/* 327 */           found = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 332 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(found) });
/* 333 */     return found;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCurrentUserInRole()
/*     */   {
/* 343 */     Tracer.traceMethodEntry(new Object[0]);
/* 344 */     boolean establishedSubject = false;
/* 345 */     boolean result = false;
/*     */     try
/*     */     {
/* 348 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 351 */       Connection jaceConn = ((CustomObject)getJaceBaseObject()).getConnection();
/* 352 */       EntireNetwork network = Factory.EntireNetwork.fetchInstance(jaceConn, null);
/* 353 */       User currentUser = network.get_CurrentUser();
/*     */       
/* 355 */       DomainConnection conn = this.jarmRepository.getDomain().getDomainConnection();
/*     */       
/* 357 */       RMPrincipal currentRMPrincipal = RMFactory.RMUser.fetchInstance(conn, currentUser.get_ShortName(), null);
/*     */       
/*     */ 
/* 360 */       CustomObject securedAccessRoleObj = getSecuredAccessRoleObj();
/*     */       
/*     */       AccessPermissionList jacePermList;
/* 363 */       if (securedAccessRoleObj != null)
/*     */       {
/*     */ 
/* 366 */         jacePermList = P8CE_Util.getJacePermissions(securedAccessRoleObj);
/* 367 */         if (jacePermList != null)
/*     */         {
/* 369 */           AccessPermission jacePerm = null;
/* 370 */           Iterator<AccessPermission> it = jacePermList.iterator();
/* 371 */           while ((it != null) && (it.hasNext()))
/*     */           {
/* 373 */             jacePerm = (AccessPermission)it.next();
/*     */             
/*     */ 
/* 376 */             if ((jacePerm != null) && (jacePerm.get_GranteeName().equalsIgnoreCase(currentRMPrincipal.getName())) && ((jacePerm.get_AccessMask().intValue() & 0xF3113) == 995603))
/*     */             {
/*     */ 
/* 379 */               result = true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 385 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 386 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 390 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 394 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 398 */       if (establishedSubject) {
/* 399 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeMember(RMPrincipal principal)
/*     */   {
/* 409 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 411 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 414 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 416 */       CustomObject securedAccessRoleObj = getSecuredAccessRoleObj();
/* 417 */       if (securedAccessRoleObj != null)
/*     */       {
/* 419 */         AccessPermissionList jacePermList = securedAccessRoleObj.get_Permissions();
/* 420 */         if (jacePermList != null)
/*     */         {
/*     */ 
/*     */ 
/* 424 */           AccessPermissionList newJacePermList = Factory.AccessPermission.createList();
/* 425 */           String principalName = principal.getName();
/* 426 */           AccessPermission jacePerm = null;
/* 427 */           Iterator<AccessPermission> it = jacePermList.iterator();
/* 428 */           while ((it != null) && (it.hasNext()))
/*     */           {
/* 430 */             jacePerm = (AccessPermission)it.next();
/* 431 */             if ((jacePerm != null) && (PermissionSource.SOURCE_DIRECT.equals(jacePerm.get_PermissionSource())) && 
/*     */             
/* 433 */               (!principalName.equals(jacePerm.get_GranteeName()))) {
/* 434 */               newJacePermList.add(jacePerm);
/*     */             }
/*     */           }
/*     */           
/* 438 */           securedAccessRoleObj.set_Permissions(newJacePermList);
/* 439 */           securedAccessRoleObj.save(RefreshMode.REFRESH);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 444 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INSUFFICIENT_RIGHTS_TO_ACCESS_ROLE, new Object[] { getRoleName(), getRoleType(), getApplicationName() });
/*     */       }
/*     */       
/* 447 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 451 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 455 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 459 */       if (establishedSubject) {
/* 460 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDescription(String description)
/*     */   {
/* 469 */     Tracer.traceMethodEntry(new Object[] { description });
/* 470 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 473 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 475 */       CustomObject securedAccessRoleObj = getSecuredAccessRoleObj();
/* 476 */       if (securedAccessRoleObj != null)
/*     */       {
/*     */ 
/* 479 */         Domain jaceDomain = P8CE_Util.getJaceDomain(this.jaceObjStore);
/*     */         
/*     */ 
/*     */ 
/* 483 */         PropertyFilter jacePF = new PropertyFilter();
/* 484 */         jacePF.addIncludeProperty((FilterElement)getMandatoryJaceFEs().get(0));
/* 485 */         UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/* 486 */         securedAccessRoleObj.getProperties().putValue("Description", description);
/* 487 */         jaceUB.add(securedAccessRoleObj, jacePF);
/*     */         
/* 489 */         long startTime = System.currentTimeMillis();
/* 490 */         jaceUB.updateBatch();
/* 491 */         long endTime = System.currentTimeMillis();
/* 492 */         Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/* 496 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INSUFFICIENT_RIGHTS_TO_ACCESS_ROLE, new Object[] { getRoleName(), getRoleType(), getApplicationName() });
/*     */       }
/*     */       
/* 499 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 503 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 507 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 511 */       if (establishedSubject) {
/* 512 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 521 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 529 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 532 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 534 */       CustomObject securedAccessRoleObj = getSecuredAccessRoleObj();
/* 535 */       if (securedAccessRoleObj != null)
/*     */       {
/* 537 */         securedAccessRoleObj.delete();
/*     */         
/* 539 */         long startTime = System.currentTimeMillis();
/* 540 */         securedAccessRoleObj.save(RefreshMode.NO_REFRESH);
/* 541 */         long endTime = System.currentTimeMillis();
/* 542 */         Tracer.traceExtCall("CustomObject.save", startTime, endTime, null, null, new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/* 546 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INSUFFICIENT_RIGHTS_TO_ACCESS_ROLE, new Object[] { getRoleName(), getRoleType(), getApplicationName() });
/*     */       }
/*     */       
/* 549 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 553 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 557 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_INSUFFICIENT_RIGHTS_TO_ACCESS_ROLE, new Object[] { getRoleName(), getRoleType(), getApplicationName() });
/*     */     }
/*     */     finally
/*     */     {
/* 561 */       if (establishedSubject) {
/* 562 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityType getEntityType()
/*     */   {
/* 572 */     return EntityType.AccessRole;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 580 */     Tracer.traceMethodEntry(new Object[0]);
/* 581 */     String result = P8CE_Util.getJaceObjectIdentity(getJaceBaseObject());
/* 582 */     Tracer.traceMethodExit(new Object[] { result });
/* 583 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 591 */     Tracer.traceMethodEntry(new Object[0]);
/* 592 */     String result = P8CE_Util.getJaceObjectClassName((EngineObject)this);
/* 593 */     Tracer.traceMethodExit(new Object[] { result });
/* 594 */     return result;
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
/*     */   static P8CE_RMRoleImpl createNew(Repository repository, String roleName, String roleType, String applicationName, String description)
/*     */   {
/* 616 */     Tracer.traceMethodEntry(new Object[] { repository, roleName, roleType, applicationName, description });
/*     */     
/* 618 */     ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 619 */     Domain jaceDomain = P8CE_Util.getJaceDomain(jaceObjStore);
/* 620 */     User jaceCurrentUser = P8CE_Util.fetchCurrentJaceUser(jaceDomain);
/* 621 */     if (jaceCurrentUser == null)
/*     */     {
/* 623 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNABLE_TO_DETERMINE_CURRENT_USER, new Object[0]);
/*     */     }
/* 625 */     String currentUserGranteeName = jaceCurrentUser.get_Name();
/*     */     
/* 627 */     PropertyFilter jacePF = new PropertyFilter();
/* 628 */     jacePF.addIncludeProperty((FilterElement)getMandatoryJaceFEs().get(0));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 633 */     UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*     */     
/* 635 */     CustomObject securedAccessRole = Factory.CustomObject.createInstance(jaceObjStore, "AccessRole");
/* 636 */     jaceUB.add(securedAccessRole, jacePF);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 644 */     Properties jaceProps = securedAccessRole.getProperties();
/* 645 */     jaceProps.putValue("RoleName", roleName);
/* 646 */     jaceProps.putValue("RoleType", roleType);
/* 647 */     jaceProps.putValue("ApplicationName", applicationName);
/* 648 */     jaceProps.putValue("Description", description);
/*     */     
/*     */ 
/* 651 */     AccessPermissionList securedJacePerms = Factory.AccessPermission.createList();
/* 652 */     AccessPermission jacePerm = Factory.AccessPermission.createInstance();
/*     */     
/* 654 */     jacePerm.set_AccessMask(Integer.valueOf(995603));
/* 655 */     jacePerm.set_AccessType(AccessType.ALLOW);
/* 656 */     jacePerm.set_InheritableDepth(Integer.valueOf(0));
/* 657 */     jacePerm.set_GranteeName(currentUserGranteeName);
/* 658 */     securedJacePerms.add(jacePerm);
/* 659 */     securedAccessRole.set_Permissions(securedJacePerms);
/*     */     
/*     */ 
/* 662 */     long startTime = System.currentTimeMillis();
/* 663 */     jaceUB.updateBatch();
/* 664 */     long endTime = System.currentTimeMillis();
/* 665 */     Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*     */     
/* 667 */     startTime = System.currentTimeMillis();
/* 668 */     securedAccessRole.refresh();
/* 669 */     endTime = System.currentTimeMillis();
/* 670 */     Tracer.traceExtCall("CustomObject.refresh", startTime, endTime, null, null, new Object[0]);
/*     */     
/* 672 */     P8CE_RMRoleImpl result = new P8CE_RMRoleImpl(repository, securedAccessRole);
/* 673 */     Tracer.traceMethodExit(new Object[] { result });
/* 674 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CustomObject getSecuredAccessRoleObj()
/*     */   {
/* 685 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 687 */     if (this.jaceSecuredAccessRoleObj == null)
/*     */     {
/* 689 */       CustomObject jaceCustomObj = null;
/* 690 */       List<FilterElement> mandatoryFEs = getMandatoryJaceFEs();
/* 691 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryFEs);
/*     */       
/*     */ 
/* 694 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "AccessRole", "ar");
/*     */       
/* 696 */       String id = P8CE_Util.getJacePropertyAsString(this, "Id");
/* 697 */       sb.append("   WHERE ar.[").append("Id").append("] = '").append(id).append("'");
/* 698 */       String sqlStatement = sb.toString();
/*     */       
/*     */ 
/* 701 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 702 */       ObjectStore jaceObjectStore = ((P8CE_RepositoryImpl)this.jarmRepository).getJaceObjectStore();
/* 703 */       SearchScope jaceSearchScope = new SearchScope(jaceObjectStore);
/*     */       
/* 705 */       Boolean continuable = Boolean.FALSE;
/* 706 */       long startTime = System.currentTimeMillis();
/* 707 */       IndependentObjectSet jaceCustomObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, Integer.valueOf(1), jacePF, continuable);
/* 708 */       long stopTime = System.currentTimeMillis();
/* 709 */       Boolean elementCountIndicator = null;
/* 710 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 712 */         elementCountIndicator = jaceCustomObjSet != null ? Boolean.valueOf(jaceCustomObjSet.isEmpty()) : null;
/*     */       }
/* 714 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceCustomObjSet, new Object[] { sqlStatement });
/*     */       
/*     */ 
/* 717 */       if ((jaceCustomObjSet != null) && (!jaceCustomObjSet.isEmpty()))
/*     */       {
/* 719 */         Iterator<CustomObject> it = jaceCustomObjSet.iterator();
/* 720 */         if ((it != null) && (it.hasNext()))
/*     */         {
/* 722 */           jaceCustomObj = (CustomObject)it.next();
/*     */         }
/*     */       }
/*     */       
/* 726 */       this.jaceSecuredAccessRoleObj = jaceCustomObj;
/*     */     }
/*     */     
/* 729 */     Tracer.traceMethodExit(new Object[] { this.jaceSecuredAccessRoleObj });
/* 730 */     return this.jaceSecuredAccessRoleObj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RMPrincipal lookupPrincipal(AccessPermission jacePerm)
/*     */   {
/* 742 */     Tracer.traceMethodEntry(new Object[] { jacePerm });
/* 743 */     RMPrincipal result = null;
/*     */     
/* 745 */     RMDomain jarmDomain = this.jarmRepository.getDomain();
/* 746 */     String granteeName = jacePerm.get_GranteeName();
/* 747 */     SecurityPrincipalType granteeType = jacePerm.get_GranteeType();
/* 748 */     String shortName = "";
/* 749 */     if (SecurityPrincipalType.GROUP.equals(granteeType))
/*     */     {
/* 751 */       P8CE_DomainConnectionImpl domainConn = (P8CE_DomainConnectionImpl)jarmDomain.getDomainConnection();
/* 752 */       if (domainConn != null)
/*     */       {
/* 754 */         Connection jaceConn = domainConn.getJaceConnection();
/* 755 */         Group principal = Factory.Group.fetchInstance(jaceConn, granteeName, null);
/* 756 */         shortName = principal.get_ShortName();
/*     */       }
/* 758 */       PageableSet<RMGroup> groupSet = jarmDomain.findGroups(shortName, RMPrincipalSearchType.Exact, RMPrincipalSearchAttribute.ShortName, RMPrincipalSearchSortType.None, Integer.valueOf(1));
/*     */       
/*     */ 
/* 761 */       if ((groupSet != null) && (!groupSet.isEmpty()))
/*     */       {
/* 763 */         Iterator<RMGroup> it = groupSet.iterator();
/* 764 */         if ((it != null) && (it.hasNext())) {
/* 765 */           result = (RMPrincipal)it.next();
/*     */         }
/*     */       }
/* 768 */     } else if (SecurityPrincipalType.USER.equals(granteeType))
/*     */     {
/* 770 */       P8CE_DomainConnectionImpl domainConn = (P8CE_DomainConnectionImpl)jarmDomain.getDomainConnection();
/* 771 */       if (domainConn != null)
/*     */       {
/* 773 */         Connection jaceConn = domainConn.getJaceConnection();
/* 774 */         User principal = Factory.User.fetchInstance(jaceConn, granteeName, null);
/* 775 */         shortName = principal.get_ShortName();
/*     */       }
/* 777 */       PageableSet<RMUser> userSet = jarmDomain.findUsers(shortName, RMPrincipalSearchType.Exact, RMPrincipalSearchAttribute.ShortName, RMPrincipalSearchSortType.None, Integer.valueOf(1));
/*     */       
/*     */ 
/*     */ 
/* 781 */       if ((userSet != null) && (!userSet.isEmpty()))
/*     */       {
/* 783 */         Iterator<RMUser> it = userSet.iterator();
/* 784 */         if ((it != null) && (it.hasNext())) {
/* 785 */           result = (RMPrincipal)it.next();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 790 */     Tracer.traceMethodExit(new Object[] { result });
/* 791 */     return result;
/*     */   }
/*     */   
/*     */   public String getPrivileges()
/*     */   {
/* 796 */     Tracer.traceMethodEntry(new Object[0]);
/* 797 */     EngineObject jaceBaseObj = getJaceBaseObject();
/* 798 */     byte[] privileges = jaceBaseObj.getProperties().getBinaryValue("Privileges");
/* 799 */     String result = new String(privileges);
/*     */     
/* 801 */     Tracer.traceMethodExit(new Object[] { result });
/* 802 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPrivileges(String privileges)
/*     */   {
/* 808 */     Tracer.traceMethodEntry(new Object[] { privileges });
/* 809 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 812 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 814 */       CustomObject securedAccessRoleObj = getSecuredAccessRoleObj();
/* 815 */       if (securedAccessRoleObj != null)
/*     */       {
/* 817 */         long startTime = System.currentTimeMillis();
/* 818 */         securedAccessRoleObj.save(RefreshMode.REFRESH);
/* 819 */         long endTime = System.currentTimeMillis();
/* 820 */         Tracer.traceExtCall("CustomObject.save", startTime, endTime, null, null, new Object[0]);
/*     */       }
/*     */       else
/*     */       {
/* 824 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INSUFFICIENT_RIGHTS_TO_ACCESS_ROLE, new Object[] { getRoleName(), getRoleType(), getApplicationName() });
/*     */       }
/*     */       
/* 827 */       securedAccessRoleObj.refresh();
/* 828 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 832 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 836 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 840 */       if (establishedSubject) {
/* 841 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMRoleImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */