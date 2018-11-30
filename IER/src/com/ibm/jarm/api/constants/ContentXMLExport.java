/*    */ package com.ibm.jarm.api.constants;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ContentXMLExport
/*    */ {
/* 28 */   AsSeparateFile(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 34 */   EmbedInDocumentXML(2);
/*    */   
/*    */   static {
/* 37 */     ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 40 */     for (ContentXMLExport option : values())
/*    */     {
/* 42 */       ByIntValueMap.put(Integer.valueOf(option.intValue), option);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private ContentXMLExport(int intValue)
/*    */   {
/* 50 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 60 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Map<Integer, ContentXMLExport> ByIntValueMap;
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */   public static ContentXMLExport getInstanceFromInt(int intValue)
/*    */   {
/* 74 */     return (ContentXMLExport)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\ContentXMLExport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */