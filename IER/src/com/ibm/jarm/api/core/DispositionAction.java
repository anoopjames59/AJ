package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.DispositionActionType;
import java.util.List;

public abstract interface DispositionAction
  extends RMCustomObject
{
  public abstract String getActionName();
  
  public abstract void setActionName(String paramString);
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract DispositionActionType getActionType();
  
  public abstract RMWorkflowDefinition getAssociatedWorkflow();
  
  public abstract void setAssociatedWorkflow(RMWorkflowDefinition paramRMWorkflowDefinition);
  
  public abstract List<DispositionSchedule> getAssociatedDispositionSchedules();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DispositionAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */