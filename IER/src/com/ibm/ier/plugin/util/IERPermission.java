/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.RMAccessType;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMPermission;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class IERPermission
/*     */ {
/*     */   private final String id;
/*     */   private final String granteeName;
/*     */   private final String displayName;
/*     */   private final int granteeType;
/*     */   private final int accessMask;
/*     */   private final int accessType;
/*     */   private final int inheritableDepth;
/*     */   private final int permissionSource;
/*     */   
/*     */   public IERPermission(String id, String granteeName, String displayName, int granteeType, int accessMask, int accessType, int inheritableDepth, int permissionSource)
/*     */   {
/*  29 */     this.id = id;
/*  30 */     this.granteeName = granteeName;
/*  31 */     this.displayName = displayName;
/*  32 */     this.granteeType = granteeType;
/*  33 */     this.accessMask = accessMask;
/*  34 */     this.accessType = accessType;
/*  35 */     this.inheritableDepth = inheritableDepth;
/*  36 */     this.permissionSource = permissionSource;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  40 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getGranteeName() {
/*  44 */     return this.granteeName;
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/*  48 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public int geGranteeType() {
/*  52 */     return this.granteeType;
/*     */   }
/*     */   
/*     */   public int getAccessMask() {
/*  56 */     return this.accessMask;
/*     */   }
/*     */   
/*     */   public int getAccessType() {
/*  60 */     return this.accessType;
/*     */   }
/*     */   
/*     */   public int getInheritableDepth() {
/*  64 */     return this.inheritableDepth;
/*     */   }
/*     */   
/*     */   public int getPermissionSource() {
/*  68 */     return this.permissionSource;
/*     */   }
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
/*     */   public static List<RMPermission> getPermissionsFromJSON(String permissionJSON)
/*     */     throws Exception
/*     */   {
/*  83 */     List<RMPermission> permissionList = null;
/*  84 */     if (permissionJSON != null) {
/*  85 */       JSONArray permissionJSONArray = JSONArray.parse(permissionJSON);
/*  86 */       permissionList = getPermissionsFromJSON(permissionJSONArray);
/*     */     }
/*     */     
/*  89 */     return permissionList;
/*     */   }
/*     */   
/*     */   public static List<RMPermission> getPermissionsFromJSON(JSONArray permissionJSONArray) throws Exception {
/*  93 */     List<RMPermission> permissionList = new ArrayList();
/*  94 */     if (permissionJSONArray.size() > 0) {
/*  95 */       for (int i = 0; i < permissionJSONArray.size(); i++) {
/*  96 */         JSONObject permission = (JSONObject)permissionJSONArray.get(i);
/*  97 */         RMPermission accessPermission = RMFactory.RMPermission.createInstance(DomainType.P8_CE);
/*  98 */         accessPermission.setAccessType(Integer.parseInt(permission.get("accessType").toString()) == RMAccessType.Allow.getIntValue() ? RMAccessType.Allow : RMAccessType.Deny);
/*  99 */         accessPermission.setAccessMask(Integer.parseInt(permission.get("accessMask").toString()));
/* 100 */         accessPermission.setInheritableDepth(Integer.valueOf(Integer.parseInt(permission.get("inheritableDepth").toString())));
/* 101 */         accessPermission.setGranteeName(permission.get("granteeName").toString());
/* 102 */         permissionList.add(accessPermission);
/*     */       }
/*     */     }
/* 105 */     return permissionList;
/*     */   }
/*     */   
/*     */   public static AccessPermissionList getCEPermissionsFromJSON(String permissionJSON) throws Exception {
/* 109 */     AccessPermissionList permissionList = null;
/* 110 */     if (permissionJSON != null) {
/* 111 */       JSONArray permissionJSONArray = JSONArray.parse(permissionJSON);
/* 112 */       permissionList = getCEPermissionsFromJSON(permissionJSONArray);
/*     */     }
/*     */     
/* 115 */     return permissionList;
/*     */   }
/*     */   
/*     */   public static AccessPermissionList getCEPermissionsFromJSON(JSONArray permissionJSONArray) throws Exception
/*     */   {
/* 120 */     AccessPermissionList permissionList = Factory.AccessPermission.createList();
/* 121 */     if (permissionJSONArray.size() > 0) {
/* 122 */       for (int i = 0; i < permissionJSONArray.size(); i++) {
/* 123 */         JSONObject permission = (JSONObject)permissionJSONArray.get(i);
/* 124 */         AccessPermission accessPermission = Factory.AccessPermission.createInstance();
/* 125 */         accessPermission.set_AccessType(Integer.parseInt(permission.get("accessType").toString()) == 1 ? AccessType.ALLOW : AccessType.DENY);
/* 126 */         accessPermission.set_AccessMask(Integer.valueOf(Integer.parseInt(permission.get("accessMask").toString())));
/* 127 */         accessPermission.set_InheritableDepth(Integer.valueOf(Integer.parseInt(permission.get("inheritableDepth").toString())));
/* 128 */         accessPermission.set_GranteeName(permission.get("granteeName").toString());
/* 129 */         permissionList.add(accessPermission);
/*     */       }
/*     */     }
/* 132 */     return permissionList;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\IERPermission.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */