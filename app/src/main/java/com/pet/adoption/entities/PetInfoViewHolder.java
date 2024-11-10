package com.pet.adoption.entities;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pet.adoption.R;

public class PetInfoViewHolder  extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView tvTime, tvDescription, tvTag1, tvTag2, tvTag3, tvTag4;
    LinearLayout ll_item_pet;
    boolean isImageLoad = false;

    public PetInfoViewHolder(View item){
        super(item);
        ll_item_pet = item.findViewById(R.id.ll_item_pet);
        imageView = item.findViewById(R.id.ivPetImage);
        tvTime = item.findViewById(R.id.tvTime);
        tvDescription = item.findViewById(R.id.tvDescription);
        tvTag1 = item.findViewById(R.id.tvTag1);
        tvTag2 = item.findViewById(R.id.tvTag2);
        tvTag3 = item.findViewById(R.id.tvTag3);
        tvTag4 = item.findViewById(R.id.tvTag4);
    }
}