package com.example.docking_milkyway;

public class CommentDB {
    int C_Num;
    String substance;
    int date;
    int like;
    int parent_Content;
    int User_SSN;
    Integer super_C_Num;

    public CommentDB() { }

    public CommentDB(int c_Num, String substance, int date, int parent_Content, int user_SSN) {
        C_Num = c_Num;
        this.substance = substance;
        this.date = date;
        this.like = 0;
        this.parent_Content = parent_Content;
        User_SSN = user_SSN;
        this.super_C_Num = null;
    }

    public CommentDB(int c_Num, String substance, int date, int parent_Content, int user_SSN, int super_C_Num) {
        C_Num = c_Num;
        this.substance = substance;
        this.date = date;
        this.like = 0;
        this.parent_Content = parent_Content;
        User_SSN = user_SSN;
        this.super_C_Num = super_C_Num;
    }

    public int getC_Num() {
        return C_Num;
    }

    public void setC_Num(int c_Num) {
        C_Num = c_Num;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getParent_Content() {
        return parent_Content;
    }

    public void setParent_Content(int parent_Content) {
        this.parent_Content = parent_Content;
    }

    public int getUser_SSN() {
        return User_SSN;
    }

    public void setUser_SSN(int user_SSN) {
        User_SSN = user_SSN;
    }

    public Integer getSuper_C_Num() {
        return super_C_Num;
    }

    public void setSuper_C_Num(Integer super_C_Num) {
        this.super_C_Num = super_C_Num;
    }
}
