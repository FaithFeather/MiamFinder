package com.example.venier.miamfinder;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Media extends Fragment {
    private ListView lv ;
    private ContentResolver cr ;
    private String itemid ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_media, container, false);
        itemid = getArguments().getString("itemid");


        cr = getActivity().getContentResolver() ;
        Cursor cursor = getComments() ;
        CustomCursorAdapterComments adapter = new CustomCursorAdapterComments(this.getContext(), cursor, true) ;;
        lv = (ListView) view.findViewById(R.id.item_comments);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

            }
        });
        Button media = (Button) view.findViewById(R.id.dialogAddComment) ;
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddComment();
            }
        });
        //cursor.close();
        return view ;
    }

    private void dialogAddComment() {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comment);
        final Button valider = (Button) dialog.findViewById(R.id.button_valider);
        final Button annuler = (Button) dialog.findViewById(R.id.button_cancel);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final EditText addComment = (EditText) dialog.findViewById(R.id.edit_text_comm);
                final String text = addComment.getText().toString() ;
                Log.d("Commentaire", text);
                if(text != "") {
                    cr = getContext().getContentResolver() ;
                    ContentValues values = new ContentValues() ;
                    values.put(Base.ID_RESTAURANT, itemid);
                    values.put(Base.DATE, getDateTime());
                    values.put(Base.TYPE_MEDIA_R, Base.COM_W);
                    values.put(Base.MEDIA, text) ;
                    cr.insert(Base.ADD_MEDIA, values);
                }
                dialog.dismiss();
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show() ;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        Log.d("Date", dateFormat.format(date)) ;
        return dateFormat.format(date);
    }

    private Cursor getComments() {
        Cursor cursor = cr.query(Base.MEDIA_SEARCH, new String[]{"rowid AS _id", Base.MEDIA, Base.DATE }, Base.ID_RESTAURANT + " = ? AND " + Base.TYPE_MEDIA_R + " = ?", new String[]{itemid, Base.COM_W}, null) ;
        return cursor ;
    }
}
