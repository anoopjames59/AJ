/*     */ package com.ibm.ier.ddcp;
/*     */ 
/*     */ import com.filenet.api.collection.RepositoryRowSet;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.Connection;
/*     */ import com.filenet.api.core.Factory.Domain;
/*     */ import com.filenet.api.core.Factory.Folder;
/*     */ import com.filenet.api.core.Factory.ObjectStore;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.filenet.api.util.UserContext;
/*     */ import com.ibm.ier.ddcp.exception.DDCPErrorCode;
/*     */ import com.ibm.ier.ddcp.exception.DDCPRuntimeException;
/*     */ import com.ibm.ier.ddcp.util.DDCPLogCode;
/*     */ import com.ibm.ier.ddcp.util.DDCPLogger;
/*     */ import com.ibm.ier.ddcp.util.DDCPTracer;
/*     */ import com.ibm.ier.ddcp.util.DDCPTracer.SubSystem;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OnHoldValidator
/*     */ {
/*     */   private static final PropertyFilter containerPropertyFilter;
/*     */   private static final String QUERY_ONHOLD_CONTAINERS;
/*     */   
/*     */   static
/*     */   {
/*  43 */     StringBuilder sb = new StringBuilder();
/*  44 */     sb.append("Id").append(' ');
/*  45 */     sb.append("Name").append(' ');
/*  46 */     sb.append("RMEntityType").append(' ');
/*     */     
/*  48 */     containerPropertyFilter = new PropertyFilter();
/*  49 */     containerPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */     StringBuilder sb = new StringBuilder();
/*  57 */     sb.append("SELECT ");
/*  58 */     sb.append("f.").append("Id");
/*  59 */     sb.append(", f.").append("Name");
/*  60 */     sb.append(", f.").append("RMEntityType");
/*     */     
/*  62 */     sb.append(" FROM RMFolder f WHERE");
/*  63 */     sb.append(" f.OnHold = TRUE");
/*     */     
/*  65 */     QUERY_ONHOLD_CONTAINERS = sb.toString();
/*     */   }
/*     */   
/*  68 */   private DDCPLogger mLogger = DDCPLogger.getDDCPLogger(ReportGeneration.class.getName());
/*  69 */   private DDCPTracer mTracer = DDCPTracer.getDDCPTracer(DDCPTracer.SubSystem.Util);
/*     */   
/*     */ 
/*     */ 
/*  73 */   private Set<String> mOnHoldContainerIdSet = new HashSet();
/*  74 */   private Set<String> mIndirectOnHoldContainerIdSet = new HashSet();
/*  75 */   private Set<String> mNotOnHoldContainerIdSet = new HashSet();
/*     */   
/*  77 */   private Map<String, Boolean> mContainerPathHoldCache = new HashMap();
/*     */   
/*  79 */   private ObjectStore mJaceFPOS = null;
/*  80 */   private int mReadBatchSize = 10000;
/*     */   
/*  82 */   private int mCacheSizeLimit = 100000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected OnHoldValidator(ObjectStore jaceFPOS, int readBatchSize, int cacheSizeLimit)
/*     */   {
/*  90 */     this.mJaceFPOS = jaceFPOS;
/*  91 */     this.mReadBatchSize = readBatchSize;
/*     */     
/*  93 */     this.mCacheSizeLimit = cacheSizeLimit;
/*     */     
/*     */ 
/*  96 */     this.mOnHoldContainerIdSet = retrieveOnHoldContainers();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOnHold(String ddContainerId, String parentContainerPath, String filePlanName)
/*     */   {
/* 108 */     this.mTracer.traceMethodEntry(new Object[] { ddContainerId });
/*     */     
/* 110 */     if (ddContainerId == null)
/*     */     {
/* 112 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     if (this.mOnHoldContainerIdSet.isEmpty())
/*     */     {
/* 118 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     if (isInOnholdList(ddContainerId, parentContainerPath, filePlanName))
/*     */     {
/* 124 */       this.mTracer.traceMinimumMsg("Container {0} is on direct/indirect IER hold.", new Object[] { ddContainerId });
/* 125 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 126 */       return true;
/*     */     }
/*     */     
/* 129 */     this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 130 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Set<String> retrieveOnHoldContainers()
/*     */   {
/* 142 */     this.mTracer.traceMethodEntry(new Object[0]);
/*     */     
/* 144 */     String queryStr = QUERY_ONHOLD_CONTAINERS;
/* 145 */     this.mTracer.traceMinimumMsg("Query to get all containers on direct hold in this FPOS: {0}", new Object[] { queryStr });
/*     */     
/* 147 */     SearchSQL qSql = new SearchSQL(queryStr);
/*     */     
/* 149 */     SearchScope ss = new SearchScope(this.mJaceFPOS);
/*     */     
/* 151 */     long startTime = System.currentTimeMillis();
/* 152 */     RepositoryRowSet resultSet = null;
/*     */     try {
/* 154 */       resultSet = ss.fetchRows(qSql, Integer.valueOf(this.mReadBatchSize), containerPropertyFilter, Boolean.TRUE);
/*     */     }
/*     */     catch (Exception ex) {
/* 157 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { queryStr });
/*     */     }
/*     */     
/* 160 */     long endTime = System.currentTimeMillis();
/* 161 */     this.mTracer.traceMinimumMsg("Time for first page query for all containers on direct hold in this FPOS: {0} ms.", new Object[] { Long.valueOf(endTime - startTime) });
/*     */     
/*     */ 
/* 164 */     Set<String> resultIdSet = new HashSet();
/* 165 */     if ((resultSet != null) && (!resultSet.isEmpty())) {
/* 166 */       Iterator it = resultSet.iterator();
/* 167 */       while ((it.hasNext()) && (resultIdSet.size() < this.mCacheSizeLimit)) {
/* 168 */         RepositoryRow containerRow = (RepositoryRow)it.next();
/* 169 */         String containerId = containerRow.getProperties().getIdValue("Id").toString();
/* 170 */         resultIdSet.add(containerId);
/*     */       }
/*     */     }
/*     */     
/* 174 */     this.mTracer.traceMinimumMsg("Total containers on direct IER hold loaded in cache: {0}", new Object[] { Integer.valueOf(resultIdSet.size()) });
/* 175 */     this.mLogger.info(DDCPLogCode.TOTAL_ONHOLD_CONTAINERS, new Object[] { Integer.valueOf(resultIdSet.size()) });
/*     */     
/* 177 */     if (resultIdSet.size() >= this.mCacheSizeLimit) {
/* 178 */       this.mTracer.traceMinimumMsg("Warnings: the cache for containers on direct IER hold is full.", new Object[0]);
/* 179 */       this.mLogger.info(DDCPLogCode.CACHE_FOR_CONTAINERS_ONDIRECTHOLD_FULL, new Object[0]);
/*     */     }
/*     */     
/* 182 */     this.mTracer.traceMethodExit(new Object[] { resultIdSet });
/* 183 */     return resultIdSet;
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
/*     */   private boolean isInOnholdList(String containerId, String parentContainerPath, String filePlanName)
/*     */   {
/* 197 */     this.mTracer.traceMethodEntry(new Object[0]);
/*     */     
/*     */ 
/* 200 */     if (this.mNotOnHoldContainerIdSet.contains(containerId))
/*     */     {
/* 202 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 203 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 207 */     if (this.mOnHoldContainerIdSet.contains(containerId))
/*     */     {
/* 209 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 210 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 214 */     if (this.mIndirectOnHoldContainerIdSet.contains(containerId))
/*     */     {
/* 216 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 217 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 221 */     if (isFiledUnderOnHoldContainers(containerId, parentContainerPath, filePlanName))
/*     */     {
/*     */ 
/* 224 */       if (this.mIndirectOnHoldContainerIdSet.size() < this.mCacheSizeLimit) {
/* 225 */         this.mIndirectOnHoldContainerIdSet.add(containerId);
/*     */       }
/*     */       
/* 228 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 229 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 233 */     if (this.mNotOnHoldContainerIdSet.size() < this.mCacheSizeLimit) {
/* 234 */       this.mNotOnHoldContainerIdSet.add(containerId);
/*     */     }
/* 236 */     this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 237 */     return false;
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
/*     */   private boolean isFiledUnderOnHoldContainers(String containerId, String parentContainerPath, String filePlanName)
/*     */   {
/* 250 */     this.mTracer.traceMethodEntry(new Object[] { containerId });
/*     */     
/* 252 */     if ((this.mOnHoldContainerIdSet == null) || (this.mOnHoldContainerIdSet.size() == 0)) {
/* 253 */       this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     if ((parentContainerPath == null) || ("".equals(parentContainerPath.trim())) || (filePlanName == null) || ("".equals(filePlanName.trim())))
/*     */     {
/* 259 */       throw DDCPRuntimeException.createDDCPRuntimeException(DDCPErrorCode.INVALID_PARENTPATH_FILEPLANNAME, new Object[0]);
/*     */     }
/*     */     
/* 262 */     PropertyFilter containerPropertyFilter = new PropertyFilter();
/* 263 */     containerPropertyFilter.addIncludeProperty(0, null, null, "OnHold", null);
/*     */     
/*     */ 
/* 266 */     StringBuilder containerNamePathBuilder = new StringBuilder();
/* 267 */     containerNamePathBuilder.append("/Records Management/");
/* 268 */     containerNamePathBuilder.append(filePlanName);
/*     */     
/*     */ 
/* 271 */     StringBuilder folderPathWithoutRootFolderBuilder = new StringBuilder();
/* 272 */     folderPathWithoutRootFolderBuilder.append(filePlanName);
/*     */     
/* 274 */     Boolean isOnHold = Boolean.valueOf(false);
/*     */     
/* 276 */     String[] parentContainerNameArray = parentContainerPath.split("/", 0);
/* 277 */     for (String containerName : parentContainerNameArray) {
/* 278 */       containerNamePathBuilder.append("/").append(containerName);
/* 279 */       String currentContainerNamePath = containerNamePathBuilder.toString();
/*     */       
/* 281 */       folderPathWithoutRootFolderBuilder.append("/").append(containerName);
/* 282 */       String folderPathWithoutRootFolder = folderPathWithoutRootFolderBuilder.toString();
/*     */       
/*     */ 
/* 285 */       if (this.mContainerPathHoldCache.containsKey(folderPathWithoutRootFolder)) {
/* 286 */         isOnHold = (Boolean)this.mContainerPathHoldCache.get(folderPathWithoutRootFolder);
/* 287 */         if (isOnHold.booleanValue()) {
/* 288 */           this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 289 */           return true;
/*     */         }
/*     */       }
/*     */       else {
/* 293 */         long timeSpot1 = System.currentTimeMillis();
/* 294 */         Folder theFolder = Factory.Folder.fetchInstance(this.mJaceFPOS, currentContainerNamePath, containerPropertyFilter);
/* 295 */         long timeSpot2 = System.currentTimeMillis();
/* 296 */         this.mTracer.traceMinimumMsg("Time to fetch the folder {0} : {1} ms.", new Object[] { currentContainerNamePath, Long.valueOf(timeSpot2 - timeSpot1) });
/*     */         
/*     */ 
/* 299 */         isOnHold = theFolder.getProperties().getBooleanValue("OnHold");
/* 300 */         if (isOnHold.booleanValue())
/*     */         {
/* 302 */           if (this.mContainerPathHoldCache.size() < this.mCacheSizeLimit) {
/* 303 */             this.mContainerPathHoldCache.put(folderPathWithoutRootFolder, isOnHold);
/*     */           }
/*     */           
/* 306 */           this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 307 */           return true;
/*     */         }
/*     */         
/* 310 */         if (this.mContainerPathHoldCache.size() < this.mCacheSizeLimit) {
/* 311 */           this.mContainerPathHoldCache.put(folderPathWithoutRootFolder, Boolean.valueOf(false));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 317 */     this.mTracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 318 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 325 */     String serverUrl = "http://cm-rm-win07:9080/wsi/FNCEWS40MTOM";
/*     */     try {
/* 327 */       UserContext userCtx = UserContext.get();
/* 328 */       Connection jaceConn = Factory.Connection.getConnection(serverUrl);
/* 329 */       String stanza = "FileNetP8";
/* 330 */       Subject jaceSubject = UserContext.createSubject(jaceConn, "p8admin", "p8admin", stanza);
/*     */       
/* 332 */       userCtx.pushSubject(jaceSubject);
/*     */       
/* 334 */       Domain jaceDomain = Factory.Domain.fetchInstance(jaceConn, null, null);
/*     */       
/* 336 */       ObjectStore jaceOS = Factory.ObjectStore.fetchInstance(jaceDomain, "LZHOU_FPOS_Base", null);
/*     */       
/*     */ 
/* 339 */       OnHoldValidator holdValidator = new OnHoldValidator(jaceOS, 10, 2);
/*     */       
/* 341 */       boolean isOnHold = holdValidator.isFiledUnderOnHoldContainers("{81EF9856-8E35-4C59-9AB8-7EB942C13D8B}", "Cat1/Cat2/Cat3OnHold", "File Plan 1");
/*     */       
/*     */ 
/* 344 */       System.out.println("Cat3OnHold is on hold : " + isOnHold);
/*     */       
/* 346 */       isOnHold = holdValidator.isFiledUnderOnHoldContainers("{D70ABD5A-50F5-47FC-BB8B-D0A884CD3A11}", "Cat1/Cat2/Cat4NotOnHold", "File Plan 1");
/*     */       
/*     */ 
/* 349 */       System.out.println("Cat4NotOnHold is on hold : " + isOnHold);
/*     */     }
/*     */     catch (Exception ex) {
/* 352 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\OnHoldValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */