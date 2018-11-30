package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.SchedulePropagation;

public abstract interface DispositionAllocatable
{
  public abstract void assignDispositionSchedule(DispositionSchedule paramDispositionSchedule, SchedulePropagation paramSchedulePropagation);
  
  public abstract void clearDispositionAssignment(SchedulePropagation paramSchedulePropagation);
  
  public abstract DispositionSchedule getAssignedSchedule();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DispositionAllocatable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */