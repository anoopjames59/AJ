package com.ibm.jarm.api.property;

import com.ibm.jarm.api.constants.DataType;
import com.ibm.jarm.api.constants.RMCardinality;
import java.util.Date;
import java.util.List;

public abstract interface RMProperty
{
  public abstract String getSymbolicName();
  
  public abstract DataType getDataType();
  
  public abstract RMCardinality getCardinality();
  
  public abstract boolean isDirty();
  
  public abstract boolean isSettable();
  
  public abstract void setBinaryValue(byte[] paramArrayOfByte);
  
  public abstract void setBinaryValue(List<byte[]> paramList);
  
  public abstract void setBooleanValue(Boolean paramBoolean);
  
  public abstract void setBooleanValue(List<Boolean> paramList);
  
  public abstract void setDateTimeValue(Date paramDate);
  
  public abstract void setDateTimeValue(List<Date> paramList);
  
  public abstract void setDoubleValue(Double paramDouble);
  
  public abstract void setDoubleValue(List<Double> paramList);
  
  public abstract void setGuidValue(String paramString);
  
  public abstract void setGuidValue(List<String> paramList);
  
  public abstract void setIntegerValue(Integer paramInteger);
  
  public abstract void setIntegerValue(List<Integer> paramList);
  
  public abstract void setObjectValue(Object paramObject);
  
  public abstract void setObjectValue(List<Object> paramList);
  
  public abstract void setStringValue(String paramString);
  
  public abstract void setStringValue(List<String> paramList);
  
  public abstract byte[] getBinaryValue();
  
  public abstract List<byte[]> getBinaryListValue();
  
  public abstract Boolean getBooleanValue();
  
  public abstract List<Boolean> getBooleanListValue();
  
  public abstract Date getDateTimeValue();
  
  public abstract List<Date> getDateTimeListValue();
  
  public abstract Double getDoubleValue();
  
  public abstract List<Double> getDoubleListValue();
  
  public abstract String getGuidValue();
  
  public abstract List<String> getGuidListValue();
  
  public abstract Integer getIntegerValue();
  
  public abstract List<Integer> getIntegerListValue();
  
  public abstract Object getObjectValue();
  
  public abstract List<Object> getObjectListValue();
  
  public abstract String getStringValue();
  
  public abstract List<String> getStringListValue();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\property\RMProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */