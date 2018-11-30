/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.ier.plugin.IERApplicationPlugin;
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.configuration.SettingsConfig;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.tasks.util.AESEncryptionUtil;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.util.RMUserContext;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ 
/*     */ public class TaskManagerUtil
/*     */ {
/*     */   public static final String DB_HANDLER_NAME = "handler_name";
/*     */   public static final String DB_STATUS = "status";
/*     */   public static final String DB_PARENT = "parent";
/*     */   public static final String DB_NAME = "name";
/*     */   public static final String DB_DESCRIPTION = "description";
/*     */   public static final String DB_REPEAT_PERIOD = "repeat_period";
/*     */   public static final String DB_TASK_MODE = "task_mode";
/*     */   public static final String DB_START_TIME = "start_time";
/*     */   public static final String DB_CREATED_BY = "created_by";
/*     */   
/*     */   public static void setSchedulingInformation(JSONObject taskRequest, HttpServletRequest request, Repository repository, JSONObject requestParameters) throws Exception
/*     */   {
/*  36 */     String methodName = "setSchedulingInformation";
/*     */     
/*  38 */     Logger.logEntry(TaskManagerUtil.class, methodName, request);
/*     */     
/*  40 */     String isRecurring = request.getParameter("isRecurring");
/*  41 */     String repeatCycle = request.getParameter("repeatCycle");
/*  42 */     String startTime = request.getParameter("startTime");
/*  43 */     String endTime = request.getParameter("endTime");
/*     */     
/*  45 */     String username = (requestParameters != null) && (requestParameters.get("userId") != null) ? (String)requestParameters.get("userId") : request.getParameter("userId");
/*  46 */     String password = (requestParameters != null) && (requestParameters.get("password") != null) ? (String)requestParameters.get("password") : request.getParameter("password");
/*  47 */     String emailAddress = (requestParameters != null) && (requestParameters.get("emailAddress") != null) ? (String)requestParameters.get("emailAddress") : request.getParameter("emailAddress");
/*     */     
/*     */ 
/*  50 */     long scheduledStartTime = 0L;
/*  51 */     if (startTime == null) {
/*  52 */       scheduledStartTime = System.currentTimeMillis();
/*     */     } else {
/*  54 */       Date date = DateUtil.parseISODate(startTime, false, TimeZone.getTimeZone("GMT"));
/*  55 */       scheduledStartTime = date.getTime();
/*     */       
/*  57 */       scheduledStartTime -= 3600000L;
/*     */     }
/*     */     
/*  60 */     Logger.logDebug(TaskManagerUtil.class, methodName, request, "Start time: " + scheduledStartTime);
/*  61 */     taskRequest.put("startDateTime", Long.valueOf(scheduledStartTime));
/*  62 */     taskRequest.put("ceEJBURL", repository.getDomain().getDomainConnection().getUrl());
/*     */     
/*     */ 
/*  65 */     if ((!IERUtil.isNullOrEmpty(username)) && (!IERUtil.isNullOrEmpty(password))) {
/*  66 */       Logger.logDebug(TaskManagerUtil.class, methodName, request, "Putting in username and password");
/*     */       
/*  68 */       String encryptionKey = Config.getSettingsConfig().getAESEncryptionKey();
/*  69 */       String schema = request.getSession(false).getServletContext().getInitParameter("schema");
/*  70 */       String jndi = request.getSession(false).getServletContext().getInitParameter("jndi");
/*  71 */       taskRequest.put("jndi", jndi);
/*  72 */       taskRequest.put("schema", schema);
/*  73 */       taskRequest.put("userId", username);
/*  74 */       taskRequest.put("password", AESEncryptionUtil.encrypt(password, encryptionKey));
/*  75 */       taskRequest.put("AESEncryptionKey", encryptionKey);
/*     */     }
/*     */     else {
/*  78 */       taskRequest.put("serverType", J2EEServerUtil.getJ2EEAppServerType(request));
/*  79 */       if (J2EEServerUtil.getJ2EEAppServerType(request).equals("websphere")) {
/*  80 */         taskRequest.put("ltpaToken", getLtpaToken(request));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  86 */     if (!isRecurring.equals("true")) {
/*  87 */       taskRequest.put("taskMode", new Long(0L));
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  92 */       taskRequest.put("taskMode", new Long(1L));
/*  93 */       taskRequest.put("period", new Long(repeatCycle));
/*  94 */       Date date = DateUtil.parseISODate(endTime, true, TimeZone.getTimeZone("GMT"));
/*     */       
/*  96 */       taskRequest.put("endDateTime", Long.valueOf(date.getTime() - 3600000L));
/*     */     }
/*     */     
/*  99 */     if (emailAddress != null) {
/* 100 */       taskRequest.put("notifyInfo", emailAddress);
/*     */     }
/*     */     
/* 103 */     taskRequest.put("autoResume", Boolean.valueOf(true));
/* 104 */     taskRequest.put("parent", "IER");
/* 105 */     taskRequest.put("logLevel", getLogLevel());
/* 106 */     taskRequest.put("logDirectory", Config.getSettingsConfig().getTaskManagerLogDirectory());
/*     */   }
/*     */   
/*     */   public static String getLogLevel() {
/* 110 */     switch (IERApplicationPlugin.getPluginLogger().getLogLevel()) {
/*     */     case 1: 
/* 112 */       return "SEVERE";
/*     */     case 2: 
/* 114 */       return "WARNING";
/*     */     case 3: 
/* 116 */       return "INFO";
/*     */     case 4: 
/* 118 */       return "FINE";
/*     */     case 5: 
/* 120 */       return "FINER";
/*     */     case 6: 
/* 122 */       return "FINEST";
/*     */     }
/* 124 */     return "SEVERE";
/*     */   }
/*     */   
/*     */   public static DomainConnection getJarmConnection(String url, String userId, String password)
/*     */   {
/* 129 */     DomainConnection connection = com.ibm.jarm.api.core.RMFactory.DomainConnection.createInstance(com.ibm.jarm.api.constants.DomainType.P8_CE, url, null);
/* 130 */     Subject subject = RMUserContext.createSubject(connection, userId, password, "FileNetP8");
/* 131 */     RMUserContext.get().setSubject(subject);
/* 132 */     return connection;
/*     */   }
/*     */   
/*     */   public static RMDomain getRMDomain(String url, String userId, String password) {
/* 136 */     DomainConnection connection = getJarmConnection(url, userId, password);
/* 137 */     RMDomain domain = RMFactory.RMDomain.fetchInstance(connection, null, null);
/* 138 */     return domain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getLtpaToken(HttpServletRequest request)
/*     */   {
/* 148 */     String methodName = "getLtpaToken";
/* 149 */     Logger.logEntry(TaskManagerUtil.class, methodName, request);
/*     */     
/* 151 */     Cookie[] cookies = request.getCookies();
/* 152 */     String ltpaCookieValue = null;
/* 153 */     if ((cookies == null) || (cookies.length == 0))
/* 154 */       return ltpaCookieValue;
/* 155 */     for (int i = 0; i < cookies.length; i++) {
/* 156 */       Logger.logDebug(TaskManagerUtil.class, methodName, request, "cookies Name = " + cookies[i].getName() + "   Value = " + cookies[i].getValue());
/*     */       
/* 158 */       if (cookies[i].getName().equals("LtpaToken2")) {
/* 159 */         ltpaCookieValue = cookies[i].getValue();
/* 160 */         break;
/*     */       }
/*     */     }
/* 163 */     if (ltpaCookieValue == null) {
/* 164 */       for (int i = 0; i < cookies.length; i++) {
/* 165 */         if (cookies[i].getName().equals("LtpaToken")) {
/* 166 */           ltpaCookieValue = cookies[i].getValue();
/* 167 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 172 */     Logger.logExit(TaskManagerUtil.class, methodName, request);
/* 173 */     Logger.logDebug(TaskManagerUtil.class, methodName, request, " LTPA Token Cookie Value=" + ltpaCookieValue);
/* 174 */     return ltpaCookieValue;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\TaskManagerUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */