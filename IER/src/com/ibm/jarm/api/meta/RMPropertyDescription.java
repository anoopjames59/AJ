package com.ibm.jarm.api.meta;

import com.ibm.jarm.api.constants.DataType;
import com.ibm.jarm.api.constants.RMCardinality;
import com.ibm.jarm.api.constants.RMPropertySettability;

public abstract interface RMPropertyDescription
{
  public abstract RMCardinality getCardinality();
  
  public abstract RMChoiceList getChoiceList();
  
  public abstract DataType getDataType();
  
  public abstract String getDescriptiveText();
  
  public abstract String getDisplayName();
  
  public abstract String getId();
  
  public abstract String getName();
  
  public abstract RMPropertySettability getSettability();
  
  public abstract String getSymbolicName();
  
  public abstract boolean isHidden();
  
  public abstract boolean isOrderable();
  
  public abstract boolean isReadOnly();
  
  public abstract boolean isSearchable();
  
  public abstract boolean isSelectable();
  
  public abstract boolean isSystemGenerated();
  
  public abstract boolean isSystemOwned();
  
  public abstract boolean isValueRequired();
  
  public abstract boolean requiresUniqueElements();
  
  public abstract boolean isForDeclare();
  
  public abstract boolean isForClassified();
  
  public abstract boolean isRMSystemProperty();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMPropertyDescription.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */