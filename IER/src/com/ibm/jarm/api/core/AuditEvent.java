package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.AuditStatus;
import java.util.List;

public abstract interface AuditEvent
  extends BaseEntity
{
  public abstract String getAuditActionType();
  
  public abstract String getDescription();
  
  public abstract String getReasonForAction();
  
  public abstract String getReviewer();
  
  public abstract AuditStatus getEventStatus();
  
  public abstract String getInitiatingUser();
  
  public abstract BaseEntity getOriginalObject();
  
  public abstract String getSourceObjectId();
  
  public abstract String getSourceClassId();
  
  public abstract BaseEntity getSourceObject();
  
  public abstract List<String> getModifiedPropertyNames();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\AuditEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */