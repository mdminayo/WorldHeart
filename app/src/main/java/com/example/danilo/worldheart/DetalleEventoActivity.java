package com.example.danilo.worldheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danilo.worldheart.Clases.Conf;
import com.squareup.picasso.Picasso;

public class DetalleEventoActivity extends AppCompatActivity {
    String id="";
    ImageView imagen;
    TextView fechaInicio,fechaFin;
    LinearLayout layoutCalendario,layoutGaleria,layoutDescripcion,layoutComentario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_evento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("nombre"));

        layoutCalendario=findViewById(R.id.detalleLayoutCalendario);
        layoutGaleria=findViewById(R.id.detalleLayoutGaleria);
        layoutDescripcion=findViewById(R.id.detalleLayoutDescripcion);
        layoutComentario=findViewById(R.id.detalleLayoutComentario);

        id=getIntent().getExtras().getString("id");
        fechaInicio=findViewById(R.id.fechaInicio);
        fechaFin=findViewById(R.id.fechaFin);

        fechaInicio.setText(getIntent().getExtras().getString("fechainicio"));
        fechaFin.setText(getIntent().getExtras().getString("fechafin"));

        imagen=findViewById(R.id.poster);

        String url=Conf.servidorprincipal+"/"+getIntent().getExtras().getString("poster").replace(" ","%20");
        Picasso.get().load(url).error(R.drawable.ic_launcher_background).into(imagen);

        layoutGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetalleEventoActivity.this,GaleryActivity.class);
                intent.putExtra("idevento",id);
                startActivity(intent);
            }
        });

        layoutComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetalleEventoActivity.this,ComentariosActivity.class);
                intent.putExtra("idevento",id);
                startActivity(intent);
            }
        });
        layoutDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetalleEventoActivity.this,DescripcionActivity.class);
                intent.putExtra("idevento",id);
                startActivity(intent);
            }
        });





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetalleEventoActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }

}
