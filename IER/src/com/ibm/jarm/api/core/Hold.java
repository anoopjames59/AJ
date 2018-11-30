package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;

public abstract interface Hold
  extends RMCustomObject
{
  public abstract String getHoldName();
  
  public abstract void setHoldName(String paramString);
  
  public abstract String getConditionXML();
  
  public abstract void setConditionXML(String paramString);
  
  public abstract Integer getSweepState();
  
  public abstract void setSweepState(Integer paramInteger);
  
  public abstract String getHoldReason();
  
  public abstract void setHoldReason(String paramString);
  
  public abstract Boolean isActive();
  
  public abstract void setActiveState(boolean paramBoolean);
  
  public abstract String getHoldType();
  
  public abstract void setHoldType(String paramString);
  
  public abstract PageableSet<Record> getAssociatedRecords(Integer paramInteger);
  
  public abstract PageableSet<Container> getAssociatedContainers(Integer paramInteger);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Hold.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */