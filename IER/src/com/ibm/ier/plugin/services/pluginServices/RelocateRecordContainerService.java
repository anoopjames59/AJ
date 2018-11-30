/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.Container;
/*     */ import com.ibm.jarm.api.core.RecordCategory;
/*     */ import com.ibm.jarm.api.core.RecordCategoryContainer;
/*     */ import com.ibm.jarm.api.core.RecordFolder;
/*     */ import com.ibm.jarm.api.core.RecordFolderContainer;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public class RelocateRecordContainerService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  62 */     return "ierRelocateRecordContainerService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  66 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*     */     try
/*     */     {
/*  70 */       String containerId = request.getParameter("ier_containerid");
/*  71 */       String reasonForRelocate = request.getParameter("ier_reasonforrelocate");
/*  72 */       String destContainerId = request.getParameter("ier_destinationContainer");
/*     */       
/*     */ 
/*  75 */       FilePlanRepository fp_repository = getFilePlanRepository();
/*     */       
/*     */ 
/*  78 */       containerId = containerId.split(",")[2];
/*  79 */       destContainerId = destContainerId.split(",")[2];
/*     */       
/*     */ 
/*  82 */       Container container = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, containerId, null);
/*  83 */       Container destContainer = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, destContainerId, null);
/*     */       
/*     */ 
/*     */ 
/*  87 */       if ((container instanceof RecordCategory))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  92 */         RecordCategory rc = (RecordCategory)container;
/*     */         RecordCategoryContainer rcDest;
/*  94 */         if ((destContainer instanceof RecordCategoryContainer)) {
/*  95 */           rcDest = (RecordCategoryContainer)destContainer;
/*     */         } else
/*  97 */           throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_RELOCATE_INVALID_DEST_CONTAINER, new Object[] { destContainer.getName() });
/*     */         RecordCategoryContainer rcDest;
/*  99 */         rc.move(rcDest, reasonForRelocate);
/*     */       }
/* 101 */       else if ((container instanceof RecordFolder))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 106 */         RecordFolder rf = (RecordFolder)container;
/*     */         RecordFolderContainer rfDest;
/* 108 */         if ((destContainer instanceof RecordFolderContainer)) {
/* 109 */           rfDest = (RecordFolderContainer)destContainer;
/*     */         } else
/* 111 */           throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_RELOCATE_INVALID_DEST_CONTAINER, new Object[] { destContainer.getName() });
/*     */         RecordFolderContainer rfDest;
/* 113 */         rf.move(rfDest, reasonForRelocate);
/*     */       }
/*     */       else {
/* 116 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_RELOCATE_INVALID_CONTAINER, new Object[] { container.getName() });
/*     */       }
/*     */     } catch (Exception exp) {
/* 119 */       throw IERUIRuntimeException.createUIRuntimeException(exp, MessageCode.E_RELOCATE_UNEXPECTED, new Object[] { exp.getLocalizedMessage() });
/*     */     }
/*     */     
/* 122 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\RelocateRecordContainerService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */