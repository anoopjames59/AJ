/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.core.BatchItemHandle;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.CustomObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.RetrievingBatch;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMRole;
/*     */ import com.ibm.jarm.api.security.RMRoles;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*     */ import java.util.ArrayList;
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
/*     */ class P8CE_RMRolesImpl
/*     */   implements RMRoles
/*     */ {
/*  43 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */   private Repository jarmRepository;
/*     */   
/*     */   private String applicationName;
/*     */   private List<P8CE_RMRoleImpl> roleList;
/*     */   
/*     */   P8CE_RMRolesImpl(Repository repository, String applicationName)
/*     */   {
/*  52 */     Tracer.traceMethodEntry(new Object[] { repository, applicationName });
/*  53 */     Util.ckNullObjParam("repository", repository);
/*     */     
/*  55 */     this.jarmRepository = repository;
/*  56 */     this.applicationName = applicationName;
/*     */     
/*  58 */     this.roleList = loadRolesFromRepository(repository, applicationName);
/*  59 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getApplicationName()
/*     */   {
/*  67 */     return this.applicationName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMRole addNewRole(String roleName, String roleType, String applicationName, String description)
/*     */   {
/*  75 */     Tracer.traceMethodEntry(new Object[] { roleName, roleType, applicationName, description });
/*     */     
/*  77 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/*  80 */       establishedSubject = P8CE_Util.associateSubject();
/*  81 */       P8CE_RMRoleImpl result = null;
/*     */       
/*     */ 
/*     */ 
/*  85 */       List<P8CE_RMRoleImpl> existingRoles = findExistingRoles(null, roleType, applicationName);
/*  86 */       if ((existingRoles != null) && (!existingRoles.isEmpty()))
/*     */       {
/*  88 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_ROLE_ALREADY_EXISTS, new Object[] { roleType, applicationName });
/*     */       }
/*     */       
/*  91 */       result = P8CE_RMRoleImpl.createNew(this.jarmRepository, roleName, roleType, applicationName, description);
/*  92 */       this.roleList.add(result);
/*     */       
/*  94 */       Tracer.traceMethodExit(new Object[] { result });
/*  95 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/*  99 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 103 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 107 */       if (establishedSubject) {
/* 108 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Repository getRepository()
/*     */   {
/* 117 */     return this.jarmRepository;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMRole getRoleById(String id)
/*     */   {
/* 125 */     Tracer.traceMethodEntry(new Object[] { id });
/*     */     
/* 127 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 130 */       establishedSubject = P8CE_Util.associateSubject();
/* 131 */       P8CE_RMRoleImpl result = null;
/*     */       
/* 133 */       List<FilterElement> mandatoryFEs = P8CE_RMRoleImpl.getMandatoryJaceFEs();
/* 134 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryFEs);
/* 135 */       Id roleId = new Id(id);
/* 136 */       ObjectStore jaceObjectStore = ((P8CE_RepositoryImpl)this.jarmRepository).getJaceObjectStore();
/*     */       
/* 138 */       long startTime = System.currentTimeMillis();
/* 139 */       CustomObject jaceCustomObj = Factory.CustomObject.fetchInstance(jaceObjectStore, roleId, jacePF);
/* 140 */       long endTime = System.currentTimeMillis();
/* 141 */       Tracer.traceExtCall("Factory.CustomObject.fetchInstance", startTime, endTime, Integer.valueOf(1), jaceCustomObj, new Object[] { roleId });
/*     */       
/* 143 */       if (jaceCustomObj != null) {
/* 144 */         result = new P8CE_RMRoleImpl(this.jarmRepository, jaceCustomObj);
/*     */       }
/* 146 */       Tracer.traceMethodExit(new Object[] { result });
/* 147 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 151 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 155 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_BY_ID_FAILED, new Object[] { id });
/*     */     }
/*     */     finally
/*     */     {
/* 159 */       if (establishedSubject) {
/* 160 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RMRole getRoleByNames(String roleName, String applicationName)
/*     */   {
/* 169 */     Tracer.traceMethodEntry(new Object[] { roleName, applicationName });
/*     */     
/* 171 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 174 */       establishedSubject = P8CE_Util.associateSubject();
/* 175 */       RMRole result = null;
/*     */       
/* 177 */       List<P8CE_RMRoleImpl> roleList = findExistingRoles(roleName, null, applicationName);
/* 178 */       if ((roleList != null) && (!roleList.isEmpty())) {
/* 179 */         result = (RMRole)roleList.get(0);
/*     */       }
/* 181 */       Tracer.traceMethodExit(new Object[] { result });
/* 182 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 186 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 190 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_ACCESSROLE_FAILED, new Object[] { roleName, applicationName });
/*     */     }
/*     */     finally
/*     */     {
/* 194 */       if (establishedSubject) {
/* 195 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCurrentUserInAnyRole(List<RMRole> roles)
/*     */   {
/* 205 */     Tracer.traceMethodEntry(new Object[] { roles });
/*     */     
/* 207 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 210 */       establishedSubject = P8CE_Util.associateSubject();
/* 211 */       boolean result = false;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */       PropertyFilter jacePF = new PropertyFilter();
/* 218 */       for (FilterElement fe : P8CE_RMRoleImpl.getMandatoryJaceFEs())
/*     */       {
/* 220 */         jacePF.addIncludeProperty(fe);
/*     */       }
/* 222 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)this.jarmRepository).getJaceObjectStore();
/* 223 */       Domain jaceDomain = P8CE_Util.getJaceDomain(jaceObjStore);
/* 224 */       RetrievingBatch rb = RetrievingBatch.createRetrievingBatchInstance(jaceDomain);
/* 225 */       CustomObject jaceUnsecuredAccessRoleObj = null;
/* 226 */       for (RMRole rmRole : roles)
/*     */       {
/* 228 */         jaceUnsecuredAccessRoleObj = (CustomObject)((P8CE_RMRoleImpl)rmRole).getJaceBaseObject();
/* 229 */         rb.add(jaceUnsecuredAccessRoleObj, jacePF);
/*     */       }
/*     */       
/* 232 */       long startTime = System.currentTimeMillis();
/* 233 */       rb.retrieveBatch();
/* 234 */       long stopTime = System.currentTimeMillis();
/* 235 */       Tracer.traceExtCall("RetrievingBatch.retrieveBatch", startTime, stopTime, Integer.valueOf(roles.size()), Boolean.valueOf(rb.hasExceptions()), new Object[0]);
/*     */       Properties unsecuredRoleProps;
/* 237 */       if (rb.hasExceptions())
/*     */       {
/* 239 */         List<BatchItemHandle> BRIs = rb.getBatchItemHandles(null);
/* 240 */         if (BRIs != null)
/*     */         {
/* 242 */           for (BatchItemHandle bri : BRIs)
/*     */           {
/* 244 */             if ((bri != null) && (bri.hasException()))
/*     */             {
/* 246 */               EngineRuntimeException cause = bri.getException();
/* 247 */               throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { cause.getLocalizedMessage() });
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 254 */         unsecuredRoleProps = null;
/* 255 */         for (RMRole rmRole : roles)
/*     */         {
/* 257 */           jaceUnsecuredAccessRoleObj = (CustomObject)((P8CE_RMRoleImpl)rmRole).getJaceBaseObject();
/* 258 */           unsecuredRoleProps = jaceUnsecuredAccessRoleObj.getProperties();
/* 259 */           if ((unsecuredRoleProps.isPropertyPresent("SecuredRole")) && (unsecuredRoleProps.getEngineObjectValue("SecuredRole") != null))
/*     */           {
/*     */ 
/* 262 */             result = true;
/* 263 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 268 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 269 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 273 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 277 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { cause.getLocalizedMessage() });
/*     */     }
/*     */     finally
/*     */     {
/* 281 */       if (establishedSubject) {
/* 282 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Iterator<RMRole> iterator()
/*     */   {
/* 291 */     Tracer.traceMethodEntry(new Object[0]);
/* 292 */     List<RMRole> tempList = new ArrayList();
/* 293 */     tempList.addAll(this.roleList);
/*     */     
/* 295 */     Iterator<RMRole> result = tempList.iterator();
/* 296 */     Tracer.traceMethodExit(new Object[] { result });
/* 297 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 305 */     Tracer.traceMethodEntry(new Object[0]);
/* 306 */     int result = this.roleList.size();
/* 307 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/* 308 */     return result;
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
/*     */   private List<P8CE_RMRoleImpl> loadRolesFromRepository(Repository repository, String applicationName)
/*     */   {
/* 323 */     Tracer.traceMethodEntry(new Object[] { repository, applicationName });
/*     */     
/* 325 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 328 */       establishedSubject = P8CE_Util.associateSubject();
/* 329 */       List<P8CE_RMRoleImpl> result = new ArrayList();
/*     */       
/* 331 */       List<FilterElement> mandatoryFEs = P8CE_RMRoleImpl.getMandatoryJaceFEs();
/* 332 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryFEs);
/* 333 */       String trimmedAppName = applicationName != null ? applicationName.trim() : null;
/*     */       
/*     */ 
/*     */ 
/* 337 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "AccessRole", "ar");
/*     */       
/* 339 */       sb.append(" WHERE 1=1 ");
/* 340 */       if (trimmedAppName != null)
/*     */       {
/* 342 */         trimmedAppName = RALBaseEntity.escapeSQLStringValue(trimmedAppName);
/* 343 */         sb.append("   AND ar.[").append("ApplicationName").append("] = '").append(trimmedAppName).append("' ");
/*     */       }
/* 345 */       String sqlStatement = sb.toString();
/*     */       
/*     */ 
/* 348 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 349 */       ObjectStore jaceObjectStore = ((P8CE_RepositoryImpl)this.jarmRepository).getJaceObjectStore();
/* 350 */       SearchScope jaceSearchScope = new SearchScope(jaceObjectStore);
/*     */       
/* 352 */       Boolean continuable = Boolean.TRUE;
/* 353 */       long startTime = System.currentTimeMillis();
/* 354 */       IndependentObjectSet jaceCustomObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, Integer.valueOf(100), jacePF, continuable);
/* 355 */       long stopTime = System.currentTimeMillis();
/* 356 */       Boolean elementCountIndicator = null;
/* 357 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 359 */         elementCountIndicator = jaceCustomObjSet != null ? Boolean.valueOf(jaceCustomObjSet.isEmpty()) : null;
/*     */       }
/* 361 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceCustomObjSet, new Object[] { sqlStatement });
/*     */       
/*     */       PageIterator jacePI;
/* 364 */       if ((jaceCustomObjSet != null) && (!jaceCustomObjSet.isEmpty()))
/*     */       {
/* 366 */         jacePI = jaceCustomObjSet.pageIterator();
/* 367 */         while ((jacePI != null) && (jacePI.nextPage()))
/*     */         {
/* 369 */           Object[] currentPage = jacePI.getCurrentPage();
/* 370 */           for (Object obj : currentPage)
/*     */           {
/* 372 */             result.add(new P8CE_RMRoleImpl(repository, (CustomObject)obj));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 377 */       Tracer.traceMethodExit(new Object[] { result });
/* 378 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 382 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 386 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETREIVING_SECURED_ACCESSROLE_FAILED, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 390 */       if (establishedSubject) {
/* 391 */         P8CE_Util.disassociateSubject();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<P8CE_RMRoleImpl> findExistingRoles(String roleName, String roleType, String applicationName)
/*     */   {
/* 409 */     Tracer.traceMethodEntry(new Object[] { roleName, roleType, applicationName });
/* 410 */     List<P8CE_RMRoleImpl> result = new ArrayList();
/*     */     
/* 412 */     List<FilterElement> mandatoryFEs = P8CE_RMRoleImpl.getMandatoryJaceFEs();
/* 413 */     PropertyFilter jacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, mandatoryFEs);
/*     */     
/*     */ 
/* 416 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "AccessRole", "ar");
/*     */     
/* 418 */     String escapedAppName = RALBaseEntity.escapeSQLStringValue(applicationName);
/* 419 */     sb.append(" WHERE ar.[").append("ApplicationName").append("] = '").append(escapedAppName).append("' ");
/*     */     
/*     */ 
/* 422 */     if (roleType != null)
/*     */     {
/* 424 */       String escapedRoleType = RALBaseEntity.escapeSQLStringValue(roleType);
/* 425 */       sb.append("   AND ar.[").append("RoleType").append("] = '").append(escapedRoleType).append("' ");
/*     */     }
/* 427 */     else if (roleName != null)
/*     */     {
/* 429 */       String escapedRoleName = RALBaseEntity.escapeSQLStringValue(roleName);
/* 430 */       sb.append("   AND ar.[").append("RoleName").append("] = '").append(escapedRoleName).append("' ");
/*     */     }
/* 432 */     String sqlStatement = sb.toString();
/*     */     
/*     */ 
/* 435 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 436 */     ObjectStore jaceObjectStore = ((P8CE_RepositoryImpl)this.jarmRepository).getJaceObjectStore();
/* 437 */     SearchScope jaceSearchScope = new SearchScope(jaceObjectStore);
/*     */     
/* 439 */     Boolean continuable = Boolean.TRUE;
/* 440 */     long startTime = System.currentTimeMillis();
/* 441 */     IndependentObjectSet jaceCustomObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, Integer.valueOf(100), jacePF, continuable);
/* 442 */     long stopTime = System.currentTimeMillis();
/* 443 */     Boolean elementCountIndicator = null;
/* 444 */     if (Tracer.isMediumTraceEnabled())
/*     */     {
/* 446 */       elementCountIndicator = jaceCustomObjSet != null ? Boolean.valueOf(jaceCustomObjSet.isEmpty()) : null;
/*     */     }
/* 448 */     Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceCustomObjSet, new Object[] { sqlStatement });
/*     */     
/*     */ 
/* 451 */     if ((jaceCustomObjSet != null) && (!jaceCustomObjSet.isEmpty()))
/*     */     {
/* 453 */       PageIterator jacePI = jaceCustomObjSet.pageIterator();
/* 454 */       while ((jacePI != null) && (jacePI.nextPage()))
/*     */       {
/* 456 */         Object[] currentPage = jacePI.getCurrentPage();
/* 457 */         for (Object obj : currentPage)
/*     */         {
/* 459 */           result.add(new P8CE_RMRoleImpl(this.jarmRepository, (CustomObject)obj));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 464 */     Tracer.traceMethodExit(new Object[] { result });
/* 465 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMRolesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */