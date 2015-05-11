package com.lza.pad.helper;

import android.app.Activity;
import android.content.DialogInterface;

import com.lza.pad.R;

import lza.com.lza.library.util.Utility;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/9/15.
 */
public class DialogHelper {

    public static void showConfirmDialog(final Activity activity) {
        Utility.showDialog(activity,
                R.string.dialog_confirm_title,
                R.string.dialog_confirm_quit,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

}
