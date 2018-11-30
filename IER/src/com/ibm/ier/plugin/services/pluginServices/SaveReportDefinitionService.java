/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.constants.AutoUniqueName;
/*     */ import com.filenet.api.constants.DefineSecurityParentage;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Factory.Folder;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERPermission;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMFactory.ReportDefinition;
/*     */ import com.ibm.jarm.api.core.ReportDefinition;
/*     */ import com.ibm.jarm.api.query.ReportParameter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_ReportDefinitionImpl;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
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
/*     */ public class SaveReportDefinitionService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  44 */     return "ierSaveReportDefinitionService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  48 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  51 */       JSONObject requestContent = getRequestContent();
/*  52 */       FilePlanRepository repository = getFilePlanRepository();
/*     */       
/*  54 */       ReportDefinition rd = null;
/*  55 */       String id = (String)requestContent.get("ier_id");
/*  56 */       if ((id != null) && (id.length() > 0))
/*     */       {
/*  58 */         rd = RMFactory.ReportDefinition.fetchInstance(repository, id, null);
/*     */       }
/*     */       else {
/*  61 */         rd = RMFactory.ReportDefinition.createInstance(repository);
/*     */       }
/*     */       
/*  64 */       fromJSON(rd, requestContent);
/*     */       
/*     */ 
/*  67 */       JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/*  68 */       List<RMPermission> permissionList = IERPermission.getPermissionsFromJSON(permissionsJSON);
/*     */       
/*     */ 
/*  71 */       if ((rd instanceof P8CE_ReportDefinitionImpl)) {
/*  72 */         P8CE_ReportDefinitionImpl p8rd = (P8CE_ReportDefinitionImpl)rd;
/*  73 */         p8rd.setPermissions(permissionList);
/*  74 */         p8rd.save(RMRefreshMode.NoRefresh);
/*  75 */         if ((id == null) || (id.isEmpty()))
/*     */         {
/*  77 */           ObjectStore os = P8CE_Convert.fromJARM(repository.getRepository());
/*  78 */           Document rdDoc = (Document)p8rd.getJaceBaseObject();
/*  79 */           Folder folder = Factory.Folder.getInstance(os, "Folder", new Id("{5E8189C9-C2E7-43BB-B5AF-EB5E69CAA543}"));
/*     */           
/*  81 */           ReferentialContainmentRelationship rcr = folder.file(rdDoc, AutoUniqueName.AUTO_UNIQUE, null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
/*     */           
/*  83 */           rcr.save(RefreshMode.REFRESH);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  88 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_REPORTDEFINITION_FAILD_TO_SAVE, new Object[] { e.getLocalizedMessage() });
/*     */     }
/*     */     
/*  91 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */ 
/*     */   private void fromJSON(ReportDefinition rd, JSONObject json)
/*     */   {
/*  97 */     JSONArray properties = (JSONArray)json.get("ier_reportdefintionProperties");
/*  98 */     JSONObject criterias = (JSONObject)json.get("criterias");
/*     */     
/* 100 */     if ((properties != null) && (properties.size() > 0)) {
/* 101 */       Iterator<JSONObject> i = properties.iterator();
/* 102 */       while (i.hasNext()) {
/* 103 */         JSONObject propJSON = (JSONObject)i.next();
/* 104 */         String propName = (String)propJSON.get("name");
/* 105 */         String value = (String)propJSON.get("value");
/* 106 */         if (propName.equals("RMReportTitle")) {
/* 107 */           rd.setReportTitle(value);
/* 108 */         } else if (propName.equals("Description")) {
/* 109 */           rd.setReportDescription(value);
/* 110 */         } else if (propName.equals("ReportName")) {
/* 111 */           rd.setReportName(value);
/* 112 */         } else if (propName.equals("EntryHelp")) {
/* 113 */           rd.setEntryHelp(value);
/* 114 */         } else if (propName.equals("DatabaseTableName")) {
/* 115 */           rd.setReportDBTableName(value);
/*     */         }
/*     */       }
/*     */     }
/* 119 */     JSONArray paras = (JSONArray)criterias.get("parameters");
/* 120 */     if ((paras != null) && (paras.size() > 0)) {
/* 121 */       List<ReportParameter> paraList = new ArrayList();
/* 122 */       Iterator<JSONObject> i = paras.iterator();
/* 123 */       while (i.hasNext()) {
/* 124 */         JSONObject paraJSON = (JSONObject)i.next();
/* 125 */         String paraName = (String)paraJSON.get("name");
/* 126 */         Boolean isReq = (Boolean)paraJSON.get("isreq");
/* 127 */         ReportParameter para = new ReportParameter(paraName, isReq.booleanValue());
/*     */         
/* 129 */         if (paraName.equals("rm_entity_type"))
/*     */         {
/* 131 */           JSONArray valuesJSON = (JSONArray)paraJSON.get("value");
/* 132 */           Iterator<JSONObject> valueIter = valuesJSON.iterator();
/* 133 */           while (valueIter.hasNext()) {
/* 134 */             JSONObject valueJSON = (JSONObject)valueIter.next();
/* 135 */             String name = (String)valueJSON.get("name");
/* 136 */             String label = (String)valueJSON.get("label");
/* 137 */             para.addValues(name, label, null);
/*     */           }
/*     */         }
/*     */         
/* 141 */         paraList.add(para);
/*     */       }
/* 143 */       rd.setReportParameters(paraList);
/*     */     }
/*     */     
/* 146 */     rd.removeAllReportQueries();
/* 147 */     JSONArray sqlstmts = (JSONArray)criterias.get("sqlstmts");
/* 148 */     if ((sqlstmts != null) && (sqlstmts.size() > 0))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */       Iterator<JSONObject> i = sqlstmts.iterator();
/* 158 */       while (i.hasNext()) {
/* 159 */         JSONObject sqlstmtJSON = (JSONObject)i.next();
/* 160 */         JSONArray sqlArray = (JSONArray)sqlstmtJSON.get("sql");
/* 161 */         List<String> sqlList = new ArrayList();
/* 162 */         Iterator<String> iter = sqlArray.iterator();
/* 163 */         while (iter.hasNext()) {
/* 164 */           String sql = (String)iter.next();
/* 165 */           if ((sql != null) && (sql.length() > 0))
/* 166 */             sqlList.add(sql);
/*     */         }
/* 168 */         String entity = (String)sqlstmtJSON.get("entity");
/* 169 */         if (entity != null) {
/* 170 */           rd.addReportQueries((String[])sqlList.toArray(new String[sqlList.size()]), entity);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\SaveReportDefinitionService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */