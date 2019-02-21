package conn;

public class TestGetJobTimestamp {

//  public static void main(String[] args) {
//    UserDbManager db = UserDbManager.getInstance();
//    List<User> users = db.listAllUsers();
//    ProjectDbManager pDb = ProjectDbManager.getInstance();
//    List<Project> dbProjects = pDb.listAllProjects();
//    List<GitlabProject> gitProjects = new ArrayList<>();
//    Conn conn = Conn.getInstance();
//    CommitResultDbManager commitDb = CommitResultDbManager.getInstance();
//    StudentDashChoosePro stuDashChoPro = new StudentDashChoosePro();
//    for (User user : users) {
//      String userName = user.getUserName();
//      gitProjects = conn.getProject(user);
//      Collections.reverse(gitProjects);
//      for (Project dbProject : dbProjects) {
//        String proName = null;
//        for (GitlabProject gitProject : gitProjects) {
//          if (dbProject.getName().equals(gitProject.getName())) {
//            proName = dbProject.getName();
//            List<Integer> buildNum = stuDashChoPro.getScmBuildCounts(userName, proName);
//            int num = 0;
//            if (buildNum.size() > 1) {
//              num = buildNum.size() - 1;
//            }
//            String buildApiJson = stuDashChoPro.getBuildApiJson(buildNum.get(num), userName,
//                proName);
//            String strDate = stuDashChoPro.getCommitTime(buildApiJson);
//            int id = db.getUser(userName).getId();
//            String pro = proName.replace("OOP-HW", "");
//            boolean check = commitDb.updateJenkinsJobTimestamp(id, pro, strDate);
//            if (check) {
//              System.out.println(userName + ", " + proName + ", " + strDate);
//            }
//            break;
//          }
//        }
//      }
//    }
//  }

}
