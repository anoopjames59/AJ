/*     */ package com.ibm.ier.report.db.impl;
/*     */ 
/*     */ import com.ibm.ier.report.util.QueryParser.SelectItem;
/*     */ import com.ibm.ier.report.util.RptTracer;
/*     */ import com.ibm.ier.report.util.RptTracer.SubSystem;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class Oracle_Services
/*     */   extends DB_ServicesImpl
/*     */ {
/*  30 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String ORACLE_DriverClass = "oracle.jdbc.OracleDriver";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String VARCHAR_Type = "VARCHAR2(255)";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void open(Map<String, String> context)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/*  47 */     Tracer.traceMethodEntry(new Object[] { context });
/*  48 */     String server = (String)context.get("DB_Server");
/*  49 */     String port = (String)context.get("DB_Port");
/*  50 */     String database = (String)context.get("DB_Database");
/*  51 */     String username = (String)context.get("DB_Username");
/*  52 */     String password = (String)context.get("DB_Password");
/*  53 */     this.schemaName = ((String)context.get("DB_Schema"));
/*     */     
/*  55 */     Class.forName("oracle.jdbc.OracleDriver");
/*  56 */     String url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + database;
/*  57 */     this.jdbcConnection = DriverManager.getConnection(url, username, password);
/*  58 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createTable(String tableName, QueryParser.SelectItem[] selectPropList)
/*     */     throws SQLException
/*     */   {
/*  71 */     Tracer.traceMethodEntry(new Object[] { tableName, selectPropList });
/*     */     
/*  73 */     Set<String> columnNamesAlreadyInUse = new HashSet();
/*     */     
/*  75 */     StringBuilder sb = new StringBuilder();
/*  76 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/*  77 */       sb.append("CREATE TABLE ").append(this.schemaName).append('.').append(tableName).append(" (");
/*     */     } else
/*  79 */       sb.append("CREATE TABLE ").append(tableName).append(" (");
/*  80 */     String columnName = null;
/*  81 */     String columnType = null;
/*  82 */     int columnCount = 0;
/*  83 */     for (int i = 0; i < selectPropList.length; i++)
/*     */     {
/*  85 */       QueryParser.SelectItem item = selectPropList[i];
/*  86 */       if ((item != null) && (item.propName != null))
/*     */       {
/*  88 */         if (item.alias != null) {
/*  89 */           columnName = item.alias;
/*     */         } else {
/*  91 */           columnName = item.propName;
/*     */         }
/*  93 */         int j = 0;
/*  94 */         while (columnNamesAlreadyInUse.contains(columnName))
/*     */         {
/*  96 */           j++;
/*  97 */           columnName = columnName + "_" + j;
/*     */         }
/*  99 */         columnNamesAlreadyInUse.add(columnName);
/*     */         
/*     */ 
/* 102 */         DataType propDataType = DataType.getInstanceFromInt(item.propType);
/* 103 */         switch (propDataType)
/*     */         {
/*     */         case String: 
/* 106 */           if (item.propSize == 0) {
/* 107 */             columnType = "VARCHAR2(255)";
/*     */           } else
/* 109 */             columnType = "VARCHAR2(" + selectPropList[i].propSize + ")";
/* 110 */           break;
/*     */         case DateTime: 
/* 112 */           columnType = "TIMESTAMP";
/* 113 */           break;
/*     */         case Integer: 
/* 115 */           columnType = "INTEGER";
/* 116 */           break;
/*     */         case Boolean: 
/* 118 */           columnType = "NUMBER(1)";
/* 119 */           break;
/*     */         case Guid: 
/* 121 */           columnType = "VARCHAR2(38)";
/* 122 */           break;
/*     */         case Double: 
/* 124 */           columnType = "DOUBLE";
/* 125 */           break;
/*     */         case Object: 
/* 127 */           columnType = "VARCHAR2(38)";
/* 128 */           break;
/*     */         }
/*     */         
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */         RMCardinality cardinality = RMCardinality.getInstanceFromInt(item.cardinality);
/* 138 */         if (cardinality == RMCardinality.List)
/*     */         {
/* 140 */           columnType = "VARCHAR2(255)";
/*     */         }
/*     */         
/* 143 */         if (columnCount > 0) {
/* 144 */           sb.append(", ");
/*     */         }
/* 146 */         sb.append('"').append(columnName.toUpperCase()).append("\" ").append(columnType);
/* 147 */         columnCount++;
/*     */       }
/*     */     }
/* 150 */     sb.append(")");
/*     */     
/* 152 */     String sql = sb.toString();
/* 153 */     Statement jdbcStmt = this.jdbcConnection.createStatement();
/* 154 */     jdbcStmt.executeUpdate(sql);
/* 155 */     jdbcStmt.close();
/* 156 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTableExist(String tableName)
/*     */     throws SQLException
/*     */   {
/* 166 */     Tracer.traceMethodEntry(new Object[] { tableName });
/* 167 */     DatabaseMetaData dbm = this.jdbcConnection.getMetaData();
/*     */     
/* 169 */     ResultSet tables = dbm.getTables(null, this.schemaName, tableName, null);
/* 170 */     boolean result = false;
/* 171 */     if (tables.next())
/*     */     {
/* 173 */       result = true;
/*     */     }
/*     */     
/*     */ 
/* 177 */     tables = dbm.getTables(null, this.schemaName, tableName.toUpperCase(), null);
/* 178 */     if (tables.next())
/*     */     {
/* 180 */       result = true;
/*     */     }
/*     */     
/* 183 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 184 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\impl\Oracle_Services.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */