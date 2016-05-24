package com.app.rmdir.rmdir;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {
    private static final String LOGIN = "LoginFragment";
    private static final String REGISTRAZIONE = "Log btn Registrazione";
    private static final String SESSTAG = "session";
    public static final String DATALOGIN = "data della registrazione";
    private static final String url = "http://RMDIR.altervista.org/Register.php";
    private RegisterFragment.OnFragmentInteractionListener mListener;

    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        final EditText nick = (EditText)view.findViewById(R.id.input_nome);
        final EditText pwd = (EditText)view.findViewById(R.id.input_password);
        final EditText pwd_rpt = (EditText)view.findViewById(R.id.input_password_repeat);

        Button signin = (Button)view.findViewById(R.id.btn_signin);
        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(REGISTRAZIONE, "onRegisterClick: entered onClick ");

                String pwd1 = pwd.getText().toString();
                String pwd2 = pwd_rpt.getText().toString();

                if (pwd1.equals(pwd2) && !pwd1.equals("")) {
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-512");
                        if (md != null) {
                            md.update(pwd.getText().toString().getBytes());
                            byte byteData[] = md.digest();
                            String base64 = Base64.encodeToString(byteData, Base64.DEFAULT);
                            request(base64.replace("\n", ""), nick.getText().toString(), getActivity());  //getContext è API 23  //esegue la richiesta della chiave di sessione dopo aver criptato la pswd(?)
                        } else {
                            requestError();
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(getActivity(), "Le password non combaciano" , Toast.LENGTH_LONG).show();
                }



            }

        });

        TextView link = (TextView)view.findViewById(R.id.link_signin);
        link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Carico il LoginFragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                ft.replace(R.id.container, new LoginFragment() , LOGIN);
                ft.addToBackStack(null);
                ft.commit();


                /*
                fragmentManager.beginTransaction()
                        .replace(R.id.container, registerfragment , REGISTRAZIONE)
                        .commit(); */

            }
        });

        return view;

    }


    public void request(String pwd, final String nick, final Context context){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams requestParams = new RequestParams();
        requestParams.add("nickname", nick);
        requestParams.add("pwd", pwd);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Boolean success;
                try{
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Log.d("Prova", jsonObject.toString());
                    if((success = jsonObject.getBoolean("success"))){
                        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_LONG).show();
                        mListener.onFragmentInteraction(success, nick);
                     /*
                        Bundle bundle = new Bundle();
                        bundle.putString(SESSTAG, jsonObject.getJSONObject("data").getString("session"));
                        bundle.putString(DATALOGIN, jsonObject.getString("data"));
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().finish();
                        */


                    }else{
                        Toast.makeText(getActivity(), "Qualcosa è andato storto, riprova", Toast.LENGTH_LONG).show();
                        mListener.onFragmentInteraction(success, nick);
                    }
                }catch (JSONException e){
                    Toast.makeText(getActivity(), "Error on jsonObject", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error on connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void requestError() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Boolean success, String nickname);
    }
}
