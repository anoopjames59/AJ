package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.ContentXMLExport;
import java.util.List;

public abstract interface ContentItem
  extends BaseEntity
{
  public abstract Record getAssociatedRecord();
  
  public abstract String getMimeType();
  
  public abstract List<RMContentElement> getContentElements();
  
  public abstract boolean isEligibleForDeclaration();
  
  public abstract void exportAsXML(String paramString, ContentXMLExport paramContentXMLExport);
  
  public abstract void delete();
  
  public abstract void deleteAllVersions();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\ContentItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */