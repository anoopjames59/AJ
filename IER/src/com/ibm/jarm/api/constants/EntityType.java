/*     */ package com.ibm.jarm.api.constants;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum EntityType
/*     */ {
/*  20 */   FilePlan(100), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  25 */   RecordCategory(101), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  30 */   RecordFolder(102), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   RecordVolume(103), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   ElectronicRecordFolder(105), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  45 */   PhysicalContainer(106), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   HybridRecordFolder(108), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   RMFolder(109), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   PhysicalRecordFolder(110), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   DispositionAction(200), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   DispositionSchedule(201), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   DisposalPhase(202), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   DispositionTrigger(203), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   Pattern(205), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  91 */   PatternLevel(206), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  96 */   ReportDefinition(207), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 101 */   Reservation(209), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 106 */   Phase(210), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 111 */   PhaseException(211), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 116 */   SystemConfiguration(212), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 121 */   ConnectorRegistration(213), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 126 */   RecordType(214), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 131 */   Location(215), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 136 */   Hold(216), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 141 */   PatternSequence(217), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 146 */   AlternateRetention(218), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 151 */   RMLog(219), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */   RMSystem(220), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */   Record(300), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 168 */   ElectronicRecord(301), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 173 */   EmailRecord(302), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 178 */   PhysicalRecord(303), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 183 */   Transcript(304), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 188 */   PDFRecord(305), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */   Relation(400), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 199 */   ExtractLink(401), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 204 */   RecordCopyLink(402), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 209 */   RecordSeeAlsoLink(403), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 214 */   ReferenceLink(404), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 219 */   SupersedeLink(405), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 224 */   RecordHoldLink(406), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 229 */   RecordContainerHoldLink(407), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 234 */   RMLink(408), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 239 */   ReceiptLink(409), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 245 */   ContentRepository(500), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 250 */   FilePlanRepository(501), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 255 */   Repository(549), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 260 */   Domain(550), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 266 */   AuditEvent(700), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 272 */   ClassificationGuide(800), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 277 */   ClassificationGuideSection(801), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 282 */   ClassificationGuideTopic(802), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 287 */   AuditConfig(803), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 292 */   TransferMapping(804), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 297 */   Container(1100), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 302 */   CustomObject(1200), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 307 */   SecurityPrincipal(1201), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 312 */   AccessRole(1202), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 317 */   ContentItem(1300), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 322 */   WorkflowDefinition(1301), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 327 */   MarkingSet(1400), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 332 */   ChoiceList(1401), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 337 */   Unknown(-1);
/*     */   
/*     */   private static Map<Integer, EntityType> ByIntValueMap;
/*     */   private int intValue;
/*     */   
/* 342 */   static { ByIntValueMap = new HashMap();
/*     */     
/*     */ 
/* 345 */     for (EntityType entityType : values())
/*     */     {
/* 347 */       ByIntValueMap.put(Integer.valueOf(entityType.getIntValue()), entityType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private EntityType(int intValue)
/*     */   {
/* 355 */     this.intValue = intValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntValue()
/*     */   {
/* 365 */     return this.intValue;
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
/*     */   public static EntityType getInstanceFromInt(int intValue)
/*     */   {
/* 379 */     return (EntityType)ByIntValueMap.get(Integer.valueOf(intValue));
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\EntityType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */