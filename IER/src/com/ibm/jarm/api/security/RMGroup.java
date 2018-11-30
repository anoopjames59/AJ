package com.ibm.jarm.api.security;

import com.ibm.jarm.api.collection.PageableSet;

public abstract interface RMGroup
  extends RMPrincipal
{
  public abstract PageableSet<RMGroup> getGroupMemberships();
  
  public abstract PageableSet<RMGroup> getGroupMembers();
  
  public abstract PageableSet<RMUser> getUserMembers();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\security\RMGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */