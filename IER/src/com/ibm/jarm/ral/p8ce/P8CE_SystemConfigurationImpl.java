/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.ClassDescriptionSet;
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.core.SystemConfiguration;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_SystemConfigurationImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements SystemConfiguration
/*     */ {
/*  41 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  42 */   private static final IGenerator<SystemConfiguration> SystemConfigurationGenerator = new Generator();
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
/*     */ 
/*  63 */   private static final String[] MandatoryPropertyNames = { "Id", "PropertyName", "PropertyDataType", "PropertyValue", "DefaultValue", "RMEntityType" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   private static final String SQL_SYSCONFIG_OBJS_QUERY;
/*     */   
/*     */   static {
/*  68 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  70 */     List<FilterElement> tempList = new ArrayList(1);
/*  71 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  72 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */     
/*  74 */     StringBuilder sb = new StringBuilder();
/*  75 */     sb.append("SELECT ");
/*  76 */     sb.append("sc.[").append("Id").append(']');
/*  77 */     sb.append(", sc.[").append("PropertyName").append(']');
/*  78 */     sb.append(", sc.[").append("PropertyDataType").append(']');
/*  79 */     sb.append(", sc.[").append("PropertyValue").append(']');
/*  80 */     sb.append(", sc.[").append("DefaultValue").append(']');
/*  81 */     sb.append(", sc.[").append("RMEntityType").append(']');
/*  82 */     sb.append(" FROM [").append("SystemConfiguration").append("] sc ");
/*  83 */     sb.append("INNER JOIN ").append("ReferentialContainmentRelationship").append(" rcr ON sc.This = rcr.Head ");
/*  84 */     sb.append("WHERE rcr.Tail = OBJECT('").append("/Records Management/RMMaster/SystemConfiguration").append("') ");
/*  85 */     SQL_SYSCONFIG_OBJS_QUERY = sb.toString();
/*     */     
/*  87 */     UpdatableMap = new HashMap();
/*  88 */     UpdatableMap.put("Allow Record Multi-Filing", Boolean.TRUE);
/*  89 */     UpdatableMap.put("Export Configuration", Boolean.TRUE);
/*  90 */     UpdatableMap.put("Export Transfer Mapping", Boolean.TRUE);
/*  91 */     UpdatableMap.put("FPOS Setup", Boolean.FALSE);
/*  92 */     UpdatableMap.put("Max Declassification Offset", Boolean.TRUE);
/*  93 */     UpdatableMap.put("Maximum Batch Size For Workflows", Boolean.TRUE);
/*  94 */     UpdatableMap.put("RM Version Info", Boolean.FALSE);
/*  95 */     UpdatableMap.put("Request Default Declassification Date Update", Boolean.TRUE);
/*  96 */     UpdatableMap.put("Screening Workflow", Boolean.TRUE);
/*  97 */     UpdatableMap.put("Security Script Run Date", Boolean.FALSE);
/*  98 */     UpdatableMap.put("Volume Pattern Suffix", Boolean.TRUE);
/*     */   }
/*     */   
/*     */ 
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/* 104 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/* 109 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<SystemConfiguration> getGenerator()
/*     */   {
/* 119 */     return SystemConfigurationGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 127 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final String SysConfigContainerPath = "/Records Management/RMMaster/SystemConfiguration";
/*     */   
/*     */ 
/*     */ 
/*     */   static final Map<String, Boolean> UpdatableMap;
/*     */   
/*     */ 
/*     */   P8CE_SystemConfigurationImpl(Repository repository, String ident, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/* 142 */     super(EntityType.SystemConfiguration, repository, ident, jaceCustomObject, isPlaceholder);
/* 143 */     Tracer.traceMethodEntry(new Object[] { repository, ident, jaceCustomObject });
/* 144 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 153 */     Tracer.traceMethodEntry(new Object[0]);
/* 154 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 157 */       establishedSubject = P8CE_Util.associateSubject();
/* 158 */       boolean excludeDeleted = false;
/* 159 */       List<Container> result = P8CE_Util.getContainedBy(getRepository(), this.jaceCustomObject, excludeDeleted);
/*     */       
/* 161 */       Tracer.traceMethodExit(new Object[] { result });
/* 162 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 166 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 170 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_PARENT_CONTAINER_FAILED, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 174 */       if (establishedSubject) {
/* 175 */         P8CE_Util.disassociateSubject();
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
/*     */   static boolean isSysConfigClassDefined(ObjectStore jaceObjectStore)
/*     */   {
/* 189 */     Tracer.traceMethodEntry(new Object[] { jaceObjectStore });
/* 190 */     SearchScope jaceSearchScope = new SearchScope(jaceObjectStore);
/* 191 */     String[] desiredClasses = { "SystemConfiguration" };
/* 192 */     PropertyFilter pf = P8CE_Util.CEPF_IdOnly;
/*     */     
/* 194 */     long startTime = System.currentTimeMillis();
/* 195 */     ClassDescriptionSet resultSet = jaceSearchScope.fetchSearchableClassDescriptions(desiredClasses, pf);
/* 196 */     long stopTime = System.currentTimeMillis();
/* 197 */     Boolean elementCountIndicator = null;
/* 198 */     if (Tracer.isMediumTraceEnabled())
/*     */     {
/* 200 */       elementCountIndicator = resultSet != null ? Boolean.valueOf(resultSet.isEmpty()) : null;
/*     */     }
/*     */     
/* 203 */     Tracer.traceExtCall("SearchScope.fetchSearchableClassDescriptions()", startTime, stopTime, elementCountIndicator, resultSet, new Object[] { "SystemConfiguration" });
/*     */     
/*     */ 
/* 206 */     boolean result = (resultSet != null) && (!resultSet.isEmpty());
/*     */     
/* 208 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 209 */     return result;
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
/*     */   static Map<String, SystemConfiguration> loadSystemConfigurations(P8CE_FilePlanRepositoryImpl fpRepos)
/*     */   {
/* 223 */     Tracer.traceMethodEntry(new Object[] { fpRepos });
/* 224 */     Map<String, SystemConfiguration> sysConfigMap = new HashMap();
/*     */     
/* 226 */     PropertyFilter pf = new PropertyFilter();
/* 227 */     for (FilterElement fe : MandatoryJaceFEs)
/*     */     {
/* 229 */       pf.addIncludeProperty(fe);
/*     */     }
/*     */     
/* 232 */     SearchScope jaceSearchScope = new SearchScope(fpRepos.getJaceObjectStore());
/* 233 */     SearchSQL jaceSearchSQL = new SearchSQL(SQL_SYSCONFIG_OBJS_QUERY);
/* 234 */     Integer pageSize = null;
/* 235 */     Boolean continuable = Boolean.TRUE;
/*     */     
/* 237 */     long startTime = System.currentTimeMillis();
/* 238 */     IndependentObjectSet jaceIndepObjSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, pf, continuable);
/* 239 */     long stopTime = System.currentTimeMillis();
/*     */     
/* 241 */     SystemConfiguration sysConfig = null;
/* 242 */     CustomObject jaceCustomObj = null;
/* 243 */     String ident = null;
/* 244 */     String propertyName = null;
/* 245 */     int resultCount = 0;
/* 246 */     PageIterator jacePI = jaceIndepObjSet.pageIterator();
/* 247 */     while (jacePI.nextPage())
/*     */     {
/* 249 */       Object[] currentPage = jacePI.getCurrentPage();
/* 250 */       for (Object obj : currentPage)
/*     */       {
/* 252 */         jaceCustomObj = (CustomObject)obj;
/* 253 */         ident = jaceCustomObj.get_Id().toString();
/* 254 */         sysConfig = new P8CE_SystemConfigurationImpl(fpRepos, ident, jaceCustomObj, false);
/* 255 */         propertyName = sysConfig.getPropertyName();
/*     */         
/* 257 */         if (propertyName != null)
/* 258 */           sysConfigMap.put(propertyName, sysConfig);
/* 259 */         resultCount++;
/*     */       }
/*     */     }
/*     */     
/* 263 */     Object elementCountIndicator = null;
/* 264 */     if (Tracer.isMediumTraceEnabled())
/*     */     {
/* 266 */       elementCountIndicator = Integer.valueOf(resultCount);
/*     */     }
/* 268 */     Tracer.traceExtCall("SearchScope.fetchObjects()", startTime, stopTime, elementCountIndicator, jaceSearchSQL, new Object[] { pageSize, pf, continuable });
/*     */     
/*     */ 
/* 271 */     Tracer.traceMethodExit(new Object[] { sysConfigMap });
/* 272 */     return sysConfigMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyName()
/*     */   {
/* 282 */     Tracer.traceMethodEntry(new Object[0]);
/* 283 */     String result = P8CE_Util.getJacePropertyAsString(this, "PropertyName");
/* 284 */     Tracer.traceMethodExit(new Object[] { result });
/* 285 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyDataType()
/*     */   {
/* 295 */     Tracer.traceMethodEntry(new Object[0]);
/* 296 */     String result = P8CE_Util.getJacePropertyAsString(this, "PropertyDataType");
/* 297 */     Tracer.traceMethodExit(new Object[] { result });
/* 298 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyValue()
/*     */   {
/* 308 */     Tracer.traceMethodEntry(new Object[0]);
/* 309 */     String result = P8CE_Util.getJacePropertyAsString(this, "PropertyValue");
/* 310 */     Tracer.traceMethodExit(new Object[] { result });
/* 311 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultSettings()
/*     */   {
/* 321 */     Tracer.traceMethodEntry(new Object[0]);
/* 322 */     String result = P8CE_Util.getJacePropertyAsString(this, "DefaultValue");
/* 323 */     Tracer.traceMethodExit(new Object[] { result });
/* 324 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeUpdated()
/*     */   {
/* 332 */     Tracer.traceMethodEntry(new Object[0]);
/* 333 */     boolean result = canBeUpdated(getPropertyName());
/* 334 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*     */     
/* 336 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean canBeUpdated(String propertyName)
/*     */   {
/* 341 */     Tracer.traceMethodEntry(new Object[0]);
/* 342 */     Boolean tblValue = (Boolean)UpdatableMap.get(propertyName);
/* 343 */     boolean result = !Boolean.FALSE.equals(tblValue);
/* 344 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*     */     
/* 346 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 355 */     Tracer.traceMethodEntry(new Object[0]);
/* 356 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "delete", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 365 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 366 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "save", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPermissions(List<RMPermission> jarmPerms)
/*     */   {
/* 375 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/* 376 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "setPermissions", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 385 */     return "P8CE_SystemConfiguration PropertyName: '" + getPropertyName() + "', PropertyValue: '" + getPropertyValue() + "'";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<SystemConfiguration>
/*     */   {
/*     */     public SystemConfiguration create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 400 */       P8CE_SystemConfigurationImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 401 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 403 */       String identity = P8CE_Util.getJaceObjectIdentity(jaceCustomObj);
/* 404 */       SystemConfiguration result = new P8CE_SystemConfigurationImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 406 */       P8CE_SystemConfigurationImpl.Tracer.traceMethodExit(new Object[] { result });
/* 407 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_SystemConfigurationImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */