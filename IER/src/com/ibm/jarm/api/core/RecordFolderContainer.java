package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.property.RMPropertyFilter;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface RecordFolderContainer
{
  public abstract PageableSet<RecordFolder> fetchRecordFolders(RMPropertyFilter paramRMPropertyFilter, Integer paramInteger);
  
  public abstract RecordFolder addRecordFolder(String paramString, RMProperties paramRMProperties, List<RMPermission> paramList);
  
  public abstract RecordFolder addRecordFolder(String paramString1, RMProperties paramRMProperties, List<RMPermission> paramList, String paramString2);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RecordFolderContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */