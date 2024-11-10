package com.pet.adoption.activities.fragments.pet;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.pet.adoption.entities.PetInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetFragment extends Fragment {

    private RecyclerView recyclerView;
    List<PetInfo> list;
    private static PetFragment instance;
    private String[] arr_countries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pet, container, false);
        list = new ArrayList<>();
        instance = this;
        onLoad(v);
        return v;
    }

    private void onLoad(View v) {
        v.findViewById(R.id.tv_back).setVisibility(View.GONE);
        this.arr_countries = getArrayById(R.array.array_states, "Whole Country");
        CommonListener listener = new CommonListener(v, getClass());
        recyclerView = v.findViewById(R.id.recyclerView);
        getPetInfoListFromDB();
        Spinner sp_type = v.findViewById(R.id.spinner_type);
        Spinner sp_species = v.findViewById(R.id.spinner_species);
        Spinner sp_age = v.findViewById(R.id.spinner_age);
        Spinner sp_size = v.findViewById(R.id.spinner_size);

        sp_type.setAdapter(getAdapter(R.array.array_pet_type, "Type"));
        sp_species.setAdapter(getAdapter(R.array.array_empty, "Less"));
        sp_age.setAdapter(getAdapter(R.array.array_pet_age, "Age"));
        sp_size.setAdapter(getAdapter(R.array.array_pet_size, "Size"));

        sp_type.setOnItemSelectedListener(listener.getSp_type_selected_listener());
        sp_size.setOnItemSelectedListener(listener.getSp_size_selected_listener());
        sp_species.setOnTouchListener(listener.getSp_species_touch_listener());

        ArrayAdapter<String> adapter = getAdapter(R.array.array_states, "Whole Country");
        Spinner countrySpinner = v.findViewById(R.id.spinner_country); // Replace with your Spinner ID
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
        //listener1 = new PetSpinnerListener(v);
        countrySpinner.setOnItemSelectedListener(getSpinnerCountry());
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

    public AdapterView.OnItemSelectedListener getSpinnerCountry(){
        AdapterView.OnItemSelectedListener listener;

        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    displayList(list);
                    return;
                }

                List<PetInfo> filterList = new ArrayList<>();

                for(PetInfo info : list){
                    if (Objects.equals(info.getState(), arr_countries[position]))
                        filterList.add(info);
                }
                displayList(filterList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        return listener;
    }
}