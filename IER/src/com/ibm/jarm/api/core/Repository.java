package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.RepositoryType;
import com.ibm.jarm.api.meta.RMChoiceList;
import com.ibm.jarm.api.meta.RMClassDescription;
import java.util.List;

public abstract interface Repository
  extends BaseEntity
{
  public abstract String getSymbolicName();
  
  public abstract String getDisplayName();
  
  public abstract RMDomain getDomain();
  
  public abstract List<RMChoiceList> fetchChoiceLists();
  
  public abstract List<RMClassDescription> fetchClassDescriptions(String[] paramArrayOfString);
  
  public abstract RepositoryType getRepositoryType();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Repository.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */