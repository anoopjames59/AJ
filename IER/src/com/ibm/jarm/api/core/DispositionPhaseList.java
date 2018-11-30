package com.ibm.jarm.api.core;

import java.util.Iterator;

public abstract interface DispositionPhaseList
  extends Iterable<DispositionPhase>
{
  public abstract int add(DispositionPhase paramDispositionPhase);
  
  public abstract DispositionPhase get(int paramInt);
  
  public abstract void remove(int paramInt);
  
  public abstract void remove(DispositionPhase paramDispositionPhase);
  
  public abstract void remove(String paramString);
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(DispositionPhase paramDispositionPhase);
  
  public abstract Iterator<DispositionPhase> iterator();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DispositionPhaseList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */