package com.sharenot.rmdir;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Inflater;

import cz.msebera.android.httpclient.Header;

public class GetLogIn extends Fragment {
    public JSONArray arrayGlobal;
    public MainActivity activity;
    public View mView;
    private IOLoginInterface mListener = new IOLoginInterface() {
        @Override
        public void getArray(JSONArray aArray) {
        }

        @Override
        public void setSession(String session) {
        }
    };


    public interface IOLoginInterface {
        void getArray(JSONArray aArray);

        void setSession(String session);
    }

    public GetLogIn() {

    }

    String session = "";

    public void LogInForm(String mail, String pwd, View view) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //start here
        RequestParams params = new RequestParams();
        params.put("email", mail);
        //vView = view;
       // EncrypterInSha512 Obj = new EncrypterInSha512();
        //pwd = Obj.GenerateHash(pwd);
        Log.d("pwd", pwd);
        params.put("password", pwd);
        //show
        HttpRest.post("public/v2/login", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONObject
                JSONObject event;
                try {

                    event = (JSONObject) response.get("data");
                    mListener.setSession(event.get("session").toString());
                    session = (event.get("session").toString());
                    Log.d("jsoon", "letto get" + session);
                   // DownloadData(session);
                } catch (Exception e) {
                    Log.d("errorr", e.getMessage());
                }
                Log.d("jsoon", "letto get");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("errorr", responseString + statusCode);
            }
        });
        //end
    }
}