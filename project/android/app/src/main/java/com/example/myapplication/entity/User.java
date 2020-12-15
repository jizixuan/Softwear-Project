package com.example.myapplication.entity;

public class User {
    private String photo;//头像
    private String sex;//性别
    private String name;//昵称
    private String pwd;//密码
    private String phone;//电话号码
    private int id;//用户id

    public User() {
        super();
    }

    public User(String photo, String sex, String name, String pwd, String phone, int id) {
        this.photo = photo;
        this.sex = sex;
        this.name = name;
        this.pwd = pwd;
        this.phone = phone;
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "photo='" + photo + '\'' +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", phone='" + phone + '\'' +
                ", id=" + id +
                '}';
    }
}
