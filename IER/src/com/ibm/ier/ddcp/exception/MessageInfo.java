package com.ibm.ier.ddcp.exception;

public abstract interface MessageInfo
{
  public abstract DDCPErrorCode getErrorCode();
  
  public abstract String getFormattedMessage();
  
  public abstract String getExplanation();
  
  public abstract String getAction();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\exception\MessageInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */