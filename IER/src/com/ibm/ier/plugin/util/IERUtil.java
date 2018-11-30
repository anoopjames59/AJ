/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.configuration.DesktopConfig;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.BulkItemResult;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.ContentSearchOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.configuration.PropertyConverter;
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
/*     */ public class IERUtil
/*     */ {
/*  32 */   private static char ARRAY_DELIMITER = ',';
/*     */   
/*     */   public static String getDocId(BaseEntity ref) {
/*  35 */     String objectReferenceId = ref.getClassName();
/*     */     
/*  37 */     StringBuilder builder = new StringBuilder(objectReferenceId);
/*  38 */     builder.append(ARRAY_DELIMITER);
/*  39 */     builder.append(ref.getRepository().getObjectIdentity());
/*  40 */     builder.append(ARRAY_DELIMITER);
/*  41 */     builder.append(ref.getObjectIdentity());
/*  42 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static String getDocId(String className, String p8RepositoryId, String objectId) {
/*  46 */     StringBuilder builder = new StringBuilder(className);
/*  47 */     builder.append(ARRAY_DELIMITER);
/*  48 */     builder.append(p8RepositoryId);
/*  49 */     builder.append(ARRAY_DELIMITER);
/*  50 */     builder.append(objectId);
/*  51 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static RuntimeException getFirstException(List<BulkItemResult> results) {
/*  55 */     for (BulkItemResult result : results) {
/*  56 */       if (!result.wasSuccessful()) {
/*  57 */         return result.getException();
/*     */       }
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getIdFromDocIdString(String docIdString)
/*     */   {
/*  70 */     if (Logger.isDebugLogged()) {
/*  71 */       Logger.logDebug("IERUtil", "getIdFromDocIdString", "DocId String: " + docIdString);
/*     */     }
/*     */     
/*  74 */     if (docIdString != null) {
/*  75 */       String[] splitFolderId = docIdString.split(",");
/*  76 */       if (splitFolderId.length >= 3) {
/*  77 */         return splitFolderId[2];
/*     */       }
/*     */     }
/*  80 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getObjectStoreFromDocIdString(String docIdString)
/*     */   {
/*  90 */     if (Logger.isDebugLogged()) {
/*  91 */       Logger.logDebug("IERUtil", "getIdFromDocIdString", "DocId String: " + docIdString);
/*     */     }
/*     */     
/*  94 */     if (docIdString != null) {
/*  95 */       String[] splitFolderId = docIdString.split(",");
/*  96 */       if (splitFolderId.length >= 3) {
/*  97 */         return splitFolderId[1];
/*     */       }
/*     */     }
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public static String getObjectIdentity(String id) {
/* 104 */     int firstSeparator = id.indexOf(ARRAY_DELIMITER);
/* 105 */     if (firstSeparator == -1)
/* 106 */       return id;
/* 107 */     int secondSeparator = id.indexOf(ARRAY_DELIMITER, firstSeparator + 1);
/* 108 */     if (secondSeparator == -1) {
/* 109 */       return id;
/*     */     }
/* 111 */     String objectIdentity = id.substring(secondSeparator + 1);
/* 112 */     return objectIdentity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getClassFromDocIdString(String docIdString)
/*     */   {
/* 122 */     if (Logger.isDebugLogged()) {
/* 123 */       Logger.logDebug("IERUtil", "getIdFromDocIdString", "DocId String: " + docIdString);
/*     */     }
/*     */     
/* 126 */     String[] splitFolderId = docIdString.split(",");
/* 127 */     if (splitFolderId.length >= 3) {
/* 128 */       return splitFolderId[0];
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String arrayToString(Object[] array)
/*     */   {
/* 139 */     StringBuffer stringBuffer = new StringBuffer();
/* 140 */     for (int i = 0; i < array.length; i++) {
/* 141 */       String escapedValue = PropertyConverter.escapeDelimiters(array[i].toString(), ARRAY_DELIMITER);
/* 142 */       stringBuffer.append(escapedValue);
/* 143 */       if (i + 1 < array.length) {
/* 144 */         stringBuffer.append(ARRAY_DELIMITER);
/*     */       }
/*     */     }
/* 147 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String StringListToString(List<String> list)
/*     */   {
/* 156 */     StringBuffer stringBuffer = new StringBuffer();
/* 157 */     for (int i = 0; i < list.size(); i++) {
/* 158 */       String escapedValue = PropertyConverter.escapeDelimiters((String)list.get(i), ARRAY_DELIMITER);
/* 159 */       stringBuffer.append(escapedValue);
/* 160 */       if (i + 1 < list.size()) {
/* 161 */         stringBuffer.append(ARRAY_DELIMITER);
/*     */       }
/*     */     }
/* 164 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> convertStringsToListString(String str)
/*     */   {
/* 173 */     String[] tokens = str.split(",");
/* 174 */     List<String> list = new ArrayList();
/* 175 */     for (String token : tokens) {
/* 176 */       list.add(token.trim());
/*     */     }
/* 178 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getNameProperty(HttpServletRequest request, boolean forFolder)
/*     */   {
/* 185 */     RepositoryConfig repositoryConfig = com.ibm.ecm.configuration.Config.getRepositoryConfig(request);
/* 186 */     String nameProperty = null;
/* 187 */     if (repositoryConfig != null) {
/* 188 */       if (forFolder) {
/* 189 */         nameProperty = repositoryConfig.getFolderNameProperty();
/*     */       } else {
/* 191 */         nameProperty = repositoryConfig.getDocNameProperty();
/*     */       }
/*     */     }
/*     */     
/* 195 */     if (nameProperty == null) {
/* 196 */       nameProperty = forFolder ? "FolderName" : "DocumentTitle";
/*     */     }
/*     */     
/* 199 */     return nameProperty;
/*     */   }
/*     */   
/*     */   public static boolean isNullOrEmpty(String value) {
/* 203 */     return (value == null) || (value.isEmpty());
/*     */   }
/*     */   
/*     */   public static int getSearchPagingSize(HttpServletRequest request, RMContentSearchDefinition cbrDefinition) {
/* 207 */     int pageSize = 200;
/* 208 */     if ((cbrDefinition != null) && (cbrDefinition.getContentSearchOption() == RMContentSearchDefinition.ContentSearchOption.content) && 
/* 209 */       (request != null)) {
/*     */       try {
/* 211 */         DesktopConfig config = com.ibm.ier.plugin.configuration.Config.getDesktopConfig(request.getParameter("desktop"));
/* 212 */         if ((config != null) && (config.getCBRPageSize() != null)) {
/*     */           try {
/* 214 */             int cbrPageSize = Integer.parseInt(config.getCBRPageSize());
/* 215 */             pageSize = cbrPageSize;
/*     */           }
/*     */           catch (NumberFormatException usedefaultsize_ignored) {}
/*     */         }
/*     */       }
/*     */       catch (Exception ignored_usedefaultsize) {}
/*     */     }
/*     */     
/* 223 */     return pageSize;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\IERUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */