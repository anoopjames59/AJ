/*      */ package com.ibm.jarm.api.core;
/*      */ 
/*      */ import com.ibm.jarm.api.constants.DispositionActionType;
/*      */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*      */ import com.ibm.jarm.api.constants.DomainType;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.meta.RMChoiceList;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.meta.RMMarkingSet;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.security.RMGroup;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.security.RMUser;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.RALService;
/*      */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*      */ import com.ibm.jarm.ral.p8ce.P8CE_RALServiceImpl;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ public class RMFactory
/*      */ {
/*   28 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*      */   
/*      */ 
/*      */ 
/*   32 */   private static final Map<DomainType, RALService> RALServices = Collections.synchronizedMap(new HashMap());
/*      */   
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*   38 */     RALServices.put(DomainType.P8_CE, new P8CE_RALServiceImpl());
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
/*      */   public static RALService getRALService(BaseEntity baseEntity)
/*      */   {
/*   58 */     Tracer.traceMethodEntry(new Object[] { baseEntity });
/*   59 */     Util.ckNullObjParam("baseEntity", baseEntity);
/*      */     
/*   61 */     RMDomain domain = null;
/*   62 */     if ((baseEntity instanceof RMDomain)) {
/*   63 */       domain = (RMDomain)baseEntity;
/*      */     } else {
/*   65 */       domain = baseEntity.getRepository().getDomain();
/*      */     }
/*   67 */     RALService result = getRALService(domain);
/*      */     
/*   69 */     Tracer.traceMethodExit(new Object[] { result });
/*   70 */     return result;
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
/*      */   static RALService getRALService(RMDomain domain)
/*      */   {
/*   86 */     Tracer.traceMethodEntry(new Object[] { domain });
/*   87 */     Util.ckNullObjParam("domain", domain);
/*      */     
/*   89 */     RALService result = getRALService(domain.getDomainConnection());
/*      */     
/*   91 */     Tracer.traceMethodExit(new Object[] { result });
/*   92 */     return result;
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
/*      */   public static RALService getRALService(DomainConnection conn)
/*      */   {
/*  108 */     Tracer.traceMethodEntry(new Object[] { conn });
/*  109 */     Util.ckNullObjParam("conn", conn);
/*      */     
/*  111 */     DomainType domainType = conn.getDomainType();
/*  112 */     RALService result = getRALService(domainType);
/*      */     
/*  114 */     Tracer.traceMethodExit(new Object[] { result });
/*  115 */     return result;
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
/*      */   static RALService getRALService(DomainType domainType)
/*      */   {
/*  134 */     Tracer.traceMethodEntry(new Object[] { domainType });
/*  135 */     Util.ckNullObjParam("domainType", domainType);
/*      */     
/*  137 */     RALService result = (RALService)RALServices.get(domainType);
/*      */     
/*  139 */     Tracer.traceMethodExit(new Object[] { result });
/*  140 */     return result;
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
/*      */   public static class BaseEntity
/*      */   {
/*      */     public static BaseEntity fetchInstance(Repository repository, EntityType entityType, String ident, RMPropertyFilter filter)
/*      */     {
/*  170 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, entityType, ident, filter });
/*  171 */       Util.ckNullObjParam("repository", repository);
/*  172 */       Util.ckNullObjParam("entityType", entityType);
/*  173 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/*  175 */       RALService ralService = RMFactory.getRALService(repository);
/*  176 */       BaseEntity result = ralService.fetchBaseEntity(repository, entityType, ident, filter);
/*      */       
/*  178 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  179 */       return result;
/*      */     }
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
/*      */     public static BaseEntity fetchInstance(Repository repository, String classIdent, String ident, RMPropertyFilter filter)
/*      */     {
/*  202 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, classIdent, ident, filter });
/*  203 */       Util.ckNullObjParam("repository", repository);
/*  204 */       Util.ckNullObjParam("classIdent", classIdent);
/*  205 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/*  207 */       RALService ralService = RMFactory.getRALService(repository);
/*  208 */       BaseEntity result = ralService.fetchBaseEntity(repository, classIdent, ident, filter);
/*      */       
/*  210 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  211 */       return result;
/*      */     }
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
/*      */     public static BaseEntity getInstance(FilePlanRepository fpRepository, EntityType entityType, String baseEntityIdent)
/*      */     {
/*  231 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, entityType, baseEntityIdent });
/*  232 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  233 */       Util.ckNullObjParam("entityType", entityType);
/*  234 */       Util.ckInvalidStrParam("baseEntityIdent", baseEntityIdent);
/*      */       
/*  236 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  237 */       BaseEntity result = ralService.getBaseEntity(fpRepository, entityType, baseEntityIdent);
/*      */       
/*  239 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  240 */       return result;
/*      */     }
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
/*      */     public static BaseEntity getInstance(FilePlanRepository fpRepository, String classIdent, String baseEntityIdent)
/*      */     {
/*  260 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, classIdent, baseEntityIdent });
/*  261 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  262 */       Util.ckNullObjParam("classIdent", classIdent);
/*  263 */       Util.ckInvalidStrParam("baseEntityIdent", baseEntityIdent);
/*      */       
/*  265 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  266 */       BaseEntity result = ralService.getBaseEntity(fpRepository, classIdent, baseEntityIdent);
/*      */       
/*  268 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  269 */       return result;
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
/*      */   public static class ClassificationGuide
/*      */   {
/*      */     public static ClassificationGuide fetchInstance(FilePlanRepository fpRepository, String guideIdent, RMPropertyFilter filter)
/*      */     {
/*  300 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, guideIdent, filter });
/*  301 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  302 */       Util.ckInvalidStrParam("guideIdent", guideIdent);
/*      */       
/*  304 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  305 */       ClassificationGuide result = ralService.fetchClassificationGuide(fpRepository, guideIdent, filter);
/*      */       
/*  307 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  308 */       return result;
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
/*      */   public static class Container
/*      */   {
/*      */     public static Container fetchInstance(FilePlanRepository fpRepository, EntityType entityType, String containerIdent, RMPropertyFilter filter)
/*      */     {
/*  340 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, entityType, containerIdent, filter });
/*  341 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  342 */       Util.ckNullObjParam("entityType", entityType);
/*  343 */       Util.ckInvalidStrParam("containerIdent", containerIdent);
/*      */       
/*  345 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  346 */       Container result = ralService.fetchContainer(fpRepository, entityType, containerIdent, filter);
/*      */       
/*  348 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  349 */       return result;
/*      */     }
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
/*      */     public static Container getInstance(FilePlanRepository fpRepository, EntityType entityType, String containerIdent)
/*      */     {
/*  369 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, containerIdent });
/*  370 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  371 */       Util.ckNullObjParam("entityType", entityType);
/*  372 */       Util.ckInvalidStrParam("containerIdent", containerIdent);
/*      */       
/*  374 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  375 */       Container result = ralService.getContainer(fpRepository, entityType, containerIdent);
/*      */       
/*  377 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  378 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class ContentItem
/*      */   {
/*      */     public static ContentItem fetchInstance(Repository repository, String contentIdent, RMPropertyFilter filter)
/*      */     {
/*  407 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, contentIdent, filter });
/*  408 */       Util.ckNullObjParam("repository", repository);
/*  409 */       Util.ckInvalidStrParam("contentIdent", contentIdent);
/*      */       
/*  411 */       RALService ralService = RMFactory.getRALService(repository);
/*  412 */       ContentItem result = ralService.fetchContentItem(repository, EntityType.ContentItem, contentIdent, filter);
/*      */       
/*  414 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  415 */       return result;
/*      */     }
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
/*      */     public static ContentItem getInstance(ContentRepository repository, String contentIdent)
/*      */     {
/*  433 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, contentIdent });
/*  434 */       Util.ckNullObjParam("fpRepository", repository);
/*  435 */       Util.ckInvalidStrParam("contentIdent", contentIdent);
/*      */       
/*  437 */       RALService ralService = RMFactory.getRALService(repository);
/*  438 */       ContentItem result = ralService.getContentItem(repository, EntityType.ContentItem, contentIdent);
/*      */       
/*  440 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  441 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class ContentRepository
/*      */   {
/*      */     public static ContentRepository fetchInstance(RMDomain domain, String ident, RMPropertyFilter filter)
/*      */     {
/*  470 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domain, ident, filter });
/*  471 */       Util.ckNullObjParam("domain", domain);
/*  472 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/*  474 */       RALService ralService = RMFactory.getRALService(domain);
/*  475 */       ContentRepository result = ralService.fetchContentRepository(domain, ident, filter);
/*      */       
/*  477 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  478 */       return result;
/*      */     }
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
/*      */     public static ContentRepository getInstance(RMDomain domain, String ident)
/*      */     {
/*  494 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domain, ident });
/*  495 */       Util.ckNullObjParam("domain", domain);
/*  496 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/*  498 */       RALService ralService = RMFactory.getRALService(domain);
/*  499 */       ContentRepository result = ralService.getContentRepository(domain, ident);
/*      */       
/*  501 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  502 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class DispositionAction
/*      */   {
/*      */     public static DispositionAction fetchInstance(FilePlanRepository fpRepository, String dispActionId, RMPropertyFilter filter)
/*      */     {
/*  531 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, dispActionId, filter });
/*  532 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  533 */       Util.ckInvalidStrParam("dispActionId", dispActionId);
/*      */       
/*  535 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  536 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.DispositionAction, dispActionId, filter);
/*      */       
/*  538 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  539 */       return (DispositionAction)result;
/*      */     }
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
/*      */     public static DispositionAction getInstance(FilePlanRepository fpRepository, String dispActionId)
/*      */     {
/*  554 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, dispActionId });
/*  555 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  556 */       Util.ckInvalidStrParam("dispActionId", dispActionId);
/*      */       
/*  558 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  559 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.DispositionAction, dispActionId);
/*      */       
/*  561 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  562 */       return (DispositionAction)result;
/*      */     }
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
/*      */     public static DispositionAction createInstance(FilePlanRepository fpRepository, DispositionActionType actionType)
/*      */     {
/*  584 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, actionType });
/*  585 */       DispositionAction result = createInstance(fpRepository, actionType, null);
/*      */       
/*  587 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  588 */       return result;
/*      */     }
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
/*      */     public static DispositionAction createInstance(FilePlanRepository fpRepository, DispositionActionType actionType, String idStr)
/*      */     {
/*  613 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, actionType });
/*  614 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  615 */       Util.ckNullObjParam("actionType", actionType);
/*      */       
/*  617 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  618 */       DispositionAction result = ralService.createDispositionAction(fpRepository, actionType, idStr);
/*  619 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/*  621 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  622 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class DispositionSchedule
/*      */   {
/*      */     public static DispositionSchedule fetchInstance(FilePlanRepository fpRepository, String dispScheduleId, RMPropertyFilter filter)
/*      */     {
/*  651 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, dispScheduleId, filter });
/*  652 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  653 */       Util.ckInvalidStrParam("dispScheduleId", dispScheduleId);
/*      */       
/*  655 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  656 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.DispositionSchedule, dispScheduleId, filter);
/*      */       
/*  658 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  659 */       return (DispositionSchedule)result;
/*      */     }
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
/*      */     public static DispositionSchedule getInstance(FilePlanRepository fpRepository, String dispScheduleId)
/*      */     {
/*  674 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, dispScheduleId });
/*  675 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  676 */       Util.ckInvalidStrParam("dispScheduleId", dispScheduleId);
/*      */       
/*  678 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  679 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.DispositionSchedule, dispScheduleId);
/*      */       
/*  681 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  682 */       return (DispositionSchedule)result;
/*      */     }
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
/*      */     public static DispositionSchedule createInstance(FilePlanRepository fpRepository)
/*      */     {
/*  703 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/*  704 */       RMCustomObject result = createInstance(fpRepository, null);
/*      */       
/*  706 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  707 */       return (DispositionSchedule)result;
/*      */     }
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
/*      */     public static DispositionSchedule createInstance(FilePlanRepository fpRepository, String idStr)
/*      */     {
/*  731 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/*  732 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*      */       
/*  734 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  735 */       RMCustomObject result = ralService.createCustomObject(fpRepository, EntityType.DispositionSchedule, idStr);
/*  736 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/*  738 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  739 */       return (DispositionSchedule)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class DispositionTrigger
/*      */   {
/*      */     public static DispositionTrigger fetchInstance(FilePlanRepository fpRepository, String dispTriggerId, RMPropertyFilter filter)
/*      */     {
/*  768 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, dispTriggerId, filter });
/*  769 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  770 */       Util.ckInvalidStrParam("dispTriggerId", dispTriggerId);
/*      */       
/*  772 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  773 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.DispositionTrigger, dispTriggerId, filter);
/*      */       
/*  775 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  776 */       return (DispositionTrigger)result;
/*      */     }
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
/*      */     public static DispositionTrigger getInstance(FilePlanRepository fpRepository, String dispTriggerId)
/*      */     {
/*  791 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, dispTriggerId });
/*  792 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  793 */       Util.ckInvalidStrParam("dispTriggerId", dispTriggerId);
/*      */       
/*  795 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  796 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.DispositionTrigger, dispTriggerId);
/*      */       
/*  798 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  799 */       return (DispositionTrigger)result;
/*      */     }
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
/*      */     public static DispositionTrigger createInstance(FilePlanRepository fpRepository, DispositionTriggerType triggerType)
/*      */     {
/*  821 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, triggerType });
/*  822 */       DispositionTrigger result = createInstance(fpRepository, triggerType, null);
/*      */       
/*  824 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  825 */       return result;
/*      */     }
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
/*      */     public static DispositionTrigger createInstance(FilePlanRepository fpRepository, DispositionTriggerType triggerType, String idStr)
/*      */     {
/*  850 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, triggerType });
/*  851 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  852 */       Util.ckNullObjParam("triggerType", triggerType);
/*      */       
/*  854 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  855 */       DispositionTrigger result = ralService.createDispositionTrigger(fpRepository, triggerType, idStr);
/*  856 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/*  858 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  859 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class DomainConnection
/*      */   {
/*      */     public static DomainConnection createInstance(DomainType domainType, String url, Map<String, Object> connectionInfo)
/*      */     {
/*  886 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domainType, url, connectionInfo });
/*      */       
/*  888 */       Util.ckNullObjParam("domainType", domainType);
/*  889 */       Util.ckInvalidStrParam("url", url);
/*      */       
/*  891 */       RALService ralService = RMFactory.getRALService(domainType);
/*  892 */       DomainConnection result = ralService.createDomainConnection(url, connectionInfo);
/*      */       
/*  894 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  895 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class FilePlan
/*      */   {
/*      */     public static FilePlan fetchInstance(FilePlanRepository fpRepository, String filePlanIdent, RMPropertyFilter filter)
/*      */     {
/*  924 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, filePlanIdent, filter });
/*  925 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  926 */       Util.ckInvalidStrParam("filePlanIdent", filePlanIdent);
/*      */       
/*  928 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  929 */       Container result = ralService.fetchContainer(fpRepository, EntityType.FilePlan, filePlanIdent, filter);
/*      */       
/*  931 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  932 */       return (FilePlan)result;
/*      */     }
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
/*      */     public static FilePlan getInstance(FilePlanRepository fpRepository, String filePlanIdent)
/*      */     {
/*  950 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, filePlanIdent });
/*  951 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*  952 */       Util.ckInvalidStrParam("filePlanIdent", filePlanIdent);
/*      */       
/*  954 */       RALService ralService = RMFactory.getRALService(fpRepository);
/*  955 */       Container result = ralService.getContainer(fpRepository, EntityType.FilePlan, filePlanIdent);
/*      */       
/*  957 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  958 */       return (FilePlan)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class FilePlanRepository
/*      */   {
/*      */     public static FilePlanRepository fetchInstance(RMDomain domain, String ident, RMPropertyFilter filter)
/*      */     {
/*  987 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domain, ident, filter });
/*  988 */       Util.ckNullObjParam("domain", domain);
/*  989 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/*  991 */       RALService ralService = RMFactory.getRALService(domain);
/*  992 */       FilePlanRepository result = ralService.fetchFilePlanRepository(domain, ident, filter);
/*      */       
/*  994 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/*  995 */       return result;
/*      */     }
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
/*      */     public static FilePlanRepository getInstance(RMDomain domain, String ident)
/*      */     {
/* 1011 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domain, ident });
/* 1012 */       Util.ckNullObjParam("domain", domain);
/* 1013 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/* 1015 */       RALService ralService = RMFactory.getRALService(domain);
/* 1016 */       FilePlanRepository result = ralService.getFilePlanRepository(domain, ident);
/*      */       
/* 1018 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1019 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Hold
/*      */   {
/*      */     public static Hold fetchInstance(FilePlanRepository fpRepository, String holdId, RMPropertyFilter filter)
/*      */     {
/* 1048 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, holdId, filter });
/* 1049 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1050 */       Util.ckInvalidStrParam("holdId", holdId);
/*      */       
/* 1052 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1053 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.Hold, holdId, filter);
/*      */       
/* 1055 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1056 */       return (Hold)result;
/*      */     }
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
/*      */     public static Hold getInstance(FilePlanRepository fpRepository, String holdId)
/*      */     {
/* 1073 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, holdId });
/* 1074 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1075 */       Util.ckInvalidStrParam("holdId", holdId);
/*      */       
/* 1077 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1078 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.Hold, holdId);
/*      */       
/* 1080 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1081 */       return (Hold)result;
/*      */     }
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
/*      */     public static Hold createInstance(FilePlanRepository fpRepository)
/*      */     {
/* 1101 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1102 */       RMCustomObject result = createInstance(fpRepository, null);
/*      */       
/* 1104 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1105 */       return (Hold)result;
/*      */     }
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
/*      */     public static Hold createInstance(FilePlanRepository fpRepository, String idStr)
/*      */     {
/* 1128 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1129 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*      */       
/* 1131 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1132 */       RMCustomObject result = ralService.createCustomObject(fpRepository, EntityType.Hold, idStr);
/* 1133 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 1135 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1136 */       return (Hold)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Location
/*      */   {
/*      */     public static Location fetchInstance(FilePlanRepository fpRepository, String locationId, RMPropertyFilter filter)
/*      */     {
/* 1165 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, locationId, filter });
/* 1166 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1167 */       Util.ckInvalidStrParam("locationId", locationId);
/*      */       
/* 1169 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1170 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.Location, locationId, filter);
/*      */       
/* 1172 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1173 */       return (Location)result;
/*      */     }
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
/*      */     public static Location getInstance(FilePlanRepository fpRepository, String locationId)
/*      */     {
/* 1188 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, locationId });
/* 1189 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1190 */       Util.ckInvalidStrParam("locationId", locationId);
/*      */       
/* 1192 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1193 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.Location, locationId);
/*      */       
/* 1195 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1196 */       return (Location)result;
/*      */     }
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
/*      */     public static Location createInstance(FilePlanRepository fpRepository)
/*      */     {
/* 1217 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1218 */       Location result = createInstance(fpRepository, null);
/*      */       
/* 1220 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1221 */       return result;
/*      */     }
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
/*      */     public static Location createInstance(FilePlanRepository fpRepository, String idStr)
/*      */     {
/* 1245 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1246 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*      */       
/* 1248 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1249 */       Location result = ralService.createLocation(fpRepository, idStr);
/* 1250 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 1252 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1253 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class NamingPattern
/*      */   {
/*      */     public static NamingPattern fetchInstance(FilePlanRepository fpRepository, String namingPatternId, RMPropertyFilter filter)
/*      */     {
/* 1282 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, namingPatternId, filter });
/* 1283 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1284 */       Util.ckInvalidStrParam("namingPatternId", namingPatternId);
/*      */       
/* 1286 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1287 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.Pattern, namingPatternId, filter);
/*      */       
/* 1289 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1290 */       return (NamingPattern)result;
/*      */     }
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
/*      */     public static NamingPattern getInstance(FilePlanRepository fpRepository, String namingPatternId)
/*      */     {
/* 1306 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, namingPatternId });
/* 1307 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1308 */       Util.ckInvalidStrParam("namingPatternId", namingPatternId);
/*      */       
/* 1310 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1311 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.Pattern, namingPatternId);
/*      */       
/* 1313 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1314 */       return (NamingPattern)result;
/*      */     }
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
/*      */     public static NamingPattern createInstance(FilePlanRepository fpRepository)
/*      */     {
/* 1335 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1336 */       RMCustomObject result = createInstance(fpRepository, null);
/*      */       
/* 1338 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1339 */       return (NamingPattern)result;
/*      */     }
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
/*      */     public static NamingPattern createInstance(FilePlanRepository fpRepository, String idStr)
/*      */     {
/* 1363 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1364 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*      */       
/* 1366 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1367 */       RMCustomObject result = ralService.createCustomObject(fpRepository, EntityType.Pattern, idStr);
/* 1368 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 1370 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1371 */       return (NamingPattern)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class NamingPatternSequence
/*      */   {
/*      */     public static NamingPatternSequence fetchInstance(FilePlanRepository fpRepository, String namingPatternSequenceId, RMPropertyFilter filter)
/*      */     {
/* 1399 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, namingPatternSequenceId, filter });
/* 1400 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1401 */       Util.ckInvalidStrParam("namingPatternSequenceId", namingPatternSequenceId);
/*      */       
/* 1403 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1404 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.PatternSequence, namingPatternSequenceId, filter);
/*      */       
/* 1406 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1407 */       return (NamingPatternSequence)result;
/*      */     }
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
/*      */     public static NamingPatternSequence getInstance(FilePlanRepository fpRepository, String namingPatternSequenceId)
/*      */     {
/* 1424 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, namingPatternSequenceId });
/* 1425 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1426 */       Util.ckInvalidStrParam("namingPatternId", namingPatternSequenceId);
/*      */       
/* 1428 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1429 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.PatternSequence, namingPatternSequenceId);
/*      */       
/* 1431 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1432 */       return (NamingPatternSequence)result;
/*      */     }
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
/*      */     public static NamingPatternSequence createInstance(FilePlanRepository fpRepository)
/*      */     {
/* 1453 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1454 */       RMCustomObject result = createInstance(fpRepository, null);
/*      */       
/* 1456 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1457 */       return (NamingPatternSequence)result;
/*      */     }
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
/*      */     public static NamingPatternSequence createInstance(FilePlanRepository fpRepository, String idStr)
/*      */     {
/* 1481 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1482 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*      */       
/* 1484 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1485 */       RMCustomObject result = ralService.createCustomObject(fpRepository, EntityType.PatternSequence, idStr);
/* 1486 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 1488 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1489 */       return (NamingPatternSequence)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Record
/*      */   {
/*      */     public static Record fetchInstance(FilePlanRepository fpRepository, String recordIdent, RMPropertyFilter filter)
/*      */     {
/* 1518 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordIdent, filter });
/* 1519 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1520 */       Util.ckInvalidStrParam("recordIdent", recordIdent);
/*      */       
/* 1522 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1523 */       Record result = ralService.fetchRecord(fpRepository, recordIdent, filter);
/*      */       
/* 1525 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1526 */       return result;
/*      */     }
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
/*      */     public static Record getInstance(FilePlanRepository fpRepository, String recordIdent, EntityType recordEntityType)
/*      */     {
/* 1544 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordIdent });
/* 1545 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1546 */       Util.ckInvalidStrParam("recordIdent", recordIdent);
/*      */       
/* 1548 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1549 */       Record result = ralService.getRecord(fpRepository, recordEntityType, recordIdent);
/*      */       
/* 1551 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1552 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RecordCategory
/*      */   {
/*      */     public static RecordCategory fetchInstance(FilePlanRepository fpRepository, String recordCategoryIdent, RMPropertyFilter filter)
/*      */     {
/* 1581 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordCategoryIdent, filter });
/* 1582 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1583 */       Util.ckInvalidStrParam("recordCategoryIdent", recordCategoryIdent);
/*      */       
/* 1585 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1586 */       Container result = ralService.fetchContainer(fpRepository, EntityType.RecordCategory, recordCategoryIdent, filter);
/*      */       
/* 1588 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1589 */       return (RecordCategory)result;
/*      */     }
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
/*      */     public static RecordCategory getInstance(FilePlanRepository fpRepository, String recordCategoryIdent)
/*      */     {
/* 1607 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordCategoryIdent });
/* 1608 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1609 */       Util.ckInvalidStrParam("recordCategoryIdent", recordCategoryIdent);
/*      */       
/* 1611 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1612 */       Container result = ralService.getContainer(fpRepository, EntityType.RecordCategory, recordCategoryIdent);
/*      */       
/* 1614 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1615 */       return (RecordCategory)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RecordFolder
/*      */   {
/*      */     public static RecordFolder fetchInstance(FilePlanRepository fpRepository, String recordFolderIdent, RMPropertyFilter filter)
/*      */     {
/* 1644 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordFolderIdent, filter });
/* 1645 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1646 */       Util.ckInvalidStrParam("recordFolderIdent", recordFolderIdent);
/*      */       
/* 1648 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1649 */       Container result = ralService.fetchContainer(fpRepository, EntityType.RecordFolder, recordFolderIdent, filter);
/*      */       
/* 1651 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1652 */       return (RecordFolder)result;
/*      */     }
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
/*      */     public static RecordFolder getInstance(FilePlanRepository fpRepository, String recordFolderIdent)
/*      */     {
/* 1670 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordFolderIdent });
/* 1671 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1672 */       Util.ckInvalidStrParam("recordFolderIdent", recordFolderIdent);
/*      */       
/* 1674 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1675 */       Container result = ralService.getContainer(fpRepository, EntityType.RecordFolder, recordFolderIdent);
/*      */       
/* 1677 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1678 */       return (RecordFolder)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RecordType
/*      */   {
/*      */     public static RecordType fetchInstance(FilePlanRepository fpRepository, String recordTypeId, RMPropertyFilter filter)
/*      */     {
/* 1707 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordTypeId, filter });
/* 1708 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1709 */       Util.ckInvalidStrParam("recordTypeId", recordTypeId);
/*      */       
/* 1711 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1712 */       RMCustomObject result = ralService.fetchCustomObject(fpRepository, EntityType.RecordType, recordTypeId, filter);
/*      */       
/* 1714 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1715 */       return (RecordType)result;
/*      */     }
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
/*      */     public static RecordType getInstance(FilePlanRepository fpRepository, String recordTypeId)
/*      */     {
/* 1730 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordTypeId });
/* 1731 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1732 */       Util.ckInvalidStrParam("recordTypeId", recordTypeId);
/*      */       
/* 1734 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1735 */       RMCustomObject result = ralService.getCustomObject(fpRepository, EntityType.RecordType, recordTypeId);
/*      */       
/* 1737 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1738 */       return (RecordType)result;
/*      */     }
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
/*      */     public static RecordType createInstance(FilePlanRepository fpRepository)
/*      */     {
/* 1759 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1760 */       RMCustomObject result = createInstance(fpRepository, null);
/*      */       
/* 1762 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1763 */       return (RecordType)result;
/*      */     }
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
/*      */     public static RecordType createInstance(FilePlanRepository fpRepository, String idStr)
/*      */     {
/* 1787 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1788 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*      */       
/* 1790 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1791 */       RMCustomObject result = ralService.createCustomObject(fpRepository, EntityType.RecordType, idStr);
/* 1792 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 1794 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1795 */       return (RecordType)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RecordVolume
/*      */   {
/*      */     public static RecordVolume fetchInstance(FilePlanRepository fpRepository, String recordVolumeIdent, RMPropertyFilter filter)
/*      */     {
/* 1824 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordVolumeIdent, filter });
/* 1825 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1826 */       Util.ckInvalidStrParam("recordVolumeIdent", recordVolumeIdent);
/*      */       
/* 1828 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1829 */       Container result = ralService.fetchContainer(fpRepository, EntityType.RecordVolume, recordVolumeIdent, filter);
/*      */       
/* 1831 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1832 */       return (RecordVolume)result;
/*      */     }
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
/*      */     public static RecordVolume getInstance(FilePlanRepository fpRepository, String recordVolumeIdent)
/*      */     {
/* 1850 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, recordVolumeIdent });
/* 1851 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1852 */       Util.ckInvalidStrParam("recordVolumeIdent", recordVolumeIdent);
/*      */       
/* 1854 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1855 */       Container result = ralService.getContainer(fpRepository, EntityType.RecordVolume, recordVolumeIdent);
/*      */       
/* 1857 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1858 */       return (RecordVolume)result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class ReportDefinition
/*      */   {
/*      */     public static ReportDefinition fetchInstance(FilePlanRepository fpRepository, String reportDefId, RMPropertyFilter filter)
/*      */     {
/* 1886 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, reportDefId, filter });
/* 1887 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1888 */       Util.ckInvalidStrParam("reportGUID", reportDefId);
/*      */       
/* 1890 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1891 */       BaseEntity result = ralService.fetchBaseEntity(fpRepository, EntityType.ReportDefinition, reportDefId, filter);
/* 1892 */       ReportDefinition repDef = (ReportDefinition)result;
/* 1893 */       repDef.parseContent();
/*      */       
/* 1895 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1896 */       return repDef;
/*      */     }
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
/*      */     public static ReportDefinition getInstance(FilePlanRepository fpRepository, String reportDefId)
/*      */     {
/* 1911 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, reportDefId });
/* 1912 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 1913 */       Util.ckInvalidStrParam("reportGUID", reportDefId);
/*      */       
/* 1915 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1916 */       BaseEntity result = ralService.getBaseEntity(fpRepository, EntityType.ReportDefinition, reportDefId);
/*      */       
/* 1918 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1919 */       return (ReportDefinition)result;
/*      */     }
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
/*      */     public static ReportDefinition createInstance(FilePlanRepository fpRepository)
/*      */     {
/* 1937 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1938 */       ReportDefinition result = createInstance(fpRepository, null);
/*      */       
/* 1940 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1941 */       return result;
/*      */     }
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
/*      */     public static ReportDefinition createInstance(FilePlanRepository fpRepository, String idStr)
/*      */     {
/* 1962 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository });
/* 1963 */       Util.ckNullObjParam("fpRepository", fpRepository);
/*      */       
/* 1965 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 1966 */       ReportDefinition result = ralService.createReportDefinition(fpRepository, idStr);
/* 1967 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 1969 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 1970 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Repository
/*      */   {
/*      */     public static Repository fetchInstance(RMDomain domain, String ident, RMPropertyFilter filter)
/*      */     {
/* 1999 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domain, ident, filter });
/* 2000 */       Util.ckNullObjParam("domain", domain);
/* 2001 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/* 2003 */       RALService ralService = RMFactory.getRALService(domain);
/* 2004 */       Repository result = ralService.fetchRepository(domain, ident, filter);
/*      */       
/* 2006 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2007 */       return result;
/*      */     }
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
/*      */     public static Repository getInstance(RMDomain domain, String ident)
/*      */     {
/* 2023 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domain, ident });
/* 2024 */       Util.ckNullObjParam("domain", domain);
/* 2025 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/* 2027 */       RALService ralService = RMFactory.getRALService(domain);
/* 2028 */       Repository result = ralService.getRepository(domain, ident);
/*      */       
/* 2030 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2031 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMChoiceList
/*      */   {
/*      */     public static RMChoiceList fetchInstance(Repository repository, String choiceListId)
/*      */     {
/* 2054 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, choiceListId });
/* 2055 */       Util.ckNullObjParam("repository", repository);
/* 2056 */       Util.ckInvalidStrParam("choiceListId", choiceListId);
/*      */       
/* 2058 */       RALService ralService = RMFactory.getRALService(repository);
/* 2059 */       RMChoiceList result = ralService.fetchChoiceList(repository, choiceListId);
/*      */       
/* 2061 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2062 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMClassDescription
/*      */   {
/*      */     public static RMClassDescription fetchInstance(Repository repository, String ident, RMPropertyFilter filter)
/*      */     {
/* 2089 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, ident });
/* 2090 */       Util.ckNullObjParam("repository", repository);
/* 2091 */       Util.ckInvalidStrParam("ident", ident);
/*      */       
/* 2093 */       RALService ralService = RMFactory.getRALService(repository);
/* 2094 */       RMClassDescription result = ralService.fetchClassDescription(repository, ident, filter);
/*      */       
/* 2096 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2097 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMCustomObject
/*      */   {
/*      */     public static RMCustomObject fetchInstance(Repository repository, String objId, RMPropertyFilter filter)
/*      */     {
/* 2126 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, objId, filter });
/* 2127 */       Util.ckNullObjParam("repository", repository);
/* 2128 */       Util.ckInvalidStrParam("objId", objId);
/*      */       
/* 2130 */       RALService ralService = RMFactory.getRALService(repository);
/* 2131 */       RMCustomObject result = ralService.fetchCustomObject(repository, EntityType.CustomObject, objId, filter);
/*      */       
/* 2133 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2134 */       return result;
/*      */     }
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
/*      */     public static RMCustomObject fetchInstance(Repository repository, EntityType entityType, String objId, RMPropertyFilter filter)
/*      */     {
/* 2157 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, objId, filter });
/* 2158 */       Util.ckNullObjParam("repository", repository);
/* 2159 */       Util.ckNullObjParam("entityType", entityType);
/* 2160 */       Util.ckInvalidStrParam("objId", objId);
/*      */       
/* 2162 */       RALService ralService = RMFactory.getRALService(repository);
/* 2163 */       RMCustomObject result = ralService.fetchCustomObject(repository, entityType, objId, filter);
/*      */       
/* 2165 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2166 */       return result;
/*      */     }
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
/*      */     public static RMCustomObject getInstance(Repository repository, String objId)
/*      */     {
/* 2181 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, objId });
/* 2182 */       Util.ckNullObjParam("repository", repository);
/* 2183 */       Util.ckInvalidStrParam("objId", objId);
/*      */       
/* 2185 */       RALService ralService = RMFactory.getRALService(repository);
/* 2186 */       RMCustomObject result = ralService.getCustomObject(repository, EntityType.CustomObject, objId);
/*      */       
/* 2188 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2189 */       return result;
/*      */     }
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
/*      */     public static RMCustomObject createInstance(FilePlanRepository repository)
/*      */     {
/* 2209 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository });
/* 2210 */       RMCustomObject result = createInstance(repository, null);
/*      */       
/* 2212 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2213 */       return result;
/*      */     }
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
/*      */     public static RMCustomObject createInstance(FilePlanRepository repository, String idStr)
/*      */     {
/* 2236 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository });
/* 2237 */       Util.ckNullObjParam("repository", repository);
/*      */       
/* 2239 */       RALService ralService = RMFactory.getRALService(repository);
/* 2240 */       RMCustomObject result = ralService.createCustomObject(repository, EntityType.CustomObject, idStr);
/* 2241 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 2243 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2244 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMDomain
/*      */   {
/*      */     public static RMDomain fetchInstance(DomainConnection domainConn, String domainIdent, RMPropertyFilter filter)
/*      */     {
/* 2273 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domainConn, domainIdent, filter });
/*      */       
/* 2275 */       RALService ralService = RMFactory.getRALService(domainConn);
/* 2276 */       RMDomain result = ralService.fetchDomain(domainConn, domainIdent, filter);
/*      */       
/* 2278 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2279 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMLink
/*      */   {
/*      */     public static RMLink fetchInstance(FilePlanRepository fpRepository, String rmLinkId, RMPropertyFilter filter)
/*      */     {
/* 2308 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, rmLinkId, filter });
/* 2309 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 2310 */       Util.ckInvalidStrParam("rmLinkId", rmLinkId);
/*      */       
/* 2312 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 2313 */       RMLink result = ralService.fetchRMLink(fpRepository, rmLinkId, filter);
/*      */       
/* 2315 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2316 */       return result;
/*      */     }
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
/*      */     public static RMLink getInstance(FilePlanRepository fpRepository, String rmLinkId)
/*      */     {
/* 2331 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, rmLinkId });
/* 2332 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 2333 */       Util.ckInvalidStrParam("rmLinkId", rmLinkId);
/*      */       
/* 2335 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 2336 */       RMLink result = ralService.getRMLink(fpRepository, "Relation", rmLinkId);
/*      */       
/* 2338 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2339 */       return result;
/*      */     }
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
/*      */     public static RMLink createInstance(FilePlanRepository fpRepository, String classIdent)
/*      */     {
/* 2360 */       RMFactory.Tracer.traceMethodEntry(new Object[] { fpRepository, classIdent });
/* 2361 */       Util.ckNullObjParam("fpRepository", fpRepository);
/* 2362 */       Util.ckInvalidStrParam("classIdent", classIdent);
/*      */       
/* 2364 */       RALService ralService = RMFactory.getRALService(fpRepository);
/* 2365 */       RMLink result = ralService.createRMLink(fpRepository, classIdent);
/* 2366 */       ((RALBaseEntity)result).setCreationPending(true);
/*      */       
/* 2368 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2369 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMMarkingSet
/*      */   {
/*      */     public static RMMarkingSet fetchInstance(RMDomain domain, String markingSetId)
/*      */     {
/* 2393 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domain, markingSetId });
/* 2394 */       Util.ckNullObjParam("domain", domain);
/* 2395 */       Util.ckInvalidStrParam("markingSetId", markingSetId);
/*      */       
/* 2397 */       RALService ralService = RMFactory.getRALService(domain);
/* 2398 */       RMMarkingSet result = ralService.fetchMarkingSet(domain, markingSetId);
/*      */       
/* 2400 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2401 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMPermission
/*      */   {
/*      */     public static RMPermission createInstance(DomainType domainType)
/*      */     {
/* 2430 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domainType });
/* 2431 */       Util.ckNullObjParam("domainType", domainType);
/*      */       
/* 2433 */       RALService ralService = RMFactory.getRALService(domainType);
/* 2434 */       RMPermission result = ralService.createPermission();
/*      */       
/* 2436 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2437 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMProperties
/*      */   {
/*      */     public static RMProperties createInstance(DomainType domainType)
/*      */     {
/* 2463 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domainType });
/* 2464 */       Util.ckNullObjParam("domainType", domainType);
/*      */       
/* 2466 */       RALService ralService = RMFactory.getRALService(domainType);
/* 2467 */       RMProperties result = ralService.createProperties();
/*      */       
/* 2469 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2470 */       return result;
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
/*      */   public static class RMUser
/*      */   {
/*      */     public static RMUser fetchInstance(DomainConnection domainConn, String userIdent, RMPropertyFilter filter)
/*      */     {
/* 2500 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domainConn, userIdent, filter });
/*      */       
/* 2502 */       RALService ralService = RMFactory.getRALService(domainConn);
/* 2503 */       RMUser result = ralService.fetchUser(domainConn, userIdent, filter);
/*      */       
/* 2505 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2506 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class RMGroup
/*      */   {
/*      */     public static RMGroup fetchInstance(DomainConnection domainConn, String groupIdent, RMPropertyFilter filter)
/*      */     {
/* 2534 */       RMFactory.Tracer.traceMethodEntry(new Object[] { domainConn, groupIdent, filter });
/*      */       
/* 2536 */       RALService ralService = RMFactory.getRALService(domainConn);
/* 2537 */       RMGroup result = ralService.fetchGroup(domainConn, groupIdent, filter);
/*      */       
/* 2539 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2540 */       return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class WorkflowDefinition
/*      */   {
/*      */     public static RMWorkflowDefinition fetchInstance(Repository repository, String workflowIdent, RMPropertyFilter filter)
/*      */     {
/* 2568 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, workflowIdent, filter });
/* 2569 */       Util.ckNullObjParam("repository", repository);
/* 2570 */       Util.ckInvalidStrParam("workflowIdent", workflowIdent);
/*      */       
/* 2572 */       RALService ralService = RMFactory.getRALService(repository);
/* 2573 */       ContentItem result = ralService.fetchContentItem(repository, EntityType.WorkflowDefinition, workflowIdent, filter);
/*      */       
/* 2575 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2576 */       return (RMWorkflowDefinition)result;
/*      */     }
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
/*      */     public static RMWorkflowDefinition getInstance(Repository repository, String workflowIdent)
/*      */     {
/* 2594 */       RMFactory.Tracer.traceMethodEntry(new Object[] { repository, workflowIdent });
/* 2595 */       Util.ckNullObjParam("fpRepository", repository);
/* 2596 */       Util.ckInvalidStrParam("workflowIdent", workflowIdent);
/*      */       
/* 2598 */       RALService ralService = RMFactory.getRALService(repository);
/* 2599 */       ContentItem result = ralService.getContentItem(repository, EntityType.WorkflowDefinition, workflowIdent);
/*      */       
/* 2601 */       RMFactory.Tracer.traceMethodExit(new Object[] { result });
/* 2602 */       return (RMWorkflowDefinition)result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RMFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */