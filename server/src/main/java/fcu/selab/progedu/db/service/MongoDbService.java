
package fcu.selab.progedu.db.service;

import com.mongodb.client.MongoCollection;
import fcu.selab.progedu.exception.LoadConfigFailureException;
import org.bson.Document;
import fcu.selab.progedu.db.MongoDb;

public class MongoDbService  {

  private MongoDb mongoDatabase = MongoDb.getInstance();

  private static MongoDbService dbService;

  static {
    try {
      dbService = new MongoDbService();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  public MongoDbService() throws LoadConfigFailureException {
  }

  public static MongoDbService getInstance() {
    return dbService;
  }

  public MongoCollection<Document> getCollection(String collection) {
    return mongoDatabase.getClient().getCollection(collection);
  }


}