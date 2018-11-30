/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.collection.RMPageIterator;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordVolume;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RecordVolumeImpl
/*     */   extends P8CE_BaseContainerImpl
/*     */   implements RecordVolume
/*     */ {
/*     */   public static final String DefaultNamingPattern = "00000";
/*  31 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  32 */   private static final IGenerator<RecordVolume> RecVolumeGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  42 */   private static final String[] MandatoryPropertyNames = { "AGGREGATION", "AllowedRMContainees", "AllowedRMTypes", "CurrentPhaseExecutionDate", "DateClosed", "DateOpened", "EmailSubject", "FolderName", "HomeLocation", "Id", "IsDeleted", "IsHiddenContainer", "Location", "Name", "OnHold", "PathName", "RecalculatePhaseRetention", "RecordFolderIdentifier", "ReOpenedDate", "Reviewer", "RMEntityDescription", "RMEntityType", "VolumeName" };
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
/*     */   static
/*     */   {
/*  58 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  59 */     List<FilterElement> tempList = new ArrayList(1);
/*  60 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  61 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RecordVolume> getGenerator()
/*     */   {
/*  71 */     return RecVolumeGenerator;
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  76 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  81 */     return MandatoryJaceFEs;
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
/*     */   P8CE_RecordVolumeImpl(Repository repository, String identity, Folder jaceFolder, boolean isPlaceholder)
/*     */   {
/*  98 */     super(EntityType.RecordVolume, repository, identity, jaceFolder, isPlaceholder);
/*  99 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceFolder, Boolean.valueOf(isPlaceholder) });
/* 100 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 108 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFolderUniqueIdentifier()
/*     */   {
/* 116 */     return P8CE_Util.getJacePropertyAsString(this, "RecordFolderIdentifier");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVolumeName()
/*     */   {
/* 124 */     return P8CE_Util.getJacePropertyAsString(this, "VolumeName");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 132 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 133 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 136 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 139 */       super.validateRecordVolumeUpdate(getProperties());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 145 */       synchronizeBaseFolderName("VolumeName", getProperties());
/*     */       
/* 147 */       super.save(jarmRefreshMode);
/* 148 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 152 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 156 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordVolume });
/*     */     }
/*     */     finally
/*     */     {
/* 160 */       if (establishedSubject) {
/* 161 */         P8CE_Util.disassociateSubject();
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
/* 180 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(doValidation), Boolean.valueOf(isPartOfTransferProcess) });
/*     */     
/*     */ 
/* 183 */     boolean childDoValidation = false;
/* 184 */     Integer pageSize = null;
/* 185 */     PageableSet<Record> childRecSet = getRecords(pageSize);
/* 186 */     RMPageIterator<Record> childRecPI = childRecSet.pageIterator();
/* 187 */     while (childRecPI.nextPage())
/*     */     {
/* 189 */       List<Record> currentRecList = childRecPI.getCurrentPage();
/* 190 */       for (Record childRec : currentRecList)
/*     */       {
/* 192 */         if (childRec.getAssociatedRecordType() == null)
/*     */         {
/* 194 */           ((P8CE_RecordImpl)childRec).destroyFromContainer(this, childDoValidation, isPartOfTransferProcess);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 199 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DESTROY_FAILURE_CHILD_RECORD_WITH_RECORDTYPE, new Object[] { getObjectIdentity() });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 204 */     destroyEmptyContainer(doValidation, isPartOfTransferProcess, userName);
/*     */     
/* 206 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 215 */     return super.toString("P8CE_RecordVolumeImpl");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RecordVolume>
/*     */   {
/*     */     public RecordVolume create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 228 */       P8CE_RecordVolumeImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 229 */       Folder jaceFolder = (Folder)jaceBaseObject;
/*     */       
/* 231 */       String identity = jaceFolder.get_Id().toString();
/* 232 */       RecordVolume result = new P8CE_RecordVolumeImpl(repository, identity, jaceFolder, false);
/*     */       
/* 234 */       P8CE_RecordVolumeImpl.Tracer.traceMethodExit(new Object[] { result });
/* 235 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RecordVolumeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */