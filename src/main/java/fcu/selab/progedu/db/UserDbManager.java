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

import fcu.selab.progedu.data.User;
import fcu.selab.progedu.service.RoleEnum;

public class UserDbManager {
  private static final String GIT_LAB_ID = "gitLabId";
  private static final String USERNAME = "username";
  private static final String NAME = "name";
  private static final String PASSWORD = "password";
  private static final String EMAIL = "email";
  private static final String GIT_LAB_TOKEN = "gitLabToken";
  private static final String ROLE = "role";
  private static final String DISPLAY = "display";
  private static UserDbManager dbManager = null;

  /**
   * 
   * @return dbManager
   */
  public static UserDbManager getInstance() {
    if (dbManager == null) {
      dbManager = new UserDbManager();
    }
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();
  RoleDbManager rdb = RoleDbManager.getInstance();
  RoleUserDbManager rudb = RoleUserDbManager.getInstance();

  private UserDbManager() {

  }

  /**
   * Add gitlab user to database
   * 
   * @param user The gitlab user
   */
  public void addUser(User user) {
    String sql = "INSERT INTO " + "User(" + GIT_LAB_ID + "," + USERNAME + "," + NAME + ","
        + PASSWORD + "," + EMAIL + "," + GIT_LAB_TOKEN + "," + DISPLAY + ")"
        + "VALUES(?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, user.getGitLabId());
      preStmt.setString(2, user.getUsername());
      preStmt.setString(3, user.getName());
      preStmt.setString(4, passwordMD5(user.getPassword()));
      preStmt.setString(5, user.getEmail());
      preStmt.setString(6, user.getGitLabToken());
      preStmt.setBoolean(7, user.getDisplay());
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
    String query = "SELECT password FROM User WHERE username = ?";

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
      String newPass = passwordMD5(password);
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
    if (currPassword.equals(passwordMD5(password))) {
      check = true;
    }
    return check;
  }

  /**
   * user name to find userId in db
   * 
   * @return id
   */
  public int getUserIdByUsername(String username) {
    String query = "SELECT id FROM User WHERE username = ?";
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
   * user name to find userId in db
   * 
   * @return id
   */
  public int getGitLabIdByUsername(String username) {
    String query = "SELECT gitLabId FROM User WHERE username = ?";
    int id = -1;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, username);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          id = rs.getInt("gitLabId");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
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
          boolean display = rs.getBoolean(DISPLAY);

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
   * List all the database user
   * 
   * @return list of user
   */
  public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
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
          boolean display = rs.getBoolean(DISPLAY);
          List<RoleEnum> roleList = rudb.getRoleList(id);

          User user = new User();
          user.setId(id);
          user.setGitLabId(gitLabId);
          user.setUsername(username);
          user.setName(name);
          user.setPassword(password);
          user.setEmail(email);
          user.setGitLabToken(gitLabToken);
          user.setDisplay(display);
          user.setRole(roleList);
          users.add(user);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
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

  /**
   * Get user from database
   *
   * @param id The db user id
   * @return user
   */
  public String getUsername(int id) {
    String name = "";
    String query = "SELECT username FROM User WHERE id = ?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
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
}
