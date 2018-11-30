/*    */ package com.ibm.ier.plugin.services.requestFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*    */ import com.ibm.json.java.JSONArtifact;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class P8FindClassesPreOverride
/*    */   extends IERBaseRequestFilterService
/*    */ {
/*    */   private static final String INCLUDE_HIDDEN_CLASSES = "includeHiddenClasses";
/*    */   
/*    */   public String[] getFilteredServices()
/*    */   {
/* 17 */     return new String[] { "/p8/getContentClasses" };
/*    */   }
/*    */   
/*    */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest) throws Exception
/*    */   {
/* 22 */     String className = request.getParameter("class_name");
/* 23 */     if (className != null) {
/* 24 */       if ((className.equals("RecordFolder")) || (className.equals("ElectronicRecordFolder")) || (className.equals("PhysicalRecordFolder")) || (className.equals("HybridRecordFolder")) || (className.equals("PhysicalBox")) || (className.equals("Box")))
/*    */       {
/*    */ 
/*    */ 
/* 28 */         request.setAttribute("includeHiddenClasses", Boolean.TRUE);
/*    */       }
/*    */     }
/* 31 */     else if (request.getParameter("itemList") != null) {
/* 32 */       request.setAttribute("includeHiddenClasses", Boolean.TRUE);
/*    */     }
/*    */     
/*    */ 
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\P8FindClassesPreOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */