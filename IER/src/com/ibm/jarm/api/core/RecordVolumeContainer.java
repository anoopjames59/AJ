package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.property.RMPropertyFilter;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface RecordVolumeContainer
{
  public abstract PageableSet<RecordVolume> fetchRecordVolumes(RMPropertyFilter paramRMPropertyFilter, Integer paramInteger);
  
  public abstract RecordVolume addRecordVolume(String paramString1, String paramString2, RMProperties paramRMProperties, List<RMPermission> paramList);
  
  public abstract RecordVolume addRecordVolume(String paramString1, String paramString2, RMProperties paramRMProperties, List<RMPermission> paramList, String paramString3);
  
  public abstract RecordVolume getActiveRecordVolume();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RecordVolumeContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */