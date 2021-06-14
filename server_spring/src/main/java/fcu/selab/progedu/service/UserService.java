package fcu.selab.progedu.service;

import fcu.selab.progedu.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/user")
public class UserService {

  @GetMapping("/getUsers")
  public Greeting getUsers() {

    return null;
  }

}
