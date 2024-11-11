package com.pet.adoption.services;

import android.content.Context;

import com.pet.adoption.R;
import com.pet.adoption.entities.PetInfo;

import java.util.ArrayList;
import java.util.List;

public class PetSearchFilter {

    private Context context;
    private String[] arrType, arrAge, arrSize, arrState, arrGender;
    private String[] arrSpecies;

    public PetSearchFilter(Context context){
        this.context = context;
        this.arrType = CommonListener.getArray(context, R.array.array_pet_type);
        this.arrAge = CommonListener.getArray(context, R.array.array_pet_age);
        this.arrSize = CommonListener.getArray(context, R.array.array_pet_size);
        this.arrState = CommonListener.getArray(context, R.array.array_states);
        this.arrGender = CommonListener.getArray(context, R.array.array_gender);
    }

    public List<PetInfo> filter(List<PetInfo> list, int typeIndex, int ageIndex,
                                int sizeIndex, int stateIndex, int speciesIndex
                                ,int genderIndex){
        List<PetInfo> filteredList = new ArrayList<>(list);

        if (typeIndex != 0){
            for(int i = filteredList.size() - 1; i >= 0; i--){
                if (!filteredList.get(i).getType().equals(arrType[typeIndex])){
                    filteredList.remove(i);
                }
            }
        }

        if (ageIndex != 0){
            for (int i = filteredList.size() - 1; i >= 0; i--){
                if(!filteredList.get(i).getAge().equals(arrAge[ageIndex])){
                    filteredList.remove(i);
                }
            }
        }

        if (sizeIndex != 0){
            for (int i = filteredList.size() - 1; i >= 0; i--){
                if (!filteredList.get(i).getSize().equals(arrSize[sizeIndex])){
                    filteredList.remove(i);
                }
            }
        }

        if (stateIndex != 0){
            for (int i = filteredList.size()-1; i >= 0; i--){
                if(!filteredList.get(i).getState().equals(arrState[stateIndex])){
                    filteredList.remove(i);
                }
            }
        }

        if (genderIndex != 0){
            for (int i = filteredList.size()-1; i >= 0; i--){
                if(!filteredList.get(i).getState().equals(arrGender[genderIndex])){
                    filteredList.remove(i);
                }
            }
        }

        if (speciesIndex != 0){
            int resId;
            if (typeIndex == 1){ // Cat
                switch(sizeIndex){
                    case 1:{
                        resId = R.array.array_species_breed_cat_small;
                        break;
                    }
                    case 2:{
                        resId =  R.array.array_species_breed_cat_medium;
                        break;
                    }
                    default:{
                        resId = R.array.array_species_breed_cat_large;
                    }
                }
            }
            else{ // type Index == 2 Dog
                switch(sizeIndex){
                    case 1:{
                        resId = R.array.array_species_breed_dog_small;
                        break;
                    }
                    case 2:{
                        resId = R.array.array_species_breed_dog_medium;
                        break;
                    }
                    default:{
                        resId = R.array.array_species_breed_dog_large;
                    }
                }
            }
            arrSpecies = CommonListener.getArray(context, resId);

            for(int i = filteredList.size() - 1 ; i >= 0; i--){
                if(!filteredList.get(i).getSpecies().equals(arrSpecies[speciesIndex])){
                    filteredList.remove(i);
                }
            }
        }
        return filteredList;
    }
}
