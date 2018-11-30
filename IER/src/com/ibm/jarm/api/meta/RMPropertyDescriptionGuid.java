package com.ibm.jarm.api.meta;

import java.util.List;

public abstract interface RMPropertyDescriptionGuid
  extends RMPropertyDescription
{
  public abstract String getGuidDefaultValue();
  
  public abstract List<String> getGuidSelectionsList();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionGuid.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */