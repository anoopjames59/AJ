/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.constants.RetainMetadata;
/*     */ import com.ibm.jarm.api.core.FilePlan;
/*     */ import com.ibm.jarm.api.core.NamingPattern;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_FilePlanImpl
/*     */   extends P8CE_BaseContainerImpl
/*     */   implements FilePlan
/*     */ {
/*  31 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*  33 */   private static final IGenerator<FilePlan> FilePlanGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private static final String[] MandatoryPropertyNames = { "AGGREGATION", "AllowedRMContainees", "AllowedRMTypes", "ContainerType", "ClassificationSchemeName", "FolderName", "Id", "LastPatternIndex", "Name", "Pattern", "PathName", "RetainMetadata", "RMEntityDescription", "RMEntityType" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  55 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  56 */     List<FilterElement> tempList = new ArrayList(1);
/*  57 */     tempList.add(new FilterElement(Integer.valueOf(0), null, Boolean.FALSE, mandatoryNames, null));
/*  58 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  63 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  68 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<FilePlan> getGenerator()
/*     */   {
/*  78 */     return FilePlanGenerator;
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
/*     */   P8CE_FilePlanImpl(Repository repository, String identity, Folder jaceFolder, boolean isPlaceholder)
/*     */   {
/*  94 */     super(EntityType.FilePlan, repository, identity, jaceFolder, isPlaceholder);
/*  95 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceFolder });
/*  96 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 104 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getLastPatternIndex()
/*     */   {
/* 112 */     Tracer.traceMethodEntry(new Object[0]);
/* 113 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "LastPatternIndex");
/* 114 */     Tracer.traceMethodEntry(new Object[] { result });
/* 115 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLastPatternIndex(Integer value)
/*     */   {
/* 123 */     Tracer.traceMethodEntry(new Object[] { value });
/* 124 */     getProperties().putIntegerValue("LastPatternIndex", value);
/* 125 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingPattern getNamingPattern()
/*     */   {
/* 133 */     Tracer.traceMethodEntry(new Object[0]);
/* 134 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 137 */       establishedSubject = P8CE_Util.associateSubject();
/* 138 */       NamingPattern result = null;
/*     */       
/* 140 */       EngineObject jaceEngObj = P8CE_Util.getJacePropertyAsEngineObject(this, "Pattern");
/* 141 */       PropertyFilter jacePF; if ((jaceEngObj != null) && ((jaceEngObj instanceof CustomObject)))
/*     */       {
/*     */ 
/*     */ 
/* 145 */         jacePF = new PropertyFilter();
/* 146 */         List<FilterElement> mandatoryFEs = P8CE_NamingPatternImpl.getMandatoryJaceFEs();
/* 147 */         for (FilterElement fe : mandatoryFEs)
/*     */         {
/* 149 */           jacePF.addIncludeProperty(fe);
/*     */         }
/*     */         
/* 152 */         P8CE_Util.fetchAdditionalJaceProperties((CustomObject)jaceEngObj, jacePF);
/*     */         
/* 154 */         IGenerator<NamingPattern> namingPatternGenerator = P8CE_NamingPatternImpl.getGenerator();
/* 155 */         result = (NamingPattern)namingPatternGenerator.create(getRepository(), jaceEngObj);
/*     */       }
/*     */       
/* 158 */       Tracer.traceMethodExit(new Object[] { result });
/* 159 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 163 */       if (establishedSubject) {
/* 164 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setNamingPattern(NamingPattern namingPattern)
/*     */   {
/* 173 */     Tracer.traceMethodEntry(new Object[] { namingPattern });
/* 174 */     getProperties().putObjectValue("Pattern", namingPattern);
/* 175 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RetainMetadata getRetainMetadata()
/*     */   {
/* 183 */     Tracer.traceMethodEntry(new Object[0]);
/* 184 */     RetainMetadata result = RetainMetadata.NeverRetain;
/*     */     
/* 186 */     Integer rawValue = P8CE_Util.getJacePropertyAsInteger(this, "RetainMetadata");
/* 187 */     if (rawValue != null)
/*     */     {
/* 189 */       result = RetainMetadata.getInstanceFromInt(rawValue.intValue());
/*     */     }
/*     */     
/* 192 */     Tracer.traceMethodExit(new Object[] { result });
/* 193 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 201 */     Tracer.traceMethodEntry(new Object[0]);
/* 202 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 205 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 207 */       this.jaceFolder.delete();
/*     */       
/* 209 */       long startTime = System.currentTimeMillis();
/* 210 */       this.jaceFolder.save(RefreshMode.NO_REFRESH);
/* 211 */       long endTime = System.currentTimeMillis();
/* 212 */       Tracer.traceExtCall("Folder.save", startTime, endTime, null, null, new Object[] { RefreshMode.NO_REFRESH });
/*     */       
/* 214 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 218 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 222 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_DELETE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.FilePlan });
/*     */     }
/*     */     finally
/*     */     {
/* 226 */       if (establishedSubject) {
/* 227 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 236 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 237 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 240 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 246 */       synchronizeBaseFolderName("ClassificationSchemeName", getProperties());
/*     */       
/* 248 */       RefreshMode jaceRefreshMode = P8CE_Util.convertToJaceRefreshMode(jarmRefreshMode);
/*     */       
/* 250 */       long startTime = System.currentTimeMillis();
/* 251 */       this.jaceFolder.save(jaceRefreshMode);
/* 252 */       long endTime = System.currentTimeMillis();
/* 253 */       Tracer.traceExtCall("Folder.save()", startTime, endTime, Integer.valueOf(1), jaceRefreshMode, new Object[0]);
/*     */       
/* 255 */       this.isCreationPending = false;
/*     */       
/* 257 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 261 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 265 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.FilePlan });
/*     */     }
/*     */     finally
/*     */     {
/* 269 */       if (establishedSubject) {
/* 270 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canContainDefensibleDisposalContainer()
/*     */   {
/* 280 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 289 */     return super.toString("P8CE_FilePlanImpl");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<FilePlan>
/*     */   {
/*     */     public FilePlan create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 303 */       P8CE_FilePlanImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 304 */       Folder jaceFolder = (Folder)jaceBaseObject;
/*     */       
/* 306 */       String identity = jaceFolder.get_Id().toString();
/* 307 */       FilePlan result = new P8CE_FilePlanImpl(repository, identity, jaceFolder, false);
/*     */       
/* 309 */       P8CE_FilePlanImpl.Tracer.traceMethodExit(new Object[] { result });
/* 310 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_FilePlanImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */