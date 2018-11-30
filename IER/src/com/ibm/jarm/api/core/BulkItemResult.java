package com.ibm.jarm.api.core;

import com.ibm.jarm.api.exception.RMRuntimeException;

public abstract interface BulkItemResult
{
  public abstract String getEntityIdent();
  
  public abstract boolean wasSuccessful();
  
  public abstract RMRuntimeException getException();
  
  public abstract Object getResultData();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\BulkItemResult.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */