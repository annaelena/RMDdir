package com.app.rmdir.rmdir;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by carlo on 22/04/2016.
 */
public class LogOutDialog extends DialogFragment {
    public interface IOButtonSelectedDialog
    {
        void onButtonSelected(String aValue);
    }
    IOButtonSelectedDialog vListener = new IOButtonSelectedDialog() {
        @Override
        public void onButtonSelected(String aValue) {

        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getActivity() instanceof IOButtonSelectedDialog)
        {
            vListener=(IOButtonSelectedDialog) getActivity();
        }
    }

    public static LogOutDialog getInstance()
    {
        return new LogOutDialog();
    }
    @Override
    public Dialog onCreateDialog(Bundle vBundle)
    {
        AlertDialog.Builder vBuilder = new AlertDialog.Builder(getActivity());
        vBuilder.setMessage("sicuro di voler effettuare il logout?");
        vBuilder.setPositiveButton("si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vListener.onButtonSelected("+++");
            }
        });
        vBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vListener.onButtonSelected("---");
            }
        });
        return vBuilder.create();
    }

}
