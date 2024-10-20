package com.pet.adoption.Activity.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.pet.adoption.R;

import java.util.ArrayList;

public class PetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pet, container, false);

        onLoad(v);

        ((GradientDrawable)v.findViewById(R.id.etSearch).getBackground()).setColor(getResources().getColor(R.color.white));
        return v;
    }


    private void onLoad(View v) {
        ArrayList<String> countriesList = new ArrayList<>();
        countriesList.add("Whole Country");

        Spinner countrySpinner = v.findViewById(R.id.spinner_country); // Replace with your Spinner ID
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, countriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = null;
    }
}