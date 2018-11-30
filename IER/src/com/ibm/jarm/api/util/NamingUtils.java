/*     */ package com.ibm.jarm.api.util;
/*     */ 
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.FilePlan;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.NamingPattern;
/*     */ import com.ibm.jarm.api.core.NamingPatternLevel;
/*     */ import com.ibm.jarm.api.core.NamingPatternSequence;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.NamingPatternSequence;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.core.RecordVolumeContainer;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.core.SystemConfiguration;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import com.ibm.jarm.api.query.RMSearch;
/*     */ import com.ibm.jarm.api.security.RMUser;
/*     */ import com.ibm.jarm.ral.p8ce.JaceBasable;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_BaseContainerImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_Util;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NamingUtils
/*     */ {
/*  45 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
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
/*     */   public static String generateNextVolumeName(RecordVolumeContainer parent)
/*     */   {
/*  62 */     Tracer.traceMethodEntry(new Object[] { parent });
/*     */     
/*  64 */     P8CE_BaseContainerImpl containerImpl = (P8CE_BaseContainerImpl)parent;
/*     */     
/*     */ 
/*  67 */     String volumeSuffixPattern = "00000";
/*  68 */     Map<String, SystemConfiguration> sysConfigs = ((FilePlanRepository)containerImpl.getRepository()).getSystemConfigurations();
/*  69 */     if (sysConfigs != null)
/*     */     {
/*  71 */       SystemConfiguration volPatternSysConfig = (SystemConfiguration)sysConfigs.get("Volume Pattern Suffix");
/*  72 */       if (volPatternSysConfig != null)
/*     */       {
/*  74 */         String temp = volPatternSysConfig.getPropertyValue();
/*  75 */         if ((temp != null) && (temp.trim().length() != 0)) {
/*  76 */           volumeSuffixPattern = temp.trim();
/*     */         }
/*     */       }
/*     */     }
/*  80 */     int nameSuffixInt = containerImpl.findLastVolumeSuffix() + 1;
/*  81 */     String nameSuffix = Integer.toString(nameSuffixInt, 10);
/*  82 */     while (nameSuffix.length() < volumeSuffixPattern.length())
/*     */     {
/*  84 */       nameSuffix = '0' + nameSuffix;
/*     */     }
/*  86 */     String parentName = containerImpl.getFolderName();
/*  87 */     String nextVolumeName = parentName + '-' + nameSuffix;
/*  88 */     Tracer.traceMethodExit(new Object[] { nextVolumeName });
/*     */     
/*  90 */     return nextVolumeName;
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
/*     */   public static String generateRecordNameFromPattern(Container parent)
/*     */   {
/* 106 */     Tracer.traceMethodEntry(new Object[] { parent });
/*     */     
/* 108 */     Integer increment = Integer.valueOf(0);
/* 109 */     String namingPattern = null;
/* 110 */     Container parentWithPattern = null;
/*     */     
/* 112 */     if (parent.getEntityType() == EntityType.RecordVolume)
/*     */     {
/* 114 */       parentWithPattern = parent.getParent();
/*     */     }
/*     */     else
/*     */     {
/* 118 */       parentWithPattern = parent;
/*     */     }
/*     */     
/* 121 */     increment = parentWithPattern.getProperties().getIntegerValue("IncrementedBy");
/* 122 */     namingPattern = parentWithPattern.getProperties().getStringValue("RecordPattern");
/*     */     String result;
/* 124 */     String result; if ((namingPattern == null) || (namingPattern.length() == 0))
/*     */     {
/* 126 */       result = null;
/*     */     }
/*     */     else
/*     */     {
/* 130 */       result = generateNameFromPattern(namingPattern, increment, parentWithPattern);
/*     */     }
/* 132 */     Tracer.traceMethodExit(new Object[] { result });
/* 133 */     return result;
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
/*     */   public static String generateCategoryNameFromPattern(Container parent)
/*     */   {
/* 148 */     Tracer.traceMethodEntry(new Object[] { parent });
/* 149 */     String result = generateContainerNameFromPattern(parent, AppliedForCategoryOrFolder.RecordCategory);
/* 150 */     Tracer.traceMethodExit(new Object[] { result });
/* 151 */     return result;
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
/*     */   public static String generateFolderNameFromPattern(RecordContainer parent)
/*     */   {
/* 166 */     Tracer.traceMethodEntry(new Object[] { parent });
/* 167 */     String result = generateContainerNameFromPattern(parent, AppliedForCategoryOrFolder.RecordFolder);
/* 168 */     Tracer.traceMethodExit(new Object[] { result });
/* 169 */     return result;
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
/*     */   public static Boolean isRecordNameConsistentWithPattern(String proposedName, Container parent)
/*     */   {
/* 186 */     Tracer.traceMethodEntry(new Object[] { proposedName, parent });
/* 187 */     Boolean result = Boolean.FALSE;
/* 188 */     String namingPattern = parent.getProperties().getStringValue("RecordPattern");
/*     */     
/* 190 */     if ((namingPattern == null) || (namingPattern.length() == 0))
/*     */     {
/*     */ 
/* 193 */       result = Boolean.TRUE;
/*     */     }
/*     */     else
/*     */     {
/* 197 */       result = isNameConsistentWithPattern(proposedName, namingPattern, parent);
/*     */     }
/* 199 */     Tracer.traceMethodExit(new Object[] { result });
/* 200 */     return result;
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
/*     */   public static Boolean isCategoryNameConsistentWithPattern(String proposedName, Container parent)
/*     */   {
/* 218 */     Tracer.traceMethodEntry(new Object[] { proposedName, parent });
/* 219 */     Boolean result = isContainerNameConsistentWithPattern(proposedName, parent, AppliedForCategoryOrFolder.RecordCategory);
/* 220 */     Tracer.traceMethodExit(new Object[] { result });
/* 221 */     return result;
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
/*     */   public static Boolean isFolderNameConsistentWithPattern(String proposedName, Container parent)
/*     */   {
/* 238 */     Tracer.traceMethodEntry(new Object[] { proposedName, parent });
/* 239 */     Boolean result = isContainerNameConsistentWithPattern(proposedName, parent, AppliedForCategoryOrFolder.RecordFolder);
/* 240 */     Tracer.traceMethodExit(new Object[] { result });
/* 241 */     return result;
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
/*     */   public static Boolean isNamingPatternValid(String namingPattern)
/*     */   {
/* 256 */     Boolean isValid = Boolean.valueOf(false);
/* 257 */     Tracer.traceMethodEntry(new Object[] { namingPattern });
/* 258 */     Util.ckInvalidStrParam("namingPattern", namingPattern);
/* 259 */     StringBuffer tempPattern = new StringBuffer(namingPattern);
/*     */     
/*     */     for (;;)
/*     */     {
/* 263 */       int curPatternStart = tempPattern.indexOf("[");
/* 264 */       if (curPatternStart == -1)
/*     */       {
/* 266 */         if (tempPattern.indexOf("]") != -1)
/*     */         {
/* 268 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PATTERN_UNMATCHED_RIGHT_BRACKET, new Object[0]);
/*     */         }
/*     */         
/* 271 */         isValid = Boolean.valueOf(true);
/*     */       }
/*     */       else {
/* 274 */         int curPatternEnd = tempPattern.indexOf("]");
/* 275 */         if (curPatternEnd == -1)
/*     */         {
/* 277 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PATTERN_MISSING_RIGHT_BRACKET, new Object[0]);
/*     */         }
/* 279 */         if (curPatternEnd > curPatternStart + 1)
/*     */         {
/* 281 */           String curPattern = tempPattern.substring(curPatternStart + 1, curPatternEnd);
/* 282 */           isValid = checkPattern(curPattern);
/*     */         }
/*     */         else
/*     */         {
/* 286 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PATTERN_MISSING_RIGHT_BRACKET, new Object[0]);
/*     */         }
/* 288 */         tempPattern.delete(0, curPatternEnd + 1);
/* 289 */         if (tempPattern.length() == 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 294 */     Tracer.traceMethodExit(new Object[] { isValid });
/* 295 */     return isValid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String generateContainerNameFromPattern(Container parent, AppliedForCategoryOrFolder appliedFor)
/*     */   {
/* 302 */     Tracer.traceMethodEntry(new Object[] { parent, appliedFor });
/* 303 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 306 */       establishedSubject = P8CE_Util.associateSubject();
/* 307 */       String result = null;
/* 308 */       Integer increment = Integer.valueOf(1);
/*     */       
/* 310 */       Integer patternLevel = Integer.valueOf(1);
/*     */       
/* 312 */       Container tempContainer = parent;
/* 313 */       while (tempContainer.getEntityType() != EntityType.FilePlan)
/*     */       {
/* 315 */         Integer localInteger1 = patternLevel;Integer localInteger2 = patternLevel = Integer.valueOf(patternLevel.intValue() + 1);
/* 316 */         tempContainer = tempContainer.getParent();
/*     */       }
/*     */       
/* 319 */       NamingPattern filePlanPattern = null;
/* 320 */       if (parent.getEntityType() == EntityType.FilePlan)
/*     */       {
/* 322 */         filePlanPattern = ((FilePlan)parent).getNamingPattern();
/*     */       }
/*     */       else
/*     */       {
/* 326 */         filePlanPattern = ((RecordContainer)parent).getFilePlan().getNamingPattern(); }
/*     */       Object theLevel;
/* 328 */       if (filePlanPattern != null)
/*     */       {
/* 330 */         theLevel = filePlanPattern.getNamingPatternLevel(patternLevel, appliedFor);
/* 331 */         if (theLevel != null)
/*     */         {
/* 333 */           increment = ((NamingPatternLevel)theLevel).getProperties().getIntegerValue("IncrementedBy");
/* 334 */           String namingPattern = ((NamingPatternLevel)theLevel).getProperties().getStringValue("PatternString");
/* 335 */           result = generateNameFromPattern(namingPattern, increment, parent);
/*     */         }
/*     */       }
/* 338 */       Tracer.traceMethodExit(new Object[] { result });
/* 339 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 343 */       if (establishedSubject) {
/* 344 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static String generateNameFromPattern(String namingPattern, Integer increment, Container parent)
/*     */   {
/* 352 */     Tracer.traceMethodEntry(new Object[] { namingPattern, increment, parent });
/* 353 */     Util.ckInvalidStrParam("namingPattern", namingPattern);
/* 354 */     Util.ckNullObjParam("parent", parent);
/* 355 */     StringBuffer tempPattern = new StringBuffer(namingPattern);
/* 356 */     StringBuffer sb = new StringBuffer();
/* 357 */     Integer digitPatternIndex = Integer.valueOf(0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 362 */     if (hasDigitPattern(namingPattern).booleanValue())
/*     */     {
/* 364 */       digitPatternIndex = fetchPatternIndex(parent, increment.intValue());
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/* 369 */       int curPatternStart = tempPattern.indexOf("[");
/* 370 */       if (curPatternStart == -1)
/*     */       {
/* 372 */         if (tempPattern.indexOf("]") != -1)
/*     */         {
/* 374 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PATTERN_UNMATCHED_RIGHT_BRACKET, new Object[0]);
/*     */         }
/* 376 */         sb.append(tempPattern);
/*     */       }
/*     */       else {
/* 379 */         int curPatternEnd = tempPattern.indexOf("]");
/* 380 */         if (curPatternEnd == -1)
/*     */         {
/* 382 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PATTERN_MISSING_RIGHT_BRACKET, new Object[0]);
/*     */         }
/* 384 */         if (curPatternStart > 0)
/*     */         {
/* 386 */           sb.append(tempPattern.substring(0, curPatternStart));
/*     */         }
/* 388 */         if (curPatternEnd > curPatternStart + 1)
/*     */         {
/* 390 */           String curPattern = tempPattern.substring(curPatternStart + 1, curPatternEnd);
/* 391 */           sb.append(translatePattern(curPattern, parent, digitPatternIndex));
/*     */         }
/*     */         else
/*     */         {
/* 395 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PATTERN_UNMATCHED_RIGHT_BRACKET, new Object[0]);
/*     */         }
/* 397 */         tempPattern.delete(0, curPatternEnd + 1);
/* 398 */         if (tempPattern.length() == 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 404 */     String result = sb.toString();
/* 405 */     Tracer.traceMethodExit(new Object[] { result });
/* 406 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 415 */   private static Pattern digitPattern = Pattern.compile("(\\\\d)+");
/*     */   
/*     */   private static String translatePattern(String substitutionMacro, Container parent, Integer digitPatternIndex) {
/* 418 */     Tracer.traceMethodEntry(new Object[] { substitutionMacro, parent, digitPatternIndex });
/* 419 */     String result = "";
/* 420 */     Matcher matcher = digitPattern.matcher(substitutionMacro);
/*     */     
/* 422 */     if (substitutionMacro.equalsIgnoreCase("username"))
/*     */     {
/* 424 */       Repository repository = parent.getRepository();
/* 425 */       RMUser currentUser = repository.getDomain().fetchCurrentUser();
/* 426 */       result = currentUser.getShortName();
/*     */     }
/* 428 */     else if (substitutionMacro.startsWith("Parent."))
/*     */     {
/* 430 */       EngineObject jaceEO = null;
/*     */       
/* 432 */       String propertyName = substitutionMacro.substring(7);
/*     */       
/* 434 */       if (!parent.getProperties().isPropertyPresent(propertyName))
/*     */       {
/*     */         try
/*     */         {
/* 438 */           jaceEO = ((JaceBasable)parent).getJaceBaseObject();
/* 439 */           if (!jaceEO.getProperties().isPropertyPresent(propertyName))
/*     */           {
/* 441 */             ((IndependentObject)jaceEO).fetchProperties(new String[] { propertyName });
/*     */           }
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/* 446 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PATTERN_HAS_INVALID_PROPERTY, new Object[] { propertyName });
/*     */         }
/*     */       }
/*     */       
/* 450 */       if (parent.getProperties().isPropertyPresent(propertyName))
/*     */       {
/* 452 */         RMProperty curProp = parent.getProperties().get(propertyName);
/* 453 */         if (curProp.getCardinality() != RMCardinality.Single)
/*     */         {
/* 455 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_IN_PATTERN_CANNOT_BE_MULTI_VALUE, new Object[] { propertyName });
/*     */         }
/* 457 */         switch (curProp.getDataType())
/*     */         {
/*     */         case Boolean: 
/* 460 */           Boolean boolVal = curProp.getBooleanValue();
/* 461 */           if (boolVal != null)
/*     */           {
/* 463 */             result = boolVal.toString();
/*     */           }
/*     */           break;
/*     */         case DateTime: 
/* 467 */           Date dateVal = curProp.getDateTimeValue();
/* 468 */           if (dateVal != null)
/*     */           {
/* 470 */             result = dateVal.toString();
/*     */           }
/*     */           break;
/*     */         case Double: 
/* 474 */           Double doubleVal = curProp.getDoubleValue();
/* 475 */           if (doubleVal != null)
/*     */           {
/* 477 */             result = doubleVal.toString();
/*     */           }
/*     */           break;
/*     */         case Guid: 
/* 481 */           String guidVal = curProp.getGuidValue();
/* 482 */           if (guidVal != null)
/*     */           {
/* 484 */             result = guidVal.toString();
/*     */           }
/*     */           break;
/*     */         case Integer: 
/* 488 */           Integer intVal = curProp.getIntegerValue();
/* 489 */           if (intVal != null)
/*     */           {
/* 491 */             result = intVal.toString();
/*     */           }
/*     */           break;
/*     */         case String: 
/* 495 */           String strVal = curProp.getStringValue();
/* 496 */           if (strVal != null)
/*     */           {
/* 498 */             result = strVal;
/*     */           }
/*     */           break;
/*     */         default: 
/* 502 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_TYPE_IN_PATTERN_NOT_SUPPORTED, new Object[] { propertyName });
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 507 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PATTERN_HAS_INVALID_PROPERTY, new Object[] { propertyName });
/*     */       }
/*     */     }
/* 510 */     else if (matcher.matches())
/*     */     {
/*     */ 
/* 513 */       int numDigits = substitutionMacro.length() / 2;
/*     */       
/* 515 */       result = digitPatternIndex.toString();
/* 516 */       if (result.length() > numDigits)
/*     */       {
/* 518 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_DIGIT_PATTERN_TOO_SHORT, new Object[0]);
/*     */       }
/* 520 */       while (result.length() < numDigits)
/*     */       {
/* 522 */         result = "0" + result;
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */       try
/*     */       {
/* 532 */         substitutionMacro = substitutionMacro.replaceAll("m", "M");
/*     */         
/* 534 */         substitutionMacro = substitutionMacro.replaceAll("Y", "y");
/*     */         
/* 536 */         substitutionMacro = substitutionMacro.replaceAll("D", "d");
/* 537 */         SimpleDateFormat dateFormat = new SimpleDateFormat(substitutionMacro);
/* 538 */         result = dateFormat.format(new Date());
/*     */       }
/*     */       catch (Exception cause)
/*     */       {
/* 542 */         throw P8CE_Util.processJaceException(cause, RMErrorCode.API_INVALID_PATTERN, new Object[0]);
/*     */       }
/*     */     }
/*     */     
/* 546 */     Tracer.traceMethodExit(new Object[] { result });
/* 547 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Integer fetchPatternIndex(Container parent, int indexIncrement)
/*     */   {
/* 557 */     Tracer.traceMethodEntry(new Object[] { parent, Integer.valueOf(indexIncrement) });
/* 558 */     Integer result = Integer.valueOf(0);
/* 559 */     String entityGuid = null;
/* 560 */     if (parent.getEntityType() == EntityType.RecordVolume)
/*     */     {
/* 562 */       entityGuid = parent.getParent().getProperties().getGuidValue("Id").toString();
/*     */     }
/*     */     else
/*     */     {
/* 566 */       entityGuid = parent.getProperties().getGuidValue("Id").toString();
/*     */     }
/*     */     
/* 569 */     NamingPatternSequence assocSequence = getNamingPatternSequence(parent);
/* 570 */     if (assocSequence == null)
/*     */     {
/*     */ 
/* 573 */       result = Integer.valueOf(1);
/* 574 */       NamingPatternSequence newSequence = RMFactory.NamingPatternSequence.createInstance((FilePlanRepository)parent.getRepository());
/* 575 */       newSequence.getProperties().putStringValue("ParentGUID", entityGuid);
/* 576 */       newSequence.getProperties().putIntegerValue("LastPatternIndex", Integer.valueOf(1));
/* 577 */       newSequence.save(RMRefreshMode.NoRefresh);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 582 */       result = Integer.valueOf(assocSequence.getLastPatternIndex().intValue() + indexIncrement);
/*     */       
/* 584 */       assocSequence.getProperties().putIntegerValue("LastPatternIndex", result);
/* 585 */       assocSequence.save(RMRefreshMode.NoRefresh);
/*     */     }
/* 587 */     Tracer.traceMethodExit(new Object[] { result });
/* 588 */     return result;
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
/*     */   public static NamingPatternSequence getNamingPatternSequence(Container parent)
/*     */   {
/* 602 */     Tracer.traceMethodEntry(new Object[] { parent });
/* 603 */     String entityGuid = null;
/* 604 */     if (parent.getEntityType() == EntityType.RecordVolume)
/*     */     {
/* 606 */       entityGuid = parent.getParent().getProperties().getGuidValue("Id").toString();
/*     */     }
/*     */     else
/*     */     {
/* 610 */       entityGuid = parent.getProperties().getGuidValue("Id").toString();
/*     */     }
/*     */     
/* 613 */     NamingPatternSequence result = null;
/* 614 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 616 */     sb.append("Select [");
/* 617 */     sb.append("Id");
/* 618 */     sb.append("] from [PatternSequences] WHERE (");
/* 619 */     sb.append("ParentGUID");
/* 620 */     sb.append("=");
/* 621 */     sb.append("'");
/* 622 */     sb.append(entityGuid);
/* 623 */     sb.append("'");
/* 624 */     sb.append(")");
/*     */     
/* 626 */     RMSearch objSearch = new RMSearch(parent.getRepository());
/* 627 */     PageableSet<NamingPatternSequence> searchResults = objSearch.fetchObjects(sb.toString(), EntityType.PatternSequence, Integer.valueOf(1), null, Boolean.valueOf(false));
/* 628 */     if ((searchResults != null) && (!searchResults.isEmpty()))
/*     */     {
/* 630 */       Iterator<NamingPatternSequence> iter = searchResults.iterator();
/* 631 */       result = (NamingPatternSequence)iter.next();
/*     */     }
/*     */     
/* 634 */     Tracer.traceMethodExit(new Object[] { result });
/* 635 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Boolean hasDigitPattern(String patternString)
/*     */   {
/* 642 */     Tracer.traceMethodEntry(new Object[] { patternString });
/* 643 */     Boolean result = Boolean.FALSE;
/* 644 */     Pattern anyDigitPattern = Pattern.compile("\\[(\\\\d)+\\]");
/* 645 */     Matcher matcher = anyDigitPattern.matcher(patternString);
/*     */     
/* 647 */     if (matcher.find())
/*     */     {
/* 649 */       result = Boolean.TRUE;
/*     */     }
/* 651 */     Tracer.traceMethodExit(new Object[] { result });
/* 652 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Boolean isContainerNameConsistentWithPattern(String proposedName, Container parent, AppliedForCategoryOrFolder appliedFor)
/*     */   {
/* 660 */     Tracer.traceMethodEntry(new Object[] { proposedName, parent, appliedFor });
/* 661 */     Boolean result = Boolean.TRUE;
/*     */     
/* 663 */     Integer patternLevel = Integer.valueOf(1);
/*     */     
/* 665 */     Container tempContainer = parent;
/* 666 */     while (tempContainer.getEntityType() != EntityType.FilePlan)
/*     */     {
/* 668 */       Integer localInteger1 = patternLevel;Integer localInteger2 = patternLevel = Integer.valueOf(patternLevel.intValue() + 1);
/* 669 */       tempContainer = tempContainer.getParent();
/*     */     }
/*     */     
/* 672 */     NamingPattern filePlanPattern = null;
/* 673 */     if (parent.getEntityType() == EntityType.FilePlan)
/*     */     {
/* 675 */       filePlanPattern = ((FilePlan)parent).getNamingPattern();
/*     */     }
/*     */     else
/*     */     {
/* 679 */       filePlanPattern = ((RecordContainer)parent).getFilePlan().getNamingPattern();
/*     */     }
/* 681 */     if (filePlanPattern != null)
/*     */     {
/* 683 */       NamingPatternLevel theLevel = filePlanPattern.getNamingPatternLevel(patternLevel, appliedFor);
/* 684 */       if (theLevel != null)
/*     */       {
/* 686 */         String namingPattern = theLevel.getProperties().getStringValue("PatternString");
/* 687 */         result = isNameConsistentWithPattern(proposedName, namingPattern, parent);
/*     */       }
/*     */     }
/* 690 */     Tracer.traceMethodExit(new Object[] { result });
/* 691 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Boolean isNameConsistentWithPattern(String proposedName, String namingPattern, Container parent)
/*     */   {
/* 698 */     Tracer.traceMethodEntry(new Object[] { proposedName, namingPattern, parent });
/* 699 */     Boolean result = Boolean.FALSE;
/* 700 */     StringBuilder tempNamingPattern = new StringBuilder(namingPattern);
/*     */     
/* 702 */     Pattern anyDigitPattern = Pattern.compile("\\[(\\\\d)+\\]");
/* 703 */     Matcher matcher = anyDigitPattern.matcher(tempNamingPattern);
/* 704 */     while (matcher.find())
/*     */     {
/* 706 */       Integer digitPatternStart = Integer.valueOf(matcher.start());
/* 707 */       Integer digitPatternEnd = Integer.valueOf(matcher.end());
/* 708 */       Integer numDigits = Integer.valueOf((digitPatternEnd.intValue() - digitPatternStart.intValue() - 1) / 2);
/* 709 */       String digitReplacement = "\\d{" + numDigits.toString() + "}";
/* 710 */       tempNamingPattern.replace(digitPatternStart.intValue(), digitPatternEnd.intValue(), digitReplacement);
/* 711 */       matcher = anyDigitPattern.matcher(tempNamingPattern);
/*     */     }
/*     */     
/* 714 */     String generatedPattern = generateNameFromPattern(tempNamingPattern.toString(), Integer.valueOf(1), parent);
/*     */     
/* 716 */     Pattern pattern = Pattern.compile(generatedPattern);
/* 717 */     Matcher nameMatcher = pattern.matcher(proposedName);
/* 718 */     if (nameMatcher.matches())
/*     */     {
/* 720 */       result = Boolean.TRUE;
/*     */     }
/* 722 */     Tracer.traceMethodExit(new Object[] { result });
/* 723 */     return result;
/*     */   }
/*     */   
/* 726 */   private static Pattern hasDigitPattern = Pattern.compile("\\\\d");
/*     */   
/*     */   private static Boolean checkPattern(String substitutionMacro)
/*     */   {
/* 730 */     Boolean result = Boolean.valueOf(false);
/* 731 */     Tracer.traceMethodEntry(new Object[] { substitutionMacro });
/* 732 */     Matcher hasDigitMatcher = hasDigitPattern.matcher(substitutionMacro);
/* 733 */     Matcher matcher = digitPattern.matcher(substitutionMacro);
/*     */     
/* 735 */     if ((substitutionMacro.equalsIgnoreCase("username")) || (substitutionMacro.startsWith("Parent.")))
/*     */     {
/*     */ 
/* 738 */       result = Boolean.valueOf(true);
/*     */     }
/* 740 */     else if (hasDigitMatcher.find()) {
/* 741 */       if (matcher.matches()) {
/* 742 */         result = Boolean.valueOf(true);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/*     */ 
/* 752 */         substitutionMacro = substitutionMacro.replaceAll("m", "M");
/*     */         
/* 754 */         substitutionMacro = substitutionMacro.replaceAll("Y", "y");
/*     */         
/* 756 */         substitutionMacro = substitutionMacro.replaceAll("D", "d");
/* 757 */         SimpleDateFormat dateFormat = new SimpleDateFormat(substitutionMacro);
/* 758 */         dateFormat.format(new Date());
/* 759 */         result = Boolean.valueOf(true);
/*     */       }
/*     */       catch (Exception cause)
/*     */       {
/* 763 */         throw P8CE_Util.processJaceException(cause, RMErrorCode.API_INVALID_PATTERN, new Object[0]);
/*     */       }
/*     */     }
/*     */     
/* 767 */     if (!result.booleanValue())
/*     */     {
/* 769 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PATTERN, new Object[0]);
/*     */     }
/*     */     
/* 772 */     Tracer.traceMethodExit(new Object[] { result });
/* 773 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\NamingUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */