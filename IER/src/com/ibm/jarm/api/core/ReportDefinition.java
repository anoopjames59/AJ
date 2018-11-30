package com.ibm.jarm.api.core;

import com.ibm.jarm.api.query.ReportParameter;
import java.util.List;

public abstract interface ReportDefinition
  extends ContentItem, Persistable
{
  public static final String COMMON_ENTITY_TYPE = "Common";
  
  public abstract String getReportTitle();
  
  public abstract void setReportTitle(String paramString);
  
  public abstract String getReportTitleLocalizationKey();
  
  public abstract void setReportTitleLocalizationKey(String paramString);
  
  public abstract String getReportDescription();
  
  public abstract void setReportDescription(String paramString);
  
  public abstract String getReportDescriptionLocalizationKey();
  
  public abstract void setReportDescriptionLocalizationKey(String paramString);
  
  public abstract List<ReportParameter> getReportParameters();
  
  public abstract void setReportParameters(List<ReportParameter> paramList);
  
  public abstract String[] getReportQueriesByAssociatedEntityType(String paramString);
  
  public abstract void addReportQueries(String[] paramArrayOfString, String paramString);
  
  public abstract void removeAllReportQueries();
  
  public abstract List<String> getAssociatedEntityTypes();
  
  public abstract String getReportName();
  
  public abstract void setReportName(String paramString);
  
  public abstract String getReportNameLocalizationKey();
  
  public abstract void setReportNameLocalizationKey(String paramString);
  
  public abstract String getEntryHelp();
  
  public abstract void setEntryHelp(String paramString);
  
  public abstract String getEntryHelpLocalizationKey();
  
  public abstract void setEntryHelpLocalizationKey(String paramString);
  
  public abstract String getReportDBTableName();
  
  public abstract void setReportDBTableName(String paramString);
  
  public abstract void parseContent();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\ReportDefinition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */