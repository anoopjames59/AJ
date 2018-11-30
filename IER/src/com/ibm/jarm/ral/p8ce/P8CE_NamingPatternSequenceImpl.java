/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.NamingPatternSequence;
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
/*     */ class P8CE_NamingPatternSequenceImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements NamingPatternSequence
/*     */ {
/*  28 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  29 */   private static final IGenerator<NamingPatternSequence> NamingPatternSequenceGenerator = new Generator();
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
/*  40 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "ParentGUID", "LastPatternIndex" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   static
/*     */   {
/*  45 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  47 */     List<FilterElement> tempList = new ArrayList(1);
/*  48 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  49 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  54 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  59 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<NamingPatternSequence> getGenerator()
/*     */   {
/*  69 */     return NamingPatternSequenceGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  77 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */   P8CE_NamingPatternSequenceImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/*  83 */     super(EntityType.PatternSequence, repository, identity, jaceCustomObject, isPlaceholder);
/*  84 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  85 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getLastPatternIndex()
/*     */   {
/*  93 */     Tracer.traceMethodEntry(new Object[0]);
/*  94 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "LastPatternIndex");
/*  95 */     Tracer.traceMethodExit(new Object[] { result });
/*  96 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLastPatternIndex(Integer value)
/*     */   {
/* 104 */     Tracer.traceMethodEntry(new Object[] { value });
/* 105 */     Util.ckNullObjParam("value", value);
/* 106 */     getProperties().putIntegerValue("LastPatternIndex", value);
/* 107 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParentIdentifier()
/*     */   {
/* 115 */     Tracer.traceMethodEntry(new Object[0]);
/* 116 */     String result = P8CE_Util.getJacePropertyAsString(this, "ParentGUID");
/* 117 */     Tracer.traceMethodExit(new Object[] { result });
/* 118 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParentIdentifier(String value)
/*     */   {
/* 126 */     Tracer.traceMethodEntry(new Object[] { value });
/* 127 */     Util.ckNullObjParam("value", value);
/* 128 */     getProperties().putStringValue("ParentGUID", value);
/* 129 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 138 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 139 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 142 */       validatePatternSequence();
/* 143 */       establishedSubject = P8CE_Util.associateSubject();
/* 144 */       super.save(jarmRefreshMode);
/* 145 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 149 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 153 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.PatternSequence });
/*     */     }
/*     */     finally
/*     */     {
/* 157 */       if (establishedSubject) {
/* 158 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 170 */     return super.toString("P8CE_NamingPatternSequenceImpl", null);
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
/*     */     implements IGenerator<NamingPatternSequence>
/*     */   {
/*     */     public NamingPatternSequence create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 185 */       P8CE_NamingPatternSequenceImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 186 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 188 */       String identity = "<undefined>";
/* 189 */       if (jaceCustomObj.getProperties().isPropertyPresent("Id")) {
/* 190 */         identity = jaceCustomObj.getProperties().getIdValue("Id").toString();
/*     */       }
/* 192 */       NamingPatternSequence result = new P8CE_NamingPatternSequenceImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 194 */       P8CE_NamingPatternSequenceImpl.Tracer.traceMethodExit(new Object[] { result });
/* 195 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_NamingPatternSequenceImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */