package com.ibm.jarm.api.collection;

import java.util.List;

public abstract interface RMPageIterator<T>
{
  public abstract void setPageSize(int paramInt);
  
  public abstract int getPageSize();
  
  public abstract boolean nextPage();
  
  public abstract int getElementCount();
  
  public abstract List<T> getCurrentPage();
  
  public abstract RMPageMark getPageMark();
  
  public abstract void reset(RMPageMark paramRMPageMark);
  
  public abstract void reset();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\collection\RMPageIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */