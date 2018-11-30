package com.ibm.jarm.api.security;

public abstract interface RMPrincipal
{
  public abstract String getDisplayName();
  
  public abstract String getDistinguishedName();
  
  public abstract String getName();
  
  public abstract String getShortName();
  
  public abstract String getId();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\security\RMPrincipal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */