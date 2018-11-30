/*    */ package com.ibm.ier.plugin.search.p8;
/*    */ 
/*    */ import com.filenet.api.collection.EngineSet;
/*    */ import com.filenet.api.constants.RefreshMode;
/*    */ import com.filenet.api.core.CmAbstractPersistable;
/*    */ import com.filenet.api.core.Document;
/*    */ import com.filenet.api.core.Factory.CmAbstractPersistable;
/*    */ import com.filenet.api.core.ObjectReference;
/*    */ import com.filenet.api.core.ObjectStore;
/*    */ import com.filenet.api.core.VersionSeries;
/*    */ import com.filenet.api.property.FilterElement;
/*    */ import com.filenet.api.property.Properties;
/*    */ import com.filenet.api.property.PropertyFilter;
/*    */ import com.filenet.api.query.SearchSQL;
/*    */ import com.filenet.api.query.SearchScope;
/*    */ import com.filenet.api.util.Id;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class P8CommentsUtil
/*    */ {
/*    */   static CmAbstractPersistable addComment(Document document, String commentText)
/*    */   {
/* 27 */     ObjectStore os = document.getObjectStore();
/* 28 */     CmAbstractPersistable comment = Factory.CmAbstractPersistable.createInstance(os, "ClbDocumentComment");
/* 29 */     comment.getProperties().putObjectValue("ClbCommentedDocument", document);
/* 30 */     comment.getProperties().putValue("ClbCommentText", commentText);
/* 31 */     comment.save(RefreshMode.REFRESH);
/*    */     
/* 33 */     return comment;
/*    */   }
/*    */   
/*    */   static CmAbstractPersistable editComment(Document document, String commentId, String commentText) {
/* 37 */     ObjectStore os = document.getObjectStore();
/* 38 */     CmAbstractPersistable comment = Factory.CmAbstractPersistable.fetchInstance(os, "ClbDocumentComment", new Id(commentId), null);
/* 39 */     comment.getProperties().putValue("ClbCommentText", commentText);
/* 40 */     comment.save(RefreshMode.REFRESH);
/*    */     
/* 42 */     return comment;
/*    */   }
/*    */   
/*    */   static void deleteComment(Document document, String commentId) {
/* 46 */     ObjectStore os = document.getObjectStore();
/* 47 */     CmAbstractPersistable comment = Factory.CmAbstractPersistable.fetchInstance(os, "ClbDocumentComment", new Id(commentId), null);
/* 48 */     comment.delete();
/* 49 */     comment.save(RefreshMode.NO_REFRESH);
/*    */   }
/*    */   
/*    */   static EngineSet retrieveComments(Document document) {
/* 53 */     ObjectStore os = document.getObjectStore();
/* 54 */     String vsId = document.get_VersionSeries().getObjectReference().getObjectIdentity();
/* 55 */     SearchSQL search = new SearchSQL("SELECT Id, ClbCommentText, ClbOriginator, ClbDocumentMajorVersion, ClbDocumentMinorVersion, ClbCommentedDocument, Creator, DateCreated FROM ClbDocumentComment WHERE ClbCommentedVersionSeries = Object(" + vsId + ") AND ClbInReplyTo IS NULL ORDER BY DateCreated");
/* 56 */     SearchScope searchScope = new SearchScope(os);
/* 57 */     PropertyFilter propertyFilter = new PropertyFilter();
/* 58 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "ClbCommentedDocument", null));
/* 59 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "VersionStatus", null));
/* 60 */     propertyFilter.addIncludeProperty(new FilterElement(Integer.valueOf(2), null, null, "IsCurrentVersion", null));
/*    */     
/* 62 */     return searchScope.fetchObjects(search, Integer.valueOf(0), propertyFilter, Boolean.FALSE);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8CommentsUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */