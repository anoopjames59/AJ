/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.constants.ReservationType;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplateDocument;
/*     */ import com.ibm.ier.plugin.search.p8.P8SearchTemplateDocumentBase;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_BaseEntityImpl;
/*     */ import com.ibm.json.java.JSON;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class SaveSearchTemplateService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public void setP8QueryContinuationData(P8QueryContinuationData queryData) {}
/*     */   
/*     */   public String getId()
/*     */   {
/*  59 */     return "ierSaveSearchTemplateService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  63 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  67 */     String methodName = "executeAction";
/*  68 */     Logger.logEntry(this, methodName, request);
/*  69 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*     */     
/*  71 */     P8Connection connection = createP8Connection();
/*  72 */     Connection ceConnection = connection.getCEConnection();
/*  73 */     synchronized (ceConnection)
/*     */     {
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/*  80 */         String serverName = request.getParameter("repositoryId");
/*  81 */         Logger.logDebug(this, methodName, request, "serverName = " + serverName);
/*     */         
/*     */ 
/*     */ 
/*  85 */         String t = readJSON(request);
/*  86 */         JSONObject searchTemplateJson = (JSONObject)JSON.parse(t);
/*     */         
/*     */ 
/*  89 */         String name = (String)searchTemplateJson.get("name");
/*  90 */         Logger.logDebug(this, methodName, request, "name = " + name);
/*  91 */         String description = (String)searchTemplateJson.get("description");
/*  92 */         Logger.logDebug(this, methodName, request, "description = " + description);
/*  93 */         String folderPID = (String)searchTemplateJson.get("parentFolderId");
/*  94 */         Logger.logDebug(this, methodName, request, "parentFolderId = " + folderPID);
/*     */         
/*     */ 
/*  97 */         JSONArray permissionsJSON = (JSONArray)searchTemplateJson.get("permissions");
/*  98 */         AccessPermissionList permissions = getPermissionsFromJSON(permissionsJSON);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */         P8SearchTemplateDocumentBase searchDocument = null;
/* 107 */         searchDocument = new P8SearchTemplateDocument(request, connection, null, null, null);
/* 108 */         Document document = searchDocument.saveAs(name, description, searchTemplateJson, folderPID, permissions);
/*     */         
/*     */ 
/* 111 */         document.checkout(ReservationType.OBJECT_STORE_DEFAULT, null, null, null);
/* 112 */         document.save(RefreshMode.REFRESH);
/*     */         
/*     */ 
/* 115 */         Collection<RMClassDescription> classDescriptions = getUniqueClassDescriptions("StoredSearch", this.baseService.getFilePlanRepository(), request);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 120 */         mediator.setServerName(serverName);
/* 121 */         mediator.setClassDescriptions(new ArrayList(classDescriptions));
/*     */         
/*     */ 
/* 124 */         IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(request);
/* 125 */         ArrayList<BaseEntity> hits = new ArrayList(1);
/*     */         
/* 127 */         BaseEntity entity = new P8CE_BaseEntityImpl(this.baseService.getRepository(), document.get_Id().toString(), "StoredSearch", EntityType.Unknown, document, false);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 132 */         hits.add(entity);
/* 133 */         searchResultsBean.setResults(hits);
/* 134 */         mediator.setSearchResultsBean(searchResultsBean);
/*     */         
/* 136 */         mediator.setRequestedProperties(IERUtil.convertStringsToListString("id"));
/*     */       } catch (Exception e) {
/* 138 */         logFDC(request, response, null, methodName);
/* 139 */         Logger.logError(this, methodName, request, e);
/* 140 */         mediator.addError("error.exception.general", e.getLocalizedMessage(), null, null, null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 149 */     Logger.logExit(this, methodName, request);
/*     */   }
/*     */   
/*     */   protected void logFDC(HttpServletRequest request, HttpServletResponse response, Object param, String methodName) {
/* 153 */     String serverName = request.getParameter("repositoryId");
/* 154 */     Logger.logInfo(this, methodName, request, "serverName = " + serverName);
/*     */   }
/*     */   
/*     */   private static AccessPermissionList getPermissionsFromJSON(JSONArray permissionJSONArray) throws Exception
/*     */   {
/* 159 */     AccessPermissionList permissionList = null;
/* 160 */     if ((permissionJSONArray != null) && (permissionJSONArray.size() >= 0)) {
/* 161 */       permissionList = Factory.AccessPermission.createList();
/* 162 */       for (int i = 0; i < permissionJSONArray.size(); i++) {
/* 163 */         JSONObject permission = (JSONObject)permissionJSONArray.get(i);
/* 164 */         int mask = Integer.parseInt(permission.get("accessMask").toString());
/* 165 */         if (mask > 0) {
/* 166 */           AccessPermission accessPermission = Factory.AccessPermission.createInstance();
/* 167 */           accessPermission.set_AccessType(Integer.parseInt(permission.get("accessType").toString()) == 1 ? AccessType.ALLOW : AccessType.DENY);
/* 168 */           accessPermission.set_AccessMask(Integer.valueOf(Integer.parseInt(permission.get("accessMask").toString())));
/* 169 */           accessPermission.set_InheritableDepth(Integer.valueOf(Integer.parseInt(permission.get("inheritableDepth").toString())));
/* 170 */           accessPermission.set_GranteeName(permission.get("granteeName").toString());
/* 171 */           permissionList.add(accessPermission);
/*     */         }
/*     */       }
/*     */     }
/* 175 */     return permissionList;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\SaveSearchTemplateService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */