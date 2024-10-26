package com.pet.adoption.entities;

import java.util.List;

public class PetInfo {
    private String time;
    private List<String> tags;
    private String fileName;
    private String ownerUID;

    private String name;
    private String age;
    private String type;
    private String size;
    private String species;
    private String status;
    private String contact;
    private Boolean gender;
    private String state;
    private String description;

    public PetInfo(String time, String description, List<String> tags, String fileName, String ownerUID) {
        this.time = time;
        this.description = description;
        this.tags = tags;
        this.fileName = fileName;
        this.ownerUID = ownerUID;
    }

    public PetInfo() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTagByIndex(int index){
        return tags.get(index);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
}
