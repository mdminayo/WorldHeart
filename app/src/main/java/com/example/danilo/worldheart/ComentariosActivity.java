package com.example.danilo.worldheart;

import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danilo.worldheart.Adaptadores.ComentarioAdapter;
import com.example.danilo.worldheart.Adaptadores.GaleryRecyclerAdapter;
import com.example.danilo.worldheart.Clases.Comentario;
import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.Imagenes;
import com.example.danilo.worldheart.Clases.UserSession;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ComentariosActivity extends AppCompatActivity {
    ArrayList<Comentario> listaComentarios;
    RecyclerView recycler;
    EditText txtcomentario;
    Button btnComentar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        recycler=findViewById(R.id.comentarioRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        btnComentar=findViewById(R.id.comentariobtn);
        txtcomentario=findViewById(R.id.comentariotxt);
        cargarComentarios();
        
        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarComentario();
            }
        });
        
    }

    private void adicionarComentario() {
        if (txtcomentario.getText().toString().length()>0){
            String comentario=txtcomentario.getText().toString();
            String id=getIntent().getExtras().getString("idevento");
            SimpleDateFormat formato=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            Date date=new Date();
            String fecha=formato.format(date);
            String url=Conf.servidor+"/comentarioAdicionar.php?comentario="+comentario+"&fecha="+fecha+"&usuario="+UserSession.session+"&idevento="+id;
            AsyncHttpClient client=new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode==200){
                        cargarComentarios();

                        txtcomentario.setText("");
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }else{
            Toast.makeText(this, "No puede enviar comentarios vacios", Toast.LENGTH_SHORT).show();
        }

    }

    private void cargarComentarios() {
        listaComentarios=new ArrayList<>();
        String url=Conf.servidor+ "/comentarios.php?idEvento="+getIntent().getExtras().getString("idevento");
        final AsyncHttpClient cliente=new AsyncHttpClient();
        cliente.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray json=new JSONArray(new String(responseBody));
                    for(int i=0; i<json.length(); i++){
                        Comentario comentario=new Comentario();
                        comentario.setId(json.getJSONObject(i).getString("id"));
                        comentario.setComentario(json.getJSONObject(i).getString("comentario"));
                        comentario.setUsuario(json.getJSONObject(i).getString("usuario"));
                        comentario.setFecha(json.getJSONObject(i).getString("fecha"));
                        comentario.setIdevento(json.getJSONObject(i).getString("idevento"));
                        comentario.setFoto(json.getJSONObject(i).getString("foto"));
                        listaComentarios.add(comentario);

                    }
                    ComentarioAdapter adapter=new ComentarioAdapter(listaComentarios,ComentariosActivity.this);
                    recycler.setAdapter(adapter);
                    recycler.smoothScrollToPosition(recycler.getAdapter().getItemCount() - 1);
                } catch (Exception e) {
                    Toast.makeText(ComentariosActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ComentariosActivity.this, "no se pudo conectar al servidor", Toast.LENGTH_SHORT).show();
            }




        });
    }
}
