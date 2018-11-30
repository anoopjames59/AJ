/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
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
/*     */ public class WCDateFormat
/*     */ {
/*     */   public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
/*     */   public static final String TIME_FORMAT_STRING = "HH.mm.ss";
/*     */   public static final String TIMESTAMP_FORMAT_STRING = "yyyy-MM-dd-HH.mm.ss.SSS";
/*     */   public static final String OD_DATE_FORMAT_STRING = "MM/dd/yy";
/*  38 */   public static final SimpleDateFormat UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
/*     */   
/*  40 */   private static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  45 */   protected DateFormat dateFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   protected DateFormat timeFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   protected DateFormat timestampFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllDateFormats(DateFormat dateFormat, DateFormat timeFormat, DateFormat timestampFormat)
/*     */   {
/*  63 */     setDateFormat(dateFormat);
/*  64 */     setTimeFormat(timeFormat);
/*  65 */     setTimestampFormat(timestampFormat);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateFormat getDateFormat()
/*     */   {
/*  72 */     return this.dateFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateFormat(DateFormat dateFormat)
/*     */   {
/*  80 */     this.dateFormat = dateFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateFormat getTimeFormat()
/*     */   {
/*  87 */     return this.timeFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeFormat(DateFormat timeFormat)
/*     */   {
/*  95 */     this.timeFormat = timeFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateFormat getTimestampFormat()
/*     */   {
/* 102 */     return this.timestampFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimestampFormat(DateFormat timestampFormat)
/*     */   {
/* 110 */     this.timestampFormat = timestampFormat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WCDateFormat getDateFormats(HttpServletRequest request, RepositoryConfig repositoryConfig)
/*     */   {
/* 118 */     String methodName = "getDateFormats";
/*     */     
/*     */ 
/*     */ 
/* 122 */     WCDateFormat wcDateFormats = getDefaultDateFormats(request);
/*     */     
/*     */ 
/* 125 */     SimpleDateFormat dateFormat = (SimpleDateFormat)wcDateFormats.getDateFormat();
/* 126 */     SimpleDateFormat timeFormat = (SimpleDateFormat)wcDateFormats.getTimeFormat();
/* 127 */     SimpleDateFormat timestampFormat = (SimpleDateFormat)wcDateFormats.getTimestampFormat();
/*     */     try
/*     */     {
/* 130 */       if ((repositoryConfig.getDateFormat() != null) && (repositoryConfig.getDateFormat().length() > 0)) {
/* 131 */         dateFormat.applyPattern(repositoryConfig.getDateFormat());
/* 132 */         Logger.logDebug(WCDateFormat.class, methodName, request, "repositoryConfig date format: " + repositoryConfig.getDateFormat());
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 135 */       Logger.logWarning(WCDateFormat.class, methodName, request, "dateFormat property not valid ('" + repositoryConfig.getDateFormat() + "'), will use default ('" + "yyyy-MM-dd" + "').\n" + e.toString());
/* 136 */       dateFormat.applyPattern("yyyy-MM-dd");
/*     */     }
/*     */     try
/*     */     {
/* 140 */       if ((repositoryConfig.getTimeFormat() != null) && (repositoryConfig.getTimeFormat().length() > 0)) {
/* 141 */         timeFormat.applyPattern(repositoryConfig.getTimeFormat());
/* 142 */         Logger.logDebug(WCDateFormat.class, methodName, request, "repositoryConfig time format: " + repositoryConfig.getTimeFormat());
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 145 */       Logger.logWarning(WCDateFormat.class, methodName, request, "timeFormat property not valid ('" + repositoryConfig.getTimeFormat() + "'), will use default ('" + "HH.mm.ss" + "').\n" + e.toString());
/* 146 */       timeFormat.applyPattern("HH.mm.ss");
/*     */     }
/*     */     try
/*     */     {
/* 150 */       if ((repositoryConfig.getTimestampFormat() != null) && (repositoryConfig.getTimestampFormat().length() > 0)) {
/* 151 */         timestampFormat.applyPattern(repositoryConfig.getTimestampFormat());
/*     */         
/* 153 */         String timestampPattern = timestampFormat.toPattern();
/* 154 */         Logger.logDebug(WCDateFormat.class, methodName, request, "timestampPattern: " + timestampPattern);
/* 155 */         boolean hasMilliHolder = timestampPattern.indexOf("S") > -1;
/* 156 */         int numMilliHolders = timestampPattern.lastIndexOf("S") - timestampPattern.indexOf("S") + 1;
/* 157 */         Logger.logDebug(WCDateFormat.class, methodName, request, "numMilliHolders: " + numMilliHolders);
/*     */         
/*     */ 
/* 160 */         if ((hasMilliHolder) && (numMilliHolders > 3)) {
/* 161 */           String newMilliFormat = "";
/* 162 */           while (newMilliFormat.length() <= 3) {
/* 163 */             newMilliFormat = newMilliFormat + "S";
/*     */           }
/* 165 */           timestampPattern = timestampPattern.replaceAll("/[S]{" + numMilliHolders + "}/", newMilliFormat);
/*     */         }
/* 167 */         Logger.logDebug(WCDateFormat.class, methodName, request, "[post-processing] timestampPattern: " + timestampPattern);
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 170 */       Logger.logWarning(WCDateFormat.class, methodName, request, "timeFormat property not valid ('" + repositoryConfig.getTimestampFormat() + "'), will use default ('" + "HH.mm.ss" + "').\n" + e.toString());
/* 171 */       timestampFormat.applyPattern("yyyy-MM-dd-HH.mm.ss.SSS");
/*     */     }
/*     */     
/* 174 */     Logger.logDebug(WCDateFormat.class, methodName, request, "{\"" + dateFormat.toPattern() + "\",\"" + timeFormat.toPattern() + "\",\"" + timestampFormat.toPattern() + "\"}");
/* 175 */     wcDateFormats.setAllDateFormats(dateFormat, timeFormat, timestampFormat);
/*     */     
/*     */ 
/* 178 */     return wcDateFormats;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WCDateFormat getDefaultDateFormats(HttpServletRequest request)
/*     */   {
/* 188 */     String methodName = "getDefaultDateFormats";
/*     */     
/*     */ 
/*     */ 
/* 192 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
/* 193 */     SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");
/* 194 */     SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
/*     */     
/*     */ 
/* 197 */     WCDateFormat wcDateFormats = new WCDateFormat();
/*     */     
/* 199 */     Logger.logDebug(WCDateFormat.class, methodName, request, "{\"" + dateFormat.toPattern() + "\",\"" + timeFormat.toPattern() + "\",\"" + timestampFormat.toPattern() + "\"}");
/* 200 */     wcDateFormats.setAllDateFormats(dateFormat, timeFormat, timestampFormat);
/*     */     
/*     */ 
/* 203 */     return wcDateFormats;
/*     */   }
/*     */   
/*     */   public static String[] getDateRangeStrings(HttpServletRequest request, long timestamp, DateFormat dateFormat) throws ParseException {
/* 207 */     String methodName = "getDateRangeStrings";
/* 208 */     Logger.logEntry(WCDateFormat.class, methodName, request);
/* 209 */     Date date = new Date(timestamp);
/* 210 */     dateFormat.setTimeZone(TIME_ZONE_GMT);
/* 211 */     String date1 = dateFormat.format(date);
/*     */     
/*     */ 
/* 214 */     long t = date.getTime();
/* 215 */     t += 86400000L;
/* 216 */     String date2 = dateFormat.format(new Date(t));
/* 217 */     Logger.logExit(WCDateFormat.class, methodName, request);
/* 218 */     return new String[] { date1, date2 };
/*     */   }
/*     */   
/*     */   public static String getDateString(HttpServletRequest request, long timestamp, DateFormat dateFormat, boolean timeZoneAdjust) {
/* 222 */     String methodName = "getDateString";
/* 223 */     Logger.logEntry(WCDateFormat.class, methodName, request);
/*     */     TimeZone tz;
/* 225 */     TimeZone tz; if (timeZoneAdjust) {
/* 226 */       tz = TimeZone.getDefault();
/*     */     } else {
/* 228 */       tz = TIME_ZONE_GMT;
/*     */     }
/* 230 */     dateFormat.setTimeZone(tz);
/*     */     
/* 232 */     Date date = new Date(timestamp);
/* 233 */     String date1 = dateFormat.format(date);
/*     */     
/* 235 */     Logger.logExit(WCDateFormat.class, methodName, request, "date: " + date1);
/* 236 */     return date1;
/*     */   }
/*     */   
/*     */   public static String getDateString(HttpServletRequest request, long timestamp, DateFormat dateFormat) {
/* 240 */     return getDateString(request, timestamp, dateFormat, false);
/*     */   }
/*     */   
/*     */   public static Date parseDate(String dateString, DateFormat dateFormat) throws ParseException
/*     */   {
/* 245 */     return parseDate(null, dateString, dateFormat);
/*     */   }
/*     */   
/*     */   public static Date parseDate(HttpServletRequest request, String dateString, DateFormat dateFormat) throws ParseException {
/* 249 */     String methodName = "parseDate";
/* 250 */     Logger.logEntry(WCDateFormat.class, methodName, request);
/* 251 */     dateFormat.setLenient(false);
/* 252 */     Date dt = dateFormat.parse(dateString);
/* 253 */     Logger.logExit(WCDateFormat.class, methodName, request);
/* 254 */     return dt;
/*     */   }
/*     */   
/*     */   public static Calendar parseW3CDate(String str, TimeZone defaultTimeZone) {
/* 258 */     if ((str == null) || (str.isEmpty())) {
/* 259 */       return null;
/*     */     }
/* 261 */     int hour = 0;int min = 0;int sec = 0;int msec = 0;
/* 262 */     int[] form = { 0, 4, 5, 7, 8, 10, 11, 13, 14, 16, 17, 19, 16, 19 };
/* 263 */     int len = str.length();
/* 264 */     Calendar cal = Calendar.getInstance();
/* 265 */     cal.setLenient(false);
/*     */     
/* 267 */     int year = Integer.parseInt(str.substring(form[0], form[1]));
/* 268 */     int month = Integer.parseInt(str.substring(form[2], form[3]));
/* 269 */     int day = Integer.parseInt(str.substring(form[4], form[5]));
/* 270 */     if (len >= form[13]) {
/* 271 */       hour = Integer.parseInt(str.substring(form[6], form[7]));
/* 272 */       min = Integer.parseInt(str.substring(form[8], form[9]));
/* 273 */       sec = Integer.parseInt(str.substring(form[10], form[11]));
/* 274 */       msec = parseMilliseconds(str, len);
/*     */     }
/*     */     
/* 277 */     TimeZone tz = defaultTimeZone == null ? TimeZone.getDefault() : defaultTimeZone;
/*     */     
/* 279 */     if (str.charAt(len - 1) == 'Z') {
/* 280 */       tz = TIME_ZONE_GMT;
/* 281 */     } else if (len >= form[12]) {
/* 282 */       int signPosition = len - 6;
/* 283 */       char sign = str.charAt(signPosition);
/*     */       
/* 285 */       if ((sign == '+') || (sign == '-')) {
/* 286 */         String timeZone = str.substring(signPosition);
/* 287 */         tz = TimeZone.getTimeZone("GMT" + timeZone);
/*     */       }
/*     */     }
/* 290 */     cal.setTimeZone(tz);
/* 291 */     cal.set(year, month - 1, day, hour, min, sec);
/* 292 */     cal.set(14, msec);
/*     */     
/* 294 */     return cal;
/*     */   }
/*     */   
/*     */   private static int parseMilliseconds(String str, int len) {
/* 298 */     int msec = 0;
/* 299 */     if ((len >= 21) && (str.charAt(19) == '.'))
/*     */     {
/* 301 */       int d = Character.digit(str.charAt(20), 10);
/* 302 */       if (d != -1) {
/* 303 */         msec = d * 100;
/* 304 */         if (len >= 22) {
/* 305 */           d = Character.digit(str.charAt(21), 10);
/* 306 */           if (d != -1) {
/* 307 */             msec += d * 10;
/* 308 */             if (len >= 23) {
/* 309 */               d = Character.digit(str.charAt(22), 10);
/* 310 */               if (d != -1) {
/* 311 */                 msec += d;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 318 */     return msec;
/*     */   }
/*     */   
/*     */   public static String convertDateToW3CDate(Calendar cal, boolean dateOnly) {
/* 322 */     StringBuilder sb = new StringBuilder(30);
/*     */     
/* 324 */     int year = cal.get(1);
/* 325 */     int month = cal.get(2) + 1;
/* 326 */     int dayOfMonth = cal.get(5);
/*     */     
/* 328 */     sb.append(year);
/* 329 */     sb.append('-');
/* 330 */     if (month < 10)
/* 331 */       sb.append('0');
/* 332 */     sb.append(month);
/* 333 */     sb.append('-');
/* 334 */     if (dayOfMonth < 10)
/* 335 */       sb.append('0');
/* 336 */     sb.append(dayOfMonth);
/* 337 */     if (!dateOnly) {
/* 338 */       int hour = cal.get(11);
/* 339 */       int min = cal.get(12);
/* 340 */       int sec = cal.get(13);
/* 341 */       int msec = cal.get(14);
/* 342 */       sb.append('T');
/* 343 */       if (hour < 10)
/* 344 */         sb.append('0');
/* 345 */       sb.append(hour);
/* 346 */       sb.append(':');
/* 347 */       if (min < 10)
/* 348 */         sb.append('0');
/* 349 */       sb.append(min);
/* 350 */       sb.append(':');
/* 351 */       if (sec < 10)
/* 352 */         sb.append('0');
/* 353 */       sb.append(sec);
/* 354 */       if (msec != 0) {
/* 355 */         sb.append('.');
/* 356 */         sb.append(msec);
/*     */       }
/*     */     }
/*     */     
/* 360 */     int offset = cal.get(15) + cal.get(16);
/* 361 */     if (offset < 0) {
/* 362 */       offset = -offset;
/* 363 */       sb.append('-');
/*     */     } else {
/* 365 */       sb.append('+');
/*     */     }
/* 367 */     int nMilliSeconsPerHour = 3600000;
/* 368 */     int nMilliSeconsPerMinute = 60000;
/* 369 */     int offsetHour = offset / nMilliSeconsPerHour;
/* 370 */     int offsetMin = (offset - nMilliSeconsPerHour * offsetHour) / nMilliSeconsPerMinute;
/* 371 */     if (offsetHour < 10)
/* 372 */       sb.append('0');
/* 373 */     sb.append(offsetHour);
/* 374 */     sb.append(':');
/* 375 */     if (offsetMin < 10)
/* 376 */       sb.append('0');
/* 377 */     sb.append(offsetMin);
/*     */     
/* 379 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\WCDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */