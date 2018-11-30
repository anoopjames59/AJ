/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.admin.ClassDefinition;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Factory.CustomObject;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.Properties;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8RecordsUtil
/*     */ {
/*     */   private static final String FPOS_SETUP_ID = "{C0B882FD-9434-4885-82B7-B13B59E29CFF}";
/*     */   private static final String PROPERTY_VALUE = "PropertyValue";
/*     */   private static final String BASE = "Base";
/*     */   private static final String PRO_2002 = "PRO-2002";
/*     */   private static final String DOD_5015_CLASSIFIED = "DOD-5015.2 Classified";
/*     */   private static final String DOD_5015 = "DOD-5015.2";
/*     */   public static final String RECORD = "RecordInfo";
/*     */   public static final String ELECTRONIC_RECORD = "ElectronicRecordInfo";
/*     */   public static final String RMFOLDER = "RMFolder";
/*     */   public static final String SYSTEM_CONFIGURATION = "SystemConfiguration";
/*     */   public static final String FILEPLAN = "ClassificationScheme";
/*     */   
/*     */   public static enum Datamodel
/*     */   {
/*  30 */     Base,  DoD,  Pro,  DoDClassified;
/*     */     
/*     */     private Datamodel() {} }
/*     */   
/*  34 */   public static enum RecordRepositoryType { Content,  FilePlan,  Plain;
/*     */     
/*     */ 
/*     */     private RecordRepositoryType() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public static Datamodel getDatamodelType(ObjectStore os)
/*     */   {
/*  43 */     CustomObject co = Factory.CustomObject.fetchInstance(os, "{C0B882FD-9434-4885-82B7-B13B59E29CFF}", null);
/*  44 */     if (co != null) {
/*  45 */       Properties properties = co.getProperties();
/*  46 */       if (properties.isPropertyPresent("PropertyValue")) {
/*  47 */         String datamodel = co.getProperties().getStringValue("PropertyValue");
/*  48 */         if (datamodel.equals("Base"))
/*  49 */           return Datamodel.Base;
/*  50 */         if (datamodel.equals("PRO-2002"))
/*  51 */           return Datamodel.Pro;
/*  52 */         if (datamodel.equals("DOD-5015.2 Classified"))
/*  53 */           return Datamodel.DoDClassified;
/*  54 */         if (datamodel.equals("DOD-5015.2")) {
/*  55 */           return Datamodel.DoD;
/*     */         }
/*  57 */         return null;
/*     */       }
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RecordRepositoryType getRecordRepositoryType(ObjectStore os)
/*     */   {
/*  69 */     P8Query query = new P8Query();
/*  70 */     query.setRequestedProperties(new String[] { "Id", "SymbolicName", "DisplayName" });
/*  71 */     query.setFromClause("ClassDefinition");
/*  72 */     query.setObjectStore(os);
/*     */     
/*  74 */     StringBuilder whereClause = new StringBuilder("SymbolicName");
/*  75 */     whereClause.append(" IN ('");
/*  76 */     whereClause.append("RecordInfo', 'ElectronicRecordInfo', 'RMFolder', 'SystemConfiguration', 'ClassificationScheme')");
/*  77 */     query.setWhereClause(whereClause.toString());
/*     */     
/*     */ 
/*     */ 
/*  81 */     int contentIndicator = 0;
/*  82 */     int fileplanIndicator = 0;
/*  83 */     Iterator<IndependentObject> results = query.executeQueryAsObjectsIterator();
/*  84 */     while (results.hasNext()) {
/*  85 */       ClassDefinition cd = (ClassDefinition)results.next();
/*  86 */       String name = cd.get_SymbolicName();
/*  87 */       if (name.equals("RecordInfo")) {
/*  88 */         contentIndicator++;
/*  89 */         fileplanIndicator++;
/*     */       }
/*     */       
/*  92 */       if ((name.equals("ElectronicRecordInfo")) || (name.equals("RMFolder")) || (name.equals("SystemConfiguration")) || (name.equals("ClassificationScheme"))) {
/*  93 */         fileplanIndicator++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  98 */     if ((contentIndicator == 1) && (fileplanIndicator != 5))
/*  99 */       return RecordRepositoryType.Content;
/* 100 */     if (fileplanIndicator == 5) {
/* 101 */       return RecordRepositoryType.FilePlan;
/*     */     }
/* 103 */     return RecordRepositoryType.Plain;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8RecordsUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */