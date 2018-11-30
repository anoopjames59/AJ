/*     */ package com.ibm.ier.plugin.nls;
/*     */ 
/*     */ import com.ibm.ier.logtrace.BaseLogger.Severity;
/*     */ import com.ibm.ier.logtrace.ResourceBundleService;
/*     */ import com.ibm.jarm.api.util.RMUserContext;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
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
/*     */ public class MessageResources
/*     */ {
/*     */   public static final String IBM_PREFIX = "FNR";
/*     */   public static final String SWG_ID = "RS";
/*     */   public static final String MESSAGE_ID_PREFIX = "FNRRS";
/*  55 */   public static final char MESSAGE_ID_SUFFIX = BaseLogger.Severity.Error.getChar();
/*     */   private static final String RESOURCE_BUNDLE_BASENAME = "resources/IERApplicationPluginResources";
/*     */   private static final String DOT = ".";
/*     */   
/*     */   public static enum MessageCodeType
/*     */   {
/*  61 */     ID("id"), 
/*  62 */     EXPLANATION("explanation"), 
/*  63 */     ADMIN_RESPONSE("adminResponse"), 
/*  64 */     USER_RESPONSE("userResponse"), 
/*  65 */     USE_CASE("useCase");
/*     */     
/*     */     private String value;
/*     */     
/*     */     private MessageCodeType(String value) {
/*  70 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  74 */       return this.value;
/*     */     }
/*     */     
/*     */     public String getValue() {
/*  78 */       return this.value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getLocalizedMessage(MessageCode code, Object... params)
/*     */   {
/*  85 */     return formatMessage(null, code.getMessageKey(), params);
/*     */   }
/*     */   
/*     */   public static String getLocalizedMessage(Locale locale, MessageCode code, Object... params)
/*     */   {
/*  90 */     return formatMessage(locale, code.getMessageKey(), params);
/*     */   }
/*     */   
/*     */   public static String getLocalizedMessage(MessageCode code, MessageCodeType type, Object... params)
/*     */   {
/*  95 */     StringBuilder messageKey = new StringBuilder(code.getMessageKey());
/*  96 */     messageKey.append(".");
/*  97 */     messageKey.append(type.toString());
/*     */     
/*  99 */     return formatMessage(null, messageKey.toString(), params);
/*     */   }
/*     */   
/*     */   public static String getLocalizedMessage(String messageKey, Object... params)
/*     */   {
/* 104 */     return formatMessage(null, messageKey, params);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatMessage(Locale locale, String messageKey, Object... params)
/*     */   {
/* 112 */     String result = null;
/* 113 */     if (locale == null) {
/* 114 */       locale = getCurrentUserLocale();
/*     */     }
/* 116 */     ResourceBundle rb = ResourceBundleService.get("resources/IERApplicationPluginResources", locale);
/*     */     try
/*     */     {
/* 119 */       String msgPattern = rb.getString(messageKey);
/* 120 */       if (msgPattern != null)
/*     */       {
/* 122 */         MessageFormat mf = new MessageFormat(msgPattern, locale);
/*     */         
/* 124 */         StringBuffer sb = new StringBuffer(64);
/*     */         
/*     */ 
/* 127 */         FieldPosition pos = null;
/* 128 */         result = mf.format(params, sb, pos).toString();
/*     */       }
/*     */       
/*     */ 
/* 132 */       return "Unable to find exception message string for message key: '" + messageKey + "'";
/*     */     }
/*     */     catch (MissingResourceException exp) {}
/*     */     
/*     */ 
/* 137 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Locale getCurrentUserLocale()
/*     */   {
/* 145 */     return RMUserContext.get().getLocale();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\nls\MessageResources.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */