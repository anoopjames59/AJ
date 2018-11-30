/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.ContentElementList;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.constants.VersionStatus;
/*     */ import com.filenet.api.core.ContentElement;
/*     */ import com.filenet.api.core.ContentTransfer;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.VersionSeries;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.Property;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.ibm.jarm.api.constants.ContentXMLExport;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.ContentItem;
/*     */ import com.ibm.jarm.api.core.RMContentElement;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*     */ import com.ibm.jarm.ral.common.RAL_XMLSupport;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_ContentItemImpl
/*     */   extends RALBaseEntity
/*     */   implements ContentItem, JaceBasable
/*     */ {
/*  57 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  58 */   private static final IGenerator<ContentItem> ContentItemGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private static final String[] MandatoryPropertyNames = { "Id", "DocumentTitle", "Name", "MimeType", "CanDeclare", "RecordInformation", "VersionStatus" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   static final PropertyFilter JaceContentElementPF;
/*     */   private com.filenet.api.core.Document jaceDocument;
/*     */   
/*     */   static {
/*  77 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  79 */     List<FilterElement> tempList = new ArrayList(1);
/*  80 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  81 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */     
/*     */ 
/*  84 */     String[] contentElementPropNames = { "ContentType", "ContentLocation", "ContentSize", "RetrievalName" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  89 */     String names = P8CE_Util.createSpaceSeparatedString(contentElementPropNames);
/*  90 */     JaceContentElementPF = new PropertyFilter();
/*  91 */     Long maxContentSize = null;
/*  92 */     JaceContentElementPF.addIncludeProperty(1, maxContentSize, null, names, null);
/*     */   }
/*     */   
/*     */ 
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  98 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/* 103 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<ContentItem> getContentItemGenerator()
/*     */   {
/* 113 */     return ContentItemGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 121 */     return getMandatoryJaceFEs();
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
/*     */   public P8CE_ContentItemImpl(EntityType entityType, Repository repository, String identity, com.filenet.api.core.Document jaceDocument, boolean isPlaceholder)
/*     */   {
/* 140 */     super(entityType, repository, identity, isPlaceholder);
/* 141 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, jaceDocument, Boolean.valueOf(isPlaceholder) });
/* 142 */     this.jaceDocument = jaceDocument;
/* 143 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Record getAssociatedRecord()
/*     */   {
/* 151 */     Tracer.traceMethodEntry(new Object[0]);
/* 152 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 155 */       establishedSubject = P8CE_Util.associateSubject();
/* 156 */       Record result = null;
/*     */       
/* 158 */       EngineObject jaceEngObj = P8CE_Util.getJacePropertyAsEngineObject(this, "RecordInformation");
/* 159 */       PropertyFilter jacePF; if ((jaceEngObj != null) && ((jaceEngObj instanceof com.filenet.api.core.Document)))
/*     */       {
/*     */ 
/*     */ 
/* 163 */         jacePF = new PropertyFilter();
/* 164 */         List<FilterElement> mandatoryFEs = P8CE_RecordImpl.getMandatoryJaceFEs();
/* 165 */         for (FilterElement fe : mandatoryFEs)
/*     */         {
/* 167 */           jacePF.addIncludeProperty(fe);
/*     */         }
/*     */         
/* 170 */         P8CE_Util.fetchAdditionalJaceProperties((com.filenet.api.core.Document)jaceEngObj, jacePF);
/*     */         
/* 172 */         IGenerator<Record> recGenerator = P8CE_RecordImpl.getGenerator();
/*     */         
/*     */ 
/*     */ 
/* 176 */         Repository recordRepos = null;
/* 177 */         result = (Record)recGenerator.create(recordRepos, jaceEngObj);
/*     */       }
/*     */       
/* 180 */       Tracer.traceMethodExit(new Object[] { result });
/* 181 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 185 */       if (establishedSubject) {
/* 186 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<RMContentElement> getContentElements()
/*     */   {
/* 195 */     Tracer.traceMethodEntry(new Object[0]);
/* 196 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 199 */       establishedSubject = P8CE_Util.associateSubject();
/* 200 */       List<RMContentElement> result = Collections.emptyList();
/*     */       
/* 202 */       Property contentElementsProp = P8CE_Util.getOrFetchJaceProperty(this.jaceDocument, "ContentElements", JaceContentElementPF);
/* 203 */       ContentElementList jaceContentElements; if (contentElementsProp != null)
/*     */       {
/* 205 */         jaceContentElements = (ContentElementList)contentElementsProp.getDependentObjectListValue();
/* 206 */         if (jaceContentElements != null)
/*     */         {
/* 208 */           result = new ArrayList(jaceContentElements.size());
/*     */           
/* 210 */           ContentElement jaceContentElement = null;
/* 211 */           for (int i = 0; i < jaceContentElements.size(); i++)
/*     */           {
/* 213 */             jaceContentElement = (ContentElement)jaceContentElements.get(i);
/* 214 */             if (jaceContentElement != null)
/*     */             {
/* 216 */               result.add(new P8CE_RMContentElementImpl(jaceContentElement));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 222 */       Tracer.traceMethodExit(new Object[] { result });
/* 223 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 227 */       if (establishedSubject) {
/* 228 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getMimeType()
/*     */   {
/* 237 */     return this.jaceDocument.get_MimeType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEligibleForDeclaration()
/*     */   {
/* 245 */     Tracer.traceMethodEntry(new Object[0]);
/* 246 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 249 */       establishedSubject = P8CE_Util.associateSubject();
/* 250 */       boolean isEligibleSoFar = true;
/*     */       
/*     */ 
/* 253 */       Boolean canDeclareValue = P8CE_Util.getJacePropertyAsBoolean(this, "CanDeclare");
/* 254 */       if ((canDeclareValue == null) || (!canDeclareValue.booleanValue())) {
/* 255 */         isEligibleSoFar = false;
/*     */       }
/* 257 */       if (isEligibleSoFar)
/*     */       {
/*     */ 
/* 260 */         VersionStatus jaceVersionStatus = this.jaceDocument.get_VersionStatus();
/* 261 */         if ((jaceVersionStatus == null) || (VersionStatus.RESERVATION.equals(jaceVersionStatus)))
/* 262 */           isEligibleSoFar = false;
/*     */       }
/*     */       EngineObject recordInfoValue;
/* 265 */       if (isEligibleSoFar)
/*     */       {
/*     */ 
/* 268 */         if (this.jaceDocument.getProperties().isPropertyPresent("RecordInformation"))
/*     */         {
/* 270 */           recordInfoValue = this.jaceDocument.getProperties().getEngineObjectValue("RecordInformation");
/* 271 */           if (recordInfoValue != null) {
/* 272 */             isEligibleSoFar = false;
/*     */           }
/*     */         }
/*     */         else {
/* 276 */           isEligibleSoFar = false;
/*     */         }
/*     */       }
/*     */       
/* 280 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(isEligibleSoFar) });
/* 281 */       return isEligibleSoFar;
/*     */     }
/*     */     finally
/*     */     {
/* 285 */       if (establishedSubject) {
/* 286 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 295 */     Tracer.traceMethodEntry(new Object[0]);
/* 296 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceDocument);
/* 297 */     Tracer.traceMethodExit(new Object[] { result });
/* 298 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 306 */     Tracer.traceMethodEntry(new Object[0]);
/* 307 */     String result = P8CE_Util.getJaceObjectClassName(this.jaceDocument);
/* 308 */     Tracer.traceMethodExit(new Object[] { result });
/* 309 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/* 317 */     return this.jaceDocument;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 325 */     Tracer.traceMethodEntry(new Object[0]);
/* 326 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 329 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 331 */       long startTime = System.currentTimeMillis();
/* 332 */       this.jaceDocument.delete();
/* 333 */       this.jaceDocument.save(RefreshMode.NO_REFRESH);
/* 334 */       long stopTime = System.currentTimeMillis();
/* 335 */       Tracer.traceExtCall("Document.delete.save", startTime, stopTime, Integer.valueOf(1), null, new Object[] { RefreshMode.NO_REFRESH });
/*     */       
/* 337 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 341 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 345 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.ContentItem });
/*     */     }
/*     */     finally
/*     */     {
/* 349 */       if (establishedSubject) {
/* 350 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void deleteAllVersions()
/*     */   {
/* 359 */     Tracer.traceMethodEntry(new Object[0]);
/* 360 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 363 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 365 */       if (!this.jaceDocument.getProperties().isPropertyPresent("VersionSeries"))
/*     */       {
/* 367 */         this.jaceDocument.refresh(new String[] { "VersionSeries" });
/*     */       }
/* 369 */       VersionSeries jaceVersionSeries = this.jaceDocument.get_VersionSeries();
/* 370 */       if (jaceVersionSeries != null)
/*     */       {
/* 372 */         long startTime = System.currentTimeMillis();
/* 373 */         jaceVersionSeries.delete();
/* 374 */         jaceVersionSeries.save(RefreshMode.NO_REFRESH);
/* 375 */         long stopTime = System.currentTimeMillis();
/* 376 */         Tracer.traceExtCall("VersionSeries.delete.save", startTime, stopTime, Integer.valueOf(1), null, new Object[] { RefreshMode.NO_REFRESH });
/*     */       }
/*     */       
/* 379 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 383 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 387 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.ContentItem });
/*     */     }
/*     */     finally
/*     */     {
/* 391 */       if (establishedSubject) {
/* 392 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 401 */     Tracer.traceMethodEntry(new Object[0]);
/* 402 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 405 */       establishedSubject = P8CE_Util.associateSubject();
/* 406 */       boolean excludeDeleted = false;
/* 407 */       List<Container> result = P8CE_Util.getContainedBy(getRepository(), this.jaceDocument, excludeDeleted);
/*     */       
/* 409 */       Tracer.traceMethodExit(new Object[] { result });
/* 410 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 414 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 418 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_PARENT_CONTAINER_FAILED, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 422 */       if (establishedSubject) {
/* 423 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 432 */     Tracer.traceMethodEntry(new Object[0]);
/* 433 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/* 434 */     Tracer.traceMethodExit(new Object[] { result });
/* 435 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 443 */     Tracer.traceMethodEntry(new Object[0]);
/* 444 */     AccessPermissionList jacePerms = P8CE_Util.getJacePermissions(this.jaceDocument);
/* 445 */     List<RMPermission> result = P8CE_Util.convertToJarmPermissions(jacePerms);
/* 446 */     Tracer.traceMethodExit(new Object[] { result });
/* 447 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 455 */     Tracer.traceMethodEntry(new Object[0]);
/* 456 */     RMProperties result = null;
/* 457 */     if (this.jaceDocument != null)
/*     */     {
/* 459 */       result = new P8CE_RMPropertiesImpl(this.jaceDocument, this);
/*     */     }
/*     */     
/* 462 */     Tracer.traceMethodExit(new Object[] { result });
/* 463 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 471 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 472 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 475 */       establishedSubject = P8CE_Util.associateSubject();
/* 476 */       internalSave(jarmRefreshMode);
/* 477 */       this.isCreationPending = false;
/* 478 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 482 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 486 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.ContentItem });
/*     */     }
/*     */     finally
/*     */     {
/* 490 */       if (establishedSubject) {
/* 491 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 500 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 501 */     RefreshMode jaceRefreshMode = P8CE_Util.convertToJaceRefreshMode(jarmRefreshMode);
/*     */     
/* 503 */     long startTime = System.currentTimeMillis();
/* 504 */     this.jaceDocument.save(jaceRefreshMode);
/* 505 */     Tracer.traceExtCall("Document.save()", startTime, System.currentTimeMillis(), Integer.valueOf(1), jaceRefreshMode, new Object[0]);
/*     */     
/* 507 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPermissions(List<RMPermission> jarmPerms)
/*     */   {
/* 515 */     Tracer.traceMethodEntry(new Object[] { jarmPerms });
/* 516 */     AccessPermissionList jacePerms = P8CE_Util.convertToJacePermissions(jarmPerms);
/* 517 */     this.jaceDocument.set_Permissions(jacePerms);
/* 518 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 526 */     Tracer.traceMethodEntry(new Object[0]);
/* 527 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 530 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 532 */       long startTime = System.currentTimeMillis();
/* 533 */       this.jaceDocument.refresh();
/* 534 */       long endTime = System.currentTimeMillis();
/* 535 */       Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[0]);
/*     */       
/* 537 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 541 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 545 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 549 */       if (establishedSubject) {
/* 550 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 559 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 560 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 563 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 567 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 568 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 570 */       long startTime = System.currentTimeMillis();
/* 571 */       this.jaceDocument.refresh(jacePF);
/* 572 */       long endTime = System.currentTimeMillis();
/* 573 */       Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 575 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 579 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 583 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 587 */       if (establishedSubject) {
/* 588 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 597 */     Tracer.traceMethodEntry((Object[])symbolicPropertyNames);
/* 598 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 601 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 604 */       RMPropertyFilter jarmFilter = new RMPropertyFilter();
/* 605 */       String spaceSepList = P8CE_Util.createSpaceSeparatedString(symbolicPropertyNames);
/* 606 */       jarmFilter.addIncludeProperty(null, null, null, spaceSepList, null);
/* 607 */       List<FilterElement> mandatoryRecordFEs = getMandatoryFEs();
/* 608 */       PropertyFilter jacePF = P8CE_Util.convertToJacePF(jarmFilter, mandatoryRecordFEs);
/*     */       
/* 610 */       long startTime = System.currentTimeMillis();
/* 611 */       this.jaceDocument.refresh(jacePF);
/* 612 */       long endTime = System.currentTimeMillis();
/* 613 */       Tracer.traceExtCall("Document.refresh", startTime, endTime, null, null, new Object[] { jacePF });
/*     */       
/* 615 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 619 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 623 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 627 */       if (establishedSubject) {
/* 628 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 637 */     return this.jaceDocument.getAccessAllowed();
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
/*     */   public void exportAsXML(String exportFileSystemPath, ContentXMLExport contentExportOption)
/*     */   {
/* 651 */     Tracer.traceMethodEntry(new Object[] { exportFileSystemPath, contentExportOption });
/* 652 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 655 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/* 658 */       File exportFileSystemDir = null;
/* 659 */       if ((exportFileSystemPath == null) || (exportFileSystemPath.trim().length() == 0))
/*     */       {
/* 661 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_FILESYSTEM_DESTINATION_PATH_INVALID, new Object[] { getObjectIdentity() });
/*     */       }
/* 663 */       boolean dirExists = false;
/*     */       
/*     */       try
/*     */       {
/* 667 */         File tmpDir = new File(exportFileSystemPath);
/* 668 */         String canonicalPath = tmpDir.getCanonicalPath();
/* 669 */         String pathToUse = canonicalPath.replaceAll("\\.{2,}", "");
/*     */         
/* 671 */         exportFileSystemDir = new File(pathToUse);
/* 672 */         dirExists = exportFileSystemDir.exists();
/* 673 */         if (!dirExists) {
/* 674 */           dirExists = exportFileSystemDir.mkdirs();
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/* 678 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.API_EXPORT_CANT_CREATE_DESTINATION_DIR, new Object[] { exportFileSystemPath });
/*     */       }
/* 680 */       if (!dirExists) {
/* 681 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_CANT_CREATE_DESTINATION_DIR, new Object[] { exportFileSystemPath });
/*     */       }
/* 683 */       exportAsXML(exportFileSystemDir, contentExportOption);
/*     */       
/* 685 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 689 */       if (establishedSubject) {
/* 690 */         P8CE_Util.disassociateSubject();
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
/*     */   void exportAsXML(File exportFileSystemDir, ContentXMLExport contentExportOption)
/*     */   {
/* 706 */     Tracer.traceMethodEntry(new Object[] { exportFileSystemDir, contentExportOption });
/*     */     
/*     */     try
/*     */     {
/* 710 */       String metadataXMLFileName = P8CE_XMLExportSupport.generateExportFileName("RM_", getName(), "_CEDOCs_");
/*     */       
/* 712 */       org.w3c.dom.Document contentMetadataDOMDoc = null;
/* 713 */       if (contentExportOption == ContentXMLExport.EmbedInDocumentXML)
/*     */       {
/*     */ 
/*     */ 
/* 717 */         int exportFlags = 128;
/* 718 */         contentMetadataDOMDoc = P8CE_XMLExportSupport.doJaceXMLExport(this.repository, this.jaceDocument, "", exportFlags);
/*     */         
/* 720 */         File metadataXMLFile = new File(exportFileSystemDir, metadataXMLFileName);
/* 721 */         RAL_XMLSupport.writeDomDocToFile(contentMetadataDOMDoc, metadataXMLFile);
/*     */ 
/*     */       }
/* 724 */       else if (contentExportOption == ContentXMLExport.AsSeparateFile)
/*     */       {
/*     */ 
/*     */ 
/* 728 */         int exportFlags = 32;
/* 729 */         contentMetadataDOMDoc = P8CE_XMLExportSupport.doJaceXMLExport(this.repository, this.jaceDocument, "", exportFlags);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 734 */         ContentElementList contentElements = this.jaceDocument.get_ContentElements();
/* 735 */         for (int i = 0; i < contentElements.size(); i++)
/*     */         {
/* 737 */           ContentElement contentElement = (ContentElement)contentElements.get(i);
/* 738 */           if ((contentElement instanceof ContentTransfer))
/*     */           {
/* 740 */             ContentTransfer ct = (ContentTransfer)contentElement;
/* 741 */             String ctRetrievalName = ct.get_RetrievalName();
/*     */             
/*     */ 
/* 744 */             File actualFile = saveContentTransferAsFile(ct, exportFileSystemDir, ctRetrievalName);
/* 745 */             String contentFileURIStr = actualFile.toURI().toString();
/*     */             
/*     */ 
/*     */ 
/* 749 */             String xPathStr = "//ContentTransfer/RetrievalName[contains(., '" + ctRetrievalName + "')]/following-sibling::ExternalRef";
/*     */             
/* 751 */             Node externRefNode = RAL_XMLSupport.findXMLNode(contentMetadataDOMDoc, xPathStr, false);
/* 752 */             if (externRefNode != null)
/*     */             {
/* 754 */               externRefNode.setTextContent(contentFileURIStr);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 760 */         File metadataXMLFile = new File(exportFileSystemDir, metadataXMLFileName);
/* 761 */         RAL_XMLSupport.writeDomDocToFile(contentMetadataDOMDoc, metadataXMLFile);
/*     */       }
/*     */       
/*     */ 
/* 765 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 769 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 773 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
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
/*     */   private File saveContentTransferAsFile(ContentTransfer ct, File exportFileSystemDir, String proposedFileName)
/*     */   {
/* 790 */     Tracer.traceMethodEntry(new Object[] { ct, exportFileSystemDir, proposedFileName });
/*     */     
/*     */ 
/* 793 */     File contentSubDir = null;
/* 794 */     String parentDirPath = exportFileSystemDir.getPath();
/* 795 */     boolean dirExists = false;
/*     */     try
/*     */     {
/* 798 */       String contentSubDirPath = parentDirPath + File.separator + "content";
/* 799 */       contentSubDir = new File(contentSubDirPath);
/* 800 */       dirExists = contentSubDir.exists();
/* 801 */       if (!dirExists) {
/* 802 */         dirExists = contentSubDir.mkdirs();
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 806 */       throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.API_EXPORT_CANT_CREATE_CONTENT_DIR, new Object[] { parentDirPath });
/*     */     }
/* 808 */     if (!dirExists) {
/* 809 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_EXPORT_CANT_CREATE_CONTENT_DIR, new Object[] { parentDirPath });
/*     */     }
/*     */     
/*     */ 
/* 813 */     String fileNameBase = proposedFileName;
/* 814 */     String fileNameExt = "";
/* 815 */     int lastDotPos = proposedFileName.lastIndexOf('.');
/* 816 */     if (lastDotPos != -1)
/*     */     {
/* 818 */       fileNameBase = proposedFileName.substring(0, lastDotPos);
/* 819 */       fileNameExt = proposedFileName.substring(lastDotPos);
/*     */     }
/*     */     
/* 822 */     String workingFileName = proposedFileName;
/* 823 */     int n = 0;
/* 824 */     File contentFile = new File(contentSubDir, workingFileName);
/* 825 */     while (contentFile.exists())
/*     */     {
/* 827 */       n++;
/* 828 */       workingFileName = fileNameBase + '[' + n + ']' + fileNameExt;
/* 829 */       contentFile = new File(contentSubDir, workingFileName);
/*     */     }
/*     */     
/*     */ 
/* 833 */     InputStream is = null;
/* 834 */     FileOutputStream fos = null;
/*     */     try
/*     */     {
/* 837 */       is = ct.accessContentStream();
/* 838 */       fos = new FileOutputStream(contentFile);
/* 839 */       byte[] buf = new byte[32768];
/* 840 */       int bytesRead = -1;
/* 841 */       while ((bytesRead = is.read(buf)) != -1)
/*     */       {
/* 843 */         fos.write(buf, 0, bytesRead);
/*     */       }
/* 845 */       fos.flush();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 853 */       if (fos != null)
/* 854 */         try { fos.close();
/*     */         } catch (Exception ignored) {}
/* 856 */       if (is != null) {
/* 857 */         try { is.close();
/*     */         }
/*     */         catch (Exception ignored) {}
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 849 */       throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.API_EXPORT_CANT_CREATE_CONTENT_FILE, new Object[] { contentFile.getPath() });
/*     */     }
/*     */     finally
/*     */     {
/* 853 */       if (fos != null)
/* 854 */         try { fos.close();
/*     */         } catch (Exception ignored) {}
/* 856 */       if (is != null)
/* 857 */         try { is.close();
/*     */         } catch (Exception ignored) {}
/*     */     }
/* 860 */     tmp421_418[0] = contentFile;Tracer.traceMethodExit(tmp421_418);
/* 861 */     return contentFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 869 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 878 */     String ident = "<unknown>";
/* 879 */     if (this.jaceDocument.getProperties().isPropertyPresent("DocumentTitle")) {
/* 880 */       ident = this.jaceDocument.getProperties().getStringValue("DocumentTitle");
/*     */     } else {
/* 882 */       ident = this.jaceDocument.getObjectReference().getObjectIdentity();
/*     */     }
/* 884 */     return "P8CE_ContentItemImpl: '" + ident + "'";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<ContentItem>
/*     */   {
/*     */     public ContentItem create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 898 */       P8CE_ContentItemImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 899 */       com.filenet.api.core.Document jaceDocument = (com.filenet.api.core.Document)jaceBaseObject;
/*     */       
/* 901 */       EntityType entityType = EntityType.ContentItem;
/* 902 */       if ("WorkflowDefinition".equalsIgnoreCase(jaceDocument.getClassName()))
/*     */       {
/* 904 */         entityType = EntityType.WorkflowDefinition;
/*     */       }
/* 906 */       else if ((jaceBaseObject instanceof EngineObject))
/*     */       {
/* 908 */         Properties jaceProps = ((EngineObject)jaceBaseObject).getProperties();
/* 909 */         if (jaceProps.isPropertyPresent("RMEntityType"))
/*     */         {
/* 911 */           Integer rawEntityType = jaceProps.getInteger32Value("RMEntityType");
/* 912 */           entityType = EntityType.getInstanceFromInt(rawEntityType.intValue());
/*     */         }
/*     */       }
/*     */       
/* 916 */       Repository contentRepos = repository;
/* 917 */       if (contentRepos == null)
/*     */       {
/* 919 */         ObjectStore jaceObjStore = jaceDocument.getObjectStore();
/* 920 */         contentRepos = P8CE_Convert.fromP8CE(jaceObjStore);
/*     */       }
/* 922 */       String identity = P8CE_Util.getJaceObjectIdentity(jaceDocument);
/* 923 */       ContentItem result = null;
/* 924 */       if (entityType == EntityType.WorkflowDefinition) {
/* 925 */         result = new P8CE_WorkflowDefinitionImpl(contentRepos, identity, jaceDocument, false);
/*     */       } else {
/* 927 */         result = new P8CE_ContentItemImpl(entityType, contentRepos, identity, jaceDocument, false);
/*     */       }
/* 929 */       P8CE_ContentItemImpl.Tracer.traceMethodExit(new Object[] { result });
/* 930 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_ContentItemImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */