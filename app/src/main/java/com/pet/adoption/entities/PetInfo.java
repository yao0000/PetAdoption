package com.pet.adoption.entities;

import com.google.android.gms.tasks.Task;
import com.pet.adoption.services.FirestoreHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class PetInfo implements Serializable {

    private String fileName;
    private String petInfoUID;
    private long timeStamp;

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
    private String publisherUID;

    public PetInfo() {
    }

    public PetInfo(String fileName, String name, String age, String type, String size,
                   String species, String status, String contact, String gender,
                   String state, String description, String publisherUID) {
        this.petInfoUID = publisherUID + "_" + System.currentTimeMillis();
        this.timeStamp = System.currentTimeMillis();
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
        this.publisherUID = publisherUID;
    }

    public String getPetInfoUID() {
        return petInfoUID;
    }

    public void setPetInfoUID(String petInfoUID) {
        this.petInfoUID = petInfoUID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostingTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(timeStamp);
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

    public String getPublisherUID() {
        return publisherUID;
    }

    public void setPublisherUID(String publisherUID) {
        this.publisherUID = publisherUID;
    }

    public Task<Void> post(){
        FirestoreHelper helper = new FirestoreHelper();
        return helper.upload("pets", petInfoUID, this);
    }
}
