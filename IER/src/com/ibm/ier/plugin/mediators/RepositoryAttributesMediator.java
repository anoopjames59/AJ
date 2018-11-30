/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*     */ import com.ibm.ier.plugin.util.PrivilegesUtil;
/*     */ import com.ibm.jarm.api.constants.DataModelType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RepositoryType;
/*     */ import com.ibm.jarm.api.core.FilePlan;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.NamingPattern;
/*     */ import com.ibm.jarm.api.core.RMCustomObject;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMCustomObject;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Iterator;
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
/*     */ public class RepositoryAttributesMediator
/*     */   extends BaseMediator
/*     */ {
/*  51 */   private static String ATTRIBUTES = "attributes";
/*  52 */   private static String IER_REPO_TYPE = "recordRepositoryType";
/*  53 */   private static String IER_DM_TYPE = "recordDatamodelType";
/*  54 */   private static String SERVERS = "servers";
/*  55 */   private static String P8_REPO_ID = "p8RepositoryId";
/*  56 */   private static String P8_REPO_GUID = "p8RepositoryGuid";
/*  57 */   private static String FILEPLANS = "fileplans";
/*  58 */   private static String PRIVILEGES = "privileges";
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*  61 */   private String[] repositoryIds = null;
/*  62 */   private String repositoryIdType = "nexus";
/*  63 */   private boolean isLogin = false;
/*  64 */   private boolean shouldRetrieveFilePlans = true;
/*     */   
/*     */   public void setRepositoryIds(String[] repositoryIds) {
/*  67 */     this.repositoryIds = repositoryIds;
/*     */   }
/*     */   
/*     */   public void setRepositoryIdType(String type) {
/*  71 */     this.repositoryIdType = type;
/*     */   }
/*     */   
/*     */   public void setIsLogin(boolean isLogin) {
/*  75 */     this.isLogin = isLogin;
/*     */   }
/*     */   
/*     */   public void setRetrieveFileplans(boolean val) {
/*  79 */     this.shouldRetrieveFilePlans = val;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRepositoryAttributeJSON(JSONObject repoAttributesJSON, Repository repository)
/*     */     throws Exception
/*     */   {
/*  88 */     Logger.logDebug(this, "toJSONObject", this.servletRequest, "RepositoryId: " + repository.getName() + " " + "RepositoryType: " + repository.getRepositoryType().toString());
/*  89 */     repoAttributesJSON.put(IER_REPO_TYPE, repository.getRepositoryType().toString());
/*  90 */     repoAttributesJSON.put(P8_REPO_ID, repository.getSymbolicName());
/*  91 */     repoAttributesJSON.put(P8_REPO_GUID, repository.getObjectIdentity());
/*     */     
/*     */ 
/*  94 */     JSONObject additionalAttributes = new JSONObject();
/*  95 */     repoAttributesJSON.put("additionalAttributes", additionalAttributes);
/*     */     
/*     */ 
/*  98 */     if ((repository.getRepositoryType() == RepositoryType.Combined) || (repository.getRepositoryType() == RepositoryType.FilePlan)) {
/*  99 */       FilePlanRepository fp_repo = (FilePlanRepository)repository;
/* 100 */       repoAttributesJSON.put(IER_DM_TYPE, fp_repo.getDataModelType().toString());
/* 101 */       Logger.logDebug(this, "toJSONObject", this.servletRequest, "DatamodelType: " + fp_repo.getDataModelType().toString());
/*     */       
/*     */ 
/* 104 */       JSONObject privileges = new JSONObject();
/* 105 */       if (PrivilegesUtil.isRecordsAdministratorAndManager(fp_repo, this.servletRequest)) {
/* 106 */         privileges.put("privIERAddHold", Boolean.valueOf(true));
/* 107 */         privileges.put("privIERAddDispositionAction", Boolean.valueOf(true));
/* 108 */         privileges.put("privIERAddEventTrigger", Boolean.valueOf(true));
/* 109 */         privileges.put("privIERAddTransferMapping", Boolean.valueOf(true));
/* 110 */         privileges.put("privIERAddRecordType", Boolean.valueOf(true));
/* 111 */         privileges.put("privIERAddLocation", Boolean.valueOf(true));
/* 112 */         privileges.put("privIERAddNamingPattern", Boolean.valueOf(true));
/* 113 */         privileges.put("privIERAddDispositionSchedule", Boolean.valueOf(true));
/* 114 */         privileges.put("privIERAddFilePlan", Boolean.valueOf(true));
/* 115 */         privileges.put("privIERRecordsAdminAndManager", Boolean.valueOf(true));
/* 116 */         privileges.put("privIERAddReportDefinition", Boolean.valueOf(true));
/*     */       }
/* 118 */       repoAttributesJSON.put(PRIVILEGES, privileges);
/*     */       
/* 120 */       String securityRunDate = getSecurityRunDate(fp_repo);
/* 121 */       if (securityRunDate != null) {
/* 122 */         repoAttributesJSON.put("securityRunDate", securityRunDate);
/*     */       }
/*     */       
/* 125 */       JSONArray fileplansJSON = new JSONArray();
/*     */       
/* 127 */       if (this.shouldRetrieveFilePlans) {
/* 128 */         List<FilePlan> fileplans = fp_repo.getFilePlans(RMPropertyFilter.MinimumPropertySet);
/*     */         
/*     */ 
/* 131 */         for (FilePlan fp : fileplans) {
/* 132 */           JSONObject fpProperties = MediatorUtil.createEntityItemJSONObject(fp, MinimumPropertiesUtil.getPropertySetList(EntityType.FilePlan), this.servletRequest);
/*     */           
/* 134 */           NamingPattern namingPattern = fp.getNamingPattern();
/* 135 */           if (namingPattern != null) {
/* 136 */             JSONObject namingPatternJSON = MediatorUtil.createEntityItemJSONObject(fp.getNamingPattern(), MinimumPropertiesUtil.getPropertySetList(EntityType.Pattern), this.servletRequest);
/*     */             
/* 138 */             fpProperties.put("namingPattern", namingPatternJSON);
/*     */           }
/*     */           
/* 141 */           fileplansJSON.add(fpProperties);
/*     */         }
/*     */       }
/*     */       
/* 145 */       repoAttributesJSON.put(FILEPLANS, fileplansJSON);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getSecurityRunDate(FilePlanRepository fp_repo) {
/*     */     try {
/* 151 */       RMCustomObject co = RMFactory.RMCustomObject.fetchInstance(fp_repo, "{C83A89E1-95EE-4E62-8B54-FEE57DBDBD3E}", null);
/* 152 */       return co.getProperties().getStringValue("PropertyValue");
/*     */     }
/*     */     catch (Exception exp) {}
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public JSONObject toJSONObject()
/*     */     throws Exception
/*     */   {
/* 161 */     Logger.logEntry(this, "toJSONObject", this.servletRequest);
/*     */     
/* 163 */     if (Logger.isDebugLogged()) {
/* 164 */       Logger.logDebug(this, "serviceExecute", this.servletRequest, "Params: " + this.completedJSONResponseObject + " isLogin: " + this.isLogin);
/*     */     }
/*     */     
/*     */ 
/* 168 */     if ((this.completedJSONResponseObject != null) && (!this.isLogin)) {
/* 169 */       return this.completedJSONResponseObject;
/*     */     }
/*     */     
/*     */ 
/* 173 */     if ((this.isLogin) && (this.completedJSONResponseObject != null))
/*     */     {
/* 175 */       if (Logger.isDebugLogged()) {
/* 176 */         Logger.logDebug(this, "serviceExecute", this.servletRequest, "isLogin is true and completedJSON is not null");
/*     */       }
/*     */       
/* 179 */       JSONArray servers = (JSONArray)this.completedJSONResponseObject.get(SERVERS);
/*     */       
/*     */ 
/* 182 */       Iterator servers_it = servers.iterator();
/*     */       
/* 184 */       while (servers_it.hasNext()) {
/* 185 */         JSONObject repoJSON = (JSONObject)servers_it.next();
/* 186 */         if (repoJSON != null) {
/* 187 */           String nexusRepositoryId = (String)repoJSON.get("repositoryId");
/* 188 */           if (nexusRepositoryId != null) {
/* 189 */             Repository repository = this.baseService.getRepositoryFromNexusRepositoryId(nexusRepositoryId);
/* 190 */             if (repository != null) {
/* 191 */               JSONObject repoAttributesJSON = (JSONObject)repoJSON.get(ATTRIBUTES);
/* 192 */               if (repoAttributesJSON == null) {
/* 193 */                 repoAttributesJSON = new JSONObject();
/* 194 */                 repoJSON.put(ATTRIBUTES, repoAttributesJSON);
/*     */               }
/* 196 */               setRepositoryAttributeJSON(repoAttributesJSON, repository);
/*     */               
/* 198 */               setRepositoryConfig(repoAttributesJSON, nexusRepositoryId);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 203 */       return this.completedJSONResponseObject;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 208 */     if (this.repositoryIds != null)
/*     */     {
/* 210 */       if (Logger.isDebugLogged()) {
/* 211 */         Logger.logDebug(this, "serviceExecute", this.servletRequest, "repositoryIds not null");
/*     */       }
/*     */       
/* 214 */       JSONObject jsonResponse = new JSONObject();
/* 215 */       JSONArray servers = new JSONArray();
/* 216 */       jsonResponse.put(SERVERS, servers);
/*     */       
/* 218 */       for (String repositoryId : this.repositoryIds)
/*     */       {
/* 220 */         if (Logger.isDebugLogged()) {
/* 221 */           Logger.logDebug(this, "serviceExecute", this.servletRequest, "RepositoryId being used: " + repositoryId + " repositoryIdType: " + this.repositoryIdType);
/*     */         }
/*     */         
/* 224 */         Repository repository = null;
/* 225 */         if (repositoryId != null) {
/* 226 */           if (this.repositoryIdType.equals("p8")) {
/* 227 */             repository = this.baseService.getRepository(repositoryId);
/*     */           } else {
/* 229 */             repository = this.baseService.getRepositoryFromNexusRepositoryId(repositoryId);
/*     */           }
/* 231 */           com.ibm.ecm.configuration.RepositoryConfig config = com.ibm.ecm.configuration.Config.getRepositoryConfigUsingIdOrServerName(this.servletRequest, repositoryId.trim());
/* 232 */           JSONObject repoJSON = new JSONObject();
/* 233 */           repoJSON.put("repositoryId", repositoryId);
/*     */           
/* 235 */           if (config != null) {
/* 236 */             repoJSON.put("serverName", config.getServerName());
/* 237 */             repoJSON.put("name", config.getName());
/*     */           }
/*     */           
/* 240 */           if (Logger.isDebugLogged()) {
/* 241 */             Logger.logDebug(this, "serviceExecute", this.servletRequest, "Repository Object:" + repository);
/*     */           }
/*     */           
/* 244 */           if (repository != null) {
/* 245 */             setRepositoryAttributeJSON(repoJSON, repository);
/*     */           }
/* 247 */           setRepositoryConfig(repoJSON, repositoryId);
/*     */           
/* 249 */           servers.add(repoJSON);
/*     */         }
/*     */       }
/*     */       
/* 253 */       return jsonResponse;
/*     */     }
/*     */     
/*     */ 
/* 257 */     Logger.logExit(this, "toJSONObject", this.servletRequest);
/* 258 */     return new JSONObject();
/*     */   }
/*     */   
/*     */   public void setRepositoryConfig(JSONObject json, String reposutoryId) throws Exception {
/* 262 */     String defaultFilePlan = null;
/* 263 */     com.ibm.ier.plugin.configuration.RepositoryConfig rc = com.ibm.ier.plugin.configuration.Config.getRepositoryConfig(reposutoryId);
/* 264 */     if ((rc != null) && (json != null))
/*     */     {
/* 266 */       defaultFilePlan = rc.getDefaultFilePlan();
/* 267 */       if (defaultFilePlan != null) {
/* 268 */         json.put("defaultFilePlan", defaultFilePlan);
/*     */       }
/*     */       
/*     */ 
/* 272 */       String reportOutputSaveDirectory = rc.getReportOutputSaveDirectory();
/* 273 */       if ((reportOutputSaveDirectory != null) && (!reportOutputSaveDirectory.isEmpty())) {
/* 274 */         json.put("reportOutputSaveDirectory", reportOutputSaveDirectory);
/*     */       }
/*     */       
/*     */ 
/* 278 */       String alwaysDeclare = rc.getDDAlwaysDeclareRecord();
/* 279 */       if ((alwaysDeclare != null) && (!alwaysDeclare.isEmpty())) {
/* 280 */         json.put("defensibleSweepAlwaysDeclareRecord", alwaysDeclare);
/*     */       }
/* 282 */       String showDeclareResult = rc.getDDAlwaysShowDeclareResult();
/* 283 */       if ((showDeclareResult != null) && (!showDeclareResult.isEmpty())) {
/* 284 */         json.put("defensibleSweepAlwaysShowDeclareResult", showDeclareResult);
/*     */       }
/* 286 */       String workflowId = rc.getDDWorkflowId();
/* 287 */       if ((workflowId != null) && (!workflowId.isEmpty())) {
/* 288 */         json.put("defensibleDisposalWorkflowId", workflowId);
/*     */       }
/* 290 */       String recordContainerId = rc.getDDRecordContainerId();
/* 291 */       if ((recordContainerId != null) && (!recordContainerId.isEmpty())) {
/* 292 */         json.put("defensibleDisposalRecordContainerId", recordContainerId);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\RepositoryAttributesMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */