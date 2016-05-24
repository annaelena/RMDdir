package com.app.rmdir.rmdir;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {


    JSONArray NoteJsonArray ;
    ListView NoteListView;
    public View vView;

    private static IOStoreInterface mListener= new IOStoreInterface() {
        @Override
        public void getObj(int nezozio) {
        }

        @Override
        public void openNewNote() {

        }
    };



    public interface IOStoreInterface{
        void getObj(int negozio);

        void openNewNote();
    }


    static public NoteListFragment getIstance(JSONArray lispArray){
        Bundle vBundle =new Bundle();

        Log.d("stampa vString", lispArray.toString());
        vBundle.putString("arraylist",lispArray.toString());
        NoteListFragment st1= new NoteListFragment();
        st1.setArguments(vBundle);
        return st1;
    }


    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle vBundle = getArguments();
        String vString = vBundle.getString("arraylist");

        Log.d("stampa vString", vString);
        try {
            NoteJsonArray= new JSONArray(vString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vView = inflater.inflate(R.layout.fragment_note_list, container, false);
        NoteListView = (ListView) vView.findViewById(R.id.ListViewS);
        if(savedInstanceState!=null) NoteJsonArray= (JSONArray)savedInstanceState.get("List");
        FloatingActionButton openmenu= (FloatingActionButton) vView.findViewById(R.id.new_note);
        openmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        mListener.openNewNote();
            }
        });

        try {
            JSONObject mNotaJson;
            ArrayList<NoteClass> arrayOfNote = new ArrayList<NoteClass>();
            for (int i = 0; i <NoteJsonArray.length(); i++) {
                Log.d("debugging",NoteJsonArray.get(i).toString());
                mNotaJson = new JSONObject( NoteJsonArray.get(i).toString());
                arrayOfNote.add(new NoteClass(mNotaJson.getString("Titolo"),mNotaJson.getString("Testo_nota"),mNotaJson.getInt("Id_nota")));
            }
            NoteAdapter adapter;
            adapter = new NoteAdapter(vView.getContext(), arrayOfNote);
            NoteListView.setAdapter(adapter);

            NoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openNote(view.getId());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vView;
    }

    public static void openNote(int id) {
        mListener.getObj(id);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(getActivity() instanceof IOStoreInterface)
        {
            mListener = (IOStoreInterface) getActivity();
        }
    }
}
