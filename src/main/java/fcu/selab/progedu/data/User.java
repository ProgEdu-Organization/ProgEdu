package fcu.selab.progedu.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

  private int id; // ??��?��?�蕭

  private int gitLabId;

  private String userName; // ??�緯??��?��?��?�蕭(??�稷??��?�蕭) ??��?��?�蕭

  private String name; // ??��?�蕭??��?�蕭??��?�蕭??��(??��?�蕭??��?�蕭)

  private String email; // ??��?��?�蕭@fcu.edu.tw

  private String password; // ??��?��?�蕭

  private String privateToken;

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

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  public String getPrivateToken() {
    return privateToken;
  }

  public void setPrivateToken(String privateToken) {
    this.privateToken = privateToken;
  }

}
