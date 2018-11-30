/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.collection.EngineSet;
/*     */ import com.filenet.api.constants.VersionStatus;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.IndependentlyPersistableObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.security.User;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.search.util.Comment;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class P8SocialCollaborationProxy
/*     */ {
/*     */   private static Boolean jaceUpToDate;
/*     */   
/*     */   public static boolean isCeClientUpToDate()
/*     */   {
/*  31 */     if (jaceUpToDate != null) {
/*  32 */       return jaceUpToDate.booleanValue();
/*     */     }
/*     */     try {
/*  35 */       Class.forName("com.filenet.api.core.CmAbstractPersistable");
/*  36 */       jaceUpToDate = Boolean.valueOf(true);
/*     */     } catch (Exception e) {
/*  38 */       jaceUpToDate = Boolean.valueOf(false);
/*     */     }
/*     */     
/*  41 */     return jaceUpToDate.booleanValue();
/*     */   }
/*     */   
/*     */   public static Comment addComment(P8Connection connection, Document document, String commentText) {
/*  45 */     if (!isDocumentCommentSupported(connection, document)) {
/*  46 */       return null;
/*     */     }
/*  48 */     IndependentlyPersistableObject comment = P8CommentsUtil.addComment(document, commentText);
/*     */     
/*  50 */     return createComment(connection, comment);
/*     */   }
/*     */   
/*     */   public static Comment editComment(P8Connection connection, Document document, String commentId, String commentText) {
/*  54 */     if (!isDocumentCommentSupported(connection, document)) {
/*  55 */       return null;
/*     */     }
/*  57 */     IndependentlyPersistableObject comment = P8CommentsUtil.editComment(document, commentId, commentText);
/*     */     
/*  59 */     return createComment(connection, comment);
/*     */   }
/*     */   
/*     */   public static void deleteComment(P8Connection connection, Document document, String commentId) {
/*  63 */     if (!isDocumentCommentSupported(connection, document)) {
/*  64 */       return;
/*     */     }
/*  66 */     P8CommentsUtil.deleteComment(document, commentId);
/*     */   }
/*     */   
/*     */   private static boolean isDocumentCommentSupported(P8Connection connection, Document document) {
/*  70 */     if (!isCeClientUpToDate())
/*  71 */       return false;
/*  72 */     ObjectStore os = P8Util.getObjectStore(connection, document.getObjectReference().getObjectStoreIdentity());
/*  73 */     if (!connection.isDocumentCommentSupported(os)) {
/*  74 */       return false;
/*     */     }
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   private static Comment createComment(P8Connection connection, IndependentlyPersistableObject commentObject) {
/*  80 */     Comment comment = new Comment();
/*  81 */     Properties props = commentObject.getProperties();
/*  82 */     comment.setCommentText(props.getStringValue("ClbCommentText"));
/*  83 */     comment.setDateAdded(props.getDateTimeValue("DateCreated"));
/*  84 */     comment.setId(props.getIdValue("Id").toString());
/*  85 */     comment.setItemVersion(props.getInteger32Value("ClbDocumentMajorVersion") + "." + props.getInteger32Value("ClbDocumentMinorVersion"));
/*  86 */     comment.setOriginator(props.getStringValue("Creator"));
/*     */     try {
/*  88 */       String userId = props.getStringValue("ClbOriginator");
/*  89 */       User user = connection.retrieveUser(userId);
/*  90 */       comment.setOriginator(user.get_ShortName());
/*  91 */       comment.setOriginatorDisplayName(user.get_DisplayName());
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/*     */     try
/*     */     {
/*  97 */       Document document = (Document)props.getEngineObjectValue("ClbCommentedDocument");
/*  98 */       VersionStatus status = document.get_VersionStatus();
/*  99 */       comment.setItemVersionStatus(status.getValue());
/* 100 */       boolean currentVersion = document.get_IsCurrentVersion().booleanValue();
/* 101 */       comment.setItemCurrentVersion(currentVersion);
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/*     */ 
/* 106 */     Integer access = commentObject.getAccessAllowed();
/* 107 */     comment.setEditable((access.intValue() & 0x2) > 0);
/* 108 */     comment.setDeletable((access.intValue() & 0x10000) > 0);
/*     */     
/* 110 */     return comment;
/*     */   }
/*     */   
/*     */   public static List<Comment> retrieveComments(P8Connection connection, Document document) {
/* 114 */     if (!isDocumentCommentSupported(connection, document)) {
/* 115 */       return null;
/*     */     }
/* 117 */     List<Comment> comments = new ArrayList();
/* 118 */     EngineSet rows = P8CommentsUtil.retrieveComments(document);
/* 119 */     Iterator<IndependentlyPersistableObject> i = rows.iterator();
/* 120 */     while (i.hasNext()) {
/* 121 */       IndependentlyPersistableObject comment = (IndependentlyPersistableObject)i.next();
/* 122 */       comments.add(createComment(connection, comment));
/*     */     }
/*     */     
/* 125 */     return comments;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8SocialCollaborationProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */