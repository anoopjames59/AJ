/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.collection.RMPageIterator;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordFolder;
/*     */ import com.ibm.jarm.api.core.RecordFolderContainer;
/*     */ import com.ibm.jarm.api.core.RecordVolume;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.JarmLogger;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.RMLogCode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RecordFolderImpl
/*     */   extends P8CE_BaseContainerImpl
/*     */   implements RecordFolder
/*     */ {
/*  43 */   private static final JarmLogger Logger = JarmLogger.getJarmLogger(P8CE_RecordFolderImpl.class.getName());
/*  44 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*  46 */   private static final IGenerator<RecordFolder> RecFolderGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */   private static final String[] MandatoryPropertyNames = { "AGGREGATION", "AllowedRMContainees", "AllowedRMTypes", "CurrentPhaseExecutionDate", "DateClosed", "DateOpened", "EmailSubject", "FolderName", "HomeLocation", "Id", "Inactive", "IncrementedBy", "IsDeleted", "IsHiddenContainer", "IsPermanentRecord", "Location", "Name", "OnHold", "PathName", "RecalculatePhaseRetention", "RecordFolderIdentifier", "RecordFolderName", "RecordPattern", "ReOpenedDate", "Reviewer", "RMEntityDescription", "RMEntityType" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  74 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  75 */     List<FilterElement> tempList = new ArrayList(1);
/*  76 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  77 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RecordFolder> getGenerator()
/*     */   {
/*  87 */     return RecFolderGenerator;
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  92 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  97 */     return MandatoryJaceFEs;
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
/*     */   P8CE_RecordFolderImpl(Repository repository, String identity, Folder jaceFolder, boolean isPlaceholder)
/*     */   {
/* 114 */     super(EntityType.RecordFolder, repository, identity, jaceFolder, isPlaceholder);
/* 115 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceFolder, Boolean.valueOf(isPlaceholder) });
/*     */     
/* 117 */     if (jaceFolder.getProperties().isPropertyPresent("RMEntityType"))
/*     */     {
/* 119 */       Integer rawEntityType = jaceFolder.getProperties().getInteger32Value("RMEntityType");
/* 120 */       if (rawEntityType != null)
/* 121 */         this.entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*     */     }
/* 123 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 131 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFolderUniqueIdentifier()
/*     */   {
/* 139 */     Tracer.traceMethodEntry(new Object[0]);
/* 140 */     String result = P8CE_Util.getJacePropertyAsString(this, "RecordFolderIdentifier");
/* 141 */     Tracer.traceMethodExit(new Object[] { result });
/* 142 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRecordFolderName()
/*     */   {
/* 150 */     Tracer.traceMethodEntry(new Object[0]);
/* 151 */     String result = P8CE_Util.getJacePropertyAsString(this, "RecordFolderName");
/* 152 */     Tracer.traceMethodExit(new Object[] { result });
/* 153 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void move(RecordFolderContainer destinationContainer, String reason)
/*     */   {
/* 161 */     Tracer.traceMethodEntry(new Object[] { destinationContainer, reason });
/* 162 */     super.move((Container)destinationContainer, reason);
/* 163 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 171 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 172 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 175 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 178 */       super.validateRecordFolderUpdate(getProperties());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */       synchronizeBaseFolderName("RecordFolderName", getProperties());
/*     */       
/* 186 */       super.save(jarmRefreshMode);
/* 187 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 191 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 195 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordFolder });
/*     */     }
/*     */     finally
/*     */     {
/* 199 */       if (establishedSubject) {
/* 200 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RecordVolume getActiveRecordVolume()
/*     */   {
/* 210 */     Tracer.traceMethodEntry(new Object[0]);
/* 211 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 214 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 218 */       PropertyFilter jacePF = new PropertyFilter();
/* 219 */       List<FilterElement> mandatoryRecVolumeFEs = P8CE_RecordVolumeImpl.getMandatoryJaceFEs();
/* 220 */       for (FilterElement fe : mandatoryRecVolumeFEs) {
/* 221 */         jacePF.addIncludeProperty(fe);
/*     */       }
/* 223 */       String parentId = getObjectIdentity();
/*     */       
/* 225 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "Volume", "rv");
/*     */       
/* 227 */       sb.append(" WHERE rv.").append("Parent").append(" = OBJECT('").append(parentId).append("') ");
/* 228 */       sb.append(" AND rv.").append("IsDeleted").append(" = FALSE ");
/* 229 */       sb.append(" AND rv.").append("DateClosed").append(" IS NULL");
/* 230 */       sb.append(" ORDER BY rv.").append("RecordFolderIdentifier").append(" DESC");
/* 231 */       String sqlStatement = sb.toString();
/*     */       
/*     */ 
/* 234 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 235 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*     */       
/* 237 */       Boolean continuable = Boolean.FALSE;
/* 238 */       Integer pageSize = Integer.valueOf(2);
/* 239 */       long startTime = System.currentTimeMillis();
/* 240 */       IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 241 */       long stopTime = System.currentTimeMillis();
/* 242 */       Boolean elementCountIndicator = null;
/* 243 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 245 */         elementCountIndicator = jaceFolderSet != null ? Boolean.valueOf(jaceFolderSet.isEmpty()) : null;
/*     */       }
/* 247 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement });
/*     */       
/*     */ 
/* 250 */       Folder activeVolJaceBase = null;
/* 251 */       int activeCount = 0;
/* 252 */       Iterator it; if (jaceFolderSet != null)
/*     */       {
/* 254 */         for (it = jaceFolderSet.iterator(); it.hasNext();)
/*     */         {
/* 256 */           activeCount++;
/* 257 */           if (activeCount == 1) {
/* 258 */             activeVolJaceBase = (Folder)it.next();
/*     */           } else {
/* 260 */             it.next();
/*     */           }
/*     */         }
/*     */       }
/* 264 */       if (activeCount > 1)
/*     */       {
/* 266 */         Logger.warn(RMLogCode.MULTIPLE_ACTIVE_VOLUMES, new Object[] { parentId });
/*     */       }
/*     */       
/* 269 */       RecordVolume result = null;
/* 270 */       String activeVolIdent; if (activeVolJaceBase != null)
/*     */       {
/* 272 */         activeVolIdent = activeVolJaceBase.get_Id().toString();
/* 273 */         result = new P8CE_RecordVolumeImpl(getRepository(), activeVolIdent, activeVolJaceBase, false);
/*     */       }
/*     */       
/* 276 */       Tracer.traceMethodExit(new Object[] { result });
/* 277 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 281 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 285 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_ACTIVE_VOLUME_FAILED, new Object[] { getPathName() });
/*     */     }
/*     */     finally
/*     */     {
/* 289 */       if (establishedSubject) {
/* 290 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PageableSet<RecordVolume> fetchRecordVolumes(RMPropertyFilter jarmFilter, Integer pageSize)
/*     */   {
/* 299 */     Tracer.traceMethodEntry(new Object[] { jarmFilter, pageSize });
/* 300 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 303 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 305 */       PageableSet<RecordVolume> result = null;
/*     */       
/*     */ 
/*     */ 
/* 309 */       List<FilterElement> mandatoryRecVolumeFEs = P8CE_RecordVolumeImpl.getMandatoryJaceFEs();
/* 310 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecVolumeFEs);
/*     */       
/* 312 */       String parentId = getObjectIdentity();
/*     */       
/* 314 */       StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "Volume", "rv");
/*     */       
/* 316 */       sb.append(" WHERE rv.[").append("Parent").append("] = OBJECT('").append(parentId).append("') ");
/* 317 */       sb.append(" AND rv.[").append("IsDeleted").append("] = FALSE ");
/* 318 */       sb.append(" ORDER BY rv.[").append("VolumeName").append("] ");
/* 319 */       String sqlStatement = sb.toString();
/*     */       
/*     */ 
/* 322 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 323 */       SearchScope jaceSearchScope = new SearchScope(this.jaceFolder.getObjectStore());
/*     */       
/*     */ 
/* 326 */       Boolean continuable = Boolean.TRUE;
/* 327 */       long startTime = System.currentTimeMillis();
/* 328 */       IndependentObjectSet jaceFolderSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, jacePF, continuable);
/* 329 */       long stopTime = System.currentTimeMillis();
/* 330 */       Boolean elementCountIndicator = null;
/* 331 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 333 */         elementCountIndicator = jaceFolderSet != null ? Boolean.valueOf(jaceFolderSet.isEmpty()) : null;
/*     */       }
/* 335 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, stopTime, elementCountIndicator, jaceFolderSet, new Object[] { sqlStatement });
/*     */       
/*     */ 
/*     */ 
/* 339 */       boolean supportsPaging = true;
/* 340 */       IGenerator<RecordVolume> generator = P8CE_RecordVolumeImpl.getGenerator();
/* 341 */       result = new P8CE_PageableSetImpl(this.repository, jaceFolderSet, supportsPaging, generator);
/*     */       
/* 343 */       Tracer.traceMethodExit(new Object[] { result });
/* 344 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 348 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 352 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_RECORD_VOLUMES_FAILED, new Object[] { getPathName() });
/*     */     }
/*     */     finally
/*     */     {
/* 356 */       if (establishedSubject) {
/* 357 */         P8CE_Util.disassociateSubject();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void internalDestroy(boolean doValidation, boolean isPartOfTransferProcess, String userName)
/*     */   {
/* 376 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(doValidation), Boolean.valueOf(isPartOfTransferProcess) });
/*     */     
/*     */ 
/* 379 */     boolean childDoValidation = false;
/* 380 */     Integer pageSize = null;
/* 381 */     EntityType entityType = getEntityType();
/* 382 */     if ((entityType == EntityType.PhysicalContainer) || (entityType == EntityType.PhysicalRecordFolder))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 388 */       PageableSet<RecordFolder> childRecFldrSet = fetchRecordFolders(DispositionJarmPF, pageSize);
/* 389 */       RMPageIterator<RecordFolder> childRecFldrPI = childRecFldrSet.pageIterator();
/* 390 */       while (childRecFldrPI.nextPage())
/*     */       {
/* 392 */         List<RecordFolder> currentRFList = childRecFldrPI.getCurrentPage();
/* 393 */         for (RecordFolder childRF : currentRFList)
/*     */         {
/* 395 */           ((P8CE_RecordFolderImpl)childRF).internalDestroy(childDoValidation, isPartOfTransferProcess, userName);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 400 */       PageableSet<Record> childRecSet = getRecords(pageSize);
/* 401 */       RMPageIterator<Record> childRecPI = childRecSet.pageIterator();
/* 402 */       while (childRecPI.nextPage())
/*     */       {
/* 404 */         List<Record> currentRecList = childRecPI.getCurrentPage();
/* 405 */         for (Record childRec : currentRecList)
/*     */         {
/* 407 */           if (childRec.getAssociatedRecordType() == null)
/*     */           {
/* 409 */             ((P8CE_RecordImpl)childRec).destroyFromContainer(this, childDoValidation, isPartOfTransferProcess);
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 414 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DESTROY_FAILURE_CHILD_RECORD_WITH_RECORDTYPE, new Object[] { getObjectIdentity() });
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 424 */       PageableSet<RecordVolume> childRecVolSet = fetchRecordVolumes(DispositionJarmPF, pageSize);
/* 425 */       RMPageIterator<RecordVolume> childRecVolPI = childRecVolSet.pageIterator();
/* 426 */       while (childRecVolPI.nextPage())
/*     */       {
/* 428 */         List<RecordVolume> currentRVList = childRecVolPI.getCurrentPage();
/* 429 */         for (RecordVolume childRV : currentRVList)
/*     */         {
/* 431 */           ((P8CE_RecordVolumeImpl)childRV).internalDestroy(childDoValidation, isPartOfTransferProcess, userName);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 436 */     destroyEmptyContainer(doValidation, isPartOfTransferProcess, userName);
/*     */     
/* 438 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 447 */     return super.toString("P8CE_RecordFolderImpl");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RecordFolder>
/*     */   {
/*     */     public RecordFolder create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 461 */       P8CE_RecordFolderImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 462 */       Folder jaceFolder = (Folder)jaceBaseObject;
/*     */       
/* 464 */       String identity = jaceFolder.get_Id().toString();
/* 465 */       RecordFolder result = new P8CE_RecordFolderImpl(repository, identity, jaceFolder, false);
/*     */       
/* 467 */       P8CE_RecordFolderImpl.Tracer.traceMethodExit(new Object[] { result });
/* 468 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RecordFolderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */