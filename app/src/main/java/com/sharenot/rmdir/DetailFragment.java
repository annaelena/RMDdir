package com.sharenot.rmdir;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    public interface IODetailFragment{
        void onShare();
        void onChange();
        void onCancel();
        void onMedia();
    }

    Button btn1,btn2,btn3,btn4;
    TextView textView1,textView2;

    public static DetailFragment getInstance(){
        return new DetailFragment();
    }

    public IODetailFragment mListener = new IODetailFragment() {
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
    };


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        btn1 = (Button)view.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShare();

            }
        });

        btn2 = (Button)view.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChange();
            }
        });

        btn3 = (Button)view.findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMedia();
            }
        });

        btn4 = (Button)view.findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity()instanceof IODetailFragment){
            mListener = (IODetailFragment)getActivity();

        }
    }
}
