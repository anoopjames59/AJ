/*     */ package com.ibm.ier.plugin.nls;
/*     */ 
/*     */ import com.ibm.ecm.extension.PluginLogger;
/*     */ import com.ibm.ier.plugin.IERApplicationPlugin;
/*     */ import java.util.Date;
/*     */ import javax.servlet.ServletRequest;
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
/*     */ 
/*     */ 
/*     */ public class Logger
/*     */ {
/*     */   public static void logEntry(Object object, String methodName, ServletRequest request)
/*     */   {
/*  33 */     IERApplicationPlugin.getPluginLogger().logEntry(object, methodName, request);
/*     */   }
/*     */   
/*     */   public static void logEntry(Object object, String methodName) {
/*  37 */     IERApplicationPlugin.getPluginLogger().logEntry(object, methodName);
/*     */   }
/*     */   
/*     */   public static void logEntry(Object object, String methodName, ServletRequest request, String message) {
/*  41 */     IERApplicationPlugin.getPluginLogger().logEntry(object, methodName, request, message);
/*     */   }
/*     */   
/*     */   public static void logEntry(Object object, String methodName, String message) {
/*  45 */     IERApplicationPlugin.getPluginLogger().logEntry(object, methodName, message);
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
/*     */   public static void logExit(Object object, String methodName, ServletRequest request)
/*     */   {
/*  59 */     IERApplicationPlugin.getPluginLogger().logExit(object, methodName, request);
/*     */   }
/*     */   
/*     */   public static void logExit(Object object, String methodName, ServletRequest request, String message) {
/*  63 */     IERApplicationPlugin.getPluginLogger().logExit(object, methodName, request, message);
/*     */   }
/*     */   
/*     */   public static void logExit(Object object, String methodName, String message) {
/*  67 */     IERApplicationPlugin.getPluginLogger().logExit(object, methodName, message);
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
/*     */   public static void logError(Object object, String methodName, Exception exp)
/*     */   {
/*  83 */     IERApplicationPlugin.getPluginLogger().logError(object, methodName, exp);
/*     */   }
/*     */   
/*     */   public static void logError(Object object, String methodName, ServletRequest request, String text) {
/*  87 */     IERApplicationPlugin.getPluginLogger().logError(object, methodName, request, text);
/*     */   }
/*     */   
/*     */   public static void logError(Object object, String methodName, String text) {
/*  91 */     IERApplicationPlugin.getPluginLogger().logError(object, methodName, text);
/*     */   }
/*     */   
/*     */   public static void logError(Object object, String methodName, ServletRequest request, Throwable exception) {
/*  95 */     IERApplicationPlugin.getPluginLogger().logError(object, methodName, request, exception);
/*     */   }
/*     */   
/*     */   public static void logError(Object object, String methodName, Throwable exception) {
/*  99 */     IERApplicationPlugin.getPluginLogger().logError(object, methodName, exception);
/*     */   }
/*     */   
/*     */   public static void logError(Object object, String methodName, ServletRequest request, String text, Throwable exception) {
/* 103 */     IERApplicationPlugin.getPluginLogger().logError(object, methodName, request, text, exception);
/*     */   }
/*     */   
/*     */   public static void logError(Object object, String methodName, String text, Throwable exception) {
/* 107 */     IERApplicationPlugin.getPluginLogger().logError(object, methodName, text, exception);
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
/*     */   public static void logWarning(Object object, String methodName, ServletRequest request, String text)
/*     */   {
/* 123 */     IERApplicationPlugin.getPluginLogger().logWarning(object, methodName, request, text);
/*     */   }
/*     */   
/*     */   public static void logWarning(Object object, String methodName, String text) {
/* 127 */     IERApplicationPlugin.getPluginLogger().logWarning(object, methodName, text);
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
/*     */   public static void logInfo(Object object, String methodName, ServletRequest request, String text)
/*     */   {
/* 143 */     IERApplicationPlugin.getPluginLogger().logInfo(object, methodName, request, text);
/*     */   }
/*     */   
/*     */   public static void logInfo(Object object, String methodName, String text) {
/* 147 */     IERApplicationPlugin.getPluginLogger().logInfo(object, methodName, text);
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
/*     */   public static Date logPerf(Object object, String methodName, ServletRequest request)
/*     */   {
/* 163 */     return IERApplicationPlugin.getPluginLogger().logPerf(object, methodName, request);
/*     */   }
/*     */   
/*     */   public static Date logPerf(Object object, String methodName, ServletRequest request, String message) {
/* 167 */     return IERApplicationPlugin.getPluginLogger().logPerf(object, methodName, request, message);
/*     */   }
/*     */   
/*     */   public static Date logPerf(Object object, String methodName, ServletRequest request, Date date) {
/* 171 */     return IERApplicationPlugin.getPluginLogger().logPerf(object, methodName, request, date);
/*     */   }
/*     */   
/*     */   public static Date logPerf(Object object, String methodName, ServletRequest request, Date date, String message) {
/* 175 */     return IERApplicationPlugin.getPluginLogger().logPerf(object, methodName, request, date, message);
/*     */   }
/*     */   
/*     */   public static Date logPerf(Object object, String methodName, Date date, String message) {
/* 179 */     return IERApplicationPlugin.getPluginLogger().logPerf(object, methodName, date, message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isDebugLogged()
/*     */   {
/* 187 */     return IERApplicationPlugin.getPluginLogger().isDebugLogged();
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
/*     */   public static void logDebug(Object loggingObject, String methodName, ServletRequest request, String message)
/*     */   {
/* 203 */     IERApplicationPlugin.getPluginLogger().logDebug(loggingObject, methodName, request, message);
/*     */   }
/*     */   
/*     */   public static void logDebug(Object loggingObject, String methodName, String message) {
/* 207 */     IERApplicationPlugin.getPluginLogger().logDebug(loggingObject, methodName, message);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\nls\Logger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */