package com.pet.adoption.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pet.adoption.Activity.fragments.HomeFragment;
import com.pet.adoption.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PetInfoAdapter extends RecyclerView.Adapter<PetInfoAdapter.PetInfoViewHolder>{

    private Context context;
    private List<PetInfo> list;

    public PetInfoAdapter(Context context, List<PetInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PetInfoAdapter.PetInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_pet_info, parent, false);
        return new PetInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetInfoAdapter.PetInfoViewHolder holder, int position) {
        PetInfo info = list.get(position);

        holder.tvTime.setText(info.getTime());
        holder.tvDescription.setText(info.getDescription());
        holder.tvTag1.setText(info.getTagByIndex(0));
        holder.tvTag2.setText(info.getTagByIndex(1));
        holder.tvTag3.setText(info.getTagByIndex(2));

        StorageReference ref = FirebaseStorage.getInstance().getReference(info.getFileName());
        try {
            File local = File.createTempFile("temp", ".jpg");
            ref.getFile(local)
                    .addOnCompleteListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                        ((ImageView)holder.imageView).setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(task -> {
                        Toast.makeText(context
                                , task.getMessage()
                                , Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            Toast.makeText(context
                    , e.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PetInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTime, tvDescription, tvTag1, tvTag2, tvTag3;

        public PetInfoViewHolder(View item){
            super(item);
            imageView = item.findViewById(R.id.ivPetImage);
            tvTime = item.findViewById(R.id.tvTime);
            tvDescription = item.findViewById(R.id.tvDescription);
            tvTag1 = item.findViewById(R.id.tvTag1);
            tvTag2 = item.findViewById(R.id.tvTag2);
            tvTag3 = item.findViewById(R.id.tvTag3);
        }
    }
}
