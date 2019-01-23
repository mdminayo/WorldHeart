package com.example.danilo.worldheart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.danilo.worldheart.Adaptadores.GaleryRecyclerAdapter;
import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.Imagenes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GaleryActivity extends AppCompatActivity {
    ArrayList<Imagenes> listadoImagenes;
    RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recycler=findViewById(R.id.galeryRecycler);
        recycler.setLayoutManager(new GridLayoutManager(this,2));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        consultarImagenes();
    }

    private void consultarImagenes() {
        listadoImagenes=new ArrayList<>();
        String url=Conf.servidor+ "/listarImagenes.php?idEvento="+getIntent().getExtras().getString("idevento");
        System.out.println(url);
            AsyncHttpClient cliente=new AsyncHttpClient();
            cliente.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONArray json=new JSONArray(new String(responseBody));
                        for(int i=0; i<json.length(); i++){
                            Imagenes imagen=new Imagenes();
                            imagen.setId(json.getJSONObject(i).getString("id"));
                            imagen.setFoto(json.getJSONObject(i).getString("foto"));
                            imagen.setDescripcion(json.getJSONObject(i).getString("descripcion"));
                            imagen.setIdevento(json.getJSONObject(i).getString("idevento"));
                            listadoImagenes.add(imagen);

                        }
                        GaleryRecyclerAdapter adapter=new GaleryRecyclerAdapter(listadoImagenes,GaleryActivity.this);
                        recycler.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(GaleryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(GaleryActivity.this, "no se pudo conectar al servidor", Toast.LENGTH_SHORT).show();
                }


            });
    }

















}
