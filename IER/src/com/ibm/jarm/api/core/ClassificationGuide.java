package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.EntityType;
import java.util.Date;
import java.util.List;

public abstract interface ClassificationGuide
{
  public abstract String getGuideName();
  
  public abstract String getDescription();
  
  public abstract Date getDateIssued();
  
  public abstract String getIssuedBy();
  
  public abstract String getApprovedBy();
  
  public abstract EntityType getEntityType();
  
  public abstract String getId();
  
  public abstract List<ClassificationGuideSection> getGuideSections();
  
  public abstract List<ClassificationGuideTopic> getGuideTopics();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\ClassificationGuide.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */