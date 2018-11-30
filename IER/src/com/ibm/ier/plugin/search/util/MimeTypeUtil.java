/*     */ package com.ibm.ier.plugin.search.util;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class MimeTypeUtil
/*     */ {
/*     */   private static class MimeTypeDef
/*     */   {
/*     */     private String[] mimeTypes;
/*     */     private String[] extensions;
/*     */     private String fileType;
/*     */     private String iconName;
/*     */     
/*     */     MimeTypeDef(String[] mimeTypes, String[] extensions, String fileType, String iconName)
/*     */     {
/*  35 */       this.mimeTypes = mimeTypes;
/*  36 */       this.extensions = extensions;
/*  37 */       this.fileType = fileType;
/*  38 */       this.iconName = iconName;
/*     */     }
/*     */     
/*     */     public String[] getMimeTypes() {
/*  42 */       return this.mimeTypes;
/*     */     }
/*     */     
/*     */     public String getDefaultMimeType() {
/*  46 */       if ((this.mimeTypes == null) || (this.mimeTypes.length == 0)) {
/*  47 */         return null;
/*     */       }
/*     */       
/*  50 */       return this.mimeTypes[0];
/*     */     }
/*     */     
/*     */     public String[] getExtensions() {
/*  54 */       return this.extensions;
/*     */     }
/*     */     
/*     */     public String getDefaultExtension() {
/*  58 */       if ((this.extensions == null) || (this.extensions.length == 0)) {
/*  59 */         return null;
/*     */       }
/*     */       
/*  62 */       return this.extensions[0];
/*     */     }
/*     */     
/*     */     public String getFileType() {
/*  66 */       return this.fileType;
/*     */     }
/*     */     
/*     */     public String getIconName() {
/*  70 */       return this.iconName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private static MimeTypeDef[] mimeTypeDefs = { new MimeTypeDef(new String[] { "application/afp", "application/vnd.ibm.afplinedata" }, new String[] { "afp" }, "file.type.afp.document", "icon.mime.afp"), new MimeTypeDef(new String[] { "application/dicom" }, new String[] { "dcm" }, "file.type.dicom.document", "icon.mime.default"), new MimeTypeDef(new String[] { "application/dca-rft" }, new String[] { "ftf" }, "file.type.richtext.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/dwg" }, new String[] { "dwg" }, "file.type.dwg.document", "icon.mime.default"), new MimeTypeDef(new String[] { "application/line", "application/lin" }, new String[] { "lin" }, "file.type.line.data.document", "icon.mime.linedata"), new MimeTypeDef(new String[] { "application/msaccess" }, new String[] { "mdb", "accdb" }, "file.type.unknown", "icon.mime.default"), new MimeTypeDef(new String[] { "application/msword" }, new String[] { "doc", "dot" }, "file.type.msword.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/octet-stream" }, new String[] { "bin", "dms", "lha", "lzh", "exe", "class" }, "file.type.unknown", "icon.mime.default"), new MimeTypeDef(new String[] { "application/pdf" }, new String[] { "pdf" }, "file.type.pdf.document", "icon.mime.pdf"), new MimeTypeDef(new String[] { "application/rss+xml" }, new String[] { "rss" }, "file.type.unknown", "icon.mime.default"), new MimeTypeDef(new String[] { "application/vnd.framemaker" }, new String[] { "fm", "frm" }, "file.type.framemaker.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/vnd.ibm.modcap" }, new String[] { "mda" }, "file.type.modca.document", "icon.mime.modca"), new MimeTypeDef(new String[] { "application/vnd.lotus-1-2-3" }, new String[] { "123", "wk4", "wk3", "wk1", "wks", "wg1" }, "file.type.lotus123.document", "icon.mime.123"), new MimeTypeDef(new String[] { "application/vnd.lotus-freelance" }, new String[] { "prz", "pre" }, "file.type.lotusfreelance.document", "icon.mime.lprz"), new MimeTypeDef(new String[] { "application/vnd.lotus-wordpro" }, new String[] { "lwp", "sam", "mwp", "smm" }, "file.type.lotuswordpro.document", "icon.mime.lwp"), new MimeTypeDef(new String[] { "application/vnd.ms-excel" }, new String[] { "xls", "xlt", "xlm", "xld", "xla", "xlc", "xlw", "xll" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.ms-excel.addin.macroenabled.12" }, new String[] { "xlam" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.ms-excel.sheet.binary.macroenabled.12" }, new String[] { "xlsb" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.ms-excel.sheet.macroenabled.12" }, new String[] { "xlsm" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.ms-excel.template.macroenabled.12" }, new String[] { "xltm" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.ms-outlook" }, new String[] { "msg" }, "file.type.msoutlook.document", "icon.mime.msoutlook"), new MimeTypeDef(new String[] { "application/vnd.ms-powerpoint" }, new String[] { "ppt", "pot", "ppa", "pps", "ppz", "pwz" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.ms-powerpoint.addin.macroenabled.12" }, new String[] { "ppam" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.ms-powerpoint.presentation.macroenabled.12" }, new String[] { "pptm" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.ms-powerpoint.slideshow.macroenabled.12" }, new String[] { "ppsm" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.ms-powerpoint.template.macroenabled.12" }, new String[] { "potm" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.ms-word.document.macroenabled.12" }, new String[] { "docm" }, "file.type.msword.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/vnd.ms-word.template.macroenabled.12" }, new String[] { "dotm" }, "file.type.msword.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/vnd.oasis.opendocument.presentation" }, new String[] { "odp" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.oasis.opendocument.spreadsheet" }, new String[] { "ods" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.oasis.opendocument.text" }, new String[] { "odt" }, "file.type.msword.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/vnd.openxmlformats-officedocument.presentationml.presentation" }, new String[] { "pptx" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.openxmlformats-officedocument.presentationml.slideshow" }, new String[] { "ppsx" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.openxmlformats-officedocument.presentationml.template" }, new String[] { "potx" }, "file.type.mspowerpoint.document", "icon.mime.mspp"), new MimeTypeDef(new String[] { "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" }, new String[] { "xlsx" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.openxmlformats-officedocument.spreadsheetml.template" }, new String[] { "xltx" }, "file.type.msexcel.document", "icon.mime.msexcel"), new MimeTypeDef(new String[] { "application/vnd.openxmlformats-officedocument.wordprocessingml.document" }, new String[] { "docx" }, "file.type.msword.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/vnd.openxmlformats-officedocument.wordprocessingml.template" }, new String[] { "dotx" }, "file.type.msword.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/vnd.visio" }, new String[] { "vsd" }, "file.type.visio.document", "icon.mime.visio"), new MimeTypeDef(new String[] { "application/wordperfect5.1" }, new String[] { "wp", "wpd", "w51" }, "file.type.wordperfect.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "audio/basic" }, new String[] { "au", "snd", "ulw" }, "file.type.audio", "icon.mime.audio"), new MimeTypeDef(new String[] { "audio/mpeg" }, new String[] { "mp3", "m1s", "m1a", "mp2", "mpm", "mpa", "mpga" }, "file.type.audio", "icon.mime.audio"), new MimeTypeDef(new String[] { "audio/vnd.qcelp" }, new String[] { "qcp" }, "file.type.audio", "icon.mime.audio"), new MimeTypeDef(new String[] { "audio/x-aiff" }, new String[] { "aif", "aiff", "aifc" }, "file.type.audio", "icon.mime.audio"), new MimeTypeDef(new String[] { "audio/x-midi" }, new String[] { "midi", "mid", "smf", "kar" }, "file.type.audio", "icon.mime.audio"), new MimeTypeDef(new String[] { "audio/x-ms-wma" }, new String[] { "wma" }, "file.type.audio", "icon.mime.audio"), new MimeTypeDef(new String[] { "audio/x-wav" }, new String[] { "wav" }, "file.type.audio", "icon.mime.audio"), new MimeTypeDef(new String[] { "image/bmp" }, new String[] { "bmp", "dib" }, "file.type.bitmap.image", "icon.mime.image"), new MimeTypeDef(new String[] { "image/cals" }, new String[] { "cals" }, "file.type.cals.image", "icon.mime.image"), new MimeTypeDef(new String[] { "image/x-dcx", "image/dcx" }, new String[] { "dcx" }, "file.type.dcx.image", "icon.mime.image"), new MimeTypeDef(new String[] { "image/g3fax" }, new String[] { "g3f" }, "file.type.g3fax", "icon.mime.image"), new MimeTypeDef(new String[] { "image/gif" }, new String[] { "gif" }, "file.type.gif.image", "icon.mime.image"), new MimeTypeDef(new String[] { "image/jpg", "image/jpeg" }, new String[] { "jpg", "jpeg", "jpe", "jfif", "pjpeg", "pjp" }, "file.type.jpeg.image", "icon.mime.image"), new MimeTypeDef(new String[] { "image/x-pcx", "image/pcx" }, new String[] { "pcx" }, "file.type.pcx.image", "icon.mime.image"), new MimeTypeDef(new String[] { "image/png", "image/x-png" }, new String[] { "png" }, "file.type.png.image", "icon.mime.image"), new MimeTypeDef(new String[] { "image/tiff", "image/tif", "image/tiff1stp" }, new String[] { "tiff", "tif" }, "file.type.tiff.image", "icon.mime.image"), new MimeTypeDef(new String[] { "text/html", "text/htm" }, new String[] { "htm", "html", "shtml", "plg" }, "file.type.html.document", "icon.mime.web"), new MimeTypeDef(new String[] { "text/plain" }, new String[] { "txt", "text", "log" }, "file.type.text.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "application/rtf", "text/richtext" }, new String[] { "rtf" }, "file.type.msword.document", "icon.mime.msword"), new MimeTypeDef(new String[] { "text/xml" }, new String[] { "xml", "dtd" }, "file.type.xml.document", "icon.mime.xml"), new MimeTypeDef(new String[] { "video/mpeg" }, new String[] { "mpg", "mpeg", "mpe", "m1v", "m75", "m15" }, "file.type.video", "icon.mime.streaming.video"), new MimeTypeDef(new String[] { "video/x-msvideo", "video/msvideo", "video/x-ibm-ivsplugin" }, new String[] { "avi", "vfw" }, "file.type.video", "icon.mime.streaming.video"), new MimeTypeDef(new String[] { "video/quicktime" }, new String[] { "mov", "qt" }, "file.type.video", "icon.mime.streaming.video"), new MimeTypeDef(new String[] { "video/x-ms-asf" }, new String[] { "asf", "asx" }, "file.type.video", "icon.mime.streaming.video"), new MimeTypeDef(new String[] { "video/x-ms-wmv" }, new String[] { "wmv" }, "file.type.video", "icon.mime.streaming.video"), new MimeTypeDef(new String[] { "application/x-filenet-workflowdefinition" }, new String[] { "pep" }, "file.type.workflow", "icon.mime.workflow"), new MimeTypeDef(new String[] { "application/x-filenet-xpdlworkflowdefinition" }, new String[] { "xpdl" }, "file.type.workflow", "icon.mime.workflow"), new MimeTypeDef(new String[] { "application/x-filenet-itxformtemplate" }, new String[] { "itx" }, "file.type.form", "icon.mime.form"), new MimeTypeDef(new String[] { "application/vnd.xfdl.design" }, new String[] { "xfdd" }, "file.type.form", "icon.mime.form") };
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
/* 151 */   public static Map<String, MimeTypeDef> extToMimeTypeDef = Collections.unmodifiableMap(new HashMap() {});
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */   public static Map<String, MimeTypeDef> mimeTypeToMimeTypeDef = Collections.unmodifiableMap(new HashMap() {});
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
/*     */   public static String getExtFromMimeType(String mimeType)
/*     */   {
/* 179 */     String extension = null;
/* 180 */     if (mimeType != null) {
/* 181 */       MimeTypeDef mimeTypeDef = (MimeTypeDef)mimeTypeToMimeTypeDef.get(mimeType.toLowerCase());
/*     */       
/* 183 */       if (mimeTypeDef != null) {
/* 184 */         extension = mimeTypeDef.getDefaultExtension();
/*     */       }
/*     */     }
/* 187 */     if ((extension == null) || (extension.length() == 0)) {
/* 188 */       extension = ".dat";
/*     */     }
/*     */     
/* 191 */     return extension;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getMimeType(String extension)
/*     */   {
/* 203 */     String mimeType = null;
/*     */     
/* 205 */     if (extension.startsWith(".")) {
/* 206 */       extension = extension.substring(1);
/*     */     }
/*     */     
/* 209 */     MimeTypeDef mimeTypeDef = (MimeTypeDef)extToMimeTypeDef.get(extension.toLowerCase());
/*     */     
/* 211 */     if (mimeTypeDef != null) {
/* 212 */       mimeType = mimeTypeDef.getDefaultMimeType();
/*     */     }
/*     */     
/* 215 */     if ((mimeType == null) || (mimeType.length() == 0)) {
/* 216 */       mimeType = "application/octet-stream";
/*     */     }
/*     */     
/* 219 */     return mimeType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getMimeTypeNoDefault(String extension)
/*     */   {
/* 231 */     String mimeType = null;
/*     */     
/* 233 */     if (extension.startsWith(".")) {
/* 234 */       extension = extension.substring(1);
/*     */     }
/*     */     
/* 237 */     MimeTypeDef mimeTypeDef = (MimeTypeDef)extToMimeTypeDef.get(extension.toLowerCase());
/*     */     
/* 239 */     if (mimeTypeDef != null) {
/* 240 */       mimeType = mimeTypeDef.getDefaultMimeType();
/*     */     }
/*     */     
/* 243 */     return mimeType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getMimeTypes(String extension)
/*     */   {
/* 254 */     String[] mimeTypes = null;
/*     */     
/* 256 */     if (extension.startsWith(".")) {
/* 257 */       extension = extension.substring(1);
/*     */     }
/*     */     
/* 260 */     MimeTypeDef mimeTypeDef = (MimeTypeDef)extToMimeTypeDef.get(extension.toLowerCase());
/*     */     
/* 262 */     if (mimeTypeDef != null) {
/* 263 */       mimeTypes = mimeTypeDef.getMimeTypes();
/*     */     }
/*     */     
/* 266 */     if ((mimeTypes == null) || (mimeTypes.length == 0)) {
/* 267 */       mimeTypes = new String[] { "application/octet-stream" };
/*     */     }
/*     */     
/* 270 */     return mimeTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getDescriptionFromMimeType(String mimeType)
/*     */   {
/* 280 */     String fileType = null;
/* 281 */     MimeTypeDef mimeTypeDef = (MimeTypeDef)mimeTypeToMimeTypeDef.get(mimeType.toLowerCase());
/*     */     
/* 283 */     if (mimeTypeDef != null) {
/* 284 */       fileType = mimeTypeDef.getFileType();
/*     */     }
/*     */     
/* 287 */     if ((fileType == null) || (fileType.length() == 0)) {
/* 288 */       if (mimeType.indexOf("audio") != -1) {
/* 289 */         fileType = "file.type.audio";
/* 290 */       } else if (mimeType.indexOf("video") != -1) {
/* 291 */         fileType = "file.type.video";
/*     */       } else {
/* 293 */         fileType = "file.type.unknown";
/*     */       }
/*     */     }
/*     */     
/* 297 */     return fileType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getIconNameFromMimeType(String mimeType)
/*     */   {
/* 307 */     String iconName = null;
/* 308 */     MimeTypeDef mimeTypeDef = (MimeTypeDef)mimeTypeToMimeTypeDef.get(mimeType.toLowerCase());
/*     */     
/* 310 */     if (mimeTypeDef != null) {
/* 311 */       iconName = mimeTypeDef.getIconName();
/*     */     }
/*     */     
/* 314 */     if ((iconName == null) || (iconName.length() == 0)) {
/* 315 */       if (mimeType.startsWith("image")) {
/* 316 */         iconName = "icon.mime.image";
/* 317 */       } else if (mimeType.indexOf("audio") != -1) {
/* 318 */         iconName = "icon.mime.audio";
/* 319 */       } else if (mimeType.indexOf("video") != -1) {
/* 320 */         iconName = "icon.mime.streaming.video";
/*     */       } else {
/* 322 */         iconName = "icon.mime.default";
/*     */       }
/*     */     }
/*     */     
/* 326 */     return iconName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getMimeTypeForOD(String mimeType)
/*     */   {
/* 337 */     int extIndex = mimeType.indexOf("extension-field=");
/* 338 */     if (extIndex != -1) {
/* 339 */       String itemMimeExtension = mimeType.substring(extIndex + 16);
/* 340 */       mimeType = getMimeType(itemMimeExtension);
/*     */     }
/*     */     
/* 343 */     return mimeType;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\util\MimeTypeUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */