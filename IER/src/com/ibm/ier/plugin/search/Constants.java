/*     */ package com.ibm.ier.plugin.search;
/*     */ 
/*     */ 
/*     */ public abstract interface Constants
/*     */   extends ParamConstants
/*     */ {
/*     */   public static final String COPY_RIGHT_NOTICE = "Licensed Materials - Property of IBM IBM DB2 Content Manager OnDemand for Multiplatforms V8.3 (program number 5724-J33) IBM DB2 Content Manager OnDemand for z/OS and OS/390 V7 (program number 5655-H39) IBM DB2 Content Manager OnDemand for iSeries V5 (program number 5722-RD1) (c) Copyright IBM Corp. 2004, 2006.  All Rights Reserved. US Government Users Restricted Rights Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corporation";
/*     */   
/*     */   public static final String ERROR_INVALID_TO_FIELD = "error_invalid_to_field";
/*     */   
/*     */   public static final String ERROR_NULL_TO_FIELD = "error_null_to_field";
/*     */   
/*     */   public static final String ERROR_INVALID_CC_FIELD = "error_invalid_cc_field";
/*     */   
/*     */   public static final String ERROR_INVALID_BCC_FIELD = "error_invalid_bcc_field";
/*     */   
/*     */   public static final String ERROR_INVALID_FROM_FIELD = "error_invalid_from_field";
/*     */   
/*     */   public static final String ERROR_NULL_FROM_FIELD = "error_null_from_field";
/*     */   
/*     */   public static final String ERROR_NULL_SUBJECT_FIELD = "error_null_subject_field";
/*     */   
/*     */   public static final String ERROR_NULL_MAIL_HOST_FIELD = "error_null_mail_host_field";
/*     */   
/*     */   public static final String ERROR_NULL_MAIL_USER_FIELD = "error_null_mail_user_field";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PARENT_DOCID = "parentDocid";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_VERSION_CONTROL = "versionControl";
/*     */   
/*     */   public static final String SESSION_USERID = "session_userid";
/*     */   
/*     */   public static final String DESKTOP_CONFIG = "desktop_config";
/*     */   
/*     */   public static final String VIEWER_CONFIG = "viewer_config";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_PROPERTIES = "itemProperties";
/*     */   
/*     */   public static final String CONTENT_LIST_SEARCH_RESULTS = "searchresults";
/*     */   
/*     */   public static final String CONTENT_LIST_WORK_LIST_RESULTS = "worklist";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_LOCATION = "location";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_NAME = "name";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_ISSELECTED = "isSelected";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_HASNOTE = "hasNote";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_ISCHECKEDOUT = "locked";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_ISCURRENTVERSION = "currentVersion";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_SHOWCHECKOUTFORRESERVATION = "showCheckoutForReservation";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_CHECKEDOUT_USER = "lockedUser";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_DOCID = "docid";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_REPOID = "repoid";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_TEMPLATE = "template";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_TEMPLATE_LABEL = "template_label";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_FILENAME = "filename";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_MIMETYPE = "mimetype";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ENTRY_TEMPLATE_ID = "entryTemplateId";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_EDITPROPERTIES = "privEditProperties";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_EDITDOC = "privEditDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_VIEWNOTES = "privViewNotes";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_ADDDOC = "privAddDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_ADDITEM = "privAddItem";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_EMAILDOC = "privEmailDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_EXPORT = "privExport";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_ADD_TO_FOLDER = "privAddToFolder";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_REMOVE_FROM_FOLDER = "privRemoveFromFolder";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_ADD_LINK = "privAddLink";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_REMOVE_LINK = "privRemoveLink";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_NONE = "privNone";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_ADDNOTES = "privAddNotes";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_PRINTNOTES = "privPrintNotes";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_PRINTDOC = "privPrintDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_CHECKINOUTDOC = "privCheckInOutDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_CHECKINDOC = "privCheckInDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_CHECKOUTDOC = "privCheckOutDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_CANCELCHECKOUTDOC = "privCancelCheckOutDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_VIEWANNOTATIONS = "privViewAnnotations";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_EDITANNOTATIONS = "privEditAnnotations";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_VIEWDOC = "privViewDoc";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_DELETE = "privDelete";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_STARTWORKFLOW = "privStartWorkflow";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_HOLD = "privHold";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_MOVE_TO_FOLDER = "privMoveToFolder";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_CHANGE_CLASS = "privChangeClass";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_MAJOR_VERSION = "privMajorVersion";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_MINOR_VERSION = "privMinorVersion";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PRIV_DECLARE = "privIERRecordDeclare";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ACTION_ID = "id";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_DATE_FORMAT = "dataFormat";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_TIME_FORMAT = "timeFormat";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_SORT_FORMAT = "sortformats";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_VERSION_SERIES_ID = "vsId";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_REPLICATION_GROUP = "replicationGroup";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_MODIFY_PERMISSIONS = "modifyPermissions";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_VERSION_STATUS_INT = "versionStatusInt";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_VERSIONCONTROL_BYAPP = "isVersionControlByApp";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_FIRST_PART_VERSIONCONTROL_BYAPP = "isFirstPartVersionControlByApp";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_ICM_FIRST_PART_TYPE = "icmPartType";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_ICM_HAS_BOOKMARKS = "hasBookmarks";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_IS_HIERARCHICAL_ITEM_TYPE = "isHierarchicalItemType";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_HAS_HOLD = "hasHold";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_IS_DECLARED_AS_RECORD = "declaredAsRecord";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ITEM_DATES = "dates";
/*     */   
/*     */   public static final String JSON_USERGROUP_DISTINGUISHED_NAME = "distinguishedName";
/*     */   
/*     */   public static final String JSON_USERGROUP_DISPLAY_NAME = "displayName";
/*     */   
/*     */   public static final String JSON_USERGROUP_SHORT_NAME = "shortName";
/*     */   
/*     */   public static final String JSON_USERGROUP_NAME = "name";
/*     */   
/*     */   public static final String JSON_USERGROUP_ID = "id";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_COLUMNS = "columns";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_MAGAZINE_COLUMNS = "magazineColumns";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_ROWS = "rows";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_SHOWLOCATION = "showLocation";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_SHOWNOTESICON = "showNotesIcon";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_PAGESIZE = "pageSize";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_SORTINDEX = "sortIndex";
/*     */   
/*     */   public static final String JSON_CONTENT_LIST_SORTDIRECTION = "sortDirection";
/*     */   public static final String JSON_CONTENT_TOTAL_COUNT = "totalCount";
/*     */   public static final String JSON_CONTENT_TOTAL_COUNT_TYPE = "totalCountType";
/*     */   public static final String JSON_PRIV_CREATE_HOLD = "privCreateHold";
/*     */   public static final String JSON_PRIV_ADD_ITEM = "priv_addDoc";
/*     */   public static final String JSON_PRIV_ADD_CONTENT = "priv_addItem";
/*     */   public static final String JSON_PRIV_ADD_SEARCH = "priv_addSearch";
/*     */   public static final String JSON_PRIV_ADD_UNIFIED_SEARCH = "priv_addUnifiedSearch";
/*     */   public static final String JSON_PRIV_ADD_TEAMSPACE = "priv_addTeamspace";
/*     */   public static final String JSON_PRIV_ADD_TEAMSPACE_TEMPLATE = "priv_addTeamspaceTemplate";
/*     */   public static final String JSON_PRIV_FOLDERING = "priv_foldering";
/*     */   public static final String JSON_PRIV_SEARCH_TEMPLATE = "searchTemplateSupported";
/*     */   public static final String JSON_PRIV_WORKFLOW = "priv_workflow";
/*     */   public static final String JSON_PRIV_TASK_USER_PERMISSION = "privTaskUserPermission";
/*     */   public static final String JSON_PRIV_TASK_ADMIN_PERMISSION = "privTaskAdminPermission";
/*     */   public static final String JSON_PRIV_FIND_USERS_AND_GROUPS = "privFindUsersAndGroups";
/*     */   public static final String JSON_GRANTEE_ID = "id";
/*     */   public static final String JSON_GRANTEE_NAME = "granteeName";
/*     */   public static final String JSON_DISPLAY_NAME = "displayName";
/*     */   public static final String JSON_GRANTEE_TYPE = "granteeType";
/*     */   public static final String JSON_ACCESS_MASK = "accessMask";
/*     */   public static final String JSON_ACCESS_TYPE = "accessType";
/*     */   public static final String JSON_INHERITABLE_DEPTH = "inheritableDepth";
/*     */   public static final String JSON_PERMISSION_SOURCE = "permissionSource";
/*     */   public static final String FORM_TYPE_SEARCH = "search";
/*     */   public static final String FORM_TYPE_SEARCH_FOLDER = "searchFolder";
/*     */   public static final String FORM_TYPE_EDIT = "edit";
/*     */   public static final String FORM_TYPE_IMPORT = "import";
/*     */   public static final String FORM_TYPE_APPLY = "apply";
/*     */   public static final String DIALOG_TYPE_DOCUMENT = "Document";
/*     */   public static final String DIALOG_TYPE_FOLDER = "Folder";
/*     */   public static final String DIALOG_TYPE_MULTI = "Items";
/*     */   public static final String FORM_OPERATOR_EQUALS = "EQUAL";
/*     */   public static final String FORM_OPERATOR_NOT_EQUAL = "NOTEQUAL";
/*     */   public static final String FORM_OPERATOR_LIKE = "LIKE";
/*     */   public static final String FORM_OPERATOR_NOT_LIKE = "NOTLIKE";
/*     */   public static final String FORM_OPERATOR_STARTS_WITH = "STARTSWITH";
/*     */   public static final String FORM_OPERATOR_ENDS_WITH = "ENDSWITH";
/*     */   public static final String FORM_OPERATOR_IN = "IN";
/*     */   public static final String FORM_OPERATOR_INANY = "INANY";
/*     */   public static final String FORM_OPERATOR_NOT_IN = "NOTIN";
/*     */   public static final String FORM_OPERATOR_IS_NULL = "NULL";
/*     */   public static final String FORM_OPERATOR_IS_NOT_NULL = "NOTNULL";
/*     */   public static final String FORM_OPERATOR_CONTAINS = "CONTAINS";
/*     */   public static final String FORM_OPERATOR_LESS_THAN = "LESS";
/*     */   public static final String FORM_OPERATOR_LESS_THAN_EQUAL = "LESSOREQUAL";
/*     */   public static final String FORM_OPERATOR_GREATER_THAN = "GREATER";
/*     */   public static final String FORM_OPERATOR_GREATER_THAN_EQUAL = "GREATEROREQUAL";
/*     */   public static final String FORM_OPERATOR_BETWEEN = "BETWEEN";
/*     */   public static final String FORM_OPERATOR_NOT_BETWEEN = "NOTBETWEEN";
/*     */   public static final String ABOUT_TITLE = "aboutTitle";
/*     */   public static final String ABOUT_PRODUCT_NAME = "aboutProductName";
/*     */   public static final String ABOUT_PRODUCT_RELEASE = "aboutProductRelease";
/*     */   public static final String ABOUT_PRODUCT_LICENSE = "aboutProductLicense";
/*     */   public static final String CONTENT_SOURCE_TYPE_DOCUMENT = "Document";
/*     */   public static final String CONTENT_SOURCE_TYPE_FOLDER = "Folder";
/*     */   public static final String CONTENT_SOURCE_TYPE_DATA_OBJECT = "DataObject";
/*     */   public static final String CONTENT_SOURCE_TYPE_ITEM = "Item";
/*     */   public static final String CONTENT_SOURCE_TYPE_EXTERNAL_URL = "ExternalURL";
/*     */   public static final String TEAMSPACE_VALIDATE = "validate";
/*     */   public static final String TEAMSPACE_ID = "workspaceId";
/*     */   public static final String TEAMSPACE_NAME = "name";
/*     */   public static final String TEAMSPACE_DESCRIPTION = "desc";
/*     */   public static final String TEAMSPACE_LAST_MODIFIED = "lastModified";
/*     */   public static final String TEAMSPACE_LAST_MODIFIED_USER = "lastModifiedUser";
/*     */   public static final String FOLDER_NAME = "name";
/*     */   public static final String MARKING_ID = "id";
/*     */   public static final String MARKING_DISPLAY_NAME = "displayName";
/*     */   public static final String MARKING_VALUE = "value";
/*     */   public static final String MARKING_MASK = "mask";
/*     */   public static final String MARKING_CONSTRAINT_MASK = "constraintMask";
/*     */   public static final String MARKING_USE_GRANTED = "useGranted";
/*     */   public static final String MARKING_CAN_USE = "canUse";
/*     */   public static final String MARKING_CAN_ADD = "canAdd";
/*     */   public static final String MARKING_CAN_REMOVE = "canRemove";
/*     */   public static final String ITEM_TYPE_COMMON = "$common";
/*     */   public static final String JSON_CONTAINMENT_NAME = "containmentName";
/*     */   public static final String JSON_FULLPATH_NAME = "PathName";
/*     */   public static final String JSON_TAMTOKEN = "tam_token";
/*     */   public static final String DEFAULT_VIEWER_CLASS = "ecm.widget.viewer.IframeDocViewer";
/*     */   public static final String PSEUDO_CLASS = "{CLASS}";
/*     */   public static final String PSEUDO_NAME = "{NAME}";
/*     */   public static final String PSEUDO_USER_ME = "{ME}";
/*     */   public static final String IS_TASK_ADMIN = "isTaskAdmin";
/*     */   public static final String IS_TASK_USER = "isTaskUser";
/*     */   public static final String IS_TASK_AUDITOR = "isTaskAuditor";
/*     */   public static final int PAGE_SIZE = 200;
/*     */   
/*     */   public static abstract enum AuthenticationType
/*     */   {
/* 279 */     CONTAINER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 284 */     REPOSITORY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 289 */     DATASOURCE;
/*     */     
/*     */     private AuthenticationType() {}
/*     */     
/*     */     public abstract int getValue();
/*     */     
/*     */     public static AuthenticationType getEnum(int value)
/*     */       throws Exception
/*     */     {
/* 298 */       for (AuthenticationType authenticationType : ) {
/* 299 */         if (authenticationType.getValue() == value) {
/* 300 */           return authenticationType;
/*     */         }
/*     */       }
/*     */       
/* 304 */       throw new Exception("Unsupported authentication type!");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\Constants.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */