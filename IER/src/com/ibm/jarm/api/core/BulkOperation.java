/*     */ package com.ibm.jarm.api.core;
/*     */ 
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.RALService;
/*     */ import com.ibm.jarm.ral.common.RALBulkOperation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BulkOperation
/*     */ {
/*  29 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
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
/*     */   public static List<BulkItemResult> activateContainers(FilePlanRepository repository, List<String> entityIdents)
/*     */   {
/*  51 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents });
/*  52 */     Util.ckNullObjParam("repository", repository);
/*  53 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/*     */     
/*  55 */     List<BulkItemResult> results = getRALBulkOperation(repository).activateContainers(repository, entityIdents);
/*     */     
/*  57 */     Tracer.traceMethodExit(new Object[] { results });
/*  58 */     return results;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<BulkItemResult> inactivateContainers(FilePlanRepository repository, List<String> entityIdents, String commonReasonForInactivate)
/*     */   {
/*  79 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, commonReasonForInactivate });
/*  80 */     Util.ckNullObjParam("repository", repository);
/*  81 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/*     */     
/*  83 */     List<BulkItemResult> results = getRALBulkOperation(repository).inactivateContainers(repository, entityIdents, commonReasonForInactivate);
/*     */     
/*  85 */     Tracer.traceMethodExit(new Object[] { results });
/*  86 */     return results;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<BulkItemResult> closeContainers(FilePlanRepository repository, List<String> entityIdents, String commonReasonForClose)
/*     */   {
/* 107 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, commonReasonForClose });
/* 108 */     Util.ckNullObjParam("repository", repository);
/* 109 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/*     */     
/* 111 */     List<BulkItemResult> results = getRALBulkOperation(repository).closeContainers(repository, entityIdents, commonReasonForClose);
/*     */     
/* 113 */     Tracer.traceMethodExit(new Object[] { results });
/* 114 */     return results;
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
/*     */   public static List<BulkItemResult> reopenContainers(FilePlanRepository repository, List<String> entityIdents)
/*     */   {
/* 132 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents });
/* 133 */     Util.ckNullObjParam("repository", repository);
/* 134 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/*     */     
/* 136 */     List<BulkItemResult> results = getRALBulkOperation(repository).reopenContainers(repository, entityIdents);
/*     */     
/* 138 */     Tracer.traceMethodExit(new Object[] { results });
/* 139 */     return results;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<BulkItemResult> placeHolds(FilePlanRepository repository, List<EntityDescription> entityDescriptions, List<String> holdIdents)
/*     */   {
/* 162 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescriptions, holdIdents });
/* 163 */     Util.ckNullObjParam("repository", repository);
/* 164 */     Util.ckNullOrInvalidCollectionParam("entityDescriptions", entityDescriptions);
/* 165 */     Util.ckNullOrInvalidCollectionParam("holdIdents", holdIdents);
/*     */     
/* 167 */     List<BulkItemResult> results = getRALBulkOperation(repository).placeHolds(repository, entityDescriptions, holdIdents);
/*     */     
/* 169 */     Tracer.traceMethodExit(new Object[] { results });
/* 170 */     return results;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<BulkItemResult> removeHolds(FilePlanRepository repository, EntityDescription entityDescription, List<String> holdIdents)
/*     */   {
/* 193 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescription, holdIdents });
/* 194 */     Util.ckNullObjParam("repository", repository);
/* 195 */     Util.ckNullObjParam("entityDescription", entityDescription);
/* 196 */     Util.ckNullOrInvalidCollectionParam("holdIdents", holdIdents);
/*     */     
/* 198 */     List<BulkItemResult> results = getRALBulkOperation(repository).removeHolds(repository, entityDescription, holdIdents);
/*     */     
/* 200 */     Tracer.traceMethodExit(new Object[] { results });
/* 201 */     return results;
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
/*     */   public static List<BulkItemResult> delete(FilePlanRepository repository, List<EntityDescription> entityDescriptions)
/*     */   {
/* 219 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescriptions });
/* 220 */     Util.ckNullObjParam("repository", repository);
/* 221 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityDescriptions);
/*     */     
/* 223 */     List<BulkItemResult> results = getRALBulkOperation(repository).delete(repository, entityDescriptions);
/*     */     
/* 225 */     Tracer.traceMethodExit(new Object[] { results });
/* 226 */     return results;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<BulkItemResult> fileRecords(FilePlanRepository repository, List<String> entityIdents, String destinationContainerIdent)
/*     */   {
/* 248 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, destinationContainerIdent });
/* 249 */     Util.ckNullObjParam("repository", repository);
/* 250 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/* 251 */     Util.ckInvalidStrParam("destinationContainerIdent", destinationContainerIdent);
/*     */     
/* 253 */     List<BulkItemResult> results = getRALBulkOperation(repository).fileRecords(repository, entityIdents, destinationContainerIdent);
/*     */     
/* 255 */     Tracer.traceMethodExit(new Object[] { results });
/* 256 */     return results;
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
/*     */   public static List<BulkItemResult> moveRecords(FilePlanRepository repository, List<String> entityIdents, String sourceContainerIdent, String destinationContainerIdent, String reasonForMove)
/*     */   {
/* 284 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, sourceContainerIdent, destinationContainerIdent, reasonForMove });
/* 285 */     Util.ckNullObjParam("repository", repository);
/* 286 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/* 287 */     Util.ckInvalidStrParam("sourceContainerIdent", sourceContainerIdent);
/* 288 */     Util.ckInvalidStrParam("destinationContainerIdent", destinationContainerIdent);
/* 289 */     Util.ckInvalidStrParam("reasonForMove", reasonForMove);
/*     */     
/* 291 */     List<BulkItemResult> results = getRALBulkOperation(repository).moveRecords(repository, entityIdents, sourceContainerIdent, destinationContainerIdent, reasonForMove);
/*     */     
/*     */ 
/*     */ 
/* 295 */     Tracer.traceMethodExit(new Object[] { results });
/* 296 */     return results;
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
/*     */ 
/*     */   public static List<BulkItemResult> copyRecords(FilePlanRepository repository, List<String> entityIdents, String destinationContainerIdent, String namePrefix)
/*     */   {
/* 315 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents, destinationContainerIdent, namePrefix });
/* 316 */     Util.ckNullObjParam("repository", repository);
/* 317 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/* 318 */     Util.ckInvalidStrParam("destinationContainerIdent", destinationContainerIdent);
/*     */     
/* 320 */     List<BulkItemResult> results = getRALBulkOperation(repository).copyRecords(repository, entityIdents, destinationContainerIdent, namePrefix);
/*     */     
/* 322 */     Tracer.traceMethodExit(new Object[] { results });
/* 323 */     return results;
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
/*     */   public static List<BulkItemResult> undeclareRecords(FilePlanRepository repository, List<String> entityIdents)
/*     */   {
/* 340 */     Tracer.traceMethodEntry(new Object[] { repository, entityIdents });
/* 341 */     Util.ckNullObjParam("repository", repository);
/* 342 */     Util.ckNullOrInvalidCollectionParam("entityIdents", entityIdents);
/*     */     
/* 344 */     List<BulkItemResult> results = getRALBulkOperation(repository).undeclareRecords(repository, entityIdents);
/*     */     
/* 346 */     Tracer.traceMethodExit(new Object[] { results });
/* 347 */     return results;
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
/*     */ 
/*     */   public static List<BulkItemResult> initiateDisposition(FilePlanRepository repository, List<EntityDescription> entityDescriptions, Object vwSession)
/*     */   {
/* 366 */     Tracer.traceMethodEntry(new Object[] { repository, entityDescriptions, vwSession });
/* 367 */     Util.ckNullObjParam("repository", repository);
/* 368 */     Util.ckNullOrInvalidCollectionParam("entityDescriptions", entityDescriptions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 373 */     List<BulkItemResult> results = getRALBulkOperation(repository).initiateDisposition(repository, entityDescriptions, vwSession);
/*     */     
/* 375 */     Tracer.traceMethodExit(new Object[] { results });
/* 376 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */   private static RALBulkOperation getRALBulkOperation(FilePlanRepository repository)
/*     */   {
/* 382 */     Tracer.traceMethodEntry(new Object[] { repository });
/* 383 */     RMDomain domain = repository.getDomain();
/* 384 */     RALService ralService = RMFactory.getRALService(domain);
/* 385 */     RALBulkOperation result = ralService.getRALBulkOperation();
/*     */     
/* 387 */     Tracer.traceMethodExit(new Object[] { result });
/* 388 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class EntityDescription
/*     */   {
/*     */     private EntityType entityType;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String entityIdent;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public EntityDescription(EntityType entityType, String entityIdent)
/*     */     {
/* 409 */       this.entityType = entityType;
/* 410 */       this.entityIdent = entityIdent;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public EntityType getEntityType()
/*     */     {
/* 420 */       return this.entityType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getEntityIdent()
/*     */     {
/* 430 */       return this.entityIdent;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\BulkOperation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */