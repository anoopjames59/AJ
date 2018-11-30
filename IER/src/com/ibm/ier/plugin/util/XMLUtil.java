/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import java.io.FilterWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class XMLUtil
/*     */ {
/*  17 */   private static javax.xml.parsers.DocumentBuilderFactory documentBuilderFactory = ;
/*  18 */   static { documentBuilderFactory.setNamespaceAware(true);
/*  19 */     documentBuilderFactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE);
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
/*     */   public static void writePrettyXML(Writer w, Node node)
/*     */   {
/*  39 */     writeXML(new PrettyFilter(w), node);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeXML(Writer w, Node node)
/*     */   {
/*  50 */     if (node.getNodeType() == 9)
/*  51 */       node = ((Document)node).getDocumentElement();
/*  52 */     XMLWriter.serializeAsXML(node, w);
/*     */   }
/*     */   
/*     */   public static void writeXML(java.io.OutputStream out, Node node) {
/*  56 */     java.io.OutputStreamWriter w = new java.io.OutputStreamWriter(out);
/*  57 */     writeXML(w, node);
/*     */   }
/*     */   
/*     */   public static Document getDocumentFromString(String xml) throws IOException, org.xml.sax.SAXException, javax.xml.parsers.ParserConfigurationException {
/*  61 */     DocumentBuilder db = getDocumentBuilder();
/*  62 */     return db.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
/*     */   }
/*     */   
/*     */   public static Document getDocumentFromReader(java.io.Reader reader) throws IOException, org.xml.sax.SAXException, javax.xml.parsers.ParserConfigurationException {
/*  66 */     DocumentBuilder dp = getDocumentBuilder();
/*  67 */     return dp.parse(new org.xml.sax.InputSource(reader));
/*     */   }
/*     */   
/*     */   public static Document getDocumentFromInputStream(java.io.InputStream inputStream) throws IOException, org.xml.sax.SAXException, javax.xml.parsers.ParserConfigurationException {
/*  71 */     DocumentBuilder dp = getDocumentBuilder();
/*  72 */     return dp.parse(inputStream);
/*     */   }
/*     */   
/*     */   public static Document getDocumentFromInputSource(org.xml.sax.InputSource inputSource) throws IOException, org.xml.sax.SAXException, javax.xml.parsers.ParserConfigurationException {
/*  76 */     DocumentBuilder dp = getDocumentBuilder();
/*  77 */     return dp.parse(inputSource);
/*     */   }
/*     */   
/*     */   public static Element getChildElement(Element parent, String childElementName) {
/*  81 */     return (Element)getChildNode(parent, childElementName);
/*     */   }
/*     */   
/*     */   public static String getChildElementText(Element parent, String childElementName) {
/*  85 */     Element child = (Element)getChildNode(parent, childElementName);
/*  86 */     if (child != null)
/*  87 */       return getElementText(child);
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   public static List getChildElements(Element parentElement)
/*     */   {
/*  93 */     NodeList oChildren = parentElement.getChildNodes();
/*  94 */     int len = oChildren.getLength();
/*  95 */     List oList = new java.util.ArrayList(len);
/*  96 */     for (int i = 0; i < len; i++) {
/*  97 */       Node oNode = oChildren.item(i);
/*  98 */       if (oNode.getNodeType() == 1)
/*  99 */         oList.add(oNode);
/*     */     }
/* 101 */     return oList;
/*     */   }
/*     */   
/*     */   public static List getChildElements(Element parentElement, String childElementName)
/*     */   {
/* 106 */     NodeList children = parentElement.getChildNodes();
/* 107 */     int len = children.getLength();
/* 108 */     List list = new java.util.ArrayList(len);
/* 109 */     for (int i = 0; i < len; i++) {
/* 110 */       Node node = children.item(i);
/* 111 */       if ((node.getNodeType() == 1) && (node.getNodeName().equals(childElementName)))
/* 112 */         list.add(node);
/*     */     }
/* 114 */     return list;
/*     */   }
/*     */   
/*     */   public static Node getChildNode(Node node, String childToFind) {
/* 118 */     NodeList oChildren = node.getChildNodes();
/* 119 */     Node oReturnValue = getNamedNode(oChildren, childToFind);
/* 120 */     return oReturnValue;
/*     */   }
/*     */   
/*     */   public static Node getNamedNode(NodeList nodes, String nameToFind) {
/* 124 */     int nCount = nodes.getLength();
/* 125 */     for (int i = 0; i < nCount; i++) {
/* 126 */       Node node = nodes.item(i);
/* 127 */       String sNodeName = node.getNodeName();
/* 128 */       if ((sNodeName != null) && (sNodeName.equals(nameToFind)))
/* 129 */         return node;
/*     */     }
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   public static void setElementText(Element element, String value) {
/* 135 */     if (element != null) {
/* 136 */       Node childNode = element.getFirstChild();
/* 137 */       if (childNode != null)
/*     */       {
/* 139 */         childNode.setNodeValue(value);
/*     */       }
/*     */       else {
/* 142 */         childNode = element.getOwnerDocument().createTextNode(value);
/* 143 */         element.appendChild(childNode);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getElementText(Element element) {
/* 149 */     return getElementText(element, false);
/*     */   }
/*     */   
/*     */   public static String getElementText(Element element, boolean checkForCDATA) {
/* 153 */     if (element != null) {
/* 154 */       Node oChildNode = element.getFirstChild();
/*     */       
/* 156 */       if (oChildNode != null) {
/* 157 */         if (oChildNode.getNodeType() == 3) {
/* 158 */           return oChildNode.getNodeValue();
/*     */         }
/* 160 */         if ((checkForCDATA) && (oChildNode.getNodeType() == 4))
/* 161 */           return oChildNode.getNodeValue();
/*     */       }
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   public static Element addChildElement(Element parent, String childElementName) {
/* 168 */     Element child = parent.getOwnerDocument().createElement(childElementName);
/* 169 */     parent.appendChild(child);
/* 170 */     return child;
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
/*     */   public static Element addChildElementNS(Element parent, String childElementName)
/*     */   {
/* 183 */     String namespace = parent.getNamespaceURI();
/* 184 */     Element child = parent.getOwnerDocument().createElementNS(namespace, childElementName);
/* 185 */     parent.appendChild(child);
/* 186 */     return child;
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
/*     */   public static Element addChildElement(Element parent, String childElementName, String childElementText)
/*     */   {
/* 201 */     Document doc = parent.getOwnerDocument();
/* 202 */     Element child = doc.createElement(childElementName);
/* 203 */     child.appendChild(doc.createTextNode(childElementText));
/* 204 */     parent.appendChild(child);
/* 205 */     return child;
/*     */   }
/*     */   
/*     */   public static Element addChildElementNS(Element parent, String childElementName, String childElementText) {
/* 209 */     Document doc = parent.getOwnerDocument();
/* 210 */     String namespace = parent.getNamespaceURI();
/* 211 */     Element child = doc.createElementNS(namespace, childElementName);
/* 212 */     child.appendChild(doc.createTextNode(childElementText));
/* 213 */     parent.appendChild(child);
/* 214 */     return child;
/*     */   }
/*     */   
/*     */   public static String getElementCompleteText(Element element) {
/* 218 */     StringBuffer sb = new StringBuffer();
/* 219 */     NodeList oChildNodes = element.getChildNodes();
/* 220 */     int nChildCount = oChildNodes.getLength();
/* 221 */     for (int i = 0; i < nChildCount; i++) {
/* 222 */       Node oNode = oChildNodes.item(i);
/* 223 */       if (oNode.getNodeType() == 3) {
/* 224 */         String sNodeValue = oNode.getNodeValue();
/* 225 */         sb.append(sNodeValue);
/*     */       }
/*     */     }
/* 228 */     String sReturnValue = sb.toString();
/* 229 */     return sReturnValue;
/*     */   }
/*     */   
/*     */   public static void removeAllChildren(Node node) {
/* 233 */     while (node.getFirstChild() != null) {
/* 234 */       Node child = node.getFirstChild();
/* 235 */       node.removeChild(child);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] convertNodeListToStringArray(NodeList nodes)
/*     */   {
/* 247 */     int len = nodes.getLength();
/* 248 */     String[] sValues = new String[len];
/* 249 */     for (int i = 0; i < len; i++) {
/* 250 */       Node oNode = nodes.item(i);
/* 251 */       sValues[i] = oNode.getNodeValue();
/*     */     }
/* 253 */     return sValues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] convertListOfNodesToStringArray(List nodes)
/*     */   {
/* 265 */     int len = nodes.size();
/* 266 */     String[] sValues = new String[len];
/* 267 */     for (int i = 0; i < len; i++) {
/* 268 */       Node oNode = (Node)nodes.get(i);
/* 269 */       sValues[i] = oNode.getNodeValue();
/*     */     }
/* 271 */     return sValues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] convertNodeListOfElementsToStringArray(NodeList elements)
/*     */   {
/* 282 */     int len = elements.getLength();
/* 283 */     String[] sValues = new String[len];
/* 284 */     for (int i = 0; i < len; i++) {
/* 285 */       Element element = (Element)elements.item(i);
/* 286 */       sValues[i] = getElementText(element);
/*     */     }
/* 288 */     return sValues;
/*     */   }
/*     */   
/*     */   public static Document getNewDocument() throws javax.xml.parsers.ParserConfigurationException {
/* 292 */     DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
/* 293 */     return db.newDocument();
/*     */   }
/*     */   
/*     */   public static String saveToString(Node node) {
/* 297 */     java.io.StringWriter sw = new java.io.StringWriter(512);
/* 298 */     writeXML(sw, node);
/* 299 */     return sw.toString();
/*     */   }
/*     */   
/*     */   public static String encodeForXML(String str) {
/* 303 */     if (str == null)
/* 304 */       return null;
/* 305 */     int nSize = str.length();
/* 306 */     StringBuffer sb = new StringBuffer(nSize);
/* 307 */     for (int i = 0; i < nSize; i++)
/*     */     {
/*     */ 
/* 310 */       char c = str.charAt(i);
/* 311 */       switch (c) {
/*     */       case '<': 
/* 313 */         sb.append("&lt;");
/* 314 */         break;
/*     */       case '>': 
/* 316 */         sb.append("&gt;");
/* 317 */         break;
/*     */       case '"': 
/* 319 */         sb.append("&quot;");
/* 320 */         break;
/*     */       case '\'': 
/* 322 */         sb.append("&apos;");
/* 323 */         break;
/*     */       case '&': 
/* 325 */         sb.append("&amp;");
/* 326 */         break;
/*     */       default: 
/* 328 */         sb.append(c);
/*     */       }
/*     */     }
/* 331 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String decodeFromXML(String str) {
/* 335 */     if (str == null) {
/* 336 */       return null;
/*     */     }
/* 338 */     int nSize = str.length();
/* 339 */     StringBuffer sb = new StringBuffer(nSize);
/* 340 */     for (int i = 0; i < nSize; i++)
/*     */     {
/* 342 */       char c = str.charAt(i);
/*     */       
/* 344 */       if (c == '&') {
/* 345 */         String entity = str.substring(i + 1, str.indexOf(';', i));
/*     */         
/* 347 */         if (entity.equals("lt")) {
/* 348 */           sb.append('<');
/* 349 */           i = str.indexOf(';', i);
/* 350 */         } else if (entity.equals("gt")) {
/* 351 */           sb.append('>');
/* 352 */           i = str.indexOf(';', i);
/* 353 */         } else if (entity.equals("quot")) {
/* 354 */           sb.append('"');
/* 355 */           i = str.indexOf(';', i);
/* 356 */         } else if (entity.equals("apos")) {
/* 357 */           sb.append('\'');
/* 358 */           i = str.indexOf(';', i);
/* 359 */         } else if (entity.equals("amp")) {
/* 360 */           sb.append('&');
/* 361 */           i = str.indexOf(';', i);
/*     */         } else {
/* 363 */           sb.append(c);
/*     */         }
/*     */       } else {
/* 366 */         sb.append(c);
/*     */       }
/*     */     }
/* 369 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static Element createNamespace(Document doc, String namespaceName, String namespaceURI) throws Exception
/*     */   {
/* 374 */     Element elemContentEngineNameSpace = doc.createElement("ns");
/* 375 */     elemContentEngineNameSpace.setAttribute(namespaceName, namespaceURI);
/* 376 */     return elemContentEngineNameSpace;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private static DocumentBuilder getDocumentBuilder()
/*     */     throws javax.xml.parsers.ParserConfigurationException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 2	com/ibm/ier/plugin/util/XMLUtil:documentBuilderFactory	Ljavax/xml/parsers/DocumentBuilderFactory;
/*     */     //   3: dup
/*     */     //   4: astore_0
/*     */     //   5: monitorenter
/*     */     //   6: getstatic 2	com/ibm/ier/plugin/util/XMLUtil:documentBuilderFactory	Ljavax/xml/parsers/DocumentBuilderFactory;
/*     */     //   9: invokevirtual 3	javax/xml/parsers/DocumentBuilderFactory:newDocumentBuilder	()Ljavax/xml/parsers/DocumentBuilder;
/*     */     //   12: aload_0
/*     */     //   13: monitorexit
/*     */     //   14: areturn
/*     */     //   15: astore_1
/*     */     //   16: aload_0
/*     */     //   17: monitorexit
/*     */     //   18: aload_1
/*     */     //   19: athrow
/*     */     // Line number table:
/*     */     //   Java source line #26	-> byte code offset #0
/*     */     //   Java source line #27	-> byte code offset #6
/*     */     //   Java source line #28	-> byte code offset #15
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   4	13	0	Ljava/lang/Object;	Object
/*     */     //   15	4	1	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	14	15	finally
/*     */     //   15	18	15	finally
/*     */   }
/*     */   
/*     */   private static class PrettyFilter
/*     */     extends FilterWriter
/*     */   {
/* 380 */     private boolean openCaretQueued = false;
/* 381 */     private boolean inElement = false;
/* 382 */     private boolean closeElementQueued = false;
/*     */     
/* 384 */     private boolean inWork = false;
/* 385 */     private int indentLevel = 0;
/* 386 */     private int lastIndentLevel = 0;
/* 387 */     private String indent = "    ";
/* 388 */     private int openCaret = 60;
/* 389 */     private int closeElement = 47;
/* 390 */     private int closeCaret = 62;
/*     */     
/*     */     public PrettyFilter(Writer w) {
/* 393 */       super();
/*     */     }
/*     */     
/*     */     private void reduceIndent() {
/* 397 */       if (--this.indentLevel < 0)
/* 398 */         this.indentLevel = 0;
/*     */     }
/*     */     
/*     */     private void writeNewLine(int useIndentLevel) throws IOException {
/* 402 */       super.write(10);
/* 403 */       for (int n = 0; n < useIndentLevel; n++)
/* 404 */         super.write(this.indent);
/*     */     }
/*     */     
/*     */     private void writeSkipCloseElement(char c) throws IOException {
/* 408 */       super.write(this.closeElement);
/* 409 */       super.write(c);
/*     */       
/* 411 */       this.closeElementQueued = false;
/*     */     }
/*     */     
/*     */     private void writeCloseSingleElement() throws IOException {
/* 415 */       boolean doNewLine = this.lastIndentLevel != this.indentLevel;
/* 416 */       this.inWork = true;
/* 417 */       super.write(this.closeElement);
/* 418 */       super.write(this.closeCaret);
/* 419 */       if (doNewLine)
/* 420 */         writeNewLine(this.indentLevel);
/* 421 */       reduceIndent();
/* 422 */       this.inWork = false;
/*     */       
/* 424 */       this.openCaretQueued = false;
/* 425 */       this.closeElementQueued = false;
/* 426 */       this.inElement = false;
/*     */     }
/*     */     
/*     */     private void writeCloseElement() throws IOException {
/* 430 */       boolean doNewLine = this.lastIndentLevel != this.indentLevel;
/* 431 */       this.inWork = true;
/* 432 */       reduceIndent();
/* 433 */       if (doNewLine)
/* 434 */         writeNewLine(this.indentLevel);
/* 435 */       super.write(this.openCaret);
/* 436 */       super.write(this.closeElement);
/* 437 */       this.inWork = false;
/*     */       
/* 439 */       this.openCaretQueued = false;
/* 440 */       this.inElement = false;
/*     */     }
/*     */     
/*     */     private void writeOpenElement(int c) throws IOException {
/* 444 */       this.inWork = true;
/* 445 */       writeNewLine(this.indentLevel++);
/* 446 */       this.lastIndentLevel = this.indentLevel;
/* 447 */       super.write(this.openCaret);
/* 448 */       super.write(c);
/* 449 */       this.inWork = false;
/*     */       
/* 451 */       this.openCaretQueued = false;
/* 452 */       this.inElement = true;
/*     */     }
/*     */     
/*     */     private void writeBuffered(StringBuffer buf) throws IOException {
/* 456 */       if (buf.length() > 0) {
/* 457 */         this.inWork = true;
/* 458 */         super.write(buf.toString());
/* 459 */         this.inWork = false;
/* 460 */         buf.setLength(0);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void rawWrite(char c, StringBuffer buf)
/*     */       throws IOException
/*     */     {
/* 472 */       if ((!this.openCaretQueued) && (c == this.openCaret)) {
/* 473 */         if (buf != null)
/* 474 */           writeBuffered(buf);
/* 475 */         this.openCaretQueued = true;
/*     */ 
/*     */       }
/* 478 */       else if ((this.openCaretQueued) && (c == this.closeElement)) {
/* 479 */         writeCloseElement();
/*     */ 
/*     */       }
/* 482 */       else if ((!this.closeElementQueued) && (this.inElement) && (c == this.closeElement)) {
/* 483 */         if (buf != null)
/* 484 */           writeBuffered(buf);
/* 485 */         this.closeElementQueued = true;
/*     */ 
/*     */       }
/* 488 */       else if ((this.closeElementQueued) && (c == this.closeCaret)) {
/* 489 */         writeCloseSingleElement();
/*     */ 
/*     */       }
/* 492 */       else if (this.closeElementQueued) {
/* 493 */         writeSkipCloseElement(c);
/*     */ 
/*     */       }
/* 496 */       else if (this.openCaretQueued) {
/* 497 */         writeOpenElement(c);
/*     */ 
/*     */       }
/* 500 */       else if (buf != null) {
/* 501 */         buf.append(c);
/*     */       }
/*     */     }
/*     */     
/* 505 */     public void write(int c) throws IOException { if (this.inWork) {
/* 506 */         super.write(c);
/*     */       }
/*     */       else
/* 509 */         rawWrite((char)c, null);
/*     */     }
/*     */     
/*     */     public void write(char[] chars, int i, int i1) throws IOException {
/* 513 */       if (this.inWork) {
/* 514 */         super.write(chars, i, i1);
/*     */       }
/*     */       else {
/* 517 */         StringBuffer buf = new StringBuffer();
/*     */         
/* 519 */         for (int n = 0; n < i1; n++) {
/* 520 */           rawWrite(chars[(i + n)], buf);
/*     */         }
/* 522 */         writeBuffered(buf);
/*     */       }
/*     */     }
/*     */     
/*     */     public void write(String s, int i, int i1) throws IOException {
/* 527 */       if (this.inWork) {
/* 528 */         super.write(s, i, i1);
/*     */       }
/*     */       else {
/* 531 */         StringBuffer buf = new StringBuffer();
/*     */         
/* 533 */         for (int n = 0; n < i1; n++) {
/* 534 */           rawWrite(s.charAt(i + n), buf);
/*     */         }
/* 536 */         writeBuffered(buf);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class XMLWriter {
/* 542 */     private static final String lineSeparator = System.getProperty("line.separator", "\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 547 */     private static String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static String nodeToString(Node node)
/*     */     {
/* 554 */       java.io.StringWriter sw = new java.io.StringWriter();
/*     */       
/* 556 */       serializeAsXML(node, sw);
/*     */       
/* 558 */       return sw.toString();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static void serializeAsXML(Node node, Writer writer)
/*     */     {
/* 565 */       print(node, null, new PrintWriter(writer));
/*     */     }
/*     */     
/*     */     private static void print(Node node, ObjectRegistry namespaceStack, PrintWriter out) {
/* 569 */       if (node == null) {
/* 570 */         return;
/*     */       }
/*     */       
/* 573 */       boolean hasChildren = false;
/* 574 */       int type = node.getNodeType();
/*     */       
/* 576 */       switch (type) {
/*     */       case 9: 
/* 578 */         out.println("<?xml version=\"1.0\"?>");
/*     */         
/* 580 */         NodeList children = node.getChildNodes();
/*     */         
/* 582 */         if (children != null) {
/* 583 */           int numChildren = children.getLength();
/*     */           
/* 585 */           for (int i = 0; i < numChildren; i++)
/* 586 */             print(children.item(i), namespaceStack, out);
/*     */         }
/* 588 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case 1: 
/* 593 */         namespaceStack = new ObjectRegistry(namespaceStack);
/*     */         
/* 595 */         out.print('<' + node.getNodeName());
/*     */         
/* 597 */         String elPrefix = node.getPrefix();
/* 598 */         String elNamespaceURI = node.getNamespaceURI();
/*     */         
/*     */ 
/*     */ 
/* 602 */         if ((elPrefix != null) && (elPrefix.length() > 0) && (elNamespaceURI != null) && (elNamespaceURI.length() > 0)) {
/* 603 */           boolean prefixIsDeclared = false;
/*     */           try
/*     */           {
/* 606 */             String namespaceURI = (String)namespaceStack.lookup(elPrefix);
/*     */             
/* 608 */             if (elNamespaceURI.equals(namespaceURI)) {
/* 609 */               prefixIsDeclared = true;
/*     */             }
/*     */           }
/*     */           catch (IllegalArgumentException e) {}
/*     */           
/* 614 */           if (!prefixIsDeclared) {
/* 615 */             printNamespaceDecl(node, namespaceStack, out);
/*     */           }
/*     */         }
/*     */         
/* 619 */         org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
/* 620 */         int len = attrs != null ? attrs.getLength() : 0;
/*     */         
/* 622 */         for (int i = 0; i < len; i++) {
/* 623 */           Attr attr = (Attr)attrs.item(i);
/*     */           
/* 625 */           out.print(' ' + attr.getNodeName() + "=\"" + normalize(attr.getValue()) + '"');
/*     */           
/* 627 */           String attrPrefix = attr.getPrefix();
/* 628 */           String attrNamespaceURI = attr.getNamespaceURI();
/*     */           
/* 630 */           if ((attrPrefix != null) && (attrPrefix.length() > 0) && (attrNamespaceURI != null) && (attrNamespaceURI.length() > 0)) {
/* 631 */             boolean prefixIsDeclared = false;
/*     */             try
/*     */             {
/* 634 */               String namespaceURI = (String)namespaceStack.lookup(attrPrefix);
/*     */               
/* 636 */               if (attrNamespaceURI.equals(namespaceURI)) {
/* 637 */                 prefixIsDeclared = true;
/*     */               }
/*     */             }
/*     */             catch (IllegalArgumentException e) {}
/*     */             
/* 642 */             if (!prefixIsDeclared) {
/* 643 */               printNamespaceDecl(attr, namespaceStack, out);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 648 */         NodeList children = node.getChildNodes();
/*     */         
/* 650 */         if (children != null) {
/* 651 */           int numChildren = children.getLength();
/*     */           
/* 653 */           hasChildren = numChildren > 0;
/*     */           
/* 655 */           if (hasChildren) {
/* 656 */             out.print('>');
/*     */           }
/*     */           
/* 659 */           for (int i = 0; i < numChildren; i++) {
/* 660 */             print(children.item(i), namespaceStack, out);
/*     */           }
/*     */         } else {
/* 663 */           hasChildren = false;
/*     */         }
/*     */         
/* 666 */         if (!hasChildren) {
/* 667 */           out.print("/>");
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       case 5: 
/* 673 */         out.print('&');
/* 674 */         out.print(node.getNodeName());
/* 675 */         out.print(';');
/* 676 */         break;
/*     */       
/*     */ 
/*     */       case 4: 
/* 680 */         out.print("<![CDATA[");
/* 681 */         out.print(node.getNodeValue());
/* 682 */         out.print("]]>");
/* 683 */         break;
/*     */       
/*     */ 
/*     */       case 3: 
/* 687 */         out.print(normalize(node.getNodeValue()));
/* 688 */         break;
/*     */       
/*     */ 
/*     */       case 8: 
/* 692 */         out.print("<!--");
/* 693 */         out.print(node.getNodeValue());
/* 694 */         out.print("-->");
/* 695 */         break;
/*     */       
/*     */ 
/*     */       case 7: 
/* 699 */         out.print("<?");
/* 700 */         out.print(node.getNodeName());
/*     */         
/* 702 */         String data = node.getNodeValue();
/*     */         
/* 704 */         if ((data != null) && (data.length() > 0)) {
/* 705 */           out.print(' ');
/* 706 */           out.print(data);
/*     */         }
/*     */         
/* 709 */         out.println("?>");
/* 710 */         break;
/*     */       }
/*     */       
/*     */       
/* 714 */       if ((type == 1) && (hasChildren)) {
/* 715 */         out.print("</");
/* 716 */         out.print(node.getNodeName());
/* 717 */         out.print('>');
/*     */       }
/*     */     }
/*     */     
/*     */     private static void printNamespaceDecl(Node node, ObjectRegistry namespaceStack, PrintWriter out)
/*     */     {
/* 723 */       switch (node.getNodeType()) {
/*     */       case 2: 
/* 725 */         printNamespaceDecl(((Attr)node).getOwnerElement(), node, namespaceStack, out);
/* 726 */         break;
/*     */       
/*     */ 
/*     */       case 1: 
/* 730 */         printNamespaceDecl((Element)node, node, namespaceStack, out);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */     private static void printNamespaceDecl(Element owner, Node node, ObjectRegistry namespaceStack, PrintWriter out)
/*     */     {
/* 737 */       String namespaceURI = node.getNamespaceURI();
/* 738 */       String prefix = node.getPrefix();
/*     */       
/* 740 */       if ((!namespaceURI.equals(NS_URI_XMLNS)) || (!prefix.equals("xmlns"))) {
/* 741 */         if (getAttributeNS(owner, NS_URI_XMLNS, prefix) == null) {
/* 742 */           out.print(" xmlns:" + prefix + "=\"" + namespaceURI + '"');
/*     */         }
/*     */       } else {
/* 745 */         prefix = node.getLocalName();
/* 746 */         namespaceURI = node.getNodeValue();
/*     */       }
/*     */       
/* 749 */       namespaceStack.register(prefix, namespaceURI);
/*     */     }
/*     */     
/*     */     public static String getAttributeNS(Element el, String namespaceURI, String localPart) {
/* 753 */       String sRet = null;
/* 754 */       Attr attr = el.getAttributeNodeNS(namespaceURI, localPart);
/*     */       
/* 756 */       if (attr != null) {
/* 757 */         sRet = attr.getValue();
/*     */       }
/*     */       
/* 760 */       return sRet;
/*     */     }
/*     */     
/*     */     private static String normalize(String s) {
/* 764 */       if (s == null) {
/* 765 */         return "";
/*     */       }
/* 767 */       int len = s.length();
/* 768 */       StringBuffer str = new StringBuffer(len * 2);
/*     */       
/* 770 */       for (int i = 0; i < len; i++) {
/* 771 */         char ch = s.charAt(i);
/*     */         
/* 773 */         switch (ch) {
/*     */         case '<': 
/* 775 */           str.append("&lt;");
/* 776 */           break;
/*     */         
/*     */         case '>': 
/* 779 */           str.append("&gt;");
/* 780 */           break;
/*     */         
/*     */         case '&': 
/* 783 */           str.append("&amp;");
/* 784 */           break;
/*     */         
/*     */         case '"': 
/* 787 */           str.append("&quot;");
/* 788 */           break;
/*     */         
/*     */         case '\n': 
/* 791 */           if (i > 0) {
/* 792 */             char lastChar = str.charAt(str.length() - 1);
/*     */             
/* 794 */             if (lastChar != '\r') {
/* 795 */               str.append(lineSeparator);
/*     */             } else {
/* 797 */               str.append('\n');
/*     */             }
/*     */           } else {
/* 800 */             str.append(lineSeparator);
/*     */           }
/* 802 */           break;
/*     */         
/*     */         default: 
/* 805 */           str.append(ch);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 810 */       return str.toString();
/*     */     }
/*     */     
/*     */     public static class ObjectRegistry {
/* 814 */       java.util.Hashtable reg = new java.util.Hashtable();
/*     */       
/* 816 */       ObjectRegistry parent = null;
/*     */       
/*     */ 
/*     */       public ObjectRegistry() {}
/*     */       
/*     */       public ObjectRegistry(ObjectRegistry parent)
/*     */       {
/* 823 */         this.parent = parent;
/*     */       }
/*     */       
/*     */ 
/*     */       public void register(String name, Object obj)
/*     */       {
/* 829 */         this.reg.put(name, obj);
/*     */       }
/*     */       
/*     */ 
/*     */       public void unregister(String name)
/*     */       {
/* 835 */         this.reg.remove(name);
/*     */       }
/*     */       
/*     */       public Object lookup(String name) throws IllegalArgumentException
/*     */       {
/* 840 */         Object obj = this.reg.get(name);
/*     */         
/* 842 */         if ((obj == null) && (this.parent != null)) {
/* 843 */           obj = this.parent.lookup(name);
/*     */         }
/*     */         
/* 846 */         if (obj == null) {
/* 847 */           throw new IllegalArgumentException("object '" + name + "' not in registry");
/*     */         }
/*     */         
/* 850 */         return obj;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\XMLUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */