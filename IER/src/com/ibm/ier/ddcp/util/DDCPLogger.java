/*     */ package com.ibm.ier.ddcp.util;
/*     */ 
/*     */ import com.ibm.ier.logtrace.BaseLogger;
/*     */ import java.io.File;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.PatternLayout;
/*     */ import org.apache.log4j.RollingFileAppender;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DDCPLogger
/*     */   extends BaseLogger
/*     */ {
/*     */   public static final String IBM_PREFIX = "FNR";
/*     */   public static final String SWG_ID = "RD";
/*     */   public static final String MESSAGE_ID_PREFIX = "FNRRD";
/*     */   public static final String DEFAULT_BUNDLE_BASENAME = "resources/DDCPResources";
/*     */   private static final String DDCP_LOG_NAME_SUFFIX = "p8_DDCP_log.log";
/*     */   private static final String DDCP_LOG_APPENDER_NAME = "DDCPLogRollingAppender";
/*     */   private static final String DDCP_LOGGER_NAME = "com.ibm.ier.ddcp";
/*     */   
/*     */   private DDCPLogger(String loggerName)
/*     */   {
/*  64 */     super(loggerName, "resources/DDCPResources");
/*     */   }
/*     */   
/*     */   private DDCPLogger(String loggerName, String resourceBundleBasename)
/*     */   {
/*  69 */     super(loggerName, resourceBundleBasename);
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
/*     */   public static DDCPLogger getDDCPLogger(String loggerName)
/*     */   {
/*  82 */     return new DDCPLogger(loggerName);
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
/*     */   public static DDCPLogger getDDCPLogger(String loggerName, String logLocation, String taskName, Level logLevel)
/*     */   {
/* 100 */     if (logLevel == null)
/*     */     {
/* 102 */       return new DDCPLogger(loggerName);
/*     */     }
/*     */     
/*     */ 
/* 106 */     Logger theLogger = Logger.getLogger(loggerName);
/* 107 */     Appender theAppender = theLogger.getAppender("DDCPLogRollingAppender");
/* 108 */     if (theAppender == null)
/*     */     {
/* 110 */       StringBuilder fileNameBuilder = new StringBuilder();
/* 111 */       if ((logLocation != null) && (!"".equals(logLocation.trim()))) {
/* 112 */         fileNameBuilder.append(logLocation);
/*     */       } else {
/* 114 */         String ceDefaultLogLocation = DDCPUtil.getCEDefaultLogLocation();
/* 115 */         if (ceDefaultLogLocation != null) {
/* 116 */           fileNameBuilder.append(ceDefaultLogLocation);
/*     */         } else {
/* 118 */           fileNameBuilder.append(".");
/*     */         }
/*     */       }
/* 121 */       fileNameBuilder.append(File.separator);
/* 122 */       if ((taskName != null) && (!"".equals(taskName))) {
/* 123 */         fileNameBuilder.append(taskName).append("_");
/*     */       }
/* 125 */       fileNameBuilder.append("p8_DDCP_log.log");
/*     */       
/* 127 */       RollingFileAppender ddcpLogRollingAppender = new RollingFileAppender();
/* 128 */       ddcpLogRollingAppender.setName("DDCPLogRollingAppender");
/* 129 */       ddcpLogRollingAppender.setFile(fileNameBuilder.toString());
/* 130 */       ddcpLogRollingAppender.setLayout(new PatternLayout("%d %5p [%t] - %m\r\n"));
/* 131 */       ddcpLogRollingAppender.setThreshold(Level.INFO);
/* 132 */       ddcpLogRollingAppender.setAppend(true);
/* 133 */       ddcpLogRollingAppender.setMaxBackupIndex(5);
/* 134 */       ddcpLogRollingAppender.setMaxFileSize("100MB");
/* 135 */       ddcpLogRollingAppender.activateOptions();
/*     */       
/* 137 */       Logger ddcpRootLogger = Logger.getLogger("com.ibm.ier.ddcp");
/* 138 */       ddcpRootLogger.addAppender(ddcpLogRollingAppender);
/* 139 */       ddcpRootLogger.setLevel(logLevel);
/*     */       
/* 141 */       Logger ddcpRootErrorLogger = Logger.getLogger("com.ibm.ier.DDCP.Error");
/* 142 */       ddcpRootErrorLogger.addAppender(ddcpLogRollingAppender);
/* 143 */       ddcpRootErrorLogger.setLevel(Level.ERROR);
/*     */     }
/*     */     
/* 146 */     return new DDCPLogger(loggerName);
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
/*     */   public static DDCPLogger getDDCPLogger(String loggerName, String resourceBundleBasename)
/*     */   {
/* 162 */     return new DDCPLogger(loggerName, resourceBundleBasename);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void error(DDCPLogCode code, Object... params)
/*     */   {
/* 174 */     String keyPrefix = generateLogResourceKeyPrefix(code);
/* 175 */     super.error(keyPrefix, params);
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
/*     */   public void error(DDCPLogCode code, Throwable t, Object... params)
/*     */   {
/* 191 */     String keyPrefix = generateLogResourceKeyPrefix(code);
/* 192 */     super.error(keyPrefix, t, params);
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
/*     */   public void warn(DDCPLogCode code, Object... params)
/*     */   {
/* 205 */     if (isEnabledFor(Level.WARN))
/*     */     {
/* 207 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/* 208 */       super.warn(keyPrefix, params);
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
/*     */   public void warn(DDCPLogCode code, Throwable t, Object... params)
/*     */   {
/* 225 */     if (isEnabledFor(Level.WARN))
/*     */     {
/* 227 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/* 228 */       super.warn(keyPrefix, t, params);
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
/*     */   public void info(DDCPLogCode code, Object... params)
/*     */   {
/* 242 */     if (isEnabledFor(Level.INFO))
/*     */     {
/* 244 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/* 245 */       super.info(keyPrefix, params);
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
/*     */   public void info(DDCPLogCode code, Throwable t, Object... params)
/*     */   {
/* 262 */     if (isEnabledFor(Level.INFO))
/*     */     {
/* 264 */       String keyPrefix = generateLogResourceKeyPrefix(code);
/* 265 */       super.info(keyPrefix, t, params);
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
/*     */   private String generateLogResourceKeyPrefix(DDCPLogCode code)
/*     */   {
/* 280 */     return code.getMessageKey();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\util\DDCPLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */