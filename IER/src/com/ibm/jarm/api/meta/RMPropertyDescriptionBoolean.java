package com.ibm.jarm.api.meta;

import java.util.List;

public abstract interface RMPropertyDescriptionBoolean
  extends RMPropertyDescription
{
  public abstract Boolean getBooleanDefaultValue();
  
  public abstract List<Boolean> getBooleanSelectionsList();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionBoolean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */