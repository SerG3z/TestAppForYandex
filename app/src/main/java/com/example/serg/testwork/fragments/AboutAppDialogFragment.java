package com.example.serg.testwork.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import static com.example.serg.testwork.R.string.dialog_message;
import static com.example.serg.testwork.R.string.dialog_title;
import static com.example.serg.testwork.R.string.ok;

/**
 * Created by SerG3z on 03.08.16.
 */

public class AboutAppDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(dialog_title)
                .setNeutralButton(ok, null)
                .setMessage(dialog_message);
        return adb.create();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
