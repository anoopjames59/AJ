/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.search.Constants;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WCDateFormat
/*     */   implements Constants
/*     */ {
/*     */   public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
/*     */   public static final String TIME_FORMAT_STRING = "HH.mm.ss";
/*     */   public static final String TIMESTAMP_FORMAT_STRING = "yyyy-MM-dd-HH.mm.ss.SSS";
/*     */   public static final String OD_DATE_FORMAT_STRING = "MM/dd/yy";
/*     */   private static final String UTC_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
/*  47 */   private static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class SharedDateFormats
/*     */   {
/*  55 */     public SimpleDateFormat gmtIsoDateFormat = null;
/*  56 */     public SimpleDateFormat localIsoDateFormat = null;
/*     */     
/*     */     SharedDateFormats() {
/*  59 */       this.localIsoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
/*  60 */       this.gmtIsoDateFormat = ((SimpleDateFormat)this.localIsoDateFormat.clone());
/*     */       
/*  62 */       this.localIsoDateFormat.setTimeZone(TimeZone.getDefault());
/*  63 */       this.gmtIsoDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */     }
/*     */   }
/*     */   
/*  67 */   private static Map<Thread, SharedDateFormats> localData = Collections.synchronizedMap(new WeakHashMap());
/*     */   
/*     */   private static SharedDateFormats getSharedDateFormats() {
/*  70 */     SharedDateFormats sharedDateFormats = (SharedDateFormats)localData.get(Thread.currentThread());
/*  71 */     if (sharedDateFormats == null) {
/*  72 */       sharedDateFormats = new SharedDateFormats();
/*  73 */       localData.put(Thread.currentThread(), sharedDateFormats);
/*     */     }
/*  75 */     return sharedDateFormats;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   protected DateFormat dateFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   protected DateFormat timeFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  91 */   protected DateFormat timestampFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllDateFormats(DateFormat dateFormat, DateFormat timeFormat, DateFormat timestampFormat)
/*     */   {
/*  99 */     setDateFormat(dateFormat);
/* 100 */     setTimeFormat(timeFormat);
/* 101 */     setTimestampFormat(timestampFormat);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateFormat getDateFormat()
/*     */   {
/* 108 */     return this.dateFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateFormat(DateFormat dateFormat)
/*     */   {
/* 116 */     this.dateFormat = dateFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateFormat getTimeFormat()
/*     */   {
/* 123 */     return this.timeFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeFormat(DateFormat timeFormat)
/*     */   {
/* 131 */     this.timeFormat = timeFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateFormat getTimestampFormat()
/*     */   {
/* 138 */     return this.timestampFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimestampFormat(DateFormat timestampFormat)
/*     */   {
/* 146 */     this.timestampFormat = timestampFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WCDateFormat getDateFormats(HttpServletRequest request, RepositoryConfig repositoryConfig)
/*     */   {
/* 154 */     String methodName = "getDateFormats";
/*     */     
/*     */ 
/*     */ 
/* 158 */     WCDateFormat wcDateFormats = getDefaultDateFormats(request);
/*     */     
/*     */ 
/* 161 */     SimpleDateFormat dateFormat = (SimpleDateFormat)wcDateFormats.getDateFormat();
/* 162 */     SimpleDateFormat timeFormat = (SimpleDateFormat)wcDateFormats.getTimeFormat();
/* 163 */     SimpleDateFormat timestampFormat = (SimpleDateFormat)wcDateFormats.getTimestampFormat();
/*     */     try
/*     */     {
/* 166 */       if ((repositoryConfig.getDateFormat() != null) && (repositoryConfig.getDateFormat().length() > 0)) {
/* 167 */         dateFormat.applyPattern(repositoryConfig.getDateFormat());
/*     */       }
/*     */     }
/*     */     catch (RuntimeException e)
/*     */     {
/* 172 */       dateFormat.applyPattern("yyyy-MM-dd");
/*     */     }
/*     */     try
/*     */     {
/* 176 */       if ((repositoryConfig.getTimeFormat() != null) && (repositoryConfig.getTimeFormat().length() > 0)) {
/* 177 */         timeFormat.applyPattern(repositoryConfig.getTimeFormat());
/*     */       }
/*     */     }
/*     */     catch (RuntimeException e)
/*     */     {
/* 182 */       timeFormat.applyPattern("HH.mm.ss");
/*     */     }
/*     */     try
/*     */     {
/* 186 */       if ((repositoryConfig.getTimestampFormat() != null) && (repositoryConfig.getTimestampFormat().length() > 0)) {
/* 187 */         timestampFormat.applyPattern(repositoryConfig.getTimestampFormat());
/*     */         
/* 189 */         String timestampPattern = timestampFormat.toPattern();
/*     */         
/* 191 */         boolean hasMilliHolder = timestampPattern.indexOf("S") > -1;
/* 192 */         int numMilliHolders = timestampPattern.lastIndexOf("S") - timestampPattern.indexOf("S") + 1;
/*     */         
/*     */ 
/*     */ 
/* 196 */         if ((hasMilliHolder) && (numMilliHolders > 3)) {
/* 197 */           String newMilliFormat = "";
/* 198 */           while (newMilliFormat.length() <= 3) {
/* 199 */             newMilliFormat = newMilliFormat + "S";
/*     */           }
/* 201 */           timestampPattern.replaceAll("/[S]{" + numMilliHolders + "}/", newMilliFormat);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (RuntimeException e)
/*     */     {
/* 207 */       timestampFormat.applyPattern("yyyy-MM-dd-HH.mm.ss.SSS");
/*     */     }
/*     */     
/*     */ 
/* 211 */     wcDateFormats.setAllDateFormats(dateFormat, timeFormat, timestampFormat);
/*     */     
/*     */ 
/* 214 */     return wcDateFormats;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WCDateFormat getDefaultDateFormats(HttpServletRequest request)
/*     */   {
/* 224 */     String methodName = "getDefaultDateFormats";
/*     */     
/*     */ 
/*     */ 
/* 228 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
/* 229 */     SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");
/* 230 */     SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
/*     */     
/*     */ 
/* 233 */     WCDateFormat wcDateFormats = new WCDateFormat();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 238 */     wcDateFormats.setAllDateFormats(dateFormat, timeFormat, timestampFormat);
/*     */     
/*     */ 
/* 241 */     return wcDateFormats;
/*     */   }
/*     */   
/*     */   public static String[] getDateRangeStrings(HttpServletRequest request, long timestamp) throws ParseException {
/* 245 */     String methodName = "getDateRangeStrings";
/*     */     
/* 247 */     Date date = new Date(timestamp);
/* 248 */     DateFormat dateFormat = getSharedDateFormats().gmtIsoDateFormat;
/* 249 */     String date1 = dateFormat.format(date);
/*     */     
/*     */ 
/* 252 */     long t = date.getTime();
/* 253 */     t += 86400000L;
/* 254 */     String date2 = dateFormat.format(new Date(t));
/*     */     
/* 256 */     return new String[] { date1, date2 };
/*     */   }
/*     */   
/*     */   public static String getDateString(HttpServletRequest request, long timestamp, boolean timeZoneAdjust) {
/* 260 */     String methodName = "getDateString";
/*     */     
/* 262 */     Date date = new Date(timestamp);
/*     */     String date1;
/* 264 */     String date1; if (timeZoneAdjust) {
/* 265 */       date1 = getSharedDateFormats().localIsoDateFormat.format(date);
/*     */     } else {
/* 267 */       date1 = getSharedDateFormats().gmtIsoDateFormat.format(date);
/*     */     }
/*     */     
/* 270 */     return date1;
/*     */   }
/*     */   
/*     */   public static Calendar parseW3CDate(String str, TimeZone defaultTimeZone) {
/* 274 */     if ((str == null) || (str.isEmpty())) {
/* 275 */       return null;
/*     */     }
/* 277 */     int hour = 0;int min = 0;int sec = 0;int msec = 0;
/* 278 */     int[] form = { 0, 4, 5, 7, 8, 10, 11, 13, 14, 16, 17, 19, 16, 19 };
/* 279 */     int len = str.length();
/* 280 */     Calendar cal = Calendar.getInstance();
/* 281 */     cal.setLenient(false);
/*     */     
/* 283 */     int year = Integer.parseInt(str.substring(form[0], form[1]));
/* 284 */     int month = Integer.parseInt(str.substring(form[2], form[3]));
/* 285 */     int day = Integer.parseInt(str.substring(form[4], form[5]));
/* 286 */     if (len >= form[13]) {
/* 287 */       hour = Integer.parseInt(str.substring(form[6], form[7]));
/* 288 */       min = Integer.parseInt(str.substring(form[8], form[9]));
/* 289 */       sec = Integer.parseInt(str.substring(form[10], form[11]));
/* 290 */       msec = parseMilliseconds(str, len);
/*     */     }
/*     */     
/* 293 */     TimeZone tz = defaultTimeZone == null ? TimeZone.getDefault() : defaultTimeZone;
/*     */     
/* 295 */     if (str.charAt(len - 1) == 'Z') {
/* 296 */       tz = TIME_ZONE_GMT;
/* 297 */     } else if (len >= form[12]) {
/* 298 */       int signPosition = len - 6;
/* 299 */       char sign = str.charAt(signPosition);
/*     */       
/* 301 */       if ((sign == '+') || (sign == '-')) {
/* 302 */         String timeZone = str.substring(signPosition);
/* 303 */         tz = TimeZone.getTimeZone("GMT" + timeZone);
/*     */       }
/*     */     }
/* 306 */     cal.setTimeZone(tz);
/* 307 */     cal.set(year, month - 1, day, hour, min, sec);
/* 308 */     cal.set(14, msec);
/*     */     
/* 310 */     return cal;
/*     */   }
/*     */   
/*     */   private static int parseMilliseconds(String str, int len) {
/* 314 */     int msec = 0;
/* 315 */     if ((len >= 21) && (str.charAt(19) == '.'))
/*     */     {
/* 317 */       int d = Character.digit(str.charAt(20), 10);
/* 318 */       if (d != -1) {
/* 319 */         msec = d * 100;
/* 320 */         if (len >= 22) {
/* 321 */           d = Character.digit(str.charAt(21), 10);
/* 322 */           if (d != -1) {
/* 323 */             msec += d * 10;
/* 324 */             if (len >= 23) {
/* 325 */               d = Character.digit(str.charAt(22), 10);
/* 326 */               if (d != -1) {
/* 327 */                 msec += d;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 334 */     return msec;
/*     */   }
/*     */   
/*     */   public static String convertDateToW3CDate(Calendar cal, boolean dateOnly) {
/* 338 */     StringBuilder sb = new StringBuilder(30);
/*     */     
/* 340 */     int year = cal.get(1);
/* 341 */     int month = cal.get(2) + 1;
/* 342 */     int dayOfMonth = cal.get(5);
/*     */     
/* 344 */     sb.append(year);
/* 345 */     sb.append('-');
/* 346 */     if (month < 10)
/* 347 */       sb.append('0');
/* 348 */     sb.append(month);
/* 349 */     sb.append('-');
/* 350 */     if (dayOfMonth < 10)
/* 351 */       sb.append('0');
/* 352 */     sb.append(dayOfMonth);
/* 353 */     if (!dateOnly) {
/* 354 */       int hour = cal.get(11);
/* 355 */       int min = cal.get(12);
/* 356 */       int sec = cal.get(13);
/* 357 */       int msec = cal.get(14);
/* 358 */       sb.append('T');
/* 359 */       if (hour < 10)
/* 360 */         sb.append('0');
/* 361 */       sb.append(hour);
/* 362 */       sb.append(':');
/* 363 */       if (min < 10)
/* 364 */         sb.append('0');
/* 365 */       sb.append(min);
/* 366 */       sb.append(':');
/* 367 */       if (sec < 10)
/* 368 */         sb.append('0');
/* 369 */       sb.append(sec);
/* 370 */       if (msec != 0) {
/* 371 */         sb.append('.');
/* 372 */         sb.append(msec);
/*     */       }
/*     */     }
/*     */     
/* 376 */     int offset = cal.get(15) + cal.get(16);
/* 377 */     if (offset < 0) {
/* 378 */       offset = -offset;
/* 379 */       sb.append('-');
/*     */     } else {
/* 381 */       sb.append('+');
/*     */     }
/* 383 */     int nMilliSeconsPerHour = 3600000;
/* 384 */     int nMilliSeconsPerMinute = 60000;
/* 385 */     int offsetHour = offset / nMilliSeconsPerHour;
/* 386 */     int offsetMin = (offset - nMilliSeconsPerHour * offsetHour) / nMilliSeconsPerMinute;
/* 387 */     if (offsetHour < 10)
/* 388 */       sb.append('0');
/* 389 */     sb.append(offsetHour);
/* 390 */     sb.append(':');
/* 391 */     if (offsetMin < 10)
/* 392 */       sb.append('0');
/* 393 */     sb.append(offsetMin);
/*     */     
/* 395 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\WCDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */