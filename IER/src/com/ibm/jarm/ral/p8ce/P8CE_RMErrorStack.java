/*    */ package com.ibm.jarm.ral.p8ce;
/*    */ 
/*    */ import com.filenet.api.exception.ErrorRecord;
/*    */ import com.filenet.api.exception.ErrorStack;
/*    */ import com.ibm.jarm.api.exception.RMErrorRecord;
/*    */ import com.ibm.jarm.api.exception.RMErrorStack;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ public class P8CE_RMErrorStack
/*    */   implements RMErrorStack
/*    */ {
/*    */   private ErrorStack jaceErrorStack;
/*    */   private List<RMErrorRecord> rmErrorRecords;
/*    */   
/*    */   public RMErrorRecord[] getErrorRecords()
/*    */   {
/* 30 */     RMErrorRecord[] result = new RMErrorRecord[0];
/* 31 */     if (this.rmErrorRecords != null)
/*    */     {
/* 33 */       result = (RMErrorRecord[])this.rmErrorRecords.toArray(new RMErrorRecord[this.rmErrorRecords.size()]);
/*    */     }
/*    */     
/* 36 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 45 */     if (this.jaceErrorStack != null) {
/* 46 */       return this.jaceErrorStack.toString();
/*    */     }
/* 48 */     return "";
/*    */   }
/*    */   
/*    */   public P8CE_RMErrorStack(ErrorStack jaceErrorStack)
/*    */   {
/* 53 */     this.jaceErrorStack = jaceErrorStack;
/* 54 */     if (jaceErrorStack != null)
/*    */     {
/* 56 */       ErrorRecord[] jaceErrorRecords = jaceErrorStack.getErrorRecords();
/* 57 */       if (jaceErrorRecords != null)
/*    */       {
/* 59 */         this.rmErrorRecords = new ArrayList(jaceErrorRecords.length);
/* 60 */         for (ErrorRecord jaceErrorRecord : jaceErrorRecords)
/*    */         {
/* 62 */           if (jaceErrorRecord != null)
/*    */           {
/* 64 */             this.rmErrorRecords.add(new P8CE_RMErrorRecord(jaceErrorRecord));
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMErrorStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */