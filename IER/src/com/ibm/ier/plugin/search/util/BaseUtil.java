/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.ecm.configuration.Config;
/*     */ import com.ibm.ecm.configuration.InterfaceTextConfig;
/*     */ import com.ibm.ecm.configuration.InterfaceTextLocaleConfig;
/*     */ import com.ibm.ecm.serviceability.Logger;
/*     */ import java.text.CollationKey;
/*     */ import java.text.Collator;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TimeZone;
/*     */ import java.util.Vector;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaseUtil
/*     */ {
/*     */   private String className;
/*     */   private ServletContext controllerServletContext;
/*     */   
/*     */   public BaseUtil()
/*     */   {
/*  44 */     this.className = getClass().getName();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  49 */     this.controllerServletContext = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public ServletContext getControllerServletContext()
/*     */   {
/*  55 */     return this.controllerServletContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setControllerServletContext(ServletContext controllerServletContext)
/*     */   {
/*  63 */     this.controllerServletContext = controllerServletContext;
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
/*     */   public Object getAttribute(ServletRequest request, String name)
/*     */   {
/*  87 */     String methodName = "getAttribute";
/*  88 */     Object property = request.getParameter(name);
/*  89 */     if ((property == null) || (((property instanceof String)) && (((String)property).length() == 0))) {
/*  90 */       property = getAttributeWithoutParameter(request, name);
/*     */     }
/*     */     
/*  93 */     return property;
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
/*     */   public Object getAttributeWithoutParameter(ServletRequest request, String name)
/*     */   {
/* 117 */     String methodName = "getAttributeWithoutParameter";
/*     */     
/*     */ 
/*     */ 
/* 121 */     HttpSession session = getSession(request);
/*     */     
/* 123 */     Object property = request.getAttribute(name);
/*     */     
/*     */ 
/* 126 */     if ((property == null) || (((property instanceof String)) && (((String)property).length() == 0)))
/*     */     {
/* 128 */       if (session != null) {
/* 129 */         property = session.getAttribute(name);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 135 */     if ((property == null) || (((property instanceof String)) && (((String)property).length() == 0))) {
/* 136 */       ServletContext servletContext = getControllerServletContext();
/*     */       
/* 138 */       if (servletContext != null) {
/* 139 */         property = servletContext.getAttribute(name);
/*     */       }
/*     */     }
/*     */     
/* 143 */     return property;
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
/*     */   public void setAttribute(ServletRequest request, Object value, String name)
/*     */   {
/* 173 */     String methodName = "setAttribute";
/* 174 */     Logger.logEntry(this.className, methodName, request);
/*     */     
/* 176 */     HttpSession session = getSession(request);
/* 177 */     String property = null;
/*     */     
/* 179 */     if ((session != null) && ("true".equals(property))) {
/* 180 */       Logger.logDebug(this, methodName, request, "storing attribtue in session, session store is enabled");
/* 181 */       session.setAttribute(name, value);
/*     */     } else {
/* 183 */       Logger.logDebug(this, methodName, request, "storing attribtue in request, session store is disabled");
/* 184 */       request.setAttribute(name, value);
/*     */     }
/*     */     
/* 187 */     Logger.logExit(this.className, methodName, request);
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
/*     */   public String getDelimiter(ServletContext application)
/*     */   {
/* 200 */     return ";";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getValidFilename(String fileName)
/*     */   {
/* 211 */     fileName = fileName.replace('\\', '-');
/* 212 */     fileName = fileName.replace('/', '-');
/* 213 */     fileName = fileName.replace(':', '-');
/* 214 */     fileName = fileName.replace('*', '-');
/* 215 */     fileName = fileName.replace('?', '-');
/* 216 */     fileName = fileName.replace('"', '-');
/* 217 */     fileName = fileName.replace('<', '-');
/* 218 */     fileName = fileName.replace('>', '-');
/* 219 */     fileName = fileName.replace('|', '-');
/* 220 */     return fileName;
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
/*     */   public static String replaceString(String message, String token, String replacementText)
/*     */   {
/* 235 */     StringTokenizer st = new StringTokenizer(message, token);
/* 236 */     StringBuffer newMessage = new StringBuffer(message.length());
/*     */     
/* 238 */     int pos = 0;
/* 239 */     int len = token.length();
/* 240 */     pos = message.indexOf(token);
/* 241 */     while (pos != -1)
/*     */     {
/* 243 */       String preString = message.substring(0, pos);
/* 244 */       String postString = message.substring(pos + len, message.length());
/* 245 */       message = preString + replacementText + postString;
/* 246 */       pos = message.indexOf(token);
/*     */     }
/*     */     
/*     */ 
/* 250 */     return message.toString().trim();
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public String replaceText(String message, String token, String replacementText)
/*     */   {
/* 266 */     return replaceString(message, token, replacementText);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] convertFromVectortoStringArray(Vector vec)
/*     */   {
/* 277 */     String[] tmpArray = new String[vec.size()];
/* 278 */     for (int counter = 0; counter < vec.size(); counter++) {
/* 279 */       tmpArray[counter] = ((String)vec.elementAt(counter));
/*     */     }
/* 281 */     return tmpArray;
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
/*     */   public String[] sortStringArray(String[] array, boolean descending, Locale locale)
/*     */   {
/* 296 */     if ((array != null) && (locale != null))
/*     */     {
/* 298 */       String[] tmpArray = new String[array.length];
/* 299 */       Arrays.sort(array, Collator.getInstance(locale));
/*     */       
/* 301 */       if (descending)
/*     */       {
/* 303 */         int counter = 0; for (int counter2 = array.length - 1; counter < array.length; counter2--)
/*     */         {
/* 305 */           tmpArray[counter] = array[counter2];counter++;
/*     */         }
/* 307 */         array = tmpArray;
/*     */       }
/*     */     }
/*     */     
/* 311 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpSession getSession(ServletRequest request)
/*     */   {
/* 323 */     HttpSession session = null;
/*     */     
/* 325 */     if ((request instanceof HttpServletRequest))
/*     */     {
/*     */ 
/*     */ 
/* 329 */       session = ((HttpServletRequest)request).getSession(false);
/*     */     }
/*     */     
/* 332 */     return session;
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
/*     */   public String getEncoding(HttpServletRequest request)
/*     */   {
/* 346 */     return "UTF-8";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEven(int tokens)
/*     */   {
/* 464 */     String methodName = "isEven";
/*     */     
/* 466 */     float tempNumb = tokens / 2.0F;
/* 467 */     boolean isEven = tempNumb - Math.floor(tempNumb) == 0.0D;
/*     */     
/* 469 */     return isEven;
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
/*     */   public static int toIntType(Object intV, int defaultValue)
/*     */   {
/* 483 */     int iVal = defaultValue;
/* 484 */     if (intV == null) {
/* 485 */       iVal = defaultValue;
/* 486 */     } else if ((intV instanceof String)) {
/*     */       try {
/* 488 */         iVal = Integer.parseInt((String)intV);
/*     */       } catch (NumberFormatException e) {
/* 490 */         iVal = defaultValue;
/*     */       }
/* 492 */     } else if ((intV instanceof Integer)) {
/*     */       try {
/* 494 */         iVal = ((Integer)intV).intValue();
/*     */       } catch (Exception e) {
/* 496 */         iVal = defaultValue;
/*     */       }
/*     */     } else {
/* 499 */       iVal = defaultValue;
/*     */     }
/*     */     
/* 502 */     return iVal;
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
/*     */   public static boolean toBooleanType(Object truefalse, boolean defaultValue)
/*     */   {
/* 516 */     boolean bVal = defaultValue;
/* 517 */     if (truefalse == null) {
/* 518 */       bVal = defaultValue;
/* 519 */     } else if ((truefalse instanceof String)) {
/*     */       try {
/* 521 */         Boolean tmpBoolean = new Boolean((String)truefalse);
/* 522 */         bVal = tmpBoolean.booleanValue();
/*     */       } catch (Exception e) {
/* 524 */         bVal = defaultValue;
/*     */       }
/* 526 */     } else if ((truefalse instanceof Boolean)) {
/*     */       try {
/* 528 */         bVal = ((Boolean)truefalse).booleanValue();
/*     */       } catch (Exception e) {
/* 530 */         bVal = defaultValue;
/*     */       }
/*     */     } else {
/* 533 */       bVal = defaultValue;
/*     */     }
/*     */     
/* 536 */     return bVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean validateDate(String date1)
/*     */   {
/*     */     int year;
/*     */     
/*     */ 
/*     */     int month;
/*     */     
/*     */     int day;
/*     */     
/*     */     try
/*     */     {
/* 552 */       StringTokenizer tk = new StringTokenizer(date1, "-");
/*     */       
/* 554 */       year = Integer.parseInt(tk.nextToken());
/* 555 */       month = Integer.parseInt(tk.nextToken());
/* 556 */       day = Integer.parseInt(tk.nextToken());
/*     */     } catch (Exception e) {
/* 558 */       return false;
/*     */     }
/*     */     
/* 561 */     if (year < 1970) {
/* 562 */       return false;
/*     */     }
/*     */     
/* 565 */     if ((month > 12) || (month < 1)) {
/* 566 */       return false;
/*     */     }
/*     */     
/* 569 */     if (month == 2) {
/* 570 */       if (year % 4 == 0) {
/* 571 */         if ((day > 28) || (day < 1)) {
/* 572 */           return false;
/*     */         }
/*     */       }
/* 575 */       else if ((day > 29) || (day < 1)) {
/* 576 */         return false;
/*     */       }
/*     */     }
/* 579 */     else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
/* 580 */       if ((day > 30) || (day < 1)) {
/* 581 */         return false;
/*     */       }
/*     */     }
/* 584 */     else if ((day > 31) || (day < 1)) {
/* 585 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 589 */     return true;
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
/*     */   public static String changeTimeZone(String tmStr, String tmFormat, TimeZone fromTimeZone, TimeZone toTimeZone)
/*     */   {
/*     */     try
/*     */     {
/* 607 */       tmStr = tmStr.substring(0, tmFormat.length() - 1);
/* 608 */       SimpleDateFormat dateFormat = new SimpleDateFormat(tmFormat);
/* 609 */       dateFormat.setTimeZone(fromTimeZone);
/* 610 */       Date convertedDate = dateFormat.parse(tmStr);
/* 611 */       dateFormat.setTimeZone(toTimeZone);
/*     */       
/* 613 */       return dateFormat.format(convertedDate);
/*     */     } catch (Exception e) {}
/* 615 */     return tmStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] sort(String[] array, Locale locale)
/*     */   {
/* 627 */     if ((array == null) || (array.length == 0)) {
/* 628 */       return array;
/*     */     }
/* 630 */     List list = new ArrayList(Arrays.asList(array));
/*     */     
/* 632 */     WCComparator comparator = new WCComparator(null);
/* 633 */     Collator localCollator = getCollator(locale);
/*     */     
/* 635 */     comparator.setCollator(localCollator);
/* 636 */     Collections.sort(list, comparator);
/*     */     
/* 638 */     return getArrayFromList(list);
/*     */   }
/*     */   
/*     */   private static String[] getArrayFromList(List list) {
/* 642 */     if (list.size() == 0) {
/* 643 */       return new String[0];
/*     */     }
/* 645 */     int size = list.size();
/* 646 */     String[] tempArray = new String[size];
/* 647 */     for (int a = 0; a < size; a++) {
/* 648 */       tempArray[a] = ((String)list.get(a));
/*     */     }
/* 650 */     return tempArray;
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
/*     */   public static boolean isMatch(String checkString, String pattern)
/*     */   {
/* 667 */     int patternPos = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 672 */     for (int i = 0; i < checkString.length(); i++)
/*     */     {
/*     */ 
/* 675 */       if (patternPos >= pattern.length()) {
/* 676 */         return false;
/*     */       }
/*     */       
/* 679 */       char patternChar = pattern.charAt(patternPos);
/* 680 */       char thisChar = checkString.charAt(i);
/*     */       
/* 682 */       switch (patternChar)
/*     */       {
/*     */ 
/*     */ 
/*     */       case '*': 
/* 687 */         if (patternPos >= pattern.length() - 1) {
/* 688 */           return true;
/*     */         }
/*     */         
/* 691 */         for (j = i; j < checkString.length(); j++) {
/* 692 */           if (isMatch(checkString.substring(j), pattern.substring(patternPos + 1))) {
/* 693 */             return true;
/*     */           }
/*     */         }
/*     */         
/* 697 */         return false;
/*     */       
/*     */ 
/*     */ 
/*     */       case '?': 
/*     */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       case '[': 
/* 708 */         if (patternPos >= pattern.length() - 1) {
/* 709 */           return false;
/*     */         }
/* 711 */         char lastPatternChar = '\000';
/* 712 */         for (j = patternPos + 1; j < pattern.length(); j++) {
/* 713 */           patternChar = pattern.charAt(j);
/* 714 */           if (patternChar == ']')
/*     */           {
/* 716 */             return false; }
/* 717 */           if (patternChar == '-')
/*     */           {
/* 719 */             j++;
/* 720 */             if (j == pattern.length()) {
/* 721 */               return false;
/*     */             }
/* 723 */             patternChar = pattern.charAt(j);
/* 724 */             if (patternChar == ']') {
/* 725 */               return false;
/*     */             }
/* 727 */             if ((thisChar >= lastPatternChar) && (thisChar <= patternChar))
/*     */               break;
/*     */           } else {
/* 730 */             if (thisChar == patternChar) {
/*     */               break;
/*     */             }
/*     */           }
/*     */           
/* 735 */           lastPatternChar = patternChar;
/*     */         }
/*     */         
/*     */ 
/* 739 */         patternPos = j;
/* 740 */         for (j = patternPos; j < pattern.length(); j++) {
/* 741 */           if (pattern.charAt(j) == ']')
/*     */             break;
/*     */         }
/* 744 */         patternPos = j;
/* 745 */         break;
/*     */       
/*     */ 
/*     */       default: 
/* 749 */         if (thisChar != patternChar) {
/* 750 */           return false;
/*     */         }
/*     */         break;
/*     */       }
/*     */       
/* 755 */       patternPos++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 762 */     for (int j = patternPos; j < pattern.length(); j++) {
/* 763 */       if (pattern.charAt(j) != '*')
/*     */         break;
/*     */     }
/* 766 */     patternPos = j;
/*     */     
/*     */ 
/*     */ 
/* 770 */     if (patternPos == pattern.length()) {
/* 771 */       return true;
/*     */     }
/* 773 */     return false;
/*     */   }
/*     */   
/*     */   private static class WCComparator
/*     */     implements Comparator
/*     */   {
/* 779 */     Collator collator = Collator.getInstance();
/*     */     
/*     */     public int compare(Object element1, Object element2) {
/* 782 */       CollationKey key1 = this.collator.getCollationKey(element1.toString());
/* 783 */       CollationKey key2 = this.collator.getCollationKey(element2.toString());
/* 784 */       return key1.compareTo(key2);
/*     */     }
/*     */     
/*     */     public void setCollator(Collator collator) {
/* 788 */       this.collator = collator;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Collator getCollator(Locale locale) {
/* 793 */     Collator localCollator = null;
/* 794 */     if (locale == null) {
/* 795 */       localCollator = Collator.getInstance();
/*     */     } else {
/* 797 */       localCollator = Collator.getInstance(locale);
/*     */     }
/* 799 */     return localCollator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] toStringArray(Collection coll)
/*     */   {
/* 810 */     if (coll == null) {
/* 811 */       return new String[0];
/*     */     }
/* 813 */     String[] retVal = new String[coll.size()];
/* 814 */     Iterator iter = coll.iterator();
/* 815 */     int i = 0;
/* 816 */     while (iter.hasNext()) {
/* 817 */       retVal[(i++)] = ((String)iter.next());
/*     */     }
/* 819 */     return retVal;
/*     */   }
/*     */   
/*     */   private class HttpException extends Exception
/*     */   {
/*     */     int statusCode;
/*     */     
/*     */     HttpException(int statusCode, String message)
/*     */     {
/* 828 */       super();
/* 829 */       this.statusCode = statusCode;
/*     */     }
/*     */     
/*     */     public int getStatusCode() {
/* 833 */       return this.statusCode;
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
/*     */   public String getMimeTypeFromFileName(HttpServletRequest request, String fileName)
/*     */     throws Exception
/*     */   {
/* 852 */     String methodName = "getMimeTypeFromFileName";
/* 853 */     Logger.logEntry(this, methodName, request);
/*     */     
/* 855 */     String partMimeType = null;
/*     */     try
/*     */     {
/* 858 */       int index = fileName.lastIndexOf(".");
/* 859 */       if (index != -1) {
/* 860 */         String fileExtension = fileName.substring(index + 1, fileName.length());
/* 861 */         partMimeType = MimeTypeUtil.getMimeType(fileExtension);
/*     */       } else {
/* 863 */         partMimeType = null;
/*     */       }
/*     */     } catch (Exception e) {
/* 866 */       Logger.logError(this, methodName, request, e);
/*     */     }
/*     */     
/* 869 */     Logger.logExit(this, methodName, request);
/* 870 */     return partMimeType;
/*     */   }
/*     */   
/*     */   public static String getCustomLabelValue(HttpServletRequest request, String id) {
/* 874 */     String applicationName = request.getParameter("application");
/* 875 */     InterfaceTextConfig labelUI = null;
/* 876 */     String customLabel = null;
/*     */     try {
/* 878 */       labelUI = Config.getInterfaceTextConfig(applicationName, id);
/* 879 */       customLabel = labelUI.getLocaleData().getPropertyValue(request.getLocale().getLanguage());
/*     */     } catch (Exception e) {
/* 881 */       customLabel = null;
/*     */     }
/* 883 */     return customLabel;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\BaseUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */