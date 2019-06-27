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
  private static final String STUDENT_ID = "studentId";
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
   * @param user
   *          The gitlab user
   */
  public void addUser(GitlabUser user) {
    String sql = "INSERT INTO "
        + "User(gitLabId, studentId, name, password, email, gitLabToken, display)  "
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
   * @param studentId
   *          user stu id
   * @return password
   */
  public String getPassword(String studentId) {
    String password = "";
    String query = "SELECT * FROM User WHERE studentId = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, studentId);
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
   * @param studentId
   *          user stu id
   * @param password
   *          user new password
   */
  public void modifiedUserPassword(String studentId, String password) {
    String query = "UPDATE User SET password=? WHERE studentId = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      String newPass = passwordMD5(password) + studentId;
      preStmt.setString(1, newPass);
      preStmt.setString(2, studentId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * check old password
   * 
   * @param studentId
   *          user stu id
   * @param password
   *          user old password
   * @return T or F
   */
  public boolean checkPassword(String studentId, String password) {
    boolean check = false;
    String currPassword = getPassword(studentId);
    if (currPassword.equals(passwordMD5(password) + studentId)) {
      check = true;
    }
    return check;
  }

  /**
   * Get user from database
   * 
   * @param studentId
   *          The gitlab user name
   * @return user
   */
  public User getUser(String studentId) {
    User user = new User();
    String query = "SELECT * FROM User WHERE studentId = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, studentId);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          int gitLabId = rs.getInt(GIT_LAB_ID);
          int id = rs.getInt("id");
          String stuId = studentId;
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String gitLabToken = rs.getString(GIT_LAB_TOKEN);

          user.setGitLabId(gitLabId);
          user.setId(id);
          user.setStudentId(stuId);
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
          String stuId = rs.getString(STUDENT_ID);
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String gitLabToken = rs.getString(GIT_LAB_TOKEN);

          user.setGitLabId(gitLabId);
          user.setId(id);
          user.setStudentId(stuId);
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
  public String getStudentId(int userId) {
    String name = "";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(QUERY)) {
      preStmt.setInt(1, userId);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          name = rs.getString(STUDENT_ID);
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
   * @param studentId
   *          user's name
   * @return id
   */
  public int getUserByStudentId(String studentId) {
    String query = "SELECT * FROM User WHERE studentId = ?";
    int id = -1;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, studentId);
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
   * @param studentId
   *          The gitlab user id
   * @return user
   */
  public boolean getUserDisplay(String studentId) {
    String query = "SELECT * FROM User WHERE studentId = ?";
    boolean display = true;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, studentId);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          display = rs.getBoolean("display");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return display;
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
          String stuId = rs.getString(STUDENT_ID);
          String name = rs.getString(NAME);
          String password = rs.getString(PASSWORD);
          String email = rs.getString(EMAIL);
          String gitLabToken = rs.getString(GIT_LAB_ID);

          User user = new User();
          user.setId(id);
          user.setGitLabId(gitLabId);
          user.setStudentId(stuId);
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
   * @param studentId
   *          studentId
   * @return isExist
   */
  public boolean checkStudentId(String studentId) {
    boolean isExist = false;
    String query = "SELECT count(*) FROM User WHERE studentId = ?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setString(1, studentId);
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
   * @param email
   *          e-mail
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
