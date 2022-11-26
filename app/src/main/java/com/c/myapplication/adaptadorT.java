package com.c.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adaptadorT extends RecyclerView.Adapter<adaptadorT.ViewholderT>  {

    ArrayList<Transaccion> transacciones;
    Context ct;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
       void OnItemClick(int position);
       
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public adaptadorT(Context ct, ArrayList<Transaccion> transacciones) {
        this.transacciones = transacciones;
        this.ct= ct;
    }

    @NonNull
    @Override
    public adaptadorT.ViewholderT onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(ct).inflate(R.layout.transaccion,null,false
        );


        return new ViewholderT(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderT holder, int position) {

        holder.concepto.setText(transacciones.get(position).getConcepto());
        holder.monto.setText(transacciones.get(position).getMonto());
        holder.fecha.setText(transacciones.get(position).getFecha());
        holder.tipo.setText(transacciones.get(position).getTipo());

        Glide.with(ct)
                .load(transacciones.get(position).getDocumento())
                .placeholder(R.drawable.foto)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.documento);
       // holder.documento.setOnClickListener(this);
         String  definirTipo= transacciones.get(position).getTipo().toString();
         if (definirTipo.equals("1")) {
             holder.monto.setTextColor(Color.GREEN);
         } else {
             holder.monto.setTextColor(Color.RED);
         }

    }


    @Override
    public int getItemCount() {
        return transacciones.size();
    }




    public class ViewholderT extends RecyclerView.ViewHolder {

        TextView concepto, monto,fecha,tipo;
        ImageView documento;

        public ViewholderT(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);


            concepto= (TextView) itemView.findViewById(R.id.conceptoT);
            monto= (TextView) itemView.findViewById(R.id.montoT);
            fecha= (TextView) itemView.findViewById(R.id.fechaT);
            tipo= (TextView) itemView.findViewById(R.id.tipoT);
            documento =(ImageView) itemView.findViewById(R.id.documentoT);


            documento.setOnClickListener(new View.OnClickListener() {
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



        }




    }


}

