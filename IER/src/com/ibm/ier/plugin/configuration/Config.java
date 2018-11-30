/*    */ package com.ibm.ier.plugin.configuration;
/*    */ 
/*    */ import com.ibm.ecm.configuration.ApplicationConfig;
/*    */ import com.ibm.ecm.configuration.exception.MissingValueException;
/*    */ import org.apache.commons.configuration.ConfigurationException;
/*    */ 
/*    */ public class Config extends com.ibm.ecm.configuration.Config
/*    */ {
/*    */   public static final String IER = "ier";
/*    */   public static final String DEFAULT = "default";
/*    */   
/*    */   public static ApplicationConfig getApplicationConfig() throws ConfigurationException, MissingValueException
/*    */   {
/* 14 */     return getApplicationConfig("ier");
/*    */   }
/*    */   
/*    */   public static SettingsConfig getSettingsConfig() throws ConfigurationException, MissingValueException {
/* 18 */     return (SettingsConfig)getApplicationConfig().getMap(SettingsConfig.class, "ier", new String[] { "default" }, new boolean[0]).get("default");
/*    */   }
/*    */   
/*    */   public static DesktopConfig getDesktopConfig(String desktopId) throws ConfigurationException, MissingValueException {
/* 22 */     DesktopConfig desktopConfig = (DesktopConfig)getApplicationConfig().getMap(DesktopConfig.class, "ier", new String[] { desktopId }, new boolean[0]).get(desktopId);
/* 23 */     if ((desktopConfig != null) && (desktopConfig.isEmpty())) {
/* 24 */       SettingsConfig settingsConfig = getSettingsConfig();
/* 25 */       desktopConfig.setDefaults(settingsConfig);
/* 26 */       removeConfigurationFromCache(desktopConfig);
/*    */     }
/* 28 */     return desktopConfig;
/*    */   }
/*    */   
/*    */   public static RepositoryConfig getRepositoryConfig(String repositoryId) throws ConfigurationException, MissingValueException {
/* 32 */     return (RepositoryConfig)getApplicationConfig().getMap(RepositoryConfig.class, "ier", new String[] { repositoryId }, new boolean[0]).get(repositoryId);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\configuration\Config.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */