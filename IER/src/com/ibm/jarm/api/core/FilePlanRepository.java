package com.ibm.jarm.api.core;

import com.ibm.jarm.api.constants.DataModelType;
import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.property.RMPropertyFilter;
import com.ibm.jarm.api.security.RMPermission;
import java.util.List;
import java.util.Map;

public abstract interface FilePlanRepository
  extends Repository
{
  public abstract FilePlan addFilePlan(String paramString, RMProperties paramRMProperties, List<RMPermission> paramList);
  
  public abstract FilePlan addFilePlan(String paramString1, RMProperties paramRMProperties, List<RMPermission> paramList, String paramString2);
  
  public abstract List<FilePlan> getFilePlans(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract Map<String, SystemConfiguration> getSystemConfigurations();
  
  public abstract SystemConfiguration putSystemConfiguration(String paramString1, String paramString2);
  
  public abstract DataModelType getDataModelType();
  
  public abstract List<Location> getLocations(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<RecordType> getRecordTypes(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<ClassificationGuide> getClassificationGuides(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract Integer getDoDMaxDeclassifyTimeLimit();
  
  public abstract List<DispositionAction> getDispositionActions(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<DispositionTrigger> getDispositionTriggers(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<DispositionSchedule> getDispositionSchedules(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<ReportDefinition> getReportDefinitions(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<RMWorkflowDefinition> getWorkflowDefinitions(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<Hold> getHolds(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<ContentRepository> getAssociatedContentRepositories();
  
  public abstract boolean supportsExternalManagement();
  
  public abstract boolean supportsDefensibleDisposal();
  
  public abstract boolean isRecordMultiFilingEnabled();
  
  public abstract void launchWorkflows(List<BaseEntity> paramList, String paramString, int paramInt, Object paramObject);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\FilePlanRepository.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */