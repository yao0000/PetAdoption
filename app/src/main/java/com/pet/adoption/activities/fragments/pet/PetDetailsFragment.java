package com.pet.adoption.activities.fragments.pet;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pet.adoption.R;
import com.pet.adoption.activities.FragmentActivity;

public class PetDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pet_details, container, false);
        onLoad(v);
        return v;
    }

    private void onLoad(View v){
        v.findViewById(R.id.btnBack).setOnClickListener(e -> FragmentActivity.getInstance().popFragment());
    }
}