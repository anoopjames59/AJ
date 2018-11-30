package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.constants.DeleteMode;
import com.ibm.jarm.api.constants.EntityType;

public abstract interface Container
  extends BaseEntity, Persistable
{
  public abstract EntityType[] getAllowedContaineeTypes();
  
  public abstract boolean canContain(EntityType paramEntityType);
  
  public abstract String getFolderName();
  
  public abstract String getPathName();
  
  public abstract Container getParent();
  
  public abstract PageableSet<Container> getImmediateSubContainers(Integer paramInteger);
  
  public abstract void delete(boolean paramBoolean, DeleteMode paramDeleteMode, String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Container.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */