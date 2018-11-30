package com.ibm.jarm.ral.p8ce;

import com.filenet.api.core.EngineObject;
import com.filenet.api.property.FilterElement;
import com.ibm.jarm.api.constants.EntityType;
import java.util.List;

public abstract interface JaceBasable
{
  public abstract String getObjectIdentity();
  
  public abstract String getClassName();
  
  public abstract EntityType getEntityType();
  
  public abstract EngineObject getJaceBaseObject();
  
  public abstract List<FilterElement> getMandatoryFEs();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\JaceBasable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */