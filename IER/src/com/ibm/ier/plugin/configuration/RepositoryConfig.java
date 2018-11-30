/*     */ package com.ibm.ier.plugin.configuration;
/*     */ 
/*     */ import com.ibm.ecm.configuration.ConfigurationObject;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.configuration.Configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RepositoryConfig
/*     */   extends ConfigurationObject
/*     */ {
/*     */   public static final String DEFAULT_FILE_PLAN = "defaultFilePlan";
/*     */   public static final String DISPLAY_COLUMNS = "DisplayColumns";
/*     */   public static final String DISPLAY_PROPERTIES = "DisplayProperties";
/*     */   public static final String PREDEFINED_CLASSES = "predefinedClasses";
/*     */   public static final String SYSTEM_PROPERTIES = "SystemProperties";
/*     */   public static final String NAME = "{NAME}";
/*     */   public static final String CONTAINER_ID = "ContainerId";
/*     */   public static final String REPORT_OUTPUT_SAVE_DIRECTORY = "reportOutputSaveDirectory";
/*     */   public static final String DDSWEEP_AlwaysDeclareRecord = "defensibleSweepAlwaysDeclareRecord";
/*     */   public static final String DDSWEEP_AlwaysShowDeclareResult = "defensibleSweepAlwaysShowDeclareResult";
/*     */   public static final String DDSWEEP_RecordContainerId = "defensibleDisposalRecordContainerId";
/*     */   public static final String DDSWEEP_WorkflowID = "defensibleDisposalWorkflowId";
/*     */   private static final String defaultFilePlan = "{7F29139B-04E0-442F-9B85-C71EA4988375}";
/*     */   private static final String defaultDDWorkflowId = "{1F98543C-87B7-43C0-B81F-FF51B4A15946}";
/*  34 */   private static final String DSWEEP_AlwaysShowDeclareResult = null;
/*     */   
/*  36 */   private static Map<String, String[]> defaultColumns = new HashMap() {};
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
/* 105 */   private static Map<String, String[]> defaultProperties = new HashMap() {};
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
/* 186 */   private static Map<String, String[]> defaultSystemProperties = new HashMap() {};
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
/*     */   public RepositoryConfig(Configuration configuration, String idPrefix, String objectId)
/*     */   {
/* 203 */     super(configuration, idPrefix, objectId);
/*     */   }
/*     */   
/*     */   public void load() {
/* 207 */     super.load();
/*     */     
/* 209 */     if (!this.properties.containsKey("defaultFilePlan")) {
/* 210 */       this.properties.setProperty("defaultFilePlan", "{7F29139B-04E0-442F-9B85-C71EA4988375}");
/*     */     }
/*     */     
/* 213 */     if (!this.properties.containsKey("defensibleDisposalWorkflowId")) {
/* 214 */       this.properties.setProperty("defensibleDisposalWorkflowId", "{1F98543C-87B7-43C0-B81F-FF51B4A15946}");
/*     */     }
/* 216 */     for (String k : defaultColumns.keySet()) {
/* 217 */       String key = k + "DisplayColumns";
/* 218 */       if (!this.properties.containsKey(key)) {
/* 219 */         this.properties.setProperty(key, arrayToString((String[])defaultColumns.get(k)));
/*     */       }
/*     */     }
/* 222 */     Set<String> keys = defaultProperties.keySet();
/* 223 */     for (String k : keys) {
/* 224 */       String key = k + "DisplayProperties";
/* 225 */       if (!this.properties.containsKey(key)) {
/* 226 */         this.properties.setProperty(key, arrayToString((String[])defaultProperties.get(k)));
/*     */       }
/*     */     }
/* 229 */     String[] predefinedClasses = new String[keys.size()];
/* 230 */     this.properties.setProperty("predefinedClasses", arrayToString((String[])keys.toArray(predefinedClasses)));
/* 231 */     for (String k : defaultSystemProperties.keySet()) {
/* 232 */       String key = k + "SystemProperties";
/* 233 */       if (!this.properties.containsKey(key)) {
/* 234 */         this.properties.setProperty(key, arrayToString((String[])defaultSystemProperties.get(k)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getObjectType() {
/* 240 */     return "repository";
/*     */   }
/*     */   
/*     */   public String getDDAlwaysShowDeclareResult() {
/* 244 */     return this.properties.getProperty("defensibleSweepAlwaysShowDeclareResult", "false");
/*     */   }
/*     */   
/*     */   public void setDDAlwaysShowDeclareResult(String value) {
/* 248 */     this.properties.setProperty(DSWEEP_AlwaysShowDeclareResult, value);
/*     */   }
/*     */   
/*     */   public String getDDWorkflowId() {
/* 252 */     return this.properties.getProperty("defensibleDisposalWorkflowId", "");
/*     */   }
/*     */   
/*     */   public void setDDWorkflowId(String value) {
/* 256 */     this.properties.setProperty("defensibleDisposalWorkflowId", value);
/*     */   }
/*     */   
/*     */   public String getDDRecordContainerId() {
/* 260 */     return this.properties.getProperty("defensibleDisposalRecordContainerId", "");
/*     */   }
/*     */   
/*     */   public void setDDRecordContainerId(String value) {
/* 264 */     this.properties.setProperty("defensibleDisposalRecordContainerId", value);
/*     */   }
/*     */   
/*     */   public String getDDAlwaysDeclareRecord() {
/* 268 */     return this.properties.getProperty("defensibleSweepAlwaysDeclareRecord", "true");
/*     */   }
/*     */   
/*     */   public void setDDAlwaysDeclareRecord(String value) {
/* 272 */     this.properties.setProperty("defensibleSweepAlwaysDeclareRecord", value);
/*     */   }
/*     */   
/*     */   public String getReportOutputSaveDirectory() {
/* 276 */     return this.properties.getProperty("reportOutputSaveDirectory");
/*     */   }
/*     */   
/*     */   public void setReportOutputSaveDirectory(String value) {
/* 280 */     this.properties.setProperty("reportOutputSaveDirectory", value);
/*     */   }
/*     */   
/*     */   public String getDefaultFilePlan() {
/* 284 */     return this.properties.getProperty("defaultFilePlan");
/*     */   }
/*     */   
/*     */   public String[] getDisplayColumns(String key) {
/* 288 */     return stringToArray(this.properties.getProperty(key + "DisplayColumns", ""));
/*     */   }
/*     */   
/*     */   public void setDisplayColumns(String key, String[] value) {
/* 292 */     this.properties.setProperty(key + "DisplayColumns", arrayToString(value));
/*     */   }
/*     */   
/*     */   public String[] getDisplayProeprties(String key) {
/* 296 */     return stringToArray(this.properties.getProperty(key + "DisplayProperties", ""));
/*     */   }
/*     */   
/*     */   public void setDisplayProperties(String key, String[] value) {
/* 300 */     this.properties.setProperty(key + "DisplayProperties", arrayToString(value));
/*     */   }
/*     */   
/*     */   public String[] getSystemProperties(String key) {
/* 304 */     return stringToArray(this.properties.getProperty(key + "SystemProperties", ""));
/*     */   }
/*     */   
/*     */   public void setSystemProperties(String key, String[] value) {
/* 308 */     this.properties.setProperty(key + "SystemProperties", arrayToString(value));
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\configuration\RepositoryConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */