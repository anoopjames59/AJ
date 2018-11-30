package com.ibm.jarm.api.core;

import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface DefensiblyDisposableContainerParent
{
  public abstract boolean canContainDefensibleDisposalContainer();
  
  public abstract RecordContainer addDefensiblyDisposableContainer(String paramString1, int paramInt1, int paramInt2, int paramInt3, RMProperties paramRMProperties, List<RMPermission> paramList, String paramString2);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DefensiblyDisposableContainerParent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */