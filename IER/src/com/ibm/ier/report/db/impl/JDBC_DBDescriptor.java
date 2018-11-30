/*    */ package com.ibm.ier.report.db.impl;
/*    */ 
/*    */ import com.ibm.ier.report.db.DBDescriptor;
/*    */ import com.ibm.ier.report.db.DBType;
/*    */ import com.ibm.ier.report.db.DB_Services;
/*    */ import com.ibm.ier.report.util.RptTracer;
/*    */ import com.ibm.ier.report.util.RptTracer.SubSystem;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Map;
/*    */ import javax.naming.NamingException;
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
/*    */ public class JDBC_DBDescriptor
/*    */   implements DBDescriptor
/*    */ {
/* 24 */   private static final RptTracer Tracer = RptTracer.getRptTracer(RptTracer.SubSystem.RptEng);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected DBType dbType;
/*    */   
/*    */ 
/*    */ 
/*    */   protected Map<String, String> connectionContext;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JDBC_DBDescriptor(DBType dbType, Map<String, String> connectionContext)
/*    */   {
/* 40 */     Tracer.traceMethodEntry(new Object[] { connectionContext });
/* 41 */     this.dbType = dbType;
/* 42 */     this.connectionContext = connectionContext;
/* 43 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public DBType getDatabaseType()
/*    */   {
/* 51 */     return this.dbType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Map<String, String> getConnectionContext()
/*    */   {
/* 59 */     return this.connectionContext;
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
/* 71 */     Tracer.traceMethodEntry(new Object[0]);
/* 72 */     DB_Services dbServices = null;
/* 73 */     if (this.dbType == DBType.DB2)
/*    */     {
/* 75 */       dbServices = new DB2_Services();
/*    */     }
/* 77 */     else if (this.dbType == DBType.Oracle)
/*    */     {
/* 79 */       dbServices = new Oracle_Services();
/*    */     }
/* 81 */     else if (this.dbType == DBType.MSSql)
/*    */     {
/* 83 */       dbServices = new Sql_Services();
/*    */     }
/*    */     
/* 86 */     dbServices.open(this.connectionContext);
/* 87 */     dbServices.close();
/*    */     
/* 89 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\impl\JDBC_DBDescriptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */