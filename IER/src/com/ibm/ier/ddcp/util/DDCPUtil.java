/*     */ package com.ibm.ier.ddcp.util;
/*     */ 
/*     */ import com.filenet.api.collection.ContentElementList;
/*     */ import com.filenet.api.collection.RepositoryRowSet;
/*     */ import com.filenet.api.constants.AutoClassify;
/*     */ import com.filenet.api.constants.CheckinType;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.ContentTransfer;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Factory.ContentElement;
/*     */ import com.filenet.api.core.Factory.ContentTransfer;
/*     */ import com.filenet.api.core.Factory.Document;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.ddcp.exception.DDCPErrorCode;
/*     */ import com.ibm.ier.ddcp.exception.DDCPRuntimeException;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.FileAppender;
/*     */ import org.apache.log4j.Logger;
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
/*     */ public class DDCPUtil
/*     */ {
/*  48 */   private static final DDCPTracer mTracer = DDCPTracer.getDDCPTracer(DDCPTracer.SubSystem.Util);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getCEDefaultLogLocation()
/*     */   {
/*  55 */     String ceDefaultLogLocation = null;
/*     */     
/*  57 */     Logger ceDefaultLogger = Logger.getLogger("filenet_error");
/*  58 */     Enumeration allCEAppenders = ceDefaultLogger.getAllAppenders();
/*  59 */     while (allCEAppenders.hasMoreElements()) {
/*  60 */       Appender ceAppender = (Appender)allCEAppenders.nextElement();
/*  61 */       if ((ceAppender instanceof FileAppender)) {
/*  62 */         String fileOfAppender = ((FileAppender)ceAppender).getFile();
/*  63 */         if (fileOfAppender.length() > 0) {
/*  64 */           if (fileOfAppender.lastIndexOf(File.separator) >= 0) {
/*  65 */             ceDefaultLogLocation = fileOfAppender.substring(0, fileOfAppender.lastIndexOf(File.separator));
/*  66 */             break; }
/*  67 */           if (fileOfAppender.lastIndexOf("/") >= 0)
/*     */           {
/*  69 */             ceDefaultLogLocation = fileOfAppender.substring(0, fileOfAppender.lastIndexOf("/"));
/*  70 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  75 */     return ceDefaultLogLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String sterilizeStringForCSV(String aStr)
/*     */   {
/*  86 */     mTracer.traceMethodEntry(new Object[] { aStr });
/*  87 */     if ((aStr != null) && (!"".equals(aStr.trim()))) {
/*  88 */       aStr = aStr.trim().replace("\"", "\"\"");
/*     */     }
/*  90 */     mTracer.traceMethodExit(new Object[] { aStr });
/*  91 */     return aStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getEntityIdSetInString(Set<String> entityIdSet)
/*     */   {
/* 100 */     mTracer.traceMethodEntry(new Object[0]);
/* 101 */     StringBuilder sb = new StringBuilder();
/* 102 */     for (String entityId : entityIdSet) {
/* 103 */       sb.append(entityId.trim());
/* 104 */       sb.append(", ");
/*     */     }
/* 106 */     int lastIndex = sb.lastIndexOf(", ");
/* 107 */     mTracer.traceMethodExit(new Object[0]);
/* 108 */     return sb.substring(0, lastIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getEntityIdListInString(List<String> entityIdList)
/*     */   {
/* 117 */     mTracer.traceMethodEntry(new Object[0]);
/* 118 */     StringBuilder sb = new StringBuilder();
/* 119 */     for (String entityId : entityIdList) {
/* 120 */       sb.append(entityId.trim());
/* 121 */       sb.append(", ");
/*     */     }
/* 123 */     int lastIndex = sb.lastIndexOf(", ");
/* 124 */     mTracer.traceMethodExit(new Object[0]);
/* 125 */     return sb.substring(0, lastIndex);
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
/*     */   public static Document saveReport(ObjectStore jaceFPOS, Document theDoc, StringBuilder reportBuilder, String docName)
/*     */   {
/* 140 */     mTracer.traceMethodEntry(new Object[] { jaceFPOS, docName });
/*     */     
/* 142 */     ContentElementList contentList = null;
/* 143 */     if (theDoc == null) {
/* 144 */       Id reportId = Id.createId();
/*     */       
/* 146 */       theDoc = Factory.Document.createInstance(jaceFPOS, "Transcript", reportId);
/*     */       
/*     */ 
/*     */ 
/* 150 */       theDoc.getProperties().putValue("DocumentTitle", docName);
/* 151 */       theDoc.set_MimeType("text/plain");
/*     */       
/* 153 */       contentList = Factory.ContentElement.createList();
/*     */     } else {
/* 155 */       contentList = theDoc.get_ContentElements();
/*     */     }
/*     */     
/* 158 */     ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
/*     */     
/* 160 */     ByteArrayInputStream docContent = null;
/*     */     try {
/* 162 */       docContent = new ByteArrayInputStream(reportBuilder.toString().getBytes("UTF-8"));
/*     */       
/* 164 */       ctObject.setCaptureSource(docContent);
/* 165 */       ctObject.set_ContentType("application/x-filenet-recordtranscript");
/* 166 */       ctObject.set_RetrievalName(docName);
/*     */       
/*     */ 
/* 169 */       contentList.add(ctObject);
/*     */       
/* 171 */       theDoc.set_ContentElements(contentList);
/*     */       
/* 173 */       PropertyFilter pf = new PropertyFilter();
/* 174 */       pf.addIncludeProperty(new FilterElement(null, null, null, "Id", null));
/*     */       
/* 176 */       pf.addIncludeProperty(new FilterElement(null, null, null, "ContentSize", null));
/*     */       
/* 178 */       pf.addIncludeProperty(new FilterElement(null, null, null, "ContentElements", null));
/*     */       
/* 180 */       theDoc.save(RefreshMode.REFRESH, pf);
/*     */     }
/*     */     catch (Exception ex) {
/* 183 */       throw DDCPRuntimeException.createDDCPRuntimeException(ex, DDCPErrorCode.FAILED_TO_SAVE_CONTENT_IN_DOC, new Object[0]);
/*     */     } finally {
/* 185 */       if (docContent != null) {
/*     */         try
/*     */         {
/* 188 */           docContent.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 195 */     mTracer.traceMethodExit(new Object[] { theDoc });
/*     */     
/* 197 */     return theDoc;
/*     */   }
/*     */   
/*     */   public static String generateReportName(String docTitlePrefix, int advanceDays) {
/* 201 */     mTracer.traceMethodEntry(new Object[] { docTitlePrefix, Integer.valueOf(advanceDays) });
/* 202 */     SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd'T'HH_mm_ss.SSS");
/* 203 */     f.setTimeZone(TimeZone.getTimeZone("UTC"));
/* 204 */     Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/*     */     
/* 206 */     currentTime.add(5, advanceDays);
/* 207 */     String currentTimeInUTC = f.format(currentTime.getTime());
/*     */     
/*     */ 
/* 210 */     String docName = docTitlePrefix + currentTimeInUTC + ".csv";
/* 211 */     mTracer.traceMethodExit(new Object[] { docName });
/* 212 */     return docName;
/*     */   }
/*     */   
/*     */   public static String checkinReport(Document doc) {
/* 216 */     mTracer.traceMethodEntry(new Object[] { doc });
/*     */     
/* 218 */     doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/*     */     
/* 220 */     doc.save(RefreshMode.NO_REFRESH);
/* 221 */     mTracer.traceMethodExit(new Object[] { doc.get_Id().toString() });
/* 222 */     return doc.get_Id().toString();
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
/*     */   public static RepositoryRowSet fetchDBRows(ObjectStore jaceOS, String queryStr, PropertyFilter propertyFilter, int readBatchSize, Boolean isPagedQuery)
/*     */   {
/* 237 */     mTracer.traceMethodEntry(new Object[] { jaceOS, queryStr, propertyFilter, Integer.valueOf(readBatchSize), isPagedQuery });
/*     */     
/* 239 */     SearchSQL qSql = new SearchSQL(queryStr);
/*     */     
/* 241 */     SearchScope ss = new SearchScope(jaceOS);
/*     */     
/* 243 */     RepositoryRowSet resultSet = ss.fetchRows(qSql, Integer.valueOf(readBatchSize), propertyFilter, isPagedQuery);
/*     */     
/* 245 */     mTracer.traceMethodExit(new Object[0]);
/*     */     
/* 247 */     return resultSet;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\util\DDCPUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */