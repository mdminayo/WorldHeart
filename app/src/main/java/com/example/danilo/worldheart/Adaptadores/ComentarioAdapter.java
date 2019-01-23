package com.example.danilo.worldheart.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danilo.worldheart.Clases.CircleTransform;
import com.example.danilo.worldheart.Clases.Comentario;
import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.Holder> {
    ArrayList<Comentario> listaComentarios;
    Context context;

    public ComentarioAdapter(ArrayList<Comentario> listaComentarios, Context context) {
        this.listaComentarios = listaComentarios;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_recycler_comentarios,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.email.setText(listaComentarios.get(i).getUsuario());
        holder.comentario.setText(listaComentarios.get(i).getComentario());
        holder.fecha.setText(listaComentarios.get(i).getFecha());
        if (!listaComentarios.get(i).getFoto().equalsIgnoreCase("null")){

        }

        if (!listaComentarios.get(i).getFoto().equalsIgnoreCase("null")){
            String url=Conf.servidorprincipal+"/"+listaComentarios.get(i).getFoto().replace(" ","%20");
            Picasso.get().load(url).error(R.mipmap.user).transform(new CircleTransform()).into(holder.imagen);
        }else{
            Picasso.get().load(R.mipmap.user).error(R.mipmap.user).transform(new CircleTransform()).into(holder.imagen);
        }

    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView email,comentario,fecha;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.cometarioimagen);
            email=itemView.findViewById(R.id.comentarioemail);
            comentario=itemView.findViewById(R.id.comentarios);
            fecha=itemView.findViewById(R.id.comentarioFecha);
        }
    }
}
