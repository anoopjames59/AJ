package com.ibm.jarm.api.core;

public abstract interface RecordCategory
  extends Container, RecordCategoryContainer, RecordFolderContainer, RecordContainer, DispositionAllocatable
{
  public abstract String getRecordCategoryIdentifier();
  
  public abstract String getRecordCategoryName();
  
  public abstract void move(RecordCategoryContainer paramRecordCategoryContainer, String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RecordCategory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */