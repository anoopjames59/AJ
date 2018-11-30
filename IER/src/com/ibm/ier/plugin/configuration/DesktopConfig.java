/*    */ package com.ibm.ier.plugin.configuration;
/*    */ 
/*    */ import org.apache.commons.configuration.Configuration;
/*    */ 
/*    */ public class DesktopConfig extends SettingsConfig
/*    */ {
/*    */   public DesktopConfig(Configuration configuration, String idPrefix, String id) {
/*  8 */     super(configuration, idPrefix, id);
/*    */   }
/*    */   
/*    */   public String getObjectType() {
/* 12 */     return "desktop";
/*    */   }
/*    */   
/*    */   public void setDefaults(SettingsConfig settingsConfig) {
/* 16 */     if (settingsConfig != null) {
/* 17 */       setBrowseFolderOnly(settingsConfig.getBrowseFolderOnly());
/* 18 */       setBrowseDisableSort(settingsConfig.getBrowseDisableSort());
/* 19 */       setTaskManagerLogDirectory(settingsConfig.getTaskManagerLogDirectory());
/* 20 */       setCBRPageSize(settingsConfig.getCBRPageSize());
/* 21 */       setCognosReportGatewayServer(settingsConfig.getCognosReportGatewayServer());
/* 22 */       setCognosReportDispatchServletServer(settingsConfig.getCognosReportDispatchServletServer());
/* 23 */       setCognosReportPath(settingsConfig.getCognosReportPath());
/* 24 */       setCognosNamespace(settingsConfig.getCognosNamespace());
/* 25 */       setReportEngineDataSource(settingsConfig.getReportEngineDataSource());
/* 26 */       setDDSweepThreadCount(settingsConfig.getDDSweepThreadCount());
/* 27 */       setDDSweepQueryPageSize(settingsConfig.getDDSweepQueryPageSize());
/* 28 */       setDDSweepLinkCacheSizeLimit(settingsConfig.getDDSweepLinkCacheSizeLimit());
/* 29 */       setDDSweepOnHoldCOntainerCacheSize(settingsConfig.getDDSweepOnHoldCOntainerCacheSize());
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\configuration\DesktopConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */