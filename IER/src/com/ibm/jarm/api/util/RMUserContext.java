/*     */ package com.ibm.jarm.api.util;
/*     */ 
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMFactory;
/*     */ import com.ibm.jarm.ral.RALService;
/*     */ import java.security.Principal;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RMUserContext
/*     */ {
/*  20 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String P8_STANZA_EJB = "FileNetP8";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String P8_STANZA_WSI = "FileNetP8WSI";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private static ThreadLocal<RMUserContext> TLS = new ThreadLocal();
/*     */   
/*     */ 
/*     */   private Locale locale;
/*     */   
/*     */   private Subject subject;
/*     */   
/*     */   private int creatorThreadIdentity;
/*     */   
/*     */ 
/*     */   protected RMUserContext()
/*     */   {
/*  50 */     this.creatorThreadIdentity = System.identityHashCode(Thread.currentThread());
/*  51 */     if (Tracer.isMinimumTraceEnabled())
/*     */     {
/*  53 */       Tracer.traceMinimumMsg("RMUserContext CTOR on Thread {0}.", new Object[] { Integer.valueOf(this.creatorThreadIdentity) });
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
/*     */   public void setLocale(Locale locale)
/*     */   {
/*  66 */     this.locale = locale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/*  77 */     return this.locale != null ? this.locale : Locale.getDefault();
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
/*     */   public void setSubject(Subject subject)
/*     */   {
/*  90 */     this.subject = subject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Subject getSubject()
/*     */   {
/*  99 */     return this.subject;
/*     */   }
/*     */   
/*     */   int getCreatorThreadIdentity()
/*     */   {
/* 104 */     return this.creatorThreadIdentity;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Subject createSubject(DomainConnection conn, String username, String password, String stanzaName)
/*     */   {
/* 133 */     Tracer.traceMethodEntry(new Object[] { conn, username, password, stanzaName });
/*     */     
/* 135 */     RALService ralService = RMFactory.getRALService(conn);
/* 136 */     Subject result = ralService.createSubject(conn, username, password, stanzaName);
/*     */     
/* 138 */     Tracer.traceMethodExit(new Object[] { result });
/* 139 */     return result;
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
/*     */   public static RMUserContext get()
/*     */   {
/* 153 */     Tracer.traceMethodEntry(new Object[0]);
/* 154 */     RMUserContext uc = (RMUserContext)TLS.get();
/* 155 */     if (uc == null)
/*     */     {
/* 157 */       uc = new RMUserContext();
/* 158 */       TLS.set(uc);
/*     */     }
/*     */     
/* 161 */     Tracer.traceMethodExit(new Object[] { uc });
/* 162 */     return uc;
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
/*     */   public static void set(RMUserContext uc)
/*     */   {
/* 176 */     Tracer.traceMethodEntry(new Object[] { uc });
/*     */     
/* 178 */     int currentThreadIdentity = System.identityHashCode(Thread.currentThread());
/*     */     
/* 180 */     if ((uc != null) && (uc.getCreatorThreadIdentity() != currentThreadIdentity))
/*     */     {
/* 182 */       RMUserContext ucClone = uc.cloneContext();
/* 183 */       TLS.set(ucClone);
/*     */     }
/*     */     else
/*     */     {
/* 187 */       TLS.set(uc);
/*     */     }
/*     */     
/* 190 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized RMUserContext cloneContext()
/*     */   {
/* 201 */     Tracer.traceMethodEntry(new Object[0]);
/* 202 */     RMUserContext uc = new RMUserContext();
/*     */     
/*     */ 
/* 205 */     uc.locale = this.locale;
/* 206 */     uc.subject = this.subject;
/*     */     
/* 208 */     Tracer.traceMethodExit(new Object[] { uc });
/* 209 */     return uc;
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
/*     */   public static String getPrincipals(Subject subject)
/*     */   {
/* 225 */     Tracer.traceMethodEntry(new Object[] { subject });
/* 226 */     String result = null;
/*     */     
/* 228 */     if ((subject == null) || (subject.getPrincipals() == null) || (subject.getPrincipals().isEmpty()))
/*     */     {
/* 230 */       result = "";
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 235 */       StringBuilder sb = new StringBuilder();
/* 236 */       int i = 0;
/* 237 */       for (Principal principal : subject.getPrincipals())
/*     */       {
/* 239 */         if (i > 0)
/* 240 */           sb.append("; ");
/* 241 */         sb.append("Principal[" + i++ + "]=" + principal.toString());
/* 242 */         i++;
/*     */       }
/* 244 */       result = sb.toString();
/*     */     }
/*     */     
/* 247 */     Tracer.traceMethodExit(new Object[] { result });
/* 248 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\util\RMUserContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */