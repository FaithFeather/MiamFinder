package com.example.venier.miamfinder;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Restaurant_Onglets extends AppCompatActivity {

    ViewPager pager;
    CustomViewPager adapter;
    String id ;
    private ContentResolver cr;
    String tel, adresse, nom ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_restaurant__onglets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent restaurant = getIntent();
        id = restaurant.getStringExtra("itemid");
        Log.d("item id", id) ;

        adapter =  new CustomViewPager(getSupportFragmentManager(), id);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        cr = getContentResolver() ;
        Cursor cursor = cr.query(Base.ALL_INFORMATIONS, new String[]{ Base.RESTAURANT_NOM, Base.RESTAURANT_ADRESSE, Base.RESTAURANT_TELEPHONE }, null, new String[]{ id }, null) ;
        cursor.moveToNext() ;
        nom = cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_NOM)) ;
        tel = cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_TELEPHONE)) ;
        adresse = cursor.getString(cursor.getColumnIndex(Base.RESTAURANT_ADRESSE)) ;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant__onglets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true ;
            case R.id.call:
                makeACall() ;
                break ;
            case R.id.add:
                addContact() ;
                break ;
            case R.id.rating:
                rate() ;
                break ;
            case R.id.envoiesms:
                envoiesms();
                break;
            case R.id.modifier:
                modifier();
                break;
            case R.id.modifierhoraires:
                modifierhoraires();
                break;
            case R.id.addCal:
                addCalendar();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void rate() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        cr = getContentResolver() ;
        Cursor cursor = cr.query(Base.ALL_INFORMATIONS, new String[] { Base.NOTE }, null, new String[] {id}, null) ;
        TextView ratingBar_txt = (TextView) dialog.findViewById(R.id.ratingBar_txt) ;
        cursor.moveToNext() ;
        ratingBar_txt.setText("Note actuelle : " + cursor.getString(cursor.getColumnIndex(Base.NOTE)) + "/5.");
        cursor.close();
        final Button dialog_btn = (Button) dialog.findViewById(R.id.button_dialog);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_dialog);
                float note = ratingBar.getRating();
                Log.d("note", Float.toString(note));
                noter(note);
                dialog.dismiss();
            }
        });
        dialog.show() ;
    }

    public void noter(float note){
        cr = getContentResolver() ;
        ContentValues values = new ContentValues() ;
        Cursor cursor = cr.query(Base.ALL_INFORMATIONS, new String[]{Base.NOTE}, null, new String[] {id}, null) ;
        cursor.moveToNext() ;
        Float moyenne = (Float.parseFloat(cursor.getString(cursor.getColumnIndex(Base.NOTE))) + note) / 2;
        Log.d("MOYENNE", Float.toString(moyenne)) ;
        values.put(Base.NOTE, Float.toString(moyenne));
        cr.update(Base.ADD_RESTAU, values, "rowid = " + id, null);
        cursor.close();
    }

    private void addContact() {
        Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel:" + tel));
        intent.putExtra(ContactsContract.Intents.Insert.NAME, nom) ;
        intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
        startActivity(intent);
    }

    private void makeACall() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ tel));
        startActivity(intent);
    }

    public void envoiesms(){
        Uri smsUri = Uri.parse("tel:"+ tel);
        Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
        intent.putExtra("sms_body", "Bonjour. Voici un très bon restaurant à tester absolument ! " + nom + " - " + adresse);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void modifierhoraires(){
        Intent intent = new Intent(Restaurant_Onglets.this, ModifierHoraires.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    public void modifier(){
        Intent intent = new Intent(Restaurant_Onglets.this, Modifier.class);
        Log.d("ICI : ", " " + id);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void addCalendar(){
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, "Restaurant - " + nom)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Repas")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, nom + " - " + adresse)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY) ;
        startActivity(intent);
    }
}