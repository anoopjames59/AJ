package com.ibm.jarm.api.property;

import com.ibm.jarm.api.constants.DataType;
import com.ibm.jarm.api.constants.RMCardinality;
import java.util.Date;
import java.util.List;

public abstract interface RMProperties
  extends Iterable<RMProperty>
{
  public abstract void add(String paramString, DataType paramDataType, RMCardinality paramRMCardinality, Object paramObject);
  
  public abstract void add(RMProperty paramRMProperty);
  
  public abstract void add(RMProperties paramRMProperties);
  
  public abstract boolean isDirty();
  
  public abstract boolean isPropertyPresent(String paramString);
  
  public abstract void remove(String paramString);
  
  public abstract void remove(String[] paramArrayOfString);
  
  public abstract int size();
  
  public abstract RMProperty[] toArray();
  
  public abstract RMProperty get(String paramString);
  
  public abstract void putBinaryValue(String paramString, byte[] paramArrayOfByte);
  
  public abstract void putBinaryListValue(String paramString, List<byte[]> paramList);
  
  public abstract void putBooleanValue(String paramString, Boolean paramBoolean);
  
  public abstract void putBooleanListValue(String paramString, List<Boolean> paramList);
  
  public abstract void putDateTimeValue(String paramString, Date paramDate);
  
  public abstract void putDateTimeListValue(String paramString, List<Date> paramList);
  
  public abstract void putDoubleValue(String paramString, Double paramDouble);
  
  public abstract void putDoubleListValue(String paramString, List<Double> paramList);
  
  public abstract void putGuidValue(String paramString1, String paramString2);
  
  public abstract void putGuidListValue(String paramString, List<String> paramList);
  
  public abstract void putIntegerValue(String paramString, Integer paramInteger);
  
  public abstract void putIntegerListValue(String paramString, List<Integer> paramList);
  
  public abstract void putObjectValue(String paramString, Object paramObject);
  
  public abstract void putObjectListValue(String paramString, List<Object> paramList);
  
  public abstract void putStringValue(String paramString1, String paramString2);
  
  public abstract void putStringListValue(String paramString, List<String> paramList);
  
  public abstract byte[] getBinaryValue(String paramString);
  
  public abstract List<byte[]> getBinaryListValue(String paramString);
  
  public abstract Boolean getBooleanValue(String paramString);
  
  public abstract List<Boolean> getBooleanListValue(String paramString);
  
  public abstract Date getDateTimeValue(String paramString);
  
  public abstract List<Date> getDateTimeListValue(String paramString);
  
  public abstract Double getDoubleValue(String paramString);
  
  public abstract List<Double> getDoubleListValue(String paramString);
  
  public abstract String getGuidValue(String paramString);
  
  public abstract List<String> getGuidListValue(String paramString);
  
  public abstract Integer getIntegerValue(String paramString);
  
  public abstract List<Integer> getIntegerListValue(String paramString);
  
  public abstract Object getObjectValue(String paramString);
  
  public abstract List<Object> getObjectListValue(String paramString);
  
  public abstract String getStringValue(String paramString);
  
  public abstract List<String> getStringListValue(String paramString);
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\property\RMProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */