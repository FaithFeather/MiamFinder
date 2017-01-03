package com.example.venier.miamfinder;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Horaires_Tarifs extends Fragment {
    private ContentResolver cr;
    private String itemid ;
    private TextView horaires ;
    private TextView tarifs ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_horaires__tarifs, container, false);
        itemid = getArguments().getString("itemid");
        cr = getActivity().getContentResolver() ;
        Cursor cursor = setHoraires() ;
        horaires = (TextView) v.findViewById(R.id.textView14) ;
        tarifs = (TextView) v.findViewById(R.id.textView16) ;
        String all_horaires = "" ;
        String[] semaine = { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" } ;
        cursor.moveToNext();
        if(cursor.getCount() != 0) {
            String jours = cursor.getString(cursor.getColumnIndex(Base.JOUR));
            all_horaires += "Midi : " + cursor.getString(cursor.getColumnIndex(Base.HORAIRES_DEB))
                    + " - " + cursor.getString(cursor.getColumnIndex(Base.HORAIRES_FIN)) + "\n";
            cursor.moveToNext();
            all_horaires += "Soir : " + cursor.getString(cursor.getColumnIndex(Base.HORAIRES_DEB))
                    + " - " + cursor.getString(cursor.getColumnIndex(Base.HORAIRES_FIN)) + "\n";
            all_horaires += "Jours de fermeture : ";

            for (String s : semaine) {
                if (!jours.contains(s))
                    all_horaires += s + " ";
            }
        }
        else
            all_horaires = "Pas d'informations" ;
        horaires.setText(all_horaires) ;
        cursor = cr.query(Base.ALL_INFORMATIONS, new String[]{ Base.TARIF }, null, new String[]{ itemid }, null) ;
        cursor.moveToNext() ;
        tarifs.setText(cursor.getString(cursor.getColumnIndex(Base.TARIF)));
        cursor.close();
        return v;
    }


    private Cursor setHoraires() {
        Cursor cursor = cr.query(Base.RESTAURANT_HORAIRES, new String[]{"*"}, null, new String[]{ itemid }, null) ;
        return cursor ;
    }
}
