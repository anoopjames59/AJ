package com.ibm.jarm.api.meta;

import java.util.List;

public abstract interface RMPropertyDescriptionInteger
  extends RMPropertyDescription
{
  public abstract Integer getIntegerDefaultValue();
  
  public abstract Integer getIntegerMaximumValue();
  
  public abstract Integer getIntegerMinimumValue();
  
  public abstract List<Integer> getIntegerSelectionsList();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionInteger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */