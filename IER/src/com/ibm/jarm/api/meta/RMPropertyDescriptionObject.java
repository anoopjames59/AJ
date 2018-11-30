package com.ibm.jarm.api.meta;

import com.ibm.jarm.api.constants.RMDeletionAction;

public abstract interface RMPropertyDescriptionObject
  extends RMPropertyDescription
{
  public abstract boolean allowsForeignObject();
  
  public abstract RMDeletionAction getDeletionAction();
  
  public abstract String getReflectivePropertyId();
  
  public abstract RMClassDescription getRequiredClass();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescriptionObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */