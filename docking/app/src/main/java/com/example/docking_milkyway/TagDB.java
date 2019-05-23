package com.example.docking_milkyway;

public class TagDB {

    int C_Num;
    int User_SSN;

    public TagDB() { }

    public TagDB(int c_Num, int user_SSN) {
        C_Num = c_Num;
        User_SSN = user_SSN;
    }

    public int getC_Num() {
        return C_Num;
    }

    public void setC_Num(int c_Num) {
        C_Num = c_Num;
    }

    public int getUser_SSN() {
        return User_SSN;
    }

    public void setUser_SSN(int user_SSN) {
        User_SSN = user_SSN;
    }
}
