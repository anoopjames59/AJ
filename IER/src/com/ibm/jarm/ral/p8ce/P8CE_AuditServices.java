/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.Factory.CustomEvent;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.IndependentlyPersistableObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.Subscribable;
/*     */ import com.filenet.api.events.CustomEvent;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.Property;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.ibm.jarm.api.constants.AuditStatus;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.security.RMUser;
/*     */ import com.ibm.jarm.api.util.JarmLogger;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.RMLString;
/*     */ import com.ibm.jarm.api.util.RMLogCode;
/*     */ import java.text.MessageFormat;
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
/*     */ public class P8CE_AuditServices
/*     */ {
/*  40 */   private static final JarmLogger Logger = JarmLogger.getJarmLogger(P8CE_AuditServices.class.getName());
/*  41 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
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
/*     */   public static void attachDeleteAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, boolean doSave)
/*     */   {
/*  62 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/*  64 */     String entityIdent = "";
/*     */     try
/*     */     {
/*  67 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/*  70 */         entityIdent = baseEntity.getName();
/*  71 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/*  73 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/*  76 */         String description = null;
/*  77 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/*  79 */           String pattern = RMLString.get("audit.successfulDelete", "RM Entity {0}, was successfully deleted");
/*  80 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         else
/*     */         {
/*  84 */           String pattern = RMLString.get("audit.failedDelete", "RM Entity {0}, was not deleted");
/*  85 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         
/*  88 */         attachAuditEvent(baseEntity, reason, auditStatus, description, "Delete", doSave);
/*     */       }
/*  90 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/*  94 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Delete", auditStatus, entityIdent });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void attachDestroyAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, boolean doSave)
/*     */   {
/* 115 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 117 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 120 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 123 */         entityIdent = baseEntity.getName();
/* 124 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 126 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 129 */         String description = null;
/* 130 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 132 */           String pattern = RMLString.get("audit.successfulDestroy", "RM Entity {0}, was successfully destroyed");
/* 133 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         else
/*     */         {
/* 137 */           String pattern = RMLString.get("audit.failedDestroy", "RM Entity {0}, could not be destroyed");
/* 138 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         
/* 141 */         String actualReason = reason != null ? reason.trim() : "";
/* 142 */         if ((auditStatus == AuditStatus.Success) && (actualReason.length() == 0))
/*     */         {
/* 144 */           String currentPhaseReviewComments = P8CE_Util.getJacePropertyAsString((JaceBasable)baseEntity, "CurrentPhaseReviewComments");
/* 145 */           if (currentPhaseReviewComments != null) {
/* 146 */             actualReason = currentPhaseReviewComments;
/*     */           }
/*     */         }
/* 149 */         attachAuditEvent(baseEntity, actualReason, auditStatus, description, "Destroy", doSave);
/*     */       }
/* 151 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 155 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Destroy", auditStatus, entityIdent });
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
/*     */   public static void attachExportAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, String destinationPath, boolean doSave)
/*     */   {
/* 179 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 181 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 184 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 187 */         entityIdent = baseEntity.getName();
/* 188 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 190 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 193 */         String description = null;
/* 194 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 196 */           String pattern = RMLString.get("audit.successfulExport", "RM Entity {0}, was successfully exported to {1)");
/* 197 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, destinationPath });
/*     */         }
/*     */         else
/*     */         {
/* 201 */           String pattern = RMLString.get("audit.failedExport", "RM Entity {0}, was not exported to {1}");
/* 202 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, destinationPath });
/*     */         }
/*     */         
/* 205 */         String actualReason = reason != null ? reason.trim() : "";
/* 206 */         if ((auditStatus == AuditStatus.Success) && (actualReason.length() == 0))
/*     */         {
/* 208 */           String currentPhaseReviewComments = P8CE_Util.getJacePropertyAsString((JaceBasable)baseEntity, "CurrentPhaseReviewComments");
/* 209 */           if (currentPhaseReviewComments != null) {
/* 210 */             actualReason = currentPhaseReviewComments;
/*     */           }
/*     */         }
/* 213 */         attachAuditEvent(baseEntity, actualReason, auditStatus, description, "Export", doSave);
/*     */       }
/* 215 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 219 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Export", auditStatus, entityIdent });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void attachHoldAuditEvent(BaseEntity baseEntity, String holdIdent, String reason, AuditStatus auditStatus, boolean doSave)
/*     */   {
/* 241 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 243 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 246 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 249 */         entityIdent = baseEntity.getName();
/* 250 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 252 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 255 */         String description = null;
/* 256 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 258 */           String pattern = RMLString.get("audit.successfulHold", "RM Entity {0}, was successfully placed on hold {1}");
/* 259 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, holdIdent });
/*     */         }
/*     */         else
/*     */         {
/* 263 */           String pattern = RMLString.get("audit.failedHold", "RM Entity {0}, could not be placed on hold {1}");
/* 264 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, holdIdent });
/*     */         }
/*     */         
/* 267 */         attachAuditEvent(baseEntity, reason, auditStatus, description, "Hold", doSave);
/*     */       }
/* 269 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 273 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Hold", auditStatus, entityIdent });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void attachInterimTransferAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, String destinationPath, boolean doSave)
/*     */   {
/* 295 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 297 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 300 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 303 */         entityIdent = baseEntity.getName();
/* 304 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 306 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 309 */         String description = null;
/* 310 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 312 */           String pattern = RMLString.get("audit.successfulInterimTransfer", "RM Entity {0}, was successfully interim transfered to {1)");
/* 313 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, destinationPath });
/*     */         }
/*     */         else
/*     */         {
/* 317 */           String pattern = RMLString.get("audit.failedInterimTransfer", "RM Entity {0}, could not be interim transfered to {1}");
/* 318 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, destinationPath });
/*     */         }
/*     */         
/* 321 */         String actualReason = reason != null ? reason.trim() : "";
/* 322 */         if ((auditStatus == AuditStatus.Success) && (actualReason.length() == 0))
/*     */         {
/* 324 */           String currentPhaseReviewComments = P8CE_Util.getJacePropertyAsString((JaceBasable)baseEntity, "CurrentPhaseReviewComments");
/* 325 */           if (currentPhaseReviewComments != null) {
/* 326 */             actualReason = currentPhaseReviewComments;
/*     */           }
/*     */         }
/* 329 */         attachAuditEvent(baseEntity, actualReason, auditStatus, description, "Interim Transfer", doSave);
/*     */       }
/* 331 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 335 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Interim Transfer", auditStatus, entityIdent });
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
/*     */   public static void attachRelocateAuditEvent(BaseEntity baseEntity, Folder jaceSrcContainer, Folder jaceDestContainer, String reason, AuditStatus auditStatus, boolean doSave)
/*     */   {
/* 359 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 361 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 364 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 367 */         entityIdent = baseEntity.getName();
/* 368 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 370 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/* 372 */         String description = null;
/* 373 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 375 */           String pattern = RMLString.get("audit.successfulRelocate", "RM Entity {0}, relocated from source {1} to destination {2}");
/*     */           
/* 377 */           if ((jaceSrcContainer.getProperties().isPropertyPresent("PathName")) && (jaceDestContainer.getProperties().isPropertyPresent("PathName")))
/*     */           {
/*     */ 
/* 380 */             String srcPath = jaceSrcContainer.get_PathName();
/* 381 */             String destPath = jaceDestContainer.get_PathName();
/* 382 */             description = MessageFormat.format(pattern, new Object[] { entityIdent, srcPath, destPath });
/* 383 */             if (description.length() > 1200)
/*     */             {
/* 385 */               description = null;
/*     */             }
/*     */           }
/*     */           
/* 389 */           if (description == null)
/*     */           {
/* 391 */             String srcIdent = jaceSrcContainer.getObjectReference().getObjectIdentity();
/* 392 */             String destIdent = jaceDestContainer.getObjectReference().getObjectIdentity();
/* 393 */             description = MessageFormat.format(pattern, new Object[] { entityIdent, srcIdent, destIdent });
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 398 */           String pattern = RMLString.get("audit.failedRelocate", "RM Entity {0}, could not be relocated");
/* 399 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         
/* 402 */         attachAuditEvent(baseEntity, reason, auditStatus, description, "Relocate", doSave);
/*     */       }
/* 404 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 408 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Relocate", auditStatus, entityIdent });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void attachRemoveHoldAuditEvent(BaseEntity baseEntity, String holdIdent, String reason, AuditStatus auditStatus, boolean doSave)
/*     */   {
/* 430 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 432 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 435 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 438 */         entityIdent = baseEntity.getName();
/* 439 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 441 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 444 */         String description = null;
/* 445 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 447 */           String pattern = RMLString.get("audit.successfulRemoveHold", "RMEntity {0}, was successfully removed from hold {1}");
/* 448 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, holdIdent });
/*     */         }
/*     */         else
/*     */         {
/* 452 */           String pattern = RMLString.get("audit.failedRemoveHold", "RMEntity {0}, could not be removed from hold {1}");
/* 453 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, holdIdent });
/*     */         }
/*     */         
/* 456 */         attachAuditEvent(baseEntity, reason, auditStatus, description, "Remove Hold", doSave);
/*     */       }
/* 458 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 462 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Remove Hold", auditStatus, entityIdent });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void attachReviewAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, boolean doSave)
/*     */   {
/* 483 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 485 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 488 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 491 */         entityIdent = baseEntity.getName();
/* 492 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 494 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 497 */         String description = null;
/* 498 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 500 */           String pattern = RMLString.get("audit.successfulReview", "RM Entity {0}, was successfully reviewed");
/* 501 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         else
/*     */         {
/* 505 */           String pattern = RMLString.get("audit.failedReview", "RM Entity {0}, could not be reviewed");
/* 506 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         
/* 509 */         attachAuditEvent(baseEntity, reason, auditStatus, description, "Review", doSave);
/*     */       }
/* 511 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 515 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Review", auditStatus, entityIdent });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void attachTransferAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, String destinationPath, boolean doSave)
/*     */   {
/* 537 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 539 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 542 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 545 */         entityIdent = baseEntity.getName();
/* 546 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 548 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 551 */         String description = null;
/* 552 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 554 */           String pattern = RMLString.get("audit.successfulTransfer", "RM Entity {0}, was successfully transfered to {1}");
/* 555 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, destinationPath });
/*     */         }
/*     */         else
/*     */         {
/* 559 */           String pattern = RMLString.get("audit.failedTransfer", "RM Entity {0}, could not be transfered to {1}");
/* 560 */           description = MessageFormat.format(pattern, new Object[] { entityIdent, destinationPath });
/*     */         }
/*     */         
/* 563 */         attachAuditEvent(baseEntity, reason, auditStatus, description, "Transfer", doSave);
/*     */       }
/* 565 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 569 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Transfer", auditStatus, entityIdent });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void attachUndeclareAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, boolean doSave)
/*     */   {
/* 589 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, Boolean.valueOf(doSave) });
/*     */     
/* 591 */     String entityIdent = "";
/*     */     try
/*     */     {
/* 594 */       if (((P8CE_RepositoryImpl)baseEntity.getRepository()).isAuditEnabled())
/*     */       {
/*     */ 
/* 597 */         entityIdent = baseEntity.getName();
/* 598 */         if ((entityIdent == null) || (entityIdent.trim().length() == 0))
/*     */         {
/* 600 */           entityIdent = baseEntity.getObjectIdentity();
/*     */         }
/*     */         
/* 603 */         String description = null;
/* 604 */         if (auditStatus == AuditStatus.Success)
/*     */         {
/* 606 */           String pattern = RMLString.get("audit.successfulUndeclare", "RM Record {0}, was successfully undeclared");
/* 607 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         else
/*     */         {
/* 611 */           String pattern = RMLString.get("audit.failedUndeclare", "RM Record {0}, could not be undeclared");
/* 612 */           description = MessageFormat.format(pattern, new Object[] { entityIdent });
/*     */         }
/*     */         
/* 615 */         attachAuditEvent(baseEntity, reason, auditStatus, description, "Undeclare", doSave);
/*     */       }
/* 617 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 621 */       Logger.warn(RMLogCode.RMAUDIT_CREATION_FAILURE, new Object[] { "Undeclare", auditStatus, entityIdent });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void attachAuditEvent(BaseEntity baseEntity, String reason, AuditStatus auditStatus, String description, String auditActionType, boolean doSave)
/*     */   {
/* 630 */     Tracer.traceMethodEntry(new Object[] { baseEntity, reason, auditStatus, description, auditActionType, Boolean.valueOf(doSave) });
/*     */     
/*     */ 
/* 633 */     String reasonForAction = generateAuditReason(auditStatus, reason);
/*     */     
/*     */ 
/* 636 */     String reviewer = determineReviewer(baseEntity);
/*     */     
/* 638 */     Repository jarmRepository = baseEntity.getRepository();
/* 639 */     ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)jarmRepository).getJaceObjectStore();
/*     */     
/* 641 */     CustomEvent jaceEvent = Factory.CustomEvent.createInstance(jaceObjStore, "RMAudit");
/* 642 */     Properties eventProps = jaceEvent.getProperties();
/* 643 */     eventProps.putValue("AuditActionType", auditActionType);
/* 644 */     eventProps.putValue("ReasonForAction", reasonForAction);
/* 645 */     eventProps.putValue("RMEntityDescription", description);
/* 646 */     eventProps.putValue("Reviewer", reviewer);
/* 647 */     eventProps.putValue("EventStatus", auditStatus.getIntValue());
/*     */     
/* 649 */     Subscribable jaceEntityBase = (Subscribable)((JaceBasable)baseEntity).getJaceBaseObject();
/* 650 */     if (auditStatus == AuditStatus.Failure)
/*     */     {
/* 652 */       ((IndependentlyPersistableObject)jaceEntityBase).clearPendingActions();
/*     */     }
/* 654 */     jaceEntityBase.raiseEvent(jaceEvent);
/*     */     
/* 656 */     if (doSave)
/*     */     {
/* 658 */       long startTime = System.currentTimeMillis();
/* 659 */       ((IndependentlyPersistableObject)jaceEntityBase).save(RefreshMode.REFRESH);
/* 660 */       long endTime = System.currentTimeMillis();
/* 661 */       Tracer.traceExtCall("IndependentlyPersistableObject.save", startTime, endTime, null, null, new Object[0]);
/*     */     }
/*     */     
/* 664 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   private static String determineReviewer(BaseEntity baseEntity)
/*     */   {
/* 670 */     Tracer.traceMethodEntry(new Object[] { baseEntity });
/* 671 */     String reviewerValue = null;
/*     */     
/*     */ 
/* 674 */     IndependentObject jaceEntityBase = (IndependentObject)((JaceBasable)baseEntity).getJaceBaseObject();
/*     */     
/* 676 */     PropertyFilter jacePF = new PropertyFilter();
/* 677 */     jacePF.addIncludeProperty(0, null, null, "LastReviewedBy", null);
/* 678 */     Property jaceReviewerProp = P8CE_Util.getOrFetchJaceProperty(jaceEntityBase, "LastReviewedBy", jacePF);
/* 679 */     if (jaceReviewerProp != null) {
/* 680 */       reviewerValue = jaceReviewerProp.getStringValue();
/*     */     }
/*     */     
/* 683 */     if ((reviewerValue == null) || (reviewerValue.trim().length() == 0))
/*     */     {
/* 685 */       RMUser currentUser = baseEntity.getRepository().getDomain().fetchCurrentUser();
/* 686 */       if (currentUser != null) {
/* 687 */         reviewerValue = currentUser.getShortName();
/*     */       }
/*     */     }
/* 690 */     Tracer.traceMethodExit(new Object[] { reviewerValue });
/* 691 */     return reviewerValue;
/*     */   }
/*     */   
/*     */   private static String generateAuditReason(AuditStatus auditStatus, String reason)
/*     */   {
/* 696 */     Tracer.traceMethodEntry(new Object[] { auditStatus, reason });
/* 697 */     String result = reason;
/* 698 */     if (auditStatus == AuditStatus.Failure)
/*     */     {
/* 700 */       String pattern = RMLString.get("audit.reasonForFailure", "Reason For Failure: {0}");
/* 701 */       result = MessageFormat.format(pattern, new Object[] { reason });
/*     */     }
/*     */     
/* 704 */     Tracer.traceMethodExit(new Object[] { result });
/* 705 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_AuditServices.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */