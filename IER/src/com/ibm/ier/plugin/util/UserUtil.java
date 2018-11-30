/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import com.filenet.api.core.Factory.User;
/*    */ import com.filenet.api.property.PropertyFilter;
/*    */ import com.filenet.api.security.User;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserUtil
/*    */ {
/*    */   public static String retrieveUserDisplayName(String userIdOrShortName, IERBaseService baseService, String repositoryId)
/*    */   {
/* 21 */     String displayName = null;
/*    */     try {
/* 23 */       User user = retrieveUser(userIdOrShortName, baseService, repositoryId);
/* 24 */       if (user != null)
/* 25 */         displayName = user.get_DisplayName();
/*    */     } catch (Exception e) {
/* 27 */       return null;
/*    */     }
/*    */     
/* 30 */     return displayName;
/*    */   }
/*    */   
/*    */   public static User retrieveUserCache(String userIdOrShortName, HttpServletRequest request, String repositoryId) throws Exception {
/* 34 */     String key = "users_" + repositoryId;
/*    */     
/* 36 */     Object usersMapObj = SessionUtil.getCacheProperty(key, request);
/* 37 */     if (usersMapObj == null) {
/* 38 */       usersMapObj = new HashMap();
/* 39 */       SessionUtil.setCacheProperty(key, usersMapObj, request);
/*    */     }
/*    */     
/*    */ 
/* 43 */     Map<String, User> users = (Map)usersMapObj;
/* 44 */     if (users.containsKey(userIdOrShortName)) {
/* 45 */       return (User)users.get(userIdOrShortName);
/*    */     }
/* 47 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static User retrieveUser(String userIdOrShortName, IERBaseService baseService, String repositoryId)
/*    */     throws Exception
/*    */   {
/* 55 */     String key = "users_" + repositoryId;
/*    */     
/* 57 */     Object usersMapObj = SessionUtil.getCacheProperty(key, baseService.getServletRequest());
/* 58 */     if (usersMapObj == null) {
/* 59 */       usersMapObj = new HashMap();
/* 60 */       SessionUtil.setCacheProperty(key, usersMapObj, baseService.getServletRequest());
/*    */     }
/*    */     
/*    */ 
/* 64 */     Map<String, User> users = (Map)usersMapObj;
/* 65 */     if (users.containsKey(userIdOrShortName)) {
/* 66 */       return (User)users.get(userIdOrShortName);
/*    */     }
/* 68 */     PropertyFilter filter = new PropertyFilter();
/* 69 */     filter.addIncludeProperty(0, null, null, "Id", null);
/* 70 */     filter.addIncludeProperty(0, null, null, "ShortName", null);
/* 71 */     filter.addIncludeProperty(0, null, null, "DisplayName", null);
/* 72 */     filter.addIncludeProperty(0, null, null, "Email", null);
/*    */     User user;
/*    */     try {
/* 75 */       user = Factory.User.fetchInstance(baseService.getP8Connection(repositoryId), userIdOrShortName, filter);
/* 76 */       users.put(userIdOrShortName, user);
/*    */     } catch (Exception e) {
/* 78 */       return null;
/*    */     }
/*    */     
/* 81 */     return user;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\UserUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */