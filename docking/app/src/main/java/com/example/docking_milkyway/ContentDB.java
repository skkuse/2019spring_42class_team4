package com.example.docking_milkyway;

//

public class ContentDB {
    int SSN;
    String substance;
    String text;
    int date;
    boolean tag;
    int like;
    int userSSN;
    ContentDB() {}

    ContentDB(int SSN, String substance, String text, int date, boolean tag, int like, int userSSN) {
        this.SSN = SSN;
        this.substance = substance;
        this.text = text;
        this.date = date;
        this.tag = tag;
        this.like = like;
        this.userSSN = userSSN;
    }

    ContentDB(int SSN, String substance, String text, int date, int userSSN) {
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
    public int getuserSSN() {return userSSN;}

    public void setSSN(int SSN) { this.SSN = SSN; }
    public void setSubstance(String substance) { this.substance = substance; }
    public void setText(String text) { this.text = text; }
    public void setDate(int date) { this.date = date; }
    public void setTag(boolean tag) { this.tag = tag; }
    public void setLike(int like) { this.like = like; }
    public void setUserSSN(int userSSN) { this.userSSN = userSSN; }
}
