/*    */ package com.ibm.ier.report.db.impl;
/*    */ 
/*    */ import com.ibm.ier.report.db.DBDescriptor;
/*    */ import com.ibm.ier.report.db.DBType;
/*    */ import com.ibm.ier.report.util.RptTracer;
/*    */ import com.ibm.ier.report.util.RptTracer.SubSystem;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
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
/*    */ public class JNDI_DBDescriptor
/*    */   implements DBDescriptor
/*    */ {
/* 24 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*    */   
/*    */ 
/*    */   protected DBType dbType;
/*    */   
/*    */ 
/*    */   protected String jndiDatasource;
/*    */   
/*    */ 
/*    */   protected String schemaName;
/*    */   
/*    */ 
/*    */   public JNDI_DBDescriptor(String jndiDatasource, String schema)
/*    */   {
/* 38 */     Tracer.traceMethodEntry(new Object[] { jndiDatasource });
/* 39 */     this.jndiDatasource = jndiDatasource;
/* 40 */     this.schemaName = schema;
/* 41 */     this.dbType = DBType.JNDIDataSource;
/* 42 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public DBType getDatabaseType()
/*    */   {
/* 50 */     return this.dbType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getJNDIDataSource()
/*    */   {
/* 58 */     return this.jndiDatasource;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSchemaName()
/*    */   {
/* 66 */     return this.schemaName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void verify()
/*    */     throws ClassNotFoundException, SQLException, NamingException
/*    */   {
/* 78 */     Tracer.traceMethodEntry(new Object[0]);
/* 79 */     InitialContext initialContext = new InitialContext();
/* 80 */     DataSource dataSource = (DataSource)initialContext.lookup(this.jndiDatasource);
/* 81 */     Connection jdbcConnection = dataSource.getConnection();
/* 82 */     if (jdbcConnection != null)
/*    */     {
/* 84 */       jdbcConnection.close();
/*    */     }
/* 86 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\impl\JNDI_DBDescriptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */