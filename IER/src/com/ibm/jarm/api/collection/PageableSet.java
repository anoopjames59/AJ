package com.ibm.jarm.api.collection;

import java.util.Iterator;

public abstract interface PageableSet<T>
  extends Iterable<T>
{
  public abstract boolean isEmpty();
  
  public abstract boolean supportsPaging();
  
  public abstract RMPageIterator<T> pageIterator();
  
  public abstract Iterator<T> iterator();
  
  public abstract Integer totalCount();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\collection\PageableSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */