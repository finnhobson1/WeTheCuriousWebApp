package com.turboconsulting.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int accountId;

    @Column(unique=true)
    private String email;

    private String name, password;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Visitor> visitors;

    public Account(){}

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        visitors = new HashSet<>();
    }

    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public Set<Visitor> getVisitors() {
        return visitors;
    }
    public void setVisitors(Set<Visitor> visitors) {
        this.visitors = visitors;
    }
    public void removeVisitor(Visitor v)  {
        visitors.remove(v);
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        if (email.equals("admin@turboconsent.com")) { authorities.add(new SimpleGrantedAuthority("ADMIN")); }
        return authorities;
    }

    public int getTotalPendingExperiments()  {
        int count = 0;
        for (Visitor v : visitors)  {
            count += v.getPendingExperiments();
        }
        return count;
    }

}
