/*     */ package com.ibm.ier.report.db.impl;
/*     */ 
/*     */ import com.ibm.ier.report.db.ColumnDescription;
/*     */ import com.ibm.ier.report.db.DB_Services;
/*     */ import com.ibm.ier.report.util.RptTracer;
/*     */ import com.ibm.ier.report.util.RptTracer.SubSystem;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DB_ServicesImpl
/*     */   implements DB_Services
/*     */ {
/*     */   protected Connection jdbcConnection;
/*     */   protected String schemaName;
/*  42 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final char SEPARATOR = ',';
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String W3C_DATE_FORMAT_ZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSchemaName(String schema)
/*     */   {
/*  66 */     this.schemaName = schema;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void open(String jndiResource)
/*     */     throws ClassNotFoundException, SQLException, NamingException
/*     */   {
/*  77 */     Tracer.traceMethodEntry(new Object[] { jndiResource });
/*  78 */     InitialContext initialContext = new InitialContext();
/*  79 */     DataSource dataSource = (DataSource)initialContext.lookup(jndiResource);
/*  80 */     this.jdbcConnection = dataSource.getConnection();
/*  81 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws SQLException
/*     */   {
/*  90 */     Tracer.traceMethodEntry(new Object[0]);
/*  91 */     if (this.jdbcConnection != null)
/*     */     {
/*  93 */       this.jdbcConnection.close();
/*  94 */       this.jdbcConnection = null;
/*     */     }
/*  96 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deleteTable(String tableName)
/*     */     throws SQLException
/*     */   {
/* 107 */     Tracer.traceMethodEntry(new Object[] { tableName });
/* 108 */     StringBuilder sb = new StringBuilder();
/* 109 */     sb.append("DROP TABLE ?");
/* 110 */     String sql = sb.toString();
/* 111 */     PreparedStatement pStmt = this.jdbcConnection.prepareStatement(sql);
/*     */     
/* 113 */     StringBuilder tableSQL = new StringBuilder();
/* 114 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/* 115 */       tableSQL.append(this.schemaName).append('.').append(tableName);
/*     */     } else {
/* 117 */       tableSQL.append(tableName);
/*     */     }
/* 119 */     pStmt.setString(1, tableSQL.toString());
/*     */     
/*     */     try
/*     */     {
/* 123 */       pStmt.executeUpdate();
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 127 */       int sqlErrCode = sqlEx.getErrorCode();
/* 128 */       if (sqlErrCode != 65332)
/*     */       {
/* 130 */         throw sqlEx;
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 135 */       if (pStmt != null)
/*     */       {
/* 137 */         pStmt.close();
/*     */       }
/*     */     }
/* 140 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public LinkedHashMap<String, ColumnDescription> getColumnDescription(LinkedHashSet<RMProperty> rowProps)
/*     */     throws SQLException
/*     */   {
/* 156 */     Tracer.traceMethodEntry(new Object[] { rowProps });
/* 157 */     LinkedHashMap<String, ColumnDescription> columnDescriptions = new LinkedHashMap();
/* 158 */     Set<String> columnNamesAlreadyInUse = new HashSet();
/*     */     
/* 160 */     RMProperty rowProp = null;
/* 161 */     String propName = null;
/* 162 */     String columnName = null;
/* 163 */     int columnCount = 0;
/* 164 */     DB_ColumnDescription columnDesc = null;
/* 165 */     for (Iterator it = rowProps.iterator(); it.hasNext();)
/*     */     {
/* 167 */       rowProp = (RMProperty)it.next();
/*     */       
/* 169 */       propName = rowProp.getSymbolicName();
/* 170 */       String trimmedPropName = propName.replaceAll(" ", "");
/* 171 */       columnName = trimmedPropName;
/* 172 */       int i = 0;
/* 173 */       while (columnNamesAlreadyInUse.contains(columnName))
/*     */       {
/* 175 */         i++;
/* 176 */         columnName = trimmedPropName + "_" + i;
/*     */       }
/* 178 */       columnNamesAlreadyInUse.add(columnName);
/*     */       
/* 180 */       columnDesc = new DB_ColumnDescription(columnCount + 1, columnName);
/*     */       
/* 182 */       DataType dataType = rowProp.getDataType();
/* 183 */       RMCardinality cardinality = rowProp.getCardinality();
/* 184 */       if ((cardinality == RMCardinality.Single) || (cardinality == RMCardinality.List))
/*     */       {
/* 186 */         switch (dataType)
/*     */         {
/*     */         case Boolean: 
/* 189 */           columnDesc.setSQLType(16);
/* 190 */           break;
/*     */         case DateTime: 
/* 192 */           columnDesc.setSQLType(91);
/* 193 */           break;
/*     */         case Double: 
/* 195 */           columnDesc.setSQLType(8);
/* 196 */           break;
/*     */         case Guid: 
/* 198 */           columnDesc.setSQLType(12);
/* 199 */           break;
/*     */         case Integer: 
/* 201 */           columnDesc.setSQLType(4);
/* 202 */           break;
/*     */         case Object: 
/* 204 */           columnDesc.setSQLType(1111);
/* 205 */           break;
/*     */         case String: 
/* 207 */           columnDesc.setSQLType(12);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 212 */       if (columnName != null)
/*     */       {
/* 214 */         columnDescriptions.put(propName, columnDesc);
/* 215 */         columnCount++;
/*     */       }
/*     */     }
/*     */     
/* 219 */     Tracer.traceMethodExit(new Object[] { columnDescriptions });
/* 220 */     return columnDescriptions;
/*     */   }
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
/* 233 */     Tracer.traceMethodEntry(new Object[] { tableName, columnDescriptions });
/* 234 */     Set<String> keySet = columnDescriptions.keySet();
/* 235 */     String[] keyNames = (String[])keySet.toArray(new String[keySet.size()]);
/*     */     
/* 237 */     StringBuilder sb = new StringBuilder();
/* 238 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/* 239 */       sb.append("INSERT INTO ").append(this.schemaName).append('.').append(tableName).append(" (");
/*     */     } else {
/* 241 */       sb.append("INSERT INTO ").append(tableName).append(" (");
/*     */     }
/* 243 */     for (int i = 0; i < keyNames.length; i++)
/*     */     {
/* 245 */       if (i > 0) {
/* 246 */         sb.append(", ");
/*     */       }
/* 248 */       sb.append('"').append(((ColumnDescription)columnDescriptions.get(keyNames[i])).getColumnName().toUpperCase()).append("\" ");
/*     */     }
/* 250 */     sb.append(") VALUES (");
/*     */     
/* 252 */     for (int i = 0; i < keyNames.length; i++)
/*     */     {
/* 254 */       if (i > 0) {
/* 255 */         sb.append(", ");
/*     */       }
/* 257 */       sb.append('?');
/*     */     }
/* 259 */     sb.append(")");
/* 260 */     String sql = sb.toString();
/* 261 */     PreparedStatement pStmt = this.jdbcConnection.prepareStatement(sql);
/*     */     
/* 263 */     Tracer.traceMethodExit(new Object[] { pStmt });
/* 264 */     return pStmt;
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
/*     */   public int performInsert(PreparedStatement pStmt, LinkedHashSet<RMProperty> rowProps, LinkedHashMap<String, ColumnDescription> columnDescriptions)
/*     */     throws SQLException
/*     */   {
/* 280 */     Tracer.traceMethodEntry(new Object[] { pStmt, rowProps, columnDescriptions });
/* 281 */     pStmt.clearParameters();
/*     */     
/* 283 */     RMProperty rowProp = null;
/* 284 */     Object rowPropValue = null;
/* 285 */     String propName = null;
/* 286 */     ColumnDescription colDesc = null;
/* 287 */     for (Iterator it = rowProps.iterator(); it.hasNext();)
/*     */     {
/* 289 */       rowProp = (RMProperty)it.next();
/* 290 */       if (rowProp.getDataType() == DataType.Object) {
/* 291 */         rowPropValue = rowProp.getGuidValue();
/*     */       } else
/* 293 */         rowPropValue = rowProp.getObjectValue();
/* 294 */       propName = rowProp.getSymbolicName();
/* 295 */       colDesc = (ColumnDescription)columnDescriptions.get(propName);
/* 296 */       if (colDesc == null)
/*     */       {
/* 298 */         System.err.println("**** No ColumnDescription for P8CE rowset property: " + propName);
/*     */ 
/*     */ 
/*     */       }
/* 302 */       else if (rowPropValue == null)
/*     */       {
/* 304 */         if (((this instanceof Oracle_Services)) && (colDesc.getSQLType() == 16))
/*     */         {
/* 306 */           pStmt.setBoolean(colDesc.getColumnIndex(), false);
/*     */         }
/*     */         else
/*     */         {
/* 310 */           pStmt.setNull(colDesc.getColumnIndex(), colDesc.getSQLType());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 315 */         String strVal = null;
/* 316 */         switch (colDesc.getSQLType())
/*     */         {
/*     */         case 12: 
/* 319 */           if ((rowPropValue instanceof List))
/*     */           {
/* 321 */             List<String> strList = (List)rowPropValue;
/* 322 */             for (int i = 0; i < strList.size(); i++)
/*     */             {
/* 324 */               String aStr = (String)strList.get(i);
/* 325 */               if (i == 0) {
/* 326 */                 strVal = '"' + aStr + '"';
/*     */               } else {
/* 328 */                 strVal = strVal + ',' + '"' + aStr + '"';
/*     */               }
/*     */             }
/*     */           } else {
/* 332 */             strVal = rowPropValue.toString(); }
/* 333 */           pStmt.setString(colDesc.getColumnIndex(), strVal);
/* 334 */           break;
/*     */         
/*     */ 
/*     */         case 91: 
/* 338 */           if ((rowPropValue instanceof List))
/*     */           {
/* 340 */             List<Date> dateList = (List)rowPropValue;
/* 341 */             for (int i = 0; i < dateList.size(); i++)
/*     */             {
/* 343 */               Date javaDate = (Date)dateList.get(i);
/* 344 */               SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
/* 345 */               dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 346 */               String strDate = dateFormatter.format(javaDate);
/* 347 */               if (i == 0) {
/* 348 */                 strVal = '"' + strDate + '"';
/*     */               } else
/* 350 */                 strVal = strVal + ',' + '"' + strDate + '"';
/*     */             }
/* 352 */             pStmt.setString(colDesc.getColumnIndex(), strVal);
/*     */           }
/*     */           else
/*     */           {
/* 356 */             Date javaDate = (Date)rowPropValue;
/* 357 */             Timestamp sqlDate = new Timestamp(javaDate.getTime());
/* 358 */             pStmt.setTimestamp(colDesc.getColumnIndex(), sqlDate, Calendar.getInstance(TimeZone.getTimeZone("GMT")));
/*     */           }
/* 360 */           break;
/*     */         
/*     */ 
/*     */         case 4: 
/* 364 */           if ((rowPropValue instanceof List))
/*     */           {
/* 366 */             List<Integer> intList = (List)rowPropValue;
/* 367 */             strVal = null;
/* 368 */             for (int i = 0; i < intList.size(); i++)
/*     */             {
/* 370 */               Integer intVal = (Integer)intList.get(i);
/* 371 */               if (i == 0) {
/* 372 */                 strVal = intVal.toString();
/*     */               } else
/* 374 */                 strVal = strVal + ',' + intVal.toString();
/*     */             }
/* 376 */             pStmt.setString(colDesc.getColumnIndex(), strVal);
/*     */           }
/*     */           else
/*     */           {
/* 380 */             Integer intVal = Integer.valueOf(((Integer)rowPropValue).intValue());
/* 381 */             pStmt.setInt(colDesc.getColumnIndex(), intVal.intValue());
/*     */           }
/* 383 */           break;
/*     */         
/*     */ 
/*     */         case 16: 
/* 387 */           if ((rowPropValue instanceof List))
/*     */           {
/* 389 */             List<Boolean> boolList = (List)rowPropValue;
/* 390 */             for (int i = 0; i < boolList.size(); i++)
/*     */             {
/* 392 */               Boolean boolVal = (Boolean)boolList.get(i);
/* 393 */               if (i == 0) {
/* 394 */                 strVal = boolVal.toString();
/*     */               } else
/* 396 */                 strVal = strVal + ',' + boolVal.toString();
/*     */             }
/* 398 */             pStmt.setString(colDesc.getColumnIndex(), strVal);
/*     */           }
/*     */           else
/*     */           {
/* 402 */             Boolean boolVal = Boolean.valueOf(((Boolean)rowPropValue).booleanValue());
/* 403 */             pStmt.setBoolean(colDesc.getColumnIndex(), boolVal.booleanValue());
/*     */           }
/* 405 */           break;
/*     */         
/*     */ 
/*     */         case 8: 
/* 409 */           if ((rowPropValue instanceof List))
/*     */           {
/* 411 */             List<Double> dblList = (List)rowPropValue;
/* 412 */             for (int i = 0; i < dblList.size(); i++)
/*     */             {
/* 414 */               Double dblVal = (Double)dblList.get(i);
/* 415 */               if (i == 0) {
/* 416 */                 strVal = dblVal.toString();
/*     */               } else
/* 418 */                 strVal = strVal + ',' + dblVal.toString();
/*     */             }
/* 420 */             pStmt.setString(colDesc.getColumnIndex(), strVal);
/*     */           }
/*     */           else
/*     */           {
/* 424 */             Double dblVal = Double.valueOf(((Double)rowPropValue).doubleValue());
/* 425 */             pStmt.setDouble(colDesc.getColumnIndex(), dblVal.doubleValue());
/*     */           }
/* 427 */           break;
/*     */         
/*     */         case 1111: 
/* 430 */           strVal = rowPropValue.toString();
/* 431 */           pStmt.setString(colDesc.getColumnIndex(), strVal);
/* 432 */           break;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 441 */     int numRows = pStmt.executeUpdate();
/* 442 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(numRows) });
/* 443 */     return numRows;
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
/*     */   public int performDelete(String rptJobId, String tableName)
/*     */     throws SQLException
/*     */   {
/* 458 */     Tracer.traceMethodEntry(new Object[] { rptJobId, tableName });
/* 459 */     StringBuilder sb = new StringBuilder();
/* 460 */     sb.append("DELETE FROM ");
/* 461 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/* 462 */       sb.append(this.schemaName).append('.').append(tableName);
/*     */     } else
/* 464 */       sb.append(tableName);
/* 465 */     sb.append(" WHERE RptJobId = '").append(rptJobId.trim()).append("'");
/*     */     
/* 467 */     String sql = sb.toString();
/* 468 */     PreparedStatement pStmt = this.jdbcConnection.prepareStatement(sql);
/* 469 */     int numRows = 0;
/*     */     try
/*     */     {
/* 472 */       numRows = pStmt.executeUpdate();
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 476 */       int sqlErrCode = sqlEx.getErrorCode();
/* 477 */       if (sqlErrCode != 65332)
/*     */       {
/* 479 */         throw sqlEx;
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 484 */       if (pStmt != null)
/*     */       {
/* 486 */         pStmt.close();
/*     */       }
/*     */     }
/*     */     
/* 490 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(numRows) });
/* 491 */     return numRows;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close(PreparedStatement pStmt)
/*     */     throws SQLException
/*     */   {
/* 501 */     pStmt.close();
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
/*     */   public boolean isDataExist(String columnValue, String columnName, String tableName)
/*     */     throws SQLException
/*     */   {
/* 516 */     Tracer.traceMethodEntry(new Object[] { columnValue, columnName, tableName });
/* 517 */     StringBuilder sb = new StringBuilder();
/* 518 */     sb.append("SELECT * FROM ");
/* 519 */     if ((this.schemaName != null) && (this.schemaName.trim().length() > 0)) {
/* 520 */       sb.append(this.schemaName).append('.').append(tableName);
/*     */     } else
/* 522 */       sb.append(tableName);
/* 523 */     sb.append(" WHERE ").append(columnName);
/* 524 */     sb.append(" = '").append(columnValue.trim()).append("'");
/*     */     
/* 526 */     String sql = sb.toString();
/* 527 */     PreparedStatement pStmt = this.jdbcConnection.prepareStatement(sql);
/* 528 */     boolean result = false;
/*     */     try
/*     */     {
/* 531 */       ResultSet rs = pStmt.executeQuery();
/* 532 */       if (rs.next()) {
/* 533 */         result = true;
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 537 */       int sqlErrCode = sqlEx.getErrorCode();
/* 538 */       if (sqlErrCode != 65332)
/*     */       {
/* 540 */         throw sqlEx;
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 545 */       if (pStmt != null)
/*     */       {
/* 547 */         pStmt.close();
/*     */       }
/*     */     }
/*     */     
/* 551 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 552 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\impl\DB_ServicesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */