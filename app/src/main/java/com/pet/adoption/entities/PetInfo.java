package com.pet.adoption.entities;

public class PetInfo {

    private String time;
    private String fileName;

    private String name;
    private String age;
    private String type;
    private String size;
    private String species;
    private String status;
    private String contact;
    private String gender;
    private String state;
    private String description;

    public PetInfo() {
    }

    public PetInfo(String time, String fileName, String name, String age, String type, String size, String species, String status, String contact, String gender, String state, String description) {
        this.time = time;
        this.fileName = fileName;
        this.name = name;
        this.age = age;
        this.type = type;
        this.size = size;
        this.species = species;
        this.status = status;
        this.contact = contact;
        this.gender = gender;
        this.state = state;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
