package com.pet.adoption.activities.fragments.pet;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pet.adoption.R;
import com.pet.adoption.entities.Account;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.services.common.ClipboardHelper;
import com.pet.adoption.services.firebase.Function;
import com.pet.adoption.services.firebase.FirestoreHelper;

public class PetDetailsActivity extends AppCompatActivity {

    private LinearLayout ll_save;
    private TextView tv_save;
    private PetInfo info;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().hasExtra("data")) {
            Toast.makeText(this, "Error data", Toast.LENGTH_LONG).show();
            return;
        }
        info = (PetInfo) getIntent().getSerializableExtra("data");
        setContentView(R.layout.activity_pet_details);
        onLoad();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void onLoad(){
        ll_save = findViewById(R.id.ll_saved);
        tv_save = findViewById(R.id.tv_save);

        setEventHandler();
        setDefaultValue();
    }

    private void setDefaultValue(){
        ((TextView)findViewById(R.id.tv_name)).setText(info.getName());
        ((TextView)findViewById(R.id.tv_age)).setText(info.getAge());
        ((TextView)findViewById(R.id.tv_gender)).setText(info.getGender());
        ((TextView)findViewById(R.id.tv_tag1)).setText(info.getSpecies());
        ((TextView)findViewById(R.id.tv_tag2)).setText(info.getStatus());
        ((TextView)findViewById(R.id.tv_tag3)).setText(info.getSize());
        ((TextView)findViewById(R.id.tv_info)).setText(info.getDescription());
        ((TextView)findViewById(R.id.tv_location)).setText(info.getState());

        loadPetImage();
        loadUsername();
        loadSaveStatus();
    }

    private void setEventHandler(){
        findViewById(R.id.tv_back).setOnClickListener(e -> finish());
        findViewById(R.id.ll_saved).setOnClickListener(e -> onClickBtnSave());
        findViewById(R.id.ll_whatsapp).setOnClickListener(e -> onClickBtnWhatsApp());
        findViewById(R.id.ll_application).setOnClickListener(e -> onClickBtnApplication());
    }

    private void onClickBtnSave(){
        String mode = tv_save.getText().toString();
        info.favourite(mode)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(PetDetailsActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mode.equals("Save")){
                        updateSaveButtonLayout(R.color.green);
                    }
                    else{
                        updateSaveButtonLayout(R.color.yellow);
                    }
                });
    }

    private void onClickBtnWhatsApp(){
        info.contactShelter(this);
    }

    private void onClickBtnApplication(){
        String form = "Basic Information\n" +
                "1. Name:\n" +
                "2. Age:\n" +
                "3. Phone Number:\n" +
                "4. Home Address:\n\n" +

                "Household Information\n" +
                "1. Do all family members agree to adopt pet?\n" +
                "2. Do you have other pets? If yes, specify type and quantity of pets.\n" +
                "3. Have you ever owned a pet before? If yes, describe your experience.\n\n" +

                "Living Situation\n" +
                "1. Type of Residence:\n" +
                "2. Is pet ownership allowed in your residence?\n\n" +

                "Financial and Employment Information\n" +
                "1. Employment Type:\n" +
                "2. Are you financially able to support a pet?\n" +
                "3. Monthly Budget for Pet Expenses:\n\n" +

                "Adoption Intentions\n" +
                "1. Name of Pet You Wish to Adopt:\n" +
                "2. Why do you want to adopt this pet?:\n\n" +

                "Pet Care Arrangements\n" +
                "1. Time Available Daily for Pet Care:\n" +
                "2. Arrangements for Pet When You're Away:\n\n" +

                "Commitment Agreement\n" +
                "1. Do you commit to long-term care for your pet, and will not abandon it due to unexpected reasons?\n" +
                "2. Are you willing to allow a post-adoption home visit?\n";
        ClipboardHelper.copyTextToClipboard(getBaseContext(), form);
    }

    private void loadPetImage(){
        Function.loadAndSetImage(this,findViewById(R.id.iv_pet_image), info.getFileName());
    }

    private void loadUsername(){
        FirestoreHelper.loadAccount(info.getPublisherUID())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(PetDetailsActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Account acc = task.getResult();
                    ((TextView) findViewById(R.id.tv_username)).setText(acc.getUsername());
                });
    }

    private void loadSaveStatus(){
        info.isSaved()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(PetDetailsActivity.this,
                                task.getException().getMessage(),  // Error message
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (task.getResult()){
                        updateSaveButtonLayout(R.color.green);
                    }
                });
    }

    private void updateSaveButtonLayout(@ColorRes int id){
        ll_save.setBackgroundTintList(ContextCompat.getColorStateList(this, id));
        if (id == R.color.yellow){
            tv_save.setText("Save");
        }
        else
            tv_save.setText("Saved");
    }

}