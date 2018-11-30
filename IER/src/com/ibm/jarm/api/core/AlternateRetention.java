package com.ibm.jarm.api.core;

public abstract interface AlternateRetention
  extends RMCustomObject
{
  public abstract Integer getRetentionNumber();
  
  public abstract DispositionPhase getDispositionPhase();
  
  public abstract String getRetentionBase();
  
  public abstract void setRetentionBase(String paramString);
  
  public abstract Integer[] getRetentionPeriod();
  
  public abstract void setRetentionPeriod(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);
  
  public abstract String getConditionXML();
  
  public abstract void setConditionXML(String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\AlternateRetention.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */