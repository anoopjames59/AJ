package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.DomainType;
import com.ibm.jarm.api.constants.EntityType;
import com.ibm.jarm.api.meta.RMClassDescription;
import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.property.RMPropertyFilter;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface BaseEntity
{
  public abstract String getClientIdentifier();
  
  public abstract String getObjectIdentity();
  
  public abstract String getClassName();
  
  public abstract RMClassDescription getClassDescription();
  
  public abstract String getName();
  
  public abstract EntityType getEntityType();
  
  public abstract Repository getRepository();
  
  public abstract boolean isPlaceholder();
  
  public abstract boolean isCreationPending();
  
  public abstract RMProperties getProperties();
  
  public abstract List<Container> getContainedBy();
  
  public abstract List<RMPermission> getPermissions();
  
  public abstract void refresh();
  
  public abstract void refresh(String[] paramArrayOfString);
  
  public abstract void refresh(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract Integer getAccessAllowed();
  
  public abstract DomainType getDomainType();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\BaseEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */