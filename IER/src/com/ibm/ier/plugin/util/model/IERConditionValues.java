/*     */ package com.ibm.ier.plugin.util.model;
/*     */ 
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.core.Factory.MarkingSet;
/*     */ import com.filenet.api.security.MarkingSet;
/*     */ import com.ibm.jarm.api.constants.DomainType;
/*     */ import com.ibm.jarm.api.core.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMDomain;
/*     */ import com.ibm.jarm.api.core.RMFactory.DomainConnection;
/*     */ import com.ibm.jarm.api.core.RMFactory.RMDomain;
/*     */ import com.ibm.jarm.api.util.P8CE_Convert;
/*     */ import com.ibm.jarm.api.util.RMUserContext;
/*     */ import java.util.HashMap;
/*     */ import javax.security.auth.Subject;
/*     */ import org.apache.commons.collections.BidiMap;
/*     */ import org.apache.commons.collections.bidimap.TreeBidiMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IERConditionValues
/*     */ {
/*     */   public String[] propertiesNames;
/*     */   private String[][] propertiesValues;
/*     */   private String[] propertiesLabels;
/*     */   private String[] propertiesTypes;
/*     */   private String[] propertiesOpers;
/*     */   private String[] propertiesHasCVL;
/*     */   private String[] propertiesHasMarkingList;
/*     */   private String[] propertiesUsesLong;
/*     */   private String[] propertiesSelectable;
/*     */   public String propertiesJoinType;
/*     */   public String recordContentContainsValues;
/*     */   private String recordContentJoinType;
/*     */   private String recordContentSearchInType;
/*     */   public String aggregationType;
/*     */   private String sql;
/*     */   private String criteriaJson;
/*  51 */   public static final HashMap<String, String> typeMap = new HashMap();
/*     */   
/*  53 */   static { typeMap.put("xs:boolean", "2");
/*  54 */     typeMap.put("xs:date", "3");
/*  55 */     typeMap.put("xs:time", "3");
/*  56 */     typeMap.put("xs:timestamp", "3");
/*  57 */     typeMap.put("xs:decimal", "4");
/*  58 */     typeMap.put("xs:double", "4");
/*  59 */     typeMap.put("xs:guid", "5");
/*  60 */     typeMap.put("xs:short", "6");
/*  61 */     typeMap.put("xs:integer", "6");
/*  62 */     typeMap.put("xs:long", "6");
/*  63 */     typeMap.put("xs:string", "8");
/*     */     
/*     */ 
/*  66 */     reverseTypeMap = new HashMap();
/*     */     
/*  68 */     typeMap.put("2", "xs:boolean");
/*  69 */     typeMap.put("3", "xs:date");
/*  70 */     typeMap.put("3", "xs:time");
/*  71 */     typeMap.put("3", "xs:timestamp");
/*  72 */     typeMap.put("4", "xs:decimal");
/*  73 */     typeMap.put("4", "xs:double");
/*  74 */     typeMap.put("5", "xs:guid");
/*  75 */     typeMap.put("6", "xs:short");
/*  76 */     typeMap.put("6", "xs:integer");
/*  77 */     typeMap.put("6", "xs:long");
/*  78 */     typeMap.put("8", "xs:string"); }
/*     */   
/*     */   public static final HashMap<String, String> reverseTypeMap;
/*  81 */   public static final BidiMap operatorMap = new TreeBidiMap() {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAggregationType(String aggregation)
/*     */   {
/* 107 */     this.aggregationType = aggregation;
/*     */   }
/*     */   
/*     */   public String getAggegationType() {
/* 111 */     return this.aggregationType;
/*     */   }
/*     */   
/*     */   public void setSQL(String sql) {
/* 115 */     this.sql = sql;
/*     */   }
/*     */   
/*     */   public String getSQL() {
/* 119 */     return this.sql;
/*     */   }
/*     */   
/*     */   public void setRecordContentJoinType(String type) {
/* 123 */     this.recordContentJoinType = type;
/*     */   }
/*     */   
/*     */   public void setRecordContentSearchInType(String type) {
/* 127 */     this.recordContentSearchInType = type;
/*     */   }
/*     */   
/*     */   public String getPropertiesJoinType() {
/* 131 */     return this.propertiesJoinType;
/*     */   }
/*     */   
/*     */   public void setPropertiesJoinType(String type) {
/* 135 */     this.propertiesJoinType = type;
/*     */   }
/*     */   
/*     */   public String getRecordContentJoinType() {
/* 139 */     return this.recordContentJoinType;
/*     */   }
/*     */   
/*     */   public String getRecordContentSearchInType() {
/* 143 */     return this.recordContentSearchInType;
/*     */   }
/*     */   
/*     */   public String getRecordContentContainsValues() {
/* 147 */     return this.recordContentContainsValues;
/*     */   }
/*     */   
/*     */   public void setRecordContentContainsValues(String value) {
/* 151 */     this.recordContentContainsValues = value;
/*     */   }
/*     */   
/*     */   public String getOperator(int slot) {
/* 155 */     return this.propertiesOpers[slot];
/*     */   }
/*     */   
/*     */   public void setPropertiesSelectable(String[] props)
/*     */   {
/* 160 */     this.propertiesSelectable = props;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesSelectable()
/*     */   {
/* 165 */     return this.propertiesSelectable;
/*     */   }
/*     */   
/*     */   public void setPropertiesNames(String[] props)
/*     */   {
/* 170 */     this.propertiesNames = props;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesNames()
/*     */   {
/* 175 */     return this.propertiesNames;
/*     */   }
/*     */   
/*     */   public void setPropertiesUsesLong(String[] props)
/*     */   {
/* 180 */     this.propertiesUsesLong = props;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesUsesLong()
/*     */   {
/* 185 */     return this.propertiesUsesLong;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPropertiesLabels(String[] proplabels)
/*     */   {
/* 191 */     this.propertiesLabels = proplabels;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesLabels()
/*     */   {
/* 196 */     return this.propertiesLabels;
/*     */   }
/*     */   
/*     */   public void setPropertiesTypes(String[] proptypes)
/*     */   {
/* 201 */     this.propertiesTypes = proptypes;
/*     */   }
/*     */   
/*     */   public String getPropertiesType(int slot)
/*     */   {
/* 206 */     return this.propertiesTypes[slot];
/*     */   }
/*     */   
/*     */   public String[] getPropertiesTypes()
/*     */   {
/* 211 */     return this.propertiesTypes;
/*     */   }
/*     */   
/*     */   public void setPropertiesOpers(String[] propopers)
/*     */   {
/* 216 */     this.propertiesOpers = propopers;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesOpers()
/*     */   {
/* 221 */     return this.propertiesOpers;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesValues(int slot) {
/* 225 */     return this.propertiesValues[slot];
/*     */   }
/*     */   
/*     */   public String[][] getPropertiesValues() {
/* 229 */     return this.propertiesValues;
/*     */   }
/*     */   
/*     */   public void setPropertiesValues(String[][] values) {
/* 233 */     this.propertiesValues = values;
/*     */   }
/*     */   
/*     */   public void setPropertiesHasCVL(String[] propCVL)
/*     */   {
/* 238 */     this.propertiesHasCVL = propCVL;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesHasCVL()
/*     */   {
/* 243 */     return this.propertiesHasCVL;
/*     */   }
/*     */   
/*     */   public void setPropertiesHasMarkingList(String[] prophasmarkinglist)
/*     */   {
/* 248 */     this.propertiesHasMarkingList = prophasmarkinglist;
/*     */   }
/*     */   
/*     */   public String[] getPropertiesHasMarkingList()
/*     */   {
/* 253 */     return this.propertiesHasMarkingList;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 257 */     DomainConnection connection = RMFactory.DomainConnection.createInstance(DomainType.P8_CE, "http://cm-rm-win10.usca.ibm.com:9080/wsi/FNCEWS40MTOM", null);
/* 258 */     Subject subject = RMUserContext.createSubject(connection, "p8admin", "p8admin", "FileNetP8WSI");
/* 259 */     RMUserContext.get().setSubject(subject);
/* 260 */     RMDomain domain = RMFactory.RMDomain.fetchInstance(connection, null, null);
/*     */     
/*     */ 
/*     */ 
/* 264 */     MarkingSet ms = Factory.MarkingSet.createInstance(P8CE_Convert.fromJARM(domain));
/*     */     
/*     */ 
/*     */ 
/* 268 */     ms.set_DisplayName("NEW MS");
/* 269 */     ms.save(RefreshMode.REFRESH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCriteriaJson(String criteriaJson)
/*     */   {
/* 280 */     this.criteriaJson = criteriaJson;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getCriteriaJson()
/*     */   {
/* 287 */     return this.criteriaJson;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\model\IERConditionValues.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */