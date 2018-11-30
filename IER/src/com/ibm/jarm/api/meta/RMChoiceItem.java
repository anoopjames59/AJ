package com.ibm.jarm.api.meta;

import com.ibm.jarm.api.constants.ChoiceItemType;
import java.util.List;
import java.util.Map;

public abstract interface RMChoiceItem
{
  public abstract Integer getChoiceIntegerValue();
  
  public abstract String getChoiceStringValue();
  
  public abstract ChoiceItemType getChoiceItemType();
  
  public abstract List<RMChoiceItem> getGroupChoiceItems();
  
  public abstract String getDisplayName();
  
  public abstract Map<String, String> getDisplayNames();
  
  public abstract String getId();
  
  public abstract String getName();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\meta\RMChoiceItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */