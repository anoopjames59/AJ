/*      */ package com.ibm.jarm.ral.p8ce;
/*      */ 
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.BinaryList;
/*      */ import com.filenet.api.collection.BooleanList;
/*      */ import com.filenet.api.collection.DateTimeList;
/*      */ import com.filenet.api.collection.DependentObjectList;
/*      */ import com.filenet.api.collection.EngineCollection;
/*      */ import com.filenet.api.collection.Float64List;
/*      */ import com.filenet.api.collection.IdList;
/*      */ import com.filenet.api.collection.IndependentObjectSet;
/*      */ import com.filenet.api.collection.Integer32List;
/*      */ import com.filenet.api.collection.StringList;
/*      */ import com.filenet.api.core.EngineObject;
/*      */ import com.filenet.api.core.Factory.BinaryList;
/*      */ import com.filenet.api.core.Factory.BooleanList;
/*      */ import com.filenet.api.core.Factory.DateTimeList;
/*      */ import com.filenet.api.core.Factory.Float64List;
/*      */ import com.filenet.api.core.Factory.IdList;
/*      */ import com.filenet.api.core.Factory.Integer32List;
/*      */ import com.filenet.api.core.Factory.StringList;
/*      */ import com.filenet.api.core.IndependentObject;
/*      */ import com.filenet.api.exception.EngineRuntimeException;
/*      */ import com.filenet.api.property.Property;
/*      */ import com.filenet.api.property.PropertyBinary;
/*      */ import com.filenet.api.property.PropertyBinaryList;
/*      */ import com.filenet.api.property.PropertyBoolean;
/*      */ import com.filenet.api.property.PropertyBooleanList;
/*      */ import com.filenet.api.property.PropertyDateTime;
/*      */ import com.filenet.api.property.PropertyDateTimeList;
/*      */ import com.filenet.api.property.PropertyDependentObjectList;
/*      */ import com.filenet.api.property.PropertyEngineObject;
/*      */ import com.filenet.api.property.PropertyFloat64;
/*      */ import com.filenet.api.property.PropertyFloat64List;
/*      */ import com.filenet.api.property.PropertyId;
/*      */ import com.filenet.api.property.PropertyIdList;
/*      */ import com.filenet.api.property.PropertyIndependentObjectSet;
/*      */ import com.filenet.api.property.PropertyInteger32;
/*      */ import com.filenet.api.property.PropertyInteger32List;
/*      */ import com.filenet.api.property.PropertyString;
/*      */ import com.filenet.api.property.PropertyStringList;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.jarm.api.constants.DataType;
/*      */ import com.ibm.jarm.api.constants.RMCardinality;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.exception.RMErrorCode;
/*      */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*      */ import com.ibm.jarm.api.property.RMProperty;
/*      */ import com.ibm.jarm.api.security.RMPermission;
/*      */ import com.ibm.jarm.api.util.JarmTracer;
/*      */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*      */ import com.ibm.jarm.api.util.Util;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ class P8CE_RMPropertyImpl
/*      */   implements RMProperty
/*      */ {
/*   63 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*      */   
/*   65 */   private static SimpleDateFormat W3CDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
/*      */   
/*      */ 
/*      */   private String symbolicName;
/*      */   
/*      */ 
/*      */   private DataType dataType;
/*      */   
/*      */   private RMCardinality cardinality;
/*      */   
/*      */   private Property jaceProperty;
/*      */   
/*      */   private P8CE_RMPropertiesImpl parentCollection;
/*      */   
/*      */ 
/*      */   P8CE_RMPropertyImpl(Property jaceProperty, P8CE_RMPropertiesImpl parentCollection)
/*      */   {
/*   82 */     Tracer.traceMethodEntry(new Object[] { jaceProperty, parentCollection });
/*   83 */     Util.ckNullObjParam("jaceProperty", jaceProperty);
/*      */     try
/*      */     {
/*   86 */       this.parentCollection = parentCollection;
/*   87 */       injestJaceProperty(jaceProperty);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*   91 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_INGESTION_ERR, new Object[] { this.symbolicName });
/*      */     }
/*      */     
/*   94 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RMCardinality getCardinality()
/*      */   {
/*  102 */     return this.cardinality;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DataType getDataType()
/*      */   {
/*  110 */     return this.dataType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSymbolicName()
/*      */   {
/*  118 */     return this.symbolicName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isDirty()
/*      */   {
/*  126 */     return this.jaceProperty.isDirty();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSettable()
/*      */   {
/*  134 */     return this.jaceProperty.isSettable();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] getBinaryValue()
/*      */   {
/*  143 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  146 */       byte[] result = this.jaceProperty.getBinaryValue();
/*  147 */       Tracer.traceMethodExit(new Object[] { result });
/*  148 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  152 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "byte[]" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<byte[]> getBinaryListValue()
/*      */   {
/*  162 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  165 */       List<byte[]> result = this.jaceProperty.getBinaryListValue();
/*  166 */       Tracer.traceMethodExit(new Object[] { result });
/*  167 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  171 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<byte[]>" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Boolean getBooleanValue()
/*      */   {
/*  180 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  183 */       Boolean result = this.jaceProperty.getBooleanValue();
/*  184 */       Tracer.traceMethodExit(new Object[] { result });
/*  185 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  189 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Boolean" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Boolean> getBooleanListValue()
/*      */   {
/*  199 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  202 */       List<Boolean> result = this.jaceProperty.getBooleanListValue();
/*  203 */       Tracer.traceMethodExit(new Object[] { result });
/*  204 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  208 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Boolean>" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getDateTimeValue()
/*      */   {
/*  217 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  220 */       Date result = this.jaceProperty.getDateTimeValue();
/*  221 */       Tracer.traceMethodExit(new Object[] { result });
/*  222 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  226 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Date" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Date> getDateTimeListValue()
/*      */   {
/*  236 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  239 */       List<Date> result = this.jaceProperty.getDateTimeListValue();
/*  240 */       Tracer.traceMethodExit(new Object[] { result });
/*  241 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  245 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Date>" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Double getDoubleValue()
/*      */   {
/*  254 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  257 */       Double result = this.jaceProperty.getFloat64Value();
/*  258 */       Tracer.traceMethodExit(new Object[] { result });
/*  259 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  263 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Double" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Double> getDoubleListValue()
/*      */   {
/*  273 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  276 */       List<Double> result = this.jaceProperty.getFloat64ListValue();
/*  277 */       Tracer.traceMethodExit(new Object[] { result });
/*  278 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  282 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Double>" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getGuidValue()
/*      */   {
/*  291 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  294 */       Id idValue = this.jaceProperty.getIdValue();
/*  295 */       String result = idValue != null ? idValue.toString() : null;
/*  296 */       Tracer.traceMethodExit(new Object[] { result });
/*  297 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  301 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "String" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getGuidListValue()
/*      */   {
/*  311 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  314 */       ArrayList<String> resultList = null;
/*  315 */       IdList idListValue = this.jaceProperty.getIdListValue();
/*  316 */       Id idEntry; Iterator it; if (idListValue != null)
/*      */       {
/*  318 */         resultList = new ArrayList(idListValue.size());
/*  319 */         idEntry = null;
/*  320 */         for (it = idListValue.iterator(); it.hasNext();)
/*      */         {
/*  322 */           idEntry = (Id)it.next();
/*  323 */           if (idEntry != null) {
/*  324 */             resultList.add(idEntry.toString());
/*      */           }
/*      */         }
/*      */       }
/*  328 */       Tracer.traceMethodExit(new Object[] { resultList });
/*  329 */       return resultList;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  333 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<String>" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getIntegerValue()
/*      */   {
/*  342 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  345 */       Integer result = this.jaceProperty.getInteger32Value();
/*  346 */       Tracer.traceMethodExit(new Object[] { result });
/*  347 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  351 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Integer" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Integer> getIntegerListValue()
/*      */   {
/*  361 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  364 */       List<Integer> result = this.jaceProperty.getInteger32ListValue();
/*  365 */       Tracer.traceMethodExit(new Object[] { result });
/*  366 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  370 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Integer>" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getObjectValue()
/*      */   {
/*  380 */     Tracer.traceMethodEntry(new Object[0]);
/*  381 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  384 */       Object result = null;
/*  385 */       IdList idList; if (this.cardinality == RMCardinality.Single)
/*      */       {
/*  387 */         switch (this.dataType)
/*      */         {
/*      */         case Binary: 
/*  390 */           result = this.jaceProperty.getBinaryValue();
/*  391 */           break;
/*      */         case Boolean: 
/*  393 */           result = this.jaceProperty.getBooleanValue();
/*  394 */           break;
/*      */         case DateTime: 
/*  396 */           result = this.jaceProperty.getDateTimeValue();
/*  397 */           break;
/*      */         case Double: 
/*  399 */           result = this.jaceProperty.getFloat64Value();
/*  400 */           break;
/*      */         case Guid: 
/*  402 */           Id idValue = this.jaceProperty.getIdValue();
/*  403 */           result = idValue != null ? idValue.toString() : null;
/*  404 */           break;
/*      */         case Integer: 
/*  406 */           result = this.jaceProperty.getInteger32Value();
/*  407 */           break;
/*      */         case Object: 
/*  409 */           establishedSubject = P8CE_Util.associateSubject();
/*  410 */           Object objValue = this.jaceProperty.getEngineObjectValue();
/*  411 */           Repository repository = getRepository();
/*  412 */           result = P8CE_Util.convertJaceObjToJarmObject(repository, objValue);
/*  413 */           break;
/*      */         case String: 
/*  415 */           result = this.jaceProperty.getStringValue();
/*      */         
/*      */         }
/*      */         
/*      */       } else {
/*  420 */         switch (this.dataType)
/*      */         {
/*      */         case Binary: 
/*  423 */           result = this.jaceProperty.getBinaryListValue();
/*  424 */           break;
/*      */         case Boolean: 
/*  426 */           result = this.jaceProperty.getBooleanListValue();
/*  427 */           break;
/*      */         case DateTime: 
/*  429 */           result = this.jaceProperty.getDateTimeListValue();
/*  430 */           break;
/*      */         case Double: 
/*  432 */           result = this.jaceProperty.getFloat64ListValue();
/*  433 */           break;
/*      */         case Guid: 
/*  435 */           idList = this.jaceProperty.getIdListValue();
/*  436 */           if (idList != null)
/*      */           {
/*  438 */             List<String> guidList = new ArrayList(idList.size());
/*  439 */             for (Iterator it = idList.iterator(); it.hasNext();)
/*      */             {
/*  441 */               Id id = (Id)it.next();
/*  442 */               if (id != null)
/*  443 */                 guidList.add(id.toString());
/*      */             }
/*  445 */             result = guidList; }
/*  446 */           break;
/*      */         
/*      */         case Integer: 
/*  449 */           result = this.jaceProperty.getInteger32ListValue();
/*  450 */           break;
/*      */         case Object: 
/*  452 */           establishedSubject = P8CE_Util.associateSubject();
/*  453 */           result = getObjectListValue();
/*  454 */           break;
/*      */         case String: 
/*  456 */           result = this.jaceProperty.getStringListValue();
/*      */         }
/*      */         
/*      */       }
/*  460 */       Tracer.traceMethodExit(new Object[] { result });
/*  461 */       return (IdList)result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  465 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Object" });
/*      */     }
/*      */     finally
/*      */     {
/*  469 */       if (establishedSubject) {
/*  470 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Object> getObjectListValue()
/*      */   {
/*  480 */     Tracer.traceMethodEntry(new Object[0]);
/*  481 */     boolean establishedSubject = false;
/*      */     try
/*      */     {
/*  484 */       establishedSubject = P8CE_Util.associateSubject();
/*  485 */       List<Object> result = new ArrayList();
/*      */       
/*  487 */       Object jaceValue = this.jaceProperty.getObjectValue();
/*  488 */       IndependentObject indepObj; if (jaceValue != null)
/*      */       {
/*  490 */         if (((jaceValue instanceof EngineCollection)) && (!((EngineCollection)jaceValue).isEmpty()))
/*      */         {
/*  492 */           if ((jaceValue instanceof DependentObjectList))
/*      */           {
/*  494 */             if ((jaceValue instanceof AccessPermissionList))
/*      */             {
/*  496 */               List<RMPermission> jarmPerms = P8CE_Util.convertToJarmPermissions((AccessPermissionList)jaceValue);
/*  497 */               for (RMPermission jarmPerm : jarmPerms)
/*      */               {
/*  499 */                 result.add(jarmPerm);
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  504 */               Tracer.traceMinimumMsg("Unsupported P8CE DependentObjectType [{0}] for property ''{1}''.", new Object[] { jaceValue.getClass().toString(), getSymbolicName() });
/*      */             }
/*      */             
/*      */           }
/*  508 */           else if ((jaceValue instanceof IndependentObjectSet))
/*      */           {
/*  510 */             indepObj = null;
/*  511 */             Iterator<IndependentObject> it = ((IndependentObjectSet)jaceValue).iterator();
/*  512 */             while ((it != null) && (it.hasNext()))
/*      */             {
/*  514 */               indepObj = (IndependentObject)it.next();
/*  515 */               Repository repository = getRepository();
/*  516 */               Object jarmEntity = P8CE_Util.convertJaceObjToJarmObject(repository, indepObj);
/*  517 */               if (jarmEntity != null) {
/*  518 */                 result.add(jarmEntity);
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/*  523 */             result.addAll((List)jaceValue);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  528 */       Tracer.traceMethodExit(new Object[] { result });
/*  529 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  533 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Object>" });
/*      */     }
/*      */     finally
/*      */     {
/*  537 */       if (establishedSubject) {
/*  538 */         P8CE_Util.disassociateSubject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getStringValue()
/*      */   {
/*  547 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  550 */       String result = null;
/*  551 */       Object rawObjValue = this.jaceProperty.getObjectValue();
/*  552 */       if ((this.dataType == DataType.DateTime) && ((rawObjValue instanceof Date)))
/*      */       {
/*  554 */         result = W3CDateFormatter.format((Date)rawObjValue);
/*      */       }
/*  556 */       else if ((this.dataType == DataType.Binary) && ((rawObjValue instanceof byte[])))
/*      */       {
/*  558 */         result = new String((byte[])rawObjValue);
/*      */       } else {
/*  560 */         if (this.dataType == DataType.Object)
/*      */         {
/*  562 */           throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "String" });
/*      */         }
/*  564 */         if (rawObjValue != null)
/*      */         {
/*  566 */           result = rawObjValue.toString();
/*      */         }
/*      */       }
/*  569 */       Tracer.traceMethodExit(new Object[] { result });
/*  570 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  574 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "String" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getStringListValue()
/*      */   {
/*  584 */     Tracer.traceMethodEntry(new Object[0]);
/*      */     try
/*      */     {
/*  587 */       List<String> result = this.jaceProperty.getStringListValue();
/*  588 */       Tracer.traceMethodExit(new Object[] { result });
/*  589 */       return result;
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  593 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<String>" });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBinaryValue(byte[] value)
/*      */   {
/*  606 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/*  609 */       if (this.dataType == DataType.Binary)
/*      */       {
/*  611 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/*  613 */           ((PropertyBinary)this.jaceProperty).setValue(value);
/*      */         }
/*  615 */         else if (value != null)
/*      */         {
/*  617 */           BinaryList list = Factory.BinaryList.createList();
/*  618 */           list.add(value);
/*  619 */           ((PropertyBinaryList)this.jaceProperty).setValue(list);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*  624 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "byte[]" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  629 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  631 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBinaryValue(List<byte[]> values)
/*      */   {
/*  640 */     Tracer.traceMethodEntry(new Object[] { values });
/*      */     try
/*      */     {
/*  643 */       if ((this.dataType == DataType.Binary) && (this.cardinality != RMCardinality.Single))
/*      */       {
/*  645 */         BinaryList list = null;
/*  646 */         if (values != null)
/*      */         {
/*  648 */           list = Factory.BinaryList.createList();
/*  649 */           list.addAll(values);
/*      */         }
/*  651 */         ((PropertyBinaryList)this.jaceProperty).setValue(list);
/*      */       }
/*      */       else
/*      */       {
/*  655 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<byte[]>" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  660 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  662 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBooleanValue(Boolean value)
/*      */   {
/*  671 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/*  674 */       if (this.dataType == DataType.Boolean)
/*      */       {
/*  676 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/*  678 */           ((PropertyBoolean)this.jaceProperty).setValue(value);
/*      */         }
/*  680 */         else if (value != null)
/*      */         {
/*  682 */           BooleanList list = Factory.BooleanList.createList();
/*  683 */           list.add(value);
/*  684 */           ((PropertyBooleanList)this.jaceProperty).setValue(list);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*  689 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Boolean" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  694 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  696 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBooleanValue(List<Boolean> values)
/*      */   {
/*  705 */     Tracer.traceMethodEntry(new Object[] { values });
/*      */     try
/*      */     {
/*  708 */       if ((this.dataType == DataType.Boolean) && (this.cardinality != RMCardinality.Single))
/*      */       {
/*  710 */         BooleanList list = null;
/*  711 */         if (values != null)
/*      */         {
/*  713 */           list = Factory.BooleanList.createList();
/*  714 */           list.addAll(values);
/*      */         }
/*  716 */         ((PropertyBooleanList)this.jaceProperty).setValue(list);
/*      */       }
/*      */       else
/*      */       {
/*  720 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Boolean>" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  725 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  727 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDateTimeValue(Date value)
/*      */   {
/*  736 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/*  739 */       if (this.dataType == DataType.DateTime)
/*      */       {
/*  741 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/*  743 */           ((PropertyDateTime)this.jaceProperty).setValue(value);
/*      */         }
/*  745 */         else if (value != null)
/*      */         {
/*  747 */           DateTimeList list = Factory.DateTimeList.createList();
/*  748 */           list.add(value);
/*  749 */           ((PropertyDateTimeList)this.jaceProperty).setValue(list);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*  754 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Date" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  759 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  761 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDateTimeValue(List<Date> values)
/*      */   {
/*  770 */     Tracer.traceMethodEntry(new Object[] { values });
/*      */     try
/*      */     {
/*  773 */       if ((this.dataType == DataType.DateTime) && (this.cardinality != RMCardinality.Single))
/*      */       {
/*  775 */         DateTimeList list = null;
/*  776 */         if (values != null)
/*      */         {
/*  778 */           list = Factory.DateTimeList.createList();
/*  779 */           list.addAll(values);
/*      */         }
/*  781 */         ((PropertyDateTimeList)this.jaceProperty).setValue(list);
/*      */       }
/*      */       else
/*      */       {
/*  785 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Date>" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  790 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  792 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDoubleValue(Double value)
/*      */   {
/*  801 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/*  804 */       if (this.dataType == DataType.Double)
/*      */       {
/*  806 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/*  808 */           ((PropertyFloat64)this.jaceProperty).setValue(value);
/*      */         }
/*  810 */         else if (value != null)
/*      */         {
/*  812 */           Float64List list = Factory.Float64List.createList();
/*  813 */           list.add(value);
/*  814 */           ((PropertyFloat64List)this.jaceProperty).setValue(list);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*  819 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Double" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  824 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  826 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDoubleValue(List<Double> values)
/*      */   {
/*  835 */     Tracer.traceMethodEntry(new Object[] { values });
/*      */     try
/*      */     {
/*  838 */       if ((this.dataType == DataType.Double) && (this.cardinality != RMCardinality.Single))
/*      */       {
/*  840 */         Float64List list = null;
/*  841 */         if (values != null)
/*      */         {
/*  843 */           list = Factory.Float64List.createList();
/*  844 */           list.addAll(values);
/*      */         }
/*  846 */         ((PropertyFloat64List)this.jaceProperty).setValue(list);
/*      */       }
/*      */       else
/*      */       {
/*  850 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Double>" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  855 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  857 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGuidValue(String value)
/*      */   {
/*  866 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/*  869 */       if (this.dataType == DataType.Guid)
/*      */       {
/*  871 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/*  873 */           Id idValue = value != null ? new Id(value) : null;
/*  874 */           ((PropertyId)this.jaceProperty).setValue(idValue);
/*      */         }
/*  876 */         else if (value != null)
/*      */         {
/*  878 */           IdList list = Factory.IdList.createList();
/*  879 */           list.add(new Id(value));
/*  880 */           ((PropertyIdList)this.jaceProperty).setValue(list);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*  885 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "String" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  890 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  892 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGuidValue(List<String> values)
/*      */   {
/*  901 */     Tracer.traceMethodEntry(new Object[] { values });
/*      */     try
/*      */     {
/*  904 */       if ((this.dataType == DataType.Guid) && (this.cardinality != RMCardinality.Single))
/*      */       {
/*  906 */         IdList list = null;
/*  907 */         if (values != null)
/*      */         {
/*  909 */           list = Factory.IdList.createList();
/*  910 */           for (String guid : values)
/*      */           {
/*  912 */             list.add(new Id(guid));
/*      */           }
/*      */         }
/*  915 */         ((PropertyIdList)this.jaceProperty).setValue(list);
/*      */       }
/*      */       else
/*      */       {
/*  919 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<String>" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  924 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  926 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIntegerValue(Integer value)
/*      */   {
/*  935 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/*  938 */       if (this.dataType == DataType.Integer)
/*      */       {
/*  940 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/*  942 */           ((PropertyInteger32)this.jaceProperty).setValue(value);
/*      */         }
/*  944 */         else if (value != null)
/*      */         {
/*  946 */           Integer32List list = Factory.Integer32List.createList();
/*  947 */           list.add(value);
/*  948 */           ((PropertyInteger32List)this.jaceProperty).setValue(list);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*  953 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Integer" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  958 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  960 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIntegerValue(List<Integer> values)
/*      */   {
/*  969 */     Tracer.traceMethodEntry(new Object[] { values });
/*      */     try
/*      */     {
/*  972 */       if ((this.dataType == DataType.Integer) && (this.cardinality != RMCardinality.Single))
/*      */       {
/*  974 */         Integer32List list = null;
/*  975 */         if (values != null)
/*      */         {
/*  977 */           list = Factory.Integer32List.createList();
/*  978 */           list.addAll(values);
/*      */         }
/*  980 */         ((PropertyInteger32List)this.jaceProperty).setValue(list);
/*      */       }
/*      */       else
/*      */       {
/*  984 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<Integer>" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/*  989 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/*  991 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setObjectValue(Object value)
/*      */   {
/*  999 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/* 1002 */       if (this.dataType == DataType.Object)
/*      */       {
/* 1004 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/* 1006 */           if ((value instanceof JaceBasable))
/*      */           {
/* 1008 */             EngineObject jaceEngObj = ((JaceBasable)value).getJaceBaseObject();
/* 1009 */             this.jaceProperty.setObjectValue(jaceEngObj);
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/* 1014 */           this.jaceProperty.setObjectValue(value);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/* 1019 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "Object" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/* 1024 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/* 1026 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setObjectValue(List<Object> values)
/*      */   {
/* 1034 */     Tracer.traceMethodEntry(new Object[] { values });
/* 1035 */     throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "setObjectValue(List<?>)", "RMProperty", getSymbolicName() });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStringValue(String value)
/*      */   {
/* 1044 */     Tracer.traceMethodEntry(new Object[] { value });
/*      */     try
/*      */     {
/* 1047 */       if (this.dataType == DataType.String)
/*      */       {
/* 1049 */         if (this.cardinality == RMCardinality.Single)
/*      */         {
/* 1051 */           ((PropertyString)this.jaceProperty).setValue(value);
/*      */         }
/* 1053 */         else if (value != null)
/*      */         {
/* 1055 */           StringList list = Factory.StringList.createList();
/* 1056 */           list.add(value);
/* 1057 */           ((PropertyStringList)this.jaceProperty).setValue(list);
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/* 1062 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "String" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/* 1067 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/* 1069 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStringValue(List<String> values)
/*      */   {
/* 1078 */     Tracer.traceMethodEntry(new Object[] { values });
/*      */     try
/*      */     {
/* 1081 */       if ((this.dataType == DataType.String) && (this.cardinality != RMCardinality.Single))
/*      */       {
/* 1083 */         StringList list = null;
/* 1084 */         if (values != null)
/*      */         {
/* 1086 */           list = Factory.StringList.createList();
/* 1087 */           list.addAll(values);
/*      */         }
/* 1089 */         ((PropertyStringList)this.jaceProperty).setValue(list);
/*      */       }
/*      */       else
/*      */       {
/* 1093 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_INVALID_PROPERTY_DATATYPE, new Object[] { this.symbolicName, "List<String>" });
/*      */       }
/*      */     }
/*      */     catch (EngineRuntimeException ex)
/*      */     {
/* 1098 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PROPERTY_SET_ERROR, new Object[] { this.symbolicName, this.dataType.name() });
/*      */     }
/* 1100 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1109 */     String symName = "<unknown>";
/* 1110 */     if (this.jaceProperty != null)
/* 1111 */       symName = this.jaceProperty.getPropertyName();
/* 1112 */     return "P8CE_RMProperty symbolicName: " + symName;
/*      */   }
/*      */   
/*      */   public Property getJaceProperty()
/*      */   {
/* 1117 */     return this.jaceProperty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void injestJaceProperty(Property jaceProperty)
/*      */   {
/* 1124 */     Tracer.traceMethodEntry(new Object[] { jaceProperty });
/* 1125 */     this.jaceProperty = jaceProperty;
/* 1126 */     this.symbolicName = jaceProperty.getPropertyName();
/*      */     
/*      */ 
/*      */ 
/* 1130 */     if ((jaceProperty instanceof PropertyString))
/*      */     {
/* 1132 */       this.dataType = DataType.String;
/* 1133 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1135 */     else if ((jaceProperty instanceof PropertyBoolean))
/*      */     {
/* 1137 */       this.dataType = DataType.Boolean;
/* 1138 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1140 */     else if ((jaceProperty instanceof PropertyDateTime))
/*      */     {
/* 1142 */       this.dataType = DataType.DateTime;
/* 1143 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1145 */     else if ((jaceProperty instanceof PropertyInteger32))
/*      */     {
/* 1147 */       this.dataType = DataType.Integer;
/* 1148 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1150 */     else if ((jaceProperty instanceof PropertyId))
/*      */     {
/* 1152 */       this.dataType = DataType.Guid;
/* 1153 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1155 */     else if ((jaceProperty instanceof PropertyEngineObject))
/*      */     {
/* 1157 */       this.dataType = DataType.Object;
/* 1158 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1160 */     else if ((jaceProperty instanceof PropertyStringList))
/*      */     {
/* 1162 */       this.dataType = DataType.String;
/* 1163 */       this.cardinality = RMCardinality.List;
/*      */     }
/* 1165 */     else if ((jaceProperty instanceof PropertyFloat64))
/*      */     {
/* 1167 */       this.dataType = DataType.Double;
/* 1168 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1170 */     else if ((jaceProperty instanceof PropertyInteger32List))
/*      */     {
/* 1172 */       this.dataType = DataType.Integer;
/* 1173 */       this.cardinality = RMCardinality.List;
/*      */     }
/* 1175 */     else if ((jaceProperty instanceof PropertyDependentObjectList))
/*      */     {
/* 1177 */       this.dataType = DataType.Object;
/* 1178 */       this.cardinality = RMCardinality.List;
/*      */     }
/* 1180 */     else if ((jaceProperty instanceof PropertyIndependentObjectSet))
/*      */     {
/* 1182 */       this.dataType = DataType.Object;
/* 1183 */       this.cardinality = RMCardinality.Enumeration;
/*      */     }
/* 1185 */     else if ((jaceProperty instanceof PropertyBinary))
/*      */     {
/* 1187 */       this.dataType = DataType.Binary;
/* 1188 */       this.cardinality = RMCardinality.Single;
/*      */     }
/* 1190 */     else if ((jaceProperty instanceof PropertyIdList))
/*      */     {
/* 1192 */       this.dataType = DataType.Guid;
/* 1193 */       this.cardinality = RMCardinality.List;
/*      */     }
/* 1195 */     else if ((jaceProperty instanceof PropertyFloat64List))
/*      */     {
/* 1197 */       this.dataType = DataType.Double;
/* 1198 */       this.cardinality = RMCardinality.List;
/*      */     }
/* 1200 */     else if ((jaceProperty instanceof PropertyDateTimeList))
/*      */     {
/* 1202 */       this.dataType = DataType.DateTime;
/* 1203 */       this.cardinality = RMCardinality.List;
/*      */     }
/* 1205 */     else if ((jaceProperty instanceof PropertyBinaryList))
/*      */     {
/* 1207 */       this.dataType = DataType.Binary;
/* 1208 */       this.cardinality = RMCardinality.List;
/*      */     }
/* 1210 */     else if ((jaceProperty instanceof PropertyBooleanList))
/*      */     {
/* 1212 */       this.dataType = DataType.Boolean;
/* 1213 */       this.cardinality = RMCardinality.List;
/*      */     }
/*      */     else
/*      */     {
/* 1217 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_UNSUPPORTED_PROPERTY_CLASS, new Object[] { this.symbolicName, jaceProperty.getClass().toString() });
/*      */     }
/* 1219 */     Tracer.traceMethodExit(new Object[0]);
/*      */   }
/*      */   
/*      */   private Repository getRepository()
/*      */   {
/* 1224 */     Tracer.traceMethodEntry(new Object[0]);
/* 1225 */     Repository result = null;
/* 1226 */     if (this.parentCollection != null)
/*      */     {
/* 1228 */       result = this.parentCollection.getRepository();
/*      */     }
/*      */     
/* 1231 */     Tracer.traceMethodExit(new Object[] { result });
/* 1232 */     return result;
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMPropertyImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */