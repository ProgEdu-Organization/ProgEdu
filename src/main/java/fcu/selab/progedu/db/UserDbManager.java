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
  private static final String USER_NAME = "userName";
  private static final String PASSWORD = "password";
  private static final String GIT_LAB_ID = "gitLabId";
  private static final String EMAIL = "email";
  private static final String PRIVATE_TOKEN = "privateToken";
  private static final String NAME = "name";

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
   * @param user
   *          The gitlab user
   */
  public void addUser(GitlabUser user) {
    String sql = "INSERT INTO " + "User(gitLabId, userName, name, password, email, privateToken)  "
        + "VALUES(?, ?, ?, ?, ?, ?)";

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
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * encrypt the user password
   * 
   * @param password
   *          The user's password
   * @return MD5 string
   * @throws NoSuchAlgorithmException
   *           on security api call error
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
   * @param userName
   *          user stu id
   * @return password
   */
  public String getPassword(String userName) {
    String password = "";
    String query = "SELECT * FROM User WHERE userName = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, userName);
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
   * @param userName
   *          user stu id
   * @param password
   *          user new password
   */
  public void modifiedUserPassword(String userName, String password) {
    String query = "UPDATE User SET password=? WHERE userName = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      String newPass = passwordMD5(password) + userName;
      preStmt.setString(1, newPass);
      preStmt.setString(2, userName);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * check old password
   * 
   * @param userName
   *          user stu id
   * @param password
   *          user old password
   * @return T or F
   */
  public boolean checkPassword(String userName, String password) {
    String currPassword = getPassword(userName);
    String newPassword = passwordMD5(password) + userName;
    boolean check = false;
    if (currPassword.equals(newPassword)) {
      check = true;
    }
    return check;
  }

  /**
   * Get user from database
   * 
   * @param userName
   *          The gitlab user name
   * @return user
   */
  public User getUser(String userName) {
    User user = new User();
    String query = "SELECT * FROM User WHERE userName = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, userName);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int gitLabId = rs.getInt(GIT_LAB_ID);
          int id = rs.getInt("id");
          String stuId = userName;
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String privateToken = rs.getString(PRIVATE_TOKEN);

          user.setGitLabId(gitLabId);
          user.setId(id);
          user.setUserName(stuId);
          user.setName(name);
          user.setPassword(password);
          user.setEmail(email);
          user.setPrivateToken(privateToken);
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
   * @param id
   *          The gitlab user id
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
          String stuId = rs.getString(USER_NAME);
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String privateToken = rs.getString(PRIVATE_TOKEN);

          user.setGitLabId(gitLabId);
          user.setId(id);
          user.setUserName(stuId);
          user.setName(name);
          user.setPassword(password);
          user.setEmail(email);
          user.setPrivateToken(privateToken);
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
   * @param userId
   *          The db user id
   * @return user
   */
  public String getName(int userId) {
    String name = "";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(QUERY)) {
      preStmt.setInt(1, userId);
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
   * @param userId
   *          The db user id
   * @return user
   */
  public String getUserName(int userId) {
    String name = "";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(QUERY)) {
      preStmt.setInt(1, userId);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          name = rs.getString(USER_NAME);
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
   * @param name
   *          user's name
   * @return id
   */
  public int getUserId(String name) {
    String query = "SELECT * FROM User WHERE name = ?";
    int id = -1;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, name);
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
   * user name to find userId in db
   * 
   * @param username
   *          user's name
   * @return id
   */
  public int getUserIdByUsername(String username) {
    String query = "SELECT * FROM User WHERE userName = ?";
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
          String stuId = rs.getString(USER_NAME);
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String privateToken = rs.getString(PRIVATE_TOKEN);

          User user = new User();
          user.setId(id);
          user.setGitLabId(gitLabId);
          user.setUserName(stuId);
          user.setName(name);
          user.setPassword(password);
          user.setEmail(email);
          user.setPrivateToken(privateToken);
          lsUsers.add(user);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lsUsers;
  }
}
