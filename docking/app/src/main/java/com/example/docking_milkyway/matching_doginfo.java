package com.example.docking_milkyway;

public class matching_doginfo {
    String dogSpecies;
    int exerciseNeeds;
    int energyLevel;
    int friendlyTowardStrangers;
    int potentialForPlayfulness;
    int sensitivityLevel;
    int size;
    String userGetMatchRequest;
    String userMatchRequestAccepted;


    public matching_doginfo(){}

    public matching_doginfo(String dogSpecies, int exerciseNeeds, int energyLevel, int friendlyTowardStrangers,
                            int potentialForPlayfulness, int sensitivityLevel, int size,
                            String userGetMatchRequest, String userMatchRequestAccepted  ){
        this.dogSpecies=dogSpecies;
        this.exerciseNeeds=exerciseNeeds;
        this.energyLevel=energyLevel;
        this.friendlyTowardStrangers=friendlyTowardStrangers;
        this.potentialForPlayfulness=potentialForPlayfulness;
        this.sensitivityLevel=sensitivityLevel;
        this.size=size;
        this.userGetMatchRequest=userGetMatchRequest;
        this.userMatchRequestAccepted = userMatchRequestAccepted;
    }

    public String getDogSpecies() {
        return dogSpecies;
    }
    public void setDogSpecies(String dogSpecies) {
        this.dogSpecies = dogSpecies;
    }

    public int getExerciseNeeds() {
        return exerciseNeeds;
    }
    public void setExerciseNeeds(int exerciseNeeds) {
        this.exerciseNeeds = exerciseNeeds;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }
    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getFriendlyTowardStrangers() {
        return friendlyTowardStrangers;
    }
    public void setFriendlyTowardStrangers(int friendlyTowardStrangers) {
        this.friendlyTowardStrangers = friendlyTowardStrangers;
    }

    public int getPotentialForPlayfulness() {
        return potentialForPlayfulness;
    }
    public void setPotentialForPlayfulness(int potentialForPlayfulness) {
        this.potentialForPlayfulness = potentialForPlayfulness;
    }

    public int getSensitivityLevel() {
        return sensitivityLevel;
    }
    public void setSensitivityLevel(int sensitivityLevel) {
        this.sensitivityLevel = sensitivityLevel;
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public String getUserGetMatchRequest() {
        return userGetMatchRequest;
    }
    public void setUserGetMatchRequest(String userGetMatchRequest) {
        this.userGetMatchRequest = userGetMatchRequest;
    }

    public String getUserMatchRequestAccepted() {
        return userMatchRequestAccepted;
    }
    public void setUserMatchRequestAccepted(String userMatchRequestAccepted) {
        this.userMatchRequestAccepted = userMatchRequestAccepted;
    }
}

