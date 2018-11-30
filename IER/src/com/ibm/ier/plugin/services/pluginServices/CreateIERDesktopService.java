/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ecm.configuration.ApplicationConfig;
/*    */ import com.ibm.ecm.configuration.DesktopConfig;
/*    */ import com.ibm.ier.plugin.configuration.Config;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import java.util.Collection;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreateIERDesktopService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public static final String IER_APPLICATION_NAME = "IBM Enterprise Records";
/*    */   private static final String DESKTOPS = "desktops";
/*    */   public static final String FEATURE_CONFIGURE = "IERConfigure";
/*    */   public static final String FEATURE_BROWSE = "IERBrowseFilePlan";
/*    */   public static final String FEATURE_SEARCH = "IERSearch";
/*    */   public static final String FEATURE_FAVORITES = "IERFavorites";
/*    */   public static final String FEATURE_REPORT = "IERReports";
/*    */   public static final String FEATURE_TASKS = "IERTasks";
/*    */   public static final String FEATURE_ADMIN = "IERAdmin";
/*    */   public static final String FEATURE_WORK = "workPane";
/*    */   
/*    */   public String getId()
/*    */   {
/* 37 */     return "ierCreateDesktopService";
/*    */   }
/*    */   
/*    */ 
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*    */     throws Exception
/*    */   {
/* 44 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */ 
/* 47 */     String desktopId = request.getParameter("ier_id");
/* 48 */     String desktopName = request.getParameter("template_name");
/* 49 */     String desktopDescription = request.getParameter("template_desc");
/*    */     
/* 51 */     ApplicationConfig appConfig = Config.getApplicationConfig("navigator");
/* 52 */     createIERDesktop(appConfig, desktopId, desktopName, desktopDescription);
/*    */     
/* 54 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void createIERDesktop(ApplicationConfig appConfig, String desktopId, String desktopName, String description)
/*    */     throws Exception
/*    */   {
/* 64 */     if (isDesktopUnique(appConfig, desktopId)) {
/* 65 */       DesktopConfig desktopConfig = Config.getDesktopConfig("navigator", desktopId);
/* 66 */       desktopConfig.setName(desktopName);
/* 67 */       if (description != null)
/* 68 */         desktopConfig.setDescription(description);
/* 69 */       desktopConfig.setApplicationName("IBM Enterprise Records");
/* 70 */       desktopConfig.setLayout("ier.widget.layout.IERMainLayout");
/* 71 */       desktopConfig.setFeatures(new String[] { "IERFavorites", "IERBrowseFilePlan", "IERSearch", "IERReports", "IERTasks", "IERConfigure", "IERAdmin" });
/* 72 */       desktopConfig.setDefaultFeature("IERFavorites");
/* 73 */       desktopConfig.setProperty("BannerToolsContextMenu", "DefaultIERBannerToolsContextMenu");
/* 74 */       desktopConfig.setViewer("default");
/* 75 */       desktopConfig.setShowSecurity(true);
/* 76 */       desktopConfig.save();
/*    */       
/* 78 */       appConfig.addValueToList("desktops", desktopId);
/* 79 */       appConfig.save();
/*    */     }
/*    */     else {
/* 82 */       throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_DESKTOP_ID_NOT_UNIQUE, new Object[] { desktopId });
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean isDesktopUnique(ApplicationConfig appConfig, String desktopId) {
/* 87 */     Collection<DesktopConfig> desktopConfigs = appConfig.getDesktopList();
/*    */     
/*    */ 
/* 90 */     for (DesktopConfig desktopConfig : desktopConfigs) {
/* 91 */       if (desktopConfig.getObjectId().equals(desktopId))
/* 92 */         return false;
/*    */     }
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\CreateIERDesktopService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */