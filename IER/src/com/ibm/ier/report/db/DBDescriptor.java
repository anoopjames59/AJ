package com.ibm.ier.report.db;

import java.sql.SQLException;
import javax.naming.NamingException;

public abstract interface DBDescriptor
{
  public static final String DB_Server = "DB_Server";
  public static final String DB_Port = "DB_Port";
  public static final String DB_Database = "DB_Database";
  public static final String DB_Username = "DB_Username";
  public static final String DB_Password = "DB_Password";
  public static final String DB_Schema = "DB_Schema";
  
  public abstract DBType getDatabaseType();
  
  public abstract void verify()
    throws ClassNotFoundException, SQLException, NamingException;
}


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\report\db\DBDescriptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */