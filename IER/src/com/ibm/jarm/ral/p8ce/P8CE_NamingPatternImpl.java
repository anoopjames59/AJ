/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;
/*     */ import com.ibm.jarm.api.constants.ApplyToNameOrID;
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
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_NamingPatternImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements NamingPattern
/*     */ {
/*  32 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  33 */   private static final IGenerator<NamingPattern> NamingPatternGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "PatternName", "RMEntityDescription", "ApplyToNameOrId", "Level" };
/*     */   
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   static
/*     */   {
/*  50 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  52 */     List<FilterElement> tempList = new ArrayList(1);
/*  53 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  54 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  59 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  64 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<NamingPattern> getGenerator()
/*     */   {
/*  74 */     return NamingPatternGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  82 */     return P8CE_NamingPatternLevelImpl.getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */   P8CE_NamingPatternImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/*  87 */     super(EntityType.Pattern, repository, identity, jaceCustomObject, isPlaceholder);
/*  88 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  89 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplyToNameOrID getApplyToNameOrId()
/*     */   {
/*  97 */     Tracer.traceMethodEntry(new Object[0]);
/*  98 */     String rawResult = P8CE_Util.getJacePropertyAsString(this, "ApplyToNameOrId");
/*  99 */     ApplyToNameOrID result = ApplyToNameOrID.valueOf(rawResult);
/* 100 */     Tracer.traceMethodExit(new Object[] { result });
/* 101 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setApplyToNameOrId(ApplyToNameOrID value)
/*     */   {
/* 109 */     Tracer.traceMethodEntry(new Object[] { value });
/* 110 */     Util.ckNullObjParam("value", value);
/* 111 */     getProperties().putStringValue("ApplyToNameOrId", value.toString());
/* 112 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 120 */     Tracer.traceMethodEntry(new Object[0]);
/* 121 */     String result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/* 122 */     Tracer.traceMethodExit(new Object[] { result });
/* 123 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String value)
/*     */   {
/* 131 */     Tracer.traceMethodEntry(new Object[] { value });
/* 132 */     getProperties().putStringValue("RMEntityDescription", value);
/* 133 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingPatternLevel createNamingPatternLevel()
/*     */   {
/* 141 */     Tracer.traceMethodEntry(new Object[0]);
/* 142 */     NamingPatternLevel result = createNamingPatternLevel(null);
/* 143 */     Tracer.traceMethodExit(new Object[] { result });
/* 144 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingPatternLevel createNamingPatternLevel(String idStr)
/*     */   {
/* 152 */     Tracer.traceMethodEntry(new Object[0]);
/* 153 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 156 */       establishedSubject = P8CE_Util.associateSubject();
/* 157 */       NamingPatternLevel result = P8CE_NamingPatternLevelImpl.createNew(getRepository(), this, idStr);
/* 158 */       Tracer.traceMethodExit(new Object[] { result });
/* 159 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 163 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 167 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 171 */       if (establishedSubject) {
/* 172 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingPatternLevel getNamingPatternLevel(Integer patternLevelNumber, AppliedForCategoryOrFolder appliedFor)
/*     */   {
/* 182 */     Tracer.traceMethodEntry(new Object[] { patternLevelNumber, appliedFor });
/* 183 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 186 */       establishedSubject = P8CE_Util.associateSubject();
/* 187 */       NamingPatternLevel result = null;
/*     */       
/*     */ 
/*     */ 
/* 191 */       List<FilterElement> mandatoryFEs = getMandatoryJaceFEs();
/* 192 */       IGenerator<NamingPatternLevel> namingPatternLevelGenerator = P8CE_NamingPatternLevelImpl.getGenerator();
/* 193 */       List<NamingPatternLevel> patternLevels = fetchMVObjPropValueAsList(mandatoryFEs, "Level", namingPatternLevelGenerator);
/*     */       
/*     */       String rawAppliedFor;
/* 196 */       if ((patternLevels != null) && (!patternLevels.isEmpty()))
/*     */       {
/* 198 */         rawAppliedFor = appliedFor.toString();
/* 199 */         Iterator<NamingPatternLevel> patternLevelIter = patternLevels.iterator();
/* 200 */         while (patternLevelIter.hasNext())
/*     */         {
/* 202 */           NamingPatternLevel curLevel = (NamingPatternLevel)patternLevelIter.next();
/* 203 */           if ((patternLevelNumber.equals(curLevel.getPatternLevelNumber())) && (curLevel.getAppliedFor().toString().equalsIgnoreCase(rawAppliedFor)))
/*     */           {
/* 205 */             result = curLevel;
/* 206 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 211 */       Tracer.traceMethodExit(new Object[] { result });
/* 212 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 216 */       if (establishedSubject) {
/* 217 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<NamingPatternLevel> getNamingPatternLevels() {
/* 223 */     Tracer.traceMethodEntry(new Object[0]);
/* 224 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 227 */       establishedSubject = P8CE_Util.associateSubject();
/* 228 */       List<NamingPatternLevel> result = null;
/*     */       
/*     */ 
/*     */ 
/* 232 */       List<FilterElement> mandatoryFEs = getMandatoryJaceFEs();
/*     */       
/* 234 */       IGenerator<NamingPatternLevel> namingPatternLevelGenerator = P8CE_NamingPatternLevelImpl.getGenerator();
/* 235 */       result = fetchMVObjPropValueAsList(mandatoryFEs, "Level", namingPatternLevelGenerator);
/*     */       
/* 237 */       Tracer.traceMethodExit(new Object[] { result });
/* 238 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 242 */       if (establishedSubject) {
/* 243 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/* 252 */     Tracer.traceMethodEntry(new Object[0]);
/* 253 */     String result = P8CE_Util.getJacePropertyAsString(this, "PatternName");
/* 254 */     Tracer.traceMethodExit(new Object[] { result });
/* 255 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPatternName(String patternName)
/*     */   {
/* 263 */     Tracer.traceMethodEntry(new Object[] { patternName });
/* 264 */     Util.ckInvalidStrParam("patternName", patternName);
/* 265 */     getProperties().putStringValue("PatternName", patternName);
/* 266 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 275 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 276 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 279 */       validatePattern();
/* 280 */       establishedSubject = P8CE_Util.associateSubject();
/* 281 */       super.save(jarmRefreshMode);
/* 282 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 286 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 290 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.Pattern });
/*     */     }
/*     */     finally
/*     */     {
/* 294 */       if (establishedSubject) {
/* 295 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 306 */     return super.toString("P8CE_NamingPatternImpl", null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<NamingPattern>
/*     */   {
/*     */     public NamingPattern create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 320 */       P8CE_NamingPatternImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 321 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 323 */       String identity = "<undefined>";
/* 324 */       if (jaceCustomObj.getProperties().isPropertyPresent("PatternName"))
/* 325 */         identity = jaceCustomObj.getProperties().getStringValue("PatternName");
/* 326 */       NamingPattern result = new P8CE_NamingPatternImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 328 */       P8CE_NamingPatternImpl.Tracer.traceMethodExit(new Object[] { result });
/* 329 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_NamingPatternImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */