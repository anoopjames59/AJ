/*     */ package com.ibm.jarm.ral.common;
/*     */ 
/*     */ import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;
/*     */ import com.ibm.jarm.api.constants.ApplyToNameOrID;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.RMCustomObject;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RALBaseCustomObject
/*     */   extends RALBaseEntity
/*     */   implements RMCustomObject
/*     */ {
/*  27 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
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
/*     */   protected RALBaseCustomObject(EntityType entityType, Repository repository, String identity, boolean isPlaceholder)
/*     */   {
/*  44 */     super(entityType, repository, identity, isPlaceholder);
/*  45 */     Tracer.traceMethodEntry(new Object[] { entityType, repository, identity, Boolean.valueOf(isPlaceholder) });
/*  46 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validateLocation()
/*     */   {
/*  55 */     Tracer.traceMethodEntry(new Object[0]);
/*  56 */     RMProperties props = getProperties();
/*  57 */     if (isCreationPending())
/*     */     {
/*     */ 
/*     */ 
/*  61 */       String proposedName = null;
/*  62 */       if (props.isPropertyPresent("LocationName"))
/*     */       {
/*  64 */         proposedName = props.getStringValue("LocationName");
/*  65 */         if (proposedName != null)
/*     */         {
/*  67 */           proposedName = proposedName.trim();
/*  68 */           if (proposedName.length() == 0)
/*  69 */             proposedName = null;
/*     */         }
/*     */       }
/*  72 */       if (proposedName == null)
/*     */       {
/*  74 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "LocationName" });
/*     */       }
/*  76 */       if (!((RALBaseRepository)this.repository).customObjNameIsUnique("Location", "LocationName", proposedName, null))
/*     */       {
/*  78 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_LOCATION_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  83 */       RMProperty prop = null;
/*     */       
/*  85 */       if ((prop = RALBaseEntity.getPropIfModified(props, "LocationName")) != null)
/*     */       {
/*  87 */         String proposedName = prop.getStringValue();
/*  88 */         if (proposedName != null)
/*     */         {
/*  90 */           proposedName = proposedName.trim();
/*  91 */           if (proposedName.length() == 0)
/*  92 */             proposedName = null;
/*     */         }
/*  94 */         if (proposedName == null)
/*     */         {
/*  96 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "LocationName" });
/*     */         }
/*     */         
/*  99 */         String ident = getObjectIdentity();
/* 100 */         if (!((RALBaseRepository)this.repository).customObjNameIsUnique("Location", "LocationName", proposedName, ident))
/*     */         {
/* 102 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_LOCATION_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*     */         }
/*     */       }
/*     */     }
/* 106 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validateHold()
/*     */   {
/* 115 */     Tracer.traceMethodEntry(new Object[0]);
/* 116 */     RMProperties props = getProperties();
/* 117 */     if (isCreationPending())
/*     */     {
/*     */ 
/*     */ 
/* 121 */       String proposedName = null;
/* 122 */       if (props.isPropertyPresent("HoldName"))
/*     */       {
/* 124 */         proposedName = props.getStringValue("HoldName");
/* 125 */         if (proposedName != null)
/*     */         {
/* 127 */           proposedName = proposedName.trim();
/* 128 */           if (proposedName.length() == 0)
/* 129 */             proposedName = null;
/*     */         }
/*     */       }
/* 132 */       if (proposedName == null)
/*     */       {
/* 134 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "HoldName" });
/*     */       }
/* 136 */       if (!((RALBaseRepository)this.repository).customObjNameIsUnique("RecordHold", "HoldName", proposedName, null))
/*     */       {
/* 138 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_HOLD_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*     */       }
/*     */       
/*     */ 
/* 142 */       int defaultSweepStateValue = 0;
/* 143 */       if (props.isPropertyPresent("ConditionXML"))
/*     */       {
/* 145 */         String conditionXMLValue = props.getStringValue("ConditionXML");
/* 146 */         if ((conditionXMLValue != null) && (conditionXMLValue.trim().length() > 0))
/*     */         {
/* 148 */           defaultSweepStateValue = 1;
/*     */         }
/*     */       }
/* 151 */       props.putIntegerValue("SweepState", Integer.valueOf(defaultSweepStateValue));
/*     */     }
/*     */     else
/*     */     {
/* 155 */       RMProperty prop = null;
/*     */       
/* 157 */       if ((prop = RALBaseEntity.getPropIfModified(props, "HoldName")) != null)
/*     */       {
/* 159 */         String proposedName = prop.getStringValue();
/* 160 */         if (proposedName != null)
/*     */         {
/* 162 */           proposedName = proposedName.trim();
/* 163 */           if (proposedName.length() == 0)
/* 164 */             proposedName = null;
/*     */         }
/* 166 */         if (proposedName == null)
/*     */         {
/* 168 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "HoldName" });
/*     */         }
/*     */         
/* 171 */         String ident = getObjectIdentity();
/* 172 */         if (!((RALBaseRepository)this.repository).customObjNameIsUnique("RecordHold", "HoldName", proposedName, ident))
/*     */         {
/* 174 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_HOLD_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 179 */       if ((prop = RALBaseEntity.getPropIfModified(props, "ConditionXML")) != null)
/*     */       {
/* 181 */         Integer sweepStateValue = Integer.valueOf(0);
/* 182 */         String conditionXMLValue = props.getStringValue("ConditionXML");
/* 183 */         if ((conditionXMLValue != null) && (conditionXMLValue.trim().length() > 0))
/*     */         {
/* 185 */           sweepStateValue = Integer.valueOf(1);
/*     */         }
/* 187 */         props.putIntegerValue("SweepState", sweepStateValue);
/*     */       }
/*     */     }
/* 190 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validatePattern()
/*     */   {
/* 199 */     Tracer.traceMethodEntry(new Object[0]);
/* 200 */     RMProperties props = getProperties();
/* 201 */     if ((props.isPropertyPresent("ApplyToNameOrId")) && (props.getStringValue("ApplyToNameOrId") != null))
/*     */     {
/* 203 */       String propVal = props.getStringValue("ApplyToNameOrId");
/* 204 */       if ((!propVal.equalsIgnoreCase(ApplyToNameOrID.Name.toString())) && (!propVal.equalsIgnoreCase(ApplyToNameOrID.Identifier.toString())))
/*     */       {
/* 206 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INVALID_APPLY_TO_VALUE, new Object[] { "ApplyToNameOrId" });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 211 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "ApplyToNameOrId" });
/*     */     }
/*     */     
/* 214 */     if (isCreationPending())
/*     */     {
/*     */ 
/*     */ 
/* 218 */       String proposedName = null;
/* 219 */       if (props.isPropertyPresent("PatternName"))
/*     */       {
/* 221 */         proposedName = props.getStringValue("PatternName");
/* 222 */         if (proposedName != null)
/*     */         {
/* 224 */           proposedName = proposedName.trim();
/* 225 */           if (proposedName.length() == 0)
/* 226 */             proposedName = null;
/*     */         }
/*     */       }
/* 229 */       if (proposedName == null)
/*     */       {
/* 231 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "PatternName" });
/*     */       }
/* 233 */       if (!((RALBaseRepository)this.repository).customObjNameIsUnique("Pattern", "PatternName", proposedName, null))
/*     */       {
/* 235 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_PATTERN_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 240 */       RMProperty prop = null;
/*     */       
/* 242 */       if ((prop = RALBaseEntity.getPropIfModified(props, "PatternName")) != null)
/*     */       {
/* 244 */         String proposedName = prop.getStringValue();
/* 245 */         if (proposedName != null)
/*     */         {
/* 247 */           proposedName = proposedName.trim();
/* 248 */           if (proposedName.length() == 0)
/* 249 */             proposedName = null;
/*     */         }
/* 251 */         if (proposedName == null)
/*     */         {
/* 253 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "PatternName" });
/*     */         }
/*     */         
/* 256 */         String ident = getObjectIdentity();
/* 257 */         if (!((RALBaseRepository)this.repository).customObjNameIsUnique("Pattern", "PatternName", proposedName, ident))
/*     */         {
/* 259 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_NEW_PATTERN_NAME_NOT_UNIQUE, new Object[] { proposedName });
/*     */         }
/*     */       }
/*     */     }
/* 263 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   protected void validatePatternSequence()
/*     */   {
/* 268 */     Tracer.traceMethodEntry(new Object[0]);
/* 269 */     RMProperties props = getProperties();
/* 270 */     if (!props.isPropertyPresent("LastPatternIndex"))
/*     */     {
/* 272 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "LastPatternIndex" });
/*     */     }
/* 274 */     if (!props.isPropertyPresent("ParentGUID"))
/*     */     {
/* 276 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "LastPatternIndex" });
/*     */     }
/* 278 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   protected void validateNamingPatternLevel()
/*     */   {
/* 283 */     Tracer.traceMethodEntry(new Object[0]);
/* 284 */     RMProperties props = getProperties();
/* 285 */     if (!props.isPropertyPresent("PatternString"))
/*     */     {
/* 287 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "PatternString" });
/*     */     }
/*     */     
/* 290 */     if (!props.isPropertyPresent("LevelNumber"))
/*     */     {
/* 292 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "LevelNumber" });
/*     */     }
/*     */     
/* 295 */     if (!props.isPropertyPresent("IncrementedBy"))
/*     */     {
/* 297 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "IncrementedBy" });
/*     */     }
/*     */     
/* 300 */     if (!props.isPropertyPresent("AppliedFor"))
/*     */     {
/* 302 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "AppliedFor" });
/*     */     }
/* 304 */     String appliedForValue = props.getStringValue("AppliedFor");
/* 305 */     if ((!AppliedForCategoryOrFolder.RecordCategory.toString().equalsIgnoreCase(appliedForValue)) && (!AppliedForCategoryOrFolder.RecordFolder.toString().equalsIgnoreCase(appliedForValue)))
/*     */     {
/*     */ 
/* 308 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INVALID_PROPERTY_VALUE, new Object[] { "AppliedFor" });
/*     */     }
/*     */     
/* 311 */     if (!props.isPropertyPresent("PatternString"))
/*     */     {
/* 313 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_MISSING_REQUIRED_PROPERTY, new Object[] { "PatternString" });
/*     */     }
/* 315 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseCustomObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */