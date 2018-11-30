/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.NamingPattern;
/*    */ import com.ibm.jarm.api.core.NamingPatternLevel;
/*    */ import com.ibm.jarm.api.core.RMFactory.NamingPattern;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ public class GetNamingPatternLevelsService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 25 */     return "ierGetNamingPatternLevels";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 29 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     try
/*    */     {
/* 32 */       JSONArray levelArray = new JSONArray();
/* 33 */       FilePlanRepository repository = getFilePlanRepository();
/* 34 */       String patternId = request.getParameter("ier_patternId");
/* 35 */       if (patternId != null) {
/* 36 */         patternId = IERUtil.getIdFromDocIdString(patternId);
/* 37 */         NamingPattern pattern = RMFactory.NamingPattern.fetchInstance(repository, patternId, null);
/* 38 */         if (pattern != null) {
/* 39 */           List<NamingPatternLevel> levels = pattern.getNamingPatternLevels();
/* 40 */           for (NamingPatternLevel level : levels) {
/* 41 */             JSONObject levelObject = new JSONObject();
/* 42 */             Integer levelNumber = level.getPatternLevelNumber();
/* 43 */             if (levelNumber != null) {
/* 44 */               levelObject.put("level", levelNumber);
/*    */             }
/* 46 */             AppliedForCategoryOrFolder entityType = level.getAppliedFor();
/* 47 */             if (entityType != null) {
/* 48 */               levelObject.put("entityType", entityType.name());
/*    */             }
/* 50 */             String patternString = level.getPatternString();
/* 51 */             if (patternString != null) {
/* 52 */               levelObject.put("pattern", patternString);
/*    */             }
/* 54 */             Integer incrementBy = level.getIncrementedBy();
/* 55 */             if (incrementBy != null) {
/* 56 */               levelObject.put("increment", incrementBy);
/*    */             }
/* 58 */             levelArray.add(levelObject);
/*    */           }
/*    */         }
/*    */       }
/* 62 */       JSONObject responseObject = new JSONObject();
/* 63 */       responseObject.put("namingPatternLevels", levelArray);
/* 64 */       setCompletedJSONResponseObject(responseObject);
/*    */     } catch (Exception e) {
/* 66 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_APP_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/*    */     
/* 69 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetNamingPatternLevelsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */