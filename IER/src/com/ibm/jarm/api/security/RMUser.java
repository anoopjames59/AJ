package com.ibm.jarm.api.security;

import com.ibm.jarm.api.collection.PageableSet;

public abstract interface RMUser
  extends RMPrincipal
{
  public abstract String getEmailName();
  
  public abstract PageableSet<RMGroup> getGroupMemberships();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\security\RMUser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */