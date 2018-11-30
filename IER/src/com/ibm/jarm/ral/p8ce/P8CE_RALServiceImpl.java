/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.admin.ChoiceList;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.core.Connection;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.Factory.ChoiceList;
/*      */ import com.filenet.api.core.Factory.ClassDescription;
/*      */ import com.filenet.api.core.Factory.CustomObject;
/*      */ import com.filenet.api.core.Factory.Document;
/*      */ import com.filenet.api.core.Factory.Folder;
/*      */ import com.filenet.api.core.Factory.Group;
/*      */ import com.filenet.api.core.Factory.Link;
/*      */ import com.filenet.api.core.Factory.MarkingSet;
/*      */ import com.filenet.api.core.Factory.ObjectStore;
/*      */ import com.filenet.api.core.Factory.User;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.core.Link;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.meta.ClassDescription;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.security.Group;
/*      */ import com.filenet.api.security.User;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.filenet.api.util.UserContext;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*      */ import com.ibm.jarm.api.constants.DomainType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RepositoryType;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.ClassificationGuide;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.ContentItem;
/*      */ import com.ibm.jarm.api.core.ContentRepository;
/*      */ import com.ibm.jarm.api.core.DispositionAction;
/*      */ import com.ibm.jarm.api.core.DispositionTrigger;
/*      */ import com.ibm.jarm.api.core.DomainConnection;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.Hold;
/*      */ import com.ibm.jarm.api.core.Location;
/*      */ import com.ibm.jarm.api.core.RMCustomObject;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.RMLink;
/*      */ import com.ibm.jarm.api.core.Record;
/*      */ import com.ibm.jarm.api.core.ReportDefinition;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.meta.RMChoiceList;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.meta.RMMarkingSet;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*      */ import com.ibm.jarm.api.security.RMGroup;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.security.RMUser;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.RALService;
/*      */ import com.ibm.jarm.ral.common.RALBaseContainer;
/*      */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*      */ import com.ibm.jarm.ral.common.RALBulkOperation;
/*      */ import com.ibm.jarm.ral.p8ce.cbr.P8CE_RMContentSearchDefinition;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javax.security.auth.Subject;
/*      */ 
/*      */ public class P8CE_RALServiceImpl implements RALService
/*      */ {
/*   81 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DomainType getDomainType()
/*      */   {
/*   88 */     return DomainType.P8_CE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DomainConnection createDomainConnection(String url, Map<String, Object> connectionInfo)
/*      */   {
/*   96 */     Tracer.traceMethodEntry(new Object[] { url, connectionInfo });
/*   97 */     DomainConnection result = null;
/*      */     
/*   99 */     String trimmedUrl = Util.ckInvalidStrParam("url", url);
/*  100 */     result = new P8CE_DomainConnectionImpl(trimmedUrl, connectionInfo);
/*      */     
/*  102 */     Tracer.traceMethodExit(new Object[] { result });
/*  103 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Subject createSubject(DomainConnection conn, String username, String password, String stanzaName)
/*      */   {
/*  111 */     Tracer.traceMethodEntry(new Object[] { conn, username, "******", stanzaName });
/*  112 */     Util.ckNullObjParam("conn", conn);
/*  113 */     Util.ckInvalidStrParam("username", username);
/*      */     
/*  115 */     Connection jaceConn = ((P8CE_DomainConnectionImpl)conn).getJaceConnection();
/*      */     try
/*      */     {
/*  118 */       long startTime = System.currentTimeMillis();
/*  119 */       Subject result = UserContext.createSubject(jaceConn, username, password, stanzaName);
/*  120 */       Tracer.traceExtCall("UserContext.createSubject()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceConn, new Object[] { username, "******", stanzaName });
/*      */       
/*      */ 
/*  123 */       Tracer.traceMethodExit(new Object[0]);
/*  124 */       return result;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  128 */       String url = jaceConn != null ? jaceConn.getURI() : "";
/*  129 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_JAAS_SUBJECT_NOT_AVAILABLE, new Object[] { getDomainType(), url, username, stanzaName });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMDomain fetchDomain(DomainConnection domainConn, String domainIdent, RMPropertyFilter filter)
/*      */   {
/*  139 */     Tracer.traceMethodEntry(new Object[] { domainConn, domainIdent, filter });
/*  140 */     Util.ckNullObjParam("domainConn", domainConn);
/*      */     
/*  142 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  145 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  147 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, P8CE_RMDomainImpl.getMandatoryJaceFEs());
/*  148 */       Domain jaceDomain = P8CE_RMDomainImpl.fetchJaceDomain(domainConn, domainIdent, jacePF);
/*  149 */       RMDomain result = new P8CE_RMDomainImpl(domainConn, domainIdent, jaceDomain);
/*      */       
/*  151 */       Tracer.traceMethodExit(new Object[] { result });
/*  152 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  156 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  160 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { domainIdent, EntityType.Domain });
/*      */     }
/*      */     finally
/*      */     {
/*  164 */       if (establishedSubject) {
/*  165 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public FilePlanRepository fetchFilePlanRepository(RMDomain domain, String ident, RMPropertyFilter filter)
/*      */   {
/*  174 */     Tracer.traceMethodEntry(new Object[] { domain, ident, filter });
/*  175 */     FilePlanRepository result = (FilePlanRepository)fetchRepository(domain, RepositoryType.FilePlan, ident, filter);
/*      */     
/*  177 */     Tracer.traceMethodExit(new Object[] { result });
/*  178 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilePlanRepository getFilePlanRepository(RMDomain domain, String ident)
/*      */   {
/*  187 */     Tracer.traceMethodEntry(new Object[] { domain, ident });
/*  188 */     FilePlanRepository result = (FilePlanRepository)getRepository(domain, RepositoryType.FilePlan, ident);
/*      */     
/*  190 */     Tracer.traceMethodExit(new Object[] { result });
/*  191 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ContentRepository fetchContentRepository(RMDomain domain, String ident, RMPropertyFilter filter)
/*      */   {
/*  199 */     Tracer.traceMethodEntry(new Object[] { domain, ident, filter });
/*  200 */     ContentRepository result = (ContentRepository)fetchRepository(domain, RepositoryType.Content, ident, filter);
/*      */     
/*  202 */     Tracer.traceMethodExit(new Object[] { result });
/*  203 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ContentRepository getContentRepository(RMDomain domain, String ident)
/*      */   {
/*  211 */     Tracer.traceMethodEntry(new Object[] { domain, ident });
/*  212 */     ContentRepository result = (ContentRepository)getRepository(domain, RepositoryType.Content, ident);
/*      */     
/*  214 */     Tracer.traceMethodExit(new Object[] { result });
/*  215 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Repository fetchRepository(RMDomain domain, String ident, RMPropertyFilter filter)
/*      */   {
/*  223 */     Tracer.traceMethodEntry(new Object[] { domain, ident, filter });
/*  224 */     Repository result = fetchRepository(domain, RepositoryType.Unknown, ident, filter);
/*      */     
/*  226 */     Tracer.traceMethodExit(new Object[] { result });
/*  227 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Repository getRepository(RMDomain domain, String ident)
/*      */   {
/*  235 */     Tracer.traceMethodEntry(new Object[] { domain, ident });
/*  236 */     Repository result = getRepository(domain, RepositoryType.Plain, ident);
/*      */     
/*  238 */     Tracer.traceMethodExit(new Object[] { result });
/*  239 */     return result;
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
/*      */   private Repository fetchRepository(RMDomain domain, RepositoryType desiredRepositoryType, String ident, RMPropertyFilter filter)
/*      */   {
/*  259 */     Tracer.traceMethodEntry(new Object[] { domain, desiredRepositoryType, ident, filter });
/*  260 */     Util.ckNullObjParam("domain", domain);
/*  261 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/*  263 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  266 */       establishedSubject = P8CE_Util.associateSubject();
/*  267 */       List<FilterElement> mandatoryJaceFEs = null;
/*  268 */       switch (desiredRepositoryType)
/*      */       {
/*      */       case FilePlan: 
/*      */       case Unknown: 
/*  272 */         mandatoryJaceFEs = P8CE_FilePlanRepositoryImpl.getMandatoryJaceFEs();
/*  273 */         break;
/*      */       case Content: 
/*  275 */         mandatoryJaceFEs = P8CE_ContentRepositoryImpl.getMandatoryJaceFEs();
/*  276 */         break;
/*      */       case Plain: 
/*      */       default: 
/*  279 */         mandatoryJaceFEs = P8CE_RepositoryImpl.getMandatoryJaceFEs();
/*      */       }
/*      */       
/*      */       
/*  283 */       Domain jaceDomain = ((P8CE_RMDomainImpl)domain).getOrFetchJaceDomain();
/*  284 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, mandatoryJaceFEs);
/*      */       
/*  286 */       long startTime = System.currentTimeMillis();
/*  287 */       ObjectStore jaceObjStore = null;
/*  288 */       if (Id.isId(ident))
/*      */       {
/*  290 */         Id objStoreId = new Id(ident);
/*  291 */         jaceObjStore = Factory.ObjectStore.fetchInstance(jaceDomain, objStoreId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/*  295 */         jaceObjStore = Factory.ObjectStore.fetchInstance(jaceDomain, ident, jacePF);
/*      */       }
/*  297 */       Tracer.traceExtCall("Factory.ObjectStore.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceDomain, new Object[] { ident, jacePF });
/*      */       
/*      */ 
/*  300 */       Repository result = null;
/*  301 */       boolean isPlaceholder = false;
/*  302 */       RepositoryType actualRepositoryType = desiredRepositoryType;
/*  303 */       if (actualRepositoryType == RepositoryType.Unknown)
/*      */       {
/*      */ 
/*  306 */         if (P8CE_FilePlanRepositoryImpl.isValidFPOS(jaceObjStore)) {
/*  307 */           actualRepositoryType = RepositoryType.FilePlan;
/*  308 */         } else if (P8CE_ContentRepositoryImpl.isValidROS(jaceObjStore)) {
/*  309 */           actualRepositoryType = RepositoryType.Content;
/*      */         } else
/*  311 */           actualRepositoryType = RepositoryType.Plain;
/*      */       }
/*      */       boolean performFPOSValidation;
/*  314 */       switch (actualRepositoryType)
/*      */       {
/*      */       case FilePlan: 
/*  317 */         performFPOSValidation = true;
/*  318 */         result = new P8CE_FilePlanRepositoryImpl(domain, ident, jaceObjStore, performFPOSValidation, isPlaceholder);
/*  319 */         break;
/*      */       case Content: 
/*  321 */         boolean performROSValidation = true;
/*  322 */         result = new P8CE_ContentRepositoryImpl((P8CE_RMDomainImpl)domain, ident, jaceObjStore, performROSValidation, isPlaceholder);
/*  323 */         break;
/*      */       case Unknown: case Plain: 
/*      */       default: 
/*  326 */         result = new P8CE_RepositoryImpl((P8CE_RMDomainImpl)domain, ident, jaceObjStore, isPlaceholder);
/*      */       }
/*      */       
/*      */       
/*  330 */       Tracer.traceMethodExit(new Object[] { result });
/*  331 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  335 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  339 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, EntityType.Repository });
/*      */     }
/*      */     finally
/*      */     {
/*  343 */       if (establishedSubject) {
/*  344 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   private Repository getRepository(RMDomain domain, RepositoryType repositoryType, String ident)
/*      */   {
/*  363 */     Tracer.traceMethodEntry(new Object[] { domain, repositoryType, ident });
/*  364 */     Util.ckNullObjParam("domain", domain);
/*  365 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/*  367 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  370 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  372 */       Domain jaceDomain = ((P8CE_RMDomainImpl)domain).getOrFetchJaceDomain();
/*  373 */       ObjectStore jaceObjStore = null;
/*  374 */       if (Id.isId(ident))
/*      */       {
/*  376 */         Id objStoreId = new Id(ident);
/*  377 */         jaceObjStore = Factory.ObjectStore.getInstance(jaceDomain, objStoreId);
/*      */       }
/*      */       else
/*      */       {
/*  381 */         jaceObjStore = Factory.ObjectStore.getInstance(jaceDomain, ident);
/*      */       }
/*      */       
/*  384 */       Repository result = null;
/*  385 */       boolean isPlaceholder = true;
/*  386 */       boolean performFPOSValidation; switch (repositoryType)
/*      */       {
/*      */       case FilePlan: 
/*  389 */         performFPOSValidation = false;
/*  390 */         result = new P8CE_FilePlanRepositoryImpl(domain, ident, jaceObjStore, performFPOSValidation, isPlaceholder);
/*  391 */         break;
/*      */       case Content: 
/*  393 */         boolean performROSValidation = false;
/*  394 */         result = new P8CE_ContentRepositoryImpl((P8CE_RMDomainImpl)domain, ident, jaceObjStore, performROSValidation, isPlaceholder);
/*  395 */         break;
/*      */       case Unknown: case Plain: 
/*      */       default: 
/*  398 */         result = new P8CE_RepositoryImpl((P8CE_RMDomainImpl)domain, ident, jaceObjStore, isPlaceholder);
/*      */       }
/*      */       
/*      */       
/*  402 */       Tracer.traceMethodExit(new Object[] { result });
/*  403 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  407 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  411 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, EntityType.Repository });
/*      */     }
/*      */     finally
/*      */     {
/*  415 */       if (establishedSubject) {
/*  416 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Container fetchContainer(Repository repository, EntityType desiredEntityType, String ident, RMPropertyFilter filter)
/*      */   {
/*  425 */     Tracer.traceMethodEntry(new Object[] { repository, desiredEntityType, ident, filter });
/*  426 */     Util.ckNullObjParam("repository", repository);
/*  427 */     Util.ckNullObjParam("desiredEntityType", desiredEntityType);
/*  428 */     Util.ckInvalidStrParam("ident", ident);
/*  429 */     boolean establishedSubject = false;
/*      */     
/*      */     try
/*      */     {
/*  433 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  435 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  436 */       List<FilterElement> mandatoryFEs = P8CE_Util.getEntityTypeMandatoryFEs(desiredEntityType);
/*  437 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, mandatoryFEs);
/*      */       
/*  439 */       long startTime = System.currentTimeMillis();
/*  440 */       Folder jaceFolder = null;
/*  441 */       if (Id.isId(ident))
/*      */       {
/*  443 */         Id folderId = new Id(ident);
/*  444 */         jaceFolder = Factory.Folder.fetchInstance(jaceObjStore, folderId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/*  448 */         jaceFolder = Factory.Folder.fetchInstance(jaceObjStore, ident, jacePF);
/*      */       }
/*  450 */       Tracer.traceExtCall("Factory.Folder.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceObjStore, new Object[] { ident, jacePF });
/*      */       
/*      */ 
/*  453 */       EntityType actualEntityType = desiredEntityType;
/*  454 */       Integer propEntityTypeValue; if ((jaceFolder != null) && (jaceFolder.getProperties().isPropertyPresent("RMEntityType")))
/*      */       {
/*      */ 
/*  457 */         Properties jaceFldrProps = jaceFolder.getProperties();
/*  458 */         propEntityTypeValue = jaceFldrProps.getInteger32Value("RMEntityType");
/*  459 */         if (propEntityTypeValue != null)
/*      */         {
/*  461 */           actualEntityType = EntityType.getInstanceFromInt(propEntityTypeValue.intValue());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  466 */           if (desiredEntityType != EntityType.Container)
/*      */           {
/*  468 */             if (desiredEntityType == EntityType.RecordFolder)
/*      */             {
/*  470 */               if (!RALBaseContainer.isARecordFolderType(actualEntityType)) {
/*  471 */                 throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_WRONG_ENTITY_TYPE_FOR_SPECIFIED_CONTAINER, new Object[] { ident, desiredEntityType, actualEntityType });
/*      */               }
/*  473 */             } else if (actualEntityType != desiredEntityType)
/*      */             {
/*  475 */               throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_WRONG_ENTITY_TYPE_FOR_SPECIFIED_CONTAINER, new Object[] { ident, desiredEntityType, actualEntityType });
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  481 */           String[] mandatoryPropNames = P8CE_Util.getEntityRequiredPropertyNames(actualEntityType);
/*  482 */           boolean atLeastOneMandatoryPropIsMissing = false;
/*  483 */           for (String mandatoryPropName : mandatoryPropNames)
/*      */           {
/*  485 */             if (!jaceFldrProps.isPropertyPresent(mandatoryPropName))
/*      */             {
/*  487 */               atLeastOneMandatoryPropIsMissing = true;
/*  488 */               break;
/*      */             }
/*      */           }
/*      */           
/*  492 */           if (atLeastOneMandatoryPropIsMissing)
/*      */           {
/*      */ 
/*      */ 
/*  496 */             List<FilterElement> actualMandatoryFEs = P8CE_Util.getEntityTypeMandatoryFEs(actualEntityType);
/*  497 */             jacePF = P8CE_Util.convertToJacePF(filter, actualMandatoryFEs);
/*  498 */             jaceFolder.refresh(jacePF);
/*      */           }
/*      */         }
/*      */       }
/*  502 */       Container result = createNewContainer(repository, actualEntityType, ident, jaceFolder, false);
/*      */       
/*  504 */       Tracer.traceMethodExit(new Object[] { result });
/*  505 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  509 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  513 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, desiredEntityType });
/*      */     }
/*      */     finally
/*      */     {
/*  517 */       if (establishedSubject) {
/*  518 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Container getContainer(Repository repository, EntityType entityType, String ident)
/*      */   {
/*  527 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident });
/*  528 */     Util.ckNullObjParam("repository", repository);
/*  529 */     Util.ckNullObjParam("entityType", entityType);
/*  530 */     Util.ckInvalidStrParam("ident", ident);
/*  531 */     boolean establishedSubject = false;
/*      */     
/*      */     try
/*      */     {
/*  535 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  537 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*      */       
/*  539 */       Folder jaceFolder = null;
/*  540 */       String className = P8CE_Util.getEntityClassName(entityType);
/*  541 */       if (Id.isId(ident))
/*      */       {
/*  543 */         Id folderId = new Id(ident);
/*  544 */         jaceFolder = Factory.Folder.getInstance(jaceObjStore, className, folderId);
/*      */       }
/*      */       else
/*      */       {
/*  548 */         jaceFolder = Factory.Folder.getInstance(jaceObjStore, className, ident);
/*      */       }
/*      */       
/*  551 */       Container result = createNewContainer(repository, entityType, ident, jaceFolder, true);
/*      */       
/*  553 */       Tracer.traceMethodExit(new Object[] { result });
/*  554 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  558 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  562 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, entityType });
/*      */     }
/*      */     finally
/*      */     {
/*  566 */       if (establishedSubject) {
/*  567 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMCustomObject fetchCustomObject(Repository repository, EntityType entityType, String ident, RMPropertyFilter filter)
/*      */   {
/*  577 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident, filter });
/*  578 */     Util.ckNullObjParam("repository", repository);
/*  579 */     Util.ckNullObjParam("entityType", entityType);
/*  580 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/*  582 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  585 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  587 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  588 */       List<FilterElement> mandatoryFEs = P8CE_Util.getEntityTypeMandatoryFEs(entityType);
/*  589 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, mandatoryFEs);
/*      */       
/*  591 */       long startTime = System.currentTimeMillis();
/*  592 */       CustomObject jaceCustomObj = null;
/*  593 */       if (Id.isId(ident))
/*      */       {
/*  595 */         Id customObjId = new Id(ident);
/*  596 */         jaceCustomObj = Factory.CustomObject.fetchInstance(jaceObjStore, customObjId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/*  600 */         jaceCustomObj = Factory.CustomObject.fetchInstance(jaceObjStore, ident, jacePF);
/*      */       }
/*  602 */       long stopTime = System.currentTimeMillis();
/*  603 */       Tracer.traceExtCall("Factory.CustomObject.fetchInstance()", startTime, stopTime, Integer.valueOf(1), jaceObjStore, new Object[] { ident, jacePF });
/*      */       
/*      */ 
/*  606 */       RMCustomObject result = createNewCustomObject(repository, entityType, ident, jaceCustomObj, false);
/*      */       
/*  608 */       Tracer.traceMethodExit(new Object[] { result });
/*  609 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  613 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  617 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, entityType });
/*      */     }
/*      */     finally
/*      */     {
/*  621 */       if (establishedSubject) {
/*  622 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMCustomObject getCustomObject(Repository repository, EntityType entityType, String ident)
/*      */   {
/*  631 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident });
/*  632 */     Util.ckNullObjParam("repository", repository);
/*  633 */     Util.ckNullObjParam("entityType", entityType);
/*  634 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/*  636 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  639 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  641 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*      */       
/*  643 */       CustomObject jaceCustomObj = null;
/*  644 */       String className = P8CE_Util.getEntityClassName(entityType);
/*  645 */       if ((className == null) || (className.trim().length() == 0)) {
/*  646 */         className = "CustomObject";
/*      */       }
/*  648 */       if (Id.isId(ident))
/*      */       {
/*  650 */         Id customObjId = new Id(ident);
/*  651 */         jaceCustomObj = Factory.CustomObject.getInstance(jaceObjStore, className, customObjId);
/*      */       }
/*      */       else
/*      */       {
/*  655 */         jaceCustomObj = Factory.CustomObject.getInstance(jaceObjStore, className, ident);
/*      */       }
/*      */       
/*  658 */       RMCustomObject result = createNewCustomObject(repository, entityType, ident, jaceCustomObj, true);
/*      */       
/*  660 */       Tracer.traceMethodExit(new Object[] { result });
/*  661 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  665 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  669 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, entityType });
/*      */     }
/*      */     finally
/*      */     {
/*  673 */       if (establishedSubject) {
/*  674 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DispositionAction createDispositionAction(Repository repository, DispositionActionType actionType, String idStr)
/*      */   {
/*  683 */     Tracer.traceMethodEntry(new Object[] { repository, actionType, idStr });
/*  684 */     RMCustomObject result = createCustomObject(repository, EntityType.DispositionAction, idStr);
/*  685 */     result.getProperties().putIntegerValue("ActionType", Integer.valueOf(actionType.getIntValue()));
/*      */     
/*  687 */     Tracer.traceMethodExit(new Object[] { result });
/*  688 */     return (DispositionAction)result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionTrigger createDispositionTrigger(Repository repository, DispositionTriggerType triggerType, String idStr)
/*      */   {
/*  696 */     Tracer.traceMethodEntry(new Object[] { repository, triggerType, idStr });
/*  697 */     RMCustomObject result = createCustomObject(repository, EntityType.DispositionTrigger, idStr);
/*  698 */     result.getProperties().putIntegerValue("EventType", Integer.valueOf(triggerType.getIntValue()));
/*      */     
/*  700 */     Tracer.traceMethodExit(new Object[] { result });
/*  701 */     return (DispositionTrigger)result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Location createLocation(Repository repository, String idStr)
/*      */   {
/*  709 */     Tracer.traceMethodEntry(new Object[] { repository, idStr });
/*  710 */     RMCustomObject result = createCustomObject(repository, EntityType.Location, idStr);
/*      */     
/*  712 */     Tracer.traceMethodExit(new Object[] { result });
/*  713 */     return (Location)result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMCustomObject createCustomObject(Repository repository, EntityType entityType, String idStr)
/*      */   {
/*  722 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, idStr });
/*  723 */     Util.ckNullObjParam("repository", repository);
/*  724 */     Util.ckNullObjParam("entityType", entityType);
/*      */     
/*  726 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  729 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  731 */       Id newId = P8CE_Util.processIdStr(idStr);
/*  732 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  733 */       String className = P8CE_Util.getEntityClassName(entityType);
/*  734 */       CustomObject jaceCustomObj = Factory.CustomObject.createInstance(jaceObjStore, className, newId);
/*      */       
/*  736 */       IGenerator<RMCustomObject> generator = P8CE_Util.getEntityGenerator(entityType);
/*  737 */       RMCustomObject result = (RMCustomObject)generator.create(repository, jaceCustomObj);
/*      */       
/*  739 */       Tracer.traceMethodExit(new Object[] { result });
/*  740 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  744 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  748 */       String repositoryIdent = repository.getSymbolicName();
/*  749 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_UNABLE_TO_CREATE_NEW_OBJECT_INSTANCE, new Object[] { entityType, repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  753 */       if (establishedSubject) {
/*  754 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMLink fetchRMLink(FilePlanRepository fpRepository, String rmLinkId, RMPropertyFilter filter)
/*      */   {
/*  763 */     Tracer.traceMethodEntry(new Object[] { fpRepository, rmLinkId, filter });
/*  764 */     Util.ckNullObjParam("fpRepository", fpRepository);
/*  765 */     Util.ckInvalidStrParam("rmLinkId", rmLinkId);
/*      */     
/*  767 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  770 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  772 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/*  773 */       List<FilterElement> mandatoryFEs = P8CE_Util.getEntityTypeMandatoryFEs(EntityType.RMLink);
/*  774 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, mandatoryFEs);
/*      */       
/*  776 */       long startTime = System.currentTimeMillis();
/*  777 */       Link jaceLink = null;
/*  778 */       if (Id.isId(rmLinkId))
/*      */       {
/*  780 */         Id linkId = new Id(rmLinkId);
/*  781 */         jaceLink = Factory.Link.fetchInstance(jaceObjStore, linkId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/*  785 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_RMLINK_IDENT, new Object[] { rmLinkId });
/*      */       }
/*  787 */       long stopTime = System.currentTimeMillis();
/*  788 */       Tracer.traceExtCall("Factory.Link.fetchInstance()", startTime, stopTime, Integer.valueOf(1), jaceObjStore, new Object[] { rmLinkId, jacePF });
/*      */       
/*      */ 
/*  791 */       Integer actualEntityTypeInt = jaceLink.getProperties().getInteger32Value("RMEntityType");
/*  792 */       EntityType actualRMLinkEntityType = EntityType.getInstanceFromInt(actualEntityTypeInt.intValue());
/*  793 */       boolean isPlaceholder = false;
/*  794 */       P8CE_RMLinkImpl result = new P8CE_RMLinkImpl(actualRMLinkEntityType, fpRepository, rmLinkId, jaceLink, isPlaceholder);
/*      */       
/*  796 */       Tracer.traceMethodExit(new Object[] { result });
/*  797 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  801 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  805 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { rmLinkId, EntityType.RMLink });
/*      */     }
/*      */     finally
/*      */     {
/*  809 */       if (establishedSubject) {
/*  810 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMLink getRMLink(FilePlanRepository fpRepository, String classIdent, String rmLinkId)
/*      */   {
/*  819 */     Tracer.traceMethodEntry(new Object[] { fpRepository, classIdent, rmLinkId });
/*  820 */     Util.ckNullObjParam("fpRepository", fpRepository);
/*  821 */     Util.ckInvalidStrParam("rmLinkId", rmLinkId);
/*      */     
/*  823 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  826 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  828 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/*  829 */       String actualClassIdent = classIdent;
/*  830 */       if (actualClassIdent == null) {
/*  831 */         actualClassIdent = "Relation";
/*      */       }
/*  833 */       Link jaceLink = null;
/*  834 */       if (Id.isId(rmLinkId))
/*      */       {
/*  836 */         Id linkId = new Id(rmLinkId);
/*  837 */         jaceLink = Factory.Link.getInstance(jaceObjStore, actualClassIdent, linkId);
/*      */       }
/*      */       else
/*      */       {
/*  841 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_RMLINK_IDENT, new Object[] { rmLinkId });
/*      */       }
/*      */       
/*  844 */       boolean isPlaceholder = true;
/*  845 */       P8CE_RMLinkImpl result = new P8CE_RMLinkImpl(EntityType.RMLink, fpRepository, rmLinkId, jaceLink, isPlaceholder);
/*      */       
/*  847 */       Tracer.traceMethodExit(new Object[] { result });
/*  848 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  852 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  856 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { rmLinkId, EntityType.RMLink });
/*      */     }
/*      */     finally
/*      */     {
/*  860 */       if (establishedSubject) {
/*  861 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMLink createRMLink(FilePlanRepository fpRepository, String classIdent)
/*      */   {
/*  870 */     Tracer.traceMethodEntry(new Object[] { fpRepository, classIdent });
/*  871 */     Util.ckNullObjParam("fpRepository", fpRepository);
/*  872 */     Util.ckInvalidStrParam("classIdent", classIdent);
/*      */     
/*  874 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  877 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  879 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/*  880 */       String actualClassIdent = classIdent;
/*  881 */       if (actualClassIdent == null) {
/*  882 */         actualClassIdent = "Relation";
/*      */       }
/*  884 */       Id newLinkId = Id.createId();
/*  885 */       Link jaceLink = Factory.Link.createInstance(jaceObjStore, actualClassIdent, newLinkId);
/*      */       
/*  887 */       boolean isPlaceholder = false;
/*  888 */       P8CE_RMLinkImpl result = new P8CE_RMLinkImpl(EntityType.RMLink, fpRepository, newLinkId.toString(), jaceLink, isPlaceholder);
/*      */       
/*  890 */       Tracer.traceMethodExit(new Object[] { result });
/*  891 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  895 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  899 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { "<unknown>", EntityType.RMLink });
/*      */     }
/*      */     finally
/*      */     {
/*  903 */       if (establishedSubject) {
/*  904 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReportDefinition createReportDefinition(Repository fpRepository, String idStr)
/*      */   {
/*  914 */     Tracer.traceMethodEntry(new Object[] { fpRepository, idStr });
/*  915 */     Util.ckNullObjParam("fpRepository", fpRepository);
/*      */     
/*  917 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  920 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  922 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/*  923 */       Id newId = P8CE_Util.processIdStr(idStr);
/*      */       
/*  925 */       Document jaceDoc = Factory.Document.createInstance(jaceObjStore, "RMReportDefinition", newId);
/*  926 */       P8CE_ReportDefinitionImpl result = new P8CE_ReportDefinitionImpl(fpRepository, newId.toString(), jaceDoc, true);
/*      */       
/*  928 */       Tracer.traceMethodExit(new Object[] { result });
/*  929 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  933 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  937 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { "<unknown>", EntityType.ReportDefinition });
/*      */     }
/*      */     finally
/*      */     {
/*  941 */       if (establishedSubject) {
/*  942 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMPermission createPermission()
/*      */   {
/*  951 */     Tracer.traceMethodEntry(new Object[0]);
/*  952 */     RMPermission result = new P8CE_RMPermissionImpl();
/*  953 */     Tracer.traceMethodExit(new Object[] { result });
/*  954 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMProperties createProperties()
/*      */   {
/*  962 */     Tracer.traceMethodEntry(new Object[0]);
/*  963 */     RMProperties result = new P8CE_RMPropertiesImpl();
/*  964 */     Tracer.traceMethodExit(new Object[] { result });
/*  965 */     return result;
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
/*      */   public ContentItem fetchContentItem(Repository repository, EntityType entityType, String contentIdent, RMPropertyFilter filter)
/*      */   {
/*  982 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, contentIdent, filter });
/*  983 */     Util.ckNullObjParam("repository", repository);
/*  984 */     Util.ckInvalidStrParam("contentIdent", contentIdent);
/*  985 */     EntityType actualEntityType = entityType != null ? entityType : EntityType.ContentItem;
/*      */     
/*  987 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  990 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  992 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  993 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, null);
/*      */       
/*  995 */       long startTime = System.currentTimeMillis();
/*  996 */       Document jaceDocument = null;
/*  997 */       if (Id.isId(contentIdent))
/*      */       {
/*  999 */         Id folderId = new Id(contentIdent);
/* 1000 */         jaceDocument = Factory.Document.fetchInstance(jaceObjStore, folderId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/* 1004 */         jaceDocument = Factory.Document.fetchInstance(jaceObjStore, contentIdent, jacePF);
/*      */       }
/* 1006 */       Tracer.traceExtCall("Factory.Document.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceObjStore, new Object[] { contentIdent, jacePF });
/*      */       
/*      */ 
/* 1009 */       P8CE_ContentItemImpl result = createNewContentItem(repository, actualEntityType, contentIdent, jaceDocument, false);
/*      */       
/* 1011 */       Tracer.traceMethodExit(new Object[] { result });
/* 1012 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1016 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1020 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { contentIdent, actualEntityType });
/*      */     }
/*      */     finally
/*      */     {
/* 1024 */       if (establishedSubject) {
/* 1025 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */   public ContentItem getContentItem(Repository repository, EntityType entityType, String contentIdent)
/*      */   {
/* 1043 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, contentIdent });
/* 1044 */     Util.ckNullObjParam("repository", repository);
/* 1045 */     Util.ckInvalidStrParam("contentIdent", contentIdent);
/* 1046 */     EntityType actualEntityType = entityType != null ? entityType : EntityType.ContentItem;
/*      */     
/* 1048 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1051 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1053 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*      */       
/* 1055 */       Document jaceDocument = null;
/* 1056 */       String className = P8CE_Util.getEntityClassName(actualEntityType);
/* 1057 */       if (Id.isId(contentIdent))
/*      */       {
/* 1059 */         Id contentId = new Id(contentIdent);
/* 1060 */         jaceDocument = Factory.Document.getInstance(jaceObjStore, className, contentId);
/*      */       }
/*      */       else
/*      */       {
/* 1064 */         jaceDocument = Factory.Document.getInstance(jaceObjStore, className, contentIdent);
/*      */       }
/*      */       
/* 1067 */       P8CE_ContentItemImpl result = createNewContentItem(repository, actualEntityType, contentIdent, jaceDocument, true);
/*      */       
/* 1069 */       Tracer.traceMethodExit(new Object[] { result });
/* 1070 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1074 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1078 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { contentIdent, actualEntityType });
/*      */     }
/*      */     finally
/*      */     {
/* 1082 */       if (establishedSubject) {
/* 1083 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Record fetchRecord(FilePlanRepository fpRepository, String recordIdent, RMPropertyFilter filter)
/*      */   {
/* 1093 */     Tracer.traceMethodEntry(new Object[] { fpRepository, recordIdent, filter });
/* 1094 */     Util.ckNullObjParam("repository", fpRepository);
/* 1095 */     Util.ckInvalidStrParam("recordIdent", recordIdent);
/*      */     
/* 1097 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1100 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1102 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/* 1103 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, P8CE_RecordImpl.getMandatoryJaceFEs());
/*      */       
/* 1105 */       long startTime = System.currentTimeMillis();
/* 1106 */       Document jaceDocument = null;
/* 1107 */       if (Id.isId(recordIdent))
/*      */       {
/* 1109 */         Id recordId = new Id(recordIdent);
/* 1110 */         jaceDocument = Factory.Document.fetchInstance(jaceObjStore, recordId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/* 1114 */         jaceDocument = Factory.Document.fetchInstance(jaceObjStore, recordIdent, jacePF);
/*      */       }
/* 1116 */       Tracer.traceExtCall("Factory.Document.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceObjStore, new Object[] { recordIdent, jacePF });
/*      */       
/*      */ 
/* 1119 */       Integer actualEntityTypeInt = jaceDocument.getProperties().getInteger32Value("RMEntityType");
/* 1120 */       EntityType actualRecordEntityType = EntityType.getInstanceFromInt(actualEntityTypeInt.intValue());
/* 1121 */       boolean isPlaceholder = false;
/* 1122 */       P8CE_RecordImpl result = new P8CE_RecordImpl(actualRecordEntityType, fpRepository, recordIdent, jaceDocument, isPlaceholder);
/*      */       
/* 1124 */       Tracer.traceMethodExit(new Object[] { result });
/* 1125 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1129 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1133 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { recordIdent, EntityType.ContentItem });
/*      */     }
/*      */     finally
/*      */     {
/* 1137 */       if (establishedSubject) {
/* 1138 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Record getRecord(FilePlanRepository fpRepository, EntityType recordEntityType, String recordIdent)
/*      */   {
/* 1147 */     Tracer.traceMethodEntry(new Object[] { fpRepository, recordEntityType, recordIdent });
/* 1148 */     Util.ckNullObjParam("repository", fpRepository);
/* 1149 */     Util.ckInvalidStrParam("recordIdent", recordIdent);
/* 1150 */     Util.ckNullObjParam("recordEntityType", recordEntityType);
/*      */     
/* 1152 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1155 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1157 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/*      */       
/* 1159 */       Document jaceDocument = null;
/* 1160 */       if (Id.isId(recordIdent))
/*      */       {
/* 1162 */         Id recordId = new Id(recordIdent);
/* 1163 */         jaceDocument = Factory.Document.getInstance(jaceObjStore, "RecordInfo", recordId);
/*      */       }
/*      */       else
/*      */       {
/* 1167 */         jaceDocument = Factory.Document.getInstance(jaceObjStore, "RecordInfo", recordIdent);
/*      */       }
/*      */       
/* 1170 */       boolean isPlaceholder = true;
/* 1171 */       P8CE_RecordImpl result = new P8CE_RecordImpl(recordEntityType, fpRepository, recordIdent, jaceDocument, isPlaceholder);
/*      */       
/* 1173 */       Tracer.traceMethodExit(new Object[] { result });
/* 1174 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1178 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1182 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { recordIdent, EntityType.ContentItem });
/*      */     }
/*      */     finally
/*      */     {
/* 1186 */       if (establishedSubject) {
/* 1187 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMMarkingSet fetchMarkingSet(RMDomain domain, String ident)
/*      */   {
/* 1197 */     Tracer.traceMethodEntry(new Object[] { domain, ident });
/* 1198 */     Util.ckNullObjParam("domain", domain);
/* 1199 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/* 1201 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1204 */       establishedSubject = P8CE_Util.associateSubject();
/* 1205 */       RMMarkingSet result = null;
/*      */       
/* 1207 */       Domain jaceDomain = ((P8CE_RMDomainImpl)domain).getOrFetchJaceDomain();
/* 1208 */       PropertyFilter jacePF = null;
/* 1209 */       com.filenet.api.security.MarkingSet jaceMarkingSet = null;
/* 1210 */       Id markingSetId; if (Id.isId(ident))
/*      */       {
/* 1212 */         markingSetId = new Id(ident);
/* 1213 */         long startTime = System.currentTimeMillis();
/* 1214 */         jaceMarkingSet = Factory.MarkingSet.fetchInstance(jaceDomain, markingSetId, jacePF);
/* 1215 */         Tracer.traceExtCall("Factory.MarkingSet.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceMarkingSet, new Object[] { jaceDomain, markingSetId, jacePF });
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1220 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_MARKINGSET_IDENT, new Object[] { ident });
/*      */       }
/*      */       
/* 1223 */       if (jaceMarkingSet != null) {
/* 1224 */         result = new P8CE_RMMarkingSetImpl(jaceMarkingSet);
/*      */       }
/* 1226 */       Tracer.traceMethodExit(new Object[] { result });
/* 1227 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1231 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1235 */       String domainIdent = domain.getClientIdentifier();
/* 1236 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_MARKINGSETS_FAILED, new Object[] { domainIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1240 */       if (establishedSubject) {
/* 1241 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMChoiceList fetchChoiceList(Repository repository, String ident)
/*      */   {
/* 1250 */     Tracer.traceMethodEntry(new Object[] { repository, ident });
/* 1251 */     Util.ckNullObjParam("repository", repository);
/* 1252 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/* 1254 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1257 */       establishedSubject = P8CE_Util.associateSubject();
/* 1258 */       RMChoiceList result = null;
/*      */       
/* 1260 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 1261 */       PropertyFilter jacePF = new PropertyFilter();
/* 1262 */       List<FilterElement> jaceFEs = P8CE_RMChoiceListImpl.getMandatoryJaceFEs();
/* 1263 */       for (FilterElement fe : jaceFEs)
/*      */       {
/* 1265 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*      */       
/* 1268 */       ChoiceList jaceChoiceList = null;
/* 1269 */       Id choiceListId; if (Id.isId(ident))
/*      */       {
/* 1271 */         choiceListId = new Id(ident);
/* 1272 */         long startTime = System.currentTimeMillis();
/* 1273 */         jaceChoiceList = Factory.ChoiceList.fetchInstance(jaceObjStore, choiceListId, jacePF);
/* 1274 */         Tracer.traceExtCall("Factory.ChoiceList.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceChoiceList, new Object[] { jaceObjStore, choiceListId, jacePF });
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1279 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_CHOICELIST_IDENT, new Object[] { ident });
/*      */       }
/*      */       
/* 1282 */       if (jaceChoiceList != null) {
/* 1283 */         result = new P8CE_RMChoiceListImpl(repository, jaceChoiceList);
/*      */       }
/* 1285 */       Tracer.traceMethodExit(new Object[] { result });
/* 1286 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1290 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1294 */       String repositoryIdent = repository.getClientIdentifier();
/* 1295 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CHOICELISTS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1299 */       if (establishedSubject) {
/* 1300 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMClassDescription fetchClassDescription(Repository repository, String ident, RMPropertyFilter filter)
/*      */   {
/* 1310 */     Tracer.traceMethodEntry(new Object[] { repository, ident, filter });
/* 1311 */     Util.ckNullObjParam("repository", repository);
/* 1312 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/* 1314 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1317 */       establishedSubject = P8CE_Util.associateSubject();
/* 1318 */       RMClassDescription result = null;
/*      */       
/* 1320 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 1321 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, P8CE_RMClassDescriptionImpl.getMandatoryJaceFEs());
/*      */       
/* 1323 */       ClassDescription jaceClassDesc = null;
/* 1324 */       long startTime = System.currentTimeMillis();
/* 1325 */       if (Id.isId(ident))
/*      */       {
/* 1327 */         Id classDescId = new Id(ident);
/* 1328 */         jaceClassDesc = Factory.ClassDescription.fetchInstance(jaceObjStore, classDescId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/* 1332 */         jaceClassDesc = Factory.ClassDescription.fetchInstance(jaceObjStore, ident, jacePF);
/*      */       }
/* 1334 */       Tracer.traceExtCall("Factory.ClassDescription.fetchInstance()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceClassDesc, new Object[] { jaceObjStore, ident, jacePF });
/*      */       
/*      */       String classSymName;
/* 1337 */       if (jaceClassDesc != null)
/*      */       {
/* 1339 */         result = new P8CE_RMClassDescriptionImpl(repository, jaceClassDesc);
/*      */         
/*      */ 
/*      */ 
/* 1343 */         classSymName = jaceClassDesc.get_SymbolicName();
/* 1344 */         P8CE_CacheService.getInstance().put(repository, "CD", classSymName, result);
/*      */       }
/*      */       
/* 1347 */       Tracer.traceMethodExit(new Object[] { result });
/* 1348 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1352 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1356 */       String repositoryIdent = repository.getClientIdentifier();
/* 1357 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTION_FAILED, new Object[] { repositoryIdent, ident });
/*      */     }
/*      */     finally
/*      */     {
/* 1361 */       if (establishedSubject) {
/* 1362 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ClassificationGuide fetchClassificationGuide(FilePlanRepository fpRepository, String guideIdent, RMPropertyFilter filter)
/*      */   {
/* 1373 */     Tracer.traceMethodEntry(new Object[] { fpRepository, guideIdent, filter });
/* 1374 */     Util.ckNullObjParam("fpRepository", fpRepository);
/* 1375 */     Util.ckInvalidStrParam("guideIdent", guideIdent);
/*      */     
/* 1377 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1380 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1382 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/* 1383 */       List<FilterElement> mandatoryFEs = P8CE_ClassificationGuideImpl.getMandatoryJaceFEs();
/* 1384 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, mandatoryFEs);
/*      */       
/* 1386 */       Folder jaceGuideBase = null;
/* 1387 */       String guidesContainerPath; if (Id.isId(guideIdent))
/*      */       {
/* 1389 */         Id guideId = new Id(guideIdent);
/* 1390 */         jaceGuideBase = Factory.Folder.fetchInstance(jaceObjStore, guideId, jacePF);
/*      */       }
/*      */       else
/*      */       {
/* 1394 */         StringBuilder sb = new StringBuilder();
/* 1395 */         sb.append(((P8CE_FilePlanRepositoryImpl)fpRepository).getFilePlanRootPath());
/* 1396 */         sb.append('/').append("Classification Guides");
/* 1397 */         guidesContainerPath = sb.toString();
/*      */         
/*      */ 
/* 1400 */         sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, Integer.valueOf(1), "ClassificationGuide", "g");
/* 1401 */         sb.append(" WHERE g.").append("GuideName").append(" = '").append(RALBaseEntity.escapeSQLStringValue(guideIdent)).append("' ");
/* 1402 */         sb.append(" AND g.").append("Parent").append(" = OBJECT('").append(guidesContainerPath).append("' ");
/* 1403 */         String sqlStatement = sb.toString();
/*      */         
/* 1405 */         SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1406 */         SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/* 1407 */         Integer pageSize = null;
/* 1408 */         Boolean continuable = Boolean.FALSE;
/*      */         
/* 1410 */         long startTime = System.currentTimeMillis();
/* 1411 */         IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1412 */         long endTime = System.currentTimeMillis();
/* 1413 */         Boolean elementCountIndicator = null;
/* 1414 */         if (Tracer.isMediumTraceEnabled())
/*      */         {
/* 1416 */           elementCountIndicator = jaceObjSet != null ? Boolean.valueOf(jaceObjSet.isEmpty()) : null;
/*      */         }
/* 1418 */         Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */         
/* 1420 */         if ((jaceObjSet != null) && (!jaceObjSet.isEmpty()))
/*      */         {
/* 1422 */           Iterator it = jaceObjSet.iterator();
/* 1423 */           if ((it != null) && (it.hasNext())) {
/* 1424 */             jaceGuideBase = (Folder)it.next();
/*      */           }
/*      */         }
/*      */       }
/* 1428 */       ClassificationGuide result = null;
/* 1429 */       if (jaceGuideBase != null) {
/* 1430 */         result = new P8CE_ClassificationGuideImpl(fpRepository, jaceGuideBase);
/*      */       }
/* 1432 */       Tracer.traceMethodExit(new Object[] { result });
/* 1433 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1437 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1441 */       String repositoryIdent = fpRepository.getClientIdentifier();
/* 1442 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CLASSIF_GUIDE_FAILED, new Object[] { repositoryIdent, guideIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1446 */       if (establishedSubject) {
/* 1447 */         P8CE_Util.disassociateSubject();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public BaseEntity fetchBaseEntity(Repository repository, EntityType entityType, String ident, RMPropertyFilter filter)
/*      */   {
/* 1466 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident, filter });
/* 1467 */     Util.ckNullObjParam("repository", repository);
/* 1468 */     Util.ckNullObjParam("entityType", entityType);
/* 1469 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/* 1471 */     BaseEntity result = null;
/* 1472 */     switch (entityType)
/*      */     {
/*      */ 
/*      */     case Container: 
/*      */     case FilePlan: 
/*      */     case RecordCategory: 
/*      */     case RecordFolder: 
/*      */     case RecordVolume: 
/*      */     case ElectronicRecordFolder: 
/*      */     case PhysicalContainer: 
/*      */     case HybridRecordFolder: 
/*      */     case PhysicalRecordFolder: 
/*      */     case ClassificationGuide: 
/* 1485 */       result = fetchContainer(repository, entityType, ident, filter);
/* 1486 */       break;
/*      */     
/*      */ 
/*      */     case CustomObject: 
/*      */     case DispositionAction: 
/*      */     case DispositionSchedule: 
/*      */     case DisposalPhase: 
/*      */     case DispositionTrigger: 
/*      */     case Pattern: 
/*      */     case PatternLevel: 
/*      */     case Reservation: 
/*      */     case Phase: 
/*      */     case PhaseException: 
/*      */     case SystemConfiguration: 
/*      */     case ConnectorRegistration: 
/*      */     case RecordType: 
/*      */     case Location: 
/*      */     case Hold: 
/*      */     case PatternSequence: 
/*      */     case AlternateRetention: 
/*      */     case RMLog: 
/*      */     case RMSystem: 
/* 1508 */       result = fetchCustomObject(repository, entityType, ident, filter);
/* 1509 */       break;
/*      */     
/*      */ 
/*      */     case Record: 
/*      */     case ElectronicRecord: 
/*      */     case EmailRecord: 
/*      */     case PhysicalRecord: 
/*      */     case PDFRecord: 
/* 1517 */       result = fetchRecord((FilePlanRepository)repository, ident, filter);
/* 1518 */       break;
/*      */     
/*      */ 
/*      */     case ContentItem: 
/*      */     case AuditConfig: 
/*      */     case TransferMapping: 
/*      */     case Transcript: 
/*      */     case ReportDefinition: 
/* 1526 */       result = fetchContentItem(repository, entityType, ident, filter);
/* 1527 */       break;
/*      */     
/*      */ 
/*      */     case Relation: 
/*      */     case ExtractLink: 
/*      */     case RecordCopyLink: 
/*      */     case RecordSeeAlsoLink: 
/*      */     case ReferenceLink: 
/*      */     case SupersedeLink: 
/*      */     case RecordHoldLink: 
/*      */     case RecordContainerHoldLink: 
/*      */     case RMLink: 
/*      */     case ReceiptLink: 
/* 1540 */       result = fetchRMLink((FilePlanRepository)repository, ident, filter);
/* 1541 */       break;
/*      */     
/*      */ 
/*      */     case FilePlanRepository: 
/*      */     case ContentRepository: 
/*      */     case Repository: 
/* 1547 */       result = repository;
/* 1548 */       break;
/*      */     
/*      */     case ChoiceList: 
/* 1551 */       result = fetchChoiceList(repository, ident);
/* 1552 */       break;
/*      */     
/*      */     case MarkingSet: 
/* 1555 */       RMDomain domain = repository.getDomain();
/* 1556 */       result = fetchMarkingSet(domain, ident);
/* 1557 */       break;
/*      */     
/*      */     case Domain: 
/* 1560 */       if (repository != null) {
/* 1561 */         result = repository.getDomain();
/*      */       }
/*      */       break;
/*      */     case AuditEvent: 
/*      */     default: 
/* 1566 */       result = null;
/*      */     }
/*      */     
/*      */     
/* 1570 */     Tracer.traceMethodExit(new Object[] { result });
/* 1571 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMUser fetchUser(DomainConnection domainConn, String userIdent, RMPropertyFilter filter)
/*      */   {
/* 1579 */     Tracer.traceMethodEntry(new Object[] { domainConn, userIdent, filter });
/* 1580 */     Util.ckNullObjParam("domainConn", domainConn);
/*      */     
/* 1582 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1585 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1587 */       if (userIdent == null) {
/* 1588 */         return fetchDomain(domainConn, null, filter).fetchCurrentUser();
/*      */       }
/*      */       
/*      */ 
/* 1592 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, P8CE_RMUserImpl.getMandatoryJaceFEs());
/* 1593 */       P8CE_DomainConnectionImpl connection = (P8CE_DomainConnectionImpl)domainConn;
/* 1594 */       Connection jaceConn = connection.getJaceConnection();
/*      */       
/* 1596 */       long startTime = System.currentTimeMillis();
/* 1597 */       User jaceUser = Factory.User.fetchInstance(jaceConn, userIdent, jacePF);
/* 1598 */       long stopTime = System.currentTimeMillis();
/* 1599 */       Tracer.traceExtCall("Factory.User.fetchInstance()", startTime, stopTime, Integer.valueOf(1), jaceConn, new Object[] { userIdent, jacePF });
/*      */       
/* 1601 */       RMUser result = new P8CE_RMUserImpl(jaceUser);
/*      */       
/* 1603 */       Tracer.traceMethodExit(new Object[] { result });
/* 1604 */       return result;
/*      */ 
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1609 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1613 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { userIdent, EntityType.SecurityPrincipal });
/*      */     }
/*      */     finally
/*      */     {
/* 1617 */       if (establishedSubject) {
/* 1618 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RMGroup fetchGroup(DomainConnection domainConn, String groupIdent, RMPropertyFilter filter)
/*      */   {
/* 1627 */     Tracer.traceMethodEntry(new Object[] { domainConn, groupIdent, filter });
/* 1628 */     Util.ckNullObjParam("domainConn", domainConn);
/*      */     
/* 1630 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1633 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1635 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, P8CE_RMUserImpl.getMandatoryJaceFEs());
/* 1636 */       P8CE_DomainConnectionImpl connection = (P8CE_DomainConnectionImpl)domainConn;
/* 1637 */       Connection jaceConn = connection.getJaceConnection();
/*      */       
/* 1639 */       long startTime = System.currentTimeMillis();
/* 1640 */       Group jaceGroup = Factory.Group.fetchInstance(jaceConn, groupIdent, jacePF);
/* 1641 */       long stopTime = System.currentTimeMillis();
/* 1642 */       Tracer.traceExtCall("Factory.Group.fetchInstance()", startTime, stopTime, Integer.valueOf(1), jaceConn, new Object[] { groupIdent, jacePF });
/*      */       
/*      */ 
/* 1645 */       RMGroup result = new P8CE_RMGroupImpl(jaceGroup);
/*      */       
/* 1647 */       Tracer.traceMethodExit(new Object[] { result });
/* 1648 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1652 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1656 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { groupIdent, EntityType.SecurityPrincipal });
/*      */     }
/*      */     finally
/*      */     {
/* 1660 */       if (establishedSubject) {
/* 1661 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public BaseEntity fetchBaseEntity(Repository repository, String classIdent, String ident, RMPropertyFilter filter)
/*      */   {
/* 1670 */     Tracer.traceMethodEntry(new Object[] { repository, classIdent, ident, filter });
/* 1671 */     Util.ckNullObjParam("repository", repository);
/* 1672 */     Util.ckNullObjParam("classIdent", classIdent);
/* 1673 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/* 1675 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1678 */       establishedSubject = P8CE_Util.associateSubject();
/* 1679 */       BaseEntity result = null;
/*      */       
/*      */ 
/*      */ 
/* 1683 */       EntityType potentialEntityType = P8CE_Util.getEntityType(classIdent);
/* 1684 */       if ((potentialEntityType != null) && (potentialEntityType != EntityType.Unknown))
/*      */       {
/* 1686 */         result = fetchBaseEntity(repository, potentialEntityType, ident, filter);
/*      */       }
/*      */       ObjectStore jaceObjStore;
/* 1689 */       if (result == null)
/*      */       {
/* 1691 */         jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 1692 */         PropertyFilter jacePF = P8CE_Util.convertToJacePF(filter, null);
/*      */         
/* 1694 */         long startTime = System.currentTimeMillis();
/* 1695 */         IndependentObject jaceBaseObj = null;
/* 1696 */         if (Id.isId(ident))
/*      */         {
/* 1698 */           Id objId = new Id(ident);
/* 1699 */           jaceBaseObj = jaceObjStore.fetchObject(classIdent, objId, jacePF);
/*      */         }
/*      */         else
/*      */         {
/* 1703 */           jaceBaseObj = jaceObjStore.fetchObject(classIdent, ident, jacePF);
/*      */         }
/* 1705 */         long stopTime = System.currentTimeMillis();
/* 1706 */         Tracer.traceExtCall("ObjectStore.fetchObject()", startTime, stopTime, Integer.valueOf(1), jaceObjStore, new Object[] { ident, jacePF });
/*      */         
/*      */ 
/* 1709 */         if (jaceBaseObj != null)
/*      */         {
/* 1711 */           result = (BaseEntity)P8CE_Util.convertJaceObjToJarmObject(repository, jaceBaseObj);
/*      */         }
/*      */       }
/*      */       
/* 1715 */       Tracer.traceMethodExit(new Object[] { result });
/* 1716 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1720 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1724 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, EntityType.Unknown });
/*      */     }
/*      */     finally
/*      */     {
/* 1728 */       if (establishedSubject) {
/* 1729 */         P8CE_Util.disassociateSubject();
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
/*      */   public BaseEntity getBaseEntity(Repository repository, EntityType entityType, String ident)
/*      */   {
/* 1744 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident });
/* 1745 */     Util.ckNullObjParam("repository", repository);
/* 1746 */     Util.ckNullObjParam("entityType", entityType);
/* 1747 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/* 1749 */     BaseEntity result = null;
/* 1750 */     switch (entityType)
/*      */     {
/*      */ 
/*      */     case Container: 
/*      */     case FilePlan: 
/*      */     case RecordCategory: 
/*      */     case RecordFolder: 
/*      */     case RecordVolume: 
/*      */     case ElectronicRecordFolder: 
/*      */     case PhysicalContainer: 
/*      */     case HybridRecordFolder: 
/*      */     case PhysicalRecordFolder: 
/*      */     case ClassificationGuide: 
/*      */     case ClassificationGuideSection: 
/* 1764 */       result = getContainer(repository, entityType, ident);
/* 1765 */       break;
/*      */     
/*      */ 
/*      */     case CustomObject: 
/*      */     case DispositionAction: 
/*      */     case DispositionSchedule: 
/*      */     case DisposalPhase: 
/*      */     case DispositionTrigger: 
/*      */     case Pattern: 
/*      */     case PatternLevel: 
/*      */     case Reservation: 
/*      */     case Phase: 
/*      */     case PhaseException: 
/*      */     case SystemConfiguration: 
/*      */     case ConnectorRegistration: 
/*      */     case RecordType: 
/*      */     case Location: 
/*      */     case Hold: 
/*      */     case PatternSequence: 
/*      */     case AlternateRetention: 
/*      */     case RMLog: 
/*      */     case RMSystem: 
/* 1787 */       result = getCustomObject(repository, entityType, ident);
/* 1788 */       break;
/*      */     
/*      */ 
/*      */     case Record: 
/*      */     case ElectronicRecord: 
/*      */     case EmailRecord: 
/*      */     case PhysicalRecord: 
/*      */     case PDFRecord: 
/* 1796 */       result = getRecord((FilePlanRepository)repository, entityType, ident);
/* 1797 */       break;
/*      */     
/*      */ 
/*      */     case ContentItem: 
/*      */     case AuditConfig: 
/*      */     case TransferMapping: 
/*      */     case Transcript: 
/*      */     case ReportDefinition: 
/*      */     case ClassificationGuideTopic: 
/* 1806 */       result = getContentItem(repository, entityType, ident);
/* 1807 */       break;
/*      */     
/*      */ 
/*      */     case Relation: 
/*      */     case ExtractLink: 
/*      */     case RecordCopyLink: 
/*      */     case RecordSeeAlsoLink: 
/*      */     case ReferenceLink: 
/*      */     case SupersedeLink: 
/*      */     case RecordHoldLink: 
/*      */     case RecordContainerHoldLink: 
/*      */     case RMLink: 
/*      */     case ReceiptLink: 
/* 1820 */       result = getRMLink((FilePlanRepository)repository, "Relation", ident);
/* 1821 */       break;
/*      */     case FilePlanRepository: 
/*      */     case ContentRepository: 
/*      */     case Repository: 
/*      */     case ChoiceList: 
/*      */     case MarkingSet: 
/*      */     case Domain: 
/*      */     case AuditEvent: 
/*      */     default: 
/* 1830 */       result = null;
/*      */     }
/*      */     
/*      */     
/* 1834 */     Tracer.traceMethodExit(new Object[] { result });
/* 1835 */     return result;
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
/*      */   public BaseEntity getBaseEntity(Repository repository, String classIdent, String ident)
/*      */   {
/* 1851 */     Tracer.traceMethodEntry(new Object[] { repository, classIdent, ident });
/* 1852 */     Util.ckNullObjParam("repository", repository);
/* 1853 */     Util.ckNullObjParam("classIdent", classIdent);
/* 1854 */     Util.ckInvalidStrParam("ident", ident);
/*      */     
/* 1856 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1859 */       establishedSubject = P8CE_Util.associateSubject();
/* 1860 */       BaseEntity result = null;
/*      */       
/*      */ 
/*      */ 
/* 1864 */       EntityType potentialEntityType = P8CE_Util.getEntityType(classIdent);
/* 1865 */       if (potentialEntityType != null)
/*      */       {
/* 1867 */         result = getBaseEntity(repository, potentialEntityType, ident);
/*      */       }
/*      */       ObjectStore jaceObjStore;
/* 1870 */       if (result == null)
/*      */       {
/* 1872 */         jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 1873 */         IndependentObject jaceBaseObj = null;
/* 1874 */         if (Id.isId(ident))
/*      */         {
/* 1876 */           Id objId = new Id(ident);
/* 1877 */           jaceBaseObj = jaceObjStore.getObject(classIdent, objId);
/*      */         }
/*      */         else
/*      */         {
/* 1881 */           jaceBaseObj = jaceObjStore.getObject(classIdent, ident);
/*      */         }
/*      */         
/* 1884 */         if (jaceBaseObj != null)
/*      */         {
/* 1886 */           boolean isPlaceholder = true;
/* 1887 */           result = new P8CE_BaseEntityImpl(repository, ident, classIdent, EntityType.Unknown, jaceBaseObj, isPlaceholder);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1892 */       Tracer.traceMethodExit(new Object[] { result });
/* 1893 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1897 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1901 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { ident, EntityType.Unknown });
/*      */     }
/*      */     finally
/*      */     {
/* 1905 */       if (establishedSubject) {
/* 1906 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public RMContentSearchDefinition createRMSearchDefinition()
/*      */   {
/* 1913 */     Tracer.traceMethodEntry(new Object[0]);
/* 1914 */     RMContentSearchDefinition result = new P8CE_RMContentSearchDefinition();
/* 1915 */     Tracer.traceMethodExit(new Object[] { result });
/* 1916 */     return result;
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
/*      */   private Container createNewContainer(Repository repository, EntityType entityType, String ident, Folder jaceFolder, boolean isPlaceholder)
/*      */   {
/* 1936 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident, jaceFolder, Boolean.valueOf(isPlaceholder) });
/*      */     
/* 1938 */     Container result = null;
/* 1939 */     switch (entityType)
/*      */     {
/*      */     case Container: 
/* 1942 */       result = new P8CE_BaseContainerImpl(entityType, repository, ident, jaceFolder, isPlaceholder);
/* 1943 */       break;
/*      */     
/*      */     case FilePlan: 
/* 1946 */       if (P8CE_Util.isAnIERRootContainer(jaceFolder))
/*      */       {
/* 1948 */         result = new P8CE_BaseContainerImpl(entityType, repository, ident, jaceFolder, isPlaceholder);
/*      */       }
/*      */       else
/*      */       {
/* 1952 */         result = new P8CE_FilePlanImpl(repository, ident, jaceFolder, isPlaceholder);
/*      */       }
/* 1954 */       break;
/*      */     
/*      */     case RecordCategory: 
/* 1957 */       result = new P8CE_RecordCategoryImpl(repository, ident, jaceFolder, isPlaceholder);
/* 1958 */       break;
/*      */     
/*      */     case RecordFolder: 
/*      */     case ElectronicRecordFolder: 
/*      */     case PhysicalContainer: 
/*      */     case HybridRecordFolder: 
/*      */     case PhysicalRecordFolder: 
/* 1965 */       result = new P8CE_RecordFolderImpl(repository, ident, jaceFolder, isPlaceholder);
/* 1966 */       break;
/*      */     
/*      */     case RecordVolume: 
/* 1969 */       result = new P8CE_RecordVolumeImpl(repository, ident, jaceFolder, isPlaceholder);
/* 1970 */       break;
/*      */     
/*      */ 
/*      */     default: 
/* 1974 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_CONTAINER_ENTITYTYPE, new Object[] { entityType });
/*      */     }
/*      */     
/* 1977 */     Tracer.traceMethodExit(new Object[] { result });
/* 1978 */     return result;
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
/*      */   private RMCustomObject createNewCustomObject(Repository repository, EntityType entityType, String ident, CustomObject jaceCustomObj, boolean isPlaceholder)
/*      */   {
/* 1998 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident, jaceCustomObj, Boolean.valueOf(isPlaceholder) });
/*      */     
/* 2000 */     RMCustomObject result = null;
/* 2001 */     switch (entityType)
/*      */     {
/*      */     case Location: 
/* 2004 */       result = new P8CE_LocationImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2005 */       break;
/*      */     
/*      */     case SystemConfiguration: 
/* 2008 */       result = new P8CE_SystemConfigurationImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2009 */       break;
/*      */     
/*      */     case RecordType: 
/* 2012 */       result = new P8CE_RecordTypeImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2013 */       break;
/*      */     
/*      */     case DispositionAction: 
/* 2016 */       result = new P8CE_DispositionActionImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2017 */       break;
/*      */     
/*      */     case DispositionSchedule: 
/* 2020 */       result = new P8CE_DispositionScheduleImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2021 */       break;
/*      */     
/*      */     case Phase: 
/* 2024 */       result = new P8CE_DispositionPhaseImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2025 */       break;
/*      */     
/*      */     case DispositionTrigger: 
/* 2028 */       result = new P8CE_DispositionTriggerImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2029 */       break;
/*      */     
/*      */     case AlternateRetention: 
/* 2032 */       result = new P8CE_AlternateRetentionImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2033 */       break;
/*      */     
/*      */     case Hold: 
/* 2036 */       result = new P8CE_HoldImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2037 */       break;
/*      */     
/*      */     case Pattern: 
/* 2040 */       result = new P8CE_NamingPatternImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2041 */       break;
/*      */     
/*      */     case PatternSequence: 
/* 2044 */       result = new P8CE_NamingPatternSequenceImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2045 */       break;
/*      */     
/*      */     case PatternLevel: 
/* 2048 */       result = new P8CE_NamingPatternLevelImpl(repository, ident, jaceCustomObj, isPlaceholder);
/* 2049 */       break;
/*      */     case DisposalPhase: case Reservation: 
/*      */     case PhaseException: 
/*      */     case ConnectorRegistration: 
/*      */     case RMLog: 
/*      */     case RMSystem: 
/*      */     default: 
/* 2056 */       result = new P8CE_RMCustomObjectImpl(entityType, repository, ident, jaceCustomObj, isPlaceholder);
/*      */     }
/*      */     
/*      */     
/* 2060 */     Tracer.traceMethodExit(new Object[] { result });
/* 2061 */     return result;
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
/*      */   private P8CE_ContentItemImpl createNewContentItem(Repository repository, EntityType entityType, String ident, Document jaceDocument, boolean isPlaceholder)
/*      */   {
/* 2081 */     Tracer.traceMethodEntry(new Object[] { repository, entityType, ident, jaceDocument, Boolean.valueOf(isPlaceholder) });
/* 2082 */     P8CE_ContentItemImpl result = null;
/*      */     
/* 2084 */     switch (entityType)
/*      */     {
/*      */     case ContentItem: 
/*      */     case AuditConfig: 
/*      */     case TransferMapping: 
/*      */     case Transcript: 
/* 2090 */       result = new P8CE_ContentItemImpl(EntityType.ContentItem, repository, ident, jaceDocument, isPlaceholder);
/* 2091 */       break;
/*      */     
/*      */     case WorkflowDefinition: 
/* 2094 */       result = new P8CE_WorkflowDefinitionImpl(repository, ident, jaceDocument, isPlaceholder);
/* 2095 */       break;
/*      */     
/*      */     case ReportDefinition: 
/* 2098 */       result = new P8CE_ReportDefinitionImpl(repository, ident, jaceDocument, isPlaceholder);
/* 2099 */       break;
/*      */     
/*      */     default: 
/* 2102 */       result = null;
/*      */     }
/*      */     
/*      */     
/* 2106 */     Tracer.traceMethodExit(new Object[] { result });
/* 2107 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Hold getHold(FilePlanRepository fpRepository, String holdIdent)
/*      */   {
/* 2117 */     Tracer.traceMethodEntry(new Object[] { fpRepository, holdIdent });
/* 2118 */     Util.ckNullObjParam("fpRepository", fpRepository);
/* 2119 */     Util.ckInvalidStrParam("holdIdent", holdIdent);
/*      */     
/* 2121 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 2124 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 2126 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)fpRepository).getJaceObjectStore();
/*      */       
/* 2128 */       long startTime = System.currentTimeMillis();
/* 2129 */       CustomObject jaceCustomObj = null;
/* 2130 */       if (Id.isId(holdIdent))
/*      */       {
/* 2132 */         Id holdId = new Id(holdIdent);
/* 2133 */         jaceCustomObj = Factory.CustomObject.getInstance(jaceObjStore, "RecordHold", holdId);
/*      */       }
/*      */       else
/*      */       {
/* 2137 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BAD_HOLD_IDENT, new Object[] { holdIdent });
/*      */       }
/* 2139 */       long stopTime = System.currentTimeMillis();
/* 2140 */       Tracer.traceExtCall("Factory.Link.getInstance()", startTime, stopTime, Integer.valueOf(1), jaceObjStore, new Object[] { holdIdent });
/*      */       
/*      */ 
/* 2143 */       boolean isPlaceholder = true;
/* 2144 */       P8CE_HoldImpl result = new P8CE_HoldImpl(fpRepository, holdIdent, jaceCustomObj, isPlaceholder);
/*      */       
/* 2146 */       Tracer.traceMethodExit(new Object[] { result });
/* 2147 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 2151 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 2155 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { holdIdent, EntityType.Hold });
/*      */     }
/*      */     finally
/*      */     {
/* 2159 */       if (establishedSubject) {
/* 2160 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RALBulkOperation getRALBulkOperation()
/*      */   {
/* 2169 */     return P8CE_RALBulkOperationImpl.getSingleton();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2178 */     return "P8CE_RALService";
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RALServiceImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */