/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.admin.AddOn;
/*     */ import com.filenet.api.admin.AddOnInstallationRecord;
/*     */ import com.filenet.api.admin.ChoiceList;
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.ChoiceListSet;
/*     */ import com.filenet.api.collection.ClassDescriptionSet;
/*     */ import com.filenet.api.collection.DependentObjectList;
/*     */ import com.filenet.api.collection.RepositoryRowSet;
/*     */ import com.filenet.api.constants.InstallationStatus;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.meta.ClassDescription;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.constants.RepositoryType;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMChoiceList;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*     */ import com.ibm.jarm.ral.common.RALBaseRepository;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_RepositoryImpl
/*     */   extends RALBaseRepository
/*     */   implements Repository, JaceBasable
/*     */ {
/*  57 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  58 */   private static final IGenerator<Repository> RepositoryGenerator = new Generator();
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
/*  78 */   private static final String[] MandatoryPropertyNames = { "DescriptiveText", "DisplayName", "Id", "LocaleName", "Name", "SymbolicName", "AddOnInstallationRecords", "AuditLevel", "CBRSearchType" };
/*     */   
/*     */   protected static final List<FilterElement> MandatoryJaceFEs;
/*     */   protected static final PropertyFilter ClassDefinitionPF;
/*     */   private static final Map<String, String> FPOS_AddONs_Map;
/*     */   private static final Map<String, String> ROS_AddONs_Map;
/*     */   private ObjectStore jaceObjectStore;
/*     */   
/*     */   static
/*     */   {
/*  88 */     String[] AddOnPropertyNames = { "AddOnInstallationRecords", "AddOnName", "InstallationStatus", "AddOn" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  96 */     List<FilterElement> tempList = new ArrayList(2);
/*  97 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.FALSE, mandatoryNames, null));
/*  98 */     String addOnNames = P8CE_Util.createSpaceSeparatedString(AddOnPropertyNames);
/*  99 */     tempList.add(new FilterElement(Integer.valueOf(2), null, Boolean.FALSE, addOnNames, null));
/* 100 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */     
/*     */ 
/* 103 */     ClassDefinitionPF = new PropertyFilter();
/* 104 */     String cdPropNames = P8CE_Util.createSpaceSeparatedString(new String[] { "Id", "Name", "DisplayName", "SymbolicName", "DescriptiveText", "PropertyDefinitions" });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */     ClassDefinitionPF.addIncludeProperty(1, null, Boolean.TRUE, cdPropNames, null);
/* 111 */     String pdPropNames = P8CE_Util.createSpaceSeparatedString(new String[] { "PropertyDefinitions", "Id", "Name", "DisplayName", "SymbolicName", "DataType" });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */     ClassDefinitionPF.addIncludeProperty(0, null, Boolean.TRUE, pdPropNames, null);
/*     */     
/*     */ 
/* 120 */     FPOS_AddONs_Map = new HashMap();
/* 121 */     FPOS_AddONs_Map.put("{75DE0BCB-925B-162E-56C6-67D41007A614}", "Records Manager 4.5.0 Base FPOS");
/* 122 */     FPOS_AddONs_Map.put("{CEAF3D5C-74D6-03C0-AFB1-3A0FEEBC85FE}", "Records Manager 4.5.0 DoD FPOS");
/* 123 */     FPOS_AddONs_Map.put("{CEE850B5-C254-B81C-A808-822FA71DE900}", "Records Manager 4.5.0 PRO FPOS");
/* 124 */     FPOS_AddONs_Map.put("{A66C1CF3-1A19-44F8-CF8E-8F6467EF7539}", "Records Manager 4.5.0 DoD_Classified FPOS");
/*     */     
/* 126 */     FPOS_AddONs_Map.put("{8E4F86D9-B7B3-42D9-851C-F5D27FDF461E}", "Records Manager 4.5.1 Base FPOS");
/* 127 */     FPOS_AddONs_Map.put("{33FAD91C-1FD2-4814-BFD8-DAB1D36CD5F2}", "Records Manager 4.5.1 DoD FPOS");
/* 128 */     FPOS_AddONs_Map.put("{CD4B318E-F6D1-428D-840B-0D83581DBE50}", "Records Manager 4.5.1 PRO FPOS");
/* 129 */     FPOS_AddONs_Map.put("{810CD6F0-2C3F-4C31-A8CB-4EE80D96217E}", "Records Manager 4.5.1 DoD_Classified FPOS");
/*     */     
/* 131 */     FPOS_AddONs_Map.put("{69FA2926-DB39-4773-9B54-B8AD4C7FCAD8}", "EnterpriseRecords 5.1.0 Base FPOS");
/* 132 */     FPOS_AddONs_Map.put("{93DE76BE-A502-495F-B7AF-E6499BE7E316}", "EnterpriseRecords 5.1.0 DoD FPOS");
/* 133 */     FPOS_AddONs_Map.put("{19DAFB58-CB0C-4744-B300-B8441067B83D}", "EnterpriseRecords 5.1.0 PRO FPOS");
/* 134 */     FPOS_AddONs_Map.put("{B40ED169-819D-46F7-A5F9-BDD9586CF08D}", "EnterpriseRecords 5.1.0 DoD_Classified FPOS");
/*     */     
/* 136 */     FPOS_AddONs_Map.put("{567E7332-1B2E-494B-A138-190F47A3617D}", "EnterpriseRecords 5.1.1 Base FPOS");
/* 137 */     FPOS_AddONs_Map.put("{5F023170-78A4-488D-AC39-69E02F03400A}", "EnterpriseRecords 5.1.1 DoD FPOS");
/* 138 */     FPOS_AddONs_Map.put("{E2B25764-E854-49E9-A89F-BF442D6CC3CB}", "EnterpriseRecords 5.1.1 PRO FPOS");
/* 139 */     FPOS_AddONs_Map.put("{4A4D02EC-1FE5-4252-9FC2-2C5B3269F04B}", "EnterpriseRecords 5.1.1 DoD_Classified FPOS");
/*     */     
/* 141 */     FPOS_AddONs_Map.put("{492A4640-A533-488C-99DF-5882F84018AB}", "Enterprise Records 5.1.2 Base FPOS");
/* 142 */     FPOS_AddONs_Map.put("{5F023170-78A4-488D-AC39-69E02F03400B}", "Enterprise Records 5.1.2 DoD FPOS");
/* 143 */     FPOS_AddONs_Map.put("{E2B25764-E854-49E9-A89F-BF442D6CC3CC}", "Enterprise Records 5.1.2 PRO FPOS");
/* 144 */     FPOS_AddONs_Map.put("{4A4D02EC-1FE5-4252-9FC2-2C5B3269F04C}", "Enterprise Records 5.1.2 DoD_Classified FPOS");
/*     */     
/* 146 */     ROS_AddONs_Map = new HashMap();
/* 147 */     ROS_AddONs_Map.put("{7515CDE1-105B-A274-B70C-C39F14F8170C}", "Records Manager ROS");
/* 148 */     ROS_AddONs_Map.put("{B90DBB8F-290E-4302-9700-5C0D93961AB2}", "Enterprise Records 5.1.1 ROS");
/* 149 */     ROS_AddONs_Map.put("{3F2449B4-9D80-448B-85D9-7B98CC2CD819}", "Enterprise Records 5.1.2 ROS");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */   private RepositoryType repositoryType = null;
/*     */   
/*     */   protected Map<String, ClassDefinition> classDefinitionCache;
/*     */   
/*     */ 
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/* 164 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/* 169 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<Repository> getGenerator()
/*     */   {
/* 179 */     return RepositoryGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 188 */     return getMandatoryJaceFEs();
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
/*     */   public P8CE_RepositoryImpl(RMDomain domain, String identity, ObjectStore jaceObjectStore, boolean isPlaceholder)
/*     */   {
/* 203 */     super(domain, identity, isPlaceholder);
/* 204 */     Tracer.traceMethodEntry(new Object[] { domain, identity, jaceObjectStore, Boolean.valueOf(isPlaceholder) });
/* 205 */     this.classDefinitionCache = new HashMap();
/* 206 */     this.jaceObjectStore = jaceObjectStore;
/* 207 */     this.entityType = EntityType.Repository;
/* 208 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 217 */     Tracer.traceMethodEntry(new Object[0]);
/* 218 */     String result = P8CE_Util.getJacePropertyAsString(this, "DisplayName");
/* 219 */     Tracer.traceMethodExit(new Object[] { result });
/* 220 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSymbolicName()
/*     */   {
/* 229 */     Tracer.traceMethodEntry(new Object[0]);
/* 230 */     String result = P8CE_Util.getJacePropertyAsString(this, "SymbolicName");
/* 231 */     Tracer.traceMethodExit(new Object[] { result });
/* 232 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMChoiceList> fetchChoiceLists()
/*     */   {
/* 241 */     Tracer.traceMethodEntry(new Object[0]);
/* 242 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 245 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 247 */       if (!this.jaceObjectStore.getProperties().isPropertyPresent("ChoiceLists"))
/*     */       {
/* 249 */         PropertyFilter pf = new PropertyFilter();
/* 250 */         pf.addIncludeProperty(1, null, Boolean.TRUE, "ChoiceLists", null);
/* 251 */         List<FilterElement> markingSetFEs = P8CE_RMChoiceListImpl.getMandatoryJaceFEs();
/* 252 */         for (FilterElement fe : markingSetFEs)
/*     */         {
/* 254 */           pf.addIncludeProperty(fe);
/*     */         }
/* 256 */         P8CE_Util.fetchAdditionalJaceProperties(this.jaceObjectStore, pf);
/*     */       }
/*     */       
/* 259 */       List<RMChoiceList> jarmChoiceListList = new ArrayList();
/* 260 */       ChoiceListSet jaceChoiceLists = this.jaceObjectStore.get_ChoiceLists();
/* 261 */       ChoiceList jaceChoiceList; if (jaceChoiceLists != null)
/*     */       {
/* 263 */         jaceChoiceList = null;
/* 264 */         Iterator<ChoiceList> it = jaceChoiceLists.iterator();
/* 265 */         while ((it != null) && (it.hasNext()))
/*     */         {
/* 267 */           jaceChoiceList = (ChoiceList)it.next();
/* 268 */           if (jaceChoiceList != null) {
/* 269 */             jarmChoiceListList.add(new P8CE_RMChoiceListImpl(this, jaceChoiceList));
/*     */           }
/*     */         }
/*     */       }
/* 273 */       Tracer.traceMethodExit(new Object[] { jarmChoiceListList });
/* 274 */       return jarmChoiceListList;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 278 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 282 */       String reposIdent = getObjectIdentity();
/* 283 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CHOICELISTS_FAILED, new Object[] { reposIdent });
/*     */     }
/*     */     finally
/*     */     {
/* 287 */       if (establishedSubject) {
/* 288 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMClassDescription> fetchClassDescriptions(String[] classIdents)
/*     */   {
/* 298 */     Tracer.traceMethodEntry(new Object[] { classIdents });
/* 299 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 302 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 304 */       List<RMClassDescription> jarmClassDescs = new ArrayList();
/*     */       
/* 306 */       SearchScope jaceSearchScope = new SearchScope(this.jaceObjectStore);
/* 307 */       long startTime = System.currentTimeMillis();
/* 308 */       ClassDescriptionSet jaceClassDescs = jaceSearchScope.fetchSearchableClassDescriptions(classIdents, null);
/* 309 */       long endTime = System.currentTimeMillis();
/* 310 */       Boolean elementCountIndicator = null;
/* 311 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 313 */         elementCountIndicator = jaceClassDescs != null ? Boolean.valueOf(jaceClassDescs.isEmpty()) : null;
/*     */       }
/* 315 */       Tracer.traceExtCall("SearchScope.fetchSearchableClassDescriptions", startTime, endTime, elementCountIndicator, jaceClassDescs, new Object[] { classIdents });
/*     */       P8CE_CacheService cache;
/* 317 */       if (jaceClassDescs != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 322 */         cache = P8CE_CacheService.getInstance();
/* 323 */         ClassDescription jaceClassDesc = null;
/* 324 */         P8CE_RMClassDescriptionImpl jarmClassDesc = null;
/* 325 */         String classSymName = null;
/* 326 */         Iterator<ClassDescription> it = jaceClassDescs.iterator();
/* 327 */         while ((it != null) && (it.hasNext()))
/*     */         {
/* 329 */           jaceClassDesc = (ClassDescription)it.next();
/* 330 */           if (jaceClassDesc != null)
/*     */           {
/* 332 */             classSymName = jaceClassDesc.get_SymbolicName();
/* 333 */             jarmClassDesc = new P8CE_RMClassDescriptionImpl(this, jaceClassDesc);
/* 334 */             cache.put(this, "CD", classSymName, jarmClassDesc);
/* 335 */             jarmClassDescs.add(jarmClassDesc);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 340 */       Tracer.traceMethodExit(new Object[] { jarmClassDescs });
/* 341 */       return jarmClassDescs;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 345 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 349 */       String reposIdent = getObjectIdentity();
/* 350 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTIONS_FAILED, new Object[] { reposIdent, classIdents });
/*     */     }
/*     */     finally
/*     */     {
/* 354 */       if (establishedSubject) {
/* 355 */         P8CE_Util.disassociateSubject();
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
/*     */   protected RMClassDescription getClassDescription(String classIdent, boolean allowFromCache)
/*     */   {
/* 372 */     Tracer.traceMethodEntry(new Object[] { classIdent, Boolean.valueOf(allowFromCache) });
/* 373 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 376 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 378 */       RMClassDescription result = P8CE_Util.getClassDescription(this, classIdent, allowFromCache);
/* 379 */       Tracer.traceMethodExit(new Object[] { result });
/* 380 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 384 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 388 */       String reposIdent = getObjectIdentity();
/* 389 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTIONS_FAILED, new Object[] { reposIdent, classIdent });
/*     */     }
/*     */     finally
/*     */     {
/* 393 */       if (establishedSubject) {
/* 394 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RepositoryType getRepositoryType()
/*     */   {
/* 406 */     Tracer.traceMethodEntry(new Object[0]);
/* 407 */     if (this.repositoryType == null)
/*     */     {
/* 409 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 412 */         establishedSubject = P8CE_Util.associateSubject();
/*     */         
/* 414 */         RepositoryType tempType = RepositoryType.Plain;
/*     */         
/* 416 */         ObjectStore jaceOS = getJaceObjectStore();
/* 417 */         DependentObjectList addOnList = jaceOS.getProperties().getDependentObjectListValue("AddOnInstallationRecords");
/* 418 */         if (addOnList != null)
/*     */         {
/* 420 */           boolean sawRosAddOnRec = false;
/* 421 */           boolean sawFposAddOnRec = false;
/* 422 */           for (Iterator it = addOnList.iterator(); it.hasNext();)
/*     */           {
/* 424 */             AddOnInstallationRecord addOnRec = (AddOnInstallationRecord)it.next();
/* 425 */             if (addOnRec != null)
/*     */             {
/* 427 */               if (InstallationStatus.INSTALLED.equals(addOnRec.get_InstallationStatus()))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 434 */                 String addOnIdStr = null;
/* 435 */                 if (addOnRec.getProperties().isPropertyPresent("AddOn"))
/*     */                 {
/* 437 */                   Id addOnId = addOnRec.getProperties().getIdValue("AddOn");
/* 438 */                   if (addOnId != null) {
/* 439 */                     addOnIdStr = addOnId.toString();
/*     */                   }
/*     */                 }
/* 442 */                 if (addOnIdStr == null)
/*     */                 {
/*     */ 
/* 445 */                   AddOn addOn = addOnRec.get_AddOn();
/* 446 */                   addOnIdStr = addOn.getObjectReference().getObjectIdentity();
/*     */                 }
/*     */                 
/* 449 */                 if (addOnIdStr != null)
/*     */                 {
/* 451 */                   String ucIdStr = addOnIdStr.toUpperCase();
/* 452 */                   if (FPOS_AddONs_Map.containsKey(ucIdStr))
/*     */                   {
/* 454 */                     sawFposAddOnRec = true;
/* 455 */                     if (sawRosAddOnRec) {
/*     */                       break;
/*     */                     }
/*     */                     
/*     */                   }
/* 460 */                   else if (ROS_AddONs_Map.containsKey(ucIdStr))
/*     */                   {
/* 462 */                     sawRosAddOnRec = true;
/* 463 */                     if (sawFposAddOnRec) {
/*     */                       break;
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 471 */           if (sawFposAddOnRec)
/*     */           {
/* 473 */             tempType = RepositoryType.FilePlan;
/* 474 */             if (sawRosAddOnRec) {
/* 475 */               tempType = RepositoryType.Combined;
/*     */             }
/* 477 */           } else if (sawRosAddOnRec)
/*     */           {
/* 479 */             tempType = RepositoryType.Content;
/*     */           }
/*     */         }
/*     */         
/* 483 */         this.repositoryType = tempType;
/*     */       }
/*     */       catch (RMRuntimeException ex)
/*     */       {
/* 487 */         throw ex;
/*     */       }
/*     */       catch (Exception cause)
/*     */       {
/* 491 */         String reposIdent = getObjectIdentity();
/* 492 */         throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_ADDONS_FAILED, new Object[] { reposIdent });
/*     */       }
/*     */       finally
/*     */       {
/* 496 */         if (establishedSubject) {
/* 497 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/* 501 */     Tracer.traceMethodExit(new Object[] { this.repositoryType });
/* 502 */     return this.repositoryType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 511 */     Tracer.traceMethodEntry(new Object[0]);
/* 512 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceObjectStore);
/* 513 */     Tracer.traceMethodExit(new Object[] { result });
/* 514 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 522 */     Tracer.traceMethodEntry(new Object[0]);
/* 523 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/* 524 */     Tracer.traceMethodExit(new Object[] { result });
/* 525 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 533 */     Tracer.traceMethodEntry(new Object[0]);
/* 534 */     String result = P8CE_Util.getJaceObjectClassName(this.jaceObjectStore);
/* 535 */     Tracer.traceMethodExit(new Object[] { result });
/* 536 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 544 */     Tracer.traceMethodEntry(new Object[0]);
/* 545 */     RMProperties result = null;
/* 546 */     if (this.jaceObjectStore != null)
/*     */     {
/* 548 */       result = new P8CE_RMPropertiesImpl(this.jaceObjectStore, this);
/*     */     }
/*     */     
/* 551 */     Tracer.traceMethodExit(new Object[] { result });
/* 552 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 561 */     Tracer.traceMethodEntry(new Object[0]);
/* 562 */     List<Container> result = Collections.EMPTY_LIST;
/* 563 */     Tracer.traceMethodExit(new Object[] { result });
/* 564 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 572 */     Tracer.traceMethodEntry(new Object[0]);
/* 573 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceObjectStore);
/* 574 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*     */     
/* 576 */     Tracer.traceMethodExit(new Object[] { result });
/* 577 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/* 585 */     return this.jaceObjectStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectStore getJaceObjectStore()
/*     */   {
/* 596 */     return this.jaceObjectStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 604 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 605 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "internalSave", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 614 */     return toString("P8CE_RepositoryImpl");
/*     */   }
/*     */   
/*     */   protected String toString(String prefix)
/*     */   {
/* 619 */     String ident = "<unknown>";
/* 620 */     if (P8CE_Util.isJacePropertyPresent(this, "SymbolicName")) {
/* 621 */       ident = getSymbolicName();
/* 622 */     } else if (P8CE_Util.isJacePropertyPresent(this, "Id")) {
/* 623 */       ident = getObjectIdentity();
/* 624 */     } else if (getClientIdentifier() != null) {
/* 625 */       ident = getClientIdentifier();
/*     */     }
/* 627 */     return prefix + ": '" + ident + "'";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 635 */     Tracer.traceMethodEntry(new Object[0]);
/* 636 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 639 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 641 */       long startTime = System.currentTimeMillis();
/* 642 */       this.jaceObjectStore.refresh();
/* 643 */       long endTime = System.currentTimeMillis();
/* 644 */       Tracer.traceExtCall("ObjectStore.refresh", startTime, endTime, null, null, new Object[0]);
/*     */       
/* 646 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 650 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 654 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 658 */       if (establishedSubject) {
/* 659 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 668 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 669 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 672 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 676 */       List<FilterElement> mandatoryRepositoryFEs = getMandatoryJaceFEs();
/* 677 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRepositoryFEs);
/*     */       
/* 679 */       long startTime = System.currentTimeMillis();
/* 680 */       this.jaceObjectStore.refresh(jacePF);
/* 681 */       long endTime = System.currentTimeMillis();
/* 682 */       Tracer.traceExtCall("ObjectStore.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 684 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 688 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 692 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 696 */       if (establishedSubject) {
/* 697 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 706 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 707 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 710 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 713 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 714 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 715 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 716 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 717 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 719 */       long startTime = System.currentTimeMillis();
/* 720 */       this.jaceObjectStore.refresh(jacePF);
/* 721 */       long endTime = System.currentTimeMillis();
/* 722 */       Tracer.traceExtCall("ObjectStore.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 724 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 728 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 732 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 736 */       if (establishedSubject) {
/* 737 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 746 */     return this.jaceObjectStore.getAccessAllowed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve()
/*     */   {
/* 755 */     Tracer.traceMethodEntry(new Object[0]);
/* 756 */     if (this.isPlaceholder)
/*     */     {
/* 758 */       P8CE_Util.refreshWithMandatoryProperties(this);
/* 759 */       this.isPlaceholder = false;
/*     */     }
/* 761 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean filePlanNameIsUnique(String filePlanName)
/*     */   {
/* 772 */     Tracer.traceMethodEntry(new Object[] { filePlanName });
/* 773 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "filePlanNameIsUnique", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean customObjNameIsUnique(String className, String propToCheck, String nameValue, String identToIgnore)
/*     */   {
/* 782 */     Tracer.traceMethodEntry(new Object[] { className, propToCheck, nameValue, identToIgnore });
/* 783 */     Boolean result = Boolean.valueOf(true);
/*     */     
/*     */ 
/* 786 */     StringBuilder sb = new StringBuilder();
/* 787 */     sb.append("SELECT TOP 1 [").append("Id").append("] ");
/* 788 */     sb.append("FROM [").append(className).append("] ");
/* 789 */     sb.append("WHERE [").append(propToCheck).append("] = '").append(RALBaseEntity.escapeSQLStringValue(nameValue)).append("' ");
/*     */     
/* 791 */     if (identToIgnore != null)
/*     */     {
/* 793 */       sb.append(" AND [").append("Id").append("] <> '").append(identToIgnore).append("' ");
/*     */     }
/* 795 */     String sqlStmt = sb.toString();
/*     */     
/* 797 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 798 */     SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*     */     
/* 800 */     long startTime = System.currentTimeMillis();
/* 801 */     RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, null, null, Boolean.FALSE);
/* 802 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, System.currentTimeMillis(), Integer.valueOf(1), rowSet, new Object[] { sqlStmt });
/*     */     
/* 804 */     if ((rowSet != null) && (!rowSet.isEmpty()))
/*     */     {
/* 806 */       result = Boolean.valueOf(false);
/*     */     }
/*     */     
/* 809 */     Tracer.traceMethodExit(new Object[] { result });
/* 810 */     return result.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<Integer> getFilePlanInitialAllowedRMTypes(String classIdent)
/*     */   {
/* 820 */     Tracer.traceMethodEntry(new Object[] { classIdent });
/* 821 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "getFilePlanInitialAllowedRMTypes", getEntityType(), getClientIdentifier() });
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
/*     */   protected ClassDefinition getOrFetchJaceClassDef(String symbolicName)
/*     */   {
/* 835 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/*     */     
/* 837 */     ClassDefinition result = null;
/*     */     
/*     */ 
/* 840 */     result = (ClassDefinition)this.classDefinitionCache.get(symbolicName);
/*     */     
/* 842 */     if ((result == null) && (!this.classDefinitionCache.containsKey(symbolicName)))
/*     */     {
/* 844 */       result = P8CE_Util.fetchJaceClassDefinition(this.jaceObjectStore, "Document", ClassDefinitionPF);
/* 845 */       this.classDefinitionCache.put(symbolicName, result);
/*     */     }
/*     */     
/* 848 */     Tracer.traceMethodExit(new Object[] { result });
/* 849 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsRecordReceipts()
/*     */   {
/* 858 */     Tracer.traceMethodEntry(new Object[0]);
/* 859 */     boolean result = false;
/* 860 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 861 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAuditEnabled()
/*     */   {
/* 871 */     Tracer.traceMethodEntry(new Object[0]);
/* 872 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 875 */       establishedSubject = P8CE_Util.associateSubject();
/* 876 */       resolve();
/*     */       
/*     */ 
/*     */ 
/* 880 */       boolean result = true;
/*     */       
/* 882 */       Integer auditFlag = P8CE_Util.getJacePropertyAsInteger(this, "AuditLevel");
/* 883 */       if (auditFlag != null) {
/* 884 */         result = auditFlag.intValue() != 0;
/*     */       }
/* 886 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 887 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 891 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 895 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 899 */       if (establishedSubject) {
/* 900 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 909 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<Repository>
/*     */   {
/*     */     public Repository create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 922 */       P8CE_RepositoryImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 923 */       ObjectStore jaceObjStore = (ObjectStore)jaceBaseObject;
/*     */       
/* 925 */       Domain jaceDomain = P8CE_Util.getJaceDomain(jaceObjStore);
/* 926 */       RMDomain rmDomain = (RMDomain)P8CE_RMDomainImpl.getGenerator().create(null, jaceDomain);
/* 927 */       String reposIdentity = jaceObjStore.get_Id().toString();
/* 928 */       Repository result = new P8CE_RepositoryImpl(rmDomain, reposIdentity, jaceObjStore, false);
/*     */       
/* 930 */       P8CE_RepositoryImpl.Tracer.traceMethodExit(new Object[] { result });
/* 931 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RepositoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */