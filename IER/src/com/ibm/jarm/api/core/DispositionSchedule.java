package com.ibm.jarm.api.core;

import java.util.Date;
import java.util.List;

public abstract interface DispositionSchedule
  extends RMCustomObject
{
  public abstract String getScheduleName();
  
  public abstract void setScheduleName(String paramString);
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract Date getCalendarDate();
  
  public abstract void setCalendarDate(Date paramDate);
  
  public abstract String getCutoffBase();
  
  public abstract void setCutoffBase(String paramString);
  
  public abstract DispositionAction getCutoffAction();
  
  public abstract void setCutoffAction(DispositionAction paramDispositionAction);
  
  public abstract String getDispositionAuthority();
  
  public abstract void setDispositionAuthority(String paramString);
  
  public abstract Integer[] getDispositionEventOffset();
  
  public abstract void setDispositionEventOffset(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);
  
  public abstract DispositionTrigger getDispositionTrigger();
  
  public abstract void setDispositionTigger(DispositionTrigger paramDispositionTrigger);
  
  public abstract DispositionPhaseList getDispositionPhases();
  
  public abstract DispositionPhase createNewPhase(String paramString);
  
  public abstract DispositionPhase createNewPhase(String paramString1, String paramString2);
  
  public abstract List<RecordCategory> getAssociatedRecordCategories();
  
  public abstract List<RecordFolder> getAssociatedRecordFolders();
  
  public abstract List<RecordType> getAssociatedRecordTypes();
  
  public abstract Date getEffectiveDateModified();
  
  public abstract Boolean isScreeningRequired();
  
  public abstract void setScreeningRequired(Boolean paramBoolean);
  
  public abstract Boolean isTriggerChanged();
  
  public abstract String getReasonForChange();
  
  public abstract void setReasonForChange(String paramString);
  
  public abstract String getAuditXML();
  
  public abstract Integer getSweepState();
  
  public abstract void recalculateCutoffDate(Dispositionable paramDispositionable);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\DispositionSchedule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */