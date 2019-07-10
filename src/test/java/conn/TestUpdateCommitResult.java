package conn;

import java.util.List;

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.service.CommitResultService;

public class TestUpdateCommitResult {
  static CommitResultService service = new CommitResultService();
  static UserDbManager userDb = UserDbManager.getInstance();

  public static void main(String[] args) {
    List<User> users = userDb.listAllUsers();

    int i = 1;
    for (User user : users) {
      String userName = user.getStufentId();
      System.out.println(i + ", " + userName);
      service.updateCommitResult(userName, "OOP-HW4");
      i++;
    }
  }
}
