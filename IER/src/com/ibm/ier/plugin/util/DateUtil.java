/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DateUtil
/*     */ {
/*     */   private static class SharedDateFormats
/*     */   {
/*  30 */     public SimpleDateFormat gmtIsoDateFormat = null;
/*  31 */     public SimpleDateFormat noTimezoneIsoDateFormat = null;
/*  32 */     public SimpleDateFormat xqueryDateFormat = null;
/*     */     
/*     */     SharedDateFormats() {
/*  35 */       this.gmtIsoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
/*  36 */       this.gmtIsoDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */       
/*  38 */       this.noTimezoneIsoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/*     */       
/*  40 */       this.xqueryDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
/*     */     }
/*     */   }
/*     */   
/*  44 */   private static Map<Thread, SharedDateFormats> localData = Collections.synchronizedMap(new WeakHashMap());
/*     */   
/*     */   private static SharedDateFormats getSharedDateFormats() {
/*  47 */     SharedDateFormats sharedDateFormats = (SharedDateFormats)localData.get(Thread.currentThread());
/*  48 */     if (sharedDateFormats == null) {
/*  49 */       sharedDateFormats = new SharedDateFormats();
/*  50 */       localData.put(Thread.currentThread(), sharedDateFormats);
/*     */     }
/*  52 */     return sharedDateFormats;
/*     */   }
/*     */   
/*     */   public static String getISODateString(Date date, boolean timeZoneAdjust) { String isoDateString;
/*     */     String isoDateString;
/*  57 */     if (timeZoneAdjust) {
/*  58 */       isoDateString = getSharedDateFormats().gmtIsoDateFormat.format(date);
/*     */     } else {
/*  60 */       isoDateString = getSharedDateFormats().noTimezoneIsoDateFormat.format(date);
/*     */     }
/*  62 */     return isoDateString;
/*     */   }
/*     */   
/*     */   public static Date parseISODate(String isoDateString, boolean timeZoneAdjust) throws ParseException, DatatypeConfigurationException {
/*  66 */     return parseISODate(isoDateString, timeZoneAdjust, (TimeZone)null);
/*     */   }
/*     */   
/*     */   public static TimeZone getClientTimeZone(HttpServletRequest request) {
/*  70 */     Locale clientLocale = request.getLocale();
/*  71 */     TimeZone clientTimeZone = Calendar.getInstance(clientLocale).getTimeZone();
/*  72 */     return clientTimeZone;
/*     */   }
/*     */   
/*     */   public static Date parseISODate(String isoDateString, boolean timeZoneAdjust, HttpServletRequest request) throws ParseException, DatatypeConfigurationException {
/*  76 */     return parseISODate(isoDateString, timeZoneAdjust, getClientTimeZone(request));
/*     */   }
/*     */   
/*     */   public static Date parseISODate(String isoDateString, boolean timeZoneAdjust, TimeZone clientTimeZone) throws ParseException, DatatypeConfigurationException {
/*     */     XMLGregorianCalendar xmlCalendar;
/*     */     XMLGregorianCalendar xmlCalendar;
/*  82 */     if (timeZoneAdjust) {
/*  83 */       xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(isoDateString);
/*     */     } else {
/*  85 */       xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(isoDateString.substring(0, 19));
/*     */     }
/*     */     
/*     */     GregorianCalendar calendar;
/*  89 */     if (xmlCalendar.getYear() < 1583)
/*     */     {
/*  91 */       GregorianCalendar calendar = new GregorianCalendar();
/*  92 */       calendar.set(xmlCalendar.getYear(), xmlCalendar.getMonth() - 1, xmlCalendar.getDay(), xmlCalendar.getHour(), xmlCalendar.getMinute(), xmlCalendar.getSecond());
/*     */     } else {
/*  94 */       calendar = xmlCalendar.toGregorianCalendar();
/*     */     }
/*  96 */     if (clientTimeZone != null) {
/*  97 */       calendar.setTimeZone(clientTimeZone);
/*     */     }
/*  99 */     return calendar.getTime();
/*     */   }
/*     */   
/*     */   public static String getXQueryDateString(Date date) {
/* 103 */     return getSharedDateFormats().xqueryDateFormat.format(date);
/*     */   }
/*     */   
/*     */   public static Date parseXQueryDateString(String xqueryDateString) throws ParseException {
/* 107 */     return getSharedDateFormats().xqueryDateFormat.parse(xqueryDateString);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\DateUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */