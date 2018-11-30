package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;

public abstract interface RecordType
  extends RMCustomObject, DispositionAllocatable
{
  public abstract String getRecordTypeName();
  
  public abstract void setRecordTypeName(String paramString);
  
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract PageableSet<Record> getAssociatedRecords();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RecordType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */