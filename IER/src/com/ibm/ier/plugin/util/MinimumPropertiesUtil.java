/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinimumPropertiesUtil
/*     */ {
/*     */   private static final String RMEntityType = "RMEntityType";
/*  23 */   private static List<String> BASE_LIST = Arrays.asList(new String[] { "Id", "Name" });
/*     */   
/*  25 */   private static Map<EntityType, String[]> EntityClassProperties_Map = Collections.unmodifiableMap(new HashMap() {});
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getPropertySet(EntityType type)
/*     */   {
/* 167 */     return (String[])EntityClassProperties_Map.get(type);
/*     */   }
/*     */   
/*     */   public static List<String> getPropertySetList(BaseEntity obj) {
/* 171 */     RMProperties properties = obj.getProperties();
/* 172 */     if (properties.isPropertyPresent("RMEntityType")) {
/* 173 */       return getPropertySetList(obj.getEntityType());
/*     */     }
/* 175 */     List<String> propertyNames = new ArrayList();
/* 176 */     for (RMProperty property : properties) {
/* 177 */       propertyNames.add(property.getSymbolicName());
/*     */     }
/* 179 */     return propertyNames;
/*     */   }
/*     */   
/*     */   public static List<String> getPropertySetList(EntityType entityType)
/*     */   {
/* 184 */     if ((entityType == EntityType.ElectronicRecordFolder) || (entityType == EntityType.HybridRecordFolder) || (entityType == EntityType.PhysicalRecordFolder) || (entityType == EntityType.PhysicalContainer))
/*     */     {
/* 186 */       entityType = EntityType.RecordFolder;
/*     */     }
/* 188 */     else if ((entityType == EntityType.ElectronicRecord) || (entityType == EntityType.EmailRecord) || (entityType == EntityType.PhysicalRecord) || (entityType == EntityType.Transcript) || (entityType == EntityType.PDFRecord))
/*     */     {
/* 190 */       entityType = EntityType.Record;
/*     */     }
/*     */     
/* 193 */     String[] properties = (String[])EntityClassProperties_Map.get(entityType);
/* 194 */     if (properties != null) {
/* 195 */       return Arrays.asList(properties);
/*     */     }
/* 197 */     return BASE_LIST;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\MinimumPropertiesUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */