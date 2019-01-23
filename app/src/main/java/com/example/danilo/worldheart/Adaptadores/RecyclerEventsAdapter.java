package com.example.danilo.worldheart.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.Evento;
import com.example.danilo.worldheart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerEventsAdapter extends RecyclerView.Adapter<RecyclerEventsAdapter.Holder> implements View.OnClickListener {
    ArrayList<Evento> listaEventos;
    Context contex;
    String colors[]={"#102229","#df9048","#1aab6c","#414ea5"};
    int contador=0;
    View.OnClickListener listener;


    public RecyclerEventsAdapter(ArrayList<Evento> listaEventos, Context contex) {
        this.listaEventos = listaEventos;
        this.contex = contex;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_recycler_eventos,viewGroup,false);
        view.setOnClickListener(this);
        return new Holder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        holder.layout.setBackgroundColor(Color.parseColor(colors[contador]));
        holder.fechaInicio.setText(listaEventos.get(i).getFechainicio());
        holder.fechaFin.setText(listaEventos.get(i).getFechafin());
        holder.nombre.setText(listaEventos.get(i).getNombre());
        String url=Conf.servidorprincipal+"/"+listaEventos.get(i).getPoster().replace(" ","%20");
        Picasso.get().load(url).error(R.drawable.ic_launcher_background).into(holder.imagen);

        if (contador==3){
            contador=0;
        }else{
            contador++;
        }
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imagen;
        ConstraintLayout layout;
        TextView fechaInicio,fechaFin,nombre;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.eventimg);
            fechaInicio=itemView.findViewById(R.id.eventlabelinicio);
            fechaFin=itemView.findViewById(R.id.eventlabefin);
            nombre=itemView.findViewById(R.id.eventlabelname);
            layout=itemView.findViewById(R.id.eventsLayout);
        }
    }
}
