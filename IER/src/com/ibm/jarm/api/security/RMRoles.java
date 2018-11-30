package com.ibm.jarm.api.security;

import com.ibm.jarm.api.core.Repository;
import java.util.Iterator;
import java.util.List;

public abstract interface RMRoles
  extends Iterable<RMRole>
{
  public abstract Repository getRepository();
  
  public abstract String getApplicationName();
  
  public abstract boolean isCurrentUserInAnyRole(List<RMRole> paramList);
  
  public abstract RMRole addNewRole(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract RMRole getRoleByNames(String paramString1, String paramString2);
  
  public abstract RMRole getRoleById(String paramString);
  
  public abstract Iterator<RMRole> iterator();
  
  public abstract int size();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\security\RMRoles.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */