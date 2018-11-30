/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ 
/*     */ public class EncoderUtil
/*     */ {
/*     */   public static String encodeURL(String str) throws UnsupportedEncodingException
/*     */   {
/*  10 */     return java.net.URLEncoder.encode(str, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String decodeURL(String str) throws UnsupportedEncodingException {
/*  14 */     return URLDecoder.decode(str, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String escapeString(String str) {
/*  18 */     if (str == null)
/*  19 */       return null;
/*  20 */     int nSize = str.length();
/*  21 */     StringBuffer sb = new StringBuffer(nSize);
/*  22 */     for (int i = 0; i < nSize; i++) {
/*  23 */       char c = str.charAt(i);
/*  24 */       switch (c) {
/*     */       case '&': 
/*  26 */         sb.append("&amp;");
/*  27 */         break;
/*     */       case '>': 
/*  29 */         sb.append("&gt;");
/*  30 */         break;
/*     */       case '<': 
/*  32 */         sb.append("&lt;");
/*  33 */         break;
/*     */       case '\'': 
/*  35 */         sb.append("''");
/*  36 */         break;
/*     */       default: 
/*  38 */         sb.append(c);
/*     */       }
/*     */     }
/*  41 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escapeSQLStringWithPlusChar(String str)
/*     */   {
/*  51 */     if (str == null)
/*  52 */       return null;
/*  53 */     int nSize = str.length();
/*  54 */     StringBuffer sb = new StringBuffer(nSize);
/*  55 */     for (int i = 0; i < nSize; i++) {
/*  56 */       char c = str.charAt(i);
/*  57 */       switch (c) {
/*     */       case '_': 
/*  59 */         sb.append("+_");
/*  60 */         break;
/*     */       case '\'': 
/*  62 */         sb.append("''");
/*  63 */         break;
/*     */       case '+': 
/*  65 */         sb.append("++");
/*  66 */         break;
/*     */       default: 
/*  68 */         sb.append(c);
/*     */       }
/*     */     }
/*  71 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String escapeSQLString(String str) {
/*  75 */     if (str == null)
/*  76 */       return null;
/*  77 */     int nSize = str.length();
/*  78 */     StringBuffer sb = new StringBuffer(nSize);
/*  79 */     for (int i = 0; i < nSize; i++) {
/*  80 */       char c = str.charAt(i);
/*  81 */       switch (c) {
/*     */       case '_': 
/*  83 */         sb.append("[_]");
/*  84 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case '\'': 
/*  89 */         sb.append("''");
/*  90 */         break;
/*     */       default: 
/*  92 */         sb.append(c);
/*     */       }
/*     */     }
/*  95 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String encodeForXML(String str) {
/*  99 */     if (str == null)
/* 100 */       return null;
/* 101 */     int nSize = str.length();
/* 102 */     StringBuffer sb = new StringBuffer(nSize);
/* 103 */     for (int i = 0; i < nSize; i++)
/*     */     {
/*     */ 
/*     */ 
/* 107 */       char c = str.charAt(i);
/* 108 */       switch (c) {
/*     */       case '<': 
/* 110 */         sb.append("&lt;");
/* 111 */         break;
/*     */       case '>': 
/* 113 */         sb.append("&gt;");
/* 114 */         break;
/*     */       case '"': 
/* 116 */         sb.append("&quot;");
/* 117 */         break;
/*     */       case '\'': 
/* 119 */         sb.append("&apos;");
/* 120 */         break;
/*     */       case '&': 
/* 122 */         sb.append("&amp;");
/* 123 */         break;
/*     */       default: 
/* 125 */         sb.append(c);
/*     */       }
/*     */     }
/* 128 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String decodeFromXML(String str) {
/* 132 */     if (str == null) {
/* 133 */       return null;
/*     */     }
/* 135 */     int nSize = str.length();
/* 136 */     StringBuffer sb = new StringBuffer(nSize);
/* 137 */     for (int i = 0; i < nSize; i++)
/*     */     {
/*     */ 
/* 140 */       char c = str.charAt(i);
/*     */       
/* 142 */       if (c == '&') {
/* 143 */         String entity = str.substring(i + 1, str.indexOf(';', i));
/*     */         
/* 145 */         if (entity.equals("lt")) {
/* 146 */           sb.append('<');
/* 147 */           i = str.indexOf(';', i);
/* 148 */         } else if (entity.equals("gt")) {
/* 149 */           sb.append('>');
/* 150 */           i = str.indexOf(';', i);
/* 151 */         } else if (entity.equals("quot")) {
/* 152 */           sb.append('"');
/* 153 */           i = str.indexOf(';', i);
/* 154 */         } else if (entity.equals("apos")) {
/* 155 */           sb.append('\'');
/* 156 */           i = str.indexOf(';', i);
/* 157 */         } else if (entity.equals("amp")) {
/* 158 */           sb.append('&');
/* 159 */           i = str.indexOf(';', i);
/*     */         } else {
/* 161 */           sb.append(c);
/*     */         }
/*     */       } else {
/* 164 */         sb.append(c);
/*     */       }
/*     */     }
/* 167 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\EncoderUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */