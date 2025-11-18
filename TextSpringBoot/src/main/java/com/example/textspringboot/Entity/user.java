package com.example.textspringboot.Entity;

public class user {
    private String userId;
    private String password;
    private String username;
    private String userTel;
    private String userMail;
    private String userAvatarUrl;

    public user() {
    }
    public user(String userId, String password, String username, String userTel, String userMail, String userAvatarUrl) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.userTel = userTel;
        this.userMail = userMail;
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    @Override
    public String toString() {
        return "user{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", userTel='" + userTel + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userAvatarUrl='" + userAvatarUrl + '\'' +
                '}';
    }
}
