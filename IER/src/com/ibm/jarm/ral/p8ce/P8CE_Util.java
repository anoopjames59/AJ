/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.action.Create;
/*      */ import com.filenet.api.action.PendingAction;
/*      */ import com.filenet.api.admin.ChoiceList;
/*      */ import com.filenet.api.admin.ClassDefinition;
/*      */ import com.filenet.api.admin.PropertyDefinition;
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.Integer32List;
/*      */ import com.filenet.api.collection.PageIterator;
/*      */ import com.filenet.api.collection.PropertyDefinitionList;
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.collection.StringList;
/*      */ import com.filenet.api.constants.FilteredPropertyType;
/*      */ import com.filenet.api.constants.PrincipalSearchAttribute;
/*      */ import com.filenet.api.constants.PrincipalSearchSortType;
/*      */ import com.filenet.api.constants.PrincipalSearchType;
/*      */ import com.filenet.api.constants.RefreshMode;
/*      */ import com.filenet.api.core.Connection;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Domain;
/*      */ import com.filenet.api.core.EngineObject;
/*      */ import com.filenet.api.core.Factory.AccessPermission;
/*      */ import com.filenet.api.core.Factory.ClassDefinition;
/*      */ import com.filenet.api.core.Factory.Integer32List;
/*      */ import com.filenet.api.core.Factory.ObjectStore;
/*      */ import com.filenet.api.core.Factory.Realm;
/*      */ import com.filenet.api.core.Factory.User;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.core.IndependentlyPersistableObject;
/*      */ import com.filenet.api.core.Link;
/*      */ import com.filenet.api.core.ObjectReference;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.RepositoryObject;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.events.Event;
/*      */ import com.filenet.api.exception.EngineRuntimeException;
/*      */ import com.filenet.api.exception.ErrorStack;
/*      */ import com.filenet.api.exception.ExceptionCode;
/*      */ import com.filenet.api.meta.ClassDescription;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.Property;
/*      */ import com.filenet.api.property.PropertyBinaryList;
/*      */ import com.filenet.api.property.PropertyBoolean;
/*      */ import com.filenet.api.property.PropertyBooleanList;
/*      */ import com.filenet.api.property.PropertyDateTime;
/*      */ import com.filenet.api.property.PropertyDateTimeList;
/*      */ import com.filenet.api.property.PropertyDependentObjectList;
/*      */ import com.filenet.api.property.PropertyEngineObject;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.property.PropertyFloat64;
/*      */ import com.filenet.api.property.PropertyFloat64List;
/*      */ import com.filenet.api.property.PropertyId;
/*      */ import com.filenet.api.property.PropertyIdList;
/*      */ import com.filenet.api.property.PropertyIndependentObjectSet;
/*      */ import com.filenet.api.property.PropertyInteger32;
/*      */ import com.filenet.api.property.PropertyInteger32List;
/*      */ import com.filenet.api.property.PropertyString;
/*      */ import com.filenet.api.property.PropertyStringList;
/*      */ import com.filenet.api.query.RepositoryRow;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.security.AccessPermission;
/*      */ import com.filenet.api.security.MarkingSet;
/*      */ import com.filenet.api.security.Realm;
/*      */ import com.filenet.api.security.User;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.filenet.api.util.UserContext;
/*      */ import com.filenet.apiimpl.property.PropertiesImpl;
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.constants.ContentXMLExport;
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMPermissionSource;
/*      */ import com.ibm.jarm.api.constants.RMPrincipalSearchAttribute;
/*      */ import com.ibm.jarm.api.constants.RMPrincipalSearchSortType;
/*      */ import com.ibm.jarm.api.constants.RMPrincipalSearchType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.core.AuditEvent;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.Container;
/*      */ import com.ibm.jarm.api.core.DispositionPhase;
/*      */ import com.ibm.jarm.api.core.DispositionSchedule;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.Hold;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.RMLink;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.core.SystemConfiguration;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMErrorStack;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.property.RMFilterElement;
/*      */ import com.ibm.jarm.api.property.RMFilteredPropertyType;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.RMUserContext;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*      */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.StringWriter;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javax.security.auth.Subject;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class P8CE_Util
/*      */ {
/*  128 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
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
/*      */   static final int UPDATE_PROPERTY_BATCH_SIZE = 10;
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
/*  157 */   static final PropertyFilter CEPF_IdOnly = new PropertyFilter();
/*  158 */   static { CEPF_IdOnly.addIncludeProperty(0, null, null, "Id", null); }
/*      */   
/*  160 */   static final PropertyFilter CEPF_Empty = new PropertyFilter();
/*      */   private static String ContainerTypes_SpaceSeparatedPropNameList;
/*  162 */   static final Map<String, EntityType> ClassSymNameToEntityTypeMap = createClassToEntityMap();
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
/*      */   static final int IER_ACCESS_LEVEL_FULL_CONTROL = 987127;
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
/*      */   static synchronized String getContainerTypesPropNames()
/*      */   {
/*  193 */     if (ContainerTypes_SpaceSeparatedPropNameList == null)
/*      */     {
/*      */ 
/*      */ 
/*  197 */       String[] combinedPropNameArray = P8CE_BaseContainerImpl.getMandatoryPropertyNames();
/*  198 */       ContainerTypes_SpaceSeparatedPropNameList = createSpaceSeparatedString(combinedPropNameArray);
/*      */     }
/*      */     
/*  201 */     return ContainerTypes_SpaceSeparatedPropNameList;
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
/*      */   public static boolean associateSubject()
/*      */   {
/*  233 */     Tracer.traceMethodEntry(new Object[0]);
/*  234 */     boolean result = false;
/*  235 */     RMUserContext rmUC = RMUserContext.get();
/*  236 */     Subject subject = rmUC.getSubject();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  241 */     if (subject != null)
/*      */     {
/*  243 */       UserContext jaceUC = UserContext.get();
/*  244 */       jaceUC.pushSubject(subject);
/*  245 */       result = true;
/*      */       
/*  247 */       if (Tracer.isMaximumTraceEnabled())
/*      */       {
/*  249 */         Tracer.traceMaximumMsg("Associated Subject<{0}> with Thread {1}.", new Object[] { RMUserContext.getPrincipals(subject), Integer.valueOf(System.identityHashCode(Thread.currentThread())) });
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */     }
/*  256 */     else if (Tracer.isMaximumTraceEnabled())
/*      */     {
/*  258 */       Tracer.traceMaximumMsg("Associated Subject<none available> with Thread {0}. Ambient Subject will be relied upon.", new Object[] { Integer.valueOf(System.identityHashCode(Thread.currentThread())) });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  263 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  264 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Subject disassociateSubject()
/*      */   {
/*  276 */     Tracer.traceMethodEntry(new Object[0]);
/*  277 */     UserContext jaceUC = UserContext.get();
/*  278 */     Subject subject = jaceUC.popSubject();
/*      */     
/*  280 */     if (Tracer.isMaximumTraceEnabled())
/*      */     {
/*  282 */       Tracer.traceMaximumMsg("Disassociated Subject<{0}> from Thread {1}.", new Object[] { RMUserContext.getPrincipals(subject), Integer.valueOf(System.identityHashCode(Thread.currentThread())) });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  287 */     Tracer.traceMethodExit(new Object[] { subject });
/*  288 */     return subject;
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
/*      */   static PropertyFilter convertToJacePF(RMPropertyFilter jarmFilter, List<FilterElement> additionalJaceFEs)
/*      */   {
/*  306 */     Tracer.traceMethodEntry(new Object[] { jarmFilter, additionalJaceFEs });
/*  307 */     PropertyFilter jaceFilter = null;
/*      */     
/*      */ 
/*  310 */     if ((jarmFilter == null) || (jarmFilter.equals(RMPropertyFilter.AllNonObjectPropertySet)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  317 */       jaceFilter = null;
/*      */     }
/*      */     else
/*      */     {
/*  321 */       jaceFilter = new PropertyFilter();
/*      */       
/*      */ 
/*      */ 
/*  325 */       if (jarmFilter.getLevelDependents() != null)
/*  326 */         jaceFilter.setLevelDependents(jarmFilter.getLevelDependents());
/*  327 */       if (jarmFilter.getMaxContentSize() != null)
/*  328 */         jaceFilter.setMaxSize(jarmFilter.getMaxContentSize());
/*  329 */       if (jarmFilter.getMaxRecursion() != null)
/*  330 */         jaceFilter.setMaxRecursion(jarmFilter.getMaxRecursion());
/*  331 */       if (jarmFilter.getPageSize() != null) {
/*  332 */         jaceFilter.setPageSize(jarmFilter.getPageSize());
/*      */       }
/*      */       
/*  335 */       for (String excludedPropertyName : jarmFilter.getExcludePropertyNames())
/*      */       {
/*  337 */         jaceFilter.addExcludeProperty(excludedPropertyName);
/*      */       }
/*      */       
/*      */ 
/*  341 */       FilterElement jaceFE = null;
/*  342 */       FilteredPropertyType jaceFPT = null;
/*  343 */       for (RMFilterElement jarmFE : jarmFilter.getIncludeProperties())
/*      */       {
/*  345 */         if (jarmFE != null)
/*      */         {
/*  347 */           if (jarmFE.getFilteredPropertyType() != null)
/*      */           {
/*  349 */             jaceFPT = convertToJaceFPT(jarmFE.getFilteredPropertyType());
/*  350 */             jaceFE = new FilterElement(jarmFE.getMaxRecursion(), jarmFE.getMaxContentSize(), jarmFE.getLevelDependents(), jaceFPT, jarmFE.getPageSize());
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */ 
/*  358 */             jaceFE = new FilterElement(jarmFE.getMaxRecursion(), jarmFE.getMaxContentSize(), jarmFE.getLevelDependents(), jarmFE.getSymbolicNames(), jarmFE.getPageSize());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  365 */           jaceFilter.addIncludeProperty(jaceFE);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  370 */       if (additionalJaceFEs != null)
/*      */       {
/*  372 */         for (FilterElement mandatoryJaceFE : additionalJaceFEs)
/*      */         {
/*  374 */           jaceFilter.addIncludeProperty(mandatoryJaceFE);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  379 */     Tracer.traceMethodExit(new Object[] { jaceFilter });
/*  380 */     return jaceFilter;
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
/*      */   static FilteredPropertyType convertToJaceFPT(RMFilteredPropertyType jarmFPT)
/*      */   {
/*  393 */     switch (jarmFPT.getIntValue())
/*      */     {
/*      */     case 396: 
/*  396 */       return FilteredPropertyType.ANY;
/*      */     case 397: 
/*  398 */       return FilteredPropertyType.ANY_LIST;
/*      */     case 395: 
/*  400 */       return FilteredPropertyType.ANY_NON_OBJECT;
/*      */     case 398: 
/*  402 */       return FilteredPropertyType.ANY_SINGLETON;
/*      */     case 399: 
/*  404 */       return FilteredPropertyType.CONTENT_DATA;
/*      */     case 299: 
/*  406 */       return FilteredPropertyType.ENUM_OF_OBJECT;
/*      */     case 201: 
/*  408 */       return FilteredPropertyType.LIST_OF_BINARY;
/*      */     case 202: 
/*  410 */       return FilteredPropertyType.LIST_OF_BOOLEAN;
/*      */     case 203: 
/*  412 */       return FilteredPropertyType.LIST_OF_DATE;
/*      */     case 204: 
/*  414 */       return FilteredPropertyType.LIST_OF_DOUBLE;
/*      */     case 205: 
/*  416 */       return FilteredPropertyType.LIST_OF_GUID;
/*      */     case 206: 
/*  418 */       return FilteredPropertyType.LIST_OF_LONG;
/*      */     case 207: 
/*  420 */       return FilteredPropertyType.LIST_OF_OBJECT;
/*      */     case 208: 
/*  422 */       return FilteredPropertyType.LIST_OF_STRING;
/*      */     case 101: 
/*  424 */       return FilteredPropertyType.SINGLETON_BINARY;
/*      */     case 102: 
/*  426 */       return FilteredPropertyType.SINGLETON_BOOLEAN;
/*      */     case 103: 
/*  428 */       return FilteredPropertyType.SINGLETON_DATE;
/*      */     case 104: 
/*  430 */       return FilteredPropertyType.SINGLETON_DOUBLE;
/*      */     case 105: 
/*  432 */       return FilteredPropertyType.SINGLETON_GUID;
/*      */     case 106: 
/*  434 */       return FilteredPropertyType.SINGLETON_LONG;
/*      */     case 107: 
/*  436 */       return FilteredPropertyType.SINGLETON_OBJECT;
/*      */     case 108: 
/*  438 */       return FilteredPropertyType.SINGLETON_STRING;
/*      */     }
/*  440 */     return null;
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
/*      */   static String getJaceObjectIdentity(EngineObject jaceEngineObject)
/*      */   {
/*  453 */     Tracer.traceMethodEntry(new Object[] { jaceEngineObject });
/*  454 */     String objIdent = null;
/*  455 */     if (jaceEngineObject != null)
/*      */     {
/*  457 */       if ((jaceEngineObject instanceof IndependentObject))
/*      */       {
/*  459 */         ObjectReference jaceObjRef = ((IndependentObject)jaceEngineObject).getObjectReference();
/*  460 */         if (jaceObjRef != null)
/*      */         {
/*  462 */           objIdent = jaceObjRef.getObjectIdentity();
/*      */         }
/*      */       }
/*      */       
/*  466 */       if (objIdent == null)
/*      */       {
/*  468 */         Properties jaceProps = jaceEngineObject.getProperties();
/*  469 */         if (jaceProps.isPropertyPresent("Id")) {
/*  470 */           objIdent = jaceProps.getStringValue("Id");
/*  471 */         } else if (jaceProps.isPropertyPresent("PathName")) {
/*  472 */           objIdent = jaceProps.getStringValue("PathName");
/*  473 */         } else if (jaceProps.isPropertyPresent("SymbolicName")) {
/*  474 */           objIdent = jaceProps.getStringValue("SymbolicName");
/*  475 */         } else if (jaceProps.isPropertyPresent("Name"))
/*  476 */           objIdent = jaceProps.getStringValue("Name");
/*      */       }
/*      */     }
/*  479 */     String result = objIdent != null ? objIdent : "";
/*      */     
/*  481 */     Tracer.traceMethodExit(new Object[] { result });
/*  482 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static String getJaceObjectClassName(EngineObject jaceEngineObject)
/*      */   {
/*  494 */     Tracer.traceMethodEntry(new Object[] { jaceEngineObject });
/*  495 */     String objClassIdent = null;
/*  496 */     if (jaceEngineObject != null)
/*      */     {
/*      */ 
/*  499 */       String tmpClassIdent = jaceEngineObject.getClassName();
/*  500 */       if (tmpClassIdent != null)
/*      */       {
/*  502 */         tmpClassIdent = tmpClassIdent.trim();
/*  503 */         if (tmpClassIdent.length() > 0)
/*      */         {
/*  505 */           objClassIdent = tmpClassIdent;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  510 */       if (objClassIdent == null)
/*      */       {
/*  512 */         if ((jaceEngineObject instanceof IndependentObject))
/*      */         {
/*  514 */           ObjectReference jaceObjRef = ((IndependentObject)jaceEngineObject).getObjectReference();
/*  515 */           if (jaceObjRef != null)
/*      */           {
/*  517 */             objClassIdent = jaceObjRef.getClassIdentity();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  523 */     Tracer.traceMethodExit(new Object[] { objClassIdent });
/*  524 */     return objClassIdent;
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
/*      */   static String createSpaceSeparatedString(String[] propNames)
/*      */   {
/*  538 */     Tracer.traceMethodEntry(new Object[] { propNames });
/*  539 */     StringBuilder sb = new StringBuilder("");
/*  540 */     String currentStr = null;
/*  541 */     boolean atLeastOne = false;
/*  542 */     for (String propName : propNames)
/*      */     {
/*  544 */       currentStr = propName.trim();
/*  545 */       if (currentStr.length() > 0)
/*      */       {
/*  547 */         if (atLeastOne) {
/*  548 */           sb.append(" ");
/*      */         }
/*  550 */         sb.append(currentStr);
/*  551 */         atLeastOne = true;
/*      */       }
/*      */     }
/*      */     
/*  555 */     String result = sb.toString();
/*  556 */     Tracer.traceMethodExit(new Object[] { result });
/*  557 */     return result;
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
/*      */   static String createSpaceSeparatedString(List<String> propNames)
/*      */   {
/*  572 */     Tracer.traceMethodEntry(new Object[] { propNames });
/*  573 */     StringBuilder sb = new StringBuilder("");
/*  574 */     String currentStr = null;
/*  575 */     boolean atLeastOne = false;
/*  576 */     for (String propName : propNames)
/*      */     {
/*  578 */       currentStr = propName.trim();
/*  579 */       if (currentStr.length() > 0)
/*      */       {
/*  581 */         if (atLeastOne) {
/*  582 */           sb.append(" ");
/*      */         }
/*  584 */         sb.append(currentStr);
/*  585 */         atLeastOne = true;
/*      */       }
/*      */     }
/*      */     
/*  589 */     String result = sb.toString();
/*  590 */     Tracer.traceMethodExit(new Object[] { result });
/*  591 */     return result;
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
/*      */   static String createCommaSeparatedString(List<?> values)
/*      */   {
/*  605 */     Tracer.traceMethodEntry(new Object[] { values });
/*  606 */     StringBuilder sb = new StringBuilder("");
/*  607 */     String currentStr = null;
/*  608 */     boolean atLeastOne = false;
/*  609 */     for (Object value : values)
/*      */     {
/*  611 */       currentStr = value.toString();
/*  612 */       if ((currentStr != null) && (currentStr.length() > 0))
/*      */       {
/*  614 */         if (atLeastOne) {
/*  615 */           sb.append(',');
/*      */         }
/*  617 */         sb.append(value);
/*  618 */         atLeastOne = true;
/*      */       }
/*      */     }
/*      */     
/*  622 */     String result = sb.toString();
/*  623 */     Tracer.traceMethodExit(new Object[] { result });
/*  624 */     return result;
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
/*      */   static String getJacePropertyAsString(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  641 */     String result = null;
/*      */     
/*  643 */     Object tmpObj = getJacePropertyAsObject(jaceBasable, symbolicName);
/*  644 */     result = tmpObj != null ? tmpObj.toString() : null;
/*      */     
/*  646 */     return result;
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
/*      */   static Boolean getJacePropertyAsBoolean(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  663 */     Boolean result = null;
/*      */     
/*  665 */     Object tmpObj = getJacePropertyAsObject(jaceBasable, symbolicName);
/*  666 */     if (tmpObj != null)
/*      */     {
/*  668 */       if ((tmpObj instanceof Boolean)) {
/*  669 */         result = (Boolean)tmpObj;
/*  670 */       } else if ((tmpObj instanceof String)) {
/*  671 */         result = Boolean.valueOf((String)tmpObj);
/*      */       } else {
/*  673 */         result = Boolean.valueOf(tmpObj.toString());
/*      */       }
/*      */     }
/*  676 */     return result;
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
/*      */   static StringList getJacePropertyAsStringList(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  692 */     StringList result = null;
/*      */     
/*  694 */     Object tmpObj = getJacePropertyAsObject(jaceBasable, symbolicName);
/*  695 */     if (tmpObj != null)
/*      */     {
/*  697 */       if ((tmpObj instanceof StringList)) {
/*  698 */         result = (StringList)tmpObj;
/*      */       }
/*      */     }
/*  701 */     return result;
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
/*      */   static Id getJacePropertyAsId(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  718 */     Id result = null;
/*      */     
/*  720 */     Object tmpObj = getJacePropertyAsObject(jaceBasable, symbolicName);
/*  721 */     if (tmpObj != null)
/*      */     {
/*  723 */       if ((tmpObj instanceof Id)) {
/*  724 */         result = (Id)tmpObj;
/*  725 */       } else if (((tmpObj instanceof String)) && (Id.isId((String)tmpObj))) {
/*  726 */         result = new Id((String)tmpObj);
/*      */       }
/*      */     }
/*  729 */     return result;
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
/*      */   static Integer getJacePropertyAsInteger(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  746 */     Integer result = null;
/*      */     
/*  748 */     Object tmpObj = getJacePropertyAsObject(jaceBasable, symbolicName);
/*  749 */     if (tmpObj != null)
/*      */     {
/*  751 */       if ((tmpObj instanceof Integer)) {
/*  752 */         result = (Integer)tmpObj;
/*  753 */       } else if ((tmpObj instanceof String)) {
/*  754 */         result = Integer.valueOf((String)tmpObj);
/*      */       } else {
/*  756 */         result = Integer.valueOf(tmpObj.toString());
/*      */       }
/*      */     }
/*  759 */     return result;
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
/*      */   static Date getJacePropertyAsDate(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  776 */     Date result = null;
/*      */     
/*      */     try
/*      */     {
/*  780 */       Object tmpObj = getJacePropertyAsObject(jaceBasable, symbolicName);
/*  781 */       if (tmpObj != null)
/*      */       {
/*  783 */         if ((tmpObj instanceof Date)) {
/*  784 */           result = (Date)tmpObj;
/*  785 */         } else if ((tmpObj instanceof String)) {
/*  786 */           result = new SimpleDateFormat().parse((String)tmpObj);
/*  787 */         } else if ((tmpObj instanceof Calendar)) {
/*  788 */           result = ((Calendar)tmpObj).getTime();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (ParseException ex) {
/*  793 */       Tracer.traceMinimumMsg("Unexpected Date parsing exception for property: {0}, exception: {1}", new Object[] { symbolicName, ex.getLocalizedMessage() });
/*      */     }
/*      */     
/*      */ 
/*  797 */     return result;
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
/*      */   static EngineObject getJacePropertyAsEngineObject(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  814 */     EngineObject result = null;
/*      */     
/*  816 */     EngineObject jaceBaseObj = jaceBasable.getJaceBaseObject();
/*  817 */     if (jaceBaseObj == null)
/*      */     {
/*  819 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { jaceBasable.getObjectIdentity(), jaceBasable.getEntityType() });
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  825 */       result = jaceBaseObj.getProperties().getEngineObjectValue(symbolicName);
/*      */     }
/*      */     catch (EngineRuntimeException jaceEx)
/*      */     {
/*  829 */       if (jaceEx.getExceptionCode() == ExceptionCode.API_PROPERTY_NOT_IN_CACHE)
/*      */       {
/*  831 */         throw processJaceException(jaceEx, RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*      */       }
/*      */       
/*      */ 
/*  835 */       throw processJaceException(jaceEx, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { jaceEx.getLocalizedMessage() });
/*      */     }
/*      */     
/*      */ 
/*  839 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static Object getJacePropertyAsObject(JaceBasable jaceBasable, String symbolicName)
/*      */   {
/*  846 */     Object result = null;
/*      */     
/*  848 */     EngineObject jaceBaseObj = jaceBasable.getJaceBaseObject();
/*  849 */     if (jaceBaseObj == null)
/*      */     {
/*  851 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { jaceBasable.getObjectIdentity(), jaceBasable.getEntityType() });
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  857 */       result = jaceBaseObj.getProperties().getObjectValue(symbolicName);
/*      */     }
/*      */     catch (EngineRuntimeException jaceEx)
/*      */     {
/*  861 */       if (jaceEx.getExceptionCode() == ExceptionCode.API_PROPERTY_NOT_IN_CACHE)
/*      */       {
/*  863 */         throw processJaceException(jaceEx, RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*      */       }
/*      */       
/*      */ 
/*  867 */       throw processJaceException(jaceEx, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { jaceEx.getLocalizedMessage() });
/*      */     }
/*      */     
/*      */ 
/*  871 */     return result;
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
/*      */   static PropertyDefinition getPropertyDefinition(ClassDefinition jaceDocClassDef, String propSymbolicName)
/*      */   {
/*  886 */     Tracer.traceMethodEntry(new Object[] { jaceDocClassDef, propSymbolicName });
/*  887 */     PropertyDefinition result = null;
/*      */     
/*  889 */     if ((jaceDocClassDef != null) && (propSymbolicName != null))
/*      */     {
/*  891 */       if (jaceDocClassDef.getProperties().isPropertyPresent("PropertyDefinitions"))
/*      */       {
/*  893 */         PropertyDefinitionList propDefs = jaceDocClassDef.get_PropertyDefinitions();
/*  894 */         Iterator<PropertyDefinition> it = propDefs.iterator();
/*  895 */         PropertyDefinition propDef = null;
/*  896 */         while (it.hasNext())
/*      */         {
/*  898 */           propDef = (PropertyDefinition)it.next();
/*  899 */           if ((propDef != null) && (propSymbolicName.equalsIgnoreCase(propDef.get_SymbolicName())))
/*      */           {
/*  901 */             result = propDef;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  908 */     Tracer.traceMethodExit(new Object[] { result });
/*  909 */     return result;
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
/*      */   static void fetchAdditionalJaceProperties(IndependentObject jaceObj, PropertyFilter pf)
/*      */   {
/*  923 */     Tracer.traceMethodEntry(new Object[] { jaceObj, pf });
/*  924 */     Util.ckNullObjParam("pf", pf);
/*  925 */     int fetchAttemptCount = 0;
/*      */     for (;;)
/*      */     {
/*      */       try
/*      */       {
/*  930 */         fetchAttemptCount++;
/*      */         
/*  932 */         long startTime = System.currentTimeMillis();
/*  933 */         jaceObj.fetchProperties(pf);
/*  934 */         Tracer.traceExtCall("IndependentObject.fetchProperties", startTime, System.currentTimeMillis(), null, null, new Object[] { pf });
/*      */         
/*      */ 
/*  937 */         Tracer.traceMethodExit(new Object[0]);
/*  938 */         return;
/*      */       }
/*      */       catch (EngineRuntimeException ex)
/*      */       {
/*  942 */         if ((ex.getExceptionCode() != ExceptionCode.API_FETCH_MERGE_PROPERTY_ERROR) || (fetchAttemptCount >= 2))
/*      */         {
/*      */ 
/*  945 */           throw ex;
/*      */         }
/*      */         
/*  948 */         long startTime = System.currentTimeMillis();
/*  949 */         jaceObj.refresh();
/*  950 */         Tracer.traceExtCall("IndependentObject.refresh", startTime, System.currentTimeMillis(), null, null, new Object[0]);
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
/*      */   static ClassDefinition fetchJaceClassDefinition(ObjectStore jaceObjStore, String classSymbolicName, PropertyFilter pf)
/*      */   {
/*  968 */     Tracer.traceMethodEntry(new Object[] { jaceObjStore, classSymbolicName, pf });
/*  969 */     ClassDefinition result = null;
/*      */     
/*      */     try
/*      */     {
/*  973 */       long startTime = System.currentTimeMillis();
/*  974 */       result = Factory.ClassDefinition.fetchInstance(jaceObjStore, classSymbolicName, pf);
/*  975 */       Tracer.traceExtCall("Factory.ClassDefinition.fetchInstance", startTime, System.currentTimeMillis(), Integer.valueOf(1), result, new Object[] { jaceObjStore, classSymbolicName, pf });
/*      */ 
/*      */     }
/*      */     catch (EngineRuntimeException jaceEx)
/*      */     {
/*      */ 
/*  981 */       if (jaceEx.getExceptionCode() != ExceptionCode.E_OBJECT_NOT_FOUND) {
/*  982 */         throw processJaceException(jaceEx, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { jaceEx.getLocalizedMessage() });
/*      */       }
/*      */     }
/*  985 */     Tracer.traceMethodExit(new Object[] { result });
/*  986 */     return result;
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
/*      */   static PropertyFilter buildMandatoryJacePF(JaceBasable jaceBasable)
/*      */   {
/* 1000 */     Tracer.traceMethodEntry(new Object[] { jaceBasable });
/* 1001 */     List<FilterElement> mandatoryFEs = jaceBasable.getMandatoryFEs();
/* 1002 */     PropertyFilter pf = buildMandatoryJacePF(mandatoryFEs);
/* 1003 */     Tracer.traceMethodExit(new Object[] { pf });
/* 1004 */     return pf;
/*      */   }
/*      */   
/*      */   static PropertyFilter buildMandatoryJacePF(List<FilterElement> mandatoryFEs)
/*      */   {
/* 1009 */     Tracer.traceMethodEntry(new Object[] { mandatoryFEs });
/* 1010 */     PropertyFilter pf = null;
/* 1011 */     if (mandatoryFEs != null)
/*      */     {
/* 1013 */       pf = new PropertyFilter();
/* 1014 */       for (FilterElement fe : mandatoryFEs)
/*      */       {
/* 1016 */         pf.addIncludeProperty(fe);
/*      */       }
/*      */     }
/*      */     
/* 1020 */     Tracer.traceMethodExit(new Object[] { pf });
/* 1021 */     return pf;
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
/*      */   public static RMErrorCode determineErrorCode(RMErrorCode defaultErrorCode, Throwable cause)
/*      */   {
/* 1035 */     Tracer.traceMethodEntry(new Object[] { defaultErrorCode, cause });
/* 1036 */     RMErrorCode result = defaultErrorCode;
/* 1037 */     if ((cause != null) && ((cause instanceof EngineRuntimeException)))
/*      */     {
/* 1039 */       ExceptionCode jaceExceptionCode = ((EngineRuntimeException)cause).getExceptionCode();
/* 1040 */       if (ExceptionCode.E_OBJECT_NOT_FOUND.equals(jaceExceptionCode)) {
/* 1041 */         result = RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE;
/*      */       }
/*      */     }
/* 1044 */     Tracer.traceMethodExit(new Object[] { result });
/* 1045 */     return result;
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
/*      */   public static RMRuntimeException processJaceException(Throwable cause, RMErrorCode code, Object... params)
/*      */   {
/* 1064 */     RMRuntimeException result = null;
/* 1065 */     RMErrorStack rmErrorStack = null;
/* 1066 */     if ((cause != null) && ((cause instanceof EngineRuntimeException)))
/*      */     {
/* 1068 */       EngineRuntimeException ere = (EngineRuntimeException)cause;
/* 1069 */       ErrorStack jaceErrorStack = ((EngineRuntimeException)cause).getAsErrorStack();
/* 1070 */       if (jaceErrorStack != null) {
/* 1071 */         rmErrorStack = new P8CE_RMErrorStack(jaceErrorStack);
/*      */       }
/* 1073 */       if (code == RMErrorCode.E_UNEXPECTED_EXCEPTION)
/*      */       {
/*      */ 
/*      */ 
/* 1077 */         String jaceErrMsg = ere.getLocalizedMessage();
/* 1078 */         ExceptionCode jaceEC = ere.getExceptionCode();
/* 1079 */         String jaceECKey = jaceEC != null ? jaceEC.getKey() : null;
/* 1080 */         result = RMRuntimeException.createRMRuntimeException(cause, RMErrorCode.RAL_UNEXPECTED_CE_SERVER_EXCEPTION, rmErrorStack, new Object[] { jaceECKey, jaceErrMsg });
/*      */       }
/*      */     }
/*      */     
/* 1084 */     if (result == null)
/*      */     {
/* 1086 */       if ((code == RMErrorCode.RAL_SAVE_OPERATION_FAILED) && (cause != null))
/*      */       {
/* 1088 */         Object[] newParams = null;
/* 1089 */         if (params != null)
/*      */         {
/* 1091 */           newParams = new Object[params.length + 1];
/* 1092 */           System.arraycopy(params, 0, newParams, 0, params.length);
/* 1093 */           newParams[(newParams.length - 1)] = cause.getLocalizedMessage();
/*      */         }
/*      */         else
/*      */         {
/* 1097 */           newParams = new Object[] { cause.getLocalizedMessage() };
/*      */         }
/* 1099 */         result = RMRuntimeException.createRMRuntimeException(cause, code, rmErrorStack, newParams);
/*      */       }
/*      */       else
/*      */       {
/* 1103 */         result = RMRuntimeException.createRMRuntimeException(cause, code, rmErrorStack, params);
/*      */       }
/*      */     }
/*      */     
/* 1107 */     return result;
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
/*      */   static EntityType[] convertFromInteger32List(Integer32List jaceIntList)
/*      */   {
/* 1120 */     Tracer.traceMethodEntry(new Object[] { jaceIntList });
/* 1121 */     EntityType[] result = new EntityType[0];
/* 1122 */     if ((jaceIntList != null) && (!jaceIntList.isEmpty()))
/*      */     {
/* 1124 */       List<EntityType> etList = new ArrayList();
/* 1125 */       Integer intEntityValue = null;
/* 1126 */       Iterator<Integer> it = jaceIntList.iterator();
/* 1127 */       while ((it != null) && (it.hasNext()))
/*      */       {
/* 1129 */         intEntityValue = (Integer)it.next();
/* 1130 */         EntityType entityType = EntityType.getInstanceFromInt(intEntityValue.intValue());
/* 1131 */         if (entityType != null) {
/* 1132 */           etList.add(entityType);
/*      */         }
/*      */       }
/* 1135 */       result = (EntityType[])etList.toArray(new EntityType[etList.size()]);
/*      */     }
/*      */     
/* 1138 */     Tracer.traceMethodExit((Object[])result);
/* 1139 */     return result;
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
/*      */   static Integer32List convertToInteger32List(List<Integer> intList)
/*      */   {
/* 1152 */     Tracer.traceMethodEntry(new Object[] { intList });
/* 1153 */     Integer32List result = null;
/* 1154 */     if (intList != null)
/*      */     {
/* 1156 */       result = Factory.Integer32List.createList();
/* 1157 */       for (Integer intValue : intList) {
/* 1158 */         result.add(intValue);
/*      */       }
/*      */     }
/* 1161 */     Tracer.traceMethodExit(new Object[] { result });
/* 1162 */     return result;
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
/*      */   static EntityType[] convertFromCommaSepStringList(String commaSepString)
/*      */   {
/* 1175 */     Tracer.traceMethodEntry(new Object[] { commaSepString });
/* 1176 */     EntityType[] result = new EntityType[0];
/* 1177 */     String trimmedStr = null;
/*      */     
/* 1179 */     if ((commaSepString != null) && ((trimmedStr = commaSepString.trim()).length() > 0))
/*      */     {
/* 1181 */       String[] strIntValues = trimmedStr.split(",");
/* 1182 */       List<EntityType> etList = new ArrayList(strIntValues.length);
/* 1183 */       for (String strIntValue : strIntValues)
/*      */       {
/* 1185 */         int intEntityValue = Integer.parseInt(strIntValue, 10);
/* 1186 */         EntityType entityType = EntityType.getInstanceFromInt(intEntityValue);
/* 1187 */         if (entityType != null) {
/* 1188 */           etList.add(entityType);
/*      */         }
/*      */       }
/* 1191 */       result = (EntityType[])etList.toArray(new EntityType[etList.size()]);
/*      */     }
/*      */     
/* 1194 */     Tracer.traceMethodExit((Object[])result);
/* 1195 */     return result;
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
/*      */   static RefreshMode convertToJaceRefreshMode(RMRefreshMode jarmMode)
/*      */   {
/* 1208 */     return jarmMode == RMRefreshMode.Refresh ? RefreshMode.REFRESH : RefreshMode.NO_REFRESH;
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
/*      */   static void convertToJaceProperties(RMProperties jarmProps, Properties jaceProps)
/*      */   {
/* 1221 */     Tracer.traceMethodEntry(new Object[] { jarmProps, jaceProps });
/* 1222 */     P8CE_RMPropertyImpl jarmProp = null;
/* 1223 */     for (Iterator it = jarmProps.iterator(); it.hasNext();)
/*      */     {
/* 1225 */       jarmProp = (P8CE_RMPropertyImpl)it.next();
/* 1226 */       if ((jarmProp != null) && (jarmProp.getJaceProperty() != null))
/* 1227 */         ((PropertiesImpl)jaceProps).put(jarmProp.getJaceProperty());
/*      */     }
/* 1229 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   static PrincipalSearchType convertToJacePrinSearchType(RMPrincipalSearchType searchType)
/*      */   {
/* 1242 */     Tracer.traceMethodEntry(new Object[] { searchType });
/* 1243 */     PrincipalSearchType result = null;
/*      */     
/* 1245 */     switch (searchType)
/*      */     {
/*      */     case Contains: 
/* 1248 */       result = PrincipalSearchType.CONTAINS;
/* 1249 */       break;
/*      */     case Custom: 
/* 1251 */       result = PrincipalSearchType.CONTAINS;
/* 1252 */       break;
/*      */     case Exact: 
/* 1254 */       result = PrincipalSearchType.EXACT;
/* 1255 */       break;
/*      */     case None: 
/* 1257 */       result = PrincipalSearchType.NONE;
/* 1258 */       break;
/*      */     case PrefixMatch: 
/* 1260 */       result = PrincipalSearchType.PREFIX_MATCH;
/* 1261 */       break;
/*      */     case SuffixMatch: 
/* 1263 */       result = PrincipalSearchType.SUFFIX_MATCH;
/*      */     }
/*      */     
/*      */     
/* 1267 */     Tracer.traceMethodExit(new Object[] { result });
/* 1268 */     return result;
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
/*      */   static PrincipalSearchAttribute convertToJacePrinSearchAttr(RMPrincipalSearchAttribute searchAttr)
/*      */   {
/* 1281 */     Tracer.traceMethodEntry(new Object[] { searchAttr });
/* 1282 */     PrincipalSearchAttribute result = null;
/* 1283 */     if (searchAttr == RMPrincipalSearchAttribute.DisplayName) {
/* 1284 */       result = PrincipalSearchAttribute.DISPLAY_NAME;
/* 1285 */     } else if (searchAttr == RMPrincipalSearchAttribute.ShortName) {
/* 1286 */       result = PrincipalSearchAttribute.SHORT_NAME;
/* 1287 */     } else if (searchAttr == RMPrincipalSearchAttribute.None) {
/* 1288 */       result = PrincipalSearchAttribute.NONE;
/*      */     }
/* 1290 */     Tracer.traceMethodExit(new Object[] { result });
/* 1291 */     return result;
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
/*      */   static PrincipalSearchSortType convertToJacePrinSortType(RMPrincipalSearchSortType sortType)
/*      */   {
/* 1304 */     Tracer.traceMethodEntry(new Object[] { sortType });
/* 1305 */     PrincipalSearchSortType result = null;
/* 1306 */     if (sortType == RMPrincipalSearchSortType.Ascending) {
/* 1307 */       result = PrincipalSearchSortType.ASCENDING;
/* 1308 */     } else if (sortType == RMPrincipalSearchSortType.Descending) {
/* 1309 */       result = PrincipalSearchSortType.DESCENDING;
/* 1310 */     } else if (sortType == RMPrincipalSearchSortType.None) {
/* 1311 */       result = PrincipalSearchSortType.NONE;
/*      */     }
/* 1313 */     Tracer.traceMethodExit(new Object[] { result });
/* 1314 */     return result;
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
/*      */   static StringBuilder createSelectSqlFromJacePF(PropertyFilter jacePF, Integer topCount, String tableName, String tblAlias)
/*      */   {
/* 1332 */     Tracer.traceMethodEntry(new Object[] { jacePF, topCount, tableName, tblAlias });
/*      */     
/* 1334 */     StringBuilder sb = new StringBuilder();
/* 1335 */     boolean firstPropName; if (jacePF != null)
/*      */     {
/* 1337 */       FilterElement[] jaceFEs = jacePF.getIncludeProperties();
/*      */       
/* 1339 */       List<String> allPropNameList = new ArrayList();
/* 1340 */       for (FilterElement jaceFE : jaceFEs)
/*      */       {
/* 1342 */         allPropNameList.addAll(extractPropNamesFromJaceFE(jaceFE));
/*      */       }
/*      */       
/* 1345 */       firstPropName = true;
/* 1346 */       sb.append("SELECT ");
/* 1347 */       if (topCount != null)
/* 1348 */         sb.append("TOP ").append(topCount).append(' ');
/* 1349 */       for (String propName : allPropNameList)
/*      */       {
/* 1351 */         if (!firstPropName) {
/* 1352 */           sb.append(", ");
/*      */         }
/* 1354 */         firstPropName = false;
/* 1355 */         sb.append(tblAlias).append(".[").append(propName).append("]");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1360 */       sb.append("SELECT ");
/* 1361 */       if (topCount != null) {
/* 1362 */         sb.append("TOP ").append(topCount).append(' ');
/*      */       }
/* 1364 */       sb.append(tblAlias).append(".*");
/*      */     }
/*      */     
/* 1367 */     sb.append(" FROM ").append("[").append(tableName).append("] ").append(tblAlias).append(' ');
/*      */     
/* 1369 */     Tracer.traceMethodExit(new Object[] { sb });
/* 1370 */     return sb;
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
/*      */   static List<String> extractPropNamesFromJaceFE(FilterElement jaceFE)
/*      */   {
/* 1383 */     Tracer.traceMethodEntry(new Object[] { jaceFE });
/* 1384 */     String spaceSeparatedList = jaceFE.getValue();
/* 1385 */     String[] propNames = spaceSeparatedList.split(" ");
/* 1386 */     String trimmedName = null;
/* 1387 */     List<String> result = new ArrayList(propNames.length);
/* 1388 */     for (String propName : propNames)
/*      */     {
/* 1390 */       if (propName != null)
/*      */       {
/* 1392 */         trimmedName = propName.trim();
/* 1393 */         if (trimmedName.length() > 0)
/*      */         {
/* 1395 */           if (!isAJaceFilteredPropertyType(trimmedName)) {
/* 1396 */             result.add(trimmedName);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1401 */     Tracer.traceMethodExit(new Object[] { result });
/* 1402 */     return result;
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
/*      */   static boolean isAJaceFilteredPropertyType(String value)
/*      */   {
/* 1416 */     Tracer.traceMethodEntry(new Object[] { value });
/* 1417 */     boolean result = false;
/*      */     
/* 1419 */     if ((value != null) && (value.trim().length() > 0))
/*      */     {
/* 1421 */       String valueUC = value.toUpperCase();
/* 1422 */       if ((valueUC.startsWith("ANY")) || (valueUC.startsWith("SINGLETON")) || (valueUC.startsWith("ENUM_OF")) || (valueUC.startsWith("LIST_OF")) || (valueUC.startsWith("CONTENT_DATA")))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1428 */         result = true;
/*      */       }
/*      */     }
/*      */     
/* 1432 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1433 */     return result;
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
/*      */   static boolean isJacePropertyPresent(JaceBasable jaceBasable, String symName)
/*      */   {
/* 1448 */     boolean result = false;
/* 1449 */     if ((symName != null) && (jaceBasable != null) && (jaceBasable.getJaceBaseObject() != null))
/*      */     {
/* 1451 */       result = jaceBasable.getJaceBaseObject().getProperties().isPropertyPresent(symName);
/*      */     }
/*      */     
/* 1454 */     return result;
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
/*      */   static IGenerator<?> getEntityGenerator(IndependentObject jaceIndepObj)
/*      */   {
/* 1468 */     Tracer.traceMethodEntry(new Object[] { jaceIndepObj });
/* 1469 */     IGenerator<?> result = null;
/*      */     
/* 1471 */     if (!jaceIndepObj.getProperties().isPropertyPresent("RMEntityType"))
/*      */     {
/* 1473 */       if (!isCreationPending(jaceIndepObj))
/*      */       {
/* 1475 */         PropertyFilter additionalPF = new PropertyFilter();
/* 1476 */         additionalPF.addIncludeProperty(0, null, null, "RMEntityType", null);
/* 1477 */         fetchAdditionalJaceProperties(jaceIndepObj, additionalPF);
/*      */       }
/*      */     }
/*      */     
/* 1481 */     EntityType entityType = null;
/* 1482 */     if (jaceIndepObj.getProperties().isPropertyPresent("RMEntityType"))
/*      */     {
/* 1484 */       Integer entityTypeInt = jaceIndepObj.getProperties().getInteger32Value("RMEntityType");
/* 1485 */       if (entityTypeInt != null)
/*      */       {
/* 1487 */         entityType = EntityType.getInstanceFromInt(entityTypeInt.intValue());
/* 1488 */         if (entityType == EntityType.FilePlan)
/*      */         {
/*      */ 
/* 1491 */           if (!jaceIndepObj.getProperties().isPropertyPresent("ContainerType"))
/*      */           {
/* 1493 */             jaceIndepObj.fetchProperties(new String[] { "ContainerType" });
/*      */           }
/* 1495 */           String containerType = jaceIndepObj.getProperties().getStringValue("ContainerType");
/* 1496 */           if ("application/x-filenet-rm-classificationschemeroot".equalsIgnoreCase(containerType))
/*      */           {
/* 1498 */             entityType = null;
/*      */           }
/*      */           else
/*      */           {
/* 1502 */             result = getEntityGenerator(entityType);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1507 */           result = getEntityGenerator(entityType);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1512 */     if (entityType == null)
/*      */     {
/* 1514 */       if ((jaceIndepObj instanceof CustomObject))
/*      */       {
/* 1516 */         result = getEntityGenerator(EntityType.CustomObject);
/*      */       }
/* 1518 */       else if ((jaceIndepObj instanceof Folder))
/*      */       {
/* 1520 */         result = getEntityGenerator(EntityType.Container);
/*      */       }
/* 1522 */       else if ((jaceIndepObj instanceof Document))
/*      */       {
/* 1524 */         result = getEntityGenerator(EntityType.ContentItem);
/*      */       }
/* 1526 */       else if ((jaceIndepObj instanceof ObjectStore))
/*      */       {
/* 1528 */         result = getEntityGenerator(EntityType.Repository);
/*      */       }
/* 1530 */       else if ((jaceIndepObj instanceof Domain))
/*      */       {
/* 1532 */         result = getEntityGenerator(EntityType.Domain);
/*      */       }
/* 1534 */       else if ((jaceIndepObj instanceof Link))
/*      */       {
/* 1536 */         result = getEntityGenerator(EntityType.RMLink);
/*      */       }
/* 1538 */       else if ((jaceIndepObj instanceof ChoiceList))
/*      */       {
/* 1540 */         result = getEntityGenerator(EntityType.ChoiceList);
/*      */       }
/* 1542 */       else if ((jaceIndepObj instanceof MarkingSet))
/*      */       {
/* 1544 */         result = getEntityGenerator(EntityType.MarkingSet);
/*      */       }
/* 1546 */       else if ((jaceIndepObj instanceof ClassDescription))
/*      */       {
/* 1548 */         result = P8CE_RMClassDescriptionImpl.getGenerator();
/*      */       }
/* 1550 */       else if ((jaceIndepObj instanceof Event))
/*      */       {
/* 1552 */         result = P8CE_AuditEventImpl.getAuditEventGenerator();
/*      */       }
/*      */       else
/*      */       {
/* 1556 */         result = getEntityGenerator(EntityType.Unknown);
/*      */       }
/*      */     }
/*      */     
/* 1560 */     Tracer.traceMethodExit(new Object[] { result });
/* 1561 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean isCreationPending(IndependentObject jaceIndepObj)
/*      */   {
/* 1573 */     Tracer.traceMethodEntry(new Object[] { jaceIndepObj });
/* 1574 */     boolean result = false;
/*      */     
/*      */ 
/*      */ 
/* 1578 */     if ((jaceIndepObj instanceof IndependentlyPersistableObject))
/*      */     {
/* 1580 */       PendingAction[] jacePendingActions = ((IndependentlyPersistableObject)jaceIndepObj).getPendingActions();
/* 1581 */       if (jacePendingActions != null)
/*      */       {
/* 1583 */         for (PendingAction action : jacePendingActions)
/*      */         {
/* 1585 */           if ((action instanceof Create))
/*      */           {
/* 1587 */             result = true;
/* 1588 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1594 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 1595 */     return result;
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
/*      */   static IGenerator<? extends BaseEntity> getEntityGenerator(EntityType entityType)
/*      */   {
/* 1608 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 1609 */     IGenerator<? extends BaseEntity> result = null;
/*      */     
/* 1611 */     switch (entityType)
/*      */     {
/*      */     case FilePlan: 
/* 1614 */       result = P8CE_FilePlanImpl.getGenerator();
/* 1615 */       break;
/*      */     
/*      */     case RecordCategory: 
/* 1618 */       result = P8CE_RecordCategoryImpl.getGenerator();
/* 1619 */       break;
/*      */     
/*      */     case RecordFolder: 
/*      */     case ElectronicRecordFolder: 
/*      */     case PhysicalContainer: 
/*      */     case HybridRecordFolder: 
/*      */     case PhysicalRecordFolder: 
/* 1626 */       result = P8CE_RecordFolderImpl.getGenerator();
/* 1627 */       break;
/*      */     
/*      */     case RecordVolume: 
/* 1630 */       result = P8CE_RecordVolumeImpl.getGenerator();
/* 1631 */       break;
/*      */     
/*      */     case SystemConfiguration: 
/* 1634 */       result = P8CE_SystemConfigurationImpl.getGenerator();
/* 1635 */       break;
/*      */     
/*      */     case Location: 
/* 1638 */       result = P8CE_LocationImpl.getGenerator();
/* 1639 */       break;
/*      */     
/*      */     case DispositionAction: 
/* 1642 */       result = P8CE_DispositionActionImpl.getGenerator();
/* 1643 */       break;
/*      */     
/*      */     case DispositionSchedule: 
/* 1646 */       result = P8CE_DispositionScheduleImpl.getGenerator();
/* 1647 */       break;
/*      */     
/*      */     case DispositionTrigger: 
/* 1650 */       result = P8CE_DispositionTriggerImpl.getGenerator();
/* 1651 */       break;
/*      */     
/*      */     case Phase: 
/* 1654 */       result = P8CE_DispositionPhaseImpl.getGenerator();
/* 1655 */       break;
/*      */     
/*      */     case RecordType: 
/* 1658 */       result = P8CE_RecordTypeImpl.getGenerator();
/* 1659 */       break;
/*      */     
/*      */     case AlternateRetention: 
/* 1662 */       result = P8CE_AlternateRetentionImpl.getGenerator();
/* 1663 */       break;
/*      */     
/*      */     case Hold: 
/* 1666 */       result = P8CE_HoldImpl.getGenerator();
/* 1667 */       break;
/*      */     
/*      */     case Pattern: 
/* 1670 */       result = P8CE_NamingPatternImpl.getGenerator();
/* 1671 */       break;
/*      */     
/*      */     case PatternSequence: 
/* 1674 */       result = P8CE_NamingPatternSequenceImpl.getGenerator();
/* 1675 */       break;
/*      */     
/*      */     case PatternLevel: 
/* 1678 */       result = P8CE_NamingPatternLevelImpl.getGenerator();
/* 1679 */       break;
/*      */     
/*      */     case ReportDefinition: 
/* 1682 */       result = P8CE_ReportDefinitionImpl.getGenerator();
/* 1683 */       break;
/*      */     
/*      */     case CustomObject: 
/*      */     case DisposalPhase: 
/*      */     case Reservation: 
/*      */     case PhaseException: 
/*      */     case ConnectorRegistration: 
/*      */     case RMLog: 
/*      */     case RMSystem: 
/*      */     case AuditConfig: 
/*      */     case AccessRole: 
/* 1694 */       result = P8CE_RMCustomObjectImpl.getCustomObjectGenerator();
/* 1695 */       break;
/*      */     
/*      */     case Record: 
/*      */     case ElectronicRecord: 
/*      */     case EmailRecord: 
/*      */     case PhysicalRecord: 
/*      */     case PDFRecord: 
/* 1702 */       result = P8CE_RecordImpl.getGenerator();
/* 1703 */       break;
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
/* 1715 */       result = P8CE_RMLinkImpl.getRMLinkGenerator();
/* 1716 */       break;
/*      */     
/*      */     case ContentRepository: 
/* 1719 */       result = P8CE_ContentRepositoryImpl.getContentRepositoryGenerator();
/* 1720 */       break;
/*      */     
/*      */     case FilePlanRepository: 
/* 1723 */       result = P8CE_FilePlanRepositoryImpl.getFilePlanRepositoryGenerator();
/* 1724 */       break;
/*      */     
/*      */     case Repository: 
/* 1727 */       result = P8CE_RepositoryImpl.getGenerator();
/* 1728 */       break;
/*      */     
/*      */     case Domain: 
/* 1731 */       result = P8CE_RMDomainImpl.getGenerator();
/* 1732 */       break;
/*      */     
/*      */     case AuditEvent: 
/* 1735 */       result = P8CE_AuditEventImpl.getAuditEventGenerator();
/* 1736 */       break;
/*      */     
/*      */     case ClassificationGuide: 
/*      */     case ClassificationGuideSection: 
/*      */     case Container: 
/*      */     case RMFolder: 
/* 1742 */       result = P8CE_BaseContainerImpl.getBaseContainerGenerator();
/* 1743 */       break;
/*      */     
/*      */     case ContentItem: 
/*      */     case Transcript: 
/*      */     case ClassificationGuideTopic: 
/*      */     case TransferMapping: 
/*      */     case WorkflowDefinition: 
/* 1750 */       result = P8CE_ContentItemImpl.getContentItemGenerator();
/* 1751 */       break;
/*      */     
/*      */     case ChoiceList: 
/* 1754 */       result = P8CE_RMChoiceListImpl.getGenerator();
/* 1755 */       break;
/*      */     
/*      */     case MarkingSet: 
/* 1758 */       result = P8CE_RMMarkingSetImpl.getGenerator();
/* 1759 */       break;
/*      */     
/*      */     case Unknown: 
/*      */     default: 
/* 1763 */       result = P8CE_BaseEntityImpl.getBaseEntityGenerator();
/*      */     }
/*      */     
/*      */     
/* 1767 */     Tracer.traceMethodExit(new Object[] { result });
/* 1768 */     return result;
/*      */   }
/*      */   
/*      */   static List<FilterElement> getEntityTypeMandatoryFEs(EntityType entityType)
/*      */   {
/* 1773 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 1774 */     List<FilterElement> result = null;
/* 1775 */     switch (entityType)
/*      */     {
/*      */     case FilePlan: 
/* 1778 */       result = P8CE_FilePlanImpl.getMandatoryJaceFEs();
/* 1779 */       break;
/*      */     
/*      */     case RecordCategory: 
/* 1782 */       result = P8CE_RecordCategoryImpl.getMandatoryJaceFEs();
/* 1783 */       break;
/*      */     
/*      */     case RecordFolder: 
/*      */     case ElectronicRecordFolder: 
/*      */     case PhysicalContainer: 
/*      */     case HybridRecordFolder: 
/*      */     case PhysicalRecordFolder: 
/* 1790 */       result = P8CE_RecordFolderImpl.getMandatoryJaceFEs();
/* 1791 */       break;
/*      */     
/*      */     case RecordVolume: 
/* 1794 */       result = P8CE_RecordVolumeImpl.getMandatoryJaceFEs();
/* 1795 */       break;
/*      */     
/*      */     case SystemConfiguration: 
/* 1798 */       result = P8CE_SystemConfigurationImpl.getMandatoryJaceFEs();
/* 1799 */       break;
/*      */     
/*      */     case Pattern: 
/* 1802 */       result = P8CE_NamingPatternImpl.getMandatoryJaceFEs();
/* 1803 */       break;
/*      */     
/*      */     case PatternSequence: 
/* 1806 */       result = P8CE_NamingPatternSequenceImpl.getMandatoryJaceFEs();
/* 1807 */       break;
/*      */     
/*      */     case DispositionAction: 
/* 1810 */       result = P8CE_DispositionActionImpl.getMandatoryJaceFEs();
/* 1811 */       break;
/*      */     
/*      */     case DispositionSchedule: 
/* 1814 */       result = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 1815 */       break;
/*      */     
/*      */     case Phase: 
/* 1818 */       result = P8CE_DispositionPhaseImpl.getMandatoryJaceFEs();
/* 1819 */       break;
/*      */     
/*      */     case DispositionTrigger: 
/* 1822 */       result = P8CE_DispositionTriggerImpl.getMandatoryJaceFEs();
/* 1823 */       break;
/*      */     
/*      */     case RecordType: 
/* 1826 */       result = P8CE_RecordTypeImpl.getMandatoryJaceFEs();
/* 1827 */       break;
/*      */     
/*      */     case Hold: 
/* 1830 */       result = P8CE_HoldImpl.getMandatoryJaceFEs();
/* 1831 */       break;
/*      */     
/*      */     case PatternLevel: 
/* 1834 */       result = P8CE_NamingPatternLevelImpl.getMandatoryJaceFEs();
/* 1835 */       break;
/*      */     
/*      */     case AlternateRetention: 
/* 1838 */       result = P8CE_AlternateRetentionImpl.getMandatoryJaceFEs();
/* 1839 */       break;
/*      */     
/*      */     case ReportDefinition: 
/* 1842 */       result = P8CE_ReportDefinitionImpl.getMandatoryJaceFEs();
/* 1843 */       break;
/*      */     
/*      */     case CustomObject: 
/*      */     case Reservation: 
/*      */     case PhaseException: 
/*      */     case ConnectorRegistration: 
/*      */     case RMSystem: 
/*      */     case AuditConfig: 
/*      */     case AccessRole: 
/* 1852 */       result = P8CE_RMCustomObjectImpl.getMandatoryJaceFEs();
/* 1853 */       break;
/*      */     
/*      */     case Location: 
/* 1856 */       result = P8CE_LocationImpl.getMandatoryJaceFEs();
/* 1857 */       break;
/*      */     
/*      */     case Record: 
/*      */     case ElectronicRecord: 
/*      */     case EmailRecord: 
/*      */     case PhysicalRecord: 
/*      */     case PDFRecord: 
/* 1864 */       result = P8CE_RecordImpl.getMandatoryJaceFEs();
/* 1865 */       break;
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
/* 1877 */       result = P8CE_RMLinkImpl.getMandatoryJaceFEs();
/* 1878 */       break;
/*      */     
/*      */     case ContentRepository: 
/* 1881 */       result = P8CE_ContentRepositoryImpl.getMandatoryJaceFEs();
/* 1882 */       break;
/*      */     
/*      */     case FilePlanRepository: 
/* 1885 */       result = P8CE_FilePlanRepositoryImpl.getMandatoryJaceFEs();
/* 1886 */       break;
/*      */     
/*      */     case Repository: 
/* 1889 */       result = P8CE_RepositoryImpl.getMandatoryJaceFEs();
/* 1890 */       break;
/*      */     
/*      */     case Domain: 
/* 1893 */       result = P8CE_RMDomainImpl.getMandatoryJaceFEs();
/* 1894 */       break;
/*      */     
/*      */     case AuditEvent: 
/* 1897 */       result = P8CE_AuditEventImpl.getMandatoryJaceFEs();
/* 1898 */       break;
/*      */     
/*      */     case ClassificationGuide: 
/* 1901 */       result = P8CE_ClassificationGuideImpl.getMandatoryJaceFEs();
/* 1902 */       break;
/*      */     
/*      */     case ClassificationGuideSection: 
/*      */     case Container: 
/*      */     case RMFolder: 
/* 1907 */       result = P8CE_BaseContainerImpl.getMandatoryJaceFEs();
/* 1908 */       break;
/*      */     
/*      */     case ContentItem: 
/*      */     case Transcript: 
/*      */     case ClassificationGuideTopic: 
/*      */     case TransferMapping: 
/*      */     case WorkflowDefinition: 
/* 1915 */       result = P8CE_ContentItemImpl.getMandatoryJaceFEs();
/* 1916 */       break;
/*      */     case DisposalPhase: case RMLog: 
/*      */     case ChoiceList: case MarkingSet: 
/*      */     case Unknown: default: 
/* 1920 */       result = null;
/*      */     }
/*      */     
/*      */     
/* 1924 */     Tracer.traceMethodExit(new Object[] { result });
/* 1925 */     return result;
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
/*      */   static String[] getEntityRequiredPropertyNames(EntityType entityType)
/*      */   {
/* 1939 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 1940 */     String[] result = null;
/* 1941 */     switch (entityType)
/*      */     {
/*      */     case FilePlan: 
/* 1944 */       result = P8CE_FilePlanImpl.getMandatoryPropertyNames();
/* 1945 */       break;
/*      */     
/*      */     case RecordCategory: 
/* 1948 */       result = P8CE_RecordCategoryImpl.getMandatoryPropertyNames();
/* 1949 */       break;
/*      */     
/*      */     case RecordFolder: 
/*      */     case ElectronicRecordFolder: 
/*      */     case PhysicalContainer: 
/*      */     case HybridRecordFolder: 
/*      */     case PhysicalRecordFolder: 
/* 1956 */       result = P8CE_RecordFolderImpl.getMandatoryPropertyNames();
/* 1957 */       break;
/*      */     
/*      */     case RecordVolume: 
/* 1960 */       result = P8CE_RecordVolumeImpl.getMandatoryPropertyNames();
/* 1961 */       break;
/*      */     
/*      */     case SystemConfiguration: 
/* 1964 */       result = P8CE_SystemConfigurationImpl.getMandatoryPropertyNames();
/* 1965 */       break;
/*      */     
/*      */     case Pattern: 
/* 1968 */       result = P8CE_NamingPatternImpl.getMandatoryPropertyNames();
/* 1969 */       break;
/*      */     
/*      */     case PatternSequence: 
/* 1972 */       result = P8CE_NamingPatternSequenceImpl.getMandatoryPropertyNames();
/* 1973 */       break;
/*      */     
/*      */     case DispositionAction: 
/* 1976 */       result = P8CE_DispositionActionImpl.getMandatoryPropertyNames();
/* 1977 */       break;
/*      */     
/*      */     case DispositionSchedule: 
/* 1980 */       result = P8CE_DispositionScheduleImpl.getMandatoryPropertyNames();
/* 1981 */       break;
/*      */     
/*      */     case Phase: 
/* 1984 */       result = P8CE_DispositionPhaseImpl.getMandatoryPropertyNames();
/* 1985 */       break;
/*      */     
/*      */     case DispositionTrigger: 
/* 1988 */       result = P8CE_DispositionTriggerImpl.getMandatoryPropertyNames();
/* 1989 */       break;
/*      */     
/*      */     case RecordType: 
/* 1992 */       result = P8CE_RecordTypeImpl.getMandatoryPropertyNames();
/* 1993 */       break;
/*      */     
/*      */     case Hold: 
/* 1996 */       result = P8CE_HoldImpl.getMandatoryPropertyNames();
/* 1997 */       break;
/*      */     
/*      */     case PatternLevel: 
/* 2000 */       result = P8CE_NamingPatternLevelImpl.getMandatoryPropertyNames();
/* 2001 */       break;
/*      */     
/*      */     case AlternateRetention: 
/* 2004 */       result = P8CE_AlternateRetentionImpl.getMandatoryPropertyNames();
/* 2005 */       break;
/*      */     
/*      */     case ReportDefinition: 
/* 2008 */       result = P8CE_ReportDefinitionImpl.getMandatoryPropertyNames();
/* 2009 */       break;
/*      */     
/*      */     case CustomObject: 
/*      */     case Reservation: 
/*      */     case PhaseException: 
/*      */     case ConnectorRegistration: 
/*      */     case RMSystem: 
/*      */     case AuditConfig: 
/*      */     case AccessRole: 
/* 2018 */       result = P8CE_RMCustomObjectImpl.getMandatoryPropertyNames();
/* 2019 */       break;
/*      */     
/*      */     case Location: 
/* 2022 */       result = P8CE_LocationImpl.getMandatoryPropertyNames();
/* 2023 */       break;
/*      */     
/*      */     case Record: 
/*      */     case ElectronicRecord: 
/*      */     case EmailRecord: 
/*      */     case PhysicalRecord: 
/*      */     case PDFRecord: 
/* 2030 */       result = P8CE_RecordImpl.getMandatoryPropertyNames();
/* 2031 */       break;
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
/* 2043 */       result = P8CE_RMLinkImpl.getMandatoryPropertyNames();
/* 2044 */       break;
/*      */     
/*      */     case ContentRepository: 
/* 2047 */       result = P8CE_ContentRepositoryImpl.getMandatoryPropertyNames();
/* 2048 */       break;
/*      */     
/*      */     case FilePlanRepository: 
/* 2051 */       result = P8CE_FilePlanRepositoryImpl.getMandatoryPropertyNames();
/* 2052 */       break;
/*      */     
/*      */     case Repository: 
/* 2055 */       result = P8CE_RepositoryImpl.getMandatoryPropertyNames();
/* 2056 */       break;
/*      */     
/*      */     case Domain: 
/* 2059 */       result = P8CE_RMDomainImpl.getMandatoryPropertyNames();
/* 2060 */       break;
/*      */     
/*      */     case AuditEvent: 
/* 2063 */       result = P8CE_AuditEventImpl.getMandatoryPropertyNames();
/* 2064 */       break;
/*      */     
/*      */     case ClassificationGuide: 
/* 2067 */       result = P8CE_ClassificationGuideImpl.getMandatoryPropertyNames();
/* 2068 */       break;
/*      */     
/*      */     case ClassificationGuideSection: 
/*      */     case Container: 
/*      */     case RMFolder: 
/* 2073 */       result = P8CE_BaseContainerImpl.getMandatoryPropertyNames();
/* 2074 */       break;
/*      */     
/*      */     case ContentItem: 
/*      */     case Transcript: 
/*      */     case ClassificationGuideTopic: 
/*      */     case TransferMapping: 
/*      */     case WorkflowDefinition: 
/* 2081 */       result = P8CE_ContentItemImpl.getMandatoryPropertyNames();
/* 2082 */       break;
/*      */     case DisposalPhase: case RMLog: 
/*      */     case ChoiceList: case MarkingSet: 
/*      */     case Unknown: default: 
/* 2086 */       result = new String[0];
/*      */     }
/*      */     
/*      */     
/* 2090 */     Tracer.traceMethodExit(new Object[] { result });
/* 2091 */     return result;
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
/*      */   static String getEntityClassName(EntityType entityType)
/*      */   {
/* 2104 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 2105 */     String result = "";
/* 2106 */     switch (entityType)
/*      */     {
/*      */     case AccessRole: 
/* 2109 */       result = "AccessRole";
/* 2110 */       break;
/*      */     case AlternateRetention: 
/* 2112 */       result = "AlternateRetention";
/* 2113 */       break;
/*      */     case AuditConfig: 
/* 2115 */       result = "AuditMetadataConfiguration";
/* 2116 */       break;
/*      */     case AuditEvent: 
/* 2118 */       result = "ObjectChangeEvent";
/* 2119 */       break;
/*      */     case ClassificationGuide: 
/* 2121 */       result = "ClassificationGuide";
/* 2122 */       break;
/*      */     case ClassificationGuideSection: 
/* 2124 */       result = "GuideSection";
/* 2125 */       break;
/*      */     case ClassificationGuideTopic: 
/* 2127 */       result = "GuideTopic";
/* 2128 */       break;
/*      */     case ConnectorRegistration: 
/* 2130 */       result = "ConnectorRegistration";
/* 2131 */       break;
/*      */     case Container: 
/* 2133 */       result = "Folder";
/* 2134 */       break;
/*      */     case ContentItem: 
/* 2136 */       result = "Document";
/* 2137 */       break;
/*      */     case ContentRepository: 
/* 2139 */       result = "ObjectStore";
/* 2140 */       break;
/*      */     case CustomObject: 
/* 2142 */       result = "CustomObject";
/* 2143 */       break;
/*      */     case DispositionAction: 
/* 2145 */       result = "Action1";
/* 2146 */       break;
/*      */     case DisposalPhase: 
/* 2148 */       result = "DisposalPhase";
/* 2149 */       break;
/*      */     case DispositionSchedule: 
/* 2151 */       result = "DisposalSchedule";
/* 2152 */       break;
/*      */     case DispositionTrigger: 
/* 2154 */       result = "DisposalTrigger";
/* 2155 */       break;
/*      */     case Domain: 
/* 2157 */       result = "Domain";
/* 2158 */       break;
/*      */     case ElectronicRecord: 
/* 2160 */       result = "ElectronicRecordInfo";
/* 2161 */       break;
/*      */     case ElectronicRecordFolder: 
/* 2163 */       result = "ElectronicRecordFolder";
/* 2164 */       break;
/*      */     case EmailRecord: 
/* 2166 */       result = "EmailRecordInfo";
/* 2167 */       break;
/*      */     case ExtractLink: 
/* 2169 */       result = "ExtractLink";
/* 2170 */       break;
/*      */     case FilePlan: 
/* 2172 */       result = "ClassificationScheme";
/* 2173 */       break;
/*      */     case FilePlanRepository: 
/* 2175 */       result = "ObjectStore";
/* 2176 */       break;
/*      */     case Hold: 
/* 2178 */       result = "RecordHold";
/* 2179 */       break;
/*      */     case HybridRecordFolder: 
/* 2181 */       result = "HybridRecordFolder";
/* 2182 */       break;
/*      */     case Location: 
/* 2184 */       result = "Location";
/* 2185 */       break;
/*      */     case Pattern: 
/* 2187 */       result = "Pattern";
/* 2188 */       break;
/*      */     case PatternLevel: 
/* 2190 */       result = "PatternLevel";
/* 2191 */       break;
/*      */     case PatternSequence: 
/* 2193 */       result = "PatternSequences";
/* 2194 */       break;
/*      */     case PDFRecord: 
/* 2196 */       result = "PDFRecord";
/* 2197 */       break;
/*      */     case Phase: 
/* 2199 */       result = "Phase";
/* 2200 */       break;
/*      */     case PhaseException: 
/* 2202 */       result = "Exception";
/* 2203 */       break;
/*      */     case PhysicalContainer: 
/* 2205 */       result = "Box";
/* 2206 */       break;
/*      */     case PhysicalRecord: 
/* 2208 */       result = "Markers";
/* 2209 */       break;
/*      */     case PhysicalRecordFolder: 
/* 2211 */       result = "PhysicalRecordFolder";
/* 2212 */       break;
/*      */     case ReceiptLink: 
/* 2214 */       result = "Relation";
/* 2215 */       break;
/*      */     case Record: 
/* 2217 */       result = "RecordInfo";
/* 2218 */       break;
/*      */     case RecordCategory: 
/* 2220 */       result = "RecordCategory";
/* 2221 */       break;
/*      */     case RecordContainerHoldLink: 
/* 2223 */       result = "RMFolderHoldLink";
/* 2224 */       break;
/*      */     case RecordCopyLink: 
/* 2226 */       result = "RecordCopyLink";
/* 2227 */       break;
/*      */     case RecordFolder: 
/* 2229 */       result = "RecordFolder";
/* 2230 */       break;
/*      */     case RecordHoldLink: 
/* 2232 */       result = "RecordHoldLink";
/* 2233 */       break;
/*      */     case RecordSeeAlsoLink: 
/* 2235 */       result = "RecordSeeAlsoLink";
/* 2236 */       break;
/*      */     case RecordType: 
/* 2238 */       result = "RecordType";
/* 2239 */       break;
/*      */     case RecordVolume: 
/* 2241 */       result = "Volume";
/* 2242 */       break;
/*      */     case ReferenceLink: 
/* 2244 */       result = "ReferenceLink";
/* 2245 */       break;
/*      */     case Relation: 
/* 2247 */       result = "CustomObject";
/* 2248 */       break;
/*      */     case ReportDefinition: 
/* 2250 */       result = "RMReportDefinition";
/* 2251 */       break;
/*      */     case Repository: 
/* 2253 */       result = "ObjectStore";
/* 2254 */       break;
/*      */     case Reservation: 
/* 2256 */       result = "CustomObject";
/* 2257 */       break;
/*      */     case RMFolder: 
/* 2259 */       result = "RMFolder";
/* 2260 */       break;
/*      */     case RMLink: 
/* 2262 */       result = "Relation";
/* 2263 */       break;
/*      */     case RMLog: 
/* 2265 */       result = "CustomObject";
/* 2266 */       break;
/*      */     case RMSystem: 
/* 2268 */       result = "RMSystem";
/* 2269 */       break;
/*      */     case SupersedeLink: 
/* 2271 */       result = "Relation";
/* 2272 */       break;
/*      */     case SystemConfiguration: 
/* 2274 */       result = "SystemConfiguration";
/* 2275 */       break;
/*      */     case Transcript: 
/* 2277 */       result = "Transcript";
/* 2278 */       break;
/*      */     case TransferMapping: 
/* 2280 */       result = "RMTransferMapping";
/* 2281 */       break;
/*      */     case WorkflowDefinition: 
/* 2283 */       result = "WorkflowDefinition";
/* 2284 */       break;
/*      */     case ChoiceList: case MarkingSet: 
/*      */     case Unknown: 
/*      */     default: 
/* 2288 */       result = "";
/*      */     }
/*      */     
/*      */     
/* 2292 */     Tracer.traceMethodExit(new Object[] { result });
/* 2293 */     return result;
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
/*      */   static EntityType getEntityType(String classSymName)
/*      */   {
/* 2307 */     Tracer.traceMethodEntry(new Object[] { classSymName });
/* 2308 */     EntityType result = null;
/* 2309 */     if (classSymName != null)
/*      */     {
/* 2311 */       result = (EntityType)ClassSymNameToEntityTypeMap.get(classSymName);
/*      */     }
/*      */     
/* 2314 */     result = result != null ? result : EntityType.Unknown;
/* 2315 */     Tracer.traceMethodExit(new Object[] { result });
/* 2316 */     return result;
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
/*      */   static String getBaseClassSymName(EntityType entityType)
/*      */   {
/* 2329 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 2330 */     String className = null;
/*      */     
/* 2332 */     int type = entityType.getIntValue();
/* 2333 */     if (((type >= 100) && (type <= 110)) || ((type >= 800) && (type <= 801)) || (type == 1100))
/*      */     {
/*      */ 
/*      */ 
/* 2337 */       className = "Folder";
/*      */     }
/* 2339 */     else if (((type >= 200) && (type <= 220)) || (type == 804) || (type == 1200))
/*      */     {
/*      */ 
/* 2342 */       className = "CustomObject";
/*      */     }
/* 2344 */     else if (((type >= 300) && (type <= 305)) || (type == 802) || ((type >= 1300) && (type <= 1301)))
/*      */     {
/*      */ 
/* 2347 */       className = "Document";
/*      */     }
/* 2349 */     else if ((type >= 400) && (type <= 409))
/*      */     {
/* 2351 */       className = "Link";
/*      */     }
/* 2353 */     else if (((type >= 500) && (type <= 501)) || (type == 549))
/*      */     {
/*      */ 
/* 2356 */       className = "ObjectStore";
/*      */     }
/* 2358 */     else if (type == 550)
/*      */     {
/* 2360 */       className = "Domain";
/*      */     }
/*      */     
/* 2363 */     Tracer.traceMethodExit(new Object[] { className });
/* 2364 */     return className;
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
/*      */   static List<RMPermission> convertToJarmPermissions(AccessPermissionList jacePermissions)
/*      */   {
/* 2379 */     Tracer.traceMethodEntry(new Object[] { jacePermissions });
/* 2380 */     List<RMPermission> result = new ArrayList();
/* 2381 */     if (jacePermissions != null)
/*      */     {
/* 2383 */       AccessPermission jacePerm = null;
/* 2384 */       Iterator<AccessPermission> it = jacePermissions.iterator();
/* 2385 */       while ((it != null) && (it.hasNext()))
/*      */       {
/* 2387 */         jacePerm = (AccessPermission)it.next();
/* 2388 */         if (jacePerm != null) {
/* 2389 */           result.add(new P8CE_RMPermissionImpl(jacePerm));
/*      */         }
/*      */       }
/*      */     }
/* 2393 */     Tracer.traceMethodExit(new Object[] { result });
/* 2394 */     return result;
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
/*      */   static AccessPermissionList convertToJacePermissions(List<RMPermission> jarmPerms)
/*      */   {
/* 2409 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/* 2410 */     AccessPermissionList result = null;
/* 2411 */     if (jarmPerms != null)
/*      */     {
/* 2413 */       result = Factory.AccessPermission.createList();
/* 2414 */       for (RMPermission jarmPerm : jarmPerms)
/*      */       {
/*      */ 
/* 2417 */         if ((jarmPerm != null) && (RMPermissionSource.Direct == jarmPerm.getPermissionSource()))
/*      */         {
/* 2419 */           result.add(((P8CE_RMPermissionImpl)jarmPerm).getJacePermission());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2424 */     Tracer.traceMethodExit(new Object[] { result });
/* 2425 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void copyJacePropIntoCollection(Property prop, Properties props)
/*      */   {
/* 2437 */     Tracer.traceMethodEntry(new Object[] { prop, props });
/* 2438 */     String symName = prop.getPropertyName();
/*      */     
/* 2440 */     if ((prop instanceof PropertyString)) {
/* 2441 */       props.putValue(symName, prop.getStringValue());
/* 2442 */     } else if ((prop instanceof PropertyDateTime)) {
/* 2443 */       props.putValue(symName, prop.getDateTimeValue());
/* 2444 */     } else if ((prop instanceof PropertyBoolean)) {
/* 2445 */       props.putValue(symName, prop.getBooleanValue());
/* 2446 */     } else if ((prop instanceof PropertyId)) {
/* 2447 */       props.putValue(symName, prop.getIdValue());
/* 2448 */     } else if ((prop instanceof PropertyInteger32)) {
/* 2449 */       props.putValue(symName, prop.getInteger32Value());
/* 2450 */     } else if ((prop instanceof PropertyFloat64)) {
/* 2451 */       props.putValue(symName, prop.getInteger32Value());
/* 2452 */     } else if ((prop instanceof PropertyEngineObject)) {
/* 2453 */       props.putValue(symName, prop.getEngineObjectValue());
/* 2454 */     } else if ((prop instanceof PropertyStringList)) {
/* 2455 */       props.putValue(symName, prop.getStringListValue());
/* 2456 */     } else if ((prop instanceof PropertyInteger32List)) {
/* 2457 */       props.putValue(symName, prop.getInteger32ListValue());
/* 2458 */     } else if ((prop instanceof PropertyDependentObjectList)) {
/* 2459 */       props.putValue(symName, prop.getDependentObjectListValue());
/* 2460 */     } else if ((prop instanceof PropertyIndependentObjectSet)) {
/* 2461 */       props.putValue(symName, prop.getIndependentObjectSetValue());
/* 2462 */     } else if ((prop instanceof PropertyBinaryList)) {
/* 2463 */       props.putValue(symName, prop.getBinaryListValue());
/* 2464 */     } else if ((prop instanceof PropertyIdList)) {
/* 2465 */       props.putValue(symName, prop.getIdListValue());
/* 2466 */     } else if ((prop instanceof PropertyFloat64List)) {
/* 2467 */       props.putValue(symName, prop.getFloat64ListValue());
/* 2468 */     } else if ((prop instanceof PropertyDateTimeList)) {
/* 2469 */       props.putValue(symName, prop.getDateTimeListValue());
/* 2470 */     } else if ((prop instanceof PropertyBooleanList)) {
/* 2471 */       props.putValue(symName, prop.getBooleanListValue());
/*      */     }
/* 2473 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   static Property getOrFetchJaceProperty(IndependentObject jaceObj, String symbolicName, PropertyFilter fetchFilter)
/*      */   {
/* 2489 */     Tracer.traceMethodEntry(new Object[] { jaceObj, symbolicName, fetchFilter });
/* 2490 */     Property result = null;
/*      */     try
/*      */     {
/* 2493 */       Properties jaceProps = jaceObj.getProperties();
/* 2494 */       if (!jaceProps.isPropertyPresent(symbolicName))
/*      */       {
/* 2496 */         fetchAdditionalJaceProperties(jaceObj, fetchFilter);
/* 2497 */         jaceProps = jaceObj.getProperties();
/*      */       }
/* 2499 */       if (jaceProps.isPropertyPresent(symbolicName))
/*      */       {
/* 2501 */         result = jaceProps.get(symbolicName);
/*      */       }
/*      */     }
/*      */     catch (Exception ignored)
/*      */     {
/* 2506 */       Tracer.traceMinimumMsg("Exception during retrieval of property ''{0}'' of JACE object ''{1}'': {2}.", new Object[] { symbolicName, jaceObj.toString(), ignored.getMessage() });
/*      */     }
/*      */     
/*      */ 
/* 2510 */     Tracer.traceMethodExit(new Object[] { result });
/* 2511 */     return result;
/*      */   }
/*      */   
/*      */   static Realm fetchJaceRealm(RMDomain jarmDomain)
/*      */   {
/* 2516 */     Tracer.traceMethodEntry(new Object[] { jarmDomain });
/* 2517 */     Realm result = null;
/* 2518 */     if (jarmDomain != null)
/*      */     {
/* 2520 */       P8CE_DomainConnectionImpl domainConn = (P8CE_DomainConnectionImpl)jarmDomain.getDomainConnection();
/* 2521 */       if (domainConn != null)
/*      */       {
/* 2523 */         Connection jaceConn = domainConn.getJaceConnection();
/* 2524 */         long startTime = System.currentTimeMillis();
/* 2525 */         result = Factory.Realm.fetchCurrent(jaceConn, CEPF_IdOnly);
/* 2526 */         long endTime = System.currentTimeMillis();
/* 2527 */         Tracer.traceExtCall("Factory.Realm.fetchCurrent", startTime, endTime, Integer.valueOf(1), result, new Object[0]);
/*      */       }
/*      */     }
/*      */     
/* 2531 */     Tracer.traceMethodExit(new Object[] { result });
/* 2532 */     return result;
/*      */   }
/*      */   
/*      */   static User fetchCurrentJaceUser(Domain jaceDomain)
/*      */   {
/* 2537 */     Tracer.traceMethodEntry(new Object[] { jaceDomain });
/* 2538 */     User result = null;
/* 2539 */     if (jaceDomain != null)
/*      */     {
/* 2541 */       Connection jaceConn = jaceDomain.getConnection();
/* 2542 */       PropertyFilter jacePF = new PropertyFilter();
/* 2543 */       List<FilterElement> mandatoryFEs = P8CE_RMUserImpl.getMandatoryJaceFEs();
/* 2544 */       for (FilterElement fe : mandatoryFEs)
/*      */       {
/* 2546 */         jacePF.addIncludeProperty(fe);
/*      */       }
/*      */       
/* 2549 */       long startTime = System.currentTimeMillis();
/* 2550 */       result = Factory.User.fetchCurrent(jaceConn, jacePF);
/* 2551 */       long endTime = System.currentTimeMillis();
/* 2552 */       Tracer.traceExtCall("Factory.User.fetchCurrent", startTime, endTime, Integer.valueOf(1), result, new Object[0]);
/*      */     }
/*      */     
/* 2555 */     Tracer.traceMethodExit(new Object[] { result });
/* 2556 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   static Integer32List allowedContaineesStringToInt32List(String allowedContainees)
/*      */   {
/* 2562 */     Tracer.traceMethodEntry(new Object[] { allowedContainees });
/* 2563 */     Integer32List result = Factory.Integer32List.createList();
/* 2564 */     if (allowedContainees != null)
/*      */     {
/* 2566 */       String[] allowedContaineesArray = allowedContainees.trim().split(",");
/* 2567 */       for (int i = 0; i < allowedContaineesArray.length; i++)
/*      */       {
/* 2569 */         result.add(Integer.valueOf(allowedContaineesArray[i]));
/*      */       }
/*      */     }
/*      */     
/* 2573 */     Tracer.traceMethodExit(new Object[] { result });
/* 2574 */     return result;
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
/*      */   static List<Container> getContainedBy(Repository repository, IndependentObject jaceBaseObj, boolean excludeDeleted)
/*      */   {
/* 2590 */     Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObj, Boolean.valueOf(excludeDeleted) });
/*      */     
/* 2592 */     List<Container> result = new ArrayList(1);
/* 2593 */     String jaceObjIdent = jaceBaseObj.getObjectReference().getObjectIdentity();
/*      */     
/* 2595 */     StringBuilder sb = new StringBuilder();
/* 2596 */     sb.append("SELECT rcr.[").append("Tail").append("], rcr.[").append("DateCreated").append("] ");
/* 2597 */     sb.append("FROM [").append("ReferentialContainmentRelationship").append("] rcr ");
/* 2598 */     sb.append("WHERE  rcr.[").append("Head").append("] = OBJECT('").append(jaceObjIdent).append("') ");
/* 2599 */     sb.append("ORDER BY rcr.[").append("DateCreated").append("] ");
/* 2600 */     String sqlStatement = sb.toString();
/*      */     
/* 2602 */     PropertyFilter jacePF = new PropertyFilter();
/* 2603 */     jacePF.addIncludeProperty(1, null, Boolean.FALSE, "Tail", null);
/* 2604 */     jacePF.addIncludeProperty(1, null, Boolean.FALSE, getContainerTypesPropNames(), null);
/*      */     
/* 2606 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 2607 */     ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 2608 */     SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/* 2609 */     Integer pageSize = null;
/* 2610 */     Boolean continuable = Boolean.TRUE;
/*      */     
/* 2612 */     long startTime = System.currentTimeMillis();
/* 2613 */     RepositoryRowSet jaceRowSet = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, jacePF, continuable);
/* 2614 */     long endTime = System.currentTimeMillis();
/* 2615 */     Boolean elementCountIndicator = null;
/* 2616 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 2618 */       elementCountIndicator = jaceRowSet != null ? Boolean.valueOf(jaceRowSet.isEmpty()) : null;
/*      */     }
/* 2620 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, endTime, elementCountIndicator, jaceRowSet, new Object[] { jaceSearchSQL, Integer.valueOf(100), jacePF, Boolean.FALSE });
/*      */     
/* 2622 */     if (jaceRowSet != null)
/*      */     {
/* 2624 */       RepositoryRow jaceRow = null;
/* 2625 */       Folder jaceBaseFolder = null;
/* 2626 */       Container jarmContainer = null;
/* 2627 */       IGenerator<? extends Container> generator = null;
/* 2628 */       PageIterator jacePI = jaceRowSet.pageIterator();
/* 2629 */       while (jacePI.nextPage())
/*      */       {
/* 2631 */         Object[] currentPage = jacePI.getCurrentPage();
/* 2632 */         for (Object obj : currentPage)
/*      */         {
/* 2634 */           jaceRow = (RepositoryRow)obj;
/* 2635 */           if ((jaceRow != null) && (jaceRow.getProperties().isPropertyPresent("Tail")))
/*      */           {
/* 2637 */             jaceBaseFolder = (Folder)jaceRow.getProperties().getEngineObjectValue("Tail");
/* 2638 */             if (jaceBaseFolder != null)
/*      */             {
/* 2640 */               if (excludeDeleted)
/*      */               {
/* 2642 */                 Properties jaceProps = jaceBaseFolder.getProperties();
/* 2643 */                 if ((jaceProps.isPropertyPresent("IsDeleted")) && (Boolean.TRUE.equals(jaceProps.getBooleanValue("IsDeleted")))) {}
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/* 2649 */                 generator = getEntityGenerator(jaceBaseFolder);
/* 2650 */                 if (generator != null)
/*      */                 {
/* 2652 */                   jarmContainer = (Container)generator.create(repository, jaceBaseFolder);
/* 2653 */                   if (jarmContainer != null)
/* 2654 */                     result.add(jarmContainer);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2662 */     Tracer.traceMethodExit(new Object[] { result });
/* 2663 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void refreshWithMandatoryProperties(JaceBasable jaceBasable)
/*      */   {
/* 2675 */     Tracer.traceMethodEntry(new Object[] { jaceBasable });
/* 2676 */     PropertyFilter jacePF = new PropertyFilter();
/* 2677 */     List<FilterElement> mandatoryFEs = jaceBasable.getMandatoryFEs();
/* 2678 */     for (FilterElement fe : mandatoryFEs) {
/* 2679 */       jacePF.addIncludeProperty(fe);
/*      */     }
/* 2681 */     IndependentObject jaceBaseObj = (IndependentObject)jaceBasable.getJaceBaseObject();
/*      */     
/* 2683 */     long startTime = System.currentTimeMillis();
/* 2684 */     jaceBaseObj.refresh(jacePF);
/* 2685 */     long endTime = System.currentTimeMillis();
/* 2686 */     Tracer.traceExtCall("IndependentObject.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*      */     
/* 2688 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   static <I> I fetchSVObjPropValue(Repository repository, IndependentObject jaceBaseObj, List<FilterElement> jaceFEs, String propSymName, IGenerator<I> generator)
/*      */   {
/* 2706 */     Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObj, jaceFEs, propSymName, generator });
/* 2707 */     I result = null;
/*      */     
/* 2709 */     PropertyFilter jacePF = new PropertyFilter();
/* 2710 */     jacePF.addIncludeProperty(1, null, null, propSymName, null);
/* 2711 */     for (FilterElement fe : jaceFEs)
/*      */     {
/*      */ 
/* 2714 */       jacePF.addIncludeProperty(2, fe.getMaxSize(), fe.getLevelDependents(), fe.getValue(), fe.getPageSize());
/*      */     }
/*      */     
/* 2717 */     Property jaceProp = getOrFetchJaceProperty(jaceBaseObj, propSymName, jacePF);
/* 2718 */     if ((jaceProp != null) && ((jaceProp instanceof PropertyEngineObject)))
/*      */     {
/* 2720 */       EngineObject jacePropValue = jaceProp.getEngineObjectValue();
/* 2721 */       if (jacePropValue != null)
/*      */       {
/* 2723 */         result = generator.create(repository, jacePropValue);
/*      */       }
/*      */     }
/*      */     
/* 2727 */     Tracer.traceMethodExit(new Object[] { result });
/* 2728 */     return result;
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
/*      */   static <I> List<I> fetchMVObjPropValueAsList(Repository repository, IndependentObject jaceBaseObj, List<FilterElement> jaceFEs, String propSymName, IGenerator<I> generator)
/*      */   {
/* 2748 */     Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObj, jaceFEs, propSymName, generator });
/* 2749 */     List<I> resultList = new ArrayList(0);
/*      */     
/* 2751 */     PropertyFilter jacePF = new PropertyFilter();
/* 2752 */     jacePF.addIncludeProperty(1, null, null, propSymName, null);
/* 2753 */     for (FilterElement fe : jaceFEs)
/*      */     {
/*      */ 
/* 2756 */       jacePF.addIncludeProperty(2, fe.getMaxSize(), fe.getLevelDependents(), fe.getValue(), fe.getPageSize());
/*      */     }
/*      */     
/* 2759 */     Property jaceProp = getOrFetchJaceProperty(jaceBaseObj, propSymName, jacePF);
/* 2760 */     EngineObject jaceObjBase; I jarmObj; Iterator it; if ((jaceProp != null) && ((jaceProp instanceof PropertyIndependentObjectSet)))
/*      */     {
/* 2762 */       IndependentObjectSet jaceObjSet = jaceProp.getIndependentObjectSetValue();
/* 2763 */       if ((jaceObjSet != null) && (!jaceObjSet.isEmpty()))
/*      */       {
/* 2765 */         jaceObjBase = null;
/* 2766 */         jarmObj = null;
/* 2767 */         for (it = jaceObjSet.iterator(); it.hasNext();)
/*      */         {
/* 2769 */           jaceObjBase = (EngineObject)it.next();
/* 2770 */           jarmObj = generator.create(repository, jaceObjBase);
/* 2771 */           resultList.add(jarmObj);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2776 */     Tracer.traceMethodExit(new Object[] { resultList });
/* 2777 */     return resultList;
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
/*      */   static <I> PageableSet<I> fetchMVObjPropValueAsPageableSet(Repository repository, IndependentObject jaceBaseObj, List<FilterElement> jaceFEs, String propSymName, IGenerator<I> generator)
/*      */   {
/* 2796 */     Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObj, jaceFEs, propSymName, generator });
/* 2797 */     PageableSet<I> resultSet = null;
/*      */     
/* 2799 */     PropertyFilter jacePF = new PropertyFilter();
/* 2800 */     jacePF.addIncludeProperty(1, null, null, propSymName, null);
/* 2801 */     for (FilterElement fe : jaceFEs)
/*      */     {
/*      */ 
/* 2804 */       jacePF.addIncludeProperty(2, fe.getMaxSize(), fe.getLevelDependents(), fe.getValue(), fe.getPageSize());
/*      */     }
/*      */     
/* 2807 */     Property jaceProp = getOrFetchJaceProperty(jaceBaseObj, propSymName, jacePF);
/* 2808 */     if ((jaceProp != null) && ((jaceProp instanceof PropertyIndependentObjectSet)))
/*      */     {
/* 2810 */       IndependentObjectSet jaceObjSet = jaceProp.getIndependentObjectSetValue();
/* 2811 */       if ((jaceObjSet != null) && (!jaceObjSet.isEmpty()))
/*      */       {
/* 2813 */         boolean supportsPaging = true;
/* 2814 */         resultSet = new P8CE_PageableSetImpl(repository, jaceObjSet, supportsPaging, generator);
/*      */       }
/*      */     }
/*      */     
/* 2818 */     Tracer.traceMethodExit(new Object[] { resultSet });
/* 2819 */     return resultSet;
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
/*      */   static AccessPermissionList getJacePermissions(IndependentObject jaceObj)
/*      */   {
/* 2832 */     Tracer.traceMethodEntry(new Object[] { jaceObj });
/* 2833 */     PropertyFilter jacePF = new PropertyFilter();
/* 2834 */     jacePF.addIncludeProperty(0, null, null, "Permissions", null);
/* 2835 */     for (FilterElement fe : P8CE_RMPermissionImpl.getMandatoryJaceFEs())
/*      */     {
/* 2837 */       jacePF.addIncludeProperty(fe);
/*      */     }
/*      */     
/* 2840 */     Properties jaceProps = jaceObj.getProperties();
/* 2841 */     fetchAdditionalJaceProperties(jaceObj, jacePF);
/* 2842 */     jaceProps = jaceObj.getProperties();
/* 2843 */     PropertyDependentObjectList jacePermsProp = (PropertyDependentObjectList)jaceProps.get("Permissions");
/*      */     
/*      */ 
/* 2846 */     AccessPermissionList result = (AccessPermissionList)jacePermsProp.getDependentObjectListValue();
/*      */     
/*      */ 
/* 2849 */     Tracer.traceMethodExit(new Object[] { result });
/* 2850 */     return result;
/*      */   }
/*      */   
/*      */   private static Map<String, EntityType> createClassToEntityMap()
/*      */   {
/* 2855 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     
/* 2857 */     Map<String, EntityType> map = new HashMap();
/* 2858 */     map.put("AlternateRetention", EntityType.AlternateRetention);
/* 2859 */     map.put("Box", EntityType.PhysicalContainer);
/* 2860 */     map.put("ClassificationGuide", EntityType.ClassificationGuide);
/* 2861 */     map.put("GuideSection", EntityType.ClassificationGuideSection);
/* 2862 */     map.put("GuideTopic", EntityType.ClassificationGuideTopic);
/* 2863 */     map.put("ConnectorRegistration", EntityType.ConnectorRegistration);
/* 2864 */     map.put("CreationEvent", EntityType.AuditEvent);
/* 2865 */     map.put("CustomObject", EntityType.CustomObject);
/* 2866 */     map.put("DeletionEvent", EntityType.AuditEvent);
/* 2867 */     map.put("DigitalPhotographRecord", EntityType.ElectronicRecord);
/* 2868 */     map.put("DisposalPhase", EntityType.DisposalPhase);
/* 2869 */     map.put("Action1", EntityType.DispositionAction);
/* 2870 */     map.put("Exception", EntityType.PhaseException);
/* 2871 */     map.put("DisposalSchedule", EntityType.DispositionSchedule);
/* 2872 */     map.put("DisposalTrigger", EntityType.DispositionTrigger);
/* 2873 */     map.put("Document", EntityType.ContentItem);
/* 2874 */     map.put("ElectronicRecordInfo", EntityType.ElectronicRecord);
/* 2875 */     map.put("ElectronicRecordFolder", EntityType.ElectronicRecordFolder);
/* 2876 */     map.put("EmailRecordInfo", EntityType.EmailRecord);
/* 2877 */     map.put("ExtractLink", EntityType.ExtractLink);
/* 2878 */     map.put("ClassificationScheme", EntityType.FilePlan);
/* 2879 */     map.put("ClassificationSchemes", EntityType.FilePlan);
/* 2880 */     map.put("Folder", EntityType.Container);
/* 2881 */     map.put("RecordHold", EntityType.Hold);
/* 2882 */     map.put("HybridFolderLink", EntityType.Relation);
/* 2883 */     map.put("HybridRecordFolder", EntityType.HybridRecordFolder);
/* 2884 */     map.put("Location", EntityType.Location);
/* 2885 */     map.put("Pattern", EntityType.Pattern);
/* 2886 */     map.put("PatternLevel", EntityType.PatternLevel);
/* 2887 */     map.put("PatternSequences", EntityType.PatternSequence);
/* 2888 */     map.put("ObjectStore", EntityType.Repository);
/* 2889 */     map.put("ObjectChangeEvent", EntityType.AuditEvent);
/* 2890 */     map.put("PDFRecord", EntityType.PDFRecord);
/* 2891 */     map.put("Phase", EntityType.Phase);
/* 2892 */     map.put("PhysicalBox", EntityType.PhysicalContainer);
/* 2893 */     map.put("Markers", EntityType.PhysicalRecord);
/* 2894 */     map.put("PhysicalRecordFolder", EntityType.PhysicalRecordFolder);
/* 2895 */     map.put("RecordInfo", EntityType.Record);
/* 2896 */     map.put("RecordCategory", EntityType.RecordCategory);
/* 2897 */     map.put("RMFolderHoldLink", EntityType.RecordContainerHoldLink);
/* 2898 */     map.put("RecordCopyLink", EntityType.RecordCopyLink);
/* 2899 */     map.put("RecordFolder", EntityType.RecordFolder);
/* 2900 */     map.put("RecordFolderSeeAlsoLink", EntityType.RecordHoldLink);
/* 2901 */     map.put("RecordHoldLink", EntityType.RecordHoldLink);
/* 2902 */     map.put("RecordSeeAlsoLink", EntityType.RecordSeeAlsoLink);
/* 2903 */     map.put("RecordType", EntityType.RecordType);
/* 2904 */     map.put("Volume", EntityType.RecordVolume);
/* 2905 */     map.put("ReferenceLink", EntityType.ReferenceLink);
/* 2906 */     map.put("RenditionLink", EntityType.RMLink);
/* 2907 */     map.put("RMReportDefinition", EntityType.ReportDefinition);
/* 2908 */     map.put("RMAudit", EntityType.AuditEvent);
/* 2909 */     map.put("RMFolder", EntityType.RMFolder);
/* 2910 */     map.put("Relation", EntityType.RMLink);
/* 2911 */     map.put("RMSystem", EntityType.RMSystem);
/* 2912 */     map.put("SystemConfiguration", EntityType.SystemConfiguration);
/* 2913 */     map.put("RMTransferMapping", EntityType.TransferMapping);
/* 2914 */     map.put("Transcript", EntityType.Transcript);
/* 2915 */     map.put("UpdateEvent", EntityType.AuditEvent);
/* 2916 */     map.put("WebRecord", EntityType.ElectronicRecord);
/* 2917 */     map.put("WorkflowDefinition", EntityType.WorkflowDefinition);
/*      */     
/* 2919 */     Tracer.traceMethodExit(new Object[] { map });
/* 2920 */     return map;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static RMLink getHoldLinkObject(RALBaseEntity holdableObject, Hold hold)
/*      */   {
/* 2929 */     Tracer.traceMethodEntry(new Object[] { holdableObject, hold });
/* 2930 */     RMLink result = null;
/*      */     
/* 2932 */     PropertyFilter jacePF = new PropertyFilter();
/* 2933 */     for (FilterElement fe : P8CE_RMLinkImpl.getMandatoryJaceFEs())
/* 2934 */       jacePF.addIncludeProperty(fe);
/* 2935 */     FilterElement isDynProp = new FilterElement(null, null, null, "IsDynamicHold", null);
/* 2936 */     jacePF.addIncludeProperty(isDynProp);
/*      */     
/*      */ 
/* 2939 */     StringBuilder sb = createSelectSqlFromJacePF(jacePF, null, "Relation", "rl");
/* 2940 */     sb.append("WHERE rl.head = OBJECT(");
/* 2941 */     sb.append(holdableObject.getObjectIdentity());
/* 2942 */     sb.append(") AND rl.tail = OBJECT(");
/* 2943 */     sb.append(hold.getObjectIdentity());
/* 2944 */     sb.append(")");
/* 2945 */     String sqlStatement = sb.toString();
/*      */     
/* 2947 */     P8CE_RepositoryImpl repository = (P8CE_RepositoryImpl)holdableObject.getRepository();
/* 2948 */     ObjectStore jaceObjStore = repository.getJaceObjectStore();
/* 2949 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 2950 */     SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/*      */     
/* 2952 */     long startTime = System.currentTimeMillis();
/* 2953 */     IndependentObjectSet jaceDocumentSet = jaceSearchScope.fetchObjects(jaceSearchSQL, Integer.valueOf(1), jacePF, Boolean.FALSE);
/* 2954 */     long stopTime = System.currentTimeMillis();
/* 2955 */     Boolean elementCountIndicator = null;
/* 2956 */     if (Tracer.isMediumTraceEnabled())
/*      */     {
/* 2958 */       elementCountIndicator = Boolean.valueOf(jaceDocumentSet.isEmpty());
/*      */     }
/* 2960 */     Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceDocumentSet, new Object[] { sqlStatement });
/*      */     
/*      */ 
/* 2963 */     Iterator<Object> linkIter = jaceDocumentSet.iterator();
/*      */     
/* 2965 */     if (linkIter.hasNext())
/*      */     {
/* 2967 */       result = (RMLink)P8CE_RMLinkImpl.getRMLinkGenerator().create(holdableObject.getRepository(), linkIter.next());
/*      */     }
/*      */     
/* 2970 */     Tracer.traceMethodExit(new Object[] { result });
/* 2971 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<String> getAssociatedROSIDs(ObjectStore fpos)
/*      */   {
/* 2979 */     Tracer.traceMethodEntry(new Object[] { fpos });
/* 2980 */     List<String> result = new ArrayList();
/* 2981 */     if (fpos == null)
/*      */     {
/* 2983 */       Tracer.traceMethodExit(new Object[] { result });
/* 2984 */       return result;
/*      */     }
/*      */     
/* 2987 */     StringBuilder sb = new StringBuilder();
/* 2988 */     sb.append("SELECT [").append("ImplementingClassName").append("], [").append("RepositoryName").append("], [").append("RepositoryType").append("], [").append("ServerName").append("] ");
/* 2989 */     sb.append("FROM [").append("ConnectorRegistration").append("] ");
/* 2990 */     sb.append("WHERE (NOT([").append("RepositoryName").append("] LIKE '*') AND [").append("Inactive").append("] = true)");
/* 2991 */     String sqlStmt = sb.toString();
/*      */     
/* 2993 */     SearchSQL jaceSearchSQL = new SearchSQL(sqlStmt);
/* 2994 */     SearchScope jaceSearchScope = new SearchScope(fpos);
/* 2995 */     Integer pageSize = null;
/* 2996 */     Boolean continuable = Boolean.TRUE;
/*      */     
/* 2998 */     long startTime = System.currentTimeMillis();
/* 2999 */     RepositoryRowSet rowset = jaceSearchScope.fetchRows(jaceSearchSQL, pageSize, null, continuable);
/* 3000 */     long stopTime = System.currentTimeMillis();
/* 3001 */     Boolean elementCountIndicator = null;
/* 3002 */     if (Tracer.isMediumTraceEnabled())
/* 3003 */       elementCountIndicator = Boolean.valueOf(rowset.isEmpty());
/* 3004 */     Tracer.traceExtCall("SearchScope.fetchRows", startTime, stopTime, elementCountIndicator, rowset, new Object[] { sqlStmt });
/*      */     
/* 3006 */     List<String> objStoreNames = new ArrayList();
/* 3007 */     RepositoryRow rr = null;
/* 3008 */     Properties props = null;
/* 3009 */     PageIterator jacePI = rowset.pageIterator();
/* 3010 */     while (jacePI.nextPage())
/*      */     {
/* 3012 */       Object[] currentPage = jacePI.getCurrentPage();
/* 3013 */       for (Object obj : currentPage)
/*      */       {
/* 3015 */         rr = (RepositoryRow)obj;
/* 3016 */         props = rr.getProperties();
/*      */         
/*      */         try
/*      */         {
/* 3020 */           String implClassName = props.getStringValue("ImplementingClassName");
/* 3021 */           if ((implClassName != null) && (implClassName.equals("com.filenet.rm.api.impl.RMConnectorImpl")))
/*      */           {
/* 3023 */             String repoName = props.getStringValue("RepositoryName");
/* 3024 */             if ((repoName != null) && (repoName.lastIndexOf(".") > 0))
/*      */             {
/* 3026 */               int rosExtIndex = repoName.indexOf(".");
/* 3027 */               objStoreNames.add(repoName.substring(rosExtIndex + 1));
/*      */             }
/*      */             else {
/* 3030 */               objStoreNames.add(repoName);
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Exception ex) {
/* 3035 */           String reposIdent = fpos.getObjectReference().getObjectIdentity();
/* 3036 */           throw processJaceException(ex, RMErrorCode.RAL_RETRIEVING_CONNECTOR_REGS_FAILED, new Object[] { reposIdent });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3043 */     for (String idName : objStoreNames)
/*      */     {
/* 3045 */       String id = null;
/*      */       try
/*      */       {
/* 3048 */         ObjectStore cbrOS = Factory.ObjectStore.fetchInstance(getJaceDomain(fpos), idName, CEPF_IdOnly);
/* 3049 */         id = cbrOS.get_Id().toString();
/*      */       }
/*      */       catch (Exception ex) {}
/*      */       
/* 3053 */       if ((id != null) && (!result.contains(id))) {
/* 3054 */         result.add(id);
/*      */       }
/*      */     }
/* 3057 */     Tracer.traceMethodExit(new Object[] { result });
/* 3058 */     return result;
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
/*      */   public static RMClassDescription getClassDescription(Repository repository, String classIdent, boolean allowFromCache)
/*      */   {
/* 3076 */     Tracer.traceMethodEntry(new Object[] { repository, classIdent, Boolean.valueOf(allowFromCache) });
/* 3077 */     RMClassDescription classDesc = null;
/* 3078 */     P8CE_CacheService cache = P8CE_CacheService.getInstance();
/*      */     
/* 3080 */     if (allowFromCache)
/*      */     {
/* 3082 */       classDesc = (RMClassDescription)cache.get(repository, "CD", classIdent);
/*      */     }
/*      */     
/* 3085 */     if (classDesc == null)
/*      */     {
/* 3087 */       List<RMClassDescription> classList = repository.fetchClassDescriptions(new String[] { classIdent });
/* 3088 */       if ((classList != null) && (classList.size() == 1))
/*      */       {
/* 3090 */         classDesc = (RMClassDescription)classList.get(0);
/* 3091 */         if (classDesc != null)
/*      */         {
/* 3093 */           cache.put(repository, "CD", classIdent, classDesc);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3098 */     Tracer.traceMethodExit(new Object[] { classDesc });
/* 3099 */     return classDesc;
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
/*      */   public static String fetchExportFormatXSLT(JaceBasable jaceBasableObj)
/*      */   {
/* 3113 */     Tracer.traceMethodEntry(new Object[0]);
/* 3114 */     String xsltStr = null;
/*      */     
/* 3116 */     EngineObject jaceBaseObj = jaceBasableObj.getJaceBaseObject();
/* 3117 */     if (jaceBaseObj.getProperties().isPropertyPresent("CurrentPhaseExportFormat"))
/*      */     {
/* 3119 */       Document jaceExportFormatDoc = (Document)jaceBaseObj.getProperties().getEngineObjectValue("CurrentPhaseExportFormat");
/* 3120 */       if (jaceExportFormatDoc != null)
/*      */       {
/* 3122 */         Double contentSize = jaceExportFormatDoc.get_ContentSize();
/* 3123 */         if ((contentSize != null) && (contentSize.doubleValue() > 0.0D))
/*      */         {
/* 3125 */           InputStreamReader isr = null;
/*      */           try
/*      */           {
/* 3128 */             isr = new InputStreamReader(jaceExportFormatDoc.accessContentStream(0));
/* 3129 */             StringWriter wr = new StringWriter();
/* 3130 */             char[] cbuf = new char['က'];
/* 3131 */             int charsRead = -1;
/* 3132 */             while ((charsRead = isr.read(cbuf)) != -1)
/*      */             {
/* 3134 */               wr.write(cbuf, 0, charsRead);
/*      */             }
/* 3136 */             wr.flush();
/* 3137 */             wr.close();
/*      */             
/* 3139 */             xsltStr = wr.toString();
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3148 */             if (isr != null) {
/*      */               try {
/* 3150 */                 isr.close();
/*      */               }
/*      */               catch (IOException ignored) {}
/*      */             }
/*      */             String docIdent;
/*      */           }
/*      */           catch (IOException ex)
/*      */           {
/* 3143 */             docIdent = jaceExportFormatDoc.getObjectReference().getObjectIdentity();
/* 3144 */             throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_EXPORTFORMAT_XLST_READ_FAILURE, new Object[] { docIdent });
/*      */           }
/*      */           finally
/*      */           {
/* 3148 */             if (isr != null) {
/*      */               try {
/* 3150 */                 isr.close();
/*      */               } catch (IOException ignored) {}
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3157 */     tmp243_240[0] = xsltStr;Tracer.traceMethodExit(tmp243_240);
/* 3158 */     return xsltStr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void validateCurrentPhaseAndSchedule(BaseEntity entity)
/*      */   {
/* 3169 */     JaceBasable jaceBasable = (JaceBasable)entity;
/* 3170 */     String cutoffInheritedFrom = getJacePropertyAsString(jaceBasable, "CutoffInheritedFrom");
/* 3171 */     Date cutoffDate = getJacePropertyAsDate(jaceBasable, "CutoffDate");
/* 3172 */     if ((cutoffInheritedFrom != null) && (cutoffDate != null) && (Id.isId(cutoffInheritedFrom)))
/*      */     {
/* 3174 */       if (RALDispositionLogic.isRecalculatePhaseIndicated(entity.getRepository(), entity, cutoffInheritedFrom))
/*      */       {
/* 3176 */         RALDispositionLogic.resetEntityDispositionState(entity.getProperties());
/* 3177 */         ((RALBaseEntity)entity).internalSave(RMRefreshMode.Refresh);
/*      */         
/* 3179 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_SCHEDULE_HAS_CHANGED, new Object[0]);
/*      */       }
/*      */       
/* 3182 */       if (RALDispositionLogic.hasCurrentPhaseChanged(entity, null))
/*      */       {
/* 3184 */         DispositionSchedule schedule = RALDispositionLogic.getInheritedSchedule(entity);
/* 3185 */         if (schedule != null) {
/* 3186 */           ((P8CE_DispositionScheduleImpl)schedule).updateCurrentPhaseDataOnEntity(entity);
/*      */         }
/* 3188 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_SCHEDULE_HAS_CHANGED, new Object[0]);
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
/*      */   static ContentXMLExport getSystemConfiguration_ExportConfigSetting(FilePlanRepository fpRepository)
/*      */   {
/* 3204 */     Tracer.traceMethodEntry(new Object[] { fpRepository });
/*      */     
/* 3206 */     ContentXMLExport contentExportMode = ContentXMLExport.EmbedInDocumentXML;
/* 3207 */     Map<String, SystemConfiguration> sysConfigs = fpRepository.getSystemConfigurations();
/* 3208 */     if (sysConfigs != null)
/*      */     {
/* 3210 */       SystemConfiguration sysConfigForExportConfig = (SystemConfiguration)sysConfigs.get("Export Configuration");
/* 3211 */       if (sysConfigForExportConfig != null)
/*      */       {
/* 3213 */         String rawSysConfigValue = sysConfigForExportConfig.getPropertyValue();
/*      */         try
/*      */         {
/* 3216 */           int intVal = Integer.parseInt(rawSysConfigValue, 10);
/* 3217 */           ContentXMLExport configuredMode = ContentXMLExport.getInstanceFromInt(intVal);
/* 3218 */           if (configuredMode != null) {
/* 3219 */             contentExportMode = configuredMode;
/*      */           }
/*      */         }
/*      */         catch (Exception ignored) {}
/*      */       }
/*      */     }
/* 3225 */     Tracer.traceMethodExit(new Object[] { contentExportMode });
/* 3226 */     return contentExportMode;
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
/*      */   static List<Link> findAssociatedLinks(IndependentObject memberCandidate)
/*      */   {
/* 3239 */     Tracer.traceMethodEntry(new Object[] { memberCandidate });
/* 3240 */     List<Link> results = new ArrayList(0);
/*      */     
/* 3242 */     String memberIdent = memberCandidate.getObjectReference().getObjectIdentity();
/* 3243 */     StringBuilder sb = null;
/* 3244 */     if ((memberCandidate instanceof Folder))
/*      */     {
/* 3246 */       sb = new StringBuilder();
/* 3247 */       sb.append("SELECT l.").append("Id").append(" FROM ").append("Link").append(" l ");
/* 3248 */       sb.append("INNER JOIN ").append("RMFolder").append(" rf ON l.").append("Tail").append(" = rf.This ");
/* 3249 */       sb.append("WHERE rf.").append("Id").append(" = ").append(memberIdent).append(" ");
/*      */     }
/* 3251 */     else if ((memberCandidate instanceof Document))
/*      */     {
/* 3253 */       sb = new StringBuilder();
/* 3254 */       sb.append("SELECT l.").append("Id").append(" FROM ").append("Link").append(" l ");
/* 3255 */       sb.append("INNER JOIN ").append("RecordInfo").append(" rec ON l.").append("Tail").append(" = rec.This ");
/* 3256 */       sb.append("WHERE rec.").append("Id").append(" = ").append(memberIdent).append(" ");
/*      */     }
/*      */     
/* 3259 */     if (sb != null)
/*      */     {
/* 3261 */       ObjectStore jaceObjStore = ((RepositoryObject)memberCandidate).getObjectStore();
/* 3262 */       String sql = sb.toString();
/* 3263 */       SearchSQL searchSQL = new SearchSQL(sql);
/* 3264 */       SearchScope searchScope = new SearchScope(jaceObjStore);
/* 3265 */       PropertyFilter pf = new PropertyFilter();
/* 3266 */       Integer pageSize = null;
/* 3267 */       Boolean continuable = Boolean.TRUE;
/* 3268 */       IndependentObjectSet resultSet = searchScope.fetchObjects(searchSQL, pageSize, pf, continuable);
/*      */       
/* 3270 */       Link jaceLink = null;
/* 3271 */       PageIterator jacePI = resultSet.pageIterator();
/* 3272 */       while (jacePI.nextPage())
/*      */       {
/* 3274 */         Object[] currentPage = jacePI.getCurrentPage();
/* 3275 */         for (Object obj : currentPage)
/*      */         {
/* 3277 */           jaceLink = (Link)obj;
/* 3278 */           results.add(jaceLink);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3283 */     Tracer.traceMethodExit(new Object[] { results });
/* 3284 */     return results;
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
/*      */   static boolean isAnIERRootContainer(Folder jaceFolder)
/*      */   {
/* 3301 */     Tracer.traceMethodEntry(new Object[] { jaceFolder });
/* 3302 */     boolean result = false;
/*      */     
/* 3304 */     Properties jaceProps = jaceFolder.getProperties();
/* 3305 */     if (jaceProps.isPropertyPresent("RMEntityType"))
/*      */     {
/* 3307 */       Integer rawEntityType = jaceProps.getInteger32Value("RMEntityType");
/* 3308 */       if ((rawEntityType != null) && (rawEntityType.intValue() == EntityType.FilePlan.getIntValue()))
/*      */       {
/* 3310 */         if (jaceProps.isPropertyPresent("ContainerType"))
/*      */         {
/* 3312 */           String containerType = jaceProps.getStringValue("ContainerType");
/* 3313 */           if ("application/x-filenet-rm-classificationschemeroot".equalsIgnoreCase(containerType)) {
/* 3314 */             result = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3319 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 3320 */     return result;
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
/*      */   static Domain getJaceDomain(RepositoryObject jaceObj)
/*      */   {
/* 3334 */     Tracer.traceMethodEntry(new Object[] { jaceObj });
/* 3335 */     ObjectStore jaceObjStore = jaceObj.getObjectStore();
/* 3336 */     Domain result = getJaceDomain(jaceObjStore);
/* 3337 */     Tracer.traceMethodExit(new Object[] { result });
/* 3338 */     return result;
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
/*      */   static Domain getJaceDomain(ObjectStore jaceObjStore)
/*      */   {
/* 3352 */     Tracer.traceMethodEntry(new Object[] { jaceObjStore });
/* 3353 */     Domain result = null;
/* 3354 */     if (jaceObjStore != null)
/*      */     {
/* 3356 */       if (!jaceObjStore.getProperties().isPropertyPresent("Domain"))
/*      */       {
/* 3358 */         jaceObjStore.fetchProperty("Domain", null);
/*      */       }
/* 3360 */       result = jaceObjStore.get_Domain();
/*      */     }
/*      */     
/* 3363 */     Tracer.traceMethodExit(new Object[] { result });
/* 3364 */     return result;
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
/*      */   public static void updatePropertyValueOnBatch(String propSymName, Object propValue, List<BaseEntity> batch)
/*      */   {
/* 3378 */     Tracer.traceMethodEntry(new Object[] { propSymName, propValue, batch });
/* 3379 */     if ((batch != null) && (batch.size() > 0))
/*      */     {
/* 3381 */       BaseEntity entity = (BaseEntity)batch.get(0);
/* 3382 */       RepositoryObject jaceRepObj = (RepositoryObject)((JaceBasable)entity).getJaceBaseObject();
/* 3383 */       Domain jaceDomain = getJaceDomain(jaceRepObj);
/*      */       
/* 3385 */       UpdatingBatch jaceUB = null;
/* 3386 */       IndependentlyPersistableObject jaceBase = null;
/* 3387 */       PropertyFilter jacePF = null;
/* 3388 */       int index = 0;
/* 3389 */       while (index < batch.size())
/*      */       {
/* 3391 */         if (index % 10 == 0)
/*      */         {
/* 3393 */           if ((jaceUB != null) && (jaceUB.hasPendingExecute())) {
/* 3394 */             jaceUB.updateBatch();
/*      */           }
/* 3396 */           jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, RefreshMode.REFRESH);
/*      */         }
/*      */         
/* 3399 */         entity = (BaseEntity)batch.get(index);
/* 3400 */         jaceBase = (IndependentlyPersistableObject)((JaceBasable)entity).getJaceBaseObject();
/* 3401 */         jaceBase.getProperties().putObjectValue(propSymName, propValue);
/* 3402 */         jaceUB.add(jaceBase, jacePF);
/* 3403 */         index++;
/*      */       }
/*      */       
/* 3406 */       if ((jaceUB != null) && (jaceUB.hasPendingExecute()))
/* 3407 */         jaceUB.updateBatch();
/*      */     }
/* 3409 */     Tracer.traceMethodExit(new Object[0]);
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
/*      */   public static Id processIdStr(String idStr)
/*      */   {
/* 3426 */     Tracer.traceMethodEntry(new Object[] { idStr });
/* 3427 */     Id result = null;
/* 3428 */     if ((idStr != null) && (idStr.trim().length() > 0))
/*      */     {
/*      */       try
/*      */       {
/* 3432 */         result = new Id(idStr);
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/* 3436 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_INVALID_ID_STRING, new Object[] { idStr });
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/* 3441 */       result = Id.createId();
/*      */     }
/*      */     
/* 3444 */     Tracer.traceMethodExit(new Object[] { result });
/* 3445 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void updatePhaseDataOnEntity(BaseEntity entity)
/*      */   {
/* 3457 */     Tracer.traceMethodEntry(new Object[] { entity });
/* 3458 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3461 */       establishedSubject = associateSubject();
/* 3462 */       RMProperties jarmProps = entity.getProperties();
/* 3463 */       Date cutoffDate = jarmProps.getDateTimeValue("CutoffDate");
/* 3464 */       if (cutoffDate != null)
/*      */       {
/* 3466 */         String currentPhaseId = jarmProps.getGuidValue("CurrentPhaseID");
/* 3467 */         if ((currentPhaseId != null) && (currentPhaseId.trim().length() > 0))
/*      */         {
/* 3469 */           DispositionPhase currentPhase = RALDispositionLogic.getCurrentPhaseForEntity(entity);
/* 3470 */           if (currentPhase != null)
/*      */           {
/* 3472 */             Repository repository = entity.getRepository();
/* 3473 */             String cutoffInheritedFrom = jarmProps.getGuidValue("CutoffInheritedFrom");
/* 3474 */             BaseEntity scheduleHolder = RALDispositionLogic.getAssociatedScheduleHolder(repository, cutoffInheritedFrom);
/* 3475 */             if (scheduleHolder != null)
/*      */             {
/* 3477 */               Integer recalcPhaseRetent = scheduleHolder.getProperties().getIntegerValue("RecalculatePhaseRetention");
/* 3478 */               if ((recalcPhaseRetent != null) && (recalcPhaseRetent.intValue() == 5))
/*      */               {
/*      */ 
/* 3481 */                 ((RALBaseEntity)entity).resetDispositionData();
/* 3482 */                 throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DISPOSITION_SCHEDULE_HAS_CHANGED, new Object[0]);
/*      */               }
/*      */             }
/*      */             
/* 3486 */             if (RALDispositionLogic.hasCurrentPhaseChanged(entity, currentPhase))
/*      */             {
/*      */ 
/* 3489 */               updateDataFromDisposition(entity);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 3494 */             updateDataFromDisposition(entity);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3503 */       Tracer.traceMethodExit(new Object[0]);
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 3507 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 3511 */       throw processJaceException(cause, RMErrorCode.RAL_CURRENT_PHASE_DATA_UPDATE_ERROR, new Object[] { entity.getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 3515 */       if (establishedSubject) {
/* 3516 */         disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static void updateDataFromDisposition(BaseEntity entity)
/*      */   {
/* 3528 */     Tracer.traceMethodEntry(new Object[0]);
/* 3529 */     DispositionSchedule schedule = RALDispositionLogic.getInheritedSchedule(entity);
/* 3530 */     if (schedule != null)
/*      */     {
/* 3532 */       ((P8CE_DispositionScheduleImpl)schedule).updateCurrentPhaseDataOnEntity(entity);
/*      */     }
/*      */     
/* 3535 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   public static PageableSet<AuditEvent> getAuditedEvents(BaseEntity jarmEntity, RMPropertyFilter filter)
/*      */   {
/* 3540 */     Tracer.traceMethodEntry(new Object[] { jarmEntity, filter });
/* 3541 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/* 3544 */       establishedSubject = associateSubject();
/* 3545 */       PageableSet<AuditEvent> resultSet = null;
/* 3546 */       String entityIdent = jarmEntity.getObjectIdentity();
/*      */       
/*      */ 
/*      */ 
/* 3550 */       List<FilterElement> mandatoryAuditEventFEs = P8CE_AuditEventImpl.getMandatoryJaceFEs();
/* 3551 */       PropertyFilter jacePF = convertToJacePF(filter, mandatoryAuditEventFEs);
/*      */       
/* 3553 */       StringBuilder sb = createSelectSqlFromJacePF(jacePF, null, "ObjectChangeEvent", "ae");
/* 3554 */       sb.append(" WHERE ae.").append("SourceObjectId").append(" = '").append(entityIdent).append("' ");
/* 3555 */       sb.append(" ORDER BY ae.").append("DateCreated");
/* 3556 */       String sqlStatement = sb.toString();
/*      */       
/* 3558 */       P8CE_RepositoryImpl repository = (P8CE_RepositoryImpl)jarmEntity.getRepository();
/* 3559 */       ObjectStore jaceObjStore = repository.getJaceObjectStore();
/* 3560 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 3561 */       SearchScope jaceSearchScope = new SearchScope(jaceObjStore);
/* 3562 */       long startTime = System.currentTimeMillis();
/* 3563 */       IndependentObjectSet jaceEventSet = jaceSearchScope.fetchObjects(jaceSearchSQL, null, jacePF, Boolean.TRUE);
/* 3564 */       long stopTime = System.currentTimeMillis();
/* 3565 */       Boolean elementCountIndicator = null;
/* 3566 */       if (Tracer.isMediumTraceEnabled())
/*      */       {
/* 3568 */         elementCountIndicator = jaceEventSet != null ? Boolean.valueOf(jaceEventSet.isEmpty()) : null;
/*      */       }
/* 3570 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceEventSet, new Object[] { sqlStatement, null, jacePF, Boolean.TRUE });
/*      */       
/*      */ 
/*      */ 
/* 3574 */       IGenerator<AuditEvent> generator = P8CE_AuditEventImpl.getAuditEventGenerator();
/* 3575 */       resultSet = new P8CE_PageableSetImpl(repository, jaceEventSet, true, generator);
/*      */       
/* 3577 */       Tracer.traceMethodExit(new Object[] { resultSet });
/* 3578 */       return resultSet;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 3582 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 3586 */       throw processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { jarmEntity.getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 3590 */       if (establishedSubject) {
/* 3591 */         disassociateSubject();
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
/*      */   static boolean validateDispositionExport(BaseEntity entity)
/*      */   {
/* 3606 */     Tracer.traceMethodEntry(new Object[] { entity });
/*      */     
/* 3608 */     validateCurrentPhaseAndSchedule(entity);
/*      */     
/*      */ 
/* 3611 */     boolean isPartOfInterimTransferProcess = false;
/* 3612 */     Integer rawCurrentActionType = getJacePropertyAsInteger((JaceBasable)entity, "CurrentActionType");
/* 3613 */     if ((rawCurrentActionType != null) && (rawCurrentActionType.intValue() == DispositionActionType.InterimTransfer.getIntValue()))
/*      */     {
/*      */ 
/* 3616 */       isPartOfInterimTransferProcess = true;
/*      */     }
/*      */     
/*      */ 
/* 3620 */     if (!isPartOfInterimTransferProcess) {
/* 3621 */       RALDispositionLogic.validateAttemptedAction(entity, DispositionActionType.Export);
/*      */     }
/* 3623 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(isPartOfInterimTransferProcess) });
/* 3624 */     return isPartOfInterimTransferProcess;
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
/*      */   public static Object convertJaceObjToJarmObject(Repository repository, Object obj)
/*      */   {
/* 3638 */     Tracer.traceMethodEntry(new Object[] { obj });
/* 3639 */     Object result = obj;
/* 3640 */     if ((obj != null) && ((obj instanceof IndependentObject)))
/*      */     {
/* 3642 */       IndependentObject jaceIndepObj = (IndependentObject)obj;
/* 3643 */       IGenerator<?> jarmEntityGenerator = getEntityGenerator(jaceIndepObj);
/* 3644 */       if (jarmEntityGenerator != null)
/*      */       {
/* 3646 */         result = jarmEntityGenerator.create(repository, jaceIndepObj);
/*      */       }
/*      */     }
/*      */     
/* 3650 */     Tracer.traceMethodExit(new Object[] { result });
/* 3651 */     return result;
/*      */   }
/*      */   
/*      */   static boolean isEntityInDDReport(IndependentObject jaceBase)
/*      */   {
/* 3656 */     Tracer.traceMethodEntry(new Object[] { jaceBase });
/* 3657 */     boolean result = false;
/*      */     
/* 3659 */     if (jaceBase.getProperties().isPropertyPresent("CurrentPhaseExecutionStatus"))
/*      */     {
/* 3661 */       Integer cpesVal = jaceBase.getProperties().getInteger32Value("CurrentPhaseExecutionStatus");
/* 3662 */       if (cpesVal != null)
/*      */       {
/* 3664 */         result = (cpesVal.intValue() >= 1000) && (cpesVal.intValue() <= 4000);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3669 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 3670 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   static List<Property> findDirtyProperties(Properties properties)
/*      */   {
/* 3676 */     Tracer.traceMethodEntry(new Object[] { properties });
/* 3677 */     List<Property> dirtyProperties = new ArrayList(0);
/* 3678 */     Iterator it; if (properties != null)
/*      */     {
/* 3680 */       for (it = properties.iterator(); it.hasNext();)
/*      */       {
/* 3682 */         Property prop = (Property)it.next();
/* 3683 */         if (prop.isDirty()) {
/* 3684 */           dirtyProperties.add(prop);
/*      */         }
/*      */       }
/*      */     }
/* 3688 */     Tracer.traceMethodExit(new Object[] { dirtyProperties });
/* 3689 */     return dirtyProperties;
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */