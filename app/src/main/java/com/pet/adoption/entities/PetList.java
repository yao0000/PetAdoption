package com.pet.adoption.entities;

import static com.pet.adoption.services.common.Function.getArray;

import android.content.Context;

import com.pet.adoption.R;

import java.util.ArrayList;
import java.util.List;

public class PetList {

    private Context context;
    private String[] arrType, arrAge, arrSize, arrState, arrGender, arrStatus;
    private List<PetInfo> list;

    public PetList(Context context, List<PetInfo> list){
        this.context = context;
        this.arrType = getArray(context, R.array.array_pet_type);
        this.arrAge = getArray(context, R.array.array_pet_age);
        this.arrSize = getArray(context, R.array.array_pet_size);
        this.arrState = getArray(context, R.array.array_states);
        this.arrGender = getArray(context, R.array.array_gender);
        this.arrStatus = getArray(context, R.array.array_neuter_status);
        this.list =list;
    }

    public List<PetInfo> filter( int typeIndex, int ageIndex,
                                int sizeIndex, int stateIndex, int speciesIndex
                                , int genderIndex, int statusIndex){
        List<PetInfo> filteredList = new ArrayList<>(list);

        // Determine the species array based on typeIndex and sizeIndex
        String[] arrSpecies = null;
        if (speciesIndex != 0) {
            int resId = 0;
            if (typeIndex == 1) { // Cat
                switch (sizeIndex) {
                    case 1:
                        resId = R.array.array_species_breed_cat_small;
                        break;
                    case 2:
                        resId = R.array.array_species_breed_cat_medium;
                        break;
                    default:
                        resId = R.array.array_species_breed_cat_large;
                        break;
                }
            } else { // typeIndex == 2 Dog
                switch (sizeIndex) {
                    case 1:
                        resId = R.array.array_species_breed_dog_small;
                        break;
                    case 2:
                        resId = R.array.array_species_breed_dog_medium;
                        break;
                    default:
                        resId = R.array.array_species_breed_dog_large;
                        break;
                }
            }
            arrSpecies = getArray(context, resId);
        }

        // Loop through filteredList in reverse order to safely remove items
        for (int i = filteredList.size() - 1; i >= 0; i--) {
            PetInfo item = filteredList.get(i);
            boolean shouldRemove = false;

            // Apply filters one by one
            if (typeIndex != 0 && !item.getType().equals(arrType[typeIndex])) {
                shouldRemove = true;
            }
            if (ageIndex != 0 && !item.getAge().equals(arrAge[ageIndex])) {
                shouldRemove = true;
            }
            if (sizeIndex != 0 && !item.getSize().equals(arrSize[sizeIndex])) {
                shouldRemove = true;
            }
            if (stateIndex != 0 && !item.getState().equals(arrState[stateIndex])) {
                shouldRemove = true;
            }
            if (genderIndex != 0 && !item.getGender().equals(arrGender[genderIndex])) { // Assuming gender is stored in state
                shouldRemove = true;
            }
            if (statusIndex != 0 && !item.getStatus().equals(arrStatus[statusIndex])) { // Assuming status check
                shouldRemove = true;
            }
            if (speciesIndex != 0 && !item.getSpecies().equals(arrSpecies[speciesIndex])) {
                shouldRemove = true;
            }

            // If any filter condition matched, remove the item
            if (shouldRemove) {
                filteredList.remove(i);
            }
        }
        return filteredList;
    }
}
