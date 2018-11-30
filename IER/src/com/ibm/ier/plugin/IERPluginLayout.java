/*    */ package com.ibm.ier.plugin;
/*    */ 
/*    */ import com.ibm.ecm.extension.PluginLayout;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.nls.MessageResources;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IERPluginLayout
/*    */   extends PluginLayout
/*    */ {
/*    */   public static final String IER_MAINLAYOUT = "ier.widget.layout.IERMainLayout";
/*    */   
/*    */   public String getId()
/*    */   {
/* 37 */     return "ier.widget.layout.IERMainLayout";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean areFeaturesConfigurable()
/*    */   {
/* 46 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getLayoutClass()
/*    */   {
/* 53 */     return "ier.widget.layout.IERMainLayout";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale)
/*    */   {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.PLUGIN_LAYOUT_NAME, new Object[0]);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\IERPluginLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */