/*     */ package com.ibm.ier.plugin.services.responseFilters;
/*     */ 
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.services.IERBaseResponseFilterService;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.RepositoryType;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ public class P8OpenContentClassOverride
/*     */   extends IERBaseResponseFilterService
/*     */ {
/*     */   private static final String JSON_IS_RECORD_CLASS = "isRecordClass";
/*     */   
/*     */   public String[] getFilteredServices()
/*     */   {
/*  40 */     return new String[] { "/p8/openContentClass" };
/*     */   }
/*     */   
/*     */ 
/*     */   public void filterServiceExecute(HttpServletRequest request, JSONObject jsonResponse)
/*     */     throws Exception
/*     */   {
/*  47 */     String className = request.getParameter("template_name");
/*     */     
/*     */     try
/*     */     {
/*  51 */       Repository repository = getRepository();
/*  52 */       if ((repository != null) && ((repository.getRepositoryType() == RepositoryType.Combined) || (repository.getRepositoryType() == RepositoryType.Content) || (repository.getRepositoryType() == RepositoryType.FilePlan)))
/*     */       {
/*  54 */         List<RMPropertyDescription> pds = SessionUtil.getPropertyDescriptionList(repository, className, request);
/*     */         
/*  56 */         if (isRecordClass(pds))
/*     */         {
/*  58 */           jsonResponse.put("isRecordClass", Boolean.valueOf(true));
/*     */         }
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
/*  71 */         String requestInConfig = request.getParameter("ier_config");
/*  72 */         String filterType = request.getParameter("filter_type");
/*  73 */         if ((requestInConfig == null) && ((filterType == null) || (!filterType.equals("searches")))) {
/*  74 */           RepositoryConfig config = Config.getRepositoryConfig(request.getParameter("repositoryId"));
/*  75 */           if (config != null) {
/*  76 */             String[] props = config.getDisplayProeprties(className);
/*  77 */             if ((props == null) || (props.length == 0))
/*     */             {
/*  79 */               RMClassDescription cd = SessionUtil.getClassDescription(repository, className, request);
/*  80 */               cd = cd.getSuperclassDescription();
/*  81 */               while (cd != null) {
/*  82 */                 props = config.getDisplayProeprties(cd.getSymbolicName());
/*  83 */                 if ((props != null) && (props.length > 0)) {
/*     */                   break;
/*     */                 }
/*  86 */                 cd = cd.getSuperclassDescription();
/*     */               }
/*     */             }
/*  89 */             if ((props != null) && (props.length > 0))
/*     */             {
/*  91 */               List<String> list = Arrays.asList(props);
/*  92 */               JSONArray criterias = (JSONArray)jsonResponse.get("criterias");
/*  93 */               for (Object p : criterias) {
/*  94 */                 JSONObject prop = (JSONObject)p;
/*  95 */                 if ((!Boolean.TRUE.equals(prop.get("system"))) && (!Boolean.TRUE.equals(prop.get("hidden"))))
/*     */                 {
/*  97 */                   int index = list.indexOf(prop.get("name"));
/*  98 */                   if (index < 0) {
/*  99 */                     prop.put("hidden", Boolean.TRUE);
/*     */                   } else {
/* 101 */                     prop.put("ier_order", Integer.valueOf(index + 1));
/*     */                   }
/*     */                 }
/*     */               }
/* 105 */               Collections.sort(criterias, new Comparator() {
/*     */                 public int compare(Object o1, Object o2) {
/* 107 */                   Integer i1 = (Integer)((JSONObject)o1).get("ier_order");
/* 108 */                   Integer i2 = (Integer)((JSONObject)o2).get("ier_order");
/* 109 */                   return (i1 == null ? 9999 : i1.intValue()) - (i2 == null ? 9999 : i2.intValue());
/*     */                 }
/*     */               });
/*     */             }
/*     */           }
/* 114 */         } else if ("RMFolder".equals(className))
/*     */         {
/* 116 */           HashMap<String, JSONObject> map = new HashMap();
/* 117 */           JSONObject crit = new JSONObject();
/* 118 */           crit.put("name", "{NAME}");
/* 119 */           crit.put("label", MessageResources.getLocalizedMessage(MessageCode.NAME, new Object[0]));
/* 120 */           map.put("{NAME}", crit);
/* 121 */           crit = new JSONObject();
/* 122 */           crit.put("name", "ContainerId");
/* 123 */           crit.put("label", MessageResources.getLocalizedMessage(MessageCode.CONTAINER_ID, new Object[0]));
/* 124 */           map.put("ContainerId", crit);
/* 125 */           RMClassDescription cd = SessionUtil.getClassDescription(repository, className, request);
/* 126 */           for (RMPropertyDescription pd : cd.getPropertyDescriptions()) {
/* 127 */             if (!pd.isHidden()) {
/* 128 */               crit = new JSONObject();
/* 129 */               String key = pd.getSymbolicName();
/* 130 */               crit.put("name", key);
/* 131 */               crit.put("label", pd.getDisplayName());
/* 132 */               crit.put("system", Boolean.valueOf((pd.isSystemGenerated()) || (pd.isSystemOwned())));
/* 133 */               map.put(key, crit);
/*     */             }
/*     */           }
/* 136 */           for (RMPropertyDescription pd : cd.getAllDescendentPropertyDescriptions()) {
/* 137 */             if (!pd.isHidden()) {
/* 138 */               crit = new JSONObject();
/* 139 */               String key = pd.getSymbolicName();
/* 140 */               crit.put("name", key);
/* 141 */               crit.put("label", pd.getDisplayName());
/* 142 */               crit.put("system", Boolean.valueOf((pd.isSystemGenerated()) || (pd.isSystemOwned())));
/* 143 */               map.put(key, crit);
/*     */             }
/*     */           }
/* 146 */           JSONArray criterias = new JSONArray();
/* 147 */           for (JSONObject c : map.values()) {
/* 148 */             criterias.add(c);
/*     */           }
/* 150 */           jsonResponse.put("criterias", criterias);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception exp) {
/* 155 */       Logger.logError(this, "filterserviceExecute", request, exp);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isRecordClass(List<RMPropertyDescription> pds) {
/* 160 */     for (RMPropertyDescription pd : pds) {
/* 161 */       if (pd.getSymbolicName().equals("RecordedDocuments"))
/* 162 */         return true;
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\responseFilters\P8OpenContentClassOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */