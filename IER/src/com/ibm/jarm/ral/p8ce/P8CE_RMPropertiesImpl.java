/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.BinaryList;
/*     */ import com.filenet.api.collection.BooleanList;
/*     */ import com.filenet.api.collection.DateTimeList;
/*     */ import com.filenet.api.collection.DependentObjectList;
/*     */ import com.filenet.api.collection.Float64List;
/*     */ import com.filenet.api.collection.IdList;
/*     */ import com.filenet.api.collection.Integer32List;
/*     */ import com.filenet.api.collection.StringList;
/*     */ import com.filenet.api.core.EngineObject;
/*     */ import com.filenet.api.core.Factory.BinaryList;
/*     */ import com.filenet.api.core.Factory.BooleanList;
/*     */ import com.filenet.api.core.Factory.DateTimeList;
/*     */ import com.filenet.api.core.Factory.Float64List;
/*     */ import com.filenet.api.core.Factory.IdList;
/*     */ import com.filenet.api.core.Factory.StringList;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.Property;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.filenet.apiimpl.property.PropertiesImpl;
/*     */ import com.ibm.jarm.api.constants.DataType;
/*     */ import com.ibm.jarm.api.constants.RMCardinality;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.property.RMProperties;
/*     */ import com.ibm.jarm.api.property.RMProperty;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import com.ibm.jarm.ral.common.RALBaseEntity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class P8CE_RMPropertiesImpl implements RMProperties
/*     */ {
/*  41 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(com.ibm.jarm.api.util.JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */ 
/*     */ 
/*     */   private Properties jaceProperties;
/*     */   
/*     */ 
/*     */   private RALBaseEntity jarmOwningEntity;
/*     */   
/*     */ 
/*     */   private EngineObject jaceEngineObject;
/*     */   
/*     */ 
/*     */ 
/*     */   public P8CE_RMPropertiesImpl()
/*     */   {
/*  57 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/*  59 */     this.jaceProperties = new PropertiesImpl();
/*  60 */     this.jarmOwningEntity = null;
/*  61 */     this.jaceEngineObject = null;
/*  62 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public P8CE_RMPropertiesImpl(Properties jaceProperties, RALBaseEntity owner)
/*     */   {
/*  74 */     Tracer.traceMethodEntry(new Object[] { jaceProperties, owner });
/*  75 */     Util.ckNullObjParam("jaceProperties", jaceProperties);
/*  76 */     this.jaceProperties = jaceProperties;
/*  77 */     this.jarmOwningEntity = owner;
/*  78 */     this.jaceEngineObject = null;
/*  79 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public P8CE_RMPropertiesImpl(EngineObject jaceEngineObject, RALBaseEntity owner)
/*     */   {
/*  90 */     Tracer.traceMethodEntry(new Object[] { jaceEngineObject, owner });
/*  91 */     Util.ckNullObjParam("jaceEngineObject", jaceEngineObject);
/*  92 */     this.jaceProperties = null;
/*  93 */     this.jarmOwningEntity = owner;
/*  94 */     this.jaceEngineObject = jaceEngineObject;
/*  95 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOwningBaseEntity(RALBaseEntity baseEntity)
/*     */   {
/* 107 */     this.jarmOwningEntity = baseEntity;
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
/*     */   private Properties getJaceProperties()
/*     */   {
/* 124 */     Tracer.traceMethodEntry(new Object[0]);
/* 125 */     Properties result = this.jaceProperties;
/* 126 */     if (this.jaceEngineObject != null)
/*     */     {
/* 128 */       result = this.jaceEngineObject.getProperties();
/*     */     }
/*     */     
/* 131 */     Tracer.traceMethodExit(new Object[] { result });
/* 132 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(RMProperties properties)
/*     */   {
/* 140 */     Tracer.traceMethodEntry(new Object[] { properties });
/* 141 */     Util.ckNullObjParam("properties", properties);
/* 142 */     for (RMProperty prop : properties)
/*     */     {
/* 144 */       add(prop);
/*     */     }
/* 146 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(RMProperty property)
/*     */   {
/* 154 */     Tracer.traceMethodEntry(new Object[] { property });
/* 155 */     Util.ckNullObjParam("property", property);
/* 156 */     add(property.getSymbolicName(), property.getDataType(), property.getCardinality(), property.getObjectValue());
/* 157 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(String symbolicName, DataType dataType, RMCardinality cardinality, Object jarmValue)
/*     */   {
/* 166 */     Tracer.traceMethodEntry(new Object[] { symbolicName, dataType, cardinality, jarmValue });
/*     */     try
/*     */     {
/* 169 */       Properties jaceProps = getJaceProperties();
/* 170 */       if (cardinality == RMCardinality.Single)
/*     */       {
/* 172 */         switch (dataType)
/*     */         {
/*     */         case Binary: 
/* 175 */           jaceProps.putValue(symbolicName, (byte[])jarmValue);
/* 176 */           break;
/*     */         
/*     */         case Boolean: 
/* 179 */           jaceProps.putValue(symbolicName, (Boolean)jarmValue);
/* 180 */           break;
/*     */         
/*     */         case DateTime: 
/* 183 */           jaceProps.putValue(symbolicName, (Date)jarmValue);
/* 184 */           break;
/*     */         
/*     */         case Double: 
/* 187 */           jaceProps.putValue(symbolicName, (Double)jarmValue);
/* 188 */           break;
/*     */         
/*     */         case Guid: 
/* 191 */           Id idValue = jarmValue != null ? new Id(jarmValue.toString()) : null;
/* 192 */           jaceProps.putValue(symbolicName, idValue);
/* 193 */           break;
/*     */         
/*     */         case Integer: 
/* 196 */           jaceProps.putValue(symbolicName, (Integer)jarmValue);
/* 197 */           break;
/*     */         
/*     */         case Object: 
/* 200 */           if (jarmValue != null)
/*     */           {
/* 202 */             if ((jarmValue instanceof JaceBasable))
/*     */             {
/* 204 */               EngineObject jaceEngObj = ((JaceBasable)jarmValue).getJaceBaseObject();
/* 205 */               jaceProps.putValue(symbolicName, jaceEngObj);
/*     */             }
/*     */             else
/*     */             {
/* 209 */               jaceProps.putObjectValue(symbolicName, jarmValue);
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/* 214 */             jaceProps.putValue(symbolicName, (EngineObject)null);
/*     */           }
/* 216 */           break;
/*     */         
/*     */         case String: 
/* 219 */           String strValue = jarmValue != null ? jarmValue.toString() : null;
/* 220 */           jaceProps.putValue(symbolicName, strValue);
/*     */         
/*     */ 
/*     */         }
/*     */         
/*     */       } else {
/* 226 */         switch (dataType)
/*     */         {
/*     */ 
/*     */         case Binary: 
/* 230 */           BinaryList jaceValue = null;
/* 231 */           if (jarmValue != null)
/*     */           {
/* 233 */             jaceValue = Factory.BinaryList.createList();
/* 234 */             if ((jarmValue instanceof byte[])) {
/* 235 */               jaceValue.add(jarmValue);
/*     */             } else
/* 237 */               jaceValue.addAll((List)jarmValue);
/*     */           }
/* 239 */           jaceProps.putValue(symbolicName, jaceValue);
/*     */           
/* 241 */           break;
/*     */         
/*     */ 
/*     */         case Boolean: 
/* 245 */           BooleanList jaceValue = null;
/* 246 */           if (jarmValue != null)
/*     */           {
/* 248 */             jaceValue = Factory.BooleanList.createList();
/* 249 */             if ((jarmValue instanceof Boolean)) {
/* 250 */               jaceValue.add(jarmValue);
/*     */             } else
/* 252 */               jaceValue.addAll((List)jarmValue);
/*     */           }
/* 254 */           jaceProps.putValue(symbolicName, jaceValue);
/*     */           
/* 256 */           break;
/*     */         
/*     */ 
/*     */         case DateTime: 
/* 260 */           DateTimeList jaceValue = null;
/* 261 */           if (jarmValue != null)
/*     */           {
/* 263 */             jaceValue = Factory.DateTimeList.createList();
/* 264 */             if ((jarmValue instanceof Date)) {
/* 265 */               jaceValue.add(jarmValue);
/*     */             } else
/* 267 */               jaceValue.addAll((List)jarmValue);
/*     */           }
/* 269 */           jaceProps.putValue(symbolicName, jaceValue);
/*     */           
/*     */ 
/* 272 */           break;
/*     */         
/*     */ 
/*     */         case Double: 
/* 276 */           Float64List jaceValue = null;
/* 277 */           if (jarmValue != null)
/*     */           {
/* 279 */             jaceValue = Factory.Float64List.createList();
/* 280 */             if ((jarmValue instanceof Double)) {
/* 281 */               jaceValue.add(jarmValue);
/*     */             } else
/* 283 */               jaceValue.addAll((List)jarmValue);
/*     */           }
/* 285 */           jaceProps.putValue(symbolicName, jaceValue);
/*     */           
/* 287 */           break;
/*     */         
/*     */ 
/*     */         case Guid: 
/* 291 */           IdList jaceValue = null;
/* 292 */           if (jarmValue != null)
/*     */           {
/* 294 */             jaceValue = Factory.IdList.createList();
/* 295 */             if ((jarmValue instanceof Id))
/*     */             {
/* 297 */               jaceValue.add(jarmValue);
/*     */             }
/* 299 */             else if ((jarmValue instanceof String))
/*     */             {
/* 301 */               jaceValue.add(new Id((String)jarmValue));
/*     */             }
/* 303 */             else if ((jarmValue instanceof List))
/*     */             {
/* 305 */               for (String guid : (List)jarmValue)
/*     */               {
/* 307 */                 jaceValue.add(new Id(guid));
/*     */               }
/*     */             }
/*     */           }
/* 311 */           jaceProps.putValue(symbolicName, jaceValue);
/*     */           
/* 313 */           break;
/*     */         
/*     */ 
/*     */         case Integer: 
/* 317 */           Integer32List jaceValue = null;
/* 318 */           if (jarmValue != null)
/*     */           {
/* 320 */             jaceValue = com.filenet.api.core.Factory.Integer32List.createList();
/* 321 */             if ((jarmValue instanceof Integer)) {
/* 322 */               jaceValue.add(jarmValue);
/*     */             } else
/* 324 */               jaceValue.addAll((List)jarmValue);
/*     */           }
/* 326 */           jaceProps.putValue(symbolicName, jaceValue);
/*     */           
/* 328 */           break;
/*     */         
/*     */ 
/*     */         case Object: 
/* 332 */           if (jarmValue != null)
/*     */           {
/* 334 */             throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "add([mulitvalued-Object])", "RMProperties", symbolicName });
/*     */           }
/*     */           
/*     */ 
/* 338 */           jaceProps.putValue(symbolicName, (DependentObjectList)null);
/*     */           
/*     */ 
/* 341 */           break;
/*     */         
/*     */ 
/*     */         case String: 
/* 345 */           StringList jaceValue = null;
/* 346 */           if (jarmValue != null)
/*     */           {
/* 348 */             jaceValue = Factory.StringList.createList();
/* 349 */             if ((jarmValue instanceof String)) {
/* 350 */               jaceValue.add(jarmValue);
/*     */             } else
/* 352 */               jaceValue.addAll((List)jarmValue);
/*     */           }
/* 354 */           jaceProps.putValue(symbolicName, jaceValue);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/* 360 */       Tracer.traceMethodExit(new Object[0]);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 364 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_ADD_ERROR, new Object[] { symbolicName, dataType.name() });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDirty()
/*     */   {
/* 373 */     return getJaceProperties().isDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperty get(String symbolicName)
/*     */   {
/* 381 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 382 */     Util.ckInvalidStrParam("symbolicName", symbolicName);
/*     */     
/* 384 */     RMProperty result = null;
/*     */     try
/*     */     {
/* 387 */       Property jaceProp = getJaceProperties().get(symbolicName);
/* 388 */       result = new P8CE_RMPropertyImpl(jaceProp, this);
/*     */     }
/*     */     catch (EngineRuntimeException ex)
/*     */     {
/* 392 */       if (ExceptionCode.API_PROPERTY_NOT_IN_CACHE.equals(ex.getExceptionCode()))
/*     */       {
/* 394 */         throw P8CE_Util.processJaceException(ex, RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */       }
/*     */       
/*     */ 
/* 398 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.E_UNEXPECTED_EXCEPTION, new Object[] { ex.getLocalizedMessage() });
/*     */     }
/*     */     
/*     */ 
/* 402 */     Tracer.traceMethodExit(new Object[] { result });
/* 403 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPropertyPresent(String symbolicName)
/*     */   {
/* 411 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 412 */     boolean result = getJaceProperties().isPropertyPresent(symbolicName);
/*     */     
/* 414 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 415 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(String symbolicName)
/*     */   {
/* 423 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 424 */     getJaceProperties().removeFromCache(symbolicName);
/* 425 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(String[] symbolicNames)
/*     */   {
/* 433 */     Tracer.traceMethodEntry((Object[])symbolicNames);
/* 434 */     getJaceProperties().removeFromCache(symbolicNames);
/* 435 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 443 */     return getJaceProperties().size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMProperty[] toArray()
/*     */   {
/* 451 */     Tracer.traceMethodEntry(new Object[0]);
/* 452 */     Property[] jaceProps = getJaceProperties().toArray();
/* 453 */     RMProperty[] jarmProps = new RMProperty[jaceProps.length];
/* 454 */     for (int i = 0; i < jaceProps.length; i++)
/*     */     {
/* 456 */       jarmProps[i] = new P8CE_RMPropertyImpl(jaceProps[i], this);
/*     */     }
/*     */     
/* 459 */     Tracer.traceMethodExit(new Object[] { jarmProps });
/* 460 */     return jarmProps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<RMProperty> iterator()
/*     */   {
/* 469 */     Tracer.traceMethodEntry(new Object[0]);
/* 470 */     List<RMProperty> jarmPropList = new ArrayList(0);
/* 471 */     Property jaceProp = null;
/* 472 */     for (Iterator it = getJaceProperties().iterator(); it.hasNext();)
/*     */     {
/* 474 */       jaceProp = (Property)it.next();
/* 475 */       jarmPropList.add(new P8CE_RMPropertyImpl(jaceProp, this));
/*     */     }
/*     */     
/* 478 */     Iterator<RMProperty> result = jarmPropList.iterator();
/* 479 */     Tracer.traceMethodExit(new Object[] { result });
/* 480 */     return result;
/*     */   }
/*     */   
/*     */   Repository getRepository()
/*     */   {
/* 485 */     Tracer.traceMethodEntry(new Object[0]);
/* 486 */     Repository result = null;
/* 487 */     if (this.jarmOwningEntity != null)
/*     */     {
/* 489 */       result = this.jarmOwningEntity.getRepository();
/*     */     }
/*     */     
/* 492 */     Tracer.traceMethodExit(new Object[] { result });
/* 493 */     return result;
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
/*     */   private RMProperty getJarmProperty(String symbolicName)
/*     */   {
/* 508 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 509 */     RMProperty result = null;
/* 510 */     Properties jaceProps = getJaceProperties();
/* 511 */     if (jaceProps.isPropertyPresent(symbolicName))
/*     */     {
/* 513 */       result = new P8CE_RMPropertyImpl(jaceProps.get(symbolicName), this);
/*     */     }
/*     */     
/* 516 */     Tracer.traceMethodExit(new Object[] { result });
/* 517 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<byte[]> getBinaryListValue(String symbolicName)
/*     */   {
/* 525 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 526 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 527 */     if (jarmProp != null)
/*     */     {
/* 529 */       List<byte[]> result = jarmProp.getBinaryListValue();
/* 530 */       Tracer.traceMethodExit(new Object[] { result });
/* 531 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 535 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getBinaryValue(String symbolicName)
/*     */   {
/* 544 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 545 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 546 */     if (jarmProp != null)
/*     */     {
/* 548 */       byte[] result = jarmProp.getBinaryValue();
/* 549 */       Tracer.traceMethodExit(new Object[] { result });
/* 550 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 554 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Boolean> getBooleanListValue(String symbolicName)
/*     */   {
/* 563 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 564 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 565 */     if (jarmProp != null)
/*     */     {
/* 567 */       List<Boolean> result = jarmProp.getBooleanListValue();
/* 568 */       Tracer.traceMethodExit(new Object[] { result });
/* 569 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 573 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean getBooleanValue(String symbolicName)
/*     */   {
/* 582 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 583 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 584 */     if (jarmProp != null)
/*     */     {
/* 586 */       Boolean result = jarmProp.getBooleanValue();
/* 587 */       Tracer.traceMethodExit(new Object[] { result });
/* 588 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 592 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Date> getDateTimeListValue(String symbolicName)
/*     */   {
/* 601 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 602 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 603 */     if (jarmProp != null)
/*     */     {
/* 605 */       List<Date> result = jarmProp.getDateTimeListValue();
/* 606 */       Tracer.traceMethodExit(new Object[] { result });
/* 607 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 611 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getDateTimeValue(String symbolicName)
/*     */   {
/* 620 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 621 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 622 */     if (jarmProp != null)
/*     */     {
/* 624 */       Date result = jarmProp.getDateTimeValue();
/* 625 */       Tracer.traceMethodExit(new Object[] { result });
/* 626 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 630 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Double> getDoubleListValue(String symbolicName)
/*     */   {
/* 639 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 640 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 641 */     if (jarmProp != null)
/*     */     {
/* 643 */       List<Double> result = jarmProp.getDoubleListValue();
/* 644 */       Tracer.traceMethodExit(new Object[] { result });
/* 645 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 649 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Double getDoubleValue(String symbolicName)
/*     */   {
/* 658 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 659 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 660 */     if (jarmProp != null)
/*     */     {
/* 662 */       Double result = jarmProp.getDoubleValue();
/* 663 */       Tracer.traceMethodExit(new Object[] { result });
/* 664 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 668 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getGuidListValue(String symbolicName)
/*     */   {
/* 677 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 678 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 679 */     if (jarmProp != null)
/*     */     {
/* 681 */       List<String> result = jarmProp.getGuidListValue();
/* 682 */       Tracer.traceMethodExit(new Object[] { result });
/* 683 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 687 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGuidValue(String symbolicName)
/*     */   {
/* 696 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 697 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 698 */     if (jarmProp != null)
/*     */     {
/* 700 */       String result = jarmProp.getGuidValue();
/* 701 */       Tracer.traceMethodExit(new Object[] { result });
/* 702 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 706 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Integer> getIntegerListValue(String symbolicName)
/*     */   {
/* 715 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 716 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 717 */     if (jarmProp != null)
/*     */     {
/* 719 */       List<Integer> result = jarmProp.getIntegerListValue();
/* 720 */       Tracer.traceMethodExit(new Object[] { result });
/* 721 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 725 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getIntegerValue(String symbolicName)
/*     */   {
/* 734 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 735 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 736 */     if (jarmProp != null)
/*     */     {
/* 738 */       Integer result = jarmProp.getIntegerValue();
/* 739 */       Tracer.traceMethodExit(new Object[] { result });
/* 740 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 744 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Object> getObjectListValue(String symbolicName)
/*     */   {
/* 753 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 754 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 755 */     if (jarmProp != null)
/*     */     {
/* 757 */       List<Object> result = jarmProp.getObjectListValue();
/* 758 */       Tracer.traceMethodExit(new Object[] { result });
/* 759 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 763 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getObjectValue(String symbolicName)
/*     */   {
/* 772 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 773 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 774 */     if (jarmProp != null)
/*     */     {
/* 776 */       Object result = jarmProp.getObjectValue();
/* 777 */       Tracer.traceMethodExit(new Object[] { result });
/* 778 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 782 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getStringListValue(String symbolicName)
/*     */   {
/* 791 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 792 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 793 */     if (jarmProp != null)
/*     */     {
/* 795 */       List<String> result = jarmProp.getStringListValue();
/* 796 */       Tracer.traceMethodExit(new Object[] { result });
/* 797 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 801 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStringValue(String symbolicName)
/*     */   {
/* 810 */     Tracer.traceMethodEntry(new Object[] { symbolicName });
/* 811 */     RMProperty jarmProp = getJarmProperty(symbolicName);
/* 812 */     if (jarmProp != null)
/*     */     {
/* 814 */       String result = jarmProp.getStringValue();
/* 815 */       Tracer.traceMethodExit(new Object[] { result });
/* 816 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 820 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_PROPERTY_NOT_IN_CACHE, new Object[] { symbolicName });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putBinaryListValue(String symbolicName, List<byte[]> values)
/*     */   {
/* 831 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 832 */     add(symbolicName, DataType.Binary, RMCardinality.List, values);
/* 833 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putBinaryValue(String symbolicName, byte[] value)
/*     */   {
/* 841 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 842 */     add(symbolicName, DataType.Binary, RMCardinality.Single, value);
/* 843 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putBooleanListValue(String symbolicName, List<Boolean> values)
/*     */   {
/* 851 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 852 */     add(symbolicName, DataType.Boolean, RMCardinality.List, values);
/* 853 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putBooleanValue(String symbolicName, Boolean value)
/*     */   {
/* 861 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 862 */     add(symbolicName, DataType.Boolean, RMCardinality.Single, value);
/* 863 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putDateTimeListValue(String symbolicName, List<Date> values)
/*     */   {
/* 871 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 872 */     add(symbolicName, DataType.DateTime, RMCardinality.List, values);
/* 873 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putDateTimeValue(String symbolicName, Date value)
/*     */   {
/* 881 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 882 */     add(symbolicName, DataType.DateTime, RMCardinality.Single, value);
/* 883 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putDoubleListValue(String symbolicName, List<Double> values)
/*     */   {
/* 891 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 892 */     add(symbolicName, DataType.Double, RMCardinality.List, values);
/* 893 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putDoubleValue(String symbolicName, Double value)
/*     */   {
/* 901 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 902 */     add(symbolicName, DataType.Double, RMCardinality.Single, value);
/* 903 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putGuidListValue(String symbolicName, List<String> values)
/*     */   {
/* 911 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 912 */     add(symbolicName, DataType.Guid, RMCardinality.List, values);
/* 913 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putGuidValue(String symbolicName, String value)
/*     */   {
/* 921 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 922 */     add(symbolicName, DataType.Guid, RMCardinality.Single, value);
/* 923 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putIntegerListValue(String symbolicName, List<Integer> values)
/*     */   {
/* 931 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 932 */     add(symbolicName, DataType.Integer, RMCardinality.List, values);
/* 933 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putIntegerValue(String symbolicName, Integer value)
/*     */   {
/* 941 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 942 */     add(symbolicName, DataType.Integer, RMCardinality.Single, value);
/* 943 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putObjectListValue(String symbolicName, List<Object> values)
/*     */   {
/* 951 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 952 */     add(symbolicName, DataType.Object, RMCardinality.List, values);
/* 953 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putObjectValue(String symbolicName, Object value)
/*     */   {
/* 961 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 962 */     add(symbolicName, DataType.Object, RMCardinality.Single, value);
/* 963 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putStringListValue(String symbolicName, List<String> values)
/*     */   {
/* 971 */     Tracer.traceMethodEntry(new Object[] { symbolicName, values });
/* 972 */     add(symbolicName, DataType.String, RMCardinality.List, values);
/* 973 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putStringValue(String symbolicName, String value)
/*     */   {
/* 981 */     Tracer.traceMethodEntry(new Object[] { symbolicName, value });
/* 982 */     add(symbolicName, DataType.String, RMCardinality.Single, value);
/* 983 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMPropertiesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */