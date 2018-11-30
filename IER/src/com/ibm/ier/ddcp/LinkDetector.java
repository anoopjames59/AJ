/*     */ package com.ibm.ier.ddcp;
/*     */ 
/*     */ import com.filenet.api.collection.RepositoryRowSet;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.ddcp.entity.LinkEntity;
/*     */ import com.ibm.ier.ddcp.exception.DDCPErrorCode;
/*     */ import com.ibm.ier.ddcp.exception.DDCPRuntimeException;
/*     */ import com.ibm.ier.ddcp.util.DDCPLogCode;
/*     */ import com.ibm.ier.ddcp.util.DDCPLogger;
/*     */ import com.ibm.ier.ddcp.util.DDCPTracer;
/*     */ import com.ibm.ier.ddcp.util.DDCPTracer.SubSystem;
/*     */ import com.ibm.ier.ddcp.util.DDCPUtil;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ public class LinkDetector
/*     */ {
/*     */   private static final PropertyFilter linkPropertyFilter;
/*     */   private static final String QUERY_RMLINKS;
/*     */   private static final String QUERY_TEMPLATE_RMLINKS_FOR_A_RECORD;
/*     */   
/*     */   static
/*     */   {
/*  39 */     StringBuilder sb = new StringBuilder();
/*  40 */     sb.append("Id").append(' ');
/*  41 */     sb.append("Name").append(' ');
/*  42 */     sb.append("Head").append(' ');
/*  43 */     sb.append("Tail").append(' ');
/*  44 */     linkPropertyFilter = new PropertyFilter();
/*  45 */     linkPropertyFilter.addIncludeProperty(0, null, null, sb.toString(), null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */     StringBuilder sb = new StringBuilder();
/*  52 */     sb.append("SELECT ");
/*  53 */     sb.append("Id");
/*  54 */     sb.append(", ").append("Name");
/*  55 */     sb.append(", ").append("Head");
/*  56 */     sb.append(", ").append("Tail");
/*  57 */     sb.append(" FROM ");
/*  58 */     sb.append("Relation");
/*  59 */     sb.append(" WHERE ").append("RMEntityType").append(" <> ").append(400);
/*  60 */     sb.append(" AND ").append("RMEntityType").append(" <> ").append(406);
/*  61 */     QUERY_RMLINKS = sb.toString();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */     StringBuilder sb = new StringBuilder();
/*  68 */     sb.append("SELECT l.").append("Id");
/*  69 */     sb.append(" FROM ").append("Relation").append(" l");
/*  70 */     sb.append(" WHERE (").append("l.").append("Head").append(" = OBJECT('%s')");
/*  71 */     sb.append(" OR ").append("l.").append("Tail").append(" = OBJECT('%s'))");
/*     */     
/*  73 */     QUERY_TEMPLATE_RMLINKS_FOR_A_RECORD = sb.toString();
/*     */   }
/*     */   
/*  76 */   private DDCPLogger mLogger = DDCPLogger.getDDCPLogger(ReportGeneration.class.getName());
/*  77 */   private DDCPTracer mTracer = DDCPTracer.getDDCPTracer(DDCPTracer.SubSystem.Util);
/*     */   
/*  79 */   private Set<LinkEntity> mLinkCache = new HashSet();
/*     */   
/*  81 */   private boolean mIsLinkCacheFull = false;
/*     */   
/*  83 */   private ObjectStore mJaceFPOS = null;
/*  84 */   private int mReadBatchSize = 10000;
/*  85 */   private int mLinkCacheSizeLimit = 100000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkDetector(ObjectStore jaceFPOS, int readBatchSize, int linkCacheSizeLimit)
/*     */   {
/*  92 */     this.mJaceFPOS = jaceFPOS;
/*  93 */     this.mReadBatchSize = readBatchSize;
/*  94 */     this.mLinkCacheSizeLimit = linkCacheSizeLimit;
/*     */     
/*     */ 
/*  97 */     loadAllRMLinks();
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
/*     */   private void loadAllRMLinks()
/*     */   {
/* 110 */     this.mTracer.traceMethodEntry(new Object[0]);
/*     */     
/*     */     try
/*     */     {
/* 114 */       SearchScope searchScope = new SearchScope(this.mJaceFPOS);
/*     */       
/* 116 */       this.mTracer.traceMinimumMsg("Query for all links: {0}", new Object[] { QUERY_RMLINKS });
/* 117 */       SearchSQL search = new SearchSQL(QUERY_RMLINKS);
/*     */       
/* 119 */       long timeSpot0 = System.currentTimeMillis();
/* 120 */       RepositoryRowSet rowSet = searchScope.fetchRows(search, Integer.valueOf(this.mReadBatchSize), linkPropertyFilter, Boolean.TRUE);
/* 121 */       long timeSpot1 = System.currentTimeMillis();
/* 122 */       this.mTracer.traceMinimumMsg("Time to get first page back for link query: {0} ms.", new Object[] { Long.valueOf(timeSpot1 - timeSpot0) });
/*     */       
/*     */ 
/* 125 */       String linkObjId = null;
/* 126 */       String linkObjName = null;
/* 127 */       String headId = null;
/* 128 */       String tailId = null;
/*     */       
/* 130 */       Iterator ite = rowSet.iterator();
/* 131 */       while (ite.hasNext())
/*     */       {
/*     */ 
/*     */ 
/* 135 */         RepositoryRow row = (RepositoryRow)ite.next();
/* 136 */         linkObjId = row.getProperties().getIdValue("Id").toString();
/* 137 */         linkObjName = row.getProperties().getStringValue("Name");
/* 138 */         headId = row.getProperties().getIdValue("Head").toString();
/* 139 */         tailId = row.getProperties().getIdValue("Tail").toString();
/*     */         
/* 141 */         LinkEntity aLink = new LinkEntity();
/* 142 */         aLink.setLinkId(linkObjId);
/* 143 */         aLink.setLinkName(linkObjName);
/* 144 */         aLink.setHeadId(headId);
/* 145 */         aLink.setTailId(tailId);
/*     */         
/* 147 */         this.mLinkCache.add(aLink);
/*     */         
/* 149 */         if (this.mLinkCache.size() == this.mLinkCacheSizeLimit) {
/* 150 */           this.mLinkCache = new HashSet(1);
/* 151 */           this.mLinkCache.add(aLink);
/* 152 */           this.mIsLinkCacheFull = true;
/* 153 */           this.mTracer.traceMinimumMsg("Warnings: RMLink cache is full.", new Object[0]);
/* 154 */           this.mLogger.warn(DDCPLogCode.LINK_CACHE_IS_FULL, new Object[0]);
/*     */           
/* 156 */           break;
/*     */         }
/*     */       }
/*     */       
/* 160 */       this.mTracer.traceMinimumMsg("Total RMLink objects loaded in cache: {0}.", new Object[] { Integer.valueOf(this.mLinkCache.size()) });
/* 161 */       this.mLogger.info(DDCPLogCode.FOUND_LINKS, new Object[] { Integer.valueOf(this.mLinkCache.size()) });
/*     */ 
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 166 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { QUERY_RMLINKS });
/*     */     }
/*     */     
/*     */ 
/* 170 */     this.mTracer.traceMethodExit(new Object[] { Integer.valueOf(this.mLinkCache.size()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getRelatedRMLinkIds(Set<String> recIds)
/*     */   {
/* 181 */     this.mTracer.traceMethodEntry(new Object[] { recIds });
/* 182 */     Set<String> result = new HashSet();
/*     */     
/* 184 */     if ((this.mLinkCache.isEmpty()) || (recIds == null) || (recIds.isEmpty())) {
/* 185 */       return result;
/*     */     }
/*     */     
/* 188 */     if (this.mIsLinkCacheFull)
/*     */     {
/* 190 */       result = retrieveRelatedRMLinkIds(recIds);
/*     */     } else {
/* 192 */       for (LinkEntity aLink : this.mLinkCache) {
/* 193 */         String linkId = aLink.getLinkId();
/* 194 */         String headId = aLink.getHeadId();
/* 195 */         String tailId = aLink.getTailId();
/*     */         
/* 197 */         if ((null != headId) && (null != tailId) && (
/* 198 */           (recIds.contains(headId)) || (recIds.contains(tailId)))) {
/* 199 */           result.add(linkId);
/* 200 */           this.mTracer.traceMinimumMsg("Found a RMLink {0} that links to a record.", new Object[] { linkId });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 206 */     this.mTracer.traceMinimumMsg("There are {0} RMLinks that link to these deleted records.", new Object[] { Integer.valueOf(result.size()) });
/* 207 */     this.mLogger.info(DDCPLogCode.NUMBER_OF_LINKS_TO_DELETED_RECORDS, new Object[] { Integer.valueOf(result.size()) });
/*     */     
/* 209 */     this.mTracer.traceMethodExit(new Object[] { result });
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
/*     */   private Set<String> retrieveRelatedRMLinkIds(Set<String> recIds)
/*     */   {
/* 222 */     this.mTracer.traceMethodEntry(new Object[] { recIds });
/*     */     
/* 224 */     PropertyFilter rmLinkPropertyFilter = new PropertyFilter();
/* 225 */     rmLinkPropertyFilter.addIncludeProperty(0, null, null, "Id", null);
/*     */     
/* 227 */     Set<String> rmLinkIDSet = new HashSet();
/*     */     
/* 229 */     long startTime = System.currentTimeMillis();
/* 230 */     this.mTracer.traceMinimumMsg("Start retrieving related RMLink objects for total {0} records...", new Object[] { Integer.valueOf(recIds.size()) });
/*     */     
/* 232 */     for (String recID : recIds) {
/* 233 */       String queryForRMLinks = String.format(QUERY_TEMPLATE_RMLINKS_FOR_A_RECORD, new Object[] { recID });
/*     */       
/* 235 */       RepositoryRowSet resultSet = null;
/*     */       try {
/* 237 */         resultSet = DDCPUtil.fetchDBRows(this.mJaceFPOS, queryForRMLinks, rmLinkPropertyFilter, 1, Boolean.FALSE);
/*     */       }
/*     */       catch (Exception ex) {
/* 240 */         throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_RETRIEVE, new Object[] { queryForRMLinks });
/*     */       }
/*     */       
/*     */ 
/* 244 */       if ((resultSet != null) && (!resultSet.isEmpty()))
/*     */       {
/*     */ 
/*     */ 
/* 248 */         Iterator rowIter = resultSet.iterator();
/* 249 */         if (rowIter.hasNext())
/*     */         {
/* 251 */           RepositoryRow row = (RepositoryRow)rowIter.next();
/* 252 */           String rmLinkId = row.getProperties().getIdValue("Id").toString();
/* 253 */           rmLinkIDSet.add(rmLinkId);
/*     */         }
/*     */       } }
/* 256 */     long endTime = System.currentTimeMillis();
/*     */     
/* 258 */     this.mTracer.traceMinimumMsg("Time to retrieve total {0{ related RMLink objects for total {1} records: {2} ms.", new Object[] { rmLinkIDSet, Integer.valueOf(recIds.size()), Long.valueOf(endTime - startTime) });
/*     */     
/* 260 */     this.mTracer.traceMethodExit(new Object[] { Integer.valueOf(rmLinkIDSet.size()) });
/* 261 */     return rmLinkIDSet;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\LinkDetector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */