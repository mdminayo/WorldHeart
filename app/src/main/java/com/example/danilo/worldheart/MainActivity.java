package com.example.danilo.worldheart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.UserSession;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    EditText txtCor,txtPass;
    Button btnIngresar,btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCor=(EditText) findViewById(R.id.txtCor);
        txtPass=(EditText) findViewById(R.id.txtPass);

        btnIngresar=(Button) findViewById(R.id.btnIngresar);
        btnRegistrar= findViewById(R.id.btnRegistrar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String url=Conf.servidor+"/login.php?email="+txtCor.getText().toString()+"&clave="+txtPass.getText().toString();
                String url=Conf.servidor+"/login.php?email="+txtCor.getText().toString()+"&clave="+txtPass.getText().toString();
                AsyncHttpClient client=new AsyncHttpClient();
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONArray json=new JSONArray(new String(responseBody));
                            if (json.length()>0){
                                UserSession.session=json.getJSONObject(0).getString("email");
                                startActivity(new Intent(MainActivity.this,PrincipalActivity.class));
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this, "Verifique el usuario o la contraseña", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
