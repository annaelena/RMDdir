package com.app.rmdir.rmdir;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Random;

public class NoteAdapter extends ArrayAdapter<NoteClass> {
    public NoteAdapter(Context context, ArrayList<NoteClass> mNote) {
        super(context, 0, mNote);

    }
    NoteClass mNote;
    View convertedView;
    int id;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        mNote = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_layout, parent, false);
        }
        convertedView=convertView;
        // Lookup view for data population
        TextView mTitle = (TextView) convertView.findViewById(R.id.post_name);
        // Populate the data into the template view using the data object
        mTitle.setText(mNote.titolo);
        TextView mDescr = (TextView) convertView.findViewById(R.id.post_descr);
        // Populate the data into the template view using the data object
        if(mNote.nota.length()>20)
            mDescr.setText(mNote.nota.substring(0,20).replace("\n"," ")+"...");
        else
            mDescr.setText(mNote.nota.replace("\n"," "));
        id=mNote.id;
        String Iniziale = ""+mNote.titolo.charAt(0);
        Iniziale=Iniziale.toUpperCase();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Iniziale, getMatColor("500",mNote.titolo));
        convertView.setId(mNote.id);
        ImageView image = (ImageView) convertView.findViewById(R.id.image_view);
        image.setImageDrawable(drawable);
        return convertView;
    }
    public int getMatColor(String typeColor, String aSeed)
    {
        int returnColor = Color.BLACK;
        int arrayId = convertedView.getResources().getIdentifier("mdcolor_" + typeColor, "array", getContext().getPackageName());

        if (arrayId != 0)
        {
            int mSeed=0;
            TypedArray colors = convertedView.getResources().obtainTypedArray(arrayId);
            for(int i=0; i<3 ; i++)
            {
                mSeed = mSeed +((int) aSeed.charAt(i)/5 );
            }
            Random generator = new Random(mSeed);
            int random = generator.nextInt(18);
            Log.d("una cosa",(""+random)+" "+mSeed+" "+aSeed);
            returnColor = colors.getColor(random, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }


}