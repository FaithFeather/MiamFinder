package com.example.venier.miamfinder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Olivia on 29/11/2015.
 */
public class CustomCursorAdapterComments extends CursorAdapter {


    public CustomCursorAdapterComments(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row =  LayoutInflater.from(context).inflate(R.layout.list_row_comm, parent, false);
        return row ;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView commentaires = (TextView) view.findViewById(R.id.list_commentaires) ;
        TextView commentaire_date = (TextView) view.findViewById(R.id.commentaire_date) ;
        commentaires.setText(cursor.getString(cursor.getColumnIndex(Base.MEDIA)));
        commentaire_date.setText(cursor.getString(cursor.getColumnIndex(Base.DATE)));
    }
}
