package com.example.docking_milkyway;

public class UserDB {
    String email;
    String name;
    String pw;
    String nickname;
    int age;
    String sex;
    String usertype;

    int count = 0;

    UserDB(){
    }

    UserDB(String email, String name, String pw, String nickname, int age, String sex, String usertype){
        this.email = email;
        this.name = name;
        this.pw = pw;
        this.nickname = nickname;
        this.age = age;
        this.sex = sex;
        this.usertype = usertype;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
