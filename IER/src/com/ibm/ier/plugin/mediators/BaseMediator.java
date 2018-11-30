/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.nls.MessageResources.MessageCodeType;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.jarm.api.exception.MessageInfo;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public class BaseMediator
/*     */   implements Serializable
/*     */ {
/*     */   private static final String CATEGORY_MESSAGES = "messages";
/*     */   private static final String CATEGORY_WARNINGS = "warnings";
/*     */   private static final String CATEGORY_ERRORS = "errors";
/*     */   public static final String METHOD_TOJSONOBJ = "toJSONObject";
/*     */   protected Throwable throwable;
/*     */   protected ArrayList<Message> messages;
/*     */   protected ArrayList<Message> warnings;
/*     */   protected ArrayList<Message> errors;
/*     */   protected IERBaseService baseService;
/*     */   protected HttpServletRequest servletRequest;
/*     */   protected JSONObject completedJSONResponseObject;
/*     */   private static final long serialVersionUID = 2L;
/*     */   
/*     */   protected static final class Message
/*     */   {
/*     */     public String number;
/*     */     public String text;
/*     */     public String explanation;
/*     */     public String userResponse;
/*     */     public String adminResponse;
/*     */     
/*     */     public JSONObject toJSONObject()
/*     */     {
/*  84 */       JSONObject jsonObject = new JSONObject();
/*  85 */       jsonObject.put("number", String.valueOf(this.number));
/*  86 */       jsonObject.put("text", this.text);
/*  87 */       jsonObject.put("explanation", this.explanation);
/*  88 */       jsonObject.put("userResponse", this.userResponse);
/*  89 */       jsonObject.put("adminResponse", this.adminResponse);
/*     */       
/*  91 */       jsonObject.put("adminReponse", this.adminResponse);
/*  92 */       return jsonObject;
/*     */     }
/*     */     
/*     */     public static Message fromJSONObject(JSONObject jsonObject) {
/*  96 */       Message message = new Message();
/*  97 */       message.number = ((String)jsonObject.get("number"));
/*  98 */       message.text = ((String)jsonObject.get("text"));
/*  99 */       message.explanation = ((String)jsonObject.get("explanation"));
/* 100 */       message.userResponse = ((String)jsonObject.get("userResponse"));
/* 101 */       message.adminResponse = ((String)jsonObject.get("adminResponse"));
/* 102 */       return message;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseMediator(IERBaseService baseService, HttpServletRequest request)
/*     */   {
/* 112 */     this.baseService = baseService;
/* 113 */     this.servletRequest = request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseMediator() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBaseService(IERBaseService baseService)
/*     */   {
/* 127 */     this.baseService = baseService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHttpServletRequest(HttpServletRequest request)
/*     */   {
/* 135 */     this.servletRequest = request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpServletRequest getHttpServletRequest()
/*     */   {
/* 143 */     return this.servletRequest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IERBaseService getBaseService()
/*     */   {
/* 151 */     return this.baseService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCompletedJSONResponseObject(JSONObject json)
/*     */   {
/* 161 */     this.completedJSONResponseObject = json;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setErrors(ArrayList<Message> errors)
/*     */   {
/* 170 */     this.errors = errors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setMessages(ArrayList<Message> messages)
/*     */   {
/* 179 */     this.messages = messages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setWarnings(ArrayList<Message> warnings)
/*     */   {
/* 188 */     this.warnings = warnings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Message> getErrors()
/*     */   {
/* 200 */     if (this.errors == null)
/* 201 */       this.errors = new ArrayList();
/* 202 */     return this.errors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Message> getMessages()
/*     */   {
/* 214 */     if (this.messages == null)
/* 215 */       this.messages = new ArrayList();
/* 216 */     return this.messages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Message> getWarnings()
/*     */   {
/* 228 */     if (this.warnings == null)
/* 229 */       this.warnings = new ArrayList();
/* 230 */     return this.warnings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Throwable getThrowable()
/*     */   {
/* 239 */     return this.throwable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThrowable(Throwable throwable)
/*     */   {
/* 248 */     this.throwable = throwable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMessage(MessageCode code, Object... inserts)
/*     */   {
/* 258 */     getMessages().add(createMessage(code, inserts));
/*     */   }
/*     */   
/*     */   public void addMessage(String id, String text, String explanation, String userResponse, String adminResponse) {
/* 262 */     getMessages().add(createMessage(id, text, explanation, userResponse, adminResponse));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addWarning(MessageCode code, Object... inserts)
/*     */   {
/* 269 */     getWarnings().add(createMessage(code, inserts));
/*     */   }
/*     */   
/*     */   public void addWarning(String id, String text, String explanation, String userResponse, String adminResponse) {
/* 273 */     getWarnings().add(createMessage(id, text, explanation, userResponse, adminResponse));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addError(MessageCode code, Object... inserts)
/*     */   {
/* 285 */     getErrors().add(createMessage(code, inserts));
/*     */   }
/*     */   
/*     */   public void addError(String id, String text, String explanation, String userResponse, String adminResponse) {
/* 289 */     getErrors().add(createMessage(id, text, explanation, userResponse, adminResponse));
/*     */   }
/*     */   
/*     */   public void addError(MessageCode code, RMRuntimeException exp) {
/* 293 */     getErrors().add(createMessage(code, exp));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONObject toJSONObject()
/*     */     throws Exception
/*     */   {
/* 302 */     return new JSONObject();
/*     */   }
/*     */   
/*     */   public JSONObject toFinalJSONObject() throws Exception {
/* 306 */     JSONObject jsonObject = this.completedJSONResponseObject;
/* 307 */     if (jsonObject == null) {
/* 308 */       jsonObject = toJSONObject();
/*     */     }
/* 310 */     addMessagesJSON(jsonObject);
/* 311 */     return jsonObject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Message createMessage(MessageCode code, Object... inserts)
/*     */   {
/* 322 */     return createMessage(MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.ID, inserts), MessageResources.getLocalizedMessage(code, inserts), MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.EXPLANATION, inserts), MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.USER_RESPONSE, inserts), MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.ADMIN_RESPONSE, inserts));
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
/*     */   protected Message createMessage(MessageCode code, RMRuntimeException jarmExp)
/*     */   {
/* 337 */     MessageInfo messageInfo = jarmExp.getMessageInfo();
/*     */     
/*     */ 
/* 340 */     String adminResponse = null;
/* 341 */     if (jarmExp != null) {
/* 342 */       adminResponse = MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.ADMIN_RESPONSE, new Object[0]);
/*     */     }
/*     */     
/* 345 */     if (adminResponse == null) {
/* 346 */       adminResponse = messageInfo.getFormattedMessage();
/*     */     }
/* 348 */     return createMessage(MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.ID, new Object[0]), MessageResources.getLocalizedMessage(code, new Object[] { messageInfo.getFormattedMessage() }), MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.EXPLANATION, new Object[0]), MessageResources.getLocalizedMessage(code, MessageResources.MessageCodeType.USER_RESPONSE, new Object[0]), adminResponse);
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
/*     */   protected Message createMessage(String id, String text, String explanation, String userResponse, String adminResponse)
/*     */   {
/* 366 */     Message message = new Message();
/* 367 */     if (text != null) {
/* 368 */       message.text = text;
/*     */     }
/* 370 */     if (id != null) {
/* 371 */       message.number = id;
/*     */     }
/* 373 */     if (explanation != null) {
/* 374 */       message.explanation = explanation;
/*     */     }
/* 376 */     if (userResponse != null) {
/* 377 */       message.userResponse = userResponse;
/*     */     }
/* 379 */     if (adminResponse != null) {
/* 380 */       message.adminResponse = adminResponse;
/*     */     }
/* 382 */     return message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void sendErrorResponse(HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/* 390 */     if (this.errors != null) {
/* 391 */       StringBuffer errorMessage = new StringBuffer();
/* 392 */       Iterator<Message> iterator = this.errors.iterator();
/* 393 */       while (iterator.hasNext()) {
/* 394 */         Message message = (Message)iterator.next();
/*     */         
/* 396 */         errorMessage.append("FNRRM").append(message.number).append(": ").append(message.text).append("\n");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 401 */       response.sendError(500, errorMessage.toString());
/*     */     } else {
/* 403 */       response.sendError(500);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMessagesJSON(JSONObject jsonObject)
/*     */   {
/* 415 */     if ((this.messages != null) && (this.messages.size() > 0)) {
/* 416 */       JSONArray jsonMessages = new JSONArray();
/* 417 */       Iterator iterator = this.messages.iterator();
/* 418 */       while (iterator.hasNext()) {
/* 419 */         Object message = iterator.next();
/* 420 */         if ((message instanceof Message)) {
/* 421 */           jsonMessages.add(((Message)message).toJSONObject());
/*     */         } else {
/* 423 */           jsonMessages.add(message);
/*     */         }
/*     */       }
/* 426 */       jsonObject.put("messages", jsonMessages);
/*     */     }
/* 428 */     if ((this.warnings != null) && (this.warnings.size() > 0)) {
/* 429 */       JSONArray jsonWarnings = new JSONArray();
/* 430 */       Iterator iterator = this.warnings.iterator();
/* 431 */       while (iterator.hasNext()) {
/* 432 */         Object message = iterator.next();
/* 433 */         if ((message instanceof Message)) {
/* 434 */           jsonWarnings.add(((Message)message).toJSONObject());
/*     */         } else {
/* 436 */           jsonWarnings.add(message);
/*     */         }
/*     */       }
/* 439 */       jsonObject.put("warnings", jsonWarnings);
/*     */     }
/* 441 */     if ((this.errors != null) && (this.errors.size() > 0)) {
/* 442 */       JSONArray jsonErrors = new JSONArray();
/* 443 */       Iterator iterator = this.errors.iterator();
/* 444 */       while (iterator.hasNext()) {
/* 445 */         Object message = iterator.next();
/* 446 */         if ((message instanceof Message)) {
/* 447 */           jsonErrors.add(((Message)message).toJSONObject());
/*     */         } else {
/* 449 */           jsonErrors.add(message);
/*     */         }
/*     */       }
/* 452 */       jsonObject.put("errors", jsonErrors);
/*     */     }
/*     */     
/* 455 */     String stackTrace = null;
/* 456 */     if (this.throwable != null) {
/* 457 */       stackTrace = throwableToString(this.throwable);
/* 458 */       jsonObject.put("throwable", stackTrace);
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
/*     */   public final void writeJSON(Writer writer)
/*     */     throws Exception
/*     */   {
/* 472 */     String jsonString = toFinalJSONObject().toString();
/* 473 */     writer.write(jsonString);
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
/*     */   protected static ArrayList toMessageListFromJSON(JSONArray jsonArray)
/*     */   {
/* 486 */     ArrayList list = new ArrayList();
/* 487 */     Iterator iterator = jsonArray.iterator();
/* 488 */     while (iterator.hasNext()) {
/* 489 */       Object item = iterator.next();
/* 490 */       Object value = null;
/* 491 */       if ((item instanceof JSONObject)) {
/* 492 */         value = Message.fromJSONObject((JSONObject)item);
/*     */       } else {
/* 494 */         value = item;
/*     */       }
/* 496 */       list.add(value);
/*     */     }
/* 498 */     return list;
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
/*     */   private String stackTraceToString(StackTraceElement[] elements)
/*     */   {
/* 511 */     StringBuffer bufferString = new StringBuffer();
/* 512 */     for (int iCounter = 0; iCounter < elements.length; iCounter++) {
/* 513 */       bufferString.append(elements[iCounter].toString()).append("\n");
/*     */     }
/* 515 */     return bufferString.toString();
/*     */   }
/*     */   
/*     */   private String throwableToString(Throwable t) {
/* 519 */     StringBuffer buffer = new StringBuffer();
/*     */     
/* 521 */     buffer.append(t.getClass().getName()).append(": ").append(t.getMessage()).append("\n");
/* 522 */     StackTraceElement[] elements = t.getStackTrace();
/*     */     
/* 524 */     for (int iCounter = 0; iCounter < elements.length; iCounter++) {
/* 525 */       buffer.append("\t at ").append(elements[iCounter].toString()).append("\n");
/*     */     }
/*     */     
/* 528 */     Throwable cause = t.getCause();
/*     */     
/*     */ 
/* 531 */     if (cause != null) {
/* 532 */       buffer.append("Caused by: ");
/* 533 */       buffer.append(throwableToString(cause));
/*     */     }
/*     */     
/* 536 */     return buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fromJSONObject(JSONObject jsonObject)
/*     */   {
/* 546 */     if (jsonObject.containsKey("messages")) {
/* 547 */       JSONArray messagesJsonObject = (JSONArray)jsonObject.get("messages");
/* 548 */       setMessages(toMessageListFromJSON(messagesJsonObject));
/*     */     }
/* 550 */     if (jsonObject.containsKey("warnings")) {
/* 551 */       JSONArray warningsJsonObject = (JSONArray)jsonObject.get("warnings");
/* 552 */       setWarnings(toMessageListFromJSON(warningsJsonObject));
/*     */     }
/* 554 */     if (jsonObject.containsKey("errors")) {
/* 555 */       JSONArray errorsJsonObject = (JSONArray)jsonObject.get("errors");
/* 556 */       setErrors(toMessageListFromJSON(errorsJsonObject));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void addStringArrayToJSON(JSONObject jsonObject, String label, String[] stringArray) {
/* 561 */     if ((jsonObject != null) && (stringArray != null))
/*     */     {
/*     */ 
/* 564 */       JSONArray jsonStringArray = new JSONArray();
/* 565 */       for (int iArrayItemCounter = 0; iArrayItemCounter < stringArray.length; iArrayItemCounter++) {
/* 566 */         jsonStringArray.add(iArrayItemCounter, stringArray[iArrayItemCounter]);
/*     */       }
/* 568 */       jsonObject.put(label, jsonStringArray);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\BaseMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */