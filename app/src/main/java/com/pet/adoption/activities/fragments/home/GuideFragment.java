package com.pet.adoption.activities.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pet.adoption.activities.FragmentActivity;
import com.pet.adoption.R;

public class GuideFragment extends Fragment {

    private View view;
    public enum Type {Dog, Cat}
    private final Type selectedType;

    public GuideFragment(Type type) {
        this.selectedType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int id = R.layout.fragment_guide_cat;
        if (selectedType == Type.Dog){
            id = R.layout.fragment_guide_dog;
        }
        view = inflater.inflate(id, container, false);
        onLoad();
        return view;
    }

    private void onLoad(){
        setEventHandler();
    }

    private void setEventHandler(){
        view.findViewById(R.id.btnBack).setOnClickListener(e ->
                FragmentActivity.getInstance().commitFragment(new HomeFragment()));
    }

}