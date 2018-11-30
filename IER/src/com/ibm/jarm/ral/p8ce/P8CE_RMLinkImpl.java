/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.Link;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.RMLink;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALBaseRMLink;
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
/*     */ class P8CE_RMLinkImpl
/*     */   extends RALBaseRMLink
/*     */   implements RMLink, JaceBasable
/*     */ {
/*  42 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  43 */   private static final IGenerator<RMLink> RMLinkGenerator = new Generator();
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
/*  54 */   private static final String[] MandatoryPropertyNames = { "Id", "LinkName", "RMEntityType", "RMEntityDescription", "Head", "Tail" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   protected Link jaceLink;
/*     */   
/*     */   static {
/*  59 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
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
/*     */   static IGenerator<RMLink> getRMLinkGenerator()
/*     */   {
/*  83 */     return RMLinkGenerator;
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
/*     */   protected P8CE_RMLinkImpl(EntityType entityType, Repository repository, String identity, Link jaceLink, boolean isPlaceholder)
/*     */   {
/* 109 */     super(entityType, repository, identity, isPlaceholder);
/* 110 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, jaceLink, Boolean.valueOf(isPlaceholder) });
/* 111 */     this.jaceLink = jaceLink;
/* 112 */     if (entityType == null) {
/* 113 */       this.entityType = EntityType.RMLink;
/*     */     }
/* 115 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLinkName()
/*     */   {
/* 123 */     Tracer.traceMethodEntry(new Object[0]);
/* 124 */     String result = P8CE_Util.getJacePropertyAsString(this, "LinkName");
/* 125 */     Tracer.traceMethodExit(new Object[] { result });
/* 126 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 134 */     Tracer.traceMethodEntry(new Object[0]);
/* 135 */     String result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/* 136 */     Tracer.traceMethodExit(new Object[] { result });
/* 137 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseEntity getHead()
/*     */   {
/* 145 */     Tracer.traceMethodEntry(new Object[0]);
/* 146 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 149 */       establishedSubject = P8CE_Util.associateSubject();
/* 150 */       EngineObject jaceEngObj = P8CE_Util.getJacePropertyAsEngineObject(this, "Head");
/* 151 */       BaseEntity result = convertEngObjToBaseEntity(jaceEngObj);
/*     */       
/* 153 */       Tracer.traceMethodExit(new Object[] { result });
/* 154 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 158 */       if (establishedSubject) {
/* 159 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BaseEntity getTail()
/*     */   {
/* 168 */     Tracer.traceMethodEntry(new Object[0]);
/* 169 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 172 */       establishedSubject = P8CE_Util.associateSubject();
/* 173 */       EngineObject jaceEngObj = P8CE_Util.getJacePropertyAsEngineObject(this, "Tail");
/* 174 */       BaseEntity result = convertEngObjToBaseEntity(jaceEngObj);
/*     */       
/* 176 */       Tracer.traceMethodExit(new Object[] { result });
/* 177 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 181 */       if (establishedSubject) {
/* 182 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private BaseEntity convertEngObjToBaseEntity(EngineObject jaceEngObj) {
/* 188 */     Tracer.traceMethodEntry(new Object[] { jaceEngObj });
/* 189 */     BaseEntity result = null;
/* 190 */     if ((jaceEngObj != null) && ((jaceEngObj instanceof IndependentObject)))
/*     */     {
/* 192 */       IndependentObject jaceIndepObj = (IndependentObject)jaceEngObj;
/* 193 */       IGenerator<?> jarmEntityGenerator = P8CE_Util.getEntityGenerator(jaceIndepObj);
/* 194 */       if (jarmEntityGenerator != null)
/*     */       {
/* 196 */         Repository repository = getRepository();
/* 197 */         result = (BaseEntity)jarmEntityGenerator.create(repository, jaceIndepObj);
/*     */       }
/*     */     }
/*     */     
/* 201 */     Tracer.traceMethodExit(new Object[] { result });
/* 202 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 210 */     Tracer.traceMethodEntry(new Object[0]);
/* 211 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceLink);
/* 212 */     Tracer.traceMethodExit(new Object[] { result });
/* 213 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 221 */     Tracer.traceMethodEntry(new Object[0]);
/* 222 */     String result = P8CE_Util.getJaceObjectClassName(this.jaceLink);
/* 223 */     Tracer.traceMethodExit(new Object[] { result });
/* 224 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/* 232 */     return this.jaceLink;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 240 */     Tracer.traceMethodEntry(new Object[0]);
/* 241 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 244 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 246 */       long startTime = System.currentTimeMillis();
/* 247 */       this.jaceLink.delete();
/* 248 */       this.jaceLink.save(RefreshMode.NO_REFRESH);
/* 249 */       long stopTime = System.currentTimeMillis();
/* 250 */       Tracer.traceExtCall("Link.save", startTime, stopTime, Integer.valueOf(1), null, new Object[] { RefreshMode.NO_REFRESH });
/*     */       
/* 252 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 256 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 260 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RMLink });
/*     */     }
/*     */     finally
/*     */     {
/* 264 */       if (establishedSubject) {
/* 265 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 274 */     Tracer.traceMethodEntry(new Object[0]);
/* 275 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceLink);
/* 276 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*     */     
/* 278 */     Tracer.traceMethodExit(new Object[] { result });
/* 279 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPermissions(List<RMPermission> jarmPerms)
/*     */   {
/* 287 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/* 288 */     Util.ckNullObjParam("jarmPerms", jarmPerms);
/*     */     
/* 290 */     AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(jarmPerms);
/* 291 */     this.jaceLink.set_Permissions(jacePerms);
/* 292 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 300 */     Tracer.traceMethodEntry(new Object[0]);
/* 301 */     List<Container> result = Collections.emptyList();
/*     */     
/* 303 */     Tracer.traceMethodExit(new Object[] { result });
/* 304 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 312 */     Tracer.traceMethodEntry(new Object[0]);
/* 313 */     String result = P8CE_Util.getJacePropertyAsString(this, "LinkName");
/* 314 */     Tracer.traceMethodExit(new Object[] { result });
/* 315 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 323 */     Tracer.traceMethodEntry(new Object[0]);
/* 324 */     RMProperties result = null;
/* 325 */     if (this.jaceLink != null)
/*     */     {
/* 327 */       result = new P8CE_RMPropertiesImpl(this.jaceLink, this);
/*     */     }
/*     */     
/* 330 */     Tracer.traceMethodExit(new Object[] { result });
/* 331 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 339 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 340 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 343 */       establishedSubject = P8CE_Util.associateSubject();
/* 344 */       internalSave(jarmRefreshMode);
/* 345 */       this.isCreationPending = false;
/* 346 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 350 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 354 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), getEntityType() });
/*     */     }
/*     */     finally
/*     */     {
/* 358 */       if (establishedSubject) {
/* 359 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 368 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 369 */     RefreshMode jaceRefreshMode = P8CE_Util.convertToJaceRefreshMode(jarmRefreshMode);
/*     */     
/* 371 */     long startTime = System.currentTimeMillis();
/* 372 */     this.jaceLink.save(jaceRefreshMode);
/* 373 */     Tracer.traceExtCall("Document.save()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceRefreshMode, new Object[0]);
/*     */     
/* 375 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 384 */     return "P8CE_RMLinkImpl EntityType: " + getEntityType() + ", Ident: " + getObjectIdentity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 392 */     Tracer.traceMethodEntry(new Object[0]);
/* 393 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 396 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 398 */       long startTime = System.currentTimeMillis();
/* 399 */       this.jaceLink.refresh();
/* 400 */       long endTime = System.currentTimeMillis();
/* 401 */       Tracer.traceExtCall("Link.refresh", startTime, endTime, null, null, new Object[0]);
/*     */       
/* 403 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 407 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 411 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 415 */       if (establishedSubject) {
/* 416 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 425 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 426 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 429 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 433 */       List<FilterElement> mandatoryRecordFEs = getMandatoryJaceFEs();
/* 434 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 436 */       long startTime = System.currentTimeMillis();
/* 437 */       this.jaceLink.refresh(jacePF);
/* 438 */       long endTime = System.currentTimeMillis();
/* 439 */       Tracer.traceExtCall("Link.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 441 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 445 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 449 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 453 */       if (establishedSubject) {
/* 454 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 463 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 464 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 467 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 470 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 471 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 472 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 473 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 474 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 476 */       long startTime = System.currentTimeMillis();
/* 477 */       this.jaceLink.refresh(jacePF);
/* 478 */       long endTime = System.currentTimeMillis();
/* 479 */       Tracer.traceExtCall("Link.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 481 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 485 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 489 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 493 */       if (establishedSubject) {
/* 494 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 503 */     return this.jaceLink.getAccessAllowed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 511 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RMLink>
/*     */   {
/*     */     public RMLink create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 525 */       P8CE_RMLinkImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 526 */       Link jaceLink = (Link)jaceBaseObject;
/*     */       
/* 528 */       EntityType entityType = EntityType.RMLink;
/* 529 */       if ((jaceBaseObject != null) && ((jaceBaseObject instanceof EngineObject)))
/*     */       {
/* 531 */         Properties jaceProps = ((EngineObject)jaceBaseObject).getProperties();
/* 532 */         if (jaceProps.isPropertyPresent("RMEntityType"))
/*     */         {
/* 534 */           Integer rawEntityType = jaceProps.getInteger32Value("RMEntityType");
/* 535 */           entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*     */         }
/*     */       }
/*     */       
/* 539 */       String identity = P8CE_Util.getJaceObjectIdentity(jaceLink);
/* 540 */       RMLink result = new P8CE_RMLinkImpl(entityType, repository, identity, jaceLink, false);
/*     */       
/* 542 */       P8CE_RMLinkImpl.Tracer.traceMethodExit(new Object[] { result });
/* 543 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMLinkImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */