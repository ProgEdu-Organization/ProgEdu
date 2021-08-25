package fcu.selab.progedu;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    if(s.equals("franky")) {
      User user = new User("franky",
              "franky-password",
              AuthorityUtils.commaSeparatedStringToAuthorityList("read,ROLE_USER"));//設定許可權和角色
      return user;
    }
    return null;
  }
}
