package com.pet.adoption.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pet.adoption.Activity.fragments.HomeFragment;
import com.pet.adoption.Activity.fragments.PetFragment;
import com.pet.adoption.Activity.fragments.PostFragment;
import com.pet.adoption.Activity.fragments.ProfileFragment;
import com.pet.adoption.R;

public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment);

        ((BottomNavigationView)findViewById(R.id.bottomNavigationView))
                .setOnNavigationItemSelectedListener(listener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new HomeFragment())
                .commit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            item -> {
                Fragment fragment;
                int id = item.getItemId();

                if (id == R.id.nav_home) fragment = new HomeFragment();
                else if (id == R.id.nav_pet) fragment = new PetFragment();
                else if (id == R.id.nav_post) fragment = new PostFragment();
                else fragment = new ProfileFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();

                return true;
            };
}