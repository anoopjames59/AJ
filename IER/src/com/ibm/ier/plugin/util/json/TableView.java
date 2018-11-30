/*     */ package com.ibm.ier.plugin.util.json;
/*     */ 
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TableView
/*     */ {
/*     */   public class ColumnProperties
/*     */   {
/*  33 */     private Map properties = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */     ColumnProperties(Map properties)
/*     */     {
/*  39 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     public String getProperty(String key) {
/*  43 */       return (String)this.properties.get(key);
/*     */     }
/*     */     
/*     */     public JSONObject toJson() throws IOException
/*     */     {
/*  48 */       JSONObject jsonObject = new JSONObject();
/*     */       
/*  50 */       for (Iterator keyIterator = this.properties.keySet().iterator(); keyIterator.hasNext();) {
/*  51 */         String key = (String)keyIterator.next();
/*  52 */         Object value = this.properties.get(key);
/*  53 */         if ((value instanceof ArrayList)) {
/*  54 */           ArrayList values = (ArrayList)value;
/*  55 */           JSONArray array = new JSONArray();
/*  56 */           for (int i = 0; i < values.size(); i++) {
/*  57 */             array.add((String)values.get(i));
/*     */           }
/*  59 */           jsonObject.put(key, array);
/*     */         } else {
/*  61 */           jsonObject.put(key, value);
/*     */         }
/*     */       }
/*     */       
/*  65 */       return jsonObject;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString(HttpServletRequest request)
/*     */     {
/*  72 */       String methodName = "toString";
/*  73 */       String returnString = null;
/*     */       try {
/*  75 */         JSONObject jsonRepresentation = toJson();
/*  76 */         returnString = JSONUtils.prettyPrintJson(jsonRepresentation);
/*     */       } catch (IOException e) {
/*  78 */         returnString = "JSONException encountered during toString() operation. " + e.getMessage();
/*  79 */         Logger.logError(this, methodName, request, e);
/*     */       }
/*  81 */       return returnString;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class ColumnSet
/*     */   {
/*  92 */     private List columnPropList = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     ColumnSet() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public TableView.ColumnProperties addColumnProperty(Map propertyMap)
/*     */     {
/* 108 */       return addColumnProperty(propertyMap, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public TableView.ColumnProperties addColumnProperty(Map propertyMap, boolean allowDuplicates)
/*     */     {
/* 121 */       TableView.ColumnProperties props = new TableView.ColumnProperties(TableView.this, propertyMap);
/* 122 */       boolean columnFound = false;
/* 123 */       String newPropName; Iterator it; if (!allowDuplicates) {
/* 124 */         newPropName = props.getProperty("name");
/* 125 */         for (it = this.columnPropList.iterator(); it.hasNext();) {
/* 126 */           TableView.ColumnProperties oldProp = (TableView.ColumnProperties)it.next();
/* 127 */           String oldPropName = oldProp.getProperty("name");
/* 128 */           if ((oldPropName != null) && (oldPropName.equals(newPropName))) {
/* 129 */             columnFound = true;
/*     */           }
/*     */         }
/*     */       }
/* 133 */       if (!columnFound) {
/* 134 */         this.columnPropList.add(props);
/*     */       }
/* 136 */       return props;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public TableView.ColumnProperties addColumnProperty(String[][] properties)
/*     */     {
/* 146 */       return addColumnProperty(JSONUtils.arrayToMap(properties));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     List getColumnPropList()
/*     */     {
/* 156 */       return this.columnPropList;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int size()
/*     */     {
/* 165 */       return this.columnPropList.size();
/*     */     }
/*     */   }
/*     */   
/* 169 */   private List columnSets = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ColumnSet createColumnSet()
/*     */   {
/* 178 */     ColumnSet set = new ColumnSet();
/* 179 */     this.columnSets.add(set);
/* 180 */     return set;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONObject toJson()
/*     */     throws IOException
/*     */   {
/* 189 */     JSONObject jsonObject = new JSONObject();
/*     */     
/* 191 */     JSONArray columnsArray = new JSONArray();
/*     */     
/*     */ 
/* 194 */     for (Iterator iterator = this.columnSets.iterator(); iterator.hasNext();) {
/* 195 */       ColumnSet columnSet = (ColumnSet)iterator.next();
/*     */       
/* 197 */       List columnPropList = columnSet.getColumnPropList();
/* 198 */       JSONArray columnPropArray = new JSONArray();
/*     */       
/*     */ 
/* 201 */       for (Iterator propIterator = columnPropList.iterator(); propIterator.hasNext();) {
/* 202 */         ColumnProperties props = (ColumnProperties)propIterator.next();
/* 203 */         columnPropArray.add(props.toJson());
/*     */       }
/*     */       
/* 206 */       columnsArray.add(columnPropArray);
/*     */     }
/*     */     
/* 209 */     jsonObject.put("cells", columnsArray);
/* 210 */     return jsonObject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString(HttpServletRequest request)
/*     */   {
/* 217 */     String methodName = "toString";
/* 218 */     String returnString = null;
/*     */     try {
/* 220 */       JSONObject jsonRepresentation = toJson();
/* 221 */       returnString = JSONUtils.prettyPrintJson(jsonRepresentation);
/*     */     } catch (IOException e) {
/* 223 */       returnString = "JSONException encountered during toString() operation. " + e.getMessage();
/* 224 */       Logger.logError(this, methodName, request, e);
/*     */     }
/* 226 */     return returnString;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\json\TableView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */