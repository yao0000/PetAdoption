package com.pet.adoption.activities.fragments.pet;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pet.adoption.R;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.entities.PetInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetFragment extends Fragment {
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pet, container, false);
        onLoad(v);
        ((GradientDrawable)v.findViewById(R.id.etSearch).getBackground()).setColor(getResources().getColor(R.color.white));
        return v;
    }


    private void onLoad(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        getPetInfoListFromDB();
        ArrayList<String> countriesList = new ArrayList<>();
        countriesList.add("Whole Country");
        Spinner countrySpinner = v.findViewById(R.id.spinner_country); // Replace with your Spinner ID
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, countriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
    }

    private void getPetInfoListFromDB(){
        FirebaseFirestore.getInstance()
                .collection("pets")
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(
                                requireContext()
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    List<PetInfo> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        list.add(document.toObject(PetInfo.class));
                    }

                    if (list.isEmpty()) {
                        Toast.makeText(
                                requireContext()
                                , "No record found!"
                                , Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    displayList(list);
                });
    }

    private void displayList(List<PetInfo> list){
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        PetInfoAdapter adapter = new PetInfoAdapter(requireContext(), list);
        recyclerView.setAdapter(adapter);
        /*recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pet_info, parent, false);
                return new PetInfoAdapter.PetInfoViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        });*/

    }
}