/*    */ package com.ibm.jarm.ral.common;
/*    */ 
/*    */ import com.ibm.jarm.api.core.DomainConnection;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RALBaseDomainConnection
/*    */   implements DomainConnection
/*    */ {
/* 17 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*    */   
/*    */   protected String url;
/*    */   protected Map<String, Object> connectionInfo;
/*    */   
/*    */   protected RALBaseDomainConnection(String url, Map<String, Object> connectionInfo)
/*    */   {
/* 24 */     Tracer.traceMethodEntry(new Object[] { url, connectionInfo });
/* 25 */     this.url = url;
/* 26 */     this.connectionInfo = connectionInfo;
/* 27 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getUrl()
/*    */   {
/* 35 */     return this.url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Map<String, Object> getConnectionInfo()
/*    */   {
/* 43 */     return this.connectionInfo;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBaseDomainConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */