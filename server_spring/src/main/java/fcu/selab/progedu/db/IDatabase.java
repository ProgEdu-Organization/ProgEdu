package fcu.selab.progedu.db;

import java.sql.Connection;

public interface IDatabase {
  
  Connection getConnection();

}
