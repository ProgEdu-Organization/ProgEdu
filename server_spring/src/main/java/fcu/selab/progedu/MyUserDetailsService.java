package fcu.selab.progedu;

import fcu.selab.progedu.config.GitlabConfig;
import fcu.selab.progedu.db.UserDbManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

  private GitlabConfig gitlabConfig = GitlabConfig.getInstance();

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // 以下可以用在單純要測試API時, 登入就用 test / password , 平常時註解起來就好
//    if(username.equals("test")) {
//      String testPassword = UserDbManager.getInstance().passwordMD5("password");
//
//      User testUser = new User(username, testPassword,
//              AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_TEACHER"));
//      return testUser;
//    }


    try {
      if (username.equals(gitlabConfig.getGitlabRootUsername())) {

        String password = gitlabConfig.getGitlabRootPassword();
        password = UserDbManager.getInstance().passwordMD5(password);

        User user = new User(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_TEACHER"));
        return user;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }



    String password = UserDbManager.getInstance().getPassword(username);
    if(!password.equals("")) {
      User user = new User(username,
              password,
              AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_STUDENT"));//設定許可權和角色
      return user;
    }

    return null;
  }
}
