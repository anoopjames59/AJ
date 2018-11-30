/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.AuditEvent;
/*     */ import com.ibm.jarm.api.core.AuditableEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.RMCustomObject;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALBaseCustomObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMCustomObjectImpl
/*     */   extends RALBaseCustomObject
/*     */   implements RMCustomObject, JaceBasable, AuditableEntity
/*     */ {
/*  43 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  44 */   private static final IGenerator<RMCustomObject> RMCustomObjectGenerator = new Generator();
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
/*  55 */   private static final String[] MandatoryPropertyNames = { "Id", "Name", "RMEntityType" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   protected CustomObject jaceCustomObject;
/*     */   
/*  59 */   static { String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  61 */     List<FilterElement> tempList = new ArrayList(1);
/*  62 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  63 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  68 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  73 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RMCustomObject> getCustomObjectGenerator()
/*     */   {
/*  83 */     return RMCustomObjectGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  91 */     return getMandatoryJaceFEs();
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
/*     */   protected P8CE_RMCustomObjectImpl(EntityType entityType, Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/* 109 */     super(entityType, repository, identity, isPlaceholder);
/* 110 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/* 111 */     this.jaceCustomObject = jaceCustomObject;
/* 112 */     if (entityType == null)
/* 113 */       this.entityType = EntityType.CustomObject;
/* 114 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 122 */     Tracer.traceMethodEntry(new Object[0]);
/* 123 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceCustomObject);
/* 124 */     Tracer.traceMethodExit(new Object[] { result });
/* 125 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 133 */     Tracer.traceMethodEntry(new Object[0]);
/* 134 */     String result = P8CE_Util.getJaceObjectClassName(this.jaceCustomObject);
/* 135 */     Tracer.traceMethodExit(new Object[] { result });
/* 136 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/* 144 */     return this.jaceCustomObject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 152 */     Tracer.traceMethodEntry(new Object[0]);
/* 153 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 156 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 158 */       long startTime = System.currentTimeMillis();
/* 159 */       this.jaceCustomObject.delete();
/* 160 */       this.jaceCustomObject.save(RefreshMode.NO_REFRESH);
/* 161 */       long stopTime = System.currentTimeMillis();
/* 162 */       Tracer.traceExtCall("CustomObject.save", startTime, stopTime, Integer.valueOf(1), null, new Object[] { RefreshMode.NO_REFRESH });
/*     */       
/* 164 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 168 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 172 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.CustomObject });
/*     */     }
/*     */     finally
/*     */     {
/* 176 */       if (establishedSubject) {
/* 177 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 186 */     Tracer.traceMethodEntry(new Object[0]);
/* 187 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceCustomObject);
/* 188 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*     */     
/* 190 */     Tracer.traceMethodExit(new Object[] { result });
/* 191 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPermissions(List<RMPermission> jarmPerms)
/*     */   {
/* 199 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/* 200 */     Util.ckNullObjParam("jarmPerms", jarmPerms);
/*     */     
/* 202 */     AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(jarmPerms);
/* 203 */     this.jaceCustomObject.set_Permissions(jacePerms);
/*     */     
/* 205 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 213 */     Tracer.traceMethodEntry(new Object[0]);
/* 214 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 217 */       establishedSubject = P8CE_Util.associateSubject();
/* 218 */       boolean excludeDeleted = false;
/* 219 */       List<Container> result = P8CE_Util.getContainedBy(getRepository(), this.jaceCustomObject, excludeDeleted);
/*     */       
/* 221 */       Tracer.traceMethodExit(new Object[] { result });
/* 222 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 226 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 230 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_PARENT_CONTAINER_FAILED, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 234 */       if (establishedSubject) {
/* 235 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 244 */     Tracer.traceMethodEntry(new Object[0]);
/* 245 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/* 246 */     Tracer.traceMethodExit(new Object[] { result });
/* 247 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 255 */     Tracer.traceMethodEntry(new Object[0]);
/* 256 */     RMProperties result = null;
/* 257 */     if (this.jaceCustomObject != null)
/*     */     {
/* 259 */       result = new P8CE_RMPropertiesImpl(this.jaceCustomObject, this);
/*     */     }
/*     */     
/* 262 */     Tracer.traceMethodExit(new Object[] { result });
/* 263 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<AuditEvent> getAuditedEvents(RMPropertyFilter filter)
/*     */   {
/* 271 */     Tracer.traceMethodEntry(new Object[] { filter });
/* 272 */     PageableSet<AuditEvent> resultSet = P8CE_Util.getAuditedEvents(this, filter);
/* 273 */     Tracer.traceMethodExit(new Object[] { resultSet });
/* 274 */     return resultSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 282 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 283 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 286 */       establishedSubject = P8CE_Util.associateSubject();
/* 287 */       internalSave(jarmRefreshMode);
/* 288 */       this.isCreationPending = false;
/* 289 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 293 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 297 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), getEntityType() });
/*     */     }
/*     */     finally
/*     */     {
/* 301 */       if (establishedSubject) {
/* 302 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 311 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 312 */     RefreshMode jaceRefreshMode = P8CE_Util.convertToJaceRefreshMode(jarmRefreshMode);
/*     */     
/* 314 */     long startTime = System.currentTimeMillis();
/* 315 */     this.jaceCustomObject.save(jaceRefreshMode);
/* 316 */     Tracer.traceExtCall("Document.save()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceRefreshMode, new Object[0]);
/*     */     
/* 318 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 327 */     return "P8CE_RMCustomObjectImpl EntityType: " + getEntityType() + ", Ident: " + getObjectIdentity();
/*     */   }
/*     */   
/*     */   protected String toString(String prefix, String namePropertySymName)
/*     */   {
/* 332 */     String ident = "<unknown>";
/* 333 */     if ((namePropertySymName != null) && (this.jaceCustomObject.getProperties().isPropertyPresent(namePropertySymName))) {
/* 334 */       ident = this.jaceCustomObject.getProperties().getStringValue(namePropertySymName);
/*     */     } else {
/* 336 */       ident = getObjectIdentity();
/*     */     }
/* 338 */     return prefix + ": " + ident;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 346 */     Tracer.traceMethodEntry(new Object[0]);
/* 347 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 350 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 352 */       long startTime = System.currentTimeMillis();
/* 353 */       this.jaceCustomObject.refresh();
/* 354 */       long endTime = System.currentTimeMillis();
/* 355 */       Tracer.traceExtCall("CustomObject.refresh", startTime, endTime, null, null, new Object[0]);
/*     */       
/* 357 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 361 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 365 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 369 */       if (establishedSubject) {
/* 370 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 379 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 380 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 383 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 387 */       List<FilterElement> mandatoryRecordFEs = getMandatoryJaceFEs();
/* 388 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 390 */       long startTime = System.currentTimeMillis();
/* 391 */       this.jaceCustomObject.refresh(jacePF);
/* 392 */       long endTime = System.currentTimeMillis();
/* 393 */       Tracer.traceExtCall("CustomObject.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 395 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 399 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 403 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 407 */       if (establishedSubject) {
/* 408 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 417 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 418 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 421 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 424 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 425 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 426 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 427 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 428 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 430 */       long startTime = System.currentTimeMillis();
/* 431 */       this.jaceCustomObject.refresh(jacePF);
/* 432 */       long endTime = System.currentTimeMillis();
/* 433 */       Tracer.traceExtCall("CustomObject.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 435 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 439 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 443 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 447 */       if (establishedSubject) {
/* 448 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 457 */     return this.jaceCustomObject.getAccessAllowed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve()
/*     */   {
/* 466 */     Tracer.traceMethodEntry(new Object[0]);
/* 467 */     if (this.isPlaceholder)
/*     */     {
/* 469 */       P8CE_Util.refreshWithMandatoryProperties(this);
/* 470 */       this.isPlaceholder = false;
/*     */     }
/* 472 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   protected <I> I fetchSVObjPropValue(List<FilterElement> jaceFEs, String propSymName, IGenerator<I> generator)
/*     */   {
/* 489 */     Tracer.traceMethodEntry(new Object[] { jaceFEs, propSymName, generator });
/* 490 */     I result = P8CE_Util.fetchSVObjPropValue(this.repository, this.jaceCustomObject, jaceFEs, propSymName, generator);
/* 491 */     Tracer.traceMethodExit(new Object[] { result });
/* 492 */     return result;
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
/*     */   protected <I> List<I> fetchMVObjPropValueAsList(List<FilterElement> jaceFEs, String propSymName, IGenerator<I> generator)
/*     */   {
/* 509 */     Tracer.traceMethodEntry(new Object[] { jaceFEs, propSymName, generator });
/* 510 */     List<I> resultList = P8CE_Util.fetchMVObjPropValueAsList(this.repository, this.jaceCustomObject, jaceFEs, propSymName, generator);
/* 511 */     Tracer.traceMethodExit(new Object[] { resultList });
/* 512 */     return resultList;
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
/*     */   protected <I> PageableSet<I> fetchMVObjPropValueAsPageableSet(List<FilterElement> jaceFEs, String propSymName, IGenerator<I> generator)
/*     */   {
/* 529 */     Tracer.traceMethodEntry(new Object[] { jaceFEs, propSymName, generator });
/* 530 */     PageableSet<I> resultSet = P8CE_Util.fetchMVObjPropValueAsPageableSet(this.repository, this.jaceCustomObject, jaceFEs, propSymName, generator);
/* 531 */     Tracer.traceMethodExit(new Object[] { resultSet });
/* 532 */     return resultSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 540 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RMCustomObject>
/*     */   {
/*     */     public RMCustomObject create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 553 */       P8CE_RMCustomObjectImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 554 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 556 */       EntityType entityType = EntityType.CustomObject;
/* 557 */       if ((jaceBaseObject != null) && ((jaceBaseObject instanceof EngineObject)))
/*     */       {
/* 559 */         Properties jaceProps = ((EngineObject)jaceBaseObject).getProperties();
/* 560 */         if (jaceProps.isPropertyPresent("RMEntityType"))
/*     */         {
/* 562 */           Integer rawEntityType = jaceProps.getInteger32Value("RMEntityType");
/* 563 */           entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*     */         }
/*     */       }
/*     */       
/* 567 */       String identity = P8CE_Util.getJaceObjectIdentity(jaceCustomObj);
/* 568 */       RMCustomObject result = new P8CE_RMCustomObjectImpl(entityType, repository, identity, jaceCustomObj, false);
/*     */       
/* 570 */       P8CE_RMCustomObjectImpl.Tracer.traceMethodExit(new Object[] { result });
/* 571 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMCustomObjectImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */