/*     */ package com.ibm.jarm.api.exception;
/*     */ 
/*     */ import com.ibm.ier.logtrace.BaseLogger.Severity;
/*     */ import com.ibm.ier.logtrace.ResourceBundleService;
/*     */ import com.ibm.jarm.api.util.JarmLogger;
/*     */ import com.ibm.jarm.api.util.RMUserContext;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RMRuntimeException
/*     */   extends RuntimeException
/*     */ {
/*     */   public static final String IBM_PREFIX = "FNR";
/*     */   public static final String SWG_ID = "RS";
/*     */   public static final String MESSAGE_ID_PREFIX = "FNRRS";
/*  71 */   public static final char MESSAGE_ID_SUFFIX = BaseLogger.Severity.Error.getChar();
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String W3C_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
/*     */   
/*     */ 
/*     */   public static final String W3C_DATE_FORMAT_WITH_ZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
/*     */   
/*     */ 
/*     */   static final String RESOURCE_BUNDLE_BASENAME = "resources/JarmResources";
/*     */   
/*     */ 
/*     */   private static final String LOGGER_NAME = "com.ibm.jarm.api.Error";
/*     */   
/*     */ 
/*  87 */   protected static final JarmLogger Logger = JarmLogger.getJarmLogger("com.ibm.jarm.api.Error");
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*  91 */   private static final SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/*     */   
/*     */ 
/*     */ 
/*     */   private RMErrorCode errorCode;
/*     */   
/*     */ 
/*     */   private RMErrorStack errorStack;
/*     */   
/*     */ 
/*     */   private MessageInfoImpl messageInfo;
/*     */   
/*     */ 
/*     */ 
/*     */   public static RMRuntimeException createRMRuntimeException(RMErrorCode code, Object... params)
/*     */   {
/*     */     try
/*     */     {
/* 109 */       Logger.error(MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 112 */     MessageInfoImpl messageInfo = new MessageInfoImpl(code, params);
/* 113 */     return new RMRuntimeException(code, messageInfo, null);
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
/*     */   public static RMRuntimeException createRMRuntimeException(RMErrorCode code, RMErrorStack errorStack, Object... params)
/*     */   {
/*     */     try
/*     */     {
/* 129 */       Logger.error(MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 132 */     MessageInfoImpl messageInfo = new MessageInfoImpl(code, params);
/* 133 */     return new RMRuntimeException(code, messageInfo, errorStack);
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
/*     */   public static RMRuntimeException createRMRuntimeException(Throwable cause, RMErrorCode code, Object... params)
/*     */   {
/*     */     try
/*     */     {
/* 148 */       Logger.error(MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 151 */     MessageInfoImpl messageInfo = new MessageInfoImpl(code, params);
/* 152 */     return new RMRuntimeException(code, messageInfo, cause, null);
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
/*     */   public static RMRuntimeException createRMRuntimeException(Throwable cause, RMErrorCode code, RMErrorStack errorStack, Object... params)
/*     */   {
/*     */     try
/*     */     {
/* 169 */       Logger.error(MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 172 */     MessageInfoImpl messageInfo = new MessageInfoImpl(code, params);
/* 173 */     return new RMRuntimeException(code, messageInfo, cause, errorStack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMErrorCode getErrorCode()
/*     */   {
/* 183 */     return this.errorCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMErrorStack getErrorStack()
/*     */   {
/* 194 */     return this.errorStack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageInfo getMessageInfo()
/*     */   {
/* 206 */     return this.messageInfo;
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
/*     */   public static synchronized String formatDate(Date dateValue)
/*     */   {
/* 219 */     String result = "<null>";
/* 220 */     if (dateValue != null) {
/* 221 */       result = DateFormatter.format(dateValue);
/*     */     }
/* 223 */     return result;
/*     */   }
/*     */   
/*     */   protected RMRuntimeException(RMErrorCode errorCode, MessageInfoImpl messageInfo, RMErrorStack errorStack)
/*     */   {
/* 228 */     super(messageInfo.getFormattedMessageWithPrefix());
/* 229 */     this.errorCode = errorCode;
/* 230 */     this.errorStack = errorStack;
/* 231 */     this.messageInfo = messageInfo;
/*     */   }
/*     */   
/*     */   protected RMRuntimeException(RMErrorCode errorCode, MessageInfoImpl messageInfo, Throwable cause, RMErrorStack errorStack)
/*     */   {
/* 236 */     super(messageInfo.getFormattedMessageWithPrefix(), cause);
/* 237 */     this.errorCode = errorCode;
/* 238 */     this.errorStack = errorStack;
/* 239 */     this.messageInfo = messageInfo;
/*     */   }
/*     */   
/*     */   private static Locale getCurrentUserLocale()
/*     */   {
/* 244 */     return RMUserContext.get().getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class MessageInfoImpl
/*     */     implements MessageInfo
/*     */   {
/*     */     static final String MESSAGE_KEY_SUFFIX = ".message";
/*     */     
/*     */     static final String EXPLANATION_KEY_SUFFIX = ".explanation";
/*     */     
/*     */     static final String ACTION_KEY_SUFFIX = ".action";
/*     */     
/*     */     private RMErrorCode errorCode;
/*     */     
/*     */     private String formattedMsg;
/*     */     
/*     */ 
/*     */     static String getMessageKey(RMErrorCode errorCode)
/*     */     {
/* 265 */       return errorCode.getMessageId() + ".message";
/*     */     }
/*     */     
/*     */     MessageInfoImpl(RMErrorCode errorCode, Object... params)
/*     */     {
/* 270 */       this.errorCode = errorCode;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 276 */       Locale locale = RMRuntimeException.access$000();
/* 277 */       String msgPattern = getUnformattedMessage(locale);
/* 278 */       if (msgPattern != null)
/*     */       {
/* 280 */         MessageFormat mf = new MessageFormat(msgPattern, locale);
/* 281 */         StringBuffer sb = new StringBuffer(64);
/* 282 */         FieldPosition pos = null;
/* 283 */         this.formattedMsg = mf.format(params, sb, pos).toString();
/*     */       }
/*     */       else
/*     */       {
/* 287 */         this.formattedMsg = ("Unable to find exception message string for ID: '" + errorCode.getMessageId() + "'");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public RMErrorCode getErrorCode()
/*     */     {
/* 296 */       return this.errorCode;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getFormattedMessage()
/*     */     {
/* 304 */       return this.formattedMsg;
/*     */     }
/*     */     
/*     */     String getFormattedMessageWithPrefix()
/*     */     {
/* 309 */       StringBuilder sb = new StringBuilder();
/* 310 */       sb.append(this.errorCode.getMessageId()).append(RMRuntimeException.MESSAGE_ID_SUFFIX).append(": ");
/* 311 */       sb.append(getFormattedMessage());
/* 312 */       return sb.toString();
/*     */     }
/*     */     
/*     */     private String getUnformattedMessage(Locale locale)
/*     */     {
/* 317 */       ResourceBundle rb = ResourceBundleService.get("resources/JarmResources", locale);
/* 318 */       String msgKey = this.errorCode.getMessageId() + ".message";
/*     */       
/* 320 */       return rb.getString(msgKey);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getExplanation()
/*     */     {
/* 328 */       Locale locale = RMRuntimeException.access$000();
/* 329 */       ResourceBundle rb = ResourceBundleService.get("resources/JarmResources", locale);
/* 330 */       String msgKey = this.errorCode.getMessageId() + ".explanation";
/*     */       
/* 332 */       String explanationStr = rb.getString(msgKey);
/* 333 */       if (explanationStr != null)
/*     */       {
/*     */ 
/* 336 */         explanationStr = MessageFormat.format(explanationStr, (Object[])null);
/*     */       }
/*     */       
/* 339 */       return explanationStr;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getAction()
/*     */     {
/* 347 */       Locale locale = RMRuntimeException.access$000();
/* 348 */       ResourceBundle rb = ResourceBundleService.get("resources/JarmResources", locale);
/* 349 */       String msgKey = this.errorCode.getMessageId() + ".action";
/*     */       
/* 351 */       String actionStr = rb.getString(msgKey);
/* 352 */       if (actionStr != null)
/*     */       {
/*     */ 
/* 355 */         actionStr = MessageFormat.format(actionStr, (Object[])null);
/*     */       }
/*     */       
/* 358 */       return actionStr;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\exception\RMRuntimeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */