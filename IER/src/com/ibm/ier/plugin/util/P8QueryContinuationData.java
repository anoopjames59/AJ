/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.filenet.api.core.Document;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.AndOrOper;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.ContentSearchOption;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.OrderBy;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.SortOrder;
/*     */ import com.ibm.jarm.ral.p8ce.cbr.P8CE_RMContentSearchDefinition;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.codec.digest.DigestUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8QueryContinuationData
/*     */ {
/*     */   private static final String SALT = "za1uve3uzera2evu41pr";
/*  22 */   public boolean isWorkBasketData = false;
/*     */   
/*     */   public String sessionKey;
/*     */   
/*     */   public String documentSQL;
/*     */   
/*     */   public String folderSQL;
/*     */   public boolean returnOnlyFolders;
/*     */   public boolean descending;
/*     */   public String objectStoreId;
/*     */   public String folderId;
/*     */   public int itemsToSkip;
/*     */   public boolean orderByRank;
/*     */   public boolean mergeUnion;
/*     */   public boolean queriedAll;
/*     */   public byte[] checkpoint;
/*     */   public Document ceStoredSearch;
/*     */   public Object ceDocumentSearchParams;
/*     */   public Object ceFolderSearchParams;
/*     */   public String properties;
/*     */   public String applicationSpaceName;
/*     */   public String processRoleName;
/*     */   public String queueName;
/*     */   public String inbasketName;
/*     */   public String lastWorkRecord;
/*     */   public String orderBy;
/*  48 */   public boolean isIER = false;
/*     */   public P8CE_RMContentSearchDefinition rmCBRDef;
/*     */   public String searchClassNames;
/*     */   
/*     */   public P8QueryContinuationData(String serializedForm, boolean isWorkBasketData) throws UnsupportedEncodingException
/*     */   {
/*  54 */     this.isWorkBasketData = isWorkBasketData;
/*  55 */     String[] parts = StringUtils.splitPreserveAllTokens(serializedForm, '*');
/*  56 */     this.sessionKey = dec(parts[0]);
/*  57 */     String checksum = "";
/*     */     
/*  59 */     this.documentSQL = dec(parts[1]);
/*  60 */     this.folderSQL = dec(parts[2]);
/*  61 */     this.returnOnlyFolders = decBool(parts[3]);
/*  62 */     this.descending = decBool(parts[4]);
/*  63 */     this.objectStoreId = dec(parts[5]);
/*  64 */     this.folderId = dec(parts[6]);
/*  65 */     this.itemsToSkip = Integer.parseInt(parts[7]);
/*  66 */     this.orderByRank = decBool(parts[8]);
/*  67 */     this.mergeUnion = decBool(parts[9]);
/*  68 */     this.queriedAll = decBool(parts[10]);
/*  69 */     this.checkpoint = decBytes(parts[11]);
/*  70 */     this.properties = dec(parts[12]);
/*  71 */     this.searchClassNames = dec(parts[13]);
/*     */     
/*  73 */     this.rmCBRDef = ((P8CE_RMContentSearchDefinition)decObject(parts[14], new P8CE_RMContentSearchDefinition()));
/*     */     
/*  75 */     if (parts[15] != null) {
/*  76 */       this.isIER = dec(parts[15]).equals("isIERQuery");
/*     */     }
/*  78 */     checksum = parts[16];
/*     */     
/*  80 */     String calculatedChecksum = calculateChecksum();
/*  81 */     if (!StringUtils.equals(checksum, calculatedChecksum)) {
/*  82 */       throw new RuntimeException("Invalid checksum!");
/*     */     }
/*     */   }
/*     */   
/*     */   public P8QueryContinuationData() {}
/*     */   
/*     */   public static boolean isIERQuery(String serializedForm) throws Exception
/*     */   {
/*  90 */     String[] parts = StringUtils.splitPreserveAllTokens(serializedForm, '*');
/*  91 */     if ((parts.length > 15) && (parts[15] != null)) {
/*  92 */       String decode = dec(parts[15]);
/*  93 */       if (decode != null)
/*  94 */         return decode.equals("isIERQuery");
/*     */     }
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   public String saveToString() throws UnsupportedEncodingException {
/* 100 */     List<String> parts = new ArrayList();
/* 101 */     parts.add(enc(this.sessionKey));
/* 102 */     parts.add(enc(this.documentSQL));
/* 103 */     parts.add(enc(this.folderSQL));
/* 104 */     parts.add(enc(this.returnOnlyFolders));
/* 105 */     parts.add(enc(this.descending));
/* 106 */     parts.add(enc(this.objectStoreId));
/* 107 */     parts.add(enc(this.folderId));
/* 108 */     parts.add(Integer.toString(this.itemsToSkip));
/* 109 */     parts.add(enc(this.orderByRank));
/* 110 */     parts.add(enc(this.mergeUnion));
/* 111 */     parts.add(enc(this.queriedAll));
/* 112 */     parts.add(enc(this.checkpoint));
/* 113 */     parts.add(enc(this.properties));
/* 114 */     parts.add(enc(this.searchClassNames));
/*     */     
/* 116 */     parts.add(enc(this.rmCBRDef));
/*     */     
/* 118 */     parts.add(enc("isIERQuery"));
/*     */     
/* 120 */     String checksum = calculateChecksum();
/* 121 */     parts.add(checksum);
/* 122 */     String result = StringUtils.join(parts.iterator(), '*');
/* 123 */     return result;
/*     */   }
/*     */   
/*     */   protected static String dec(String encoded) throws UnsupportedEncodingException {
/* 127 */     if (StringUtils.isEmpty(encoded)) {
/* 128 */       return "";
/*     */     }
/* 130 */     return new String(Base64.decodeBase64(encoded), "UTF-8");
/*     */   }
/*     */   
/*     */   private static boolean decBool(String encoded) {
/* 134 */     return StringUtils.equals(encoded, "1");
/*     */   }
/*     */   
/*     */   private static byte[] decBytes(String encoded) {
/* 138 */     if (StringUtils.isEmpty(encoded)) {
/* 139 */       return null;
/*     */     }
/* 141 */     return Base64.decodeBase64(encoded);
/*     */   }
/*     */   
/*     */   private static Object decObject(String encoded, Object obj) throws UnsupportedEncodingException {
/* 145 */     if ((StringUtils.isEmpty(encoded)) || (obj == null)) {
/* 146 */       return null;
/*     */     }
/*     */     
/* 149 */     if ((obj instanceof P8CE_RMContentSearchDefinition))
/*     */     {
/* 151 */       P8CE_RMContentSearchDefinition cbrDef = (P8CE_RMContentSearchDefinition)obj;
/* 152 */       String[] parts = StringUtils.splitPreserveAllTokens(encoded, '#');
/* 153 */       cbrDef.setSelectClause(dec(parts[0]));
/* 154 */       cbrDef.setFromClause(dec(parts[1]));
/* 155 */       cbrDef.setWhereClause(dec(parts[2]));
/* 156 */       cbrDef.setOrderClause(dec(parts[3]));
/* 157 */       cbrDef.setSqlAlias(dec(parts[4]));
/* 158 */       cbrDef.setContentSearch(dec(parts[5]));
/*     */       try
/*     */       {
/* 161 */         cbrDef.setSortOrder(RMContentSearchDefinition.SortOrder.valueOf(dec(parts[6])));
/*     */       } catch (IllegalArgumentException ex) {
/* 163 */         cbrDef.setSortOrder(RMContentSearchDefinition.SortOrder.desc);
/*     */       }
/*     */       try
/*     */       {
/* 167 */         cbrDef.setOrderBy(RMContentSearchDefinition.OrderBy.valueOf(dec(parts[7])));
/*     */       } catch (IllegalArgumentException ex) {
/* 169 */         cbrDef.setOrderBy(RMContentSearchDefinition.OrderBy.cbrscores);
/*     */       }
/*     */       
/* 172 */       cbrDef.setCBRConditionOnly(decBool(parts[8]));
/* 173 */       cbrDef.setCommonWhereClause(dec(parts[9]));
/*     */       try
/*     */       {
/* 176 */         cbrDef.setOperBtwContentAndMetadataSearch(RMContentSearchDefinition.AndOrOper.valueOf(dec(parts[10])));
/*     */       } catch (IllegalArgumentException ex) {
/* 178 */         cbrDef.setOperBtwContentAndMetadataSearch(RMContentSearchDefinition.AndOrOper.and);
/*     */       }
/*     */       try
/*     */       {
/* 182 */         cbrDef.setContentSearchOption(RMContentSearchDefinition.ContentSearchOption.valueOf(dec(parts[11])));
/*     */       } catch (IllegalArgumentException ex) {
/* 184 */         cbrDef.setContentSearchOption(RMContentSearchDefinition.ContentSearchOption.content);
/*     */       }
/* 186 */       return cbrDef;
/*     */     }
/*     */     
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   private static String enc(String str) throws UnsupportedEncodingException {
/* 193 */     if (StringUtils.isEmpty(str)) {
/* 194 */       return "";
/*     */     }
/* 196 */     return Base64.encodeBase64String(str.getBytes("UTF-8")).trim();
/*     */   }
/*     */   
/*     */   private static String enc(byte[] bytes) {
/* 200 */     if (bytes == null) {
/* 201 */       return "";
/*     */     }
/* 203 */     return Base64.encodeBase64String(bytes).trim();
/*     */   }
/*     */   
/*     */   private static String enc(boolean bool) {
/* 207 */     return bool ? "1" : "0";
/*     */   }
/*     */   
/*     */   private static String enc(Object obj) throws UnsupportedEncodingException {
/* 211 */     if (obj == null) return "";
/* 212 */     if ((obj instanceof P8CE_RMContentSearchDefinition)) {
/* 213 */       P8CE_RMContentSearchDefinition cbrDef = (P8CE_RMContentSearchDefinition)obj;
/* 214 */       List<String> parts = new ArrayList();
/* 215 */       parts.add(enc(cbrDef.getSelectClause()));
/* 216 */       parts.add(enc(cbrDef.getFromClause()));
/* 217 */       parts.add(enc(cbrDef.getWhereClause()));
/* 218 */       parts.add(enc(cbrDef.getOrderClause()));
/* 219 */       parts.add(enc(cbrDef.getSqlAlias()));
/* 220 */       parts.add(enc(cbrDef.getContentSearch()));
/* 221 */       parts.add(enc(cbrDef.getSortOrder()));
/* 222 */       parts.add(enc(cbrDef.getOrderBy()));
/* 223 */       parts.add(enc(cbrDef.isCBRConditionOnly()));
/* 224 */       parts.add(enc(cbrDef.getCommonWhereClause()));
/* 225 */       parts.add(enc(cbrDef.getOperBtwContentAndMetadataSearch()));
/* 226 */       parts.add(enc(cbrDef.getContentSearchOption()));
/*     */       
/* 228 */       String result = StringUtils.join(parts.iterator(), '#');
/* 229 */       return result;
/*     */     }
/* 231 */     return "";
/*     */   }
/*     */   
/*     */   private String calculateChecksum() {
/* 235 */     String str = "za1uve3uzera2evu41pr" + (this.sessionKey == null ? "" : this.sessionKey) + (this.checkpoint == null ? "" : enc(this.checkpoint));
/* 236 */     String checksumString = enc(DigestUtils.sha256(str));
/* 237 */     return checksumString;
/*     */   }
/*     */   
/*     */   public void setObjectStoreIds(String[] objectStoreIds) {
/* 241 */     for (int i = 0; i < objectStoreIds.length; i++) {
/* 242 */       if (i == 0) {
/* 243 */         this.objectStoreId = objectStoreIds[i];
/*     */       } else {
/* 245 */         this.objectStoreId = (this.objectStoreId + ":" + objectStoreIds[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String[] getObjectStoreIds() {
/* 251 */     return this.objectStoreId.split(":");
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\P8QueryContinuationData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */