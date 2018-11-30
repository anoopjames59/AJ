/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.GroupSet;
/*     */ import com.filenet.api.collection.UserSet;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.security.Group;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.security.RMGroup;
/*     */ import com.ibm.jarm.api.security.RMUser;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMGroupImpl
/*     */   extends P8CE_RMPrincipalImpl
/*     */   implements RMGroup
/*     */ {
/*  32 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  33 */   private static final IGenerator<RMGroup> RMGroupGenerator = new Generator();
/*     */   
/*     */   static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   private Group jaceGroup;
/*     */   
/*     */   static
/*     */   {
/*  41 */     List<FilterElement> tempList = new ArrayList(2);
/*  42 */     tempList.add(P8CE_RMPrincipalImpl.MandatoryJaceFE);
/*  43 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  48 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RMGroup> getGenerator()
/*     */   {
/*  58 */     return RMGroupGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_RMGroupImpl(Group jaceGroup)
/*     */   {
/*  66 */     Tracer.traceMethodEntry(new Object[] { jaceGroup });
/*  67 */     this.jaceGroup = jaceGroup;
/*  68 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/*  78 */     return this.jaceGroup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  87 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<RMGroup> getGroupMembers()
/*     */   {
/*  95 */     Tracer.traceMethodEntry(new Object[0]);
/*  96 */     PageableSet<RMGroup> result = null;
/*     */     
/*  98 */     fetchPropertyIfNecessary("Groups", MandatoryJaceFEs);
/*     */     
/* 100 */     if (this.jaceGroup.getProperties().isPropertyPresent("Groups"))
/*     */     {
/* 102 */       GroupSet jaceGroupSet = this.jaceGroup.get_Groups();
/*     */       
/*     */ 
/* 105 */       boolean supportsPaging = true;
/* 106 */       IGenerator<RMGroup> generator = getGenerator();
/* 107 */       result = new P8CE_PageableSetImpl(null, jaceGroupSet, supportsPaging, generator);
/*     */     }
/*     */     
/* 110 */     Tracer.traceMethodExit(new Object[] { result });
/* 111 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<RMGroup> getGroupMemberships()
/*     */   {
/* 119 */     Tracer.traceMethodEntry(new Object[0]);
/* 120 */     PageableSet<RMGroup> result = null;
/*     */     
/* 122 */     fetchPropertyIfNecessary("MemberOfGroups", MandatoryJaceFEs);
/*     */     
/* 124 */     if (this.jaceGroup.getProperties().isPropertyPresent("MemberOfGroups"))
/*     */     {
/* 126 */       GroupSet jaceGroupSet = this.jaceGroup.get_MemberOfGroups();
/*     */       
/*     */ 
/* 129 */       boolean supportsPaging = true;
/* 130 */       IGenerator<RMGroup> generator = getGenerator();
/* 131 */       result = new P8CE_PageableSetImpl(null, jaceGroupSet, supportsPaging, generator);
/*     */     }
/*     */     
/* 134 */     Tracer.traceMethodExit(new Object[] { result });
/* 135 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<RMUser> getUserMembers()
/*     */   {
/* 143 */     Tracer.traceMethodEntry(new Object[0]);
/* 144 */     PageableSet<RMUser> result = null;
/*     */     
/* 146 */     fetchPropertyIfNecessary("Users", P8CE_RMUserImpl.MandatoryJaceFEs);
/*     */     
/* 148 */     if (this.jaceGroup.getProperties().isPropertyPresent("Users"))
/*     */     {
/* 150 */       UserSet jaceUserSet = this.jaceGroup.get_Users();
/*     */       
/*     */ 
/* 153 */       boolean supportsPaging = true;
/* 154 */       IGenerator<RMUser> generator = P8CE_RMUserImpl.getGenerator();
/* 155 */       result = new P8CE_PageableSetImpl(null, jaceUserSet, supportsPaging, generator);
/*     */     }
/*     */     
/* 158 */     Tracer.traceMethodExit(new Object[] { result });
/* 159 */     return result;
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
/*     */   private void fetchPropertyIfNecessary(String propSymName, List<FilterElement> mandatoryJaceFEs)
/*     */   {
/* 172 */     Tracer.traceMethodEntry(new Object[] { propSymName, mandatoryJaceFEs });
/* 173 */     if (!this.jaceGroup.getProperties().isPropertyPresent(propSymName))
/*     */     {
/* 175 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 178 */         establishedSubject = P8CE_Util.associateSubject();
/* 179 */         PropertyFilter pf = new PropertyFilter();
/* 180 */         pf.addIncludeProperty(1, null, Boolean.FALSE, propSymName, null);
/* 181 */         for (FilterElement fe : mandatoryJaceFEs)
/*     */         {
/* 183 */           pf.addIncludeProperty(fe);
/*     */         }
/*     */         
/* 186 */         P8CE_Util.fetchAdditionalJaceProperties(this.jaceGroup, pf);
/* 187 */         Tracer.traceMethodExit(new Object[0]);
/*     */       }
/*     */       catch (RMRuntimeException ex)
/*     */       {
/* 191 */         throw ex;
/*     */       }
/*     */       catch (Exception cause)
/*     */       {
/* 195 */         throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { getName(), EntityType.SecurityPrincipal });
/*     */       }
/*     */       finally
/*     */       {
/* 199 */         if (establishedSubject) {
/* 200 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RMGroup>
/*     */   {
/*     */     public RMGroup create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 217 */       P8CE_RMGroupImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 218 */       Group jaceGroup = (Group)jaceBaseObject;
/* 219 */       RMGroup result = new P8CE_RMGroupImpl(jaceGroup);
/*     */       
/* 221 */       P8CE_RMGroupImpl.Tracer.traceMethodExit(new Object[] { result });
/* 222 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMGroupImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */