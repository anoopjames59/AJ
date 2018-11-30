/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.admin.PropertyDefinition;
/*     */ import com.filenet.api.collection.ObjectStoreSet;
/*     */ import com.filenet.api.collection.PropertyDefinitionList;
/*     */ import com.filenet.api.constants.AccessRight;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.ClassDefinition;
/*     */ import com.filenet.api.core.Factory.Domain;
/*     */ import com.filenet.api.core.Factory.Group;
/*     */ import com.filenet.api.core.Factory.MetadataCache;
/*     */ import com.filenet.api.core.Factory.User;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.RetrievingBatch;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.meta.MetadataCache;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.security.Group;
/*     */ import com.filenet.api.security.SecurityPrincipal;
/*     */ import com.filenet.api.security.User;
/*     */ import com.filenet.api.util.UserContext;
/*     */ import com.filenet.apiimpl.meta.Cache;
/*     */ import com.ibm.ecm.serviceability.Logger;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
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
/*     */ public class P8Connection
/*     */   extends com.ibm.ier.plugin.search.util.Connection
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4970277270717424306L;
/*     */   private String userName;
/*     */   private String distinguishedName;
/*     */   private com.filenet.api.core.Connection ceConnection;
/*     */   private transient Subject _subject;
/*     */   private Domain domain;
/*     */   private ObjectStore objectStore;
/*     */   private String domainName;
/*     */   private String objectStoreName;
/*  65 */   private boolean objectStoreNotFound = false;
/*     */   private String objectStoreRecordType;
/*     */   private String objectStoreRecordDataModel;
/*  68 */   private String p8ConnectionType = null;
/*     */   private ArrayList<String> domainObjectStoreNames;
/*  70 */   private Map<String, ObjectStore> objectStores = new HashMap();
/*     */   
/*  72 */   private String privAddItem = null;
/*  73 */   private String privAddDoc = null;
/*  74 */   private String privAddSearch = null;
/*  75 */   private String privAddUnifiedSearch = null;
/*  76 */   private String privAddItemTeamspaceTemplate = null;
/*  77 */   private String privAddItemTeamspace = null;
/*  78 */   private String privWorkflow = null;
/*  79 */   private String privFindUsersAndGroups = null;
/*  80 */   private String appServerType = null;
/*  81 */   private boolean connected = false;
/*     */   
/*  83 */   private String searchTemplateSupported = null;
/*  84 */   private String unifiedSearchTemplateSupported = null;
/*  85 */   private Map<String, Boolean> osDocumentCommentSupported = new HashMap();
/*  86 */   private Map<String, Boolean> osDownloadCountSupported = new HashMap();
/*  87 */   private Map<String, User> users = new HashMap();
/*     */   public static final String TEAMSPACE_CLASS = "ClbTeamspace";
/*     */   public static final String TEAMSPACE_TEMPLATE_CLASS = "ClbTeamspaceTemplate";
/*     */   
/*     */   public P8Connection(String userId, String distinguishedName, com.filenet.api.core.Connection ceConnection, Subject subject, Domain domain, ObjectStore objectStore, String userName, String userDisplayName, String appServerType)
/*     */   {
/*  93 */     this.userId = userId;
/*  94 */     this.userName = userName;
/*  95 */     this.distinguishedName = distinguishedName;
/*  96 */     this.ceConnection = ceConnection;
/*  97 */     this._subject = subject;
/*  98 */     this.domain = domain;
/*  99 */     this.objectStore = objectStore;
/* 100 */     this.userDisplayName = userDisplayName;
/* 101 */     this.appServerType = appServerType;
/* 102 */     this.connected = true;
/* 103 */     this.type = "p8";
/*     */   }
/*     */   
/*     */   public P8Connection(String userId, String distinguishedName, com.filenet.api.core.Connection ceConnection, Subject subject, Domain domain, String objectStoreName, String userName, String userDisplayName) {
/* 107 */     this.userId = userId;
/* 108 */     this.userName = userName;
/* 109 */     this.distinguishedName = distinguishedName;
/* 110 */     this.ceConnection = ceConnection;
/* 111 */     this._subject = subject;
/* 112 */     this.domain = domain;
/* 113 */     this.objectStoreName = objectStoreName;
/* 114 */     this.userDisplayName = userDisplayName;
/* 115 */     this.type = "p8";
/*     */   }
/*     */   
/*     */   public P8Connection(String userId, String distinguishedName, com.filenet.api.core.Connection ceConnection, Subject subject, String domainName, String objectStoreName, String userName, String userDisplayName, String appServerType) {
/* 119 */     this.userId = userId;
/* 120 */     this.userName = userName;
/* 121 */     this.distinguishedName = distinguishedName;
/* 122 */     this.ceConnection = ceConnection;
/* 123 */     this._subject = subject;
/* 124 */     this.domain = null;
/* 125 */     this.objectStore = null;
/* 126 */     this.domainName = domainName;
/* 127 */     this.objectStoreName = objectStoreName;
/* 128 */     this.userDisplayName = userDisplayName;
/* 129 */     this.appServerType = appServerType;
/* 130 */     this.connected = true;
/* 131 */     this.type = "p8";
/*     */   }
/*     */   
/*     */   public P8Connection(String userId, String distinguishedName, com.filenet.api.core.Connection ceConnection, Subject subject, Domain domain, String objectStoreName, String userName, String userDisplayName, String appServerType) {
/* 135 */     this.userId = userId;
/* 136 */     this.userName = userName;
/* 137 */     this.distinguishedName = distinguishedName;
/* 138 */     this.ceConnection = ceConnection;
/* 139 */     this._subject = subject;
/* 140 */     this.domain = domain;
/* 141 */     this.objectStore = null;
/* 142 */     this.objectStoreName = objectStoreName;
/* 143 */     this.userDisplayName = userDisplayName;
/* 144 */     this.appServerType = appServerType;
/* 145 */     this.connected = true;
/* 146 */     this.type = "p8";
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 150 */     return this.connected;
/*     */   }
/*     */   
/*     */   private void establishP8Connection()
/*     */   {
/* 155 */     com.filenet.api.core.Connection connection = getCEConnection();
/* 156 */     if ((this.distinguishedName == null) || (this.userName == null) || (this.userDisplayName == null) || (this.domain == null))
/*     */     {
/*     */ 
/* 159 */       User user = Factory.User.fetchCurrent(connection, null);
/* 160 */       this.distinguishedName = user.get_DistinguishedName();
/* 161 */       this.userName = user.get_Name();
/* 162 */       this.userDisplayName = user.get_DisplayName();
/* 163 */       if (((this.domainName == null) || (this.domainName.length() == 0)) && (this.domain == null))
/*     */       {
/* 165 */         this.domain = Factory.Domain.fetchInstance(connection, null, null);
/* 166 */       } else if (this.domain == null) {
/* 167 */         this.domain = Factory.Domain.getInstance(connection, this.domainName);
/*     */       }
/*     */     }
/* 170 */     if (this.domain != null) {
/* 171 */       this.domain.fetchProperty("ObjectStores", null);
/* 172 */       ObjectStoreSet objectStoreSet = this.domain.get_ObjectStores();
/* 173 */       this.domainObjectStoreNames = new ArrayList();
/* 174 */       Iterator<ObjectStore> iterator = objectStoreSet.iterator();
/* 175 */       boolean permissionsSet = false;
/* 176 */       while (iterator.hasNext()) {
/* 177 */         ObjectStore store = (ObjectStore)iterator.next();
/* 178 */         this.domainObjectStoreNames.add(store.get_SymbolicName());
/* 179 */         if ((!permissionsSet) && (store.get_SymbolicName().equals(this.objectStoreName))) {
/* 180 */           this.objectStore = store;
/* 181 */           setPermissions(this.objectStore.getAccessAllowed());
/* 182 */           permissionsSet = true;
/*     */         }
/*     */       }
/*     */     }
/* 186 */     if (this.objectStore == null) {
/* 187 */       this.objectStoreNotFound = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPermissions(Integer accessRights)
/*     */   {
/* 199 */     boolean canAddDoc = (accessRights.intValue() & AccessRight.STORE_OBJECTS.getValue()) == AccessRight.STORE_OBJECTS.getValue();
/* 200 */     setPrivAddDoc(canAddDoc ? "true" : "false");
/* 201 */     setPrivAddItem(canAddDoc ? "true" : "false");
/*     */     
/*     */ 
/* 204 */     boolean canAddSearch = false;
/* 205 */     boolean canAddUnifiedSearch = false;
/* 206 */     boolean searchAddOnInstalled = false;
/* 207 */     boolean unifiedSearchAddOnInstalled = false;
/*     */     
/* 209 */     PropertyFilter filter = new PropertyFilter();
/* 210 */     filter.addIncludeProperty(0, null, null, "PropertyDefinitions", null);
/*     */     
/*     */     try
/*     */     {
/* 214 */       ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(this.objectStore, "StoredSearch", filter);
/* 215 */       PropertyDefinitionList properties = classDefinition.get_PropertyDefinitions();
/* 216 */       Iterator<PropertyDefinition> i = properties.iterator();
/* 217 */       while (i.hasNext()) {
/* 218 */         PropertyDefinition prop = (PropertyDefinition)i.next();
/* 219 */         if (prop.get_SymbolicName().equals("IcnShowInTree")) {
/* 220 */           searchAddOnInstalled = true;
/* 221 */           break;
/*     */         }
/*     */       }
/*     */       
/* 225 */       canAddSearch = (searchAddOnInstalled) && (canAddDoc) && ((classDefinition.getAccessAllowed().intValue() & 0x100) == 256);
/*     */     } catch (EngineRuntimeException e) {
/* 227 */       Logger.logError(P8Connection.class, "establishP8Connection", "StoredSearch class was not found", e);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 232 */       ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(this.objectStore, "IcnSearch", filter);
/* 233 */       unifiedSearchAddOnInstalled = true;
/* 234 */       canAddUnifiedSearch = (canAddDoc) && ((classDefinition.getAccessAllowed().intValue() & 0x100) == 256);
/*     */     }
/*     */     catch (EngineRuntimeException e) {}
/*     */     
/*     */ 
/* 239 */     boolean canAddTeamspace = false;
/* 240 */     boolean canAddTeamspaceTemplate = false;
/*     */     
/* 242 */     if (canAddDoc) {
/* 243 */       ClassDefinition classDefinition = null;
/*     */       try
/*     */       {
/* 246 */         classDefinition = Factory.ClassDefinition.fetchInstance(this.objectStore, "ClbTeamspace", filter);
/* 247 */         if ((classDefinition.getAccessAllowed().intValue() & 0x100) == 256)
/*     */         {
/* 249 */           canAddTeamspace = true;
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */ 
/* 258 */         Logger.logError(P8Connection.class, "establishP8Connection", "ClbTeamspace class was not found", e);
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 263 */         classDefinition = Factory.ClassDefinition.fetchInstance(this.objectStore, "ClbTeamspaceTemplate", filter);
/* 264 */         if ((classDefinition.getAccessAllowed().intValue() & 0x100) == 256)
/*     */         {
/* 266 */           canAddTeamspaceTemplate = true;
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */ 
/* 276 */         Logger.logError(P8Connection.class, "establishP8Connection", "ClbTeamspaceTemplate class was not found", e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 282 */     setPrivAddItemTeamspace(canAddTeamspace ? "true" : "false");
/* 283 */     setPrivAddItemTeamspaceTemplate(canAddTeamspaceTemplate ? "true" : "false");
/* 284 */     setPrivAddSearch(canAddSearch ? "true" : "false");
/* 285 */     setPrivAddUnifiedSearch(canAddUnifiedSearch ? "true" : "false");
/* 286 */     setSearchTemplateSupported(searchAddOnInstalled ? "true" : "false");
/* 287 */     setUnifiedSearchTemplateSupported(unifiedSearchAddOnInstalled ? "true" : "false");
/* 288 */     setPrivFindUsersAndGroups("true");
/*     */   }
/*     */   
/*     */   public void cleanUp(HttpSession session) {
/*     */     Subject sub;
/*     */     do {
/* 294 */       sub = null;
/* 295 */       sub = UserContext.get().popSubject();
/* 296 */       Logger.logDebug(this, "cleanUp", session, "Cleaning up the subject from User context " + sub);
/*     */     }
/* 298 */     while (sub != null);
/*     */     
/* 300 */     MetadataCache metadataCache = Factory.MetadataCache.getDefaultInstance();
/* 301 */     if ((metadataCache instanceof Cache)) {
/* 302 */       Logger.logDebug(this, "cleanUp", session, "Evicting all the Class descriptions from the cache");
/* 303 */       ((Cache)metadataCache).evictAllClassDescriptions();
/*     */     }
/* 305 */     this.userId = null;
/* 306 */     this.userName = null;
/* 307 */     this.distinguishedName = null;
/* 308 */     this.ceConnection = null;
/* 309 */     this._subject = null;
/* 310 */     this.domain = null;
/* 311 */     this.objectStore = null;
/* 312 */     this.domainName = null;
/* 313 */     this.objectStoreName = null;
/* 314 */     this.userDisplayName = null;
/* 315 */     this.connected = false;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 319 */     if (this.userName == null)
/* 320 */       establishP8Connection();
/* 321 */     return this.userName;
/*     */   }
/*     */   
/*     */   public String getAppServerType() {
/* 325 */     return this.appServerType;
/*     */   }
/*     */   
/*     */   public com.filenet.api.core.Connection getCEConnection() {
/* 329 */     Subject subject = getSubject();
/* 330 */     UserContext.get().popSubject();
/* 331 */     UserContext.get().pushSubject(subject);
/* 332 */     return this.ceConnection;
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
/*     */   public Subject getSubject()
/*     */   {
/* 347 */     return this._subject;
/*     */   }
/*     */   
/*     */   public Domain getDomain() {
/* 351 */     Subject subject = getSubject();
/* 352 */     if (this.domain == null)
/* 353 */       establishP8Connection();
/* 354 */     if (subject != null) {
/* 355 */       UserContext.get().popSubject();
/* 356 */       UserContext.get().pushSubject(subject);
/*     */     }
/*     */     
/* 359 */     return this.domain;
/*     */   }
/*     */   
/*     */   public ObjectStore getObjectStore() {
/* 363 */     if ((this.objectStore == null) && (!this.objectStoreNotFound))
/* 364 */       establishP8Connection();
/* 365 */     return this.objectStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectStoreName()
/*     */   {
/* 373 */     return getObjectStore() != null ? this.objectStore.get_Name() : this.objectStoreName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<String> getDomainObjectStoreNames()
/*     */   {
/* 381 */     if (this.domainObjectStoreNames == null)
/* 382 */       establishP8Connection();
/* 383 */     return this.domainObjectStoreNames;
/*     */   }
/*     */   
/*     */   void addObjectStore(String id, ObjectStore objectStore) {
/* 387 */     this.objectStores.put(id, objectStore);
/*     */   }
/*     */   
/*     */   ObjectStore getObjectStore(String id) {
/* 391 */     return (ObjectStore)this.objectStores.get(id);
/*     */   }
/*     */   
/*     */   public String getDistinguishedName() {
/* 395 */     if (this.distinguishedName == null)
/* 396 */       establishP8Connection();
/* 397 */     return this.distinguishedName;
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
/*     */   public String getP8ConnectionType()
/*     */   {
/* 431 */     return this.p8ConnectionType;
/*     */   }
/*     */   
/*     */   public void setP8ConnectionType(String p8ConnectionType) {
/* 435 */     this.p8ConnectionType = p8ConnectionType;
/*     */   }
/*     */   
/*     */   public String getPrivAddItemTeamspaceTemplate() {
/* 439 */     return this.privAddItemTeamspaceTemplate;
/*     */   }
/*     */   
/*     */   public void setPrivAddItemTeamspaceTemplate(String privAddItemTeamspaceTemplate) {
/* 443 */     this.privAddItemTeamspaceTemplate = privAddItemTeamspaceTemplate;
/*     */   }
/*     */   
/*     */   public String getPrivAddItemTeamspace() {
/* 447 */     return this.privAddItemTeamspace;
/*     */   }
/*     */   
/*     */   public void setPrivAddItemTeamspace(String privAddItemTeamspace) {
/* 451 */     this.privAddItemTeamspace = privAddItemTeamspace;
/*     */   }
/*     */   
/*     */   public String getPrivAddItem() {
/* 455 */     return this.privAddItem;
/*     */   }
/*     */   
/*     */   public void setPrivAddItem(String privAddItem) {
/* 459 */     this.privAddItem = privAddItem;
/*     */   }
/*     */   
/*     */   public String getPrivAddDoc() {
/* 463 */     return this.privAddDoc;
/*     */   }
/*     */   
/*     */   public void setPrivAddDoc(String privAddDoc) {
/* 467 */     this.privAddDoc = privAddDoc;
/*     */   }
/*     */   
/*     */   public String getPrivAddSearch() {
/* 471 */     return this.privAddSearch;
/*     */   }
/*     */   
/*     */   public void setPrivAddSearch(String privAddSearch) {
/* 475 */     this.privAddSearch = privAddSearch;
/*     */   }
/*     */   
/*     */   public String getPrivAddUnifiedSearch() {
/* 479 */     return this.privAddUnifiedSearch;
/*     */   }
/*     */   
/*     */   public void setPrivAddUnifiedSearch(String privAddUnifiedSearch) {
/* 483 */     this.privAddUnifiedSearch = privAddUnifiedSearch;
/*     */   }
/*     */   
/*     */   public String getSearchTemplateSupported() {
/* 487 */     return this.searchTemplateSupported;
/*     */   }
/*     */   
/*     */   public void setSearchTemplateSupported(String searchTemplateSupported) {
/* 491 */     this.searchTemplateSupported = searchTemplateSupported;
/*     */   }
/*     */   
/*     */   public String getUnifiedSearchTemplateSupported() {
/* 495 */     return this.unifiedSearchTemplateSupported;
/*     */   }
/*     */   
/*     */   public void setUnifiedSearchTemplateSupported(String unifiedSearchTemplateSupported) {
/* 499 */     this.unifiedSearchTemplateSupported = unifiedSearchTemplateSupported;
/*     */   }
/*     */   
/*     */   public void setPrivWorkflow(String privWorkflow) {
/* 503 */     this.privWorkflow = privWorkflow;
/*     */   }
/*     */   
/*     */   public String getPrivWorkflow() {
/* 507 */     return this.privWorkflow;
/*     */   }
/*     */   
/*     */   public String getPrivFindUsersAndGroups() {
/* 511 */     return this.privFindUsersAndGroups;
/*     */   }
/*     */   
/*     */   public void setPrivFindUsersAndGroups(String privFindUsersAndGroups) {
/* 515 */     this.privFindUsersAndGroups = privFindUsersAndGroups;
/*     */   }
/*     */   
/*     */   public String getTextSearchType(HttpServletRequest request) {
/* 519 */     return P8TextSearchUtil.getTextSearchType(this, request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObjectStoreRecordType(String recordType)
/*     */   {
/* 528 */     this.objectStoreRecordType = recordType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectStoreRecordType()
/*     */   {
/* 537 */     if ((this.objectStoreRecordType == null) && (this.objectStore != null)) {
/* 538 */       this.objectStoreRecordType = P8RecordsUtil.getRecordRepositoryType(this.objectStore).toString();
/*     */     }
/* 540 */     return this.objectStoreRecordType == null ? "" : this.objectStoreRecordType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObjectStoreRecordDataModel(String dataModel)
/*     */   {
/* 549 */     this.objectStoreRecordDataModel = dataModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectStoreRecordDataModel()
/*     */   {
/* 558 */     if (this.objectStoreRecordDataModel == null) {
/* 559 */       this.objectStoreRecordDataModel = P8RecordsUtil.getDatamodelType(this.objectStore).toString();
/*     */     }
/* 561 */     return this.objectStoreRecordDataModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRecordObjectStore()
/*     */   {
/* 571 */     return (this.objectStoreRecordType != null) && ((this.objectStoreRecordType.equals(P8RecordsUtil.RecordRepositoryType.FilePlan.toString())) || (this.objectStoreRecordType.equals(P8RecordsUtil.RecordRepositoryType.Content.toString())));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSearchAddOnInstalled()
/*     */   {
/* 578 */     return "true".equals(getSearchTemplateSupported());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUnifiedSearchAddOnInstalled()
/*     */   {
/* 585 */     return "true".equals(getUnifiedSearchTemplateSupported());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDocumentCommentSupported()
/*     */   {
/* 595 */     return getObjectStore() != null ? isDocumentCommentSupported(this.objectStore) : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDocumentCommentSupported(ObjectStore objectStore)
/*     */   {
/* 604 */     String name = objectStore.get_SymbolicName();
/* 605 */     if (this.osDocumentCommentSupported.containsKey(name)) {
/* 606 */       return ((Boolean)this.osDocumentCommentSupported.get(name)).booleanValue();
/*     */     }
/* 608 */     boolean supported = P8SocialCollaborationProxy.isCeClientUpToDate();
/* 609 */     if (supported) {
/*     */       try {
/* 611 */         PropertyFilter filter = new PropertyFilter();
/* 612 */         filter.addIncludeProperty(0, null, null, "Id", null);
/* 613 */         Factory.ClassDefinition.fetchInstance(objectStore, "ClbDocumentComment", filter);
/*     */       } catch (EngineRuntimeException e) {
/* 615 */         supported = false;
/*     */       }
/*     */     }
/* 618 */     this.osDocumentCommentSupported.put(name, Boolean.valueOf(supported));
/*     */     
/* 620 */     return supported;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDownloadCountSupported()
/*     */   {
/* 631 */     return getObjectStore() != null ? isDownloadCountSupported(this.objectStore) : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDownloadCountSupported(ObjectStore objectStore)
/*     */   {
/* 640 */     String name = objectStore.get_SymbolicName();
/* 641 */     if (this.osDownloadCountSupported.containsKey(name)) {
/* 642 */       return ((Boolean)this.osDownloadCountSupported.get(name)).booleanValue();
/*     */     }
/* 644 */     boolean supported = P8SocialCollaborationProxy.isCeClientUpToDate();
/* 645 */     if (supported) {
/*     */       try {
/* 647 */         PropertyFilter filter = new PropertyFilter();
/* 648 */         filter.addIncludeProperty(0, null, null, "Id", null);
/* 649 */         Factory.ClassDefinition.fetchInstance(objectStore, "ClbDownloadRecord", filter);
/*     */       } catch (EngineRuntimeException e) {
/* 651 */         supported = false;
/*     */       }
/*     */     }
/* 654 */     this.osDownloadCountSupported.put(name, Boolean.valueOf(supported));
/*     */     
/* 656 */     return supported;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String retrieveUserDisplayName(String userIdOrShortName)
/*     */   {
/* 665 */     String displayName = null;
/*     */     try {
/* 667 */       User user = retrieveUser(userIdOrShortName);
/* 668 */       displayName = user.get_DisplayName();
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/*     */ 
/* 673 */     return displayName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public User retrieveUser(String userIdOrShortName)
/*     */     throws Exception
/*     */   {
/* 682 */     if (this.users.containsKey(userIdOrShortName)) {
/* 683 */       return (User)this.users.get(userIdOrShortName);
/*     */     }
/* 685 */     PropertyFilter filter = new PropertyFilter();
/* 686 */     filter.addIncludeProperty(0, null, null, "Id", null);
/* 687 */     filter.addIncludeProperty(0, null, null, "ShortName", null);
/* 688 */     filter.addIncludeProperty(0, null, null, "DisplayName", null);
/* 689 */     filter.addIncludeProperty(0, null, null, "Email", null);
/*     */     User user;
/*     */     try {
/* 692 */       user = Factory.User.fetchInstance(this.ceConnection, userIdOrShortName, filter);
/* 693 */       this.users.put(userIdOrShortName, user);
/*     */     } catch (Exception e) {
/* 695 */       throw e;
/*     */     }
/*     */     
/* 698 */     return user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<SecurityPrincipal> retrieveUsersAndGroups(String[] userIdOrShortName, String[] groupIdOrShortName)
/*     */     throws Exception
/*     */   {
/* 707 */     PropertyFilter filter = new PropertyFilter();
/* 708 */     filter.addIncludeProperty(0, null, null, "Id", null);
/* 709 */     filter.addIncludeProperty(0, null, null, "ShortName", null);
/* 710 */     filter.addIncludeProperty(0, null, null, "DisplayName", null);
/* 711 */     filter.addIncludeProperty(0, null, null, "Email", null);
/*     */     
/* 713 */     RetrievingBatch batch = RetrievingBatch.createRetrievingBatchInstance(this.domain);
/* 714 */     List<SecurityPrincipal> array = new ArrayList();
/* 715 */     if (userIdOrShortName != null) {
/* 716 */       for (int i = 0; i < userIdOrShortName.length; i++) {
/* 717 */         User user = Factory.User.getInstance(this.ceConnection, userIdOrShortName[i]);
/* 718 */         array.add(user);
/* 719 */         batch.add(user, filter);
/*     */       }
/*     */     }
/* 722 */     if (groupIdOrShortName != null) {
/* 723 */       for (int i = 0; i < groupIdOrShortName.length; i++) {
/* 724 */         Group group = Factory.Group.getInstance(this.ceConnection, groupIdOrShortName[i]);
/* 725 */         array.add(group);
/* 726 */         batch.add(group, filter);
/*     */       }
/*     */     }
/* 729 */     batch.retrieveBatch();
/* 730 */     return array;
/*     */   }
/*     */   
/*     */   public void addPermissionsToJSON(JSONObject json, HttpServletRequest request, String repositoryId)
/*     */   {
/* 735 */     json.put("priv_addItem", this.privAddDoc == null ? null : Boolean.valueOf(this.privAddDoc.equalsIgnoreCase("true")));
/* 736 */     json.put("priv_addDoc", this.privAddItem == null ? null : Boolean.valueOf(this.privAddItem.equalsIgnoreCase("true")));
/* 737 */     json.put("textSearchType", P8TextSearchUtil.getTextSearchType(this, request));
/* 738 */     json.put("capabilityComment", isDocumentCommentSupported() ? "documentonly" : "none");
/* 739 */     json.put("priv_addTeamspace", getPrivAddItemTeamspace() == null ? null : Boolean.valueOf(getPrivAddItemTeamspace().equalsIgnoreCase("true")));
/* 740 */     json.put("priv_addTeamspaceTemplate", getPrivAddItemTeamspaceTemplate() == null ? null : Boolean.valueOf(getPrivAddItemTeamspaceTemplate().equalsIgnoreCase("true")));
/* 741 */     json.put("privFindUsersAndGroups", this.privFindUsersAndGroups == null ? null : Boolean.valueOf(this.privAddItem.equalsIgnoreCase("true")));
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8Connection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */