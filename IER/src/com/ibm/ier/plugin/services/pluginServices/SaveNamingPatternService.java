/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.MediatorUtil;
/*     */ import com.ibm.ier.plugin.nls.IERUIRuntimeException;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.MinimumPropertiesUtil;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.ier.plugin.util.ValidationUtil;
/*     */ import com.ibm.jarm.api.constants.AppliedForCategoryOrFolder;
/*     */ import com.ibm.jarm.api.constants.ApplyToNameOrID;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.NamingPattern;
/*     */ import com.ibm.jarm.api.core.NamingPatternLevel;
/*     */ import com.ibm.jarm.api.core.RMFactory.NamingPattern;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SaveNamingPatternService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  35 */     return "ierSaveNamingPatternService";
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  39 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     try
/*     */     {
/*  42 */       JSONObject requestContent = getRequestContent();
/*  43 */       JSONArray criterias = (JSONArray)requestContent.get("criterias");
/*  44 */       JSONArray levelArray = (JSONArray)requestContent.get("ier_patternLevels");
/*  45 */       FilePlanRepository repository = getFilePlanRepository();
/*     */       
/*  47 */       RMProperties props = MediatorUtil.getProperties(SessionUtil.getClassDescription(repository, "Pattern", request), criterias, repository);
/*     */       
/*  49 */       String patternName = props.getStringValue("PatternName");
/*  50 */       if (!ValidationUtil.validatePatternName(patternName)) {
/*  51 */         throw IERUIRuntimeException.createUIRuntimeException(MessageCode.E_NAMING_PATTERN_INVALID_NAME, new Object[0]);
/*     */       }
/*     */       
/*  54 */       NamingPattern pattern = null;
/*  55 */       String patternId = (String)requestContent.get("ier_patternId");
/*  56 */       if ((patternId != null) && (patternId.length() > 0)) {
/*  57 */         patternId = IERUtil.getIdFromDocIdString(patternId);
/*  58 */         pattern = RMFactory.NamingPattern.fetchInstance(repository, patternId, null);
/*     */       } else {
/*  60 */         pattern = RMFactory.NamingPattern.createInstance(repository);
/*     */       }
/*  62 */       pattern.setPatternName(patternName);
/*     */       
/*  64 */       if (props.isPropertyPresent("RMEntityDescription")) {
/*  65 */         pattern.setDescription(props.getStringValue("RMEntityDescription"));
/*     */       }
/*  67 */       if (props.isPropertyPresent("ApplyToNameOrId")) {
/*  68 */         String nameOrId = props.getStringValue("ApplyToNameOrId");
/*  69 */         pattern.setApplyToNameOrId(ApplyToNameOrID.valueOf(nameOrId));
/*     */       }
/*  71 */       pattern.save(RMRefreshMode.Refresh);
/*     */       
/*  73 */       List<NamingPatternLevel> levels = pattern.getNamingPatternLevels();
/*  74 */       int index = 0;
/*  75 */       if (levelArray != null)
/*     */       {
/*  77 */         for (int i = 0; i < levelArray.size(); i++) {
/*  78 */           JSONObject levelObject = (JSONObject)levelArray.get(i);
/*  79 */           NamingPatternLevel level = null;
/*  80 */           if ((levels != null) && (index < levels.size())) {
/*  81 */             level = (NamingPatternLevel)levels.get(index);
/*     */           } else {
/*  83 */             level = pattern.createNamingPatternLevel();
/*     */           }
/*  85 */           Object levelNumber = levelObject.get("level");
/*  86 */           if (levelNumber != null)
/*     */           {
/*     */ 
/*  89 */             level.setPatternLevelNumber(Integer.valueOf(levelNumber.toString()));
/*  90 */             String entityType = (String)levelObject.get("entityType");
/*  91 */             if (entityType != null)
/*     */             {
/*     */ 
/*  94 */               level.setAppliedFor(AppliedForCategoryOrFolder.valueOf(entityType));
/*  95 */               String patternString = (String)levelObject.get("pattern");
/*  96 */               if (patternString != null)
/*     */               {
/*     */ 
/*  99 */                 level.setPatternString(patternString);
/* 100 */                 Object increment = levelObject.get("increment");
/* 101 */                 if (increment != null) {
/* 102 */                   level.setIncrementedBy(Integer.valueOf(increment.toString()));
/*     */                 } else {
/* 104 */                   level.setIncrementedBy(null);
/*     */                 }
/* 106 */                 level.save(RMRefreshMode.NoRefresh);
/* 107 */                 index++;
/*     */               }
/*     */             } } } }
/* 110 */       if (levels != null) {
/* 112 */         for (; 
/* 112 */             index < levels.size(); index++) {
/* 113 */           NamingPatternLevel level = (NamingPatternLevel)levels.get(index);
/* 114 */           level.delete();
/*     */         }
/*     */       }
/*     */       
/* 118 */       setCompletedJSONResponseObject(MediatorUtil.createEntityItemJSONObject(pattern, MinimumPropertiesUtil.getPropertySetList(EntityType.Pattern), this.servletRequest));
/*     */     }
/*     */     catch (Exception e) {
/* 121 */       throw IERUIRuntimeException.createUIRuntimeException(e, MessageCode.E_NAMING_PATTERN_UNEXPECTED, new Object[] { e.getLocalizedMessage() });
/*     */     }
/*     */     
/* 124 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\SaveNamingPatternService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */