/*     */ package com.ibm.ier.plugin.util.model;
/*     */ 
/*     */ import com.ibm.ier.plugin.util.XPathUtil;
/*     */ 
/*     */ public class RecordEntryTemplate
/*     */ {
/*     */   public static final String TRUE = "1";
/*     */   private static final String INST_SHOW_RECORD_CLASS = "recordclassaccess";
/*     */   private static final String INST_SHOW_SELECT_FILEPLAN_LOCATION = "classificationlistaccess";
/*     */   private static final String INST_CONSTRAIN_FOLDER = "constrainfolder";
/*     */   private static final String INST_SHOW_RECORD_FILEPLAN_LOCATION_STEP = "selectfolder";
/*     */   private static final String INST_SHOW_PROPERTIES_STEP = "recordsproperties";
/*     */   private static final String INST_PRIMARY_FILEPLAN_LOCATION = "primaryclassification";
/*     */   public static final String ENTRY_TEMPLATE_DESCRIPTION = "EntryTemplateDescription";
/*     */   private String displayName;
/*     */   private String description;
/*     */   private Reference targetObjectStore;
/*     */   private boolean showSelectFilePlanLocationStep;
/*     */   private boolean showPropertiesStep;
/*     */   private Option showSelectFilePlanLocation;
/*     */   private Option showRecordClassSelection;
/*     */   private Reference folder;
/*     */   private String recordClassId;
/*     */   private String classId;
/*     */   private String classDisplayName;
/*     */   private boolean setProperties;
/*     */   private java.util.LinkedHashMap<String, PropertyOptions> propertiesOptions;
/*     */   private java.util.List<FolderLocation> filePlanLocations;
/*     */   private String startingFilePlanLocationId;
/*     */   private boolean constrainFolder;
/*     */   private String primaryFilePlanLocationId;
/*     */   private String entryTemplateType;
/*     */   
/*     */   public static enum TemplateType
/*     */   {
/*  36 */     DOCUMENT,  FOLDER,  CUSTOMOBJECT;
/*     */     
/*     */ 
/*     */ 
/*     */     private TemplateType() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public RecordEntryTemplate()
/*     */   {
/*  46 */     this.showSelectFilePlanLocationStep = true;
/*  47 */     this.showPropertiesStep = true;
/*  48 */     this.showSelectFilePlanLocation = null;
/*  49 */     this.showRecordClassSelection = null;
/*     */     
/*  51 */     this.recordClassId = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */     this.propertiesOptions = new java.util.LinkedHashMap();
/*  58 */     this.filePlanLocations = new java.util.ArrayList();
/*  59 */     this.startingFilePlanLocationId = null;
/*     */     
/*  61 */     this.primaryFilePlanLocationId = null;
/*     */     
/*  63 */     this.entryTemplateType = "DeclareRecords";
/*     */   }
/*     */   
/*  66 */   public String getDisplayName() { return this.displayName; }
/*     */   
/*     */   public String getPrimaryFilePlanLocationId()
/*     */   {
/*  70 */     return this.primaryFilePlanLocationId;
/*     */   }
/*     */   
/*     */   public String getStartingFilePlanLocationId() {
/*  74 */     return this.startingFilePlanLocationId;
/*     */   }
/*     */   
/*     */   public boolean getShowSelectFilePlanLocationStep() {
/*  78 */     return this.showSelectFilePlanLocationStep;
/*     */   }
/*     */   
/*     */   public boolean getShowPropertiesStep() {
/*  82 */     return this.showPropertiesStep;
/*     */   }
/*     */   
/*     */   public Option getShowSelectFilePlanLocation() {
/*  86 */     return this.showSelectFilePlanLocation;
/*     */   }
/*     */   
/*     */   public Option getShowRecordClassSelection() {
/*  90 */     return this.showRecordClassSelection;
/*     */   }
/*     */   
/*     */   public java.util.List<FolderLocation> getFilePlanLocations() {
/*  94 */     return this.filePlanLocations;
/*     */   }
/*     */   
/*     */   public String getEntryTemplateType() {
/*  98 */     return this.entryTemplateType;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/* 102 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 106 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 110 */     this.description = description;
/*     */   }
/*     */   
/*     */   public Reference getTargetObjectStore() {
/* 114 */     return this.targetObjectStore;
/*     */   }
/*     */   
/*     */   public void setTargetObjectStore(Reference objectStore) {
/* 118 */     this.targetObjectStore = objectStore;
/*     */   }
/*     */   
/*     */   private static boolean getBooleanValue(String value) {
/* 122 */     return !value.equals("0");
/*     */   }
/*     */   
/*     */   public void applyInstruction(Instruction instruction) {
/* 126 */     String value = instruction.getValue();
/* 127 */     String name = instruction.getName();
/* 128 */     boolean isHidden = false;
/* 129 */     boolean isReadOnly = false;
/*     */     
/* 131 */     if (name.equals("recordclassaccess")) {
/* 132 */       if (value.equals("Read only")) {
/* 133 */         isReadOnly = true;
/* 134 */       } else if (value.equals("Hide"))
/* 135 */         isHidden = true;
/* 136 */       this.showRecordClassSelection = new Option(isReadOnly, isHidden);
/*     */     }
/* 138 */     else if (name.equals("classificationlistaccess")) {
/* 139 */       if (value.equals("2")) {
/* 140 */         isReadOnly = true;
/* 141 */       } else if (value.equals("4"))
/* 142 */         isHidden = true;
/* 143 */       this.showSelectFilePlanLocation = new Option(isReadOnly, isHidden);
/*     */     }
/* 145 */     else if (name.equals("constrainfolder")) {
/* 146 */       this.constrainFolder = getBooleanValue(value);
/* 147 */     } else if (name.equals("primaryclassification")) {
/* 148 */       this.primaryFilePlanLocationId = value;
/* 149 */     } else if (name.equals("selectfolder")) {
/* 150 */       this.showSelectFilePlanLocationStep = getBooleanValue(value);
/* 151 */     } else if (name.equals("recordsproperties")) {
/* 152 */       this.showPropertiesStep = getBooleanValue(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isConstrainFolder() {
/* 157 */     return this.constrainFolder;
/*     */   }
/*     */   
/*     */   public void setIsConstrainFolder(boolean constrainFolder) {
/* 161 */     this.constrainFolder = constrainFolder;
/*     */   }
/*     */   
/*     */   public Reference getFolder() {
/* 165 */     return this.folder;
/*     */   }
/*     */   
/*     */   public void setFolder(Reference folder) {
/* 169 */     this.folder = folder;
/*     */   }
/*     */   
/*     */   public String getClassId() {
/* 173 */     return this.classId;
/*     */   }
/*     */   
/*     */   public void setClassId(String classId) {
/* 177 */     this.classId = classId;
/*     */   }
/*     */   
/*     */   public String getClassDisplayName() {
/* 181 */     return this.classDisplayName;
/*     */   }
/*     */   
/*     */   public void setClassDisplayName(String classDisplayName) {
/* 185 */     this.classDisplayName = classDisplayName;
/*     */   }
/*     */   
/*     */   public boolean isSetProperties() {
/* 189 */     return this.setProperties;
/*     */   }
/*     */   
/*     */   public void setIsSetProperties(boolean setProperties) {
/* 193 */     this.setProperties = setProperties;
/*     */   }
/*     */   
/*     */   public java.util.LinkedHashMap<String, PropertyOptions> getPropertiesOptions() {
/* 197 */     return this.propertiesOptions;
/*     */   }
/*     */   
/*     */   public void setPropertiesOptions(java.util.LinkedHashMap<String, PropertyOptions> propertiesOptions) {
/* 201 */     this.propertiesOptions = propertiesOptions;
/*     */   }
/*     */   
/*     */   public void addPropertyOptions(PropertyOptions propertyConfiguration) {
/* 205 */     this.propertiesOptions.put(propertyConfiguration.getId(), propertyConfiguration);
/*     */   }
/*     */   
/*     */   public void addFilePlanLocation(FolderLocation folderLocation) {
/* 209 */     this.filePlanLocations.add(folderLocation);
/*     */   }
/*     */   
/*     */   public static class Option {
/*     */     private boolean readOnly;
/*     */     private boolean hidden;
/*     */     
/*     */     public Option() {
/* 217 */       setOption(false, false);
/*     */     }
/*     */     
/*     */     public Option(boolean readOnly, boolean hidden) {
/* 221 */       setOption(readOnly, hidden);
/*     */     }
/*     */     
/*     */     public void setOption(boolean readOnly, boolean hidden) {
/* 225 */       this.readOnly = readOnly;
/* 226 */       this.hidden = hidden;
/*     */     }
/*     */     
/*     */     public boolean isReadOnly()
/*     */     {
/* 231 */       return this.readOnly;
/*     */     }
/*     */     
/*     */     public void setIsReadOnly(boolean readOnly) {
/* 235 */       this.readOnly = readOnly;
/*     */     }
/*     */     
/*     */     public boolean isHidden()
/*     */     {
/* 240 */       return this.hidden;
/*     */     }
/*     */     
/*     */     public void setIsHidden(boolean hidden) {
/* 244 */       this.hidden = hidden;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Instruction {
/*     */     private String name;
/*     */     private String value;
/*     */     
/*     */     public Instruction(String name, String value) {
/* 253 */       this.name = name;
/* 254 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 258 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/* 262 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 266 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(String value) {
/* 270 */       this.value = value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class PropertyOptions
/*     */   {
/*     */     private String id;
/*     */     
/*     */     private String name;
/*     */     
/*     */     private boolean required;
/*     */     private boolean readOnly;
/*     */     private boolean hidden;
/*     */     private com.ibm.jarm.api.constants.RMCardinality cardinality;
/*     */     private com.ibm.jarm.api.constants.DataType dataType;
/*     */     private Object defaultValue;
/*     */     private String requiredClassName;
/*     */     private String requiredClassDisplayName;
/*     */     private RecordEntryTemplate.Reference defaultObjectValue;
/*     */     private RecordEntryTemplate.Reference defaultObjectValueObjectStore;
/*     */     
/*     */     public com.ibm.jarm.api.constants.RMCardinality getCardinality()
/*     */     {
/* 294 */       return this.cardinality;
/*     */     }
/*     */     
/*     */     public String getCardinalityName() {
/* 298 */       return this.cardinality.toString();
/*     */     }
/*     */     
/*     */     public void setCardinality(com.ibm.jarm.api.constants.RMCardinality cardinality) {
/* 302 */       this.cardinality = cardinality;
/*     */     }
/*     */     
/*     */     public String getRequiredClassName() {
/* 306 */       return this.requiredClassName;
/*     */     }
/*     */     
/*     */     public String getRequiredClassDisplayName() {
/* 310 */       return this.requiredClassDisplayName;
/*     */     }
/*     */     
/*     */     public RecordEntryTemplate.Reference getDefaultObjectValue() {
/* 314 */       return this.defaultObjectValue;
/*     */     }
/*     */     
/*     */     public RecordEntryTemplate.Reference getDefaultObjectValueObjectStore() {
/* 318 */       return this.defaultObjectValueObjectStore;
/*     */     }
/*     */     
/*     */     public void setCardinalityName(String cardinalityName) {
/* 322 */       if (cardinalityName.equalsIgnoreCase(com.ibm.jarm.api.constants.RMCardinality.Single.toString())) {
/* 323 */         setCardinality(com.ibm.jarm.api.constants.RMCardinality.Single);
/*     */       }
/* 325 */       else if (cardinalityName.equalsIgnoreCase(com.ibm.jarm.api.constants.RMCardinality.Enumeration.toString())) {
/* 326 */         setCardinality(com.ibm.jarm.api.constants.RMCardinality.Enumeration);
/*     */       }
/* 328 */       else if (cardinalityName.equalsIgnoreCase(com.ibm.jarm.api.constants.RMCardinality.List.toString())) {
/* 329 */         setCardinality(com.ibm.jarm.api.constants.RMCardinality.List);
/*     */       }
/*     */       else {
/* 332 */         setCardinality(com.ibm.jarm.api.constants.RMCardinality.Single);
/*     */       }
/*     */     }
/*     */     
/*     */     public void setCardinalityValue(String cardinalityValue) {
/*     */       try {
/* 338 */         setCardinality(com.ibm.jarm.api.constants.RMCardinality.getInstanceFromInt(Integer.parseInt(cardinalityValue)));
/*     */       } catch (Exception e) {
/* 340 */         setCardinality(com.ibm.jarm.api.constants.RMCardinality.Single);
/*     */       }
/*     */     }
/*     */     
/*     */     public Object getDefaultValue() {
/* 345 */       return this.defaultValue;
/*     */     }
/*     */     
/*     */     public boolean isHidden() {
/* 349 */       return this.hidden;
/*     */     }
/*     */     
/*     */     public void setIsHidden(boolean hidden) {
/* 353 */       this.hidden = hidden;
/*     */     }
/*     */     
/*     */     public void setIsHiddenValue(String hidden) {
/* 357 */       setIsHidden("1".equals(hidden));
/*     */     }
/*     */     
/*     */     public String getId() {
/* 361 */       return this.id;
/*     */     }
/*     */     
/*     */     public void setId(String id) {
/* 365 */       this.id = id;
/*     */     }
/*     */     
/*     */     public boolean isReadOnly() {
/* 369 */       return this.readOnly;
/*     */     }
/*     */     
/*     */     public boolean isRequired() {
/* 373 */       return this.required;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 377 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/* 381 */       this.name = name;
/*     */     }
/*     */     
/*     */     public com.ibm.jarm.api.constants.DataType getDataType() {
/* 385 */       return this.dataType;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Reference
/*     */   {
/*     */     private String id;
/*     */     private String name;
/*     */     
/*     */     public Reference() {}
/*     */     
/*     */     public Reference(String id, String name)
/*     */     {
/* 398 */       this.id = id;
/* 399 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getId() {
/* 403 */       return this.id;
/*     */     }
/*     */     
/*     */     public void setId(String id) {
/* 407 */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 411 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/* 415 */       this.name = name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FolderLocation {
/*     */     private String id;
/*     */     private String objectStoreId;
/*     */     
/*     */     public FolderLocation(String id, String os) {
/* 424 */       this.id = id;
/* 425 */       this.objectStoreId = os;
/*     */     }
/*     */     
/*     */     public String getId() {
/* 429 */       return this.id;
/*     */     }
/*     */     
/*     */     public void setId(String id) {
/* 433 */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getObjectStoreId() {
/* 437 */       return this.objectStoreId;
/*     */     }
/*     */     
/*     */     public void setObjectStoreId(String id) {
/* 441 */       this.objectStoreId = id;
/*     */     }
/*     */   }
/*     */   
/*     */   public static RecordEntryTemplate loadEntryTemplate(javax.servlet.http.HttpServletRequest request, com.ibm.jarm.api.core.Repository repository, String templateId) throws Exception {
/* 446 */     String methodName = "loadEntryTemplate";
/* 447 */     com.ibm.ier.plugin.nls.Logger.logDebug(RecordEntryTemplate.class, methodName, request, "retrieving search template document");
/*     */     
/*     */ 
/* 450 */     com.ibm.jarm.api.core.ContentItem document = com.ibm.jarm.api.core.RMFactory.ContentItem.fetchInstance(repository, templateId, null);
/*     */     
/* 452 */     com.ibm.jarm.api.core.RMContentElement contentElement = (com.ibm.jarm.api.core.RMContentElement)document.getContentElements().get(0);
/* 453 */     java.io.InputStream contentStream = contentElement.getBinaryContentStream();
/*     */     
/* 455 */     RecordEntryTemplate entryTemplate = parse(request, contentStream);
/* 456 */     entryTemplate.setDisplayName(document.getName());
/* 457 */     entryTemplate.setDescription(document.getProperties().getStringValue("EntryTemplateDescription"));
/*     */     
/* 459 */     return entryTemplate;
/*     */   }
/*     */   
/*     */   static RecordEntryTemplate parse(javax.servlet.http.HttpServletRequest request, java.io.InputStream definition) throws Exception
/*     */   {
/* 464 */     String methodName = "parse";
/* 465 */     com.ibm.ier.plugin.nls.Logger.logEntry(RecordEntryTemplate.class, methodName, request);
/*     */     
/* 467 */     RecordEntryTemplate template = new RecordEntryTemplate();
/* 468 */     org.w3c.dom.Document doc = XPathUtil.loadDocument(definition);
/* 469 */     String recordClassId = XPathUtil.getXPathResult(doc, "/wcm:entrytemplate/wcm:classdesc/wcm:id");
/* 470 */     template.setRecordClassId(recordClassId);
/*     */     
/*     */ 
/* 473 */     String osId = XPathUtil.getXPathResult(doc, "/wcm:entrytemplate/wcm:objectstore/wcm:id");
/* 474 */     String osName = XPathUtil.getXPathResult(doc, "/wcm:entrytemplate/wcm:objectstore/wcm:name");
/* 475 */     Reference osRef = new Reference(osId, osName);
/* 476 */     template.setTargetObjectStore(osRef);
/*     */     
/*     */ 
/* 479 */     java.util.List<org.w3c.dom.Node> classifications = XPathUtil.selectNodes(doc, "/wcm:entrytemplate/wcm:classifications/wcm:classification");
/* 480 */     for (org.w3c.dom.Node classification : classifications) {
/* 481 */       String id = XPathUtil.getXPathResult(classification, "wcm:id");
/* 482 */       String os = XPathUtil.getXPathResult(classification, "wcm:objectstore");
/* 483 */       template.addFilePlanLocation(new FolderLocation(id, os));
/*     */     }
/*     */     
/*     */ 
/* 487 */     template.startingFilePlanLocationId = XPathUtil.getXPathResult(doc, "/wcm:entrytemplate/wcm:folder/wcm:id");
/*     */     
/*     */ 
/* 490 */     java.util.List<org.w3c.dom.Node> instructions = XPathUtil.selectNodes(doc, "/wcm:entrytemplate/wcm:instructions/wcm:instruction");
/* 491 */     for (org.w3c.dom.Node instruction : instructions) {
/* 492 */       String name = XPathUtil.getXPathResult(instruction, "wcm:name");
/* 493 */       String value = XPathUtil.getXPathResult(instruction, "wcm:value");
/* 494 */       template.applyInstruction(new Instruction(name, value));
/*     */     }
/*     */     
/*     */ 
/* 498 */     java.util.List<org.w3c.dom.Node> properties = XPathUtil.selectNodes(doc, "/wcm:entrytemplate/wcm:propdescs/wcm:propdesc");
/* 499 */     for (org.w3c.dom.Node property : properties) {
/* 500 */       PropertyOptions po = new PropertyOptions();
/* 501 */       po.id = XPathUtil.getXPathResult(property, "wcm:id");
/* 502 */       po.name = XPathUtil.getXPathResult(property, "wcm:symname");
/* 503 */       po.required = getBooleanValue(XPathUtil.getXPathResult(property, "wcm:isvalreq"));
/* 504 */       po.readOnly = getBooleanValue(XPathUtil.getXPathResult(property, "wcm:isreadonly"));
/* 505 */       po.hidden = getBooleanValue(XPathUtil.getXPathResult(property, "wcm:ishidden"));
/* 506 */       po.setCardinalityValue(XPathUtil.getXPathResult(property, "wcm:cardinality"));
/* 507 */       po.dataType = com.ibm.jarm.api.constants.DataType.getInstanceFromInt(Integer.parseInt(XPathUtil.getXPathResult(property, "wcm:datatype")));
/* 508 */       po.requiredClassName = XPathUtil.getXPathResult(property, "wcm:reqclass/wcm:symname");
/* 509 */       po.requiredClassDisplayName = XPathUtil.getXPathResult(property, "wcm:reqclass/wcm:dispname");
/*     */       
/*     */ 
/* 512 */       org.w3c.dom.Node propDefNode = XPathUtil.selectSingleNode(property, "wcm:propdef");
/* 513 */       if (propDefNode != null)
/*     */       {
/* 515 */         java.util.List<String> values = null;
/* 516 */         if ((po.cardinality == com.ibm.jarm.api.constants.RMCardinality.List) || (po.cardinality == com.ibm.jarm.api.constants.RMCardinality.Enumeration)) {
/* 517 */           java.util.List<org.w3c.dom.Node> defaultValueNodes = XPathUtil.selectNodes(propDefNode, "wcm:values/wcm:value");
/* 518 */           if ((defaultValueNodes != null) && (defaultValueNodes.size() > 0)) {
/* 519 */             values = new java.util.ArrayList();
/* 520 */             for (org.w3c.dom.Node defaultValueNode : defaultValueNodes) {
/* 521 */               String value = XPathUtil.getStringResult(defaultValueNode);
/* 522 */               if ((po.dataType == com.ibm.jarm.api.constants.DataType.Boolean) && (value != null))
/* 523 */                 value = String.valueOf(getBooleanValue(value));
/* 524 */               values.add(value);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 530 */         if (po.dataType == com.ibm.jarm.api.constants.DataType.Object) {
/* 531 */           String objId = XPathUtil.getXPathResult(propDefNode, "wcm:id");
/* 532 */           String objValue = XPathUtil.getXPathResult(propDefNode, "wcm:namevalue");
/* 533 */           String objStoreName = XPathUtil.getXPathResult(propDefNode, "wcm:objectstore/wcm:name");
/* 534 */           if ((objId != null) && (objValue != null)) {
/* 535 */             po.defaultObjectValue = new Reference(objId, objValue);
/*     */           }
/* 537 */           if (objStoreName != null) {
/* 538 */             po.defaultObjectValueObjectStore = new Reference(objStoreName, objStoreName);
/*     */           }
/*     */         }
/* 541 */         if (values == null) {
/* 542 */           String value = XPathUtil.getStringResult(propDefNode);
/* 543 */           if ((po.dataType == com.ibm.jarm.api.constants.DataType.Boolean) && (value != null)) {
/* 544 */             value = String.valueOf(getBooleanValue(value));
/*     */           } else {
/* 546 */             po.defaultValue = value;
/*     */           }
/*     */         } else {
/* 549 */           po.defaultValue = values;
/*     */         } }
/* 551 */       template.addPropertyOptions(po);
/*     */     }
/*     */     
/* 554 */     com.ibm.ier.plugin.nls.Logger.logExit(RecordEntryTemplate.class, methodName, request);
/*     */     
/* 556 */     return template;
/*     */   }
/*     */   
/*     */   public void setRecordClassId(String recordClassId) {
/* 560 */     this.recordClassId = recordClassId;
/*     */   }
/*     */   
/*     */   public String getRecordClassId() {
/* 564 */     return this.recordClassId;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\model\RecordEntryTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */