/*     */ package com.ibm.jarm.ral.common;
/*     */ 
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpression;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RAL_XMLSupport
/*     */ {
/*  44 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalCommon);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   private static Map<String, XPathExpression> XPathExpCache = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private static XPath XPathSingleton = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Node findXMLNode(Object source, String xPathStr, boolean useExprCache)
/*     */     throws XPathExpressionException
/*     */   {
/*  78 */     Tracer.traceMethodEntry(new Object[] { source, xPathStr });
/*     */     
/*  80 */     XPathExpression xPathExpr = getXPathExpression(xPathStr, useExprCache);
/*  81 */     Node result = (Node)xPathExpr.evaluate(source, XPathConstants.NODE);
/*     */     
/*  83 */     Tracer.traceMethodExit(new Object[] { result });
/*  84 */     return result;
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
/*     */   public static Document transformUsingXSLT(Document srcDomDoc, String xsltDefStr)
/*     */     throws ParserConfigurationException, TransformerException
/*     */   {
/* 102 */     Tracer.traceMethodEntry(new Object[] { srcDomDoc, xsltDefStr });
/*     */     
/*     */ 
/* 105 */     ByteArrayInputStream bais = new ByteArrayInputStream(xsltDefStr.getBytes());
/* 106 */     StreamSource xsltSource = new StreamSource(bais);
/*     */     
/*     */ 
/* 109 */     DOMSource origSource = new DOMSource(srcDomDoc);
/*     */     
/*     */ 
/* 112 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/* 113 */     Document transformedXMLDOMDoc = dbFactory.newDocumentBuilder().newDocument();
/* 114 */     DOMResult xfrmResult = new DOMResult(transformedXMLDOMDoc);
/*     */     
/*     */ 
/* 117 */     TransformerFactory tFactory = TransformerFactory.newInstance();
/* 118 */     Transformer xsltXfrm = tFactory.newTransformer(xsltSource);
/*     */     
/*     */ 
/* 121 */     xsltXfrm.transform(origSource, xfrmResult);
/*     */     
/* 123 */     Tracer.traceMethodExit(new Object[] { transformedXMLDOMDoc });
/* 124 */     return transformedXMLDOMDoc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeDomDocToFile(Document domDoc, File outputFile)
/*     */   {
/* 135 */     Tracer.traceMethodEntry(new Object[] { domDoc, outputFile });
/*     */     try
/*     */     {
/* 138 */       Source source = new DOMSource(domDoc);
/* 139 */       Result result = new StreamResult(outputFile);
/* 140 */       Transformer xformer = TransformerFactory.newInstance().newTransformer();
/* 141 */       xformer.transform(source, result);
/* 142 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 146 */       String filePath = outputFile != null ? outputFile.getPath() : "<null>";
/* 147 */       throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_EXPORT_XML_FILE_OUTPUT_FAILURE, new Object[] { filePath, ex.getLocalizedMessage() });
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
/*     */   public static NodeList findXMLNodes(Object source, String xPathStr, boolean useExprCache)
/*     */     throws XPathExpressionException
/*     */   {
/* 172 */     Tracer.traceMethodEntry(new Object[] { source, xPathStr });
/*     */     
/* 174 */     XPathExpression xPathExpr = getXPathExpression(xPathStr, useExprCache);
/* 175 */     NodeList result = (NodeList)xPathExpr.evaluate(source, XPathConstants.NODESET);
/*     */     
/* 177 */     Tracer.traceMethodExit(new Object[] { result });
/* 178 */     return result;
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
/*     */   private static synchronized XPathExpression getXPathExpression(String xPathStr, boolean useExprCache)
/*     */   {
/* 193 */     Tracer.traceMethodEntry(new Object[] { xPathStr, Boolean.valueOf(useExprCache) });
/* 194 */     XPathExpression xPathExpr = null;
/*     */     try
/*     */     {
/* 197 */       if (useExprCache)
/*     */       {
/* 199 */         xPathExpr = (XPathExpression)XPathExpCache.get(xPathStr);
/* 200 */         if (xPathExpr == null)
/*     */         {
/* 202 */           xPathExpr = getXPathInstance().compile(xPathStr);
/* 203 */           XPathExpCache.put(xPathStr, xPathExpr);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 208 */         xPathExpr = getXPathInstance().compile(xPathStr);
/*     */       }
/*     */     }
/*     */     catch (XPathExpressionException ex)
/*     */     {
/* 213 */       throw RMRuntimeException.createRMRuntimeException(ex, RMErrorCode.RAL_XPATH_COMPILE_ERROR, new Object[] { xPathExpr });
/*     */     }
/*     */     
/* 216 */     Tracer.traceMethodExit(new Object[] { xPathExpr });
/* 217 */     return xPathExpr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized XPath getXPathInstance()
/*     */   {
/* 227 */     Tracer.traceMethodEntry(new Object[0]);
/* 228 */     if (XPathSingleton == null)
/*     */     {
/* 230 */       XPathSingleton = XPathFactory.newInstance().newXPath();
/*     */     }
/*     */     
/* 233 */     Tracer.traceMethodExit(new Object[] { XPathSingleton });
/* 234 */     return XPathSingleton;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\common\RAL_XMLSupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */