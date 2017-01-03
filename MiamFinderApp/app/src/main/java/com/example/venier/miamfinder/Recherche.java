package com.example.venier.miamfinder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Recherche extends AppCompatActivity {
    private ArrayList<String> choicesSelected ;
    private boolean all = false ;
    private String criteres_nom_adresse = "";
    private ArrayList<String> criteres_cuisine ;
    private String criteres_heure = "" ;
    private String criteres_jour = "";
    private String criteres_tarif = "" ;
    private String criteres_distance = "" ;
    private String criteres_note = "" ;

    private RadioGroup radioTarifGroup ;
    private RadioButton tarif_1 ;
    private RadioButton tarif_2 ;
    private RadioButton tarif_3 ;

    private EditText et_ou ;
    private TextView seek ;

    private CheckBox ck_1 ;
    private CheckBox ck_2 ;
    private CheckBox ck_3 ;
    private CheckBox ck_4 ;

    private SeekBar sb ;
    private RatingBar rb ;
    private TextView rayon ;

    private Spinner jour ;
    private Spinner heure ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        ck_1 = (CheckBox) findViewById(R.id.checkBox);
        ck_2 = (CheckBox) findViewById(R.id.checkBox2);
        ck_3 = (CheckBox) findViewById(R.id.checkBox3);
        ck_4 = (CheckBox) findViewById(R.id.checkBox4);
        et_ou = (EditText) findViewById(R.id.editText6);

        radioTarifGroup = (RadioGroup) findViewById(R.id.radioTarifGroup);
        tarif_1 = (RadioButton) findViewById(R.id.radioButton);
        tarif_2 = (RadioButton) findViewById(R.id.radioButton1);
        tarif_3 = (RadioButton) findViewById(R.id.radioButton2);

        ArrayAdapter<CharSequence> tarif_adapter = ArrayAdapter.createFromResource(this, R.array.tarifs, android.R.layout.simple_spinner_item);
        tarif_1.setText(tarif_adapter.getItem(0).toString());
        tarif_2.setText(tarif_adapter.getItem(1).toString());
        tarif_3.setText(tarif_adapter.getItem(2).toString());
        rayon = (TextView) findViewById(R.id.textView9);
        jour = (Spinner) findViewById(R.id.spinner);
        heure = (Spinner) findViewById(R.id.spinner2);

        rb = (RatingBar) findViewById(R.id.ratingBar) ;
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                seek = (TextView) findViewById(R.id.ratingBar_txt) ;
                if(rating == 0)
                    seek.setText("Pas de note minimum");
                else
                    seek.setText("Note minimale de : " + rating + "/5") ;
            }
        });

        sb = (SeekBar) findViewById(R.id.seekBar);
        sb.setMax(50);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getProgress() == 0)
                    rayon.setText("Pas de rayon de recherche");
                else
                    rayon.setText("Rayon de : " + seekBar.getProgress() + " kilomètres.");
            }
        });
    }

    public void seekbarPosition(){
        String s = rayon.getText().toString() ;
        s += Integer.toString(sb.getProgress()) ;
        rayon.setText(s) ;
    }

    public void launchSearch(){
        Intent result_search ;
        result_search = new Intent(this, ResultatsRecherche.class) ;
        result_search.putExtra("all", all) ;
        result_search.putExtra("critere_nom_adresse", criteres_nom_adresse) ;
        result_search.putStringArrayListExtra("critere_cuisine", criteres_cuisine) ;
        result_search.putExtra("criteres_heure", criteres_heure) ;
        result_search.putExtra("criteres_jour", criteres_jour) ;
        result_search.putExtra("criteres_tarif", criteres_tarif) ;
        result_search.putExtra("criteres_distance", criteres_distance) ;
        result_search.putExtra("criteres_note", criteres_note) ;
        startActivity(result_search) ;
    }

    public String getInformations(){
        String result = "" ;
        criteres_nom_adresse = null;
        criteres_heure = null ;
        criteres_jour = null;
        criteres_tarif = null ;
        criteres_distance = null ;
        criteres_note = null ;
        criteres_cuisine = new ArrayList() ;

        if(sb.getProgress() != 0){
            result += "<b>"+ "Rayon de recherche : " +"</b>" ;
            String str = rayon.getText().toString().replaceAll("[a-zA-zè.:\\s]","") ;
            result += str + "km.";
            criteres_distance = str ;
        }

            /* RECHERCHE PAR NOM / ADRESSE / VILLE */
        if(et_ou.getText() != null && et_ou.length() > 0){
            result += "<b>"+ "Où : " +"</b>" ;
            result += et_ou.getText().toString() ;
            criteres_nom_adresse = et_ou.getText().toString() ;
        }

            /* RECHERCHE PAR TYPE DE CUISINE */
        if(ck_1.isChecked() | ck_2.isChecked() | ck_3.isChecked() | ck_4.isChecked()) {
            result += "<br/><b>" + "Type de cuisine : " + "</b>";
            if (ck_1.isChecked()) {
                criteres_cuisine.add(ck_1.getText().toString());
                result += ck_1.getText().toString() + " ";
            }
            if (ck_2.isChecked()) {
                criteres_cuisine.add(ck_2.getText().toString());
                result += ck_2.getText().toString() + " ";
            }
            if (ck_3.isChecked()) {
                criteres_cuisine.add(ck_3.getText().toString());
                result += ck_3.getText().toString() + " ";
            }
            if (ck_4.isChecked()){
                criteres_cuisine.add(ck_4.getText().toString());
                result += ck_4.getText().toString() + " ";
            }
        }
        if (choicesSelected != null) {
            if(criteres_cuisine.size() == 0)
                result += "<br/><b>" + "Type de cuisine : " + "</b>";
            for (int i = 0; i < choicesSelected.size(); i++){
                if(!criteres_cuisine.contains(choicesSelected.get(i))) {
                    criteres_cuisine.add(choicesSelected.get(i));
                    result += choicesSelected.get(i) + " ";
                }
            }
        }

            /* RECHERCHE PAR TARIF */

        if(tarif_1.isChecked() | tarif_2.isChecked() | tarif_3.isChecked()) {
            int selectedId = radioTarifGroup.getCheckedRadioButtonId();
            RadioButton tarif = (RadioButton) findViewById(selectedId);
            result += "<br/><b>" + "Tarif : " + "</b>";
            result += tarif.getText().toString();
            criteres_tarif = tarif.getText().toString() ;
        }

            /* RECHERCHE PAR JOURS A FAIRE */


        String spinner_text_jour = jour.getSelectedItem().toString();
        if(!spinner_text_jour.equals("Jours")){
            result += "<br/><b>" + "Jour : " + "</b>";
            result += spinner_text_jour ;
            criteres_jour = spinner_text_jour ;
        }

        String spinner_text_heure = heure.getSelectedItem().toString();
        if(!spinner_text_heure.equals("Heures")){
            result += "<br/><b>" + "Heure : " + "</b>";
            result += spinner_text_heure ;
            criteres_heure = heure.getSelectedItem().toString();
        }
            /* RECHERCHE PAR NOTE A FAIRE */

        if(rb.getRating() != 0){
            result += "<br/><b>"+ "Note minimale : " +"</b>" ;
            String str = Float.toString(rb.getRating()) ;
            result += str + "/5.";
            criteres_note = str ;
        }

        if(result.length() < 1) {
            all = true ;
            result = "Chercher tous les restaurants ?";
        }
        return result ;
    }

    public void confirmDialog(){
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmer la recherche") ;
        builder.setMessage(Html.fromHtml(getInformations()));
        builder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                launchSearch() ;
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        }) ;
        dialog = builder.create();
        dialog.show();
    }

    public void seeMore(View view){
        Dialog dialog;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_cuisine, android.R.layout.simple_spinner_item) ;
        final String[] choices = new String[adapter.getCount()];

        for(int i = 0 ; i < choices.length ; i++)
            choices[i] = adapter.getItem(i).toString() ;
        choicesSelected = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tous les types de cuisine : ");
        builder.setMultiChoiceItems(choices, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId, boolean isChecked) {
                        if (isChecked) {
                            choicesSelected.add(choices[selectedItemId]);
                        } else if (choicesSelected.contains(choices[selectedItemId])) {
                            choicesSelected.remove(choices[selectedItemId]);
                        }
                    }
                })
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public  void reloadPage() {
        et_ou.getText().clear();
        rayon.setText("Pas de rayon de recherche.");
        if(ck_1.isSelected() || ck_1.isChecked()) ck_1.toggle();
        if(ck_2.isSelected() || ck_2.isChecked()) ck_2.toggle();
        if(ck_3.isSelected() || ck_3.isChecked()) ck_3.toggle();
        if(ck_4.isSelected() || ck_4.isChecked()) ck_4.toggle();
        radioTarifGroup.clearCheck();
        jour.setSelection(0);
        heure.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recherche, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_launch:
                confirmDialog() ;
                break;
            case R.id.reload_page:
                reloadPage() ;
                break ;
        }

        return super.onOptionsItemSelected(item);
    }


}
