/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
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
/*     */   private static String prettyPrintJsonObject(JSONObject jsonObject, int depth)
/*     */     throws IOException
/*     */   {
/* 128 */     StringBuffer buffer = new StringBuffer();
/* 129 */     String indent = getIndent(depth);
/*     */     
/* 131 */     if (depth == 0) {
/* 132 */       buffer.append("{").append("\n");
/*     */     } else {
/* 134 */       buffer.append("  ").append("{").append("\n");
/*     */     }
/*     */     
/*     */ 
/* 138 */     for (Iterator keyIterator = jsonObject.keySet().iterator(); keyIterator.hasNext();) {
/* 139 */       String key = (String)keyIterator.next();
/* 140 */       Object value = jsonObject.get(key);
/*     */       
/* 142 */       if (value != null)
/*     */       {
/*     */ 
/*     */ 
/* 146 */         buffer.append(indent).append("  ").append("'").append(key).append("':");
/*     */         
/*     */ 
/* 149 */         if ((value instanceof String)) {
/* 150 */           buffer.append("'").append(value).append("'");
/* 151 */         } else if ((value instanceof JSONObject)) {
/* 152 */           buffer.append(prettyPrintJsonObject((JSONObject)value, depth + 1));
/* 153 */         } else if ((value instanceof JSONArray)) {
/* 154 */           buffer.append(prettyPrintJsonArray((JSONArray)value, depth + 1));
/*     */         }
/*     */         
/* 157 */         buffer.append(",\n");
/*     */       }
/*     */     }
/* 160 */     removeTrailingCommaAndNewline(buffer);
/*     */     
/* 162 */     buffer.append("\n").append(indent).append("}");
/*     */     
/* 164 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private static void removeTrailingCommaAndNewline(StringBuffer buffer) {
/* 168 */     int bufferLength = buffer.length();
/*     */     
/*     */ 
/* 171 */     if ((bufferLength > 2) && (buffer.charAt(bufferLength - 1) == '\n') && (buffer.charAt(bufferLength - 2) == ',')) {
/* 172 */       buffer.delete(bufferLength - 2, bufferLength);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, boolean value) {
/* 177 */     jsonObject.put(key, new Boolean(value));
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, double value) {
/* 181 */     jsonObject.put(key, new Double(value));
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, int value) {
/* 185 */     jsonObject.put(key, new Integer(value));
/*     */   }
/*     */   
/*     */   public static void put(JSONObject jsonObject, String key, long value) {
/* 189 */     jsonObject.put(key, new Long(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void put(JSONObject jsonObject, String key, BigDecimal value)
/*     */   {
/* 196 */     jsonObject.put(key, value.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   public static void accumulate(JSONObject jsonObject, Object key, Object value)
/*     */   {
/*     */     JSONArray jsonArray;
/*     */     
/* 204 */     if (!jsonObject.containsKey(key)) {
/* 205 */       JSONArray jsonArray = new JSONArray();
/* 206 */       jsonObject.put(key, jsonArray);
/*     */     } else {
/* 208 */       jsonArray = (JSONArray)jsonObject.get(key);
/*     */     }
/* 210 */     jsonArray.add(value);
/*     */   }
/*     */   
/*     */   public static JSONArray toJSONArray(String[] values) {
/* 214 */     JSONArray jsonArray = new JSONArray();
/* 215 */     if (values != null) {
/* 216 */       for (int i = 0; i < values.length; i++) {
/* 217 */         jsonArray.add(values[i]);
/*     */       }
/*     */     }
/* 220 */     return jsonArray;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\JSONUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */