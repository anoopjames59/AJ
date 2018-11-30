/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.report.db.DBDescriptor;
/*    */ import com.ibm.ier.report.db.impl.JNDI_DBDescriptor;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NameNotFoundException;
/*    */ import javax.naming.NamingException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.sql.DataSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JDBCUtil
/*    */ {
/*    */   private static final String SCHEMA = "IER_REPORT";
/*    */   private static final String JDBC = "jdbc/";
/*    */   
/*    */   public static Connection connectToDatabase(String dataSourceName, HttpServletRequest request)
/*    */     throws ClassNotFoundException, SQLException, NamingException
/*    */   {
/* 33 */     String method = "connectToDatabase";
/* 34 */     Logger.logEntry(JDBCUtil.class, method, request);
/*    */     
/* 36 */     if (!dataSourceName.startsWith("jdbc/")) {
/* 37 */       dataSourceName = "jdbc/" + dataSourceName;
/*    */     }
/*    */     
/* 40 */     DataSource dataSource = null;
/*    */     try {
/* 42 */       Logger.logInfo(JDBCUtil.class, method, request, "Connecting to database: " + dataSourceName);
/*    */       
/* 44 */       InitialContext initialContext = new InitialContext();
/* 45 */       dataSource = (DataSource)initialContext.lookup(dataSourceName);
/*    */     } catch (NameNotFoundException e) {
/* 47 */       Logger.logInfo(JDBCUtil.class, method, request, "connection failed: " + e.getMessage());
/*    */       
/* 49 */       dataSourceName = "java:comp/env/" + dataSourceName;
/* 50 */       Logger.logInfo(JDBCUtil.class, "connectToDatabase", request, "Initial lookup failed.  Attemping again.: " + dataSourceName);
/*    */       
/* 52 */       InitialContext initialContext = new InitialContext();
/* 53 */       dataSource = (DataSource)initialContext.lookup(dataSourceName);
/*    */     }
/*    */     
/* 56 */     Logger.logExit(JDBCUtil.class, method, request);
/* 57 */     return dataSource.getConnection();
/*    */   }
/*    */   
/*    */   public static DBDescriptor connectReportDataSource(String dataSource, HttpServletRequest request) throws Exception
/*    */   {
/* 62 */     String method = "connectReportDataSource";
/* 63 */     Logger.logEntry(JDBCUtil.class, method, request);
/*    */     
/* 65 */     if (!dataSource.startsWith("jdbc/")) {
/* 66 */       dataSource = "jdbc/" + dataSource;
/*    */     }
/*    */     
/* 69 */     DBDescriptor dbDescriptor = null;
/*    */     try
/*    */     {
/*    */       try {
/* 73 */         dbDescriptor = new JNDI_DBDescriptor(dataSource, "IER_REPORT");
/* 74 */         dbDescriptor.verify();
/*    */       }
/*    */       catch (NameNotFoundException e) {
/* 77 */         Logger.logInfo(JDBCUtil.class, method, request, "connection failed: " + e.getMessage());
/*    */         
/* 79 */         dataSource = "java:comp/env/" + dataSource;
/* 80 */         Logger.logInfo(JDBCUtil.class, "connectToDatabase", request, "Initial lookup failed.  Attemping again.: " + dataSource);
/*    */         
/* 82 */         dbDescriptor = new JNDI_DBDescriptor(dataSource, "IER_REPORT");
/* 83 */         dbDescriptor.verify();
/*    */       }
/*    */       
/* 86 */       Logger.logInfo(JDBCUtil.class, method, request, "Verified Connect to datasource " + dataSource + " succeeded.");
/*    */       
/* 88 */       Logger.logExit(JDBCUtil.class, method, request);
/* 89 */       return dbDescriptor;
/*    */     }
/*    */     catch (Exception exc)
/*    */     {
/* 93 */       Logger.logError(JDBCUtil.class, method, request, "Unable to connect to datasource", exc);
/* 94 */       throw exc;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\JDBCUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */