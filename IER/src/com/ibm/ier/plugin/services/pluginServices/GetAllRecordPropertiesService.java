/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.RMClassDescription;
/*    */ import com.ibm.jarm.api.meta.RMClassDescription;
/*    */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import java.util.TreeMap;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ public class GetAllRecordPropertiesService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 28 */     return "ierGetAllRecordPropertiesService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 32 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 34 */     JSONObject jsonResponse = new JSONObject();
/* 35 */     JSONArray jsonArray = new JSONArray();
/* 36 */     FilePlanRepository repository = this.baseService.getFilePlanRepository();
/* 37 */     RMClassDescription record_cd = RMFactory.RMClassDescription.fetchInstance(repository, "RecordInfo", RMPropertyFilter.MinimumPropertySet);
/* 38 */     Map<String, RMPropertyDescription> pds = new TreeMap();
/* 39 */     getAllPropertyDescriptions(pds, record_cd);
/*    */     
/* 41 */     Iterator<Map.Entry<String, RMPropertyDescription>> it = pds.entrySet().iterator();
/* 42 */     while (it.hasNext()) {
/* 43 */       Map.Entry<String, RMPropertyDescription> pairs = (Map.Entry)it.next();
/* 44 */       RMPropertyDescription pd = (RMPropertyDescription)pairs.getValue();
/*    */       
/* 46 */       JSONObject propertyJSON = new JSONObject();
/* 47 */       propertyJSON.put("name", pd.getDisplayName());
/* 48 */       propertyJSON.put("id", pd.getSymbolicName());
/* 49 */       propertyJSON.put("dataType", MediatorUtil.getJSONDataType(pd.getDataType()));
/* 50 */       propertyJSON.put("system", Boolean.valueOf((pd.isSystemGenerated()) || (pd.isSystemOwned())));
/* 51 */       propertyJSON.put("readOnly", Boolean.valueOf(pd.isReadOnly()));
/* 52 */       jsonArray.add(propertyJSON);
/*    */     }
/*    */     
/* 55 */     jsonResponse.put("properties", jsonArray);
/*    */     
/* 57 */     setCompletedJSONResponseObject(jsonResponse);
/* 58 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */   
/*    */   private void getAllPropertyDescriptions(Map<String, RMPropertyDescription> pds, RMClassDescription class_cd) {
/* 62 */     if (class_cd != null) {
/* 63 */       List<RMClassDescription> cds = class_cd.getImmediateSubclassDescriptions();
/* 64 */       if (cds.size() == 0) {
/* 65 */         List<RMPropertyDescription> list = class_cd.getPropertyDescriptions();
/* 66 */         for (RMPropertyDescription pd : list) {
/* 67 */           if ((!pds.containsKey(pd.getSymbolicName())) && (!pd.isHidden())) {
/* 68 */             pds.put(pd.getSymbolicName(), pd);
/*    */           }
/*    */         }
/* 71 */         return;
/*    */       }
/*    */       
/* 74 */       for (RMClassDescription cd : cds) {
/* 75 */         getAllPropertyDescriptions(pds, cd);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetAllRecordPropertiesService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */