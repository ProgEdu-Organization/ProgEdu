package fcu.selab.progedu.service;

import java.io.IOException;

public class UserServiceTest {

  public static void main(String[] args) throws IOException {
//    File f = new File("C:\\Users\\hoky\\Downloads\\StudentTemplate.csv");
//    InputStream is = new FileInputStream(f);
//    CsvReader csvReader = new CsvReader(new InputStreamReader(is, "BIG5"));
//    csvReader.readHeaders();
//    while (csvReader.readRecord()) {
////      User newUser = new User();
//      String role = csvReader.get("Role");
//      RoleEnum roleEnum = RoleEnum.getRoleEnum(role);
//      List<RoleEnum> roleList = new ArrayList<>();
//      roleList.add(roleEnum);
//      String username = csvReader.get("Username");
//      String password = csvReader.get("Password");
//      String name = csvReader.get("Name");
//      String email = csvReader.get("Email");
//      System.out.println(username);
//      System.out.println(password);
//      System.out.println(name);
//      System.out.println(email);
    String username = "M0721053";
    UserService us = UserService.getInstance();
    us.updateStatus(username);
  }

}
