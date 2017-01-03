package com.example.venier.miamfinder;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Informations_generales extends Fragment {
    String itemid ;
    private ContentResolver cr;
    TextView title ;
    TextView address  ;
    TextView tel  ;
    TextView siteweb ;
    TextView infos ;
    Cursor cursor ;

    public Informations_generales() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_informations_generales, container, false);
        itemid = getArguments().getString("itemid");
        Log.d("item id ", itemid) ;
        cr = getActivity().getContentResolver() ;
        cursor = cr.query(Base.ALL_INFORMATIONS, new String[]{" * "}, null, new String[]{itemid}, null) ;
        cursor.moveToNext() ;

        title = (TextView) v.findViewById(R.id.textView4) ;
        address = (TextView) v.findViewById(R.id.textView12) ;
        tel = (TextView) v.findViewById(R.id.textView17) ;
        siteweb = (TextView) v.findViewById(R.id.textView18) ;

        title.setText(cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_NOM)));
        address.setText(cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_ADRESSE)));
        tel.setText(cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_TELEPHONE)));
        siteweb.setText(cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_SITE)));

        infos = (TextView) v.findViewById(R.id.textView16) ;

        String infos_str = "Cuisine : " + cursor.getString(cursor.getColumnIndex(Base.TYPE_CUISINE)) + "\n" ;
        infos_str += "Accès handicapé : " + cursor.getString(cursor.getColumnIndex(Base.ACCES_HANDICAPE)) + "\n" ;
        infos_str += "Livraisons à domicile : " + cursor.getString(cursor.getColumnIndex(Base.LIVRAISONS)) +  "\n" ;
        infos.setText(infos_str) ;

        if(cursor.getString(cursor.getColumnIndex(Base.COORDONNEES)) != null) {

            Button gps = (Button) v.findViewById(R.id.button3);
            gps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String coord = cursor.getString(cursor.getColumnIndex(Base.COORDONNEES)) ;
                    String[] latlong = coord.split(" ");
                    Uri uri = Uri.parse("geo:" + latlong[0] + "," + latlong[1] + "?q=" + Uri.encode(address.getText().toString()));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setData(uri);
                    cursor.close();
                    startActivity(intent);

                }
            });
        }
        return v ;
    }



}

