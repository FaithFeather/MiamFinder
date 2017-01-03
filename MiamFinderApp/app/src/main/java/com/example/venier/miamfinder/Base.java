package com.example.venier.miamfinder;

import android.net.Uri;

/**
 * Created by Olivia on 19/11/2015.
 */
public class Base {

    public static final Uri ALL_CASE = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/all");
    public static final Uri TOUS_RESTAURANT = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/tout");

    public static final Uri LAST_ID = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/last");

    public static final Uri ADD_MEDIA = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/ajout/media_restaurant") ;
    public static final Uri ADD_RESTAU = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/ajout/restaurants") ;
    public static final Uri HORAIRES_ADD = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/ajout/jours_ouverture_restau") ;


    public static final Uri RESTAURANT_SEARCH = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/nom");
    public static final Uri CUISINE_SEARCH = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/type_cuisine");
    public static final Uri TARIF_SEARCH = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/tarif");
    public static final Uri JOURS_SEARCH = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/jours");
    public static final Uri HEURES_SEARCH = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/heures");
    public static final Uri NOTE_SEARCH = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/note");
    public static final Uri RESTAURANT_HORAIRES = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/jours_ouverture_restau");
    public static final Uri MEDIA_SEARCH = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/media_restaurant");
    public static final Uri ID_HORAIRES = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/id_horaires");
    public static final Uri ALL_INFORMATIONS = Uri.parse("content://fr.benkhemis.venier.miamfinder.contentprovider/recherche/*");

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
    public static final String DATE = "date" ;
    public static final String MEDIA = "media" ;

    public static final String PHOTO = "photo" ;
    public static final String MENU_W = "menu_w" ;
    public static final String MENU_P = "menu_p" ;
    public static final String COM_W = "comm_w" ;

    public Base(){}
}
