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

import com.pet.adoption.R;
import com.pet.adoption.services.common.Listener;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.entities.PetList;
import com.pet.adoption.services.firebase.FirestoreHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    List<PetInfo> list;
    private PetList petList;
    private Spinner sp_state, sp_size, sp_age, sp_type, sp_species, sp_gender, sp_status;
    Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pet, container, false);
        list = new ArrayList<>();
        petList = new PetList(getContext(), list);
        onLoad();
        return view;
    }

    private void onLoad() {
        listener = new Listener(view, getClass());
        view.findViewById(R.id.tv_back).setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recyclerView);
        sp_type = view.findViewById(R.id.spinner_type);
        sp_species = view.findViewById(R.id.spinner_species);
        sp_age = view.findViewById(R.id.spinner_age);
        sp_size = view.findViewById(R.id.spinner_size);
        sp_gender = view.findViewById(R.id.spinner_gender);
        sp_status = view.findViewById(R.id.spinner_status);
        sp_state = view.findViewById(R.id.spinner_country);

        loadPetListFromDB();
        setSpinner();
        setEventHandler();
    }

    private void setSpinner(){
        sp_type.setAdapter(getAdapter(R.array.array_pet_type, "Type"));
        sp_species.setAdapter(getAdapter(R.array.array_empty, "Species"));
        sp_age.setAdapter(getAdapter(R.array.array_pet_age, "Age"));
        sp_size.setAdapter(getAdapter(R.array.array_pet_size, "Size"));
        sp_gender.setAdapter(getAdapter(R.array.array_gender, "Gender"));
        sp_status.setAdapter(getAdapter(R.array.array_neuter_status, "Status"));
        sp_state.setAdapter(getAdapter(R.array.array_states, "Whole Country"));
    }

    private void setEventHandler(){
        sp_type.setOnItemSelectedListener(spinnerListener);
        sp_size.setOnItemSelectedListener(spinnerListener);
        sp_age.setOnItemSelectedListener(spinnerListener);
        sp_species.setOnItemSelectedListener(spinnerListener);
        sp_status.setOnItemSelectedListener(spinnerListener);
        sp_state.setOnItemSelectedListener(spinnerListener);
        sp_gender.setOnItemSelectedListener(spinnerListener);
        sp_species.setOnTouchListener(listener.getSp_species_touch_listener());
    }

    private void loadPetListFromDB(){
        FirestoreHelper.loadPetList()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(
                                requireContext()
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    list.clear();
                    list.addAll(task.getResult());
                    displayList(list);

                    if(list.isEmpty())
                        Toast.makeText(requireContext(), "No record found!", Toast.LENGTH_SHORT)
                                .show();
                });
    }

    protected void displayList(List<PetInfo> petInfoList){
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        PetInfoAdapter adapter = new PetInfoAdapter(requireContext(), petInfoList);
        recyclerView.setAdapter(adapter);
    }

    private ArrayAdapter<String> getAdapter(@ArrayRes int id, String firstElement){
        String[] values = getResources().getStringArray(id);
        values[0] = firstElement;
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

            List<PetInfo> filteredList = petList.filter(
                    sp_type.getSelectedItemPosition(), sp_age.getSelectedItemPosition(),
                    sp_size.getSelectedItemPosition(), sp_state.getSelectedItemPosition(),
                    sp_species.getSelectedItemPosition(),
                    sp_gender.getSelectedItemPosition(),
                    sp_status.getSelectedItemPosition());

            displayList(filteredList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}