package com.ibm.jarm.api.meta;

import com.ibm.jarm.api.constants.DataType;
import com.ibm.jarm.api.core.BaseEntity;
import java.util.List;

public abstract interface RMChoiceList
  extends BaseEntity
{
  public abstract List<RMChoiceItem> getChoiceListValues();
  
  public abstract DataType getDataType();
  
  public abstract String getDescriptiveText();
  
  public abstract String getDisplayName();
  
  public abstract boolean hasHierarchy();
  
  public abstract String getId();
  
  public abstract String getName();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMChoiceList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */