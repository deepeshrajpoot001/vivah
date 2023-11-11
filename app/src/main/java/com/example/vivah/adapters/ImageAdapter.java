package com.example.vivah.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.vivah.R;
import com.example.vivah.databinding.CostomSingleImageBinding;


import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final ArrayList<String> uriArrayList;
    private Context context;

    public ImageAdapter( ArrayList<String> uriArrayList, Context context) {
        this.uriArrayList = uriArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CostomSingleImageBinding costomSingleImageBinding = CostomSingleImageBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return  new ViewHolder(costomSingleImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Glide.with(holder.binding.image).load(String.valueOf(uriArrayList.get(position))).into(holder.binding.image);


        holder.binding.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Image")
                        .setMessage("Are you sure want to delete")
                        .setIcon(R.drawable.ic_round_delete_24)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                uriArrayList.remove(holder.getAbsoluteAdapterPosition());
                                notifyItemRemoved(holder.getAbsoluteAdapterPosition());

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();


                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CostomSingleImageBinding binding;
        public ViewHolder(CostomSingleImageBinding costomSingleImageBinding) {
            super(costomSingleImageBinding.getRoot());
            binding = costomSingleImageBinding;
        }


    }
}
