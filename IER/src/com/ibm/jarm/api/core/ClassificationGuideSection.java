package com.ibm.jarm.api.core;

import java.util.List;

public abstract interface ClassificationGuideSection
  extends ClassificationGuideTopic
{
  public abstract List<ClassificationGuideSection> getGuideSections();
  
  public abstract List<ClassificationGuideTopic> getGuideTopics();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\ClassificationGuideSection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */