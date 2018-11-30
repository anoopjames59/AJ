package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;
import com.ibm.jarm.api.constants.ApplyToNameOrID;
import java.util.List;

public abstract interface NamingPattern
  extends RMCustomObject
{
  public abstract String getPatternName();
  
  public abstract void setPatternName(String paramString);
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract ApplyToNameOrID getApplyToNameOrId();
  
  public abstract void setApplyToNameOrId(ApplyToNameOrID paramApplyToNameOrID);
  
  public abstract NamingPatternLevel createNamingPatternLevel();
  
  public abstract NamingPatternLevel createNamingPatternLevel(String paramString);
  
  public abstract NamingPatternLevel getNamingPatternLevel(Integer paramInteger, AppliedForCategoryOrFolder paramAppliedForCategoryOrFolder);
  
  public abstract List<NamingPatternLevel> getNamingPatternLevels();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\NamingPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */