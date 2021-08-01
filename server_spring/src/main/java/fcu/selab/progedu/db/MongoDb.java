
package fcu.selab.progedu.db;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import fcu.selab.progedu.config.MongoDbConfig;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class MongoDb {

  private static MongoDb dbService = new MongoDb();
  private static MongoClient mongoClient;
  private static MongoDatabase database;

  
  /**
   * Get MongoDatabase Instance
   */
  public static MongoDb getInstance() throws LoadConfigFailureException {
    if (mongoClient == null) {
      mongoClient = MongoClients.create(MongoDbConfig.getInstance().getDbConnectionString());
      database = mongoClient.getDatabase(MongoDbConfig.getInstance().getDbSchema());
    }
    return dbService;
  }

  /**
   * Connection to db
   */
  public MongoDatabase getClient() {
    return database;
  }

}