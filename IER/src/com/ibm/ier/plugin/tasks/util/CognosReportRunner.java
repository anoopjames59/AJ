/*     */ package com.ibm.ier.plugin.tasks.util;
/*     */ 
/*     */ import com.cognos.developer.schemas.bibus._3.AsynchDetailReportOutput;
/*     */ import com.cognos.developer.schemas.bibus._3.AsynchDetailReportStatus;
/*     */ import com.cognos.developer.schemas.bibus._3.AsynchDetailReportStatusEnum;
/*     */ import com.cognos.developer.schemas.bibus._3.AsynchReply;
/*     */ import com.cognos.developer.schemas.bibus._3.AsynchReplyStatusEnum;
/*     */ import com.cognos.developer.schemas.bibus._3.AsynchSecondaryRequest;
/*     */ import com.cognos.developer.schemas.bibus._3.BiBusHeader;
/*     */ import com.cognos.developer.schemas.bibus._3.ContentManagerService_PortType;
/*     */ import com.cognos.developer.schemas.bibus._3.ContentManagerService_ServiceLocator;
/*     */ import com.cognos.developer.schemas.bibus._3.Option;
/*     */ import com.cognos.developer.schemas.bibus._3.ParameterValue;
/*     */ import com.cognos.developer.schemas.bibus._3.ParmValueItem;
/*     */ import com.cognos.developer.schemas.bibus._3.ReportService_PortType;
/*     */ import com.cognos.developer.schemas.bibus._3.ReportService_ServiceLocator;
/*     */ import com.cognos.developer.schemas.bibus._3.RunOptionBoolean;
/*     */ import com.cognos.developer.schemas.bibus._3.RunOptionEnum;
/*     */ import com.cognos.developer.schemas.bibus._3.RunOptionStringArray;
/*     */ import com.cognos.developer.schemas.bibus._3.SearchPathSingleObject;
/*     */ import com.cognos.developer.schemas.bibus._3.SimpleParmValueItem;
/*     */ import com.cognos.developer.schemas.bibus._3.XmlEncodedXML;
/*     */ import com.cognos.org.apache.axis.client.Stub;
/*     */ import com.cognos.org.apache.axis.encoding.Base64;
/*     */ import com.cognos.org.apache.axis.message.SOAPHeaderElement;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CognosReportRunner
/*     */ {
/*  38 */   private ContentManagerService_PortType contentManagerService = null;
/*     */   
/*  40 */   private ReportService_PortType reportService = null;
/*  41 */   private Logger logger = null;
/*     */   
/*     */   private static final String PDF_OUTPUT = "PDF";
/*     */   private static final String REPORT_JOB_ID = "rpt_job_id";
/*     */   
/*     */   public CognosReportRunner(Logger logger, String cognosServletDispatchURL)
/*     */     throws Exception
/*     */   {
/*  49 */     this.logger = logger;
/*  50 */     logger.fine("Cognos Servlet Dispatch URL: " + cognosServletDispatchURL);
/*  51 */     URL url = new URL(cognosServletDispatchURL);
/*     */     
/*     */ 
/*  54 */     logger.fine("Initializing report service");
/*  55 */     this.reportService = new ReportService_ServiceLocator().getreportService(url);
/*     */     
/*     */ 
/*  58 */     logger.fine("Initializing content manager service");
/*  59 */     this.contentManagerService = new ContentManagerService_ServiceLocator().getcontentManagerService(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] executeReport(String reportName, String reportJobID)
/*     */     throws Exception
/*     */   {
/*  71 */     byte[] binaryOutput = null;
/*     */     
/*  73 */     Option[] runOptions = new Option[2];
/*     */     
/*  75 */     RunOptionStringArray outputFormat = new RunOptionStringArray();
/*  76 */     RunOptionBoolean ignoreParameterPrompts = new RunOptionBoolean();
/*     */     
/*     */ 
/*  79 */     outputFormat.setName(RunOptionEnum.outputFormat);
/*  80 */     outputFormat.setValue(new String[] { "PDF" });
/*     */     
/*     */ 
/*  83 */     ignoreParameterPrompts.setName(RunOptionEnum.prompt);
/*  84 */     ignoreParameterPrompts.setValue(false);
/*     */     
/*     */ 
/*  87 */     runOptions[0] = outputFormat;
/*  88 */     runOptions[1] = ignoreParameterPrompts;
/*     */     
/*     */ 
/*  91 */     SearchPathSingleObject repPath = new SearchPathSingleObject();
/*  92 */     repPath.set_value(reportName);
/*     */     
/*  94 */     ParameterValue[] parameters = new ParameterValue[1];
/*  95 */     ParameterValue param = new ParameterValue();
/*  96 */     param.setName("rpt_job_id");
/*  97 */     SimpleParmValueItem item = new SimpleParmValueItem();
/*  98 */     item.setUse(reportJobID);
/*  99 */     item.setDisplay(reportJobID);
/* 100 */     param.setValue(new ParmValueItem[] { item });
/* 101 */     parameters[0] = param;
/*     */     
/* 103 */     this.logger.fine("Starting the report generation");
/* 104 */     AsynchReply asyncReply = getReportService(true).run(repPath, parameters, runOptions);
/* 105 */     this.logger.fine("Finished running the report");
/*     */     
/*     */ 
/* 108 */     this.logger.fine("Waiting to get the report output");
/* 109 */     if (!asyncReply.getStatus().equals(AsynchReplyStatusEnum.complete))
/*     */     {
/* 111 */       this.logger.fine("Waiting for the report reply: " + asyncReply.getStatus());
/* 112 */       while (!asyncReply.getStatus().equals(AsynchReplyStatusEnum.complete))
/*     */       {
/*     */ 
/* 115 */         if (!hasSecondaryRequest(asyncReply, "wait"))
/*     */         {
/* 117 */           return null;
/*     */         }
/* 119 */         asyncReply = getReportService(false).wait(asyncReply.getPrimaryRequest(), parameters, new Option[0]);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */       if (outputIsReady(asyncReply))
/*     */       {
/* 129 */         asyncReply = getReportService(false).getOutput(asyncReply.getPrimaryRequest(), new ParameterValue[0], new Option[0]);
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 137 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 141 */     String data = getOutputPage(asyncReply);
/* 142 */     this.logger.fine("Finished obtaining report output");
/*     */     
/* 144 */     binaryOutput = Base64.decode(data);
/* 145 */     this.logger.fine("Finished decoding report output");
/*     */     
/* 147 */     if (binaryOutput != null)
/*     */     {
/* 149 */       return binaryOutput;
/*     */     }
/* 151 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOutputPage(AsynchReply response)
/*     */   {
/* 162 */     AsynchDetailReportOutput reportOutput = null;
/* 163 */     for (int i = 0; i < response.getDetails().length; i++)
/*     */     {
/* 165 */       if ((response.getDetails()[i] instanceof AsynchDetailReportOutput))
/*     */       {
/* 167 */         reportOutput = (AsynchDetailReportOutput)response.getDetails()[i];
/* 168 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 173 */     return reportOutput.getOutputPages()[0].toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean outputIsReady(AsynchReply response)
/*     */   {
/* 182 */     for (int i = 0; i < response.getDetails().length; i++)
/*     */     {
/* 184 */       if (((response.getDetails()[i] instanceof AsynchDetailReportStatus)) && (((AsynchDetailReportStatus)response.getDetails()[i]).getStatus() == AsynchDetailReportStatusEnum.responseReady) && (hasSecondaryRequest(response, "getOutput")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */         return true;
/*     */       }
/*     */     }
/* 193 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean hasSecondaryRequest(AsynchReply response, String secondaryRequest)
/*     */   {
/* 199 */     AsynchSecondaryRequest[] secondaryRequests = response.getSecondaryRequests();
/*     */     
/* 201 */     for (int i = 0; i < secondaryRequests.length; i++)
/*     */     {
/* 203 */       if (secondaryRequests[i].getName().compareTo(secondaryRequest) == 0)
/*     */       {
/*     */ 
/* 206 */         return true;
/*     */       }
/*     */     }
/* 209 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logon(String namespace, String username, String pwd)
/*     */     throws Exception
/*     */   {
/* 222 */     this.logger.fine("Attempting to perform login..namespace: " + namespace + " username: " + username);
/* 223 */     StringBuffer credentialXML = new StringBuffer();
/*     */     
/* 225 */     credentialXML.append("<credential>");
/* 226 */     credentialXML.append("<namespace>").append(namespace).append("</namespace>");
/* 227 */     credentialXML.append("<username>").append(username).append("</username>");
/* 228 */     credentialXML.append("<password>").append(pwd).append("</password>");
/* 229 */     credentialXML.append("</credential>");
/*     */     
/* 231 */     String encodedCredentials = credentialXML.toString();
/*     */     
/* 233 */     this.contentManagerService.logon(new XmlEncodedXML(encodedCredentials), new SearchPathSingleObject[0]);
/*     */     try
/*     */     {
/* 236 */       SOAPHeaderElement temp = ((Stub)this.contentManagerService).getResponseHeader("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader");
/* 237 */       BiBusHeader cmBiBusHeader = (BiBusHeader)temp.getObjectValue();
/* 238 */       ((Stub)this.contentManagerService).setHeader("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader", cmBiBusHeader);
/*     */     }
/*     */     catch (Throwable exp) {
/* 241 */       this.logger.log(Level.SEVERE, exp.getLocalizedMessage(), exp);
/*     */     }
/*     */   }
/*     */   
/*     */   public ReportService_PortType getReportService(boolean isNewConversation)
/*     */   {
/* 247 */     BiBusHeader bibus = getHeaderObject(((Stub)this.reportService).getResponseHeader("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader"), isNewConversation);
/*     */     
/* 249 */     if (bibus == null)
/*     */     {
/* 251 */       bibus = getHeaderObject(((Stub)this.contentManagerService).getResponseHeader("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader"), true);
/*     */     }
/*     */     
/* 254 */     ((Stub)this.reportService).clearHeaders();
/* 255 */     ((Stub)this.reportService).setHeader("http://developer.cognos.com/schemas/bibus/3/", "biBusHeader", bibus);
/*     */     
/* 257 */     return this.reportService;
/*     */   }
/*     */   
/*     */   public BiBusHeader getHeaderObject(SOAPHeaderElement SourceHeader, boolean isNewConversation)
/*     */   {
/* 262 */     if (SourceHeader == null) {
/* 263 */       return null;
/*     */     }
/* 265 */     BiBusHeader bibus = null;
/*     */     try {
/* 267 */       bibus = (BiBusHeader)SourceHeader.getObjectValue();
/*     */       
/*     */ 
/* 270 */       if (isNewConversation) {
/* 271 */         bibus.setTracking(null);
/*     */       }
/*     */     } catch (Exception e) {
/* 274 */       e.printStackTrace();
/*     */     }
/*     */     
/* 277 */     return bibus;
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {}
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\util\CognosReportRunner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */