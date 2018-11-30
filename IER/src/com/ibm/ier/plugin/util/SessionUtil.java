/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMClassDescription;
/*     */ import com.ibm.jarm.api.core.RMFactory.Repository;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SessionUtil
/*     */ {
/*  38 */   private static String IER_SESSION_MAP = "ierSessionCacheMap";
/*  39 */   private static String KEY_REPOSITORIES = "keyRepositories";
/*  40 */   private static String KEY_CLASS_DESCRIPTIONS = "keyClassDescriptions";
/*  41 */   private static String KEY_PROPERTY_DESCRIPTION_LISTS = "keyPropertyDescriptionLists";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Repository getRepository(String repoId, RMDomain domain, HttpServletRequest request)
/*     */   {
/*  52 */     Object obj = getCacheProperty(KEY_REPOSITORIES + repoId, request);
/*     */     
/*  54 */     if (Logger.isDebugLogged()) {
/*  55 */       Logger.logDebug(SessionUtil.class, "getRepository", request, "RepositoryId: " + repoId + " RMDomain: " + domain + " obj:" + obj);
/*     */     }
/*     */     
/*  58 */     if (obj == null) {
/*  59 */       Repository repo = RMFactory.Repository.fetchInstance(domain, repoId, RMPropertyFilter.MinimumPropertySet);
/*  60 */       if (Logger.isDebugLogged()) {
/*  61 */         Logger.logDebug(SessionUtil.class, "getRepository", request, "Repository session object: " + repo);
/*     */       }
/*  63 */       setCacheProperty(KEY_REPOSITORIES + repoId, repo, request);
/*  64 */       return repo;
/*     */     }
/*     */     
/*  67 */     if (Logger.isDebugLogged()) {
/*  68 */       Logger.logDebug(SessionUtil.class, "getRepository", request, "Obj in session: " + obj);
/*     */     }
/*     */     
/*  71 */     if ((obj instanceof Repository)) {
/*  72 */       if (Logger.isDebugLogged()) {
/*  73 */         Logger.logDebug(SessionUtil.class, "getRepository", request, "is obj a repository obj" + String.valueOf(obj instanceof Repository));
/*     */       }
/*  75 */       return (Repository)obj;
/*     */     }
/*     */     
/*     */ 
/*  79 */     if (Logger.isDebugLogged()) {
/*  80 */       Logger.logDebug(SessionUtil.class, "getRepository", request, "obj is not a repository obj");
/*     */     }
/*  82 */     return null;
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
/*     */   public static RMClassDescription getClassDescription(Repository repo, String className, HttpServletRequest request)
/*     */   {
/*  97 */     Locale locale = request.getLocale();
/*  98 */     RMClassDescription cd = (RMClassDescription)getCacheProperty(KEY_CLASS_DESCRIPTIONS + className + repo.getSymbolicName() + locale.toString(), request);
/*  99 */     if (cd == null)
/*     */     {
/* 101 */       cd = RMFactory.RMClassDescription.fetchInstance(repo, className, RMPropertyFilter.MinimumPropertySet);
/* 102 */       setCacheProperty(KEY_CLASS_DESCRIPTIONS + className + repo.getSymbolicName() + locale.toString(), cd, request);
/*     */     }
/* 104 */     return cd;
/*     */   }
/*     */   
/*     */   public static List<RMPropertyDescription> getPropertyDescriptionList(Repository repo, String className, HttpServletRequest request)
/*     */   {
/* 109 */     return getPropertyDescriptionList(repo, className, request, false);
/*     */   }
/*     */   
/*     */   public static List<RMPropertyDescription> getPropertyDescriptionList(Repository repo, String className, HttpServletRequest request, boolean includeSubClass)
/*     */   {
/* 114 */     Locale locale = request.getLocale();
/*     */     
/* 116 */     List<RMPropertyDescription> pd_list = (List)getCacheProperty(KEY_PROPERTY_DESCRIPTION_LISTS + className + repo.getSymbolicName() + locale.toString() + includeSubClass, request);
/* 117 */     if (pd_list == null)
/*     */     {
/* 119 */       RMClassDescription cd = getClassDescription(repo, className, request);
/* 120 */       pd_list = cd.getPropertyDescriptions();
/* 121 */       if (includeSubClass)
/*     */       {
/* 123 */         List<RMPropertyDescription> pd_listSub = cd.getAllDescendentPropertyDescriptions();
/* 124 */         pd_list.addAll(pd_listSub);
/*     */       }
/* 126 */       setCacheProperty(KEY_PROPERTY_DESCRIPTION_LISTS + className + repo.getSymbolicName() + locale.toString() + includeSubClass, pd_list, request);
/*     */     }
/* 128 */     return pd_list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setClassDescriptionCache(Repository repo, RMClassDescription cd, HttpServletRequest request)
/*     */   {
/* 138 */     Locale locale = request.getLocale();
/* 139 */     RMClassDescription cd_session = (RMClassDescription)getCacheProperty(KEY_CLASS_DESCRIPTIONS + cd.getSymbolicName() + repo.getSymbolicName() + locale.toString(), request);
/* 140 */     if (cd_session == null)
/*     */     {
/* 142 */       setCacheProperty(KEY_CLASS_DESCRIPTIONS + cd.getSymbolicName() + repo.getSymbolicName() + locale.toString(), cd, request);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object getCacheProperty(String name, HttpServletRequest request)
/*     */   {
/* 154 */     Map<String, Object> map = getIERSessionMap(request);
/* 155 */     return map.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setCacheProperty(String name, Object value, HttpServletRequest request)
/*     */   {
/* 166 */     Map<String, Object> map = getIERSessionMap(request);
/* 167 */     map.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void removeCacheProperty(String name, HttpServletRequest request)
/*     */   {
/* 177 */     Map<String, Object> map = getIERSessionMap(request);
/* 178 */     map.remove(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Map<String, Object> getIERSessionMap(HttpServletRequest request)
/*     */   {
/* 189 */     Map<String, Object> map = (Map)request.getSession().getAttribute(IER_SESSION_MAP);
/* 190 */     if (map == null) {
/* 191 */       map = new HashMap();
/* 192 */       request.getSession().setAttribute(IER_SESSION_MAP, map);
/*     */     }
/* 194 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void clearSessionCache(HttpServletRequest request)
/*     */   {
/* 202 */     request.getSession().removeAttribute(IER_SESSION_MAP);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\SessionUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */