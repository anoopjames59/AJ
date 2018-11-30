package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.constants.ContentXMLExport;
import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;

public abstract interface RecordContainer
  extends Container, Dispositionable, Holdable, Vitalable, AuditableEntity, DefensiblyDisposable, DefensiblyDisposableContainerParent
{
  public abstract PageableSet<Record> getRecords(Integer paramInteger);
  
  public abstract void fileRecord(Record paramRecord);
  
  public abstract void fileRecord(String paramString);
  
  public abstract void unfileRecord(Record paramRecord);
  
  public abstract void unfileRecord(String paramString);
  
  public abstract boolean isPhysicalRecordContainer();
  
  public abstract Record declare(String paramString, RMProperties paramRMProperties, List<RMPermission> paramList, List<RecordContainer> paramList1);
  
  public abstract Record declare(String paramString, RMProperties paramRMProperties, List<RMPermission> paramList, List<RecordContainer> paramList1, List<ContentItem> paramList2);
  
  public abstract Record declare(String paramString, RMProperties paramRMProperties, List<RMPermission> paramList, List<RecordContainer> paramList1, ContentRepository paramContentRepository, List<String> paramList2);
  
  public abstract boolean isInactive();
  
  public abstract void setActive();
  
  public abstract void setInactive(String paramString);
  
  public abstract boolean isOpen();
  
  public abstract boolean isReopened();
  
  public abstract boolean isClosed();
  
  public abstract boolean isAnyParentClosed();
  
  public abstract void reopen();
  
  public abstract void close(String paramString);
  
  public abstract boolean requiresChildVolume();
  
  public abstract FilePlan getFilePlan();
  
  public abstract void exportUsingP8_XML(String paramString1, String paramString2, ContentXMLExport paramContentXMLExport);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RecordContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */