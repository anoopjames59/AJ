/*     */ package com.ibm.ier.plugin.nls;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class GenerateDitaFromRuntimeMessages
/*     */ {
/*     */   public static final String DITA_CODE = "FNRRM";
/*     */   
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/*  39 */     File file = new File("./output/dita");
/*  40 */     if (file.exists()) {
/*  41 */       file.delete();
/*     */     }
/*  43 */     file.mkdir();
/*     */     
/*  45 */     String outDir = "./output/dita";
/*  46 */     System.out.println("Generating message dita files to directory " + outDir);
/*     */     
/*  48 */     ResourceBundle servicesBundle = ResourceBundle.getBundle("resources.IERApplicationPluginResources", Locale.ENGLISH);
/*  49 */     processJavaBundle(servicesBundle, outDir);
/*  50 */     processJavascriptBundle("./src/com/ibm/ier/plugin/WebContent/ier/nls/messages.js", outDir);
/*     */     
/*  52 */     System.out.println("Message dita file generation completed");
/*     */   }
/*     */   
/*     */   private static final void processJavaBundle(ResourceBundle bundle, String outDir) throws IOException {
/*  56 */     for (String key : bundle.keySet()) {
/*  57 */       if (key.endsWith(".id")) {
/*  58 */         String keyRoot = key.substring(0, key.length() - ".id".length());
/*  59 */         System.out.println("Processing message " + keyRoot);
/*  60 */         String numKey = keyRoot + ".id";
/*  61 */         String num = bundle.getString(numKey);
/*  62 */         String id = "FNRRM" + num;
/*  63 */         String textKey = keyRoot;
/*  64 */         String text = "";
/*     */         try {
/*  66 */           text = bundle.getString(textKey);
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*  69 */         String explanationKey = keyRoot + ".explanation";
/*  70 */         String explanation = "";
/*     */         try {
/*  72 */           explanation = bundle.getString(explanationKey);
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*  75 */         String userResponseKey = keyRoot + ".userResponse";
/*  76 */         String userResponse = "";
/*     */         try {
/*  78 */           userResponse = bundle.getString(userResponseKey);
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*  81 */         String adminResponseKey = keyRoot + ".adminResponse";
/*  82 */         String adminResponse = "";
/*     */         try {
/*  84 */           adminResponse = bundle.getString(adminResponseKey);
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*  87 */         String parm0key = keyRoot + ".0";
/*  88 */         String parm0 = "parameter_0";
/*     */         try {
/*  90 */           parm0 = bundle.getString(parm0key);
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*  93 */         String parm1key = keyRoot + ".1";
/*  94 */         String parm1 = "parameter_1";
/*     */         try {
/*  96 */           parm1 = bundle.getString(parm1key);
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*  99 */         String parm2key = keyRoot + ".2";
/* 100 */         String parm2 = "parameter_2";
/*     */         try {
/* 102 */           parm2 = bundle.getString(parm2key);
/*     */         }
/*     */         catch (MissingResourceException e) {}
/* 105 */         BufferedWriter writer = new BufferedWriter(new FileWriter(outDir + "/" + id + ".dita"));
/* 106 */         String s = getMessage(num, text, explanation, userResponse, adminResponse, parm0, parm1, parm2, numKey, textKey, explanationKey, userResponseKey, adminResponseKey, parm0key, parm1key, parm2key) + "\n";
/* 107 */         writer.write(s);
/* 108 */         writer.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void processJavascriptBundle(String filename, String outDir) throws IOException {
/* 114 */     Hashtable<String, String> javascriptBundle = new Hashtable();
/* 115 */     BufferedReader reader = new BufferedReader(new FileReader(filename));
/* 116 */     String line = reader.readLine().trim();
/* 117 */     while (line != null) {
/* 118 */       line = line.trim();
/* 119 */       if (line.contains(":")) {
/* 120 */         String key = line.substring(0, line.indexOf(":"));
/* 121 */         String value = line.substring(line.indexOf(":") + 1);
/* 122 */         value = value.trim();
/* 123 */         if (value.startsWith("\"")) {
/* 124 */           value = value.substring(1);
/*     */         }
/* 126 */         if (value.endsWith(",")) {
/* 127 */           value = value.substring(0, value.length() - 1);
/*     */         }
/* 129 */         if (value.endsWith("\"")) {
/* 130 */           value = value.substring(0, value.length() - 1);
/*     */         }
/* 132 */         javascriptBundle.put(key, value);
/*     */       }
/* 134 */       line = reader.readLine();
/*     */     }
/*     */     
/* 137 */     for (String key : javascriptBundle.keySet()) {
/* 138 */       if (key.endsWith("_number")) {
/* 139 */         String keyRoot = key.substring(0, key.length() - "_number".length());
/* 140 */         System.out.println("Processing message " + keyRoot);
/* 141 */         String numKey = keyRoot + "_number";
/* 142 */         String num = (String)javascriptBundle.get(numKey);
/* 143 */         String id = "FNRRM" + num;
/* 144 */         String textKey = keyRoot;
/* 145 */         String text = "";
/*     */         try {
/* 147 */           text = (String)javascriptBundle.get(textKey);
/*     */         }
/*     */         catch (Exception e) {}
/* 150 */         String explanationKey = keyRoot + "_explanation";
/* 151 */         String explanation = "";
/*     */         try {
/* 153 */           explanation = (String)javascriptBundle.get(explanationKey);
/*     */         }
/*     */         catch (Exception e) {}
/* 156 */         String userResponseKey = keyRoot + "_userResponse";
/* 157 */         String userResponse = "";
/*     */         try {
/* 159 */           userResponse = (String)javascriptBundle.get(userResponseKey);
/*     */         }
/*     */         catch (Exception e) {}
/* 162 */         String adminResponseKey = keyRoot + "_adminResponse";
/* 163 */         String adminResponse = "";
/*     */         try {
/* 165 */           adminResponse = (String)javascriptBundle.get(adminResponseKey);
/*     */         }
/*     */         catch (Exception e) {}
/* 168 */         String parm0key = keyRoot + "_0";
/* 169 */         String parm0 = "parameter_0";
/*     */         try {
/* 171 */           parm0 = (String)javascriptBundle.get(parm0key);
/*     */         }
/*     */         catch (Exception e) {}
/* 174 */         String parm1key = keyRoot + "_1";
/* 175 */         String parm1 = "parameter_1";
/*     */         try {
/* 177 */           parm1 = (String)javascriptBundle.get(parm1key);
/*     */         }
/*     */         catch (Exception e) {}
/* 180 */         String parm2key = keyRoot + "_2";
/* 181 */         String parm2 = "parameter_2";
/*     */         try {
/* 183 */           parm2 = (String)javascriptBundle.get(parm2key);
/*     */         }
/*     */         catch (Exception e) {}
/* 186 */         BufferedWriter writer = new BufferedWriter(new FileWriter(outDir + "/" + id + ".dita"));
/* 187 */         String s = getMessage(num, text, explanation, userResponse, adminResponse, parm0, parm1, parm2, numKey, textKey, explanationKey, userResponseKey, adminResponseKey, parm0key, parm1key, parm2key) + "\n";
/* 188 */         writer.write(s);
/* 189 */         writer.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final String addAttr(String name, String value) {
/* 195 */     return ' ' + name + "=\"" + value + "\"";
/*     */   }
/*     */   
/*     */   private static final String getMessage(String num, String msg, String explanation, String userResponse, String adminResponse, String parm0, String parm1, String parm2, String numKey, String msgKey, String explanationKey, String userResponseKey, String adminResponseKey, String parmam0Key, String parm1Key, String param2Key)
/*     */   {
/* 200 */     StringBuffer buf = new StringBuffer();
/* 201 */     msg = closetags(msg);
/* 202 */     explanation = closetags(explanation);
/* 203 */     userResponse = closetags(userResponse);
/* 204 */     adminResponse = closetags(adminResponse);
/* 205 */     msg = substituteParms(msg, parm0, parm1, parm2);
/* 206 */     explanation = substituteParms(explanation, parm0, parm1, parm2);
/* 207 */     userResponse = substituteParms(userResponse, parm0, parm1, parm2);
/* 208 */     adminResponse = substituteParms(adminResponse, parm0, parm1, parm2);
/* 209 */     buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/*     */     
/* 211 */     buf.append("\n<!DOCTYPE msg PUBLIC \"-//IBM//DTD DITA Message Reference//EN\" \"msgRef.dtd\">");
/* 212 */     buf.append("\n<?Pub Inc?>");
/* 213 */     buf.append("\n<msg");
/* 214 */     buf.append(addAttr("activity", "troubleshoot"));
/* 215 */     buf.append(addAttr("id", "FNRRM" + num));
/* 216 */     buf.append(addAttr("product", "SSNVVQ_5.1.2"));
/* 217 */     buf.append(addAttr("xml:lang", "en-us"));
/* 218 */     buf.append(">");
/* 219 */     buf.append("\n<msgId><msgPrefix>FNRRM</msgPrefix><msgNumber id=\"").append(numKey).append("\">").append(num).append("</msgNumber><msgSuffix></msgSuffix>\n</msgId>");
/* 220 */     buf.append("<?Pub Caret -2?>");
/* 221 */     buf.append("\n<msgText id=\"").append(msgKey).append("\">").append(msg).append("</msgText>");
/* 222 */     buf.append("\n<msgBody>");
/* 223 */     if ((explanation != null) && (explanation.length() != 0))
/* 224 */       buf.append("\n<msgExplanation id=\"").append(explanationKey).append("\">").append(explanation).append("</msgExplanation>");
/* 225 */     if ((userResponse != null) && (userResponse.length() != 0))
/* 226 */       buf.append("\n<msgUserResponse id=\"").append(userResponseKey).append("\">").append(userResponse).append("</msgUserResponse>");
/* 227 */     if ((adminResponse != null) && (adminResponse.length() != 0)) {
/* 228 */       buf.append("\n<msgAdministratorResponse id=\"").append(adminResponseKey).append("\">").append(adminResponse).append("</msgAdministratorResponse>");
/*     */     }
/* 230 */     buf.append("\n</msgBody>");
/*     */     
/* 232 */     buf.append("\n</msg>");
/* 233 */     buf.append("\n<?Pub *0000001261?>");
/*     */     
/* 235 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static final String substituteParms(String msg, String parm0, String parm1, String parm2) {
/* 239 */     if (msg == null) {
/* 240 */       return null;
/*     */     }
/* 242 */     msg = msg.replaceAll("\\$\\{0\\}", "<varname>" + parm0 + "</varname>");
/* 243 */     msg = msg.replaceAll("\\{0\\}", "<varname>" + parm0 + "</varname>");
/* 244 */     msg = msg.replaceAll("\\$\\{1\\}", "<varname>" + parm1 + "</varname>");
/* 245 */     msg = msg.replaceAll("\\$\\{2\\}", "<varname>" + parm2 + "</varname>");
/* 246 */     msg = msg.replaceAll("\\{1\\}", "<varname>" + parm1 + "</varname>");
/* 247 */     msg = msg.replaceAll("\\{2\\}", "<varname>" + parm2 + "</varname>");
/* 248 */     return msg;
/*     */   }
/*     */   
/*     */   private static final String closetags(String msg) {
/* 252 */     if (msg == null) {
/* 253 */       return null;
/*     */     }
/* 255 */     String msgout = "";
/* 256 */     StringTokenizer tokenizer = new StringTokenizer(msg, "<", true);
/* 257 */     while (tokenizer.hasMoreTokens()) {
/* 258 */       String token = tokenizer.nextToken();
/* 259 */       if (token.startsWith("p>")) {
/* 260 */         msgout = msgout + token + "</p>";
/* 261 */       } else if (token.startsWith("li>")) {
/* 262 */         msgout = msgout + token + "</li>";
/* 263 */       } else if (token.startsWith("/p>")) {
/* 264 */         msgout = msgout.substring(0, msgout.length() - 1);
/* 265 */         msgout = msgout + token.substring(3);
/* 266 */       } else if (token.startsWith("/li>")) {
/* 267 */         msgout = msgout.substring(0, msgout.length() - 1);
/* 268 */         msgout = msgout + token.substring(4);
/*     */       } else {
/* 270 */         msgout = msgout + token;
/*     */       }
/*     */     }
/* 273 */     return msgout;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\nls\GenerateDitaFromRuntimeMessages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */