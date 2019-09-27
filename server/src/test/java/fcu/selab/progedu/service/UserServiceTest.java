package fcu.selab.progedu.service;

import org.junit.Test;

public class UserServiceTest {
//
//  public static void main(String[] args) throws IOException {
//    String username = "M0721053";
//    UserService us = UserService.getInstance();
//    us.updateStatus(username);
//  }

  @Test
  public void getGroup() {
    UserService us = UserService.getInstance();
    System.out.println(us.getGroup("test01").getEntity());
  }

}
