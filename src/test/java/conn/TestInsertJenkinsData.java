package conn;

public class TestInsertJenkinsData {

//  public static void main(String[] args) {
//
//    UserDbManager db = UserDbManager.getInstance();
//    ProjectDbManager pDb = ProjectDbManager.getInstance();
//    List<User> users = db.listAllUsers();
//    List<GitlabProject> gitProjects = new ArrayList<>();
//    List<Project> dbProjects = pDb.listAllProjects();
//    Conn conn = Conn.getInstance();
//    CommitResultDbManager commitDb = CommitResultDbManager.getInstance();
//    JenkinsService jenkins = new JenkinsService();
//
//    for (User user : users) {
//      String userName = user.getUserName();
//      gitProjects = conn.getProject(user);
//      Collections.reverse(gitProjects);
//      StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();
//      for (Project dbProject : dbProjects) {
//        String proName = null;
//        if (!dbProject.getName().equals("OOP-HW10") && !dbProject.getName().equals("OOP-HW11")) {
//          continue;
//        }
//        for (GitlabProject gitProject : gitProjects) {
//          if (dbProject.getName().equals(gitProject.getName())) {
//            proName = dbProject.getName();
//            break;
//          } else {
//            proName = "N/A";
//          }
//        }
//
//        if ("N/A".equals(proName) || "".equals(proName) || null == proName) {
//          if (proName == null) {
//            proName = "N/A";
//          }
//        } else {
//          String[] result = jenkins.getColor(proName, userName).split(",");
//          String color = result[0].replace("circle ", "");
//          int commit = Integer.valueOf(result[1]) - 1;
//          String hw = proName.replace("OOP-HW", "");
//
//          List<Integer> buildNum = stuDashChoPro.getScmBuildCounts(userName, proName);
//          int num = 0;
//          if (buildNum.size() > 1) {
//            num = buildNum.size() - 1;
//          }
//
//          String buildApiJson = stuDashChoPro.getBuildApiJson(buildNum.get(num), userName, proName);
//          String strDate = stuDashChoPro.getCommitTime(buildApiJson);
//          int id = db.getUser(userName).getId();
//
//          boolean check = commitDb.checkJenkinsJobTimestamp(user.getId(), hw);
//          if (check) {
//            commitDb.updateJenkinsCommitCount(id, hw, commit, color);
//            commitDb.updateJenkinsJobTimestamp(id, hw, strDate);
//            System.out.println("update, " + user.getId() + ", " + hw + ", " + commit + ", " + color
//                + ", " + strDate);
//          } else {
//            commitDb.insertJenkinsCommitCount(id, hw, commit, color);
//            commitDb.updateJenkinsJobTimestamp(id, hw, strDate);
//            System.out.println("insert, " + user.getId() + ", " + hw + ", " + commit + ", " + color
//                + ", " + strDate);
//          }
//        }
//      }
//    }
//  }
}
