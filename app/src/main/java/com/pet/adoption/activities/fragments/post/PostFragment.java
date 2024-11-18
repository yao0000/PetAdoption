package com.pet.adoption.activities.fragments.post;

import static android.app.Activity.RESULT_OK;

import static com.pet.adoption.services.common.Function.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pet.adoption.R;
import com.pet.adoption.activities.FragmentActivity;
import com.pet.adoption.activities.fragments.pet.PetFragment;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.services.common.Listener;
import com.pet.adoption.services.firebase.FirebaseAuthHelper;
import com.pet.adoption.services.firebase.FirebaseStorageHelper;
import com.pet.adoption.services.firebase.FirestoreHelper;

import java.util.Objects;

public class PostFragment extends Fragment {

    private View view;

    private static final int PICK_IMG = 101;
    private Uri imgUri;
    private String filename;
    private Listener listener;

    private TextView tv_pic_selection;
    private EditText et_name, et_contact, et_desc;
    private Spinner sp_age, sp_type, sp_size, sp_species, sp_status, sp_gender, sp_state;
    private ProgressBar uploadProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post, container, false);
        listener = new Listener(view, getClass());
        onLoad();
        return view;
    }

    private void onLoad(){
        et_name = view.findViewById(R.id.et_name);
        sp_age = view.findViewById(R.id.spinner_age);
        tv_pic_selection = view.findViewById(R.id.tv_upload);
        sp_type = view.findViewById(R.id.spinner_type);
        sp_size = view.findViewById(R.id.spinner_size);
        sp_species = view.findViewById(R.id.spinner_species);
        sp_status = view.findViewById(R.id.spinner_neuter_status);
        et_contact = view.findViewById(R.id.et_contact_no);
        sp_gender = view.findViewById(R.id.spinner_gender);
        sp_state = view.findViewById(R.id.spinner_state);
        et_desc = view.findViewById(R.id.et_description);
        uploadProgressBar = view.findViewById(R.id.uploadProgressBar);

        setEventHandler();
    }

    private void setEventHandler(){
        tv_pic_selection.setOnClickListener(e -> tvPicSelection_Click());
        view.findViewById(R.id.btn_submit).setOnClickListener(e -> btnSubmit_Click());

        sp_type.setOnItemSelectedListener(listener.getSp_type_selected_listener());
        sp_size.setOnItemSelectedListener(listener.getSp_size_selected_listener());
        sp_species.setOnTouchListener(listener.getSp_species_touch_listener());
    }

    private void reset(){
        et_name.setText("");
        sp_age.setSelection(0);
        sp_type.setSelection(0);
        sp_size.setSelection(0);
        sp_status.setSelection(0);
        et_contact.setText("");
        sp_gender.setSelection(0);
        sp_state.setSelection(0);
        tv_pic_selection.setText(getString(R.string.please_select));
    }

    private void tvPicSelection_Click(){
        // to open the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMG);
    }

    private void btnSubmit_Click(){
        String name = et_name.getText().toString().trim();
        String contact = et_contact.getText().toString().trim();
        String description = et_desc.getText().toString().trim();

        if (name.isEmpty() || contact.isEmpty() || description.isEmpty()
                || sp_age.getSelectedItemPosition() == 0
                || sp_type.getSelectedItemPosition() == 0
                || sp_size.getSelectedItemPosition() == 0
                || sp_species.getSelectedItemPosition() == 0
                || sp_status.getSelectedItemPosition() == 0
                || sp_gender.getSelectedItemPosition() == 0
                || sp_state.getSelectedItemPosition() == 0){

            Toast.makeText(requireContext()
                            , "All fields are required"
                            ,Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        upload();
        postPetInfo();
    }

    private void upload() {
        filename = FirebaseAuthHelper.getUID() + "_" + System.currentTimeMillis()
                + getFileTypeFromUri(getContext(), imgUri);
        uploadProgressBar.setVisibility(View.VISIBLE);
        FirebaseStorageHelper.uploadImage(filename, imgUri)
                .addOnCompleteListener(task -> {
                    uploadProgressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void postPetInfo(){
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

        PetInfo info = new PetInfo(filename, name, age, type, size,
                species, status, contact,
                gender, state, description,
                FirebaseAuth.getInstance().getUid());

        info.post()
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()){
                        Toast.makeText(getContext()
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_SHORT).show();

                        FirebaseStorageHelper.deleteImage(filename);
                        return;
                    }

                    new AlertDialog.Builder(requireContext())
                            .setTitle("Info")
                            .setMessage("Submit successfully.\nTo submit a new post?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                reset();
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                FragmentActivity.getInstance()
                                        .commitFragment(new PetFragment());
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
            if (imgUri == null) return;
            tv_pic_selection.setText(getFilenameFromUri(getContext(), imgUri));
        }
    }

}