/*     */ package com.ibm.jarm.api.util;
/*     */ 
/*     */ import com.ibm.ier.logtrace.ResourceBundleService;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RMLString
/*     */ {
/*  30 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ResourceBundleBaseName = "resources/JarmResources";
/*     */   
/*     */ 
/*  37 */   private static final ResourceBundle JARMResourceBundle = ResourceBundleService.get("resources/JarmResources", Locale.getDefault());
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
/*     */   public static String get(String key)
/*     */   {
/*  51 */     Tracer.traceMethodEntry(new Object[] { key });
/*  52 */     String result = null;
/*  53 */     if (JARMResourceBundle != null) {
/*     */       try {
/*  55 */         result = JARMResourceBundle.getString(key);
/*     */       }
/*     */       catch (MissingResourceException ignored) {}
/*     */     }
/*  59 */     Tracer.traceMethodExit(new Object[] { result });
/*  60 */     return result;
/*     */   }
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
/*     */   public static String get(String key, String defaultValue)
/*     */   {
/*  79 */     Tracer.traceMethodEntry(new Object[] { key, defaultValue });
/*  80 */     String result = get(key);
/*  81 */     if (result == null) {
/*  82 */       result = defaultValue;
/*     */     }
/*  84 */     Tracer.traceMethodExit(new Object[] { result });
/*  85 */     return result;
/*     */   }
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
/*     */   public static String format(String key, Object... args)
/*     */   {
/* 100 */     Tracer.traceMethodEntry(new Object[] { key, args });
/* 101 */     String result = null;
/* 102 */     String pattern = get(key);
/* 103 */     if (pattern != null) {
/* 104 */       result = formatResult(pattern, args);
/*     */     }
/* 106 */     Tracer.traceMethodExit(new Object[] { result });
/* 107 */     return result;
/*     */   }
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
/*     */   public static String format(String key, String defaultPattern, Object... args)
/*     */   {
/* 124 */     Tracer.traceMethodEntry(new Object[] { key, defaultPattern, args });
/* 125 */     String result = null;
/* 126 */     String pattern = get(key, defaultPattern);
/* 127 */     if (pattern != null) {
/* 128 */       result = formatResult(pattern, args);
/*     */     }
/* 130 */     Tracer.traceMethodExit(new Object[] { result });
/* 131 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private static String formatResult(String pattern, Object... args)
/*     */   {
/* 137 */     Tracer.traceMethodEntry(new Object[] { pattern, args });
/* 138 */     String result = MessageFormat.format(pattern, args);
/* 139 */     Tracer.traceMethodExit(new Object[] { result });
/* 140 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\RMLString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */