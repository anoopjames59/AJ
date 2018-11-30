/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.exception.ErrorRecord;
/*     */ import com.ibm.jarm.api.exception.Diagnostic;
/*     */ import com.ibm.jarm.api.exception.RMErrorRecord;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMErrorRecord
/*     */   implements RMErrorRecord
/*     */ {
/*     */   private ErrorRecord jaceErrorRecord;
/*     */   private String description;
/*     */   private String source;
/*     */   private List<Diagnostic> diagnostics;
/*     */   
/*     */   public String getDescription()
/*     */   {
/*  87 */     return this.description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Diagnostic[] getDiagnosticTypes()
/*     */   {
/*  95 */     Diagnostic[] result = new Diagnostic[0];
/*  96 */     if (this.diagnostics != null)
/*     */     {
/*  98 */       result = (Diagnostic[])this.diagnostics.toArray(new Diagnostic[this.diagnostics.size()]);
/*     */     }
/*     */     
/* 101 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSource()
/*     */   {
/* 109 */     return this.source;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     if (this.jaceErrorRecord != null) {
/* 119 */       return this.jaceErrorRecord.toString();
/*     */     }
/* 121 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */   P8CE_RMErrorRecord(ErrorRecord jaceErrorRecord)
/*     */   {
/* 127 */     if (jaceErrorRecord != null)
/*     */     {
/* 129 */       this.jaceErrorRecord = jaceErrorRecord;
/* 130 */       this.description = jaceErrorRecord.getDescription();
/* 131 */       this.source = jaceErrorRecord.getSource();
/*     */       
/* 133 */       Map<String, Object> jaceDiagTypes = jaceErrorRecord.getDiagnosticTypes();
/* 134 */       if (jaceDiagTypes != null)
/*     */       {
/* 136 */         Set<Map.Entry<String, Object>> entrySet = jaceDiagTypes.entrySet();
/* 137 */         if (entrySet != null)
/*     */         {
/* 139 */           this.diagnostics = new ArrayList(entrySet.size());
/* 140 */           for (Map.Entry<String, Object> entry : entrySet)
/*     */           {
/* 142 */             this.diagnostics.add(new P8CE_Diagnostic((String)entry.getKey(), entry.getValue()));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMErrorRecord.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */