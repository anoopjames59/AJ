package com.ibm.jarm.api.core;

import com.ibm.jarm.api.property.RMProperties;
import java.util.Date;

public abstract interface Vitalable
{
  public abstract void setVital(RMProperties paramRMProperties);
  
  public abstract void executeVital();
  
  public abstract void updateVitalStatus(Date paramDate);
  
  public abstract boolean isVital();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Vitalable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */