package com.ibm.jarm.api.meta;

import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface RMClassDescription
{
  public abstract boolean allowsInstances();
  
  public abstract boolean canIncludeSubclassesInQuery();
  
  public abstract boolean canIncludeDescendentPropertiesInQuery();
  
  public abstract List<RMPropertyDescription> getAllDescendentPropertyDescriptions();
  
  public abstract String getDefaultInstanceOwner();
  
  public abstract List<RMPermission> getDefaultInstancePermissions();
  
  public abstract String getDescriptiveText();
  
  public abstract String getDisplayName();
  
  public abstract String getId();
  
  public abstract List<RMClassDescription> getImmediateSubclassDescriptions();
  
  public abstract String getName();
  
  public abstract Integer getNamePropertyIndex();
  
  public abstract List<RMPropertyDescription> getPropertyDescriptions();
  
  public abstract RMPropertyDescription getPropertyDescription(String paramString);
  
  public abstract RMClassDescription getSuperclassDescription();
  
  public abstract Integer getImmediateInheritedPropertyCount();
  
  public abstract String getSymbolicName();
  
  public abstract boolean isCBREnabled();
  
  public abstract boolean isHidden();
  
  public abstract boolean describedIsOfClass(String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMClassDescription.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */