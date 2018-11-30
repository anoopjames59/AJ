/*     */ package com.ibm.ier.plugin.tasks;
/*     */ 
/*     */ import com.cognos.org.apache.axis.AxisFault;
/*     */ import com.filenet.api.util.UserContext;
/*     */ import com.ibm.ecm.task.TaskLogger;
/*     */ import com.ibm.ecm.task.commonj.work.BaseTask;
/*     */ import com.ibm.ecm.task.entities.Task;
/*     */ import com.ibm.ecm.task.entities.TaskExecutionRecord;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.tasks.util.TaskUtil;
/*     */ import com.ibm.jarm.api.exception.MessageInfo;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.RMUserContext;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseIERTask
/*     */   extends BaseTask
/*     */ {
/*  30 */   public Logger logger = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   protected JSONObject results = null;
/*     */   
/*  37 */   protected JSONObject taskInfo = null;
/*     */   
/*     */   public BaseIERTask(Task task, File file) throws IOException {
/*  40 */     super(task, null);
/*     */     
/*  42 */     JSONObject taskInfo = JSONObject.parse(task.getTaskInfo());
/*  43 */     String logDirectory = (String)taskInfo.get("logDirectory");
/*  44 */     if (logDirectory != null) {
/*  45 */       this.logDirectory = new File(logDirectory);
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
/*     */   public String getEmailNotificationContent(long status)
/*     */   {
/*  64 */     if (this.task.getNotifyInfo() != null) {
/*  65 */       if (status == 16L)
/*  66 */         return MessageResources.getLocalizedMessage("task.started", new Object[] { this.task.getName() });
/*  67 */       if (status == 128L)
/*  68 */         return MessageResources.getLocalizedMessage("task.failed", new Object[] { this.task.getName() });
/*  69 */       if (status == 32L) {
/*  70 */         return MessageResources.getLocalizedMessage("task.completed", new Object[] { this.task.getName() });
/*     */       }
/*     */     }
/*     */     
/*  74 */     return null;
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
/*     */   public String getEmailSubject(long status)
/*     */   {
/*  92 */     if (status == 16L)
/*  93 */       return MessageResources.getLocalizedMessage("task.started", new Object[] { this.task.getName() });
/*  94 */     if (status == 128L)
/*  95 */       return MessageResources.getLocalizedMessage("task.failed", new Object[] { this.task.getName() });
/*  96 */     if (status == 32L) {
/*  97 */       return MessageResources.getLocalizedMessage("task.completed", new Object[] { this.task.getName() });
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   public void performTask()
/*     */   {
/* 104 */     String methodName = "performTask";
/* 105 */     String className = BaseIERTask.class.getName();
/* 106 */     this.logger = getLogger();
/*     */     try
/*     */     {
/* 109 */       TaskLogger.severe(className, methodName, MessageResources.getLocalizedMessage("task.started", new Object[] { this.task.getId() + " " + this.task.getHandlerName() }), null);
/*     */       
/* 111 */       this.taskInfo = JSONObject.parse(this.task.getTaskInfo());
/* 112 */       initializeResults();
/*     */       
/*     */ 
/* 115 */       Subject subject = TaskUtil.performTaskLogin(this.taskInfo, this.logger);
/* 116 */       UserContext.get().pushSubject(subject);
/* 117 */       RMUserContext.get().setSubject(subject);
/*     */       
/*     */ 
/* 120 */       TaskLogger.fine(className, methodName, "Starting task implementation");
/* 121 */       performBaseTask();
/* 122 */       TaskLogger.fine(className, methodName, "Finishing performing task.");
/*     */       
/* 124 */       saveTaskInfo();
/*     */       
/* 126 */       TaskLogger.severe(className, methodName, MessageResources.getLocalizedMessage("task.completed", new Object[] { this.task.getId() }), null);
/*     */     }
/*     */     catch (Throwable exp)
/*     */     {
/* 130 */       TaskLogger.fine(className, methodName, "An error occurred:" + exp.toString());
/* 131 */       handleErrors(exp);
/*     */     }
/*     */     finally {
/* 134 */       if (UserContext.get().getSubject() != null)
/* 135 */         UserContext.get().popSubject();
/*     */     }
/*     */   }
/*     */   
/*     */   private void initializeResults() throws Exception {
/* 140 */     if ((this.taskExecutionRecord != null) && (this.task.getTaskMode().longValue() == 1L)) {
/* 141 */       String savedResults = this.taskExecutionRecord.getTaskExecutionInfo();
/* 142 */       if ((savedResults != null) && (savedResults.length() != 0)) {
/* 143 */         this.results = JSONObject.parse(savedResults);
/*     */       }
/*     */     } else {
/* 146 */       Object savedResults = this.taskInfo.get("results");
/* 147 */       if (savedResults != null) {
/* 148 */         this.results = ((JSONObject)savedResults);
/*     */       }
/*     */     }
/*     */     
/* 152 */     if (this.results == null)
/* 153 */       this.results = new JSONObject();
/*     */   }
/*     */   
/*     */   public void saveTaskInfo() throws Exception {
/* 157 */     String methodName = "saveTaskInfo";
/* 158 */     String className = BaseIERTask.class.getName();
/*     */     
/*     */ 
/*     */ 
/* 162 */     String resultsString = this.results.serialize();
/* 163 */     TaskLogger.severe(className, methodName, MessageResources.getLocalizedMessage("task.results", new Object[] { resultsString }), null);
/*     */     
/* 165 */     if ((this.taskExecutionRecord != null) && (this.task.getTaskMode().longValue() == 1L)) {
/* 166 */       this.taskExecutionRecord.setTaskExecutionInfo(this.results.serialize());
/* 167 */       updateTaskExecutionRecord();
/*     */       
/* 169 */       if (this.taskInfo.get("taskInstanceCount") != null) {
/* 170 */         long taskInstanceCount = ((Long)this.taskInfo.get("taskInstanceCount")).longValue();
/* 171 */         taskInstanceCount += 1L;
/* 172 */         this.taskInfo.put("taskInstanceCount", new Long(taskInstanceCount));
/*     */       } else {
/* 174 */         this.taskInfo.put("taskInstanceCount", new Long(1L));
/*     */       }
/*     */     }
/*     */     else {
/* 178 */       this.taskInfo.put("results", this.results);
/*     */     }
/* 180 */     this.task.setTaskInfo(this.taskInfo.serialize());
/*     */     
/* 182 */     if (this.logger.isLoggable(Level.FINE)) {
/* 183 */       TaskLogger.fine(className, methodName, "TaskId: " + this.task.getId() + " results: " + this.results.serialize());
/*     */     }
/*     */     
/* 186 */     updateTask();
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void performBaseTask()
/*     */     throws Throwable;
/*     */   
/*     */ 
/*     */   public String parseFaultReason(String axisFaultReason, String startString, String endString)
/*     */   {
/* 196 */     int start = axisFaultReason.indexOf(startString);
/* 197 */     int end = axisFaultReason.indexOf(endString);
/* 198 */     if ((start != -1) && (end != -1)) {
/* 199 */       return " " + axisFaultReason.substring(start + startString.length(), end - 1);
/*     */     }
/* 201 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleErrors(Throwable exp)
/*     */   {
/* 210 */     String methodName = "handleErrors";
/* 211 */     String className = BaseIERTask.class.getName();
/* 212 */     Exception runtimeError = null;
/*     */     
/* 214 */     setTaskStatus(128L);
/* 215 */     TaskLogger.severe(className, methodName, exp.getLocalizedMessage(), exp);
/* 216 */     exp.printStackTrace();
/*     */     
/* 218 */     TaskLogger.fine(className, methodName, "Attempting to handle error");
/* 219 */     if ((exp != null) && (exp.getMessage() != null)) {
/* 220 */       StringBuilder message = new StringBuilder(exp.getMessage());
/*     */       
/* 222 */       TaskLogger.fine(className, methodName, "Attempting to handle error with error message");
/*     */       try
/*     */       {
/* 225 */         if ((exp instanceof AxisFault)) {
/* 226 */           TaskLogger.fine(className, methodName, "This is an axis fault exception");
/*     */           
/* 228 */           AxisFault fault = (AxisFault)exp;
/* 229 */           String axisFaultReason = fault.dumpToString();
/* 230 */           TaskLogger.fine(className, methodName, "Axis fault exception: " + axisFaultReason);
/*     */           
/* 232 */           String reason = parseFaultReason(axisFaultReason, "<ns1:messageString xsi:type=\"xs:string\">", "</ns1:messageString>");
/* 233 */           if (reason == null) {
/* 234 */             reason = parseFaultReason(axisFaultReason, "<ns1:messageString>", "</ns1:messageString>");
/*     */             
/* 236 */             if (reason == null) {
/* 237 */               reason = parseFaultReason(axisFaultReason, "<messageString>", "</messageString>");
/*     */             }
/*     */           }
/* 240 */           if (reason != null) {
/* 241 */             message.append(" " + reason);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Throwable e) {
/* 246 */         TaskLogger.fine(className, methodName, "Getting the axis fault exception caused an error: " + e.getLocalizedMessage());
/* 247 */         TaskLogger.severe(className, methodName, e.getLocalizedMessage(), e);
/*     */       }
/*     */       
/* 250 */       TaskLogger.fine(className, methodName, "message error: " + message.toString());
/*     */       
/* 252 */       if (((exp instanceof Exception)) && (!(exp instanceof AxisFault))) {
/* 253 */         TaskLogger.fine(className, methodName, "message error is exception");
/* 254 */         runtimeError = (Exception)exp;
/*     */       }
/*     */       else {
/* 257 */         TaskLogger.fine(className, methodName, "message error is just throwable or axis fault exception");
/* 258 */         runtimeError = new Exception(message.toString(), exp);
/*     */       }
/*     */     }
/*     */     else {
/* 262 */       TaskLogger.fine(className, methodName, "Attempting to handle error without error message");
/*     */       
/* 264 */       if ((exp instanceof Exception)) {
/* 265 */         runtimeError = (Exception)exp;
/*     */       } else {
/* 267 */         runtimeError = new Exception(exp.getMessage(), exp);
/*     */       }
/*     */     }
/* 270 */     StringBuilder message = new StringBuilder();
/*     */     
/* 272 */     TaskLogger.fine(className, methodName, "Finished assigning run time error");
/*     */     
/* 274 */     if (runtimeError != null) {
/* 275 */       message.append(runtimeError.toString());
/*     */     }
/*     */     
/*     */ 
/* 279 */     if ((runtimeError instanceof RMRuntimeException)) {
/* 280 */       RMRuntimeException rmException = (RMRuntimeException)runtimeError;
/* 281 */       if (rmException.getMessageInfo() != null) {
/* 282 */         MessageInfo info = rmException.getMessageInfo();
/* 283 */         if (info.getExplanation() != null) {
/* 284 */           message.append("\n" + info.getExplanation());
/*     */         }
/*     */         
/* 287 */         if (info.getAction() != null) {
/* 288 */           message.append("\n" + info.getAction());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 294 */     if ((runtimeError != null) && (runtimeError.getCause() != null)) {
/* 295 */       message.append("\n" + runtimeError.getCause().toString());
/*     */     }
/* 297 */     addError(new Long(0L), message.toString());
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\BaseIERTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */