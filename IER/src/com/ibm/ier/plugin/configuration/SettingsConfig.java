/*     */ package com.ibm.ier.plugin.configuration;
/*     */ 
/*     */ import com.ibm.ecm.configuration.ConfigurationObject;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.configuration.Configuration;
/*     */ 
/*     */ public class SettingsConfig
/*     */   extends ConfigurationObject
/*     */ {
/*     */   public static final String BROWSE_FOLDER_ONLY = "browseFolderOnly";
/*     */   public static final String BROWSE_DISABLE_SORT = "browseDisableSort";
/*     */   public static final String AES_ENCRYPTION_KEY = "AESEncryptionKey";
/*     */   public static final String TASK_MANAGER_LOG_DIRECTORY = "taskManagerLogDirectory";
/*     */   public static final String COGNOS_REPORT_GATEWAY_SERVER = "cognosGatewayServerName";
/*     */   public static final String COGNOS_REPORT_DISPATH_SERVLET_SERVER = "cognosDispatchServletServerName";
/*     */   public static final String COGNOS_REPORT_PATH = "cognosReportPath";
/*     */   public static final String COGNOS_NAMESPACE = "cognosReportNamespace";
/*     */   public static final String REPORT_ENGINE_DATASOURCE = "reportEngineDataSource";
/*     */   public static final String DDSWEEP_TheadCount = "defensibleSweepThreadCount";
/*     */   public static final String DDSWEEP_QueryPageSize = "defensibleSweepQueryPageSize";
/*     */   public static final String DDSWEEP_UpdateBatchSize = "defensibleSweepUpdateBatchSize";
/*     */   public static final String DDSWEEP_ContentSizeLimit = "defensibleSweepContentSizeLimit";
/*     */   public static final String DDSWEEP_LinkCacheSizeLimit = "defensibleSweepLinkCacheSizeLimit";
/*     */   public static final String DDSWEEP_OnHoldCOntainerCacheSize = "defensibleSweepOnHoldContainerCacheSize";
/*     */   public static final String CBR_PAGESIZE = "cbrPageSize";
/*     */   
/*     */   public SettingsConfig(Configuration configuration, String idPrefix, String objectId)
/*     */   {
/*  29 */     super(configuration, idPrefix, objectId);
/*     */   }
/*     */   
/*     */   public String getObjectType()
/*     */   {
/*  34 */     return "settings";
/*     */   }
/*     */   
/*     */   public String getDDSweepThreadCount() {
/*  38 */     return this.properties.getProperty("defensibleSweepThreadCount", "1");
/*     */   }
/*     */   
/*     */   public void setDDSweepThreadCount(String value) {
/*  42 */     this.properties.setProperty("defensibleSweepThreadCount", value);
/*     */   }
/*     */   
/*     */   public String getDDSweepQueryPageSize() {
/*  46 */     return this.properties.getProperty("defensibleSweepQueryPageSize", "10000");
/*     */   }
/*     */   
/*     */   public void setDDSweepQueryPageSize(String value) {
/*  50 */     this.properties.setProperty("defensibleSweepQueryPageSize", value);
/*     */   }
/*     */   
/*     */   public String getDDSweepUpdateBatchSize() {
/*  54 */     return this.properties.getProperty("defensibleSweepUpdateBatchSize", "100");
/*     */   }
/*     */   
/*     */   public void setDDSweepUpdateBatchSize(String value) {
/*  58 */     this.properties.setProperty("defensibleSweepUpdateBatchSize", value);
/*     */   }
/*     */   
/*     */   public String getDDSweepContentSizeLimit() {
/*  62 */     return this.properties.getProperty("defensibleSweepContentSizeLimit", "200000");
/*     */   }
/*     */   
/*     */   public void setDDSweepContentSizeLimit(String value) {
/*  66 */     this.properties.setProperty("defensibleSweepContentSizeLimit", value);
/*     */   }
/*     */   
/*     */   public String getDDSweepLinkCacheSizeLimit() {
/*  70 */     return this.properties.getProperty("defensibleSweepLinkCacheSizeLimit", "100000");
/*     */   }
/*     */   
/*     */   public void setDDSweepLinkCacheSizeLimit(String value) {
/*  74 */     this.properties.setProperty("defensibleSweepLinkCacheSizeLimit", value);
/*     */   }
/*     */   
/*     */   public String getDDSweepOnHoldCOntainerCacheSize() {
/*  78 */     return this.properties.getProperty("defensibleSweepOnHoldContainerCacheSize", "100000");
/*     */   }
/*     */   
/*     */   public void setDDSweepOnHoldCOntainerCacheSize(String value) {
/*  82 */     this.properties.setProperty("defensibleSweepOnHoldContainerCacheSize", value);
/*     */   }
/*     */   
/*     */   public String getReportEngineDataSource() {
/*  86 */     return this.properties.getProperty("reportEngineDataSource", "");
/*     */   }
/*     */   
/*     */   public void setReportEngineDataSource(String value) {
/*  90 */     this.properties.setProperty("reportEngineDataSource", value);
/*     */   }
/*     */   
/*     */   public String getCognosNamespace() {
/*  94 */     return this.properties.getProperty("cognosReportNamespace", "");
/*     */   }
/*     */   
/*     */   public void setCognosNamespace(String value) {
/*  98 */     this.properties.setProperty("cognosReportNamespace", value);
/*     */   }
/*     */   
/*     */   public String getTaskManagerLogDirectory() {
/* 102 */     return this.properties.getProperty("taskManagerLogDirectory", "");
/*     */   }
/*     */   
/*     */   public void setTaskManagerLogDirectory(String value) {
/* 106 */     this.properties.setProperty("taskManagerLogDirectory", value);
/*     */   }
/*     */   
/*     */   public String getCognosReportGatewayServer() {
/* 110 */     return this.properties.getProperty("cognosGatewayServerName", "");
/*     */   }
/*     */   
/*     */   public void setCognosReportGatewayServer(String value) {
/* 114 */     this.properties.setProperty("cognosGatewayServerName", value);
/*     */   }
/*     */   
/*     */   public String getCognosReportDispatchServletServer() {
/* 118 */     return this.properties.getProperty("cognosDispatchServletServerName", "");
/*     */   }
/*     */   
/*     */   public void setCognosReportDispatchServletServer(String value) {
/* 122 */     this.properties.setProperty("cognosDispatchServletServerName", value);
/*     */   }
/*     */   
/*     */   public String getCognosReportPath() {
/* 126 */     return this.properties.getProperty("cognosReportPath", "/content/folder[@name='IERReport']");
/*     */   }
/*     */   
/*     */   public void setCognosReportPath(String value) {
/* 130 */     this.properties.setProperty("cognosReportPath", value);
/*     */   }
/*     */   
/*     */   public boolean getBrowseFolderOnly() {
/* 134 */     return Boolean.parseBoolean(this.properties.getProperty("browseFolderOnly", "false"));
/*     */   }
/*     */   
/*     */   public void setBrowseFolderOnly(boolean value) {
/* 138 */     this.properties.setProperty("browseFolderOnly", Boolean.toString(value));
/*     */   }
/*     */   
/*     */   public boolean getBrowseDisableSort() {
/* 142 */     return Boolean.parseBoolean(this.properties.getProperty("browseDisableSort", "false"));
/*     */   }
/*     */   
/*     */   public void setBrowseDisableSort(boolean value) {
/* 146 */     this.properties.setProperty("browseDisableSort", Boolean.toString(value));
/*     */   }
/*     */   
/*     */   public String getAESEncryptionKey() {
/* 150 */     return this.properties.getProperty("AESEncryptionKey", "");
/*     */   }
/*     */   
/*     */   public void setAESEncryptionKey(String value) {
/* 154 */     this.properties.setProperty("AESEncryptionKey", value);
/*     */   }
/*     */   
/*     */   public String getCBRPageSize() {
/* 158 */     return this.properties.getProperty("cbrPageSize", Integer.toString(50));
/*     */   }
/*     */   
/*     */   public void setCBRPageSize(String value) {
/* 162 */     this.properties.setProperty("cbrPageSize", value);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\configuration\SettingsConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */