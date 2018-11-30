/*     */ package com.ibm.ier.report.db.impl;
/*     */ 
/*     */ import com.ibm.ier.report.db.ColumnDescription;
/*     */ import com.ibm.ier.report.util.QueryParser.SelectItem;
/*     */ import com.ibm.ier.report.util.RptTracer;
/*     */ import com.ibm.ier.report.util.RptTracer.SubSystem;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public class DB2_Services
/*     */   extends DB_ServicesImpl
/*     */ {
/*  33 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String DB2_DriverClass = "com.ibm.db2.jcc.DB2Driver";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String VARCHAR_Type = "VARCHAR(255)";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void open(Map<String, String> context)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/*  50 */     Tracer.traceMethodEntry(new Object[] { context });
/*  51 */     String server = (String)context.get("DB_Server");
/*  52 */     String port = (String)context.get("DB_Port");
/*  53 */     String database = (String)context.get("DB_Database");
/*  54 */     String username = (String)context.get("DB_Username");
/*  55 */     String password = (String)context.get("DB_Password");
/*  56 */     this.schemaName = ((String)context.get("DB_Schema"));
/*     */     
/*  58 */     Class.forName("com.ibm.db2.jcc.DB2Driver");
/*  59 */     String url = "jdbc:db2://" + server + ":" + port + "/" + database;
/*  60 */     this.jdbcConnection = DriverManager.getConnection(url, username, password);
/*  61 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   public void deleteTable(String tableName) throws SQLException
/*     */   {
/*  66 */     Tracer.traceMethodEntry(new Object[] { tableName });
/*  67 */     StringBuilder sb = new StringBuilder();
/*  68 */     sb.append("DROP TABLE ?");
/*     */     
/*  70 */     StringBuilder tableSQL = new StringBuilder();
/*  71 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/*  72 */       tableSQL.append(this.schemaName).append('.').append(tableName);
/*     */     } else {
/*  74 */       tableSQL.append(tableName);
/*     */     }
/*  76 */     String sql = sb.toString();
/*  77 */     PreparedStatement pStmt = this.jdbcConnection.prepareStatement(sql);
/*     */     
/*  79 */     pStmt.setString(1, tableSQL.toString());
/*     */     try
/*     */     {
/*  82 */       pStmt.executeUpdate();
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/*  86 */       int sqlErrCode = sqlEx.getErrorCode();
/*  87 */       if (sqlErrCode != 65332)
/*     */       {
/*  89 */         throw sqlEx;
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*  94 */       if (pStmt != null)
/*     */       {
/*  96 */         pStmt.close();
/*     */       }
/*     */     }
/*  99 */     Tracer.traceMethodExit(new Object[0]);
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
/* 112 */     Tracer.traceMethodEntry(new Object[] { tableName, selectPropList });
/*     */     
/* 114 */     Set<String> columnNamesAlreadyInUse = new HashSet();
/*     */     
/* 116 */     StringBuilder sb = new StringBuilder();
/* 117 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/* 118 */       sb.append("CREATE TABLE ").append(this.schemaName).append('.').append(tableName).append(" (");
/*     */     } else
/* 120 */       sb.append("CREATE TABLE ").append(tableName).append(" (");
/* 121 */     String columnName = null;
/* 122 */     String columnType = null;
/* 123 */     int columnCount = 0;
/* 124 */     for (int i = 0; i < selectPropList.length; i++)
/*     */     {
/* 126 */       QueryParser.SelectItem item = selectPropList[i];
/* 127 */       if ((item != null) && (item.propName != null))
/*     */       {
/* 129 */         if (item.alias != null) {
/* 130 */           columnName = item.alias;
/*     */         } else {
/* 132 */           columnName = item.propName;
/*     */         }
/* 134 */         int j = 0;
/* 135 */         while (columnNamesAlreadyInUse.contains(columnName))
/*     */         {
/* 137 */           j++;
/* 138 */           columnName = columnName + "_" + j;
/*     */         }
/* 140 */         columnNamesAlreadyInUse.add(columnName);
/*     */         
/*     */ 
/* 143 */         DataType propDataType = DataType.getInstanceFromInt(item.propType);
/* 144 */         switch (propDataType)
/*     */         {
/*     */         case String: 
/* 147 */           if (item.propSize == 0) {
/* 148 */             columnType = "VARCHAR(255)";
/*     */           } else
/* 150 */             columnType = "VARCHAR(" + selectPropList[i].propSize + ")";
/* 151 */           break;
/*     */         case DateTime: 
/* 153 */           columnType = "TIMESTAMP";
/* 154 */           break;
/*     */         case Integer: 
/* 156 */           columnType = "INTEGER";
/* 157 */           break;
/*     */         case Boolean: 
/* 159 */           columnType = "SMALLINT";
/* 160 */           break;
/*     */         case Guid: 
/* 162 */           columnType = "VARCHAR(38)";
/* 163 */           break;
/*     */         case Double: 
/* 165 */           columnType = "DOUBLE";
/* 166 */           break;
/*     */         case Object: 
/* 168 */           columnType = "VARCHAR(38)";
/* 169 */           break;
/*     */         }
/*     */         
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */         RMCardinality cardinality = RMCardinality.getInstanceFromInt(item.cardinality);
/* 179 */         if (cardinality == RMCardinality.List)
/*     */         {
/* 181 */           columnType = "VARCHAR(255)";
/*     */         }
/*     */         
/* 184 */         if (columnCount > 0) {
/* 185 */           sb.append(", ");
/*     */         }
/* 187 */         sb.append(columnName).append(' ').append(columnType);
/* 188 */         columnCount++;
/*     */       }
/*     */     }
/* 191 */     sb.append(")");
/*     */     
/* 193 */     String sql = sb.toString();
/* 194 */     Statement jdbcStmt = this.jdbcConnection.createStatement();
/* 195 */     jdbcStmt.executeUpdate(sql);
/* 196 */     jdbcStmt.close();
/* 197 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public PreparedStatement createPreparedStatement(String tableName, LinkedHashMap<String, ColumnDescription> columnDescriptions)
/*     */     throws SQLException
/*     */   {
/* 211 */     Tracer.traceMethodEntry(new Object[] { tableName, columnDescriptions });
/* 212 */     Set<String> keySet = columnDescriptions.keySet();
/* 213 */     String[] keyNames = (String[])keySet.toArray(new String[keySet.size()]);
/*     */     
/* 215 */     StringBuilder sb = new StringBuilder();
/* 216 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/* 217 */       sb.append("INSERT INTO ").append(this.schemaName).append('.').append(tableName).append(" (");
/*     */     } else
/* 219 */       sb.append("INSERT INTO ").append(tableName).append(" (");
/* 220 */     for (int i = 0; i < keyNames.length; i++)
/*     */     {
/* 222 */       if (i > 0) {
/* 223 */         sb.append(", ");
/*     */       }
/* 225 */       sb.append(((ColumnDescription)columnDescriptions.get(keyNames[i])).getColumnName());
/*     */     }
/* 227 */     sb.append(") VALUES (");
/*     */     
/* 229 */     for (int i = 0; i < keyNames.length; i++)
/*     */     {
/* 231 */       if (i > 0) {
/* 232 */         sb.append(", ");
/*     */       }
/* 234 */       sb.append('?');
/*     */     }
/* 236 */     sb.append(")");
/* 237 */     String sql = sb.toString();
/* 238 */     PreparedStatement pStmt = this.jdbcConnection.prepareStatement(sql);
/*     */     
/* 240 */     Tracer.traceMethodExit(new Object[] { pStmt });
/* 241 */     return pStmt;
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
/*     */   public int performDelete(String rptJobId, String tableName)
/*     */     throws SQLException
/*     */   {
/* 257 */     Tracer.traceMethodEntry(new Object[] { rptJobId, tableName });
/* 258 */     StringBuilder sb = new StringBuilder();
/* 259 */     sb.append("DELETE FROM ");
/* 260 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/* 261 */       sb.append(this.schemaName).append('.').append(tableName);
/*     */     } else
/* 263 */       sb.append(tableName);
/* 264 */     sb.append(" WHERE RptJobId = '").append(rptJobId.trim()).append("'");
/*     */     
/* 266 */     String sql = sb.toString();
/* 267 */     PreparedStatement pStmt = this.jdbcConnection.prepareStatement(sql);
/* 268 */     int numRows = 0;
/*     */     try
/*     */     {
/* 271 */       numRows = pStmt.executeUpdate();
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 275 */       int sqlErrCode = sqlEx.getErrorCode();
/* 276 */       if (sqlErrCode != 65332)
/*     */       {
/* 278 */         throw sqlEx;
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 283 */       if (pStmt != null)
/*     */       {
/* 285 */         pStmt.close();
/*     */       }
/*     */     }
/*     */     
/* 289 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(numRows) });
/* 290 */     return numRows;
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
/* 302 */     Tracer.traceMethodEntry(new Object[] { tableName });
/* 303 */     DatabaseMetaData dbm = this.jdbcConnection.getMetaData();
/*     */     
/* 305 */     ResultSet tables = dbm.getTables(null, this.schemaName, tableName, null);
/* 306 */     if (tables.next())
/*     */     {
/* 308 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 312 */     tables = dbm.getTables(null, this.schemaName, tableName.toUpperCase(), null);
/* 313 */     boolean result = false;
/* 314 */     if (tables.next())
/*     */     {
/* 316 */       result = true;
/*     */     }
/*     */     
/* 319 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 320 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\impl\DB2_Services.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */