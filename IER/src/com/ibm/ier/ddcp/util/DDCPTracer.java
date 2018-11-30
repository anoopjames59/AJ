/*     */ package com.ibm.ier.ddcp.util;
/*     */ 
/*     */ import com.ibm.ier.logtrace.BaseTracer;
/*     */ import com.ibm.ier.logtrace.BaseTracer.TraceLevel;
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
/*     */ public class DDCPTracer
/*     */   extends BaseTracer
/*     */ {
/*     */   private static final String DDCP_TRACE_PREFIX = "DDCPTrace";
/*     */   private static final String DDCP_TRACE_NAME_SUFFIX = "p8_DDCP_trace.log";
/*     */   private static final String DDCP_TRACE_APPENDER_NAME = "DDCPTraceRollingAppender";
/*     */   
/*     */   private DDCPTracer(String traceNamePrefix)
/*     */   {
/*  39 */     super(traceNamePrefix);
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
/*     */   public static DDCPTracer getDDCPTracer(SubSystem subSystem)
/*     */   {
/*  53 */     String traceNamePrefix = "DDCPTrace." + subSystem.getLabel();
/*  54 */     return new DDCPTracer(traceNamePrefix);
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
/*     */   public static DDCPTracer getDDCPTracer(SubSystem subSystem, String logLocation, String taskName, BaseTracer.TraceLevel traceLevel)
/*     */   {
/*  72 */     String traceNamePrefix = "DDCPTrace." + subSystem.getLabel();
/*  73 */     if (traceLevel == null)
/*     */     {
/*  75 */       return new DDCPTracer(traceNamePrefix);
/*     */     }
/*     */     
/*  78 */     StringBuilder traceNameBuilder = new StringBuilder();
/*  79 */     traceNameBuilder.append("DDCPTrace").append('.');
/*  80 */     traceNameBuilder.append(subSystem.getLabel()).append('.');
/*  81 */     traceNameBuilder.append(traceLevel.getLoggerNameSuffix());
/*  82 */     String tracerName = traceNameBuilder.toString();
/*     */     
/*     */ 
/*  85 */     Logger theLogger = Logger.getLogger(tracerName);
/*  86 */     Appender theAppender = theLogger.getAppender("DDCPTraceRollingAppender");
/*  87 */     if (theAppender == null)
/*     */     {
/*  89 */       StringBuilder fileNameBuilder = new StringBuilder();
/*  90 */       if ((logLocation != null) && (!"".equals(logLocation.trim()))) {
/*  91 */         fileNameBuilder.append(logLocation);
/*     */       } else {
/*  93 */         String ceDefaultLogLocation = DDCPUtil.getCEDefaultLogLocation();
/*  94 */         if (ceDefaultLogLocation != null) {
/*  95 */           fileNameBuilder.append(ceDefaultLogLocation);
/*     */         } else {
/*  97 */           fileNameBuilder.append(".");
/*     */         }
/*     */       }
/* 100 */       fileNameBuilder.append(File.separator);
/* 101 */       if ((taskName != null) && (!"".equals(taskName.trim()))) {
/* 102 */         fileNameBuilder.append(taskName).append("_");
/*     */       }
/* 104 */       fileNameBuilder.append("p8_DDCP_trace.log");
/*     */       
/* 106 */       RollingFileAppender ddcpTraceRollingAppender = new RollingFileAppender();
/* 107 */       ddcpTraceRollingAppender.setName("DDCPTraceRollingAppender");
/* 108 */       ddcpTraceRollingAppender.setFile(fileNameBuilder.toString());
/* 109 */       ddcpTraceRollingAppender.setLayout(new PatternLayout("%d %5p [%t] - %m\r\n"));
/* 110 */       ddcpTraceRollingAppender.setThreshold(Level.ALL);
/* 111 */       ddcpTraceRollingAppender.setAppend(true);
/* 112 */       ddcpTraceRollingAppender.setMaxBackupIndex(5);
/* 113 */       ddcpTraceRollingAppender.setMaxFileSize("100MB");
/* 114 */       ddcpTraceRollingAppender.activateOptions();
/*     */       
/*     */ 
/* 117 */       for (SubSystem aSubSystem : SubSystem.values()) {
/* 118 */         traceNameBuilder = new StringBuilder();
/* 119 */         traceNameBuilder.append("DDCPTrace").append('.');
/* 120 */         traceNameBuilder.append(aSubSystem.getLabel()).append('.');
/* 121 */         traceNameBuilder.append(traceLevel.getLoggerNameSuffix());
/* 122 */         tracerName = traceNameBuilder.toString();
/*     */         
/* 124 */         Logger ddcpTracer = Logger.getLogger(tracerName);
/* 125 */         ddcpTracer.addAppender(ddcpTraceRollingAppender);
/* 126 */         ddcpTracer.setLevel(Level.DEBUG);
/*     */       }
/*     */     }
/*     */     
/* 130 */     return new DDCPTracer(traceNamePrefix);
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
/*     */   public void traceMethodEntry(Object... methodParams)
/*     */   {
/* 149 */     BaseTracer.TraceLevel traceLevel = null;
/* 150 */     Logger traceLogger = getMaximumLog4jLogger();
/* 151 */     if (traceLogger.isDebugEnabled())
/*     */     {
/* 153 */       traceLevel = BaseTracer.TraceLevel.Maximum;
/*     */     }
/*     */     else
/*     */     {
/* 157 */       traceLogger = getMediumLog4jLogger();
/* 158 */       if (traceLogger.isDebugEnabled())
/*     */       {
/* 160 */         traceLevel = BaseTracer.TraceLevel.Medium;
/*     */       }
/*     */       else
/*     */       {
/* 164 */         traceLogger = getMinimumLog4jLogger();
/* 165 */         if (traceLogger.isDebugEnabled())
/*     */         {
/* 167 */           traceLevel = BaseTracer.TraceLevel.Minimum;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 172 */     if ((traceLevel != null) && (traceLevel != BaseTracer.TraceLevel.Minimum))
/*     */     {
/* 174 */       String methodParamStr = null;
/* 175 */       if (traceLevel == BaseTracer.TraceLevel.Minimum)
/*     */       {
/*     */ 
/* 178 */         methodParamStr = null;
/*     */       }
/* 180 */       else if (traceLevel == BaseTracer.TraceLevel.Medium)
/*     */       {
/* 182 */         methodParamStr = " #params: " + (methodParams != null ? methodParams.length : 0);
/*     */       }
/* 184 */       else if (traceLevel == BaseTracer.TraceLevel.Maximum)
/*     */       {
/* 186 */         StringBuilder sb = new StringBuilder(64);
/* 187 */         sb.append(" #params: ").append(methodParams != null ? methodParams.length : 0).append(" - ");
/* 188 */         if (methodParams != null)
/*     */         {
/* 190 */           appendParams(sb, methodParams);
/*     */         }
/* 192 */         methodParamStr = sb.toString();
/*     */       }
/*     */       
/* 195 */       logMethodNameForTrace(traceLogger, FQCN, Level.DEBUG, "Enter ", methodParamStr);
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
/*     */   public void traceMethodExit(Object... returnValue)
/*     */   {
/* 232 */     BaseTracer.TraceLevel traceLevel = null;
/* 233 */     Logger traceLogger = getMaximumLog4jLogger();
/* 234 */     if (traceLogger.isDebugEnabled())
/*     */     {
/* 236 */       traceLevel = BaseTracer.TraceLevel.Maximum;
/*     */     }
/*     */     else
/*     */     {
/* 240 */       traceLogger = getMediumLog4jLogger();
/* 241 */       if (traceLogger.isDebugEnabled())
/*     */       {
/* 243 */         traceLevel = BaseTracer.TraceLevel.Medium;
/*     */       }
/*     */       else
/*     */       {
/* 247 */         traceLogger = getMinimumLog4jLogger();
/* 248 */         if (traceLogger.isDebugEnabled())
/*     */         {
/* 250 */           traceLevel = BaseTracer.TraceLevel.Minimum;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 255 */     if ((traceLevel != null) && (traceLevel != BaseTracer.TraceLevel.Minimum))
/*     */     {
/* 257 */       String returnValueStr = null;
/* 258 */       if (traceLevel == BaseTracer.TraceLevel.Minimum)
/*     */       {
/*     */ 
/* 261 */         returnValueStr = null;
/*     */       }
/* 263 */       else if (traceLevel == BaseTracer.TraceLevel.Medium)
/*     */       {
/*     */ 
/* 266 */         returnValueStr = null;
/*     */       }
/* 268 */       else if (traceLevel == BaseTracer.TraceLevel.Maximum)
/*     */       {
/*     */ 
/* 271 */         if ((returnValue != null) && (returnValue.length > 0) && (returnValue[0] != null))
/*     */         {
/* 273 */           StringBuilder sb = new StringBuilder(32);
/* 274 */           sb.append(" = ");
/* 275 */           appendItem(sb, returnValue[0]);
/* 276 */           returnValueStr = sb.toString();
/*     */         }
/*     */       }
/*     */       
/* 280 */       logMethodNameForTrace(traceLogger, FQCN, Level.DEBUG, "Exit  ", returnValueStr);
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
/*     */   public static enum SubSystem
/*     */   {
/* 293 */     Report("report"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 298 */     Delete("delete"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 303 */     Util("util");
/*     */     
/*     */ 
/*     */     private String label;
/*     */     
/*     */     private SubSystem(String label)
/*     */     {
/* 310 */       this.label = label;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getLabel()
/*     */     {
/* 321 */       return this.label;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\util\DDCPTracer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */