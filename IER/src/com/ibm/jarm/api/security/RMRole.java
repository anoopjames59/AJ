package com.ibm.jarm.api.security;

import java.util.List;

public abstract interface RMRole
{
  public abstract String getObjectIdentity();
  
  public abstract String getRoleName();
  
  public abstract String getApplicationName();
  
  public abstract String getRoleType();
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract void addMember(RMPrincipal paramRMPrincipal);
  
  public abstract void removeMember(RMPrincipal paramRMPrincipal);
  
  public abstract List<RMPrincipal> getMembers();
  
  public abstract boolean isCurrentUserInRole();
  
  public abstract boolean isPrincipalInRole(RMPrincipal paramRMPrincipal);
  
  public abstract void delete();
  
  public abstract String getPrivileges();
  
  public abstract void setPrivileges(String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\security\RMRole.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */