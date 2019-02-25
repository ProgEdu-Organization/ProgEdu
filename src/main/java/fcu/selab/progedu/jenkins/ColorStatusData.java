package fcu.selab.progedu.jenkins;

public class ColorStatusData {

  private String username;
  private String projName;
  private int num;

  /**
   * Constructer
   * 
   * @param url
   *          project url
   */
  public ColorStatusData(String url) {

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
