package com.ibm.jarm.api.security;

import com.ibm.jarm.api.constants.RMAccessLevel;
import com.ibm.jarm.api.constants.RMAccessRight;
import com.ibm.jarm.api.constants.RMAccessType;
import com.ibm.jarm.api.constants.RMGranteeType;
import com.ibm.jarm.api.constants.RMPermissionSource;

public abstract interface RMPermission
{
  public abstract void setAccessType(RMAccessType paramRMAccessType);
  
  public abstract RMAccessType getAccessType();
  
  public abstract void setAccessMask(int paramInt);
  
  public abstract void setAccessMask(RMAccessLevel paramRMAccessLevel);
  
  public abstract void setAccessMask(RMAccessRight... paramVarArgs);
  
  public abstract int getAccessMask();
  
  public abstract void setGranteeName(String paramString);
  
  public abstract String getGranteeName();
  
  public abstract RMGranteeType getGranteeType();
  
  public abstract void setInheritableDepth(Integer paramInteger);
  
  public abstract Integer getInheritableDepth();
  
  public abstract RMPermissionSource getPermissionSource();
  
  public abstract String getAuthenticatedUsersDesignation();
  
  public abstract String getCreatorOwnerNameDesignation();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\security\RMPermission.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */