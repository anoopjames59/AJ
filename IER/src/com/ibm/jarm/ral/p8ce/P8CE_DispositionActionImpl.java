/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.ibm.jarm.api.constants.DispositionActionType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.ContentItem;
/*     */ import com.ibm.jarm.api.core.DispositionAction;
/*     */ import com.ibm.jarm.api.core.DispositionSchedule;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.RMWorkflowDefinition;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALDispositionLogic;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CE_DispositionActionImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements DispositionAction
/*     */ {
/*  37 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  38 */   private static final IGenerator<DispositionAction> DispActionGenerator = new Generator();
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
/*  49 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "ActionName", "RMEntityDescription", "ActionType", "RMValidationOptions" };
/*     */   
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   static
/*     */   {
/*  55 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  57 */     List<FilterElement> tempList = new ArrayList(1);
/*  58 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  59 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  64 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  69 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<DispositionAction> getGenerator()
/*     */   {
/*  79 */     return DispActionGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  87 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */   P8CE_DispositionActionImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/*  92 */     super(EntityType.DispositionAction, repository, identity, jaceCustomObject, isPlaceholder);
/*  93 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/*  94 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getActionName()
/*     */   {
/* 103 */     Tracer.traceMethodEntry(new Object[0]);
/* 104 */     String result = P8CE_Util.getJacePropertyAsString(this, "ActionName");
/* 105 */     Tracer.traceMethodExit(new Object[] { result });
/* 106 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setActionName(String name)
/*     */   {
/* 114 */     Tracer.traceMethodEntry(new Object[] { name });
/* 115 */     Util.ckInvalidStrParam("name", name);
/* 116 */     getProperties().putStringValue("ActionName", name);
/* 117 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 125 */     Tracer.traceMethodEntry(new Object[0]);
/* 126 */     String result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/* 127 */     Tracer.traceMethodExit(new Object[] { result });
/* 128 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String value)
/*     */   {
/* 136 */     Tracer.traceMethodEntry(new Object[] { value });
/* 137 */     getProperties().putStringValue("RMEntityDescription", value);
/* 138 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DispositionActionType getActionType()
/*     */   {
/* 146 */     Tracer.traceMethodEntry(new Object[0]);
/* 147 */     DispositionActionType result = null;
/*     */     
/* 149 */     Integer rawType = P8CE_Util.getJacePropertyAsInteger(this, "ActionType");
/* 150 */     if (rawType != null) {
/* 151 */       result = DispositionActionType.getInstanceFromInt(rawType.intValue());
/*     */     }
/* 153 */     Tracer.traceMethodExit(new Object[] { result });
/* 154 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMWorkflowDefinition getAssociatedWorkflow()
/*     */   {
/* 162 */     Tracer.traceMethodEntry(new Object[0]);
/* 163 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 166 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 168 */       List<FilterElement> jaceContentItemFEs = P8CE_ContentItemImpl.getMandatoryJaceFEs();
/* 169 */       IGenerator<ContentItem> generator = P8CE_ContentItemImpl.getContentItemGenerator();
/* 170 */       ContentItem result = (ContentItem)fetchSVObjPropValue(jaceContentItemFEs, "DefaultWorkflow", generator);
/*     */       
/* 172 */       Tracer.traceMethodExit(new Object[] { result });
/* 173 */       return (RMWorkflowDefinition)result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 177 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 181 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { getObjectIdentity() });
/*     */     }
/*     */     finally
/*     */     {
/* 185 */       if (establishedSubject) {
/* 186 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAssociatedWorkflow(RMWorkflowDefinition workflow)
/*     */   {
/* 195 */     Tracer.traceMethodEntry(new Object[] { workflow });
/* 196 */     Util.ckNullObjParam("workflow", workflow);
/* 197 */     getProperties().putObjectValue("DefaultWorkflow", workflow);
/* 198 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<DispositionSchedule> getAssociatedDispositionSchedules()
/*     */   {
/* 206 */     Tracer.traceMethodEntry(new Object[0]);
/* 207 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 210 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 212 */       List<FilterElement> jaceDispSchedFEs = P8CE_DispositionScheduleImpl.getMandatoryJaceFEs();
/* 213 */       IGenerator<DispositionSchedule> generator = P8CE_DispositionScheduleImpl.getGenerator();
/* 214 */       List<DispositionSchedule> results = fetchMVObjPropValueAsList(jaceDispSchedFEs, "AssociatedCutOffDisposalTriggers", generator);
/*     */       
/* 216 */       Tracer.traceMethodExit(new Object[] { results });
/* 217 */       return results;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 221 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 225 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PROPERTY_UNAVAILABLE, new Object[] { "AssociatedCutOffDisposalTriggers" });
/*     */     }
/*     */     finally
/*     */     {
/* 229 */       if (establishedSubject) {
/* 230 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 240 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 241 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 244 */       establishedSubject = P8CE_Util.associateSubject();
/* 245 */       RALDispositionLogic.validateDispositionAction((FilePlanRepository)getRepository(), this);
/* 246 */       super.save(jarmRefreshMode);
/* 247 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 251 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 255 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.DispositionAction });
/*     */     }
/*     */     finally
/*     */     {
/* 259 */       if (establishedSubject) {
/* 260 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 270 */     return super.toString("P8CE_DispositionActionImpl", "ActionName");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<DispositionAction>
/*     */   {
/*     */     public DispositionAction create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 284 */       P8CE_DispositionActionImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 285 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 287 */       String identity = "<undefined>";
/* 288 */       if (jaceCustomObj.getProperties().isPropertyPresent("ActionName")) {
/* 289 */         identity = jaceCustomObj.getProperties().getStringValue("ActionName");
/*     */       }
/* 291 */       DispositionAction result = new P8CE_DispositionActionImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 293 */       P8CE_DispositionActionImpl.Tracer.traceMethodExit(new Object[] { result });
/* 294 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_DispositionActionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */