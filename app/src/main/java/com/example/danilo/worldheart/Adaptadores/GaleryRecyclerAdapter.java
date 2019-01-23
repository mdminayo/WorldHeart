package com.example.danilo.worldheart.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.Imagenes;
import com.example.danilo.worldheart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GaleryRecyclerAdapter extends RecyclerView.Adapter<GaleryRecyclerAdapter.Holder> {
    ArrayList<Imagenes> listadoImagenes;
    Context context;

    public GaleryRecyclerAdapter(ArrayList<Imagenes> listadoImagenes, Context context) {
        this.listadoImagenes = listadoImagenes;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_recycler_galery,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        String url=Conf.servidorprincipal+"/"+listadoImagenes.get(i).getFoto().replace(" ","%20");
        System.out.println(url);
        Picasso.get().load(url).error(R.drawable.side_nav_bar).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return listadoImagenes.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imagen;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.galeryImageView);
        }
    }
}
