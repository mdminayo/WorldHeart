package com.example.danilo.worldheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danilo.worldheart.Adaptadores.RecyclerEventsAdapter;
import com.example.danilo.worldheart.Clases.CircleTransform;
import com.example.danilo.worldheart.Clases.Conf;
import com.example.danilo.worldheart.Clases.Evento;
import com.example.danilo.worldheart.Clases.UserSession;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycler;
    TextView nombre,email;
    ImageView imagen;
    View viewnav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewnav=navigationView.getHeaderView(0);

        recycler=findViewById(R.id.recyclerEvents);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        nombre=viewnav.findViewById(R.id.nav_nameuser);
        email=viewnav.findViewById(R.id.nav_useremail);
        imagen=viewnav.findViewById(R.id.nav_userimage);


        consultarInformacionUser();
        cargarListadoEventos();
    }

    private void consultarInformacionUser() {
        String url=Conf.servidor+"/informacionUsuario.php?email="+UserSession.session;
        System.out.println("el url es"+url );

        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray json=new JSONArray(new String(responseBody));
                    nombre.setText(json.getJSONObject(0).getString("nombre"));
                    email.setText(json.getJSONObject(0).getString("email"));
                    String url="";

                    if (!json.getJSONObject(0).getString("foto").equalsIgnoreCase("null")){
                        url=Conf.servidorprincipal+"/"+json.getJSONObject(0).getString("foto").replace(" ","%20");
                        Picasso.get().load(url).error(R.mipmap.user).transform(new CircleTransform()).into(imagen);
                    }else{
                        Picasso.get().load(R.mipmap.user).error(R.mipmap.user).transform(new CircleTransform()).into(imagen);
                    }

                } catch (Exception e) {
                    Toast.makeText(PrincipalActivity.this, "error en "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void cargarListadoEventos() {

        String url=Conf.servidor+"/lista_eventos.php";
        System.out.println(url);
        AsyncHttpClient client=new AsyncHttpClient();

        final ArrayList<Evento> listadoEventos=new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray json=new JSONArray(new String(responseBody));
                    for (int i=0; i<json.length(); i++){
                        Evento evento=new Evento();
                        evento.setId(json.getJSONObject(i).getString("id"));
                        evento.setNombre(json.getJSONObject(i).getString("nombre"));
                        evento.setFechainicio(json.getJSONObject(i).getString("fechainicio"));
                        evento.setFechafin(json.getJSONObject(i).getString("fechafin"));
                        evento.setPoster(json.getJSONObject(i).getString("poster"));
                        listadoEventos.add(evento);
                    }
                    RecyclerEventsAdapter adapter=new RecyclerEventsAdapter(listadoEventos,PrincipalActivity.this);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(PrincipalActivity.this,DetalleEventoActivity.class);
                            intent.putExtra("id",listadoEventos.get(recycler.getChildAdapterPosition(v)).getId());
                            intent.putExtra("nombre",listadoEventos.get(recycler.getChildAdapterPosition(v)).getNombre());
                            intent.putExtra("poster",listadoEventos.get(recycler.getChildAdapterPosition(v)).getPoster());
                            intent.putExtra("fechainicio",listadoEventos.get(recycler.getChildAdapterPosition(v)).getFechainicio());
                            intent.putExtra("fechafin",listadoEventos.get(recycler.getChildAdapterPosition(v)).getFechafin());
                            startActivity(intent);
                        }
                    });
                    recycler.setAdapter(adapter);

                } catch (Exception e) {
                    Toast.makeText(PrincipalActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PrincipalActivity.this, "no se pudo conectar al servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_eventos) {
            // Handle the camera action
            Intent intent=new Intent(PrincipalActivity.this,PrincipalActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notificaciones) {

        } else if (id == R.id.nav_configuracion) {
            Intent intent=new Intent(PrincipalActivity.this,ConfiguracionActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_salir) {
            Intent intent=new Intent(PrincipalActivity.this,MainActivity.class);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
