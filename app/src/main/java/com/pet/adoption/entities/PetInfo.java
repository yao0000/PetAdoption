package com.pet.adoption.entities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.pet.adoption.services.firebase.FirestoreHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void contactShelter(Context context){
        try{
            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + this.contact);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.whatsapp");
            intent.setData(uri);
            context.startActivity(intent);
        }
        catch(Exception e){
            Toast.makeText(context, "Error opening WhatsApp chat", Toast.LENGTH_SHORT).show();
        }
    }

    public Task<Void> post(){
        return FirestoreHelper.upload("pets", petInfoUID, this.toMap());
    }

    public Task<Void> favourite(String mode){
        return FirestoreHelper.favorite(petInfoUID, mode);
    }

    public Task<Boolean> isSaved() {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        FirestoreHelper.loadSavedList()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        taskCompletionSource.setException(task.getException());
                        return;
                    }

                    List<String> list = task.getResult();
                    if (list == null) {
                        taskCompletionSource.setResult(false);
                        return;
                    }

                    taskCompletionSource.setResult(list.contains(PetInfo.this.getPetInfoUID()));
                });

        return taskCompletionSource.getTask();
    }

    private Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("age", this.age);
        map.put("contact", this.contact);
        map.put("description", this.description);
        map.put("fileName", this.fileName);
        map.put("gender", this.gender);
        map.put("name", this.name);
        map.put("petInfoUID", this.petInfoUID);
        map.put("publisherUID", this.publisherUID);
        map.put("size", this.size);
        map.put("species", this.species);
        map.put("state", this.state);
        map.put("status", this.status);
        map.put("timeStamp", this.timeStamp);
        map.put("type", this.type);
        return map;
    }
}
