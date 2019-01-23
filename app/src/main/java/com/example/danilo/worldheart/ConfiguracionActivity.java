package com.example.danilo.worldheart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danilo.worldheart.Clases.CircleTransform;
import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.UserSession;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import cz.msebera.android.httpclient.Header;

public class ConfiguracionActivity extends AppCompatActivity {

    TextView textView,txtEmail;
    EditText etxtNombre, etxtNacimiento,etxtTelefono,etxtDireccion;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        txtEmail=findViewById(R.id.txtEmail);
        etxtNombre=findViewById(R.id.etxtNombre);
        etxtNacimiento=findViewById(R.id.etxtNacimiento);
        etxtTelefono=findViewById(R.id.etxtTelefono);
        etxtDireccion=findViewById(R.id.etxtDireccion);

        btnGuardar=findViewById(R.id.btnGuardar);

        configuracionUser();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread tr=new Thread(){
                    @Override
                    public void run(){
                        final String res=enviarPost(etxtNombre.getText(),etxtNacimiento.getText(),etxtTelefono.getText(),etxtDireccion.getText());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=objJSON(res);
                                if(r>0){
                                    Toast.makeText(getApplicationContext(),"Registro exitoso",Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                }else {

                                   // Toast.makeText(getApplicationContext(),"Registro fallido",Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


                    }
                };
                tr.start();
            }
        });



    }

    private String enviarPost( Editable text1, Editable text2, Editable text3, Editable text4) {

        String parametros="nombre="+text1+"fechadenacimiento="+text2+"telefono="+text3+"direccion="+text4;
        HttpURLConnection conection=null;
        String respuesta="";

        try {
            URL url= new URL(Conf.servidor+"/webservices/pruebaupdate.php?email=danilo");
            Toast.makeText(getApplicationContext(),"Registro fallido"+url,Toast.LENGTH_SHORT).show();
            conection=(HttpURLConnection)url.openConnection();
            conection.setRequestMethod("POST");
            conection.setRequestProperty("Content-length",""+Integer.toString(parametros.getBytes().length));

            conection.setDoOutput(true);
            DataOutputStream wr=new DataOutputStream(conection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();

            Scanner inStrem = new Scanner(conection.getInputStream());

            while (inStrem.hasNext())respuesta+=(inStrem.nextLine());

        }catch (Exception e){}

        return respuesta.toString();
    }


    //******este funciona//

    private void configuracionUser() {

        String url=Conf.servidor+"/informacionUsuario.php?email="+UserSession.session;
        System.out.println("el url es"+url );


        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray json=new JSONArray(new String(responseBody));
                    etxtNombre.setText(json.getJSONObject(0).getString("nombre"));
                    txtEmail.setText(json.getJSONObject(0).getString("email"));
                    etxtNacimiento.setText(json.getJSONObject(0).getString("fechanacimiento"));
                    etxtTelefono.setText(json.getJSONObject(0).getString("telefono"));
                    etxtDireccion.setText(json.getJSONObject(0).getString("direccion"));
                    String url="";



                } catch (Exception e) {
                    Toast.makeText(ConfiguracionActivity.this, "error en "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    //***********hasta aqui

    private int objJSON(String rspta) {

        int res=0;
        try {
            JSONArray json = new JSONArray(rspta);
            if (json.length()>0) res=1;
        }catch (Exception e){}
        return res;
    }
}
