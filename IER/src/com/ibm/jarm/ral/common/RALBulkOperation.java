package com.ibm.jarm.ral.common;

import com.ibm.jarm.api.core.BulkItemResult;
import com.ibm.jarm.api.core.BulkOperation.EntityDescription;
import com.ibm.jarm.api.core.FilePlanRepository;
import java.util.List;

public abstract interface RALBulkOperation
{
  public abstract List<BulkItemResult> activateContainers(FilePlanRepository paramFilePlanRepository, List<String> paramList);
  
  public abstract List<BulkItemResult> inactivateContainers(FilePlanRepository paramFilePlanRepository, List<String> paramList, String paramString);
  
  public abstract List<BulkItemResult> closeContainers(FilePlanRepository paramFilePlanRepository, List<String> paramList, String paramString);
  
  public abstract List<BulkItemResult> reopenContainers(FilePlanRepository paramFilePlanRepository, List<String> paramList);
  
  public abstract List<BulkItemResult> placeHolds(FilePlanRepository paramFilePlanRepository, List<BulkOperation.EntityDescription> paramList, List<String> paramList1);
  
  public abstract List<BulkItemResult> removeHolds(FilePlanRepository paramFilePlanRepository, BulkOperation.EntityDescription paramEntityDescription, List<String> paramList);
  
  public abstract List<BulkItemResult> delete(FilePlanRepository paramFilePlanRepository, List<BulkOperation.EntityDescription> paramList);
  
  public abstract List<BulkItemResult> fileRecords(FilePlanRepository paramFilePlanRepository, List<String> paramList, String paramString);
  
  public abstract List<BulkItemResult> moveRecords(FilePlanRepository paramFilePlanRepository, List<String> paramList, String paramString1, String paramString2, String paramString3);
  
  public abstract List<BulkItemResult> copyRecords(FilePlanRepository paramFilePlanRepository, List<String> paramList, String paramString1, String paramString2);
  
  public abstract List<BulkItemResult> undeclareRecords(FilePlanRepository paramFilePlanRepository, List<String> paramList);
  
  public abstract List<BulkItemResult> initiateDisposition(FilePlanRepository paramFilePlanRepository, List<BulkOperation.EntityDescription> paramList, Object paramObject);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALBulkOperation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */