package conn;

import java.text.ParseException;
import java.util.List;

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.service.CommitRecordService;

public class TestUpdateCommitResult {
  static CommitRecordService service = new CommitRecordService();
  static UserDbManager userDb = UserDbManager.getInstance();

  public static void main(String[] args) throws ParseException {
    List<User> users = userDb.getAllUsers();

    int i = 1;
    for (User user : users) {
      String userName = user.getUsername();
      System.out.println(i + ", " + userName);
      service.updateCommitResult(userName, "OOP-HW4");
      i++;
    }
  }
}
