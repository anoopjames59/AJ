/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.constants.AutoUniqueName;
/*     */ import com.filenet.api.constants.DefineSecurityParentage;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.CustomObject;
/*     */ import com.filenet.api.core.Domain;
/*     */ import com.filenet.api.core.Factory.Folder;
/*     */ import com.filenet.api.core.Factory.ReferentialContainmentRelationship;
/*     */ import com.filenet.api.core.Folder;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.core.ReferentialContainmentRelationship;
/*     */ import com.filenet.api.core.UpdatingBatch;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.Location;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
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
/*     */ class P8CE_LocationImpl
/*     */   extends P8CE_RMCustomObjectImpl
/*     */   implements Location
/*     */ {
/*     */   public static final String LOCATIONS_CONTAINER_NAME = "Locations";
/*  43 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*  44 */   private static final IGenerator<Location> RMLocationGenerator = new Generator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private static final String[] MandatoryPropertyNames = { "Id", "RMEntityType", "LocationName", "RMEntityDescription", "BarcodeID", "Reviewer" };
/*     */   
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   static
/*     */   {
/*  61 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  63 */     List<FilterElement> tempList = new ArrayList(1);
/*  64 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  65 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  70 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  75 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static IGenerator<Location> getGenerator()
/*     */   {
/*  85 */     return RMLocationGenerator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/*  93 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */ 
/*     */   P8CE_LocationImpl(Repository repository, String identity, CustomObject jaceCustomObject, boolean isPlaceholder)
/*     */   {
/*  99 */     super(EntityType.Location, repository, identity, jaceCustomObject, isPlaceholder);
/* 100 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceCustomObject, Boolean.valueOf(isPlaceholder) });
/* 101 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBarcode()
/*     */   {
/* 109 */     Tracer.traceMethodEntry(new Object[0]);
/* 110 */     String result = P8CE_Util.getJacePropertyAsString(this, "BarcodeID");
/* 111 */     Tracer.traceMethodExit(new Object[] { result });
/* 112 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBarcode(String value)
/*     */   {
/* 120 */     Tracer.traceMethodEntry(new Object[] { value });
/* 121 */     getProperties().putStringValue("BarcodeID", value);
/* 122 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 130 */     Tracer.traceMethodEntry(new Object[0]);
/* 131 */     String result = P8CE_Util.getJacePropertyAsString(this, "RMEntityDescription");
/* 132 */     Tracer.traceMethodExit(new Object[] { result });
/* 133 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String value)
/*     */   {
/* 141 */     Tracer.traceMethodEntry(new Object[] { value });
/* 142 */     getProperties().putStringValue("RMEntityDescription", value);
/* 143 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocationName()
/*     */   {
/* 151 */     Tracer.traceMethodEntry(new Object[0]);
/* 152 */     String result = P8CE_Util.getJacePropertyAsString(this, "LocationName");
/* 153 */     Tracer.traceMethodExit(new Object[] { result });
/* 154 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocationName(String locationName)
/*     */   {
/* 162 */     Tracer.traceMethodEntry(new Object[] { locationName });
/* 163 */     Util.ckInvalidStrParam("locationName", locationName);
/* 164 */     getProperties().putStringValue("LocationName", locationName);
/* 165 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReviewer()
/*     */   {
/* 173 */     Tracer.traceMethodEntry(new Object[0]);
/* 174 */     String result = P8CE_Util.getJacePropertyAsString(this, "Reviewer");
/* 175 */     Tracer.traceMethodExit(new Object[] { result });
/* 176 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReviewer(String value)
/*     */   {
/* 184 */     Tracer.traceMethodEntry(new Object[] { value });
/* 185 */     getProperties().putStringValue("Reviewer", value);
/* 186 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 195 */     Tracer.traceMethodEntry(new Object[0]);
/* 196 */     String result = getLocationName();
/* 197 */     Tracer.traceMethodExit(new Object[] { result });
/* 198 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 209 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 210 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 213 */       establishedSubject = P8CE_Util.associateSubject();
/* 214 */       validateLocation();
/*     */       
/* 216 */       if (this.isCreationPending)
/*     */       {
/* 218 */         StringBuilder sb = new StringBuilder();
/* 219 */         sb.append(((P8CE_FilePlanRepositoryImpl)this.repository).getFilePlanRootPath());
/* 220 */         sb.append('/').append("Locations");
/* 221 */         String locationsContainerPath = sb.toString();
/*     */         
/* 223 */         RMDomain jarmDomain = getRepository().getDomain();
/* 224 */         Domain jaceDomain = ((P8CE_RMDomainImpl)jarmDomain).getOrFetchJaceDomain();
/* 225 */         ObjectStore jaceObjStore = this.jaceCustomObject.getObjectStore();
/* 226 */         RefreshMode jaceRefreshMode = jarmRefreshMode == RMRefreshMode.Refresh ? RefreshMode.REFRESH : RefreshMode.NO_REFRESH;
/* 227 */         UpdatingBatch jaceUB = UpdatingBatch.createUpdatingBatchInstance(jaceDomain, jaceRefreshMode);
/*     */         
/* 229 */         PropertyFilter jaceLocationPF = new PropertyFilter();
/* 230 */         for (FilterElement fe : getMandatoryJaceFEs()) {
/* 231 */           jaceLocationPF.addIncludeProperty(fe);
/*     */         }
/* 233 */         jaceUB.add(this.jaceCustomObject, jaceLocationPF);
/* 234 */         ReferentialContainmentRelationship rcr = Factory.ReferentialContainmentRelationship.createInstance(jaceObjStore, "ReferentialContainmentRelationship", AutoUniqueName.NOT_AUTO_UNIQUE, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 239 */         Folder jaceLocationsBase = Factory.Folder.getInstance(jaceObjStore, "Folder", locationsContainerPath);
/* 240 */         rcr.getProperties().putValue("Head", this.jaceCustomObject);
/* 241 */         rcr.getProperties().putValue("Tail", jaceLocationsBase);
/* 242 */         PropertyFilter rcrPF = new PropertyFilter();
/* 243 */         jaceUB.add(rcr, rcrPF);
/*     */         
/* 245 */         long startTime = System.currentTimeMillis();
/* 246 */         jaceUB.updateBatch();
/* 247 */         long endTime = System.currentTimeMillis();
/* 248 */         Tracer.traceExtCall("UpdatingBatch.updateBatch", startTime, endTime, null, null, new Object[0]);
/*     */         
/* 250 */         this.isCreationPending = false;
/*     */       }
/*     */       else
/*     */       {
/* 254 */         super.save(jarmRefreshMode);
/*     */       }
/*     */       
/* 257 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 261 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 265 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.Location });
/*     */     }
/*     */     finally
/*     */     {
/* 269 */       if (establishedSubject) {
/* 270 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 280 */     if (this.isPlaceholder) {
/* 281 */       return "P8CE_LocationImpl ObjectIdent: " + getObjectIdentity();
/*     */     }
/* 283 */     return "P8CE_LocationImpl LocationName: " + getLocationName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class Generator
/*     */     implements IGenerator<Location>
/*     */   {
/*     */     public Location create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 298 */       P8CE_LocationImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 299 */       CustomObject jaceCustomObj = (CustomObject)jaceBaseObject;
/*     */       
/* 301 */       String identity = "<undefined>";
/* 302 */       if (jaceCustomObj.getProperties().isPropertyPresent("LocationName")) {
/* 303 */         identity = jaceCustomObj.getProperties().getStringValue("LocationName");
/*     */       }
/* 305 */       Location result = new P8CE_LocationImpl(repository, identity, jaceCustomObj, false);
/*     */       
/* 307 */       P8CE_LocationImpl.Tracer.traceMethodExit(new Object[] { result });
/* 308 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_LocationImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */