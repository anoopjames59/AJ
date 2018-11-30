/*    */ package com.ibm.ier.plugin.util.json.changesummary;
/*    */ 
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.io.IOException;
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
/*    */ public class Value
/*    */ {
/*    */   private String id;
/*    */   private String name;
/*    */   private String value;
/*    */   
/*    */   public void fromJSONString(String jsonString)
/*    */     throws IOException
/*    */   {
/* 31 */     JSONObject jsonObject = JSONObject.parse(jsonString);
/*    */     
/* 33 */     this.id = jsonObject.get("id").toString();
/* 34 */     this.name = jsonObject.get("name").toString();
/* 35 */     this.value = jsonObject.get("value").toString();
/*    */   }
/*    */   
/*    */   public String getId() {
/* 39 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 47 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 51 */     StringBuffer buffer = new StringBuffer("Value: {");
/*    */     
/* 53 */     buffer.append("id=\"");
/* 54 */     buffer.append(getId());
/* 55 */     buffer.append("\", ");
/*    */     
/* 57 */     buffer.append("name=\"");
/* 58 */     buffer.append(getName());
/* 59 */     buffer.append("\", ");
/*    */     
/* 61 */     buffer.append("value=\"");
/* 62 */     buffer.append(getValue());
/* 63 */     buffer.append("\"");
/*    */     
/* 65 */     buffer.append("}");
/*    */     
/* 67 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\json\changesummary\Value.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */