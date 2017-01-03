package com.example.venier.miamfinder;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AjouterHoraires extends AppCompatActivity {
    private ContentResolver cr;
    Button annuler, valider, passer;
    CheckBox lun, mar, mer, jeu, ven, sam, dim;
    Spinner dejDeb, dejFin, dinDeb, dinFin ;
    String itemid ;
    ArrayList<String> fermeture, heureFin, heureDeb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_horaires);

        Intent i = getIntent();
        itemid = i.getStringExtra("id") ;
        annuler = (Button) findViewById(R.id.annuler);
        valider = (Button) findViewById(R.id.valider);
        passer = (Button) findViewById(R.id.passer);

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
                new Thread(new Runnable() {
                    public void run() {
                        ajoutJoursHeures();
                        runOnUiThread(new Runnable() {

                            public void run() {
                                Toast.makeText(AjouterHoraires.this, R.string.added_informations, Toast.LENGTH_SHORT).show();
                                finish() ;
                            }

                        });
                    }
                }).start();

            }
        });
        annuler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(Base.HORAIRES_ADD, "rowid = ?", new String[] { itemid }) ;
                finish() ;
            }
        });

        passer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
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


    public void horairesOuvertures(){
        heureDeb = new ArrayList<>();
        heureFin = new ArrayList<>();
        fermeture = new ArrayList<>() ;

        if(lun.isChecked())
            fermeture.add(lun.getText().toString());
        if(mar.isChecked())
            fermeture.add(mar.getText().toString());
        if(mer.isChecked())
            fermeture.add(mer.getText().toString());
        if(jeu.isChecked())
            fermeture.add(jeu.getText().toString());
        if(ven.isChecked())
            fermeture.add(ven.getText().toString());
        if(sam.isChecked())
            fermeture.add(sam.getText().toString());
        if(dim.isChecked())
            fermeture.add(dim.getText().toString());
    }

    public void ajoutJoursHeures(){
        String[] semaine = { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" } ;

        ContentValues rowJ = new ContentValues();
        cr = getContentResolver();
        String jours = "" ;

        for(String s : semaine){
            if(!fermeture.contains(s))
                jours += s + " " ;
        }

        rowJ.put(Base.ID_RESTAURANT, itemid);
        rowJ.put(Base.JOUR, jours);
        rowJ.put(Base.HORAIRES_DEB, dejDeb.getSelectedItem().toString());
        rowJ.put(Base.HORAIRES_FIN, dejFin.getSelectedItem().toString());
        cr.insert(Base.HORAIRES_ADD, rowJ);
        rowJ = new ContentValues() ;
        rowJ.put(Base.ID_RESTAURANT, itemid);
        rowJ.put(Base.JOUR, jours);
        rowJ.put(Base.HORAIRES_DEB, dinDeb.getSelectedItem().toString());
        rowJ.put(Base.HORAIRES_FIN, dinFin.getSelectedItem().toString());
        cr.insert(Base.HORAIRES_ADD, rowJ);

        }

}