/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.constants.SchedulePropagation;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordType;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALBaseRepository;
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
/*     */ class P8CE_RecordTypeImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements RecordType
/*     */ {
/*  36 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  37 */   private static final IGenerator<RecordType> RMRecordTypeGenerator = new Generator();
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
/*  48 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "RecordTypeName", "RMEntityDescription", "DisposalSchedule" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   static
/*     */   {
/*  53 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  55 */     List<FilterElement> tempList = new ArrayList(1);
/*  56 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  57 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  62 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  67 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RecordType> getGenerator()
/*     */   {
/*  77 */     return RMRecordTypeGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  85 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */   P8CE_RecordTypeImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/*  91 */     super(EntityType.RecordType, repository, identity, jaceCustomObject, isPlaceholder);
/*  92 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  93 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 102 */     Tracer.traceMethodEntry(new Object[0]);
/* 103 */     String result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/* 104 */     Tracer.traceMethodExit(new Object[] { result });
/* 105 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String value)
/*     */   {
/* 113 */     Tracer.traceMethodEntry(new Object[] { value });
/* 114 */     getProperties().putStringValue("RMEntityDescription", value);
/* 115 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRecordTypeName()
/*     */   {
/* 123 */     Tracer.traceMethodEntry(new Object[0]);
/* 124 */     String result = P8CE_Util.getJacePropertyAsString(this, "RecordTypeName");
/* 125 */     Tracer.traceMethodExit(new Object[] { result });
/* 126 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRecordTypeName(String name)
/*     */   {
/* 134 */     Tracer.traceMethodEntry(new Object[] { name });
/* 135 */     Util.ckInvalidStrParam("name", name);
/* 136 */     getProperties().putStringValue("RecordTypeName", name);
/* 137 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 146 */     Tracer.traceMethodEntry(new Object[0]);
/* 147 */     String result = getRecordTypeName();
/* 148 */     Tracer.traceMethodExit(new Object[] { result });
/* 149 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<Record> getAssociatedRecords()
/*     */   {
/* 157 */     Tracer.traceMethodEntry(new Object[0]);
/* 158 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 161 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 163 */       List<FilterElement> jaceRecordFEs = P8CE_RecordImpl.getMandatoryJaceFEs();
/* 164 */       IGenerator<Record> generator = P8CE_RecordImpl.getGenerator();
/* 165 */       PageableSet<Record> resultSet = fetchMVObjPropValueAsPageableSet(jaceRecordFEs, "AssociatedRecordPtr", generator);
/*     */       
/* 167 */       Tracer.traceMethodExit(new Object[] { resultSet });
/* 168 */       return resultSet;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 172 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 176 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 180 */       if (establishedSubject) {
/* 181 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 191 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 192 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 195 */       establishedSubject = P8CE_Util.associateSubject();
/* 196 */       RALDispositionLogic.validateRecordType((RALBaseRepository)getRepository(), this);
/*     */       
/*     */ 
/*     */ 
/* 200 */       this.jaceCustomObject.getProperties().putValue("RecalculatePhaseRetention", 5);
/*     */       
/* 202 */       super.save(jarmRefreshMode);
/* 203 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 207 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 211 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.RecordType });
/*     */     }
/*     */     finally
/*     */     {
/* 215 */       if (establishedSubject) {
/* 216 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 226 */     return super.toString("P8CE_RecordTypeImpl", "RecordTypeName");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void assignDispositionSchedule(DispositionSchedule schedule, SchedulePropagation propagationMode)
/*     */   {
/* 234 */     Tracer.traceMethodEntry(new Object[] { schedule, propagationMode });
/* 235 */     Util.ckNullObjParam("schedule", schedule);
/* 236 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 239 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 241 */       Date now = new Date();
/* 242 */       RMProperties jarmProps = getProperties();
/* 243 */       jarmProps.putObjectValue("DisposalSchedule", schedule);
/* 244 */       jarmProps.putDateTimeValue("DisposalScheduleAllocationDate", now);
/* 245 */       jarmProps.putDateTimeValue("LastSweepDate", null);
/* 246 */       jarmProps.putDateTimeValue("ExternalEventOccurrenceDate", null);
/* 247 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 251 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 255 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 259 */       if (establishedSubject) {
/* 260 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearDispositionAssignment(SchedulePropagation propagationMode)
/*     */   {
/* 269 */     Tracer.traceMethodEntry(new Object[] { propagationMode });
/* 270 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DispositionSchedule getAssignedSchedule()
/*     */   {
/* 278 */     Tracer.traceMethodEntry(new Object[0]);
/* 279 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 282 */       establishedSubject = P8CE_Util.associateSubject();
/* 283 */       List<FilterElement> jaceDispSchedFEs = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 284 */       IGenerator<DispositionSchedule> generator = P8CE_DispositionScheduleImpl.getGenerator();
/* 285 */       DispositionSchedule result = (DispositionSchedule)fetchSVObjPropValue(jaceDispSchedFEs, "DisposalSchedule", generator);
/*     */       
/* 287 */       Tracer.traceMethodExit(new Object[] { result });
/* 288 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 292 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 296 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 300 */       if (establishedSubject) {
/* 301 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RecordType>
/*     */   {
/*     */     public RecordType create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 316 */       P8CE_RecordTypeImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 317 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 319 */       String identity = "<undefined>";
/* 320 */       if (jaceCustomObj.getProperties().isPropertyPresent("RecordTypeName")) {
/* 321 */         identity = jaceCustomObj.getProperties().getStringValue("RecordTypeName");
/*     */       }
/* 323 */       RecordType result = new P8CE_RecordTypeImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 325 */       P8CE_RecordTypeImpl.Tracer.traceMethodExit(new Object[] { result });
/* 326 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RecordTypeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */