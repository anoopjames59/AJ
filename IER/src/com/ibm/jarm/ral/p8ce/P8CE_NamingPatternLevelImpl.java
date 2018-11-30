/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.Factory.CustomObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.NamingPattern;
/*     */ import com.ibm.jarm.api.core.NamingPatternLevel;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_NamingPatternLevelImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements NamingPatternLevel
/*     */ {
/*  36 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  37 */   private static final IGenerator<NamingPatternLevel> NamingPatternLevelGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "LevelNumber", "AppliedFor", "PatternString", "IncrementedBy", "PatternPtr" };
/*     */   
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  55 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  57 */     List<FilterElement> tempList = new ArrayList(1);
/*  58 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  59 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  64 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  69 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<NamingPatternLevel> getGenerator()
/*     */   {
/*  79 */     return NamingPatternLevelGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  87 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */   static NamingPatternLevel createNew(Repository repository, P8CE_NamingPatternImpl owningNamingPattern, String idStr)
/*     */   {
/*  92 */     Tracer.traceMethodEntry(new Object[] { repository, owningNamingPattern, idStr });
/*  93 */     ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  94 */     Id newId = P8CE_Util.processIdStr(idStr);
/*  95 */     CustomObject jaceCustObj = Factory.CustomObject.createInstance(jaceObjStore, "PatternLevel", newId);
/*  96 */     jaceCustObj.getProperties().putObjectValue("PatternPtr", owningNamingPattern.jaceCustomObject);
/*     */     
/*  98 */     NamingPatternLevel result = new P8CE_NamingPatternLevelImpl(repository, "NamingPatternLevel", jaceCustObj, true);
/*  99 */     Tracer.traceMethodExit(new Object[] { result });
/* 100 */     return result;
/*     */   }
/*     */   
/*     */   P8CE_NamingPatternLevelImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/* 105 */     super(EntityType.PatternLevel, repository, identity, jaceCustomObject, isPlaceholder);
/* 106 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/* 107 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 116 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 117 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 120 */       establishedSubject = P8CE_Util.associateSubject();
/* 121 */       validateNamingPatternLevel();
/* 122 */       super.save(jarmRefreshMode);
/* 123 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 127 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 131 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.PatternLevel });
/*     */     }
/*     */     finally
/*     */     {
/* 135 */       if (establishedSubject) {
/* 136 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AppliedForCategoryOrFolder getAppliedFor()
/*     */   {
/* 146 */     Tracer.traceMethodEntry(new Object[0]);
/* 147 */     String rawResult = P8CE_Util.getJacePropertyAsString(this, "AppliedFor");
/* 148 */     AppliedForCategoryOrFolder result = AppliedForCategoryOrFolder.valueOf(rawResult);
/* 149 */     Tracer.traceMethodExit(new Object[] { result });
/* 150 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAppliedFor(AppliedForCategoryOrFolder value)
/*     */   {
/* 158 */     Tracer.traceMethodEntry(new Object[] { value });
/* 159 */     Util.ckNullObjParam("value", value);
/* 160 */     getProperties().putStringValue("AppliedFor", value.toString());
/* 161 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getIncrementedBy()
/*     */   {
/* 169 */     Tracer.traceMethodEntry(new Object[0]);
/* 170 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "IncrementedBy");
/* 171 */     Tracer.traceMethodExit(new Object[] { result });
/* 172 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIncrementedBy(Integer value)
/*     */   {
/* 180 */     Tracer.traceMethodEntry(new Object[] { value });
/* 181 */     getProperties().putIntegerValue("IncrementedBy", value);
/* 182 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingPattern getNamingPattern()
/*     */   {
/* 190 */     Tracer.traceMethodEntry(new Object[0]);
/* 191 */     NamingPattern result = null;
/*     */     
/* 193 */     EngineObject jaceEngObj = P8CE_Util.getJacePropertyAsEngineObject(this, "PatternPtr");
/* 194 */     if ((jaceEngObj != null) && ((jaceEngObj instanceof CustomObject)))
/*     */     {
/*     */ 
/*     */ 
/* 198 */       PropertyFilter jacePF = new PropertyFilter();
/* 199 */       List<FilterElement> mandatoryFEs = P8CE_NamingPatternImpl.getMandatoryJaceFEs();
/* 200 */       for (FilterElement fe : mandatoryFEs)
/*     */       {
/* 202 */         jacePF.addIncludeProperty(fe);
/*     */       }
/*     */       
/* 205 */       P8CE_Util.fetchAdditionalJaceProperties((CustomObject)jaceEngObj, jacePF);
/*     */       
/* 207 */       IGenerator<NamingPattern> namingPatternGenerator = P8CE_NamingPatternImpl.getGenerator();
/* 208 */       result = (NamingPattern)namingPatternGenerator.create(getRepository(), jaceEngObj);
/*     */     }
/*     */     
/* 211 */     Tracer.traceMethodExit(new Object[] { result });
/* 212 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getPatternLevelNumber()
/*     */   {
/* 220 */     Tracer.traceMethodEntry(new Object[0]);
/* 221 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "LevelNumber");
/* 222 */     Tracer.traceMethodExit(new Object[] { result });
/* 223 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPatternLevelNumber(Integer value)
/*     */   {
/* 231 */     Tracer.traceMethodEntry(new Object[] { value });
/* 232 */     Util.ckNullObjParam("value", value);
/* 233 */     getProperties().putIntegerValue("LevelNumber", value);
/* 234 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPatternString()
/*     */   {
/* 242 */     Tracer.traceMethodEntry(new Object[0]);
/* 243 */     String result = P8CE_Util.getJacePropertyAsString(this, "PatternString");
/* 244 */     Tracer.traceMethodExit(new Object[] { result });
/* 245 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPatternString(String value)
/*     */   {
/* 253 */     Tracer.traceMethodEntry(new Object[] { value });
/* 254 */     Util.ckInvalidStrParam("value", value);
/* 255 */     getProperties().putStringValue("PatternString", value);
/* 256 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 265 */     return super.toString("P8CE_NamingPatternLevelImpl", null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<NamingPatternLevel>
/*     */   {
/*     */     public NamingPatternLevel create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 280 */       P8CE_NamingPatternLevelImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 281 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 283 */       String identity = "<undefined>";
/* 284 */       if (jaceCustomObj.getProperties().isPropertyPresent("Id")) {
/* 285 */         identity = jaceCustomObj.getProperties().getIdValue("Id").toString();
/*     */       }
/* 287 */       NamingPatternLevel result = new P8CE_NamingPatternLevelImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 289 */       P8CE_NamingPatternLevelImpl.Tracer.traceMethodExit(new Object[] { result });
/* 290 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_NamingPatternLevelImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */