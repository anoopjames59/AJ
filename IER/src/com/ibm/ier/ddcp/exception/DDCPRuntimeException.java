/*     */ package com.ibm.ier.ddcp.exception;
/*     */ 
/*     */ import com.filenet.api.util.UserContext;
/*     */ import com.ibm.ier.ddcp.util.DDCPLogger;
/*     */ import com.ibm.ier.logtrace.BaseLogger.Severity;
/*     */ import com.ibm.ier.logtrace.ResourceBundleService;
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
/*     */ public class DDCPRuntimeException
/*     */   extends RuntimeException
/*     */ {
/*  44 */   public static final char MESSAGE_ID_SUFFIX = BaseLogger.Severity.Error.getChar();
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String W3C_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
/*     */   
/*     */ 
/*     */   public static final String W3C_DATE_FORMAT_WITH_ZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
/*     */   
/*     */ 
/*     */   public static final String DEFAULT_EXCEPTION_BUNDLE_BASENAME = "resources/DDCPErrorResources";
/*     */   
/*     */ 
/*     */   public static final String DDCP_ERROR_LOGGER_NAME = "com.ibm.ier.DDCP.Error";
/*     */   
/*     */ 
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*  63 */   private static final SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private DDCPErrorCode errorCode;
/*     */   
/*     */ 
/*     */ 
/*     */   private MessageInfoImpl messageInfo;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DDCPRuntimeException createDDCPRuntimeException(DDCPErrorCode code, Object... params)
/*     */   {
/*  79 */     DDCPLogger ddcpErrLogger = DDCPLogger.getDDCPLogger("com.ibm.ier.DDCP.Error", "resources/DDCPErrorResources");
/*  80 */     try { ddcpErrLogger.error(code.getMessageId(), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/*  83 */     MessageInfoImpl messageInfoImpl = new MessageInfoImpl(code, params);
/*  84 */     return new DDCPRuntimeException(code, messageInfoImpl);
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
/*     */   public static DDCPRuntimeException createDDCPRuntimeException(Throwable cause, DDCPErrorCode code, Object... params)
/*     */   {
/* 100 */     DDCPLogger ddcpErrLogger = DDCPLogger.getDDCPLogger("com.ibm.ier.DDCP.Error", "resources/DDCPErrorResources");
/* 101 */     try { ddcpErrLogger.error(code.getMessageId(), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 104 */     MessageInfoImpl messageInfoImpl = new MessageInfoImpl(code, params);
/* 105 */     return new DDCPRuntimeException(code, messageInfoImpl, cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DDCPErrorCode getErrorCode()
/*     */   {
/* 115 */     return this.errorCode;
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
/* 127 */     return this.messageInfo;
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
/* 140 */     String result = "<null>";
/* 141 */     if (dateValue != null) {
/* 142 */       result = mDateFormatter.format(dateValue);
/*     */     }
/* 144 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Locale getCurrentUserLocale()
/*     */   {
/* 153 */     UserContext userCtx = UserContext.get();
/*     */     
/* 155 */     Locale resultLocale = Locale.getDefault();
/*     */     
/* 157 */     if (userCtx != null)
/*     */     {
/* 159 */       resultLocale = userCtx.getLocale();
/*     */     }
/*     */     
/* 162 */     return resultLocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DDCPRuntimeException(DDCPErrorCode errorCode, MessageInfoImpl messageInfoImpl)
/*     */   {
/* 172 */     super(messageInfoImpl.getFormattedMessageWithPrefix());
/* 173 */     this.errorCode = errorCode;
/* 174 */     this.messageInfo = messageInfoImpl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DDCPRuntimeException(DDCPErrorCode errorCode, MessageInfoImpl messageInfoImpl, Throwable cause)
/*     */   {
/* 185 */     super(messageInfoImpl.getFormattedMessageWithPrefix(), cause);
/* 186 */     this.errorCode = errorCode;
/* 187 */     this.messageInfo = messageInfoImpl;
/*     */   }
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
/*     */     private DDCPErrorCode errorCode;
/*     */     private String formattedMsg;
/*     */     
/*     */     static String getMessageKey(DDCPErrorCode errorCode)
/*     */     {
/* 205 */       return errorCode.getMessageId() + ".message";
/*     */     }
/*     */     
/*     */     MessageInfoImpl(DDCPErrorCode errorCode, Object... params)
/*     */     {
/* 210 */       this.errorCode = errorCode;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */       Locale locale = DDCPRuntimeException.access$000();
/* 217 */       String msgPattern = getUnformattedMessage(locale);
/* 218 */       if (msgPattern != null)
/*     */       {
/* 220 */         MessageFormat mf = new MessageFormat(msgPattern, locale);
/* 221 */         StringBuffer sb = new StringBuffer(64);
/* 222 */         FieldPosition pos = null;
/* 223 */         this.formattedMsg = mf.format(params, sb, pos).toString();
/*     */       }
/*     */       else
/*     */       {
/* 227 */         this.formattedMsg = ("Unable to find exception message string for ID: '" + errorCode.getMessageId() + "'");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public DDCPErrorCode getErrorCode()
/*     */     {
/* 236 */       return this.errorCode;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getFormattedMessage()
/*     */     {
/* 244 */       return this.formattedMsg;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     String getFormattedMessageWithPrefix()
/*     */     {
/* 253 */       StringBuilder sb = new StringBuilder();
/* 254 */       sb.append(this.errorCode.getMessageId()).append(DDCPRuntimeException.MESSAGE_ID_SUFFIX).append(": ");
/* 255 */       sb.append(getFormattedMessage());
/* 256 */       return sb.toString();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private String getUnformattedMessage(Locale locale)
/*     */     {
/* 266 */       ResourceBundle rb = ResourceBundleService.get("resources/DDCPErrorResources", locale);
/* 267 */       String msgKey = this.errorCode.getMessageId() + ".message";
/*     */       
/* 269 */       return rb.getString(msgKey);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getExplanation()
/*     */     {
/* 277 */       Locale locale = DDCPRuntimeException.access$000();
/* 278 */       ResourceBundle rb = ResourceBundleService.get("resources/DDCPErrorResources", locale);
/* 279 */       String msgKey = this.errorCode.getMessageId() + ".explanation";
/*     */       
/* 281 */       String explanationStr = rb.getString(msgKey);
/* 282 */       if (explanationStr != null)
/*     */       {
/*     */ 
/* 285 */         explanationStr = MessageFormat.format(explanationStr, (Object[])null);
/*     */       }
/*     */       
/* 288 */       return explanationStr;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getAction()
/*     */     {
/* 296 */       Locale locale = DDCPRuntimeException.access$000();
/* 297 */       ResourceBundle rb = ResourceBundleService.get("resources/DDCPErrorResources", locale);
/* 298 */       String msgKey = this.errorCode.getMessageId() + ".action";
/*     */       
/* 300 */       String actionStr = rb.getString(msgKey);
/* 301 */       if (actionStr != null)
/*     */       {
/*     */ 
/* 304 */         actionStr = MessageFormat.format(actionStr, (Object[])null);
/*     */       }
/*     */       
/* 307 */       return actionStr;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\exception\DDCPRuntimeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */