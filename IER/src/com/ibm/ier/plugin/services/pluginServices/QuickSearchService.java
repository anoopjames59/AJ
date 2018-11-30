/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Record;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QuickSearchService
/*     */   extends IERBasePluginService
/*     */ {
/*  60 */   private List<String> folderProps = Arrays.asList(new String[] { "FolderName", "Name", "AGGREGATION", "Reviewer", "CurrentPhaseExecutionDate", "LockToken", "LockTimeout", "DateLastModified", "LastModifier", "Creator", "DateCreated", "Id", "FolderName", "OnHold", "CurrentPhaseExecutionStatus", "DateClosed" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private List<String> recordProps = Arrays.asList(new String[] { "DocumentTitle", "Name", "AGGREGATION", "Reviewer", "CurrentPhaseExecutionDate", "LockToken", "LockTimeout", "DateLastModified", "LockOwner", "Id", "DocumentTitle", "ClassDescription", "Creator", "DateCreated", "MimeType", "LastModifier", "IsReserved", "VersionSeries", "MajorVersionNumber", "MinorVersionNumber", "OnHold" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/*  75 */     return "ierGetQuickSearchResults";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  79 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  81 */     String repositoryId = request.getParameter("repositoryId");
/*  82 */     String fileplanId = IERUtil.getIdFromDocIdString(request.getParameter("ier_fileplanId"));
/*  83 */     String queryString = request.getParameter("ier_quickSearchQueryString");
/*  84 */     String startPoint = request.getParameter("ier_quickSearchStartingPoint");
/*  85 */     String moreType = request.getParameter("ier_quickSearchMoreType");
/*     */     
/*  87 */     JSONObject jsonResponse = new JSONObject();
/*  88 */     FilePlanRepository fp_repo = getFilePlanRepository();
/*     */     
/*  90 */     Integer topSizeLimit = Integer.valueOf(5);
/*  91 */     Integer recordTopSizeLimit = topSizeLimit;
/*  92 */     Integer rcTopSizeLimit = topSizeLimit;
/*  93 */     Integer rfTopSizeLimit = topSizeLimit;
/*  94 */     Integer rvTopSizeLimit = topSizeLimit;
/*     */     
/*  96 */     JSONArray itemsJSON = new JSONArray();
/*     */     
/*  98 */     if (moreType != null)
/*  99 */       if (moreType.equals(EntityType.Record.toString())) {
/* 100 */         int startPointValue = Integer.parseInt(startPoint);
/* 101 */         int topSizeValue = topSizeLimit.intValue();
/* 102 */         recordTopSizeLimit = new Integer(startPointValue + topSizeValue);
/*     */ 
/*     */       }
/* 105 */       else if (moreType.equals(EntityType.RecordCategory.toString())) {
/* 106 */         rcTopSizeLimit = Integer.valueOf(Integer.parseInt(startPoint) + topSizeLimit.intValue());
/* 107 */       } else if (moreType.equals(EntityType.RecordFolder.toString())) {
/* 108 */         rfTopSizeLimit = Integer.valueOf(Integer.parseInt(startPoint) + topSizeLimit.intValue());
/* 109 */       } else if (moreType.equals(EntityType.RecordVolume.toString())) {
/* 110 */         rvTopSizeLimit = Integer.valueOf(Integer.parseInt(startPoint) + topSizeLimit.intValue());
/*     */       }
/* 112 */     List<BaseEntity> records = getRecords(fp_repo, queryString, fileplanId, recordTopSizeLimit.intValue());
/* 113 */     if (!records.isEmpty())
/*     */     {
/* 115 */       for (BaseEntity entity : records) {
/* 116 */         itemsJSON.add(getEntityJSONObject(repositoryId, fileplanId, entity));
/*     */       }
/* 118 */       if (records.size() >= recordTopSizeLimit.intValue()) {
/* 119 */         itemsJSON.add(getMoreJSONObject(repositoryId, EntityType.Record, recordTopSizeLimit.intValue()));
/*     */       }
/*     */     }
/* 122 */     List<BaseEntity> categories = getRecordFolders(fp_repo, EntityType.RecordCategory, queryString, fileplanId, rcTopSizeLimit.intValue());
/* 123 */     if (!categories.isEmpty())
/*     */     {
/* 125 */       for (BaseEntity entity : categories) {
/* 126 */         itemsJSON.add(getEntityJSONObject(repositoryId, fileplanId, entity));
/*     */       }
/* 128 */       if (categories.size() >= rcTopSizeLimit.intValue()) {
/* 129 */         itemsJSON.add(getMoreJSONObject(repositoryId, EntityType.RecordCategory, rcTopSizeLimit.intValue()));
/*     */       }
/*     */     }
/* 132 */     List<BaseEntity> recordFolders = getRecordFolders(fp_repo, EntityType.RecordFolder, queryString, fileplanId, rfTopSizeLimit.intValue());
/* 133 */     if (!recordFolders.isEmpty())
/*     */     {
/* 135 */       for (BaseEntity entity : recordFolders) {
/* 136 */         itemsJSON.add(getEntityJSONObject(repositoryId, fileplanId, entity));
/*     */       }
/* 138 */       if (recordFolders.size() >= rfTopSizeLimit.intValue()) {
/* 139 */         itemsJSON.add(getMoreJSONObject(repositoryId, EntityType.RecordFolder, rfTopSizeLimit.intValue()));
/*     */       }
/*     */     }
/* 142 */     List<BaseEntity> recordVolumes = getRecordFolders(fp_repo, EntityType.RecordVolume, queryString, fileplanId, rvTopSizeLimit.intValue());
/* 143 */     if (!recordVolumes.isEmpty())
/*     */     {
/* 145 */       for (BaseEntity entity : recordVolumes) {
/* 146 */         itemsJSON.add(getEntityJSONObject(repositoryId, fileplanId, entity));
/*     */       }
/* 148 */       if (recordVolumes.size() >= rvTopSizeLimit.intValue())
/* 149 */         itemsJSON.add(getMoreJSONObject(repositoryId, EntityType.RecordVolume, rvTopSizeLimit.intValue()));
/*     */     }
/* 151 */     jsonResponse.put("identifier", "id");
/* 152 */     jsonResponse.put("items", itemsJSON);
/*     */     
/*     */ 
/* 155 */     setCompletedJSONResponseObject(jsonResponse);
/*     */   }
/*     */   
/*     */   private JSONObject getMoreJSONObject(String repositoryId, EntityType type, int sizeLimit) {
/* 159 */     JSONObject object = new JSONObject();
/* 160 */     object.put("repositoryId", repositoryId);
/* 161 */     object.put("name", "More " + type.toString() + " ...");
/* 162 */     object.put("title", "\tMore " + type.toString() + " ...");
/* 163 */     object.put("id", type.toString() + "more");
/* 164 */     object.put("type", type.toString());
/* 165 */     object.put("isMore", Boolean.valueOf(true));
/* 166 */     object.put("endPoint", Integer.valueOf(sizeLimit));
/* 167 */     return object;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private JSONObject getEntityJSONObject(String repositoryId, String fileplanId, BaseEntity entity)
/*     */   {
/* 189 */     EntityType type = entity.getEntityType();
/* 190 */     JSONObject object = new JSONObject();
/* 191 */     object.put("repositoryId", repositoryId);
/* 192 */     object.put("name", entity.getName());
/* 193 */     object.put("id", entity.getObjectIdentity());
/* 194 */     object.put("docid", IERUtil.getDocId(entity));
/* 195 */     object.put("entityType", type.toString());
/*     */     
/* 197 */     if ((type == EntityType.Record) || (type == EntityType.ElectronicRecord) || (type == EntityType.PhysicalRecord)) {
/* 198 */       Record record = (Record)entity;
/* 199 */       object.put("content", "Record Class: " + entity.getEntityType() + " | Id: " + record.getObjectIdentity());
/* 200 */       object.put("iconClass", "electronicRecordIcon");
/* 201 */       object.put("type", "record");
/* 202 */       object.put("title", entity.getName());
/*     */     }
/*     */     else
/*     */     {
/* 206 */       Container folder = (Container)entity;
/* 207 */       object.put("content", "Folder Type: " + entity.getEntityType() + " | Id: " + entity.getObjectIdentity() + " | Path: " + folder.getPathName());
/* 208 */       object.put("title", entity.getName());
/*     */       
/* 210 */       if (type == EntityType.RecordCategory) {
/* 211 */         object.put("iconClass", "recordCategoryIcon");
/* 212 */       } else if ((type == EntityType.RecordFolder) || (type == EntityType.PhysicalRecordFolder) || (type == EntityType.HybridRecordFolder) || (type == EntityType.ElectronicRecordFolder)) {
/* 213 */         object.put("iconClass", "recordElectronicFolderIcon");
/* 214 */       } else if (type == EntityType.RecordVolume) {
/* 215 */         object.put("iconClass", "volumeIcon");
/*     */       }
/* 217 */       object.put("type", entity.getEntityType().toString());
/*     */     }
/* 219 */     return object;
/*     */   }
/*     */   
/*     */   private List<BaseEntity> getRecordFolders(FilePlanRepository fp_repo, EntityType type, String entityName, String fileplanId, int topLimit)
/*     */   {
/* 224 */     IERQuery q = new IERQuery();
/* 225 */     q.setTopLimitSize(topLimit);
/* 226 */     if (type == EntityType.RecordCategory) {
/* 227 */       q.setFromClause("RecordCategory");
/* 228 */     } else if (type == EntityType.RecordFolder) {
/* 229 */       q.setFromClause("RecordFolder");
/* 230 */     } else if (type == EntityType.RecordVolume) {
/* 231 */       q.setFromClause("Volume");
/*     */     } else
/* 233 */       q.setFromClause("RMFolder");
/* 234 */     q.setRequestedProperties(this.folderProps);
/* 235 */     q.setRepository(fp_repo);
/* 236 */     q.setWhereClause("This INSUBFOLDER ('" + fileplanId + "') AND FolderName like '%" + entityName + "%'");
/* 237 */     Iterator<BaseEntity> foldersIterator = q.executeQueryAsObjectsIterator(type);
/* 238 */     List<BaseEntity> entities = toList(foldersIterator, topLimit);
/* 239 */     return entities;
/*     */   }
/*     */   
/*     */   private List<BaseEntity> getRecords(FilePlanRepository fp_repo, String entityName, String fileplanId, int topLimit)
/*     */   {
/* 244 */     IERQuery q = new IERQuery();
/* 245 */     q.setTopLimitSize(topLimit);
/* 246 */     q.setFromClause("RecordInfo");
/* 247 */     q.setRequestedProperties(this.recordProps);
/* 248 */     q.setRepository(fp_repo);
/* 249 */     q.setWhereClause("This INSUBFOLDER ('" + fileplanId + "') AND DocumentTitle like '%" + entityName + "%'");
/* 250 */     Iterator<BaseEntity> foldersIterator = q.executeQueryAsObjectsIterator(EntityType.Record);
/* 251 */     List<BaseEntity> entities = toList(foldersIterator, topLimit);
/* 252 */     return entities;
/*     */   }
/*     */   
/*     */   private List<BaseEntity> toList(Iterator<BaseEntity> iterator, int topSizeLimit) {
/* 256 */     List<BaseEntity> list = new ArrayList(topSizeLimit);
/* 257 */     while (iterator.hasNext()) {
/* 258 */       list.add(iterator.next());
/*     */     }
/* 260 */     return list;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\QuickSearchService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */