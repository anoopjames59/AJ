/*     */ package com.ibm.ier.logtrace;
/*     */ 
/*     */ import java.text.FieldPosition;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
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
/*     */ public class BaseLogger
/*     */ {
/*     */   private static final String SEPARATOR = ": ";
/*     */   private static final String MESSAGE_KEY_SUFFIX = ".message";
/*     */   private Logger realLogger;
/*     */   private String resourceBundleBasename;
/*     */   
/*     */   public static enum Severity
/*     */   {
/*  51 */     Error('E'), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  56 */     Warn('W'), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  61 */     Info('I');
/*     */     
/*     */     private final char charValue;
/*     */     
/*     */     private Severity(char charValue)
/*     */     {
/*  67 */       this.charValue = charValue;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char getChar()
/*     */     {
/*  77 */       return this.charValue;
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
/*     */ 
/*     */ 
/*     */ 
/*  95 */   private static Locale LoggingLocale = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ResourceBundle resourceBundle;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BaseLogger(String loggerName, String resourceBundleBasename)
/*     */   {
/* 116 */     if ((loggerName == null) || (loggerName.trim().length() == 0))
/*     */     {
/* 118 */       throw new RuntimeException("'loggerName' parameter cannot be null nor blank.");
/*     */     }
/*     */     
/* 121 */     if ((resourceBundleBasename == null) || (resourceBundleBasename.trim().length() == 0))
/*     */     {
/* 123 */       throw new RuntimeException("'resourceBundleBasename' parameter cannot be null nor blank.");
/*     */     }
/*     */     
/* 126 */     this.realLogger = Logger.getLogger(loggerName);
/* 127 */     establishResourceBundleBasename(resourceBundleBasename);
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
/*     */   protected BaseLogger(Logger providedLogger, String resourceBundleBasename)
/*     */   {
/* 145 */     if (providedLogger == null)
/*     */     {
/* 147 */       throw new RuntimeException("'providedLogger' parameter cannot be null.");
/*     */     }
/*     */     
/* 150 */     if ((resourceBundleBasename == null) || (resourceBundleBasename.trim().length() == 0))
/*     */     {
/* 152 */       throw new RuntimeException("'resourceBundleBasename' parameter cannot be null nor blank.");
/*     */     }
/*     */     
/* 155 */     this.realLogger = providedLogger;
/* 156 */     establishResourceBundleBasename(resourceBundleBasename);
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
/*     */   public static BaseLogger getBaseLogger(String loggerName, String resourceBundleBasename)
/*     */   {
/* 175 */     return new BaseLogger(loggerName, resourceBundleBasename);
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
/*     */   public static BaseLogger getBaseLogger(Logger providedLogger, String resourceBundleBasename)
/*     */   {
/* 194 */     return new BaseLogger(providedLogger, resourceBundleBasename);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized void setLocale(Locale locale)
/*     */   {
/* 205 */     LoggingLocale = locale != null ? locale : Locale.getDefault();
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
/*     */   public boolean isEnabledFor(Level level)
/*     */   {
/* 222 */     return this.realLogger.isEnabledFor(level);
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
/*     */   public void error(String key, Object... params)
/*     */   {
/* 235 */     if (isEnabledFor(Level.ERROR))
/*     */     {
/* 237 */       String formattedMsg = formatMessageWithPrefix(key, Severity.Error, params);
/* 238 */       this.realLogger.error(formattedMsg);
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
/*     */   public void error(String key, Throwable t, Object... params)
/*     */   {
/* 255 */     if (isEnabledFor(Level.ERROR))
/*     */     {
/* 257 */       String formattedMsg = formatMessageWithPrefix(key, Severity.Error, params);
/* 258 */       this.realLogger.error(formattedMsg, t);
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
/*     */   public void warn(String key, Object... params)
/*     */   {
/* 274 */     if (isEnabledFor(Level.WARN))
/*     */     {
/* 276 */       String formattedMsg = formatMessageWithPrefix(key, Severity.Warn, params);
/* 277 */       this.realLogger.warn(formattedMsg);
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
/*     */ 
/*     */ 
/*     */   public void warn(String key, Throwable t, Object... params)
/*     */   {
/* 296 */     if (isEnabledFor(Level.WARN))
/*     */     {
/* 298 */       String formattedMsg = formatMessageWithPrefix(key, Severity.Warn, params);
/* 299 */       this.realLogger.warn(formattedMsg, t);
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
/*     */   public void info(String key, Object... params)
/*     */   {
/* 315 */     if (isEnabledFor(Level.INFO))
/*     */     {
/* 317 */       String formattedMsg = formatMessageWithPrefix(key, Severity.Info, params);
/* 318 */       this.realLogger.info(formattedMsg);
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
/*     */ 
/*     */ 
/*     */   public void info(String key, Throwable t, Object... params)
/*     */   {
/* 337 */     if (isEnabledFor(Level.INFO))
/*     */     {
/* 339 */       String formattedMsg = formatMessageWithPrefix(key, Severity.Info, params);
/* 340 */       this.realLogger.info(formattedMsg, t);
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
/*     */   protected synchronized ResourceBundle getResourceBundle()
/*     */   {
/* 354 */     if (this.resourceBundle == null)
/*     */     {
/* 356 */       Locale locale = LoggingLocale;
/* 357 */       this.resourceBundle = ResourceBundleService.get(this.resourceBundleBasename, locale);
/*     */     }
/*     */     
/* 360 */     return this.resourceBundle;
/*     */   }
/*     */   
/*     */   private void establishResourceBundleBasename(String resourceBundleBasename)
/*     */   {
/* 365 */     String baseName = resourceBundleBasename != null ? resourceBundleBasename.trim() : null;
/* 366 */     if ((baseName != null) && (baseName.length() > 0))
/*     */     {
/* 368 */       this.resourceBundleBasename = baseName;
/*     */     }
/*     */     else
/*     */     {
/* 372 */       this.resourceBundleBasename = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String formatMessageWithPrefix(String prefix, Severity severity, Object... params)
/*     */   {
/*     */     String actualKey;
/*     */     
/*     */ 
/*     */     String actualPrefix;
/*     */     
/*     */ 
/*     */     String actualKey;
/*     */     
/*     */ 
/* 390 */     if (prefix.endsWith(".message"))
/*     */     {
/* 392 */       String actualPrefix = prefix.substring(0, prefix.length() - ".message".length());
/* 393 */       actualKey = prefix;
/*     */     }
/*     */     else
/*     */     {
/* 397 */       actualPrefix = prefix;
/* 398 */       actualKey = prefix + ".message";
/*     */     }
/*     */     
/* 401 */     StringBuffer sb = new StringBuffer(64);
/* 402 */     sb.append(actualPrefix);
/* 403 */     sb.append(severity.getChar());
/* 404 */     sb.append(": ");
/*     */     
/* 406 */     String msgPattern = getResourceBundle().getString(actualKey);
/* 407 */     MessageFormat mf = new MessageFormat(msgPattern, LoggingLocale);
/* 408 */     FieldPosition pos = null;
/*     */     
/* 410 */     return mf.format(params, sb, pos).toString();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\logtrace\BaseLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */