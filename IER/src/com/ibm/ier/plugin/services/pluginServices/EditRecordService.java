/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*    */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERPermission;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.ier.plugin.util.SessionUtil;
/*    */ import com.ibm.jarm.api.constants.EntityType;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.RMFactory.Container;
/*    */ import com.ibm.jarm.api.core.RMFactory.Record;
/*    */ import com.ibm.jarm.api.core.Record;
/*    */ import com.ibm.jarm.api.core.RecordContainer;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*    */ import com.ibm.jarm.api.security.RMPermission;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ public class EditRecordService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 33 */     return "ierEditRecordService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 38 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/*    */     try
/*    */     {
/* 42 */       String docId = request.getParameter("ier_recordId");
/* 43 */       String className = request.getParameter("ier_recordClass");
/*    */       
/*    */ 
/* 46 */       FilePlanRepository repository = getFilePlanRepository();
/*    */       
/* 48 */       JSONObject requestContent = getRequestContent();
/* 49 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/* 50 */       JSONArray permissionsJson = (JSONArray)requestContent.get("ier_permissions");
/* 51 */       List<RMPermission> permissionList = null;
/* 52 */       if (permissionsJson != null) {
/* 53 */         permissionList = IERPermission.getPermissionsFromJSON(permissionsJson);
/*    */       }
/*    */       
/* 56 */       String recordId = IERUtil.getIdFromDocIdString(docId);
/* 57 */       Record record = RMFactory.Record.fetchInstance(repository, recordId, RMPropertyFilter.MinimumPropertySet);
/*    */       
/* 59 */       String securityParentItemId = (String)requestContent.get("ier_securityParentItemId");
/*    */       
/*    */ 
/* 62 */       RMProperties props = MediatorUtil.setProperties(SessionUtil.getClassDescription(repository, className, request), criterias, repository, record.getProperties());
/* 63 */       if ((securityParentItemId != null) && (securityParentItemId.length() > 0)) {
/* 64 */         String containerId = IERUtil.getIdFromDocIdString(securityParentItemId);
/* 65 */         RecordContainer rc = (RecordContainer)RMFactory.Container.fetchInstance(repository, EntityType.Container, containerId, null);
/* 66 */         props.putObjectValue("SecurityFolder", rc);
/*    */       }
/* 68 */       if (permissionList != null) {
/* 69 */         record.setPermissions(permissionList);
/*    */       }
/*    */       
/* 72 */       record.save(RMRefreshMode.Refresh);
/*    */     } catch (Exception e) {
/* 74 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_EDITRECORD_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*    */     }
/* 76 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\EditRecordService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */