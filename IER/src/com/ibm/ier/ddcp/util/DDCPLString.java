/*     */ package com.ibm.ier.ddcp.util;
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
/*     */ public class DDCPLString
/*     */ {
/*  30 */   private static DDCPTracer Tracer = DDCPTracer.getDDCPTracer(DDCPTracer.SubSystem.Util);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ResourceBundleBaseName = "resources/DDCPResources";
/*     */   
/*     */ 
/*     */ 
/*  38 */   private static final ResourceBundle DDCPResourceBundle = ResourceBundleService.get("resources/DDCPResources", Locale.getDefault());
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
/*  52 */     Tracer.traceMethodEntry(new Object[] { key });
/*  53 */     String result = null;
/*  54 */     if (DDCPResourceBundle != null) {
/*     */       try {
/*  56 */         result = DDCPResourceBundle.getString(key);
/*     */       }
/*     */       catch (MissingResourceException ignored) {}
/*     */     }
/*  60 */     Tracer.traceMethodExit(new Object[] { result });
/*  61 */     return result;
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
/*  80 */     Tracer.traceMethodEntry(new Object[] { key, defaultValue });
/*  81 */     String result = get(key);
/*  82 */     if (result == null) {
/*  83 */       result = defaultValue;
/*     */     }
/*  85 */     Tracer.traceMethodExit(new Object[] { result });
/*  86 */     return result;
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
/* 101 */     Tracer.traceMethodEntry(new Object[] { key, args });
/* 102 */     String result = null;
/* 103 */     String pattern = get(key);
/* 104 */     if (pattern != null) {
/* 105 */       result = formatResult(pattern, args);
/*     */     }
/* 107 */     Tracer.traceMethodExit(new Object[] { result });
/* 108 */     return result;
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
/* 125 */     Tracer.traceMethodEntry(new Object[] { key, defaultPattern, args });
/* 126 */     String result = null;
/* 127 */     String pattern = get(key, defaultPattern);
/* 128 */     if (pattern != null) {
/* 129 */       result = formatResult(pattern, args);
/*     */     }
/* 131 */     Tracer.traceMethodExit(new Object[] { result });
/* 132 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String formatResult(String pattern, Object... args)
/*     */   {
/* 144 */     Tracer.traceMethodEntry(new Object[] { pattern, args });
/* 145 */     String result = MessageFormat.format(pattern, args);
/* 146 */     Tracer.traceMethodExit(new Object[] { result });
/* 147 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\util\DDCPLString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */