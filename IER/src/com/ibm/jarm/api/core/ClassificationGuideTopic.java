package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.EntityType;
import java.util.Date;
import java.util.List;

public abstract interface ClassificationGuideTopic
{
  public abstract String getId();
  
  public abstract String getCode();
  
  public abstract String getTopic();
  
  public abstract String getInitialClassification();
  
  public abstract List<String> getReasonsForClassification();
  
  public abstract Date getDeclassifyOnDate();
  
  public abstract List<String> getDeclassifyOnEvents();
  
  public abstract List<String> getExemptions();
  
  public abstract String getRemarks();
  
  public abstract List<String> getSupplementalMarkings();
  
  public abstract String getGuideID();
  
  public abstract EntityType getRMEntityType();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\ClassificationGuideTopic.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */