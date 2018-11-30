package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.DomainType;
import java.util.Map;

public abstract interface DomainConnection
{
  public abstract DomainType getDomainType();
  
  public abstract String getUrl();
  
  public abstract Map<String, Object> getConnectionInfo();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DomainConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */