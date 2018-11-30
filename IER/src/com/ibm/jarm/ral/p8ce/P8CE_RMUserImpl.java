/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.GroupSet;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.security.User;
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
/*     */ class P8CE_RMUserImpl
/*     */   extends P8CE_RMPrincipalImpl
/*     */   implements RMUser
/*     */ {
/*  31 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  32 */   private static final IGenerator<RMUser> RMUserGenerator = new Generator();
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
/*  43 */   static final String[] MandatoryPropertyNames = { "Email" };
/*     */   static final List<FilterElement> MandatoryJaceFEs;
/*     */   private User jaceUser;
/*     */   
/*     */   static {
/*  48 */     List<FilterElement> tempList = new ArrayList(2);
/*  49 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  50 */     tempList.add(P8CE_RMPrincipalImpl.MandatoryJaceFE);
/*  51 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null));
/*  52 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  57 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RMUser> getGenerator()
/*     */   {
/*  67 */     return RMUserGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_RMUserImpl(User jaceUser)
/*     */   {
/*  75 */     Tracer.traceMethodEntry(new Object[] { jaceUser });
/*  76 */     this.jaceUser = jaceUser;
/*  77 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineObject getJaceBaseObject()
/*     */   {
/*  87 */     return this.jaceUser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  96 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEmailName()
/*     */   {
/* 104 */     Tracer.traceMethodEntry(new Object[0]);
/* 105 */     String result = P8CE_Util.getJacePropertyAsString(this, "Email");
/* 106 */     Tracer.traceMethodExit(new Object[] { result });
/* 107 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PageableSet<RMGroup> getGroupMemberships()
/*     */   {
/* 115 */     Tracer.traceMethodEntry(new Object[0]);
/* 116 */     PageableSet<RMGroup> result = null;
/*     */     
/* 118 */     if (!this.jaceUser.getProperties().isPropertyPresent("MemberOfGroups"))
/*     */     {
/* 120 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 123 */         establishedSubject = P8CE_Util.associateSubject();
/* 124 */         PropertyFilter pf = new PropertyFilter();
/* 125 */         pf.addIncludeProperty(1, null, Boolean.FALSE, "MemberOfGroups", null);
/* 126 */         List<FilterElement> groupMandatoryFEs = P8CE_RMGroupImpl.getMandatoryJaceFEs();
/* 127 */         for (FilterElement fe : groupMandatoryFEs)
/*     */         {
/* 129 */           pf.addIncludeProperty(fe);
/*     */         }
/*     */         
/* 132 */         P8CE_Util.fetchAdditionalJaceProperties(this.jaceUser, pf);
/*     */       }
/*     */       catch (RMRuntimeException ex)
/*     */       {
/* 136 */         throw ex;
/*     */       }
/*     */       catch (Exception cause)
/*     */       {
/* 140 */         throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_BASEOBJECT_UNAVAILABLE, new Object[] { getName(), EntityType.SecurityPrincipal });
/*     */       }
/*     */       finally
/*     */       {
/* 144 */         if (establishedSubject) {
/* 145 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/* 149 */     if (this.jaceUser.getProperties().isPropertyPresent("MemberOfGroups"))
/*     */     {
/* 151 */       GroupSet jaceGroupSet = this.jaceUser.get_MemberOfGroups();
/*     */       
/*     */ 
/* 154 */       boolean supportsPaging = true;
/* 155 */       IGenerator<RMGroup> generator = P8CE_RMGroupImpl.getGenerator();
/* 156 */       result = new P8CE_PageableSetImpl(null, jaceGroupSet, supportsPaging, generator);
/*     */     }
/*     */     
/* 159 */     Tracer.traceMethodExit(new Object[] { result });
/* 160 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RMUser>
/*     */   {
/*     */     public RMUser create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 175 */       P8CE_RMUserImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 176 */       User jaceUser = (User)jaceBaseObject;
/* 177 */       RMUser result = new P8CE_RMUserImpl(jaceUser);
/*     */       
/* 179 */       P8CE_RMUserImpl.Tracer.traceMethodExit(new Object[] { result });
/* 180 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMUserImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */