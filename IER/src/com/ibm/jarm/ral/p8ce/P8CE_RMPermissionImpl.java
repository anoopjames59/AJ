/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.constants.AccessType;
/*     */ import com.filenet.api.constants.PermissionSource;
/*     */ import com.filenet.api.constants.SecurityPrincipalType;
/*     */ import com.filenet.api.constants.SpecialPrincipal;
/*     */ import com.filenet.api.core.Factory.AccessPermission;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.security.AccessPermission;
/*     */ import com.ibm.jarm.api.constants.RMAccessLevel;
/*     */ import com.ibm.jarm.api.constants.RMAccessRight;
/*     */ import com.ibm.jarm.api.constants.RMAccessType;
/*     */ import com.ibm.jarm.api.constants.RMGranteeType;
/*     */ import com.ibm.jarm.api.constants.RMPermissionSource;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMPermissionImpl
/*     */   implements RMPermission
/*     */ {
/*  32 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
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
/*  43 */   static final String[] MandatoryPropertyNames = { "GranteeName", "GranteeType", "AccessMask", "AccessType", "InheritableDepth", "PermissionSource" };
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   private AccessPermission jacePermission;
/*     */   private boolean createdLocally;
/*     */   
/*     */   static
/*     */   {
/*  50 */     List<FilterElement> tempList = new ArrayList(1);
/*  51 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  52 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null));
/*  53 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  58 */     return MandatoryJaceFEs;
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
/*     */   P8CE_RMPermissionImpl()
/*     */   {
/*  72 */     Tracer.traceMethodEntry(new Object[0]);
/*  73 */     this.jacePermission = Factory.AccessPermission.createInstance();
/*  74 */     this.createdLocally = true;
/*  75 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_RMPermissionImpl(AccessPermission jacePermission)
/*     */   {
/*  85 */     Tracer.traceMethodEntry(new Object[] { jacePermission });
/*  86 */     this.jacePermission = jacePermission;
/*  87 */     this.createdLocally = false;
/*  88 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAuthenticatedUsersDesignation()
/*     */   {
/*  96 */     return SpecialPrincipal.AUTHENTICATED_USERS.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCreatorOwnerNameDesignation()
/*     */   {
/* 104 */     return SpecialPrincipal.CREATOR_OWNER.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getAccessMask()
/*     */   {
/* 112 */     return this.jacePermission.get_AccessMask().intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMAccessType getAccessType()
/*     */   {
/* 120 */     Tracer.traceMethodEntry(new Object[0]);
/* 121 */     AccessType jaceAccessType = this.jacePermission.get_AccessType();
/* 122 */     RMAccessType result = RMAccessType.Allow;
/* 123 */     if (AccessType.DENY.equals(jaceAccessType)) {
/* 124 */       result = RMAccessType.Deny;
/*     */     }
/* 126 */     Tracer.traceMethodExit(new Object[] { result });
/* 127 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGranteeName()
/*     */   {
/* 135 */     return this.jacePermission.get_GranteeName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMGranteeType getGranteeType()
/*     */   {
/* 143 */     Tracer.traceMethodEntry(new Object[0]);
/* 144 */     RMGranteeType result = RMGranteeType.Unknown;
/*     */     
/* 146 */     SecurityPrincipalType jaceGranteeType = this.jacePermission.get_GranteeType();
/* 147 */     if (SecurityPrincipalType.USER.equals(jaceGranteeType)) {
/* 148 */       result = RMGranteeType.User;
/* 149 */     } else if (SecurityPrincipalType.GROUP.equals(jaceGranteeType)) {
/* 150 */       result = RMGranteeType.Group;
/*     */     }
/* 152 */     Tracer.traceMethodExit(new Object[] { result });
/* 153 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getInheritableDepth()
/*     */   {
/* 161 */     return this.jacePermission.get_InheritableDepth();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMPermissionSource getPermissionSource()
/*     */   {
/* 169 */     Tracer.traceMethodEntry(new Object[0]);
/* 170 */     RMPermissionSource result = null;
/* 171 */     if (this.createdLocally)
/*     */     {
/* 173 */       result = RMPermissionSource.Direct;
/*     */     }
/*     */     else
/*     */     {
/* 177 */       PermissionSource jacePermSource = this.jacePermission.get_PermissionSource();
/* 178 */       switch (jacePermSource.getValue())
/*     */       {
/*     */       case 1: 
/* 181 */         result = RMPermissionSource.Default;
/* 182 */         break;
/*     */       case 0: 
/* 184 */         result = RMPermissionSource.Direct;
/* 185 */         break;
/*     */       case 3: 
/* 187 */         result = RMPermissionSource.Parent;
/* 188 */         break;
/*     */       case 255: 
/* 190 */         result = RMPermissionSource.Proxy;
/* 191 */         break;
/*     */       case 2: 
/* 193 */         result = RMPermissionSource.Template;
/* 194 */         break;
/*     */       default: 
/* 196 */         result = null;
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 201 */     Tracer.traceMethodExit(new Object[] { result });
/* 202 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAccessMask(RMAccessLevel accessLevel)
/*     */   {
/* 211 */     Tracer.traceMethodEntry(new Object[] { accessLevel });
/* 212 */     Util.ckNullObjParam("accessLevel", accessLevel);
/* 213 */     setAccessMask(accessLevel.getIntValue());
/* 214 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAccessMask(RMAccessRight... accessRights)
/*     */   {
/* 222 */     Tracer.traceMethodEntry(new Object[] { accessRights });
/* 223 */     Util.ckNullOrInvalidArrayParam("accessRights", accessRights);
/* 224 */     int fullAccessMask = 0;
/* 225 */     for (RMAccessRight accessRight : accessRights)
/*     */     {
/* 227 */       fullAccessMask |= accessRight.getIntValue();
/*     */     }
/* 229 */     setAccessMask(fullAccessMask);
/* 230 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAccessMask(int accessMask)
/*     */   {
/* 238 */     this.jacePermission.set_AccessMask(Integer.valueOf(accessMask));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAccessType(RMAccessType accessType)
/*     */   {
/* 246 */     Tracer.traceMethodEntry(new Object[] { accessType });
/* 247 */     AccessType jaceAccessType = AccessType.ALLOW;
/* 248 */     if (RMAccessType.Deny.equals(accessType)) {
/* 249 */       jaceAccessType = AccessType.DENY;
/*     */     }
/* 251 */     this.jacePermission.set_AccessType(jaceAccessType);
/* 252 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGranteeName(String granteeName)
/*     */   {
/* 260 */     this.jacePermission.set_GranteeName(granteeName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInheritableDepth(Integer inheritableDepth)
/*     */   {
/* 268 */     this.jacePermission.set_InheritableDepth(inheritableDepth);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   AccessPermission getJacePermission()
/*     */   {
/* 278 */     return this.jacePermission;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMPermissionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */