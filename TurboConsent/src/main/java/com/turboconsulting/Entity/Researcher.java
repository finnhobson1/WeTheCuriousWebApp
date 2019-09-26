package com.turboconsulting.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Researcher {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String uname;
    private String password;
}
