package com.c.myapplication;



import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter <Adaptador.Viewholderpersonajes>  {

    ArrayList<Cliente> ListaClientes;
    Context ct;
    private OnItemClickListener adapListener;

    public interface OnItemClickListener{
        Void OnItemClick(int position);
        void OnitemClickForDelete(int position);
    }

    public void  setOnItemClickListener(OnItemClickListener listener){
        adapListener=listener;
    }



    public Adaptador(Context ct, ArrayList<Cliente> ListaClientes) {
        this.ListaClientes = ListaClientes;
        this.ct= ct;
    }

    @NonNull
    @Override
    public Adaptador.Viewholderpersonajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(ct).inflate(R.layout.clientes,null,false
        );

        return new Viewholderpersonajes(view,adapListener) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.Viewholderpersonajes holder, int position) {

        holder.etiNombre.setText(ListaClientes.get(position).getNombre());
        holder.etiInformacion.setText(ListaClientes.get(position).getId());
        holder.etinOcupacion.setText(ListaClientes.get(position).getOcupacion());
        holder.etinfecha.setText(ListaClientes.get(position).getFecha());
        holder.etinTipo.setText(ListaClientes.get(position).getTipo());

        Glide.with(ct)
                .load(ListaClientes.get(position).getFoto())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.Foto);




    }

    @Override
    public int getItemCount() {
        return ListaClientes.size();
    }





    public class Viewholderpersonajes extends RecyclerView.ViewHolder {

        TextView etiNombre, etiInformacion, etinOcupacion, etinfecha,etinTipo;
        ImageView Foto,Delete   ;
        LinearLayout layClientes;

        public Viewholderpersonajes(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            etiNombre= (TextView) itemView.findViewById(R.id.nombre);
            etiInformacion= (TextView) itemView.findViewById(R.id.documento);
            etinOcupacion= (TextView) itemView.findViewById(R.id.laOcupacion);
            etinfecha= (TextView) itemView.findViewById(R.id.fechaIngreso);
            Foto= (ImageView) itemView.findViewById(R.id.imageViewc);
            Delete= (ImageView) itemView.findViewById(R.id.imageDelete);
            layClientes = (LinearLayout) itemView.findViewById(R.id.layoutClientes);
            etinTipo= (TextView) itemView.findViewById(R.id.tipo);


            itemView.setOnClickListener(new View.OnClickListener() {
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

            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null){

                        int position= getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.OnitemClickForDelete(position);

                        }


                    }

                }
            });



        }
    }
}