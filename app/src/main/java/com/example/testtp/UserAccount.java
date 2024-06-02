package com.example.testtp;

public class UserAccount {
    public UserAccount() {

    }

    public void setIdToken(String idToken){
        this.idToken = idToken;
    }

    private String idToken;
    public String getEmailId() { return emailId; }
    public void setEmailId(String email){ this.emailId = emailId;}
    private String emailId;
    public String getPassword(){ return password; }
    public void setPassword(String strPwd){this.password = password;}
    private String password;
}
