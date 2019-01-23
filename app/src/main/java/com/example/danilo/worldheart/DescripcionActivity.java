package com.example.danilo.worldheart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.Evento;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DescripcionActivity extends AppCompatActivity {

    ArrayList<Evento> listaEventos;
    TextView txtDescripcion,txtTitulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);
        
        txtTitulo=findViewById(R.id.txtTitulo);
        txtDescripcion=findViewById(R.id.txtDescripcion);
        cargarDescripcion();
        
    }

    private void cargarDescripcion() {

       final String url=Conf.servidor+ "/descripcion_eventos.php?id="+getIntent().getExtras().getString("idevento");
       // final String url=Conf.servidor+ "/descripcionevento.php?id="+getIntent().getExtras().getString("idevento");

        System.out.println(url);
        final AsyncHttpClient cliente=new AsyncHttpClient();
        cliente.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray json=new JSONArray(new String(responseBody));
                    txtTitulo.setText(json.getJSONObject(0).getString("nombre"));
                    txtDescripcion.setText(json.getJSONObject(0).getString("descripcion"));
                    String url="";

                }catch (Exception e){

                    Toast.makeText(DescripcionActivity.this, "error en descripcion "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
}
