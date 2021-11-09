package fcu.selab.progedu.data;

import java.io.Serializable;
import java.util.List;

import fcu.selab.progedu.service.RoleEnum;

@SuppressWarnings("serial")
public class User implements Serializable {

  private int id; // ??��?��?�蕭

  private int gitLabId;

  private String username; // ??�緯??��?��?��?�蕭(??�稷??��?�蕭)
                           // ??��?��?�蕭

  private String name; // ??��?�蕭??��?�蕭??��?�蕭??��(??��?�蕭??��?�蕭)

  private String email; // ??��?��?�蕭@fcu.edu.tw

  private String password; // ??��?��?�蕭

  private String gitLabToken;

  private boolean display;

  private List<RoleEnum> roleList;

  public User() {
  }

  /**
   * (to do)
   * 
   * @param username    (to do)
   * @param name        (to do)
   * @param email       (to do)
   * @param password    (to do)
   * @param isDisplayed (to do)
   */
  public User(String username, String name, String email, String password, List<RoleEnum> role,
      boolean isDisplayed) {
    this.username = username;
    this.name = name;
    this.email = email;
    this.password = password;
    this.roleList = role;
    this.display = isDisplayed;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  /**
   * to do
   * 
   * @param password password
   */
  public void setPassword(String password) {
    if (password == null || password.isEmpty()) {
      this.password = this.username;
    } else {
      this.password = password;
    }
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

  public List<RoleEnum> getRole() {
    return roleList;
  }

  public void setRole(List<RoleEnum> role) {
    this.roleList = role;
  }

}
