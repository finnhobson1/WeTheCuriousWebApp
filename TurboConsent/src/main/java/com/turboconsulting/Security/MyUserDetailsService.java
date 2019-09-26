package com.turboconsulting.Security;

import com.turboconsulting.DAO.AccountDao;
import com.turboconsulting.Entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    private final AccountDao accountDao;

    @Autowired
    public MyUserDetailsService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = accountDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown User");
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNotExpired = true;
        boolean accountNonLocked = true;
        MyUser principal = new MyUser(user, enabled, accountNonExpired, credentialsNotExpired, accountNonLocked);
        return principal;
    }
}

