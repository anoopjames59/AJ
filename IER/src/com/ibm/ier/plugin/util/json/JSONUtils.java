/*     */ package com.ibm.ier.plugin.util.json;
/*     */ 
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONUtils
/*     */ {
/*     */   private static final String FIXED_INDENT = "  ";
/*     */   
/*     */   public static Map arrayToMap(String[][] properties)
/*     */   {
/*  33 */     Map propertyMap = new HashMap();
/*     */     
/*     */ 
/*  36 */     if (properties != null) {
/*  37 */       for (int i = 0; i < properties.length; i++) {
/*  38 */         if (properties[i].length >= 2) {
/*  39 */           if (propertyMap == null) {
/*  40 */             propertyMap = new HashMap();
/*     */           }
/*     */           
/*  43 */           String key = properties[i][0];
/*  44 */           String value = properties[i][1];
/*  45 */           propertyMap.put(key, value);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  50 */     return propertyMap;
/*     */   }
/*     */   
/*     */   private static String getIndent(int depth) {
/*  54 */     String indent = "";
/*     */     
/*  56 */     if (depth > 0) {
/*  57 */       StringBuffer calculatedIndent = new StringBuffer();
/*  58 */       for (int i = 0; i < depth; i++) {
/*  59 */         calculatedIndent.append("  ");
/*     */       }
/*  61 */       indent = calculatedIndent.toString();
/*     */     }
/*     */     
/*  64 */     return indent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String prettyPrintJson(JSONObject jsonObject)
/*     */     throws IOException
/*     */   {
/*  76 */     return prettyPrintJsonObject(jsonObject, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String prettyPrintJsonArray(JSONArray jsonArray, int depth)
/*     */     throws IOException
/*     */   {
/*  88 */     StringBuffer buffer = new StringBuffer();
/*  89 */     String indent = getIndent(depth);
/*     */     
/*     */ 
/*  92 */     if (jsonArray.size() == 0) {
/*  93 */       buffer.append("[]");
/*     */     }
/*     */     else
/*     */     {
/*  97 */       buffer.append("[").append("\n");
/*     */       
/*  99 */       for (int i = 0; i < jsonArray.size(); i++) {
/* 100 */         Object object = jsonArray.get(i);
/*     */         
/* 102 */         if ((object instanceof JSONObject)) {
/* 103 */           buffer.append(indent).append(prettyPrintJsonObject((JSONObject)object, depth + 1));
/* 104 */         } else if ((object instanceof JSONArray)) {
/* 105 */           buffer.append(indent).append(prettyPrintJsonArray((JSONArray)object, depth + 1));
/*     */         }
/*     */         
/* 108 */         buffer.append(",\n");
/*     */       }
/*     */       
/* 111 */       removeTrailingCommaAndNewline(buffer);
/*     */       
/* 113 */       buffer.append("\n").append(indent).append("]");
/*     */     }
/*     */     
/* 116 */     return buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String prettyPrintJsonObject(JSONObject jsonObject, int depth)
/*     */     throws IOException
/*     */   {
/* 129 */     StringBuffer buffer = new StringBuffer();
/* 130 */     String indent = getIndent(depth);
/*     */     
/* 132 */     if (depth == 0) {
/* 133 */       buffer.append("{").append("\n");
/*     */     } else {
/* 135 */       buffer.append("  ").append("{").append("\n");
/*     */     }
/*     */     
/*     */ 
/* 139 */     for (Iterator keyIterator = jsonObject.keySet().iterator(); keyIterator.hasNext();) {
/* 140 */       String key = (String)keyIterator.next();
/* 141 */       Object value = jsonObject.get(key);
/*     */       
/* 143 */       if (value != null)
/*     */       {
/*     */ 
/*     */ 
/* 147 */         buffer.append(indent).append("  ").append("'").append(key).append("':");
/*     */         
/*     */ 
/* 150 */         if ((value instanceof String)) {
/* 151 */           buffer.append("'").append(value).append("'");
/* 152 */         } else if ((value instanceof JSONObject)) {
/* 153 */           buffer.append(prettyPrintJsonObject((JSONObject)value, depth + 1));
/* 154 */         } else if ((value instanceof JSONArray)) {
/* 155 */           buffer.append(prettyPrintJsonArray((JSONArray)value, depth + 1));
/*     */         }
/*     */         
/* 158 */         buffer.append(",\n");
/*     */       }
/*     */     }
/* 161 */     removeTrailingCommaAndNewline(buffer);
/*     */     
/* 163 */     buffer.append("\n").append(indent).append("}");
/*     */     
/* 165 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private static void removeTrailingCommaAndNewline(StringBuffer buffer) {
/* 169 */     int bufferLength = buffer.length();
/*     */     
/*     */ 
/* 172 */     if ((bufferLength > 2) && (buffer.charAt(bufferLength - 1) == '\n') && (buffer.charAt(bufferLength - 2) == ',')) {
/* 173 */       buffer.delete(bufferLength - 2, bufferLength);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, boolean value) {
/* 178 */     jsonObject.put(key, new Boolean(value));
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, double value) {
/* 182 */     jsonObject.put(key, new Double(value));
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, int value) {
/* 186 */     jsonObject.put(key, new Integer(value));
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, long value) {
/* 190 */     jsonObject.put(key, new Long(value));
/*     */   }
/*     */   
/*     */ 
/*     */   public static void accumulate(JSONObject jsonObject, Object key, Object value)
/*     */   {
/*     */     JSONArray jsonArray;
/*     */     
/* 198 */     if (!jsonObject.containsKey(key)) {
/* 199 */       JSONArray jsonArray = new JSONArray();
/* 200 */       jsonObject.put(key, jsonArray);
/*     */     } else {
/* 202 */       jsonArray = (JSONArray)jsonObject.get(key);
/*     */     }
/* 204 */     jsonArray.add(value);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\json\JSONUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */