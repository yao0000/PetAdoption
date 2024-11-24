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
import com.pet.adoption.MainActivity;
import com.pet.adoption.R;
import com.pet.adoption.entities.Account;
import com.pet.adoption.services.firebase.FirebaseAuthHelper;
import com.pet.adoption.services.firebase.FirestoreHelper;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private View view;
    private TextView tv_username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        onLoad();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void onLoad(){
        tv_username = view.findViewById(R.id.tv_username);

        loadUsername();
        setEventHandler();
    }

    private void setEventHandler(){
        view.findViewById(R.id.ll_log_out).setOnClickListener(e -> onClickLogOut());
        view.findViewById(R.id.ll_saved).setOnClickListener(e -> onClickBtnSaved());
    }

    private void loadUsername(){
        FirestoreHelper.loadAccount(FirebaseAuth.getInstance().getUid())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(getContext()
                                , Objects.requireNonNull(task.getException()).getMessage()
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Account acc = task.getResult();
                    tv_username.setText("Hi, " + acc.getUsername());
                });
    }

    private void onClickBtnSaved(){
        startActivity(new Intent(requireContext(), SavedPageActivity.class));
    }

    private void onClickLogOut(){
        FirebaseAuthHelper.signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}