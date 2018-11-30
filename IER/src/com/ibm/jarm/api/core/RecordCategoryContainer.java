package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.property.RMPropertyFilter;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface RecordCategoryContainer
{
  public abstract PageableSet<RecordCategory> fetchRecordCategories(RMPropertyFilter paramRMPropertyFilter, Integer paramInteger);
  
  public abstract RecordCategory addRecordCategory(String paramString, RMProperties paramRMProperties, List<RMPermission> paramList);
  
  public abstract RecordCategory addRecordCategory(String paramString1, RMProperties paramRMProperties, List<RMPermission> paramList, String paramString2);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RecordCategoryContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */