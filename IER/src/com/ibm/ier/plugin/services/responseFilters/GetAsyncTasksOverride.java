/*    */ package com.ibm.ier.plugin.services.responseFilters;
/*    */ 
/*    */ import com.ibm.ier.plugin.services.IERBaseResponseFilterService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetAsyncTasksOverride
/*    */   extends IERBaseResponseFilterService
/*    */ {
/*    */   private static final String COLUMNNAME = "columnName";
/*    */   
/*    */   public String[] getFilteredServices()
/*    */   {
/* 19 */     return new String[] { "/getAsyncTasks", "/getAsyncTaskInstances" };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void filterServiceExecute(HttpServletRequest request, JSONObject jsonResponse)
/*    */     throws Exception
/*    */   {
/* 28 */     if (this.baseService.isIERDesktop())
/*    */     {
/* 30 */       JSONArray rows = (JSONArray)jsonResponse.get("rows");
/* 31 */       for (Object row : rows) {
/* 32 */         if ((row != null) && ((row instanceof JSONObject))) {
/* 33 */           JSONObject rowObj = (JSONObject)row;
/* 34 */           JSONObject attributes = (JSONObject)rowObj.get("attributes");
/* 35 */           attributes.put("stop_time", attributes.get("endTime"));
/*    */         }
/*    */       }
/*    */       
/* 39 */       JSONObject columns = (JSONObject)jsonResponse.get("columns");
/* 40 */       if (columns != null) {
/* 41 */         JSONArray cells = (JSONArray)columns.get("cells");
/* 42 */         if (cells != null) {
/* 43 */           cells = (JSONArray)cells.get(0);
/* 44 */           for (Object cell : cells) {
/* 45 */             if ((cell != null) && ((cell instanceof JSONObject))) {
/* 46 */               JSONObject cellObj = (JSONObject)cell;
/* 47 */               String columnName = (String)cellObj.get("columnName");
/* 48 */               if (columnName != null) {
/* 49 */                 if (columnName.equals("typeInternalIconColumn")) {
/* 50 */                   cellObj.put("decorator", "ierAsyncTaskTypeColumnDecorator");
/*    */                 }
/* 52 */                 if (columnName.equals("statusInternalIconColumn")) {
/* 53 */                   cellObj.put("decorator", "ierAsyncTaskStatusColumnDecorator");
/*    */                 }
/*    */                 
/* 56 */                 if (columnName.equals("endTime")) {
/* 57 */                   cellObj.put("columnName", "stop_time");
/* 58 */                   cellObj.put("field", "stop_time");
/*    */                 }
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\responseFilters\GetAsyncTasksOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */