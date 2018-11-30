/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_CacheService
/*     */ {
/*  19 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */   public static final String KeyType_ClassDescription = "CD";
/*     */   
/*     */   public static final String KeyType_ConnectorRegistration = "CR";
/*     */   public static final String KeyType_MiscFlags = "MF";
/*  25 */   private static P8CE_CacheService Singleton = new P8CE_CacheService();
/*     */   private Map<String, Object> cacheMap;
/*     */   
/*     */   public static P8CE_CacheService getInstance() {
/*  29 */     return Singleton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_CacheService()
/*     */   {
/*  37 */     Tracer.traceMethodEntry(new Object[0]);
/*  38 */     this.cacheMap = Collections.synchronizedMap(new HashMap());
/*  39 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public void put(Repository repository, String keyType, String key, Object value)
/*     */   {
/*  55 */     Tracer.traceMethodEntry(new Object[] { repository, keyType, key, value });
/*  56 */     String actualKey = makeActualKey(repository, keyType, key);
/*  57 */     this.cacheMap.put(actualKey, value);
/*  58 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public Object get(Repository repository, String keyType, String key)
/*     */   {
/*  76 */     Tracer.traceMethodEntry(new Object[] { repository, keyType, key });
/*  77 */     String actualKey = makeActualKey(repository, keyType, key);
/*  78 */     Object result = this.cacheMap.get(actualKey);
/*     */     
/*  80 */     Tracer.traceMethodExit(new Object[] { result });
/*  81 */     return result;
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
/*     */   public boolean containsKey(Repository repository, String keyType, String key)
/*     */   {
/*  97 */     Tracer.traceMethodEntry(new Object[] { repository, keyType, key });
/*  98 */     String actualKey = makeActualKey(repository, keyType, key);
/*  99 */     boolean result = this.cacheMap.containsKey(actualKey);
/*     */     
/* 101 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 102 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 111 */     synchronized (this.cacheMap)
/*     */     {
/* 113 */       this.cacheMap.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private String makeActualKey(Repository repository, String keyType, String key)
/*     */   {
/* 119 */     Tracer.traceMethodEntry(new Object[] { repository, keyType, key });
/* 120 */     StringBuilder sb = new StringBuilder();
/* 121 */     if (repository != null)
/*     */     {
/* 123 */       ((P8CE_RepositoryImpl)repository).resolve();
/* 124 */       sb.append(repository.getSymbolicName()).append('.');
/*     */     }
/*     */     
/* 127 */     sb.append(keyType).append('.');
/* 128 */     sb.append(key);
/* 129 */     String result = sb.toString();
/*     */     
/* 131 */     Tracer.traceMethodExit(new Object[] { result });
/* 132 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_CacheService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */