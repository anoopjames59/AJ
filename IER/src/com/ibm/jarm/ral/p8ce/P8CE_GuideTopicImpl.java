/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.StringList;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.ClassificationGuideTopic;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.property.RMPropertyFilter;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
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
/*     */ class P8CE_GuideTopicImpl
/*     */   implements ClassificationGuideTopic
/*     */ {
/* 401 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
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
/* 415 */   protected static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "Code", "Topic", "InitialClassification", "ReasonsForClassification", "DeclassifyOnDate", "DeclassifyOnEvents", "Exemptions", "Remarks", "SupplementalMarkingsStringList", "GuideID" };
/*     */   
/*     */ 
/*     */   protected static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/* 424 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/* 426 */     List<FilterElement> tempList = new ArrayList(1);
/* 427 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/* 428 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList); }
/*     */   
/* 430 */   private static final PropertyFilter MandatoryJacePF = P8CE_Util.convertToJacePF(RMPropertyFilter.MinimumPropertySet, MandatoryJaceFEs);
/*     */   
/*     */   protected Repository repository;
/*     */   
/*     */   protected EngineObject jaceBaseObject;
/*     */   
/*     */   static PropertyFilter getMandatoryJacePF()
/*     */   {
/* 438 */     return MandatoryJacePF;
/*     */   }
/*     */   
/*     */ 
/*     */   P8CE_GuideTopicImpl(Repository repository, EngineObject jaceBaseObject)
/*     */   {
/* 444 */     Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 445 */     this.repository = repository;
/* 446 */     this.jaceBaseObject = jaceBaseObject;
/* 447 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 455 */     Id idValue = this.jaceBaseObject.getProperties().getIdValue("Id");
/* 456 */     String result = idValue != null ? idValue.toString() : null;
/* 457 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCode()
/*     */   {
/* 466 */     return this.jaceBaseObject.getProperties().getStringValue("Code");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getDeclassifyOnDate()
/*     */   {
/* 474 */     return this.jaceBaseObject.getProperties().getDateTimeValue("DeclassifyOnDate");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getDeclassifyOnEvents()
/*     */   {
/* 483 */     StringList jaceList = this.jaceBaseObject.getProperties().getStringListValue("DeclassifyOnEvents");
/* 484 */     return new ArrayList(jaceList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getExemptions()
/*     */   {
/* 493 */     StringList jaceList = this.jaceBaseObject.getProperties().getStringListValue("Exemptions");
/* 494 */     return new ArrayList(jaceList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGuideID()
/*     */   {
/* 502 */     Id rawId = this.jaceBaseObject.getProperties().getIdValue("GuideID");
/* 503 */     return rawId != null ? rawId.toString() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getInitialClassification()
/*     */   {
/* 511 */     return this.jaceBaseObject.getProperties().getStringValue("InitialClassification");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getReasonsForClassification()
/*     */   {
/* 520 */     StringList jaceList = this.jaceBaseObject.getProperties().getStringListValue("ReasonsForClassification");
/* 521 */     return new ArrayList(jaceList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRemarks()
/*     */   {
/* 529 */     return this.jaceBaseObject.getProperties().getStringValue("Remarks");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityType getRMEntityType()
/*     */   {
/* 537 */     Integer rawInteger = this.jaceBaseObject.getProperties().getInteger32Value("RMEntityType");
/* 538 */     return rawInteger != null ? EntityType.getInstanceFromInt(rawInteger.intValue()) : EntityType.Unknown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getSupplementalMarkings()
/*     */   {
/* 547 */     StringList jaceList = this.jaceBaseObject.getProperties().getStringListValue("SupplementalMarkingsStringList");
/* 548 */     return new ArrayList(jaceList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTopic()
/*     */   {
/* 556 */     return this.jaceBaseObject.getProperties().getStringValue("Topic");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 565 */     String ident = "<unknown>";
/* 566 */     if (this.jaceBaseObject.getProperties().isPropertyPresent("DocumentTitle")) {
/* 567 */       ident = this.jaceBaseObject.getProperties().getStringValue("DocumentTitle");
/*     */     } else {
/* 569 */       ident = ((IndependentObject)this.jaceBaseObject).getObjectReference().getObjectIdentity();
/*     */     }
/* 571 */     return "P8CE_GuideTopicImpl: '" + ident + "'";
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_GuideTopicImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */