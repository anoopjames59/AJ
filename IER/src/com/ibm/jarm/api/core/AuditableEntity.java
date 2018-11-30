package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.property.RMPropertyFilter;

public abstract interface AuditableEntity
{
  public abstract PageableSet<AuditEvent> getAuditedEvents(RMPropertyFilter paramRMPropertyFilter);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\AuditableEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */