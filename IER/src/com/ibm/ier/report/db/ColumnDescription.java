package com.ibm.ier.report.db;

public abstract interface ColumnDescription
{
  public abstract int getColumnIndex();
  
  public abstract String getColumnName();
  
  public abstract int getSQLType();
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\ColumnDescription.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */