/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SearchCriterion
/*     */   extends SearchCriteria
/*     */ {
/*     */   private static final long serialVersionUID = 978511320910079716L;
/*     */   private String[] allowedOperators;
/*  30 */   private String operator = "EQUAL";
/*  31 */   private String dataType = "xs:string";
/*  32 */   private String cardinality = "SINGLE";
/*     */   
/*     */   private String[] values;
/*     */   
/*     */   private String[] displayValues;
/*     */   
/*     */   private boolean required;
/*     */   
/*     */   private boolean readOnly;
/*     */   
/*     */   private boolean hidden;
/*     */   private String itemId;
/*     */   
/*     */   public String getOperator()
/*     */   {
/*  47 */     return this.operator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOperator(String operator)
/*     */   {
/*  55 */     this.operator = operator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getValues()
/*     */   {
/*  62 */     return this.values;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValues(String[] values)
/*     */   {
/*  70 */     this.values = values;
/*     */   }
/*     */   
/*     */   public String[] getDisplayValues() {
/*  74 */     return this.displayValues;
/*     */   }
/*     */   
/*     */   public void setDisplayValues(String[] displayValues) {
/*  78 */     this.displayValues = displayValues;
/*     */   }
/*     */   
/*     */   public String[] getAllowedOperators() {
/*  82 */     return this.allowedOperators;
/*     */   }
/*     */   
/*     */   public void setAllowedOperators(String[] allowedOperators) {
/*  86 */     this.allowedOperators = allowedOperators;
/*     */   }
/*     */   
/*     */   public String getDataType() {
/*  90 */     return this.dataType;
/*     */   }
/*     */   
/*     */   public void setDataType(String dataType) {
/*  94 */     this.dataType = dataType;
/*     */   }
/*     */   
/*     */   public String getCardinality() {
/*  98 */     return this.cardinality;
/*     */   }
/*     */   
/*     */   public void setCardinality(String cardinality) {
/* 102 */     this.cardinality = cardinality;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly() {
/* 106 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public void setReadOnly(boolean readOnly) {
/* 110 */     this.readOnly = readOnly;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 114 */     return this.required;
/*     */   }
/*     */   
/*     */   public void setRequired(boolean required) {
/* 118 */     this.required = required;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/* 122 */     return this.hidden;
/*     */   }
/*     */   
/*     */   public void setHidden(boolean hidden) {
/* 126 */     this.hidden = hidden;
/*     */   }
/*     */   
/*     */   public void setItemId(String itemId) {
/* 130 */     this.itemId = itemId;
/*     */   }
/*     */   
/*     */   public String getItemId() {
/* 134 */     return this.itemId;
/*     */   }
/*     */   
/*     */   public void fromJSON(JSONObject json)
/*     */   {
/* 139 */     if (json != null) {
/* 140 */       if (json.containsKey("dataType"))
/* 141 */         this.dataType = ((String)json.get("dataType"));
/* 142 */       if (json.containsKey("id"))
/* 143 */         this.name = ((String)json.get("id"));
/* 144 */       if (json.containsKey("name"))
/* 145 */         this.displayName = ((String)json.get("name"));
/* 146 */       if (json.containsKey("selectedOperator"))
/* 147 */         this.operator = ((String)json.get("selectedOperator"));
/* 148 */       if (json.containsKey("dataType"))
/* 149 */         this.dataType = ((String)json.get("dataType"));
/* 150 */       if (json.containsKey("cardinality"))
/* 151 */         this.cardinality = ((String)json.get("cardinality"));
/* 152 */       if (json.containsKey("itemId")) {
/* 153 */         this.itemId = ((String)json.get("itemId"));
/*     */       }
/* 155 */       if (json.containsKey("required")) {
/*     */         try {
/* 157 */           this.required = Boolean.parseBoolean((String)json.get("required"));
/*     */         } catch (Exception e) {
/* 159 */           this.required = false;
/*     */         }
/*     */       }
/*     */       
/* 163 */       if (json.containsKey("readOnly")) {
/*     */         try {
/* 165 */           this.readOnly = Boolean.parseBoolean((String)json.get("readOnly"));
/*     */         } catch (Exception e) {
/* 167 */           this.readOnly = false;
/*     */         }
/*     */       }
/*     */       
/* 171 */       if (json.containsKey("hidden")) {
/*     */         try {
/* 173 */           this.hidden = Boolean.parseBoolean((String)json.get("hidden"));
/*     */         } catch (Exception e) {
/* 175 */           this.hidden = false;
/*     */         }
/*     */       }
/*     */       
/* 179 */       if (json.containsKey("values")) {
/* 180 */         JSONArray criteriaJSONArray = (JSONArray)json.get("values");
/* 181 */         this.values = new String[criteriaJSONArray.size()];
/* 182 */         criteriaJSONArray.toArray(this.values);
/*     */       }
/*     */       
/* 185 */       if (json.containsKey("allowedOperators")) {
/* 186 */         JSONArray allowedOpsJSONArray = (JSONArray)json.get("allowedOperators");
/* 187 */         this.allowedOperators = new String[allowedOpsJSONArray.size()];
/* 188 */         allowedOpsJSONArray.toArray(this.allowedOperators);
/*     */       }
/*     */       
/* 191 */       if (json.containsKey("displayValues")) {
/* 192 */         JSONArray dispValsJSONArray = (JSONArray)json.get("displayValues");
/* 193 */         this.displayValues = new String[dispValsJSONArray.size()];
/* 194 */         dispValsJSONArray.toArray(this.displayValues);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public JSONObject toJSON() {
/* 200 */     JSONObject json = new JSONObject();
/*     */     
/* 202 */     json.put("id", this.name);
/* 203 */     json.put("name", this.displayName);
/* 204 */     json.put("dataType", this.dataType);
/* 205 */     json.put("selectedOperator", this.operator);
/*     */     
/* 207 */     JSONArray valuesJSON = new JSONArray();
/* 208 */     if ((this.values != null) && (this.values.length > 0))
/* 209 */       valuesJSON.addAll(Arrays.asList(this.values));
/* 210 */     json.put("values", valuesJSON);
/*     */     
/* 212 */     JSONArray allowedOperatorsJSON = new JSONArray();
/* 213 */     if (this.allowedOperators != null)
/* 214 */       allowedOperatorsJSON.addAll(Arrays.asList(this.allowedOperators));
/* 215 */     json.put("allowedOperators", allowedOperatorsJSON);
/*     */     
/* 217 */     JSONArray displayValuesJSON = new JSONArray();
/* 218 */     if (this.displayValues != null)
/* 219 */       displayValuesJSON.addAll(Arrays.asList(this.displayValues));
/* 220 */     json.put("displayValues", displayValuesJSON);
/*     */     
/* 222 */     json.put("cardinality", this.cardinality);
/* 223 */     json.put("required", Boolean.valueOf(this.required));
/* 224 */     json.put("readOnly", Boolean.valueOf(this.readOnly));
/* 225 */     json.put("hidden", Boolean.valueOf(this.hidden));
/* 226 */     json.put("itemId", this.itemId);
/*     */     
/* 228 */     return json;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 233 */     StringBuilder str = new StringBuilder();
/* 234 */     str.append("(allowedOperators = ").append(Arrays.toString(this.allowedOperators));
/* 235 */     str.append(", operator = ").append(this.operator);
/* 236 */     str.append(", dataType = ").append(this.dataType);
/* 237 */     str.append(", cardinality = ").append(this.cardinality);
/* 238 */     str.append(", values = ").append(Arrays.toString(this.values));
/* 239 */     str.append(", displayValues = ").append(Arrays.toString(this.displayValues));
/* 240 */     str.append(", required = ").append(this.required);
/* 241 */     str.append(", readOnly = ").append(this.readOnly);
/* 242 */     str.append(", hidden = ").append(this.hidden);
/* 243 */     str.append(", itemId = ").append(this.itemId).append(")");
/*     */     
/* 245 */     return str.toString();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\SearchCriterion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */