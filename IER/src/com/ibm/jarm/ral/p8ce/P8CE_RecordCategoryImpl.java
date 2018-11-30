/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.collection.RMPageIterator;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordCategory;
/*     */ import com.ibm.jarm.api.core.RecordCategoryContainer;
/*     */ import com.ibm.jarm.api.core.RecordFolder;
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
/*     */ class P8CE_RecordCategoryImpl
/*     */   extends P8CE_BaseContainerImpl
/*     */   implements RecordCategory
/*     */ {
/*  32 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  33 */   private static final IGenerator<RecordCategory> RecCatGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */   private static final String[] MandatoryPropertyNames = { "AGGREGATION", "AllowedRMContainees", "AllowedRMTypes", "CurrentPhaseExecutionDate", "DateClosed", "DateOpened", "EmailSubject", "FolderName", "HomeLocation", "Id", "Inactive", "IncrementedBy", "IsDeleted", "IsHiddenContainer", "IsPermanentRecord", "IsVitalRecord", "Location", "Name", "OnHold", "PathName", "RecalculatePhaseRetention", "RecordCategoryIdentifier", "RecordCategoryName", "RecordPattern", "ReOpenedDate", "Reviewer", "RMEntityType" };
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
/*  63 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  64 */     List<FilterElement> tempList = new ArrayList(1);
/*  65 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  66 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  71 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  76 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RecordCategory> getGenerator()
/*     */   {
/*  86 */     return RecCatGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  95 */     return getMandatoryJaceFEs();
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
/*     */   P8CE_RecordCategoryImpl(Repository repository, String identity, Folder jaceFolder, boolean isPlaceholder)
/*     */   {
/* 111 */     super(EntityType.RecordCategory, repository, identity, jaceFolder, isPlaceholder);
/* 112 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceFolder });
/* 113 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRecordCategoryIdentifier()
/*     */   {
/* 121 */     Tracer.traceMethodEntry(new Object[0]);
/* 122 */     String result = P8CE_Util.getJacePropertyAsString(this, "RecordCategoryIdentifier");
/* 123 */     Tracer.traceMethodExit(new Object[] { result });
/* 124 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRecordCategoryName()
/*     */   {
/* 132 */     Tracer.traceMethodEntry(new Object[0]);
/* 133 */     String result = P8CE_Util.getJacePropertyAsString(this, "RecordCategoryName");
/* 134 */     Tracer.traceMethodExit(new Object[] { result });
/* 135 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void move(RecordCategoryContainer destinationContainer, String reason)
/*     */   {
/* 143 */     Tracer.traceMethodEntry(new Object[] { destinationContainer, reason });
/* 144 */     super.move((Container)destinationContainer, reason);
/* 145 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 153 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 154 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 157 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 160 */       super.validateRecordCategoryUpdate(getProperties());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 166 */       synchronizeBaseFolderName("RecordCategoryName", getProperties());
/*     */       
/* 168 */       super.save(jarmRefreshMode);
/* 169 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 173 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 177 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordCategory });
/*     */     }
/*     */     finally
/*     */     {
/* 181 */       if (establishedSubject) {
/* 182 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canContainDefensibleDisposalContainer()
/*     */   {
/* 192 */     Tracer.traceMethodEntry(new Object[0]);
/* 193 */     RMRuntimeException noDDEx = super.verifyDDMetadataIsInstalled();
/* 194 */     boolean result = noDDEx == null;
/* 195 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 196 */     return result;
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
/*     */   protected void internalDestroy(boolean doValidation, boolean isPartOfTransferProcess, String userName)
/*     */   {
/* 215 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(doValidation), Boolean.valueOf(isPartOfTransferProcess) });
/*     */     
/*     */ 
/* 218 */     boolean childDoValidation = false;
/* 219 */     Integer pageSize = null;
/*     */     
/*     */ 
/* 222 */     PageableSet<RecordCategory> childRecCatSet = fetchRecordCategories(DispositionJarmPF, pageSize);
/* 223 */     RMPageIterator<RecordCategory> childRecCatPI = childRecCatSet.pageIterator();
/* 224 */     while (childRecCatPI.nextPage())
/*     */     {
/* 226 */       List<RecordCategory> currentRCList = childRecCatPI.getCurrentPage();
/* 227 */       for (RecordCategory childRC : currentRCList)
/*     */       {
/* 229 */         ((P8CE_RecordCategoryImpl)childRC).internalDestroy(childDoValidation, isPartOfTransferProcess, userName);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 234 */     PageableSet<RecordFolder> childRecFldrSet = fetchRecordFolders(DispositionJarmPF, pageSize);
/* 235 */     RMPageIterator<RecordFolder> childRecFldrPI = childRecFldrSet.pageIterator();
/* 236 */     while (childRecFldrPI.nextPage())
/*     */     {
/* 238 */       List<RecordFolder> currentRFList = childRecFldrPI.getCurrentPage();
/* 239 */       for (RecordFolder childRF : currentRFList)
/*     */       {
/* 241 */         ((P8CE_RecordFolderImpl)childRF).internalDestroy(childDoValidation, isPartOfTransferProcess, userName);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 246 */     PageableSet<Record> childRecSet = getRecords(pageSize);
/* 247 */     RMPageIterator<Record> childRecPI = childRecSet.pageIterator();
/* 248 */     while (childRecPI.nextPage())
/*     */     {
/* 250 */       List<Record> currentRecList = childRecPI.getCurrentPage();
/* 251 */       for (Record childRec : currentRecList)
/*     */       {
/* 253 */         if (childRec.getAssociatedRecordType() == null)
/*     */         {
/* 255 */           ((P8CE_RecordImpl)childRec).destroyFromContainer(this, childDoValidation, isPartOfTransferProcess);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 260 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_DESTROY_FAILURE_CHILD_RECORD_WITH_RECORDTYPE, new Object[] { getObjectIdentity() });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 265 */     destroyEmptyContainer(doValidation, isPartOfTransferProcess, userName);
/*     */     
/* 267 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 276 */     return super.toString("P8CE_RecordCategoryImpl");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RecordCategory>
/*     */   {
/*     */     public RecordCategory create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 290 */       P8CE_RecordCategoryImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 291 */       Folder jaceFolder = (Folder)jaceBaseObject;
/*     */       
/* 293 */       String identity = jaceFolder.get_Id().toString();
/* 294 */       RecordCategory result = new P8CE_RecordCategoryImpl(repository, identity, jaceFolder, false);
/*     */       
/* 296 */       P8CE_RecordCategoryImpl.Tracer.traceMethodExit(new Object[] { result });
/* 297 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RecordCategoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */