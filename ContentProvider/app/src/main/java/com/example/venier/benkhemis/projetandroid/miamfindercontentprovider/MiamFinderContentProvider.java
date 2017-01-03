package com.example.venier.benkhemis.projetandroid.miamfindercontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Olivia on 07/11/2015.
 */
public class MiamFinderContentProvider extends ContentProvider {
    final String CONTENT_TYPE = "vnd.android.cursor.dir/restaurants";
    final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/restaurant";

    private static final int ALL_CASE = 0;
    private static final int TOUS_RESTAURANT = 1;
    private static final int RESTAURANT_SEARCH = 2 ;
    private static final int CUISINE_SEARCH = 3;
    private static final int TARIF_SEARCH = 4;
    private static final int JOURS_SEARCH = 5;
    private static final int HEURES_SEARCH = 6;
    private static final int ADD_MEDIA = 7 ;
    private static final int ALL_INFORMATIONS = 8 ;
    private static final int RESTAURANT_HORAIRES = 9 ;
    private static final int NOTE_SEARCH = 10 ;
    private static final int LAST_ID = 11 ;
    private static final int MEDIA_SEARCH = 13 ;
    private static final int ID_HORAIRES = 14 ;

    private static final int AJOUTER_RESTAU = 12;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final Uri CONTENT_URI = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/tout");

    static {
        /* RECHERCHE */
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/jours_ouverture_restau", RESTAURANT_HORAIRES);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/all", ALL_CASE);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/last", LAST_ID);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/tout", TOUS_RESTAURANT);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/nom", RESTAURANT_SEARCH);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/type_cuisine", CUISINE_SEARCH);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/tarif", TARIF_SEARCH);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/jours", JOURS_SEARCH);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/heures", HEURES_SEARCH);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/note", NOTE_SEARCH) ;
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/media_restaurant", MEDIA_SEARCH);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/id_horaires", ID_HORAIRES);
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "recherche/*", ALL_INFORMATIONS);

        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "ajout/media_restaurant", ADD_MEDIA) ;

        /* INSERTION RESTAURANT */
        matcher.addURI("fr.benkhemis.venier.miamfinder.contentprovider", "ajout/restaurants", AJOUTER_RESTAU);

    }

    private Base helper;
    private SQLiteDatabase db;

    public boolean onCreate() {
        try {
            helper = new Base(getContext());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Uri insert(Uri uri, ContentValues values) {
        db = helper.getWritableDatabase();
        String parse = uri.getLastPathSegment() ;
        Log.d("parser : ", parse) ;
        long id = db.insert(parse, null, values);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = helper.getWritableDatabase();
        int rowsDeleted;
        rowsDeleted = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = helper.getWritableDatabase();
        int rowsChanged ;
        Log.d("UPDATE", uri.getLastPathSegment()) ;
        rowsChanged = db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
        return rowsChanged;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db=helper.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor ;
        String select ;
        sortOrder = Base.NOTE ;
        Log.d("URI", Integer.toString(code)) ;
        switch (code) {
            case ALL_CASE :
                Log.d("provider ", "COMPLEXE");
                for(int i = 0 ; i < selectionArgs.length ; i++)
                    Log.d("provider ", selectionArgs[i]);
                Log.d("provider", selection) ;
                cursor = db.query(Base.RESTAURANT, projection, selection, selectionArgs, null, null, sortOrder) ;
                break ;
            case LAST_ID :
                Log.d("provider", "Last id") ;
                cursor = db.query(Base.RESTAURANT, projection, selection, selectionArgs, null, null, sortOrder) ;
                break ;
            case TOUS_RESTAURANT:
                Log.d("provider ", "ALL_REST");
                cursor = db.query(Base.RESTAURANT, projection, null, null, null, null, sortOrder);
                break;
            case CUISINE_SEARCH :
                Log.d("provider ", "Par cuisine");
                select = Base.TYPE_CUISINE + " = ? " ;
                for(int i = 1 ; i < selectionArgs.length ; i++)
                    select += "OR " + select ;
                cursor = db.query(Base.RESTAURANT, projection, select, selectionArgs, null, null, null) ;
                break ;
            case JOURS_SEARCH :
                Log.d("provider ", "Par jours");
                Log.d("provider ", selectionArgs[0]);
                cursor = db.query(Base.RESTAURANT, projection,
                        " rowid IN ( SELECT "+ Base.ID_RESTAURANT +" FROM " + Base.JOURS_OUVERTURE_RESTAU
                                + " WHERE " + Base.JOUR + " LIKE ?)" , new String[]{ "%" + selectionArgs[0] + "%"}, null, null, null) ;
                break ;
            case HEURES_SEARCH :
                Log.d("provider ", "Par heures");
                Log.d("provider ", selectionArgs[0]);
                cursor = db.query(Base.RESTAURANT, projection,
                        " rowid IN (SELECT " + Base.ID_RESTAURANT + " FROM " + Base.JOURS_OUVERTURE_RESTAU
                                + " WHERE " + Base.HORAIRES_DEB + " < ? AND "+ Base.HORAIRES_FIN + " > ?)" , selectionArgs, null, null, null) ;
                break ;
            case TARIF_SEARCH :
                Log.d("provider ", "Par tarif");
                Log.d("provider ", selectionArgs[0]);
                cursor = db.query(Base.RESTAURANT, projection, Base.TARIF + " = ?", selectionArgs, null, null, null) ;
                break ;
            case RESTAURANT_SEARCH :
                Log.d("provider ", "size : " + selectionArgs.length) ;
                for(int i = 0 ; i < selectionArgs.length ; i++)
                    selectionArgs[i] = "%"+selectionArgs[i]+"%" ;
                cursor = db.query(Base.RESTAURANT, projection, Base.RESTAURANT_NOM + " like ? or " + Base.RESTAURANT_ADRESSE + " like ?", selectionArgs, null, null, null) ;
                break ;
            case RESTAURANT_HORAIRES :
                Log.d("provider ", "Par horaires");
                cursor = db.query(Base.JOURS_OUVERTURE_RESTAU, projection, Base.ID_RESTAURANT + " = ?", selectionArgs, null, null, null) ;
                break ;
            case NOTE_SEARCH :
                Log.d("provider ", "Par note");
                cursor = db.query(Base.RESTAURANT, projection, Base.NOTE + " >= ?", selectionArgs, null, null, null) ;
                break ;
            case MEDIA_SEARCH :
                Log.d("provider ", "Media" ) ;
                Log.d("provider ", selection ) ;
                cursor = db.query(Base.MEDIA_RESTAU, projection, selection, selectionArgs, null, null, null) ;
                break ;
            case ID_HORAIRES :
                Log.d("provider ", "id horaires");
                cursor = db.query(Base.JOURS_OUVERTURE_RESTAU, projection, selection, selectionArgs, null, null, null) ;
                break ;
            case ALL_INFORMATIONS :
                Log.d("provider ", "toutes les infos");
                cursor = db.query(Base.RESTAURANT, projection, "rowid = ? ", selectionArgs, null, null, null) ;
                break ;
             default:
                return null;
        }
        Log.d("provider ", "count=" + cursor.getCount());
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
