package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.RMRefreshMode;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface Persistable
{
  public abstract void setPermissions(List<RMPermission> paramList);
  
  public abstract void save(RMRefreshMode paramRMRefreshMode);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Persistable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */