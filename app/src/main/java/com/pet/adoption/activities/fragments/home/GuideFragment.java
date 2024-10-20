package com.pet.adoption.activities.fragments.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pet.adoption.activities.FragmentActivity;
import com.pet.adoption.R;

public class GuideFragment extends Fragment {

    public enum Type {Dog, Cat}
    private final Type selectedType;

    public GuideFragment(Type type) {
        this.selectedType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        if (selectedType == Type.Dog){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
            ((ImageView)view.findViewById(R.id.iv_pet_image)).setImageBitmap(bitmap);
            ((TextView)view.findViewById(R.id.tv_guide_desc)).setText(getString(R.string.dog_intro));
        }
        onLoad(view);
        return view;
    }

    private void onLoad(View v){
        v.findViewById(R.id.btnBack).setOnClickListener(e ->
                FragmentActivity.getInstance().commitFragment(new HomeFragment()));
    }

}