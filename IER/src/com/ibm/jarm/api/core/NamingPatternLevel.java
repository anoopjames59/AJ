package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;

public abstract interface NamingPatternLevel
  extends RMCustomObject
{
  public abstract Integer getPatternLevelNumber();
  
  public abstract void setPatternLevelNumber(Integer paramInteger);
  
  public abstract String getPatternString();
  
  public abstract void setPatternString(String paramString);
  
  public abstract Integer getIncrementedBy();
  
  public abstract void setIncrementedBy(Integer paramInteger);
  
  public abstract AppliedForCategoryOrFolder getAppliedFor();
  
  public abstract void setAppliedFor(AppliedForCategoryOrFolder paramAppliedForCategoryOrFolder);
  
  public abstract NamingPattern getNamingPattern();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\NamingPatternLevel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */