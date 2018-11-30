package com.ibm.jarm.api.core;

import java.io.InputStream;

public abstract interface RMContentElement
{
  public abstract boolean isAContentReference();
  
  public abstract String getContentType();
  
  public abstract String getReferenceContentLocation();
  
  public abstract Double getBinaryContentSize();
  
  public abstract String getBinaryContentRetrievalName();
  
  public abstract InputStream getBinaryContentStream();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RMContentElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */