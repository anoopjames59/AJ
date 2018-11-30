package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.DispositionTriggerType;
import java.util.Date;
import java.util.List;

public abstract interface DispositionTrigger
  extends RMCustomObject
{
  public abstract String getTriggerName();
  
  public abstract void setTriggerName(String paramString);
  
  public abstract DispositionTriggerType getTriggerType();
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract String getAggregation();
  
  public abstract void setAggregation(String paramString);
  
  public abstract String getPropertyName();
  
  public abstract void setPropertyName(String paramString);
  
  public abstract String getPropertyValue();
  
  public abstract void setPropertyValue(String paramString);
  
  public abstract Integer getOperator();
  
  public abstract void setOperator(Integer paramInteger);
  
  public abstract Date getDateTime();
  
  public abstract void setDateTime(Date paramDate);
  
  public abstract Integer[] getRecurringCyclePeriod();
  
  public abstract void setRecurringCyclePeriod(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);
  
  public abstract Date getExternalEventOccurrenceDate();
  
  public abstract void setExternalEventOccurrenceDate(Date paramDate);
  
  public abstract String getConditionXML();
  
  public abstract void setConditionXML(String paramString);
  
  public abstract List<DispositionSchedule> getAssociatedDispositionSchedules();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DispositionTrigger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */