package com.ibm.jarm.api.core;

import com.ibm.jarm.api.exception.RMRuntimeException;

public abstract interface DefensiblyDisposable
{
  public abstract RMRuntimeException isEligibleForConversion(String paramString);
  
  public abstract boolean isADefensiblyDisposableContainer();
  
  public abstract void convertToDefensiblyDisposable(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  public abstract String getTriggerPropertyName();
  
  public abstract void setTriggerPropertyName(String paramString);
  
  public abstract int[] getRetentionPeriod();
  
  public abstract void setRetentionPeriod(int paramInt1, int paramInt2, int paramInt3);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DefensiblyDisposable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */