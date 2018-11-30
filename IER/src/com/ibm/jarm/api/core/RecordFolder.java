package com.ibm.jarm.api.core;

public abstract interface RecordFolder
  extends Container, RecordFolderContainer, RecordVolumeContainer, RecordContainer, DispositionAllocatable
{
  public abstract String getFolderUniqueIdentifier();
  
  public abstract String getRecordFolderName();
  
  public abstract void move(RecordFolderContainer paramRecordFolderContainer, String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RecordFolder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */