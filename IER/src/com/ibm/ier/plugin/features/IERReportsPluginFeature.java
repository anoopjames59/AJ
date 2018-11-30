/*     */ package com.ibm.ier.plugin.features;
/*     */ 
/*     */ import com.ibm.ecm.extension.PluginFeature;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IERReportsPluginFeature
/*     */   extends PluginFeature
/*     */ {
/*     */   public static final String ID = "IERReports";
/*     */   
/*     */   public String getId()
/*     */   {
/*  38 */     return "IERReports";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName(Locale locale)
/*     */   {
/*  48 */     return MessageResources.getLocalizedMessage(locale, MessageCode.FEATURE_REPORTS, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription(Locale locale)
/*     */   {
/*  58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.FEATURE_REPORTS, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIconUrl()
/*     */   {
/*  68 */     return "launcherReportsIcon";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHelpContext()
/*     */   {
/*  76 */     return "/com.ibm.ier.help.doc/frmovh00.htm";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFeatureIconTooltipText(Locale locale)
/*     */   {
/*  87 */     return MessageResources.getLocalizedMessage(locale, MessageCode.FEATURE_REPORTS_TOOLTIP, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPopupWindowTooltipText(Locale locale)
/*     */   {
/*  97 */     return MessageResources.getLocalizedMessage(locale, MessageCode.FEATURE_REPORTS_TOOLTIP, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentClass()
/*     */   {
/* 107 */     return "ier.widget.layout.ReportPane";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPopupWindowClass()
/*     */   {
/* 118 */     return "ier.widget.layout.ReportsFlyoutPane";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPreLoad()
/*     */   {
/* 127 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\features\IERReportsPluginFeature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */