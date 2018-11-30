/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.ContentElementList;
/*     */ import com.filenet.api.constants.AutoClassify;
/*     */ import com.filenet.api.constants.CheckinType;
/*     */ import com.filenet.api.constants.RefreshMode;
/*     */ import com.filenet.api.constants.ReservationType;
/*     */ import com.filenet.api.core.ContentTransfer;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.property.FilterElement;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*     */ import com.ibm.jarm.api.core.ReportDefinition;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.query.ReportParameter;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class P8CE_ReportDefinitionImpl extends P8CE_ContentItemImpl implements ReportDefinition
/*     */ {
/*  52 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(com.ibm.jarm.api.util.JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private static final String[] MandatoryPropertyNames = { "DocumentTitle", "Id", "RMReportTitle", "Description", "RMEntityType", "ContentElements" };
/*     */   public static final String ID_REPORTDEFINITION_FOLDER = "{5E8189C9-C2E7-43BB-B5AF-EB5E69CAA543}";
/*     */   private static final List<FilterElement> MandatoryJaceFEs;
/*     */   
/*     */   static
/*     */   {
/*  70 */     String mandatoryNames = P8CE_Util.createSpaceSeparatedString(MandatoryPropertyNames);
/*     */     
/*  72 */     List<FilterElement> tempList = new ArrayList(1);
/*  73 */     tempList.add(new FilterElement(null, null, null, mandatoryNames, null));
/*  74 */     MandatoryJaceFEs = Collections.unmodifiableList(tempList);
/*     */   }
/*     */   
/*     */   static String[] getMandatoryPropertyNames()
/*     */   {
/*  79 */     return MandatoryPropertyNames;
/*     */   }
/*     */   
/*     */   static List<FilterElement> getMandatoryJaceFEs()
/*     */   {
/*  84 */     return MandatoryJaceFEs;
/*     */   }
/*     */   
/*     */   static IGenerator<ReportDefinition> getGenerator()
/*     */   {
/*  89 */     return new Generator();
/*     */   }
/*     */   
/*     */ 
/*  93 */   private ReportDefinitionParser parser = null;
/*  94 */   private String reportTitle = null;
/*  95 */   private String reportTitleLocKey = null;
/*  96 */   private String reportName = null;
/*  97 */   private String reportNameLocKey = null;
/*  98 */   private String reportDesp = null;
/*  99 */   private String reportDespLocKey = null;
/* 100 */   private String reportDBTableName = null;
/* 101 */   private String reportEntryHelp = null;
/* 102 */   private String reportEntryHelpLocKey = null;
/*     */   
/* 104 */   private List<ReportParameter> paraList = null;
/* 105 */   private Map<String, String[]> sqlList = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<FilterElement> getMandatoryFEs()
/*     */   {
/* 112 */     return getMandatoryJaceFEs();
/*     */   }
/*     */   
/*     */   P8CE_ReportDefinitionImpl(Repository repository, String identity, com.filenet.api.core.Document jaceDocument, boolean isPlaceholder)
/*     */   {
/* 117 */     super(EntityType.ReportDefinition, repository, identity, jaceDocument, isPlaceholder);
/* 118 */     Tracer.traceMethodEntry(new Object[] { repository, identity, jaceDocument, Boolean.valueOf(isPlaceholder) });
/*     */     
/* 120 */     if (!isPlaceholder) {
/* 121 */       initProperties();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 126 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   private void initProperties()
/*     */   {
/* 131 */     Tracer.traceMethodEntry(new Object[0]);
/* 132 */     com.filenet.api.core.Document doc = (com.filenet.api.core.Document)getJaceBaseObject();
/* 133 */     if (doc != null) {
/* 134 */       String reportTitle = P8CE_Util.getJacePropertyAsString(this, "RMReportTitle");
/* 135 */       setReportTitle(reportTitle);
/*     */       
/* 137 */       String desp = P8CE_Util.getJacePropertyAsString(this, "Description");
/* 138 */       setReportDescription(desp);
/*     */     }
/* 140 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */   private ReportDefinitionParser initParser()
/*     */   {
/* 145 */     Tracer.traceMethodEntry(new Object[0]);
/* 146 */     if (this.parser == null)
/*     */     {
/* 148 */       boolean establishedSubject = false;
/* 149 */       com.filenet.api.core.Document doc = (com.filenet.api.core.Document)getJaceBaseObject();
/* 150 */       InputStream is = null;
/*     */       try
/*     */       {
/* 153 */         establishedSubject = P8CE_Util.associateSubject();
/* 154 */         is = doc.accessContentStream(0);
/* 155 */         String docTitle = doc.getProperties().getStringValue("DocumentTitle");
/* 156 */         this.parser = new ReportDefinitionParser(docTitle);
/* 157 */         this.parser.createXMLDoc(is);
/* 158 */         Tracer.traceMethodExit(new Object[0]);
/*     */       }
/*     */       catch (EngineRuntimeException ex)
/*     */       {
/* 162 */         throw P8CE_Util.processJaceException(ex, RMErrorCode.RETRIEVE_REPORTDEFINTION_CONTENT_FAILED, new Object[] { getObjectIdentity() });
/*     */       }
/*     */       finally
/*     */       {
/* 166 */         if (is != null)
/*     */         {
/*     */           try
/*     */           {
/* 170 */             is.close();
/*     */           }
/*     */           catch (IOException ignored) {}
/*     */         }
/*     */         
/* 175 */         if (establishedSubject)
/* 176 */           P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/* 179 */     Tracer.traceMethodExit(new Object[] { this.parser });
/* 180 */     return this.parser;
/*     */   }
/*     */   
/*     */   public String getReportTitle() {
/* 184 */     return this.reportTitle;
/*     */   }
/*     */   
/* 187 */   public void setReportTitle(String title) { this.reportTitle = title; }
/*     */   
/*     */   public String getReportTitleLocalizationKey() {
/* 190 */     return this.reportTitleLocKey;
/*     */   }
/*     */   
/* 193 */   public void setReportTitleLocalizationKey(String key) { this.reportTitleLocKey = key; }
/*     */   
/*     */   public String getReportDescription() {
/* 196 */     return this.reportDesp;
/*     */   }
/*     */   
/* 199 */   public void setReportDescription(String desc) { this.reportDesp = desc; }
/*     */   
/*     */   public String getReportDescriptionLocalizationKey() {
/* 202 */     return this.reportDespLocKey;
/*     */   }
/*     */   
/* 205 */   public void setReportDescriptionLocalizationKey(String key) { this.reportDespLocKey = key; }
/*     */   
/*     */   public String getEntryHelp() {
/* 208 */     return this.reportEntryHelp;
/*     */   }
/*     */   
/* 211 */   public void setEntryHelp(String help) { this.reportEntryHelp = help; }
/*     */   
/*     */   public String getEntryHelpLocalizationKey() {
/* 214 */     return this.reportEntryHelpLocKey;
/*     */   }
/*     */   
/* 217 */   public void setEntryHelpLocalizationKey(String key) { this.reportEntryHelpLocKey = key; }
/*     */   
/*     */   public List<ReportParameter> getReportParameters() {
/* 220 */     return this.paraList;
/*     */   }
/*     */   
/* 223 */   public void setReportParameters(List<ReportParameter> params) { this.paraList = params; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getAssociatedEntityTypes()
/*     */   {
/* 235 */     List<String> queries = new ArrayList();
/* 236 */     queries.addAll(this.sqlList.keySet());
/* 237 */     return queries;
/*     */   }
/*     */   
/*     */   public String getReportName() {
/* 241 */     return this.reportName;
/*     */   }
/*     */   
/* 244 */   public void setReportName(String name) { this.reportName = name; }
/*     */   
/*     */   public String getReportDBTableName() {
/* 247 */     return this.reportDBTableName;
/*     */   }
/*     */   
/* 250 */   public void setReportDBTableName(String name) { this.reportDBTableName = name; }
/*     */   
/*     */   public String[] getReportQueriesByAssociatedEntityType(String type)
/*     */   {
/* 254 */     return (type != null) && (!type.isEmpty()) ? (String[])this.sqlList.get(type) : (String[])this.sqlList.get("Common");
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
/*     */   public void removeAllReportQueries()
/*     */   {
/* 267 */     this.sqlList.clear();
/*     */   }
/*     */   
/*     */   public void addReportQueries(String[] sql, String type) {
/* 271 */     if ((type == null) || (type.trim().length() == 0)) {
/* 272 */       this.sqlList.put("Common", sql);
/*     */     } else {
/* 274 */       this.sqlList.put(type, sql);
/*     */     }
/*     */   }
/*     */   
/*     */   public void parseContent() {
/* 279 */     ReportDefinitionParser parser = initParser();
/* 280 */     if (parser != null)
/*     */     {
/*     */ 
/* 283 */       setEntryHelp(parser.getReportIntro());
/* 284 */       setEntryHelpLocalizationKey(parser.getReportIntroLocalizationKey());
/* 285 */       setReportName(parser.getReportName());
/* 286 */       setReportDBTableName(parser.getReportDBTableName());
/* 287 */       setReportParameters(parser.getParameters());
/*     */       
/* 289 */       Map<String, String[]> queryMap = parser.getQueriesAndAssociatedEntityTypes();
/* 290 */       for (Map.Entry<String, String[]> entry : queryMap.entrySet()) {
/* 291 */         addReportQueries((String[])entry.getValue(), (String)entry.getKey());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReportNameLocalizationKey()
/*     */   {
/* 304 */     return this.reportNameLocKey;
/*     */   }
/*     */   
/*     */   public void setReportNameLocalizationKey(String key) {
/* 308 */     this.reportNameLocKey = key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void save(RMRefreshMode jarmRefreshMode)
/*     */   {
/* 315 */     Tracer.traceMethodEntry(new Object[] { jarmRefreshMode });
/* 316 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 319 */       establishedSubject = P8CE_Util.associateSubject();
/* 320 */       com.filenet.api.core.Document doc = (com.filenet.api.core.Document)getJaceBaseObject();
/* 321 */       com.filenet.api.core.Document reservation = null;
/*     */       
/* 323 */       if (!this.isPlaceholder) {
/* 324 */         if (!doc.get_IsReserved().booleanValue()) {
/* 325 */           doc.checkout(ReservationType.EXCLUSIVE, null, null, doc.getProperties());
/* 326 */           doc.save(RefreshMode.REFRESH);
/*     */         }
/* 328 */         reservation = (com.filenet.api.core.Document)doc.get_Reservation();
/* 329 */         reservation.set_Permissions(doc.get_Permissions());
/*     */       }
/*     */       
/* 332 */       reservation = reservation == null ? doc : reservation;
/*     */       
/*     */ 
/* 335 */       reservation.getProperties().putValue("DocumentTitle", getReportName());
/* 336 */       reservation.getProperties().putValue("RMReportTitle", getReportTitle());
/*     */       
/* 338 */       reservation.getProperties().putValue("Description", getReportDescription());
/*     */       
/* 340 */       if (this.isPlaceholder) {
/* 341 */         reservation.set_MimeType("application/x-filenet-reportdefinition");
/*     */       }
/* 343 */       ContentElementList contentList = com.filenet.api.core.Factory.ContentElement.createList();
/* 344 */       ContentTransfer tempContent = com.filenet.api.core.Factory.ContentTransfer.createInstance();
/* 345 */       tempContent.set_ContentType("application/x-filenet-reportdefinition");
/*     */       
/* 347 */       ReportDefinitionSerializer serializer = new ReportDefinitionSerializer(null);
/* 348 */       tempContent.setCaptureSource(new java.io.ByteArrayInputStream(serializer.serialize(this).getBytes("UTF-8")));
/* 349 */       contentList.add(tempContent);
/* 350 */       reservation.set_ContentElements(contentList);
/*     */       
/*     */ 
/* 353 */       reservation.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
/* 354 */       reservation.save(jarmRefreshMode == RMRefreshMode.Refresh ? RefreshMode.REFRESH : RefreshMode.NO_REFRESH);
/*     */       
/* 356 */       this.isCreationPending = false;
/* 357 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 361 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 365 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_SAVE_OPERATION_FAILED, new Object[] { getObjectIdentity(), EntityType.ReportDefinition });
/*     */     }
/*     */     finally
/*     */     {
/* 369 */       if (establishedSubject) {
/* 370 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class ReportDefinitionComparator
/*     */     implements java.util.Comparator<ReportDefinition>
/*     */   {
/*     */     public int compare(ReportDefinition o1, ReportDefinition o2)
/*     */     {
/* 460 */       if ((o1.getReportTitle() == null) && (o2.getReportTitle() != null))
/*     */       {
/* 462 */         return -1;
/*     */       }
/* 464 */       if ((o1.getReportTitle() != null) && (o2.getReportTitle() == null))
/*     */       {
/* 466 */         return 1;
/*     */       }
/*     */       
/* 469 */       if ((o1.getReportTitle() != null) && (o2.getReportTitle() != null))
/*     */       {
/* 471 */         if (o1.getReportTitle().compareTo(o2.getReportTitle()) < 0)
/*     */         {
/* 473 */           return -1;
/*     */         }
/* 475 */         if (o1.getReportTitle().compareTo(o2.getReportTitle()) > 0)
/*     */         {
/* 477 */           return 1;
/*     */         }
/*     */       }
/*     */       
/* 481 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Generator implements IGenerator<ReportDefinition>
/*     */   {
/*     */     public ReportDefinition create(Repository repository, Object jaceBaseObject)
/*     */     {
/* 489 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[] { repository, jaceBaseObject });
/* 490 */       com.filenet.api.core.Document jaceDoc = (com.filenet.api.core.Document)jaceBaseObject;
/*     */       
/* 492 */       String identity = jaceDoc.get_Id().toString();
/* 493 */       ReportDefinition def = new P8CE_ReportDefinitionImpl(repository, identity, jaceDoc, false);
/* 494 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[] { def });
/* 495 */       return def;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ReportDefinitionParser
/*     */   {
/*     */     private static final String REPORT_TITLE_PATH = "/object/setting[@key='title']";
/*     */     
/*     */     private static final String REPORT_DESC_PATH = "/object/setting[@key='desc']";
/*     */     
/*     */     private static final String REPORT_NAME_PATH = "/object/setting[@key='reportName']";
/*     */     
/*     */     private static final String REPORT_TITLE_LOCALKEY = "/object/setting[@key='title']/@localizationKey";
/*     */     
/*     */     private static final String REPORT_DESC_LOCALKEY = "/object/setting[@key='desc']/@localizationKey";
/*     */     
/*     */     private static final String REPORT_HELP_PATH = "/object/setting[@key='entryhelp']";
/*     */     private static final String REPORT_HELP_LOCALKEY = "/object/setting[@key='entryhelp']/@localizationKey";
/*     */     private static final String REPORT_PARAMETERS = "//object[@key='property']";
/*     */     private static final String REPORT_SQL = "//setting[@key='sql']";
/*     */     private static final String REPORT_QUERY = "//object[@key='query']";
/*     */     private static final String REPORT_DB_TABLE_NAME = "/object/setting[@key='db_table_name']";
/* 518 */     private org.w3c.dom.Document xmlDomDoc = null;
/* 519 */     private XPath xpath = null;
/*     */     private String sourceIdentity;
/*     */     
/*     */     ReportDefinitionParser(String sourceIdentity)
/*     */     {
/* 524 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[] { sourceIdentity });
/* 525 */       this.sourceIdentity = sourceIdentity;
/* 526 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     
/*     */     void createXMLDoc(InputStream is)
/*     */     {
/* 531 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[] { is });
/*     */       
/*     */       try
/*     */       {
/* 535 */         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 536 */         this.xmlDomDoc = builder.parse(is);
/* 537 */         this.xpath = XPathFactory.newInstance().newXPath();
/* 538 */         P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[0]);
/*     */       }
/*     */       catch (ParserConfigurationException e)
/*     */       {
/* 542 */         throw P8CE_Util.processJaceException(e, RMErrorCode.XMLPARSER_CONFIGURATION_ERR, new Object[0]);
/*     */       }
/*     */       catch (SAXException e)
/*     */       {
/* 546 */         throw P8CE_Util.processJaceException(e, RMErrorCode.REPORTDEFINTION_XMLFORMAT_ERR, new Object[] { this.sourceIdentity });
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 550 */         throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { this.sourceIdentity });
/*     */       }
/*     */     }
/*     */     
/*     */     private NodeList getNodeSet(String xPathExp, Node parent)
/*     */     {
/* 556 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[] { xPathExp, parent });
/* 557 */       NodeList nodeset = null;
/*     */       try
/*     */       {
/* 560 */         nodeset = (NodeList)this.xpath.evaluate(xPathExp, parent, XPathConstants.NODESET);
/*     */       }
/*     */       catch (XPathExpressionException ignored) {}
/*     */       
/* 564 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[] { nodeset });
/* 565 */       return nodeset;
/*     */     }
/*     */     
/*     */     private String getStringValue(String xPathExp, Node parent)
/*     */     {
/* 570 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[] { xPathExp, parent });
/* 571 */       String val = null;
/*     */       try
/*     */       {
/* 574 */         val = (String)this.xpath.evaluate(xPathExp, parent, XPathConstants.STRING);
/*     */       }
/*     */       catch (XPathExpressionException ignored)
/*     */       {
/* 578 */         val = null;
/*     */       }
/*     */       
/* 581 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[] { val });
/* 582 */       return val;
/*     */     }
/*     */     
/*     */     String getReportTitle() {
/* 586 */       return getStringValue("/object/setting[@key='title']", this.xmlDomDoc);
/*     */     }
/*     */     
/* 589 */     String getReportDescription() { return getStringValue("/object/setting[@key='desc']", this.xmlDomDoc); }
/*     */     
/*     */     String getReportIntro() {
/* 592 */       return getStringValue("/object/setting[@key='entryhelp']", this.xmlDomDoc);
/*     */     }
/*     */     
/* 595 */     String getReportName() { return getStringValue("/object/setting[@key='reportName']", this.xmlDomDoc); }
/*     */     
/*     */     String getReportTitleLocalizationKey() {
/* 598 */       return getStringValue("/object/setting[@key='title']/@localizationKey", this.xmlDomDoc);
/*     */     }
/*     */     
/* 601 */     String getReportDescriptionLocalizationKey() { return getStringValue("/object/setting[@key='desc']/@localizationKey", this.xmlDomDoc); }
/*     */     
/*     */     String getReportIntroLocalizationKey() {
/* 604 */       return getStringValue("/object/setting[@key='entryhelp']/@localizationKey", this.xmlDomDoc);
/*     */     }
/*     */     
/* 607 */     String getReportDBTableName() { return getStringValue("/object/setting[@key='db_table_name']", this.xmlDomDoc); }
/*     */     
/*     */ 
/*     */     List<String> getQueries()
/*     */     {
/* 612 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[0]);
/* 613 */       NodeList nodeList = getNodeSet("//setting[@key='sql']", this.xmlDomDoc);
/* 614 */       List<String> sqlList = new ArrayList();
/* 615 */       if (nodeList != null) {
/* 616 */         for (int idx = 0; idx < nodeList.getLength(); idx++)
/*     */         {
/* 618 */           Node nd = nodeList.item(idx);
/* 619 */           sqlList.add(nd.getTextContent());
/*     */         }
/*     */       }
/*     */       
/* 623 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[] { sqlList });
/* 624 */       return sqlList;
/*     */     }
/*     */     
/*     */     List<ReportParameter> getParameters()
/*     */     {
/* 629 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[0]);
/* 630 */       List<ReportParameter> paraList = new ArrayList();
/* 631 */       NodeList nodeList = getNodeSet("//object[@key='property']", this.xmlDomDoc);
/* 632 */       if (nodeList != null)
/*     */       {
/* 634 */         for (int idx = 0; idx < nodeList.getLength(); idx++)
/*     */         {
/* 636 */           Node nd = nodeList.item(idx);
/*     */           
/* 638 */           String synname = getStringValue("./setting[@key='symbolicName']", nd);
/* 639 */           if (synname != null)
/*     */           {
/* 641 */             String isRequired = getStringValue("./setting[@key='required']", nd);
/* 642 */             boolean bRequired = (isRequired != null) && (!isRequired.equals("0"));
/* 643 */             ReportParameter rp = new ReportParameter(synname, bRequired);
/*     */             
/* 645 */             NodeList valueList = getNodeSet("./list[@key='values']/object[@key='value']", nd);
/* 646 */             if (valueList != null)
/*     */             {
/* 648 */               for (int idx2 = 0; idx2 < valueList.getLength(); idx2++)
/*     */               {
/* 650 */                 Node valueNd = valueList.item(idx2);
/* 651 */                 String id = getStringValue("./setting[@key='id']", valueNd);
/* 652 */                 String value = getStringValue("./setting[@key='value']", valueNd);
/* 653 */                 String localeKey = getStringValue("./setting[@key='value']/@localizationKey", valueNd);
/*     */                 
/* 655 */                 rp.addValues(id, value, localeKey);
/*     */               }
/*     */             }
/*     */             
/* 659 */             paraList.add(rp);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 664 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[] { paraList });
/* 665 */       return paraList;
/*     */     }
/*     */     
/*     */     Map<String, String[]> getQueriesAndAssociatedEntityTypes()
/*     */     {
/* 670 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[0]);
/* 671 */       Map<String, String[]> result = new HashMap();
/* 672 */       NodeList nodeList = getNodeSet("//object[@key='query']", this.xmlDomDoc);
/* 673 */       if (nodeList != null)
/*     */       {
/* 675 */         for (int idx = 0; idx < nodeList.getLength(); idx++)
/*     */         {
/* 677 */           Node nd = nodeList.item(idx);
/*     */           
/* 679 */           String sqlText = getStringValue("./setting[@key='sql']", nd);
/* 680 */           String entity = getStringValue("./setting[@key='rm_entity_type']", nd);
/* 681 */           if ((sqlText != null) && (sqlText.trim().length() > 0))
/*     */           {
/* 683 */             if ((entity == null) || (entity.trim().length() == 0)) {
/* 684 */               entity = "Common";
/*     */             }
/* 686 */             String[] existingSQLs = (String[])result.get(entity);
/* 687 */             result.put(entity, addToStringArray(existingSQLs, sqlText));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 692 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[0]);
/* 693 */       return result;
/*     */     }
/*     */     
/*     */     private String[] addToStringArray(String[] oldArray, String newStr)
/*     */     {
/* 698 */       if (oldArray == null) {
/* 699 */         return new String[] { newStr };
/*     */       }
/* 701 */       int i = oldArray.length;
/* 702 */       String[] newArray = new String[i + 1];
/* 703 */       for (int cnt = 0; cnt < i; cnt++) {
/* 704 */         newArray[cnt] = oldArray[cnt];
/*     */       }
/* 706 */       newArray[i] = newStr;
/* 707 */       return newArray;
/*     */     }
/*     */     
/*     */     String getReportQueryAssociatedEntityType(String sql)
/*     */     {
/* 712 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodEntry(new Object[] { sql });
/* 713 */       String result = null;
/* 714 */       if ((sql != null) && (sql.length() > 0))
/*     */       {
/* 716 */         NodeList nodeList = getNodeSet("//object[@key='query']", this.xmlDomDoc);
/* 717 */         if (nodeList != null)
/*     */         {
/* 719 */           for (int idx = 0; idx < nodeList.getLength(); idx++)
/*     */           {
/* 721 */             Node nd = nodeList.item(idx);
/*     */             
/* 723 */             String sqlText = getStringValue("./setting[@key='sql']", nd);
/* 724 */             if ((sqlText != null) && (sqlText.equals(sql)))
/*     */             {
/* 726 */               result = getStringValue("./setting[@key='rm_entity_type']", nd);
/* 727 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 733 */       P8CE_ReportDefinitionImpl.Tracer.traceMethodExit(new Object[] { result });
/* 734 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ReportDefinitionSerializer
/*     */   {
/*     */     private org.w3c.dom.Document createXMLDoc(ReportDefinition rd) throws ParserConfigurationException
/*     */     {
/* 742 */       Map<String, String> attributes = new HashMap();
/*     */       
/* 744 */       String repId = Id.createId().toString();
/*     */       
/* 746 */       DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 747 */       org.w3c.dom.Document doc = docBuilder.newDocument();
/*     */       
/*     */ 
/* 750 */       attributes.put("key", "reportDefinition");
/* 751 */       Element rootElement = createElement(doc, "object", attributes, null);
/* 752 */       doc.appendChild(rootElement);
/*     */       
/*     */ 
/* 755 */       attributes.clear();
/* 756 */       attributes.put("key", "id");
/* 757 */       rootElement.appendChild(createElement(doc, "setting", attributes, repId));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 777 */       attributes.clear();
/* 778 */       attributes.put("key", "entryhelp");
/* 779 */       String helpkey = rd.getEntryHelpLocalizationKey();
/* 780 */       attributes.put("localizationKey", "server.report_entryhelp." + repId);
/* 781 */       rootElement.appendChild(createElement(doc, "setting", attributes, rd.getEntryHelp()));
/*     */       
/*     */ 
/* 784 */       attributes.clear();
/* 785 */       attributes.put("key", "reportName");
/* 786 */       attributes.put("localizationKey", "server.report_name." + repId);
/* 787 */       rootElement.appendChild(createElement(doc, "setting", attributes, rd.getReportName()));
/*     */       
/*     */ 
/* 790 */       attributes.clear();
/* 791 */       attributes.put("key", "db_table_name");
/* 792 */       rootElement.appendChild(createElement(doc, "setting", attributes, rd.getReportDBTableName()));
/*     */       
/*     */ 
/* 795 */       attributes.clear();
/* 796 */       attributes.put("key", "report parameters");
/* 797 */       Element paraListEle = createElement(doc, "list", attributes, null);
/* 798 */       rootElement.appendChild(paraListEle);
/* 799 */       createParametersList(doc, rd, paraListEle);
/*     */       
/*     */ 
/* 802 */       attributes.clear();
/* 803 */       attributes.put("key", "report queries");
/* 804 */       Element qListEle = createElement(doc, "list", attributes, null);
/* 805 */       rootElement.appendChild(qListEle);
/* 806 */       createQueriesList(doc, rd, qListEle);
/*     */       
/* 808 */       return doc;
/*     */     }
/*     */     
/*     */     private void createQueriesList(org.w3c.dom.Document doc, ReportDefinition rd, Element qListEle)
/*     */     {
/* 813 */       List<String> qList = rd.getAssociatedEntityTypes();
/* 814 */       if ((qList == null) || (qList.size() == 0)) return;
/* 815 */       Map<String, String> attributes = new HashMap();
/* 816 */       for (String entityType : qList)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 821 */         String[] queries = rd.getReportQueriesByAssociatedEntityType(entityType);
/* 822 */         if (queries != null) {
/* 823 */           for (String query : queries)
/*     */           {
/* 825 */             attributes.clear();
/* 826 */             attributes.put("key", "query");
/* 827 */             Element objEle = createElement(doc, "object", attributes, null);
/* 828 */             qListEle.appendChild(objEle);
/*     */             
/* 830 */             attributes.clear();
/* 831 */             attributes.put("key", "sql");
/* 832 */             objEle.appendChild(createElement(doc, "setting", attributes, query));
/*     */             
/* 834 */             attributes.clear();
/* 835 */             if ((entityType != null) && (!entityType.equals("Common"))) {
/* 836 */               attributes.put("key", "rm_entity_type");
/* 837 */               objEle.appendChild(createElement(doc, "setting", attributes, entityType));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void createParametersList(org.w3c.dom.Document doc, ReportDefinition rd, Element paraListEle)
/*     */     {
/* 846 */       List<ReportParameter> pList = rd.getReportParameters();
/* 847 */       if ((pList == null) || (pList.size() == 0)) { return;
/*     */       }
/* 849 */       Map<String, String> attributes = new HashMap();
/* 850 */       for (Iterator i$ = pList.iterator(); i$.hasNext();) { rPara = (ReportParameter)i$.next();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 855 */         attributes.clear();
/* 856 */         attributes.put("key", "property");
/* 857 */         Element objEle = createElement(doc, "object", attributes, null);
/*     */         
/* 859 */         attributes.clear();
/* 860 */         attributes.put("key", "symbolicName");
/* 861 */         objEle.appendChild(createElement(doc, "setting", attributes, rPara.getName()));
/*     */         
/* 863 */         attributes.clear();
/* 864 */         attributes.put("key", "required");
/* 865 */         objEle.appendChild(createElement(doc, "setting", attributes, rPara.isRequired() ? "1" : "0"));
/*     */         
/* 867 */         paraListEle.appendChild(objEle);
/*     */         
/*     */ 
/* 870 */         List<String> valueIds = rPara.getAvailValueIds();
/* 871 */         if ((valueIds != null) && (valueIds.size() > 0))
/*     */         {
/* 873 */           attributes.clear();
/* 874 */           attributes.put("key", "values");
/* 875 */           valuesNode = createElement(doc, "list", attributes, null);
/* 876 */           objEle.appendChild(valuesNode);
/* 877 */           for (String valueId : valueIds)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 882 */             attributes.clear();
/* 883 */             attributes.put("key", "value");
/* 884 */             Element valueNode = createElement(doc, "object", attributes, null);
/* 885 */             valuesNode.appendChild(valueNode);
/*     */             
/* 887 */             attributes.clear();
/* 888 */             attributes.put("key", "id");
/* 889 */             valueNode.appendChild(createElement(doc, "setting", attributes, valueId));
/*     */             
/* 891 */             attributes.clear();
/* 892 */             attributes.put("key", "value");
/* 893 */             String locKey = "server.report_1." + valueId;
/* 894 */             attributes.put("localizationKey", locKey);
/* 895 */             valueNode.appendChild(createElement(doc, "setting", attributes, rPara.getAvailValue(valueId)));
/*     */           }
/*     */         }
/*     */       }
/*     */       ReportParameter rPara;
/*     */       Element valuesNode;
/*     */     }
/*     */     
/*     */     private Element createElement(org.w3c.dom.Document doc, String nodeName, Map<String, String> attributes, String text) {
/* 904 */       Element ele = doc.createElement(nodeName);
/* 905 */       if ((attributes != null) && (attributes.size() > 0)) {
/* 906 */         for (Map.Entry<String, String> entry : attributes.entrySet()) {
/* 907 */           Attr attr = doc.createAttribute((String)entry.getKey());
/* 908 */           attr.setValue((String)entry.getValue());
/* 909 */           ele.setAttributeNode(attr);
/*     */         }
/*     */       }
/* 912 */       if ((text != null) && (text.length() > 0)) {
/* 913 */         ele.appendChild(doc.createTextNode(text));
/*     */       }
/* 915 */       return ele;
/*     */     }
/*     */     
/*     */     public String serialize(ReportDefinition rd) {
/* 919 */       Transformer transformer = null;
/* 920 */       StringWriter stw = new StringWriter();
/*     */       try
/*     */       {
/* 923 */         org.w3c.dom.Document doc = createXMLDoc(rd);
/*     */         
/* 925 */         if (doc != null) {
/* 926 */           transformer = TransformerFactory.newInstance().newTransformer();
/* 927 */           transformer.transform(new DOMSource(doc), new StreamResult(stw));
/*     */         }
/*     */       } catch (ParserConfigurationException pce) {
/* 930 */         pce.printStackTrace();
/*     */       }
/*     */       catch (TransformerConfigurationException e) {
/* 933 */         e.printStackTrace();
/*     */       }
/*     */       catch (TransformerException e) {
/* 936 */         e.printStackTrace();
/*     */       }
/*     */       catch (Exception e) {}
/*     */       
/*     */ 
/* 941 */       return stw.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_ReportDefinitionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */