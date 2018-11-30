/*      */ package com.ibm.ier.report.service;
/*      */ 
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.ier.report.db.ColumnDescription;
/*      */ import com.ibm.ier.report.db.DBDescriptor;
/*      */ import com.ibm.ier.report.db.DBType;
/*      */ import com.ibm.ier.report.db.DB_Services;
/*      */ import com.ibm.ier.report.db.impl.DB2_Services;
/*      */ import com.ibm.ier.report.db.impl.JDBC_DBDescriptor;
/*      */ import com.ibm.ier.report.db.impl.JNDI_DBDescriptor;
/*      */ import com.ibm.ier.report.db.impl.Oracle_Services;
/*      */ import com.ibm.ier.report.db.impl.Sql_Services;
/*      */ import com.ibm.ier.report.exception.RptErrorCode;
/*      */ import com.ibm.ier.report.exception.RptRuntimeException;
/*      */ import com.ibm.ier.report.util.QueryParser;
/*      */ import com.ibm.ier.report.util.QueryParser.FromClass;
/*      */ import com.ibm.ier.report.util.QueryParser.SelectItem;
/*      */ import com.ibm.ier.report.util.QueryParser.WhereCondition;
/*      */ import com.ibm.ier.report.util.RptTracer;
/*      */ import com.ibm.ier.report.util.RptTracer.SubSystem;
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.collection.RMPageIterator;
/*      */ import com.ibm.jarm.api.constants.DataType;
/*      */ import com.ibm.jarm.api.constants.DomainType;
/*      */ import com.ibm.jarm.api.constants.RMCardinality;
/*      */ import com.ibm.jarm.api.core.FilePlanRepository;
/*      */ import com.ibm.jarm.api.core.RMDomain;
/*      */ import com.ibm.jarm.api.core.RMFactory.RMClassDescription;
/*      */ import com.ibm.jarm.api.core.RMFactory.RMProperties;
/*      */ import com.ibm.jarm.api.core.RMFactory.ReportDefinition;
/*      */ import com.ibm.jarm.api.core.RMFactory.Repository;
/*      */ import com.ibm.jarm.api.core.ReportDefinition;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.meta.RMClassDescription;
/*      */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*      */ import com.ibm.jarm.api.meta.RMPropertyDescriptionString;
/*      */ import com.ibm.jarm.api.property.RMFilteredPropertyType;
/*      */ import com.ibm.jarm.api.property.RMProperties;
/*      */ import com.ibm.jarm.api.property.RMProperty;
/*      */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*      */ import com.ibm.jarm.api.query.RMSearch;
/*      */ import com.ibm.jarm.api.query.ReportParameter;
/*      */ import com.ibm.jarm.api.query.ResultRow;
/*      */ import com.ibm.jarm.api.security.RMUser;
/*      */ import com.ibm.jarm.api.util.P8CE_Convert;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Vector;
/*      */ import javax.naming.InitialContext;
/*      */ import javax.naming.NamingException;
/*      */ import javax.sql.DataSource;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Report_Services
/*      */ {
/*   76 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*      */   
/*      */   private static final String RPTPARAM_TABLE = "IERRPTPARAM";
/*      */   
/*      */   private static final String RPTJOBID = "RptJobId";
/*      */   private static final String PARAMETER_NAME = "Parameter_Name";
/*      */   private static final String PARAMETER_VALUE = "Parameter_Value";
/*      */   private static final String RPT_CREATOR = "rpt_creator";
/*      */   private static final String RPT_UNIQUE_CREATOR = "rpt_unique_creator";
/*      */   private static final String RPT_TITLE = "rpt_title";
/*      */   private static final String RPT_CREATE_DATE = "rpt_create_date";
/*      */   private static final String RPT_TABLE_NAME = "rpt_table_name";
/*      */   private static final String RPT_REPOSITORY_ID = "rpt_repository_id";
/*      */   private static final String RPT_REPORT_DEFINITION_ID = "rpt_rptDef_id";
/*      */   private static final int RPTJOBID_LENGTH = 64;
/*      */   private static final int PARAMETER_NAME_LENGTH = 64;
/*      */   private static final int PARAMETER_VALUE_LENGTH = 512;
/*      */   private static final int MAX_STRING_LENGTH = 1200;
/*      */   private static final char SEPARATOR = ',';
/*      */   private static final String W3C_DATE_FORMAT_WITH_ZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
/*      */   private Repository repository;
/*      */   private Repository rptRepository;
/*      */   private String rptCreator;
/*      */   private String rptUniqueCreator;
/*      */   private Date rptCreateDate;
/*  101 */   private int retrievalPageSize = 10000;
/*      */   private DBDescriptor dbDescriptor;
/*  103 */   private DB_Services dbServices = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Report_Services(DBDescriptor dbDescriptor)
/*      */     throws SQLException, NamingException
/*      */   {
/*  113 */     Tracer.traceMethodEntry(new Object[] { dbDescriptor });
/*  114 */     Util.ckNullObjParam("dbDescriptor", dbDescriptor);
/*      */     
/*  116 */     this.dbDescriptor = dbDescriptor;
/*  117 */     this.rptCreateDate = new Date();
/*      */     
/*  119 */     if ((dbDescriptor instanceof JNDI_DBDescriptor))
/*      */     {
/*  121 */       String jndiResource = ((JNDI_DBDescriptor)dbDescriptor).getJNDIDataSource();
/*  122 */       InitialContext initialContext = new InitialContext();
/*  123 */       DataSource dataSource = (DataSource)initialContext.lookup(jndiResource);
/*  124 */       Connection jdbcConnection = dataSource.getConnection();
/*  125 */       DatabaseMetaData metaData = jdbcConnection.getMetaData();
/*  126 */       String databaseProduct = metaData.getDatabaseProductName();
/*  127 */       if (databaseProduct != null)
/*      */       {
/*  129 */         if (databaseProduct.toUpperCase().contains("DB2".toUpperCase()))
/*      */         {
/*  131 */           this.dbServices = new DB2_Services();
/*      */         }
/*  133 */         else if (databaseProduct.toUpperCase().contains("oracle".toUpperCase()))
/*      */         {
/*  135 */           this.dbServices = new Oracle_Services();
/*      */         }
/*  137 */         else if (databaseProduct.toUpperCase().contains("SQL Server".toUpperCase()))
/*      */         {
/*  139 */           this.dbServices = new Sql_Services();
/*      */         }
/*      */       }
/*      */     }
/*  143 */     else if ((dbDescriptor instanceof JDBC_DBDescriptor))
/*      */     {
/*  145 */       DBType dsType = dbDescriptor.getDatabaseType();
/*  146 */       if (dsType == DBType.DB2)
/*      */       {
/*  148 */         this.dbServices = new DB2_Services();
/*      */       }
/*  150 */       else if (dsType == DBType.Oracle)
/*      */       {
/*  152 */         this.dbServices = new Oracle_Services();
/*      */       }
/*  154 */       else if (dsType == DBType.MSSql)
/*      */       {
/*  156 */         this.dbServices = new Sql_Services();
/*      */       }
/*      */     }
/*  159 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRetrievalPageSize(int pageSize)
/*      */   {
/*  175 */     this.retrievalPageSize = pageSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getReportQueries(String reportDefIden, ObjectStore jaceObjStore, Map<String, Object> reportParams)
/*      */   {
/*  196 */     Tracer.traceMethodEntry(new Object[] { reportDefIden, jaceObjStore, reportParams });
/*  197 */     Repository repository = P8CE_Convert.fromP8CE(jaceObjStore);
/*  198 */     Tracer.traceMethodExit(new Object[0]);
/*      */     
/*  200 */     return getReportQueries(reportDefIden, repository, reportParams);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<String> getReportQueries(String reportDefIden, Repository repository, Map<String, Object> reportParams)
/*      */   {
/*  221 */     Tracer.traceMethodEntry(new Object[] { reportDefIden, repository, reportParams });
/*  222 */     List<String> results = new ArrayList();
/*  223 */     ReportDefinition rptDefinition = RMFactory.ReportDefinition.fetchInstance((FilePlanRepository)repository, reportDefIden, null);
/*  224 */     List<ReportParameter> rptDefParams = rptDefinition.getReportParameters();
/*      */     
/*  226 */     validateReportParameters(rptDefParams, reportParams);
/*      */     
/*  228 */     List<String> entityTypeList = new ArrayList();
/*  229 */     if (reportParams.containsKey("rm_entity_type"))
/*      */     {
/*  231 */       entityTypeList = (List)reportParams.get("rm_entity_type");
/*      */     }
/*      */     
/*      */ 
/*  235 */     if ((entityTypeList == null) || (entityTypeList.size() == 0)) {
/*  236 */       entityTypeList.add("Common");
/*      */     }
/*      */     
/*  239 */     for (String entityType : entityTypeList) {
/*  240 */       String[] rptQueries = rptDefinition.getReportQueriesByAssociatedEntityType(entityType);
/*  241 */       for (String rptDefQuery : rptQueries) {
/*  242 */         if (rptDefQuery.length() > 0)
/*      */         {
/*  244 */           String rptQuery = buildQuery(rptDefQuery, reportParams);
/*  245 */           results.add(rptQuery);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  285 */     Tracer.traceMethodExit(new Object[] { results });
/*  286 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */   private void validateReportParameters(List<ReportParameter> rptDefParams, Map<String, Object> reportParams)
/*      */   {
/*  292 */     Tracer.traceMethodEntry(new Object[] { rptDefParams, reportParams });
/*      */     
/*      */ 
/*  295 */     for (int i = 0; i < rptDefParams.size(); i++)
/*      */     {
/*  297 */       ReportParameter rptDefParam = (ReportParameter)rptDefParams.get(i);
/*  298 */       if (rptDefParam.isRequired())
/*      */       {
/*  300 */         String rptDefParamName = rptDefParam.getName();
/*  301 */         if (reportParams.containsKey(rptDefParamName))
/*      */         {
/*  303 */           Object paramValue = reportParams.get(rptDefParamName);
/*  304 */           boolean isValid = false;
/*  305 */           if (paramValue != null)
/*      */           {
/*  307 */             isValid = true;
/*  308 */             if ((paramValue instanceof String))
/*      */             {
/*  310 */               if (((String)paramValue).trim().length() == 0) {
/*  311 */                 isValid = false;
/*      */               }
/*  313 */             } else if ((paramValue instanceof List))
/*      */             {
/*  315 */               if (((List)paramValue).size() == 0)
/*  316 */                 isValid = false;
/*      */             }
/*      */           }
/*  319 */           if (!isValid)
/*      */           {
/*  321 */             throw RptRuntimeException.createRptRuntimeException(RptErrorCode.RPTENG_INVALID_VALUE_FOR_REQUIRED_PARAMETER, new Object[] { rptDefParamName });
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  326 */           throw RptRuntimeException.createRptRuntimeException(RptErrorCode.RPTENG_NO_AVAILABLE_REQUIRED_PARAMETER, new Object[] { rptDefParamName });
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  332 */     Set<Map.Entry<String, Object>> paramEntries = reportParams.entrySet();
/*  333 */     for (Map.Entry<String, Object> entry : paramEntries)
/*      */     {
/*  335 */       boolean found = false;
/*  336 */       String paramName = (String)entry.getKey();
/*  337 */       for (int i = 0; i < rptDefParams.size(); i++)
/*      */       {
/*  339 */         ReportParameter rptDefParam = (ReportParameter)rptDefParams.get(i);
/*  340 */         String rptDefParamName = rptDefParam.getName();
/*  341 */         if ((rptDefParamName != null) && (rptDefParamName.compareTo(paramName) == 0))
/*      */         {
/*      */ 
/*  344 */           found = true;
/*  345 */           break;
/*      */         }
/*      */       }
/*  348 */       if (!found)
/*      */       {
/*  350 */         throw RptRuntimeException.createRptRuntimeException(RptErrorCode.RPTENG_INPUT_PARAMETER_NOT_DEFINED, new Object[] { paramName });
/*      */       }
/*      */     }
/*      */     
/*  354 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   private String buildQuery(String reportQuery, Map<String, Object> reportParams)
/*      */   {
/*  359 */     Tracer.traceMethodEntry(new Object[] { reportQuery, reportParams });
/*  360 */     String results = null;
/*  361 */     int paramStartPos = 0;
/*  362 */     int paramEndPos = 0;
/*  363 */     String param = null;
/*      */     
/*  365 */     QueryParser queryParser = new QueryParser(reportQuery);
/*  366 */     results = queryParser.getSelectFromStatement();
/*  367 */     QueryParser.WhereCondition[] whereConditions = queryParser.getWhereConditions();
/*      */     
/*  369 */     int addCountition = 0;
/*  370 */     for (int i = 0; i < whereConditions.length; i++)
/*      */     {
/*  372 */       String condition = whereConditions[i].condition;
/*  373 */       String operator = whereConditions[i].operator;
/*  374 */       paramStartPos = condition.indexOf("{?");
/*  375 */       if (paramStartPos < 0)
/*      */       {
/*  377 */         if (addCountition == 0) {
/*  378 */           results = results + "WHERE " + condition;
/*      */         } else
/*  380 */           results = results + operator + " " + condition;
/*  381 */         addCountition++;
/*      */       }
/*      */       else
/*      */       {
/*  385 */         paramEndPos = condition.indexOf("}");
/*  386 */         param = condition.substring(paramStartPos + 2, paramEndPos);
/*  387 */         if (reportParams.containsKey(param))
/*      */         {
/*  389 */           String paramValue = (String)reportParams.get(param);
/*  390 */           if ((paramValue != null) && (paramValue.length() > 0))
/*      */           {
/*  392 */             param = "{?" + param + "}";
/*  393 */             condition = condition.replace(param, getFormattedString(paramValue));
/*  394 */             if (addCountition == 0) {
/*  395 */               results = results + "WHERE " + condition;
/*      */             } else
/*  397 */               results = results + operator + " " + condition;
/*  398 */             addCountition++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  408 */     Tracer.traceMethodExit(new Object[] { results });
/*  409 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReportDataResult generateReportData(String reportDefIden, ObjectStore jaceObjStore, Map<String, Object> reportParams, String jobId)
/*      */     throws SQLException, Exception
/*      */   {
/*  432 */     Tracer.traceMethodEntry(new Object[] { reportDefIden, jaceObjStore, reportParams, jobId });
/*  433 */     Repository repository = P8CE_Convert.fromP8CE(jaceObjStore);
/*      */     
/*  435 */     ReportDataResult reportDataResult = generateReportData(reportDefIden, repository, reportParams, jobId);
/*      */     
/*  437 */     Tracer.traceMethodExit(new Object[] { reportDataResult });
/*  438 */     return reportDataResult;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReportDataResult generateReportData(String reportDefIden, Repository repository, Map<String, Object> reportParams, String jobId)
/*      */     throws SQLException, Exception
/*      */   {
/*  461 */     Tracer.traceMethodEntry(new Object[] { reportDefIden, repository, reportParams, jobId });
/*  462 */     int totalRowsAdded = 0;
/*  463 */     this.repository = repository;
/*  464 */     RMUser currentUser = repository.getDomain().fetchCurrentUser();
/*  465 */     this.rptCreator = currentUser.getShortName();
/*  466 */     this.rptUniqueCreator = currentUser.getDistinguishedName();
/*      */     
/*  468 */     if ((jobId == null) || (jobId.length() <= 0)) {
/*  469 */       jobId = Id.createId().toString();
/*      */     }
/*  471 */     ReportDataResultImpl reportDataResult = new ReportDataResultImpl(jobId);
/*  472 */     ReportDefinition rptDefinition = RMFactory.ReportDefinition.fetchInstance((FilePlanRepository)repository, reportDefIden, null);
/*      */     
/*  474 */     List<String> rptQueries = getReportQueries(reportDefIden, repository, reportParams);
/*      */     
/*  476 */     if (reportParams.containsKey("ros_name"))
/*      */     {
/*  478 */       String rosName = (String)reportParams.get("ros_name");
/*  479 */       this.rptRepository = RMFactory.Repository.fetchInstance(repository.getDomain(), rosName, null);
/*      */     }
/*      */     else {
/*  482 */       this.rptRepository = repository;
/*      */     }
/*  484 */     if (this.dbDescriptor != null)
/*      */     {
/*  486 */       if ((this.dbDescriptor instanceof JDBC_DBDescriptor)) {
/*  487 */         this.dbServices.open(((JDBC_DBDescriptor)this.dbDescriptor).getConnectionContext());
/*  488 */       } else if ((this.dbDescriptor instanceof JNDI_DBDescriptor))
/*      */       {
/*  490 */         this.dbServices.open(((JNDI_DBDescriptor)this.dbDescriptor).getJNDIDataSource());
/*  491 */         this.dbServices.setSchemaName(((JNDI_DBDescriptor)this.dbDescriptor).getSchemaName());
/*      */       }
/*      */       
/*  494 */       if (this.dbServices.isTableExist("IERRPTPARAM"))
/*      */       {
/*  496 */         if (this.dbServices.isDataExist(jobId, "RptJobId", "IERRPTPARAM"))
/*      */         {
/*  498 */           throw RptRuntimeException.createRptRuntimeException(RptErrorCode.RPTENG_REPORT_IDENTIFIER_NOT_UNIQUE, new Object[] { "IERRPTPARAM", jobId });
/*      */         }
/*      */       }
/*      */       
/*  502 */       addReportParametersToDB(reportParams, jobId, rptDefinition);
/*  503 */       totalRowsAdded = generateReportDataToDB(rptDefinition, rptQueries, this.rptRepository, jobId);
/*      */     }
/*      */     
/*      */ 
/*  507 */     reportDataResult.setTotalRowsProcessed(totalRowsAdded);
/*      */     
/*  509 */     Tracer.traceMethodExit(new Object[] { reportDataResult });
/*  510 */     return reportDataResult;
/*      */   }
/*      */   
/*      */   private void addReportParametersToDB(Map<String, Object> reportParams, String jobId, ReportDefinition reportDefinition) throws SQLException, Exception
/*      */   {
/*  515 */     Vector<QueryParser.SelectItem> vSelectList = new Vector();
/*  516 */     QueryParser.SelectItem item = new QueryParser.SelectItem();
/*  517 */     item.propName = "RptJobId";
/*  518 */     item.propType = DataType.String.getIntValue();
/*  519 */     item.propSize = 64;
/*  520 */     vSelectList.add(item);
/*      */     
/*  522 */     item = new QueryParser.SelectItem();
/*  523 */     item.propName = "Parameter_Name";
/*  524 */     item.propType = DataType.String.getIntValue();
/*  525 */     item.propSize = 64;
/*  526 */     vSelectList.add(item);
/*      */     
/*  528 */     item = new QueryParser.SelectItem();
/*  529 */     item.propName = "Parameter_Value";
/*  530 */     item.propType = DataType.String.getIntValue();
/*  531 */     item.propSize = 512;
/*  532 */     vSelectList.add(item);
/*      */     
/*  534 */     QueryParser.SelectItem[] columnArray = new QueryParser.SelectItem[vSelectList.size()];
/*  535 */     Object[] obj = vSelectList.toArray();
/*      */     
/*  537 */     for (int i = 0; i < obj.length; i++) {
/*  538 */       columnArray[i] = ((QueryParser.SelectItem)obj[i]);
/*      */     }
/*  540 */     if (!this.dbServices.isTableExist("IERRPTPARAM"))
/*      */     {
/*  542 */       this.dbServices.createTable("IERRPTPARAM", columnArray);
/*      */     }
/*      */     
/*  545 */     Map<String, Object> rptParams = new HashMap();
/*  546 */     if (reportParams != null) {
/*  547 */       rptParams.putAll(reportParams);
/*      */     }
/*  549 */     rptParams.put("rpt_creator", this.rptCreator);
/*  550 */     rptParams.put("rpt_unique_creator", this.rptUniqueCreator);
/*  551 */     rptParams.put("rpt_title", reportDefinition.getName());
/*  552 */     rptParams.put("rpt_rptDef_id", reportDefinition.getObjectIdentity());
/*  553 */     SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
/*  554 */     dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
/*  555 */     rptParams.put("rpt_create_date", dateFormatter.format(this.rptCreateDate));
/*  556 */     rptParams.put("rpt_table_name", reportDefinition.getReportDBTableName());
/*  557 */     rptParams.put("rpt_repository_id", this.repository.getObjectIdentity());
/*      */     
/*  559 */     Set<Map.Entry<String, Object>> paramEntries = rptParams.entrySet();
/*  560 */     int totalRowsAdded = 0;
/*  561 */     for (Map.Entry<String, Object> entry : paramEntries)
/*      */     {
/*  563 */       String entryName = (String)entry.getKey();
/*  564 */       Object entryValue = entry.getValue();
/*  565 */       String paramValue = null;
/*  566 */       if (entryValue != null)
/*      */       {
/*  568 */         if ((entryValue instanceof String))
/*      */         {
/*  570 */           if (((String)entryValue).trim().length() != 0) {
/*  571 */             paramValue = (String)entryValue;
/*      */           }
/*  573 */         } else if ((entryValue instanceof List))
/*      */         {
/*      */ 
/*  576 */           List<String> strList = (List)entryValue;
/*  577 */           for (int i = 0; i < strList.size(); i++)
/*      */           {
/*  579 */             String aStr = (String)strList.get(i);
/*  580 */             if (i == 0) {
/*  581 */               paramValue = '"' + aStr + '"';
/*      */             } else
/*  583 */               paramValue = paramValue + ',' + '"' + aStr + '"';
/*      */           }
/*      */         }
/*      */       }
/*  587 */       RMProperties paramProps = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/*  588 */       paramProps.putStringValue("RptJobId", jobId);
/*  589 */       paramProps.putStringValue("Parameter_Name", entryName);
/*  590 */       paramProps.putStringValue("Parameter_Value", paramValue);
/*      */       
/*  592 */       RMProperty rowProp = null;
/*  593 */       LinkedHashSet<RMProperty> paramPropsSet = new LinkedHashSet();
/*  594 */       for (Iterator<RMProperty> it = paramProps.iterator(); it.hasNext();)
/*      */       {
/*  596 */         rowProp = (RMProperty)it.next();
/*  597 */         paramPropsSet.add(rowProp);
/*      */       }
/*      */       
/*  600 */       LinkedHashMap<String, ColumnDescription> columnDescriptions = this.dbServices.getColumnDescription(paramPropsSet);
/*  601 */       PreparedStatement pStmt = null;
/*  602 */       pStmt = this.dbServices.createPreparedStatement("IERRPTPARAM", columnDescriptions);
/*      */       
/*      */ 
/*  605 */       totalRowsAdded += this.dbServices.performInsert(pStmt, paramPropsSet, columnDescriptions);
/*  606 */       pStmt.close();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReportDataResult deleteReportData(String jobId, String reportDefIden, ObjectStore jaceObjStore)
/*      */     throws SQLException, Exception
/*      */   {
/*  624 */     Tracer.traceMethodEntry(new Object[] { jobId, reportDefIden, jaceObjStore });
/*  625 */     Repository repository = P8CE_Convert.fromP8CE(jaceObjStore);
/*      */     
/*  627 */     ReportDataResult reportDataResult = deleteReportData(jobId, reportDefIden, repository);
/*      */     
/*  629 */     Tracer.traceMethodExit(new Object[] { reportDataResult });
/*  630 */     return reportDataResult;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReportDataResult deleteReportData(String jobId, String tableName)
/*      */     throws SQLException, Exception
/*      */   {
/*  647 */     Tracer.traceMethodEntry(new Object[] { jobId, tableName });
/*      */     
/*  649 */     ReportDataResult reportDataResult = deleteReportDataHelper(jobId, tableName);
/*      */     
/*  651 */     Tracer.traceMethodExit(new Object[] { reportDataResult });
/*  652 */     return reportDataResult;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ReportDataResult deleteReportData(String jobId, String reportDefIden, Repository repository)
/*      */     throws SQLException, Exception
/*      */   {
/*  665 */     Tracer.traceMethodEntry(new Object[] { jobId, reportDefIden });
/*  666 */     ReportDefinition rptDefinition = RMFactory.ReportDefinition.fetchInstance((FilePlanRepository)repository, reportDefIden, null);
/*  667 */     String tableName = rptDefinition.getReportDBTableName();
/*      */     
/*  669 */     ReportDataResult reportDataResult = deleteReportDataHelper(jobId, tableName);
/*      */     
/*  671 */     Tracer.traceMethodExit(new Object[] { reportDataResult });
/*  672 */     return reportDataResult;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ReportDataResult deleteReportDataHelper(String jobId, String tableName)
/*      */     throws SQLException, Exception
/*      */   {
/*  685 */     Tracer.traceMethodEntry(new Object[] { jobId, tableName });
/*      */     
/*  687 */     if ((this.dbDescriptor instanceof JDBC_DBDescriptor)) {
/*  688 */       this.dbServices.open(((JDBC_DBDescriptor)this.dbDescriptor).getConnectionContext());
/*  689 */     } else if ((this.dbDescriptor instanceof JNDI_DBDescriptor))
/*      */     {
/*  691 */       this.dbServices.open(((JNDI_DBDescriptor)this.dbDescriptor).getJNDIDataSource());
/*  692 */       this.dbServices.setSchemaName(((JNDI_DBDescriptor)this.dbDescriptor).getSchemaName());
/*      */     }
/*      */     
/*  695 */     this.dbServices.performDelete(jobId, "IERRPTPARAM");
/*      */     
/*  697 */     ReportDataResultImpl reportDataResult = new ReportDataResultImpl(jobId);
/*  698 */     int totalRowsDeleted = this.dbServices.performDelete(jobId, tableName);
/*  699 */     this.dbServices.close();
/*  700 */     reportDataResult.setTotalRowsProcessed(totalRowsDeleted);
/*      */     
/*  702 */     Tracer.traceMethodExit(new Object[] { reportDataResult });
/*  703 */     return reportDataResult;
/*      */   }
/*      */   
/*      */ 
/*      */   private int generateReportDataToDB(ReportDefinition rptDefinition, List<String> rptQueries, Repository rptRepos, String jobId)
/*      */     throws SQLException, Exception
/*      */   {
/*  710 */     Tracer.traceMethodEntry(new Object[] { rptDefinition, rptQueries, rptRepos, jobId });
/*  711 */     int totalRowsAdded = 0;
/*      */     
/*  713 */     String tableName = rptDefinition.getReportDBTableName();
/*  714 */     if ((tableName == null) || (tableName.length() <= 0)) {
/*  715 */       throw RptRuntimeException.createRptRuntimeException(RptErrorCode.RPTENG_DB_TABLE_NAME_NOT_DEFINED, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  726 */     if (!this.dbServices.isTableExist(tableName))
/*      */     {
/*  728 */       List<QueryParser.SelectItem> mergedSelectList = new ArrayList();
/*  729 */       List<QueryParser.FromClass> mergedFromList = new ArrayList();
/*  730 */       for (int i = 0; i < rptQueries.size(); i++)
/*      */       {
/*  732 */         QueryParser queryParser = new QueryParser((String)rptQueries.get(i));
/*      */         
/*      */ 
/*  735 */         QueryParser.SelectItem[] selectItemList = queryParser.getSelectList();
/*  736 */         for (QueryParser.SelectItem selectItem : selectItemList)
/*      */         {
/*  738 */           boolean found = false;
/*  739 */           for (int j = 0; j < mergedSelectList.size(); j++)
/*      */           {
/*  741 */             if (((selectItem.alias != null) && (((QueryParser.SelectItem)mergedSelectList.get(j)).alias != null) && (selectItem.alias.compareToIgnoreCase(((QueryParser.SelectItem)mergedSelectList.get(j)).alias) == 0)) || ((selectItem.propName != null) && (((QueryParser.SelectItem)mergedSelectList.get(j)).propName != null) && (selectItem.propName.compareToIgnoreCase(((QueryParser.SelectItem)mergedSelectList.get(j)).propName) == 0)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  746 */               found = true;
/*  747 */               break;
/*      */             }
/*      */           }
/*  750 */           if (!found)
/*      */           {
/*  752 */             mergedSelectList.add(selectItem);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  757 */         QueryParser.FromClass[] fromClassList = queryParser.getFromClasses();
/*  758 */         for (QueryParser.FromClass fromClass : fromClassList)
/*      */         {
/*  760 */           boolean found = false;
/*  761 */           for (int j = 0; j < mergedFromList.size(); j++)
/*      */           {
/*  763 */             if ((fromClass.alias != null) && (((QueryParser.FromClass)mergedFromList.get(j)).alias != null) && (fromClass.alias.compareToIgnoreCase(((QueryParser.FromClass)mergedFromList.get(j)).alias) == 0) && (fromClass.className != null) && (((QueryParser.FromClass)mergedFromList.get(j)).className != null) && (fromClass.className.compareToIgnoreCase(((QueryParser.FromClass)mergedFromList.get(j)).className) == 0))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  768 */               found = true;
/*  769 */               break;
/*      */             }
/*      */           }
/*  772 */           if (!found)
/*      */           {
/*  774 */             mergedFromList.add(fromClass);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  779 */       List<QueryParser.SelectItem> retSelectList = getSelectPropertyLength(mergedSelectList, mergedFromList);
/*  780 */       if (retSelectList.size() > 0)
/*      */       {
/*  782 */         QueryParser.SelectItem[] newSelectList = addDefaultItemsToSelectList(retSelectList);
/*  783 */         this.dbServices.createTable(tableName, newSelectList);
/*      */       }
/*      */     }
/*      */     
/*  787 */     for (int i = 0; i < rptQueries.size(); i++)
/*      */     {
/*  789 */       String sqlString = (String)rptQueries.get(i);
/*  790 */       totalRowsAdded += executeQueryToDB(this.dbServices, rptRepos, jobId, tableName, sqlString);
/*      */     }
/*      */     
/*  793 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(totalRowsAdded) });
/*  794 */     return totalRowsAdded;
/*      */   }
/*      */   
/*      */   private int executeQueryToDB(DB_Services dbServices, Repository rptRepos, String jobId, String tableName, String sqlString)
/*      */     throws SQLException
/*      */   {
/*  800 */     Tracer.traceMethodEntry(new Object[] { dbServices, rptRepos, jobId, tableName, sqlString });
/*  801 */     int totalRowsAdded = 0;
/*      */     
/*      */ 
/*  804 */     RMSearch rmSearch = new RMSearch(rptRepos);
/*  805 */     RMFilteredPropertyType jarmFPT = RMFilteredPropertyType.SingletonString;
/*  806 */     RMPropertyFilter jarmPF = new RMPropertyFilter();
/*  807 */     jarmPF.addIncludeType(Integer.valueOf(0), null, null, jarmFPT, null);
/*  808 */     PageableSet<ResultRow> rows = rmSearch.fetchRows(sqlString, Integer.valueOf(this.retrievalPageSize), jarmPF, Boolean.TRUE);
/*      */     
/*  810 */     RMProperties addProps = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
/*  811 */     addProps.putStringValue("RptJobId", jobId);
/*      */     
/*  813 */     LinkedHashMap<String, ColumnDescription> columnDescriptions = null;
/*  814 */     PreparedStatement pStmt = null;
/*  815 */     RMPageIterator<ResultRow> pi = rows.pageIterator();
/*  816 */     int elemCount = 0;
/*      */     try {
/*      */       List<ResultRow> pageContents;
/*  819 */       while (pi.nextPage())
/*      */       {
/*  821 */         pageContents = pi.getCurrentPage();
/*  822 */         for (ResultRow row : pageContents)
/*      */         {
/*  824 */           elemCount++;
/*  825 */           RMProperties rmProps = row.getProperties();
/*  826 */           LinkedHashSet<RMProperty> newRowProps = mergeProperties(rmProps, addProps);
/*      */           
/*  828 */           if (columnDescriptions == null)
/*      */           {
/*  830 */             columnDescriptions = dbServices.getColumnDescription(newRowProps);
/*      */           }
/*  832 */           pStmt = dbServices.createPreparedStatement(tableName, columnDescriptions);
/*      */           
/*      */ 
/*  835 */           totalRowsAdded += dbServices.performInsert(pStmt, newRowProps, columnDescriptions);
/*  836 */           pStmt.close();
/*      */         }
/*      */       }
/*      */       
/*  840 */       Tracer.traceMethodExit(new Object[] { Integer.valueOf(totalRowsAdded) });
/*  841 */       return totalRowsAdded;
/*      */     }
/*      */     finally
/*      */     {
/*      */       try
/*      */       {
/*  847 */         if (totalRowsAdded > 0) {
/*  848 */           dbServices.close(pStmt);
/*      */         }
/*      */       }
/*      */       catch (Exception cause) {
/*  852 */         throw RptRuntimeException.createRptRuntimeException(cause, RptErrorCode.RPTENG_ERROR_DB_SERVICE_CLOSE, new Object[0]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private LinkedHashSet<RMProperty> mergeProperties(RMProperties rowProps, RMProperties addProps)
/*      */   {
/*  859 */     Tracer.traceMethodEntry(new Object[] { rowProps, addProps });
/*  860 */     RMProperty rowProp = null;
/*  861 */     LinkedHashSet<RMProperty> newPropsSet = new LinkedHashSet();
/*  862 */     for (Iterator<RMProperty> it = addProps.iterator(); it.hasNext();)
/*      */     {
/*  864 */       rowProp = (RMProperty)it.next();
/*  865 */       newPropsSet.add(rowProp);
/*      */     }
/*  867 */     for (Iterator<RMProperty> it = rowProps.iterator(); it.hasNext();)
/*      */     {
/*  869 */       rowProp = (RMProperty)it.next();
/*  870 */       newPropsSet.add(rowProp);
/*      */     }
/*      */     
/*  873 */     Tracer.traceMethodExit(new Object[] { newPropsSet });
/*  874 */     return newPropsSet;
/*      */   }
/*      */   
/*      */   private List<QueryParser.SelectItem> getSelectPropertyLength(List<QueryParser.SelectItem> selectProps, List<QueryParser.FromClass> fromClass)
/*      */   {
/*  879 */     Tracer.traceMethodEntry(new Object[] { selectProps, fromClass });
/*  880 */     int isize = selectProps.size();
/*  881 */     QueryParser.SelectItem item; boolean bFoundProp; for (int i = 0; i < isize; i++)
/*      */     {
/*  883 */       item = (QueryParser.SelectItem)selectProps.get(i);
/*  884 */       for (int j = 0; j < fromClass.size(); j++)
/*      */       {
/*  886 */         QueryParser.FromClass aClass = (QueryParser.FromClass)fromClass.get(j);
/*  887 */         if ((item.tableName == null) || (item.tableName.compareToIgnoreCase(aClass.alias) == 0))
/*      */         {
/*  889 */           item.tableName = aClass.className;
/*  890 */           break;
/*      */         }
/*      */       }
/*  893 */       RMClassDescription rmClassDesc = RMFactory.RMClassDescription.fetchInstance(this.rptRepository, item.tableName, RMPropertyFilter.MinimumPropertySet);
/*  894 */       List<RMPropertyDescription> propDescs = rmClassDesc.getPropertyDescriptions();
/*      */       
/*  896 */       bFoundProp = false;
/*  897 */       for (RMPropertyDescription propDesc : propDescs)
/*      */       {
/*  899 */         String propSymName = propDesc.getSymbolicName();
/*  900 */         if (propSymName.equalsIgnoreCase(item.propName))
/*      */         {
/*  902 */           item.propType = propDesc.getDataType().getIntValue();
/*  903 */           item.cardinality = propDesc.getCardinality().getIntValue();
/*  904 */           if (propDesc.getDataType() == DataType.String)
/*      */           {
/*  906 */             RMPropertyDescriptionString jarmPDStr = (RMPropertyDescriptionString)propDesc;
/*  907 */             Integer maxStrLen = jarmPDStr.getStringMaximumLength();
/*  908 */             item.propSize = (maxStrLen != null ? maxStrLen.intValue() : 1200);
/*      */           }
/*  910 */           bFoundProp = true;
/*  911 */           break;
/*      */         }
/*      */       }
/*      */       
/*  915 */       if (!bFoundProp)
/*      */       {
/*  917 */         propDescs = rmClassDesc.getAllDescendentPropertyDescriptions();
/*  918 */         for (RMPropertyDescription propDesc : propDescs)
/*      */         {
/*  920 */           String propSymName = propDesc.getSymbolicName();
/*  921 */           if (propSymName.equalsIgnoreCase(item.propName))
/*      */           {
/*  923 */             item.propType = propDesc.getDataType().getIntValue();
/*  924 */             if (propDesc.getDataType() == DataType.String)
/*      */             {
/*  926 */               RMPropertyDescriptionString jarmPDStr = (RMPropertyDescriptionString)propDesc;
/*  927 */               Integer maxStrLen = jarmPDStr.getStringMaximumLength();
/*  928 */               item.propSize = (maxStrLen != null ? maxStrLen.intValue() : 1200);
/*      */             }
/*  930 */             bFoundProp = true;
/*  931 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  939 */     List<QueryParser.SelectItem> retSelectList = new ArrayList();
/*  940 */     for (QueryParser.SelectItem selectItem : selectProps)
/*      */     {
/*  942 */       boolean found = false;
/*  943 */       for (int j = 0; j < retSelectList.size(); j++)
/*      */       {
/*  945 */         if (((selectItem.alias != null) && (((QueryParser.SelectItem)retSelectList.get(j)).alias != null) && (selectItem.alias.compareToIgnoreCase(((QueryParser.SelectItem)retSelectList.get(j)).alias) == 0)) || ((selectItem.alias != null) && (((QueryParser.SelectItem)retSelectList.get(j)).propName != null) && (selectItem.alias.compareToIgnoreCase(((QueryParser.SelectItem)retSelectList.get(j)).propName) == 0)) || ((selectItem.propName != null) && (((QueryParser.SelectItem)retSelectList.get(j)).alias != null) && (selectItem.propName.compareToIgnoreCase(((QueryParser.SelectItem)retSelectList.get(j)).alias) == 0)) || ((selectItem.propName != null) && (((QueryParser.SelectItem)retSelectList.get(j)).propName != null) && (selectItem.propName.compareToIgnoreCase(((QueryParser.SelectItem)retSelectList.get(j)).propName) == 0)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  954 */           if (selectItem.propSize > ((QueryParser.SelectItem)retSelectList.get(j)).propSize)
/*      */           {
/*  956 */             ((QueryParser.SelectItem)retSelectList.get(j)).propSize = selectItem.propSize;
/*      */           }
/*  958 */           found = true;
/*  959 */           break;
/*      */         }
/*      */       }
/*  962 */       if (!found)
/*      */       {
/*  964 */         retSelectList.add(selectItem);
/*      */       }
/*      */     }
/*      */     
/*  968 */     Tracer.traceMethodExit(new Object[] { retSelectList });
/*  969 */     return retSelectList;
/*      */   }
/*      */   
/*      */   private QueryParser.SelectItem[] addDefaultItemsToSelectList(List<QueryParser.SelectItem> selectItemList)
/*      */   {
/*  974 */     Tracer.traceMethodEntry(new Object[] { selectItemList });
/*  975 */     int propCount = 0;
/*  976 */     QueryParser.SelectItem[] newSelectList = new QueryParser.SelectItem[selectItemList.size() + 1];
/*  977 */     QueryParser.SelectItem entry = new QueryParser.SelectItem();
/*  978 */     entry.propName = "RptJobId";
/*  979 */     entry.propType = DataType.String.getIntValue();
/*  980 */     entry.propSize = 64;
/*  981 */     newSelectList[(propCount++)] = entry;
/*      */     
/*  983 */     for (int i = 0; i < selectItemList.size(); i++)
/*      */     {
/*  985 */       newSelectList[(propCount + i)] = ((QueryParser.SelectItem)selectItemList.get(i));
/*      */     }
/*      */     
/*  988 */     Tracer.traceMethodExit((Object[])newSelectList);
/*  989 */     return newSelectList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String getFormattedString(String stringValue)
/*      */   {
/* 1000 */     String formattedString = stringValue;
/* 1001 */     if (formattedString != null) {
/* 1002 */       formattedString = formattedString.replaceAll("'", "''");
/*      */     }
/* 1004 */     return formattedString;
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\service\Report_Services.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */