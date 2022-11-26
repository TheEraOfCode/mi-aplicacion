package com.c.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdaptadorImagenes extends RecyclerView.Adapter<AdaptadorImagenes.ViewholderImagenes>{

    ArrayList<Imagenes> imaginadas;
    Context ct;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
        void OnItemClickDelete(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }


    public AdaptadorImagenes(Context ct, ArrayList<Imagenes> imaginadas){
        this.imaginadas = imaginadas;
        this.ct= ct;
    }

    @NonNull
    @Override
    public ViewholderImagenes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(ct).inflate(R.layout.images,null,false
        );

        return new ViewholderImagenes(view, mListener);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewholderImagenes holder, int position) {

        holder.description.setText(imaginadas.get(position).getDescription());
        holder.date.setText(imaginadas.get(position).getFecha());

        Glide.with(ct)
                .load(imaginadas.get(position).getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.laImagen);
    }

    @Override
    public int getItemCount() {
        return imaginadas.size();
    }


    public class ViewholderImagenes extends RecyclerView.ViewHolder{
        TextView description,date;
        ImageView laImagen,delete;

        public ViewholderImagenes(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);
            description=(TextView)itemView.findViewById(R.id.descriptionImage);
            date=(TextView)itemView.findViewById(R.id.fechado);
            laImagen= (ImageView) itemView.findViewById(R.id.imagenDeImage);
            delete= (ImageView) itemView.findViewById(R.id.imageDelete);

            laImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){

                        int position= getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);

                        }


                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){

                        int position= getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.OnItemClickDelete(position);

                        }


                    }
                }
            });

        }


    }


}
