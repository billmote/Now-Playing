package com.androidfu.foundation.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;

import com.androidfu.foundation.R;
import com.androidfu.foundation.util.Log;

import org.xml.sax.XMLReader;

import hugo.weaving.DebugLog;

/**
 * Created by bill.mote on 6/28/14.
 */
public class ReusableDialogFragment extends DialogFragment {

    public static final String TAG = ReusableDialogFragment.class.getSimpleName();

    public static final String KEY_BUNDLE_DIALOG_TITLE = "dialog_title";
    public static final String KEY_BUNDLE_DIALOG_MESSAGE = "dialog_message";
    public static final String KEY_BUNDLE_POSITIVE_BUTTON_LABEL = "positive_button_label";
    public static final String KEY_BUNDLE_NEUTRAL_BUTTON_LABEL = "neutral_button_label";
    public static final String KEY_BUNDLE_NEGATIVE_BUTTON_LABEL = "negative_button_label";
    public static final String KEY_BUNDLE_PATH_TO_ICON_FILE = "icon_path";

    private ReusableDialogListener mHost;

    @DebugLog
    public static ReusableDialogFragment newInstance(String dialogTitle, String dialogBodyText, String positiveButtonLabel, String neutralButtonLabel, String negativeButtonLabel, String iconFilePath) {
        Log.v(TAG, "newInstance()");
        ReusableDialogFragment reusableDialogFragment = new ReusableDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ReusableDialogFragment.KEY_BUNDLE_DIALOG_TITLE, dialogTitle);
        bundle.putString(ReusableDialogFragment.KEY_BUNDLE_DIALOG_MESSAGE, dialogBodyText);
        bundle.putString(ReusableDialogFragment.KEY_BUNDLE_POSITIVE_BUTTON_LABEL, positiveButtonLabel);
        bundle.putString(ReusableDialogFragment.KEY_BUNDLE_NEUTRAL_BUTTON_LABEL, neutralButtonLabel);
        bundle.putString(ReusableDialogFragment.KEY_BUNDLE_NEGATIVE_BUTTON_LABEL, negativeButtonLabel);
        bundle.putString(ReusableDialogFragment.KEY_BUNDLE_PATH_TO_ICON_FILE, iconFilePath);
        reusableDialogFragment.setArguments(bundle);
        return reusableDialogFragment;
    }

    @DebugLog
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = (ReusableDialogListener) activity;
    }

    @DebugLog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Bundle arguments = getArguments();
        String dialogTitle = arguments.getString(KEY_BUNDLE_DIALOG_TITLE);
        String dialogBodyText = arguments.getString(KEY_BUNDLE_DIALOG_MESSAGE);
        String positiveButtonLabel = arguments.getString(KEY_BUNDLE_POSITIVE_BUTTON_LABEL);
        String neutralButtonLabel = arguments.getString(KEY_BUNDLE_NEUTRAL_BUTTON_LABEL);
        String negativeButtonLabel = arguments.getString(KEY_BUNDLE_NEGATIVE_BUTTON_LABEL);

        /**
         * If we're not cancelable and we have no buttons we can get the app into a really bad state.  Let 'er die here.  This is a developer problem, not a user problem.
         */
        if (!this.isCancelable() && (positiveButtonLabel == null && neutralButtonLabel == null && negativeButtonLabel == null)) {
            throw new IllegalStateException("Dialog is not cancelable and we have no buttons.  Fail.");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(Html.fromHtml(dialogBodyText, new ImageGetter(), new TagHandler()));

        if (positiveButtonLabel != null) {
            Log.i(TAG, "Added a positive button to dialog.");
            alertDialogBuilder.setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mHost.handlePositiveResult();
                        }
                    }
            );
        }
        if (neutralButtonLabel != null) {
            Log.i(TAG, "Added a neutral button to dialog");
            alertDialogBuilder.setNeutralButton(neutralButtonLabel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mHost.handleNeutralResult();
                        }
                    }
            );
        }
        if (negativeButtonLabel != null) {
            Log.i(TAG, "Added a negative button to dialog");
            alertDialogBuilder.setNegativeButton(negativeButtonLabel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mHost.handleNegativeResult();
                        }
                    }
            );
        }
        return alertDialogBuilder.create();
    }

    private class TagHandler implements Html.TagHandler {

        @DebugLog
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            // Do something fancy?
        }
    }

    private class ImageGetter implements Html.ImageGetter {

        @DebugLog
        public Drawable getDrawable(String source) {
            int id;
            if (source.equals("hughjackman.jpg")) {
                id = R.drawable.ic_launcher;
            } else {
                return null;
            }

            Drawable d = getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    }

    @DebugLog
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //mHost.handleNeutralResult();
    }

    public interface ReusableDialogListener {

        /* what else comes back onActivityResult?  Might be more useful stuff. */

        @DebugLog
        void handlePositiveResult();

        @DebugLog
        void handleNeutralResult();

        @DebugLog
        void handleNegativeResult();

    }

}
