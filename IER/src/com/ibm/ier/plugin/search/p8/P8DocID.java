/*    */ package com.ibm.ier.plugin.search.p8;
/*    */ 
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ public class P8DocID
/*    */ {
/*    */   private String classID;
/*    */   private String objectStoreID;
/*    */   private String objectID;
/*    */   
/*    */   private P8DocID() {}
/*    */   
/*    */   public P8DocID(String docId) {
/* 14 */     StringTokenizer docIdTok = new StringTokenizer(docId, ",");
/*    */     
/* 16 */     this.classID = (docIdTok.hasMoreTokens() ? docIdTok.nextToken() : null);
/* 17 */     this.objectStoreID = (docIdTok.hasMoreTokens() ? docIdTok.nextToken() : null);
/* 18 */     this.objectID = (docIdTok.hasMoreTokens() ? docIdTok.nextToken() : null);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 22 */     StringBuffer sBuf = new StringBuffer();
/*    */     
/* 24 */     sBuf.append(this.classID).append(",");
/* 25 */     sBuf.append(this.objectStoreID).append(",");
/* 26 */     sBuf.append(this.objectID);
/*    */     
/* 28 */     return sBuf.toString();
/*    */   }
/*    */   
/*    */   protected Object clone() throws CloneNotSupportedException
/*    */   {
/* 33 */     P8DocID cloned = new P8DocID();
/*    */     
/* 35 */     cloned.classID = this.classID;
/* 36 */     cloned.objectStoreID = this.objectStoreID;
/* 37 */     cloned.objectID = this.objectID;
/*    */     
/* 39 */     return cloned;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 44 */     boolean returnValue = false;
/*    */     
/* 46 */     if ((o instanceof P8DocID)) {
/* 47 */       P8DocID to = (P8DocID)o;
/* 48 */       returnValue = (this.classID.equals(to.classID)) && (this.objectStoreID.equalsIgnoreCase(to.objectStoreID)) && (this.objectID.equalsIgnoreCase(this.objectID));
/*    */     }
/*    */     
/* 51 */     return returnValue;
/*    */   }
/*    */   
/*    */   public String getObjectID() {
/* 55 */     return this.objectID;
/*    */   }
/*    */   
/*    */   public void setObjectID(String objectID) {
/* 59 */     this.objectID = objectID;
/*    */   }
/*    */   
/*    */   public String getClassID() {
/* 63 */     return this.classID;
/*    */   }
/*    */   
/*    */   public String getObjectStoreID() {
/* 67 */     return this.objectStoreID;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8DocID.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */