package com.app.rmdir.rmdir;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements  LoginFragment.OnFragmentInteractionListener , NavigationView.OnNavigationItemSelectedListener, LogOutDialog.IOButtonSelectedDialog, RegisterFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment())
                .commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*if(getFragmentManager().findFragmentByTag("login")==null)
        {
            FragmentTransaction vTr = getFragmentManager().beginTransaction();
            vTr.replace(R.id.container,LoginFragment,"login");
            vTr.commit();
        }*/



        /*FloatingActionButton openmenu= (FloatingActionButton) findViewById(R.id.menu_opener);
        openmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });*/





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
        if (id == R.id.mieNote) Log.d("debugging","cose a caso");
        else if (id == R.id.noteCondivise) Log.d("debugging", "cose a caso");
        else if (id == R.id.modificaPwd) mostranegozi();
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
    private void mostranegozi() {/*
        Snackbar.make(this.findViewById(android.R.id.content), "Caricamento negozi", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        if(getFragmentManager().findFragmentByTag("lista")==null)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            StoreList= StoreFragment.getInstance(mArrayList);
            FragmentTransaction vTr = getFragmentManager().beginTransaction();
            vTr.replace(R.id.container, StoreList, "login");
            vTr.commit();
        }*/

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
            ListFragment listFragment = ListFragment.getIstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment, "lista").commit();
            }
            else
            {}

    }

    @Override
    public void onButtonSelected(String aValue) {
        if(aValue.equals("+++")) {
            reload();
        }
    }
}
