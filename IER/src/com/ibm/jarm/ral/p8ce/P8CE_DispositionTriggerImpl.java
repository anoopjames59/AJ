/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.ibm.jarm.api.constants.DispositionTriggerType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.DispositionTrigger;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_DispositionTriggerImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements DispositionTrigger
/*     */ {
/*  36 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  37 */   private static final IGenerator<DispositionTrigger> DispTriggerGenerator = new Generator();
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
/*  48 */   private static final String[] MandatoryPropertyNames = { "Id", "DisposalTriggerName", "RMEntityType", "RMEntityDescription", "AGGREGATION", "PropertyName", "PropertyValue", "Operator", "DateTime", "CycleYears", "CycleMonths", "CycleDays", "EventType", "ExternalEventOccurrenceDate", "ConditionXML" };
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
/*  59 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  61 */     List<FilterElement> tempList = new ArrayList(1);
/*  62 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  63 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  68 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  73 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<DispositionTrigger> getGenerator()
/*     */   {
/*  83 */     return DispTriggerGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  91 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */   P8CE_DispositionTriggerImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/*  96 */     super(EntityType.DispositionTrigger, repository, identity, jaceCustomObject, isPlaceholder);
/*  97 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  98 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTriggerName()
/*     */   {
/* 106 */     Tracer.traceMethodEntry(new Object[0]);
/* 107 */     String result = P8CE_Util.getJacePropertyAsString(this, "DisposalTriggerName");
/* 108 */     Tracer.traceMethodExit(new Object[] { result });
/* 109 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTriggerName(String triggerName)
/*     */   {
/* 117 */     Tracer.traceMethodEntry(new Object[] { triggerName });
/* 118 */     Util.ckInvalidStrParam("triggerName", triggerName);
/* 119 */     getProperties().putStringValue("DisposalTriggerName", triggerName);
/* 120 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DispositionTriggerType getTriggerType()
/*     */   {
/* 128 */     Tracer.traceMethodEntry(new Object[0]);
/* 129 */     DispositionTriggerType result = null;
/* 130 */     Integer rawType = P8CE_Util.getJacePropertyAsInteger(this, "EventType");
/* 131 */     if (rawType != null) {
/* 132 */       result = DispositionTriggerType.getInstanceFromInt(rawType.intValue());
/*     */     }
/* 134 */     Tracer.traceMethodExit(new Object[] { result });
/* 135 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 143 */     Tracer.traceMethodEntry(new Object[0]);
/* 144 */     String result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/* 145 */     Tracer.traceMethodExit(new Object[] { result });
/* 146 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String value)
/*     */   {
/* 154 */     Tracer.traceMethodEntry(new Object[] { value });
/* 155 */     getProperties().putStringValue("RMEntityDescription", value);
/* 156 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAggregation()
/*     */   {
/* 164 */     Tracer.traceMethodEntry(new Object[0]);
/* 165 */     String result = P8CE_Util.getJacePropertyAsString(this, "AGGREGATION");
/* 166 */     Tracer.traceMethodExit(new Object[] { result });
/* 167 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAggregation(String aggregation)
/*     */   {
/* 175 */     Tracer.traceMethodEntry(new Object[] { aggregation });
/* 176 */     Util.ckInvalidStrParam("aggregation", aggregation);
/* 177 */     getProperties().putStringValue("AGGREGATION", aggregation);
/* 178 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyName()
/*     */   {
/* 186 */     Tracer.traceMethodEntry(new Object[0]);
/* 187 */     String result = P8CE_Util.getJacePropertyAsString(this, "PropertyName");
/* 188 */     Tracer.traceMethodExit(new Object[] { result });
/* 189 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropertyName(String propSymbolicName)
/*     */   {
/* 197 */     Tracer.traceMethodEntry(new Object[] { propSymbolicName });
/* 198 */     getProperties().putStringValue("PropertyName", propSymbolicName);
/* 199 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyValue()
/*     */   {
/* 207 */     Tracer.traceMethodEntry(new Object[0]);
/* 208 */     String result = P8CE_Util.getJacePropertyAsString(this, "PropertyValue");
/* 209 */     Tracer.traceMethodExit(new Object[] { result });
/* 210 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropertyValue(String value)
/*     */   {
/* 218 */     Tracer.traceMethodEntry(new Object[] { value });
/* 219 */     getProperties().putStringValue("PropertyValue", value);
/* 220 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getOperator()
/*     */   {
/* 228 */     Tracer.traceMethodEntry(new Object[0]);
/* 229 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "Operator");
/* 230 */     Tracer.traceMethodExit(new Object[] { result });
/* 231 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOperator(Integer value)
/*     */   {
/* 239 */     Tracer.traceMethodEntry(new Object[] { value });
/* 240 */     getProperties().putIntegerValue("Operator", value);
/* 241 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getDateTime()
/*     */   {
/* 249 */     Tracer.traceMethodEntry(new Object[0]);
/* 250 */     Date result = P8CE_Util.getJacePropertyAsDate(this, "DateTime");
/* 251 */     Tracer.traceMethodExit(new Object[] { result });
/* 252 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateTime(Date dateValue)
/*     */   {
/* 260 */     Tracer.traceMethodEntry(new Object[] { dateValue });
/* 261 */     getProperties().putDateTimeValue("DateTime", dateValue);
/* 262 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer[] getRecurringCyclePeriod()
/*     */   {
/* 270 */     Tracer.traceMethodEntry(new Object[0]);
/* 271 */     Integer[] results = new Integer[3];
/* 272 */     results[0] = P8CE_Util.getJacePropertyAsInteger(this, "CycleYears");
/* 273 */     results[1] = P8CE_Util.getJacePropertyAsInteger(this, "CycleMonths");
/* 274 */     results[2] = P8CE_Util.getJacePropertyAsInteger(this, "CycleDays");
/*     */     
/* 276 */     Tracer.traceMethodExit((Object[])results);
/* 277 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRecurringCyclePeriod(Integer years, Integer months, Integer days)
/*     */   {
/* 285 */     Tracer.traceMethodEntry(new Object[] { years, months, days });
/* 286 */     Properties jaceProps = this.jaceCustomObject.getProperties();
/* 287 */     jaceProps.putValue("CycleYears", years);
/* 288 */     jaceProps.putValue("CycleMonths", months);
/* 289 */     jaceProps.putValue("CycleDays", days);
/* 290 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getExternalEventOccurrenceDate()
/*     */   {
/* 298 */     Tracer.traceMethodEntry(new Object[0]);
/* 299 */     Date result = P8CE_Util.getJacePropertyAsDate(this, "ExternalEventOccurrenceDate");
/* 300 */     Tracer.traceMethodExit(new Object[] { result });
/* 301 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExternalEventOccurrenceDate(Date dateValue)
/*     */   {
/* 309 */     Tracer.traceMethodEntry(new Object[] { dateValue });
/* 310 */     getProperties().putDateTimeValue("ExternalEventOccurrenceDate", dateValue);
/* 311 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getConditionXML()
/*     */   {
/* 319 */     Tracer.traceMethodEntry(new Object[0]);
/* 320 */     String result = P8CE_Util.getJacePropertyAsString(this, "ConditionXML");
/* 321 */     Tracer.traceMethodExit(new Object[] { result });
/* 322 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConditionXML(String xmlFragment)
/*     */   {
/* 330 */     Tracer.traceMethodEntry(new Object[] { xmlFragment });
/* 331 */     getProperties().putStringValue("ConditionXML", xmlFragment);
/* 332 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<DispositionSchedule> getAssociatedDispositionSchedules()
/*     */   {
/* 340 */     Tracer.traceMethodEntry(new Object[0]);
/* 341 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 344 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 346 */       List<FilterElement> jaceDispSchedFEs = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 347 */       IGenerator<DispositionSchedule> generator = P8CE_DispositionScheduleImpl.getGenerator();
/* 348 */       List<DispositionSchedule> results = fetchMVObjPropValueAsList(jaceDispSchedFEs, "AssociatedDisposalTrigger", generator);
/*     */       
/* 350 */       Tracer.traceMethodExit(new Object[] { results });
/* 351 */       return results;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 355 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 359 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "AssociatedDisposalTrigger" });
/*     */     }
/*     */     finally
/*     */     {
/* 363 */       if (establishedSubject) {
/* 364 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 374 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 375 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 378 */       establishedSubject = P8CE_Util.associateSubject();
/* 379 */       RALDispositionLogic.validateDispositionTrigger((FilePlanRepository)getRepository(), this);
/* 380 */       super.save(jarmRefreshMode);
/* 381 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 385 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 389 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.DispositionTrigger });
/*     */     }
/*     */     finally
/*     */     {
/* 393 */       if (establishedSubject) {
/* 394 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 404 */     return super.toString("P8CE_DispositionTriggerImpl", "DisposalTriggerName");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<DispositionTrigger>
/*     */   {
/*     */     public DispositionTrigger create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 418 */       P8CE_DispositionTriggerImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 419 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 421 */       String identity = "<undefined>";
/* 422 */       if (jaceCustomObj.getProperties().isPropertyPresent("DisposalTriggerName")) {
/* 423 */         identity = jaceCustomObj.getProperties().getStringValue("DisposalTriggerName");
/*     */       }
/* 425 */       DispositionTrigger result = new P8CE_DispositionTriggerImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 427 */       P8CE_DispositionTriggerImpl.Tracer.traceMethodExit(new Object[] { result });
/* 428 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_DispositionTriggerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */