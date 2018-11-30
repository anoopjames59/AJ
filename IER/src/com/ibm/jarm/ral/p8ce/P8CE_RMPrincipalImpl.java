/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.security.RMPrincipal;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class P8CE_RMPrincipalImpl
/*     */   implements RMPrincipal, JaceBasable
/*     */ {
/*  21 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  31 */   protected static final String[] MandatoryPropertyNames = { "DisplayName", "DistinguishedName", "Id", "Name", "ShortName" };
/*     */   
/*     */   protected static final FilterElement MandatoryJaceFE;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  38 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  39 */     MandatoryJaceFE = new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null);
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
/*     */   public EntityType getEntityType()
/*     */   {
/*  52 */     return EntityType.SecurityPrincipal;
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
/*     */   public String getObjectIdentity()
/*     */   {
/*  65 */     Tracer.traceMethodEntry(new Object[0]);
/*  66 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/*  67 */     Tracer.traceMethodExit(new Object[] { result });
/*  68 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  76 */     Tracer.traceMethodEntry(new Object[0]);
/*  77 */     String result = P8CE_Util.getJaceObjectClassName(getJaceBaseObject());
/*  78 */     Tracer.traceMethodExit(new Object[] { result });
/*  79 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/*  87 */     Tracer.traceMethodEntry(new Object[0]);
/*  88 */     String result = P8CE_Util.getJacePropertyAsString(this, "DisplayName");
/*  89 */     Tracer.traceMethodExit(new Object[] { result });
/*  90 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDistinguishedName()
/*     */   {
/*  98 */     Tracer.traceMethodEntry(new Object[0]);
/*  99 */     String result = P8CE_Util.getJacePropertyAsString(this, "DistinguishedName");
/* 100 */     Tracer.traceMethodExit(new Object[] { result });
/* 101 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 109 */     Tracer.traceMethodEntry(new Object[0]);
/* 110 */     String result = P8CE_Util.getJacePropertyAsString(this, "Id");
/* 111 */     Tracer.traceMethodExit(new Object[] { result });
/* 112 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 120 */     Tracer.traceMethodEntry(new Object[0]);
/* 121 */     String result = P8CE_Util.getJacePropertyAsString(this, "Name");
/* 122 */     Tracer.traceMethodExit(new Object[] { result });
/* 123 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShortName()
/*     */   {
/* 131 */     Tracer.traceMethodEntry(new Object[0]);
/* 132 */     String result = P8CE_Util.getJacePropertyAsString(this, "ShortName");
/* 133 */     Tracer.traceMethodExit(new Object[] { result });
/* 134 */     return result;
/*     */   }
/*     */   
/*     */   public abstract List<FilterElement> getMandatoryFEs();
/*     */   
/*     */   public abstract EngineObject getJaceBaseObject();
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMPrincipalImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */