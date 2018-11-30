/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.ClassificationGuide;
/*     */ import com.ibm.jarm.api.core.ClassificationGuideSection;
/*     */ import com.ibm.jarm.api.core.ClassificationGuideTopic;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
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
/*     */ public class P8CE_ClassificationGuideImpl
/*     */   implements ClassificationGuide
/*     */ {
/*     */   public static final String CLASSIFICATTION_GUIDES_CONTAINERNAME = "Classification Guides";
/*  44 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
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
/*  55 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "GuideName", "RMEntityDescription", "DateIssued", "IssuedBy", "ApprovedBy" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   private Repository repository;
/*     */   private Folder jaceFolder;
/*     */   
/*     */   static {
/*  61 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  63 */     List<FilterElement> tempList = new ArrayList(1);
/*  64 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  65 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  73 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  78 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */   P8CE_ClassificationGuideImpl(Repository repository, Folder jaceFolder)
/*     */   {
/*  84 */     Tracer.traceMethodEntry(new Object[] { repository, jaceFolder });
/*  85 */     this.repository = repository;
/*  86 */     this.jaceFolder = jaceFolder;
/*  87 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getApprovedBy()
/*     */   {
/*  95 */     return this.jaceFolder.getProperties().getStringValue("ApprovedBy");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getDateIssued()
/*     */   {
/* 103 */     return this.jaceFolder.getProperties().getDateTimeValue("DateIssued");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 111 */     return this.jaceFolder.getProperties().getStringValue("RMEntityDescription");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityType getEntityType()
/*     */   {
/* 119 */     Integer rawInteger = this.jaceFolder.getProperties().getInteger32Value("RMEntityType");
/* 120 */     return rawInteger != null ? EntityType.getInstanceFromInt(rawInteger.intValue()) : EntityType.Unknown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGuideName()
/*     */   {
/* 128 */     return this.jaceFolder.getProperties().getStringValue("GuideName");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ClassificationGuideSection> getGuideSections()
/*     */   {
/* 136 */     Tracer.traceMethodEntry(new Object[0]);
/* 137 */     List<ClassificationGuideSection> results = fetchChildSections(this.repository, this.jaceFolder);
/*     */     
/* 139 */     Tracer.traceMethodExit(new Object[] { results });
/* 140 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ClassificationGuideTopic> getGuideTopics()
/*     */   {
/* 148 */     Tracer.traceMethodEntry(new Object[0]);
/* 149 */     List<ClassificationGuideTopic> results = fetchChildTopics(this.repository, this.jaceFolder);
/*     */     
/* 151 */     Tracer.traceMethodExit(new Object[] { results });
/* 152 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 160 */     Id rawId = this.jaceFolder.getProperties().getIdValue("Id");
/* 161 */     return rawId != null ? rawId.toString() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIssuedBy()
/*     */   {
/* 169 */     return this.jaceFolder.getProperties().getStringValue("IssuedBy");
/*     */   }
/*     */   
/*     */ 
/*     */   static String createChildSectionsSQLStatement(Folder parentContainer)
/*     */   {
/* 175 */     Tracer.traceMethodEntry(new Object[] { parentContainer });
/*     */     
/* 177 */     String parentId = parentContainer.get_Id().toString();
/* 178 */     PropertyFilter jacePF = P8CE_GuideSectionImpl.getMandatoryJacePF();
/* 179 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "GuideSection", "s");
/*     */     
/* 181 */     sb.append(" WHERE s.").append("Parent").append(" = OBJECT('").append(parentId).append("')");
/* 182 */     sb.append(" ORDER BY s.").append("Code").append(", s.").append("Topic");
/* 183 */     String sqlStatement = sb.toString();
/*     */     
/* 185 */     Tracer.traceMethodExit(new Object[] { sqlStatement });
/* 186 */     return sqlStatement;
/*     */   }
/*     */   
/*     */   static String createChildTopicsSQLStatement(Folder parentContainer)
/*     */   {
/* 191 */     Tracer.traceMethodEntry(new Object[] { parentContainer });
/*     */     
/* 193 */     String parentId = parentContainer.get_Id().toString();
/* 194 */     PropertyFilter jacePF = P8CE_GuideTopicImpl.getMandatoryJacePF();
/* 195 */     StringBuilder sb = P8CE_Util.createSelectSqlFromJacePF(jacePF, null, "GuideTopic", "t");
/*     */     
/* 197 */     sb.append(" INNER JOIN ").append("ReferentialContainmentRelationship").append(" r on t.This = r.").append("Head");
/* 198 */     sb.append(" WHERE r.").append("Tail").append(" = OBJECT('").append(parentId).append("')");
/* 199 */     sb.append(" ORDER BY t.").append("Code").append(", t.").append("Topic");
/* 200 */     String sqlStatement = sb.toString();
/*     */     
/* 202 */     Tracer.traceMethodExit(new Object[] { sqlStatement });
/* 203 */     return sqlStatement;
/*     */   }
/*     */   
/*     */   static List<ClassificationGuideSection> fetchChildSections(Repository repository, Folder parentContainer)
/*     */   {
/* 208 */     Tracer.traceMethodEntry(new Object[0]);
/* 209 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 212 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 214 */       String sqlStatement = createChildSectionsSQLStatement(parentContainer);
/* 215 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 216 */       SearchScope jaceSearchScope = new SearchScope(parentContainer.getObjectStore());
/*     */       
/* 218 */       Boolean continuable = Boolean.TRUE;
/* 219 */       Integer pageSize = null;
/* 220 */       PropertyFilter pf = P8CE_GuideSectionImpl.getMandatoryJacePF();
/*     */       
/* 222 */       long startTime = System.currentTimeMillis();
/* 223 */       IndependentObjectSet resultSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, pf, continuable);
/* 224 */       long endTime = System.currentTimeMillis();
/* 225 */       Boolean elementCountIndicator = null;
/* 226 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 228 */         elementCountIndicator = Boolean.valueOf(resultSet.isEmpty());
/*     */       }
/* 230 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, resultSet, new Object[] { sqlStatement, pageSize, pf, continuable });
/*     */       
/* 232 */       List<ClassificationGuideSection> resultList = new ArrayList(0);
/* 233 */       PageIterator jacePI = resultSet.pageIterator();
/* 234 */       Folder jaceSectionBase = null;
/* 235 */       Object[] currentPage; while (jacePI.nextPage())
/*     */       {
/* 237 */         currentPage = jacePI.getCurrentPage();
/* 238 */         for (Object obj : currentPage)
/*     */         {
/* 240 */           jaceSectionBase = (Folder)obj;
/* 241 */           resultList.add(new P8CE_GuideSectionImpl(repository, jaceSectionBase));
/*     */         }
/*     */       }
/*     */       
/* 245 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 246 */       return resultList;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 250 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 254 */       String repositoryIdent = repository.getSymbolicName();
/* 255 */       String parentIdent = parentContainer.getObjectReference().getObjectIdentity();
/* 256 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_GUIDE_SECTIONS_FAILED, new Object[] { repositoryIdent, parentIdent });
/*     */     }
/*     */     finally
/*     */     {
/* 260 */       if (establishedSubject) {
/* 261 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static List<ClassificationGuideTopic> fetchChildTopics(Repository repository, Folder parentContainer) {
/* 267 */     Tracer.traceMethodEntry(new Object[0]);
/* 268 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 271 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 273 */       String sqlStatement = createChildTopicsSQLStatement(parentContainer);
/* 274 */       SearchSQL jaceSearchSQL = new SearchSQL(sqlStatement);
/* 275 */       SearchScope jaceSearchScope = new SearchScope(parentContainer.getObjectStore());
/*     */       
/* 277 */       Boolean continuable = Boolean.TRUE;
/* 278 */       Integer pageSize = null;
/* 279 */       PropertyFilter pf = P8CE_GuideTopicImpl.getMandatoryJacePF();
/*     */       
/* 281 */       long startTime = System.currentTimeMillis();
/* 282 */       IndependentObjectSet resultSet = jaceSearchScope.fetchObjects(jaceSearchSQL, pageSize, pf, continuable);
/* 283 */       long endTime = System.currentTimeMillis();
/* 284 */       Boolean elementCountIndicator = null;
/* 285 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 287 */         elementCountIndicator = Boolean.valueOf(resultSet.isEmpty());
/*     */       }
/* 289 */       Tracer.traceExtCall("SearchScope.fetchObjects", startTime, endTime, elementCountIndicator, resultSet, new Object[] { sqlStatement, pageSize, pf, continuable });
/*     */       
/* 291 */       List<ClassificationGuideTopic> resultList = new ArrayList(0);
/* 292 */       PageIterator jacePI = resultSet.pageIterator();
/* 293 */       Document jaceTopicBase = null;
/* 294 */       Object[] currentPage; while (jacePI.nextPage())
/*     */       {
/* 296 */         currentPage = jacePI.getCurrentPage();
/* 297 */         for (Object obj : currentPage)
/*     */         {
/* 299 */           jaceTopicBase = (Document)obj;
/* 300 */           resultList.add(new P8CE_GuideTopicImpl(repository, jaceTopicBase));
/*     */         }
/*     */       }
/*     */       
/* 304 */       Tracer.traceMethodExit(new Object[] { resultList });
/* 305 */       return resultList;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 309 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 313 */       String repositoryIdent = repository.getSymbolicName();
/* 314 */       String parentIdent = parentContainer.getObjectReference().getObjectIdentity();
/* 315 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_RETRIEVING_GUIDE_SECTIONS_FAILED, new Object[] { repositoryIdent, parentIdent });
/*     */     }
/*     */     finally
/*     */     {
/* 319 */       if (establishedSubject) {
/* 320 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 330 */     String ident = "<unknown>";
/* 331 */     if (this.jaceFolder.getProperties().isPropertyPresent("FolderName")) {
/* 332 */       ident = this.jaceFolder.getProperties().getStringValue("FolderName");
/*     */     } else {
/* 334 */       ident = this.jaceFolder.getObjectReference().getObjectIdentity();
/*     */     }
/* 336 */     return "P8CE_ClassificationGuideImpl: '" + ident + "'";
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_ClassificationGuideImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */