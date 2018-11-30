/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.apiimpl.util.ExportXML;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.jarm.api.core.RecordCategory;
/*     */ import com.ibm.jarm.api.core.RecordContainer;
/*     */ import com.ibm.jarm.api.core.RecordFolder;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.io.StringReader;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.InputSource;
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
/*     */ class P8CE_XMLExportSupport
/*     */ {
/*  51 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  52 */   private static final SimpleDateFormat TimeStampFormatter = new SimpleDateFormat("yyyyMMddhhmmssS");
/*     */   
/*     */ 
/*  55 */   private static Map<EntityType, String> ExportClassFilters = new HashMap();
/*     */   
/*     */ 
/*  58 */   private static Set<String> ExcludedPropSymNames = buildPropExcludeSet();
/*     */   
/*  60 */   private static String CustomObjsClassFilterFragments = null;
/*     */   
/*     */   public static Document doJaceXMLExport(Repository jarmRepository, IndependentObject jaceEntity, String exportFilterStr, int exportFlags)
/*     */   {
/*  64 */     Tracer.traceMethodEntry(new Object[] { jaceEntity, exportFilterStr });
/*     */     
/*  66 */     Element exportFilterNode = null;
/*  67 */     Document exportXMLDOMDoc = null;
/*  68 */     Element exportXMLNode = null;
/*     */     try
/*     */     {
/*  71 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */       
/*  73 */       if ((exportFilterStr != null) && (exportFilterStr.trim().length() > 0))
/*     */       {
/*  75 */         DocumentBuilder parser = null;
/*  76 */         parser = factory.newDocumentBuilder();
/*  77 */         InputSource inputStream = new InputSource(new StringReader(exportFilterStr));
/*  78 */         Document exportFilterDOMDoc = parser.parse(inputStream);
/*  79 */         exportFilterNode = exportFilterDOMDoc.getDocumentElement();
/*     */       }
/*     */       
/*     */ 
/*  83 */       exportXMLDOMDoc = factory.newDocumentBuilder().newDocument();
/*  84 */       exportXMLNode = exportXMLDOMDoc.createElement("ObjectManifest");
/*  85 */       exportXMLDOMDoc.appendChild(exportXMLNode);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  89 */       throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_EXPORT_XMLDOM_ERROR, new Object[] { jaceEntity.getObjectReference().getObjectIdentity() });
/*     */     }
/*     */     
/*     */ 
/*  93 */     jaceEntity.refresh((PropertyFilter)null);
/*  94 */     RMDomain jarmDomain = jarmRepository.getDomain();
/*  95 */     Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/*  96 */     Integer exportFlagsObj = Integer.valueOf(exportFlags);
/*     */     
/*     */     try
/*     */     {
/* 100 */       String contentRefParam = null;
/* 101 */       if ((exportFlagsObj.intValue() & 0x20) != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 106 */         contentRefParam = "C:\\Temp\\";
/*     */       }
/*     */       
/*     */ 
/* 110 */       ExportXML.exportObject(jaceDomain, jaceEntity, exportXMLNode, exportFlagsObj, contentRefParam, exportFilterNode);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 114 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_EXPORT_P8CE_XMLGENERATION_FAILURE, new Object[] { jaceEntity.getObjectReference().getObjectIdentity() });
/*     */     }
/*     */     
/* 117 */     Tracer.traceMethodExit(new Object[] { exportXMLDOMDoc });
/* 118 */     return exportXMLDOMDoc;
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
/*     */   public static String getContainerExportClassFilter(RecordContainer container)
/*     */   {
/* 132 */     Tracer.traceMethodEntry(new Object[] { container });
/* 133 */     Repository repository = container.getRepository();
/*     */     
/* 135 */     boolean isARecordCategory = false;
/* 136 */     boolean isARecordFolder = false;
/* 137 */     if ((container instanceof RecordCategory)) {
/* 138 */       isARecordCategory = true;
/* 139 */     } else if ((container instanceof RecordFolder)) {
/* 140 */       isARecordFolder = true;
/*     */     }
/* 142 */     StringBuilder sb = new StringBuilder();
/* 143 */     sb.append("<ExportFilter>");
/* 144 */     sb.append(getExportClassFilterFragment(repository, "RecordInfo", EntityType.PhysicalRecord));
/* 145 */     sb.append(getExportClassFilterFragment(repository, "Volume", EntityType.RecordVolume));
/*     */     
/* 147 */     if ((isARecordCategory) || (isARecordFolder)) {
/* 148 */       sb.append(getExportClassFilterFragment(repository, "RecordFolder", EntityType.PhysicalRecordFolder));
/*     */     }
/* 150 */     if (isARecordCategory) {
/* 151 */       sb.append(getExportClassFilterFragment(repository, "RecordCategory", EntityType.RecordCategory));
/*     */     }
/* 153 */     sb.append(getExportClassFilterFragmentsForCustomObjs());
/*     */     
/* 155 */     sb.append("</ExportFilter>");
/* 156 */     String result = sb.toString();
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
/*     */   public static synchronized String getRecordExportClassFilter(Record record)
/*     */   {
/* 172 */     Tracer.traceMethodEntry(new Object[] { record });
/*     */     
/* 174 */     String fragment = null;
/* 175 */     if (ExportClassFilters.containsKey(EntityType.ContentItem))
/*     */     {
/* 177 */       fragment = (String)ExportClassFilters.get(EntityType.ContentItem);
/*     */     }
/*     */     else
/*     */     {
/* 181 */       Repository repository = record.getRepository();
/*     */       
/* 183 */       StringBuilder sb2 = new StringBuilder();
/* 184 */       sb2.append("<ObjectFilter SymbolicName=\"").append("Document").append("\"");
/* 185 */       sb2.append(" OVPRecursion=\"0\" >");
/* 186 */       sb2.append("<IncludeProperties>");
/*     */       
/* 188 */       boolean allowFromCDCache = true;
/* 189 */       RMClassDescription jarmCD = P8CE_Util.getClassDescription(repository, "Document", allowFromCDCache);
/* 190 */       String propSymName; if (jarmCD != null)
/*     */       {
/* 192 */         propSymName = null;
/* 193 */         List<RMPropertyDescription> propDescs = jarmCD.getPropertyDescriptions();
/* 194 */         for (RMPropertyDescription propDesc : propDescs)
/*     */         {
/* 196 */           if (propDesc.getDataType() == DataType.Object)
/*     */           {
/* 198 */             propSymName = propDesc.getSymbolicName();
/* 199 */             sb2.append("<Property SymbolicName=\"").append(propSymName).append("\" />");
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 204 */       sb2.append("</IncludeProperties>");
/* 205 */       sb2.append("</ObjectFilter>");
/* 206 */       fragment = sb2.toString();
/*     */       
/* 208 */       ExportClassFilters.put(EntityType.ContentItem, fragment);
/*     */     }
/*     */     
/* 211 */     StringBuilder sb = new StringBuilder();
/* 212 */     sb.append("<ExportFilter>");
/* 213 */     sb.append(fragment);
/* 214 */     sb.append("</ExportFilter>");
/* 215 */     String result = sb.toString();
/*     */     
/* 217 */     Tracer.traceMethodExit(new Object[] { result });
/* 218 */     return result;
/*     */   }
/*     */   
/*     */   private static synchronized String getExportClassFilterFragmentsForCustomObjs()
/*     */   {
/* 223 */     Tracer.traceMethodEntry(new Object[0]);
/* 224 */     if (CustomObjsClassFilterFragments == null)
/*     */     {
/* 226 */       StringBuilder sb = new StringBuilder();
/* 227 */       sb.append("<ObjectFilter SymbolicName=\"DisposalSchedule\" OVPRecursion=\"5\" >");
/* 228 */       sb.append("<IncludeProperties>");
/* 229 */       sb.append("<Property SymbolicName=\"CutoffDisposalTrigger\" />");
/* 230 */       sb.append("<Property SymbolicName=\"CutoffAction\" />");
/* 231 */       sb.append("<Property SymbolicName=\"Phases\" />");
/* 232 */       sb.append("</IncludeProperties>");
/* 233 */       sb.append("</ObjectFilter>");
/*     */       
/* 235 */       sb.append("<ObjectFilter SymbolicName=\"Phase\" OVPRecursion=\"3\" >");
/* 236 */       sb.append("<IncludeProperties>");
/* 237 */       sb.append("<Property SymbolicName=\"AlternateRetentions\" />");
/* 238 */       sb.append("<Property SymbolicName=\"PhaseAction\" />");
/* 239 */       sb.append("</IncludeProperties>");
/* 240 */       sb.append("</ObjectFilter>");
/*     */       
/* 242 */       sb.append("<ObjectFilter SymbolicName=\"Action1\" OVPRecursion=\"1\" >");
/* 243 */       sb.append("<IncludeProperties>");
/* 244 */       sb.append("<Property SymbolicName=\"DefaultWorkflow\" />");
/* 245 */       sb.append("</IncludeProperties>");
/* 246 */       sb.append("</ObjectFilter>");
/*     */       
/* 248 */       sb.append("<ObjectFilter SymbolicName=\"RecordType\" OVPRecursion=\"1\" >");
/* 249 */       sb.append("<IncludeProperties>");
/* 250 */       sb.append("<Property SymbolicName=\"Containers\" />");
/* 251 */       sb.append("<Property SymbolicName=\"DisposalSchedule\" />");
/* 252 */       sb.append("</IncludeProperties>");
/* 253 */       sb.append("</ObjectFilter>");
/*     */       
/* 255 */       CustomObjsClassFilterFragments = sb.toString();
/*     */     }
/*     */     
/* 258 */     Tracer.traceMethodExit(new Object[] { CustomObjsClassFilterFragments });
/* 259 */     return CustomObjsClassFilterFragments;
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
/*     */   public static synchronized String getExportClassFilterFragment(Repository repository, String baseClassSymName, EntityType entityType)
/*     */   {
/* 278 */     Tracer.traceMethodEntry(new Object[] { entityType });
/* 279 */     String result = null;
/* 280 */     if (ExportClassFilters.containsKey(entityType))
/*     */     {
/* 282 */       result = (String)ExportClassFilters.get(entityType);
/*     */     }
/*     */     else
/*     */     {
/* 286 */       String className = P8CE_Util.getEntityClassName(entityType);
/* 287 */       boolean allowFromCDCache = true;
/* 288 */       RMClassDescription jarmCD = P8CE_Util.getClassDescription(repository, className, allowFromCDCache);
/* 289 */       if (jarmCD != null)
/*     */       {
/* 291 */         String propSymName = null;
/* 292 */         List<RMPropertyDescription> propDescs = jarmCD.getPropertyDescriptions();
/*     */         
/* 294 */         StringBuilder sb = new StringBuilder();
/* 295 */         sb.append("<ObjectFilter SymbolicName=\"").append(baseClassSymName).append("\"");
/* 296 */         sb.append(" OVPRecursion=\"100\" >");
/* 297 */         sb.append("<IncludeProperties>");
/*     */         
/* 299 */         for (RMPropertyDescription propDesc : propDescs)
/*     */         {
/* 301 */           if (propDesc.getDataType() == DataType.Object)
/*     */           {
/* 303 */             propSymName = propDesc.getSymbolicName();
/* 304 */             if ((!ExcludedPropSymNames.contains(propSymName.toUpperCase())) && ((!baseClassSymName.equalsIgnoreCase("Volume")) || (!propSymName.equalsIgnoreCase("SubFolders"))) && ((!baseClassSymName.equalsIgnoreCase("RecordInfo")) || (!propSymName.equalsIgnoreCase("RecordInformation"))))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */               sb.append("<Property SymbolicName=\"").append(propSymName).append("\"/>");
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 315 */         sb.append("</IncludeProperties>");
/* 316 */         sb.append("</ObjectFilter>");
/*     */         
/* 318 */         String newFilter = sb.toString();
/* 319 */         ExportClassFilters.put(entityType, newFilter);
/* 320 */         result = newFilter;
/*     */       }
/*     */     }
/*     */     
/* 324 */     Tracer.traceMethodExit(new Object[] { result });
/* 325 */     return result;
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
/*     */   public static String generateExportFileName(String part1, String part2, String part3)
/*     */   {
/* 339 */     Tracer.traceMethodEntry(new Object[] { part1, part2, part3 });
/*     */     
/* 341 */     String timeStamp = null;
/* 342 */     synchronized (TimeStampFormatter)
/*     */     {
/* 344 */       timeStamp = TimeStampFormatter.format(new Date());
/*     */     }
/*     */     
/* 347 */     StringBuilder sb = new StringBuilder();
/* 348 */     sb.append(part1).append(part2).append(part3).append(timeStamp).append(".xml");
/* 349 */     String result = sb.toString();
/*     */     
/* 351 */     Tracer.traceMethodExit(new Object[] { result });
/* 352 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Set<String> buildPropExcludeSet()
/*     */   {
/* 364 */     Tracer.traceMethodEntry(new Object[0]);
/* 365 */     Set<String> set = new HashSet();
/*     */     
/* 367 */     set.add("Annotations".toUpperCase());
/* 368 */     set.add("ClassDescription".toUpperCase());
/* 369 */     set.add("Containers".toUpperCase());
/* 370 */     set.add("ContentElements".toUpperCase());
/* 371 */     set.add("CurrentPhaseExportFormat".toUpperCase());
/* 372 */     set.add("CurrentVersion".toUpperCase());
/* 373 */     set.add("DependentDocuments".toUpperCase());
/* 374 */     set.add("DestinationDocuments".toUpperCase());
/* 375 */     set.add("DocumentLifecyclePolicy".toUpperCase());
/* 376 */     set.add("DocURI".toUpperCase());
/* 377 */     set.add("FoldersFiledIn".toUpperCase());
/* 378 */     set.add("Holds".toUpperCase());
/* 379 */     set.add("ObjectStore".toUpperCase());
/* 380 */     set.add("OwnerDocument".toUpperCase());
/* 381 */     set.add("Parent".toUpperCase());
/* 382 */     set.add("RecordedDocuments".toUpperCase());
/* 383 */     set.add("ReleasedVersion".toUpperCase());
/* 384 */     set.add("Reservation".toUpperCase());
/* 385 */     set.add("SecurityFolder".toUpperCase());
/* 386 */     set.add("SecurityParent".toUpperCase());
/* 387 */     set.add("SecurityPolicy".toUpperCase());
/* 388 */     set.add("SourceDocument".toUpperCase());
/* 389 */     set.add("StoragePolicies".toUpperCase());
/* 390 */     set.add("StoragePolicy".toUpperCase());
/* 391 */     set.add("THIS");
/* 392 */     set.add("VersionSeries".toUpperCase());
/* 393 */     set.add("Versions".toUpperCase());
/*     */     
/* 395 */     Tracer.traceMethodExit(new Object[] { set });
/* 396 */     return set;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_XMLExportSupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */