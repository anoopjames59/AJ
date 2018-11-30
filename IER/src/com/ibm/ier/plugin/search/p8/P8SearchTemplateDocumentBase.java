/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.collection.ContentElementList;
/*     */ import com.filenet.api.constants.AutoClassify;
/*     */ import com.filenet.api.constants.AutoUniqueName;
/*     */ import com.filenet.api.constants.CheckinType;
/*     */ import com.filenet.api.constants.DefineSecurityParentage;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.constants.SpecialPrincipal;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.core.Factory.Document;
/*     */ import com.filenet.api.core.Factory.User;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*     */ import com.filenet.api.meta.ClassDescription;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.filenet.api.security.User;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase;
/*     */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Iterator;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ public abstract class P8SearchTemplateDocumentBase
/*     */ {
/*     */   public static final String STORED_SEARCH_MIMETYPE = "application/x-filenet-searchtemplate";
/*     */   public static final String MIMETYPE_JSON = "application/json";
/*     */   protected static final String UTF8 = "UTF-8";
/*     */   protected static final int SEARCHING_OBJECT_TYPE_DOCUMENT = 1;
/*     */   protected static final int SEARCHING_OBJECT_TYPE_FOLDER = 2;
/*     */   protected static final int SEARCH_TYPE_STORED_SEARCH = 1;
/*     */   protected static final int SEARCH_TYPE_SEARCH_TEMPLATE = 2;
/*     */   protected static final int SEARCH_TYPE_UNIFIED_SEARCH = 1;
/*     */   protected static final String PROPERTY_SEARCH_TYPE = "SearchType";
/*     */   protected static final String PROPERTY_SEARCHING_OBJECT_TYPE = "SearchingObjectType";
/*     */   protected static final String PROPERTY_SEARCHING_OBJECT_STORES = "SearchingObjectStores";
/*     */   protected static final String PROPERTY_APPLICATION_NAME = "ApplicationName";
/*     */   protected static final String PROPERTY_CM_SEARCH_SCHEMA_VERSION = "CmSearchSchemaVersion";
/*     */   protected static final String PROPERTY_SEARCHING_REPOSITORIES = "IcnSearchingRepositories";
/*     */   public static final String CLASS_STORED_SEARCH = "StoredSearch";
/*     */   public static final String CLASS_UNIFIED_SEARCH = "IcnSearch";
/*     */   protected HttpServletRequest request;
/*     */   protected P8Connection connection;
/*     */   protected String storedSearchId;
/*     */   protected String storedSearchVsId;
/*     */   protected String teamspaceId;
/*     */   protected Document document;
/*     */   protected SearchTemplateBase searchTemplate;
/*     */   
/*     */   public P8SearchTemplateDocumentBase(HttpServletRequest request, P8Connection connection, String storedSearchId, String storedSearchVsId, String teamspaceId)
/*     */   {
/*  83 */     this.request = request;
/*  84 */     this.connection = connection;
/*  85 */     this.storedSearchId = storedSearchId;
/*  86 */     this.storedSearchVsId = storedSearchVsId;
/*  87 */     this.teamspaceId = teamspaceId;
/*     */   }
/*     */   
/*     */   protected boolean isSearchTemplateIdSet() {
/*  91 */     return ((this.storedSearchId != null) && (!this.storedSearchId.isEmpty())) || ((this.storedSearchVsId != null) && (!this.storedSearchVsId.isEmpty()));
/*     */   }
/*     */   
/*     */   public SearchTemplateBase getSearchTemplate(JSONObject searchTemplateJson, boolean loadSearchDefinition) throws Exception {
/*  95 */     return getSearchTemplate(searchTemplateJson, loadSearchDefinition, false);
/*     */   }
/*     */   
/*     */   public SearchTemplateBase getSearchTemplate(boolean autoResolve) throws Exception {
/*  99 */     return getSearchTemplate(null, true, autoResolve);
/*     */   }
/*     */   
/*     */   public abstract Document save(String paramString1, String paramString2, JSONObject paramJSONObject) throws Exception;
/*     */   
/*     */   public abstract Document saveAs(String paramString1, String paramString2, JSONObject paramJSONObject, String paramString3, AccessPermissionList paramAccessPermissionList) throws Exception;
/*     */   
/*     */   public Document save(String name, String description, JSONObject searchTemplateJson, boolean isUnifiedSearch) throws Exception {
/* 107 */     if (searchTemplateJson != null) {
/* 108 */       getSearchTemplate(searchTemplateJson, false);
/*     */     }
/* 110 */     Document checkedOutDoc = (Document)this.document.get_Reservation();
/* 111 */     if (checkedOutDoc == null) {
/* 112 */       return null;
/*     */     }
/*     */     
/* 115 */     int access = checkedOutDoc.getAccessAllowed().intValue();
/* 116 */     if ((access & 0x4) != 4) {
/* 117 */       throw new IllegalAccessException();
/*     */     }
/*     */     
/* 120 */     if (isUnifiedSearch) {
/* 121 */       checkedOutDoc.set_MimeType("application/x-unifiedsearchtemplate");
/*     */     }
/*     */     
/* 124 */     Properties properties = checkedOutDoc.getProperties();
/* 125 */     properties.putObjectValue("DocumentTitle", name);
/* 126 */     properties.putObjectValue("Description", description);
/* 127 */     properties.putValue("IcnAutoRun", this.searchTemplate.isAutoRun());
/* 128 */     properties.putValue("IcnShowInTree", this.searchTemplate.isShowInTree());
/*     */     
/* 130 */     if (!isUnifiedSearch) {
/* 131 */       properties.putValue("SearchingObjectType", getSearchObjectType());
/* 132 */       properties.putValue("SearchingObjectStores", this.connection.getObjectStore().get_SymbolicName());
/* 133 */       properties.putValue("SearchType", this.searchTemplate.isAutoRun() ? 1 : 2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */     checkedOutDoc.set_ContentElements(createContentElements());
/*     */     
/*     */ 
/* 143 */     checkedOutDoc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/* 144 */     checkedOutDoc.save(RefreshMode.REFRESH);
/*     */     
/* 146 */     return checkedOutDoc;
/*     */   }
/*     */   
/*     */   public Document saveAs(String templateName, String description, JSONObject searchTemplateJson, String folderPID, AccessPermissionList permissions, boolean isUnifiedSearch) throws Exception
/*     */   {
/* 151 */     if (searchTemplateJson != null) {
/* 152 */       this.searchTemplate = getSearchTemplate(searchTemplateJson, false);
/*     */     }
/* 154 */     ObjectStore objectStore = this.connection.getObjectStore();
/* 155 */     ClassDescription classDescription = null;
/* 156 */     String mimeType = "application/x-filenet-searchtemplate";
/* 157 */     if (isUnifiedSearch) {
/* 158 */       classDescription = P8Util.getCachedCD(objectStore, "IcnSearch");
/* 159 */       mimeType = "application/x-unifiedsearchtemplate";
/*     */     } else {
/* 161 */       classDescription = P8Util.getCachedCD(objectStore, "StoredSearch");
/*     */     }
/*     */     
/* 164 */     Id id = classDescription.get_Id();
/* 165 */     Document document = Factory.Document.createInstance(objectStore, id.toString());
/* 166 */     document.set_MimeType(mimeType);
/*     */     
/* 168 */     Properties documentProperties = document.getProperties();
/* 169 */     documentProperties.putValue("DocumentTitle", templateName);
/* 170 */     documentProperties.putValue("Description", description);
/* 171 */     documentProperties.putValue("IcnAutoRun", this.searchTemplate.isAutoRun());
/* 172 */     documentProperties.putValue("IcnShowInTree", this.searchTemplate.isShowInTree());
/*     */     
/* 174 */     if (!isUnifiedSearch) {
/* 175 */       documentProperties.putValue("SearchingObjectType", getSearchObjectType());
/* 176 */       documentProperties.putValue("SearchingObjectStores", this.connection.getObjectStore().get_SymbolicName());
/* 177 */       String applicationName = (String)searchTemplateJson.get("application");
/* 178 */       documentProperties.putValue("ApplicationName", applicationName != null ? applicationName : "Navigator");
/* 179 */       documentProperties.putValue("SearchType", this.searchTemplate.isAutoRun() ? 1 : 2);
/*     */       
/* 181 */       if (documentProperties.isPropertyPresent("CmSearchSchemaVersion")) {
/* 182 */         documentProperties.putValue("CmSearchSchemaVersion", 3);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 189 */     document.set_ContentElements(createContentElements());
/*     */     
/*     */ 
/* 192 */     document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/*     */     
/*     */ 
/* 195 */     AccessPermissionList permissionAPL = Factory.AccessPermission.createList();
/* 196 */     AccessPermissionList defaultInstancePermissionsList = classDescription.get_DefaultInstancePermissions();
/*     */     
/* 198 */     Iterator it_apl = defaultInstancePermissionsList.iterator();
/* 199 */     while (it_apl.hasNext()) {
/* 200 */       AccessPermission ap = (AccessPermission)it_apl.next();
/*     */       
/* 202 */       if (ap.get_GranteeName().equalsIgnoreCase(SpecialPrincipal.CREATOR_OWNER.getValue())) {
/* 203 */         AccessPermission newAp = Factory.AccessPermission.createInstance();
/* 204 */         newAp.set_AccessMask(ap.get_AccessMask());
/* 205 */         newAp.set_AccessType(ap.get_AccessType());
/* 206 */         newAp.set_InheritableDepth(ap.get_InheritableDepth());
/* 207 */         User currentUser = Factory.User.fetchCurrent(this.connection.getCEConnection(), null);
/* 208 */         newAp.set_GranteeName(currentUser.get_ShortName());
/* 209 */         ap = newAp;
/*     */       }
/*     */       
/*     */ 
/* 213 */       if (!ap.get_GranteeName().equalsIgnoreCase(SpecialPrincipal.AUTHENTICATED_USERS.getValue())) {
/* 214 */         permissionAPL.add(ap);
/*     */       }
/*     */     }
/*     */     
/* 218 */     if (permissions != null) {
/* 219 */       permissionAPL.addAll(permissions);
/*     */     }
/* 221 */     document.set_Permissions(permissionAPL);
/* 222 */     document.save(RefreshMode.REFRESH);
/*     */     
/* 224 */     if ((folderPID != null) && (folderPID.length() > 0)) {
/* 225 */       Folder folder = P8Util.getFolder(this.request, this.connection, folderPID);
/* 226 */       ReferentialContainmentRelationship rcr = folder.file(document, AutoUniqueName.AUTO_UNIQUE, null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
/* 227 */       rcr.save(RefreshMode.NO_REFRESH);
/*     */     }
/*     */     
/* 230 */     return document;
/*     */   }
/*     */   
/*     */   public void loadSearchDefinitionDocument(boolean loadContent, boolean isUnifiedSearch) {
/* 234 */     boolean released = (this.storedSearchVsId != null) && (!this.storedSearchVsId.isEmpty());
/* 235 */     PropertyFilter pf = new PropertyFilter();
/* 236 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Id", null));
/* 237 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Name", null));
/* 238 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Description", null));
/* 239 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IcnAutoRun", null));
/* 240 */     pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IcnShowInTree", null));
/*     */     
/* 242 */     if (isUnifiedSearch) {
/* 243 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "IcnSearchingRepositories", null));
/*     */     }
/* 245 */     if (released) {
/* 246 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "ReleasedVersion", null));
/* 247 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Reservation", null));
/* 248 */       if (loadContent) {
/* 249 */         pf.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "ContentElements", null));
/* 250 */         pf.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "Content", null));
/* 251 */         pf.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "ContentType", null));
/*     */       }
/*     */     } else {
/* 254 */       pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Reservation", null));
/* 255 */       if (loadContent) {
/* 256 */         pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "ContentElements", null));
/* 257 */         pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Content", null));
/* 258 */         pf.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "ContentType", null));
/*     */       }
/*     */     }
/*     */     
/* 262 */     String version = released ? "released" : null;
/* 263 */     this.document = P8Util.getDocument(this.request, this.connection, new P8DocID(this.storedSearchId), this.storedSearchVsId, version, pf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getSearchObjectType()
/*     */   {
/* 273 */     SearchTemplateBase.ObjectType objectType = ((P8SearchTemplate)this.searchTemplate).getObjectType();
/* 274 */     if (objectType != null) {
/* 275 */       if (objectType == SearchTemplateBase.ObjectType.document)
/* 276 */         return 1;
/* 277 */       if (objectType == SearchTemplateBase.ObjectType.folder)
/* 278 */         return 2;
/*     */     }
/* 280 */     return -1;
/*     */   }
/*     */   
/*     */   public abstract SearchTemplateBase getSearchTemplate(JSONObject paramJSONObject, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract ContentElementList createContentElements()
/*     */     throws Exception;
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8SearchTemplateDocumentBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */