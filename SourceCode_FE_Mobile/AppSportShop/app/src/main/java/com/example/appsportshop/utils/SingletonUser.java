package com.example.appsportshop.utils;


public class SingletonUser {
    private static SingletonUser instance;
    private long idUser;
    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String birthday;

    private String phone;
    private String role;
    private String avatarUrl;
    private String publicId;

    private String adress;
    private String token;

    private SingletonUser() {
        // Private constructor to prevent instantiation from outside the class
    }
    public void clearValues() {
        instance = null;
    }
    public static SingletonUser getInstance() {
        if (instance == null) {
            synchronized (SingletonUser.class) {
                if (instance == null) {
                    instance = new SingletonUser();
                }
            }
        }
        return instance;
    }

    @Override
    public String toString() {
        return "SingletonUser{" +
                "idUser=" + idUser +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +

                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", publicId='" + publicId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getAdress() {
        return adress;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public static void setInstance(SingletonUser instance) {
        SingletonUser.instance = instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
