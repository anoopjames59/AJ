package com.ibm.jarm.api.core;

import java.util.Iterator;

public abstract interface AlternateRetentionList
  extends Iterable<AlternateRetention>
{
  public abstract int add(AlternateRetention paramAlternateRetention);
  
  public abstract AlternateRetention get(int paramInt);
  
  public abstract void remove(int paramInt);
  
  public abstract void remove(AlternateRetention paramAlternateRetention);
  
  public abstract void remove(String paramString);
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(AlternateRetention paramAlternateRetention);
  
  public abstract Iterator<AlternateRetention> iterator();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\AlternateRetentionList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */