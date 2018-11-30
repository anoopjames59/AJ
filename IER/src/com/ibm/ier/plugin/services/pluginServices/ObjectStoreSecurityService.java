/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.filenet.api.core.Connection;
/*    */ import com.filenet.api.core.Factory.Group;
/*    */ import com.filenet.api.core.Factory.User;
/*    */ import com.filenet.api.security.SecurityPrincipal;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.services.IERBaseService;
/*    */ import com.ibm.ier.plugin.util.security.ObjectStoreBaseSecurity;
/*    */ import com.ibm.ier.plugin.util.security.ObjectStoreDoDCh4Security;
/*    */ import com.ibm.ier.plugin.util.security.ObjectStoreDoDSecurity;
/*    */ import com.ibm.ier.plugin.util.security.ObjectStorePROSecurity;
/*    */ import com.ibm.ier.plugin.util.security.ObjectStoreSecurityUpdater;
/*    */ import com.ibm.jarm.api.constants.DataModelType;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.json.java.JSONArray;
/*    */ import com.ibm.json.java.JSONObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ public class ObjectStoreSecurityService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 30 */     return "ierObjectStoreSecurity";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 35 */     Logger.logEntry(this, "serviceExecute", request);
/* 36 */     JSONObject requestContent = getRequestContent();
/* 37 */     JSONArray permissionsJSON = (JSONArray)requestContent.get("ier_permissions");
/* 38 */     FilePlanRepository repository = getFilePlanRepository();
/* 39 */     Connection conn = this.baseService.getP8Connection();
/*    */     
/*    */ 
/* 42 */     List<SecurityPrincipal>[] permissionList = getPermissionsFromJSON(conn, permissionsJSON);
/*    */     
/* 44 */     String reposIdent = repository.getSymbolicName();
/*    */     
/* 46 */     DataModelType datamodel = repository.getDataModelType();
/* 47 */     ObjectStoreSecurityUpdater securityUpdater = null;
/* 48 */     if (datamodel == DataModelType.DoDBase) {
/* 49 */       securityUpdater = new ObjectStoreDoDSecurity(reposIdent, permissionList, conn);
/* 50 */     } else if (datamodel == DataModelType.PRO2002) {
/* 51 */       securityUpdater = new ObjectStorePROSecurity(reposIdent, permissionList, conn);
/* 52 */     } else if (datamodel == DataModelType.DoDClassified) {
/* 53 */       securityUpdater = new ObjectStoreDoDCh4Security(reposIdent, permissionList, conn);
/*    */     } else {
/* 55 */       securityUpdater = new ObjectStoreBaseSecurity(reposIdent, permissionList, conn);
/*    */     }
/* 57 */     String securityRunDate = securityUpdater.setEntireSecurity();
/*    */     
/* 59 */     JSONObject jsonResponse = new JSONObject();
/* 60 */     jsonResponse.put("securityRunDate", securityRunDate);
/* 61 */     setCompletedJSONResponseObject(jsonResponse);
/*    */   }
/*    */   
/*    */   public static List<SecurityPrincipal>[] getPermissionsFromJSON(Connection connection, JSONArray permissionJSONArray)
/*    */     throws Exception
/*    */   {
/* 67 */     List<SecurityPrincipal>[] permissionList = new List[5];
/* 68 */     for (int index = 0; index < permissionList.length; index++) {
/* 69 */       permissionList[index] = new ArrayList();
/*    */     }
/* 71 */     if (permissionJSONArray.size() > 0) {
/* 72 */       for (int i = 0; i < permissionJSONArray.size(); i++) {
/* 73 */         JSONObject permission = (JSONObject)permissionJSONArray.get(i);
/* 74 */         if (permission != null) {
/* 75 */           String granteeName = permission.get("granteeName").toString();
/* 76 */           String granteeType = permission.get("granteeType").toString();
/* 77 */           String group = permission.get("securityGroup").toString();
/* 78 */           SecurityPrincipal principal = null;
/* 79 */           if (Integer.parseInt(granteeType) == 2000) {
/* 80 */             principal = Factory.User.fetchInstance(connection, granteeName, null);
/*    */           } else {
/* 82 */             principal = Factory.Group.fetchInstance(connection, granteeName, null);
/*    */           }
/* 84 */           int groupInt = Integer.parseInt(group);
/* 85 */           List<SecurityPrincipal> list = permissionList[groupInt];
/* 86 */           list.add(principal);
/*    */         }
/*    */       }
/*    */     }
/* 90 */     return permissionList;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\ObjectStoreSecurityService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */