/*     */ package com.ibm.ier.logtrace;
/*     */ 
/*     */ import java.text.MessageFormat;
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
/*     */ public class BaseTracer
/*     */ {
/*  27 */   protected static final String FQCN = BaseTracer.class.getName();
/*     */   
/*     */ 
/*     */   protected static final String ENTER_STR = "Enter ";
/*     */   
/*     */ 
/*     */   protected static final String EXIT_STR = "Exit  ";
/*     */   
/*     */ 
/*     */   protected static final String EXT_CALL = "ExtCall ";
/*     */   
/*     */ 
/*     */   protected String baseLoggerName;
/*     */   
/*     */ 
/*     */   protected String timerLoggerName;
/*     */   
/*     */ 
/*     */   protected String minimumLoggerName;
/*     */   
/*     */ 
/*     */   protected String mediumLoggerName;
/*     */   
/*     */ 
/*     */   protected String maximumLoggerName;
/*     */   
/*     */ 
/*     */   protected Logger maximumActualLogger;
/*     */   
/*     */   protected Logger mediumActualLogger;
/*     */   
/*     */   protected Logger minimumActualLogger;
/*     */   
/*     */   protected Logger timerActualLogger;
/*     */   
/*     */ 
/*     */   public static BaseTracer getBaseTracer(String traceNamePrefix)
/*     */   {
/*  65 */     return new BaseTracer(traceNamePrefix);
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
/*     */   public void overrideTraceLoggers(Logger maximum, Logger medium, Logger minimum, Logger timer)
/*     */   {
/*  84 */     this.maximumActualLogger = maximum;
/*  85 */     this.mediumActualLogger = medium;
/*  86 */     this.minimumActualLogger = minimum;
/*  87 */     this.timerActualLogger = timer;
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
/*     */   protected BaseTracer(String traceNamePrefix)
/*     */   {
/* 108 */     if ((traceNamePrefix == null) || (traceNamePrefix.trim().length() == 0))
/*     */     {
/* 110 */       throw new RuntimeException("'traceNamePrefix' parameter cannot be null nor blank");
/*     */     }
/*     */     
/* 113 */     this.baseLoggerName = traceNamePrefix;
/*     */     
/*     */ 
/*     */ 
/* 117 */     this.timerLoggerName = createTraceLoggerName(TraceLevel.Timer);
/* 118 */     this.minimumLoggerName = createTraceLoggerName(TraceLevel.Minimum);
/* 119 */     this.mediumLoggerName = createTraceLoggerName(TraceLevel.Medium);
/* 120 */     this.maximumLoggerName = createTraceLoggerName(TraceLevel.Maximum);
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
/*     */   public void enableTraceLevel(TraceLevel traceLevel)
/*     */   {
/* 139 */     switch (traceLevel)
/*     */     {
/*     */     case Maximum: 
/* 142 */       getMaximumLog4jLogger().setLevel(Level.DEBUG);
/* 143 */       getMediumLog4jLogger().setLevel(Level.DEBUG);
/* 144 */       getMinimumLog4jLogger().setLevel(Level.DEBUG);
/* 145 */       break;
/*     */     
/*     */     case Medium: 
/* 148 */       getMediumLog4jLogger().setLevel(Level.DEBUG);
/* 149 */       getMinimumLog4jLogger().setLevel(Level.DEBUG);
/* 150 */       break;
/*     */     
/*     */     case Minimum: 
/* 153 */       getMinimumLog4jLogger().setLevel(Level.DEBUG);
/* 154 */       break;
/*     */     
/*     */     case Timer: 
/* 157 */       getTimerLog4jLogger().setLevel(Level.DEBUG);
/*     */     }
/*     */     
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
/*     */   public void disableTraceLevel(TraceLevel traceLevel)
/*     */   {
/* 178 */     switch (traceLevel)
/*     */     {
/*     */     case Minimum: 
/* 181 */       getMinimumLog4jLogger().setLevel(Level.OFF);
/* 182 */       getMediumLog4jLogger().setLevel(Level.OFF);
/* 183 */       getMaximumLog4jLogger().setLevel(Level.OFF);
/* 184 */       break;
/*     */     
/*     */     case Medium: 
/* 187 */       getMediumLog4jLogger().setLevel(Level.OFF);
/* 188 */       getMaximumLog4jLogger().setLevel(Level.OFF);
/* 189 */       break;
/*     */     
/*     */     case Maximum: 
/* 192 */       getMaximumLog4jLogger().setLevel(Level.OFF);
/* 193 */       break;
/*     */     
/*     */     case Timer: 
/* 196 */       getTimerLog4jLogger().setLevel(Level.OFF);
/*     */     }
/*     */     
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
/*     */   public boolean isEnabledFor(TraceLevel traceLevel)
/*     */   {
/* 211 */     boolean levelEnabled = false;
/* 212 */     switch (traceLevel)
/*     */     {
/*     */     case Maximum: 
/* 215 */       levelEnabled = isMaximumTraceEnabled();
/* 216 */       break;
/*     */     
/*     */     case Medium: 
/* 219 */       levelEnabled = isMediumTraceEnabled();
/* 220 */       break;
/*     */     
/*     */     case Minimum: 
/* 223 */       levelEnabled = isMinimumTraceEnabled();
/* 224 */       break;
/*     */     
/*     */     case Timer: 
/* 227 */       levelEnabled = isTimerTraceEnabled();
/*     */     }
/*     */     
/*     */     
/* 231 */     return levelEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTimerTraceEnabled()
/*     */   {
/* 242 */     return getTimerLog4jLogger().isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMinimumTraceEnabled()
/*     */   {
/* 253 */     return getMinimumLog4jLogger().isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMediumTraceEnabled()
/*     */   {
/* 264 */     return getMediumLog4jLogger().isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMaximumTraceEnabled()
/*     */   {
/* 275 */     return getMaximumLog4jLogger().isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void traceMaximumMsg(String msg, Object... parameters)
/*     */   {
/* 287 */     String formattedMsg = MessageFormat.format(msg, parameters);
/* 288 */     getMaximumLog4jLogger().log(FQCN, Level.DEBUG, formattedMsg, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void traceMediumMsg(String msg, Object... parameters)
/*     */   {
/* 300 */     String formattedMsg = MessageFormat.format(msg, parameters);
/* 301 */     getMediumLog4jLogger().log(FQCN, Level.DEBUG, formattedMsg, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void traceMinimumMsg(String msg, Object... parameters)
/*     */   {
/* 313 */     String formattedMsg = MessageFormat.format(msg, parameters);
/* 314 */     getMinimumLog4jLogger().log(FQCN, Level.DEBUG, formattedMsg, null);
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
/* 333 */     TraceLevel traceLevel = null;
/* 334 */     Logger traceLogger = getMaximumLog4jLogger();
/* 335 */     if (traceLogger.isDebugEnabled())
/*     */     {
/* 337 */       traceLevel = TraceLevel.Maximum;
/*     */     }
/*     */     else
/*     */     {
/* 341 */       traceLogger = getMediumLog4jLogger();
/* 342 */       if (traceLogger.isDebugEnabled())
/*     */       {
/* 344 */         traceLevel = TraceLevel.Medium;
/*     */       }
/*     */       else
/*     */       {
/* 348 */         traceLogger = getMinimumLog4jLogger();
/* 349 */         if (traceLogger.isDebugEnabled())
/*     */         {
/* 351 */           traceLevel = TraceLevel.Minimum;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 356 */     if (traceLevel != null)
/*     */     {
/* 358 */       String methodParamStr = null;
/* 359 */       if (traceLevel == TraceLevel.Minimum)
/*     */       {
/*     */ 
/* 362 */         methodParamStr = null;
/*     */       }
/* 364 */       else if (traceLevel == TraceLevel.Medium)
/*     */       {
/* 366 */         methodParamStr = " #params: " + (methodParams != null ? methodParams.length : 0);
/*     */       }
/* 368 */       else if (traceLevel == TraceLevel.Maximum)
/*     */       {
/* 370 */         StringBuilder sb = new StringBuilder(64);
/* 371 */         sb.append(" #params: ").append(methodParams != null ? methodParams.length : 0).append(" - ");
/* 372 */         if (methodParams != null)
/*     */         {
/* 374 */           appendParams(sb, methodParams);
/*     */         }
/* 376 */         methodParamStr = sb.toString();
/*     */       }
/*     */       
/* 379 */       logMethodNameForTrace(traceLogger, FQCN, Level.DEBUG, "Enter ", methodParamStr);
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
/* 416 */     TraceLevel traceLevel = null;
/* 417 */     Logger traceLogger = getMaximumLog4jLogger();
/* 418 */     if (traceLogger.isDebugEnabled())
/*     */     {
/* 420 */       traceLevel = TraceLevel.Maximum;
/*     */     }
/*     */     else
/*     */     {
/* 424 */       traceLogger = getMediumLog4jLogger();
/* 425 */       if (traceLogger.isDebugEnabled())
/*     */       {
/* 427 */         traceLevel = TraceLevel.Medium;
/*     */       }
/*     */       else
/*     */       {
/* 431 */         traceLogger = getMinimumLog4jLogger();
/* 432 */         if (traceLogger.isDebugEnabled())
/*     */         {
/* 434 */           traceLevel = TraceLevel.Minimum;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 439 */     if (traceLevel != null)
/*     */     {
/* 441 */       String returnValueStr = null;
/* 442 */       if (traceLevel == TraceLevel.Minimum)
/*     */       {
/*     */ 
/* 445 */         returnValueStr = null;
/*     */       }
/* 447 */       else if (traceLevel == TraceLevel.Medium)
/*     */       {
/*     */ 
/* 450 */         returnValueStr = null;
/*     */       }
/* 452 */       else if (traceLevel == TraceLevel.Maximum)
/*     */       {
/*     */ 
/* 455 */         if ((returnValue != null) && (returnValue.length > 0) && (returnValue[0] != null))
/*     */         {
/* 457 */           StringBuilder sb = new StringBuilder(32);
/* 458 */           sb.append(" = ");
/* 459 */           appendItem(sb, returnValue[0]);
/* 460 */           returnValueStr = sb.toString();
/*     */         }
/*     */       }
/*     */       
/* 464 */       logMethodNameForTrace(traceLogger, FQCN, Level.DEBUG, "Exit  ", returnValueStr);
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
/*     */   public void traceTimer(String msg, long startTimeMS, long endTimeMS)
/*     */   {
/* 477 */     Logger actualLogger = getTimerLog4jLogger();
/* 478 */     if (actualLogger.isDebugEnabled())
/*     */     {
/* 480 */       StringBuilder sb = new StringBuilder(64);
/* 481 */       sb.append(msg);
/* 482 */       sb.append(' ').append(endTimeMS - startTimeMS).append(" ms");
/* 483 */       actualLogger.log(FQCN, Level.DEBUG, sb.toString(), null);
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
/*     */   public void traceExtCall(String callName, long startTimeMS, long endTimeMS, Object elementCountIndicator, Object returnValue, Object... callParams)
/*     */   {
/* 517 */     TraceLevel traceLevel = null;
/* 518 */     Logger traceLogger = getMaximumLog4jLogger();
/* 519 */     if (traceLogger.isDebugEnabled())
/*     */     {
/* 521 */       traceLevel = TraceLevel.Maximum;
/*     */     }
/*     */     else
/*     */     {
/* 525 */       traceLogger = getMediumLog4jLogger();
/* 526 */       if (traceLogger.isDebugEnabled())
/*     */       {
/* 528 */         traceLevel = TraceLevel.Medium;
/*     */       }
/*     */       else
/*     */       {
/* 532 */         traceLogger = getMinimumLog4jLogger();
/* 533 */         if (traceLogger.isDebugEnabled())
/*     */         {
/* 535 */           traceLevel = TraceLevel.Minimum;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 540 */     if (traceLevel != null)
/*     */     {
/* 542 */       long duration = endTimeMS - startTimeMS;
/* 543 */       StringBuilder sb = new StringBuilder(64);
/* 544 */       if (traceLevel == TraceLevel.Minimum)
/*     */       {
/*     */ 
/* 547 */         sb.append("ExtCall ").append(callName);
/* 548 */         sb.append(" dur: ").append(duration).append(" ms.");
/*     */       }
/* 550 */       else if ((traceLevel == TraceLevel.Medium) || (traceLevel == TraceLevel.Maximum))
/*     */       {
/* 552 */         String numElements = "<unknown>";
/* 553 */         if (elementCountIndicator != null)
/*     */         {
/* 555 */           if ((elementCountIndicator instanceof Boolean))
/*     */           {
/* 557 */             if (((Boolean)elementCountIndicator).equals(Boolean.TRUE)) {
/* 558 */               numElements = "<empty>";
/*     */             } else {
/* 560 */               numElements = "<not empty>";
/*     */             }
/*     */           }
/*     */           else {
/* 564 */             numElements = elementCountIndicator.toString();
/*     */           }
/*     */         }
/*     */         
/* 568 */         if (traceLevel == TraceLevel.Medium)
/*     */         {
/*     */ 
/* 571 */           sb.append("ExtCall ").append(callName);
/* 572 */           sb.append(" dur: ").append(duration).append(" ms.");
/* 573 */           sb.append(" #elements: ").append(numElements);
/*     */         }
/* 575 */         else if (traceLevel == TraceLevel.Maximum)
/*     */         {
/*     */ 
/* 578 */           sb.append("ExtCall ").append(callName);
/* 579 */           sb.append(" dur: ").append(duration).append(" ms.");
/* 580 */           sb.append(" #elements: ").append(numElements);
/* 581 */           sb.append(", returnValue: ");appendItem(sb, returnValue);
/* 582 */           if ((callParams != null) && (callParams.length > 0))
/*     */           {
/* 584 */             sb.append(", callParams: ");
/* 585 */             appendParams(sb, callParams);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 590 */       traceLogger.log(FQCN, Level.DEBUG, sb.toString(), null);
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
/*     */   protected void appendParams(StringBuilder sb, Object[] params)
/*     */   {
/* 603 */     boolean isFirst = true;
/* 604 */     for (Object param : params)
/*     */     {
/* 606 */       if (!isFirst)
/*     */       {
/* 608 */         sb.append(", ");
/*     */       }
/*     */       
/* 611 */       appendItem(sb, param);
/* 612 */       isFirst = false;
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
/*     */   protected void appendItem(StringBuilder sb, Object item)
/*     */   {
/* 625 */     sb.append("<");
/* 626 */     if ((item != null) && ((item instanceof String)))
/*     */     {
/* 628 */       sb.append("'").append(item).append("'");
/*     */     }
/* 630 */     else if (item != null)
/*     */     {
/* 632 */       sb.append(item.toString());
/*     */     }
/*     */     else
/*     */     {
/* 636 */       sb.append("null");
/*     */     }
/* 638 */     sb.append(">");
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
/*     */   protected void logMethodNameForTrace(Logger traceLogger, String fqcn, Level log4jLevel, String prefix, String suffix)
/*     */   {
/* 653 */     String methodName = getMethodName(3);
/*     */     
/* 655 */     StringBuilder sb = new StringBuilder(64);
/* 656 */     sb.append(prefix).append(methodName).append("()");
/* 657 */     if (suffix != null) {
/* 658 */       sb.append(suffix);
/*     */     }
/* 660 */     traceLogger.log(fqcn, log4jLevel, sb.toString(), null);
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
/*     */   private String getMethodName(int position)
/*     */   {
/* 674 */     String result = "<unknown>";
/*     */     try
/*     */     {
/* 677 */       StackTraceElement[] elems = new Throwable().getStackTrace();
/* 678 */       StackTraceElement caller = elems[position];
/* 679 */       String fullClassName = caller.getClassName();
/* 680 */       int lastDotPos = fullClassName.lastIndexOf('.');
/*     */       
/* 682 */       StringBuilder sb = new StringBuilder(64);
/* 683 */       sb.append(fullClassName.substring(lastDotPos + 1)).append('.');
/* 684 */       sb.append(caller.getMethodName());
/* 685 */       result = sb.toString();
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException ignored) {}
/*     */     
/* 689 */     return result;
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
/*     */   protected Logger getMaximumLog4jLogger()
/*     */   {
/* 715 */     if (this.maximumActualLogger == null)
/*     */     {
/* 717 */       this.maximumActualLogger = Logger.getLogger(this.maximumLoggerName);
/*     */     }
/* 719 */     return this.maximumActualLogger;
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
/*     */   protected Logger getMediumLog4jLogger()
/*     */   {
/* 732 */     if (this.mediumActualLogger == null)
/*     */     {
/* 734 */       this.mediumActualLogger = Logger.getLogger(this.mediumLoggerName);
/*     */     }
/* 736 */     return this.mediumActualLogger;
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
/*     */   protected Logger getMinimumLog4jLogger()
/*     */   {
/* 749 */     if (this.minimumActualLogger == null)
/*     */     {
/* 751 */       this.minimumActualLogger = Logger.getLogger(this.minimumLoggerName);
/*     */     }
/* 753 */     return this.minimumActualLogger;
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
/*     */   protected Logger getTimerLog4jLogger()
/*     */   {
/* 766 */     if (this.timerActualLogger == null)
/*     */     {
/* 768 */       this.timerActualLogger = Logger.getLogger(this.timerLoggerName);
/*     */     }
/* 770 */     return this.timerActualLogger;
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
/*     */   private String createTraceLoggerName(TraceLevel traceLevel)
/*     */   {
/* 783 */     StringBuilder sb = new StringBuilder();
/* 784 */     sb.append(this.baseLoggerName).append('.');
/* 785 */     sb.append(traceLevel.getLoggerNameSuffix());
/*     */     
/* 787 */     return sb.toString();
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
/*     */   public static enum TraceLevel
/*     */   {
/* 800 */     Timer("timer", "timer"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 805 */     Maximum("maximum", "maximum"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 810 */     Medium("medium", "maximum.medium"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 815 */     Minimum("minimum", "maximum.medium.minimum");
/*     */     
/*     */ 
/*     */     private String label;
/*     */     private String loggerNameSuffix;
/*     */     
/*     */     private TraceLevel(String label, String loggerNameSuffix)
/*     */     {
/* 823 */       this.label = label;
/* 824 */       this.loggerNameSuffix = loggerNameSuffix;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getLabel()
/*     */     {
/* 834 */       return this.label;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getLoggerNameSuffix()
/*     */     {
/* 846 */       return this.loggerNameSuffix;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static TraceLevel fromString(String str)
/*     */     {
/* 860 */       TraceLevel result = null;
/*     */       
/* 862 */       if ((Maximum.label.equalsIgnoreCase(str)) || (Maximum.loggerNameSuffix.equalsIgnoreCase(str)))
/*     */       {
/*     */ 
/* 865 */         result = Maximum;
/*     */       }
/* 867 */       else if ((Medium.label.equalsIgnoreCase(str)) || (Medium.loggerNameSuffix.equalsIgnoreCase(str)))
/*     */       {
/*     */ 
/* 870 */         result = Medium;
/*     */       }
/* 872 */       else if ((Minimum.label.equalsIgnoreCase(str)) || (Minimum.loggerNameSuffix.equalsIgnoreCase(str)))
/*     */       {
/*     */ 
/* 875 */         result = Minimum;
/*     */       }
/* 877 */       else if ((Timer.label.equalsIgnoreCase(str)) || (Timer.loggerNameSuffix.equalsIgnoreCase(str)))
/*     */       {
/*     */ 
/* 880 */         result = Timer;
/*     */       }
/*     */       
/* 883 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\logtrace\BaseTracer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */