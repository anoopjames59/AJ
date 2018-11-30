package com.ibm.jarm.api.core;

public abstract interface Location
  extends RMCustomObject
{
  public abstract String getLocationName();
  
  public abstract void setLocationName(String paramString);
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract String getBarcode();
  
  public abstract void setBarcode(String paramString);
  
  public abstract String getReviewer();
  
  public abstract void setReviewer(String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Location.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */