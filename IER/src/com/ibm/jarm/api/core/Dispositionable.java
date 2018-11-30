package com.ibm.jarm.api.core;

import com.ibm.jarm.api.property.RMProperties;
import java.util.Date;
import java.util.List;

public abstract interface Dispositionable
{
  public abstract void resetDispositionData();
  
  public abstract void resetVitalData();
  
  public abstract boolean isReadyForInitiateDisposition();
  
  public abstract void completePhaseExecution(Date paramDate, boolean paramBoolean);
  
  public abstract void destroy();
  
  public abstract void destroy(boolean paramBoolean);
  
  public abstract void exportUsingP8_XML();
  
  public abstract void exportUsingExternalExport(ExternalExport paramExternalExport, boolean paramBoolean);
  
  public abstract void executeInterimTransferUsingP8_XML(Location paramLocation);
  
  public abstract void executeInterimTransferUsingExternalExport(Location paramLocation, ExternalExport paramExternalExport, boolean paramBoolean);
  
  public abstract void transferUsingP8_XML();
  
  public abstract void transferUsingExternalExport(ExternalExport paramExternalExport, boolean paramBoolean);
  
  public abstract void review(RMProperties paramRMProperties);
  
  public abstract List<BulkItemResult> initiateDisposition(Object paramObject);
  
  public abstract void updatePhaseDataOnEntity();
  
  public abstract void validateForDispositionExport();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Dispositionable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */