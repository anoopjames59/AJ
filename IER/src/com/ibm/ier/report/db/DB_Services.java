package com.ibm.ier.report.db;

import com.ibm.ier.report.util.QueryParser.SelectItem;
import com.ibm.jarm.api.property.RMProperty;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.naming.NamingException;

public abstract interface DB_Services
{
  public abstract void setSchemaName(String paramString);
  
  public abstract void open(String paramString)
    throws ClassNotFoundException, SQLException, NamingException;
  
  public abstract void open(Map<String, String> paramMap)
    throws ClassNotFoundException, SQLException;
  
  public abstract void close()
    throws SQLException;
  
  public abstract void deleteTable(String paramString)
    throws SQLException;
  
  public abstract void createTable(String paramString, QueryParser.SelectItem[] paramArrayOfSelectItem)
    throws SQLException;
  
  public abstract LinkedHashMap<String, ColumnDescription> getColumnDescription(LinkedHashSet<RMProperty> paramLinkedHashSet)
    throws SQLException;
  
  public abstract PreparedStatement createPreparedStatement(String paramString, LinkedHashMap<String, ColumnDescription> paramLinkedHashMap)
    throws SQLException;
  
  public abstract int performInsert(PreparedStatement paramPreparedStatement, LinkedHashSet<RMProperty> paramLinkedHashSet, LinkedHashMap<String, ColumnDescription> paramLinkedHashMap)
    throws SQLException;
  
  public abstract int performDelete(String paramString1, String paramString2)
    throws SQLException;
  
  public abstract void close(PreparedStatement paramPreparedStatement)
    throws SQLException;
  
  public abstract boolean isTableExist(String paramString)
    throws SQLException;
  
  public abstract boolean isDataExist(String paramString1, String paramString2, String paramString3)
    throws SQLException;
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\DB_Services.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */