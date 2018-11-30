package com.ibm.jarm.api.core;

public abstract interface SystemConfiguration
  extends RMCustomObject
{
  public static final String EnableRecordMultiFile = "Allow Record Multi-Filing";
  public static final String ExportConfiguration = "Export Configuration";
  public static final String ExportTransferMapping = "Export Transfer Mapping";
  public static final String FPOS_Setup = "FPOS Setup";
  public static final String MaxDeclassificationOffset = "Max Declassification Offset";
  public static final String MaxWorkflowBatchSize = "Maximum Batch Size For Workflows";
  public static final String RMVersionInfo = "RM Version Info";
  public static final String RequestDefaultDeclassificationDateUpdate = "Request Default Declassification Date Update";
  public static final String ScreeningWorkflow = "Screening Workflow";
  public static final String SecurityScriptRunDate = "Security Script Run Date";
  public static final String VolumePatternSuffix = "Volume Pattern Suffix";
  
  public abstract String getPropertyName();
  
  public abstract String getPropertyDataType();
  
  public abstract String getPropertyValue();
  
  public abstract String getDefaultSettings();
  
  public abstract boolean canBeUpdated();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\SystemConfiguration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */