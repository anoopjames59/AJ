package com.ibm.jarm.api.collection;

import java.util.List;

public abstract interface CBRPageIterator<T>
  extends RMPageIterator<T>
{
  public abstract boolean hasNextPage();
  
  public abstract boolean hasPrevPage();
  
  public abstract List<T> getNextPage();
  
  public abstract List<T> getPrevPage();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\collection\CBRPageIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */