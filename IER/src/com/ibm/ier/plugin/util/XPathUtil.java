/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.apache.commons.collections.map.SingletonMap;
/*     */ import org.apache.commons.jxpath.JXPathContext;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XPathUtil
/*     */ {
/*  35 */   public static final Map WCM_NS = new SingletonMap("wcm", "http://filenet.com/namespaces/wcm/apps/1.0");
/*     */   
/*  37 */   public static final Map EMPTY_NS = new SingletonMap();
/*     */   
/*     */   public static Document loadDocument(String path) throws Exception
/*     */   {
/*  41 */     DocumentBuilder dB = getDocumentBuilder();
/*  42 */     InputStream inputStream = new FileInputStream(path);
/*  43 */     Document doc = dB.parse(inputStream);
/*  44 */     return doc;
/*     */   }
/*     */   
/*     */   public static Document loadDocument(InputStream inputStream) throws Exception
/*     */   {
/*  49 */     DocumentBuilder dB = getDocumentBuilder();
/*  50 */     Document doc = dB.parse(inputStream);
/*  51 */     return doc;
/*     */   }
/*     */   
/*     */   private static DocumentBuilder getDocumentBuilder() throws Exception {
/*  55 */     DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
/*  56 */     DocumentBuilder dB = dBF.newDocumentBuilder();
/*     */     
/*  58 */     dB.setEntityResolver(new EntityResolver() {
/*     */       public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/*  60 */         return new InputSource(new StringReader(""));
/*     */       }
/*  62 */     });
/*  63 */     return dB;
/*     */   }
/*     */   
/*     */   public static String getStringResult(Node node) throws Exception
/*     */   {
/*  68 */     if ((node != null) && (node.getFirstChild() != null)) {
/*  69 */       return node.getFirstChild().getNodeValue();
/*     */     }
/*  71 */     return null;
/*     */   }
/*     */   
/*     */   public static String getXPathResult(Node contextNode, String xpath) throws Exception
/*     */   {
/*  76 */     Node node = selectSingleNode(contextNode, xpath, WCM_NS);
/*  77 */     if ((node != null) && (node.getFirstChild() != null)) {
/*  78 */       return node.getFirstChild().getNodeValue();
/*     */     }
/*  80 */     return null;
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
/*     */   public static Node selectSingleNode(Node contextNode, String xPath)
/*     */     throws Exception
/*     */   {
/*  94 */     JXPathContext context = JXPathContext.newContext(contextNode);
/*  95 */     context.setLenient(true);
/*  96 */     configNamespaces(context, WCM_NS);
/*  97 */     Node node = (Node)context.selectSingleNode(xPath);
/*  98 */     return node;
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
/*     */   public static List selectNodes(Node contextNode, String xPath)
/*     */     throws Exception
/*     */   {
/* 113 */     JXPathContext context = JXPathContext.newContext(contextNode);
/* 114 */     context.setLenient(true);
/* 115 */     configNamespaces(context, WCM_NS);
/* 116 */     List list = context.selectNodes(xPath);
/* 117 */     return list;
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
/*     */   public static Node selectSingleNode(Node contextNode, String xPath, Map namespaces)
/*     */     throws Exception
/*     */   {
/* 133 */     JXPathContext context = JXPathContext.newContext(contextNode);
/* 134 */     context.setLenient(true);
/* 135 */     configNamespaces(context, namespaces);
/* 136 */     Node node = (Node)context.selectSingleNode(xPath);
/* 137 */     return node;
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
/*     */   public static List selectNodes(Node contextNode, String xPath, Map namespaces)
/*     */     throws Exception
/*     */   {
/* 153 */     JXPathContext context = JXPathContext.newContext(contextNode);
/* 154 */     context.setLenient(true);
/* 155 */     configNamespaces(context, namespaces);
/* 156 */     List list = context.selectNodes(xPath);
/* 157 */     return list;
/*     */   }
/*     */   
/*     */   private static void configNamespaces(JXPathContext context, Map namespaces) throws Exception
/*     */   {
/*     */     Iterator iterator;
/* 163 */     if (namespaces != null)
/*     */     {
/* 165 */       Set entries = namespaces.entrySet();
/* 166 */       for (iterator = entries.iterator(); iterator.hasNext();)
/*     */       {
/* 168 */         Map.Entry entry = (Map.Entry)iterator.next();
/* 169 */         String prefix = (String)entry.getKey();
/* 170 */         String uri = (String)entry.getValue();
/* 171 */         context.registerNamespace(prefix, uri);
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
/*     */   public static Node removeNode(Node contextNode, String xPath, Map namespaces)
/*     */     throws Exception
/*     */   {
/* 190 */     Node node = selectSingleNode(contextNode, xPath, namespaces);
/*     */     
/* 192 */     if (node != null)
/*     */     {
/* 194 */       Node parent = node.getParentNode();
/* 195 */       if (parent != null)
/* 196 */         parent.removeChild(node);
/*     */     }
/* 198 */     return node;
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
/*     */   public static void removeNodes(Node contextNode, String xPath, Map namespaces)
/*     */     throws Exception
/*     */   {
/* 213 */     List nodelist = selectNodes(contextNode, xPath, namespaces);
/*     */     
/* 215 */     if (nodelist != null)
/*     */     {
/* 217 */       for (int i = 0; i < nodelist.size(); i++)
/*     */       {
/* 219 */         Node currentNode = (Node)nodelist.get(i);
/* 220 */         Node parent = currentNode.getParentNode();
/* 221 */         if (parent != null) {
/* 222 */           parent.removeChild(currentNode);
/*     */         }
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
/*     */   public static NodeList createNodeList(List nodes)
/*     */   {
/* 236 */     return new NodeListImpl(nodes);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NodeListImpl
/*     */     implements NodeList
/*     */   {
/*     */     private List list;
/*     */     
/*     */     public NodeListImpl(List list)
/*     */     {
/* 247 */       this.list = list;
/*     */     }
/*     */     
/*     */     public int getLength()
/*     */     {
/* 252 */       return this.list.size();
/*     */     }
/*     */     
/*     */     public Node item(int i)
/*     */     {
/* 257 */       return (Node)this.list.get(i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\XPathUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */