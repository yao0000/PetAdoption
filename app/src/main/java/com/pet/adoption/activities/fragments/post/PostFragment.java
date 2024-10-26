package com.pet.adoption.activities.fragments.post;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pet.adoption.R;

public class PostFragment extends Fragment {

    private static final int PICK_IMG = 101;

    private TextView tv_pic_selection;
    private EditText et_name, et_contact, et_desc;
    private Spinner sp_age, sp_type, sp_size, sp_species, sp_status, sp_gender, sp_state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        onLoad(view);
        return view;
    }

    private void onLoad(View v){
        et_name = v.findViewById(R.id.et_name);
        sp_age = v.findViewById(R.id.spinner_age);
        tv_pic_selection = v.findViewById(R.id.tv_upload);
        sp_type = v.findViewById(R.id.spinner_type);
        sp_size = v.findViewById(R.id.spinner_size);
        sp_species = v.findViewById(R.id.spinner_species);
        sp_status = v.findViewById(R.id.spinner_neuter_status);
        et_contact = v.findViewById(R.id.et_contact_no);
        sp_gender = v.findViewById(R.id.spinner_gender);
        sp_state = v.findViewById(R.id.spinner_state);
        et_desc = v.findViewById(R.id.et_description);

        // To set event handler
        tv_pic_selection.setOnClickListener(e -> tvPicSelection_Click());

        // To set value change listener
        sp_type.setOnItemSelectedListener(null);
        sp_size.setOnItemSelectedListener(null);
    }

    private void setSpinnerEntries(Spinner spinner, @ArrayRes int id){
        String[] values = getResources().getStringArray(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void tvPicSelection_Click(){
        // to open the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMG);
    }

    private void btnSubmit_Click(){
        String name = et_name.getText().toString().trim();
        String age = sp_age.getSelectedItem().toString();
        String type = sp_type.getSelectedItem().toString();
        String size = sp_size.getSelectedItem().toString();
        String species = sp_species.getSelectedItem().toString();
        String status = sp_status.getSelectedItem().toString();
        String contact = et_contact.getText().toString().trim();
        String gender = sp_gender.getSelectedItem().toString();
        String state = sp_state.getSelectedItem().toString();
        String description = et_desc.getText().toString().trim();

        if (name.isEmpty() || contact.isEmpty() || description.isEmpty()) {
            return;
        }

        if (sp_age.getSelectedItemPosition() == 0
                || sp_type.getSelectedItemPosition() == 0
                || sp_size.getSelectedItemPosition() == 0
                || sp_species.getSelectedItemPosition() == 0
                || sp_status.getSelectedItemPosition() == 0
                || sp_gender.getSelectedItemPosition() == 0
                || sp_state.getSelectedItemPosition() == 0){
            return;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG && resultCode == RESULT_OK && data != null) {
            Uri imgUri = data.getData();
            if (imgUri == null) return;
            tv_pic_selection.setText(imgUri.getPath());
        }
    }
}