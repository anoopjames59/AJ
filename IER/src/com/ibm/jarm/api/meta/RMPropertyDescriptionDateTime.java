package com.ibm.jarm.api.meta;

import java.util.Date;
import java.util.List;

public abstract interface RMPropertyDescriptionDateTime
  extends RMPropertyDescription
{
  public abstract Date getDateTimeDefaultValue();
  
  public abstract Date getDateTimeMaximumValue();
  
  public abstract Date getDateTimeMinimumValue();
  
  public abstract List<Date> getDateTimeSelectionsList();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionDateTime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */