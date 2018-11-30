/*     */ package com.ibm.jarm.api.util;
/*     */ 
/*     */ import com.ibm.ier.logtrace.BaseLogger;
/*     */ import org.apache.log4j.Level;
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
/*     */ public class JarmLogger
/*     */   extends BaseLogger
/*     */ {
/*     */   private static final String DEFAULT_BUNDLE_BASENAME = "resources/JarmResources";
/*     */   
/*     */   private JarmLogger(String loggerName)
/*     */   {
/*  23 */     super(loggerName, "resources/JarmResources");
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
/*     */   public static JarmLogger getJarmLogger(String loggerName)
/*     */   {
/*  36 */     return new JarmLogger(loggerName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void error(RMLogCode code, Object... params)
/*     */   {
/*  48 */     String keyPrefix = generateLogResourceKeyPrefix(code);
/*  49 */     super.error(keyPrefix, params);
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
/*     */   public void error(RMLogCode code, Throwable t, Object... params)
/*     */   {
/*  65 */     String keyPrefix = generateLogResourceKeyPrefix(code);
/*  66 */     super.error(keyPrefix, t, params);
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
/*     */   public void warn(RMLogCode code, Object... params)
/*     */   {
/*  79 */     if (isEnabledFor(Level.WARN))
/*     */     {
/*  81 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/*  82 */       super.warn(keyPrefix, params);
/*     */     }
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
/*     */   public void warn(RMLogCode code, Throwable t, Object... params)
/*     */   {
/*  99 */     if (isEnabledFor(Level.WARN))
/*     */     {
/* 101 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/* 102 */       super.warn(keyPrefix, t, params);
/*     */     }
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
/*     */   public void info(RMLogCode code, Object... params)
/*     */   {
/* 116 */     if (isEnabledFor(Level.INFO))
/*     */     {
/* 118 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/* 119 */       super.info(keyPrefix, params);
/*     */     }
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
/*     */   public void info(RMLogCode code, Throwable t, Object... params)
/*     */   {
/* 136 */     if (isEnabledFor(Level.INFO))
/*     */     {
/* 138 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/* 139 */       super.info(keyPrefix, t, params);
/*     */     }
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
/*     */   private String generateLogResourceKeyPrefix(RMLogCode code)
/*     */   {
/* 154 */     return code.getMessageKey();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\JarmLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */