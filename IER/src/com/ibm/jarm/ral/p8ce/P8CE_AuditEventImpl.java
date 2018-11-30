/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.StringList;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.events.Event;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.ibm.jarm.api.constants.AuditStatus;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.AuditEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_AuditEventImpl
/*     */   extends RALBaseEntity
/*     */   implements AuditEvent, JaceBasable
/*     */ {
/*  43 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  44 */   private static final IGenerator<AuditEvent> AuditEventGenerator = new Generator();
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
/*  55 */   private static final String[] MandatoryPropertyNames = { "Id", "Name", "EventStatus", "InitiatingUser", "SourceObjectId", "SourceClassId", "SourceObject", "DateCreated" };
/*     */   
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  62 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  64 */     List<FilterElement> tempList = new ArrayList(1);
/*  65 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  66 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*  69 */   private static final String[] UpdateAdditionalPropNames = { "OriginalObject", "ModifiedProperties" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private static final String[] RMAuditAdditionalPropNames = { "OriginalObject", "ModifiedProperties", "AuditActionType", "RMEntityDescription", "Reviewer", "ReasonForAction" };
/*     */   
/*     */ 
/*     */   protected Event jaceEvent;
/*     */   
/*     */ 
/*     */ 
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  83 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  88 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<AuditEvent> getAuditEventGenerator()
/*     */   {
/*  98 */     return AuditEventGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 106 */     return getMandatoryJaceFEs();
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
/*     */   protected P8CE_AuditEventImpl(Repository repository, String identity, Event jaceEvent, boolean isPlaceholder)
/*     */   {
/* 123 */     super(EntityType.AuditEvent, repository, identity, isPlaceholder);
/* 124 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceEvent, Boolean.valueOf(isPlaceholder) });
/* 125 */     this.jaceEvent = jaceEvent;
/* 126 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAuditActionType()
/*     */   {
/* 134 */     Tracer.traceMethodEntry(new Object[0]);
/* 135 */     String result = null;
/* 136 */     if (!getProperties().isPropertyPresent("AuditActionType")) {
/* 137 */       fetchAdditionalProperties();
/*     */     }
/* 139 */     if (getProperties().isPropertyPresent("AuditActionType")) {
/* 140 */       result = P8CE_Util.getJacePropertyAsString(this, "AuditActionType");
/*     */     }
/* 142 */     Tracer.traceMethodExit(new Object[] { result });
/* 143 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 151 */     Tracer.traceMethodEntry(new Object[0]);
/* 152 */     String result = null;
/* 153 */     if (!getProperties().isPropertyPresent("RMEntityDescription")) {
/* 154 */       fetchAdditionalProperties();
/*     */     }
/* 156 */     if (getProperties().isPropertyPresent("RMEntityDescription")) {
/* 157 */       result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/*     */     }
/* 159 */     Tracer.traceMethodExit(new Object[] { result });
/* 160 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReasonForAction()
/*     */   {
/* 168 */     Tracer.traceMethodEntry(new Object[0]);
/* 169 */     String result = null;
/* 170 */     if (!getProperties().isPropertyPresent("ReasonForAction")) {
/* 171 */       fetchAdditionalProperties();
/*     */     }
/* 173 */     if (getProperties().isPropertyPresent("ReasonForAction")) {
/* 174 */       result = P8CE_Util.getJacePropertyAsString(this, "ReasonForAction");
/*     */     }
/* 176 */     Tracer.traceMethodExit(new Object[] { result });
/* 177 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReviewer()
/*     */   {
/* 185 */     Tracer.traceMethodEntry(new Object[0]);
/* 186 */     String result = null;
/* 187 */     if (!getProperties().isPropertyPresent("Reviewer")) {
/* 188 */       fetchAdditionalProperties();
/*     */     }
/* 190 */     if (getProperties().isPropertyPresent("Reviewer")) {
/* 191 */       result = P8CE_Util.getJacePropertyAsString(this, "Reviewer");
/*     */     }
/* 193 */     Tracer.traceMethodExit(new Object[] { result });
/* 194 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AuditStatus getEventStatus()
/*     */   {
/* 202 */     Tracer.traceMethodEntry(new Object[0]);
/* 203 */     Integer rawValue = P8CE_Util.getJacePropertyAsInteger(this, "EventStatus");
/* 204 */     AuditStatus result = null;
/* 205 */     if (rawValue != null)
/*     */     {
/* 207 */       result = AuditStatus.getInstanceFromInt(rawValue.intValue());
/*     */     }
/* 209 */     Tracer.traceMethodExit(new Object[] { result });
/* 210 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getInitiatingUser()
/*     */   {
/* 218 */     Tracer.traceMethodEntry(new Object[0]);
/* 219 */     String result = P8CE_Util.getJacePropertyAsString(this, "InitiatingUser");
/* 220 */     Tracer.traceMethodExit(new Object[] { result });
/* 221 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSourceObjectId()
/*     */   {
/* 229 */     Tracer.traceMethodEntry(new Object[0]);
/* 230 */     String result = P8CE_Util.getJacePropertyAsString(this, "SourceObjectId");
/* 231 */     Tracer.traceMethodExit(new Object[] { result });
/* 232 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSourceClassId()
/*     */   {
/* 240 */     Tracer.traceMethodEntry(new Object[0]);
/* 241 */     String result = P8CE_Util.getJacePropertyAsString(this, "SourceClassId");
/* 242 */     Tracer.traceMethodExit(new Object[] { result });
/* 243 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseEntity getOriginalObject()
/*     */   {
/* 251 */     Tracer.traceMethodEntry(new Object[0]);
/* 252 */     BaseEntity result = null;
/* 253 */     String eventClassName = getClassName();
/* 254 */     if (("UpdateEvent".equalsIgnoreCase(eventClassName)) || ("UpdateSecurityEvent".equalsIgnoreCase(eventClassName)) || ("RMAudit".equalsIgnoreCase(eventClassName)))
/*     */     {
/*     */ 
/*     */ 
/* 258 */       result = getEventObject("OriginalObject");
/*     */     }
/* 260 */     Tracer.traceMethodExit(new Object[] { result });
/* 261 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseEntity getSourceObject()
/*     */   {
/* 269 */     Tracer.traceMethodEntry(new Object[0]);
/* 270 */     BaseEntity result = getEventObject("SourceObject");
/* 271 */     Tracer.traceMethodExit(new Object[] { result });
/* 272 */     return result;
/*     */   }
/*     */   
/*     */   private BaseEntity getEventObject(String objectPropertyName)
/*     */   {
/* 277 */     Tracer.traceMethodEntry(new Object[0]);
/* 278 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 281 */       establishedSubject = P8CE_Util.associateSubject();
/* 282 */       List<FilterElement> jaceFEs = P8CE_BaseEntityImpl.getMandatoryJaceFEs();
/* 283 */       IGenerator<BaseEntity> generator = P8CE_BaseEntityImpl.getBaseEntityGenerator();
/*     */       
/* 285 */       BaseEntity result = (BaseEntity)P8CE_Util.fetchSVObjPropValue(this.repository, this.jaceEvent, jaceFEs, objectPropertyName, generator);
/*     */       
/*     */ 
/* 288 */       Tracer.traceMethodExit(new Object[] { result });
/* 289 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 293 */       if (establishedSubject) {
/* 294 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getModifiedPropertyNames()
/*     */   {
/* 304 */     Tracer.traceMethodEntry(new Object[0]);
/* 305 */     List<String> result = null;
/*     */     
/* 307 */     if (!getProperties().isPropertyPresent("ModifiedProperties")) {
/* 308 */       fetchAdditionalProperties();
/*     */     }
/* 310 */     if (getProperties().isPropertyPresent("ModifiedProperties"))
/*     */     {
/* 312 */       StringList jaceStringList = P8CE_Util.getJacePropertyAsStringList(this, "ModifiedProperties");
/* 313 */       if (jaceStringList != null)
/*     */       {
/* 315 */         result = new ArrayList(jaceStringList);
/*     */       }
/*     */     }
/*     */     
/* 319 */     Tracer.traceMethodExit(new Object[] { result });
/* 320 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 328 */     Tracer.traceMethodEntry(new Object[0]);
/* 329 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceEvent);
/* 330 */     Tracer.traceMethodExit(new Object[] { result });
/* 331 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 339 */     Tracer.traceMethodEntry(new Object[0]);
/* 340 */     String result = P8CE_Util.getJaceObjectClassName(this.jaceEvent);
/* 341 */     Tracer.traceMethodExit(new Object[] { result });
/* 342 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/* 350 */     return this.jaceEvent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 358 */     Tracer.traceMethodEntry(new Object[0]);
/* 359 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceEvent);
/* 360 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/*     */     
/* 362 */     Tracer.traceMethodExit(new Object[] { result });
/* 363 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 371 */     Tracer.traceMethodEntry(new Object[0]);
/* 372 */     List<Container> result = Collections.emptyList();
/*     */     
/* 374 */     Tracer.traceMethodExit(new Object[] { result });
/* 375 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 383 */     Tracer.traceMethodEntry(new Object[0]);
/* 384 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/* 385 */     Tracer.traceMethodExit(new Object[] { result });
/* 386 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 394 */     Tracer.traceMethodEntry(new Object[0]);
/* 395 */     RMProperties result = null;
/* 396 */     if (this.jaceEvent != null)
/*     */     {
/* 398 */       result = new P8CE_RMPropertiesImpl(this.jaceEvent, this);
/*     */     }
/*     */     
/* 401 */     Tracer.traceMethodExit(new Object[] { result });
/* 402 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 411 */     return "P8CE_AuditEventImpl EntityType: " + getEntityType() + ", Ident: " + getObjectIdentity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 419 */     Tracer.traceMethodEntry(new Object[0]);
/* 420 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 423 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 425 */       long startTime = System.currentTimeMillis();
/* 426 */       this.jaceEvent.refresh();
/* 427 */       long endTime = System.currentTimeMillis();
/* 428 */       Tracer.traceExtCall("CustomEvent.refresh", startTime, endTime, null, null, new Object[0]);
/*     */       
/* 430 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 434 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 438 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 442 */       if (establishedSubject) {
/* 443 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 452 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 453 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 456 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 460 */       List<FilterElement> mandatoryRecordFEs = getMandatoryJaceFEs();
/* 461 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 463 */       long startTime = System.currentTimeMillis();
/* 464 */       this.jaceEvent.refresh(jacePF);
/* 465 */       long endTime = System.currentTimeMillis();
/* 466 */       Tracer.traceExtCall("CustomEvent.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 468 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 472 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 476 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 480 */       if (establishedSubject) {
/* 481 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 490 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 491 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 494 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 497 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 498 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 499 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 500 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 501 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 503 */       long startTime = System.currentTimeMillis();
/* 504 */       this.jaceEvent.refresh(jacePF);
/* 505 */       long endTime = System.currentTimeMillis();
/* 506 */       Tracer.traceExtCall("CustomEvent.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 508 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 512 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 516 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 520 */       if (establishedSubject) {
/* 521 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 530 */     return this.jaceEvent.getAccessAllowed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 538 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 546 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 547 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "internalSave", getEntityType(), getClientIdentifier() });
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
/*     */   private boolean fetchAdditionalProperties()
/*     */   {
/* 560 */     Tracer.traceMethodEntry(new Object[0]);
/* 561 */     boolean result = false;
/*     */     
/* 563 */     String[] addlPropNames = null;
/* 564 */     String eventClassName = this.jaceEvent.getClassName();
/* 565 */     if ("RMAudit".equalsIgnoreCase(eventClassName))
/*     */     {
/* 567 */       addlPropNames = RMAuditAdditionalPropNames;
/*     */     }
/* 569 */     else if (("UpdateEvent".equalsIgnoreCase(eventClassName)) || ("UpdateSecurityEvent".equalsIgnoreCase(eventClassName)))
/*     */     {
/*     */ 
/* 572 */       addlPropNames = UpdateAdditionalPropNames;
/*     */     }
/*     */     
/* 575 */     if (addlPropNames != null)
/*     */     {
/* 577 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 580 */         establishedSubject = P8CE_Util.associateSubject();
/* 581 */         this.jaceEvent.fetchProperties(addlPropNames);
/* 582 */         result = true;
/*     */       }
/*     */       finally
/*     */       {
/* 586 */         if (establishedSubject) {
/* 587 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/* 591 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 592 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<AuditEvent>
/*     */   {
/*     */     public AuditEvent create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 605 */       P8CE_AuditEventImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 606 */       Event jaceEvent = (Event)jaceBaseObject;
/* 607 */       String identity = P8CE_Util.getJaceObjectIdentity(jaceEvent);
/* 608 */       AuditEvent result = new P8CE_AuditEventImpl(repository, identity, jaceEvent, false);
/*     */       
/* 610 */       P8CE_AuditEventImpl.Tracer.traceMethodExit(new Object[] { result });
/* 611 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_AuditEventImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */