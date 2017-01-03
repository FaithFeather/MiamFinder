package com.example.venier.miamfinder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.ArrayList;

public class ModifierHoraires extends AppCompatActivity {
    Button valider, annuler;
    CheckBox lun, mar, mer, jeu, ven, sam, dim;
    ArrayList<String> fermeture ;
    private ContentResolver cr;
    Spinner dejDeb, dejFin, dinDeb, dinFin;
    String id ;


    private OnClickListener clickAnnuler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent b = new Intent(ModifierHoraires.this, HomePage.class);
            startActivity(b);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_horaires);

        Intent restaurant = getIntent();
        id = restaurant.getStringExtra("id");


        annuler = (Button) findViewById(R.id.annuler);
        valider = (Button) findViewById(R.id.valider);


        lun = (CheckBox) findViewById(R.id.lundi);
        mar = (CheckBox) findViewById(R.id.mardi);
        mer = (CheckBox) findViewById(R.id.mercredi);
        jeu = (CheckBox) findViewById(R.id.jeudi);
        ven = (CheckBox) findViewById(R.id.vendredi);
        sam = (CheckBox) findViewById(R.id.samedi);
        dim = (CheckBox) findViewById(R.id.dimanche);

        dejDeb = (Spinner) findViewById(R.id.spindebdej);
        dejFin = (Spinner) findViewById(R.id.spinfindej);
        dinDeb = (Spinner) findViewById(R.id.spindebdin);
        dinFin = (Spinner) findViewById(R.id.spinfindin);


        valider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                horairesOuvertures();
                horairesJours() ;
                Intent intent = new Intent(ModifierHoraires.this, HomePage.class);
                startActivity(intent);
            }
        });

        annuler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });
    }

    public void horairesJours() {
        String[] semaine = { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" } ;
        Cursor cursor = getContentResolver().query(Base.ID_HORAIRES, new String[] { "rowid" }, Base.ID_RESTAURANT + " = ?", new String[] { id }, null) ;
        cursor.moveToNext() ;

        ContentValues rowJ = new ContentValues();
        cr = getContentResolver();
        String jours = "" ;

        for(String s : semaine){
            if(!fermeture.contains(s))
                jours += s + " " ;
        }
        if(cursor.getCount() != 0) {
            rowJ.put(Base.ID_RESTAURANT, id);
            rowJ.put(Base.JOUR, jours);
            if (!(dejDeb.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_DEB, dejDeb.getSelectedItem().toString());
            if (!(dejFin.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_FIN, dejFin.getSelectedItem().toString());
            cr.update(Base.HORAIRES_ADD, rowJ, "rowid = ?", new String[]{cursor.getString(cursor.getColumnIndex("rowid"))});

            cursor.moveToNext();
            rowJ.put(Base.ID_RESTAURANT, id);
            rowJ.put(Base.JOUR, jours);
            if (!(dinDeb.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_DEB, dinDeb.getSelectedItem().toString());
            if (!(dinFin.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_FIN, dinFin.getSelectedItem().toString());
            cr.update(Base.HORAIRES_ADD, rowJ, "rowid = ?", new String[]{cursor.getString(cursor.getColumnIndex("rowid"))});
        }
        else {
            rowJ.put(Base.ID_RESTAURANT, id);
            rowJ.put(Base.JOUR, jours);
            if (!(dejDeb.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_DEB, dejDeb.getSelectedItem().toString());
            if (!(dejFin.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_FIN, dejFin.getSelectedItem().toString());
            cr.insert(Base.HORAIRES_ADD, rowJ);
            rowJ = new ContentValues() ;
            rowJ.put(Base.ID_RESTAURANT, id);
            rowJ.put(Base.JOUR, jours);
            if (!(dinDeb.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_DEB, dinDeb.getSelectedItem().toString());
            if (!(dinFin.getSelectedItem().toString()).equals("Heures"))
                rowJ.put(Base.HORAIRES_FIN, dinFin.getSelectedItem().toString());
            cr.insert(Base.HORAIRES_ADD, rowJ);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modifier, menu);
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

    public void horairesOuvertures() {
        fermeture = new ArrayList<>();

        if (lun.isChecked())
            fermeture.add(lun.getText().toString());
        if (mar.isChecked())
            fermeture.add(mar.getText().toString());
        if (mer.isChecked())
            fermeture.add(mer.getText().toString());
        if (jeu.isChecked())
            fermeture.add(jeu.getText().toString());
        if (ven.isChecked())
            fermeture.add(ven.getText().toString());
        if (sam.isChecked())
            fermeture.add(sam.getText().toString());
        if (dim.isChecked())
            fermeture.add(dim.getText().toString());
    }

}