package com.ibm.jarm.api.meta;

import java.util.List;

public abstract interface RMPropertyDescriptionBinary
  extends RMPropertyDescription
{
  public abstract Integer getBinaryMaximumLength();
  
  public abstract byte[] getBinaryDefaultValue();
  
  public abstract List<byte[]> getBinarySelectionsList();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionBinary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */