/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.core.ReportDefinition;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.query.ReportParameter;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ReportDefinitionListMediator extends BaseMediator
/*     */ {
/*  14 */   private static String IER_REPORTDEFINITION_ENTRYHELP_DESC_KEY = "ier_reportdefinition_entryhelp_desc_key";
/*  15 */   private static String IER_REPORTDEFINITION_ENTRYHELP = "ier_reportdefinition_entryhelp";
/*     */   
/*  17 */   private static String IER_PARM_TEMPLATE_DESC_KEY = "ier_reportdefinition_desc_key";
/*  18 */   private static String IER_PARM_TEMPLATE_TITLE_KEY = "ier_reportdefinition_title_key";
/*  19 */   private static String IER_REPORTDEFINITION_PARAMETERS = "ier_reportdefinition_parameters";
/*  20 */   private static String IER_REPORTDEFINITION_QUERIES = "ier_reportdefinition_queries";
/*  21 */   private static String QUERY_STATEMENT = "query_statement";
/*  22 */   private static String QUERY_ENTITY_TYPE = "query_entity_type";
/*     */   
/*  24 */   private static String PARAMETER_SYMNAME = "parameter_symname";
/*  25 */   private static String PARAMETER_NAME = "parameter_name";
/*  26 */   private static String PARAMETER_CARDINALITY = "parameter_cardinality";
/*  27 */   private static String PARAMETER_DATATYPE = "parameter_datatype";
/*  28 */   private static String PARAMETER_IS_REQUIRED = "parameter_is_required";
/*     */   
/*  30 */   private static String PARAMETER_VALUES = "parameter_values";
/*  31 */   private static String VALUE_KEY = "value_key";
/*  32 */   private static String VALUE_ID = "value_id";
/*  33 */   private static String VALUE = "value";
/*     */   
/*     */   private static final long serialVersionUID = 134L;
/*  36 */   private List<ReportDefinition> rdList = null;
/*  37 */   private List<RMPropertyDescription> pdList = null;
/*  38 */   private boolean bDetailedInfo = false;
/*     */   
/*     */   public void setReportDefinitions(List<ReportDefinition> rdList) {
/*  41 */     this.rdList = rdList;
/*     */   }
/*     */   
/*     */   public void setReportPropertyDescriptions(List<RMPropertyDescription> pdList) {
/*  45 */     this.pdList = pdList;
/*     */   }
/*     */   
/*     */   public void setGenerateDetailedInfo(boolean bDetailed) {
/*  49 */     this.bDetailedInfo = bDetailed;
/*     */   }
/*     */   
/*     */   public JSONObject toJSONObject() throws Exception
/*     */   {
/*  54 */     Logger.logEntry(this, "toJSONObject", this.servletRequest);
/*  55 */     JSONObject jsonObj = super.toJSONObject();
/*     */     
/*  57 */     jsonObj.put("repositoryId", this.baseService.getP8RepositoryId());
/*  58 */     jsonObj.put("num_results", Integer.valueOf(this.rdList == null ? 0 : this.rdList.size()));
/*  59 */     JSONObject items = new JSONObject();
/*  60 */     JSONArray reportDefinitions = new JSONArray();
/*     */     
/*  62 */     if (this.rdList != null)
/*     */     {
/*  64 */       for (ReportDefinition rd : this.rdList)
/*     */       {
/*  66 */         JSONObject itemJSON = new JSONObject();
/*  67 */         itemJSON.put("template_id", rd.getObjectIdentity());
/*  68 */         itemJSON.put("template_name", rd.getReportName());
/*  69 */         itemJSON.put("ier_reportTitle", rd.getReportTitle());
/*  70 */         itemJSON.put("template_desc", rd.getReportDescription());
/*  71 */         itemJSON.put("tableName", rd.getReportDBTableName());
/*  72 */         itemJSON.put(IER_PARM_TEMPLATE_DESC_KEY, rd.getReportDescriptionLocalizationKey());
/*  73 */         itemJSON.put(IER_PARM_TEMPLATE_TITLE_KEY, rd.getReportTitleLocalizationKey());
/*     */         
/*  75 */         itemJSON.put(IER_REPORTDEFINITION_ENTRYHELP, rd.getEntryHelp());
/*  76 */         itemJSON.put(IER_REPORTDEFINITION_ENTRYHELP_DESC_KEY, rd.getEntryHelpLocalizationKey());
/*     */         
/*  78 */         if (this.bDetailedInfo)
/*     */         {
/*  80 */           List<ReportParameter> reportParams = rd.getReportParameters();
/*  81 */           if ((reportParams != null) && (!reportParams.isEmpty()))
/*     */           {
/*  83 */             JSONArray paramsJSON = generateParametersJSON(reportParams);
/*  84 */             itemJSON.put(IER_REPORTDEFINITION_PARAMETERS, paramsJSON);
/*     */           }
/*     */           
/*  87 */           List<String> entityTypes = rd.getAssociatedEntityTypes();
/*  88 */           if ((entityTypes != null) && (!entityTypes.isEmpty()))
/*     */           {
/*  90 */             JSONArray queriesJSON = generateQueriesJSON(entityTypes, rd);
/*  91 */             itemJSON.put(IER_REPORTDEFINITION_QUERIES, queriesJSON);
/*     */           }
/*     */         }
/*  94 */         reportDefinitions.add(itemJSON);
/*     */       }
/*     */     }
/*     */     
/*  98 */     items.put("items", reportDefinitions);
/*  99 */     jsonObj.put("datastore", items);
/* 100 */     Logger.logExit(this, "toJSONObject", this.servletRequest);
/*     */     
/* 102 */     return jsonObj;
/*     */   }
/*     */   
/*     */   private JSONArray generateQueriesJSON(List<String> entityTypes, ReportDefinition rd)
/*     */   {
/* 107 */     JSONArray queriesJSON = new JSONArray();
/* 108 */     for (String entityType : entityTypes)
/*     */     {
/* 110 */       JSONObject queryJSON = new JSONObject();
/* 111 */       String[] query = rd.getReportQueriesByAssociatedEntityType(entityType);
/* 112 */       JSONArray sqlsJSON = new JSONArray();
/* 113 */       if ((query != null) && (query.length > 0)) {
/* 114 */         for (String sql : query) {
/* 115 */           sqlsJSON.add(sql);
/*     */         }
/*     */       }
/* 118 */       queryJSON.put(QUERY_STATEMENT, sqlsJSON);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 123 */       if ((entityType != null) && (!entityType.equals("Common"))) {
/* 124 */         queryJSON.put(QUERY_ENTITY_TYPE, entityType);
/*     */       }
/* 126 */       queriesJSON.add(queryJSON);
/*     */     }
/*     */     
/* 129 */     return queriesJSON;
/*     */   }
/*     */   
/*     */   private void generatePropertyJSON(JSONObject paramJSON, String symname)
/*     */   {
/* 134 */     if ((this.pdList == null) || (symname == null) || (symname.isEmpty())) return;
/* 135 */     for (RMPropertyDescription pd : this.pdList) {
/* 136 */       if (symname.equals(pd.getSymbolicName())) {
/* 137 */         paramJSON.put(PARAMETER_NAME, pd.getDisplayName());
/* 138 */         paramJSON.put(PARAMETER_CARDINALITY, pd.getCardinality().name());
/* 139 */         paramJSON.put(PARAMETER_DATATYPE, pd.getDataType().name());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private JSONArray generateParametersJSON(List<ReportParameter> reportParams)
/*     */   {
/* 146 */     JSONArray paramsJSON = new JSONArray();
/* 147 */     for (ReportParameter reportParam : reportParams)
/*     */     {
/* 149 */       JSONObject paramJSON = new JSONObject();
/* 150 */       paramJSON.put(PARAMETER_SYMNAME, reportParam.getName());
/* 151 */       paramJSON.put(PARAMETER_IS_REQUIRED, Boolean.valueOf(reportParam.isRequired()));
/*     */       
/* 153 */       generatePropertyJSON(paramJSON, reportParam.getName());
/*     */       
/* 155 */       List<String> valueids = reportParam.getAvailValueIds();
/* 156 */       if ((valueids != null) && (!valueids.isEmpty()))
/*     */       {
/* 158 */         JSONArray valuesJSON = new JSONArray();
/* 159 */         for (String valueid : valueids)
/*     */         {
/* 161 */           JSONObject valueJSON = new JSONObject();
/* 162 */           String value = reportParam.getAvailValue(valueid);
/* 163 */           String valueKey = reportParam.getAvailValueLocalizationKey(valueid);
/* 164 */           valueJSON.put(VALUE_ID, valueid);
/* 165 */           valueJSON.put(VALUE, value);
/* 166 */           valueJSON.put(VALUE_KEY, valueKey);
/*     */           
/* 168 */           valuesJSON.add(valueJSON);
/*     */         }
/* 170 */         paramJSON.put(PARAMETER_VALUES, valuesJSON);
/*     */       }
/* 172 */       paramsJSON.add(paramJSON);
/*     */     }
/*     */     
/* 175 */     return paramsJSON;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\ReportDefinitionListMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */