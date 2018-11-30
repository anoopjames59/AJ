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
/*     */ public class Sql_Services
/*     */   extends DB_ServicesImpl
/*     */ {
/*  30 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String SQLSERVER_DriverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String VARCHAR_Type = "NVARCHAR(255)";
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
/*  55 */     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
/*  56 */     String url = "jdbc:sqlserver://" + server + ":" + port + ";DatabaseName=" + database;
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
/*     */ 
/*     */   public void createTable(String tableName, QueryParser.SelectItem[] selectPropList)
/*     */     throws SQLException
/*     */   {
/*  72 */     Tracer.traceMethodEntry(new Object[] { tableName, selectPropList });
/*     */     
/*  74 */     Set<String> columnNamesAlreadyInUse = new HashSet();
/*     */     
/*  76 */     StringBuilder sb = new StringBuilder();
/*  77 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/*  78 */       sb.append("CREATE TABLE ").append(this.schemaName).append('.').append(tableName).append(" (");
/*     */     } else
/*  80 */       sb.append("CREATE TABLE ").append(tableName).append(" (");
/*  81 */     String columnName = null;
/*  82 */     String columnType = null;
/*  83 */     int columnCount = 0;
/*  84 */     for (int i = 0; i < selectPropList.length; i++)
/*     */     {
/*  86 */       QueryParser.SelectItem item = selectPropList[i];
/*  87 */       if ((item != null) && (item.propName != null))
/*     */       {
/*  89 */         if (item.alias != null) {
/*  90 */           columnName = item.alias;
/*     */         } else {
/*  92 */           columnName = item.propName;
/*     */         }
/*  94 */         int j = 0;
/*  95 */         while (columnNamesAlreadyInUse.contains(columnName))
/*     */         {
/*  97 */           j++;
/*  98 */           columnName = columnName + "_" + j;
/*     */         }
/* 100 */         columnNamesAlreadyInUse.add(columnName);
/*     */         
/*     */ 
/* 103 */         DataType propDataType = DataType.getInstanceFromInt(item.propType);
/* 104 */         switch (propDataType)
/*     */         {
/*     */         case String: 
/* 107 */           if (item.propSize == 0) {
/* 108 */             columnType = "NVARCHAR(255)";
/*     */           } else
/* 110 */             columnType = "NVARCHAR(" + selectPropList[i].propSize + ")";
/* 111 */           break;
/*     */         case DateTime: 
/* 113 */           columnType = "DATETIME";
/* 114 */           break;
/*     */         case Integer: 
/* 116 */           columnType = "INTEGER";
/* 117 */           break;
/*     */         case Boolean: 
/* 119 */           columnType = "TINYINT";
/* 120 */           break;
/*     */         case Guid: 
/* 122 */           columnType = "NVARCHAR(38)";
/* 123 */           break;
/*     */         case Double: 
/* 125 */           columnType = "DOUBLE";
/* 126 */           break;
/*     */         case Object: 
/* 128 */           columnType = "NVARCHAR(38)";
/* 129 */           break;
/*     */         }
/*     */         
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 138 */         RMCardinality cardinality = RMCardinality.getInstanceFromInt(item.cardinality);
/* 139 */         if (cardinality == RMCardinality.List)
/*     */         {
/* 141 */           columnType = "NVARCHAR(255)";
/*     */         }
/*     */         
/* 144 */         if (columnCount > 0) {
/* 145 */           sb.append(", ");
/*     */         }
/* 147 */         sb.append('"').append(columnName).append("\" ").append(columnType);
/* 148 */         columnCount++;
/*     */       }
/*     */     }
/* 151 */     sb.append(")");
/*     */     
/* 153 */     String sql = sb.toString();
/* 154 */     Statement jdbcStmt = this.jdbcConnection.createStatement();
/* 155 */     jdbcStmt.executeUpdate(sql);
/* 156 */     jdbcStmt.close();
/* 157 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTableExist(String tableName)
/*     */     throws SQLException
/*     */   {
/* 169 */     Tracer.traceMethodEntry(new Object[] { tableName });
/* 170 */     DatabaseMetaData dbm = this.jdbcConnection.getMetaData();
/*     */     
/* 172 */     ResultSet tables = dbm.getTables(null, this.schemaName, tableName, null);
/* 173 */     boolean result = false;
/* 174 */     if (tables.next())
/*     */     {
/* 176 */       result = true;
/*     */     }
/*     */     
/* 179 */     Tracer.traceMethodExit(new Object[0]);
/* 180 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\impl\Sql_Services.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */