package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.RetainMetadata;

public abstract interface FilePlan
  extends Container, RecordCategoryContainer, DefensiblyDisposableContainerParent
{
  public abstract RetainMetadata getRetainMetadata();
  
  public abstract NamingPattern getNamingPattern();
  
  public abstract void setNamingPattern(NamingPattern paramNamingPattern);
  
  public abstract Integer getLastPatternIndex();
  
  public abstract void setLastPatternIndex(Integer paramInteger);
  
  public abstract void delete();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\FilePlan.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */