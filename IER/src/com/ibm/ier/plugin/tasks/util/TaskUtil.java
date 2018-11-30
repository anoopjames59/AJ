/*     */ package com.ibm.ier.plugin.tasks.util;
/*     */ 
/*     */ import com.ibm.ecm.task.TaskLogger;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMFactory.DomainConnection;
/*     */ import com.ibm.jarm.api.util.RMUserContext;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import com.ibm.websphere.security.auth.WSSubject;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskUtil
/*     */ {
/*     */   public static Subject performTaskLogin(JSONObject taskInfo, Logger logger)
/*     */     throws Exception
/*     */   {
/*  30 */     String methodName = "performTask";
/*  31 */     String className = TaskUtil.class.getName();
/*     */     
/*  33 */     JSONObject specificTaskRequest = (JSONObject)taskInfo.get("specificTaskRequest");
/*     */     
/*  35 */     String connectionUrl = (String)taskInfo.get("ceEJBURL");
/*  36 */     if (connectionUrl == null)
/*  37 */       connectionUrl = (String)specificTaskRequest.get("ceEJBURL");
/*  38 */     String username = (String)taskInfo.get("userId");
/*  39 */     String password = (String)taskInfo.get("password");
/*  40 */     String serverType = (String)taskInfo.get("serverType");
/*  41 */     String ltpaToken = (String)taskInfo.get("ltpaToken");
/*     */     
/*  43 */     DomainConnection connection = RMFactory.DomainConnection.createInstance(DomainType.P8_CE, connectionUrl, null);
/*  44 */     String context = connectionUrl.contains("wsi/FNCEWS") ? "FileNetP8WSI" : "FileNetP8";
/*  45 */     Subject subject = null;
/*  46 */     if ((username == null) || (password == null)) {
/*  47 */       TaskLogger.fine(className, methodName, "No username and password available");
/*  48 */       if (serverType.equals("websphere")) {
/*  49 */         TaskLogger.fine(className, methodName, "Logging in into Websphere with ltpaToken: " + ltpaToken);
/*  50 */         if (ltpaToken != null) {
/*  51 */           TaskLogger.severe(className, methodName, ltpaToken, null);
/*     */         }
/*  53 */         subject = SSOLoginUtil.getSubjectForWAS(ltpaToken);
/*  54 */         if (subject != null) {
/*  55 */           WSSubject.setRunAsSubject(subject);
/*  56 */           TaskLogger.fine(className, methodName, "Login successfully into WAS: " + subject);
/*     */         }
/*     */         else {
/*  59 */           TaskLogger.fine(className, methodName, "WAS login failed!");
/*     */         }
/*     */       }
/*  62 */       else if (serverType.equals("weblogic")) {
/*  63 */         TaskLogger.fine(className, methodName, "Logging in into Weblogic");
/*  64 */         subject = SSOLoginUtil.getSubjectForWL();
/*  65 */         if (subject != null) {
/*  66 */           TaskLogger.fine(className, methodName, "Login successfully into WL: " + subject);
/*     */         }
/*     */         else {
/*  69 */           TaskLogger.fine(className, methodName, "WL login failed!");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  74 */         subject = RMUserContext.createSubject(connection, "p8admin", "p8admin", context);
/*     */       }
/*     */       
/*  77 */       if (subject == null) {
/*  78 */         TaskLogger.fine(className, methodName, "Login has failed. No subject available.");
/*     */       }
/*     */     }
/*     */     else {
/*  82 */       String rawPassword = getDecryptedPassword(taskInfo);
/*  83 */       subject = RMUserContext.createSubject(connection, username, rawPassword, context);
/*     */     }
/*  85 */     return subject;
/*     */   }
/*     */   
/*     */   public static String getDecryptedPassword(JSONObject taskInfo) throws Exception {
/*  89 */     String methodName = "getDecryptedPassword";
/*  90 */     String className = TaskUtil.class.getName();
/*     */     
/*  92 */     String password = (String)taskInfo.get("password");
/*  93 */     if (password != null) {
/*  94 */       TaskLogger.fine(className, methodName, "Logging in with provided username and password");
/*  95 */       String encryptionKey = (String)taskInfo.get("AESEncryptionKey");
/*  96 */       if (encryptionKey == null) {
/*  97 */         encryptionKey = (String)taskInfo.get("AESEncryptionKey");
/*     */       }
/*  99 */       if (encryptionKey == null) {
/* 100 */         throw new Exception(MessageResources.getLocalizedMessage("error.aesEncryptionKey.notAvailable", new Object[0]));
/*     */       }
/* 102 */       String rawPassword = AESEncryptionUtil.decrypt(password, encryptionKey);
/* 103 */       return rawPassword;
/*     */     }
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public static String stripInvalidCharacters(String string) {
/* 109 */     return string.replaceAll("[\\\\/:\"*?<>|]+", "");
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\util\TaskUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */