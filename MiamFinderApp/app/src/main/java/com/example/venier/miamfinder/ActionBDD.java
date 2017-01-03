package com.example.venier.miamfinder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ActionBDD extends AppCompatActivity {

    ContentResolver cr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bdd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

    }

    public void removeBDD(View view){
        cr = getContentResolver() ;
        cr.delete(Base.ADD_RESTAU, null, null) ;
        cr.delete(Base.ADD_MEDIA, null, null) ;
        cr.delete(Base.HORAIRES_ADD, null, null) ;
    }

    public void addBDD(View view){
        cr = getContentResolver() ;
        ContentValues row = new ContentValues() ;
        row.put(Base.RESTAURANT_NOM, "Le Square Marcadet") ;
        row.put(Base.RESTAURANT_ADRESSE, "227, bis rue Marcadet, 75018 Paris") ;
        row.put(Base.COORDONNEES, "48.8920383 2.3313764") ;
        row.put(Base.RESTAURANT_TELEPHONE, "01 53 11 08 41");
        row.put(Base.RESTAURANT_SITE, "http://www.square-marcadet.com/") ;
        row.put(Base.TARIF, "20€ à 40€") ;
        row.put(Base.TYPE_CUISINE, "Français") ;
        row.put(Base.ACCES_HANDICAPE, "Oui") ;
        row.put(Base.LIVRAISONS, "Non") ;
        row.put(Base.NOTE, "5") ;
        cr.insert(Base.ADD_RESTAU, row) ;
        addHoraires(getLastId("Le Square Marcadet")) ;

        row = new ContentValues() ;
        row.put(Base.RESTAURANT_NOM, "Brasserie Julien") ;
        row.put(Base.RESTAURANT_ADRESSE, "16, rue du Faubourg Saint-Denis 75010 Paris") ;
        row.put(Base.COORDONNEES, "48.8705869 2.3531901") ;
        row.put(Base.RESTAURANT_TELEPHONE, "01 47 70 12 06");
        row.put(Base.RESTAURANT_SITE, "http://www.julienparis.com/fr/") ;
        row.put(Base.TARIF, "+ de 40€") ;
        row.put(Base.TYPE_CUISINE, "Français") ;
        row.put(Base.ACCES_HANDICAPE, "Oui") ;
        row.put(Base.LIVRAISONS, "Non") ;
        row.put(Base.NOTE, "5") ;
        cr.insert(Base.ADD_RESTAU, row) ;
        addHoraires(getLastId("Brasserie Julien")) ;

        row = new ContentValues() ;
        row.put(Base.RESTAURANT_NOM, "Terminus Nord") ;
        row.put(Base.RESTAURANT_ADRESSE, "23, rue de Dunkerque 75010 Paris") ;
        row.put(Base.COORDONNEES, "48.8798336 2.354702") ;
        row.put(Base.RESTAURANT_TELEPHONE, "01 42 85 05 15");
        row.put(Base.RESTAURANT_SITE, "http://www.terminusnord.com/fr/") ;
        row.put(Base.TARIF, "+ de 40€") ;
        row.put(Base.TYPE_CUISINE, "Français") ;
        row.put(Base.ACCES_HANDICAPE, "Oui") ;
        row.put(Base.LIVRAISONS, "Non") ;
        row.put(Base.NOTE, "5") ;
        cr.insert(Base.ADD_RESTAU, row) ;
        addHoraires(getLastId("Terminus Nord")) ;

        row = new ContentValues() ;
        row.put(Base.RESTAURANT_NOM, "Sapporo") ;
        row.put(Base.RESTAURANT_ADRESSE, "37, rue Sainte-Anne 75001 Paris") ;
        row.put(Base.COORDONNEES, "48.8663717 2.3355358") ;
        row.put(Base.RESTAURANT_SITE, "http://sapporo.fr/") ;
        row.put(Base.TARIF, "20€ à 40€") ;
        row.put(Base.TYPE_CUISINE, "Japonais") ;
        row.put(Base.ACCES_HANDICAPE, "Non") ;
        row.put(Base.LIVRAISONS, "Non") ;
        row.put(Base.NOTE, "5") ;
        cr.insert(Base.ADD_RESTAU, row) ;
        addHoraires(getLastId("Sapporo")) ;
    }

    public void addHoraires(String idRestau){
        ContentValues row = new ContentValues() ;
        row.put(Base.ID_RESTAURANT, idRestau);
        row.put(Base.JOUR, "Lundi Mardi Mercredi Jeudi Vendredi Samedi");
        row.put(Base.HORAIRES_DEB, "11:30");
        row.put(Base.HORAIRES_FIN, "16:30");
        cr.insert(Base.HORAIRES_ADD, row) ;
        row = new ContentValues() ;
        row.put(Base.ID_RESTAURANT, idRestau);
        row.put(Base.JOUR, "Lundi Mardi Mercredi Jeudi Vendredi Samedi");
        row.put(Base.HORAIRES_DEB, "18:30");
        row.put(Base.HORAIRES_FIN, "23:30");
        cr.insert(Base.HORAIRES_ADD, row) ;
    }

    public String getLastId(String nomRestau){
        cr = getContentResolver() ;
        Cursor cursor = cr.query(Base.LAST_ID, new String[]{ "rowid" }, Base.RESTAURANT_NOM + " = ?", new String[]{ nomRestau }, "DESC") ;
        cursor.moveToFirst() ;
        Log.d("item : ", cursor.getString(cursor.getColumnIndex("rowid"))) ;
        return cursor.getString(cursor.getColumnIndex("rowid")) ;
    }
}
