/*     */ package com.ibm.ier.plugin.mediators;
/*     */ 
/*     */ import com.filenet.api.collection.AccessPermissionList;
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.constants.PermissionSource;
/*     */ import com.filenet.api.constants.SecurityPrincipalType;
/*     */ import com.filenet.api.constants.SpecialPrincipal;
/*     */ import com.filenet.api.core.Connection;
/*     */ import com.filenet.api.core.Factory.Group;
/*     */ import com.filenet.api.core.Factory.User;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.filenet.api.security.Group;
/*     */ import com.filenet.api.security.User;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ public class GetObjectStoreSecurityMediator
/*     */   extends BaseMediator
/*     */ {
/*     */   private static final long serialVersionUID = 3966817027633720289L;
/*  27 */   private final List<Permission> permissions = new ArrayList();
/*     */   public static final String RM_GROUP_SEPARATOR = "<-RM_GROUP_SEPARATOR->";
/*     */   public static final String RM_MEMBER_SEPARATOR = "<-RM_MEMBER_SEPARATOR->";
/*     */   
/*     */   private class Permission
/*     */   {
/*     */     private String id;
/*     */     private final int group;
/*     */     private String granteeName;
/*     */     private String displayName;
/*     */     private final int granteeType;
/*     */     private final int accessMask;
/*     */     private final int accessType;
/*     */     private final int inheritableDepth;
/*     */     private final int permissionSource;
/*     */     
/*     */     public Permission(String id, int group, String granteeName, String displayName, int granteeType) {
/*  44 */       this.id = id;
/*  45 */       this.group = group;
/*  46 */       this.granteeName = granteeName;
/*  47 */       this.displayName = displayName;
/*  48 */       this.granteeType = granteeType;
/*  49 */       this.accessMask = 0;
/*  50 */       this.accessType = 0;
/*  51 */       this.inheritableDepth = 0;
/*  52 */       this.permissionSource = 0;
/*     */     }
/*     */     
/*  55 */     public Permission(String id, String granteeName, String displayName, int granteeType, int accessMask, int accessType, int inheritableDepth, int permissionSource) { this.id = id;
/*  56 */       this.group = 0;
/*  57 */       this.granteeName = granteeName;
/*  58 */       this.displayName = displayName;
/*  59 */       this.granteeType = granteeType;
/*  60 */       this.accessMask = accessMask;
/*  61 */       this.accessType = accessType;
/*  62 */       this.inheritableDepth = inheritableDepth;
/*  63 */       this.permissionSource = permissionSource;
/*     */     }
/*     */     
/*  66 */     public String getId() { return this.id; }
/*     */     
/*     */     public int getGroup() {
/*  69 */       return this.group;
/*     */     }
/*     */     
/*  72 */     public String getGranteeName() { return this.granteeName; }
/*     */     
/*     */     public String getDisplayName() {
/*  75 */       return this.displayName;
/*     */     }
/*     */     
/*  78 */     public int getGranteeType() { return this.granteeType; }
/*     */     
/*     */     public int getAccessMask() {
/*  81 */       return this.accessMask;
/*     */     }
/*     */     
/*  84 */     public int getAccessType() { return this.accessType; }
/*     */     
/*     */     public int getInheritableDepth() {
/*  87 */       return this.inheritableDepth;
/*     */     }
/*     */     
/*  90 */     public int getPermissionSource() { return this.permissionSource; }
/*     */     
/*     */ 
/*     */ 
/*     */     public void setId(String id)
/*     */     {
/*  96 */       this.id = id;
/*     */     }
/*     */     
/*  99 */     public void setGranteeName(String granteeName) { this.granteeName = granteeName; }
/*     */     
/*     */     public void setDisplayName(String displayName) {
/* 102 */       this.displayName = displayName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public GetObjectStoreSecurityMediator(IERBaseService baseService, HttpServletRequest request)
/*     */   {
/* 110 */     super(baseService, request);
/*     */   }
/*     */   
/*     */   public void addPermission(Connection connection, String line) {
/* 114 */     String[] groups = line.split("<-RM_GROUP_SEPARATOR->");
/* 115 */     if ((groups != null) && (1 < groups.length)) {
/* 116 */       int securityGroup = Integer.parseInt(groups[0]);
/* 117 */       for (int index = 1; index < groups.length; index++) {
/* 118 */         String[] members = groups[index].split("<-RM_MEMBER_SEPARATOR->");
/* 119 */         if ((members != null) && (1 < members.length)) {
/* 120 */           String granteeName = members[0];
/* 121 */           int granteeType = Integer.parseInt(members[1]);
/* 122 */           Permission permission = new Permission(granteeName, securityGroup, granteeName, granteeName, granteeType);
/* 123 */           updatePermissionNames(connection, permission, true);
/* 124 */           this.permissions.add(permission);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAccessPermissionList(Connection connection, AccessPermissionList apl)
/*     */   {
/* 132 */     List<Permission> permissionList = getPermissionList(connection, apl, true);
/* 133 */     this.permissions.addAll(permissionList);
/*     */   }
/*     */   
/*     */   public JSONObject toJSONObject() {
/* 137 */     String methodName = "toJSONObject";
/* 138 */     Logger.logEntry(this, methodName, this.servletRequest);
/*     */     
/* 140 */     JSONObject jsonObj = new JSONObject();
/* 141 */     jsonObj.put("acl", permissionsToJSONObject(this.permissions));
/*     */     
/* 143 */     Logger.logDebug(this, methodName, this.servletRequest, "Returning JSON:" + jsonObj.toString());
/* 144 */     Logger.logExit(this, methodName, this.servletRequest);
/*     */     
/* 146 */     return jsonObj;
/*     */   }
/*     */   
/*     */   private static JSONArray permissionsToJSONObject(List<Permission> permissions) {
/* 150 */     JSONArray jsonPermissions = new JSONArray();
/* 151 */     if (permissions != null)
/*     */     {
/* 153 */       for (Permission permission : permissions) {
/* 154 */         JSONObject jsonPermission = new JSONObject();
/* 155 */         jsonPermission.put("id", permission.getId());
/* 156 */         jsonPermission.put("granteeName", permission.getGranteeName());
/* 157 */         jsonPermission.put("displayName", permission.getDisplayName());
/* 158 */         jsonPermission.put("granteeType", Integer.valueOf(permission.getGranteeType()));
/* 159 */         jsonPermission.put("accessMask", Integer.valueOf(permission.getAccessMask()));
/* 160 */         jsonPermission.put("accessType", Integer.valueOf(permission.getAccessType()));
/* 161 */         jsonPermission.put("inheritableDepth", Integer.valueOf(permission.getInheritableDepth()));
/* 162 */         jsonPermission.put("permissionSource", Integer.valueOf(permission.getPermissionSource()));
/* 163 */         jsonPermission.put("securityGroup", Integer.valueOf(permission.getGroup()));
/* 164 */         jsonPermissions.add(jsonPermission);
/*     */       }
/*     */     }
/* 167 */     return jsonPermissions;
/*     */   }
/*     */   
/*     */   private List<Permission> getPermissionList(Connection connection, AccessPermissionList apl, boolean setCreatorOwner) {
/* 171 */     List<Permission> permissionList = new ArrayList();
/* 172 */     Iterator it = apl.iterator();
/*     */     
/*     */ 
/* 175 */     while (it.hasNext()) {
/* 176 */       AccessPermission ap = (AccessPermission)it.next();
/* 177 */       String granteeId = ap.get_GranteeName();
/* 178 */       Permission permission = new Permission(granteeId, granteeId, granteeId, ap.get_GranteeType().getValue(), ap.get_AccessMask().intValue(), ap.get_AccessType().getValue(), ap.get_InheritableDepth().intValue(), ap.get_PermissionSource().getValue());
/* 179 */       updatePermissionNames(connection, permission, setCreatorOwner);
/* 180 */       permissionList.add(permission);
/*     */     }
/*     */     
/* 183 */     return permissionList;
/*     */   }
/*     */   
/*     */   private static void updatePermissionNames(Connection connection, Permission permission, boolean setCreatorOwner) {
/* 187 */     if (connection == null) {
/* 188 */       return;
/*     */     }
/*     */     
/*     */ 
/* 192 */     String granteeId = permission.getId();
/* 193 */     String granteeName = permission.getGranteeName();
/* 194 */     String displayName = permission.getDisplayName();
/*     */     
/*     */ 
/* 197 */     if ((setCreatorOwner) && (granteeName.equalsIgnoreCase(SpecialPrincipal.CREATOR_OWNER.getValue()))) {
/* 198 */       User currentUser = Factory.User.fetchCurrent(connection, null);
/* 199 */       granteeId = currentUser.get_Id();
/* 200 */       granteeName = currentUser.get_ShortName();
/* 201 */       displayName = currentUser.get_DisplayName();
/*     */     }
/* 203 */     else if (!granteeName.equalsIgnoreCase(SpecialPrincipal.AUTHENTICATED_USERS.getValue())) {
/*     */       try {
/* 205 */         if (permission.getGranteeType() == 2000) {
/* 206 */           User user = Factory.User.fetchInstance(connection, granteeId, null);
/* 207 */           granteeId = user.get_Id();
/* 208 */           granteeName = user.get_ShortName();
/* 209 */           displayName = user.get_DisplayName();
/*     */         }
/*     */         else {
/* 212 */           Group group = Factory.Group.fetchInstance(connection, granteeId, null);
/* 213 */           granteeId = group.get_Id();
/* 214 */           granteeName = group.get_ShortName();
/* 215 */           displayName = group.get_DisplayName();
/*     */         }
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/* 221 */     if ((granteeName == null) || (granteeName.length() == 0)) {
/* 222 */       granteeName = granteeId;
/*     */     }
/*     */     
/* 225 */     if ((displayName == null) || (displayName.length() == 0)) {
/* 226 */       displayName = granteeName;
/*     */     }
/*     */     
/* 229 */     permission.setId(granteeId);
/* 230 */     permission.setGranteeName(granteeName);
/* 231 */     permission.setDisplayName(displayName);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\mediators\GetObjectStoreSecurityMediator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */