package com.example.venier.miamfinder;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Livia on 20/11/2015.
 */
public class CustomCursorAdapter extends CursorAdapter{
    double rayon = 0;
    Location actuel ;
    Context context ;

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public CustomCursorAdapter(Context context, Cursor c, double rayon, Location actuel) {
        super(context, c);
        this.actuel = actuel ;
        this.rayon = rayon ;
        this.context = context ;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row =  LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        return row ;
    }


    public void confirmDialog(int itemid, Context context){
        Dialog dialog;
        final int item = itemid ;
        final Context contexte = context ;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmer la suppression") ;
        builder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                remove(item, contexte) ;
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        }) ;
        dialog = builder.create();
        dialog.show();
    }

    public void remove(int item, Context context){
        ContentResolver cr = context.getContentResolver();
        Intent intent = new Intent(context, HomePage.class);
        cr.delete(Base.HORAIRES_ADD, Base.ID_RESTAURANT + " = ?", new String[]{Integer.toString(item)});
        cr.delete(Base.ADD_RESTAU, "rowid = ?", new String[]{Integer.toString(item)});
        context.startActivity(intent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView note = (TextView) view.findViewById(R.id.note);
        TextView nom = (TextView) view.findViewById(R.id.nom);
        TextView adresse = (TextView) view.findViewById(R.id.adresse);
        TextView tarif = (TextView) view.findViewById(R.id.tarif);
        RelativeLayout l = (RelativeLayout) view.findViewById(R.id.layout);
        String latitude = cursor.getString(cursor.getColumnIndex(Base.COORDONNEES));

        String[] coordonnees = latitude.split(" ");
        Log.d("rest - lat  : ", coordonnees[0]);
        Log.d("rest - long  : ", coordonnees[1]);
        if (rayon != 0) {

            Location location = new Location("restaurant");
            location.setLatitude(Float.parseFloat(coordonnees[0]));
            location.setLongitude(Float.parseFloat(coordonnees[1]));

            if (actuel.distanceTo(location) < rayon * 1000) {
                if (l.getVisibility() == view.INVISIBLE)
                    l.setVisibility(view.VISIBLE);

                Log.d("distance ", Float.toString(actuel.distanceTo(location)));
                Log.d("distance ", Double.toString(rayon * 1000));
                String nom_r = cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_NOM));
                String adresse_r = cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_ADRESSE));
                String tarif_r = cursor.getString(cursor.getColumnIndex(Base.TARIF));
                String note_r = cursor.getString(cursor.getColumnIndex(Base.NOTE)) + "/5";
                nom.setText(nom_r);
                adresse.setText(adresse_r);
                tarif.setText(tarif_r);
                note.setText(note_r);
            }
        } else {
            if (l.getVisibility() == view.INVISIBLE)
                l.setVisibility(view.VISIBLE);
            String nom_r = cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_NOM));
            String adresse_r = cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_ADRESSE));
            String tarif_r = cursor.getString(cursor.getColumnIndex(Base.TARIF));
            String note_r = cursor.getString(cursor.getColumnIndex(Base.NOTE)) + "/5";
            nom.setText(nom_r);
            adresse.setText(adresse_r);
            tarif.setText(tarif_r);
            note.setText(note_r);
        }


        ImageButton deleteImageView = (ImageButton) view.findViewById(R.id.list_image);

        final int itemid = cursor.getInt(cursor.getColumnIndex("_id"));
        final Context contexte = context;
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmDialog(itemid, contexte);
            }
        });

    }
}
