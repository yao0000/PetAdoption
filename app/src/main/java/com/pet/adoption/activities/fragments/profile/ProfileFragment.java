package com.pet.adoption.activities.fragments.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pet.adoption.activities.MainActivity;
import com.pet.adoption.R;
import com.pet.adoption.entities.Account;
import com.pet.adoption.services.FirebaseAuthHelper;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        onLoad(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void onLoad(View v){

        TextView tv_username = v.findViewById(R.id.tv_username);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(getContext()
                                        , Objects.requireNonNull(task.getException()).getMessage()
                                        , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DocumentSnapshot doc = task.getResult();
                    if (!doc.exists()){
                        Toast.makeText(getContext()
                                        , "User name not found"
                                        , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Account acc = doc.toObject(Account.class);
                    assert acc != null;
                    tv_username.setText("Hi, " + acc.getUsername());
                });

        setEventHandler(v);
    }

    private void setEventHandler(View v){
        v.findViewById(R.id.ll_log_out).setOnClickListener(e -> onClickLogOut());
        v.findViewById(R.id.ll_saved).setOnClickListener(e -> onClickBtnSaved());
    }

    private void onClickBtnSaved(){
        startActivity(new Intent(requireContext(), SavedPageActivity.class));
    }

    private void onClickLogOut(){
        new FirebaseAuthHelper().signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);  // Replace with your main activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}