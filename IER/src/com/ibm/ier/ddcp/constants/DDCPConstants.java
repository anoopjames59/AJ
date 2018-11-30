/*     */ package com.ibm.ier.ddcp.constants;
/*     */ 
/*     */ import com.ibm.ier.ddcp.util.DDCPLString;
/*     */ import java.util.ArrayList;
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
/*     */ public class DDCPConstants
/*     */ {
/*     */   public static final String AUDIT_EVENT_NAME = "RMAudit";
/*     */   public static final String AUDIT_PROP_AUDITACTIONTYPE = "AuditActionType";
/*     */   public static final String AUDIT_PROP_AUDITACTIONTYPE_DELETE = "Delete";
/*     */   public static final String AUDIT_PROP_REVIEWER = "Reviewer";
/*     */   public static final String AUDIT_PROP_EVENTSTATUS = "EventStatus";
/*     */   public static final String AUDIT_PROP_REASONFORACTION = "ReasonForAction";
/*     */   public static final String AUDIT_PROP_RMENTITYDESCRIPTION = "RMEntityDescription";
/*     */   public static final String CLASS_NAME_DOCUMENT = "Document";
/*     */   public static final String CLASS_NAME_FILEPLAN = "ClassificationScheme";
/*     */   public static final String CLASS_NAME_FOLDER = "Folder";
/*     */   public static final String CLASS_NAME_LINK = "Link";
/*     */   public static final String CLASS_NAME_RMLINK = "Relation";
/*     */   public static final String CLASS_NAME_TRANSCRIPT = "Transcript";
/*     */   public static final String CE_DEFAULT_ERROR_LOGGER = "filenet_error";
/*     */   public static final int DDCP_LOG_TRACE_MAX_BACKUP_INDEX = 5;
/*     */   public static final String DDCP_LOG_TRACE_MAX_FILE_SIZE = "100MB";
/*     */   public static final String DDCP_LOG_TRACE_PATTERN_LAYOUT = "%d %5p [%t] - %m\r\n";
/*     */   public static final int DEFAULT_THREAD_COUNT = 1;
/*     */   public static final int DEFAULT_READ_BATCH_SIZE = 10000;
/*     */   public static final int DEFAULT_UPDATE_BATCH_SIZE = 1000;
/*     */   public static final int DEFAULT_CONTENT_SIZE_LIMIT = 200000;
/*     */   public static final int DEFAULT_LINK_CACHE_SIZE = 100000;
/*     */   public static final int DEFAULT_ONHOLD_CONTAINER_CACHE_SIZE = 200000;
/*     */   public static final int SQL_IN_ITEMS_LIMIT = 100;
/*     */   public static final String DOD_CLASSIFIED_VERSION_5015_2 = "DOD-5015.2 Classified";
/*     */   public static final String EMPTY_GUID = "{00000000-0000-0000-0000-000000000000}";
/*     */   public static final int RM_TYPE_ELEC_RECORD = 301;
/*     */   public static final String ERR_ACCESS_DENIED = "ACCESS_DENIED";
/*     */   
/*     */   static
/*     */   {
/*  60 */     StringBuilder sb = new StringBuilder();
/*  61 */     sb.append(DDCPLString.get("header.recordName", "RecordName"));
/*  62 */     sb.append(",").append(DDCPLString.get("header.description", "Description"));
/*  63 */     sb.append(",").append(DDCPLString.get("header.reviewer", "Reviewer"));
/*  64 */     sb.append(",").append(DDCPLString.get("header.parentPath", "ParentPath"));
/*  65 */     sb.append(",").append(DDCPLString.get("header.filePlan", "FilePlan"));
/*  66 */     sb.append(",").append(DDCPLString.get("header.retentionTriggerName", "RetentionTriggerName"));
/*  67 */     sb.append(",").append(DDCPLString.get("header.retentionTriggerValue", "RetentionTriggerValue"));
/*  68 */     sb.append(",").append(DDCPLString.get("header.retentionPeriod", "RetentionPeriod(YYYY-MMMM-DDDD)"));
/*  69 */     sb.append(",").append(DDCPLString.get("header.recordID", "RecordID"));
/*  70 */     sb.append(",").append(DDCPLString.get("header.parentContainerID", "ParentContainerID")).append("\n");
/*  71 */     HEADLINE_RETENTION_DUE_REPORT = sb.toString();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  76 */     StringBuilder sb = new StringBuilder();
/*  77 */     sb.append(DDCPLString.get("header.recordName", "RecordName"));
/*  78 */     sb.append(",").append(DDCPLString.get("header.parentPath", "ParentPath"));
/*  79 */     sb.append(",").append(DDCPLString.get("header.filePlan", "FilePlan"));
/*  80 */     sb.append(",").append(DDCPLString.get("header.severityLevel", "SeverityLevel"));
/*  81 */     sb.append(",").append(DDCPLString.get("header.reason", "Reason"));
/*  82 */     sb.append(",").append(DDCPLString.get("header.recordID", "RecordID")).append("\n");
/*  83 */     HEADLINE_FAILURE_REPORT = sb.toString();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  88 */     StringBuilder sb = new StringBuilder();
/*  89 */     sb.append(DDCPLString.get("header.recordName", "RecordName"));
/*  90 */     sb.append(",").append(DDCPLString.get("header.parentPath", "ParentPath"));
/*  91 */     sb.append(",").append(DDCPLString.get("header.filePlan", "FilePlan"));
/*  92 */     sb.append(",").append(DDCPLString.get("header.recordID", "RecordID")).append("\n");
/*  93 */     HEADLINE_SUCCESS_REPORT = sb.toString();
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
/* 184 */     SEVERITY_REASON_LIST_PARENT_ON_HOLD = new ArrayList(2);
/* 185 */     SEVERITY_REASON_LIST_PARENT_ON_HOLD.add(DDCPLString.get("severity.hold", "On Hold"));
/* 186 */     SEVERITY_REASON_LIST_PARENT_ON_HOLD.add(DDCPLString.get("failure.parentOnHold", "Parent of this record is on hold"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */     SEVERITY_REASON_LIST_RECORD_ON_HOLD = new ArrayList(2);
/* 193 */     SEVERITY_REASON_LIST_RECORD_ON_HOLD.add(DDCPLString.get("severity.hold", "On Hold"));
/* 194 */     SEVERITY_REASON_LIST_RECORD_ON_HOLD.add(DDCPLString.get("failure.onHold", "This record is on hold"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */     SEVERITY_REASON_LIST_RECORD_NOT_FOUND = new ArrayList(2);
/* 201 */     SEVERITY_REASON_LIST_RECORD_NOT_FOUND.add(DDCPLString.get("severity.notFound", "Not Found"));
/* 202 */     SEVERITY_REASON_LIST_RECORD_NOT_FOUND.add(DDCPLString.get("failure.notFound", "This record does not exist"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */     SEVERITY_REASON_LIST_RECORD_MARKED_DELETED = new ArrayList(2);
/* 209 */     SEVERITY_REASON_LIST_RECORD_MARKED_DELETED.add(DDCPLString.get("severity.markedAsDeleted", "Marked As Deleted"));
/* 210 */     SEVERITY_REASON_LIST_RECORD_MARKED_DELETED.add(DDCPLString.get("failure.markedAsDeleted", "This record is marked as deleted"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */     SEVERITY_REASON_LIST_RECORD_RECORDEDDOCUMENT_DELETION_FAILED = new ArrayList(2);
/* 217 */     SEVERITY_REASON_LIST_RECORD_RECORDEDDOCUMENT_DELETION_FAILED.add(DDCPLString.get("severity.error", "Error"));
/* 218 */     SEVERITY_REASON_LIST_RECORD_RECORDEDDOCUMENT_DELETION_FAILED.add(DDCPLString.get("failure.contentDeletionFailed", "Failed to delete its recorded document(s)"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 224 */     SEVERITY_REASON_LIST_RECORD_SOFT_DELETION_PROPS_UPDATE_FAILED = new ArrayList(2);
/* 225 */     SEVERITY_REASON_LIST_RECORD_SOFT_DELETION_PROPS_UPDATE_FAILED.add(DDCPLString.get("severity.error", "Error"));
/* 226 */     SEVERITY_REASON_LIST_RECORD_SOFT_DELETION_PROPS_UPDATE_FAILED.add(DDCPLString.get("failure.propsUpdatingFailed", "Failed to update its IsDeleted and DocURI properties"));
/*     */   }
/*     */   
/*     */   public static final String ERR_OBJECT_NOT_FOUND = "OBJECT_NOT_FOUND";
/*     */   public static final String ERR_OBJECT_REFERENCED = "OBJECT_REFERENCED";
/*     */   public static final String FPOS_ROOT_FOLDER_PATH = "/Records Management/";
/*     */   public static final String HEADLINE_RETENTION_DUE_REPORT;
/*     */   public static final String HEADLINE_FAILURE_REPORT;
/*     */   public static final String HEADLINE_SUCCESS_REPORT;
/*     */   public static final String KEY_READ_BATCH_SIZE = "ReadBatchSize";
/*     */   public static final String KEY_THREAD_COUNT = "ThreadCount";
/*     */   public static final String KEY_UPDATE_BATCH_SIZE = "UpdateBatchSize";
/*     */   public static final String KEY_CONTENT_SIZE_LIMIT = "ContentSizeLimit";
/*     */   public static final String KEY_URI = "Uri";
/*     */   public static final String KEY_USERNAME = "UserName";
/*     */   public static final String KEY_PASSWORD = "Password";
/*     */   public static final String KEY_LINK_CACHE_SIZE_LIMIT = "LinkCacheSizeLimit";
/*     */   public static final String KEY_ONHOLD_CONTAINER_CACHE_SIZE_LIMIT = "OnHoldContainerCacheSizeLimit";
/*     */   public static final String KEY_ALWAYS_DECLARE_RECORD = "AlwaysDeclareRecord";
/*     */   public static final String KEY_ALWAYS_SHOW_DECLARE_RESULT = "AlwaysShowDeclareResult";
/*     */   public static final String KEY_NEED_APPROVAL = "NeedApproval";
/*     */   public static final String KEY_RECORD_CONTAINER_ID = "RecordContainerID";
/*     */   public static final String KEY_NOT_DELETED_REC_MAP = "NotDeletedRecMap";
/*     */   public static final String KEY_NOT_FOUND_REC_MAP = "NotFoundRecMap";
/*     */   public static final String KEY_DDC_SCHEDULE_CHANGED = "DDContainerScheduleChanged";
/*     */   public static final String KEY_DDC_NOT_EXIST = "DDContainerNotExit";
/*     */   public static final String PREFIX_RETENTION_DUE_REPORT_NAME = "RetentionDueRecReport_";
/*     */   public static final String PREFIX_FAILURE_REPORT_NAME = "NotDeletedRecReport_";
/*     */   public static final String PREFIX_SUCCESS_REPORT_NAME = "DeletedRecReport_";
/*     */   public static final String PROPERTY_ALLOWED_RM_TYPES = "AllowedRMTypes";
/*     */   public static final String PROPERTY_AUDIT_LEVEL = "AuditLevel";
/*     */   public static final String PROPERTY_CAN_DECLARE = "CanDeclare";
/*     */   public static final String PROPERTY_CLASSIFICATION_SCHEME_NAME = "ClassificationSchemeName";
/*     */   public static final String PROPERTY_CURRENT_PHASE_EXEC_STATUS = "CurrentPhaseExecutionStatus";
/*     */   public static final String PROPERTY_DESCRIPTION = "RMEntityDescription";
/*     */   public static final String PROPERTY_DOCUMENT_TITLE = "DocumentTitle";
/*     */   public static final String PROPERTY_DOCURI = "DocURI";
/*     */   public static final String PROPERTY_DOMAIN = "Domain";
/*     */   public static final String PROPERTY_FISCAL_YEAR_END_DATE = "RMFiscalYearEndDate";
/*     */   public static final String PROPERTY_HEAD = "Head";
/*     */   public static final String PROPERTY_ID = "Id";
/*     */   public static final String PROPERTY_ISDELETED = "IsDeleted";
/*     */   public static final String PROPERTY_NAME = "Name";
/*     */   public static final String PROPERTY_NONPAGEDQUERYMAXSIZE = "NonPagedQueryMaxSize";
/*     */   public static final String PROPERTY_ONHOLD = "OnHold";
/*     */   public static final String PROPERTY_PATH_NAME = "PathName";
/*     */   public static final String PROPERTY_PREVENTRMENTITYDELETION = "PreventRMEntityDeletion";
/*     */   public static final String PROPERTY_PROPERTY_NAME = "PropertyName";
/*     */   public static final String PROPERTY_PROPERTY_VALUE = "PropertyValue";
/*     */   public static final String PROPERTY_QUERYPAGEDMAXSIZE = "QueryPageMaxSize";
/*     */   public static final String PROPERTY_RECEIPTS = "Receipts";
/*     */   public static final String PROPERTY_RECEIPT_STATUS = "ReceiptStatus";
/*     */   public static final String PROPERTY_RECEIPTOF = "ReceiptOf";
/*     */   public static final String PROPERTY_RECORDEDDOCUMENTS = "RecordedDocuments";
/*     */   public static final String PROPERTY_RECORDINFORMATION = "RecordInformation";
/*     */   public static final String PROPERTY_RETAIN_METADATA = "RetainMetadata";
/*     */   public static final String PROPERTY_RETENTION_PERIOD = "RMRetentionPeriod";
/*     */   public static final String PROPERTY_RETENTION_TRIGGER_PROPERTY_NAME = "RMRetentionTriggerPropertyName";
/*     */   public static final String PROPERTY_REVIEWER = "Reviewer";
/*     */   public static final String PROPERTY_RM_ENTITY_TYPE = "RMEntityType";
/*     */   public static final String PROPERTY_TAIL = "Tail";
/*     */   public static final String PROPERTY_VWVERSTION = "VWVersion";
/*     */   public static final int TYPE_HOLD_LINK = 400;
/*     */   public static final int TYPE_FOLDER_SEE_ALSO_LINK = 406;
/*     */   public static final String WORKFLOW_PARAM_REVIEWER = "Reviewer";
/*     */   public static final String WORKFLOW_PARAM_REPORTID = "RetentionDueReportID";
/*     */   public static final String WORKFLOW_PARAM_OBJECTSTOREID = "ObjectStoreName";
/*     */   public static final String WORKFLOW_PARAM_ADVANVEDAYS = "ReportAdvanceDays";
/*     */   public static final String WORKFLOW_PARAM_ALWAYSDECLARERECORD = "AlwaysDeclareRecord";
/*     */   public static final String WORKFLOW_PARAM_ALWAYSSHOWDECLARERESULT = "AlwaysShowDeclareResult";
/*     */   public static final String WORKFLOW_PARAM_NEEDAPPROVAL = "NeedApproval";
/*     */   public static final String WORKFLOW_PARAM_RECORDCONTAINERID = "RecordContainerID";
/*     */   public static final String WORKFLOW_PARAM_THREADCOUNT = "ThreadCount";
/*     */   public static final String WORKFLOW_PARAM_READ_BATCH_SIZE = "RetrievalBatchSize";
/*     */   public static final String WORKFLOW_PARAM_UPDATE_BATCH_SIZE = "UpdateBatchSize";
/*     */   public static final String WORKFLOW_PARAM_CONTENT_SIZE_LIMIT = "ReportContentSizeLimit";
/*     */   public static final String WORKFLOW_PARAM_LINK_CACHE_SIZE = "LinkCacheSize";
/*     */   public static final String WORKFLOW_PARAM_ONHOLD_CONTAINER_CACHE_SIZE = "OnHoldCacheSize";
/*     */   public static final String WORKFLOW_PARAM_RECORDNAME = "RecordName";
/*     */   public static final String WORKFLOW_PARAM_LOG_LEVEL = "LogLevel";
/*     */   public static final String WORKFLOW_PARAM_TRACE_LEVEL = "TraceLevel";
/*     */   public static final List<String> SEVERITY_REASON_LIST_PARENT_ON_HOLD;
/*     */   public static final List<String> SEVERITY_REASON_LIST_RECORD_ON_HOLD;
/*     */   public static final List<String> SEVERITY_REASON_LIST_RECORD_NOT_FOUND;
/*     */   public static final List<String> SEVERITY_REASON_LIST_RECORD_MARKED_DELETED;
/*     */   public static final List<String> SEVERITY_REASON_LIST_RECORD_RECORDEDDOCUMENT_DELETION_FAILED;
/*     */   public static final List<String> SEVERITY_REASON_LIST_RECORD_SOFT_DELETION_PROPS_UPDATE_FAILED;
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\constants\DDCPConstants.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */