package com.ibm.jarm.api.core;

import com.ibm.jarm.api.collection.PageableSet;
import com.ibm.jarm.api.constants.DomainType;
import com.ibm.jarm.api.constants.RMPrincipalSearchAttribute;
import com.ibm.jarm.api.constants.RMPrincipalSearchSortType;
import com.ibm.jarm.api.constants.RMPrincipalSearchType;
import com.ibm.jarm.api.meta.RMMarkingSet;
import com.ibm.jarm.api.property.RMPropertyFilter;
import com.ibm.jarm.api.security.RMGroup;
import com.ibm.jarm.api.security.RMRoles;
import com.ibm.jarm.api.security.RMUser;
import java.util.List;

public abstract interface RMDomain
  extends BaseEntity
{
  public abstract String getName();
  
  public abstract DomainConnection getDomainConnection();
  
  public abstract DomainType getDomainType();
  
  public abstract List<FilePlanRepository> fetchFilePlanRepositories(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<ContentRepository> fetchContentRepositories(boolean paramBoolean, RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<Repository> fetchCombinedRepositories(RMPropertyFilter paramRMPropertyFilter);
  
  public abstract List<RMMarkingSet> fetchMarkingSets();
  
  public abstract RMRoles fetchRMRoles(Repository paramRepository, String paramString);
  
  public abstract RMUser fetchCurrentUser();
  
  public abstract PageableSet<RMGroup> findGroups(String paramString, RMPrincipalSearchType paramRMPrincipalSearchType, RMPrincipalSearchAttribute paramRMPrincipalSearchAttribute, RMPrincipalSearchSortType paramRMPrincipalSearchSortType, Integer paramInteger);
  
  public abstract PageableSet<RMUser> findUsers(String paramString, RMPrincipalSearchType paramRMPrincipalSearchType, RMPrincipalSearchAttribute paramRMPrincipalSearchAttribute, RMPrincipalSearchSortType paramRMPrincipalSearchSortType, Integer paramInteger);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\core\RMDomain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */