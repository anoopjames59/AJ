/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.admin.PropertyDefinition;
/*     */ import com.filenet.api.collection.PropertyDefinitionList;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.Factory.ClassDefinition;
/*     */ import com.filenet.api.core.Factory.Document;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.SearchTemplateListMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMAccessRight;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GetSearchTemplatesService
/*     */   extends IERBasePluginService
/*     */ {
/*  37 */   private static final List<String> TEMPLATE_PROPERTIES = Arrays.asList(new String[] { "Id", "Name", "Description", "VersionSeries", "MimeType", "DocumentTitle", "Creator", "DateLastModified", "LastModifier", "IsCurrentVersion", "MajorVersionNumber", "MinorVersionNumber", "ContentSize", "DateCreated", "Permissions" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private static final List<String> OOTB_TEMPLATE_IDS = Arrays.asList(new String[] { "{4BB7944D-ED03-40B0-AEDF-4AEE237DB178}", "{7ECE39F4-D377-4977-95E6-6900CEA6E14C}" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private static final List<String> TEMPLATE_FILTER = Arrays.asList(new String[] { "RM", "IER" });
/*     */   
/*     */   public String getId()
/*     */   {
/*  63 */     return "ierRetrieveIERSearchTemplates";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator()
/*     */   {
/*  68 */     return new SearchTemplateListMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*     */   {
/*  73 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  75 */     String mimeType = "application/x-filenet-searchtemplate";
/*  76 */     List<BaseEntity> ierSearchTemplates = getIERSearchTemplates(mimeType);
/*  77 */     SearchTemplateListMediator mediator = (SearchTemplateListMediator)getMediator();
/*  78 */     mediator.setSearchTemplates(ierSearchTemplates);
/*     */     
/*  80 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   public List<BaseEntity> getIERSearchTemplates(String mimeType) {
/*  84 */     String methodName = "getIERSearchTemplates";
/*  85 */     Logger.logEntry(this, methodName, this.servletRequest);
/*     */     
/*  87 */     List<BaseEntity> ierSearchTemplates = new ArrayList();
/*     */     
/*  89 */     boolean isSearchAddonInstalled = isSearchAddonInstalled(getRepository());
/*  90 */     IERQuery query = new IERQuery();
/*  91 */     query.setRepository(getRepository());
/*  92 */     query.setFromClause("StoredSearch");
/*     */     
/*  94 */     List<String> propertiesList = new ArrayList(TEMPLATE_PROPERTIES);
/*  95 */     if (isSearchAddonInstalled) {
/*  96 */       propertiesList.add("IcnShowInTree");
/*  97 */       propertiesList.add("IcnAutoRun");
/*     */     }
/*  99 */     query.setRequestedProperties(propertiesList);
/*     */     
/* 101 */     StringBuilder sqlWhere = new StringBuilder();
/*     */     
/* 103 */     if ((mimeType != null) && (mimeType.length() > 0)) {
/* 104 */       sqlWhere.append("MimeType = '").append(mimeType).append("' AND ");
/*     */     }
/* 106 */     sqlWhere.append("VersionStatus").append(" = ").append(1).append(" AND ");
/*     */     
/* 108 */     sqlWhere.append("(").append(getFilteredApplicationNameWhereClause()).append(")");
/*     */     
/* 110 */     String OOTBFilter = getFilteredOOTBTemplateWhereClause();
/* 111 */     if (OOTBFilter != null) {
/* 112 */       sqlWhere.append(" AND (").append(OOTBFilter).append(")");
/*     */     }
/* 114 */     query.setWhereClause(sqlWhere.toString());
/* 115 */     query.setOrderByClause("DocumentTitle");
/*     */     
/* 117 */     if (Logger.isDebugLogged()) {
/* 118 */       Logger.logDebug(this, methodName, this.servletRequest, "Retrieve IER search templates sql query: " + query.toString());
/*     */     }
/* 120 */     Iterator<BaseEntity> ierSearchTemplate_it = query.executeQueryAsObjectsIterator(EntityType.ContentItem);
/* 121 */     while (ierSearchTemplate_it.hasNext()) {
/* 122 */       BaseEntity template = (BaseEntity)ierSearchTemplate_it.next();
/*     */       
/*     */ 
/*     */ 
/* 126 */       Integer access = template.getAccessAllowed();
/* 127 */       if ((access == null) || ((RMAccessRight.ViewContent.getIntValue() & access.intValue()) != 0))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */         ierSearchTemplates.add(template);
/*     */       }
/*     */     }
/* 136 */     Logger.logExit(this, methodName, this.servletRequest);
/* 137 */     return ierSearchTemplates;
/*     */   }
/*     */   
/*     */   private String getFilteredApplicationNameWhereClause()
/*     */   {
/* 142 */     StringBuffer buf = new StringBuffer();
/* 143 */     int idx = 0;
/* 144 */     for (String filter : TEMPLATE_FILTER) {
/* 145 */       if (idx++ > 0) buf.append(" OR ");
/* 146 */       buf.append("(ApplicationName= '").append(filter).append("')");
/*     */     }
/* 148 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String getFilteredOOTBTemplateWhereClause()
/*     */   {
/* 153 */     List<String> templateIds = getFilteredTemplateIds(getRepository(), OOTB_TEMPLATE_IDS);
/* 154 */     if ((templateIds == null) || (templateIds.size() == 0)) { return null;
/*     */     }
/* 156 */     StringBuffer buf = new StringBuffer();
/* 157 */     int idx = 0;
/* 158 */     for (String id : templateIds) {
/* 159 */       if (idx++ > 0) buf.append(" AND ");
/* 160 */       buf.append("(Id <> '").append(id).append("')");
/*     */     }
/* 162 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private List<String> getFilteredTemplateIds(Repository rep, List<String> templateIds)
/*     */   {
/* 167 */     List<String> idList = new ArrayList();
/* 168 */     for (String id : templateIds) {
/* 169 */       Id curVersionId = getCurrentVersionId(rep, id);
/* 170 */       if (curVersionId != null) idList.add(curVersionId.toString());
/*     */     }
/* 172 */     return idList;
/*     */   }
/*     */   
/*     */   private Id getCurrentVersionId(Repository rep, String propId)
/*     */   {
/* 177 */     ObjectStore objStore = P8CE_Convert.fromJARM(rep);
/* 178 */     Id currentVersionDocId = null;
/*     */     try {
/* 180 */       Document propDoc = Factory.Document.fetchInstance(objStore, propId, new PropertyFilter());
/* 181 */       if (propDoc != null) {
/* 182 */         Document propDocCurrent = (Document)propDoc.get_CurrentVersion();
/* 183 */         if (propDocCurrent != null) {
/* 184 */           currentVersionDocId = propDocCurrent.get_Id();
/*     */         }
/*     */       }
/*     */     } catch (EngineRuntimeException ignored) {}
/* 188 */     return currentVersionDocId;
/*     */   }
/*     */   
/*     */   private boolean isSearchAddonInstalled(Repository rep)
/*     */   {
/*     */     try
/*     */     {
/* 195 */       ObjectStore objStore = P8CE_Convert.fromJARM(rep);
/*     */       
/* 197 */       PropertyFilter filter = new PropertyFilter();
/* 198 */       filter.addIncludeProperty(0, null, null, "PropertyDefinitions", null);
/*     */       
/* 200 */       ClassDefinition classDefinition = Factory.ClassDefinition.fetchInstance(objStore, "StoredSearch", filter);
/* 201 */       PropertyDefinitionList properties = classDefinition.get_PropertyDefinitions();
/* 202 */       Iterator<PropertyDefinition> i = properties.iterator();
/* 203 */       while (i.hasNext()) {
/* 204 */         PropertyDefinition prop = (PropertyDefinition)i.next();
/* 205 */         if (prop.get_SymbolicName().equals("IcnAutoRun")) {
/* 206 */           return true;
/*     */         }
/*     */       }
/*     */     } catch (EngineRuntimeException ignore) {
/* 210 */       return false;
/*     */     }
/* 212 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetSearchTemplatesService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */