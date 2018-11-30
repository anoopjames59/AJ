/*    */ package com.ibm.ier.ddcp.entity;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class LinkEntity
/*    */   implements Serializable
/*    */ {
/* 18 */   private String linkId = null;
/* 19 */   private String linkName = null;
/* 20 */   private String headId = null;
/* 21 */   private String TailId = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getLinkId()
/*    */   {
/* 28 */     return this.linkId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setLinkId(String linkId)
/*    */   {
/* 36 */     this.linkId = linkId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getLinkName()
/*    */   {
/* 44 */     return this.linkName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setLinkName(String linkName)
/*    */   {
/* 52 */     this.linkName = linkName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getHeadId()
/*    */   {
/* 60 */     return this.headId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setHeadId(String headId)
/*    */   {
/* 68 */     this.headId = headId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getTailId()
/*    */   {
/* 76 */     return this.TailId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setTailId(String tailId)
/*    */   {
/* 84 */     this.TailId = tailId;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\entity\LinkEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */