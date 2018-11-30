package com.ibm.jarm.api.meta;

import java.util.List;

public abstract interface RMPropertyDescriptionString
  extends RMPropertyDescription
{
  public abstract RMMarkingSet getMarkingSet();
  
  public abstract Integer getStringMaximumLength();
  
  public abstract String getStringDefaultValue();
  
  public abstract List<String> getStringSelectionsList();
  
  public abstract boolean isCBREnabled();
  
  public abstract boolean usesLongColumn();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */