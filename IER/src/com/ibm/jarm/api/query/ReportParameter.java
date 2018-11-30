/*     */ package com.ibm.jarm.api.query;
/*     */ 
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReportParameter
/*     */ {
/*  17 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*     */   
/*     */   private String name;
/*     */   private boolean isRequired;
/*  21 */   private Map<String, ParameterValue> paraValueMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReportParameter(String name, boolean isRequired)
/*     */   {
/*  31 */     Tracer.traceMethodEntry(new Object[] { name, Boolean.valueOf(isRequired) });
/*  32 */     this.name = name;
/*  33 */     this.isRequired = isRequired;
/*  34 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  44 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  54 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRequired()
/*     */   {
/*  64 */     return this.isRequired;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequired(boolean isRequired)
/*     */   {
/*  74 */     this.isRequired = isRequired;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getAvailValueIds()
/*     */   {
/*  85 */     Tracer.traceMethodEntry(new Object[0]);
/*  86 */     List<String> result = new ArrayList();
/*  87 */     if ((this.paraValueMap != null) && (!this.paraValueMap.isEmpty()))
/*     */     {
/*  89 */       Set<String> ids = this.paraValueMap.keySet();
/*  90 */       for (Object id : ids) {
/*  91 */         result.add((String)id);
/*     */       }
/*     */     }
/*  94 */     Tracer.traceMethodExit(new Object[] { result });
/*  95 */     return result;
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
/*     */   public String getAvailValue(String valueId)
/*     */   {
/* 108 */     Tracer.traceMethodEntry(new Object[] { valueId });
/* 109 */     String result = null;
/* 110 */     if (this.paraValueMap != null)
/*     */     {
/* 112 */       ParameterValue pValue = (ParameterValue)this.paraValueMap.get(valueId);
/* 113 */       result = pValue != null ? pValue.value : null;
/*     */     }
/*     */     
/* 116 */     Tracer.traceMethodExit(new Object[] { result });
/* 117 */     return result;
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
/*     */   public String getAvailValueLocalizationKey(String valueId)
/*     */   {
/* 130 */     Tracer.traceMethodEntry(new Object[] { valueId });
/* 131 */     String result = null;
/* 132 */     if (this.paraValueMap != null)
/*     */     {
/* 134 */       ParameterValue pValue = (ParameterValue)this.paraValueMap.get(valueId);
/* 135 */       result = pValue != null ? pValue.localizationKey : null;
/*     */     }
/*     */     
/* 138 */     Tracer.traceMethodExit(new Object[] { result });
/* 139 */     return result;
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
/*     */   public void addValues(String id, String value, String localKey)
/*     */   {
/* 152 */     Tracer.traceMethodEntry(new Object[] { id, value, localKey });
/* 153 */     this.paraValueMap.put(id, new ParameterValue(id, value, localKey));
/* 154 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\query\ReportParameter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */