package com.example.venier.miamfinder;

/**
 * Created by Olivia on 27/11/2015.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class CustomViewPager extends FragmentStatePagerAdapter {
    String id ;

    public CustomViewPager(FragmentManager fm, String id) {
        super(fm);
        this.id = id ;
        Log.d("item id", id) ;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("itemid", id);
        if(position == 0){
            Informations_generales tab1 = new Informations_generales();
            tab1.setArguments(bundle);
            return tab1;
        } else if(position == 1){
            Horaires_Tarifs tab2 = new Horaires_Tarifs();
            tab2.setArguments(bundle);
            return tab2;
        } else if(position == 2){
            Media tab3 = new Media();
            tab3.setArguments(bundle);
            return tab3;
        }
        Informations_generales tab1 = new Informations_generales();
        tab1.setArguments(bundle);
        return tab1 ;
    }

    @Override
    public int getCount() {
        return 3;
    }
}