package com.app.rmdir.rmdir;


import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String NOTE = "note";
    private MediaPlayer mp = null;
    static String filename=null;

    private static final String NICKNAME = "nickname";

    public interface IODetailFragment{
        void onChoice( String aChoice,Bundle vBundle1);
        public void onError();
    }

    AHBottomNavigationItem media;
    ImageButton btn1;
    static TextView textView1;
    static TextView textView2;
    static Bundle vBundle1=new Bundle();

    static String notaSelected, nickname;

    public static DetailFragment getInstance(String note, String nickname){

        DetailFragment detailFragment = new DetailFragment();
        Bundle  bundle = new Bundle();
        bundle.putString(NOTE,note);
        bundle.putString(NICKNAME,nickname);
        detailFragment.setArguments(bundle);
        return detailFragment;




    }

    public IODetailFragment mListener = new IODetailFragment() {
        @Override
        public void onChoice(String aChoice,Bundle vBundle1) {

        }
        public void onError() {

        }
    };


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle bundle = getArguments();
        notaSelected = bundle.getString(NOTE).toString();
        nickname= bundle.getString(NICKNAME).toString();
        AHBottomNavigation bottomNavigation;
        bottomNavigation = (AHBottomNavigation) view.findViewById(R.id.bottom_navigation);
        //AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_maps_place, R.color.color_tab_1);

        media = new AHBottomNavigationItem("modify", R.drawable.insert,R.color.red);
        AHBottomNavigationItem insert = new AHBottomNavigationItem("media", R.drawable.media,R.color.red);
        AHBottomNavigationItem rec = new AHBottomNavigationItem("delete", R.drawable.delete,R.color.red);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#60b8fd"));
        bottomNavigation.setAccentColor(R.color.red);
        bottomNavigation.addItem(media);
        bottomNavigation.addItem(insert);
        bottomNavigation.addItem(rec);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    mListener.onChoice("modify",vBundle1);
                }
                if (position == 1) {
                    if(!filename.isEmpty()||!filename.equals(""))
                    {
                        String path=Environment.getExternalStorageDirectory().getAbsolutePath()+filename;
                        File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath()+filename);
                        if(!file.exists())
                        {
                            TransferObserver observer = MainActivity.transferUtility.download(
                                    "its-tsac",     /* The bucket to download from */
                                    filename,    /* The key for the object to download */
                                    file        /* The file to download the object to */
                            );
                            observer.getAbsoluteFilePath();
                            path= observer.getAbsoluteFilePath()+filename;
                        }
                        mp = new MediaPlayer();
                        try {
                            Log.d("Listening", "true");
                            mp.setDataSource(path);
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Errore durante la riproduzione", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "file non collegato", Toast.LENGTH_LONG).show();

                    }
                }
                if (position == 2) {
                    mListener.onChoice("delete",vBundle1);

                }
            }
        });
        bottomNavigation.setCurrentItem(-1);


        textView1 = (TextView)view.findViewById(R.id.textView1);
        textView2 = (TextView)view.findViewById(R.id.textView2);

       btn1 = (ImageButton)view.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChoice("share",vBundle1);

            }
        });

        try {
            DataForm(view);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return view;
    }
    public void DataForm( View view) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //start here
        RequestParams params = new RequestParams();
        params.put("nickname", nickname);
        params.put("Id_nota",notaSelected);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
        asyncHttpClient.post("http://rmdir.altervista.org/NoteDetail.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // If the response is JSONObject instead of expected JSONObject
                JSONObject note;
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    note = new JSONObject(jsonObject.get("data").toString());
                    dataGetted(note);
                } catch (Exception e) {
                    Log.d("errorre", e.getMessage());
                }
                Log.d("jsoon", "letto get");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mListener.onError();

            }
        });
        //end
    }
    public static void dataGetted(JSONObject note) throws JSONException {

        MainActivity.spinner.setVisibility(View.INVISIBLE);
        vBundle1.putString("nickname",nickname);
        vBundle1.putString("titolo",note.getString("Titolo"));
        vBundle1.putString("testo",note.getString("Testo_nota"));
        vBundle1.putString("id",note.getString("Id_nota"));
        filename = note.getString("Url_media");

        Log.d("debbuging",note.getString("Id_nota").toString()+nickname+note);
        textView1.setText("" + note.getString("Titolo"));
        textView2.setText("" + note.getString("Testo_nota"));


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity()instanceof IODetailFragment){
            mListener = (IODetailFragment)getActivity();

        }
    }
}
