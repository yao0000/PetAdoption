package com.pet.adoption.services.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ArrayRes;

import com.pet.adoption.R;
import com.pet.adoption.activities.fragments.pet.PetFragment;
import com.pet.adoption.activities.fragments.post.PostFragment;

public class Listener {

    private final Context context;
    private final Spinner sp_type;
    private final Spinner sp_size;
    private final Spinner sp_species;
    private final String[] arr_empty;
    private Class<?> cls;

    public Listener(View view, Class<?> cls) {
        this.cls = cls;
        context = view.getContext();

        if (cls == PostFragment.class)
            arr_empty = getArrayString(R.array.array_empty);
        else
            arr_empty = new String[]{"Species"};

        sp_type = view.findViewById(R.id.spinner_type);
        sp_size = view.findViewById(R.id.spinner_size);
        sp_species = view.findViewById(R.id.spinner_species);
    }

    public View.OnTouchListener getSp_species_touch_listener(){
        View.OnTouchListener sp_species_touch_listener;
        sp_species_touch_listener = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sp_species.getAdapter().getCount() != 1){
                    return false;
                }

                if (sp_type.getSelectedItemPosition() == 0){
                    Toast.makeText(context,
                            "Please select value for Pet Type",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (sp_size.getSelectedItemPosition() == 0){
                    Toast.makeText(context,
                            "Please select value for Size of Pet",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        };

        return sp_species_touch_listener;
    }

    public AdapterView.OnItemSelectedListener getSp_type_selected_listener() {

        AdapterView.OnItemSelectedListener sp_type_selected_listener;

        sp_type_selected_listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    setSpinnerEntries(arr_empty);
                    return;
                }

                int index = sp_size.getSelectedItemPosition();
                if (position == 1){ // Cat
                    String[][] value = {
                            arr_empty,
                            getArrayString(R.array.array_species_breed_cat_small),
                            getArrayString(R.array.array_species_breed_cat_medium),
                            getArrayString(R.array.array_species_breed_cat_large)
                    };

                    setSpinnerEntries(value[index]);
                }
                else if (position == 2){ // dog

                    String[][] value = {
                            arr_empty,
                            getArrayString(R.array.array_species_breed_dog_small),
                            getArrayString(R.array.array_species_breed_dog_medium),
                            getArrayString(R.array.array_species_breed_dog_large)
                    };

                    setSpinnerEntries(value[index]);
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
                    setSpinnerEntries(arr_empty);
                    return;
                }

                int index = sp_type.getSelectedItemPosition();
                if (position == 1){ // small
                    String[][] value = {
                            arr_empty,
                            getArrayString(R.array.array_species_breed_cat_small),
                            getArrayString(R.array.array_species_breed_dog_small)
                    };
                    setSpinnerEntries(value[index]);
                }
                else if (position == 2){ //medium
                    String[][] value = {
                            arr_empty,
                            getArrayString(R.array.array_species_breed_cat_medium),
                            getArrayString(R.array.array_species_breed_dog_medium)
                    };
                    setSpinnerEntries(value[index]);
                }
                else if (position == 3){ //large
                    String[][] value = {
                            arr_empty,
                            getArrayString(R.array.array_species_breed_cat_large),
                            getArrayString(R.array.array_species_breed_dog_large)
                    };
                    setSpinnerEntries(value[index]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        return sp_size_selected_listener;
    }

    public void valueChangeSPSize(){
        if (sp_size.getSelectedItemPosition() == 0){
            setSpinnerEntries(arr_empty);
            return;
        }

        int index = sp_type.getSelectedItemPosition();
        if (sp_size.getSelectedItemPosition() == 1){ // small
            String[][] value = {
                    arr_empty,
                    getArrayString(R.array.array_species_breed_cat_small),
                    getArrayString(R.array.array_species_breed_dog_small)
            };
            setSpinnerEntries(value[index]);
        }
        else if (sp_size.getSelectedItemPosition() == 2){ //medium
            String[][] value = {
                    arr_empty,
                    getArrayString(R.array.array_species_breed_cat_medium),
                    getArrayString(R.array.array_species_breed_dog_medium)
            };
            setSpinnerEntries(value[index]);
        }
        else if (sp_size.getSelectedItemPosition() == 3){ //large
            String[][] value = {
                    arr_empty,
                    getArrayString(R.array.array_species_breed_cat_large),
                    getArrayString(R.array.array_species_breed_dog_large)
            };
            setSpinnerEntries(value[index]);
        }
    }

    public void valueChangeSPType(){
        if (sp_type.getSelectedItemPosition() == 0){
            setSpinnerEntries(arr_empty);
            return;
        }

        int index = sp_size.getSelectedItemPosition();
        if (sp_type.getSelectedItemPosition() == 1){ // Cat
            String[][] value = {
                    arr_empty,
                    getArrayString(R.array.array_species_breed_cat_small),
                    getArrayString(R.array.array_species_breed_cat_medium),
                    getArrayString(R.array.array_species_breed_cat_large)
            };

            setSpinnerEntries(value[index]);
        }
        else if (sp_type.getSelectedItemPosition() == 2){ // dog

            String[][] value = {
                    arr_empty,
                    getArrayString(R.array.array_species_breed_dog_small),
                    getArrayString(R.array.array_species_breed_dog_medium),
                    getArrayString(R.array.array_species_breed_dog_large)
            };

            setSpinnerEntries(value[index]);
        }
    }

    private String[] getArrayString(@ArrayRes int id){
        assert context != null;
        String[] values = context.getResources().getStringArray(id);
        if (cls == PetFragment.class)
            values[0] = "Species";
        return values;
    }

    private void setSpinnerEntries(String[] values){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_species.setAdapter(adapter);
    }

}