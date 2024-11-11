package com.pet.adoption.activities.fragments.pet;

import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pet.adoption.R;
import com.pet.adoption.services.CommonListener;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.services.PetSearchFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetFragment extends Fragment {

    private RecyclerView recyclerView;
    List<PetInfo> list;
    private static PetFragment instance;
    private String[] arr_countries;
    private PetSearchFilter filter;
    private Spinner sp_state, sp_size, sp_age, sp_type, sp_species, sp_gender;
    CommonListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pet, container, false);
        list = new ArrayList<>();
        instance = this;
        filter = new PetSearchFilter(getContext());
        onLoad(v);
        return v;
    }

    private void onLoad(View v) {
        v.findViewById(R.id.tv_back).setVisibility(View.GONE);
        this.arr_countries = getArrayById(R.array.array_states, "Whole Country");
        listener = new CommonListener(v, getClass());
        recyclerView = v.findViewById(R.id.recyclerView);
        getPetInfoListFromDB();
        sp_type = v.findViewById(R.id.spinner_type);
        sp_species = v.findViewById(R.id.spinner_species);
        sp_age = v.findViewById(R.id.spinner_age);
        sp_size = v.findViewById(R.id.spinner_size);
        sp_gender = v.findViewById(R.id.spinner_gender);

        sp_type.setAdapter(getAdapter(R.array.array_pet_type, "Type"));
        sp_species.setAdapter(getAdapter(R.array.array_empty, "Species"));
        sp_age.setAdapter(getAdapter(R.array.array_pet_age, "Age"));
        sp_size.setAdapter(getAdapter(R.array.array_pet_size, "Size"));
        sp_gender.setAdapter(getAdapter(R.array.array_gender, "Gender"));

        sp_type.setOnItemSelectedListener(spinnerListener);
        sp_size.setOnItemSelectedListener(spinnerListener);
        sp_age.setOnItemSelectedListener(spinnerListener);
        sp_species.setOnItemSelectedListener(spinnerListener);
        sp_species.setOnTouchListener(listener.getSp_species_touch_listener());

        ArrayAdapter<String> adapter = getAdapter(R.array.array_states, "Whole Country");
        sp_state = v.findViewById(R.id.spinner_country); // Replace with your Spinner ID
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_state.setAdapter(adapter);
        sp_state.setOnItemSelectedListener(spinnerListener);
    }

    public static PetFragment getInstance() {
        return instance;
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

                    list = new ArrayList<>();
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

    protected void displayList(List<PetInfo> petInfoList){
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        PetInfoAdapter adapter = new PetInfoAdapter(requireContext(), petInfoList);
        recyclerView.setAdapter(adapter);
    }

    protected String[] getArrayById(@ArrayRes int id, String firstElement){
        String[] values = getResources().getStringArray(id);
        values[0] = firstElement;
        return values;
    }

    private ArrayAdapter<String> getAdapter(@ArrayRes int id, String firstElement){
        String[] values = getArrayById(id, firstElement);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent == sp_type){
                listener.valueChangeSPType();
            }
            else if (parent == sp_size){
                listener.valueChangeSPSize();
            }

            List<PetInfo> filteredList = filter.filter(list,
                    sp_type.getSelectedItemPosition(), sp_age.getSelectedItemPosition(),
                    sp_size.getSelectedItemPosition(), sp_state.getSelectedItemPosition(),
                    sp_species.getSelectedItemPosition(),
                    sp_gender.getSelectedItemPosition());

            displayList(filteredList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}