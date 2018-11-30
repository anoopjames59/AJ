/*     */ package com.ibm.jarm.api.exception;
/*     */ 
/*     */ import com.ibm.jarm.api.util.JarmLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IERWorkflowException
/*     */   extends RMRuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static IERWorkflowException createIERWorkflowException(RMErrorCode code, Object... params)
/*     */   {
/*     */     try
/*     */     {
/*  37 */       Logger.error(RMRuntimeException.MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/*  40 */     RMRuntimeException.MessageInfoImpl messageInfo = new RMRuntimeException.MessageInfoImpl(code, params);
/*  41 */     return new IERWorkflowException(code, messageInfo, null);
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
/*     */   public static IERWorkflowException createIERWorkflowException(RMErrorCode code, RMErrorStack errorStack, Object... params)
/*     */   {
/*     */     try
/*     */     {
/*  57 */       Logger.error(RMRuntimeException.MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/*  60 */     RMRuntimeException.MessageInfoImpl messageInfo = new RMRuntimeException.MessageInfoImpl(code, params);
/*  61 */     return new IERWorkflowException(code, messageInfo, errorStack);
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
/*     */   public static IERWorkflowException createIERWorkflowException(Throwable cause, RMErrorCode code, Object... params)
/*     */   {
/*     */     try
/*     */     {
/*  76 */       Logger.error(RMRuntimeException.MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/*  79 */     RMRuntimeException.MessageInfoImpl messageInfo = new RMRuntimeException.MessageInfoImpl(code, params);
/*  80 */     return new IERWorkflowException(code, messageInfo, cause, null);
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
/*     */   public static IERWorkflowException createIERWorkflowException(Throwable cause, RMErrorCode code, RMErrorStack errorStack, Object... params)
/*     */   {
/*     */     try
/*     */     {
/*  97 */       Logger.error(RMRuntimeException.MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 100 */     RMRuntimeException.MessageInfoImpl messageInfo = new RMRuntimeException.MessageInfoImpl(code, params);
/* 101 */     return new IERWorkflowException(code, messageInfo, cause, errorStack);
/*     */   }
/*     */   
/*     */ 
/*     */   private IERWorkflowException(RMErrorCode errorCode, RMRuntimeException.MessageInfoImpl messageInfo, RMErrorStack errorStack)
/*     */   {
/* 107 */     super(errorCode, messageInfo, errorStack);
/*     */   }
/*     */   
/*     */   private IERWorkflowException(RMErrorCode errorCode, RMRuntimeException.MessageInfoImpl messageInfo, Throwable cause, RMErrorStack errorStack)
/*     */   {
/* 112 */     super(errorCode, messageInfo, cause, errorStack);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\exception\IERWorkflowException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */