/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.ecm.serviceability.Logger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageUtil
/*     */ {
/*     */   private static final String PARM_RESOURCE_BUNDLE_NAME = "com.ibm.ecm.nls.ServicesMessages";
/*     */   private static final String I18N_PARMS = "I18N_PARMS";
/*     */   
/*     */   private static ResourceBundle getResourceBundle(Locale locale)
/*     */   {
/*  68 */     ClassLoader loader = MessageUtil.class.getClassLoader();
/*  69 */     return ResourceBundle.getBundle("com.ibm.ecm.nls.ServicesMessages", locale, loader);
/*     */   }
/*     */   
/*     */   private static String replace(String string, String token, String replacementText) {
/*  73 */     StringBuffer resource = new StringBuffer().append(string);
/*  74 */     String str = resource.toString();
/*  75 */     int start = str.indexOf(token, 0);
/*  76 */     while (start != -1) {
/*  77 */       resource.replace(start, start + token.length(), replacementText);
/*  78 */       start = str.indexOf(token, start + 1);
/*     */     }
/*  80 */     return resource.toString();
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
/*     */   public static String getMessage(PageContext page, String messageKey)
/*     */   {
/* 110 */     return getMessage(page.getRequest().getLocale(), messageKey);
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
/*     */   public static String getMessageWithToken(PageContext page, String messageKey, String text)
/*     */   {
/* 142 */     ArrayList list = new ArrayList();
/* 143 */     list.add(text);
/* 144 */     return getMessagePrivateArrayList(page.getRequest(), page.getRequest().getLocale(), messageKey, list);
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
/*     */   public static String getMessageWithTokens(PageContext page, String messageKey)
/*     */   {
/* 176 */     return getMessagePrivateArrayList(page.getRequest(), page.getRequest().getLocale(), messageKey, getList(page));
/*     */   }
/*     */   
/*     */   private static String getMessagePrivateArrayList(ServletRequest request, Locale locale, String messageKey, ArrayList tokenValues)
/*     */   {
/* 181 */     String methodName = "getMessagePrivateArrayList";
/* 182 */     String nlsMessage = null;
/*     */     try {
/* 184 */       nlsMessage = getResourceBundle(locale).getString(messageKey);
/*     */     } catch (Exception e) {
/* 186 */       Logger.logWarning(MessageUtil.class, methodName, request, messageKey + " not found");
/* 187 */       return messageKey + " not found";
/*     */     }
/*     */     
/* 190 */     if (nlsMessage == null) {
/* 191 */       return messageKey + " not found";
/*     */     }
/* 193 */     for (int i = 0; i < tokenValues.size(); i++) {
/* 194 */       nlsMessage = replace(nlsMessage, "{" + i + "}", tokenValues.get(i).toString());
/*     */     }
/*     */     
/* 197 */     return nlsMessage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ArrayList getList(PageContext page)
/*     */   {
/* 205 */     ArrayList list = (ArrayList)page.getAttribute("I18N_PARMS");
/* 206 */     if (list == null)
/* 207 */       list = new ArrayList();
/* 208 */     return list;
/*     */   }
/*     */   
/*     */   private static void setList(PageContext page, ArrayList list) {
/* 212 */     if (list != null) {
/* 213 */       page.setAttribute("I18N_PARMS", list);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void clearList(PageContext page) {
/* 218 */     ArrayList list = (ArrayList)page.getAttribute("I18N_PARMS");
/* 219 */     if (list != null) {
/* 220 */       list.clear();
/* 221 */       page.removeAttribute("I18N_PARMS");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getParm(PageContext page, int i)
/*     */   {
/* 230 */     ArrayList list = getList(page);
/* 231 */     return (String)list.get(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setParm(PageContext page, int i, Object parm)
/*     */   {
/* 238 */     ArrayList list = getList(page);
/* 239 */     if (i == 0) {
/* 240 */       clearList(page);
/*     */     }
/* 242 */     list.add(i, parm.toString());
/* 243 */     setList(page, list);
/*     */   }
/*     */   
/*     */   public static void setClear(PageContext page)
/*     */   {
/* 248 */     ArrayList list = getList(page);
/* 249 */     clearList(page);
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
/*     */   public static boolean containsKey(HttpServletRequest request, String messageKey)
/*     */   {
/*     */     try
/*     */     {
/* 281 */       ResourceBundle bundle = getResourceBundle(request.getLocale());
/* 282 */       return bundle.containsKey(messageKey);
/*     */     }
/*     */     catch (Exception e) {}
/* 285 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getMessage(HttpServletRequest request, String messageKey)
/*     */   {
/* 291 */     return getMessage(request.getLocale(), messageKey);
/*     */   }
/*     */   
/*     */   public static String getMessage(HttpServletRequest request, String messageKey, String text) {
/* 295 */     ArrayList al = new ArrayList();
/* 296 */     al.add(text);
/* 297 */     return getMessage(request.getLocale(), messageKey, al.toArray());
/*     */   }
/*     */   
/*     */   public static String getMessage(HttpServletRequest request, String messageKey, ArrayList list) {
/* 301 */     return getMessage(request.getLocale(), messageKey, list.toArray());
/*     */   }
/*     */   
/*     */   public static String getMessage(HttpServletRequest request, String messageKey, Object[] tokenValues) {
/* 305 */     return getMessage(request.getLocale(), messageKey, tokenValues);
/*     */   }
/*     */   
/*     */   public static ErrorMessage getErrorMessage(HttpServletRequest request, String messageKey, Object[] tokenValues) {
/* 309 */     ErrorMessage errorMessage = new ErrorMessage(null);
/* 310 */     Locale locale = request.getLocale();
/*     */     
/* 312 */     errorMessage.id = getMessage(locale, messageKey + ".id");
/* 313 */     errorMessage.message = getMessage(locale, messageKey, tokenValues);
/* 314 */     errorMessage.explanation = getMessage(locale, messageKey + ".explanation", tokenValues);
/* 315 */     errorMessage.userResponse = getMessage(locale, messageKey + ".userResponse", tokenValues);
/* 316 */     errorMessage.adminResponse = getMessage(locale, messageKey + ".adminResponse", tokenValues);
/*     */     
/* 318 */     return errorMessage;
/*     */   }
/*     */   
/*     */   public static String getMessage(Locale locale, String messageKey)
/*     */   {
/* 323 */     ArrayList al = new ArrayList();
/* 324 */     return getMessage(locale, messageKey, al.toArray());
/*     */   }
/*     */   
/*     */   private static String getMessage(Locale locale, String messageKey, String text) {
/* 328 */     ArrayList al = new ArrayList();
/* 329 */     al.add(text);
/* 330 */     return getMessage(locale, messageKey, al.toArray());
/*     */   }
/*     */   
/*     */   private static String getMessage(Locale locale, String messageKey, Object[] tokenValues) {
/* 334 */     String nlsMessage = null;
/*     */     try {
/* 336 */       ResourceBundle bundle = getResourceBundle(locale);
/* 337 */       nlsMessage = bundle.getString(messageKey);
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 341 */     if (nlsMessage == null) {
/* 342 */       nlsMessage = messageKey;
/*     */     }
/* 344 */     if (tokenValues != null) {
/* 345 */       for (int i = 0; i < tokenValues.length; i++) {
/* 346 */         nlsMessage = replace(nlsMessage, "{" + i + "}", tokenValues[i].toString());
/*     */       }
/*     */     }
/* 349 */     return nlsMessage;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class ErrorMessage
/*     */   {
/*     */     String id;
/*     */     String message;
/*     */     String explanation;
/*     */     String userResponse;
/*     */     String adminResponse;
/*     */     
/*     */     public String getId()
/*     */     {
/* 363 */       return this.id;
/*     */     }
/*     */     
/*     */     public String getMessage() {
/* 367 */       return this.message;
/*     */     }
/*     */     
/*     */     public String getExplanation() {
/* 371 */       return this.explanation;
/*     */     }
/*     */     
/*     */     public String getUserResponse() {
/* 375 */       return this.userResponse;
/*     */     }
/*     */     
/*     */     public String getAdminResponse() {
/* 379 */       return this.adminResponse;
/*     */     }
/*     */     
/*     */     public String getFormatted() {
/* 383 */       return getFormatted("\n");
/*     */     }
/*     */     
/*     */     public String getHtmlFormatted() {
/* 387 */       return getFormatted("<br>\n");
/*     */     }
/*     */     
/*     */     private String getFormatted(String lineSeparator) {
/* 391 */       StringBuffer formatted = new StringBuffer(this.message);
/*     */       
/* 393 */       if (this.explanation != null) {
/* 394 */         formatted.append(lineSeparator).append(this.explanation);
/*     */       }
/*     */       
/* 397 */       if (this.userResponse != null) {
/* 398 */         formatted.append(lineSeparator).append(this.userResponse);
/*     */       }
/*     */       
/* 401 */       if (this.adminResponse != null) {
/* 402 */         formatted.append(lineSeparator).append(this.adminResponse);
/*     */       }
/*     */       
/* 405 */       formatted.append(lineSeparator).append(this.id);
/*     */       
/* 407 */       return formatted.toString();
/*     */     }
/*     */     
/*     */     public void setEcmError(HttpServletRequest request) {
/* 411 */       request.setAttribute("ecmErrorId", "CIWEB" + this.id);
/* 412 */       request.setAttribute("ecmErrorMessage", this.message);
/* 413 */       request.setAttribute("ecmErrorExplanation", this.explanation);
/* 414 */       request.setAttribute("ecmErrorUserResponse", this.userResponse);
/* 415 */       request.setAttribute("ecmErrorAdminResponse", this.adminResponse);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\MessageUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */