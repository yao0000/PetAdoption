package com.pet.adoption.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pet.adoption.activities.fragments.home.HomeFragment;
import com.pet.adoption.activities.fragments.pet.PetFragment;
import com.pet.adoption.activities.fragments.post.PostFragment;
import com.pet.adoption.activities.fragments.profile.ProfileFragment;
import com.pet.adoption.R;

public class FragmentActivity extends AppCompatActivity {
    private static FragmentActivity instance;
    private final BottomNavigationView.OnNavigationItemSelectedListener listener =
            item -> {
                Fragment fragment;
                int id = item.getItemId();

                if (id == R.id.nav_home) fragment = new HomeFragment();
                else if (id == R.id.nav_pet) fragment = new PetFragment();
                else if (id == R.id.nav_post) fragment = new PostFragment();
                else fragment = new ProfileFragment();

                commitFragment(fragment);

                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment);
        instance = this;

        ((BottomNavigationView)findViewById(R.id.bottomNavigationView))
                .setOnNavigationItemSelectedListener(listener);

        commitFragment(new HomeFragment());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    public static FragmentActivity getInstance(){
        return instance;
    }

    public void commitFragment(@NonNull Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

}