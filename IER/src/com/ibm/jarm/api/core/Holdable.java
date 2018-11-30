package com.ibm.jarm.api.core;

import java.util.List;

public abstract interface Holdable
{
  public abstract void placeHold(Hold paramHold);
  
  public abstract void removeHold(Hold paramHold);
  
  public abstract boolean isOnHold(boolean paramBoolean);
  
  public abstract boolean isAnyParentOnHold();
  
  public abstract List<Hold> getAssociatedHolds();
  
  public abstract List<RecordContainer> getParentsOnHold();
  
  public abstract boolean isAnyChildOnHold();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Holdable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */