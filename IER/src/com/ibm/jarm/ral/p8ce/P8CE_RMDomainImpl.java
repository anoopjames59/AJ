/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.GroupSet;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.MarkingSetSet;
/*      */ import com.filenet.api.collection.ObjectStoreSet;
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.collection.UserSet;
/*      */ import com.filenet.api.constants.MergeMode;
/*      */ import com.filenet.api.constants.PrincipalSearchAttribute;
/*      */ import com.filenet.api.constants.PrincipalSearchSortType;
/*      */ import com.filenet.api.constants.PrincipalSearchType;
/*      */ import com.filenet.api.core.Connection;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.EngineObject;
/*      */ import com.filenet.api.core.Factory.Domain;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.meta.ClassDescription;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.security.MarkingSet;
/*      */ import com.filenet.api.security.Realm;
/*      */ import com.filenet.api.security.User;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.constants.DomainType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMMergeMode;
/*      */ import com.ibm.jarm.api.constants.RMPrincipalSearchAttribute;
/*      */ import com.ibm.jarm.api.constants.RMPrincipalSearchSortType;
/*      */ import com.ibm.jarm.api.constants.RMPrincipalSearchType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.constants.RepositoryType;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.ContentRepository;
/*      */ import com.ibm.jarm.api.core.DomainConnection;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.meta.RMMarkingSet;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.query.CBRResult;
/*      */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*      */ import com.ibm.jarm.api.query.RMSearch;
/*      */ import com.ibm.jarm.api.query.ResultRow;
/*      */ import com.ibm.jarm.api.security.RMGroup;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.security.RMRoles;
/*      */ import com.ibm.jarm.api.security.RMUser;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.common.RALBaseDomain;
/*      */ import com.ibm.jarm.ral.p8ce.cbr.P8CE_CBRSearch;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class P8CE_RMDomainImpl
/*      */   extends RALBaseDomain
/*      */   implements JaceBasable
/*      */ {
/*   80 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*   81 */   private static final IGenerator<RMDomain> RMDomainGenerator = new Generator();
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
/*   92 */   private static final String[] MandatoryPropertyNames = { "Id", "Name" };
/*      */   private static final List<FilterElement> MandatoryJaceFEs;
/*      */   private Domain jaceDomain;
/*      */   
/*   96 */   static { String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*      */     
/*   98 */     List<FilterElement> tempList = new ArrayList(1);
/*   99 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  100 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*      */   }
/*      */   
/*      */   static String[] getMandatoryPropertyNames()
/*      */   {
/*  105 */     return MandatoryPropertyNames;
/*      */   }
/*      */   
/*      */   public static List<FilterElement> getMandatoryJaceFEs()
/*      */   {
/*  110 */     return MandatoryJaceFEs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static IGenerator<RMDomain> getGenerator()
/*      */   {
/*  120 */     return RMDomainGenerator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   P8CE_RMDomainImpl(DomainConnection domainConn, String identity)
/*      */   {
/*  127 */     super(domainConn, identity, true);
/*  128 */     Tracer.traceMethodEntry(new Object[] { domainConn, identity });
/*  129 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   P8CE_RMDomainImpl(DomainConnection domainConn, String identity, Domain jaceDomain)
/*      */   {
/*  134 */     super(domainConn, identity, false);
/*  135 */     Tracer.traceMethodEntry(new Object[] { domainConn, identity, jaceDomain });
/*  136 */     this.jaceDomain = jaceDomain;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  141 */     if (getClientIdentifier() == null)
/*      */     {
/*  143 */       this.identity = P8CE_Util.getJacePropertyAsString(this, "Name");
/*      */     }
/*  145 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<FilterElement> getMandatoryFEs()
/*      */   {
/*  153 */     return getMandatoryJaceFEs();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getObjectIdentity()
/*      */   {
/*  161 */     return P8CE_Util.getJaceObjectIdentity(this.jaceDomain);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  169 */     return P8CE_Util.getJaceObjectClassName(this.jaceDomain);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMClassDescription getClassDescription()
/*      */   {
/*  178 */     Tracer.traceMethodEntry(new Object[0]);
/*  179 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  182 */       establishedSubject = P8CE_Util.associateSubject();
/*  183 */       RMClassDescription result = null;
/*      */       
/*  185 */       Domain jaceDomain = getOrFetchJaceDomain();
/*  186 */       if (!jaceDomain.getProperties().isPropertyPresent("ClassDescription"))
/*      */       {
/*  188 */         List<FilterElement> jaceFEs = new ArrayList(2);
/*  189 */         jaceFEs.addAll(MandatoryJaceFEs);
/*  190 */         jaceFEs.add(new FilterElement(null, null, null, "ClassDescription", null));
/*  191 */         jaceFEs.addAll(P8CE_RMClassDescriptionImpl.getMandatoryJaceFEs());
/*  192 */         PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(jaceFEs);
/*      */         
/*  194 */         long startTime = System.currentTimeMillis();
/*  195 */         jaceDomain.refresh(jacePF);
/*  196 */         long endTime = System.currentTimeMillis();
/*  197 */         Tracer.traceExtCall("Domain.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       }
/*      */       ClassDescription jaceClassDesc;
/*  200 */       if (jaceDomain.getProperties().isPropertyPresent("ClassDescription"))
/*      */       {
/*  202 */         jaceClassDesc = jaceDomain.get_ClassDescription();
/*  203 */         result = new P8CE_RMClassDescriptionImpl(null, jaceClassDesc);
/*      */       }
/*      */       
/*  206 */       Tracer.traceMethodExit(new Object[] { result });
/*  207 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  211 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  215 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTION_FAILED, new Object[] { "", "Domain" });
/*      */     }
/*      */     finally
/*      */     {
/*  219 */       if (establishedSubject) {
/*  220 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  229 */     Tracer.traceMethodEntry(new Object[0]);
/*  230 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/*  231 */     Tracer.traceMethodExit(new Object[] { result });
/*  232 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DomainType getDomainType()
/*      */   {
/*  240 */     return DomainType.P8_CE;
/*      */   }
/*      */   
/*      */   public EngineObject getJaceBaseObject()
/*      */   {
/*  245 */     return this.jaceDomain;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<ContentRepository> fetchContentRepositories(boolean excludeFilePlanRepositories, RMPropertyFilter filter)
/*      */   {
/*  254 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(excludeFilePlanRepositories), filter });
/*  255 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  258 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  260 */       Domain jaceDomain = getOrFetchJaceDomain();
/*  261 */       if (!jaceDomain.getProperties().isPropertyPresent("ObjectStores"))
/*      */       {
/*  263 */         PropertyFilter pf = new PropertyFilter();
/*  264 */         pf.addIncludeProperty(1, null, Boolean.FALSE, "ObjectStores", null);
/*  265 */         for (int i = 0; i < P8CE_ContentRepositoryImpl.getMandatoryJaceFEs().size(); i++)
/*      */         {
/*  267 */           pf.addIncludeProperty((FilterElement)P8CE_ContentRepositoryImpl.getMandatoryJaceFEs().get(i));
/*      */         }
/*  269 */         P8CE_Util.fetchAdditionalJaceProperties(jaceDomain, pf);
/*      */       }
/*      */       
/*  272 */       TreeMap<String, ContentRepository> sortedReposMap = new TreeMap();
/*  273 */       ObjectStoreSet jaceOSSet = jaceDomain.get_ObjectStores();
/*  274 */       ObjectStore jaceObjStore = null;
/*  275 */       Id jaceId = null;
/*  276 */       String displayName = null;
/*  277 */       ContentRepository jarmRepos = null;
/*  278 */       for (Iterator<ObjectStore> it = jaceOSSet.iterator(); it.hasNext();)
/*      */       {
/*  280 */         jaceObjStore = (ObjectStore)it.next();
/*  281 */         if ((jaceObjStore != null) && (P8CE_ContentRepositoryImpl.isValidROS(jaceObjStore)) && (
/*      */         
/*  283 */           (!excludeFilePlanRepositories) || (!P8CE_FilePlanRepositoryImpl.isValidFPOS(jaceObjStore))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  289 */           performValidation = false;
/*  290 */           jaceId = jaceObjStore.get_Id();
/*  291 */           displayName = jaceObjStore.get_DisplayName();
/*  292 */           jarmRepos = new P8CE_ContentRepositoryImpl(this, jaceId.toString(), jaceObjStore, performValidation, false);
/*  293 */           sortedReposMap.put(displayName, jarmRepos);
/*      */         }
/*      */       }
/*      */       boolean performValidation;
/*  297 */       List<ContentRepository> reposSortedList = new ArrayList(sortedReposMap.values());
/*      */       
/*  299 */       Tracer.traceMethodExit(new Object[] { reposSortedList });
/*  300 */       return reposSortedList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  304 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  308 */       String domainIdent = getClientIdentifier();
/*  309 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_NO_AVAILABLE_CONTENT_REPOSITORIES, new Object[] { domainIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  313 */       if (establishedSubject) {
/*  314 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Repository> fetchCombinedRepositories(RMPropertyFilter filter)
/*      */   {
/*  324 */     Tracer.traceMethodEntry(new Object[] { filter });
/*  325 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  328 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  330 */       Domain jaceDomain = getOrFetchJaceDomain();
/*  331 */       if (!jaceDomain.getProperties().isPropertyPresent("ObjectStores"))
/*      */       {
/*  333 */         PropertyFilter pf = new PropertyFilter();
/*  334 */         pf.addIncludeProperty(1, null, Boolean.FALSE, "ObjectStores", null);
/*  335 */         for (int i = 0; i < P8CE_FilePlanRepositoryImpl.getMandatoryJaceFEs().size(); i++)
/*      */         {
/*  337 */           pf.addIncludeProperty((FilterElement)P8CE_FilePlanRepositoryImpl.getMandatoryJaceFEs().get(i));
/*      */         }
/*  339 */         P8CE_Util.fetchAdditionalJaceProperties(jaceDomain, pf);
/*      */       }
/*      */       
/*  342 */       TreeMap<String, Repository> sortedReposMap = new TreeMap();
/*  343 */       ObjectStoreSet jaceOSSet = jaceDomain.get_ObjectStores();
/*  344 */       ObjectStore jaceObjStore = null;
/*  345 */       Id jaceId = null;
/*  346 */       String displayName = null;
/*  347 */       FilePlanRepository fpRepos = null;
/*  348 */       for (Iterator<ObjectStore> it = jaceOSSet.iterator(); it.hasNext();)
/*      */       {
/*  350 */         jaceObjStore = (ObjectStore)it.next();
/*  351 */         if ((jaceObjStore != null) && (P8CE_FilePlanRepositoryImpl.isValidFPOS(jaceObjStore)))
/*      */         {
/*  353 */           doValidation = false;
/*  354 */           boolean isPlaceholder = false;
/*  355 */           jaceId = jaceObjStore.get_Id();
/*  356 */           displayName = jaceObjStore.get_DisplayName();
/*  357 */           fpRepos = new P8CE_FilePlanRepositoryImpl(this, jaceId.toString(), jaceObjStore, doValidation, isPlaceholder);
/*  358 */           if (RepositoryType.Combined.equals(fpRepos.getRepositoryType()))
/*      */           {
/*  360 */             sortedReposMap.put(displayName, fpRepos); }
/*      */         }
/*      */       }
/*      */       boolean doValidation;
/*  364 */       List<Repository> reposSortedList = new ArrayList(sortedReposMap.values());
/*      */       
/*  366 */       Tracer.traceMethodExit(new Object[] { reposSortedList });
/*  367 */       return reposSortedList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  371 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  375 */       String domainIdent = getClientIdentifier();
/*  376 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_NO_AVAILABLE_CONTENT_REPOSITORIES, new Object[] { domainIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  380 */       if (establishedSubject) {
/*  381 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<FilePlanRepository> fetchFilePlanRepositories(RMPropertyFilter filter)
/*      */   {
/*  391 */     Tracer.traceMethodEntry(new Object[] { filter });
/*  392 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  395 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  397 */       Domain jaceDomain = getOrFetchJaceDomain();
/*  398 */       if (!jaceDomain.getProperties().isPropertyPresent("ObjectStores"))
/*      */       {
/*  400 */         PropertyFilter pf = new PropertyFilter();
/*  401 */         pf.addIncludeProperty(1, null, Boolean.TRUE, "ObjectStores", null);
/*  402 */         for (int i = 0; i < P8CE_FilePlanRepositoryImpl.MandatoryJaceFEs.size(); i++)
/*      */         {
/*  404 */           pf.addIncludeProperty((FilterElement)P8CE_FilePlanRepositoryImpl.MandatoryJaceFEs.get(i));
/*      */         }
/*  406 */         P8CE_Util.fetchAdditionalJaceProperties(jaceDomain, pf);
/*      */       }
/*      */       
/*  409 */       TreeMap<String, FilePlanRepository> sortedReposMap = new TreeMap();
/*  410 */       ObjectStoreSet jaceOSSet = jaceDomain.get_ObjectStores();
/*  411 */       ObjectStore jaceObjStore = null;
/*  412 */       Id jaceId = null;
/*  413 */       String displayName = null;
/*  414 */       FilePlanRepository jarmRepos = null;
/*  415 */       for (Iterator<ObjectStore> it = jaceOSSet.iterator(); it.hasNext();)
/*      */       {
/*  417 */         jaceObjStore = (ObjectStore)it.next();
/*  418 */         if ((jaceObjStore != null) && (P8CE_FilePlanRepositoryImpl.isValidFPOS(jaceObjStore)))
/*      */         {
/*  420 */           jaceId = jaceObjStore.get_Id();
/*  421 */           displayName = jaceObjStore.get_DisplayName();
/*  422 */           jarmRepos = new P8CE_FilePlanRepositoryImpl(this, jaceId.toString(), jaceObjStore, false, false);
/*  423 */           sortedReposMap.put(displayName, jarmRepos);
/*      */         }
/*      */       }
/*  426 */       List<FilePlanRepository> reposSortedList = new ArrayList(sortedReposMap.values());
/*      */       
/*  428 */       Tracer.traceMethodExit(new Object[] { reposSortedList });
/*  429 */       return reposSortedList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  433 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  437 */       String domainIdent = getClientIdentifier();
/*  438 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_NO_AVAILABLE_FILEPLAN_REPOSITORIES, new Object[] { domainIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  442 */       if (establishedSubject) {
/*  443 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<RMMarkingSet> fetchMarkingSets()
/*      */   {
/*  453 */     Tracer.traceMethodEntry(new Object[0]);
/*  454 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  457 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  459 */       Domain jaceDomain = getOrFetchJaceDomain();
/*      */       
/*  461 */       if (!jaceDomain.getProperties().isPropertyPresent("MarkingSets"))
/*      */       {
/*  463 */         PropertyFilter pf = new PropertyFilter();
/*  464 */         pf.addIncludeProperty(1, null, Boolean.TRUE, "MarkingSets", null);
/*  465 */         List<FilterElement> markingSetFEs = P8CE_RMMarkingSetImpl.getMandatoryJaceFEs();
/*  466 */         for (FilterElement fe : markingSetFEs)
/*      */         {
/*  468 */           pf.addIncludeProperty(fe);
/*      */         }
/*  470 */         P8CE_Util.fetchAdditionalJaceProperties(jaceDomain, pf);
/*      */       }
/*      */       
/*  473 */       List<RMMarkingSet> jarmMarkingSetList = new ArrayList();
/*  474 */       MarkingSetSet jaceMarkingSets = jaceDomain.get_MarkingSets();
/*  475 */       MarkingSet jaceMarkingSet; if (jaceMarkingSets != null)
/*      */       {
/*  477 */         jaceMarkingSet = null;
/*  478 */         Iterator<MarkingSet> it = jaceMarkingSets.iterator();
/*  479 */         while ((it != null) && (it.hasNext()))
/*      */         {
/*  481 */           jaceMarkingSet = (MarkingSet)it.next();
/*  482 */           if (jaceMarkingSet != null) {
/*  483 */             jarmMarkingSetList.add(new P8CE_RMMarkingSetImpl(jaceMarkingSet));
/*      */           }
/*      */         }
/*      */       }
/*  487 */       Tracer.traceMethodExit(new Object[] { jarmMarkingSetList });
/*  488 */       return jarmMarkingSetList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  492 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  496 */       String domainIdent = getClientIdentifier();
/*  497 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_MARKINGSETS_FAILED, new Object[] { domainIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  501 */       if (establishedSubject) {
/*  502 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PageableSet<? extends BaseEntity> fetchObjects(RMSearch searchObj, String sqlStatement, EntityType entityType, Integer pageSize, RMPropertyFilter jarmFilter, Boolean continuable)
/*      */   {
/*  515 */     Tracer.traceMethodEntry(new Object[] { searchObj, sqlStatement, entityType, pageSize, jarmFilter, continuable });
/*  516 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  519 */       establishedSubject = P8CE_Util.associateSubject();
/*  520 */       PageableSet<BaseEntity> result = null;
/*      */       
/*      */ 
/*      */ 
/*  524 */       String[] requiredPropertyNames = P8CE_Util.getEntityRequiredPropertyNames(entityType);
/*  525 */       String ucSQL = sqlStatement.toUpperCase();
/*  526 */       int fromPos = ucSQL.indexOf("FROM ");
/*  527 */       String selectClause = sqlStatement.substring(0, fromPos);
/*  528 */       String ucSelectClause = selectClause.toUpperCase();
/*  529 */       List<String> missingRequiredPropNames = new ArrayList(0);
/*  530 */       for (String requiredPropName : requiredPropertyNames)
/*      */       {
/*  532 */         if (ucSelectClause.indexOf(requiredPropName.toUpperCase()) == -1) {
/*  533 */           missingRequiredPropNames.add(requiredPropName);
/*      */         }
/*      */       }
/*  536 */       String updatedSqlStatement = sqlStatement;
/*  537 */       if (missingRequiredPropNames.size() > 0)
/*      */       {
/*  539 */         String tableAlias = determineTableAlias(selectClause);
/*  540 */         StringBuilder sb = new StringBuilder(selectClause.trim());
/*  541 */         for (String missingPropName : missingRequiredPropNames)
/*      */         {
/*  543 */           sb.append(", ").append(tableAlias).append('[').append(missingPropName).append(']');
/*      */         }
/*      */         
/*  546 */         sb.append(' ').append(sqlStatement.substring(fromPos));
/*  547 */         updatedSqlStatement = sb.toString();
/*      */       }
/*      */       
/*  550 */       SearchSQL jaceSearchSql = new SearchSQL(updatedSqlStatement);
/*  551 */       SearchScope jaceSearchScope = createJaceSearchScope(searchObj);
/*  552 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, null);
/*      */       
/*  554 */       long startTime = System.currentTimeMillis();
/*  555 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSql, pageSize, jacePF, continuable);
/*  556 */       long stopTime = System.currentTimeMillis();
/*  557 */       Boolean elementCountIndicator = null;
/*  558 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  560 */         elementCountIndicator = jaceObjSet != null ? Boolean.valueOf(jaceObjSet.isEmpty()) : null;
/*      */       }
/*  562 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement });
/*      */       Repository pageableSetRepository;
/*  564 */       if (jaceObjSet != null)
/*      */       {
/*      */ 
/*  567 */         pageableSetRepository = null;
/*  568 */         Repository[] repositories = searchObj.getRepositories();
/*  569 */         if ((repositories != null) && (repositories.length > 0))
/*      */         {
/*  571 */           pageableSetRepository = repositories[0];
/*      */         }
/*  573 */         boolean supportsPaging = Boolean.TRUE.equals(continuable);
/*  574 */         IGenerator<BaseEntity> generator = P8CE_Util.getEntityGenerator(entityType);
/*  575 */         result = new P8CE_PageableSetImpl(pageableSetRepository, jaceObjSet, supportsPaging, generator);
/*      */       }
/*      */       
/*  578 */       Tracer.traceMethodExit(new Object[] { result });
/*  579 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  583 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  587 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SEARCH_FOR_OBJECTS_FAILED, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  591 */       if (establishedSubject) {
/*  592 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
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
/*      */   private String determineTableAlias(String selectClause)
/*      */   {
/*  608 */     Tracer.traceMethodEntry(new Object[] { selectClause });
/*  609 */     String result = "";
/*  610 */     int dotPos = -1;
/*  611 */     int startPos = -1;
/*  612 */     if ((dotPos = selectClause.indexOf('.')) != -1)
/*      */     {
/*  614 */       for (int i = dotPos - 1; i >= "SELECT".length(); i--)
/*      */       {
/*  616 */         char currentChar = selectClause.charAt(i);
/*  617 */         if ((currentChar == ' ') || (currentChar == ','))
/*      */         {
/*  619 */           startPos = i + 1;
/*  620 */           break;
/*      */         }
/*      */       }
/*      */       
/*  624 */       if (startPos != -1)
/*      */       {
/*  626 */         result = selectClause.substring(startPos, dotPos + 1);
/*      */       }
/*      */     }
/*      */     
/*  630 */     Tracer.traceMethodExit(new Object[] { result });
/*  631 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PageableSet<ResultRow> fetchRows(RMSearch searchObj, String sqlStatement, Integer pageSize, RMPropertyFilter jarmFilter, Boolean continuable)
/*      */   {
/*  641 */     Tracer.traceMethodEntry(new Object[] { searchObj, sqlStatement, pageSize, jarmFilter, continuable });
/*  642 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  645 */       establishedSubject = P8CE_Util.associateSubject();
/*  646 */       PageableSet<ResultRow> result = null;
/*      */       
/*  648 */       SearchSQL jaceSearchSql = new SearchSQL(sqlStatement);
/*  649 */       SearchScope jaceSearchScope = createJaceSearchScope(searchObj);
/*  650 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, null);
/*      */       
/*  652 */       long startTime = System.currentTimeMillis();
/*  653 */       RepositoryRowSet jaceRowSet = jaceSearchScope.fetchRows(jaceSearchSql, pageSize, jacePF, continuable);
/*  654 */       long stopTime = System.currentTimeMillis();
/*  655 */       Boolean elementCountIndicator = null;
/*  656 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  658 */         elementCountIndicator = jaceRowSet != null ? Boolean.valueOf(jaceRowSet.isEmpty()) : null;
/*      */       }
/*  660 */       Tracer.traceExtCall("SearchScope.fetchRows", startTime, stopTime, elementCountIndicator, jaceRowSet, new Object[] { sqlStatement });
/*      */       boolean supportsPaging;
/*  662 */       if (jaceRowSet != null)
/*      */       {
/*      */ 
/*  665 */         supportsPaging = Boolean.TRUE.equals(continuable);
/*  666 */         IGenerator<ResultRow> generator = P8CE_ResultRowImpl.getGenerator();
/*  667 */         result = new P8CE_PageableSetImpl(this.repository, jaceRowSet, supportsPaging, generator);
/*      */       }
/*      */       
/*  670 */       Tracer.traceMethodExit(new Object[] { result });
/*  671 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  675 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  679 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SEARCH_FOR_ROWS_FAILED, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  683 */       if (establishedSubject) {
/*  684 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PageableSet<CBRResult> contentBasedRetrieval(RMSearch searchObj, RMContentSearchDefinition srchDef, Integer pageSize, Boolean continuable)
/*      */   {
/*  693 */     Tracer.traceMethodEntry(new Object[] { searchObj, srchDef, pageSize, continuable });
/*  694 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  697 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  699 */       P8CE_CBRSearch rmSearch = new P8CE_CBRSearch(searchObj.getRepositories()[0]);
/*  700 */       PageableSet<CBRResult> result = rmSearch.initCBRSearch(srchDef, pageSize.intValue(), continuable.booleanValue());
/*  701 */       Tracer.traceMethodExit(new Object[] { result });
/*  702 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  706 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  710 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SEARCH_FOR_ROWS_FAILED, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  714 */       if (establishedSubject) {
/*  715 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMRoles fetchRMRoles(Repository repository, String applicationName)
/*      */   {
/*  724 */     Tracer.traceMethodEntry(new Object[] { repository, applicationName });
/*  725 */     Util.ckNullObjParam("repository", repository);
/*      */     
/*  727 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  730 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  732 */       RMRoles result = new P8CE_RMRolesImpl(repository, applicationName);
/*  733 */       Tracer.traceMethodExit(new Object[] { result });
/*  734 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  738 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  742 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CURRENT_USER_FAILED, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  746 */       if (establishedSubject) {
/*  747 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMUser fetchCurrentUser()
/*      */   {
/*  756 */     Tracer.traceMethodEntry(new Object[0]);
/*  757 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  760 */       establishedSubject = P8CE_Util.associateSubject();
/*  761 */       RMUser result = null;
/*      */       
/*  763 */       User jaceUser = P8CE_Util.fetchCurrentJaceUser(this.jaceDomain);
/*  764 */       if (jaceUser != null) {
/*  765 */         result = new P8CE_RMUserImpl(jaceUser);
/*      */       }
/*  767 */       Tracer.traceMethodExit(new Object[] { result });
/*  768 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  772 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  776 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CURRENT_USER_FAILED, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  780 */       if (establishedSubject) {
/*  781 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMProperties getProperties()
/*      */   {
/*  791 */     Tracer.traceMethodEntry(new Object[0]);
/*  792 */     RMProperties result = null;
/*  793 */     Domain domain = getOrFetchJaceDomain();
/*  794 */     if (domain != null)
/*      */     {
/*  796 */       result = new P8CE_RMPropertiesImpl(domain, this);
/*      */     }
/*      */     
/*  799 */     Tracer.traceMethodExit(new Object[] { result });
/*  800 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Container> getContainedBy()
/*      */   {
/*  809 */     Tracer.traceMethodEntry(new Object[0]);
/*  810 */     List<Container> result = Collections.EMPTY_LIST;
/*  811 */     Tracer.traceMethodExit(new Object[] { result });
/*  812 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<RMPermission> getPermissions()
/*      */   {
/*  820 */     Tracer.traceMethodEntry(new Object[0]);
/*  821 */     Domain jaceDomain = getOrFetchJaceDomain();
/*  822 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(jaceDomain);
/*  823 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*      */     
/*  825 */     Tracer.traceMethodExit(new Object[] { result });
/*  826 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PageableSet<RMGroup> findGroups(String searchPattern, RMPrincipalSearchType searchType, RMPrincipalSearchAttribute searchAttr, RMPrincipalSearchSortType sortType, Integer pageSize)
/*      */   {
/*  836 */     Tracer.traceMethodEntry(new Object[] { searchPattern, searchType, searchAttr, sortType, pageSize });
/*  837 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  840 */       establishedSubject = P8CE_Util.associateSubject();
/*  841 */       PageableSet<RMGroup> result = null;
/*      */       
/*  843 */       PropertyFilter jacePF = new PropertyFilter();
/*  844 */       List<FilterElement> mandatoryFEs = P8CE_RMGroupImpl.getMandatoryJaceFEs();
/*  845 */       for (FilterElement fe : mandatoryFEs)
/*      */       {
/*  847 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*  849 */       PrincipalSearchType jaceSearchType = P8CE_Util.convertToJacePrinSearchType(searchType);
/*  850 */       PrincipalSearchAttribute jaceSearchAttr = P8CE_Util.convertToJacePrinSearchAttr(searchAttr);
/*  851 */       PrincipalSearchSortType jaceSortType = P8CE_Util.convertToJacePrinSortType(sortType);
/*      */       
/*  853 */       long startTime = System.currentTimeMillis();
/*  854 */       Realm jaceCurrentRealm = P8CE_Util.fetchJaceRealm(this);
/*  855 */       GroupSet jaceGroupSet = jaceCurrentRealm.findGroups(searchPattern, jaceSearchType, jaceSearchAttr, jaceSortType, pageSize, jacePF);
/*  856 */       long stopTime = System.currentTimeMillis();
/*  857 */       Boolean elementCountIndicator = null;
/*  858 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  860 */         elementCountIndicator = jaceGroupSet != null ? Boolean.valueOf(jaceGroupSet.isEmpty()) : null;
/*      */       }
/*  862 */       Tracer.traceExtCall("Realm.findGroups", startTime, stopTime, elementCountIndicator, jaceGroupSet, new Object[] { searchPattern, jaceSearchType, jaceSearchAttr, jaceSortType, pageSize, jacePF });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  867 */       boolean supportsPaging = true;
/*  868 */       IGenerator<RMGroup> generator = P8CE_RMGroupImpl.getGenerator();
/*  869 */       result = new P8CE_PageableSetImpl(this.repository, jaceGroupSet, supportsPaging, generator);
/*      */       
/*  871 */       Tracer.traceMethodExit(new Object[] { result });
/*  872 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  876 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  880 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_USERGROUPS_FAILED, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  884 */       if (establishedSubject) {
/*  885 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PageableSet<RMUser> findUsers(String searchPattern, RMPrincipalSearchType searchType, RMPrincipalSearchAttribute searchAttr, RMPrincipalSearchSortType sortType, Integer pageSize)
/*      */   {
/*  896 */     Tracer.traceMethodEntry(new Object[] { searchPattern, searchType, searchAttr, sortType, pageSize });
/*  897 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  900 */       establishedSubject = P8CE_Util.associateSubject();
/*  901 */       PageableSet<RMUser> result = null;
/*      */       
/*  903 */       PropertyFilter jacePF = new PropertyFilter();
/*  904 */       List<FilterElement> mandatoryFEs = P8CE_RMUserImpl.getMandatoryJaceFEs();
/*  905 */       for (FilterElement fe : mandatoryFEs)
/*      */       {
/*  907 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*  909 */       PrincipalSearchType jaceSearchType = P8CE_Util.convertToJacePrinSearchType(searchType);
/*  910 */       PrincipalSearchAttribute jaceSearchAttr = P8CE_Util.convertToJacePrinSearchAttr(searchAttr);
/*  911 */       PrincipalSearchSortType jaceSortType = P8CE_Util.convertToJacePrinSortType(sortType);
/*      */       
/*  913 */       long startTime = System.currentTimeMillis();
/*  914 */       Realm jaceCurrentRealm = P8CE_Util.fetchJaceRealm(this);
/*  915 */       UserSet jaceUserSet = jaceCurrentRealm.findUsers(searchPattern, jaceSearchType, jaceSearchAttr, jaceSortType, pageSize, jacePF);
/*  916 */       long stopTime = System.currentTimeMillis();
/*  917 */       Boolean elementCountIndicator = null;
/*  918 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  920 */         elementCountIndicator = jaceUserSet != null ? Boolean.valueOf(jaceUserSet.isEmpty()) : null;
/*      */       }
/*  922 */       Tracer.traceExtCall("Realm.findUsers", startTime, stopTime, elementCountIndicator, jaceUserSet, new Object[] { searchPattern, jaceSearchType, jaceSearchAttr, jaceSortType, pageSize, jacePF });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  927 */       boolean supportsPaging = true;
/*  928 */       IGenerator<RMUser> generator = P8CE_RMUserImpl.getGenerator();
/*  929 */       result = new P8CE_PageableSetImpl(this.repository, jaceUserSet, supportsPaging, generator);
/*      */       
/*  931 */       Tracer.traceMethodExit(new Object[] { result });
/*  932 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  936 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  940 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_USERS_FAILED, new Object[0]);
/*      */     }
/*      */     finally
/*      */     {
/*  944 */       if (establishedSubject) {
/*  945 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  955 */     String ident = "<unknown>";
/*  956 */     if (P8CE_Util.isJacePropertyPresent(this, "Name")) {
/*  957 */       ident = getName();
/*  958 */     } else if (P8CE_Util.isJacePropertyPresent(this, "Id")) {
/*  959 */       ident = getObjectIdentity();
/*  960 */     } else if (getClientIdentifier() != null) {
/*  961 */       ident = getClientIdentifier();
/*      */     }
/*  963 */     return "P8CE_Domain: '" + ident + "'";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void refresh()
/*      */   {
/*  971 */     Tracer.traceMethodEntry(new Object[0]);
/*  972 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  975 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  977 */       long startTime = System.currentTimeMillis();
/*  978 */       this.jaceDomain.refresh();
/*  979 */       long endTime = System.currentTimeMillis();
/*  980 */       Tracer.traceExtCall("Domain.refresh", startTime, endTime, null, null, new Object[0]);
/*      */       
/*  982 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  986 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  990 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/*  994 */       if (establishedSubject) {
/*  995 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refresh(RMPropertyFilter jarmFilter)
/*      */   {
/* 1004 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1005 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1008 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/* 1012 */       List<FilterElement> mandatoryRecordFEs = getMandatoryJaceFEs();
/* 1013 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*      */       
/* 1015 */       long startTime = System.currentTimeMillis();
/* 1016 */       this.jaceDomain.refresh(jacePF);
/* 1017 */       long endTime = System.currentTimeMillis();
/* 1018 */       Tracer.traceExtCall("Domain.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       
/* 1020 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1024 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1028 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1032 */       if (establishedSubject) {
/* 1033 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void refresh(String[] symbolicPropertyNames)
/*      */   {
/* 1042 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 1043 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1046 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/* 1049 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 1050 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 1051 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 1052 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 1053 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*      */       
/* 1055 */       long startTime = System.currentTimeMillis();
/* 1056 */       this.jaceDomain.refresh(jacePF);
/* 1057 */       long endTime = System.currentTimeMillis();
/* 1058 */       Tracer.traceExtCall("Domain.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */       
/* 1060 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1064 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1068 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1072 */       if (establishedSubject) {
/* 1073 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Integer getAccessAllowed()
/*      */   {
/* 1082 */     return this.jaceDomain.getAccessAllowed();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*      */   {
/* 1090 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 1091 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "internalSave", getEntityType(), getClientIdentifier() });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Connection getJaceConn()
/*      */   {
/* 1102 */     Tracer.traceMethodEntry(new Object[0]);
/* 1103 */     Connection result = null;
/*      */     
/* 1105 */     if (this.jaceDomain != null)
/*      */     {
/* 1107 */       result = this.jaceDomain.getConnection();
/*      */     }
/* 1109 */     else if (this.domainConn != null)
/*      */     {
/* 1111 */       result = ((P8CE_DomainConnectionImpl)this.domainConn).getJaceConnection();
/*      */     }
/*      */     
/* 1114 */     Tracer.traceMethodExit(new Object[] { result });
/* 1115 */     return result;
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
/*      */   Domain getOrFetchJaceDomain()
/*      */   {
/* 1130 */     Tracer.traceMethodEntry(new Object[0]);
/* 1131 */     if (this.jaceDomain == null)
/*      */     {
/* 1133 */       PropertyFilter jacePF = P8CE_Util.buildMandatoryJacePF(this);
/* 1134 */       this.jaceDomain = fetchJaceDomain(this.domainConn, getClientIdentifier(), jacePF);
/*      */     }
/*      */     
/* 1137 */     Tracer.traceMethodExit(new Object[] { this.jaceDomain });
/* 1138 */     return this.jaceDomain;
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
/*      */   static Domain fetchJaceDomain(DomainConnection conn, String ident, PropertyFilter jaceFilter)
/*      */   {
/* 1155 */     Tracer.traceMethodEntry(new Object[] { conn, ident, jaceFilter });
/* 1156 */     Connection jaceConnection = ((P8CE_DomainConnectionImpl)conn).getJaceConnection();
/*      */     
/* 1158 */     long startTime = System.currentTimeMillis();
/* 1159 */     Domain result = Factory.Domain.fetchInstance(jaceConnection, ident, jaceFilter);
/* 1160 */     Tracer.traceExtCall("Factory.Domain.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), result, new Object[] { jaceConnection, ident, jaceFilter });
/*      */     
/*      */ 
/* 1163 */     Tracer.traceMethodExit(new Object[] { result });
/* 1164 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   private SearchScope createJaceSearchScope(RMSearch rmSearch)
/*      */   {
/* 1170 */     Tracer.traceMethodEntry(new Object[] { rmSearch });
/* 1171 */     SearchScope result = null;
/*      */     
/* 1173 */     Repository[] repositories = rmSearch.getRepositories();
/* 1174 */     if (repositories.length > 1)
/*      */     {
/* 1176 */       ObjectStore[] jaceObjStores = new ObjectStore[repositories.length];
/* 1177 */       for (int i = 0; i < repositories.length; i++)
/*      */       {
/* 1179 */         jaceObjStores[i] = ((P8CE_RepositoryImpl)repositories[i]).getJaceObjectStore();
/*      */       }
/* 1181 */       RMMergeMode rmMode = rmSearch.getMergeMode();
/* 1182 */       MergeMode jaceMode = rmMode == RMMergeMode.Intersection ? MergeMode.INTERSECTION : MergeMode.UNION;
/* 1183 */       result = new SearchScope(jaceObjStores, jaceMode);
/*      */     }
/*      */     else
/*      */     {
/* 1187 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repositories[0]).getJaceObjectStore();
/* 1188 */       result = new SearchScope(jaceObjStore);
/*      */     }
/*      */     
/* 1191 */     Tracer.traceMethodExit(new Object[] { result });
/* 1192 */     return result;
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
/*      */   static class Generator
/*      */     implements IGenerator<RMDomain>
/*      */   {
/*      */     public RMDomain create(Repository repository, Object jaceBaseObject)
/*      */     {
/* 1208 */       P8CE_RMDomainImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 1209 */       Domain jaceDomain = (Domain)jaceBaseObject;
/*      */       
/* 1211 */       Connection jaceConn = jaceDomain.getConnection();
/* 1212 */       String url = jaceConn.getURI();
/* 1213 */       DomainConnection rmDomainConn = new P8CE_DomainConnectionImpl(url, null);
/*      */       
/* 1215 */       String domainName = jaceDomain.get_Name();
/* 1216 */       RMDomain result = new P8CE_RMDomainImpl(rmDomainConn, domainName, jaceDomain);
/*      */       
/* 1218 */       P8CE_RMDomainImpl.Tracer.traceMethodExit(new Object[] { result });
/* 1219 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMDomainImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */