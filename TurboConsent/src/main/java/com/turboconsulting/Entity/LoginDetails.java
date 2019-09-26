package com.turboconsulting.Entity;



public class LoginDetails {

    public LoginDetails(){}

    public LoginDetails(String email, String password){
        this.email = email;
        this.pword = password;
    }



    private String email;
    private String pword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }
}
