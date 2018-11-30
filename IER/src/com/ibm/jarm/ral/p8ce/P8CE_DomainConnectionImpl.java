/*    */ package com.ibm.jarm.ral.p8ce;
/*    */ 
/*    */ import com.filenet.api.core.Connection;
/*    */ import com.filenet.api.core.Factory.Connection;
/*    */ import com.ibm.jarm.api.constants.DomainType;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ import com.ibm.jarm.ral.common.RALBaseDomainConnection;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8CE_DomainConnectionImpl
/*    */   extends RALBaseDomainConnection
/*    */ {
/* 17 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*    */   
/* 19 */   private Connection jaceConnection = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   P8CE_DomainConnectionImpl(String url, Map<String, Object> connectionInfo)
/*    */   {
/* 29 */     super(url, connectionInfo);
/* 30 */     Tracer.traceMethodEntry(new Object[] { url, connectionInfo });
/*    */     
/* 32 */     long startTime = System.currentTimeMillis();
/* 33 */     this.jaceConnection = Factory.Connection.getConnection(url);
/* 34 */     Tracer.traceExtCall("Factory.Connection.getConnection()", startTime, System.currentTimeMillis(), Integer.valueOf(1), this.jaceConnection, new Object[] { url });
/*    */     
/* 36 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */   public P8CE_DomainConnectionImpl(Connection jaceConnection)
/*    */   {
/* 41 */     super(jaceConnection.getURI(), null);
/* 42 */     Tracer.traceMethodEntry(new Object[] { jaceConnection });
/* 43 */     this.jaceConnection = jaceConnection;
/* 44 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public DomainType getDomainType()
/*    */   {
/* 52 */     return DomainType.P8_CE;
/*    */   }
/*    */   
/*    */   public Connection getJaceConnection()
/*    */   {
/* 57 */     return this.jaceConnection;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 66 */     return "P8CE_DomainConnectionImpl url: '" + getUrl() + "'";
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_DomainConnectionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */