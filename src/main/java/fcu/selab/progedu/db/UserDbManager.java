package fcu.selab.progedu.db;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.gitlab.api.models.GitlabUser;

import fcu.selab.progedu.data.User;

public class UserDbManager {
  private static final String QUERY = "SELECT * FROM User WHERE id = ?";
  private static final String GIT_LAB_ID = "gitLabId";
  private static final String USERNAME = "username";
  private static final String NAME = "name";
  private static final String PASSWORD = "password";
  private static final String EMAIL = "email";
  private static final String GIT_LAB_TOKEN = "gitLabToken";
  private static final String DISPLAY = "display";

  private static UserDbManager dbManager = new UserDbManager();

  public static UserDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  private UserDbManager() {

  }

  /**
   * Add gitlab user to database
   * 
   * @param user The gitlab user
   */
  public void addUser(GitlabUser user) {
    String sql = "INSERT INTO "
        + "User(gitLabId, username, name, password, email, gitLabToken, display)  "
        + "VALUES(?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      String password = passwordMD5(user.getUsername());
      password += user.getUsername();
      preStmt.setInt(1, user.getId());
      preStmt.setString(2, user.getUsername());
      preStmt.setString(3, user.getName());
      preStmt.setString(4, password);
      preStmt.setString(5, user.getEmail());
      preStmt.setString(6, user.getPrivateToken());
      preStmt.setBoolean(7, true);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * encrypt the user password
   * 
   * @param password The user's password
   * @return MD5 string
   * @throws NoSuchAlgorithmException on security api call error
   */
  public String passwordMD5(String password) {
    String hashtext = "";
    try {
      String msg = password;
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(msg.getBytes());
      BigInteger number = new BigInteger(1, messageDigest);
      hashtext = number.toString(16);

      StringBuilder bld = new StringBuilder();
      for (int count = hashtext.length(); count < 32; count++) {
        bld.append("0");
      }
      hashtext = bld.toString() + hashtext;
      System.out.println(hashtext);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return hashtext;
  }

  /**
   * get user password
   * 
   * @param username user stu id
   * @return password
   */
  public String getPassword(String username) {
    String password = "";
    String query = "SELECT * FROM User WHERE username = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, username);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          password = rs.getString(PASSWORD);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return password;
  }

  /**
   * update user db password
   * 
   * @param username user stu id
   * @param password user new password
   */
  public void modifiedUserPassword(String username, String password) {
    String query = "UPDATE User SET password=? WHERE username = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      String newPass = passwordMD5(password) + username;
      preStmt.setString(1, newPass);
      preStmt.setString(2, username);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * check old password
   * 
   * @param username user stu id
   * @param password user old password
   * @return T or F
   */
  public boolean checkPassword(String username, String password) {
    boolean check = false;
    String currPassword = getPassword(username);
    if (currPassword.equals(passwordMD5(password) + username)) {
      check = true;
    }
    return check;
  }

  /**
   * Get user from database
   * 
   * @param username The gitlab user name
   * @return user
   */
  public User getUser(String username) {
    User user = new User();
    String query = "SELECT * FROM User WHERE username = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, username);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int gitLabId = rs.getInt(GIT_LAB_ID);
          int id = rs.getInt("id");
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String gitLabToken = rs.getString(GIT_LAB_TOKEN);

          user.setGitLabId(gitLabId);
          user.setId(id);
          user.setUsername(username);
          user.setName(name);
          user.setPassword(password);
          user.setEmail(email);
          user.setGitLabToken(gitLabToken);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  /**
   * Get user from database
   *
   * @param id The gitlab user id
   * @return user
   */
  public User getUser(int id) {
    User user = new User();

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(QUERY)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          int gitLabId = rs.getInt(GIT_LAB_ID);
          String username = rs.getString(USERNAME);
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String gitLabToken = rs.getString(GIT_LAB_TOKEN);
          Boolean display = rs.getBoolean(DISPLAY);

          user.setGitLabId(gitLabId);
          user.setId(id);
          user.setUsername(username);
          user.setName(name);
          user.setPassword(password);
          user.setEmail(email);
          user.setGitLabToken(gitLabToken);
          user.setDisplay(display);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  /**
   * Get user from database
   *
   * @param id The db user id
   * @return user
   */
  public String getName(int id) {
    String name = "";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(QUERY)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          name = rs.getString(NAME);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return name;
  }

  /**
   * Get user from database
   *
   * @param id The db user id
   * @return user
   */
  public String getUsername(int id) {
    String name = "";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(QUERY)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          name = rs.getString(USERNAME);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return name;
  }

  /**
   * user name to find userId in db
   * 
   * @return id
   */
  public int getUserIdByUsername(String username) {
    String query = "SELECT * FROM User WHERE username = ?";
    int id = -1;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, username);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  /**
   * Get userdisplay from database
   *
   * @param username The gitlab user id
   * @return display
   */
  public boolean getUserDisplay(String username) {
    String query = "SELECT * FROM User WHERE username = ?";
    boolean display = true;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, username);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          display = rs.getBoolean(DISPLAY);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return display;
  }

  /**
   * Set user display in database
   *
   * @param username The gitlab user id
   */
  public void setUserDisplay(String username) {
    String query = "UPDATE User SET display= ? WHERE username = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setBoolean(1, false);
      preStmt.setString(2, username);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * List all the database user
   * 
   * @return list of user
   */
  public List<User> listAllUsers() {
    List<User> lsUsers = new ArrayList<>();
    String sql = "SELECT * FROM User";

    try (Connection conn = database.getConnection(); Statement stmt = conn.createStatement()) {
      try (ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
          int id = rs.getInt("id");
          int gitLabId = rs.getInt(GIT_LAB_ID);
          String username = rs.getString(USERNAME);
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String gitLabToken = rs.getString(GIT_LAB_ID);

          User user = new User();
          user.setId(id);
          user.setGitLabId(gitLabId);
          user.setUsername(username);
          user.setName(name);
          user.setPassword(password);
          user.setEmail(email);
          user.setGitLabToken(gitLabToken);
          lsUsers.add(user);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsUsers;
  }

  /**
   * check username
   * 
   * @param username studentId
   * @return isExist
   */
  public boolean checkUsername(String username) {
    boolean isExist = false;
    String query = "SELECT count(*) FROM User WHERE username = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, username);
      try (ResultSet rs = preStmt.executeQuery()) {
        rs.next();
        if (rs.getInt("count(*)") > 0) {
          isExist = true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isExist;
  }

  /**
   * check e-mail
   * 
   * @param email e-mail
   * @return isExist
   */
  public boolean checkEmail(String email) {
    boolean isExist = false;
    String query = "SELECT count(*) FROM User WHERE email = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, email);
      try (ResultSet rs = preStmt.executeQuery()) {
        rs.next();
        if (rs.getInt("count(*)") > 0) {
          isExist = true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isExist;
  }
}
