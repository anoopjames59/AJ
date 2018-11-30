/*     */ package com.ibm.ier.plugin.services.responseFilters;
/*     */ 
/*     */ import com.filenet.api.collection.IndependentObjectSet;
/*     */ import com.filenet.api.core.Factory.Document;
/*     */ import com.filenet.api.core.Factory.Folder;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.Link;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.ibm.ecm.extension.PluginServiceCallbacks;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBaseResponseFilterService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.UserUtil;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8OpenItemOverride
/*     */   extends IERBaseResponseFilterService
/*     */ {
/*  27 */   private IndependentObject originalItem = null;
/*     */   
/*     */   private static final String CONTAINER_ID = "ContainerId";
/*     */   
/*     */   public String[] getFilteredServices()
/*     */   {
/*  33 */     return new String[] { "/p8/openItem" };
/*     */   }
/*     */   
/*     */   public void filterServiceExecute(HttpServletRequest request, JSONObject jsonResponse) throws Exception
/*     */   {
/*     */     try {
/*  39 */       criteriaArray = (JSONArray)jsonResponse.get("criterias");
/*  40 */       String id = (String)jsonResponse.get("docid");
/*  41 */       String mimeType = (String)jsonResponse.get("mimetype");
/*     */       
/*  43 */       List<JSONObject> criteriasToAdd = new ArrayList();
/*  44 */       boolean isExternallyManaged = false;
/*     */       
/*     */ 
/*  47 */       for (Object o : criteriaArray) {
/*  48 */         JSONObject criteriaObject = (JSONObject)o;
/*  49 */         String name = (String)criteriaObject.get("name");
/*  50 */         if ((name != null) && (name.equals("RMExternallyManagedBy")) && 
/*  51 */           (criteriaObject.get("values") != null))
/*     */         {
/*  53 */           isExternallyManaged = true;
/*  54 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  60 */       for (Object o : criteriaArray) {
/*  61 */         JSONObject criteriaObject = (JSONObject)o;
/*  62 */         String name = (String)criteriaObject.get("name");
/*  63 */         String type = (String)criteriaObject.get("dataType");
/*  64 */         Object values = criteriaObject.get("values");
/*     */         
/*  66 */         if (name != null)
/*     */         {
/*     */ 
/*  69 */           if (name.equals("Holds"))
/*     */           {
/*  71 */             JSONArray valueArray = new JSONArray();
/*  72 */             IndependentObject item = fetchOriginalItem(id, mimeType);
/*  73 */             IndependentObjectSet set = (IndependentObjectSet)item.getProperties().getObjectValue(name);
/*     */             
/*  75 */             Iterator it = set.iterator();
/*  76 */             while (it.hasNext()) {
/*  77 */               Link link = (Link)it.next();
/*  78 */               IndependentObject tail = link.get_Tail();
/*  79 */               String holdName = tail.getProperties().getStringValue("HoldName");
/*  80 */               if ((holdName != null) && (!holdName.isEmpty())) {
/*  81 */                 valueArray.add(holdName);
/*     */               }
/*     */             }
/*  84 */             criteriaObject.put("values", valueArray);
/*  85 */           } else if ((name.equals("RecordCategoryIdentifier")) || (name.equals("RecordFolderIdentifier")))
/*     */           {
/*  87 */             JSONObject cid = new JSONObject();
/*  88 */             cid.put("name", "ContainerId");
/*  89 */             cid.put("DataType", type);
/*  90 */             cid.put("values", criteriaObject.get("values"));
/*  91 */             criteriasToAdd.add(cid);
/*     */           }
/*     */           
/*     */ 
/*  95 */           if ((name.equals("RecordCategoryName")) || (name.equals("RecordFolderName")) || (name.equals("RecordCategoryIdentifier")) || (name.equals("RecordFolderIdentifier")))
/*     */           {
/*     */ 
/*     */ 
/*  99 */             if (isExternallyManaged) {
/* 100 */               criteriaObject.put("readOnly", Boolean.valueOf(true));
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 105 */           if ((name.equals("Reviewer")) && (values != null) && (!values.toString().isEmpty())) {
/* 106 */             Object val = UserUtil.retrieveUserDisplayName(values.toString(), this.baseService, this.baseService.getNexusRepositoryId());
/* 107 */             if (val != null) {
/* 108 */               criteriaObject.put("values", val);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 114 */       for (JSONObject criteriaToAdd : criteriasToAdd) {
/* 115 */         criteriaArray.add(criteriaToAdd);
/*     */       }
/*     */     } catch (Exception e) {
/*     */       JSONArray criteriaArray;
/* 119 */       Logger.logError(this, "filterServiceExecute", request, e);
/*     */     }
/*     */   }
/*     */   
/*     */   private IndependentObject fetchOriginalItem(String id, String mimeType) {
/* 124 */     if (this.originalItem == null) {
/* 125 */       if (mimeType.equals("folder"))
/*     */       {
/* 127 */         this.originalItem = Factory.Folder.fetchInstance(getBaseService().getPluginServiceCallbacks().getP8ObjectStore(getBaseService().getNexusRepositoryId()), id, null);
/*     */       } else {
/* 129 */         this.originalItem = Factory.Document.fetchInstance(getBaseService().getPluginServiceCallbacks().getP8ObjectStore(getBaseService().getNexusRepositoryId()), id, null);
/*     */       }
/*     */     }
/* 132 */     return this.originalItem;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\responseFilters\P8OpenItemOverride.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */