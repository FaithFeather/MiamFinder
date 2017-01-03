package com.example.venier.miamfinder;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultatsRecherche extends AppCompatActivity {
    private ContentResolver cr ;
    private Uri uri ;
    private ListView lv ;
    private String criteres_nom_adresse = null ;
    private ArrayList<String> criteres_cuisine  = null ;
    private String criteres_heure = null;
    private String criteres_jour = null ;
    private String criteres_tarif  = null ;
    private String criteres_note  = null ;
    private int flag = 0 ;
    private String criteres_distance  = null ;
    private LocationManager lm;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_recherche);

        Intent search_result = getIntent();

        criteres_nom_adresse = search_result.getStringExtra("critere_nom_adresse") ;
        criteres_cuisine = search_result.getStringArrayListExtra("critere_cuisine");
        criteres_heure = search_result.getStringExtra("criteres_heure");
        criteres_jour = search_result.getStringExtra("criteres_jour");
        criteres_tarif = search_result.getStringExtra("criteres_tarif");
        criteres_distance = search_result.getStringExtra("criteres_distance") ;
        criteres_note = search_result.getStringExtra("criteres_note") ;

        affichageRecherche() ;
    }

    private void affichageRecherche() {
        if(criteres_nom_adresse != null)
            Log.d("requete", criteres_nom_adresse) ;
        if(criteres_heure != null)
            Log.d("requete", criteres_heure) ;
        if(criteres_jour != null)
            Log.d("requete", criteres_jour) ;
        if(criteres_tarif != null)
            Log.d("requete", criteres_tarif) ;

        cr = getContentResolver();
        Cursor cursor;
        String[] crit_nom, crit_cuisine, crit_heure, crit_jour, crit_tarif, crit_note, criteres_finaux = new String[0] ;
        String select = "" ;

        uri = Base.TOUS_RESTAURANT ;
        if(criteres_nom_adresse != null){
            uri = Base.RESTAURANT_SEARCH;
            crit_nom = new String[2];
            for(int i = 0 ; i < 2 ; i++)
                crit_nom[i] = "%"+criteres_nom_adresse+"%" ;
            select += "(" + Base.RESTAURANT_NOM + " = ? OR " + Base.RESTAURANT_ADRESSE + " = ?)" ;
            criteres_finaux = concat(criteres_finaux, crit_nom) ;
        }
        if(criteres_cuisine.size() != 0){
            uri = Base.CUISINE_SEARCH;
            crit_cuisine = new String[criteres_cuisine.size()];
            if(select.length() > 0)
                select += " AND (" ;
            else
                select += "(" ;
            select += Base.TYPE_CUISINE + " = ?" ;
            crit_cuisine[0] = criteres_cuisine.get(0);
            for (int j = 1; j < criteres_cuisine.size(); j++) {
                select += " OR " +  Base.TYPE_CUISINE + " = ?" ;
                crit_cuisine[j] = criteres_cuisine.get(j);
            }
            select += ")" ;
            criteres_finaux = concat(criteres_finaux, crit_cuisine) ;
        }
        if(criteres_tarif != null){
            if(select.length() > 0)
                select += " AND " ;
            uri = Base.TARIF_SEARCH;
            crit_tarif = new String[1];
            crit_tarif[0] = criteres_tarif ;
            select += "(" + Base.TARIF + " = ?)" ;
            criteres_finaux = concat(criteres_finaux, crit_tarif) ;
        }
        if(criteres_heure != null || criteres_jour !=null) {
            if(select.length() > 0)
                select += " AND " ;
            select += " rowid IN ( SELECT " + Base.ID_RESTAURANT + " FROM " + Base.JOURS_OUVERTURE_RESTAU + " WHERE " ;

            if (criteres_heure != null) {
                uri = Base.HEURES_SEARCH;
                crit_heure = new String[2];
                for (int i = 0; i < 2; i++)
                    crit_heure[i] = criteres_heure;
                select += "(" + Base.HORAIRES_DEB + " < ? AND " + Base.HORAIRES_FIN + " > ?)";
                criteres_finaux = concat(criteres_finaux, crit_heure);
            }
            if (criteres_jour != null) {
                if (criteres_heure != null)
                    select += " AND ";
                uri = Base.JOURS_SEARCH;
                crit_jour = new String[1];
                crit_jour[0] = "%" + criteres_jour + "%";
                select += "(" + Base.JOUR + " LiKE ?)";
                criteres_finaux = concat(criteres_finaux, crit_jour);
            }
            select += ")" ;
        }
        if(criteres_note != null){
            select += " AND ";
            uri = Base.NOTE_SEARCH;
            crit_note = new String[] { criteres_note };
            select += "(" + Base.NOTE + " >= ?)" ;
            criteres_finaux = concat(criteres_finaux, crit_note) ;
        }

        Log.d("requete", select);
        Log.d("requete", Integer.toString(flag)) ;

        if(flag > 1)
            uri = Base.ALL_CASE ;

        for(int i = 0 ; i < criteres_finaux.length ; i++)
            Log.d("requete", criteres_finaux[i]) ;

        Log.d("requete", uri.toString()) ;
        cursor = getRestaurants(criteres_finaux, select);
        CustomCursorAdapter adapter ;

        if(criteres_distance != null) {
            try {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // Define the criteria how to select the location provider
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

                // user defines the criteria

                criteria.setCostAllowed(false);
                // get the best provider depending on the criteria
                String provider = locationManager.getBestProvider(criteria, false);
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  // the last known location of this provider
                Location location = locationManager.getLastKnownLocation(provider);
                adapter = new CustomCursorAdapter(this, cursor, Integer.parseInt(criteres_distance), location);
            } catch(Exception e){
                e.printStackTrace();
                Toast.makeText(ResultatsRecherche.this, R.string.impossibleGPS, Toast.LENGTH_SHORT).show();
                adapter = new CustomCursorAdapter(this, cursor) ;
            }
        } else
            adapter = new CustomCursorAdapter(this, cursor) ;
        lv = (ListView)findViewById(R.id.item_contact);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(pos);
                String itemid = cursor.getString(cursor.getColumnIndex("_id"));//Repeat for other values
                Log.d("Cursor", itemid);
                show_restaurant(itemid);
            }
        });
    }


    private void show_restaurant(String itemid) {
        Intent restaurant;
        restaurant = new Intent(this, Restaurant_Onglets.class);
        restaurant.putExtra("itemid", itemid);
        startActivity(restaurant);

    }

    public String[] concat(String[] tab1, String[] tab2){
        flag++ ;
        String[] result = new String[tab1.length + tab2.length] ;
        for(int i = 0 ; i < tab1.length ; i++)
            result[i] = tab1[i] ;
        for(int i = tab1.length ; i < result.length ; i++)
            result[i] = tab2[i-tab1.length] ;
        return result ;
    }

    public Cursor getRestaurants(String[] criteres_finaux, String projection){
        Cursor cursor = cr.query(uri, new String[]{"DISTINCT rowid AS _id", Base.COORDONNEES, Base.RESTAURANT_NOM, Base.RESTAURANT_ADRESSE, Base.TARIF, Base.NOTE}, projection, criteres_finaux, null) ;
        return cursor ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultats_recherche, menu);
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

}
