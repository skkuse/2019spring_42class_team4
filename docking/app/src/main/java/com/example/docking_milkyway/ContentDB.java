package com.example.docking_milkyway;

//

public class ContentDB {
    int SSN;
    String substance;
    String text;
    int date;
    boolean tag;
    int like;
    String userSSN;
    ContentDB() {}

    ContentDB(int SSN, String substance, String text, int date, boolean tag, int like, String userSSN) {
        this.SSN = SSN;
        this.substance = substance;
        this.text = text;
        this.date = date;
        this.tag = tag;
        this.like = like;
        this.userSSN = userSSN;
    }

    //테스트용
    ContentDB(String text, boolean tag, String userSSN){
        this.SSN = 0;
        this.substance = "substance";
        this.text = text;
        this.date = 190101;
        this.tag = tag;
        this.like = 0;
        this.userSSN = userSSN;
    }

    ContentDB(int SSN, String substance, String text, int date, String userSSN) {
        this.SSN = SSN;
        this.substance = substance;
        this.text = text;
        this.date = date;
        this.tag = false;
        this.like = 0;
        this.userSSN = userSSN;
    }

    //test용
    ContentDB(int SSN, String text){
        this.SSN = SSN;
        this.text = text;
    }

    public String gettitle() {return text;}
    public String getsubstance() {return substance;}
    public String gettext() {return substance;}
    public int getdate() {return date;}
    public boolean gettag() {return tag;}
    public int getlike() {return like;}
    public String getuserSSN() {return userSSN;}

    public void setSSN(int SSN) { this.SSN = SSN; }
    public void setSubstance(String substance) { this.substance = substance; }
    public void setText(String text) { this.text = text; }
    public void setDate(int date) { this.date = date; }
    public void setTag(boolean tag) { this.tag = tag; }
    public void setLike(int like) { this.like = like; }
    public void setUserSSN(String userSSN) { this.userSSN = userSSN; }
}
