package com.ibm.jarm.api.meta;

import com.ibm.jarm.api.core.BaseEntity;
import java.util.List;

public abstract interface RMMarkingSet
  extends BaseEntity
{
  public abstract String getDescriptiveText();
  
  public abstract String getDisplayName();
  
  public abstract String getId();
  
  public abstract boolean isHierarchical();
  
  public abstract List<RMMarkingItem> getMarkings();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMMarkingSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */