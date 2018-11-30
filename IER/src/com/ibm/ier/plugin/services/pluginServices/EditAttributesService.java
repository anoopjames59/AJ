/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.ier.plugin.util.ValidationUtil;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Persistable;
/*     */ import com.ibm.jarm.api.core.RMFactory.BaseEntity;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditAttributesService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  40 */     return "ierEditAttributesService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  44 */     IERSearchResultsMediator mediator = new IERSearchResultsMediator();
/*  45 */     mediator.setIsDocId(true);
/*  46 */     return mediator;
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  52 */     FilePlanRepository fp_repository = getBaseService().getFilePlanRepository();
/*     */     
/*  54 */     String docId = request.getParameter("docid");
/*  55 */     String id = IERUtil.getIdFromDocIdString(docId);
/*  56 */     String className = request.getParameter("template_name");
/*     */     
/*  58 */     BaseEntity entity = RMFactory.BaseEntity.fetchInstance(fp_repository, className, id, RMPropertyFilter.MinimumPropertySet);
/*     */     
/*  60 */     if ((entity instanceof Persistable)) {
/*  61 */       IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*  62 */       mediator.setServerName(request.getParameter("repositoryId"));
/*  63 */       mediator.setPageSize(0);
/*     */       
/*  65 */       List<String> properties = new ArrayList();
/*  66 */       properties.addAll(MinimumPropertiesUtil.getPropertySetList(entity.getEntityType()));
/*     */       
/*  68 */       String json = readJSON(request);
/*  69 */       JSONArray jsonArray = JSONArray.parse(json);
/*  70 */       JSONObject jsonObj = (JSONObject)jsonArray.get(0);
/*  71 */       JSONArray criterias = (JSONArray)jsonObj.get("criterias");
/*     */       
/*     */ 
/*  74 */       Iterator<Object> iter = criterias.iterator();
/*  75 */       while (iter.hasNext()) {
/*  76 */         JSONObject criteria = (JSONObject)iter.next();
/*  77 */         Object criteriaNameObj = criteria.get("name");
/*  78 */         String criteriaName = criteriaNameObj != null ? criteriaNameObj.toString() : "";
/*  79 */         Object criteriaIsShownObj = criteria.get("isShown");
/*  80 */         if (criteriaName.equals("HoldName")) {
/*  81 */           String holdName = criteria.get("value").toString();
/*  82 */           if (!ValidationUtil.validateHoldName(holdName))
/*  83 */             throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_HOLD_INVALID_NAME, new Object[0]);
/*     */         }
/*  85 */         if (criteriaName.equals("LocationName")) {
/*  86 */           String locationName = criteria.get("value").toString();
/*  87 */           if (!ValidationUtil.validateLocationName(locationName))
/*  88 */             throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_LOCATION_INVALID_NAME, new Object[0]);
/*     */         }
/*  90 */         if (criteriaName.equals("PatternName")) {
/*  91 */           String patternName = criteria.get("value").toString();
/*  92 */           if (!ValidationUtil.validatePatternName(patternName))
/*  93 */             throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_NAMING_PATTERN_INVALID_NAME, new Object[0]);
/*     */         }
/*  95 */         if (!properties.contains(criteriaName)) {
/*  96 */           if ((criteriaIsShownObj != null) && (criteriaIsShownObj.toString().equals("false"))) {
/*  97 */             iter.remove();
/*     */           } else {
/*  99 */             properties.add(criteriaName);
/*     */           }
/*     */         }
/*     */       }
/* 103 */       mediator.setRequestedProperties(properties);
/*     */       
/* 105 */       MediatorUtil.setProperties(SessionUtil.getClassDescription(fp_repository, className, request), criterias, fp_repository, entity.getProperties());
/* 106 */       ((Persistable)entity).save(RMRefreshMode.Refresh);
/*     */       
/* 108 */       List<RMClassDescription> classDescriptions = new ArrayList();
/* 109 */       classDescriptions.add(entity.getClassDescription());
/* 110 */       mediator.setClassDescriptions(classDescriptions);
/*     */       
/* 112 */       List<BaseEntity> hits = new ArrayList();
/* 113 */       hits.add(entity);
/* 114 */       IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(hits, entity instanceof Container);
/* 115 */       mediator.setSearchResultsBean(searchResultsBean);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\EditAttributesService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */