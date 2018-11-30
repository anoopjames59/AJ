package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.constants.ContentXMLExport;
import com.ibm.jarm.api.constants.DeleteMode;
import com.ibm.jarm.api.property.RMProperties;
import java.util.List;

public abstract interface Record
  extends BaseEntity, Persistable, Dispositionable, Holdable, Vitalable, AuditableEntity
{
  public abstract boolean isPhysicalRecord();
  
  public abstract void associateRecordType(RecordType paramRecordType);
  
  public abstract RecordType getAssociatedRecordType();
  
  public abstract PageableSet<ContentItem> getAssociatedContentItems();
  
  public abstract void assignRecordAsReceiptOfParent(Record paramRecord);
  
  public abstract Record unassignRecordAsReceiptOfParent();
  
  public abstract Record getReceiptParent();
  
  public abstract List<Record> getChildReceipts();
  
  public abstract void move(RecordContainer paramRecordContainer1, RecordContainer paramRecordContainer2, String paramString);
  
  public abstract Record copy(RecordContainer paramRecordContainer, RMProperties paramRMProperties);
  
  public abstract void supersede(Record paramRecord);
  
  public abstract boolean isSuperseded();
  
  public abstract void undeclare();
  
  public abstract FilePlan getFilePlan();
  
  public abstract Container getSecurityFolder();
  
  public abstract void exportUsingP8_XML(String paramString1, String paramString2, ContentXMLExport paramContentXMLExport);
  
  public abstract void delete(boolean paramBoolean, DeleteMode paramDeleteMode, String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\Record.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */