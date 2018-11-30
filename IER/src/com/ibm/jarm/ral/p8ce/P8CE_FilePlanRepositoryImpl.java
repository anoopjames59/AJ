/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.admin.PropertyTemplate;
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.ClassDefinitionSet;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.PageIterator;
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.constants.AutoUniqueName;
/*      */ import com.filenet.api.constants.DefineSecurityParentage;
/*      */ import com.filenet.api.constants.RefreshMode;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.Factory.CustomObject;
/*      */ import com.filenet.api.core.Factory.Document;
/*      */ import com.filenet.api.core.Factory.Folder;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.exception.EngineRuntimeException;
/*      */ import com.filenet.api.exception.ExceptionCode;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.jarm.api.constants.DataModelType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.ClassificationGuide;
/*      */ import com.ibm.jarm.api.core.ContentRepository;
/*      */ import com.ibm.jarm.api.core.DispositionAction;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.DispositionTrigger;
/*      */ import com.ibm.jarm.api.core.FilePlan;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.Hold;
/*      */ import com.ibm.jarm.api.core.Location;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.RMFactory.ContentRepository;
/*      */ import com.ibm.jarm.api.core.RMWorkflowDefinition;
/*      */ import com.ibm.jarm.api.core.RecordType;
/*      */ import com.ibm.jarm.api.core.ReportDefinition;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.core.SystemConfiguration;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*      */ import com.ibm.jarm.ral.common.RALWorkflowSupport;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class P8CE_FilePlanRepositoryImpl
/*      */   extends P8CE_ContentRepositoryImpl
/*      */   implements FilePlanRepository
/*      */ {
/*   77 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*   78 */   private static final IGenerator<FilePlanRepository> FilePlanRepositoryGenerator = new Generator();
/*      */   
/*      */ 
/*   81 */   private static final Map<String, Boolean> ValidFilePlanRepositoryCache = Collections.synchronizedMap(new HashMap());
/*      */   
/*      */   private static final String CacheKey_SupportsReceipts = "SupportsReceipts";
/*      */   
/*      */   private static final String CacheKey_SupportsExternallyManagement = "SupportsExternalManagement";
/*      */   
/*      */   private static final String CacheKey_SupportsDefensibleDisposal = "SupportsDefensibleDisposal";
/*      */   
/*      */   private static final String SQL_SYSCONFIG_FPOS_SETUP;
/*      */   private static final String SQL_FILEPLAN_ROOT_PATH;
/*      */   private static final String SQL_CONNECTOR_REGISTRATIONS;
/*      */   private static final PropertyFilter JACE_CONNECTOR_REGISTRATION_PF;
/*      */   private static final String CONNECTOR_REG_DEFAULT_IMPLCLASS = "com.filenet.rm.api.impl.RMConnectorImpl";
/*      */   private static final String CONNECTOR_REG_DEFAULT_REPOSTYPE = "CE";
/*      */   private static final String CONNECTOR_REG_DEFAULT_SERVERNAME = "*";
/*      */   private Map<String, SystemConfiguration> systemConfigurations;
/*      */   private DataModelType dataModel;
/*      */   private String filePlanRootPath;
/*      */   
/*      */   static
/*      */   {
/*  102 */     StringBuilder sb = new StringBuilder();
/*  103 */     sb.append("SELECT ");
/*  104 */     sb.append('[').append("Id").append(']');
/*  105 */     sb.append(" FROM [").append("SystemConfiguration").append(']');
/*  106 */     sb.append(" WHERE [").append("PropertyName").append("] = '").append("FPOS Setup").append("' ");
/*  107 */     SQL_SYSCONFIG_FPOS_SETUP = sb.toString();
/*      */     
/*  109 */     sb = new StringBuilder();
/*  110 */     sb.append("SELECT TOP 1 ");
/*  111 */     sb.append('[').append("PathName").append(']');
/*  112 */     sb.append(", [").append("DateCreated").append(']');
/*  113 */     sb.append(" FROM [").append("ClassificationSchemes").append("] ");
/*  114 */     sb.append(" ORDER BY [").append("DateCreated").append("] ");
/*  115 */     SQL_FILEPLAN_ROOT_PATH = sb.toString();
/*      */     
/*      */ 
/*  118 */     String[] connRegPropNames = { "RepositoryName", "Id", "RepositoryType", "ServerName", "Inactive", "RMEntityType", "ImplementingClassName" };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  125 */     sb = new StringBuilder();
/*  126 */     sb.append("SELECT");
/*  127 */     for (int i = 0; i < connRegPropNames.length; i++)
/*      */     {
/*  129 */       if (i > 0)
/*  130 */         sb.append(',');
/*  131 */       sb.append(' ').append(connRegPropNames[i]);
/*      */     }
/*  133 */     sb.append(" FROM [").append("ConnectorRegistration").append("] ");
/*  134 */     SQL_CONNECTOR_REGISTRATIONS = sb.toString();
/*  135 */     String connRegPropNamesStr = P8CE_Util.createSpaceSeparatedString(connRegPropNames);
/*  136 */     JACE_CONNECTOR_REGISTRATION_PF = new PropertyFilter();
/*  137 */     JACE_CONNECTOR_REGISTRATION_PF.addIncludeProperty(0, null, null, connRegPropNamesStr, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static String[] getMandatoryPropertyNames()
/*      */   {
/*  146 */     return P8CE_ContentRepositoryImpl.getMandatoryPropertyNames();
/*      */   }
/*      */   
/*      */   static List<FilterElement> getMandatoryJaceFEs()
/*      */   {
/*  151 */     return P8CE_ContentRepositoryImpl.getMandatoryJaceFEs();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static IGenerator<FilePlanRepository> getFilePlanRepositoryGenerator()
/*      */   {
/*  161 */     return FilePlanRepositoryGenerator;
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
/*      */   public P8CE_FilePlanRepositoryImpl(RMDomain domain, String identity, ObjectStore jaceObjectStore, boolean performValidation, boolean isPlaceholder)
/*      */   {
/*  176 */     super(domain, identity, jaceObjectStore, false, isPlaceholder);
/*  177 */     Tracer.traceMethodEntry(new Object[] { domain, identity, jaceObjectStore, Boolean.valueOf(performValidation) });
/*      */     
/*  179 */     if ((performValidation) && (!isValidFPOS(jaceObjectStore)))
/*      */     {
/*  181 */       String osIdent = P8CE_Util.getJaceObjectIdentity(jaceObjectStore);
/*  182 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INVALID_FILEPLAN_OBJSTORE, new Object[] { osIdent });
/*      */     }
/*      */     
/*  185 */     this.entityType = EntityType.FilePlanRepository;
/*      */     
/*  187 */     if (!isPlaceholder) {
/*  188 */       getSystemConfigurations();
/*      */     }
/*  190 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DataModelType getDataModelType()
/*      */   {
/*  198 */     Tracer.traceMethodEntry(new Object[0]);
/*  199 */     if (this.dataModel == null)
/*      */     {
/*  201 */       Map<String, SystemConfiguration> sysConfigs = getSystemConfigurations();
/*  202 */       SystemConfiguration fposSetupSysConfig = (SystemConfiguration)sysConfigs.get("FPOS Setup");
/*  203 */       if (fposSetupSysConfig == null)
/*      */       {
/*  205 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_SYSCONFIG_FPOS_SETUP, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/*  208 */       String fposSetupValue = fposSetupSysConfig.getPropertyValue();
/*  209 */       if ((fposSetupValue == null) || (fposSetupValue.trim().length() == 0))
/*      */       {
/*  211 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INVALID_FPOS_SETUP_VALUE, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       
/*  214 */       this.dataModel = DataModelType.getInstanceFromString(fposSetupValue);
/*      */     }
/*      */     
/*  217 */     Tracer.traceMethodExit(new Object[] { this.dataModel });
/*  218 */     return this.dataModel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, SystemConfiguration> getSystemConfigurations()
/*      */   {
/*  227 */     Tracer.traceMethodEntry(new Object[0]);
/*  228 */     if (this.systemConfigurations == null)
/*      */     {
/*  230 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/*  233 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/*  235 */         Map<String, SystemConfiguration> tmpMap = P8CE_SystemConfigurationImpl.loadSystemConfigurations(this);
/*  236 */         if (tmpMap != null) {
/*  237 */           this.systemConfigurations = Collections.unmodifiableMap(tmpMap);
/*      */         } else {
/*  239 */           this.systemConfigurations = Collections.EMPTY_MAP;
/*      */         }
/*      */       }
/*      */       finally {
/*  243 */         if (establishedSubject) {
/*  244 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/*  248 */     Tracer.traceMethodExit(new Object[] { this.systemConfigurations });
/*  249 */     return this.systemConfigurations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void flushSystemConfigurationCache()
/*      */   {
/*  259 */     this.systemConfigurations = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public SystemConfiguration putSystemConfiguration(String propertyName, String propertyValue)
/*      */   {
/*  267 */     Tracer.traceMethodEntry(new Object[] { propertyName, propertyValue });
/*  268 */     Util.ckInvalidStrParam("propertyName", propertyName);
/*  269 */     Util.ckInvalidStrParam("propertyValue", propertyValue);
/*      */     
/*  271 */     if (!P8CE_SystemConfigurationImpl.canBeUpdated(propertyName))
/*      */     {
/*  273 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANNOT_UPDATE_SYSTEMCONFIGURATION_INSTANCE, new Object[] { propertyName });
/*      */     }
/*      */     
/*  276 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  279 */       establishedSubject = P8CE_Util.associateSubject();
/*  280 */       Map<String, SystemConfiguration> sysConfigs = P8CE_SystemConfigurationImpl.loadSystemConfigurations(this);
/*      */       
/*      */ 
/*  283 */       P8CE_RMDomainImpl jarmDomain = (P8CE_RMDomainImpl)getDomain();
/*  284 */       Domain jaceDomain = jarmDomain.getOrFetchJaceDomain();
/*  285 */       UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.NO_REFRESH);
/*      */       
/*  287 */       CustomObject jaceBase = null;
/*  288 */       SystemConfiguration sysConfig = (SystemConfiguration)sysConfigs.get(propertyName);
/*  289 */       if (sysConfig != null)
/*      */       {
/*  291 */         jaceBase = (CustomObject)((P8CE_SystemConfigurationImpl)sysConfig).getJaceBaseObject(); }
/*      */       ReferentialContainmentRelationship rcr;
/*  293 */       if (jaceBase == null)
/*      */       {
/*  295 */         jaceBase = Factory.CustomObject.createInstance(getJaceObjectStore(), "SystemConfiguration");
/*  296 */         jaceBase.getProperties().putValue("PropertyName", propertyName);
/*  297 */         jaceBase.getProperties().putValue("PropertyValue", propertyValue);
/*  298 */         jaceUB.add(jaceBase, P8CE_Util.CEPF_Empty);
/*      */         
/*  300 */         Folder sysConfigsFolder = Factory.Folder.getInstance(getJaceObjectStore(), "Folder", "/Records Management/RMMaster/SystemConfiguration");
/*      */         
/*  302 */         rcr = sysConfigsFolder.file(jaceBase, AutoUniqueName.NOT_AUTO_UNIQUE, propertyName, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
/*      */         
/*  304 */         jaceUB.add(rcr, P8CE_Util.CEPF_Empty);
/*      */       }
/*      */       else
/*      */       {
/*  308 */         jaceBase.getProperties().putValue("PropertyValue", propertyValue);
/*  309 */         jaceUB.add(jaceBase, P8CE_Util.CEPF_Empty);
/*      */       }
/*      */       
/*  312 */       jaceUB.updateBatch();
/*      */       
/*      */ 
/*  315 */       flushSystemConfigurationCache();
/*      */       
/*  317 */       sysConfigs = getSystemConfigurations();
/*      */       
/*  319 */       SystemConfiguration result = (SystemConfiguration)sysConfigs.get(propertyName);
/*  320 */       Tracer.traceMethodExit(new Object[] { result });
/*  321 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/*  325 */       if (establishedSubject) {
/*  326 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<ContentRepository> getAssociatedContentRepositories()
/*      */   {
/*  335 */     Tracer.traceMethodEntry(new Object[0]);
/*  336 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  339 */       establishedSubject = P8CE_Util.associateSubject();
/*  340 */       resolve();
/*      */       
/*  342 */       List<CustomObject> jaceConnRegs = getAvailableConnectorRegistrations();
/*  343 */       List<ContentRepository> results = new ArrayList(jaceConnRegs.size());
/*  344 */       Properties jaceProps = null;
/*  345 */       for (CustomObject jaceConnReg : jaceConnRegs)
/*      */       {
/*  347 */         jaceProps = jaceConnReg.getProperties();
/*      */         
/*  349 */         if (("CE".equalsIgnoreCase(jaceProps.getStringValue("RepositoryType"))) && ("com.filenet.rm.api.impl.RMConnectorImpl".equalsIgnoreCase(jaceProps.getStringValue("ImplementingClassName"))) && ("*".equalsIgnoreCase(jaceProps.getStringValue("ServerName"))) && (EntityType.ConnectorRegistration.getIntValue() == jaceProps.getInteger32Value("RMEntityType").intValue()))
/*      */         {
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
/*  364 */           String domainIdent = null;
/*  365 */           String objStoreIdent = null;
/*  366 */           String rnValue = jaceProps.getStringValue("RepositoryName");
/*  367 */           if (rnValue != null)
/*      */           {
/*  369 */             rnValue = rnValue.trim();
/*  370 */             String remainder = null;
/*      */             
/*      */ 
/*  373 */             if ((rnValue.length() >= 38) && (Id.isId(rnValue.substring(0, 38))))
/*      */             {
/*  375 */               domainIdent = rnValue.substring(0, 38);
/*  376 */               remainder = rnValue.substring(38);
/*      */             }
/*      */             else
/*      */             {
/*  380 */               int dotPos = rnValue.indexOf('.');
/*  381 */               if (dotPos == -1) {
/*      */                 continue;
/*      */               }
/*  384 */               domainIdent = rnValue.substring(0, dotPos);
/*  385 */               remainder = rnValue.substring(dotPos);
/*      */             }
/*  387 */             if ((domainIdent != null) && (domainIdent.length() != 0) && 
/*      */             
/*      */ 
/*      */ 
/*  391 */               ('.' == remainder.charAt(0)) && (remainder.length() > 1))
/*      */             {
/*      */ 
/*  394 */               objStoreIdent = remainder.substring(1);
/*  395 */               ContentRepository contentRepos = null;
/*      */               
/*      */ 
/*      */               try
/*      */               {
/*  400 */                 contentRepos = RMFactory.ContentRepository.fetchInstance(getDomain(), objStoreIdent, RMPropertyFilter.MinimumPropertySet);
/*      */               }
/*      */               catch (Exception ignored) {}
/*      */               
/*  404 */               continue;
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*  409 */               boolean isADupllicate = false;
/*  410 */               String newCRIdent = contentRepos.getObjectIdentity();
/*  411 */               for (ContentRepository existCR : results)
/*      */               {
/*  413 */                 if (newCRIdent.equalsIgnoreCase(existCR.getObjectIdentity()))
/*      */                 {
/*  415 */                   isADupllicate = true;
/*  416 */                   break;
/*      */                 }
/*      */               }
/*      */               
/*  420 */               if (!isADupllicate)
/*  421 */                 results.add(contentRepos);
/*      */             }
/*      */           }
/*      */         } }
/*  425 */       Tracer.traceMethodExit(new Object[] { results });
/*  426 */       return results;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  430 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  434 */       String repositoryIdent = getSymbolicName();
/*  435 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CONNECTOR_REGS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  439 */       if (establishedSubject) {
/*  440 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private List<CustomObject> getAvailableConnectorRegistrations() {
/*  446 */     Tracer.traceMethodEntry(new Object[0]);
/*  447 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  450 */       establishedSubject = P8CE_Util.associateSubject();
/*  451 */       List<CustomObject> results = new ArrayList(2);
/*      */       
/*      */ 
/*  454 */       SearchSQL jaceSearchSQL = new SearchSQL(SQL_CONNECTOR_REGISTRATIONS);
/*  455 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*  456 */       Integer pageSize = null;
/*  457 */       Boolean continuable = Boolean.TRUE;
/*      */       
/*  459 */       long startTime = System.currentTimeMillis();
/*  460 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, JACE_CONNECTOR_REGISTRATION_PF, continuable);
/*  461 */       long endTime = System.currentTimeMillis();
/*  462 */       Boolean elementCountIndicator = null;
/*  463 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  465 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/*  467 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, jaceObjSet, new Object[] { SQL_CONNECTOR_REGISTRATIONS, null, JACE_CONNECTOR_REGISTRATION_PF, continuable });
/*      */       
/*  469 */       CustomObject jaceCustomObj = null;
/*  470 */       PageIterator jacePI = jaceObjSet.pageIterator();
/*  471 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/*  473 */         currentPage = jacePI.getCurrentPage();
/*  474 */         for (Object obj : currentPage)
/*      */         {
/*  476 */           jaceCustomObj = (CustomObject)obj;
/*  477 */           results.add(jaceCustomObj);
/*      */         }
/*      */       }
/*      */       
/*  481 */       Tracer.traceMethodExit(new Object[] { results });
/*  482 */       return results;
/*      */     }
/*      */     finally
/*      */     {
/*  486 */       if (establishedSubject) {
/*  487 */         P8CE_Util.disassociateSubject();
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
/*      */   public CustomObject updateConnectorRegistrations(Id contentReposId)
/*      */   {
/*  505 */     Tracer.traceMethodEntry(new Object[] { contentReposId });
/*  506 */     CustomObject newConnReg = null;
/*      */     
/*      */ 
/*      */ 
/*  510 */     P8CE_CacheService cache = P8CE_CacheService.getInstance();
/*  511 */     String curCRIdent = contentReposId.toString();
/*      */     
/*  513 */     if (!cache.containsKey(this, "CR", curCRIdent))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  520 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/*  523 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/*  525 */         List<ContentRepository> associatedCRs = getAssociatedContentRepositories();
/*  526 */         boolean foundMatch = false;
/*  527 */         for (ContentRepository crRepos : associatedCRs)
/*      */         {
/*  529 */           if ((curCRIdent.equalsIgnoreCase(crRepos.getObjectIdentity())) || (curCRIdent.equalsIgnoreCase(crRepos.getSymbolicName())))
/*      */           {
/*      */ 
/*  532 */             foundMatch = true;
/*  533 */             break;
/*      */           }
/*      */         }
/*  536 */         if (!foundMatch)
/*      */         {
/*  538 */           RMDomain jarmDomain = getDomain();
/*  539 */           Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/*  540 */           ObjectStore jaceFPOS = getJaceObjectStore();
/*  541 */           String connRegReposNameValue = jaceDomain.get_Id().toString() + '.' + curCRIdent;
/*      */           
/*  543 */           newConnReg = Factory.CustomObject.createInstance(jaceFPOS, "ConnectorRegistration");
/*  544 */           newConnReg.getProperties().putValue("ImplementingClassName", "com.filenet.rm.api.impl.RMConnectorImpl");
/*  545 */           newConnReg.getProperties().putValue("ServerName", "*");
/*  546 */           newConnReg.getProperties().putValue("RepositoryType", "CE");
/*  547 */           newConnReg.getProperties().putValue("RepositoryName", connRegReposNameValue);
/*      */           
/*  549 */           newConnReg.save(RefreshMode.NO_REFRESH);
/*      */         }
/*      */         
/*      */ 
/*  553 */         cache.put(this, "CR", curCRIdent, curCRIdent);
/*      */       }
/*      */       finally
/*      */       {
/*  557 */         if (establishedSubject) {
/*  558 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/*  562 */     Tracer.traceMethodExit(new Object[] { newConnReg });
/*  563 */     return newConnReg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilePlan addFilePlan(String classIdent, RMProperties props, List<RMPermission> perms)
/*      */   {
/*  571 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms });
/*  572 */     FilePlan result = addFilePlan(classIdent, props, perms, null);
/*  573 */     Tracer.traceMethodExit(new Object[] { result });
/*  574 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilePlan addFilePlan(String classIdent, RMProperties props, List<RMPermission> perms, String idStr)
/*      */   {
/*  582 */     Tracer.traceMethodEntry(new Object[] { classIdent, props, perms, idStr });
/*  583 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  586 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  591 */       super.validateAndPrepareFilePlan(classIdent, props, perms);
/*      */       
/*      */ 
/*  594 */       ObjectStore jaceObjStore = getJaceObjectStore();
/*  595 */       String filePlanRootPath = getFilePlanRootPath();
/*  596 */       Folder jaceFilePlanRootFolder = Factory.Folder.getInstance(jaceObjStore, "ClassificationSchemes", filePlanRootPath);
/*  597 */       Id newId = P8CE_Util.processIdStr(idStr);
/*  598 */       Folder jaceBaseFolder = Factory.Folder.createInstance(jaceObjStore, "ClassificationScheme", newId);
/*      */       
/*  600 */       Properties jaceProps = jaceBaseFolder.getProperties();
/*  601 */       P8CE_Util.convertToJaceProperties(props, jaceProps);
/*      */       
/*  603 */       String filePlanName = props.getStringValue("ClassificationSchemeName").trim();
/*  604 */       jaceProps.putValue("FolderName", filePlanName);
/*      */       
/*  606 */       jaceProps.putValue("Parent", jaceFilePlanRootFolder);
/*      */       
/*  608 */       if (perms != null)
/*      */       {
/*  610 */         AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(perms);
/*  611 */         jaceBaseFolder.set_Permissions(jacePerms);
/*      */       }
/*      */       
/*  614 */       PropertyFilter jacePF = new PropertyFilter();
/*  615 */       for (FilterElement fe : P8CE_FilePlanImpl.getMandatoryJaceFEs()) {
/*  616 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*  618 */       long startTime = System.currentTimeMillis();
/*  619 */       jaceBaseFolder.save(RefreshMode.REFRESH, jacePF);
/*  620 */       long endTime = System.currentTimeMillis();
/*  621 */       Tracer.traceExtCall("Folder.save", startTime, endTime, null, null, new Object[] { RefreshMode.REFRESH, jacePF });
/*      */       
/*      */ 
/*  624 */       FilePlan result = (FilePlan)P8CE_FilePlanImpl.getGenerator().create(this, jaceBaseFolder);
/*      */       
/*  626 */       Tracer.traceMethodExit(new Object[] { result });
/*  627 */       return result;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  631 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  635 */       String repositoryIdent = getSymbolicName();
/*  636 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_ADD_FILEPLAN_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  640 */       if (establishedSubject) {
/*  641 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<FilePlan> getFilePlans(RMPropertyFilter jarmFilter)
/*      */   {
/*  650 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/*  651 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  654 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  656 */       String fpRootPath = getFilePlanRootPath();
/*  657 */       List<FilterElement> mandatoryFilePlanFEs = P8CE_FilePlanImpl.getMandatoryJaceFEs();
/*  658 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryFilePlanFEs);
/*      */       
/*      */ 
/*  661 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "ClassificationScheme", "fp");
/*      */       
/*  663 */       sb.append(" WHERE fp.[").append("Parent").append("] = OBJECT('").append(fpRootPath).append("') ");
/*  664 */       sb.append(" ORDER BY fp.[").append("ClassificationSchemeName").append("] ");
/*  665 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/*  668 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/*  669 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*      */       
/*  671 */       Integer pageSize = null;
/*  672 */       Boolean continuable = Boolean.TRUE;
/*      */       
/*  674 */       long startTime = System.currentTimeMillis();
/*  675 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/*  676 */       long stopTime = System.currentTimeMillis();
/*  677 */       Boolean elementCountIndicator = null;
/*  678 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  680 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/*  682 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*  685 */       List<FilePlan> resultList = new ArrayList();
/*  686 */       Folder jaceFolder = null;
/*  687 */       String objIdent = null;
/*  688 */       PageIterator jacePI = jaceObjSet.pageIterator();
/*  689 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/*  691 */         currentPage = jacePI.getCurrentPage();
/*  692 */         for (Object obj : currentPage)
/*      */         {
/*  694 */           jaceFolder = (Folder)obj;
/*  695 */           objIdent = jaceFolder.get_Id().toString();
/*  696 */           resultList.add(new P8CE_FilePlanImpl(this, objIdent, jaceFolder, false));
/*      */         }
/*      */       }
/*      */       
/*  700 */       Tracer.traceMethodExit(new Object[] { resultList });
/*  701 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  705 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  709 */       String repositoryIdent = getSymbolicName();
/*  710 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_FILEPLANS_FAILED, new Object[] { repositoryIdent });
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
/*      */ 
/*      */   public List<Location> getLocations(RMPropertyFilter jarmFilter)
/*      */   {
/*  725 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/*  726 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  729 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  731 */       StringBuilder sb = new StringBuilder(getFilePlanRootPath());
/*  732 */       sb.append('/').append("Locations");
/*  733 */       String locationsContainerPath = sb.toString();
/*  734 */       List<FilterElement> mandatoryFilePlanFEs = P8CE_LocationImpl.getMandatoryJaceFEs();
/*  735 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryFilePlanFEs);
/*      */       
/*      */ 
/*  738 */       sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "Location", "l");
/*      */       
/*  740 */       sb.append(" INNER JOIN [").append("ReferentialContainmentRelationship").append("] rcr");
/*  741 */       sb.append(" ON l.[This] = rcr.[").append("Head").append("]");
/*  742 */       sb.append(" WHERE (rcr.[").append("Tail").append("] = Object('").append(locationsContainerPath).append("')) ");
/*  743 */       sb.append(" ORDER BY l.[").append("LocationName").append("] ");
/*  744 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/*  747 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/*  748 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*  749 */       Integer pageSize = null;
/*  750 */       Boolean continuable = Boolean.TRUE;
/*      */       
/*  752 */       long startTime = System.currentTimeMillis();
/*  753 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/*  754 */       long stopTime = System.currentTimeMillis();
/*  755 */       Boolean elementCountIndicator = null;
/*  756 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  758 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/*  760 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/*  764 */       List<Location> resultList = new ArrayList();
/*  765 */       CustomObject jaceCustomObj = null;
/*  766 */       String objIdent = null;
/*  767 */       PageIterator jacePI = jaceObjSet.pageIterator();
/*  768 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/*  770 */         currentPage = jacePI.getCurrentPage();
/*  771 */         for (Object obj : currentPage)
/*      */         {
/*  773 */           jaceCustomObj = (CustomObject)obj;
/*  774 */           objIdent = jaceCustomObj.get_Id().toString();
/*  775 */           resultList.add(new P8CE_LocationImpl(this, objIdent, jaceCustomObj, false));
/*      */         }
/*      */       }
/*      */       
/*  779 */       Tracer.traceMethodExit(new Object[] { resultList });
/*  780 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  784 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  788 */       String repositoryIdent = getSymbolicName();
/*  789 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_LOCATIONS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  793 */       if (establishedSubject) {
/*  794 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<RMWorkflowDefinition> getWorkflowDefinitions(RMPropertyFilter jarmFilter)
/*      */   {
/*  803 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/*  804 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  807 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  809 */       StringBuilder sb = new StringBuilder(getFilePlanRootPath());
/*  810 */       sb.append('/').append("Workflows");
/*  811 */       String workflowsContainerPath = sb.toString();
/*  812 */       List<FilterElement> mandatoryFilePlanFEs = P8CE_WorkflowDefinitionImpl.getMandatoryJaceFEs();
/*  813 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryFilePlanFEs);
/*      */       
/*      */ 
/*  816 */       sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "WorkflowDefinition", "w");
/*      */       
/*  818 */       sb.append(" WHERE w.[This] INSUBFOLDER '").append(workflowsContainerPath).append("' ");
/*  819 */       sb.append(" ORDER BY w.[").append("DocumentTitle").append("] ");
/*  820 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/*  823 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/*  824 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*  825 */       Integer pageSize = null;
/*  826 */       Boolean continuable = Boolean.TRUE;
/*      */       
/*  828 */       long startTime = System.currentTimeMillis();
/*  829 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/*  830 */       long stopTime = System.currentTimeMillis();
/*  831 */       Boolean elementCountIndicator = null;
/*  832 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  834 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/*  836 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/*  840 */       List<RMWorkflowDefinition> resultList = new ArrayList();
/*  841 */       Document jaceDocument = null;
/*  842 */       String objIdent = null;
/*  843 */       PageIterator jacePI = jaceObjSet.pageIterator();
/*  844 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/*  846 */         currentPage = jacePI.getCurrentPage();
/*  847 */         for (Object obj : currentPage)
/*      */         {
/*  849 */           jaceDocument = (Document)obj;
/*  850 */           objIdent = jaceDocument.get_Id().toString();
/*  851 */           resultList.add(new P8CE_WorkflowDefinitionImpl(this, objIdent, jaceDocument, false));
/*      */         }
/*      */       }
/*      */       
/*  855 */       Tracer.traceMethodExit(new Object[] { resultList });
/*  856 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  860 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  864 */       String repositoryIdent = getSymbolicName();
/*  865 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_WORKFLOWDEFS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  869 */       if (establishedSubject) {
/*  870 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<RecordType> getRecordTypes(RMPropertyFilter jarmFilter)
/*      */   {
/*  879 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/*  880 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  883 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  885 */       List<FilterElement> mandatoryFilePlanFEs = P8CE_RecordTypeImpl.getMandatoryJaceFEs();
/*  886 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryFilePlanFEs);
/*      */       
/*      */ 
/*  889 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordType", "rt");
/*  890 */       sb.append(" ORDER BY rt.").append("RecordTypeName").append(" ");
/*  891 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/*  894 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/*  895 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*  896 */       Integer pageSize = null;
/*  897 */       Boolean continuable = Boolean.TRUE;
/*      */       
/*  899 */       long startTime = System.currentTimeMillis();
/*  900 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/*  901 */       long stopTime = System.currentTimeMillis();
/*  902 */       Boolean elementCountIndicator = null;
/*  903 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  905 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/*  907 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/*  911 */       List<RecordType> resultList = new ArrayList();
/*  912 */       CustomObject jaceCustomObj = null;
/*  913 */       String objIdent = null;
/*  914 */       PageIterator jacePI = jaceObjSet.pageIterator();
/*  915 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/*  917 */         currentPage = jacePI.getCurrentPage();
/*  918 */         for (Object obj : currentPage)
/*      */         {
/*  920 */           jaceCustomObj = (CustomObject)obj;
/*  921 */           objIdent = jaceCustomObj.get_Id().toString();
/*  922 */           resultList.add(new P8CE_RecordTypeImpl(this, objIdent, jaceCustomObj, false));
/*      */         }
/*      */       }
/*      */       
/*  926 */       Tracer.traceMethodExit(new Object[] { resultList });
/*  927 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/*  931 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/*  935 */       String repositoryIdent = getSymbolicName();
/*  936 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_RECORDTYPES_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/*  940 */       if (establishedSubject) {
/*  941 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<ClassificationGuide> getClassificationGuides(RMPropertyFilter jarmFilter)
/*      */   {
/*  950 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/*  951 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  954 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/*  956 */       if (!DataModelType.DoDClassified.equals(getDataModelType()))
/*      */       {
/*  958 */         String repositoryIdent = getSymbolicName();
/*  959 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_WRONG_DATA_MODEL_FOR_CLASSIF_GUIDES, new Object[] { repositoryIdent });
/*      */       }
/*      */       
/*  962 */       StringBuilder sb = new StringBuilder(getFilePlanRootPath());
/*  963 */       sb.append('/').append("Classification Guides");
/*  964 */       String guidesContainerPath = sb.toString();
/*      */       
/*  966 */       List<FilterElement> mandatoryClassGuideFEs = P8CE_ClassificationGuideImpl.getMandatoryJaceFEs();
/*  967 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryClassGuideFEs);
/*      */       
/*      */ 
/*  970 */       sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "ClassificationGuide", "g");
/*      */       
/*  972 */       sb.append(" WHERE g.").append("Parent").append(" = OBJECT('").append(guidesContainerPath).append("') ");
/*  973 */       sb.append(" ORDER BY g.[").append("GuideName").append("] ");
/*  974 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/*  977 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/*  978 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*  979 */       Integer pageSize = null;
/*  980 */       Boolean continuable = Boolean.TRUE;
/*      */       
/*  982 */       long startTime = System.currentTimeMillis();
/*  983 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/*  984 */       long stopTime = System.currentTimeMillis();
/*  985 */       Boolean elementCountIndicator = null;
/*  986 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/*  988 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/*  990 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/*  994 */       List<ClassificationGuide> resultList = new ArrayList();
/*  995 */       Folder jaceFolder = null;
/*  996 */       PageIterator jacePI = jaceObjSet.pageIterator();
/*  997 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/*  999 */         currentPage = jacePI.getCurrentPage();
/* 1000 */         for (Object obj : currentPage)
/*      */         {
/* 1002 */           jaceFolder = (Folder)obj;
/* 1003 */           resultList.add(new P8CE_ClassificationGuideImpl(this, jaceFolder));
/*      */         }
/*      */       }
/*      */       
/* 1007 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 1008 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1012 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1016 */       String repositoryIdent = getSymbolicName();
/* 1017 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_LOCATIONS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1021 */       if (establishedSubject) {
/* 1022 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Integer getDoDMaxDeclassifyTimeLimit()
/*      */   {
/* 1031 */     Tracer.traceMethodEntry(new Object[0]);
/* 1032 */     Integer result = null;
/*      */     
/*      */     try
/*      */     {
/* 1036 */       Map<String, SystemConfiguration> sysConfigs = getSystemConfigurations();
/* 1037 */       if (sysConfigs != null)
/*      */       {
/* 1039 */         SystemConfiguration sysConfig = (SystemConfiguration)sysConfigs.get("Max Declassification Offset");
/* 1040 */         if (sysConfig != null)
/*      */         {
/* 1042 */           String maxOffsetStrVal = sysConfig.getPropertyValue();
/* 1043 */           result = Integer.valueOf(maxOffsetStrVal, 10);
/*      */         }
/*      */       }
/*      */       
/* 1047 */       Tracer.traceMethodExit(new Object[] { result });
/* 1048 */       return result;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1052 */       throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_UNABLE_TO_GET_DOD_MAX_DECLASS_TIMELIMIT, new Object[] { getRepository().getName() });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<DispositionAction> getDispositionActions(RMPropertyFilter jarmFilter)
/*      */   {
/* 1061 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1062 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1065 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1067 */       List<FilterElement> mandatoryDispActionFEs = P8CE_DispositionActionImpl.getMandatoryJaceFEs();
/* 1068 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryDispActionFEs);
/*      */       
/*      */ 
/* 1071 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "Action1", "da");
/*      */       
/* 1073 */       sb.append(" ORDER BY da.[").append("ActionName").append("] ");
/* 1074 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/* 1077 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1078 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/* 1079 */       Integer pageSize = null;
/* 1080 */       Boolean continuable = Boolean.TRUE;
/*      */       
/* 1082 */       long startTime = System.currentTimeMillis();
/* 1083 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1084 */       long stopTime = System.currentTimeMillis();
/* 1085 */       Boolean elementCountIndicator = null;
/* 1086 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 1088 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/* 1090 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/* 1094 */       List<DispositionAction> resultList = new ArrayList();
/* 1095 */       CustomObject jaceCustomObj = null;
/* 1096 */       String objIdent = null;
/* 1097 */       PageIterator jacePI = jaceObjSet.pageIterator();
/* 1098 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/* 1100 */         currentPage = jacePI.getCurrentPage();
/* 1101 */         for (Object obj : currentPage)
/*      */         {
/* 1103 */           jaceCustomObj = (CustomObject)obj;
/* 1104 */           objIdent = jaceCustomObj.get_Id().toString();
/* 1105 */           resultList.add(new P8CE_DispositionActionImpl(this, objIdent, jaceCustomObj, false));
/*      */         }
/*      */       }
/*      */       
/* 1109 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 1110 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1114 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1118 */       String repositoryIdent = getSymbolicName();
/* 1119 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_DISPOSITION_ACTIONS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1123 */       if (establishedSubject) {
/* 1124 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<ReportDefinition> getReportDefinitions(RMPropertyFilter jarmFilter)
/*      */   {
/* 1133 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1134 */     boolean establishedSubject = false;
/* 1135 */     List<ReportDefinition> resultList = new ArrayList();
/*      */     
/*      */     try
/*      */     {
/* 1139 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1141 */       List<FilterElement> mandatoryReportDefFEs = P8CE_ReportDefinitionImpl.getMandatoryJaceFEs();
/* 1142 */       jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryReportDefFEs);
/*      */       
/*      */ 
/* 1145 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RMReportDefinition", "rd");
/*      */       
/* 1147 */       sb.append(" INNER JOIN [").append("ReferentialContainmentRelationship").append("] rcr");
/* 1148 */       sb.append(" ON rd.[This] = rcr.[").append("Head").append("]");
/* 1149 */       sb.append(" WHERE (rd.[").append("VersionStatus").append("] = 1)");
/* 1150 */       sb.append("   AND (rcr.[").append("Tail").append("] = Object('/Records Management/Report Definitions')) ");
/* 1151 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/* 1154 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1155 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/* 1156 */       Integer pageSize = null;
/* 1157 */       Boolean continuable = Boolean.TRUE;
/*      */       
/* 1159 */       long startTime = System.currentTimeMillis();
/* 1160 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1161 */       long stopTime = System.currentTimeMillis();
/*      */       
/* 1163 */       Boolean elementCountIndicator = null;
/* 1164 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 1166 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/* 1168 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/* 1172 */       Document jaceDocObj = null;
/* 1173 */       String objIdent = null;
/* 1174 */       PageIterator jacePI = jaceObjSet.pageIterator();
/* 1175 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/* 1177 */         currentPage = jacePI.getCurrentPage();
/* 1178 */         for (Object obj : currentPage)
/*      */         {
/* 1180 */           jaceDocObj = (Document)obj;
/* 1181 */           objIdent = jaceDocObj.get_Id().toString();
/* 1182 */           resultList.add(new P8CE_ReportDefinitionImpl(this, objIdent, jaceDocObj, false));
/*      */         }
/*      */       }
/*      */       
/* 1186 */       if (resultList.size() > 1) {
/* 1187 */         Collections.sort(resultList, new P8CE_ReportDefinitionImpl.ReportDefinitionComparator());
/*      */       }
/* 1189 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 1190 */       return resultList;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*      */       PropertyFilter jacePF;
/* 1195 */       if (ex.getExceptionCode() == ExceptionCode.E_BAD_CLASSID) {
/* 1196 */         return resultList;
/*      */       }
/*      */       
/* 1199 */       String repositoryIdent = getSymbolicName();
/* 1200 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_RETRIEVING_REPORT_DEFINITIONS_FAILED, new Object[] { repositoryIdent });
/*      */ 
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1205 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1209 */       String repositoryIdent = getSymbolicName();
/* 1210 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_REPORT_DEFINITIONS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1214 */       if (establishedSubject) {
/* 1215 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<DispositionTrigger> getDispositionTriggers(RMPropertyFilter jarmFilter)
/*      */   {
/* 1224 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1225 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1228 */       establishedSubject = P8CE_Util.associateSubject();
/* 1229 */       List<FilterElement> mandatoryDispTriggerFEs = P8CE_DispositionTriggerImpl.getMandatoryJaceFEs();
/* 1230 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryDispTriggerFEs);
/*      */       
/*      */ 
/* 1233 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "DisposalTrigger", "dt");
/*      */       
/* 1235 */       sb.append(" ORDER BY dt.[").append("DisposalTriggerName").append("] ");
/* 1236 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/* 1239 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1240 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/* 1241 */       Integer pageSize = null;
/* 1242 */       Boolean continuable = Boolean.TRUE;
/*      */       
/* 1244 */       long startTime = System.currentTimeMillis();
/* 1245 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1246 */       long stopTime = System.currentTimeMillis();
/* 1247 */       Boolean elementCountIndicator = null;
/* 1248 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 1250 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/* 1252 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/* 1256 */       List<DispositionTrigger> resultList = new ArrayList();
/* 1257 */       CustomObject jaceCustomObj = null;
/* 1258 */       String objIdent = null;
/* 1259 */       PageIterator jacePI = jaceObjSet.pageIterator();
/* 1260 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/* 1262 */         currentPage = jacePI.getCurrentPage();
/* 1263 */         for (Object obj : currentPage)
/*      */         {
/* 1265 */           jaceCustomObj = (CustomObject)obj;
/* 1266 */           objIdent = jaceCustomObj.get_Id().toString();
/* 1267 */           resultList.add(new P8CE_DispositionTriggerImpl(this, objIdent, jaceCustomObj, false));
/*      */         }
/*      */       }
/*      */       
/* 1271 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 1272 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1276 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1280 */       String repositoryIdent = getSymbolicName();
/* 1281 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_DISPOSITION_TRIGGERS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1285 */       if (establishedSubject) {
/* 1286 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<DispositionSchedule> getDispositionSchedules(RMPropertyFilter jarmFilter)
/*      */   {
/* 1295 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1296 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1299 */       establishedSubject = P8CE_Util.associateSubject();
/*      */       
/* 1301 */       List<FilterElement> mandatoryDispScheduleFEs = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 1302 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryDispScheduleFEs);
/*      */       
/*      */ 
/* 1305 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "DisposalSchedule", "ds");
/*      */       
/* 1307 */       sb.append(" ORDER BY ds.[").append("DisposalScheduleName").append("] ");
/* 1308 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/* 1311 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1312 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/* 1313 */       Integer pageSize = null;
/* 1314 */       Boolean continuable = Boolean.TRUE;
/*      */       
/* 1316 */       long startTime = System.currentTimeMillis();
/* 1317 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1318 */       long stopTime = System.currentTimeMillis();
/* 1319 */       Boolean elementCountIndicator = null;
/* 1320 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 1322 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/* 1324 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/* 1328 */       List<DispositionSchedule> resultList = new ArrayList();
/* 1329 */       CustomObject jaceCustomObj = null;
/* 1330 */       String objIdent = null;
/* 1331 */       PageIterator jacePI = jaceObjSet.pageIterator();
/* 1332 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/* 1334 */         currentPage = jacePI.getCurrentPage();
/* 1335 */         for (Object obj : currentPage)
/*      */         {
/* 1337 */           jaceCustomObj = (CustomObject)obj;
/* 1338 */           objIdent = jaceCustomObj.get_Id().toString();
/* 1339 */           resultList.add(new P8CE_DispositionScheduleImpl(this, objIdent, jaceCustomObj, false));
/*      */         }
/*      */       }
/*      */       
/* 1343 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 1344 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1348 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1352 */       String repositoryIdent = getSymbolicName();
/* 1353 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_DISPOSITION_SCHEDULES_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1357 */       if (establishedSubject) {
/* 1358 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<Hold> getHolds(RMPropertyFilter jarmFilter)
/*      */   {
/* 1367 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 1368 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1371 */       establishedSubject = P8CE_Util.associateSubject();
/* 1372 */       List<FilterElement> mandatoryHoldFEs = P8CE_HoldImpl.getMandatoryJaceFEs();
/* 1373 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryHoldFEs);
/*      */       
/*      */ 
/* 1376 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "RecordHold", "hld");
/*      */       
/* 1378 */       sb.append(" ORDER BY hld.[").append("HoldName").append("] ");
/* 1379 */       String sqlStatement = sb.toString();
/*      */       
/*      */ 
/* 1382 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 1383 */       SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/* 1384 */       Integer pageSize = null;
/* 1385 */       Boolean continuable = Boolean.TRUE;
/*      */       
/* 1387 */       long startTime = System.currentTimeMillis();
/* 1388 */       IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 1389 */       long stopTime = System.currentTimeMillis();
/* 1390 */       Boolean elementCountIndicator = null;
/* 1391 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 1393 */         elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */       }
/* 1395 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceObjSet, new Object[] { sqlStatement, pageSize, jacePF, continuable });
/*      */       
/*      */ 
/*      */ 
/* 1399 */       List<Hold> resultList = new ArrayList();
/* 1400 */       PageIterator jacePI = jaceObjSet.pageIterator();
/* 1401 */       CustomObject jaceHoldBase = null;
/* 1402 */       String objIdent = null;
/* 1403 */       Object[] currentPage; while (jacePI.nextPage())
/*      */       {
/* 1405 */         currentPage = jacePI.getCurrentPage();
/* 1406 */         for (Object obj : currentPage)
/*      */         {
/* 1408 */           jaceHoldBase = (CustomObject)obj;
/* 1409 */           objIdent = jaceHoldBase.get_Id().toString();
/* 1410 */           resultList.add(new P8CE_HoldImpl(this, objIdent, jaceHoldBase, false));
/*      */         }
/*      */       }
/*      */       
/* 1414 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 1415 */       return resultList;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1419 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1423 */       String repositoryIdent = getSymbolicName();
/* 1424 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_HOLDS_FAILED, new Object[] { repositoryIdent });
/*      */     }
/*      */     finally
/*      */     {
/* 1428 */       if (establishedSubject) {
/* 1429 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1440 */     return super.toString("P8CE_FilePlanRepositoryImpl");
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
/*      */   public static boolean isValidFPOS(ObjectStore jaceObjectStore)
/*      */   {
/* 1455 */     Tracer.traceMethodEntry(new Object[] { jaceObjectStore });
/* 1456 */     boolean result = false;
/*      */     
/*      */ 
/* 1459 */     String objStoreIdStr = jaceObjectStore.get_Id().toString();
/* 1460 */     Boolean validationState = (Boolean)ValidFilePlanRepositoryCache.get(objStoreIdStr);
/* 1461 */     if (Boolean.TRUE.equals(validationState))
/*      */     {
/* 1463 */       result = true;
/*      */     }
/*      */     else
/*      */     {
/* 1467 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/* 1470 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/*      */ 
/*      */ 
/* 1474 */         if (P8CE_SystemConfigurationImpl.isSysConfigClassDefined(jaceObjectStore))
/*      */         {
/*      */ 
/*      */ 
/* 1478 */           SearchScope jaceSearchScope = new SearchScope(jaceObjectStore);
/* 1479 */           SearchSQL jaceSearchSQL = new SearchSQL(SQL_SYSCONFIG_FPOS_SETUP);
/* 1480 */           Integer pageSize = null;
/* 1481 */           Boolean continuable = Boolean.FALSE;
/*      */           
/* 1483 */           long startTime = System.currentTimeMillis();
/* 1484 */           RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, P8CE_Util.CEPF_IdOnly, continuable);
/* 1485 */           long stopTime = System.currentTimeMillis();
/* 1486 */           Boolean elementCountIndicator = null;
/* 1487 */           if (Tracer.isMediumTraceEnabled())
/*      */           {
/* 1489 */             elementCountIndicator = rowSet != null ? Boolean.valueOf(rowSet.isEmpty()) : null;
/*      */           }
/* 1491 */           Tracer.traceExtCall("SearchScope.fetchRows", startTime, stopTime, elementCountIndicator, rowSet, new Object[] { SQL_SYSCONFIG_FPOS_SETUP, pageSize, P8CE_Util.CEPF_IdOnly, continuable });
/*      */           
/*      */ 
/* 1494 */           result = (rowSet != null) && (!rowSet.isEmpty());
/*      */         }
/*      */         
/* 1497 */         ValidFilePlanRepositoryCache.put(objStoreIdStr, Boolean.valueOf(result));
/*      */       }
/*      */       finally
/*      */       {
/* 1501 */         if (establishedSubject) {
/* 1502 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/* 1506 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1507 */     return result;
/*      */   }
/*      */   
/*      */   protected boolean filePlanNameIsUnique(String filePlanName)
/*      */   {
/* 1512 */     Tracer.traceMethodEntry(new Object[] { filePlanName });
/* 1513 */     Boolean result = Boolean.valueOf(true);
/* 1514 */     String filePlanRootPath = getFilePlanRootPath();
/*      */     
/*      */ 
/* 1517 */     StringBuilder sb = new StringBuilder();
/* 1518 */     sb.append("SELECT TOP 1 [").append("Id").append("] ");
/* 1519 */     sb.append("FROM [").append("ClassificationScheme").append("] ");
/* 1520 */     sb.append("WHERE [").append("ClassificationSchemeName").append("] = '").append(RALBaseEntity.escapeSQLStringValue(filePlanName)).append("'");
/* 1521 */     sb.append("  AND [").append("Parent").append("] = OBJECT('").append(RALBaseEntity.escapeSQLStringValue(filePlanRootPath)).append("') ");
/* 1522 */     String sqlStmt = sb.toString();
/*      */     
/* 1524 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 1525 */     SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/*      */     
/* 1527 */     long startTime = System.currentTimeMillis();
/* 1528 */     RepositoryRowSet rowSet = jaceSearchScope.fetchRows(jaceSearchSQL, null, null, Boolean.FALSE);
/* 1529 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, System.currentTimeMillis(), Integer.valueOf(1), rowSet, new Object[] { sqlStmt });
/*      */     
/* 1531 */     if ((rowSet != null) && (!rowSet.isEmpty())) {
/* 1532 */       result = Boolean.valueOf(false);
/*      */     }
/* 1534 */     Tracer.traceMethodExit(new Object[] { result });
/* 1535 */     return result.booleanValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<Integer> getFilePlanInitialAllowedRMTypes(String classIdent)
/*      */   {
/* 1544 */     Tracer.traceMethodEntry(new Object[] { classIdent });
/*      */     
/* 1546 */     List<Integer> result = new ArrayList();
/* 1547 */     result.add(Integer.valueOf(101));
/*      */     
/* 1549 */     Tracer.traceMethodExit(new Object[] { result });
/* 1550 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   String getFilePlanRootPath()
/*      */   {
/* 1562 */     Tracer.traceMethodEntry(new Object[0]);
/* 1563 */     if (this.filePlanRootPath == null)
/*      */     {
/* 1565 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/* 1568 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/* 1570 */         SearchSQL jaceSearchSQL = new SearchSQL(SQL_FILEPLAN_ROOT_PATH);
/* 1571 */         SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/* 1572 */         PropertyFilter jacePF = new PropertyFilter();
/* 1573 */         jacePF.addIncludeProperty(0, null, null, "PathName", Integer.valueOf(1));
/*      */         
/*      */ 
/* 1576 */         long startTime = System.currentTimeMillis();
/* 1577 */         IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, null, jacePF, Boolean.FALSE);
/* 1578 */         Tracer.traceExtCall("SearchScope.fetchObjects", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceObjSet, new Object[] { SQL_FILEPLAN_ROOT_PATH });
/*      */         
/*      */ 
/* 1581 */         if ((jaceObjSet != null) && (!jaceObjSet.isEmpty()))
/*      */         {
/* 1583 */           Iterator<Folder> it = jaceObjSet.iterator();
/* 1584 */           Folder jaceFolder = (Folder)it.next();
/* 1585 */           if (jaceFolder != null)
/*      */           {
/* 1587 */             if (!jaceFolder.getProperties().isPropertyPresent("PathName"))
/*      */             {
/* 1589 */               jaceFolder.fetchProperties(new String[] { "PathName" });
/*      */             }
/* 1591 */             this.filePlanRootPath = jaceFolder.getProperties().getStringValue("PathName").trim();
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1596 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_FILEPLAN_ROOT_UNAVAILABLE, new Object[] { getObjectIdentity() });
/*      */         }
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 1601 */         throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_FILEPLAN_ROOT_UNAVAILABLE, new Object[] { getObjectIdentity() });
/*      */       }
/*      */       finally
/*      */       {
/* 1605 */         if (establishedSubject) {
/* 1606 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/* 1610 */     Tracer.traceMethodExit(new Object[] { this.filePlanRootPath });
/* 1611 */     return this.filePlanRootPath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsRecordReceipts()
/*      */   {
/* 1620 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*      */ 
/* 1623 */     P8CE_CacheService cache = P8CE_CacheService.getInstance();
/* 1624 */     Boolean hasReceipts = (Boolean)cache.get(this, "MF", "SupportsReceipts");
/* 1625 */     if (hasReceipts == null)
/*      */     {
/*      */ 
/* 1628 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/* 1631 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/* 1633 */         hasReceipts = Boolean.FALSE;
/* 1634 */         resolve();
/* 1635 */         boolean allowFromCache = true;
/* 1636 */         RMClassDescription recordCD = P8CE_Util.getClassDescription(this, "RecordInfo", allowFromCache);
/* 1637 */         if ((recordCD.getPropertyDescription("ReceiptOf") != null) && (recordCD.getPropertyDescription("Receipts") != null) && (recordCD.getPropertyDescription("ReceiptStatus") != null))
/*      */         {
/*      */ 
/*      */ 
/* 1641 */           hasReceipts = Boolean.TRUE;
/*      */         }
/*      */         
/*      */ 
/* 1645 */         cache.put(this, "MF", "SupportsReceipts", hasReceipts);
/*      */       }
/*      */       finally
/*      */       {
/* 1649 */         if (establishedSubject) {
/* 1650 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/* 1654 */     boolean result = hasReceipts != null ? hasReceipts.booleanValue() : false;
/* 1655 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1656 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsExternalManagement()
/*      */   {
/* 1664 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*      */ 
/* 1667 */     P8CE_CacheService cache = P8CE_CacheService.getInstance();
/* 1668 */     Boolean supportsExtManagement = (Boolean)cache.get(this, "MF", "SupportsExternalManagement");
/* 1669 */     if (supportsExtManagement == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1674 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/* 1677 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/* 1679 */         supportsExtManagement = Boolean.FALSE;
/* 1680 */         resolve();
/* 1681 */         boolean allowFromCache = true;
/* 1682 */         RMClassDescription recordCD = P8CE_Util.getClassDescription(this, "DisposalSchedule", allowFromCache);
/* 1683 */         if (recordCD.getPropertyDescription("RMExternallyManagedBy") != null)
/*      */         {
/* 1685 */           supportsExtManagement = Boolean.TRUE;
/*      */         }
/*      */         
/*      */ 
/* 1689 */         cache.put(this, "MF", "SupportsExternalManagement", supportsExtManagement);
/*      */       }
/*      */       finally
/*      */       {
/* 1693 */         if (establishedSubject) {
/* 1694 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/* 1698 */     boolean result = supportsExtManagement != null ? supportsExtManagement.booleanValue() : false;
/* 1699 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1700 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsDefensibleDisposal()
/*      */   {
/* 1709 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/*      */ 
/* 1712 */     P8CE_CacheService cache = P8CE_CacheService.getInstance();
/* 1713 */     Boolean supportsDD = (Boolean)cache.get(this, "MF", "SupportsDefensibleDisposal");
/* 1714 */     if (supportsDD == null)
/*      */     {
/*      */ 
/* 1717 */       boolean establishedSubject = false;
/*      */       try
/*      */       {
/* 1720 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/* 1722 */         supportsDD = Boolean.FALSE;
/* 1723 */         resolve();
/*      */         
/* 1725 */         String sqlStr = "SELECT pt.Id, pt.SymbolicName, pt.UsedInClasses FROM PropertyTemplate pt WHERE (pt.SymbolicName = 'RMRetentionPeriod') OR (pt.SymbolicName = 'RMRetentionTriggerPropertyName') ";
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1730 */         SearchSQL jaceSearchSQL = new SearchSQL(sqlStr);
/* 1731 */         SearchScope jaceSearchScope = new SearchScope(getJaceObjectStore());
/* 1732 */         Integer pageSize = null;
/* 1733 */         Boolean continuable = Boolean.FALSE;
/*      */         
/* 1735 */         long startTime = System.currentTimeMillis();
/* 1736 */         IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, JACE_CONNECTOR_REGISTRATION_PF, continuable);
/* 1737 */         long endTime = System.currentTimeMillis();
/* 1738 */         Boolean elementCountIndicator = null;
/* 1739 */         if (Tracer.isMediumTraceEnabled())
/*      */         {
/* 1741 */           elementCountIndicator = Boolean.valueOf(jaceObjSet.isEmpty());
/*      */         }
/* 1743 */         Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, jaceObjSet, new Object[] { SQL_CONNECTOR_REGISTRATIONS, null, JACE_CONNECTOR_REGISTRATION_PF, continuable });
/*      */         
/*      */ 
/* 1746 */         int validCount = 0;
/* 1747 */         for (Iterator it = jaceObjSet.iterator(); it.hasNext();)
/*      */         {
/* 1749 */           PropertyTemplate pt = (PropertyTemplate)it.next();
/* 1750 */           ClassDefinitionSet usedInClassSet = pt.get_UsedInClasses();
/* 1751 */           if (!usedInClassSet.isEmpty()) {
/* 1752 */             validCount++;
/*      */           }
/*      */         }
/* 1755 */         supportsDD = Boolean.valueOf(validCount == 2);
/*      */         
/*      */ 
/* 1758 */         cache.put(this, "MF", "SupportsDefensibleDisposal", supportsDD);
/*      */       }
/*      */       finally
/*      */       {
/* 1762 */         if (establishedSubject) {
/* 1763 */           P8CE_Util.disassociateSubject();
/*      */         }
/*      */       }
/*      */     }
/* 1767 */     boolean result = supportsDD != null ? supportsDD.booleanValue() : false;
/* 1768 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1769 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRecordMultiFilingEnabled()
/*      */   {
/* 1777 */     Tracer.traceMethodEntry(new Object[0]);
/* 1778 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 1781 */       establishedSubject = P8CE_Util.associateSubject();
/* 1782 */       resolve();
/* 1783 */       Boolean allowsMultiFiling = Boolean.TRUE;
/* 1784 */       Map<String, SystemConfiguration> sysConfigItems = getSystemConfigurations();
/* 1785 */       String itemValue; if (sysConfigItems != null)
/*      */       {
/* 1787 */         SystemConfiguration configItem = (SystemConfiguration)sysConfigItems.get("Allow Record Multi-Filing");
/* 1788 */         if (configItem != null)
/*      */         {
/* 1790 */           itemValue = configItem.getPropertyValue();
/* 1791 */           if ((itemValue != null) && (Boolean.valueOf(itemValue).equals(Boolean.FALSE)))
/* 1792 */             allowsMultiFiling = Boolean.FALSE;
/*      */         }
/*      */       }
/* 1795 */       boolean result = allowsMultiFiling != null ? allowsMultiFiling.booleanValue() : true;
/* 1796 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1797 */       return result;
/*      */     }
/*      */     finally
/*      */     {
/* 1801 */       if (establishedSubject) {
/* 1802 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public RMWorkflowDefinition getAutoDestroyWorkflow(String idStr, String name)
/*      */   {
/* 1809 */     Tracer.traceMethodEntry(new Object[] { idStr, name });
/*      */     
/*      */ 
/* 1812 */     Document jaceWflBase = null;
/* 1813 */     ObjectStore jaceObjStore = getJaceObjectStore();
/* 1814 */     String sqlStmt = "SELECT Id, DocumentTitle FROM WorkflowDefinition WHERE Id = '" + idStr + "' ";
/*      */     
/* 1816 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 1817 */     SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/* 1818 */     IndependentObjectSet jaceObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, null, null, Boolean.FALSE);
/* 1819 */     if ((jaceObjSet != null) && (!jaceObjSet.isEmpty()))
/*      */     {
/* 1821 */       Iterator<Document> it = jaceObjSet.iterator();
/* 1822 */       jaceWflBase = (Document)it.next();
/*      */     }
/*      */     
/* 1825 */     if (jaceWflBase == null)
/*      */     {
/* 1827 */       Id wflId = new Id(idStr);
/* 1828 */       jaceWflBase = Factory.Document.createInstance(jaceObjStore, "WorkflowDefinition", wflId);
/* 1829 */       jaceWflBase.getProperties().putValue("DocumentTitle", name);
/* 1830 */       jaceWflBase.save(RefreshMode.REFRESH);
/*      */     }
/*      */     
/* 1833 */     RMWorkflowDefinition result = new P8CE_WorkflowDefinitionImpl(this, idStr, jaceWflBase, false);
/*      */     
/* 1835 */     Tracer.traceMethodExit(new Object[] { result });
/* 1836 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void launchWorkflows(List<BaseEntity> entities, String separationPropertyName, int workflowType, Object vwSession)
/*      */   {
/* 1845 */     Tracer.traceMethodEntry(new Object[] { entities, separationPropertyName, Integer.valueOf(workflowType), vwSession });
/* 1846 */     RALWorkflowSupport.launchWorkflows(this, entities, separationPropertyName, workflowType, vwSession);
/* 1847 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class Generator
/*      */     implements IGenerator<FilePlanRepository>
/*      */   {
/*      */     public FilePlanRepository create(Repository repository, Object jaceBaseObject)
/*      */     {
/* 1862 */       P8CE_FilePlanRepositoryImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 1863 */       ObjectStore jaceObjStore = (ObjectStore)jaceBaseObject;
/*      */       
/* 1865 */       Domain jaceDomain = P8CE_Util.getJaceDomain(jaceObjStore);
/* 1866 */       RMDomain rmDomain = (RMDomain)P8CE_RMDomainImpl.getGenerator().create(null, jaceDomain);
/* 1867 */       String reposIdentity = jaceObjStore.get_Id().toString();
/* 1868 */       FilePlanRepository result = new P8CE_FilePlanRepositoryImpl(rmDomain, reposIdentity, jaceObjStore, false, false);
/*      */       
/* 1870 */       P8CE_FilePlanRepositoryImpl.Tracer.traceMethodExit(new Object[] { result });
/* 1871 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_FilePlanRepositoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */