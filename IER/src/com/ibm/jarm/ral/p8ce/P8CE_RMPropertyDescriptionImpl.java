/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.admin.ChoiceList;
/*     */ import com.filenet.api.constants.Cardinality;
/*     */ import com.filenet.api.constants.PropertySettability;
/*     */ import com.filenet.api.constants.TypeID;
/*     */ import com.filenet.api.meta.PropertyDescription;
/*     */ import com.filenet.api.meta.PropertyDescriptionBinary;
/*     */ import com.filenet.api.meta.PropertyDescriptionBoolean;
/*     */ import com.filenet.api.meta.PropertyDescriptionDateTime;
/*     */ import com.filenet.api.meta.PropertyDescriptionFloat64;
/*     */ import com.filenet.api.meta.PropertyDescriptionId;
/*     */ import com.filenet.api.meta.PropertyDescriptionInteger32;
/*     */ import com.filenet.api.meta.PropertyDescriptionObject;
/*     */ import com.filenet.api.meta.PropertyDescriptionString;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import com.ibm.jarm.api.constants.RMPropertySettability;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMChoiceList;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ class P8CE_RMPropertyDescriptionImpl
/*     */   implements RMPropertyDescription
/*     */ {
/*  59 */   protected static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String DECLARE_TAG = "declare";
/*     */   
/*     */ 
/*     */   private static final String DOD_CLASSIFY_TAG = "RMSecClassify";
/*     */   
/*     */ 
/*     */   private static final String RMSYSTEM_TAG = "RMSystem";
/*     */   
/*     */ 
/*  72 */   private static final Set<String> SpecialDeclarePropNames = new HashSet();
/*  73 */   static { SpecialDeclarePropNames.add("DocumentTitle");
/*     */     
/*  75 */     SpecialDeclarePropNames.add("DerivedFrom");
/*  76 */     SpecialDeclarePropNames.add("InitialClassification");
/*  77 */     SpecialDeclarePropNames.add("CurrentClassification");
/*  78 */     SpecialDeclarePropNames.add("ReasonsForClassification");
/*  79 */     SpecialDeclarePropNames.add("ClassifyingAgency");
/*  80 */     SpecialDeclarePropNames.add("ClassifiedBy");
/*  81 */     SpecialDeclarePropNames.add("DeclassifyOnDate");
/*  82 */     SpecialDeclarePropNames.add("DeclassifyOnEvents");
/*  83 */     SpecialDeclarePropNames.add("Exemptions");
/*  84 */     SpecialDeclarePropNames.add("DowngradeOnDate");
/*  85 */     SpecialDeclarePropNames.add("DowngradeOnEvents");
/*  86 */     SpecialDeclarePropNames.add("DowngradeInstructions");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyDescription jacePropDesc;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Repository repository;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static RMPropertyDescription create(Repository repository, PropertyDescription jacePropDesc)
/*     */   {
/* 105 */     Tracer.traceMethodEntry(new Object[] { repository, jacePropDesc });
/* 106 */     RMPropertyDescription result = null;
/*     */     
/* 108 */     if ((jacePropDesc instanceof PropertyDescriptionString)) {
/* 109 */       result = new P8CE_RMPropertyDescriptionStringImpl(jacePropDesc);
/* 110 */     } else if ((jacePropDesc instanceof PropertyDescriptionBoolean)) {
/* 111 */       result = new P8CE_RMPropertyDescriptionBooleanImpl(jacePropDesc);
/* 112 */     } else if ((jacePropDesc instanceof PropertyDescriptionDateTime)) {
/* 113 */       result = new P8CE_RMPropertyDescriptionDateTimeImpl(jacePropDesc);
/* 114 */     } else if ((jacePropDesc instanceof PropertyDescriptionInteger32)) {
/* 115 */       result = new P8CE_RMPropertyDescriptionIntegerImpl(jacePropDesc);
/* 116 */     } else if ((jacePropDesc instanceof PropertyDescriptionId)) {
/* 117 */       result = new P8CE_RMPropertyDescriptionGuidImpl(jacePropDesc);
/* 118 */     } else if ((jacePropDesc instanceof PropertyDescriptionObject)) {
/* 119 */       result = new P8CE_RMPropertyDescriptionObjectImpl(jacePropDesc);
/* 120 */     } else if ((jacePropDesc instanceof PropertyDescriptionFloat64)) {
/* 121 */       result = new P8CE_RMPropertyDescriptionDoubleImpl(jacePropDesc);
/* 122 */     } else if ((jacePropDesc instanceof PropertyDescriptionBinary)) {
/* 123 */       result = new P8CE_RMPropertyDescriptionBinaryImpl(jacePropDesc);
/*     */     }
/* 125 */     if (result != null)
/*     */     {
/* 127 */       ((P8CE_RMPropertyDescriptionImpl)result).repository = repository;
/*     */     }
/*     */     
/* 130 */     Tracer.traceMethodExit(new Object[] { result });
/* 131 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_RMPropertyDescriptionImpl(PropertyDescription jacePropDesc)
/*     */   {
/* 142 */     Tracer.traceMethodEntry(new Object[] { jacePropDesc });
/* 143 */     this.jacePropDesc = jacePropDesc;
/* 144 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public RMCardinality getCardinality()
/*     */   {
/* 157 */     Tracer.traceMethodEntry(new Object[0]);
/* 158 */     RMCardinality result = null;
/* 159 */     Cardinality jaceCardinality = this.jacePropDesc.get_Cardinality();
/* 160 */     if (jaceCardinality != null)
/*     */     {
/* 162 */       int intValue = jaceCardinality.getValue();
/* 163 */       result = RMCardinality.getInstanceFromInt(intValue);
/*     */     }
/*     */     
/* 166 */     Tracer.traceMethodExit(new Object[] { result });
/* 167 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMChoiceList getChoiceList()
/*     */   {
/* 175 */     Tracer.traceMethodEntry(new Object[0]);
/* 176 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 179 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 181 */       RMChoiceList result = null;
/* 182 */       ChoiceList jaceChoiceList = this.jacePropDesc.get_ChoiceList();
/* 183 */       if (jaceChoiceList != null)
/*     */       {
/* 185 */         result = new P8CE_RMChoiceListImpl((Repository)null, jaceChoiceList);
/*     */       }
/*     */       
/* 188 */       Tracer.traceMethodExit(new Object[] { result });
/* 189 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 193 */       if (establishedSubject) {
/* 194 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DataType getDataType()
/*     */   {
/* 203 */     Tracer.traceMethodEntry(new Object[0]);
/* 204 */     DataType result = null;
/*     */     
/* 206 */     TypeID jaceTypeID = this.jacePropDesc.get_DataType();
/* 207 */     if (jaceTypeID != null)
/*     */     {
/* 209 */       int intValue = jaceTypeID.getValue();
/* 210 */       result = DataType.getInstanceFromInt(intValue);
/*     */     }
/*     */     
/* 213 */     Tracer.traceMethodEntry(new Object[] { result });
/* 214 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescriptiveText()
/*     */   {
/* 222 */     return this.jacePropDesc.get_DescriptiveText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 230 */     return this.jacePropDesc.get_DisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 238 */     Tracer.traceMethodEntry(new Object[0]);
/* 239 */     String result = null;
/*     */     
/* 241 */     Id id = this.jacePropDesc.get_Id();
/* 242 */     if (id != null) {
/* 243 */       result = id.toString();
/*     */     }
/* 245 */     Tracer.traceMethodExit(new Object[] { result });
/* 246 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 254 */     return this.jacePropDesc.get_Name();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMPropertySettability getSettability()
/*     */   {
/* 262 */     Tracer.traceMethodEntry(new Object[0]);
/* 263 */     RMPropertySettability result = null;
/* 264 */     PropertySettability jaceSettability = this.jacePropDesc.get_Settability();
/* 265 */     if (jaceSettability != null)
/*     */     {
/* 267 */       int intValue = jaceSettability.getValue();
/* 268 */       result = RMPropertySettability.getInstanceFromInt(intValue);
/*     */     }
/*     */     
/* 271 */     Tracer.traceMethodExit(new Object[] { result });
/* 272 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSymbolicName()
/*     */   {
/* 280 */     return this.jacePropDesc.get_SymbolicName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHidden()
/*     */   {
/* 288 */     Tracer.traceMethodEntry(new Object[0]);
/* 289 */     Boolean boolObj = this.jacePropDesc.get_IsHidden();
/* 290 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 291 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 292 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOrderable()
/*     */   {
/* 300 */     Tracer.traceMethodEntry(new Object[0]);
/* 301 */     Boolean boolObj = this.jacePropDesc.get_IsOrderable();
/* 302 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 303 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 304 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 312 */     Tracer.traceMethodEntry(new Object[0]);
/* 313 */     Boolean boolObj = this.jacePropDesc.get_IsReadOnly();
/* 314 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 315 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 316 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSearchable()
/*     */   {
/* 324 */     Tracer.traceMethodEntry(new Object[0]);
/* 325 */     Boolean boolObj = this.jacePropDesc.get_IsSearchable();
/* 326 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 327 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 328 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSelectable()
/*     */   {
/* 336 */     Tracer.traceMethodEntry(new Object[0]);
/* 337 */     Boolean boolObj = this.jacePropDesc.get_IsSelectable();
/* 338 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 339 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 340 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSystemGenerated()
/*     */   {
/* 348 */     Tracer.traceMethodEntry(new Object[0]);
/* 349 */     Boolean boolObj = this.jacePropDesc.get_IsSystemGenerated();
/* 350 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 351 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 352 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSystemOwned()
/*     */   {
/* 360 */     Tracer.traceMethodEntry(new Object[0]);
/* 361 */     Boolean boolObj = this.jacePropDesc.get_IsSystemOwned();
/* 362 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 363 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 364 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isValueRequired()
/*     */   {
/* 372 */     Tracer.traceMethodEntry(new Object[0]);
/* 373 */     Boolean boolObj = this.jacePropDesc.get_IsValueRequired();
/* 374 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 375 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 376 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean requiresUniqueElements()
/*     */   {
/* 384 */     Tracer.traceMethodEntry(new Object[0]);
/* 385 */     Boolean boolObj = this.jacePropDesc.get_RequiresUniqueElements();
/* 386 */     boolean result = boolObj != null ? boolObj.booleanValue() : false;
/* 387 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 388 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isForDeclare()
/*     */   {
/* 396 */     Tracer.traceMethodEntry(new Object[0]);
/* 397 */     boolean result = false;
/* 398 */     String symbolicName = this.jacePropDesc.get_SymbolicName();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 403 */     if (SpecialDeclarePropNames.contains(symbolicName))
/*     */     {
/* 405 */       result = true;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 410 */       String descText = this.jacePropDesc.get_DescriptiveText();
/* 411 */       if (descText != null)
/*     */       {
/* 413 */         String[] tokens = descText.trim().split(",");
/* 414 */         for (String token : tokens)
/*     */         {
/* 416 */           if ("declare".equalsIgnoreCase(token.trim()))
/*     */           {
/* 418 */             result = true;
/* 419 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 425 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 426 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRMSystemProperty()
/*     */   {
/* 434 */     Tracer.traceMethodEntry(new Object[0]);
/* 435 */     boolean result = false;
/*     */     
/*     */ 
/* 438 */     String descText = this.jacePropDesc.get_DescriptiveText();
/* 439 */     if (descText != null)
/*     */     {
/* 441 */       result = descText.contains("RMSystem");
/*     */     }
/*     */     
/* 444 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 445 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isForClassified()
/*     */   {
/* 453 */     Tracer.traceMethodEntry(new Object[0]);
/* 454 */     boolean result = false;
/* 455 */     String symbolicName = this.jacePropDesc.get_SymbolicName();
/* 456 */     if (("RMEntityDescription".equalsIgnoreCase(symbolicName)) || ("ReceiptOf".equalsIgnoreCase(symbolicName)))
/*     */     {
/*     */ 
/*     */ 
/* 460 */       result = false;
/*     */     }
/*     */     else
/*     */     {
/* 464 */       String descText = this.jacePropDesc.get_DescriptiveText();
/* 465 */       if (descText != null)
/*     */       {
/* 467 */         result = descText.indexOf("RMSecClassify") != -1;
/*     */       }
/*     */     }
/*     */     
/* 471 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 472 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMPropertyDescriptionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */