package com.ibm.jarm.ral.p8ce;

import com.ibm.jarm.api.core.Repository;

public abstract interface IGenerator<T>
{
  public abstract T create(Repository paramRepository, Object paramObject);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\IGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */