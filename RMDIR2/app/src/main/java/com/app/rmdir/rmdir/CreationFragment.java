package com.app.rmdir.rmdir;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link creationFragmentInterface} interface
 * to handle interaction events.
 */
public class CreationFragment extends Fragment {
    private MediaRecorder registratore = null;
    private MediaPlayer mp = null;
    private ImageButton insert;
    private boolean incorso = false;
    private Calendar cal = Calendar.getInstance();
    private static String filename = null;
    private static String filenameTmp = null;

    private creationFragmentInterface mListener;
    private boolean recording =false;
    public CreationFragment() {}
    String mNickname, mTesto, mTitolo, mId=null;

    public static CreationFragment getIstance(String nickname) {
        CreationFragment creationFragment =new CreationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nickname",nickname);
        creationFragment.setArguments(bundle);
        return creationFragment;
    }
    public static CreationFragment getIstance(String nickname,Bundle vBundle1) {
        CreationFragment creationFragment =new CreationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nickname",nickname);
        bundle.putString("testo",vBundle1.getString("testo"));
        bundle.putString("id",vBundle1.getString("id"));
        bundle.putString("titolo",vBundle1.getString("titolo"));
        creationFragment.setArguments(bundle);
        return creationFragment;
    }

    private AHBottomNavigation bottomNavigation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_creation, container, false);
        final EditText titolo = (EditText)view.findViewById(R.id.input_titolo);
        final EditText testo =(EditText)view.findViewById(R.id.testo);

        filename = Environment.getExternalStorageDirectory().getAbsolutePath();
        filename += "/reg_"+cal.getTime().toString().replace(" ","_").replace(":","_").substring(0,19)+getArguments().getString("nickname")+".3gp";
        filenameTmp = "/reg_"+cal.getTime().toString().replace(" ","_").replace(":","_").substring(0,19)+getArguments().getString("nickname")+".3gp";
        Log.d("filename",filename);


        insert = (ImageButton)view.findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mId==null){
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("nickname",mNickname );
                    requestParams.add("Testo_nota",testo.getText().toString());
                    requestParams.add("Title",titolo.getText().toString());
                    if(filename!=null)
                    {
                        requestParams.add("Url_media",filenameTmp);
                    }
                    asyncHttpClient.post("http://rmdir.altervista.org/InsertNote.php", requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            try {
                                JSONObject jsonObject = new JSONObject(new String(responseBody));
                                Log.d("Prova", jsonObject.toString());
                                if ((jsonObject.getBoolean("success"))) {
                                    Log.d("caricamento nota", "ok");
                                    mListener.onCreationInteraction(filenameTmp);
                                } else {
                                    Toast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), "Error on jsonObject", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getActivity(), "Error on connection", Toast.LENGTH_LONG).show();

                        }
                    });





                }
                else
                {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("id",mId );
                    requestParams.add("nickname",mNickname);
                    requestParams.add("Testo_nota",testo.getText().toString());
                    requestParams.add("Title",titolo.getText().toString());
                    if(filename!=null)
                    {
                        requestParams.add("Url_media",filenameTmp);
                    }
                    Log.d("debugg", mId+mNickname+testo.getText().toString()+titolo.getText().toString());
                    asyncHttpClient.post("http://rmdir.altervista.org/updateNote.php", requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            try {
                                JSONObject jsonObject = new JSONObject(new String(responseBody));
                                Log.d("Prova", jsonObject.toString());
                                if ((jsonObject.getBoolean("success"))) {
                                    Log.d("caricamento nota", "ok");
                                    mListener.onCreationInteraction(filenameTmp);
                                } else {
                                    Toast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), "Error on jsonObject", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getActivity(), "Error on connection", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

        });

        bottomNavigation = (AHBottomNavigation) view.findViewById(R.id.bottom_navigation);
        //AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_maps_place, R.color.color_tab_1);

        AHBottomNavigationItem record = new AHBottomNavigationItem("",R.drawable.rec);
        AHBottomNavigationItem camera = new AHBottomNavigationItem("",R.drawable.camera);
        bottomNavigation.setAccentColor(R.color.red);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#60b8fd"));

        bottomNavigation.setUseElevation(false);
        bottomNavigation.addItem(camera);
        bottomNavigation.addItem(record);

        bottomNavigation.setCurrentItem(-1);
        if(getArguments().getString("id")!=null)
        {

            mId=getArguments().getString("id");
            mTesto=getArguments().getString("testo");
            mTitolo=getArguments().getString("titolo");
        }
        mNickname=getArguments().getString("nickname");
        titolo.setText(mTitolo);
        testo.setText(mTesto);




        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    if(recording){
                        registratore.stop();
                        registratore.release();
                        registratore = null;
                        recording=false;
                        bottomNavigation.removeAllItems();
                        AHBottomNavigationItem record = new AHBottomNavigationItem("",R.drawable.rec);
                        AHBottomNavigationItem camera = new AHBottomNavigationItem("",R.drawable.camera);
                        AHBottomNavigationItem listen = new AHBottomNavigationItem("", R.drawable.play, R.color.red);
                        AHBottomNavigationItem delete = new AHBottomNavigationItem("", R.drawable.delete, R.color.red);
                        bottomNavigation.addItem(camera);
                        bottomNavigation.addItem(record);
                        bottomNavigation.addItem(listen);
                        bottomNavigation.addItem(delete);
                        bottomNavigation.setCurrentItem(-1);
                    }

                    //implement photo

                }
                if (position == 1) {
                    if(!recording){
                        recording=true;
                        registratore = new MediaRecorder();
                        registratore.setAudioSource(MediaRecorder.AudioSource.MIC);
                        registratore.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        registratore.setOutputFile(filename);
                        registratore.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        try
                        {
                            registratore.prepare();
                        }
                        catch (IOException e)
                        {
                            Toast.makeText(getContext(), "Errore durante la registrazione" , Toast.LENGTH_LONG).show();
                        }
                        registratore.start();
                        bottomNavigation.removeAllItems();
                        AHBottomNavigationItem record = new AHBottomNavigationItem("",R.drawable.rec);
                        bottomNavigation.addItem(record);


                    }
                    bottomNavigation.setCurrentItem(-1);
                    bottomNavigation.setBackgroundColor(Color.parseColor("#d43337"));


                }
                if (position == 2) {
                    if(!incorso){
                        mp = new MediaPlayer();
                        try
                        {
                            Log.d("Listening","true");
                            mp.setDataSource(filename);
                            mp.prepare();
                            mp.start();
                        }
                        catch (IOException e)
                        {
                            Toast.makeText(getContext(), "Errore durante la riproduzione" , Toast.LENGTH_LONG).show();
                        }
                        incorso=true;
                    }else{
                        Log.d("listening","true");
                        mp.release();
                        mp = null;
                        incorso=false;
                        bottomNavigation.setCurrentItem(-1);
                    }



                }
                if (position == 3) {
                    if(incorso){
                        Log.d("listening","true");
                        mp.release();
                        mp = null;
                        incorso=false;
                    }
                    File file = new File(filename);
                    file.delete();
                    bottomNavigation.removeItemAtIndex(2);
                    bottomNavigation.removeItemAtIndex(2);
                    Toast.makeText(getActivity(), "Recording removed", Toast.LENGTH_LONG).show();
                }
            }
        });
        bottomNavigation.setCurrentItem(-1);


        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof creationFragmentInterface) {
            mListener = (creationFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (registratore != null) {
            registratore.release();
            registratore = null;
        }

        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface creationFragmentInterface {
        // TODO: Update argument type and name
        void onCreationInteraction(String aMedia);
    }
}
