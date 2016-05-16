package com.sharenot.rmdir;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements DetailFragment.IODetailFragment{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,DetailFragment.getInstance(),"test");
        fragmentTransaction.commit();


    }

    @Override
    public void onShare() {

    }

    @Override
    public void onChange() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onMedia() {

    }
}
