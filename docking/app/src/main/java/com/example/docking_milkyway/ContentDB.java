package com.example.docking_milkyway;

//

import java.util.Date;

public class ContentDB {
    String SSN;
    String substance;
    String text;
    Date date;
    boolean tag;
    int like;
    int comment;
    String userSSN;
    String location;

    ContentDB() {}

    ContentDB(String substance, String text, boolean tag, String userSSN) {
        this.SSN = "";
        this.substance = substance;
        this.text = text;
        this.tag = tag;
        this.like = 0;
        this.comment = 0;
        this.userSSN = userSSN;
        this.location = "";
    }

    ContentDB(String substance, String text, boolean tag, String userSSN, String location){
        this.SSN = "";
        this.substance = substance;
        this.text = text;
        this.tag = tag;
        this.like = 0;
        this.comment = 0;
        this.userSSN = userSSN;
        this.location = location;
    }

    //public String getSSN() {return SSN;}
    public String getsubstance() {return substance;}
    public String gettext() {return text;}
    public Date getdate() {return date;}
    public boolean gettag() {return tag;}
    public int getlike() {return like;}
    public String getuserSSN() {return userSSN;}

    //public void setSSN(String SSN) { this.SSN = SSN; }
    public void setSubstance(String substance) { this.substance = substance; }
    public void setText(String text) { this.text = text; }
    public void setDate(Date date) { this.date = date; }
    public void setTag(boolean tag) { this.tag = tag; }
    public void setLike(int like) { this.like = like; }
    public void setUserSSN(String userSSN) { this.userSSN = userSSN; }

    @Override
    public String toString() {
        String thisstring = "SSN:"+SSN+",text:"+text;
        return thisstring;
    }
}
