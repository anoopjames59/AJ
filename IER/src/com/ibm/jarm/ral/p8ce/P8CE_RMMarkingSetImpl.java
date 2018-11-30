/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.MarkingList;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.security.Marking;
/*     */ import com.filenet.api.security.MarkingSet;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMMarkingItem;
/*     */ import com.ibm.jarm.api.meta.RMMarkingSet;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMMarkingSetImpl
/*     */   extends RALBaseEntity
/*     */   implements RMMarkingSet
/*     */ {
/*  38 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  39 */   private static final IGenerator<RMMarkingSet> RMMarkingSetGenerator = new Generator();
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
/*  50 */   static final String[] MandatoryPropertyNames = { "Id", "DisplayName", "DescriptiveText", "IsHierarchical", "Markings" };
/*     */   
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   private MarkingSet jaceMarkingSet;
/*     */   
/*     */   static
/*     */   {
/*  57 */     List<FilterElement> tempList = new ArrayList(2);
/*     */     
/*  59 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  60 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null));
/*     */     
/*  62 */     mandatoryNames = P8CE_Util.createSpaceSeparatedString(P8CE_RMMarkingItemImpl.MandatoryPropertyNames);
/*  63 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null));
/*  64 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  69 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RMMarkingSet> getGenerator()
/*     */   {
/*  79 */     return RMMarkingSetGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_RMMarkingSetImpl(MarkingSet jaceMarkingSet)
/*     */   {
/*  87 */     super(EntityType.MarkingSet, (Repository)null, jaceMarkingSet.get_Id().toString(), false);
/*  88 */     Tracer.traceMethodEntry(new Object[] { jaceMarkingSet });
/*  89 */     this.jaceMarkingSet = jaceMarkingSet;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     this.jaceMarkingSet.get_Markings();
/*     */     
/*  98 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescriptiveText()
/*     */   {
/* 106 */     return this.jaceMarkingSet.get_DescriptiveText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 114 */     return this.jaceMarkingSet.get_DisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 122 */     Tracer.traceMethodEntry(new Object[0]);
/* 123 */     String result = null;
/* 124 */     Id markingSetId = this.jaceMarkingSet.get_Id();
/* 125 */     if (markingSetId != null) {
/* 126 */       result = markingSetId.toString();
/*     */     }
/* 128 */     Tracer.traceMethodExit(new Object[] { result });
/* 129 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMMarkingItem> getMarkings()
/*     */   {
/* 138 */     Tracer.traceMethodEntry(new Object[0]);
/* 139 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 142 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*     */ 
/*     */ 
/* 146 */       this.jaceMarkingSet.refresh();
/*     */       
/* 148 */       List<RMMarkingItem> result = null;
/* 149 */       MarkingList jaceMarkingList = this.jaceMarkingSet.get_Markings();
/* 150 */       Marking jaceMarking; if (jaceMarkingList != null)
/*     */       {
/* 152 */         jaceMarking = null;
/* 153 */         result = new ArrayList();
/* 154 */         Iterator<Marking> it = jaceMarkingList.iterator();
/* 155 */         while ((it != null) && (it.hasNext()))
/*     */         {
/* 157 */           jaceMarking = (Marking)it.next();
/* 158 */           if (jaceMarking != null) {
/* 159 */             result.add(new P8CE_RMMarkingItemImpl(jaceMarking));
/*     */           }
/*     */         }
/*     */       }
/* 163 */       Tracer.traceMethodExit(new Object[] { result });
/* 164 */       return result;
/*     */     }
/*     */     finally
/*     */     {
/* 168 */       if (establishedSubject) {
/* 169 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isHierarchical()
/*     */   {
/* 178 */     Tracer.traceMethodEntry(new Object[0]);
/* 179 */     Boolean jaceIsHier = this.jaceMarkingSet.get_IsHierarchical();
/* 180 */     boolean result = jaceIsHier != null ? jaceIsHier.booleanValue() : false;
/* 181 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 182 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 191 */     return "MarkingSet";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 200 */     Tracer.traceMethodEntry(new Object[0]);
/* 201 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceMarkingSet);
/* 202 */     Tracer.traceMethodExit(new Object[] { result });
/* 203 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 212 */     Tracer.traceMethodEntry(new Object[0]);
/* 213 */     List<Container> result = Collections.EMPTY_LIST;
/* 214 */     Tracer.traceMethodExit(new Object[] { result });
/* 215 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 223 */     return this.jaceMarkingSet.get_DisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 232 */     Tracer.traceMethodEntry(new Object[0]);
/* 233 */     List<RMPermission> result = Collections.EMPTY_LIST;
/* 234 */     Tracer.traceMethodExit(new Object[] { result });
/* 235 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 243 */     Tracer.traceMethodEntry(new Object[0]);
/* 244 */     RMProperties result = null;
/* 245 */     if (this.jaceMarkingSet != null)
/*     */     {
/* 247 */       result = new P8CE_RMPropertiesImpl(this.jaceMarkingSet, this);
/*     */     }
/*     */     
/* 250 */     Tracer.traceMethodExit(new Object[] { result });
/* 251 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 259 */     Tracer.traceMethodEntry(new Object[0]);
/* 260 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "refresh", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter jarmFilter)
/*     */   {
/* 268 */     Tracer.traceMethodEntry(new Object[] { jarmFilter });
/* 269 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "refresh", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 277 */     Tracer.traceMethodEntry(new Object[] { symbolicPropertyNames });
/* 278 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "refresh", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 286 */     return this.jaceMarkingSet.getAccessAllowed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 294 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 302 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 303 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "internalSave", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RMMarkingSet>
/*     */   {
/*     */     public RMMarkingSet create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 317 */       P8CE_RMMarkingSetImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/*     */       
/* 319 */       MarkingSet jaceMarkingSet = (MarkingSet)jaceBaseObject;
/* 320 */       RMMarkingSet result = new P8CE_RMMarkingSetImpl(jaceMarkingSet);
/*     */       
/* 322 */       P8CE_RMMarkingSetImpl.Tracer.traceMethodExit(new Object[] { result });
/* 323 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMMarkingSetImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */