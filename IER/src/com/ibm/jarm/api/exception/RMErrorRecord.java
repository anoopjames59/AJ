package com.ibm.jarm.api.exception;

public abstract interface RMErrorRecord
{
  public abstract String getDescription();
  
  public abstract String getSource();
  
  public abstract Diagnostic[] getDiagnosticTypes();
  
  public abstract String toString();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\exception\RMErrorRecord.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */