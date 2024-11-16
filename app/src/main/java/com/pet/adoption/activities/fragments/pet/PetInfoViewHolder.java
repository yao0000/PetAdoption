package com.pet.adoption.activities.fragments.pet;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pet.adoption.R;

public class PetInfoViewHolder  extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView tvTime, tvDescription, tvTag1, tvTag2, tvTag3, tvTag4;
    private LinearLayout ll_item_pet;

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

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTvTime() {
        return tvTime;
    }

    public TextView getTvDescription() {
        return tvDescription;
    }

    public TextView getTvTag1() {
        return tvTag1;
    }

    public TextView getTvTag2() {
        return tvTag2;
    }

    public TextView getTvTag3() {
        return tvTag3;
    }

    public TextView getTvTag4() {
        return tvTag4;
    }

    public LinearLayout getLl_item_pet() {
        return ll_item_pet;
    }

}