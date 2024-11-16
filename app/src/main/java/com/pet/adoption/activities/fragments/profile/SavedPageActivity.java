package com.pet.adoption.activities.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pet.adoption.R;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.activities.fragments.pet.PetInfoAdapter;
import com.pet.adoption.services.firebase.FirestoreHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PetInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pet);
        onLoad();
    }

    @SuppressLint("SetTextI18n")
    private void onLoad(){
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.spinner_country).setVisibility(View.GONE);
        findViewById(R.id.ll_spinner_1).setVisibility(View.GONE);
        findViewById(R.id.ll_spinner_2).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tv_title)).setText("Saved Page");
        findViewById(R.id.tv_back).setOnClickListener(e -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPetListFromDB();
    }

    private void loadPetListFromDB(){
        FirestoreHelper.loadPetList()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(
                                SavedPageActivity.this
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_LONG
                        ).show();
                        return;
                    }
                    list = task.getResult();
                    loadSavedListFromDB();
                });
    }

    private void loadSavedListFromDB(){
        FirestoreHelper.loadSavedList()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(
                                SavedPageActivity.this
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_LONG
                        ).show();
                        return;
                    }
                    List<String> uidList = task.getResult();
                    List<PetInfo> savedList = new ArrayList<>();
                    for (int i = 0 ; i < uidList.size(); i++){
                        for(int j = 0; j < list.size(); j++){
                            if (uidList.get(i).equals(list.get(j).getPetInfoUID())){
                                savedList.add(list.get(j));
                            }
                        }
                    }
                    displayList(savedList);
                });
    }

    private void displayList(List<PetInfo> petInfoList){
        if (petInfoList.isEmpty())
            Toast.makeText(SavedPageActivity.this, "No records found", Toast.LENGTH_LONG).show();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PetInfoAdapter adapter = new PetInfoAdapter(this, petInfoList);
        recyclerView.setAdapter(adapter);
    }

}