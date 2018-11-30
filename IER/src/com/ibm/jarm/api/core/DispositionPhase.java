package com.ibm.jarm.api.core;

public abstract interface DispositionPhase
  extends RMCustomObject
{
  public abstract String getPhaseName();
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract DispositionAction getPhaseAction();
  
  public abstract void setPhaseAction(DispositionAction paramDispositionAction);
  
  public abstract boolean isScreeningRequired();
  
  public abstract void setScreeningRequired(boolean paramBoolean);
  
  public abstract Integer[] getRetentionPeriod();
  
  public abstract void setRetentionPeriod(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);
  
  public abstract String getReasonForChange();
  
  public abstract void setReasonForChange(String paramString);
  
  public abstract String getExportDestination();
  
  public abstract void setExportDestination(String paramString);
  
  public abstract ContentItem getExportFormat();
  
  public abstract void setExportFormat(ContentItem paramContentItem);
  
  public abstract Integer getPhaseNumber();
  
  public abstract DispositionSchedule getAssociatedSchedule();
  
  public abstract AlternateRetentionList getAlternateRetentions();
  
  public abstract AlternateRetention createAlternateRetention();
  
  public abstract AlternateRetention createAlternateRetention(String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DispositionPhase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */