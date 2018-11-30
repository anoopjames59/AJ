package com.ibm.jarm.api.exception;

public abstract interface RMErrorStack
{
  public abstract RMErrorRecord[] getErrorRecords();
  
  public abstract String toString();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\exception\RMErrorStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */