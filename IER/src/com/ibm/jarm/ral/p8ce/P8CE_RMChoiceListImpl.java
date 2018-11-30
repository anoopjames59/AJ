/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.admin.Choice;
/*     */ import com.filenet.api.constants.TypeID;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Container;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.meta.RMChoiceItem;
/*     */ import com.ibm.jarm.api.meta.RMChoiceList;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.security.RMPermission;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMChoiceListImpl
/*     */   extends RALBaseEntity
/*     */   implements RMChoiceList
/*     */ {
/*  39 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  40 */   private static final IGenerator<RMChoiceList> RMChoiceListGenerator = new Generator();
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
/*  51 */   static final String[] MandatoryPropertyNames = { "Id", "ChoiceValues", "DataType", "DisplayName", "DescriptiveText", "Name", "HasHierarchy" };
/*     */   
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   com.filenet.api.admin.ChoiceList jaceChoiceList;
/*     */   
/*     */   static
/*     */   {
/*  59 */     List<FilterElement> tempList = new ArrayList(2);
/*     */     
/*  61 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*  62 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null));
/*     */     
/*  64 */     mandatoryNames = P8CE_Util.createSpaceSeparatedString(P8CE_RMChoiceItemImpl.MandatoryPropertyNames);
/*  65 */     tempList.add(new FilterElement(Integer.valueOf(1), null, Boolean.TRUE, mandatoryNames, null));
/*  66 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  71 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<RMChoiceList> getGenerator()
/*     */   {
/*  81 */     return RMChoiceListGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   P8CE_RMChoiceListImpl(Repository repository, com.filenet.api.admin.ChoiceList jaceChoiceList)
/*     */   {
/*  89 */     super(EntityType.ChoiceList, repository, jaceChoiceList.get_Id().toString(), false);
/*  90 */     Tracer.traceMethodEntry(new Object[] { repository, jaceChoiceList });
/*  91 */     this.jaceChoiceList = jaceChoiceList;
/*  92 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMChoiceItem> getChoiceListValues()
/*     */   {
/* 101 */     Tracer.traceMethodEntry(new Object[0]);
/* 102 */     List<RMChoiceItem> result = null;
/* 103 */     com.filenet.api.collection.ChoiceList jaceSubChoices = this.jaceChoiceList.get_ChoiceValues();
/* 104 */     if (jaceSubChoices != null)
/*     */     {
/* 106 */       Choice jaceChoice = null;
/* 107 */       result = new ArrayList();
/* 108 */       Iterator<Choice> it = jaceSubChoices.iterator();
/* 109 */       while ((it != null) && (it.hasNext()))
/*     */       {
/* 111 */         jaceChoice = (Choice)it.next();
/* 112 */         if (jaceChoice != null) {
/* 113 */           result.add(new P8CE_RMChoiceItemImpl(jaceChoice));
/*     */         }
/*     */       }
/*     */     }
/* 117 */     Tracer.traceMethodExit(new Object[] { result });
/* 118 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataType getDataType()
/*     */   {
/* 126 */     Tracer.traceMethodEntry(new Object[0]);
/* 127 */     DataType result = DataType.String;
/* 128 */     TypeID jaceType = this.jaceChoiceList.get_DataType();
/* 129 */     if (jaceType != null)
/*     */     {
/* 131 */       int intValue = jaceType.getValue();
/* 132 */       result = DataType.getInstanceFromInt(intValue);
/*     */     }
/*     */     
/* 135 */     Tracer.traceMethodExit(new Object[] { result });
/* 136 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescriptiveText()
/*     */   {
/* 144 */     return this.jaceChoiceList.get_DescriptiveText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 152 */     return this.jaceChoiceList.get_DisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 160 */     Tracer.traceMethodEntry(new Object[0]);
/* 161 */     String result = null;
/* 162 */     Id choiceId = this.jaceChoiceList.get_Id();
/* 163 */     if (choiceId != null) {
/* 164 */       result = choiceId.toString();
/*     */     }
/* 166 */     Tracer.traceMethodExit(new Object[] { result });
/* 167 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 175 */     return this.jaceChoiceList.get_Name();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasHierarchy()
/*     */   {
/* 183 */     Tracer.traceMethodEntry(new Object[0]);
/* 184 */     Boolean jaceBool = this.jaceChoiceList.get_HasHierarchy();
/* 185 */     boolean result = jaceBool != null ? jaceBool.booleanValue() : false;
/* 186 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 187 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 196 */     return "ChoiceList";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getObjectIdentity()
/*     */   {
/* 205 */     Tracer.traceMethodEntry(new Object[0]);
/* 206 */     String result = P8CE_Util.getJaceObjectIdentity(this.jaceChoiceList);
/* 207 */     Tracer.traceMethodExit(new Object[] { result });
/* 208 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Container> getContainedBy()
/*     */   {
/* 217 */     Tracer.traceMethodEntry(new Object[0]);
/* 218 */     List<Container> result = Collections.EMPTY_LIST;
/* 219 */     Tracer.traceMethodExit(new Object[] { result });
/* 220 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMPermission> getPermissions()
/*     */   {
/* 229 */     Tracer.traceMethodEntry(new Object[0]);
/* 230 */     List<RMPermission> result = Collections.EMPTY_LIST;
/* 231 */     Tracer.traceMethodExit(new Object[] { result });
/* 232 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperties getProperties()
/*     */   {
/* 240 */     Tracer.traceMethodEntry(new Object[0]);
/* 241 */     RMProperties result = null;
/* 242 */     if (this.jaceChoiceList != null)
/*     */     {
/* 244 */       result = new P8CE_RMPropertiesImpl(this.jaceChoiceList, this);
/*     */     }
/*     */     
/* 247 */     Tracer.traceMethodExit(new Object[] { result });
/* 248 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh()
/*     */   {
/* 256 */     Tracer.traceMethodEntry(new Object[0]);
/* 257 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "refresh", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh(RMPropertyFilter filter)
/*     */   {
/* 265 */     Tracer.traceMethodEntry(new Object[] { filter });
/* 266 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "refresh", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh(String[] symbolicPropertyNames)
/*     */   {
/* 274 */     Tracer.traceMethodEntry(new Object[] { symbolicPropertyNames });
/* 275 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "refresh", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getAccessAllowed()
/*     */   {
/* 283 */     return this.jaceChoiceList.getAccessAllowed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainType getDomainType()
/*     */   {
/* 291 */     return DomainType.P8_CE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void internalSave(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 299 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 300 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "internalSave", getEntityType(), getClientIdentifier() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<RMChoiceList>
/*     */   {
/*     */     public RMChoiceList create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 314 */       P8CE_RMChoiceListImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/*     */       
/* 316 */       com.filenet.api.admin.ChoiceList jaceChoiceList = (com.filenet.api.admin.ChoiceList)jaceBaseObject;
/* 317 */       RMChoiceList result = new P8CE_RMChoiceListImpl(repository, jaceChoiceList);
/*     */       
/* 319 */       P8CE_RMChoiceListImpl.Tracer.traceMethodExit(new Object[] { result });
/* 320 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMChoiceListImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */