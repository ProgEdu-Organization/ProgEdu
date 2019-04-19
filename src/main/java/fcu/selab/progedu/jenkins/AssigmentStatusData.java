package fcu.selab.progedu.jenkins;

public class AssigmentStatusData {

  private String username;
  private String projName;
  private int num;

  /**
   * Constructer
   * 
   * @param url
   *          project url
   */
  public AssigmentStatusData(String url) {
    // suppose url = http://140.134.26.71:8888/job/M0000001_OOP-HW1/1/console
    // username
    int startChar = url.indexOf("job") + 4;
    int endChar = url.indexOf("_");
    username = url.substring(startChar, endChar);

    // projName
    startChar = endChar + 1;
    endChar = url.indexOf("/", startChar);
    projName = url.substring(startChar, endChar);

    // num
    startChar = endChar + 1;
    endChar = url.indexOf("/", startChar);
    num = Integer.valueOf(url.substring(startChar, endChar));
  }

  public String getUsername() {
    return username;
  }

  public String getProjectName() {
    return projName;
  }

  public int getNumber() {
    return num;
  }
}
