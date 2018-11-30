package com.ibm.jarm.api.meta;

import java.util.List;

public abstract interface RMPropertyDescriptionDouble
  extends RMPropertyDescription
{
  public abstract Double getDoubleDefaultValue();
  
  public abstract Double getDoubleMaximumValue();
  
  public abstract Double getDoubleMinimumValue();
  
  public abstract List<Double> getDoubleSelectionsList();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionDouble.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */