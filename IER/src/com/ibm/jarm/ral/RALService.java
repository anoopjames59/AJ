package com.ibm.jarm.ral;

import com.ibm.jarm.api.constants.DispositionActionType;
import com.ibm.jarm.api.constants.DispositionTriggerType;
import com.ibm.jarm.api.constants.DomainType;
import com.ibm.jarm.api.constants.EntityType;
import com.ibm.jarm.api.core.BaseEntity;
import com.ibm.jarm.api.core.ClassificationGuide;
import com.ibm.jarm.api.core.Container;
import com.ibm.jarm.api.core.ContentItem;
import com.ibm.jarm.api.core.ContentRepository;
import com.ibm.jarm.api.core.DispositionAction;
import com.ibm.jarm.api.core.DispositionTrigger;
import com.ibm.jarm.api.core.DomainConnection;
import com.ibm.jarm.api.core.FilePlanRepository;
import com.ibm.jarm.api.core.Location;
import com.ibm.jarm.api.core.RMCustomObject;
import com.ibm.jarm.api.core.RMDomain;
import com.ibm.jarm.api.core.RMLink;
import com.ibm.jarm.api.core.Record;
import com.ibm.jarm.api.core.ReportDefinition;
import com.ibm.jarm.api.core.Repository;
import com.ibm.jarm.api.meta.RMChoiceList;
import com.ibm.jarm.api.meta.RMClassDescription;
import com.ibm.jarm.api.meta.RMMarkingSet;
import com.ibm.jarm.api.property.RMProperties;
import com.ibm.jarm.api.property.RMPropertyFilter;
import com.ibm.jarm.api.query.RMContentSearchDefinition;
import com.ibm.jarm.api.security.RMGroup;
import com.ibm.jarm.api.security.RMPermission;
import com.ibm.jarm.api.security.RMUser;
import com.ibm.jarm.ral.common.RALBulkOperation;
import java.util.Map;
import javax.security.auth.Subject;

public abstract interface RALService
{
  public abstract DomainType getDomainType();
  
  public abstract DomainConnection createDomainConnection(String paramString, Map<String, Object> paramMap);
  
  public abstract Subject createSubject(DomainConnection paramDomainConnection, String paramString1, String paramString2, String paramString3);
  
  public abstract RMDomain fetchDomain(DomainConnection paramDomainConnection, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract Repository fetchRepository(RMDomain paramRMDomain, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract Repository getRepository(RMDomain paramRMDomain, String paramString);
  
  public abstract FilePlanRepository fetchFilePlanRepository(RMDomain paramRMDomain, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract FilePlanRepository getFilePlanRepository(RMDomain paramRMDomain, String paramString);
  
  public abstract ContentRepository fetchContentRepository(RMDomain paramRMDomain, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract ContentRepository getContentRepository(RMDomain paramRMDomain, String paramString);
  
  public abstract Container fetchContainer(Repository paramRepository, EntityType paramEntityType, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract Container getContainer(Repository paramRepository, EntityType paramEntityType, String paramString);
  
  public abstract RMCustomObject fetchCustomObject(Repository paramRepository, EntityType paramEntityType, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract RMCustomObject getCustomObject(Repository paramRepository, EntityType paramEntityType, String paramString);
  
  public abstract RMCustomObject createCustomObject(Repository paramRepository, EntityType paramEntityType, String paramString);
  
  public abstract DispositionAction createDispositionAction(Repository paramRepository, DispositionActionType paramDispositionActionType, String paramString);
  
  public abstract DispositionTrigger createDispositionTrigger(Repository paramRepository, DispositionTriggerType paramDispositionTriggerType, String paramString);
  
  public abstract Location createLocation(Repository paramRepository, String paramString);
  
  public abstract ReportDefinition createReportDefinition(Repository paramRepository, String paramString);
  
  public abstract RMPermission createPermission();
  
  public abstract RMProperties createProperties();
  
  public abstract RMContentSearchDefinition createRMSearchDefinition();
  
  public abstract ContentItem fetchContentItem(Repository paramRepository, EntityType paramEntityType, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract ContentItem getContentItem(Repository paramRepository, EntityType paramEntityType, String paramString);
  
  public abstract RMMarkingSet fetchMarkingSet(RMDomain paramRMDomain, String paramString);
  
  public abstract RMChoiceList fetchChoiceList(Repository paramRepository, String paramString);
  
  public abstract RMClassDescription fetchClassDescription(Repository paramRepository, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract BaseEntity fetchBaseEntity(Repository paramRepository, EntityType paramEntityType, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract BaseEntity fetchBaseEntity(Repository paramRepository, String paramString1, String paramString2, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract BaseEntity getBaseEntity(Repository paramRepository, EntityType paramEntityType, String paramString);
  
  public abstract BaseEntity getBaseEntity(Repository paramRepository, String paramString1, String paramString2);
  
  public abstract Record fetchRecord(FilePlanRepository paramFilePlanRepository, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract Record getRecord(FilePlanRepository paramFilePlanRepository, EntityType paramEntityType, String paramString);
  
  public abstract RMLink fetchRMLink(FilePlanRepository paramFilePlanRepository, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract RMLink getRMLink(FilePlanRepository paramFilePlanRepository, String paramString1, String paramString2);
  
  public abstract RMLink createRMLink(FilePlanRepository paramFilePlanRepository, String paramString);
  
  public abstract ClassificationGuide fetchClassificationGuide(FilePlanRepository paramFilePlanRepository, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract RMUser fetchUser(DomainConnection paramDomainConnection, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract RMGroup fetchGroup(DomainConnection paramDomainConnection, String paramString, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract RALBulkOperation getRALBulkOperation();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\RALService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */