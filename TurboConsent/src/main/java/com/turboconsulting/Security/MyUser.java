package com.turboconsulting.Security;

import com.turboconsulting.Entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.Set;


public class MyUser extends User {

    private final Account user;

    public MyUser(Account user) {
        super(user.getEmail(), user.getPassword(), user.getAuthorities());
        this.user = user;
    }

    public MyUser(Account user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                  boolean accountNonLocked) {
        super(user.getEmail(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, user.getAuthorities());
        this.user = user;
    }

    public Account getUser() {
        return this.user;
    }


}
