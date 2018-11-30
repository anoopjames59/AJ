/*     */ package com.ibm.ier.plugin.nls;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IERUIRuntimeException
/*     */   extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  30 */   private MessageCode messageCode = null;
/*     */   
/*  32 */   private String messageKey = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IERUIRuntimeException createUIRuntimeException(MessageCode code, Object... params)
/*     */   {
/*  45 */     String message = MessageResources.getLocalizedMessage(code, params);
/*     */     try {
/*  47 */       Logger.logError(null, "createUIRuntimeException", message);
/*     */     }
/*     */     catch (Exception ignored) {}
/*  50 */     return new IERUIRuntimeException(code, message, null);
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
/*     */   public static IERUIRuntimeException createUIRuntimeException(String key, Object... params)
/*     */   {
/*  64 */     String message = MessageResources.getLocalizedMessage(key, params);
/*     */     try {
/*  66 */       Logger.logError(null, "createUIRuntimeException", message);
/*     */     }
/*     */     catch (Exception ignored) {}
/*  69 */     return new IERUIRuntimeException(key, message, null);
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
/*     */   public static IERUIRuntimeException createUIRuntimeException(Throwable cause, MessageCode code, Object... params)
/*     */   {
/*  84 */     String message = MessageResources.getLocalizedMessage(code, params);
/*     */     try {
/*  86 */       System.out.println(message);
/*     */     }
/*     */     catch (Exception ignored) {}
/*  89 */     return new IERUIRuntimeException(code, message, cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageCode getMessageCode()
/*     */   {
/*  99 */     return this.messageCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessageKey()
/*     */   {
/* 109 */     return this.messageKey;
/*     */   }
/*     */   
/*     */   private IERUIRuntimeException(MessageCode messageCode, String message)
/*     */   {
/* 114 */     super(message);
/* 115 */     this.messageCode = messageCode;
/*     */   }
/*     */   
/*     */   private IERUIRuntimeException(MessageCode messageCode, String message, Throwable cause)
/*     */   {
/* 120 */     super(message, cause);
/* 121 */     this.messageCode = messageCode;
/*     */   }
/*     */   
/*     */   private IERUIRuntimeException(String key, String message, Throwable cause)
/*     */   {
/* 126 */     super(message, cause);
/* 127 */     this.messageKey = key;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\nls\IERUIRuntimeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */