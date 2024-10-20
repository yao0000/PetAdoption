package com.pet.adoption.Activity.fragments.home;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pet.adoption.Activity.FragmentActivity;
import com.pet.adoption.R;

public class CatGuideFragment extends Fragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cat_guide, container, false);
        onLoad(view);
        return view;
    }

    private void onLoad(View v){
        v.findViewById(R.id.btnBack).setOnClickListener(e ->
                FragmentActivity.getInstance().commitFragment(new HomeFragment()));
    }

}