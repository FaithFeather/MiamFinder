package com.example.venier.miamfinder;

import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Calendar;

public class HomePage extends AppCompatActivity {
    private Button ajout, mesrestaux, herenow;

    private OnClickListener hereAndNow = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent r = new Intent(HomePage.this, ResultatsRecherche.class) ;
            String criteres_nom_adresse = null;
            Calendar rightNow = Calendar.getInstance() ;
            String criteres_heure = rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE);
            Log.d("heure:minute = ", rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE));
            int day = rightNow.get(Calendar.DAY_OF_WEEK_IN_MONTH) ;
            String[] jours = new String[] {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"} ;
            String criteres_jour = jours[day] ;
            String criteres_tarif = null ;
            String criteres_distance = "1" ;
            String criteres_note = null ;
            ArrayList<String> criteres_cuisine = new ArrayList() ;

            r.putExtra("critere_nom_adresse", criteres_nom_adresse) ;
            r.putStringArrayListExtra("critere_cuisine", criteres_cuisine);
            r.putExtra("criteres_heure", criteres_heure) ;
            r.putExtra("criteres_jour", criteres_jour) ;
            r.putExtra("criteres_tarif", criteres_tarif) ;
            r.putExtra("criteres_distance", criteres_distance) ;
            r.putExtra("criteres_note", criteres_note);
            startActivity(r);

        }
    };

    private OnClickListener clickAjout = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent a = new Intent(HomePage.this, Ajouter.class);
            startActivity(a);
        }
    };

    private OnClickListener clickMesRestaux = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            Intent r = new Intent(HomePage.this, ResultatsRecherche.class);
                            String criteres_nom_adresse = null;
                            String criteres_heure = null ;
                            String criteres_jour = null;
                            String criteres_tarif = null ;
                            String criteres_distance = null ;
                            String criteres_note = null ;
                            ArrayList<String> criteres_cuisine = new ArrayList() ;

                            r.putExtra("critere_nom_adresse", criteres_nom_adresse) ;
                            r.putStringArrayListExtra("critere_cuisine", criteres_cuisine);
                            r.putExtra("criteres_heure", criteres_heure) ;
                            r.putExtra("criteres_jour", criteres_jour) ;
                            r.putExtra("criteres_tarif", criteres_tarif) ;
                            r.putExtra("criteres_distance", criteres_distance) ;
                            r.putExtra("criteres_note", criteres_note);
                            startActivity(r);
                        }

                    });
                }
            }).start();
        }
    };

    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ajout = (Button)findViewById(R.id.nouvrestau);
        mesrestaux = (Button)findViewById(R.id.lesrestaux);

        ajout.setOnClickListener(clickAjout);
        mesrestaux.setOnClickListener(clickMesRestaux);

        herenow = (Button) findViewById(R.id.buttonnow) ;
        herenow.setOnClickListener(hereAndNow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                searchFunction() ;
                break;
            case R.id.action_quit:
                finish();
                return true;
            case R.id.action_bdd :
                Intent iii = new Intent(this, ActionBDD.class) ;
                startActivity(iii);
                break ;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchFunction(){
        Intent aa = new Intent(this, Recherche.class) ;
        startActivity(aa) ;
    }

}
