package com.example.venier.miamfinder;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.media.Rating;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Ajouter extends AppCompatActivity  {

    private ContentResolver cr;
    private Button annuler, suivant;
    private ArrayList<String> selectedItems;
    EditText nom, num, adr, ville, codepostal, tel, web;
    String nomTxt, numTxt, adrTxt, villeTxt, codepostalTxt, telTxt, webTxt;
    String adr_loc;
    double lat, lgt;
    String itemid ;
    RadioGroup btnsLivr, btnsAccess;
    RadioButton ouiLivr, nonLivr, ouiAccess, nonAccess;
    Spinner tarifRestau;
    RatingBar rbNote;
    String rbNoteTxt, tarifTxt, livrTxt, accessTxt;
    private ArrayList<String> cuisine;
    String cuisineTxt = "";


    private OnClickListener clickAnnuler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent b = new Intent(Ajouter.this, HomePage.class);
            startActivity(b);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);

        nom = (EditText) findViewById(R.id.nom);
        num = (EditText) findViewById(R.id.numero);
        adr = (EditText) findViewById(R.id.adresse);
        ville = (EditText) findViewById(R.id.ville);
        codepostal = (EditText) findViewById(R.id.cp);
        tel = (EditText) findViewById(R.id.phone);
        web = (EditText) findViewById(R.id.siteweb);
        annuler = (Button) findViewById(R.id.annuler);
        suivant = (Button) findViewById(R.id.valider);
        btnsLivr = (RadioGroup) findViewById(R.id.radioGpeLivraison);
        ouiLivr = (RadioButton) findViewById(R.id.radioBtnL1);
        nonLivr = (RadioButton) findViewById(R.id.radioBtnL2);
        btnsAccess = (RadioGroup) findViewById(R.id.radioGpeAccessible);
        ouiAccess = (RadioButton) findViewById(R.id.radioBtnA1);
        nonAccess = (RadioButton) findViewById(R.id.radioBtnA2);
        tarifRestau = (Spinner) findViewById(R.id.spinTarif);
        rbNote = (RatingBar) findViewById(R.id.ratingBar1);


        suivant.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nomTxt = nom.getText().toString();
                numTxt = num.getText().toString();

                adrTxt = adr.getText().toString();
                villeTxt = ville.getText().toString();
                codepostalTxt = codepostal.getText().toString();
                telTxt = tel.getText().toString();
                webTxt = web.getText().toString();
                tarifTxt = tarifRestau.getSelectedItem().toString();
                rbNoteTxt = String.valueOf(rbNote.getRating());

                cuisine = new ArrayList<String>();
                if(selectedItems != null) {
                    for (int i = 0; i < selectedItems.size(); i++) {
                        cuisine.add(selectedItems.get(i));
                        cuisineTxt += selectedItems.get(i) + " ";
                    }
                }
                if (ouiLivr.isChecked() || nonLivr.isChecked()) {
                    int selectedId = btnsLivr.getCheckedRadioButtonId();
                    RadioButton l = (RadioButton) findViewById(selectedId);
                    livrTxt = l.getText().toString();
                }

                if (ouiAccess.isChecked() || nonAccess.isChecked()) {
                    int selectedId = btnsAccess.getCheckedRadioButtonId();
                    RadioButton a = (RadioButton) findViewById(selectedId);
                    accessTxt = a.getText().toString();
                }

                if (nomTxt.equals("") || numTxt.equals("") || adrTxt.equals("") || villeTxt.equals("") || codepostalTxt.equals("")) {
                    Toast.makeText(Ajouter.this, R.string.case_empty, Toast.LENGTH_SHORT).show();
                    suivant.setEnabled(true);
                } else {
                    if(adresseLocalisation()) {
                        new Thread(new Runnable() {
                            public void run() {
                                ajout();
                                runOnUiThread(new Runnable() {

                                    public void run() {
                                        Toast.makeText(Ajouter.this, R.string.added_informations, Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(Ajouter.this, AjouterHoraires.class);
                                        i.putExtra("id", itemid);
                                        startActivity(i);
                                        finish() ;
                                    }

                                });
                            }
                        }).start();
                    }
                }
            }
        });

        annuler.setOnClickListener(clickAnnuler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_ajouter, menu);
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

    public boolean adresseLocalisation(){
        Geocoder gc = new Geocoder(getBaseContext());
        try{
            adr_loc = numTxt + ", " + adrTxt + ", " + codepostalTxt + " " + villeTxt;
            Log.d("adresse", adr_loc) ;
            List<Address> list = gc.getFromLocationName(adr_loc, 1);
            if(list.size() !=0) {
                Address address = list.get(0);
                lat = address.getLatitude();
                lgt = address.getLongitude();
                Log.d("Add - Latitude", Double.toString(lat));
                Log.d("Add - Longitude", Double.toString(lgt));
                return true ;
            } else {
                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(Ajouter.this, R.string.adresse_fausse, Toast.LENGTH_SHORT).show();
                    }

                });
                return false ;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false ;
    }

    public void ajout(){
        ContentValues row = new ContentValues() ;
        cr = getContentResolver() ;
        StringBuffer sb = new StringBuffer() ;
        for(int i = 0; i < telTxt.length(); i += 2) {
            sb.append(telTxt.substring(i, i + 2));
            sb.append(" ");
        }
        row.put(Base.RESTAURANT_NOM, nomTxt) ;
        row.put(Base.RESTAURANT_ADRESSE, adr_loc);
        row.put(Base.COORDONNEES, lat+ " "+lgt);
        row.put(Base.RESTAURANT_TELEPHONE, sb.toString().trim());
        row.put(Base.RESTAURANT_SITE, webTxt) ;
        row.put(Base.TARIF, tarifTxt);
        row.put(Base.TYPE_CUISINE, cuisineTxt);
        row.put(Base.NOTE, rbNoteTxt) ;
        row.put(Base.ACCES_HANDICAPE, accessTxt) ;
        row.put(Base.LIVRAISONS, livrTxt);

        cr.insert(Base.ADD_RESTAU, row) ;
        cr = getContentResolver() ;
        Cursor cursor = cr.query(Base.LAST_ID, new String[]{ "rowid" }, null, null, null) ;
        if(cursor != null)
            cursor.moveToNext() ;
        itemid = cursor.getString(cursor.getColumnIndex("rowid"));
    }

    public void onClickTypeCuisine(View v) {
        Dialog dialog;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_cuisine, android.R.layout.simple_spinner_item) ;
        final String[] choices = new String[adapter.getCount()];
        for(int i = 0 ; i < choices.length ; i++)
            choices[i] = adapter.getItem(i).toString() ;
        selectedItems =  new ArrayList();
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Sélectionner le type de cuisine");
        alertbuilder.setMultiChoiceItems(choices, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedItems.add(choices[which]);
                } else if (selectedItems.add(choices[which])) {
                    selectedItems.remove(choices[which]);
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //code ajouter dans la liste
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //code annuler
            }
        });
        dialog = alertbuilder.create();
        dialog.show();
    }
}