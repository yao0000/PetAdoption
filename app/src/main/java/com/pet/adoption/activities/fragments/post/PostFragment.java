package com.pet.adoption.activities.fragments.post;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pet.adoption.R;
import com.pet.adoption.activities.FragmentActivity;
import com.pet.adoption.activities.fragments.pet.PetFragment;
import com.pet.adoption.entities.PetInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PostFragment extends Fragment {

    private static final int PICK_IMG = 101;
    private Uri imgUri;
    private String filename;
    private PostListener listener;

    private TextView tv_pic_selection;
    private EditText et_name, et_contact, et_desc;
    private Spinner sp_age, sp_type, sp_size, sp_species, sp_status, sp_gender, sp_state;
    private ProgressBar uploadProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        listener = new PostListener(view);
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
        uploadProgressBar = v.findViewById(R.id.uploadProgressBar);
        // To set event handler
        tv_pic_selection.setOnClickListener(e -> tvPicSelection_Click());
        v.findViewById(R.id.btn_submit).setOnClickListener(e -> btnSubmit_Click());

        // To set value change listener
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

        if (name.isEmpty()
                || contact.isEmpty()
                || description.isEmpty()
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
        uploadImage();
    }

    private void uploadImage(){
        File tempFile = null;
        try {
            String userUID = FirebaseAuth.getInstance().getUid();
            filename = userUID + "_" + System.currentTimeMillis() + getFileType(imgUri);

            tempFile = File.createTempFile(filename, getFileType((imgUri)), requireContext().getCacheDir());
            copyUriToFile(imgUri, tempFile);
            Uri uri = Uri.fromFile(tempFile);
            StorageReference ref = FirebaseStorage.getInstance().getReference("images").child(filename);

            UploadTask uploadTask = null;
            uploadTask = ref.putFile(uri);
            uploadProgressBar.setVisibility(View.VISIBLE);

            uploadTask.addOnSuccessListener(task -> {
                uploadInfoToDB();
            }).addOnFailureListener(task -> {

                uploadProgressBar.setVisibility(View.GONE);

                Toast.makeText(requireContext()
                                , task.getMessage()
                                , Toast.LENGTH_SHORT)
                        .show();

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (tempFile != null && tempFile.exists()){
                Boolean deleted = tempFile.delete();
            }
        }

    }

    private void uploadInfoToDB(){
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
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

        PetInfo info = new PetInfo(time, filename, name, age, type, size,
                species, status, contact,
                gender, state, description);

        FirebaseFirestore.getInstance()
                .collection("pets")
                .add(info)
                .addOnCompleteListener(task -> {
                    uploadProgressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()){
                        Toast.makeText(getContext()
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_SHORT).show();

                        FirebaseStorage.getInstance()
                                .getReference("images")
                                .child(filename)
                                .delete();
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

    private void copyUriToFile(Uri uri, File file) throws IOException, FileNotFoundException {
        ContentResolver contentResolver = requireContext().getContentResolver();
        try (InputStream inputStream = contentResolver.openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String getFileName(Uri uri) {
        String fileName = null;
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};

        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            fileName = cursor.getString(nameIndex);
            cursor.close();
        }
        return fileName;
    }

    private String getFileType(Uri uri) {
        String mimeType = null;
        ContentResolver contentResolver = requireContext().getContentResolver();

        // Get the MIME type
        if (Objects.equals(uri.getScheme(), "content")) {
            mimeType = contentResolver.getType(uri);
        }
        else {
            // For file URIs, you can get the file extension and infer the type
            String filePath = uri.getPath();
            if (filePath != null) {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(filePath)).toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            }
        }
        assert mimeType != null;
        return "." + mimeType.substring(mimeType.indexOf("/") + 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
            if (imgUri == null) return;
            tv_pic_selection.setText(getFileName(imgUri));
        }
    }
}