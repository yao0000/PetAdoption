package com.pet.adoption.Activity.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pet.adoption.Activity.FragmentActivity;
import com.pet.adoption.R;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        onLoad(v);
        return v;
    }

    private void onLoad(View v){
        v.findViewById(R.id.ll_cat).setOnClickListener(e ->
                setFragment(new CatGuideFragment()));
    }

    private void setFragment(Fragment fragment){
        FragmentActivity.getInstance().commitFragment(fragment);
    }

}