/*     */ package com.ibm.ier.logtrace;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceBundleService
/*     */ {
/*  23 */   private static Map<String, Map<Locale, ResourceBundle>> Cache = Collections.synchronizedMap(new HashMap());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ResourceBundle get(String resourceBundleBasePath, Locale locale)
/*     */   {
/*  48 */     if ((resourceBundleBasePath == null) || (resourceBundleBasePath.trim().length() == 0))
/*     */     {
/*  50 */       throw new RuntimeException("'resourceBundleBasePath' parameter cannot be null nor blank.");
/*     */     }
/*     */     
/*  53 */     ResourceBundle result = null;
/*  54 */     Locale actualLocale = locale != null ? locale : Locale.getDefault();
/*  55 */     Map<Locale, ResourceBundle> bundleMap = null;
/*     */     
/*     */     try
/*     */     {
/*  59 */       bundleMap = (Map)Cache.get(resourceBundleBasePath);
/*  60 */       if (bundleMap != null)
/*     */       {
/*     */ 
/*  63 */         result = (ResourceBundle)bundleMap.get(actualLocale);
/*     */         
/*  65 */         if (result == null)
/*     */         {
/*  67 */           result = ResourceBundle.getBundle(resourceBundleBasePath, actualLocale);
/*     */           
/*  69 */           bundleMap.put(actualLocale, result);
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/*  75 */         bundleMap = Collections.synchronizedMap(new HashMap());
/*  76 */         Cache.put(resourceBundleBasePath, bundleMap);
/*  77 */         result = ResourceBundle.getBundle(resourceBundleBasePath, actualLocale);
/*  78 */         bundleMap.put(actualLocale, result);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (MissingResourceException ex)
/*     */     {
/*  85 */       if (bundleMap != null)
/*     */       {
/*  87 */         bundleMap.put(actualLocale, new UnavailableResourceBundle(resourceBundleBasePath));
/*     */       }
/*     */       
/*  90 */       throw ex;
/*     */     }
/*     */     
/*  93 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void flush()
/*     */   {
/* 102 */     Cache = Collections.synchronizedMap(new HashMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class UnavailableResourceBundle
/*     */     extends ResourceBundle
/*     */   {
/* 112 */     private static final Vector<String> EmptyVector = new Vector(0);
/*     */     
/*     */ 
/*     */     private final String resourceBasename;
/*     */     
/*     */ 
/*     */     public UnavailableResourceBundle(String resourceBasename)
/*     */     {
/* 120 */       this.resourceBasename = resourceBasename;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Enumeration<String> getKeys()
/*     */     {
/* 127 */       return EmptyVector.elements();
/*     */     }
/*     */     
/*     */ 
/*     */     protected Object handleGetObject(String key)
/*     */     {
/* 133 */       return "<Unavailable ResourceBundle: '" + this.resourceBasename + "'>";
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\logtrace\ResourceBundleService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */