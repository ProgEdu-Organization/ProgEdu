package fcu.selab.progedu.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

  private int id; // ??��?��?�蕭

  private int gitLabId;

  private String studentId; // ??�緯??��?��?��?�蕭(??�稷??��?�蕭)
                            // ??��?��?�蕭

  private String name; // ??��?�蕭??��?�蕭??��?�蕭??��(??��?�蕭??��?�蕭)

  private String email; // ??��?��?�蕭@fcu.edu.tw

  private String password; // ??��?��?�蕭

  private String gitLabToken;

  private boolean display;

  public User() {

  }

  /**
   * User constructor
   * 
   * @param studentId studentId
   * @param name      student's full name
   * @param email     student's email
   * @param password  student's password
   * @param display   student's display
   */
  public User(String studentId, String name, String email, String password, boolean display) {
    this.studentId = studentId;
    this.name = name;
    this.email = email;
    this.password = password;
    this.display = display;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getGitLabId() {
    return gitLabId;
  }

  public void setGitLabId(int gitLabId) {
    this.gitLabId = gitLabId;
  }

  public String getStufentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getGitLabToken() {
    return gitLabToken;
  }

  public void setGitLabToken(String gitLabToken) {
    this.gitLabToken = gitLabToken;
  }

  public boolean getDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }

}
