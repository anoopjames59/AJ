/*     */ package com.ibm.jarm.api.util;
/*     */ 
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */ {
/*  21 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
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
/*     */   public static void ckNullObjParam(String paramName, Object param)
/*     */   {
/*  38 */     Tracer.traceMethodEntry(new Object[] { paramName, param });
/*  39 */     if (param == null)
/*  40 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_EMPTY_INPUT_PARAM, new Object[] { paramName });
/*  41 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public static void ckNullOrInvalidArrayParam(String paramName, Object[] param)
/*     */   {
/*  55 */     Tracer.traceMethodEntry(new Object[] { paramName, param });
/*  56 */     if ((param == null) || (param.length == 0)) {
/*  57 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_INVALID_ARRAY_PARAM, new Object[] { paramName });
/*     */     }
/*  59 */     for (Object member : param)
/*     */     {
/*  61 */       if (member == null)
/*  62 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_INVALID_ARRAY_PARAM, new Object[] { paramName });
/*     */     }
/*  64 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public static void ckNullOrInvalidCollectionParam(String paramName, Collection<? extends Object> param)
/*     */   {
/*  78 */     Tracer.traceMethodEntry(new Object[] { paramName, param });
/*  79 */     if ((param == null) || (param.size() == 0)) {
/*  80 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_INVALID_COLLECTION_PARAM, new Object[] { paramName });
/*     */     }
/*  82 */     for (Object member : param)
/*     */     {
/*  84 */       if (member == null)
/*  85 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_INVALID_COLLECTION_PARAM, new Object[] { paramName });
/*     */     }
/*  87 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public static String ckInvalidStrParam(String paramName, String param)
/*     */   {
/* 103 */     Tracer.traceMethodEntry(new Object[] { paramName, param });
/* 104 */     ckNullObjParam(paramName, param);
/* 105 */     String trimmedStr = param.trim();
/* 106 */     if (trimmedStr.length() == 0) {
/* 107 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.E_NULL_OR_EMPTY_INPUT_PARAM, new Object[] { paramName });
/*     */     }
/* 109 */     Tracer.traceMethodExit(new Object[] { trimmedStr });
/* 110 */     return trimmedStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 115 */   private static PrintStream DbgPS = null;
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
/*     */   public static void dbgOutput(String msgPattern, Object... params)
/*     */   {
/* 129 */     PrintStream ps = getDbgOutputStream();
/* 130 */     if (ps != null)
/*     */     {
/*     */       try
/*     */       {
/* 134 */         MessageFormat mf = new MessageFormat(msgPattern);
/* 135 */         String msg = mf.format(params);
/* 136 */         ps.println(msg);
/* 137 */         ps.flush();
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 141 */         System.out.println("Error attempting to output dbg msg: " + msgPattern + "; " + ex.getLocalizedMessage());
/*     */       }
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
/*     */   public static PrintStream getDbgOutputStream()
/*     */   {
/* 160 */     if (DbgPS == null)
/*     */     {
/*     */       try
/*     */       {
/* 164 */         File tempDir = new File("D:/Temp");
/* 165 */         if (!tempDir.exists()) {
/* 166 */           tempDir = new File("C:/Temp");
/*     */         }
/* 168 */         if ((tempDir.exists()) && (tempDir.isDirectory()))
/*     */         {
/* 170 */           File dbgDir = new File(tempDir, "JARM_DBG");
/* 171 */           boolean dbgDirExistis = dbgDir.exists();
/* 172 */           if (!dbgDirExistis) {
/* 173 */             dbgDirExistis = dbgDir.mkdir();
/*     */           }
/* 175 */           if (dbgDirExistis)
/*     */           {
/* 177 */             SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd-HHmmss");
/* 178 */             String outputFileName = "dbgInfo_" + sdf.format(new Date()) + ".txt";
/* 179 */             File dbgFile = new File(dbgDir, outputFileName);
/* 180 */             DbgPS = new PrintStream(dbgFile);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 186 */         System.out.println("Can't acquire Debug output file PrintStream: " + ex.getLocalizedMessage());
/*     */       }
/*     */     }
/*     */     
/* 190 */     return DbgPS;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */