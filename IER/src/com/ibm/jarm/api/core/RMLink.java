package com.ibm.jarm.api.core;

public abstract interface RMLink
  extends BaseEntity, Persistable
{
  public abstract String getLinkName();
  
  public abstract String getDescription();
  
  public abstract BaseEntity getHead();
  
  public abstract BaseEntity getTail();
  
  public abstract void delete();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RMLink.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */