/*     */ package com.ibm.jarm.ral.common;
/*     */ 
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_Util;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class RALPESupport
/*     */ {
/*  29 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*     */   
/*     */   public static final String CLASSNAME_VWSession = "filenet.vw.api.VWSession";
/*     */   
/*     */   public static final String CLASSNAME_VWStepElement = "filenet.vw.api.VWStepElement";
/*     */   
/*     */   public static final String CLASSNAME_VWUserInfo = "filenet.vw.api.VWUserInfo";
/*     */   
/*     */   public static final String CLASSNAME_VWException = "filenet.vw.api.VWException";
/*     */   private static Class<?> CLASS_VWSession;
/*     */   private static Class<?> CLASS_VWStepElement;
/*     */   private static Class<?> CLASS_VWUserInfo;
/*     */   private static Class<?> CLASS_VWException;
/*     */   private static Method METHOD_VWSession_createWorkflow;
/*     */   private static Method METHOD_VWSession_fetchCurrentUserInfo;
/*     */   private static Method METHOD_VWStepElement_setParameterValue;
/*     */   private static Method METHOD_VWStepElement_doDispatch;
/*     */   private static Method METHOD_VWUserInfo_getName;
/*  47 */   private static Boolean StaticSetupStatus = null;
/*  48 */   private static Exception SetupException = null;
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
/*     */   public static synchronized boolean isPESupportAvailable()
/*     */   {
/*  62 */     Tracer.traceMethodEntry(new Object[0]);
/*  63 */     if (StaticSetupStatus == null)
/*     */     {
/*  65 */       StaticSetupStatus = Boolean.FALSE;
/*     */       try
/*     */       {
/*  68 */         CLASS_VWException = Class.forName("filenet.vw.api.VWException");
/*     */         
/*  70 */         CLASS_VWSession = Class.forName("filenet.vw.api.VWSession");
/*  71 */         METHOD_VWSession_createWorkflow = CLASS_VWSession.getMethod("createWorkflow", new Class[] { String.class });
/*     */         
/*  73 */         METHOD_VWSession_fetchCurrentUserInfo = CLASS_VWSession.getMethod("fetchCurrentUserInfo", new Class[0]);
/*     */         
/*     */ 
/*  76 */         CLASS_VWStepElement = Class.forName("filenet.vw.api.VWStepElement");
/*  77 */         METHOD_VWStepElement_setParameterValue = CLASS_VWStepElement.getMethod("setParameterValue", new Class[] { String.class, Object.class, Boolean.TYPE });
/*     */         
/*  79 */         METHOD_VWStepElement_doDispatch = CLASS_VWStepElement.getMethod("doDispatch", new Class[0]);
/*     */         
/*     */ 
/*  82 */         CLASS_VWUserInfo = Class.forName("filenet.vw.api.VWUserInfo");
/*  83 */         METHOD_VWUserInfo_getName = CLASS_VWUserInfo.getMethod("getName", new Class[0]);
/*     */         
/*     */ 
/*  86 */         StaticSetupStatus = Boolean.TRUE;
/*     */       }
/*     */       catch (ClassNotFoundException ex)
/*     */       {
/*  90 */         SetupException = ex;
/*     */       }
/*     */       catch (NoSuchMethodException ex)
/*     */       {
/*  94 */         SetupException = ex;
/*     */       }
/*     */     }
/*     */     
/*  98 */     boolean result = StaticSetupStatus.booleanValue();
/*  99 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 100 */     return result;
/*     */   }
/*     */   
/*     */   public static synchronized Exception getSetupException()
/*     */   {
/* 105 */     return SetupException;
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
/*     */ 
/*     */ 
/*     */   public static Object createWorkflow(Object vwSession, String workflowIdentifier)
/*     */   {
/* 125 */     Tracer.traceMethodEntry(new Object[] { vwSession, workflowIdentifier });
/* 126 */     if (!isPESupportAvailable())
/*     */     {
/* 128 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_NOT_AVAILABLE, new Object[0]);
/*     */     }
/*     */     
/* 131 */     if (!CLASS_VWSession.isInstance(vwSession))
/*     */     {
/* 133 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_BAD_VWSESSION_PARAMETER, new Object[0]);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 138 */       Object result = METHOD_VWSession_createWorkflow.invoke(vwSession, new Object[] { workflowIdentifier });
/*     */       
/* 140 */       Tracer.traceMethodExit(new Object[] { result });
/* 141 */       return result;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 145 */       if (CLASS_VWException.isInstance(ex))
/*     */       {
/* 147 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_PE_API_VWEXCEPTION_OCCURRED, new Object[] { ex.getLocalizedMessage() });
/*     */       }
/*     */       
/*     */ 
/* 151 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
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
/*     */ 
/*     */ 
/*     */   public static void setParameterValue(Object vwStepElement, String parameterName, Object parameterValue, boolean compareValue)
/*     */   {
/* 171 */     Tracer.traceMethodEntry(new Object[] { vwStepElement, parameterName, parameterValue, Boolean.valueOf(compareValue) });
/* 172 */     if (!CLASS_VWStepElement.isInstance(vwStepElement))
/*     */     {
/* 174 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_BAD_STEPELEMENT_PARAMETER, new Object[0]);
/*     */     }
/*     */     
/* 177 */     if (!isPESupportAvailable())
/*     */     {
/* 179 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_NOT_AVAILABLE, new Object[0]);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 184 */       METHOD_VWStepElement_setParameterValue.invoke(vwStepElement, new Object[] { parameterName, parameterValue, Boolean.valueOf(compareValue) });
/* 185 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 189 */       if (CLASS_VWException.isInstance(ex))
/*     */       {
/* 191 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_PE_API_VWEXCEPTION_OCCURRED, new Object[] { ex.getLocalizedMessage() });
/*     */       }
/*     */       
/*     */ 
/* 195 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
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
/*     */   public static void doDispatch(Object vwStepElement)
/*     */   {
/* 212 */     Tracer.traceMethodEntry(new Object[] { vwStepElement });
/* 213 */     if (!CLASS_VWStepElement.isInstance(vwStepElement))
/*     */     {
/* 215 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_BAD_STEPELEMENT_PARAMETER, new Object[0]);
/*     */     }
/*     */     
/* 218 */     if (!isPESupportAvailable())
/*     */     {
/* 220 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_NOT_AVAILABLE, new Object[0]);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 225 */       METHOD_VWStepElement_doDispatch.invoke(vwStepElement, new Object[0]);
/* 226 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 230 */       if (CLASS_VWException.isInstance(ex))
/*     */       {
/* 232 */         throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_PE_API_VWEXCEPTION_OCCURRED, new Object[] { ex.getLocalizedMessage() });
/*     */       }
/*     */       
/*     */ 
/* 236 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
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
/*     */ 
/*     */ 
/*     */   public static String getPEUserName(Object vwSession)
/*     */   {
/* 256 */     Tracer.traceMethodEntry(new Object[] { vwSession });
/* 257 */     if (!CLASS_VWSession.isInstance(vwSession))
/*     */     {
/* 259 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_BAD_VWSESSION_PARAMETER, new Object[0]);
/*     */     }
/*     */     
/* 262 */     if (!isPESupportAvailable())
/*     */     {
/* 264 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_NOT_AVAILABLE, new Object[0]);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 269 */       Object vwUserInfoObj = METHOD_VWSession_fetchCurrentUserInfo.invoke(vwSession, new Object[0]);
/* 270 */       if (vwUserInfoObj == null)
/*     */       {
/* 272 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PE_API_CANNOT_GET_VWUSERINFO, new Object[0]);
/*     */       }
/*     */       
/* 275 */       String result = (String)METHOD_VWUserInfo_getName.invoke(vwUserInfoObj, new Object[0]);
/*     */       
/* 277 */       Tracer.traceMethodExit(new Object[] { result });
/* 278 */       return result;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 282 */       Throwable actualEx = ex;
/* 283 */       if ((ex instanceof InvocationTargetException))
/*     */       {
/* 285 */         actualEx = ((InvocationTargetException)ex).getTargetException();
/*     */       }
/*     */       
/* 288 */       if (CLASS_VWException.isInstance(actualEx))
/*     */       {
/* 290 */         throw RMRuntimeException.createRMRuntimeException(actualEx, RMErrorCode.RAL_PE_API_VWEXCEPTION_OCCURRED, new Object[] { ex.getLocalizedMessage() });
/*     */       }
/*     */       
/*     */ 
/* 294 */       throw P8CE_Util.processJaceException(actualEx, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALPESupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */