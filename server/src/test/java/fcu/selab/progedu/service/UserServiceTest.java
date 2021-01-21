package fcu.selab.progedu.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {


    UserService userService = UserService.getInstance();
    @Test
    public void createAccount() {
        userService.createAccount("unit-test-user", "unit-test-username",
                "unit-test-user@unit-test-user", "unit-test-user","student", true);

    }
}