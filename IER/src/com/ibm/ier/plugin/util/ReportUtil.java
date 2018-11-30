/*    */ package com.ibm.ier.plugin.util;
/*    */ 
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.core.Container;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMCustomObject;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMCustomObject;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class ReportUtil
/*    */ {
/*    */   public static Map<String, Object> getReportParameters(FilePlanRepository fp_repository, JSONArray criterias)
/*    */   {
/* 18 */     Map<String, Object> rptParams = new HashMap();
/*    */     
/*    */ 
/* 21 */     for (Object obj : criterias) {
/* 22 */       JSONObject jsonObj = (JSONObject)obj;
/* 23 */       String name = (String)jsonObj.get("name");
/* 24 */       if (rptParams.get(name) == null) {
/* 25 */         Object value = jsonObj.get("value");
/* 26 */         String dataType = (String)jsonObj.get("dataType");
/* 27 */         if ((dataType != null) && ((dataType.equals("xs:user")) || (dataType.equals("xs:group")))) {
/* 28 */           JSONObject groupObject = (JSONObject)((JSONArray)value).get(0);
/* 29 */           value = (String)groupObject.get("shortName");
/*    */         }
/*    */         
/* 32 */         if (((name.equals("disposal_schedule")) || (name.equals("hold_name"))) && 
/* 33 */           (value != null)) {
/* 34 */           value = IERUtil.getIdFromDocIdString(value.toString());
/* 35 */           value = RMFactory.RMCustomObject.fetchInstance(fp_repository, value.toString(), null).getName();
/*    */         }
/*    */         
/*    */ 
/* 39 */         if (name.equals("fileplan_browse")) {
/* 40 */           Container container = com.ibm.jarm.api.core.RMFactory.Container.fetchInstance(fp_repository, EntityType.Container, (String)value, RMPropertyFilter.MinimumPropertySet);
/* 41 */           value = container.getPathName();
/*    */         }
/*    */         
/* 44 */         if (name.equals("fileplan_name")) {
/* 45 */           value = "/Records Management/" + value;
/*    */         }
/*    */         
/* 48 */         if ((name != null) && (value != null))
/* 49 */           rptParams.put(name, value);
/*    */       }
/*    */     }
/* 52 */     return rptParams;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\ReportUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */