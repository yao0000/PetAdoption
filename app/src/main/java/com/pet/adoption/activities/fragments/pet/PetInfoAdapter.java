package com.pet.adoption.activities.fragments.pet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pet.adoption.R;
import com.pet.adoption.entities.PetInfo;
import com.pet.adoption.entities.PetInfoViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PetInfoAdapter extends RecyclerView.Adapter<PetInfoViewHolder>{

    private final Context context;
    private final List<PetInfo> list;

    public PetInfoAdapter(Context context, List<PetInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PetInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_pet_info, parent, false);
        return new PetInfoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PetInfoViewHolder holder, int position) {
        PetInfo info = list.get(position);
        holder.getLl_item_pet().setOnClickListener(e -> {
            if (!holder.isImageLoad()){
                Toast.makeText(context, "Item is loading, please wait", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(context, PetDetailsActivity.class);
            intent.putExtra("data", info);
            context.startActivity(intent);
        });
        holder.getTvTime().setText(info.getPostingTime());
        holder.getTvDescription().setText(info.getDescription());
        holder.getTvTag1().setText("#" + info.getSpecies());
        holder.getTvTag2().setText("#" + info.getStatus());
        holder.getTvTag3().setText("#" + info.getGender());
        holder.getTvTag4().setText("#" + info.getSize());


        StorageReference ref = FirebaseStorage.getInstance().getReference("images").child(info.getFileName());
        File file = null;
        try {
            file = File.createTempFile(info.getFileName(), "");
            File finalFile = file;
            ref.getFile(file)
                    .addOnCompleteListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                        holder.getImageView().setImageBitmap(bitmap);
                        holder.setImageLoad(true);
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
        finally {
            if (file != null && file.exists()){
                Boolean deleted = file.delete();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
