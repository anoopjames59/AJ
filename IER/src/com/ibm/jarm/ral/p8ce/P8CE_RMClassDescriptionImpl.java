/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.DependentObjectList;
/*     */ import com.filenet.api.collection.PropertyDescriptionList;
/*     */ import com.filenet.api.constants.FilteredPropertyType;
/*     */ import com.filenet.api.constants.PropertyState;
/*     */ import com.filenet.api.meta.ClassDescription;
/*     */ import com.filenet.api.meta.PropertyDescription;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.Property;
/*     */ import com.filenet.api.property.PropertyDependentObjectList;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
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
/*     */ class P8CE_RMClassDescriptionImpl
/*     */   implements RMClassDescription
/*     */ {
/*  41 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  42 */   private static final IGenerator<RMClassDescription> RMClassDescGenerator = new Generator();
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
/*  57 */   static final String[] MandatoryPropertyNames = { "AllowsInstances", "HasProperSubclassProperties", "HasIncludeSubclasses", "DescriptiveText", "DisplayName", "Id", "SymbolicName", "IsCBREnabled", "IsHidden", "DefaultInstanceOwner", "NamePropertyIndex", "Name" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   private static final PropertyFilter PF_DefaultInstancePermissions;
/*     */   private static final PropertyFilter PF_SuperclassPropertyCount;
/*     */   private static final PropertyFilter PF_PropertyDescriptions;
/*     */   private ClassDescription jaceClassDesc;
/*     */   private Repository repository;
/*     */   private LinkedHashMap<String, RMPropertyDescription> propDescsMap;
/*     */   
/*     */   static {
/*  67 */     List<FilterElement> tempList = new ArrayList(2);
/*  68 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  69 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null));
/*  70 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */     
/*  72 */     PF_DefaultInstancePermissions = new PropertyFilter();
/*  73 */     PF_DefaultInstancePermissions.addIncludeProperty(1, null, Boolean.FALSE, "DefaultInstancePermissions", null);
/*  74 */     for (FilterElement fe : P8CE_RMPermissionImpl.getMandatoryJaceFEs())
/*     */     {
/*  76 */       PF_DefaultInstancePermissions.addIncludeProperty(fe);
/*     */     }
/*     */     
/*  79 */     PF_SuperclassPropertyCount = new PropertyFilter();
/*  80 */     PF_SuperclassPropertyCount.addIncludeProperty(1, null, Boolean.FALSE, "SuperclassPropertyCount", null);
/*     */     
/*  82 */     PF_PropertyDescriptions = new PropertyFilter();
/*  83 */     PF_PropertyDescriptions.addIncludeType(1, null, Boolean.FALSE, FilteredPropertyType.ANY, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static String[] getMandatoryPropertyNames()
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
/*     */   static IGenerator<RMClassDescription> getGenerator()
/*     */   {
/* 104 */     return RMClassDescGenerator;
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
/*     */   P8CE_RMClassDescriptionImpl(Repository repository, ClassDescription jaceClassDesc)
/*     */   {
/* 120 */     Tracer.traceMethodEntry(new Object[] { repository, jaceClassDesc });
/* 121 */     this.repository = repository;
/* 122 */     this.jaceClassDesc = jaceClassDesc;
/* 123 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean allowsInstances()
/*     */   {
/* 131 */     Tracer.traceMethodEntry(new Object[0]);
/* 132 */     Boolean jaceBool = this.jaceClassDesc.get_AllowsInstances();
/* 133 */     boolean result = jaceBool != null ? jaceBool.booleanValue() : false;
/* 134 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 135 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canIncludeDescendentPropertiesInQuery()
/*     */   {
/* 144 */     Tracer.traceMethodEntry(new Object[0]);
/* 145 */     Boolean jaceBool = this.jaceClassDesc.get_HasProperSubclassProperties();
/* 146 */     boolean result = jaceBool != null ? jaceBool.booleanValue() : false;
/* 147 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 148 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canIncludeSubclassesInQuery()
/*     */   {
/* 157 */     Tracer.traceMethodEntry(new Object[0]);
/* 158 */     Boolean jaceBool = this.jaceClassDesc.get_HasIncludeSubclasses();
/* 159 */     boolean result = jaceBool != null ? jaceBool.booleanValue() : false;
/* 160 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 161 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPropertyDescription> getAllDescendentPropertyDescriptions()
/*     */   {
/* 170 */     Tracer.traceMethodEntry(new Object[0]);
/* 171 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 174 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 176 */       List<RMPropertyDescription> resultList = new ArrayList();
/* 177 */       PropertyFilter jacePF = new PropertyFilter();
/* 178 */       jacePF.addIncludeProperty(0, null, null, "ProperSubclassPropertyDescriptions", null);
/* 179 */       Property jaceProp = P8CE_Util.getOrFetchJaceProperty(this.jaceClassDesc, "ProperSubclassPropertyDescriptions", jacePF);
/* 180 */       DependentObjectList jacePropDescList; if ((jaceProp != null) && ((jaceProp instanceof PropertyDependentObjectList)))
/*     */       {
/* 182 */         jacePropDescList = jaceProp.getDependentObjectListValue();
/* 183 */         if (jacePropDescList != null)
/*     */         {
/* 185 */           PropertyDescription jacePropDesc = null;
/* 186 */           RMPropertyDescription jarmPropDesc = null;
/* 187 */           for (int i = 0; i < jacePropDescList.size(); i++)
/*     */           {
/* 189 */             jacePropDesc = (PropertyDescription)jacePropDescList.get(i);
/* 190 */             jarmPropDesc = P8CE_RMPropertyDescriptionImpl.create(this.repository, jacePropDesc);
/* 191 */             resultList.add(jarmPropDesc);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 196 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 197 */       return resultList;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 201 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 205 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "ProperSubclassPropertyDescriptions" });
/*     */     }
/*     */     finally
/*     */     {
/* 209 */       if (establishedSubject) {
/* 210 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultInstanceOwner()
/*     */   {
/* 220 */     return this.jaceClassDesc.get_DefaultInstanceOwner();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getDefaultInstancePermissions()
/*     */   {
/* 229 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 231 */     checkAndFetchClassDescProperty("DefaultInstancePermissions", PF_DefaultInstancePermissions);
/* 232 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(this.jaceClassDesc.get_DefaultInstancePermissions());
/*     */     
/* 234 */     Tracer.traceMethodExit(new Object[] { result });
/* 235 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescriptiveText()
/*     */   {
/* 244 */     return this.jaceClassDesc.get_DescriptiveText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 253 */     return this.jaceClassDesc.get_DisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 262 */     Tracer.traceMethodEntry(new Object[0]);
/* 263 */     String result = null;
/*     */     
/* 265 */     Id cdId = this.jaceClassDesc.get_Id();
/* 266 */     if (cdId != null) {
/* 267 */       result = cdId.toString();
/*     */     }
/* 269 */     Tracer.traceMethodExit(new Object[] { result });
/* 270 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getImmediateInheritedPropertyCount()
/*     */   {
/* 279 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 281 */     checkAndFetchClassDescProperty("SuperclassPropertyCount", PF_SuperclassPropertyCount);
/* 282 */     Integer result = this.jaceClassDesc.get_SuperclassPropertyCount();
/*     */     
/* 284 */     Tracer.traceMethodExit(new Object[] { result });
/* 285 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMClassDescription> getImmediateSubclassDescriptions()
/*     */   {
/* 294 */     Tracer.traceMethodEntry(new Object[0]);
/* 295 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 298 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 300 */       List<FilterElement> jaceClassDescFEs = getMandatoryJaceFEs();
/* 301 */       IGenerator<RMClassDescription> generator = getGenerator();
/* 302 */       List<RMClassDescription> results = P8CE_Util.fetchMVObjPropValueAsList(this.repository, this.jaceClassDesc, jaceClassDescFEs, "ImmediateSubclassDescriptions", generator);
/*     */       
/* 304 */       Tracer.traceMethodExit(new Object[] { results });
/* 305 */       return results;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 309 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 313 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "ImmediateSubclassDescriptions" });
/*     */     }
/*     */     finally
/*     */     {
/* 317 */       if (establishedSubject) {
/* 318 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 328 */     return this.jaceClassDesc.get_Name();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getNamePropertyIndex()
/*     */   {
/* 337 */     return this.jaceClassDesc.get_NamePropertyIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPropertyDescription> getPropertyDescriptions()
/*     */   {
/* 346 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 348 */     List<RMPropertyDescription> result = null;
/* 349 */     result = new ArrayList(getPropertyDescriptionsMap().values());
/*     */     
/* 351 */     Tracer.traceMethodExit(new Object[] { result });
/* 352 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMPropertyDescription getPropertyDescription(String symbolicName)
/*     */   {
/* 360 */     Tracer.traceMethodEntry(new Object[0]);
/* 361 */     RMPropertyDescription result = (RMPropertyDescription)getPropertyDescriptionsMap().get(symbolicName);
/* 362 */     Tracer.traceMethodExit(new Object[] { result });
/* 363 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMClassDescription getSuperclassDescription()
/*     */   {
/* 371 */     Tracer.traceMethodEntry(new Object[0]);
/* 372 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 375 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 377 */       List<FilterElement> jaceClassDescFEs = getMandatoryJaceFEs();
/* 378 */       IGenerator<RMClassDescription> generator = getGenerator();
/* 379 */       RMClassDescription result = (RMClassDescription)P8CE_Util.fetchSVObjPropValue(this.repository, this.jaceClassDesc, jaceClassDescFEs, "SuperclassDescription", generator);
/*     */       
/* 381 */       Tracer.traceMethodExit(new Object[] { result });
/* 382 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 386 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 390 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "SuperclassDescription" });
/*     */     }
/*     */     finally
/*     */     {
/* 394 */       if (establishedSubject) {
/* 395 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSymbolicName()
/*     */   {
/* 404 */     return this.jaceClassDesc.get_SymbolicName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCBREnabled()
/*     */   {
/* 413 */     Tracer.traceMethodEntry(new Object[0]);
/* 414 */     Boolean jaceBool = this.jaceClassDesc.get_IsCBREnabled();
/* 415 */     boolean result = jaceBool != null ? jaceBool.booleanValue() : false;
/* 416 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 417 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHidden()
/*     */   {
/* 426 */     Tracer.traceMethodEntry(new Object[0]);
/* 427 */     Boolean jaceBool = this.jaceClassDesc.get_IsHidden();
/* 428 */     boolean result = jaceBool != null ? jaceBool.booleanValue() : false;
/* 429 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 430 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean describedIsOfClass(String classSymName)
/*     */   {
/* 438 */     Tracer.traceMethodEntry(new Object[] { classSymName });
/* 439 */     Util.ckInvalidStrParam("classSymName", classSymName);
/* 440 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 443 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 445 */       boolean result = this.jaceClassDesc.describedIsOfClass(classSymName).booleanValue();
/* 446 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 447 */       return result;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 451 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "SuperclassDescription" });
/*     */     }
/*     */     finally
/*     */     {
/* 455 */       if (establishedSubject) {
/* 456 */         P8CE_Util.disassociateSubject();
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
/*     */   private void checkAndFetchClassDescProperty(String propSymName, PropertyFilter pf)
/*     */   {
/* 471 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 473 */     boolean needToFetch = true;
/* 474 */     Properties jaceProps = this.jaceClassDesc.getProperties();
/* 475 */     if (jaceProps.isPropertyPresent(propSymName))
/*     */     {
/*     */ 
/* 478 */       Property jaceProp = jaceProps.get(propSymName);
/* 479 */       needToFetch = PropertyState.UNEVALUATED.equals(jaceProp.getState());
/*     */     }
/*     */     
/* 482 */     if (needToFetch)
/*     */     {
/* 484 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 487 */         establishedSubject = P8CE_Util.associateSubject();
/* 488 */         P8CE_Util.fetchAdditionalJaceProperties(this.jaceClassDesc, pf);
/*     */       }
/*     */       catch (RMRuntimeException ex)
/*     */       {
/* 492 */         throw ex;
/*     */       }
/*     */       catch (Exception cause)
/*     */       {
/* 496 */         throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_CLASSDESCRIPTION_FAILED, new Object[] { "?", getSymbolicName() });
/*     */       }
/*     */       finally
/*     */       {
/* 500 */         if (establishedSubject)
/* 501 */           P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/* 504 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   private synchronized LinkedHashMap<String, RMPropertyDescription> getPropertyDescriptionsMap()
/*     */   {
/* 518 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 520 */     if (this.propDescsMap == null)
/*     */     {
/* 522 */       this.propDescsMap = new LinkedHashMap();
/* 523 */       checkAndFetchClassDescProperty("PropertyDescriptions", PF_PropertyDescriptions);
/* 524 */       PropertyDescriptionList jacePropDescs = this.jaceClassDesc.get_PropertyDescriptions();
/* 525 */       if (jacePropDescs != null)
/*     */       {
/* 527 */         String symName = null;
/* 528 */         PropertyDescription jacePropDesc = null;
/* 529 */         RMPropertyDescription jarmPropDesc = null;
/* 530 */         Iterator<PropertyDescription> it = jacePropDescs.iterator();
/* 531 */         while ((it != null) && (it.hasNext()))
/*     */         {
/* 533 */           jacePropDesc = (PropertyDescription)it.next();
/* 534 */           if (jacePropDesc != null)
/*     */           {
/* 536 */             symName = jacePropDesc.get_SymbolicName();
/* 537 */             jarmPropDesc = P8CE_RMPropertyDescriptionImpl.create(this.repository, jacePropDesc);
/* 538 */             this.propDescsMap.put(symName, jarmPropDesc);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 544 */     Tracer.traceMethodExit(new Object[] { this.propDescsMap });
/* 545 */     return this.propDescsMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RMClassDescription>
/*     */   {
/*     */     public RMClassDescription create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 559 */       P8CE_RMClassDescriptionImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 560 */       ClassDescription jaceClassDesc = (ClassDescription)jaceBaseObject;
/*     */       
/* 562 */       RMClassDescription result = new P8CE_RMClassDescriptionImpl(repository, jaceClassDesc);
/*     */       
/* 564 */       P8CE_RMClassDescriptionImpl.Tracer.traceMethodExit(new Object[] { result });
/* 565 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMClassDescriptionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */