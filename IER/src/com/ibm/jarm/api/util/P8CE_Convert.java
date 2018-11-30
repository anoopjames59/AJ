/*     */ package com.ibm.jarm.api.util;
/*     */ 
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.ContentItem;
/*     */ import com.ibm.jarm.api.core.ContentRepository;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.ral.p8ce.IGenerator;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_BaseContainerImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_ContentItemImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_ContentRepositoryImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_DomainConnectionImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_FilePlanRepositoryImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_RMDomainImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_RepositoryImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_Convert
/*     */ {
/*  37 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
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
/*     */   public static DomainConnection fromP8CE(Connection p8CEConnection)
/*     */   {
/*  54 */     Tracer.traceMethodEntry(new Object[] { p8CEConnection });
/*  55 */     Util.ckNullObjParam("p8CEConnection", p8CEConnection);
/*  56 */     DomainConnection result = new P8CE_DomainConnectionImpl(p8CEConnection);
/*  57 */     Tracer.traceMethodExit(new Object[] { result });
/*  58 */     return result;
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
/*     */   public static Connection fromJARM(DomainConnection jarmDomainConnection)
/*     */   {
/*  73 */     Tracer.traceMethodEntry(new Object[] { jarmDomainConnection });
/*  74 */     Util.ckNullObjParam("jarmDomainConnection", jarmDomainConnection);
/*     */     
/*  76 */     Connection result = null;
/*  77 */     if ((jarmDomainConnection instanceof P8CE_DomainConnectionImpl)) {
/*  78 */       result = ((P8CE_DomainConnectionImpl)jarmDomainConnection).getJaceConnection();
/*     */     }
/*  80 */     Tracer.traceMethodExit(new Object[] { result });
/*  81 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RMDomain fromP8CE(Domain p8CEDomain)
/*     */   {
/* 100 */     Tracer.traceMethodEntry(new Object[] { p8CEDomain });
/* 101 */     Util.ckNullObjParam("p8CEDomain", p8CEDomain);
/*     */     
/* 103 */     if ((!p8CEDomain.getObjectReference().isResolved()) || (!p8CEDomain.getProperties().isPropertyPresent("Name")))
/*     */     {
/*     */ 
/*     */ 
/* 107 */       PropertyFilter jacePF = new PropertyFilter();
/* 108 */       for (FilterElement jaceFE : P8CE_RMDomainImpl.getMandatoryJaceFEs())
/*     */       {
/* 110 */         jacePF.addIncludeProperty(jaceFE);
/*     */       }
/*     */       
/* 113 */       p8CEDomain.fetchProperties(jacePF);
/*     */     }
/*     */     
/* 116 */     RMDomain result = (RMDomain)P8CE_RMDomainImpl.getGenerator().create(null, p8CEDomain);
/* 117 */     Tracer.traceMethodExit(new Object[] { result });
/* 118 */     return result;
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
/*     */   public static Domain fromJARM(RMDomain jarmRMDomain)
/*     */   {
/* 133 */     Tracer.traceMethodEntry(new Object[] { jarmRMDomain });
/* 134 */     Util.ckNullObjParam("jarmRMDomain", jarmRMDomain);
/*     */     
/* 136 */     Domain result = null;
/* 137 */     if ((jarmRMDomain instanceof P8CE_RMDomainImpl)) {
/* 138 */       result = (Domain)((P8CE_RMDomainImpl)jarmRMDomain).getJaceBaseObject();
/*     */     }
/* 140 */     Tracer.traceMethodExit(new Object[] { result });
/* 141 */     return result;
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
/*     */   public static ObjectStore fromJARM(Repository jarmRepository)
/*     */   {
/* 157 */     Tracer.traceMethodEntry(new Object[] { jarmRepository });
/* 158 */     Util.ckNullObjParam("jarmRepository", jarmRepository);
/*     */     
/* 160 */     ObjectStore result = null;
/* 161 */     if ((jarmRepository instanceof P8CE_RepositoryImpl)) {
/* 162 */       result = ((P8CE_RepositoryImpl)jarmRepository).getJaceObjectStore();
/*     */     }
/* 164 */     Tracer.traceMethodExit(new Object[] { result });
/* 165 */     return result;
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
/*     */   public static Repository fromP8CE(ObjectStore p8CEObjectStore)
/*     */   {
/* 179 */     Tracer.traceMethodEntry(new Object[] { p8CEObjectStore });
/* 180 */     Util.ckNullObjParam("p8CEObjectStore", p8CEObjectStore);
/*     */     
/* 182 */     if ((!p8CEObjectStore.getProperties().isPropertyPresent("Domain")) || (!p8CEObjectStore.getProperties().isPropertyPresent("Id")))
/*     */     {
/*     */ 
/* 185 */       p8CEObjectStore.refresh(new String[] { "Id", "Domain" });
/*     */     }
/* 187 */     RMDomain rmDom = fromP8CE(p8CEObjectStore.get_Domain());
/* 188 */     String reposIdent = p8CEObjectStore.get_Id().toString();
/* 189 */     Repository result = new P8CE_RepositoryImpl(rmDom, reposIdent, p8CEObjectStore, true);
/*     */     
/* 191 */     boolean performValidation = false;
/* 192 */     boolean isAPlaceHolder = false;
/* 193 */     if (P8CE_FilePlanRepositoryImpl.isValidFPOS(p8CEObjectStore))
/*     */     {
/* 195 */       result = new P8CE_FilePlanRepositoryImpl(rmDom, reposIdent, p8CEObjectStore, performValidation, isAPlaceHolder);
/*     */     }
/* 197 */     else if (P8CE_ContentRepositoryImpl.isValidROS(p8CEObjectStore))
/*     */     {
/* 199 */       result = new P8CE_ContentRepositoryImpl(rmDom, reposIdent, p8CEObjectStore, performValidation, isAPlaceHolder);
/*     */     }
/*     */     else
/*     */     {
/* 203 */       result = new P8CE_RepositoryImpl(rmDom, reposIdent, p8CEObjectStore, isAPlaceHolder);
/*     */     }
/*     */     
/* 206 */     if (result != null) {
/* 207 */       result.refresh(RMPropertyFilter.MinimumPropertySet);
/*     */     }
/* 209 */     Tracer.traceMethodExit(new Object[] { result });
/* 210 */     return result;
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
/*     */   public static Folder fromJARM(Container jarmContainer)
/*     */   {
/* 226 */     Tracer.traceMethodEntry(new Object[] { jarmContainer });
/* 227 */     Util.ckNullObjParam("jarmContainer", jarmContainer);
/*     */     
/* 229 */     Folder result = null;
/* 230 */     if ((jarmContainer instanceof P8CE_BaseContainerImpl))
/*     */     {
/* 232 */       result = (Folder)((P8CE_BaseContainerImpl)jarmContainer).getJaceBaseObject();
/*     */     }
/*     */     
/* 235 */     Tracer.traceMethodExit(new Object[] { result });
/* 236 */     return result;
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
/*     */   public static Container fromP8CE(Folder p8CEFolder)
/*     */   {
/* 250 */     Tracer.traceMethodEntry(new Object[] { p8CEFolder });
/* 251 */     Util.ckNullObjParam("p8CEFolder", p8CEFolder);
/*     */     
/* 253 */     ObjectStore jaceObjStore = p8CEFolder.getObjectStore();
/* 254 */     Repository jarmRepos = fromP8CE(jaceObjStore);
/* 255 */     Container result = (Container)P8CE_Util.convertJaceObjToJarmObject(jarmRepos, p8CEFolder);
/*     */     
/* 257 */     Tracer.traceMethodExit(new Object[] { result });
/* 258 */     return result;
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
/*     */   public static Document fromJARM(ContentItem jarmContentItem)
/*     */   {
/* 274 */     Tracer.traceMethodEntry(new Object[] { jarmContentItem });
/* 275 */     Util.ckNullObjParam("jarmContentItem", jarmContentItem);
/*     */     
/* 277 */     Document result = null;
/* 278 */     if ((jarmContentItem instanceof P8CE_ContentItemImpl))
/*     */     {
/* 280 */       result = (Document)((P8CE_ContentItemImpl)jarmContentItem).getJaceBaseObject();
/*     */     }
/*     */     
/* 283 */     Tracer.traceMethodExit(new Object[] { result });
/* 284 */     return result;
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
/*     */   public static ContentItem fromP8CE(Document p8CEDocument)
/*     */   {
/* 298 */     Tracer.traceMethodEntry(new Object[] { p8CEDocument });
/* 299 */     Util.ckNullObjParam("p8CEDocument", p8CEDocument);
/*     */     
/* 301 */     ObjectStore jaceObjStore = p8CEDocument.getObjectStore();
/* 302 */     if ((!jaceObjStore.getProperties().isPropertyPresent("Domain")) || (!jaceObjStore.getProperties().isPropertyPresent("Id")))
/*     */     {
/*     */ 
/* 305 */       jaceObjStore.refresh(new String[] { "Id", "Domain" });
/*     */     }
/* 307 */     RMDomain jarmDomain = fromP8CE(jaceObjStore.get_Domain());
/* 308 */     String reposIdent = jaceObjStore.getObjectReference().getObjectIdentity();
/*     */     
/* 310 */     boolean performValidation = true;
/* 311 */     boolean isPlaceholder = false;
/* 312 */     ContentRepository jarmRepository = new P8CE_ContentRepositoryImpl(jarmDomain, reposIdent, jaceObjStore, performValidation, isPlaceholder);
/*     */     
/* 314 */     String docIdent = p8CEDocument.getObjectReference().getObjectIdentity();
/* 315 */     ContentItem result = new P8CE_ContentItemImpl(EntityType.ContentItem, jarmRepository, docIdent, p8CEDocument, false);
/*     */     
/*     */ 
/* 318 */     Tracer.traceMethodExit(new Object[] { result });
/* 319 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\P8CE_Convert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */