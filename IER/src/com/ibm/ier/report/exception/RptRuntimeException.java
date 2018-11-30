/*     */ package com.ibm.ier.report.exception;
/*     */ 
/*     */ import com.ibm.ier.logtrace.BaseLogger.Severity;
/*     */ import com.ibm.ier.logtrace.ResourceBundleService;
/*     */ import com.ibm.ier.report.util.RptLogger;
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
/*     */ public class RptRuntimeException
/*     */   extends RuntimeException
/*     */ {
/*     */   public static final String IBM_PREFIX = "FNR";
/*     */   public static final String SWG_ID = "RE";
/*     */   public static final String MESSAGE_ID_PREFIX = "FNRRE";
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
/*     */   static final String RESOURCE_BUNDLE_BASENAME = "resources/RptResources";
/*     */   
/*     */ 
/*     */   private static final String LOGGER_NAME = "com.ibm.ier.report.Error";
/*     */   
/*     */ 
/*  87 */   protected static final RptLogger Logger = RptLogger.getRptLogger("com.ibm.ier.report.Error");
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*  91 */   private static final SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/*     */   
/*     */ 
/*     */ 
/*     */   private RptErrorCode errorCode;
/*     */   
/*     */ 
/*     */ 
/*     */   private MessageInfoImpl messageInfo;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RptRuntimeException createRptRuntimeException(RptErrorCode code, Object... params)
/*     */   {
/*     */     try
/*     */     {
/* 108 */       Logger.error(MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 111 */     MessageInfoImpl messageInfo = new MessageInfoImpl(code, params);
/* 112 */     return new RptRuntimeException(code, messageInfo, null);
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
/*     */   public static RptRuntimeException createRptRuntimeException(Throwable cause, RptErrorCode code, Object... params)
/*     */   {
/*     */     try
/*     */     {
/* 127 */       Logger.error(MessageInfoImpl.getMessageKey(code), params);
/*     */     }
/*     */     catch (Exception ignored) {}
/* 130 */     MessageInfoImpl messageInfo = new MessageInfoImpl(code, params);
/* 131 */     return new RptRuntimeException(code, messageInfo, cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RptErrorCode getErrorCode()
/*     */   {
/* 141 */     return this.errorCode;
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
/* 153 */     return this.messageInfo;
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
/* 166 */     String result = "<null>";
/* 167 */     if (dateValue != null) {
/* 168 */       result = DateFormatter.format(dateValue);
/*     */     }
/* 170 */     return result;
/*     */   }
/*     */   
/*     */   protected RptRuntimeException(RptErrorCode errorCode, MessageInfoImpl messageInfo)
/*     */   {
/* 175 */     super(messageInfo.getFormattedMessageWithPrefix());
/* 176 */     this.errorCode = errorCode;
/* 177 */     this.messageInfo = messageInfo;
/*     */   }
/*     */   
/*     */   protected RptRuntimeException(RptErrorCode errorCode, MessageInfoImpl messageInfo, Throwable cause)
/*     */   {
/* 182 */     super(messageInfo.getFormattedMessageWithPrefix(), cause);
/* 183 */     this.errorCode = errorCode;
/* 184 */     this.messageInfo = messageInfo;
/*     */   }
/*     */   
/*     */   private static Locale getCurrentUserLocale()
/*     */   {
/* 189 */     return RMUserContext.get().getLocale();
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
/*     */     private RptErrorCode errorCode;
/*     */     
/*     */     private String formattedMsg;
/*     */     
/*     */     static String getMessageKey(RptErrorCode errorCode)
/*     */     {
/* 208 */       return errorCode.getMessageId() + ".message";
/*     */     }
/*     */     
/*     */     MessageInfoImpl(RptErrorCode errorCode, Object... params)
/*     */     {
/* 213 */       this.errorCode = errorCode;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 219 */       Locale locale = RptRuntimeException.access$000();
/* 220 */       String msgPattern = getUnformattedMessage(locale);
/* 221 */       if (msgPattern != null)
/*     */       {
/* 223 */         MessageFormat mf = new MessageFormat(msgPattern, locale);
/* 224 */         StringBuffer sb = new StringBuffer(64);
/* 225 */         FieldPosition pos = null;
/* 226 */         this.formattedMsg = mf.format(params, sb, pos).toString();
/*     */       }
/*     */       else
/*     */       {
/* 230 */         this.formattedMsg = ("Unable to find exception message string for ID: '" + errorCode.getMessageId() + "'");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public RptErrorCode getErrorCode()
/*     */     {
/* 239 */       return this.errorCode;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getFormattedMessage()
/*     */     {
/* 247 */       return this.formattedMsg;
/*     */     }
/*     */     
/*     */     String getFormattedMessageWithPrefix()
/*     */     {
/* 252 */       StringBuilder sb = new StringBuilder();
/* 253 */       sb.append(this.errorCode.getMessageId()).append(RptRuntimeException.MESSAGE_ID_SUFFIX).append(": ");
/* 254 */       sb.append(getFormattedMessage());
/* 255 */       return sb.toString();
/*     */     }
/*     */     
/*     */     private String getUnformattedMessage(Locale locale)
/*     */     {
/* 260 */       ResourceBundle rb = ResourceBundleService.get("resources/RptResources", locale);
/* 261 */       String msgKey = this.errorCode.getMessageId() + ".message";
/*     */       
/* 263 */       return rb.getString(msgKey);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getExplanation()
/*     */     {
/* 271 */       Locale locale = RptRuntimeException.access$000();
/* 272 */       ResourceBundle rb = ResourceBundleService.get("resources/RptResources", locale);
/* 273 */       String msgKey = this.errorCode.getMessageId() + ".explanation";
/*     */       
/* 275 */       return rb.getString(msgKey);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getAction()
/*     */     {
/* 283 */       Locale locale = RptRuntimeException.access$000();
/* 284 */       ResourceBundle rb = ResourceBundleService.get("resources/RptResources", locale);
/* 285 */       String msgKey = this.errorCode.getMessageId() + ".action";
/*     */       
/* 287 */       return rb.getString(msgKey);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\exception\RptRuntimeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */