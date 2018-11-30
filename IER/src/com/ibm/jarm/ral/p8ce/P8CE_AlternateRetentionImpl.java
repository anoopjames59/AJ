/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.collection.RepositoryRowSet;
/*      */ import com.filenet.api.core.CustomObject;
/*      */ import com.filenet.api.core.Factory.CustomObject;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.core.UpdatingBatch;
/*      */ import com.filenet.api.property.FilterElement;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.query.RepositoryRow;
/*      */ import com.filenet.api.query.SearchSQL;
/*      */ import com.filenet.api.query.SearchScope;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*      */ import com.ibm.jarm.api.core.AlternateRetention;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.DispositionPhase;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class P8CE_AlternateRetentionImpl
/*      */   extends P8CE_RMCustomObjectImpl
/*      */   implements AlternateRetention
/*      */ {
/*  890 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  891 */   private static final IGenerator<AlternateRetention> AltRetentionGenerator = new Generator();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  902 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "RetentionPeriodYears", "RetentionPeriodMonths", "RetentionPeriodDays", "RetentionBase", "ConditionXML", "IntelligentRetentionNumber" };
/*      */   
/*      */   private static final List<FilterElement> MandatoryJaceFEs;
/*      */   private P8CE_DispositionPhaseImpl parentPhase;
/*      */   
/*      */   static
/*      */   {
/*  909 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*      */     
/*  911 */     List<FilterElement> tempList = new ArrayList(1);
/*  912 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  913 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*      */   }
/*      */   
/*      */   static String[] getMandatoryPropertyNames()
/*      */   {
/*  918 */     return MandatoryPropertyNames;
/*      */   }
/*      */   
/*      */   static List<FilterElement> getMandatoryJaceFEs()
/*      */   {
/*  923 */     return MandatoryJaceFEs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static IGenerator<AlternateRetention> getGenerator()
/*      */   {
/*  933 */     return AltRetentionGenerator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<FilterElement> getMandatoryFEs()
/*      */   {
/*  944 */     return getMandatoryJaceFEs();
/*      */   }
/*      */   
/*      */   static AlternateRetention createNew(Repository repository, P8CE_DispositionPhaseImpl owningPhase, String idStr)
/*      */   {
/*  949 */     Tracer.traceMethodEntry(new Object[] { repository, owningPhase, idStr });
/*  950 */     ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/*  951 */     Id newId = P8CE_Util.processIdStr(idStr);
/*  952 */     CustomObject jaceCustObj = Factory.CustomObject.createInstance(jaceObjStore, "AlternateRetention", newId);
/*  953 */     jaceCustObj.getProperties().putValue("AssociatedPhase", owningPhase.jaceCustomObject);
/*      */     
/*  955 */     String identifier = "aNewAltRetention";
/*  956 */     P8CE_AlternateRetentionImpl result = new P8CE_AlternateRetentionImpl(repository, identifier, jaceCustObj, true);
/*  957 */     result.parentPhase = owningPhase;
/*  958 */     Tracer.traceMethodExit(new Object[] { result });
/*  959 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   protected P8CE_AlternateRetentionImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*      */   {
/*  965 */     super(EntityType.AlternateRetention, repository, identity, jaceCustomObject, isPlaceholder);
/*  966 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  967 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getRetentionNumber()
/*      */   {
/*  975 */     Tracer.traceMethodEntry(new Object[0]);
/*  976 */     Integer result = P8CE_Util.getJacePropertyAsInteger(this, "IntelligentRetentionNumber");
/*  977 */     Tracer.traceMethodExit(new Object[] { result });
/*  978 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispositionPhase getDispositionPhase()
/*      */   {
/*  986 */     Tracer.traceMethodEntry(new Object[0]);
/*  987 */     boolean establishedSubject = false;
/*      */     try {
/*      */       List<FilterElement> jaceDispPhaseFEs;
/*  990 */       if (this.parentPhase == null)
/*      */       {
/*  992 */         establishedSubject = P8CE_Util.associateSubject();
/*      */         
/*  994 */         jaceDispPhaseFEs = P8CE_DispositionPhaseImpl.getMandatoryJaceFEs();
/*  995 */         IGenerator<DispositionPhase> generator = P8CE_DispositionPhaseImpl.getGenerator();
/*  996 */         this.parentPhase = ((P8CE_DispositionPhaseImpl)fetchSVObjPropValue(jaceDispPhaseFEs, "AssociatedPhase", generator));
/*      */       }
/*      */       
/*  999 */       Tracer.traceMethodExit(new Object[] { this.parentPhase });
/* 1000 */       return this.parentPhase;
/*      */     }
/*      */     catch (RMRuntimeException ex)
/*      */     {
/* 1004 */       throw ex;
/*      */     }
/*      */     catch (Exception cause)
/*      */     {
/* 1008 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*      */     }
/*      */     finally
/*      */     {
/* 1012 */       if (establishedSubject) {
/* 1013 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void setDispositionPhase(P8CE_DispositionPhaseImpl parentPhase) {
/* 1019 */     this.parentPhase = parentPhase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRetentionBase()
/*      */   {
/* 1027 */     Tracer.traceMethodEntry(new Object[0]);
/* 1028 */     String result = P8CE_Util.getJacePropertyAsString(this, "RetentionBase");
/* 1029 */     Tracer.traceMethodExit(new Object[] { result });
/* 1030 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRetentionBase(String propSymbolicName)
/*      */   {
/* 1038 */     Tracer.traceMethodEntry(new Object[] { propSymbolicName });
/* 1039 */     Util.ckInvalidStrParam("propSymbolicName", propSymbolicName);
/* 1040 */     getProperties().putStringValue("RetentionBase", propSymbolicName);
/* 1041 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer[] getRetentionPeriod()
/*      */   {
/* 1049 */     Tracer.traceMethodEntry(new Object[0]);
/* 1050 */     Integer[] results = new Integer[3];
/* 1051 */     results[0] = P8CE_Util.getJacePropertyAsInteger(this, "RetentionPeriodYears");
/* 1052 */     results[1] = P8CE_Util.getJacePropertyAsInteger(this, "RetentionPeriodMonths");
/* 1053 */     results[2] = P8CE_Util.getJacePropertyAsInteger(this, "RetentionPeriodDays");
/*      */     
/* 1055 */     Tracer.traceMethodExit((Object[])results);
/* 1056 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRetentionPeriod(Integer years, Integer months, Integer days)
/*      */   {
/* 1064 */     Tracer.traceMethodEntry(new Object[] { years, months, days });
/* 1065 */     Integer actualYears = years != null ? years : Integer.valueOf(0);
/* 1066 */     Integer actualMonths = months != null ? months : Integer.valueOf(0);
/* 1067 */     Integer actualDays = days != null ? days : Integer.valueOf(0);
/*      */     
/* 1069 */     Properties jaceProps = this.jaceCustomObject.getProperties();
/* 1070 */     jaceProps.putValue("RetentionPeriodYears", actualYears);
/* 1071 */     jaceProps.putValue("RetentionPeriodMonths", actualMonths);
/* 1072 */     jaceProps.putValue("RetentionPeriodDays", actualDays);
/* 1073 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConditionXML()
/*      */   {
/* 1081 */     Tracer.traceMethodEntry(new Object[0]);
/* 1082 */     String result = P8CE_Util.getJacePropertyAsString(this, "ConditionXML");
/* 1083 */     Tracer.traceMethodExit(new Object[] { result });
/* 1084 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConditionXML(String xmlFragment)
/*      */   {
/* 1092 */     Tracer.traceMethodEntry(new Object[] { xmlFragment });
/* 1093 */     Util.ckInvalidStrParam("xmlFragment", xmlFragment);
/* 1094 */     getProperties().putStringValue("ConditionXML", xmlFragment);
/* 1095 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void delete()
/*      */   {
/* 1104 */     Tracer.traceMethodEntry(new Object[0]);
/* 1105 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_CANT_DIRECT_DELETE_ALTRETENT, new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void save(RMRefreshMode jarmRefreshMode)
/*      */   {
/* 1114 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/*      */     
/* 1116 */     this.parentPhase.save(jarmRefreshMode);
/* 1117 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   void contributeToSaveBatch(RMRefreshMode jarmRefreshMode, UpdatingBatch jaceUB)
/*      */   {
/* 1122 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode, jaceUB });
/* 1123 */     if (this.jaceCustomObject.getProperties().isDirty())
/*      */     {
/* 1125 */       jaceUB.add(this.jaceCustomObject, null);
/*      */     }
/* 1127 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Date calcEffectiveRetentionDate(BaseEntity entity)
/*      */     throws Exception
/*      */   {
/* 1143 */     Tracer.traceMethodEntry(new Object[] { entity });
/* 1144 */     Date result = null;
/*      */     
/* 1146 */     String conditionXML = getConditionXML();
/*      */     
/* 1148 */     if (conditionXML != null)
/*      */     {
/* 1150 */       String whereClause = RALDispositionLogic.extractWhereClauseFromConditionXML(conditionXML);
/* 1151 */       boolean startsWithAND = (whereClause != null) && (whereClause.trim().toUpperCase().startsWith("AND "));
/*      */       
/*      */ 
/* 1154 */       String basePropSymName = getRetentionBase();
/* 1155 */       String tableName = entity.getProperties().getStringValue("AGGREGATION");
/*      */       
/* 1157 */       StringBuilder sb = new StringBuilder();
/* 1158 */       sb.append("SELECT Id, ").append(basePropSymName);
/* 1159 */       sb.append(" FROM ").append(tableName);
/* 1160 */       sb.append(" WHERE (Id = '").append(entity.getObjectIdentity()).append("') ");
/* 1161 */       if (!startsWithAND)
/* 1162 */         sb.append("AND ");
/* 1163 */       sb.append(whereClause).append(" ");
/* 1164 */       String sql = sb.toString();
/*      */       
/* 1166 */       Repository repository = entity.getRepository();
/* 1167 */       ObjectStore jaceObjStore = ((P8CE_RepositoryImpl)repository).getJaceObjectStore();
/* 1168 */       SearchSQL searchSQL = new SearchSQL(sql);
/* 1169 */       SearchScope scope = new SearchScope(jaceObjStore);
/* 1170 */       RepositoryRowSet rowSet = scope.fetchRows(searchSQL, null, null, Boolean.FALSE);
/* 1171 */       Iterator it = rowSet.iterator();
/* 1172 */       if (it.hasNext())
/*      */       {
/* 1174 */         RepositoryRow row = (RepositoryRow)it.next();
/* 1175 */         Date entityBaseDate = row.getProperties().getDateTimeValue(basePropSymName);
/* 1176 */         result = RALDispositionLogic.addRetentionPeriod(entityBaseDate, getRetentionPeriod());
/*      */       }
/*      */     }
/*      */     
/* 1180 */     Tracer.traceMethodExit(new Object[] { result });
/* 1181 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class Generator
/*      */     implements IGenerator<AlternateRetention>
/*      */   {
/*      */     public AlternateRetention create(Repository repository, Object jaceBaseObject)
/*      */     {
/* 1194 */       P8CE_AlternateRetentionImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 1195 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*      */       
/* 1197 */       String identity = "AlternateRetention";
/* 1198 */       AlternateRetention result = new P8CE_AlternateRetentionImpl(repository, identity, jaceCustomObj, false);
/*      */       
/* 1200 */       P8CE_AlternateRetentionImpl.Tracer.traceMethodExit(new Object[] { result });
/* 1201 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class AlternateRetentionComparator
/*      */     implements Comparator<AlternateRetention>
/*      */   {
/*      */     public int compare(AlternateRetention o1, AlternateRetention o2)
/*      */     {
/* 1216 */       P8CE_AlternateRetentionImpl.Tracer.traceMethodEntry(new Object[] { o1, o2 });
/* 1217 */       Integer o1AltNum = o1.getRetentionNumber();
/* 1218 */       Integer o2AltNum = o2.getRetentionNumber();
/*      */       
/* 1220 */       int result = o1AltNum.compareTo(o2AltNum);
/* 1221 */       P8CE_AlternateRetentionImpl.Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/* 1222 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_AlternateRetentionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */