package com.ibm.jarm.api.core;

public abstract interface NamingPatternSequence
  extends RMCustomObject
{
  public abstract Integer getLastPatternIndex();
  
  public abstract void setLastPatternIndex(Integer paramInteger);
  
  public abstract String getParentIdentifier();
  
  public abstract void setParentIdentifier(String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\NamingPatternSequence.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */