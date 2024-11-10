package com.pet.adoption.activities.fragments.pet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pet.adoption.R;
import com.pet.adoption.entities.Account;
import com.pet.adoption.entities.PetInfo;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        loadSaveStatus();
        setEventHandler();
        setDefaultValue();
        ImageView iv = findViewById(R.id.iv_pet_image);
        StorageReference ref = FirebaseStorage.getInstance().getReference("images").child(info.getFileName());
        File file = null;
        try {
            file = File.createTempFile(info.getFileName(), "");
            File finalFile = file;
            ref.getFile(file)
                    .addOnCompleteListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                        iv.setImageBitmap(bitmap);

                    })
                    .addOnFailureListener(task -> {
                        Toast.makeText(PetDetailsActivity.this
                                , task.getMessage()
                                , Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            Toast.makeText(PetDetailsActivity.this
                    , e.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
        finally {
            if (file != null && file.exists()){
                Boolean deleted = file.delete();
            }
        }
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

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(info.getPublisherUID())
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(PetDetailsActivity.this
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DocumentSnapshot doc = task.getResult();
                    if (!doc.exists()){
                        Toast.makeText(PetDetailsActivity.this
                                , "User name not found"
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Account acc = doc.toObject(Account.class);
                    assert acc != null;
                    ((TextView)findViewById(R.id.tv_username)).setText(acc.getUsername());
                });
    }

    private void setEventHandler(){
        findViewById(R.id.tv_back).setOnClickListener(e -> finish());
        findViewById(R.id.ll_saved).setOnClickListener(e -> onClickBtnSave());
    }

    private void onClickBtnSave(){
        FirebaseFirestore.getInstance()
                .collection("save")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) return;

                    List<String> uidList = (List<String>) snapshot.get("list");
                    if (uidList == null)
                        uidList = Collections.emptyList();
                });
    }

    private void loadSaveStatus(){
        FirebaseFirestore.getInstance()
                .collection("save")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists())
                        return;

                    List<String> saveList = (List<String>) snapshot.get("list");
                    if (saveList == null)
                        return;

                    if (saveList.contains(info.getPetInfoUID())){
                        ll_save.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
                    }
                });
    }

}