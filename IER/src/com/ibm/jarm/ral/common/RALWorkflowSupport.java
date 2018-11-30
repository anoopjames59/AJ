/*     */ package com.ibm.jarm.ral.common;
/*     */ 
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.constants.RMWorkflowStatus;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.DispositionAction;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Persistable;
/*     */ import com.ibm.jarm.api.core.RMFactory.DispositionAction;
/*     */ import com.ibm.jarm.api.core.RMWorkflowDefinition;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.core.SystemConfiguration;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.query.RMSearch;
/*     */ import com.ibm.jarm.api.query.ResultRow;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RALWorkflowSupport
/*     */ {
/*  56 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*     */   
/*     */   public static final int WORKFLOW_TYPE_DISPOSAL = 1001;
/*     */   
/*     */   public static final int WORKFLOW_TYPE_VITAL = 1002;
/*     */   
/*     */   public static final int WORKFLOW_TYPE_SCREENING = 1003;
/*     */   
/*     */   private static final String WORKFLOW_PARAM_GUID = "GUID";
/*     */   
/*     */   private static final String WORKFLOW_PARAM_ACTION = "F_Subject";
/*     */   
/*     */   private static final String WORKFLOW_PARAM_OBJECTSTOREID = "ObjectStoreID";
/*     */   
/*     */   private static final String KEY_SCREEN_REQUIRED = "SCREENING_REQUIRED";
/*     */   
/*     */   private static final String KEY_SCREEN_NOT_REQUIRED = "SCREENING_NOT_REQUIRED";
/*     */   private static final int DEFAULT_WORKFLOW_BATCH_SIZE = 10;
/*     */   private static final int MAX_SUBJECT_ENTITIES = 5;
/*     */   private static final int MAX_SUBJECT_LENGTH = 200;
/*  76 */   private static final Map<String, AtomicInteger> MaxWFLBatchSizes = Collections.synchronizedMap(new HashMap());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   private static final Map<String, Map<String, String>> VWVersionCache = Collections.synchronizedMap(new HashMap());
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
/*     */   public static void launchWorkflows(Repository repository, List<BaseEntity> entities, String separationPropertyName, int workflowType, Object vwSession)
/*     */   {
/* 113 */     Tracer.traceMethodEntry(new Object[] { repository, entities, separationPropertyName, Integer.valueOf(workflowType), vwSession });
/* 114 */     updateMaxBatchSizeCache(repository);
/*     */     
/* 116 */     if (workflowType == 1003)
/*     */     {
/* 118 */       launchScreeningWorkflows(repository, entities, separationPropertyName, vwSession);
/*     */     }
/* 120 */     else if ((workflowType == 1001) || (workflowType == 1002))
/*     */     {
/* 122 */       launchOtherTypeWorkflows(repository, entities, separationPropertyName, workflowType, vwSession);
/*     */     }
/*     */     
/* 125 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
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
/*     */ 
/*     */   private static void launchScreeningWorkflows(Repository repository, List<BaseEntity> entities, String separationPropertyName, Object vwSession)
/*     */   {
/* 146 */     Tracer.traceMethodEntry(new Object[] { repository, entities, separationPropertyName, vwSession });
/*     */     
/*     */ 
/* 149 */     Map<String, List<BaseEntity>> mapOfListsByScreeningReq = separateByScreeningRequired(entities);
/*     */     
/*     */ 
/* 152 */     List<BaseEntity> screeningReqEntities = (List)mapOfListsByScreeningReq.get("SCREENING_REQUIRED");
/* 153 */     String vwVersionData; if ((screeningReqEntities != null) && (!screeningReqEntities.isEmpty()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 158 */       vwVersionData = getVWVersionData(repository, "Screening Workflow");
/*     */       
/*     */ 
/*     */ 
/* 162 */       Map<String, List<BaseEntity>> mapOfListsByProperty = separateByPropertyValue(separationPropertyName, screeningReqEntities);
/*     */       
/* 164 */       for (List<BaseEntity> singleList : mapOfListsByProperty.values())
/*     */       {
/* 166 */         List<List<BaseEntity>> listOfBatches = splitIntoBatches(repository, singleList);
/* 167 */         for (List<BaseEntity> batch : listOfBatches)
/*     */         {
/*     */ 
/* 170 */           updatePropertyValueOnBatch("CurrentPhaseExecutionStatus", Integer.valueOf(RMWorkflowStatus.Started.getIntValue()), batch);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 175 */           startupWorkflow(repository, "Screening", batch, 1003, vwVersionData, vwSession);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 181 */     List<BaseEntity> nonScreeningReqEntities = (List)mapOfListsByScreeningReq.get("SCREENING_NOT_REQUIRED");
/* 182 */     if ((nonScreeningReqEntities != null) && (!nonScreeningReqEntities.isEmpty()))
/*     */     {
/* 184 */       launchOtherTypeWorkflows(repository, nonScreeningReqEntities, separationPropertyName, 1001, vwSession);
/*     */     }
/*     */     
/* 187 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void launchOtherTypeWorkflows(Repository repository, List<BaseEntity> entities, String separationPropertyName, int workflowType, Object vwSession)
/*     */   {
/* 210 */     Tracer.traceMethodEntry(new Object[] { repository, entities, separationPropertyName, Integer.valueOf(workflowType), vwSession });
/*     */     
/* 212 */     String exeStatusPropertyName = "CurrentPhaseExecutionStatus";
/* 213 */     if (workflowType == 1002) {
/* 214 */       exeStatusPropertyName = "VitalWorkflowStatus";
/*     */     }
/*     */     
/* 217 */     Map<String, List<BaseEntity>> mapOfListsByProperty = separateByPropertyValue(separationPropertyName, entities);
/* 218 */     for (List<BaseEntity> singleByPropList : mapOfListsByProperty.values())
/*     */     {
/*     */ 
/*     */ 
/* 222 */       Map<String, List<BaseEntity>> mapOfListsByAction = separateByAction(workflowType, singleByPropList);
/* 223 */       for (Map.Entry<String, List<BaseEntity>> entry : mapOfListsByAction.entrySet())
/*     */       {
/*     */ 
/*     */ 
/* 227 */         String actionIdent = (String)entry.getKey();
/* 228 */         String[] workflowActionInfo = getActionWorkflowIdent(repository, actionIdent);
/* 229 */         String workflowIdent = workflowActionInfo[0];
/* 230 */         actionName = workflowActionInfo[1] != null ? workflowActionInfo[1] : actionIdent;
/* 231 */         vwVersionData = getVWVersionData(repository, workflowIdent);
/*     */         
/*     */ 
/*     */ 
/* 235 */         List<BaseEntity> singleByActionList = (List)entry.getValue();
/* 236 */         List<List<BaseEntity>> listOfBatches = splitIntoBatches(repository, singleByActionList);
/* 237 */         for (List<BaseEntity> batch : listOfBatches)
/*     */         {
/*     */ 
/* 240 */           updatePropertyValueOnBatch(exeStatusPropertyName, Integer.valueOf(RMWorkflowStatus.Started.getIntValue()), batch);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 245 */           startupWorkflow(repository, actionName, batch, workflowType, vwVersionData, vwSession);
/*     */         }
/*     */       } }
/*     */     String actionName;
/*     */     String vwVersionData;
/* 250 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
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
/*     */   private static String[] getActionWorkflowIdent(Repository repository, String actionIdent)
/*     */   {
/* 265 */     Tracer.traceMethodEntry(new Object[] { repository, actionIdent });
/* 266 */     FilePlanRepository fpRepos = (FilePlanRepository)repository;
/* 267 */     DispositionAction action = RMFactory.DispositionAction.fetchInstance(fpRepos, actionIdent, null);
/* 268 */     String[] result = new String[2];
/* 269 */     RMWorkflowDefinition workflowDef = action.getAssociatedWorkflow();
/* 270 */     result[0] = workflowDef.getObjectIdentity();
/* 271 */     result[1] = action.getActionName();
/*     */     
/* 273 */     Tracer.traceMethodExit((Object[])result);
/* 274 */     return result;
/*     */   }
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
/*     */   private static void startupWorkflow(Repository repository, String actionName, List<BaseEntity> entities, int workflowType, String vwVersionData, Object vwSession)
/*     */   {
/* 294 */     Tracer.traceMethodEntry(new Object[] { repository, actionName, entities, Integer.valueOf(workflowType), vwVersionData, vwSession });
/*     */     
/*     */     try
/*     */     {
/* 298 */       String reposIdent = repository.getObjectIdentity();
/* 299 */       String[] entityIdents = new String[entities.size()];
/* 300 */       for (int i = 0; i < entities.size(); i++)
/*     */       {
/* 302 */         entityIdents[i] = ((BaseEntity)entities.get(i)).getObjectIdentity();
/*     */       }
/*     */       
/*     */ 
/* 306 */       StringBuilder sb = new StringBuilder();
/* 307 */       sb.append(actionName).append(':');
/* 308 */       for (int i = 0; (i < 5) && (i < entities.size()); i++)
/*     */       {
/* 310 */         if (i > 0) {
/* 311 */           sb.append(',');
/*     */         }
/* 313 */         String entityName = ((BaseEntity)entities.get(i)).getName();
/* 314 */         sb.append(entityName);
/*     */         
/* 316 */         if (sb.length() > 200)
/*     */         {
/* 318 */           sb.setLength(200);
/* 319 */           sb.append("....");
/* 320 */           break;
/*     */         }
/*     */       }
/* 323 */       String actionParamValue = sb.toString();
/*     */       
/* 325 */       Object vwStepElement = RALPESupport.createWorkflow(vwSession, vwVersionData);
/* 326 */       RALPESupport.setParameterValue(vwStepElement, "GUID", entityIdents, false);
/* 327 */       RALPESupport.setParameterValue(vwStepElement, "ObjectStoreID", reposIdent, false);
/* 328 */       RALPESupport.setParameterValue(vwStepElement, "F_Subject", actionParamValue, false);
/*     */       
/* 330 */       RALPESupport.doDispatch(vwStepElement);
/*     */     }
/*     */     catch (RMRuntimeException rre)
/*     */     {
/* 334 */       resetWorkflowStatus(repository, entities, workflowType);
/* 335 */       throw rre;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 339 */       resetWorkflowStatus(repository, entities, workflowType);
/* 340 */       throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[0]);
/*     */     }
/* 342 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void resetWorkflowStatus(Repository repository, List<BaseEntity> entities, int workflowType)
/*     */   {
/* 355 */     String propSymName = "VitalWorkflowStatus";
/* 356 */     if ((workflowType == 1003) || (workflowType == 1001)) {
/* 357 */       propSymName = "CurrentPhaseExecutionStatus";
/*     */     }
/* 359 */     updatePropertyValueOnBatch(propSymName, Integer.valueOf(RMWorkflowStatus.NotStarted.getIntValue()), entities);
/*     */   }
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
/*     */   private static String getVWVersionData(Repository repository, String workflowDefIdent)
/*     */   {
/* 377 */     Tracer.traceMethodEntry(new Object[] { repository, workflowDefIdent });
/*     */     
/* 379 */     String reposIdent = repository.getObjectIdentity();
/* 380 */     Map<String, String> perReposMap = (Map)VWVersionCache.get(reposIdent);
/* 381 */     if (perReposMap == null)
/*     */     {
/* 383 */       perReposMap = Collections.synchronizedMap(new HashMap());
/* 384 */       VWVersionCache.put(reposIdent, perReposMap);
/*     */     }
/*     */     
/* 387 */     String workflowDefKey = workflowDefIdent != null ? workflowDefIdent.trim() : null;
/* 388 */     if ((workflowDefKey == null) || (workflowDefKey.length() == 0))
/*     */     {
/* 390 */       workflowDefKey = "Screening Workflow";
/*     */     }
/*     */     
/* 393 */     String vwVersionData = null;
/* 394 */     if (perReposMap.containsKey(workflowDefKey))
/*     */     {
/* 396 */       vwVersionData = (String)perReposMap.get(workflowDefKey);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 403 */       String actualWorkflowDefIdent = workflowDefKey;
/* 404 */       if ("Screening Workflow".equalsIgnoreCase(workflowDefKey))
/*     */       {
/* 406 */         Map<String, SystemConfiguration> sysConfigs = ((FilePlanRepository)repository).getSystemConfigurations();
/* 407 */         if (sysConfigs != null)
/*     */         {
/* 409 */           SystemConfiguration screenWkflSysConfig = (SystemConfiguration)sysConfigs.get("Screening Workflow");
/* 410 */           if (screenWkflSysConfig != null)
/* 411 */             actualWorkflowDefIdent = screenWkflSysConfig.getPropertyValue();
/*     */         }
/*     */       }
/* 414 */       actualWorkflowDefIdent = actualWorkflowDefIdent != null ? actualWorkflowDefIdent.trim() : null;
/*     */       
/* 416 */       if ((actualWorkflowDefIdent != null) && (actualWorkflowDefIdent.length() > 0))
/*     */       {
/* 418 */         String queryPropName = Id.isId(actualWorkflowDefIdent) ? "Id" : "DocumentTitle";
/* 419 */         StringBuilder sb = new StringBuilder();
/* 420 */         sb.append("SELECT TOP 1 ").append("VWVersion").append(", ").append("DateCreated");
/* 421 */         sb.append(" FROM ").append("WorkflowDefinition");
/* 422 */         sb.append(" WHERE ").append(queryPropName).append(" = '").append(actualWorkflowDefIdent).append("'");
/* 423 */         sb.append(" ORDER BY ").append("DateCreated").append(' ');
/* 424 */         String sqlStmt = sb.toString();
/*     */         
/* 426 */         RMSearch rmSearch = new RMSearch(repository);
/* 427 */         PageableSet<ResultRow> resultSet = rmSearch.fetchRows(sqlStmt, null, null, Boolean.FALSE);
/* 428 */         Iterator<ResultRow> iter = resultSet.iterator();
/* 429 */         if (iter.hasNext())
/*     */         {
/* 431 */           ResultRow row = (ResultRow)iter.next();
/* 432 */           vwVersionData = row.getProperties().getStringValue("VWVersion");
/*     */         }
/*     */       }
/*     */       
/* 436 */       perReposMap.put(workflowDefKey, vwVersionData);
/*     */     }
/*     */     
/* 439 */     Tracer.traceMethodExit(new Object[] { vwVersionData });
/* 440 */     return vwVersionData;
/*     */   }
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
/*     */   private static List<List<BaseEntity>> splitIntoBatches(Repository repository, List<BaseEntity> entities)
/*     */   {
/* 455 */     Tracer.traceMethodEntry(new Object[] { repository, entities });
/* 456 */     List<List<BaseEntity>> listOfBatches = new ArrayList(1);
/*     */     
/* 458 */     int totalCount = entities.size();
/* 459 */     int maxBatchSize = getMaxWorkflowBatchSize(repository);
/*     */     
/* 461 */     if (totalCount <= maxBatchSize)
/*     */     {
/* 463 */       listOfBatches.add(entities);
/*     */     }
/*     */     else
/*     */     {
/* 467 */       int startIndex = 0;
/* 468 */       int endIndex = 0;
/* 469 */       List<BaseEntity> nextBatch = null;
/* 470 */       while (startIndex < totalCount)
/*     */       {
/* 472 */         endIndex = startIndex + Math.min(maxBatchSize, totalCount - startIndex);
/* 473 */         nextBatch = entities.subList(startIndex, endIndex);
/* 474 */         listOfBatches.add(nextBatch);
/* 475 */         startIndex = endIndex;
/*     */       }
/*     */     }
/*     */     
/* 479 */     Tracer.traceMethodExit(new Object[] { listOfBatches });
/* 480 */     return listOfBatches;
/*     */   }
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
/*     */   private static Map<String, List<BaseEntity>> separateByPropertyValue(String propSymName, List<BaseEntity> entities)
/*     */   {
/* 498 */     Tracer.traceMethodEntry(new Object[] { propSymName, entities });
/* 499 */     Map<String, List<BaseEntity>> resultMap = new HashMap();
/*     */     
/* 501 */     for (BaseEntity entity : entities)
/*     */     {
/* 503 */       Object rawValue = entity.getProperties().getObjectValue(propSymName);
/* 504 */       String strValue = rawValue != null ? rawValue.toString() : "<null>";
/* 505 */       addToMapOfLists(resultMap, strValue, entity);
/*     */     }
/*     */     
/* 508 */     Tracer.traceMethodExit(new Object[] { resultMap });
/* 509 */     return resultMap;
/*     */   }
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
/*     */   private static Map<String, List<BaseEntity>> separateByAction(int workflowType, List<BaseEntity> entities)
/*     */   {
/* 527 */     Tracer.traceMethodEntry(new Object[] { Integer.valueOf(workflowType) });
/* 528 */     String actionPropName = "CurrentPhaseAction";
/* 529 */     if (workflowType == 1002) {
/* 530 */       actionPropName = "VitalRecordReviewAction";
/*     */     }
/* 532 */     DispositionAction action = null;
/* 533 */     String actionIdent = null;
/* 534 */     Map<String, List<BaseEntity>> resultMap = new HashMap();
/* 535 */     for (BaseEntity entity : entities)
/*     */     {
/* 537 */       action = (DispositionAction)entity.getProperties().getObjectValue(actionPropName);
/* 538 */       actionIdent = action != null ? action.getObjectIdentity() : "<null>";
/* 539 */       addToMapOfLists(resultMap, actionIdent, entity);
/*     */     }
/*     */     
/* 542 */     Tracer.traceMethodExit(new Object[] { resultMap });
/* 543 */     return resultMap;
/*     */   }
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
/*     */   private static Map<String, List<BaseEntity>> separateByScreeningRequired(List<BaseEntity> entities)
/*     */   {
/* 558 */     Tracer.traceMethodEntry(new Object[] { entities });
/* 559 */     Map<String, List<BaseEntity>> resultMap = new HashMap();
/*     */     
/* 561 */     for (BaseEntity entity : entities)
/*     */     {
/* 563 */       Boolean entityValue = entity.getProperties().getBooleanValue("IsScreeningRequired");
/* 564 */       if (Boolean.TRUE.equals(entityValue)) {
/* 565 */         addToMapOfLists(resultMap, "SCREENING_REQUIRED", entity);
/*     */       } else {
/* 567 */         addToMapOfLists(resultMap, "SCREENING_NOT_REQUIRED", entity);
/*     */       }
/*     */     }
/* 570 */     Tracer.traceMethodExit(new Object[] { resultMap });
/* 571 */     return resultMap;
/*     */   }
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
/*     */   private static void addToMapOfLists(Map<String, List<BaseEntity>> mapOfLists, String key, BaseEntity entity)
/*     */   {
/* 585 */     List<BaseEntity> listToAppendTo = (List)mapOfLists.get(key);
/* 586 */     if (listToAppendTo == null)
/*     */     {
/* 588 */       listToAppendTo = new ArrayList();
/* 589 */       mapOfLists.put(key, listToAppendTo);
/*     */     }
/*     */     
/* 592 */     listToAppendTo.add(entity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void updateMaxBatchSizeCache(Repository repository)
/*     */   {
/* 604 */     int maxSize = getMaxWorkflowBatchSize_NoCache(repository);
/*     */     
/*     */ 
/* 607 */     String reposIdent = repository.getObjectIdentity();
/* 608 */     AtomicInteger cachedAI = (AtomicInteger)MaxWFLBatchSizes.get(reposIdent);
/* 609 */     if (cachedAI == null)
/*     */     {
/* 611 */       cachedAI = new AtomicInteger();
/* 612 */       MaxWFLBatchSizes.put(reposIdent, cachedAI);
/*     */     }
/*     */     
/* 615 */     cachedAI.set(maxSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getMaxWorkflowBatchSize(Repository repository)
/*     */   {
/* 628 */     String reposIdent = repository.getObjectIdentity();
/* 629 */     AtomicInteger cachedAI = (AtomicInteger)MaxWFLBatchSizes.get(reposIdent);
/* 630 */     if (cachedAI == null)
/*     */     {
/* 632 */       int maxSize = getMaxWorkflowBatchSize_NoCache(repository);
/* 633 */       cachedAI = new AtomicInteger(maxSize);
/* 634 */       MaxWFLBatchSizes.put(reposIdent, cachedAI);
/*     */     }
/*     */     
/* 637 */     return cachedAI.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getMaxWorkflowBatchSize_NoCache(Repository repository)
/*     */   {
/* 650 */     Tracer.traceMethodEntry(new Object[] { repository });
/* 651 */     int maxSize = 10;
/*     */     
/* 653 */     Map<String, SystemConfiguration> sysConfigs = ((FilePlanRepository)repository).getSystemConfigurations();
/* 654 */     if (sysConfigs != null)
/*     */     {
/* 656 */       SystemConfiguration sysConfig = (SystemConfiguration)sysConfigs.get("Maximum Batch Size For Workflows");
/* 657 */       if (sysConfig != null)
/*     */       {
/* 659 */         String sysConfigRawValue = sysConfig.getPropertyValue();
/* 660 */         if (sysConfigRawValue != null) {
/*     */           try {
/* 662 */             maxSize = Integer.parseInt(sysConfigRawValue, 10);
/*     */           }
/*     */           catch (NumberFormatException ignored) {}
/*     */         }
/*     */       }
/*     */     }
/* 668 */     if (maxSize <= 0) {
/* 669 */       maxSize = 10;
/*     */     }
/* 671 */     Tracer.traceMethodExit(new Object[] { Integer.valueOf(maxSize) });
/* 672 */     return maxSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void updatePropertyValueOnBatch(String propSymName, Object propValue, List<BaseEntity> batch)
/*     */   {
/*     */     DataType propDataType;
/*     */     
/*     */ 
/*     */ 
/* 685 */     if (batch.size() > 0)
/*     */     {
/* 687 */       if (((BaseEntity)batch.get(0)).getDomainType() == DomainType.P8_CE)
/*     */       {
/* 689 */         P8CE_Util.updatePropertyValueOnBatch(propSymName, propValue, batch);
/*     */       }
/*     */       else
/*     */       {
/* 693 */         propDataType = null;
/* 694 */         if ((propValue instanceof Integer)) {
/* 695 */           propDataType = DataType.Integer;
/* 696 */         } else if ((propValue instanceof String)) {
/* 697 */           propDataType = DataType.String;
/* 698 */         } else if ((propValue instanceof Date)) {
/* 699 */           propDataType = DataType.DateTime;
/*     */         }
/* 701 */         for (BaseEntity entity : batch)
/*     */         {
/* 703 */           switch (propDataType)
/*     */           {
/*     */           case Integer: 
/* 706 */             entity.getProperties().putIntegerValue(propSymName, (Integer)propValue);
/* 707 */             break;
/*     */           case String: 
/* 709 */             entity.getProperties().putStringValue(propSymName, (String)propValue);
/* 710 */             break;
/*     */           case DateTime: 
/* 712 */             entity.getProperties().putDateTimeValue(propSymName, (Date)propValue);
/*     */           }
/*     */           
/*     */           
/* 716 */           ((Persistable)entity).save(RMRefreshMode.Refresh);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RALWorkflowSupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */