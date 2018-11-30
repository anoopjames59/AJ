/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.Containable;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.IndependentlyPersistableObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALBaseEntity;
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
/*     */ public class P8CE_BaseEntityImpl
/*     */   extends RALBaseEntity
/*     */   implements JaceBasable
/*     */ {
/*  42 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  43 */   private static final IGenerator<BaseEntity> BaseEntityGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private static final String[] MandatoryPropertyNames = { "Id", "Name", "RMEntityType" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   private IndependentObject jaceBaseObj;
/*     */   private String classIdent;
/*     */   
/*     */   static {
/*  59 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  60 */     List<FilterElement> tempList = new ArrayList(1);
/*  61 */     tempList.add(new FilterElement(Integer.valueOf(0), null, Boolean.FALSE, mandatoryNames, null));
/*  62 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  67 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  72 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<BaseEntity> getBaseEntityGenerator()
/*     */   {
/*  82 */     return BaseEntityGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public P8CE_BaseEntityImpl(Repository repository, String identity, String classIdent, EntityType entityType, IndependentObject jaceBaseObj, boolean isPlaceholder)
/*     */   {
/*  90 */     super(entityType, repository, identity, isPlaceholder);
/*  91 */     Tracer.traceMethodEntry(new Object[] { repository, identity, classIdent, entityType, jaceBaseObj, Boolean.valueOf(isPlaceholder) });
/*  92 */     this.jaceBaseObj = jaceBaseObj;
/*  93 */     this.classIdent = classIdent;
/*  94 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/* 102 */     return this.jaceBaseObj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 110 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 119 */     return this.classIdent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 128 */     Tracer.traceMethodEntry(new Object[0]);
/* 129 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceBaseObj);
/* 130 */     Tracer.traceMethodExit(new Object[] { result });
/* 131 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 139 */     Tracer.traceMethodEntry(new Object[0]);
/* 140 */     Integer result = null;
/* 141 */     if ((this.jaceBaseObj != null) && ((this.jaceBaseObj instanceof IndependentlyPersistableObject)))
/*     */     {
/* 143 */       result = ((IndependentlyPersistableObject)this.jaceBaseObj).getAccessAllowed();
/*     */     }
/*     */     
/* 146 */     Tracer.traceMethodExit(new Object[] { result });
/* 147 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 155 */     Tracer.traceMethodEntry(new Object[0]);
/* 156 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 159 */       establishedSubject = P8CE_Util.associateSubject();
/* 160 */       boolean excludeDeleted = true;
/* 161 */       List<Container> result = P8CE_Util.getContainedBy(getRepository(), this.jaceBaseObj, excludeDeleted);
/*     */       
/* 163 */       Tracer.traceMethodExit(new Object[] { result });
/* 164 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 168 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 172 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_PARENT_CONTAINER_FAILED, new Object[] { getObjectIdentity() });
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
/*     */   public String getName()
/*     */   {
/* 186 */     Tracer.traceMethodEntry(new Object[0]);
/* 187 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/* 188 */     Tracer.traceMethodExit(new Object[] { result });
/* 189 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 197 */     Tracer.traceMethodEntry(new Object[0]);
/* 198 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceBaseObj);
/* 199 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*     */     
/* 201 */     Tracer.traceMethodExit(new Object[] { result });
/* 202 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPermissions(List<RMPermission> jarmPerms)
/*     */   {
/* 210 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/* 211 */     Util.ckNullObjParam("jarmPerms", jarmPerms);
/* 212 */     if ((this.jaceBaseObj instanceof Containable))
/*     */     {
/* 214 */       AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(jarmPerms);
/* 215 */       ((Containable)this.jaceBaseObj).set_Permissions(jacePerms);
/*     */     }
/* 217 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 225 */     Tracer.traceMethodEntry(new Object[0]);
/* 226 */     RMProperties result = null;
/* 227 */     if (this.jaceBaseObj != null)
/*     */     {
/* 229 */       result = new P8CE_RMPropertiesImpl(this.jaceBaseObj, this);
/*     */     }
/*     */     
/* 232 */     Tracer.traceMethodExit(new Object[] { result });
/* 233 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 241 */     Tracer.traceMethodEntry(new Object[0]);
/* 242 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 245 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 247 */       long startTime = System.currentTimeMillis();
/* 248 */       this.jaceBaseObj.refresh();
/* 249 */       long endTime = System.currentTimeMillis();
/* 250 */       Tracer.traceExtCall("IndependentObject.refresh", startTime, endTime, null, null, new Object[0]);
/*     */       
/* 252 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 256 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 260 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
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
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 274 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 275 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 278 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 282 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, null);
/*     */       
/* 284 */       long startTime = System.currentTimeMillis();
/* 285 */       this.jaceBaseObj.refresh(jacePF);
/* 286 */       long endTime = System.currentTimeMillis();
/* 287 */       Tracer.traceExtCall("IndependentObject.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 289 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 293 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 297 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
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
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 311 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 312 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 315 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 318 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 319 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 320 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 321 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 322 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 324 */       long startTime = System.currentTimeMillis();
/* 325 */       this.jaceBaseObj.refresh(jacePF);
/* 326 */       long endTime = System.currentTimeMillis();
/* 327 */       Tracer.traceExtCall("IndependentObject.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 329 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 333 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 337 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 341 */       if (establishedSubject) {
/* 342 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode) {
/* 348 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 349 */     if ((this.jaceBaseObj != null) && ((this.jaceBaseObj instanceof IndependentlyPersistableObject)))
/*     */     {
/* 351 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 354 */         establishedSubject = P8CE_Util.associateSubject();
/*     */         
/* 356 */         RefreshMode jaceRefreshMode = jarmRefreshMode == RMRefreshMode.Refresh ? RefreshMode.REFRESH : RefreshMode.NO_REFRESH;
/*     */         
/* 358 */         long startTime = System.currentTimeMillis();
/* 359 */         ((IndependentlyPersistableObject)this.jaceBaseObj).save(jaceRefreshMode);
/* 360 */         long stopTime = System.currentTimeMillis();
/* 361 */         Tracer.traceExtCall("IndependentlyPersistableObject.save", startTime, stopTime, Integer.valueOf(1), null, new Object[] { jaceRefreshMode });
/*     */         
/* 363 */         this.isCreationPending = false;
/* 364 */         Tracer.traceMethodExit(new Object[0]);
/*     */       }
/*     */       catch (RMRuntimeException ex)
/*     */       {
/* 368 */         throw ex;
/*     */       }
/*     */       catch (Exception cause)
/*     */       {
/* 372 */         throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), this.entityType });
/*     */       }
/*     */       finally
/*     */       {
/* 376 */         if (establishedSubject) {
/* 377 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve()
/*     */   {
/* 389 */     Tracer.traceMethodEntry(new Object[0]);
/* 390 */     if (this.isPlaceholder)
/*     */     {
/* 392 */       P8CE_Util.refreshWithMandatoryProperties(this);
/* 393 */       this.isPlaceholder = false;
/*     */       
/* 395 */       if (this.jaceBaseObj.getProperties().isPropertyPresent("RMEntityType"))
/*     */       {
/* 397 */         Integer rawEntityType = this.jaceBaseObj.getProperties().getInteger32Value("RMEntityType");
/* 398 */         if (rawEntityType != null)
/* 399 */           this.entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*     */       }
/*     */     }
/* 402 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 410 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 420 */     return "P8CE_BaseEntityImpl ObjectIdent: " + getObjectIdentity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<BaseEntity>
/*     */   {
/*     */     public BaseEntity create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 434 */       P8CE_BaseEntityImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 435 */       IndependentlyPersistableObject jaceIndepPersistObj = (IndependentlyPersistableObject)jaceBaseObject;
/*     */       
/* 437 */       EntityType entityType = null;
/* 438 */       String classIdent = null;
/* 439 */       if (jaceIndepPersistObj != null)
/*     */       {
/* 441 */         Properties jaceProps = jaceIndepPersistObj.getProperties();
/* 442 */         if (jaceProps.isPropertyPresent("RMEntityType"))
/*     */         {
/* 444 */           Integer rawEntityType = jaceProps.getInteger32Value("RMEntityType");
/* 445 */           entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*     */         }
/* 447 */         classIdent = jaceIndepPersistObj.getClassName();
/*     */       }
/* 449 */       String identity = P8CE_Util.getJaceObjectIdentity(jaceIndepPersistObj);
/*     */       
/* 451 */       BaseEntity result = null;
/* 452 */       if ((entityType == null) || (entityType == EntityType.Unknown))
/*     */       {
/* 454 */         result = new P8CE_BaseEntityImpl(repository, identity, classIdent, EntityType.Unknown, jaceIndepPersistObj, false);
/*     */       }
/*     */       else
/*     */       {
/* 458 */         IGenerator<? extends BaseEntity> actualGenerator = P8CE_Util.getEntityGenerator(entityType);
/* 459 */         if (actualGenerator != null)
/*     */         {
/* 461 */           result = (BaseEntity)actualGenerator.create(repository, jaceIndepPersistObj);
/*     */         }
/*     */         else
/*     */         {
/* 465 */           result = new P8CE_BaseEntityImpl(repository, identity, classIdent, EntityType.Unknown, jaceIndepPersistObj, false);
/*     */         }
/*     */       }
/*     */       
/* 469 */       P8CE_BaseEntityImpl.Tracer.traceMethodExit(new Object[] { result });
/* 470 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_BaseEntityImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */