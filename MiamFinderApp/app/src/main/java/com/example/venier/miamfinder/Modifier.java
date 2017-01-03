package com.example.venier.miamfinder;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Modifier extends AppCompatActivity {
    private ContentResolver cr;
    Button valider, annuler, typecuisine;
    EditText nom, tel, web;
    RadioGroup btnsLivr, btnsAccess;
    RadioButton ouiLivr, nonLivr, ouiAccess, nonAccess;
    Spinner tarifRestau;
    String nomTxt = "", webTxt = "", telTxt = "", livrTxt = "", accessTxt = "", tarifTxt = "", cuisineTxt = "";
    String id="";
    ArrayList<String> cuisine;
    ArrayList<String> selectedItems;

    private OnClickListener clickAnnuler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent b = new Intent(Modifier.this, HomePage.class);
            startActivity(b);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier);
        Intent restaurant = getIntent();
        id = restaurant.getStringExtra("id");

        nom = (EditText)findViewById(R.id.nom);
        tel = (EditText)findViewById(R.id.phone);
        web = (EditText)findViewById(R.id.siteweb);
        btnsAccess = (RadioGroup)findViewById(R.id.radioGpeAccessible);
        btnsLivr = (RadioGroup)findViewById(R.id.radioGpeLivraison);
        ouiAccess = (RadioButton)findViewById(R.id.radioBtnA1);
        nonAccess = (RadioButton)findViewById(R.id.radioBtnA2);
        ouiLivr = (RadioButton)findViewById(R.id.radioBtnL1);
        nonLivr = (RadioButton)findViewById(R.id.radioBtnL2);
        tarifRestau = (Spinner) findViewById(R.id.spinTarif);
        valider = (Button) findViewById(R.id.valider);
        annuler = (Button) findViewById(R.id.annuler);
        typecuisine = (Button) findViewById(R.id.typecuisine);

        valider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nomTxt = nom.getText().toString();
                telTxt = tel.getText().toString();
                webTxt = web.getText().toString();
                tarifTxt = tarifRestau.getSelectedItem().toString();

                cuisine = new ArrayList<String>();
                if(selectedItems != null){
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

                Toast.makeText(Modifier.this, "Le restaurant a été modifié", Toast.LENGTH_SHORT).show();
                modification();
                Intent intent = new Intent(Modifier.this, HomePage.class);
                startActivity(intent);
            }

        });
        annuler.setOnClickListener(clickAnnuler);

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

    public void modification(){
        cr = getContentResolver();
        ContentValues values = new ContentValues();

        Log.d("TEEESTT : ", " " + id);

        if(nomTxt.length() > 0)
            values.put(Base.RESTAURANT_NOM, nomTxt);
        if(telTxt.length() > 0){

            StringBuffer sb = new StringBuffer() ;
            for(int i = 0; i < telTxt.length(); i += 2) {
                sb.append(telTxt.substring(i, i + 2));
                sb.append(" ");
            }
            values.put(Base.RESTAURANT_TELEPHONE, sb.toString().trim());
        }
        if(webTxt.length() > 0)
            values.put(Base.RESTAURANT_SITE, webTxt);
        if(cuisineTxt.length() > 0)
            values.put(Base.TYPE_CUISINE, cuisineTxt);
        if(livrTxt.length() > 0)
           values.put(Base.LIVRAISONS, livrTxt);
        if(accessTxt.length() > 0)
            values.put(Base.ACCES_HANDICAPE, accessTxt);
        if(tarifTxt.length() > 0)
            values.put(Base.TARIF, tarifTxt);
        cr.update(Base.ADD_RESTAU, values, "rowid = ?", new String[]{id});
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
