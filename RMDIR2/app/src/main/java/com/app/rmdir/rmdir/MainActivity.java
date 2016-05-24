package com.app.rmdir.rmdir;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.auth.AWSBasicCognitoIdentityProvider;
import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements CreationFragment.creationFragmentInterface,DetailFragment.IODetailFragment,LoginFragment.OnFragmentInteractionListener ,NoteListFragment.IOStoreInterface, NavigationView.OnNavigationItemSelectedListener, LogOutDialog.IOButtonSelectedDialog, RegisterFragment.OnFragmentInteractionListener {


    JSONArray ListNote;
    private String nickname;
    static ImageButton openMenu;
    static TransferUtility transferUtility;
    public static ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openMenu= (ImageButton) findViewById(R.id.btn_menu);
        openMenu.setVisibility(View.INVISIBLE);
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                "840029f3-c8ed-4720-8490-5e010e6875e9",    /* Identity Pool ID */
                Regions.EU_WEST_1          /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3, getApplicationContext());
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment(), "lista")
                .commit();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        /*if(getFragmentManager().findFragmentByTag("login")==null)
        {
            FragmentTransaction vTr = getFragmentManager().beginTransaction();
            vTr.replace(R.id.container,LoginFragment,"login");
            vTr.commit();
        }*/





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mieNote){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            mostraNote();}
        else if (id == R.id.noteCondivise){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
            mostraNoteCondivise();}
        else if (id == R.id.modificaPwd) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            mostraNoteCondivise();}
        else if (id == R.id.LoggingOut) reloading();
        return true;
    }
    private void reloading() {
        LogOutDialog vDialog = LogOutDialog.getInstance();
        vDialog.show(getSupportFragmentManager(), "LogOut");
    }

    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    private void mostraNote() {
        getListNote(nickname);
    }
    private void mostraNoteCondivise() {
        getListNoteShared(nickname);
    }

   /* private void condividiapp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, sto usando questa applicazione per trovare i negozi+" +
                "\nScaricala anche tu: \nhttps://www.dropbox.com/s/ys3he23t0b4eah8/app-debug.apk?dl=0");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Condivici app"));
    }*/
    @Override
    public void onFragmentInteraction(Boolean success, String nickname) {
            if(success)
            {

                spinner.setVisibility(View.GONE);
                this.nickname=nickname;
                getListNote(nickname);
            }
            else
            {}

    }
    private void getListNoteShared(String nickname) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams requestParams = new RequestParams();
        requestParams.add("nickname", nickname);
        asyncHttpClient.post("http://rmdir.altervista.org/noteListShared.php", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    JSONArray ListNote= jsonObject.getJSONArray("data");
                    openListView(ListNote);


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


            }
        });
    }

    private void getListNote(String nickname) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams requestParams = new RequestParams();
        requestParams.add("nickname", nickname);
        asyncHttpClient.post("http://rmdir.altervista.org/noteList.php", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    if(openMenu.getVisibility()==View.INVISIBLE)
                        openMenu.setVisibility(View.VISIBLE);
                       ListNote= jsonObject.getJSONArray("data");
                        openListView(ListNote);


                    } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


            }
        });
    }



    @Override
    public void onButtonSelected(String aValue) {
        if(aValue.equals("+++")) {
            reload();
        }
    }
    private void openListView(JSONArray listNote) {
        NoteListFragment listFragment = NoteListFragment.getIstance(listNote);
        Log.d("leggendo nel main ", listNote.toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment, "lista").commit();
    }

    @Override
    public void getObj(int nota) {
        spinner.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,DetailFragment.getInstance(nota+"",nickname),"lista");
        fragmentTransaction.addToBackStack("lista");
        fragmentTransaction.commit();
    }

    @Override
    public void openNewNote() {

                getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CreationFragment.getIstance(nickname)).addToBackStack("lista")
                .commit();
    }
    private String m_Text = "";
    @Override
    public void onChoice(String aChoice, final Bundle vBundle1) {
        if(aChoice.equals("share"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Con chi vuoi condividere questa nota?");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            input.setTextColor(Color.rgb(210,210,210));
            input.setText("inserire un contatto");
            input.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    input.setText("");
                    input.setTextColor(Color.rgb(10,10,10));
                }
            });
            input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    ShareNote(m_Text,vBundle1.getString("id"));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
        else if(aChoice.equals("modify")) {
                    getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CreationFragment.getIstance(nickname,vBundle1)).addToBackStack("lista")
                    .commit();
        }
        else if(aChoice.equals("delete")) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
            RequestParams requestParams = new RequestParams();
            requestParams.add("id", vBundle1.getString("id"));
            requestParams.add("nickname", nickname);
            asyncHttpClient.post("http://rmdir.altervista.org/deleteNote.php", requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    JSONObject jsonObject = null;
                    try {
                        String mString = new String(responseBody);
                        jsonObject = new JSONObject(mString);
                        boolean mSuccess= jsonObject.getBoolean("success");
                        if (mSuccess)
                        {
                            Toast.makeText(getApplicationContext(), "nota rimossa con successo", Toast.LENGTH_SHORT).show();
                            getListNote(nickname);
                        }
                        else
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), "qualcosa è andato storto..", Toast.LENGTH_SHORT).show();


                }
            });
        }
        else if(aChoice.equals("media")){

        }
    }

    @Override
    public void onError() {
        spinner.setVisibility(View.GONE);
        getListNote(nickname);
    }

    private void ShareNote(final String aText,String aId) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams requestParams = new RequestParams();
        requestParams.add("nickname_cond", aText);
        requestParams.add("nickname", nickname);
        requestParams.add("share", aId);
        asyncHttpClient.post("http://rmdir.altervista.org/shareNote.php", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject jsonObject = null;
                try {
                    String mString = new String(responseBody);
                    jsonObject = new JSONObject(mString);
                    boolean mSuccess= jsonObject.getBoolean("success");
                    if (mSuccess)
                    Toast.makeText(getApplicationContext(), "nota condivisa con l'utente " + aText + " con successo", Toast.LENGTH_SHORT).show();
                    else
                    Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "qualcosa è andato storto..", Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onCreationInteraction(String aMedia) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+aMedia);
        TransferObserver observer = transferUtility.upload(
                "tsac-its",     /* The bucket to upload to */
                aMedia,     /* The key for the uploaded object */
                file        /* The file where the data to upload exists */
        );
        getListNote(nickname);

    }
}

