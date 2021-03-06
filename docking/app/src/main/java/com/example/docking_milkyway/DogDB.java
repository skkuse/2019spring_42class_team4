package com.example.docking_milkyway;

public class DogDB {
    String SSN;
    String name;
    String species;
    String age;
    String sex;
    int weight;

    public DogDB(){ }

    public DogDB(String SSN, String name) {
        this.SSN = SSN;
        this.name = name;
    }

    public DogDB(String SSN, String name, String species, String age, String sex) {
        this.SSN = SSN;
        this.name = name;
        this.species = species;
        this.age = age;
        this.sex = sex;
    }

    //public int getSSN() {
    //    return SSN;
    //}

    //public void setSSN(int SSN) {
    //    this.SSN = SSN;
    //}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
