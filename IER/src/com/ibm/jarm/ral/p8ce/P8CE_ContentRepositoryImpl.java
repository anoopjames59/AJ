/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.admin.PropertyDefinition;
/*     */ import com.filenet.api.constants.TypeID;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.ContentRepository;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_ContentRepositoryImpl
/*     */   extends P8CE_RepositoryImpl
/*     */   implements ContentRepository, JaceBasable
/*     */ {
/*  31 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  32 */   private static final IGenerator<ContentRepository> ContentRepositoryGenerator = new Generator();
/*     */   
/*     */ 
/*  35 */   private static final Map<String, Boolean> ValidContentRepositoryCache = Collections.synchronizedMap(new HashMap());
/*     */   
/*     */ 
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  40 */     return P8CE_RepositoryImpl.getMandatoryPropertyNames();
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  45 */     return P8CE_RepositoryImpl.MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<ContentRepository> getContentRepositoryGenerator()
/*     */   {
/*  55 */     return ContentRepositoryGenerator;
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
/*     */   public P8CE_ContentRepositoryImpl(RMDomain domain, String identity, ObjectStore jaceObjectStore, boolean performValidation, boolean isPlaceholder)
/*     */   {
/*  71 */     super(domain, identity, jaceObjectStore, isPlaceholder);
/*  72 */     Tracer.traceMethodEntry(new Object[] { domain, identity, jaceObjectStore, Boolean.valueOf(performValidation), Boolean.valueOf(isPlaceholder) });
/*     */     
/*  74 */     if ((performValidation) && (!isValidROS(jaceObjectStore)))
/*     */     {
/*  76 */       String osIdent = P8CE_Util.getJaceObjectIdentity(jaceObjectStore);
/*  77 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_INVALID_CONTENT_OBJSTORE, new Object[] { osIdent });
/*     */     }
/*     */     
/*  80 */     this.entityType = EntityType.ContentRepository;
/*  81 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isValidROS(ObjectStore jaceObjStore)
/*     */   {
/*  92 */     Tracer.traceMethodEntry(new Object[] { jaceObjStore });
/*  93 */     boolean result = false;
/*     */     
/*     */ 
/*  96 */     String objStoreIdStr = jaceObjStore.get_Id().toString();
/*  97 */     Boolean validationState = (Boolean)ValidContentRepositoryCache.get(objStoreIdStr);
/*  98 */     if (Boolean.TRUE.equals(validationState))
/*     */     {
/* 100 */       result = true;
/*     */     }
/*     */     else
/*     */     {
/* 104 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 107 */         establishedSubject = P8CE_Util.associateSubject();
/*     */         
/*     */ 
/* 110 */         ClassDefinition documentClassDef = P8CE_Util.fetchJaceClassDefinition(jaceObjStore, "Document", ClassDefinitionPF);
/* 111 */         if (documentClassDef != null)
/*     */         {
/* 113 */           PropertyDefinition jaceRecInfoPropDef = P8CE_Util.getPropertyDefinition(documentClassDef, "RecordInformation");
/* 114 */           if ((jaceRecInfoPropDef != null) && (jaceRecInfoPropDef.get_DataType() == TypeID.OBJECT)) {
/* 115 */             result = true;
/*     */           }
/*     */         }
/* 118 */         ValidContentRepositoryCache.put(objStoreIdStr, Boolean.valueOf(result));
/*     */       }
/*     */       finally
/*     */       {
/* 122 */         if (establishedSubject) {
/* 123 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/* 127 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 128 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 137 */     return toString("P8CE_ContentRepositoryImpl");
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
/*     */     implements IGenerator<ContentRepository>
/*     */   {
/*     */     public ContentRepository create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 152 */       P8CE_ContentRepositoryImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 153 */       ObjectStore jaceObjStore = (ObjectStore)jaceBaseObject;
/*     */       
/* 155 */       Domain jaceDomain = P8CE_Util.getJaceDomain(jaceObjStore);
/* 156 */       RMDomain rmDomain = (RMDomain)P8CE_RMDomainImpl.getGenerator().create(null, jaceDomain);
/* 157 */       String reposIdentity = jaceObjStore.get_Id().toString();
/* 158 */       ContentRepository result = new P8CE_ContentRepositoryImpl(rmDomain, reposIdentity, jaceObjStore, false, false);
/*     */       
/* 160 */       P8CE_ContentRepositoryImpl.Tracer.traceMethodExit(new Object[] { result });
/* 161 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_ContentRepositoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */