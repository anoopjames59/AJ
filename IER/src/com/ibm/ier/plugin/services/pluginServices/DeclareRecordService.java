/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.DataModelType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RepositoryType;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.ContentRepository;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.Container;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class DeclareRecordService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  75 */     return "ierDeclareRecordService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  79 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  81 */     String fpLocation = request.getParameter("ier_fileplanRepositoryFolderLocation");
/*  82 */     String recordClassSymbolicName = request.getParameter("ier_recordClass");
/*  83 */     String numberOfDocuments = request.getParameter("ier_ndocs");
/*  84 */     String fpRepo_nexusRepositoryId = request.getParameter("ier_fileplanRepositoryNexusId");
/*  85 */     int ndocs = Integer.parseInt(numberOfDocuments);
/*     */     
/*  87 */     JSONObject requestContent = getRequestContent();
/*  88 */     JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*     */     
/*     */ 
/*  91 */     FilePlanRepository fp_repository = (FilePlanRepository)getRepository(getBaseService().getP8RepositoryId(fpRepo_nexusRepositoryId));
/*     */     
/*     */ 
/*  94 */     String fpLocationId = IERUtil.getIdFromDocIdString(fpLocation);
/*     */     
/*     */ 
/*  97 */     RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, recordClassSymbolicName, request);
/*  98 */     RMProperties props = MediatorUtil.getProperties(cd, criterias, fp_repository);
/*  99 */     if (fp_repository.getDataModelType() == DataModelType.DoDClassified) {
/* 100 */       props.putStringValue("CurrentClassification", "Unclassified");
/*     */     }
/*     */     
/*     */ 
/* 104 */     Repository repository = getRepository(getBaseService().getP8RepositoryId());
/*     */     
/*     */ 
/* 107 */     if ((repository.getRepositoryType() != RepositoryType.Combined) && (repository.getRepositoryType() != RepositoryType.Content)) {
/* 108 */       throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_INVALID_CONTENT_REPOSITORY, new Object[0]);
/*     */     }
/* 110 */     ContentRepository content_repository = (ContentRepository)repository;
/*     */     
/*     */ 
/* 113 */     List<String> contentListIds = new ArrayList();
/* 114 */     for (int i = 0; i < ndocs; i++)
/*     */     {
/*     */ 
/* 117 */       contentListIds.add(IERUtil.getIdFromDocIdString(request.getParameter("ier_docId" + i)));
/*     */     }
/*     */     
/*     */ 
/* 121 */     Container container = RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, fpLocationId, RMPropertyFilter.MinimumPropertySet);
/* 122 */     if ((container instanceof RecordContainer)) {
/*     */       try {
/* 124 */         RecordContainer rc = (RecordContainer)container;
/* 125 */         rc.declare(recordClassSymbolicName, props, null, null, content_repository, contentListIds);
/*     */       }
/*     */       catch (RMRuntimeException ierExp)
/*     */       {
/* 129 */         if ((ierExp.getCause() != null) && ((ierExp.getCause() instanceof EngineRuntimeException))) {
/* 130 */           EngineRuntimeException ceExp = (EngineRuntimeException)ierExp.getCause();
/* 131 */           if (ceExp.getExceptionCode() == ExceptionCode.E_READ_ONLY) {
/* 132 */             throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_DECLARE_READONLY, new Object[0]);
/*     */           }
/* 134 */           throw ierExp;
/*     */         }
/*     */         
/* 137 */         throw ierExp;
/*     */       }
/*     */       
/*     */     } else {
/* 141 */       throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_DECLARE_INVALID_CONTAINER, new Object[0]);
/*     */     }
/* 143 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\DeclareRecordService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */