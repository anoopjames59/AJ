/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.filenet.api.core.IndependentlyPersistableObject;
/*     */ import com.filenet.api.core.ObjectReference;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.meta.PropertyDescriptionBinary;
/*     */ import com.filenet.api.meta.PropertyDescriptionDateTime;
/*     */ import com.filenet.api.meta.PropertyDescriptionFloat64;
/*     */ import com.filenet.api.meta.PropertyDescriptionInteger32;
/*     */ import com.filenet.api.meta.PropertyDescriptionString;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.RMLink;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyUtil
/*     */ {
/*     */   public static RMPropertyDescription getPropertyDescription(RMClassDescription classDescription, RMProperty property)
/*     */   {
/*  36 */     String propertyName = property.getSymbolicName();
/*  37 */     List<RMPropertyDescription> propertyDescriptionList = classDescription.getPropertyDescriptions();
/*  38 */     for (RMPropertyDescription propertyDescription : propertyDescriptionList) {
/*  39 */       String descriptionName = propertyDescription.getSymbolicName();
/*  40 */       if (descriptionName.equals(propertyName)) {
/*  41 */         return propertyDescription;
/*     */       }
/*     */     }
/*  44 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getMinAndMaxValuesForProperty(RMPropertyDescription propertyDescription, HttpServletRequest request)
/*     */   {
/*  54 */     WCDateFormat wcDateFormat = WCDateFormat.getDefaultDateFormats(request);
/*  55 */     String[] results = new String[2];
/*     */     
/*  57 */     if ((propertyDescription instanceof PropertyDescriptionDateTime)) {
/*  58 */       PropertyDescriptionDateTime pdDateTime = (PropertyDescriptionDateTime)propertyDescription;
/*  59 */       Date date = pdDateTime.get_PropertyMinimumDateTime();
/*  60 */       if (date != null) {
/*  61 */         results[0] = wcDateFormat.getDateFormat().format(date);
/*     */       }
/*  63 */       date = pdDateTime.get_PropertyMaximumDateTime();
/*  64 */       if (date != null) {
/*  65 */         results[1] = wcDateFormat.getDateFormat().format(date);
/*     */       }
/*     */     }
/*  68 */     else if ((propertyDescription instanceof PropertyDescriptionFloat64)) {
/*  69 */       PropertyDescriptionFloat64 pdFloat64 = (PropertyDescriptionFloat64)propertyDescription;
/*  70 */       if (pdFloat64.get_PropertyMinimumFloat64() != null) {
/*  71 */         results[0] = pdFloat64.get_PropertyMinimumFloat64().toString();
/*     */       } else {
/*  73 */         results[1] = String.valueOf(Double.MIN_VALUE);
/*     */       }
/*  75 */       if (pdFloat64.get_PropertyMaximumFloat64() != null) {
/*  76 */         results[0] = pdFloat64.get_PropertyMaximumFloat64().toString();
/*     */       } else {
/*  78 */         results[1] = String.valueOf(Double.MAX_VALUE);
/*     */       }
/*  80 */     } else if ((propertyDescription instanceof PropertyDescriptionInteger32)) {
/*  81 */       PropertyDescriptionInteger32 pdInteger32 = (PropertyDescriptionInteger32)propertyDescription;
/*  82 */       if (pdInteger32.get_PropertyMinimumInteger32() != null) {
/*  83 */         results[0] = pdInteger32.get_PropertyMinimumInteger32().toString();
/*     */       } else {
/*  85 */         results[1] = String.valueOf(Integer.MIN_VALUE);
/*     */       }
/*  87 */       if (pdInteger32.get_PropertyMaximumInteger32() != null) {
/*  88 */         results[0] = pdInteger32.get_PropertyMaximumInteger32().toString();
/*     */       } else {
/*  90 */         results[1] = String.valueOf(Integer.MAX_VALUE);
/*     */       }
/*     */     }
/*  93 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getMaxEntryForProperty(RMPropertyDescription propertyDescription)
/*     */   {
/* 102 */     if ((propertyDescription instanceof PropertyDescriptionString)) {
/* 103 */       PropertyDescriptionString pdString = (PropertyDescriptionString)propertyDescription;
/* 104 */       if (pdString.get_MaximumLengthString() != null) {
/* 105 */         return pdString.get_MaximumLengthString().intValue();
/*     */       }
/* 107 */     } else if ((propertyDescription instanceof PropertyDescriptionBinary)) {
/* 108 */       PropertyDescriptionBinary pdBinary = (PropertyDescriptionBinary)propertyDescription;
/* 109 */       if (pdBinary.get_MaximumLengthBinary() != null) {
/* 110 */         return pdBinary.get_MaximumLengthBinary().intValue();
/*     */       }
/*     */     }
/*     */     
/* 114 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object getDataTypeOutputFromString(String input, DataType dataType)
/*     */   {
/* 124 */     if (input.length() == 0) {
/* 125 */       return null;
/*     */     }
/* 127 */     if (dataType == DataType.Binary)
/* 128 */       return input.getBytes();
/* 129 */     if (dataType == DataType.Object)
/* 130 */       return input;
/* 131 */     if (dataType == DataType.DateTime)
/*     */       try {
/* 133 */         return DateUtil.parseISODate(input, true);
/*     */       } catch (Exception e) {
/* 135 */         return input;
/*     */       }
/* 137 */     if (dataType == DataType.Double)
/* 138 */       return new Double(input);
/* 139 */     if (dataType == DataType.Integer)
/* 140 */       return new Integer(input);
/* 141 */     if (dataType == DataType.Boolean) {
/* 142 */       return new Boolean(input);
/*     */     }
/* 144 */     if ((dataType == DataType.String) || (dataType == DataType.Guid)) {
/* 145 */       return input;
/*     */     }
/*     */     
/* 148 */     return input;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object getDisplayableValue(RMProperty property, boolean isDocId)
/*     */   {
/*     */     try
/*     */     {
/* 159 */       Object dispValue = "";
/* 160 */       if (property.getDataType() == DataType.Binary) {
/* 161 */         if (property.getCardinality() == RMCardinality.List) {
/* 162 */           List<byte[]> list = property.getBinaryListValue();
/* 163 */           if (list != null) {
/* 164 */             String[] array = new String[list.size()];
/* 165 */             int i = 0;
/* 166 */             for (byte[] item : list) {
/* 167 */               array[i] = new String(item);
/* 168 */               i++;
/*     */             }
/* 170 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 174 */           dispValue = property.getBinaryValue();
/*     */         }
/* 176 */       } else if (property.getDataType() == DataType.Guid) {
/* 177 */         if (property.getCardinality() == RMCardinality.List) {
/* 178 */           List<String> list = property.getGuidListValue();
/* 179 */           if (list != null) {
/* 180 */             String[] array = new String[list.size()];
/* 181 */             int i = 0;
/* 182 */             for (String item : list) {
/* 183 */               array[i] = item;
/* 184 */               i++;
/*     */             }
/* 186 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 190 */           dispValue = property.getGuidValue();
/*     */         }
/* 192 */       } else if (property.getDataType() == DataType.Boolean) {
/* 193 */         if (property.getCardinality() == RMCardinality.List) {
/* 194 */           List<Boolean> list = property.getBooleanListValue();
/* 195 */           if (list != null) {
/* 196 */             String[] array = new String[list.size()];
/* 197 */             int i = 0;
/* 198 */             for (Boolean item : list) {
/* 199 */               array[i] = item.toString();
/* 200 */               i++;
/*     */             }
/* 202 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 206 */           dispValue = property.getBooleanValue();
/*     */         }
/* 208 */       } else if (property.getDataType() == DataType.DateTime) {
/* 209 */         if (property.getCardinality() == RMCardinality.List) {
/* 210 */           List<Date> list = property.getDateTimeListValue();
/* 211 */           if (list != null) {
/* 212 */             String[] array = new String[list.size()];
/* 213 */             int i = 0;
/* 214 */             for (Date item : list) {
/* 215 */               array[i] = DateUtil.getISODateString(item, true);
/* 216 */               i++;
/*     */             }
/* 218 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 222 */           dispValue = DateUtil.getISODateString(property.getDateTimeValue(), true);
/* 223 */         } } else if (property.getDataType() == DataType.Double) {
/* 224 */         if (property.getCardinality() == RMCardinality.List) {
/* 225 */           List<Double> list = property.getDoubleListValue();
/* 226 */           if (list != null) {
/* 227 */             String[] array = new String[list.size()];
/* 228 */             int i = 0;
/* 229 */             for (Double item : list) {
/* 230 */               array[i] = item.toString();
/* 231 */               i++;
/*     */             }
/* 233 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 237 */           dispValue = property.getDoubleValue();
/* 238 */         } } else if (property.getDataType() == DataType.Integer) {
/* 239 */         if (property.getCardinality() == RMCardinality.List) {
/* 240 */           List<Integer> list = property.getIntegerListValue();
/* 241 */           if (list != null) {
/* 242 */             String[] array = new String[list.size()];
/* 243 */             int i = 0;
/* 244 */             for (Integer item : list) {
/* 245 */               array[i] = item.toString();
/* 246 */               i++;
/*     */             }
/* 248 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 252 */           dispValue = property.getIntegerValue();
/* 253 */         } } else if (property.getDataType() == DataType.String) {
/* 254 */         if (property.getCardinality() == RMCardinality.List) {
/* 255 */           List<String> list = property.getStringListValue();
/* 256 */           if (list != null) {
/* 257 */             String[] array = new String[list.size()];
/* 258 */             int i = 0;
/* 259 */             for (String item : list) {
/* 260 */               array[i] = item;
/* 261 */               i++;
/*     */             }
/* 263 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 267 */           dispValue = property.getStringValue();
/* 268 */         } } else if (property.getDataType() == DataType.Object)
/* 269 */         if ((property.getCardinality() == RMCardinality.List) || (property.getCardinality() == RMCardinality.Enumeration)) {
/* 270 */           List<Object> list = property.getObjectListValue();
/* 271 */           if (list != null) {
/* 272 */             String[] array = new String[list.size()];
/* 273 */             int i = 0;
/* 274 */             for (Object item : list) {
/* 275 */               array[i] = objectPropertyToStringValue(item, isDocId);
/* 276 */               i++;
/*     */             }
/* 278 */             dispValue = array;
/*     */           }
/*     */         }
/*     */         else {
/* 282 */           dispValue = objectPropertyToStringValue(property.getObjectValue(), isDocId);
/*     */         }
/* 284 */       if (dispValue == null) {}
/* 285 */       return "";
/*     */     }
/*     */     catch (Exception exp) {}
/*     */     
/* 289 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String objectPropertyToStringValue(Object value, boolean isDocId)
/*     */   {
/* 299 */     if (value != null) {
/* 300 */       if ((value instanceof RMLink)) {
/* 301 */         BaseEntity tail = ((RMLink)value).getTail();
/* 302 */         if (tail != null) {
/* 303 */           return tail.getName();
/*     */         }
/*     */       } else {
/* 306 */         if ((value instanceof BaseEntity)) {
/* 307 */           BaseEntity objProp = (BaseEntity)value;
/* 308 */           return isDocId ? IERUtil.getDocId(objProp) : objProp.getObjectIdentity(); }
/* 309 */         if ((value instanceof RMClassDescription)) {
/* 310 */           RMClassDescription cd = (RMClassDescription)value;
/* 311 */           return cd.getId();
/*     */         }
/*     */       }
/*     */     }
/* 315 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isNumericDataType(RMPropertyDescription propertyDescription)
/*     */   {
/* 324 */     DataType dataType = propertyDescription.getDataType();
/* 325 */     if (dataType == DataType.Double) {
/* 326 */       return true;
/*     */     }
/* 328 */     return false;
/*     */   }
/*     */   
/*     */   public static Object getPropertyValue(Object hit, String propName) {
/* 332 */     Object value = null;
/* 333 */     if ((hit instanceof IndependentlyPersistableObject)) {
/* 334 */       IndependentlyPersistableObject object = (IndependentlyPersistableObject)hit;
/* 335 */       if (propName == "AccessMask") {
/* 336 */         value = object.getAccessAllowed();
/* 337 */       } else if (propName.equals("Id")) {
/* 338 */         value = object.getObjectReference().getObjectIdentity();
/*     */       } else {
/* 340 */         Properties props = object.getProperties();
/*     */         try {
/* 342 */           if (props.isPropertyPresent(propName)) {
/* 343 */             value = props.getObjectValue(propName);
/*     */           } else {
/* 345 */             object.fetchProperties(new String[] { propName });
/* 346 */             value = object.getProperties().getObjectValue(propName);
/*     */           }
/*     */         }
/*     */         catch (EngineRuntimeException e) {}
/*     */       }
/*     */     }
/* 352 */     return value;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\PropertyUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */