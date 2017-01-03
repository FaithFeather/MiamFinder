package com.example.venier.benkhemis.projetandroid.miamfindercontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Olivia on 07/11/2015.
 */
public class Base extends SQLiteOpenHelper {
    /* nom base de données */
    public static final String DB_NAME = "MiamFinder" ;

    /* nom de la table */
    public static final String RESTAURANT = "restaurants" ;
    public static final String RESTAURANT_NOM = "nom" ;
    public static final String RESTAURANT_ADRESSE = "adresse" ;
    public static final String COORDONNEES = "coordonnees" ;
    public static final String RESTAURANT_TELEPHONE = "telephone" ;
    public static final String RESTAURANT_SITE = "site" ;
    public static final String TARIF = "tarif" ;
    public static final String TYPE_CUISINE = "type_cuisine" ;
    public static final String NOTE = "note" ;
    public static final String ACCES_HANDICAPE = "acces_handicape" ;
    public static final String LIVRAISONS = "livraisons" ;

    /* nom de la table */
    public static final String JOURS_OUVERTURE_RESTAU = "jours_ouverture_restau" ;
    public static final String ID_RESTAURANT = "id_restaurant" ;
    public static final String JOUR = "jour" ;
    public static final String HORAIRES_DEB = "horaires_deb" ;
    public static final String HORAIRES_FIN = "horaires_fin" ;

    /* nom de la table */
    public static final String MEDIA_RESTAU = "media_restaurant" ;
    public static final String TYPE_MEDIA_R = "nom_media_restau" ;
    public static final String MEDIA = "media" ;
    public static final String DATE = "date" ;


    private static final int VERSION = 9 ;

    private static final String CREATE_RESTAURANT = "CREATE TABLE IF NOT EXISTS  " +  RESTAURANT
            + "(" + RESTAURANT_NOM + " String_not_null, " + RESTAURANT_ADRESSE + " String_not_null, "
            + COORDONNEES + " String_not_null, "
            + RESTAURANT_TELEPHONE + " String_not_null, " + RESTAURANT_SITE + " String_not_null, "
            + TARIF + " String_not_null, " + TYPE_CUISINE + " String_not_null, " + NOTE + " String_not_null, "
            + LIVRAISONS + " String_not_null, " + ACCES_HANDICAPE + " String_not_null);" ;

    private static final String CREATE_JOURS_OUVERTURE_RESTAU = "CREATE TABLE IF NOT EXISTS  " +  JOURS_OUVERTURE_RESTAU
            + "(" + ID_RESTAURANT + " String_not_null, " + JOUR + " String_not_null, "
            + HORAIRES_DEB +  " date, " + HORAIRES_FIN +  " date);" ;

    private static final String CREATE_MEDIA_RESTAU = "CREATE TABLE IF NOT EXISTS  " +  MEDIA_RESTAU
            + "(" + ID_RESTAURANT + " String_not_null, " + TYPE_MEDIA_R + " String_not_null, "
            + MEDIA +  " String_not_null, " + DATE + " String_not_null);" ;

    public Base(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RESTAURANT);
        Log.d("Table", CREATE_RESTAURANT);
        db.execSQL(CREATE_JOURS_OUVERTURE_RESTAU);
        Log.d("Table", CREATE_JOURS_OUVERTURE_RESTAU);
        db.execSQL(CREATE_MEDIA_RESTAU);
        Log.d("Table", CREATE_MEDIA_RESTAU) ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("drop table if exists " + RESTAURANT);
            db.execSQL("drop table if exists " + JOURS_OUVERTURE_RESTAU);
            db.execSQL("drop table if exists " + MEDIA_RESTAU);
            onCreate(db);
        }
    }
}