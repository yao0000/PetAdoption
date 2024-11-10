package com.pet.adoption.activities.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.adoption.R;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.entities.PetInfoAdapter;

import java.util.List;

public class SavedPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
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

    private void displayList(List<PetInfo> petInfoList){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PetInfoAdapter adapter = new PetInfoAdapter(this, petInfoList);
        recyclerView.setAdapter(adapter);
    }

}