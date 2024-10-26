package com.pet.adoption.activities.fragments.post;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.ArrayRes;

import com.pet.adoption.R;

import java.util.List;

public class PostListener {

    private Context context;
    private Spinner sp_type, sp_size, sp_species;
    private String[] arr_empty = getArrayString(R.array.array_empty);

    public PostListener(View view){
        sp_type = view.findViewById(R.id.spinner_type);
        sp_size = view.findViewById(R.id.spinner_size);
        sp_species = view.findViewById(R.id.spinner_species);
    }

    public AdapterView.OnItemSelectedListener getSp_type_selected_listener() {

        AdapterView.OnItemSelectedListener sp_type_selected_listener;

        sp_type_selected_listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    setSpinnerEntries(sp_species, arr_empty);
                    return;
                }

                int index = sp_size.getSelectedItemPosition();
                if (position == 1){ // Cat
                    String[][] value = {
                            getArrayString(R.array.array_empty),
                            getArrayString(R.array.array_species_breed_cat_small),
                            getArrayString(R.array.array_species_breed_cat_medium),
                            getArrayString(R.array.array_species_breed_cat_large)
                    };

                    setSpinnerEntries(sp_species, value[index]);
                    /*
                    if (index == 0){ // none
                        setSpinnerEntries(sp_species, R.array.array_empty);
                    }
                    else if (index == 1){ // small
                        setSpinnerEntries(sp_species, R.array.array_species_breed_cat_small);
                    }
                    else if (index  == 2){ // medium
                        setSpinnerEntries(sp_species, R.array.array_species_breed_cat_medium);
                    }
                    else if (index == 3){
                        setSpinnerEntries(sp_species, R.array.array_species_breed_cat_large);
                    }*/
                }
                else if (position == 2){ // dog

                    String[][] value = {
                            getArrayString(R.array.array_empty),
                            getArrayString(R.array.array_species_breed_dog_small),
                            getArrayString(R.array.array_species_breed_dog_medium),
                            getArrayString(R.array.array_species_breed_dog_large)
                    };

                    setSpinnerEntries(sp_species, value[index]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        return sp_type_selected_listener;
    }

    public AdapterView.OnItemSelectedListener getSp_size_selected_listener(){
        AdapterView.OnItemSelectedListener sp_size_selected_listener;

        sp_size_selected_listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    setSpinnerEntries(sp_species, arr_empty);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        return sp_size_selected_listener;
    }

    private String[] getArrayString(@ArrayRes int id){
        assert context != null;
        return context.getResources().getStringArray(id);
    }

    private void setSpinnerEntries(Spinner spinner, String[] values){
        //String[] values = context.getResources().getStringArray(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
