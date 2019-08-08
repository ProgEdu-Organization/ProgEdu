package conn;

import org.gitlab.api.models.GitlabSession;
import org.json.JSONObject;

import fcu.selab.progedu.config.MySqlDbConfig;
import fcu.selab.progedu.conn.GitlabService;
import fcu.selab.progedu.exception.LoadConfigFailureException;

public class TestGetSession {
  MySqlDbConfig getDb = MySqlDbConfig.getInstance();

  private String dbUser;
  private String dbPassword;

  /**
   * constructor
   * 
   * @throws LoadConfigFailureException on properties call error
   */
  public TestGetSession() {
    try {
      dbUser = getDb.getDbUser();
      dbPassword = getDb.getDbPassword();
    } catch (LoadConfigFailureException e) {
      e.printStackTrace();
    }
  }

  public void testSession() {
    GitlabService conn = GitlabService.getInstance();
    String username = dbUser;
    String password = dbPassword;

    GitlabSession session = conn.getSession(username, password);
    if (session == null) {
      System.out.println("session null");
    }
    JSONObject json = new JSONObject(session);
    System.out.println("session : " + session);
    System.out.println("json : " + json);
  }
}
